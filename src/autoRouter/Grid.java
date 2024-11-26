package autoRouter;

import edu.princeton.cs.algs4.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;
import static java.util.Arrays.binarySearch;

/// Operations for a elements in a rectilinear grid and its dense graph representation.
/// Provides methods to create an operate on a grid graph based up`algs4.Graph`.
/// @author Wesley Miller
public class Grid {
    private SET<Integer> excludedV = new SET<>();           // TODO no need for set, sorted array is sufficient
    private final int dim;
    private final Graph grid;

    public int getDimension(){
        return dim;
    }
    public Grid(int dimension){
        dim = dimension;
        this.grid = generateDenseGrid(dim);
    }
//    public Grid(){
//        dim = 10;
//        this.grid = generateDenseGrid();
//    }
    /// Returns the `Graph` instance.
    public Graph graph(){
        return grid;
    }

    @Deprecated
    Iterable<Integer> indicesOf(int[] nodes) {
        Bag<Integer> indices = new Bag<>();
        for(int i = 1 ; i < nodes.length ; i+=2) {
            indices.add(indexOf(nodes[i-1], nodes[i]));
        }
        return indices;
    }

    /// Converts and array of nodes in (x,y) grid coordinates to an array of graph vertices
    public int[] indexArrayOf(Point[] nodes){
        int[] indexArray = new int[nodes.length];
        for(int i = 0 ; i < nodes.length ; i++) {
            indexArray[i] = indexOf(nodes[i]);
        }
        return indexArray;
    }

    ///  Converts an array of nodes as (x,y) grid coordinates to graph vertices
    public Iterable<Integer> indicesOf(Point[] nodes) {
        Bag<Integer> indices = new Bag<>();
        for(int i = 1 ; i < nodes.length ; i+=2) {
            indices.add(indexOf(nodes[i]));
        }
        return indices;
    }

    ///  Converts nodes as (x,y) grid coordinates to graph vertices
    public Iterable<Integer> indicesOf(Iterable<Point> nodes) {
        Bag<Integer> indices = new Bag<>();
        for(Point p : nodes) {
            indices.add(indexOf(p));
        }
        return indices;
    }

    public SET<Integer> getExcludedV(){
        return excludedV;
    }
    /// Add an excluded graph vertex
    public void addExcludedV(int v){
        excludedV.add(v);
    }
    /// Converts individual 1-based (x, y) coordinates of node to 0-based indexed vertex in `Graph`.
    public int indexOf(int x, int y){
        if(x == 0 || y == 0) return -1; // TODO: Remove and add bounds check elsewhere like file input conversion
        return (x - 1) * dim + (y - 1);
    }
    /// Converts a `Point` as 1-based (x, y) coordinates of node to 0-based indexed vertex in `Graph`.
    public int indexOf(Point p){
        if(p.x() == 0 || p.y() == 0)
            return -1; // TODO: Remove and add bounds check elsewhere like file input conversion
        return ((p.x() - 1) * dim + (p.y() - 1));
    }
    /// Converts a 0-based indexed vertex in `Graph` to 1-based (x, y) coordinates of node.
    public int[] nodeAt(int index){
        return new int[]{
                (index) / dim + 1,
                (index) % dim + 1
        };
    }
    /// Converts a 0-based indexed vertex in `Graph` to a Point with 1-based (x, y) coordinates.
    public Point pointAt(int index){
        return new Point(
            (index) / dim + 1,
            (index) % dim + 1);
    }

    /// Assigns edges to adjacent nodes in a `dim` x `dim` grid, nodes in the grid are only connected horizontally and
    /// vertically to other adjacent nodes. Diagonal connections are NOT created.
    /// Skips attaching edges to excluded vertices.
    /// Nodes at grid-corners have 2 edges, nodes along grid-borders have 3, and internal nodes have 4.
    /// The `Graph` vertices are indexed in the range = \[0, (dim*dim -1 )\] A dense graph is formed by
    /// TODO Sparse version to reduce the space cost .
    public Graph generateDenseGrid(int dim) {
        Graph grid = new Graph(dim*dim);
        for(int v = 0; v < dim*dim; v++){
            if(!excludedV.contains(v)){
                if(v < dim*dim - (dim)  && !excludedV.contains(v+dim)) // skip attaching beyond top bordder and excluded
                    grid.addEdge(v, v + dim);
                if((v + 1) % dim != 0   && !excludedV.contains(v+1)) // skip attaching beyond right border and excluded
                    grid.addEdge(v, (v+1));
            }
        }
        return grid;
    }
    public Graph generateDenseGrid() {
        return generateDenseGrid(dim);
    }
    
    /// Generates random nodes in the grid
    public Iterable<Point> generateNodes(int nodeCount){
        SET<Point> uniqueNodes = new SET<>();
        for(int i = 0 ; i < nodeCount; i++) {
            Point n;
            do{
                n = new Point(StdRandom.uniformInt(1, dim), StdRandom.uniformInt(1, dim));
            }while(uniqueNodes.contains(n));
            uniqueNodes.add(n);
        }
        return uniqueNodes;
    }
    /// Generates random nodes in the grid
    public Point[] generateNodeArray(int nodeCount){
        Set<Point> uniqueNodes = new HashSet<>();
        for(int i = 0 ; i < nodeCount; i++) {
            Point n;
            do{
                n = new Point(StdRandom.uniformInt(1, dim), StdRandom.uniformInt(1, dim));
            }while(uniqueNodes.contains(n));
            uniqueNodes.add(n);
        }
        return uniqueNodes.toArray(new Point[0]);
    }
    // Returns shortest component of vector pq. From p this points to the closest line through q
    public static Point toIntersection(Point p, Point q){
        int magPQx = abs(q.x() - p.x()) ; int magPQy = abs(q.y() - p.y());
        // select line through q closest to p
        if(magPQx > magPQy) {
            return new Point( 0 , q.y() - p.y() );
        }
        else if( magPQy > magPQx){
            return new Point(q.x() - p.x(), 0);
        } else return new Point(0, 0);
    }
    /// Creates a subgraph, or window, of a grid graph using inclusive bounds defined by node (x,y)
    /// vertices ll(lower left) and ur(upper right).
    public Graph subGraph(int[] ll, int[] ur, Graph graph){
        if( ur[0] < ll[0] && ur[1] < ll[1])  throw new IllegalArgumentException("invalid bounds provided for subgraph");
        Graph subGraph = new Graph((ur[0] - ll[0]) * (ur[1] - ll[1]));

        for(int v = 0; v < dim * dim; v++){
            int vx = nodeAt(v)[0];  int vy = nodeAt(v)[1];
            if(( vx >= ll[0] && vx < ur[0]) && ( vy >= ll[1] && vy < ur[1] )) {
                // attach vertex upward of v unless adding to top or exclude list
                if (v < dim * dim - (dim)  && !excludedV.contains(v + dim))
                    subGraph.addEdge(v, v + dim);
                // attach vertex rightward of v edge to right except at rightmost spot
                if ((v + 1) % dim != 0  && !excludedV.contains(v + 1))
                    subGraph.addEdge(v, (v + 1));
            }
        }
        return subGraph;
    }
    /// Uses class `Point`  instead of `int[]` pairs to form a subgraph, or window.
     public Graph subGraph(Point ll, Point ur, Graph graph){
        if( ur.x() < ll.x() && ur.y() < ll.y())  throw new IllegalArgumentException("invalid bounds provided for subgraph");
        Graph subGraph = new Graph((ur.x() - ll.x()) * (ur.y() - ll.y()));

        for(int v = 0; v < dim * dim; v++){
            int vx = nodeAt(v)[0];  int vy = nodeAt(v)[1];
            if(( vx >= ll.x() && vx < ur.x()) && ( vy >= ll.y() && vy < ur.y() )) {
                // attach vertex upward of v unless adding to top or exclude list
                if (v < dim * dim - (dim)  && !excludedV.contains(v + dim))
                    subGraph.addEdge(v, v + dim);
                // attach vertex rightward of v edge to right except at rightmost spot
                if ((v + 1) % dim != 0  && !excludedV.contains(v + 1))
                    subGraph.addEdge(v, (v + 1));
            }
        }
        return subGraph;
    }
    private boolean contained(int v, int[] ll, int[] ur){
        int vx = nodeAt(v)[0];  int vy = nodeAt(v)[1];
        if(( vx > ll[0] && vx < ur[0]) && ( vy > ll[1] && vy < ur[1] ))
            return true;
        return false;
    }
    private boolean contained(int v, Point ll, Point ur){
        int vx = nodeAt(v)[0];  int vy = nodeAt(v)[1];
        if(( vx > ll.x() && vx < ur.x()) && ( vy > ll.y() && vy < ur.y() ))
            return true;
        return false;
    }
    static void printPairs(int[] array){
        for(int i = 1; i < array.length; i++){
            System.out.print(i - 1 + " " + i);
        }
    }
    static void println(int[] array){
        for(int i : array){
            System.out.print(i + " ");
        }
    }

    public static void main(String[] args) {
        Grid grid = new Grid(10);
        Draw pane = Display.init(10);
        int[] nodes = {   // |dist|
                1, 8,     //   0
                6, 6,     //   7
                3, 1,     //   9
        };
        Display.drawCircles(nodes, pane);
        pane.show();
        pane.pause(100);
        BreadthFirstPaths bfp = new BreadthFirstPaths(grid.graph(), grid.indexOf(nodes[0], nodes[1]));
        for(int v : bfp.pathTo(grid.indexOf(6,6))){
            Display.drawPoint(grid.nodeAt(v)[0], grid.nodeAt(v)[1], pane);
            pane.pause(100);
            pane.show();
        }
    }

    //    /// Converts nodes to graph vertices representing a recently formed net, that becomes an obstacle to all future
//    /// operations.
//    /// @param nodes - array of the all the nodes in the most recent spanning tree
//    void addWall(int[] nodes){
//        for(int i = 2; i < nodes.length; i += 2){
//            excludedV.add(indexOf(nodes[i], nodes[i-1]));
//        }
//    }

    //    static int[] distanceArray(int[] source, int[] coords){
//        assert(coords.length %2 == 0);
//        int[] distances = new int[(coords.length)/2];
//        for(int i = 0; i < coords.length; i += 2 ){
//            distances[i/2] = distance(source[0], source[1], coords[i], coords[i + 1] );
//        }
//        return distances;
//    }
//
//    // returns an array of the distance from the first node (coords[0],coords[1]) to the other pairs
//    static int[] distanceArray(int[] coords){
//        return distanceArray(new int[]{coords[0], coords[1]}, coords);
//    }

    //    static int distance(int x1, int y1, int x2, int y2){
//        return abs(x2 - x1) + abs(y2 - y1);
//    }
    //static Comparator<Point> byX = (q, p) -> { return q.x() - p.x();};
//        Comparator<Point> byX = ( p,  q) -> {return p.x() - p.y(); };
//        static Point[] bounds(Point p, Point q){
//            if(q.minus(p)  0){
//
//            }
}
