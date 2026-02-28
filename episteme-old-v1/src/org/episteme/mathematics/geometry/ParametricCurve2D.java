/*
 * �Q�����̃p���?�g���b�N�ȋ�?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ParametricCurve2D.java,v 1.5 2006/03/01 21:16:06 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.MachineEpsilon;
import org.episteme.util.FatalException;

/**
 * �Q�����̃p���?�g���b�N�ȋ�?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * ���̃N���X��?A��̎�?��l�ŕ\�����p���??[�^ t �̒l�ɂ�B�?A
 * �ʒu�����肳���Q�����̋�?� P(t) �S�ʂ��?��?�����\������?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.5 $, $Date: 2006/03/01 21:16:06 $
 * @see ParametricCurve3D
 */

public abstract class ParametricCurve2D extends AbstractParametricCurve {
    /**
     * ��?� Line �ł��邱�Ƃ���?�
     */
    static final int LINE_2D = 1;

    /**
     * ��?� Bounded Line �ł��邱�Ƃ���?�
     */
    static final int BOUNDED_LINE_2D = 2;

    /**
     * ��?� Circle �ł��邱�Ƃ���?�
     */
    static final int CIRCLE_2D = 10;

    /**
     * ��?� Ellipse �ł��邱�Ƃ���?�
     */
    static final int ELLIPSE_2D = 11;

    /**
     * ��?� Hyperbola �ł��邱�Ƃ���?�
     */
    static final int HYPERBOLA_2D = 12;

    /**
     * ��?� Parabola �ł��邱�Ƃ���?�
     */
    static final int PARABOLA_2D = 13;

    /**
     * ��?� Polyline �ł��邱�Ƃ���?�
     */
    static final int POLYLINE_2D = 20;

    /**
     * ��?� Bspline Curve �ł��邱�Ƃ���?�
     */
    static final int BSPLINE_CURVE_2D = 21;

    /**
     * ��?� Pure Bezier Curve �ł��邱�Ƃ���?�
     */
    static final int PURE_BEZIER_CURVE_2D = 22;

    /**
     * ��?� Trimmed Curve �ł��邱�Ƃ���?�
     */
    static final int TRIMMED_CURVE_2D = 23;

    /**
     * ��?� Composite Curve �ł��邱�Ƃ���?�
     */
    static final int COMPOSITE_CURVE_2D = 24;

    /**
     * ��?� Composite Curve Segment �ł��邱�Ƃ���?�
     */
    static final int COMPOSITE_CURVE_SEGMENT_2D = 25;

    /**
     * ��?� Polynomial Curve �ł��邱�Ƃ���?�
     */
    static final int POLYNOMIAL_CURVE_2D = 30;

    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    protected ParametricCurve2D() {
        super();
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ���?ۃ?�\�b�h?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    public abstract Point2D coordinates(double param);

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ���?ۃ?�\�b�h?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public abstract Vector2D tangentVector(double param);

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ���?ۃ?�\�b�h?B
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public abstract CurveCurvature2D curvature(double param);

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ���?ۃ?�\�b�h?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public abstract CurveDerivative2D evaluation(double param);

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
    public abstract PointOnCurve2D[] singular() throws IndefiniteSolutionException;

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
    public abstract PointOnCurve2D[] inflexion() throws IndefiniteSolutionException;

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
     * @see #projectFrom(Point2D)
     */
    protected PointOnCurve2D checkProjection(double Bparam,
                                             Point2D p,
                                             double dTol2) {
        PointOnCurve2D result = null;

        if (!isValid(Bparam)) {
            return null;
        }

        Point2D Bpnt = coordinates(Bparam);
        Vector2D Bpnt2A = p.subtract(Bpnt);
        double norm = Bpnt2A.norm();

        if (norm > dTol2) {
            CurveCurvature2D Bcurv = curvature(Bparam);
            double dist_from_Bnrml = Bpnt2A.zOfCrossProduct(Bcurv.normal());

            if (dist_from_Bnrml * dist_from_Bnrml > dTol2) {
                return null;
            }
        }

        try {
            return new PointOnCurve2D(Bpnt, this, Bparam, doCheckDebug);
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
        Point2D o1_crd, o2_crd;
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
    public abstract PointOnCurve2D[] projectFrom(Point2D point)
            throws IndefiniteSolutionException;

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ���?ۃ?�\�b�h?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_�� PointOnCurve2D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     * @see PointOnCurve2D
     */
    public abstract Polyline2D
    toPolyline(ParameterSection pint, ToleranceForDistance tol);

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ���?ۃ?�\�b�h?B
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     */
    public abstract BsplineCurve2D
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
    public abstract IntersectionPoint2D[]
    intersect(ParametricCurve2D mate)
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
    abstract IntersectionPoint2D[]
    intersect(Line2D mate, boolean doExchange)
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
    abstract IntersectionPoint2D[]
    intersect(Circle2D mate, boolean doExchange)
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
    abstract IntersectionPoint2D[]
    intersect(Ellipse2D mate, boolean doExchange)
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
    abstract IntersectionPoint2D[]
    intersect(Hyperbola2D mate, boolean doExchange)
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
    abstract IntersectionPoint2D[]
    intersect(Parabola2D mate, boolean doExchange)
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
     */
    abstract IntersectionPoint2D[]
    intersect(Polyline2D mate, boolean doExchange);

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    abstract IntersectionPoint2D[]
    intersect(PureBezierCurve2D mate, boolean doExchange);

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    abstract IntersectionPoint2D[]
    intersect(BsplineCurve2D mate, boolean doExchange);

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    abstract IntersectionPoint2D[]
    intersect(TrimmedCurve2D mate, boolean doExchange);

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    abstract IntersectionPoint2D[]
    intersect(CompositeCurveSegment2D mate, boolean doExchange);

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    abstract IntersectionPoint2D[]
    intersect(CompositeCurve2D mate, boolean doExchange);

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ����� Bspline ��?��?�߂钊?ۃ?�\�b�h?B
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ����� Bspline ��?�
     * @see WhichSide
     */
    public abstract BsplineCurve2D
    offsetByBsplineCurve(ParameterSection pint,
                         double magni,
                         int side,
                         ToleranceForDistance tol);

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ�����L��?��?�߂�?B
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ�����L��?�
     * @see WhichSide
     */
    public BoundedCurve2D
    offsetByBoundedCurve(ParameterSection pint,
                         double magni,
                         int side,
                         ToleranceForDistance tol) {
        return this.offsetByBsplineCurve(pint, magni, side, tol);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A���̋�?�̎w��̋�Ԃɂ�����t�B���b�g��?�߂�?B
     * <p/>
     * �t�B���b�g����?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param pint1  ���̋�?�̃p���??[�^���
     * @param side1  ���̋�?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param mate   ���̋�?�
     * @param pint2  ���̋�?�̃p���??[�^���
     * @param side2  ���̋�?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param radius �t�B���b�g���a
     * @return �t�B���b�g�̔z��
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     * @see WhichSide
     */
    public FilletObject2D[]
    fillet(ParameterSection pint1, int side1,
           ParametricCurve2D mate, ParameterSection pint2, int side2,
           double radius)
            throws IndefiniteSolutionException {
        return FiltCrvCrv2D.fillet(this, pint1, side1, mate, pint2, side2, radius);
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̋���?�?��?�߂钊?ۃ?�\�b�h (?���͎����Ȃ�) ?B
     * <p/>
     * ����?�?�?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ����?�?�̔z��
     */
    public abstract CommonTangent2D[]
    commonTangent(ParametricCurve2D mate);

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̋��ʖ@?��?�߂钊?ۃ?�\�b�h (?���͎����Ȃ�) ?B
     * <p/>
     * ���ʖ@?�?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ���ʖ@?�̔z��
     */
    public abstract CommonNormal2D[]
    commonNormal(ParametricCurve2D mate);

    /**
     * ���̋�?�̎�����Ԃ�?B
     * <p/>
     * ?�� 2 ��Ԃ�?B
     * </p>
     *
     * @return �Q�����Ȃ̂�?A?�� 2
     */
    public int dimension() {
        return 2;
    }

    /**
     * ���̋�?�Q�������ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �Q�����Ȃ̂�?A?�� true
     */
    public boolean is2D() {
        return true;
    }

    /**
     * ���̋�?�̗v�f��ʂ�Ԃ���?ۃ?�\�b�h?B
     *
     * @return �v�f���
     * @see #LINE_2D
     * @see #BOUNDED_LINE_2D
     * @see #CIRCLE_2D
     * @see #ELLIPSE_2D
     * @see #HYPERBOLA_2D
     * @see #PARABOLA_2D
     * @see #POLYLINE_2D
     * @see #BSPLINE_CURVE_2D
     * @see #PURE_BEZIER_CURVE_2D
     * @see #TRIMMED_CURVE_2D
     * @see #COMPOSITE_CURVE_2D
     * @see #COMPOSITE_CURVE_SEGMENT_2D
     * @see #POLYNOMIAL_CURVE_2D
     */
    abstract int type();

    /**
     * �^����ꂽ�_ P ���炱�̋�?�ւ̓��e�_�̓��?AP ��?ł�߂��_��Ԃ�?B
     * <p/>
     * ���e�_����?݂��Ȃ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param pnt ���e���̓_
     * @return ���e���̓_��?ł�߂����e�_
     * @see #projectFrom(Point2D)
     * @see #nearestProjectWithDistanceFrom(Point2D,double)
     */
    public PointOnCurve2D nearestProjectFrom(Point2D pnt) {
        PointOnCurve2D[] proj;
        try {
            proj = projectFrom(pnt);
        } catch (IndefiniteSolutionException e) {
            proj = new PointOnCurve2D[1];
            proj[0] = (PointOnCurve2D) e.suitable();
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
     * @see #projectFrom(Point2D)
     * @see #nearestProjectFrom(Point2D)
     */
    public PointOnCurve2D nearestProjectWithDistanceFrom(Point2D pnt,
                                                         double distance) {
        PointOnCurve2D[] proj;
        try {
            proj = projectFrom(pnt);
        } catch (IndefiniteSolutionException e) {
            proj = new PointOnCurve2D[1];
            proj[0] = (PointOnCurve2D) e.suitable();
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
     * @see #nearestProjectFrom(Point2D)
     * @see ConditionOfOperation
     */
    public double pointToParameter(Point2D pnt) {
        // ���e�����_�� identical ��?H
        PointOnCurve2D proj = nearestProjectFrom(pnt);
        if ((proj != null) && (pnt.identical(proj)))
            return proj.parameter();

        // ���[�_������?�?�?A����炪 identical ��?H
        if (this.isFinite() && this.isOpen()) {
            double param;

            param = this.parameterDomain().section().lower();
            if (pnt.identical(new PointOnCurve2D(this, param)))
                return param;

            param = this.parameterDomain().section().upper();
            if (pnt.identical(new PointOnCurve2D(this, param)))
                return param;
        }

        // �|�����C���̉�?�������?�?�?Aidentical �Ȓ��_�����邩?H
        if (this.hasPolyline() == true) {
            BoundedCurve2D bounded = (BoundedCurve2D) this;
            Polyline2D polyline = bounded.toPolyline(getToleranceForDistanceAsObject());
            PointOnCurve2D pos;

            for (int i = 0; i < polyline.nPoints(); i++) {
                pos = (PointOnCurve2D) polyline.pointAt(i);
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
        Vector2D tangentVector;
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
    protected abstract ParametricCurve2D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator2D transformationOperator,
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
    public synchronized ParametricCurve2D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator2D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        ParametricCurve2D transformed = (ParametricCurve2D) transformedGeometries.get(this);
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
    public synchronized ParametricCurve2D
    transformBy(CartesianTransformationOperator2D transformationOperator,
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
    public synchronized ParametricCurve2D
    reverseTransformBy(CartesianTransformationOperator2D transformationOperator,
                       java.util.Hashtable transformedGeometries) {
        return transformBy(true, transformationOperator, transformedGeometries);
    }

    /**
     * ���̋�?�|�����C���̕�����܂ނ��ۂ���Ԃ�?B
     * <p/>
     * ���ʂ� true ��?�?�?A
     * ���̋�?�� {@link BoundedCurve2D BoundedCurve2D} �ł���͂��ł���?B
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
     * ���̋�?�� {@link BoundedCurve2D BoundedCurve2D} �ł���͂��ł���?B
     * </p>
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C��������܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean isComposedOfOnlyPolylines() {
        return false;
    }
}

