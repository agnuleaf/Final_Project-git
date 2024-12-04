package maze;

import edu.princeton.cs.algs4.Draw;
import grid.GridDraw;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;

/**
 * Main App to run the interactive maze gui. Holds reference to the main `JFrame`
 * @author Wesley Miller, Ty Greenburg
 */
public class MazeApp {

	int sections = 2;	// multiples of 5 squares on the axis
	int tPause = 1;	// pause (ms) between draw 
	AppMode appMode = AppMode.DEMO;
	GridDraw gridDraw;		
	Draw draw  ;			
	MazeControlPanel pnlControl;	
	JLabel drawCanvas;				// algs4 Draw label
	JFrame frame;					// main JFrame
	JPanel pnlMain;					// contentPane holds the canvas, button panel and instructions label

	JLabel instructions;			// instructions label
	private static final Font fontLabel = new Font("Sans", Font.BOLD, 16);
	private static final Font fontTitle = new Font("Sans", Font.BOLD, 18);
	public enum AppFont{
		LABEL(MazeApp.fontLabel),
		TITLE(MazeApp.fontTitle);

		Font font;
		/**
		 * changes the font
		 * @param font 
		 */
		AppFont(Font font){
			this.font = font;
		}
	}
	/**
	 * Constructor for the mazeApp
	 * @param grids
	 * @param mode what mode was selected
	 */
	public MazeApp(int grids, AppMode mode){
			this(grids, 1, mode);
			setMode(mode);
			if(mode == AppMode.GAME){
				gameMode();
			}
	}

	/**
	 * Maze GUI Constructor with a 10x10 grid.
	 */
	public MazeApp() {
		this(2,10, AppMode.DEMO);
		setMode(AppMode.DEMO);
	}

	/**
	 * Constructor for the main Maze App
	 * @param sections - the number of 5-squares sections along an axis
	 * @param pause - ms pause between draw calls
	 * @param mode - demo or game mode
	 */
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
	
	void gameMode(){
		double density = 1.0;   // does not give variety
		gridDraw.setGridThickness(3);
		gridDraw.drawEmptyGrid();
		gridDraw.generateRandomWalls(density);
		gridDraw.getDraw().show();
		gridDraw.getDrawLabel().repaint();
	}

	/**
	 * Optional challenge mode:
	 *  - Walls are randomly generated for the first round.
	 *  - Each round the user must place a start and end point on opposing halves of the grid.
	 *  - Paths are converted into walls if a path exits.
	 * @return 0 for DEMO mode and 1 for CHALLENGE
	 */
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

	/**
	 * sets the mode to DEMO: demonstrate placement and pathfinding or
	 *  GAME: survive as many rounds and cover as many squares by placing endpoints
	 * @param appMode
	 */
	void setMode(AppMode appMode){
		this.appMode = appMode;
		this.pnlControl.setMode(appMode);
	}

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
	
	/**
	 * To launch the MazeApp
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			int m = startupDialog();
			System.out.println(m);
			MazeApp maze = new MazeApp(2, AppMode.getMode(m));
			System.out.println(maze.appMode);
			maze.pnlControl.control(maze.appMode); // run event handler
		});
	}

}