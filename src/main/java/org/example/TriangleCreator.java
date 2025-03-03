package org.example;

import java.awt.*;

public class TriangleCreator implements ShapeCreator{
    @Override
    public Shape create(int x, int y, int size, Color color){
        return new Triangle(x, y, size, color);
    }
}
