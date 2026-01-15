package org.jscience.astronomy;

import javax.media.j3d.Group;


/**
 * The Nebula class provides support for the category of AstralBody of the
 * same name.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//describes complete information about a nebula
//this is not a solid object but a gaz cloud, nevertheless, it is treated as if a solid object as the whole gaz cloud by itself may have a huge mass
public abstract class Nebula extends AstralBody {
/**
     * Creates a new Nebula object.
     *
     * @param name DOCUMENT ME!
     */
    public Nebula(String name) {
        super(name);
    }

/**
     * Creates a new Nebula object.
     *
     * @param name DOCUMENT ME!
     * @param mass DOCUMENT ME!
     */
    public Nebula(String name, double mass) {
        super(name, mass);
    }

/**
     * Creates a new Nebula object.
     *
     * @param name  DOCUMENT ME!
     * @param shape DOCUMENT ME!
     * @param mass  DOCUMENT ME!
     */
    public Nebula(String name, Group shape, double mass) {
        super(name, shape, mass);
    }

    /**
     * DOCUMENT ME!
     */
    protected void updateShape() {
        super.updateShape();
    }
}
