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

import org.jscience.util.FatalException;

import java.io.PrintWriter;


/**
 * 2������Bezier�Ȗʂ�\���N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.5 $, $Date: 2006/03/28 21:47:45 $
 */
class PureBezierSurfaceWithGivenControlPointsArray2D
    extends FreeformSurfaceWithGivenControlPointsArray2D {
/**
     * ?���_(��?d��)��3�����z��Ƃ��ė^���đ�?���/�L�?Bezier�Ȗʂ�?\�z����
     *
     * @param cpArray ?���_?A?d�݂�\���z��
     */
    PureBezierSurfaceWithGivenControlPointsArray2D(double[][][] cpArray) {
        super(cpArray);
    }

    /**
     * U���̎�?���Ԃ�
     *
     * @return U���̎�?�
     */
    public int uDegree() {
        return uNControlPoints() - 1;
    }

    /**
     * V���̎�?���Ԃ�
     *
     * @return V���̎�?�
     */
    public int vDegree() {
        return vNControlPoints() - 1;
    }

    /**
     * �^����ꂽ�p���??[�^�ł�?W�l��?�߂�
     *
     * @param uParam U���̃p���??[�^�l
     * @param vParam V���̃p���??[�^�l
     *
     * @return ?W�l
     *
     * @see Point2D
     */
    public Point2D coordinates(double uParam, double vParam) {
        double[][][] cntlPnts;
        int uUicp;
        int vUicp;
        double[][] bzc;
        double[] d0D;
        boolean isPoly = isPolynomial();

        uParam = checkUParameter(uParam);
        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray();
        uUicp = cntlPnts.length;
        vUicp = cntlPnts[0].length;
        bzc = new double[uUicp][];

        /*
         * map for V-direction
         */
        for (int i = 0; i < uUicp; i++) {
            bzc[i] = PureBezierCurveEvaluation.coordinates(cntlPnts[i], vParam);
        }

        /*
         * map for U-direction
         */
        d0D = PureBezierCurveEvaluation.coordinates(bzc, uParam);

        if (!isPoly) {
            convRational0Deriv(d0D);
        }

        return new CartesianPoint2D(d0D);
    }

    /**
     * �^����ꂽ�p���??[�^�ł�?ڃx�N�g����?�߂�
     *
     * @param uParam U���̃p���??[�^
     * @param vParam V���̃p���??[�^
     *
     * @return ?ڃx�N�g��[0]:U����?ڃx�N�g��
     *         [1]:V����?ڃx�N�g��
     *
     * @see Vector2D
     */
    public Vector2D[] tangentVector(double uParam, double vParam) {
        double[][][] cntlPnts;
        int uUicp;
        int vUicp;
        double[][] pp;
        double[][] dd;
        double[][] tt;
        double[][] ld1D = new double[2][];
        Vector2D[] d1D = new Vector2D[2];
        boolean isPoly = isPolynomial();

        uParam = checkUParameter(uParam);
        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray();
        uUicp = cntlPnts.length;
        vUicp = cntlPnts[0].length;
        pp = new double[uUicp][3];
        tt = new double[uUicp][3];

        /*
         * map for V-direction
         */
        for (int i = 0; i < uUicp; i++) {
            PureBezierCurveEvaluation.evaluation(cntlPnts[i], vParam, pp[i],
                tt[i], null, null);
        }

        /*
         * map for U-direction
         */
        ld1D[0] = new double[3];

        if (isPoly) {
            PureBezierCurveEvaluation.evaluation(pp, uParam, null, ld1D[0],
                null, null);
            ld1D[1] = PureBezierCurveEvaluation.coordinates(tt, uParam);
        } else {
            double[] ld0D = new double[3];
            PureBezierCurveEvaluation.evaluation(pp, uParam, ld0D, ld1D[0],
                null, null);
            ld1D[1] = PureBezierCurveEvaluation.coordinates(tt, uParam);
            convRational1Deriv(ld0D, ld1D[0], ld1D[1]);
        }

        for (int i = 0; i < 2; i++) {
            d1D[i] = new LiteralVector2D(ld1D[i]);
        }

        return d1D;
    }

    /**
     * ����?��?�̒��ԃf?[�^�̗̈��l������
     *
     * @return ����?��?�̒��ԃf?[�^�̗̈�
     */
    double[][][][] allocateIntermediateDoubleArrayForDividing() {
        boolean isPoly = isPolynomial();
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        double[][][][] bzss_array = new double[2][][][];

        for (int i = 0; i < 2; i++)
            bzss_array[i] = allocateDoubleArray(isPoly, uUicp, vUicp);

        return bzss_array;
    }

    /**
     * �^����ꂽU���̃p���??[�^��2��������
     *
     * @param uParam U���̃p���??[�^�l
     *
     * @return �������ꂽBezier�Ȗ�
     */
    public PureBezierSurfaceWithGivenControlPointsArray2D[] uDivide(
        double uParam) {
        return uDivide(uParam, allocateIntermediateDoubleArrayForDividing());
    }

    /**
     * �^����ꂽU���̃p���??[�^��2��������
     *
     * @param uParam U���̃p���??[�^�l
     * @param bzssArray ����?��?�̒��ԃf?[�^�̗̈�
     *
     * @return �������ꂽBezier�Ȗ�
     *
     * @throws FatalException
     *         �p���??[�^����`��͈̔͊O�ł���
     */
    PureBezierSurfaceWithGivenControlPointsArray2D[] uDivide(double uParam,
        double[][][][] bzssArray) {
        double[][][] cntlPnts;
        double[][] bzc;
        double[][][] bzcsArray;
        PureBezierSurfaceWithGivenControlPointsArray2D[] bzss;
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        int i;
        int j;
        int k;

        uParam = checkUParameter(uParam);
        cntlPnts = toDoubleArray();
        bzc = new double[uUicp][];
        bzcsArray = new double[2][uUicp][];

        for (i = 0; i < vUicp; i++) {
            for (j = 0; j < uUicp; j++) {
                bzc[j] = cntlPnts[j][i];
                bzcsArray[0][j] = bzssArray[0][j][i];
                bzcsArray[1][j] = bzssArray[1][j][i];
            }

            try {
                PureBezierCurveEvaluation.divide(bzc, uParam, bzcsArray);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        bzss = new PureBezierSurfaceWithGivenControlPointsArray2D[2];

        for (i = 0; i < 2; i++) {
            try {
                bzss[i] = new PureBezierSurfaceWithGivenControlPointsArray2D(bzssArray[i]);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        return bzss;
    }

    /**
     * �^����ꂽV���̃p���??[�^��2��������
     *
     * @param vParam V���̃p���??[�^�l
     *
     * @return �������ꂽBezier�Ȗ�
     */
    public PureBezierSurfaceWithGivenControlPointsArray2D[] vDivide(
        double vParam) {
        return vDivide(vParam, allocateIntermediateDoubleArrayForDividing());
    }

    /**
     * �^����ꂽV���̃p���??[�^��2��������
     *
     * @param vParam V���̃p���??[�^�l
     * @param bzssArray ����?��?�̒��ԃf?[�^�̗̈�
     *
     * @return �������ꂽBezier�Ȗ�
     *
     * @throws FatalException
     *         �p���??[�^����`��͈̔͊O�ł���
     */
    PureBezierSurfaceWithGivenControlPointsArray2D[] vDivide(double vParam,
        double[][][][] bzssArray) {
        double[][][] cntlPnts;
        double[][][] bzcsArray;
        PureBezierSurfaceWithGivenControlPointsArray2D[] bzss;
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        int i;
        int j;

        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray();
        bzcsArray = new double[2][][];

        for (i = 0; i < uUicp; i++) {
            bzcsArray[0] = bzssArray[0][i];
            bzcsArray[1] = bzssArray[1][i];

            try {
                PureBezierCurveEvaluation.divide(cntlPnts[i], vParam, bzcsArray);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        bzss = new PureBezierSurfaceWithGivenControlPointsArray2D[2];

        for (i = 0; i < 2; i++) {
            try {
                bzss[i] = new PureBezierSurfaceWithGivenControlPointsArray2D(bzssArray[i]);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        return bzss;
    }

    /**
     * U���̃p���??[�^�h�?�C���𒲂ׂ�
     *
     * @return U���̃p���??[�^�h�?�C����Ԃ�
     *
     * @throws FatalException DOCUMENT ME!
     */
    ParameterDomain getUParameterDomain() {
        try {
            return new ParameterDomain(false, 0.0, 1.0);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * V���̃p���??[�^�h�?�C���𒲂ׂ�
     *
     * @return V���̃p���??[�^�h�?�C����Ԃ�
     *
     * @throws FatalException DOCUMENT ME!
     */
    ParameterDomain getVParameterDomain() {
        try {
            return new ParameterDomain(false, 0.0, 1.0);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /*
     * U���̃p���??[�^���L��ǂ������ׂ�
     *
     * @return                �ۂ߂�ꂽ�p���??[�^
     * @exception ParameterOutOfRange �p���??[�^����`��͈̔͊O�ł���
     */
    private double checkUParameter(double param) {
        checkUValidity(param);

        return uParameterDomain().force(param);
    }

    /*
     * V���̃p���??[�^���L��ǂ������ׂ�
     *
     * @return                �ۂ߂�ꂽ�p���??[�^
     * @exception ParameterOutOfRange �p���??[�^����`��͈̔͊O�ł���
     */
    private double checkVParameter(double param) {
        checkVValidity(param);

        return vParameterDomain().force(param);
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o��
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     *
     * @see GeometryElement
     */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);

        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tuNControlPoints\t" + uNControlPoints());
        writer.println(indent_tab + "\tvNControlPoints\t" + vNControlPoints());

        writer.println(indent_tab + "\tcontrolPoints");

        double[][][] cntlPnts = toDoubleArray();

        for (int i = 0; i < cntlPnts.length; i++) {
            for (int j = 0; j < cntlPnts[i].length; j++) {
                writer.print(indent_tab + "\t");

                for (int k = 0; k < cntlPnts[i][j].length; k++) {
                    if (k == 0) {
                        writer.print("" + cntlPnts[i][j][k]);
                    } else {
                        writer.print(", " + cntlPnts[i][j][k]);
                    }
                }

                writer.println("");
            }
        }

        writer.println(indent_tab + "End");
    }
}
