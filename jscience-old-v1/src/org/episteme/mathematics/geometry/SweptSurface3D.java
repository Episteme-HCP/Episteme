/*
 * �R���� : �X�C?[�v�ʂ�\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SweptSurface3D.java,v 1.2 2006/03/01 21:16:11 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �R���� : �X�C?[�v�ʂ�\����?ۃN���X?B
 * <p/>
 * �X�C?[�v�ʂƂ�?A
 * ����R������?��ʂ̂R������?�ɉ��Bđ|�� (�X�C?[�v) �����O?Ղ�ȖʂƂ݂Ȃ���̂ł���?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �X�C?[�v������ׂ��R������?� sweptCurve
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:11 $
 */

public abstract class SweptSurface3D extends ParametricSurface3D {
    /**
     * �X�C?[�v������ׂ���?�?B
     *
     * @serial
     */
    private ParametricCurve3D sweptCurve;

    /**
     * �X�C?[�v�������?��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * sweptCurve �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param sweptCurve �X�C?[�v�������?�
     * @see InvalidArgumentValueException
     */
    protected SweptSurface3D(ParametricCurve3D sweptCurve) {
        super();
        setSweptCurve(sweptCurve);
    }

    /**
     * �X�C?[�v�������?��t�B?[���h��?ݒ肷��?B
     * <p/>
     * sweptCurve �� null ��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param sweptCurve �X�C?[�v�������?�
     * @see InvalidArgumentValueException
     */
    private void setSweptCurve(ParametricCurve3D sweptCurve) {
        if (sweptCurve == null) {
            throw new InvalidArgumentValueException();
        }
        this.sweptCurve = sweptCurve;
    }

    /**
     * ���̋Ȗʂ�?A�X�C?[�v�������?��Ԃ�?B
     *
     * @return �X�C?[�v�������?�
     */
    public ParametricCurve3D sweptCurve() {
        return sweptCurve;
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���̋Ȗʂ͈�ʂɖ��ȋȖʂƂȂ邱�Ƃ�����?A
     * ����?�?�?A���I�ɂ͖��Ȏ��R�ȖʂƓ��l�Ȉ����ƂȂ�?B
     * ������ʂɉ⭂��Ƃ͓������?A���܂̂Ƃ���?A?��
     * ImproperOperationException �̗�O��?�����?B
     * </p>
     * <p/>
     * ���̋ȖʂƂ̌�_��?�߂���?�?���?A
     * ���̋Ȗʂ��ȖʂƂ����`�L�Ȗʂµ����
     * ��?�E�Ȗʂ�?�?���?A���̋ȖʂƂ̌�_�Ƃ��ċ?�߂�ꂽ��?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @see ImproperOperationException
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�~??��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�~??��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Conic3D mate, boolean doExchange) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
        throw new ImproperOperationException();
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
     * <p/>
     * ���̋Ȗʂ͈�ʂɖ��ȋȖʂƂȂ邱�Ƃ�����?A
     * ����?�?�?A���I�ɂ͖��Ȏ��R�ȖʂƓ��l�Ȉ����ƂȂ�?B
     * ������ʂɉ⭂��Ƃ͓��?A
     * �܂���ֳ�Ȏ��R��?�(�|�����C��)�Ƃ��Ē�`���Ȃ���΂Ȃ�Ȃ�
     * �\?������邽��?A���܂̂Ƃ���?A?��
     * ImproperOperationException �̗�O��?�����?B
     * </p>
     * <p/>
     * ���̋ȖʂƂ̌�?��?�߂���?�?���?A
     * ���̋Ȗʂ��ȖʂƂ����`�L�Ȗʂµ����
     * ��?�E�Ȗʂ�?�?���?A���̋ȖʂƂ̌�?�Ƃ��ċ?�߂�ꂽ��?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��?� (�܂��͌�_) �̔z��
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     * @see ImproperOperationException
     */
    public SurfaceSurfaceInterference3D[] intersect(ParametricSurface3D mate) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��ԂƑ��̋Ȗʂ̌�?��?�߂�?B
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
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param mate  ���̋Ȗ�
     * @return ��?� (�܂��͌�_) �̔z��
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     * @see #changeTargetOfIntersection(IntersectionPoint3D)
     * @see #changeTargetOfIntersection(IntersectionCurve3D)
     */
    SurfaceSurfaceInterference3D[] intersect(ParameterSection uPint,
                                             ParameterSection vPint,
                                             ParametricSurface3D mate) {
        SurfaceSurfaceInterference3D[] results =
                this.toBsplineSurface(uPint, vPint).intersect(mate);

        for (int i = 0; i < results.length; i++) {
            if (results[i].isIntersectionPoint() == true) {
                results[i] = this.changeTargetOfIntersection(results[i].toIntersectionPoint());
            } else {
                results[i] = this.changeTargetOfIntersection(results[i].toIntersectionCurve());
            }
        }

        return results;
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate, boolean doExchange) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate, boolean doExchange) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~??��) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate, boolean doExchange) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�x�W�G�Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(PureBezierSurface3D mate,
                                             boolean doExchange) {
        throw new ImproperOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(BsplineSurface3D mate,
                                             boolean doExchange) {
        throw new ImproperOperationException();
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return �X�C?[�v�������?�R�`?�ł���� true?A�����łȂ���� false
     */
    public boolean isFreeform() {
        return this.sweptCurve.isFreeform();
    }

    /**
     * �^����ꂽ��_��?A���̋Ȗʂɑ΂����_�ɕϊ�����?B
     * <p/>
     * ���̃?�\�b�h���Ԃ���_��?A���̑�?ۂ����łȂ�
     * �p���??[�^�l�±�̋Ȗʂɑ΂����̂ɕϊ�����Ă���?B
     * </p>
     *
     * @param ints ��_
     * @return ��?ۂⱂ̋Ȗʂɕ�?X������_
     * @see #intersect(ParameterSection,ParameterSection,ParametricSurface3D)
     * @see #changeTargetOfIntersection(IntersectionCurve3D)
     */
    private IntersectionPoint3D
    changeTargetOfIntersection(IntersectionPoint3D ints) {
        double[] ownParams = this.pointToParameter(ints.coordinates());
        PointOnGeometry3D pog1 = new PointOnSurface3D(this,
                ownParams[0],
                ownParams[1],
                doCheckDebug);
        return new IntersectionPoint3D(ints.coordinates(),
                pog1, ints.pointOnGeometry2(), doCheckDebug);
    }

    /**
     * �^����ꂽ��?��?A���̋Ȗʂɑ΂����?�ɕϊ�����?B
     * <p/>
     * �^�������?��?A���̂R�����\�����|�����C���ł����̂Ƒz�肵�Ă���?B
     * �����łȂ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * ���̃?�\�b�h���Ԃ���?��?A���̑�?ۂ����łȂ�
     * �p���??[�^�l�±�̋Ȗʂɑ΂����̂ɕϊ�����Ă���?B
     * </p>
     *
     * @param ints ��?�
     * @return ��?ۂⱂ̋Ȗʂɕ�?X������?�
     * @see InvalidArgumentValueException
     * @see #intersect(ParameterSection,ParameterSection,ParametricSurface3D)
     * @see #changeTargetOfIntersection(IntersectionPoint3D)
     */
    private IntersectionCurve3D
    changeTargetOfIntersection(IntersectionCurve3D ints) {
        if (ints.curve3d().type() != ParametricCurve3D.POLYLINE_3D)
            throw new InvalidArgumentValueException();

        Polyline3D polyline3d = (Polyline3D) ints.curve3d();
        Point2D[] ownParams = new Point2D[polyline3d.nPoints()];
        for (int i = 0; i < polyline3d.nPoints(); i++)
            ownParams[i] = Point2D.of(this.pointToParameter(polyline3d.pointAt(i)));
        Polyline2D curve2d1 = new Polyline2D(ownParams, polyline3d.closed());

        return new IntersectionCurve3D(ints.curve3d(),
                this, curve2d1,
                ints.basisSurface2(), ints.curve2d2(),
                ints.masterRepresentation());
    }
}
