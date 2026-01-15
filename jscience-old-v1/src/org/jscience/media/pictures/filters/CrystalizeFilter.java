/*
 * Copyright (C) Jerry Huxtable 1998
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
