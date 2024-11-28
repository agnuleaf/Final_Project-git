package maze;

import grid.Display;
import grid.Grid;
import grid.GridPoint;
import edu.princeton.cs.algs4.*;

import edu.princeton.cs.algs4.BreadthFirstPaths;

import java.awt.*;

import static java.lang.Math.abs;

/// # 2D Grid Breadth First Search Visualization
///
/// Uses [BreadthFirstPaths] to find the shortest path from a given source to a target in an //TODO weighted
/// grid graph. Animating the expanding wavefront until the target is found.
/// @author Wesley Miller
public class BreadthFirstPathView implements Runnable {
    private Grid grid;
    private GridPoint p;
    private GridPoint q;
    Draw pane;
    Display display;
    Thread t; // Run Path view after wavefront thread to demonstrate true operation

        @Override
    public void run() {
//        t = new Thread(this, "BFS Path Taken View" );
//        t.start();
//            System.out.println(Thread.currentThread().getThreadGroup());
            BreadthFirstPaths bfp = new BreadthFirstPaths(grid.graph(), grid.indexOf(p));
        if(bfp.hasPathTo(grid.indexOf(q))){
            GridPoint prev = p;
            for(var step  : bfp.pathTo(grid.indexOf(q))){
                display.path(prev, grid.pointAt(step), Color.PINK);
                prev = grid.pointAt(step);
                pane.disableDoubleBuffering();
                pane.pause(50);
//                pane.show();
//                try{
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
            }
        }
    }

    public BreadthFirstPathView(GridPoint p, GridPoint q, Grid grid, Display display){
        this.p = p; this.q = q; this.grid = grid; this.display = display; this.pane = display.getPane();
    }
    /// View the algorithm on a test set using internal `algs4.Draw`
    public static void main(String[] args) {

    }

    // TODO modify a graph search from `algs4` to use 2 stacks containing neighbors of previously visited nodes to jump
    // to when search fails.
    // nodes
    //  move to grid class as instance method
//    private static Point pointNear(Point p, BreadthFirstPaths bfp , Grid grid ){
//       if( bfp.hasPathTo(grid.indexOf(p)) ){
//            return p;
//        }
//       else{
//           p.inWindow()
//       }
//    }

    private void printBFSPath(int graphIndex, BreadthFirstPaths bfp, Grid grid, Color color){
        if( bfp.hasPathTo(graphIndex) ){
            for(int step : bfp.pathTo(graphIndex)) {
                display.drawPoint(grid.pointAt(step),color);
//                    System.out.print("("+grid.nodeAt(step)[0]+ " " + grid.nodeAt(step)[1]+")");
                pane.pause(200 );
                pane.show();
            }
        }
    }

    private static Iterable<BreadthFirstPaths> walk(Grid grid, GridPoint[] gridPoints){         // polynomial
        Bag<BreadthFirstPaths> paths = new Bag<>();
        int[] graphIndices = grid.indexArrayOf(gridPoints);

        for(int i = 1; i < gridPoints.length; i++){                                          // ~V
            paths.add( new BreadthFirstPaths(grid.graph(), grid.indexOf(gridPoints[i])));     // O(V + E) ~
        }
        return paths;
    }

    private static void printAdjacency(Grid grid) {
        for(int v = 0; v < grid.graph().V() ; v++){
            System.out.print(grid.pointAt(v) + " adj: ");
            for(int adj : grid.graph().adj(v)){
                System.out.print( grid.pointAt(adj));
            }
            System.out.println();
        }
    }

    private static GridPoint[] fromCoords(int[] coordinates){    //
        GridPoint[] gridPoints = new GridPoint[coordinates.length/2];
        for (int i = 1; i < coordinates.length / 2; i++){
            gridPoints[i - 1] = new GridPoint(coordinates[i -1], coordinates[i]);
        }
        return gridPoints;
    }

}
