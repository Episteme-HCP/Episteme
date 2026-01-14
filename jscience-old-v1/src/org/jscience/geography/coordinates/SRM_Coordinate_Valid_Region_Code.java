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
package org.jscience.geography.coordinates;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author David Shen
 * @brief Declaration of SRF validity enumeration class.
 */
public class SRM_Coordinate_Valid_Region_Code extends Enum {
    private static Vector _enumVec = new Vector();
    private static HashMap _enumMap = new HashMap();
    protected static final int _UNDEFINED = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int _VALID = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int _EXTENDED_VALID = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int _DEFINED = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int _totalEnum = 3;
    protected static final SRM_Coordinate_Valid_Region_Code UNDEFINED = new SRM_Coordinate_Valid_Region_Code(_UNDEFINED,
            "UNDEFINED");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_Coordinate_Valid_Region_Code VALID = new SRM_Coordinate_Valid_Region_Code(_VALID,
            "VALID");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_Coordinate_Valid_Region_Code EXTENDED_VALID = new SRM_Coordinate_Valid_Region_Code(_EXTENDED_VALID,
            "EXTENDED_VALID");

    /**
     * DOCUMENT ME!
     */
    public static final SRM_Coordinate_Valid_Region_Code DEFINED = new SRM_Coordinate_Valid_Region_Code(_DEFINED,
            "DEFINED");

    private SRM_Coordinate_Valid_Region_Code(int code, String name) {
        super(code, name);
        _enumMap.put(name, this);
        _enumVec.add(code, this);
    }

    /// returns the Coord Valid Region object from its enumerant value
    public static SRM_Coordinate_Valid_Region_Code getEnum(int enumeration)
            throws SrmException {
        if ((enumeration < 1) || (enumeration > _totalEnum)) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_Coordinate_Valid_Region_Code.getEnum: enumerant out of range"));
        } else {
            return (SRM_Coordinate_Valid_Region_Code) (_enumVec.elementAt(enumeration));
        }
    }

    /// returns the SRM_Coordinate_Valid_Region_Code from its string name
    public static SRM_Coordinate_Valid_Region_Code getEnum(String name)
            throws SrmException {
        SRM_Coordinate_Valid_Region_Code retCode = (SRM_Coordinate_Valid_Region_Code) _enumMap.get(name);

        if (retCode == null) {
            throw new SrmException(SrmException._INVALID_INPUT,
                    new String("SRM_Coordinate_Valid_Region_Code.getEnum: enum. string not found=> " +
                            name));
        }

        return retCode;
    }
}
