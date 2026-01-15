/*
 * �􉽓I�ȕϊ���?s�Ȃ����Z�q��\���N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: AbstractCartesianTransformationOperator.java,v 1.3 2007-10-21 21:08:05 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �􉽓I�ȕϊ���?s�Ȃ����Z�q��\���N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * �􉽓I�ȕϊ���?A��?s�ړ�?A��]�ړ�?A�~��?[�����O?A�ψ�ȃX�P?[�����O
 * ��?\?������?B
 * ���̕ϊ��ł�?A�ϊ��O�ƕϊ���ŔC�ӂ̓�_�Ԃ̋����̔�͈��ł���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:05 $
 */

public abstract class AbstractCartesianTransformationOperator extends GeometryElement {
    /**
     * �X�P?[�����O�l?B
     *
     * @serial
     */
    private final double scale;

    /**
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * scale �̒l��?��łȂ���΂Ȃ�Ȃ�?B
     * </p>
     * <p/>
     * scale �̒l��?A��?�?ݒ肳��Ă��鉉�Z?�?��
     * �����̋��e��?��ȉ���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param scale �X�P?[�����O�l
     */
    protected AbstractCartesianTransformationOperator(double scale) {
        super();
        if (scale <= ConditionOfOperation.getCondition().getToleranceForDistance())
            throw new InvalidArgumentValueException();
        this.scale = scale;
    }

    /**
     * �􉽓I�ȕϊ���?s�Ȃ����Z�q���ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �􉽓I�ȕϊ���?s�Ȃ����Z�q�Ȃ̂�?A?�� true
     */
    public boolean isTransformationOperator() {
        return true;
    }

    /**
     * ���̉��Z�q�̃X�P?[�����O�l��Ԃ�?B
     */
    public double scale() {
        return scale;
    }

    /**
     * �^����ꂽ�l (����) ��?A���̉��Z�q�̃X�P?[�����O�l��|�����l��Ԃ�?B
     *
     * @param length ����
     * @return �X�P?[�����O��{��������
     */
    public double transform(double length) {
        return length * scale;
    }

    /**
     * �^����ꂽ�l (����) ��?A���̉��Z�q�̃X�P?[�����O�l�Ŋ��B��l��Ԃ�?B
     *
     * @param length ����
     * @return �t��̃X�P?[�����O��{��������
     */
    public double reverseTransform(double length) {
        return length / scale;
    }
}

