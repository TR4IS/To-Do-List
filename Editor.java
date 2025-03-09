import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedWriter;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class Editor {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 450);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setLayout(new BorderLayout());
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        JPanel Toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        Toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY)); // Bottom border only
        Toolbar.setPreferredSize(new Dimension(frame.getWidth(), 35));
        Toolbar.setBackground(Color.BLACK);

        JTextPane textPane = new JTextPane();
        textPane.setBackground(Color.BLACK);
        textPane.setForeground(Color.WHITE);
        textPane.setEditable(true);
        textPane.setFont(getFont("To-Do-List/Fonts/FiraCode-VF.ttf", 20));
        textPane.setText(readFile("To-Do-List/config/output.txt"));
        textPane.setOpaque(true);

        JButton closeButton = new JButton("X");
        closeButton.setFocusable(false);
        closeButton.setOpaque(true);
        closeButton.setPreferredSize(new Dimension(50, 25));
        closeButton.setBackground(Color.BLACK);
        closeButton.setForeground(Color.WHITE);

        closeButton.addActionListener(e -> {
            saveTextToFile(textPane.getText(), "To-Do-List/config/output.txt");
            frame.dispose();
        });

        JButton topButton = new JButton("not Top");
        topButton.setFocusable(false);
        topButton.setOpaque(true);
        topButton.setPreferredSize(new Dimension(80, 25));
        topButton.setBackground(Color.BLACK);
        topButton.setForeground(Color.WHITE);

        topButton.addActionListener(e -> {
            boolean state = frame.isAlwaysOnTop();
            frame.setAlwaysOnTop(!state);
            if (state) {
                topButton.setText("not Top");
            } else {
                topButton.setText("Top");
            }
        });

        MouseAdapter DragListener = new MouseAdapter() {
            private Point initialPoint;

            public void mousePressed(MouseEvent e) {
                initialPoint = e.getPoint();
            }

            public void mouseDragged(MouseEvent e) {
                Point point = e.getLocationOnScreen();
                frame.setLocation(point.x - initialPoint.x, point.y - initialPoint.y);
            }
        };

        Toolbar.add(topButton, BorderLayout.WEST);
        Toolbar.addMouseListener(DragListener);
        Toolbar.addMouseMotionListener(DragListener);
        frame.add(textPane, BorderLayout.CENTER);
        Toolbar.add(closeButton, BorderLayout.EAST);
        frame.add(Toolbar, BorderLayout.NORTH);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveTextToFile(textPane.getText(), "To-Do-List/config/output.txt");
            }
        });
        frame.setVisible(true);
    }

    public static Font getFont(String path, int size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            return font.deriveFont((float) size);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(String filename) {
        StringBuilder string = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                string.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string.toString();
    }

    private static void saveTextToFile(String text, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

