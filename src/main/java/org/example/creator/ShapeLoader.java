package org.example.creator;

import org.example.ShapeFactory;
import org.example.shape.Shape;
import org.example.shape.ShapeGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShapeLoader {
    private ShapeFactory shapeFactory = new ShapeFactory(); // Используем фабрику для создания фигур

    public List<Shape> loadShapesFromFile(String filePath) {
        List<Shape> shapes = new ArrayList<>();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(filePath));
            doc.getDocumentElement().normalize();

            int a = 0;
            NodeList shapeNodes = doc.getElementsByTagName("*");
            for (int i = 0; i < shapeNodes.getLength(); i++) {
                Element shapeElement = (Element) shapeNodes.item(i);
                if (shapeElement.getNodeName().equals("Square") ||
                        shapeElement.getNodeName().equals("Circle") ||
                        shapeElement.getNodeName().equals("Triangle") ||
                        shapeElement.getNodeName().equals("RedTriangle")) {

                    int x = Integer.parseInt(shapeElement.getElementsByTagName("x").item(0).getTextContent());
                    int y = Integer.parseInt(shapeElement.getElementsByTagName("y").item(0).getTextContent());
                    int size = Integer.parseInt(shapeElement.getElementsByTagName("size").item(0).getTextContent());
                    String type = shapeElement.getElementsByTagName("type").item(0).getTextContent();
                    Color color = type.equals("redtriangle") ? Color.RED : Color.ORANGE;
                    int angleS =  Integer.parseInt(shapeElement.getElementsByTagName("angleS").item(0).getTextContent());

                    Shape shape = shapeFactory.create(type, x, y, size, color, angleS);
                    if (shape != null) {
                        shapes.add(shape);
                    }

                } else if (shapeElement.getNodeName().equals("Group")) {
                    ShapeGroup group = new ShapeGroup(
                            Integer.parseInt(shapeElement.getElementsByTagName("x").item(0).getTextContent()),
                            Integer.parseInt(shapeElement.getElementsByTagName("y").item(0).getTextContent()),
                            Integer.parseInt(shapeElement.getElementsByTagName("size").item(0).getTextContent()),
                            Color.ORANGE,
                            Integer.parseInt(shapeElement.getElementsByTagName("angleS").item(0).getTextContent())
                            );

                    // Рекурсивно загружаем фигуры из вложенных групп
                    a = loadGroupShapes(shapeElement, group, a);
                    shapes.add(group);
                    i +=a;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shapes;
    }

    private int loadGroupShapes(Element groupElement, ShapeGroup group, int a) {
        NodeList shapeNodes = groupElement.getChildNodes();
        for (int i = 0; i < shapeNodes.getLength(); i++) {
            a +=1;
            if (shapeNodes.item(i) instanceof Element) {
                Element shapeElement = (Element) shapeNodes.item(i);
                if (shapeElement.getNodeName().equals("Square") ||
                        shapeElement.getNodeName().equals("Circle") ||
                        shapeElement.getNodeName().equals("Triangle") ||
                        shapeElement.getNodeName().equals("RedTriangle")) {


                    int x = Integer.parseInt(shapeElement.getElementsByTagName("x").item(0).getTextContent());
                    int y = Integer.parseInt(shapeElement.getElementsByTagName("y").item(0).getTextContent());
                    int size = Integer.parseInt(shapeElement.getElementsByTagName("size").item(0).getTextContent());
                    String type = shapeElement.getElementsByTagName("type").item(0).getTextContent();
                    Color color = type.equals("redtriangle") ? Color.RED : Color.ORANGE;
                    int angleS =  Integer.parseInt(shapeElement.getElementsByTagName("angleS").item(0).getTextContent());


                    Shape shape = shapeFactory.create(type, x, y, size, color, angleS);
                    if (shape != null) {
                        group.addShape(shape);
                    }
                } else if (shapeElement.getNodeName().equals("Group")) {
                    ShapeGroup subGroup = new ShapeGroup(
                            Integer.parseInt(shapeElement.getElementsByTagName("x").item(0).getTextContent()),
                            Integer.parseInt(shapeElement.getElementsByTagName("y").item(0).getTextContent()),
                            Integer.parseInt(shapeElement.getElementsByTagName("size").item(0).getTextContent()),
                            Color.ORANGE,
                            Integer.parseInt(shapeElement.getElementsByTagName("angleS").item(0).getTextContent())
                    );

                    a = loadGroupShapes(shapeElement, subGroup,a);
                    group.addShape(subGroup);
                }
            }
        }
        return a;
    }
}
