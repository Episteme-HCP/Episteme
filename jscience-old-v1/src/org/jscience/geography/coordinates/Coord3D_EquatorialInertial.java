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

// SRM SDK Release 4.0.0 - July 29, 2004
// - SRM spec. 4.0
/*
 *                             NOTICE
 *
 * This software is provided openly and freely for use in representing and
 * interchanging environmental data & databases.
 *
 * This software was developed for use by the United States Government with
 * unlimited rights.  The software was developed under contract
 * N61339-03-C-0068 by Science Applications International Corporation.
 * The software is unclassified and is deemed as Distribution A, approved
 * for Public Release.
 *
 * Use by others is permitted only upon the ACCEPTANCE OF THE TERMS AND
 * CONDITIONS, AS STIPULATED UNDER THE FOLLOWING PROVISIONS:
 *
 *    1. Recipient may make unlimited copies of this software and give
 *       copies to other persons or entities as long as the copies contain
 *       this NOTICE, and as long as the same copyright notices that
 *       appear on, or in, this software remain.
 *
 *    2. Trademarks. All trademarks belong to their respective trademark
 *       holders.  Third-Party applications/software/information are
 *       copyrighted by their respective owners.
 *
 *    3. Recipient agrees to forfeit all intellectual property and
 *       ownership rights for any version created from the modification
 *       or adaptation of this software, including versions created from
 *       the translation and/or reverse engineering of the software design.
 *
 *    4. Transfer.  Recipient may not sell, rent, lease, or sublicense
 *       this software.  Recipient may, however enable another person
 *       or entity the rights to use this software, provided that this
 *       AGREEMENT and NOTICE is furnished along with the software and
 *       /or software system utilizing this software.
 *
 *       All revisions, modifications, created by the Recipient, to this
 *       software and/or related technical data shall be forwarded by the
 *       Recipient to the Government at the following address:
 *
 *         NAVAIR Orlando TSD PJM
 *         Attention SEDRIS II TPOC
 *         12350 Research Parkway
 *         Orlando, FL 32826-3275
 *
 *         or via electronic mail to:  se-mgmt@sedris.org
 *
 *    5. No Warranty. This software is being delivered to you AS IS
 *       and there is no warranty, EXPRESS or IMPLIED, as to its use
 *       or performance.
 *
 *       The RECIPIENT ASSUMES ALL RISKS, KNOWN AND UNKNOWN, OF USING
 *       THIS SOFTWARE.  The DEVELOPER EXPRESSLY DISCLAIMS, and the
 *       RECIPIENT WAIVES, ANY and ALL PERFORMANCE OR RESULTS YOU MAY
 *       OBTAIN BY USING THIS SOFTWARE OR DOCUMENTATION.  THERE IS
 *       NO WARRANTY, EXPRESS OR, IMPLIED, AS TO NON-INFRINGEMENT OF
 *       THIRD PARTY RIGHTS, MERCHANTABILITY, OR FITNESS FOR ANY
 *       PARTICULAR PURPOSE.  IN NO EVENT WILL THE DEVELOPER, THE
 *       UNITED STATES GOVERNMENT OR ANYONE ELSE ASSOCIATED WITH THE
 *       DEVELOPMENT OF THIS SOFTWARE BE HELD LIABLE FOR ANY CONSEQUENTIAL,
 *       INCIDENTAL OR SPECIAL DAMAGES, INCLUDING ANY LOST PROFITS
 *       OR LOST SAVINGS WHATSOEVER.
 */

/*
 * COPYRIGHT 2003, SCIENCE APPLICATIONS INTERNATIONAL CORPORATION.
 *                 ALL RIGHTS RESERVED.
 *
 */
/**
 */
package org.jscience.geography.coordinates;

/**
 * A Coord3D for SRF_EquatorialInertial.
 *
 * @author David Shen
 *
 * @see SRF_EquatorialInertial
 */
public class Coord3D_EquatorialInertial extends Coord3D {
    /// Constructor with initial value defaulted to [ Double.NaN, Double.NaN, Double.NaN ]
    /**
     * Creates a new Coord3D_EquatorialInertial object.
     *
     * @param srf DOCUMENT ME!
     */
    public Coord3D_EquatorialInertial(SRF_EquatorialInertial srf) {
        super(srf, Coord_ClassType.EI);
        setValues(Double.NaN, Double.NaN, Double.NaN);
    }

    /// Constructor with input values
    /**
     * Creates a new Coord3D_EquatorialInertial object.
     *
     * @param srf DOCUMENT ME!
     * @param right_ascension DOCUMENT ME!
     * @param declination DOCUMENT ME!
     * @param radius DOCUMENT ME!
     */
    public Coord3D_EquatorialInertial(SRF_EquatorialInertial srf,
        double right_ascension, double declination, double radius) {
        super(srf, Coord_ClassType.EI);
        setValues(right_ascension, declination, radius);
    }

    /// Sets all coordinate component values
    /**
     * DOCUMENT ME!
     *
     * @param right_ascension DOCUMENT ME!
     * @param declination DOCUMENT ME!
     * @param radius DOCUMENT ME!
     */
    public void setValues(double right_ascension, double declination,
        double radius) {
        _values[0] = right_ascension;
        _values[1] = declination;
        _values[2] = radius;
    }

    /// Returns the right_ascension value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_right_ascension() {
        return _values[0];
    }

    /// Returns the declination value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_declination() {
        return _values[1];
    }

    /// Returns the radius value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_radius() {
        return _values[2];
    }

    /// Sets the right_ascension value
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void set_right_ascension(double value) {
        _values[0] = value;
    }

    /// Sets the declination value
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void set_declination(double value) {
        _values[1] = value;
    }

    /// Sets the radius value
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void set_radius(double value) {
        _values[2] = value;
    }

    /* Returns TRUE if the coordinate components are "Equal"
       @note Two component values of type "double" are equal
             if their difference is within epsilon=0.000001
    */
    public boolean isEqual(Coord coord) {
        if (coord == null) {
            return false;
        }

        double[] tmp = coord.getValues();

        return ((coord instanceof Coord3D_EquatorialInertial) &&
        Const.isEqual(this._values[0], tmp[0]) &&
        Const.isEqual(this._values[1], tmp[1]) &&
        Const.isEqual(this._values[2], tmp[2]));
    }

    /// Returns a String of coordinate component values
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new String("[ " + _values[0] + ", " + _values[1] + ", " +
            _values[2] + " ]");
    }
}
