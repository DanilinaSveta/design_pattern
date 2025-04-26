package org.example.shape;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
        g2.rotate(Math.toRadians(getAngleS()), getX() + (double) getSize() / 2, getY() + (double) getSize() / 2);
        g2.fillRect(getX(), getY(), getSize(), getSize());
        g2.rotate(Math.toRadians(-getAngleS()), getX() + (double) getSize() / 2, getY() + (double) getSize() / 2);
    }
    public boolean isInside(Point p){
        return p.getX() >= getX() &&
                p.getX() <= getX() + getSize() &&
                p.getY() >= getY() &&
                p.getY() <= getY() + getSize();
    }
    @Override
    public void addShape (Shape shape){}

    @Override
    public  void removeShape(){};

    @Override
    public void move(int dx, int dy) {
//        this.x += dx;
//        this.y += dy;
        setX(getX() + dx);
        setY(getY() + dy);
    }
    public void accept (Visitor visitor) throws IOException {
        visitor.visit(this);
    }
}
