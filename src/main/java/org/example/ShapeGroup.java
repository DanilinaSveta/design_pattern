package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeGroup extends Shape{
    private List<Shape> shapes;

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
        int maxX = 0, minX = -1;
        int maxY = 0, minY = -1;
        int size = 0;
        for (Shape shape : shapes) {
            if (minX == -1 && minY == -1){
                minX = shape.x;
                minY = shape.y;
            }
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
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(Math.toRadians(angleS), minX + (maxX+ size)/4, minY + (maxY+size)/4);
        for (Shape shape : shapes) {
            shape.paintComponent(g2);
        }
        g2.rotate(Math.toRadians(-angleS), minX +(maxX+ size)/4, minY + (maxY+size)/4);
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
