/**
 * Title:        NewProj
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright:    Copyright (c) imt
 * </p>
 *
 * <p>
 * Company:      imt
 * </p>
 *
 * <p></p>
 */
package org.jscience.physics.waves.optics.rays;

import org.jscience.physics.waves.optics.elements.OpticalDevice;
import org.jscience.physics.waves.optics.materials.Material;

import java.awt.*;

import java.util.Enumeration;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class RayCaster {
    /** DOCUMENT ME! */
    protected Vector rays;

    /** DOCUMENT ME! */
    protected Material mat;

/**
     * Creates a new RayCaster object.
     */
    public RayCaster() {
        super();
        rays = new Vector(5, 5);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    public void drawRays(Graphics g, OpticalDevice d) {
        Enumeration e = rays.elements();

        while (e.hasMoreElements()) {
            Ray o = (Ray) e.nextElement();
            o.clear();
            d.propagate(o);
            d.drawRay(g, o, 0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param mat DOCUMENT ME!
     */
    public void setInitialMaterial(Material mat) {
        this.mat = mat;
    }
}
