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
public class CompositeFunction1D implements Function1D {
    /** DOCUMENT ME! */
    private Function1D f1;

    /** DOCUMENT ME! */
    private Function1D f2;

/**
     * Creates a new CompositeFunction1D object.
     *
     * @param f1 DOCUMENT ME!
     * @param f2 DOCUMENT ME!
     */
    public CompositeFunction1D(Function1D f1, Function1D f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float evaluate(float v) {
        return f1.evaluate(f2.evaluate(v));
    }
}
