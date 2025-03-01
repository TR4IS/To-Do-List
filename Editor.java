import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class Editor {
    private static final String LAST = "last_location.txt";
    static ImageIcon icon = new ImageIcon("Images\\iconX.png");
    private static final int RESIZE_MARGIN = 10; // الهامش لتغيير الحجم

    public static void main(String[] args) {
        JFrame f1 = new JFrame();
        f1.setUndecorated(true);
        f1.setIconImage(icon.getImage());
        f1.setSize(300, 300);
        f1.getRootPane().setBorder(BorderFactory.createEmptyBorder());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        panel.setBackground(Color.BLACK);

        JPanel BPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        BPanel.setBackground(Color.BLACK);
        BPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        JPanel PPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        PPanel.setBackground(Color.BLACK);
        PPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        panel.add(BPanel, BorderLayout.NORTH);
        panel.add(PPanel, BorderLayout.CENTER);

        f1.setContentPane(panel);

        JLabel l1 = new JLabel("Editor X");
        BPanel.add(l1);

        JButton closeButton = new JButton("X");
        closeButton.addActionListener(e -> {
            saveWindowPosition(f1);
            System.exit(0);
        });
        closeButton.setFocusPainted(false);
        closeButton.setForeground(Color.BLACK);
        closeButton.setBackground(Color.LIGHT_GRAY);
        closeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        BPanel.add(closeButton);

        JButton sB = new JButton("Save");
        BPanel.add(sB);

        JButton AlwaysOnTop = new JButton("On Top");
        BPanel.add(AlwaysOnTop);

        JButton openButton = new JButton("Open");
        BPanel.add(openButton);

        JTextPane tp = new JTextPane();
        tp.setBackground(Color.BLACK);
        tp.setForeground(Color.LIGHT_GRAY);
        tp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tp.setEditable(true);

        JScrollPane scrollPane = new JScrollPane(tp);
        panel.add(scrollPane);

        try {
            File fontFile = new File("Fonts\\IkiMono-Regular.otf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(15f);
            Font customFont2 = customFont.deriveFont(12f);

            tp.setFont(customFont);
            l1.setFont(customFont);
            l1.setForeground(Color.LIGHT_GRAY);
            sB.setFont(customFont2);
            closeButton.setFont(customFont2);
            AlwaysOnTop.setFont(customFont2);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // استرجاع الموقع من الملف عند بدء التشغيل
        Point lastLocation = loadWindowPosition();
        if (lastLocation != null) {
            f1.setLocation(lastLocation);
        } else {
            f1.setLocationRelativeTo(null);
        }

        AlwaysOnTop.addActionListener(new ActionListener() {
            private boolean isAlwaysOnTop = false;

            public void actionPerformed(ActionEvent e) {
                isAlwaysOnTop = !isAlwaysOnTop;
                f1.setAlwaysOnTop(isAlwaysOnTop);
                AlwaysOnTop.setBackground(isAlwaysOnTop ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            }
        });

        // تحريك النافذة عند السحب
        f1.addMouseListener(new MouseAdapter() {
            int x, y;

            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        f1.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                f1.setLocation(f1.getX() + e.getX() - x, f1.getY() + e.getY() - y);
            }
        });

        // تغيير حجم النافذة
        f1.addMouseListener(new MouseAdapter() {
            int mouseX, mouseY;
            boolean resizing = false;

            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                resizing = isInResizeArea(f1, mouseX, mouseY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                resizing = false;
            }
        });

        f1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isInResizeArea(f1, mouseX, mouseY)) return;

                int newWidth = f1.getWidth();
                int newHeight = f1.getHeight();

                if (mouseX >= f1.getWidth() - RESIZE_MARGIN) {
                    newWidth = e.getX();
                }
                if (mouseY >= f1.getHeight() - RESIZE_MARGIN) {
                    newHeight = e.getY();
                }

                f1.setSize(newWidth, newHeight);
            }
        });

        sB.addActionListener(e -> SaveTextToFile(tp.getText()));
        openButton.addActionListener(e -> openFile(tp));

        loadTextFromFile(tp);
        f1.setVisible(true);
    }

    private static boolean isInResizeArea(JFrame frame, int x, int y) {
        return x >= frame.getWidth() - RESIZE_MARGIN || y >= frame.getHeight() - RESIZE_MARGIN;
    }

    private static void SaveTextToFile(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("saved_text"))) {
            writer.write(text);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving file");
        }
    }

    private static void loadTextFromFile(JTextPane textArea) {
        File file = new File("saved_text");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading file");
            }
        }
    }

    private static Point loadWindowPosition() {
        try (Scanner scanner = new Scanner(new File(LAST))) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            return new Point(x, y);
        } catch (Exception e) {
            return null;
        }
    }

    private static void saveWindowPosition(Frame frame) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("last_location.txt"))) {
            writer.write(frame.getX() + " " + frame.getY());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void openFile(JTextPane textPane) {
        FileDialog fileDialog = new FileDialog((Frame) null, "Select a text file", FileDialog.LOAD);
        fileDialog.setVisible(true);
        String directory = fileDialog.getDirectory();
        String filename = fileDialog.getFile();

        if (directory != null && filename != null) {
            File selectedFile = new File(directory, filename);
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                textPane.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error opening file");
            }
        }
    }
}
