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
    //    private int height;
    private int tPause = 50;     // pause time between draws todo speed of animation

    private static double THICK_PEN_RADIUS = 0.002 * 10;
    public JFrame mainFrame;
    public GridDraw(int ticks, JFrame frame) {
        mainFrame = frame;
        this.ticks = ticks;
        //        height = width;
        grid = new Grid(ticks);

        this.draw = new Draw();
//        this.tPause = tPause;
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
        fillSquare(p, gridColor);
        fillSquare(p, backgroundColor);

        // redraw the grid with correct weights
        draw.setPenColor(gridColor);
        double gridlineX = ( p.x() % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineX);

        draw.line(  p.x()  -1.0 ,   p.y() -1.0,
                    p.x()  -1.0 ,   p.y() );

        gridlineX = ( (p.x() + 1.0) % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineX);

        draw.line(  p.x() ,  p.y() -1.0,
                    p.x() ,  p.y() );
        draw.setPenColor(gridColor);
        double gridlineY = ( (p.y()) % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius( gridlineY);

        draw.line(  p.x() -1.0  , p.y() -1.0,
                    p.x()       , p.y() -1.0 );
        gridlineY = ( (p.y() + 1.0) % 5 == 0 ? 0.002 : 0.0005);
        draw.setPenRadius(gridlineY);

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
        draw.setPenColor(color);
        draw.setPenRadius(THICK_PEN_RADIUS);
        draw.line(p.x() + shift, p.y() + shift, q.x() + shift, q.y() + shift );

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

}