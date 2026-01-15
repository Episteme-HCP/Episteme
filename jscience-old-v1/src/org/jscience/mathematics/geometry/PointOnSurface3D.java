/*
 * �R���� : ����p���?�g���b�N�Ȗʂ�?�ɂ���_��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PointOnSurface3D.java,v 1.3 2006/03/01 21:16:08 virtualcall Exp $
 *
 */

package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �R���� : ����p���?�g���b�N�Ȗʂ�?�ɂ���_��\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �_��?�BĂ���p���?�g���b�N�Ȗ� ({@link ParametricSurface3D ParametricSurface3D})
 * basisSurface ��?A
 * ���̃p���?�g���b�N�Ȗ�?�ł̓_�̃p���??[�^�l (uParameter, vParameter) ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:08 $
 * @see PointOnPoint3D
 * @see PointOnCurve3D
 */

public class PointOnSurface3D extends PointOnGeometry3D {
    /**
     * �_��?�BĂ���p���?�g���b�N�Ȗ�?B
     *
     * @serial
     */
    private ParametricSurface3D basisSurface;

    /**
     * �p���?�g���b�N�Ȗ�?�ł̓_�� U ���̃p���??[�^�l?B
     *
     * @serial
     */
    private double uParameter;

    /**
     * �p���?�g���b�N�Ȗ�?�ł̓_�� V ���̃p���??[�^�l?B
     *
     * @serial
     */
    private double vParameter;

    /**
     * �_��?�BĂ���p���?�g���b�N�Ȗʂ�
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry3D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisSurface �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	(uParameter, vParameter) �� basisSurface �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * ������?AbasisSurface ����?�E�Ȗʂ�?�?��ɂ�?A���̃`�F�b�N��?s�Ȃ�Ȃ�?B
     * </ul>
     * </p>
     *
     * @param basisSurface �_��?�BĂ���p���?�g���b�N�Ȗ�
     * @param uParameter   �p���?�g���b�N�Ȗ�?�ł̓_�� U ���̃p���??[�^�l
     * @param vParameter   �p���?�g���b�N�Ȗ�?�ł̓_�� V ���̃p���??[�^�l
     * @param doCheck      ��?��̒l�̑Ó�?���`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricSurface#checkUValidity(double)
     * @see AbstractParametricSurface#checkVValidity(double)
     * @see ParametricSurface3D#coordinates(double,double)
     * @see Point3D#identical(Point3D)
     */
    PointOnSurface3D(ParametricSurface3D basisSurface,
                     double uParameter, double vParameter,
                     boolean doCheck) {
        this(null, basisSurface, uParameter, vParameter, doCheck);
    }

    /**
     * �_��?W�l�����
     * �_��?�BĂ���p���?�g���b�N�Ȗʂ�
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisSurface �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	(uParameter, vParameter) �� basisSurface �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * ������?AbasisSurface ����?�E�Ȗʂ�?�?��ɂ�?A���̃`�F�b�N��?s�Ȃ�Ȃ�?B
     * <li>	point �� null �łȂ�?A
     * point �� basisSurface ?�� (uParameter, vParameter) �ɑΉ�����_����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param point        ?�?�_��?W�l
     * @param basisSurface �_��?�BĂ���p���?�g���b�N�Ȗ�
     * @param uParameter   �p���?�g���b�N�Ȗ�?�ł̓_�� U ���̃p���??[�^�l
     * @param vParameter   �p���?�g���b�N�Ȗ�?�ł̓_�� V ���̃p���??[�^�l
     * @param doCheck      ��?��̒l�̑Ó�?���`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricSurface#checkUValidity(double)
     * @see AbstractParametricSurface#checkVValidity(double)
     * @see ParametricSurface3D#coordinates(double,double)
     * @see Point3D#identical(Point3D)
     */
    PointOnSurface3D(Point3D point,
                     ParametricSurface3D basisSurface,
                     double uParameter, double vParameter,
                     boolean doCheck) {
        super(point);
        if (doCheck == true) {
            ConditionOfOperation condition
                    = ConditionOfOperation.getCondition();
            double pTol = condition.getToleranceForParameter();

            if (basisSurface == null) {
                throw new InvalidArgumentValueException();
            }
            if (basisSurface.type() != ParametricSurface3D.CURVE_BOUNDED_SURFACE_3D) {
                basisSurface.checkUValidity(uParameter);
                basisSurface.checkVValidity(vParameter);
            }
            if (point != null) {
                if (!point.identical(basisSurface.coordinates
                        (uParameter, vParameter))) {
                    throw new InvalidArgumentValueException();
                }
            }
        }
        this.basisSurface = basisSurface;
        this.uParameter = uParameter;
        this.vParameter = vParameter;
    }

    /**
     * �_��?�BĂ���p���?�g���b�N�Ȗʂ�
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry3D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * <ul>
     * <li>	basisSurface �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	(uParameter, vParameter) �� basisSurface �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param basisSurface �_��?�BĂ���p���?�g���b�N�Ȗ�
     * @param uParameter   �p���?�g���b�N�Ȗ�?�ł̓_�� U ���̃p���??[�^�l
     * @param vParameter   �p���?�g���b�N�Ȗ�?�ł̓_�� V ���̃p���??[�^�l
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricSurface#checkUValidity(double)
     * @see AbstractParametricSurface#checkVValidity(double)
     */
    public PointOnSurface3D(ParametricSurface3D basisSurface,
                            double uParameter, double vParameter) {
        this(null, basisSurface, uParameter, vParameter);
    }

    /**
     * �_��?�BĂ���p���?�g���b�N�Ȗʂ�
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry3D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisSurface �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	(U �p���??[�^�l, V �p���??[�^�l) �� basisSurface �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * ������?AbasisSurface ����?�E�Ȗʂ�?�?��ɂ�?A���̃`�F�b�N��?s�Ȃ�Ȃ�?B
     * </ul>
     * </p>
     *
     * @param basisSurface     �_��?�BĂ���p���?�g���b�N�Ȗ�
     * @param pairOfParameters �p���?�g���b�N�Ȗ�?�ł̓_�� (U �p���??[�^�l, V �p���??[�^�l)
     * @param doCheck          ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricSurface#checkUValidity(double)
     * @see AbstractParametricSurface#checkVValidity(double)
     */
    PointOnSurface3D(ParametricSurface3D basisSurface,
                     Point2D pairOfParameters,
                     boolean doCheck) {
        this(null, basisSurface, pairOfParameters.x(), pairOfParameters.y(), doCheck);
    }

    /**
     * �_��?�BĂ���p���?�g���b�N�Ȗʂ�
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry3D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * <ul>
     * <li>	basisSurface �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	(U �p���??[�^�l, V �p���??[�^�l) �� basisSurface �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * ������?AbasisSurface ����?�E�Ȗʂ�?�?��ɂ�?A���̃`�F�b�N��?s�Ȃ�Ȃ�?B
     * </ul>
     * </p>
     *
     * @param basisSurface     �_��?�BĂ���p���?�g���b�N�Ȗ�
     * @param pairOfParameters �p���?�g���b�N�Ȗ�?�ł̓_�� (U �p���??[�^�l, V �p���??[�^�l)
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricSurface#checkUValidity(double)
     * @see AbstractParametricSurface#checkVValidity(double)
     */
    public PointOnSurface3D(ParametricSurface3D basisSurface,
                            Point2D pairOfParameters) {
        this(null, basisSurface, pairOfParameters.x(), pairOfParameters.y());
    }

    /**
     * �_��?W�l�����
     * �_��?�BĂ���p���?�g���b�N�Ȗʂ�
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * <ul>
     * <li>	basisSurface �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	(uParameter, vParameter) �� basisSurface �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * ������?AbasisSurface ����?�E�Ȗʂ�?�?��ɂ�?A���̃`�F�b�N��?s�Ȃ�Ȃ�?B
     * <li>	point �� null �łȂ�?A
     * point �� basisSurface ?�� (uParameter, vParameter) �ɑΉ�����_����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param point        ?�?�_��?W�l
     * @param basisSurface �_��?�BĂ���p���?�g���b�N�Ȗ�
     * @param uParameter   �p���?�g���b�N�Ȗ�?�ł̓_�� U ���̃p���??[�^�l
     * @param vParameter   �p���?�g���b�N�Ȗ�?�ł̓_�� V ���̃p���??[�^�l
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricSurface#checkUValidity(double)
     * @see AbstractParametricSurface#checkVValidity(double)
     * @see ParametricSurface3D#coordinates(double,double)
     * @see Point3D#identical(Point3D)
     */
    public PointOnSurface3D(Point3D point,
                            ParametricSurface3D basisSurface,
                            double uParameter, double vParameter) {
        this(point, basisSurface, uParameter, vParameter, true);
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����`?�v�f��
     * ParametricSurface3D �̃C���X�^���X�ł���?B
     * </p>
     *
     * @return �x?[�X�ƂȂ�`?�v�f
     * @see #basisSurface()
     */
    public GeometryElement geometry() {
        return basisSurface();
    }

    /**
     * �x?[�X�ƂȂ�p���?�g���b�N�Ȗʂ�Ԃ�?B
     *
     * @return �x?[�X�ƂȂ�p���?�g���b�N�Ȗ�
     * @see #geometry()
     */
    public ParametricSurface3D basisSurface() {
        return basisSurface;
    }

    /**
     * �Ȗ�?�ł̓_�� U ���̃p���??[�^�l��Ԃ�?B
     *
     * @return �Ȗ�?�ł̓_�� U ���̃p���??[�^�l
     */
    public double uParameter() {
        return uParameter;
    }

    /**
     * �Ȗ�?�ł̓_�� V ���̃p���??[�^�l��Ԃ�?B
     *
     * @return �Ȗ�?�ł̓_�� V ���̃p���??[�^�l
     */
    public double vParameter() {
        return vParameter;
    }

    /**
     * �Ȗ�?�ł̓_�� UV �p���??[�^�l��Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f�� U ���̃p���??[�^�l?A
     * ��Ԗڂ̗v�f�� V ���̃p���??[�^�l
     * �����?B
     * </p>
     *
     * @return �Ȗ�?�ł̓_�� UV �p���??[�^�l
     */
    public double[] parameters() {
        double[] param = {uParameter, vParameter};
        return param;
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�_��?W�l��?�߂�?B
     *
     * @return �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�?�߂��_��?W�l
     */
    Point3D coordinates() {
        Point3D coord;
        try {
            coord = basisSurface.coordinates(uParameter, vParameter);
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
        return coord;
    }

    /**
     * ���̓_��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� this ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param reverseTransform       �t�ϊ�����̂ł���� true?A�����łȂ���� false
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̊􉽗v�f
     */
    protected synchronized Point3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Point3D tPoint = this.point();
        if (tPoint != null)
            tPoint = tPoint.transformBy(reverseTransform,
                    transformationOperator, transformedGeometries);
        ParametricSurface3D tBasisSurface =
                this.basisSurface.transformBy(reverseTransform,
                        transformationOperator, transformedGeometries);
        return new PointOnSurface3D(tPoint,
                tBasisSurface,
                this.uParameter, this.vParameter,
                doCheckDebug);
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
        writer.println(indent_tab + "\tpoint");
        coordinates().output(writer, indent + 2);
        writer.println(indent_tab + "\tbasisSurface");
        basisSurface.output(writer, indent + 2);
        writer.println(indent_tab + "\tuParameter\t" + uParameter);
        writer.println(indent_tab + "\tvParameter\t" + vParameter);
        writer.println(indent_tab + "End");
    }
}
