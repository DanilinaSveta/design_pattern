package org.example;

import java.awt.*;

public class Circle extends Shape{
    public Circle(int x, int y, int size, Color color){
        super("circle", x, y, size, color);
    }

    public void paintComponent(Graphics g){
        g.setColor(color);
        g.fillOval(x, y, size, size);
    }
    public boolean isInside(Point p){
        return Math.pow(p.x - (x + size / 2), 2) +
                Math.pow(p.y - (y + size / 2), 2) <=
                Math.pow(size / 2, 2);
    }
}
