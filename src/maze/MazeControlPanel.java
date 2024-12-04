package maze;

import edu.princeton.cs.algs4.*;
import grid.GridDraw;
import grid.Grid;
import grid.GridPoint;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.util.function.Supplier;

import static grid.GridPoint.distRectilinear;
import static java.awt.Color.PINK;
import static maze.MazeApp.AppMode.GAME;
import static maze.MazeApp.AppMode.DEMO;

/**
 * Collects user input from mouse and keyboard events.
 * @author Wesley Miller, Ty Greenburg
 */
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
    private int rounds = 0;
    private double coverage = 0.0;
    private final Queue<Integer> path = new Queue<>();
    private double density = 1.0;
    private boolean lastRunFailed;
    private int spacing;
 
    /**
     * Creates the control panel
     * @param gridDraw Display of the grid
     * @param instructions  for the display
     * @param mode of the display
     */
    MazeControlPanel(GridDraw gridDraw, JLabel instructions, MazeApp.AppMode mode) {
        this.gridDraw = gridDraw;
        this.instructions = instructions;
        this.grid = gridDraw.getGrid();
        this.draw = gridDraw.getDraw();
        this.mode = mode;
        drawCanvas = draw.getJLabel();      // for including algs4.Draw canvas in a larger gui
        xmax = gridDraw.getSquares();
        ymax = xmax;

        setLayout(new BorderLayout());
        btnUndo = new JButton("Undo");
        btnRun = new JButton("Run");
        spacing = gridDraw.getSquares()*gridDraw.getSquares() / 25;

        add(btnUndo, BorderLayout.WEST);
        add(btnRun, BorderLayout.CENTER);
        btnUndo.setFont(MazeApp.AppFont.LABEL.font);
        btnRun.setFont(MazeApp.AppFont.LABEL.font);
        System.out.println("mode "+ mode);
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
    
    /**
     * Instructions to the game
     */
    void gameInsructions(){
        instrInputA = ChallengeStart;
        instrInputB = ChallengeRun;
        instrRunning = ChallengeRun;
        instrRestart = instrInputA;
    }
    
    /**
     * Sets the mode of the app
     * @param mode of the app
     */
    void setMode(MazeApp.AppMode mode){
        this.mode = mode;
    }
    
    /**
     * Creates all the user interactions
     * @param mode of the app
     */
    void control(MazeApp.AppMode mode) {
        if(mode == GAME)
            gameInsructions();
        // user input for grid placement
        drawCanvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {

                if (!isVisualRunning  && !isRestartScreen) {
                    double x = userX(mouseEvent.getX()); // "borrowed" from algs4.Draw to convert mouse press locations
                    double y = userY(mouseEvent.getY()); //  to user friendly coordinates in the draw canvas
                    GridPoint p = new GridPoint(         //  convert to nearest grid center
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
                }else if(mode != DEMO && lastRunFailed || isRestartScreen){ // click anywhere to reset and continue
                    btnRun.doClick();
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
                    if(mode != DEMO ) {
                        MazeReset reset = (lastRunFailed ? MazeReset.CLEARALL : MazeReset.SAVE);
                        restartMaze(reset, mode);
                    }
                    else
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
                    else if(mode != DEMO && (grid.endpointsSize() <= 2 )){
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

        // Delete the last wall placed with 'U' key.
        btnUndo.addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent ke){
                if (btnUndo.getText().equals(labelUndo) && (ke.getKeyCode() == KeyEvent.VK_D) || (ke.getKeyCode() == KeyEvent.VK_U)) {
                    if (!isVisualRunning && (grid.countWalls() > 0) && !isRestartScreen) {
                        btnUndo.doClick();
                    }
                }
            }
        });
    }

    /**
     * removes the most recent point
     * @param remove last point
     * @return the point
     */
    GridPoint removeLast(Supplier<GridPoint> remove){
        GridPoint p = remove.get();
        if(p != null) {
            gridDraw.eraseSquare(p);
            gridDraw.mainFrame.repaint();
        }
        return p;
    };
    
    /**
     * Challenge mode endpoints must be in different quadrants.
     * @param p
     */
    private void challengePlaceEndpoints(GridPoint p) {
        String msgQuad = "Place in Different Quadrant. ";
        String msgTooClose = "Too close! Place " + spacing +" Apart";

        if (grid.endpointsSize() == 0 && grid.addEndpoint(p)) {
            gridDraw.drawEndpoint(p);
        } else if (grid.endpointsSize() == 1) { // check placement is correct
            boolean sameQuad = false;
            boolean tooClose = false;

            sameQuad = !grid.onDifferentQuads(p, grid.getStart());
            tooClose = areTooClose(p, grid.getStart());

        if (!sameQuad && !tooClose) { // ok to add
            if (grid.addEndpoint(p))
                gridDraw.drawEndpoint(p);
            btnRun.doClick();  // last placement launches pathfinding
        } else {
            instructions.setText((sameQuad ? msgQuad : "") + (tooClose ? msgTooClose : ""));
        }
    }
        }

    /**
     * Checks if two points and spaced too close for placement in Challenge mode.
     * @param p point 1
     * @param q point 2
     * @return true or false based on the space between
     */
    private boolean areTooClose(GridPoint p, GridPoint q){
        int space = (grid.getWidth() * grid.getHeight()) / 25;
        System.out.println("dist: "+  distRectilinear(p, q) + " space:"+ space);
        return distRectilinear(p, q) <= space;
    }

    /**
     * Helps restart the maze
     * @param reset
     * @param mode
     */
    private void restartMaze(MazeReset reset, MazeApp.AppMode mode) {

        btnUndo.setText(labelUndo);
        btnUndo.setVisible(true);
        btnRun.setText(labelRun);
        instructions.setText(instrInputA);
        gridDraw.drawEmptyGrid();

        if(mode == GAME){
            if(reset.areWallsSaved) { // convert path to walls
                pathToWalls(path);
            }
            else {
                rounds = 0;
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

    /**
     * for clarity
     */
    enum MazeReset{
        CLEARALL(false),
        SAVE(true);
        boolean areWallsSaved;
        MazeReset(boolean areWallsSaved){
            this.areWallsSaved = areWallsSaved;
        }
    }

    /**
     * The animation method for breadth-first search visualization. Starts a new `Thread` to bypasses Swing's
     * optimization by combining draw calls. `algs4.Draw` timer is used to add delay between frame.
     * @param pause - the length of time between animation updates
     */
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
                if(mode != DEMO) // only need 1 button in Game over
                    btnUndo.setVisible(false);
                lastRunFailed = true;
                visualComplete();
                return;
            }

            for (int v : wavefront.pathTo(grid.indexOf(grid.getEnd()))) {
                gridDraw.path(p, grid.pointAt(v), Color.RED);
                path.enqueue(v);
                p = grid.pointAt(v);
                draw.pause(gridDraw.getPause());
                draw.show();
                frame.repaint();
                draw.getJLabel().paintImmediately(this.getBounds());
            }
            rounds++;
            lastRunFailed = false;
            visualComplete();
            System.out.println("thread Sim complete");
        }).start();
    }
    
    /**
     * Runs when the visual is complete
     */
    private void visualComplete() {
    	isRestartScreen = true;
        btnUndo.setText(labelReset);
        btnUndo.setEnabled(true);
        btnRun.setText(labelContinue);
        btnRun.setEnabled(true);
        if(mode == DEMO)
            instructions.setText(instrRestart);
        else {
            scoreSession();
        }
        isVisualRunning = false;
    }

    /**
     * Scores the session
     */
    private void scoreSession() {
        double totalWalls = (double)grid.totalCountWalls();
        System.out.println(totalWalls);
        System.out.println(grid.getWidth() * grid.getHeight());
        coverage = ( totalWalls / grid.getWidth() * grid.getHeight() );
        String msgScore = String.format("rounds: %d |  ", rounds);
        String msgCoverage =  String.format(" %3.1f%% |", coverage);
        instructions.setText(msgScore + " " + msgCoverage + " " + (lastRunFailed ? " You Missed a Path!" : ""));

    }

    /**
     * Changes path to walls
     * @param path  path taken
     */
    void pathToWalls(Queue<Integer> path){
        if(path == null) return;
        while( !path.isEmpty()) {
            grid.addWall(grid.pointAt(path.dequeue()));
        }
    }

    /**
     * debug method used to helping find errors
     */
    void printThreadDebug(){
        System.out.println("is EDT?: " +
                (javax.swing.SwingUtilities.isEventDispatchThread() ?
                        "T": "F\n\t"+ Thread.currentThread().getName()));
    }

    /**
     * Sets width and height
     * @param width
     * @param height
     */
    public void setWidthHeight ( int width, int height){
        this.width = width;
        this.height = height;
    }

    /**
     * From algs4.Draw . Helpers to convert from native coordintes to user friendly ones.
     * @param x value
     * @return user friendly value
     */
    private double userX  (double x) { return xmin + x * (xmax - xmin) / width;    }
    /**
     * From algs4.Draw . Helpers to convert from native coordintes to user friendly ones.
     * @param y value
     * @return user friendly value
     */
    private double userY  (double y) { return ymax - y * (ymax - ymin) / height;   }

}