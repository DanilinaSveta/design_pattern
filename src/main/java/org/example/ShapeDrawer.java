package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeDrawer extends JFrame {
    private List<Shape> shapes = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public ShapeDrawer() {
        setTitle("Drawer");
        setSize(1500,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        JButton btnCircle = new JButton("Круг");
        JButton btnSquare = new JButton("Квадрат");
        JButton btnTriangle = new JButton("Треугольник");
        JButton btnSave = new JButton("Сохранить");
        JButton btnLoad = new JButton("Загрузить");

        buttonPanel.add(btnCircle);
        buttonPanel.add(btnSquare);
        buttonPanel.add(btnTriangle);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnLoad);

        add(buttonPanel, BorderLayout.NORTH);

        Canvas canvas = new Canvas();
        add(canvas,BorderLayout.CENTER);

        btnCircle.addActionListener(e ->{
            shapes.add(new Shape("circle", 100, 100, 100));
            canvas.repaint();
        });
        btnSquare.addActionListener(e -> {
            shapes.add(new Shape("square", 300, 100, 100));
            canvas.repaint();
        });

        btnTriangle.addActionListener(e -> {
            shapes.add(new Shape("triangle", 500, 100, 100));
            canvas.repaint();
        });

        btnSave.addActionListener(e -> saveShapes());
        btnLoad.addActionListener(e -> loadShapes());

    }
    private void saveShapes(){
        try {
            String json = mapper.writeValueAsString(shapes);
            try (Writer writer = new BufferedWriter(new FileWriter("shapes.json"))) {
                writer.write(json);
            }
            JOptionPane.showMessageDialog(this, "Shapes saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadShapes() {
        try {
            shapes = List.of(mapper.readValue(new File("shapes.json"), Shape[].class));
            repaint();
            JOptionPane.showMessageDialog(this, "Shapes loaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public class Canvas extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.orange);
            for (Shape shape : shapes) {
                switch (shape.type) {
                    case "circle":
                        g.fillOval(shape.x, shape.y, shape.size, shape.size);
                        break;
                    case "square":
                        g.fillRect(shape.x, shape.y, shape.size, shape.size);
                        break;
                    case "triangle":
                        int[] xPoints = {shape.x, shape.x - shape.size / 2, shape.x + shape.size / 2};
                        int[] yPoints = {shape.y, shape.y + shape.size, shape.y + shape.size};
                        g.fillPolygon(xPoints, yPoints, 3);
                        break;
                }
            }
        }
    }
}
