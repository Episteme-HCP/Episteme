/*
 * �R���� : ���ʂ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SphericalSurface3D.java,v 1.5 2006/05/20 23:25:55 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.analysis.polynomials.DoublePolynomial;
import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : ���ʂ�\���N���X?B
 * <p/>
 * ���ʂ�?A���̒�?S�̈ʒu�Ƌ�?� X/Y/Z ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement3D Axis2Placement3D}) position ��
 * ���a radius �Œ�`�����?B
 * </p>
 * <p/>
 * ���ʂ�
 * U ���̃p���??[�^��`��͗L�Ŏ��I�ł���?A
 * ���̃v���C�}���ȗL���Ԃ� [0, (2 * ��)] �ł���?B
 * V ���̃p���??[�^��`��͗L�Ŕ���I�ł���?A
 * ���̗L���Ԃ� [(- �� / 2), (�� / 2)] �ł���?B
 * </p>
 * <p/>
 * (u, v) ��p���??[�^�Ƃ��鋅�� P(u, v) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(u, v) = c + radius * (cos(v) * (cos(u) * x + sin(u) * y) + sin(v) * z)
 * </pre>
 * ������?Ac, x, y, z �͂��ꂼ��
 * <pre>
 * 	c : position.location()
 * 	x : position.x()
 * 	y : position.y()
 * 	z : position.z()
 * </pre>
 * ��\��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.5 $, $Date: 2006/05/20 23:25:55 $
 */

public class SphericalSurface3D extends ElementarySurface3D {
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
    public SphericalSurface3D(Axis2Placement3D position,
                              double radius) {
        super(position);
        setRadius(radius);
    }

    /**
     * ��?S�Ɣ��a��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?\�z�����~�̋�?� X/Y/Z ���̕���?A���?W�n�� X/Y/Z ���̕��Ƃ���?B
     * </p>
     * <p/>
     * cntr �� null ��?�?��ɂ�?A
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
    public SphericalSurface3D(Point3D cntr, double radius) {
        this(new Axis2Placement3D(cntr,
                Vector3D.zUnitVector,
                Vector3D.xUnitVector),
                radius);
    }

    /**
     * ���̋��ʂ̔��a��Ԃ�?B
     *
     * @return ���a
     */
    public double radius() {
        return this.radius;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���p���??[�^�l
     * @param vParam V ���p���??[�^�l
     * @return ?W�l
     * @see ParameterOutOfRange
     */
    public Point3D coordinates(double uParam, double vParam) {
        checkVValidity(vParam);
        CartesianTransformationOperator3D gtrans = toGlobal();
        double x = radius * Math.cos(uParam) * Math.cos(vParam);
        double y = radius * Math.sin(uParam) * Math.cos(vParam);
        double z = radius * Math.sin(vParam);
        Point3D pnt = new CartesianPoint3D(x, y, z);

        return gtrans.transform(pnt);
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * �����ł�?ڃx�N�g���Ƃ�?A�p���??[�^ U/V �̊e?X�ɂ��Ă̈ꎟ�Γ���?��ł���?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ�z��̗v�f?��� 2 �ł���?B
     * �z���?�?��̗v�f�ɂ� U �p���??[�^�ɂ��Ă�?ڃx�N�g��?A
     * ��Ԗڂ̗v�f�ɂ� V �p���??[�^�ɂ��Ă�?ڃx�N�g����܂�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���p���??[�^�l
     * @param vParam V ���p���??[�^�l
     * @return ?ڃx�N�g���̔z��
     * @see ParameterOutOfRange
     */
    public Vector3D[] tangentVector(double uParam, double vParam) {
        checkVValidity(vParam);
        CartesianTransformationOperator3D gtrans = toGlobal();
        double dux = -radius * Math.sin(uParam) * Math.cos(vParam);
        double duy = radius * Math.cos(uParam) * Math.cos(vParam);
        double dvx = -radius * Math.cos(uParam) * Math.sin(vParam);
        double dvy = -radius * Math.sin(uParam) * Math.sin(vParam);
        double dvz = radius * Math.cos(vParam);

        Vector3D dup = new LiteralVector3D(dux, duy, 0.0);
        Vector3D dvp = new LiteralVector3D(dvx, dvy, dvz);

        Vector3D[] tang = {gtrans.transform(dup), gtrans.transform(dvp)};
        return tang;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̖@?�x�N�g����Ԃ�?B
     * <p/>
     * ���̃?�\�b�h���Ԃ��@?�x�N�g����?A?��K�����ꂽ�P�ʃx�N�g���ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?��K�����ꂽ�@?�x�N�g��
     * @see ParameterOutOfRange
     */
    public Vector3D normalVector(double uParam, double vParam) {
        checkVValidity(vParam);
        Point3D pnt = coordinates(uParam, vParam);

        return pnt.subtract(position().location()).unitized();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̎�ȗ�?���Ԃ�?B
     * <p/>
     * ��̎�ȗ� (principalCurvature1, principalCurvature2) �̒l�͂ǂ���� (- 1 / radius) �ł���?B
     * ����x�N�g��1 (principalDirection1) �ɂ� U �p���??[�^�ɂ��Ă�?ڃx�N�g���̒P�ʃx�N�g��?A
     * ����x�N�g��2 (principalDirection2) �ɂ� V �p���??[�^�ɂ��Ă�?ڃx�N�g���̒P�ʃx�N�g��
     * ��Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ��ȗ�?��
     * @see ParameterOutOfRange
     */
    public SurfaceCurvature3D curvature(double uParam, double vParam) {
        checkVValidity(vParam);
        Vector3D[] tangent = tangentVector(uParam, vParam);

        return new SurfaceCurvature3D(-1.0 / radius, tangent[0].unitized(),
                -1.0 / radius, tangent[1].unitized());
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     * @see ParameterOutOfRange
     */
    public SurfaceDerivative3D evaluation(double uParam, double vParam) {
        checkVValidity(vParam);
        CartesianTransformationOperator3D gtrans = toGlobal();
        double x = radius * Math.cos(uParam) * Math.cos(vParam);
        double y = radius * Math.sin(uParam) * Math.cos(vParam);
        double z = radius * Math.sin(vParam);
        // dux =  -radius * sin(u) * cos(v) = -y
        // duy =   radius * cos(u) * cos(v) = x
        // duux = -radius * cos(u) * cos(v) = -x
        // duuy = -radius * sin(u) * cos(v) = -y
        double dvx = -radius * Math.cos(uParam) * Math.sin(vParam);
        double dvy = -radius * Math.sin(uParam) * Math.sin(vParam);
        double dvz = radius * Math.cos(vParam);
        // dvvx = -radius * cos(u) * cos(v) = -x
        // dvvy = -radius * sin(u) * cos(v) = -y
        // dvvz = -radius * sin(v) = -z
        // duvx =  radius * sin(u) * sin(v) = -dvy
        // duvy = -radius * cos(u) * sin(v) = dvx

        Point3D pnt = new CartesianPoint3D(x, y, z);
        Vector3D dup = new LiteralVector3D(-y, x, 0.0);
        Vector3D dvp = new LiteralVector3D(dvx, dvy, dvz);
        Vector3D duup = new LiteralVector3D(-x, -y, 0.0);
        Vector3D duvp = new LiteralVector3D(-dvy, dvx, 0.0);
        Vector3D dvvp = new LiteralVector3D(-x, -y, -z);

        return new SurfaceDerivative3D(gtrans.transform(pnt),
                gtrans.transform(dup),
                gtrans.transform(dvp),
                gtrans.transform(duup),
                gtrans.transform(duvp),
                gtrans.transform(dvvp));
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * �^����ꂽ�_�����̋��ʂ̒�?S�Ɉ�v���Ȃ���?A
     * ?�ɓ�̓��e�_��Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�_�Ƃ��̋��ʂ̒�?S�Ƃ̋�����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����
     * ?�����?�?��ɂ�?A
     * �p���??[�^�l (0, 0) �̓_�� suitable �Ƃ���
     * IndefiniteSolutionException �̗�O�𓊂���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s�� (���e���̓_�����ʂ̒�?S�Ɉ�v����)
     */
    public PointOnSurface3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        CartesianTransformationOperator3D gtrans = toGlobal();
        Point3D lpoint = gtrans.reverseTransform(point);

        // direction vector from center to point
        Vector3D eduvec = lpoint.toVector3D();

        // check length & unitize
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = Math.abs(condition.getToleranceForDistance());
        double aTol = condition.getToleranceForAngle();

        if (eduvec.length() < dTol) {
            // any point
            PointOnSurface3D p;

            try {
                p = new PointOnSurface3D(this, 0, 0, doCheckDebug);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

            throw new IndefiniteSolutionException(p);
        }

        eduvec = eduvec.unitized();
        // eduvec = (cos(u)*cos(v), sin(u)*cos(v), sin(v))

        double sin_v = eduvec.z();
        double v = Math.asin(sin_v);
        PointOnSurface3D foot1, foot2;

        if (v < -Math.PI + aTol || Math.PI - aTol < v) {
            // north pole or south pole
            foot1 = new PointOnSurface3D(this, 0, v, doCheckDebug);
            foot2 = new PointOnSurface3D(this, 0, -v, doCheckDebug);
        } else {
            double u = Math.atan2(eduvec.y(), eduvec.x());
            if (u < 0)
                u += 2 * Math.PI;

            foot1 = new PointOnSurface3D(this, u, v, doCheckDebug);
            u += Math.PI;
            if (u >= 2 * Math.PI)
                u -= 2 * Math.PI;
            foot2 = new PointOnSurface3D(this, u, -v, doCheckDebug);
        }

        PointOnSurface3D[] proj = {foot1, foot2};

        return proj;
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * �^����ꂽ��?��ŕ��ʋߎ�����i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ��Ȗʂ̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see ParameterOutOfRange
     */
    public Mesh3D
    toMesh(ParameterSection uPint, ParameterSection vPint,
           ToleranceForDistance tol) {
        PointOnSurface3D[][] mesh;
        double vStart = vPint.start();
        double vEnd = vPint.end();
        double uParam, uDelta;
        double vParam, vDelta;
        double rad;
        int u_npnts, v_npnts;
        int i, j;

        rad = maxRadius(vPint);    // V���̃p���??[�^��Ԃ̒���?ł�傫�Ȕ��a

        /*
        * ���ꂼ��̕���?A�K�v�ȕ����_?��𓾂�?B
        */
        u_npnts = Circle2D.toPolylineNDivision(rad, uPint.increase(), tol) + 1;
        v_npnts = Circle2D.toPolylineNDivision(radius(), vPint.increase(), tol) + 1;

        /*
        * ���ꂼ��̕��� u_npnts, v_npnts �p���??[�^���������Ċi�q�_��?\?�����?B
        * (�~�ʂ̃|�����C���ߎ��̓p���??[�^�����ƂȂ�͂��ł���)
        */
        mesh = new PointOnSurface3D[u_npnts][v_npnts];

        uDelta = uPint.increase() / (u_npnts - 1);
        vDelta = vPint.increase() / (v_npnts - 1);

        uParam = uPint.start();
        for (i = 0; i < u_npnts; i++) {
            vParam = vStart;
            for (j = 0; j < v_npnts; j++) {
                try {
                    mesh[i][j] = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
                } catch (InvalidArgumentValueException e) {
                    throw new FatalException();
                }
                if (j == (v_npnts - 2))
                    vParam = vEnd;
                else
                    vParam += vDelta;
            }
            if (i == (u_npnts - 2))
                uParam = uPint.end();
            else
                uParam += uDelta;
        }

        return new Mesh3D(mesh, false);
    }

    /**
     * ���̋��ʂ�?A�^����ꂽ V ���̃p���??[�^��Ԃ̒��ł�?ł�傫�Ȕ��a��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ��Ȗʂ̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param vPint V ���̃p���??[�^���
     * @return ?ő唼�a
     * @see ParameterOutOfRange
     */
    private double maxRadius(ParameterSection vPint) {
        double vLow, vUpp, rad;

        checkVValidity(vLow = vPint.lower());
        checkVValidity(vUpp = vPint.upper());
        if (vLow <= 0.0 && vUpp >= 0.0)
            rad = radius();
        else if (Math.abs(vLow) < Math.abs(vUpp))
            rad = radius() * Math.cos(vLow);
        else
            rad = radius() * Math.cos(vUpp);

        return rad;
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ쵖���?Č�����L�? Bspline �Ȗʂ�Ԃ�?B
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @return ���̋Ȗʂ̎w��̋�Ԃ�?Č�����L�? Bspline �Ȗ�
     */
    public BsplineSurface3D
    toBsplineSurface(ParameterSection uPint,
                     ParameterSection vPint) {
        Circle2D circle2D =
                new Circle2D(Axis2Placement2D.origin, this.radius());

        BsplineCurve2D uBsplineCurve2D = circle2D.toBsplineCurve(uPint);
        int uDegree = uBsplineCurve2D.degree();
        boolean uPeriodic = uBsplineCurve2D.isPeriodic();
        int uNPoints = uBsplineCurve2D.nControlPoints();

        // vBsplineCurve2D : x -> x, y -> z
        BsplineCurve2D vBsplineCurve2D = circle2D.toBsplineCurve(vPint);
        int vDegree = vBsplineCurve2D.degree();
        boolean vPeriodic = vBsplineCurve2D.isPeriodic();
        int vNPoints = vBsplineCurve2D.nControlPoints();

        Point3D[][] controlPoints = new Point3D[uNPoints][vNPoints];
        double[][] weights = new double[uNPoints][vNPoints];

        CartesianTransformationOperator3D localTransformationOperator =
                this.position().toCartesianTransformationOperator(1.0);

        for (int vi = 0; vi < vNPoints; vi++) {
            Point2D vPoint = vBsplineCurve2D.controlPointAt(vi);
            double scale = vPoint.x() / this.radius();
            for (int ui = 0; ui < uNPoints; ui++) {
                Point2D uPoint = uBsplineCurve2D.controlPointAt(ui);
                Point3D localPoint =
                        Point3D.of(scale * uPoint.x(),
                                scale * uPoint.y(),
                                vPoint.y());
                controlPoints[ui][vi] = localTransformationOperator.toEnclosed(localPoint);
                weights[ui][vi] = vBsplineCurve2D.weightAt(vi) * uBsplineCurve2D.weightAt(ui);
            }
        }

        return new BsplineSurface3D(uBsplineCurve2D.knotData(),
                vBsplineCurve2D.knotData(),
                controlPoints, weights);
    }

    /**
     * �^����ꂽ�_�����̋Ȗ�?�ɂ��邩�ۂ���`�F�b�N����?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �^����ꂽ�_�����̋Ȗ�?�ɂ���� true?A�����łȂ���� false
     */
    boolean checkSolution(Point3D point) {
        double dTol = getToleranceForDistance();
        return Math.abs(point.distance(point.origin) - this.radius()) < dTol;
    }

    /**
     * ����?����Ȗʂ� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?�����?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ����?����Ȗʂ� poly �̌�_��\����?�����?���
     */
    DoublePolynomial makePoly(DoublePolynomial[] poly) {
        DoublePolynomial xPoly = (DoublePolynomial) poly[0].multiply(poly[0]);
        DoublePolynomial yPoly = (DoublePolynomial) poly[1].multiply(poly[1]);
        DoublePolynomial zPoly = (DoublePolynomial) poly[2].multiply(poly[2]);
        double radius2 = this.radius() * this.radius();
        boolean isPoly = poly.length < 4;
        int degree = xPoly.degree();
        double[] coef = new double[degree + 1];
        if (isPoly) {
            for (int j = 0; j <= degree; j++) {
                coef[j] = xPoly.getCoefficientAsDouble(j) +
                        yPoly.getCoefficientAsDouble(j) +
                        zPoly.getCoefficientAsDouble(j);
            }
            coef[0] -= radius2;
        } else {
            DoublePolynomial wPoly = (DoublePolynomial) poly[3].multiply(poly[3]);
            for (int j = 0; j <= degree; j++) {
                coef[j] = xPoly.getCoefficientAsDouble(j) +
                        yPoly.getCoefficientAsDouble(j) +
                        zPoly.getCoefficientAsDouble(j) -
                        (radius2 * wPoly.getCoefficientAsDouble(j));
            }
        }
        return new DoublePolynomial(coef);
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋��ʂ̒�?S����^����ꂽ��?�ւ̋����� D?A���ʂ̔��a�� R �Ƃ���
     * <ul>
     * <li> |D - R| �������̋��e��?�����?��������?A��_���Ԃ�?B
     * <li> D �� R ����?��������?A��_���Ԃ�?B
     * <li> D �� R ����傫�����?A��_�͂Ȃ�?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
        Point3D[] int_pnt;         // intersection point
        double dTol = getToleranceForDistance();

        // project sphere's center to line
        Point3D center = position().location();
        PointOnCurve3D pA = mate.project1From(center);

        // distance between sphere'center & projected point
        double distance = pA.distance(center);

        // intersect line & sphere ?
        if (Math.abs(radius() - distance) < dTol) {
            int_pnt = new Point3D[1];
            int_pnt[0] = pA;
        } else if (radius() > distance) {
            int_pnt = new Point3D[2];
            Vector3D unitVector =
                    mate.dir().unitized();       // line's unit vector
            Point3D line_pnt = mate.pnt();

            double offset =
                    Math.sqrt(radius() * radius() - distance * distance);

            Vector3D dir_vec = unitVector.multiply(offset);

            int_pnt[0] = pA.add(dir_vec);
            int_pnt[1] = pA.subtract(dir_vec);
        } else {
            int_pnt = new Point3D[0];
        }

        IntersectionPoint3D[] return_pnt = new IntersectionPoint3D[int_pnt.length];

        for (int i = 0; i < int_pnt.length; i++) {

            double dU, dV, dT;

            // calculate parameter dT
            Vector3D pnt_to_int = int_pnt[i].subtract(mate.pnt());
            dT = pnt_to_int.length() / mate.dir().length();
            if (pnt_to_int.dotProduct(mate.dir()) < 0.0)
                dT *= -1;

            // transform intersection point to sphere's local coordinates
            CartesianTransformationOperator3D trans =
                    new CartesianTransformationOperator3D(position(), 1.0);
            Point3D transformed_int_pnt = trans.reverseTransform(int_pnt[i]);

            // calculate parameter dV
            Vector3D center_to_int = transformed_int_pnt.toVector3D();

            Vector3D unit_center_to_int = center_to_int.unitized();

            dV = Math.asin(unit_center_to_int.z());

            // calculate parameter dU
            Vector2D dir2D = center_to_int.to2D();
            Vector2D center_to_int_2D = dir2D.unitized();
            dU = Math.acos(center_to_int_2D.x());
            if (center_to_int_2D.y() < 0.0)
                dU = 2 * Math.PI - dU;

            // point on line
            PointOnCurve3D PonC = new PointOnCurve3D(int_pnt[i], mate, dT, doCheckDebug);

            // point on surface
            PointOnSurface3D PonS =
                    new PointOnSurface3D(int_pnt[i], this, dU, dV, doCheckDebug);

            // intersection point
            if (doExchange)
                return_pnt[i] = new IntersectionPoint3D(int_pnt[i], PonC, PonS, doCheckDebug);
            else
                return_pnt[i] = new IntersectionPoint3D(int_pnt[i], PonS, PonC, doCheckDebug);
        }

        return return_pnt;
    }

    /**
     * ���̋ȖʂƑ��̋Ȗʂ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ���?������?��ɂ��Ă�?A��?� (IntersectionCurve3D) ���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ�?ڂ����?��ɂ��Ă�?A��_ (IntersectionPoint3D) ���Ԃ邱�Ƃ�����?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��?� (�܂��͌�_) �̔z��
     * @throws IndefiniteSolutionException mate �˅�ʂ�?A���҂��I?[�o?[���b�v���Ă���?A�⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    public SurfaceSurfaceInterference3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���ʂ̃N���X��?u���� vs. ����?v�̌�?�?�\�b�h
     * {@link Plane3D#intersect(SphericalSurface3D,boolean)
     * Plane3D.intersect(SphericalSurface3D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �񋅖ʂ̒�?S�Ԃ̋����Ɠ񋅖ʂ̔��a��?���?A
     * �Ƃ�Ɍ�?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?����?�����?�?��ɂ�?A
     * �񋅖ʂ̓I?[�o?[���b�v���Ă����̂Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�������?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * �񋅖ʂ̒�?S�Ԃ̋����Ɠ񋅖ʂ̔��a�̘a���邢��?��ɂ�B�?A
     * ��?��?���?�?���������?��?A�􉽓I�ɉ⢂Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException ���҂��I?[�o?[���b�v���Ă���?A�⪕s��ł���
     * @see ElementarySurface3D#curveToIntersectionCurve(ParametricCurve3D,ElementarySurface3D,boolean)
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        double d_tol = getToleranceForDistance();
        Vector3D vector2 = position().location().subtract(mate.position().location());
        double dist = vector2.length();
        if (dist < d_tol) {
            if (Math.abs(radius() - mate.radius()) < d_tol) {
                /*
                * 2 spheres are coincident
                */
                Circle3D res = new Circle3D(position(), (radius() + mate.radius()) / 2.0);
                IntersectionCurve3D ints = curveToIntersectionCurve(res, mate, doExchange);
                throw new IndefiniteSolutionException(ints);
            }
            /*
            * no intersection
            */
            return new SurfaceSurfaceInterference3D[0];
        }

        double Ar = radius();
        double Br = mate.radius();
        Vector3D vector = vector2.divide(dist);
        if ((Math.abs(dist - (Ar + Br)) < d_tol) ||
                (Math.abs(dist - (Ar - Br)) < d_tol) ||
                (Math.abs(dist - (Br - Ar)) < d_tol)) {
            /*
            * intersection is a point
            */
            Point3D Aint, Bint;
            if (Math.abs(dist - (Ar + Br)) < d_tol) {
                Aint = position().location().subtract(vector.multiply(Ar));
                Bint = mate.position().location().add(vector.multiply(Br));
            } else if (Math.abs(dist - (Ar - Br)) < d_tol) {
                Aint = position().location().subtract(vector.multiply(Ar));
                Bint = mate.position().location().subtract(vector.multiply(Br));
            } else {    // Math.abs(dist - (Br - Ar)) < d_tol
                Aint = position().location().add(vector.multiply(Ar));
                Bint = mate.position().location().add(vector.multiply(Br));
            }
            Point3D res = Aint.linearInterpolate(Bint, 0.5);
            double[] params = pointToParameter(Aint);
            PointOnSurface3D ponA
                    = new PointOnSurface3D(Aint, this, params[0], params[1], doCheckDebug);
            params = mate.pointToParameter(Bint);
            PointOnSurface3D ponB
                    = new PointOnSurface3D(Bint, mate, params[0], params[1], doCheckDebug);
            IntersectionPoint3D intp;
            if (!doExchange)
                intp = new IntersectionPoint3D(res, ponA, ponB, doCheckDebug);
            else
                intp = new IntersectionPoint3D(res, ponB, ponA, doCheckDebug);
            SurfaceSurfaceInterference3D[] sol = {intp};
            return sol;
        }

        if ((dist < (Ar + Br)) && (dist > Math.abs(Ar - Br))) {
            /*
            * intersection is a circle
            */
            double Adist = Math.abs((Ar * Ar - Br * Br + dist * dist) / (2.0 * dist));
            Point3D Apnt = position().location().subtract(vector.multiply(Adist));
            double Bdist = Math.abs((Br * Br - Ar * Ar + dist * dist) / (2.0 * dist));
            Point3D Bpnt = mate.position().location().add(vector.multiply(Bdist));
            Point3D loc = Apnt.linearInterpolate(Bpnt, 0.5);
            Vector3D axis = vector2;
            Vector3D refDir;
            if ((Math.abs(axis.y()) < d_tol) && (Math.abs(axis.z()) < d_tol))
                refDir = Vector3D.zUnitVector;
            else
                refDir = Vector3D.xUnitVector;
            Axis2Placement3D a2p = new Axis2Placement3D(loc, axis, refDir);
            double radius
                    = Math.sqrt(((Ar * Ar - Adist * Adist) + (Br * Br - Bdist * Bdist)) / 2.0);
            Circle3D res = new Circle3D(a2p, radius);
            IntersectionCurve3D ints = curveToIntersectionCurve(res, mate, doExchange);
            SurfaceSurfaceInterference3D[] sol = {ints};
            return sol;
        }

        /*
        * no intersection
        */
        return new SurfaceSurfaceInterference3D[0];
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSphCyl3D#intersection(SphericalSurface3D,CylindricalSurface3D,boolean)
     * IntsSphCyl3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange) {
        return IntsSphCyl3D.intersection(this, mate, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~??��) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSphCon3D IntsSphCon3D}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate,
                                             boolean doExchange) {
        IntsSphCon3D doObj = new IntsSphCon3D(this, mate);
        return doObj.getInterference(doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�x�W�G�Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsQrdBzs3D#intersection(ElementarySurface3D,PureBezierSurface3D,boolean)
     * IntsQrdBzs3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(PureBezierSurface3D mate,
                                             boolean doExchange) {
        return IntsQrdBzs3D.intersection(this, mate, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSrfBss3D#intersection(ElementarySurface3D,BsplineSurface3D,boolean)
     * IntsSrfBss3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(BsplineSurface3D mate,
                                             boolean doExchange) {
        return IntsSrfBss3D.intersection(this, mate, doExchange);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�I�t�Z�b�g�����Ȗʂ�
     * �^����ꂽ��?��ŋߎ����� Bspline �Ȗʂ�?�߂�?B
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.FRONT/BACK)
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�`��Ԃ̃I�t�Z�b�g�Ȗʂ�ߎ����� Bspline �Ȗ�
     * @see WhichSide
     */
    public BsplineSurface3D
    offsetByBsplineSurface(ParameterSection uPint,
                           ParameterSection vPint,
                           double magni,
                           int side,
                           ToleranceForDistance tol) {
        Ofst3D doObj = new Ofst3D(this, uPint, vPint, magni, side, tol);
        return doObj.offset();
    }

    /**
     * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
     * <p/>
     * ���̓��p���??[�^��?�͉~�ʂƂȂ邽�߃g������?�Ƃ��ĕԂ�?B
     * ���̂��߂ɂ��̓��p���??[�^��?�̃p���??[�^��`�悪 [0, ��] �ƂȂBĂ��܂�?A
     * ���� V ���̃p���??[�^��`�� [(- �� / 2), (�� / 2)] ��?���Ȃ��ȂBĂ��܂��̂�?A
     * ��舵���ɂ͒?�ӂ��K�v�ł���?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @return �w��� U �p���??[�^�l�ł̓��p���??[�^��?�
     */
    public ParametricCurve3D uIsoParametricCurve(double uParam) {
        CartesianTransformationOperator3D trns = position().toCartesianTransformationOperator();

        Point3D cntr = position().location();    // center point of Circle
        Vector3D xVec
                = position().x().multiply(Math.cos(uParam)).add(position().y().multiply(Math.sin(uParam)));
        xVec = trns.toEnclosed(xVec);                // X axis of Circle
        Vector3D yVec = trns.toEnclosed(position().z());    // Y axis
        Vector3D zVec = xVec.crossProduct(yVec);        // Z axis
        Axis2Placement3D a2p = new Axis2Placement3D(cntr, zVec, xVec);
        Circle3D cir = new Circle3D(a2p, radius());
        ParameterSection sec = new ParameterSection(-Math.PI / 2.0, Math.PI);
        return new TrimmedCurve3D(cir, sec);
    }

    /**
     * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
     *
     * @param vParam V ���̃p���??[�^�l
     * @return �w��� V �p���??[�^�l�ł̓��p���??[�^��?�
     */
    public ParametricCurve3D vIsoParametricCurve(double vParam)
            throws ReducedToPointException {
        checkVValidity(vParam);

        CartesianTransformationOperator3D trns = position().toCartesianTransformationOperator();

        Point3D cntr = new CartesianPoint3D(0.0, 0.0, radius() * Math.sin(vParam));
        cntr = trns.toEnclosed(cntr);    // center of Circle
        double cRadius = radius() * Math.cos(vParam);
        if (cRadius <= getToleranceForDistance()) {
            throw new ReducedToPointException(cntr);
        }
        Axis2Placement3D a2p = new Axis2Placement3D(cntr, position().z(), position().x());
        return new Circle3D(a2p, cRadius);
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * �L�Ŏ��I�Ȓ�`���Ԃ�?B
     * </p>
     *
     * @return ���̋Ȗʂ� U ���̃p���??[�^��`��
     */
    ParameterDomain getUParameterDomain() {
        try {
            return new ParameterDomain(true, 0, 2 * Math.PI);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * �L�Ŕ���I�Ȓ�`���Ԃ�?B
     * </p>
     *
     * @return ���̋Ȗʂ� V ���̃p���??[�^��`��
     */
    ParameterDomain getVParameterDomain() {
        try {
            return new ParameterDomain(false, -Math.PI / 2, Math.PI);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#SPHERICAL_SURFACE_3D ParametricSurface3D.SPHERICAL_SURFACE_3D}
     */
    int type() {
        return SPHERICAL_SURFACE_3D;
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * �^����ꂽ��?��ŕ��ʋߎ�����_�Q��Ԃ�?B
     * <p/>
     * ?��?���ʂƂ��ē�����_�Q�͈�ʂ�?A�ʑ��I�ɂ�􉽓I�ɂ�?A�i�q?�ł͂Ȃ�?B
     * </p>
     * <p/>
     * scalingFactor ��?A��͗p�ł͂Ȃ�?A?o�͗p�̈�?��ł���?B
     * scalingFactor �ɂ�?A�v�f?� 2 �̔z���^����?B
     * scalingFactor[0] �ɂ� U ����?k�ڔ{��?A
     * scalingFactor[1] �ɂ� V ����?k�ڔ{�����Ԃ�?B
     * �����̒l�͉��炩��?�Βl�ł͂Ȃ�?A
     * �p���??[�^��?i�ޑ��x T �ɑ΂���?A
     * U/V �����ɂ��Ď��?�ŋȖ�?�̓_��?i�ޑ��x Pu/Pv ��\�����Βl�ł���?B
     * �܂�?A�p���??[�^�� T ����?i�ނ�?A
     * ���?�ł̋Ȗ�?�̓_�� U ���ł� Pu (scalingFactor[0])?A
     * V ���ł� Pv (scalingFactor[1]) ����?i�ނ��Ƃ�\���Ă���?B
     * T �̑傫���͖�������Ȃ��̂�?A���̒l��Q?Ƃ���?ۂɂ�?A
     * scalingFactor[0] �� scalingFactor[1] �̔䂾����p����ׂ��ł���?B
     * �Ȃ�?A�����̒l�͂����܂ł�ڈł���?A�����ȑ��x����̂ł͂Ȃ�?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ� Vector �Ɋ܂܂��e�v�f��
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D
     * �ł��邱�Ƃ���҂ł���?B
     * </p>
     *
     * @param uParameterSection U ���̃p���??[�^���
     * @param vParameterSection V ���̃p���??[�^���
     * @param tolerance         �����̋��e��?�
     * @param scalingFactor     �_�Q��O�p�`��������?ۂɗL�p�Ǝv���� U/V ��?k�ڔ{��
     * @return �_�Q��܂� Vector
     * @see PointOnSurface3D
     */
    public Vector toNonStructuredPoints(ParameterSection uParameterSection,
                                        ParameterSection vParameterSection,
                                        double tolerance,
                                        double[] scalingFactor) {
        Vector result = new Vector();

        Mesh3D mesh = this.toMesh(uParameterSection,
                vParameterSection,
                new ToleranceForDistance(tolerance));

        for (int u = 0; u < mesh.uNPoints(); u++)
            for (int v = 0; v < mesh.vNPoints(); v++)
                result.addElement(mesh.pointAt(u, v));

        /*
        * U ����?ő唼�a�𓾂�
        */
        double uCircleRadius;
        if (vParameterSection.start() > 0.0) {
            uCircleRadius = this.radius() * Math.cos(vParameterSection.start());
        } else if (vParameterSection.end() < 0.0) {
            uCircleRadius = this.radius() * Math.cos(vParameterSection.end());
        } else {
            uCircleRadius = this.radius();
        }

        scalingFactor[0] = uCircleRadius; // * GH__PINT_INCRS(pint) / GH__PINT_INCRS(pint)

        /*
        * V ����?ő唼�a�𓾂�
        */
        double vCircleRadius = this.radius();
        scalingFactor[1] = vCircleRadius; // * GH__PINT_INCRS(pint) / GH__PINT_INCRS(pint)

        return result;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    protected synchronized ParametricSurface3D
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
        return new SphericalSurface3D(tPosition, tRadius);
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
        writer.println(indent_tab + "\tradius\t" + radius);
        writer.println(indent_tab + "End");
    }
}
