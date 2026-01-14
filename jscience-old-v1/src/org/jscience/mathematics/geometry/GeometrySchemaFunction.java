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

import org.jscience.util.FatalException;

/**
 * ISO 10303-42 4.6 geometry_schema function definitions �̈ꕔ��������N���X?B
 * <p/>
 * ISO 10303-42:1994(E) �� 95 �y?[�W�����Q?Ƃ̂���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:58 $
 */

public class GeometrySchemaFunction {
    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private GeometrySchemaFunction() {
    }

    /**
     * �Q���� : refDirection ���w�肳��Ȃ��Ƃ��̃f�t�H���g�l
     */
    public static final Vector2D defaultRefDirection2D = Vector2D.xUnitVector;

    /**
     * �R���� : axis ���w�肳��Ȃ��Ƃ��̃f�t�H���g�l
     */
    public static final Vector3D defaultAxis3D = Vector3D.zUnitVector;

    /**
     * �R���� : refDirection ���w�肳��Ȃ��Ƃ��̃f�t�H���g�l
     */
    public static final Vector3D defaultRefDirection3D = Vector3D.xUnitVector;

    /**
     * ISO 10303-42 4.6.6 ?� (����1) : �^����ꂽ��̃x�N�g������?��K�����ꂽ�Q��������?�߂�?B
     * <p/>
     * axis1, axis2 �쳂ɒ��ⷂ��̒P�ʃx�N�g�� U1, U2 ��?�߂�?B
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f����ꎲ��\���P�ʃx�N�g�� U1?A
     * ��Ԗڂ̗v�f����ꎲ�ɒ��ⷂ��񎲂�\���P�ʃx�N�g�� U2 ��\��?B
     * </p>
     * <p/>
     * axis1 �� null �łȂ����?A
     * axis1 ��P�ʉ������x�N�g���� U1 �Ƃ�?A
     * U1 �𔽎��v���� 90?���]�������x�N�g���� U2 �Ƃ���?B
     * ���̂Ƃ��� axis2 �� null �łȂ�?A
     * axis2 �� U2 �̓�?ς̒l�����ł����?AU2 �� 180?���]������?B
     * </p>
     * <p/>
     * axis1 �� null �ł���?A���� axis2 �� null �łȂ����?A
     * axis2 ��P�ʉ������x�N�g���� U2 �Ƃ�?A
     * U2 �v���� 90?���]�������x�N�g���� U1 �Ƃ���?B
     * </p>
     * <p/>
     * axis1, axis2 ���Ƃ�� null �ł����?A
     * �O�??[�o���� X �����̒P�ʃx�N�g���� U1?A
     * �O�??[�o���� Y �����̒P�ʃx�N�g���� U2 �Ƃ���?B
     * </p>
     * <p/>
     * �Ȃ�?A?�L�Ŗ��炩�Ȃ悤��?A
     * ���̃?�\�b�h���Ԃ�����?A?���n�ƂȂ�?�?�������?B
     * </p>
     *
     * @param axis1 ��ꎲ�̕���K�肷��Q�����x�N�g��
     * @param axis2 ��񎲂̕���K�肷��Q�����x�N�g��
     * @return �Q�����̒��̔z��
     * @see #orthogonalComplement(Vector2D)
     */
    public static Vector2D[] baseAxis(Vector2D axis1, Vector2D axis2) {
        Vector2D[] u = new Vector2D[2];

        if (axis1 != null) {
            u[0] = axis1.unitized();
            u[1] = orthogonalComplement(u[0]);
            if (axis2 != null) {
                if (axis2.dotProduct(u[1]) < 0.0) {
                    u[1] = u[1].reverse();
                }
            }
        } else {
            if (axis2 != null) {
                u[1] = axis2.unitized();
                u[0] = orthogonalComplement(u[1]).reverse();
            } else {
                u[0] = Vector2D.xUnitVector;
                u[1] = Vector2D.yUnitVector;
            }
        }
        return u;
    }

    /**
     * ISO 10303-42 4.6.6 ?� (����2) : �^����ꂽ�O�̃x�N�g������?��K�����ꂽ�R��������?�߂�?B
     * <p/>
     * axis1, axis2, axis3 �쳂Ɍ݂��ɒ��ⷂ�O�̒P�ʃx�N�g�� U1, U2, U3 ��?�߂�?B
     * ���ʂƂ��ē�����z��̗v�f?��� 3 ��?A
     * ?�?��̗v�f����ꎲ��\���P�ʃx�N�g�� U1?A
     * ��Ԗڂ̗v�f����񎲂�\���P�ʃx�N�g�� U2?A
     * ?Ō�̗v�f����O����\���P�ʃx�N�g�� U3 ��\��?B
     * </p>
     * <p/>
     * �܂� axis3 �� null �łȂ����?A
     * axis3 ��P�ʉ������x�N�g���� U3 �Ƃ���?B
     * axis3 �� null �ł����?A
     * �O�??[�o���� Z �����̒P�ʃx�N�g���� U3 �Ƃ���?B
     * </p>
     * <p/>
     * ���� U1 ��ȉ���?��?�Ō��肷��?B
     * <pre>
     * 	U1 = {@link #firstProjAxis(Vector3D,Vector3D) firstProjAxis}(U3, axis1)
     * </pre>
     * </p>
     * <p/>
     * ?Ō�� U2 ��ȉ���?��?�Ō��肷��?B
     * <pre>
     * 	U2 = {@link #secondProjAxis(Vector3D,Vector3D,Vector3D) secondProjAxis}(U3, U1, axis2)
     * </pre>
     * </p>
     * <p/>
     * �Ȃ�?A���̃?�\�b�h��ł�?A
     * firstProjAxis(Vector3D, Vector3D)
     * �����
     * secondProjAxis(Vector3D, Vector3D, Vector3D)
     * �Ŕ�?������O�� catch ���Ă��Ȃ�?B
     * </p>
     * <p/>
     * �Ȃ�?A?�L�Ŗ��炩�Ȃ悤��?A
     * ���̃?�\�b�h���Ԃ�����?A?���n�ƂȂ�?�?�������?B
     * </p>
     *
     * @param axis1 ��ꎲ�̕���K�肷��R�����x�N�g��
     * @param axis2 ��񎲂̕���K�肷��R�����x�N�g��
     * @param axis3 ��O���̕���K�肷��R�����x�N�g��
     * @return �R�����̒��̔z��
     * @see #firstProjAxis(Vector3D,Vector3D)
     * @see #secondProjAxis(Vector3D,Vector3D,Vector3D)
     */
    public static Vector3D[] baseAxis(Vector3D axis1, Vector3D axis2,
                                      Vector3D axis3) {
        Vector3D[] u = new Vector3D[3];

        if (axis3 != null) {
            u[2] = axis3.unitized();
        } else {
            u[2] = Vector3D.zUnitVector;
        }
        u[0] = firstProjAxis(u[2], axis1);
        u[1] = secondProjAxis(u[2], u[0], axis2);

        return u;
    }

    /**
     * ISO 10303-42 4.6.7 ?� : �^����ꂽ�x�N�g������?A��?�?W�n�� X/Y ����\���P�ʃx�N�g����?�߂�?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f����?�?W�n�� X ����\���P�ʃx�N�g��?A
     * ��Ԗڂ̗v�f����?�?W�n�� Y ����\���P�ʃx�N�g����?B
     * </p>
     * <p/>
     * refDirection �� null �łȂ����?A
     * refDirection ��P�ʉ������x�N�g�����?�?W�n�� X ���Ƃ���?B
     * refDirection �� null �ł����?A
     * �O�??[�o���� X �����̒P�ʃx�N�g�����?�?W�n�� X ���Ƃ���?B
     * </p>
     * <p/>
     * ��?�?W�n�� Y �����P�ʃx�N�g����?A
     * ?�ɋ�?�?W�n�� X �����P�ʃx�N�g����
     * �����v���� 90?���]��������̂ł���?B
     * </p>
     *
     * @param refDirection ��?�?W�n�� X ����\���Q�����x�N�g��
     * @return ��?�?W�n�� X/Y ����\���P�ʃx�N�g���̔z��
     */
    public static Vector2D[] build2Axes(Vector2D refDirection) {
        Vector2D[] axes = new Vector2D[2];

        if (refDirection == null) {
            axes[0] = GeometrySchemaFunction.defaultRefDirection2D;
        } else {
            axes[0] = refDirection.unitized();
        }
        axes[1] = orthogonalComplement(axes[0]);
        return axes;
    }

    /**
     * ISO 10303-42 4.6.8 ?� : �^����ꂽ�x�N�g������?A��?�?W�n�� X/Y/Z ����\���P�ʃx�N�g����?�߂�?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 3 ��?A
     * ?�?��̗v�f����?�?W�n�� X ����\���P�ʃx�N�g�� U1?A
     * ��Ԗڂ̗v�f����?�?W�n�� Y ����\���P�ʃx�N�g�� U2?A
     * ?Ō�̗v�f����?�?W�n�� Z ����\���P�ʃx�N�g�� U3 ��?B
     * </p>
     * <p/>
     * �܂�?Aaxis �� null �łȂ����?A
     * axis ��P�ʉ������x�N�g���� U3 �Ƃ���?B
     * axis �� null �ł����?A
     * �O�??[�o���� Z �����̒P�ʃx�N�g���� U3 �Ƃ���?B
     * </p>
     * <p/>
     * ����?AU1 ��ȉ���?��?�Ō��肷��?B
     * <pre>
     * 	U1 = {@link #firstProjAxis(Vector3D,Vector3D) firstProjAxis}(U3, refDirection)
     * </pre>
     * </p>
     * <p/>
     * ?Ō�� U3 �� U1 �̊O?ς�P�ʉ������x�N�g���� U2 �Ƃ���?B
     * </p>
     * <p/>
     * �Ȃ�?A���̃?�\�b�h��ł�?A
     * firstProjAxis(Vector3D, Vector3D)
     * �Ŕ�?������O�� catch ���Ă��Ȃ�?B
     * </p>
     *
     * @param axis         Z ����K�肷��R�����x�N�g��
     * @param refDirection X ����K�肷��Q�����x�N�g��
     * @return X/Y/Z ����\���P�ʃx�N�g���̔z��
     * @see #firstProjAxis(Vector3D,Vector3D)
     */
    public static Vector3D[] buildAxes(Vector3D axis, Vector3D refDirection) {
        Vector3D[] axes = new Vector3D[3];

        if (axis == null) {
            axes[2] = GeometrySchemaFunction.defaultAxis3D;
        } else {
            axes[2] = axis.unitized();
        }
        if (refDirection == null) {
            axes[0] = firstProjAxis(axes[2], GeometrySchemaFunction.defaultRefDirection3D);
        } else {
            axes[0] = firstProjAxis(axes[2], refDirection);
        }
        axes[1] = axes[2].crossProduct(axes[0]).unitized();
        return axes;
    }

    /**
     * ISO 10303-42 4.6.9 ?� : �^����ꂽ�x�N�g����?���� (�����v���) �� 90?���]�������x�N�g����?�߂�?B
     *
     * @param vec �x�N�g��
     * @return ?���� (�����v���) �� 90?���]�������x�N�g��
     */
    public static Vector2D orthogonalComplement(Vector2D vec) {
        return new LiteralVector2D(-vec.y(), vec.x());
    }

    /**
     * ISO 10303-42 4.6.10 ?� : �^����ꂽ�x�N�g���⠂镽�ʂɓ��e�����x�N�g����?�߂�?B
     * <p/>
     * arg ��?AzAxis ��@?���Ƃ��镽�ʂɓ��e?��P�ʉ������x�N�g����Ԃ�?B
     * </p>
     * <p/>
     * zAxis �� null ��?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     * <p/>
     * zAxis ��P�ʉ������x�N�g���� arg �̊O?σx�N�g���̑傫����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?�����?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     * <p/>
     * arg �� null �ł����?A
     * ���̃?�\�b�h�̓Ք�� zAxis �Ɠ�����ł͂Ȃ��x�N�g����I��?A
     * ����� arg �Ƃ��ĉ��Z��?i�߂�?B
     * </p>
     *
     * @param zAxis ���ʂ̖@?�����x�N�g��
     * @param arg   ���e�����x�N�g��
     * @return ���e��̃x�N�g��
     */
    public static Vector3D firstProjAxis(Vector3D zAxis, Vector3D arg) {
        Vector3D z;
        Vector3D v;

        if (zAxis == null) {
            throw new FatalException();
        }
        z = zAxis.unitized();
        if (arg == null) {
            v = Vector3D.xUnitVector;
            if (z.identicalDirection(v)) {
                v = Vector3D.yUnitVector;
            }
        } else {
            ConditionOfOperation condition = ConditionOfOperation.getCondition();
            if (arg.crossProduct(z).norm() <= condition.getToleranceForDistance2()) {
                throw new FatalException();
            }
            v = arg.unitized();
        }
        return v.subtract(z.multiply(v.dotProduct(z))).unitized();
    }

    /**
     * ISO 10303-42 4.6.11 ?� : �^����ꂽ�x�N�g���⠂��̕��ʂɓ��e�����x�N�g����?�߂�?B
     * <p/>
     * arg ��?A
     * zAxis ��@?���Ƃ��镽�ʂɓ��e�������?A�����
     * xAxis ��@?���Ƃ��镽�ʂɓ��e?��P�ʉ������x�N�g����Ԃ�?B
     * </p>
     * <p/>
     * zAxis, xAxis �̂����ꂩ�� null ��?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     * <p/>
     * arg �� null �ł����?A
     * �O�??[�o���� Y �����̒P�ʃx�N�g���� arg �Ƃ��ĉ��Z��?i�߂�?B
     * </p>
     *
     * @param zAxis ���̕��ʂ̖@?�����x�N�g��
     * @param xAxis ���̕��ʂ̖@?�����x�N�g��
     * @param arg   ���e�����x�N�g��
     * @return ���e��̃x�N�g��
     */
    public static Vector3D secondProjAxis(Vector3D zAxis, Vector3D xAxis,
                                          Vector3D arg) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        Vector3D z, x, v;

        if (zAxis == null || xAxis == null) {
            throw new FatalException();
        }

        z = zAxis.unitized();
        x = xAxis.unitized();

        if (arg == null) {
            v = Vector3D.yUnitVector;
        } else {
            v = arg.unitized();
        }
        v = v.subtract(z.multiply(v.dotProduct(z))).unitized();
        return v.subtract(x.multiply(v.dotProduct(x))).unitized();
    }
}
