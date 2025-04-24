package org.example.creator;

import org.example.shape.Shape;
import org.example.shape.Triangle;

import java.awt.*;

public class TriangleCreator implements ShapeCreator{
    @Override
    public Shape create(int x, int y, int size, Color color, int angleS){
        return new Triangle(x, y, size, color, angleS);
    }
}
