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
 * �Q���� : �Ȑ�̓��֐���\���N���X�B
 * <p/>
 * ���̃N���X�̃C���X�^���X�́A
 * ����Ȑ� P �̂���p�����[�^�l t �ɂ�����
 * �뎟/�ꎟ/�񎟂̓��֐��̒l d0D/d1D/d2D ��ێ�����B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public class CurveDerivative2D extends CurveDerivative {
    /**
     * �Ȑ��̓_ (�뎟���֐��l) �B
     */
    private final Point2D d0D;

    /**
     * �ꎟ���֐��l�B
     */
    private final Vector2D d1D;

    /**
     * �񎟓��֐��l�B
     */
    private final Vector2D d2D;

    /**
     * �뎟/�ꎟ/�񎟂̓��֐��̒l��^���ăI�u�W�F�N�g��\�z����B
     *
     * @param d0D �Ȑ��̓_ (�뎟���֐��l)
     * @param d1D �ꎟ���֐��l
     * @param d2D �񎟓��֐��l
     */
    CurveDerivative2D(Point2D d0D,
                      Vector2D d1D,
                      Vector2D d2D) {
        super();
        this.d0D = d0D;
        this.d1D = d1D;
        this.d2D = d2D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̋Ȑ��̓_ (�뎟���֐��l) ��Ԃ��B
     *
     * @return �Ȑ��̓_ (�뎟���֐��l)
     */
    public Point2D d0D() {
        return d0D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̈ꎟ���֐���Ԃ��B
     *
     * @return �ꎟ���֐��l
     */
    public Vector2D d1D() {
        return d1D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̓񎟓��֐���Ԃ��B
     *
     * @return �񎟓��֐��l
     */
    public Vector2D d2D() {
        return d2D;
    }

    /**
     * �f�o�b�O�p���C���v���O�����B
     */
    public static void main(String[] args) {
        Point2D d0D = Point2D.origin;
        Vector2D d1D = Vector2D.xUnitVector;
        Vector2D d2D = Vector2D.yUnitVector;
        CurveDerivative2D deriv;

        deriv = new CurveDerivative2D(d0D, d1D, d2D);
        System.out.println(deriv);
    }
}

