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
    @Override
    public void addShape (Shape shape){}

    @Override
    public  void removeShape(){};

    @Override
    public void move(int dx, int dy) {
        int[] xPoints = {x, x - size / 2, x + size / 2};
        int[] yPoints = {y, y + size, y + size};
        this.x += dx;
        this.y += dy;
        xPoints[0] += dx;
        xPoints[1] += dx;
        xPoints[2] += dx;
        yPoints[0] += dy;
        yPoints[1] += dy;
        yPoints[2] += dy;
    }
}
