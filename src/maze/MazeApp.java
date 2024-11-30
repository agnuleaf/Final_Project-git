package maze;


import edu.princeton.cs.algs4.Draw;
import grid.GridDraw;
import grid.Grid;
import grid.GridPoint;

//import java.awt.;
import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;

//import javax.util.*;


/// main class
public class MazeApp{

	static int ticks = 10;	// number of squares on the axis
	static int tPause = 10;	// pause (ms) between draw updates
	Grid 			grid 		;//= new Grid(ticks);
	GridDraw 		gridDraw 	;//= new GridDraw(ticks);
	Draw 			draw  		;//= gridDraw.getDraw();
	MazeControlPanel pnlControl;
	JLabel drawCanvas;			// algs4 Draw label
	JFrame frame;//= new JFrame();
	JPanel pnlMain;
	JLabel instructions;
	private final Color background = Color.DARK_GRAY;
	/// Maze gui app
	public MazeApp() {

		frame = new JFrame();

		grid 		= new Grid(ticks);
		gridDraw	= new GridDraw(ticks);
		gridDraw.setPause(5);
		gridDraw.grid();
		draw = gridDraw.getDraw();
		draw.show(); frame.repaint();
		drawCanvas = draw.getJLabel();
		pnlMain = new JPanel(new BorderLayout());
		pnlMain.add(drawCanvas, BorderLayout.CENTER);
		instructions = new JLabel("place start & end points", CENTER);
		pnlMain.add(instructions, BorderLayout.NORTH);
		pnlControl = new MazeControlPanel( grid, gridDraw, frame);
		pnlMain.add(pnlControl,BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // algs4Draw uses DISPOSE instead
		frame.setTitle("Maze");
		draw  		= gridDraw.getDraw();
		frame.add(pnlMain);
		frame.setContentPane(pnlMain);
		frame.pack();
		frame.setVisible(true);
		pnlMain.setVisible(true);
//		pnlMain.setOpaque(true);


		pnlControl.control();

		draw.show(); frame.repaint();


	}

	public static void main(String[] args) {
		MazeApp app = new MazeApp();

	//testSim(app.frame, app.grid, app.gridDraw, app.pnlControl);
	}

		private static void testSim( JFrame frame, Grid grid, GridDraw gridDraw, MazeControlPanel controller) {


		var p = new GridPoint(3, 3);
		var q = new GridPoint(9, 8);

		grid.addEndpoint(p);
		gridDraw.drawEndpoint(p, true);
		gridDraw.getDraw().show();
		grid.addEndpoint(q);
		gridDraw.drawEndpoint(q, false);

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
			grid.addWall(w);
			gridDraw.drawWall(w);
			gridDraw.getDraw().show(); frame.repaint();
		}

		grid.buildGraph();
		frame.getContentPane();
		// Start threads for the wavefront of `BreadthFirstSearchView` and the path taken
			BreadthFirstSearchView wavefront = new BreadthFirstSearchView(p, q, grid, gridDraw, frame);
			wavefront.view();
	}
	void runSimulation (GridPoint p, GridPoint q){
		BreadthFirstSearchView wavefront = new BreadthFirstSearchView(p, q, grid, gridDraw, frame);
		wavefront.view();
		//		var gst = new GridSearchTargeted(grid, display); // TODO extract shortest path or find alternate algorithm
		//		gst.searchWithBacktrack(p, q)
	}

}