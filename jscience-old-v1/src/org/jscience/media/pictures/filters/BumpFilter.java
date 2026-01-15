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
public class BumpFilter extends ConvolveFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 2528502820741699111L;

    /** DOCUMENT ME! */
    protected static float[] embossMatrix = {
            -1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f
        };

/**
     * Creates a new BumpFilter object.
     */
    public BumpFilter() {
        super(embossMatrix);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Blur/Bumps";
    }
}
