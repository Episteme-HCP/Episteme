/*
 * �Q���� : ����_��?�ɂ���_��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PointOnPoint2D.java,v 1.3 2006/03/01 21:16:08 virtualcall Exp $
 *
 */

package org.jscience.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �Q���� : ����_��?�ɂ���_��\���N���X
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �_��?�BĂ���_ ({@link Point2D Point2D})
 * basisPoint ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:08 $
 * @see PointOnCurve2D
 */

public class PointOnPoint2D extends PointOnGeometry2D {
    /**
     * �_��?�BĂ���_?B
     *
     * @serial
     */
    private Point2D basisPoint;

    /**
     * �_��?�BĂ���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry2D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisPoint �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisPoint ����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param basisPoint �_��?�BĂ���_
     * @param doCheck    ��?��̒l�̑Ó�?���`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     * @see Point2D#identical(Point2D)
     */
    PointOnPoint2D(Point2D basisPoint, boolean doCheck) {
        this(null, basisPoint, doCheck);
    }

    /**
     * �_��?W�l�����
     * �_��?�BĂ���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisPoint �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisPoint ����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param point      �_?�_��?W�l
     * @param basisPoint �_��?�BĂ���_
     * @param doCheck    ��?��̒l�̑Ó�?���`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     * @see Point2D#identical(Point2D)
     */
    PointOnPoint2D(Point2D point,
                   Point2D basisPoint,
                   boolean doCheck) {
        super(point);
        if (doCheck == true) {
            if (basisPoint == null) {
                throw new InvalidArgumentValueException();
            }
            if (point != null) {
                if (!point.identical(basisPoint)) {
                    throw new InvalidArgumentValueException();
                }
            }
        }
        this.basisPoint = basisPoint;
    }

    /**
     * �_��?�BĂ���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry2D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * ��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisPoint �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisPoint ����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param basisPoint �_��?�BĂ���_
     * @see InvalidArgumentValueException
     * @see Point2D#identical(Point2D)
     */
    public PointOnPoint2D(Point2D basisPoint) {
        this(null, basisPoint);
    }

    /**
     * �_��?W�l�����
     * �_��?�BĂ���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * ��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisPoint �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisPoint ����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param point      �_?�_��?W�l
     * @param basisPoint �_��?�BĂ���_
     * @see InvalidArgumentValueException
     * @see Point2D#identical(Point2D)
     */
    public PointOnPoint2D(Point2D point, Point2D basisPoint) {
        this(point, basisPoint, true);
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����`?�v�f��
     * Point2D �̃C���X�^���X�ł���?B
     * </p>
     *
     * @return �x?[�X�ƂȂ�`?�v�f
     * @see #basisPoint()
     */
    public GeometryElement geometry() {
        return basisPoint();
    }

    /**
     * �x?[�X�ƂȂ�_��Ԃ�?B
     *
     * @return �x?[�X�ƂȂ�_
     * @see #geometry()
     */
    public Point2D basisPoint() {
        return basisPoint;
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�_��?W�l��?�߂�?B
     *
     * @return �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�?�߂��_��?W�l
     * @see #basisPoint()
     */
    Point2D coordinates() {
        return basisPoint();
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
        Point2D tBasisPoint =
                this.basisPoint.transformBy(reverseTransform,
                        transformationOperator, transformedGeometries);

        return new PointOnPoint2D(tPoint, tBasisPoint, doCheckDebug);
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
        basisPoint.output(writer, indent + 2);
        writer.println("End");
    }
}
