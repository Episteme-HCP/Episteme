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
import java.util.Vector;

/**
 * �Q���� : ?��\���N���X?B
 * <p/>
 * ?��?A
 * �n�_��?W�l spnt
 * ��
 * ?I�_��?W�l epnt
 * �Œ�`�����?B
 * </p>
 * <p/>
 * ?�͔���I�ȗL��?��?A���̃p���??[�^��`��� [0, 1] �ƂȂ�?B
 * </p>
 * <p/>
 * t ��p���??[�^�Ƃ���?� P(t) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(t) = (1 - t) * spnt + t * epnt
 * </pre>
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:52 $
 */

public class BoundedLine2D extends BoundedCurve2D {
    /**
     * �n�_?B
     *
     * @serial
     */
    private Point2D spnt;

    /**
     * ?I�_?B
     *
     * @serial
     */
    private Point2D epnt;

    /**
     * ���̃C���X�^���X�̃t�B?[���h�ɒl��?ݒ肷��?B
     * <p/>
     * doCheck �� true ��?�?�?A
     * spnt �� epnt �̋�������?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param spnt    �n�_��?W�l
     * @param epnt    ?I�_��?W�l
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see Point2D#identical(Point2D)
     * @see InvalidArgumentValueException
     */
    private void setPoints(Point2D spnt, Point2D epnt, boolean doCheck) {
        if (doCheck && spnt.identical(epnt))
            throw new InvalidArgumentValueException();

        this.spnt = spnt;
        this.epnt = epnt;
    }

    /**
     * �n�_��?I�_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * spnt �� epnt �̋�������?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param spnt �n�_
     * @param epnt ?I�_
     * @see Point2D#identical(Point2D)
     * @see InvalidArgumentValueException
     */
    public BoundedLine2D(Point2D spnt, Point2D epnt) {
        super();
        setPoints(spnt, epnt, true);
    }

    /**
     * �n�_��?I�_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true ��?�?�?A
     * spnt �� epnt �̋�������?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param spnt    �n�_
     * @param epnt    ?I�_
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see Point2D#identical(Point2D)
     * @see InvalidArgumentValueException
     */
    BoundedLine2D(Point2D spnt, Point2D epnt, boolean doCheck) {
        super();
        setPoints(spnt, epnt, doCheck);
    }

    /**
     * �n�_��?u�n�_����?I�_�܂ł̃x�N�g��?v��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * dir �̑傫������?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param spnt �n�_
     * @param dir  �n�_����?I�_�܂ł̃x�N�g��
     * @see InvalidArgumentValueException
     */
    public BoundedLine2D(Point2D spnt, Vector2D dir) {
        super();
        setPoints(spnt, spnt.add(dir), true);
    }

    /**
     * �n�_��?u�n�_����?I�_�܂ł̃x�N�g��?v��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true ��?�?�?A
     * dir �̑傫������?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param spnt    �n�_
     * @param dir     �n�_����?I�_�܂ł̃x�N�g��
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     */
    BoundedLine2D(Point2D spnt, Vector2D dir, boolean doCheck) {
        super();
        setPoints(spnt, spnt.add(dir), doCheck);
    }

    /**
     * ����?�̎n�_��Ԃ�?B
     *
     * @return �n�_
     */
    public Point2D spnt() {
        return this.spnt;
    }

    /**
     * ����?��?I�_��Ԃ�?B
     *
     * @return ?I�_
     */
    public Point2D epnt() {
        return this.epnt;
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ�?B
     * <p/>
     * pint �̑?���l�͕��ł©�܂�Ȃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     * @see ParameterOutOfRange
     */
    public double length(ParameterSection pint) {
        checkParameter(pint.start());
        checkParameter(pint.end());
        return length() * Math.abs(pint.increase());
    }

    /**
     * ���̗L��?�S�̂̎��?�ł̒��� (���̂�) ��Ԃ�?B
     *
     * @return ��?�S�̂̒���
     */
    public double length() {
        return this.spnt().distance(this.epnt());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     * @see ParameterOutOfRange
     */
    public Point2D coordinates(double param) {
        param = checkParameter(param);
        return this.epnt().linearInterpolate(this.spnt(), param);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector2D tangentVector(double param) {
        param = checkParameter(param);
        return this.epnt().subtract(this.spnt());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     * <p/>
     * ?�̋ȗ���?A?�� 0 �ł���?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public CurveCurvature2D curvature(double param) {
        param = checkParameter(param);
        return new CurveCurvature2D(0.0, Vector2D.zeroVector);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative2D evaluation(double param) {
        param = checkParameter(param);
        return new CurveDerivative2D(coordinates(param),
                tangentVector(param),
                Vector2D.zeroVector);
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * ?�ɂ͓Hٓ_�͑�?݂��Ȃ���̂Ƃ���?A���� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     */
    public PointOnCurve2D[] singular() {
        return new PointOnCurve2D[0];
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * ?�ɂ͕ϋȓ_�͑�?݂��Ȃ���̂Ƃ���?A���� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     */
    public PointOnCurve2D[] inflexion() {
        return new PointOnCurve2D[0];
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ����_����?�ւ̓��e�_��?���?A���ꂪ��?݂���?�?��ɂ�?A�K�� 1 �ł���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     * @see #project1From(Point2D)
     */
    public PointOnCurve2D[] projectFrom(Point2D point) {
        PointOnCurve2D[] prjp;
        PointOnCurve2D poc;
        double param;

        if ((poc = project1From(point)) == null)
            return new PointOnCurve2D[0];
        prjp = new PointOnCurve2D[1];
        prjp[0] = poc;
        return prjp;
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_���?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��� null ��Ԃ�?B
     * </p>
     * <p/>
     * ����_����?�ւ̓��e�_��?���?A���ꂪ��?݂���?�?��ɂ�?A�K�� 1 �ł���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     * @see #projectFrom(Point2D)
     */
    public PointOnCurve2D project1From(Point2D point) {
        PointOnCurve2D[] prjp;
        PointOnCurve2D poc;
        double param;

        poc = toLine().project1From(point);
        param = poc.parameter();

        PointOnCurve2D result = null;

        if (!isValid(param)) {        // �����Ń`�F�b�N���ׂ�
            return null;
        }

        return new PointOnCurve2D(this, param, doCheckDebug);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve2D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * ������|�����C����?�Ɍ����ɂ���?��?Č�����̂�?A
     * ���̃?�\�b�h�̓Ք�ł� tol �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     */
    public Polyline2D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        Point2D[] points = new Point2D[2];

        points[0] = new PointOnCurve2D(this, pint.start(), doCheckDebug);
        points[1] = new PointOnCurve2D(this, pint.end(), doCheckDebug);
        return new Polyline2D(points);
    }

    /**
     * ����?��?Č�����|�����C����Ԃ�?B
     *
     * @return ����?��?Č�����|�����C��
     */
    Polyline2D toPolyline() {
        Point2D[] points = new Point2D[2];

        points[0] = this.spnt;
        points[1] = this.epnt;
        return new Polyline2D(points);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        return toLine().toBsplineCurve(pint);
    }

    /*
    * ����?��?�ɕϊ�����?B
    *
    * @return	����?��ϊ�������?�
    */
    public Line2D toLine() {
        return new Line2D(spnt(), epnt());
    }

    /**
     * ����?��?i?s����P�ʉ������x�N�g����Ԃ�?B
     *
     * @return �P�ʉ����ꂽ?i?s���
     */
    public Vector2D unitizedDirection() {
        return tangentVector(0.0).unitized();
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�@��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�?B
     * ��B�?A�˂� UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ����?�Ɨ^����ꂽ��?�Ƃ̊ԂɌ�_�����邩�ۂ���Ԃ�?B
     * <p/>
     * ���e��?��̔��f�͂��ׂ�?u����?v��?s�Ȃ�?B
     * </p>
     *
     * @param lin   ��?�
     * @param ludir ��?��?��K���x�N�g��
     * @return 0: parallel, 1: intersects, -1: overlap
     */
    private int haveIntsWithLine(Line2D lin, Vector2D ludir) {
        double magni;
        Vector2D dir;
        double[] dists = new double[2];
        double d_tol = getToleranceForDistance();

        dir = this.spnt.subtract(lin.pnt());
        dists[0] = dir.zOfCrossProduct(ludir);

        dir = this.epnt.subtract(lin.pnt());
        dists[1] = dir.zOfCrossProduct(ludir);

        if (Math.abs(dists[0]) < d_tol && Math.abs(dists[1]) < d_tol)
            return -1;
        else if (Math.abs(dists[0] - dists[1]) < d_tol)
            return 0;
        return 1;

    }

    /*
    * ����?�̒[�_������?��?u�Ѥ?v�ɂ��邩�ǂ����𒲂ׂ�?B
    * <p>
    * is_in, param �̗v�f?���?A?��Ȃ��Ƃ� 2 �ł���K�v������?B
    * </p>
    * <p>
    * is_in[0] �� 0 ��?�?�?A����?�̎n�_�͑���?�̓Ѥ�ɂȂ����Ƃ�?B
    * </p>
    * <p>
    * is_in[0] �� 1 ��?�?�?A����?�̎n�_�͑���?�̓Ѥ�ɂ��邱�Ƃ�?B
    * ����?�?��ɂ�?Aparam[0] �̒l��?A����?�̎n�_�̑���?�ɂ�����p���??[�^�l��?B
    * </p>
    * <p>
    * is_in[1], param[1] ��?A����?��?I�_�Ɋւ��ē��l�̈Ӗ���?��?B
    * </p>
    *
    * @param	bas	����?�
    * @param	BUdir	����?�̒P�ʕ��x�N�g��
    * @param	BLeng	����?�̒���
    * @param	is_in	�[�_������?��?u�Ѥ?v�ɂ��邩�ǂ�����܂ޔz�� (?o�͗p)
    * @param	param	�[�_�̑���?�ɂ�����p���??[�^�l��܂ޔz�� (?o�͗p)
    */
    private void
    isThisInBase(BoundedLine2D bas, Vector2D BUdir, double BLeng,
                 int[] is_in, double[] param) {
        Vector2D dir;
        double d_tol = getToleranceForDistance();

        for (int i = 0; i < 2; i++) {
            if (i == 0)
                dir = this.spnt().subtract(bas.spnt());
            else
                dir = this.epnt().subtract(bas.spnt());

            param[i] = BUdir.dotProduct(dir);
            if ((param[i] < (0.0 - d_tol)) ||
                    (param[i] > (BLeng + d_tol))) {
                is_in[i] = 0;    /* out of range */
            } else {
                is_in[i] = 1;    /* this is in bas */
                if (true) {
                    if (param[i] < (0.0 + d_tol)) param[i] = 0.0;
                    if (param[i] > (BLeng - d_tol)) param[i] = BLeng;
                }
                param[i] /= BLeng;
            }
        }
    }

    /**
     * �^����ꂽ?�񂩂�?A����?�Ƒ��̗L��?�̌�_��?�?�����?B
     *
     * @param mate   ���̗L��?�
     * @param Aparam ��_�̂���?�ɂ�����p���??[�^�l
     * @param Bparam ��_�̑��̗L��?�ɂ�����p���??[�^�l
     * @return ?�?����ꂽ��_
     */
    private IntersectionPoint2D
    toIntersectionPoint(BoundedCurve2D mate, double Aparam, double Bparam) {
        Point2D crd1, crd2;
        PointOnCurve2D poc1, poc2;

        crd1 = this.coordinates(Aparam);
        crd2 = mate.coordinates(Bparam);
        poc1 = new PointOnCurve2D(crd1, this, Aparam, doCheckDebug);
        poc2 = new PointOnCurve2D(crd2, mate, Bparam, doCheckDebug);

        crd1 = crd1.linearInterpolate(crd2, 0.5);

        return new IntersectionPoint2D(crd1, poc1, poc2, doCheckDebug);
    }

    /**
     * ����?�Ƒ���?�̃I?[�o?[���b�v��?�߂�?B
     * <p/>
     * ����?�Ƒ���?�I?[�o?[���b�v���Ȃ���� null ��Ԃ�?B
     * </p>
     * <p/>
     * ����?�Ƒ���?�I?[�o?[���b�v����?�?���?A
     * needOverlap �� false �Ȃ��?A
     * �I?[�o?[���b�v�����Ԃ̒��_�̈ʒu��?�?�������_�� suitable �Ƃ���?A
     * IndefiniteSolutionException �̗�O��?�����?B
     * </p>
     *
     * @param needOverlap �I?[�o?[���b�v���~������� true?A��_���~������� false
     * @param mate        ����?�
     * @param AUdir       ����?�̒P�ʕ��x�N�g��
     * @param BUdir       ����?�̒P�ʕ��x�N�g��
     * @param Aleng       ����?�̒���
     * @param Bleng       ����?�̒���
     * @return �I?[�o?[���b�v
     * @throws IndefiniteSolutionException �I?[�o?[���b�v�����邪?AneedOverlap �� false �ł���
     */
    private CurveCurveInterference2D
    haveCommonSection(boolean needOverlap, BoundedLine2D mate,
                      Vector2D AUdir, Vector2D BUdir,
                      double Aleng, double Bleng)
            throws IndefiniteSolutionException {
        CurveCurveInterference2D intf;
        int[] A_inB = new int[2];
        int[] B_inA = new int[2];

        double[] pA_inA = new double[2];
        double[] pA_inB = new double[2];
        double[] pB_inA = new double[2];
        double[] pB_inB = new double[2];
        double[] Ap = new double[2];
        double[] Bp = new double[2];

        int i;

        this.isThisInBase(mate, BUdir, Bleng, A_inB, pA_inB);
        mate.isThisInBase(this, AUdir, Aleng, B_inA, pB_inA);

        if ((A_inB[0] + A_inB[1] + B_inA[0] + B_inA[1]) < 2)
            return null;

        pA_inA[0] = 0.0;
        pA_inA[1] = 1.0;
        pB_inB[0] = 0.0;
        pB_inB[1] = 1.0;

        switch (A_inB[0] + A_inB[1] + B_inA[0] + B_inA[1]) {
            case 2:
                i = 0;
                if (A_inB[0] != 0) {
                    Ap[i] = pA_inA[0];
                    Bp[i] = pA_inB[0];
                    i++;
                }
                if (A_inB[1] != 0) {
                    Ap[i] = pA_inA[1];
                    Bp[i] = pA_inB[1];
                    i++;
                }
                if (B_inA[0] != 0) {
                    Ap[i] = pB_inA[0];
                    Bp[i] = pB_inB[0];
                    i++;
                }
                if (B_inA[1] != 0) {
                    Ap[i] = pB_inA[1];
                    Bp[i] = pB_inB[1];
                    i++;
                }
                break;

            case 3:
                if (A_inB[0] != 0 && A_inB[1] != 0) {
                    Ap[0] = pA_inA[0];
                    Bp[0] = pA_inB[0];
                    Ap[1] = pA_inA[1];
                    Bp[1] = pA_inB[1];
                } else {
                    Ap[0] = pB_inA[0];
                    Bp[0] = pB_inB[0];
                    Ap[1] = pB_inA[1];
                    Bp[1] = pB_inB[1];
                }
                break;

            case 4:
                if (Aleng > Bleng) {
                    Ap[0] = pA_inA[0];
                    Bp[0] = pA_inB[0];
                    Ap[1] = pA_inA[1];
                    Bp[1] = pA_inB[1];
                } else {
                    Ap[0] = pB_inA[0];
                    Bp[0] = pB_inB[0];
                    Ap[1] = pB_inA[1];
                    Bp[1] = pB_inB[1];
                }
                break;
        }

        /*
        * overlap
        */
        double d_tol = getToleranceForDistance();
        boolean hasWidth = true;
        boolean throwIndefinite = false;
        if ((Math.abs(Ap[0] - Ap[1]) * Aleng < d_tol) ||
                (Math.abs(Bp[0] - Bp[1]) * Bleng < d_tol)) {
            hasWidth = false;

        } else if (!needOverlap) {
            /*
            * make middle point of overlap as an intersection
            */
            hasWidth = false;
            throwIndefinite = true;
        }

        if (hasWidth) {
            ParameterSection sec1, sec2;

            sec1 = new ParameterSection(Ap[0], Ap[1] - Ap[0]);
            sec2 = new ParameterSection(Bp[0], Bp[1] - Bp[0]);
            intf = new OverlapCurve2D(this, sec1, mate, sec2, false);
        } else {
            Ap[0] = (Ap[0] + Ap[1]) / 2.0;
            Bp[0] = (Bp[0] + Bp[1]) / 2.0;
            intf = toIntersectionPoint(mate, Ap[0], Bp[0]);
            if (throwIndefinite)
                throw new IndefiniteSolutionException(intf.toIntersectionPoint());
        }

        return intf;
    }

    /**
     * ����?�Ƒ���?��_��?���Ƃ����BĂ����̂Ƃ���?A���̌�_��?�߂�?B
     * <p/>
     * �?�߂���_���ǂ��炩��?�̒�`���O��Ă���?�?��ɂ�?Anull ��Ԃ�?B
     * ����?ۂ̋��e��?����f��?A��?�ł�?u�p���??[�^�l?v��?s�Ȃ���?B
     * </p>
     *
     * @param mate ����?�
     * @param Alin ����?��ϊ�������?�
     * @param Blin ����?��ϊ�������?�
     * @return ����?�Ƒ���?�̌�_
     * @see #checkParameter(double)
     */
    private IntersectionPoint2D getIntsWithBln(BoundedLine2D mate,
                                               Line2D Alin, Line2D Blin) {
        Vector2D a2b;    /* vector from Alin.pnt to Blin.pnt */
        double crosz;        /* 3rd element of cross product */
        double Aparam, Bparam;

        a2b = Blin.pnt().subtract(Alin.pnt());
        crosz = Alin.dir().zOfCrossProduct(Blin.dir());
        try {
            Aparam = a2b.zOfCrossProduct(Blin.dir()) / crosz;
            Aparam = this.checkParameter(Aparam);    // �����Ń`�F�b�N���ׂ�
            Bparam = a2b.zOfCrossProduct(Alin.dir()) / crosz;
            Bparam = mate.checkParameter(Bparam);    // �����Ń`�F�b�N���ׂ�
        } catch (ParameterOutOfRange e) {
            return null;
        }

        return toIntersectionPoint(mate, Aparam, Bparam);
    }

    /**
     * ����?�Ƒ���?�Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param mate     ����?�
     * @param thisUdir ����?�̒P�ʕ��x�N�g��
     * @param mateUdir ����?�̒P�ʕ��x�N�g��
     * @param thisLeng ����?�̒���
     * @param mateLeng ����?�̒���
     * @return ��?�
     * @see #haveIntsWithLine(Line2D,Vector2D)
     * @see #haveCommonSection(boolean,BoundedLine2D,Vector2D,Vector2D,double,double)
     * @see #getIntsWithBln(BoundedLine2D,Line2D,Line2D)
     */
    CurveCurveInterference2D
    interfere1(BoundedLine2D mate, Vector2D thisUdir, Vector2D mateUdir,
               double thisLeng, double mateLeng) {
        CurveCurveInterference2D com_sec;
        IntersectionPoint2D ints;
        Line2D Alin;
        Line2D Blin;

        Blin = mate.toLine();
        if ((haveIntsWithLine(Blin, mateUdir)) < 0) {    // overlap?
            try {
                return haveCommonSection(true, mate, thisUdir, mateUdir, thisLeng, mateLeng);
            } catch (IndefiniteSolutionException e) {
                throw new FatalException();
            }
        }

        Alin = this.toLine();
        switch (mate.haveIntsWithLine(Alin, thisUdir)) {
            case-1:                    // overlap?
                try {
                    return haveCommonSection(true, mate, thisUdir, mateUdir, thisLeng, mateLeng);
                } catch (IndefiniteSolutionException e) {
                    throw new FatalException();
                }
            case 0:                        // parallel
                return null;
        }

        return getIntsWithBln(mate, Alin, Blin);
    }

    /**
     * ����?�Ƒ���?�Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param mate ����?�
     * @return ��?�
     */
    public CurveCurveInterference2D interfere1(BoundedLine2D mate) {
        Vector2D AUdir = this.unitizedDirection();
        Vector2D BUdir = mate.unitizedDirection();
        double Aleng = this.length();
        double Bleng = mate.length();

        return interfere1(mate, AUdir, BUdir, Aleng, Bleng);
    }

    /**
     * ����?�Ƒ���?�Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate     ����?�
     * @param thisUdir ����?�̒P�ʕ��x�N�g��
     * @param mateUdir ����?�̒P�ʕ��x�N�g��
     * @param thisLeng ����?�̒���
     * @param mateLeng ����?�̒���
     * @return ��?̔z��
     */
    CurveCurveInterference2D[]
    interfere(BoundedLine2D mate, Vector2D thisUdir, Vector2D mateUdir,
              double thisLeng, double mateLeng) {
        CurveCurveInterference2D sol;
        if ((sol = interfere1(mate, thisUdir, mateUdir, thisLeng, mateLeng)) == null)
            return new CurveCurveInterference2D[0];

        CurveCurveInterference2D[] intf = new CurveCurveInterference2D[1];
        intf[0] = sol;
        return intf;
    }

    /**
     * ����?�Ƒ���?�Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ����?�
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?̔z��
     */
    CurveCurveInterference2D[] interfere(BoundedLine2D mate,
                                         boolean doExchange) {
        BoundedLine2D Abln;
        BoundedLine2D Bbln;
        if (!doExchange) {
            Abln = this;
            Bbln = mate;
        } else {
            Abln = mate;
            Bbln = this;
        }

        Vector2D AUdir = Abln.unitizedDirection();
        Vector2D BUdir = Bbln.unitizedDirection();
        double Aleng = Abln.length();
        double Bleng = Bbln.length();

        return Abln.interfere(Bbln, AUdir, BUdir, Aleng, Bleng);
    }

    /**
     * �^����ꂽ��?̑�?ۂƂȂ��?�ⱂ�?�ɕ�?X����?B
     *
     * @param sourceInterferences ��?̔z��
     * @param doExchange          ?�?G?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    private CurveCurveInterference2D[]
    convertInterferences(CurveCurveInterference2D[] sourceInterferences,
                         boolean doExchange) {
        Vector resultVector = new Vector();

        for (int i = 0; i < sourceInterferences.length; i++) {
            CurveCurveInterference2D intf;
            if (!doExchange)
                intf = sourceInterferences[i].changeCurve1(this);
            else
                intf = sourceInterferences[i].changeCurve2(this);
            if (intf != null)
                resultVector.addElement(intf);
        }

        CurveCurveInterference2D[] result =
                new CurveCurveInterference2D[resultVector.size()];
        resultVector.copyInto(result);
        return result;
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�|�����C��) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ����?��|�����C���ɕϊ���?A
     * �|�����C���̃N���X��?u�|�����C�� vs. �|�����C��?v�̊�?��Z�?�\�b�h
     * {@link Polyline2D#interfere(Polyline2D,boolean)
     * Polyline2D.interfere(Polyline2D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ?�?G?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(Polyline2D mate,
                                         boolean doExchange) {
        return this.convertInterferences(this.toPolyline().interfere(mate, doExchange),
                doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�x�W�G��?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ����?��L�?�a�X�v���C����?�ɕϊ���?A
     * �a�X�v���C����?�̃N���X��?u�a�X�v���C����?� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link BsplineCurve2D#interfere(PureBezierCurve2D,boolean)
     * BsplineCurve2D.interfere(PureBezierCurve2D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ?�?G?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(PureBezierCurve2D mate,
                                         boolean doExchange) {
        return this.convertInterferences(this.toBsplineCurve().interfere(mate, doExchange),
                doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�a�X�v���C����?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ����?��L�?�a�X�v���C����?�ɕϊ���?A
     * �a�X�v���C����?�̃N���X��?u�a�X�v���C����?� vs. �a�X�v���C����?�?v�̊�?��Z�?�\�b�h
     * {@link BsplineCurve2D#interfere(BsplineCurve2D,boolean)
     * BsplineCurve2D.interfere(BsplineCurve2D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ?�?G?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(BsplineCurve2D mate,
                                         boolean doExchange) {
        return this.convertInterferences(this.toBsplineCurve().interfere(mate, doExchange),
                doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�g������?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �g������?�̃N���X��?u�g������?� vs. ?�?v�̊�?��Z�?�\�b�h
     * {@link TrimmedCurve2D#interfere(BoundedLine2D,boolean)
     * TrimmedCurve2D.interfere(BoundedLine2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�g������?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(TrimmedCurve2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�Z�O�?���g) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. ?�?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurveSegment2D#interfere(BoundedLine2D,boolean)
     * CompositeCurveSegment2D.interfere(BoundedLine2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�Z�O�?���g)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(CompositeCurveSegment2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�̃N���X��?u��?���?� vs. ?�?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurve2D#interfere(BoundedLine2D,boolean)
     * CompositeCurve2D.interfere(BoundedLine2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(CompositeCurve2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?�̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ���?�̊�?̔z��
     */
    public CurveCurveInterference2D[] interfere(BoundedCurve2D mate) {
        return mate.interfere(this, true);
    }

    /**
     * ����?�Ƒ���?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param mate  ����?�
     * @param AUdir ����?�̒P�ʕ��x�N�g��
     * @param BUdir ����?�̒P�ʕ��x�N�g��
     * @param Aleng ����?�̒���
     * @param Bleng ����?�̒���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException ��?�I?[�o?[���b�v���Ă���
     */
    IntersectionPoint2D intersect1(BoundedLine2D mate, Vector2D AUdir,
                                   Vector2D BUdir, double Aleng, double Bleng)
            throws IndefiniteSolutionException {
        CurveCurveInterference2D com_sec;
        Line2D Alin;
        Line2D Blin;

        Blin = mate.toLine();
        if ((haveIntsWithLine(Blin, BUdir)) < 0) {    // overlap?
            if ((com_sec = haveCommonSection(false, mate, AUdir, BUdir, Aleng, Bleng)) == null)
                return null;

            return com_sec.toIntersectionPoint();
        }

        Alin = this.toLine();
        switch (mate.haveIntsWithLine(Alin, AUdir)) {
            case-1:                    // overlap?
                if ((com_sec = haveCommonSection(false, mate, AUdir, BUdir, Aleng, Bleng)) == null)
                    return null;

                return com_sec.toIntersectionPoint();
            case 0:                        // parallel
                return null;
        }

        return getIntsWithBln(mate, Alin, Blin);
    }

    /**
     * ����?�Ƒ���?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param mate ����?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException ��?�I?[�o?[���b�v���Ă���
     */
    public IntersectionPoint2D intersect1(BoundedLine2D mate)
            throws IndefiniteSolutionException {
        CurveCurveInterference2D com_sec;
        Line2D Alin;
        Line2D Blin;

        Vector2D AUdir = this.unitizedDirection();
        Vector2D BUdir = mate.unitizedDirection();
        double Aleng = this.length();
        double Bleng = mate.length();

        return intersect1(mate, AUdir, BUdir, Aleng, Bleng);
    }

    /**
     * ����?�Ƒ��̒�?�̌�_��?�߂�?B
     * <p/>
     * ���̃?�\�b�h�ł�?Athis �� bounded �ł͂Ȃ� infinite �� line �ƌ��Ȃ�?A
     * mate �Ƃ̌�_��?�߂�?B
     * ��_�Ƃ��ĕԂ� IntersectionPoint2D �� pointOnCurve1 �� basisCurve �ɂ�
     * this �ł͂Ȃ�?Athis ��ϊ����� Line2D ��?ݒ肳���?B
     * ��?�?s��?�?��ɂ� null ��Ԃ���?A
     * ��?�?d�����Ă���?�?��ɂ� IndefiniteSolutionException �𓊂���?B
     * </p>
     *
     * @param mate ���̒�?�
     * @return ��_
     * @throws IndefiniteSolutionException ��?�?d�����Ă���
     */
    public IntersectionPoint2D intersect1AsInfiniteLine(Line2D mate)
            throws IndefiniteSolutionException {
        boolean overlap;

        switch (this.haveIntsWithLine(mate, mate.dir().unitized())) {
            case 0:        // parallel
                return null;
            case(-1):    // overlap
                overlap = true;
                break;
            default:    // intersect
                overlap = false;
                break;
        }

        Line2D lineA = this.toLine();
        Line2D lineB = mate;
        double paramA;
        double paramB;

        if (overlap == false) {
            Vector2D a2b = lineB.pnt().subtract(lineA.pnt());
            double crosz = lineA.dir().zOfCrossProduct(lineB.dir());
            paramA = a2b.zOfCrossProduct(lineB.dir()) / crosz;
            paramB = a2b.zOfCrossProduct(lineA.dir()) / crosz;
        } else {
            paramA = 0.0;
            paramB = lineB.project1From(lineA.coordinates(paramA)).parameter();
        }

        IntersectionPoint2D intersection =
                new IntersectionPoint2D(lineA, paramA,
                        lineB, paramB, doCheckDebug);

        if (overlap == true)
            throw new IndefiniteSolutionException("Two curves overlap.", intersection);

        return intersection;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ����� Bspline ��?��?�߂�?B
     * <p/>
     * ���ʂƂ��ē����� Bspline ��?�͂��̃|�����C���̃I�t�Z�b�g��?��?�Ɍ�����?Č�����?B
     * ��B�?A���̃?�\�b�h�̓Ք�ł� tol �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
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
        return toLine().offsetByBsplineCurve(pint, magni, side, tol);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ�����L��?��?�߂�?B
     * <p/>
     * ���ʂƂ��ē�����L��?�͂��̃|�����C���̃I�t�Z�b�g��?��?�Ɍ�����?Č�����?B
     * ��B�?A���̃?�\�b�h�̓Ք�ł� tol �̒l�͎Q?Ƃ��Ȃ�?B
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
        Vector2D lineDirection = this.tangentVector(0.0);
        Vector2D offsetVector;
        if (side == WhichSide.RIGHT)
            offsetVector = Vector2D.of(lineDirection.y(), -lineDirection.x());
        else
            offsetVector = Vector2D.of(-lineDirection.y(), lineDirection.x());
        offsetVector = offsetVector.unitized().multiply(magni);
        if (pint.increase() < 0.0)
            offsetVector = offsetVector.reverse();

        return new BoundedLine2D(this.coordinates(pint.start()).add(offsetVector),
                this.coordinates(pint.end()).add(offsetVector));
    }

    /**
     * ����?�̎w��̋�Ԃ�?A���̋�?�̎w��̋�Ԃɂ�����t�B���b�g��?�߂�?B
     * <p/>
     * �t�B���b�g����?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param pint1      ���̋�?�̃p���??[�^���
     * @param side1      ���̋�?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *                   (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param mate       ���̋�?�
     * @param pint2      ���̋�?�̃p���??[�^���
     * @param side2      ���̋�?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *                   (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param radius     �t�B���b�g���a
     * @param doExchange �t�B���b�g�� point1/2 ��귂��邩�ǂ���
     * @return �t�B���b�g�̔z��
     * @throws IndefiniteSolutionException ��s��
     * @see WhichSide
     */
    FilletObject2D[]
    doFillet(ParameterSection pint1, int side1, ParametricCurve2D mate,
             ParameterSection pint2, int side2, double radius,
             boolean doExchange)
            throws IndefiniteSolutionException {
        Line2D lin = toLine();
        FilletObject2D[] flts;
        boolean indefinite = false;
        try {
            flts = lin.fillet(pint1, side1, mate, pint2, side2, radius);
        } catch (IndefiniteSolutionException e) {
            flts = new FilletObject2D[1];
            flts[0] = (FilletObject2D) e.suitable();
            indefinite = true;
        }
        PointOnCurve2D poc1;
        for (int i = 0; i < flts.length; i++) {
            poc1 = new PointOnCurve2D(this, flts[i].pointOnCurve1().parameter(), doCheckDebug);
            if (!doExchange)
                flts[i] = new FilletObject2D(radius, flts[i].center(), poc1, flts[i].pointOnCurve2());
            else
                flts[i] = new FilletObject2D(radius, flts[i].center(), flts[i].pointOnCurve2(), poc1);
        }
        if (indefinite)
            throw new IndefiniteSolutionException(flts[0]);
        return flts;
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
     * ����?�̑�?ݔ͈͂���`��Ԃ�?B
     *
     * @return ��?ݔ͈͂���`
     */
    EnclosingBox2D enclosingBox() {
        double min_x, max_x, min_y, max_y;

        if (spnt().x() < epnt().x()) {
            min_x = spnt().x();
            max_x = epnt().x();
        } else {
            min_x = epnt().x();
            max_x = spnt().x();
        }
        if (spnt().y() < epnt().y()) {
            min_y = spnt().y();
            max_y = epnt().y();
        } else {
            min_y = epnt().y();
            max_y = spnt().y();
        }
        return new EnclosingBox2D(min_x, min_y, max_x, max_y);
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     * <p/>
     * �L�Ŕ���I�Ȓ�`���Ԃ�?B
     * �p���??[�^��Ԃ� [0, 1] ?B
     * </p>
     *
     * @return �p���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        return new ParameterDomain(false, 0.0, 1.0);
    }

    /*
    * �^����ꂽ�p���??[�^�l������?�ɑ΂��ėL��ۂ���Ԃ�?B
    * <p>
    * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
    * ParameterOutOfRange �̗�O��?�����?B
    * </p>
    *
    * @param param	�p���??[�^�l
    * @return	��`���Ɋۂ߂�ꂽ�p���??[�^�l
    * @see	AbstractParametricCurve#checkValidity(double)
    */
    private double checkParameter(double param) {
        checkValidity(param);
        return parameterDomain().force(param);
    }

    /**
     * ����?�𔽓]����?��Ԃ�?B
     *
     * @return ���]����?�
     */
    public BoundedLine2D reverse() {
        return new BoundedLine2D(this.epnt(), this.spnt());
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#BOUNDED_LINE_2D ParametricCurve2D.BOUNDED_LINE_2D}
     */
    int type() {
        return BOUNDED_LINE_2D;
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
        Point2D tSpnt = this.spnt().transformBy(reverseTransform,
                transformationOperator,
                transformedGeometries);
        Point2D tEpnt = this.epnt().transformBy(reverseTransform,
                transformationOperator,
                transformedGeometries);
        return new BoundedLine2D(tSpnt, tEpnt);
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
        writer.println(indent_tab + "\tspnt");
        spnt.output(writer, indent + 2);
        writer.println(indent_tab + "\tepnt");
        epnt.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
