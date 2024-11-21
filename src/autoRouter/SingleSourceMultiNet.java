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
/// Goal
/// * given a source p and multiple q,
///     - sort q by their distance from p
/// * find shortest paths to q0
/// * backtrack the path scanning for the shortest path to next nearest q
/// ```
/// * Find SPs p to q0(closest target)  .
/// To arrive at q, from p two separate intermediate points r = (0, a.y + p.y), s = (a.x + p.x, 0) exist
///                                 option a                    option b
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
///
///  * backtrack the option whose path is closest to next q     Find point to line distance
///
///   -  -  -  -  -  -  -  -        -  -  -  -  -  -  -  -
///   -  p  -  -  -  -  -  -        -  p -------------.  -
///   -  |  -  -  -  -  -  -        -  -  -  -  -  -  |  -
///   -  | _ _ x _ _ _ _q0 -        -  -  -  -  -  -  q0 -
///   -  -  -  |  -  -  -  -        -  -  -  -  -  -  -  -
///   -  -  -  |  -  -  -  -        -  -  -  -  -  -  -  -
///   -  -  -  |  -  -  -  -        -  -  -  -  -  -  -  -
///   -  -  -  |  -  -  -  -        -  -  -  -  -  -  -  -
///   -  -  -  q1 -  -  -  -        -  -  -  q1 -  -  -  -
///   -  -  -  -  -  -  -  -        -  -  -  -  -  -  -  -
///
/// ```
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

        Graph grid = generateDenseGraph(); // dense dim x dim graph with unweighted edges connecting adjacent squares
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
                6, 6,           //   7      // TODO fix to prefer shortest path
                3, 1,           //   9
        };                      //
        int[] dist = distanceArray(nodes);      // sort nodes[] by distance from source

        //  System.out.print("dist:" ); println(dist);
        IndexMinPQ<Integer> minDistance = new IndexMinPQ<>(dist.length);
        for (int i = 0; i < dist.length; i++) {
            minDistance.insert(i, dist[i]);
        }

//        for(int j = grid.V() -dim ; j >= 0; j-=dim){
//            for(int  i = j ; i < j + dim  ; i++) {
//                System.out.print(nodeAt(i)[0] + " " + nodeAt(i)[1] + "|" + grid.degree(i) + "  ");
//            }System.out.println();
//        }

        Draw pane = new Draw();
        Display.init(pane);
        Display.drawCircles(nodes, pane);
        pane.show();
        for (int i = 0; i < minDistance.size(); i+=2){
            System.out.print(minDistance.keyOf(i/2) + " d:");
            int dx = nodes[i+2] - nodes[0];
            int dy = nodes[i+3] - nodes[1];
            System.out.println(dx + " " + dy);
            Display.drawCircle(dx + nodes[0],nodes[1],Color.BLUE,pane);
            Display.drawCircle(nodes[0],dy + nodes[1] ,Color.BLUE,pane);
            pane.pause(200);
            pane.show();
            //System.out.println(minDistance.delMin());
        }
        BreadthFirstPaths pathfind = new BreadthFirstPaths(grid, indexOf(nodes[0], nodes[1]));
    //    BreadthFirstPaths pathfind = new BreadthFirstPaths(grid,indicesOf(nodes));

//        while(!minDistance.isEmpty()) {
//            int min = minDistance.delMin();

//        for (int i = 2; i < nodes.length; i +=  2) {
        pane.setPenColor();
        for (int i = 0; i < nodes.length; i +=  2) {
            int gridIndex = indexOf(nodes[i], nodes[i+1]); // processes input array as is
//            System.out.print("path to:("+nodes[i]+" "+ nodes[i+1] + ")\n");
            if (pathfind.hasPathTo(gridIndex)) {
                for(int step : pathfind.pathTo(gridIndex)){

                    Display.drawPoint(nodeAt(step)[0],nodeAt(step)[1], pane);
//                    System.out.print("("+nodeAt(step)[0]+ " " + nodeAt(step)[1]+")");
                    pane.pause(200);
                    pane.show();
                }
                System.out.println();
            }
        }
    }
    // returns the graphindices of all the coordinate pairs in nodes
    static Iterable<Integer> indicesOf(int[] nodes){
        Bag<Integer> indices = new Bag<>();
        for(int i = 1 ; i < nodes.length ; i+=2){
            indices.add(indexOf(nodes[i-1], nodes[i]));
        }
        return indices;
    }

    // converts a node to graph index, x or y cannot equal 0.
    private static int indexOf(int row, int col){
        if(row == 0 || col == 0) return -1;
        return (row - 1) * dim + (col - 1);
    }
    // converts graph index to a node
    private static int[] nodeAt(int index){
        return new int[]{ (index ) / dim  + 1, (index) % dim   + 1};
    }
    // stupid check
    private static int[] gridSquare(int index){
        return new int[]{index % dim, index / dim};
    }
    // Adds edges to adjacent squares in a dim x dim grid. Index range = [0, (dim*dim -1 )]
    private static Graph generateDenseGraph() {
        Graph grid = new Graph(dim*dim);
        for(int v = 0; v < dim * dim; v++){     // dim*dim - dim (skip top row, already attached)
            if(v < dim*dim - (dim)) grid.addEdge(v, v + dim);            // edge to above except on top row
            if((v + 1) % dim != 0)                  // edge to right except at rightmost spot
                grid.addEdge(v, (v+1));
        }
        return grid;
    }

    static void println(int[] array){
        for(int i : array){
            System.out.print(i + " ");
        }
    }
    static void printPairs(int[] array){
        for(int i = 1; i < array.length; i++){
            System.out.print(i - 1 + " " + i);
        }
    }

}
