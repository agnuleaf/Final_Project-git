package maze;

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

    static boolean isRestartScreen;
    static boolean isVisualRunning;
    private static boolean isBtnRestart;

    GridPoint lastMousePosition = GridPoint.ZERO;
    Grid grid;
    GridDraw gridDraw;
    Draw draw;
    JButton btnUndo;
    JButton btnRun;

    // canvas size
    final int DEFAULT_SIZE = 512;
    int width = DEFAULT_SIZE; //384;
    int height = DEFAULT_SIZE; //384;
    private int xmin = 0;
    private int ymin = 0;
    private int xmax;
    private int ymax;
    private final JLabel drawCanvas;
    private final JLabel instructions;



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
                if (!isVisualRunning || !isRestartScreen) {

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

                    lastMousePosition = p;
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
                    newThreadVisualization(gridDraw.getPause()); // new Thread, reenables buttons at it's end
                    btnRun.setText(labelContinue);
                    btnUndo.setText(labelReset);
                    System.out.println("btnRun Sim");
                    btnUndo.setEnabled(false);
                    btnRun.setEnabled(false);
                }
                else if(btnRun.getText().equals(labelContinue)){
                    instructions.setText(instrInputA);
                    grid.restart(true);
                    gridDraw.drawEmptyGrid();

                    for (int v : grid.getWalls()) {  // add last path
                        gridDraw.drawWall(grid.pointAt(v));
                    }
                    btnRun.setText(labelRun);
                    btnUndo.setText(labelUndo);
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
                    restartRunnable();

                    btnUndo.setText(labelUndo);
                    btnRun.setText(labelRun);
                    instructions.setText(instrInputA);
                    grid.restart(false);   // clears the grid
                    gridDraw.drawEmptyGrid();
                    draw.show();
                    drawCanvas.repaint();
                    isBtnRestart = false;
                }
            }
            });

        // Delete the last wall placed with 'd'
        btnUndo.addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent ke){
            if (btnUndo.getText().equals(labelUndo) && ke.getKeyCode() == KeyEvent.VK_D) {
                if (!isVisualRunning && !isRestartScreen && grid.countWalls() > 0) {
                    GridPoint tmp = grid.removeLastWall();
                    if(tmp != null) gridDraw.eraseSquare(tmp);
                    gridDraw.mainFrame.repaint();
                }
            }
            }
        });
    }

    /// The animation method for breadth-first search visualization. Starts a new `Thread` to bypasses Swing's
    /// optimization by combining draw calls. `algs4.Draw` timer is used to add delay between frame.
    /// @param pause - the length of time between animation updates
    void newThreadVisualization(int pause) {
        boolean[] pathFound = new boolean[1];   // FIXME
        Thread tSim = new Thread(() -> {
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
                pathFound[0] =false;
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
            pathFound[0] =true;
            System.out.println("Sim Done");
            visualComplete();
            btnRun.setEnabled(true);
            btnUndo.setEnabled(true);
            isVisualRunning = false;
        });

//        Thread tPostSim = new Thread(() ->{
//            System.out.println("Sim Done");
//            visualComplete();
//            btnRun.setEnabled(true);
//            btnUndo.setEnabled(true);
//            isVisualRunning = false;
//        });
        tSim.start();
//        try {
//
//            tPostSim.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        // These execute directly after the Thread starts . They should wait until the trhead ends.
        // maybe make new thread for them and join the two?

//            return pathFound[0];
    }
    
    private void visualComplete() {
    	isRestartScreen = true;
        isVisualRunning = false;
        btnUndo.setText("Reset");
        btnRun.setText("Continue");
        instructions.setText(instrRestart);
    	isBtnRestart = false;
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
    
    /**
     * For the reset of the maze
     */
    private void restartRunnable() {
    	isVisualRunning = false;
    }


    // From algs4.Draw . Helpers to convert from native coordintes to user friendly ones.
    private double userX  (double x) { return xmin + x * (xmax - xmin) / width;    }
    private double userY  (double y) { return ymax - y * (ymax - ymin) / height;   }

}