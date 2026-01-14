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
 @brief class that computes the Natural SRF Set Member code.
 */
package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
class NaturalSetMember {
    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static int forUPS(double[] coord) throws SrmException {
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
            new String("Natural Set Member Code for UPS not implemented"));
    }

    // Be careful!! This routine assume input in radians
    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static int forUTM(double[] coord) throws SrmException {
        int zone;
        double[] my_coord = { Math.toDegrees(coord[0]), Math.toDegrees(coord[1]) };

        if ((my_coord[1] >= 84.5) || (my_coord[1] <= -80.5)) {
            /*Nether Region*/
            throw new SrmException(SrmException._INVALID_SOURCE_COORDINATE,
                new String("Source coordinate not defined within UTM zones"));
        } else {
            // get UTM zone from 1 to 60 in the classical UTM definition
            zone = (int) Math.floor(31.0 +
                    (my_coord[0] * 0.16666666666666666666666666666666666666666666666666));

            if (my_coord[0] < -180.0) {
                zone = 60;
            } else if (my_coord[0] > 180.0) {
                zone = 1;
            }

            // add 60 to make it southern hemisphere in SRM UTM definition.
            if (my_coord[1] < 0.0) {
                zone += 60;
            }
        }

        return zone;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static int forGTRS(double[] coord) throws SrmException {
        return GtrsDataSet.getNaturalCell(coord);
    }

    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static int forALSP(double[] coord) throws SrmException {
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
            new String("Natural Set Member Code for ALABAMA SPCS not implemented"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static int forLAMBERT_NTF(double[] coord)
        throws SrmException {
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
            new String("Natural Set Member Code for LAMBERT NTF not implemented"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static int forJPRP(double[] coord) throws SrmException {
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
            new String("Natural Set Member Code for JAPAN RECTANGULAR PLACE CS not implemented"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    protected static int forWISP(double[] coord) throws SrmException {
        throw new SrmException(SrmException._NOT_IMPLEMENTED,
            new String("Natural Set Member Code for WISCONSIN SPCS not implemented"));
    }
}
