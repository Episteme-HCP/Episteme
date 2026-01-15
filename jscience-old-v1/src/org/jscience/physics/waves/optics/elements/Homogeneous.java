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

import org.jscience.mathematics.algebraic.matrices.Double3Vector;

import org.jscience.physics.waves.optics.materials.Material;
import org.jscience.physics.waves.optics.rays.RayPoint;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Homogeneous extends OpticalElement {
    /** DOCUMENT ME! */
    private Material mat;

/**
     * Creates a new Homogeneous object.
     */
    public Homogeneous() {
        super();
    }

/**
     * Creates a new Homogeneous object.
     *
     * @param w   DOCUMENT ME!
     * @param mat DOCUMENT ME!
     */
    public Homogeneous(double w, Material mat) {
        super(w);
        this.mat = mat;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Material getMaterial() {
        return mat;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mat DOCUMENT ME!
     */
    public void setMaterial(Material mat) {
        this.mat = mat;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void propagateRayPointSelf(RayPoint r) {
        double distance;
        distance = -r.getPosition().x;

        double kb;
        double ka;
        double wb;

        kb = r.getKVector().norm();
        wb = r.getWavelength();
        ka = (2 * Math.PI * mat.indexAtWavelength(wb)) / wb;
        // The index is the same before and after, so no matter whether the RayPoint
        // is located after the begining of the element. A threshold is used because
        // of rounding errors.
        //if( Math.abs( ka - kb ) > 1e-8 )
        {
            if (distance < 0) {
                //if( distance < -1e-8 )  // Si erreur d'arrondi
                r.invalidate();
            } else {
                if (distance > 0) {
                    r.goStraight(distance);
                }

                // Here, the RayPoint is at the beginning of the element.
                Double3Vector kvect = r.getKVector();

                double kx;
                double kx2;
                double ky;
                double kz;
                double l;
                double w;

                ky = kvect.y;
                kz = kvect.z;
                w = r.getWavelength();
                l = (2 * Math.PI * mat.indexAtWavelength(w)) / w;
                kx2 = (l * l) - (ky * ky) - (kz * kz);

                if (kx2 >= 0) {
                    kx = Math.sqrt(kx2);

                    Double3Vector newk = new Double3Vector(kx, ky, kz);
                    r.setKVector(newk);
                } else {
                    r.invalidate();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawSelf(Graphics g) {
        //g.drawLine( 0, -50, 0, 50 );
    }
}
