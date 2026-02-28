/*
 * �^�u�V���ʂ�?�?����邽�߂̃N���X(3D)
 * �؎� 7.2.1
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: TabcylSurface3D.java,v 1.3 2006/03/28 21:47:45 virtualcall Exp $
 */
package org.episteme.mathematics.geometry;

/**
 * �^�u�V���ʂ�?�?����邽�߂̃N���X(3D)
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/28 21:47:45 $
 */
class TabcylSurface3D {
    /*
     * ���̃K�C�h��?�
     */
    /** DOCUMENT ME! */
    private final BsplineCurve3D basisCurve1;

    /*
     * ����̃K�C�h��?�
     */
    /** DOCUMENT ME! */
    private final BsplineCurve3D basisCurve2;

/**
     * 2�{��Bspline��?��^����?A�I�u�W�F�N�g��\�z����
     *
     * @param basisCurve1 ���̃K�C�h��?�
     * @param basisCurve2 ����̃K�C�h��?�
     */
    TabcylSurface3D(BsplineCurve3D basisCurve1, BsplineCurve3D basisCurve2) {
        this.basisCurve1 = basisCurve1;
        this.basisCurve2 = basisCurve2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    BsplineSurface3D getSurface() {
        BsplineCurve3D[] crvs = new BsplineCurve3D[2];
        crvs[0] = basisCurve1;
        crvs[1] = basisCurve2;

        ParameterSection[] sec = new ParameterSection[2];

        for (int i = 0; i < 2; i++) {
            sec[i] = crvs[i].parameterDomain().section();
        }

        double sleng = crvs[0].coordinates(sec[0].start())
                              .distance(crvs[1].coordinates(sec[1].start()));
        double eleng = crvs[0].coordinates(sec[0].end())
                              .distance(crvs[1].coordinates(sec[1].end()));
        double leng = (sleng + eleng) / 2.0;

        ParameterSection uSec = new ParameterSection(0.0, leng);

        crvs = BsplineCurve3D.identicalKnotSequences(crvs, uSec);

        boolean hasRational = false;

        for (int i = 0; i < crvs.length; i++)
            if (crvs[i].isRational()) {
                hasRational = true;
            }

        if (hasRational) {
            for (int i = 0; i < crvs.length; i++)
                crvs[i] = crvs[i].toBsplineCurve();
        }

        int uNControlPoints = 2;
        int vNControlPoints = crvs[0].nControlPoints();
        Point3D[][] controlPoints = new Point3D[uNControlPoints][vNControlPoints];

        for (int i = 0; i < uNControlPoints; i++)
            for (int j = 0; j < vNControlPoints; j++)
                controlPoints[i][j] = crvs[i].controlPointAt(j);

        double[][] weights = null;

        if (hasRational) {
            weights = new double[uNControlPoints][vNControlPoints];

            for (int i = 0; i < uNControlPoints; i++)
                for (int j = 0; j < vNControlPoints; j++)
                    weights[i][j] = crvs[i].weightAt(j);
        }

        int uDegree = 1;
        double[] uKnots = new double[2];
        uKnots[0] = uSec.start();
        uKnots[1] = uSec.end();

        int[] uKnotMulti = new int[2];
        uKnotMulti[0] = uKnotMulti[1] = 2;

        BsplineKnot uKnotData = new BsplineKnot(uDegree, KnotType.UNSPECIFIED,
                false, uKnotMulti, uKnots, uNControlPoints);

        return new BsplineSurface3D(uKnotData, crvs[0].knotData().beautify(),
            controlPoints, weights);
    }
}
