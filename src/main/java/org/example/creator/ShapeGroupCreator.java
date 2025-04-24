package org.example.creator;

import org.example.shape.Shape;
import org.example.shape.ShapeGroup;

import java.awt.*;

public class ShapeGroupCreator implements ShapeCreator{
    @Override
    public Shape create(int x, int y, int size, Color color, int angleS){
        return new ShapeGroup(x, y, size, color, angleS);
    }
}
