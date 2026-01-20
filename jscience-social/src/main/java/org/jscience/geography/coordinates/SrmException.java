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
package org.jscience.geography.coordinates;

/**
 * @author David Shen
 * @brief Declaration of SRM Exception class.
 */
public class SrmException extends Exception {
    /// Exception Code

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_SRF = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_SOURCE_SRF = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_TARGET_SRF = 4;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_SOURCE_COORDINATE = 5;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_TARGET_COORDINATE = 6;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_SOURCE_DIRECTION = 7;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_INPUT = 8;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_CODE = 9;

    /**
     * DOCUMENT ME!
     */
    public static final int _CREATION_FAILURE = 10;

    /**
     * DOCUMENT ME!
     */
    public static final int _DESTRUCTION_FAILURE = 11;

    /**
     * DOCUMENT ME!
     */
    public static final int _OPERATION_UNSUPPORTED = 12;

    /**
     * DOCUMENT ME!
     */
    public static final int _FLOATING_OVERFLOW = 13;

    /**
     * DOCUMENT ME!
     */
    public static final int _FLOATING_UNDERFLOW = 14;

    /**
     * DOCUMENT ME!
     */
    public static final int _FLOATING_POINT_ERROR = 15;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_TARGET_DIRECTION = 16;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_SOURCE_ORIENTATION = 17;

    /**
     * DOCUMENT ME!
     */
    public static final int _INVALID_TARGET_ORIENTATION = 18;

    /**
     * DOCUMENT ME!
     */
    public static final int _NOT_IMPLEMENTED = 19;

    /**
     * DOCUMENT ME!
     */
    public static final int _INACTIONABLE = 20;

    /**
     * DOCUMENT ME!
     */
    public static final int _OUT_OF_MEMORY = 21;
    private int _exceptionCode = 0;
    private String _exceptionMsg = new String("General Exception");

    protected SrmException(int exceptionCode, String exceptionMsg) {
        super();
        _exceptionCode = exceptionCode;
        _exceptionMsg = exceptionMsg;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int what() {
        return _exceptionCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return _exceptionMsg;
    }
}
