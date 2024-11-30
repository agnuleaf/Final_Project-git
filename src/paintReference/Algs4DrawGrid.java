package paintReference;
import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.DrawListener;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.KeyEvent;

/// TAKEN FROM introcs princeton
///
public class Algs4DrawGrid implements DrawListener {

        private int pieces = 0;
        private Draw draw = new Draw();

        public Algs4DrawGrid(int n) {
            draw.addListener(this);

            draw.setXscale(0, n);
            draw.setYscale(0, n);

            // draw black grid lines with gray background
            draw.clear(Color.LIGHT_GRAY);
            draw.setPenColor(Color.BLACK);
            for (int i = 0; i <= n; i++) draw.line(i, 0, i, n);
            for (int j = 0; j <= n; j++) draw.line(0, j, n, j);
            draw.show();

            SwingUtilities.invokeLater(() -> {
                System.out.println("su");
                    if (draw.isKeyPressed(KeyEvent.VK_SPACE)) {
                        System.out.println("space");
                    }
                    if(draw.hasNextKeyTyped()){
                        System.out.println("has next key");
                    }


            });


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

