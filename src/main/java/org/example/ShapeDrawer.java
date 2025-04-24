package org.example;

import org.example.shape.Shape;
import org.example.visitor.XmlDeserializer;
import org.example.visitor.XmlSerializerVisitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ShapeDrawer extends JFrame {
    Color colorOrange = Color.ORANGE;
    Color colorGreen = Color.GREEN;

    private List<org.example.shape.Shape> shapes = new ArrayList<>();
    private List<org.example.shape.Shape> selectedShapes = new ArrayList<>();

    private org.example.shape.Shape selectedShape = null;
    private Point delta;

    private Rectangle selectionRectangle;
    private Point dragStart;
    private boolean dragging = false;

    private boolean isKeyPressed = false;
    private static final ShapeFactory SHAPE_FACTORY = new ShapeFactory();

    public  int x = 100, y = 100, size = 100, angleS = 0;

    public ShapeDrawer() {
        selectionRectangle = new Rectangle(0, 0, 0, 0);

        setTitle("Drawer");
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setFocusable(true);
        requestFocusInWindow();

        JPanel buttonPanel = new JPanel();
        JPanel buttonPanel2 = new JPanel();
        JPanel buttonPanel3 = new JPanel();

        Box box = Box.createVerticalBox();

        JButton btnCircle = new JButton("Круг");
        JButton btnSquare = new JButton("Квадрат");
        JButton btnTriangle = new JButton("Треугольник");
        JButton btnSave = new JButton("Сохранить");
        JButton btnLoad = new JButton("Загрузить");
        JButton btnGroup = new JButton("Объединить");
        JButton btnUnGroup = new JButton("Разъединить");
        JButton btnTurn = new JButton("Поворот на 45");
        JButton btnTriangleRed = new JButton("Все треугольники красные");
        JButton btnSaveXML = new JButton("Сохранить в XML");
        JButton btnLoadXML = new JButton("Загрузка XML");
        
        buttonPanel.add(btnCircle);
        buttonPanel.add(btnSquare);
        buttonPanel.add(btnTriangle);
        buttonPanel.add(btnGroup);
        buttonPanel.add(btnUnGroup);
        buttonPanel.add(btnTurn);
        buttonPanel.add(btnTriangleRed);

        buttonPanel2.add(btnSave);
        buttonPanel2.add(btnLoad);

        buttonPanel3.add(btnSaveXML);
        buttonPanel3.add(btnLoadXML);

        buttonPanel.requestFocusInWindow();
        buttonPanel2.requestFocusInWindow();
        buttonPanel3.requestFocusInWindow();

        box.add(buttonPanel);
        box.add(buttonPanel2);
        box.add(buttonPanel3);

        add(box, BorderLayout.NORTH);

        Canvas canvas = new Canvas();
        add(canvas,BorderLayout.CENTER);

        btnCircle.addActionListener(e ->{
            shapes.add(SHAPE_FACTORY.create("circle", x, y, size, colorOrange, angleS));
            canvas.repaint();
        });
        btnSquare.addActionListener(e -> {
            shapes.add(SHAPE_FACTORY.create("square", x + 200, y, size, colorOrange, angleS));
            canvas.repaint();
        });
        btnTriangle.addActionListener(e -> {
            shapes.add(SHAPE_FACTORY.create("triangle", x + 400, y, size, colorOrange, angleS));
            canvas.repaint();
        });

        btnGroup.addActionListener(e ->{
            int minX = 100000000, minY = 100000000, maxX = 0, maxY = 0, size = 0;
            org.example.shape.Shape shapeGroup = SHAPE_FACTORY.create("group",x,y, size,Color.ORANGE, angleS);

            for (org.example.shape.Shape shape : selectedShapes){
                if (shape.x < minX){
                    minX = shape.x;
                }
                if (shape.y < minY){
                    minY = shape.y;
                }
                if (shape.x > maxX){
                    maxX = shape.x;
                    size = shape.size;
                }
                if (shape.y > maxY) {
                    maxY = shape.y;
                    size = shape.size;
                }
                shapeGroup.x = minX;
                shapeGroup.y = minY;
                shapeGroup.size = (maxX + size - minX);
                shapeGroup.addShape(shape);
                shapes.remove(shape);
            }
            shapes.add(shapeGroup);
            canvas.repaint();
        });

        btnUnGroup.addActionListener(e -> {
            for (org.example.shape.Shape shape : selectedShapes){
                if (shape.type.equals("group")){
                    shapes.addAll(shape.getShapes());
                    shapes.remove(shape);
                }
            }
            repaint();
        });

        btnTurn.addActionListener( e ->{
            for (org.example.shape.Shape shape : selectedShapes){
                shape.angle(45);
            }
            repaint();
        });

        btnTriangleRed.addActionListener(e ->{
            for (org.example.shape.Shape shape : shapes){
                if (shape.type.equals("triangle")){
                    shape.color = Color.RED;

                }
                if (shape.getShapes() != null){
                    ArrayList<org.example.shape.Shape> groupShapes = new ArrayList<>();
                    groupShapes.addAll(shape.getShapes());
                    for (org.example.shape.Shape shape1 : groupShapes){
                        System.out.println(shape1.type);
                        if (shape1.type.equals("triangle")){
                            shape1.color = Color.RED;
                        }
                    }
                }
            }
            repaint();
        });

        btnSave.addActionListener(e -> saveShapes());
        btnLoad.addActionListener(e -> loadShapes());
        btnSaveXML.addActionListener(e -> {
            try {
                saveShapesXML();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnLoadXML.addActionListener(e -> {
            try {
                loadShapesXML();
                canvas.repaint();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

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

                for (org.example.shape.Shape shape : shapes) {
                    if (shape.isInside(e.getPoint())) {
                        selectedShape = shape;
                        delta = new Point(e.getX(), e.getY());
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
                org.example.shape.Shape selectedShape = null;
                for (org.example.shape.Shape shape : shapes){
                    if (shape.isInside(e.getPoint())){
                        selectedShape = shape;
                        break;
                    }
                }
                if (!isKeyPressed) {
                    for (org.example.shape.Shape shape : selectedShapes) {
                        shape.select(false);
                    }
                    selectedShapes.clear();
                }
                if(selectedShape != null)
                {
                    selectedShape.select(true);
                    selectedShapes.add(selectedShape);
                }
                repaint();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (!dragging){
                    selectedShape.move(e.getX() - delta.x, e.getY() - delta.y);
                    delta.x = e.getX();
                    delta.y = e.getY();
                }
                if (dragging) {
                    for (org.example.shape.Shape shape : shapes){
                        int x = Math.min(dragStart.x, e.getX());
                        int y = Math.min(dragStart.y, e.getY());
                        int width = Math.abs(dragStart.x - e.getX());
                        int height = Math.abs(dragStart.y - e.getY());
                        selectionRectangle.setBounds(x, y, width, height);
                        if (selectionRectangle.intersects(shape.x,shape.y,shape.size,shape.size)){
                            shape.select(true);
                            if (!selectedShapes.contains(shape)){
                                selectedShapes.add(shape);
                            }
                        } else {
                            shape.select(false);
                            selectedShapes.remove(shape);
                        }
                    }
                }
                canvas.repaint();
            }
        });
        requestFocusInWindow();

    }
    private void saveShapes(){
        ShapeSerializer.saveShapesToJson(shapes,"shapes.json");
        JOptionPane.showMessageDialog(this, "Shapes saved successfully!");
    }
    private void loadShapes(){
        shapes.clear();
        ShapeSerializer.readShapeFromJson("shapes.json",shapes,SHAPE_FACTORY);
        repaint();
        JOptionPane.showMessageDialog(this, "Shapes loaded successfully!");
    }

    private void saveShapesXML() throws IOException {
        XmlSerializerVisitor xmlSerializerVisitor = new XmlSerializerVisitor();
        for (Shape shape : shapes){
            shape.accept(xmlSerializerVisitor);
        }
        xmlSerializerVisitor.saveToXML("shapes.xml");
        JOptionPane.showMessageDialog(this, "Shapes saved successfully!");
    }

    private void loadShapesXML() throws IOException {
        shapes.clear();
        XmlDeserializer.readShapeFromXml("shapes.xml",shapes,SHAPE_FACTORY);
        repaint();
        JOptionPane.showMessageDialog(this, "Shapes loaded successfully!");
    }


    public class Canvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            for (Shape shape : shapes) {
                shape.paintComponent(g2);
            }
            if (dragging) {
                g2.setColor(new Color(0, 255, 0, 128)); // Полупрозрачный зеленый
                g2.drawRect(selectionRectangle.x,selectionRectangle.y,selectionRectangle.width,selectionRectangle.height);
            }

       }
    }

}
