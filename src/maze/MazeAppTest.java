package maze;

import edu.princeton.cs.algs4.Draw;
import grid.Grid;
import grid.GridDraw;
import grid.GridPoint;

import javax.swing.*;
import java.awt.*;

import static java.awt.FlowLayout.CENTER;

/// Test Class for the Maze gui app, drawing methods, and 2d grid operations.
public class MazeAppTest {
    // place test methods here
    public static void main(String[] args) {
    // quickStartA();
    //        testChallengeMode();
       testMazeApp();
    }

    // grid with predefined walls and endpoints
    private static void quickStartA() {
        MazeApp app = new MazeApp();
        app.setMode(MazeApp.AppMode.DEMO);
        app.pnlControl.control(MazeApp.AppMode.DEMO);
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
                new GridPoint(1, 1),
                new GridPoint(1, 4),
                new GridPoint(1, 3),
                new GridPoint(1, 2),
                new GridPoint(6, 1),
                new GridPoint(1, 6),
                new GridPoint(5, 1),
                new GridPoint(1, 5),
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
//        gridDraw.path(walls[0], walls[4], Color.GREEN);
//        System.out.println(walls[0] + " " + walls[4]+ " " +grid.onSameHalf(walls[0], walls[4]));
//
//        gridDraw.path(walls[4], walls[5], Color.YELLOW);
//        System.out.println(walls[4] + " " + walls[5]+ " " +grid.onSameHalf(walls[4], walls[5]));
//
//        gridDraw.path(walls[5], walls[6], Color.GRAY);
//        System.out.println(walls[5] + " " + walls[6]+ " " +grid.onSameHalf(walls[5], walls[6]));


        gridDraw.getDraw().show();
        gridDraw.getDrawLabel().repaint();
        mainFrame.repaint();
        // Start threads for the wavefront of `BreadthFirstSearchView` and the path taken
//		BreadthFirstSearchView wavefront = new BreadthFirstSearchView(/*p, q, grid,*/ gridDraw/*, frame*/);
//		wavefront.view();
    }

    // tests the intro dialog and full app
    private static void testMazeApp(){
        int m = selectMode();
        System.out.println(m);
        if (m == 1) {
            MazeApp app = new MazeApp(2,MazeApp.AppMode.GAME);
            app.setMode(MazeApp.AppMode.GAME);
            app.gameMode();
        }else{
            quickStartA();
        }
    }

    private static void testChallengeMode(){
        MazeApp app = new MazeApp(2,MazeApp.AppMode.GAME);
        app.setMode(MazeApp.AppMode.GAME);
        app.gameMode();
    }

    private static int selectMode(){
		JFrame frame = new JFrame("Select Mode");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JDialog dlgSelect = new JDialog(frame, "Select Mode");
        return  JOptionPane.showConfirmDialog(dlgSelect,
                "Demo Mode?",
                "Maze Builder",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
	}

    // For testing the graphics and drawing functions.
    void gridDrawTest(){
        // @formatter:off
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel(new FlowLayout(), true); // creates JPanel, sets DoubleBuffering and sets to opaque
        GridDraw gridDraw = new GridDraw(15, frame);
        Draw draw = gridDraw.getDraw();
        mainPanel.add(draw.getJLabel());
        mainPanel.setOpaque(true);
        frame.getContentPane().setLayout(new FlowLayout(CENTER, 8, 8));
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
        gridDraw.drawEmptyGrid();

        gridDraw.showMessage("Title Message");

        GridPoint[] wall = new GridPoint[]{
                new GridPoint ( 3 , 3 ),
                new GridPoint ( 7 , 6 ),
                new GridPoint ( 6 , 6 ),
                new GridPoint ( 6 , 7 ),
                new GridPoint ( 8 , 7 ),
                new GridPoint ( 8 , 8 ),
                new GridPoint ( 7 , 8 ),
                new GridPoint ( 7 , 7 ),
                new GridPoint ( 10, 10),
        };
        GridPoint[] known = new GridPoint[]{
                new GridPoint ( 6 , 5 ),
                new GridPoint ( 5 , 5 ), new GridPoint ( 5 , 4 ),  new GridPoint ( 5 , 6 ),
                new GridPoint ( 4 , 5 ), new GridPoint ( 4 , 4 ),  new GridPoint ( 4 , 6 ),
                new GridPoint ( 3 , 5 ), new GridPoint ( 3 , 4 ),  new GridPoint ( 3 , 6 ),
                new GridPoint ( 2 , 5 ),
        };
        for( GridPoint p : known ){   gridDraw.discovered(p, Color.PINK.brighter());  }
        GridPoint[] path = new GridPoint[]{
                new GridPoint ( 5 , 5 ),
                new GridPoint ( 4 , 5 ),
                new GridPoint ( 3 , 5 ),
                new GridPoint ( 3 , 6 ),
        };
        for(int i = 1; i < path.length ; i ++){
            gridDraw.path(path[i - 1], path[i], Color.RED.darker());
            draw.show();
            draw.getJLabel().repaint();

        }
        for( GridPoint p : wall  ){   gridDraw.drawWall(p);   }

        gridDraw.drawEndpoint( new GridPoint (1,1));
        gridDraw.drawEndpoint( new GridPoint (9,9));
        draw.getJLabel().repaint();



        for(int i = 0; i < 5; i++){
            gridDraw.eraseSquare(wall[i]);
            draw.getJLabel().repaint();
        }

        gridDraw.drawEndpoint( new GridPoint (1,1));
        gridDraw.drawEndpoint( new GridPoint (9,9));
        //        gridDraw.placeText(new GridPoint(1,5), "Left");
        //        gridDraw.placeText(new GridPoint(9,5), "Right");
        draw.show();
        frame.repaint();
    }

}
