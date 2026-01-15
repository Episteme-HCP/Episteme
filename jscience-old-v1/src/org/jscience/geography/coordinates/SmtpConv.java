package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class SmtpConv extends SphereConv {
/**
     * Creates a new SmtpConv object.
     */
    protected SmtpConv() {
        // setting the source and destinations of this conversion object
        super(SRM_SRFT_Code.SRFT_SOLAR_MAGNETIC_ECLIPTIC,
            new SRM_SRFT_Code[] {
                SRM_SRFT_Code.SRFT_CELESTIOCENTRIC, SRM_SRFT_Code.SRFT_UNDEFINED
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new HaecConv();
    }

    /*
     *----------------------------------------------------------------------------
     *
     * Conversion dispatcher
     *
     *----------------------------------------------------------------------------
     */
    protected SRM_Coordinate_Valid_Region_Code convert(
        SRM_SRFT_Code destSrfType, BaseSRF srcSrf, BaseSRF destSrf,
        double[] src, double[] dest) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        if (destSrfType == SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) {
            // perform pre validation
            retValid = CoordCheck.forSpherical(src);
            toCcen(srcSrf, destSrf, src, dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_UNDEFINED) {
            // just pass the coordinate through.  This is the last conversion of chain
            dest[0] = src[0];
            dest[1] = src[1];
            dest[2] = src[2];
        }

        return retValid;
    }
}
