package autoRouter;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Draw;

/// Wraps `algs4.Draw` shape painting methods, to simplify painting data defined from `SquareGrid`.
/// TODO add listeners for mouse events, Get position of mouse on gridmap during click -> add location to node list.
/// TODO drop-down menu for some predefined examples
/// TODO button to generate random nodes
/// TODO nametags for nodes or unique node colors with legend
public class Display implements Runnable{

        Draw pane;

    /// Displays information provided by the Plane object (node positions as circles, edges as lines)

        static final int width = 600; static final int height = 600;
        static final double gridCount = SquareGrid.squares;
        static final double unit = 1 / gridCount;   // dimensions of unit square , gives a 20 x 20 grid
        static double pointDiameter = 0.5;          // point size

        public static void grid(Draw pane){
            grid(width, height, unit , pane );
        }
        /// Draw a grid, 20x20
        private static void grid(int width, int height, double unit, Draw pane){
            pane.setPenColor(Draw.LIGHT_GRAY);
            for (int x = 0; x < width; x ++){
                pane.setPenRadius((x % 5 == 0) ? 0.002 : 0.0005);
                double xTick = x * unit;
                pane.line(xTick, 0.0,xTick,1.0);
            }
            for (int y = 0; y < height; y ++){
                pane.setPenRadius((y % 5 == 0) ? 0.002 : 0.0005);
                double yTick = y * unit;
                pane.line(0.0, yTick,1.0,yTick);
            }
            pane.setPenColor(Draw.BLACK);
        }

        private static void twoPointsInline_displayTest(Draw pane) {
            Bag<Int2D> points = new Bag<>();
            Int2D a = new Int2D(1,1);
            Int2D b = new Int2D(1,3);
            Int2D m = new Int2D(1,2);

            points.add(a);
            points.add(b);
            drawCirclesMini(points, pane);
            drawPoint(m, pane);
        }
        // TODO Associate circles with names. write either a nametag placement method or uniquely color circle with a legend
        public static void drawCircle(Int2D v , Draw pane){
            pane.setPenColor(Draw.PRINCETON_ORANGE);
            pane.setPenRadius(0.01);
            pane.circle(
                    v.x() * unit,
                    v.y() * unit,
                    pointDiameter * unit * 0.5 );
            pane.setPenRadius();
        }

        private static void drawCircle(Int2D v, double diameter, Draw pane){
            pane.setPenRadius(0.01);
            pane.circle(
                    v.x() * unit,
                    v.y() * unit,
                    diameter * unit * 0.5);
            pane.setPenRadius();
        }
        // draws a smaller circle
        public static void drawPoint(Int2D p, Draw pane){
            double diameter = 0.1;
            pane.filledCircle(
                    p.x() * unit,
                    p.y() * unit,
                    diameter * unit );
        }
        public static void drawPoints(Int2D[] points, Draw pane){
            pane.setPenColor(pane.getPenColor().darker());
            for(Int2D p : points){
                drawPoint(p, pane);
            }
            pane.setPenColor();
        }
    public static void drawPoints(Iterable<Int2D> points, int msPause, Draw pane) {
        pane.setPenColor(pane.getPenColor().darker());
        for (Int2D p : points) {
            drawPoint(p, pane);
            pane.show();
            pane.pause(msPause);
        }
        pane.setPenColor();
    }
        public static void drawPoint(Int2D p, boolean alt, Draw pane){
            pane.setPenRadius(0.05);
            double diameter = 0.1;
            if(alt){
                pane.setPenColor(Draw.GREEN);
                diameter = 0.05;
            }
            drawCircle(p, diameter , pane);
            pane.setPenColor();
        }
        private static void drawCirclesMini(Iterable<Int2D> points, Draw pane){
            points.forEach(p -> drawCircle(p, pane));
        }
        public static void drawBlock(Int2D p, Draw pane){
            pane.setPenColor();
            pane.square(
                    p.x() * unit,
                    p.y() * unit,
                    0.5* unit
                    );
        }
        public static void init(Draw pane){
            pane.enableDoubleBuffering();        // defer rendering until show() is called. Draws noticeably faster and allows animation
            pane.setCanvasSize(width, height);
            pane.clear(Draw.GRAY);        // set background
            grid(pane);
        }

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

        public static void main(String[] args) {
            Draw pane = new Draw();
            init(pane);
            twoPointsInline_displayTest(pane);
            pane.show();
//
//            Draw paneDebug = new Draw();
//            init(paneDebug);
//            paneDebug.show();
//            pause();
//            drawCircle(4,4);
//            show();
        }

    @Override
    public void run() {

    }
}




