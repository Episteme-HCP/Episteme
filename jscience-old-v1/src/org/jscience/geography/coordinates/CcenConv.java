package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class CcenConv extends Conversions {
    /** DOCUMENT ME! */
    private ToCdetConst _toCdetConst;

    /** DOCUMENT ME! */
    private ToLteConst _toLteConst;

/**
     * Creates a new CcenConv object.
     */
    protected CcenConv() {
        super(SRM_SRFT_Code.SRFT_CELESTIOCENTRIC,
            new SRM_SRFT_Code[] {
                SRM_SRFT_Code.SRFT_CELESTIODETIC,
                SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN,
                SRM_SRFT_Code.SRFT_CELESTIOMAGNETIC,
                SRM_SRFT_Code.SRFT_EQUATORIAL_INERTIAL,
                SRM_SRFT_Code.SRFT_SOLAR_ECLIPTIC,
                SRM_SRFT_Code.SRFT_SOLAR_EQUATORIAL,
                SRM_SRFT_Code.SRFT_SOLAR_MAGNETIC_ECLIPTIC,
                SRM_SRFT_Code.SRFT_SOLAR_MAGNETIC_DIPOLE,
                SRM_SRFT_Code.SRFT_HELIOSPHERIC_ARIES_ECLIPTIC,
                SRM_SRFT_Code.SRFT_HELIOSPHERIC_EARTH_ECLIPTIC,
                SRM_SRFT_Code.SRFT_HELIOSPHERIC_EARTH_EQUATORIAL,
                SRM_SRFT_Code.SRFT_UNDEFINED
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new CcenConv();
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

        if (destSrfType == SRM_SRFT_Code.SRFT_CELESTIODETIC) {
            toCdet(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forCelestiodetic(this.getOrmData(), dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_EUCLIDEAN) {
            toLte(srcSrf, destSrf, src, dest);

            // perform post validation
            // for GTRS
            if (srcSrf.getSRFSetCode() == SRM_SRFS_Code.SRFS_GTRS_GLOBAL_COORDINATE_SYSTEM) {
                retValid = CoordCheck.forGTRS(dest);
            }

            // for LTES there is no restrictions
            // apply the false origin offset after the conversion if LTSE is
            // the destination SRF.  Otherwise do nothing because LTSE is just
            // an intermediate SRF
            if (destSrf instanceof SRF_LocalTangentSpaceEuclidean) {
                dest[0] += ((SRF_LocalTangentSpaceEuclidean) destSrf).get_x_false_origin();
                dest[1] += ((SRF_LocalTangentSpaceEuclidean) destSrf).get_y_false_origin();
            }
        } else if (destSrfType == SRM_SRFT_Code.SRFT_CELESTIOMAGNETIC) {
            toSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_EQUATORIAL_INERTIAL) {
            toSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_SOLAR_ECLIPTIC) {
            toSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_SOLAR_EQUATORIAL) {
            toSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_SOLAR_MAGNETIC_ECLIPTIC) {
            toSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_SOLAR_MAGNETIC_DIPOLE) {
            toSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_HELIOSPHERIC_ARIES_ECLIPTIC) {
            toSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_HELIOSPHERIC_EARTH_ECLIPTIC) {
            toSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_HELIOSPHERIC_EARTH_EQUATORIAL) {
            toSphere(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forSpherical(dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_UNDEFINED) {
            // just pass the coordinate through.  This is a datum shift case
            // or this is the last convesion in the chain
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
     * @param source_generic_coord DOCUMENT ME!
     * @param dest_generic_coord DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected void toCdet(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coord, double[] dest_generic_coord)
        throws SrmException {
        final int REGION_1 = 0;
        final int REGION_2 = 1;
        final int REGION_3 = 2;
        final int REGION_4 = 3;
        final int REGION_5 = 4;
        final int REGION_UNDEFINED = 5;
        final int REGION_SPHERICAL = 6;

        double arg;
        double p;
        double s1;
        double s12;

        double alpha;
        double arg2;
        double cf;
        double cl;
        double q;
        double r1;
        double r2;
        double ro;
        double roe;
        double rr;
        double s;
        double top;
        double top2;
        double v;
        double w;
        double w2;
        double xarg;
        double z2;
        double zo;

        double lowerBound;
        double upperBound;
        double ga;
        double bot;

        double dest_lat_ptr = -1.0;
        double dest_lon_ptr = -1.0;
        double dest_elv_ptr = -1.0;

        int region = REGION_UNDEFINED;

        OrmData e_constants = this.getOrmData();

        if (_toCdetConst == null) {
            _toCdetConst = new ToCdetConst(this.getOrmData());
        }

        boolean done_by_special_case = false;

        w2 = Const.square(source_generic_coord[0]) +
            Const.square(source_generic_coord[1]);
        z2 = Const.square(source_generic_coord[2]);
        w = Math.sqrt(w2);

        if (Math.abs(e_constants.Eps) <= (1.0e-12)) {
            /*This is the spherical special case*/
/**
             * dest_lon_ptr=atan2(source_generic_coord[1],source_generic_coord[0]);
             */

            /*This is done below at the end to avoid duplicating the code*/
            dest_lat_ptr = Math.atan2(source_generic_coord[2], w);
            dest_elv_ptr = Math.sqrt(w2 + z2) - e_constants.A;
            region = REGION_SPHERICAL;
        } else {
            /* Check for special cases */
            if (Math.abs(source_generic_coord[0]) <= (1.0e-12)) {
                if (source_generic_coord[1] > 0.0) {
                    dest_lon_ptr = Const.PI_DIV_2;
                } else {
                    if (source_generic_coord[1] < 0.0) {
                        dest_lon_ptr = -Const.PI_DIV_2;
                    } else /* y == 0 */
                     {
                        if (source_generic_coord[2] > 0) {
                            dest_lat_ptr = Const.PI_DIV_2;
                            dest_lon_ptr = 0.0;
                            dest_elv_ptr = source_generic_coord[2] -
                                e_constants.C;

                            done_by_special_case = true;
                        } /* end if z> 0 */ else if (source_generic_coord[2] < 0.0) {
                            dest_lat_ptr = -Const.PI_DIV_2;
                            dest_lon_ptr = 0.0;
                            dest_elv_ptr = -(source_generic_coord[2] +
                                e_constants.C);

                            done_by_special_case = true;
                        } /* end if z < 0 */ else {
                            /* at origin */
                            throw new SrmException(SrmException._INVALID_TARGET_COORDINATE,
                                new String("changeCoordinateSRF: impossible conversion"));
                        } /* end else at origin */} /* end else y == 0 */} /* end else y < 0 */} /* end if x == 0 */}

        /* end of special cases */
        if (!done_by_special_case || (region == REGION_SPHERICAL)) {
            if (region != REGION_SPHERICAL) {
                /* region 2      0-50 kilometers*/
                lowerBound = w2 + (_toCdetConst.u[REGION_2] * z2);
                upperBound = w2 + (_toCdetConst.u[REGION_3] * z2);

                if ((lowerBound >= _toCdetConst.v[REGION_2]) &&
                        (upperBound <= _toCdetConst.v[REGION_3])) {
                    region = REGION_2;
                } else {
                    /* region 3  50 - 23000 kilometers*/
                    lowerBound = upperBound;
                    upperBound = w2 + (z2 * _toCdetConst.u[REGION_4]);

                    if ((lowerBound >= _toCdetConst.v[REGION_3]) &&
                            (upperBound <= _toCdetConst.v[REGION_4])) {
                        region = REGION_3;
                    } else {
                        /* region 1 -30 to 0 kilometers */
                        lowerBound = w2 + (z2 * _toCdetConst.u[REGION_1]);
                        upperBound = w2 + (z2 * _toCdetConst.u[REGION_2]);

                        if ((lowerBound >= _toCdetConst.v[REGION_1]) &&
                                (upperBound <= _toCdetConst.v[REGION_2])) {
                            region = REGION_1;
                        } else {
                            /* region 4  23000 to 10e6 kilometers */
                            lowerBound = upperBound;
                            upperBound = w2 + (z2 * _toCdetConst.u[REGION_5]);

                            if ((lowerBound >= _toCdetConst.v[REGION_4]) &&
                                    (upperBound <= _toCdetConst.v[REGION_5])) {
                                region = REGION_4;
                            } else {
                                /*Declare region 5 unless the following test fails*/
                                region = REGION_5;

                                /* region 5 < -30 or > 10e6 kilometers*/
                            }
                        }
                    }
                }
            }

            /* Approximation to g function*/
            ga = _toCdetConst.b1[region] +
                ((_toCdetConst.b2[region] + (_toCdetConst.b3[region] * w2)) / (_toCdetConst.b4[region] +
                (_toCdetConst.b5[region] * w2) + z2));

            /*
              GA=B1(II)+(B2(II)+B3(II)*W2)/(B4(II)+B5(II)*W2+Z2)
            */
            switch (region) {
            case REGION_1:
            case REGION_2: {
                double Rn;
                top = source_generic_coord[2] * ga;
                dest_lat_ptr = Math.atan2(top, w);
                top2 = top * top;
                rr = top2 + w2;
                q = Math.sqrt(rr);
                s12 = top2 / rr;

                /*uses a Newton-Raphson single iteration with
                excellent first guess usin only multiplications*/
                Rn = Const.computeRnFast(s12, e_constants);

                if (s12 <= 0.5) { /* If between +- 45 degrees lattitude */
                    dest_elv_ptr = q - Rn;
                } else {
                    dest_elv_ptr = (q / ga) +
                        (_toCdetConst.aeps21 * Rn * e_constants.A_inv);
                }

                //******************************************************************
                // Done below at end of function as optimization
                // dest_lon_ptr = atan2(src.source_y,src.source_x);
                // ***************************************************************
            }

            break;

            case REGION_3:
            case REGION_4: {
                /* correct g by using it as a first guess into the bowring formula*/
                top = source_generic_coord[2] * ga * _toCdetConst.rho_inv;
                top2 = top * top;

                rr = top2 + w2;
                q = Math.sqrt(rr);

                {
                    double qinv;
                    double sn;
                    double cn;
                    double s3;
                    double c3;
                    qinv = 1.0 / q;
                    sn = top * qinv;
                    cn = w * qinv;
                    s3 = Const.cube(sn);
                    c3 = Const.cube(cn);
                    top = source_generic_coord[2] + (_toCdetConst.ak1 * s3);
                    bot = w - (_toCdetConst.ak2 * c3);
                }

                top2 = Const.square(top);

                rr = top2 + Const.square(bot);
                q = Math.sqrt(rr);
                s12 = top2 / rr;
                /*Fast Rn computation*/
                {
                    double Rn;

                    Rn = Const.computeRnFast(s12, e_constants);

                    /*Fast Computation of Rn = a/sqrt(1-Eps2*sin_squared(latitude))*/
                    /* end inline root*/
                    if (s12 <= 0.5) {
                        dest_elv_ptr = ((w * q) / bot) - Rn;
                    } else {
                        dest_elv_ptr = ((source_generic_coord[2] * q) / top) +
                            (_toCdetConst.aeps21 * Rn * e_constants.A_inv);
                    }
                }

                dest_lat_ptr = Math.atan2(top, bot);
            }

            break;

            case REGION_5: {
                double gee;

                cf = _toCdetConst.C254 * z2;
                gee = w2 - (_toCdetConst.Eps21 * z2) - _toCdetConst.CEEps2;

                {
                    double g_inv = 1.0 / gee;
                    alpha = cf * Const.square(g_inv);
                    cl = _toCdetConst.CEE * w2 * alpha * g_inv;
                }

                arg2 = cl * (cl + 2.0);
                s1 = 1.0 + cl + Math.sqrt(arg2);
                s = Math.pow(s1, (.333333333333333333333333333333));

                {
                    double temp = s / (Const.square(s) + 1.0 + s);
                    p = alpha * .333333333333333333333333333 * Const.square(temp);
                }

                xarg = 1.0 + (_toCdetConst.TwoCEE * p);
                q = Math.sqrt(xarg);

                {
                    double q_inv = 1.0 / q;
                    double one_plus_q_inv = 1.0 / (1.0 + q);
                    r2 = -p * ((2.0 * (1.0 - e_constants.Eps2) * z2 * (q_inv * one_plus_q_inv)) +
                        w2);
                    r1 = (1.0 + q_inv);
                    r2 = r2 * e_constants.A2_inv;

                    /*
                    * DUE TO PRECISION ERRORS THE ARGUMENT MAY BECOME
                    * NEGATIVE. IF SO SET THE ARGUMENT TO ZERO.
                    */
                    if ((r1 + r2) > 0.0) {
                        ro = e_constants.A * Math.sqrt(.50 * (r1 + r2));
                    } else {
                        ro = 0.0;
                    }

                    ro = ro - (p * e_constants.Eps2 * w * one_plus_q_inv);
                }

                roe = e_constants.Eps2 * ro;
                arg = Const.square(w - roe) + z2;
                v = Math.sqrt(arg - (e_constants.Eps2 * z2));

                {
                    double v_inv = 1.0 / v;
                    zo = _toCdetConst.C2DA * source_generic_coord[2] * v_inv;
                    dest_elv_ptr = Math.sqrt(arg) * (1.0 -
                        (_toCdetConst.C2DA * v_inv));
                }

                top = source_generic_coord[2] + (_toCdetConst.tem * zo);
                dest_lat_ptr = Math.atan(top / w);

                //************************************************************
                //As an optimization, this is done below
                //*dest_lon_ptr = atan2(src.source_y, src.source_x);
                //************************************************************

                /* end of Exact solution */
            }

            break;

            case REGION_SPHERICAL:

                /*This case gets out of the switch statement and lets the longitude
                *be computed at the end of the routine with a single return statement
                *Conclusion:  Don't Remove this case or there won't be a good way
                *to skip the region test and this won't work very well.
                */
                break;

            default:
                throw new SrmException(SrmException._INVALID_TARGET_COORDINATE,
                    new String("changeCoordinateSRF: impossible conversion"));
            }

            /*Since logitude calculation is common to all regions and requires lots of time,
              we'll just do it here to break interlock and hopefully have it occur
              during some of the function return bureaucracy*/
            dest_lon_ptr = Math.atan2(source_generic_coord[1],
                    source_generic_coord[0]);
        }

        dest_generic_coord[0] = dest_lon_ptr;
        dest_generic_coord[1] = dest_lat_ptr;
        dest_generic_coord[2] = dest_elv_ptr;
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
    protected void toLte(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        double[] temp = new double[4];
        double[] temp1 = new double[4];

        // copy source to temp1 and set last element to 1.0
        temp[0] = source_generic_coordinate[0];
        temp[1] = source_generic_coordinate[1];
        temp[2] = source_generic_coordinate[2];
        temp[3] = 1.0;

        OrmData e_constants = this.getOrmData();

        // Converting to LTES, then there are only three possibilities for the
        // LTES parameters to come from.  Either they are copies from the target
        // SRF's params when these are LTSAS or LTSC, or they are the parameters of
        // target LTES SRF.
        if (_toLteConst == null) {
            if (destSrf instanceof SRF_LocalTangentSpaceAzimuthalSpherical) {
                SRF_LT_Params tmpLTParam = ((SRF_LocalTangentSpaceAzimuthalSpherical) destSrf).getSRFParameters();
                SRF_LTSE_Params tmpLTEParam = new SRF_LTSE_Params();
                tmpLTEParam.geodetic_longitude = tmpLTParam.geodetic_longitude;
                tmpLTEParam.geodetic_latitude = tmpLTParam.geodetic_latitude;
                tmpLTEParam.azimuth = tmpLTParam.azimuth;
                tmpLTEParam.x_false_origin = 0.0;
                tmpLTEParam.y_false_origin = 0.0;
                tmpLTEParam.height_offset = tmpLTParam.height_offset;
                _toLteConst = new ToLteConst(e_constants, tmpLTEParam);
            } else if (destSrf instanceof SRF_LocalTangentSpaceCylindrical) {
                SRF_LT_Params tmpLTParam = ((SRF_LocalTangentSpaceCylindrical) destSrf).getSRFParameters();
                SRF_LTSE_Params tmpLTEParam = new SRF_LTSE_Params();
                tmpLTEParam.geodetic_longitude = tmpLTParam.geodetic_longitude;
                tmpLTEParam.geodetic_latitude = tmpLTParam.geodetic_latitude;
                tmpLTEParam.azimuth = tmpLTParam.azimuth;
                tmpLTEParam.x_false_origin = 0.0;
                tmpLTEParam.y_false_origin = 0.0;
                tmpLTEParam.height_offset = tmpLTParam.height_offset;
                _toLteConst = new ToLteConst(e_constants, tmpLTEParam);
            } else { // it is LTES itself
                _toLteConst = new ToLteConst(e_constants,
                        ((SRF_LocalTangentSpaceEuclidean) destSrf).getSRFParameters());
            }
        }

        Const.multMatrixSubsetVector(_toLteConst.T_inv, temp, temp1, 4);

        // copy first three elements of temp1 to destination
        dest_generic_coordinate[0] = temp1[0];
        dest_generic_coordinate[1] = temp1[1];
        dest_generic_coordinate[2] = temp1[2];
    }

    /*
     *----------------------------------------------------------------------------
     *
     * CCEN to Spherical routines and classes
     *
     *----------------------------------------------------------------------------
     */
    protected void toSphere(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coord, double[] dest_generic_coord)
        throws SrmException {
        // the source coordinate is interpreted as
        // source_generic_coord[0] => x
        // source_generic_coord[1] => y
        // source_generic_coord[2] => z
        double x2 = Const.square(source_generic_coord[0]);
        double y2 = Const.square(source_generic_coord[1]);

        // lambda
        if ((x2 > Const.EPSILON_SQ) && (y2 > Const.EPSILON_SQ)) {
            if (source_generic_coord[0] >= 0.0) {
                // invariant on source_generic_coord[1] (Q1, Q4)
                dest_generic_coord[0] = Math.atan(source_generic_coord[1] / source_generic_coord[0]);
            } else {
                if (source_generic_coord[1] >= 0.0) {
                    dest_generic_coord[0] = Const.PI +
                        Math.atan(source_generic_coord[1] / source_generic_coord[0]);
                } else {
                    dest_generic_coord[0] = -Const.PI +
                        Math.atan(source_generic_coord[1] / source_generic_coord[0]);
                }
            }
        } else {
            if (source_generic_coord[1] >= Const.EPSILON) {
                dest_generic_coord[0] = Const.PI_DIV_2;
            } else {
                dest_generic_coord[0] = -Const.PI_DIV_2;
            }

            if (source_generic_coord[0] >= Const.EPSILON) {
                dest_generic_coord[0] = 0.0;
            } else {
                dest_generic_coord[0] = Const.PI;
            }
        }

        // rho
        dest_generic_coord[2] = Math.sqrt(x2 + y2 +
                Const.square(source_generic_coord[2]));

        // theta
        dest_generic_coord[1] = Math.asin(source_generic_coord[2] / dest_generic_coord[2]);
    }

    /*
     *----------------------------------------------------------------------------
     *
     * CCEN to CDET routines and classes
     *
     *----------------------------------------------------------------------------
     */
    public class ToCdetConst {
        /** DOCUMENT ME! */
        double Eps21;

        /** DOCUMENT ME! */
        double C254;

        /** DOCUMENT ME! */
        double CEEps2;

        /** DOCUMENT ME! */
        double CEE;

        /** DOCUMENT ME! */
        double TwoCEE;

        /** DOCUMENT ME! */
        double C2DA;

        /** DOCUMENT ME! */
        double tem;

        /** DOCUMENT ME! */
        double ARat1;

        /** DOCUMENT ME! */
        double ARat2;

        /** DOCUMENT ME! */
        double BRat1;

        /** DOCUMENT ME! */
        double BRat2;

        /** DOCUMENT ME! */
        double aeps21;

        /** DOCUMENT ME! */
        double ak1;

        /** DOCUMENT ME! */
        double ak2;

        /** DOCUMENT ME! */
        double rho;

        /** DOCUMENT ME! */
        double rho_inv;

        /** DOCUMENT ME! */
        double[] b1 = new double[5];

        /** DOCUMENT ME! */
        double[] b2 = new double[5];

        /** DOCUMENT ME! */
        double[] b3 = new double[5];

        /** DOCUMENT ME! */
        double[] b4 = new double[5];

        /** DOCUMENT ME! */
        double[] b5 = new double[5];

        /** DOCUMENT ME! */
        double[] u = new double[5];

        /** DOCUMENT ME! */
        double[] v = new double[5];

/**
         * Creates a new ToCdetConst object.
         *
         * @param e_constants DOCUMENT ME!
         */
        public ToCdetConst(OrmData e_constants) {
            double CE2;
            double[] del = new double[5];
            double hmn;
            double hmx;
            double g1;
            double g2;
            double g3;
            double g4;
            double gm;
            double hm;
            double d1;
            double d2;
            double d3;
            double d4;
            double d5;
            double d6;
            double sm;
            double rnm;
            double zm;
            double wm;
            double z2;
            double w2;
            double d7;
            double d8;
            double d9;
            double d10;
            double a1;
            double a2;
            double a3;
            double a4;
            double a5;
            double f1;
            double f2;
            int i;

            if (e_constants.Eps > Const.EPSILON_SQ) {
                CE2 = e_constants.A2 - e_constants.C2;

                Eps21 = e_constants.Eps2 - 1.0;
                C254 = 54.0 * e_constants.C2;
                CEEps2 = e_constants.Eps2 * CE2;
                CEE = e_constants.Eps2 * e_constants.Eps2;
                TwoCEE = 2 * CEE;
                C2DA = e_constants.C2 / e_constants.A;
                tem = CE2 / e_constants.C2;

                rho = e_constants.A / e_constants.C;
                rho_inv = e_constants.C / e_constants.A;
                aeps21 = e_constants.A * (e_constants.Eps2 - 1);
                ak1 = (e_constants.C * e_constants.Eps2) / (1 -
                    e_constants.Eps2);
                ak2 = e_constants.Eps2 * e_constants.A;

                del[0] = 3E4;
                del[1] = 5E4;
                del[2] = 2.2E7 - 5E4;
                del[3] = 4.5E8 - 2.2E7;
                del[4] = 1E10;

                hmn = -30000.0;

                for (i = 0; i < 5; i++) {
                    hmx = hmn + del[i];
                    g1 = Const.gee(hmn, 0.0, e_constants.A, e_constants.Eps2); /* changed 0 to 1e-14*/
                    g2 = Const.gee(hmx, 0.0, e_constants.A, e_constants.Eps2);
                    g3 = Const.gee(hmx, Const.PI_DIV_2, e_constants.A,
                            e_constants.Eps2);
                    g4 = Const.gee(hmn, Const.PI_DIV_2, e_constants.A,
                            e_constants.Eps2);

                    hm = (hmx - hmn) * 0.5;

                    gm = Const.gee(hm, Const.PI_DIV_4, e_constants.A,
                            e_constants.Eps2);

                    d1 = (Const.square(e_constants.C + hmx) * g3) -
                        (Const.square(e_constants.C + hmn) * g4);
                    d1 = d1 / (Const.square(e_constants.C + hmx) -
                        Const.square(e_constants.C + hmn));

                    d2 = -(g4 - g3) / (Const.square(e_constants.C + hmx) -
                        Const.square(e_constants.C + hmn));

                    d3 = (Const.square(e_constants.C + hmx) * d2) - g3;

                    d4 = Const.square(e_constants.C + hmx) * (g3 - d1);

                    d5 = ((g1 + d3) / Const.square(e_constants.A + hmn)) -
                        ((g2 + d3) / Const.square(e_constants.A + hmx));

                    d5 = d5 / (g2 - g1);

                    d6 = (1 / Const.square(e_constants.A + hmx)) -
                        (1 / Const.square(e_constants.A + hmn));

                    d6 = (d6 * d4) / (g2 - g1);

                    sm = Const.ONE_DIV_ROOT_2; /*sin(SRM_PI_DIV_4)=1/sqrt(2)*/

                    rnm = e_constants.A / Math.sqrt(1 -
                            (e_constants.Eps2 * Const.square(sm)));

                    zm = (((1 - e_constants.Eps2) * rnm) + hm) * sm;
                    wm = (rnm + hm) * Const.ONE_DIV_ROOT_2; /*cos(SRM_PI_DIV_4)=1/sqrt(2)*/

                    z2 = zm * zm;
                    w2 = wm * wm;

                    d7 = ((z2 * d2) - d3 - gm - (gm * w2 * d5)) / w2;

                    d8 = ((z2 * gm) - d4 - (z2 * d1) + (gm * w2 * d6)) / w2;

                    d9 = ((g2 + d3) / Const.square(e_constants.A + hmx)) +
                        (g2 * d5);

                    d10 = (-d4 / Const.square(e_constants.A + hmx)) +
                        (d6 * g2);

                    a4 = (d8 - d10) / (d9 + d7);
                    a2 = (d9 * a4) + d10;
                    a5 = (d5 * a4) + d6;
                    a1 = d4 - (d3 * a4);
                    a3 = d1 + (d2 * a4);

                    b1[i] = a3;
                    b2[i] = a1 - (a3 * a4);
                    b3[i] = a2 - (a3 * a5);
                    b4[i] = a4;
                    b5[i] = a5;

                    f1 = Const.square((e_constants.A + hmn) - 1E-8);
                    f2 = Const.square((e_constants.C + hmn) - 1E-8);

                    u[i] = f1 / f2;
                    v[i] = f1;
                    hmn = hmx;
                }
            } else {
                Eps21 = Double.NaN;
                C254 = Double.NaN;
                CEEps2 = Double.NaN;
                CEE = Double.NaN;
                TwoCEE = Double.NaN;
                C2DA = Double.NaN;
                tem = Double.NaN;
                ARat1 = Double.NaN;
                ARat2 = Double.NaN;
                BRat1 = Double.NaN;
                BRat2 = Double.NaN;

                for (i = 0; i < 5; i++) {
                    b1[i] = Double.NaN;
                    b2[i] = Double.NaN;
                    b3[i] = Double.NaN;
                    b4[i] = Double.NaN;
                    b5[i] = Double.NaN;
                    u[i] = Double.NaN;
                    v[i] = Double.NaN;
                }

                aeps21 = Double.NaN;
                ak1 = Double.NaN;
                ak2 = Double.NaN;
                rho = Double.NaN;
                rho_inv = Double.NaN;
            }
        }
    }

    /*
     *----------------------------------------------------------------------------
     *
     * CCEN to LTES routines and classes
     *
     *----------------------------------------------------------------------------
     */
    public class ToLteConst {
        /** DOCUMENT ME! */
        double[][] T = new double[4][4];

        /** DOCUMENT ME! */
        double[][] T_inv = new double[4][4];

/**
         * Creates a new ToLteConst object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         * @throws SrmException DOCUMENT ME!
         */
        public ToLteConst(OrmData e_constants, SRF_LTSE_Params params)
            throws SrmException {
            Const.calc_T(e_constants, params, T, T_inv);
        }
    }
}
