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
 * SRF_LocalSpacePolar class declaration.
 *
 * @author David Shen
 *
 * @see BaseSRF_2D
 */
public class SRF_LocalSpacePolar extends BaseSRF_2D {
    /// SRF_LocalSpacePolar only constructor by ORM code and Hsr code
    /**
     * Creates a new SRF_LocalSpacePolar object.
     *
     * @param orm DOCUMENT ME!
     * @param hsr DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    public SRF_LocalSpacePolar(SRM_ORM_Code orm, SRM_HSR_Code hsr)
        throws SrmException {
        _mySrftCode = SRM_SRFT_Code.SRFT_LOCAL_SPACE_POLAR;
        _orm = orm;
        _hsr = hsr;

        SrfCheck.forLocalSpacePolar(_orm, _hsr);
    }

    /// Create specific 2D coordinate for SRF_LocalSpacePolar with [ Double.NaN, Double.NaN ]
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord2D createCoordinate2D() {
        return new Coord2D_LocalSpacePolar((SRF_LocalSpacePolar) this,
            Double.NaN, Double.NaN);
    }

    /// Create specific 2D coordinate for SRF_LocalSpacePolar with input values
    /**
     * DOCUMENT ME!
     *
     * @param coord_comp1 DOCUMENT ME!
     * @param coord_comp2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord2D createCoordinate2D(double coord_comp1, double coord_comp2) {
        return new Coord2D_LocalSpacePolar((SRF_LocalSpacePolar) this,
            coord_comp1, coord_comp2);
    }

    /* Returns TRUE if the SRF parameters are equal
    */
    public boolean isEqual(BaseSRF srf) {
        return ((srf != null) && (srf instanceof SRF_LocalSpacePolar) &&
        (this._orm == srf.get_orm()) && (this._hsr == srf.get_hsr()));
    }

    /// Returns a String with the parameter values
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String retString = new String();

        retString = retString + "orm: " + _orm + "\n";
        retString = retString + "hsr: " + _hsr;

        return retString;
    }
}
