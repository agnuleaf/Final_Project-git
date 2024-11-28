package grid;
import edu.princeton.cs.algs4.Draw;

import javax.swing.text.StyleConstants;
import java.awt.*;

// TODO add listeners for mouse events, Get position of mouse on gridmap during click -> add location to node list.
// TODO drop-down menu for some predefined examples
// TODO button to generate random nodes
// TODO Associate circles with nametags.  Either write a nametag placement method or uniquely color circle with a legend
/// Wraps `algs4.Draw` shape painting methods for painting on a 2D grid.
public class Display {

    private final Draw pane = new Draw();
    private Color background = Color.DARK_GRAY;
    private Color gridline   = Color.LIGHT_GRAY;
    /// Displays information provided by the Plane object (node positions as circles, edges as lines)

    private int width = 300;
    private int height = 300;
    int squares = 1;
    static int gridCount = 10;
    static double unit = 1.0 / gridCount;   // dimensions of unit square , gives a 20 x 20 grid
    static double pointScalar = 0.5;          // point size

    public Display(int squares, int squareScale){
        this.squares = squares;
        int scale = 512;
        width = scale;
        height = scale;
        pane.setCanvasSize(width, height);
        pane.setXscale(0, squares);
        pane.setYscale(0, squares);



    }
    public Draw getPane(){
        return pane;
    }
    public static void main(String[] args) {
        Display display = new Display(20, 1);
        display.grid();
        Draw pane = display.getPane();
        display.showMessage("Title Message");
        GridPoint[] wall = new GridPoint[]{
                new GridPoint ( 3 , 3 ),
                new GridPoint ( 3 , 2 ),
                new GridPoint ( 2 , 3 ),
                new GridPoint ( 2 , 2 ),
        };
        GridPoint[] known = new GridPoint[]{
                new GridPoint ( 6 , 5 ),
                new GridPoint ( 5 , 5 ), new GridPoint ( 5 , 4 ),  new GridPoint ( 5 , 6 ),
                new GridPoint ( 4 , 5 ), new GridPoint ( 4 , 4 ),  new GridPoint ( 4 , 6 ),
                new GridPoint ( 3 , 5 ), new GridPoint ( 3 , 4 ),  new GridPoint ( 3 , 6 ),
                new GridPoint ( 2 , 5 ),
        };
        for( GridPoint p : wall  ){   display.wall(p);        }
        for( GridPoint p : known ){   display.discovered(p);  }
        GridPoint[] path = new GridPoint[]{
                new GridPoint ( 5 , 5 ),
                new GridPoint ( 4 , 5 ),
                new GridPoint ( 3 , 5 ),
                new GridPoint ( 3 , 6 ),
        };
        for(int i = 1; i < path.length ; i ++){
            display.path(path[i - 1], path[i], Color.RED.darker());
        }
        display.endPoint( new GridPoint (1,1), true);
        display.endPoint( new GridPoint (9,9), false);

//        display.placeText(new GridPoint(1,5), "Left");
//        display.placeText(new GridPoint(9,5), "Right");


        pane.show();
    }
    public void discovered(GridPoint p){
        pane.setPenRadius();
//        pane.setPenColor(Color.WHITE);
//        pane.filledSquare(
//                p.x() + 0.5,
//                p.y() + 0.5,
//                0.4);
//        circle(p,Color.GRAY);

        pane.setPenColor(new Color(255,155,175,80));// Color.PINK.);
        pane.setPenRadius();
        pane.filledSquare(
                p.x() + 0.5,
                p.y() + 0.5,
                0.5);
        pane.setPenRadius();
    }

    public void path(GridPoint p, GridPoint q, Color color){
        pane.setPenColor(alphaColor(color.darker(),128));
        pane.setPenRadius(0.02);
        pane.line(p.x() + 0.5, p.y() + 0.5, q.x() + 0.5, q.y() + 0.5 );

    }
    private Color alphaColor(Color color, int alpha){
        return new Color( // add alpha to path color for transparency
                color.getRGB()   & 0xff0000 >>16,
                color.getRGB()   & 0x00ff00 >>8,
                color.getRGB()   & 0x0000ff,
                alpha            & 0xff
        );
    }
    public void circle( GridPoint p, Color color){
        pane.setPenRadius();
        pane.setPenColor(color);
        pane.circle(
                p.x() + 0.5,
                p.y() + 0.5,
                0.5);
        pane.setPenRadius();
    }
    public void wall(GridPoint p){
        pane.setPenRadius();
        pane.setPenColor();
        pane.filledSquare(
                p.x() + 0.5,
                p.y() + 0.5,
                0.5);
        pane.setPenRadius();
    }
    private void fill( GridPoint p, Color color){
        pane.setPenRadius();
        pane.setPenColor(color);
        pane.filledSquare(
                p.x() + 0.5,
                p.y() + 0.5,
                0.5);
        pane.setPenRadius();
    }
    public void endPoint(GridPoint p, boolean isStart){
        pane.setPenRadius(0.01);
        Color color = (isStart? Draw.GREEN.darker() : Draw.RED.darker());
        pane.setPenColor(color);
        pane.filledCircle(
                p.x() + 0.5,
                p.y() + 0.5,
                 0.4);
        pane.setPenRadius(0.005);
        pane.setPenColor(color.darker());
        pane.circle(
                p.x() + 0.5,
                p.y() + 0.5,
                0.4);
        if(isStart){
            pane.setPenColor(Draw.RED.darker());
        }else{
            pane.setPenColor(Draw.GREEN);
        }
        pane.text(p.x() + 0.5,p.y() + 0.5, (isStart? "A" : "B"));
        pane.setPenColor();
        pane.setPenRadius();
    }
    public void placeText(GridPoint p, String msg){
        if(p.x() < squares / 2){ // left justify
            pane.setPenColor(gridline);
            pane.filledRectangle(p.x() + 0.5,p.y(),0.8,0.4);
            pane.setPenColor(background.darker());
            pane.rectangle(p.x(),p.y(), 0.8, 0.4);
            pane.textLeft(p.x() + 0.2,p.y(),msg);
        }else {
            pane.setPenColor(gridline);
            pane.filledRectangle(p.x() - 0.5,p.y(),0.8,0.4);
            pane.setPenColor(background.darker());
            pane.rectangle(p.x(),p.y(), 0.8, 0.4);
            pane.textLeft(p.x() - 0.2,p.y(),msg);
        }
    }
    /// Draws grid in 5-square blocks, dark-gray background
    public void grid(){
        pane.clear(Color.DARK_GRAY);
        pane.setPenColor(Color.LIGHT_GRAY);

        for (int x = 0; x < squares; x++) {
            pane.setPenRadius((x % 5 == 0) ? 0.002 : 0.0005);
            pane.line(x, 0.0, x, squares);
        }
        for (int y = 0; y < squares; y++) {
            pane.setPenRadius((y % 5 == 0) ? 0.002 : 0.0005);
            pane.line(0.0, y, squares, y);
        }
        pane.setPenColor(Draw.BLACK);
    }

     public void showMessage(String msg){
        pane.show();
        pane.setPenColor(Color.LIGHT_GRAY);
        pane.filledRectangle(squares / 2.0 , squares - 2 , 2, 1);
        pane.setPenColor();
        pane.text(squares / 2.0, squares -2 , msg);
    }
    private void centeredString(String msg){

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
//    public static Draw init() {
//        Draw pane = new Draw();
//        gridCount = 10;
//        unit = 1.0 / gridCount;
//
//        pane.enableDoubleBuffering();        // defer rendering until show() is called
//        pane.setCanvasSize(width, height);
//        pane.clear(Draw.GRAY);        // set background
//        grid(pane);
//        pane.pause(20);
//        pane.show();
//        return pane;
//    }
//
//    /// static builder for draw pane with gridcount input
//    public static Draw init(int axisSize){
//
//        gridCount = axisSize;
//        unit = 1 / gridCount;
//        Draw pane = new Draw();
//        pane.enableDoubleBuffering();        // defer rendering until show() is called
//        pane.setCanvasSize(width, height);
//        pane.clear(Draw.GRAY);        // set background
//        grid(axisSize , axisSize, unit, pane);
//        return pane;
//    }
}




