package maze;

import autoRouter.Display;
import autoRouter.Grid;
import autoRouter.GridPoint;
import edu.princeton.cs.algs4.*;


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
public class BreadthFirstSearchView implements Runnable{

        private static final int INFINITY = Integer.MAX_VALUE;
        private boolean[] marked;  // marked[v] = is there an s-v path
        private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
        private int[] distTo;      // distTo[v] = number of edges shortest s-v path
        final private Grid grid;
        final private GridPoint p;
        final private Draw pane;
        Thread t;

    public BreadthFirstSearchView(GridPoint p, Grid grid, Draw pane) {
        this.p = p ;
        this.grid = grid;
        this.pane = pane;
        Graph graph = grid.graph();
        marked = new boolean[graph.V()];
        distTo = new int[graph.V()];
        edgeTo = new int[graph.V()];
        int s = grid.indexOf(p);
        validateVertex(s);
    }

    @Override
    public void run() {
//        t = new Thread(this, "BFS Wavefront");
//        t.start();
        int s = grid.indexOf(p);
        System.out.println(Thread.currentThread().getThreadGroup());
        bfs(grid, s, pane);
        //        assert check(grid.graph(), s);
    }
        // breadth-first search from a single source
        private void bfs(Grid grid, int s, Draw pane) {
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
                        Display.drawPoint(grid.pointAt(w), Draw.PINK, pane);
                        pane.disableDoubleBuffering();
                        pane.pause(50);
//                        pane.show();

//                        try{
//                            Thread.sleep(100); // instead of pane.pause()
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
                    }
                }
            }
        }


        /**
         * Is there a path between the source vertex {@code s} (or sources) and vertex {@code v}?
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
         * @param v the vertex
         * @return the number of edges in such a shortest path
         *         (or {@code Integer.MAX_VALUE} if there is no such path)
         * @throws IllegalArgumentException unless {@code 0 <= v < V}
         */
        public int distTo(int v) {
            validateVertex(v);
            return distTo[v];
        }

        /**
         * Returns a shortest path between the source vertex {@code s} (or sources)
         * and {@code v}, or {@code null} if no such path.
         * @param  v the vertex
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


        // check optimality conditions for single source
        private boolean check(Graph G, int s) {

            // check that the distance of s = 0
            if (distTo[s] != 0) {
                StdOut.println("distance of source " + s + " to itself = " + distTo[s]);
                return false;
            }

            // check that for each edge v-w dist[w] <= dist[v] + 1
            // provided v is reachable from s
            for (int v = 0; v < G.V(); v++) {
                for (int w : G.adj(v)) {
                    if (hasPathTo(v) != hasPathTo(w)) {
                        StdOut.println("edge " + v + "-" + w);
                        StdOut.println("hasPathTo(" + v + ") = " + hasPathTo(v));
                        StdOut.println("hasPathTo(" + w + ") = " + hasPathTo(w));
                        return false;
                    }
                    if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
                        StdOut.println("edge " + v + "-" + w);
                        StdOut.println("distTo[" + v + "] = " + distTo[v]);
                        StdOut.println("distTo[" + w + "] = " + distTo[w]);
                        return false;
                    }
                }
            }

            // check that v = edgeTo[w] satisfies distTo[w] = distTo[v] + 1
            // provided v is reachable from s
            for (int w = 0; w < G.V(); w++) {
                if (!hasPathTo(w) || w == s) continue;
                int v = edgeTo[w];
                if (distTo[w] != distTo[v] + 1) {
                    StdOut.println("shortest path edge " + v + "-" + w);
                    StdOut.println("distTo[" + v + "] = " + distTo[v]);
                    StdOut.println("distTo[" + w + "] = " + distTo[w]);
                    return false;
                }
            }

            return true;
        }

        // throw an IllegalArgumentException unless {@code 0 <= v < V}
        private void validateVertex(int v) {
            int V = marked.length;
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }

        // throw an IllegalArgumentException if vertices is null, has zero vertices,
        // or has a vertex not between 0 and V-1
        private void validateVertices(Iterable<Integer> vertices) {
            if (vertices == null) {
                throw new IllegalArgumentException("argument is null");
            }
            int vertexCount = 0;
            for (Integer v : vertices) {
                vertexCount++;
                if (v == null) {
                    throw new IllegalArgumentException("vertex is null");
                }
                validateVertex(v);
            }
            if (vertexCount == 0) {
                throw new IllegalArgumentException("zero vertices");
            }
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
