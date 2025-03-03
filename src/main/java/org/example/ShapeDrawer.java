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
    private final ObjectMapper mapper = new ObjectMapper();
    private Shape selectedShape = null;
    private Point delta;

    Color colorOrange = Color.orange;
    Color colorGreen = Color.green;

    private boolean isKeyPressed = false;
    private static final ShapeFactory SHAPE_FACTORY = new ShapeFactory();

    public  int x = 100, y = 100, size = 100;
    private Point groupOffset = new Point();

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
        btnGroup.addActionListener(e ->{
            for (Shape shape : shapes){
                if (shape.color == colorGreen){
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
                    if (shape.isInside(e.getPoint())) {
                        selectedShape = shape;
                        delta = new Point(e.getX() - shape.x, e.getY() - shape.y);
                        break;
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
              //  selectedShape = null;
            }

            @Override
            public void mouseClicked(MouseEvent e){
                canvas.requestFocusInWindow();
                for (Shape shape : shapes) {
                    if (!isKeyPressed){
                        shape.color = colorOrange;
                    }
                    if (shape.isInside(e.getPoint())) {
                        if (shape.color == colorOrange){
                            shape.color = colorGreen;
                        } else {
                            shape.color = colorOrange;
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
            for (Shape shape : shapes){
                if (shape.grouped) {
                    g.translate(groupOffset.x, groupOffset.y);
                }
            }
            for (Shape shape : shapes) {
                shape.paintComponent(g);
            }

       }
    }
}
