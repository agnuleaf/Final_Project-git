package maze;


import grid.Display;
import grid.Grid;
import grid.GridPoint;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.DrawListener;

import java.awt.*;

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

	private static AppState state = AppState.INIT;
	private static UIState uistate = UIState.START;

	/**
	 * Main App
	 */
	public MazeApp() {
		// Initialize `dim` x `dim` grid.
		Grid grid = new Grid(dim);        // generate grid in State.SIMULATE
		Display display = new Display(dim ,1);
		Draw pane = display.getPane();
		display.grid();
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
	enum UIState {
		START,
		FINISH,
		WALL,
	}

	enum AppState {
		INIT,
		USER_INPUT,
		SIMULATE,
		RESTART;

		AppState next() {
			AppState next;
			switch (state) {
				case INIT 		-> next = USER_INPUT;
				case USER_INPUT -> next = SIMULATE;
				case SIMULATE 	-> next = RESTART;
				case RESTART 	-> next = INIT;
				default 		-> throw new IllegalStateException("Unexpected value: " + state);
			}
			return next;
		}
	}

	// if setup by user
	private static void gridPopulate(Draw pane) {
		if(state == AppState.USER_INPUT) {
			if (pane.isMousePressed()) {
				// Debugging: Print grid coordinates to ensure the mapping works
				// place node or wall;
			}
		}
	}



	public void mousePressed(double x, double y) {

		if(state == AppState.USER_INPUT){
			GridPoint p = new GridPoint(
					(int)(Math.floor(x)	+ 0.5),
					(int)(Math.floor(y) + 0.5));
			switch(uistate){
				case START 	-> display.endPoint(p,	true);
				case FINISH -> display.endPoint(p,	false);
				case WALL	-> display.wall(p);
			}
		}
	}
	/**
	 *  Changes state
	 */
	public void keyTyped(char c) {

		if(state == AppState.USER_INPUT){
			if(c == ' '){
				state = state.next();
			}
		}
		else if(state == AppState.RESTART){
			display.showMessage("Press SPC to clear and restart");
			if(c == ' '){
				// TODO draw new empty grid
				state = state.next();
			}
		}
	}
}
