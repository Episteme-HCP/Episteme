/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters.math;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class TurbulenceFunction extends CompoundFunction2D {
    /** DOCUMENT ME! */
    private float octaves;

/**
     * Creates a new TurbulenceFunction object.
     *
     * @param basis   DOCUMENT ME!
     * @param octaves DOCUMENT ME!
     */
    public TurbulenceFunction(Function2D basis, float octaves) {
        super(basis);
        this.octaves = octaves;
    }

    /**
     * DOCUMENT ME!
     *
     * @param octaves DOCUMENT ME!
     */
    public void setOctaves(float octaves) {
        this.octaves = octaves;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getOctaves() {
        return octaves;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float evaluate(float x, float y) {
        float t = 0.0f;

        for (float f = 1.0f; f <= octaves; f *= 2)
            t += (Math.abs(basis.evaluate(f * x, f * y)) / f);

        return t;
    }
}
