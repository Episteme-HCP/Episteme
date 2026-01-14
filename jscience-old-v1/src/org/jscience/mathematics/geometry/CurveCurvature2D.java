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
 * �Q���� : ��?�̋ȗ���\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��
 * ��@?�x�N�g�� normal
 * ��?��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public class CurveCurvature2D extends CurveCurvature {
    /**
     * ��@?�x�N�g��?B
     */
    private final Vector2D normal;

    /**
     * �ȗ��Ǝ�@?�x�N�g����^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param curvature �ȗ�
     * @param normal    ��@?�x�N�g��
     */
    CurveCurvature2D(double curvature,
                     Vector2D normal) {
        super(curvature);

        if (normal == null)
            throw new NullArgumentException();
        this.normal = normal.unitized();
    }

    /**
     * ���̋ȗ��I�u�W�F�N�g�̎�@?�x�N�g����Ԃ�?B
     *
     * @return ��@?�x�N�g��
     * @see Vector2D
     */
    public Vector2D normal() {
        return normal;
    }

    /**
     * ���̋ȗ��I�u�W�F�N�g��?A�^����ꂽ�ȗ��I�u�W�F�N�g������Ƃ݂Ȃ��邩�ۂ���Ԃ�?B
     * <p/>
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ��̋ȗ��I�u�W�F�N�g�̎�@?�x�N�g���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ��̋ȗ��I�u�W�F�N�g�̋ȗ��̒l�̋t?���?��������̋��e��?��ȓ�ł����?A
     * ��̋ȗ��I�u�W�F�N�g�͓���ł����̂Ƃ���?B
     * </p>
     *
     * @param mate �����?ۂ̋ȗ��I�u�W�F�N�g
     * @return this �� mate ������̋ȗ��Ƃ݂Ȃ���� true?A�����łȂ���� false
     */
    public boolean identical(CurveCurvature2D mate) {
        if (!this.normal().identicalDirection(mate.normal()))
            return false;

        return CurveCurvature.identical(this.curvature(), mate.curvature());
    }
}

