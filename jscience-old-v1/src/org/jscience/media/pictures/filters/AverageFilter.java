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
public class AverageFilter extends ConvolveFilter {
    /** DOCUMENT ME! */
    protected static float[] theMatrix = {
            0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f
        };

/**
     * Creates a new AverageFilter object.
     */
    public AverageFilter() {
        super(theMatrix);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Blur/Average Blur";
    }
}
