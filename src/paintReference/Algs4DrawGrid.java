package paintReference;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.DrawListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_SPACE;

/// TAKEN FROM introcs princeton
///
public class Algs4DrawGrid implements DrawListener {

        private int pieces = 0;
        private Draw draw = new Draw();
        private JTextField focus;
        public Algs4DrawGrid(int n) {
            draw.addListener(this);

            draw.setXscale(0, n);
            draw.setYscale(0, n);
//            focus = new JTextField();
//            JPanel pnl = new JPanel();
//            pnl.add(focus);
//            focus.setVisible(false);
//            pnl.add(draw.getJLabel());
//            JFrame frame = new JFrame();
//            frame.getContentPane().add(pnl);
//            frame.pack(); frame.setResizable(false);
//            frame.getContentPane().setVisible(true);
//            frame.setVisible(true);
//            frame.setLocationRelativeTo(null);

            // draw black grid lines with gray background
            draw.clear(Color.LIGHT_GRAY);
            draw.setPenColor(Color.BLACK);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//            frame.getContentPane().add(guiFun);
//            frame.pack();
//            frame.setResizable(false);
//            frame.setLocationRelativeTo(null);


            for (int i = 0; i <= n; i++) draw.line(i, 0, i, n);
            for (int j = 0; j <= n; j++) draw.line(0, j, n, j);
            draw.show();
//            frame.repaint();


            SwingUtilities.invokeLater(() -> {
                System.out.println("su");
                    if (draw.isKeyPressed(VK_SPACE)) {
                        System.out.println("space");
                    }
                    if(draw.hasNextKeyTyped()){
                        System.out.println("has next key");
                    }


            });

//            draw.keyTyped(new KeyAdapter()
//                if(e == VK_SPACE){
//                    System.out.println("keyTyped" + VK_SPACE);
//                }
//            });

// btnUndo.addKeyListener(new KeyAdapter() {
//        public void keyPressed (KeyEvent ke){
//            if (ke.getKeyCode() == KeyEvent.VK_D) {
//                if (grid.countWalls() > 0) {
//                    gridDraw.eraseSquare(grid.removeLastWall());
//                }
//            }
//        }
       }

        public void mousePressed(double x, double y) {
            if (pieces % 2 == 0) draw.setPenColor(Color.BLUE);
            else                 draw.setPenColor(Color.RED);
            draw.filledCircle(Math.floor(x) + 0.5, Math.floor(y) + 0.5, .375);
            pieces++;
            draw.show();
        }


        public void keyPressed(int keycode) {
            System.out.println(keycode);
        }

        // test client
        public static void main(String[] args) {
            int n = Integer.parseInt("20");
            new Algs4DrawGrid(n);
    }

}

