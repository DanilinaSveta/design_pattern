package org.example.shape;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.example.visitor.Visitor;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;


public class Triangle extends Shape {
    private Rectangle2D rectangle;


    public Triangle(int x, int y, int size, Color color, int angleS){
        super("triangle", x, y, size, color, angleS);
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        int[] xPoints = {getX(), getX() - getSize() / 2, getX() + getSize() / 2};
        int[] yPoints = {getY(), getY() + getSize(), getY() + getSize()};
        g2.rotate(Math.toRadians(getAngleS()), getX()  , getY() + (double) getSize() / 2);
        g2.fillPolygon(xPoints, yPoints, 3);
        g2.rotate(Math.toRadians(-getAngleS()), getX()  , getY() + (double) getSize() /2);
    }
    public boolean isInside(Point p){
        return p.getX() >= getX() - getSize() / 2 &&
                p.getX() <= getX() + getSize() / 2 &&
                p.getY() >= getY() &&
                p.getY() <= getY() + getSize();
    }
    @Override
    public void addShape (Shape shape){}

    @Override
    public  void removeShape(){};

    @Override
    public void move(int dx, int dy) {
        int[] xPoints = {getX(), getX() - getSize() / 2, getX() + getSize() / 2};
        int[] yPoints = {getY(), getY() + getSize(), getY() + getSize()};
      //  this.x += dx;
        setX(getX() + dx);
        setY(getY() + dy);
      //  this.y += dy;
        xPoints[0] += dx;
        xPoints[1] += dx;
        xPoints[2] += dx;
        yPoints[0] += dy;
        yPoints[1] += dy;
        yPoints[2] += dy;
    }
    public void accept (Visitor visitor) throws IOException {
        visitor.visit(this);
    }

}
