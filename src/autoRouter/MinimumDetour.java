package autoRouter;

import edu.princeton.cs.algs4.*;
import static autoRouter.SquareGrid.Adjacent;
import static autoRouter.SquareGrid.print;
import static autoRouter.Int2D.zero;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// Finds a path connecting all the nodes given by an array. For each pair of nodes, adjacent by index in the array,
/// the shortest path is determined by `manhattanPath`.
/// When an obstacle is encountered, paths starting from the q-Positive stack are searched before switching to q-Negative.
/// Using q-Negative routes increments a detour counter to account for the extra path around an obstacle.
/// ```
/// 1. u <- P. d <- 0
/// 2. visit u:
///     * mark u
///     * if(u = Q) done
///     * `S-` .push : all unmakred `Q-` neighbors of 'u'
///         + if no unmarked `Q+` neighbors exist, goto(3)
///         + u <- v , unmarked `Q+` neighbor and push to `S+` the other `Q+` neighbor
/// 3. if (`S+` isEmpty) goto (4).
///     else {
///         Pop 'v' from `S+` ;
///         if(v marked, discard v and Pop another)
///         } else {u <- v ; goto(2)}
/// 4. if (`S-` is not Empty) Swap `S+` and `S-` . d++ . goto(3)
///     else { No PATH EXISTS }
/// ```
/// Based on the paper 'Shortest Path Algorithms for Grid Graphs' by Hadlock, 1977
public class MinimumDetour {

    private Stack<Int2D> pStack;
    private Stack<Int2D> nStack;
    private SET<Int2D> marked;

    /// Returns the manhattan paths given an array of nodes, in the order of the array.
    /// Does NOT find the minimum spanning tree. TODO find the minimum spanning tree separately
    public LinkedBag<Stack<Int2D>> getRoute(Int2D[] nodes) {
        LinkedBag<Stack<Int2D>> route = new LinkedBag<>();
        for (int i = 1; i < nodes.length; i++) {
            Stack<Int2D> stepsTaken = manhattanPath(nodes[i - 1], nodes[i]);
            route.add(stepsTaken);
        }
        return route;
    }

    public static void main(String[] args) {
        MinimumDetour md = new MinimumDetour();
        LinkedBag<Stack<Int2D>> route = new LinkedBag<>();
        Int2D[] nodes = {
                new Int2D(1 ,1 ),
                new Int2D(3 ,1 ),
                new Int2D(3 ,3 ),
                new Int2D(1 ,3 ),
                new Int2D(5 ,9 ),
                new Int2D(17,5 ),
                new Int2D(8 ,11)
        };
        printRoute(md.getRoute(nodes));
//        int totalCostA= 0;
//        for(int i = 1 ; i < nodes.length ; i++ ) {
//            Stack<Int2D> stepsTaken = md.manhattanPath(nodes[i - 1], nodes[i]);
//
//            route.add(stepsTaken);
//            totalCostA += stepsTaken.size();
//            System.out.print(nodes[i-1] + "->" + nodes[i] + ":" + stepsTaken.size() + "\t" ); print(stepsTaken);
//        }
//        System.out.println("total:" + totalCostA);

//        Int2D a = new Int2D(3, 3); Int2D b = new Int2D(1,1);
//        MinimumDetour md = new MinimumDetour();
//        Stack<Int2D> path = new Stack<>();
//        for(Int2D step : md.manhattanPath(a, b)){
//            path.push(step);
//        }
//        print(path);
    }
        static void printRoute(Iterable<Stack<Int2D>> paths){
            for(Stack<Int2D> p : paths){
                print(p);
            }
        }
    // Moves the current node. u - current position; q - target;  posStack - qPositive stack; pathStack - path taken so far;
//    private Int2D nextMove(Int2D u, Int2D q,
//                                  Stack<Int2D> pathStack, int d) {
//        Int2D dirToQ = u.to(q);
//        Int2D vX = u.xPlus(dirToQ.x()) ;
//        if(!isQpositive(vX, u, q) && marked.contains(vX)) {
//            vX = zero();            //TODO maybe try to remove all zero()
//        }
//        Int2D vY = u.yPlus(dirToQ.y());
//
//        if(!isQpositive(vY, u, q) && marked.contains(vY)){
//            vY = zero();
//        }
//
//        Int2D v; Int2D w;
//        if(!vX.equals(zero()) ) {
//            v = vX; w = vY;
//        }
//        else {
//            v = vY; w = vX; }
//
//        if ( !marked.contains(v) && isQpositive(v, u, q) ) {
//            if ( !marked.contains(w) && isQpositive(w, u, q))
//                pStack.push(w);
//            u = v;
//            marked.add(u);
//            pathStack.push(u);
//        }
//        else {
//            if ( !pStack.isEmpty()) { // check untraveled branches towards target
//                do{
//                    u = pStack.pop();
//                } while(marked.contains(u));
//            }
//            else {
//                if (nStack.isEmpty()) { // no path exists
//                    System.err.println("NO PATH FOUND");
//                    System.out.print("u:"+u+" q:"+q+" dist:"+Int2D.manhattan(u,q));
//                    System.out.print("pathStack:");print(pathStack);
//                    System.out.print("marked:"); print(marked);
//                    assertTrue(false);  // FIXME
//                } // FAILED
//                pStack = nStack;
//                nStack = new Stack<>();
//                d++;
//            }
//        }
//        return u;
//    }

    /// Finds the minimum set of nodes needed to reach a target node 'q', from starting node 'p' in a grid graph.
    /// Two stacks of nodes are used for detouring obstacles TODO add symbol table mapping Int2D coordinates to node weight(infinite weight for a wall)
    /// : q-Negative (nodes farther from 'q') and q-Positive (nodes closer to 'q'
    /// Only nonzero positive integers are valid positions. Only vertical and horizontal movement, one node per move is allowed.
    public Stack<Int2D> manhattanPath(Int2D p, Int2D q) {
        nStack     = new Stack<>();        // nodes farther from Q than u, required to detour obstacles
        pStack     = new Stack<>();        // nodes closer to Q than u
        Stack<Int2D>  pathStack  = new Stack<>();
        marked = new SET<>();
        marked.add(zero());
        Int2D u = p;                        // set current position
        int d = 0;

        while(!u.equals(q)) {
            marked.add(u);
            pushQnegativeNeighbors(u, q);

            Int2D lastU = u;
//            u = nextMove(u, q, pathStack, d);
            Int2D dirToQ = u.to(q);
            Int2D vX = u.xPlus(dirToQ.x()) ;
            if(!isQpositive(vX, u, q) && marked.contains(vX)) {
                vX = zero();
            }
            Int2D vY = u.yPlus(dirToQ.y());

            if(!isQpositive(vY, u, q) && marked.contains(vY)){
                vY = zero();
            }
            Int2D v; Int2D w;
            if(!vX.equals(zero()) ) {
                v = vX; w = vY;
            }
            else { v = vY; w = vX; }

            if ( !marked.contains(v) && isQpositive(v, u, q) ) {
                if ( !marked.contains(w) && isQpositive(w, u, q))
                    pStack.push(w);
                u = v;
                marked.add(u);
                pathStack.push(u);
            }
            else {
                if ( !pStack.isEmpty()) { // check untraveled branches towards target
                    u = pStack.pop();
                    while(marked.contains(u)) {
                        u = pStack.pop();
                    }
                }
                else {
                    if (nStack.isEmpty()) { // no path exists
                        System.err.println("NO PATH FOUND");
                        System.out.print("u:"+u+" q:"+q+" dist:"+Int2D.manhattan(u,q));
                        System.out.print("pathStack:");print(pathStack);
                        System.out.print("marked:"); print(marked);
                        assertTrue(false);  // FIXME
                    } // FAILED
                    pStack = nStack;
                    nStack = new Stack<>();
                    d++;
                }
            }
        }
        if(u.equals(q)){
            return pathStack;
        }else{
            return null;
        }
    }

    /// Determines if a position,`v` adjacent to current position, `u`, is 'q-negative', i.e. farther from `q` than `u` is.
    /// Pushes those positions to the `nStack`.
    ///
    /// @implNote Consider a return of (0, 1). This results if either no positions adjacent u.x() were pushed or **both** were pushed.
    void pushQnegativeNeighbors(Int2D u, Int2D q) {

        for(var adj : Adjacent.values()) {
            Int2D v = adj.from(u);
            if (!marked.contains(v) && isQnegative(v, u, q)
                    && adj.bounds(v)) {                             // check neighbor is in bounds
                nStack.push(v);
            }
        }
    }
    // Test if 'v' is q-positive
    static boolean isQpositive(Int2D v, Int2D u, Int2D q){
        return (Int2D.manhattan(u, q) >= Int2D.manhattan(v, q) + 1);
    }
    static boolean isQnegative(Int2D v, Int2D u, Int2D q){
        return (Int2D.manhattan(u, q) <= Int2D.manhattan(v, q) - 1);
    }
}
