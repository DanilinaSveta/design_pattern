package org.example.shape;

import org.example.visitor.Visitor;

import java.awt.*;
import java.io.IOException;

public class RedTriangle extends Shape{
    Triangle triangle;
    public RedTriangle(int x, int y, int size, Color color, int angleS) {
        triangle = new Triangle(x, y, size, color, angleS);
        super.type = "redtriangle";
    }

    @Override
    public void select(boolean s){
        if (s){
            triangle.color = Color.GREEN;
        } else {
            triangle.color = Color.RED;
        }
    }

    @Override
    public void paintComponent(Graphics g){
        triangle.paintComponent(g);
    }

    @Override
    public boolean isInside(Point p){
        return triangle.isInside(p);
    }

    @Override
    public void addShape (org.example.shape.Shape shape){}

    @Override
    public  void removeShape(){};
    @Override
    public void move(int dx, int dy) {
        triangle.move(dx,dy);
    }
    public void accept (Visitor visitor) throws IOException {
        visitor.visit(this);
    }
    public int getX() {
        return triangle.x;
    }

    public int getY() {
        return triangle.y;
    }

    public int getSize() {
        return triangle.size;
    }

    public int getAngleS() {
        return triangle.angleS;
    }
    public void setX(int x) {
        triangle.x = x;
    }

    public void setY(int y) {
        triangle.y = y;
    }

    public void setSize(int size) {
        triangle.size = size;
    }

    public void setAngleS(int angleS) {
        triangle.angleS = angleS;
    }
}
