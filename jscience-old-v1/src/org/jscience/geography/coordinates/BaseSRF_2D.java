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
 @brief Declaration of Base SRF 2D class.
 */
package org.jscience.geography.coordinates;

/**
 * The BaseSRF_2D abstract class is the base class for the 2D SRFs.
 *
 * @author David Shen
 * @see BaseSRF, BaseSRF_3D
 */
public abstract class BaseSRF_2D extends BaseSRF {
    /**
     * Creates a 2D coordinate object.
     *
     * @return a 2D coordinate object
     * @Note The initial coordinate value is defaulted to [ Double.NaN, Double.NaN ].
     */
    public abstract Coord2D createCoordinate2D();

    /**
     * Creates a 2D coordinate object.
     *
     * @param coord_comp1 in: the values of the first component of the 2D coordinate
     * @param coord_comp2 in: the values of the second component of the 2D coordinate
     * @return a 2D coordinate object
     */
    public abstract Coord2D createCoordinate2D(double coord_comp1,
                                               double coord_comp2);

    /**
     * Retrieves the 2D coordinate component values.
     *
     * @param coord in: a 2D coordinate
     * @return an array of size 2 containing the component values for the 2D coordinate
     * @note The input coordinate must have been created using this SRF.
     */
    public double[] getCoordinate2DValues(Coord2D coord)
            throws SrmException {
        if (coord == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("getCoordinate2DValues: null reference input parameter"));
        }

        return coord.getValues();
    }

    /**
     * Changes a coordinate's values to this SRF.
     *
     * @param src in: the source coordinate in some other 2D SRF
     * @param tgt in out: the target coordinate in this 2D SRF
     * @return the Valid Region of the target coordinate
     */
    public SRM_Coordinate_Valid_Region_Code changeCoordinate2DSRF(Coord2D src,
                                                                  Coord2D tgt) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid;

        if ((src == null) || (tgt == null)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("changeCoordinate3DSRF: null reference input parameters"));
        }

        double[] srcValues = {src.getValues()[0], src.getValues()[1], 0.0};
        double[] tgtValues = new double[3];

        retValid = OpManager.instance().computeAsArray(src.getSRF(), this,
                srcValues, tgtValues);
        ((Coord2D) tgt).setValues(tgtValues);

        return retValid;
    }

    /**
     * Returns the euclidean distance between two coordinates.
     *
     * @param coord1 in: a coordinate in some SRF
     * @param coord2 in: a coordinate in some SRF
     * @return the Euclidean distance between the two Coord2D coordinates (in meters).
     * @note The input coordinates do not need to be from the same SRF.
     * @note This method will make (and cache) internal conversions when the inputs coordinates
     * are from SRFs other than SRF_Celestiocentric.
     */
    public static double calculateEuclideanDistance(Coord2D coord1,
                                                    Coord2D coord2) throws SrmException {
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
                new String("calculateEuclideanDistance: Not implemented for 2D coordinates" +
                        " in this release"));
    }
}
