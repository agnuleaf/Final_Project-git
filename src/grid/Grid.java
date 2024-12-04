package grid;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdRandom;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Provides a dense graph representation of a 2D grid using [Graph] api, and conversions from its 0-based vertex array
 * to 1-based coordinates of the positive XY plane presented as an immutable [GridPoint].
 * A 2D rectilinear grid graph contains equally distanced nodes, with only vertical and horizontal edges to adjacent nodes.
 * 
 * In a square grid, by disallowing diagonal movement the minimum distance between p and q becomes :
 * dM = |q.x - p.x| + |q.y - p.y| , called Manhattan or taxicab distance.
 * Unlike the more familiar Euclidean distance on a Cartesian plane or the Chebyshev distance for a king on a chessboard,
 * there can be many routes with equal Manhattan distance exist can take possible shortest paths from p to q.
 * 
 * [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry)
 * [Lattice graph](https://en.wikipedia.org/wiki/Lattice_graph)
 * [Lattice path](https://en.wikipedia.org/wiki/Lattice_path)
 * [Hanan Grid](https://en.wikipedia.org/wiki/Hanan_grid)
 * 
 * @author Wesley Miller, Ty Greenburg
 */
public class Grid {
    private int width = 5;     // squares for x axis
    private int height ;
    private StackSet<Integer> excludedV;   //  walls from user input, can pop the most recent and search faster than stack
    private Queue<Integer> endpoints;
    private final int endpointsAllowed = 2;  // only 2 endpoints at a time allowed
    private Graph graph;

    /**
     * Constructor for a square Grid
     * @param width number of squares along each axis
     */
    public Grid(int width){
        this.width = width;
        height = this.width;
        excludedV = new StackSet();
        endpoints = new Queue<>();
    }

    /**
     * Gets the width
     * @return width
     */
    public int getWidth(){
        return width;
    }
    
    /**
     * Gets the Height
     * @return height
     */
    public int getHeight(){
        return height;
    }
    
    /**
     * The number of walls placed for this phase
     * @return # of walls
     */
    public int countWalls(){
        return excludedV.stackSize() ;
    }
    
    /**
     * Gets the number of walls
     * @return walls on the grid
     */
    public int totalCountWalls(){
        return excludedV.size();
    }
    
    /**
     * Gets all walls in the grid
     * @return
     */
    public Iterable<Integer> getWalls(){
        return excludedV.getSet();
    }
    
    /**
     * Gets the endpoints size
     * @return size
     */
    public int endpointsSize(){
        return endpoints.size();
    }

    /**
     * Tries to add endpoint to grid
     * @param p point
     * @return true if add was successful
     */
    public boolean addEndpoint(GridPoint p) {
        if (!isEndpoint(p) && !isWall(p) && endpointsSize() < endpointsAllowed) {
            endpoints.enqueue(indexOf(p));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if a given point is an endpoint
     * @param p point
     * @return true if point already exists as an enpoint
     */
    public boolean isEndpoint(GridPoint p){
        boolean b = false;           // check if the  queue contains p
        for(int v : endpoints){  b |= (v == indexOf(p));  }
        return b;
    }

    /**
     * Gets endpoints as in the order start to end
     * @return endpoints as a Queue
     */
    public Queue<GridPoint> getEndpoints(){
        Queue<GridPoint> q = new Queue<>();
        return q;
    }

    /**
     * Gets the start endpoint
     * @return endpoints Start
     */
    public GridPoint getStart(){
        return pointAt(endpoints.peek());
    }

    /**
     * Gets the final endpoint
     * @return endpoints End
     */
    public GridPoint getEnd(){
        int start = endpoints.dequeue();
        int end = endpoints.dequeue();
        endpoints.enqueue(start);
        endpoints.enqueue(end);
        return pointAt(end);
    }
    
    /**
     * removes the last endpoint
     * @return Point
     */
    public GridPoint removeLastEndpoint(){
        if(endpoints.isEmpty()) return null;
        return pointAt(endpoints.dequeue());
    }
    
    /**
     * Returns an array of index of the points
     * @param points
     * @return index of points
     */
    public int[] indexArrayOf(GridPoint[] points){
        int[] indexArray = new int[points.length];
        for(int i = 0 ; i < points.length ; i++) {
            indexArray[i] = indexOf(points[i]);
        }
        return indexArray;
    }

    /**
     * Converts an array of points as (x,y) grid coordinates to graph vertices
     * @param points
     * @return indices
     */
    public Iterable<Integer> indicesOf(GridPoint[] points) {
        Bag<Integer> indices = new Bag<>();
        for(int i = 1 ; i < points.length ; i+=2) {
            indices.add(indexOf(points[i]));
        }
        return indices;
    }

    /**
     * Checks to add wall unless an endpoint or wall already exists there
     * @param p
     * @return true if successfully added a wall
     */
    public boolean addWall(GridPoint p){
        if(!isWall(p) && !isEndpoint(p)){
            excludedV.push(indexOf(p));
            return true;
        }
        else if(isEndpoint(p) || isWall(p)){                 
            System.out.println("can't add wall on wall or other endpoint");
            return false;
        }
        else{
            throw new IllegalStateException("attempted to at wall at invalid location: "+p);
        }
    }

    /**
     * Checks if the given point is a wall
     * @param p point
     * @return true if its a wall
     */
    public boolean isWall(GridPoint p){
        return excludedV.contains(indexOf(p));
    }

    /**
     * Removes the last wall placed, or null if there is none
     * @return Point removed
     */
    public GridPoint removeLastWall() {
        return (!excludedV.isStackEmpty()? pointAt(excludedV.remove()) : null);
    }

    /**
     * Converts a Point as 1-based (x, y) coordinates of node to 0-based indexed vertex in Graph
     * @param p point
     * @return index
     */
    public int indexOf(GridPoint p){
        if(p.x() == 0 || p.y() == 0)
            return -1; 
        return ((p.x() - 1) * width + (p.y() - 1));
    }

    /**
     * Finds the GridPoint at a given index
     * @param index
     * @return point at given index
     */
    public GridPoint pointAt(int index){
        return new GridPoint(
            (index) / width + 1,
            (index) % width + 1);
    }
    
    /**
     * Checks if two points are in the different quadrants
     * @param p Point 1
     * @param q Point 2
     * @return true if points are in different quadrants
     */
    public  boolean onDifferentQuads(GridPoint p, GridPoint q){
        boolean testX = (p.x() > width/2) ^ (q.x() > width/2);
        boolean testY = (p.y() > height/2) ^ (q.y() > height/2);
        return testX || testY;
    }


   /**
    * returns the graph 
    * @return graph
    */
    public Graph graph(){
        return graph;
    }

    /**
     * Assigns edges to adjacent nodes in a `dim` x `dim` grid, nodes in the grid are only connected horizontally and
     * vertically to other adjacent nodes. Diagonal connections are NOT created.
     * Skips attaching edges to excluded vertices.
     * Nodes at grid-corners have 2 edges, nodes along grid-borders have 3, and internal nodes have 4.
     * The Graph vertices are indexed in the range = \[0, (dim*dim -1 )\]
     * @return Graph
     */
    public Graph buildGraph() {
        this.graph = new Graph(width * height);

        for(int v = 0; v < width * height; v++){
            if(!excludedV.contains(v)){
                if(v < width * height - (height)  && !excludedV.contains(v+ height)) // skip attaching beyond top border and excluded
                    graph.addEdge(v, v + width);
                if((v + 1) % width != 0   && !excludedV.contains(v+1)) // skip attaching beyond right border and excluded
                    graph.addEdge(v, (v+1));
            }
        }
        return graph;
    }

    /**
     * Finds the number of total squares
     * @return total number of squares
     */
    int count(){
        return height*width;
    }
    
    /**
     * Finds the number of unoccupied squares
     * @return number of unoccupied squares
     */
    int countUnoccupied(){
        return count() - (endpointsSize() + totalCountWalls());
    }

    /**
     * Generates a random set of GridPoints within the grid's bounds and under its limit
     * @param count
     * @return unique set of GrindPoints
     */
    public SET<GridPoint> generateGridPoints(int count){
        int pointCount = Math.max(count, this.countUnoccupied());
        SET<GridPoint> uniqueNodes = new SET<>();
        for(int i = 0 ; i < pointCount; i++) {
            int tries = 0;
            GridPoint n = new GridPoint(StdRandom.uniformInt(1, width), StdRandom.uniformInt(1, width));
            while(uniqueNodes.contains(n) && (tries < 4 )){
                n = new GridPoint(StdRandom.uniformInt(1, width), StdRandom.uniformInt(1, width));
                tries++;
            }
            if(!uniqueNodes.contains(n)){ uniqueNodes.add(n); };
        }
        return uniqueNodes;
    }

    /**
     * Resets the grid completely or resets retaining the walls placed
     * @param doSaveWalls
     */
    public void restart(boolean doSaveWalls){
         if(doSaveWalls) {
             while(!excludedV.isStackEmpty()){ // unload sessions stack but retain the set
                 excludedV.pop();
             }
         }else {    // reset completely
             excludedV = new StackSet();
         }
         endpoints = new Queue<>();
    }

    /**
     * Generates a unique random GridPoint[]
     * @param count number of GrindPoints wanted
     * @return GridPoint[]
     */
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

    /**
     * Provides fast removal by recency and fast check for duplicates.
     * Inner class for debug: allows using pointAt and indexOf without making the grid dimensions (ticks) static
     * @param <E>
     */
    class StackSet<E extends Comparable<E>> implements Iterable <E>{
        private final Stack<E> stack  ;
        private final SET <E>  treeSet;
        StackSet(){
            stack    = new Stack<>();
            treeSet  = new SET<>();  // better to use HashSet rather than use compareTo in TreeSet
        }
        boolean contains(E v){
            return treeSet.contains(v);
        }
        boolean isEmpty(){
            return treeSet.isEmpty();
        }
        boolean isStackEmpty() { return stack.isEmpty(); }
        void push(E v){
            if( !contains(v) ){
                stack.push(v);
                treeSet.add(v);
            }
        }
        // only pops the stack, leaving the element in the set. For storing between sessions
        E pop(){
            return stack.pop();
        }
        E remove(){

            E tmp = stack.pop();
            treeSet.remove(tmp);
            return tmp;
        }
        E peek(){
            return stack.peek();
        }
        int size(){
            return treeSet.size();
        }
        int stackSize(){
            return stack.size();
        }

        Iterable<E> getSet(){
            return treeSet;
        }

        public Iterator<E> iterator() {
            return treeSet.iterator();
        }
    }
}