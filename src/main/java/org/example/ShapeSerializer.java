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

    public  static List<Shape> readShapesFromJson (String filename, List<Shape> shapes, ShapeGroup shapeGroup, ShapeFactory SHAPE_FACTORY){
        try {
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
                System.out.println("type " + type);
                if (type.equals("group")){
                    shapes.remove(shapeGroup);
                    shapeGroup.removeShape();
                    List<Map<String, Object>> shapes1 = new ArrayList<>();
                    shapes1 = (List<Map<String, Object>>) shape.get("shapes");
                    System.out.println(shapes1);
                    for (Map<String, Object> shape1 : shapes1){
                         type = (String) shape1.get("type");
                         x = (int) shape1.get("x");
                         y = (int) shape1.get("y");
                         size = (int) shape1.get("size");
                        color = Color.ORANGE;
                        angleS = (int) shape1.get("angleS");
                        shapeGroup.addShape(SHAPE_FACTORY.create(type, x, y, size, color,angleS));
                    }
                    shapes.add(shapeGroup);
                    break;
                }
                shapes.add(SHAPE_FACTORY.create(type, x, y, size, color,angleS));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shapes;
    }
}
