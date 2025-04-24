package org.example.creator;

import org.example.shape.Shape;
import org.example.shape.Circle;

import java.awt.*;

public class CircleCreator  implements ShapeCreator{
    @Override
    public Shape create(int x, int y, int size, Color color, int angleS){
        return new Circle(x, y, size, color, angleS);
    }
}
