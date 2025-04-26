package org.example;

public class ShapeInfo {
    public String type;
    public int x;
    public int y;
    public int size;
    public int angleS;

    public ShapeInfo[] shapes; // Для группы фигур


    public ShapeInfo(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public ShapeInfo[] getShapes() {
        return shapes;
    }

    public void setShapes(ShapeInfo[] shapes) {
        this.shapes = shapes;
    }


}
