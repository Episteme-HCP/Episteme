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
 * A CoordSurf for SRF_TransverseMercator.
 *
 * @author David Shen
 *
 * @see SRF_TransverseMercator
 */
public class CoordSurf_TransverseMercator extends CoordSurf {
    /// Constructor with initial value defaulted to [ Double.NaN, Double.NaN ]
    /**
     * Creates a new CoordSurf_TransverseMercator object.
     *
     * @param srf DOCUMENT ME!
     */
    public CoordSurf_TransverseMercator(SRF_TransverseMercator srf) {
        super(srf, Coord_ClassType.TM);
        setValues(Double.NaN, Double.NaN);
    }

    /// Constructor with input values
    /**
     * Creates a new CoordSurf_TransverseMercator object.
     *
     * @param srf DOCUMENT ME!
     * @param easting DOCUMENT ME!
     * @param northing DOCUMENT ME!
     */
    public CoordSurf_TransverseMercator(SRF_TransverseMercator srf,
        double easting, double northing) {
        super(srf, Coord_ClassType.TM);
        setValues(easting, northing);
    }

    /// Sets all coordinate component values
    /**
     * DOCUMENT ME!
     *
     * @param easting DOCUMENT ME!
     * @param northing DOCUMENT ME!
     */
    public void setValues(double easting, double northing) {
        _values[0] = easting;
        _values[1] = northing;
    }

    /// Returns the easting value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_easting() {
        return _values[0];
    }

    /// Returns the northing value
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double get_northing() {
        return _values[1];
    }

    /// Sets the easting value
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void set_easting(double value) {
        _values[0] = value;
    }

    /// Sets the northing value
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void set_northing(double value) {
        _values[1] = value;
    }

    /* Returns TRUE if the coordinate component values are "Equal"
       @note Two components of type "double" are equal
             if their difference is within epsilon=0.000001
    */
    public boolean isEqual(Coord coord) {
        if (coord == null) {
            return false;
        }

        double[] tmp = coord.getValues();

        return ((coord instanceof CoordSurf_TransverseMercator) &&
        Const.isEqual(this._values[0], tmp[0]) &&
        Const.isEqual(this._values[1], tmp[1]));
    }

    /// Returns a String of coordinate component values
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new String("[ " + _values[0] + ", " + _values[1] + " ]");
    }
}
