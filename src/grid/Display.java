package grid;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Stack;

import java.awt.*;

// TODO add listeners for mouse events, Get position of mouse on gridmap during click -> add location to node list.
// TODO drop-down menu for some predefined examples
// TODO button to generate random nodes
// TODO Associate circles with nametags.  Either write a nametag placement method or uniquely color circle with a legend
/// Wraps `algs4.Draw` shape painting methods for painting on a 2D grid.
public class Display {
    private static final double shift = maze.MazeApp.shiftGrid;  // shift placement of shapes
    private final Draw pane = new Draw();
    private Color backgroundColor = Color.DARK_GRAY;
    private Color gridColor = Color.LIGHT_GRAY;
    private final double dropShadow = 0.01;
    private Stack<GridPoint> wallsPlaced;    //  undo painting a shape
    private Stack<GridPoint> endpoints;
    /// Displays information provided by the Plane object (node positions as circles, edges as lines)

    private int width = 300;
    private int height = 300;
    int squares;
//    static int gridCount = 10;
//    static double unit = 1.0 / gridCount;   // dimensions of unit square , gives a 20 x 20 grid
//    static double pointScalar = 0.5;          // point size

    public Display(int squares/*, int squareScale*/) {
        this.squares = squares;
        int scale = 512; // todo height and width
        width = scale;
        height = scale;
        pane.setCanvasSize(width, height);
        pane.setXscale(0, squares);
        pane.setYscale(0, squares);
        wallsPlaced = new Stack<>();
        endpoints = new Stack<>();



    }

    public Draw getPane() {
        return pane;
    }
    public Iterable<GridPoint> getWalls(){
        return wallsPlaced;
    }
    //@formatter:off
    public static void main(String[] args) {
        Display display = new Display(20);
        display.grid();
        Draw pane = display.getPane();
        display.showMessage("Title Message");

        GridPoint[] wall = new GridPoint[]{
                new GridPoint ( 3 , 3 ),
                new GridPoint ( 7 , 6 ),
                new GridPoint ( 6 , 6 ),
                new GridPoint ( 6 , 7 ),
                new GridPoint ( 8 , 7 ),
                new GridPoint ( 8 , 8 ),
                new GridPoint ( 7 , 8 ),
                new GridPoint ( 7 , 7 ),
                new GridPoint ( 10, 10),
        };
        GridPoint[] known = new GridPoint[]{
                new GridPoint ( 6 , 5 ),
                new GridPoint ( 5 , 5 ), new GridPoint ( 5 , 4 ),  new GridPoint ( 5 , 6 ),
                new GridPoint ( 4 , 5 ), new GridPoint ( 4 , 4 ),  new GridPoint ( 4 , 6 ),
                new GridPoint ( 3 , 5 ), new GridPoint ( 3 , 4 ),  new GridPoint ( 3 , 6 ),
                new GridPoint ( 2 , 5 ),
        };
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
        for( GridPoint p : wall  ){   display.placeWall(p);        }
        display.placeEndpoint( new GridPoint (1,1), true);
        display.placeEndpoint( new GridPoint (9,9), false);
        pane.pause(500);

        for(int i = 0; i < 5; i++){ display.undoWallPlacement(); pane.pause(300); }
        for(int i = 0; i < 5; i++){ display.undoEndpointPlacement(); pane.pause(300); }

        display.placeEndpoint( new GridPoint (1,1), true);
        display.placeEndpoint( new GridPoint (9,9), false);
//        display.placeText(new GridPoint(1,5), "Left");
//        display.placeText(new GridPoint(9,5), "Right");


        pane.show();
    }

    /// Erases the most recent wall in the input history stack
    public void undoWallPlacement() {
        if(!wallsPlaced.isEmpty()){
            GridPoint p = wallsPlaced.pop();
            eraseSquare(p);
        }
    }
    ///  Erases the most recent endpoint in the input history stack
    public void undoEndpointPlacement() {
        if(!endpoints.isEmpty()){
            GridPoint p = endpoints.pop();
            eraseSquare(p);
        }
    }
    /// Displays the nodes visited as light, translucent overlay.
    public void discovered(GridPoint p){
        pane.setPenRadius();
        pane.setPenColor(new Color(255,155,175,80));// Color.PINK.);
        pane.setPenRadius();
        pane.filledSquare(
                p.x() + shift,
                p.y() + shift,
                0.5);
        pane.setPenRadius();
    }

    /// Displays the path taken
    public void path(GridPoint p, GridPoint q, Color color){
        pane.setPenColor(alphaColor(color.darker(),128));
        pane.setPenRadius(0.03);
        pane.line(p.x() + shift, p.y() + shift, q.x() + shift, q.y() + shift );

    }
    // Adds translucency as the alpha value to a color. higher alpha = high opacity.
    private Color alphaColor(Color color, int alpha){
        return new Color( // add alpha to path color for transparency
                color.getRGB()   & 0xff0000 >>16,
                color.getRGB()   & 0x00ff00 >>8,
                color.getRGB()   & 0x0000ff,
                alpha            & 0xff
        );
    }
    /// Draws a faint circle outline
    public void circleOutline( GridPoint p, Color color){
        pane.setPenRadius();
        pane.setPenColor(color);
        pane.circle(
                p.x() + shift,
                p.y() + shift,
                0.2);
        pane.setPenRadius();
    }
    /// Draws a black square for a wall
    public void placeWall(GridPoint p){
        pane.setPenRadius();
        pane.setPenColor();
        pane.filledSquare(
                p.x() + shift,
                p.y() + shift,
                0.5);
        pane.setPenRadius();
        wallsPlaced.push(p);
    }
    /// Fills the square with the given color
    private void fillSquare(GridPoint p, Color color){
        pane.setPenRadius();
        pane.setPenColor(color);
        pane.filledSquare(
                p.x() + shift,
                p.y() + shift,
                0.5);
        pane.setPenRadius();
    }

    // helper for undo last user input. Redraws a single grid square with outlines.
    private void eraseSquare( GridPoint p ){
        fillSquare(p,Color.LIGHT_GRAY.brighter());
        pane.pause(100);
        fillSquare(p, gridColor);
        pane.pause(100);
        fillSquare(p, backgroundColor);

        // redraw the grid with correct weights
        pane.setPenColor(gridColor);
        double gridlineX = ( p.x() % 5 == 0 ? 0.002 : 0.0005);
        pane.setPenRadius(gridlineX);
        pane.line(  p.x(),  p.y(),
                    p.x() , p.y() + 1.0 );

        gridlineX = ( (p.x()+1.0) % 5 == 0 ? 0.002 : 0.0005);
        pane.setPenRadius(gridlineX);
        pane.line(  p.x() + 1.0,  p.y() ,
                    p.x() + 1.0,  p.y() +1.0);
        pane.setPenColor(gridColor);
        double gridlineY = ( (p.y()) % 5 == 0 ? 0.002 : 0.0005);
        pane.setPenRadius(gridlineY);

        pane.line(  p.x()       , p.y(),
                    p.x() + 1.0 , p.y());
        gridlineY = ( (p.y()+1.0) % 5 == 0 ? 0.002 : 0.0005);
        pane.setPenRadius(gridlineY);

        pane.line(  p.x()       , p.y() + 1.0,
                    p.x() + 1.0 , p.y() + 1.0);
        pane.setPenRadius();
    }

    /// Draws two distinct nodes as 'A' for Start and 'B' for Finish.
    public void placeEndpoint(GridPoint p, boolean isStart){
        pane.setPenRadius(0.01);
        Color color = (isStart? Draw.GREEN.darker() : Draw.RED.darker());
        pane.setPenColor(color);
        pane.filledCircle(
                p.x() + shift,
                p.y() + shift,
                 0.4);
        pane.setPenRadius(0.005);
        pane.setPenColor(color.darker());
        pane.circle(
                p.x() + shift,
                p.y() + shift,
                0.4);
        if(isStart){
            pane.setPenColor(Draw.RED.darker());
        }else{
            pane.setPenColor(Draw.GREEN);
        }
        pane.text(p.x() + shift,p.y() + shift, (isStart? "A" : "B"));
        pane.setPenColor();
        pane.setPenRadius();
        endpoints.push(p);
    }
    /// Checks if the provide point is in the wall stack.
    public boolean isWall(GridPoint p){
        for (GridPoint w : wallsPlaced){
            if(w.equals(p)){ return true; }
        }
        return false;
    }

    private void placeText(GridPoint p, String msg){
        if(p.x() < squares / 2){ // left justify
            pane.setPenColor(gridColor);
            pane.filledRectangle(p.x() + 0.5,p.y(),0.8,0.4);
            pane.setPenColor(backgroundColor.darker());
            pane.rectangle(p.x(),p.y(), 0.8, 0.4);
            pane.textLeft(p.x() + 0.2,p.y(),msg);
        }else {
            pane.setPenColor(gridColor);
            pane.filledRectangle(p.x() - 0.5 ,p.y(),0.8,0.4);
            pane.setPenColor(backgroundColor.darker());
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
    /// Draws grid in 5-square blocks, dark-gray background
    public void grid(GridPoint[] walls){
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
        for(GridPoint w : walls){ placeWall(w); }
    }

    /// Displays a title message in the top center of the grid
     public void showMessage(String msg){
        pane.show();
        pane.setPenColor(Color.BLACK);
        pane.filledRectangle(squares / 2.0 , squares - 2 , 2, 1);

        pane.setPenColor(Color.LIGHT_GRAY);
        pane.filledRectangle(squares / 2.0 , squares - 2 , 2, 1);
        pane.setPenColor();
        pane.text(squares / 2.0, squares -2 , msg);
    }

    public void drawCircle(GridPoint p) {
        pane.setPenRadius(0.01);
        pane.circle(
                p.x(),
                p.y(),
                0.5);
        pane.setPenRadius();
    }
    public void drawCircles(GridPoint[] circles) {
        pane.setPenColor(pane.getPenColor().darker());
        for (GridPoint p : circles){
            drawCircle(p);
        }
        pane.setPenColor();
    }
    /// Draws small black circle
    public void drawPoint(GridPoint p) {
        double diameter = 0.1;
        pane.setPenColor();
        pane.filledCircle(
                p.x(),
                p.y(),
                diameter);
    }

    public void drawPoint(GridPoint p, Color color) {
        double diameter = 0.1;
        pane.setPenColor(color);
        pane.filledCircle(
                p.x(),
                p.y(),
                diameter );
        pane.setPenColor();
    }

//    public static void drawPath(GridPoint p, double offset, Color color, Draw pane) {
//
//        pane.setPenColor(color);
//        pane.setPenRadius(0.01);
//        pane.circle(
//                p.x() * unit + offset,
//                p.y() * unit + offset,
//                pointScalar * unit * 0.5);
//        pane.setPenRadius();
//        pane.setPenColor();
//    }


//    public static void drawSteinerPoint(GridPoint p, Draw pane) {
//        pane.setPenColor(Draw.CYAN);
//        pane.setPenRadius(0.005);
//        pane.circle(
//                p.x() * unit,
//                p.y() * unit,
//                pointScalar * unit * 0.3);
//        pane.setPenRadius();
//        pane.setPenColor();
//    }
//
}



