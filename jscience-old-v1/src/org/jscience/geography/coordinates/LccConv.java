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
class LccConv extends Conversions {
    /** DOCUMENT ME! */
    private ToCdetConst1 _toCdetConst1;

    /** DOCUMENT ME! */
    private ToCdetConst2 _toCdetConst2;

/**
     * Creates a new LccConv object.
     */
    public LccConv() {
        super(SRM_SRFT_Code.SRFT_LAMBERT_CONFORMAL_CONIC,
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
        return (Conversions) new LccConv();
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
            src[0] -= ((SRF_LambertConformalConic) srcSrf).get_false_easting();
            src[1] -= ((SRF_LambertConformalConic) srcSrf).get_false_northing();
            toCdet(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forCelestiodetic(this.getOrmData(), dest);
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
     * FUNCTION: toCdet
     *
     *----------------------------------------------------------------------------
     */
    protected void toCdet(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coord, double[] dest_generic_coord)
        throws SrmException {
        double latitude1 = (((SRF_LambertConformalConic) srcSrf).get_latitude1());
        double latitude2 = (((SRF_LambertConformalConic) srcSrf).get_latitude2());

        // use lcc1 is north and south parallels are the same
        if (Math.abs(latitude1 - latitude2) < Const.EPSILON) {
            toCdet1(srcSrf, destSrf, source_generic_coord, dest_generic_coord);
        } else { // use lcc2 otherwise
            toCdet2(srcSrf, destSrf, source_generic_coord, dest_generic_coord);
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
    protected void toCdet1(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        OrmData e_constants = this.getOrmData();
        double dest_lat;
        double dest_lon;
        double source_x;
        double source_y;
        double source_z;
        double cki;
        double den;
        double drz;
        double r2;
        double ski;
        double tanz2;
        double t2;
        double thet;
        double xki;

        if (_toCdetConst1 == null) {
            _toCdetConst1 = new ToCdetConst1(e_constants,
                    ((SRF_LambertConformalConic) srcSrf).getSRFParameters());
        }

        source_x = source_generic_coordinate[0];
        source_y = source_generic_coordinate[1];
        source_z = source_generic_coordinate[2];

        if (_toCdetConst1._xl < 0.0) {
            source_x = -source_x;
            source_y = -source_y;
        }

        drz = _toCdetConst1._rho_origin - source_y;
        thet = Math.atan2(source_x, drz);
        dest_lon = (thet / _toCdetConst1._xl) + _toCdetConst1._xlonz;

        /* get latitude using inverse power series */
        /* Get r squared  */
        r2 = (source_x * source_x) + (drz * drz);
        tanz2 = Math.pow(r2 / _toCdetConst1._rz2, _toCdetConst1._ex) / _toCdetConst1._pz;

        if (e_constants.Eps != 0.0) /* do ellipsoid calculations */ {
            double sk2;
            double z;

            /* Use Identity to get sin & cos without trig functions */
            t2 = tanz2 * tanz2;
            den = 1.0 + t2;
            cki = (2 * tanz2) / den;
            ski = (1.0 - t2) / den;

            sk2 = ski * ski;
            z = 2 * Math.atan(tanz2);
            xki = (Const.PI / 2) - z;

            dest_lat = xki +
                (ski * cki * (_toCdetConst1._b1 +
                (sk2 * (_toCdetConst1._b2 +
                (sk2 * (_toCdetConst1._b3 + (sk2 * _toCdetConst1._b4)))))));
        } else { /* do spherical calculations */
            dest_lat = (Const.PI_DIV_2) - (2 * Math.atan(tanz2));
        }

        dest_lon = Const.fix_longitude(dest_lon);

        dest_generic_coordinate[0] = dest_lon;
        dest_generic_coordinate[1] = dest_lat;
        dest_generic_coordinate[2] = source_z;
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
    protected void toCdet2(BaseSRF srcSrf, BaseSRF destSrf,
        double[] source_generic_coordinate, double[] dest_generic_coordinate)
        throws SrmException {
        OrmData e_constants = this.getOrmData();

        double dest_lat;
        double dest_lon;
        double source_x;
        double source_y;
        double source_z;
        double drz;
        double r2;
        double tanz2;
        double thet;

        if (_toCdetConst2 == null) {
            _toCdetConst2 = new ToCdetConst2(e_constants,
                    ((SRF_LambertConformalConic) srcSrf).getSRFParameters());
        }

        source_x = source_generic_coordinate[0];
        source_y = source_generic_coordinate[1];
        source_z = source_generic_coordinate[2];

        /*
        * LATITUDE FIRST USING INVERSE POWER SERIES
        */
        if (_toCdetConst2._xl < 0.0) {
            source_x = -source_x;
            source_y = -source_y;
        }

        drz = _toCdetConst2._rz - source_y;

        /* Get r2 */
        r2 = (source_x * source_x) + (drz * drz);

        tanz2 = Math.pow(r2 / _toCdetConst2._rz2, _toCdetConst2._ex) / _toCdetConst2._p1;

        if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
            double cki;
            double den;
            double sk2;
            double ski;
            double t2;
            double xki;
            double z;

            /*
             * Use Identity to get sin & cos without trig functions
             */
            t2 = tanz2 * tanz2;
            den = 1.0 + t2;
            cki = (2.0 * tanz2) / den;
            ski = (1.0 - t2) / den;

            /* Sin and Cos of xki now done */
            /* Use inverse power series for latitude */
            sk2 = ski * ski;
            z = 2 * Math.atan(tanz2);
            xki = Const.PI_DIV_2 - z;

            dest_lat = xki +
                (ski * cki * (_toCdetConst2._b1 +
                (sk2 * (_toCdetConst2._b2 +
                (sk2 * (_toCdetConst2._b3 + (sk2 * _toCdetConst2._b4)))))));
        } else /* compute spherical values */
         {
            dest_lat = Const.PI_DIV_2 - (2 * Math.atan(tanz2));
        }

        thet = Math.atan2(source_x, drz);

        dest_lon = (thet / _toCdetConst2._xl) + _toCdetConst2._xlonz;

        dest_lon = Const.fix_longitude(dest_lon);

        dest_generic_coordinate[0] = dest_lon;
        dest_generic_coordinate[1] = dest_lat;
        dest_generic_coordinate[2] = source_z;
    }

    /*
     *----------------------------------------------------------------------------
     *
     * FUNCTION: toCdet1
     *
     *----------------------------------------------------------------------------
     */

    // this is for the case where the northern parallel is the same
    // as the southern parallel.
    private class ToCdetConst1 {
        /** DOCUMENT ME! */
        double _b1;

        /** DOCUMENT ME! */
        double _b2;

        /** DOCUMENT ME! */
        double _b3;

        /** DOCUMENT ME! */
        double _b4;

        /** DOCUMENT ME! */
        double _xlonz;

        /** DOCUMENT ME! */
        double _xl;

        /** DOCUMENT ME! */
        double _pz;

        /** DOCUMENT ME! */
        double _rz;

        /** DOCUMENT ME! */
        double _rz2;

        /** DOCUMENT ME! */
        double _ex;

        /** DOCUMENT ME! */
        double _rho_origin;

/**
         * Creates a new ToCdetConst1 object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         */
        public ToCdetConst1(OrmData e_constants, SRF_LCC_Params params) {
            double phiz;
            double pz;
            double xl;
            double rz;
            double rz2;
            double ex;
            double xl2;
            double cl2;
            double rn2;
            double latitude_origin;
            double sin_org;
            double porg;
            double pa2;
            double pa4;
            double pa6;
            double pa8;

            phiz = params.latitude1;
            _xlonz = params.origin_longitude;
            latitude_origin = params.origin_latitude;

            sin_org = Math.sin(latitude_origin);

            porg = Math.tan((Const.PI / 4) + (0.5 * latitude_origin));

            xl = Math.sin(phiz);
            ex = 1.0 / (xl + xl);
            xl2 = xl * xl;
            cl2 = 1.0 - xl2;

            pz = Math.tan((Const.PI / 4) + (.5 * phiz));

            if (e_constants.Eps != 0.0) /* do ellipsoid calculations */ {
                pa2 = (e_constants.Eps2 / 2.0) +
                    ((Math.pow(e_constants.Eps2, 2.0) * 5.0) / 24.0);

                pa2 += ((Math.pow(e_constants.Eps2, 3.0) / 12.0) +
                ((Math.pow(e_constants.Eps2, 4.0) * 13.0) / 360.0));

                pa4 = ((Math.pow(e_constants.Eps2, 2.0) * 7.0) / 48.0) +
                    ((Math.pow(e_constants.Eps2, 3.0) * 29.0) / 240.0);

                pa4 += ((Math.pow(e_constants.Eps2, 4.0) * 811.0) / 11520.0);

                pa6 = ((Math.pow(e_constants.Eps2, 3.0) * 7.0) / 120.0) +
                    ((Math.pow(e_constants.Eps2, 4.0) * 81.0) / 1120.0);

                pa8 = (Math.pow(e_constants.Eps2, 4.0) * 4279.0) / 161280.0;

                _b1 = (pa2 * 2.0) + (pa4 * 4.0) + (pa6 * 6.0) + (pa8 * 8.0);
                _b2 = (pa4 * -8.0) - (pa6 * 32.0) - (pa8 * 80.0);
                _b3 = (pa6 * 32.0) + (pa8 * 192.0);
                _b4 = pa8 * -128.0;

                pz = Math.pow((1.0 - (e_constants.Eps * xl)) / ((e_constants.Eps * xl) +
                        1.0), e_constants.EpsH) * pz;

                rn2 = e_constants.A2 / (1 - (e_constants.Eps2 * xl2));
                rz2 = (rn2 * cl2) / xl2;

                porg = Math.pow((1.0 - (e_constants.Eps * sin_org)) / ((e_constants.Eps * sin_org) +
                        1.0), e_constants.EpsH) * porg;
            } else /* do sphere calculations */
             {
                rz2 = (e_constants.A2 * cl2) / xl2;
            }

            rz = Math.sqrt(rz2);

            _pz = pz;
            _rz = rz;
            _rz2 = rz2;
            _xl = xl;
            _ex = ex;

            /* prevent divide by zero  */
            if (Math.abs(porg) <= .000000000001) {
                pz = 0.0;
            } else {
                pz = pz / porg;
            }

            _rho_origin = rz * Math.pow(pz, xl);
        }
    }

    /*
     *----------------------------------------------------------------------------
     *
     * FUNCTION: toCdet2
     *
     *----------------------------------------------------------------------------
     */

    // this is for the case where the northern parallel is different
    // from the southern parallel.
    private class ToCdetConst2 {
        /** DOCUMENT ME! */
        double _b1;

        /** DOCUMENT ME! */
        double _b2;

        /** DOCUMENT ME! */
        double _b3;

        /** DOCUMENT ME! */
        double _b4;

        /** DOCUMENT ME! */
        double _xlonz;

        /** DOCUMENT ME! */
        double _rz;

        /** DOCUMENT ME! */
        double _xl;

        /** DOCUMENT ME! */
        double _ex;

        /** DOCUMENT ME! */
        double _rz2;

        /** DOCUMENT ME! */
        double _p1;

/**
         * Creates a new ToCdetConst2 object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         */
        public ToCdetConst2(OrmData e_constants, SRF_LCC_Params params) {
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
            double rz;
            double ex;
            double c22;
            double rz2;
            double pa2;
            double pa4;
            double pa6;
            double pa8;

            phiz1 = params.latitude1;
            phiz2 = params.latitude2;
            _xlonz = params.origin_longitude;
            latitude_origin = params.origin_latitude;

            pa2 = (e_constants.Eps2 / 2.0) +
                ((Math.pow(e_constants.Eps2, 2) * 5.0) / 24.0);

            pa2 += ((Math.pow(e_constants.Eps2, 3) / 12.0) +
            ((Math.pow(e_constants.Eps2, 4) * 13.0) / 360.0));

            pa4 = ((Math.pow(e_constants.Eps2, 2) * 7.0) / 48.0) +
                ((Math.pow(e_constants.Eps2, 3) * 29.0) / 240.0);

            pa4 += ((Math.pow(e_constants.Eps2, 4) * 811.0) / 11520.0);

            pa6 = ((Math.pow(e_constants.Eps2, 3) * 7.0) / 120.0) +
                ((Math.pow(e_constants.Eps2, 4) * 81.0) / 1120.0);

            pa8 = (Math.pow(e_constants.Eps2, 4) * 4279.0) / 161280.0;

            _b1 = (pa2 * 2.0) + (pa4 * 4.0) + (pa6 * 6.0) + (pa8 * 8.0);
            _b2 = (pa4 * -8.0) - (pa6 * 32.0) - (pa8 * 80.0);
            _b3 = (pa6 * 32.0) + (pa8 * 192.0);
            _b4 = pa8 * -128.0;

            sfx = Math.sin(latitude_origin);
            s1 = Math.sin(phiz1);
            s2 = Math.sin(phiz2);
            s12 = s1 * s1;
            c12 = 1 - s12;
            s22 = s2 * s2;
            c22 = 1 - s22;

            pz = Math.tan((Const.PI / 4.0) + (.5 * latitude_origin));

            p1 = Math.tan((Const.PI / 4.0) + (.5 * phiz1));
            p2 = Math.tan((Const.PI / 4.0) + (.5 * phiz2));

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
                temp = p2 / p1;
                rn12 = e_constants.A2;
                xl = (.5 * Math.log(c12 / c22)) / Math.log(temp);
            }

            rz2 = (rn12 * c12) / (xl * xl);

            rz = Math.sqrt(rz2) * Math.pow(p1 / pz, xl);

            ex = 1 / (xl + xl);

            _rz = rz;
            _xl = xl;
            _ex = ex;
            _rz2 = rz2;
            _p1 = p1;
        }
    }
}
