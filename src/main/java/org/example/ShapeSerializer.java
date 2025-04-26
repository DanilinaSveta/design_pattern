package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.creator.ShapeCreator;
import org.example.shape.Shape;
import org.example.shape.ShapeGroup;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShapeSerializer {
    private  static final ObjectMapper objectMapper = new ObjectMapper();

    public static void saveShapesToJson (List<org.example.shape.Shape> shapes, String filename){
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), shapes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Shape> loadFromJSON(String filePath) throws IOException {
        ShapeInfo[] shapesInfo = objectMapper.readValue(new File(filePath), ShapeInfo[].class);
        return createShapes(shapesInfo);
    }
    private static List<Shape> createShapes(ShapeInfo[] shapesData) {
        List<Shape> shapes = new ArrayList<>();
        for (ShapeInfo shapeData : shapesData) {
            ShapeFactory factory = new ShapeFactory();
            Shape shape = factory.create(
                    shapeData.type,
                    shapeData.x,
                    shapeData.y,
                    shapeData.size,
                    shapeData.type.equals("redtriangle") ? Color.RED : Color.ORANGE,
                    shapeData.angleS);
            if (shape instanceof ShapeGroup) {
                ShapeGroup shapeGroup = (ShapeGroup) shape;
                shapeGroup.setShapes(createShapes(shapeData.getShapes()));
            }
            shapes.add(shape);
        }
        return shapes;
    }
}
