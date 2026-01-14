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
 @brief Declaration of coordinate base classes.
 */
package org.jscience.geography.coordinates;

/**
 * The Coord abstract class is the base class for all coordinate types.
 * Coordinates can be allocated on the stack, by using "new", or by using the
 * SRF class methods.
 *
 * @author David Shen
 *
 * @see Coord2D, Coord3D
 */
public abstract class Coord implements Coordinates, Cloneable {
    /// The SRF this coordinate belongs to.
    /**
     * DOCUMENT ME!
     */
    private BaseSRF _srf;

    /// The class type of the coordinate
    /**
     * DOCUMENT ME!
     */
    private Coord_ClassType _type;

    /// Constructor setting the associated SRF and the concrete class type
    /**
     * Creates a new Coord object.
     *
     * @param assocSRF DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    protected Coord(BaseSRF assocSRF, Coord_ClassType type) {
        _srf = assocSRF;
        _type = type;
    }

    /// Returns the SRF this coordassocSRFinate belongs to.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BaseSRF getSRF() {
        return _srf;
    }

    /// Returns an array containing the coordinate component values.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract double[] getValues();

    /// Returns the class type of this coordinate.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord_ClassType getClassType() {
        return _type;
    }

    /// Returns the copy of this coordinate object instance.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SrmException DOCUMENT ME!
     */
    public Coord makeClone() throws SrmException {
        try {
            return (Coord) super.clone();
        } catch (java.lang.CloneNotSupportedException ex) {
            throw new SrmException(SrmException._INACTIONABLE,
                new String("Coord.makeClone(): failed"));
        }
    }

    /// Returns TRUE if the coordinate component values are "equal"
    /**
     * DOCUMENT ME!
     *
     * @param coord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isEqual(Coord coord);

    // Returns the string with coordinate component labels and values
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String toString();
}
