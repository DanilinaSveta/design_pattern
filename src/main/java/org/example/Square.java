package org.example;

import java.awt.*;

public class Square extends Shape {
    public Square(int x, int y, int size, Color color){
        super("square", x, y, size, color);
    }

    public void paintComponent(Graphics g){
        g.setColor(color);
        g.fillRect(x, y, size, size);
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
}
