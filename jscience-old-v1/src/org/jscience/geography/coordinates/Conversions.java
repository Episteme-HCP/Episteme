/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class Conversions {
    /** DOCUMENT ME! */
    private SRM_SRFT_Code _src;

    /** DOCUMENT ME! */
    private SRM_SRFT_Code[] _dest;

    /** DOCUMENT ME! */
    private OrmData _ormData;

/**
     * Creates a new Conversions object.
     *
     * @param src  DOCUMENT ME!
     * @param dest DOCUMENT ME!
     */
    protected Conversions(SRM_SRFT_Code src, SRM_SRFT_Code[] dest) {
        // set the source SRF
        _src = src;

        // set the destination SRFs
        _dest = dest;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srfType DOCUMENT ME!
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     * @param src DOCUMENT ME!
     * @param dest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected abstract SRM_Coordinate_Valid_Region_Code convert(
        SRM_SRFT_Code srfType, BaseSRF srcSrf, BaseSRF destSrf, double[] src,
        double[] dest) throws SrmException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract Conversions makeClone();

    /**
     * DOCUMENT ME!
     *
     * @param orm DOCUMENT ME!
     */
    protected void setOrmData(SRM_ORM_Code orm) {
        _ormData = new OrmData(orm);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ormData DOCUMENT ME!
     */
    protected void setOrmData(OrmData ormData) {
        _ormData = ormData;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected SRM_SRFT_Code getSrc() {
        return _src;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected SRM_SRFT_Code[] getDest() {
        return _dest;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected OrmData getOrmData() {
        return _ormData;
    }
}
