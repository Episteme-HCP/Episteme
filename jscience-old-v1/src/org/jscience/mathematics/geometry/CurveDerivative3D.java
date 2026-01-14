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
 * �R���� : �Ȑ�̓��֐��l��\���N���X�B
 * <p/>
 * ���̃N���X�̃C���X�^���X�́A
 * ����Ȑ� P �̂���p�����[�^�l t �ɂ�����
 * �뎟/�ꎟ/��/�O���̓��֐��̒l d0D/d1D/d2D/d3D ��ێ�����B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public class CurveDerivative3D extends CurveDerivative {
    /**
     * �Ȑ��̓_ (�뎟���֐��l) �B
     */
    private final Point3D d0D;

    /**
     * �ꎟ���֐��l�B
     */
    private final Vector3D d1D;

    /**
     * �񎟓��֐��l�B
     */
    private final Vector3D d2D;

    /**
     * �O�����֐��l�B
     */
    private final Vector3D d3D;

    /**
     * �뎟/�ꎟ/��/�O���̓��֐��̒l��^���ăI�u�W�F�N�g��\�z����B
     *
     * @param d0D �Ȑ��̓_ (�뎟���֐��l)
     * @param d1D �ꎟ���֐��l
     * @param d2D �񎟓��֐��l
     * @param d3D �O�����֐��l
     */
    CurveDerivative3D(Point3D d0D,
                      Vector3D d1D,
                      Vector3D d2D,
                      Vector3D d3D) {
        super();
        this.d0D = d0D;
        this.d1D = d1D;
        this.d2D = d2D;
        this.d3D = d3D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̋Ȑ��̓_ (�뎟���֐��l) ��Ԃ��B
     *
     * @return �Ȑ��̓_ (�뎟���֐��l)
     */
    public Point3D d0D() {
        return d0D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̈ꎟ���֐���Ԃ��B
     *
     * @return �ꎟ���֐��l
     */
    public Vector3D d1D() {
        return d1D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̓񎟓��֐���Ԃ��B
     *
     * @return �񎟓��֐��l
     */
    public Vector3D d2D() {
        return d2D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̎O�����֐���Ԃ��B
     *
     * @return �O�����֐��l
     */
    public Vector3D d3D() {
        return d3D;
    }
}

