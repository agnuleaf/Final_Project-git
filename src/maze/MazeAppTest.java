package maze;

import grid.Grid;
import grid.GridDraw;
import grid.GridPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import static java.awt.event.MouseEvent.MOUSE_FIRST;

public class MazeAppTest {
    public static void main(String[] args) {
        MazeApp app = new MazeApp();
        app.pnlControl.control();
        testSim(app); // To test display only comment out to ignore listeners
    }

    private static void autoUI(MazeApp app){


    }
    public static void testSim( MazeApp app ) {

        var p = new GridPoint(3, 3);
        var q = new GridPoint(9, 8);
        JFrame mainFrame = app.frame;
        GridDraw gridDraw = app.gridDraw;
        MazeControlPanel ctrlPanel = app.pnlControl;
        Grid grid = gridDraw.getGrid();
        grid.addEndpoint(p);
        gridDraw.drawEndpoint(p);
        grid.addEndpoint(q);
        gridDraw.drawEndpoint(q);

        GridPoint[] walls = new GridPoint[]{
                new GridPoint(1, 4),
                new GridPoint(1, 3),
                new GridPoint(1, 2),
                new GridPoint(8, 6),
                new GridPoint(7, 6),
                new GridPoint(8, 7),
                new GridPoint(8, 8),
        };
        for(GridPoint w : walls){
            if(grid.addWall(w)) {
                gridDraw.drawWall(w);
            }
        }
        gridDraw.getDraw().show();
        gridDraw.getDrawLabel().repaint();
        mainFrame.repaint();
        // Start threads for the wavefront of `BreadthFirstSearchView` and the path taken
//		BreadthFirstSearchView wavefront = new BreadthFirstSearchView(/*p, q, grid,*/ gridDraw/*, frame*/);
//		wavefront.view();
    }
}