package org.example;

import java.awt.*;

public class SquareCreator implements ShapeCreator{
    @Override
    public Shape create(int x, int y, int size, Color color){
         return new Square(x, y, size, color);
    }
}
