package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShapeDrawer extends JFrame {

    private List<Shape> shapes = new ArrayList<>();
    private List<Shape> groupShapes = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private Shape selectedShape = null;
    private Shape selectedShapeGroup = null;
    private Point delta;
    private Point deltaGroup;
    private boolean btnG = false;

    private Rectangle selectionRectangle;
    private Point dragStart;
    private boolean dragging = false;

    Color colorOrange = Color.orange;
    Color colorGreen = Color.green;

    private boolean isKeyPressed = false;
    private static final ShapeFactory SHAPE_FACTORY = new ShapeFactory();

    public  int x = 100, y = 100, size = 100;

    public ShapeDrawer() {
        selectionRectangle = new Rectangle(0, 0, 0, 0);
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

        Canvas canvas = new Canvas();
        add(canvas,BorderLayout.CENTER);

        btnCircle.addActionListener(e ->{
            shapes.add(SHAPE_FACTORY.create("circle", x, y, size, colorOrange));
            canvas.repaint();
        });
        btnSquare.addActionListener(e -> {
            shapes.add(SHAPE_FACTORY.create("square", x + 200, y, size, colorOrange));
            canvas.repaint();
        });
        btnTriangle.addActionListener(e -> {
            shapes.add(SHAPE_FACTORY.create("triangle", x + 400, y, size, colorOrange));
            canvas.repaint();
        });

        ShapeGroup shapeGroup = new ShapeGroup();
        btnGroup.addActionListener(e ->{
            btnG = true;
            for (Shape shape : groupShapes){
                shapeGroup.addShape(shape);
                shapes.remove(shape);
            }
            shapes.add(shapeGroup);
            canvas.repaint();
        });

        btnUnGroup.addActionListener(e -> {
            btnG = false;
            for (Shape shape : groupShapes){
                shapes.add(shape);
            }
            shapes.remove(shapeGroup);
            shapeGroup.removeShape();
            groupShapes.clear();
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
                    if (shape.isInside(e.getPoint())) {
                        selectedShape = shape;
                        delta = new Point(e.getX() - shape.x, e.getY() - shape.y);
                        dragging = false;
                        break;
                    }
                    else {
                        dragStart = e.getPoint();
                        selectionRectangle.setLocation(dragStart);
                        selectionRectangle.setSize(0, 0);
                        dragging = true;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
                selectionRectangle.setSize(0, 0); // Сброс выделения
                selectedShape = null;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e){
                canvas.requestFocusInWindow();
                for (Shape shape : shapes) {
                    if (!isKeyPressed){
                        shape.select(false);
                        if (!btnG){
                            groupShapes.clear();
                        }
                        if (shape.isInside(e.getPoint())){
                            shape.select(true);
                            groupShapes.add(shape);
                        }
                    } else {
                        if (shape.isInside(e.getPoint())){
                            groupShapes.add(shape);
                            shape.select(true);
                        }
                    }
                }
                repaint();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                for (Shape shape : shapes){
                    if (shape.isInside(e.getPoint()) & !dragging) {
                        selectedShape.x = e.getX() - delta.x;
                        selectedShape.y = e.getY() - delta.y;
                    }
                    if (dragging) {
                        int x = Math.min(dragStart.x, e.getX());
                        int y = Math.min(dragStart.y, e.getY());
                        int width = Math.abs(dragStart.x - e.getX());
                        int height = Math.abs(dragStart.y - e.getY());
                        selectionRectangle.setBounds(x, y, width, height);
                        if (selectionRectangle.intersects(shape.x,shape.y,shape.size,shape.size)){
                            shape.select(true);
                            groupShapes.add(shape);
                        } else {
                            shape.select(false);
                            groupShapes.remove(shape);
                        }
                    }
                }
                canvas.repaint();
            }
        });
        requestFocusInWindow();

    }

    private void saveShapes(){
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(shapes);
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
            List<Map<String, Object>> loadedShapes = new ArrayList<>();
            loadedShapes = mapper.readValue(new File("shapes.json"), new TypeReference<List<Map<String, Object>>>() {});
            System.out.println(loadedShapes);
            shapes.clear();
            for (Map<String, Object> shape : loadedShapes) {
                String type = (String) shape.get("type");
                int x = (int) shape.get("x");
                int y = (int) shape.get("y");
                int size = (int) shape.get("size");
                Color color = colorOrange;
                shapes.add(SHAPE_FACTORY.create(type, x, y, size, color));
            }
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
            for (Shape shape : shapes) {
                shape.paintComponent(g);
            }
            if (dragging) {
                g.setColor(new Color(0, 255, 0, 128)); // Полупрозрачный зеленый
                g.drawRect(selectionRectangle.x,selectionRectangle.y,selectionRectangle.width,selectionRectangle.height);
            }
       }
    }
}
