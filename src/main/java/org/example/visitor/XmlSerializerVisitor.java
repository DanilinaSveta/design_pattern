package org.example.visitor;

import org.example.shape.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.example.shape.Circle;

import java.io.File;
import java.io.IOException;

public class XmlSerializerVisitor implements Visitor {
    private Document doc;
    private Element rootElement;

    public XmlSerializerVisitor() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.newDocument();
            rootElement = doc.createElement("shapes");
            doc.appendChild(rootElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveToXML(String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Включение отступов
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    private void appendChildWithText(Element parent, String childName, String textValue) {
        Element child = doc.createElement(childName);
        child.setTextContent(textValue);
        parent.appendChild(child);
    }

    @Override
    public void visit(Square square) throws IOException {
        Element squareElement = doc.createElement("Square");
        appendChildWithText(squareElement, "type", square.type);
        appendChildWithText(squareElement, "x", String.valueOf(square.x));
        appendChildWithText(squareElement, "y", String.valueOf(square.y));
        appendChildWithText(squareElement, "size", String.valueOf(square.size));
        appendChildWithText(squareElement, "angleS", String.valueOf(square.angleS));
        rootElement.appendChild(squareElement);

    }


    @Override
    public void visit(Triangle triangle) throws IOException {
        Element triangleElement = doc.createElement("Triangle");
        appendChildWithText(triangleElement, "type", triangle.type);
        appendChildWithText(triangleElement, "x", String.valueOf(triangle.x));
        appendChildWithText(triangleElement, "y", String.valueOf(triangle.y));
        appendChildWithText(triangleElement, "size", String.valueOf(triangle.size));
        appendChildWithText(triangleElement, "angleS", String.valueOf(triangle.angleS));
        rootElement.appendChild(triangleElement);
    }

    @Override
    public void visit(Circle circle) throws IOException {
        Element circleElement = doc.createElement("Circle");
        appendChildWithText(circleElement, "type", circle.type);
        appendChildWithText(circleElement, "x", String.valueOf(circle.x));
        appendChildWithText(circleElement, "y", String.valueOf(circle.y));
        appendChildWithText(circleElement, "size", String.valueOf(circle.size));
        appendChildWithText(circleElement, "angleS", String.valueOf(circle.angleS));
        rootElement.appendChild(circleElement);
    }

    @Override
    public void visit(ShapeGroup shapeGroup) throws IOException {
        Element groupElement = doc.createElement("ShapeGroup");
        appendChildWithText(groupElement, "type", shapeGroup.type);
        appendChildWithText(groupElement, "x", String.valueOf(shapeGroup.x));
        appendChildWithText(groupElement, "y", String.valueOf(shapeGroup.y));
        appendChildWithText(groupElement, "size", String.valueOf(shapeGroup.size));
        appendChildWithText(groupElement, "angleS", String.valueOf(shapeGroup.angleS));

        for (Shape shape : shapeGroup.getShapes()) {
            shape.accept(this);
            Element shapeElement = (Element) doc.getDocumentElement().getLastChild();
            groupElement.appendChild(shapeElement);
        }
        rootElement.appendChild(groupElement);
    }
}
