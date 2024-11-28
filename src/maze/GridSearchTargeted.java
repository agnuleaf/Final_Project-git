package maze;

import grid.Display;
import grid.GridPoint;
import grid.Grid;
import edu.princeton.cs.algs4.*;

/// TODO DOES NOT REMEMBER THE SHORTEST PATH.
/// TODO Need to associate a 'predecessor label' with each stacked neighbor
///
/// 'brute force'
/// Brainstorm notes:
/// Can we define a branch somehow based off the target, source and stack history??
/// Features of a dead end path:
/// - If the target is not collinear with the path, a turn must occur. Otherwise it is a dead end
///     - When does this dead end begin ? the top of the stack
///     - How long is this dead end ??  peek stack for the node coordinates,
///         - pStack : one of the coordinates is off by one(in the orthogonal direction),
///             the other (collinear direction)is off by the number of steps taken
///         - nStack : Can either follow the pStack rule, or if collinear it is off by (number of steps - 1)
///
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
    public /*Iterable<Point>*/ void searchWithBacktrack(GridPoint p, GridPoint q) {
        gU = grid.indexOf(p);
        gT = grid.indexOf(q);
        Stack<Integer> pStack = new Stack<>();          // neighbors closer to target
        Stack<Integer> nStack = new Stack<>();          // neighbors away from target
        Stack<GridPoint> pathStack = new Stack<>();
        Graph graph = grid.graph();
        marked[gU] = true;
        pathStack.push(grid.pointAt(gU));

        while (gU != gT) {
            GridPoint u = grid.pointAt(gU); // currently visited node
            Bag<Integer> closer =  new Bag<>(); // closer neighbors
            for (int gV : graph.adj(gU)) {  // evaluate each neighbor of u
                if (!marked[gV]) {
                    GridPoint v = grid.pointAt(gV); // the neighbor
                    if (v.isFarther(u, q)) {
                        nStack.push(gV);
                    }else if(!v.isFarther(u, q)){
                        pStack.push(gV);
                    }
                }
            }

            if (!pStack.isEmpty()) {
//                if (!marked[pStack.peek()]) {
                    gU = pStack.pop();
//                    pathStack.pop();        // TODO How to pop the right amount of nodes??
                    marked[gU] = true;
//                    pathStack.push(grid.pointAt(gU));

                    Display.drawPoint(grid.pointAt(gU), Draw.BOOK_RED, pane);     // VISUAL
                    pane.pause(50);
                    pane.show();

                    // StdOut.printf("dfs(%d)\n", w);
//                } else {
//                 // --- Inefficient Backtrack Step: Pop pathStack until neighboring
//
//                    pStack.pop();               // FIXME
//                    pathStack.pop();
//                }
            } else if (!nStack.isEmpty()) {
                pStack = nStack;        // swap stacks, find a possible detour
                nStack = new Stack<>();

            } else { // if both stacks are empty there is no path!
                // TODO
            }
        }
//        return pathStack;
    }

    //    private boolean isCloserTo
    public static void main(String[] args) {
        int dim = 10;

        Draw pane = Display.init(10);
        Grid grid = new Grid(dim);
        grid.addWall(new GridPoint[]{
                new GridPoint(3, 2),
                new GridPoint(2, 2),
                new GridPoint(4,1)
        });
        GridPoint[] nodes = new GridPoint[]{
                new GridPoint(1, 1),
                new GridPoint(3, 5)
        };
        grid.buildGraph();
        pane = Display.init(dim);
        Display.drawCircles(nodes, pane);
        pane.pause(100);
        pane.show();
        GridSearchTargeted gs = new GridSearchTargeted(grid, pane);
        /*Iterable<Point> path = */gs.searchWithBacktrack( nodes[0], nodes[1]);
//        int count = 0;
//        for(Point p : path){
//            Display.drawPath(p, 0.01, Draw.BLUE, pane);
//            pane.show();
//            count++;
//        }
//        System.out.println(count); // FIXME correct pathresult for grids with obstacles
    }

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