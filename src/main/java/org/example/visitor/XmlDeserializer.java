package org.example.visitor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.ShapeFactory;
import org.example.shape.Circle;
import org.example.shape.Shape;
import org.example.shape.Square;
import org.example.shape.Triangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlDeserializer{
    static XmlMapper xmlMapper = new XmlMapper();

//    public static void saveShapesToXml (List<org.example.shape.Shape> shapes, String filename) {
//        try {
//            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), shapes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static List<Shape> readShapeFromXml (String filePath,List<Shape> shapes, ShapeFactory SHAPE_FACTORY){

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(filePath));
            doc.getDocumentElement().normalize();
            NodeList shapeNodes = doc.getElementsByTagName("*");

            for (int i = 0; i < shapeNodes.getLength(); i++) {
                Node node = shapeNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.getNodeName().equals("Circle") || element.getNodeName().equals("Square") || element.getNodeName().equals("Triangle")) {
                            shapes.add(SHAPE_FACTORY.create(element.getNodeName().toLowerCase(),
                                    Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent()),
                                    Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent()),
                                    Integer.parseInt(element.getElementsByTagName("size").item(0).getTextContent()),
                                    Color.ORANGE,
                                    Integer.parseInt(element.getElementsByTagName("angleS").item(0).getTextContent())));
                    }
                    if (element.getNodeName().equals("ShapeGroup")){
                        Shape shapeGroup = SHAPE_FACTORY.create("group",
                                Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent()),
                                Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent()),
                                Integer.parseInt(element.getElementsByTagName("size").item(0).getTextContent()),
                                Color.ORANGE,
                                Integer.parseInt(element.getElementsByTagName("angleS").item(0).getTextContent()));
                        NodeList innerShapes = element.getChildNodes();
                        for (int j = 0; j < innerShapes.getLength(); j++) {
                            Node innerNode = innerShapes.item(j);
                            if (innerNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element innerElement = (Element) innerNode;
                                if (innerElement.getNodeName().equals("Circle") || innerElement.getNodeName().equals("Square") || innerElement.getNodeName().equals("Triangle")) {
                                    shapeGroup.addShape(SHAPE_FACTORY.create(innerElement.getNodeName().toLowerCase(),
                                            Integer.parseInt(innerElement.getElementsByTagName("x").item(0).getTextContent()),
                                            Integer.parseInt(innerElement.getElementsByTagName("y").item(0).getTextContent()),
                                            Integer.parseInt(innerElement.getElementsByTagName("size").item(0).getTextContent()),
                                            Color.ORANGE,
                                            Integer.parseInt(innerElement.getElementsByTagName("angleS").item(0).getTextContent())));
                                }
                            }
                        }
                        shapes.add(shapeGroup);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shapes;
    }
}
