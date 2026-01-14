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
 * �P���� : ����`?�v�f��?�ɂ���_��\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �`?�v�f��?�ɂ���_��?W�l ({@link Point1D Point1D}) point
 * ��ێ?����?B
 * </p>
 * <p/>
 * point �� null �ł�?\��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:07 $
 */

public abstract class PointOnGeometry1D extends Point1D {
    /**
     * �`?�v�f��?�ɂ���_��?W�l?B
     * <p/>
     * null �ł�?\��Ȃ�?B
     * </p>
     *
     * @serial
     */
    private Point1D point;

    /**
     * �`?�v�f��?�ɂ���_��?W�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     *
     * @param point �`?�v�f?�̓_��?W�l
     */
    protected PointOnGeometry1D(Point1D point) {
        super();
        this.point = point;
    }

    /**
     * ���̓_�� X ?W�l��Ԃ�?B
     *
     * @return �_�� X ?W�l
     */
    public double x() {
        if (point == null) {
            point = coordinates();
        }
        return point.x();
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f��Ԃ���?ۃ?�\�b�h?B
     *
     * @return �x?[�X�ƂȂ�`?�v�f
     */
    public abstract GeometryElement geometry();

    /**
     * �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�_��?W�l��?�߂钊?ۃ?�\�b�h?B
     *
     * @return �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�?�߂��_��?W�l
     */
    abstract Point1D coordinates();
}
