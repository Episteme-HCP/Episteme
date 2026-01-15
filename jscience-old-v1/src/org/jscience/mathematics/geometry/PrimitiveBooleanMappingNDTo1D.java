/*
 * ��?���?���?�_�?��?���\���C���^?[�t�F�C�X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PrimitiveBooleanMappingNDTo1D.java,v 1.3 2007-10-23 18:19:45 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import org.jscience.mathematics.analysis.AbstractMapping;


/**
 * ��?���?���?�_�?��?���\���C���^?[�t�F�C�X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:45 $
 */
public interface PrimitiveBooleanMappingNDTo1D extends AbstractMapping {
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean map(int[] x);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean map(long[] x);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean map(float[] x);

    /**
     * A user-defined function. We do not state anything about the
     * domain on which this function applies.
     *
     * @see See org.jscience.mathematics.analysis.Domain.
     */
    public boolean map(double[] x);

    /**
     * The dimension of variable parameter. Should be a strictly
     * positive integer.
     *
     * @return DOCUMENT ME!
     */
    public int numInputDimensions();
}
/* end of file */
