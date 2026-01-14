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
 * �p�x�̋��e�덷��\���N���X�B
 * <p/>
 * JGCL �ł́A
 * �􉽉��Z��i�߂�ۂ̋��e�덷�����ɂ����ĎQ�Ƃ��ׂ��e��̋��e�덷�l��
 * ���Z�� {@link ConditionOfOperation ConditionOfOperation} �Ƃ��āA
 * �܂Ƃ߂ĊǗ�����B
 * </p>
 * <p/>
 * �����ł̊p�x�̒P�ʂ́u�ʓx (���W�A��) �v�ł����̂Ƃ���B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:20 $
 * @see ConditionOfOperation
 * @see ToleranceForDistance
 * @see ToleranceForParameter
 * @see Tolerance
 */

public class ToleranceForAngle extends Tolerance {

    /**
     * �^����ꂽ�l�떗e�덷�l�Ƃ���I�u�W�F�N�g��\�z����B
     * <p/>
     * value �̒l�̎�舵���Ɋւ��ẮA
     * {@link Tolerance#Tolerance(double) �X�[�p�[�N���X�̃R���X�g���N�^}
     * �ɏ�����B
     * </p>
     *
     * @param value �p�x�̋��e�덷�l
     */
    public ToleranceForAngle(double value) {
        super(value);
    }

    /**
     * ���̊p�x�̋��e�덷��A
     * �^����ꂽ���a�̉~���ł�
     * �u�����̍��v�ɕϊ�����B
     * <p/>
     * ���̊p�x�̋��e�덷�̒l�ɁA
     * ���a radius �̒l��|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param radius ���a
     * @return ���̊p�x�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance toToleranceForDistance(double radius) {
        return new ToleranceForDistance(this.value() * radius);
    }
}

