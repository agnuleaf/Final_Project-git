package grid;

import edu.princeton.cs.algs4.*;

//import java.util.HashSet;
import java.util.Iterator;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/// Provides a dense graph representation of a 2D grid using [Graph] api, and conversions from its 0-based vertex array
/// to 1-based coordinates of the positive XY plane presented as an immutable [GridPoint].
/// ### Overview
///
/// A 2D rectilinear grid graph contains equally distanced nodes, with only vertical and horizontal edges to adjacent nodes.
/// In a square grid, by disallowing diagonal movement the minimum distance between p and q becomes :
/// <p>  dM = |q.x - p.x| + |q.y - p.y| , called Manhattan or taxicab distance. </p>
/// Unlike the more familiar Euclidean distance on a Cartesian plane or the Chebyshev distance for a king on a chessboard,
/// there can be many routes with equal Manhattan distance exist can take possible shortest paths from p to q.
///
/// [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry)
/// [Lattice graph](https://en.wikipedia.org/wiki/Lattice_graph)
/// [Lattice path](https://en.wikipedia.org/wiki/Lattice_path)
/// [Hanan Grid](https://en.wikipedia.org/wiki/Hanan_grid)
/// @author Wesley Miller
public class Grid {
    private StackSet excludedV;     //  walls from user input
    private Queue<Integer> endpoints;
    private int endpointCount;
    private static int ticks = 15;
    private Graph graph;
    private int[] savedExcludedV;   // saved walls from the previous session(s);


    public Grid(int tickMarks){
        ticks = tickMarks;
        excludedV = new StackSet();
        endpoints = new Queue<>();
    }

    public static void setDim(int n){
        ticks = (n >= 0 && n <= 512 ? n: ticks);
    }
    /// Receive endpoints
    public void addEndpoint(GridPoint p){
        endpoints.enqueue(indexOf(p));
        endpointCount++;
    }
    public int getCountEndpoints(){
        return endpointCount;
    }
    public int getCountWalls(){
        return excludedV.size();
    }
    /// returns endpoints as a set of `GridPoint`s
    public Queue<GridPoint> getEndpoints(){
        Queue<GridPoint> q = new Queue<>();
        for(int v : endpoints){
            q.enqueue(pointAt(v));
        }
        return q;
    }
    public Iterable<Integer> getWalls(){
        return excludedV.getSet();
    }
    /// Returns the `Graph` instance.
    public Graph graph(){
        return graph;
    }
    public boolean isEndpoint(GridPoint p){
        boolean b = false;
        for(int v : endpoints){
            b |= (v == indexOf(p));
        }
        return b;
    }
    /// Converts and array of points in (x,y) grid coordinates to an array of graph vertices
    public int[] indexArrayOf(GridPoint[] points){
        int[] indexArray = new int[points.length];
        for(int i = 0 ; i < points.length ; i++) {
            indexArray[i] = indexOf(points[i]);
        }
        return indexArray;
    }

    ///  Converts an array of points as (x,y) grid coordinates to graph vertices
    public Iterable<Integer> indicesOf(GridPoint[] points) {
        Bag<Integer> indices = new Bag<>();
        for(int i = 1 ; i < points.length ; i+=2) {
            indices.add(indexOf(points[i]));
        }
        return indices;
    }


    void save(){
        savedExcludedV = excludedV.combineArrays(savedExcludedV);
    }
//    ///  Converts nodes as (x,y) grid coordinates to graph vertices
//    public Iterable<Integer> indicesOf(Iterable<GridPoint> nodes) {
//        Bag<Integer> indices = new Bag<>();
//        for(GridPoint p : nodes) {
//            indices.add(indexOf(p));
//        }
//        return indices;
//    }

    public Iterable<Integer> getWall(){
        return excludedV;
    }

    /// Add a 'wall' to the grid
    public void addWall(GridPoint... points){
        for(GridPoint p : points){
            excludedV.push(p);
        }
    }
    public boolean isWall(GridPoint p){
        return excludedV.contains(p);
    }
    /// Removes the last excluded vertex added
    public GridPoint removeLastWall() {
            return excludedV.pop();
    }
//
//    // Converts individual 1-based (x, y) coordinates of node to 0-based indexed vertex in `Graph`.
//    private int indexOf(int x, int y){
//        if(x == 0 || y == 0) return -1; // TODO: Remove and add bounds check elsewhere like file input conversion
//        return (x - 1) * dim + (y - 1);
//    }

    /// Converts a `Point` as 1-based (x, y) coordinates of node to 0-based indexed vertex in `Graph`.
    public static int indexOf(GridPoint p){
        if(p.x() == 0 || p.y() == 0)
            return -1; // TODO: Remove and add bounds check elsewhere like file input conversion
        return ((p.x() - 1) * ticks + (p.y() - 1));
    }

    /// Converts a 0-based indexed vertex in `Graph` to a Point with 1-based (x, y) coordinates.
    public static GridPoint pointAt(int index){
        return new GridPoint(
            (index) / ticks + 1,
            (index) % ticks + 1);
    }

    /// Assigns edges to adjacent nodes in a `dim` x `dim` grid, nodes in the grid are only connected horizontally and
    /// vertically to other adjacent nodes. Diagonal connections are NOT created.
    /// Skips attaching edges to excluded vertices.
    /// Nodes at grid-corners have 2 edges, nodes along grid-borders have 3, and internal nodes have 4.
    /// The `Graph` vertices are indexed in the range = \[0, (dim*dim -1 )\] A dense graph is formed by
    public void buildGraph() {
        this.graph = new Graph(ticks * ticks);
        for(int v = 0; v < ticks * ticks; v++){
            if(!excludedV.contains(v)){
                if(v < ticks * ticks - (ticks)  && !excludedV.contains(v+ ticks)) // skip attaching beyond top border and excluded
                    graph.addEdge(v, v + ticks);
                if((v + 1) % ticks != 0   && !excludedV.contains(v+1)) // skip attaching beyond right border and excluded
                    graph.addEdge(v, (v+1));
            }
        }
    }

    /// Generates random nodes in the grid
    /// @return - nodes as an `Iterable<GridPoint>`
    public Iterable<GridPoint> generateNodes(int nodeCount){
        SET<GridPoint> uniqueNodes = new SET<>();
        for(int i = 0 ; i < nodeCount; i++) {
            GridPoint n;
            do{
                n = new GridPoint(StdRandom.uniformInt(1, ticks), StdRandom.uniformInt(1, ticks));
            }while(uniqueNodes.contains(n));
            uniqueNodes.add(n);
        }
        return uniqueNodes;
    }

//    /// Generates random nodes in the grid
//    /// @return - nodes as an `GridPoint[]`
//    public GridPoint[] generateNodeArray(int nodeCount){
//        Set<GridPoint> uniqueNodes = new HashSet<>();
//        for(int i = 0 ; i < nodeCount; i++) {
//            GridPoint n;
//            do {
//                n = new GridPoint(StdRandom.uniformInt(1, dim), StdRandom.uniformInt(1, dim));
//            } while (uniqueNodes.contains(n));
//            uniqueNodes.add(n);
//        }
//        return uniqueNodes.toArray(new GridPoint[0]);
//    }

    // TODO test
    ///  Finds shortest component of vector 'pq'. From 'p' this points to the closest line through 'q'
    public static GridPoint toIntersection(GridPoint p, GridPoint q){
        int magPQx = abs(q.x() - p.x()) ; int magPQy = abs(q.y() - p.y());
        // select line through q closest to p
        if(magPQx > magPQy) {
            return new GridPoint( 0 , q.y() - p.y() );
        }
        else if( magPQy > magPQx){
            return new GridPoint(q.x() - p.x(), 0);
        } else return new GridPoint(0, 0);
    }


    private Graph subGraph(int[] ll, int[] ur, Graph graph){
        if( ur[0] < ll[0] && ur[1] < ll[1])  throw new IllegalArgumentException("invalid bounds provided for subgraph");
        Graph subGraph = new Graph((ur[0] - ll[0]) * (ur[1] - ll[1]));

        for(int v = 0; v < ticks * ticks; v++){
            int vx = xOf(v);  int vy = yOf(v);
            if(( vx >= ll[0] && vx < ur[0]) && ( vy >= ll[1] && vy < ur[1] )) {
                // attach vertex upward of v unless adding to top or exclude list
                if (v < ticks * ticks - (ticks)  && !excludedV.contains(v + ticks))
                    subGraph.addEdge(v, v + ticks);
                // attach vertex rightward of v edge to right except at rightmost spot
                if ((v + 1) % ticks != 0  && !excludedV.contains(v + 1))
                    subGraph.addEdge(v, (v + 1));
            }
        }
        return subGraph;
    }

    /// Creates a subgraph, or window, of a grid graph using inclusive bounds defined by two [GridPoint]s
    /// 'll' (lower left) and 'ur'(upper right).
    /// @return the subGraph
    /// @throws IllegalArgumentException if `ll` and 'ur' are invalid coordinates relative to one another
    public Graph subGraph(GridPoint ll, GridPoint ur, Graph graph){
        if( ur.x() < ll.x() && ur.y() < ll.y())  throw new IllegalArgumentException("invalid bounds provided for subgraph");
        Graph subGraph = new Graph((ur.x() - ll.x()) * (ur.y() - ll.y()));

        for(int v = 0; v < ticks * ticks; v++){
            int vx = xOf(v);  int vy = yOf(v);
            if(( vx >= ll.x() && vx < ur.x()) && ( vy >= ll.y() && vy < ur.y() )) {
                // attach vertex upward of v unless adding to top or exclude list
                if (v < ticks * ticks - (ticks)  && !excludedV.contains(v + ticks))
                    subGraph.addEdge(v, v + ticks);
                // attach vertex rightward of v edge to right except at rightmost spot
                if ((v + 1) % ticks != 0  && !excludedV.contains(v + 1))
                    subGraph.addEdge(v, (v + 1));
            }
        }
        return subGraph;
    }

    // is the graph vertex 'v' within the 'll'-'ur' window?
    private boolean contained(int v, GridPoint ll, GridPoint ur){
        int vx = xOf(v);  int vy = yOf(v);
        return (vx > ll.x() && vx < ur.x()) && (vy > ll.y() && vy < ur.y());
    }

    // converts individual axis coordinates
    private static int xOf(int index){ return (index) / ticks + 1; }
    private static int yOf(int index){ return (index) % ticks + 1; }

//    public static void main(String[] args) {
//        dim = 10;
//        Grid grid = new Grid(dim);
//        Display display = new Display(dim, new Draw());
//        Draw pane = display.getDraw();
//        display.grid();
//        GridPoint[] points = new GridPoint[]{
//                new GridPoint( 1, 8 ),
//                new GridPoint( 6, 6 ),
//                new GridPoint( 3, 1 ),
//        };
//        display.drawCircles(points);
//        pane.show();
//        pane.pause(100);
//        BreadthFirstPaths bfp = new BreadthFirstPaths(grid.graph(), grid.indicesOf(points));
//        for(int v : bfp.pathTo(indexOf(new GridPoint(6,6)))){
//            display.drawPoint(pointAt(v));
//            pane.pause(100);
//            pane.show();
//        }
//    }
}
// todo make generic and pass indexOf as Function?
// Provides fast removal by recency and fast check for duplicates.
 class StackSet implements Iterable <Integer>{
    private final Stack<Integer> stack  ;
    private final SET <Integer>  treeSet;
    StackSet(){
        stack    = new Stack<>();
        treeSet  = new SET<>();
    }
    boolean contains(GridPoint p){
        return treeSet.contains(Grid.indexOf(p));
    }
    boolean contains(int v){
        return treeSet.contains(v);
    }
    boolean isEmpty(){
        return treeSet.size() == 0;
    }
    void push(GridPoint p){
        int v = Grid.indexOf(p);
        if(!contains(p)){
            stack.push(v);
            treeSet.add(v);
        }
    }
    // for saving all to a single array
    int[] combineArrays(int[] lArray){
        int[] combined = new int[lArray.length + treeSet.size()];
        for(int i = lArray.length ; i < lArray.length + treeSet.size() ; i++) {
            int min = treeSet.min();
            treeSet.remove(min);
            stack.pop();
            combined[i] = min;
        }
        return combined;
    }
    void push(int v){
        if(!contains(v)){
            stack.push(v);
            treeSet.add(v);
        }
    }
    GridPoint pop(){
        int tmp = stack.pop();
        treeSet.remove(tmp);
        return Grid.pointAt(tmp);
    }

    int size(){
        return treeSet.size();
    }
    Iterable<Integer> getSet(){
        return treeSet;
    }
    @Override
    public Iterator<Integer> iterator() {
        return stack.iterator();
    }
}