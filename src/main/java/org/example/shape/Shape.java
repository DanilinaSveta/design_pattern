package org.example.shape;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.example.visitor.Visitor;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public abstract class Shape {

    public String type;
    public int x;
    public int y;
    public int size;
    public int angleS;
    @JsonIgnore
    public Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Shape(){}

    public Shape(String type, int x, int y, int size, Color color, int angleS) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.angleS = angleS;
    }

    public abstract void paintComponent(Graphics g);
    public abstract boolean isInside(Point p);
    public abstract void addShape(Shape shape);
    public abstract void removeShape();
    public abstract void move(int dx, int dy);
    public abstract void accept(Visitor visitor) throws IOException;

    public void select(boolean s){
        if (s){
            this.color = Color.GREEN;
        } else {
            this.color = Color.ORANGE;
        }
    }
    public void angle(int angle){
        setAngleS(getAngleS()+angle);
        if (getAngleS() == 360){
            setAngleS(0);
        }
    }
    public List<Shape> getShapes(){
        return null;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAngleS() {
        return angleS;
    }

    public void setAngleS(int angleS) {
        this.angleS = angleS;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
