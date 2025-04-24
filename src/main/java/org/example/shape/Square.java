package org.example.shape;

import org.example.visitor.Visitor;

import java.awt.*;
import java.io.IOException;


public class Square extends Shape {
    public Square(int x, int y, int size, Color color, int angleS){
        super("square", x, y, size, color, angleS);
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.rotate(Math.toRadians(angleS), x + (double) size / 2, y + (double) size / 2);
        g2.fillRect(x, y, size, size);
        g2.rotate(Math.toRadians(-angleS), x + (double) size / 2, y + (double) size / 2);
    }
    public boolean isInside(Point p){
        return p.x >= x &&
                p.x <= x + size &&
                p.y >= y &&
                p.y <= y + size;
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
    public void accept (Visitor visitor) throws IOException {
        visitor.visit(this);
    }
}
