package autoRouter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GridPanel extends JPanel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width, height;
    private double unit;
    
    public GridPanel(int width, int height, double unit) {
        this.width = width;
        this.height = height;
        this.unit = unit;
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawGrid(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
        // Set the grid line color to light gray
        g2d.setColor(Color.LIGHT_GRAY);

        // Calculate the number of vertical and horizontal lines needed
        int numVerticalLines = (int) Math.ceil(width / unit);
        int numHorizontalLines = (int) Math.ceil(height / unit);

        // Draw vertical lines
        for (int i = 0; i <= numVerticalLines; i++) {
            int xTick = (int) (i * unit);
            g2d.setStroke(new BasicStroke((i % 5 == 0) ? 2f : 0.5f));
            g2d.drawLine(xTick, 0, xTick, height);
        }

        // Draw horizontal lines
        for (int j = 0; j <= numHorizontalLines; j++) {
            int yTick = (int) (j * unit);
            g2d.setStroke(new BasicStroke((j % 5 == 0) ? 2f : 0.5f));
            g2d.drawLine(0, yTick, width, yTick); 
        }
        g2d.setColor(Color.BLACK); //reset color
    }
}