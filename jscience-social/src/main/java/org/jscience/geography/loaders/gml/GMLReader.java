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

package org.jscience.geography.loaders.gml;

import org.jscience.geography.Boundary;
import org.jscience.geography.Coordinate;
import org.jscience.geography.Place;
import org.jscience.earth.loaders.gml.*;
import org.jscience.io.AbstractResourceReader;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Geography Markup Language (GML) Reader for JScience Social.
 * <p>
 * This reader parses GML files and converts them into {@link Place} objects.
 * It uses the core GML parser from the natural module.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class GMLReader extends AbstractResourceReader<List<Place>> {

    private final org.jscience.earth.loaders.gml.GMLReader coreReader;

    public GMLReader() {
        this.coreReader = new org.jscience.earth.loaders.gml.GMLReader();
    }

    @Override
    public String getCategory() {
        return "Geography";
    }

    @Override
    public String getName() {
        return "GML Place Reader";
    }

    @Override
    public String getDescription() {
        return "Reads geographical places from GML format.";
    }

    @Override
    public String getLongDescription() {
        return "Converts OGC GML features into JScience Place objects, preserving names, " +
               "geometries, and attributes.";
    }

    @Override
    public String getResourcePath() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Place>> getResourceType() {
        return (Class<List<Place>>) (Class<?>) List.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return coreReader.getSupportedVersions();
    }

    @Override
    protected List<Place> loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) {
            return read(file);
        }
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) {
                return read(is);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Reads a list of places from a GML input stream.
     *
     * @param input the input stream
     * @return list of parsed places
     * @throws GMLException if parsing fails
     */
    public List<Place> read(InputStream input) throws Exception {
        GMLDocument doc = coreReader.read(input);
        return convertToPlaces(doc);
    }

    /**
     * Reads a list of places from a GML file.
     *
     * @param file the file
     * @return list of parsed places
     * @throws GMLException if parsing fails
     */
    public List<Place> read(File file) throws Exception {
        GMLDocument doc = coreReader.read(file);
        return convertToPlaces(doc);
    }

    private List<Place> convertToPlaces(GMLDocument doc) {
        List<Place> places = new ArrayList<>();
        for (GMLFeature feature : doc.getFeatures()) {
            Place place = convertFeature(feature);
            if (place != null) {
                places.add(place);
            }
        }
        return places;
    }

    private Place convertFeature(GMLFeature feature) {
        String name = feature.getProperty("name");
        if (name == null) name = feature.getProperty("label");
        if (name == null) name = "Feature " + feature.getId();

        Boundary boundary = convertGeometry(feature.getGeometry());
        
        // Determine type from type name or properties
        Place.Type type = Place.Type.OTHER;
        String typeName = feature.getTypeName().toLowerCase();
        if (typeName.contains("city")) type = Place.Type.CITY;
        else if (typeName.contains("country")) type = Place.Type.COUNTRY;
        else if (typeName.contains("region")) type = Place.Type.REGION;
        
        return new Place(name, boundary, type);
    }

    private Boundary convertGeometry(GMLGeometry geom) {
        if (geom == null) return null;

        if (geom instanceof GMLPoint) {
            GMLPoint p = (GMLPoint) geom;
            return Boundary.point(p.getY(), p.getX());
        } else if (geom instanceof GMLPolygon) {
            GMLPolygon poly = (GMLPolygon) geom;
            List<double[]> points = poly.getExteriorRing();
            Coordinate[] coords = new Coordinate[points.size()];
            for (int i = 0; i < points.size(); i++) {
                double[] p = points.get(i);
                coords[i] = new Coordinate(p[1], p[0], p.length > 2 ? p[2] : 0);
            }
            return new Boundary(coords);
        } else if (geom instanceof GMLLineString) {
            GMLLineString line = (GMLLineString) geom;
            List<double[]> points = line.getPoints();
            Coordinate[] coords = new Coordinate[points.size()];
            for (int i = 0; i < points.size(); i++) {
                double[] p = points.get(i);
                coords[i] = new Coordinate(p[1], p[0], p.length > 2 ? p[2] : 0);
            }
            return new Boundary(coords);
        }
        
        return null;
    }
}
