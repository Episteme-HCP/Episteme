/*
 * 2������?���_��?�B����R�Ȗʂ�\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FreeformSurfaceWithGivenControlPointsArray2D.java,v 1.3 2006/03/01 21:15:58 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * 2������?���_��?�B����R�Ȗʂ�\����?ۃN���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:58 $
 */
abstract class FreeformSurfaceWithGivenControlPointsArray2D extends ParametricSurface2D {
    /**
     * ?���_��?d�݂�\��3�����z��
     * <p/>
     * ?Ō�̎����� 3 �ł���ΗL�?��?A
     * [][][0], [][][1] ��?����͓���?W�l�ŗ^�������̂Ƃ���
     * </p>
     *
     * @serial
     */
    protected double[][][] controlPointsArray;

    /**
     * �L�?���ۂ�
     *
     * @serial
     */
    boolean isRational;

    /**
     * ?���_(��?d��)��3�����z��Ƃ��ė^���đ�?���/�L�?�Ȗʂ�?\�z����
     * <p/>
     * �^������z��̓x�W�G�Ȗʂ�?���_�̔z��Ƃ���?�?������Ă����̂Ƃ���
     * </p>
     *
     * @param cpArray ?���_?A?d�݂�\���z��
     */
    protected FreeformSurfaceWithGivenControlPointsArray2D(double[][][] cpArray) {
        super();
        controlPointsArray = cpArray;
        isRational = (controlPointsArray[0][0].length == 3) ? true : false;
    }

    /**
     * ?���_��?�����i�[���� double �̎O�����z��̗̈��l������
     *
     * @param isPoly ��?����`�����ǂ���?
     * @param uSize  �z��̑�ꎟ���̗v�f?�
     * @param vSize  �z��̑�񎟌��̗v�f?�
     * @return ?���_��?�����i�[����z��
     */
    static protected double[][][] allocateDoubleArray(boolean isPoly,
                                                      int uSize,
                                                      int vSize) {
        return new double[uSize][vSize][(isPoly) ? 2 : 3];
    }

    /*
    * ?���_��double��3�����z��ɃZ�b�g����
    *
    * @param isPoly	��?����`�����ǂ���?
    * @return		?���_��\���z��
    */
    protected double[][][] toDoubleArray() {
        return controlPointsArray;
    }

    /**
     * (i, j)�Ԃ߂�?���_�� X ?�����Ԃ�
     *
     * @param i U���̃C���f�b�N�X(i�Ԃ�)
     * @param j V���̃C���f�b�N�X(j�Ԃ�)
     * @return ?���_�� X ?���
     */
    public double xAt(int i, int j) {
        if (isRational != true)
            return controlPointsArray[i][j][0];
        else
            return controlPointsArray[i][j][0] / controlPointsArray[i][j][2];
    }

    /**
     * (i, j)�Ԃ߂�?���_�� Y ?�����Ԃ�
     *
     * @param i U���̃C���f�b�N�X(i�Ԃ�)
     * @param j V���̃C���f�b�N�X(j�Ԃ�)
     * @return ?���_
     */
    public double yAt(int i, int j) {
        if (isRational != true)
            return controlPointsArray[i][j][1];
        else
            return controlPointsArray[i][j][1] / controlPointsArray[i][j][2];
    }

    /**
     * (i, j)�Ԃ߂�?d�݂�Ԃ�
     * ��?�����?��?�?���?AInvalidArgumentValueException�𓊂���(?)
     *
     * @param i U���̃C���f�b�N�X(i�Ԃ�)
     * @param j V���̃C���f�b�N�X(j�Ԃ�)
     * @return ?d��
     */
    public double weightAt(int i, int j) {
        if (isRational != true)
            throw new InvalidArgumentValueException();
        else
            return controlPointsArray[i][j][2];
    }

    /**
     * U����?���_��?���Ԃ�
     *
     * @return U����?���_��?�
     */
    public int uNControlPoints() {
        return controlPointsArray.length;
    }

    /**
     * V����?���_��?���Ԃ�
     *
     * @return V����?���_��?�
     */
    public int vNControlPoints() {
        return controlPointsArray[0].length;
    }

    /**
     * ?���_��?���Ԃ�
     *
     * @return ?���_��?�
     */
    public int nControlPoints() {
        return uNControlPoints() * vNControlPoints();
    }

    /**
     * �L�?�`�����ۂ���Ԃ�
     *
     * @return �L�?�`���Ȃ�true
     */
    public boolean isRational() {
        return isRational;
    }

    /**
     * ��?����`�����ۂ���Ԃ�
     *
     * @return ��?����`���Ȃ�true
     */
    public boolean isPolynomial() {
        return !isRational;
    }

    /**
     * �L�?�Ȗ�?�̓_��?Čv�Z����
     *
     * @param d0 ��?�?�̓_
     */
    protected void convRational0Deriv(double[] d0) {
        for (int i = 0; i < 2; i++)
            d0[i] /= d0[2];
    }

    /**
     * �L�?�Ȗʂ̈ꎟ�Γ���?���?Čv�Z����
     *
     * @param d0D �Ȗ�?�̓_
     * @param du  U���̈ꎟ�Γ���?�
     * @param dv  V���̈ꎟ�Γ���?�
     */
    protected void convRational1Deriv(double[] d0, double[] du, double[] dv) {
        convRational0Deriv(d0);
        for (int i = 0; i < 2; i++) {
            du[i] = (du[i] - (du[2] * d0[i])) / d0[2];
            dv[i] = (dv[i] - (dv[2] * d0[i])) / d0[2];
        }
    }

    /**
     * �L�?�Ȗʂ̓񎟕Γ���?�?A�ꎟ?�?��Γ���?���?Čv�Z����
     *
     * @param d0D �Ȗ�?�̓_
     * @param du  U���̈ꎟ�Γ���?�
     * @param dv  V���̈ꎟ�Γ���?�
     * @param duv U���̓񎟕Γ���?�
     * @param duv UV���̈ꎟ?�?��Γ���?�
     * @param dvv V���̓񎟕Γ���?�
     */
    protected void convRational2Deriv(double[] d0, double[] du, double[] dv,
                                      double[] duu, double[] duv, double[] dvv) {
        convRational1Deriv(d0, du, dv);
        for (int i = 0; i < 2; i++) {
            duu[i] = (duu[i] - (duu[2] * d0[i]) - (2.0 * du[2] * du[i])) / d0[2];
            duv[i] = (duv[i] - (duv[2] * d0[i]) - (du[2] * dv[i]) - (dv[2] * du[i])) / d0[2];
            dvv[i] = (dvv[i] - (dvv[2] * d0[i]) - (2.0 * dv[2] * dv[i])) / d0[2];
        }
    }

    /**
     * �����悻�̑�?ݔ͈͂���`��?�߂�
     *
     * @return �����悻�̑�?ݔ͈͂���`
     * @see EnclosingBox2D
     */
    EnclosingBox2D approximateEnclosingBox() {
        double min_crd_x;
        double min_crd_y;
        double max_crd_x;
        double max_crd_y;
        int uN = uNControlPoints();
        int vN = vNControlPoints();
        double x, y;
        int i, j;

        max_crd_x = min_crd_x = xAt(0, 0);
        max_crd_y = min_crd_y = yAt(0, 0);

        for (i = 1; i < uN; i++) {
            x = xAt(i, 0);
            y = yAt(i, 0);
             /**/if (x < min_crd_x)
            min_crd_x = x;
        else if (x > max_crd_x) max_crd_x = x;
             /**/if (y < min_crd_y)
            min_crd_y = y;
        else if (y > max_crd_y) max_crd_y = y;
        }
        for (j = 1; j < vN; j++) {
            for (i = 0; i < uN; i++) {
                x = xAt(i, j);
                y = yAt(i, j);
                 /**/if (x < min_crd_x)
                min_crd_x = x;
            else if (x > max_crd_x) max_crd_x = x;
                 /**/if (y < min_crd_y)
                min_crd_y = y;
            else if (y > max_crd_y) max_crd_y = y;
            }
        }
        return new EnclosingBox2D(min_crd_x, min_crd_y,
                max_crd_x, max_crd_y);
    }

    /**
     * ���R�`?󂩔ۂ���Ԃ�
     */
    public boolean isFreeform() {
        return true;
    }
}
