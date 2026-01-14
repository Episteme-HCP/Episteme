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
 * �Q���� : �|�����C����\���N���X?B
 * <p/>
 * �|�����C����?A?ߓ_�̗� points ��
 * �����`�����ۂ���\���t���O closed
 * �Œ�`�����?B
 * </p>
 * <p/>
 * closed �̒l�� true �ł���Ε����`���Ƃ���?Afalse �ł���ΊJ�����`���Ƃ��Ĉ�����?B
 * �����`���̃|�����C����?A
 * ?��?�?���?Ō��?ߓ_�싂�?�����̂Ƃ��Ĉ�����?B
 * </p>
 * <p/>
 * �|�����C���̃p���??[�^��`���?A
 * ��?ڂ���?ߓ_�̊Ԃ̃p���??[�^��Ԃ̑傫����?�� 1 �Ƃ���?A
 * ��?�S��ł� [0, N] �ƂȂ�?B
 * ������ N ��?A
 * �|�����C�����J�����`���ł���� (?ߓ_��?� - 1)?A
 * �����`���ł���� (?ߓ_��?�) �ɂȂ�?B
 * </p>
 * <p/>
 * t ��p���??[�^�Ƃ���|�����C�� P(t) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(t) = (1 - (t - s)) * points[s] + (t - s) * points[s + 1]
 * </pre>
 * ������ s �� t ��z���Ȃ�?ő��?�?�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:18 $
 */

public class Polyline2D extends BoundedCurve2D {
    /**
     * �ʑ��I�ɗ�?ڂ���?ߓ_�̊􉽓I�ȓ���?���`�F�b�N���邩�ǂ������t���O
     * <p/>
     * ���܂̂Ƃ��� false (����?���`�F�b�N���Ȃ�) �Ƃ��Ă���?B
     * </p>
     */
    private static final boolean CHECK_SAME_POINTS = false;

    /**
     * ����p���??[�^�l�ɑΉ�����Z�O�?���g�Ƌ�?��I�ȃp���??[�^�l��\���Ք�N���X
     */
    private class PolyParam {
        /**
         * �Z�O�?���g�̊J�n�_
         */
        Point2D sp;

        /**
         * �Z�O�?���g��?I���_
         */
        Point2D ep;

        /**
         * ��?��I�ȃp���??[�^�l
         */
        double weight;

        /**
         * (?��K�����ꂽ) ���I�ȃp���??[�^�l
         */
        double param;

        /**
         * �Z�O�?���g�̔�?� (0 �x?[�X)
         */
        int index;
    }

    /**
     * ?ߓ_�̔z��?B
     *
     * @serial
     */
    private Point2D[] points;

    /**
     * �����`�����ۂ���\���t���O?B
     *
     * @serial
     */
    private boolean closed;

    /**
     * ���̃C���X�^���X�̃t�B?[���h�̒l��?ݒ肷��?B
     * <p/>
     * closed �� false ��?�?�?A
     * points �̗v�f?��� 2 ���?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * closed �� true ��?�?�?A
     * points �̗v�f?��� 3 ���?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param points ?ߓ_�̔z��
     * @param closed ���Ă��邩�ۂ���\���t���O
     * @see InvalidArgumentValueException
     */
    private void setPoints(Point2D[] points, boolean closed) {
        if (!closed && points.length < 2 ||
                closed && points.length < 3)
            throw new InvalidArgumentValueException();

        this.closed = closed;
        this.points = new Point2D[points.length];

        this.points[0] = points[0];
        for (int i = 1; i < points.length; i++) {
            if (CHECK_SAME_POINTS) {
                if (points[i].identical(points[i - 1]))
                    throw new InvalidArgumentValueException();
            }
            this.points[i] = points[i];
        }
        if (CHECK_SAME_POINTS) {
            if (closed && points[0].identical(points[points.length - 1]))
                throw new InvalidArgumentValueException();
        }
    }

    /**
     * ?ߓ_�ƕ����`�����ۂ���\���t���O��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * closed �� false ��?�?�?A
     * points �̗v�f?��� 2 ���?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * closed �� true ��?�?�?A
     * points �̗v�f?��� 3 ���?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param points ?ߓ_�̔z��
     * @param closed ���Ă��邩�ۂ���\���t���O
     * @see InvalidArgumentValueException
     */
    public Polyline2D(Point2D[] points, boolean closed) {
        super();
        setPoints(points, closed);
    }

    /**
     * ?ߓ_��^���ĊJ�����`���Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * points �̗v�f?��� 2 ���?��������
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param points ?ߓ_�̔z��
     * @see InvalidArgumentValueException
     */
    public Polyline2D(Point2D[] points) {
        super();
        setPoints(points, false);
    }

    /**
     * �^����ꂽ�L��?��w��̋��e��?��Œ�?�ߎ������̂Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param curve �L��?�
     * @param tol   �����̋��e��?�
     * @see BoundedCurve2D#toPolyline(ToleranceForDistance)
     */
    public Polyline2D(BoundedCurve2D curve,
                      ToleranceForDistance tol) {
        super();
        Polyline2D pl = curve.toPolyline(tol);
        this.points = pl.points;
        this.closed = pl.closed;
    }

    /**
     * �^����ꂽ��?�̎w��̋�Ԃ�w��̋��e��?��Œ�?�ߎ������̂Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param curve ��?�
     * @param pint  ��?�ߎ�����p���??[�^���
     * @param tol   �����̋��e��?�
     * @see ParametricCurve2D#toPolyline(ParameterSection,ToleranceForDistance)
     */
    public Polyline2D(ParametricCurve2D curve,
                      ParameterSection pint,
                      ToleranceForDistance tol) {
        super();
        Polyline2D pl = curve.toPolyline(pint, tol);
        this.points = pl.points;
        this.closed = pl.closed;
    }

    /**
     * ���̃|�����C����?ߓ_�̔z���Ԃ�?B
     *
     * @return ?ߓ_�̔z��
     */
    public Point2D[] points() {
        Point2D[] pnts = new Point2D[points.length];

        for (int i = 0; i < points.length; i++)
            pnts[i] = points[i];
        return pnts;
    }

    /**
     * ���̃|�����C���� i �Ԃ߂�?ߓ_��Ԃ�?B
     * <p/>
     * ���̃|�����C���������`����?Ai ��?ߓ_��?��ɓ�����?�?���?A0 �Ԗڂ�?ߓ_��Ԃ�?B
     * </p>
     *
     * @return i �Ԃ߂�?ߓ_
     */
    public Point2D pointAt(int i) {
        if (closed() && i == nPoints())
            return points[0];

        return points[i];
    }

    /**
     * ���̃|�����C���������`���ł��邩�ۂ���Ԃ�?B
     *
     * @return �����`���ł���� true?A����Ȃ��� false
     */
    public boolean closed() {
        return this.closed;
    }

    /**
     * ���̃|�����C����?ߓ_��?���Ԃ�?B
     *
     * @return ?ߓ_��?�
     */
    public int nPoints() {
        return points.length;
    }

    /**
     * ���̃|�����C���̃Z�O�?���g��?���Ԃ�?B
     * <p/>
     * ���̃|�����C���������`���ł����?A
     * �Z�O�?���g��?���?ߓ_��?��ɓ�����?B
     * �J�����`���ł���� (?ߓ_��?� - 1) ��Ԃ�?B
     * </p>
     *
     * @return �Z�O�?���g��?�
     */
    public int nSegments() {
        if (closed())
            return nPoints();

        return nPoints() - 1;
    }

    /**
     * �^����ꂽ�p���??[�^�l�ɑΉ�����Z�O�?���g�Ƌ�?��I�ȃp���??[�^�l��?���Ԃ�?B
     * <p/>
     * param �̒l�� i.0 ��?�?��ɂ� i �Ԗڂ̃Z�O�?���g��?���Ԃ�?B
     * ������?Ai ���Z�O�?���g��?��ɓ�����?�?��ɂ� (i - 1) �Ԗڂ̃Z�O�?���g��?���Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return param �ɑΉ�����Z�O�?���g�Ƌ�?��I�ȃp���??[�^�l��?��
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    private PolyParam checkParameter(double param) {
        PolyParam p = new PolyParam();

        int n = closed ? points.length : points.length - 1;

        if (closed) {
            param = parameterDomain().wrap(param);
        } else {
            checkValidity(param);
        }

        int idx = (int) Math.floor(param);
        if (idx < 0)
            idx = 0;
        if (n - 1 < idx)
            idx = n - 1;

        p.sp = points[idx];
        if (idx + 1 == points.length)
            p.ep = points[0];    // only closed case
        else
            p.ep = points[idx + 1];

        p.weight = param - idx;
        p.param = param;
        p.index = idx;

        return p;
    }

    /**
     * ?��?��?ۂƂȂ�p���??[�^��ԂɊ܂܂��Z�O�?���g���ɔC�ӂ�?��?��{�����߂̒�?ۃN���X?B
     */
    private abstract class LineSegmentAccumulator {

        /**
         * �^����ꂽ��_�ɂ���?A���炩��?��?��?s�Ȃ���?ۃ?�\�b�h?B
         * <p/>
         * ���̃?�\�b�h��
         * {@link #accumulate(ParameterSection) accumulate(ParameterSection)}
         * �̒��ŌĂ�?o�����?B
         * </p>
         * <p/>
         * �^����ꂽ��_��?A
         * ?�ɂ���Z�O�?���g�̒[�_�ł���킯�ł͂Ȃ�?A
         * ����Z�O�?���g�̒��ԓ_�ł��邩�µ��Ȃ�?B
         * </p>
         *
         * @param sp     �J�n�_
         * @param ep     ?I���_
         * @param sParam sp �ɑΉ�����p���??[�^�l
         * @param eParam ep �ɑΉ�����p���??[�^�l
         */
        abstract void doit(Point2D sp, Point2D ep,
                           double sParam, double eParam);

        /**
         * ���炩��?��?��n�߂邽�߂�?����?s�Ȃ���?ۃ?�\�b�h?B
         * <p/>
         * ���̃?�\�b�h��
         * {@link #accumulate(ParameterSection) accumulate(ParameterSection)}
         * �̒���
         * {@link #doit(Point2D,Point2D,double,double)
         * doit(Point2D, Point2D, double, double)}
         * ��Ă�?o���O�ɌĂ�?o�����?B
         * </p>
         */
        abstract void allocate(int nsegs);

        /**
         * �^����ꂽ�p���??[�^��ԂɊ܂܂��Z�O�?���g����
         * {@link #doit(Point2D,Point2D,double,double)
         * doit(Point2D, Point2D, double, double)}
         * ��Ă�?o��?B
         */
        void accumulate(ParameterSection pint) {
            PolyParam sPolyParam = checkParameter(pint.start());
            PolyParam ePolyParam = checkParameter(pint.end());
            Point2D sPoint;
            Point2D ePoint;
            ParameterDomain domain = parameterDomain();
            boolean wrapAround;

            if (domain.isPeriodic())
                wrapAround = (sPolyParam.param > ePolyParam.param);
            else
                wrapAround = false;

            if (wrapAround) {
                allocate(nPoints() - sPolyParam.index + ePolyParam.index);

                sPoint = coordinates(sPolyParam.param);
                ePoint = sPolyParam.ep;
                doit(sPoint, ePoint,
                        sPolyParam.param, (double) sPolyParam.index + 1);

                for (int seg = sPolyParam.index + 1; seg < nPoints(); seg++) {
                    int wrappedSeg1 = (seg + 1) % nPoints();
                    doit(points[seg], points[wrappedSeg1],
                            (double) seg, (double) (seg + 1));
                }

                for (int seg = 0; seg < ePolyParam.index; seg++) {
                    int wrappedSeg1 = (seg + 1) % nPoints();
                    doit(points[seg], points[wrappedSeg1],
                            (double) seg, (double) (seg + 1));
                }

                sPoint = ePolyParam.sp;
                ePoint = coordinates(ePolyParam.param);
                doit(sPoint, ePoint,
                        (double) ePolyParam.index, ePolyParam.param);
            } else if (sPolyParam.index == ePolyParam.index) {
                allocate(1);

                sPoint = coordinates(sPolyParam.param);
                ePoint = coordinates(ePolyParam.param);
                doit(sPoint, ePoint, sPolyParam.param, ePolyParam.param);
            } else {
                allocate(ePolyParam.index - sPolyParam.index + 1);

                sPoint = coordinates(sPolyParam.param);
                ePoint = sPolyParam.ep;
                doit(sPoint, ePoint,
                        sPolyParam.param, (double) sPolyParam.index + 1);

                for (int seg = sPolyParam.index + 1;
                     seg < ePolyParam.index; seg++)
                    doit(points[seg], points[seg + 1],
                            (double) seg, (double) (seg + 1));

                sPoint = ePolyParam.sp;
                ePoint = coordinates(ePolyParam.param);
                doit(sPoint, ePoint,
                        (double) ePolyParam.index, ePolyParam.param);
            }
        }
    }

    /**
     * {@link #length(ParameterSection) length(ParameterSection)}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class LengthAccumulator extends LineSegmentAccumulator {
        /**
         * ��?�̎w��̋�Ԃ̒���?B
         */
        double leng;

        /**
         * leng �� 0 ��?�����?B
         *
         * @param nsegs ��?ۂƂȂ�Z�O�?���g��?�
         */
        void allocate(int nsegs) {
            leng = 0.0;
        }

        /**
         * �^����ꂽ��_�Ԃ̋����� leng �ɑ���?B
         *
         * @param sp     �J�n�_
         * @param ep     ?I���_
         * @param sParam sp �ɑΉ�����p���??[�^�l
         * @param eParam ep �ɑΉ�����p���??[�^�l
         */
        void doit(Point2D sp, Point2D ep,
                  double sParam, double eParam) {
            leng += sp.distance(ep);
        }

        /**
         * ��?�̎w��̋�Ԃ̒�����Ԃ�?B
         *
         * @param leng �̒l
         */
        double extract() {
            return leng;
        }
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
        if (pint.increase() < 0.0) {
            return length(pint.reverse());
        }

        LengthAccumulator acc = new LengthAccumulator();

        acc.accumulate(pint);
        return acc.extract();
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
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public Point2D coordinates(double param) {
        PolyParam p = checkParameter(param);
        return p.ep.linearInterpolate(p.sp, p.weight);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public Vector2D tangentVector(double param) {
        PolyParam p = checkParameter(param);
        return p.ep.subtract(p.sp);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     * <p/>
     * �|�����C���̋ȗ���?A?�� 0 �ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public CurveCurvature2D curvature(double param) {
        checkParameter(param);
        return new CurveCurvature2D(0.0, Vector2D.zeroVector());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ����?�
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public CurveDerivative2D evaluation(double param) {
        return new CurveDerivative2D(coordinates(param),
                tangentVector(param),
                Vector2D.zeroVector());
    }

    /**
     * {@link #singular() singular()}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class SingularAccumulator extends LineSegmentAccumulator {
        private ParametricCurve2D curve;
        private Vector singularVec;
        private Vector2D prevTangVec;

        SingularAccumulator(ParametricCurve2D curve) {
            this.curve = curve;
        }

        void allocate(int nsegs) {
            singularVec = new Vector();
            prevTangVec = null;
        }

        void doit(Point2D sp, Point2D ep,
                  double sParam, double eParam) {
            Vector2D tangVec = ep.subtract(sp);
            if (prevTangVec != null) {
                if (!tangVec.identicalDirection(prevTangVec)) {
                    PointOnCurve2D candidate =
                            new PointOnCurve2D(curve, sParam, doCheckDebug);
                    singularVec.addElement(candidate);
                }
            }
            prevTangVec = tangVec;
        }

        PointOnCurve2D[] extract() {
            PointOnCurve2D[] singular =
                    new PointOnCurve2D[singularVec.size()];
            singularVec.copyInto(singular);
            return singular;
        }
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * �Hٓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     */
    public PointOnCurve2D[] singular() {
        SingularAccumulator acc = new SingularAccumulator(this);
        acc.accumulate(parameterDomain().section());

        return acc.extract();
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * �|�����C���ɂ͕ϋȓ_�͑�?݂��Ȃ���̂Ƃ���?A���� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     */
    public PointOnCurve2D[] inflexion() {
        return new PointOnCurve2D[0];
    }

    /**
     * {@link #projectFrom(Point2D) projectFrom(Point2D)}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class ProjectionAccumulator extends LineSegmentAccumulator {
        PointOnGeometryList projList;
        Point2D point;
        double dTol;
        Polyline2D curv;

        ProjectionAccumulator(Polyline2D curv, Point2D point,
                              double dTol) {
            this.point = point;
            this.dTol = dTol;
            this.curv = curv;
        }

        void allocate(int nsegs) {
            projList = new PointOnGeometryList();
        }

        void doit(Point2D sp, Point2D ep,
                  double sParam, double eParam) {
            Line2D line;
            try {
                line = new Line2D(sp, ep);
            } catch (InvalidArgumentValueException e) {    // segment is reduced
                return;
            }
            PointOnCurve2D proj = line.project1From(point);
            double length = line.dir().length();
            double param = proj.parameter();
            double fromSp = param * length;

            if (-dTol <= fromSp && fromSp <= length + dTol) {
                // parameter on Polyline
                if (param < 0.0)
                    param = 0.0;
                else if (param > 1.0) param = 1.0;
                double p2 = sParam + (eParam - sParam) * param;
                projList.addPoint(curv, p2);
            }
        }

        PointOnCurve2D[] extract() {
            return projList.toPointOnCurve2DArray();
        }
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     */
    public PointOnCurve2D[] projectFrom(Point2D point) {
        double dTol = getToleranceForDistance();

        ProjectionAccumulator acc =
                new ProjectionAccumulator(this, point, dTol);

        try {
            acc.accumulate(parameterDomain().section());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
        return acc.extract();
    }

    /**
     * {@link #toPolyline(ParameterSection,ToleranceForDistance)
     * toPolyline(ParameterSection, ToleranceForDistance)}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class ToPolylineAccumulator extends LineSegmentAccumulator {
        Vector pntVec;
        Point2D lastPoint;
        Polyline2D curv;

        ToPolylineAccumulator(Polyline2D curv) {
            this.curv = curv;
        }

        void allocate(int nsegs) {
            pntVec = new Vector();
            lastPoint = null;
        }

        void doit(Point2D sp, Point2D ep,
                  double sParam, double eParam) {
            if (lastPoint == null) {
                lastPoint = new PointOnCurve2D(curv, sParam, doCheckDebug);
                pntVec.addElement(lastPoint);
            }

            Point2D newPoint = new PointOnCurve2D(curv, eParam, doCheckDebug);
            if (!newPoint.identical(lastPoint)) {
                pntVec.addElement(newPoint);
                lastPoint = newPoint;
            }
        }

        Polyline2D extract() {
            int nPnts = pntVec.size();
            if (nPnts < 2)
                throw new ZeroLengthException();

            Point2D[] pntsArray = new Point2D[nPnts];
            pntVec.copyInto(pntsArray);
            return new Polyline2D(pntsArray);
        }
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve2D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     */
    public Polyline2D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        if (pint.increase() < 0.0) {
            return toPolyline(pint.reverse(), tol).reverse();
        }

        ToPolylineAccumulator acc = new ToPolylineAccumulator(this);

        acc.accumulate(pint);
        return acc.extract();
    }

    /**
     * ���̗L��?�S�̂쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @return ���̋�?�S�̂�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve2D toBsplineCurve() {
        int degree = 1;
        boolean periodic = this.closed();
        int uicp = this.nPoints();
        int uik = (periodic == false) ? uicp : (uicp + 2);
        int[] knotMultiplicities = new int[uik];
        double[] knots = new double[uik];
        Point2D[] controlPoints = new Point2D[uicp];
        double[] weights = new double[uicp];

        int ik = (periodic == false) ? 0 : 1;
        if (periodic == false) {
            ik = 0;
        } else {
            ik = 1;
            knots[0] = (-1.0);
            knots[uik - 1] = uicp + 1;
            knotMultiplicities[0] = 1;
            knotMultiplicities[uik - 1] = 1;
        }
        for (int i = 0; i < uicp; i++, ik++) {
            knots[ik] = i;
            if ((periodic == false) &&
                    ((i == 0) || (i == (this.nPoints() - 1))))
                knotMultiplicities[ik] = 2;
            else
                knotMultiplicities[ik] = 1;

            controlPoints[i] = this.pointAt(i);
            weights[i] = 1.0;
        }

        return new BsplineCurve2D(degree, periodic,
                knotMultiplicities, knots,
                controlPoints,
                weights);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        Polyline2D target;
        if (this.closed() == true) {
            if (pint.absIncrease() >= this.parameterDomain().section().absIncrease()) {
                target = this;
                if (pint.increase() < 0.0)
                    target = target.reverse();
            } else {
                target = this.toPolyline(pint, this.getToleranceForDistanceAsObject());
            }
        } else {
            target = this.toPolyline(pint, this.getToleranceForDistanceAsObject());
        }
        return target.toBsplineCurve();
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate) {
        return mate.intersect(this, true);
    }

    /**
     * ���̃|�����C���̎w��̃Z�O�?���g�̒�����?�߂�?B
     *
     * @param nseg   �Z�O�?���g��?�
     * @param segIdx ������?�߂�Z�O�?���g�̔�?� (0 �x?[�X)
     * @return �w��̃Z�O�?���g�̒���
     */
    private double segLength(int nseg, int segIdx) {
        int head_pnt_idx;
        int tail_pnt_idx;

        if (closed()) {
            while (segIdx < 0)
                segIdx += nseg;
            while (segIdx > (nseg - 1))
                segIdx -= nseg;
        }

        head_pnt_idx = segIdx;

        if (closed() && (head_pnt_idx == nSegments())) {
            tail_pnt_idx = 0;
        } else {
            tail_pnt_idx = head_pnt_idx + 1;
        }

        return points[head_pnt_idx].distance(points[tail_pnt_idx]);
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li>	�e�Z�O�?���g�ɂ���?A���[�_��ʂ钼?��?l��?A���̒�?�Ƒ��̋�?�Ƃ̌�_��?�߂�?B
     * <li>	?��?��?�ŋ?�߂���_�̓��?A�Ή�����Z�O�?���g�̓Ք�ɂ����̂�?u��_?v�Ƃ���?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋�?�
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    private IntersectionPoint2D[] doIntersect(ParametricCurve2D mate, boolean doExchange) {
        int nSeg = nSegments();
        CurveCurveInterferenceList intf =
                new CurveCurveInterferenceList(this, mate);

        for (int i = 0; i < nSeg; i++) {
            Line2D realSegment;
            BoundedLine2D segment;
            try {
                realSegment = new Line2D(pointAt(i), pointAt(i + 1));
                segment = new BoundedLine2D(pointAt(i), pointAt(i + 1));
            } catch (InvalidArgumentValueException e) {    // segment is reduced
                continue;
            }

            IntersectionPoint2D[] segIntersect;
            try {
                segIntersect = realSegment.intersect(mate);
            } catch (IndefiniteSolutionException e) {
                // �Z�O�?���g�̒��_��ǉB��Ă���
                segIntersect = new IntersectionPoint2D[1];
                Point2D segmentMidPoint = realSegment.coordinates(0.5);
                double paramOnMate = mate.pointToParameter(segmentMidPoint);
                segIntersect[0] =
                        new IntersectionPoint2D(realSegment, 0.5,
                                mate, paramOnMate, doCheckDebug);
            }
            if ((segIntersect == null) || (segIntersect.length == 0))
                continue;

            int segResolution = segIntersect.length;
            for (int j = 0; j < segResolution; j++) {
                Point2D point = segIntersect[j].coordinates();
                double segParam = segIntersect[j].pointOnCurve1().parameter();
                int validity = segment.parameterValidity(segParam);

                switch (validity) {
                    case ParameterValidity.OUTSIDE:
                        continue;
                    case ParameterValidity.TOLERATED_LOWER_LIMIT:
                        if (segParam < 0.0) {
                            if ((!closed()) && (i == 0)) {
                                segParam = 0.0;
                            } else {
                                segParam = segLength(nSeg, i) * segParam / segLength(nSeg, (i - 1));
                            }
                        }
                        break;
                    case ParameterValidity.TOLERATED_UPPER_LIMIT:
                        if (segParam > 1.0) {
                            if ((!closed()) && (i == (nSeg - 1))) {
                                segParam = 1.0;
                            } else {
                                segParam = 1.0 + (segLength(nSeg, i) * (segParam - 1.0)) / segLength(nSeg, (i + 1));
                            }
                        }
                        break;
                    default: // ParameterValidity.PROPERLY_INSIDE
                        break;
                }
                intf.addAsIntersection(point, segParam + i,
                        segIntersect[j].pointOnCurve2().parameter());
            }
        }

        return intf.toIntersectionPoint2DArray(doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * this.{@link #doIntersect(ParametricCurve2D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * this.{@link #doIntersect(ParametricCurve2D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * this.{@link #doIntersect(ParametricCurve2D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * this.{@link #doIntersect(ParametricCurve2D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * this.{@link #doIntersect(ParametricCurve2D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsPolPol2D#intersection(Polyline2D,Polyline2D,boolean)
     * IntsPolPol2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return IntsPolPol2D.intersection(this, mate, doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * this.{@link #doIntersect(ParametricCurve2D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * this.{@link #doIntersect(ParametricCurve2D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �g������?�̃N���X��?u�g������?� vs. �|�����C��?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve2D#intersect(Polyline2D,boolean)
     * TrimmedCurve2D.intersect(Polyline2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �|�����C��?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment2D#intersect(Polyline2D,boolean)
     * CompositeCurveSegment2D.intersect(Polyline2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�̃N���X��?u��?���?� vs. �|�����C��?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve2D#intersect(Polyline2D,boolean)
     * CompositeCurve2D.intersect(Polyline2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L�E��?�̊�?�?�߂�?B
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
     * ���̗L��?�Ƒ��̗L��?� (?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ?�̃N���X��?u?� vs. �|�����C��?v�̊�?��Z�?�\�b�h
     * {@link BoundedLine2D#interfere(Polyline2D,boolean)
     * BoundedLine2D.interfere(Polyline2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(BoundedLine2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�|�����C��) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsPolPol2D#interference(Polyline2D,Polyline2D)
     * IntsPolPol2D.interference}(this, mate)
     * �µ����
     * IntsPolPol2D.interference(mate, this)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ?�?G?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(Polyline2D mate,
                                         boolean doExchange) {
        if (!doExchange) {
            return IntsPolPol2D.interference(this, mate);
        } else {
            return IntsPolPol2D.interference(mate, this);
        }
    }

    /**
     * �^����ꂽ��?̑�?ۂƂȂ��?�ⱂ̃|�����C���ɕ�?X����?B
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
     * ���̗L��?�Ƒ��̗L��?� (�x�W�G��?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̃|�����C����L�?�a�X�v���C����?�ɕϊ���?A
     * �a�X�v���C����?�̃N���X��?u�a�X�v���C����?� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link BsplineCurve2D#interfere(PureBezierCurve2D,boolean)
     * BsplineCurve2D.interfere(PureBezierCurve2D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�x�W�G��?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
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
     * ���̃|�����C����L�?�a�X�v���C����?�ɕϊ���?A
     * �a�X�v���C����?�̃N���X��?u�a�X�v���C����?� vs. �a�X�v���C����?�?v�̊�?��Z�?�\�b�h
     * {@link BsplineCurve2D#interfere(BsplineCurve2D,boolean)
     * BsplineCurve2D.interfere(BsplineCurve2D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�a�X�v���C����?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
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
     * �g������?�̃N���X��?u�g������?� vs. �|�����C��?v�̊�?��Z�?�\�b�h
     * {@link TrimmedCurve2D#interfere(Polyline2D,boolean)
     * TrimmedCurve2D.interfere(Polyline2D, boolean)}
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
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �|�����C��?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurveSegment2D#interfere(Polyline2D,boolean)
     * CompositeCurveSegment2D.interfere(Polyline2D, boolean)}
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
     * ��?���?�̃N���X��?u��?���?� vs. �|�����C��?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurve2D#interfere(Polyline2D,boolean)
     * CompositeCurve2D.interfere(Polyline2D, boolean)}
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
     * ���̃|�����C���̎��Ȍ�?��_��?�߂�?B
     * <p/>
     * ���Ȍ�?�����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return ���Ȍ�?��_�̔z��
     */
    public IntersectionPoint2D[] selfIntersect() {
        return SelfIntsPol2D.intersection(this);
    }

    /**
     * ���̃|�����C���̎��Ȋ�?�?�߂�?B
     * <p/>
     * ���Ȋ�?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return ���Ȋ�?̔z��
     */
    public CurveCurveInterference2D[] selfInterfere() {
        return SelfIntsPol2D.interference(this);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ����镡?���?��?�߂�?B
     * <p/>
     * ���ʂƂ��ē����镡?���?�͂��̃|�����C���̃I�t�Z�b�g��?��?�Ɍ�����?Č�����?B
     * ��B�?A���̃?�\�b�h�̓Ք�ł� tol �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ����镡?���?�
     * @see WhichSide
     */
    public CompositeCurve2D
    offsetByCompositeCurve(ParameterSection pint,
                           double magni,
                           int side,
                           ToleranceForDistance tol) {
        boolean offsettedIsPeriodic = false;
        if (this.isPeriodic() == true) {
            if ((this.parameterDomain().section().absIncrease() - pint.absIncrease()) <
                    this.getToleranceForParameter())
                offsettedIsPeriodic = true;
        }

        BoundedLine2D[] boundedLines = this.toBoundedLines(pint);
        int nBoundedLines = boundedLines.length;

        CompositeCurveSegment2D[] offsetted =
                new CompositeCurveSegment2D[2 * nBoundedLines];

        BoundedLine2D prevOffsettedCurve = null;
        BoundedLine2D crntOffsettedCurve = null;
        BoundedLine2D firstOffsettedCurve = null;

        int transition;

        for (int i = 0; i <= nBoundedLines; i++) {
            /*
            * offset the curve
            */
            if (i == nBoundedLines) {
                crntOffsettedCurve = firstOffsettedCurve;
            } else {
                crntOffsettedCurve = (BoundedLine2D)
                        boundedLines[i].offsetByBoundedCurve(magni, side, tol);
                if (i == 0)
                    firstOffsettedCurve = crntOffsettedCurve;

                transition = TransitionCode.CONTINUOUS;
                if ((offsettedIsPeriodic == false) && (i == (nBoundedLines - 1)))
                    transition = TransitionCode.DISCONTINUOUS;

                offsetted[2 * i] =
                        new CompositeCurveSegment2D(transition, true,
                                crntOffsettedCurve);
            }

            /*
            * offset the corner
            */
            if (i == 0) {
                // start point at first offsetted segment
                ; // do nothing
            } else if ((i == nBoundedLines) && (offsettedIsPeriodic == false)) {
                // start point at first offsetted segment, but offsetted is open
                offsetted[2 * i - 1] = null;
            } else if (prevOffsettedCurve.epnt().identical(crntOffsettedCurve.spnt()) == true) {
                // end point at prev. and start point at crnt. are identical
                offsetted[2 * i - 1] = null;
            } else {
                Point2D center = (i < nBoundedLines)
                        ? boundedLines[i].spnt() : boundedLines[0].spnt();
                TrimmedCurve2D offsettedCorner =
                        Circle2D.makeTrimmedCurve(center,
                                prevOffsettedCurve.epnt(),
                                crntOffsettedCurve.spnt());
                transition = TransitionCode.CONTINUOUS;

                offsetted[2 * i - 1] =
                        new CompositeCurveSegment2D(transition, true,
                                offsettedCorner);
            }

            prevOffsettedCurve = crntOffsettedCurve;
        }

        Vector listOfOffsetted = new Vector();
        for (int i = 0; i < (2 * nBoundedLines); i++)
            if (offsetted[i] != null)
                listOfOffsetted.addElement(offsetted[i]);

        offsetted = new CompositeCurveSegment2D[listOfOffsetted.size()];
        listOfOffsetted.copyInto(offsetted);

        return new CompositeCurve2D(offsetted, offsettedIsPeriodic);
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
        CompositeCurve2D cmc =
                this.offsetByCompositeCurve(pint, magni, side, tol);

        return cmc.toBsplineCurve();
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
        return this.offsetByCompositeCurve(pint, magni, side, tol);
    }

    /**
     * {@link #doFillet(ParameterSection,int,ParametricCurve2D,ParameterSection,int,double,boolean)
     * doFillet(ParameterSection, int, ParametricCurve2D, ParameterSection, int, double, boolean)}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class FilletAccumulator extends LineSegmentAccumulator {
        ParametricCurve2D mate;
        Polyline2D curve;
        ParameterSection mateSec;
        int mateSide;
        int mySide;
        double radius;
        boolean doExchange;
        FilletObjectList fltList;
        ParameterSection sec;    // staic value

        FilletAccumulator(ParametricCurve2D mate,
                          ParameterSection mateSec,
                          int mateSide,
                          Polyline2D curve,
                          int mySide,
                          double radius,
                          boolean doExchange) {
            this.curve = curve;
            this.mate = mate;
            this.mateSec = mateSec;
            this.mateSide = mateSide;
            this.mySide = mySide;
            this.radius = radius;
            this.doExchange = doExchange;
            sec = new ParameterSection(0.0, 1.0);
        }

        void allocate(int nsegs) {
            fltList = new FilletObjectList();
        }

        void doit(Point2D sp, Point2D ep,
                  double sParam, double eParam) {
            BoundedLine2D blin = new BoundedLine2D(sp, ep);
            FilletObject2D[] flts;
            try {
                flts = blin.doFillet(sec, mySide, mate, mateSec, mateSide, radius, false);
            } catch (IndefiniteSolutionException e) {
                flts = new FilletObject2D[1];
                flts[0] = (FilletObject2D) e.suitable();
            }
            FilletObject2D thisFlt;

            for (int i = 0; i < flts.length; i++) {
                double param = sParam + (eParam - sParam) * flts[i].pointOnCurve1().parameter();

                PointOnCurve2D thisPnt = new PointOnCurve2D(curve, param, doCheckDebug);
                if (!doExchange)
                    thisFlt = new FilletObject2D(radius, flts[i].center(), thisPnt, flts[i].pointOnCurve2());
                else
                    thisFlt = new FilletObject2D(radius, flts[i].center(), flts[i].pointOnCurve2(), thisPnt);
                fltList.addFillet(thisFlt);
            }
        }

        FilletObject2D[] extract() {
            return fltList.toFilletObject2DArray(false);
        }
    }

    /**
     * ���̃|�����C���̎w��̋�ԂƑ��̋�?�̎w��̋�Ԃɂ�����t�B���b�g��?�߂�?B
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
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     * @see WhichSide
     */
    FilletObject2D[]
    doFillet(ParameterSection pint1, int side1, ParametricCurve2D mate,
             ParameterSection pint2, int side2, double radius,
             boolean doExchange)
            throws IndefiniteSolutionException {
        FilletAccumulator acc =
                new FilletAccumulator(mate, pint2, side2, this, side1, radius, doExchange);
        acc.accumulate(pint1);
        return acc.extract();
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
     * ���̃|�����C���𔽓]�����|�����C����Ԃ�?B
     *
     * @return ���]�����|�����C��
     */
    Polyline2D reverse() {
        int uip = nPoints();
        Point2D[] rPnts = new Point2D[uip];
        int i, j;

        if (closed) {
            rPnts[0] = points[0];
            i = 1;
        } else {
            i = 0;
        }
        for (j = uip - 1; i < uip; i++, j--) {
            rPnts[i] = points[j];
        }
        return new Polyline2D(rPnts, closed);
    }

    /**
     * ���̃|�����C���̑�?ݔ͈͂���`��Ԃ�?B
     *
     * @return ��?ݔ͈͂���`
     */
    EnclosingBox2D enclosingBox() {
        double min_crd_x;
        double min_crd_y;
        double max_crd_x;
        double max_crd_y;

        max_crd_x = min_crd_x = pointAt(0).x();
        max_crd_y = min_crd_y = pointAt(0).y();

        for (int i = 1; i < nPoints(); i++) {
            min_crd_x = Math.min(min_crd_x, pointAt(i).x());
            min_crd_y = Math.min(min_crd_y, pointAt(i).y());
            max_crd_x = Math.max(max_crd_x, pointAt(i).x());
            max_crd_y = Math.max(max_crd_y, pointAt(i).y());
        }
        return new EnclosingBox2D(min_crd_x, min_crd_y, max_crd_x, max_crd_y);
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     *
     * @return �p���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        double n = closed ? points.length : points.length - 1;

        try {
            return new ParameterDomain(closed, 0, n);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
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
     * ���̗L��?�̊J�n�_��Ԃ�?B
     * <p/>
     * ��?�����`����?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return �J�n�_
     */
    public Point2D startPoint() {
        if (isPeriodic())
            return null;

        return points[0];
    }

    /**
     * ���̗L��?��?I���_��Ԃ�?B
     * <p/>
     * ��?�����`����?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return ?I���_
     */
    public Point2D endPoint() {
        if (isPeriodic())
            return null;

        return points[points.length - 1];
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#POLYLINE_2D ParametricCurve2D.POLYLINE_2D}
     */
    int type() {
        return POLYLINE_2D;
    }

    /**
     * {@link #toBoundedLines(ParameterSection)
     * toBoundedLines(ParameterSection)}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class ToBoundedLinesAccumulator extends LineSegmentAccumulator {
        BoundedLine2D[] boundedLines;
        int index;
        boolean rvrs;

        ToBoundedLinesAccumulator(boolean reverse) {
            rvrs = reverse;
        }

        void allocate(int nsegs) {
            boundedLines = new BoundedLine2D[nsegs];
            if (rvrs == false)
                index = 0;
            else
                index = nsegs - 1;
        }

        void doit(Point2D sp, Point2D ep,
                  double sParam, double eParam) {
            if (rvrs == false)
                boundedLines[index++] = new BoundedLine2D(sp, ep, false);
            else
                boundedLines[index--] = new BoundedLine2D(ep, sp, false);
        }

        BoundedLine2D[] extract() {
            return boundedLines;
        }
    }

    /**
     * ���̃|�����C���̎w��̋�Ԃ�?��ɕϊ�����?B
     *
     * @param pint ?��ɕϊ�����p���??[�^���
     * @return �w�肳�ꂽ��Ԃ�ϊ�����?��
     */
    public BoundedLine2D[] toBoundedLines(ParameterSection pint) {
        ToBoundedLinesAccumulator acc =
                new ToBoundedLinesAccumulator(pint.increase() < 0.0);
        acc.accumulate(pint);
        return acc.extract();
    }

    /**
     * ���̃|�����C���S�̂�?��ɕϊ�����?B
     *
     * @return ��?�S�̂�ϊ�����?��
     */
    public BoundedLine2D[] toBoundedLines() {
        return this.toBoundedLines(this.parameterDomain().section());
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
        Point2D[] tPoints =
                Point2D.transform(this.points,
                        reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new Polyline2D(tPoints, this.closed);
    }

    /**
     * ���̋�?�|�����C���̕�����܂ނ��ۂ���Ԃ�?B
     *
     * @return ?�� true
     */
    protected boolean hasPolyline() {
        return true;
    }

    /**
     * ���̋�?�|�����C���̕��������łł��Ă��邩�ۂ���Ԃ�?B
     *
     * @return ?�� true
     */
    protected boolean isComposedOfOnlyPolylines() {
        return true;
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
        writer.println(indent_tab + "\tpoints");
        for (int i = 0; i < nPoints(); i++) {
            points[i].output(writer, indent + 2);
        }
        writer.println(indent_tab + "\tclosed\t" + closed);
        writer.println(indent_tab + "End");
    }
}
