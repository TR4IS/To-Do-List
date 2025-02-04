import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;



public class Greetings {
    private static final String LAST = "last_location.txt";
    static ImageIcon icon = new ImageIcon("Images\\31412.jpg");

    public static void main(String[] args) {


        JFrame f1 = new JFrame();
        f1.setUndecorated(true);
        f1.setIconImage(icon.getImage());
        f1.setSize(300, 300);


        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);
        f1.setContentPane(panel);


        JLabel l1 = new JLabel("To-Do-List :");
        l1.setBounds(8, 1, 150, 50);
        f1.add(l1);

        JCheckBox c1 = new JCheckBox();
        c1.setBounds(80, 50, 150, 50);
        f1.add(c1);


        JButton closeButton = new JButton("X");

        closeButton.addActionListener(e -> System.exit(0));

        closeButton.setFocusPainted(false);
        closeButton.setForeground(Color.BLACK);
        closeButton.setBackground(Color.LIGHT_GRAY);
        closeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        closeButton.setBounds(278,2,20,30);
        f1.add(closeButton);

        JButton sB = new JButton("Save");
        sB.setFocusPainted(false);
        sB.setBounds(233,2,45,30);
        sB.setForeground(Color.BLACK);
        sB.setBackground(Color.LIGHT_GRAY);
        sB.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        f1.add(sB);

        JButton AlwaysOnTop = new JButton("Not Top");
        AlwaysOnTop.setFocusPainted(false);
        AlwaysOnTop.setBounds(186,2,47,30);
        AlwaysOnTop.setForeground(Color.BLACK);
        AlwaysOnTop.setBackground(Color.LIGHT_GRAY);
        AlwaysOnTop.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        f1.add(AlwaysOnTop);

        JTextPane tp = new JTextPane();
        tp.setBackground(Color.BLACK);
        tp.setForeground(Color.LIGHT_GRAY);
        tp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tp.setEditable(true);
        tp.setBounds(20,60,200,210);
        f1.add(tp);

        try {
            // download the font from the font file
            File fontFile = new File("Fonts\\IkiMono-Regular.otf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            customFont = customFont.deriveFont(Font.TRUETYPE_FONT, 15);
            // changing the font of the label
            l1.setFont(customFont);
            l1.setForeground(Color.LIGHT_GRAY);
            //b1.setFont(customFont);
            tp.setFont(customFont);
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("error");
        }

        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ✅ استرجاع الموقع من الملف عند بدء التشغيل
        Point lastLocation = loadWindowPosition();
        if (lastLocation != null) {
            f1.setLocation(lastLocation);
        } else {
            f1.setLocationRelativeTo(null); // إذا لم يكن هناك موقع محفوظ، افتح في المنتصف
        }

        // ✅ حفظ الموقع عند إغلاق النافذة
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveWindowPosition(f1);
            }
        });






        AlwaysOnTop.addActionListener(new ActionListener() {
            private boolean isAlwaysOnTop = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                isAlwaysOnTop = !isAlwaysOnTop;
                SwingUtilities.invokeLater(()-> {
                    f1.setAlwaysOnTop(false);
                    f1.setAlwaysOnTop(isAlwaysOnTop);
                });
                if (isAlwaysOnTop == false) {
                    AlwaysOnTop.setBackground(Color.LIGHT_GRAY);
                    AlwaysOnTop.setText("Not Top");
                }
                else {
                    AlwaysOnTop.setBackground(Color.DARK_GRAY);
                    AlwaysOnTop.setText("On Top");
                }


            }
        });

        f1.addMouseListener(new MouseAdapter() {
            // جعل المتغيرات final
            final int[] x = new int[1]; // استخدام array لجعل المتغير قابل للتعديل ولكن ما زال final
            final int[] y = new int[1];

            public void mousePressed(MouseEvent e) {
                // حفظ مكان الضغط
                x[0] = e.getX();
                y[0] = e.getY();

                System.out.println(x[0]+" "+y[0]);
                System.out.println(f1.getX()+ " "+f1.getY());

                // إضافة مستمع الحركة عند الضغط
                f1.addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent e) {
                        // تحريك النافذة أثناء السحب
                        SwingUtilities.invokeLater(() -> {
                            f1.setLocation(f1.getX() + e.getX() - x[0], f1.getY() + e.getY() - y[0]);
                        });
                    }
                });
            }
            public void mouseReleased(MouseEvent e) {
                // إزالة مستمع الحركة عند رفع الفأرة
                for (MouseMotionListener listener : f1.getMouseMotionListeners()) {
                    f1.removeMouseMotionListener(listener);
                }
            }
        });

        sB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SaveTextToFile(tp.getText());
            }
        });

        loadTextFromFile(tp);


        // always at the end
        f1.setVisible(true);

    }

    private static void SaveTextToFile(String text) {
        try{
            File SaveFile = new File("saved_text");
            BufferedWriter writer = new BufferedWriter(new FileWriter(SaveFile));
            writer.write(text);
            writer.close();
            //JOptionPane.showMessageDialog(null, "Saved"+ SaveFile.getAbsolutePath());
        }
        catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error");
        }
    }
    private static void loadTextFromFile(JTextPane textArea) {
        File file = new File("saved_text");
        if (file.exists()) { // يتحقق مما إذا كان الملف موجودًا
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null); // تحميل النص داخل JTextArea
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "حدث خطأ أثناء التحميل!", "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private static Point loadWindowPosition() {
        try (Scanner scanner = new Scanner(new File(LAST))) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            return new Point(x, y);
        } catch (Exception e) {
            return null; // إذا لم يتم العثور على ملف أو حدث خطأ، نعيد null
        }
    }

    // 🔹 حفظ موقع النافذة في الملف عند الإغلاق
    private static void saveWindowPosition(Frame frame) {
        try  {
            File lastLocation = new File("last_location.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter (lastLocation));
            System.out.println(lastLocation.getAbsolutePath());
            System.out.println("hi");
            writer.write(frame.getX() + " " + frame.getY());
            writer.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }







}
