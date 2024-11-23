package autoRouter;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.princeton.cs.algs4.Queue;
import java.awt.BorderLayout;

public class AutoDisplay extends JFrame {

	private static final long serialVersionUID = 1L;
	static int width = 20; static int height = 20; static int scale = 40;

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
		setResizable(false);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		GridPanel panel_1 = new GridPanel(width, height, scale);
		getContentPane().add(panel_1, BorderLayout.CENTER);
		pack(); // sets the size dynamically based on how big the graph is
		//everything past here is just testing feel free to remove everything past here
		Point point =  new Point(2, 1);
		Point point2 = new Point(3, 1);
		Point point3 = new Point(10, 3);
		Queue<Point> q = new Queue<>(); // can use anything Iterable here
		q.enqueue(point);
		q.enqueue(point2); 
		q.enqueue(point3);
		panel_1.addCircle(q); // this also works with Point
		
		
		
	}
	
}
