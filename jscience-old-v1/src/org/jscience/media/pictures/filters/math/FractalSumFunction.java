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
public class FractalSumFunction extends CompoundFunction2D {
    /** DOCUMENT ME! */
    private float octaves = 1.0f;

/**
     * Creates a new FractalSumFunction object.
     *
     * @param basis DOCUMENT ME!
     */
    public FractalSumFunction(Function2D basis) {
        super(basis);
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
            t += (basis.evaluate(f * x, f * y) / f);

        return t;
    }
}
