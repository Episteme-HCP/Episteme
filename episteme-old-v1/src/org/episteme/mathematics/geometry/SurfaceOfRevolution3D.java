/*
 * �R���� : ��]�ʂ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SurfaceOfRevolution3D.java,v 1.3 2006/03/01 21:16:11 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : ��]�ʂ�\���N���X?B
 * <p/>
 * ��]�ʂƂ�?A
 * ����R������?�⠂钼?�̎��ŉ�]�������O?Ղ�ȖʂƂ݂Ȃ���̂ł���?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��]������ׂ��R������?� sweptCurve
 * ��
 * ��]�̒�?S�����z�u?�� (��?�?W�n, {@link Axis1Placement3D Axis1Placement3D}) axisPosition
 * ��ێ?����?B
 * ������?AsweptCurve �� {@link SweptSurface3D �X?[�p?[�N���X} ��ŕێ?�����?B
 * </p>
 * <p/>
 * ��]�ʂ� U ���̃p���??[�^��`��͗L�Ŏ��I�ł���?A
 * ���̃v���C�}���ȗL���Ԃ� [0, (2 * ��)] �ł���?B
 * V ���̃p���??[�^��`���?AsweptCurve �̃p���??[�^��`��Ɉ�v����?B
 * </p>
 * <p/>
 * (u, v) ��p���??[�^�Ƃ����]�� P(u, v) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(u, v) = c + m(u) * cos(v) + ((m(u), d) * d * (1 - cos(v)) + (d X m(u)) * sin(v)
 * </pre>
 * ������
 * <pre>
 * 	c = axisPosition.location()
 * 	d = axisPosition.z()
 * 	m(u) = sweptCurve(u) - c
 * 	(a, b) : a, b �̓�?�
 * 	(a X b) : a, b �̊O?�
 * </pre>
 * ��\��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:11 $
 */

public class SurfaceOfRevolution3D extends SweptSurface3D {
    /**
     * ��]�̒�?S��?B
     *
     * @serial
     */
    private Axis1Placement3D axisPosition;

    /**
     * ��]�̒�?S������?�?W�n������?W�n�ւ̕ϊ����Z�q?B
     *
     * @serial
     */
    private CartesianTransformationOperator3D transformationOperator;

    /**
     * ��]�������?�Ɖ�]�̒�?S����^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * sweptCurve ���邢�� axisPosition �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param sweptCurve   ��]�������?�
     * @param axisPosition ��]�̒�?S��
     * @see InvalidArgumentValueException
     */
    public SurfaceOfRevolution3D(ParametricCurve3D sweptCurve,
                                 Axis1Placement3D axisPosition) {
        super(sweptCurve);
        setAxisPosition(axisPosition);
        setTransformationOperator();
    }

    /**
     * ��]�̒�?S����t�B?[���h��?ݒ肷��?B
     * <p/>
     * axisPosition �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param axisPosition ��]�̒�?S��
     * @see InvalidArgumentValueException
     */
    private void setAxisPosition(Axis1Placement3D axisPosition) {
        if (axisPosition == null) {
            throw new InvalidArgumentValueException();
        }
        this.axisPosition = axisPosition;
    }

    /**
     * ��]�̒�?S������?�?W�n������?W�n�ւ̕ϊ����Z�q��?ݒ肷��?B
     */
    private void setTransformationOperator() {
        ParameterDomain pd = this.vParameterDomain();

        double sp = 0.0;
        double ep = 1000.0;
        if (pd.isFinite() == true) {
            sp = pd.section().start();
            ep = pd.section().end();
        }
        double ip = (ep - sp) / 1000.0;

        double dTol2 = this.getToleranceForDistance2();
        double cosATol = Math.cos(this.getToleranceForAngle());

        Point3D location = this.axisPosition().location();
        Vector3D axis = this.axisPosition().z();    // unitized
        Vector3D refDir = null;

        for (int i = 0; i <= 1000; i++) {
            refDir = this.sweptCurve().coordinates(sp + (i * ip)).subtract(location);
            if (refDir.norm() > dTol2) {
                refDir = refDir.unitized();
                if (Math.abs(axis.dotProduct(refDir)) < cosATol) {
                    transformationOperator =
                            new CartesianTransformationOperator3D
                                    (new Axis2Placement3D(location, axis, refDir), 1.0);
                    return;
                }
            }
        }

        throw new FatalException("Maybe the surface is degenate.");
    }

    /**
     * ���̋Ȗʂ̉�]�̒�?S�����z�u?���Ԃ�?B
     *
     * @return ��]�̒�?S�����z�u?��
     */
    public Axis1Placement3D axisPosition() {
        return axisPosition;
    }

    /**
     * ���̋Ȗʂ̉�]�̒�?S����\����?��Ԃ�?B
     *
     * @return ��]�̒�?S����\����?�
     */
    public Line3D axisLine() {
        return axisPosition().toLine();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?W�l
     * @see ParameterOutOfRange
     */
    public Point3D coordinates(double uParam, double vParam) {
        Point3D cpnt;

        cpnt = sweptCurve().coordinates(vParam);

        Point3D lpnt;
        double u_cos = Math.cos(uParam);
        double u_sin = Math.sin(uParam);

        lpnt = transformationOperator.reverseTransform(cpnt);
        return transformationOperator.transform(XYRotation(lpnt, u_cos, u_sin));
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
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?ڃx�N�g��
     * @see ParameterOutOfRange
     */
    public Vector3D[] tangentVector(double uParam, double vParam) {
        Point3D cpnt;
        Vector3D ctng;

        cpnt = sweptCurve().coordinates(vParam);
        ctng = sweptCurve().tangentVector(vParam);

        Vector3D[] tng = new Vector3D[2];
        Point3D lpnt;
        Vector3D lUtng, lVtng;
        double u_cos = Math.cos(uParam);
        double u_sin = Math.sin(uParam);

        lpnt = transformationOperator.reverseTransform(cpnt);
        lUtng = new LiteralVector3D(-lpnt.y(), lpnt.x(), 0.0);
        lVtng = transformationOperator.reverseTransform(ctng);
        tng[0] = transformationOperator.transform(XYRotation(lUtng, u_cos, u_sin));
        tng[1] = transformationOperator.transform(XYRotation(lVtng, u_cos, u_sin));
        return tng;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     * @see ParameterOutOfRange
     */
    public SurfaceDerivative3D evaluation(double uParam, double vParam) {
        CurveDerivative3D crv_drv;

        crv_drv = sweptCurve().evaluation(vParam);

        Point3D L;
        Vector3D Lu, Lv, Luu, Lvv, Luv;
        Point3D d0;
        Vector3D du, dv, duu, duv, dvv;
        double u_cos = Math.cos(uParam);
        double u_sin = Math.sin(uParam);

        L = transformationOperator.reverseTransform(crv_drv.d0D());
        Lv = transformationOperator.reverseTransform(crv_drv.d1D());
        Lvv = transformationOperator.reverseTransform(crv_drv.d2D());
        Lu = new LiteralVector3D(-L.y(), L.x(), 0.0);
        Luu = new LiteralVector3D(-L.x(), -L.y(), 0.0);
        Luv = new LiteralVector3D(-Lv.y(), Lv.x(), 0.0);
        d0 = transformationOperator.transform(XYRotation(L, u_cos, u_sin));
        du = transformationOperator.transform(XYRotation(Lu, u_cos, u_sin));
        dv = transformationOperator.transform(XYRotation(Lv, u_cos, u_sin));
        duu = transformationOperator.transform(XYRotation(Luu, u_cos, u_sin));
        dvv = transformationOperator.transform(XYRotation(Lvv, u_cos, u_sin));
        duv = transformationOperator.transform(XYRotation(Luv, u_cos, u_sin));
        return new SurfaceDerivative3D(d0, du, dv, duu, duv, dvv);
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂ̉�]������ׂ���?�ւ̓��e�_�̈�� suitable �Ƃ���
     * IndefiniteSolutionException �̗�O��?�������?B
     *
     * @param point ���e���̓_
     * @throws IndefiniteSolutionException �⪕s��ł��� (��?��̉⪑�?݂���)
     * @see #projectFrom(Point3D)
     */
    private void indefiniteFoot(Point3D point)
            throws IndefiniteSolutionException {
        PointOnCurve3D p;

        try {
            p = sweptCurve().projectFrom(point)[0];
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        throw new IndefiniteSolutionException(p);
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * point ����?A���̋Ȗʂ̉�]�̒�?S���ւ̋�����
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?�����?�?��ɂ�
     * ��s��Ƃ��� IndefiniteSolutionException �̗�O��?�����?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł��� (��?��̉⪑�?݂���)
     */
    public PointOnSurface3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        /*
        * If projected point is on the Revolution's axis
        * then throws IndefiniteSolutionException.
        */
        if (point.isOn(axisLine())) {
            indefiniteFoot(point);
        }

        CartesianTransformationOperator3D cto = getCartesianTransformationOperator();

        // make angle1.
        Point3D local_point = cto.toLocal(point);
        local_point = Point3D.of(local_point.x(), local_point.y(), 0.0);
        Vector3D local_vector = local_point.toVector3D().unitized();
        double angle1 = Math.acos(local_vector.x());
        if (local_point.y() < 0.0) {
            angle1 = 2 * Math.PI - angle1;
        }

        // rotate swept curve1.
        ParametricCurve3D curve = sweptCurve();
        ParametricCurve3D rotated_curve1 =
                curve.rotateZ(cto, Math.cos(angle1), Math.sin(angle1));

        // make projected point on rotated swept curve1.
        PointOnCurve3D[][] proj_on_curve = new PointOnCurve3D[2][];
        proj_on_curve[0] = rotated_curve1.projectFrom(point);

        // make angle2.
        double angle2 = angle1 + Math.PI;
        if (angle2 > 2 * Math.PI) {
            angle2 -= 2 * Math.PI;
        }

        // rotate swept curve2.
        ParametricCurve3D rotated_curve2 =
                curve.rotateZ(cto, Math.cos(angle2), Math.sin(angle2));

        // make projected points on rotated swept curve2.
        proj_on_curve[1] = rotated_curve2.projectFrom(point);

        // convert PointOnCurve3D to PointOnSurface3D.
        PointOnSurface3D[] proj = new
                PointOnSurface3D[proj_on_curve[0].length +
                proj_on_curve[1].length];

        int i = 0;
        double uParam, vParam;

        uParam = angle1;
        for (int j = 0; j < proj_on_curve[0].length; i++, j++) {
            vParam = proj_on_curve[0][j].parameter();
            proj[i] = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
        }

        uParam = angle2;
        for (int k = 0; k < proj_on_curve[1].length; i++, k++) {
            vParam = proj_on_curve[1][k].parameter();
            proj[i] = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
        }

        return proj;
    }

    /**
     * ��]��?ő唼�a?B
     */
    private transient double maxRadius = 0.0;

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
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
        Polyline3D pol;
        int u_npnts, v_npnts;
        Line3D lin;
        Point3D pnt;
        PointOnCurve3D foot;
        double dist;
        PointOnCurve3D poc;
        double uParam, vParam, uDelta;
        int i, j;

        /*
        * �X�C?[�v�����?��^����ꂽ���e��?��Ń|�����C���ߎ�����?B
        */
        pol = sweptCurve().toPolyline(vPint, tol);
        v_npnts = pol.nPoints();

        /*
        * ��]�̒�?S������?łɓ���|�����C����?ߓ_��?��?A
        * ��]��?ő唼�a�𓾂�?B
        */
        lin = new Line3D(axisPosition.location(), axisPosition.z());
        maxRadius = 0.0;
        for (i = 0; i < v_npnts; i++) {
            pnt = pol.pointAt(i);
            try {
                foot = lin.project1From(pnt);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
            dist = foot.distance2(pnt);
            if (dist > maxRadius) {
                maxRadius = dist;
            }
        }
        maxRadius = Math.sqrt(maxRadius);

        if (maxRadius <= ConditionOfOperation.getCondition().getToleranceForDistance()) {
            u_npnts = 2;
        } else {
            /*
            * ?ő唼�a�𔼌a�Ƃ���~��?��?A
            * ���̉~��|�����C���ߎ���?A���̓_��?��𓾂�?B
            */
            try {
                Circle3D cir;

                cir = new Circle3D(axisPosition.location(), axisPosition.z(), maxRadius);
                u_npnts = cir.toPolyline(uPint, tol).nPoints();
                if (u_npnts < 2) u_npnts = 2;
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        /*
        * U���� u_npnts ��������?AV���̓|�����C���ߎ��̃p���??[�^��p���Ċi�q�_��?\?�����?B
        * (�~�ʂ̃|�����C���ߎ��̓p���??[�^�����ƂȂ�͂��ł���)
        */
        mesh = new PointOnSurface3D[u_npnts][v_npnts];

        uDelta = uPint.increase() / (u_npnts - 1);
        uParam = uPint.start();
        for (i = 0; i < u_npnts; i++) {
            for (j = 0; j < v_npnts; j++) {
                poc = (PointOnCurve3D) pol.pointAt(j);
                vParam = poc.parameter();
                try {
                    mesh[i][j] = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
                } catch (InvalidArgumentValueException e) {
                    throw new FatalException();
                }
            }
            if (i == (u_npnts - 2))
                uParam = uPint.end();
            else
                uParam += uDelta;
        }

        return new Mesh3D(mesh, false);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @return ���̋Ȗʂ̎w��̋�Ԃ�?Č�����L�? Bspline �Ȗ�
     * @see ParameterOutOfRange
     */
    public BsplineSurface3D
    toBsplineSurface(ParameterSection uPint,
                     ParameterSection vPint) {
        Circle2D uUnitCircle2D = new Circle2D(Axis2Placement2D.origin, 1.0);
        BsplineCurve2D uUnitBsplineCurve2D = uUnitCircle2D.toBsplineCurve(uPint);

        BsplineCurve3D vBsplineCurve = this.sweptCurve().toBsplineCurve(vPint);

        int uNPoints = uUnitBsplineCurve2D.nControlPoints();
        int vNPoints = vBsplineCurve.nControlPoints();

        Point3D[] vLocalControlPoints = new Point3D[vNPoints];
        for (int vi = 0; vi < vNPoints; vi++)
            vLocalControlPoints[vi] =
                    transformationOperator.toLocal(vBsplineCurve.controlPointAt(vi));

        Point3D[][] controlPoints = new Point3D[uNPoints][vNPoints];
        double[][] weights = new double[uNPoints][vNPoints];

        for (int ui = 0; ui < uNPoints; ui++) {
            Point2D uPoint = uUnitBsplineCurve2D.controlPointAt(ui);
            for (int vi = 0; vi < vNPoints; vi++) {
                controlPoints[ui][vi] =
                        transformationOperator.toEnclosed
                                (this.XYRotation(vLocalControlPoints[vi], uPoint.x(), uPoint.y()));
                weights[ui][vi] = uUnitBsplineCurve2D.weightAt(ui) * vBsplineCurve.weightAt(vi);
            }
        }

        return new BsplineSurface3D(uUnitBsplineCurve2D.knotData(),
                vBsplineCurve.knotData(),
                controlPoints, weights);
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
     * ���̋Ȗʂ̉�]�̒�?S�����?� Z ���Ƃ���?A�����?�?W�n������?W�n�ւ�
     * �ϊ����Z�q��Ԃ�?B
     *
     * @return ��]�̒�?S�����?� Z ���Ƃ���?A�����?�?W�n������?W�n�ւ̕ϊ����Z�q
     */
    private CartesianTransformationOperator3D getCartesianTransformationOperator() {

        /*
        *     Make vector xv which is parpendicular with the axisPosition
        * and its direction of the plane that include the sweptCurve and
        * the axisPosition.
        */
        Line3D line = axisLine();
        Point3D x = sweptCurve().getPointNotOnLine(line);
        Vector3D xv = x.subtract(line.project1From(x));

        // make CartesianTransformationOperator3D by Revolution's axis and xv.
        Axis2Placement3D local_position
                = new Axis2Placement3D(axisPosition().location(), axisPosition().z(), xv);
        return local_position.toCartesianTransformationOperator();
    }

    /*
    * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param uParam	U ���̃p���??[�^�l
    * @return	�w��� U �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D uIsoParametricCurve(double uParam) {
        CartesianTransformationOperator3D cto = getCartesianTransformationOperator();
        return sweptCurve().rotateZ(cto, Math.cos(uParam), Math.sin(uParam));
    }

    /*
    * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param vParam	V ���̃p���??[�^�l
    * @return	�w��� V �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D vIsoParametricCurve(double vParam)
            throws ReducedToPointException {
        Point3D pnt = sweptCurve().coordinates(vParam);
        Point3D prj = axisLine().project1From(pnt).literal();
        double radius = pnt.distance(prj);

        if (radius <= getToleranceForDistance()) {
            throw new ReducedToPointException(prj);
        }
        Axis2Placement3D a2p = new Axis2Placement3D(prj, axisPosition().z(), pnt.subtract(prj));
        return new Circle3D(a2p, radius);
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * �L�Ŏ��I�ȃp���??[�^��`���Ԃ�?B
     * ���̒�`��̃v���C�}���ȗL���Ԃ� [0, (2 * ��)] �ł���?B
     * </p>
     *
     * @return U ���̃p���??[�^��`��
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
     * ��]�������?�̃p���??[�^��`���Ԃ�?B
     * </p>
     *
     * @return V ���̃p���??[�^��`��
     */
    ParameterDomain getVParameterDomain() {
        return sweptCurve().parameterDomain();
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#SURFACE_OF_REVOLUTION_3D ParametricSurface3D.SURFACE_OF_REVOLUTION_3D}
     */
    int type() {
        return SURFACE_OF_REVOLUTION_3D;
    }

    /**
     * �^����ꂽ�_��?A���?W�n�� Z �����̎��Ŏw��̊p�x�Ƃ�����]������?B
     *
     * @param pnt      ��]������_
     * @param cosValue cos(��) �̒l
     * @param sinValue sin(��) �̒l
     * @return ��]��̓_
     */
    private Point3D XYRotation(Point3D pnt,
                               double cosValue, double sinValue) {
        double x, y, z;

        x = cosValue * pnt.x() - sinValue * pnt.y();
        y = sinValue * pnt.x() + cosValue * pnt.y();
        z = pnt.z();
        return new CartesianPoint3D(x, y, z);
    }

    /**
     * �^����ꂽ�x�N�g����?A���?W�n�� Z �����̎��Ŏw��̊p�x�Ƃ�����]������?B
     *
     * @param vec      ��]������x�N�g��
     * @param cosValue cos(��) �̒l
     * @param sinValue sin(��) �̒l
     * @return ��]��̃x�N�g��
     */
    private Vector3D XYRotation(Vector3D vec,
                                double cosValue, double sinValue) {
        double x, y, z;

        x = cosValue * vec.x() - sinValue * vec.y();
        y = sinValue * vec.x() + cosValue * vec.y();
        z = vec.z();
        return new LiteralVector3D(x, y, z);
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

        double vScale = 0.0;
        for (int v = 1; v < mesh.vNPoints(); v++)
            vScale += mesh.pointAt(0, v).distance(mesh.pointAt(0, (v - 1)));
        vScale /= vParameterSection.increase();

        scalingFactor[0] = maxRadius;
        scalingFactor[1] = vScale;

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
        ParametricCurve3D tSweptCurve =
                this.sweptCurve().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        Axis1Placement3D tAxisPosition =
                this.axisPosition.transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new SurfaceOfRevolution3D(tSweptCurve, tAxisPosition);
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
        writer.println(indent_tab + "\tsweptCurve");
        sweptCurve().output(writer, indent + 2);
        writer.println(indent_tab + "\taxisPosition");
        axisPosition.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
