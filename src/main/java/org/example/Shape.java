package org.example;

import java.awt.*;

public abstract class Shape {
    public String type;
    public int x, y, size;
    public Color color;
    public boolean selected, grouped;

    public Shape(String type, int x, int y, int size, Color color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public abstract void paintComponent(Graphics g);
    public abstract boolean isInside(Point p);

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

}
