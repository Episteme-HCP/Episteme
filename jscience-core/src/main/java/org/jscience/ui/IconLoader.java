package org.jscience.ui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads Lucide SVG icons (or simple SVGs) into JavaFX Nodes.
 * Handles common SVG shapes: path, circle, rect, line, polyline, polygon.
 */
public class IconLoader {

    public static Node getIcon(String name) {
        return getIcon(name, 16); // Default size
    }

    public static Node getIcon(String name, int size) {
        try {
            // Load SVG from resources
            String path = "/org/jscience/ui/icons/" + name + ".svg";
            InputStream is = IconLoader.class.getResourceAsStream(path);
            if (is == null) {
                System.err.println("Icon not found: " + path);
                return new javafx.scene.shape.Rectangle(size, size, Color.TRANSPARENT); // Placeholder
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element root = doc.getDocumentElement();

            Group iconGroup = new Group();
            List<Node> shapes = parseElement(root);
            iconGroup.getChildren().addAll(shapes);

            // Lucide icons are usually 24x24 viewBox.
            // Scale to requested size.
            double scale = size / 24.0;
            iconGroup.setScaleX(scale);
            iconGroup.setScaleY(scale);

            // Group doesn't have intrinsic size, wrapping in a Pane is safer for layout?
            // But Group is fine for Graphic.
            return iconGroup;

        } catch (Exception e) {
            e.printStackTrace();
            return new javafx.scene.shape.Rectangle(size, size, Color.RED);
        }
    }

    private static List<Node> parseElement(Element element) {
        List<Node> nodes = new ArrayList<>();
        NodeList children = element.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            org.w3c.dom.Node child = children.item(i);
            if (child.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element tag = (Element) child;
                Node shape = createShape(tag);
                if (shape != null) {
                    applyStyles(shape, tag);
                    nodes.add(shape);
                } else if (tag.getTagName().equals("g")) {
                    nodes.addAll(parseElement(tag));
                }
            }
        }
        return nodes;
    }

    private static Node createShape(Element tag) {
        String name = tag.getTagName();
        switch (name) {
            case "path":
                SVGPath path = new SVGPath();
                path.setContent(tag.getAttribute("d"));
                return path;
            case "circle":
                return new Circle(
                        parse(tag.getAttribute("cx")),
                        parse(tag.getAttribute("cy")),
                        parse(tag.getAttribute("r")));
            case "rect":
                Rectangle rect = new Rectangle(
                        parse(tag.getAttribute("x")),
                        parse(tag.getAttribute("y")),
                        parse(tag.getAttribute("width")),
                        parse(tag.getAttribute("height")));
                String rx = tag.getAttribute("rx");
                String ry = tag.getAttribute("ry");
                if (!rx.isEmpty())
                    rect.setArcWidth(parse(rx) * 2);
                if (!ry.isEmpty())
                    rect.setArcHeight(parse(ry) * 2);
                return rect;
            case "line":
                return new Line(
                        parse(tag.getAttribute("x1")),
                        parse(tag.getAttribute("y1")),
                        parse(tag.getAttribute("x2")),
                        parse(tag.getAttribute("y2")));
            case "polyline":
                Polyline polyline = new Polyline();
                parsePoints(tag.getAttribute("points"), polyline.getPoints());
                return polyline;
            case "polygon":
                Polygon polygon = new Polygon();
                parsePoints(tag.getAttribute("points"), polygon.getPoints());
                return polygon;
            default:
                return null;
        }
    }

    private static void parsePoints(String pointsObj, List<Double> list) {
        if (pointsObj == null || pointsObj.isEmpty())
            return;
        String[] pairs = pointsObj.trim().split("\\s+");
        for (String pair : pairs) {
            // Handle "x,y" or "x y"
            String[] coords = pair.split("[,\\s]+");
            for (String c : coords) {
                if (!c.isEmpty())
                    list.add(Double.parseDouble(c));
            }
        }
    }

    private static double parse(String val) {
        if (val == null || val.isEmpty())
            return 0;
        return Double.parseDouble(val);
    }

    private static void applyStyles(Node shape, Element tag) {
        if (shape instanceof Shape) {
            Shape s = (Shape) shape;
            s.setFill(Color.TRANSPARENT); // Lucide default
            s.setStroke(Color.BLACK); // Default, logic should use css 'currentColor' ideally
            // But JavaFX doesn't support currentColor easy. We set a StyleClass.
            s.getStyleClass().add("icon-shape");

            s.setStrokeWidth(2); // Lucide default
            if (tag.hasAttribute("stroke-width")) {
                s.setStrokeWidth(parse(tag.getAttribute("stroke-width")));
            }

            // Lucide usually "round" caps/joins
            s.setStrokeLineCap(StrokeLineCap.ROUND);
            s.setStrokeLineJoin(StrokeLineJoin.ROUND);
        }
    }
}
