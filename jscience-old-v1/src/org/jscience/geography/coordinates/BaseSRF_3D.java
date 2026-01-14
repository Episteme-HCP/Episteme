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
// SRM_OTHERS_GOES_HERE
/**
 @author David Shen
 @brief Declaration of Base SRF 3D class.
 */
package org.jscience.geography.coordinates;

import java.util.HashMap;

/**
 * The BaseSRF_3D abstract class is the base class for the 3D SRFs.
 *
 * @author David Shen
 * @see BaseSRF, BaseSRF_2D
 */
public abstract class BaseSRF_3D extends BaseSRF {
    /**
     * Creates a 3D coordinate object.
     *
     * @return a 3D coordinate object
     * @Note The initial coordinate value is defaulted to [ Double.NaN, Double.NaN, Double.NaN ].
     */
    public abstract Coord3D createCoordinate3D();

    /**
     * Creates a 3D coordinate object.
     *
     * @param coord_comp1 in: the values of the first component of the 3D coordinate
     * @param coord_comp2 in: the values of the second component of the 3D coordinate
     * @param coord_comp3 in: the values of the third component of the 3D coordinate
     * @return a 3D coordinate object
     */
    public abstract Coord3D createCoordinate3D(double coord_comp1,
                                               double coord_comp2, double coord_comp3);

    /**
     * Retrieves the 3D coordinate component values.
     *
     * @param coord in: a 3D coordinate
     * @return an array of size 3 containing the component values for the 3D coordinate
     * @note The input coordinate must have been created using this SRF.
     */
    public double[] getCoordinate3DValues(Coord3D coord)
            throws SrmException {
        if (coord == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getCoordinate3DValues: null reference input parameter"));
        }

        if (coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("getCoordinate3DValues: Coordinate associated with different SRF"));
        }

        return coord.getValues();
    }

    /**
     * Changes a coordinate's values to this SRF.
     *
     * @param src in: the source 3D coordinate in some other 3D SRF
     * @param tgt in out: the target coordinate in this 3D SRF
     * @return the Valid Region of the target coordinate
     */
    public SRM_Coordinate_Valid_Region_Code changeCoordinate3DSRF(Coord3D src,
                                                                  Coord3D tgt) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid;
        double[] tgtValues = new double[3];

        if ((src == null) || (tgt == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("changeCoordinate3DSRF: null reference input parameters"));
        }

        retValid = OpManager.instance().computeAsArray(src.getSRF(), this,
                src.getValues(), tgtValues);
        ((Coord3D) tgt).setValues(tgtValues);

        return retValid;
    }

    /**
     * Creates a direction object.
     *
     * @param ref_coord in: the 3D reference coordinate in this SRF.
     * @param vec[]     in: the array of size 3 containing the vector component values.  It can
     *                  be changed after the direction is created.
     * @return a direction object
     * @note The input reference coordinate must have been created using this SRF.
     * @note The reference coordinate cannot be changed after the direction is instantiated.
     * <p/>
     * A sample code to instantiate a Direction object is as follows:
     * @code ...
     * <p/>
     * try {
     * <p/>
     * // first instantiate a SRF, say CD SRF in WGS 1984
     * SRF_Celestiodetic CdSrf = new SRF_Celestiodetic(SRM_ORM_Code.ORM_WGS_1984,
     * SRM_HSR_Code.HSR_WGS_1984_IDENTITY);
     * <p/>
     * // then instantiate a 3D CD coordinate as the reference coordinate
     * Coord3D_Celestiodetic CdCoord =
     * (Coord3D_Celestiodetic)CdSrf.createCoordinate3D(Math.toRadians(10.0),
     * Math.toRadians(20.0),
     * 100.0);
     * <p/>
     * // then instantiate the Direction object by invoking the createDirection method
     * Direction dir = CdSrf.createDirection( CdCoord, { 1.0, 2.0, 3.0 } );
     * <p/>
     * } catch (SrmException ex)
     * ...
     * <p/>
     * // Note: The input reference coordinate is immutable.
     * @endcode
     */
    public Direction createDirection(Coord3D ref_coord, double[] vec)
            throws SrmException {
        if ((ref_coord == null) || (vec == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createDirection: null reference input parameter"));
        }

        if (ref_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("createDirection: Ref. coordinate associated with different SRF"));
        }

        if (vec.length != 3) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createDirection: Input vector is not of size 3"));
        }

        return new Direction(this, ref_coord, vec);
    }

    /**
     * Creates a direction object with reference location and vector components as Double.NaN.
     *
     * @return a direction object
     * @note This method is mainly intended for facilitating the creation of Direction objects
     * that will be used as the target Direction for the changeDirectionSRF() method.
     */
    public Direction createDirection() throws SrmException {
        return new Direction(this, this.createCoordinate3D(),
                new double[]{Double.NaN, Double.NaN, Double.NaN});
    }

    /**
     * Retrieves the direction component values.
     *
     * @param direction in: the direction object
     * @param ref_coord in/out: the 3D reference coordinate in this SRF
     * @param vec[]     in/out: the 3D reference coordinate in this SRF
     * @note The input direction must have been created using this SRF.
     * @note The input reference coordinate must have been created using this SRF.
     */
    public void getDirectionValues(Direction direction, Coord3D ref_coord,
                                   double[] vec) throws SrmException {
        if ((direction == null) || (ref_coord == null) || (vec == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getDirectionValues: null reference input parameter"));
        }

        if (direction.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("getDirectionValues: Direction associated with different SRF"));
        }

        if (ref_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("getDirectionValues: Reference Coordinate associated with different SRF"));
        }

        if (vec.length != 3) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getDirectionValues: Input vector is not of size 3"));
        }

        double[] tmpDir = direction.getVec();

        ref_coord.setValues(direction.getInternalRefCoord().getValues());

        vec[0] = tmpDir[0];
        vec[1] = tmpDir[1];
        vec[2] = tmpDir[2];
    }

    /**
     * Changes a direction's reference coordinate and vector to this SRF.
     *
     * @param src_dir in: the source direction in some other SRF
     * @param tgt_dir in/out: the target direction in this SRF
     */
    public SRM_Coordinate_Valid_Region_Code changeDirectionSRF(Direction src_dir, Direction tgt_dir) throws SrmException {
        if ((src_dir == null) || (tgt_dir == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("changeDirectionSRF: null reference input parameter"));
        }

        if (tgt_dir.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("changeDirectionSRF: Target Direction associated with different SRF"));
        }

        SRM_Coordinate_Valid_Region_Code retValidity = SRM_Coordinate_Valid_Region_Code.VALID;
        double[] dir_values_tgt = new double[3];

        retValidity = this.changeCoordinate3DSRF(src_dir.getInternalRefCoord(),
                tgt_dir.getInternalRefCoord());

        DirectionSupport.changeDirectionVector((BaseSRF_3D) src_dir.getSRF(),
                src_dir.getInternalRefCoord(), src_dir.getVec(), this,
                tgt_dir.getInternalRefCoord(), dir_values_tgt);

        tgt_dir.setVec(dir_values_tgt);

        return retValidity;
    }

    /**
     * Check a direction in this SRF.
     *
     * @param direction in: the direction in this SRF
     * @return the coordinate valid region code in the direction's SRF
     * @note The input direction object must have been created using this SRF.
     */
    public SRM_Coordinate_Valid_Region_Code checkDirection(Direction direction)
            throws SrmException {
        if (direction == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("checkDirection: null reference input parameter"));
        }

        if (direction.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("checkDirection: direction associated with different SRF"));
        }

        SRM_Coordinate_Valid_Region_Code retReg = null;

        retReg = this.checkCoordinate(direction.getInternalRefCoord());

        double[] vec = direction.getVec();

        if (((vec[0] * vec[0]) + (vec[1] * vec[1]) + (vec[2] * vec[2])) <= Const.EPSILON_SQ) {
            throw new SrmException(SrmException._INVALID_SOURCE_DIRECTION,
                    new String("checkDirection: Direction vector magnitude ~ 0.0 "));
        }

        return retReg;
    }

    /**
     * Creates an orientation object.
     *
     * @param ref_coord in: the 3D reference coordinate in this SRF
     * @param mtx[][]   in: the 3x3 matrix containing the three vector component values
     * @return an orientation object
     * @note The input reference coordinate must have been created using this SRF.
     * <p/>
     * A sample code to instantiate an Orientation object is as follows:
     * @code ...
     * <p/>
     * try {
     * <p/>
     * // first instantiate a SRF, say CD SRF in WGS 1984
     * SRF_Celestiodetic CdSrf = new SRF_Celestiodetic(SRM_ORM_Code.ORM_WGS_1984,
     * SRM_HSR_Code.HSR_WGS_1984_IDENTITY);
     * <p/>
     * // then Instantiate a 3D CD coordinate as the reference coordinate
     * Coord3D_Celestiodetic CdCoord =
     * (Coord3D_Celestiodetic)CdSrf.createCoordinate3D(Math.toRadians(10.0),
     * Math.toRadians(20.0),
     * 100.0);
     * // then allocate a 3x3 matrix
     * double[][] matrix = new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} };
     * <p/>
     * // then instantiate the Orientation object by invoking the createOrientation method
     * Orientation ori = CdSrf.createOrientation( CdCoord, matrix );
     * <p/>
     * } catch (SrmException ex)
     * ...
     * <p/>
     * // Note: The input reference coordinate is immutable.
     * @endcode
     */
    public Orientation createOrientation(Coord3D ref_coord, double[][] mtx)
            throws SrmException {
        if ((ref_coord == null) || (mtx == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createOrientation: null reference input parameter"));
        }

        if (ref_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("createOrientation: Ref. coordinate associated with different SRF"));
        }

        if ((mtx.length != 3) && (mtx[2].length != 3)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createOrientation: Input Orientation matrix is not 3x3"));
        }

        return new Orientation(this, ref_coord, mtx);
    }

    /**
     * Creates an Orientation object with a reference location and three Direction objects as the 3x3
     * Orientation matrix.
     *
     * @return an Orientation object
     * @note The input reference coordinate must have been created using this SRF.
     * @note The input Direction objects must have use the same reference location
     * as the input reference location.
     */
    public Orientation createOrientation(Coord3D ref_coord, Direction dir1,
                                         Direction dir2, Direction dir3) throws SrmException {
        if ((ref_coord == null) || (dir1 == null) || (dir2 == null) ||
                (dir3 == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createOrientation: null reference input parameter"));
        }

        if (ref_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("createOrientation: Ref. coordinate associated with different SRF"));
        }

        if ((dir1._ref_coord != ref_coord) || (dir2._ref_coord != ref_coord) ||
                (dir3._ref_coord != ref_coord)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("createOrientation: Direction reference location mismatch"));
        }

        return new Orientation(this, ref_coord,
                new double[][]{dir1._vec, dir2._vec, dir3._vec});
    }

    /**
     * Creates an Orientation object with a reference location and matrix components as Double.NaN.
     *
     * @return an Orientation object
     * @note This method is mainly intended for facilitating the creation of Orientation objects
     * that will be used as the target Orientation for the changeOrientationSRF() method.
     */
    public Orientation createOrientation() throws SrmException {
        return new Orientation(this, this.createCoordinate3D(),
                new double[][]{
                        {Double.NaN, Double.NaN, Double.NaN},
                        {Double.NaN, Double.NaN, Double.NaN},
                        {Double.NaN, Double.NaN, Double.NaN}
                });
    }

    /**
     * Retrieves the orientation component values.
     *
     * @param orientation in: the orientaiton object
     * @param ref_coord   in/out: the 3D reference coordinate in this SRF
     * @param mtx[]       in/out: the 3D reference coordinate in this SRF
     * @note The input orientation must have been created using this SRF.
     * @note The input reference coordinate must have been created using this SRF.
     */
    public void getOrientationValues(Orientation orientation,
                                     Coord3D ref_coord, double[][] mtx) throws SrmException {
        if ((orientation == null) || (ref_coord == null) || (mtx == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getOrientationValues: null reference input parameter"));
        }

        if (orientation.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("getOrientationValues: Orientation associated with different SRF"));
        }

        if (ref_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("getOrientationValues: Reference Coordinate associated with different SRF"));
        }

        if ((mtx.length != 3) && (mtx[2].length != 3)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getOrientationValues: Input Orientation matrix is not 3x3"));
        }

        double[][] tmpMtx = orientation.getMatrix();

        ref_coord.setValues(orientation.getInternalRefCoord().getValues());

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                mtx[i][j] = tmpMtx[i][j];
    }

    /**
     * Check an orientation in this SRF.
     *
     * @param orientation in: the orientation in this SRF
     * @return the coordinate valid region code in the orientation's SRF
     * @note The input orientation object must have been created using this SRF.
     */
    public SRM_Coordinate_Valid_Region_Code checkOrientation(Orientation orientation) throws SrmException {
        if (orientation == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("checkOrientation: null reference input parameter"));
        }

        if (orientation.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("checkOrientation: orientation associated with different SRF"));
        }

        SRM_Coordinate_Valid_Region_Code retReg = null;

        retReg = this.checkCoordinate(orientation.getInternalRefCoord());

        if (Const.det(orientation.getMatrix(), 3) < Const.EPSILON) {
            throw new SrmException(SrmException._INVALID_SOURCE_ORIENTATION,
                    new String("checkOrientation: orientation matrix not inversible"));
        }

        return retReg;
    }

    /**
     * Changes an orientation's values to this SRF.
     *
     * @param src_ori in: the source orientation in some other SRF
     * @return the destination orientation in this SRF
     */
    public SRM_Coordinate_Valid_Region_Code changeOrientationSRF(Orientation src_ori, Orientation tgt_ori) throws SrmException {
        if ((src_ori == null) || (tgt_ori == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("changeOrientationSRF: null reference input parameter"));
        }

        if (tgt_ori.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("changeOrientationSRF: Target Orientation associated with different SRF"));
        }

        SRM_Coordinate_Valid_Region_Code retValidity = SRM_Coordinate_Valid_Region_Code.VALID;
        double[][] dir_values_tgt = new double[3][3];
        double[][] tempOri;
        double[] tempVec = new double[3];
        double[] tempVecOut = new double[3];

        tempOri = src_ori.getMatrix();

        retValidity = this.changeCoordinate3DSRF(src_ori.getInternalRefCoord(),
                tgt_ori.getInternalRefCoord());

        for (int i = 0; i < 3; i++) {
            tempVec[0] = tempOri[i][0];
            tempVec[1] = tempOri[i][1];
            tempVec[2] = tempOri[i][2];

            DirectionSupport.changeDirectionVector((BaseSRF_3D) src_ori.getSRF(),
                    src_ori.getInternalRefCoord(), tempVec, this,
                    tgt_ori.getInternalRefCoord(), tempVecOut);

            dir_values_tgt[i][0] = tempVecOut[0];
            dir_values_tgt[i][1] = tempVecOut[1];
            dir_values_tgt[i][2] = tempVecOut[2];
        }

        if (Const.det(dir_values_tgt, 3) < Const.EPSILON) {
            throw new SrmException(SrmException._INVALID_TARGET_ORIENTATION,
                    new String("changeOrientationSRF: Invalid target orientation matrix (det ~ 0.0)"));
        }

        tgt_ori.setMatrix(dir_values_tgt);

        return retValidity;
    }

    /**
     * Instances a 3D source coordinate and orientation into this SRF.
     *
     * @param src_coord   in: the source coordinate to be instantiated in this SRF
     * @param orientation in: the orientation to be instantiated in this SRF
     * @return the coordinates in this SRF
     */
    public Coord3D instanceAbstractSpaceCoordinate(Coord3D src_coord,
                                                   Orientation orientation) throws SrmException {
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
                new String("instanceAbstractSpaceCoordinate is unimplemented"));
    }

    /**
     * Returns the euclidean distance between two coordinates.
     *
     * @param coord1 in: a coordinate in some SRF
     * @param coord2 in: a coordinate in some SRF
     * @return the Euclidean distance between the two Coord3D coordinates (in meters).
     * @note The input coordinates do not need to be from the same SRF.
     * @note This method will make (and cache) internal conversions when the inputs coordinates
     * are from SRFs other than SRF_Celestiocentric.
     */
    public static double calculateEuclideanDistance(Coord3D coord1,
                                                    Coord3D coord2) throws SrmException {
        SRM_ORM_Code orm_ref_tgt;

        SRF_Celestiocentric tempCcSrf;
        double[] tempCcSrcCoord = new double[3];
        double[] tempCcTgtCoord = new double[3];

        double delta_x;
        double delta_y;
        double delta_z;

        /*Check for null reference*/
        if ((coord1 == null) || (coord2 == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("calculateEuclideanDistance: null reference input parameter"));
        }

        /*Test to see if the source and target SRF's are both for the same body*/
        if (OrmDataSet.getElem(coord1.getSRF().get_orm())._reference_orm != OrmDataSet.getElem(coord2.getSRF().get_orm())._reference_orm) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("calculateEuclideanDistance: coordinates" +
                            " associated with different reference ORMs"));
        }

        // instantiate cache for the interim SRFs
        if (coord1.getSRF()._internalSRFs == null) {
            coord1.getSRF()._internalSRFs = new HashMap();
        }

        tempCcSrf = (SRF_Celestiocentric) coord1.getSRF()._internalSRFs.get("Interim_Cc");

        // create a Celestiocentric SRF using the common
        if (tempCcSrf == null) {
            tempCcSrf = new SRF_Celestiocentric(coord1.getSRF().get_orm(),
                    coord1.getSRF().get_hsr());
        }

        // cache the interim Celestiocentric in the (source) SRF for
        // coordinate 1.  We use the same one for coordinate 2.
        coord1.getSRF()._internalSRFs.put("Interim_Cc", tempCcSrf);

        // converting source coordinate to celestiocentric SRF
        OpManager.instance().computeAsArray(coord1.getSRF(), tempCcSrf,
                coord1.getValues(), tempCcSrcCoord);

        // 		System.out.println("tempCcSrcCoord=> " + tempCcSrcCoord[0] + ", "
        // 				   + tempCcSrcCoord[1] + ", "
        // 				   + tempCcSrcCoord[2]);
        // converting target coordinate to celestiocentric SRF
        OpManager.instance().computeAsArray(coord2.getSRF(), tempCcSrf,
                coord2.getValues(), tempCcTgtCoord);

        // 		System.out.println("tempCcTgtCoord=> " + tempCcTgtCoord[0] + ", "
        // 				   + tempCcTgtCoord[1] + ", "
        // 				   + tempCcTgtCoord[2]);
        delta_x = tempCcSrcCoord[0] - tempCcTgtCoord[0];
        delta_y = tempCcSrcCoord[1] - tempCcTgtCoord[1];
        delta_z = tempCcSrcCoord[2] - tempCcTgtCoord[2];

        return Math.sqrt(Const.square(delta_x) + Const.square(delta_y) +
                Const.square(delta_z));
    }

    /**
     * Computes the natural SRF Set member code (region) where the 3D coordinate is
     * located in the target SRF Set.
     *
     * @param src_coord in : the source 3D coordinate in an SRF
     * @param orm_dst   in : the ORM for the destination SRF Set
     * @param hsr_dst   in : the HSR for the destination SRF Set
     * @param tgt_srfs  in : the destination SRF Set Code
     * @return the SRF Set Member code for the destination SRF Set
     * @note The input coordinate must have been created using this SRF.
     */
    public int getNaturalSRFSetMemberCode(Coord3D src_coord,
                                          SRM_ORM_Code orm_dst, SRM_HSR_Code hsr_dst, SRM_SRFS_Code tgt_srfs)
            throws SrmException {
        if ((src_coord == null) || (orm_dst == null) || (hsr_dst == null) ||
                (tgt_srfs == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getNaturalSRFSetMemberCode: null reference input parameter"));
        }

        if (src_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("getNaturalSRFSetMemberCode: Coordinate associated with different SRF"));
        }

        return getMemberCode(src_coord.getValues(), orm_dst, hsr_dst, tgt_srfs);
    }

    /**
     * Returns the natural SRF Set member instance that the 3D coordinate is
     * located in the target SRF Set.
     *
     * @param src_coord in : the source 3D coordinate in an SRF
     * @param orm_dst   in : the ORM for the destination SRF Set
     * @param hsr_dst   in : the HSR for the destination SRF Set
     * @param tgt_srfs  in : the destination SRF Set Code
     * @return the SRF Set Member instance for the destination SRF Set
     * @note The input coordinate must have been created using this SRF.
     */
    public BaseSRF_3D getNaturalSRFSetMember(Coord3D src_coord,
                                             SRM_ORM_Code orm_dst, SRM_HSR_Code hsr_dst, SRM_SRFS_Code tgt_srfs)
            throws SrmException {
        if ((src_coord == null) || (orm_dst == null) || (hsr_dst == null) ||
                (tgt_srfs == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getNaturalSRFSetMemberCode: null reference input parameter"));
        }

        if (src_coord.getSRF() != this) {
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                    new String("getNaturalSRFSetMemberCode: Coordinate associated with different SRF"));
        }

        int tmpMemberCode;

        tmpMemberCode = getMemberCode(src_coord.getValues(), orm_dst, hsr_dst,
                tgt_srfs);

        return (BaseSRF_3D) CreateSRF.srfSetMember(tgt_srfs, tmpMemberCode,
                orm_dst, hsr_dst);
    }

    protected int getMemberCode(double[] src_coord, SRM_ORM_Code orm_dst,
                                SRM_HSR_Code hsr_dst, SRM_SRFS_Code tgt_srfs) throws SrmException {
        SRM_SRFT_Code myBoundaryTemplateSrf;
        BaseSRF tmpSrf;
        double[] coord_tgt = new double[3];
        int retSetMember;

        if (tgt_srfs == SRM_SRFS_Code.SRFS_UNDEFINED) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getNaturalSRFSetMemberCode: UNDEFINED SRF Set is not valid for this operation"));
        }

        // instantiate cache for the SRF if not already created
        if (this._internalSRFs == null) {
            this._internalSRFs = new HashMap();
        }

        tmpSrf = (BaseSRF) this._internalSRFs.get("TgtBoundSrfT" + orm_dst +
                tgt_srfs);

        // create an interim boundary SRF using SRF's local cache
        if (tmpSrf == null) {
            myBoundaryTemplateSrf = CoordCheck.getsrfsBoundaryDefTemplate(tgt_srfs);

            tmpSrf = CreateSRF.fromCode(myBoundaryTemplateSrf, orm_dst, hsr_dst);

            // cache the created interim SRF
            this._internalSRFs.put("NatRegTgtBoundSrfT" + orm_dst + tgt_srfs,
                    tmpSrf);
        }

        try {
            // convert src coord to the interin Celestiodetic (in this case) coord.
            OpManager.instance().computeAsArray(this, tmpSrf, src_coord,
                    coord_tgt);
        } catch (SrmException ex) {
            throw new SrmException(SrmException._INVALID_SOURCE_SRF,
                    new String("No natural set member code for this coordinate"));
        }

        if (tgt_srfs == SRM_SRFS_Code.SRFS_ALABAMA_SPCS) {
            retSetMember = NaturalSetMember.forALSP(coord_tgt);
        } else if (tgt_srfs == SRM_SRFS_Code.SRFS_LAMBERT_NTF) {
            retSetMember = NaturalSetMember.forLAMBERT_NTF(coord_tgt);
        } else if (tgt_srfs == SRM_SRFS_Code.SRFS_GTRS_GLOBAL_COORDINATE_SYSTEM) {
            retSetMember = NaturalSetMember.forGTRS(coord_tgt);
        } else if (tgt_srfs == SRM_SRFS_Code.SRFS_JAPAN_RECTANGULAR_PLANE_COORDINATE_SYSTEM) {
            retSetMember = NaturalSetMember.forJPRP(coord_tgt);
        } else if (tgt_srfs == SRM_SRFS_Code.SRFS_UNIVERSAL_POLAR_STEREOGRAPHIC) {
            retSetMember = NaturalSetMember.forUPS(coord_tgt);
        } else if (tgt_srfs == SRM_SRFS_Code.SRFS_UNIVERSAL_TRANSVERSE_MERCATOR) {
            retSetMember = NaturalSetMember.forUTM(coord_tgt);
        } else if (tgt_srfs == SRM_SRFS_Code.SRFS_WISCONSIN_SPCS) {
            retSetMember = NaturalSetMember.forWISP(coord_tgt);
        } else {
            throw new SrmException(SrmException._INACTIONABLE,
                    new String("getNaturalSRFSetMemberCode: Inactionable error"));
        }

        return retSetMember;
    }
}
