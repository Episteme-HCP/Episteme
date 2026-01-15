/*
 * �R���� : ?���_��?�B����R�Ȗʂ�\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FreeformSurfaceWithControlPoints3D.java,v 1.3 2007-10-21 21:08:12 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.MathUtils;
import org.jscience.util.FatalException;

import java.util.Vector;

/**
 * �R���� : ?���_��?�B����R�Ȗʂ�\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ?���_ (Point3D) �̂Q�����z�� controlPoints
 * ��
 * ?d�� (double) �̂Q�����z�� weights
 * ��?��?B
 * </p>
 * <p/>
 * points ����� mesh ��?AU ���̃C���f�b�N�X��?�?AV ���̃C���f�b�N�X����?B
 * �܂�?AU ���� i �Ԗ�?AV ���� j �Ԗڂ�?���_��?���
 * points[i][j], weights[i][j] �Ɋi�[�����?B
 * </p>
 * <p/>
 * weights �� null ��?�?��ɂ͔�L�?�Ȗ� (��?����Ȗ�) ��\��?B
 * </p>
 * <p/>
 * weights �ɔz��?ݒ肳��Ă���?�?��ɂ͗L�?�Ȗʂ�\��?B
 * weights[i][j] �� controlPoints[i][j] �ɑΉ�����?B
 * �Ȃ�?A���܂̂Ƃ��� weights[i][j] �̒l��?��łȂ���΂Ȃ�Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:12 $
 */

public abstract class FreeformSurfaceWithControlPoints3D extends BoundedSurface3D {
    /**
     * ?���_�̂Q�����z��?B
     *
     * @serial
     */
    protected Point3D[][] controlPoints;

    /**
     * ?d�݂̂Q�����z��?B
     * <p/>
     * ��?����Ȗʂł���� null �Ƃ���?B
     * </p>
     *
     * @serial
     */
    protected double[][] weights;

    /**
     * ?���_��?d�݂�\���R�����z��?B
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * </p>
     * <p/>
     * controlPointsArray[i][j] �̒����� 3 ��?�?�?A
     * controlPointsArray[i][j][0] �� (i, j) �Ԗڂ�?���_�� X ?���?A
     * controlPointsArray[i][j][1] �� (i, j) �Ԗڂ�?���_�� Y ?���
     * controlPointsArray[i][j][2] �� (i, j) �Ԗڂ�?���_�� Z ?���
     * ��\��?B
     * </p>
     * <p/>
     * controlPointsArray[i][j] �̒����� 4 ��?�?�?A
     * controlPointsArray[i][j][0] �� ((i, j) �Ԗڂ�?���_�� X ?��� * (i, j) �Ԗڂ�?d��)?A
     * controlPointsArray[i][j][1] �� ((i, j) �Ԗڂ�?���_�� Y ?��� * (i, j) �Ԗڂ�?d��)?A
     * controlPointsArray[i][j][2] �� ((i, j) �Ԗڂ�?���_�� Z ?��� * (i, j) �Ԗڂ�?d��)?A
     * controlPointsArray[i][j][3] �� (i, j) �Ԗڂ�?d��
     * ��\��?B
     * </p>
     *
     * @serial
     */
    private double[][][] controlPointsArray = null;

    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     * <p/>
     * �e�t�B?[���h�ɂ͒l��?ݒ肵�Ȃ�?B
     * </p>
     */
    protected FreeformSurfaceWithControlPoints3D() {
        super();
    }

    /**
     * ?���_���^���đ�?����ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * <ul>
     * <li>	controlPoints �� null �ł���
     * <li>	controlPoints �̒����� 2 ���?�����
     * <li>	controlPoints[i] �̒����� 2 ���?�����
     * <li>	controlPoints[k] �� controlPoints[l] �̒������قȂ�
     * <li>	controlPoints �̂���v�f�̒l�� null �ł���
     * </ul>
     * </p>
     *
     * @param controlPoints ?���_�̂Q�����z��
     * @see InvalidArgumentValueException
     */
    protected FreeformSurfaceWithControlPoints3D(Point3D[][] controlPoints) {
        super();
        int[] npnts = setControlPoints(controlPoints);
        weights = null;
    }

    /**
     * ?���_���?d�ݗ��^���ėL�?�ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * <ul>
     * <li>	controlPoints �� null �ł���
     * <li>	controlPoints �̒����� 2 ���?�����
     * <li>	controlPoints[i] �̒����� 2 ���?�����
     * <li>	controlPoints[k] �� controlPoints[l] �̒������قȂ�
     * <li>	controlPoints �̂���v�f�̒l�� null �ł���
     * <li>	weights �� null �ł���
     * <li>	weights �̒����� controlPoints �̒����ƈقȂ�
     * <li>	weights[i] �̒����� controlPoints[i] �̒����ƈقȂ�
     * <li>	weights �̂���v�f�̒l��?��łȂ�
     * <li>	weights �̂���v�f�̒l w �ɂ���?A
     * (w / (weights ���?ő�l)) �� MachineEpsilon.DOUBLE ���?������Ȃ�?B
     * </ul>
     * </p>
     *
     * @param controlPoints ?���_�̂Q�����z��
     * @param weights       ?d�݂̂Q�����z��
     * @see InvalidArgumentValueException
     */
    protected FreeformSurfaceWithControlPoints3D(Point3D[][] controlPoints,
                                                 double[][] weights) {
        super();
        int[] npnts = setControlPoints(controlPoints);
        setWeights(npnts, weights);
    }

    /**
     * ?���_(��?d��)��3�����z��Ƃ��ė^���đ�?���/�L�?�Ȗʂ�?\�z����
     *
     * @param cpArray ?���_?A?d�݂�\���z��
     */
    protected FreeformSurfaceWithControlPoints3D(double[][][] cpArray) {
        this(cpArray, true);
    }

    /**
     * ?���_ (��?d��) ��O�����z��ŗ^����
     * ��?����Ȗ� (���邢�͗L�?�Ȗ�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * cpArray �̒����� U ����?���_��?�?A
     * cpArray[0] �̒����� V ����?���_��?��Ƃ���?B
     * �܂�?AcpArray[0][0] �̒����� 3 �ł���Α�?����Ȗ�?A4 �ł���ΗL�?�ȖʂƂ���?B
     * </p>
     * <p/>
     * cpArray[i][j] �̒����� 3 ��?�?�?A
     * cpArray[i][j][0] �� (i, j) �Ԗڂ�?���_�� X ?���?A
     * cpArray[i][j][1] �� (i, j) �Ԗڂ�?���_�� Y ?���?A
     * cpArray[i][j][2] �� (i, j) �Ԗڂ�?���_�� Z ?���
     * ����̂Ƃ���?B
     * </p>
     * <p/>
     * cpArray[i][j] �̒����� 4 ��?�?�?A
     * cpArray[i][j][0] �� ((i, j) �Ԗڂ�?���_�� X ?��� * (i, j) �Ԗڂ�?d��)?A
     * cpArray[i][j][1] �� ((i, j) �Ԗڂ�?���_�� Y ?��� * (i, j) �Ԗڂ�?d��)?A
     * cpArray[i][j][2] �� ((i, j) �Ԗڂ�?���_�� Z ?��� * (i, j) �Ԗڂ�?d��)?A
     * cpArray[i][j][3] �� (i, j) �Ԗڂ�?d��
     * ����̂Ƃ���?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?�?A�ȉ��̃`�F�b�N��?s�Ȃ�?B
     * </p>
     * <blockquote>
     * <p/>
     * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * <ul>
     * <li>	U ����?���_��?��� 2 ���?�����
     * <li>	V ����?���_��?��� 2 ���?�����
     * <li>	����?d�݂̒l��?��łȂ�
     * <li>	����?d�݂̒l w �ɂ���?A
     * (w / (?d�ݗ���?ő�l)) �� MachineEpsilon.DOUBLE ���?������Ȃ�?B
     * </ul>
     * </p>
     * </blockquote>
     *
     * @param cpArray ?���_ (�����?d��) �̔z��
     * @param doCheck ��?��̃`�F�b�N��?s�Ȃ����ǂ���
     * @see InvalidArgumentValueException
     */
    protected FreeformSurfaceWithControlPoints3D(double[][][] cpArray,
                                                 boolean doCheck) {
        super();

        int uNpnts = cpArray.length;
        int vNpnts = cpArray[0].length;
        int[] npnts = null;
        int dimension = cpArray[0][0].length;
        Point3D[][] cp = new Point3D[uNpnts][vNpnts];
        double[][] wt = null;
        boolean isPoly = (dimension == 3);
        int i, j, k;

        if (!isPoly) {    // �L�?
            double[] tmp = new double[4];
            wt = new double[uNpnts][vNpnts];
            for (i = 0; i < uNpnts; i++) {
                for (j = 0; j < vNpnts; j++) {
                    for (k = 0; k < 4; k++)
                        tmp[k] = cpArray[i][j][k];
                    convRational0Deriv(tmp);
                    cp[i][j] = new CartesianPoint3D(tmp[0], tmp[1], tmp[2]);
                    wt[i][j] = tmp[3];
                }
            }
        } else {
            for (i = 0; i < uNpnts; i++) {
                for (j = 0; j < vNpnts; j++) {
                    cp[i][j] = new CartesianPoint3D(cpArray[i][j][0],
                            cpArray[i][j][1],
                            cpArray[i][j][2]);
                }
            }
        }
        if (doCheck) {
            npnts = setControlPoints(cp);
        } else {
            this.controlPoints = cp;
        }
        if (isPoly) {
            this.weights = null;
        } else {
            if (doCheck) {
                setWeights(npnts, wt);
            } else {
                this.weights = wt;
            }
        }
    }

    /**
     * ?���_���?d�ݗ��^����
     * ��?����Ȗʂ��邢�͗L�?�ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� false ��?�?�?A
     * �^����ꂽ controlPoints ����� weights �̒l��
     * �Ή�����t�B?[���h�ɂ��̂܂�?ݒ肷��?B
     * ������ weights �� null �ł����?A�Ȗʂ͔�L�? (��?���) �`���ɂȂ�?B
     * �Ȃ�?AcontrolPoints �� null ���^�������?A�\��ł��Ȃ����ʂ�?���?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?�?A
     * weights ���l��?�Ă�
     * {@link #FreeformSurfaceWithControlPoints3D(Point3D[][],double[][])
     * FreeformSurfaceWithControlPoints3D(Point3D[][], double[][])}?A
     * weights �� null �ł����
     * {@link #FreeformSurfaceWithControlPoints3D(Point3D[][])
     * FreeformSurfaceWithControlPoints3D(Point3D[][])}
     * �Ɠ��l��?��?��?s�Ȃ�?B
     * </p>
     *
     * @param controlPoitns ?���_�̔z��
     * @param weights       ?d�݂̔z��
     * @param doCheck       ��?��̃`�F�b�N��?s�Ȃ����ǂ���
     */
    protected FreeformSurfaceWithControlPoints3D(Point3D[][] controlPoints,
                                                 double[][] weights,
                                                 boolean doCheck) {
        super();
        if (doCheck) {
            int[] npnts = setControlPoints(controlPoints);
            if (weights == null)
                weights = null;
            else
                setWeights(npnts, weights);
        } else {
            this.controlPoints = controlPoints;
            this.weights = weights;
        }
    }

    /**
     * ���̋Ȗʂ�?���_�̂Q�����z���Ԃ�?B
     *
     * @return ?���_�̂Q�����z��
     */
    public Point3D[][] controlPoints() {
        Point3D[][] copied = new Point3D[controlPoints.length][controlPoints[0].length];

        for (int i = 0; i < controlPoints.length; i++)
            for (int j = 0; j < controlPoints[0].length; j++)
                copied[i][j] = controlPoints[i][j];
        return copied;
    }

    /**
     * ���̋Ȗʂ� (i, j) �Ԗڂ�?���_��Ԃ�?B
     *
     * @param i U ���̃C���f�b�N�X (i �Ԗ�)
     * @param j V ���̃C���f�b�N�X (j �Ԗ�)
     * @return ?���_
     */
    public Point3D controlPointAt(int i, int j) {
        return controlPoints[i][j];
    }

    /**
     * ���̋Ȗʂ�?d�݂̂Q�����z���Ԃ�?B
     * <p/>
     * �Ȗʂ���?����Ȗʂ�?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return ?d�݂̂Q�����z��
     */
    public double[][] weights() {
        if (weights == null)
            return null;
        return (double[][]) weights.clone();
    }

    /**
     * ���̋Ȗʂ� (i, j) �Ԗڂ�?d�݂�Ԃ�?B
     * <p/>
     * �Ȗʂ���?����Ȗʂ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param i U ���̃C���f�b�N�X (i �Ԗ�)
     * @param j V ���̃C���f�b�N�X (j �Ԗ�)
     * @return ?d��
     * @see InvalidArgumentValueException
     */
    public double weightAt(int i, int j) {
        if (weights == null)
            throw new InvalidArgumentValueException();
        return weights[i][j];
    }

    /**
     * ���̋Ȗʂ� U ����?���_��?���Ԃ�?B
     *
     * @return U ����?���_��?�
     * @see #vNControlPoints()
     * @see #nControlPoints()
     */
    public int uNControlPoints() {
        return controlPoints.length;
    }

    /**
     * ���̋Ȗʂ� V ����?���_��?���Ԃ�?B
     *
     * @return V ����?���_��?�
     * @see #uNControlPoints()
     * @see #nControlPoints()
     */
    public int vNControlPoints() {
        return controlPoints[0].length;
    }

    /**
     * ���̋Ȗʂ�?���_�̑??���Ԃ�?B
     * <p/>
     * (U ����?���_��?� * V ����?���_��?�) ��Ԃ�?B
     * </p>
     *
     * @return ?���_�̑??�
     * @see #uNControlPoints()
     * @see #vNControlPoints()
     */
    public int nControlPoints() {
        return uNControlPoints() * vNControlPoints();
    }

    /**
     * ���̋Ȗʂ��L�?�`�����ۂ���Ԃ�?B
     *
     * @return �L�?�`���Ȃ�� true?A�����łȂ���� false
     */
    public boolean isRational() {
        return weights != null;
    }

    /**
     * ���̋Ȗʂ���?����`�����ۂ���Ԃ�?B
     *
     * @return ��?����`���Ȃ�� true?A�����łȂ���� false
     */
    public boolean isPolynomial() {
        return weights == null;
    }

    /**
     * ���̋Ȗʂ����ʌ`?�Ƃ݂Ȃ��邩�ǂ�����Ԃ���?ۃ?�\�b�h?B
     *
     * @param tol ���ʂƂ݂Ȃ������̋��e��?�
     * @return ���ʂƂ݂Ȃ���Ȃ�� true?A�����łȂ���� false
     */
    abstract boolean isPlaner(ToleranceForDistance tol);

    /**
     * ���̋Ȗʂ��^����ꂽ?��x�ɂ����ĕ��ʂƌ��Ȃ��Ȃ�?�?���?A
     * U/V ���Ƀp���??[�^���_�œ񕪊����钊?ۃ?�\�b�h?B
     * <p/>
     * ���ʂƂ��ē�����z�� S �̗v�f��?��� 4 �ł���?B
     * �e�v�f��?A���̋Ȗʂ𕪊������Ȗʂ̂��ꂼ���\��?B
     * <p/>
     * �^����ꂽ tol �ɂ�����?A�Ȗʂ𕪊�����K�v���Ȃ�?�?��ɂ�
     * S[i] (i = 0, ..., 3) �ɂ͂��ׂ� null �����?B
     * </p>
     * <p/>
     * �Ȗʂ� U/V ���Ƃ�ɓ񕪊�����?�?��ɂ�?A
     * S �̊e�v�f�͈ȉ��̋Ȗʂ�\��?B
     * <pre>
     * 		S[0] : U ���?AV ���ɂ�����Ȗ�
     * 		S[1] : U ���?㑤?AV ���ɂ�����Ȗ�
     * 		S[2] : U ���?AV ���?㑤�ɂ�����Ȗ�
     * 		S[3] : U ���?㑤?AV ���?㑤�ɂ�����Ȗ�
     * </pre>
     * </p>
     * <p/>
     * �Ȗʂ� U ���ɂ̂ݓ񕪊����� (V ���ɂ͕�������K�v���Ȃ�) ?�?��ɂ�?A
     * S �̊e�v�f�͈ȉ��̋Ȗʂ�\��?B
     * <pre>
     * 		S[0] : U ���ɂ�����Ȗ�
     * 		S[1] : U ���?㑤�ɂ�����Ȗ�
     * 		S[2] : null
     * 		S[3] : null
     * </pre>
     * </p>
     * <p/>
     * �Ȗʂ� V ���ɂ̂ݓ񕪊����� (U ���ɂ͕�������K�v���Ȃ�) ?�?��ɂ�?A
     * S �̊e�v�f�͈ȉ��̋Ȗʂ�\��?B
     * <pre>
     * 		S[0] : V ���ɂ�����Ȗ�
     * 		S[1] : null
     * 		S[2] : V ���?㑤�ɂ�����Ȗ�
     * 		S[3] : null
     * </pre>
     * </p>
     *
     * @param tol ���ʂƂ݂Ȃ������̋��e��?�
     * @return �������ꂽ�Ȗʂ̔z��
     */
    abstract FreeformSurfaceWithControlPoints3D[] divideForMesh(ToleranceForDistance tol);

    /**
     * ����Z�O�?���g�ɂ������?��I�ȃp���??[�^�l��\���Ք�N���X?B
     * <p/>
     * ��?��I�ȃp���??[�^�l�͕�?� (a/b) �ŕ\��?B
     * ���̕�?��̎�蓾��l�͈̔͂� [0, 1] �ł���?B
     * </p>
     */
    class MeshParam {
        /**
         * ����Z�O�?���g��
         * ?u(�p���??[�^�I��) ?k�ނ��Ă��Ȃ��L��ȃZ�O�?���g��?v�ɂ�����C���f�b�N�X?B
         */
        int sgidx;

        /**
         * �p���??[�^�l�̕��q : a/b �� a ?B
         */
        int numer;

        /**
         * �p���??[�^�l�̕��� : a/b �� b ?B
         */
        int denom;

        /**
         * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param sgidx �Z�O�?���g��?u�L��ȃZ�O�?���g��?v�ɂ�����C���f�b�N�X
         * @param numer �p���??[�^�l�̕��q
         * @param denom �p���??[�^�l�̕���
         */
        MeshParam(int sgidx, int numer, int denom) {
            super();
            this.sgidx = sgidx;
            this.numer = numer;
            this.denom = denom;
        }

        /**
         * ���̃p���??[�^�l�Ɨ^����ꂽ�p���??[�^�l������̒l��\�����ۂ���Ԃ�?B
         *
         * @return ����̒l��\���� true?A�����łȂ���� false
         */
        private boolean isSame(MeshParam mate) {
            if (this.sgidx != mate.sgidx) {
                /*
                * different segment
                *
                * (end param) vs. (start param of next non-reduced segment) ?
                */
                if ((this.sgidx == (mate.sgidx - 1)) &&
                        (this.numer == this.denom) && (mate.numer == 0))
                    return true;

                if ((mate.sgidx == (this.sgidx - 1)) &&
                        (mate.numer == mate.denom) && (this.numer == 0))
                    return true;

            } else {
                /*
                * same segment
                *
                * same parameter?
                */
                if (this.denom == mate.denom) {
                    if (this.numer == mate.numer)
                        return true;

                } else {
                    int cmn_denom = MathUtils.LCM(this.denom, mate.denom);
                    int a_numer = this.numer * (cmn_denom / this.denom);
                    int b_numer = mate.numer * (cmn_denom / mate.denom);

                    if (a_numer == b_numer)
                        return true;
                }
            }

            return false;
        }
    }

    /**
     * �p���??[�^�I�ɋ�`�ȋ�Ԃ�\���Ք�N���X?B
     */
    class SegInfo {
        /**
         * U ���̃p���??[�^��Ԃ̊J�n�l?B
         */
        MeshParam u_sp;

        /**
         * U ���̃p���??[�^��Ԃ�?I���l?B
         */
        MeshParam u_ep;

        /**
         * V ���̃p���??[�^��Ԃ̊J�n�l?B
         */
        MeshParam v_sp;

        /**
         * V ���̃p���??[�^��Ԃ�?I���l?B
         */
        MeshParam v_ep;

        /**
         * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param u_sp U ���̃p���??[�^��Ԃ̊J�n�l
         * @param u_ep U ���̃p���??[�^��Ԃ�?I���l
         * @param v_sp V ���̃p���??[�^��Ԃ̊J�n�l
         * @param v_ep V ���̃p���??[�^��Ԃ�?I���l
         */
        SegInfo(MeshParam u_sp, MeshParam u_ep, MeshParam v_sp, MeshParam v_ep) {
            super();
            this.u_sp = u_sp;
            this.u_ep = u_ep;
            this.v_sp = v_sp;
            this.v_ep = v_ep;
        }
    }

    /**
     * �p���??[�^�l�̃��X�g��\���Ք�N���X?B
     * <p/>
     * ���X�g�̊e�v�f��
     * {@link FreeformSurfaceWithControlPoints3D.MeshParam
     * FreeformSurfaceWithControlPoints3D.MeshParam}
     * �̃C���X�^���X�ł����̂Ƃ���?B
     * </p>
     */
    class GpList {
        /**
         * �p���??[�^�l��܂ރ��X�g?B
         */
        Vector list;

        /**
         * ��̃��X�g�Ƃ��ăI�u�W�F�N�g��?\�z����?B
         */
        GpList() {
            super();
            this.list = new Vector();
        }

        /**
         * ���̃��X�g���܂ރp���??[�^�l��?���Ԃ�?B
         *
         * @return ���X�g��̃p���??[�^�l��?�
         */
        int size() {
            return list.size();
        }

        /**
         * ���̃��X�g�̎w��̈ʒu�Ɋi�[����Ă���p���??[�^�l��Ԃ�?B
         *
         * @param index �p���??[�^�l�̃��X�g��ł̈ʒu���C���f�b�N�X
         * @return �w��̈ʒu�Ɋi�[����Ă���p���??[�^�l
         */
        MeshParam elementAt(int index) {
            return (MeshParam) list.elementAt(index);
        }

        /**
         * �^����ꂽ�p���??[�^�l�ⱂ̃��X�g�ɒǉB���?B
         * <p/>
         * gp �Ɠ����p���??[�^�l��\���I�u�W�F�N�g����ɂ��̃��X�g��ɑ�?݂���?�?��ɂ�?A
         * ���µ�Ȃ�?B
         * </p>
         *
         * @param gp ���X�g�ɒǉB���p���??[�^�l
         */
        void addGp(MeshParam gp) {
            int n_list = size();

            for (int i = 0; i < n_list; i++)
                if (gp.isSame(elementAt(i)))
                    return;

            list.addElement(gp);
            return;
        }
    }

    /**
     * ���̋Ȗʂ̎w��̋�`��Ԃ�?A
     * �^����ꂽ?��x�ŕ��ʋߎ�����i�q�_�̃p���??[�^�l��?�߂�?B
     *
     * @param si        ���ʋߎ������`���
     * @param tol       ���ʂƌ��Ȃ������̋��e��?�
     * @param u_gp_list �i�q�_�� U ���̃p���??[�^�l�̃��X�g (?o�͗p)
     * @param v_gp_list �i�q�_�� V ���̃p���??[�^�l�̃��X�g (?o�͗p)
     * @see #isPlaner(ToleranceForDistance)
     * @see #divideForMesh(ToleranceForDistance)
     * @see #makeMidGp(FreeformSurfaceWithControlPoints3D.MeshParam,FreeformSurfaceWithControlPoints3D.MeshParam)
     * @see FreeformSurfaceWithControlPoints3D.GpList#addGp(FreeformSurfaceWithControlPoints3D.MeshParam)
     * @see #makeParamAndMesh(FreeformSurfaceWithControlPoints3D.GpList,FreeformSurfaceWithControlPoints3D.GpList,double[],double[])
     */
    void getSrfMesh(SegInfo si, ToleranceForDistance tol,
                    GpList u_gp_list, GpList v_gp_list) {
        FreeformSurfaceWithControlPoints3D[] divsrf;
        FreeformSurfaceWithControlPoints3D lb_srf, rb_srf, lu_srf, ru_srf;
        SegInfo si_lb, si_rb;
        SegInfo si_lu, si_ru;
        MeshParam u_mp, v_mp;
        int ret_val;

        /*
        * if the surface is planar, add 4 corners into gp_list and return.
        */
        if (isPlaner(tol)) {
            u_gp_list.addGp(si.u_sp);
            u_gp_list.addGp(si.u_ep);
            v_gp_list.addGp(si.v_sp);
            v_gp_list.addGp(si.v_ep);
            return;
        }

        /*
        * divide it into 4, and call myself for each.
        */
        divsrf = divideForMesh(tol);
        lb_srf = divsrf[0];
        rb_srf = divsrf[1];
        lu_srf = divsrf[2];
        ru_srf = divsrf[3];

        if ((lb_srf == null) && (rb_srf == null) &&
                (lu_srf == null) && (ru_srf == null)) {
            /*
            * both of U/V are not divided
            */
            u_gp_list.addGp(si.u_sp);
            u_gp_list.addGp(si.u_ep);
            v_gp_list.addGp(si.v_sp);
            v_gp_list.addGp(si.v_ep);
            return;
        }

        u_mp = makeMidGp(si.u_sp, si.u_ep);
        v_mp = makeMidGp(si.v_sp, si.v_ep);

        if ((lb_srf != null) && (rb_srf != null) &&
                (lu_srf != null) && (ru_srf != null)) {
            /*
            * both of U/V are divided
            */
            si_lb = new SegInfo(si.u_sp, u_mp, si.v_sp, v_mp);
            si_rb = new SegInfo(u_mp, si.u_ep, si.v_sp, v_mp);
            si_lu = new SegInfo(si.u_sp, u_mp, v_mp, si.v_ep);
            si_ru = new SegInfo(u_mp, si.u_ep, v_mp, si.v_ep);

            lb_srf.getSrfMesh(si_lb, tol, u_gp_list, v_gp_list);
            rb_srf.getSrfMesh(si_rb, tol, u_gp_list, v_gp_list);
            lu_srf.getSrfMesh(si_lu, tol, u_gp_list, v_gp_list);
            ru_srf.getSrfMesh(si_ru, tol, u_gp_list, v_gp_list);

        } else if ((lb_srf != null) && (rb_srf == null) &&
                (lu_srf != null) && (ru_srf == null)) {
            /*
            * U is not divided
            */
            si_lb = new SegInfo(si.u_sp, si.u_ep, si.v_sp, v_mp);
            si_lu = new SegInfo(si.u_sp, si.u_ep, v_mp, si.v_ep);

            lb_srf.getSrfMesh(si_lb, tol, u_gp_list, v_gp_list);
            lu_srf.getSrfMesh(si_lu, tol, u_gp_list, v_gp_list);

        } else if ((lb_srf != null) && (rb_srf != null) &&
                (lu_srf == null) && (ru_srf == null)) {
            /*
            * V is not divided
            */
            si_lb = new SegInfo(si.u_sp, u_mp, si.v_sp, si.v_ep);
            si_rb = new SegInfo(u_mp, si.u_ep, si.v_sp, si.v_ep);

            lb_srf.getSrfMesh(si_lb, tol, u_gp_list, v_gp_list);
            rb_srf.getSrfMesh(si_rb, tol, u_gp_list, v_gp_list);
        }

        return;
    }

    /**
     * �^����ꂽ��̃p���??[�^�l�̒��_��Ԃ�?B
     *
     * @param sp �p���??[�^�l
     * @param ep �p���??[�^�l
     * @return ��̃p���??[�^�l�̒��_
     */
    private MeshParam makeMidGp(MeshParam sp, MeshParam ep) {
        MeshParam mp;
        int sgidx, denom, numer;

        sgidx = sp.sgidx; /* == ep->sgidx */

        denom = MathUtils.LCM(sp.denom, ep.denom);
        if ((denom == sp.denom) || (denom == ep.denom))
            denom *= 2;

        numer = ((sp.numer * (denom / sp.denom)) +
                (ep.numer * (denom / ep.denom))) / 2;

        return new MeshParam(sgidx, numer, denom);
    }

    /**
     * �^����ꂽ�_�񂪎w���?��x�̉��Œ�?�?�ł��邩�ۂ���Ԃ�?B
     * <p/>
     * �_�񂪋�?�ł���?�?�?Ainfo �̗v�f�ɂ͈ȉ���Ӗ�����l�����?B
     * <pre>
     * 		info[0] : ��?�̕��x�N�g���� X ?���
     * 		info[1] : ��?�̕��x�N�g���� Y ?���
     * 		info[2] : ��?�̕��x�N�g���� Z ?���
     * 		info[3] : ?łף�ꂽ��_�Ԃ̋���
     * </pre>
     * �Ȃ�?A���ׂĂ̓_�� sqrt(tol2) �ȓ�͈̔͂ɂ���?�?��ɂ�
     * info[i] (i = 0, 1, 2) �ɂ� 0 �����?B
     * </p>
     *
     * @param tol2 �����̋��e��?��̎�?�l
     * @param pnts �_��
     * @param info �v�f?� 4 �̔z�� (?o�͗p)
     * @return ��?�?�ł���� true?A�����łȂ���� false
     * @see #vIsColinear(Point3D[][],double)
     * @see #uIsColinear(Point3D[][],double)
     */
    private static boolean pointsAreColinear(double tol2,
                                             Point3D[] pnts,
                                             double[] info) {
        int npnts = pnts.length;
        int npnts_1 = npnts - 1;
        Point3D l_pnt;
        Vector3D uax;

        Vector3D evec, ecrs;
        double length;
        int i;

        if (npnts <= 1) {
            for (i = 0; i < 4; i++) info[i] = 0.0;
            return true;
        }

        l_pnt = pnts[0].longestPoint(pnts);
        uax = l_pnt.subtract(pnts[0]);
        if ((length = uax.norm()) < tol2) {
            /*
            * points are concurrent
            */
            for (i = 0; i < 3; i++) info[i] = 0.0;
            info[3] = Math.sqrt(length);
            return true;
        }

        /*
        *	     + pnts[j]
        *	    /^
        *	   / |
        *      /  | |cross_product((pnts[j] - pnts[0]), uax)|
        *	 /   v
        *   -+--------> uax
        *   pnts[0]
        */
        length = Math.sqrt(length);
        uax = uax.divide(length);

        for (i = 1; i < npnts; i++) {
            evec = pnts[i].subtract(pnts[0]);
            ecrs = evec.crossProduct(uax);
            if (ecrs.norm() > tol2)
                return false;
        }

        info[0] = uax.x();
        info[1] = uax.y();
        info[2] = uax.z();
        info[3] = length;
        return true;
    }

    /**
     * �^����ꂽ�i�q�_�Q�̊e?s��̓_��?A�w���?��x�̉���
     * ����̕��ɒ�?�?�ɕ�ł��邩�ۂ���Ԃ�?B
     *
     * @param pnts      �i�q?�̈ʑ���?�_�Q
     * @param tolerance �����̋��e��?�
     * @return �e?s��̓_��������ɒ�?�?�ł���� true?A�����łȂ���� false
     * @see #pointsAreColinear(double,Point3D[],double[])
     * @see #uIsColinear(Point3D[][],double)
     */
    static boolean vIsColinear(Point3D[][] pnts,
                               double tolerance) {
        double tol2 = tolerance * tolerance;
        int u_uicp = pnts.length;
        int v_uicp = pnts[0].length;
        double[] info = new double[4];
        Vector3D[] dir = new Vector3D[2];
        double[] leng = new double[2];
        Vector3D[] tgt_vec = new Vector3D[2];
        Vector3D crs_prod;
        boolean result;
        int u;

        result = true;

        for (u = 0; u < u_uicp; u++) {
            if (!pointsAreColinear(tol2, pnts[u], info)) {
                result = false;
                break;
            }
            dir[1] = new LiteralVector3D(info[0], info[1], info[2]);
            leng[1] = info[3];
            if (u == 0) {
                dir[0] = dir[1];
                leng[0] = leng[1];
                tgt_vec[0] = dir[0].multiply(leng[0]);
                continue;
            }

            tgt_vec[1] = dir[1].multiply(leng[1]);

            crs_prod = dir[0].crossProduct(tgt_vec[1]);
            if (crs_prod.norm() > tol2) {
                result = false;
                break;
            }

            crs_prod = dir[1].crossProduct(tgt_vec[0]);
            if (crs_prod.norm() > tol2) {
                result = false;
                break;
            }
        }

        return result;
    }

    /**
     * �^����ꂽ�i�q�_�Q�̊e���̓_��?A�w���?��x�̉���?A
     * ����̕��ɒ�?�?�ɕ�ł��邩�ۂ���Ԃ�?B
     *
     * @param pnts      �i�q?�̈ʑ���?�_�Q
     * @param tolerance �����̋��e��?�
     * @return �e���̓_��������ɒ�?�?�ł���� true?A�����łȂ���� false
     * @see #pointsAreColinear(double,Point3D[],double[])
     * @see #vIsColinear(Point3D[][],double)
     */
    static boolean uIsColinear(Point3D[][] pnts,
                               double tolerance) {
        double tol2 = tolerance * tolerance;
        int u_uicp = pnts.length;
        int v_uicp = pnts[0].length;
        Point3D[] my_pnts = new Point3D[u_uicp];
        double[] info = new double[4];
        Vector3D[] dir = new Vector3D[2];
        double[] leng = new double[2];
        Vector3D[] tgt_vec = new Vector3D[2];
        Vector3D crs_prod;
        boolean result;
        int u, v;

        result = true;

        for (v = 0; v < v_uicp; v++) {
            for (u = 0; u < u_uicp; u++)
                my_pnts[u] = pnts[u][v];
            if (!pointsAreColinear(tol2, my_pnts, info)) {
                result = false;
                break;
            }
            dir[1] = new LiteralVector3D(info[0], info[1], info[2]);
            leng[1] = info[3];
            if (v == 0) {
                dir[0] = dir[1];
                leng[0] = leng[1];
                tgt_vec[0] = dir[0].multiply(leng[0]);
                continue;
            }

            tgt_vec[1] = dir[1].multiply(leng[1]);

            crs_prod = dir[0].crossProduct(tgt_vec[1]);
            if (crs_prod.norm() > tol2) {
                result = false;
                break;
            }

            crs_prod = dir[1].crossProduct(tgt_vec[0]);
            if (crs_prod.norm() > tol2) {
                result = false;
                break;
            }
        }

        return result;
    }

    /**
     * ���̋Ȗʂɂ���?A�^����ꂽ�p���??[�^�l���X�g��̊e�p���??[�^�l�ɑΉ������_��܂�
     * �?�b�V����?�?�����?B
     * <p/>
     * ?�?������?�b�V����̊e�_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D
     * �ł��邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param u_gp_list �i�q�_�� U ���̃p���??[�^�l�̃��X�g
     * @param v_gp_list �i�q�_�� V ���̃p���??[�^�l�̃��X�g
     * @param u_kp      U ���̃p���??[�^�I��?k�ނ��Ă��Ȃ��L��ȃZ�O�?���g�̃m�b�g�_�̔z��
     * @param v_kp      V ���̃p���??[�^�I��?k�ނ��Ă��Ȃ��L��ȃZ�O�?���g�̃m�b�g�_�̔z��
     * @see PointOnSurface3D
     * @see #convGpList2Params(FreeformSurfaceWithControlPoints3D.GpList,double[])
     * @see #getSrfMesh(FreeformSurfaceWithControlPoints3D.SegInfo,ToleranceForDistance,FreeformSurfaceWithControlPoints3D.GpList,FreeformSurfaceWithControlPoints3D.GpList)
     */
    Mesh3D makeParamAndMesh(GpList u_gp_list,
                            GpList v_gp_list,
                            double[] u_kp,
                            double[] v_kp) {
        PointOnSurface3D[][] mesh;
        int u_npnts, v_npnts;
        double[] u_params, v_params;
        int i, j;        /* loop counter */

        /*
        * convert 'MeshParam' into surface's parameter & sort them
        */
        u_params = convGpList2Params(u_gp_list, u_kp);
        v_params = convGpList2Params(v_gp_list, v_kp);

        /*
        * get 3D coordinates for each of mesh points
        */
        u_npnts = u_params.length;
        v_npnts = v_params.length;
        mesh = new PointOnSurface3D[u_npnts][v_npnts];

        for (i = 0; i < u_npnts; i++)
            for (j = 0; j < v_npnts; j++) {
                try {
                    mesh[i][j] = new PointOnSurface3D(this, u_params[i], v_params[j], doCheckDebug);
                } catch (InvalidArgumentValueException e) {
                    throw new FatalException();
                }
            }

        return new Mesh3D(mesh, false);
    }

    /**
     * {@link FreeformSurfaceWithControlPoints3D.MeshParam
     * FreeformSurfaceWithControlPoints3D.MeshParam}
     * �̖��\?[�g�̃��X�g��?u��?�̃p���??[�^�l?v�̃\?[�g?ς̔z��ɕϊ�����?B
     *
     * @param gp_list MeshParam �̃��X�g
     * @param kp      �p���??[�^�I��?k�ނ��Ă��Ȃ��L��ȃZ�O�?���g�̃m�b�g�_�̔z��
     */
    private static double[] convGpList2Params(GpList gp_list,
                                              double[] kp) {
        int n_params;
        double[] params;
        MeshParam gp;
        double lp;

        n_params = gp_list.size();
        params = new double[n_params];

        for (int i = 0; i < n_params; i++) {
            gp = gp_list.elementAt(i);
            lp = (double) gp.numer / (double) gp.denom;
            params[i] = (kp[gp.sgidx] * (1.0 - lp)) + (kp[gp.sgidx + 1] * lp);
        }

        GeometryUtils.sortDoubleArray(params, 0, (n_params - 1));

        return params;
    }

    /*
    * ���̃C���X�^���X�̃t�B?[���h��?���_���?ݒ肷��?B
    * <p>
    * ���ʂƂ��ē�����z��̗v�f?��� 2 �ł���?B
    * ?�?��̗v�f�ɂ� U ����?���_��?�?A
    * ��Ԗڂ̗v�f�ɂ� V ����?���_��?�
    * �����?B
    * </p>
    * <p>
    * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
    * <ul>
    * <li>	controlPoints �� null �ł���
    * <li>	controlPoints �̒����� 2 ���?�����
    * <li>	controlPoints[i] �̒����� 2 ���?�����
    * <li>	controlPoints[k] �� controlPoints[l] �̒������قȂ�
    * <li>	controlPoints �̂���v�f�̒l�� null �ł���
    * </ul>
    * </p>
    *
    * @param controlPoints	?ݒ肷��?���_��
    * @return	?���_��?�
    * @see	InvalidArgumentValueException
    */
    private int[] setControlPoints(Point3D[][] controlPoints) {
        int[] npnts = new int[2];

        if (controlPoints == null) {
            throw new InvalidArgumentValueException();
        }
        if ((npnts[0] = controlPoints.length) < 2) {
            throw new InvalidArgumentValueException();
        }
        for (int i = 0; i < npnts[0]; i++) {
            if (i == 0) {
                if ((npnts[1] = controlPoints[0].length) < 2) {
                    throw new InvalidArgumentValueException();
                }
                this.controlPoints = new Point3D[npnts[0]][npnts[1]];
            } else {
                if (controlPoints[i].length != npnts[1]) {
                    throw new InvalidArgumentValueException();
                }
            }
            for (int j = 0; j < npnts[1]; j++) {
                if (controlPoints[i][j] == null) {
                    throw new InvalidArgumentValueException();
                }
                this.controlPoints[i][j] = controlPoints[i][j];
            }
        }
        return npnts;
    }

    /*
    * ���̃C���X�^���X�̃t�B?[���h��?d�ݗ��?ݒ肷��?B
    * <p>
    * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
    * <ul>
    * <li>	weights �� null �ł���
    * <li>	weights �̒����� npnts �ƈ�v���Ă��Ȃ�
    * <li>	weights �̂���v�f�̒l��?��łȂ�
    * <li>	weights �̂���v�f�̒l w �ɂ���?A
    *		(w / (weights ���?ő�l)) �� MachineEpsilon.DOUBLE ���?������Ȃ�?B
    * </ul>
    * </p>
    *
    * @param npnts	?���_��?�
    * @param weights	?ݒ肷��?d�ݗ�
    * @see	Util#isDividable(double, double)
    * @see	MachineEpsilon#DOUBLE
    * @see	InvalidArgumentValueException
    */
    private void setWeights(int[] npnts,
                            double[][] weights) {
        if (weights == null) {
            throw new InvalidArgumentValueException();
        }
        if (weights.length != npnts[0]) {
            throw new InvalidArgumentValueException();
        }

        double max_weight = 0.0;
        for (int i = 0; i < npnts[0]; i++)
            for (int j = 0; j < npnts[1]; j++)
                if (weights[i][j] > max_weight)
                    max_weight = weights[i][j];
        if (max_weight <= 0.0)
            throw new InvalidArgumentValueException();

        this.weights = new double[npnts[0]][npnts[1]];
        for (int i = 0; i < npnts[0]; i++) {
            if (weights[i].length != npnts[1]) {
                throw new InvalidArgumentValueException();
            }
            for (int j = 0; j < npnts[1]; j++) {
                if (weights[i][j] <= 0.0 || !GeometryUtils.isDividable(max_weight, weights[i][j])) {
                    throw new InvalidArgumentValueException();
                }
                this.weights[i][j] = weights[i][j];
            }
        }
    }

    /**
     * �^����ꂽ?���?���_��?�����i�[����?u��?��̎O�����z��?v�̗̈��l������?B
     *
     * @param isPoly ��?����`�����ۂ�
     * @param uSize  �z��̑�ꎟ���̗v�f?� (U ����?���_��?�)
     * @param vSize  �z��̑�񎟌��̗v�f?� (V ����?���_��?�)
     * @return ?���_��?�����i�[����z��
     */
    static protected double[][][] allocateDoubleArray(boolean isPoly,
                                                      int uSize,
                                                      int vSize) {
        return new double[uSize][vSize][(isPoly) ? 3 : 4];
    }

    /**
     * ���̋Ȗʂ�?���_ (�����?d��) ��?�����?A�^����ꂽ�O�����z��̗v�f�ɑ���?B
     * <p/>
     * isPoly �� true ��?�?�?A
     * doubleArray[i][j][0] �� (i, j) �Ԗڂ�?���_�� X ?���?A
     * doubleArray[i][j][1] �� (i, j) �Ԗڂ�?���_�� Y ?���
     * doubleArray[i][j][2] �� (i, j) �Ԗڂ�?���_�� Z ?���
     * ��\��?B
     * </p>
     * <p/>
     * isPoly �� false ��?�?�?A
     * doubleArray[i][j][0] �� ((i, j) �Ԗڂ�?���_�� X ?��� * (i, j) �Ԗڂ�?d��)?A
     * doubleArray[i][j][1] �� ((i, j) �Ԗڂ�?���_�� Y ?��� * (i, j) �Ԗڂ�?d��)?A
     * doubleArray[i][j][2] �� ((i, j) �Ԗڂ�?���_�� Z ?��� * (i, j) �Ԗڂ�?d��)?A
     * doubleArray[i][j][3] �� (i, j) �Ԗڂ�?d��
     * ��\��?B
     * </p>
     *
     * @param isPoly      ��?����`�����ۂ�
     * @param uUicp       �z��̑�ꎟ���̗v�f?� (�z��ɒl����� U ����?���_��?�)
     * @param vUicp       �z��̑�񎟌��̗v�f?� (�z��ɒl����� V ����?���_��?�)
     * @param doubleArray ?���_��?�����i�[����O�����z��
     */
    protected void setCoordinatesToDoubleArray(boolean isPoly,
                                               int uUicp,
                                               int vUicp,
                                               double[][][] doubleArray) {
        if (isPoly) {
            for (int i = 0; i < uUicp; i++) {
                for (int j = 0; j < vUicp; j++) {
                    doubleArray[i][j][0] = controlPoints[i][j].x();
                    doubleArray[i][j][1] = controlPoints[i][j].y();
                    doubleArray[i][j][2] = controlPoints[i][j].z();
                }
            }
        } else {
            for (int i = 0; i < uUicp; i++) {
                for (int j = 0; j < vUicp; j++) {
                    doubleArray[i][j][0] = controlPoints[i][j].x() * weights[i][j];
                    doubleArray[i][j][1] = controlPoints[i][j].y() * weights[i][j];
                    doubleArray[i][j][2] = controlPoints[i][j].z() * weights[i][j];
                    doubleArray[i][j][3] = weights[i][j];
                }
            }
        }

    }

    /*
    * ���̋Ȗʂ�?���_ (�����?d��) ��?�����܂�?u��?��̎O�����z��?v��Ԃ�?B
    * <p>
    * isPoly �� true ��?�?�?A
    * ���̃?�\�b�h���Ԃ��O�����z�� C �̗v�f
    * C[i][j][0] �� (i, j) �Ԗڂ�?���_�� X ?���?A
    * C[i][j][1] �� (i, j) �Ԗڂ�?���_�� Y ?���
    * C[i][j][2] �� (i, j) �Ԗڂ�?���_�� Z ?���
    * ��\��?B
    * </p>
    * <p>
    * isPoly �� false ��?�?�?A
    * ���̃?�\�b�h���Ԃ��O�����z�� C �̗v�f
    * C[i][j][0] �� ((i, j) �Ԗڂ�?���_�� X ?��� * (i, j) �Ԗڂ�?d��)?A
    * C[i][j][1] �� ((i, j) �Ԗڂ�?���_�� Y ?��� * (i, j) �Ԗڂ�?d��)?A
    * C[i][j][2] �� ((i, j) �Ԗڂ�?���_�� Z ?��� * (i, j) �Ԗڂ�?d��)?A
    * C[i][j][3] �� (i, j) �Ԗڂ�?d��
    * ��\��?B
    * </p>
    *
    * @param isPoly	��?����`�����ۂ�
    * @return	?���_ (�����?d��) ��?�����܂ޔz��
    */
    protected double[][][] toDoubleArray(boolean isPoly) {

        if (controlPointsArray != null) {
            return controlPointsArray;
        }

        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();

        controlPointsArray =
                FreeformSurfaceWithControlPoints3D.allocateDoubleArray(isPoly, uUicp, vUicp);

        setCoordinatesToDoubleArray(isPoly, uUicp, vUicp, controlPointsArray);

        return controlPointsArray;
    }

    /**
     * ����?W�ŗ^����ꂽ (�Ȗ�?��) �_�� X/Y/Z/W ?����ɕϊ�����?B
     * <p/>
     * (wx, wy, wz, w) �ŗ^����ꂽ (�Ȗ�?��) �_�� (x, y, z, w) �ɕϊ�����?B
     * </p>
     *
     * @param d0 �Ȗ�?�̓_
     */
    protected void convRational0Deriv(double[] d0) {
        for (int i = 0; i < 3; i++)
            d0[i] /= d0[3];
    }

    /**
     * ����?W�ŗ^����ꂽ�Ȗ�?�̓_�ƈꎟ�Γ���?��� X/Y/Z/W ?����ɕϊ�����?B
     * <p/>
     * (wx, wy, wz, w) �ŗ^����ꂽ�Ȗ�?�̓_�ƈꎟ�Γ���?��� (x, y, z, w) �ɕϊ�����?B
     * </p>
     *
     * @param d0 �Ȗ�?�̓_
     * @param du U ���̈ꎟ�Γ���?�
     * @param dv V ���̈ꎟ�Γ���?�
     */
    protected void convRational1Deriv(double[] d0, double[] du, double[] dv) {
        convRational0Deriv(d0);
        for (int i = 0; i < 3; i++) {
            du[i] = (du[i] - (du[3] * d0[i])) / d0[3];
            dv[i] = (dv[i] - (dv[3] * d0[i])) / d0[3];
        }
    }

    /**
     * ����?W�ŗ^����ꂽ�Ȗ�?�̓_/�ꎟ�Γ���?�/�񎟕Γ���?��� X/Y/Z/W ?����ɕϊ�����?B
     * <p/>
     * (wx, wy, wz, w) �ŗ^����ꂽ�Ȗ�?�̓_/�ꎟ�Γ���?�/�񎟕Γ���?��� (x, y, z, w) �ɕϊ�����?B
     * </p>
     *
     * @param d0  �Ȗ�?�̓_
     * @param du  U ���̈ꎟ�Γ���?�
     * @param dv  V ���̈ꎟ�Γ���?�
     * @param duv U ���̓񎟕Γ���?�
     * @param duv UV ���̈ꎟ?�?��Γ���?�
     * @param dvv V ���̓񎟕Γ���?�
     */
    protected void convRational2Deriv(double[] d0, double[] du, double[] dv,
                                      double[] duu, double[] duv, double[] dvv) {
        convRational1Deriv(d0, du, dv);
        for (int i = 0; i < 3; i++) {
            duu[i] = (duu[i] - (duu[3] * d0[i]) - (2.0 * du[3] * du[i])) / d0[3];
            duv[i] = (duv[i] - (duv[3] * d0[i]) - (du[3] * dv[i]) - (dv[3] * du[i])) / d0[3];
            dvv[i] = (dvv[i] - (dvv[3] * d0[i]) - (2.0 * dv[3] * dv[i])) / d0[3];
        }
    }

    /**
     * ���̋Ȗʂ̎��?�ł̂����悻�̑�?ݔ͈͂�����̂�Ԃ�?B
     * <p/>
     * ?���_���܂ޒ���̂�Ԃ�?B
     * </p>
     *
     * @return �Ȗʂ̂����悻�̑�?ݔ͈͂������
     */
    EnclosingBox3D approximateEnclosingBox() {
        double min_crd_x;
        double min_crd_y;
        double min_crd_z;
        double max_crd_x;
        double max_crd_y;
        double max_crd_z;
        int uN = uNControlPoints();
        int vN = vNControlPoints();
        Point3D point;
        double x, y, z;
        int i, j;

        point = controlPointAt(0, 0);
        max_crd_x = min_crd_x = point.x();
        max_crd_y = min_crd_y = point.y();
        max_crd_z = min_crd_z = point.z();

        for (i = 1; i < uN; i++) {
            point = controlPointAt(i, 0);
            x = point.x();
            y = point.y();
            z = point.z();
             /**/if (x < min_crd_x)
            min_crd_x = x;
        else if (x > max_crd_x) max_crd_x = x;
             /**/if (y < min_crd_y)
            min_crd_y = y;
        else if (y > max_crd_y) max_crd_y = y;
             /**/if (z < min_crd_z)
            min_crd_z = z;
        else if (z > max_crd_z) max_crd_z = z;
        }
        for (j = 1; j < vN; j++) {
            for (i = 0; i < uN; i++) {
                point = controlPointAt(i, j);
                x = point.x();
                y = point.y();
                z = point.z();
                 /**/if (x < min_crd_x)
                min_crd_x = x;
            else if (x > max_crd_x) max_crd_x = x;
                 /**/if (y < min_crd_y)
                min_crd_y = y;
            else if (y > max_crd_y) max_crd_y = y;
                 /**/if (z < min_crd_z)
                min_crd_z = z;
            else if (z > max_crd_z) max_crd_z = z;
            }
        }
        return new EnclosingBox3D(min_crd_x, min_crd_y, min_crd_z,
                max_crd_x, max_crd_y, max_crd_z);
    }

    /**
     * ���̋Ȗʂ� U ����?��䑽�p�`��?ő�̒�����Ԃ�?B
     *
     * @return U ����?��䑽�p�`��?ő�̒���
     */
    double getMaxLengthOfUControlPolygons(boolean closed) {
        double result;
        double scale;
        int i, j, k;

        result = 0.0;
        for (j = 0; j < vNControlPoints(); j++) {
            scale = 0.0;
            for (k = 0, i = 1; i < uNControlPoints(); k++, i++)
                scale += controlPointAt(k, j).distance(controlPointAt(i, j));
            if (closed == true)
                scale += controlPointAt(k, j).distance(controlPointAt(0, j));

            if (result < scale)
                result = scale;
        }

        return result;
    }

    /**
     * ���̋Ȗʂ� V ����?��䑽�p�`��?ő�̒�����Ԃ�?B
     *
     * @return V ����?��䑽�p�`��?ő�̒���
     */
    double getMaxLengthOfVControlPolygons(boolean closed) {
        double result;
        double scale;
        int i, j, k;

        result = 0.0;
        for (i = 0; i < uNControlPoints(); i++) {
            scale = 0.0;
            for (k = 0, j = 1; j < vNControlPoints(); k++, j++)
                scale += controlPointAt(i, k).distance(controlPointAt(i, j));
            if (closed == true)
                scale += controlPointAt(i, k).distance(controlPointAt(i, 0));

            if (result < scale)
                result = scale;
        }

        return result;
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ?�� true
     */
    public boolean isFreeform() {
        return true;
    }

    /**
     * ���̋Ȗʂ�?���_��?��ɓ������ψ��?d�ݗ��Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����z��̊e�v�f�̒l�� 1 �ł���?B
     * </p>
     *
     * @return ?d�݂̔z��
     */
    public double[][] makeUniformWeights() {
        double[][] uniformWeights = new double[this.uNControlPoints()][this.vNControlPoints()];
        for (int ui = 0; ui < this.uNControlPoints(); ui++)
            for (int vi = 0; vi < this.vNControlPoints(); vi++)
                uniformWeights[ui][vi] = 1.0;
        return uniformWeights;
    }
}
