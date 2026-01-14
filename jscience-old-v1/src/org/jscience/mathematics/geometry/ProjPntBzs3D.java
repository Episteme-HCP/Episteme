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

import org.jscience.mathematics.MathUtils;

import java.util.Enumeration;
import java.util.Vector;


/**
 * 3D
 * �_����x�W�G�Ȗʂւ�?�?�̑���?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:26 $
 */
final class ProjPntBzs3D {
    /**
     * �Ք�N���X�̃C���X�^���X��?�?����邽�߂̃C���X�^���X
     */
    private static ProjPntBzs3D myOwnInstance = new ProjPntBzs3D();

    /**
     * �I�u�W�F�N�g��?\�z����
     */
    private ProjPntBzs3D() {
    }

    /*
     * �^����ꂽ�x�W�G�Ȗʂ� U ���̈ꎟ�Γ���?���\���x�W�G�Ȗʂ�Ԃ� : gh3pdrv_bzs()
     */
    private static PureBezierSurface3D getPartialDerivForU(
            PureBezierSurface3D bzs) {
        int degree = bzs.uDegree();
        int uncp = bzs.uNControlPoints() - 1;
        int vncp = bzs.vNControlPoints();

        Point3D[][] partialDerivControlPoints = new Point3D[uncp][vncp];
        PureBezierSurface3D partialDeriv;

        if (bzs.isPolynomial() == true) {
            for (int j = 0; j < vncp; j++) {
                for (int i = 0; i < uncp; i++) {
                    partialDerivControlPoints[i][j] = bzs.controlPointAt(i + 1,
                            j).subtract(bzs.controlPointAt(i, j))
                            .multiply(degree)
                            .toPoint3D();
                }
            }

            partialDeriv = new PureBezierSurface3D(partialDerivControlPoints);
        } else {
            double[][] partialDerivWeights = new double[uncp][vncp];

            for (int j = 0; j < vncp; j++) {
                for (int i = 0; i < uncp; i++) {
                    partialDerivControlPoints[i][j] = bzs.controlPointAt(i + 1,
                            j).subtract(bzs.controlPointAt(i, j))
                            .multiply(degree)
                            .toPoint3D();
                    partialDerivWeights[i][j] = (bzs.weightAt(i + 1, j) -
                            bzs.weightAt(i, j)) * degree;
                }
            }

            partialDeriv = new PureBezierSurface3D(partialDerivControlPoints,
                    partialDerivWeights, false);
        }

        return partialDeriv;
    }

    /*
     * �^����ꂽ�x�W�G�Ȗʂ� V ���̈ꎟ�Γ���?���\���x�W�G�Ȗʂ�Ԃ� : gh3pdrv_bzs()
     */
    private static PureBezierSurface3D getPartialDerivForV(
            PureBezierSurface3D bzs) {
        int degree = bzs.vDegree();
        int uncp = bzs.uNControlPoints();
        int vncp = bzs.vNControlPoints() - 1;

        Point3D[][] partialDerivControlPoints = new Point3D[uncp][vncp];
        PureBezierSurface3D partialDeriv;

        if (bzs.isPolynomial() == true) {
            for (int j = 0; j < vncp; j++) {
                for (int i = 0; i < uncp; i++) {
                    partialDerivControlPoints[i][j] = bzs.controlPointAt(i,
                            j + 1).subtract(bzs.controlPointAt(i, j))
                            .multiply(degree)
                            .toPoint3D();
                }
            }

            partialDeriv = new PureBezierSurface3D(partialDerivControlPoints);
        } else {
            double[][] partialDerivWeights = new double[uncp][vncp];

            for (int j = 0; j < vncp; j++) {
                for (int i = 0; i < uncp; i++) {
                    partialDerivControlPoints[i][j] = bzs.controlPointAt(i,
                            j + 1).subtract(bzs.controlPointAt(i, j))
                            .multiply(degree)
                            .toPoint3D();
                    partialDerivWeights[i][j] = (bzs.weightAt(i, j + 1) -
                            bzs.weightAt(i, j)) * degree;
                }
            }

            partialDeriv = new PureBezierSurface3D(partialDerivControlPoints,
                    partialDerivWeights, false);
        }

        return partialDeriv;
    }

    /* gh3prod_bzsbzs(3, ...), gh3dpf_bzsbzs() */

    /**
     * ����̒�`���?��̃x�W�G�Ȗʂ̓�?ς�\���x�W�G��?���Ԃ�
     *
     * @param bzs1 �x�W�G�ȖʂP
     * @param bzs2 �x�W�G�ȖʂQ
     * @return ��̃x�W�G�Ȗʂ̓�?ς�\���x�W�G��?�
     */
    private static BezierSurface1D getProductFunctionOf3D(
            PureBezierSurface3D bzs1, PureBezierSurface3D bzs2) {
        int uDeg1 = bzs1.uDegree();
        int vDeg1 = bzs1.vDegree();

        int uDeg2 = bzs2.uDegree();
        int vDeg2 = bzs2.vDegree();

        int uDeg = uDeg1 + uDeg2;
        int vDeg = vDeg1 + vDeg2;

        int uNcp = uDeg + 1;
        int vNcp = vDeg + 1;

        double[] uBinCoef1 = MathUtils.pascalTriangle(bzs1.uNControlPoints());
        double[] vBinCoef1 = MathUtils.pascalTriangle(bzs1.vNControlPoints());
        double[] uBinCoef2 = MathUtils.pascalTriangle(bzs2.uNControlPoints());
        double[] vBinCoef2 = MathUtils.pascalTriangle(bzs2.vNControlPoints());
        double[] uBinCoef = MathUtils.pascalTriangle(uNcp);
        double[] vBinCoef = MathUtils.pascalTriangle(vNcp);

        BezierSurface1D prodFunc = myOwnInstance.new BezierSurface1D();
        prodFunc.controlPoints = new double[uNcp][vNcp];
        prodFunc.weights = null;

        int j_vDeg1 = 0 - vDeg1;

        for (int j = 0; j <= vDeg; j++) {
            int max0j = Math.max(0, j_vDeg1);
            int mindj = Math.min(vDeg2, j);

            int i_uDeg1 = 0 - uDeg1;

            for (int i = 0; i <= uDeg; i++) {
                int max0i = Math.max(0, i_uDeg1);
                int mindi = Math.min(uDeg2, i);

                double rl = 0.0;
                int l = max0i;
                int i_l = i - max0i;

                for (; l <= mindi;) {
                    double rm = 0.0;
                    int m = max0j;
                    int j_m = j - max0j;

                    for (; m <= mindj;) {
                        Point3D b3p1 = bzs1.controlPointAt(i_l, j_m);
                        Point3D b3p2 = bzs2.controlPointAt(l, m);

                        rm += (vBinCoef2[m] * vBinCoef1[j_m] * ((b3p1.x() * b3p2.x()) +
                                (b3p1.y() * b3p2.y()) + (b3p1.z() * b3p2.z())));

                        m++;
                        j_m--;
                    }

                    rl += (uBinCoef2[l] * uBinCoef1[i_l] * rm);

                    l++;
                    i_l--;
                }

                prodFunc.controlPoints[i][j] = rl / (uBinCoef[i] * vBinCoef[j]);

                i_uDeg1++;
            }

            j_vDeg1++;
        }

        return prodFunc;
    }

    /* gh3prod_bzsbzs(1, ...), gh3prdf_bzfbzf() */

    /**
     * ����̒�`���?��̃x�W�G��?���
     * (��) ?ς�\���x�W�G��?���Ԃ�
     *
     * @param bzs1 �x�W�G��?��P
     * @param bzs2 �x�W�G��?��Q
     * @return ��̃x�W�G��?��� (��)
     *         ?ς�\���x�W�G��?�
     */
    private static BezierSurface1D getProductFunctionOf1D(
            BezierSurface1D bzs1, BezierSurface1D bzs2) {
        int uDeg1 = bzs1.uDegree();
        int vDeg1 = bzs1.vDegree();

        int uDeg2 = bzs2.uDegree();
        int vDeg2 = bzs2.vDegree();

        int uDeg = uDeg1 + uDeg2;
        int vDeg = vDeg1 + vDeg2;

        int uNcp = uDeg + 1;
        int vNcp = vDeg + 1;

        double[] uBinCoef1 = MathUtils.pascalTriangle(bzs1.uNControlPoints());
        double[] vBinCoef1 = MathUtils.pascalTriangle(bzs1.vNControlPoints());
        double[] uBinCoef2 = MathUtils.pascalTriangle(bzs2.uNControlPoints());
        double[] vBinCoef2 = MathUtils.pascalTriangle(bzs2.vNControlPoints());
        double[] uBinCoef = MathUtils.pascalTriangle(uNcp);
        double[] vBinCoef = MathUtils.pascalTriangle(vNcp);

        BezierSurface1D prodFunc = myOwnInstance.new BezierSurface1D();
        prodFunc.controlPoints = new double[uNcp][vNcp];
        prodFunc.weights = null;

        int j_vDeg1 = 0 - vDeg1;

        for (int j = 0; j <= vDeg; j++) {
            int max0j = Math.max(0, j_vDeg1);
            int mindj = Math.min(vDeg2, j);

            int i_uDeg1 = 0 - uDeg1;

            for (int i = 0; i <= uDeg; i++) {
                int max0i = Math.max(0, i_uDeg1);
                int mindi = Math.min(uDeg2, i);

                double rl = 0.0;
                int l = max0i;
                int i_l = i - max0i;

                for (; l <= mindi;) {
                    double rm = 0.0;
                    int m = max0j;
                    int j_m = j - max0j;

                    for (; m <= mindj;) {
                        double b1p1 = bzs1.controlPointAt(i_l, j_m);
                        double b1p2 = bzs2.controlPointAt(l, m);

                        rm += (vBinCoef2[m] * vBinCoef1[j_m] * (b1p1 * b1p2));

                        m++;
                        j_m--;
                    }

                    rl += (uBinCoef2[l] * uBinCoef1[i_l] * rm);

                    l++;
                    i_l--;
                }

                prodFunc.controlPoints[i][j] = rl / (uBinCoef[i] * vBinCoef[j]);

                i_uDeg1++;
            }

            j_vDeg1++;
        }

        return prodFunc;
    }

    /**
     * �x�W�G��?���?���\���x�W�G��?���Ԃ�
     *
     * @param bzf1 �x�W�G��?��P
     * @param bzf2 �x�W�G��?��Q
     * @return bzf1 - bzf2
     */
    private static BezierSurface1D makeSubtractOfTwoProdFunctions(
            BezierSurface1D bzf1, BezierSurface1D bzf2) {
        BezierSurface1D diff = myOwnInstance.new BezierSurface1D();
        diff.controlPoints = new double[bzf1.uNControlPoints()][bzf1.vNControlPoints()];
        diff.weights = null;

        // System.out.println("bzsf1 : " + bzf1.uNControlPoints() + ", " + bzf1.vNControlPoints());
        // System.out.println("bzsf2 : " + bzf2.uNControlPoints() + ", " + bzf2.vNControlPoints());
        for (int i = 0; i < bzf1.uNControlPoints(); i++)
            for (int j = 0; j < bzf1.vNControlPoints(); j++)
                diff.controlPoints[i][j] = bzf1.controlPoints[i][j] -
                        bzf2.controlPoints[i][j];

        return diff;
    }

    /**
     * �_����x�W�G�Ȗ�?�̂���_�ւ̃x�N�g����?A
     * <p/>
     * ���̋Ȗ�?�̓_�ł̕Γ���?��x�N�g���Ƃ̓�?ς̒l��\��
     * �Q�����̃x�W�G�Ȗʂ�?�?�����
     *
     * @param pnt �_
     * @param bzs �x�W�G�Ȗ�
     * @return �Q�����̃x�W�G�Ȗ�
     */
    private static PureBezierSurfaceWithGivenControlPointsArray2D makeBezierSurface2D(
            Point3D pnt, PureBezierSurface3D bzs) {
        /*
         * bzs �� pnt �촓_�Ƃ���?W�n�Ɉڂ�
         */
        int uncp = bzs.uNControlPoints();
        int vncp = bzs.vNControlPoints();
        Point3D[][] transformedControlPoints = new Point3D[uncp][vncp];

        PureBezierSurface3D eB; // transformed bzs
        BezierSurface1D eBdpB; // (eB(u, v), eB(u, v))

        PureBezierSurface3D eBdu; // U partial deriv. of eB
        PureBezierSurface3D eBdv; // V partial deriv. of eB

        BezierSurface1D eBdpu; // (eB(u,v), (deB(u,v) / du))
        BezierSurface1D eBmdpu; // degree-elevated eBdpu

        BezierSurface1D eBdpv; // (eB(u,v), (deB(u,v) / dv))
        BezierSurface1D eBmdpv; // degree-elevated eBdpv

        BezierSurface1D bzf_w = myOwnInstance.new BezierSurface1D();

        // aux. bezier function for degree elevation
        BezierSurface1D eBdpt1; // work area
        BezierSurface1D eBdpt2; // work area
        BezierSurface1D eBdpt3; // work area
        BezierSurface1D eBde; // work pointer

        if (bzs.isPolynomial() == true) {
            for (int u = 0; u < uncp; u++)
                for (int v = 0; v < vncp; v++)
                    transformedControlPoints[u][v] = bzs.controlPointAt(u, v)
                            .subtract(pnt)
                            .toPoint3D();

            eB = new PureBezierSurface3D(transformedControlPoints);
        } else {
            // �L�?�ł����?A����?W�ɕϊ����Ă���
            for (int u = 0; u < uncp; u++)
                for (int v = 0; v < vncp; v++)
                    transformedControlPoints[u][v] = bzs.controlPointAt(u, v)
                            .subtract(pnt)
                            .multiply(bzs.weightAt(
                                    u, v)).toPoint3D();

            eB = new PureBezierSurface3D(transformedControlPoints, bzs.weights());
        }

        if (bzs.isRational() == true) {
            eBdpB = getProductFunctionOf3D(eB, eB);
        } else {
            eBdpB = null;
        }

        /*
         * (eB(u,v), (deB(u,v) / du))
         */
        eBdu = getPartialDerivForU(eB);
        eBdpu = getProductFunctionOf3D(eB, eBdu);
        eBde = eBdpu;

        if (bzs.isRational() == true) {
            bzf_w.controlPoints = eB.weights;
            eBdpt1 = getProductFunctionOf1D(bzf_w, eBdpu);

            bzf_w.controlPoints = eBdu.weights;
            eBdpt2 = getProductFunctionOf1D(bzf_w, eBdpB);

            eBdpt3 = makeSubtractOfTwoProdFunctions(eBdpt1, eBdpt2);
            eBde = eBdpt3;
        }

        double[][] bzf_de_cntrl_pnts_21 = new double[2][1];
        bzf_de_cntrl_pnts_21[0][0] = 1.0;
        bzf_de_cntrl_pnts_21[1][0] = 1.0;

        bzf_w.controlPoints = bzf_de_cntrl_pnts_21;
        eBmdpu = getProductFunctionOf1D(bzf_w, eBde);

        /*
         * (eB(u,v), (deB(u,v) / dv))
         */
        eBdv = getPartialDerivForV(eB);
        eBdpv = getProductFunctionOf3D(eB, eBdv);
        eBde = eBdpv;

        if (bzs.isRational() == true) {
            bzf_w.controlPoints = eB.weights;
            eBdpt1 = getProductFunctionOf1D(bzf_w, eBdpv);

            bzf_w.controlPoints = eBdv.weights;
            eBdpt2 = getProductFunctionOf1D(bzf_w, eBdpB);

            eBdpt3 = makeSubtractOfTwoProdFunctions(eBdpt1, eBdpt2);
            eBde = eBdpt3;
        }

        double[][] bzf_de_cntrl_pnts_12 = new double[1][2];
        bzf_de_cntrl_pnts_12[0][0] = 1.0;
        bzf_de_cntrl_pnts_12[0][1] = 1.0;

        bzf_w.controlPoints = bzf_de_cntrl_pnts_12;
        eBmdpv = getProductFunctionOf1D(bzf_w, eBde);

        /*
         * (BezierSurface1D, BezierSurface1D) => BezierSurface2D
         */
        double[][][] controlPoints = FreeformSurfaceWithGivenControlPointsArray2D.allocateDoubleArray(true,
                eBmdpu.uNControlPoints(), eBmdpu.vNControlPoints());

        for (int i = 0; i < eBmdpu.uNControlPoints(); i++) {
            for (int j = 0; j < eBmdpu.vNControlPoints(); j++) {
                controlPoints[i][j][0] = eBmdpu.controlPoints[i][j];
                controlPoints[i][j][1] = eBmdpv.controlPoints[i][j];
            }
        }

        return new PureBezierSurfaceWithGivenControlPointsArray2D(controlPoints);
    }

    /**
     * ��̃p���??[�^�l�̑g���������Ƃ݂Ȃ��邩�ۂ�
     *
     * @param pos       �p���??[�^�l�̑g
     * @param paramPair �p���??[�^�l�̑g
     * @param dTol      �����̋��e��?�
     * @return DOCUMENT ME!
     */
    private static boolean twoPointsCoincide(PointOnSurface3D pos,
                                             double[] paramPair, ToleranceForDistance dTol) {
        Point3D crd0 = pos.basisSurface().coordinates(pos.uParameter(),
                pos.vParameter());
        Point3D crd1 = pos.basisSurface().coordinates(paramPair[0], paramPair[1]);

        if (crd0.distance2(crd1) > dTol.squared()) {
            return false;
        }

        double uDiff = pos.uParameter() - paramPair[0];
        double vDiff = pos.vParameter() - paramPair[1];

        if ((Math.abs(uDiff) < dTol.toToleranceForParameterU(
                pos.basisSurface(), pos.uParameter(), pos.vParameter())
                .value()) &&
                (Math.abs(vDiff) < dTol.toToleranceForParameterV(
                        pos.basisSurface(), pos.uParameter(), pos.vParameter())
                        .value())) {
            return true;
        }

        return false;
    }

    /**
     * �^����ꂽ�p���??[�^�̑g���Ƃ��ă��X�g�ɉB���
     *
     * @param bzs       �x�W�G�Ȗ�
     * @param paramPair �p���??[�^�̑g
     * @param dTol      �����̋��e��?�
     * @param zpl       ��̃��X�g
     */
    private static void addAsSolution(PureBezierSurface3D bzs,
                                      double[] paramPair, ToleranceForDistance dTol, Vector zpl) {
        for (Enumeration e = zpl.elements(); e.hasMoreElements();) {
            if (twoPointsCoincide((PointOnSurface3D) e.nextElement(),
                    paramPair, dTol) == true) {
                return;
            }
        }

        zpl.addElement(new PointOnSurface3D(bzs, paramPair[0], paramPair[1]));
    }

    /**
     * �Q�����̃x�W�G�Ȗʂ� (0, 0)
     * �ƂȂ�p���??[�^�l (u, v) ��?�߂�
     *
     * @param bzs3D �R�����̃x�W�G�Ȗ�
     * @param bzs   �Q�����̃x�W�G�Ȗ�
     * @param uLbp  �Q�����̃x�W�G�Ȗʂ� U
     *              ���̃p���??[�^�l����
     * @param uUbp  �Q�����̃x�W�G�Ȗʂ� U
     *              ���̃p���??[�^�l?��
     * @param vLbp  �Q�����̃x�W�G�Ȗʂ� V
     *              ���̃p���??[�^�l����
     * @param vUbp  �Q�����̃x�W�G�Ȗʂ� V
     *              ���̃p���??[�^�l?��
     * @param dTol  �����̋��e��?�
     * @param zpl   �Q�����̃x�W�G�Ȗʂ� (0, 0)
     *              �ƂȂ�p���??[�^�l (u, v) �̃��X�g
     */
    private static void getZeroPoints(PureBezierSurface3D bzs3D,
                                      PureBezierSurfaceWithGivenControlPointsArray2D bzs, double uLbp,
                                      double uUbp, double vLbp, double vUbp, ToleranceForDistance dTol,
                                      Vector zpl) {
        double[] paramCenter = new double[2];
        paramCenter[0] = (uLbp + uUbp) / 2.0;
        paramCenter[1] = (vLbp + vUbp) / 2.0;

        // bzs �̑�?ݔ͈͂���`�𓾂�
        EnclosingBox2D box = bzs.approximateEnclosingBox();

        // ��`�����_ (0, 0) ��܂܂Ȃ��Ȃ��?A���µ�Ȃ�
        if ((box.min().x() > 0.0) || (box.min().y() > 0.0) ||
                (box.max().x() < 0.0) || (box.max().y() < 0.0)) {
            return;
        }

        // ��`��?\����?������Ȃ��?Azero point list �ɉB���
        if ((box.min().x() > (-dTol.value())) &&
                (box.min().y() > (-dTol.value())) &&
                (box.max().x() < dTol.value()) &&
                (box.max().y() < dTol.value())) {
            addAsSolution(bzs3D, paramCenter, dTol, zpl);

            return;
        }

        // �l��������
        PureBezierSurfaceWithGivenControlPointsArray2D[] uBzs = bzs.uDivide(0.5);
        PureBezierSurfaceWithGivenControlPointsArray2D[] u0vBzs = uBzs[0].vDivide(0.5);
        PureBezierSurfaceWithGivenControlPointsArray2D[] u1vBzs = uBzs[1].vDivide(0.5);

        // ���ꂼ��ɂ���?Azero point �𒲂ׂ�
        getZeroPoints(bzs3D, u0vBzs[0], uLbp, paramCenter[0], vLbp,
                paramCenter[1], dTol, zpl);
        getZeroPoints(bzs3D, u0vBzs[1], uLbp, paramCenter[0], paramCenter[1],
                vUbp, dTol, zpl);
        getZeroPoints(bzs3D, u1vBzs[0], paramCenter[0], uUbp, vLbp,
                paramCenter[1], dTol, zpl);
        getZeroPoints(bzs3D, u1vBzs[1], paramCenter[0], uUbp, paramCenter[1],
                vUbp, dTol, zpl);
    }

    /**
     * �x�W�G�Ȗʂ̋��E?��Ԃ�
     *
     * @param bzs �x�W�G�Ȗ�
     * @param i   ���E�̔�?�
     * @return DOCUMENT ME!
     */
    private static PureBezierCurve3D getBoundary(PureBezierSurface3D bzs, int i) {
        Point3D[] controlPoints;
        double[] weights = null;
        int anotherIndex;

        switch (i) {
            case 0: /* U = 0 */
                anotherIndex = 0;

                break;

            case 1: /* U = 1 */
                anotherIndex = bzs.uDegree();

                break;

            case 2: /* V = 0 */
                anotherIndex = 0;

                break;

            case 3: /* V = 1 */
                anotherIndex = bzs.vDegree();

                break;

            default:
                return null;
        }

        if (i <= 1) {
            controlPoints = new Point3D[bzs.vNControlPoints()];

            for (i = 0; i < bzs.vNControlPoints(); i++)
                controlPoints[i] = bzs.controlPointAt(anotherIndex, i);

            if (bzs.isRational() == true) {
                weights = new double[bzs.vNControlPoints()];

                for (i = 0; i < bzs.vNControlPoints(); i++)
                    weights[i] = bzs.weightAt(anotherIndex, i);
            }
        } else {
            controlPoints = new Point3D[bzs.uNControlPoints()];

            for (i = 0; i < bzs.uNControlPoints(); i++)
                controlPoints[i] = bzs.controlPointAt(i, anotherIndex);

            if (bzs.isRational() == true) {
                weights = new double[bzs.uNControlPoints()];

                for (i = 0; i < bzs.uNControlPoints(); i++)
                    weights[i] = bzs.weightAt(i, anotherIndex);
            }
        }

        if (bzs.isPolynomial() == true) {
            return new PureBezierCurve3D(controlPoints);
        } else {
            return new PureBezierCurve3D(controlPoints, weights);
        }
    }

    /**
     * �x�W�G�Ȗʂ̋��E?�ɗ�����?�?��?�߂�
     *
     * @param pnt  �_
     * @param bzs  �x�W�G�Ȗ�
     * @param dTol �����̋��e��?�
     * @param aTol �p�x�̋��e��?�
     * @param zpl  �Q�����̃x�W�G�Ȗʂ� (0, 0)
     *             �ƂȂ�p���??[�^�l (u, v) �̔z��
     */
    private static void computeWithBoundary(Point3D pnt,
                                            PureBezierSurface3D bzs, ToleranceForDistance dTol,
                                            ToleranceForAngle aTol, Vector zpl) {
        double param;
        PureBezierCurve3D bzc;
        PointOnCurve3D[] feet;
        double[] paramPair = new double[2];
        Vector3D footVector;
        Vector3D[] tangents;

        for (int i = 0; i < 4; i++) {
            param = ((i % 2) == 0) ? 0.0 : 1.0;
            bzc = getBoundary(bzs, i);
            feet = bzc.projectFrom(pnt);

            if (feet.length <= 0) {
                if ((pnt.distance2(bzc.controlPointAt(0)) > dTol.squared()) != true) {
                    if (i < 2) {
                        paramPair[0] = param;
                        paramPair[1] = 0.0;
                    } else {
                        paramPair[0] = 0.0;
                        paramPair[1] = param;
                    }

                    addAsSolution(bzs, paramPair, dTol, zpl);
                }

                if ((pnt.distance2(bzc.controlPointAt(bzc.degree())) > dTol.squared()) != true) {
                    if (i < 2) {
                        paramPair[0] = param;
                        paramPair[1] = 1.0;
                    } else {
                        paramPair[0] = 1.0;
                        paramPair[1] = param;
                    }

                    addAsSolution(bzs, paramPair, dTol, zpl);
                }

                continue;
            }

            for (int j = 0; j < feet.length; j++) {
                if (i < 2) {
                    paramPair[0] = param;
                    paramPair[1] = feet[j].parameter();
                } else {
                    paramPair[0] = feet[j].parameter();
                    paramPair[1] = param;
                }

                if (pnt.distance2(feet[j].coordinates()) > dTol.squared()) {
                    footVector = feet[j].coordinates().subtract(pnt).unitized();
                    tangents = bzs.tangentVector(paramPair[0], paramPair[1]);

                    if ((footVector.dotProduct(tangents[0].unitized()) > aTol.value()) ||
                            (footVector.dotProduct(tangents[1].unitized()) > aTol.value())) {
                        continue;
                    }
                }

                addAsSolution(bzs, paramPair, dTol, zpl);
            }
        }
    }

    /**
     * 3D
     * �_����x�W�G�Ȗʂւ�?�?�̑���?�߂�N���X
     *
     * @param pnt �_
     * @param bzs �x�W�G�Ȗ�
     * @return ?�?�̑��̔z��
     * @see PointOnSurface3D
     */
    static PointOnSurface3D[] projection(Point3D pnt, PureBezierSurface3D bzs) {
        ToleranceForDistance dTol = ConditionOfOperation.getCondition()
                .getToleranceForDistanceAsObject();
        ToleranceForAngle aTol = ConditionOfOperation.getCondition()
                .getToleranceForAngleAsObject();

        Vector zeroPointList = new Vector();

        PureBezierSurfaceWithGivenControlPointsArray2D eBmdp = makeBezierSurface2D(pnt,
                bzs);
        getZeroPoints(bzs, eBmdp, 0.0, 1.0, 0.0, 1.0, dTol, zeroPointList);

        computeWithBoundary(pnt, bzs, dTol, aTol, zeroPointList);

        PointOnSurface3D[] result = new PointOnSurface3D[zeroPointList.size()];
        zeroPointList.copyInto(result);

        return result;
    }

    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     *
     * @param argv DOCUMENT ME!
     */
    public static void main(String[] argv) {
        Point3D[][] controlPoints = new Point3D[4][4];

        controlPoints[0][0] = new CartesianPoint3D(-906.190856934,
                1146.898925781, 2000.000000000);

        controlPoints[1][0] = new CartesianPoint3D(46.812759399,
                1270.228881836, 1000.000000000);

        controlPoints[2][0] = new CartesianPoint3D(943.757324219,
                1281.440673828, 1000.000000000);

        controlPoints[3][0] = new CartesianPoint3D(1594.042114258,
                1102.051757813, 2000.000000000);

        controlPoints[0][1] = new CartesianPoint3D(-794.072753906,
                -277.000549316, 0.000000000);

        controlPoints[1][1] = new CartesianPoint3D(259.837097168,
                -108.823440552, 0.000000000);

        controlPoints[2][1] = new CartesianPoint3D(1022.239990234,
                -176.094284058, 0.000000000);

        controlPoints[3][1] = new CartesianPoint3D(1560.406738281,
                -512.448486328, 0.000000000);

        controlPoints[0][2] = new CartesianPoint3D(-558.624816895,
                -1644.841064453, 0.000000000);

        controlPoints[1][2] = new CartesianPoint3D(428.014221191,
                -1330.910400391, 0.000000000);

        controlPoints[2][2] = new CartesianPoint3D(1246.476074219,
                -1476.663940430, 0.000000000);

        controlPoints[3][2] = new CartesianPoint3D(1829.490112305,
                -2037.254272461, 0.000000000);

        controlPoints[0][3] = new CartesianPoint3D(-502.565795898,
                -2833.292480469, 1000.000000000);

        controlPoints[1][3] = new CartesianPoint3D(349.531555176,
                -2519.362060547, 0.000000000);

        controlPoints[2][3] = new CartesianPoint3D(1033.451782227,
                -2597.844726563, 500.000000000);

        controlPoints[3][3] = new CartesianPoint3D(1268.899780273,
                -3068.740478516, 1000.00000000);

        Point3D pnt = new CartesianPoint3D(574.673950195, -1225.472839355,
                3300.000000000);
        PureBezierSurface3D bzs = new PureBezierSurface3D(controlPoints);
        PointOnSurface3D[] feet;

        //try
        //{
        feet = bzs.projectFrom(pnt);

        //}
        //catch (IndefiniteSolutionException e)
        //{
        //    feet = new PointOnSurface3D[1];
        //    feet[0] = (PointOnSurface3D)e.suitable();
        //}
        for (int i = 0; i < feet.length; i++) {
            Point3D coord = feet[i].coordinates();
            System.out.println("" + coord.x() + ", " + coord.y() + ", " +
                    coord.z() + " (" + feet[i].uParameter() + ", " +
                    feet[i].vParameter() + ")");
        }

        /*
         * solutions by GHL
         *
         * 1110.039439435705845, -1873.073499380423300, 275.300229335716381
         *        (0.716023034576210, 0.745925374008948)
         *
         * 18.232798954902169, -1728.225976249814948, 243.533115228460275
         *        (0.221145955409156, 0.737372916351887)
         *
         * 237.995939897379287, 72.902444530493824, 497.372987478862456
         *        (0.367881981641403, 0.279059129883535)
         */
    }

    /**
     * �P�����̃x�W�G�Ȗ�
     */
    private class BezierSurface1D {
        /**
         * ?���_
         */
        double[][] controlPoints;

        /**
         * ?d��
         */
        double[][] weights;

        /**
         * �I�u�W�F�N�g��?\�z����
         */
        BezierSurface1D() {
        }

        /**
         * U����?���_��?���Ԃ�
         *
         * @return U����?���_��?�
         */
        int uNControlPoints() {
            return controlPoints.length;
        }

        /**
         * V����?���_��?���Ԃ�
         *
         * @return V����?���_��?�
         */
        int vNControlPoints() {
            return controlPoints[0].length;
        }

        /**
         * U���̎�?���Ԃ�
         *
         * @return U���̎�?�
         */
        int uDegree() {
            return uNControlPoints() - 1;
        }

        /**
         * V���̎�?���Ԃ�
         *
         * @return V���̎�?�
         */
        int vDegree() {
            return vNControlPoints() - 1;
        }

        /**
         * (i, j)�Ԃ߂�?���_��Ԃ�
         *
         * @param i U���̃C���f�b�N�X(i�Ԃ�)
         * @param j V���̃C���f�b�N�X(j�Ԃ�)
         * @return ?���_
         */
        double controlPointAt(int i, int j) {
            return controlPoints[i][j];
        }
    }
}

// end of file
