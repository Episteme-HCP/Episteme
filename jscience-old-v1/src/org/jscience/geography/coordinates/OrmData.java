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
public class OrmData {
    /** DOCUMENT ME! */
    public double A;

    /** DOCUMENT ME! */
    public double A_inv;

    /** DOCUMENT ME! */
    public double B;

    /** DOCUMENT ME! */
    public double B_inv;

    /** DOCUMENT ME! */
    public double F;

    /** DOCUMENT ME! */
    public double F_inv;

    /** DOCUMENT ME! */
    public double C;

    /** DOCUMENT ME! */
    public double Eps2;

    /** DOCUMENT ME! */
    public double Eps;

    /** DOCUMENT ME! */
    public double Epps2;

    /** DOCUMENT ME! */
    public double EpsH;

    /** DOCUMENT ME! */
    public double Eps25;

    /** DOCUMENT ME! */
    public double A2;

    /** DOCUMENT ME! */
    public double A2_inv;

    /** DOCUMENT ME! */
    public double C2;

/**
     * Creates a new OrmData object.
     *
     * @param orm DOCUMENT ME!
     */
    public OrmData(SRM_ORM_Code orm) {
        SRM_RD_Code rdCode = OrmDataSet.getElem(orm)._rd_code;
        SRM_ORMT_Code tmpORMT = OrmDataSet.getElem(orm)._orm_template;

        if (rdCode != SRM_RD_Code.RD_UNDEFINED) {
            RdDataSet rdSet = RdDataSet.getElem(rdCode);

            if (tmpORMT == SRM_ORMT_Code.ORMT_SPHERE) {
                A = rdSet._R;
                F_inv = Double.NaN;
                F = 0.0;

                A_inv = 1.0 / (A);
                A2 = (A * A);
                A2_inv = 1.0 / A2;
                B = (A) * (1.0 - F);
                B_inv = A_inv;

                C = (A) * (1.0 - F);
                Eps2 = 0.0;
                Eps = Math.sqrt(Eps2);
                Eps25 = .25 * (Eps2);
                Epps2 = (Eps2) / (1.0 - Eps2);
                EpsH = (Eps) * 0.5;
                C2 = C * C;
            } else if ((tmpORMT == SRM_ORMT_Code.ORMT_OBLATE_ELLIPSOID) ||
                    (tmpORMT == SRM_ORMT_Code.ORMT_TRI_AXIAL_ELLIPSOID)) {
                A = rdSet._A;

                F_inv = rdSet._inv_F;

                F = 1 / F_inv;

                A_inv = 1.0 / (A);
                A2 = (A * A);
                A2_inv = 1.0 / (A * A);
                B = A * (1.0 - F);
                B_inv = 1.0 / B;

                C = (A) * (1.0 - F);
                Eps2 = (F) * (2.0 - F);
                Eps = Math.sqrt(Eps2);
                Eps25 = .25 * (Eps2);
                Epps2 = (Eps2) / (1.0 - Eps2);
                EpsH = (Eps) * 0.5;
                C2 = C * C;
            }
        }

        // else all the values initialized to zero.
    }
}
