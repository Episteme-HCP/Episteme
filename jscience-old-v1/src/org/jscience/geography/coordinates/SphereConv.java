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

package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class SphereConv extends Conversions {
/**
     * Creates a new SphereConv object.
     *
     * @param src  DOCUMENT ME!
     * @param dest DOCUMENT ME!
     */
    protected SphereConv(SRM_SRFT_Code src, SRM_SRFT_Code[] dest) {
        super(src, dest);
    }

    /*
     *----------------------------------------------------------------------------
     *
     * ToSphere routines
     *
     *----------------------------------------------------------------------------
     */
    protected SRM_Coordinate_Valid_Region_Code toCcen(BaseSRF srcSrf,
        BaseSRF destSrf, double[] source_generic_coord,
        double[] dest_generic_coord) throws SrmException {
        SRM_Coordinate_Valid_Region_Code retValid = SRM_Coordinate_Valid_Region_Code.VALID;

        double lambda = source_generic_coord[0];
        double theta = source_generic_coord[1];
        double rho = source_generic_coord[2];

        // x
        dest_generic_coord[0] = rho * Math.cos(theta) * Math.cos(lambda);

        // y
        dest_generic_coord[1] = rho * Math.cos(theta) * Math.sin(lambda);

        // z
        dest_generic_coord[2] = rho * Math.sin(theta);

        return retValid;
    }
}
