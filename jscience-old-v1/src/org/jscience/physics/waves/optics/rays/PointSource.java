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

import org.jscience.mathematics.algebraic.matrices.Double3Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class PointSource extends RayCaster {
    /** DOCUMENT ME! */
    private double n;

    /** DOCUMENT ME! */
    private double width;

    /** DOCUMENT ME! */
    private double w;

    /** DOCUMENT ME! */
    private Double3Vector s;

    /** DOCUMENT ME! */
    private Double3Vector d1;

    /** DOCUMENT ME! */
    private Double3Vector d2;

/**
     * Creates a new PointSource object.
     */
    public PointSource() {
        super();
    }

/**
     * Creates a new PointSource object.
     *
     * @param src   DOCUMENT ME!
     * @param dest1 DOCUMENT ME!
     * @param dest2 DOCUMENT ME!
     * @param n     DOCUMENT ME!
     * @param w     DOCUMENT ME!
     */
    public PointSource(Double3Vector src, Double3Vector dest1,
        Double3Vector dest2, int n, double w) {
        super();

        this.n = n;

        //this.width = width;
        this.w = w;

        Move(src, dest1, dest2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     * @param dest1 DOCUMENT ME!
     * @param dest2 DOCUMENT ME!
     */
    public void Move(Double3Vector src, Double3Vector dest1, Double3Vector dest2) {
        s = new Double3Vector(src);

        if (dest1 != null) {
            d1 = new Double3Vector(dest1);
        }

        if (dest2 != null) {
            d2 = new Double3Vector(dest2);
        }

        rays.setSize(0);

        double diffX = (d2.x - d1.x);
        double diffY = (d2.y - d1.y);

        for (int i = 0; i < n; i++) {
            Double3Vector newdir = new Double3Vector(d1.x - s.x +
                    ((i * diffX) / (n - 1)),
                    d1.y - s.y + ((i * diffY) / (n - 1)), d1.z - s.z);
            rays.addElement(new Ray(new RayPoint(s, newdir, w)));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     */
    public void Move(Double3Vector src) {
        Move(src, null, null);
    }
}
