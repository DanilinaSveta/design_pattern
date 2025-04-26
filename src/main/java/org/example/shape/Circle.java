package org.example.shape;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.example.visitor.Visitor;

import java.awt.*;
import java.io.IOException;


public class Circle extends Shape {

    //public Circle() {}

    public Circle(int x, int y, int size, Color color, int angleS){
        super("circle", x, y, size, color,angleS);
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.rotate(Math.toRadians(getAngleS()), getX() + (double) getSize() / 2, getY() + (double) getSize() );
        g2.fillOval(getX(), getY(), getSize(), getSize());
        g2.rotate(Math.toRadians(-getAngleS()), getX() + (double) getSize() / 2, getY() + (double) getSize() );
    }
    public boolean isInside(Point p){
        return Math.pow(p.getX() - (getX() + getSize() / 2), 2) +
                Math.pow(p.getY() - (getY() + getSize() / 2), 2) <=
                Math.pow(getSize() / 2, 2);
    }
    @Override
    public void addShape (Shape shape){}

    @Override
    public  void removeShape(){};

    @Override
    public void move(int dx, int dy) {
        setX(getX() + dx);
        setY(getY() + dy);
//        this.x += dx;
//        this.y += dy;
    }

    public void accept (Visitor visitor) throws IOException {
        visitor.visit(this);
    }
}
