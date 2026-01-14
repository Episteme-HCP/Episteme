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

package org.jscience.geography;

import org.jscience.geography.coordinates.Coord;
import org.jscience.geography.coordinates.Coord2D;
import org.jscience.geography.coordinates.SrmException;

import org.jscience.util.Commented;
import org.jscience.util.Named;
import org.jscience.util.Positioned;

import java.awt.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing a 2D map of geographical sort.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this has nothing to do with java.util.Map
//may be we should also define a Projection class (for architecture) for cross section maps (which are not really "maps")
public class Map extends Object implements Named, Commented, Positioned {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private boolean isColored;

    /** DOCUMENT ME! */
    private double scale;

    /** DOCUMENT ME! */
    private double width;

    /** DOCUMENT ME! */
    private double height;

    /** DOCUMENT ME! */
    private Coord2D topLeftCoords;

    /** DOCUMENT ME! */
    private Vector images; //0 is the lowest layer

    /** DOCUMENT ME! */
    private Set places;

    /** DOCUMENT ME! */
    private Set paths;

    /** DOCUMENT ME! */
    private String comments;

    //width and heights are in actual meters
    /**
     * Creates a new Map object.
     *
     * @param name DOCUMENT ME!
     * @param scale DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param topLeftCoords DOCUMENT ME!
     */
    public Map(String name, double scale, double width, double height,
        Coord2D topLeftCoords) {
        if ((name != null) && (name.length() > 0) && (topLeftCoords != null)) {
            this.name = name;
            this.isColored = false;
            this.scale = scale;
            this.width = width;
            this.height = height;
            this.topLeftCoords = topLeftCoords;
            this.images = new Vector();
            this.places = Collections.EMPTY_SET;
            this.paths = Collections.EMPTY_SET;
            this.comments = new String();
        } else {
            throw new IllegalArgumentException(
                "The Map constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isColored() {
        return isColored;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colors DOCUMENT ME!
     */
    public void hasColors(boolean colors) {
        this.isColored = colors;
    }

    //may return -1
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getScale() {
        return scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param scale DOCUMENT ME!
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    //may return -1
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(double width) {
        this.width = width;
    }

    //may return -1
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord2D getBottomRightCoordinates() {
        Coord2D bottomRightCoords;

        bottomRightCoords = null;

        try {
            bottomRightCoords = (Coord2D) topLeftCoords.makeClone();
            bottomRightCoords.setValues(new double[] {
                    topLeftCoords.getValues()[0] + width,
                    topLeftCoords.getValues()[1] + height
                });
        } catch (SrmException e) {
        }

        return bottomRightCoords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord getTopLeftCoordinates() {
        return topLeftCoords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coordinates DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setTopLeftCoordinates(Coord2D coordinates) {
        if (coordinates != null) {
            this.topLeftCoords = coordinates;
        } else {
            throw new IllegalArgumentException(
                "You can't set null coordinates.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getImages() {
        return images;
    }

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     */
    public void addImage(Image image) {
        images.add(image);
    }

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     */
    public void removeImage(Image image) {
        images.remove(image);
    }

    /**
     * DOCUMENT ME!
     *
     * @param images DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setImages(Vector images) {
        Iterator iterator;
        Image image;
        boolean result;

        if (images != null) {
            iterator = images.iterator();
            result = true;

            while (iterator.hasNext() && result) {
                result = iterator.next() instanceof Image;
            }

            if (result) {
                this.images = images;
            } else {
                throw new IllegalArgumentException(
                    "The Vector of images must contain only images.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set images of a null Vector.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        Coord2D[] coords;

        Coord2D bottomRightCoords;

        coords = new Coord2D[4];

        coords[0] = null;
        coords[1] = null;
        coords[2] = null;
        coords[3] = null;

        try {
            coords[0] = (Coord2D) topLeftCoords.makeClone();
            coords[1] = (Coord2D) topLeftCoords.makeClone();
            coords[1].setValues(new double[] {
                    topLeftCoords.getValues()[0] + width,
                    topLeftCoords.getValues()[1]
                });
            coords[2] = (Coord2D) topLeftCoords.makeClone();
            coords[2].setValues(new double[] {
                    topLeftCoords.getValues()[0],
                    topLeftCoords.getValues()[1] + height
                });
            coords[3] = (Coord2D) topLeftCoords.makeClone();
            coords[3].setValues(new double[] {
                    topLeftCoords.getValues()[0] + width,
                    topLeftCoords.getValues()[1] + height
                });
        } catch (SrmException e) {
        }

        return new Place(getName(), new Boundary(coords));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getPlaces() {
        return places;
    }

    /**
     * DOCUMENT ME!
     *
     * @param place DOCUMENT ME!
     */
    public void addPlace(Place place) {
        places.add(place);
    }

    /**
     * DOCUMENT ME!
     *
     * @param place DOCUMENT ME!
     */
    public void removePlace(Place place) {
        places.remove(place);
    }

    /**
     * DOCUMENT ME!
     *
     * @param places DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPlaces(Set places) {
        Iterator iterator;
        Place place;
        boolean result;

        if (places != null) {
            iterator = places.iterator();
            result = true;

            while (iterator.hasNext() && result) {
                result = iterator.next() instanceof Place;
            }

            if (result) {
                this.places = places;
            } else {
                throw new IllegalArgumentException(
                    "The Set of places must contain only places.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set places of a null Set.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getPaths() {
        return paths;
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public void addPath(Path path) {
        paths.add(path);
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public void removePath(Path path) {
        paths.remove(path);
    }

    /**
     * DOCUMENT ME!
     *
     * @param paths DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPaths(Set paths) {
        Iterator iterator;
        Path path;
        boolean result;

        if (paths != null) {
            iterator = paths.iterator();
            result = true;

            while (iterator.hasNext() && result) {
                result = iterator.next() instanceof Path;
            }

            if (result) {
                this.paths = paths;
            } else {
                throw new IllegalArgumentException(
                    "The Set of paths must contain only paths.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set paths of a null Set.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setComments(String comments) {
        if (comments != null) {
            this.comments = comments;
        } else {
            throw new IllegalArgumentException("You can't set null comments.");
        }
    }
}
