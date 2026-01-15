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
public class ThreeRays extends RayCaster {
    /** DOCUMENT ME! */
    private double w;

    /** DOCUMENT ME! */
    private Double3Vector pos;

/**
     * Creates a new ThreeRays object.
     */
    public ThreeRays() {
        super();
    }

/**
     * Creates a new ThreeRays object.
     *
     * @param pos    DOCUMENT ME!
     * @param inter1 DOCUMENT ME!
     * @param inter2 DOCUMENT ME!
     * @param w      DOCUMENT ME!
     */
    public ThreeRays(Double3Vector pos, Double3Vector inter1,
        Double3Vector inter2, double w) {
        super();
        setIntersections(pos, inter1, inter2, w);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     * @param inter1 DOCUMENT ME!
     * @param inter2 DOCUMENT ME!
     * @param w DOCUMENT ME!
     */
    public void setIntersections(Double3Vector pos, Double3Vector inter1,
        Double3Vector inter2, double w) {
        Double3Vector dir1 = new Double3Vector(inter1.x - pos.x,
                inter1.y - pos.y, inter1.z - pos.z);
        Double3Vector dir2 = new Double3Vector(inter2.x - pos.x,
                inter2.y - pos.y, inter2.z - pos.z);
        Double3Vector dir3 = new Double3Vector(1.0, 0.0, 0.0);

        this.pos = new Double3Vector(pos);
        this.w = w;

        RayPoint r;

        rays.setSize(0);
        r = new RayPoint(pos, dir1, w);

        if (dir1.x == 0) {
            r.invalidate();
        }

        rays.addElement(new Ray(r));
        r = new RayPoint(pos, dir2, w);

        if (dir2.x == 0) {
            r.invalidate();
        }

        rays.addElement(new Ray(r));
        r = new RayPoint(pos, dir3, w);
        rays.addElement(new Ray(r));
    }

    /**
     * DOCUMENT ME!
     *
     * @param inter1 DOCUMENT ME!
     * @param inter2 DOCUMENT ME!
     */
    public void SetIntersections(Double3Vector inter1, Double3Vector inter2) {
        Double3Vector dir1 = new Double3Vector(inter1.x - pos.x,
                inter1.y - pos.y, inter1.z - pos.z);
        Double3Vector dir2 = new Double3Vector(inter2.x - pos.x,
                inter2.y - pos.y, inter2.z - pos.z);
        Double3Vector dir3 = new Double3Vector(1.0, 0.0, 0.0);

        rays.setSize(0);
        rays.addElement(new Ray(new RayPoint(pos, dir1, w)));
        rays.addElement(new Ray(new RayPoint(pos, dir2, w)));
        rays.addElement(new Ray(new RayPoint(pos, dir3, w)));
    }
}
