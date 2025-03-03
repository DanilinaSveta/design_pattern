package org.example;

import java.awt.*;

public class Shape {
    public String type;
    public int x, y, size;
    public Color color;
    public boolean selected, grouped;

    public Shape(){}

    public Shape(String type, int x, int y, int size, Color color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

//    public void move(int dx, int dy) {
//        this.x = dx + 100;
//        this.y = dy;
//    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    public boolean isGrouped() {
        return grouped;
    }
}
