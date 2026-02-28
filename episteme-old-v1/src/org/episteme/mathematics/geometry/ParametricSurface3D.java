/*
 * �R�����̃p���?�g���b�N�ȋȖʂ̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ParametricSurface3D.java,v 1.2 2006/03/01 21:16:06 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import java.util.Vector;

/**
 * �R�����̃p���?�g���b�N�ȋȖʂ̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * ���̃N���X��?A��̎�?��l�ŕ\�����p���??[�^�̑g (u, v) �̒l�ɂ�B�?A
 * �ʒu�����肳���R�����̋Ȗ� P(u, v) �S�ʂ��?��?�����\������?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:06 $
 */

public abstract class ParametricSurface3D extends AbstractParametricSurface {
    /**
     * �Ȗʂ� Plane �ł��邱�Ƃ���?�
     */
    static final int PLANE_3D = 1;

    /**
     * �Ȗʂ� Cylindrical Surface �ł��邱�Ƃ���?�
     */
    static final int CYLINDRICAL_SURFACE_3D = 10;

    /**
     * �Ȗʂ� Conical Surface �ł��邱�Ƃ���?�
     */
    static final int CONICAL_SURFACE_3D = 11;

    /**
     * �Ȗʂ� Spherical Surface �ł��邱�Ƃ���?�
     */
    static final int SPHERICAL_SURFACE_3D = 12;

    /**
     * �Ȗʂ� Surface Of Linear Extrusion �ł��邱�Ƃ���?�
     */
    static final int SURFACE_OF_LINEAR_EXTRUSION_3D = 20;

    /**
     * �Ȗʂ� Surface Of Revolution �ł��邱�Ƃ���?�
     */
    static final int SURFACE_OF_REVOLUTION_3D = 21;

    /**
     * �Ȗʂ� Bspline Surface �ł��邱�Ƃ���?�
     */
    static final int BSPLINE_SURFACE_3D = 30;

    /**
     * �Ȗʂ� Pure Bezier Surface �ł��邱�Ƃ���?�
     */
    static final int PURE_BEZIER_SURFACE_3D = 31;

    /**
     * �Ȗʂ� Rectangular Trimmed Surface �ł��邱�Ƃ���?�
     */
    static final int RECTANGULAR_TRIMMED_SURFACE_3D = 32;

    /**
     * �Ȗʂ� Curve Bounded Surface �ł��邱�Ƃ���?�
     */
    static final int CURVE_BOUNDED_SURFACE_3D = 33;

    /**
     * �Ȗʂ� Mesh �ł��邱�Ƃ���?�
     */
    static final int MESH_3D = 40;

    /**
     * �I�u�W�F�N�g��?\�z����?B
     */
    protected ParametricSurface3D() {
        super();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ���?ۃ?�\�b�h?B
     *
     * @param uParam U ���p���??[�^�l
     * @param vParam V ���p���??[�^�l
     * @return ?W�l
     */
    public abstract Point3D coordinates(double uParam, double vParam);

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ���?ۃ?�\�b�h?B
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
    public abstract Vector3D[] tangentVector(double uParam, double vParam);

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
        Vector3D[] tang;

        tang = tangentVector(uParam, vParam);
        return tang[0].crossProduct(tang[1]).unitized();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̎�ȗ�?���Ԃ�?B
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ��ȗ�?��
     */
    public SurfaceCurvature3D curvature(double uParam, double vParam) {
        SurfaceDerivative3D deriv;

        deriv = evaluation(uParam, vParam);
        return deriv.principalCurvature();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ���?ۃ?�\�b�h?B
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     */
    public abstract SurfaceDerivative3D
    evaluation(double uParam, double vParam);

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public abstract PointOnSurface3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException;

    /**
     * �^����ꂽ�_ P ���炱�̋Ȗʂւ̓��e�_�̓��?AP ��?ł�߂��_��Ԃ�?B
     * <p/>
     * ���e�_����?݂��Ȃ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param pnt ���e���̓_
     * @return ���e���̓_��?ł�߂����e�_
     * @see #projectFrom(Point3D)
     * @see #nearestProjectWithDistanceFrom(Point3D,double)
     */
    public PointOnSurface3D nearestProjectFrom(Point3D pnt) {
        PointOnSurface3D[] proj;
        try {
            proj = projectFrom(pnt);
        } catch (IndefiniteSolutionException e) {
            proj = new PointOnSurface3D[1];
            proj[0] = (PointOnSurface3D) e.suitable();
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
     * �^����ꂽ�_ P ���炱�̋Ȗʂւ̓��e�_�̓��?AP ����̋������w��̒l��?ł�߂��_��Ԃ�?B
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
    public PointOnSurface3D nearestProjectWithDistanceFrom(Point3D pnt, double distance) {
        PointOnSurface3D[] proj;
        try {
            proj = projectFrom(pnt);
        } catch (IndefiniteSolutionException e) {
            proj = new PointOnSurface3D[1];
            proj[0] = (PointOnSurface3D) e.suitable();
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
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ���?ۃ?�\�b�h?B
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
    public abstract Mesh3D
    toMesh(ParameterSection uPint, ParameterSection vPint,
           ToleranceForDistance tol);

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ���?ۃ?�\�b�h?B
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @return ���̋Ȗʂ̎w��̋�Ԃ�?Č�����L�? Bspline �Ȗ�
     */
    public abstract BsplineSurface3D
    toBsplineSurface(ParameterSection uPint,
                     ParameterSection vPint);

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂钊?ۃ?�\�b�h?B
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
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋ȖʂƑ��̋�?� (�~??��?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�~??��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(Conic3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋ȖʂƑ��̋�?� (�|�����C��) �̌�_��?�߂� (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Polyline3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�x�W�G��?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(PureBezierCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋ȖʂƑ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂钊?ۃ?�\�b�h (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    abstract IntersectionPoint3D[]
    intersect(BsplineCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋ȖʂƑ��̋�?� (�g������?�) �̌�_��?�߂� (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(TrimmedCurve3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?���?�) �̌�_��?�߂� (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(CompositeCurve3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂� (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(CompositeCurveSegment3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋ȖʂƂ̌�?��?�߂钊?ۃ?�\�b�h?B
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
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    public abstract SurfaceSurfaceInterference3D[]
    intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException;

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �Ƃ̌�?��?�߂钊?ۃ?�\�b�h (internal use) ?B
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
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    abstract SurfaceSurfaceInterference3D[]
    intersect(Plane3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �Ƃ̌�?��?�߂钊?ۃ?�\�b�h (internal use) ?B
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
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    abstract SurfaceSurfaceInterference3D[]
    intersect(SphericalSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~����) �Ƃ̌�?��?�߂钊?ۃ?�\�b�h (internal use) ?B
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
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    abstract SurfaceSurfaceInterference3D[]
    intersect(CylindricalSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~??��) �Ƃ̌�?��?�߂钊?ۃ?�\�b�h (internal use) ?B
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
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    abstract SurfaceSurfaceInterference3D[]
    intersect(ConicalSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException;

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�x�W�G�Ȗ�) �Ƃ̌�?��?�߂钊?ۃ?�\�b�h (internal use) ?B
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
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    abstract SurfaceSurfaceInterference3D[]
    intersect(PureBezierSurface3D mate, boolean doExchange);

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�a�X�v���C���Ȗ�) �Ƃ̌�?��?�߂钊?ۃ?�\�b�h (internal use) ?B
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
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    abstract SurfaceSurfaceInterference3D[]
    intersect(BsplineSurface3D mate, boolean doExchange);

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�I�t�Z�b�g�����Ȗʂ�
     * �^����ꂽ��?��ŋߎ����� Bspline �Ȗʂ�?�߂钊?ۃ?�\�b�h?B
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.FRONT/BACK)
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�`��Ԃ̃I�t�Z�b�g�Ȗʂ�ߎ����� Bspline �Ȗ�
     * @see WhichSide
     */
    public abstract BsplineSurface3D
    offsetByBsplineSurface(ParameterSection uSect,
                           ParameterSection vSect,
                           double magni,
                           int side,
                           ToleranceForDistance tol);

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃɂ�����t�B���b�g��?�߂�?B
     * <p/>
     * �t�B���b�g����?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param uSect1 ���̋Ȗʂ� U ���̃p���??[�^���
     * @param vSect1 ���̋Ȗʂ� V ���̃p���??[�^���
     * @param side1  ���̋Ȗʂ̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.FRONT�Ȃ�Ε\��?ARIGHT�Ȃ�Η���?ABOTH�Ȃ�Η���)
     * @param mate   ���̋Ȗ�
     * @param uSect2 ���̋Ȗʂ� U ���̃p���??[�^���
     * @param vSect2 ���̋Ȗʂ� V ���̃p���??[�^���
     * @param side2  ���̋Ȗʂ̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.FRONT�Ȃ�Ε\��?ARIGHT�Ȃ�Η���?ABOTH�Ȃ�Η���)
     * @param radius �t�B���b�g���a
     * @return �t�B���b�g�̔z��
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     * @see WhichSide
     */
    public FilletObject3D[]
    fillet(ParameterSection uSect1, ParameterSection vSect1, int side1,
           ParametricSurface3D mate,
           ParameterSection uSect2, ParameterSection vSect2, int side2,
           double radius)
            throws IndefiniteSolutionException {
        return FiltSrfSrf3D.fillet(this, uSect1, vSect1, side1,
                mate, uSect2, vSect2, side2, radius);
    }

    /**
     * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ���?ۃ?�\�b�h?B
     *
     * @param uParam U ���̃p���??[�^�l
     * @return �w��� U �p���??[�^�l�ł̓��p���??[�^��?�
     */
    public abstract ParametricCurve3D uIsoParametricCurve(double uParam)
            throws ReducedToPointException;

    /**
     * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ���?ۃ?�\�b�h?B
     *
     * @param vParam V ���̃p���??[�^�l
     * @return �w��� V �p���??[�^�l�ł̓��p���??[�^��?�
     */
    public abstract ParametricCurve3D vIsoParametricCurve(double vParam)
            throws ReducedToPointException;

    /**
     * ���̋Ȗʂ̎�����Ԃ�?B
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
     * ���̋Ȗʂ��R�������ۂ���Ԃ�?B
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
     * ���̋Ȗʂ̗v�f��ʂ�Ԃ���?ۃ?�\�b�h?B
     *
     * @return �v�f���
     * @see #PLANE_3D
     * @see #CYLINDRICAL_SURFACE_3D
     * @see #CONICAL_SURFACE_3D
     * @see #SPHERICAL_SURFACE_3D
     * @see #SURFACE_OF_LINEAR_EXTRUSION_3D
     * @see #SURFACE_OF_REVOLUTION_3D
     * @see #BSPLINE_SURFACE_3D
     * @see #PURE_BEZIER_SURFACE_3D
     * @see #RECTANGULAR_TRIMMED_SURFACE_3D
     * @see #CURVE_BOUNDED_SURFACE_3D
     * @see #MESH_3D
     */
    abstract int type();

    /**
     * �^����ꂽ��?�_��?A���̋Ȗʂł̃p���??[�^�l��?�߂�?B
     * <p/>
     * �^����ꂽ�_�����̋Ȗ�?��?�BĂ����̂Ƃ���?A
     * ���̓_�ɑΉ�����p���??[�^�l (u, v) ��?�߂�?B
     * </p>
     * <p/>
     * ���̃?�\�b�h�̓Ք?��?�͈ȉ��̒ʂ�?B
     * <ul>
     * <li> nearestProjectFrom(pnt) ��g�B�?A
     * �^����ꂽ�_ P ��?ł�߂����e�_ Q �𓾂�
     * <li> ���e�_����?݂��Ȃ����?A
     * �^����ꂽ�_�͂��̋Ȗʂ�?�BĂ��Ȃ���̂Ƃ���O�𓊂���
     * <li> P �� Q ����?�?ݒ肳��Ă��鉉�Z?�?���œ���̓_�Ƃ݂Ȃ��Ȃ����?A
     * �^����ꂽ�_�͂��̋Ȗʂ�?�BĂ��Ȃ���̂Ƃ���O�𓊂���
     * <li> Q �̃p���??[�^�l (u, v) ��Ԃ�
     * </ul>
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ�z��̗v�f?��� 2 �ł���?B
     * �z���?�?��̗v�f�� U ���̃p���??[�^�l?A
     * ��Ԗڂ̗v�f�� V ���̃p���??[�^�l��܂�?B
     * </p>
     *
     * @param pnt �Ȗ�?�̓_
     * @return �Ή�����p���??[�^�l�̔z�� (u, v)
     * @throws InvalidArgumentValueException �^����ꂽ�_�����̋Ȗʂ�?�BĂ��Ȃ�
     * @see #nearestProjectFrom(Point3D)
     * @see ConditionOfOperation
     */
    public double[] pointToParameter(Point3D pnt) {
        PointOnSurface3D proj = nearestProjectFrom(pnt);
        if (proj == null)
            throw new InvalidArgumentValueException();

        if (pnt.identical(proj))
            return proj.parameters();
        else
            throw new InvalidArgumentValueException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (��?͋Ȗ�) �̌�_��?W�l��?A��_�I�u�W�F�N�g�ɕϊ�����?B
     * <p/>
     * ���̃?�\�b�h�̓Ք�ł�?A
     * this �� mate �̂��ꂼ��ɂ��� pointToParameter(Point3D) ��Ă�?o��?A
     * �^����ꂽ�_�̃p���??[�^�l�𓾂Ă���?B
     * �Ȃ�?ApointToParameter(Point3D) ��
     * InvalidArgumentValueException �̗�O��?������邱�Ƃ����邪?A
     * ���̃?�\�b�h�̓Ք�ł͂���� catch ���Ă��Ȃ�?B
     * </p>
     *
     * @param point      ��_��\��?W�l
     * @param mate       ���̋Ȗ� (��?͋Ȗ�)
     * @param doExchange ��_�̊i�[?���귂��邩�ǂ���
     * @return ��_�I�u�W�F�N�g
     * @throws InvalidArgumentValueException �^����ꂽ�_���ǂ��炩�̋Ȗʂ�?�BĂ��Ȃ�
     * @see #pointToParameter(Point3D)
     */
    IntersectionPoint3D pointToIntersectionPoint(Point3D point,
                                                 ElementarySurface3D mate,
                                                 boolean doExchange) {
        double[] thisParams = this.pointToParameter(point);
        double[] mateParams = mate.pointToParameter(point);
        if (!doExchange)
            return new IntersectionPoint3D(point,
                    this, thisParams[0], thisParams[1],
                    mate, mateParams[0], mateParams[1],
                    doCheckDebug);
        else
            return new IntersectionPoint3D(point,
                    mate, mateParams[0], mateParams[1],
                    this, thisParams[0], thisParams[1],
                    doCheckDebug);
    }

    /**
     * ���̋Ȗʂ����I�� (����) �Ȗʂł����?A
     * �^����ꂽ�p���??[�^���?�̓_����?�����?B
     * <p/>
     * ���̋Ȗʂ� U/V �����Ɏ��I�łȂ����?A
     * ���̃?�\�b�h�͉��µ�Ȃ�?B
     * </p>
     * <p/>
     * pnts �ɗ^����ꂽ�Q�����̓_���?A
     * ���̎��I�ȋȖʂ̃p���??[�^���?�̃|�����C���ƌ��Ȃ�?A
     * ���̃|�����C�����Ȗʂ̎��̌q���ڂ̓_��܂�ł���Ƃ���?A
     * �^����ꂽ�|�����C�����̂�̂��\����?����?A
     * �|�����C�������̎��̌q���ڂ�ׂ���?L�тĂ����?l������
     * �|�����C���̒������Z���Ȃ�?�?���?A
     * ���̎��̌q���ڂ�ׂ��悤�Ƀ|�����C���̊e�_�̒l���?X����?B
     * </p>
     * <p/>
     * pnts ��̊e�v�f�͂��̃?�\�b�h�̓Ք�ŕ�?X����邩�µ��Ȃ�?B
     * </p>
     *
     * @param pnts �Q�����̓_�̔z��
     */
    void confirmConnectionOfPointsOnSurface(Point2D[] pnts) {
        int uip = pnts.length;
        double width;
        double val_i, val_j;
        double gap0, gapm, gapp;
        int i, j;

        if (isUFinite() && isUPeriodic()) {
            width = uParameterDomain().section().absIncrease();
            for (i = 0, j = 1; j < uip; i++, j++) {
                val_i = pnts[i].x();
                val_j = pnts[j].x();
                gap0 = Math.abs(val_j - val_i);
                gapm = Math.abs(val_j - width - val_i);
                gapp = Math.abs(val_j + width - val_i);
                if (gapm < gapp) {
                    if (gapm < gap0)
                        pnts[j] = new CartesianPoint2D(pnts[j].x() - width, pnts[j].y());
                } else {
                    if (gapp < gap0)
                        pnts[j] = new CartesianPoint2D(pnts[j].x() + width, pnts[j].y());
                }
            }
        }

        if (isVFinite() && isVPeriodic()) {
            width = vParameterDomain().section().absIncrease();
            for (i = 0, j = 1; j < uip; i++, j++) {
                val_i = pnts[i].y();
                val_j = pnts[j].y();
                gap0 = Math.abs(val_j - val_i);
                gapm = Math.abs(val_j - width - val_i);
                gapp = Math.abs(val_j + width - val_i);
                if (gapm < gapp) {
                    if (gapm < gap0)
                        pnts[j] = new CartesianPoint2D(pnts[j].x(), pnts[j].y() - width);
                } else {
                    if (gapp < gap0)
                        pnts[j] = new CartesianPoint2D(pnts[j].x(), pnts[j].y() + width);
                }
            }
        }
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �_�Q��Ԃ���?ۃ?�\�b�h?B
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
    public abstract Vector toNonStructuredPoints(ParameterSection uParameterSection,
                                                 ParameterSection vParameterSection,
                                                 double tolerance,
                                                 double[] scalingFactor);

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
    protected abstract ParametricSurface3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries);

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
    public synchronized ParametricSurface3D
    transformBy(boolean reverseTransform,
                CartesianTransformationOperator3D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        if (transformedGeometries == null)
            return this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        ParametricSurface3D transformed = (ParametricSurface3D) transformedGeometries.get(this);
        if (transformed == null) {
            transformed = this.doTransformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);
            transformedGeometries.put(this, transformed);
        }
        return transformed;
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
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̊􉽗v�f
     */
    public synchronized ParametricSurface3D
    transformBy(CartesianTransformationOperator3D transformationOperator,
                java.util.Hashtable transformedGeometries) {
        return transformBy(false, transformationOperator, transformedGeometries);
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�􉽓I�ϊ����Z�q�ŋt�ϊ�����?B
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
    public synchronized ParametricSurface3D
    reverseTransformBy(CartesianTransformationOperator3D transformationOperator,
                       java.util.Hashtable transformedGeometries) {
        return transformBy(true, transformationOperator, transformedGeometries);
    }
}

