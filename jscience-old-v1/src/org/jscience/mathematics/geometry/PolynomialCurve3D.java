/*
 * �R���� : ��?�����?��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PolynomialCurve3D.java,v 1.3 2007-10-21 21:08:18 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �R���� : ��?�����?��\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X�ɂ�?A�R������ X, Y, Z ?�����
 * �O�̑�?��� (X, Y, Z) �ŕ\��?���?�����?��?A
 * �l�̑�?��� (WX, WY, WZ, W) �ŕ\���L�?��?�����?��?B
 * </p>
 * <p/>
 * X, Y, Z �µ���� WX, XY, WZ, W �̊e?�����\����?����͓���?��ł����̂Ƃ���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:18 $
 */

public class PolynomialCurve3D extends ParametricCurve3D {
    /**
     * ��?�� X ?�����\����?��� (�L�?��?�����?�?��� WX ?���) ?B
     *
     * @serial
     */
    private final DoublePolynomial polyX;

    /**
     * ��?�� Y ?�����\����?��� (�L�?��?�����?�?��� WY ?���) ?B
     *
     * @serial
     */
    private final DoublePolynomial polyY;

    /**
     * ��?�� Z ?�����\����?��� (�L�?��?�����?�?��� WZ ?���) ?B
     *
     * @serial
     */
    private final DoublePolynomial polyZ;

    /**
     * ��?�� W ?�����\����?��� (�L�?��?�����?�?��̂�) ?B
     *
     * @serial
     */
    private final DoublePolynomial polyW;

    /**
     * �ꎟ����?���\����?�?B
     * <p/>
     * ���̃t�B?[���h�̓C���X�^���X�̓Ք�ł̂ݗ��p�����?B
     * </p>
     *
     * @serial
     */
    private PolynomialCurve3D derivativeCurve;

    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z���邱�Ƃ͂ł��Ȃ�?B
     */
    private PolynomialCurve3D() {
        super();
        polyX = null;
        polyY = null;
        polyZ = null;
        polyW = null;
    }

    /**
     * ?���?��� (integral polynomial) ��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param xPoly X ?�����\����?���
     * @param yPoly Y ?�����\����?���
     * @param zPoly Z ?�����\����?���
     */
    public PolynomialCurve3D(DoublePolynomial xPoly,
                             DoublePolynomial yPoly,
                             DoublePolynomial zPoly) {
        if ((xPoly == null) || (yPoly == null) || (zPoly == null))
            throw new FatalException("one of arguments is null");

        if ((xPoly.degree() != yPoly.degree()) ||
                (xPoly.degree() != zPoly.degree()))
            throw new FatalException("given polynomials have different degrees");

        polyX = xPoly;
        polyY = yPoly;
        polyZ = zPoly;
        polyW = null;

        derivativeCurve = null;
    }

    /**
     * �L�?��?��� (rational polynomial) ��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��?�̑��?����ɂ��Ă̑�?����̒l�� X �ł͂Ȃ� WX ��\�����Ƃɒ?��?B
     * ���?����ɂ��Ă̑�?����̒l��?A���l��?AY �ł͂Ȃ� WY ��\��?B
     * ��O?����ɂ��Ă̑�?����̒l��?A���l��?AZ �ł͂Ȃ� WZ ��\��?B
     * </p>
     *
     * @param wxPoly WX ?�����\����?���
     * @param wyPoly WY ?�����\����?���
     * @param wzPoly WZ ?�����\����?���
     * @param wPoly  W ?�����\����?���
     */
    public PolynomialCurve3D(DoublePolynomial wxPoly,
                             DoublePolynomial wyPoly,
                             DoublePolynomial wzPoly,
                             DoublePolynomial wPoly) {
        if ((wxPoly == null) || (wyPoly == null) || (wzPoly == null) || (wPoly == null))
            throw new FatalException("one of arguments is null");

        if ((wxPoly.degree() != wPoly.degree()) ||
                (wxPoly.degree() != wPoly.degree()) ||
                (wzPoly.degree() != wPoly.degree()))
            throw new FatalException("given polynomials have different degrees");

        polyX = wxPoly;
        polyY = wyPoly;
        polyZ = wzPoly;
        polyW = wPoly;

        derivativeCurve = null;
    }

    /**
     * ���̑�?�����?�̎�?���Ԃ�?B
     *
     * @return ��?�̎�?�
     */
    public int degree() {
        return polyX.degree();
    }

    /**
     * ���̋�?�?���?����`�����ۂ���Ԃ�?B
     *
     * @return ?���?����`���Ȃ�� true?A�����łȂ���� false
     */
    public boolean isIntegral() {
        return (polyW == null);
    }

    /**
     * ���̋�?�L�?�`�����ۂ���Ԃ�?B
     *
     * @return �L�?�`���Ȃ�� true?A�����łȂ���� false
     */
    public boolean isRational() {
        return (polyW != null);
    }

    /**
     * ���̋�?�̓���?���\����?��Ԃ�?B
     *
     * @return ����?���\����?�
     */
    private PolynomialCurve3D derivative() {
        if (derivativeCurve == null) {
            if (isIntegral()) {
                derivativeCurve =
                        new PolynomialCurve3D((DoublePolynomial) polyX.differentiate(), (DoublePolynomial) polyY.differentiate(), (DoublePolynomial) polyZ.differentiate());
            } else {
                derivativeCurve =
                        new PolynomialCurve3D((DoublePolynomial) polyX.differentiate(), (DoublePolynomial) polyY.differentiate(), (DoublePolynomial) polyZ.differentiate(),
                                (DoublePolynomial) polyW.differentiate());
            }
        }

        return derivativeCurve;
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     * @see UnsupportedOperationException
     */
    public ParametricCurve3D parallelTranslate(Vector3D moveVec) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     *
     * @return ���Ŕ���I�ȃp���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        return new ParameterDomain();
    }

    /**
     * ���̋�?�􉽓I�ɕ��Ă��邩�ۂ���Ԃ�?B
     *
     * @return ?�� false
     */
    boolean getClosedFlag() {
        return false;    // ???
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
     * ���̋�?��?���?�����?�ł���Ƃ���?A�^����ꂽ�p���??[�^�l�ŕ]������?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    private LiteralVector3D evaluateAsIntegral(double param) {
        return new LiteralVector3D(polyX.map(param),
                polyY.map(param),
                polyZ.map(param));
    }

    /**
     * ���̋�?��L�?��?�����?�ł���Ƃ���?A�^����ꂽ�p���??[�^�l�ŕ]������?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    private HomogeneousVector3D evaluateAsRational(double param) {
        return new HomogeneousVector3D(polyX.map(param),
                polyY.map(param),
                polyZ.map(param),
                polyW.map(param));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    private Point3D evaluateD0(double param) {
        Point3D result;

        if (isIntegral()) {
            LiteralVector3D D0 = evaluateAsIntegral(param);
            result = new CartesianPoint3D(D0.x(), D0.y(), D0.z());
        } else {
            HomogeneousVector3D D0H = evaluateAsRational(param);
            result = new HomogeneousPoint3D(D0H.wx(), D0H.wy(), D0H.wz(), D0H.w());
        }

        return result;
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̈ꎟ����?���Ԃ�?B
     *
     * @param param �p���??[�^
     * @return �ꎟ����?�
     */
    private Vector3D evaluateD1(double param) {
        Vector3D result;

        if (isIntegral()) {
            result = derivative().evaluateAsIntegral(param);
        } else {
            /*
            * p(t)  = h(t)  / w(t)	: h = (wx, wy)
            * h(t)  = w(t)  * p(t)
            * h'(t) = w'(t) * p(t) + w(t) * p'(t)
            * p'(t) = (h'(t) - w'(t) * p(t)) / w(t)
            */
            HomogeneousVector3D D0H = evaluateAsRational(param);
            HomogeneousVector3D D1H = derivative().evaluateAsRational(param);

            Vector3D D0 = D0H;
            Vector3D D1 = new HomogeneousVector3D
                    ((D1H.wx() - (D1H.w() * D0.x())),
                            (D1H.wy() - (D1H.w() * D0.y())),
                            (D1H.wz() - (D1H.w() * D0.z())),
                            D0H.w());
            result = D1;
        }

        return result;
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̈ꎟ/�񎟓���?���Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f�Ɉꎟ����?�?A
     * ��Ԗڂ̗v�f�ɓ񎟓���?�
     * �����?B
     * </p>
     *
     * @param param �p���??[�^
     * @return ����?��̔z��
     */
    private Vector3D[] evaluateD1D2(double param) {
        Vector3D[] result = new Vector3D[2];

        if (isIntegral()) {
            result[0] = derivative().evaluateAsIntegral(param);
            result[1] = derivative().derivative().evaluateAsIntegral(param);
        } else {
            HomogeneousVector3D D0H = evaluateAsRational(param);
            HomogeneousVector3D D1H = derivative().evaluateAsRational(param);
            HomogeneousVector3D D2H = derivative().derivative().evaluateAsRational(param);

            Vector3D D0 = D0H;
            Vector3D D1 = new HomogeneousVector3D
                    ((D1H.wx() - (D1H.w() * D0.x())),
                            (D1H.wy() - (D1H.w() * D0.y())),
                            (D1H.wz() - (D1H.w() * D0.z())),
                            D0H.w());
            Vector3D D2 = new HomogeneousVector3D
                    ((D2H.wx() - ((2.0 * D1H.w() * D1.x()) + (D2H.w() * D0.x()))),
                            (D2H.wy() - ((2.0 * D1H.w() * D1.y()) + (D2H.w() * D0.y()))),
                            (D2H.wz() - ((2.0 * D1H.w() * D1.z()) + (D2H.w() * D0.z()))),
                            D0H.w());
            result[0] = D1;
            result[1] = D2;
        }

        return result;
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̈ꎟ/��/�O������?���Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 3 ��?A
     * ?�?��̗v�f�Ɉꎟ����?�?A
     * ��Ԗڂ̗v�f�ɓ񎟓���?�?A
     * �O�Ԗڂ̗v�f�ɎO������?�
     * �����?B
     * </p>
     *
     * @param param �p���??[�^
     * @return ����?��̔z��
     */
    private Vector3D[] evaluateD1D2D3(double param) {
        Vector3D[] result = new Vector3D[3];

        if (isIntegral()) {
            result[0] = derivative().evaluateAsIntegral(param);
            result[1] = derivative().derivative().evaluateAsIntegral(param);
            result[2] = derivative().derivative().derivative().evaluateAsIntegral(param);
        } else {
            HomogeneousVector3D D0H = evaluateAsRational(param);
            HomogeneousVector3D D1H = derivative().evaluateAsRational(param);
            HomogeneousVector3D D2H = derivative().derivative().evaluateAsRational(param);
            HomogeneousVector3D D3H = derivative().derivative().derivative().evaluateAsRational(param);

            Vector3D D0 = D0H;
            Vector3D D1 = new HomogeneousVector3D
                    ((D1H.wx() - (D1H.w() * D0.x())),
                            (D1H.wy() - (D1H.w() * D0.y())),
                            (D1H.wz() - (D1H.w() * D0.z())),
                            D0H.w());
            Vector3D D2 = new HomogeneousVector3D
                    ((D2H.wx() - ((2.0 * D1H.w() * D1.x()) + (D2H.w() * D0.x()))),
                            (D2H.wy() - ((2.0 * D1H.w() * D1.y()) + (D2H.w() * D0.y()))),
                            (D2H.wz() - ((2.0 * D1H.w() * D1.z()) + (D2H.w() * D0.z()))),
                            D0H.w());
            Vector3D D3 = new HomogeneousVector3D
                    ((D3H.wx() - ((3.0 * ((D1H.w() * D2.x()) + (D2H.w() * D1.x()))) + (D3H.w() * D0.x()))),
                            (D3H.wy() - ((3.0 * ((D1H.w() * D2.y()) + (D2H.w() * D1.y()))) + (D3H.w() * D0.y()))),
                            (D3H.wz() - ((3.0 * ((D1H.w() * D2.z()) + (D2H.w() * D1.z()))) + (D3H.w() * D0.z()))),
                            D0H.w());

            result[0] = D1;
            result[1] = D2;
            result[2] = D3;
        }

        return result;
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    public Point3D coordinates(double param) {
        return evaluateD0(param);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector3D tangentVector(double param) {
        return evaluateD1(param);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     */
    public CurveCurvature3D curvature(double param) {
        Vector3D[] D1D2 = evaluateD1D2(param);
        Vector3D D1 = D1D2[0];
        Vector3D D2 = D1D2[1];
        double D1Leng = D1.length();
        Vector3D crsProd = D1.crossProduct(D2);
        double curvature = crsProd.magnitude() / (D1Leng * D1Leng * D1Leng);
        Vector3D normal = crsProd.crossProduct(D1).unitized();
        return new CurveCurvature3D(curvature, normal);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̃��C����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ���C��
     */
    public double torsion(double param) {
        Vector3D[] D1D2D3 = evaluateD1D2D3(param);
        Vector3D D1 = D1D2D3[0];
        Vector3D D2 = D1D2D3[1];
        Vector3D D3 = D1D2D3[2];
        double D1Leng = D1.length();
        Vector3D crsProd = D1.crossProduct(D2);
        double leng3 = D1Leng * D1Leng * D1Leng;
        double curvature = crsProd.magnitude() / leng3;
        return crsProd.dotProduct(D3) / (leng3 * leng3 * curvature * curvature);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ����?�
     */
    public CurveDerivative3D evaluation(double param) {
        Point3D D0 = evaluateD0(param);
        Vector3D[] D1D2D3 = evaluateD1D2D3(param);
        return new CurveDerivative3D(D0, D1D2D3[0], D1D2D3[1], D1D2D3[2]);
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     * @see UnsupportedOperationException
     */
    public PointOnCurve3D[] singular() {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     * @see UnsupportedOperationException
     */
    public PointOnCurve3D[] inflexion() {
        throw new UnsupportedOperationException();
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @see UnsupportedOperationException
     */
    public PointOnCurve3D[] projectFrom(Point3D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param section   ��?�ߎ�����p���??[�^���
     * @param tolerance �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     * @see UnsupportedOperationException
     */
    public Polyline3D toPolyline(ParameterSection section,
                                 ToleranceForDistance tolerance) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param section �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     * @see UnsupportedOperationException
     */
    public BsplineCurve3D toBsplineCurve(ParameterSection section) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
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
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (��?�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (�~)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Circle3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (�ȉ~)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (��?�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (�o��?�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Hyperbola3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (�|�����C��)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Polyline3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (�g������?�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(TrimmedCurve3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?��?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (��?���?��?�Z�O�?���g)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(CompositeCurveSegment3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?��?�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @param mate       ���̋�?� (��?���?��?�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(CompositeCurve3D mate, boolean doExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋Ȗʂ̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    public IntersectionPoint3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (?����Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋Ȗ� (?����Ȗ�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(ElementarySurface3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�x�W�G�Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋Ȗ� (�x�W�G�Ȗ�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(PureBezierSurface3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @return ��_�̔z��
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(BsplineSurface3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        throw new UnsupportedOperationException();
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ�?B
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     */
    public double length(ParameterSection pint) {
        // �p���??[�^�̃`�F�b�N���K�v����?H
        double dTol = getToleranceForDistance() / 2.0;
        return GeometryUtils.getDefiniteIntegral
                (new PrimitiveMapping() {
                    /**
                     * Evaluates this function.
                     */
                    public double map(float x) {
                        return map((double) x);
                    }

                    /**
                     * Evaluates this function.
                     */
                    public double map(long x) {
                        return map((double) x);
                    }

                    /**
                     * Evaluates this function.
                     */
                    public double map(int x) {
                        return map((double) x);
                    }

                    public double map(double parameter) {
                        return tangentVector(parameter).length();
                    }
                },
                        pint, dTol);
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve3D#POLYNOMIAL_CURVE_3D ParametricCurve3D.POLYNOMIAL_CURVE_3D}
     */
    int type() {
        return POLYNOMIAL_CURVE_3D;
    }

    /**
     * ���̋�?��?A�^����ꂽ��?�?W�n�� Z ���̎���?A
     * �^����ꂽ�p�x������]��������?��Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param trns ��?�?W�n���瓾��ꂽ?W�ϊ����Z�q
     * @param rCos cos(��]�p�x)
     * @param rSin sin(��]�p�x)
     * @return ��]��̋�?�
     * @see UnsupportedOperationException
     */
    ParametricCurve3D rotateZ(CartesianTransformationOperator3D trns,
                              double rCos, double rSin) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�?�̓_��?A�^����ꂽ��?�?�ɂȂ��_���Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̋@�\�̓T�|?[�g����Ă��Ȃ�����?A?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param line ��?�
     * @return �^����ꂽ��?�?�ɂȂ��_
     * @see UnsupportedOperationException
     */
    Point3D getPointNotOnLine(Line3D line) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?��?\?����Ă��鑽?�����z��ɓ��ĕԂ�?B
     *
     * @return ��?����̔z��
     */
    private DoublePolynomial[] toPolynomialArray() {
        if (isIntegral()) {
            DoublePolynomial[] polyArray = {polyX, polyY, polyZ};
            return polyArray;
        } else {
            DoublePolynomial[] polyArray = {polyX, polyY, polyZ, polyW};
            return polyArray;
        }
    }

    /**
     * ���̋�?�̈ꎟ����?��Ɠ񎟓���?��̊O?ς�\����?�����?�߂�?B
     *
     * @return �ꎟ����?��Ɠ񎟓���?��̊O?ς�\�����鑽?���
     */
    public DoublePolynomial[] crossProductD1D2() {
        PolynomialCurve3D d1 = derivative();
        PolynomialCurve3D d2 = d1.derivative();
        DoublePolynomial crossPoly1;
        DoublePolynomial crossPoly2;
        DoublePolynomial[] d1Poly;
        DoublePolynomial[] d2Poly;
        DoublePolynomial[] crossPoly;

        if (isIntegral()) {
            d1Poly = d1.toPolynomialArray();
            d2Poly = d2.toPolynomialArray();
        } else {
            DoublePolynomial workPoly;
            DoublePolynomial workPoly1;
            DoublePolynomial workPoly2;
            DoublePolynomial workPoly3;
            DoublePolynomial workPoly4;
            ;
            DoublePolynomial[] d0WPoly = toPolynomialArray();
            DoublePolynomial[] d1WPoly = d1.toPolynomialArray();
            DoublePolynomial[] d2WPoly = d2.toPolynomialArray();

            d1Poly = new DoublePolynomial[3];
            d2Poly = new DoublePolynomial[3];

            for (int klm = 0; klm < 3; klm++) {
                workPoly1 = (DoublePolynomial) polyW.multiply(d1WPoly[klm]);
                workPoly2 = (DoublePolynomial) d1.polyW.multiply(d0WPoly[klm]);
                d1Poly[klm] = (DoublePolynomial) workPoly1.subtract(workPoly2);

                workPoly = (DoublePolynomial) polyW.multiply(polyW);
                workPoly1 = (DoublePolynomial) workPoly.multiply(d2WPoly[klm]);
                workPoly = (DoublePolynomial) d1.polyW.multiply(d0WPoly[klm]);
                workPoly2 = (DoublePolynomial) workPoly.multiply(d1WPoly[klm]);
                workPoly = (DoublePolynomial) d1.polyW.multiply(d1.polyW);
                workPoly3 = (DoublePolynomial) workPoly.multiply(d0WPoly[klm]);
                workPoly = (DoublePolynomial) d2.polyW.multiply(polyW);
                workPoly4 = (DoublePolynomial) workPoly.multiply(d0WPoly[klm]);

                workPoly = ((DoublePolynomial) workPoly2.subtract(workPoly3)).scalarMultiply(2.0);
                d2Poly[klm] = (DoublePolynomial) workPoly1.subtract(workPoly).subtract(workPoly4);
            }
        }

        crossPoly = new DoublePolynomial[3];
        for (int ijk = 0; ijk < 3; ijk++) {
            crossPoly1 = (DoublePolynomial) d1Poly[(ijk + 1) % 3].multiply
                    (d2Poly[(ijk + 2) % 3]);
            crossPoly2 = (DoublePolynomial) d1Poly[(ijk + 2) % 3].multiply
                    (d2Poly[(ijk + 1) % 3]);
            crossPoly[ijk] = (DoublePolynomial) crossPoly1.subtract(crossPoly2);
        }

        return crossPoly;
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
        throw new UnsupportedOperationException();
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer ?o�̓X�g��?[��
     * @param indent �C���f���g��?[��
     */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);
        StringBuffer buf = new StringBuffer();

        writer.println(indent_tab + getClassName());
        if (this.polyW == null) {
            writer.print(indent_tab + "\tpolyX");
            writer.print(" [" + this.polyX.getCoefficientAsDouble(0));
            for (int i = 1; i <= this.polyX.degree(); i++)
                writer.print(", " + this.polyX.getCoefficientAsDouble(i));
            writer.println("]");

            writer.print(indent_tab + "\tpolyY");
            writer.print(" [" + this.polyY.getCoefficientAsDouble(0));
            for (int i = 1; i <= this.polyY.degree(); i++)
                writer.print(", " + this.polyY.getCoefficientAsDouble(i));
            writer.println("]");

            writer.print(indent_tab + "\tpolyZ");
            writer.print(" [" + this.polyZ.getCoefficientAsDouble(0));
            for (int i = 1; i <= this.polyZ.degree(); i++)
                writer.print(", " + this.polyZ.getCoefficientAsDouble(i));
            writer.println("]");
        } else {
            writer.print(indent_tab + "\tpolyWX");
            writer.print(" [" + this.polyX.getCoefficientAsDouble(0));
            for (int i = 1; i <= this.polyX.degree(); i++)
                writer.print(", " + this.polyX.getCoefficientAsDouble(i));
            writer.println("]");

            writer.print(indent_tab + "\tpolyWY");
            writer.print(" [" + this.polyY.getCoefficientAsDouble(0));
            for (int i = 1; i <= this.polyY.degree(); i++)
                writer.print(", " + this.polyY.getCoefficientAsDouble(i));
            writer.println("]");

            writer.print(indent_tab + "\tpolyWZ");
            writer.print(" [" + this.polyZ.getCoefficientAsDouble(0));
            for (int i = 1; i <= this.polyZ.degree(); i++)
                writer.print(", " + this.polyZ.getCoefficientAsDouble(i));
            writer.println("]");

            writer.print(indent_tab + "\tpolyW");
            writer.print(" [" + this.polyW.getCoefficientAsDouble(0));
            for (int i = 1; i <= this.polyW.degree(); i++)
                writer.print(", " + this.polyW.getCoefficientAsDouble(i));
            writer.println("]");
        }
    }

    // Debug : integral
    /**
     * �f�o�b�O�p�?�\�b�h 1
     */
    private static void test1(String argv[]) throws InvalidArgumentValueException {
        Point3D[] cntrlPoints = new Point3D[4];
        cntrlPoints[0] = new CartesianPoint3D(0.0, 0.0, 0.0);
        cntrlPoints[1] = new CartesianPoint3D(10.0, 10.0, 1.0);
        cntrlPoints[2] = new CartesianPoint3D(20.0, 10.0, -1.0);
        cntrlPoints[3] = new CartesianPoint3D(30.0, 0.0, 0.0);
        DoublePolynomial[] polynomial =
                (new PureBezierCurve3D(cntrlPoints)).polynomial(true);
        PolynomialCurve3D polynomialCurve =
                new PolynomialCurve3D(polynomial[0], polynomial[1],
                        polynomial[2]);

        for (int i = 0; i <= 100; i++) {
            double param = 0.01 * i;
            Point3D crd = polynomialCurve.coordinates(param);
            Vector3D tng = polynomialCurve.tangentVector(param);
            CurveCurvature3D crv = polynomialCurve.curvature(param);
            System.out.println(crd.x() + "\t" + crd.y() + "\t" + crd.z() + "\t" +
                    tng.x() + "\t" + tng.y() + "\t" + tng.z() + "\t" +
                    crv.curvature());
        }
    }

    // Debug : rational
    /**
     * �f�o�b�O�p�?�\�b�h 2
     */
    private static void test2(String argv[]) throws InvalidArgumentValueException {
        Point3D[] cntrlPoints = new Point3D[4];
        cntrlPoints[0] = new CartesianPoint3D(0.0, 0.0, 0.0);
        cntrlPoints[1] = new CartesianPoint3D(10.0, 10.0, 1.0);
        cntrlPoints[2] = new CartesianPoint3D(20.0, 10.0, -1.0);
        cntrlPoints[3] = new CartesianPoint3D(30.0, 0.0, 0.0);
        double[] weights = new double[4];
        weights[0] = 1.0;
        weights[1] = 1.0;
        weights[2] = 1.0;
        weights[3] = 1.0;
        DoublePolynomial[] polynomial =
                (new PureBezierCurve3D(cntrlPoints, weights)).polynomial(false);
        PolynomialCurve3D polynomialCurve =
                new PolynomialCurve3D(polynomial[0], polynomial[1],
                        polynomial[2], polynomial[3]);

        for (int i = 0; i <= 100; i++) {
            double param = 0.01 * i;
            Point3D crd = polynomialCurve.coordinates(param);
            Vector3D tng = polynomialCurve.tangentVector(param);
            CurveCurvature3D crv = polynomialCurve.curvature(param);
            System.out.println(crd.x() + "\t" + crd.y() + "\t" + crd.z() + "\t" +
                    tng.x() + "\t" + tng.y() + "\t" + tng.z() + "\t" +
                    crv.curvature());
        }
    }

    // Debug
    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     */
    public static void main(String argv[]) {
        try {
            // test1(argv);
            test2(argv);
        } catch (InvalidArgumentValueException e) {
        }
    }
}

/* end of file */
