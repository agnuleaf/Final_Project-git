package mst;

import autoRouter.Display;
import autoRouter.Grid;
import autoRouter.GridPoint;
import edu.princeton.cs.algs4.*;

import java.awt.*;
import java.util.Arrays;

import static java.lang.Math.abs;
/// # Background
/// A rectilinear grid graph contains equally distanced nodes, with only vertical and horizontal edges to adjacent nodes.
/// In a square grid, by disallowing diagonal movement the distance between p and q becomes :
/// <p>  dM = |q.x - p.x| + |q.y - p.y| , called Manhattan distance. </p>
/// Unlike the Euclidean distance, this gives many possible paths from p to q with the equivalent minimum distance.
/// For more, see:
///
/// [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry)
/// # Implementation
/// for find a shortest path to the nearest neighbor, which in the case shown gives a sub optimal path to the next
/// nearest neighbor.
/// <p>For source 'p' and multiple targest 'q',
///     - sort 'q' by their distance from 'p'
/// * find 1 or 2 shortest paths to q0, p and q are axis aligned there is only one, else consider the two with minimal turns.
/// * ? backtrack the path scanning for the shortest path to next nearest q
///
/// * Find SPs p to q0(closest target)  .
/// To arrive at q, from p two separate intermediate points r = (0, a.y + p.y), s = (a.x + p.x, 0) exist
///
/// ```
///  3 nodes                         option a                    option b
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  p  -  -  -  -  -  -      -  p  -  -  -  -  -  -      -  p -------------.  -
///  -  -  -  -  -  -  -  -      -  |  -  -  -  -  -  -      -  -  -  -  -  -  |  -
///  -  -  -  -  -  -  q0 -      -  | _ _ _ _ _ _ _q0 -      -  -  -  -  -  -  q0 -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  -  -  q1 -  -  -  -      -  -  -  q1 -  -  -  -      -  -  -  q1 -  -  -  -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///
///```
///  * Need to figure out how can MST select the best option for a route to cover every q.
///    + if we separate options a and b into separate graphs MST can be peformed on each graph.
///       - Issues: The number of combinations gets out of hand.
///
/// - Possible solution: Determine  nodes to add, n2 and n1,  Perform BFS separately on p0->n1->q0 and p->n2->q0 .
/// Adding n1 yields option a and n2 yields option b
///  Issues:
/// - Considers only two Manhattan distances, despite many other valid zig-zag patterns equal to 'Manhattan' Distance.
///
/// ```
///     add 2 virtual nodes          n1 gives option a          n2 gives  option b
///   -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///   -  p  -  -  -  -  n2 -      -  p  -  -  -  -  -  -      -  p -------------.  -
///   -  -  -  -  -  -  -  -      -  |  -  -  -  -  -  -      -  -  -  |  -  -  |  -
///   -  n1 -  -  -  -  q0 -      -  | _ _ x _ _ _ _q0 -      -  -  -  |  -  -  q0 -
///   -  -  -  -  -  -  -  -      -  -  -  |  -  -  -  -      -  -  -  |  -  -  -  -
///   -  -  -  -  -  -  -  -      -  -  -  |  -  -  -  -      -  -  -  |  -  -  -  -
///   -  -  -  -  -  -  -  -      -  -  -  |  -  -  -  -      -  -  -  |  -  -  -  -
///   -  -  -  -  -  -  -  -      -  -  -  |  -  -  -  -      -  -  -  |  -  -  -  -
///   -  -  -  q1 -  -  -  -      -  -  -  q1 -  -  -  -      -  -  -  q1 -  -  -  -
///   -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
/// ```
///
/// ## References
///
/// - [Rectilinear Minimum Spanning Tree](https://en.wikipedia.org/wiki/Rectilinear_minimum_spanning_tree)
/// - [Graph slides from University of Utah's CS 2420](https://github.com/tsung-wei-huang/cs2420)
/// @author Wesley Miller
public class SingleSourceMultiNet {
    private static int dim = 10;        // VISUAL: TEST dimensions

    /// View the algorithm on a test set using internal `algs4.Draw`
    public static void main(String[] args) {
        Grid grid = new Grid(dim);
        SET<Integer> excludedV = grid.getExcludedV();
//
//        Point[] nodes = {
//                new Point( 1, 8 ) ,
//                new Point( 6, 6 ) ,
//                new Point( 3, 1 ) ,
//                new Point( 5, 8 )
//        };

        int nodeCount = 8;
        GridPoint[] nodes = new GridPoint[nodeCount];
         nodes = grid.generateNodeArray(nodeCount);

        GridPoint[] nodesNearestOrigin = Arrays.copyOf(nodes, nodes.length);
        MergeX.sort(nodesNearestOrigin);

    // IndexMinPQ<Point>pqPoints = new IndexMinPQ<Point>(nodes.length);
//        for(int i = 0; i < nodes.length; i++) {
//            pqPoints.insert(i, nodes[i]);

        int[] exclude = {
                1,  1,
                2,  2,
                6,  8,
                12, 12,
                13, 13,
                14, 14,
        };

        for(int i = 1; i < exclude.length; i += 2){ // skip connecting edges to excluded vertices
            grid.addExcludedV(grid.indexOf(exclude[i - 1], exclude[i]));
        }

        Graph gridGraph = grid.generateDenseGrid(dim); // dense dim x dim graph with unweighted edges connecting adjacent squares
//        int exx = exclude[0]; int exy = exclude[1];
//        System.out.printf("excluded (%d %d) adj: ",exx ,exy);
//        for(int v: grid.graph().adj(grid.indexOf(exx,exy))){
//            System.out.print("("+ grid.nodeAt(v)[0] + " " + grid.nodeAt(v)[1]+") ");
//        }
//        System.out.println();
//        printAdjacency(grid);

//        int[] dist = distanceArray(nodes);      // nodes[] by distance from source

        // Sort the input array by its distance from the first entry
        // TODO to find the start node 'p', find the 2 closest elements in the input array
//        IndexMinPQ<Integer> minDistance = new IndexMinPQ<>(dist.length);
//        for (int i = 0; i < dist.length; i++) {
//            minDistance.insert(i, dist[i]);
//        }
        Draw pane = Display.init(dim);
        Display.drawCircles(nodes, pane);
        pane.show();
        // need to add a node in between p and q to
//        for (int i = 3; i < nodes.length; i += 2){

//            System.out.print(minDistance.keyOf(i/2) + " d:");
//            int dx = nodes[i+2] - nodes[0];
//            int dy = nodes[i+3] - nodes[1];
//            System.out.println(dx + " " + dy);
//            Display.drawCircle(nodes[i - 1], nodes[1], Color.BLUE,pane); // VISUAL: added node to define route
//            Display.drawCircle(nodes[i - 3], nodes[0], Color.YELLOW,pane);

//            pane.show();
            //System.out.println(minDistance.delMin());\

//        if(paths.hasPathTo(q))

//      BreadthFirstPaths paths = new BreadthFirstPaths(grid,indicesOf(nodes));
//        while(!minDistance.isEmpty()) {
//            int min = minDistance.delMin();
//        Iterable<BreadthFirstPaths> route =  walk(grid, nodesNearestOrigin);
        for(int i = 0; i < nodesNearestOrigin.length - 1; i++) {
            GridPoint p = nodesNearestOrigin[i]; GridPoint q = nodesNearestOrigin[i + 1];
            Bag<Integer> pq = new Bag<>();
            pq.add(grid.indexOf(p));         pq.add(grid.indexOf(q));
            BreadthFirstPaths pToq = new BreadthFirstPaths(gridGraph, pq);
            GridPoint mp = GridPoint.midPoint(p, q);
            if (pToq.hasPathTo(grid.indexOf(mp))){

            }
        }

        // incorrect behavior: produces loops
        for(int i = 0; i < nodesNearestOrigin.length-2; i+=2) {
            BreadthFirstPaths start = new BreadthFirstPaths(gridGraph,
                    grid.indexOf(nodesNearestOrigin[i])); // start at origin
            BreadthFirstPaths next  = new BreadthFirstPaths(gridGraph,
                    grid.indexOf(nodesNearestOrigin[i + 2]));  // skip a node and look back
            GridPoint centroid = GridPoint.centroid(nodesNearestOrigin[i],
                    nodesNearestOrigin[i + 1], nodesNearestOrigin[i+2]);
            Display.drawSteinerPoint(centroid, pane);

            printBFSPath(grid.indexOf(centroid) , start , grid , Color.BLUE  , pane);
            printBFSPath(grid.indexOf(centroid) , next  , grid , Color.GREEN , pane);
        }

        pane.setPenColor();

//            System.out.print("path to:("+nodes[i]+" "+ nodes[i+1] + ")\n");
//            if (bfp.hasPathTo(nodesNearestOrigin)) {
//                System.out.print(i + ": path to (" + nodes[i] +" "+ nodes[i+1]+"):");
//                for(int step : paths.pathTo(gridIndex)){
//                    System.out.println(grid.nodeAt(step)[0] + " " + grid.nodeAt(step)[1]);
//                    Display.drawPoint(grid.nodeAt(step)[0],grid.nodeAt(step)[1], pane);
// //                    System.out.print("("+grid.nodeAt(step)[0]+ " " + grid.nodeAt(step)[1]+")");
//                    pane.pause(200);
//                    pane.show();
//                }
//                System.out.println();
//            }
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
            System.out.print(grid.nodeAt(v)[0] + " "+grid.nodeAt(v)[1] + " adj: ");
            for(int adj : grid.graph().adj(v)){
                System.out.print( "("+grid.nodeAt(adj)[0] + " " + grid.nodeAt(adj)[1] + ") ");
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

    /// TODO determine midpoint between two points, `p` and `q`.
    /// If midpoint does not exist, search for nearest point connecting both `p` and `q`,
    /// with preference of quadrants containing `p` or `q`
    void placeholder(){}


}