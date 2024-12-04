package maze;

import edu.princeton.cs.algs4.Graph;
import grid.GridDraw;
import grid.GridPoint;
import grid.Grid;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Draw;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/// The {@code BreadthFirstPaths} class represents a data type for finding
/// shortest paths (number of edges) from a source vertex <em>s</em>
/// (or a set of source vertices)
/// to every other vertex in an undirected graph.
/// <p>
/// This implementation uses breadth-first search.
/// The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
/// worst case, where <em>V</em> is the number of vertices and <em>E</em>
/// is the number of edges.
/// Each instance method takes &Theta;(1) time.
/// It uses &Theta;(<em>V</em>) extra space (not including the graph).
/// <p>
/// For additional documentation,
/// see <a href="https://algs4.cs.princeton.edu/41graph">Section 4.1</a>
/// of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
///
/// @author Robert Sedgewick
/// @author Kevin Wayne
public class BreadthFirstSearchView {

    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path

    private Grid grid;
    private GridPoint p;

    //        private int tPause;

    /**
     * Constructor used to map the possible connections in the unweighted graph.
     * @param gridDraw - used to access the `Grid` object which holds the [Graph] object, used to vertices
     *                 in the order they are visited by the breadth first search algorithm.
     */
    public BreadthFirstSearchView(GridDraw gridDraw) {
        // For displaying the visited nodes
        grid = gridDraw.getGrid();
        p = gridDraw.getGrid().getStart();

        // Original Constructor below
        Graph graph = grid.buildGraph();
        marked = new boolean[graph.V()];
        distTo = new int[graph.V()];
        edgeTo = new int[graph.V()];
        int s = grid.indexOf(p);
        validateVertex(s);
    }


    /// Runs the breadth first search method `bfs` and exports the vertices in the order they are visited, for external
    /// animation. Call this method from a new `Thread` when running on the Event Dispatch Thread,
    /// or else all individual draw calls will be combined and occur simultaneously after the delay, defeating
    /// the attempt at animation.
    /// @return the `Queue` of visited vertices as `GridPoint`s
    public Queue<GridPoint> viewWave() {
        return bfs(grid.indexOf(p));
    }

    // breadth-first search from a single source
    private Queue<GridPoint> bfs(int s) {
        Queue<GridPoint> qConverted = new Queue<>();
        Graph graph = grid.graph();
        Queue<Integer> q = new Queue<>();
        for (int v = 0; v < graph.V(); v++)
            distTo[v] = INFINITY;
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : graph.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                    qConverted.enqueue(grid.pointAt(w));
                }
            }
        }
        return qConverted;
    }
// NO MODIFICATIONS BELOW
    /**
     * Is there a path between the source vertex {@code s} (or sources) and vertex {@code v}?
     *
     * @param v the vertex
     * @return {@code true} if there is a path, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    /**
     * Returns the number of edges in a shortest path between the source vertex {@code s}
     * (or sources) and vertex {@code v}?
     *
     * @param v the vertex
     * @return the number of edges in such a shortest path
     * (or {@code Integer.MAX_VALUE} if there is no such path)
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    /**
     * Returns a shortest path between the source vertex {@code s} (or sources)
     * and {@code v}, or {@code null} if no such path.
     *
     * @param v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }
}



/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/