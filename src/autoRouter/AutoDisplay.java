package autoRouter;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.BorderLayout;

public class AutoDisplay extends JFrame {

	private static final long serialVersionUID = 1L;
	static final int width = 670; static final int height = 1000;
	static final double unit = 30.0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AutoDisplay frame = new AutoDisplay();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AutoDisplay() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, width, height);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		GridPanel panel_1 = new GridPanel(width,height,unit);
		getContentPane().add(panel_1, BorderLayout.CENTER);
		
	}
	
	public void grid(){
        grid(width, height, unit);
    }
    /// Draw a grid, 20x20
	private static void grid(int width, int height, double unit){
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
	       for (int x = 0; x < width; x ++){
	    	   StdDraw.setPenRadius((x % 5 == 0) ? 0.002 : 0.0005);
	           double xTick = x * unit;
	           StdDraw.line(xTick, 0.0,xTick,1.0);
	       }
	       for (int y = 0; y < height; y ++){
	    	   StdDraw.setPenRadius((y % 5 == 0) ? 0.002 : 0.0005);
	           double yTick = y * unit;
	           StdDraw.line(0.0, yTick,1.0,yTick);
	       }
	       StdDraw.setPenColor(StdDraw.BLACK);
	   }
	


}
