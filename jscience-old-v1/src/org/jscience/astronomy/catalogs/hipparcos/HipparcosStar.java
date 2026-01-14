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

package org.jscience.astronomy.catalogs.hipparcos;

import com.sun.j3d.utils.geometry.Sphere;

import org.jscience.astronomy.Star;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.
// 30 Nov 1999 - changed order in while statment in getApperance
// lazy eval seesm to have changed.
/**
 * A star as a 3D renderable object
 */
public class HipparcosStar extends Star {
    /** DOCUMENT ME! */
    public static final int flags = Sphere.GENERATE_NORMALS |
        Sphere.ENABLE_GEOMETRY_PICKING | Sphere.ENABLE_PICK_REPORTING;

    /** DOCUMENT ME! */
    static private float colStep = 0;

    /** DOCUMENT ME! */
    static private Appearance[] appearances;

    /** DOCUMENT ME! */
    private static int verbose = Integer.parseInt(HipparcosProperties.getProperty(
                "verbose", "1"));

    /** DOCUMENT ME! */
    private static final float[] colBands = { 0.3f, 0.4f, 0.7f, 1.1f }; // b-v bands

    /* this array holds the r,b,g values for the color bands
     Note there is one more than the bands for greater than the last band */
    /** DOCUMENT ME! */
    private static final float[][] colours = {
            { 0.1f, 0.5f, 0.1f },
            { 0.13f, 0.36f, 0.37f },
            { 0.5f, 0.15f, 0.40f },
            { 0.55f, 0.1f, 0.3f },
            { 0.6f, 0.1f, 0.1f }
        };

    /** DOCUMENT ME! */
    private HipparcosCatalogEntry star;

/**
     * Creates a new Star3D object.
     *
     * @param star DOCUMENT ME!
     */
    public HipparcosStar(HipparcosCatalogEntry star) {
        super(star.toString());
        this.star = star;
        super.getGeometry(calcSize(star), flags, 15, getStarAppearance(star));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HipparcosCatalogEntry getHipparcosCatalogEntry() {
        return star;
    }

    /**
     * Set up appearances for the colours
     */
    private static void init() {
        appearances = new Appearance[colours.length];

        float r;
        float g;
        float b;

        for (int i = 0; i < appearances.length; i++) {
            appearances[i] = new Appearance();
            r = colours[i][0];
            b = colours[i][1];
            g = colours[i][2];

            if (verbose > 3) {
                System.out.println("Col:" + i + "r:" + r + " b:" + b + " g:" +
                    g);
            }

            Material material = new Material();
            material.setDiffuseColor(r + 0.05f, g + 0.05f, b + 0.05f);
            material.setShininess(10);
            material.setAmbientColor(r, g, b);
            material.setLightingEnable(true);
            appearances[i].setMaterial(material);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Appearance getStarAppearance(int col) {
        if (appearances == null) {
            init();
        }

        return appearances[col];
    }

    /**
     * Get Appearance for given star
     *
     * @param star DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Appearance getStarAppearance(HipparcosCatalogEntry star) {
        if (appearances == null) {
            init();
        }

        // search forward through the colorbands until the we find
        // the band for this b-v.
        int col = 0;
        double b_v = star.getB_V();

        while ((col < colBands.length) && (b_v > colBands[col]))
            col++;

        if (col > (colBands.length)) {
            col = colBands.length;
        }

        if (verbose > 3) {
            System.out.println("Allocating colour " + col + " for " + b_v +
                " " + star.getId());
        }

        return appearances[col];
    }

    /**
     * Size for given Magnitude
     *
     * @param star DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static float calcSize(HipparcosCatalogEntry star) {
        float size = (float) (star.getMag() + 5 +
            (5 * Math.log(star.getParalax())));
        size = (float) ((60f - size) / 60);

        return size;
    }
}
