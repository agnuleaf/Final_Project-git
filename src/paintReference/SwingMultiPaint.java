package paintReference;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/// THIS IS COPIED FROM stackoverflow
public class SwingMultiPaint extends JPanel {


    @SuppressWarnings("serial")
        private static final int PREF_W = 800;
        private static final int PREF_H = 600;
        private static final Color BACKGROUND = Color.WHITE;
        private List<SwingMultiPaint_Draw> draws = new ArrayList<>();

        public SwingMultiPaint() {
            setPreferredSize(new Dimension(PREF_W, PREF_H));
            setBackground(BACKGROUND);
        }

        public void addDraw(SwingMultiPaint_Draw draw) {
            draws.add(draw);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (SwingMultiPaint_Draw draw : draws) {
                draw.myPaint(g);
            }
        }

        private static void createAndShowGui() {
            SwingMultiPaint guiFun = new SwingMultiPaint();
            guiFun.addDraw(new SwingMultiPaint_Draw(3, 3, 1, 20));
            guiFun.addDraw(new SwingMultiPaint_Draw(0, 0, 5, 4));

            JFrame frame = new JFrame("GuiFun");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(guiFun);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> createAndShowGui());
        }
}


