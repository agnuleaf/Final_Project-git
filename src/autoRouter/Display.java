package autoRouter;
import edu.princeton.cs.algs4.Draw;

import java.awt.*;

// TODO add listeners for mouse events, Get position of mouse on gridmap during click -> add location to node list.
// TODO drop-down menu for some predefined examples
// TODO button to generate random nodes
// TODO Associate circles with nametags.  Either write a nametag placement method or uniquely color circle with a legend
/// Wraps `algs4.Draw` shape painting methods, to simplify painting data defined from `SquareGrid`.
public class Display {

    Draw pane;

    /// Displays information provided by the Plane object (node positions as circles, edges as lines)

    static int width = 300;
    static int height = 300;
    static double gridCount = 10;
    static double unit = 1 / gridCount;   // dimensions of unit square , gives a 20 x 20 grid
    static double pointScalar = 0.5;          // point size

    public static void grid(Draw pane) {

        grid(width, height, unit, pane);
    }

    private static void grid(int width, int height, double unit, Draw pane) {
        pane.setPenColor(Draw.LIGHT_GRAY);
        for (int x = 0; x < width; x++) {
            pane.setPenRadius((x % 5 == 0) ? 0.002 : 0.0005);
            double xTick = x * unit;
            pane.line(xTick, 0.0, xTick, 1.0);
        }
        for (int y = 0; y < height; y++) {
            pane.setPenRadius((y % 5 == 0) ? 0.002 : 0.0005);
            double yTick = y * unit;
            pane.line(0.0, yTick, 1.0, yTick);
        }
        pane.setPenColor(Draw.BLACK);
    }

    /// Draw a grid, 20x20
    public static void drawCircle(int x, int y, Color color, Draw pane) {
        pane.setPenColor(color);
        pane.setPenRadius(0.01);
        pane.circle(
                x * unit,
                y * unit,
                pointScalar * unit * 0.5);
        pane.setPenRadius();
    }

    public static void drawCircle(int x, int y, Draw pane) {
        pane.setPenColor(Draw.PRINCETON_ORANGE);
        pane.setPenRadius(0.01);
        pane.circle(
                x * unit,
                y * unit,
                pointScalar * unit * 0.5);
        pane.setPenRadius();
    }
        public static void drawCircle(Point p, Draw pane) {
            drawCircle(p.x(),p.y(),pane);
        }


    public static void drawPath(Point p, double offset, Color color, Draw pane) {

        pane.setPenColor(color);
        pane.setPenRadius(0.01);
        pane.circle(
                p.x() * unit + offset,
                p.y() * unit + offset,
                pointScalar * unit * 0.5);
        pane.setPenRadius();
        pane.setPenColor();
    }
    public static void drawSteinerPoint(Point p, Draw pane) {
        pane.setPenColor(Draw.CYAN);
        pane.setPenRadius(0.005);
        pane.circle(
                p.x() * unit,
                p.y() * unit,
                pointScalar * unit * 0.3);
        pane.setPenRadius();
        pane.setPenColor();
    }

    public static void drawPoint(int x, int y, Draw pane) {
        double diameter = 0.1;
        pane.filledCircle(
                x * unit,
                y * unit,
                diameter * unit);
    }
    public static void drawPoint(Point p, Color color, Draw pane) {
        double diameter = 0.1;
        pane.setPenColor(color);
        pane.filledCircle(
                p.x() * unit,
                p.y() * unit,
                diameter * unit);
        pane.setPenColor();
    }
    public static void drawPoint(int x, int y, Color color, Draw pane) {
        double diameter = 0.1;
        pane.setPenColor(color);
        pane.filledCircle(
                x * unit,
                y * unit,
                diameter * unit);
        pane.setPenColor();
    }
    public static void drawSquare(Point p, int halfWidth, Color color, Draw pane){
        pane.setPenColor(color);
        pane.square(
                p.x() * unit,
                p.y() * unit,
                halfWidth * unit
        );
        pane.setPenColor();
    }
    /// TODO explored regions and search square display. Must be in the background : translucent or painted before all other animations somehow.
    public static void drawCoverage(Point p, int halfWidth, Color color, Draw pane){

    }
    ///  draws points from int[] assumed to be in the form x0, y0 ,x1, y1, ...xi ,yi
    public static void drawPoints(int[] points, Draw pane) {
        pane.setPenColor(Draw.MAGENTA);
        for (int i = 1; i < points.length; i += 2) {
            drawPoint(points[i - 1], points[i], pane);
        }
        pane.setPenColor();
    }

    public static void drawCircles(int[] circles, Draw pane) {
        pane.setPenColor(pane.getPenColor().darker());
        for (int i = 1; i < circles.length; i += 2) {
            drawCircle(circles[i - 1], circles[i], pane);
        }
        pane.setPenColor();
    }
    public static void drawCircles(Point[] circles, Draw pane) {
        pane.setPenColor(pane.getPenColor().darker());
        for (Point p : circles){
            drawCircle(p, pane);
        }
        pane.setPenColor();
    }
    /// static builder for a draw pane with default gridcount = 10
    public static Draw init() {
        Draw pane = new Draw();
        gridCount = 10;
        unit = 1 / gridCount;
        pane.enableDoubleBuffering();        // defer rendering until show() is called
        pane.setCanvasSize(width, height);
        pane.clear(Draw.GRAY);        // set background
        grid(pane);
        pane.pause(20);
        pane.show();
        return pane;
    }

    /// static builder for draw pane with gridcount input
    public static Draw init(int axisSize){

        gridCount = axisSize;
        unit = 1 / gridCount;
        Draw pane = new Draw();
        pane.enableDoubleBuffering();        // defer rendering until show() is called
        pane.setCanvasSize(width, height);
        pane.clear(Draw.GRAY);        // set background
        grid(axisSize , axisSize, unit, pane);
        return pane;
    }

}



//        public static void drawPoints(Int2D[] points, Draw pane){
//            pane.setPenColor(pane.getPenColor().darker());
//            for(Int2D p : points){
//                drawPoint(p, pane);
//            }
//            pane.setPenColor();
//        }
    //        private static void drawCircle(Int2D v, double diameter, Draw pane){
//            pane.setPenRadius(0.01);
//            pane.circle(
//                    v.x() * unit,
//                    v.y() * unit,
//                    diameter * unit * 0.5);
//            pane.setPenRadius();
//        }
    // draws a smaller circle
//        public static void drawPoint(Int2D p, Draw pane){
//            double diameter = 0.1;
//            pane.filledCircle(
//                    p.x() * unit,
//                    p.y() * unit,
//                    diameter * unit );
//        }
//    public static void main(String[] args) {
//            Draw pane = new Draw();
//            init(pane);
// //            twoPointsInline_displayTest(pane);
//            pane.show();
//        private static void pause(long ms){
//            long start = System.currentTimeMillis();
//            long end = ms + start;
//            while(true){
//                if(System.currentTimeMillis() > end)
//                    return;
//            }
//        }
//        public static void pause(){
//            pause(100);
//        }
//    public static void drawPoints(Iterable<Int2D> points, int msPause, Draw pane) {
//        pane.setPenColor(pane.getPenColor().darker());
//        for (Int2D p : points) {
//            drawPoint(p, pane);
//            pane.show();
//            pane.pause(msPause);
//        }
//        pane.setPenColor();
//    }
//        public static void drawPoint(Int2D p, boolean alt, Draw pane){
//            pane.setPenRadius(0.05);
//            double diameter = 0.1;
//            if(alt){
//                pane.setPenColor(Draw.GREEN);
//                diameter = 0.05;
//            }
//            drawCircle(p, diameter , pane);
//            pane.setPenColor();
//        }
//        private static void drawCirclesMini(Iterable<Int2D> points, Draw pane){
//            points.forEach(p -> drawCircle(p, pane));
//        }
//        public static void drawBlock(Int2D p, Draw pane){
//            pane.setPenColor();
//            pane.square(
//                    p.x() * unit,
//                    p.y() * unit,
//                    0.5* unit
//                    );
//        }
//
//
//            Draw paneDebug = new Draw();
//            init(paneDebug);
//            paneDebug.show();
//            pause();
//            drawCircle(4,4);
//            show();

//        private static void twoPointsInline_displayTest(Draw pane) {
//            Bag<Int2D> points = new Bag<>();
//            Int2D a = new Int2D(1,1);
//            Int2D b = new Int2D(1,3);
//            Int2D m = new Int2D(1,2);
//
//            points.add(a);
//            points.add(b);
//            drawCirclesMini(points, pane);
//            drawPoint(m, pane);
//        }
//        public static void drawCircle(Int2D v , Draw pane){
//            pane.setPenColor(Draw.PRINCETON_ORANGE);
//            pane.setPenRadius(0.01);
//            pane.circle(
//                    v.x() * unit,
//                    v.y() * unit,
//                    pointDiameter * unit * 0.5 );
//            pane.setPenRadius();
//        }






