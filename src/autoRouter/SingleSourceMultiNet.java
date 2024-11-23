package autoRouter;

import edu.princeton.cs.algs4.*;


// TODO find interval bounding box of p, q0 , q1  // import edu.princeton.cs.algs4.Interval2D;
import static autoRouter.Grid.*;
import static java.lang.Math.abs;

/// # Background
/// A square grid graph contains equally distanced nodes, connected only to vertically or horizontally adjacent nodes.
/// This is unlike the familiar 2D Cartesian plane, where we define the distance between two points as the length of
/// the hypotenuse of a triangle (or line if collinear) containing those points as corners.
/// This square grid disallows diagonal movement, so the distance between any two points is now the sum of the sides of
/// that same triangle. Additionally, all other patterns maintaining course towards the target are equivalent. This
/// length is termed [Manhattan distance.](https://en.wikipedia.org/wiki/Taxicab_geometry)
/// # Implementation
/// <p>`SingleSourceMultiNet` aims to connect all the given nodes, while minimizing the number of intermediate nodes
/// used to connect them.</p>
/// <p>TODO ideally we find the MST of the cheapest total cost of connecting nodes. But node weights are determined by
/// node count where many distinct possibilities exist for every node to node connection.</p>
/// - The input node array is of the form:
///
/// ```
/// p.x   p.y
/// q0.x  q0.y
/// ...
/// qi.x  qi.y
/// ```
///
/// <p>For source 'p' and multiple targest 'q',
///     - sort q by their distance from p
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

    public static void main(String[] args) {
        int dim = 15;
        Grid grid = new Grid(dim);
        SET<Integer> excludedV = grid.getExcludedV();

        int[] nodes = {   // |dist|
                1, 8,     //   0
                6, 6,     //   7
                3, 1,     //   9
        };                //
        int[] exclude = {
                1,1,
                2,2,
                6,8,
                12,12,
                13,13,
                14,14,
        };

        for(int i = 1; i < exclude.length; i += 2){
            grid.addExcludedV(grid.indexOf(exclude[i - 1], exclude[i]));
        }
        Graph gridGraph = grid.generateDenseGrid(dim); // dense dim x dim graph with unweighted edges connecting adjacent squares


        int exx = exclude[0]; int exy = exclude[1];
        System.out.printf("excluded (%d %d) adj: ",exx ,exy);
        for(int v: grid.graph().adj(grid.indexOf(exx,exy))){
            System.out.print("("+ grid.nodeAt(v)[0] + " " + grid.nodeAt(v)[1]+") ");
        }
        System.out.println();
        printAdjacency(grid);


//        int[] dist = distanceArray(nodes);      // nodes[] by distance from source


        // Sort the input array by its distance from the first entry
        // TODO to find the start node 'p', find the 2 closest elements in the input array
//        IndexMinPQ<Integer> minDistance = new IndexMinPQ<>(dist.length);
//        for (int i = 0; i < dist.length; i++) {
//            minDistance.insert(i, dist[i]);
//        }

        Draw pane = new Draw();
        Display.init(dim,pane);
        Display.drawCircles(nodes, pane);
        pane.show();
        // need to add a node in between p and q to
        Bag<Integer> intermediateNodes = new Bag<>();
        Point[] p = fromCoords(nodes);
//        for (int i = 3; i < nodes.length; i += 2){

//            System.out.print(minDistance.keyOf(i/2) + " d:");
//            int dx = nodes[i+2] - nodes[0];
//            int dy = nodes[i+3] - nodes[1];
//            System.out.println(dx + " " + dy);
//            Display.drawCircle(nodes[i - 1], nodes[1], Color.BLUE,pane); // VISUAL: added node to define route
//            Display.drawCircle(nodes[i - 3], nodes[0], Color.YELLOW,pane);

//            pane.show();
            //System.out.println(minDistance.delMin());


//        for()
        BreadthFirstPaths paths = new BreadthFirstPaths(gridGraph, grid.indexOf(nodes[0], nodes[1]));
//        if(paths.hasPathTo(q))

//      BreadthFirstPaths paths = new BreadthFirstPaths(grid,indicesOf(nodes));
//        while(!minDistance.isEmpty()) {
//            int min = minDistance.delMin();

        pane.setPenColor();
        for (int i = 0; i < nodes.length; i +=  2) {
            int gridIndex = grid.indexOf(nodes[i], nodes[i+1]); // processes input array as is
//            System.out.print("path to:("+nodes[i]+" "+ nodes[i+1] + ")\n");
            if (paths.hasPathTo(gridIndex)) {
                for(int step : paths.pathTo(gridIndex)){

                    Display.drawPoint(grid.nodeAt(step)[0],grid.nodeAt(step)[1], pane);
//                    System.out.print("("+grid.nodeAt(step)[0]+ " " + grid.nodeAt(step)[1]+")");
                    pane.pause(200);
                    pane.show();
                }
                System.out.println();
            }
        }
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


    private static Point[] fromCoords(int[] coords){    //
        Point[] points = new Point[coords.length/2];
        for (int i = 1; i < coords.length / 2; i++){
            points[i - 1] = new Point(coords[i -1], coords[i]);
        }
        return points;
    }

    static int[] distanceArray(int[] source, int[] coords){
        assert(coords.length %2 == 0);
        int[] distances = new int[(coords.length)/2];
        for(int i = 0; i < coords.length; i += 2 ){
            distances[i/2] = distance(source[0], source[1], coords[i], coords[i + 1] );
        }
        return distances;
    }
    // returns an array of the distance from the first node (coords[0],coords[1]) to the other pairs
    static int[] distanceArray(int[] coords){
        return distanceArray(new int[]{coords[0], coords[1]}, coords);
    }

    static int distance(int x1, int y1, int x2, int y2){
        return abs(x2 - x1) + abs(y2 - y1);
    }
        //static Comparator<Point> byX = (q, p) -> { return q.x() - p.x();};
//        Comparator<Point> byX = ( p,  q) -> {return p.x() - p.y(); };
//        static Point[] bounds(Point p, Point q){
//            if(q.minus(p)  0){
//
//            }


}
