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

package org.jscience.mathematics.geometry;

/**
 * ���t�g�ʂ�?�?����邽�߂̃N���X(3D)
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/28 21:47:44 $
 */
class LoftSurface3D {
    /*
     * ���?�
     */
    /** DOCUMENT ME! */
    private final BsplineCurve3D basisCurve;

    /*
     * �|�����\���x�N�g��
     */
    /** DOCUMENT ME! */
    private final Vector3D axisVector;

    /*
     * �|��钷��
     */
    /** DOCUMENT ME! */
    private final double length;

/**
     * ���?�A�|����A�|����^����?A�I�u�W�F�N�g��\�z����
     *
     * @param basisCurve ���?�
     * @param axisVector �|�����\���x�N�g��
     * @param length     �|��钷��
     */
    LoftSurface3D(BsplineCurve3D basisCurve, Vector3D axisVector, double length) {
        this.basisCurve = basisCurve;
        this.axisVector = axisVector;
        this.length = length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    BsplineSurface3D getSurface() {
        Vector3D moveVector = axisVector.unitized().multiply(length);

        int uNControlPoints = 2;
        int vNControlPoints = basisCurve.nControlPoints();
        Point3D[][] controlPoints = new Point3D[uNControlPoints][vNControlPoints];

        for (int j = 0; j < vNControlPoints; j++) {
            controlPoints[0][j] = basisCurve.controlPointAt(j);
            controlPoints[1][j] = controlPoints[0][j].add(moveVector);
        }

        double[][] weights = null;

        if (basisCurve.isRational()) {
            weights = new double[uNControlPoints][vNControlPoints];

            for (int j = 0; j < vNControlPoints; j++) {
                weights[0][j] = weights[1][j] = basisCurve.weightAt(j);
            }
        }

        int uDegree = 1;
        double[] uKnots = new double[2];
        uKnots[0] = 0.0;
        uKnots[1] = length;

        int[] uKnotMulti = new int[2];
        uKnotMulti[0] = uKnotMulti[1] = 2;

        BsplineKnot uKnotData = new BsplineKnot(uDegree, KnotType.UNSPECIFIED,
                false, uKnotMulti, uKnots, uNControlPoints);

        return new BsplineSurface3D(uKnotData, basisCurve.knotData(),
            controlPoints, weights);
    }
}
