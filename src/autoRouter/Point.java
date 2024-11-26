package autoRouter;

import edu.princeton.cs.algs4.Draw;
import java.util.Comparator;
import static java.lang.Math.abs;
/// Immutable representation of (x,y) integer coordinates in a rectilinear grid, with accompany
/// arithmetic and comparison methods. Two `Point`s are `Comparable` by their distance from origin.
/// Per axis `Comparator`s are given as well.
/// @author Wesley Miller
public record Point(int x, int y) implements Comparable<Point>{

    Point minus(Point q) { return new Point(x - q.x(), y - q.y()); }
    Point plus (Point q) { return new Point(x + q.x(), y + q.y()); }

    int magRect(){ return abs(x) + abs(y); }

    /// Compares by relative distance from origin, negative means closer.
    @Override
    public int compareTo(Point q) {
        return magRect() - q.magRect();
    }

    public String toString(){ return "(" + x + ", " + y + ") "; }
    /// Checks if point w is within the window
    public boolean inWindow(Point w, int halfWidth){
        return (nearX(w, halfWidth) && nearY(w, halfWidth));
    }
    /// Checks if point w is within the window of this points axis
    public boolean nearX(Point w, int halfWidth){
        return (    w.x > x - halfWidth
                 && w.x < x + halfWidth);
    }
    public boolean nearY(Point w, int halfWidth){
        return (   w.y > y - halfWidth
                && w.y < y + halfWidth);
    }

    /// Check if this vertex is farther from q than v.
    public boolean isFarther(Point v, Point q){
        return (distRectilinear(v, q) < distRectilinear(this, q));
    }
    /// Determines the relative diagonal quadrant a given point resides.
    /// For a point `p` the two diagonal lines, with slope = 1, intersecting at p are:
    /// <p> Lpos(x) = x + (p.y - p.x)  and Lneg(x) = -x + p.y + p.x </p>
    /// @return - `q`s diagonal quadrant relative to `this`
    public Point vectorTo(Point q){
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
    public static Comparator<Point> compareX = Comparator.comparingInt(Point::x);
    public static Comparator<Point> compareY = Comparator.comparingInt(Point::y);



    /// Distance squared between two points in Cartesian plane.
    public static double distSqEuclid(Point p, Point q){
        return (q.x - p.x)*(q.x - p.x) - (q.y - p.y)*(q.y - p.y);
    }

    /// Rectilinear Distance between two points.
    public static double distRectilinear(Point p, Point q){
        return abs(q.x - p.x) + abs(q.y - p.y);
    }

    /// Returns the bounding box of two `Point`s as an array { lowerLeft, upperRight }
    public static Point[] bounds(Point p , Point q){
        if(q.x - p.x >=0 && q.y - p.y >= 0 ){   // p is already lower left and q is upper right
            return new Point[]{ p, q }; // dont swap
        }
        if(q.x <= p.x || q.y <= p.y) {
            return new Point[] { new Point(p.x, q.y), new Point(q.x, p.y) }; // swap elements
        }
        else return new Point[] { q, p }; // swap points
    }

    public static void main(String[] args) {

        // bounding box checks
//        Point ll = new Point (1,1);
//        Point ur = new Point (5, 5);
//        Point[] llur = bounds(ll, ur);
//        Point ul = new Point ( 2, 6);
//        Point lr = new Point ( 6, 2);
//        Point[] ullr = bounds(ul, lr);
//        Point q1 = new Point(2,1);
//        System.out.println(ll.compareTo(q1));
        Point[] quadtest = new Point[] {
                new Point(3, 4), // left
                new Point(2, 1), // down
                new Point(6, 8), // up
                new Point(7, 6),  // right
        };
        Point p = new Point(5,5);
        assert(p.vectorTo(quadtest[0]) == LEFT);
        assert(p.vectorTo(quadtest[1]) == DOWN );
        assert(p.vectorTo(quadtest[2]) == UP );
        assert(p.vectorTo(quadtest[3]) == RIGHT );

        System.out.println();
//        Point[] points = new Point[] { ll, ur, ul, lr};
//        Display display = new Display();
//        Draw pane = display.init();
//        draw(points, pane); pane.pause(200); pane.show();
//        draw(llur, pane); pane.pause(200); pane.show();
//        draw(ullr, pane); pane.pause(200); pane.show();

    }
    // Position vectors
    static final Point UP       = new Point( 0, 1);
    static final Point DOWN     = new Point( 0,-1);
    static final Point LEFT     = new Point(-1, 0);
    static final Point RIGHT    = new Point( 1, 0);
    public static final Point ZERO     = new Point( 0, 0);

    /// Finds the midpoint of two points, rounding down to nearest integers.
    public static Point midPoint(Point p, Point q){
        return new Point(( p.x + q.x ) / 2 , ( p.y + q.y ) / 2);
    }
    /// Finds the centroid of at triangle , rounding down to nearest integers.
    public static Point centroid(Point p, Point q, Point r){
        return new Point(( p.x + q.x + r.x ) / 3 , ( p.y + q.y + r.y ) / 3);
    }
    /// Finds the centroid of a polygon, rounding down to nearest integers.
    public static Point centroid( Point ... points){
        int x = 0; int y = 0;
        for(Point p : points){
            x += p.x ; y += p.y;
        }
        return new Point(x / points.length, y / points.length);
    }

    private static void draw(Point p, Draw pane){
        Display.drawCircle(p.x, p.y, pane);
    }
    private static void draw(Point[] points, Draw pane){
        for(Point p :points){
            Point.draw(p, pane);
        }
    }
}
