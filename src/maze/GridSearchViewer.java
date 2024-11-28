package maze;


import autoRouter.Display;
import autoRouter.Grid;
import autoRouter.GridPoint;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.DrawListener;
import edu.princeton.cs.algs4.SET;

// TODO draw bfs wavefront, then the found path, then draw the other targeted search
//  what are strengths and weaknesses of each approach?
//      (When is it better to NOT create a path 'library' like all the algs4 algorithms
//      but instead exlporing along one single path with very basic 'guidance'
/// dummy main app. TODO can multiple threads allow simultaneously drawing of pathfinding algorithms??
public class GridSearchViewer implements DrawListener  {
	private int dim = 10;
	private Draw pane = Display.init(dim);
	private boolean mode = false;      // false is creating, true is destroying
	private boolean function = false;  // false is creating nodes, true is creating blockers
	
    /**
	 * 
	 */
	public GridSearchViewer() {
		// Initialize `dim` x `dim` grid.
        Grid grid = new Grid(dim);
        SET<Integer> excludedV = grid.getExcludedV();
        pane.addListener(this);
//        pane.show(50);

        // From UI we receive Node[] endPoints, MinPQ<Node> walls.
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
//        var gst = new GridSearchTargeted(grid, pane); // TODO dumb algorithm doesn't even save the shortest path
//        gst.searchWithBacktrack(p, q);
	}

	public static void main(String[] args) {
        new GridSearchViewer();
    }
    
    public void mousePressed(double x, double y) {
    	// times it by the dimensions of the display
    	int gridX = (int) (Math.round(x*dim));
    	int gridY = (int) (Math.round(y*dim));
        
        // Create the GridPoint
        var p = new GridPoint(gridX, gridY);
        
        // Debugging: Print grid coordinates to ensure the mapping works
        System.out.println("Mouse clicked at: (" + x + ", " + y + ")");
        System.out.println("Mapped to grid coordinates: (" + gridX + ", " + gridY + ")");
        
        // destroying
        if(mode) {
        	// blockers
        	if(function) {
        		
        	}
        	//nodes
        	else {
        		
        	}
        }
        // creating
        else {
        	// blockers
        	if(function) {
        		
        	}
        	// nodes
        	else {
        		Display.drawCircle(p, pane);
        	}
        }
        
        pane.show();
    }
}

