package refSwing;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/// Animation using Thread.Sleep see [repaint in a loop](https://stackoverflow.com/questions/4120528/repaint-in-a-loop)
/// All GUI events are supposed to occur on the EDT, but multiple calls to `repaint`
/// are collapsed in the same thread. So placing a pause in a loop between paint calls is not enough when it is run on
/// the EDT.
/// @see javax.swing.RepaintManager#addDirtyRegion  [javax.swing.JComponent#paintImmediately]
///   older public api (but shouldnt be used) sun.swing.SwingUtilities2#RepaintListener [javax.swing.RepaintManager#addRepaintListener]
/// See also [github swing tasks examples ](https://github.com/javagl/SwingTasks/tree/master/src/test/java/de/javagl/swing/tasks/samples)
public class SwingThreadAnim {
    static final boolean []b = {false};
    static boolean a = false;
    JFrame frame = new JFrame();
    JPanel pnl = new JPanel();
    final JButton[] buttons = new JButton[10];
    SwingThreadAnim(){
        Component add = frame.add(pnl);
        frame.setContentPane(pnl);
//        frame.setContentPane(pnl);
        for(int i =0 ; i < 10; i++){
            buttons[i] = new  JButton(String.format("%d", i));
            frame.getContentPane().add(buttons[i]);
        }
        frame.pack();
        frame.getContentPane().setVisible(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Timer swingTimer = new Timer(100, ae->{

        });

        buttons[1].addActionListener(e->{
            b[0] = true;
            a = true;
            System.out.println(b[0]);
            uiFunction();
        });
//        buttons[0].addActionListener(e->{
//            uiFunction();
//        });
//        while(true){
//            if(a||b[0]){
//                uiFunction();
//                buttons[0].doClick();
//
//            }
//        }

    }

    void uiFunction() {
       new Thread(() -> {
           for(JButton b: buttons) {
               SwingUtilities.invokeLater(() -> {
                           b.setBackground(Color.WHITE);
                           b.paintImmediately(b.getBounds());
                       });
               try {
                   Thread.sleep(200);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               SwingUtilities.invokeLater( () -> {
                       b.setBackground(Color.GRAY);
                       b.paintImmediately(b.getBounds());
               });
           }
       }).start();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{  // if this is uncommented it will collapse the draws into one call
            SwingThreadAnim pnl = new SwingThreadAnim();
            Thread.dumpStack();
        });
    }
}
