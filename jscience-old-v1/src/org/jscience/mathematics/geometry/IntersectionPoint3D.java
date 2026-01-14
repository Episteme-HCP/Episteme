/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �R���� : ��􉽗v�f�̌�_��\���N���X
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��_�̎��?�ł�?W�l���_ coordinates?A
 * ���̊􉽗v�f?�ł̈ʒu���_ pointOnGeometry1?A
 * ����̊􉽗v�f?�ł̈ʒu���_ pointOnGeometry2
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:00 $
 * @see OverlapCurve3D
 */

public class IntersectionPoint3D extends Point3D
        implements CurveCurveInterference3D, SurfaceSurfaceInterference3D {
    /**
     * ���?�ł�?W�l?B
     *
     * @serial
     */
    private final Point3D coordinates;

    /**
     * ���̊􉽗v�f (�􉽗v�f1) ?�ł̈ʒu?B
     *
     * @serial
     */
    private final PointOnGeometry3D pointOnGeometry1;

    /**
     * ����̊􉽗v�f (�􉽗v�f2) ?�ł̈ʒu?B
     *
     * @serial
     */
    private final PointOnGeometry3D pointOnGeometry2;

    /**
     * ���̃C���X�^���X�̊e�t�B?[���h�̒l��
     * �݂���?�?��̂Ƃꂽ��̂ł��邩�ǂ�����`�F�b�N����?B
     * <p/>
     * ?�?�?����Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @see InvalidArgumentValueException
     */
    private void checkPoints() {
        if (!this.coordinates.identical(this.pointOnGeometry1) ||
                !this.coordinates.identical(this.pointOnGeometry2) ||
                !this.pointOnGeometry1.identical(this.pointOnGeometry2))
            throw new InvalidArgumentValueException();
    }

    /**
     * ��􉽗v�f�̌�_��?W�l�Ɗe�􉽗v�f��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param coordinates      ���?�ł�?W�l
     * @param pointOnGeometry1 ���̊􉽗v�f (�􉽗v�f1) ?�ł̈ʒu
     * @param pointOnGeometry2 ����̊􉽗v�f (�􉽗v�f2) ?�ł̈ʒu
     * @param doCheck          ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(Point3D coordinates,
                        PointOnGeometry3D pointOnGeometry1,
                        PointOnGeometry3D pointOnGeometry2,
                        boolean doCheck) {
        super();
        this.coordinates = coordinates;
        this.pointOnGeometry1 = pointOnGeometry1;
        this.pointOnGeometry2 = pointOnGeometry2;
        if (doCheck)
            checkPoints();
    }

    /**
     * ��􉽗v�f�̌�_�̊e�􉽗v�f��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��_�̎��?�ł�?W�l��?A���ꂼ��̊􉽗v�f?�̓_�̒��_�ƂȂ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param pointOnGeometry1 ���̊􉽗v�f (�􉽗v�f1) ?�ł̈ʒu
     * @param pointOnGeometry2 ����̊􉽗v�f (�􉽗v�f2) ?�ł̈ʒu
     * @param doCheck          ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(PointOnGeometry3D pointOnGeometry1,
                        PointOnGeometry3D pointOnGeometry2,
                        boolean doCheck) {
        super();
        this.pointOnGeometry1 = pointOnGeometry1;
        this.pointOnGeometry2 = pointOnGeometry2;
        this.coordinates = this.pointOnGeometry1.linearInterpolate(this.pointOnGeometry2, 0.5);
        if (doCheck)
            checkPoints();
    }

    /**
     * ���?�̌�_��?W�l�Ɗe��?��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param coordinates ���?�ł�?W�l
     * @param curve1      ���̋�?� (��?�1?A �􉽗v�f1)
     * @param param1      ��?�1 ?�̃p���??[�^�l
     * @param curve2      ����̋�?� (��?�2?A �􉽗v�f2)
     * @param param2      ��?�2 ?�̃p���??[�^�l
     * @param doCheck     ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(Point3D coordinates,
                        ParametricCurve3D curve1, double param1,
                        ParametricCurve3D curve2, double param2,
                        boolean doCheck) {
        super();
        this.coordinates = coordinates;
        this.pointOnGeometry1 = new PointOnCurve3D(curve1, param1, doCheckDebug);
        this.pointOnGeometry2 = new PointOnCurve3D(curve2, param2, doCheckDebug);
        if (doCheck)
            checkPoints();
    }

    /**
     * ���?�̌�_�̊e��?��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��_�̎��?�ł�?W�l��?A���ꂼ��̋�?�?�̓_�̒��_�ƂȂ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param curve1  ���̋�?� (��?�1?A �􉽗v�f1)
     * @param param1  ��?�1 ?�̃p���??[�^�l
     * @param curve2  ����̋�?� (��?�2?A �􉽗v�f2)
     * @param param2  ��?�2 ?�̃p���??[�^�l
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(ParametricCurve3D curve1, double param1,
                        ParametricCurve3D curve2, double param2,
                        boolean doCheck) {
        super();
        this.pointOnGeometry1 = new PointOnCurve3D(curve1, param1, doCheckDebug);
        this.pointOnGeometry2 = new PointOnCurve3D(curve2, param2, doCheckDebug);
        this.coordinates = this.pointOnGeometry1.linearInterpolate(this.pointOnGeometry2, 0.5);
        if (doCheck)
            checkPoints();
    }

    /**
     * ��?�ƋȖʂ̌�_��?W�l�Ƃ��ꂼ��̊􉽗v�f��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param coordinates ���?�ł�?W�l
     * @param curve1      ���̋�?� (��?�1?A �􉽗v�f1)
     * @param param1      ��?�1 ?�̃p���??[�^�l
     * @param surface2    ����̋Ȗ� (�Ȗ�2?A �􉽗v�f2)
     * @param uParam2     �Ȗ�2 ?�� U �p���??[�^�l
     * @param vParam2     �Ȗ�2 ?�� V �p���??[�^�l
     * @param doCheck     ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(Point3D coordinates,
                        ParametricCurve3D curve1, double param1,
                        ParametricSurface3D surface2, double uParam2, double vParam2,
                        boolean doCheck) {
        super();
        this.coordinates = coordinates;

        this.pointOnGeometry1 = new PointOnCurve3D(curve1, param1, doCheckDebug);
        this.pointOnGeometry2 = new PointOnSurface3D(surface2, uParam2, vParam2, doCheckDebug);
        if (doCheck)
            checkPoints();
    }

    /**
     * ��?�ƋȖʂ̌�_�̂��ꂼ��̊􉽗v�f��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��_�̎��?�ł�?W�l��?A���ꂼ��̊􉽗v�f?�̓_�̒��_�ƂȂ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param curve1   ���̋�?� (��?�1?A �􉽗v�f1)
     * @param param1   ��?�1 ?�̃p���??[�^�l
     * @param surface2 ����̋Ȗ� (�Ȗ�2?A �􉽗v�f2)
     * @param uParam2  �Ȗ�2 ?�� U �p���??[�^�l
     * @param vParam2  �Ȗ�2 ?�� V �p���??[�^�l
     * @param doCheck  ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(ParametricCurve3D curve1, double param1,
                        ParametricSurface3D surface2, double uParam2, double vParam2,
                        boolean doCheck) {
        super();
        this.pointOnGeometry1 = new PointOnCurve3D(curve1, param1, doCheckDebug);
        this.pointOnGeometry2 = new PointOnSurface3D(surface2, uParam2, vParam2, doCheckDebug);
        this.coordinates = this.pointOnGeometry1.linearInterpolate(this.pointOnGeometry2, 0.5);
        if (doCheck)
            checkPoints();
    }

    /**
     * �ȖʂƋ�?�̌�_��?W�l�Ƃ��ꂼ��̊􉽗v�f��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param coordinates ���?�ł�?W�l
     * @param surface1    ���̋Ȗ� (�Ȗ�1?A �􉽗v�f1)
     * @param uParam1     �Ȗ�1 ?�� U �p���??[�^�l
     * @param vParam1     �Ȗ�1 ?�� V �p���??[�^�l
     * @param curve2      ����̋�?� (��?�2?A �􉽗v�f2)
     * @param param2      ��?�2 ?�̃p���??[�^�l
     * @param doCheck     ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(Point3D coordinates,
                        ParametricSurface3D surface1, double uParam1, double vParam1,
                        ParametricCurve3D curve2, double param2,
                        boolean doCheck) {
        super();
        this.coordinates = coordinates;
        this.pointOnGeometry1 = new PointOnSurface3D(surface1, uParam1, vParam1, doCheckDebug);
        this.pointOnGeometry2 = new PointOnCurve3D(curve2, param2, doCheckDebug);
        if (doCheck)
            checkPoints();
    }

    /**
     * �ȖʂƋ�?�̌�_��?W�l�Ƃ��ꂼ��̊􉽗v�f��?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��_�̎��?�ł�?W�l��?A���ꂼ��̊􉽗v�f?�̓_�̒��_�ƂȂ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param surface1 ���̋Ȗ� (�Ȗ�1?A �􉽗v�f1)
     * @param uParam1  �Ȗ�1 ?�� U �p���??[�^�l
     * @param vParam1  �Ȗ�1 ?�� V �p���??[�^�l
     * @param curve2   ����̋�?� (��?�2?A �􉽗v�f2)
     * @param param2   ��?�2 ?�̃p���??[�^�l
     * @param doCheck  ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(ParametricSurface3D surface1, double uParam1, double vParam1,
                        ParametricCurve3D curve2, double param2,
                        boolean doCheck) {
        super();
        this.pointOnGeometry1 = new PointOnSurface3D(surface1, uParam1, vParam1, doCheckDebug);
        this.pointOnGeometry2 = new PointOnCurve3D(curve2, param2, doCheckDebug);
        this.coordinates = this.pointOnGeometry1.linearInterpolate(this.pointOnGeometry2, 0.5);
        if (doCheck)
            checkPoints();
    }

    /**
     * ��Ȗʂ̌�_��?W�l�Ɗe�Ȗʂ�?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param coordinates ���?�ł�?W�l
     * @param surface1    ���̋Ȗ� (�Ȗ�1?A �􉽗v�f1)
     * @param uParam1     �Ȗ�1 ?�� U �p���??[�^�l
     * @param vParam1     �Ȗ�1 ?�� V �p���??[�^�l
     * @param surface2    ����̋Ȗ� (�Ȗ�2?A �􉽗v�f2)
     * @param uParam2     �Ȗ�2 ?�� U �p���??[�^�l
     * @param vParam2     �Ȗ�2 ?�� V �p���??[�^�l
     * @param doCheck     ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(Point3D coordinates,
                        ParametricSurface3D surface1, double uParam1, double vParam1,
                        ParametricSurface3D surface2, double uParam2, double vParam2,
                        boolean doCheck) {
        super();
        this.coordinates = coordinates;
        this.pointOnGeometry1 = new PointOnSurface3D(surface1, uParam1, vParam1, doCheckDebug);
        this.pointOnGeometry2 = new PointOnSurface3D(surface2, uParam2, vParam2, doCheckDebug);
        if (doCheck)
            checkPoints();
    }

    /**
     * ��Ȗʂ̌�_�̊e�Ȗʂ�?�ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��_�̎��?�ł�?W�l��?A���ꂼ��̋Ȗ�?�̓_�̒��_�ƂȂ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A
     * �e�t�B?[���h�ɒl��?ݒ肵�����
     * {@link #checkPoints() checkPoints()} ��Ă�?o��?B
     * </p>
     *
     * @param surface1 ���̋Ȗ� (�Ȗ�1?A �􉽗v�f1)
     * @param uParam1  �Ȗ�1 ?�� U �p���??[�^�l
     * @param vParam1  �Ȗ�1 ?�� V �p���??[�^�l
     * @param surface2 ����̋Ȗ� (�Ȗ�2?A �􉽗v�f2)
     * @param uParam2  �Ȗ�2 ?�� U �p���??[�^�l
     * @param vParam2  �Ȗ�2 ?�� V �p���??[�^�l
     * @param doCheck  ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    IntersectionPoint3D(ParametricSurface3D surface1, double uParam1, double vParam1,
                        ParametricSurface3D surface2, double uParam2, double vParam2,
                        boolean doCheck) {
        super();
        this.pointOnGeometry1 = new PointOnSurface3D(surface1, uParam1, vParam1, doCheckDebug);
        this.pointOnGeometry2 = new PointOnSurface3D(surface2, uParam2, vParam2, doCheckDebug);
        this.coordinates = this.pointOnGeometry1.linearInterpolate(this.pointOnGeometry2, 0.5);
        if (doCheck)
            checkPoints();
    }

    /**
     * ���̓_�� X ?W�l��Ԃ�?B
     *
     * @return �_�� X ?W�l
     */
    public double x() {
        return coordinates.x();
    }

    /**
     * ���̓_�� Y ?W�l��Ԃ�?B
     *
     * @return �_�� Y ?W�l
     */
    public double y() {
        return coordinates.y();
    }

    /**
     * ���̓_�� Z ?W�l��Ԃ�?B
     *
     * @return �_�� Z ?W�l
     */
    public double z() {
        return coordinates.z();
    }

    /**
     * ���̌�_�̎��?�ł�?W�l��Ԃ�?B
     *
     * @return ���?�ł�?W�l
     */
    public Point3D coordinates() {
        return coordinates;
    }

    /**
     * ���̌�_�̊􉽗v�f1 ?�ł̈ʒu��Ԃ�?B
     *
     * @return �􉽗v�f1 ?�ł̈ʒu
     */
    public PointOnGeometry3D pointOnGeometry1() {
        return pointOnGeometry1;
    }

    /**
     * ���̌�_�̊􉽗v�f2 ?�ł̈ʒu��Ԃ�?B
     *
     * @return �􉽗v�f2 ?�ł̈ʒu
     */
    public PointOnGeometry3D pointOnGeometry2() {
        return pointOnGeometry2;
    }

    /**
     * ���̌�_�̊􉽗v�f1 ���?�ł����̂Ƃ���?A��?�1 ?�̈ʒu��Ԃ�?B
     *
     * @return ��?�1 ?�ł̈ʒu
     */
    public PointOnCurve3D pointOnCurve1() {
        if (!pointOnGeometry1().geometry().isCurve())
            return null;
        return (PointOnCurve3D) pointOnGeometry1();
    }

    /**
     * ���̌�_�̊􉽗v�f2 ���?�ł����̂Ƃ���?A��?�2 ?�̈ʒu��Ԃ�?B
     *
     * @return ��?�2 ?�ł̈ʒu
     */
    public PointOnCurve3D pointOnCurve2() {
        if (!pointOnGeometry2().geometry().isCurve())
            return null;
        return (PointOnCurve3D) pointOnGeometry2();
    }

    /**
     * ���̌�_�̊􉽗v�f1 ��Ȗʂł����̂Ƃ���?A�Ȗ�1 ?�̈ʒu��Ԃ�?B
     *
     * @return �Ȗ�1 ?�ł̈ʒu
     */
    public PointOnSurface3D pointOnSurface1() {
        if (!pointOnGeometry1().geometry().isSurface())
            return null;
        return (PointOnSurface3D) pointOnGeometry1();
    }

    /**
     * ���̌�_�̊􉽗v�f2 ��Ȗʂł����̂Ƃ���?A�Ȗ�2 ?�̈ʒu��Ԃ�?B
     *
     * @return �Ȗ�2 ?�ł̈ʒu
     */
    public PointOnSurface3D pointOnSurface2() {
        if (!pointOnGeometry2().geometry().isSurface())
            return null;
        return (PointOnSurface3D) pointOnGeometry2();
    }

    /**
     * ���̊�?���_�ł��邩�ۂ���Ԃ�?B
     *
     * @return ��_�Ȃ̂�?A?�� true
     * @see #isOverlapCurve()
     * @see #isIntersectionCurve()
     */
    public boolean isIntersectionPoint() {
        return true;
    }

    /**
     * ���̊�?��I?[�o?[���b�v�ł��邩�ۂ���Ԃ�?B
     *
     * @return �I?[�o?[���b�v�ł͂Ȃ���_�Ȃ̂�?A?�� false
     * @see #isIntersectionPoint()
     */
    public boolean isOverlapCurve() {
        return false;
    }

    /**
     * ���̊�?���?�ł��邩�ۂ���Ԃ�?B
     *
     * @return ��?�ł͂Ȃ���_�Ȃ̂�?A?�� false
     * @see #isIntersectionPoint()
     */
    public boolean isIntersectionCurve() {
        return false;
    }

    /**
     * ���̊�?��_�ɕϊ�����?B
     * <p/>
     * ������?g��Ԃ�?B
     * </p>
     *
     * @return ������?g
     */
    public IntersectionPoint3D toIntersectionPoint() {
        return this;
    }

    /**
     * ���̊�?�I?[�o?[���b�v�ɕϊ�����?B
     * <p/>
     * ��_��I?[�o?[���b�v�ɕϊ����邱�Ƃ͂ł��Ȃ��̂� null ��Ԃ�?B
     * </p>
     *
     * @return ?�� null
     */
    public OverlapCurve3D toOverlapCurve() {
        return null;
    }

    /**
     * ���̊�?��?�ɕϊ�����?B
     * <p/>
     * ��_���?�ɕϊ����邱�Ƃ͂ł��Ȃ��̂� null ��Ԃ�?B
     * </p>
     *
     * @return ?�� null
     */
    public IntersectionCurve3D toIntersectionCurve() {
        return null;
    }

    /**
     * ���̌�_�� pointOnGeometry1 �� pointOnGeometry2 ��귂�����_��Ԃ�?B
     *
     * @return pointOnGeometry1 �� pointOnGeometry2 ��귂�����_
     */
    public IntersectionPoint3D exchange() {
        IntersectionPoint3D ex =
                new IntersectionPoint3D(coordinates,
                        pointOnGeometry2, pointOnGeometry1, doCheckDebug);
        return ex;
    }

    /**
     * ���̊�?̈��̋�?� (��?�1) ?�ł̈ʒu��?A
     * �^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�Ԃ�?B
     * <p/>
     * ���̌�_�� pointOnCurve1 �̃p���??[�^�l�� sec �͈̔͂�O��Ă���?�?��ɂ�
     * null ��Ԃ�?B
     * </p>
     *
     * @param sec  ��?�1 �̃p���??[�^���
     * @param conv ��?�1 �̃p���??[�^�l��ϊ�����I�u�W�F�N�g
     * @return ��?�1 ?�̈ʒu��^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�
     */
    public CurveCurveInterference3D trim1(ParameterSection sec,
                                          ParameterConversion3D conv) {
        PointOnCurve3D pnt = (PointOnCurve3D) pointOnGeometry1;
        double param = pnt.parameter();
        ParametricCurve3D curve =
                (ParametricCurve3D) pnt.geometry();
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();

        cond.makeCopy(cond.getToleranceForDistanceAsObject()
                .toToleranceForParameter(curve, param)).push();
        try {
            if (!sec.isValid(param))
                return null;
        } finally {
            ConditionOfOperation.pop();
        }
        PointOnCurve3D poc1 = conv.convToPoint(param);
        PointOnCurve3D poc2 = (PointOnCurve3D) pointOnGeometry2;
        return new IntersectionPoint3D(poc1, poc2, doCheckDebug);
    }

    /**
     * ���̊�?̑���̋�?� (��?�2) ?�ł̈ʒu��?A
     * �^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�Ԃ�?B
     * <p/>
     * ���̌�_�� pointOnCurve2 �̃p���??[�^�l�� sec �͈̔͂�O��Ă���?�?��ɂ�
     * null ��Ԃ�?B
     * </p>
     *
     * @param sec  ��?�2 �̃p���??[�^���
     * @param conv ��?�2 �̃p���??[�^�l��ϊ�����I�u�W�F�N�g
     * @return ��?�2 ?�̈ʒu��^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�
     */
    public CurveCurveInterference3D trim2(ParameterSection sec,
                                          ParameterConversion3D conv) {
        PointOnCurve3D pnt = (PointOnCurve3D) pointOnGeometry2;
        double param = pnt.parameter();
        ParametricCurve3D curve =
                (ParametricCurve3D) pnt.geometry();
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();

        cond.makeCopy(cond.getToleranceForDistanceAsObject()
                .toToleranceForParameter(curve, param)).push();
        try {
            if (!sec.isValid(param))
                return null;
        } finally {
            ConditionOfOperation.pop();
        }
        PointOnCurve3D poc1 = (PointOnCurve3D) pointOnGeometry1;
        PointOnCurve3D poc2 = conv.convToPoint(param);
        return new IntersectionPoint3D(poc1, poc2, doCheckDebug);
    }

    /**
     * ���̊�?̈��̋�?� (��?�1) ��^����ꂽ��?�ɒu����������?�Ԃ�?B
     * <p/>
     * �p���??[�^�l�Ȃǂ͂��̂܂�?B
     * </p>
     *
     * @param newCurve ��?�1 ��?ݒ肷���?�
     * @return ��?�1��u����������?�
     */
    public CurveCurveInterference3D changeCurve1(ParametricCurve3D newCurve) {
        if (!this.pointOnGeometry1().geometry().isCurve())
            throw new FatalException();
        PointOnCurve3D pointOnCurve1 = (PointOnCurve3D) this.pointOnGeometry1();
        PointOnCurve3D newPointOnCurve1 =
                new PointOnCurve3D(newCurve, pointOnCurve1.parameter(), doCheckDebug);

        return new IntersectionPoint3D(newPointOnCurve1, this.pointOnGeometry2, doCheckDebug);
    }

    /**
     * ���̊�?̑���̋�?� (��?�2) ��^����ꂽ��?�ɒu����������?�Ԃ�?B
     * <p/>
     * �p���??[�^�l�Ȃǂ͂��̂܂�?B
     * </p>
     *
     * @param newCurve ��?�2 ��?ݒ肷���?�
     * @return ��?�2 ��u����������?�
     */
    public CurveCurveInterference3D changeCurve2(ParametricCurve3D newCurve) {
        if (!this.pointOnGeometry2().geometry().isCurve())
            throw new FatalException();
        PointOnCurve3D pointOnCurve2 = (PointOnCurve3D) this.pointOnGeometry2();
        PointOnCurve3D newPointOnCurve2 =
                new PointOnCurve3D(newCurve, pointOnCurve2.parameter(), doCheckDebug);

        return new IntersectionPoint3D(this.pointOnGeometry1, newPointOnCurve2, doCheckDebug);
    }

    /**
     * ���̓_��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    protected synchronized Point3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Point3D tCoordinates =
                this.coordinates.transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);

        PointOnGeometry3D tPointOnGeometry1;
        PointOnGeometry3D tPointOnGeometry2;

        if (this.pointOnGeometry1.geometry().isPoint() == true) {
            PointOnPoint3D pointOnPoint1
                    = (PointOnPoint3D) this.pointOnGeometry1;
            tPointOnGeometry1 =
                    (PointOnPoint3D) pointOnPoint1.transformBy(reverseTransform,
                            transformationOperator,
                            transformedGeometries);
        } else if (this.pointOnGeometry1.geometry().isCurve() == true) {
            PointOnCurve3D pointOnCurve1 =
                    (PointOnCurve3D) this.pointOnGeometry1;
            tPointOnGeometry1 =
                    (PointOnCurve3D) pointOnCurve1.transformBy(reverseTransform,
                            transformationOperator,
                            transformedGeometries);
        } else {
            PointOnSurface3D pointOnSurface1 =
                    (PointOnSurface3D) this.pointOnGeometry1;
            tPointOnGeometry1 =
                    (PointOnSurface3D) pointOnSurface1.transformBy(reverseTransform,
                            transformationOperator,
                            transformedGeometries);
        }

        if (this.pointOnGeometry2.geometry().isPoint() == true) {
            PointOnPoint3D pointOnPoint2 =
                    (PointOnPoint3D) this.pointOnGeometry2;
            tPointOnGeometry2 =
                    (PointOnPoint3D) pointOnPoint2.transformBy(reverseTransform,
                            transformationOperator,
                            transformedGeometries);
        } else if (this.pointOnGeometry2.geometry().isCurve() == true) {
            PointOnCurve3D pointOnCurve2 =
                    (PointOnCurve3D) this.pointOnGeometry2;
            tPointOnGeometry2 =
                    (PointOnCurve3D) pointOnCurve2.transformBy(reverseTransform,
                            transformationOperator,
                            transformedGeometries);
        } else {
            PointOnSurface3D pointOnSurface2 =
                    (PointOnSurface3D) this.pointOnGeometry2;
            tPointOnGeometry2 =
                    (PointOnSurface3D) pointOnSurface2.transformBy(reverseTransform,
                            transformationOperator,
                            transformedGeometries);
        }

        return new IntersectionPoint3D(tCoordinates,
                tPointOnGeometry1,
                tPointOnGeometry2,
                doCheckDebug);
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
        writer.println(indent_tab + "\tcoordinates");
        coordinates.output(writer, indent + 2);
        writer.println(indent_tab + "\tpointOnGeometry1");
        pointOnGeometry1.output(writer, indent + 2);
        writer.println(indent_tab + "\tpointOnGeometry2");
        pointOnGeometry2.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
