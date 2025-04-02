package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShapeSerializer {
    private  static final ObjectMapper objectMapper = new ObjectMapper();

    public static void saveShapesToJson (List<Shape> shapes, String filename){
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), shapes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static List<Shape>readShapesFromJson (
            String filename,
            List<Shape> shapes,
            ShapeGroup shapeGroup,
            ShapeFactory SHAPE_FACTORY,
            List<Shape> groupShapes){
        try {
            groupShapes.clear();
            List<Map<String, Object>> loadedShapes = new ArrayList<>();
            loadedShapes = objectMapper.readValue(new File(filename), new TypeReference<List<Map<String, Object>>>() {});
            System.out.println(loadedShapes);
            shapes.clear();

            for (Map<String, Object> shape : loadedShapes) {
                String type = (String) shape.get("type");
                int x = (int) shape.get("x");
                int y = (int) shape.get("y");
                int size = (int) shape.get("size");
                Color color = Color.ORANGE;
                int angleS = (int) shape.get("angleS");
                if (type.equals("group")){
                    shapes.remove(shapeGroup);
                    shapeGroup.removeShape();
                    List<Map<String, Object>> shapes1 = new ArrayList<>();
                    shapes1 = (List<Map<String, Object>>) shape.get("shapes");
                    System.out.println(shapes1);
                    for (Map<String, Object> shape1 : shapes1){
                         String typeG = (String) shape1.get("type");
                         int xG = (int) shape1.get("x");
                         int yG = (int) shape1.get("y");
                         int sizeG = (int) shape1.get("size");
                        Color colorG = Color.ORANGE;
                        int angleSG = (int) shape1.get("angleS");
                        shapeGroup.addShape(SHAPE_FACTORY.create(typeG , xG, yG, sizeG, colorG, angleSG));
                        groupShapes.add(shapeGroup);
                    }
                    shapes.add(shapeGroup);
                    break;
                }
                shapes.add(SHAPE_FACTORY.create(type, x, y, size, color, angleS));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shapes;
    }
}
