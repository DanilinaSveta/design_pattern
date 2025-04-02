package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeGroup extends Shape{
    private final List<Shape> shapes;

    public ShapeGroup(int x, int y, int size, Color color, int angleS) {
        super("group", x, y, size, color, angleS);
        this.shapes = new ArrayList<>();
    }

    @Override
    public void addShape(Shape shape) {
        if (!shapes.contains(shape)){
            shapes.add(shape);
        }
    }
    public List<Shape> getShapes(){
        return shapes;
    }

    @Override
    public void removeShape(){
        shapes.clear();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (Shape shape : shapes) {
            shape.paintComponent(g2);
        }
    }

    @Override
    public boolean isInside(Point p) {
        for (Shape shape : shapes) {
            if (shape.isInside(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void select(boolean s) {
        for (Shape shape : shapes) {
            shape.select(s);
        }
    }
    @Override
    public void move(int dx, int dy) {
        for (Shape shape : shapes) {
           // System.out.println(shape.type);
            shape.move(dx, dy);
        }
    }

}
