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

    public static List<Shape> readShapeFromJson (String filename, ShapeFactory SHAPE_FACTORY){
        List<Shape> shapes = new ArrayList<>();

        try {
            List<Shape> shapeList = objectMapper.readValue(new File(filename), new TypeReference<List<Shape>>() {});
            System.out.println(shapeList);
            for (Shape shape : shapeList){
                if ("group".equals(shape.type)){
                    ShapeGroup shapeGroup = new ShapeGroup(shape.x,shape.y,shape.size,Color.ORANGE,shape.angleS);
                    for (Shape innerShape : ((ShapeGroup) shape).getShapes()){
                       // shapeGroup.addShape(innerShape);
                        shapeGroup.addShape(SHAPE_FACTORY.create(
                                innerShape.type,
                                innerShape.x,
                                innerShape.y,
                                innerShape.size,
                                Color.ORANGE,
                                innerShape.angleS));
                    }
                   //shapes.add(shapeGroup);
                    shapes.add(SHAPE_FACTORY.create(
                            shapeGroup.type,
                            shapeGroup.x,
                            shapeGroup.y,
                            shapeGroup.size,
                            Color.ORANGE,
                            shapeGroup.angleS));
                } else {
                    shapes.add(SHAPE_FACTORY.create(shape.type,shape.x, shape.y,shape.size, Color.ORANGE,shape.angleS));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return shapes;
    }

}
