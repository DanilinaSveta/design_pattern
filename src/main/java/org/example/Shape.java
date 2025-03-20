package org.example;

import java.awt.*;

public abstract class Shape {
    public String type;
    public int x, y, size;
    public Color color;

    public Shape(){}

    public Shape(String type, int x, int y, int size, Color color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public abstract void paintComponent(Graphics g);
    public abstract boolean isInside(Point p);
    public abstract void addShape(Shape shape);
    public abstract void removeShape();
    public abstract void move(int dx, int dy);

    public void select(boolean s){
        if (s){
            this.color = Color.GREEN;
        } else {
            this.color = Color.ORANGE;
        }
    }


}
