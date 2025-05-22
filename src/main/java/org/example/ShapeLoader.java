package org.example;

import org.example.shape.Shape;
import org.example.shape.ShapeGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShapeLoader {
    private ShapeFactory shapeFactory;

    public ShapeLoader(){
        shapeFactory = new ShapeFactory();
    }

    public List<Shape> loadShapesFromFile(String filePath) {
        List<Shape> shapes = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new File(filePath));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            for (int i = 0;  i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (node instanceof Element){
                    Element elem = (Element) node;
                    String nodeName = elem.getNodeName();

                    if (nodeName.equalsIgnoreCase("Square") ||
                            nodeName.equalsIgnoreCase("Circle") ||
                            nodeName.equalsIgnoreCase("Triangle") ||
                            nodeName.equalsIgnoreCase("RedTriangle")){
                        Shape shape = createShapeFromElement(elem);
                        if (shape != null){
                            shapes.add(shape);
                        }
                    }  else if (nodeName.equalsIgnoreCase("Group")) {
                        ShapeGroup group = createGroupFromElement(elem);
                        shapes.add(group);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return shapes;
    }
    private Shape createShapeFromElement(Element elem) {
        try {
            String type = elem.getElementsByTagName("type").item(0).getTextContent();
            int x = Integer.parseInt(elem.getElementsByTagName("x").item(0).getTextContent());
            int y = Integer.parseInt(elem.getElementsByTagName("y").item(0).getTextContent());
            int size = Integer.parseInt(elem.getElementsByTagName("size").item(0).getTextContent());
            int angleS = Integer.parseInt(elem.getElementsByTagName("angleS").item(0).getTextContent());

            Color color = Color.ORANGE;
            if (type.equalsIgnoreCase("redtriangle")) {
                color = Color.RED;
            }

            return shapeFactory.create(type, x, y, size, color, angleS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private ShapeGroup createGroupFromElement(Element groupElem) {
        try {
            int x = Integer.parseInt(groupElem.getElementsByTagName("x").item(0).getTextContent());
            int y = Integer.parseInt(groupElem.getElementsByTagName("y").item(0).getTextContent());
            int size = Integer.parseInt(groupElem.getElementsByTagName("size").item(0).getTextContent());
            int angleS = Integer.parseInt(groupElem.getElementsByTagName("angleS").item(0).getTextContent());

            ShapeGroup group = new ShapeGroup(x, y, size, Color.ORANGE, angleS);

            NodeList children = groupElem.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node instanceof Element) {
                    Element childElem = (Element) node;
                    String childName = childElem.getNodeName();

                    if (childName.equalsIgnoreCase("Square") ||
                            childName.equalsIgnoreCase("Circle")
                            || childName.equalsIgnoreCase("Triangle") ||
                            childName.equalsIgnoreCase("RedTriangle")) {
                        Shape shape = createShapeFromElement(childElem);
                        if (shape != null) {
                            group.addShape(shape);
                        }
                    } else if (childName.equalsIgnoreCase("Group")) {
                        ShapeGroup subGroup = createGroupFromElement(childElem);
                        group.addShape(subGroup);
                    }
                }
            }
            return group;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
