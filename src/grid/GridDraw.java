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
    private double gridThickness = 1.0;
    private int squares;
    //    private int height;
    private int tPause = 10;
    private  JFrame mainFrame;

    public GridDraw(int squares, JFrame frame) {
        mainFrame = frame;
        this.squares = squares;
        //        height = width;
        grid = new Grid(squares);

        this.draw = new Draw();
//        this.tPause = tPause;
        draw.enableDoubleBuffering();
        draw.setXscale(0,squares);
        draw.setYscale(0,squares);
    }

    /// Returns the parent JFrame, used for calls to `repaint`.
    /// @return the main JFrame
    public JFrame getFrame(){
        return mainFrame;
    }
    /// Gets the instance of the `Grid`, useful for converting between [Graph] vertices and `GridPoint`s.
    /// @return the grid object
    public Grid getGrid(){
        return grid;
    }
    /// Sets the time delay between animation updates in milliseconds. Used by the [Draw] timer.
    /// @param msTime - the time in ms to pause between animation frames.
    public void setPause(int msTime){
        tPause = msTime;
    }
    /// The time delay between animation updates in milliseconds.
    /// @return time between draw calls
    public int getPause(){
        return tPause;
    }
    /// Returns number of squares per side
    public int getSquares(){
        return squares;
    }
    /// Returns the `Draw` instance
    /// @return `Draw` instance
    public Draw getDraw() {
        return draw;
    }
    ///  Returns the `Draw` instance's `JLabel` for including in another `JFrame`.
    /// @return the JLabel for the `Draw` instance
    public JLabel getDrawLabel () {  return draw.getJLabel(); }

    /// Draws a black square for a wall
    public void drawWall(GridPoint p){
        draw.setPenRadius();
        draw.setPenColor();
        fillSquare(p, draw.getPenColor());
    }
    /// Makes the grid more or less visible.
    /// @param multiplier - multiplies the drawing pen thickness for the gridlines.
    public void setGridThickness(int multiplier){
        gridThickness = multiplier;
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

    /// Visually erases a single grid square centered at p, by painting the grid background over it.
    /// @param p - the `GridPoint` whose contents will be erased. `GridPoint` can be thought out as the center
    ///  of the square.
    public void eraseSquare( GridPoint p ){

        fillSquare(p, backgroundColor);
        draw.setPenColor(gridColor);

        double gridlineX = ( (p.x() - 1) % 5 == 0 ? 0.002 : 0.0005) * gridThickness;
        draw.setPenRadius(gridlineX);
        draw.line(  p.x()  -1.0 ,   p.y() -1.0,
                p.x()  -1.0 ,   p.y() );

        gridlineX  = ( (p.x() ) % 5 == 0 ? 0.002 : 0.0005)  * gridThickness;
        draw.setPenRadius(gridlineX);
        draw.line(  p.x() ,  p.y() - 1.0,
                p.x() ,  p.y() );

        double gridlineY = ( (p.y() - 1) % 5 == 0 ? 0.002 : 0.0005) * gridThickness;
        draw.setPenRadius( gridlineY);

        draw.line(  p.x() -1.0  , p.y() -1.0,
                p.x()       , p.y() -1.0 );
        gridlineY = ( (p.y() ) % 5 == 0 ? 0.002 : 0.0005) * gridThickness;
        draw.setPenRadius(gridlineY);

        draw.line(  p.x()  -1.0 , p.y() ,
                p.x()       , p.y() );

        draw.setPenRadius();
        draw.show();
    }

    /// Draws two distinct nodes as 'A' for Start and 'B' for Finish. Returns false if unable to place.
    /// @param p - the location of the endpoint to draw
    public void drawEndpoint(GridPoint p){
        boolean isStart = grid.endpointsSize() == 1;   // should we draw start?
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

    /// During a search animation the visited squares are brightened with a translucent overlay.
    /// @param p - the `GridPoint` square to color
    /// @param color - the base color to color the square with a constant translucency.
    public void discovered(GridPoint p, Color color){
        draw.setPenRadius();
        draw.setPenColor(alphaColor(color, 80));
//        draw.setPenColor(new Color(255,155,175,80));// Color.PINK.);
        draw.setPenRadius();
        draw.filledSquare(
                p.x() + shift,
                p.y() + shift,
                0.5);
        draw.setPenRadius();
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
    /// GridDraws the path taken
    /// @param p - the 'from' `GridPoint`
    /// @param q - the 'to' `GridPoint`
    /// @param color - the color of the path
    public void path(GridPoint p, GridPoint q, Color color){
        draw.setPenColor(color);
        double THICK_PEN_RADIUS = 0.002 * 10;
        draw.setPenRadius(THICK_PEN_RADIUS);
        draw.line(p.x() + shift, p.y() + shift, q.x() + shift, q.y() + shift );

    }

    /// GridDraws a title message in the top center of the grid
    /// @param msg - message to display
    public void showMessage(String msg){
        draw.setPenColor(Color.BLACK);
        draw.filledRectangle(squares / 2.0 , squares - 2 , 2, 1);

        draw.setPenColor(Color.LIGHT_GRAY);
        draw.filledRectangle(squares / 2.0 , squares - 2 , 2, 1);
        draw.setPenColor();
        draw.text(squares / 2.0, squares -2 , msg);
    }
    /// Draws a grid in 5-square blocks, with light-gray lines and dark-gray background
    public void drawEmptyGrid(){

        draw.clear(Color.DARK_GRAY);
        draw.setPenColor(Color.LIGHT_GRAY);

        for (int x = 0; x < squares; x++) {
            draw.setPenRadius(((x % 5 == 0) ? 0.002 : 0.0005) * gridThickness);
            draw.line(x, 0.0, x, squares);
        }
        for (int y = 0; y < squares; y++) {
            draw.setPenRadius(((y % 5 == 0) ? 0.002 : 0.0005) * gridThickness);
            draw.line(0.0, y, squares, y);
        }
        draw.setPenColor(Draw.BLACK);
    }
    /// Generates and draws random walls in the grid.
    /// @param density where 0 is empty and 1 means the generator was ran for at least as many times as the total number squares in the grid.
    public void generateRandomWalls(double density) {
        Grid grid = getGrid();
        int total = squares * squares;
        Iterable<GridPoint> points = grid.generateGridPoints((int) (total * density));
        for (GridPoint p : points) {
            if (grid.addWall(p)) {
                drawWall(p);
            }
        }
    }

}