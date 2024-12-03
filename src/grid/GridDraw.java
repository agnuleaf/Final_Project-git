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
    private int tPause = 50;     // pause time between draws todo speed of animation

    private static double THICK_PEN_RADIUS = 0.002 * 10;
    public JFrame mainFrame;
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
    /// Returns number of squares per side
    public int getSquares(){
        return squares;
    }
    ///
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

    /// Visually erases a single grid square centered at p, by painting the grid background over it.
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


    // ----- Simulation  shapes
    /// GridDraws the nodes visited as light, translucent overlay.
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
    /// GridDraws a title message in the top center of the grid
    public void showMessage(String msg){
        draw.setPenColor(Color.BLACK);
        draw.filledRectangle(squares / 2.0 , squares - 2 , 2, 1);

        draw.setPenColor(Color.LIGHT_GRAY);
        draw.filledRectangle(squares / 2.0 , squares - 2 , 2, 1);
        draw.setPenColor();
        draw.text(squares / 2.0, squares -2 , msg);
    }
    /// Draws grid in 5-square blocks, dark-gray background
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