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
 * �p���?�g���b�N�Ȋ�?��ɂ�����?A�����̃p���??[�^�̒�`���\���N���X?B
 * <p/>
 * �p���??[�^��`���?A�܂����Ȃ�̂ƗL�Ȃ�̂ɕ�������?B
 * JGCL �̋�?��ыȖʂ̕\���ɂ����Ă�?A
 * ���Ȓ�`���?��?��̒l��͖���?A
 * �L�Ȓ�`���?��?��̒l��͗L��?A
 * �ł����̂Ƃ��Ă���?B
 * <br>
 * ���ȃp���??[�^��`��ł�?A
 * �p���??[�^�̗L��ȋ�Ԃ� (-?�, ?�) �ł���?B
 * <br>
 * �L�ȃp���??[�^��`���?A����Ɏ��I�Ȃ�̂Ɣ���I�Ȃ�̂ɕ�������?B
 * <br>
 * �L�Ŏ��I�ȃp���??[�^��`��ł�
 * �p���??[�^�̗L��ȋ�Ԃ� (-?�, ?�) �ł��邪?A
 * �v���C�}���ȗL���� [a, b] ��?��?B
 * (b - a) �ⱂ̒�`���?u���?v�ƌĂ�?B
 * ������?A��̃p���??[�^ t0, t1 ������Ƃ���?A
 * t0 �� t1 ��?��� (b - a) �ł���?�?�?A
 * ��?� F(t) �� t0, t1 ���ꂼ��ɑ΂���l F(t0), F(t1) �͓���ɂȂ�?B
 * <br>
 * �L�Ŕ���I�ȃp���??[�^��`��ł�?A
 * �p���??[�^�̗L��ȋ�Ԃ� [a, b] �ł���?B
 * ���̗L��ȋ�Ԃ�O���p���??[�^�l t �ɑ΂���?AF(t) �͒l��?���Ȃ�?B
 * <br>
 * �Ȃ�?A���ȃp���??[�^��`���?A?�ɔ���I�ł����̂Ƃ���?B
 * </p>
 * <p/>
 * ���̃N���X�ł�?A�p���??[�^��?��?��̕���?i�ނ�̂Ƃ��Ĉ���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:05 $
 * @see ParameterSection
 * @see AbstractParametricCurve
 * @see AbstractParametricSurface
 */

public class ParameterDomain extends java.lang.Object
        implements java.io.Serializable {

    /**
     * �L���ۂ�?B
     *
     * @serial
     */
    private final boolean finite;

    /**
     * ���I���ۂ�?B
     *
     * @serial
     */
    private final boolean periodic;

    /**
     * �p���??[�^�̗L���� (���I��?�?��ɂ�?A�v���C�}���ȗL����) ?B
     *
     * @serial
     */
    private final ParameterSection section;

    /**
     * ���Ŕ���I�ȃI�u�W�F�N�g��?\�z����?B
     */
    public ParameterDomain() {
        super();
        finite = false;
        periodic = false;
        section = null;
    }

    /**
     * �L�ȃI�u�W�F�N�g��?\�z����?B
     * <p/>
     * periodic �� true ��?�?�?A
     * start �� increase �� �v���C�}���ȗL���Ԃ�w�肷��l�Ɖ�߂���?A
     * increase �����̃p���??[�^��`��̎��ƂȂ�?B
     * </p>
     * <p/>
     * increase ��?��̒l�łȂ���΂Ȃ�Ȃ�?B
     * increase �̒l�������邢��?u��?�?ݒ肳��Ă���p���??[�^�l�̋��e��?�?v���?�����?�?��ɂ�
     * InvalidArgumentValueException �̗�O����?�����?B
     * </p>
     *
     * @param periodic ���I���ۂ�
     * @param start    �p���??[�^��Ԃ̊J�n�l
     * @param increase �p���??[�^��Ԃ̑?���l
     * @see ConditionOfOperation
     * @see InvalidArgumentValueException
     */
    public ParameterDomain(boolean periodic,
                           double start, double increase) {
        super();
        double pTol = ConditionOfOperation.getCondition().
                getToleranceForParameter();
        if (increase <= pTol)
            throw new InvalidArgumentValueException("increase is less than the tolerance for parameter");

        finite = true;
        this.periodic = periodic;
        section = new ParameterSection(start, increase);
    }

    /**
     * �L�ȃI�u�W�F�N�g��?\�z����?B
     * <p/>
     * periodic �� true ��?�?�?A
     * section �� �v���C�}���ȗL���Ԃ�w�肷��l�Ɖ�߂���?A
     * section �̑?���l�����̃p���??[�^��`��̎��ƂȂ�?B
     * </p>
     * <p/>
     * section �̑?���l��?��̒l�łȂ���΂Ȃ�Ȃ�?B
     * section �̑?���l�������邢��?u��?�?ݒ肳��Ă���p���??[�^�l�̋��e��?�?v���?�����?�?��ɂ�
     * InvalidArgumentValueException �̗�O����?�����?B
     * </p>
     *
     * @param periodic ���I���ۂ�
     * @param section  �p���??[�^���
     * @see ConditionOfOperation
     * @see InvalidArgumentValueException
     */
    public ParameterDomain(boolean periodic,
                           ParameterSection section) {
        super();
        double pTol = ConditionOfOperation.getCondition().
                getToleranceForParameter();
        if (section.increase() <= pTol)
            throw new InvalidArgumentValueException("increase is less than the tolerance for parameter");

        finite = true;
        this.periodic = periodic;
        this.section = section;
    }

    /**
     * ���̃p���??[�^��`�悪�L���ۂ���Ԃ�?B
     *
     * @return �L�Ȃ�� <code>true</code>?A����Ȃ��� <code>false</code>
     */
    public boolean isFinite() {
        return finite;
    }

    /**
     * ���̃p���??[�^��`�悪�����ۂ���Ԃ�?B
     *
     * @return ���Ȃ�� <code>true</code>?A����Ȃ��� <code>false</code>
     */
    public boolean isInfinite() {
        return !finite;
    }

    /**
     * ���̃p���??[�^��`�悪���I���ۂ���Ԃ�?B
     *
     * @return ���I�Ȃ�� <code>true</code>?A����Ȃ��� <code>false</code>
     */
    public boolean isPeriodic() {
        return periodic;
    }

    /**
     * ���̃p���??[�^��`�悪����I���ۂ���Ԃ�?B
     *
     * @return ����I�ł���� <code>true</code>?A����Ȃ��� <code>false</code>
     */
    public boolean isNonPeriodic() {
        return !periodic;
    }

    /**
     * ���̃p���??[�^��`��̗L��ȃp���??[�^��Ԃ�Ԃ�?B
     * <p/>
     * �p���??[�^��`�悪���I��?�?��ɂ�?A�v���C�}���ȗL���Ԃ�Ԃ�?B
     * </p>
     * <p/>
     * �p���??[�^��`�悪����?�?��ɂ�?Anull ��Ԃ�?B
     * </p>
     *
     * @return �p���??[�^���
     */
    public ParameterSection section() {
        return this.section;
    }

    /**
     * �^���ꂽ�p���??[�^�l��?A���̃p���??[�^��`��ɂ����ėL��ۂ���Ԃ�?B
     * <p/>
     * value ��?A�L��ȃp���??[�^��Ԃ̒[�_�ɂ���?�?��ɂ�?u�L��?v�Ɣ��f����?B
     * </p>
     * <p/>
     * ���e��?��Ƃ���?A��?�?ݒ肳��Ă��鉉�Z?�?��?u�p���??[�^�l�̋��e��?�?v��Q?Ƃ���?B
     * </p>
     *
     * @param value ��?�����p���??[�^�l
     * @return value ���L��Ȃ� <code>true</code>?A����Ȃ��� <code>false</code>
     * @see ConditionOfOperation
     * @see ParameterSection#isValid(double)
     */
    public boolean isValid(double value) {
        if (isInfinite())
            return true;
        else if (isPeriodic())
            return true;
        else
            return section.isValid(value);
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * value �����̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param value ��?�����p���??[�^�l
     * @see ParameterOutOfRange
     * @see #isValid(double)
     */
    public void checkValidity(double value) {
        if (!isValid(value))
            throw new ParameterOutOfRange();
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃ�?A���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * section �̑?���l�͕��ł�?\��Ȃ�?B
     * </p>
     *
     * @param section ��?�����p���??[�^���
     * @throws ParameterOutOfRange           �p���??[�^��Ԃ���`���O��Ă���
     * @throws InvalidArgumentValueException �p���??[�^��Ԃ̒�����?A����蒷��
     *                                       (���I��?�?��̂�)
     * @see #checkValidity(double)
     */
    public void checkValidity(ParameterSection section) {
        checkValidity(section.start());
        checkValidity(section.end());

        if (isPeriodic()) {
            double pTol = ConditionOfOperation.getCondition().getToleranceForParameter();
            if (Math.abs(section.increase()) > Math.abs(section().increase()) + pTol)
                throw new InvalidArgumentValueException();
        }
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̃p���??[�^��`���
     * �v���C�}���Ȏ����?܂�?�񂾒l��Ԃ�?B
     * <p/>
     * �L�Ŏ��I��?�?��ȊO��?A�l�⻂̂܂ܕԂ�?B
     * </p>
     *
     * @param value ?܂�?�ރp���??[�^�l
     * @return ?܂�?�񂾒l
     */
    public double wrap(double value) {
        if (isInfinite() || isNonPeriodic())
            return value;
        else {
            double low, inc, n;

            low = section.lower();
            inc = Math.abs(section.increase());

            if (value < low) {
                n = Math.floor((low - value) / inc) + 1;
                return value + (n * inc);
            } else if ((value > section.upper()) != true) {
                return value;
            } else {
                n = Math.floor((value - low) / inc);
                return value - (n * inc);
            }
        }
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̃p���??[�^��`���
     * �L��ȃp���??[�^��ԓ�Ɏ�܂�悤��?��ϊ������l��Ԃ�?B
     * <p/>
     * �L�Ŕ���I��?�?��ȊO��?A�l�⻂̂܂ܕԂ�?B
     * </p>
     *
     * @param value �ϊ�����p���??[�^�l
     * @return ��?��ϊ�������̒l
     */
    public double force(double value) {
        if (isInfinite() || isPeriodic()) {
            return value;
        } else {
            double low, high;

            low = section.lower();
            high = section.upper();

            if (value < low)
                return low;
            else if (high < value)
                return high;
            else
                return value;
        }
    }
}
