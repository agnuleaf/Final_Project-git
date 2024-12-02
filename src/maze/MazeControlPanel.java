package maze;

import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Queue;
import grid.GridDraw;
import grid.Grid;
import grid.GridPoint;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

import static maze.MazeApp.tPause;

/// Collects user input from mouse and keyboard events.
public class MazeControlPanel extends JPanel {


    static boolean isSimRunning;
    private static boolean restartBtn;
    GridPoint lastMousePosition = GridPoint.ZERO;
    Grid grid;
    GridDraw gridDraw;
    Draw draw;
    private static JButton btnUndo = new JButton("Undo");
    private final JToggleButton btnRun = new JToggleButton("Start");
    // canvas size
    final int DEFAULT_SIZE = 512;
    private int width = DEFAULT_SIZE; //384;
    private int height = DEFAULT_SIZE; //384;
    private int xmin = 0;
    private int ymin = 0;
    private int xmax;
    private int ymax;
    JLabel drawCanvas;

    MazeControlPanel(GridDraw gridDraw) {

        this.gridDraw = gridDraw;

        this.grid = gridDraw.getGrid();
        this.draw = gridDraw.getDraw();
        drawCanvas = draw.getJLabel();      // for including algs4.Draw canvas in a larger gui
        xmax = gridDraw.getTicks();
        ymax = xmax;


        setLayout(new GridLayout(1, 2, 0, 0));
        add(btnUndo);
        add(btnRun);


    }

    void control() {
            if(btnRun.isSelected()) {
            	// i dont think we need this anymore
                btnRun.setEnabled(false);
                btnRun.setEnabled(true);
            }
            drawCanvas.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (!isSimRunning) {

                        double x = userX(mouseEvent.getX()); // "borrowed" from algs4.Draw to convert mouse press locations
                        double y = userY(mouseEvent.getY()); //  to user friendly coordinates in the draw canvas
                        GridPoint p = new GridPoint(            // convert to nearest grid center
                                (int) (Math.floor(x) + 1.0),
                                (int) (Math.floor(y) + 1.0));
//                    printThreadDebug();
                        System.out.println(p);
                        if (grid.addEndpoint(p)) {
                            gridDraw.drawEndpoint(p);
                        } else if (grid.addWall(p)) {
                            gridDraw.drawWall(p);
                        }
                        lastMousePosition = p;
                        draw.show();
                        drawCanvas.repaint();
                    }
                }
            });

         /**   btnRun.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (grid.recentGridEndpoint() >= 2) {
                        btnRun.setEnabled(true);
                        System.out.println("btnRun enabled");
                    }
                }
            }); **/ // Note By Ty Greenburg: Dont really know what this is used for and didnt see any difference when i run with it and without it

            btnRun.addActionListener(e ->
            {
                if (!isSimRunning && grid.recentGridEndpoint() >= 2) {
                    isSimRunning = true;
                    runSimEDTrepaint(gridDraw.getPause());
                }
            });

            // allow undo when all endpoints placed and at least one wall is placed.
            btnUndo.addActionListener(e -> {
            	if(restartBtn) {
            		restartRunnable();
            		btnUndo.setText("Undo");
            		//TODO
            		restartBtn = false;
            	}
            	else
            		if (!isSimRunning) {
            			if (grid.recentGridEndpoint() >= 2 && grid.countWalls() > 0) {
            				GridPoint tmp = grid.removeLastWall();
            				if (tmp != null) {
            					gridDraw.eraseSquare(tmp);
            					gridDraw.mainFrame.repaint();
                        }
                    }
                }
            });

        // Delete the last wall placed with 'd'
        btnUndo.addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent ke){
                if (ke.getKeyCode() == KeyEvent.VK_D) {
                    if (grid.countWalls() > 0) {
                        GridPoint tmp = grid.removeLastWall();
                        if(tmp != null) gridDraw.eraseSquare(tmp);
                        gridDraw.mainFrame.repaint();
                    }
                }
            }
        });
        }

    /// The animation method for breadth-first search visualization. Starts a new `Thread` to sleep
    /// between calls to `repaint()`. Using a separate thread bypasses Swing's optimization of contiguous draw calls.
    /// Other options to animate may be a `Swing Timer` or `SwingWorker`.
    void runSimEDTrepaint(int pause) {
        tPause = pause;
        new Thread(() -> {
            BreadthFirstSearchView wavefront = new BreadthFirstSearchView(gridDraw);
            Queue<GridPoint> wave = wavefront.viewWave();
            Draw draw = gridDraw.getDraw();
            JFrame frame = gridDraw.getFrame();

            for (GridPoint q : wave) {
                gridDraw.discovered(q);
                SwingUtilities.invokeLater(() -> {
                    draw.show();
                    frame.repaint(); // or this.paintImmediately(this.getBounds());
                    draw.getJLabel().paintImmediately(this.getBounds());
                });
            }
            try {
                Thread.sleep(tPause);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Timer delay here
            Grid grid = gridDraw.getGrid();
            GridPoint p = grid.getStart();

            for (int v : wavefront.pathTo(grid.indexOf(grid.getEnd()))) {
                gridDraw.path(p, grid.pointAt(v), Color.RED);
                p = grid.pointAt(v);
                SwingUtilities.invokeLater(() -> {
                    draw.show();
                    frame.repaint();
                });
            }
            System.out.println("Sim Done");
            restartMaze();
            }).start();         
        
    }
    
    private static void restartMaze() {
    	btnUndo.setText("Restart");
    	restartBtn = true;
    }

    void printThreadDebug(){
        System.out.println("is EDT?: " +
                (javax.swing.SwingUtilities.isEventDispatchThread() ?
                        "T": "F\n\t"+ Thread.currentThread().getName()));
    }
    record GridSquare(GridPoint p ){

        public Rectangle getBounds(){
            return new Rectangle();
        }
    }
    public static void main(String[] args) {

    }
    public void setWidthHeight ( int width, int height){
        this.width = width;
        this.height = height;
    }
    public int getWidth() { return width;  }
    public int getHeight(){ return height; }
    
    /**
     * For the reset of the maze
     */
    private static void restartRunnable() {
    	isSimRunning = false;
    }


    // From algs4.Draw . Helpers to convert from native coordintes to user friendly ones.
    private double userX  (double x) { return xmin + x * (xmax - xmin) / width;    }
    private double userY  (double y) { return ymax - y * (ymax - ymin) / height;   }

}