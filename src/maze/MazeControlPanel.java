package maze;

import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Queue;
import grid.GridDraw;
import grid.Grid;
import grid.GridPoint;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;

import static grid.GridPoint.distRectilinear;
import static java.awt.Color.PINK;
import static java.lang.Math.max;
import static maze.MazeApp.AppMode.GAME;
import static maze.MazeApp.AppMode.DEMO;
//import static maze.MazeApp.tPause;

/// Collects user input from mouse and keyboard events.
public class MazeControlPanel extends JPanel {

    // state variables
    private static boolean isRestartScreen;
    private static boolean isVisualRunning;

    Grid grid;
    GridDraw gridDraw;
    Draw draw;
    JButton btnUndo;
    JButton btnRun;
    private final JLabel drawCanvas;
    private final JLabel instructions;

    // canvas variables
    final int DEFAULT_SIZE = 512;
    int width = DEFAULT_SIZE; //384;
    int height = DEFAULT_SIZE; //384;
    private int xmin = 0;
    private int ymin = 0;
    private int xmax;
    private int ymax;

    // challenge mode
    private MazeApp.AppMode mode;
    private int topScore = 0;
    private double topCoverage = 0.0;
    private int score = 0;
    private double coverage = 0.0;
    private JLabel lblScore;
    private final Queue<Integer> path = new Queue<>();
    private double density = 1.0;

    /// Constructor for the control panel.
    MazeControlPanel(GridDraw gridDraw, JLabel instructions, MazeApp.AppMode mode) {
        this.gridDraw = gridDraw;
        this.instructions = instructions;
        this.grid = gridDraw.getGrid();
        this.draw = gridDraw.getDraw();
        drawCanvas = draw.getJLabel();      // for including algs4.Draw canvas in a larger gui
        xmax = gridDraw.getSquares();
        ymax = xmax;

        setLayout(new BorderLayout());
        btnUndo = new JButton("Undo");
        btnRun = new JButton("Run");
        if(mode == GAME){
            lblScore = new JLabel(" 0");
            lblScore.setFont(MazeApp.AppFont.TITLE.font);
            add(lblScore,BorderLayout.EAST);
        }

        add(btnUndo, BorderLayout.WEST);
        add(btnRun, BorderLayout.CENTER);
        btnUndo.setFont(MazeApp.AppFont.LABEL.font);
        btnRun.setFont(MazeApp.AppFont.LABEL.font);

    }

    String instrInputA = "Place two endpoints";
    String instrInputB = "Place Walls or Run";
    String instrRunning = "Breadth First Search Visualization";
    String instrRestart = "Reset or Continue with Walls";
    String labelUndo = "Undo";
    String labelReset = "Reset";
    String labelRun = "Run";
    String labelContinue = "Continue";

    String ChallengeStart = "Paths Turn to Walls";
    String ChallengeRun = "Finding Path";

    void challengeLabels(){
        instrInputA = ChallengeStart;
        instrInputB = ChallengeRun;
        instrRunning = ChallengeRun;
        instrRestart = instrInputA;
    }
    void setMode(MazeApp.AppMode mode){
        this.mode = mode;
    }
    /// Runs program logic and handles user input.
    void control(MazeApp.AppMode mode) {
        if(mode == GAME)
            challengeLabels();
        // user input for grid placement
        drawCanvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {

                if (!isVisualRunning  && !isRestartScreen) {
                    double x = userX(mouseEvent.getX()); // "borrowed" from algs4.Draw to convert mouse press locations
                    double y = userY(mouseEvent.getY()); //  to user friendly coordinates in the draw canvas
                    GridPoint p = new GridPoint(            // convert to nearest grid center
                            (int) (Math.floor(x) + 1.0),
                            (int) (Math.floor(y) + 1.0));
                    System.out.println(p);
                    if(mode != GAME){
                    if (grid.addEndpoint(p)) {
                        gridDraw.drawEndpoint(p);
                        if(grid.endpointsSize() == 2){
                            instructions.setText(instrInputB);
                        }
                    } else if (grid.addWall(p) ) {
                        gridDraw.drawWall(p);
                    }
                    } else {
                        challengePlaceEndpoints(p);
                    }
                    draw.show();
                    drawCanvas.repaint();
                }
            }
        });

        // Run Visualization and Continue with the same walls

        btnRun.addActionListener(e ->  {
            if(!isVisualRunning) {
                if (btnRun.getText().equals(labelRun) && grid.endpointsSize() >= 2){
                    isVisualRunning = true;
                    instructions.setText(instrRunning);

                    newThreadVisualization(gridDraw.getPause()); // new Thread, reenables buttons at it's end
                    btnRun.setText(labelContinue);
                    btnUndo.setText(labelReset);
                    btnUndo.setEnabled(false);
                    btnRun.setEnabled(false);
                }
                else if(btnRun.getText().equals(labelContinue)){
                        restartMaze(MazeReset.SAVE, mode);
                }
            }
        });

        // Undo previous recent placement or Clear and Restart after Visualization
        btnUndo.addActionListener(e -> {
            if(!isVisualRunning){
                if (btnUndo.getText().equals(labelUndo)){
                    if ((grid.endpointsSize() >= 2) && (grid.countWalls() > 0)) {
                        GridPoint tmp = grid.removeLastWall();
                        if (tmp != null) {
                            gridDraw.eraseSquare(tmp);
                            gridDraw.mainFrame.repaint();
                        }
                    }
                    else if(grid.endpointsSize() <= 2 ){
                        GridPoint tmp = grid.removeLastEndpoint();
                        if(tmp != null) {
                            gridDraw.eraseSquare(tmp);
                            gridDraw.mainFrame.repaint();
                        }
                    }
                } else if (btnUndo.getText().equals(labelReset)) {
                    restartMaze(MazeReset.CLEARALL, mode);
                }
            }
            });


        // Delete the last wall placed with 'd' key.
        btnUndo.addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent ke){
                if (btnUndo.getText().equals(labelUndo) && ke.getKeyCode() == KeyEvent.VK_D) {
                    if (!isVisualRunning && (grid.countWalls() > 0) && !isRestartScreen) {
                        GridPoint tmp = grid.removeLastWall();
                        if(tmp != null) gridDraw.eraseSquare(tmp);
                        gridDraw.mainFrame.repaint();
                    }
                }
            }
        });
    }

    // Challenge mode endpoints must be in different quadrants.
    private void challengePlaceEndpoints(GridPoint p) {
        if (grid.endpointsSize() == 0 && grid.addEndpoint(p)) {
            gridDraw.drawEndpoint(p);
        } else if (grid.endpointsSize() == 1) {
            boolean sameQuad = false;
            boolean tooClose = false;
            if (grid.onSameQuad(p, grid.getStart())) {
                System.out.println(p+"  same quadrant " + grid.getStart());
                sameQuad = true;
            } else if (areTooClose(p, grid.getStart())) {
                tooClose = true;
                int space = (grid.getWidth() * grid.getHeight()) / 25;
                System.out.println(p + " within " + space +" from " + grid.getStart());
            }
            if (!sameQuad && !tooClose) { // ok to add
                if(grid.addEndpoint(p))
                    gridDraw.drawEndpoint(p);
                btnRun.doClick();  // last placement launches pathfinding
            }
        }
    }
    // Checks if two points and spaced too close for placement in Challenge mode.
    private boolean areTooClose(GridPoint p, GridPoint q){
        int space = (grid.getWidth() * grid.getHeight()) / 25;
        System.out.println("dist: "+  (int)distRectilinear(p, q) + " space:"+ space);
        return (int)distRectilinear(p, q) <= space;
    }
    private void updateScore(){

    }

    // Common maze reset helper
    private void restartMaze(MazeReset reset, MazeApp.AppMode mode) {

        btnUndo.setText(labelUndo);
        btnRun.setText(labelRun);
        instructions.setText(instrInputA);
        gridDraw.drawEmptyGrid();

        if(mode == GAME){
            if(reset.areWallsSaved) { // convert path to walls
                pathToWalls(path);
            }
            else {
                score = 0;
                grid.restart(false);  // clear grid memory
                gridDraw.generateRandomWalls(density); // generate and draw random walls
            }
        }
        if(mode == DEMO || reset.areWallsSaved)
            grid.restart(reset.areWallsSaved);   // clears or saves the grid memory

        if(reset.areWallsSaved){
            for (int v : grid.getWalls()) {  // add last path
                gridDraw.drawWall(grid.pointAt(v));
            }
        }

        draw.show();
        drawCanvas.repaint();
        isRestartScreen = false;
        isVisualRunning = false;

    }
    // for clarity
    enum MazeReset{
        CLEARALL(false),
        SAVE(true);
        boolean areWallsSaved;
        MazeReset(boolean areWallsSaved){
            this.areWallsSaved = areWallsSaved;
        }
    }

    /// The animation method for breadth-first search visualization. Starts a new `Thread` to bypasses Swing's
    /// optimization by combining draw calls. `algs4.Draw` timer is used to add delay between frame.
    /// @param pause - the length of time between animation updates
    void newThreadVisualization(int pause) {
        new Thread(() -> {
            BreadthFirstSearchView wavefront = new BreadthFirstSearchView(gridDraw);
            Queue<GridPoint> bfsPaths = wavefront.viewWave();

            Draw draw = gridDraw.getDraw();
            JFrame frame = gridDraw.getFrame();
            int batch = 1; int i = 0;
            GridPoint p = grid.getStart();
            if(mode != DEMO) { // faster visuals
                batch = 5;
            }
            for (GridPoint q : bfsPaths) {
                gridDraw.discovered(q, PINK);
                if(i++ % batch == 0) {
                    draw.pause(gridDraw.getPause());
                    draw.show();
                    frame.repaint();
                    draw.getJLabel().paintImmediately(this.getBounds());
                }
            }
            if(wavefront.pathTo(grid.indexOf(grid.getEnd())) == null){
                gridDraw.showMessage("NO PATH FOUND!");
                draw.show(); frame.repaint();
                draw.pause(1000);
                visualComplete();
                return;
            }

//            for(GridPoint w : wave){
            for (int v : wavefront.pathTo(grid.indexOf(grid.getEnd()))) {
                gridDraw.path(p, grid.pointAt(v), Color.RED);
                path.enqueue(v);
                p = grid.pointAt(v);
                draw.pause(gridDraw.getPause());
                draw.show();
                frame.repaint();
                draw.getJLabel().paintImmediately(this.getBounds());
            }
            score++;
            visualComplete();
            System.out.println("thread Sim complete");
        }).start();
    }
    
    private void visualComplete() {
    	isRestartScreen = true;
        btnUndo.setText(labelReset);
        btnUndo.setEnabled(true);
        btnRun.setText(labelContinue);
        btnRun.setEnabled(true);
        if(mode == DEMO)
            instructions.setText(instrRestart);
        else {
            topScore = max(topScore, score);
            String msgScore = (((score > topScore)? "Top Score! " : "Score: ") + score);
            double totalWalls = (double)grid.totalCountWalls();
            System.out.println(totalWalls);
            System.out.println(grid.getWidth()*grid.getHeight());
            coverage = ( totalWalls / grid.getWidth()*grid.getHeight() );
            String.format("%s", msgScore);
            String.format("%3.3f", coverage);
            double tmp = topCoverage;
            topCoverage = max(topCoverage, coverage);
            String msgCoverage = (((topCoverage - tmp) > 0.0001)? "Top Coverage! ": "Coverage ") + coverage + "%";
            instructions.setText(msgScore + " " +msgCoverage);
            draw.pause(1000);
        }
        isVisualRunning = false;
    }

    // Converts that last path to a wall, for a game challenge.
    void pathToWalls(Queue<Integer> path){
        if(path == null) return;

        while( !path.isEmpty()) {
            grid.addWall(grid.pointAt(path.dequeue()));
        }
    }

    void printThreadDebug(){
        System.out.println("is EDT?: " +
                (javax.swing.SwingUtilities.isEventDispatchThread() ?
                        "T": "F\n\t"+ Thread.currentThread().getName()));
    }

    public void setWidthHeight ( int width, int height){
        this.width = width;
        this.height = height;
    }
//    public int getWidth() { return width;  }
//    public int getHeight(){ return height; }
    


    // From algs4.Draw . Helpers to convert from native coordintes to user friendly ones.
    private double userX  (double x) { return xmin + x * (xmax - xmin) / width;    }
    private double userY  (double y) { return ymax - y * (ymax - ymin) / height;   }

}