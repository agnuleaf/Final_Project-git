package grid;
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

    private static int width = 300;
    private static int height = 300;
    static int gridCount = 10;
    static double unit = 1.0 / gridCount;   // dimensions of unit square , gives a 20 x 20 grid
    static double pointScalar = 0.5;          // point size

    public Display(){
        pane = Display.init();
    }
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
    public void showMessage(String msg){
        pane.text(width / 2.0, height, msg);
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
        public static void drawCircle(GridPoint p, Draw pane) {
            drawCircle(p.x(),p.y(),pane);
        }


    public static void drawPath(GridPoint p, double offset, Color color, Draw pane) {

        pane.setPenColor(color);
        pane.setPenRadius(0.01);
        pane.circle(
                p.x() * unit + offset,
                p.y() * unit + offset,
                pointScalar * unit * 0.5);
        pane.setPenRadius();
        pane.setPenColor();
    }
    public static void drawSteinerPoint(GridPoint p, Draw pane) {
        pane.setPenColor(Draw.CYAN);
        pane.setPenRadius(0.005);
        pane.circle(
                p.x() * unit,
                p.y() * unit,
                pointScalar * unit * 0.3);
        pane.setPenRadius();
        pane.setPenColor();
    }
    /// D
    private static void drawPoint(int x, int y, Draw pane) {
        double diameter = 0.1;
        pane.filledCircle(
                x * unit,
                y * unit,
                diameter * unit);
    }
    /// Draws a small circle of the given color
    public static void drawPoint(GridPoint p, Color color, Draw pane) {
        double diameter = 0.1;
        pane.setPenColor(color);
        pane.filledCircle(
                p.x() * unit,
                p.y() * unit,
                diameter * unit);
        pane.setPenColor();
    }
    /// Draws small black circle
        public static void drawPoint(GridPoint p, Draw pane) {
        double diameter = 0.1;
        pane.setPenColor();
        pane.filledCircle(
                p.x() * unit,
                p.y() * unit,
                diameter * unit);
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
    public static void drawSquare(GridPoint p, int halfWidth, Color color, Draw pane){
        pane.setPenColor(color);
        pane.square(
                p.x() * unit,
                p.y() * unit,
                halfWidth * unit
        );
        pane.setPenColor();
    }
    /// TODO explored regions and search square display. Must be in the background : translucent or painted before all other animations somehow.
    public static void drawCoverage(GridPoint p, int halfWidth, Color color, Draw pane){

    }
    public static void drawCircles(GridPoint[] circles, Draw pane) {
        pane.setPenColor(pane.getPenColor().darker());
        for (GridPoint p : circles){
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




