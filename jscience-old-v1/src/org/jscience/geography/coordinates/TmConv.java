package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class TmConv extends Conversions {
    /** DOCUMENT ME! */
    private ToCdetConst _toCdetConst = null;

    /** DOCUMENT ME! */
    private TmValidConst _tmValidConst = null;

/**
     * Creates a new TmConv object.
     */
    public TmConv() {
        super(SRM_SRFT_Code.SRFT_TRANSVERSE_MERCATOR,
            new SRM_SRFT_Code[] {
                SRM_SRFT_Code.SRFT_CELESTIODETIC, SRM_SRFT_Code.SRFT_UNDEFINED
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new TmConv();
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

        if (destSrfType == SRM_SRFT_Code.SRFT_CELESTIODETIC) {
            // apply the false origin offset before the conversion
            src[0] -= ((SRF_TransverseMercator) srcSrf).get_false_easting();
            src[1] -= ((SRF_TransverseMercator) srcSrf).get_false_northing();

            // apply pre validation
            if (_tmValidConst == null) {
                _tmValidConst = new TmValidConst(this.getOrmData(),
                        ((SRF_TransverseMercator) srcSrf).getSRFParameters());
            }

            retValid = CoordCheck.forTransverseMercator_native(_tmValidConst._m,
                    _tmValidConst._bl, _tmValidConst._x_threshold,
                    _tmValidConst._y_threshold, src);

            // perform conversion
            toCdet(srcSrf, destSrf, src, dest);

            // perform post validation
            // use ALSP validation if source SRF is ALSP
            if (srcSrf.getSRFSetCode() == SRM_SRFS_Code.SRFS_ALABAMA_SPCS) {
                retValid = CoordCheck.forALSP_cd(this.getOrmData(),
                        ((SRF_TransverseMercator) srcSrf).getSRFParameters(),
                        dest);
            }
            // 	    // use UPS validation if destination SRF is UPS
            // 	    else if ( destSrf.getSRFSetCode() == SRM_SRFS_Code.UNIV_POLAR_STEREOG )
            // 		retValid = CoordCheck.forUPS( ((SRF_PolarStereographic)destSrf).getSRFParameters(), source_generic_coordinate );
            // use UTM validation if destination SRF is UTM
            else if (srcSrf.getSRFSetCode() == SRM_SRFS_Code.SRFS_UNIVERSAL_TRANSVERSE_MERCATOR) {
                retValid = CoordCheck.forUTM_cd(((SRF_TransverseMercator) srcSrf).getSRFParameters(),
                        srcSrf.getSRFSetMemberCode(), dest);
            } else { // this is a celestiodetic source SRF
                retValid = CoordCheck.forTransverseMercator_cd(this.getOrmData(),
                        ((SRF_TransverseMercator) srcSrf).getSRFParameters(),
                        dest);
            }
        } else if (destSrfType == SRM_SRFT_Code.SRFT_UNDEFINED) {
            // just pass the coordinate through. this is the last convesion in
            // the chain
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
    protected void toCdet(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        double source_x = 0.0;
        double source_y = 0.0;
        double source_z = 0.0;
        double b3;
        double b4;
        double b5;
        double b6;
        double b6x;
        double cp;
        double cp2;
        double cu;
        double d1;
        double d2;
        double eta2;
        double phi1;
        double rn;
        double sp;
        double sp2;
        double su;
        double su2;
        double temp;
        double top;
        double tp;
        double tp2;
        double u;
        double dest_lat;
        double dest_lon;
        OrmData e_constants = this.getOrmData();

        if (_toCdetConst == null) {
            _toCdetConst = new ToCdetConst(e_constants,
                    ((SRF_TransverseMercator) srcSrf).getSRFParameters());
        }

        source_x = source_generic_coordinate[0];
        source_y = source_generic_coordinate[1];
        source_z = source_generic_coordinate[2];

        u = (_toCdetConst._smz + (source_y / _toCdetConst._CScale)) / _toCdetConst._conap;

        if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
            su = Math.sin(u);
            cu = Math.cos(u);
            su2 = su * su;

            temp = _toCdetConst._polx2b +
                (su2 * (_toCdetConst._polx3b +
                (su2 * (_toCdetConst._polx4b + (su2 * _toCdetConst._polx5b)))));

            phi1 = u + (su * cu * temp);
        } else { /* compute spherical values */
            phi1 = u;
        }

        /*
         * COMPUTE VARIABLE COEFFICIENTS FOR FINAL RESULT
         * COMPUTE THE VARIABLE COEFFICIENTS OF THE LAT AND LON
         * EXPANSIONS
         */
        sp = Math.sin(phi1);
        cp = Math.cos(phi1);
        tp = sp / cp;
        tp2 = tp * tp;
        sp2 = sp * sp;
        cp2 = cp * cp;

        b3 = 1.0 + tp2 + tp2;
        b4 = 5 + (tp2 * 3);
        b5 = 5 + (tp2 * ((tp2 * 24.0) + 28.0));
        b6 = 61 + (tp2 * ((tp2 * 45.0) + 90.0));

        if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
            eta2 = e_constants.Epps2 * cp2;
            top = .25 - (sp2 * (e_constants.Eps2 / 4));

            /* inline sq root*/
            temp = e_constants.Eps25 * sp2;
            temp = 0.5 - temp;
            temp = temp + ((temp - .25) / temp);
            rn = (e_constants.A) / temp;
            d1 = source_x / (_toCdetConst._CScale * rn);
            d2 = d1 * d1;

            b3 = b3 + eta2;
            b4 = b4 + (tp2 * (-9 * eta2)) + (eta2 * (1 - (4 * eta2)));
            b5 = b5 + (eta2 * ((tp2 * 8.0) + 6.0));
            b6x = 46 - (3.0 * eta2) + (tp2 * (-252.0 - (tp2 * 90.0)));
            b6x = eta2 * (b6x + (eta2 * tp2 * ((tp2 * 225.0) - 66.0)));
            b6 = b6x + b6;
        } else /* compute spherical values */
         {
            rn = e_constants.A;
            top = .25;
            d1 = source_x / (_toCdetConst._CScale * rn);
            d2 = d1 * d1;
        }

        dest_lat = phi1 -
            (tp * top * (d2 * (_toCdetConst._Con2 +
            (d2 * (((-_toCdetConst._Con24) * b4) +
            (d2 * _toCdetConst._Con720 * b6))))));

        dest_lon = _toCdetConst._longitude_origin +
            ((d1 * (1.0 +
            (d2 * ((-_toCdetConst._Con6 * b3) +
            (d2 * _toCdetConst._Con120 * b5))))) / cp);

        dest_generic_coordinate[0] = dest_lon;
        dest_generic_coordinate[1] = dest_lat;
        dest_generic_coordinate[2] = source_z;
    }

    /*
     *----------------------------------------------------------------------------
     *
     * FUNCTION: toCdet
     *
     *----------------------------------------------------------------------------
     */
    private class TmValidConst {
        /*This software abomination computes spherical TM coordinates for
          a bounding box for TM shaped like this to ensure that horrible
          coordinates are rejected without being operated on.  The assumption
          is that if the sphere and the scaling are made generous enough that
          this will always be outside the precise boundary. */
        /** DOCUMENT ME! */
        double _m; /*slope of the lines being drawn in TMERC*/

        /** DOCUMENT ME! */
        double _bl; /*Intercept of lines on the right side*/

        /** DOCUMENT ME! */
        double _x_threshold; /* Northing that when less than just uses
        a square sided test*/

        /** DOCUMENT ME! */
        double _y_threshold; /*The x value tested against when
        not testing against the lines*/

/**
         * Creates a new TmValidConst object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         * @throws SrmException DOCUMENT ME!
         */
        public TmValidConst(OrmData e_constants, SRF_Mercator_Params params)
            throws SrmException {
            CdetConv tmpCdet = new CdetConv();
            tmpCdet.setOrmData(e_constants);

            double[] uln = new double[] { 0.0, 0.0, 0.0 };
            double[] uls = new double[] { 0.0, 0.0, 0.0 };
            double[] eql = new double[] { 0.0, 0.0, 0.0 };
            double[] uln_cd = new double[] {
                    -13.0 * Const.RADIANS_PER_DEGREE,
                    89.991 * Const.RADIANS_PER_DEGREE, 0.0
                };
            double[] uls_cd = new double[] {
                    -13.0 * Const.RADIANS_PER_DEGREE,
                    89.990 * Const.RADIANS_PER_DEGREE, 0.0
                };
            double[] eql_cd = new double[] {
                    -13.0 * Const.RADIANS_PER_DEGREE, 0.0, 0.0
                };

            tmpCdet.toTmer(params, uln_cd, uln);
            tmpCdet.toTmer(params, uls_cd, uls);
            tmpCdet.toTmer(params, eql_cd, eql);

            _m = (uln[1] - uls[1]) / (uln[0] - uls[0]);

            /*upper left boundary*/
            _bl = uln[1] - (_m * uln[0]);

            _y_threshold = (_m * eql[0]) + _bl;

            _x_threshold = Math.abs(eql[0]);
        }
    }

    /*
     *----------------------------------------------------------------------------
     *
     * FUNCTION: toCdet
     *
     *----------------------------------------------------------------------------
     */
    private class ToCdetConst {
        /** DOCUMENT ME! */
        double _Con2;

        /** DOCUMENT ME! */
        double _EF;

        /** DOCUMENT ME! */
        double _Con6;

        /** DOCUMENT ME! */
        double _Con24;

        /** DOCUMENT ME! */
        double _Con120;

        /** DOCUMENT ME! */
        double _Con720;

        /** DOCUMENT ME! */
        double _CScale;

        /** DOCUMENT ME! */
        double _polx2b;

        /** DOCUMENT ME! */
        double _polx3b;

        /** DOCUMENT ME! */
        double _polx4b;

        /** DOCUMENT ME! */
        double _polx5b;

        /** DOCUMENT ME! */
        double _conap;

        /** DOCUMENT ME! */
        double _longitude_origin;

        /** DOCUMENT ME! */
        double _smz;

/**
         * Creates a new ToCdetConst object.
         *
         * @param e_constants DOCUMENT ME!
         * @param tmParams    DOCUMENT ME!
         */
        public ToCdetConst(OrmData e_constants, SRF_Mercator_Params tmParams) {
            double polx1a;
            double polx2a;
            double polx4a;
            double polx6a;
            double polx8a;
            double con;
            double s1;
            double c1;
            double s12;
            double latitude_origin;
            double poly1b;
            double poly2b;
            double poly3b;
            double poly4b;
            double poly5b;
            double polx2b;
            double polx3b;
            double polx4b;
            double polx5b;
            double longitude_origin;

            longitude_origin = tmParams.origin_longitude;
            latitude_origin = tmParams.origin_latitude;
            _CScale = tmParams.central_scale;

            _longitude_origin = longitude_origin;

            _Con2 = 2.0;
            _Con6 = .166666666666667;
            _Con24 = 4.0 * .0416666666666667;
            _Con120 = .00833333333333333;
            _Con720 = 4.0 * .00138888888888888;

            s1 = Math.sin(latitude_origin);
            c1 = Math.cos(latitude_origin);
            s12 = s1 * s1;

            if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
                _EF = e_constants.F / (2.0 - e_constants.F);
                con = 1.0 - e_constants.Eps2;

                _Con2 /= con;
                _Con24 /= con;
                _Con720 /= con;

                polx1a = 1.0 - (e_constants.Eps2 / 4.0) -
                    (3.0 / 64.0 * Math.pow(e_constants.Eps2, 2)) -
                    (5.0 / 256.0 * Math.pow(e_constants.Eps2, 3)) -
                    (175.0 / 16384.0 * Math.pow(e_constants.Eps2, 4));

                _conap = e_constants.A * polx1a;

                polx2a = (3.0 / 2.0 * _EF) - (27.0 / 32.0 * Math.pow(_EF, 3));

                polx4a = (21.0 / 16.0 * Math.pow(_EF, 2)) -
                    (55.0 / 32.0 * Math.pow(_EF, 4));

                polx6a = 151.0 / 96.0 * Math.pow(_EF, 3);

                polx8a = 1097.0 / 512.0 * Math.pow(_EF, 4);

                /* add polys for computing smz */
                polx2b = ((1.0 * e_constants.Eps2) +
                    (0.25 * Math.pow(e_constants.Eps2, 2)) +
                    (15.0 / 128.0 * Math.pow(e_constants.Eps2, 3))) -
                    (455.0 / 4096.0 * Math.pow(e_constants.Eps2, 4));

                polx2b = 3.0 / 8.0 * polx2b;

                polx3b = ((1.0 * Math.pow(e_constants.Eps2, 2)) +
                    (0.75 * Math.pow(e_constants.Eps2, 3))) -
                    (77.0 / 128.0 * Math.pow(e_constants.Eps2, 4));

                polx3b = 15.0 / 256.0 * polx3b;

                polx4b = Math.pow(e_constants.Eps2, 3) -
                    (41.0 / 32.0 * Math.pow(e_constants.Eps2, 4));

                polx4b = (polx4b * 35.0) / 3072.0;

                polx5b = -315.0 / 131072.0 * Math.pow(e_constants.Eps2, 4);

                poly1b = 1.0 - (0.25 * e_constants.Eps2) -
                    (3.0 / 64.0 * Math.pow(e_constants.Eps2, 2)) -
                    (5.0 / 256.0 * Math.pow(e_constants.Eps2, 3)) -
                    (175.0 / 16384.0 * Math.pow(e_constants.Eps2, 4));

                poly2b = ((polx2b * -2.0) + (polx3b * 4.0)) - (polx4b * 6.0) +
                    (polx5b * 8.0);

                poly3b = ((polx3b * -8.0) + (polx4b * 32.0)) - (polx5b * 80.0);

                poly4b = (polx4b * -32.0) + (polx5b * 192.0);

                poly5b = polx5b * -128.0;

                /* these are different polxb's than above */
                _polx2b = (polx2a * 2.0) + (polx4a * 4.0) + (polx6a * 6.0) +
                    (polx8a * 8.0);

                _polx3b = (polx4a * -8.0) - (polx6a * 32.0) - (80.0 * polx8a);
                _polx4b = (polx6a * 32.0) + (192.0 * polx8a);
                _polx5b = -128.0 * polx8a;

                _smz = s1 * c1 * (poly2b +
                    (s12 * (poly3b + (s12 * (poly4b + (s12 * poly5b))))));
                _smz = e_constants.A * ((poly1b * latitude_origin) + _smz);
            } else /* compute spherical values */
             {
                _smz = e_constants.A * latitude_origin;
                _conap = e_constants.A;
            }
        }
    }
}
