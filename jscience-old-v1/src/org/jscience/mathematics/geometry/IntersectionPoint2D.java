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
 * �Q���� : ���?�̌�_��\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��_�̎��?�ł�?W�l���_ coordinates?A
 * ���̋�?�?�ł̈ʒu��?�?�_ pointOnCurve1?A
 * ����̋�?�?�ł̈ʒu��?�?�_ pointOnCurve2
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:00 $
 * @see OverlapCurve2D
 */

public class IntersectionPoint2D extends Point2D implements CurveCurveInterference2D {
    /**
     * ���?�ł�?W�l?B
     *
     * @serial
     */
    private final Point2D coordinates;

    /**
     * ���̋�?� (��?�1) ?�ł̈ʒu?B
     *
     * @serial
     */
    private final PointOnCurve2D pointOnCurve1;

    /**
     * ����̋�?� (��?�2) ?�ł̈ʒu?B
     *
     * @serial
     */
    private final PointOnCurve2D pointOnCurve2;

    /**
     * ���̃C���X�^���X�̊e�t�B?[���h�̒l��
     * �݂���?�?��̂Ƃꂽ��̂ł��邩�ǂ�����`�F�b�N����?B
     * <p/>
     * ?�?�?����Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @see InvalidArgumentValueException
     */
    private void checkPoints() {
        if (!this.coordinates.identical(this.pointOnCurve1) ||
                !this.coordinates.identical(this.pointOnCurve2) ||
                !this.pointOnCurve1.identical(this.pointOnCurve2))
            throw new InvalidArgumentValueException();
    }

    /**
     * ���?�̌�_��?W�l�Ɗe��?��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param coordinates   ���?�ł�?W�l
     * @param pointOnCurve1 ���̋�?� (��?�1) ?�ł̈ʒu
     * @param pointOnCurve2 ����̋�?� (��?�2) ?�ł̈ʒu
     * @param doCheck       ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint2D(Point2D coordinates,
                        PointOnCurve2D pointOnCurve1,
                        PointOnCurve2D pointOnCurve2,
                        boolean doCheck) {
        super();
        this.coordinates = coordinates;
        this.pointOnCurve1 = pointOnCurve1;
        this.pointOnCurve2 = pointOnCurve2;
        if (doCheck)
            checkPoints();
    }

    /**
     * ���?�̌�_�̊e��?��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��_�̎��?�ł�?W�l��?A���ꂼ��̋�?�?�̓_�̒��_�ƂȂ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param pointOnCurve1 ���̋�?� (��?�1) ?�ł̈ʒu
     * @param pointOnCurve2 ����̋�?� (��?�2) ?�ł̈ʒu
     * @param doCheck       ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint2D(PointOnCurve2D pointOnCurve1,
                        PointOnCurve2D pointOnCurve2,
                        boolean doCheck) {
        super();
        this.pointOnCurve1 = pointOnCurve1;
        this.pointOnCurve2 = pointOnCurve2;
        this.coordinates = this.pointOnCurve1.linearInterpolate(this.pointOnCurve2, 0.5);
        if (doCheck)
            checkPoints();
    }

    /**
     * ���?�̌�_��?W�l�Ɗe��?��?�ł̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param coordinates ���?�ł�?W�l
     * @param curve1      ���̋�?� (��?�1)
     * @param param1      ��?�1 ?�̃p���??[�^�l
     * @param curve2      ����̋�?� (��?�2)
     * @param param2      ��?�2 ?�̃p���??[�^�l
     * @param doCheck     ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint2D(Point2D coordinates,
                        ParametricCurve2D curve1, double param1,
                        ParametricCurve2D curve2, double param2,
                        boolean doCheck) {
        super();
        this.coordinates = coordinates;
        this.pointOnCurve1 = new PointOnCurve2D(curve1, param1, doCheckDebug);
        this.pointOnCurve2 = new PointOnCurve2D(curve2, param2, doCheckDebug);
        if (doCheck)
            checkPoints();
    }

    /**
     * ���?�̌�_�̊e��?��?�ł̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��_�̎��?�ł�?W�l��?A���ꂼ��̋�?�?�̓_�̒��_�ƂȂ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param curve1  ���̋�?� (��?�1)
     * @param param1  ��?�1 ?�̃p���??[�^�l
     * @param curve2  ����̋�?� (��?�2)
     * @param param2  ��?�2 ?�̃p���??[�^�l
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint2D(ParametricCurve2D curve1, double param1,
                        ParametricCurve2D curve2, double param2,
                        boolean doCheck) {
        super();
        this.pointOnCurve1 = new PointOnCurve2D(curve1, param1, doCheckDebug);
        this.pointOnCurve2 = new PointOnCurve2D(curve2, param2, doCheckDebug);
        this.coordinates = this.pointOnCurve1.linearInterpolate(this.pointOnCurve2, 0.5);
        if (doCheck)
            checkPoints();
    }

    /**
     * ���̓_�� X ?W�l��Ԃ�?B
     *
     * @return �_�� X ?W�l
     */
    public double x() {
        return coordinates.x();
    }

    /**
     * ���̓_�� Y ?W�l��Ԃ�?B
     *
     * @return �_�� Y ?W�l
     */
    public double y() {
        return coordinates.y();
    }

    /**
     * ���̌�_�̎��?�ł�?W�l��Ԃ�?B
     *
     * @return ���?�ł�?W�l
     */
    public Point2D coordinates() {
        return coordinates;
    }

    /**
     * ���̌�_�̋�?�1 ?�ł̈ʒu��Ԃ�?B
     *
     * @return ��?�1 ?�ł̈ʒu
     */
    public PointOnCurve2D pointOnCurve1() {
        return pointOnCurve1;
    }

    /**
     * ���̌�_�̋�?�2 ?�ł̈ʒu��Ԃ�?B
     *
     * @return ��?�2 ?�ł̈ʒu
     */
    public PointOnCurve2D pointOnCurve2() {
        return pointOnCurve2;
    }

    /**
     * ���̊�?���_�ł��邩�ۂ���Ԃ�?B
     *
     * @return ��_�Ȃ̂�?A?�� true
     * @see #isOverlapCurve()
     */
    public boolean isIntersectionPoint() {
        return true;
    }

    /**
     * ���̊�?��I?[�o?[���b�v�ł��邩�ۂ���Ԃ�?B
     *
     * @return �I?[�o?[���b�v�ł͂Ȃ���_�Ȃ̂�?A?�� false
     * @see #isIntersectionPoint()
     */
    public boolean isOverlapCurve() {
        return false;
    }

    /**
     * ���̊�?��_�ɕϊ�����?B
     * <p/>
     * ������?g��Ԃ�?B
     * </p>
     *
     * @return ������?g
     */
    public IntersectionPoint2D toIntersectionPoint() {
        return this;
    }

    /**
     * ���̊�?�I?[�o?[���b�v�ɕϊ�����?B
     * <p/>
     * ��_��I?[�o?[���b�v�ɕϊ����邱�Ƃ͂ł��Ȃ��̂� null ��Ԃ�?B
     * </p>
     *
     * @return ?�� null
     */
    public OverlapCurve2D toOverlapCurve() {
        return null;
    }

    /**
     * ���̌�_�� pointOnCurve1 �� pointOnCurve2 ��귂�����_��Ԃ�?B
     *
     * @return pointOnCurve1 �� pointOnCurve2 ��귂�����_
     */
    public IntersectionPoint2D exchange() {
        IntersectionPoint2D ex =
                new IntersectionPoint2D(coordinates,
                        pointOnCurve2, pointOnCurve1, doCheckDebug);
        return ex;
    }

    /**
     * ���̊�?̈��̋�?� (��?�1) ?�ł̈ʒu��?A
     * �^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�Ԃ�?B
     * <p/>
     * ���̌�_�� pointOnCurve1 �̃p���??[�^�l�� sec �͈̔͂�O��Ă���?�?��ɂ�
     * null ��Ԃ�?B
     * </p>
     *
     * @param sec  ��?�1 �̃p���??[�^���
     * @param conv ��?�1 �̃p���??[�^�l��ϊ�����I�u�W�F�N�g
     * @return ��?�1 ?�̈ʒu��^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�
     */
    public CurveCurveInterference2D trim1(ParameterSection sec,
                                          ParameterConversion2D conv) {
        double param = pointOnCurve1.parameter();
        ParametricCurve2D curve =
                (ParametricCurve2D) pointOnCurve1.geometry();
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();

        cond.makeCopy(cond.getToleranceForDistanceAsObject()
                .toToleranceForParameter(curve, param)).push();
        try {
            if (!sec.isValid(param))
                return null;
        } finally {
            ConditionOfOperation.pop();
        }
        PointOnCurve2D poc = conv.convToPoint(param);
        return new IntersectionPoint2D(poc, pointOnCurve2, doCheckDebug);
    }

    /**
     * ���̊�?̑���̋�?� (��?�2) ?�ł̈ʒu��?A
     * �^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�Ԃ�?B
     * <p/>
     * ���̌�_�� pointOnCurve2 �̃p���??[�^�l�� sec �͈̔͂�O��Ă���?�?��ɂ�
     * null ��Ԃ�?B
     * </p>
     *
     * @param sec  ��?�2 �̃p���??[�^���
     * @param conv ��?�2 �̃p���??[�^�l��ϊ�����I�u�W�F�N�g
     * @return ��?�2 ?�̈ʒu��^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�
     */
    public CurveCurveInterference2D trim2(ParameterSection sec,
                                          ParameterConversion2D conv) {
        double param = pointOnCurve2.parameter();
        ParametricCurve2D curve =
                (ParametricCurve2D) pointOnCurve2.geometry();
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();

        cond.makeCopy(cond.getToleranceForDistanceAsObject()
                .toToleranceForParameter(curve, param)).push();
        try {
            if (!sec.isValid(param))
                return null;
        } finally {
            ConditionOfOperation.pop();
        }
        PointOnCurve2D poc = conv.convToPoint(param);
        return new IntersectionPoint2D(pointOnCurve1, poc, doCheckDebug);
    }

    /**
     * ���̊�?̈��̋�?� (��?�1) ��^����ꂽ��?�ɒu����������?�Ԃ�?B
     * <p/>
     * �p���??[�^�l�Ȃǂ͂��̂܂�?B
     * </p>
     *
     * @param newCurve ��?�1 ��?ݒ肷���?�
     * @return ��?�1��u����������?�
     */
    public CurveCurveInterference2D changeCurve1(ParametricCurve2D newCurve) {
        PointOnCurve2D newPointOnCurve1 =
                new PointOnCurve2D(newCurve, this.pointOnCurve1.parameter(), doCheckDebug);

        return new IntersectionPoint2D(newPointOnCurve1, this.pointOnCurve2, doCheckDebug);
    }

    /**
     * ���̊�?̑���̋�?� (��?�2) ��^����ꂽ��?�ɒu����������?�Ԃ�?B
     * <p/>
     * �p���??[�^�l�Ȃǂ͂��̂܂�?B
     * </p>
     *
     * @param newCurve ��?�2 ��?ݒ肷���?�
     * @return ��?�2 ��u����������?�
     */
    public CurveCurveInterference2D changeCurve2(ParametricCurve2D newCurve) {
        PointOnCurve2D newPointOnCurve2 =
                new PointOnCurve2D(newCurve, this.pointOnCurve2.parameter(), doCheckDebug);

        return new IntersectionPoint2D(this.pointOnCurve1, newPointOnCurve2, doCheckDebug);
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
        Point2D tCoordinates =
                this.coordinates.transformBy(reverseTransform,
                        transformationOperator, transformedGeometries);
        PointOnCurve2D tPointOnCurve1 =
                (PointOnCurve2D) this.pointOnCurve1.transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        PointOnCurve2D tPointOnCurve2 =
                (PointOnCurve2D) this.pointOnCurve2.transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new IntersectionPoint2D(tCoordinates,
                tPointOnCurve1,
                tPointOnCurve2,
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
        writer.println(indent_tab + "\tcoordinates");
        coordinates.output(writer, indent + 2);
        writer.println(indent_tab + "\tpointOnCurve1");
        pointOnCurve1.output(writer, indent + 2);
        writer.println(indent_tab + "\tpointOnCurve2");
        pointOnCurve2.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
