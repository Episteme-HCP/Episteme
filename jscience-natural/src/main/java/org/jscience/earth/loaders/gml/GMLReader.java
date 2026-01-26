/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.earth.loaders.gml;

import org.jscience.io.AbstractResourceReader;

import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.*;

/**
 * GML Reader for Geography Markup Language data.
 * <p>
 * GML is an OGC (Open Geospatial Consortium) standard XML encoding for
 * geographic features including points, lines, polygons, and complex geometries.
 * </p>
 * <p>
 * <b>Supported GML 3.2 Elements:</b>
 * <ul>
 *   <li>Feature collections</li>
 *   <li>Points, LineStrings, Polygons</li>
 *   <li>MultiPoint, MultiLineString, MultiPolygon</li>
 *   <li>Coordinate reference systems</li>
 *   <li>Feature properties and attributes</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://www.ogc.org/standards/gml">OGC GML Standard</a>
 */
public class GMLReader extends AbstractResourceReader<GMLDocument> {

    private static final String GML_NS = "http://www.opengis.net/gml/3.2";
    private static final String GML_NS_OLD = "http://www.opengis.net/gml";

    public GMLReader() {
    }

    @Override public String getResourcePath() { return null; }
    @Override public Class<GMLDocument> getResourceType() { return GMLDocument.class; }
    @Override public String getName() { return "GML Reader"; }
    @Override public String getDescription() { return "Reads geographic features from GML format"; }
    @Override public String getLongDescription() { return "GML is the OGC standard for geographic feature encoding including points, lines, polygons, and complex geometries."; }
    @Override public String getCategory() { return "Geography"; }
    @Override public String[] getSupportedVersions() { return new String[] {"3.2", "3.1", "2.1"}; }

    @Override
    protected GMLDocument loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) return read(file);
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) return read(is);
        }
        throw new GMLException("Resource not found: " + resourceId);
    }

    @Override
    protected GMLDocument loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    /**
     * Reads GML data from an input stream.
     */
    public GMLDocument read(InputStream input) throws GMLException {
        try {
            DocumentBuilder builder = org.jscience.io.SecureXMLFactory.createSecureDocumentBuilder();
            Document doc = builder.parse(input);
            return parseDocument(doc);
        } catch (Exception e) {
            throw new GMLException("Failed to parse GML", e);
        }
    }

    /**
     * Reads GML data from a file.
     */
    public GMLDocument read(File file) throws GMLException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis);
        } catch (IOException e) {
            throw new GMLException("Failed to read file: " + file, e);
        }
    }

    private GMLDocument parseDocument(Document doc) {
        GMLDocument result = new GMLDocument();
        Element root = doc.getDocumentElement();
        
        result.setSrsName(root.getAttribute("srsName"));
        
        // Parse feature members
        parseFeatureMembers(root, result);
        
        return result;
    }

    private void parseFeatureMembers(Element root, GMLDocument result) {
        // Try gml:featureMember
        NodeList members = root.getElementsByTagNameNS(GML_NS, "featureMember");
        if (members.getLength() == 0) {
            members = root.getElementsByTagNameNS(GML_NS_OLD, "featureMember");
        }
        
        for (int i = 0; i < members.getLength(); i++) {
            Element memberElem = (Element) members.item(i);
            // The actual feature is the first child element
            NodeList children = memberElem.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                if (children.item(j) instanceof Element) {
                    result.addFeature(parseFeature((Element) children.item(j)));
                    break;
                }
            }
        }
        
        // Try gml:featureMembers (plural)
        NodeList membersList = root.getElementsByTagNameNS(GML_NS, "featureMembers");
        if (membersList.getLength() == 0) {
            membersList = root.getElementsByTagNameNS(GML_NS_OLD, "featureMembers");
        }
        
        for (int i = 0; i < membersList.getLength(); i++) {
            Element membersElem = (Element) membersList.item(i);
            NodeList children = membersElem.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                if (children.item(j) instanceof Element) {
                    result.addFeature(parseFeature((Element) children.item(j)));
                }
            }
        }
    }

    private GMLFeature parseFeature(Element elem) {
        GMLFeature feature = new GMLFeature();
        feature.setId(elem.getAttributeNS(GML_NS, "id"));
        if (feature.getId() == null || feature.getId().isEmpty()) {
            feature.setId(elem.getAttribute("gml:id"));
        }
        feature.setTypeName(elem.getLocalName());
        
        // Parse properties and geometry
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element child = (Element) children.item(i);
                String localName = child.getLocalName();
                
                // Check if it's a geometry
                GMLGeometry geom = tryParseGeometry(child);
                if (geom != null) {
                    feature.setGeometry(geom);
                } else {
                    // It's a property
                    feature.setProperty(localName, child.getTextContent().trim());
                }
            }
        }
        
        return feature;
    }

    private GMLGeometry tryParseGeometry(Element elem) {
        String localName = elem.getLocalName();
        if (localName == null) return null;
        
        switch (localName) {
            case "Point":
                return parsePoint(elem);
            case "LineString":
                return parseLineString(elem);
            case "Polygon":
                return parsePolygon(elem);
            case "MultiPoint":
                return parseMultiPoint(elem);
            case "MultiLineString":
            case "MultiCurve":
                return parseMultiLineString(elem);
            case "MultiPolygon":
            case "MultiSurface":
                return parseMultiPolygon(elem);
            default:
                // Check for nested geometry in property elements
                NodeList children = elem.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    if (children.item(i) instanceof Element) {
                        GMLGeometry nested = tryParseGeometry((Element) children.item(i));
                        if (nested != null) return nested;
                    }
                }
                return null;
        }
    }

    private GMLPoint parsePoint(Element elem) {
        GMLPoint point = new GMLPoint();
        point.setSrsName(elem.getAttribute("srsName"));
        
        // Try pos
        Element posElem = getFirstChildElement(elem, "pos");
        if (posElem != null) {
            double[] coords = parseCoordinateString(posElem.getTextContent());
            if (coords.length >= 2) {
                point.setX(coords[0]);
                point.setY(coords[1]);
                if (coords.length >= 3) point.setZ(coords[2]);
            }
        }
        
        // Try coordinates
        Element coordsElem = getFirstChildElement(elem, "coordinates");
        if (coordsElem != null) {
            double[] coords = parseCoordinateString(coordsElem.getTextContent().replace(",", " "));
            if (coords.length >= 2) {
                point.setX(coords[0]);
                point.setY(coords[1]);
                if (coords.length >= 3) point.setZ(coords[2]);
            }
        }
        
        return point;
    }

    private GMLLineString parseLineString(Element elem) {
        GMLLineString line = new GMLLineString();
        line.setSrsName(elem.getAttribute("srsName"));
        
        // Try posList
        Element posListElem = getFirstChildElement(elem, "posList");
        if (posListElem != null) {
            String srsDim = posListElem.getAttribute("srsDimension");
            int dim = srsDim.isEmpty() ? 2 : Integer.parseInt(srsDim);
            double[] coords = parseCoordinateString(posListElem.getTextContent());
            
            for (int i = 0; i + dim <= coords.length; i += dim) {
                double x = coords[i];
                double y = coords[i + 1];
                double z = dim >= 3 && i + 2 < coords.length ? coords[i + 2] : 0;
                line.addPoint(x, y, z);
            }
        }
        
        // Try coordinates
        Element coordsElem = getFirstChildElement(elem, "coordinates");
        if (coordsElem != null) {
            String[] tuples = coordsElem.getTextContent().trim().split("\\s+");
            for (String tuple : tuples) {
                String[] parts = tuple.split(",");
                if (parts.length >= 2) {
                    double x = Double.parseDouble(parts[0]);
                    double y = Double.parseDouble(parts[1]);
                    double z = parts.length >= 3 ? Double.parseDouble(parts[2]) : 0;
                    line.addPoint(x, y, z);
                }
            }
        }
        
        return line;
    }

    private GMLPolygon parsePolygon(Element elem) {
        GMLPolygon polygon = new GMLPolygon();
        polygon.setSrsName(elem.getAttribute("srsName"));
        
        // Exterior ring
        Element exterior = getFirstChildElement(elem, "exterior");
        if (exterior != null) {
            Element ring = getFirstChildElement(exterior, "LinearRing");
            if (ring != null) {
                GMLLineString extRing = parseLineString(ring);
                polygon.setExteriorRing(extRing.getPoints());
            }
        }
        
        // Interior rings (holes)
        NodeList interiors = elem.getElementsByTagNameNS(GML_NS, "interior");
        for (int i = 0; i < interiors.getLength(); i++) {
            Element interior = (Element) interiors.item(i);
            Element ring = getFirstChildElement(interior, "LinearRing");
            if (ring != null) {
                GMLLineString intRing = parseLineString(ring);
                polygon.addInteriorRing(intRing.getPoints());
            }
        }
        
        return polygon;
    }

    private GMLMultiPoint parseMultiPoint(Element elem) {
        GMLMultiPoint multi = new GMLMultiPoint();
        NodeList members = elem.getElementsByTagNameNS(GML_NS, "pointMember");
        for (int i = 0; i < members.getLength(); i++) {
            Element member = (Element) members.item(i);
            Element point = getFirstChildElement(member, "Point");
            if (point != null) {
                multi.addPoint(parsePoint(point));
            }
        }
        return multi;
    }

    private GMLMultiLineString parseMultiLineString(Element elem) {
        GMLMultiLineString multi = new GMLMultiLineString();
        NodeList members = elem.getElementsByTagNameNS(GML_NS, "lineStringMember");
        if (members.getLength() == 0) {
            members = elem.getElementsByTagNameNS(GML_NS, "curveMember");
        }
        for (int i = 0; i < members.getLength(); i++) {
            Element member = (Element) members.item(i);
            Element line = getFirstChildElement(member, "LineString");
            if (line != null) {
                multi.addLineString(parseLineString(line));
            }
        }
        return multi;
    }

    private GMLMultiPolygon parseMultiPolygon(Element elem) {
        GMLMultiPolygon multi = new GMLMultiPolygon();
        NodeList members = elem.getElementsByTagNameNS(GML_NS, "polygonMember");
        if (members.getLength() == 0) {
            members = elem.getElementsByTagNameNS(GML_NS, "surfaceMember");
        }
        for (int i = 0; i < members.getLength(); i++) {
            Element member = (Element) members.item(i);
            Element polygon = getFirstChildElement(member, "Polygon");
            if (polygon != null) {
                multi.addPolygon(parsePolygon(polygon));
            }
        }
        return multi;
    }

    private double[] parseCoordinateString(String text) {
        if (text == null || text.trim().isEmpty()) return new double[0];
        String[] parts = text.trim().split("\\s+");
        double[] result = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                result[i] = Double.parseDouble(parts[i]);
            } catch (NumberFormatException e) {
                result[i] = 0;
            }
        }
        return result;
    }

    private Element getFirstChildElement(Element parent, String localName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element elem = (Element) children.item(i);
                if (localName.equals(elem.getLocalName())) {
                    return elem;
                }
            }
        }
        return null;
    }
}
