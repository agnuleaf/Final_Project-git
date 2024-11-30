package maze;



import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.DrawListener;
import grid.GridDraw;
import grid.Grid;
import grid.GridPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/// Collects user input from mouse and keyboard events.
public class MazeController implements ActionListener, DrawListener {


    private Draw draw = new Draw();        // algs4.Draw JFrame Object
    private final JLabel drawCanvas = draw.getJLabel(); // reference to algs4.Draw frame to compose inside main gui

    boolean isSimRunning;
    GridPoint lastMousePosition;
    Grid grid;
    GridDraw gridDraw;
    private final JPanel btnPanel = new JPanel();
    private final JButton btnUndo = new JButton("Undo");
    private final JButton btnRun  = new JButton("Start");
    // canvas size

    MazeController(Grid grid, GridDraw gridDraw) {
        this.grid = grid;
        this.gridDraw = gridDraw;

        btnPanel.setLayout(new GridLayout(1,2,0,0));
        btnPanel.add(btnUndo);
        btnPanel.add(btnRun);

        // init ==>  (gather user input) ==> (allow undos and run) ==> (

        btnUndo.setEnabled(false);
        btnRun.setEnabled(false);

        // when at least start and stop are placed, Run is enabled

        setBtnEnabledUndoAndRun(true);

        btnUndo.addActionListener(e -> {

        });

        // disable both buttons during simulation
        btnRun.addActionListener(e -> {
            btnRun.setEnabled(false);
            btnUndo.setEnabled(false);
        });

        drawCanvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me){
                if( !isSimRunning ) {

                    if (grid.getCountEndpoints() >= 2) {
                        setBtnEnabledUndoAndRun(true);          // activate both undo and run btn
                    } else if (grid.getCountEndpoints() < 2) {
                        btnRun.setEnabled(false);
                    } else if (grid.getCountEndpoints() < 1) {
                        setBtnEnabledUndoAndRun(false);
                    }

                    System.out.println(me.getX() + " " + me.getY());
                }
            }
        });


        // Delete the last wall placed with 'd'
        btnUndo.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_D) {
                    if(grid.getCountWalls() > 0 ) {
                        SwingUtilities.invokeLater(grid::removeLastWall);
                    }
                }
            }
        });
    }
    private void setBtnEnabledUndoAndRun(boolean isEnabled){
        btnRun.setEnabled(isEnabled);
        btnUndo.setEnabled(isEnabled);
    }
    private boolean areButtonsEnabled(){
        return btnRun.isEnabled() && btnUndo.isEnabled();
    }
    JPanel  getButtonPanel()     { return btnPanel; }

    Draw    getDraw()            { return draw; }

    JLabel  getDrawLabel()       { return drawCanvas; }



    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Undo")) {
            SwingUtilities.invokeLater(() -> {
               if(grid.getCountWalls() > 0 ){
                   grid.removeLastWall();
               }
            });
        }
        // disable mouse and button input during simulation
        if (e.getActionCommand().equals("Start") &&
                grid.getCountEndpoints() == 2) {
            GridPoint p = (grid.getEndpoints()).dequeue();
            GridPoint q = (grid.getEndpoints()).dequeue();

            SwingUtilities.invokeLater(() -> {
                isSimRunning = true;
                runSimulation(p, q);
                isSimRunning = false;
            });
        }

    }

    void runSimulation(GridPoint p, GridPoint q) {
        BreadthFirstSearchView wavefront = new BreadthFirstSearchView(p, q, grid, gridDraw);
        wavefront.view();
        //		var gst = new GridSearchTargeted(grid, display); // TODO extract shortest path or find alternate algorithm
        //		gst.searchWithBacktrack(p, q)
    }


    @Override
    public void mousePressed(double x, double y) {
        GridPoint p = new GridPoint(
                (int) (Math.floor(x) + 1.0),
                (int) (Math.floor(y) + 1.0));
        if (p.x() != lastMousePosition.x() && p.y() != lastMousePosition.y()
                    &&  (!grid.isWall(p) || !grid.isEndpoint(p))) {
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
            lastMousePosition = p;
        }
    }
}


