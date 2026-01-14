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
 * �_�Ԃ� B-spline �Ȗʂŕ��(Interpolation)���邽�߂̃N���X(3D)
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:13 $
 */

class InterpolationSurface3D {
    /**
     * U���̕�ԂɕK�v�ȏ��
     *
     * @see Interpolation
     */
    private Interpolation uInfo;

    /**
     * V���̕�ԂɕK�v�ȏ��
     *
     * @see Interpolation
     */
    private Interpolation vInfo;

    /**
     * ��Ԃ���_��
     *
     * @see Point3D
     */
    private Point3D[][] points;

    /**
     * U���̗��[�ł̌X��(U�����Ă���ꍇ�͎Q�Ƃ��Ȃ�)
     *
     * @see Vector3D
     */
    private Vector3D[][] uEndvecs;

    /**
     * V���̗��[�ł̌X��(V�����Ă���ꍇ�͎Q�Ƃ��Ȃ�)
     *
     * @see Vector3D
     */
    private Vector3D[][] vEndvecs;

    /**
     * �_��A�p�����[�^��^���ăI�u�W�F�N�g��\�z����
     *
     * @param points    �_��
     * @param uParams   U �p�����[�^
     * @param vParams   V �p�����[�^
     * @param uIsClosed U�����Ă��邩�ǂ���
     * @param vIsClosed V�����Ă��邩�ǂ���
     */
    InterpolationSurface3D(Point3D[][] points,
                           double[] uParams, double[] vParams,
                           boolean uIsClosed, boolean vIsClosed) {
        this.uInfo = new Interpolation(uParams, uIsClosed);
        this.vInfo = new Interpolation(vParams, vIsClosed);
        this.points = points;
        Vector3D[] tmpVecs;
        if (!uIsClosed) {
            /*
            * �x�b�Z���̏I�[�쏂�p����
            */
            uEndvecs = new Vector3D[2][vInfo.uip];
            Point3D[] auxPoints = new Point3D[uInfo.uip];
            for (int i = 0; i < vInfo.uip; i++) {
                for (int j = 0; j < uInfo.uip; j++)
                    auxPoints[j] = points[j][i];
                tmpVecs = Interpolation3D.besselPoints(auxPoints, uParams);
                for (int j = 0; j < 2; j++)
                    uEndvecs[j][i] = tmpVecs[j];
            }
        }
        if (!vIsClosed) {
            /*
            * �x�b�Z���̏I�[�쏂�p����
            */
            vEndvecs = new Vector3D[2][uInfo.uip];
            for (int i = 0; i < uInfo.uip; i++) {
                tmpVecs = Interpolation3D.besselPoints(points[i], vParams);
                for (int j = 0; j < 2; j++)
                    vEndvecs[j][i] = tmpVecs[j];
            }
        }
    }

    /**
     * �_��A�p�����[�^�A���[�̌X����^���ăI�u�W�F�N�g��\�z����
     *
     * @param points    �_��
     * @param uParams   U �p�����[�^
     * @param vParams   V �p�����[�^
     * @param uEndvecs  U���̗��[�̌X��(U�����Ă���ꍇ�͎Q�Ƃ��Ȃ�)
     * @param vEndvecs  V���̗��[�̌X��(V�����Ă���ꍇ�͎Q�Ƃ��Ȃ�)
     * @param uIsClosed U�����Ă��邩�ǂ���
     * @param vIsClosed V�����Ă��邩�ǂ���
     */
    InterpolationSurface3D(Point3D[][] points,
                           double[] uParams, double[] vParams,
                           Vector3D[][] uEndvecs, Vector3D[][] vEndvecs,
                           boolean uIsClosed, boolean vIsClosed) {
        this.uInfo = new Interpolation(uParams, uIsClosed);
        this.vInfo = new Interpolation(vParams, vIsClosed);
        this.points = points;
        if (!uIsClosed)
            this.uEndvecs = uEndvecs;
        if (!vIsClosed)
            this.vEndvecs = vEndvecs;
    }

    /**
     * U���̃m�b�g����Ԃ�
     *
     * @return U���̃m�b�g���
     * @see BsplineKnot
     */
    BsplineKnot uKnotData() {
        return uInfo.knotData();
    }

    /**
     * V���̃m�b�g����Ԃ�
     *
     * @return V���̃m�b�g���
     * @see BsplineKnot
     */
    BsplineKnot vKnotData() {
        return vInfo.knotData();
    }

    /**
     * ����_�끂߂�
     * <p/>
     * gh3intpCBssC2 (in gh3intpCBsp.c)
     *
     * @return ����_
     */
    Point3D[][] controlPoints() {
        Interpolation3D intp;
        int u_uicp = uInfo.nControlPoints();
        int v_uicp = vInfo.nControlPoints();
        Point3D[][] controlPoints = new Point3D[u_uicp][v_uicp];
        Point3D[][] auxPoints = new Point3D[v_uicp][uInfo.uip];
        Vector3D[][] auxUEndVecs = null;
        Point3D[] work;
        Vector3D[] tmpVecs = new Vector3D[2];
        int i, j;

        /*
        * V���̊e����Ԃ���
        */
        for (j = 0; j < uInfo.uip; j++) {
            if (!vInfo.isClosed)
                for (i = 0; i < 2; i++)
                    tmpVecs[i] = vEndvecs[i][j];

            work = Interpolation3D.controlPoints(vInfo, points[j], tmpVecs);
            for (i = 0; i < v_uicp; i++)
                auxPoints[i][j] = work[i];
        }
        if (!uInfo.isClosed) {
            /*
            * ���[�̌X�����Ԃ���(U���̕�Ԃɗp������)
            */
            auxUEndVecs = new Vector3D[2][];
            for (j = 0; j < 2; j++) {
                work = Interpolation3D.controlPoints(vInfo, Vector3D.toPoint3D(uEndvecs[j]), null);
                auxUEndVecs[j] = Point3D.toVector3D(work);
            }
        }

        /*
        * V���ɕ�Ԃ��ꂽ����_�쳂ɁAU���̊e����Ԃ���
        */
        for (j = 0; j < v_uicp; j++) {
            if (!uInfo.isClosed)
                for (i = 0; i < 2; i++)
                    tmpVecs[i] = auxUEndVecs[i][j];

            work = Interpolation3D.controlPoints(uInfo, auxPoints[j], tmpVecs);
            for (i = 0; i < u_uicp; i++)
                controlPoints[i][j] = work[i];
        }

        return controlPoints;
    }

    /**
     * �d�݂끂߂�
     *
     * @return �d��(�������`���Ȃ̂�null)
     */
    double[][] weights() {
        return null;
    }
}
