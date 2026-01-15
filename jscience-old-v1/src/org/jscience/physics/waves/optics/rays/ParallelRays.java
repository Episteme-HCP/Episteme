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

import org.jscience.physics.waves.optics.materials.Material;

import java.util.Enumeration;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ParallelRays extends RayCaster {
    /** DOCUMENT ME! */
    private double n;

    /** DOCUMENT ME! */
    private double width;

    /** DOCUMENT ME! */
    private double w;

/**
     * Creates a new ParallelRays object.
     */
    public ParallelRays() {
        super();
    }

/**
     * Creates a new ParallelRays object.
     *
     * @param pos   DOCUMENT ME!
     * @param dir   DOCUMENT ME!
     * @param n     DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param w     DOCUMENT ME!
     * @param mat   DOCUMENT ME!
     */
    public ParallelRays(Double3Vector pos, Double3Vector dir, int n,
        double width, double w, Material mat) {
        super();

        this.n = n;
        this.width = width;
        this.w = w;
        this.mat = mat;

        Move(pos, dir);
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     */
    public void setWavelength(double w) {
        this.w = w;

        Enumeration e = rays.elements();

        while (e.hasMoreElements()) {
            Ray o = (Ray) e.nextElement();
            o.setWavelength(w);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getWavelength() {
        return w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     * @param dir DOCUMENT ME!
     */
    public void Move(Double3Vector pos, Double3Vector dir) {
        rays.setSize(0);

        Double3Vector top = new Double3Vector(pos.x, pos.y - (width / 2), pos.z);

        if (n > 1) {
            double interval = width / (n - 1);

            for (int i = 0; i < n; i++) {
                Double3Vector p = new Double3Vector(top.x,
                        top.y + (i * interval), top.z);
                rays.addElement(new Ray(
                        new RayPoint(p, dir, w, mat.indexAtWavelength(w))));
            }
        } else {
            rays.addElement(new Ray(
                    new RayPoint(pos, dir, w, mat.indexAtWavelength(w))));
        }
    }
}
