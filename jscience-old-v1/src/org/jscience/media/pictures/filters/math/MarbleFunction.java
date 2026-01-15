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
public class MarbleFunction extends CompoundFunction2D {
/**
     * Creates a new MarbleFunction object.
     */
    public MarbleFunction() {
        super(new TurbulenceFunction(new Noise(), 6));
    }

/**
     * Creates a new MarbleFunction object.
     *
     * @param basis DOCUMENT ME!
     */
    public MarbleFunction(Function2D basis) {
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
        return (float) Math.pow(0.5 * (Math.sin(8. * basis.evaluate(x, y)) + 1),
            0.77);
    }
}
