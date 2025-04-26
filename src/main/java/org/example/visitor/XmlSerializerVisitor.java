package org.example.visitor;

import org.example.shape.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.example.shape.Circle;

import java.io.File;
import java.io.IOException;

public class XmlSerializerVisitor implements Visitor {
    private Document document;
    private Element rootElement;

    public XmlSerializerVisitor(String rootElementName) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            document = docBuilder.newDocument();
            rootElement = document.createElement(rootElementName);
            document.appendChild(rootElement);;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(Square square) throws IOException {
        Element squareElement = document.createElement("Square");
        addCommonAttributes(squareElement, square);
        rootElement.appendChild(squareElement);
    }


    @Override
    public void visit(Triangle triangle) throws IOException {
        Element triangleElement = document.createElement("Triangle");
        addCommonAttributes(triangleElement, triangle);
        rootElement.appendChild(triangleElement);
    }

    @Override
    public void visit(Circle circle) throws IOException {
        Element circleElement = document.createElement("Circle");
        addCommonAttributes(circleElement, circle);
        rootElement.appendChild(circleElement);
    }

    @Override
    public void visit(ShapeGroup shapeGroup) throws IOException {
        Element groupElement = document.createElement("Group");
        addCommonAttributes(groupElement, shapeGroup);
        for (Shape shape : shapeGroup.getShapes()) {
            shape.accept(this); // Визит для каждого из вложенных объектов
            Element shapeElement  = (Element) document.getDocumentElement().getLastChild();
            groupElement.appendChild(shapeElement);
        }
        rootElement.appendChild(groupElement);
    }

    @Override
    public void visit(RedTriangle redTriangle) throws IOException {
        Element circleElement = document.createElement("RedTriangle");
        appendChildWithText(circleElement, "type", redTriangle.type);
        appendChildWithText(circleElement, "x", String.valueOf(redTriangle.getX()));
        appendChildWithText(circleElement, "y", String.valueOf(redTriangle.getY()));
        appendChildWithText(circleElement, "size", String.valueOf(redTriangle.getSize()));
        appendChildWithText(circleElement, "angleS", String.valueOf(redTriangle.getAngleS()));
        rootElement.appendChild(circleElement);
    }

    private void addCommonAttributes(Element element, Shape shape) {
        appendChildWithText(element, "type", shape.type);
        appendChildWithText(element,"x", String.valueOf(shape.x));
        appendChildWithText(element,"y", String.valueOf(shape.y));
        appendChildWithText(element,"size", String.valueOf(shape.size));
        appendChildWithText(element,"angleS", String.valueOf(shape.angleS));
    }

    private void appendChildWithText(Element parent, String childName, String textValue) {
        Element child = document.createElement(childName);
        child.setTextContent(textValue);
        parent.appendChild(child);
    }

    public void saveToFile(String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
