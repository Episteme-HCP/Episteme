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

import org.jscience.physics.waves.optics.materials.Material;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ThickLens extends OpticalDevice {
    /** DOCUMENT ME! */
    Spherical first;

    /** DOCUMENT ME! */
    Spherical second;

    /** DOCUMENT ME! */
    Material mat1;

    /** DOCUMENT ME! */
    Material mat2;

    /** DOCUMENT ME! */
    double aperture;

/**
     * Creates a new ThickLens object.
     *
     * @param C1        DOCUMENT ME!
     * @param C2        DOCUMENT ME!
     * @param thickness DOCUMENT ME!
     * @param width     DOCUMENT ME!
     * @param aperture  DOCUMENT ME!
     * @param mat1      DOCUMENT ME!
     * @param mat2      DOCUMENT ME!
     */
    public ThickLens(double C1, double C2, double thickness, double width,
        double aperture, Material mat1, Material mat2) {
        super();

        double a;
        double R1;
        double R2;
        double Rp;

        if (C1 == 0) {
            if (C2 == 0) {
                a = 10000;
            } else {
                R2 = Math.abs(1 / C2);
                Rp = R2 - thickness;
                a = Math.sqrt((R2 * R2) - (Rp * Rp));
            }
        } else {
            if (C2 == 0) {
                R1 = Math.abs(1 / C1);
                Rp = R1 - thickness;
                a = Math.sqrt((R1 * R1) - (Rp * Rp));
            } else {
                R1 = Math.abs(1 / C1);
                R2 = Math.abs(1 / C2);

                double x2;
                double xi;
                x2 = -((R1 + R2) - thickness);

                xi = ((R1 * R1) - (R2 * R2) + (x2 * x2)) / (2 * x2);
                a = Math.sqrt((R1 * R1) - (xi * xi));
            }
        }

        a = Math.min(aperture, a);
        this.aperture = a;

        /*R1 = Math.abs( 1/C1 );
        R2 = Math.abs( 1/C2 );
        
        alpha = - ( 2*R2 + 1 ) / ( 2*thickness*( R1-R2-1 ) );
        a = Math.sqrt( 2*alpha*thickness*R1 - (alpha*alpha*thickness*thickness ) );
        System.out.println( a );
        a = Math.min( aperture, a );*/
        this.mat1 = mat1;
        this.mat2 = mat2;
        first = new Spherical(C1, thickness, a, mat1);

        //first.SetWidth( thickness );
        append(first);
        second = new Spherical(C2, width, a, mat2);

        //second.SetWidth( nextElement );
        append(second);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void setC1(double c) {
        first.setC(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void setC2(double c) {
        second.setC(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getC1() {
        return first.getC();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getC2() {
        return second.getC();
    }

    /**
     * DOCUMENT ME!
     *
     * @param mat DOCUMENT ME!
     */
    public void setMaterial1(Material mat) {
        mat1 = mat;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mat DOCUMENT ME!
     */
    public void setMaterial2(Material mat) {
        mat2 = mat;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Material getMaterial1() {
        return mat1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Material getMaterial2() {
        return mat2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawSelf(Graphics g) {
        super.drawSelf(g);

        Color savecolor = g.getColor();
        g.setColor(Color.red);

        int x1;
        int x2;
        int yt;
        int yb;
        double pos1;
        double pos2;

        yt = (int) Math.round(-aperture);
        yb = (int) Math.round(aperture);

        pos1 = 0;
        pos2 = first.getWidth();

        x1 = (int) Math.round(first.asph(aperture));
        x2 = (int) Math.round(pos2 + second.asph(aperture));

        g.drawLine(x1, yt, x2, yt);
        g.drawLine(x1, yb, x2, yb);
        g.setColor(savecolor);
    }
}
