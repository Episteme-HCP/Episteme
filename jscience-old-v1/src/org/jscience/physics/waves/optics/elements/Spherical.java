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
package org.jscience.physics.waves.optics.elements;

import org.jscience.physics.waves.optics.materials.Material;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Spherical extends Aspherical {
/**
     * Creates a new Spherical object.
     */
    public Spherical() {
        super();
    }

/**
     * Creates a new Spherical object.
     *
     * @param C   DOCUMENT ME!
     * @param mat DOCUMENT ME!
     */
    public Spherical(double C, Material mat) {
        super(C, 0.0, mat);
    }

/**
     * Creates a new Spherical object.
     *
     * @param C        DOCUMENT ME!
     * @param width    DOCUMENT ME!
     * @param aperture DOCUMENT ME!
     * @param mat      DOCUMENT ME!
     */
    public Spherical(double C, double width, double aperture, Material mat) {
        super(C, 0.0, width, aperture, mat);
    }
}
