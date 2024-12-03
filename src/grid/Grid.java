package grid;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

import edu.princeton.cs.algs4.SET;      // import java.util.HashSet;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdRandom;


import java.util.HashSet;
import java.util.Iterator;

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
    private int width = 5;     // squares for x axis
    private int height ;
    private StackSet excludedV;   //  walls from user input, can pop the most recent and search faster than stack
    private Queue<Integer> endpoints;
    private final int endpointsAllowed = 2;  // only 2 endpoints at a time allowed
    private Graph graph;

    /// Constructor for a square `Grid` instance where width and height are the same.
    /// @param width number of squares along each axis
    public Grid(int width){
        this.width = width;
        height = this.width;
        excludedV = new StackSet();
        endpoints = new Queue<>();
    }

    /// The number of walls placed for this phase.
    public int countWalls(){
        return excludedV.stackSize() ;
    }
    /// The total number of walls in the grid.
    public int totalCountWalls(){
        return excludedV.size();
    }
    /// Gets all walls in the grid.
    public Iterable<Integer> getWalls(){
        return excludedV.getSet();
    }
    /// The most recently added endpoint's place in Queue.
    public int endpointsSize(){
        return endpoints.size();
    }

    /// Tries to add endpoint to grid, returning true if add is successful.
    public boolean addEndpoint(GridPoint p) {
        if (!isEndpoint(p) && !isWall(p) && endpointsSize() < endpointsAllowed) {
            endpoints.enqueue(indexOf(p));
            return true;
        } else {
//            System.out.println(
//                    "failed to add endpoint" + p);    // incase we allow walls to accumulate
            return false;
        }
    }

    /// Check if a given point is an endpoint
    /// @return true if point already exists as an endpoint
    public boolean isEndpoint(GridPoint p){
        boolean b = false;           // check if the  queue contains p
        for(int v : endpoints){  b |= (v == indexOf(p));  }
        return b;
    }

    ///  Gets endpoints as in the order start to end
    /// @return endpoints as a `Queue<GridPoint>`
    public Queue<GridPoint> getEndpoints(){
        Queue<GridPoint> q = new Queue<>();
        return q;
    }

    /// Gets the start endpoint
    public GridPoint getStart(){
        return pointAt(endpoints.peek());
    }
    ///  Gets the final endpoint
    public GridPoint getEnd(){
        int start = endpoints.dequeue();
        int end = endpoints.dequeue();
        endpoints.enqueue(start);
        endpoints.enqueue(end);
        return pointAt(end);
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

    /// Checks to add wall unless an endpoint or wall already exists there.
    /// @return true if successfully added a wall, otherwise false.
    public boolean addWall(GridPoint p){
        if(!isWall(p) && !isEndpoint(p)){
            excludedV.push(indexOf(p));
            return true;
        }
        else if(isEndpoint(p) || isWall(p)){                  // DEBUG print for
            System.out.println("can't add wall on wall or other endpoint");
            return false;
        }
        else{
            throw new IllegalStateException("attempted to at wall at invalid location: "+p);
        }
    }

    /// Checks if the given point is a wall
    public boolean isWall(GridPoint p){
        return excludedV.contains(indexOf(p));
    }

    /// Removes the last wall placed, or null if there is none.
    public GridPoint removeLastWall() {
        return (!excludedV.isStackEmpty()? pointAt(excludedV.remove()) : null);
    }

    /// Converts a `Point` as 1-based (x, y) coordinates of node to 0-based indexed vertex in `Graph`.
    public int indexOf(GridPoint p){
        if(p.x() == 0 || p.y() == 0)
            return -1; // TODO: Remove and add bounds check elsewhere like file input conversion
        return ((p.x() - 1) * width + (p.y() - 1));
    }

    /// Converts a 0-based indexed vertex in `Graph` to a Point with 1-based (x, y) coordinates.
    public GridPoint pointAt(int index){
        return new GridPoint(
            (index) / width + 1,
            (index) % width + 1);
    }
    /// Builds the graph with the grid's data then returns it.
    public Graph graph(){
        buildGraph();
        return graph;
    }
    /** 
     Assigns edges to adjacent nodes in a `dim` x `dim` grid, nodes in the grid are only connected horizontally and
     vertically to other adjacent nodes. Diagonal connections are NOT created.
     Skips attaching edges to excluded vertices.
     Nodes at grid-corners have 2 edges, nodes along grid-borders have 3, and internal nodes have 4.
     The `Graph` vertices are indexed in the range = \[0, (dim*dim -1 )\] A dense graph is formed by
     **/
    private void buildGraph() {
        this.graph = new Graph(width * width);

        for(int v = 0; v < width * width; v++){
            if(!excludedV.contains(v)){
                if(v < width * width - (width)  && !excludedV.contains(v+ width)) // skip attaching beyond top border and excluded
                    graph.addEdge(v, v + width);
                if((v + 1) % width != 0   && !excludedV.contains(v+1)) // skip attaching beyond right border and excluded
                    graph.addEdge(v, (v+1));
            }
        }
    }

    /// The total number of squares in the grid, including empty and nonempty
    int count(){
        return height*width;
    }
    int countUnoccupied(){
        return count() - (endpointsSize() + countWalls());
    }
    /// Generates a random set of `GridPoint`s within the grid's bounds and under its limit
    /// @return - set of `GridPoint`s
    public SET<GridPoint> generateNodes(int count){
        int pointCount = Math.max(count, this.countUnoccupied());
        SET<GridPoint> uniqueNodes = new SET<>();
        for(int i = 0 ; i < pointCount; i++) {
            GridPoint n;
            do{
                n = new GridPoint(StdRandom.uniformInt(1, width), StdRandom.uniformInt(1, width));
            }while(uniqueNodes.contains(n));
            uniqueNodes.add(n);
        }
        return uniqueNodes;
    }
    /// Resets the grid completely or resets retaining the walls placed.
    public void restart(boolean doSaveWalls){
         if(doSaveWalls) {
             while(!excludedV.isStackEmpty()){ // unload sessions stack but retain the set
                 excludedV.pop();
             }
         }else {    // reset completely
             excludedV = new StackSet();
         }
         endpoints = new Queue<>();
         // We could load the shortest path to the endpoints queue and add to set walls


    }
    /// Generates a unique random `GridPoint[]`
    public GridPoint[] generateNodeArray(int count){
        HashSet<GridPoint> uniqueNodes = new HashSet<>();
        int pointCount = Math.max(count, this.countUnoccupied());
        for(int i = 0 ; i < pointCount; i++) {
            GridPoint n;
            do {
                n = new GridPoint(StdRandom.uniformInt(1, width), StdRandom.uniformInt(1, width));
            } while (uniqueNodes.contains(n));
            uniqueNodes.add(n);
        }
        return uniqueNodes.toArray(new GridPoint[0]);
    }

    // Provides fast removal by recency and fast check for duplicates.
    // Inner class for debug: allows using pointAt and indexOf without making the grid dimensions (ticks) static
    class StackSet implements Iterable <Integer>{
        private final Stack<Integer> stack  ;
        private final SET <Integer>  treeSet;
        StackSet(){
            stack    = new Stack<>();
            treeSet  = new SET<>();  // better to use HashSet rather than use compareTo in TreeSet
        }
        boolean contains(int v){
            return treeSet.contains(v);
        }
        boolean isEmpty(){
            return treeSet.isEmpty();
        }
        boolean isStackEmpty() { return stack.isEmpty(); }
        void push(int v){
            if( !contains(v) ){
                stack.push(v);
                treeSet.add(v);
//                System.out.print(pointAt(v)
//                        + "push successes\n stack n: "+ stack.size()
//                        + " treeSee n:" + treeSet.size());
            }
//            else{
//                System.out.println(pointAt(v)
//                        + "push failed\t is something in the way?");
//            }
        }
        // only pops the stack, leaving the element in the set. For storing between sessions
        int pop(){
            return stack.pop();
        }
        int remove(){

            int tmp = stack.pop();
//            System.out.print(pointAt(tmp) + "popped");
            treeSet.remove(tmp);
//            System.out.println("treeSet n:" + treeSet.size() + " stack n:" + stack.size());
            return tmp;
        }
        int peek(){
            return stack.peek();
        }
        int size(){
            return treeSet.size();
        }
        int stackSize(){
            return stack.size();
        }

        Iterable<Integer> getSet(){
            return treeSet;
        }
        @Override
        public Iterator<Integer> iterator() {
            return stack.iterator();
        }
    }
}