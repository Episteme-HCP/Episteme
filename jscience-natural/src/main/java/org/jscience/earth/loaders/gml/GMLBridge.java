/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

import org.jscience.geography.Coordinate;
import org.jscience.geography.Feature;
import org.jscience.geography.FeatureCollection;
import org.jscience.geography.geometry.Point;
import org.jscience.geography.geometry.LineString;
import org.jscience.geography.geometry.Polygon;
import org.jscience.geography.geometry.Geometry;
import org.jscience.geography.geometry.MultiPoint;
import org.jscience.geography.geometry.MultiLineString;
import org.jscience.geography.geometry.MultiPolygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridge for converting GML DTOs to core JScience geography objects.
 * <p>
 * GML (Geography Markup Language) is the OGC standard for geographic data.
 * This bridge converts parsed GML structures (from GMLReader) to the core
 * JScience geography domain.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * GML XML → GMLReader → GML DTOs → GMLBridge → Core JScience Objects
 *                       (GMLDocument,            (Feature,
 *                        GMLFeature,              FeatureCollection,
 *                        GMLPoint, etc.)          Point, Polygon, etc.)
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see GMLReader
 * @see GMLDocument
 */
public class GMLBridge {

    /**
     * Converts GML document to JScience FeatureCollection.
     *
     * @param gmlDoc the parsed GML document from GMLReader
     * @return a FeatureCollection with all features and geometries
     */
    public FeatureCollection toFeatureCollection(GMLDocument gmlDoc) {
        if (gmlDoc == null) {
            return null;
        }
        
        FeatureCollection fc = new FeatureCollection("GML");
        fc.setTrait("gml.srs", gmlDoc.getSrsName());
        
        // Convert each GML feature to JScience Feature
        if (gmlDoc.getFeatures() != null) {
            for (GMLFeature gmlFeature : gmlDoc.getFeatures()) {
                Feature feature = convertFeature(gmlFeature);
                if (feature != null) {
                    fc.addFeature(feature);
                }
            }
        }
        
        return fc;
    }

    /**
     * Converts GML feature to JScience Feature.
     *
     * @param gmlFeature the GML feature DTO
     * @return a Feature with geometry and properties
     */
    public Feature convertFeature(GMLFeature gmlFeature) {
        if (gmlFeature == null) {
            return null;
        }
        
        String id = gmlFeature.getId();
        Feature feature = new Feature(id != null ? id : "unknown");
        
        feature.setTrait("gml.id", id);
        feature.setTrait("gml.type", gmlFeature.getTypeName());
        
        // Convert geometry
        if (gmlFeature.getGeometry() != null) {
            Geometry geom = convertGeometry(gmlFeature.getGeometry());
            feature.setGeometry(geom);
        }
        
        // Transfer all properties
        if (gmlFeature.getProperties() != null) {
            for (String key : gmlFeature.getProperties().keySet()) {
                feature.setTrait(key, gmlFeature.getProperty(key));
            }
        }
        
        return feature;
    }

    /**
     * Converts GML geometry to JScience Geometry.
     *
     * @param gmlGeom the GML geometry DTO
     * @return a JScience Geometry subtype
     */
    public Geometry convertGeometry(GMLGeometry gmlGeom) {
        if (gmlGeom == null) {
            return null;
        }
        
        if (gmlGeom instanceof GMLPoint) {
            return convertPoint((GMLPoint) gmlGeom);
        } else if (gmlGeom instanceof GMLLineString) {
            return convertLineString((GMLLineString) gmlGeom);
        } else if (gmlGeom instanceof GMLPolygon) {
            return convertPolygon((GMLPolygon) gmlGeom);
        } else if (gmlGeom instanceof GMLMultiPoint) {
            return convertMultiPoint((GMLMultiPoint) gmlGeom);
        } else if (gmlGeom instanceof GMLMultiLineString) {
            return convertMultiLineString((GMLMultiLineString) gmlGeom);
        } else if (gmlGeom instanceof GMLMultiPolygon) {
            return convertMultiPolygon((GMLMultiPolygon) gmlGeom);
        }
        
        return null;
    }

    /**
     * Converts GML Point to JScience Point.
     */
    public Point convertPoint(GMLPoint gmlPoint) {
        if (gmlPoint == null) {
            return null;
        }
        
        Coordinate coord = new Coordinate(gmlPoint.getX(), gmlPoint.getY(), gmlPoint.getZ());
        Point point = new Point(coord);
        point.setTrait("gml.srs", gmlPoint.getSrsName());
        return point;
    }

    /**
     * Converts GML LineString to JScience LineString.
     */
    public LineString convertLineString(GMLLineString gmlLine) {
        if (gmlLine == null || gmlLine.getPoints() == null) {
            return null;
        }
        
        List<Coordinate> coords = new ArrayList<>();
        for (double[] pt : gmlLine.getPoints()) {
            double x = pt.length > 0 ? pt[0] : 0;
            double y = pt.length > 1 ? pt[1] : 0;
            double z = pt.length > 2 ? pt[2] : 0;
            coords.add(new Coordinate(x, y, z));
        }
        
        LineString line = new LineString(coords);
        line.setTrait("gml.srs", gmlLine.getSrsName());
        return line;
    }

    /**
     * Converts GML Polygon to JScience Polygon.
     */
    public Polygon convertPolygon(GMLPolygon gmlPolygon) {
        if (gmlPolygon == null) {
            return null;
        }
        
        // Convert exterior ring
        List<Coordinate> exteriorCoords = new ArrayList<>();
        if (gmlPolygon.getExteriorRing() != null) {
            for (double[] pt : gmlPolygon.getExteriorRing()) {
                exteriorCoords.add(new Coordinate(pt[0], pt[1], pt.length > 2 ? pt[2] : 0));
            }
        }
        LineString exterior = new LineString(exteriorCoords);
        
        // Convert interior rings (holes)
        List<LineString> interiors = new ArrayList<>();
        if (gmlPolygon.getInteriorRings() != null) {
            for (List<double[]> ring : gmlPolygon.getInteriorRings()) {
                List<Coordinate> intCoords = new ArrayList<>();
                for (double[] pt : ring) {
                    intCoords.add(new Coordinate(pt[0], pt[1], pt.length > 2 ? pt[2] : 0));
                }
                interiors.add(new LineString(intCoords));
            }
        }
        
        Polygon polygon = new Polygon(exterior, interiors);
        polygon.setTrait("gml.srs", gmlPolygon.getSrsName());
        return polygon;
    }

    /**
     * Converts GML MultiPoint to JScience MultiPoint.
     */
    public MultiPoint convertMultiPoint(GMLMultiPoint gmlMulti) {
        if (gmlMulti == null) {
            return null;
        }
        
        List<Point> points = new ArrayList<>();
        if (gmlMulti.getPoints() != null) {
            for (GMLPoint gmlPt : gmlMulti.getPoints()) {
                Point pt = convertPoint(gmlPt);
                if (pt != null) {
                    points.add(pt);
                }
            }
        }
        
        return new MultiPoint(points);
    }

    /**
     * Converts GML MultiLineString to JScience MultiLineString.
     */
    public MultiLineString convertMultiLineString(GMLMultiLineString gmlMulti) {
        if (gmlMulti == null) {
            return null;
        }
        
        List<LineString> lines = new ArrayList<>();
        if (gmlMulti.getLineStrings() != null) {
            for (GMLLineString gmlLine : gmlMulti.getLineStrings()) {
                LineString line = convertLineString(gmlLine);
                if (line != null) {
                    lines.add(line);
                }
            }
        }
        
        return new MultiLineString(lines);
    }

    /**
     * Converts GML MultiPolygon to JScience MultiPolygon.
     */
    public MultiPolygon convertMultiPolygon(GMLMultiPolygon gmlMulti) {
        if (gmlMulti == null) {
            return null;
        }
        
        List<Polygon> polygons = new ArrayList<>();
        if (gmlMulti.getPolygons() != null) {
            for (GMLPolygon gmlPoly : gmlMulti.getPolygons()) {
                Polygon poly = convertPolygon(gmlPoly);
                if (poly != null) {
                    polygons.add(poly);
                }
            }
        }
        
        return new MultiPolygon(polygons);
    }

    /**
     * Extracts all features from a GML document as a list.
     *
     * @param gmlDoc the parsed GML document
     * @return list of JScience Feature objects
     */
    public List<Feature> extractFeatures(GMLDocument gmlDoc) {
        List<Feature> features = new ArrayList<>();
        if (gmlDoc != null && gmlDoc.getFeatures() != null) {
            for (GMLFeature gmlFeature : gmlDoc.getFeatures()) {
                Feature f = convertFeature(gmlFeature);
                if (f != null) {
                    features.add(f);
                }
            }
        }
        return features;
    }
}
