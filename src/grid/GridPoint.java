package grid;

import java.util.Comparator;
import java.util.function.BiFunction;

import static java.lang.Math.abs;

/**
 * Immutable representation of (x,y) integer coordinates in a rectilinear grid, with accompany
 * arithmetic and comparison methods. Two `Point`s are `Comparable` by their distance from origin.
 * Per axis `Comparator`s are given as well.
 * @author Wesley Miller
 */
public record GridPoint(int x, int y) implements Comparable<GridPoint>{

    GridPoint minus(GridPoint q) { return new GridPoint(x - q.x(), y - q.y()); }
    GridPoint plus (GridPoint q) { return new GridPoint(x + q.x(), y + q.y()); }

    int magRect(){ return abs(x) + abs(y); }

    /// Compares by relative distance from origin, negative means closer.
    @Override
    public int compareTo(GridPoint q) {
        return magRect() - q.magRect();
    }

    public String toString(){ return "(" + x + ", " + y + ") "; }
    
    /**
     * Checks if point w is within the window
     * @param w point on the grid
     * @param halfWidth
     * @return true its in the window
     */
    public boolean inWindow(GridPoint w, int halfWidth){
        return (nearX(w, halfWidth) && nearY(w, halfWidth));
    }
    
    /**
     * Checks if point w is within the window of this points axis
     * @param w point on the grid
     * @param halfWidth
     * @return true/false based on if its in the window
     */
    public boolean nearX(GridPoint w, int halfWidth){
        return (    w.x > x - halfWidth
                 && w.x < x + halfWidth);
    }
    
    /**
     * Checks if point w is within the window of this points axis
     * @param w point on the grid
     * @param halfWidth
     * @return true/false based on if its in the window
     */
    public boolean nearY(GridPoint w, int halfWidth){
        return (   w.y > y - halfWidth
                && w.y < y + halfWidth);
    }

    /**
     * Check if this vertex is farther from q than v.
     * @param v point 1
     * @param q point 2
     * @return
     */
    public boolean isFarther(GridPoint v, GridPoint q){
        return (distRectilinear(v, q) < distRectilinear(this, q));
    }
    
    /**
     * Determines the relative diagonal quadrant a given point resides.
     * For a point `p` the two diagonal lines, with slope = 1, intersecting at p are:
     * Lpos(x) = x + (p.y - p.x)  and Lneg(x) = -x + p.y + p.x
     * @param q point on the grid
     * @return - `q`s diagonal quadrant relative to `this`
     */
    public GridPoint vectorTo(GridPoint q){
        if( q.y > q.x + (y - x) && q.y < -q.x + y +  x ){
            return LEFT;
        }
        if( q.x <= q.y + (y - x) && q.x > -q.y + y + x ){
            return UP;
        }
        if( q.x  > q.y + (y - x) && q.x < -q.y + y + x ){
            return DOWN;
        }
        if( q.y <= q.x + (y - x) // below Lpos
                && q.y > -q.x + y + x ) { // above Lpos
            return RIGHT;
        }
        if( q.equals(this) ){
            return ZERO;
        }
        else {
            throw new UnsupportedOperationException("Point: directionOf");
        }
    }
    
    public static Comparator<GridPoint> compareX = Comparator.comparingInt(GridPoint::x);
    public static Comparator<GridPoint> compareY = Comparator.comparingInt(GridPoint::y);

    /**
     * Finds the distance between the two points in the Cartesian plane
     * @param p point 1
     * @param q point 2
     * @return Distance squared between two points in Cartesian plane
     */
    public static double distSqEuclid(GridPoint p, GridPoint q){
        return (q.x - p.x)*(q.x - p.x) - (q.y - p.y)*(q.y - p.y);
    }

    /**
     * Finds the Rectilinear Distance
     * @param p point 1
     * @param q point 2
     * @return Rectilinear Distance between two points
     */
    public static int distRectilinear(GridPoint p, GridPoint q){
        return (int)(abs(q.x - p.x) + abs(q.y - p.y));
    }

   public static BiFunction<GridPoint, GridPoint, Integer> taxiDist =
        (p, q) ->  (Math.abs(q.x() - p.x()) + Math.abs(q.y() - p.y()));

    /**
     * Finds the bounding box between two Points
     * @param p
     * @param q
     * @return bounding box of two `Point`s as an array { lowerLeft, upperRight }
     */
    public static GridPoint[] bounds(GridPoint p , GridPoint q){
        if(q.x - p.x >=0 && q.y - p.y >= 0 ){   // p is already lower left and q is upper right
            return new GridPoint[]{ p, q }; // dont swap
        }
        if(q.x <= p.x || q.y <= p.y) {
            return new GridPoint[] { new GridPoint(p.x, q.y), new GridPoint(q.x, p.y) }; // swap elements
        }
        else return new GridPoint[] { q, p }; // swap points
    }

    /**
     * Test for the gridPoints
     * @param args
     */
    public static void main(String[] args) {
        GridPoint[] quadtest = new GridPoint[] {
                new GridPoint(3, 4), // left
                new GridPoint(2, 1), // down
                new GridPoint(6, 8), // up
                new GridPoint(7, 6),  // right
        };
        GridPoint p = new GridPoint(5,5);
        assert(p.vectorTo(quadtest[0]) == LEFT);
        assert(p.vectorTo(quadtest[1]) == DOWN );
        assert(p.vectorTo(quadtest[2]) == UP );
        assert(p.vectorTo(quadtest[3]) == RIGHT );

        System.out.println();

    }
    
    // Position vectors
    static final GridPoint UP       = new GridPoint( 0, 1);
    static final GridPoint DOWN     = new GridPoint( 0,-1);
    static final GridPoint LEFT     = new GridPoint(-1, 0);
    static final GridPoint RIGHT    = new GridPoint( 1, 0);
    public static final GridPoint ZERO     = new GridPoint( 0, 0);

    /**
     * Finds the midpoint of two points, rounding down to nearest integers
     * @param p point 1
     * @param q point 2
     * @return midpoint
     */
    public static GridPoint midPoint(GridPoint p, GridPoint q){
        return new GridPoint(( p.x + q.x ) / 2 , ( p.y + q.y ) / 2);
    }
    
    /**
     * Finds the centroid of at triangle , rounding down to nearest integers
     * @param p point 1
     * @param q point 2
     * @param r point 3
     * @return centroid
     */
    public static GridPoint centroid(GridPoint p, GridPoint q, GridPoint r){
        return new GridPoint(( p.x + q.x + r.x ) / 3 , ( p.y + q.y + r.y ) / 3);
    }
    
    /**
     * Finds the centroid of a polygon, rounding down to nearest integers
     * @param gridPoints
     * @return centroid
     */
    public static GridPoint centroid(GridPoint... gridPoints){
        int x = 0; int y = 0;
        for(GridPoint p : gridPoints){
            x += p.x ; y += p.y;
        }
        return new GridPoint(x / gridPoints.length, y / gridPoints.length);
    }
}