package org.jscience.social.geography.loaders.gml;

import org.jscience.natural.earth.Place;
import org.jscience.natural.earth.PlaceType;
import org.jscience.social.geography.TimedPlace;
import org.jscience.natural.earth.coordinates.EarthCoordinate;
import org.jscience.natural.earth.coordinates.GeodeticCoordinate;
import org.jscience.social.geography.GeoMap;
import org.jscience.social.geography.GeoPath;
import org.jscience.social.geography.GeoPolygon;
import org.jscience.core.mathematics.geometry.boundaries.PointBoundary;


import java.util.ArrayList;
import java.util.List;

/**
 * Bridge for converting GML DTOs to core JScience geography objects.
 * <p>
 * This bridge has been refactored to use standard Place and GeoPath classes,
 * phasing out the social Feature model in favor of a unified geographic representation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GMLBridge {

    /**
     * Converts GML document to JScience GeoMap.
     */
    public GeoMap toGeoMap(GMLDocument gmlDoc) {
        if (gmlDoc == null) return null;
        
        GeoMap map = new GeoMap("GML Map");
        map.setDescription("Imported from GML. SRS: " + gmlDoc.getSrsName());
        
        if (gmlDoc.getFeatures() != null) {
            for (GMLFeature gmlFeature : gmlDoc.getFeatures()) {
                List<Object> results = convertToGeography(gmlFeature);
                for (Object obj : results) {
                    if (obj instanceof Place) {
                        map.addPlace((Place) obj);
                    } else if (obj instanceof GeoPath) {
                        map.addPath((GeoPath) obj);
                    }
                }
            }
        }
        
        return map;
    }


    /**
     * Converts GML feature to a JScience geographic object (Place or GeoPath).
     */
    public List<Object> convertToGeography(GMLFeature gmlFeature) {
        List<Object> results = new ArrayList<>();
        if (gmlFeature == null) return results;
        
        GMLGeometry gmlGeom = gmlFeature.getGeometry();
        String id = gmlFeature.getId();
        String name = gmlFeature.getProperty("name");
        if (name == null) name = id != null ? id : "feature";

        if (gmlGeom instanceof GMLPoint || gmlGeom instanceof GMLPolygon || gmlGeom instanceof GMLMultiPoint || gmlGeom instanceof GMLMultiPolygon) {
            // Check if it should be a TimedPlace
            boolean isTimed = gmlFeature.getProperty("time") != null || 
                             gmlFeature.getProperty("validTime") != null ||
                             gmlFeature.getProperty("timestamp") != null;
            
            Place place = isTimed ? new TimedPlace(name, PlaceType.UNKNOWN) : new Place(name, PlaceType.UNKNOWN);
            
            if (id != null) place.setTrait("gml.id", id);
            place.setTrait("gml.type", gmlFeature.getTypeName());
            
            if (gmlFeature.getProperties() != null) {
                gmlFeature.getProperties().forEach(place::setTrait);
            }

            if (gmlGeom instanceof GMLPoint) {
                GMLPoint pt = (GMLPoint) gmlGeom;
                GeodeticCoordinate coord = new GeodeticCoordinate(pt.getY(), pt.getX(), pt.getZ());
                place.setCenter(coord);
                place.addBoundary(new PointBoundary<EarthCoordinate>(coord));
            } else if (gmlGeom instanceof GMLPolygon) {
                place.addBoundary(convertPolygon((GMLPolygon) gmlGeom));
            } else if (gmlGeom instanceof GMLMultiPoint) {
                List<GMLPoint> pts = ((GMLMultiPoint) gmlGeom).getPoints();
                if (!pts.isEmpty()) {
                    place.setCenter(new GeodeticCoordinate(pts.get(0).getY(), pts.get(0).getX(), pts.get(0).getZ()));
                }
                for (GMLPoint pt : pts) {
                    place.addBoundary(new PointBoundary<EarthCoordinate>(new GeodeticCoordinate(pt.getY(), pt.getX(), pt.getZ())));
                }
            } else if (gmlGeom instanceof GMLMultiPolygon) {
                GeoPolygon multiPoly = new GeoPolygon();
                for (GMLPolygon poly : ((GMLMultiPolygon) gmlGeom).getPolygons()) {
                    GeoPolygon.SimpleGeoPolygon simple = new GeoPolygon.SimpleGeoPolygon();
                    if (poly.getExteriorRing() != null) {
                        simple.setExterior(convertToGeodetic(poly.getExteriorRing()));
                    }
                    if (poly.getInteriorRings() != null) {
                        for (List<double[]> ring : poly.getInteriorRings()) {
                            simple.addInterior(convertToGeodetic(ring));
                        }
                    }
                    multiPoly.addInclusion(simple);
                }
                place.addBoundary(multiPoly);
            }
            results.add(place);
        } else if (gmlGeom instanceof GMLLineString) {
            results.add(convertLineString((GMLLineString) gmlGeom, name));
        } else if (gmlGeom instanceof GMLMultiLineString) {
            GeoPath multiPath = new GeoPath(name);
            for (GMLLineString line : ((GMLMultiLineString) gmlGeom).getLineStrings()) {
                GeoPath.SimpleGeoPath simple = new GeoPath.SimpleGeoPath();
                for (GeodeticCoordinate c : convertToGeodetic(line.getPoints())) {
                    simple.addPoint(c);
                }
                multiPath.addInclusion(simple);
            }
            results.add(multiPath);
        }
        
        return results;
    }


    private GeoPolygon convertPolygon(GMLPolygon gmlPolygon) {
        GeoPolygon poly = new GeoPolygon();
        if (gmlPolygon.getExteriorRing() != null) {
            poly.setExterior(convertToGeodetic(gmlPolygon.getExteriorRing()));
        }
        if (gmlPolygon.getInteriorRings() != null) {
            for (List<double[]> ring : gmlPolygon.getInteriorRings()) {
                poly.addInterior(convertToGeodetic(ring));
            }
        }
        return poly;
    }

    private GeoPath convertLineString(GMLLineString gmlLine, String name) {
        GeoPath path = new GeoPath(name);
        for (GeodeticCoordinate c : convertToGeodetic(gmlLine.getPoints())) {
            path.addPoint(c);
        }
        return path;
    }

    private List<GeodeticCoordinate> convertToGeodetic(List<double[]> points) {
        List<GeodeticCoordinate> coords = new ArrayList<>();
        for (double[] pt : points) {
            coords.add(new GeodeticCoordinate(pt[1], pt[0], pt.length > 2 ? pt[2] : 0));
        }
        return coords;
    }
}


