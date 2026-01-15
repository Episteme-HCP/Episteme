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
public class RayCastersCollection extends RayCaster {
    /** DOCUMENT ME! */
    protected Vector raycasters;

    /** DOCUMENT ME! */
    protected Material mat;

/**
     * Creates a new RayCastersCollection object.
     */
    public RayCastersCollection() {
        super();
        raycasters = new Vector(5, 5);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    public void drawRays(Graphics g, OpticalDevice d) {
        Enumeration e = raycasters.elements();

        while (e.hasMoreElements()) {
            RayCaster o = (RayCaster) e.nextElement();
            o.drawRays(g, d);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rc DOCUMENT ME!
     */
    public void append(RayCaster rc) {
        raycasters.addElement(rc);
        rc.setInitialMaterial(mat);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mat DOCUMENT ME!
     */
    public void setInitialMaterial(Material mat) {
        this.mat = mat;

        Enumeration e = raycasters.elements();

        while (e.hasMoreElements()) {
            RayCaster o = (RayCaster) e.nextElement();
            o.setInitialMaterial(mat);
        }
    }
}
