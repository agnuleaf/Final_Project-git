package grid;
import edu.princeton.cs.algs4.*;

import java.awt.*;

/// Wraps `algs4.Draw` shape painting methods for painting on a 2D grid.
public class GridDraw {
    private static final double shift = -0.5;  // shift placement of shapes
    private final Draw draw;
    private final Color backgroundColor = Color.DARK_GRAY;
    private final Color gridColor = Color.LIGHT_GRAY;

    private int ticks;
    private int tPause = 25;     // pause time between draws
    final int DEFAULT_SIZE = 512;
    private int width  = DEFAULT_SIZE;
    private int height = DEFAULT_SIZE;
    public GridDraw(int squares, Draw draw) {
        this.ticks = squares;
        this.draw = draw;
    }
    public void setWidthHeight(int width, int height){
        this.width = width;
        this.height = height;
    }
    public void setRate(boolean isSlow){
        tPause = (isSlow? 300: 25);
    }
    public int getPause(){
        return tPause;
    }
    public void setSquaresPerAxis(int squares){
        this.ticks = squares;
    }
    public Draw getDraw() {
        return draw;
    }

//    //@formatter:off
//    public static void main(String[] args) {
//        Display display = new Display(20, new Draw());
//        display.grid();
//        Draw draw = display.draw;
//        display.showMessage("Title Message");
//        int tPause = display.tPause;
//        GridPoint[] wall = new GridPoint[]{
//                new GridPoint ( 3 , 3 ),
//                new GridPoint ( 7 , 6 ),
//                new GridPoint ( 6 , 6 ),
//                new GridPoint ( 6 , 7 ),
//                new GridPoint ( 8 , 7 ),
//                new GridPoint ( 8 , 8 ),
//                new GridPoint ( 7 , 8 ),
//                new GridPoint ( 7 , 7 ),
//                new GridPoint ( 10, 10),
//        };
//        GridPoint[] known = new GridPoint[]{
//                new GridPoint ( 6 , 5 ),
//                new GridPoint ( 5 , 5 ), new GridPoint ( 5 , 4 ),  new GridPoint ( 5 , 6 ),
//                new GridPoint ( 4 , 5 ), new GridPoint ( 4 , 4 ),  new GridPoint ( 4 , 6 ),
//                new GridPoint ( 3 , 5 ), new GridPoint ( 3 , 4 ),  new GridPoint ( 3 , 6 ),
//                new GridPoint ( 2 , 5 ),
//        };
//        for( GridPoint p : known ){   display.discovered(p);  }
//        GridPoint[] path = new GridPoint[]{
//                new GridPoint ( 5 , 5 ),
//                new GridPoint ( 4 , 5 ),
//                new GridPoint ( 3 , 5 ),
//                new GridPoint ( 3 , 6 ),
//        };
//        for(int i = 1; i < path.length ; i ++){
//            display.path(path[i - 1], path[i], Color.RED.darker());
//        }
//        for( GridPoint p : wall  ){   display.drawWall(p);   }
//
//        display.drawEndpoint( new GridPoint (1,1), true);
//        display.drawEndpoint( new GridPoint (9,9), false);
//        draw.show();
//
//
// //        for(int i = 0; i < 5; i++){ display.eraseSquare(); draw.pause(tPause); }
//        display.drawEndpoint( new GridPoint (1,1), true);
//        display.drawEndpoint( new GridPoint (9,9), false);
// //        display.placeText(new GridPoint(1,5), "Left");
// //        display.placeText(new GridPoint(9,5), "Right");
//        draw.show();
//    }
    /// Draws a black square for a wall
    public void drawWall(GridPoint p){
        draw.setPenRadius();
        draw.setPenColor();
        fillSquare(p, draw.getPenColor());
    }

    // Fills the square with the given color
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
    public void eraseSquare( GridPoint p ){
        fillSquare(p,Color.LIGHT_GRAY.brighter());
        int tDelete = 100;
        draw.pause(tDelete);
        fillSquare(p, gridColor);
        draw.pause(tDelete);
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

    /// Draws two distinct nodes as 'A' for Start and 'B' for Finish. Returns false if unable to place.
    public void drawEndpoint(GridPoint p, boolean isStart){
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
    }


    // ----- Simulation  shapes
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

    private void placeText(GridPoint p, String msg){
        if(p.x() < ticks / 2){ // left justify
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

        for (int x = 0; x < ticks; x++) {
            draw.setPenRadius((x % 5 == 0) ? 0.002 : 0.0005);
            draw.line(x, 0.0, x, ticks);
        }
        for (int y = 0; y < ticks; y++) {
            draw.setPenRadius((y % 5 == 0) ? 0.002 : 0.0005);
            draw.line(0.0, y, ticks, y);
        }
        draw.setPenColor(Draw.BLACK);
    }
    /// Draws grid in 5-square blocks, dark-gray background
    public void grid(GridPoint[] walls){
        draw.clear(Color.DARK_GRAY);
        draw.setPenColor(Color.LIGHT_GRAY);

        for (int x = 0; x < ticks; x++) {
            draw.setPenRadius((x % 5 == 0) ? 0.002 : 0.0005);
            draw.line(x, 0.0, x, ticks);
        }
        for (int y = 0; y < ticks; y++) {
            draw.setPenRadius((y % 5 == 0) ? 0.002 : 0.0005);
            draw.line(0.0, y, ticks, y);
        }
        draw.setPenColor(Draw.BLACK);
        for(GridPoint w : walls){ drawWall(w); }
    }

    /// Displays a title message in the top center of the grid
     public void showMessage(String msg){
        draw.show();
        draw.setPenColor(Color.BLACK);
        draw.filledRectangle(ticks / 2.0 , ticks - 2 , 2, 1);

        draw.setPenColor(Color.LIGHT_GRAY);
        draw.filledRectangle(ticks / 2.0 , ticks - 2 , 2, 1);
        draw.setPenColor();
        draw.text(ticks / 2.0, ticks -2 , msg);
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



