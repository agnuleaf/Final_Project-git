package old;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.princeton.cs.algs4.Queue;
import grid.GridPoint;

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
		JPanel Interactable = new JPanel(); // for the user to interact
		getContentPane().add(Interactable, BorderLayout.SOUTH);
		
		GridPanel Display = new GridPanel(width, height, scale); // displays the graph, points, and paths
		getContentPane().add(Display, BorderLayout.CENTER);
		pack(); // sets the size dynamically based on how big the graph is
		
		//everything past here is just testing feel free to remove everything past here
		GridPoint gridPoint =  new GridPoint(2, 1);
		GridPoint gridPoint2 = new GridPoint(3, 1);
		GridPoint gridPoint3 = new GridPoint(10, 3);
		Queue<GridPoint> q = new Queue<>(); // can use anything Iterable here
		q.enqueue(gridPoint);
		q.enqueue(gridPoint2);
		q.enqueue(gridPoint3);
		Display.addCircle(q); // this also works with Int2D point
		
		//TODO Add Interactable functionality
		//TODO Add class to calculate the best path
		
		
		
	}
	
}
