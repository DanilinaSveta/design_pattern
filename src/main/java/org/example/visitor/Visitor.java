package org.example.visitor;

import org.example.shape.*;

import java.io.IOException;

public interface Visitor {
    public void visit (Square square) throws IOException;
    public void visit (Triangle triangle) throws IOException;
    public void visit (Circle circle) throws IOException;
    public void visit (ShapeGroup shapeGroup) throws IOException;
    public void visit (RedTriangle redTriangle) throws IOException;
}