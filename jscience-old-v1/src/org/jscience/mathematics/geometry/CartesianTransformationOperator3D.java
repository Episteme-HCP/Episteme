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
 * �R���� : �􉽓I�ȕϊ���?s�Ȃ����Z�q��\���N���X?B
 * <p/>
 * �􉽓I�ȕϊ���?A��?s�ړ�?A��]�ړ�?A�~��?[�����O?A�ψ�ȃX�P?[�����O
 * ��?\?������?B
 * ���̕ϊ��ł�?A�ϊ��O�ƕϊ���ŔC�ӂ̓�_�Ԃ̋����̔�͈��ł���?B
 * </p>
 * <p/>
 * �܂�?A�P�ʉ����ꂽ��̃x�N�g�� U1, U2, U3 ��?A�e����`���钼��?s�� T ��?l����?B
 * U1, U2, U3 �݂͌��ɒ��ⷂ�x�N�g���ł���?B
 * �����̒P�ʃx�N�g����?A���x�N�g�� axis1, axis2, axis3 ���
 * {@link GeometrySchemaFunction#baseAxis(Vector3D,Vector3D,Vector3D)
 * GeometrySchemaFunction.baseAxis}(axis1, axis2, axis3) �Ōv�Z�����?B
 * T ��?s�񎮂� -1 ��?�?��ɂ�?AT �⻂�?\?��v�f�Ƃ��Ċ܂ޕϊ��̓~��?[�����O��܂�?B
 * <br>
 * �􉽓I�ȕϊ���?AT �ɉB���?A
 * ��?s�ړ��̗ʂ숂߂��?��I�Ȍ��_ A (localOrigin)
 * ����� �X�P?[�����O�̗ʂ숂߂�l S
 * �Œ�`�����?B
 * </p>
 * <p/>
 * �ȉ��̋L?q��?AA.b �͓_ (���邢�̓x�N�g��) A �� b ?�����\��?B
 * </p>
 * <p/>
 * �_ P �̕ϊ���?A�ϊ���̓_�� Q �Ƃ���?A�ȉ��Œ�`�����?B
 * <pre>
 * 	Q.x = A.x + S * (P.x * U1.x + P.y * U2.x + P.z * U3.x)
 * 	Q.y = A.y + S * (P.x * U1.y + P.y * U2.y + P.z * U3.y)
 * 	Q.z = A.z + S * (P.x * U1.z + P.y * U2.z + P.z * U3.z)
 * </pre>
 * </p>
 * <p/>
 * �x�N�g�� V �̕ϊ���?A�ϊ���̃x�N�g���� W �Ƃ���?A�ȉ��Œ�`�����?B
 * <pre>
 * 	W.x = S * (V.x * U1.x + V.y * U2.x + V.z * U3.x)
 * 	W.y = S * (V.x * U1.y + V.y * U2.y + V.z * U3.y)
 * 	W.z = S * (V.x * U1.z + V.y * U2.z + V.z * U3.z)
 * </pre>
 * </p>
 * <p/>
 * ���� L �̕ϊ���?A�ϊ���̒����� M �Ƃ���?A�ȉ��Œ�`�����?B
 * <pre>
 * 	M = S * L
 * </pre>
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:07 $
 * @see CartesianTransformationOperator2D
 */

public class CartesianTransformationOperator3D extends AbstractCartesianTransformationOperator {
    /**
     * �ϊ��̑�ꎲ U1 ��K�肷��x�N�g��?B
     *
     * @serial
     * @see GeometrySchemaFunction#baseAxis(Vector3D,Vector3D,Vector3D)
     */
    private Vector3D axis1;

    /**
     * �ϊ��̑�� U2 ��K�肷��x�N�g��
     *
     * @serial
     * @see GeometrySchemaFunction#baseAxis(Vector3D,Vector3D,Vector3D)
     */
    private Vector3D axis2;

    /**
     * �ϊ��̑�O�� U3 ��K�肷��x�N�g��
     *
     * @serial
     * @see GeometrySchemaFunction#baseAxis(Vector3D,Vector3D,Vector3D)
     */
    private Vector3D axis3;

    /**
     * ��?s�ړ��̗ʂ�K�肷���?��I�Ȍ��_ A?B
     * <p/>
     * �_�̕ϊ��ł�?A���I�Ȍ��_ (0, 0, 0) ���炱�̓_�܂ł̕�?s�ړ���܂�?B
     * </p>
     *
     * @serial
     */
    private Point3D localOrigin;

    /**
     * �ϊ��̎��ƂȂ�P�ʃx�N�g�� U1, U2, U3?B
     * <p/>
     * ����?s�� T �̊e���?B
     * </p>
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * </p>
     *
     * @serial
     */
    private Vector3D u[];

    /**
     * �e�t�B?[���h�̒l��?ڎw�肵��?A�I�u�W�F�N�g��?\�z����?B
     * <p/>
     * axis1 �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * axis2 �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * axis3 �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * localOgirin �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * scale �̒l��?��łȂ���΂Ȃ�Ȃ�?B
     * scale �̒l��?A��?�?ݒ肳��Ă��鉉�Z?�?��
     * �����̋��e��?��ȉ���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param axis1       �ϊ��̑�ꎲ U1 ��K�肷��x�N�g��
     * @param axis2       �ϊ��̑�� U2 ��K�肷��x�N�g��
     * @param axis3       �ϊ��̑�O�� U3 ��K�肷��x�N�g��
     * @param localOrigin ��?s�ړ��̗ʂ숂߂��?��I�Ȍ��_ A
     * @param scale       �X�P?[�����O�ʂ숂߂�l S
     * @see InvalidArgumentValueException
     */
    public CartesianTransformationOperator3D(Vector3D axis1, Vector3D axis2,
                                             Vector3D axis3, Point3D localOrigin,
                                             double scale) {
        super(scale);
        setFields(axis1, axis2, axis3, localOrigin);
    }

    /**
     * ��?�?W�n (�z�u?��) ������I��?W�n�ւ̕ϊ���\���I�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �̌��_/X ��/Y��/Z ���⻂ꂼ��
     * localOrigin/axis1/axis2/axis3 �Ƃ���?B
     * </p>
     * <p/>
     * position �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * scale �̒l��?��łȂ���΂Ȃ�Ȃ�?B
     * scale �̒l��?A��?�?ݒ肳��Ă��鉉�Z?�?��
     * �����̋��e��?��ȉ���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param position ��?�?W�n (�z�u?��)
     * @param scale    �X�P?[�����O�l
     * @see InvalidArgumentValueException
     */
    public CartesianTransformationOperator3D(Axis2Placement3D position, double scale) {
        super(scale);
        fromAxis2Placement(position);
    }

    /**
     * �X�P?[�����O�l�� 1 �Ƃ���?A
     * ��?�?W�n (�z�u?��) ������I��?W�n�ւ̕ϊ���\���I�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �̌��_/X ��/Y��/Z ���⻂ꂼ��
     * localOrigin/axis1/axis2/axis3 �Ƃ���?B
     * </p>
     * <p/>
     * position �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param position ��?�?W�n (�z�u?��)
     * @see InvalidArgumentValueException
     */
    public CartesianTransformationOperator3D(Axis2Placement3D position) {
        super(1.0);
        fromAxis2Placement(position);
    }

    /**
     * �e�t�B?[���h�̒l��?ݒ肷��?B
     * <p/>
     * axis1 �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * axis2 �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * axis3 �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * localOgirin �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param axis1       �ϊ��̑�ꎲ U1 ��K�肷��x�N�g��
     * @param axis2       �ϊ��̑�� U2 ��K�肷��x�N�g��
     * @param axis3       �ϊ��̑�O�� U3 ��K�肷��x�N�g��
     * @param localOrigin ��?s�ړ��̗ʂ숂߂��?��I�Ȍ��_ A
     * @see InvalidArgumentValueException
     */
    private void setFields(Vector3D axis1,
                           Vector3D axis2,
                           Vector3D axis3,
                           Point3D localOrigin) {
        if (localOrigin == null) {
            throw new InvalidArgumentValueException();
        }
        // check more ???
        this.localOrigin = localOrigin;
        this.axis1 = axis1;
        this.axis2 = axis2;
        this.axis3 = axis3;
    }

    /**
     * ��?�?W�n(�z�u?��) ������I��?W�n�ւ̕ϊ���\���悤��
     * �e�t�B?[���h�̒l��?ݒ肷��?B
     * <p/>
     * position �̌��_/X ��/Y��/Z ���⻂ꂼ��
     * localOrigin/axis1/axis2/axis3 �Ƃ���?B
     * </p>
     * <p/>
     * position �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param position ��?�?W�n (�z�u?��)
     * @see InvalidArgumentValueException
     */
    private void fromAxis2Placement(Axis2Placement3D position) {
        if (position == null) {
            throw new InvalidArgumentValueException();
        }
        u = position.axes();
        this.localOrigin = position.location();
        this.axis1 = u1();
        this.axis2 = u2();
        this.axis3 = u3();
    }

    /**
     * ���̉��Z�q�̎�����Ԃ�?B
     * <p/>
     * ?�� 3 ��Ԃ�?B
     * </p>
     *
     * @return �R�����Ȃ̂�?A?�� 3
     */
    public int dimension() {
        return 3;
    }

    /**
     * ���̉��Z�q�̕ϊ��̑�ꎲ U1 ��K�肷��x�N�g����Ԃ�?B
     *
     * @return �ϊ��̑�ꎲ U1 ��K�肷��x�N�g��
     */
    public Vector3D axis1() {
        return axis1;
    }

    /**
     * ���̉��Z�q�̕ϊ��̑�� U2 ��K�肷��x�N�g����Ԃ�?B
     *
     * @return �ϊ��̑�� U2 ��K�肷��x�N�g��
     */
    public Vector3D axis2() {
        return axis2;
    }

    /**
     * ���̉��Z�q�̕ϊ��̑�O�� U3 ��K�肷��x�N�g����Ԃ�?B
     *
     * @return �ϊ��̑�O�� U3 ��K�肷��x�N�g��
     */
    public Vector3D axis3() {
        return axis3;
    }

    /**
     * ���̉��Z�q�̕�?s�ړ��̗ʂ�K�肷���?��I�Ȍ��_��Ԃ�?B
     *
     * @return ��?s�ړ��̗ʂ�K�肷���?��I�Ȍ��_
     */
    public Point3D localOrigin() {
        return localOrigin;
    }

    /**
     * ���̉��Z�q�̕ϊ��̑�ꎲ�ƂȂ�P�ʃx�N�g�� U1 ��Ԃ�?B
     *
     * @return �ϊ��̑�ꎲ�ƂȂ�P�ʃx�N�g�� U1
     */
    public Vector3D u1() {
        if (u == null)
            u();
        return u[0];
    }

    /**
     * ���̉��Z�q�̕ϊ��̑�񎲂ƂȂ�P�ʃx�N�g�� U2 ��Ԃ�?B
     *
     * @return �ϊ��̑�񎲂ƂȂ�P�ʃx�N�g�� U2
     */
    public Vector3D u2() {
        if (u == null)
            u();
        return u[1];
    }

    /**
     * ���̉��Z�q�̕ϊ��̑�O���ƂȂ�P�ʃx�N�g�� U3 ��Ԃ�?B
     *
     * @return �ϊ��̑�O���ƂȂ�P�ʃx�N�g�� U3
     */
    public Vector3D u3() {
        if (u == null)
            u();
        return u[2];
    }

    /**
     * ���̉��Z�q�̕ϊ��̎��ƂȂ�P�ʃx�N�g�� U1, U2, U3 ��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ�z��̗v�f?��� 3 ��?A
     * ?�?��̗v�f�� U1?A��Ԗڂ̗v�f�� U2?A?Ō�̗v�f�� U3 ��܂�?B
     * </p>
     *
     * @return �ϊ��̎��ƂȂ�P�ʃx�N�g���̔z��
     */
    public Vector3D[] u() {
        if (u == null) {
            u = GeometrySchemaFunction.baseAxis(axis1, axis2, axis3);
        }
        return (Vector3D[]) u.clone();
    }

    /**
     * �^����ꂽ�x�N�g���ⱂ̉��Z�q�ŕϊ�����?B
     *
     * @param vector �x�N�g��
     * @return �ϊ���̃x�N�g��
     */
    public Vector3D transform(Vector3D vector) {
        double x, y, z;

        x = scale() * (vector.x() * u1().x() + vector.y() * u2().x() + vector.z() * u3().x());
        y = scale() * (vector.x() * u1().y() + vector.y() * u2().y() + vector.z() * u3().y());
        z = scale() * (vector.x() * u1().z() + vector.y() * u2().z() + vector.z() * u3().z());
        return new LiteralVector3D(x, y, z);
    }

    /**
     * �^����ꂽ�_�ⱂ̉��Z�q�ŕϊ�����?B
     *
     * @param point �_
     * @return �ϊ���̓_
     */
    public Point3D transform(Point3D point) {
        double x, y, z;

        x = localOrigin.x() + scale()
                * (point.x() * u1().x() + point.y() * u2().x() + point.z() * u3().x());
        y = localOrigin.y() + scale()
                * (point.x() * u1().y() + point.y() * u2().y() + point.z() * u3().y());
        z = localOrigin.z() + scale()
                * (point.x() * u1().z() + point.y() * u2().z() + point.z() * u3().z());
        return new CartesianPoint3D(x, y, z);
    }

    /**
     * �^����ꂽ�x�N�g���ⱂ̉��Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * vector �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * vector �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� vector ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * vector �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� vector �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param vector                �x�N�g��
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̃x�N�g��
     */
    public Vector3D transform(Vector3D vector,
                              java.util.Hashtable transformedGeometries) {
        return vector.transformBy(this, transformedGeometries);
    }

    /**
     * �^����ꂽ�_�ⱂ̉��Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * point �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * point �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� point ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * point �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� point �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param point                 �_
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̓_
     */
    public Point3D transform(Point3D point,
                             java.util.Hashtable transformedGeometries) {
        return point.transformBy(this, transformedGeometries);
    }

    /**
     * �^����ꂽ��?�ⱂ̉��Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * curve �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * curve �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� curve ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * curve �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� curve �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param curve                 ��?�
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̋�?�
     */
    public ParametricCurve3D transform(ParametricCurve3D curve,
                                       java.util.Hashtable transformedGeometries) {
        return curve.transformBy(this, transformedGeometries);
    }

    /**
     * �^����ꂽ�Ȗʂⱂ̉��Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * surface �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * surface �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� surface ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * surface �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� surface �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param surface               �Ȗ�
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̋Ȗ�
     */
    public ParametricSurface3D transform(ParametricSurface3D surface,
                                         java.util.Hashtable transformedGeometries) {
        return surface.transformBy(this, transformedGeometries);
    }

    /**
     * �^����ꂽ�x�N�g���ⱂ̉��Z�q�ŋt�ϊ�����?B
     *
     * @param vector �x�N�g��
     * @return �t�ϊ���̃x�N�g��
     */
    public Vector3D reverseTransform(Vector3D vector) {
        double x, y, z;

        x = (vector.x() * u1().x() + vector.y() * u1().y() + vector.z() * u1().z()) / scale();
        y = (vector.x() * u2().x() + vector.y() * u2().y() + vector.z() * u2().z()) / scale();
        z = (vector.x() * u3().x() + vector.y() * u3().y() + vector.z() * u3().z()) / scale();
        return new LiteralVector3D(x, y, z);
    }

    /**
     * �^����ꂽ�_�ⱂ̉��Z�q�ŋt�ϊ�����?B
     *
     * @param point �_
     * @return �t�ϊ���̓_
     */
    public Point3D reverseTransform(Point3D point) {
        Vector3D wk;
        double x, y, z;

        wk = point.subtract(localOrigin);
        x = (wk.x() * u1().x() + wk.y() * u1().y() + wk.z() * u1().z()) / scale();
        y = (wk.x() * u2().x() + wk.y() * u2().y() + wk.z() * u2().z()) / scale();
        z = (wk.x() * u3().x() + wk.y() * u3().y() + wk.z() * u3().z()) / scale();

        return new CartesianPoint3D(x, y, z);
    }

    /**
     * �^����ꂽ�x�N�g���ⱂ̉��Z�q�ŋt�ϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * vector �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * vector �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� vector ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * vector �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� point �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param vector                �x�N�g��
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �t�ϊ���̃x�N�g��
     */
    public Vector3D reverseTransform(Vector3D vector,
                                     java.util.Hashtable transformedGeometries) {
        return vector.reverseTransformBy(this, transformedGeometries);
    }

    /**
     * �^����ꂽ�_�ⱂ̉��Z�q�ŋt�ϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * point �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * point �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� point ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * point �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� point �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param point                 �_
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �t�ϊ���̓_
     */
    public Point3D reverseTransform(Point3D point,
                                    java.util.Hashtable transformedGeometries) {
        return point.reverseTransformBy(this, transformedGeometries);
    }

    /**
     * �^����ꂽ��?�ⱂ̉��Z�q�ŋt�ϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * curve �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * curve �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� curve ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * curve �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� curve �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param curve                 ��?�
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �t�ϊ���̋�?�
     */
    public ParametricCurve3D reverseTransform(ParametricCurve3D curve,
                                              java.util.Hashtable transformedGeometries) {
        return curve.reverseTransformBy(this, transformedGeometries);
    }

    /**
     * �^����ꂽ�Ȗʂⱂ̉��Z�q�ŋt�ϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * surface �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * surface �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� surface ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * surface �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� surface �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param surface               �Ȗ�
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �t�ϊ���̋Ȗ�
     */
    public ParametricSurface3D reverseTransform(ParametricSurface3D surface,
                                                java.util.Hashtable transformedGeometries) {
        return surface.reverseTransformBy(this, transformedGeometries);
    }

    /**
     * {@link #transform(Vector3D) transform(Vector3D)} �̕ʖ��?�\�b�h?B
     * <p/>
     * local?W -> enclosed (global) ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param vector �x�N�g��
     * @return �ϊ���̃x�N�g��
     */
    public Vector3D toEnclosed(Vector3D vector) {
        return transform(vector);
    }

    /**
     * {@link #transform(Point3D) transform(Point3D)} �̕ʖ��?�\�b�h?B
     * <p/>
     * local?W -> enclosed (global) ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param point �_
     * @return �ϊ���̓_
     */
    public Point3D toEnclosed(Point3D point) {
        return transform(point);
    }

    /**
     * {@link #transform(Vector3D,java.util.Hashtable)
     * transform(Vector3D, java.util.Hashtable)} �̕ʖ��?�\�b�h?B
     * <p/>
     * local?W -> enclosed (global) ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param vector                �x�N�g��
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̃x�N�g��
     */
    public Vector3D toEnclosed(Vector3D vector,
                               java.util.Hashtable transformedGeometries) {
        return transform(vector, transformedGeometries);
    }

    /**
     * {@link #transform(Point3D,java.util.Hashtable)
     * transform(Point3D, java.util.Hashtable)} �̕ʖ��?�\�b�h?B
     * <p/>
     * local?W -> enclosed (global) ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param point                 �_
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̓_
     */
    public Point3D toEnclosed(Point3D point,
                              java.util.Hashtable transformedGeometries) {
        return transform(point, transformedGeometries);
    }

    /**
     * {@link #transform(ParametricCurve3D,java.util.Hashtable)
     * transform(ParametricCurve3D, java.util.Hashtable)} �̕ʖ��?�\�b�h?B
     * <p/>
     * local?W -> enclosed (global) ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param curve                 ��?�
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̋�?�
     */
    public ParametricCurve3D toEnclosed(ParametricCurve3D curve,
                                        java.util.Hashtable transformedGeometries) {
        return transform(curve, transformedGeometries);
    }

    /**
     * {@link #transform(ParametricSurface3D,java.util.Hashtable)
     * transform(ParametricSurface3D, java.util.Hashtable)} �̕ʖ��?�\�b�h?B
     * <p/>
     * local?W -> enclosed (global) ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param surface               �Ȗ�
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̋Ȗ�
     */
    public ParametricSurface3D toEnclosed(ParametricSurface3D surface,
                                          java.util.Hashtable transformedGeometries) {
        return transform(surface, transformedGeometries);
    }

    /**
     * {@link #reverseTransform(Vector3D) reverseTransform(Vector3D)} �̕ʖ��?�\�b�h?B
     * <p/>
     * enclosed (global) ?W -> local?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param vector �x�N�g��
     * @return �ϊ���̃x�N�g��
     */
    public Vector3D toLocal(Vector3D vector) {
        return reverseTransform(vector);
    }

    /**
     * {@link #reverseTransform(Point3D) reverseTransform(Point3D)} �̕ʖ��?�\�b�h?B
     * <p/>
     * enclosed (global) ?W -> local ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param point �_
     * @return �ϊ���̓_
     */
    public Point3D toLocal(Point3D point) {
        return reverseTransform(point);
    }

    /**
     * {@link #reverseTransform(Vector3D,java.util.Hashtable)
     * reverseTransform(Vector3D, java.util.Hashtable)} �̕ʖ��?�\�b�h?B
     * <p/>
     * enclosed (global) ?W -> local ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param vector                �x�N�g��
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̃x�N�g��
     */
    public Vector3D toLocal(Vector3D vector,
                            java.util.Hashtable transformedGeometries) {
        return reverseTransform(vector, transformedGeometries);
    }

    /**
     * {@link #reverseTransform(Point3D,java.util.Hashtable)
     * reverseTransform(Point3D, java.util.Hashtable)} �̕ʖ��?�\�b�h?B
     * <p/>
     * enclosed (global) ?W -> local ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param point                 �_
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̓_
     */
    public Point3D toLocal(Point3D point,
                           java.util.Hashtable transformedGeometries) {
        return reverseTransform(point, transformedGeometries);
    }

    /**
     * {@link #reverseTransform(ParametricCurve3D,java.util.Hashtable)
     * reverseTransform(ParametricCurve3D, java.util.Hashtable)} �̕ʖ��?�\�b�h?B
     * <p/>
     * enclosed (global) ?W -> local ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param curve                 ��?�
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̋�?�
     */
    public ParametricCurve3D toLocal(ParametricCurve3D curve,
                                     java.util.Hashtable transformedGeometries) {
        return reverseTransform(curve, transformedGeometries);
    }

    /**
     * {@link #reverseTransform(ParametricSurface3D,java.util.Hashtable)
     * reverseTransform(ParametricSurface3D, java.util.Hashtable)} �̕ʖ��?�\�b�h?B
     * <p/>
     * enclosed (global) ?W -> local ?W�̕ϊ��ł��邱�Ƃ�킩��₷������?B
     * </p>
     *
     * @param surface               �Ȗ�
     * @param transformedGeometries ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̋Ȗ�
     */
    public ParametricSurface3D toLocal(ParametricSurface3D surface,
                                       java.util.Hashtable transformedGeometries) {
        return reverseTransform(surface, transformedGeometries);
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
        writer.println(indent_tab + "\tscale\t" + scale());
        if (axis1 != null) {
            writer.println(indent_tab + "\taxis1");
            axis1.output(writer, indent + 2);
        }
        if (axis2 != null) {
            writer.println(indent_tab + "\taxis2");
            axis2.output(writer, indent + 2);
        }
        if (axis3 != null) {
            writer.println(indent_tab + "\taxis3");
            axis3.output(writer, indent + 2);
        }
        writer.println(indent_tab + "\tlocalOrigin");
        localOrigin.output(writer, indent + 2);
        writer.println("End");
    }
}

