/**
 * 
 */
package autoRouter;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Queue;

/**
 * @author Ty Greenburg & Wesley Miller
 */
public class PathFinder {
	private static int dim = 10;
	
	/**
	 * 
	 */
	public PathFinder(Queue<Point> p) {
		
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Grid grid = new Grid(dim);
		int[] n = {   // |dist|
                1, 8,     //   0
                6, 6,     //   7
                3, 1,     //   9
                1, 3
        };             
		Queue<Point> nodes = new Queue<>();
		for(int i = 0; i < n.length; i += 2)
			nodes.enqueue(new Point(n[i],n[i+1]));
        int[] exclude = {
                1,1,
                2,2,
                6,8,
        };
        for(int i = 0; i < exclude.length; i += 2){ // skip connecting edges to excluded vertices
            grid.addExcludedV(grid.indexOf(exclude[i], exclude[i+1]));
        }
        Graph gridGraph = grid.generateDenseGrid(dim);
       
        Draw pane = Display.init(dim);
        Display.drawCircles(n, pane);
        pane.show();
        pane.setPenColor();
        for (int i = 0; i < nodes.size(); i ++) {
        	BreadthFirstPaths paths = new BreadthFirstPaths(gridGraph, grid.indexOf(nodes.dequeue()));
            int gridIndex = grid.indexOf(nodes.peek()); // processes input array as is
//            System.out.print("path to:("+nodes[i]+" "+ nodes[i+1] + ")\n");
            if (paths.hasPathTo(gridIndex)) {
                //System.out.print(i + ": path to (" + nodes[i] +" "+ nodes[i+1]+"):");
                for(int step : paths.pathTo(gridIndex)){
                    System.out.println(grid.nodeAt(step)[0] + " " + grid.nodeAt(step)[1]);
                    Display.drawPoint(grid.nodeAt(step)[0],grid.nodeAt(step)[1], pane);
//                    System.out.print("("+grid.nodeAt(step)[0]+ " " + grid.nodeAt(step)[1]+")");
                    pane.pause(200);
                    pane.show();
                }
                System.out.println();
            }
        }
	}

}
