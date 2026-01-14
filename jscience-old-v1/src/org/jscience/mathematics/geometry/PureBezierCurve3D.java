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

import org.jscience.mathematics.MachineEpsilon;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
 * �R���� : ��L�? (��?���) �x�W�G��?��їL�?�x�W�G��?��\���N���X
 * <p/>
 * ���̃N���X�ɓWL�ȑ�?���\���t�B?[���h�͓BɂȂ�?B
 * ?���_��Ȃǂ�ێ?����t�B?[���h�ɂ��Ă�?A
 * {@link FreeformCurveWithControlPoints3D �X?[�p?[�N���X�̉�?�} ��Q?�?B
 * </p>
 * <p/>
 * �x�W�G��?�̃p���??[�^��`��� [0, 1] �ƂȂ�?B
 * </p>
 * <p/>
 * t ��p���??[�^�Ƃ���x�W�G��?� P(t) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	n = ?���_��?� - 1
 * 	bi = controlPoints[i]
 * 	wi = weights[i]
 * </pre>
 * �Ƃ���?A��L�?�x�W�G��?��
 * <pre>
 * 	P(t) =	(bi * Bn,i(t)) �̑?�a		(i = 0, ..., n)
 * </pre>
 * �L�?�x�W�G��?��
 * <pre>
 * 		(wi * bi * Bn,i(t)) �̑?�a
 * 	P(t) =	-------------------------- 	(i = 0, ..., n)
 * 		(wi * Bn,i(t)) �̑?�a
 * </pre>
 * ������ Bn,i(t) �̓o?[���X�^�C������?��ł���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:19 $
 */

public class PureBezierCurve3D extends FreeformCurveWithControlPoints3D {
    /**
     * ?���_���^���đ�?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints3D#FreeformCurveWithControlPoints3D(Point3D[])
     * super}(controlPoints)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param controlPoints ?���_�̔z��
     */
    public PureBezierCurve3D(Point3D[] controlPoints) {
        super(controlPoints);
    }

    /**
     * ?���_���?d�ݗ��^���ėL�?��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints3D#FreeformCurveWithControlPoints3D(Point3D[],double[])
     * super}(controlPoints, weights)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param controlPoints ?���_�̔z��
     * @param weights       ?d�݂̔z��
     */
    public PureBezierCurve3D(Point3D[] controlPoints, double[] weights) {
        super(controlPoints, weights);
    }

    /**
     * ?���_���?d�ݗ��^����
     * ��?�����?� (���邢�͗L�?��?�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints3D#FreeformCurveWithControlPoints3D(Point3D[],double[],boolean)
     * super}(controlPoints, weights, doCheck)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param controlPoints ?���_�̔z��?B
     * @param weights       ?d�݂̔z��
     * @param doCheck       ��?��̃`�F�b�N�ⷂ邩�ǂ���
     */
    public PureBezierCurve3D(Point3D[] controlPoints,
                             double[] weights,
                             boolean doCheck) {
        super(controlPoints, weights, doCheck);
    }

    /**
     * ?���_ (��?d��) ��񎟌��z��ŗ^����
     * ��?�����?� (���邢�͗L�?��?�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints3D#FreeformCurveWithControlPoints3D(double[][])
     * super}(cpArray)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param cpArray ?���_ (�����?d��) �̔z��
     */
    PureBezierCurve3D(double[][] cpArray) {
        super(cpArray);
    }

    /**
     * ���̋�?�̎�?���Ԃ�?B
     *
     * @return ��?�
     */
    public int degree() {
        return nControlPoints() - 1;
    }

    /**
     * ���̋�?�̑�?����\����Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����z�� R �̗v�f?���?A
     * ���̋�?��L�?�ł���� 3?A
     * �L�?�ł���� 4 �ł���?B
     * </p>
     * <p/>
     * ��L�?��?��?�?�?A
     * R[0] �� X ?���?A
     * R[1] �� Y ?���
     * R[2] �� Z ?���
     * �̑�?����\����\��?B
     * </p>
     * <p/>
     * �L�?��?��?�?�?A
     * R[0] �� WX ?���?A
     * R[1] �� WY ?���
     * R[2] �� WZ ?���
     * R[3] �� W ?���
     * �̑�?����\����\��?B
     * </p>
     *
     * @param isPoly ��L�?�ł��邩�ǂ���
     * @return ��?����̔z��
     * @see #polynomialCurve(boolean)
     */
    public DoublePolynomial[] polynomial(boolean isPoly) {
        int ijk, kji, mno, klm;
        int binml;
        int uicp = nControlPoints();
        double[][] dDcoef = toDoubleArray(isPoly);
        int npoly = dDcoef[0].length;
        double[] coef = new double[uicp];
        DoublePolynomial[] polynomial = new DoublePolynomial[npoly];

        for (klm = 0; klm < npoly; klm++) {
            for (ijk = 0; ijk < uicp; ijk++)
                coef[ijk] = dDcoef[ijk][klm];

            // forward differences
            for (ijk = 0; ijk < uicp; ijk++)
                for (mno = uicp - 1; ijk < mno; mno--)
                    coef[mno] -= coef[mno - 1];

            // nCr
            binml = 1;
            for (ijk = 1, kji = uicp - 2; ijk < kji; ijk++, kji--) {
                binml = (binml * (uicp - ijk)) / ijk;
                coef[ijk] *= binml;
                coef[kji] *= binml;
            }

            if (ijk == kji) {
                binml = (binml * (uicp - ijk)) / ijk;
                coef[ijk] *= binml;
            }

            polynomial[klm] = new DoublePolynomial(coef);
        }

        return polynomial;
    }

    /**
     * ���̋�?��?Č����鑽?�����?��Ԃ�?B
     *
     * @param isPoly ��L�?�ł��邩�ǂ���
     * @return ��?�����?�
     * @see #polynomial(boolean)
     */
    public PolynomialCurve3D polynomialCurve(boolean isPoly) {
        DoublePolynomial[] poly = polynomial(isPoly);
        if (isPoly) {
            return new PolynomialCurve3D(poly[0], poly[1], poly[2]);
        } else {
            return new PolynomialCurve3D(poly[0], poly[1], poly[2], poly[3]);
        }
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃ�?A���̋�?�̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * section �̑?���l�͕��ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ����̋�?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param pint �`�F�b�N����p���??[�^���
     * @return �K�v�ɉ����Ă��̋�?�̒�`���Ɋۂ߂�ꂽ�p���??[�^���
     * @see AbstractParametricCurve#checkValidity(ParameterSection)
     * @see ParameterOutOfRange
     */
    ParameterSection checkBoundary(ParameterSection pint) {
        checkValidity(pint);
        double start = pint.start();
        if (start < 0.0)
            start = 0.0;
        else if (start > 1.0) start = 1.0;
        double end = pint.end();
        if (end < 0.0)
            end = 0.0;
        else if (end > 1.0) end = 1.0;

        return new ParameterSection(start, end - start);
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ�?B
     * <p/>
     * pint �̑?���l�͕��ł©�܂�Ȃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ����̋�?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     * @see ParameterOutOfRange
     */
    public double length(ParameterSection pint) {
        // check boundary
        pint = checkBoundary(pint);

        PrimitiveMapping realFunction;
        if (!isPolynomial()) {
            realFunction
                    = new PrimitiveMapping() {
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
            };
        } else {
            DoublePolynomial[] poly = polynomial(isPolynomial());
            final DoublePolynomial[] deriv =
                    new DoublePolynomial[poly.length];

            for (int ijk = 0; ijk < 3; ijk++)
                deriv[ijk] = (DoublePolynomial) poly[ijk].differentiate();

            realFunction
                    = new PrimitiveMapping() {
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
                    final double[] tang = new double[3];
                    for (int ijk = 0; ijk < 3; ijk++)
                        tang[ijk] = deriv[ijk].map(parameter);

                    return Math.sqrt(tang[0] * tang[0] +
                            tang[1] * tang[1] +
                            tang[2] * tang[2]);
                }
            };
        }
        double dTol = getToleranceForDistance() / 2.0;

        return GeometryUtils.getDefiniteIntegral(realFunction, pint, dTol);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     * @see ParameterOutOfRange
     */
    public Point3D coordinates(double param) {
        double[][] cntlPnts;
        double[] d0D;
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        d0D = PureBezierCurveEvaluation.coordinates(cntlPnts, param);
        if (!isPoly) {
            convRational0Deriv(d0D);
        }
        return new CartesianPoint3D(d0D[0], d0D[1], d0D[2]);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     * @see ParameterOutOfRange
     */
    public Vector3D tangentVector(double param) {
        double[][] cntlPnts;
        double[] d1D = new double[4];
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        if (isPoly) {
            PureBezierCurveEvaluation.evaluation(cntlPnts, param, null, d1D, null, null);
        } else {
            double[] d0D = new double[4];

            PureBezierCurveEvaluation.evaluation(cntlPnts, param, d0D, d1D, null, null);
            convRational1Deriv(d0D, d1D);
        }
        return new LiteralVector3D(d1D[0], d1D[1], d1D[2]);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     * @see ParameterOutOfRange
     */
    public CurveCurvature3D curvature(double param) {
        int degree;
        CurveDerivative3D deriv;
        boolean tang0;
        double tang_leng;
        Vector3D ewvec;
        double dDcrv;
        Vector3D dDnrm;
        CurveCurvature3D crv;
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double tol_d = condition.getToleranceForDistance();

        degree = degree();
        deriv = evaluation(param);

        tang_leng = deriv.d1D().norm();
        if (tang_leng < (tol_d * tol_d)) {
            tang0 = true;
        } else {
            tang0 = false;
        }

        if ((degree < 2) || (tang0 == true)) {
            dDcrv = 0.0;
            dDnrm = Vector3D.zeroVector;
        } else {
            tang_leng = Math.sqrt(tang_leng);
            dDcrv = tang_leng * tang_leng * tang_leng;

            ewvec = deriv.d1D().crossProduct(deriv.d2D());
            dDcrv = ewvec.magnitude() / dDcrv;
            dDnrm = ewvec.crossProduct(deriv.d1D()).unitized();
        }
        return new CurveCurvature3D(dDcrv, dDnrm);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̃��C����Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ���C��
     * @see ParameterOutOfRange
     */
    public double torsion(double param) {
        int degree;
        CurveDerivative3D deriv;
        boolean tang0, curv0;
        double tang_leng;
        Vector3D ewvec;
        double ewrk1, ewrk2;
        double dDcrv;
        double dDtsn;
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double tol_d = condition.getToleranceForDistance();

        degree = degree();
        deriv = evaluation(param);

        tang_leng = deriv.d1D().norm();
        if (tang_leng < (tol_d * tol_d)) {
            tang0 = true;
        } else {
            tang0 = false;
        }

        if ((degree < 2) || (tang0 == true)) {
            dDcrv = 0.0;
            curv0 = true;
            ewrk2 = 0.0;
            ewvec = null;
        } else {
            tang_leng = Math.sqrt(tang_leng);
            ewrk2 = dDcrv = tang_leng * tang_leng * tang_leng;

            ewvec = deriv.d1D().crossProduct(deriv.d2D());
            dDcrv = ewvec.magnitude() / dDcrv;
            curv0 = false;
        }

        if (degree < 3 || curv0) {
            dDtsn = 0.0;
        } else {
            ewrk1 = ewvec.dotProduct(deriv.d3D());
            ewrk2 = ewrk2 * ewrk2 * dDcrv * dDcrv;
            dDtsn = ewrk1 / ewrk2;
        }

        return dDtsn;
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ����?�
     * @see ParameterOutOfRange
     */
    public CurveDerivative3D evaluation(double param) {
        double[][] cntlPnts;
        double[] ld0D = new double[4];
        double[] ld1D = new double[4];
        double[] ld2D = new double[4];
        double[] ld3D = new double[4];
        Point3D d0D;
        Vector3D d1D;
        Vector3D d2D;
        Vector3D d3D;
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        PureBezierCurveEvaluation.evaluation(cntlPnts, param,
                ld0D, ld1D, ld2D, ld3D);
        if (!isPoly) {
            convRational3Deriv(ld0D, ld1D, ld2D, ld3D);
        }
        d0D = new CartesianPoint3D(ld0D[0], ld0D[1], ld0D[2]);
        d1D = new LiteralVector3D(ld1D[0], ld1D[1], ld1D[2]);
        d2D = new LiteralVector3D(ld2D[0], ld2D[1], ld2D[2]);
        d3D = new LiteralVector3D(ld3D[0], ld3D[1], ld3D[2]);
        return new CurveDerivative3D(d0D, d1D, d2D, d3D);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^��ɑ΂���u�?�b�T�~���O�̌��ʂ�Ԃ�?B
     * <p/>
     * parameters �̗v�f?���?A���̋�?�̎�?��Ɉ�v���Ă���K�v������?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param parameters �p���??[�^�l�̔z��
     * @return �u�?�b�T�~���O�̌��ʂł���?W�l
     * @see ParameterOutOfRange
     */
    public Point3D blossoming(double[] parameters) {
        double[] adjustedParameters = new double[this.nControlPoints() - 1];
        for (int i = 0; i < this.nControlPoints() - 1; i++)
            adjustedParameters[i] = this.checkParameter(parameters[i]);
        boolean isPoly = this.isPolynomial();

        double[] d0D =
                PureBezierCurveEvaluation.blossoming(this.toDoubleArray(isPoly),
                        adjustedParameters);

        if (isPoly == true)
            return new CartesianPoint3D(d0D[0], d0D[1], d0D[2]);
        else
            return new HomogeneousPoint3D(d0D[0], d0D[1], d0D[2], d0D[3]);
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * �Hٓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     * @throws IndefiniteSolutionException ��?�S�̂�?k�ނ��Ă���
     */
    public PointOnCurve3D[] singular() throws IndefiniteSolutionException {
        int uicp = nControlPoints();
        int uicp_1 = uicp - 1;
        int ijk, klm;
        ParameterDomain pdmn;
        Vector paramVec = new Vector();
        Vector snglrVec = new Vector();

        for (ijk = 1; ijk < uicp; ijk++) {
            if (!startPoint().identical(controlPointAt(ijk))) {
                break;
            }
        }
        if (ijk == uicp) {
            throw new IndefiniteSolutionException(this);
        }

        if (startPoint().identical(controlPointAt(1))) {
            paramVec.addElement(new Double(0.0));
            snglrVec.addElement(startPoint());
        }
        if (endPoint().identical(controlPointAt(uicp_1 - 1))) {
            paramVec.addElement(new Double(1.0));
            snglrVec.addElement(endPoint());
        }

        DoublePolynomial pointPoly[] = polynomial(isPolynomial());
        int polySize = pointPoly.length;
        DoublePolynomial tangPoly[] = new DoublePolynomial[polySize];
        DoublePolynomial dotePoly[] = new DoublePolynomial[3];

        for (klm = 0; klm < polySize; klm++) {
            tangPoly[klm] = (DoublePolynomial) pointPoly[klm].differentiate();
        }

        if (!isRational()) {
            for (klm = 0; klm < 3; klm++)
                dotePoly[klm] = (DoublePolynomial) tangPoly[klm].multiply(tangPoly[klm]);
        } else {
            DoublePolynomial work0, work1, work2, sub;
            double[] work3;

            for (klm = 0; klm < 3; klm++) {
                work0 = (DoublePolynomial) pointPoly[3].multiply(tangPoly[klm]);
                work1 = (DoublePolynomial) tangPoly[3].multiply(pointPoly[klm]);
                // ((a * t^n) * (nb * t^(n-1))) - ((na * t^(n-1)) * (b * t^n)) == 0
                work2 = (DoublePolynomial) work0.subtract(work1);
                work3 = GeometryPrivateUtils.coefficientsBetween(work2, 0, (work2.degree() - 1));
                sub = new DoublePolynomial(work3);

                dotePoly[klm] = (DoublePolynomial) pointPoly[klm].multiply(sub);
            }
        }

        ComplexPolynomial dtPoly;

        try {
            dtPoly = ((DoublePolynomial) dotePoly[0].add(dotePoly[1]).add(dotePoly[2])).toComplexPolynomial();
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        Complex[] root;

        try {
            root = GeometryPrivateUtils.getRootsByDKA(dtPoly);
        } catch (GeometryPrivateUtils.DKANotConvergeException e) {
            root = e.getValues();
        } catch (GeometryPrivateUtils.ImpossibleEquationException e) {
            throw new FatalException();
        } catch (GeometryPrivateUtils.IndefiniteEquationException e) {
            throw new FatalException();
        }

        pdmn = parameterDomain();
        double pTol = getToleranceForParameter();
        for (ijk = 0; ijk < dtPoly.degree(); ijk++) {
            if (!pdmn.isValid(root[ijk].real())) {
                continue;
            }
            if (root[ijk].real() < 0.0 - pTol) continue;
            if (root[ijk].real() > 1.0 + pTol) continue;
            if (root[ijk].real() < 0.0)
                root[ijk] = new Complex(0.0);
            if (root[ijk].real() > 1.0)
                root[ijk] = new Complex(1.0);
            if (Math.abs(root[ijk].imag()) > 0.5) continue;

            Point3D point;
            Vector3D tangent;

            try {
                point = coordinates(root[ijk].real());
                tangent = tangentVector(root[ijk].real());
            } catch (ParameterOutOfRange e) {
                throw new FatalException();
            }

            double dTol2 = getToleranceForDistance();
            if (tangent.norm() > dTol2)
                continue;

            for (klm = 0; klm < paramVec.size(); klm++) {
                if (identicalParameter(root[ijk].real(),
                        ((Double) paramVec.elementAt(klm)).doubleValue())) {
                    break;
                }
            }
            if (klm < paramVec.size())
                continue;

            paramVec.addElement(new Double(root[ijk].real()));
            snglrVec.addElement(point);
        }

        PointOnCurve3D singularPoint[] =
                new PointOnCurve3D[paramVec.size()];
        for (ijk = 0; ijk < paramVec.size(); ijk++) {
            singularPoint[ijk] = new PointOnCurve3D
                    (this, ((Double) paramVec.elementAt(ijk)).doubleValue(), doCheckDebug);
        }
        return singularPoint;
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * �ϋȓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł��� (���̋�?�͒�?�?�ł���)
     */
    public PointOnCurve3D[] inflexion() throws IndefiniteSolutionException {
        int uicp = nControlPoints();
        int uicp_1 = uicp - 1;
        Point3D[] cp = controlPoints();
        int ijk, klm, mno;

        if (uicp_1 < 2) {
            throw new IndefiniteSolutionException(this);
        }

        Vector paramVec = new Vector();
        Vector crvVec = new Vector();
        Vector3D collinearDir;

        if ((collinearDir = Point3D.collinear(cp, 0, uicp_1)) != null) {
            throw new IndefiniteSolutionException(this);
        }

        CurveCurvature3D crv;
        if ((collinearDir = Point3D.collinear(cp, 0, 2)) != null) {
            crv = curvature(0.0);
            paramVec.addElement(new Double(0.0));
            crvVec.addElement(crv);
        }

        if ((collinearDir = Point3D.collinear(cp, uicp_1 - 2, uicp_1))
                != null) {
            crv = curvature(1.0);
            paramVec.addElement(new Double(1.0));
            crvVec.addElement(crv);
        }

        PolynomialCurve3D polyCurve = polynomialCurve(isPolynomial());
        DoublePolynomial[] crossPoly = polyCurve.crossProductD1D2();

        for (ijk = 0; ijk < crossPoly.length; ijk++)
            crossPoly[ijk] = (DoublePolynomial) crossPoly[ijk].multiply(crossPoly[ijk]); //.normalize();
        ComplexPolynomial complexPoly = ((DoublePolynomial) crossPoly[0].add(crossPoly[1]).add(crossPoly[2])).toComplexPolynomial();

        Complex[] root;

        try {
            root = GeometryPrivateUtils.getRootsByDKA(complexPoly);
        } catch (GeometryPrivateUtils.DKANotConvergeException e) {
            root = e.getValues();
        } catch (GeometryPrivateUtils.ImpossibleEquationException e) {
            throw new FatalException();
        } catch (GeometryPrivateUtils.IndefiniteEquationException e) {
            throw new FatalException();
        }

        ParameterDomain pdmn = parameterDomain();
        for (ijk = 0; ijk < root.length; ijk++) {
            if (!pdmn.isValid(root[ijk].real()))
                continue;

            double pTol = getToleranceForParameter();
            if (root[ijk].real() < (0.0 - pTol)) continue;
            if (root[ijk].real() > (1.0 + pTol)) continue;
            if (root[ijk].real() < 0.0)
                root[ijk] = new Complex(0.0);
            if (root[ijk].real() > 1.0)
                root[ijk] = new Complex(1.0);

            crv = curvature(root[ijk].real());
            double dTol = getToleranceForDistance();
            if (Math.abs(crv.curvature()) > dTol)
                continue;

            double intvl = 1.0 / uicp_1;
            double stake;
            for (klm = 0; klm < paramVec.size(); klm++) {
                if (Math.abs(root[ijk].real() -
                        ((Double) paramVec.elementAt(klm)).
                                doubleValue()) < intvl) {
                    for (mno = 1; mno < uicp_1; mno++) {
                        stake = mno * intvl;
                        if (((root[ijk].real() - stake) *
                                ((Double) paramVec.elementAt(klm)).
                                        doubleValue() - stake) < 0.0)
                            break;
                    }
                    if (mno == uicp_1) {
                        if (Math.abs(crv.curvature())
                                < Math.abs(((Double) crvVec.elementAt(klm)).
                                doubleValue())) {
                            paramVec.addElement(new Double(root[ijk].real()));
                            crvVec.addElement(crv);
                        }
                        break;
                    }
                }
            }
            if (klm < paramVec.size())
                continue;

            paramVec.addElement(new Double(root[ijk].real()));
            crvVec.addElement(crv);
        }

        PointOnCurve3D[] inflexion =
                new PointOnCurve3D[paramVec.size()];
        for (ijk = 0; ijk < paramVec.size(); ijk++)
            inflexion[ijk] = new PointOnCurve3D
                    (this, ((Double) paramVec.elementAt(ijk)).doubleValue(), doCheckDebug);

        return inflexion;
    }

    /**
     * ���̗L��?�S�̂쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @return ���̋�?�S�̂�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve3D toBsplineCurve() {
        double[] www =
                (this.isRational()) ? this.weights : this.makeUniformWeights();

        return new BsplineCurve3D(BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
                this.controlPoints, www);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
        return this.truncate(pint).toBsplineCurve();
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
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̃x�W�G��?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ����x�W�G��?�ƒ�?�̌�_��\����?����ɋA�������ĉ⢂Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
        Axis2Placement3D placement =
                new Axis2Placement3D(mate.pnt(), mate.dir().verticalVector(),
                        mate.dir());
        CartesianTransformationOperator3D transform =
                new CartesianTransformationOperator3D(placement, 1.0);
        int uicp = nControlPoints();
        Point3D[] newCp = new Point3D[uicp];

        for (int i = 0; i < uicp; i++)
            newCp[i] = transform.toLocal(controlPointAt(i));

        double[] weights = weights();
        if (isRational()) {
            double max_weight = 0.0;
            for (int i = 0; i < uicp; i++)
                if (Math.abs(weights[i]) > max_weight)
                    max_weight = weights[i];

            if (max_weight > 0.0)
                for (int i = 0; i < uicp; i++) {
                    weights[i] /= max_weight;
                }
        }
        // ����?���_(newCp)�� Bezier Curve ��?��
        PureBezierCurve3D bzc = new PureBezierCurve3D(newCp, weights, doCheckDebug);

        Point3D[] anotherCp = new Point3D[uicp + 2];
        int m;
        for (m = 0; m < uicp; m++)
            anotherCp[m] = bzc.controlPointAt(m);
        anotherCp[m++] = Point3D.origin;
        anotherCp[m] = new CartesianPoint3D(1.0, 0.0, 0.0);

        //check coplaner
        Vector3D verticalVector = Plane3D.coplaner(anotherCp);

        PureBezierCurve3D oldBzc = null;
        if (verticalVector != null) {
            if (verticalVector.norm() < getToleranceForDistance())
                return new IntersectionPoint3D[0];    // ??
            Vector3D crossVector =
                    verticalVector.crossProduct(Vector3D.xUnitVector).unitized();

            CartesianTransformationOperator3D transformToPlane =
                    new CartesianTransformationOperator3D
                            (Vector3D.xUnitVector, crossVector, verticalVector,
                                    Point3D.origin, 1.0);
            // transform
            Point3D[] cpOnPlane = new Point3D[uicp];
            for (int i = 0; i < uicp; i++)
                cpOnPlane[i] = transformToPlane.toLocal(anotherCp[i]);

            // reserve old control points in Bzc,
            oldBzc = bzc;

            // and set new control points.
            bzc = new PureBezierCurve3D(cpOnPlane, weights, doCheckDebug);
        }

        DoublePolynomial[] realPoly = bzc.polynomial(isPolynomial());

        ComplexPolynomial compPoly;
        if (oldBzc != null) {
            bzc = oldBzc;
            compPoly = realPoly[1].toComplexPolynomial();
        } else {
            DoublePolynomial yPoly = (DoublePolynomial) realPoly[1].multiply(realPoly[1]);
            DoublePolynomial zPoly = (DoublePolynomial) realPoly[2].multiply(realPoly[2]);
            compPoly = ((DoublePolynomial) yPoly.add(zPoly)).toComplexPolynomial();
        }

        Complex[] roots;
        try {
            roots = GeometryPrivateUtils.getRootsByDKA(compPoly);
        } catch (GeometryPrivateUtils.DKANotConvergeException e) {
            roots = e.getValues();
        } catch (GeometryPrivateUtils.ImpossibleEquationException e) {
            throw new FatalException();
        } catch (GeometryPrivateUtils.IndefiniteEquationException e) {
            throw new FatalException();
        }

        int nRoots = roots.length;

        // ��̃`�F�b�N
        Vector lineParam = new Vector();
        Vector bzcParam = new Vector();
        Vector bzcPoints = new Vector();

        for (int j = 0; j < nRoots; j++) {
            double realRoot = roots[j].real();
            if (bzc.parameterValidity(realRoot) == ParameterValidity.OUTSIDE)
                continue;

            if (realRoot < 0.0) realRoot = 0.0;
            if (realRoot > 1.0) realRoot = 1.0;

            Point3D workPoint = bzc.coordinates(realRoot);
            double dTol = bzc.getToleranceForDistance();

            int paramNum = bzcParam.size();
            if (workPoint.y() * workPoint.y() + workPoint.z() * workPoint.z()
                    < getToleranceForDistance2()) {
                int k;
                for (k = 0; k < paramNum; k++) {
                    double paramA =
                            ((Double) bzcParam.elementAt(k)).doubleValue();
                    double paramB =
                            ((Double) lineParam.elementAt(k)).doubleValue();
                    if ((Math.abs(paramB - workPoint.x()) < dTol)
                            && (bzc.identicalParameter(realRoot, paramA)))
                        break;
                }
                if (k >= paramNum) {
                    bzcParam.addElement(new Double(realRoot));
                    lineParam.addElement(new Double(workPoint.x()));
                    bzcPoints.addElement(transform.toEnclosed(workPoint));
                }
            }
        }

        int num = bzcParam.size();
        IntersectionPoint3D[] intersectPoint = new
                IntersectionPoint3D[num];
        double mateLength = mate.dir().length();
        for (int i = 0; i < num; i++) {
            double work = ((Double) lineParam.elementAt(i)).doubleValue() / mateLength;
            PointOnCurve3D pointOnLine = new PointOnCurve3D(mate, work, doCheckDebug);

            work = ((Double) bzcParam.elementAt(i)).doubleValue();
            PointOnCurve3D pointOnBzc = new PointOnCurve3D(this, work, doCheckDebug);

            Point3D coordinates = (Point3D) bzcPoints.elementAt(i);

            if (!doExchange)
                intersectPoint[i] = new IntersectionPoint3D
                        (coordinates, pointOnBzc, pointOnLine, doCheckDebug);
            else
                intersectPoint[i] = new IntersectionPoint3D
                        (coordinates, pointOnLine, pointOnBzc, doCheckDebug);
        }

        return intersectPoint;
    }

    /**
     * ���̃x�W�G��?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �~�̃N���X��?u�~ vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link Circle3D#intersect(PureBezierCurve3D,boolean)
     * Circle3D.intersect(PureBezierCurve3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Circle3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̃x�W�G��?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �ȉ~�̃N���X��?u�ȉ~ vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link Ellipse3D#intersect(PureBezierCurve3D,boolean)
     * Ellipse3D.intersect(PureBezierCurve3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̃x�W�G��?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?�̃N���X��?u��?� vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link Parabola3D#intersect(PureBezierCurve3D,boolean)
     * Parabola3D.intersect(PureBezierCurve3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̃x�W�G��?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �o��?�̃N���X��?u�o��?� vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link Hyperbola3D#intersect(PureBezierCurve3D,boolean)
     * Hyperbola3D.intersect(PureBezierCurve3D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Hyperbola3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̃x�W�G��?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �|�����C���̃N���X��?u�|�����C�� vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link Polyline3D#intersect(PureBezierCurve3D,boolean)
     * Polyline3D.intersect(PureBezierCurve3D, boolean)}
     * ��?s�ȂBĂ���?B
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
     * ���̃x�W�G��?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBzcBzc3D#intersection(PureBezierCurve3D,PureBezierCurve3D,boolean)
     * IntsBzcBzc3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
        return IntsBzcBzc3D.intersection(this, mate, doExchange);
    }

    /**
     * ���̃x�W�G��?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBzcBsc3D#intersection(PureBezierCurve3D,BsplineCurve3D,boolean)
     * IntsBzcBsc3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
        return IntsBzcBsc3D.intersection(this, mate, doExchange);
    }

    /**
     * ���̃x�W�G��?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �g������?�̃N���X��?u�g������?� vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve3D#intersect(PureBezierCurve3D,boolean)
     * TrimmedCurve3D.intersect(PureBezierCurve3D, boolean)}
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
     * ���̃x�W�G��?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment3D#intersect(PureBezierCurve3D,boolean)
     * CompositeCurveSegment3D.intersect(PureBezierCurve3D, boolean)}
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
     * ���̃x�W�G��?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�N���X��?u��?���?� vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve3D#intersect(PureBezierCurve3D,boolean)
     * CompositeCurve3D.intersect(PureBezierCurve3D, boolean)}
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
     * ���̋�?�Ƒ��̋Ȗʂ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��_�̔z��
     */
    public IntersectionPoint3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (��?͋Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ� (��?͋Ȗ�)
     * @return ��_�̔z��
     */
    public IntersectionPoint3D[] intersect(ElementarySurface3D mate) {
        return mate.intersect(this, true);
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
        return mate.intersect(this, !doExchange);
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
        return mate.intersect(this, !doExchange);
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
        return mate.intersect(this, !doExchange);
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
     * ���̗L��?�Ƒ��̗L��?� (?�) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ?�̃N���X��?u?� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link BoundedLine3D#interfere(PureBezierCurve3D,boolean)
     * BoundedLine3D.interfere(PureBezierCurve3D, boolean)}
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
     * �|�����C���̃N���X��?u�|�����C�� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link Polyline3D#interfere(PureBezierCurve3D,boolean)
     * Polyline3D.interfere(PureBezierCurve3D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(Polyline3D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�x�W�G��?�) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBzcBzc3D#interference(PureBezierCurve3D,PureBezierCurve3D,boolean)
     * IntsBzcBzc3D.interference}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(PureBezierCurve3D mate,
                                         boolean doExchange) {
        return IntsBzcBzc3D.interference(this, mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�a�X�v���C����?�) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBzcBsc3D#interference(PureBezierCurve3D,BsplineCurve3D,boolean)
     * IntsBzcBsc3D.interference}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(BsplineCurve3D mate,
                                         boolean doExchange) {
        return IntsBzcBsc3D.interference(this, mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�g������?�) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * �g������?�̃N���X��?u�g������?� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link TrimmedCurve3D#interfere(PureBezierCurve3D,boolean)
     * TrimmedCurve3D.interfere(PureBezierCurve3D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(TrimmedCurve3D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�Z�O�?���g) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurveSegment3D#interfere(PureBezierCurve3D,boolean)
     * CompositeCurveSegment3D.interfere(PureBezierCurve3D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(CompositeCurveSegment3D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�) �Ƃ̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?���?�N���X��?u��?���?� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurve3D#interfere(PureBezierCurve3D,boolean)
     * CompositeCurve3D.interfere(PureBezierCurve3D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference3D[] interfere(CompositeCurve3D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̃x�W�G��?��?A�^����ꂽ�p���??[�^�l�œ�ɕ�������?B
     * <p/>
     * param �̒l��?A���̃x�W�G��?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f��?u�n�_���番���_�܂ł�\���x�W�G��?�?v?A
     * ��Ԗڂ̗v�f��?u�����_����?I�_�܂ł�\���x�W�G��?�?v
     * �����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ������̃x�W�G��?��܂ޔz��
     * @see ParameterOutOfRange
     */
    public PureBezierCurve3D[] divide(double param) {
        double[][] cntlPnts;
        double[][][] bzcs_array;
        PureBezierCurve3D[] bzcs;
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        try {
            bzcs_array = PureBezierCurveEvaluation.divide(cntlPnts, param);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
        bzcs = new PureBezierCurve3D[2];
        for (int i = 0; i < 2; i++) {
            try {
                bzcs[i] = new PureBezierCurve3D(bzcs_array[i]);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        return bzcs;
    }

    /**
     * ���̃x�W�G��?��?A�^����ꂽ�p���??[�^��Ԃ�?ؒf����?B
     * <p/>
     * section �̑?���l������?�?��ɂ�?A?i?s���]�����x�W�G��?��Ԃ�?B
     * </p>
     * <p/>
     * section �̒l��?A���̃x�W�G��?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param section ?ؒf���Ďc��������\���p���??[�^���
     * @return ?ؒf���Ďc����������\���x�W�G��?�
     * @see ParameterOutOfRange
     */
    public PureBezierCurve3D truncate(ParameterSection section) {
        double start_par, end_par;
        PureBezierCurve3D t_bzc;

        start_par = checkParameter(section.lower());
        end_par = checkParameter(section.upper());

        t_bzc = divide(start_par)[1];
        end_par = (end_par - start_par) / (1.0 - start_par);

        t_bzc = t_bzc.divide(end_par)[0];

        if (section.increase() < 0.0)
            t_bzc = t_bzc.reverse();

        return t_bzc;
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?�?�̂���_ P(t) ����^����ꂽ�_�֌�x�N�g����
     * P(t) �ɂ�����?ڃx�N�g�� P'(t) �̓�?ς�\����?��� D(t) ��?�?���?A
     * �����?��ӂƂ����?���� D(t) = 0 ��⢂Ă���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     */
    public PointOnCurve3D[] projectFrom(Point3D mate) {
        DoublePolynomial pointPoly[] = polynomial(isPolynomial());
        DoublePolynomial offsPoly[] = new DoublePolynomial[3];
        int coef_size = pointPoly.length;

        if (isRational()) {
            offsPoly[0] = pointPoly[3].scalarMultiply(mate.x());
            offsPoly[1] = pointPoly[3].scalarMultiply(mate.y());
            offsPoly[2] = pointPoly[3].scalarMultiply(mate.z());
        } else {
            double coef[][] = {{mate.x()}, {mate.y()}, {mate.z()}};
            offsPoly[0] = new DoublePolynomial(coef[0]);
            offsPoly[1] = new DoublePolynomial(coef[1]);
            offsPoly[2] = new DoublePolynomial(coef[2]);
        }

        for (int i = 0; i < 3; i++)
            pointPoly[i] = (DoublePolynomial) pointPoly[i].subtract(offsPoly[i]);

        DoublePolynomial tangPoly[] = new DoublePolynomial[coef_size];
        DoublePolynomial dotePoly[] = new DoublePolynomial[3];

        // polynomial of tangent vector
        for (int klm = 0; klm < coef_size; klm++)
            tangPoly[klm] = (DoublePolynomial) pointPoly[klm].differentiate();

        if (!isRational()) {
            for (int klm = 0; klm < 3; klm++)
                dotePoly[klm] = (DoublePolynomial) pointPoly[klm].multiply(tangPoly[klm]);
        } else {
            DoublePolynomial work0, work1, work2, sub;
            double[] work3;

            for (int klm = 0; klm < 3; klm++) {
                work0 = (DoublePolynomial) pointPoly[3].multiply(tangPoly[klm]);
                work1 = (DoublePolynomial) tangPoly[3].multiply(pointPoly[klm]);
                // ((a * t^n) * (nb * t^(n-1))) - ((na * t^(n-1)) * (b * t^n)) == 0
                work2 = (DoublePolynomial) work0.subtract(work1);
                work3 = GeometryPrivateUtils.coefficientsBetween(work2, 0, (work2.degree() - 1));
                sub = new DoublePolynomial(work3);

                dotePoly[klm] = (DoublePolynomial) pointPoly[klm].multiply(sub);
            }
        }

        DoublePolynomial rpoly = (DoublePolynomial) dotePoly[0].add(dotePoly[1]).add(dotePoly[2]);

        ComplexPolynomial dtPoly;
        try {
            dtPoly = rpoly.toComplexPolynomial();
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        Complex[] root;

        try {
            root = GeometryPrivateUtils.getRootsByDKA(dtPoly);
        } catch (GeometryPrivateUtils.DKANotConvergeException e) {
            root = e.getValues();
        } catch (GeometryPrivateUtils.ImpossibleEquationException e) {
            throw new FatalException();
        } catch (GeometryPrivateUtils.IndefiniteEquationException e) {
            throw new FatalException();
        }

        PointOnGeometryList projList = new PointOnGeometryList();
        ParameterDomain domain = parameterDomain();
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();

        for (int i = 0; i < root.length; i++) {
            PointOnCurve3D proj;

            double par = root[i].real();

            if (!domain.isValid(par))
                continue;

            if (par > 1.0)
                par = 1.0;
            if (par < 0.0)
                par = 0.0;

            proj = checkProjection(par, mate, dTol * dTol);
            if (proj != null)
                projList.addPoint(proj);
        }
        return projList.toPointOnCurve3DArray();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * section �̒l��?A���̃x�W�G��?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param section   ��?�ߎ�����p���??[�^���
     * @param tolerance �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     * @see ParameterOutOfRange
     */
    public Polyline3D toPolyline(ParameterSection section,
                                 ToleranceForDistance tolerance) {
        PureBezierCurve3D root_bzc;
        double sp, ep;
        IntervalInfo root_info;
        BinaryTree pnt_tree;
        int no_pnts;
        Point3D[] pnts;
        FillInfo fill_info;
        double tol = tolerance.value();
        double tol_2 = tol * tol;
        ConditionOfOperation condition = ConditionOfOperation.getCondition();
        double tol_p = condition.getToleranceForParameter();

        root_bzc = truncate(section.positiveIncrease());
        sp = checkParameter(section.lower());
        ep = checkParameter(section.upper());
        root_info = new IntervalInfo(root_bzc, sp, ep);

        pnt_tree = new BinaryTree(root_info);

        no_pnts = divideInterval(2, pnt_tree.rootNode(), tol_2);

        pnts = new Point3D[no_pnts];
        if (true) {
            fill_info = new FillInfo(this, pnts, 0, tol_p, ep);
            pnt_tree.rootNode().preOrderTraverse(new fillArray(), fill_info);
        } else {
            Enumeration enumeration = pnt_tree.rootNode().preOrderEnumeration();
            BinaryTree.Node node;
            IntervalInfo bi;
            int i = 0;

            while (enumeration.hasMoreElements()) {
                node = (BinaryTree.Node) enumeration.nextElement();
                if ((node.left() == null) && (node.right() == null)) {
                    bi = (IntervalInfo) node.data();

                    try {
                        pnts[i++] = new PointOnCurve3D
                                (bi.bzc().controlPointAt(0), this, bi.sp());
                        if (i == (no_pnts - 1)) {
                            pnts[i] = new PointOnCurve3D
                                    (bi.bzc().controlPointAt(bi.bzc().nControlPoints() - 1),
                                            this, bi.ep());
                        }
                    } catch (InvalidArgumentValueException e) {
                        throw new FatalException();
                    }
                }
            }
        }

        if (no_pnts == 2 && pnts[0].identical(pnts[1]))
            throw new ZeroLengthException();

        if (section.increase() > 0.0) {
            return new Polyline3D(pnts);
        } else {
            return new Polyline3D(pnts).reverse();
        }
    }

    /**
     * �x�W�G��?�̂����Ԃ��Ք�N���X?B
     */
    private class IntervalInfo {
        /**
         * ���̋�ԂɑΉ����� (?ו������ꂽ) �x�W�G��?�?B
         */
        private PureBezierCurve3D bzc;    // subdivided bezier curve

        /**
         * ���̋�Ԃ̊J�n�_�ɑΉ����錳��?�̃p���??[�^�l?B
         */
        private double sp;        // start value of this curve in global parametrization

        /**
         * ���̋�Ԃ�?I���_�ɑΉ����錳��?�̃p���??[�^�l?B
         */
        private double ep;        // end value of this curve in global parametrization

        /**
         * �e�t�B?[���h�̒l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param bzc ��ԂɑΉ����� (?ו������ꂽ) �x�W�G��?�
         * @param sp  ��Ԃ̊J�n�_�ɑΉ����錳��?�̃p���??[�^�l
         * @param ep  ��Ԃ�?I���_�ɑΉ����錳��?�̃p���??[�^�l
         */
        private IntervalInfo(PureBezierCurve3D bzc, double sp, double ep) {
            super();
            this.bzc = bzc;
            this.sp = sp;
            this.ep = ep;
        }

        /**
         * ���̋�ԂɑΉ����� (?ו������ꂽ) �x�W�G��?��Ԃ�?B
         *
         * @return ���̋�ԂɑΉ����� (?ו������ꂽ) �x�W�G��?�
         */
        private PureBezierCurve3D bzc() {
            return bzc;
        }

        /**
         * ���̋�Ԃ̊J�n�_�ɑΉ����錳��?�̃p���??[�^�l��Ԃ�?B
         *
         * @return ���̋�Ԃ̊J�n�_�ɑΉ����錳��?�̃p���??[�^�l
         */
        private double sp() {
            return sp;
        }

        /**
         * ���̋�Ԃ�?I���_�ɑΉ����錳��?�̃p���??[�^�l��Ԃ�?B
         *
         * @return ���̋�Ԃ�?I���_�ɑΉ����錳��?�̃p���??[�^�l
         */
        private double ep() {
            return ep;
        }
    }

    /**
     * ���̋�?�̂����Ԃ�?�ߎ�����_����߂邽�߂̔z���?�N���X?B
     */
    private class FillInfo {
        /**
         * ���̋�?�?B
         */
        private ParametricCurve3D basisCurve;

        /**
         * ���̋�?�̂����Ԃ�?�ߎ�����_��?B
         */
        private Point3D[] pnts;

        /**
         * ���̑��� pnts[index] �ɑ΂���?s�Ȃ���ׂ��ł��邱�Ƃ��l?B
         */
        private int index;

        /**
         * �p���??[�^�l�̋��e��?�?B
         * <p/>
         * ���܂̂Ƃ���?A�L��ɗ��p����Ă��Ȃ�?B
         * </p>
         */
        private double tol_p;

        /**
         * ��?�ߎ�����p���??[�^��Ԃ�?��?B
         * <p/>
         * ���܂̂Ƃ���?A�L��ɗ��p����Ă��Ȃ�?B
         * </p>
         */
        private double ep;

        /**
         * �e�t�B?[���h�̒l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param basisCurve ���̋�?�
         * @param pnts       ���̋�?�̂����Ԃ�?�ߎ�����_��
         * @param index      pnts[] �ւ�?u��?v�̑���?ۂ̑��ʒu���C���f�b�N�X
         * @param tol_p      �p���??[�^�l�̋��e��?�
         * @param ep         ��?�ߎ�����p���??[�^��Ԃ�?��
         */
        private FillInfo(ParametricCurve3D basisCurve,
                         Point3D[] pnts,
                         int index,
                         double tol_p,
                         double ep) {
            super();
            this.basisCurve = basisCurve;
            this.pnts = pnts;
            this.index = index;
            this.tol_p = tol_p;
            this.ep = ep;
        }
    }

    /**
     * ���̃x�W�G��?��?A�^����ꂽ��Ԃ�?�ߎ�����_���?�������?B
     * <p/>
     * ��?��������_���?��� crnt_node �ȉ��̓񕪖ؓ�Ɏ�߂���?B
     * </p>
     *
     * @param no_pnts   ��?��?�ߎ�����_���̓_��?�
     * @param crnt_node ���������Ԃ�ێ?����񕪖؂̃m?[�h
     * @param tol_2     ��?�ߎ���?��x�Ƃ��ė^����ꂽ?u�����̋��e��?�?v
     * @return ��?��?�ߎ�����_���̓_��?�
     * @see #checkInterval(PureBezierCurve3D.IntervalInfo,double)
     */
    private int divideInterval(int no_pnts,
                               BinaryTree.Node crnt_node,
                               double tol_2) {
        IntervalInfo crnt_info;
        double mid_param;

        BinaryTree.Node left_node;
        IntervalInfo left_info;
        PureBezierCurve3D left_bzc;

        BinaryTree.Node right_node;
        IntervalInfo right_info;
        PureBezierCurve3D right_bzc;

        final double half_point = 0.5;
        PureBezierCurve3D[] div_bzcs;

        no_pnts++;

        crnt_info = (IntervalInfo) crnt_node.data();
        mid_param = (crnt_info.sp() + crnt_info.ep()) / 2.0;

        /*
        * divide current interval into two
        */
        try {
            div_bzcs = crnt_info.bzc().divide(half_point);
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
        left_bzc = div_bzcs[0];
        right_bzc = div_bzcs[1];

        left_info = new IntervalInfo(left_bzc, crnt_info.sp(), mid_param);
        left_node = crnt_node.makeLeft(left_info);

        right_info = new IntervalInfo(right_bzc, mid_param, crnt_info.ep());
        right_node = crnt_node.makeRight(right_info);

        /*
        * check
        */
        if (!checkInterval(left_info, tol_2))
            no_pnts = divideInterval(no_pnts, left_node, tol_2);

        if (!checkInterval(right_info, tol_2))
            no_pnts = divideInterval(no_pnts, right_node, tol_2);

        return no_pnts;
    }

    /**
     * ���̋�?��?A�^����ꂽ��Ԃ�?u?���?v���w���?��x���?��������ۂ���Ԃ�?B
     *
     * @param bi    ��?�?�̋��
     * @param tol_2 ��?�ߎ���?��x�Ƃ��ė^����ꂽ?u�����̋��e��?�?v�̎�?�
     * @return ��Ԃ�?u?���?v���w���?��x���?�������� true?A�����łȂ���� false
     */
    private boolean checkInterval(IntervalInfo bi, double tol_2) {
        PureBezierCurve3D bzc = bi.bzc();
        int uicp_1 = bzc.nControlPoints() - 1;

        Vector3D edirs;
        Point3D ppnt;
        double dist;

        int i;

        edirs = bzc.controlPointAt(uicp_1).subtract(bzc.controlPointAt(0));

        for (i = 1; i < uicp_1; i++) {
            ppnt = projectPointLine(bzc.controlPointAt(i), bzc.controlPointAt(0), edirs);
            dist = bzc.controlPointAt(i).subtract(ppnt).norm();
            if (dist > tol_2)
                return false;
        }
        return true;
    }

    /**
     * ����_�⠂钼?�ɓ��e����?B
     *
     * @param dApnt  ���e���̓_
     * @param dB_pnt ��?�?�̓_
     * @param dB_dir ��?�̕��x�N�g��
     * @return ���e�_
     */
    private Point3D projectPointLine(Point3D dApnt,
                                     Point3D dB_pnt,
                                     Vector3D dB_dir) {
        double magni_dir;    /* magnitude of dB_dir */
        Vector3D euvec;    /* unitized vector of line */
        Vector3D evpp;    /* vector from dB_pnt to dApnt */
        double edot;        /* dot product */
        double m_eps = MachineEpsilon.DOUBLE;

        if ((magni_dir = dB_dir.magnitude()) < m_eps)
            euvec = Vector3D.zeroVector;
        else
            euvec = dB_dir.divide(magni_dir);

        evpp = dApnt.subtract(dB_pnt);
        edot = euvec.dotProduct(evpp);

        return dB_pnt.add(euvec.multiply(edot));
    }

    /**
     * �q�m?[�h��?���Ȃ��m?[�h��?u�f?[�^?v��
     * �^����ꂽ�z��ɑ��� BinaryTree.TraverseProc?B
     */
    private class fillArray implements BinaryTree.TraverseProc {
        /**
         * �q�m?[�h��?���Ȃ��m?[�h��?u�f?[�^?v��^����ꂽ�z��ɑ���?B
         * <p/>
         * pdata �� {@link PureBezierCurve3D.FillInfo PureBezierCurve3D.FillInfo} �N���X��
         * �C���X�^���X�łȂ���΂Ȃ�Ȃ�?B
         * </p>
         *
         * @param node  ?��?��?ۂƂȂ�m?[�h
         * @param ctl   ?��?��J�n�����m?[�h���� node �܂ł�?[�� (�Q?Ƃ��Ȃ�)
         * @param pdata �m?[�h��?u�f?[�^?v�����z��
         * @see #toPolyline(ParameterSection,ToleranceForDistance)
         */
        public boolean doit(BinaryTree.Node node, int ctl, Object pdata) {
            if ((node.left() == null) && (node.right() == null)) {
                try {
                    FillInfo fill_info = (FillInfo) pdata;
                    int idx = fill_info.index;
                    IntervalInfo bi = (IntervalInfo) node.data();

                    fill_info.pnts[idx++] = new PointOnCurve3D
                            (bi.bzc().controlPointAt(0), fill_info.basisCurve, bi.sp());

                    if (idx == (fill_info.pnts.length - 1))
                        fill_info.pnts[idx++] = new PointOnCurve3D
                                (bi.bzc().controlPointAt(bi.bzc().nControlPoints() - 1),
                                        fill_info.basisCurve, bi.ep());
                    fill_info.index = idx;
                } catch (InvalidArgumentValueException e) {
                    throw new FatalException();
                }
            }
            return false;
        }
    }

    /**
     * ���̗L��?�̊J�n�_��Ԃ�?B
     *
     * @return �J�n�_
     */
    public Point3D startPoint() {
        return controlPointAt(0);
    }

    /**
     * ���̗L��?��?I���_��Ԃ�?B
     *
     * @return ?I���_
     */
    public Point3D endPoint() {
        int index = nControlPoints() - 1;
        return controlPointAt(index);
    }

    /**
     * ���̃x�W�G��?�𔽓]�����x�W�G��?��Ԃ�?B
     *
     * @return ���]�����x�W�G��?�
     */
    PureBezierCurve3D reverse() {
        boolean isRat = isRational();
        int uicp = nControlPoints();
        Point3D[] rCp = new Point3D[uicp];
        double[] rWt = null;
        int i, j;

        if (isRat)
            rWt = new double[uicp];
        for (i = 0, j = uicp - 1; i < uicp; i++, j--) {
            rCp[i] = controlPointAt(j);
            if (isRat)
                rWt[i] = weightAt(j);
        }
        try {
            if (isRat)
                return new PureBezierCurve3D(rCp, rWt);
            else
                return new PureBezierCurve3D(rCp);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ�?B
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     */
    public ParametricCurve3D parallelTranslate(Vector3D moveVec) {
        Point3D[] pnts = new Point3D[nControlPoints()];
        for (int i = 0; i < nControlPoints(); i++)
            pnts[i] = controlPointAt(i).add(moveVec);
        if (isPolynomial())
            return new PureBezierCurve3D(pnts);
        else
            return new PureBezierCurve3D(pnts, weights);
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     *
     * @return �p���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        try {
            return new ParameterDomain(false, 0.0, 1.0);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /*
    * �^����ꂽ�p���??[�^�l��?A���̋�?�̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
    * <p>
    * �^����ꂽ�p���??[�^�l�����̋�?�̒�`���O��Ă���?�?��ɂ�
    * ParameterOutOfRange �̗�O��?�����?B
    * </p>
    *
    * @param param	�p���??[�^�l
    * @return	�K�v�ɉ����Ă��̋�?�̒�`���Ɋۂ߂�ꂽ�p���??[�^�l
    * @see	AbstractParametricCurve#checkValidity(double)
    * @see	ParameterDomain#force(double)
    * @see	ParameterOutOfRange
    */
    private double checkParameter(double param) {
        checkValidity(param);
        return parameterDomain().force(param);
    }

    /**
     * ���̋�?��?A�`?�⻂̂܂܂ɂ���?A��?�����?グ����?��Ԃ�?B
     *
     * @return ����`?��?A��?������?オ�B���?�
     */
    public PureBezierCurve3D elevateOneDegree() {
        boolean isPoly = this.isPolynomial();
        int nCP = this.nControlPoints();

        double[][] newCP =
                FreeformCurveWithControlPoints3D.allocateDoubleArray(isPoly,
                        (nCP + 1));
        this.setCoordinatesToDoubleArray(isPoly, nCP, newCP);
        PureBezierCurveEvaluation.elevateOneDegree(nCP, newCP);

        return new PureBezierCurve3D(newCP);
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve3D#PURE_BEZIER_CURVE_3D ParametricCurve3D.PURE_BEZIER_CURVE_3D}
     */
    int type() {
        return PURE_BEZIER_CURVE_3D;
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
        int n_pnts = nControlPoints();
        Point3D[] pnts = new Point3D[n_pnts];

        for (int i = 0; i < n_pnts; i++)
            pnts[i] = controlPointAt(i).rotateZ(trns, rCos, rSin);

        PureBezierCurve3D result;
        if (isRational())
            result = new PureBezierCurve3D(pnts, weights());
        else
            result = new PureBezierCurve3D(pnts);

        return result;
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

        int itry = 0, limit = nControlPoints();
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
            point = controlPointAt(itry);
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
        Point3D[] tControlPoints =
                Point3D.transform(this.controlPoints,
                        reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        if (this.isPolynomial() == true)
            return new PureBezierCurve3D(tControlPoints);
        else
            return new PureBezierCurve3D(tControlPoints, this.weights);
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
        StringBuffer buf = new StringBuffer();

        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tcontrolPoints");
        for (int i = 0; i < nControlPoints(); i++) {
            controlPointAt(i).output(writer, indent + 2);
        }
        if (weights() != null) {
            writer.println(indent_tab + "\tweights ");
            int i = 0;
            while (true) {
                for (int j = 0; j < 10 && i < weights().length; j++, i++) {
                    writer.print(" " + weightAt(i));
                }
                writer.println();
                if (i < weights().length) {
                    writer.print(indent_tab + "\t");
                } else {
                    break;
                }
            }
        }
        writer.println(indent_tab + "End");
    }
}
