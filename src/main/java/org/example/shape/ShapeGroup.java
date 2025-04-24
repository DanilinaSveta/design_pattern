package org.example.shape;

import org.example.visitor.Visitor;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShapeGroup extends Shape {
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
        int sizeG = 0;
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
                sizeG = shape.size;
            }
            if (shape.y > maxY) {
                maxY = shape.y;
                sizeG = shape.size;
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(123, 226, 202, 128));
        g2.rotate(Math.toRadians(angleS), minX + (maxX+ size)/4, minY + (maxY+size)/4);
        g2.drawRect(minX-10,minY-10,maxX+ sizeG-minX+20,maxY+sizeG-minY+20);
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
            shape.move(dx, dy);
            this.x += dx;
            this.y += dy;
        }
    }
    public void accept (Visitor visitor) throws IOException {
        visitor.visit(this);
    }

}
