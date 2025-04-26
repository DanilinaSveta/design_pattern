package org.example.visitor;


import org.example.ShapeFactory;
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

public class XmlDeserializer{
    private final ShapeFactory shapeFactory = new ShapeFactory();
    private final List<Shape> shapes = new ArrayList<>();

    public void loadShapesFromFile(String filePath){
        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList shapeNodes = doc.getElementsByTagName("*");
            int a = 0;
            for (int i = 0; i < shapeNodes.getLength(); i++) {
                a +=1;
                Node node = shapeNodes.item(i);
              //  A1:

                if (node.getNodeType() == Node.ELEMENT_NODE){
                //    System.out.println(a);
                    Element shapeElement = (Element) node;
                   // System.out.println(shapeElement);

                    if ((shapeElement.getNodeName().equals("Circle") ||
                            shapeElement.getNodeName().equals("Square") ||
                            shapeElement.getNodeName().equals("Triangle") ||
                            shapeElement.getNodeName().equals("RedTriangle"))){
                       a = createShapeFromElement(shapeElement, a);

                    }
                    if (shapeElement.getNodeName().equals("Group")){
                        a = createShapeFromElement(shapeElement, a);
                        System.out.println(a);
                        a +=1;
                        i += a;
                      //  break A1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int createShapeFromElement(Element element, int a) {
        String type = element.getNodeName().toLowerCase();
       // System.out.println(type);
        int x = Integer.parseInt(getElementValue(element, "x"));
       // System.out.println(x);
        int y = Integer.parseInt(getElementValue(element, "y"));
        //System.out.println(y);
        int size = Integer.parseInt(getElementValue(element, "size"));
       // System.out.println(size);
        int angleS = Integer.parseInt(getElementValue(element, "angleS"));
       // System.out.println(angleS);
        Color color = determineColor(type);
       // System.out.println(color);
        a = 0;
        if (type.equals("group")) {
            ShapeGroup group = (ShapeGroup) shapeFactory.create(type, x, y, size, color, angleS);
            // Рекурсивно добавляем фигуры в группу
            NodeList childNodes = element.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                a+=1;

                Node node = childNodes.item(j);
                if (childNodes.item(j) instanceof Element ) {
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element shapeElementChild = (Element) node;
                        if (!shapeElementChild.getNodeName().equals("Shapes") &
                                (shapeElementChild.getNodeName().equals("Circle") ||
                                        shapeElementChild.getNodeName().equals("Square") ||
                                        shapeElementChild.getNodeName().equals("Triangle") ||
                                        shapeElementChild.getNodeName().equals("RedTriangle") )) {
                            a = createShapeFromElement(shapeElementChild, group, a);
                        }
                        if (node.getNodeName().equals("Group")){
                            a = createShapeFromElement(shapeElementChild, a);
                            j+=a;
                        }
                    }
                }
            }
            shapes.add(group);
        } else {
            Shape newShape = shapeFactory.create(type, x, y, size, color, angleS);
            shapes.add(newShape);
        }
        return a;
    }

    private int createShapeFromElement(Element element, ShapeGroup group, int a) {
        a +=1;
        String type = element.getNodeName().toLowerCase();
        int x = Integer.parseInt(getElementValue(element, "x"));
        int y = Integer.parseInt(getElementValue(element, "y"));
        int size = Integer.parseInt(getElementValue(element, "size"));
        int angleS = Integer.parseInt(getElementValue(element, "angleS"));
        Color color = determineColor(type);

        Shape newShape = shapeFactory.create(type, x, y, size, color, angleS);
        group.addShape(newShape);
        return a;
    }

    private String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return nodeList.getLength() > 0 ? nodeList.item(0).getTextContent() : "0";
    }

    private Color determineColor(String type) {
        switch (type) {
            case "redtriangle":
                return Color.RED;
            default:
                return Color.ORANGE;
        }
    }

    public List<Shape> getShapes() {
        return shapes;
    }

}
