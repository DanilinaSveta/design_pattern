package org.example;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Triangle extends Shape {
    private Rectangle2D rectangle;
    public Triangle(int x, int y, int size, Color color, int angleS){
        super("triangle", x, y, size, color, angleS);
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        int[] xPoints = {x, x - size / 2, x + size / 2};
        int[] yPoints = {y, y + size, y + size};
        g2.rotate(Math.toRadians(angleS), x  , y + (double) size / 2);
        g2.fillPolygon(xPoints, yPoints, 3);
        g2.rotate(Math.toRadians(-angleS), x  , y + (double) size /2);
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
//    @Override
//    public Rectangle2D getBounds() {
//        return rectangle;
//    }
//    @Override
//    public void rotate(int angle){
//        System.out.println("Поворот треугольник");
//    }
}
