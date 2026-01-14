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

import java.io.PrintWriter;

/**
 * �R���� : ��ȖʊԂ̌�?��\���N���X?B
 * <p/>
 * ���̃N���X�ɓWL�ȑ�?���\���t�B?[���h�͓BɂȂ�?B
 * ��?��\�����߂ɕK�v�ȋ�?��?��Ȃǂ�ێ?����t�B?[���h�ɂ��Ă�?A
 * {@link SurfaceCurve3D �X?[�p?[�N���X�̉�?�} ��Q?�?B
 * �Ȃ�?A��?�I�u�W�F�N�g��?A
 * �X?[�p?[�N���X SurfaceCurve3D �Œ�`����Ă���C���X�^���X�t�B?[���h��
 * basisSurface2, curve2d2 �ט�p����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:13 $
 */

public class IntersectionCurve3D extends SurfaceCurve3D
        implements SurfaceSurfaceInterference3D {
    /**
     * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link SurfaceCurve3D#SurfaceCurve3D(ParametricCurve3D,ParametricSurface3D,ParametricCurve2D,ParametricSurface3D,ParametricCurve2D,int)
     * super}(curve3d, basisSurface1, curve2d1, basisSurface2, curve2d2, masterRepresentation)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * basisSurface1 �� basisSurface2 �������Ȗʂł���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param curve3d              ��?�̎O�����\��
     * @param basisSurface1        ��?��?�߂����̋Ȗ�
     * @param curve2d1             ��?�� basisSurface1 �ɂ�����񎟌��\��
     * @param basisSurface2        ��?��?�߂�����̋Ȗ�
     * @param curve2d2             ��?�� basisSurface2 �ɂ�����񎟌��\��
     * @param masterRepresentation �ǂ̋�?�\����D?悷�邩����?� (PreferredSurfaceCurveRepresentation)
     * @see PreferredSurfaceCurveRepresentation
     * @see InvalidArgumentValueException
     */
    public IntersectionCurve3D(ParametricCurve3D curve3d,
                               ParametricSurface3D basisSurface1,
                               ParametricCurve2D curve2d1,
                               ParametricSurface3D basisSurface2,
                               ParametricCurve2D curve2d2,
                               int masterRepresentation) {
        super(curve3d, basisSurface1, curve2d1, basisSurface2, curve2d2, masterRepresentation);
        if (basisSurface1 == basisSurface2)
            throw new InvalidArgumentValueException();
    }

    /**
     * ���̊�?���_�ł��邩�ۂ���Ԃ�?B
     *
     * @return ��_�ł͂Ȃ���?�Ȃ̂�?A?�� false
     * @see #isIntersectionCurve()
     */
    public boolean isIntersectionPoint() {
        return false;
    }

    /**
     * ���̊�?���?�ł��邩�ۂ���Ԃ�?B
     *
     * @return ��?�Ȃ̂�?A?�� true
     * @see #isIntersectionPoint()
     */
    public boolean isIntersectionCurve() {
        return true;
    }

    /**
     * ���̊�?��_�ɕϊ�����?B
     * <p/>
     * ��?���_�ɕϊ����邱�Ƃ͂ł��Ȃ��̂� null ��Ԃ�?B
     * </p>
     *
     * @return ?�� null
     */
    public IntersectionPoint3D toIntersectionPoint() {
        return null;
    }

    /**
     * ���̊�?��?�ɕϊ�����?B
     * <p/>
     * ������?g��Ԃ�?B
     * </p>
     *
     * @return ������?g
     */
    public IntersectionCurve3D toIntersectionCurve() {
        return this;
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve3D#INTERSECTION_CURVE_3D ParametricCurve3D.INTERSECTION_CURVE_3D}
     */
    int type() {
        return INTERSECTION_CURVE_3D;
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     * @see GeometryElement
     */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);

        writer.println(indent_tab + getClassName());
        if (curve3d() != null) {
            writer.println(indent_tab + "\tcurve3d");
            curve3d().output(writer, indent + 2);
        }
        writer.println(indent_tab + "\tbasisSurface1");
        basisSurface1().output(writer, indent + 2);
        if (curve2d1() != null) {
            writer.println(indent_tab + "\tcurve2d1");
            curve2d1().output(writer, indent + 2);
        }
        writer.println(indent_tab + "\tbasisSurface2");
        basisSurface2().output(writer, indent + 2);
        if (curve2d2() != null) {
            writer.println(indent_tab + "\tcurve2d2");
            curve2d2().output(writer, indent + 2);
        }
        writer.println(indent_tab + "\tmasterRepresentation\t"
                + PreferredSurfaceCurveRepresentation.toString(masterRepresentation()));
        writer.println(indent_tab + "End");
    }
}
