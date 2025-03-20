package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeGroup extends Shape{
    private final List<Shape> shapes = new ArrayList<>();

    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Override
    public void removeShape(){
      //  shapes.remove(shape);
        shapes.clear();
    }

    @Override
    public void paintComponent(Graphics g) {
        for (Shape shape : shapes) {
            shape.paintComponent(g);
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
        this.x += dx;
        this.y += dy;
    }

}
