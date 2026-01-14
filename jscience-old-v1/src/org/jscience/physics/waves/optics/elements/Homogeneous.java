/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
