/*
 * �R���� : �~��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Circle3D.java,v 1.6 2006/05/20 23:25:38 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.analysis.polynomials.DoublePolynomial;
import org.episteme.util.FatalException;

import java.io.PrintWriter;

/**
 * �R���� : �~��\���N���X
 * <p/>
 * �~��?A���̒�?S�̈ʒu�Ƌ�?� X/Y ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement3D Axis2Placement3D}) position ��
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
 * @version $Revision: 1.6 $, $Date: 2006/05/20 23:25:38 $
 */

public class Circle3D extends Conic3D {

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
     * @param position ��?S�Ƌ�?� X/Y/Z ���̕�����?�?W�n
     * @param radius   ���a
     * @see InvalidArgumentValueException
     */
    public Circle3D(Axis2Placement3D position, double radius) {
        super(position);
        setRadius(radius);
    }

    /**
     * ��?S?A�@?�x�N�g���Ɣ��a��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?\�z�����~�̋�?� X/Y ���̕���?A
     * ���̃R���X�g���N�^�̓Ք�Ō��肷��?B
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
     * @param normal �@?�x�N�g��
     * @param radius ���a
     * @see InvalidArgumentValueException
     */
    public Circle3D(Point3D center, Vector3D normal, double radius) {
        super(new Axis2Placement3D(center, normal,
                normal.verticalVector().unitized()));
        setRadius(radius);
    }

    /**
     * �^����ꂽ�O�_��܂ޕ��ʂ�\����?�?W�n��?�?�����?B
     * <p/>
     * �^����ꂽ�O�_�̒�?S���?�?W�n�̌��_�Ƃ���?B
     * </p>
     * <p/>
     * (pnt2 - pnt1) �̕����?� X ���̕��Ƃ���?B
     * </p>
     * <p/>
     * �^����ꂽ�O�_����?�?�Ԃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param pnt1 ����?�̈�_
     * @param pnt2 ����?�̈�_
     * @param pnt3 ����?�̈�_
     * @return �O�_��܂ޕ��ʂ�\����?�?W�n
     * @see InvalidArgumentValueException
     */
    static private Axis2Placement3D placement
            (Point3D pnt1, Point3D pnt2, Point3D pnt3) {
        if (pnt1.identical(pnt2) || pnt1.identical(pnt3) || pnt2.identical(pnt3))
            throw new InvalidArgumentValueException();

        Vector3D vec2 = pnt2.subtract(pnt1); // X-axis direction
        Vector3D vec3 = pnt3.subtract(pnt1);

        if (vec2.identicalDirection(vec3))
            throw new InvalidArgumentValueException();

        Vector3D normal = vec2.crossProduct(vec3); // Z-axis direction

        Line3D line2 = new Line3D(pnt1.linearInterpolate(pnt2, 0.5),
                vec2.crossProduct(normal));

        Line3D line3 = new Line3D(pnt1.linearInterpolate(pnt3, 0.5),
                vec3.crossProduct(normal));
        IntersectionPoint3D isec;

        try {
            isec = line2.intersect1Line(line3);
            if (isec == null)
                throw new FatalException();
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        return new Axis2Placement3D(isec.coordinates(), normal, vec2);
    }

    /**
     * �ʉ߂���O�_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * (pnt2 - pnt1) �̕����?� X ���̕��Ƃ���?B
     * </p>
     * <p/>
     * �^����ꂽ�O�_����?�?�Ԃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param pnt1 �~?�̈�_
     * @param pnt2 �~?�̈�_
     * @param pnt3 �~?�̈�_
     * @see InvalidArgumentValueException
     */
    public Circle3D(Point3D pnt1, Point3D pnt2, Point3D pnt3) {
        super(placement(pnt1, pnt2, pnt3));
        setRadius(position().location().subtract(pnt1).length());
    }

    /**
     * ���̉~�̔��a��p����?A�^����ꂽ�ʒu�ƌX���łQ�����̉~��?�?�����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param position �Q�����̉~�̈ʒu�ƌX������?�?W�n
     * @return �Q�����̉~
     * @see InvalidArgumentValueException
     */
    Conic2D toLocal2D(Axis2Placement2D position) {
        return new Circle2D(position, radius);
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
    public Point3D coordinates(double param) {
        param = parameterDomain().wrap(param);
        Point3D center = position().location();
        double ecos = Math.cos(param) * radius;
        double esin = Math.sin(param) * radius;
        Vector3D x = position().x().multiply(ecos);
        Vector3D y = position().y().multiply(esin);

        return center.add(x.add(y));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector3D tangentVector(double param) {
        param = parameterDomain().wrap(param);
        double ecos = Math.cos(param) * radius;
        double esin = Math.sin(param) * radius;
        Vector3D x = position().x().multiply(-esin);
        Vector3D y = position().y().multiply(ecos);

        return x.add(y);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public CurveCurvature3D curvature(double param) {
        param = parameterDomain().wrap(param);
        double ucos = Math.cos(param);
        double usin = Math.sin(param);
        Vector3D x = position().x().multiply(-ucos);
        Vector3D y = position().y().multiply(-usin);

        return new CurveCurvature3D(1.0 / radius, x.add(y));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative3D evaluation(double param) {
        param = parameterDomain().wrap(param);
        double ecos = Math.cos(param) * radius;
        double esin = Math.sin(param) * radius;
        Point3D center = position().location();
        Vector3D xcos = position().x().multiply(ecos);
        Vector3D ysin = position().y().multiply(esin);
        Vector3D xsin = position().x().multiply(esin);
        Vector3D ycos = position().y().multiply(ecos);

        Point3D d0 = center.add(xcos.add(ysin));
        Vector3D d1 = ycos.add(xsin.multiply(-1.0));
        Vector3D d2 = xcos.add(ysin).multiply(-1.0);
        Vector3D d3 = ycos.multiply(-1.0).add(xsin);

        return new CurveDerivative3D(d0, d1, d2, d3);
    }

    /**
     * ���̉~�̃p���??[�^�l 0 �̓_��
     * ?�?�_ ({@link PointOnCurve3D PointOnCurve3D}) �� suitable �Ƃ���
     * IndefiniteSolutionException �̗�O��?�����?B
     */
    private void indefiniteFoot()
            throws IndefiniteSolutionException {
        PointOnCurve3D p;

        try {
            p = new PointOnCurve3D(this, 0, doCheckDebug);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        throw new IndefiniteSolutionException(p);
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * �^����ꂽ�_�����̉~�̒�?S��ʂ��?� Z ��?�ɂȂ���?A
     * ?�ɓ�̓��e�_��Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�_�Ƃ��̉~�̒�?S��ʂ��?� Z ���Ƃ̋�����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����
     * ?�����?�?��ɂ�?A
     * �p���??[�^�l 0 �̓_�� suitable �Ƃ���
     * IndefiniteSolutionException �̗�O�𓊂���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s�� (���e���̓_���~�̒�?S��ʂ��?� Z ��?�ɂ���)
     */
    public PointOnCurve3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();
        double aTol = condition.getToleranceForAngle();

        // vector from center to point -> V
        Vector3D eV = point.subtract(position().location());

        if (eV.length() < dTol)
            indefiniteFoot();

        eV = eV.unitized();

        double edot = eV.dotProduct(position().z());

        // angle of V & normal is less than aTol ?
        if (Math.abs(edot) > Math.cos(aTol))
            indefiniteFoot();

        // cross of V & normal -> U
        Vector3D eU = eV.crossProduct(position().z());

        // cross of normal & U -> V
        eV = position().z().crossProduct(eU).unitized();

        double eangle = position().x().angleWith(eV, position().z());
        double eangle2 = eangle + Math.PI;
        if (eangle2 >= 2 * Math.PI)
            eangle2 -= 2 * Math.PI;

        // get the projected

        PointOnCurve3D[] prj = {new PointOnCurve3D(this, eangle, doCheckDebug),
                new PointOnCurve3D(this, eangle2, doCheckDebug)};
        return prj;
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ�����?A
     * ��Ԃ̗��[�싂Ԍ�����?łף�ꂽ�_�̃p���??[�^�l��?�߂�?B
     * <p/>
     * ���̃?�\�b�h��
     * {@link Conic3D#toPolyline(ParameterSection,ToleranceForDistance)
     * Conic3D.toPolyline(ParameterSection, ToleranceForDistance)}
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
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve3D ��
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
     * @see PointOnCurve3D
     * @see ZeroLengthException
     */
    public Polyline3D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {

        double sa = parameterDomain().wrap(pint.start());
        double inc = pint.increase();

        int no_intvls = Circle2D.toPolylineNDivision(radius(), inc, tol);
        double atheta = inc / no_intvls;

        Point3D[] pnts = new Point3D[no_intvls + 1];

        for (int i = 0; i < no_intvls + 1; i++)
            pnts[i] = new PointOnCurve3D(this, sa + (atheta * i), doCheckDebug);

        if (no_intvls == 1 && pnts[0].identical(pnts[1]))
            throw new ZeroLengthException();

        return new Polyline3D(pnts);
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
    public PureBezierCurve3D[] toPolyBezierCurves(ParameterSection pint) {
        Circle2D this2D =
                (Circle2D) this.toLocal2D(Axis2Placement2D.origin);
        PureBezierCurve2D[] bzcs2D = this2D.toPolyBezierCurves(pint);
        return this.transformPolyBezierCurvesInLocal2DToGrobal3D(bzcs2D);
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
    PureBezierCurve3D[] toPolyBezierCurvesOfN(int nCurves,
                                              ParameterSection pint) {
        Circle2D this2D =
                (Circle2D) this.toLocal2D(Axis2Placement2D.origin);
        PureBezierCurve2D[] bzcs2D = this2D.toPolyBezierCurvesOfN(nCurves, pint);
        return this.transformPolyBezierCurvesInLocal2DToGrobal3D(bzcs2D);
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
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
        PureBezierCurve3D[] bzcs = this.toPolyBezierCurves(pint);
        boolean closed =
                (Math.abs(pint.increase()) >= GeometryUtils.PI2) ? true : false;

        return Conic3D.convertPolyBezierCurvesToOneBsplineCurve(bzcs, closed);
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
     * @see Conic3D#convertPolyBezierCurvesToOneBsplineCurve(PureBezierCurve3D[],boolean)
     */
    BsplineCurve3D toBsplineCurveOfNSegments(int nSegments,
                                             ParameterSection pint) {
        PureBezierCurve3D[] bzcs = this.toPolyBezierCurvesOfN(nSegments, pint);
        boolean closed =
                (Math.abs(pint.increase()) >= GeometryUtils.PI2) ? true : false;

        return Conic3D.convertPolyBezierCurvesToOneBsplineCurve(bzcs, closed);
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����~�̂Ƃ���?A
     * ��~�����ꕽ��?��?�BĂ���?A
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
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
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
        boolean isPoly = poly.length < 4;
        int degree = xPoly.degree();
        double[] coef = new double[degree + 1];

        if (isPoly) {
            for (int j = 0; j <= degree; j++)
                coef[j] = xPoly.getCoefficientAsDouble(j) + yPoly.getCoefficientAsDouble(j);
            coef[0] -= rad2;
        } else {
            DoublePolynomial wPoly = (DoublePolynomial) poly[3].multiply(poly[3]);
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
    boolean checkSolution(Point3D point) {
        double dTol = getToleranceForDistance();
        return (Math.abs(point.toVector3D().length() - radius()) < dTol)
                && (Math.abs(point.z()) < dTol);
    }

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ����̂Ƃ���?A
     * ���̓_�̋�?�?�ł̃p���??[�^�l��?�߂�?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �p���??[�^�l
     */
    double getParameter(Point3D point) {
        double cos = point.x() / radius();
        if (cos > 1.0) cos = 1.0;
        if (cos < -1.0) cos = -1.0;
        double acos = Math.acos(cos);
        if (point.y() < 0.0) acos = Math.PI * 2 - acos;

        return acos;
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��~�����ꕽ��?��?�BĂ���?A
     * ��~�̒�?S�Ԃ̋����Ɠ�~�̔��a��?���?A
     * �Ƃ�Ɍ�?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�?A
     * ��~�̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link Conic3D#intersectCnc(Conic3D,boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException ��~�̓I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Circle3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return intersectCnc(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link Conic3D#intersectCnc(Conic3D,boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();    // Never be occured
        }
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link Conic3D#intersectCnc(Conic3D,boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();    // Never be occured
        }
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link Conic3D#intersectCnc(Conic3D,boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Hyperbola3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();    // Never be occured
        }
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �|�����C���̃N���X��?u�|�����C�� vs. �~?v�̌�_���Z�?�\�b�h
     * {@link Polyline3D#intersect(Circle3D,boolean)
     * Polyline3D.intersect(Circle3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Polyline3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �g������?�̃N���X��?u�g������?� vs. �~?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve3D#intersect(Circle3D,boolean)
     * TrimmedCurve3D.intersect(Circle3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(TrimmedCurve3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �~?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment3D#intersect(Circle3D,boolean)
     * CompositeCurveSegment3D.intersect(Circle3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(CompositeCurveSegment3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�N���X��?u��?���?� vs. �~?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve3D#intersect(Circle3D,boolean)
     * CompositeCurve3D.intersect(Circle3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(CompositeCurve3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ�?B
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     */
    public ParametricCurve3D parallelTranslate(Vector3D moveVec) {
        return new Circle3D(position().parallelTranslate(moveVec), radius);
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
     * @return {@link ParametricCurve3D#CIRCLE_3D ParametricCurve3D.CIRCLE_3D}
     */
    int type() {
        return CIRCLE_3D;
    }

    /**
     * ���̋�?��?A�^����ꂽ��?�?W�n�� Z ���̎���?A
     * �^����ꂽ�p�x������]��������?��Ԃ�?B
     *
     * @param trns ��?�?W�n���瓾��ꂽ?W�ϊ����Z�q
     * @param rCos cos(��]�p�x)
     * @param rSin sin(��]�p�x)
     * @return ��]��̋�?�
     */
    ParametricCurve3D rotateZ(CartesianTransformationOperator3D trns,
                              double rCos, double rSin) {
        Axis2Placement3D rpos = position().rotateZ(trns, rCos, rSin);
        return new Circle3D(rpos, radius());
    }

    /**
     * ���̋�?�?�̓_��?A�^����ꂽ��?�?�ɂȂ��_���Ԃ�?B
     *
     * @param line ��?�
     * @return �^����ꂽ��?�?�ɂȂ��_
     */
    Point3D getPointNotOnLine(Line3D line) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol2 = condition.getToleranceForDistance2();

        double start = 0.0, increase = Math.PI / 2.0;
        int itry = 0, limit = 3;
        Point3D point;
        Vector3D vector;

        /*
        * Get a point which is not on the line and verify that
        * the distance between a point and the line is greater
        * than the tolelance.
        */
        do {
            if (itry > limit) {
                throw new FatalException();    // should never be occurred
            }
            point = this.coordinates(start + (increase * itry));
            vector = point.subtract(line.project1From(point));
            itry++;
        } while (point.isOn(line) || vector.norm() < dTol2);

        return point;
    }

    /**
     * ���̋�?��?A�������ꂽ�x�W�G�ȖʂƂ̊�?𒲂ׂ�?B
     * <p/>
     * ���̃?�\�b�h�� {@link IntsCncBzs3D IntsCncBzs3D} �̓Ք�Ŏg����?B
     * </p>
     *
     * @param bi �������ꂽ�x�W�G�Ȗʂ�?��
     * @return ��?��Ă���� true?A�����łȂ���� false
     */
    boolean checkInterfere(IntsCncBzs3D.BezierSurfaceInfo bi) {
        double dTol = getToleranceForDistance();
        if (!((bi.box.min().z() < -dTol) && (bi.box.max().z() > dTol)))
            return false;
        boolean all_in = true;
        boolean all_out = true;

        Point2D[] point = new Point2D[4];
        point[0] = new CartesianPoint2D(bi.box.min().x(), bi.box.min().y());
        point[1] = new CartesianPoint2D(bi.box.max().x(), bi.box.min().y());
        point[2] = new CartesianPoint2D(bi.box.max().x(), bi.box.max().y());
        point[3] = new CartesianPoint2D(bi.box.min().x(), bi.box.max().y());

        for (int i = 0; i < 4; i++) {
            double dist = point[i].toVector2D().length();
            if (dist < (this.radius() - dTol))
                all_out = false;
            else if (dist > (this.radius() + dTol))
                all_in = false;
            else {
                all_out = false;
                all_in = false;
            }
        }

        if (all_in == true)
            return false; /* no interfere */

        else if (all_out == true) {
            if ((bi.box.min().x() > (this.radius() + dTol))
                    || (bi.box.min().y() > (this.radius() + dTol))
                    || (bi.box.max().x() < (-(this.radius() + dTol)))
                    || (bi.box.max().y() < (-(this.radius() + dTol))))
                return false;  /* no interfere */
            if (((point[0].x() > 0.0) && (point[0].y() > 0.0))
                    || ((point[1].x() < 0.0) && (point[1].y() > 0.0))
                    || ((point[2].x() < 0.0) && (point[2].y() < 0.0))
                    || ((point[3].x() > 0.0) && (point[3].y() < 0.0)))
                return false;  /* no interfere */

            return true; /* interfere */
        } else
            return true; /* interfere */
    }

    /**
     * ���̉~??��?�Ɨ^����ꂽ���ʂ̌�_��?�߂�?B
     * <p/>
     * ���̃?�\�b�h�� {@link IntsCncBzs3D IntsCncBzs3D} �̓Ք�Ŏg����?B
     * </p>
     *
     * @param plane ����
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersectConicPlane(Plane3D plane) {
        Axis2Placement3D position =
                new Axis2Placement3D(Point3D.origin, null, null);
        Circle3D circle = new Circle3D(position, this.radius());

        try {
            return circle.intersect(plane);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł� (���̉~??��?�̋�?�?W�n��ł�) ?W�l
     * ��Ԃ�?B
     *
     * @param parameter �p���??[�^�l
     * @return ���̉~??��?�̋�?�?W�n��ł�?W�l
     */
    Point3D nlFunc(double parameter) {
        double x = this.radius() * Math.cos(parameter);
        double y = this.radius() * Math.sin(parameter);
        double z = 0.0;

        return new CartesianPoint3D(x, y, z);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł� (���̉~??��?�̋�?�?W�n��ł�) ?ڃx�N�g��
     * ��Ԃ�?B
     *
     * @param parameter �p���??[�^�l
     * @return ���̉~??��?�̋�?�?W�n��ł�?ڃx�N�g��
     */
    Vector3D dnlFunc(double parameter) {
        double x = -this.radius() * Math.sin(parameter);
        double y = this.radius() * Math.cos(parameter);
        double z = 0.0;

        return new LiteralVector3D(x, y, z);
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
    protected synchronized ParametricCurve3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Axis2Placement3D tPosition =
                this.position().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        double tRadius;
        if (reverseTransform != true)
            tRadius = transformationOperator.transform(this.radius());
        else
            tRadius = transformationOperator.reverseTransform(this.radius());
        return new Circle3D(tPosition, tRadius);
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
}
