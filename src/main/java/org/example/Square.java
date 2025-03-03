package org.example;

import javax.swing.*;
import java.awt.*;

public class Square extends JPanel {
    protected void paintComponent(Graphics g, Shape shape){
        super.paintComponent(g);
        g.setColor(shape.color);
        g.fillRect(shape.x, shape.y, shape.size, shape.size);
    }
    public boolean isInside(Point p, Shape shape){
        return p.x >= shape.x &&
                p.x <= shape.x + shape.size &&
                p.y >= shape.y &&
                p.y <= shape.y + shape.size;
    }
}
