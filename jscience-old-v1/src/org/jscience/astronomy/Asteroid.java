package org.jscience.astronomy;

import javax.media.j3d.Group;


/**
 * The Asteroid class provides support for asteroids. same name.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a natural free floating small AstralBody
//usually either : metallic, stony or carbonaceous
public abstract class Asteroid extends AstralBody {
    /** DOCUMENT ME! */
    private String composition; //defaults to empty string

/**
     * Creates a new Asteroid object.
     *
     * @param name DOCUMENT ME!
     */
    public Asteroid(String name) {
        super(name);
        this.composition = new String("");
    }

/**
     * Creates a new Asteroid object.
     *
     * @param name DOCUMENT ME!
     * @param mass DOCUMENT ME!
     */
    public Asteroid(String name, double mass) {
        super(name, mass);
        this.composition = new String("");
    }

/**
     * Creates a new Asteroid object.
     *
     * @param name  DOCUMENT ME!
     * @param shape DOCUMENT ME!
     * @param mass  DOCUMENT ME!
     */
    public Asteroid(String name, Group shape, double mass) {
        super(name, shape, mass);
        this.composition = new String("");
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
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setComposition(String composition) {
        if (composition != null) {
            this.composition = composition;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null composition");
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void updateShape() {
        super.updateShape();
    }
}
