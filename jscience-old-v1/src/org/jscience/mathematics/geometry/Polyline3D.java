/*
 * �R���� : �|�����C����\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Polyline3D.java,v 1.3 2007-10-21 21:08:18 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : �|�����C����\���N���X?B
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

public class Polyline3D extends BoundedCurve3D {
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
        Point3D sp;

        /**
         * �Z�O�?���g��?I���_
         */
        Point3D ep;

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
    private Point3D[] points;

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
    private void setPoints(Point3D[] points, boolean closed) {
        if (!closed && points.length < 2 ||
                closed && points.length < 3)
            throw new InvalidArgumentValueException();

        this.closed = closed;
        this.points = new Point3D[points.length];

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
    public Polyline3D(Point3D[] points, boolean closed) {
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
    public Polyline3D(Point3D[] points) {
        super();
        setPoints(points, false);
    }

    /**
     * �^����ꂽ�L��?��w��̋��e��?��Œ�?�ߎ������̂Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param curve ��?�
     * @param tol   �����̋��e��?�
     * @see BoundedCurve3D#toPolyline(ToleranceForDistance)
     */
    public Polyline3D(BoundedCurve3D curve,
                      ToleranceForDistance tol) {
        super();
        Polyline3D pl = curve.toPolyline(tol);
        this.points = pl.points;
        this.closed = pl.closed;
    }

    /**
     * �^����ꂽ��?�̎w��̋�Ԃ�w��̋��e��?��Œ�?�ߎ������̂Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param curve ��?�
     * @param pint  ��?�ߎ�����p���??[�^���
     * @param tol   �����̋��e��?�
     * @see ParametricCurve3D#toPolyline(ParameterSection,ToleranceForDistance)
     */
    public Polyline3D(ParametricCurve3D curve,
                      ParameterSection pint,
                      ToleranceForDistance tol) {
        super();
        Polyline3D pl = curve.toPolyline(pint, tol);
        this.points = pl.points;
        this.closed = pl.closed;
    }

    /**
     * ���̃|�����C����?ߓ_�̔z���Ԃ�?B
     *
     * @return ?ߓ_�̔z��
     */
    public Point3D[] points() {
        Point3D[] pnts = new Point3D[points.length];

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
    public Point3D pointAt(int i) {
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
        abstract void doit(Point3D sp, Point3D ep,
                           double sParam, double eParam);

        /**
         * ���炩��?��?��n�߂邽�߂�?����?s�Ȃ���?ۃ?�\�b�h?B
         * <p/>
         * ���̃?�\�b�h��
         * {@link #accumulate(ParameterSection) accumulate(ParameterSection)}
         * �̒���
         * {@link #doit(Point3D,Point3D,double,double)
         * doit(Point3D, Point3D, double, double)}
         * ��Ă�?o���O�ɌĂ�?o�����?B
         * </p>
         */
        abstract void allocate(int nsegs);

        /**
         * �^����ꂽ�p���??[�^��ԂɊ܂܂��Z�O�?���g����
         * {@link #doit(Point3D,Point3D,double,double)
         * doit(Point3D, Point3D, double, double)}
         * ��Ă�?o��?B
         */
        void accumulate(ParameterSection pint) {
            PolyParam sPolyParam = checkParameter(pint.start());
            PolyParam ePolyParam = checkParameter(pint.end());
            Point3D sPoint;
            Point3D ePoint;
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
        void doit(Point3D sp, Point3D ep,
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
            return length(new ParameterSection(pint.end(),
                    -pint.increase()));
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
    public Point3D coordinates(double param) {
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
    public Vector3D tangentVector(double param) {
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
    public CurveCurvature3D curvature(double param) {
        checkParameter(param);

        return new CurveCurvature3D(0.0, Vector3D.zeroVector());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̃��C����Ԃ�?B
     * <p/>
     * �|�����C���̃��C����?A?�� 0 �ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ���C��
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public double torsion(double param) {
        checkParameter(param);
        return 0.0;
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
    public CurveDerivative3D evaluation(double param) {
        return new CurveDerivative3D(coordinates(param),
                tangentVector(param),
                Vector3D.zeroVector(),
                Vector3D.zeroVector());
    }

    /**
     * {@link #singular() singular()}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class SingularAccumulator extends LineSegmentAccumulator {
        private ParametricCurve3D curve;
        private Vector singularVec;
        private Vector3D prevTangVec;

        SingularAccumulator(ParametricCurve3D curve) {
            this.curve = curve;
        }

        void allocate(int nsegs) {
            singularVec = new Vector();
            prevTangVec = null;
        }

        void doit(Point3D sp, Point3D ep,
                  double sParam, double eParam) {
            Vector3D tangVec = ep.subtract(sp);
            if (prevTangVec != null) {
                if (!tangVec.identicalDirection(prevTangVec)) {
                    PointOnCurve3D candidate =
                            new PointOnCurve3D(curve, sParam, doCheckDebug);
                    singularVec.addElement(candidate);
                }
            }
            prevTangVec = tangVec;
        }

        PointOnCurve3D[] extract() {
            PointOnCurve3D[] singular =
                    new PointOnCurve3D[singularVec.size()];
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
    public PointOnCurve3D[] singular() {
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
    public PointOnCurve3D[] inflexion() {
        return new PointOnCurve3D[0];
    }

    /**
     * {@link #projectFrom(Point3D) projectFrom(Point3D)}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class ProjectionAccumulator extends LineSegmentAccumulator {
        PointOnGeometryList projList;
        Point3D point;
        double dTol;
        Polyline3D curv;

        ProjectionAccumulator(Polyline3D curv, Point3D point,
                              double dTol) {
            this.point = point;
            this.dTol = dTol;
            this.curv = curv;
        }

        void allocate(int nsegs) {
            projList = new PointOnGeometryList();
        }

        void doit(Point3D sp, Point3D ep,
                  double sParam, double eParam) {
            Line3D line;
            try {
                line = new Line3D(sp, ep);
            } catch (InvalidArgumentValueException e) {    // segment is reduced
                return;
            }
            PointOnCurve3D proj = line.project1From(point);
            double length = line.dir().length();
            double param = proj.parameter();
            double fromSp = param * length;

            if (-dTol <= fromSp && fromSp <= length + dTol) {
                // parameter on Polyline
                if (param < 0.0)
                    param = 0.0;
                else if (param > 1.0) param = 1.0;
                double p2 = sParam + (eParam - sParam) * param;
                PointOnCurve3D proj2 = new PointOnCurve3D(curv, p2, doCheckDebug);
                projList.addPoint(curv, p2);
            }
        }

        PointOnCurve3D[] extract() {
            return projList.toPointOnCurve3DArray();
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
    public PointOnCurve3D[] projectFrom(Point3D point) {
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
        Point3D lastPoint;
        Polyline3D curv;

        ToPolylineAccumulator(Polyline3D curv) {
            this.curv = curv;
        }

        void allocate(int nsegs) {
            pntVec = new Vector();
            lastPoint = null;
        }

        void doit(Point3D sp, Point3D ep,
                  double sParam, double eParam) {
            if (lastPoint == null) {
                lastPoint = new PointOnCurve3D(curv, sParam, doCheckDebug);
                pntVec.addElement(lastPoint);
            }

            Point3D newPoint = new PointOnCurve3D(curv, eParam, doCheckDebug);
            if (!newPoint.identical(lastPoint)) {
                pntVec.addElement(newPoint);
                lastPoint = newPoint;
            }
        }

        Polyline3D extract() {
            int nPnts = pntVec.size();
            if (nPnts < 2)
                throw new ZeroLengthException();

            Point3D[] pntsArray = new Point3D[nPnts];
            pntVec.copyInto(pntsArray);
            return new Polyline3D(pntsArray);
        }
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     */
    public Polyline3D toPolyline(ParameterSection pint,
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
    public BsplineCurve3D toBsplineCurve() {
        int degree = 1;
        boolean periodic = this.closed();
        int uicp = this.nPoints();
        int uik = (periodic == false) ? uicp : (uicp + 2);
        int[] knotMultiplicities = new int[uik];
        double[] knots = new double[uik];
        Point3D[] controlPoints = new Point3D[uicp];
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

        return new BsplineCurve3D(degree, periodic,
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
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
        Polyline3D target;
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
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
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
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    private IntersectionPoint3D[] doIntersect(ParametricCurve3D mate, boolean doExchange) {
        int nSeg = nSegments();
        CurveCurveInterferenceList intf =
                new CurveCurveInterferenceList(this, mate);

        for (int i = 0; i < nSeg; i++) {
            Line3D realSegment;
            BoundedLine3D segment;
            try {
                realSegment = new Line3D(pointAt(i), pointAt(i + 1));
                segment = new BoundedLine3D(pointAt(i), pointAt(i + 1));
            } catch (InvalidArgumentValueException e) {    // segment is reduced
                continue;
            }

            IntersectionPoint3D[] segIntersect;
            try {
                segIntersect = realSegment.intersect(mate);
            } catch (IndefiniteSolutionException e) {
                // �Z�O�?���g�̒��_��ǉB��Ă���
                segIntersect = new IntersectionPoint3D[1];
                Point3D segmentMidPoint = realSegment.coordinates(0.5);
                double paramOnMate = mate.pointToParameter(segmentMidPoint);
                segIntersect[0] =
                        new IntersectionPoint3D(realSegment, 0.5,
                                mate, paramOnMate, doCheckDebug);
            }
            if ((segIntersect == null) || (segIntersect.length == 0))
                continue;

            int segResolution = segIntersect.length;
            for (int j = 0; j < segResolution; j++) {
                Point3D point = segIntersect[j].coordinates();
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
        return intf.toIntersectionPoint3DArray(doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * this.{@link #doIntersect(ParametricCurve3D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
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
     * this.{@link #doIntersect(ParametricCurve3D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Circle3D mate, boolean doExchange) {
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
     * this.{@link #doIntersect(ParametricCurve3D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange) {
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
     * this.{@link #doIntersect(ParametricCurve3D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange) {
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
     * this.{@link #doIntersect(ParametricCurve3D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Hyperbola3D mate, boolean doExchange) {
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
     * {@link IntsPolPol3D#intersection(Polyline3D,Polyline3D,boolean)
     * IntsPolPol3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Polyline3D mate, boolean doExchange) {
        return IntsPolPol3D.intersection(this, mate, doExchange);
    }

    /**
     * ���̃|�����C���Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * this.{@link #doIntersect(ParametricCurve3D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
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
     * this.{@link #doIntersect(ParametricCurve3D,boolean)
     * doIntersect}(mate, doExchange) ��Ă�ł���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
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
     * {@link TrimmedCurve3D#intersect(Polyline3D,boolean)
     * TrimmedCurve3D.intersect(Polyline3D, boolean)}
     * ��?s�ȂBĂ���?B
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
     * ���̃|�����C���Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �|�����C��?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment3D#intersect(Polyline3D,boolean)
     * CompositeCurveSegment3D.intersect(Polyline3D, boolean)}
     * ��?s�ȂBĂ���?B
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
     * ���̃|�����C���Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�N���X��?u��?���?� vs. �|�����C��?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve3D#intersect(Polyline3D,boolean)
     * CompositeCurve3D.intersect(Polyline3D, boolean)}
     * ��?s�ȂBĂ���?B
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
     * {@link #doIntersect(ParametricSurface3D,boolean)
     * doIntersect(ParametricSurface3D, boolean)}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class IntersectWithSurfaceAccumulator extends LineSegmentAccumulator {
        Polyline3D curve;
        ParametricSurface3D surface;
        CurveSurfaceInterferenceList intfList;

        IntersectWithSurfaceAccumulator(Polyline3D curve,
                                        ParametricSurface3D surface) {
            this.curve = curve;
            this.surface = surface;
        }

        void allocate(int nsegs) {
            intfList = new CurveSurfaceInterferenceList(curve, surface);
        }

        void doit(Point3D sp, Point3D ep,
                  double sParam, double eParam) {
            Line3D line;
            try {
                line = new Line3D(sp, ep);
            } catch (InvalidArgumentValueException e) {    // segment is reduced
                return;
            }

            // remake tolerance
            double dTol = getToleranceForDistance();
            double segtol = Math.abs(line.dir().norm());
            if (segtol < dTol)
                segtol = -1.0;
            else
                segtol = dTol / segtol;

            IntersectionPoint3D[] intp;
            try {
                intp = line.intersect(surface);
            } catch (IndefiniteSolutionException e) {
                throw new FatalException();
            }

            for (int i = 0; i < intp.length; i++) {
                double crvParam =
                        ((PointOnCurve3D) intp[i].pointOnGeometry1()).parameter();
                double srfUParam =
                        ((PointOnSurface3D) intp[i].pointOnGeometry2()).uParameter();
                double srfVParam =
                        ((PointOnSurface3D) intp[i].pointOnGeometry2()).vParameter();

                // validation check

                // is_valid_ints method of GHL
                if ((crvParam < (0.0 - segtol)) || (crvParam > (1.0 + segtol)))
                    continue;
                if (crvParam < 0.0) crvParam = 0.0;
                if (crvParam > 1.0) crvParam = 1.0;
                crvParam += sParam;

                // GHL �ł�?Ais_isolate_ints method ���Ă΂�邪?A
                // JGCL �ł�?A���� add ����?ۂ�?A����?���`�F�b�N���Ă���̂�?A
                // ����?��?�͕K�v�Ȃ��Ǝv�BĂ���?B

                // add solution to List
                intfList.addAsIntersection(intp[i].coordinates(), crvParam,
                        srfUParam, srfVParam);
            }
        }

        IntersectionPoint3D[] extract(boolean doExchange) {
            return intfList.toIntersectionPoint3DArray(doExchange);
        }
    }

    /**
     * ���̋�?�Ƒ��̋Ȗʂ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li>	�e�Z�O�?���g�ɂ���?A���[�_��ʂ钼?��?l��?A���̒�?�Ƒ��̋ȖʂƂ̌�_��?�߂�?B
     * <li>	?��?��?�ŋ?�߂���_�̓��?A�Ή�����Z�O�?���g�̓Ք�ɂ����̂�?u��_?v�Ƃ���?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋Ȗ�
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] doIntersect(ParametricSurface3D mate,
                                      boolean doExchange) {
        IntersectWithSurfaceAccumulator acc =
                new IntersectWithSurfaceAccumulator(this, mate);
        acc.accumulate(parameterDomain().section());
        return acc.extract(doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗʂ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��_�̔z��
     */
    public IntersectionPoint3D[] intersect(ParametricSurface3D mate) {
        return doIntersect(mate, false);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗʂ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ�
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(ParametricSurface3D mate,
                                    boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (��?͋Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (��?͋Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(ElementarySurface3D mate,
                                    boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�x�W�G�Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(PureBezierSurface3D mate,
                                    boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(BsplineSurface3D mate,
                                    boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (?�) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ?�̃N���X��?u?� vs. �|�����C��?v�̊�?��Z�?�\�b�h
     * {@link BoundedLine3D#interfere(Polyline3D,boolean)
     * BoundedLine3D.interfere(Polyline3D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(BoundedLine3D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�|�����C��) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsPolPol3D#interference(Polyline3D,Polyline3D)
     * IntsPolPol3D.interference(Polyline3D, Polyline3D)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(Polyline3D mate,
                                         boolean doExchange) {
        if (!doExchange) {
            return IntsPolPol3D.interference(this, mate);
        } else {
            return IntsPolPol3D.interference(mate, this);
        }
    }

    /**
     * �^����ꂽ��?̑�?ۂƂȂ��?�ⱂ̃|�����C���ɕ�?X����?B
     *
     * @param sourceInterferences ��?̔z��
     * @param doExchange          ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    private CurveCurveInterference3D[]
    convertInterferences(CurveCurveInterference3D[] sourceInterferences,
                         boolean doExchange) {
        Vector resultVector = new Vector();

        for (int i = 0; i < sourceInterferences.length; i++) {
            CurveCurveInterference3D intf;
            if (!doExchange)
                intf = sourceInterferences[i].changeCurve1(this);
            else
                intf = sourceInterferences[i].changeCurve2(this);
            if (intf != null)
                resultVector.addElement(intf);
        }

        CurveCurveInterference3D[] result =
                new CurveCurveInterference3D[resultVector.size()];
        resultVector.copyInto(result);
        return result;
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�x�W�G��?�) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̃|�����C����L�?�a�X�v���C����?�ɕϊ���?A
     * �a�X�v���C����?�̃N���X��?u�a�X�v���C����?� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link BsplineCurve3D#interfere(PureBezierCurve3D,boolean)
     * BsplineCurve3D.interfere(PureBezierCurve3D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(PureBezierCurve3D mate,
                                         boolean doExchange) {
        return this.convertInterferences(this.toBsplineCurve().interfere(mate, doExchange),
                doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�a�X�v���C����?�) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̃|�����C����L�?�a�X�v���C����?�ɕϊ���?A
     * �a�X�v���C����?�̃N���X��?u�a�X�v���C����?� vs. �a�X�v���C����?�?v�̊�?��Z�?�\�b�h
     * {@link BsplineCurve3D#interfere(PureBezierCurve3D,boolean)
     * BsplineCurve3D.interfere(PureBezierCurve3D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(BsplineCurve3D mate,
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
     * {@link TrimmedCurve3D#interfere(Polyline3D,boolean)
     * TrimmedCurve3D.interfere(Polyline3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�g������?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(TrimmedCurve3D mate,
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
     * {@link CompositeCurveSegment3D#interfere(Polyline3D,boolean)
     * CompositeCurveSegment3D.interfere(Polyline3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�Z�O�?���g)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(CompositeCurveSegment3D mate,
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
     * {@link CompositeCurve3D#interfere(Polyline3D,boolean)
     * CompositeCurve3D.interfere(Polyline3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(CompositeCurve3D mate,
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
    public CurveCurveInterference3D[] interfere(BoundedCurve3D mate) {
        return mate.interfere(this, true);
    }

    /**
     * ���̃|�����C���̎��Ȍ�?��_��?�߂�?B
     * <p/>
     * ���Ȍ�?�����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return ���Ȍ�?��_�̔z��
     */
    public IntersectionPoint3D[] selfIntersect() {
        return SelfIntsPol3D.intersection(this);
    }

    /**
     * ���̃|�����C���̎��Ȋ�?�?�߂�?B
     * <p/>
     * ���Ȋ�?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return ���Ȋ�?̔z��
     */
    public CurveCurveInterference3D[] selfInterfere() {
        return SelfIntsPol3D.interference(this);
    }

    /**
     * ���̃|�����C���𔽓]�����|�����C����Ԃ�?B
     *
     * @return ���]�����|�����C��
     */
    Polyline3D reverse() {
        int uip = nPoints();
        Point3D[] rPnts = new Point3D[uip];
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
        return new Polyline3D(rPnts, closed);
    }

    /**
     * ���̃|�����C���̑�?ݔ͈͂�����̂�Ԃ�?B
     *
     * @return ��?ݔ͈͂������
     */
    EnclosingBox3D enclosingBox() {
        double min_crd_x;
        double min_crd_y;
        double min_crd_z;
        double max_crd_x;
        double max_crd_y;
        double max_crd_z;

        max_crd_x = min_crd_x = pointAt(0).x();
        max_crd_y = min_crd_y = pointAt(0).y();
        max_crd_z = min_crd_z = pointAt(0).z();

        for (int i = 1; i < nPoints(); i++) {
            min_crd_x = Math.min(min_crd_x, pointAt(i).x());
            min_crd_y = Math.min(min_crd_y, pointAt(i).y());
            min_crd_z = Math.min(min_crd_z, pointAt(i).z());
            max_crd_x = Math.max(max_crd_x, pointAt(i).x());
            max_crd_y = Math.max(max_crd_y, pointAt(i).y());
            max_crd_z = Math.max(max_crd_z, pointAt(i).z());
        }
        return new EnclosingBox3D(min_crd_x, min_crd_y, min_crd_z,
                max_crd_x, max_crd_y, max_crd_z);
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ�?B
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     */
    public ParametricCurve3D parallelTranslate(Vector3D moveVec) {
        Point3D[] pnts = new Point3D[nPoints()];
        for (int i = 0; i < nPoints(); i++)
            pnts[i] = pointAt(i).add(moveVec);
        return new Polyline3D(pnts, closed);
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
    public Point3D startPoint() {
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
    public Point3D endPoint() {
        if (isPeriodic())
            return null;

        return points[points.length - 1];
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve3D#POLYLINE_3D ParametricCurve3D.POLYLINE_3D}
     */
    int type() {
        return POLYLINE_3D;
    }

    /**
     * {@link #toBoundedLines(ParameterSection)
     * toBoundedLines(ParameterSection)}
     * ��?��?���邽�߂� LineSegmentAccumulator �̎�?B
     */
    private class ToBoundedLinesAccumulator extends LineSegmentAccumulator {
        BoundedLine3D[] boundedLines;
        int index;
        boolean rvrs;

        ToBoundedLinesAccumulator(boolean reverse) {
            rvrs = reverse;
        }

        void allocate(int nsegs) {
            boundedLines = new BoundedLine3D[nsegs];
            if (rvrs == false)
                index = 0;
            else
                index = nsegs - 1;
        }

        void doit(Point3D sp, Point3D ep,
                  double sParam, double eParam) {
            if (rvrs == false)
                boundedLines[index++] = new BoundedLine3D(sp, ep, false);
            else
                boundedLines[index--] = new BoundedLine3D(ep, sp, false);
        }

        BoundedLine3D[] extract() {
            return boundedLines;
        }
    }

    /**
     * ���̃|�����C���̎w��̋�Ԃ�?��ɕϊ�����?B
     *
     * @param pint ?��ɕϊ�����p���??[�^���
     * @return �w�肳�ꂽ��Ԃ�ϊ�����?��
     */
    public BoundedLine3D[] toBoundedLines(ParameterSection pint) {
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
    public BoundedLine3D[] toBoundedLines() {
        return this.toBoundedLines(this.parameterDomain().section());
    }

    /**
     * ���̋�?��?A�^����ꂽ��?�?W�n�� Z ���̎���?A
     * �^����ꂽ�p�x������]��������?��Ԃ�?B
     *
     * @param trns ��?�?W�n���瓾��ꂽ?W�ϊ����Z�q
     * @param rCos cos(��]�p�x)
     * @param rSin sin(��]�p�x)
     * @return ��]��̋�?�
     */
    ParametricCurve3D rotateZ(CartesianTransformationOperator3D trns,
                              double rCos, double rSin) {
        int n_pnts = nPoints();
        Point3D[] pnts = new Point3D[n_pnts];

        for (int i = 0; i < n_pnts; i++)
            pnts[i] = pointAt(i).rotateZ(trns, rCos, rSin);

        return new Polyline3D(pnts, closed());
    }

    /**
     * ���̋�?�?�̓_��?A�^����ꂽ��?�?�ɂȂ��_���Ԃ�?B
     *
     * @param line ��?�
     * @return �^����ꂽ��?�?�ɂȂ��_
     */
    Point3D getPointNotOnLine(Line3D line) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol2 = condition.getToleranceForDistance2();

        int itry = 0, limit = nPoints();
        Point3D point;
        Vector3D vector;

        /*
        * Get a point which is not on the line, then verify that
        * the distance between a point and the line is greater
        * than the tolerance.
        */
        do {
            if (itry >= limit) {
                throw new FatalException();    // should never be occurred
            }
            point = pointAt(itry);
            vector = point.subtract(line.project1From(point));
            itry++;
        } while (point.isOn(line) || vector.norm() < dTol2);

        return point;
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
    protected synchronized ParametricCurve3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Point3D[] tPoints =
                Point3D.transform(this.points,
                        reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new Polyline3D(tPoints, this.closed);
    }

    /**
     * ���̋�?�|�����C���̕�����܂ނ��ۂ���Ԃ�?B
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C����܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean hasPolyline() {
        return true;
    }

    /**
     * ���̋�?�|�����C���̕��������łł��Ă��邩�ۂ���Ԃ�?B
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C��������܂ނȂ�� true?A
     *         �����łȂ���� false
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
