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
public class BlurFilter extends ConvolveFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -4753886159026796838L;

    /** DOCUMENT ME! */
    protected static float[] blurMatrix = {
            0.1f, 0.2f, 0.1f, 0.2f, 0.2f, 0.2f, 0.1f, 0.2f, 0.1f
        };

    /** DOCUMENT ME! */
    private int blur = 2;

/**
     * Creates a new BlurFilter object.
     */
    public BlurFilter() {
        super((float[]) blurMatrix.clone());
    }

    /**
     * DOCUMENT ME!
     *
     * @param blur DOCUMENT ME!
     */
    public void setBlur(int blur) {
        this.blur = blur;

        float[] m = (float[]) blurMatrix.clone();
        m[4] = (float) blur / 10;

        Kernel kernel = new Kernel(3, 3, m);
        kernel.normalize();
        setKernel(kernel);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBlur() {
        return blur;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Blur/Simple Blur...";
    }
}
