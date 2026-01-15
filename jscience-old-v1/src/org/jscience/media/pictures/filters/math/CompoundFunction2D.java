/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters.math;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class CompoundFunction2D implements Function2D {
    /** DOCUMENT ME! */
    protected Function2D basis;

/**
     * Creates a new CompoundFunction2D object.
     *
     * @param basis DOCUMENT ME!
     */
    public CompoundFunction2D(Function2D basis) {
        this.basis = basis;
    }

    /**
     * DOCUMENT ME!
     *
     * @param basis DOCUMENT ME!
     */
    public void setBasis(Function2D basis) {
        this.basis = basis;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Function2D getBasis() {
        return basis;
    }
}
