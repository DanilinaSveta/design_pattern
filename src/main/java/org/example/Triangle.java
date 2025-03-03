package org.example;

import javax.swing.*;
import java.awt.*;

public class Triangle extends JPanel {
    protected void paintComponent(Graphics g, Shape shape){
        super.paintComponent(g);
        g.setColor(shape.color);
        int[] xPoints = {shape.x, shape.x - shape.size / 2, shape.x + shape.size / 2};
        int[] yPoints = {shape.y, shape.y + shape.size, shape.y + shape.size};
        g.fillPolygon(xPoints, yPoints, 3);
    }
    public boolean isInside(Point p, Shape shape){
        return p.x >= shape.x - shape.size / 2 &&
                p.x <= shape.x + shape.size / 2 &&
                p.y >= shape.y &&
                p.y <= shape.y + shape.size;
    }
}
