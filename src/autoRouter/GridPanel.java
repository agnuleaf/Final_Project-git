package autoRouter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public class GridPanel extends JPanel {
	
    /**
	 * @author Ty Greenburg
	 */
	private static final long serialVersionUID = 1L;
	private int width, height;
    private int scale;
    private Set<GridPoint> gridPoints = new HashSet<>(); // to insure that it doesn't add any points that are already on there
    
    /**
     * Initializes the object
     * @param width Number of cells you want on the x cords
     * @param height Number of cells you want on the y cords
     * @param scale How big you want the graph to be
     */
    public GridPanel(int width, int height, int scale) {
        this.width = width * scale; // how many units (multiple of 5) of the x cords
        this.height = height * scale; // how many units (multiple of 5) of the y cords
        this.scale = scale;
        setPreferredSize(new Dimension(this.width, this.height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawGrid(g2d);
        
        g2d.setColor(Color.ORANGE);
        // Iterates through the list and updates the display Dynamically
        for (GridPoint gridPoint : gridPoints) {
            int x = gridPoint.x() * scale;
            int y = gridPoint.y() * scale;
            g2d.fillOval(x-scale/4, y-scale/4, scale/2, scale/2); 
        }
        g2d.setColor(Color.BLACK); // resets color to black for future use
    }

    /**
     * Draws a grid based on parameters 
     * @param g2d graphic that will be drawn on
     */
    private void drawGrid(Graphics2D g2d) {
        // Set the grid line color to light gray
        g2d.setColor(Color.LIGHT_GRAY);

        // Draw vertical lines
        for (int x = 0; x <= width; x++) {
            int xTick = x * scale;
            g2d.setStroke(new BasicStroke((x % 5 == 0) ? 2f : 0.5f));
            g2d.drawLine(xTick, 0, xTick, height);
        }

        // Draw horizontal lines
        for (int y = 0; y <= height; y++) {
            int yTick = y * scale;
            g2d.setStroke(new BasicStroke((y % 5 == 0) ? 2f : 0.5f));
            g2d.drawLine(0, yTick, width, yTick); 
        }
        
        g2d.setColor(Color.BLACK); //reset color
    }
    /**
     * Method to add a new circle point
     * @param gridPoint to add
     */
    public void addCircle(GridPoint gridPoint) {
        gridPoints.add(gridPoint); // Note this wont do anything if it becomes out of bounds of the graph
        repaint(); // Trigger a repaint to reflect the added circle
    }

    /**
     * Method to remove circle point
     * @param gridPoint to remove
     */
    public void removeCircle(GridPoint gridPoint) {
        gridPoints.remove(gridPoint);
        repaint(); // Trigger a repaint to reflect the removed circle
    }

    /**
     * Method to remove all points
     */
    public void clearCircles() {
        gridPoints.clear();
        repaint(); // Trigger a repaint to clear circles
    }
    
    /**
     * Method to add multiple circles
     * @param points to add
     */
    public void addCircle(Iterable<GridPoint> points) {
    	points.forEach(point -> addCircle(point));
    }
    
    /**
     * Method to remove multiple circles
     * @param points to remove
     */
    public void removeCircle(Iterable<GridPoint> points) {
    	points.forEach(point -> removeCircle(point));
    }
    
    //TODO Add paths through some type of list
    
}
