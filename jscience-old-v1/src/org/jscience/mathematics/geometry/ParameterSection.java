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
 * �p�����[�^�l�̑��ݔ͈͂��p�����[�^��Ԃ�\���N���X�B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:06 $
 * @see ParameterDomain
 */

public class ParameterSection extends java.lang.Object
        implements java.io.Serializable {

    /**
     * �J�n�l�B
     *
     * @serial
     */
    private final double start;

    /**
     * �����l�B
     *
     * @serial
     */
    private final double increase;

    /**
     * �I���l (end = start + increase) �B
     *
     * @serial
     */
    private final double end;

    /**
     * �J�n�l�Ƒ����l��w�肵�ăI�u�W�F�N�g��\�z����B
     * <p/>
     * increase �̒l�̓[�����邢�͕��ł��BĂ�\��Ȃ��B
     * </p>
     *
     * @param start    �J�n�l
     * @param increase �����l
     */
    public ParameterSection(double start, double increase) {
        super();
        this.start = start;
        this.increase = increase;
        end = start + increase;
    }

    /**
     * �J�n�l��Ԃ��B
     *
     * @return �J�n�l
     */
    public double start() {
        return this.start;
    }

    /**
     * �I���l��Ԃ��B
     *
     * @return �I���l
     */
    public double end() {
        return this.end;
    }

    /**
     * �����l��Ԃ��B
     *
     * @return �����l
     */
    public double increase() {
        return this.increase;
    }

    /**
     * �����l�̐�Βl��Ԃ��B
     *
     * @return �����l�̐�Βl
     */
    public double absIncrease() {
        return Math.abs(this.increase);
    }

    /**
     * ���l��Ԃ��B
     *
     * @return ���l
     */
    public double lower() {
        if (increase() < 0)
            return end();
        else
            return start();
    }

    /**
     * ��l��Ԃ��B
     *
     * @return ��l
     */
    public double upper() {
        if (increase() >= 0)
            return end();
        else
            return start();
    }

    /**
     * ��𔽓]�������I�u�W�F�N�g��Ԃ��B
     * <p/>
     * end ���� start �֌�I�u�W�F�N�g��Ԃ��B
     * </p>
     *
     * @return ��𔽓]�������I�u�W�F�N�g
     */
    public ParameterSection reverse() {
        return new ParameterSection(this.end(), (-this.increase()));
    }

    /**
     * �͈͂������ŁA�����l�����̃I�u�W�F�N�g��Ԃ��B
     *
     * @return �͈͂������ŁA�����l�����̃I�u�W�F�N�g
     */
    public ParameterSection positiveIncrease() {
        return (this.increase() < 0.0) ? this.reverse() : this;
    }

    /**
     * �^����ꂽ�p�����[�^�l���A���̃p�����[�^��Ԃ̓Ѥ�ɂ��邩�ۂ���Ԃ��B
     * <p/>
     * value ���A�p�����[�^��Ԃ̒[�_�ɂ���ꍇ�ɂ́u�Ѥ�v�Ɣ��f����B
     * </p>
     * <p/>
     * ���e�덷�Ƃ��āA���ݐݒ肳��Ă��鉉�Z�쏂́u�p�����[�^�l�̋��e�덷�v��Q�Ƃ���B
     * </p>
     *
     * @param value ��������p�����[�^�l
     * @return value ���L��ł����(���l�Ə�l�̊Ԃɂ����)
     *         <code>true</code>�A����Ȃ��� <code>false</code>
     * @see ConditionOfOperation
     */
    public boolean isValid(double value) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double sval = start();
        double eval = end();
        double pTol = condition.getToleranceForParameter();

        if (increase() >= 0) {
            return sval - pTol <= value && value <= eval + pTol;
        } else {
            return eval - pTol <= value && value <= sval + pTol;
        }
    }

    /**
     * �^����ꂽ��Ԃ��A���̋�ԂƓ������ƌ��Ȃ��邩�ۂ���Ԃ��B
     * <p/>
     * ���e�덷�Ƃ��āA���ݐݒ肳��Ă��鉉�Z�쏂́u�p�����[�^�l�̋��e�덷�v��Q�Ƃ���B
     * </p>
     *
     * @param mate ���̋��
     * @return �p�����[�^�l�̋��e�덷��œ������Ȃ�� true�A�����łȂ���� false
     * @see ConditionOfOperation
     */
    public boolean identical(ParameterSection mate) {
        if (this == mate)
            return true;

        double pTol =
                ConditionOfOperation.getCondition().getToleranceForParameter();

        if (Math.abs(this.start() - mate.start()) > pTol)
            return false;

        if (Math.abs(this.increase() - mate.increase()) > pTol)
            return false;

        return true;
    }
}

