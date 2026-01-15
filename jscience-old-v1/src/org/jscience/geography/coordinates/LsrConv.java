package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class LsrConv extends Conversions {
    /** DOCUMENT ME! */
    protected double[][] _transformation_matrix;

/**
     * Creates a new LsrConv object.
     *
     * @param src  DOCUMENT ME!
     * @param dest DOCUMENT ME!
     */
    protected LsrConv(SRM_SRFT_Code src, SRM_SRFT_Code[] dest) {
        super(src, dest);
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected abstract void setTransformMatrix(BaseSRF srcSrf, BaseSRF destSrf)
        throws SrmException;

    /*
     *----------------------------------------------------------------------------
     *
     * ToSphere routines
     *
     *----------------------------------------------------------------------------
     */
    protected void toLsr(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coord, double[] dest_generic_coord)
        throws SrmException {
        if (_transformation_matrix == null) {
            setTransformMatrix(srcSrf, destSrf);
        }

        // although the transformation matrix is 4x4, but we really need to
        // use the upper left 3x3 for this operation.
        Const.applyMatrix3x3(source_generic_coord, _transformation_matrix,
            dest_generic_coord);
    }
}
