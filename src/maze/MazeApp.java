package maze;


import grid.Display;
import grid.Grid;
import grid.GridPoint;
import edu.princeton.cs.algs4.Draw;

import edu.princeton.cs.algs4.DrawListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;


/// main class
public class MazeApp implements /*ChangeListener,*/ DrawListener, ActionListener {


	private GridPoint lastMousePosition;		// last mouse click position
	private AppState appState = AppState.USER_INPUT;

	public static final double shiftGrid = -0.5;
	static Draw draw = new Draw();        // algs4.Draw JFrame Object
	static JLabel drawCanvas = draw.getJLabel(); // reference to algs4.Draw frame to compose inside main gui
	static JFrame frame = new JFrame("Grid Search");

	private final JPanel 		btnPanel 	= new JPanel(new GridLayout(1,2,0,0));
	private final JButton		btnUndo 	= new JButton("undo");
	private final JButton 		btnRun 		= new JButton("Start");


	static Display displayLogic;    //  grid display with simple algs4.Draw methods
	static Grid grid;
	static private GridPoint 		mouseGridPosition;    // location of latest mouse press
//	static private Queue<GridPoint> mouseDraggedPoints;
	/**
	 * Main App
	 */
	public MazeApp(int n) {
		// Initialize `dim` x `dim` grid.
		int dim = n;
		grid = new Grid(dim);				// initial dimensions
		displayLogic = new Display(dim, draw);	// places and maintains shapes on the drawing canvas

		Arrays.asList( btnRun, btnUndo).forEach(btnPanel::add);
		Arrays.asList( btnRun, btnUndo).forEach(b -> b.addActionListener(this));
//		btnPanel.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.add(btnPanel, BorderLayout.EAST);
		frame.add(drawCanvas, BorderLayout.CENTER);

		displayLogic.grid();			// draw the representation of the grid on the drawing canvas


		draw.enableDoubleBuffering();
		draw.addListener(this);			// add mousePress Listener to the drawing canvas

//		mouseDraggedPoints = new Queue<>();
//		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(appState == AppState.USER_INPUT) {
			if (e.getSource() == btnRun) {
				displayDataToGrid(displayLogic, grid);
			}
			if (e.getSource() == btnUndo) {
				if (btnUndo.isSelected()){

				}else{

				}
			}
		}
	}

//	@Override
//	public void mousePressed(double x, double y) {
//		DrawListener.super.mousePressed(x, y);
//		mousePosition = new GridPoint(
//				(int) (Math.floor(x) + 1.0),
//				(int) (Math.floor(y) + 1.0));
//	}

	enum AppState {
		USER_INPUT,
		RUN
	}
//	@Override
//	public void stateChanged(ChangeEvent e) {
//
//		if(appState == AppState.USER_INPUT){
//			if(draw.isMousePressed()
//					&& lastMousePosition.x() != draw.mouseX()
//					&& lastMousePosition.y() != draw.mouseY()){
//				lastMousePosition = new GridPoint(
//						(int) (Math.floor(draw.mouseX()) + 1.0),
//						(int) (Math.floor(draw.mouseY()) + 1.0));
//
//			}
//		}
//	}

	@Override
	public void mousePressed(double x, double y) {
		if(appState == AppState.USER_INPUT) {
			int gX = (int) (Math.floor(x) + 1.0);
			int gY = (int) (Math.floor(y) + 1.0);
			if(mouseGridPosition.x() != gX && mouseGridPosition.y() != gY) {
				mouseGridPosition = new GridPoint(gX, gY);

			}
		}
	}

	@Override
	public void keyTyped(char c) {
		DrawListener.super.keyTyped(c);
		System.out.println(c);
	}

	@Override
	public void keyPressed(int keycode) {
		DrawListener.super.keyPressed(keycode);
		System.out.println(Character.getName(keycode));
	}
//	@Override
//	public void mouseDragged(double x, double y){
//
//	}
	/// After input and before simulation, this transfer positional data obtained from user input stored by [Display] into the graph held by [Grid]
	public void displayDataToGrid(Display display, Grid grid) {
		for (GridPoint w : display.getWalls()) {
			if (grid.isExcluded(w)) {
				grid.addWall(w);
			}
		}
			for(GridPoint p : display.getEndpoints()){
				grid.add;
		}
	}

	private static void testSP(Grid grid, Display display) {
		grid.addWall(
				new GridPoint(3, 3),
				new GridPoint(3, 4),
				new GridPoint(3, 2),
				new GridPoint(3, 1)

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
		new MazeApp(10);
		//testSP(grid, displayLogic);

	}
}
//	enum UIState {
//
//		PLACE_START,
//		PLACE_FINISH,
//		PLACE_WALLS;
//
//		void nextState(){
//			switch(uiState){
//				case PLACE_START  -> uiState = PLACE_FINISH;
//				case PLACE_FINISH -> uiState = PLACE_WALLS;
//				default 		  -> uiState = uiState;
//			}
//		}
//	}

//
//	enum AppState {
//		INIT,
//		USER_INPUT,
//		SIMULATE,
//		RESTART;
//
//		static boolean reset;
//		void next() {
//			AppState next;
//			switch (appState) {
//				case INIT 		-> next = USER_INPUT;
//				case USER_INPUT -> next = SIMULATE;
//				case SIMULATE 	-> next = RESTART;
//				case RESTART 	-> {
//					if(reset) {
//						next = INIT;
//						uiState = UIState.PLACE_START;
//						reset = false;
//					}
//					else {
//						next = USER_INPUT;
//						uiState = UIState.PLACE_START;
//					}
//				}
//				default 		-> throw new IllegalStateException("Unexpected value: " + appState);
//			}
//			appState = next;
//		}
//		void reset(){
//			reset = true;
//		}
//	}
//
//	public void mousePressed(double x, double y) {
//
//		if(appState == AppState.USER_INPUT){
//			GridPoint p = new GridPoint(
//					(int)(Math.floor(x)	+1.0/*+ shiftGrid*/),
//					(int)(Math.floor(y) +1.0/*+ shiftGrid*/));
//			if(!display.isWall(p)) {
//				switch (uiState) {
//					case PLACE_START -> {
//						display.placeEndpoint(p, true);
//						uiState.nextState();
//					}
//					case PLACE_FINISH -> {
//						display.placeEndpoint(p, false);
//						uiState.nextState();
//					}
//					case PLACE_WALLS -> display.placeWall(p);
//
//				}
//			}
//		}
//	}


//	@Override
//	public void keyTyped(char c) {
//		DrawListener.super.keyTyped(c);
//		if(appState == AppState.USER_INPUT){
//			if(c == 0x20) {
//				if (!(uiState == UIState.PLACE_START
//						|| uiState == UIState.PLACE_FINISH)) {
//					appState.next();
//				}
//			}
//			else if(Character.toLowerCase(c) == 'r' ) {
//				display.undoWallPlacement();
//			}
//			else if(Character.toLowerCase(c) == 'e' ) {
//				display.undoEndpointPlacement();
//			}
//		}
//		else if(appState == AppState.RESTART){
//			if(c == ' '){
//				// TODO draw new empty grid
//				display.grid();
//				AppState.reset = true;
//				appState.next();
//			}
//		}
//	}

//	@Override
//	public void keyPressed(int keycode) {
//		DrawListener.super.keyPressed(keycode);
//	}
//
//	@Override
//	public void keyReleased(int keycode) {
//		DrawListener.super.keyReleased(keycode);
//	}

