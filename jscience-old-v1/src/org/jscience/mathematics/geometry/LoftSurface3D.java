/*
 * ���t�g�ʂ�?�?����邽�߂̃N���X(3D)
 * �؎� 7.2.2
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: LoftSurface3D.java,v 1.3 2006/03/28 21:47:44 virtualcall Exp $
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
