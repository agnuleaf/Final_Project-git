package autoRouter;

import edu.princeton.cs.algs4.*;
import java.util.Comparator;
import static autoRouter.Int2D.zero;
import static autoRouter.SquareGrid.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// Finds a path connecting all the nodes given by an array. For each pair of nodes, adjacent by index in the array,
/// the shortest path is determined by `manhattanPath`.
/// When an obstacle is encountered, paths starting from the q-Positive stack are searched before switching to q-Negative.
/// Using q-Negative routes increments a detour counter to account for the extra path around an obstacle.
/// Note: The total distance to any target 'q' from 'p' = 2d + manhattanPath(p, q)
/// ```
///  define P as the starting node, Q as the target.
///         u  as the current vertex. v as a neighbor vertex
///         S-, S+ as the 'Q-negative' and 'Q-positive' stacks respectively.
/// 1. u := P. d := 0
/// 2. visit u:
///     * mark u
///     * if(u = Q) done
///     * Push to S- : all unmarked Q- neighbors of u
///         + if no unmarked `Q+` neighbors exist, goto(3)
///         + u := v , unmarked `Q+` neighbor and push to `S+` the other `Q+` neighbor
/// 3. if (S+ isEmpty) goto (4).
///     else
///         Pop v from S+, if(v not marked) u := v
/// 4. if (S- is not Empty) Swap S+, S- . goto(3)
///     else { No PATH EXISTS }
/// ```
/// Algorithm from the paper 'Shortest Path Algorithms for Grid Graphs' by Hadlock, 1977
public class MinimumDetour {

    private SET<Int2D> marked;
    private static final int weightDefault = 2;
    private static final int weightMin = 1;  // allows preference to a target node or one previously traversed
    private ST<Int2D, Integer> weightMap;    // stores weights for nodes that are targets, walls or already traveled.

    // TODO how to find routes with a different set of paths.  Once found add to a graph to find the minimum spanning tree route
    /// Returns the manhattan paths given an array of nodes, in the order of the array. Assigns minimum weight to a previous path
    public Queue<Stack<Int2D>> findRoute(Int2D[] targets) {
        Queue<Stack<Int2D>> route = new Queue<>();
        for (int i = 1; i < targets.length; i++) {
            Stack<Int2D> stepsTaken = manhattanPath(targets[i - 1], targets[i]);
            route.enqueue(stepsTaken);
        }
        return route;
    }

    /// Initialize weights arrays of targets and walls.
    public void weightMapInit(Int2D[] targets, Int2D[] walls) {
        weightMap = new ST<>();
        for(Int2D w : walls){
            weightMap.put(w, Integer.MAX_VALUE);
        }
        for(Int2D n : targets){
            weightMap.put(n, weightMin);
        }
    }

    // ST is sparse relative to grid, entries are 1 by default
    private int getWeight(Int2D v){
//        if(!SquareGrid.checkBounds(v))
//
        if(weightMap.contains(v))
            return weightMap.get(v);
        return weightDefault;
    }

    public static void main(String[] args) {
        MinimumDetour md = new MinimumDetour();
        Int2D[] walls = {
                new Int2D(5 ,1 ),
                new Int2D(8 , 9 ),
                new Int2D(4 ,4 ),
                new Int2D(5 ,10 ),
                new Int2D(15,5 ),
                new Int2D(8 ,5)
        };
        Int2D[] nodes = {
                new Int2D(1 ,1 ),
                new Int2D(3 ,1 ),
                new Int2D(3 ,3 ),
                new Int2D(1 ,3 ),
                new Int2D(5 ,9 ),
                new Int2D(17,5 ),
                new Int2D(8 ,11)
        };
//        System.out.println(isQnegative(new Int2D(2,1),nodes[0],nodes[1]));
//        System.out.println(isQnegative(new Int2D(4,1),nodes[1], nodes[2]));
        md.weightMapInit(nodes, walls);
        printRoute(md.findRoute(nodes));
        }
        static void printRoute(Iterable<Stack<Int2D>> paths){
            for(Stack<Int2D> p : paths){
                print(p);
            }
        }

    Stack<Int2D> neighbors(Int2D u){
        Stack<Int2D> neighbors = new Stack<>();
        for(Adjacent adj : Adjacent.values()){
            Int2D v = adj.from(u);
            if(adj.bounds(v) && !marked.contains(v) && isTraversible(v)){
                neighbors.push(v);
            }
        }
        return neighbors;
    }
    private boolean isTraversible(Int2D v){
        return getWeight(v) < Integer.MAX_VALUE;
    }
    // only considers the weight of v
    private int getWeightedDistance(Int2D v, Int2D q){
        return Int2D.manhattan(v, q) - 1 + getWeight(v) ;  // -1 since we consider u's weight
    }

    /// Finds the minimum set of nodes needed to reach a target node 'q', from starting node 'p' in a grid graph.
    /// Two stacks of nodes are used for detouring obstacles TODO use a MinPQ for all neighbors instead of two stacks?
    /// : q-Negative (nodes farther from 'q') and q-Positive (nodes closer to 'q'
    /// Only nonzero positive integers are valid positions. Only vertical and horizontal movement, one node per move is allowed.
    public Stack<Int2D> manhattanPath(Int2D p, Int2D q) {
        //    private Stack<Int2D> pStack;
        Stack<Int2D> nStack = new Stack<>(); // nodes farther from Q than u, required to detour around obstacles
        // pStack = new Stack<>(); // nodes closer to Q than u that aren't visited next
        Stack<Int2D>  pathStack = new Stack<>();
        marked = new SET<>();
        marked.add(zero); // zero off limits
        Comparator<Int2D> byWeightDistance =(v, w) ->
                (getWeightedDistance(v,q) - getWeightedDistance(w,q)); // for sorting by distance + the node's weight
        Int2D u = p;                            // 1) set current position to source 'p'
//        int d = 0;
        while(!u.equals(q)) {
            marked.add(u);                      // 2) visit u
            pathStack.push(u);
            Stack<Int2D> neighbors = neighbors(u);
            MaxPQ<Int2D> negPQ = new MaxPQ<>(byWeightDistance);
            MinPQ<Int2D> posPQ = new MinPQ<>(byWeightDistance);

            int numNeighbors = neighbors.size();
            for(int i = 0; i < numNeighbors; i++){
                if (isQnegative(neighbors.peek(), u, q)) {
                    negPQ.insert(neighbors.pop());
                }else {
                    posPQ.insert(neighbors.pop());
                }
            }
            while(!negPQ.isEmpty()){
                nStack.push(negPQ.delMax());         // push the max first to keep the min neighbors at the top
            }
            if(!posPQ.isEmpty()){
                u = posPQ.delMin();
            }
//            Int2D dirToQ = u.to(q);
//            Int2D vX = u.xPlus(dirToQ.x()) ;    // q+ neighbor along x-axis
//            Int2D vY = u.yPlus(dirToQ.y());     // q+ neighbor along y-axis
//
//            int xWeight = getWeight(vX);
//
//            if ( !marked.contains(v) && isQpositive(v, u, q) ) {
//                if ( !marked.contains(w) && isQpositive(w, u, q))
//                    pStack.push(w);
//                u = v;
//                marked.add(u);
//                pathStack.push(u);
//            }
                // check untraveled branches towards target
//            if ( !posPQ.isEmpty()) {
//                do {
//                    u = posPQ.delMin();
//                } while (marked.contains(u));
            else {
                if (nStack.isEmpty()) { // no path exists
                    System.err.println("NO PATH FOUND");
                    System.out.print("u:"+u+" q:"+q+" dist:"+Int2D.manhattan(u,q));
                    System.out.print(" pathStack:");print(pathStack);
                    System.out.print("marked:"); print(marked);
                    assertTrue(false);  // FIXME
                } // FAILED
                while(!nStack.isEmpty()) {
                    posPQ.insert(nStack.pop());     // transfer neighbors from q- when there are no more q+
                }
//                    d++;  // TODO: remove?
            }
        }
//        if(u.equals(q)){
        return pathStack;
//        }else{
//            return null;
    }


//    // Test if 'v' is q-positive, (closer to q than u)
//    boolean isQpositive(Int2D v, Int2D u, Int2D q){
//       if(weightMap.contains(v) && weightMap.get(v).equals(Integer.MAX_VALUE))
//           return false; // check if 'v' is a wall;
//        return (Int2D.manhattan(u, q) >= Int2D.manhattan(v, q) + 1);
//    }

    static boolean isQnegative(Int2D v, Int2D u, Int2D q){
//        if(weightMap.contains(v) && weightMap.get(v).equals(Integer.MAX_VALUE))
//             return false;
        return (Int2D.manhattan(u, q) == Int2D.manhattan(v, q) - 1);
    }
}
