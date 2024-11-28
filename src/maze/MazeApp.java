package maze;


import grid.Display;
import grid.Grid;
import grid.GridPoint;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.DrawListener;

// TODO draw bfs wavefront, then the found path, then draw the other targeted search
//  what are strengths and weaknesses of each approach?
//      (When is it better to NOT create a path 'library' like all the algs4 algorithms
//      but instead exlporing along one single path with very basic 'guidance'
/// dummy main app. TODO can multiple threads allow simultaneously drawing of pathfinding algorithms??
public class MazeApp implements DrawListener {
	private int dim = 10;
	static Draw pane;		// algs4.Draw JFrame Object
	static Display display;	//  grid display with simple algs4.Draw methods

	private boolean mode = false;      // false is creating, true is destroying
	private boolean function = false;  // false is creating nodes, true is creating blockers
	private static GridPoint gridMouseClick;

	private static State state = State.INIT;
	private static int mouseClickX;
	private static int mouseClickY;

	/**
	 * Main App
	 */
	public MazeApp() {
		// Initialize `dim` x `dim` grid.
		Grid grid = new Grid(dim);        // generate grid in State.SIMULATE
		pane = Display.init(dim);
		pane.addListener(this);
//		testSP(grid);
	}

	private void testSP(Grid grid) {
		//From UI we receive Node[] endPoints, MinPQ<Node> walls.
		var p = new GridPoint(1, 1);
		var q = new GridPoint(7, 5);
		GridPoint[] endpoints = new GridPoint[]{
				p,          // start
				q           // finish
		};
		Display.drawCircles(endpoints, pane);
		// Grid Init complete ---
		BreadthFirstSearchView wavefront = new BreadthFirstSearchView(p, grid, pane);
		wavefront.run();
		// Start threads for the wavefront of `BreadthFirstSearchView` and the path taken
		BreadthFirstPathView pathView = new BreadthFirstPathView(p, q, grid, pane);
		pathView.run();
		var gst = new GridSearchTargeted(grid, pane); // TODO dumb algorithm doesn't even save the shortest path
		gst.searchWithBacktrack(p, q);
	}

	public static void main(String[] args) {
		new MazeApp();

	}


	enum State {
		INIT,
		USER_INPUT,
		SIMULATE,
		RESTART;

		State next() {
			State next;
			switch (state) {

				case INIT -> next = USER_INPUT;
				case USER_INPUT -> next = SIMULATE;
				case SIMULATE -> next = RESTART;
				case RESTART -> next = INIT;
				default -> throw new IllegalStateException("Unexpected value: " + state);
			}
			;
			return next;
		}
	}

	// if setup by user
	private static void gridPopulate(Draw pane) {
		if(state == State.USER_INPUT) {
			if (pane.isMousePressed()) {
				// Debugging: Print grid coordinates to ensure the mapping works
				// place node or wall;
			}
		}
	}

	/**
	 * Sets mouse x y
	 *
	 * @param x the x-coordinate of the mouse
	 * @param y the y-coordinate of the mouse
	 */
	public void mousePressed(double x, double y) {
		mouseClickX = (int) (Math.round(x * dim));
		mouseClickY = (int) (Math.round(y * dim));
		System.out.printf("(%3.2f %3.2f ) --> (%3d %3d)\tMouse Mapping", x, y, mouseClickX, mouseClickY);
	}

	/**
	 *  Changes state
	 */
	public void keyTyped(char c) {

		if(state == State.USER_INPUT){
			if(c == ' '){
				state = state.next();
			}
		}
		else if(state == State.RESTART){
			display.showMessage("Press SPC to clear and restart");
			if(c == ' '){
				// TODO draw new empty grid
				state = state.next();
			}
		}
	}
}
