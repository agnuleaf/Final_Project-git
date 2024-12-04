package maze;

import edu.princeton.cs.algs4.Draw;
import grid.GridDraw;
import javax.swing.*;
import java.awt.*;
import static javax.swing.SwingConstants.CENTER;
/// Main App to run both the interactive breadth first search maze gui, and a minigame challenge.
/// Creates the `JFrame`, `MazeControlPanel` and runs on the Event Dispatch Thread.
/// @author Wesley Miller,  Ty Greenburg
public class MazeApp {

	int sections = 2;	// multiples of 5 squares on the axis
	int tPause = 1;	    // pause (ms) between draw . I think it is much slower than
	private AppMode appMode = AppMode.DEMO;

	GridDraw gridDraw;
	JFrame 	 frame;					// main JFrame
	MazeControlPanel pnlControl;

	private Draw    draw  ;			//
	private JLabel  drawCanvas;		// algs4 Draw label
	private JPanel  pnlMain;	    // contentPane holds the canvas, button panel and instructions label

	JLabel instructions;			// instructions label
	private static final Font fontLabel = new Font("Sans", Font.BOLD, 16);
	private static final Font fontTitle = new Font("Sans", Font.BOLD, 18);
	public enum AppFont{
		LABEL(MazeApp.fontLabel),
		TITLE(MazeApp.fontTitle);

		Font font;
		AppFont(Font font){
			this.font = font;
		}
	}
	/// Maze GUI constructor for declaring the mode and gridsize.
	public MazeApp(int grids, AppMode mode){
			this(grids, 1, mode);
			setMode(mode);
			if(mode == AppMode.GAME){
				gameMode();
			}
	}
	/// Maze GUI Constructor with a 10x10 grid with >10ms animation delay.
	public MazeApp() {
		this(2,10, AppMode.DEMO);
		setMode(AppMode.DEMO);
	}

	/// Constructor for the main Maze App.
	/// @param sections  the number of 5-squares sections along an axis
	/// @param pause  ms pause between draw calls
	/// @param mode  demo or game mode
	public MazeApp(int sections, int pause, AppMode mode){
		this.sections = sections;
		this.appMode = mode;
		this.tPause = pause;
		frame = new JFrame();
		gridDraw	= new GridDraw(sections * 5, frame);
		gridDraw.setPause(tPause);
		draw = gridDraw.getDraw();
		draw.setVisible(false);

		drawCanvas = draw.getJLabel();
		pnlMain = new JPanel(new BorderLayout(), true);
		pnlMain.add(drawCanvas, BorderLayout.CENTER);

		instructions = new JLabel("place start & end points", CENTER);
		instructions.setFont(AppFont.TITLE.font);
		pnlMain.add(instructions, BorderLayout.NORTH);
		pnlControl = new MazeControlPanel(gridDraw, instructions, appMode);
		pnlMain.add(pnlControl,BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // algs4Draw uses DISPOSE instead
		frame.setTitle("Maze " + mode.toString());
		frame.add(pnlMain);
		frame.setContentPane(pnlMain);
		frame.pack();
		frame.setVisible(true);
		pnlMain.setVisible(true);
		pnlMain.setOpaque(true);

		gridDraw.drawEmptyGrid();
		draw.show(); frame.repaint();
	}
	// initiates the game mode
	void gameMode(){
		double density = 0.4;   // does not give variety
		gridDraw.setGridThickness(3);
		gridDraw.drawEmptyGrid();
		gridDraw.generateRandomWalls(density);
		gridDraw.getDraw().show();
		gridDraw.getDrawLabel().repaint();
	}
	///  Startup dialog to choose Demo or not. Choosing NO results in the Game Mode:
	/// In Game Mode:
	/// - Walls are randomly generated at the start of the first round.
	/// - Each round the player places a start and end point on different quadrants of the grid.
	/// - Paths are converted into walls if a path exits.
	/// - Continue with the goal of maximum coverage until the all remaining paths are too short to be valid
	/// ( ~5 squares or more).
	/// @return 0 for DEMO mode and 1 for CHALLENGE
	static int startupDialog(){
		JFrame frame = new JFrame("Select Mode");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JDialog dlgSelect = new JDialog(frame, "Select Mode");
		return  JOptionPane.showConfirmDialog(dlgSelect,
				"Demo Mode?",
				"Maze Builder",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
	}
	// sets the mode to DEMO: demonstrate placement and pathfinding or
	// GAME: survive as many rounds and cover as many squares by placing endpoints
	void setMode(AppMode appMode){
		this.appMode = appMode;
		this.pnlControl.setMode(appMode);
	}

	/// Enum to dictate the maze mode. It is accessed by [MazeControlPanel] .
	public enum AppMode {
		DEMO(0),
		GAME(1);
		int mode;
		AppMode(int mode){
			this.mode = mode;
		}

		static AppMode getMode(int code){
			if(code == 0){
				return DEMO;
			}
			else return GAME;
		}
	}
	/// The EDT is launched here and all the maze logic. Animation occurs on a separate thread.
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			int m = startupDialog();
			int squares = m + 2;
			MazeApp maze = new MazeApp(squares, AppMode.getMode(m));
			maze.pnlControl.control(maze.appMode); // run event handler
		});
	}

}