package org.example;

import javax.swing.*;
import java.awt.*;

public class Circle extends JPanel{

    protected void paintComponent(Graphics g, Shape shape){
        super.paintComponent(g);
        g.setColor(shape.color);
        g.fillOval(shape.x, shape.y, shape.size, shape.size);
    }
    public boolean isInside(Point p, Shape shape){
        return Math.pow(p.x - (shape.x + shape.size / 2), 2) +
                Math.pow(p.y - (shape.y + shape.size / 2), 2) <=
                Math.pow(shape.size / 2, 2);
    }
}
