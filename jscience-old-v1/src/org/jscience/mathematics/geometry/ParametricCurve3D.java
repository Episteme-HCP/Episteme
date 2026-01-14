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

import org.jscience.mathematics.MachineEpsilon;
import org.jscience.util.FatalException;

/**
 * �R�����̃p���?�g���b�N�ȋ�?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * ���̃N���X��?A��̎�?��l�ŕ\�����p���??[�^ t �̒l�ɂ�B�?A
 * �ʒu�����肳���R�����̋�?� P(t) �S�ʂ��?��?�����\������?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.5 $, $Date: 2006/03/01 21:16:06 $
 * @see ParametricCurve2D
 */

public abstract class ParametricCurve3D extends AbstractParametricCurve {
    /**
     * ��?� Line �ł��邱�Ƃ���?�
     */
    static final int LINE_3D = 1;

    /**
     * ��?� Bounded Line �ł��邱�Ƃ���?�
     */
    static final int BOUNDED_LINE_3D = 2;

    /**
     * ��?� Circle �ł��邱�Ƃ���?�
     */
    static final int CIRCLE_3D = 10;

    /**
     * ��?� Ellipse �ł��邱�Ƃ���?�
     */
    static final int ELLIPSE_3D = 11;

    /**
     * ��?� Hyperbola �ł��邱�Ƃ���?�
     */
    static final int HYPERBOLA_3D = 12;

    /**
     * ��?� Parabola �ł��邱�Ƃ���?�
     */
    static final int PARABOLA_3D = 13;

    /**
     * ��?� Polyline �ł��邱�Ƃ���?�
     */
    static final int POLYLINE_3D = 20;

    /**
     * ��?� Bspline Curve �ł��邱�Ƃ���?�
     */
    static final int BSPLINE_CURVE_3D = 21;

    /**
     * ��?� Pure Bezier Curve �ł��邱�Ƃ���?�
     */
    static final int PURE_BEZIER_CURVE_3D = 22;

    /**
     * ��?� Trimmed Curve �ł��邱�Ƃ���?�
     */
    static final int TRIMMED_CURVE_3D = 23;

    /**
     * ��?� Composite Curve �ł��邱�Ƃ���?�
     */
    static final int COMPOSITE_CURVE_3D = 24;

    /**
     * ��?� Composite Curve Segment �ł��邱�Ƃ���?�
     */
    static final int COMPOSITE_CURVE_SEGMENT_3D = 25;

    /**
     * ��?� Surface Curve �ł��邱�Ƃ���?�
     */
    static final int SURFACE_CURVE_3D = 26;

    /**
     * ��?� Intersection Curve �ł��邱�Ƃ���?�
     */
    static final int INTERSECTION_CURVE_3D = 27;

    /**
     * ��?� Polynomial Curve �ł��邱�Ƃ���?�
     */
    static final int POLYNOMIAL_CURVE_3D = 30;

    /**
     * �I�u�W�F�N�g��?\�z����?B
     */
    protected ParametricCurve3D() {
        super();
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ���?ۃ?�\�b�h?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    public abstract Point3D coordinates(double param);

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ���?ۃ?�\�b�h?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public abstract Vector3D tangentVector(double param);

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ���?ۃ?�\�b�h?B
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public abstract CurveCurvature3D curvature(double param);

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̃��C����Ԃ���?ۃ?�\�b�h?B
     *
     * @param param �p���??[�^�l
     * @return ���C��
     */
    public abstract double torsion(double param);

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ���?ۃ?�\�b�h?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public abstract CurveDerivative3D evaluation(double param);

    /**
     * ���̋�?�̓Hٓ_��Ԃ���?ۃ?�\�b�h?B
     * <p/>
     * �Hٓ_�Ƃ�?A
     * ?ڃx�N�g���̑傫���� 0 �ɂȂ�_?A
     * ���邢�͂��̑O���?ڃx�N�g�����s�A���ƂȂ�_�ł���?B
     * </p>
     * <p/>
     * �Hٓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public abstract PointOnCurve3D[] singular() throws IndefiniteSolutionException;

    /**
     * ���̋�?�̕ϋȓ_��Ԃ���?ۃ?�\�b�h?B
     * <p/>
     * �ϋȓ_�Ƃ�?A���̑O��ŋȗ���?S�̑�?݂���T�C�h���ړ�����_�ł���?B
     * </p>
     * <p/>
     * �ϋȓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public abstract PointOnCurve3D[] inflexion() throws IndefiniteSolutionException;

    /**
     * �^����ꂽ�p���??[�^�l�̓_��?A
     * ����_�ⱂ̋�?�ɓ��e�����_ (�܂�?A����_���炱�̋�?�ւ�?�?�̑�) ��
     * ���邩�ǂ�����Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l�̓_�����e�_�ł����?A���̓_��\��?�?�_��Ԃ�?B
     * �^����ꂽ�p���??[�^�l�̓_�����e�_�łȂ���� null ��Ԃ�?B
     * </p>
     *
     * @param Bparam ���e�_�̃p���??[�^�l
     * @param p      ���e���̓_
     * @param dTol2  �����̋��e��?��̎�?�
     * @return ���e�_�I�u�W�F�N�g
     * @see #projectFrom(Point3D)
     */
    protected PointOnCurve3D checkProjection(double Bparam,
                                             Point3D p,
                                             double dTol2) {
        PointOnCurve3D result = null;

        if (!isValid(Bparam)) {
            return null;
        }

        Point3D Bpnt = coordinates(Bparam);
        Vector3D Bpnt2A = p.subtract(Bpnt);
        double norm = Bpnt2A.norm();

        if (norm > dTol2) {
            CurveCurvature3D Bcurv = curvature(Bparam);
            double norm_from_Bnrml = Bpnt2A.crossProduct(Bcurv.normal()).norm();
            if (norm_from_Bnrml > dTol2) {
                return null;
            }
        }

        try {
            return new PointOnCurve3D(Bpnt, this, Bparam, doCheckDebug);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
    }

    /**
     * �^����ꂽ��̃p���??[�^�l�����̋�?�?�œ���̓_�Ƃ݂Ȃ��邩�ǂ�����Ԃ�?B
     * <p/>
     * ��̃p���??[�^�l�̊Ԃ̋�?�̓��̂肪?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?��������
     * ��̃p���??[�^�l�͓���̓_��\���Ă����̂Ƃ���?B
     * </p>
     *
     * @param own_prm1 �p���??[�^�l 1
     * @param own_prm2 �p���??[�^�l 2
     * @return ����̓_�Ƃ݂Ȃ���Ȃ�� true?A�����łȂ���� false
     */
    public boolean identicalParameter(double own_prm1, double own_prm2) {
        Point3D o1_crd, o2_crd;
        ParameterDomain pdmn;
        ParameterSection pint;
        double leng;

        checkValidity(own_prm1);
        checkValidity(own_prm2);

        o1_crd = coordinates(own_prm1);
        o2_crd = coordinates(own_prm2);

        if (!o1_crd.identical(o2_crd))
            return false;

        pdmn = parameterDomain();

        double pTol;
        double increase = 0.0;
        if (pdmn.isInfinite())
            pTol = MachineEpsilon.DOUBLE;
        else {
            increase = Math.abs(pdmn.section().increase());
            if (increase < 1.0)
                pTol = MachineEpsilon.DOUBLE * increase;
            else
                pTol = MachineEpsilon.DOUBLE;
        }

        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double savedPTol = condition.getToleranceForParameter();

        condition.makeCopyWithToleranceForParameter(pTol).push();
        try {

            if (own_prm1 > own_prm2)
                pint = new ParameterSection(own_prm2, own_prm1 - own_prm2);
            else
                pint = new ParameterSection(own_prm1, own_prm2 - own_prm1);

            leng = length(pint);

            double dTol = condition.getToleranceForDistance();
            if (leng < dTol)
                return true;

            if (!pdmn.isPeriodic())
                return false;
            else {
                double start = (own_prm1 > own_prm2) ? own_prm1 : own_prm2;
                increase = increase - pint.increase();
                pint = new ParameterSection(start, increase);
                leng = length(pint);

                if (leng < dTol)
                    return true;
            }
            return false;
        } finally {
            ConditionOfOperation.pop();
        }
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public abstract PointOnCurve3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ���?ۃ?�\�b�h?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_�� PointOnCurve3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     * @see PointOnCurve3D
     */
    public abstract Polyline3D
    toPolyline(ParameterSection pint, ToleranceForDistance tol);

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ���?ۃ?�\�b�h?B
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     */
    public abstract BsplineCurve3D
    toBsplineCurve(ParameterSection pint);

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public abstract IntersectionPoint3D[]
    intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(Circle3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(Ellipse3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(Hyperbola3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(Parabola3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(Polyline3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(PureBezierCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(BsplineCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(TrimmedCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(CompositeCurveSegment3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(CompositeCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋Ȗʂ̌�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public abstract IntersectionPoint3D[]
    intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋Ȗ� (��?͓I�Ȗ�) �̌�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ� (��?͓I�Ȗ�)
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(ElementarySurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�x�W�G�Ȗ�) �̌�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ� (�x�W�G�Ȗ�)
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(PureBezierSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(BsplineSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�Ƒ��̋Ȗ� (��?�E�Ȗ�) �̌�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ� (��?�E�Ȗ�)
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[]
    intersect(CurveBoundedSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (��`�g�����Ȗ�) �̌�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ� (��`�g�����Ȗ�)
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[]
    intersect(RectangularTrimmedSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ���?ۃ?�\�b�h?B
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     */
    public abstract ParametricCurve3D
    parallelTranslate(Vector3D moveVec);

    /**
     * ���̋�?�̎�����Ԃ�?B
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
     * ���̋�?�R�������ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �R�����Ȃ̂�?A?�� true
     */
    public boolean is3D() {
        return true;
    }

    /**
     * ���̋�?�̗v�f��ʂ�Ԃ���?ۃ?�\�b�h?B
     *
     * @return �v�f���
     * @see #LINE_3D
     * @see #BOUNDED_LINE_3D
     * @see #CIRCLE_3D
     * @see #ELLIPSE_3D
     * @see #HYPERBOLA_3D
     * @see #PARABOLA_3D
     * @see #POLYLINE_3D
     * @see #BSPLINE_CURVE_3D
     * @see #PURE_BEZIER_CURVE_3D
     * @see #TRIMMED_CURVE_3D
     * @see #COMPOSITE_CURVE_3D
     * @see #COMPOSITE_CURVE_SEGMENT_3D
     * @see #SURFACE_CURVE_3D
     * @see #INTERSECTION_CURVE_3D
     * @see #POLYNOMIAL_CURVE_3D
     */
    abstract int type();

    /**
     * ���̋�?��?A�^����ꂽ��?�?W�n�� Z ���̎���?A
     * �^����ꂽ�p�x������]��������?��Ԃ���?ۃ?�\�b�h?B
     *
     * @param trns ��?�?W�n���瓾��ꂽ?W�ϊ����Z�q
     * @param rCos cos(��]�p�x)
     * @param rSin sin(��]�p�x)
     * @return ��]��̋�?�
     */
    abstract ParametricCurve3D rotateZ(CartesianTransformationOperator3D trns,
                                       double rCos,
                                       double rSin);

    /**
     * ���̋�?�?�̓_��?A�^����ꂽ��?�?�ɂȂ��_���Ԃ�?B
     *
     * @param line ��?�
     * @return �^����ꂽ��?�?�ɂȂ��_
     */
    abstract Point3D getPointNotOnLine(Line3D line);

    /**
     * �^����ꂽ�_ P ���炱�̋�?�ւ̓��e�_�̓��?AP ��?ł�߂��_��Ԃ�?B
     * <p/>
     * ���e�_����?݂��Ȃ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param pnt ���e���̓_
     * @return ���e���̓_��?ł�߂����e�_
     * @see #projectFrom(Point3D)
     * @see #nearestProjectWithDistanceFrom(Point3D,double)
     */
    public PointOnCurve3D nearestProjectFrom(Point3D pnt) {
        PointOnCurve3D[] proj;
        try {
            proj = projectFrom(pnt);
        } catch (IndefiniteSolutionException e) {
            proj = new PointOnCurve3D[1];
            proj[0] = (PointOnCurve3D) e.suitable();
        }

        if (proj.length == 0)
            return null;

        double dist = proj[0].distance2(pnt);
        int idx = 0;

        // find nearest point
        for (int i = 1; i < proj.length; i++) {
            double dist2 = proj[i].distance2(pnt);

            if (dist2 < dist) {
                dist = dist2;
                idx = i;
            }
        }

        return proj[idx];
    }

    /**
     * �^����ꂽ�_ P ���炱�̋�?�ւ̓��e�_�̓��?AP ����̋������w��̒l��?ł�߂��_��Ԃ�?B
     * <p/>
     * ���e�_����?݂��Ȃ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param pnt      ���e���̓_
     * @param distance ����
     * @return �w�肵��������?ł�߂����e�_
     * @see #projectFrom(Point3D)
     * @see #nearestProjectFrom(Point3D)
     */
    public PointOnCurve3D nearestProjectWithDistanceFrom(Point3D pnt, double distance) {
        PointOnCurve3D[] proj;
        try {
            proj = projectFrom(pnt);
        } catch (IndefiniteSolutionException e) {
            proj = new PointOnCurve3D[1];
            proj[0] = (PointOnCurve3D) e.suitable();
        }

        if (proj.length == 0)
            return null;

        double diff = Math.abs(distance - proj[0].distance(pnt));
        int idx = 0;

        // find nearest point
        for (int i = 1; i < proj.length; i++) {
            double diff2 = Math.abs(distance - proj[i].distance(pnt));

            if (diff2 < diff) {
                diff = diff2;
                idx = i;
            }
        }

        return proj[idx];
    }

    /**
     * �^����ꂽ?�?�_��?A���̋�?�ł̃p���??[�^�l��?�߂�?B
     * <p/>
     * �^����ꂽ�_�����̋�?�?��?�BĂ����̂Ƃ���?A
     * ���̓_�ɑΉ�����p���??[�^�l��?�߂�?B
     * </p>
     * <p/>
     * ���̃?�\�b�h�̓Ք?��?�͈ȉ��̒ʂ�?B
     * <ul>
     * <li> nearestProjectFrom(pnt) ��g�B�?A
     * �^����ꂽ�_ P ��?ł�߂����e�_ Q �𓾂�
     * <li> Q ����?݂���?A
     * P �� Q ����?�?ݒ肳��Ă��鉉�Z?�?���œ���̓_�Ƃ݂Ȃ����?A
     * Q �̃p���??[�^�l��Ԃ�
     * <li> ���̋�?�L�ŊJ������?��?A
     * �[�_�� P �Ɉ�v����Ȃ��?A���̃p���??[�^�l��Ԃ�
     * <li> ���̋�?�|�����C���̕�����?�Ȃ��?A�|�����C���ɕϊ���?A
     * ���̃|�����C���̒��_�� P �ƈ�v�����̂������?A
     * ���̃p���??[�^�l��Ԃ�
     * <li> ��?�̂�����ɂӖ�Ă͂܂�Ȃ����?AInvalidArgumentValueException �𓊂���
     * </ul>
     * </p>
     *
     * @param pnt ��?�?�̓_
     * @return �Ή�����p���??[�^�l
     * @see InvalidArgumentValueException
     * @see #nearestProjectFrom(Point3D)
     * @see ConditionOfOperation
     */
    public double pointToParameter(Point3D pnt) {
        // ���e�����_�� identical ��?H
        PointOnCurve3D proj = nearestProjectFrom(pnt);
        if ((proj != null) && (pnt.identical(proj)))
            return proj.parameter();

        // ���[�_������?�?�?A����炪 identical ��?H
        if (this.isFinite() && this.isOpen()) {
            double param;

            param = this.parameterDomain().section().lower();
            if (pnt.identical(new PointOnCurve3D(this, param)))
                return param;

            param = this.parameterDomain().section().upper();
            if (pnt.identical(new PointOnCurve3D(this, param)))
                return param;
        }

        // �|�����C���̉�?�������?�?�?Aidentical �Ȓ��_�����邩?H
        if (this.hasPolyline() == true) {
            BoundedCurve3D bounded = (BoundedCurve3D) this;
            Polyline3D polyline = bounded.toPolyline(getToleranceForDistanceAsObject());
            PointOnCurve3D pos;

            for (int i = 0; i < polyline.nPoints(); i++) {
                pos = (PointOnCurve3D) polyline.pointAt(i);
                if (pnt.identical(pos))
                    return pos.parameter();
            }
        }

        throw new InvalidArgumentValueException();
    }

    /**
     * �^����ꂽ�p���??[�^�l�̂��̋�?�ɑ΂���?���?��𒲂ׂ�?B
     * <p/>
     * <ul>
     * <li> �^����ꂽ�p���??[�^�l P ��?A���̋�?�̒�`��� (�[�_��܂�) �Ѥ�ɂ����
     * ParameterValidity.PROPERLY_INSIDE ��Ԃ�?B
     * <li> �����ł͂Ȃ�?AP �����̋�?�̒�`�扺���?�������?A
     * ����?�����?�ł̋����ɒu���������l��
     * ��?�?ݒ肳��Ă��鉉�Z?�?����
     * �����̋��e��?�����?�����?�?��ɂ�
     * ParameterValidity.TOLERATED_LOWER_LIMIT ��Ԃ�?B
     * <li> �����ł͂Ȃ�?AP �����̋�?�̒�`��?���傫����?A
     * ����?�����?�ł̋����ɒu���������l��
     * ��?�?ݒ肳��Ă��鉉�Z?�?����
     * �����̋��e��?�����?�����?�?��ɂ�
     * ParameterValidity.TOLERATED_UPPER_LIMIT ��Ԃ�?B
     * <li> �����̂�����ł�Ȃ����?A
     * ParameterValidity.OUTSIDE ��Ԃ�?B
     * <ul>
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �p���??[�^�l�̂��̋�?�ɑ΂���?���?�
     * @see ParameterValidity
     */
    int parameterValidity(double param) {
        ParameterDomain pDomain = this.parameterDomain();

        if (pDomain.isInfinite() || pDomain.isPeriodic())
            return ParameterValidity.PROPERLY_INSIDE;

        double lower = pDomain.section().lower();
        double upper = pDomain.section().upper();
        double deltaDenom = 100;
        double delta = (upper - lower) / deltaDenom;
        double tol4Norm = this.getToleranceForDistance2();
        double endParam;
        Vector3D tangentVector;
        double tangentNorm = 0.0;
        double tol4Param;

        if (param < lower) {
            endParam = lower;
            while (endParam < upper) {
                tangentVector = this.tangentVector(endParam);
                if ((tangentNorm = tangentVector.norm()) > tol4Norm)
                    break;
                endParam += delta;
            }
            if (!(tangentNorm > tol4Norm))
                return ParameterValidity.OUTSIDE;

            tol4Param = this.getToleranceForDistance() / Math.sqrt(tangentNorm);
            if ((lower - param) < tol4Param)
                return ParameterValidity.TOLERATED_LOWER_LIMIT;
            else
                return ParameterValidity.OUTSIDE;
        }

        if (upper < param) {
            endParam = upper;
            while (endParam > lower) {
                tangentVector = this.tangentVector(endParam);
                if ((tangentNorm = tangentVector.norm()) > tol4Norm)
                    break;
                endParam -= delta;
            }
            if (!(tangentNorm > tol4Norm))
                return ParameterValidity.OUTSIDE;

            tol4Param = this.getToleranceForDistance() / Math.sqrt(tangentNorm);
            if ((param - upper) < tol4Param)
                return ParameterValidity.TOLERATED_UPPER_LIMIT;
            else
                return ParameterValidity.OUTSIDE;
        }

        return ParameterValidity.PROPERLY_INSIDE;
    }

    /**
     * ���̋�?��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    protected abstract ParametricCurve3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries);

    /**
     * ���̋�?��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    public synchronized ParametricCurve3D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator3D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        ParametricCurve3D transformed = (ParametricCurve3D) transformedGeometries.get(this);
        if (transformed == null) {
            transformed = this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);
            transformedGeometries.put(this, transformed);
        }
        return transformed;
    }

    /**
     * ���̋�?��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̊􉽗v�f
     */
    public synchronized ParametricCurve3D
    transformBy(CartesianTransformationOperator3D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        return transformBy(false, transformationOperator, transformedGeometries);
    }

    /**
     * ���̋�?��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŋt�ϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * this �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
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
     * ?�� this �� transformationOperator �ŋt�ϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �t�ϊ���̊􉽗v�f
     */
    public synchronized ParametricCurve3D
    reverseTransformBy(CartesianTransformationOperator3D transformationOperator,
                       java.util.Hashtable transformedGeometries) {
        return transformBy(true, transformationOperator, transformedGeometries);
    }

    /**
     * ���̋�?�|�����C���̕�����܂ނ��ۂ���Ԃ�?B
     * <p/>
     * ���ʂ� true ��?�?�?A
     * ���̋�?�� {@link BoundedCurve3D BoundedCurve3D} �ł���͂��ł���?B
     * </p>
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C����܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean hasPolyline() {
        return false;
    }

    /**
     * ���̋�?�|�����C���̕��������łł��Ă��邩�ۂ���Ԃ�?B
     * <p/>
     * ���ʂ� true ��?�?�?A
     * ���̋�?�� {@link BoundedCurve3D BoundedCurve3D} �ł���͂��ł���?B
     * </p>
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C��������܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean isComposedOfOnlyPolylines() {
        return false;
    }

}

