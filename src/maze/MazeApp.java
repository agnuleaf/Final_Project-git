package maze;

import edu.princeton.cs.algs4.Draw;
import grid.GridDraw;
import javax.swing.*;
import java.awt.BorderLayout;
import static javax.swing.SwingConstants.CENTER;

/// Main App to run the interactive maze gui. Holds reference to the main `JFrame`, and
///
public class MazeApp {

	static int ticks = 10;	// number of squares on the axis
	static int tPause = 10;	// pause (ms) between draw . I think it is much slower than
	GridDraw 		gridDraw;		//
	Draw 			draw  ;			//
	MazeControlPanel pnlControl;	//
	JLabel drawCanvas;				// algs4 Draw label
	JFrame frame;					// main JFrame
	JPanel pnlMain;					// contentPane holds the canvas, button panel and instructions label
	JLabel instructions;			// instructions label

	/// Maze GUI Constructor
	public MazeApp() {

		frame = new JFrame();

		gridDraw	= new GridDraw(ticks, frame);
		gridDraw.setPause(tPause);
		gridDraw.drawEmptyGrid();
		draw = gridDraw.getDraw();
		draw.setVisible(false);

		drawCanvas = draw.getJLabel();
		pnlMain = new JPanel(new BorderLayout(), true);
		pnlMain.add(drawCanvas, BorderLayout.CENTER);
		instructions = new JLabel("place start & end points", CENTER);
		pnlMain.add(instructions, BorderLayout.NORTH);
		pnlControl = new MazeControlPanel(gridDraw, instructions); //
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
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MazeApp app = new MazeApp();
			app.pnlControl.control(); // run event handler
		});

	}
}