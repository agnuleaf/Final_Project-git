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

    /// Visually erases a single grid square centered at p, by painting the grid background over it.
    public void eraseSquare( GridPoint p ){
//        Color DEBUG_RED     = Color.RED     ;
//        Color DEBUG_BLUE    = Color.BLUE    ;
//        Color DEBUG_YELLOW  = Color.YELLOW  ;
//        Color DEBUG_GREEN   = Color.GREEN   ;
//        fillSquare(p,Color.LIGHT_GRAY.brighter());
//        fillSquare(p, gridColor);
        fillSquare(p, backgroundColor);
        draw.setPenColor(gridColor);

//        draw.setPenColor(DEBUG_RED);
        // redraw the grid with correct weights
        double gridlineX = ( (p.x() - 1) % 5 == 0 ? 0.002 : 0.0005) * gridThickness;
        draw.setPenRadius(gridlineX);
        draw.line(  p.x()  -1.0 ,   p.y() -1.0,
                p.x()  -1.0 ,   p.y() );

//        draw.setPenColor(DEBUG_GREEN);
        gridlineX  = ( (p.x() ) % 5 == 0 ? 0.002 : 0.0005)  * gridThickness;
        draw.setPenRadius(gridlineX);
        draw.line(  p.x() ,  p.y() - 1.0,
                p.x() ,  p.y() );

        double gridlineY = ( (p.y() - 1) % 5 == 0 ? 0.002 : 0.0005) * gridThickness;
        draw.setPenRadius( gridlineY);
//        draw.setPenColor(DEBUG_BLUE);

        draw.line(  p.x() -1.0  , p.y() -1.0,
                p.x()       , p.y() -1.0 );
        gridlineY = ( (p.y() ) % 5 == 0 ? 0.002 : 0.0005) * gridThickness;
        draw.setPenRadius(gridlineY);
//        draw.setPenColor(DEBUG_YELLOW);

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
    // Adds translucency as the alpha value to a color. higher alpha = high opacity.
    private Color alphaColor(Color color, byte alpha){
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
    /// GridDraws a title message in the top center of the grid
    public void showMessage(String msg){
        draw.setPenColor(Color.BLACK);
        draw.filledRectangle(ticks / 2.0 , ticks - 2 , 2, 1);

        draw.setPenColor(Color.LIGHT_GRAY);
        draw.filledRectangle(ticks / 2.0 , ticks - 2 , 2, 1);
        draw.setPenColor();
        draw.text(ticks / 2.0, ticks -2 , msg);
    }
    /// Draws grid in 5-square blocks, dark-gray background
    public void drawEmptyGrid(){
        draw.clear(Color.DARK_GRAY);
        draw.setPenColor(Color.LIGHT_GRAY);

        for (int x = 0; x < ticks; x++) {
            draw.setPenRadius(((x % 5 == 0) ? 0.002 : 0.0005) * gridThickness);
            draw.line(x, 0.0, x, ticks);
        }
        for (int y = 0; y < ticks; y++) {
            draw.setPenRadius(((y % 5 == 0) ? 0.002 : 0.0005) * gridThickness);
            draw.line(0.0, y, ticks, y);
        }
        draw.setPenColor(Draw.BLACK);
    }
    
    // @formatter:off

    /// For testing the graphics and drawing functions.
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel(new FlowLayout(), true); // creates JPanel, sets DoubleBuffering and sets to opaque
        GridDraw gridDraw = new GridDraw(15, frame );
        Draw draw = gridDraw.draw;
        mainPanel.add(draw.getJLabel());
        frame.setContentPane(mainPanel);
        gridDraw.drawEmptyGrid();
        draw.show();
        draw.getJLabel().repaint();

        frame.repaint();

        gridDraw.showMessage("Title Message");

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
        for( GridPoint p : known ){   gridDraw.discovered(p);  }
        GridPoint[] path = new GridPoint[]{
                new GridPoint ( 5 , 5 ),
                new GridPoint ( 4 , 5 ),
                new GridPoint ( 3 , 5 ),
                new GridPoint ( 3 , 6 ),
        };
        for(int i = 1; i < path.length ; i ++){
            gridDraw.path(path[i - 1], path[i], Color.RED.darker());
            draw.pause(gridDraw.tPause);
            draw.show();   draw.getJLabel().repaint();

        }
        for( GridPoint p : wall  ){   gridDraw.drawWall(p);   }

        gridDraw.drawEndpoint( new GridPoint (1,1));
        gridDraw.drawEndpoint( new GridPoint (9,9));
        draw.pause(gridDraw.tPause);
        draw.show();  draw.getJLabel().repaint();



        for(int i = 0; i < 5; i++){
             gridDraw.eraseSquare(wall[i]);
             draw.show();
             draw.getJLabel().repaint(); draw.pause(gridDraw.tPause);
         }

        gridDraw.drawEndpoint( new GridPoint (1,1));
        gridDraw.drawEndpoint( new GridPoint (9,9));
 //        gridDraw.placeText(new GridPoint(1,5), "Left");
 //        gridDraw.placeText(new GridPoint(9,5), "Right");
        draw.show();
    }

}