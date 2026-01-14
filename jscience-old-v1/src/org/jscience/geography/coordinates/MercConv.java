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
class MercConv extends Conversions {
    /** DOCUMENT ME! */
    private ToCdetConst _toCdetConst;

/**
     * Creates a new MercConv object.
     */
    public MercConv() {
        super(SRM_SRFT_Code.SRFT_MERCATOR,
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
        return (Conversions) new MercConv();
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
            src[0] -= ((SRF_Mercator) srcSrf).get_false_easting();
            src[1] -= ((SRF_Mercator) srcSrf).get_false_northing();
            toCdet(srcSrf, destSrf, src, dest);

            // perform post validation
            retValid = CoordCheck.forCelestiodetic(this.getOrmData(), dest);
        } else if (destSrfType == SRM_SRFT_Code.SRFT_UNDEFINED) {
            // just pass the coordinate through.  This is the last convesion in the chain
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
        OrmData e_constants = this.getOrmData();

        if (_toCdetConst == null) {
            _toCdetConst = new ToCdetConst(e_constants,
                    ((SRF_Mercator) srcSrf).getSRFParameters());
        }

        double dest_lat;
        double dest_lon;
        double source_x;
        double source_y;
        double source_z;
        double tanz2;
        double z;

        source_x = source_generic_coordinate[0] * _toCdetConst.scale_inv;
        source_y = source_generic_coordinate[1] * _toCdetConst.scale_inv;
        source_z = source_generic_coordinate[2];

        /* COMPUTE LONGITUDE */
        dest_lon = (source_x * _toCdetConst.Cos_Rn_Origin_Lat_Inv) +
            _toCdetConst.longitude_origin;

        tanz2 = Math.exp(-source_y * _toCdetConst.Cos_Rn_Origin_Lat_Inv);
        z = 2.0 * Math.atan(tanz2);

        if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
            double cki;
            double den_inv;
            double sk2;
            double ski;
            double t2;
            double xki;

            /* Compute Latitude */
            /*
             * Use identity to get SIN and COS of xki without trig
             * functions
             */
            t2 = tanz2 * tanz2;
            den_inv = 1.0 / (1.0 + t2);
            cki = 2.0 * tanz2 * den_inv;
            ski = (1.0 - t2) * den_inv;

            /* Use inverse power series for latitude */
            sk2 = ski * ski;

            xki = Const.PI_DIV_2 - z;

            dest_lat = xki +
                (ski * cki * (_toCdetConst.b1 +
                (sk2 * (_toCdetConst.b2 +
                (sk2 * (_toCdetConst.b3 + (sk2 * _toCdetConst.b4)))))));
        } else { /* compute spherical values */
            dest_lat = Const.PI_DIV_2 - z;
        }

        dest_lon = Const.fix_longitude(dest_lon);

        dest_generic_coordinate[0] = dest_lon;
        dest_generic_coordinate[1] = dest_lat;
        dest_generic_coordinate[2] = source_z;
    }

    /*
     *----------------------------------------------------------------------------
     *
     * To Cdet functions
     *
     *----------------------------------------------------------------------------
     */
    private class ToCdetConst {
        /** DOCUMENT ME! */
        double b1;

        /** DOCUMENT ME! */
        double b2;

        /** DOCUMENT ME! */
        double b3;

        /** DOCUMENT ME! */
        double b4;

        /** DOCUMENT ME! */
        double longitude_origin;

        /** DOCUMENT ME! */
        double geodetic_latitude;

        /** DOCUMENT ME! */
        double scale_inv;

        /** DOCUMENT ME! */
        double Cos_Rn_Origin_Lat_Inv;

/**
         * Creates a new ToCdetConst object.
         *
         * @param e_constants DOCUMENT ME!
         * @param params      DOCUMENT ME!
         */
        public ToCdetConst(OrmData e_constants, SRF_Mercator_Params params) {
            /*$$$ The CD compliant version shows a latitude origin
             *parameter.  This is not present in the implementation carried
             *forward from 3.1.2  CDK 8/15/03
             */
            double pa2;

            /*$$$ The CD compliant version shows a latitude origin
             *parameter.  This is not present in the implementation carried
             *forward from 3.1.2  CDK 8/15/03
             */
            double pa4;

            /*$$$ The CD compliant version shows a latitude origin
             *parameter.  This is not present in the implementation carried
             *forward from 3.1.2  CDK 8/15/03
             */
            double pa6;

            /*$$$ The CD compliant version shows a latitude origin
             *parameter.  This is not present in the implementation carried
             *forward from 3.1.2  CDK 8/15/03
             */
            double pa8;

            longitude_origin = params.origin_longitude;
            geodetic_latitude = params.origin_latitude;
            scale_inv = 1.0 / params.central_scale;

            /*
             * NOTE: There are no spherical constants, so we save a second by
             *       not computing the ellipsoid constants needlessly. -- dan
             */
            if (e_constants.Eps != 0.0) /* compute ellipsoidal values */ {
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

                b1 = (pa2 * 2.0) + (pa4 * 4.0) + (pa6 * 6.0) + (pa8 * 8.0);
                b2 = (pa4 * -8.0) - (pa6 * 32.0) - (pa8 * 80.0);
                b3 = (pa6 * 32.0) + (pa8 * 192.0);
                b4 = pa8 * -128.0;
                Cos_Rn_Origin_Lat_Inv = 1.0 / (Math.cos(geodetic_latitude) * Const.computeRn(geodetic_latitude,
                        e_constants));
            }
        }
    }
}
