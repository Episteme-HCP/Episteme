/*
 * �Q���� : ����p���?�g���b�N��?��?�ɂ���_��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PointOnCurve2D.java,v 1.3 2006/03/01 21:16:07 virtualcall Exp $
 *
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.io.PrintWriter;

/**
 * �Q���� : ����p���?�g���b�N��?��?�ɂ���_��\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �_��?�BĂ���p���?�g���b�N��?� ({@link ParametricCurve2D ParametricCurve2D})
 * basisCurve ��?A
 * ���̃p���?�g���b�N��?�?�ł̓_�̃p���??[�^�l parameter ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:07 $
 * @see PointOnPoint2D
 */

public class PointOnCurve2D extends PointOnGeometry2D implements ParameterRangeOnCurve2D {
    /**
     * �_��?�BĂ���p���?�g���b�N��?�?B
     *
     * @serial
     */
    private ParametricCurve2D basisCurve;

    /**
     * �p���?�g���b�N��?�?�ł̓_�̃p���??[�^�l?B
     *
     * @serial
     */
    private double parameter;

    /**
     * �_��?�BĂ���p���?�g���b�N��?��
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry2D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisCurve �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	parameter �� basisCurve �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param basisCurve �_��?�BĂ���p���?�g���b�N��?�
     * @param parameter  �p���?�g���b�N��?�?�ł̓_�̃p���??[�^�l
     * @param doCheck    ��?��̒l�̑Ó�?���`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParametricCurve2D#coordinates(double)
     * @see Point2D#identical(Point2D)
     */
    PointOnCurve2D(ParametricCurve2D basisCurve,
                   double parameter,
                   boolean doCheck) {
        this(null, basisCurve, parameter, doCheck);
    }

    /**
     * �_��?W�l�����
     * �_��?�BĂ���p���?�g���b�N��?��
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisCurve �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	parameter �� basisCurve �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisCurve ?�� parameter �ɑΉ�����_����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param point      ?�?�_��?W�l
     * @param basisCurve �_��?�BĂ���p���?�g���b�N��?�
     * @param parameter  �p���?�g���b�N��?�?�ł̓_�̃p���??[�^�l
     * @param doCheck    ��?��̒l�̑Ó�?���`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParametricCurve2D#coordinates(double)
     * @see Point2D#identical(Point2D)
     */
    PointOnCurve2D(Point2D point,
                   ParametricCurve2D basisCurve,
                   double parameter,
                   boolean doCheck) {
        super(point);
        if (doCheck == true) {
            ConditionOfOperation condition
                    = ConditionOfOperation.getCondition();
            double pTol = condition.getToleranceForParameter();

            if (basisCurve == null) {
                throw new InvalidArgumentValueException("basisCurve is null.");
            }
            basisCurve.checkValidity(parameter);
            if (point != null) {
                if (!point.identical(basisCurve.coordinates(parameter))) {
                    throw new InvalidArgumentValueException("point is not consistent with parameter.");
                }
            }
        }
        this.basisCurve = basisCurve;
        this.parameter = parameter;
    }

    /**
     * �_��?�BĂ���p���?�g���b�N��?��
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry2D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * ��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisCurve �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	parameter �� basisCurve �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param basisCurve �_��?�BĂ���p���?�g���b�N��?�
     * @param parameter  �p���?�g���b�N��?�?�ł̓_�̃p���??[�^�l
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParametricCurve2D#coordinates(double)
     * @see Point2D#identical(Point2D)
     */
    public PointOnCurve2D(ParametricCurve2D basisCurve,
                          double parameter) {
        this(null, basisCurve, parameter);
    }

    /**
     * �_��?W�l�����
     * �_��?�BĂ���p���?�g���b�N��?��
     * ����?�ł̓_�̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * ��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisCurve �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	parameter �� basisCurve �̃p���??[�^��`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisCurve ?�� parameter �ɑΉ�����_����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param point      ?�?�_��?W�l
     * @param basisCurve �_��?�BĂ���p���?�g���b�N��?�
     * @param parameter  �p���?�g���b�N��?�?�ł̓_�̃p���??[�^�l
     * @see InvalidArgumentValueException
     * @see ParameterOutOfRange
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParametricCurve2D#coordinates(double)
     * @see Point2D#identical(Point2D)
     */
    public PointOnCurve2D(Point2D point,
                          ParametricCurve2D basisCurve,
                          double parameter) {
        this(point, basisCurve, parameter, true);
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����`?�v�f��
     * ParametricCurve2D �̃C���X�^���X�ł���?B
     * </p>
     *
     * @return �x?[�X�ƂȂ�`?�v�f
     * @see #basisCurve()
     */
    public GeometryElement geometry() {
        return basisCurve();
    }

    /**
     * �x?[�X�ƂȂ�p���?�g���b�N��?��Ԃ�?B
     *
     * @return �x?[�X�ƂȂ�p���?�g���b�N��?�
     * @see #geometry()
     */
    public ParametricCurve2D basisCurve() {
        return basisCurve;
    }

    /**
     * ��?�?�ł̓_�̃p���??[�^�l��Ԃ�?B
     *
     * @return ��?�?�ł̓_�̃p���??[�^�l
     */
    public double parameter() {
        return parameter;
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�_��?W�l��?�߂�?B
     *
     * @return �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�?�߂��_��?W�l
     */
    Point2D coordinates() {
        Point2D coord;
        try {
            coord = basisCurve.coordinates(parameter);
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
        return coord;
    }

    /**
     * �_���ۂ���Ԃ�?B
     *
     * @return ?�� true
     */
    public boolean isPoint() {
        return true;
    }

    /**
     * ��Ԃ��ۂ���Ԃ�?B
     *
     * @return ?�� false
     */
    public boolean isSection() {
        return false;
    }

    /**
     * ����?�?�_�Ƒ���?�?�_���p���??[�^�I�ɓ��ꂩ�ǂ����𒲂ׂ�?B
     * <p/>
     * �x?[�X�ƂȂ��?�قȂ�?�?��� <code>false</code> ��Ԃ�?B
     * </p>
     * <p/>
     * this �� mate �̎��?��?W�l��?���?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?���?�ł����
     * <code>false</code> ��Ԃ�?B
     * </p>
     * <p/>
     * this �� mate �̓�̃p���??[�^�l�̊Ԃ̋�?�̓��̂肪?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?���?�ł����
     * <code>false</code> ��Ԃ�?B
     * </p>
     * <p/>
     * ?�L�̂�����ł�Ȃ���� <code>true</code> ��Ԃ�?B
     * </p>
     *
     * @return ����ł���� <code>true</code>?A�����łȂ���� <code>false</code>
     * @see Point2D#identical(Point2D)
     * @see ParametricCurve2D#identicalParameter(double,double)
     */
    boolean parametricallyIdentical(PointOnCurve2D mate) {
        if (basisCurve() != mate.basisCurve())
            return false;

        if (!identical(mate))
            return false;

        if (!basisCurve().identicalParameter(parameter(), mate.parameter()))
            return false;

        return true;
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
    protected synchronized Point2D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator2D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Point2D tPoint = this.point();
        if (tPoint != null)
            tPoint = tPoint.transformBy(reverseTransform,
                    transformationOperator, transformedGeometries);
        ParametricCurve2D tBasisCurve =
                this.basisCurve.transformBy(reverseTransform,
                        transformationOperator, transformedGeometries);
        return new PointOnCurve2D(tPoint, tBasisCurve, this.parameter, doCheckDebug);
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
        writer.println(indent_tab + "\tbasisCurve");
        basisCurve.output(writer, indent + 2);
        writer.println(indent_tab + "\tparameter\t" + parameter);
        writer.println(indent_tab + "End");
    }
}
