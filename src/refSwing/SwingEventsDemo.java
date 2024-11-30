package refSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class SwingEventsDemo {

    private final static String password = "1234";


    SwingEventsDemo(){
        JFrame frame = new JFrame("Timer Pause");
        JPanel mainPanel = new JPanel();
        JTextField jtf = new JTextField(6);
        JButton btnEnter = new JButton("Enter");
        jtf.requestFocus();
        JLabel lbl = new JLabel();

        frame.setPreferredSize(new Dimension(300, 200));
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setContentPane(mainPanel);
        frame.getContentPane().add(lbl,BorderLayout.NORTH);
        frame.getContentPane().add(jtf,BorderLayout.CENTER);
        mainPanel.add(btnEnter,BorderLayout.SOUTH);

        btnEnter.addActionListener(e ->{
            if(jtf.getText().equals(password)) {
                frame.getContentPane().setBackground(Color.green);
                lbl.setText("Welcome");

            } else {
                frame.getContentPane().setBackground(Color.red);
                lbl.setText("Access Denied");
            }
        });
        /// Textfield keyevent listener
        jtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
//                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) btnEnter.doClick();

            }
        });

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().setBackground(Color.cyan);
                lbl.setText("Enter your password");
            }
        });
        timer.setRepeats(false);
        timer.start();
        frame.pack();
        frame.getContentPane().setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    public static SwingEventsDemo buildFrame(){
       return new SwingEventsDemo();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            buildFrame();
        });

    }

}
