package autoRouter;

import java.util.Comparator;

import static java.lang.Math.abs;

public record Point(int x, int y) {
    static final Point UP       = new Point( 0, 1);
    static final Point DOWN     = new Point( 0,-1);
    static final Point LEFT     = new Point(-1, 0);
    static final Point RIGHT    = new Point( 1, 0);

    Point minus(Point q) { return new Point(x - q.x(), y - q.y()); }
    Point plus (Point q) { return new Point(x + q.x(), y + q.y()); }

    int magnitude(Point p){ return abs(p.x + p.y); }
    static Comparator<Point> compareX = Comparator.comparingInt(Point::x);
    static Comparator<Point> compareY = Comparator.comparingInt(Point::y);

    /// Returns the bounding box of two `Point`s as an array { lowerLeft, upperRight }
    public Point[] bounds(Point p , Point q){
        if(q.x - p.x >=0 && q.y - p.y >= 0 ){   // p is already lower left and q is upper right
            return new Point[]{p,q};
        }
        return null; //TODO

    }
}
