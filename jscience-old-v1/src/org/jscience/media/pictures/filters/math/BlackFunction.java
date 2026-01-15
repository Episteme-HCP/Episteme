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
public class BlackFunction implements BinaryFunction {
    /**
     * DOCUMENT ME!
     *
     * @param rgb DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBlack(int rgb) {
        return rgb == 0xff000000;
    }
}
