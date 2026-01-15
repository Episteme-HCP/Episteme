/*
 * �Q���� : �~��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Circle2D.java,v 1.6 2006/05/20 23:25:37 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �Q���� : �~��\���N���X?B
 * <p/>
 * �~��?A���̒�?S�̈ʒu�Ƌ�?� X/Y ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement2D Axis2Placement2D}) position ��
 * ���a radius �Œ�`�����?B
 * </p>
 * <p/>
 * t ��p���??[�^�Ƃ���~ P(t) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(t) = position.location() + radius * (cos(t) * position.x() + sin(t) * position.y())
 * </pre>
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.6 $, $Date: 2006/05/20 23:25:37 $
 */

public class Circle2D extends Conic2D {

    /**
     * ���a?B
     *
     * @serial
     */
    private double radius;

    /**
     * ���a��?ݒ肷��?B
     * <p/>
     * radius �̒l��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param radius ���a
     * @see InvalidArgumentValueException
     */
    private void setRadius(double radius) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();

        if (radius < dTol) {
            throw new InvalidArgumentValueException();
        }
        this.radius = radius;
    }

    /**
     * ��?�?W�n�Ɣ��a��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * radius �̒l��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param position ��?S�Ƌ�?� X/Y ���̕�����?�?W�n
     * @param radius   ���a
     * @see InvalidArgumentValueException
     */
    public Circle2D(Axis2Placement2D position, double radius) {
        super(position);
        setRadius(radius);
    }

    /**
     * ��?S�Ɣ��a��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?\�z�����~�̋�?� X/Y ���̕���?A
     * �O�??[�o����?W�n�� X/Y ���̕��Ɉ�v����?B
     * </p>
     * <p/>
     * center �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * radius �̒l��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param center ��?S
     * @param radius ���a
     * @see InvalidArgumentValueException
     */
    public Circle2D(Point2D center, double radius) {
        super(new Axis2Placement2D(center, Vector2D.xUnitVector));
        setRadius(radius);
    }

    /**
     * �ʉ߂���O�_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?\�z�����~�̋�?� X/Y ���̕���?A
     * �O�??[�o����?W�n�� X/Y ���̕��Ɉ�v����?B
     * </p>
     * <p/>
     * pnt1, pnt2, pnt3 �̂����ꂩ�� null ��?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     * <p/>
     * �܂�?Apnt1, pnt2, pnt3 ����?�?�Ԃɂ���?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param pnt1 �~?�̈�_
     * @param pnt2 �~?�̈�_
     * @param pnt3 �~?�̈�_
     * @see Point2D#center(Point2D,Point2D)
     * @see InvalidArgumentValueException
     */
    public Circle2D(Point2D pnt1, Point2D pnt2, Point2D pnt3) {
        super(new Axis2Placement2D(Point2D.center(pnt1, pnt2, pnt3),
                Vector2D.xUnitVector));
        setRadius(position().location().subtract(pnt1).length());
    }

    /**
     * ���̉~�̔��a��Ԃ�?B
     *
     * @return ���a
     */
    public double radius() {
        return this.radius;
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ�?B
     * <p/>
     * pint �ŗ^�������Ԃ� [0, 2 * PI] �Ɏ�܂BĂ���K�v�͂Ȃ�?B
     * �܂�?Apint �̑?���l�͕��ł©�܂�Ȃ�?B
     * </p>
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     */
    public double length(ParameterSection pint) {
        return radius() * Math.abs(pint.increase());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    public Point2D coordinates(double param) {
        param = parameterDomain().wrap(param);
        Point2D center = position().location();
        double ecos = Math.cos(param) * radius;
        double esin = Math.sin(param) * radius;
        Vector2D x = position().x().multiply(ecos);
        Vector2D y = position().y().multiply(esin);

        return center.add(x.add(y));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector2D tangentVector(double param) {
        param = parameterDomain().wrap(param);
        double ecos = Math.cos(param) * radius;
        double esin = Math.sin(param) * radius;
        Vector2D x = position().x().multiply(-esin);
        Vector2D y = position().y().multiply(ecos);

        return x.add(y);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public CurveCurvature2D curvature(double param) {
        param = parameterDomain().wrap(param);
        double ucos = Math.cos(param);
        double usin = Math.sin(param);
        Vector2D x = position().x().multiply(-ucos);
        Vector2D y = position().y().multiply(-usin);

        return new CurveCurvature2D(1.0 / radius, x.add(y));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative2D evaluation(double param) {
        param = parameterDomain().wrap(param);
        double ecos = Math.cos(param) * radius;
        double esin = Math.sin(param) * radius;
        Point2D center = position().location();
        Vector2D xcos = position().x().multiply(ecos);
        Vector2D ysin = position().y().multiply(esin);
        Vector2D xsin = position().x().multiply(esin);
        Vector2D ycos = position().y().multiply(ecos);

        Point2D d0 = center.add(xcos.add(ysin));
        Vector2D d1 = ycos.add(xsin.multiply(-1.0));
        Vector2D d2 = xcos.add(ysin).multiply(-1.0);

        return new CurveDerivative2D(d0, d1, d2);
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * �^����ꂽ�_�����̉~�̒�?S�Ɉ�v���Ȃ���?A
     * ?�ɓ�̓��e�_��Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�_�Ƃ��̉~�̒�?S�Ƃ̋�����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����
     * ?�����?�?��ɂ�?A
     * �p���??[�^�l 0 �̓_�� suitable �Ƃ���
     * IndefiniteSolutionException �̗�O�𓊂���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s�� (���e���̓_���~�̒�?S�Ɉ�v����)
     */
    public PointOnCurve2D[] projectFrom(Point2D point)
            throws IndefiniteSolutionException {
        // unit direction vector from center to point
        Vector2D eduvec = point.subtract(position().location());

        // check length & unitize
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();

        if (eduvec.length() < dTol) {
            // any point
            PointOnCurve2D p;

            try {
                p = new PointOnCurve2D(this, 0, doCheckDebug);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

            throw new IndefiniteSolutionException(p);
        }

        eduvec = eduvec.unitized();

        Vector2D x_axis = position().x();

        // angle between x_axis & eduvec

        // get vector angle, XXX: should be moved to Vector2D
        double eangle = Math.acos(x_axis.dotProduct(eduvec));
        if (x_axis.zOfCrossProduct(eduvec) < 0.0)
            eangle = GeometryUtils.PI2 - eangle;
        double eangle2 = eangle + Math.PI;
        if (eangle2 >= 2 * Math.PI)
            eangle2 -= 2 * Math.PI;

        // get the projected
        try {
            PointOnCurve2D[] prj = {
                    new PointOnCurve2D(this, eangle, doCheckDebug),
                    new PointOnCurve2D(this, eangle2, doCheckDebug)
            };

            return prj;
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ�����?A
     * ��Ԃ̗��[�싂Ԍ�����?łף�ꂽ�_�̃p���??[�^�l��?�߂�?B
     * <p/>
     * ���̃?�\�b�h��
     * {@link Conic2D#toPolyline(ParameterSection,ToleranceForDistance)
     * Conic2D.toPolyline(ParameterSection, ToleranceForDistance)}
     * �̓Ք�ŌĂ�?o����邽�߂ɗp�ӂ���Ă���?B
     * ���̃N���X�ł�
     * toPolyline(ParameterSection, ToleranceForDistance)
     * ��I?[�o?[���C�h���Ă���̂�?A
     * ���̃?�\�b�h�͌Ă�?o����邱�Ƃ͂Ȃ�?B
     * </p>
     * <p/>
     * ���̃?�\�b�h��?�� FatalException �̗�O�𓊂���?B
     * </p>
     *
     * @param left  ?��[ (��ԉ���) �̃p���??[�^�l
     * @param right �E�[ (���?��) �̃p���??[�^�l
     * @return ?łף�ꂽ�_�̃p���??[�^�l
     */
    double getPeak(double left, double right) {
        // This should never be called because Circle provides
        // its own toPolyline().
        throw new FatalException();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��?A
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve2D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * �Ȃ�?A���ʂƂ��ē�����|�����C�����_��?k�ނ���悤��?�?��ɂ�
     * ZeroLengthException �̗�O��?�����?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     * @see PointOnCurve2D
     * @see ZeroLengthException
     */
    public Polyline2D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {

        double sa = parameterDomain().wrap(pint.start());
        double inc = pint.increase();

        int no_intvls = toPolylineNDivision(radius(), inc, tol);
        double atheta = inc / no_intvls;

        Point2D[] pnts = new Point2D[no_intvls + 1];

        for (int i = 0; i < no_intvls + 1; i++)
            pnts[i] = new PointOnCurve2D(this, sa + (atheta * i), doCheckDebug);

        if (no_intvls == 1 && pnts[0].identical(pnts[1]))
            throw new ZeroLengthException();

        return new Polyline2D(pnts);
    }

    /**
     * �^����ꂽ���a��?�~�̎w��͈̔͂�?A
     * �^����ꂽ��?��Œ�?�ߎ�����|�����C����?�?�����̂�
     * �K���� (���̎w��͈͂�) ����?���Ԃ�?B
     *
     * @param radius   ���a
     * @param increase ��?�ߎ������Ԃ̑?���l
     * @param tol      �����̋��e��?�
     * @return ��Ԃ̕���?�
     * @see #toPolyline(ParameterSection,ToleranceForDistance)
     */
    static int toPolylineNDivision(double radius,
                                   double increase,
                                   ToleranceForDistance tol) {
        // theta < 2 acos ((R - TOL) / R)
        double etheta = 2.0 * Math.acos((radius - Math.abs(tol.value()))
                / radius);
        return Math.round((float) Math.floor(Math.abs(increase) / etheta)) + 1;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̗��Ԃ�?B
     *
     * @param nCurves   ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?��?�
     * @param increaseP ��̗L�?�x�W�G��?�?Č�����p���??[�^��Ԃ̑傫��
     * @param pint      ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̔z��
     * @see #toPolyBezierCurves(ParameterSection)
     */
    private PureBezierCurve2D[] toPolyBezierCurves(int nCurves,
                                                   double increaseP,
                                                   ParameterSection pint) {
        double startP;
        int i;

        PureBezierCurve2D[] bzcs = new PureBezierCurve2D[nCurves];

        for (i = 0, startP = pint.start();
             i < nCurves;
             i++, startP += increaseP) {

            ParameterSection pintl = new ParameterSection(startP, increaseP);
            Point2D[] controlPoints = this.getControlPointsOfBezierCurve(pintl);
            double[] weights = {1.0, 1.0, 1.0};

            double shoulderParam = (pintl.lower() + pintl.upper()) / 2.0;
            Point2D shoulderPoint = this.coordinates(shoulderParam);
            Point2D middlePoint = controlPoints[0].midPoint(controlPoints[2]);

            double vvv = Math.sqrt(shoulderPoint.subtract(middlePoint).norm() /
                    controlPoints[1].subtract(middlePoint).norm());
            // if (Math.abs(increaseP) > Math.PI) vvv = (- vvv);

            weights[1] = vvv / (1.0 - vvv);

            bzcs[i] = new PureBezierCurve2D(controlPoints, weights);
        }

        return bzcs;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̗��Ԃ�?B
     * <p/>
     * pint �̑?���l��?�Βl�� (2 * ��) ��?��?�?��ɂ�?A
     * ����� (2 * ��) �ƌ��Ȃ���?��?����?B
     * </p>
     *
     * @param pint ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̔z��
     */
    public PureBezierCurve2D[] toPolyBezierCurves(ParameterSection pint) {
        double increase = pint.increase();
        int nCurves;
        double increaseP;

        if (Math.abs(increase) > GeometryUtils.PI2) {
            nCurves = 3;
            increaseP = (increase > 0.0) ? (GeometryUtils.PI2 / 3) : (-GeometryUtils.PI2 / 3);
        } else if (Math.abs(increase) > (4 * GeometryUtils.PI2 / 5)) {
            nCurves = 3;
            increaseP = (increase) / 3;
        } else if (Math.abs(increase) > (4 * Math.PI / 5)) {
            nCurves = 2;
            increaseP = (increase) / 2;
        } else {
            nCurves = 1;
            increaseP = (increase);
        }

        return toPolyBezierCurves(nCurves, increaseP, pint);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̗��Ԃ�?B
     * <p/>
     * nCurves �̒l�� 0 �ȉ����邢�� 4 ��?��?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     * <p/>
     * �܂�?Apint �̑?���l�ɑ΂��� nCurves �̒l��?���������?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @param nCurves ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?��?� (1 �Ȃ��� 3)
     * @param pint    ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�x�W�G��?�̔z��
     * @see #toPolyBezierCurves(ParameterSection)
     */
    PureBezierCurve2D[] toPolyBezierCurvesOfN(int nCurves,
                                              ParameterSection pint) {
        double increase = pint.increase();
        double increaseP;

        increaseP = (increase) / nCurves;

        switch (nCurves) {
            case 3:
                if (Math.abs(increase) > GeometryUtils.PI2)
                    increaseP = (increase > 0.0) ? (GeometryUtils.PI2 / 3) : (-GeometryUtils.PI2 / 3);
                break;

            case 2:
                if (Math.abs(increase) > (4 * GeometryUtils.PI2 / 5))
                    throw new FatalException("Can not convert with given parameters");
                break;

            case 1:
                if (Math.abs(increase) > (4 * Math.PI / 5))
                    throw new FatalException("Can not convert with given parameters");
                increaseP = (increase);
                break;

            default:
                throw new ParameterOutOfRange("value of nCurves is out of range");
        }

        return toPolyBezierCurves(nCurves, increaseP, pint);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?��Ԃ�?B
     * <p/>
     * pint �̑?���l��?�Βl�� (2 * ��) ��?��?�?��ɂ�?A
     * ����� (2 * ��) �ƌ��Ȃ���?��?��?A
     * �����`���̋�?��Ԃ�?B
     * </p>
     *
     * @param pint ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?�
     * @see #toPolyBezierCurves(ParameterSection)
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        PureBezierCurve2D[] bzcs = this.toPolyBezierCurves(pint);
        boolean closed =
                (Math.abs(pint.increase()) >= GeometryUtils.PI2) ? true : false;

        return Conic2D.convertPolyBezierCurvesToOneBsplineCurve(bzcs, closed);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?��Ԃ�?B
     * <p/>
     * nSegments �̒l�� 0 �ȉ����邢�� 4 ��?��?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     * <p/>
     * �܂�?Apint �̑?���l�ɑ΂��� nSegments �̒l��?���������?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     * <p/>
     * pint �̑?���l��?�Βl�� (2 * ��) ��?��?�?��ɂ�?A
     * �����`���̋�?��Ԃ�?B
     * </p>
     *
     * @param nSegments ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?�̃Z�O�?���g?� (1 �Ȃ��� 3)
     * @param pint      ?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�?�a�X�v���C����?�
     * @see #toPolyBezierCurvesOfN(int,ParameterSection)
     * @see Conic2D#convertPolyBezierCurvesToOneBsplineCurve(PureBezierCurve2D[],boolean)
     */
    BsplineCurve2D toBsplineCurveOfNSegments(int nSegments,
                                             ParameterSection pint) {
        PureBezierCurve2D[] bzcs = this.toPolyBezierCurvesOfN(nSegments, pint);
        boolean closed =
                (Math.abs(pint.increase()) >= GeometryUtils.PI2) ? true : false;

        return Conic2D.convertPolyBezierCurvesToOneBsplineCurve(bzcs, closed);
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����~�̂Ƃ���?A
     * ��~�̒�?S�Ԃ̋����Ɠ�~�̔��a��?���?A
     * �Ƃ�Ɍ�?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�?A
     * ��~�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException mate ��~��?A��~�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̉~�� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?�����?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ���̉~�� poly �̌�_��\����?�����?���
     */
    DoublePolynomial makePoly(DoublePolynomial[] poly) {
        DoublePolynomial xPoly = (DoublePolynomial) poly[0].multiply(poly[0]);
        DoublePolynomial yPoly = (DoublePolynomial) poly[1].multiply(poly[1]);
        double rad2 = radius() * radius();
        boolean isPoly = poly.length < 3;
        int degree = xPoly.degree();
        double[] coef = new double[degree + 1];

        if (isPoly) {
            for (int j = 0; j <= degree; j++)
                coef[j] = xPoly.getCoefficientAsDouble(j) + yPoly.getCoefficientAsDouble(j);
            coef[0] -= rad2;
        } else {
            DoublePolynomial wPoly = (DoublePolynomial) poly[2].multiply(poly[2]);
            for (int j = 0; j <= degree; j++)
                coef[j] = xPoly.getCoefficientAsDouble(j) + yPoly.getCoefficientAsDouble(j) -
                        (rad2 * wPoly.getCoefficientAsDouble(j));
        }
        return new DoublePolynomial(coef);
    }

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ��邩�ۂ���`�F�b�N����?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �^����ꂽ�_�����̋�?�?�ɂ���� true?A�����łȂ���� false
     */
    boolean checkSolution(Point2D point) {
        double dTol = getToleranceForDistance();
        return Math.abs(point.toVector2D().length() - radius()) < dTol;
    }

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ����̂Ƃ���?A
     * ���̓_�̋�?�?�ł̃p���??[�^�l��?�߂�?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �p���??[�^�l
     */
    double getParameter(Point2D point) {
        double cos = point.x() / radius();
        if (cos > 1.0) cos = 1.0;
        if (cos < -1.0) cos = -1.0;
        double acos = Math.acos(cos);
        if (point.y() < 0.0) acos = Math.PI * 2 - acos;

        return acos;
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsLinCnc2D#intersection(Line2D,Circle2D,boolean)
     * IntsLinCnc2D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see IntsLinCnc2D
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        IntsLinCnc2D doObj = new IntsLinCnc2D(mate, this);
        return doObj.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��~�̒�?S�Ԃ̋����Ɠ�~�̔��a��?���?A
     * �Ƃ�Ɍ�?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�?A
     * ��~�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��~�̒�?S�Ԃ̋����Ɠ�~�̔��a�̘a���邢��?��ɂ�B�?A
     * ��_��?���?�?���������?��?A
     * �􉽓I�ɉ⢂Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException ��~�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        Point2D center1 = position().location();
        Point2D center2 = mate.position().location();
        Vector2D evec = center2.subtract(center1);    // this -> mate
        double edist = evec.length();
        double egap1 = edist - (radius() + mate.radius());
        double egap2 = Math.abs(radius() - mate.radius()) - edist;
        double dTol = getToleranceForDistance();
        Point2D pnt1;
        Point2D pnt2;

        if (egap1 > dTol || egap2 > dTol) {
            return new IntersectionPoint2D[0];
        }
        evec = evec.unitized();

        if (egap1 > -dTol) {
            // 1 intersection (external contact)
            pnt1 = center1.add(evec.multiply(this.radius()));
            pnt2 = center2.add(evec.multiply(-mate.radius()));
            pnt1 = pnt1.linearInterpolate(pnt2, 0.5);
            IntersectionPoint2D pnts[] = {
                    new IntersectionPoint2D(this, this.pointToParameter(pnt1),
                            mate, mate.pointToParameter(pnt1), doCheckDebug)
            };
            if (doExchange)
                pnts[0] = pnts[0].exchange();

            return pnts;
        } else if (egap2 > -dTol) {
            // 1 intersection (internal contact)
            egap2 = radius() - mate.radius();
            if (Math.abs(egap2) < dTol) {
                // 2 circles are same
                IntersectionPoint2D ip =
                        new IntersectionPoint2D(this, 0.0, mate, 0.0, doCheckDebug);
                if (doExchange)
                    ip = ip.exchange();
                throw new IndefiniteSolutionException(ip);
            }
            if (egap2 < 0.0)

                evec = evec.multiply(-1);
            pnt1 = center1.add(evec.multiply(this.radius()));
            pnt2 = center2.add(evec.multiply(mate.radius()));
            pnt1 = pnt1.linearInterpolate(pnt2, 0.5);
            IntersectionPoint2D pnts[] = {
                    new IntersectionPoint2D(this, this.pointToParameter(pnt1),
                            mate, mate.pointToParameter(pnt1), doCheckDebug)
            };
            if (doExchange)
                pnts[0] = pnts[0].exchange();

            return pnts;

        } else {
            // 2 intersections
            double edfatl = (radius() * radius() -
                    mate.radius() * mate.radius() +
                    edist * edist) / (2 * edist);
            double e2dfatl = Math.sqrt(radius() * radius() - edfatl * edfatl);
            Point2D eill = center1.add(evec.multiply(edfatl));
            Vector2D elvec = evec.verticalVector();

            pnt1 = eill.add(elvec.multiply(e2dfatl));
            pnt2 = eill.add(elvec.multiply(-e2dfatl));

            IntersectionPoint2D pnts[] = {
                    new IntersectionPoint2D(this, this.pointToParameter(pnt1),
                            mate, mate.pointToParameter(pnt1), doCheckDebug),
                    new IntersectionPoint2D(this, this.pointToParameter(pnt2),
                            mate, mate.pointToParameter(pnt2), doCheckDebug)
            };
            if (doExchange)
                for (int i = 0; i < 2; i++)
                    pnts[i] = pnts[i].exchange();

            return pnts;
        }
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsCirCnc2D#intersection(Circle2D,Ellipse2D,boolean)
     * IntsCirCnc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see IntsCirCnc2D
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        IntsCirCnc2D doObj = new IntsCirCnc2D();
        return doObj.intersection(this, mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsCirCnc2D#intersection(Circle2D,Parabola2D,boolean)
     * IntsCirCnc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see IntsCirCnc2D
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        IntsCirCnc2D doObj = new IntsCirCnc2D();
        return doObj.intersection(this, mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsCirCnc2D#intersection(Circle2D,Hyperbola2D,boolean)
     * IntsCirCnc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see IntsCirCnc2D
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        IntsCirCnc2D doObj = new IntsCirCnc2D();
        return doObj.intersection(this, mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �|�����C���̃N���X��?u�|�����C�� vs. �~?v�̌�_���Z�?�\�b�h
     * {@link Polyline2D#intersect(Circle2D,boolean)
     * Polyline2D.intersect(Circle2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �g������?�̃N���X��?u�g������?� vs. �~?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve2D#intersect(Circle2D,boolean)
     * TrimmedCurve2D.intersect(Circle2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �~?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment2D#intersect(Circle2D,boolean)
     * CompositeCurveSegment2D.intersect(Circle2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�̃N���X��?u��?���?� vs. �~?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve2D#intersect(Circle2D,boolean)
     * CompositeCurve2D.intersect(Circle2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ����� Bspline ��?��?�߂�?B
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ����� Bspline ��?�
     * @see WhichSide
     */
    public BsplineCurve2D
    offsetByBsplineCurve(ParameterSection pint,
                         double magni,
                         int side,
                         ToleranceForDistance tol) {
        Ofst2D doObj = new Ofst2D(this, pint, magni, side, tol);
        return doObj.offset();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ�����L��?��?�߂�?B
     * <p/>
     * ���ʂƂ��ĕԂ����L��?��?A
     * Circle2D �̃C���X�^���X����?�Ƃ���
     * {@link TrimmedCurve2D TrimmedCurve2D}
     * �̃C���X�^���X�ł���?B
     * </p>
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
        Circle2D basisCircle;

        if (side == WhichSide.RIGHT) {
            basisCircle = new Circle2D(this.position(), (this.radius() + magni));
            pint = pint;
        } else {
            if (!(magni > radius)) {
                basisCircle = new Circle2D(this.position(), (this.radius() - magni));
                pint = pint;
            } else {
                basisCircle = new Circle2D(this.position(), (magni - this.radius()));
                pint = new ParameterSection
                        (this.parameterDomain().wrap(pint.start() + Math.PI),
                                pint.increase());
            }
        }

        return new TrimmedCurve2D(basisCircle, pint);
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̋���?�?��?�߂�?B
     * <p/>
     * ����?�?�?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����_�ł͎�����Ă��Ȃ�����?A
     * UnsupportedOperationException	�̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ����?�?�̔z��
     * @throws UnsupportedOperationException ���܂̂Ƃ���?A������Ȃ��@�\�ł���
     */
    public CommonTangent2D[] commonTangent(ParametricCurve2D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̋��ʖ@?��?�߂�?B
     * <p/>
     * ���ʖ@?�?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����_�ł͎�����Ă��Ȃ�����?A
     * UnsupportedOperationException	�̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ���ʖ@?�̔z��
     * @throws UnsupportedOperationException ���܂̂Ƃ���?A������Ȃ��@�\�ł���
     */
    public CommonNormal2D[] commonNormal(ParametricCurve2D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     * <p/>
     * �L�Ŏ��I�ȃp���??[�^��`���Ԃ�?B
     * �Ȃ�?A�v���C�}���ȗL���Ԃ� [0, (2 * ��)] �ł���?B
     * </p>
     *
     * @return �L�Ŏ��I�ȃp���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        try {
            return new ParameterDomain(true, 0, 2 * Math.PI);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ���̋�?�􉽓I�ɕ��Ă��邩�ۂ���Ԃ�?B
     * <p/>
     * �~�Ȃ̂�?A?�� true ��Ԃ�?B
     * </p>
     *
     * @return �~�Ȃ̂�?A?�� <code>false</code>
     */
    boolean getClosedFlag() {
        return true;
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#CIRCLE_2D ParametricCurve2D.CIRCLE_2D}
     */
    int type() {
        return CIRCLE_2D;
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
    protected synchronized ParametricCurve2D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator2D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Axis2Placement2D tPosition =
                this.position().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        double tRadius;
        if (reverseTransform != true)
            tRadius = transformationOperator.transform(this.radius());
        else
            tRadius = transformationOperator.reverseTransform(this.radius());
        return new Circle2D(tPosition, tRadius);
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
        writer.println(indent_tab + "\tposition");
        position().output(writer, indent + 2);
        writer.println(indent_tab + "\tradius " + radius);
        writer.println(indent_tab + "End");
    }

    /**
     * ��?S�Ǝn�_?A?I�_����~�ʂ�?�?�����?B
     * <p/>
     * �~�ʂ̓g������?�ŕ\�������?B
     * </p>
     * <p/>
     * �g������?�̕��?�ƂȂ�~�̋�?� X ���̕���?A
     * ��?S����n�_�֌���ƂȂ�?B
     * �܂�?A�~�̔��a��?A?u��?S?|�n�_�Ԃ̋���?v��?u��?S?|?I�_�Ԃ̋���?v�̕��ϒl�ƂȂ�?B
     * ?u��?S?|�n�_�Ԃ̋���?v��?u��?S?|?I�_�Ԃ̋���?v��?��ɂ��Ă͓BɈӎ����Ă��Ȃ�?B
     * </p>
     * <p/>
     * �Ȃ�?A�~�ʂ�?A�n�_����?I�_�Ɍ�p���??[�^�?���l��?�Βl��
     * �΂�z���Ȃ��悤��?�?������?B
     * ��B�?A�~�ʂ�\���g������?�̃p���??[�^�?���l�͕��ɂȂ邱�Ƃ �蓾��?B
     * </p>
     *
     * @param center     ��?S
     * @param startPoint �n�_
     * @param endPoint   ?I�_
     * @return �~�ʂ�\���g������?�
     */
    public static TrimmedCurve2D makeTrimmedCurve(Point2D center,
                                                  Point2D startPoint,
                                                  Point2D endPoint) {
        Vector2D sVec = startPoint.subtract(center);
        Vector2D eVec = endPoint.subtract(center);
        double radius = (sVec.length() + eVec.length()) / 2.0;

        sVec = sVec.unitized();
        eVec = eVec.unitized();

        double iParam = sVec.angleWith(eVec);
        if (Math.abs(iParam) > Math.PI)
            iParam -= GeometryUtils.PI2;

        return new TrimmedCurve2D
                (new Circle2D(new Axis2Placement2D(center, sVec), radius),
                        new ParameterSection(0.0, iParam));
    }
}
