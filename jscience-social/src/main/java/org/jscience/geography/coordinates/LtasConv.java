package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class LtasConv extends SphereConv {
/**
     * Creates a new LtasConv object.
     */
    protected LtasConv() {
        super(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL,
            new SRM_SRFT_Code[] {
                SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN,
                SRM_SRFT_Code.SRFT_UNDEFINED
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new LtasConv();
    }

    // Function dispatcher keyed on the destination SRF
    /**
     * DOCUMENT ME!
     *
     * @param destSrfType DOCUMENT ME!
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     * @param src DOCUMENT ME!
     * @param dest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected SRM_Coordinate_Valid_Region_Code convert(
        SRM_SRFT_Code destSrfType, BaseSRF srcSrf, BaseSRF destSrf,
        double[] src, double[] dest) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        if (destSrfType == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN) {
            // perform pre validation
            retValid = CoordCheck.forAzSpherical(src);
            toLte(srcSrf, destSrf, src, dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_UNDEFINED) {
            // just pass the coordinate through.  This is a datum shift case
            // or this is the last convesion in the chain
            dest[0] = src[0];
            dest[1] = src[1];
            dest[2] = src[2];
        }

        return retValid;
    }

    /*
     *----------------------------------------------------------------------------
     *
     * FUNCTION: toLTE
     *
     *----------------------------------------------------------------------------
     */
    protected void toLte(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coord, double[] dest_generic_coord)
        throws SrmException {
        // the source coordinate is interpreted as
        // source_generic_coord[0] => alpha
        // source_generic_coord[1] => theta
        // source_generic_coord[2] => rho
        double ctheta = Math.cos(source_generic_coord[1]);

        // output x
        dest_generic_coord[0] = source_generic_coord[2] * ctheta * Math.cos(source_generic_coord[0]);

        // output y
        dest_generic_coord[1] = source_generic_coord[2] * ctheta * Math.sin(source_generic_coord[0]);

        // output z
        dest_generic_coord[2] = source_generic_coord[2] * Math.sin(source_generic_coord[1]);
    }
}
