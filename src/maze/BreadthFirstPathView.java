package maze;

import autoRouter.Display;
import autoRouter.Grid;
import autoRouter.Point;
import edu.princeton.cs.algs4.*;

import edu.princeton.cs.algs4.BreadthFirstPaths;

import java.awt.*;

import static java.lang.Math.abs;

/// # 2D Grid Breadth First Search Visualization
///
/// Uses [BreadthFirstPaths] to find the shortest path from a given source to a target in an //TODO weighted
/// grid graph. Animating the expanding wavefront until the target is found.
///
/// ## Background
///
/// A rectilinear grid graph contains equally distanced nodes, with only vertical and horizontal edges to adjacent nodes.
/// In a square grid, by disallowing diagonal movement the distance between p and q becomes :
/// <p>  dM = |q.x - p.x| + |q.y - p.y| , called Manhattan distance. </p>
/// Unlike the Euclidean distance, this gives many possible paths from p to q with the equivalent minimum distance.
///
/// ## Reference
///
/// [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry)
/// @author Wesley Miller
public class BreadthFirstPathView implements Runnable {
    private Grid grid;
    private Point p;
    private Point q;
    Draw pane;
    Thread t; // Run Path view after wavefront thread to demonstrate true operation

        @Override
    public void run() {
//        t = new Thread(this, "BFS Path Taken View" );
//        t.start();
            System.out.println(Thread.currentThread().getThreadGroup());
            BreadthFirstPaths bfp = new BreadthFirstPaths(grid.graph(), grid.indexOf(p));
        if(bfp.hasPathTo(grid.indexOf(q))){
            for(int step: bfp.pathTo(grid.indexOf(q))){
                Display.drawPoint(grid.pointAt(step), Draw.GREEN, pane);
                pane.disableDoubleBuffering();
//                pane.show();
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public BreadthFirstPathView(Point p, Point q, Grid grid, Draw pane){
        this.p = p; this.q = q; this.grid = grid; this.pane = pane;
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

    private static void printBFSPath(int graphIndex, BreadthFirstPaths bfp, Grid grid, Color color, Draw pane){
        if( bfp.hasPathTo(graphIndex) ){
            for(int step : bfp.pathTo(graphIndex)) {
                System.out.println(grid.nodeAt(step)[0] + " " + grid.nodeAt(step)[1]);
                Display.drawPoint(grid.nodeAt(step)[0], grid.nodeAt(step)[1],color, pane);
//                    System.out.print("("+grid.nodeAt(step)[0]+ " " + grid.nodeAt(step)[1]+")");
                pane.pause(200 );
                pane.show();
            }
        }
    }

    private static <T>Iterable <T> asIterable(T[] array){
        Bag<T> bag = new Bag<>();
        for (T t : array) {
            bag.add(t);
        }
        return bag;
    }

    private static Iterable<BreadthFirstPaths> walk(Grid grid, autoRouter.Point[] points){         // polynomial
        Bag<BreadthFirstPaths> paths = new Bag<>();
        int[] graphIndices = grid.indexArrayOf(points);

        for(int i = 1 ; i < points.length; i++){                                          // ~V
            paths.add( new BreadthFirstPaths(grid.graph(), grid.indexOf(points[i])));     // O(V + E) ~
        }
        return paths;
    }

    private static void printAdjacency(Grid grid) {
        for(int v = 0; v < grid.graph().V() ; v++){
            System.out.print(grid.nodeAt(v)[0] + " "+grid.nodeAt(v)[1] + " adj: ");
            for(int adj : grid.graph().adj(v)){
                System.out.print( "("+grid.nodeAt(adj)[0] + " " + grid.nodeAt(adj)[1] + ") ");
            }
            System.out.println();
        }
    }

    private static autoRouter.Point[] fromCoords(int[] coordinates){    //
        autoRouter.Point[] points = new autoRouter.Point[coordinates.length/2];
        for (int i = 1; i < coordinates.length / 2; i++){
            points[i - 1] = new Point(coordinates[i -1], coordinates[i]);
        }
        return points;
    }

}
