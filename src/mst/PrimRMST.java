package mst;

import grid.Grid;
import grid.GridPoint;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.IndexMinPQ;

import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Edge;

import static  grid.GridPoint.distSqEuclid;

import java.awt.*;
import java.util.Arrays;
/// Goal: Generate a spanning tree over a selection of points in a rectilinear graph.
/// Input:     Rectilinear Dense Grid Graph, selection of nodes to include
/// Output:    TODO standard MST or ideally Rectilinear MST with some Steiner points
/// First determines the MST for nodes given their cartesian coordinates,
/// 1. perform a scan for nearest neighbors, start with a window halfwidth of 1, and length of the axis
/// 2. Find nearest neighbors, 1 per quadrant for each node in ascending order
/// assigning weights to edges equal to their Euclidean distance .
/// 3. Perform Minimum Spanning Tree on this newly formed `EdgeWeightedGraph`
/// 4. TODO
/// @author Wesley Miller
public class PrimRMST {

    private GridPoint[] pins;
    private Graph rectilinearGraph;             // integer coordinates, no diagonal edges
    private EdgeWeightedGraph cartesianGraph;   // edges between nodes are their real valued Euclidean distance

    /// Constructor holds reference to the underlying dense rectilinear graph
    PrimRMST(Graph graph, GridPoint[] pins){
        this.pins = pins;
        this.rectilinearGraph = graph;
    }

    /// Sort nodes by distance from origin.
    /// For each node, create an edge to the next nearest neighbor, weighted by the distance between them.
    /// At most 1 neighbor is attached for each direction (up, down , right ,left)
    /// Create a graph from the nodes and edges. Create a MST.
    void mst() {
//        Point[] pinsByX = Arrays.copyOf(pins, pins.length);
//        Point[] pinsByY = Arrays.copyOf(pins, pins.length);
//        Arrays.sort(pinsByX, Point.compareX);
//        Arrays.sort(pinsByY, Point.compareY);


//        IndexMinPQ<Point> pinsNearestOrigin = new IndexMinPQ<Point>(pins.length);
//        for(int i = 0; i < pins.length; i++) {
//            pinsNearestOrigin.insert(i, pins[i]);
//        }

//        for(int i = 1 ; i < pins.length ; i++){
//            distance[i] = pins
//        }

        }

    // TODO f: MST -> RMST
    public static void main(String[] args) {
        Grid grid = new Grid(10);
        GridPoint[] pinsTest = {
                new GridPoint(2, 3),
                new GridPoint(3, 1),
                new GridPoint(1, 2),
                new GridPoint(2, 1),
                new GridPoint( 1,1),
//                new Point(3, 5),
//                new Point(1,2),
//                new Point(7,8),
//                new Point(4,4)
        };
        GridPoint[] pinsByX = Arrays.copyOf(pinsTest, pinsTest.length);
        GridPoint[] pinsByY = Arrays.copyOf(pinsTest, pinsTest.length);
        MergeX.sort(pinsByX, GridPoint.compareX);  MergeX.sort(pinsByY, GridPoint.compareY);

        // TODO associate pinsByX and pinsByY to the same index in pinsTest
        // TODO quadrant check for neighborsbv
        EdgeWeightedGraph euclidGraph = new EdgeWeightedGraph(pinsTest.length);
        for(int i = 0; i < pinsByX.length - 1; i++){
            euclidGraph.addEdge(new Edge(i,i+1, distSqEuclid(pinsByX[i], pinsByX[i+1])));
        }

        for(int i = 0; i < pinsByY.length - 1; i++){
            euclidGraph.addEdge(new Edge(i,i+1, distSqEuclid(pinsByX[i], pinsByX[i+1])));
        }
//        for(Point p : pinsTest){
//            System.out.print(p + " ");
//        }
        PrimRMST rmst = new PrimRMST(grid.graph(), pinsTest);

        IndexMinPQ<GridPoint> pinsSorted = new IndexMinPQ<>(pinsTest.length);
        for(int i = 0 ; i < pinsTest.length - 1; i++){
            pinsSorted.insert(i, pinsTest[i] );
        }

//        for(int i = 0; i < pinsSorted.size(); i++){
//            Point p = pinsSorted.(i);
//            System.out.println(p);
//            System.out.println( v+":" + + pinsTest[v] +"--" + q +":" +  );
//            Edge e = new Edge()
//            euclidGraph.addEdge()
//        }


//        for(int i = 0; i < pinsSorted.size(); i++){
//            System.out.println(i + ": " + pinsSorted.keyOf(i));
//        }

//        private static void printAdjacency(Grid grid) {
//            for(int v = 0; v < grid.graph().V() ; v++){
//                System.out.print(grid.pointAt(v) + " adj: ");
//                for(int adj : grid.graph().adj(v)){
//                    System.out.print( grid.pointAt(adj));
//                }
//                System.out.println();
//            }
//        }
//        private void printBFSPath(int graphIndex, BreadthFirstPaths bfp, Grid grid, Color color){
//            if( bfp.hasPathTo(graphIndex) ){
//                for(int step : bfp.pathTo(graphIndex)) {
//                    display.drawPoint(grid.pointAt(step),color);
////                    System.out.print("("+grid.nodeAt(step)[0]+ " " + grid.nodeAt(step)[1]+")");
//                    pane.pause(200 );
//                    pane.show();
//                }
//            }
//        }
    }
}


