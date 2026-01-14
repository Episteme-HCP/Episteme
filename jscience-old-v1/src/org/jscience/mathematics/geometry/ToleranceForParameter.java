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
 * �p�����[�^�l�̋��e�덷��\���N���X�B
 * <p/>
 * JGCL �ł́A
 * �􉽉��Z��i�߂�ۂ̋��e�덷�����ɂ����ĎQ�Ƃ��ׂ��e��̋��e�덷�l��
 * ���Z�� {@link ConditionOfOperation ConditionOfOperation} �Ƃ��āA
 * �܂Ƃ߂ĊǗ�����B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:20 $
 * @see ConditionOfOperation
 * @see ToleranceForDistance
 * @see ToleranceForAngle
 * @see Tolerance
 */

public class ToleranceForParameter extends Tolerance {

    /**
     * �^����ꂽ�l�떗e�덷�l�Ƃ���I�u�W�F�N�g��\�z����B
     * <p/>
     * value �̒l�̎�舵���Ɋւ��ẮA
     * {@link Tolerance#Tolerance(double) �X�[�p�[�N���X�̃R���X�g���N�^}
     * �ɏ�����B
     * </p>
     *
     * @param value �p�����[�^�l�̋��e�덷
     */
    public ToleranceForParameter(double value) {
        super(value);
    }

    /**
     * ���̃p�����[�^�l�̋��e�덷��A
     * �^����ꂽ�Q�����̋Ȑ�̎w��̃p�����[�^�l�ł�
     * �u�Ȑ�̓��̂�v�ɕϊ�����B
     * <p/>
     * ���̃p�����[�^�l�̋��e�덷�̒l�ɁA
     * curve �� t �ɂ�����ڃx�N�g���̑傫����|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param curve �Ȑ�
     * @param t     �p�����[�^�l
     * @return ���̃p�����[�^�l�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance
    toToleranceForDistance(ParametricCurve2D curve, double t) {
        return new
                ToleranceForDistance(this.value() * curve.tangentVector(t).length());
    }

    /**
     * ���̃p�����[�^�l�̋��e�덷��A
     * �^����ꂽ�R�����̋Ȑ�̎w��̃p�����[�^�l�ł�
     * �u�Ȑ�̓��̂�v�ɕϊ�����B
     * <p/>
     * ���̃p�����[�^�l�̋��e�덷�̒l�ɁA
     * curve �� t �ɂ�����ڃx�N�g���̑傫����|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param curve �Ȑ�
     * @param t     �p�����[�^�l
     * @return ���̃p�����[�^�l�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance
    toToleranceForDistance(ParametricCurve3D curve, double t) {
        return new
                ToleranceForDistance(this.value() * curve.tangentVector(t).length());
    }

    /**
     * ���̃p�����[�^�l�̋��e�덷��A
     * �^����ꂽ�R�����̋Ȗʂ̎w��̃p�����[�^�l (u, v) �ł�
     * �uU ���̓��p�����[�^�Ȑ�̓��̂�v�ɕϊ�����B
     * <p/>
     * ���̃p�����[�^�l�̋��e�덷�̒l�ɁA
     * surface �� (u, v) �ɂ����� U ���̈ꎟ�Γ��֐��̑傫����|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param surface �Ȗ�
     * @param u       U ���̃p�����[�^�l
     * @param v       V ���̃p�����[�^�l
     * @return ���̃p�����[�^�l�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance
    toToleranceForDistanceU(ParametricSurface3D surface,
                            double u, double v) {
        return new
                ToleranceForDistance(this.value() * surface.tangentVector(u, v)[0].length());
    }

    /**
     * ���̃p�����[�^�l�̋��e�덷��A
     * �^����ꂽ�R�����̋Ȗʂ̎w��̃p�����[�^�l (u, v) �ł�
     * �uV ���̓��p�����[�^�Ȑ�̓��̂�v�ɕϊ�����B
     * <p/>
     * ���̃p�����[�^�l�̋��e�덷�̒l�ɁA
     * surface �� (u, v) �ɂ����� V ���̈ꎟ�Γ��֐��̑傫����|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param surface �Ȗ�
     * @param u       U ���̃p�����[�^�l
     * @param v       V ���̃p�����[�^�l
     * @return ���̃p�����[�^�l�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance
    toToleranceForDistanceV(ParametricSurface3D surface,
                            double u, double v) {
        return new
                ToleranceForDistance(this.value() * surface.tangentVector(u, v)[0].length());
    }
}

