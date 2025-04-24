package org.example.visitor;

import org.example.shape.Circle;
import org.example.shape.ShapeGroup;
import org.example.shape.Square;
import org.example.shape.Triangle;

import java.io.IOException;

public interface Visitor {
    public void visit (Square square) throws IOException;
    public void visit (Triangle triangle) throws IOException;
    public void visit (Circle circle) throws IOException;
    public void visit (ShapeGroup shapeGroup) throws IOException;
}