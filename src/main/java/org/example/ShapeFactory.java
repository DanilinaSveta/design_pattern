package org.example;

import org.example.creator.*;
import org.example.shape.Shape;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ShapeFactory {
    Map<String , ShapeCreator> creatorMap = new HashMap<>();

    public ShapeFactory(){
        creatorMap.put("circle", new CircleCreator());
        creatorMap.put("square", new SquareCreator());
        creatorMap.put("triangle", new TriangleCreator());
        creatorMap.put("group", new ShapeGroupCreator());
    }

    public Shape create(String type, int x, int y, int size, Color color, int angleS){
        return creatorMap.get(type).create(x, y, size, color, angleS);
    }
}
