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
 * ��?�̋ȗ���\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��
 * �ȗ��̒l (��?��l) curvature
 * ��?��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public abstract class CurveCurvature {
    /**
     * �ȗ��̒l?B
     */
    private final double curvature;

    /**
     * �ȗ��̒l��w�肵�ăI�u�W�F�N�g��?\�z����?B
     *
     * @param curvature �ȗ��̒l
     */
    protected CurveCurvature(double curvature) {
        super();
        this.curvature = curvature;
    }

    /**
     * ���̋ȗ��I�u�W�F�N�g�̋ȗ��̒l��Ԃ�?B
     *
     * @return �ȗ��̒l
     */
    public double curvature() {
        return curvature;
    }

    /**
     * �^����ꂽ��̋ȗ��̒l������Ƃ݂Ȃ��邩�ۂ���Ԃ�?B
     * <p/>
     * a, b ���ꂼ��̋t?���?���?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȓ�ł����?A
     * a �� b �͓���̋ȗ��ł����̂Ƃ���?B
     * </p>
     *
     * @param a �ȗ��̒l1
     * @param b �ȗ��̒l2
     * @return a �� b �� ����̋ȗ��Ƃ݂Ȃ���� true?A�����łȂ���� false
     */
    static boolean identical(double a, double b) {
        if (GeometryUtils.isReciprocatable(a)) {
            if (!GeometryUtils.isReciprocatable(b))
                return false;

            double a_rad = 1.0 / a;
            double b_rad = 1.0 / b;
            double tol_d = ConditionOfOperation.getCondition().getToleranceForDistance();
            if (Math.abs(a_rad - b_rad) > tol_d)
                return false;
        } else {
            if (GeometryUtils.isReciprocatable(b))
                return false;
        }

        return true;
    }
}

