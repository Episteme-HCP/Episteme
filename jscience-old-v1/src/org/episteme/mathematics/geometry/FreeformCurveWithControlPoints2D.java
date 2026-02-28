/*
 * �Q���� : ?���_��?�B����R��?��\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FreeformCurveWithControlPoints2D.java,v 1.3 2007-10-21 21:08:11 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �Q���� : ?���_��?�B����R��?��\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ?���_ (Point2D) �̔z�� controlPoints
 * ��
 * ?d�� (double) �̔z�� weights
 * ��?��?B
 * </p>
 * <p/>
 * weights �� null ��?�?��ɂ͔�L�?��?� (��?�����?�) ��\��?B
 * </p>
 * <p/>
 * weights �ɔz��?ݒ肳��Ă���?�?��ɂ͗L�?��?��\��?B
 * weights[i] �� controlPoints[i] �ɑΉ�����?B
 * �Ȃ�?A���܂̂Ƃ��� weights[i] �̒l��?��łȂ���΂Ȃ�Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:11 $
 */

public abstract class FreeformCurveWithControlPoints2D extends BoundedCurve2D {
    /**
     * ?���_�̔z��?B
     *
     * @serial
     */
    protected Point2D[] controlPoints;

    /**
     * ?d�݂̔z��?B
     * <p/>
     * ��?�����?�ł���� null �Ƃ���?B
     * </p>
     *
     * @serial
     */
    protected double[] weights;

    /**
     * ?���_��?d�݂�\���񎟌��z��?B
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * </p>
     * <p/>
     * controlPointsArray[i] �̒����� 2 ��?�?�?A
     * controlPointsArray[i][0] �� i �Ԗڂ�?���_�� X ?���?A
     * controlPointsArray[i][1] �� i �Ԗڂ�?���_�� Y ?���
     * ��\��?B
     * </p>
     * <p/>
     * controlPointsArray[i] �̒����� 3 ��?�?�?A
     * controlPointsArray[i][0] �� (i �Ԗڂ�?���_�� X ?��� * i �Ԗڂ�?d��)?A
     * controlPointsArray[i][1] �� (i �Ԗڂ�?���_�� Y ?��� * i �Ԗڂ�?d��)?A
     * controlPointsArray[i][2] �� i �Ԗڂ�?d��
     * ��\��?B
     * </p>
     *
     * @serial
     */
    private double[][] controlPointsArray = null;

    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     * <p/>
     * �e�t�B?[���h�ɂ͒l��?ݒ肵�Ȃ�?B
     * </p>
     */
    protected FreeformCurveWithControlPoints2D() {
        super();
    }

    /**
     * ?���_���^���đ�?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * <ul>
     * <li>	controlPoints �� null �ł���
     * <li>	controlPoints �̒����� 2 ���?�����
     * <li>	controlPoints �̂���v�f�̒l�� null �ł���
     * </ul>
     * </p>
     *
     * @param controlPoints ?���_�̔z��
     * @see InvalidArgumentValueException
     */
    protected FreeformCurveWithControlPoints2D(Point2D[] controlPoints) {
        super();
        int npnts = setControlPoints(controlPoints);
        weights = null;
    }

    /**
     * ?���_���?d�ݗ��^���ėL�?��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * <ul>
     * <li>	controlPoints �� null �ł���
     * <li>	controlPoints �̒����� 2 ���?�����
     * <li>	controlPoints �̂���v�f�̒l�� null �ł���
     * <li>	weights �� null �ł���
     * <li>	weights �̒����� npnts �ƈ�v���Ă��Ȃ�
     * <li>	weights �̂���v�f�̒l��?��łȂ�
     * <li>	weights �̂���v�f�̒l w �ɂ���?A
     * (w / (weights ���?ő�l)) �� MachineEpsilon.DOUBLE ���?������Ȃ�?B
     * </ul>
     * </p>
     *
     * @param controlPoints ?���_�̔z��
     * @param weights       ?d�݂̔z��
     * @see InvalidArgumentValueException
     */
    protected FreeformCurveWithControlPoints2D(Point2D[] controlPoints,
                                               double[] weights) {
        super();
        int npnts = setControlPoints(controlPoints);
        setWeights(npnts, weights);
    }

    /**
     * ?���_ (��?d��) ��񎟌��z��ŗ^����
     * ��?�����?� (���邢�͗L�?��?�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * cpArray �̒�����?���_��?��Ƃ���?B
     * �܂�?AcpArray[0] �̒����� 2 �ł���Α�?�����?�?A3 �ł���ΗL�?��?�Ƃ���?B
     * </p>
     * <p/>
     * cpArray[i] �̒����� 2 ��?�?�?A
     * cpArray[i][0] �� i �Ԗڂ�?���_�� X ?���?A
     * cpArray[i][1] �� i �Ԗڂ�?���_�� Y ?���
     * ����̂Ƃ���?B
     * </p>
     * <p/>
     * cpArray[i] �̒����� 3 ��?�?�?A
     * cpArray[i][0] �� (i �Ԗڂ�?���_�� X ?��� * i �Ԗڂ�?d��)?A
     * cpArray[i][1] �� (i �Ԗڂ�?���_�� Y ?��� * i �Ԗڂ�?d��)?A
     * cpArray[i][2] �� i �Ԗڂ�?d��
     * ����̂Ƃ���?B
     * </p>
     * <p/>
     * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * <ul>
     * <li>	?���_��?��� 2 ���?�����
     * <li>	����?d�݂̒l��?��łȂ�
     * <li>	����?d�݂̒l w �ɂ���?A
     * (w / (?d�ݗ���?ő�l)) �� MachineEpsilon.DOUBLE ���?������Ȃ�?B
     * </ul>
     * </p>
     *
     * @param cpArray ?���_ (�����?d��) �̔z��
     * @see InvalidArgumentValueException
     */
    protected FreeformCurveWithControlPoints2D(double[][] cpArray) {
        super();

        int npnts = cpArray.length;
        Point2D[] cp = new Point2D[npnts];
        boolean isPoly = (cpArray[0].length == 2);

        if (!isPoly) {    // �L�?
            double[] tmp = new double[3];
            double[] wt = new double[npnts];
            for (int i = 0; i < npnts; i++) {
                for (int j = 0; j < 3; j++)
                    tmp[j] = cpArray[i][j];
                convRational0Deriv(tmp);
                cp[i] = new CartesianPoint2D(tmp[0], tmp[1]);
                wt[i] = tmp[2];
            }
            setWeights(npnts, wt);
        } else {
            for (int i = 0; i < npnts; i++) {
                cp[i] = new CartesianPoint2D(cpArray[i][0], cpArray[i][1]);
            }
            weights = null;
        }
        npnts = setControlPoints(cp);
    }

    /**
     * ?���_���?d�ݗ��^����
     * ��?�����?�邢�͗L�?��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� false ��?�?�?A
     * �^����ꂽ controlPoints ����� weights �̒l��
     * �Ή�����t�B?[���h�ɂ��̂܂�?ݒ肷��?B
     * ������ weights �� null �ł����?A��?�͔�L�? (��?���) �`���ɂȂ�?B
     * �Ȃ�?AcontrolPoints �� null ���^�������?A�\��ł��Ȃ����ʂ�?���?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?�?A
     * weights ���l��?�Ă�
     * {@link #FreeformCurveWithControlPoints2D(Point2D[],double[])
     * FreeformCurveWithControlPoints2D(Point2D[], double[])}?A
     * weights �� null �ł����
     * {@link #FreeformCurveWithControlPoints2D(Point2D[])
     * FreeformCurveWithControlPoints2D(Point2D[])}
     * �Ɠ��l��?��?��?s�Ȃ�?B
     * </p>
     *
     * @param controlPoitns ?���_�̔z��
     * @param weights       ?d�݂̔z��
     * @param doCheck       ��?��̃`�F�b�N��?s�Ȃ����ǂ���
     */
    protected FreeformCurveWithControlPoints2D(Point2D[] controlPoints,
                                               double[] weights,
                                               boolean doCheck) {
        super();
        if (doCheck) {
            int npnts = setControlPoints(controlPoints);
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
     * ���̋�?��?���_���Ԃ�?B
     *
     * @return ?���_�̔z��
     */
    public Point2D[] controlPoints() {
        Point2D[] copied = new Point2D[controlPoints.length];

        for (int i = 0; i < controlPoints.length; i++)
            copied[i] = controlPoints[i];
        return copied;
    }

    /**
     * ���̋�?�� i �Ԗڂ�?���_��Ԃ�?B
     *
     * @param i �C���f�b�N�X
     * @return ?���_
     */
    public Point2D controlPointAt(int i) {
        return controlPoints[i];
    }

    /**
     * ���̋�?��?d�ݗ��Ԃ�?B
     * <p/>
     * ��?�?�����?��?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return ?d�݂̔z��
     */
    public double[] weights() {
        if (weights == null)
            return null;
        return (double[]) weights.clone();
    }

    /**
     * ���̋�?�� i �Ԗڂ�?���_��?d�݂�Ԃ�?B
     * <p/>
     * ��?�?�����?��?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param i �C���f�b�N�X
     * @return ?d��
     * @see InvalidArgumentValueException
     */
    public double weightAt(int i) {
        if (weights == null)
            throw new InvalidArgumentValueException();
        return weights[i];
    }

    /**
     * ���̋�?��?���_��?���Ԃ�?B
     *
     * @return ?���_��?�
     */
    public int nControlPoints() {
        return controlPoints.length;
    }

    /**
     * ���̋�?�L�?�`�����ۂ���Ԃ�?B
     *
     * @return �L�?�`���Ȃ�� true?A�����łȂ���� false
     */
    public boolean isRational() {
        return weights != null;
    }

    /**
     * ���̋�?�?����`�����ۂ���Ԃ�?B
     *
     * @return ��?����`���Ȃ�� true?A�����łȂ���� false
     */
    public boolean isPolynomial() {
        return weights == null;
    }

    /**
     * ���̋�?�̎��?�ł̂����悻�̒�����Ԃ�?B
     * <p/>
     * ?���_��싂񂾃|���S�� (?��䑽�p�`) �̒�����Ԃ�?B
     * </p>
     *
     * @return ��?�̂����悻�̒���
     */
    double approximateLength() {
        int i, j;
        double aprx_leng;

        aprx_leng = 0.0;
        for (i = 0, j = 1; j < nControlPoints(); i++, j++)
            aprx_leng += controlPointAt(i).distance(controlPointAt(j));

        return aprx_leng;
    }

    /**
     * ���̋�?�̎��?�ł̂����悻�̑�?ݔ͈͂���`��Ԃ�?B
     * <p/>
     * ?���_���܂ދ�`��Ԃ�?B
     * </p>
     *
     * @return ��?�̂����悻�̑�?ݔ͈͂���`
     */
    EnclosingBox2D approximateEnclosingBox() {
        double min_crd_x;
        double min_crd_y;
        double max_crd_x;
        double max_crd_y;
        int n = nControlPoints();
        Point2D point;
        double x, y;

        point = controlPointAt(0);
        max_crd_x = min_crd_x = point.x();
        max_crd_y = min_crd_y = point.y();

        for (int i = 1; i < n; i++) {
            point = controlPointAt(i);
            x = point.x();
            y = point.y();
             /**/if (x < min_crd_x)
            min_crd_x = x;
        else if (x > max_crd_x) max_crd_x = x;
             /**/if (y < min_crd_y)
            min_crd_y = y;
        else if (y > max_crd_y) max_crd_y = y;
        }
        return new EnclosingBox2D(min_crd_x, min_crd_y, max_crd_x, max_crd_y);
    }

    /**
     * ���̃C���X�^���X�̃t�B?[���h��?���_���?ݒ肷��?B
     * <p/>
     * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * <ul>
     * <li>	controlPoints �� null �ł���
     * <li>	controlPoints �̒����� 2 ���?�����
     * <li>	controlPoints �̂���v�f�̒l�� null �ł���
     * </ul>
     * </p>
     *
     * @param controlPoints ?ݒ肷��?���_��
     * @return ?���_��?�
     * @see InvalidArgumentValueException
     */
    private int setControlPoints(Point2D[] controlPoints) {
        int npnts;

        if (controlPoints == null) {
            throw new InvalidArgumentValueException();
        }
        if ((npnts = controlPoints.length) < 2) {
            throw new InvalidArgumentValueException();
        }
        this.controlPoints = new Point2D[npnts];
        for (int i = 0; i < npnts; i++) {
            if (controlPoints[i] == null) {
                throw new InvalidArgumentValueException();
            }
            this.controlPoints[i] = controlPoints[i];
        }
        return npnts;
    }

    /**
     * ���̃C���X�^���X�̃t�B?[���h��?d�ݗ��?ݒ肷��?B
     * <p/>
     * �ȉ��̂����ꂩ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * <ul>
     * <li>	weights �� null �ł���
     * <li>	weights �̒����� npnts �ƈ�v���Ă��Ȃ�
     * <li>	weights �̂���v�f�̒l��?��łȂ�
     * <li>	weights �̂���v�f�̒l w �ɂ���?A
     * (w / (weights ���?ő�l)) �� MachineEpsilon.DOUBLE ���?������Ȃ�?B
     * </ul>
     * </p>
     *
     * @param npnts   ?���_��?�
     * @param weights ?ݒ肷��?d�ݗ�
     * @see Util#isDividable(double,double)
     * @see MachineEpsilon#DOUBLE
     * @see InvalidArgumentValueException
     */
    private void setWeights(int npnts, double[] weights) {
        if (weights == null) {
            throw new InvalidArgumentValueException();
        }
        if (weights.length != npnts) {
            throw new InvalidArgumentValueException();
        }

        double max_weight = 0.0;
        for (int i = 0; i < npnts; i++)
            if (weights[i] > max_weight)
                max_weight = weights[i];
        if (max_weight <= 0.0)
            throw new InvalidArgumentValueException();

        this.weights = new double[npnts];
        for (int i = 0; i < npnts; i++) {
            if (weights[i] <= 0.0 || !GeometryUtils.isDividable(max_weight, weights[i])) {
                throw new InvalidArgumentValueException();
            }
            this.weights[i] = weights[i];
        }
    }

    /**
     * �^����ꂽ?���?���_��?�����i�[����?u��?��̓񎟌��z��?v�̗̈��l������?B
     *
     * @param isPoly ��?����`�����ۂ�
     * @param size   �z��̑�ꎟ���̗v�f?� (?���_��?�)
     * @return ?���_��?�����i�[����z��
     */
    static protected double[][] allocateDoubleArray(boolean isPoly,
                                                    int size) {
        return new double[size][(isPoly) ? 2 : 3];
    }

    /**
     * ���̋�?��?���_ (�����?d��) ��?�����?A�^����ꂽ�񎟌��z��̗v�f�ɑ���?B
     * <p/>
     * isPoly �� true ��?�?�?A
     * doubleArray[i][0] �� i �Ԗڂ�?���_�� X ?���?A
     * doubleArray[i][1] �� i �Ԗڂ�?���_�� Y ?���
     * ��\��?B
     * </p>
     * <p/>
     * isPoly �� false ��?�?�?A
     * doubleArray[i][0] �� (i �Ԗڂ�?���_�� X ?��� * i �Ԗڂ�?d��)?A
     * doubleArray[i][1] �� (i �Ԗڂ�?���_�� Y ?��� * i �Ԗڂ�?d��)?A
     * doubleArray[i][2] �� i �Ԗڂ�?d��
     * ��\��?B
     * </p>
     *
     * @param isPoly      ��?����`�����ۂ�
     * @param uicp        �z��̑�ꎟ���̗v�f?� (�z��ɒl�����?���_��?�)
     * @param doubleArray ?���_��?�����i�[����񎟌��z��
     */
    protected void setCoordinatesToDoubleArray(boolean isPoly,
                                               int uicp,
                                               double[][] doubleArray) {
        if (isPoly) {
            for (int i = 0; i < uicp; i++) {
                doubleArray[i][0] = controlPoints[i].x();
                doubleArray[i][1] = controlPoints[i].y();
            }
        } else {
            for (int i = 0; i < uicp; i++) {
                doubleArray[i][0] = controlPoints[i].x() * weights[i];
                doubleArray[i][1] = controlPoints[i].y() * weights[i];
                doubleArray[i][2] = weights[i];
            }
        }
    }

    /**
     * ���̋�?��?���_ (�����?d��) ��?�����܂�?u��?��̓񎟌��z��?v��Ԃ�?B
     * <p/>
     * isPoly �� true ��?�?�?A
     * ���̃?�\�b�h���Ԃ��񎟌��z�� C �̗v�f
     * C[i][0] �� i �Ԗڂ�?���_�� X ?���?A
     * C[i][1] �� i �Ԗڂ�?���_�� Y ?���
     * ��\��?B
     * </p>
     * <p/>
     * isPoly �� false ��?�?�?A
     * ���̃?�\�b�h���Ԃ��񎟌��z�� C �̗v�f
     * C[i][0] �� (i �Ԗڂ�?���_�� X ?��� * i �Ԗڂ�?d��)?A
     * C[i][1] �� (i �Ԗڂ�?���_�� Y ?��� * i �Ԗڂ�?d��)?A
     * C[i][2] �� i �Ԗڂ�?d��
     * ��\��?B
     * </p>
     *
     * @param isPoly ��?����`�����ۂ�
     * @return ?���_ (�����?d��) ��?�����܂ޔz��
     */
    protected double[][] toDoubleArray(boolean isPoly) {
        if (controlPointsArray != null)
            return controlPointsArray;

        int uicp = nControlPoints();

        controlPointsArray =
                FreeformCurveWithControlPoints2D.allocateDoubleArray(isPoly, uicp);

        setCoordinatesToDoubleArray(isPoly, uicp, controlPointsArray);

        return controlPointsArray;
    }

    /**
     * ����?W�ŗ^����ꂽ (��?�?��) �_�� X/Y/W ?����ɕϊ�����?B
     * <p/>
     * (wx, wy, w) �ŗ^����ꂽ (��?�?��) �_�� (x, y, w) �ɕϊ�����?B
     * </p>
     *
     * @param d0D ��?�?�̓_
     */
    protected void convRational0Deriv(double[] d0D) {
        for (int i = 0; i < 2; i++) {
            d0D[i] /= d0D[2];
        }
    }

    /**
     * ����?W�ŗ^����ꂽ��?�?�̓_�ƈꎟ����?��� X/Y/W ?����ɕϊ�����?B
     * <p/>
     * (wx, wy, w) �ŗ^����ꂽ��?�?�̓_�ƈꎟ����?��� (x, y, w) �ɕϊ�����?B
     * </p>
     *
     * @param d0D ��?�?�̓_
     * @param d1D �ꎟ����?�
     */
    protected void convRational1Deriv(double[] d0D, double[] d1D) {
        convRational0Deriv(d0D);
        for (int i = 0; i < 2; i++) {
            d1D[i] = (d1D[i] - (d1D[2] * d0D[i])) / d0D[2];
        }
    }

    /**
     * ����?W�ŗ^����ꂽ��?�?�̓_/�ꎟ����?�/�񎟓���?��� X/Y/W ?����ɕϊ�����?B
     * <p/>
     * (wx, wy, w) �ŗ^����ꂽ��?�?�̓_/�ꎟ����?�/�񎟓���?��� (x, y, w) �ɕϊ�����?B
     * </p>
     *
     * @param d0D ��?�?�̓_
     * @param d1D �ꎟ����?�
     * @param d2D �񎟓���?�
     */
    protected void convRational2Deriv(double[] d0D, double[] d1D, double[] d2D) {
        convRational1Deriv(d0D, d1D);
        for (int i = 0; i < 2; i++) {
            d2D[i] = (d2D[i] - ((2.0 * d1D[2] * d1D[i]) + (d2D[2] * d0D[i]))) / d0D[2];
        }
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
     * ���̋�?��?���_��?��ɓ������ψ��?d�ݗ��Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����z��̊e�v�f�̒l�� 1 �ł���?B
     * </p>
     *
     * @return ?d�݂̔z��
     */
    public double[] makeUniformWeights() {
        double[] uniformWeights = new double[this.nControlPoints()];
        for (int i = 0; i < uniformWeights.length; i++)
            uniformWeights[i] = 1.0;
        return uniformWeights;
    }
}
