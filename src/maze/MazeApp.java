package maze;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Queue;
import grid.GridDraw;
import grid.Grid;
import grid.GridPoint;

//import java.awt.;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 //SwingUtilities2;
import static javax.swing.SwingConstants.CENTER;

//import javax.util.*;

/// main class
public class MazeApp {

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
	/// Maze GUI Constructor
	public MazeApp() {

		frame = new JFrame();

		gridDraw	= new GridDraw(ticks, frame);
		grid = gridDraw.getGrid();
		gridDraw.setPause(5);
		gridDraw.drawEmptyGrid();
		draw = gridDraw.getDraw();

		drawCanvas = draw.getJLabel();
		pnlMain = new JPanel(new BorderLayout(), true);
		pnlMain.add(drawCanvas, BorderLayout.CENTER);
		instructions = new JLabel("place start & end points", CENTER);
		pnlMain.add(instructions, BorderLayout.NORTH);
		pnlControl = new MazeControlPanel( grid, gridDraw); //
		pnlMain.add(pnlControl,BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // algs4Draw uses DISPOSE instead
		frame.setTitle("Maze");
		frame.add(pnlMain);
		frame.setContentPane(pnlMain);
		frame.pack();
		frame.setVisible(true);
		pnlMain.setVisible(true);
		pnlMain.setOpaque(true);

		gridDraw.drawEmptyGrid();
		draw.show(); frame.repaint();


//		Timer timer = new Timer(100, new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				draw.show(); draw.getJLabel().repaint(); frame.repaint();
//			}
//		});
//
//		timer.setRepeats(true);  // sends one event
//		timer.start();
	}

	/// Expected to be ran after `viewWave` to view the path if it exists.
	/// @param pathToIfExists - Adds the path in order to the `Queue` if it exists
	/// @return true if a path exists, false otherwise
	public boolean viewPath(BreadthFirstPaths bfp, GridDraw gridDraw, Queue<GridPoint> pathToIfExists){
		Grid grid = gridDraw.getGrid();
		GridPoint p = grid.getStart();
		GridPoint q = grid.getEnd();
		if(!bfp.hasPathTo(grid.indexOf(q))){
			return false;
		}

		GridPoint prev = p;
		for (int step : bfp.pathTo(grid.indexOf(q))) {
			gridDraw.path(prev, grid.pointAt(step), Color.RED);
			gridDraw.getDraw().show();
			gridDraw.getDrawLabel().repaint();
			gridDraw.mainFrame.getContentPane().repaint();
			prev = grid.pointAt(step);
		}
		return true;
	}
	public static void main(String[] args) {
	SwingUtilities.invokeLater(() -> {
		MazeApp app = new MazeApp();
		app.pnlControl.control(); // run event handlers

	});

	}
}