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
class HsrDataSet {
    /** DOCUMENT ME! */
    private static boolean _subClassesNotInstantiated = true;

    /** DOCUMENT ME! */
    public String _label;

    /** DOCUMENT ME! */
    public String _description;

    /** DOCUMENT ME! */
    public SRM_ORM_Code _orm_code;

    /** DOCUMENT ME! */
    public SRM_HSR_Code _hsr_code;

    /** DOCUMENT ME! */
    public double _delta_x;

    /** DOCUMENT ME! */
    public double _delta_y;

    /** DOCUMENT ME! */
    public double _delta_z;

    /** DOCUMENT ME! */
    public double _omega_1;

    /** DOCUMENT ME! */
    public double _omega_2;

    /** DOCUMENT ME! */
    public double _omega_3;

    /** DOCUMENT ME! */
    public double _delta_scale;

    /** DOCUMENT ME! */
    public double _region_ll_lat;

    /** DOCUMENT ME! */
    public double _region_ll_long;

    /** DOCUMENT ME! */
    public double _region_ur_lat;

    /** DOCUMENT ME! */
    public double _region_ur_long;

/**
     * Creates a new HsrDataSet object.
     *
     * @param label          DOCUMENT ME!
     * @param description    DOCUMENT ME!
     * @param orm_code       DOCUMENT ME!
     * @param hsr_code       DOCUMENT ME!
     * @param delta_x        DOCUMENT ME!
     * @param delta_y        DOCUMENT ME!
     * @param delta_z        DOCUMENT ME!
     * @param omega_1        DOCUMENT ME!
     * @param omega_2        DOCUMENT ME!
     * @param omega_3        DOCUMENT ME!
     * @param delta_scale    DOCUMENT ME!
     * @param region_ll_lat  DOCUMENT ME!
     * @param region_ll_long DOCUMENT ME!
     * @param region_ur_lat  DOCUMENT ME!
     * @param region_ur_long DOCUMENT ME!
     */
    protected HsrDataSet(String label, String description,
        SRM_ORM_Code orm_code, SRM_HSR_Code hsr_code, double delta_x,
        double delta_y, double delta_z, double omega_1, double omega_2,
        double omega_3, double delta_scale, double region_ll_lat,
        double region_ll_long, double region_ur_lat, double region_ur_long) {
        _label = label;
        _description = description;
        _orm_code = orm_code;
        _hsr_code = hsr_code;
        _delta_x = delta_x;
        _delta_y = delta_y;
        _delta_z = delta_z;
        _omega_1 = omega_1;
        _omega_2 = omega_2;
        _omega_3 = omega_3;
        _delta_scale = delta_scale;
        _region_ll_lat = region_ll_lat;
        _region_ll_long = region_ll_long;
        _region_ur_lat = region_ur_lat;
        _region_ur_long = region_ur_long;
    }

    /**
     * DOCUMENT ME!
     *
     * @param orm_code DOCUMENT ME!
     * @param hsr_code DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    public static HsrDataSet getElem(SRM_ORM_Code orm_code,
        SRM_HSR_Code hsr_code) throws SrmException {
        HsrDataSet retHsrData = null;

        // this statement forces the ellaboration of HsrDataSet1 and HsrDataSet2 classes.
        // Can't think of any better way to do this at this moment.
        if (_subClassesNotInstantiated) {
            instantiateSubClasses();
        }

        try {
            retHsrData = (HsrDataSet) OrmDataSet.getElem(orm_code)._hsr_vector.elementAt(hsr_code.toInt() -
                    1);

            // this is the case where an HSR data was found but not the right one
            if (retHsrData._hsr_code != hsr_code) {
                throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_HSR_Code " + hsr_code +
                        " isn't valid for SRM_ORM_Code " + orm_code));
            }

            // this is the case where the HSR data was not found ( out of vector bounds )
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new SrmException(SrmException._INVALID_INPUT,
                new String("SRM_HSR_Code " + hsr_code +
                    " isn't valid for SRM_ORM_Code " + orm_code));
        }

        return retHsrData;
    }

    /**
     * DOCUMENT ME!
     */
    private static void instantiateSubClasses() {
        boolean dummy = ((HsrDataSet) HsrDataSet1.table[1] == (HsrDataSet) HsrDataSet2.table[1]);
        _subClassesNotInstantiated = false;
    }
}
