package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class CdetConv extends Conversions {
    /** DOCUMENT ME! */
    private ToTmerConst _toTmerConst;

    /** DOCUMENT ME! */
    private ToMercConst _toMercConst;

    /** DOCUMENT ME! */
    private ToLccConst1 _toLccConst1;

    /** DOCUMENT ME! */
    private ToLccConst2 _toLccConst2;

/**
     * Creates a new CdetConv object.
     */
    protected CdetConv() {
        // setting the source and destinations of this conversion object
        super(SRM_SRFT_Code.SRFT_CELESTIODETIC,
            new SRM_SRFT_Code[] {
                SRM_SRFT_Code.SRFT_CELESTIOCENTRIC,
                SRM_SRFT_Code.SRFT_LAMBERT_CONFORMAL_CONIC,
                SRM_SRFT_Code.SRFT_TRANSVERSE_MERCATOR,
                SRM_SRFT_Code.SRFT_MERCATOR, SRM_SRFT_Code.SRFT_UNDEFINED
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Conversions makeClone() {
        return (Conversions) new CdetConv();
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
            retValid = CoordCheck.forCelestiodetic(this.getOrmData(), src);
            toCcen(srcSrf, destSrf, src, dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_TRANSVERSE_MERCATOR) {
            // perform pre validation
            // use ALSP validation if destination SRF is ALSP
            if (destSrf.getSRFSetCode() == SRM_SRFS_Code.SRFS_ALABAMA_SPCS) {
                retValid = CoordCheck.forALSP_cd(this.getOrmData(),
                        ((SRF_TransverseMercator) destSrf).getSRFParameters(),
                        src);
            }
            // 	    // use UPS validation if destination SRF is UPS
            // 	    else if ( destSrf.getSRFSetCode() == SRM_SRFS_Code.UNIV_POLAR_STEREOG )
            // 		retValid = CoordCheck.forUPS( ((SRF_PolarStereographic)destSrf).getSRFParameters(), source_generic_coordinate );
            // use UPS validation if destination SRF is UPS
            else if (destSrf.getSRFSetCode() == SRM_SRFS_Code.SRFS_UNIVERSAL_TRANSVERSE_MERCATOR) {
                retValid = CoordCheck.forUTM_cd(((SRF_TransverseMercator) destSrf).getSRFParameters(),
                        destSrf.getSRFSetMemberCode(), src);
            } else { // use TM validation if destination SRF is TM
                retValid = CoordCheck.forTransverseMercator_cd(this.getOrmData(),
                        ((SRF_TransverseMercator) destSrf).getSRFParameters(),
                        src);
            }

            toTmer(srcSrf, destSrf, src, dest);

            // apply the false origin offset after the conversion to TM
            dest[0] += ((SRF_TransverseMercator) destSrf).get_false_easting();
            dest[1] += ((SRF_TransverseMercator) destSrf).get_false_northing();
        } else if (destSrfType == SRM_SRFT_Code.SRFT_MERCATOR) {
            // perform pre validation
            retValid = CoordCheck.forCelestiodetic(this.getOrmData(), src);
            toMerc(srcSrf, destSrf, src, dest);

            // apply the false origin offset after the conversion to M
            dest[0] += ((SRF_Mercator) destSrf).get_false_easting();
            dest[1] += ((SRF_Mercator) destSrf).get_false_northing();
        } else if (destSrfType == SRM_SRFT_Code.SRFT_LAMBERT_CONFORMAL_CONIC) {
            // perform pre validation
            retValid = CoordCheck.forCelestiodetic(this.getOrmData(), src);
            toLcc(srcSrf, destSrf, src, dest);

            // apply the false origin offset after the conversion to LCC
            dest[0] += ((SRF_LambertConformalConic) destSrf).get_false_easting();
            dest[1] += ((SRF_LambertConformalConic) destSrf).get_false_northing();
        } else if (destSrfType == SRM_SRFT_Code.SRFT_UNDEFINED) {
            // just pass the coordinate through.  This is the last conversion of chain
            dest[0] = src[0];
            dest[1] = src[1];
            dest[2] = src[2];
        }

        return retValid;
    }

    /*
     *----------------------------------------------------------------------------
     *
     * CDET to CCEN routines
     *
     *----------------------------------------------------------------------------
     */
    private void toCcen(BaseSRF srcSrf, BaseSRF destSrf, double[] src,
        double[] dest) throws SrmException {
        double rn;
        double Rnc2a2;
        double Rnhc;
        double slat;
        double temp;
        double srcLon = src[0];
        double srcLat = src[1];
        double srcHeight = src[2];
        OrmData e_constants = this.getOrmData();

        slat = Math.sin(srcLat);

        if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
            temp = e_constants.Eps25 * slat * slat;
            temp = 0.5 - temp;
            temp = temp + ((temp - .25) / temp);
            rn = (e_constants.A) / temp;
            Rnc2a2 = (rn * e_constants.C2) / e_constants.A2;
        } else /* compute spherical values */
         {
            rn = e_constants.A;
            Rnc2a2 = rn;
        }

        Rnhc = (rn + srcHeight) * Math.cos(srcLat);

        dest[0] = Rnhc * Math.cos(srcLon);
        dest[1] = Rnhc * Math.sin(srcLon);
        dest[2] = (Rnc2a2 + srcHeight) * slat;
    }

    /* FUNCTION delta_lambda_min
     * This function retunrs the absolute value of the smallest change in longitude
     * difference between two values of longitude.  It is used because it
     * is difficult to get right the test for within range or not in UTM and TM
     */
    private double delta_lambda_min(double lambda1, double lambda2) {
        double simple_difference;

        if (lambda2 >= lambda1) {
            simple_difference = lambda2 - lambda1;

            if (Math.abs(simple_difference) >= Const.PI) {
                return ((Const.TWO_PI - lambda2) + lambda1) % Const.TWO_PI;
            } else {
                return simple_difference;
            }
        } else {
            simple_difference = lambda1 - lambda2;

            if (Math.abs(simple_difference) >= Const.PI) {
                return ((Const.TWO_PI - lambda1) + lambda2) % Const.TWO_PI;
            } else {
                return simple_difference;
            }
        }
    }

    // This method takes an "intermediate" format so that the TM Conv
    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     * @param source_generic_coordinate DOCUMENT ME!
     * @param dest_generic_coordinate DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected void toTmer(SRF_Mercator_Params params,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        double dest_x_ptr;
        double dest_y_ptr;
        double dest_z_ptr;
        double source_elv = 0.0;
        double source_lat = 0.0;
        double source_lon = 0.0;
        double al;
        double al2;
        double cee;
        double clat;
        double lambda_star;
        double poly1;
        double poly2;
        double rn;
        double slat;
        double slat2;
        double sm;
        double temp;
        double tn2;
        double tx;
        OrmData e_constants = this.getOrmData();

        // This works because the intermediate STFs are either CCEN
        // or CDET.  Only source or destination SRFs can be TM.  Since
        // This is a conversion to TM, then the TM must be the
        // destination SRF.
        if (_toTmerConst == null) {
            _toTmerConst = new ToTmerConst(e_constants, params);
        }

        source_lon = source_generic_coordinate[0];
        source_lat = source_generic_coordinate[1];
        source_elv = source_generic_coordinate[2];

        slat = Math.sin(source_lat);
        clat = Math.cos(source_lat);
        tx = slat / clat;
        tn2 = tx * tx;
        slat2 = slat * slat;

        /*!\bug Note that the changes made to replace dlon with
          lambda_star must be made to the convergenc eof the meridian
          routines
        */
        lambda_star = Const.getLambdaStar(source_lon,
                _toTmerConst._longitude_origin);
        al = lambda_star * clat;
        al2 = al * al;

        poly1 = 1.0 - tn2;
        poly2 = 5.0 + (tn2 * (tn2 - 18.0));

        if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
            /* USE IN-LINE SQUARE ROOT */
            temp = e_constants.Eps25 * slat2;
            temp = 0.5 - temp;
            temp = temp + ((temp - .25) / temp);
            rn = (e_constants.A) / temp;
            cee = e_constants.Epps2 * clat * clat;

            poly1 = poly1 + cee;
            poly2 = poly2 + (cee * (14.0 - (tn2 * 58.0)));

            sm = slat * clat * (_toTmerConst._poly2b +
                (slat2 * (_toTmerConst._poly3b +
                (slat2 * (_toTmerConst._poly4b +
                (slat2 * _toTmerConst._poly5b))))));
            sm = e_constants.A * ((_toTmerConst._poly1b * source_lat) + sm);
        } else /* compute spherical values */
         {
            rn = e_constants.A;
            sm = e_constants.A * source_lat;
            cee = 0.0;
        }

        /* COMPUTE EASTING */
        dest_x_ptr = _toTmerConst._CScale * rn * al * (1.0 +
            (al2 * ((_toTmerConst._Cdb6 * poly1) +
            (_toTmerConst._Cdb120 * al2 * poly2))));

        /* COMPUTE NORTHING */
        poly1 = 5.0 - tn2;
        poly2 = 61.0 + (tn2 * (tn2 - 58.0));

        if (e_constants.Eps != 0.0) /* ellipsoid calcs */ {
            poly1 = poly1 + (cee * ((cee * 4.0) + 9.0));
            poly2 = poly2 + (cee * (270.0 - (tn2 * 330.0)));
        }

        dest_y_ptr = _toTmerConst._CScale * (sm - _toTmerConst._smz +
            (rn * tx * al2 * (0.5 +
            (al2 * ((_toTmerConst._Cdb24 * poly1) +
            (_toTmerConst._Cdb720 * al2 * poly2))))));

        dest_z_ptr = source_elv;

        dest_generic_coordinate[0] = dest_x_ptr;
        dest_generic_coordinate[1] = dest_y_ptr;
        dest_generic_coordinate[2] = dest_z_ptr;
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
    private void toTmer(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        toTmer(((SRF_TransverseMercator) destSrf).getSRFParameters(),
            source_generic_coordinate, dest_generic_coordinate);
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
    private void toMerc(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        double source_elv;
        double source_lat;
        double source_lon;
        double dest_x;
        double dest_y;
        double p;
        double sl;

        OrmData e_constants = this.getOrmData();

        if (_toMercConst == null) {
            _toMercConst = new ToMercConst(e_constants,
                    ((SRF_Mercator) destSrf).getSRFParameters());
        }

        source_lon = Const.getLambdaStar(source_generic_coordinate[0],
                _toMercConst.longitude_origin);
        source_lat = source_generic_coordinate[1];
        source_elv = source_generic_coordinate[2];

        sl = Math.sin(source_lat);

        if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
            double absl;

            absl = Math.abs(sl);

            if (absl <= (35.5 * Const.RADIANS_PER_DEGREE)) {
                p = _toMercConst.CR11 +
                    (((_toMercConst.CR12 * absl) + _toMercConst.CR13) / (_toMercConst.CR14 +
                    (absl * (_toMercConst.CR15 + absl))));
            } else {
                p = _toMercConst.CR21 +
                    (((_toMercConst.CR22 * absl) + _toMercConst.CR23) / (_toMercConst.CR24 +
                    (absl * (_toMercConst.CR25 + absl))));
            }

            if (sl < 0.0) {
                p = 1.0 / p;
            }

            p = p * Math.sqrt((1.0 + sl) / (1.0 - sl));
        } else { /* compute spherical values */
            p = Math.sqrt((1.0 + sl) / (1.0 - sl));
        }

        /* fix quadrant on longitude */
        source_lon = Const.fix_longitude(source_lon);

        /* COMPUTE NORTHING AND EASTING */
        dest_x = _toMercConst.scale * _toMercConst.Cos_Rn_Origin_Lat * source_lon;

        dest_y = _toMercConst.scale * _toMercConst.Cos_Rn_Origin_Lat * Math.log(p);

        dest_generic_coordinate[0] = dest_x;
        dest_generic_coordinate[1] = dest_y;
        dest_generic_coordinate[2] = source_elv;
    }

    /*----------------------------------------------------------------------------
     *
     * CDET to LCC1 routines and classes
     *
     *----------------------------------------------------------------------------
     */
    private void toLcc(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        double latitude1 = (((SRF_LambertConformalConic) destSrf).get_latitude1());
        double latitude2 = (((SRF_LambertConformalConic) destSrf).get_latitude2());

        // use lcc1 is north and south parallels are the same
        if (Math.abs(latitude1 - latitude2) < 0.0001) {
            toLcc1(srcSrf, destSrf, source_generic_coordinate,
                dest_generic_coordinate);
        } else { // use lcc2 otherwise
            toLcc2(srcSrf, destSrf, source_generic_coordinate,
                dest_generic_coordinate);
        }
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
    protected void toLcc1(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        OrmData e_constants = this.getOrmData();

        if (_toLccConst1 == null) {
            _toLccConst1 = new ToLccConst1(e_constants,
                    ((SRF_LambertConformalConic) destSrf).getSRFParameters());
        }

        double dest_x;
        double dest_y;
        double source_elv;
        double source_lat;
        double source_lon;
        double arg;
        double dlon;
        double p;
        double r;
        double s1;
        double sl;
        double t;
        double local_source_elv;

        source_lon = source_generic_coordinate[0];
        source_lat = source_generic_coordinate[1];
        source_elv = source_generic_coordinate[2];

        local_source_elv = source_elv;

        sl = Math.sin(source_lat);

        if (e_constants.Eps != 0.0) /* do ellipsoid calcs */ {
            double absl = Math.abs(sl);

            if (absl <= (35 * Const.RADIANS_PER_DEGREE)) {
                p = _toLccConst1._CR11 +
                    (((_toLccConst1._CR12 * absl) + _toLccConst1._CR13) / (_toLccConst1._CR14 +
                    (absl * (_toLccConst1._CR15 + absl))));
            } else {
                p = _toLccConst1._CR21 +
                    (((_toLccConst1._CR22 * absl) + _toLccConst1._CR23) / (_toLccConst1._CR24 +
                    (absl * (_toLccConst1._CR25 + absl))));
            }

            if (sl < 0.0) {
                p = 1.0 / p;
            }
        } /* end if ellipsoid */ else /* for sphere */
         {
            p = 1.0;
        }

        if (sl < .9998476952) { /*sl < sin (89 degrees)*/
            p = p * Math.sqrt((1 + sl) / (1 - sl));
        } else {
            p = p * Math.tan(Const.PI_DIV_4 + (.5 * source_lat));
        }

        r = _toLccConst1._rz * Math.pow(_toLccConst1._pz / p, _toLccConst1._xl);

        dlon = Const.getLambdaStar(source_lon, _toLccConst1._xlonz);

        arg = _toLccConst1._xl * dlon;
        s1 = Math.sin(arg);
        t = Math.cos(arg);

        dest_x = r * s1;
        dest_y = _toLccConst1._rho_origin - (r * t);

        /* fix signs */
        if (_toLccConst1._xl < 0.0) {
            dest_x = -dest_x;
            dest_y = -dest_y;
        }

        dest_generic_coordinate[0] = dest_x;
        dest_generic_coordinate[1] = dest_y;
        dest_generic_coordinate[2] = source_elv;
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
    protected void toLcc2(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        OrmData e_constants = this.getOrmData();

        if (_toLccConst2 == null) {
            _toLccConst2 = new ToLccConst2(e_constants,
                    ((SRF_LambertConformalConic) destSrf).getSRFParameters());
        }

        double dest_x;
        double dest_y;
        double source_elv;
        double source_lat;
        double source_lon;
        double arg;
        double dlon;
        double p;
        double r;
        double s1;
        double sl;
        double t;

        double local_source_elv;

        source_lon = source_generic_coordinate[0];
        source_lat = source_generic_coordinate[1];
        source_elv = source_generic_coordinate[2];

        local_source_elv = source_elv;

        sl = Math.sin(source_lat);

        if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
            double absl = Math.abs(sl);

            if (absl <= (35.5 * Const.RADIANS_PER_DEGREE)) {
                p = _toLccConst2._CR11 +
                    (((_toLccConst2._CR12 * sl) + _toLccConst2._CR13) / (_toLccConst2._CR14 +
                    (sl * (_toLccConst2._CR15 + sl))));
            } else {
                p = _toLccConst2._CR21 +
                    (((_toLccConst2._CR22 * sl) + _toLccConst2._CR23) / (_toLccConst2._CR24 +
                    (sl * (_toLccConst2._CR25 + sl))));
            }

            if (sl < 0.0) {
                p = 1.0 / p;
            }
        } else /* compute spherical values */
         {
            p = 1.0;
        }

        /*
         * if sl < sin(89 degrees)
         */
        if (sl < .9998476952) {
            p = p * Math.sqrt((1.0 + sl) / (1.0 - sl));
        } else {
            p = p * Math.tan(Const.PI_DIV_4 + (.5 * source_lat));
        }

        r = _toLccConst2._rx * Math.pow(_toLccConst2._p1 / p, _toLccConst2._xl);

        dlon = Const.getLambdaStar(source_lon, _toLccConst2._xlonz);

        arg = _toLccConst2._xl * dlon;

        s1 = Math.sin(arg);
        t = Math.cos(arg);

        dest_x = r * s1;
        dest_y = _toLccConst2._rz - (r * t);

        if (_toLccConst2._xl < 0) {
            dest_x = -dest_x;
            dest_y = -dest_y;
        }

        dest_generic_coordinate[0] = dest_x;
        dest_generic_coordinate[1] = dest_y;
        dest_generic_coordinate[2] = source_elv;
    }

    /*
     *----------------------------------------------------------------------------
     *
     * CDET to TMER routines and classes
     *
     *----------------------------------------------------------------------------
     */
    private class ToTmerConst {
        /** DOCUMENT ME! */
        double _Cdb6;

        /** DOCUMENT ME! */
        double _Cdb24;

        /** DOCUMENT ME! */
        double _Cdb120;

        /** DOCUMENT ME! */
        double _Cdb720;

        /** DOCUMENT ME! */
        double _CScale;

        /** DOCUMENT ME! */
        double _poly1b;

        /** DOCUMENT ME! */
        double _poly2b;

        /** DOCUMENT ME! */
        double _poly3b;

        /** DOCUMENT ME! */
        double _poly4b;

        /** DOCUMENT ME! */
        double _poly5b;

        /** DOCUMENT ME! */
        double _longitude_origin;

        /** DOCUMENT ME! */
        double _smz;

/**
         * Creates a new ToTmerConst object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         */
        public ToTmerConst(OrmData e_constants, SRF_Mercator_Params params) {
            double polx2b;
            double polx3b;
            double polx4b;
            double polx5b;
            double s1;
            double s12;
            double c1;
            double latitude_origin;

            latitude_origin = params.origin_latitude;

            _longitude_origin = params.origin_longitude;
            _CScale = params.central_scale;

            _Cdb6 = .166666666666667;
            _Cdb24 = .0416666666666667;
            _Cdb120 = .00833333333333333;
            _Cdb720 = .00138888888888888;

            if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
                s1 = Math.sin(latitude_origin);
                s12 = s1 * s1;
                c1 = Math.cos(latitude_origin);

                polx2b = ((1.0 * e_constants.Eps2) +
                    (1.0 / 4.0 * Math.pow(e_constants.Eps2, 2)) +
                    (15.0 / 128.0 * Math.pow(e_constants.Eps2, 3))) -
                    (455.0 / 4096.0 * Math.pow(e_constants.Eps2, 4));

                polx2b = 3.0 / 8.0 * polx2b;

                polx3b = ((1.0 * Math.pow(e_constants.Eps2, 2)) +
                    (3.0 / 4.0 * Math.pow(e_constants.Eps2, 3))) -
                    (77.0 / 128.0 * Math.pow(e_constants.Eps2, 4));

                polx3b = 15.0 / 256.0 * polx3b;

                polx4b = Math.pow(e_constants.Eps2, 3) -
                    (41.0 / 32.0 * Math.pow(e_constants.Eps2, 4));

                polx4b = (polx4b * 35.0) / 3072.0;

                polx5b = -315.0 / 131072.0 * Math.pow(e_constants.Eps2, 4);

                _poly1b = 1.0 - (0.25 * e_constants.Eps2) -
                    (3.0 / 64.0 * Math.pow(e_constants.Eps2, 2)) -
                    (5.0 / 256.0 * Math.pow(e_constants.Eps2, 3)) -
                    (175.0 / 16384.0 * Math.pow(e_constants.Eps2, 4));

                _poly2b = ((polx2b * -2.0) + (polx3b * 4.0)) - (polx4b * 6.0) +
                    (polx5b * 8.0);

                _poly3b = ((polx3b * -8.0) + (polx4b * 32.0)) -
                    (polx5b * 80.0);

                _poly4b = (polx4b * -32.0) + (polx5b * 192.0);

                _poly5b = polx5b * -128.0;

                _smz = s1 * c1 * (_poly2b +
                    (s12 * (_poly3b + (s12 * (_poly4b + (s12 * _poly5b))))));
                _smz = e_constants.A * ((_poly1b * latitude_origin) + _smz);
            } else { /* compute spherical values */
                _smz = e_constants.A * latitude_origin;
            }
        }
    }

    /*
     *----------------------------------------------------------------------------
     *
     * CDET to Mercator routines and classes
     *
     *----------------------------------------------------------------------------
     */
    private class ToMercConst {
        /** DOCUMENT ME! */
        double CR11;

        /** DOCUMENT ME! */
        double CR12;

        /** DOCUMENT ME! */
        double CR13;

        /** DOCUMENT ME! */
        double CR14;

        /** DOCUMENT ME! */
        double CR15;

        /** DOCUMENT ME! */
        double CR21;

        /** DOCUMENT ME! */
        double CR22;

        /** DOCUMENT ME! */
        double CR23;

        /** DOCUMENT ME! */
        double CR24;

        /** DOCUMENT ME! */
        double CR25;

        /** DOCUMENT ME! */
        double scale;

        /** DOCUMENT ME! */
        double Cos_Rn_Origin_Lat;

        /** DOCUMENT ME! */
        double longitude_origin;

        /** DOCUMENT ME! */
        double latitude_origin;

/**
         * Creates a new ToMercConst object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         */
        public ToMercConst(OrmData e_constants, SRF_Mercator_Params params) {
            double AA1;
            double AA2;
            double AA3;
            double AA4;
            double AA5;
            double AA6;
            double AA7;
            double AA8;
            double AA9;
            double AB1;
            double AB2;
            double AB3;
            double AB4;
            double AB5;
            double AB6;
            double AB7;
            double AB8;
            double AB9;
            double BB1;
            double BB2;
            double BB3;
            double BB4;
            double BB5;

            longitude_origin = params.origin_longitude;
            latitude_origin = params.origin_latitude;
            scale = params.central_scale;

            AA1 = 0.100000000003490E+01;
            AA2 = -0.643155723158021;
            AA3 = -0.333332134894985;
            AA4 = -0.241457540671514E-04;
            AA5 = 0.143376648162652;
            AA6 = 0.356844276587295;
            AA7 = -0.333332875955149;
            AA8 = 0.0;
            AA9 = 0.0;

            BB1 = AA1;
            BB2 = e_constants.Eps2 * AA2;
            BB3 = e_constants.Eps2 * (AA3 +
                (e_constants.Eps * (AA4 + (e_constants.Eps * AA5))));
            BB4 = e_constants.Eps2 * AA6;
            BB5 = e_constants.Eps2 * (AA7 +
                (e_constants.Eps * (AA8 + (e_constants.Eps * AA9))));

            CR11 = (BB3 / BB5) - .187E-11;
            CR12 = (BB2 - ((BB4 * BB3) / BB5)) / BB5;
            CR13 = (BB1 - (BB3 / BB5)) / BB5;
            CR14 = 1.0 / BB5;
            CR15 = BB4 / BB5;

            AB1 = 0.999999999957885;
            AB2 = -0.115979311942142E+01;
            AB3 = -0.333339671395063;
            AB4 = 0.276473457331734E-03;
            AB5 = 0.587786240368508;
            AB6 = -0.159793128888088;
            AB7 = -0.333333465982150;
            AB8 = 0.746505041501704E-04;
            AB9 = -0.701559218182283E-01;

            BB1 = AB1;
            BB2 = e_constants.Eps2 * AB2;
            BB3 = e_constants.Eps2 * (AB3 +
                (e_constants.Eps * (AB4 + (e_constants.Eps * AB5))));
            BB4 = e_constants.Eps2 * AB6;
            BB5 = e_constants.Eps2 * (AB7 +
                (e_constants.Eps * (AB8 + (e_constants.Eps * AB9))));

            CR21 = (BB3 / BB5) + .2689E-11;
            CR22 = (BB2 - ((BB4 * BB3) / BB5)) / BB5;
            CR23 = (BB1 - (BB3 / BB5)) / BB5;
            CR24 = 1.0 / BB5;
            CR25 = BB4 / BB5;

            Cos_Rn_Origin_Lat = Math.cos(latitude_origin) * Const.computeRn(latitude_origin,
                    e_constants);
        }
    }

    /*----------------------------------------------------------------------------
     *
     * CDET to LCC1 routines and classes
     *
     *----------------------------------------------------------------------------
     */

    // this is for the case where the northern parallel is the same
    // as the southern parallel.
    private class ToLccConst1 {
        /** DOCUMENT ME! */
        double _CR11;

        /** DOCUMENT ME! */
        double _CR12;

        /** DOCUMENT ME! */
        double _CR13;

        /** DOCUMENT ME! */
        double _CR14;

        /** DOCUMENT ME! */
        double _CR15;

        /** DOCUMENT ME! */
        double _CR21;

        /** DOCUMENT ME! */
        double _CR22;

        /** DOCUMENT ME! */
        double _CR23;

        /** DOCUMENT ME! */
        double _CR24;

        /** DOCUMENT ME! */
        double _CR25;

        /** DOCUMENT ME! */
        double _xlonz;

        /** DOCUMENT ME! */
        double _xl;

        /** DOCUMENT ME! */
        double _pz;

        /** DOCUMENT ME! */
        double _rz;

        /** DOCUMENT ME! */
        double _rho_origin;

/**
         * Creates a new ToLccConst1 object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         */
        public ToLccConst1(OrmData e_constants, SRF_LCC_Params params) {
            double AA1;
            double AA2;
            double AA3;
            double AA4;
            double AA5;
            double AA6;
            double AA7;
            double AA8;
            double AA9;
            double AB1;
            double AB2;
            double AB3;
            double AB4;
            double AB5;
            double AB6;
            double AB7;
            double AB8;
            double AB9;
            double BB1;
            double BB2;
            double BB3;
            double BB4;
            double BB5;
            double cl2;
            double phiz;
            double pz;
            double rn2;
            double rz2;
            double xl;
            double rz;
            double xl2;
            double porg;
            double latitude_origin;
            double sin_org;

            phiz = params.latitude1;
            _xlonz = params.origin_longitude;
            latitude_origin = params.origin_latitude;

            xl = Math.sin(phiz);
            xl2 = xl * xl;
            cl2 = 1 - xl2;

            rn2 = e_constants.A2 / (1 - (e_constants.Eps2 * xl2));
            rz2 = (rn2 * cl2) / xl2;
            rz = Math.sqrt(rz2);

            pz = Math.tan((Const.PI / 4) + (0.5 * phiz));

            sin_org = Math.sin(latitude_origin);

            porg = Math.tan((Const.PI / 4) + (0.5 * latitude_origin));

            if (e_constants.Eps != 0) /* ellipsoid calcs */ {
                AA1 = 0.100000000003490E+01;
                AA2 = -0.643155723158021;
                AA3 = -0.333332134894985;
                AA4 = -0.241457540671514E-04;
                AA5 = 0.143376648162652;
                AA6 = 0.356844276587295;
                AA7 = -0.333332875955149;
                AA8 = 0.0;
                AA9 = 0.0;

                BB1 = AA1;
                BB2 = e_constants.Eps2 * AA2;
                BB3 = e_constants.Eps2 * (AA3 +
                    (e_constants.Eps * (AA4 + (e_constants.Eps * AA5))));
                BB4 = e_constants.Eps2 * AA6;
                BB5 = e_constants.Eps2 * (AA7 +
                    (e_constants.Eps * (AA8 + (e_constants.Eps * AA9))));

                _CR11 = (BB3 / BB5) - .187E-11;
                _CR12 = (BB2 - ((BB4 * BB3) / BB5)) / BB5;
                _CR13 = (BB1 - (BB3 / BB5)) / BB5;
                _CR14 = 1.0 / BB5;
                _CR15 = BB4 / BB5;

                AB1 = 0.999999999957885;
                AB2 = -0.115979311942142E+01;
                AB3 = -0.333339671395063;
                AB4 = 0.276473457331734E-03;
                AB5 = 0.587786240368508;
                AB6 = -0.159793128888088;
                AB7 = -0.333333465982150;
                AB8 = 0.746505041501704E-04;
                AB9 = -0.701559218182283E-01;

                BB1 = AB1;
                BB2 = e_constants.Eps2 * AB2;
                BB3 = e_constants.Eps2 * (AB3 +
                    (e_constants.Eps * (AB4 + (e_constants.Eps * AB5))));
                BB4 = e_constants.Eps2 * AB6;
                BB5 = e_constants.Eps2 * (AB7 +
                    (e_constants.Eps * (AB8 + (e_constants.Eps * AB9))));

                _CR21 = (BB3 / BB5) - .2689E-11;
                _CR22 = (BB2 - ((BB4 * BB3) / BB5)) / BB5;
                _CR23 = (BB1 - (BB3 / BB5)) / BB5;
                _CR24 = 1.0 / BB5;
                _CR25 = BB4 / BB5;

                pz = Math.pow((1.0 - (e_constants.Eps * xl)) / ((e_constants.Eps * xl) +
                        1.0), e_constants.EpsH) * pz;

                porg = Math.pow((1.0 - (e_constants.Eps * sin_org)) / ((e_constants.Eps * sin_org) +
                        1.0), e_constants.EpsH) * porg;
            } else /* for sphere */
             {
                rz = Math.sqrt((e_constants.A2 * cl2) / xl2);
            }

            _xl = xl;
            _pz = pz;
            _rz = rz;

            /* prevent divide by zero  */
            if (Math.abs(porg) <= .000000000001) {
                pz = 0.0;
            } else {
                pz = pz / porg;
            }

            _rho_origin = rz * Math.pow(pz, xl);
        }
    }

    /*----------------------------------------------------------------------------
     *
     * CDET to LCC2 routines and classes
     *
     *----------------------------------------------------------------------------
     */

    // this is for the case where the northern parallel is different
    // from the southern parallel.
    private class ToLccConst2 {
        /** DOCUMENT ME! */
        double _CR11;

        /** DOCUMENT ME! */
        double _CR12;

        /** DOCUMENT ME! */
        double _CR13;

        /** DOCUMENT ME! */
        double _CR14;

        /** DOCUMENT ME! */
        double _CR15;

        /** DOCUMENT ME! */
        double _CR21;

        /** DOCUMENT ME! */
        double _CR22;

        /** DOCUMENT ME! */
        double _CR23;

        /** DOCUMENT ME! */
        double _CR24;

        /** DOCUMENT ME! */
        double _CR25;

        /** DOCUMENT ME! */
        double _xlonz;

        /** DOCUMENT ME! */
        double _rx;

        /** DOCUMENT ME! */
        double _rz;

        /** DOCUMENT ME! */
        double _p1;

        /** DOCUMENT ME! */
        double _xl;

/**
         * Creates a new ToLccConst2 object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         */
        public ToLccConst2(OrmData e_constants, SRF_LCC_Params params) {
            double AA1;
            double AA2;
            double AA3;
            double AA4;
            double AA5;
            double AA6;
            double AA7;
            double AA8;
            double AA9;
            double AB1;
            double AB2;
            double AB3;
            double AB4;
            double AB5;
            double AB6;
            double AB7;
            double AB8;
            double AB9;
            double BB1;
            double BB2;
            double BB3;
            double BB4;
            double BB5;
            double phiz1;
            double phiz2;
            double latitude_origin;
            double sfx;
            double pz;
            double s1;
            double s12;
            double c12;
            double p1;
            double s2;
            double s22;
            double p2;
            double rn12;
            double rn22;
            double temp;
            double xl;
            double rx2;
            double rz;
            double c22;
            double rx;

            phiz1 = params.latitude1;
            phiz2 = params.latitude2;
            _xlonz = params.origin_longitude;
            latitude_origin = params.origin_latitude;

            /*
             * test whether this is a spherical transformation(Eps=0);
             * if not, then we compute these constants
             */
            if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
                AA1 = 0.100000000003490E+01;
                AA2 = -0.643155723158021;
                AA3 = -0.333332134894985;
                AA4 = -0.241457540671514E-04;
                AA5 = 0.143376648162652;
                AA6 = 0.356844276587295;
                AA7 = -0.333332875955149;
                AA8 = 0.0;
                AA9 = 0.0;

                BB1 = AA1;
                BB2 = e_constants.Eps2 * AA2;
                BB3 = e_constants.Eps2 * (AA3 +
                    (e_constants.Eps * (AA4 + (e_constants.Eps * AA5))));
                BB4 = e_constants.Eps2 * AA6;
                BB5 = e_constants.Eps2 * (AA7 +
                    (e_constants.Eps * (AA8 + (e_constants.Eps * AA9))));

                _CR11 = (BB3 / BB5) - .187E-11;
                _CR12 = (BB2 - ((BB4 * BB3) / BB5)) / BB5;
                _CR13 = (BB1 - (BB3 / BB5)) / BB5;
                _CR14 = 1.0 / BB5;
                _CR15 = BB4 / BB5;

                AB1 = 0.999999999957885;
                AB2 = -0.115979311942142E+01;
                AB3 = -0.333339671395063;
                AB4 = 0.276473457331734E-03;
                AB5 = 0.587786240368508;
                AB6 = -0.159793128888088;
                AB7 = -0.333333465982150;
                AB8 = 0.746505041501704E-04;
                AB9 = -0.701559218182283E-01;

                BB1 = AB1;
                BB2 = e_constants.Eps2 * AB2;
                BB3 = e_constants.Eps2 * (AB3 +
                    (e_constants.Eps * (AB4 + (e_constants.Eps * AB5))));
                BB4 = e_constants.Eps2 * AB6;
                BB5 = e_constants.Eps2 * (AB7 +
                    (e_constants.Eps * (AB8 + (e_constants.Eps * AB9))));

                _CR21 = (BB3 / BB5) - .2689E-11;
                _CR22 = (BB2 - ((BB4 * BB3) / BB5)) / BB5;
                _CR23 = (BB1 - (BB3 / BB5)) / BB5;
                _CR24 = 1.0 / BB5;
                _CR25 = BB4 / BB5;
            } /* end if not spherical */
            sfx = Math.sin(latitude_origin);
            s1 = Math.sin(phiz1);
            s2 = Math.sin(phiz2);

            s12 = s1 * s1;
            c12 = 1 - s12;

            s22 = s2 * s2;
            c22 = 1 - s22;

            pz = Math.tan((Const.PI / 4.0) + (0.5 * latitude_origin));
            p1 = Math.tan((Const.PI / 4.0) + (0.5 * phiz1));
            p2 = Math.tan((Const.PI / 4.0) + (0.5 * phiz2));

            if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
                pz = pz * Math.pow((1.0 - (e_constants.Eps * sfx)) / ((e_constants.Eps * sfx) +
                        1.0), e_constants.EpsH);

                p1 = p1 * Math.pow((1.0 - (e_constants.Eps * s1)) / ((e_constants.Eps * s1) +
                        1.0), e_constants.EpsH);

                p2 = p2 * Math.pow((1.0 - (e_constants.Eps * s2)) / ((e_constants.Eps * s2) +
                        1.0), e_constants.EpsH);

                rn12 = e_constants.A2 / (1.0 - (e_constants.Eps2 * s12));
                rn22 = e_constants.A2 / (1.0 - (e_constants.Eps2 * s22));
                temp = p2 / p1;
                xl = (.5 * Math.log((rn12 * c12) / (c22 * rn22))) / Math.log(temp);
            } else /* compute spherical values */
             {
                rn12 = e_constants.A2;
                temp = p2 / p1;
                xl = (0.5 * Math.log(c12 / c22)) / Math.log(temp);
            }

            rx2 = (rn12 * c12) / (xl * xl);
            rx = Math.sqrt(rx2);
            rz = rx * Math.pow(p1 / pz, xl);

            _rx = rx;
            _rz = rz;
            _xl = xl;
            _p1 = p1;
        }
    }
}
