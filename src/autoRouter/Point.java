package autoRouter;

import edu.princeton.cs.algs4.Draw;

import java.util.Comparator;

import static java.lang.Math.abs;

public record Point(int x, int y) implements Comparable<Point>{

    static final Point UP       = new Point( 0, 1);
    static final Point DOWN     = new Point( 0,-1);
    static final Point LEFT     = new Point(-1, 0);
    static final Point RIGHT    = new Point( 1, 0);

    Point minus(Point q) { return new Point(x - q.x(), y - q.y()); }
    Point plus (Point q) { return new Point(x + q.x(), y + q.y()); }

    int magRect(Point p){ return abs(p.x + p.y); }
    public static Comparator<Point> compareX = Comparator.comparingInt(Point::x);
    public static Comparator<Point> compareY = Comparator.comparingInt(Point::y);

    /// Distance squared between two points in Cartesian plane.
    public static double distSqEuclid(Point p, Point q){
        return (q.x - p.x)*(q.x - p.x) - (q.y - p.y)*(q.y - p.y);
    }
    /// Returns the bounding box of two `Point`s as an array { lowerLeft, upperRight }
    public static Point[] bounds(Point p , Point q){
        if(q.x - p.x >=0 && q.y - p.y >= 0 ){   // p is already lower left and q is upper right
            return new Point[]{p,q}; // dont swap
        }
        if(q.x <= p.x || q.y <= p.y) {
            return new Point[] { new Point(p.x, q.y), new Point(q.x, p.y) }; // swap elements
        }
        else return new Point[] { q, p }; // swap points
    }

    private static void draw(Point p, Draw pane){
        Display.drawCircle(p.x, p.y, pane);
    }
    private static void draw(Point[] points, Draw pane){
        for(Point p :points){
            Point.draw(p, pane);
        }
    }

    public static void main(String[] args) {
        Draw pane = new Draw();
        Display.init(pane);

        Point ll = new Point (1,1);
        Point ur = new Point (5, 5);
        Point[] llur = bounds(ll, ur);
        Point ul = new Point ( 2, 6);
        Point lr = new Point ( 6, 2);
        Point[] ullr = bounds(ul, lr);

        Point[] points = new Point[] { ll, ur, ul, lr};
        draw(points, pane); pane.pause(200); pane.show();
        draw(llur, pane); pane.pause(200); pane.show();
        draw(ullr, pane); pane.pause(200); pane.show();

    }

    /// For comparing two points' distances from origin
    @Override
    public int compareTo(Point q) {

        return (this.x - q.x) + (this.y - q.y);
    }
}
