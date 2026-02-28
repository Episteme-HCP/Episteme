/*
 * �R���� : �~??�ʂ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ConicalSurface3D.java,v 1.6 2006/04/22 22:41:45 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.MachineEpsilon;
import org.episteme.mathematics.analysis.polynomials.DoublePolynomial;
import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : �~??�ʂ�\���N���X?B
 * <p/>
 * �~??�ʂ�?A���̒�?S�̈ʒu�Ƌ�?� X/Y/Z ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement3D Axis2Placement3D}) position ��
 * position �ɂ����� XY ���ʂł̉~??�̔��a radius?A
 * ����� (���p / 2) ��\���p�x semiAngle �Œ�`�����?B
 * </p>
 * <p/>
 * �~??�ʂ�
 * U ���̃p���??[�^��`��͗L�Ŏ��I�ł���?A
 * ���̃v���C�}���ȗL���Ԃ� [0, (2 * ��)] �ł���?B
 * V ���̃p���??[�^��`��͖��Ŕ���I�ł���?B
 * </p>
 * <p/>
 * (u, v) ��p���??[�^�Ƃ���~??�� P(u, v) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(u, v) = c + (radius + v * tan(semiAngle)) * (cos(u) * x + sin(u) * y) + v * z
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
 * @version $Revision: 1.6 $, $Date: 2006/04/22 22:41:45 $
 */

public class ConicalSurface3D extends ElementarySurface3D {
    /**
     * ��?� XY ���ʂł̉~??�̔��a?B
     *
     * @serial
     */
    private double radius;

    /**
     * (���p / 2) ?B
     *
     * @serial
     */
    private double semiAngle;

    /**
     * ��?� XY ���ʂł̉~??�̔��a�� (���p / 2) ��?ݒ肷��?B
     * <p/>
     * radius �̒l�����ł���?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     * <p/>
     * semiAngle �̒l�Ƃ��p�x�̋��e��?����?�������
     * ���邢�� (�� / 2 - �p�x�̋��e��?�) ���傫��?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param radius    ��?� XY ���ʂł̉~??�̔��a
     * @param semiAngle (���p / 2)
     * @see InvalidArgumentValueException
     */
    private void setRadius(double radius, double semiAngle) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();
        double aTol = condition.getToleranceForAngle();

        if (radius < 0) {
            throw new InvalidArgumentValueException();
        }
        this.radius = radius;
        if (semiAngle < aTol || Math.PI / 2 - aTol < semiAngle) {
            throw new InvalidArgumentValueException();
        }
        this.semiAngle = semiAngle;
    }

    /**
     * ��?�?W�n?A��?� XY ���ʂł̉~??�̔��a����� (���p / 2) ��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * radius �̒l�����ł���?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     * <p/>
     * semiAngle �̒l�Ƃ��p�x�̋��e��?����?�������
     * ���邢�� (�� / 2 - �p�x�̋��e��?�) ���傫��?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param position  ��?����_�Ƌ�?� X/Y/Z ���̕�����?�?W�n
     * @param radius    ��?� XY ���ʂł̉~??�̔��a
     * @param semiAngle (���p / 2)
     * @see InvalidArgumentValueException
     */
    public ConicalSurface3D(Axis2Placement3D position,
                            double radius, double semiAngle) {
        super(position);
        setRadius(radius, semiAngle);
    }

    /**
     * ��?S��?A��?� XY ���ʂł̉~??�̔��a����� (���p / 2) ��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * pnt �µ���� axis �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * radius �̒l�����ł���?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     * <p/>
     * semiAngle �̒l�Ƃ��p�x�̋��e��?����?�������
     * ���邢�� (�� / 2 - �p�x�̋��e��?�) ���傫��?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param pnt       ��?����_
     * @param axis      ��?� Z �����
     * @param radius    ��?� XY ���ʂł̉~??�̔��a
     * @param semiAngle (���p / 2)
     */
    public ConicalSurface3D(Point3D pnt, Vector3D axis,
                            double radius, double semiAngle) {
        super(new Axis2Placement3D(pnt, axis,
                axis.verticalVector().unitized()));
        setRadius(radius, semiAngle);
    }

    /**
     * ���̉~??�ʂ̋�?� XY ���ʂɂ�����~??�̔��a��Ԃ�?B
     *
     * @return ��?� XY ���ʂɂ�����~??�̔��a
     */
    public double radius() {
        return this.radius;
    }

    /**
     * ���̉~??�ʂ� (���p / 2) ��Ԃ�?B
     *
     * @return (���p / 2)
     */
    public double semiAngle() {
        return this.semiAngle;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param uParam U ���p���??[�^�l
     * @param vParam V ���p���??[�^�l
     * @return ?W�l
     */
    public Point3D coordinates(double uParam, double vParam) {
        CartesianTransformationOperator3D gtrans = toGlobal();
        // c + (r + v*tan(semiAngle))*(cos(u)*x + sin(u)*y) + v*z
        double rad = radius + vParam * Math.tan(semiAngle);
        double x = rad * Math.cos(uParam);
        double y = rad * Math.sin(uParam);
        Point3D pnt = new CartesianPoint3D(x, y, vParam);

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
     *
     * @param uParam U ���p���??[�^�l
     * @param vParam V ���p���??[�^�l
     * @return ?ڃx�N�g���̔z��
     */
    public Vector3D[] tangentVector(double uParam, double vParam) {
        CartesianTransformationOperator3D gtrans = toGlobal();
        double tan_sa = Math.tan(semiAngle);
        double sin_u = Math.sin(uParam);
        double cos_u = Math.cos(uParam);
        double rad = radius + vParam * tan_sa;

        Vector3D dup =
                new LiteralVector3D(-rad * sin_u, rad * cos_u, 0.0);

        Vector3D dvp =
                new LiteralVector3D(tan_sa * cos_u, tan_sa * sin_u, 1.0);

        Vector3D[] tang = {gtrans.transform(dup), gtrans.transform(dvp)};
        return tang;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̖@?�x�N�g����Ԃ�?B
     * <p/>
     * ���̃?�\�b�h���Ԃ��@?�x�N�g����?A?��K�����ꂽ�P�ʃx�N�g���ł���?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?��K�����ꂽ�@?�x�N�g��
     */
    public Vector3D normalVector(double uParam, double vParam) {
        CartesianTransformationOperator3D gtrans = toGlobal();
        Vector3D nrm =
                new LiteralVector3D(Math.cos(uParam), Math.sin(uParam),
                        -Math.tan(semiAngle));
        return gtrans.transform(nrm).unitized();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̎�ȗ�?���Ԃ�?B
     * <p/>
     * ��ȗ�1 (principalCurvature1) �ɂ� (- 1 / (radius * (tan(semiAngle)^2 + 1)))?A
     * ��ȗ�2 (principalCurvature2) �ɂ� 0?A
     * ����x�N�g��1 (principalDirection1) �ɂ� U �p���??[�^�ɂ��Ă�?ڃx�N�g���̒P�ʃx�N�g��?A
     * ����x�N�g��2 (principalDirection2) �ɂ� V �p���??[�^�ɂ��Ă�?ڃx�N�g���̒P�ʃx�N�g��
     * ��Ԃ�?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ��ȗ�?��
     */
    public SurfaceCurvature3D curvature(double uParam, double vParam) {
        Vector3D[] tangent = tangentVector(uParam, vParam);
        double tan_sa = Math.tan(semiAngle);
        double rad = radius + vParam * tan_sa;

        return new SurfaceCurvature3D(-1.0 / (rad * (tan_sa * tan_sa + 1)),
                tangent[0].unitized(),
                0,
                tangent[1].unitized());
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ�?B
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     */
    public SurfaceDerivative3D evaluation(double uParam, double vParam) {
        CartesianTransformationOperator3D gtrans = toGlobal();
        double tan_sa = Math.tan(semiAngle);
        double sin_u = Math.sin(uParam);
        double cos_u = Math.cos(uParam);
        double rad = radius + vParam * tan_sa;

        Point3D pnt =
                new CartesianPoint3D(rad * cos_u, rad * sin_u, vParam);
        Vector3D dup =
                new LiteralVector3D(-rad * sin_u, rad * cos_u, 0.0);
        Vector3D dvp =
                new LiteralVector3D(tan_sa * cos_u, tan_sa * sin_u, 1.0);

        Vector3D duup =
                new LiteralVector3D(-rad * cos_u, -rad * sin_u, 0.0);

        Vector3D duvp =
                new LiteralVector3D(-tan_sa * sin_u, tan_sa * cos_u, 0.0);

        Vector3D zerov = Vector3D.zeroVector;

        return new SurfaceDerivative3D(gtrans.transform(pnt),
                gtrans.transform(dup),
                gtrans.transform(dvp),
                gtrans.transform(duup),
                gtrans.transform(duvp),
                gtrans.transform(zerov));
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * �^����ꂽ�_�����̉~??�ʂ̒�?S��?�ɂȂ���?A
     * ?�ɓ�̓��e�_��Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�_�Ƃ��̉~??�ʂ̒�?S���Ƃ̋�����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����
     * ?�����?�?��ɂ�?A
     * IndefiniteSolutionException �̗�O�𓊂���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s�� (���e���̓_���~??�ʂ̒�?S��?�ɂ���)
     */
    public PointOnSurface3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        CartesianTransformationOperator3D gtrans = toGlobal();
        Point3D lpoint = gtrans.reverseTransform(point);
        double z = lpoint.z();
        Point3D apoint = new CartesianPoint3D(0.0, 0.0, z);
        Vector3D eduvec = lpoint.subtract(apoint);

        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = Math.abs(condition.getToleranceForDistance());
        double lx = eduvec.length();

        if (lx < dTol) {
            // point on axis
            PointOnSurface3D p;

            try {
                p = new PointOnSurface3D(this, 0.0, z, doCheckDebug);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

            throw new IndefiniteSolutionException(p);
        }

        // get vector angle
        double u = Math.atan2(eduvec.y(), eduvec.x());

        double Btan = Math.tan(semiAngle);
        double Bcos = Math.cos(semiAngle);
        double Bsin = Math.sin(semiAngle);
        double zoff = radius / Btan;
        double ly = z + zoff;

        double edist = (ly * Bcos) + (lx * Bsin);

        if (u < 0)
            u += 2 * Math.PI;
        PointOnSurface3D foot1 =
                new PointOnSurface3D(this, u, Bcos * edist - zoff, doCheckDebug);

        edist = (ly * Bcos) - (lx * Bsin);
        u += Math.PI;
        if (u >= 2 * Math.PI)
            u -= 2 * Math.PI;
        PointOnSurface3D foot2 =
                new PointOnSurface3D(this, u, Bcos * edist - zoff, doCheckDebug);

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
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     */
    public Mesh3D
    toMesh(ParameterSection uPint,
           ParameterSection vPint,
           ToleranceForDistance tol) {
        return makeMesh(1, uPint, vPint, tol);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * �^����ꂽ��?��ŕ��ʋߎ�����i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param meshType toMesh ����Ȃ� 1 ?AtoNonStructuredPoints ����Ȃ� 2
     * @param uPint    U ���̃p���??[�^���
     * @param vPint    V ���̃p���??[�^���
     * @param tol      �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see #toMesh(ParameterSection,ParameterSection,ToleranceForDistance)
     * @see #toNonStructuredPoints(ParameterSection,ParameterSection,double,double[])
     */
    private Mesh3D
    makeMesh(int meshType,
             ParameterSection uPint,
             ParameterSection vPint,
             ToleranceForDistance tol) {
        PointOnSurface3D[][] mesh;
        double vStart = vPint.start();
        double vEnd = vPint.end();
        double vMiddle = 0.0;
        double rad = getMaxRadius(vPint);
        int u_npnts, v_npnts;
        double uParam, vParam, uDelta;
        int i, j;

        if (rad <= ConditionOfOperation.getCondition().getToleranceForDistance()) {
            u_npnts = 2;
        } else {
            /*
            * ���a�̑傫���p���??[�^�ł̒f�ʂƂȂ�~��?�߂�?B
            * ����ɉ~��|�����C���ߎ���?A���̓_��?��𓾂�?B
            */
            u_npnts = Circle2D.toPolylineNDivision(rad, uPint.increase(), tol) + 1;
        }

        if (meshType == 1) {
            /*
            * v���͈̔͂����_��܂ނƂ�?A���_�őO��ɕ�������?B
            * �܂܂Ȃ��Ƃ��͕������Ȃ�?B
            */
            double apexV = apexVParameter();
            double sp = vStart - apexV;
            double ep = vEnd - apexV;
            if (sp * ep >= 0.0) {
                v_npnts = 2;
            } else {
                Point3D apex = apex();
                if ((coordinates(0.0, vStart).identical(apex)) ||
                        (coordinates(0.0, vEnd).identical(apex)))
                    v_npnts = 2;
                else {
                    v_npnts = 3;
                    vMiddle = apexV;
                }
            }
        } else {
            v_npnts = 3;
            vMiddle = (vStart + vEnd) / 2.0;
        }

        /*
        * U���� u_npnts ��������?AV���̗��[�ł̈ʒu��i�q�_�Ƃ���?B
        * (�~�ʂ̃|�����C���ߎ��̓p���??[�^�����ƂȂ�͂��ł���)
        */
        mesh = new PointOnSurface3D[u_npnts][v_npnts];

        uDelta = uPint.increase() / (u_npnts - 1);
        uParam = uPint.start();
        for (i = 0; i < u_npnts; i++) {
            for (j = 0; j < v_npnts; j++) {
                if (j == 0)
                    vParam = vStart;
                else if (j == 2 || v_npnts == 2)
                    vParam = vEnd;
                else
                    vParam = vMiddle;
                mesh[i][j] = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
            }
            if (i == (u_npnts - 2))
                uParam = uPint.end();
            else
                uParam += uDelta;
        }

        return new Mesh3D(mesh, false);
    }

    /**
     * ���̉~??�ʂ�?A�^����ꂽ V ���̃p���??[�^��Ԃ̒��ł� (?�Βl��) ?ł�傫�Ȕ��a��Ԃ�?B
     *
     * @param vPint V ���̃p���??[�^���
     * @return ?ő唼�a
     */
    private double getMaxRadius(ParameterSection vPint) {

        double rads = Math.abs(this.radius + vPint.start() * Math.tan(this.semiAngle));
        double rade = Math.abs(this.radius + vPint.end() * Math.tan(this.semiAngle));
        return Math.max(rads, rade);
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

        int uNPoints = uBsplineCurve2D.nControlPoints();
        int vNPoints = 2;

        Point3D[][] controlPoints = new Point3D[uNPoints][vNPoints];
        double[][] weights = new double[uNPoints][vNPoints];

        CartesianTransformationOperator3D localTransformationOperator =
                this.position().toCartesianTransformationOperator(1.0);

        Point3D localVertex =
                Point3D.of(0.0, 0.0, (-(this.radius() / Math.tan(this.semiAngle()))));
        double vLowerCoord = vPint.start() / localVertex.z();
        double vUpperCoord = vPint.end() / localVertex.z();

        for (int ui = 0; ui < uNPoints; ui++) {
            Point3D uPoint = uBsplineCurve2D.controlPointAt(ui).to3D();
            controlPoints[ui][0] =
                    localTransformationOperator.toEnclosed(localVertex.linearInterpolate(uPoint, vLowerCoord));
            controlPoints[ui][1] =
                    localTransformationOperator.toEnclosed(localVertex.linearInterpolate(uPoint, vUpperCoord));
            weights[ui][0] = weights[ui][1] = uBsplineCurve2D.weightAt(ui);
        }

        return new BsplineSurface3D(uBsplineCurve2D.knotData(),
                BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
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
        double tanBsa = Math.tan(this.semiAngle());
        double dist = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        double ework = (tanBsa * point.z()) + this.radius();
        return Math.abs(dist - ework) < dTol;
    }

    /**
     * ����?����Ȗʂ� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?�����?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ����?����Ȗʂ� poly �̌�_��\����?�����?���
     */
    DoublePolynomial makePoly(DoublePolynomial[] poly) {
        double tanBsa = Math.tan(this.semiAngle());
        boolean isPoly = poly.length < 4;
        int degree = poly[0].degree();

        double[] zCoef = new double[degree + 1];

        if (isPoly) {
            for (int j = 0; j <= degree; j++) {
                zCoef[j] = tanBsa * poly[2].getCoefficientAsDouble(j);
            }
            zCoef[0] += this.radius();
        } else {
            for (int j = 0; j <= degree; j++) {
                zCoef[j] = tanBsa * poly[2].getCoefficientAsDouble(j) +
                        this.radius() * poly[3].getCoefficientAsDouble(j);
            }
        }

        DoublePolynomial xPoly = (DoublePolynomial) poly[0].multiply(poly[0]);
        DoublePolynomial yPoly = (DoublePolynomial) poly[1].multiply(poly[1]);
        DoublePolynomial workPoly = new DoublePolynomial(zCoef);
        DoublePolynomial wPoly = (DoublePolynomial) workPoly.multiply(workPoly);
        degree = xPoly.degree();
        double[] coef = new double[degree + 1];
        for (int j = 0; j <= degree; j++) {
            coef[j] = xPoly.getCoefficientAsDouble(j) +
                    yPoly.getCoefficientAsDouble(j) -
                    wPoly.getCoefficientAsDouble(j);
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
     *
     * @param mate ���̋�?� (��?�)
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        // get intersection one side only
        IntersectionPoint3D[] one_side;

        one_side = intersectLine(mate, doExchange);

        // get reversed cone
        ConicalSurface3D another_cone = getReverse();

        // get another side intersection
        IntersectionPoint3D[] another_side = another_cone.intersectLine(mate, doExchange);

        IntersectionPoint3D[] returnValue;

        // translate another side intersection
        if (another_side.length == 2) {     // case : both two intersection points exist another side
            // translate two intersection points
            Point3D pnt;
            double param[] = new double[2];

            pnt = another_side[0].coordinates();
            param = pointToParameter(pnt);
            PointOnSurface3D trans_another_side1 = new PointOnSurface3D(pnt, this, param[0], param[1]);

            pnt = another_side[1].coordinates();
            param = pointToParameter(pnt);
            PointOnSurface3D trans_another_side2 = new PointOnSurface3D(pnt, this, param[0], param[1]);

            returnValue = new IntersectionPoint3D[2];

            if (doExchange) {
                returnValue[0] =
                        new IntersectionPoint3D(another_side[0].pointOnGeometry1(), trans_another_side1, false);
                returnValue[1] =
                        new IntersectionPoint3D(another_side[1].pointOnGeometry1(), trans_another_side2, false);
            } else {
                returnValue[0] =
                        new IntersectionPoint3D(trans_another_side1, another_side[0].pointOnGeometry1(), false);
                returnValue[1] =
                        new IntersectionPoint3D(trans_another_side2, another_side[1].pointOnGeometry1(), false);
            }
            return returnValue;
        } else if (another_side.length == 1) {  // case : one intersection point exists another side

            // translate another side intersection point
            Point3D pnt;
            double param[] = new double[2];

            pnt = another_side[0].coordinates();
            param = pointToParameter(pnt);
            PointOnSurface3D trans_another_side = new PointOnSurface3D(pnt, this, param[0], param[1]);

            IntersectionPoint3D another_intersection;
            if (doExchange)
                another_intersection =
                        new IntersectionPoint3D(another_side[0].pointOnGeometry1(), trans_another_side, false);
            else
                another_intersection =
                        new IntersectionPoint3D(trans_another_side, another_side[0].pointOnGeometry2(), false);

            if (one_side.length == 1) {  // case : one intersection point exists this side

                // together this side intersection point & another side intersection point
                Point3D this_point = one_side[0].pointOnGeometry2().coordinates();
                Point3D another_point = another_intersection.pointOnGeometry2().coordinates();

                if (this_point.distance(another_point) < getToleranceForDistance()) {  // case : through vertex point
                    returnValue = new IntersectionPoint3D[1];
                    returnValue[0] = one_side[0];
                } else {  // case : intersection points exists on each other

                    returnValue = new IntersectionPoint3D[2];

                    double this_param;
                    if (doExchange)
                        this_param = ((PointOnCurve3D) one_side[0].pointOnGeometry1()).parameter();
                    else
                        this_param = ((PointOnCurve3D) one_side[0].pointOnGeometry2()).parameter();

                    double another_param;
                    if (doExchange)
                        another_param = ((PointOnCurve3D) another_intersection.pointOnGeometry1()).parameter();
                    else
                        another_param = ((PointOnCurve3D) another_intersection.pointOnGeometry2()).parameter();

                    if (this_param > another_param) {
                        returnValue[0] = one_side[0];
                        returnValue[1] = another_intersection;
                    } else {
                        returnValue[0] = another_intersection;
                        returnValue[1] = one_side[0];
                    }
                }
            } else {    // case : no intersection point exists this side
                returnValue = new IntersectionPoint3D[1];
                returnValue[0] = another_intersection;
            }
            return returnValue;
        } else {  // case : no intersection points exist another side
            // return this side intersection point
            return one_side;
        }
    }

    /**
     * ���̉~??�ʂ� ((radius + v * tan(semiAngle)) > 0) �̕����Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?� (��?�)
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersectLine(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        double paraA[] = new double[2];
        double paraB[][] = new double[2][2];
        double ework1, ework2;
        int number_of_intpnt;

        // tolerance
        double aTol = getToleranceForAngle();
        double dTol = getToleranceForDistance();

        // epsilon
        double epsilon = MachineEpsilon.DOUBLE;

        // calculate vertex position
        double etan = Math.tan(semiAngle());
        ework1 = radius() / etan;
        Point3D dBorg = position().location();
        Vector3D dBxyz = position().z();

        CartesianPoint3D Vp = new
                CartesianPoint3D(dBorg.x() - ework1 * dBxyz.x(),
                dBorg.y() - ework1 * dBxyz.y(),
                dBorg.z() - ework1 * dBxyz.z());
        Point3D int_pnt[];

        // check if line crosses cone's vertex
        PointOnCurve3D project = mate.project1From(Vp);
        if (project.distance(Vp) < dTol) {

            // line passes through the vertex of cone
            Vector3D uAdir = mate.dir().unitized();
            ework1 = Math.abs(uAdir.dotProduct(dBxyz));
            if (ework1 > 1.0) ework1 = 1.0;

            if (Math.abs(Math.acos(ework1) - semiAngle()) < aTol) {
                // A & B are overlap
                throw new IndefiniteSolutionException(mate.pnt());
            } else {
                // intersection is cone's vertex
                int_pnt = new Point3D[1];
                int_pnt[0] = new CartesianPoint3D(Vp.x(), Vp.y(), Vp.z());
                Vector3D pnt_to_vertex = Vp.subtract(mate.pnt());
                paraA[0] = pnt_to_vertex.length() / mate.dir().length();
                if (pnt_to_vertex.dotProduct(mate.dir()) < 0.0)
                    paraA[0] = -1 * paraA[0];
                paraB[0][0] = 0.0;
                paraB[0][1] = -1 * radius() / etan;

                number_of_intpnt = 1;
            }
        } else {
            // general case

            // transform line to cone's local coordinates system
            CartesianTransformationOperator3D trans = new
                    CartesianTransformationOperator3D(position(), 1.0);

            Point3D dAtpnt = trans.reverseTransform(mate.pnt());
            Vector3D dAtdir = trans.reverseTransform(mate.dir());
            Line3D dAt = new Line3D(dAtpnt, dAtdir);

            // make polynomial
            ework1 = etan * dAtdir.z();
            ework2 = etan * dAtpnt.z() + radius();

            double ecoef[] = new double[3];

            ecoef[2] = (dAtdir.x() * dAtdir.x()) + (dAtdir.y() * dAtdir.y()) - (ework1 * ework1);
            ecoef[1] = 2.0 * ((dAtpnt.x() * dAtdir.x()) + (dAtpnt.y() * dAtdir.y()) - (ework1 * ework2));
            ecoef[0] = (dAtpnt.x() * dAtpnt.x()) + (dAtpnt.y() * dAtpnt.y()) - (ework2 * ework2);

            DoublePolynomial poly = new DoublePolynomial(ecoef);

            double eroot[] = GeometryPrivateUtils.getRootsIfQuadric(poly);

            int_pnt = new Point3D[eroot.length];

            int j = 0;
            for (int i = 0; i < eroot.length; i++) {
                Point3D epnt = dAt.coordinates(eroot[i]);
                if (epnt.z() < (-(radius() / etan + dTol)))
                    continue;

                int_pnt[j] = mate.coordinates(eroot[i]);

                paraA[j] = eroot[i];

                if (Math.abs(epnt.x()) < epsilon)
                    if (epnt.y() > 0.0)
                        paraB[j][0] = Math.PI / 2.0;
                    else
                        paraB[j][0] = -1 * Math.PI / 2.0;
                else {
                    paraB[j][0] = Math.atan(epnt.y() / epnt.x());
                    if (epnt.x() < 0.0)
                        paraB[j][0] += Math.PI;
                    if (paraB[j][0] < 0.0)
                        paraB[j][0] += 2 * Math.PI;
                }
                paraB[j][1] = epnt.z();

                j++;
            }

            if (j == 2) {
                double dist = int_pnt[0].distance(int_pnt[1]);
                if (dist < dTol) {
                    j = 1;
                    int_pnt[0] = int_pnt[0].midPoint(int_pnt[1]);
                    paraA[0] = (paraA[0] + paraA[1]) / 2.0;
                    if (Math.abs(paraB[0][0] - paraB[0][1]) < Math.PI)
                        paraB[0][0] = (paraB[0][0] + paraB[1][0]) / 2.0;
                    paraB[0][1] = (paraB[0][1] + paraB[1][1]) / 2.0;
                }
            }
            number_of_intpnt = j;
        }

        IntersectionPoint3D return_pnt[] = new IntersectionPoint3D[number_of_intpnt];

        // make intersection point
        for (int i = 0; i < number_of_intpnt; i++) {
            // point on line
            PointOnCurve3D PonC =
                    new PointOnCurve3D(int_pnt[i], mate, paraA[i], doCheckDebug);

            // point on conic surface
            PointOnSurface3D PonS =
                    new PointOnSurface3D(int_pnt[i], this, paraB[i][0], paraB[i][1], doCheckDebug);

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
     * @throws IndefiniteSolutionException mate ��~??�ʂ�?A���҂��I?[�o?[���b�v���Ă���?A�⪕s��ł���
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
     * ��?ۂ̉��Z��
     * {@link IntsPlnCon3D IntsPlnCon3D}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate,
                                             boolean doExchange) {
        IntsPlnCon3D doObj = new IntsPlnCon3D(mate, this);
        return doObj.getInterference(!doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
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
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate,
                                             boolean doExchange) {
        IntsSphCon3D doObj = new IntsSphCon3D(mate, this);
        return doObj.getInterference(!doExchange);
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
     * {@link IntsCylCon3D#intersection(CylindricalSurface3D,ConicalSurface3D,boolean)
     * IntsCylCon3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange) {
        return IntsCylCon3D.intersection(mate, this, !doExchange);
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
     * {@link IntsConCon3D IntsConCon3D}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException ���҂��I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        IntsConCon3D doObj = new IntsConCon3D(this, mate);
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
     *
     * @param uParam U ���̃p���??[�^�l
     * @return �w��� U �p���??[�^�l�ł̓��p���??[�^��?�
     */
    public ParametricCurve3D uIsoParametricCurve(double uParam) {
        CartesianTransformationOperator3D trns = position().toCartesianTransformationOperator();

        double tan_sa = Math.tan(semiAngle());
        double cos_u = Math.cos(uParam);
        double sin_u = Math.sin(uParam);

        // point at (para, 0.0) becomes start point of Line
        Point3D pnt = new CartesianPoint3D(cos_u * radius(), sin_u * radius(), 0.0);
        pnt = trns.toEnclosed(pnt);
        // V-dir tangent vector becomes direction of Line
        Vector3D dir = new LiteralVector3D(tan_sa * cos_u, tan_sa * sin_u, 1.0);
        dir = trns.toEnclosed(dir);

        return new Line3D(pnt, dir);
    }

    /**
     * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
     *
     * @param vParam V ���̃p���??[�^�l
     * @return �w��� V �p���??[�^�l�ł̓��p���??[�^��?�
     */
    public ParametricCurve3D vIsoParametricCurve(double vParam)
            throws ReducedToPointException {
        CartesianTransformationOperator3D trns = position().toCartesianTransformationOperator();

        double tan_sa = Math.tan(semiAngle());
        Point3D cntr = new CartesianPoint3D(0.0, 0.0, vParam);
        cntr = trns.toEnclosed(cntr);    // center of Circle
        double cRadius = radius() + vParam * tan_sa;
        if (cRadius <= getToleranceForDistance()) {
            throw new ReducedToPointException(cntr);
        }

        /*
        * copy Axis2Placement
        * but when (virtual) radius is less than zero, reverse X axis.
        */
        Vector3D xVec = position().x();
        if (radius < 0.0) {
            xVec = xVec.reverse();
            radius = -radius;
        }
        Axis2Placement3D a2p = new Axis2Placement3D(cntr, position().z(), xVec);
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
     * ���Ŕ���I�Ȓ�`���Ԃ�?B
     * </p>
     *
     * @return ���̋Ȗʂ� V ���̃p���??[�^��`��
     */
    ParameterDomain getVParameterDomain() {
        return new ParameterDomain();
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#CONICAL_SURFACE_3D ParametricSurface3D.CONICAL_SURFACE_3D}
     */
    int type() {
        return CONICAL_SURFACE_3D;
    }

    /**
     * ���̉~??�ʂ̒��_�ɂ����� V ���̃p���??[�^�l��Ԃ�?B
     *
     * @return ���_�ɂ����� V ���̃p���??[�^�l
     */
    private double apexVParameter() {
        return (-radius() / Math.tan(semiAngle()));
    }

    /**
     * ���̉~??�ʂ̒��_��?W�l��Ԃ�?B
     *
     * @return ���_
     */
    Point3D apex() {
        return coordinates(0.0, apexVParameter());
    }

    /**
     * ���̉~??�ʂ̒�?S���ƂȂ钼?��Ԃ�?B
     *
     * @return ��?S������?�
     */
    Line3D getAxis() {
        return new Line3D(position().location(), position().z());
    }

    /**
     * ���̉~??�ʂ𔽓]�������~??�ʂ�Ԃ�?B
     *
     * @return ���]�������~??��
     */
    ConicalSurface3D getReverse() {
        Vector3D originToVertex = apex().subtract(position().location());
        Point3D reverseLocation = getAxis().project1From(apex().add(originToVertex));
        Axis2Placement3D reverse_position = new
                Axis2Placement3D(reverseLocation,
                position().z().reverse(),
                position().x().reverse());

        ConicalSurface3D result =
                new ConicalSurface3D(reverse_position, this.radius, this.semiAngle);
        return result;
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

        Mesh3D mesh = this.makeMesh(2, uParameterSection, vParameterSection,
                new ToleranceForDistance(tolerance));

        for (int u = 0; u < mesh.uNPoints(); u++)
            for (int v = 0; v < mesh.vNPoints(); v++)
                result.addElement(mesh.pointAt(u, v));

        scalingFactor[0] = getMaxRadius(vParameterSection);
        scalingFactor[1] = Math.sqrt(1.0 + (this.semiAngle * this.semiAngle));

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
        return new ConicalSurface3D(tPosition, tRadius, this.semiAngle);
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
        writer.println(indent_tab + "\tsemiAngle\t" + semiAngle);
        writer.println(indent_tab + "End");
    }
}
