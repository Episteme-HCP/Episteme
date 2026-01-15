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
public class SharpenFilter extends ConvolveFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -4883137561307845895L;

    /** DOCUMENT ME! */
    protected static float[] sharpenMatrix = {
            0.0f, -0.2f, 0.0f, -0.2f, 1.8f, -0.2f, 0.0f, -0.2f, 0.0f
        };

/**
     * Creates a new SharpenFilter object.
     */
    public SharpenFilter() {
        super(sharpenMatrix);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Blur/Sharpen";
    }
}
