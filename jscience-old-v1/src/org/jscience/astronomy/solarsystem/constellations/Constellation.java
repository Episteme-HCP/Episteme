package org.jscience.astronomy.solarsystem.constellations;

import java.awt.*;

import javax.vecmath.Color4f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class Constellation {
    /** DOCUMENT ME! */
    Color4f color;

    /** DOCUMENT ME! */
    String name;

    /** DOCUMENT ME! */
    Point range;

/**
     * Creates a new Constellation object.
     *
     * @param s DOCUMENT ME!
     * @param r DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public Constellation(String s, Point r, Color4f c) {
        name = s;
        range = r;
        color = c;
    } //constructor

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name + " " + range + " " + color;
    } //toString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color4f getColor() {
        return color;
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
    public Point getRange() {
        return range;
    }
}
