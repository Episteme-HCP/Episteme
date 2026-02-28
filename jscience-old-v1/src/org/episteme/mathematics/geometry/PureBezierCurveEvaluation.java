/*
 * Bezier��?�̕]����\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PureBezierCurveEvaluation.java,v 1.2 2006/03/01 21:16:09 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * Bezier��?�̕]����\���N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:09 $
 */

class PureBezierCurveEvaluation {
    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�
     */
    private PureBezierCurveEvaluation() {
    }

    /**
     * ?W�l��?�߂�
     *
     * @param controlPoints ?���_
     * @param param         �p���??[�^
     * @return ?W�l
     */
    static double[] coordinates(double[][] controlPoints,
                                double param) {
        double[] d0D = new double[controlPoints[0].length];

        evaluation(controlPoints, param, d0D, null, null, null);
        return d0D;
    }

    /**
     * ����?���?�߂�
     *
     * @param controlPoints ?���_
     * @param param         �p���??[�^
     * @param d0D           Out: ��?�?�̓_
     * @param d1D           Out: �ꎟ����?�
     * @param d2D           Out: �񎟓���?�
     * @param d3D           Out: �O������?�
     */
    static void evaluation(double[][] controlPoints,
                           double param,
                           double[] d0D,
                           double[] d1D,
                           double[] d2D,
                           double[] d3D) {
        double param1;
        int uicp;
        int degree;
        int dimension;
        double[][] auxpnts, auxpnts0;
        double x, y;
        int i, j, k;

        param1 = 1.0 - param;
        uicp = controlPoints.length;
        degree = uicp - 1;
        dimension = controlPoints[0].length;

        auxpnts0 = new double[uicp][dimension];
        auxpnts = controlPoints;

        for (i = 1; i < uicp; i++) {
            if (i == (uicp - 3) && d3D != null)    /* 3rd derivative */
                for (j = 0; j < dimension; j++)
                    d3D[j] = degree * (degree - 1) * (degree - 2)
                            * (auxpnts[i + 2][j] - 3.0 * (auxpnts[i + 1][j] - auxpnts[i][j]) - auxpnts[i - 1][j]);

            if (i == (uicp - 2) && d2D != null)    /* 2nd derivative */
                for (j = 0; j < dimension; j++)
                    d2D[j] = degree * (degree - 1)
                            * (auxpnts[i + 1][j] - 2.0 * auxpnts[i][j] + auxpnts[i - 1][j]);

            if (i == (uicp - 1) && d1D != null)    /* 1st derivative */
                for (j = 0; j < dimension; j++)
                    d1D[j] = degree * (auxpnts[i][j] - auxpnts[i - 1][j]);

            for (j = uicp - 1; j >= i; j--)
                for (k = 0; k < dimension; k++)
                    auxpnts0[j][k] = param * auxpnts[j][k] + param1 * auxpnts[j - 1][k];
            auxpnts = auxpnts0;
        }

        if (d0D != null)
            for (i = 0; i < dimension; i++)
                d0D[i] = auxpnts[uicp - 1][i];

        if (d2D != null && degree < 2)
            for (i = 0; i < dimension; i++)
                d2D[i] = 0.0;

        if (d3D != null && degree < 3)
            for (i = 0; i < dimension; i++)
                d3D[i] = 0.0;
    }

    /**
     * �u�?�b�T�~���O
     *
     * @param controlPoints ?���_�̔z��
     * @param parameters    �p���??[�^�̔z�� (�v�f?�?FcontrolPoints.length - 1)
     * @return �u�?�b�T�~���O�̌���
     */
    static double[] blossoming(double[][] controlPoints,
                               double[] parameters) {
        int uicp = controlPoints.length;
        int dimension = controlPoints[0].length;
        double[][] auxpnts0 = new double[uicp][dimension];
        double[][] auxpnts = controlPoints;

        for (int ti = 0; ti < (uicp - 1); ti++) {
            double rearWeight = parameters[ti];
            double frontWeight = 1.0 - rearWeight;
            for (int pi = uicp - 1; pi > ti; pi--)
                for (int di = 0; di < dimension; di++)
                    auxpnts0[pi][di] = rearWeight * auxpnts[pi][di] +
                            frontWeight * auxpnts[pi - 1][di];
            auxpnts = auxpnts0;
        }

        double[] result = new double[dimension];
        for (int di = 0; di < dimension; di++)
            result[di] = auxpnts0[uicp - 1][di];
        return result;
    }

    /**
     * �^����ꂽ�p���??[�^��2��������
     */
    static double[][][] divide(double[][] controlPoints,
                               double param) {
        double[][][] cpArray =
                new double[2][controlPoints.length][controlPoints[0].length];
        return divide(controlPoints, param, cpArray);
    }

    /**
     * �^����ꂽ�p���??[�^��2��������
     */
    static double[][][] divide(double[][] controlPoints,
                               double param,
                               double[][][] cpArray) {
        int uicp = controlPoints.length;
        int lp = uicp - 1;
        int dimension = controlPoints[0].length;
        double t1 = param;
        double t2 = 1.0 - t1;
        int i, j, k, l;

        switch (dimension) {
            // �{���I�ɂ�?A���� switch ���͕K�v�Ȃ�?A��ԉ��� default ��?��?��
            // �����?\���Ȃ�ł��邪?Adimension ��?[�v���� for �����̂�̂�
            // ��?s������ɒx�� (?��?�S�̂̔����ʂ�����) �̂�?A�����?Ȃ����߂�
            // switch ���ɂ��Ă���
            case 2:
                for (i = 0; i < uicp; i++) {
                    if (controlPoints[i].length != dimension) {
                        throw new InvalidArgumentValueException();
                    }
                    cpArray[0][i][0] = controlPoints[i][0];
                    cpArray[0][i][1] = controlPoints[i][1];
                }

                for (i = 0, k = lp; i < uicp; i++, k--) {
                    cpArray[1][k][0] = cpArray[0][lp][0];
                    cpArray[1][k][1] = cpArray[0][lp][1];

                    for (j = lp; j > i; j--) {
                        cpArray[0][j][0] = t1 * cpArray[0][j][0] + t2 * cpArray[0][j - 1][0];
                        cpArray[0][j][1] = t1 * cpArray[0][j][1] + t2 * cpArray[0][j - 1][1];
                    }
                }
                break;

            case 3:
                for (i = 0; i < uicp; i++) {
                    if (controlPoints[i].length != dimension) {
                        throw new InvalidArgumentValueException();
                    }
                    cpArray[0][i][0] = controlPoints[i][0];
                    cpArray[0][i][1] = controlPoints[i][1];
                    cpArray[0][i][2] = controlPoints[i][2];
                }

                for (i = 0, k = lp; i < uicp; i++, k--) {
                    cpArray[1][k][0] = cpArray[0][lp][0];
                    cpArray[1][k][1] = cpArray[0][lp][1];
                    cpArray[1][k][2] = cpArray[0][lp][2];

                    for (j = lp; j > i; j--) {
                        cpArray[0][j][0] = t1 * cpArray[0][j][0] + t2 * cpArray[0][j - 1][0];
                        cpArray[0][j][1] = t1 * cpArray[0][j][1] + t2 * cpArray[0][j - 1][1];
                        cpArray[0][j][2] = t1 * cpArray[0][j][2] + t2 * cpArray[0][j - 1][2];
                    }
                }
                break;

            case 4:
                for (i = 0; i < uicp; i++) {
                    if (controlPoints[i].length != dimension) {
                        throw new InvalidArgumentValueException();
                    }
                    cpArray[0][i][0] = controlPoints[i][0];
                    cpArray[0][i][1] = controlPoints[i][1];
                    cpArray[0][i][2] = controlPoints[i][2];
                    cpArray[0][i][3] = controlPoints[i][3];
                }

                for (i = 0, k = lp; i < uicp; i++, k--) {
                    cpArray[1][k][0] = cpArray[0][lp][0];
                    cpArray[1][k][1] = cpArray[0][lp][1];
                    cpArray[1][k][2] = cpArray[0][lp][2];
                    cpArray[1][k][3] = cpArray[0][lp][3];

                    for (j = lp; j > i; j--) {
                        cpArray[0][j][0] = t1 * cpArray[0][j][0] + t2 * cpArray[0][j - 1][0];
                        cpArray[0][j][1] = t1 * cpArray[0][j][1] + t2 * cpArray[0][j - 1][1];
                        cpArray[0][j][2] = t1 * cpArray[0][j][2] + t2 * cpArray[0][j - 1][2];
                        cpArray[0][j][3] = t1 * cpArray[0][j][3] + t2 * cpArray[0][j - 1][3];
                    }
                }
                break;

            default:
                for (i = 0; i < uicp; i++) {
                    if (controlPoints[i].length != dimension) {
                        throw new InvalidArgumentValueException();
                    }
                    for (j = 0; j < dimension; j++)
                        cpArray[0][i][j] = controlPoints[i][j];
                }

                for (i = 0, k = lp; i < uicp; i++, k--) {
                    for (j = 0; j < dimension; j++)
                        cpArray[1][k][j] = cpArray[0][lp][j];

                    for (j = lp; j > i; j--)
                        for (l = 0; l < dimension; l++)
                            cpArray[0][j][l] = t1 * cpArray[0][j][l] + t2 * cpArray[0][j - 1][l];
                }
                break;
        }

        return cpArray;
    }

    /**
     * ��?�����?グ��
     * <p/>
     * �?��: <br>
     * uicp ��?A��?���?グ��O��?���_��?���\�� <br>
     * controlPoints �̑�ꎟ���̗v�f?���?A(uicp + 1) ��?�ł��邱�� <br>
     * controlPoints[i] (0 <= i < uicp) �ɂ�?A��?���?グ��O��?���_��?��?ݒ肳��Ă��邱��
     * </p>
     *
     * @param uicp          ��?���?グ��O��?���_��?� (���)
     * @param controlPoints ?���_�̔z�� (��?o��)
     */
    static void elevateOneDegree(int uicp,
                                 double[][] controlPoints) {
        int dimension = controlPoints[0].length;

        for (int j = 0; j < dimension; j++)
            controlPoints[uicp][j] = controlPoints[uicp - 1][j];

        for (int i = uicp - 1; i >= 1; i--) {
            double tF = i / (double) uicp;    // �܂��̓_�ɑ΂���?d��
            double tR = 1.0 - tF;        // ���̓_�ɑ΂���?d��
            for (int j = 0; j < dimension; j++)
                controlPoints[i][j] = tF * controlPoints[i - 1][j] +
                        tR * controlPoints[i][j];
        }
    }
}
