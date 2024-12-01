package grid;
import edu.princeton.cs.algs4.*;

import javax.swing.*;
import java.awt.*;

/// Wraps `algs4.Draw` shape painting methods for painting on a 2D grid.
public class GridDraw {
    private static final double shift =  -0.5;  // shift placement of shapes
    private Draw draw;
    private Grid grid;
    private final Color backgroundColor = Color.DARK_GRAY;
    private final Color gridColor = Color.LIGHT_GRAY;

    private int ticks;
    private int width;
//    private int height;
    private int tPause = 5;     // pause time between draws todo speed of animation

    private static double DEFAULT_PEN_RADIUS = 0.002;
    private static double THICK_PEN_RADIUS = 0.002 * 10;
    public JFrame mainFrame;
    public GridDraw(int ticks, JFrame frame) {
        mainFrame = frame;
        this.ticks = ticks;
        width = ticks;
//        height = width;
        grid = new Grid(ticks);

        this.draw = new Draw();
        draw.enableDoubleBuffering();
        draw.setXscale(0,ticks);
        draw.setYscale(0,ticks);
    }

    /// Returns the parent JFrame
    public JFrame getFrame(){
        return mainFrame;
    }
    public Grid getGrid(){
        return grid;
    }
    public void setPause(int msTime){
        tPause = msTime;
    }
    public int getPause(){
        return tPause;
    }
    public int getTicks(){
        return ticks;
    }
    public Draw getDraw() {
        return draw;
    }
    public JLabel getDrawLabel () {  return draw.getJLabel(); }
    //@formatter:off
//
//    public static void main(String[] args)
//    {
//        JFrame frame = new JFrame();
//        JPanel mainPanel = new JPanel(new FlowLayout(), true); // creates JPanel, sets DoubleBuffering and sets to opaque
//        GridDraw gridDraw = new GridDraw(15, frame );
//        Draw draw = gridDraw.draw;
//        mainPanel.add(draw.getJLabel());
//        frame.setContentPane(mainPanel);
//        gridDraw.drawEmptyGrid();
//        draw.show();
//        draw.getJLabel().repaint();
//
//        frame.repaint();
//
//        gridDraw.showMessage("Title Message");
//
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
//        for( GridPoint p : known ){   gridDraw.discovered(p);  }
//        GridPoint[] path = new GridPoint[]{
//                new GridPoint ( 5 , 5 ),
//                new GridPoint ( 4 , 5 ),
//                new GridPoint ( 3 , 5 ),
//                new GridPoint ( 3 , 6 ),
//        };
//        for(int i = 1; i < path.length ; i ++){
//            gridDraw.path(path[i - 1], path[i], Color.RED.darker());
//            draw.pause(gridDraw.tPause);
//            draw.show();   draw.getJLabel().repaint();
//
//        }
//        for( GridPoint p : wall  ){   gridDraw.drawWall(p);   }
//
//        gridDraw.drawEndpoint( new GridPoint (1,1));
//        gridDraw.drawEndpoint( new GridPoint (9,9));
//        draw.pause(gridDraw.tPause);
//        draw.show();  draw.getJLabel().repaint();
//
//
//
//        for(int i = 0; i < 5; i++){
//             gridDraw.eraseSquare(wall[i]);
//             draw.show();
//             draw.getJLabel().repaint(); draw.pause(gridDraw.tPause);
//         }
//
//        gridDraw.drawEndpoint( new GridPoint (1,1));
//        gridDraw.drawEndpoint( new GridPoint (9,9));
// //        gridDraw.placeText(new GridPoint(1,5), "Left");
// //        gridDraw.placeText(new GridPoint(9,5), "Right");
//        draw.show();
//    }

    /// Draws a black square for a wall
    public void drawWall(GridPoint p){
        draw.setPenRadius();
        draw.setPenColor();
        fillSquare(p, draw.getPenColor());
//        draw.show();
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
//        draw.pause(tDelete);
        fillSquare(p, gridColor);
//        draw.pause(tDelete);
        fillSquare(p, backgroundColor);

        // redraw the grid with correct weights
        draw.setPenColor(gridColor);
        double gridlineX = ( p.x() % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineX);

//        draw.setPenRadius(); draw.setPenColor(Color.RED);
        draw.line(  p.x()  -1.0 ,   p.y() -1.0,
                    p.x()  -1.0 ,   p.y() );

//        draw.show();
        gridlineX = ( (p.x() + 1.0) % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineX);

//        draw.setPenRadius(); draw.setPenColor(Color.BLUE);

        draw.line(  p.x() ,  p.y() -1.0,
                    p.x() ,  p.y() );
//        draw.show();
        draw.setPenColor(gridColor);
        double gridlineY = ( (p.y()) % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius( gridlineY);

//        draw.setPenRadius(); draw.setPenColor(Color.GREEN);

        draw.line(  p.x() -1.0  , p.y() -1.0,
                    p.x()       , p.y() -1.0 );
//        draw.show();
        gridlineY = ( (p.y() + 1.0) % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineY);
//         draw.setPenRadius(); draw.setPenColor(Color.YELLOW);

        draw.line(  p.x()  -1.0 , p.y() ,
                    p.x()       , p.y() );

        draw.setPenRadius();
        draw.show();
    }

    /// Draws two distinct nodes as 'A' for Start and 'B' for Finish. Returns false if unable to place.
    public void drawEndpoint(GridPoint p){
        boolean isStart = grid.recentGridEndpoint() == 1;   // should we draw start?
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
    /// GridDraws the nodes visited as light, translucent overlay.
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

    /// GridDraws the path taken
    public void path(GridPoint p, GridPoint q, Color color){
//        draw.setPenColor(alphaColor(color.darker(),128));
        draw.setPenColor(color);
        draw.setPenRadius(THICK_PEN_RADIUS);
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
    public void drawEmptyGrid(){
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
    public void drawEmptyGrid(GridPoint[] walls){
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

    /// GridDraws a title message in the top center of the grid
     public void showMessage(String msg){
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



