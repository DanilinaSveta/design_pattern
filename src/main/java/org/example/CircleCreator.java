package org.example;

import java.awt.*;

public class CircleCreator  implements ShapeCreator{
    @Override
    public Shape create(int x, int y, int size, Color color){
        return new Circle(x, y, size, color);
    }
}
