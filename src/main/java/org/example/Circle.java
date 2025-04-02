package org.example;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Circle extends Shape{
    public Circle(int x, int y, int size, Color color, int angleS){
        super("circle", x, y, size, color,angleS);
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.rotate(Math.toRadians(angleS), x + (double) size / 2, y + (double) size );
        g2.fillOval(x, y, size, size*2);
        g2.rotate(Math.toRadians(-angleS), x + (double) size / 2, y + (double) size );
    }
    public boolean isInside(Point p){
        return Math.pow(p.x - (x + size / 2), 2) +
                Math.pow(p.y - (y + size / 2), 2) <=
                Math.pow(size / 2, 2);
    }
    @Override
    public void addShape (Shape shape){}

    @Override
    public  void removeShape(){};

    @Override
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }
}
