package org.jscience.earth.geosphere.sky;

/**
 */
import javax.vecmath.Color3f;


//Describes the atmosphere (the blue halo around earth)
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class Atmosphere extends Object {
    /** DOCUMENT ME! */
    private float height; //relative to the soil level in meters

    /** DOCUMENT ME! */
    private Color3f color;

    /** DOCUMENT ME! */
    private String composition; //a string of the main gaz with percentage in parenthesis separated by commas.

/**
     * Creates a new Atmosphere object.
     */
    public Atmosphere() {
        height = 0;
        color = new Color3f();
        composition = new String();
    }

/**
     * Creates a new Atmosphere object.
     *
     * @param height      DOCUMENT ME!
     * @param composition DOCUMENT ME!
     */
    public Atmosphere(float height, String composition) {
        this.height = height;
        this.composition = composition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getHeight(float height) {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color3f getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color3f color) {
        this.color = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComposition() {
        return composition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param composition DOCUMENT ME!
     */
    public void setComposition(String composition) {
        this.composition = composition;
    }
}
