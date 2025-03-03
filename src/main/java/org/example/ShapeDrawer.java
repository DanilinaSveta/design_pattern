package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeDrawer extends JFrame {

    private List<Shape> shapes = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private Shape selectedShape = null;
    private Point delta;
    Color color = Color.ORANGE;
    Canvas canvas = new Canvas();
    private boolean isKeyPressed = false;
    Circle circle = new Circle();
    Square square = new Square();
    Triangle triangle = new Triangle();
    public  int x = 100, y = 100, size = 100;
    private Point groupOffset = new Point(0, 0);

    public ShapeDrawer() {
        setTitle("Drawer");
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setFocusable(true);
        requestFocusInWindow();

        JPanel buttonPanel = new JPanel();

        JButton btnCircle = new JButton("Круг");
        JButton btnSquare = new JButton("Квадрат");
        JButton btnTriangle = new JButton("Треугольник");
        JButton btnSave = new JButton("Сохранить");
        JButton btnLoad = new JButton("Загрузить");
        JButton btnGroup = new JButton("Объединить");
        JButton btnUnGroup = new JButton("Разъединить");
        
        buttonPanel.add(btnCircle);
        buttonPanel.add(btnSquare);
        buttonPanel.add(btnTriangle);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnGroup);
        buttonPanel.add(btnUnGroup);

        buttonPanel.requestFocusInWindow();

        add(buttonPanel, BorderLayout.NORTH);

        add(canvas,BorderLayout.CENTER);

        btnCircle.addActionListener(e ->{
            shapes.add(new Shape("circle", x, y, size, color));
            canvas.repaint();
        });
        btnSquare.addActionListener(e -> {
            shapes.add(new Shape("square", x + 200, y, size, color));
            canvas.repaint();
        });
        btnTriangle.addActionListener(e -> {
            shapes.add(new Shape("triangle", x + 400, y, size, color));
            canvas.repaint();
        });
        btnGroup.addActionListener(e ->{
            for (Shape shape : shapes){
                if (shape.color == Color.GREEN){
                    shape.setGrouped(true);
                }
            }
            canvas.repaint();
        });
        btnUnGroup.addActionListener(e -> {
            for (Shape shape : shapes){
                shape.setGrouped(false);
            }
            canvas.repaint();
        });

        btnSave.addActionListener(e -> saveShapes());
        btnLoad.addActionListener(e -> loadShapes());

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL){
                    isKeyPressed = true;
                }
            }
            @Override
            public void keyReleased(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_CONTROL){
                    isKeyPressed = false;
                }
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                canvas.requestFocusInWindow();
                for (Shape shape : shapes) {
                    if (isInsideShape(e.getPoint(), shape)) {
                        selectedShape = shape;
                        delta = new Point(e.getX() - shape.x, e.getY() - shape.y);
                        break;
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedShape = null;
            }

            @Override
            public void mouseClicked(MouseEvent e){
                canvas.requestFocusInWindow();
                for (Shape shape : shapes) {
                    if (!isKeyPressed){
                        shape.color = Color.ORANGE;
                    }
                    if (isInsideShape(e.getPoint(), shape)) {
                        if (shape.color == Color.ORANGE){
                            shape.color = Color.GREEN;
                        } else {
                            shape.color = Color.ORANGE;
                        }
                    }
                }
                repaint();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                for (Shape shape : shapes){
                    selectedShape.x = e.getX() - delta.x;
                    selectedShape.y = e.getY() - delta.y;
                    if (shape.grouped){
                        groupOffset.translate(selectedShape.x, selectedShape.y);
                        delta.setLocation(e.getPoint());
                    } else {
                        groupOffset.setLocation(e.getX() - shape.x, e.getY() - shape.y);
                    }
                }
                canvas.repaint();
            }
        });
        requestFocusInWindow();

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

    private boolean isInsideShape(Point p, Shape shape) {
        switch (shape.type) {
            case "circle":
                return circle.isInside(p, shape);
            case "square":
                return square.isInside(p, shape);
            case "triangle":
                return triangle.isInside(p, shape);
        }
        return false;
    }

    public class Canvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Shape shape : shapes){
                if (shape.grouped) {
                    g.translate(groupOffset.x, groupOffset.y);
                }
            }
            for (Shape shape : shapes) {
                switch (shape.type) {
                    case "circle":
                        circle.paintComponent(g,shape);
                        break;
                    case "square":
                        square.paintComponent(g,shape);
                        break;
                    case "triangle":
                        triangle.paintComponent(g,shape);
                        break;
                }
            }

       }
    }
}
