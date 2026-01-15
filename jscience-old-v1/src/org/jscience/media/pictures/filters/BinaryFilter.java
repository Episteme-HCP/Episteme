/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import org.jscience.media.pictures.filters.math.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class BinaryFilter extends WholeImageFilter {
    /** DOCUMENT ME! */
    protected int newColor = 0xff000000;

    /** DOCUMENT ME! */
    protected BinaryFunction blackFunction = new BlackFunction();

    /** DOCUMENT ME! */
    protected int iterations = 1;

    /** DOCUMENT ME! */
    protected Colormap colormap;

    /**
     * DOCUMENT ME!
     *
     * @param iterations DOCUMENT ME!
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colormap DOCUMENT ME!
     */
    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Colormap getColormap() {
        return colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newColor DOCUMENT ME!
     */
    public void setNewColor(int newColor) {
        this.newColor = newColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNewColor() {
        return newColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param blackFunction DOCUMENT ME!
     */
    public void setBlackFunction(BinaryFunction blackFunction) {
        this.blackFunction = blackFunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BinaryFunction getBlackFunction() {
        return blackFunction;
    }
}
