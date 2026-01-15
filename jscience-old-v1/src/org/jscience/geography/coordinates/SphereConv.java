package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class SphereConv extends Conversions {
/**
     * Creates a new SphereConv object.
     *
     * @param src  DOCUMENT ME!
     * @param dest DOCUMENT ME!
     */
    protected SphereConv(SRM_SRFT_Code src, SRM_SRFT_Code[] dest) {
        super(src, dest);
    }

    /*
     *----------------------------------------------------------------------------
     *
     * ToSphere routines
     *
     *----------------------------------------------------------------------------
     */
    protected SRM_Coordinate_Valid_Region_Code toCcen(BaseSRF srcSrf,
        BaseSRF destSrf, double[] source_generic_coord,
        double[] dest_generic_coord) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        double lambda = source_generic_coord[0];
        double theta = source_generic_coord[1];
        double rho = source_generic_coord[2];

        // x
        dest_generic_coord[0] = rho * Math.cos(theta) * Math.cos(lambda);

        // y
        dest_generic_coord[1] = rho * Math.cos(theta) * Math.sin(lambda);

        // z
        dest_generic_coord[2] = rho * Math.sin(theta);

        return retValid;
    }
}
