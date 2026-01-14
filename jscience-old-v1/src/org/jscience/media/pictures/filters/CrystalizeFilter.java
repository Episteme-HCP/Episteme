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

package org.jscience.media.pictures.filters;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class CrystalizeFilter extends CellularFilter {
    /** DOCUMENT ME! */
    private float edgeThickness = 0.4f;

    /** DOCUMENT ME! */
    private boolean fadeEdges = false;

/**
     * Creates a new CrystalizeFilter object.
     */
    public CrystalizeFilter() {
        setScale(16);
        setRandomness(0.0f);
    }

    /**
     * DOCUMENT ME!
     *
     * @param edgeThickness DOCUMENT ME!
     */
    public void setEdgeThickness(float edgeThickness) {
        this.edgeThickness = edgeThickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getEdgeThickness() {
        return edgeThickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fadeEdges DOCUMENT ME!
     */
    public void setFadeEdges(boolean fadeEdges) {
        this.fadeEdges = fadeEdges;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getFadeEdges() {
        return fadeEdges;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param inPixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPixel(int x, int y, int[] inPixels, int width, int height) {
        float nx = (m00 * x) + (m01 * y);
        float ny = (m10 * x) + (m11 * y);
        nx /= scale;
        ny /= (scale * stretch);
        nx += 1000;
        ny += 1000; // Reduce artifacts around 0,0

        float f = evaluate(nx, ny);

        float f1 = results[0].distance;
        float f2 = results[1].distance;
        int srcx = ImageMath.clamp((int) ((results[0].x - 1000) * scale), 0,
                width - 1);
        int srcy = ImageMath.clamp((int) ((results[0].y - 1000) * scale), 0,
                height - 1);
        int v = inPixels[(srcy * width) + srcx];

        //		f = (f2 - f1) / (f2 + f1);
        f = (f2 - f1) / edgeThickness;
        f = ImageMath.smoothStep(0, edgeThickness, f);

        if (fadeEdges) {
            srcx = ImageMath.clamp((int) ((results[1].x - 1000) * scale), 0,
                    width - 1);
            srcy = ImageMath.clamp((int) ((results[1].y - 1000) * scale), 0,
                    height - 1);

            int v2 = inPixels[(srcy * width) + srcx];
            v2 = ImageMath.mixColors(0.5f, v2, v);
            v = ImageMath.mixColors(f, v2, v);
        } else {
            v = ImageMath.mixColors(f, 0xff000000, v);
        }

        return v;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Stylize/Crystallize...";
    }
}
