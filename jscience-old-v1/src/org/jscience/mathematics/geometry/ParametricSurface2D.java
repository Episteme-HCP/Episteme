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
 * �p���?�g���b�N��2D�Ȗʂ�\����?ۃN���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/28 21:47:45 $
 */
abstract class ParametricSurface2D extends AbstractParametricSurface {
/**
     * uDomain, vDomain �� null
     * �Ƃ��ăI�u�W�F�N�g��?\�z����
     */
    protected ParametricSurface2D() {
        super();
    }

    /**
     * �􉽗v�f�̎�����Ԃ�
     *
     * @return �􉽗v�f�̎���?�
     */
    public int dimension() {
        return 2;
    }

    /**
     * �Q�������ۂ���Ԃ�
     *
     * @return �Q�������ۂ���Ԃ� (true ��Ԃ�)
     */
    public boolean is2D() {
        return true;
    }

    /**
     * �^����ꂽ�p���??[�^�ł�?W�l��?�߂�
     *
     * @param uParam U���p���??[�^
     * @param vParam V���p���??[�^
     *
     * @return ?W�l
     *
     * @see Point2D
     */
    public abstract Point2D coordinates(double uParam, double vParam);

    /**
     * �^����ꂽ�p���??[�^�ł�?ڃx�N�g����?�߂�
     *
     * @param uParam U���p���??[�^
     * @param vParam V���p���??[�^
     *
     * @return ?ڃx�N�g��[0]:U����?ڃx�N�g��
     *         [1]:V����?ڃx�N�g��
     *
     * @see Vector2D
     */
    public abstract Vector2D[] tangentVector(double uParam, double vParam);
}
// end of file
