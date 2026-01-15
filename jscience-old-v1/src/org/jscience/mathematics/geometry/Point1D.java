/*
 * �P�����̓_��\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Point1D.java,v 1.2 2006/03/01 21:16:07 virtualcall Exp $
 *
 */

package org.jscience.mathematics.geometry;

/**
 * �P�����̓_��\����?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:07 $
 * @see Vector1D
 */

public abstract class Point1D extends AbstractPoint {
    /**
     * �P�����̌��_ (0)?B
     */
    public static final Point1D origin;

    /**
     * static �ȃt�B?[���h�ɒl��?ݒ肷��?B
     */
    static {
        origin = new CartesianPoint1D(0.0);
    }

    /**
     * �I�u�W�F�N�g��?\�z����?B
     */
    protected Point1D() {
        super();
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
     * �P�������ۂ���Ԃ�
     * <p/>
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �P�����Ȃ̂�?A?�� <code>true</code>
     */
    public boolean is1D() {
        return true;
    }

    /**
     * �_�� X ?W�l��Ԃ���?ۃ?�\�b�h?B
     *
     * @return �_�� X ?W�l
     */
    public abstract double x();

    /**
     * ���̓_�ɗ^����ꂽ�x�N�g���𑫂����_��Ԃ�?B
     *
     * @param vector �_�ɑ����x�N�g��
     * @return �^����ꂽ�x�N�g���𑫂����_ (this + vector)
     */
    public Point1D add(Vector1D vector) {
        return new CartesianPoint1D(x() + vector.x());
    }

    /**
     * ���̓_����^����ꂽ�x�N�g����򢂽�_��Ԃ�?B
     *
     * @param vector �_�����x�N�g��
     * @return �^����ꂽ�x�N�g����򢂽�_ (this - vector)
     */
    public Point1D subtract(Vector1D vector) {
        return new CartesianPoint1D(x() - vector.x());
    }

    /**
     * ���̓_�ɗ^����ꂽ�X�P?[����?悶���_��Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this * scale)
     */
    public Point1D multiply(double scale) {
        return new CartesianPoint1D(x() * scale);
    }

    /**
     * ���̓_��^����ꂽ�X�P?[���Ŋ��B��_��Ԃ�?B
     *
     * @param scale �X�P?[��
     * @return (this / scale)
     */
    public Point1D divide(double scale) {
        return new CartesianPoint1D(x() / scale);
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_��?�^��Ԃ������ʂ�Ԃ�?B
     *
     * @param mate          ?�`��Ԃ̑���ƂȂ�_
     * @param weightForThis ��?g�ɑ΂���?d�� (����ɑ΂���?d�݂� 1 - weightForThis)
     * @return ?�`��Ԃ������ʂ̓_ (weightForThis * this + (1 - weightForThis) * mate)
     */
    public Point1D linearInterpolate(Point1D mate,
                                     double weightForThis) {
        return new CartesianPoint1D(this.x() * weightForThis + mate.x() * (1 - weightForThis));
    }

    /**
     * ���̓_�Ɨ^����ꂽ�_�̒��_��Ԃ�?B
     *
     * @param mate ���_��?�߂鑊��ƂȂ�_
     * @return ���_ (0.5 * this + 0.5 * mate)
     */
    public Point1D midPoint(Point1D mate) {
        return linearInterpolate(mate, 0.5);
    }

    /**
     * �Q�_�̓���?��𔻒肷��?B
     *
     * @param mate ����̑�?ۂƂȂ�_
     * @return this �� mate ��?u�����̋��e��?�?v�ȓ��
     *         ����̓_�ł���Ƃ݂Ȃ���� true?A����Ȃ��� false
     * @see ConditionOfOperation
     */
    public boolean identical(Point1D mate) {
        return Math.abs(x() - mate.x()) < getToleranceForDistance();
    }

    /**
     * ���̓_��P�����̃x�N�g�� (Vector1D) �ɕϊ�����?B
     *
     * @return ���_����̃x�N�g���Ƃ݂Ȃ����x�N�g��
     */
    public Vector1D toVector1D() {
        return new LiteralVector1D(x());
    }

    /**
     * CartesianPoint1D �̃C���X�^���X��?�?�����?B
     *
     * @param x X ?���
     * @return CartesianPoint1D �̃C���X�^���X
     */
    public static CartesianPoint1D of(double x) {
        return new CartesianPoint1D(x);
    }
}
