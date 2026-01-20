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
// SRM_OTHERS_GOES_HERE
/**
 @author David Shen
 @brief Declaration of Orientation.
 */
package org.jscience.geography.coordinates;

/**
 * The Orientation abstract contains a 3x3 matrix and a reference location.
 *
 * @author David Shen
 * @see BaseSRF_3D
 */
public class Orientation implements Cloneable {
    /// The SRF this Orientation belongs to
    private BaseSRF _srf;

    /// The reference coordinate
    private Coord3D _ref_coord;

    /// The orientation values
    private double[][] _mtx = new double[3][3];

    /**
     * Constructor, requires an SRF, 3 reference coordinate values, and
     * 3 direction values.
     */
    protected Orientation(BaseSRF srf, Coord3D ref_coord, double[][] mtx) {
        _srf = srf;
        _ref_coord = ref_coord;
        _mtx = mtx;
    }

    /// Returns the SRF this Orientation belongs to.
    public BaseSRF getSRF() {
        return _srf;
    }

    /// Returns the reference coordinate values
    public Coord3D getRefCoord() throws SrmException {
        try {
            return (Coord3D) _ref_coord.makeClone();
        } catch (SrmException ex) {
            throw new SrmException(SrmException._INACTIONABLE,
                    new String("Orientation.getRefCoord: Unable to return reference location"));
        }
    }

    /// Returns the 3x3 orientation matrix
    public double[][] getMatrix() {
        return _mtx;
    }

    /**
     * Returns the 1st row of the Orientation matrix 3x3 in the form
     * of a Direction object.
     *
     * @note The Direction argument must be associated with the same reference coordinate
     */
    public void getDirectionComp1(Direction dir) throws SrmException {
        if (dir == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.getDirectionComp1: Input Direction reference is null"));
        }

        if (this._ref_coord != dir._ref_coord) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.getDirectionComp1: Direction argument associated with a different reference coordinate"));
        }

        dir._vec[0] = this._mtx[0][0];
        dir._vec[1] = this._mtx[0][1];
        dir._vec[2] = this._mtx[0][2];
    }

    /**
     * Returns the 2nd row of the Orientation matrix 3x3 in the form
     * of a Direction object.
     *
     * @note The Direction argument must be associated with the same reference coordinate
     */
    public void getDirectionComp2(Direction dir) throws SrmException {
        if (dir == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.getDirectionComp2: Input Direction reference is null"));
        }

        if (this._ref_coord != dir._ref_coord) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.getDirectionComp2: Direction argument associated with a different reference coordinate"));
        }

        dir._vec[0] = this._mtx[1][0];
        dir._vec[1] = this._mtx[1][1];
        dir._vec[2] = this._mtx[1][2];
    }

    /**
     * Returns the 3rd row of the Orientation matrix 3x3 in the form
     * of a Direction object.
     *
     * @note The Direction argument must be associated with the same reference coordinate
     */
    public void getDirectionComp3(Direction dir) throws SrmException {
        if (dir == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.getDirectionComp3: Input Direction reference is null"));
        }

        if (this._ref_coord != dir._ref_coord) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.getDirectionComp3: Direction argument associated with a different reference coordinate"));
        }

        dir._vec[0] = this._mtx[2][0];
        dir._vec[1] = this._mtx[2][1];
        dir._vec[2] = this._mtx[2][2];
    }

    /**
     * Returns the Nth row of the Orientation matrix 3x3 in the form of a Direction object.
     *
     * @note N within [ 1, 2, 3 ]
     * @note The Direction argument must be associated with the same reference coordinate
     */
    public void getDirectionComp(int n, Direction dir)
            throws SrmException {
        if ((n < 1) || (n > 3)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.getDirectionComp: Direction component index out of bound"));
        }

        if (dir == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.getDirectionComp: Input Direction reference is null"));
        }

        if (this._ref_coord != dir._ref_coord) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.getDirectionComp: Direction argument associated with a different reference coordinate"));
        }

        dir._vec[0] = this._mtx[n - 1][0];
        dir._vec[1] = this._mtx[n - 1][1];
        dir._vec[2] = this._mtx[n - 1][2];
    }

    /**
     * Sets rows of the Orientation matrix 3x3 using the vectors from 3 Direction objects.
     *
     * @note The Direction arguments must be associated with the same reference coordinate
     */
    public void setDirectionComp(Direction dir1, Direction dir2, Direction dir3)
            throws SrmException {
        if ((dir1 == null) || (dir2 == null) || (dir3 == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.setDirectionComp: Input Direction reference is null"));
        }

        if ((this._ref_coord != dir1._ref_coord) ||
                (this._ref_coord != dir2._ref_coord) ||
                (this._ref_coord != dir3._ref_coord)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.setDirectionComp: Direction arguments associated with a different reference coordinate"));
        }

        _mtx[0][0] = dir1._vec[0];
        _mtx[0][1] = dir1._vec[1];
        _mtx[0][2] = dir1._vec[2];
        _mtx[1][0] = dir2._vec[0];
        _mtx[1][1] = dir2._vec[1];
        _mtx[1][2] = dir2._vec[2];
        _mtx[2][0] = dir3._vec[0];
        _mtx[2][1] = dir3._vec[1];
        _mtx[2][2] = dir3._vec[2];
    }

    /**
     * Sets Nth rows of the Orientation matrix 3x3 using the vector from the Direction object.
     *
     * @note The Direction argument must be associated with the same reference coordinate.
     * @note N within [ 1, 2, 3 ]
     */
    public void setDirectionComp(int n, Direction dir)
            throws SrmException {
        if ((n < 1) || (n > 3)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.setDirectionComp: Direction component index out of bound"));
        }

        if (dir == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.setDirectionComp: Input Direction reference is null"));
        }

        if (this._ref_coord != dir._ref_coord) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.setDirectionComp: Direction argument associated with a different reference coordinate"));
        }

        _mtx[n - 1][0] = dir._vec[0];
        _mtx[n - 1][1] = dir._vec[1];
        _mtx[n - 1][2] = dir._vec[2];
    }

    /// Sets the 3x3 orientation matrix
    public void setMatrix(double[][] mtx) throws SrmException {
        try {
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    _mtx[i][j] = mtx[i][j];
        } catch (RuntimeException r) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.setMtx: Input matrix size different than 3x3"));
        }
    }

    /**
     * Returns the shallow copy of this object instance.
     *
     * @note The cloned object will reference the same object as the
     * original object.
     */
    public Orientation makeClone() throws SrmException {
        try {
            return (Orientation) super.clone();
        } catch (java.lang.CloneNotSupportedException ex) {
            throw new SrmException(SrmException._INACTIONABLE,
                    new String("Direction.makeClone(): failed"));
        }
    }

    /**
     * Copies the matrix component values of this orientation to the input orientation.
     *
     * @note The reference coordinate of this and input orientation must be the same.
     */
    public void copyTo(Orientation ori) throws SrmException {
        if (ori == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("Orientation.copyTo: Input orientation reference is null"));
        }

        // don't need to check the srf because the ref. coordinate implies the same SRF.
        if (this._ref_coord != ori._ref_coord) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("Orientation.copyTo: Input orientation not referring to the" +
                            " same reference coordinate"));
        }

        ori._mtx[0][0] = this._mtx[0][0];
        ori._mtx[0][1] = this._mtx[0][1];
        ori._mtx[0][2] = this._mtx[0][2];
        ori._mtx[1][0] = this._mtx[1][0];
        ori._mtx[1][1] = this._mtx[1][1];
        ori._mtx[1][2] = this._mtx[1][2];
        ori._mtx[2][0] = this._mtx[2][0];
        ori._mtx[2][1] = this._mtx[2][1];
        ori._mtx[2][2] = this._mtx[2][2];
    }

    /**
     * Returns TRUE if the Orientaqtion parameters are "Equal"
     *
     * @note Two variables of type "double" are equal
     * if their difference is within epsilon=0.000001
     */
    public boolean isEqual(Orientation ori) {
        return ((ori != null) && (this._srf == ori._srf) &&
                (this._ref_coord == ori._ref_coord) &&
                Const.isEqual(this._mtx[0][0], ori._mtx[0][0]) &&
                Const.isEqual(this._mtx[0][1], ori._mtx[0][1]) &&
                Const.isEqual(this._mtx[0][2], ori._mtx[0][2]) &&
                Const.isEqual(this._mtx[1][0], ori._mtx[1][0]) &&
                Const.isEqual(this._mtx[1][1], ori._mtx[1][1]) &&
                Const.isEqual(this._mtx[1][2], ori._mtx[1][2]) &&
                Const.isEqual(this._mtx[2][0], ori._mtx[2][0]) &&
                Const.isEqual(this._mtx[2][1], ori._mtx[2][1]) &&
                Const.isEqual(this._mtx[2][2], ori._mtx[2][2]));
    }

    /// Returns the values of the 3x3 matrix as "[ val11, val12, .., val32, val33 ]"
    public String toString() {
        return ("[ " + _mtx[0][0] + ", " + _mtx[0][1] + ", " + _mtx[0][2] +
                "\n  " + _mtx[1][0] + ", " + _mtx[1][1] + ", " + _mtx[1][2] + "\n  " +
                _mtx[2][0] + ", " + _mtx[2][1] + ", " + _mtx[2][2] + " ]");
    }

    /// Returns the actual reference coordinate object associated with the Direction
    protected Coord3D getInternalRefCoord() {
        return _ref_coord;
    }
}
