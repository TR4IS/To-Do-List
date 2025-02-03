import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Greetings {
    public static void main(String[] args) {

        JFrame f1 = new JFrame();
        f1.getContentPane().setBackground(Color.BLACK);
        f1.setUndecorated(true);
        f1.setVisible(true);
        f1.setBounds(100,100,100,100);
        f1.setSize(300,300);
        f1.setLayout(null);

        // creating a label
        JLabel l1 = new JLabel("To-Do-List :");
        l1.setBounds(5,1,150,50);
        f1.add(l1);

        JButton b1 = new JButton("Add");
        b1.setBounds(200,10,80,30);
        b1.setForeground(Color.BLACK);
        b1.setBackground(Color.red);
        b1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        f1.add(b1);

        JTextArea ta1 = new JTextArea();
        ta1.setEditable(true);
        ta1.setBackground(Color.BLACK);
        ta1.setForeground(Color.RED);
        // ta1.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        ta1.setBounds(20,100,200,100);
        f1.add(ta1);
        try {
            // download the font from the font file
            File fontFile = new File("Fonts\\IkiMono-Regular.otf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            customFont = customFont.deriveFont(Font.TRUETYPE_FONT, 15);
            // changing the font of the label
            l1.setFont(customFont);
            l1.setForeground(Color.red);
            b1.setFont(customFont);
            ta1.setFont(customFont);


        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("error");
        }





        f1.addMouseListener(new MouseAdapter() {
            final int[] x = new int[1]; 
            final int[] y = new int[1];

            public void mousePressed(MouseEvent e) {
                x[0] = e.getX();
                y[0] = e.getY();

                System.out.println(x[0]+" "+y[0]);
                System.out.println(f1.getX()+ " "+f1.getY());

                f1.addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent e) {
                        
                        SwingUtilities.invokeLater(() -> {
                            f1.setLocation(f1.getX() + e.getX() - x[0], f1.getY() + e.getY() - y[0]);
                        });
                    }
                });
            }

            public void mouseReleased(MouseEvent e) {
                for (MouseMotionListener listener : f1.getMouseMotionListeners()) {
                    f1.removeMouseMotionListener(listener);
                }
            }
        });

        b1.addActionListener(e -> ta1.append("to do \n"));
    }
}
