package grid;

import edu.princeton.cs.algs4.*;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;
import static java.util.Arrays.binarySearch;

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
    private SET<Integer> excludedV;           // TODO no need for set, sorted array is sufficient
    private final int dim;
    private Graph graph;

    public int getDimension(){
        return dim;
    }
    public Grid(int dimension){
        dim = dimension;
        excludedV = new SET<>();
    }

//    public Grid(){
//        dim = 10;
//        this.grid = generateDenseGrid();
//    }
    /// Returns the `Graph` instance.
    public Graph graph(){
        return graph;
    }
    public void replaceGraph(Graph graph){
        this.graph = graph;
    }

    public boolean isExcluded(GridPoint p){
        return excludedV.contains(indexOf(p));
    }
    /// Converts and array of nodes in (x,y) grid coordinates to an array of graph vertices
    public int[] indexArrayOf(GridPoint[] nodes){
        int[] indexArray = new int[nodes.length];
        for(int i = 0 ; i < nodes.length ; i++) {
            indexArray[i] = indexOf(nodes[i]);
        }
        return indexArray;
    }

    ///  Converts an array of nodes as (x,y) grid coordinates to graph vertices
    public Iterable<Integer> indicesOf(GridPoint[] nodes) {
        Bag<Integer> indices = new Bag<>();
        for(int i = 1 ; i < nodes.length ; i+=2) {
            indices.add(indexOf(nodes[i]));
        }
        return indices;
    }

    ///  Converts nodes as (x,y) grid coordinates to graph vertices
    public Iterable<Integer> indicesOf(Iterable<GridPoint> nodes) {
        Bag<Integer> indices = new Bag<>();
        for(GridPoint p : nodes) {
            indices.add(indexOf(p));
        }
        return indices;
    }

    public Iterable<Integer> getWall(){
        return excludedV;
    }

    /// Add a 'wall' to the grid
    public void addWall(GridPoint... points){
        for(GridPoint p : points){
            excludedV.add(indexOf(p));
        }
    }

    // Converts individual 1-based (x, y) coordinates of node to 0-based indexed vertex in `Graph`.
    private int indexOf(int x, int y){
        if(x == 0 || y == 0) return -1; // TODO: Remove and add bounds check elsewhere like file input conversion
        return (x - 1) * dim + (y - 1);
    }

    /// Converts a `Point` as 1-based (x, y) coordinates of node to 0-based indexed vertex in `Graph`.
    public int indexOf(GridPoint p){
        if(p.x() == 0 || p.y() == 0)
            return -1; // TODO: Remove and add bounds check elsewhere like file input conversion
        return ((p.x() - 1) * dim + (p.y() - 1));
    }

    // Converts a 0-based indexed vertex in `Graph` to 1-based (x, y) coordinates of node.
    private int[] coordsAt(int index){
        return new int[]{
                (index) / dim + 1,
                (index) % dim + 1
        };
    }

    /// Converts a 0-based indexed vertex in `Graph` to a Point with 1-based (x, y) coordinates.
    public GridPoint pointAt(int index){
        return new GridPoint(
            (index) / dim + 1,
            (index) % dim + 1);
    }


    private Graph buildGraph(int dim) {
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

    /// Assigns edges to adjacent nodes in a `dim` x `dim` grid, nodes in the grid are only connected horizontally and
    /// vertically to other adjacent nodes. Diagonal connections are NOT created.
    /// Skips attaching edges to excluded vertices.
    /// Nodes at grid-corners have 2 edges, nodes along grid-borders have 3, and internal nodes have 4.
    /// The `Graph` vertices are indexed in the range = \[0, (dim*dim -1 )\] A dense graph is formed by
    public void buildGraph() {
        this.graph = buildGraph(dim);
    }
    
    /// Generates random nodes in the grid
    /// @return - nodes as an `Iterable<GridPoint>`
    public Iterable<GridPoint> generateNodes(int nodeCount){
        SET<GridPoint> uniqueNodes = new SET<>();
        for(int i = 0 ; i < nodeCount; i++) {
            GridPoint n;
            do{
                n = new GridPoint(StdRandom.uniformInt(1, dim), StdRandom.uniformInt(1, dim));
            }while(uniqueNodes.contains(n));
            uniqueNodes.add(n);
        }
        return uniqueNodes;
    }

    /// Generates random nodes in the grid
    /// @return - nodes as an `GridPoint[]`
    public GridPoint[] generateNodeArray(int nodeCount){
        Set<GridPoint> uniqueNodes = new HashSet<>();
        for(int i = 0 ; i < nodeCount; i++) {
            GridPoint n;
            do {
                n = new GridPoint(StdRandom.uniformInt(1, dim), StdRandom.uniformInt(1, dim));
            } while (uniqueNodes.contains(n));
            uniqueNodes.add(n);
        }
        return uniqueNodes.toArray(new GridPoint[0]);
    }

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

        for(int v = 0; v < dim * dim; v++){
            int vx = coordsAt(v)[0];  int vy = coordsAt(v)[1];
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

    /// Creates a subgraph, or window, of a grid graph using inclusive bounds defined by two [GridPoint]s
    /// 'll' (lower left) and 'ur'(upper right).
    /// @return the subGraph
    /// @throws IllegalArgumentException if `ll` and 'ur' are invalid coordinates relative to one another
    public Graph subGraph(GridPoint ll, GridPoint ur, Graph graph){
        if( ur.x() < ll.x() && ur.y() < ll.y())  throw new IllegalArgumentException("invalid bounds provided for subgraph");
        Graph subGraph = new Graph((ur.x() - ll.x()) * (ur.y() - ll.y()));

        for(int v = 0; v < dim * dim; v++){
            int vx = coordsAt(v)[0];  int vy = coordsAt(v)[1];
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

    // is the graph vertex 'v' within the 'll'-'ur' window?
    private boolean contained(int v, GridPoint ll, GridPoint ur){
        int vx = coordsAt(v)[0];  int vy = coordsAt(v)[1];
        if(( vx > ll.x() && vx < ur.x()) && ( vy > ll.y() && vy < ur.y() ))
            return true;
        return false;
    }

    public static void main(String[] args) {
        int dim = 10;
        Grid grid = new Grid(dim);
        Display display = new Display(dim);
        Draw pane = display.getPane();
        display.grid();
        GridPoint[] points = new GridPoint[]{
                new GridPoint( 1, 8 ),
                new GridPoint( 6, 6 ),
                new GridPoint( 3, 1 ),
        };
        display.drawCircles(points);
        pane.show();
        pane.pause(100);
        BreadthFirstPaths bfp = new BreadthFirstPaths(grid.graph(), grid.indicesOf(points));
        for(int v : bfp.pathTo(grid.indexOf(6,6))){
            display.drawPoint(grid.pointAt(v));
            pane.pause(100);
            pane.show();
        }
    }
}
