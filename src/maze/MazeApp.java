package maze;


import edu.princeton.cs.algs4.Draw;
import grid.GridDraw;
import grid.Grid;

//import java.awt.;
import javax.swing.*;
import java.awt.*;

//import javax.util.*;


/// main class
public class MazeApp{



//	private static final double shiftGrid = -0.5;
	private final JFrame frame = new JFrame();

	private GridDraw gridDraw;    //  grid display with simple algs4.Draw methods
	private Grid grid;
	private int ticks = 15;
	private boolean isSlow;
	private MazeController controller;
	private final Color background = Color.DARK_GRAY;

	/// Maze gui app
	public MazeApp() {
		Draw draw =  new Draw();
		grid = new Grid(ticks);
		gridDraw = new GridDraw(ticks, draw);
		controller = new MazeController( grid, gridDraw);
		JLabel drawCanvas = controller.getDrawLabel();
		draw.enableDoubleBuffering();
		draw.setXscale(0, ticks);
		draw.setYscale(0, ticks);


		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(drawCanvas, BorderLayout.CENTER);

		contentPane.add(controller.getButtonPanel(), BorderLayout.SOUTH);

		frame.setTitle("Maze");
		frame.add(contentPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setContentPane(contentPane);
		frame.setVisible(true);
		contentPane.setVisible(true);

		gridDraw.grid();			// draw the representation of the grid on the drawing canvas
		draw.show();

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() ->{
			MazeApp app = new MazeApp();
		});
//		testSimAfterInput(app, app.grid, app.display);
	}
	//	private static void testSimAfterInput(MazeApp app, Grid grid, Display display) {
//
//		var p = new GridPoint(3, 3);
//		var q = new GridPoint(9, 8);
//
//		grid.addEndpoint(p);
//		display.drawEndpoint(p, true);
//
//		grid.addEndpoint(q);
//		display.drawEndpoint(q, false);
//
//		GridPoint[] walls = new GridPoint[]{
//				new GridPoint(1, 4),
//				new GridPoint(1, 3),
//				new GridPoint(1, 2),
//				new GridPoint(8, 6),
//				new GridPoint(7, 6),
//				new GridPoint(8, 7),
//				new GridPoint(8, 8),
//
//		};
//		for(GridPoint w : walls){
//			grid.addWall(w);
//			display.drawWall(w);
//		}
//
//		grid.buildGraph();
//
//		// Start threads for the wavefront of `BreadthFirstSearchView` and the path taken
//		app.handler.runSimulation(p, q);
//	}

}