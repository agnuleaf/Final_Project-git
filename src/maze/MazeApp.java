package maze;

// import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Draw;
import grid.GridDraw;

import javax.swing.*;
import java.awt.Color;
import java.awt.BorderLayout;
import static javax.swing.SwingConstants.CENTER;

/// Main App to run the interactive maze gui. Holds reference to the main `JFrame`, and
public class MazeApp {

	static int ticks = 10;	// number of squares on the axis
	static int tPause = 10;	// pause (ms) between draw updates

	GridDraw 		gridDraw;		//
	Draw 			draw  ;			//
	MazeControlPanel pnlControl;	//
	JLabel drawCanvas;				// algs4 Draw label
	JFrame frame;					// main JFrame
	JPanel pnlMain;					// contentPane holds the canvas, button panel and instructions label
	JLabel instructions;			// instructions label

	private final Color background = Color.DARK_GRAY;
	/// Maze GUI Constructor
	public MazeApp() {

		frame = new JFrame();

		gridDraw	= new GridDraw(ticks, frame);
//		grid = gridDraw.getGrid();
		gridDraw.setPause(tPause);
		gridDraw.drawEmptyGrid();
		gridDraw.setPause(5);
		draw = gridDraw.getDraw();
		draw.setVisible(false);

		drawCanvas = draw.getJLabel();
		pnlMain = new JPanel(new BorderLayout(), true);
		pnlMain.add(drawCanvas, BorderLayout.CENTER);
		instructions = new JLabel("place start & end points", CENTER);

		pnlMain.add(instructions, BorderLayout.NORTH);
		pnlControl = new MazeControlPanel(gridDraw); //
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
//
//	/// Expected to be ran after `viewWave` to view the path if it exists.
//	/// @param pathToIfExists - Adds the path in order to the `Queue` if it exists
//	/// @return true if a path exists, false otherwise
//	public boolean viewPath(BreadthFirstPaths bfp, GridDraw gridDraw, Queue<GridPoint> pathToIfExists){
//		Grid grid = gridDraw.getGrid();
//		GridPoint p = grid.getStart();
//		GridPoint q = grid.getEnd();
//		if(!bfp.hasPathTo(grid.indexOf(q))){
//			return false;
//		}
//
//		GridPoint prev = p;
//		for (int step : bfp.pathTo(grid.indexOf(q))) {
//			gridDraw.path(prev, grid.pointAt(step), Color.RED);
//			gridDraw.getDraw().show();
//			gridDraw.getDrawLabel().repaint();
//			gridDraw.mainFrame.getContentPane().repaint();
//			prev = grid.pointAt(step);
//		}
//		return true;
//	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MazeApp app = new MazeApp();
			app.pnlControl.control(); // run event handler
		});

	}
}