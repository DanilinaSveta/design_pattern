package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class ShapeDrawer extends JFrame {
    Color colorOrange = Color.ORANGE;
    Color colorGreen = Color.GREEN;

    private List<Shape> shapes = new ArrayList<>();
    private List<Shape> groupShapes = new ArrayList<>();
    private ShapeGroup shapeGroup = new ShapeGroup(0,0,0,colorOrange,0);

    private final ObjectMapper mapper = new ObjectMapper();
    private Shape selectedShape = null;
    private Point delta;

    private boolean btnG = false;

    private Rectangle selectionRectangle;
    private Point dragStart;
    private boolean dragging = false;



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
        JPanel buttonPanel2 = new JPanel();

        Box box = Box.createVerticalBox();

        JButton btnCircle = new JButton("Круг");
        JButton btnSquare = new JButton("Квадрат");
        JButton btnTriangle = new JButton("Треугольник");
        JButton btnSave = new JButton("Сохранить");
        JButton btnLoad = new JButton("Загрузить");
        JButton btnGroup = new JButton("Объединить");
        JButton btnUnGroup = new JButton("Разъединить");
        JButton btnTurn = new JButton("Поворот на 45");
        
        buttonPanel.add(btnCircle);
        buttonPanel.add(btnSquare);
        buttonPanel.add(btnTriangle);
        buttonPanel.add(btnGroup);
        buttonPanel.add(btnUnGroup);
        buttonPanel.add(btnTurn);

        buttonPanel2.add(btnSave);
        buttonPanel2.add(btnLoad);

        buttonPanel.requestFocusInWindow();
        buttonPanel2.requestFocusInWindow();

        box.add(buttonPanel);
        box.add(buttonPanel2);

        add(box, BorderLayout.NORTH);

        Canvas canvas = new Canvas();
        add(canvas,BorderLayout.CENTER);

        btnCircle.addActionListener(e ->{
            shapes.add(SHAPE_FACTORY.create("circle", x, y, size, colorOrange, 0));
            canvas.repaint();
        });
        btnSquare.addActionListener(e -> {
            shapes.add(SHAPE_FACTORY.create("square", x + 200, y, size, colorOrange, 0));
            canvas.repaint();
        });
        btnTriangle.addActionListener(e -> {
            shapes.add(SHAPE_FACTORY.create("triangle", x + 400, y, size, colorOrange, 0));
            canvas.repaint();
        });

        btnGroup.addActionListener(e ->{
            btnG = true;
            shapes.remove(shapeGroup);
            for (Shape shape : groupShapes){
                shapeGroup.addShape(shape);
                shapes.remove(shape);
            }
            shapes.add(shapeGroup);
            canvas.repaint();
        });

        btnUnGroup.addActionListener(e -> {
            btnG = false;
            shapes.addAll(groupShapes);
            shapes.remove(shapeGroup);
            shapeGroup.removeShape();
            groupShapes.clear();
            canvas.repaint();
        });

        btnTurn.addActionListener( e ->{
            for (Shape shape : shapes){
                if (shape.color == Color.GREEN){
                   // System.out.println("ПОВОРОТ");
                    shape.angle(45);
                }
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
                for (Shape shape : shapes) {
                    if (!isKeyPressed){
                        shape.select(false);
                        if (!btnG){
                            groupShapes.clear();
                        }
                        if (shape.isInside(e.getPoint())){
                            shape.select(true);
                            selectedShape = shape;
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
                if (!dragging){
                    selectedShape.move(e.getX() - delta.x, e.getY() - delta.y);
                    delta.x = e.getX();
                    delta.y = e.getY();
                }
                if (dragging) {
                    for (Shape shape : shapes){
                        int x = Math.min(dragStart.x, e.getX());
                        int y = Math.min(dragStart.y, e.getY());
                        int width = Math.abs(dragStart.x - e.getX());
                        int height = Math.abs(dragStart.y - e.getY());
                        selectionRectangle.setBounds(x, y, width, height);
                        if (selectionRectangle.intersects(shape.x,shape.y,shape.size,shape.size)){
                            shape.select(true);
                            if (!groupShapes.contains(shape)){
                                groupShapes.add(shape);
                            }
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
        ShapeSerializer.saveShapesToJson(shapes,"shapes.json");
        JOptionPane.showMessageDialog(this, "Shapes saved successfully!");
    }
    private void loadShapes(){
        ShapeSerializer.readShapesFromJson("shapes.json",shapes, shapeGroup, SHAPE_FACTORY);
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
