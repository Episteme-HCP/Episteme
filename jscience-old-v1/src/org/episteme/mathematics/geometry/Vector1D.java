/*
 * �P�����̃x�N�g����\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Vector1D.java,v 1.2 2006/03/01 21:16:12 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �P�����̃x�N�g����\����?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:12 $
 * @see Point1D
 */

public abstract class Vector1D extends AbstractVector {

    /**
     * �P�����̃[�?�x�N�g��?B
     */
    public static final Vector1D zeroVector;

    /**
     * �P�����̃O�??[�o����?W�n�� X �����̒P�ʃx�N�g��?B
     */
    public static final Vector1D xUnitVector;

    /**
     * �P�����̃O�??[�o����?W�n�� -X �����̒P�ʃx�N�g��?B
     */
    public static final Vector1D nagativeXUnitVector;

    /**
     * static �ȃt�B?[���h�ɒl��?ݒ肷��?B
     */
    static {
        zeroVector = new LiteralVector1D(0.0);
        xUnitVector = new LiteralVector1D(1.0, true);
        nagativeXUnitVector = new LiteralVector1D(-1.0, true);
    }

    /**
     * �P�ʃx�N�g��
     *
     * @serial
     */
    private Vector1D unitized;

    /**
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?�?����悤�Ƃ���x�N�g����
     * �P�ʃx�N�g���ł��邩�ǂ���������Ȃ�?�?�?A�µ����
     * �P�ʃx�N�g���łȂ����Ƃ���?؂���Ă���?�?��ɂ�?A
     * ���̃R���X�g���N�^��g�p����?B
     * </p>
     */
    protected Vector1D() {
        super();

        unitized = null;
    }

    /**
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?�?����悤�Ƃ���x�N�g����
     * �P�ʃx�N�g���ł��邩�ǂ���������?�?��ɂ�?A
     * ���̃R���X�g���N�^��g�p����?B
     * </p>
     *
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł���Ȃ�� <code>true</code>?A
     *                            ����Ȃ��� <code>false</code>
     */
    protected Vector1D(boolean confirmedAsUnitized) {
        super();

        unitized = (confirmedAsUnitized) ? this : null;
    }

    /**
     * �P�����̃[�?�x�N�g����Ԃ�?B
     *
     * @return �P�����̃[�?�x�N�g��
     */
    public static Vector1D zeroVector() {
        return zeroVector;
    }

    /**
     * �P�����̃O�??[�o����?W�n�� X �����̒P�ʃx�N�g����Ԃ�?B
     *
     * @return �P�����̃O�??[�o����?W�n�� X �����̒P�ʃx�N�g��
     */
    public static Vector1D xUnitVector() {
        return xUnitVector;
    }

    /**
     * ������Ԃ�?B
     * <p/>
     * ?�� 1 ��Ԃ�?B
     * </p>
     *
     * @return �P�����Ȃ̂�?A?�� 1
     */
    public int dimension() {
        return 1;
    }

    /**
     * �P�������ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �P�����Ȃ̂� <code>true</code>
     */
    public boolean is1D() {
        return true;
    }

    /**
     * �x�N�g���� X ?�����Ԃ���?ۃ?�\�b�h?B
     *
     * @return �x�N�g���� X ?���
     */
    public abstract double x();

    /**
     * �P�ʉ������x�N�g����Ԃ�?B
     *
     * @return �P�ʉ������x�N�g��
     */
    public Vector1D unitized() {
        if (unitized != null)
            return unitized;

        // 1D unitized vector always takes 1 as x
        if (!(x() < 0.0))
            return (unitized = xUnitVector);
        else
            return (unitized = nagativeXUnitVector);
    }

    /**
     * �e?����̕�?��𔽓]�������x�N�g����Ԃ�?B
     *
     * @return this �𔽓]�����x�N�g��
     */
    public Vector1D reverse() {
        return new LiteralVector1D(-x());
    }

    /**
     * ��?ς�Ԃ�?B
     *
     * @param mate ��?ς��鑊��̃x�N�g��
     * @return ��?�
     */
    public double dotProduct(Vector1D mate) {
        return x() * mate.x();
    }

    /**
     * �x�N�g�����m�̘a��Ԃ�?B
     *
     * @param mate �a���鑊��̃x�N�g��
     * @return �x�N�g���̘a (this + mate)
     */
    public Vector1D add(Vector1D mate) {
        return new LiteralVector1D(x() + mate.x());
    }

    /**
     * �x�N�g�����m��?���Ԃ�?B
     *
     * @param mate ?����鑊��̃x�N�g��
     * @return �x�N�g����?� (this - mate)
     */
    public Vector1D subtract(Vector1D mate) {
        return new LiteralVector1D(x() - mate.x());
    }

    /**
     * �^����ꂽ�X�P?[����?悶���x�N�g����Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this * scale)
     */
    public Vector1D multiply(double scale) {
        return new LiteralVector1D(x() * scale);
    }

    /**
     * �^����ꂽ�X�P?[���Ŋ��B��x�N�g����Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this / scale)
     */
    public Vector1D divide(double scale) {
        return new LiteralVector1D(x() / scale);
    }

    /**
     * �Q�x�N�g���̓���?��𔻒肷��?B
     *
     * @param mate ����̑�?ۂƂȂ�x�N�g��
     * @return this �� mate ��?u�����̋��e��?�?v�ȓ��
     *         ����̃x�N�g���ł���Ƃ݂Ȃ���� true?A����Ȃ��� false
     * @see ConditionOfOperation
     */
    public boolean identical(Vector1D mate) {
        double dTol;
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();

        dTol = condition.getToleranceForDistance();
        return Math.abs(x() - mate.x()) < dTol;
    }

    /**
     * �x�N�g���̑傫����Ԃ�?B
     *
     * @return �x�N�g���̑傫�� abs(x)
     */
    public double length() {
        return Math.abs(x());
    }

    /**
     * �x�N�g���̑傫����Ԃ�?B
     *
     * @return �x�N�g���̑傫�� abs(x)
     */
    public double magnitude() {
        return Math.abs(x());
    }

    /**
     * �x�N�g���̃m������Ԃ�?B
     *
     * @return �x�N�g���̃m���� (x * x)
     */
    public double norm() {
        double val = x();
        return val * val;
    }

    /**
     * �P�����̓_ (Point1D) �ɕϊ�����?B
     *
     * @return ���_����̈ʒu�x�N�g���Ƃ݂Ȃ����_
     */
    public Point1D toPoint1D() {
        return Point1D.of(x());
    }

    /**
     * LiteralVector1D �̃C���X�^���X��?�?�����?B
     *
     * @param x X ?���
     * @return LiteralVector1D �̃C���X�^���X
     */
    public static LiteralVector1D of(double x) {
        return new LiteralVector1D(x);
    }
}
