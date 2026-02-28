/*
 * �R���� : ���ʂ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SurfaceOfLinearExtrusion3D.java,v 1.3 2006/03/01 21:16:11 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : ���ʂ�\���N���X?B
 * <p/>
 * ���ʂƂ�?A
 * ����R������?�⠂���ɂ܂B����|�� (�X�C?[�v) �����O?Ղ�ȖʂƂ݂Ȃ���̂ł���?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �X�C?[�v������ׂ��R������?� sweptCurve
 * ��
 * sweptCurve ��X�C?[�v��������x�N�g�� extrusionAxis
 * ��ێ?����?B
 * ������?AsweptCurve �� {@link SweptSurface3D �X?[�p?[�N���X} ��ŕێ?�����?B
 * </p>
 * <p/>
 * ���ʂ� U ���̃p���??[�^��`���?AsweptCurve �̃p���??[�^��`��Ɉ�v����?B
 * V ���̃p���??[�^��`��͖��Ŕ���I�ł���?B
 * </p>
 * <p/>
 * (u, v) ��p���??[�^�Ƃ��钌�� P(u, v) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(u, v) = sweptCurve(u) + v * extrusionAxis
 * </pre>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:11 $
 */

public class SurfaceOfLinearExtrusion3D extends SweptSurface3D {
    /**
     * �X�C?[�v������?B
     *
     * @serial
     */
    private Vector3D extrusionAxis;

    /**
     * �X�C?[�v�������?�ƃX�C?[�v�������^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * sweptCurve ���邢�� extrusionAxis �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param sweptCurve    �X�C?[�v�������?�
     * @param extrusionAxis �X�C?[�v������
     * @see InvalidArgumentValueException
     */
    public SurfaceOfLinearExtrusion3D(ParametricCurve3D sweptCurve,
                                      Vector3D extrusionAxis) {
        super(sweptCurve);
        setExtrusionAxis(extrusionAxis);
    }

    /**
     * �X�C?[�v�������t�B?[���h��?ݒ肷��?B
     * <p/>
     * extrusionAxis �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param extrusionAxis �X�C?[�v������
     * @see InvalidArgumentValueException
     */
    private void setExtrusionAxis(Vector3D extrusionAxis) {
        if (extrusionAxis == null) {
            throw new InvalidArgumentValueException();
        }
        this.extrusionAxis = extrusionAxis;
    }

    /**
     * ���̋Ȗʂ�?A�X�C?[�v�������Ԃ�?B
     *
     * @return �X�C?[�v������
     */
    public Vector3D extrusionAxis() {
        return extrusionAxis;
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
        return sweptCurve().coordinates(uParam).add(extrusionAxis.multiply(vParam));
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
        Vector3D[] tng = new Vector3D[2];

        tng[0] = sweptCurve().tangentVector(uParam);
        tng[1] = extrusionAxis;
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
        Point3D d0;
        Vector3D du, dv, duu, duv, dvv;

        crv_drv = sweptCurve().evaluation(uParam);
        d0 = crv_drv.d0D().add(extrusionAxis.multiply(vParam));
        ;
        du = crv_drv.d1D();
        dv = extrusionAxis;
        duu = crv_drv.d2D();
        dvv = duv = Vector3D.zeroVector;
        return new SurfaceDerivative3D(d0, du, dv, duu, duv, dvv);
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnSurface3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        // make plane include the sweptCurve.
        Line3D line = new Line3D(Point3D.origin, extrusionAxis);
        Point3D point_on_curve = sweptCurve().getPointNotOnLine(line);
        Vector3D normal = extrusionAxis;
        Plane3D plane = new Plane3D(point_on_curve, normal);

        // make projection from point to above plane.
        PointOnSurface3D lpoint = plane.projectFrom(point)[0];

        // make a AbstractCartesianTransformationOperator of lpoint.
        Axis2Placement3D position = new
                Axis2Placement3D(lpoint, normal,
                normal.verticalVector().unitized());
        CartesianTransformationOperator3D cto = new
                CartesianTransformationOperator3D(position, 1.0);

        // make projection on above plane.
        PointOnCurve3D[] lproj = sweptCurve().projectFrom(lpoint);
        // make projection to SweptSurface.
        PointOnSurface3D[] proj = new PointOnSurface3D[lproj.length];
        Vector3D vector = point.subtract(lpoint);
        double flag = 1.0;
        if (!normal.identicalDirection(vector)) {
            flag = -1.0;
        }
        double vParam = flag * Math.sqrt(vector.norm() / normal.norm());
        for (int i = 0; i < lproj.length; i++) {
            double uParam = lproj[i].parameter();
            proj[i] = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
        }

        return proj;
    }

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
    toMesh(ParameterSection uPint,
           ParameterSection vPint,
           ToleranceForDistance tol) {
        return this.makeMesh(1, uPint, vPint, tol);
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
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param meshType toMesh ����Ȃ� 1 ?AtoNonStructuredPoints ����Ȃ� 2
     * @param uPint    U ���̃p���??[�^���
     * @param vPint    V ���̃p���??[�^���
     * @param tol      �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see ParameterOutOfRange
     */
    private Mesh3D
    makeMesh(int meshType,
             ParameterSection uPint,
             ParameterSection vPint,
             ToleranceForDistance tol) {
        PointOnSurface3D[][] mesh;
        Polyline3D pol;
        int u_npnts;
        PointOnCurve3D poc;
        double uParam;
        double vStart = vPint.start();
        double vEnd = vPint.end();
        double vMiddle = (vStart + vEnd) / 2.0;
        int i;

        /*
        * �X�C?[�v�����?��^����ꂽ���e��?��Ń|�����C���ߎ�����?B
        */
        pol = sweptCurve().toPolyline(uPint, tol);
        u_npnts = pol.nPoints();

        if (meshType == 1) {
            mesh = new PointOnSurface3D[u_npnts][2];
        } else {
            mesh = new PointOnSurface3D[u_npnts][3];
        }

        /*
        * U���̓|�����C���̋ߎ��̃p���??[�^?AV���̓p���??[�^��Ԃ̗��[�Ŋi�q�_��?\?�����?B
        */
        for (i = 0; i < u_npnts; i++) {
            poc = (PointOnCurve3D) pol.pointAt(i);
            uParam = poc.parameter();
            try {
                mesh[i][0] = new PointOnSurface3D(this, uParam, vStart, doCheckDebug);
                if (meshType == 1) {
                    mesh[i][1] = new PointOnSurface3D(this, uParam, vEnd, doCheckDebug);
                } else {
                    mesh[i][1] = new PointOnSurface3D(this, uParam, vMiddle, doCheckDebug);
                    mesh[i][2] = new PointOnSurface3D(this, uParam, vEnd, doCheckDebug);
                }
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
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
        BsplineCurve3D uBsplineCurve = this.sweptCurve().toBsplineCurve(uPint);

        int uNPoints = uBsplineCurve.nControlPoints();
        int vNPoints = 2;

        Point3D[][] controlPoints = new Point3D[uNPoints][vNPoints];
        double[][] weights = new double[uNPoints][vNPoints];

        Vector3D vLowerVec = this.extrusionAxis().multiply(vPint.start());
        Vector3D vUpperVec = this.extrusionAxis().multiply(vPint.end());

        for (int ui = 0; ui < uNPoints; ui++) {
            Point3D uPoint = uBsplineCurve.controlPointAt(ui);
            controlPoints[ui][0] = uPoint.add(vLowerVec);
            controlPoints[ui][1] = uPoint.add(vUpperVec);
            weights[ui][0] = weights[ui][1] = uBsplineCurve.weightAt(ui);
        }

        return new BsplineSurface3D(uBsplineCurve.knotData(),
                BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
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

    /*
    * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param uParam	U ���̃p���??[�^�l
    * @return	�w��� U �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D uIsoParametricCurve(double uParam) {
        return new Line3D(sweptCurve().coordinates(uParam), extrusionAxis);
    }

    /*
    * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param vParam	V ���̃p���??[�^�l
    * @return	�w��� V �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D vIsoParametricCurve(double vParam) {
        Vector3D moveVec = extrusionAxis.multiply(vParam);
        return sweptCurve().parallelTranslate(moveVec);
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * �X�C?[�v�������?�̃p���??[�^��`���Ԃ�?B
     * </p>
     *
     * @return U ���̃p���??[�^��`��
     */
    ParameterDomain getUParameterDomain() {
        return sweptCurve().parameterDomain();
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * ���Ŕ���I�Ȓ�`���Ԃ�?B
     * </p>
     *
     * @return V ���̃p���??[�^��`��
     */
    ParameterDomain getVParameterDomain() {
        return new ParameterDomain();
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#SURFACE_OF_LINEAR_EXTRUSION_3D ParametricSurface3D.SURFACE_OF_LINEAR_EXTRUSION_3D}
     */
    int type() {
        return SURFACE_OF_LINEAR_EXTRUSION_3D;
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

        double uScale = 0.0;
        for (int u = 0; u < mesh.uNPoints(); u++) {
            if (u > 0)
                uScale += mesh.pointAt(u, 0).distance(mesh.pointAt((u - 1), 0));
            for (int v = 0; v < mesh.vNPoints(); v++)
                result.addElement(mesh.pointAt(u, v));
        }
        uScale /= uParameterSection.increase();

        scalingFactor[0] = uScale;
        scalingFactor[1] = 1.0;

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
        Vector3D tExtrusionAxis =
                this.extrusionAxis.transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new SurfaceOfLinearExtrusion3D(tSweptCurve, tExtrusionAxis);
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
        writer.println(indent_tab + "\textrusionAxis");
        extrusionAxis.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}

