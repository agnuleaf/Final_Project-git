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

	public static final double shiftGrid = -0.5;
	private int dim = 10;
	static Draw pane;		// algs4.Draw JFrame Object
	static Display display;	//  grid display with simple algs4.Draw methods
	static Grid grid;

	private static AppState appState = AppState.INIT;
	private static UIState uiState = UIState.PLACE_START;

	/**
	 * Main App
	 */
	public MazeApp() {
		// Initialize `dim` x `dim` grid.
		grid = new Grid(dim);        // generate grid in State.SIMULATE
		display = new Display(dim);
		pane = display.getPane();
		display.grid();
		pane.addListener(this);

		appState = AppState.USER_INPUT;
	}

	/// Load walls from user input to the graph
	public void updateGraphFromDisplay(Display display, Grid grid){
		for(GridPoint w : display.getWalls()){
			if(!grid.isExcluded(w)){
				grid.addWall(w);
			}
		}
	}
	private static void testSP(Grid grid, Display display) {
		grid.addWall(
				new GridPoint(3,3),
				new GridPoint(3,4),
				new GridPoint(3,2),
				new GridPoint(3,1)

		);
		grid.buildGraph();
		//From UI we receive Node[] endPoints, MinPQ<Node> walls.
		var p = new GridPoint(1, 1);
		var q = new GridPoint(7, 5);

		display.placeEndpoint(p, true);
		display.placeEndpoint(q, false);

		// Grid Init complete ---
		BreadthFirstSearchView wavefront = new BreadthFirstSearchView(p, grid, display);
		wavefront.run();
		// Start threads for the wavefront of `BreadthFirstSearchView` and the path taken
		BreadthFirstPathView pathView = new BreadthFirstPathView(p, q, grid, display);
		pathView.run();
//		var gst = new GridSearchTargeted(grid, display); // TODO dumb algorithm doesn't even save the shortest path
//		gst.searchWithBacktrack(p, q);
	}

	public static void main(String[] args) {
		new MazeApp();
		testSP(grid, display);

	}

	// TODO diagram state machines
	enum UIState {

		PLACE_START,
		PLACE_FINISH,
		PLACE_WALLS;

		void nextState(){
			switch(uiState){
				case PLACE_START  -> uiState = PLACE_FINISH;
				case PLACE_FINISH -> uiState = PLACE_WALLS;
				default 		  -> uiState = uiState;
			}
		}
	}

	enum AppState {
		INIT,
		USER_INPUT,
		SIMULATE,
		RESTART;

		static boolean reset;
		void next() {
			AppState next;
			switch (appState) {
				case INIT 		-> next = USER_INPUT;
				case USER_INPUT -> next = SIMULATE;
				case SIMULATE 	-> next = RESTART;
				case RESTART 	-> {
					if(reset) {
						next = INIT;
						uiState = UIState.PLACE_START;
						reset = false;
					}
					else {
						next = USER_INPUT;
						uiState = UIState.PLACE_START;
					}
				}
				default 		-> throw new IllegalStateException("Unexpected value: " + appState);
			}
			appState = next;
		}
		void reset(){
			reset = true;
		}
	}

	public void mousePressed(double x, double y) {

		if(appState == AppState.USER_INPUT){
			GridPoint p = new GridPoint(
					(int)(Math.floor(x)	+1.0/*+ shiftGrid*/),
					(int)(Math.floor(y) +1.0/*+ shiftGrid*/));
			if(!display.isWall(p)) {
				switch (uiState) {
					case PLACE_START -> {
						display.placeEndpoint(p, true);
						uiState.nextState();
					}
					case PLACE_FINISH -> {
						display.placeEndpoint(p, false);
						uiState.nextState();
					}
					case PLACE_WALLS -> display.placeWall(p);

				}
			}
		}
	}
	/**
	 *  Changes state
	 */
	public void keyTyped(char c) {
		if(appState == AppState.USER_INPUT){
			if(c == ' ') {
				if (!(uiState == UIState.PLACE_START
						|| uiState == UIState.PLACE_FINISH)) {
					appState.next();
				}
			}
			else if(Character.toLowerCase(c) == 'r' ) {
				display.undoWallPlacement();
			}
			else if(Character.toLowerCase(c) == 'e' ) {
				display.undoEndpointPlacement();
			}
		}
		else if(appState == AppState.RESTART){
			if(c == ' '){
				// TODO draw new empty grid
				display.grid();
				AppState.reset = true;
				appState.next();
			}
		}
	}
}
