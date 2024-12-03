package maze;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Queue;
import grid.GridDraw;
import grid.Grid;
import grid.GridPoint;
import javax.swing.*;
import java.awt.Color;
import java.awt.GridLayout;

import java.awt.event.*;

//import static maze.MazeApp.tPause;

/// Collects user input from mouse and keyboard events.
public class MazeControlPanel extends JPanel {

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


    private BreadthFirstPaths path;
    /// Constructor for the control panel.
    MazeControlPanel(GridDraw gridDraw, JLabel instructions) {
        this.gridDraw = gridDraw;
        this.instructions = instructions;
        this.grid = gridDraw.getGrid();
        this.draw = gridDraw.getDraw();
        drawCanvas = draw.getJLabel();      // for including algs4.Draw canvas in a larger gui
        xmax = gridDraw.getTicks();
        ymax = xmax;

        btnUndo = new JButton("Undo");
        btnRun = new JButton("Run");
        setLayout(new GridLayout(1, 2, 0, 0));
        add(btnUndo);
        add(btnRun);
        btnUndo.setFont(MazeApp.AppFont.LABEL.font);
        btnRun.setFont(MazeApp.AppFont.LABEL.font);

    }

    final String instrInputA = "Place two endpoints";
    final String instrInputB = "Place Walls or Run";
    final String instrVis = "Breadth First Search Visualization";
    final String instrRestart = "Reset or Continue with Walls";
    final String labelUndo = "Undo";
    final String labelReset = "Reset";
    final String labelRun = "Run";
    final String labelContinue = "Continue";
    /// Runs program logic and handles user input. Runs on the EDT, except for the timer delay and draw calls for
    ///  animation.
    void control() {
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
                    if (grid.addEndpoint(p)) {
                        gridDraw.drawEndpoint(p);
                        if(grid.endpointsSize()==2){
                            instructions.setText(instrInputB);
                        }
                    } else if (grid.addWall(p)) {
                        gridDraw.drawWall(p);
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
                    instructions.setText(instrVis);
                    System.out.println("Thread start: btnRun ");
                    newThreadVisualization(gridDraw.getPause()); // new Thread, reenables buttons at it's end
                    btnRun.setText(labelContinue);
                    btnUndo.setText(labelReset);
                    btnUndo.setEnabled(false);
                    btnRun.setEnabled(false);
                }
                else if(btnRun.getText().equals(labelContinue)){
                    restartMaze(MazeReset.SAVE);


                    btnRun.setText(labelRun);
                    btnUndo.setText(labelUndo);
                    isRestartScreen = false;
                    draw.show();
                    drawCanvas.repaint();
                }
            }
        });

        // Undo previous recent placement or Clear and Restart after Visualization
        btnUndo.addActionListener(e -> {
            if(!isVisualRunning){
                if (btnUndo.getText().equals(labelUndo)){
                    if (grid.endpointsSize() >= 2 && grid.countWalls() > 0) {
                        GridPoint tmp = grid.removeLastWall();
                        if (tmp != null) {
                            gridDraw.eraseSquare(tmp);
                            gridDraw.mainFrame.repaint();
                        }
                    }
                } else if (btnUndo.getText().equals(labelReset)) {
                    restartMaze(MazeReset.CLEAR);

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
    // Common maze reset helper
    private void restartMaze(MazeReset reset) {

        btnUndo.setText(labelUndo);
        btnRun.setText(labelRun);
        instructions.setText(instrInputA);
        gridDraw.drawEmptyGrid();

        if(reset == MazeReset.HARD) {  // convert path to walls
            pathToWalls(path.pathTo(grid.indexOf(grid.getEnd())));
        }
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
        CLEAR(false,false),
        SAVE(true,false),
        HARD(true,true);
        boolean areWallsSaved;
        boolean arePathsConverted;
        MazeReset(boolean areWallsSaved, boolean arePathsConverted){
            this.areWallsSaved = areWallsSaved;
            this.arePathsConverted = arePathsConverted;
        }
    }
    /// The animation method for breadth-first search visualization. Starts a new `Thread` to bypasses Swing's
    /// optimization by combining draw calls. `algs4.Draw` timer is used to add delay between frame.
    /// @param pause - the length of time between animation updates
    void newThreadVisualization(int pause) {
        new Thread(() -> {
            BreadthFirstSearchView wavefront = new BreadthFirstSearchView(gridDraw);
            Queue<GridPoint> wave = wavefront.viewWave();

            Draw draw = gridDraw.getDraw();
            JFrame frame = gridDraw.getFrame();

            GridPoint p = grid.getStart();
            for (GridPoint q : wave) {
                gridDraw.discovered(q);
                draw.pause(gridDraw.getPause());
                draw.show();
                frame.repaint();
                draw.getJLabel().paintImmediately(this.getBounds());
            }
            if(wavefront.pathTo(grid.indexOf(grid.getEnd())) == null){
                gridDraw.showMessage("NO PATH FOUND!");
                draw.show(); frame.repaint();
                btnRun.setEnabled(true);
                btnUndo.setEnabled(true);
                isVisualRunning = false;
                return;
            //    todo draw message            gridDraw.
            }

//            for(GridPoint w : wave){
            for (int v : wavefront.pathTo(grid.indexOf(grid.getEnd()))) {
                gridDraw.path(p, grid.pointAt(v), Color.RED);
                p = grid.pointAt(v);
                draw.pause(gridDraw.getPause());
                draw.show();
                frame.repaint();
                draw.getJLabel().paintImmediately(this.getBounds());
            }
            visualComplete();
            System.out.println("thread Sim complete");
        }).start();
    }
    
    private void visualComplete() {
    	isRestartScreen = true;
        btnUndo.setText("Reset");
        btnUndo.setEnabled(true);
        btnRun.setText("Continue");
        btnRun.setEnabled(true);
        instructions.setText(instrRestart);
        isVisualRunning = false;
    }

    // Converts that last path to a wall, for a game challenge.
    void pathToWalls(Iterable<Integer> path){
        var iterator = path.iterator();
        if(iterator.hasNext()) iterator.next(); // ignore the first and last points
        while(iterator.hasNext()) {
            int p = iterator.next();
            if (iterator.hasNext()) {
                grid.addWall(grid.pointAt(p));
            }
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