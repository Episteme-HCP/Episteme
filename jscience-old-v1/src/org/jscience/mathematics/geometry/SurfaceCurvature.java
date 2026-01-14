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
 * �Ȗʂ̋ȗ���\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��
 * ��̎�ȗ��̒l (��?��l) principalCurvature1, principalCurvature2
 * ��?��?B
 * ������?A|principalCurvature1| &gt; |principalCurvature2| �ł���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:19 $
 */

public abstract class SurfaceCurvature {
    /**
     * ��ȗ��l1 (?�Βl�̑傫����) ?B
     */
    private double principalCurvature1;

    /**
     * ��ȗ��l2 (?�Βl��?�������) ?B
     */
    private double principalCurvature2;

    /**
     * �t�B?|���h�ɒl��?ݒ肷��?B
     * <p/>
     * |principalCurvature1| &lt; |principalCurvature2| �ł���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param principalCurvature1 ��ȗ�1 (?�Βl�̑傫����)
     * @param principalCurvature2 ��ȗ�2 (?�Βl��?�������)
     * @see InvalidArgumentValueException
     */
    private void setData(double principalCurvature1, double principalCurvature2) {
        if (Math.abs(principalCurvature1) < Math.abs(principalCurvature2))
            throw new InvalidArgumentValueException("|principalCurvature1| is less than |principalCurvature2|.");

        this.principalCurvature1 = principalCurvature1;
        this.principalCurvature2 = principalCurvature2;
    }

    /**
     * ��̎�ȗ��̒l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * |principalCurvature1| &lt; |principalCurvature2| �ł���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param principalCurvature1 ��ȗ�1(?�Βl�̑傫����)
     * @param principalCurvature2 ��ȗ�2(?�Βl��?�������)
     * @see InvalidArgumentValueException
     */
    protected SurfaceCurvature(double principalCurvature1,
                               double principalCurvature2) {
        super();
        setData(principalCurvature1, principalCurvature2);
    }

    /**
     * ��̎�ȗ��̒l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * principalCurvature �͓�̗v�f��?�z��łȂ���΂Ȃ�Ȃ�?B
     * ���̔z��̗v�f��?��� 2 �ȊO�ł���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * �܂�?AprincipalCurvature �� null ��?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * </p>
     * <p/>
     * ?�?��̗v�f��?�Βl����Ԗڂ̗v�f��?�Βl����?�����?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param principalCurvature ��ȗ��̔z��
     * @see InvalidArgumentValueException
     * @see NullArgumentException
     */
    protected SurfaceCurvature(double[] principalCurvature) {
        super();

        if (principalCurvature == null)
            throw new NullArgumentException();

        if (principalCurvature.length != 2)
            throw new InvalidArgumentValueException();

        setData(principalCurvature[0], principalCurvature[1]);
    }

    /**
     * ���̋ȗ��I�u�W�F�N�g�̎�ȗ��l1 (?�Βl�̑傫����) ��Ԃ�?B
     *
     * @return ��ȗ��l1 (?�Βl�̑傫����)
     */
    public double principalCurvature1() {
        return principalCurvature1;
    }

    /**
     * ���̋ȗ��I�u�W�F�N�g�̎�ȗ��l2 (?�Βl��?�������) ��Ԃ�?B
     *
     * @return ��ȗ��l2 (?�Βl��?�������)
     */
    public double principalCurvature2() {
        return principalCurvature2;
    }
}

