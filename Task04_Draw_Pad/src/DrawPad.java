
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DrawPad extends JFrame {

    private final ArrayList<Point> points = new ArrayList<>();
    private Color drawingColor = Color.BLACK;
    private int brushSize = 5;

    public DrawPad() {
        setTitle("Campus Draw Pad");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel toolbar = new JPanel();

        JButton blackButton = new JButton("Black");
        JButton redButton = new JButton("Red");
        JButton blueButton = new JButton("Blue");
        JButton greenButton = new JButton("Green");
        JButton clearButton = new JButton("Clear");

        String[] sizes = {"3", "5", "8", "12"};
        JComboBox<String> sizeBox = new JComboBox<>(sizes);

        toolbar.add(new JLabel("Brush Size:"));
        toolbar.add(sizeBox);
        toolbar.add(blackButton);
        toolbar.add(redButton);
        toolbar.add(blueButton);
        toolbar.add(greenButton);
        toolbar.add(clearButton);

        DrawingPanel drawingPanel = new DrawingPanel();

        add(toolbar, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);

        blackButton.addActionListener(e -> drawingColor = Color.BLACK);
        redButton.addActionListener(e -> drawingColor = Color.RED);
        blueButton.addActionListener(e -> drawingColor = Color.BLUE);
        greenButton.addActionListener(e -> drawingColor = Color.GREEN);

        sizeBox.addActionListener(e ->
            brushSize = Integer.parseInt((String) sizeBox.getSelectedItem())
        );

        clearButton.addActionListener(e -> {
            points.clear();
            drawingPanel.repaint();
        });
    }

    private class DrawingPanel extends JPanel {

        DrawingPanel() {
            setBackground(Color.WHITE);

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    points.add(new Point(
                        e.getX(), e.getY(),
                        drawingColor, brushSize
                    ));
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    points.add(new Point(
                        e.getX(), e.getY(),
                        drawingColor, brushSize
                    ));
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (Point point : points) {
                g.setColor(point.color);
                g.fillOval(
                    point.x - point.size / 2,
                    point.y - point.size / 2,
                    point.size,
                    point.size
                );
            }
        }
    }

    private static class Point {
        int x, y, size;
        Color color;

        Point(int x, int y, Color color, int size) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.size = size;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new DrawPad().setVisible(true)
        );
    }
}
