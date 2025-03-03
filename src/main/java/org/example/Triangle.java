package org.example;

import java.awt.*;

public class Triangle extends Shape {
    public Triangle(int x, int y, int size, Color color){
        super("triangle", x, y, size, color);
    }
    public void paintComponent(Graphics g){
        g.setColor(color);
        int[] xPoints = {x, x - size / 2, x + size / 2};
        int[] yPoints = {y, y + size, y + size};
        g.fillPolygon(xPoints, yPoints, 3);
    }
    public boolean isInside(Point p){
        return p.x >= x - size / 2 &&
                p.x <= x + size / 2 &&
                p.y >= y &&
                p.y <= y + size;
    }
}
