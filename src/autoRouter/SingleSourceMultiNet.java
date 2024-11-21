package autoRouter;

/*  import edu.princeton.cs.algs4.Graph;
    import edu.princeton.cs.algs4.BreadthFirstPaths;
    import edu.princeton.cs.algs4.Draw;
    import edu.princeton.cs.algs4.Bag;
    import edu.princeton.cs.algs4.IndexMinPQ;
    import java.awt.Color;
*/
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.IndexMinPQ;
import java.awt.Color;
import static java.lang.Math.abs;
/// # Background
/// A square grid graph contains equally distanced nodes, connected only to vertically or horizontally adjacent nodes.
/// This is unlike the familiar 2D Cartesian plane, where we define the distance between two points as the length of
/// the hypotenuse of a triangle (or line if collinear) containing those points as corners.
/// This square grid disallows diagonal movement, so the distance between any two points is now the sum of the sides of
/// that same triangle. Additionally, all other patterns maintaining course towards the target are equivalent. This
/// length is termed [Manhattan distance.](https://en.wikipedia.org/wiki/Taxicab_geometry)
/// # Implementation
/// <p>`SingleSourceMultiNet` aims to connect all the given nodes, while minimizing the number of intermediate nodes
/// used to connect them.</p>
/// <p>TODO ideally we find the MST of the cheapest total cost of connecting nodes. But node weights are determined by
/// node count where many distinct possibilities exist for every node to node connection.</p>
/// - The input node array is of the form:
///
/// ```
/// p.x   p.y
/// q0.x  q0.y
/// ...
/// qi.x  qi.y
/// ```
///
/// <p>For source 'p' and multiple targest 'q',
///     - sort q by their distance from p
/// * find 1 or 2 shortest paths to q0, p and q are axis aligned there is only one, else consider the two with minimal turns.
/// * ? backtrack the path scanning for the shortest path to next nearest q
///
/// * Find SPs p to q0(closest target)  .
/// To arrive at q, from p two separate intermediate points r = (0, a.y + p.y), s = (a.x + p.x, 0) exist
///
/// ```
///  3 nodes                         option a                    option b
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  p  -  -  -  -  -  -      -  p  -  -  -  -  -  -      -  p -------------.  -
///  -  -  -  -  -  -  -  -      -  |  -  -  -  -  -  -      -  -  -  -  -  -  |  -
///  -  -  -  -  -  -  q0 -      -  | _ _ _ _ _ _ _q0 -      -  -  -  -  -  -  q0 -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///  -  -  -  q1 -  -  -  -      -  -  -  q1 -  -  -  -      -  -  -  q1 -  -  -  -
///  -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///```
///  * Need to figure out how can MST select the best option for a route to cover every q.
///    + if we separate options a and b into separate graphs MST can be peformed on each graph.
///       - Issues: The number of combinations gets out of hand.
///
/// - Possible solution: Determine  nodes to add, n2 and n1,  Perform BFS separately on p0->n1->q0 and p->n2->q0 .
/// Adding n1 yields option a and n2 yields option b
///  Issues:
/// - Considers only two Manhattan distances, despite many other valid zig-zag patterns equal to 'Manhattan' Distance.
///
/// ```
///     add 2 virtual nodes          n1 gives option a          n2 gives  option b
///   -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
///   -  p  -  -  -  -  n2 -      -  p  -  -  -  -  -  -      -  p -------------.  -
///   -  -  -  -  -  -  -  -      -  |  -  -  -  -  -  -      -  -  -  |  -  -  |  -
///   -  n1 -  -  -  -  q0 -      -  | _ _ x _ _ _ _q0 -      -  -  -  |  -  -  q0 -
///   -  -  -  -  -  -  -  -      -  -  -  |  -  -  -  -      -  -  -  |  -  -  -  -
///   -  -  -  -  -  -  -  -      -  -  -  |  -  -  -  -      -  -  -  |  -  -  -  -
///   -  -  -  -  -  -  -  -      -  -  -  |  -  -  -  -      -  -  -  |  -  -  -  -
///   -  -  -  -  -  -  -  -      -  -  -  |  -  -  -  -      -  -  -  |  -  -  -  -
///   -  -  -  q1 -  -  -  -      -  -  -  q1 -  -  -  -      -  -  -  q1 -  -  -  -
///   -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -      -  -  -  -  -  -  -  -
/// ```
/// ## References
///
/// - [Graph slides from University of Utah's CS 2420](https://github.com/tsung-wei-huang/cs2420)
/// @author Wesley Miller
public class SingleSourceMultiNet {
    static int dim = 10;
    // returns an array of the distance from the first node (coords[0],coords[1]) to the other pairs
    static int[] distanceArray(int[] source, int[] coords){
        assert(coords.length %2 == 0);
        int[] distances = new int[(coords.length)/2];
        for(int i = 0; i < coords.length; i += 2 ){
            distances[i/2] = distance(source[0], source[1], coords[i], coords[i + 1] );
        }
        return distances;
    }
    static int[] distanceArray(int[] coords){
      return distanceArray(new int[]{coords[0], coords[1]}, coords);
    }

    static int distance(int x1, int y1, int x2, int y2){
        return abs(x2 - x1) + abs(y2 - y1);
    }

    public static void main(String[] args) {
        Graph grid = generateDenseGrid(); // dense dim x dim graph with unweighted edges connecting adjacent squares
        //        int[] p = { 1, 1};
        //        int[] nodes = {
        //                3, 3,       // source
        //                3, 5,
        //                6, 7,
        //                4, 5,
        //                3, 9
        //        };
        //        int[] nodes = {
        //                1, 1,       // source
        //                1, 4,
        //                3, 2,
        //                2, 5,
        //        };

        int[] nodes = {   // |dist|   dist
                1, 8,           //   0
                6, 6,           //   7
                3, 1,           //   9
        };                      //
        int[] dist = distanceArray(nodes);      // sort nodes[] by distance from source

        // Sort the input array by its distance from the first entry
        // TODO to find the start node 'p', find the 2 closest elements in the input array
        IndexMinPQ<Integer> minDistance = new IndexMinPQ<>(dist.length);
        for (int i = 0; i < dist.length; i++) {
            minDistance.insert(i, dist[i]);
        }

        Draw pane = new Draw();
        Display.init(pane);
        Display.drawCircles(nodes, pane);
        pane.show();
        // need to add a virtual node in between p and q to ensure

        for (int i = 0; i < minDistance.size(); i+=2){
            System.out.print(minDistance.keyOf(i/2) + " d:");
            int dx = nodes[i+2] - nodes[0];
            int dy = nodes[i+3] - nodes[1];
            System.out.println(dx + " " + dy);
            Display.drawCircle(dx + nodes[0],nodes[1], Color.BLUE,pane);     // VISUAL: virtual node to coerce bfs
            Display.drawCircle(nodes[0],dy + nodes[1], Color.BLUE,pane);
            pane.show();
            //System.out.println(minDistance.delMin());
        }

        BreadthFirstPaths paths = new BreadthFirstPaths(grid, indexOf(nodes[0], nodes[1]));
//      BreadthFirstPaths paths = new BreadthFirstPaths(grid,indicesOf(nodes));
//        while(!minDistance.isEmpty()) {
//            int min = minDistance.delMin();

        pane.setPenColor();
        for (int i = 0; i < nodes.length; i +=  2) {
            int gridIndex = indexOf(nodes[i], nodes[i+1]); // processes input array as is
//            System.out.print("path to:("+nodes[i]+" "+ nodes[i+1] + ")\n");
            if (paths.hasPathTo(gridIndex)) {
                for(int step : paths.pathTo(gridIndex)){

                    Display.drawPoint(nodeAt(step)[0],nodeAt(step)[1], pane);
//                    System.out.print("("+nodeAt(step)[0]+ " " + nodeAt(step)[1]+")");
                    pane.pause(200);
                    pane.show();
                }
                System.out.println();
            }
        }
    }

    // TODO: Decouple the following grid methods into its own 'final' library class of static methods.
    // Future docComment:
    // Provides methods to create an operate on a grid graph based up`algs4.Graph`. A dense graph is formed by
    // `generateDenseGrid`, where every edge is explicitly generated resulting in O(n*n) space complexity.
    // TODO `generateSparse
    // returns the graphindices of all the coordinate pairs in nodes
    static Iterable<Integer> indicesOf(int[] nodes){
        Bag<Integer> indices = new Bag<>();
        for(int i = 1 ; i < nodes.length ; i+=2){
            indices.add(indexOf(nodes[i-1], nodes[i]));
        }
        return indices;
    }

    /// Converts a 1-based (x, y) coordinates of node to 0-based indexed vertex in `Graph`.
    static int indexOf(int row, int col){
        if(row == 0 || col == 0) return -1;     // TODO: Remove and add bounds check elsewhere like file input conversion
        return (row - 1) * dim + (col - 1);
    }
    /// Converts a 0-based indexed vertex in `Graph` to 1-based (x, y) coordinates of node.
    static int[] nodeAt(int index){
        return new int[]{
                (index ) / dim  + 1,
                (index)  % dim  + 1
        };
    }
//    private void debugPrint() {
//        for (int j = grid.V() - dim; j >= 0; j -= dim) {
//            for (int i = j; i < j + dim; i++) {
//                System.out.print(nodeAt(i)[0] + " " + nodeAt(i)[1] + "|" + grid.degree(i) + "  ");
//            }
//            System.out.println();
//        }
//    }
    /// Assigns edges to adjacent nodes in a `dim` x `dim` grid, nodes in the grid are only connected horizontally and
    /// vertically to other adjacent nodes. Diagonal connections are NOT created.
    /// More specifically, nodes at grid-corners will receive 2 edges, nodes along grid-borders receive 3, and central nodes receive 4.
    /// The `Graph` vertices are comprised of the array in the range = [0, (dim*dim -1 )]
    /// The `nodeAt` returns the '1-based indexed' grid coordinates (x, y), as an int[2] of its equivalent `Graph` vertex.
    ///     - E.g. Node (1,1) is the zeroth vertex of `Graph`
    public static Graph generateDenseGrid() {
        Graph grid = new Graph(dim*dim);
        for(int v = 0; v < dim * dim; v++){     // dim*dim - dim (skip top row, already attached)
            if(v < dim*dim - (dim)) grid.addEdge(v, v + dim);            // edge to above except on top row
            if((v + 1) % dim != 0)                  // edge to right except at rightmost spot
                grid.addEdge(v, (v+1));
        }
        return grid;
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


}
