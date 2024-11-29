package grid;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.DrawListener;
import edu.princeton.cs.algs4.Stack;

import java.awt.*;

/// Wraps `algs4.Draw` shape painting methods for painting on a 2D grid.
public class Display {
    private static final double shift = -0.5;  // shift placement of shapes
    private final Draw draw;
    private final Color backgroundColor = Color.DARK_GRAY;
    private final Color gridColor = Color.LIGHT_GRAY;
    private final double dropShadow = 0.01;
    private final Stack<GridPoint> wallsPlaced;    //  undo painting a shape
    private final Stack<GridPoint> endpoints;
    /// Displays information provided by the Plane object (node positions as circles, edges as lines)

    private int squares;

    public Display(int squares, Draw draw) {
        this.squares = squares;
        this.draw = draw;
        int scale = 512;
        int width = scale;
        int height = scale;
        draw.setCanvasSize(width, height);
        draw.setXscale(0, squares);
        draw.setYscale(0, squares);
        wallsPlaced = new Stack<>();
        endpoints = new Stack<>();

    }
    public void setSquaresPerAxis(int squares){
        this.squares = squares;
    }
    public Draw getDraw() {
        return draw;
    }
    public Iterable<GridPoint> getWalls(){
        return wallsPlaced;
    }
    public Iterable<GridPoint> getEndpoints(){ return endpoints; }
    //@formatter:off
    public static void main(String[] args) {
        Display display = new Display(20, new Draw());
        display.grid();
        Draw draw = display.draw;
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
        draw.pause(500);

        for(int i = 0; i < 5; i++){ display.undoWallPlacement(); draw.pause(300); }
        for(int i = 0; i < 5; i++){ display.undoEndpointPlacement(); draw.pause(300); }

        display.placeEndpoint( new GridPoint (1,1), true);
        display.placeEndpoint( new GridPoint (9,9), false);
//        display.placeText(new GridPoint(1,5), "Left");
//        display.placeText(new GridPoint(9,5), "Right");


        draw.show();
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
        draw.setPenRadius();
        draw.setPenColor(new Color(255,155,175,80));// Color.PINK.);
        draw.setPenRadius();
        draw.filledSquare(
                p.x() + shift,
                p.y() + shift,
                0.5);
        draw.setPenRadius();
    }

    /// Displays the path taken
    public void path(GridPoint p, GridPoint q, Color color){
        draw.setPenColor(alphaColor(color.darker(),128));
        draw.setPenRadius(0.03);
        draw.line(p.x() + shift, p.y() + shift, q.x() + shift, q.y() + shift );

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
        draw.setPenRadius();
        draw.setPenColor(color);
        draw.circle(
                p.x() + shift,
                p.y() + shift,
                0.2);
        draw.setPenRadius();
    }
    /// Draws a black square for a wall
    public void placeWall(GridPoint p){
        draw.setPenRadius();
        draw.setPenColor();
        draw.filledSquare(
                p.x() + shift,
                p.y() + shift,
                0.5);
        draw.setPenRadius();
        wallsPlaced.push(p);
    }
    /// Fills the square with the given color
    private void fillSquare(GridPoint p, Color color){
        draw.setPenRadius();
        draw.setPenColor(color);
        draw.filledSquare(
                p.x() + shift,
                p.y() + shift,
                0.5);
        draw.setPenRadius();
    }

    // helper for undo last user input. Redraws a single grid square with outlines.
    private void eraseSquare( GridPoint p ){
        fillSquare(p,Color.LIGHT_GRAY.brighter());
        draw.pause(100);
        fillSquare(p, gridColor);
        draw.pause(100);
        fillSquare(p, backgroundColor);

        // redraw the grid with correct weights
        draw.setPenColor(gridColor);
        double gridlineX = ( p.x() % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineX);
        draw.line(  p.x(),  p.y(),
                    p.x() , p.y() + 1.0 );

        gridlineX = ( (p.x()+1.0) % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineX);
        draw.line(  p.x() + 1.0,  p.y() ,
                    p.x() + 1.0,  p.y() +1.0);
        draw.setPenColor(gridColor);
        double gridlineY = ( (p.y()) % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineY);

        draw.line(  p.x()       , p.y(),
                    p.x() + 1.0 , p.y());
        gridlineY = ( (p.y()+1.0) % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineY);

        draw.line(  p.x()       , p.y() + 1.0,
                    p.x() + 1.0 , p.y() + 1.0);
        draw.setPenRadius();
    }

    /// Draws two distinct nodes as 'A' for Start and 'B' for Finish.
    public void placeEndpoint(GridPoint p, boolean isStart){
        draw.setPenRadius(0.01);
        Color color = (isStart? Draw.GREEN.darker() : Draw.RED.darker());
        draw.setPenColor(color);
        draw.filledCircle(
                p.x() + shift,
                p.y() + shift,
                 0.4);
        draw.setPenRadius(0.005);
        draw.setPenColor(color.darker());
        draw.circle(
                p.x() + shift,
                p.y() + shift,
                0.4);
        if(isStart){
            draw.setPenColor(Draw.RED.darker());
        }else{
            draw.setPenColor(Draw.GREEN);
        }
        draw.text(p.x() + shift,p.y() + shift, (isStart? "A" : "B"));
        draw.setPenColor();
        draw.setPenRadius();
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
            draw.setPenColor(gridColor);
            draw.filledRectangle(p.x() + 0.5,p.y(),0.8,0.4);
            draw.setPenColor(backgroundColor.darker());
            draw.rectangle(p.x(),p.y(), 0.8, 0.4);
            draw.textLeft(p.x() + 0.2,p.y(),msg);
        }else {
            draw.setPenColor(gridColor);
            draw.filledRectangle(p.x() - 0.5 ,p.y(),0.8,0.4);
            draw.setPenColor(backgroundColor.darker());
            draw.rectangle(p.x(),p.y(), 0.8, 0.4);
            draw.textLeft(p.x() - 0.2,p.y(),msg);
        }

    }
    /// Draws grid in 5-square blocks, dark-gray background
    public void grid(){
        draw.clear(Color.DARK_GRAY);
        draw.setPenColor(Color.LIGHT_GRAY);

        for (int x = 0; x < squares; x++) {
            draw.setPenRadius((x % 5 == 0) ? 0.002 : 0.0005);
            draw.line(x, 0.0, x, squares);
        }
        for (int y = 0; y < squares; y++) {
            draw.setPenRadius((y % 5 == 0) ? 0.002 : 0.0005);
            draw.line(0.0, y, squares, y);
        }
        draw.setPenColor(Draw.BLACK);
    }
    /// Draws grid in 5-square blocks, dark-gray background
    public void grid(GridPoint[] walls){
        draw.clear(Color.DARK_GRAY);
        draw.setPenColor(Color.LIGHT_GRAY);

        for (int x = 0; x < squares; x++) {
            draw.setPenRadius((x % 5 == 0) ? 0.002 : 0.0005);
            draw.line(x, 0.0, x, squares);
        }
        for (int y = 0; y < squares; y++) {
            draw.setPenRadius((y % 5 == 0) ? 0.002 : 0.0005);
            draw.line(0.0, y, squares, y);
        }
        draw.setPenColor(Draw.BLACK);
        for(GridPoint w : walls){ placeWall(w); }
    }

    /// Displays a title message in the top center of the grid
     public void showMessage(String msg){
        draw.show();
        draw.setPenColor(Color.BLACK);
        draw.filledRectangle(squares / 2.0 , squares - 2 , 2, 1);

        draw.setPenColor(Color.LIGHT_GRAY);
        draw.filledRectangle(squares / 2.0 , squares - 2 , 2, 1);
        draw.setPenColor();
        draw.text(squares / 2.0, squares -2 , msg);
    }

    public void drawCircle(GridPoint p) {
        draw.setPenRadius(0.01);
        draw.circle(
                p.x(),
                p.y(),
                0.5);
        draw.setPenRadius();
    }
    public void drawCircles(GridPoint[] circles) {
        draw.setPenColor(draw.getPenColor().darker());
        for (GridPoint p : circles){
            drawCircle(p);
        }
        draw.setPenColor();
    }
    /// Draws small black circle
    public void drawPoint(GridPoint p) {
        double diameter = 0.1;
        draw.setPenColor();
        draw.filledCircle(
                p.x(),
                p.y(),
                diameter);
    }

    public void drawPoint(GridPoint p, Color color) {
        double diameter = 0.1;
        draw.setPenColor(color);
        draw.filledCircle(
                p.x(),
                p.y(),
                diameter );
        draw.setPenColor();
    }

//    public static void drawPath(GridPoint p, double offset, Color color, Draw draw) {
//
//        draw.setPenColor(color);
//        draw.setPenRadius(0.01);
//        draw.circle(
//                p.x() * unit + offset,
//                p.y() * unit + offset,
//                pointScalar * unit * 0.5);
//        draw.setPenRadius();
//        draw.setPenColor();
//    }


//    public static void drawSteinerPoint(GridPoint p, Draw draw) {
//        draw.setPenColor(Draw.CYAN);
//        draw.setPenRadius(0.005);
//        draw.circle(
//                p.x() * unit,
//                p.y() * unit,
//                pointScalar * unit * 0.3);
//        draw.setPenRadius();
//        draw.setPenColor();
//    }
//
}



