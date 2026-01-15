package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class LteConv extends Conversions {
    /** DOCUMENT ME! */
    private ToCcenConst _toCcenConst;

/**
     * Creates a new LteConv object.
     */
    public LteConv() {
        super(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN,
            new SRM_SRFT_Code[] {
                SRM_SRFT_Code.SRFT_CELESTIOCENTRIC,
                SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL,
                SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL,
                SRM_SRFT_Code.SRFT_UNDEFINED
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new LteConv();
    }

    /*
     *----------------------------------------------------------------------------
     *
     * FUNCTION: convert
     *
     *----------------------------------------------------------------------------
     */
    public SRM_Coordinate_Valid_Region_Code convert(SRM_SRFT_Code destSrfType,
        BaseSRF srcSrf, BaseSRF destSrf, double[] src, double[] dest)
        throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        if (destSrfType == SRM_SRFT_Code.SRFT_CELESTIOCENTRIC) {
            if (srcSrf.getSRFSetCode() == SRM_SRFS_Code.SRFS_GTRS_GLOBAL_COORDINATE_SYSTEM) {
                retValid = CoordCheck.forGTRS(src);
            }

            // else
            // NO restriction
            // apply the false origin offset before the conversion if LTSE is the
            // source SRF.  Otherwise do nothing because LTSE is just an intermediate SRF
            if (srcSrf instanceof SRF_LocalTangentSpaceEuclidean) {
                src[0] -= ((SRF_LocalTangentSpaceEuclidean) srcSrf).get_x_false_origin();
                src[1] -= ((SRF_LocalTangentSpaceEuclidean) srcSrf).get_y_false_origin();
            }

            // no restrictions
            toCcen(srcSrf, destSrf, src, dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL) {
            toLctp(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forCylindrical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_AZIMUTHAL_SPHERICAL) {
            toAzSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forAzSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_UNDEFINED) {
            // just pass the coordinate through.  This is the last convesion
            // in the chain
            dest[0] = src[0];
            dest[1] = src[1];
            dest[2] = src[2];
        }

        return retValid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcSrf DOCUMENT ME!
     * @param destSrf DOCUMENT ME!
     * @param source_generic_coordinate DOCUMENT ME!
     * @param dest_generic_coordinate DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected void toCcen(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        double[] temp = new double[4];
        double[] temp1 = new double[4];

        // copy source to temp and set last element to 1.0
        temp[0] = source_generic_coordinate[0];
        temp[1] = source_generic_coordinate[1];
        temp[2] = source_generic_coordinate[2];
        temp[3] = 1.0;

        OrmData e_constants = this.getOrmData();

        // if converting to CC, then there are only three possibilities for the
        // LTE parameters to come from.  Either they are copies from the source
        // SRF's params when these are LTAS or LTC, or they are the parameters of
        // source LTE SRF.
        if (_toCcenConst == null) {
            if (srcSrf instanceof SRF_LocalTangentSpaceAzimuthalSpherical) {
                SRF_LT_Params tmpLTParam = ((SRF_LocalTangentSpaceAzimuthalSpherical) srcSrf).getSRFParameters();
                SRF_LTSE_Params tmpLTEParam = new SRF_LTSE_Params();
                tmpLTEParam.geodetic_longitude = tmpLTParam.geodetic_longitude;
                tmpLTEParam.geodetic_latitude = tmpLTParam.geodetic_latitude;
                tmpLTEParam.azimuth = tmpLTParam.azimuth;
                tmpLTEParam.x_false_origin = 0.0;
                tmpLTEParam.y_false_origin = 0.0;
                tmpLTEParam.height_offset = tmpLTParam.height_offset;
                _toCcenConst = new ToCcenConst(e_constants, tmpLTEParam);
            } else if (srcSrf instanceof SRF_LocalTangentSpaceCylindrical) {
                SRF_LT_Params tmpLTParam = ((SRF_LocalTangentSpaceCylindrical) srcSrf).getSRFParameters();
                SRF_LTSE_Params tmpLTEParam = new SRF_LTSE_Params();
                tmpLTEParam.geodetic_longitude = tmpLTParam.geodetic_longitude;
                tmpLTEParam.geodetic_latitude = tmpLTParam.geodetic_latitude;
                tmpLTEParam.azimuth = tmpLTParam.azimuth;
                tmpLTEParam.x_false_origin = 0.0;
                tmpLTEParam.y_false_origin = 0.0;
                tmpLTEParam.height_offset = tmpLTParam.height_offset;
                _toCcenConst = new ToCcenConst(e_constants, tmpLTEParam);
            } else { // it is LTE itself
                _toCcenConst = new ToCcenConst(e_constants,
                        ((SRF_LocalTangentSpaceEuclidean) srcSrf).getSRFParameters());
            }
        }

        Const.multMatrixSubsetVector(_toCcenConst.T, temp, temp1, 4);

        // copy first three elements of temp1 to destination
        dest_generic_coordinate[0] = temp1[0];
        dest_generic_coordinate[1] = temp1[1];
        dest_generic_coordinate[2] = temp1[2];
    }

    /*
     *----------------------------------------------------------------------------
     *
     * LTE to Cylindrical (LTCP) routines and classes
     *
     *----------------------------------------------------------------------------
     */
    protected void toLctp(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coord, double[] dest_generic_coord)
        throws SrmException {
        // the source coordinate is interpreted as
        // source_generic_coord[0] => x
        // source_generic_coord[1] => y
        // source_generic_coord[2] => z
        double x2 = source_generic_coord[0] * source_generic_coord[0];

        // theta
        if (x2 > Const.EPSILON_SQ) {
            if (source_generic_coord[0] > 0.0) {
                if (source_generic_coord[1] > 0.0) { // Q I
                    dest_generic_coord[0] = Math.atan(source_generic_coord[1] / source_generic_coord[0]);
                } else { // Q IV
                    dest_generic_coord[0] = Const.TWO_PI +
                        Math.atan(source_generic_coord[1] / source_generic_coord[0]);
                }
            } else { // Q II and III
                dest_generic_coord[0] = Const.PI +
                    Math.atan(source_generic_coord[1] / source_generic_coord[0]);
            }
        } else {
            if (source_generic_coord[1] >= Const.EPSILON) {
                dest_generic_coord[0] = 0.0;
            } else {
                dest_generic_coord[0] = Const.PI;
            }
        }

        // rho
        dest_generic_coord[1] = Math.sqrt(x2 +
                Const.square(source_generic_coord[1]));

        // zeta
        dest_generic_coord[2] = source_generic_coord[2];
    }

    /*
     *----------------------------------------------------------------------------
     *
     * LTE to Spherical routines and classes
     *
     *----------------------------------------------------------------------------
     */
    protected void toAzSphere(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coord, double[] dest_generic_coord)
        throws SrmException {
        // the source coordinate is interpreted as
        // source_generic_coord[0] => x
        // source_generic_coord[1] => y
        // source_generic_coord[2] => z
        double x2 = Const.square(source_generic_coord[0]);

        // alpha
        if (x2 > Const.EPSILON_SQ) {
            if (source_generic_coord[0] >= 0.0) {
                if (source_generic_coord[1] >= 0.0) { // I Q
                    dest_generic_coord[0] = Math.atan(source_generic_coord[1] / source_generic_coord[0]);
                } else { // IV Q
                    dest_generic_coord[0] = Const.TWO_PI +
                        Math.atan(source_generic_coord[1] / source_generic_coord[0]);
                }
            } else { // II Q and III Q
                dest_generic_coord[0] = Const.PI +
                    Math.atan(source_generic_coord[1] / source_generic_coord[0]);
            }
        } else {
            if (source_generic_coord[1] > Const.EPSILON) {
                dest_generic_coord[0] = 0.0;
            } else {
                dest_generic_coord[0] = Const.PI;
            }
        }

        // rho
        dest_generic_coord[2] = Math.sqrt(x2 +
                Const.square(source_generic_coord[1]) +
                Const.square(source_generic_coord[2]));

        // theta
        dest_generic_coord[1] = Math.asin(source_generic_coord[2] / dest_generic_coord[2]);
    }

    /*
     *----------------------------------------------------------------------------
     *
     * FUNCTION: toCcenConst
     *
     *----------------------------------------------------------------------------
     */
    public class ToCcenConst {
        /** DOCUMENT ME! */
        double[][] T = new double[4][4];

        /** DOCUMENT ME! */
        double[][] T_inv = new double[4][4];

/**
         * Creates a new ToCcenConst object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         */
        public ToCcenConst(OrmData e_constants, SRF_LTSE_Params params) {
            Const.calc_T(e_constants, params, T, T_inv);
        }
    }
}
