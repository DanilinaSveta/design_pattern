package org.example.creator;

import org.example.shape.RedTriangle;
import org.example.shape.Shape;

import java.awt.*;

public class RedTriangleCreator implements ShapeCreator{
    @Override
    public Shape create(int x, int y, int size, Color color, int angleS){
        return new RedTriangle(x, y, size, color, angleS);
    }
}
