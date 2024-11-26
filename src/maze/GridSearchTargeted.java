package maze;

import autoRouter.Display;
import autoRouter.Point;
import autoRouter.Grid;
import edu.princeton.cs.algs4.*;

/// Search for a target in a rectilinear grid graph. Finds a path to the target, using two stacks of neighbors
/// of previously visited nodes. The `pStack` contains neighbors closer to target than the node at which they were
/// stacked, and an `nStack` contains neighbors farther from target.
/// Unlike a BFS-style, it heads directly toward the target instead of expanding a search radius.
/// 'graph-index' refers to the vertex's position in the array of the `algs4.Graph` instance.
/// 'node' is the `Point` instance from mapping its 'graph-index' to a 2D-plane.
/// or `int[2]` //TODO remove int[2] references outside `Grid` and any unnecessary methods
public class GridSearchTargeted implements Runnable {

    private boolean[] marked;                   // marked[v] = is there an s-v path?
    private int gU;
    private int gT;
    final private Grid grid;
    private Draw pane;       // VISUAL


    /// Computes a shortest path to `q` from `p` in ~sqrt(V) time for a square grid.
    /// @param grid the grid graph instance
    /// @throws IllegalArgumentException unless {@code 0 <= p,q < V}
    public GridSearchTargeted(Grid grid, Draw pane) {
        this.grid = grid;
        this.pane = pane;
        //  currently visited graph vertex in 'index-form'
        //  target graph vertex
        marked = new boolean[grid.graph().V()];
        validateVertex(gU);
        validateVertex(gT);
    }
    /// Less expansive search than BFS
    /// @param p the source node in the grid
    /// @param q the target node in the grid
    public Iterable<Point> search(Point p, Point q) {
        gU = grid.indexOf(p);
        gT = grid.indexOf(q);
        Stack<Integer> pStack = new Stack<>();          // neighbors closer to target
        Stack<Integer> nStack = new Stack<>();          // neighbors away from target
        Stack<Point> pathStack = new Stack<>();
        Graph graph = grid.graph();
        marked[gU] = true;
        pathStack.push(grid.pointAt(gU));

        while (gU != gT) {
            Point u = grid.pointAt(gU); // currently visited node`
            for (int gV : graph.adj(gU)) {  // evaluate each neighbor of u
                if (!marked[gV]) {
                    Point v = grid.pointAt(gV); // the neighbor
                    if (v.isFarther(u, q)) {
                        nStack.push(gV);
                    } else {
                        pStack.push(gV);
                    }
                }
            }
            if (!pStack.isEmpty()) {
                if (!marked[pStack.peek()]) {  // FIXME pop pathStack when obstacle found
                    gU = pStack.pop();
                    marked[gU] = true;
                    pathStack.push(grid.pointAt(gU));
                    // discovered vertex w for the first time
                    Display.drawPoint(grid.pointAt(gU), Draw.BOOK_RED, pane);     // VISUAL
                    pane.pause(50);
                    pane.show();
                    // edgeTo[w] = v;
                    // StdOut.printf("dfs(%d)\n", w);
                } else {
                    pStack.pop();               // FIXME
//                    pathStack.pop();
                }
            } else if (!nStack.isEmpty()) {
                pStack = nStack;        // swap stacks, find a possible detour
                nStack = new Stack<>();

            } else { // if both stacks are empty there is no path!
                // TODO
            }
        }
        return pathStack;

    }

    //    private boolean isCloserTo
    public static void main(String[] args) {
        int dim = 10;
        Draw pane = Display.init(10);
        Grid grid = new Grid(dim);
        Point[] nodes = new Point[]{
                new Point(1, 1),
                new Point(3, 5)
        };
        pane = Display.init(dim);
        Display.drawCircles(nodes, pane);
        pane.pause(100);
        pane.show();
        GridSearchTargeted gs = new GridSearchTargeted(grid, pane);
        Iterable<Point> path = gs.search( nodes[0], nodes[1]);
        int count = 0;
        for(Point p : path){
            Display.drawPath(p, 0.01, Draw.BLUE, pane);
            pane.show();
            count++;
        }
        System.out.println(count); // FIXME correct pathresult for grids with obstacles
    }
    // TODO  Display 4 iterations of performing a `p` to `q` search where `p` and `q` are both internal nodes.
    // For each path block the travelled nodes except `p` and `q`


    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    @Override
    public void run() {

    }
}