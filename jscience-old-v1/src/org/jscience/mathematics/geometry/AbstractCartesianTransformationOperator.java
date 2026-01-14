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

