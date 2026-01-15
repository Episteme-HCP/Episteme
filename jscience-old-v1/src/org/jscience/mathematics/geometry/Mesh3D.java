/*
 * �R���� : �i�q?�_�Ԃ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Mesh3D.java,v 1.3 2007-10-21 21:08:15 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : �i�q?�_�Ԃ�\���N���X?B
 * <p/>
 * ���̃N���X�͈ʑ��I�Ɋi�q?��?\����?�_�Q���Ԃ���Ȗʂ�\��?B
 * </p>
 * <p/>
 * �?�b�V����?A�i�q�_�̂Q�����z�� points ��
 * U/V �e?X�̕��ɕ����`���ł��邩�ۂ���\���t���O uClosed/vClosed
 * �Œ�`�����?B
 * </p>
 * <p/>
 * points ��?AU ���̃C���f�b�N�X��?�?AV ���̃C���f�b�N�X����?B
 * �܂�?AU ���� i �Ԗ�?AV ���� j �Ԗڂ̓_�� points[i][j] �Ɋi�[�����?B
 * </p>
 * <p/>
 * �?�b�V���� U ���̃p���??[�^��`���?A
 * ��?ڂ���i�q�_�̊Ԃ̃p���??[�^��Ԃ̑傫����?�� 1 �Ƃ���?A
 * �ȖʑS��ł� [0, N] �ƂȂ�?B
 * ������ N ��?A
 * �?�b�V���� U ���ɊJ�����`���ł���� (�i�q�_�� U ����?� - 1)?A
 * �����`���ł���� (�i�q�_�� U ����?� - 1) �ɂȂ�?B
 * </p>
 * <p/>
 * V ���̃p���??[�^��`��ɂ��Ă�?A���l�ł���?B
 * </p>
 * <p/>
 * (u, v) ��p���??[�^�Ƃ��镽�� P(u, v) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(u, v) =
 * 		(1 - lv) * ((1 - lu) * points[i][j]     + lu * points[i + 1][j]) +
 * 		     lv  * ((1 - lu) * points[i][j + 1] + lu * points[i + 1][j + 1])
 * </pre>
 * ������ i �� u ��z���Ȃ�?ő��?�?�?A j �� v ��z���Ȃ�?ő��?�?�?B
 * ����� lu = (u - i), lv = (v - j) �ł���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:15 $
 */

public class Mesh3D extends BoundedSurface3D {
    /**
     * �i�q?�_�Ԃ�\���Q�����z��?B
     * <p/>
     * U ���̃C���f�b�N�X��?�?AV ���̃C���f�b�N�X����?B
     * �܂�?AU ���� i �Ԗ�?AV ���� j �Ԗڂ̓_�� points[i][j] �Ɋi�[�����?B
     * </p>
     *
     * @serial
     */
    private Point3D[][] points;

    /**
     * U ���ɕ����`�����ۂ���\���t���O?B
     *
     * @serial
     */
    private boolean uClosed;

    /**
     * V ���ɕ����`�����ۂ���\���t���O?B
     *
     * @serial
     */
    private boolean vClosed;

    /**
     * �i�q?�_�Ԃ̃t�B?[���h�ɒl��?ݒ肷��?B
     * <p/>
     * points �� U ���̃C���f�b�N�X��?�?AV ���̃C���f�b�N�X����?B
     * �܂�?AU ���� i �Ԗ�?AV ���� j �Ԗڂ̓_�� points[i][j] �Ɋi�[����Ă����̂Ƃ���?B
     * </p>
     * <p/>
     * U ���̓_��?��� n �Ƃ���?A
     * points[i] (i = 0, ..., (n - 1)) �̗v�f?����������Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * U ���̓_��?��� n?AV ���̓_��?��� m �Ƃ���?A
     * points[i][j] (i = 0, ..., (n - 1), j = 0, ..., (m - 1)) �̒l�� null ������?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param points �i�q?�_�Ԃ̔z��
     * @see InvalidArgumentValueException
     */
    private void setPoints(Point3D[][] points) {
        int uUip = points.length;
        int vUip = points[0].length;
        int i, j;

        this.points = new Point3D[uUip][vUip];

        for (i = 0; i < uUip; i++) {
            if (points[i].length != vUip)
                throw new InvalidArgumentValueException();

            for (j = 0; j < vUip; j++) {
                if (points[i][j] == null)
                    throw new NullArgumentException();
                this.points[i][j] = points[i][j];
            }
        }
    }

    /**
     * �i�q�_?AU/V ���̊J��?���^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * points �� U ���̃C���f�b�N�X��?�?AV ���̃C���f�b�N�X����?B
     * �܂�?AU ���� i �Ԗ�?AV ���� j �Ԗڂ̓_�� points[i][j] �Ɋi�[����Ă����̂Ƃ���?B
     * </p>
     * <p/>
     * U ���̓_��?��� n �Ƃ���?A
     * points[i] (i = 0, ..., (n - 1)) �̗v�f?����������Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * U ���̓_��?��� n?AV ���̓_��?��� m �Ƃ���?A
     * points[i][j] (i = 0, ..., (n - 1), j = 0, ..., (m - 1)) �̒l�� null ������?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param points  �i�q?�_�Ԃ̔z��
     * @param uClosed U ���ɕ����`�����ۂ���\���t���O
     * @param vClosed V ���ɕ����`�����ۂ���\���t���O
     * @see InvalidArgumentValueException
     */
    public Mesh3D(Point3D[][] points, boolean uClosed, boolean vClosed) {
        super();
        this.uClosed = uClosed;
        this.vClosed = vClosed;
        setPoints(points);
    }

    /**
     * �i�q�_��^���� U/V ���Ƃ�ɊJ�����`���ŃI�u�W�F�N�g��?\�z����?B
     * <p/>
     * points �� U ���̃C���f�b�N�X��?�?AV ���̃C���f�b�N�X����?B
     * �܂�?AU ���� i �Ԗ�?AV ���� j �Ԗڂ̓_�� points[i][j] �Ɋi�[����Ă����̂Ƃ���?B
     * </p>
     * <p/>
     * U ���̓_��?��� n �Ƃ���?A
     * points[i] (i = 0, ..., (n - 1)) �̗v�f?����������Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * U ���̓_��?��� n?AV ���̓_��?��� m �Ƃ���?A
     * points[i][j] (i = 0, ..., (n - 1), j = 0, ..., (m - 1)) �̒l�� null ������?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param points �i�q?�_�Ԃ̔z��
     * @see InvalidArgumentValueException
     */
    public Mesh3D(Point3D[][] points) {
        super();
        this.uClosed = false;
        this.vClosed = false;
        setPoints(points);
    }

    /**
     * �i�q�_��^���� U/V ���Ƃ�ɊJ�����`���ŃI�u�W�F�N�g��?\�z����?B
     * <p/>
     * points �� U ���̃C���f�b�N�X��?�?AV ���̃C���f�b�N�X����?B
     * �܂�?AU ���� i �Ԗ�?AV ���� j �Ԗڂ̓_�� points[i][j] �Ɋi�[����Ă����̂Ƃ���?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A�ȉ��̃`�F�b�N��?s�Ȃ�?B
     * </p>
     * <blockquote>
     * <p/>
     * U ���̓_��?��� n �Ƃ���?A
     * points[i] (i = 0, ..., (n - 1)) �̗v�f?����������Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * U ���̓_��?��� n?AV ���̓_��?��� m �Ƃ���?A
     * points[i][j] (i = 0, ..., (n - 1), j = 0, ..., (m - 1)) �̒l�� null ������?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * </blockquote>
     * <p/>
     * doCheck �� false ��?�?��ɂ�?A
     * �^����ꂽ points �⻂̂܂܃C���X�^���X�̃t�B?[���h�ɑ���?B
     * </p>
     *
     * @param points  �i�q?�_�Ԃ̔z��
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     */
    Mesh3D(Point3D[][] points, boolean doCheck) {
        super();
        this.uClosed = false;
        this.vClosed = false;
        if (doCheck)
            setPoints(points);
        else
            this.points = points;
    }

    /**
     * ����L�ȋȖʂ̑S�̂�?A�^����ꂽ���e��?��ŋߎ�����I�u�W�F�N�g��?\�z����?B
     *
     * @param surface �L�ȋȖ�
     * @param tol     �����̋��e��?�
     * @see BoundedSurface3D#toMesh(ToleranceForDistance)
     */
    public Mesh3D(BoundedSurface3D surface,
                  ToleranceForDistance tol) {
        super();
        Mesh3D mesh = surface.toMesh(tol);
        this.uClosed = mesh.uClosed;
        this.vClosed = mesh.vClosed;
        this.points = mesh.points;
    }

    /**
     * ����Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * �^����ꂽ���e��?��ŋߎ�����I�u�W�F�N�g��?\�z����?B
     *
     * @param surface �Ȗ�
     * @param uPint   �ߎ����� U ���̃p���??[�^���
     * @param vPint   �ߎ����� V ���̃p���??[�^���
     * @param tol     �����̋��e��?�
     * @see ParametricSurface3D#toMesh(ParameterSection,ParameterSection,ToleranceForDistance)
     */
    public Mesh3D(ParametricSurface3D surface,
                  ParameterSection uPint,
                  ParameterSection vPint,
                  ToleranceForDistance tol) {
        super();
        Mesh3D mesh = surface.toMesh(uPint, vPint, tol);
        this.uClosed = mesh.uClosed;
        this.vClosed = mesh.vClosed;
        this.points = mesh.points;
    }

    /**
     * ���̃?�b�V���̊i�q?�_�Ԃ̔z���Ԃ�?B
     *
     * @return �i�q?�_�Ԃ̔z��
     */
    public Point3D[][] points() {
        Point3D[][] pnts = new Point3D[uNPoints()][vNPoints()];
        int i, j;

        for (i = 0; i < uNPoints(); i++)
            for (j = 0; j < vNPoints(); j++)
                pnts[i][j] = pointAt(i, j);
        return pnts;
    }

    /**
     * ���̃?�b�V���� (i, j) �Ԗڂ̊i�q�_��Ԃ�?B
     * <p/>
     * U ���� i �Ԗ�?AV ���� j �Ԗڂ̊i�q�_��Ԃ�?B
     * </p>
     *
     * @return (i,j) �Ԗڂ̊i�q�_
     */
    public Point3D pointAt(int i, int j) {
        if (uClosed() && i == uNPoints())
            i = 0;
        if (vClosed() && j == vNPoints())
            j = 0;

        return points[i][j];
    }

    /**
     * ���̃?�b�V���� U ���ɕ����`�����ۂ���Ԃ�?B
     *
     * @return �����`���Ȃ�� true?A����Ȃ��� false
     */
    public boolean uClosed() {
        return this.uClosed;
    }

    /**
     * ���̃?�b�V���� V ���ɕ����`�����ۂ���Ԃ�?B
     *
     * @return �����`���Ȃ�� true?A����Ȃ��� false
     */
    public boolean vClosed() {
        return this.vClosed;
    }

    /**
     * ���̃?�b�V���� U ���̊i�q��?���Ԃ�?B
     *
     * @return U ���̊i�q��?�
     */
    public int uNPoints() {
        return points.length;
    }

    /**
     * ���̃?�b�V���� V ���̊i�q��?���Ԃ�?B
     *
     * @return V ���̊i�q��?�
     */
    public int vNPoints() {
        return points[0].length;
    }

    /**
     * ���̃?�b�V���� U ���̃Z�O�?���g��?���Ԃ�?B
     *
     * @return U ���̃Z�O�?���g��?�
     */
    public int uNSegments() {
        if (uClosed())
            return uNPoints();

        return uNPoints() - 1;
    }

    /**
     * ���̃?�b�V���� V ���̃Z�O�?���g��?���Ԃ�?B
     *
     * @return V ���̃Z�O�?���g��?�
     */
    public int vNSegments() {
        if (vClosed())
            return vNPoints();

        return vNPoints() - 1;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?W�l
     * @see UnsupportedOperationException
     */
    public Point3D coordinates(double uParam, double vParam) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?ڃx�N�g��
     * @see UnsupportedOperationException
     */
    public Vector3D[] tangentVector(double uParam, double vParam) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ����?�
     * @see UnsupportedOperationException
     */
    public SurfaceDerivative3D evaluation(double uParam, double vParam) {
        throw new UnsupportedOperationException();
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     * @see UnsupportedOperationException
     */
    public PointOnSurface3D[] projectFrom(Point3D point) {
        throw new UnsupportedOperationException();
    }

    /**
     * �����`������^����ꂽ��?��Ŋi�q�_�Q�ɕ�������
     * �i�q�_�Q��?\?�����_��PointOnSurface3D�ł��邱�Ƃ��҂ł���
     * ���̋@�\�̓T�|?[�g����Ă��Ȃ�
     *
     * @param uPint U���̃p���??[�^���
     * @param vPint V���̃p���??[�^���
     * @param tol   ��?�
     * @return �i�q�_
     * @see ParameterSection
     * @see ToleranceForDistance
     * @see Mesh3D
     * @see PointOnSurface3D
     */
    public Mesh3D
    toMesh(ParameterSection uPint, ParameterSection vPint,
           ToleranceForDistance tol) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ���?ۃ?�\�b�h?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param tol �����̋��e��?�
     * @return ���̗L�ȖʑS�̂𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see UnsupportedOperationException
     */
    public Mesh3D toMesh(ToleranceForDistance tol) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ���?ۃ?�\�b�h?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @return ���̋Ȗʂ̎w��̋�Ԃ�?Č�����L�? Bspline �Ȗ�
     * @see UnsupportedOperationException
     */
    public BsplineSurface3D
    toBsplineSurface(ParameterSection uPint,
                     ParameterSection vPint) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�~??��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�~??��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Conic3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋ȖʂƂ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��?� (�܂��͌�_) �̔z��
     * @see UnsupportedOperationException
     */
    public SurfaceSurfaceInterference3D[] intersect(ParametricSurface3D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �Ƃ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @see UnsupportedOperationException
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �Ƃ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @see UnsupportedOperationException
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~����) �Ƃ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @see UnsupportedOperationException
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~??��) �Ƃ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @see UnsupportedOperationException
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�x�W�G�Ȗ�) �Ƃ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @see UnsupportedOperationException
     */
    SurfaceSurfaceInterference3D[] intersect(PureBezierSurface3D mate,
                                             boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�a�X�v���C���Ȗ�) �Ƃ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?� (�܂��͌�_) �̔z��
     * @see UnsupportedOperationException
     */
    SurfaceSurfaceInterference3D[] intersect(BsplineSurface3D mate,
                                             boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�I�t�Z�b�g�����Ȗʂ�
     * �^����ꂽ��?��ŋߎ����� Bspline �Ȗʂ�?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.FRONT/BACK)
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�`��Ԃ̃I�t�Z�b�g�Ȗʂ�ߎ����� Bspline �Ȗ�
     * @see WhichSide
     * @see UnsupportedOperationException
     */
    public BsplineSurface3D
    offsetByBsplineSurface(ParameterSection uPint,
                           ParameterSection vPint,
                           double magni,
                           int side,
                           ToleranceForDistance tol) {
        throw new UnsupportedOperationException();
    }

    /*
    * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    * <p>
    * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
    * UnsupportedOperationException �̗�O��?�����?B
    * </p>
    *
    * @param parameter	U ���̃p���??[�^�l
    * @return	�w��� U �p���??[�^�l�ł̓��p���??[�^��?�
    * @see	UnsupportedOperationException
    */
    public ParametricCurve3D uIsoParametricCurve(double parameter) {
        throw new UnsupportedOperationException();
    }

    /*
    * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    * <p>
    * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
    * UnsupportedOperationException �̗�O��?�����?B
    * </p>
    *
    * @param parameter	V ���̃p���??[�^�l
    * @return	�w��� V �p���??[�^�l�ł̓��p���??[�^��?�
    * @see	UnsupportedOperationException
    */
    public ParametricCurve3D vIsoParametricCurve(double parameter) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̃?�b�V����O�p�`��?W?��ɕϊ�����?B
     *
     * @return �O�p�`��?W?�
     * @see SetOfTriangles3D#SetOfTriangles3D(Mesh3D)
     */
    public SetOfTriangles3D toSetOfTriangles() {
        return new SetOfTriangles3D(this);
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     *
     * @return ���̋Ȗʂ� U ���̃p���??[�^��`��
     */
    ParameterDomain getUParameterDomain() {
        double n = uClosed ? uNPoints() : uNPoints() - 1;

        return new ParameterDomain(uClosed, 0, n);
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
     *
     * @return ���̋Ȗʂ� V ���̃p���??[�^��`��
     */
    ParameterDomain getVParameterDomain() {
        double n = vClosed ? vNPoints() : vNPoints() - 1;

        return new ParameterDomain(vClosed, 0, n);
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#MESH_3D ParametricSurface3D.MESH_3D}
     */
    int type() {
        return MESH_3D;
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
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param uParameterSection U ���̃p���??[�^���
     * @param vParameterSection V ���̃p���??[�^���
     * @param tolerance         �����̋��e��?�
     * @param scalingFactor     �_�Q��O�p�`��������?ۂɗL�p�Ǝv���� U/V ��?k�ڔ{��
     * @return �_�Q��܂� Vector
     * @see PointOnSurface3D
     * @see UnsupportedOperationException
     */
    public Vector toNonStructuredPoints(ParameterSection uParameterSection,
                                        ParameterSection vParameterSection,
                                        double tolerance,
                                        double[] scalingFactor) {
        throw new UnsupportedOperationException();    // toNonStructuredPoints
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ?�� true
     */
    public boolean isFreeform() {
        return true;
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
        Point3D[][] tPoints = new Point3D[this.uNPoints()][];
        for (int i = 0; i < this.uNPoints(); i++)
            tPoints[i] = Point3D.transform(this.points[i],
                    reverseTransform,
                    transformationOperator,
                    transformedGeometries);
        return new Mesh3D(tPoints, this.uClosed, this.vClosed);
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
        int i, j;

        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tpoints");
        for (i = 0; i < uNPoints(); i++) {
            for (j = 0; j < vNPoints(); j++) {
                pointAt(i, j).output(writer, indent + 2);
            }
        }
        writer.println(indent_tab + "\tuClosed\t" + uClosed);
        writer.println(indent_tab + "\tvClosed\t" + vClosed);
        writer.println(indent_tab + "End");
    }
}
