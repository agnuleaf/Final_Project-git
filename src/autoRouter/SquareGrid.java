package autoRouter;

import edu.princeton.cs.algs4.*;
import java.util.Arrays;
import java.util.HashSet;

import static autoRouter.Display.*;
import static autoRouter.Int2D.*;

/// Defines the grid
public class SquareGrid {

    static final int squares = 20;                    // number of square tiles along an axis, global used by Display
    static final int border = 1;                      // width unusable border
    private static final int boundsLower = 0;         // exclusive lower bounds
    private static final int boundsUpper = squares;   // exclusvie upper bounds

    public static void main(String[] args) {
        Int2D[] walls = {
                new Int2D(5 ,1 ),
                new Int2D(8 , 9 ),
                new Int2D(4 ,4 ),
                new Int2D(5 ,10 ),
                new Int2D(15,5 ),
                new Int2D(8 ,5)};
        Int2D[] nodes = {
                new Int2D(1 ,1 ),
                new Int2D(3 ,1 ),
                new Int2D(3 ,3 ),
                new Int2D(1 ,3 ),
                new Int2D(5 ,9 ),
                new Int2D(17,5 ),
                new Int2D(8 ,11)
        };

        Draw pane = new Draw();
        Display.init(pane);
        MinimumDetour md = new MinimumDetour();
        md.weightMapInit(nodes, walls);
        Iterable<Stack<Int2D>> routes = md.findRoute(nodes);
        for(Int2D n : nodes){
            drawCircle(n, pane);
        }
        for(Int2D w : walls){
            drawBlock(w, pane);
        }
        pane.pause(200);
        routes.forEach(s-> drawPoints(s,200, pane));

//        boolean isXpreferred = false;
//        Int2D[] nodes = generateNodes(20);
//        sortBy(isXpreferred, nodes);

        int totalCostA= 0; int totalCostB = 0;
        for(int i = 1 ; i < nodes.length ; i++ ){
            int pathCost = 0;
            Stack<Int2D> path = new Stack<>();
            for(Int2D step : md.manhattanPath(nodes[i-1], nodes[i])){
                path.push(step);
                pathCost++;
            }
            totalCostA += pathCost;
            System.out.println(pathCost);
            pathCost = 0;
//            isXpreferred = true;                  // TODO implement a preferred axis of travel selection
//            sortBy(isXpreferred, nodes);
//            Queue<Int2D> pathalt = new Queue<>();
//            for(Int2D step : manhattanPath(nodes[i-1], nodes[i])){
//                pathalt.enqueue(step);
//                pathCost++;
//            }
            totalCostB+=pathCost;
            System.out.println(pathCost);
        }
        System.out.println(totalCostA);
        System.out.println(totalCostB);

//        System.out.print("\nPaths: ");
//        print(paths);
        // @formatter:on
    }

//    // Sorts by x-values or y-values first
//    private static void sortBy(boolean preferX, Int2D[] nodes) {
//        if(preferX){
//            Arrays.sort(nodes, byY);
//            Arrays.sort(nodes, byX);
//        }
//        else {
//            Arrays.sort(nodes, byY);
//            Arrays.sort(nodes, byX);
//        }
//    }
    // Generates a random set of unique nodes within the grid range.
    private static Int2D[] generateNodes(int length){
        HashSet<Int2D> uniqueNodes = new HashSet<>();
        for(int i = 0 ; i < length; i++) {
            Int2D n;
            do{
                n = new Int2D(StdRandom.uniformInt(1, squares), StdRandom.uniformInt(1, squares));
            }while(uniqueNodes.contains(n));
            uniqueNodes.add(n);
        }
        return uniqueNodes.toArray(new Int2D[length]);
    }

    /// Provides cardinal vectors, that can return an adjacent node to a given 'v' in a square grid.
    public enum Adjacent {
        //@formatter:off
        LEFT (new Int2D(-1,  0   )),
        RIGHT(new Int2D( 1,  0   )),
        UP   (new Int2D( 0,  1   )),
        DOWN (new Int2D( 0, -1   ));
        //@formatter:on
        // bounding box corner positions
        private final Int2D direction;
        Adjacent(Int2D direction) {
            this.direction = direction;
        }
        Int2D from(Int2D u) {
            return u.plus(this.direction);
        }
        // check nodes to 'v' are within bounds
        boolean bounds(Int2D v){
            boolean b = false;
            switch (this) {
                case LEFT   -> b = v.x() /*- 1 */> boundsLower;
                case RIGHT  -> b = v.x() /*+ 1 */< boundsUpper;
                case UP     -> b = v.y() /*+ 1 */< boundsUpper;
                case DOWN   -> b = v.y() /*- 1 */> boundsLower;
            }
            return b;
        }
    }
    //
    static <T>  void print(Iterable<T> iterable){
        iterable.forEach(System.out::print);
        System.out.println();
    }
    // basic test for two points
//        Int2D q = new Int2D(5, 3);
//        Int2D p = new Int2D (1, 1);
//        Queue<Int2D> path = new Queue<>();
//        for(Int2D step : manhattanPath(p, q, true)){
//            path.enqueue(step);
//        }

}
