package maze;



import edu.princeton.cs.algs4.Draw;
import grid.GridDraw;
import grid.Grid;
import grid.GridPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/// Collects user input from mouse and keyboard events.
public class MazeControlPanel extends JPanel {


    boolean isSimRunning;
    GridPoint lastMousePosition = GridPoint.ZERO;
    Grid grid;
    GridDraw gridDraw;
    Draw draw;
    JFrame frame;
    private final JButton btnUndo = new JButton("Undo");
    private final JButton btnRun = new JButton("Start");
    // canvas size
    final int DEFAULT_SIZE = 512;
    private int width = DEFAULT_SIZE; //384;
    private int height = DEFAULT_SIZE; //384;
    private int xmin = 0;
    private int ymin = 0;
    private int xmax;
    private int ymax;
    JLabel drawCanvas;
    MazeControlPanel(Grid grid, GridDraw gridDraw, JFrame frame) {
        this.frame = frame;
        this.grid = grid;
        this.gridDraw = gridDraw;
        this.draw = gridDraw.getDraw();
        drawCanvas = draw.getJLabel(); // reference to algs4.Draw
        xmax = gridDraw.getTicks();
        ymax = xmax;

        setLayout(new GridLayout(1, 2, 0, 0));
        add(btnUndo);
        add(btnRun);
        setEnableUndoAndRun(false);

    }

    void control(){
    // disable both buttons during simulation
            btnRun.addActionListener(e ->
        {
            if (!isSimRunning && grid.getCountEndpoints() >= 2) {
                btnRun.setEnabled(false);
                btnUndo.setEnabled(false);
                GridPoint p = (grid.getEndpoints()).dequeue();
                GridPoint q = (grid.getEndpoints()).dequeue();
                isSimRunning = true;
                System.out.println("PLACEHOLDER FOR SIMULATION");
                gridDraw.showMessage("SIMULATION");
                gridDraw.getDraw().show();
                drawCanvas.repaint();
//                gridDraw.getDraw().pause(500);

//                runSimulation(p, q);
                isSimRunning = false;
            }
        });

            drawCanvas.addMouseListener(new

        MouseAdapter() {
            public void mouseClicked (MouseEvent mouseEvent){
            if (!isSimRunning) {
                if (grid.getCountEndpoints() >= 2) {
                    setEnableUndoAndRun(true);          // activate both undo and run btn
                } else if (grid.getCountEndpoints() < 2) {
                    btnRun.setEnabled(false);
                } else if (grid.getCountEndpoints() < 1) setEnableUndoAndRun(false);
                double x = userX(mouseEvent.getX());
                double y = userY(mouseEvent.getY());

                GridPoint p = new GridPoint(
                        (int) (Math.floor(x) + 1.0),
                        (int) (Math.floor(y) + 1.0));
                printThreadDebug();
                System.out.println(p);
                if (/*p.x() != lastMousePosition.x() && p.y() != lastMousePosition.y() &&*/
                         (!grid.isWall(p) || !grid.isEndpoint(p))) {
                    if (grid.getCountEndpoints() == 0) {
                        grid.addEndpoint(p);
                        gridDraw.drawEndpoint(p, true);
                    } else if (grid.getCountEndpoints() == 1) {
                        grid.addEndpoint(p);
                        gridDraw.drawEndpoint(p, false);
                    } else {
                        grid.addWall(p);
                        gridDraw.drawWall(p);
                    }

                }
                // for debug or to inform the user somehow
                else if(/*p.x() != lastMousePosition.x() && p.y() != lastMousePosition.y() &&*/
                        (grid.isWall(p))){
                    System.out.println("Wall is already placed here");
                } else if(/*p.x() != lastMousePosition.x() && p.y() != lastMousePosition.y() &&*/
                        grid.isEndpoint(p)){
                System.out.println("Endpoint is already placed here");
                }
                lastMousePosition = p;
                draw.show();
                drawCanvas.repaint();
                }
            }
        });

            btnUndo.addActionListener(e ->

        {
            if (!isSimRunning) {
                if (grid.getCountWalls() > 0) {
                    gridDraw.eraseSquare(grid.removeLastWall());
                    if (grid.getCountWalls() == 2)
                        btnUndo.setEnabled(false);
                }
            }
            });

        // Delete the last wall placed with 'd'
            btnUndo.addKeyListener(new

        KeyAdapter() {
            public void keyPressed (KeyEvent ke){
                if (ke.getKeyCode() == KeyEvent.VK_D) {
                    if (grid.getCountWalls() > 0) {
                        gridDraw.eraseSquare(grid.removeLastWall());
                        if (grid.getCountWalls() == 2)
                            btnUndo.setEnabled(false);
                    }
                }
            }
        });
    }

    private void setEnableUndoAndRun(boolean isEnabled){
        btnRun.setEnabled(isEnabled);
        btnUndo.setEnabled(isEnabled);
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
    public int getWidth() { return width;  }
    public int getHeight(){ return height; }

    // From algs4.Draw . Helpers to convert from native coordintes to user friendly ones.
    private double userX ( double x){
        return xmin + x * (xmax - xmin) / width;
    }
    private double userY ( double y){
        return ymax - y * (ymax - ymin) / height;
    }


}