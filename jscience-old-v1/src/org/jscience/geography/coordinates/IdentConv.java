package org.jscience.geography.coordinates;


// This class is create for the cases when the source and the target
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
class IdentConv extends Conversions {
/**
     * Creates a new IdentConv object.
     */
    public IdentConv() {
        // this is a dummy data and is not used anywhere in the application
        super(SRM_SRFT_Code.SRFT_UNDEFINED,
            new SRM_SRFT_Code[] { SRM_SRFT_Code.SRFT_UNDEFINED });
    }

    // This method will never be called
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new IdentConv();
    }

    /**
     * DOCUMENT ME!
     *
     * @param SrfType DOCUMENT ME!
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     * @param src DOCUMENT ME!
     * @param dest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SRM_Coordinate_Valid_Region_Code convert(SRM_SRFT_Code SrfType,
        BaseSRF srcSrf, BaseSRF destSrf, double[] src, double[] dest) {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        System.arraycopy(src, 0, dest, 0, src.length);

        return retValid;
    }
}
