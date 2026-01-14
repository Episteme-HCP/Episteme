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
 * �Q���� : ��L�? (��?���) �x�W�G��?��їL�?�x�W�G��?��\���N���X
 * <p/>
 * ���̃N���X�ɓWL�ȑ�?���\���t�B?[���h�͓BɂȂ�?B
 * ?���_��Ȃǂ�ێ?����t�B?[���h�ɂ��Ă�?A
 * {@link FreeformCurveWithControlPoints2D �X?[�p?[�N���X�̉�?�} ��Q?�?B
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
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:18 $
 */

public class PureBezierCurve2D extends FreeformCurveWithControlPoints2D {
    /**
     * ?���_���^���đ�?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[])
     * super}(controlPoints)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param controlPoints ?���_�̔z��
     */
    public PureBezierCurve2D(Point2D[] controlPoints) {
        super(controlPoints);
    }

    /**
     * ?���_���?d�ݗ��^���ėL�?��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[],double[])
     * super}(controlPoints, weights)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param controlPoints ?���_�̔z��
     * @param weights       ?d�݂̔z��
     */
    public PureBezierCurve2D(Point2D[] controlPoints,
                             double[] weights) {
        super(controlPoints, weights);
    }

    /**
     * ?���_���?d�ݗ��^����
     * ��?�����?� (���邢�͗L�?��?�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[],double[],boolean)
     * super}(controlPoints, weights, doCheck)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param controlPoints ?���_�̔z��?B
     * @param weights       ?d�݂̔z��
     * @param doCheck       ��?��̃`�F�b�N�ⷂ邩�ǂ���
     */
    public PureBezierCurve2D(Point2D[] controlPoints,
                             double[] weights,
                             boolean doCheck) {
        super(controlPoints, weights, doCheck);
    }

    /**
     * ?���_ (��?d��) ��񎟌��z��ŗ^����
     * ��?�����?� (���邢�͗L�?��?�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(double[][])
     * super}(cpArray)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param cpArray ?���_ (�����?d��) �̔z��
     */
    PureBezierCurve2D(double[][] cpArray) {
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
     * ���̋�?��L�?�ł���� 2?A
     * �L�?�ł���� 3 �ł���?B
     * </p>
     * <p/>
     * ��L�?��?��?�?�?A
     * R[0] �� X ?���?A
     * R[1] �� Y ?���
     * �̑�?����\����\��?B
     * </p>
     * <p/>
     * �L�?��?��?�?�?A
     * R[0] �� WX ?���?A
     * R[1] �� WY ?���
     * R[2] �� W ?���
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
    public PolynomialCurve2D polynomialCurve(boolean isPoly) {
        DoublePolynomial[] poly = polynomial(isPoly);
        if (isPoly) {
            return new PolynomialCurve2D(poly[0], poly[1]);
        } else {
            return new PolynomialCurve2D(poly[0], poly[1], poly[2]);
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

            for (int ijk = 0; ijk < 2; ijk++)
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
                    final double[] tang = new double[2];
                    for (int ijk = 0; ijk < 2; ijk++)
                        tang[ijk] = deriv[ijk].map(parameter);

                    return Math.sqrt(tang[0] * tang[0] +
                            tang[1] * tang[1]);
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
    public Point2D coordinates(double param) {
        double[][] cntlPnts;
        double[] d0D;
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        d0D = PureBezierCurveEvaluation.coordinates(cntlPnts, param);
        if (!isPoly) {
            convRational0Deriv(d0D);
        }
        return new CartesianPoint2D(d0D[0], d0D[1]);
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
    public Vector2D tangentVector(double param) {
        double[][] cntlPnts;
        double[] d1D = new double[3];
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        if (isPoly) {
            PureBezierCurveEvaluation.evaluation(cntlPnts, param, null, d1D, null, null);
        } else {
            double[] d0D = new double[3];

            PureBezierCurveEvaluation.evaluation(cntlPnts, param, d0D, d1D, null, null);
            convRational1Deriv(d0D, d1D);
        }
        return new LiteralVector2D(d1D[0], d1D[1]);
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
    public CurveCurvature2D curvature(double param) {
        int degree;
        CurveDerivative2D deriv;
        boolean tang0;
        double tang_leng;
        double dDcrv;
        Vector2D dDnrm;
        CurveCurvature2D crv;
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
            dDnrm = Vector2D.zeroVector;
        } else {
            double ewvec;

            tang_leng = Math.sqrt(tang_leng);
            dDcrv = tang_leng * tang_leng * tang_leng;

            ewvec = deriv.d1D().zOfCrossProduct(deriv.d2D());
            dDcrv = Math.abs(ewvec) / dDcrv;

            if (ewvec < 0.0) {
                dDnrm = new LiteralVector2D(deriv.d1D().y(),
                        (-deriv.d1D().x()));
            } else {
                dDnrm = new LiteralVector2D((-deriv.d1D().y()),
                        deriv.d1D().x());
            }

            dDnrm = dDnrm.unitized();
        }

        return new CurveCurvature2D(dDcrv, dDnrm);
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
    public CurveDerivative2D evaluation(double param) {
        double[][] cntlPnts;
        double[] ld0D = new double[3];
        double[] ld1D = new double[3];
        double[] ld2D = new double[3];
        Point2D d0D;
        Vector2D d1D;
        Vector2D d2D;
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        PureBezierCurveEvaluation.evaluation(cntlPnts, param, ld0D, ld1D, ld2D, null);
        if (!isPoly) {
            convRational2Deriv(ld0D, ld1D, ld2D);
        }
        d0D = new CartesianPoint2D(ld0D[0], ld0D[1]);
        d1D = new LiteralVector2D(ld1D[0], ld1D[1]);
        d2D = new LiteralVector2D(ld2D[0], ld2D[1]);
        return new CurveDerivative2D(d0D, d1D, d2D);
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
    public Point2D blossoming(double[] parameters) {
        double[] adjustedParameters = new double[this.nControlPoints() - 1];
        for (int i = 0; i < this.nControlPoints() - 1; i++)
            adjustedParameters[i] = this.checkParameter(parameters[i]);
        boolean isPoly = this.isPolynomial();

        double[] d0D =
                PureBezierCurveEvaluation.blossoming(this.toDoubleArray(isPoly),
                        adjustedParameters);
        if (isPoly == true)
            return new CartesianPoint2D(d0D[0], d0D[1]);
        else
            return new HomogeneousPoint2D(d0D[0], d0D[1], d0D[2]);
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
    public PointOnCurve2D[] singular() throws IndefiniteSolutionException {
        int uicp = nControlPoints();
        int uicp_1 = uicp - 1;
        int ijk, klm;
        ParameterDomain pdmn;
        Vector paramVec = new Vector();

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
        }
        if (endPoint().identical(controlPointAt(uicp_1 - 1))) {
            paramVec.addElement(new Double(1.0));
        }

        DoublePolynomial pointPoly[] = polynomial(isPolynomial());
        int polySize = pointPoly.length;
        DoublePolynomial tangPoly[] = new DoublePolynomial[polySize];
        DoublePolynomial dotePoly[] = new DoublePolynomial[2];

        for (klm = 0; klm < polySize; klm++) {
            tangPoly[klm] = (DoublePolynomial) pointPoly[klm].differentiate();
        }

        if (!isRational()) {
            for (klm = 0; klm < 2; klm++)
                dotePoly[klm] = (DoublePolynomial) tangPoly[klm].multiply(tangPoly[klm]);
        } else {
            for (klm = 0; klm < 2; klm++) {
                DoublePolynomial work0, work1, sub;
                work0 = (DoublePolynomial) pointPoly[2].multiply(tangPoly[klm]);
                work1 = (DoublePolynomial) tangPoly[2].multiply(pointPoly[klm]);
                sub = (DoublePolynomial) work0.subtract(work1);
                dotePoly[klm] = (DoublePolynomial) sub.multiply(sub);
            }
        }

        ComplexPolynomial dtPoly;

        try {
            dtPoly = ((DoublePolynomial) dotePoly[0].add(dotePoly[1])).toComplexPolynomial();    //.normalize()
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
        for (ijk = 0; ijk < root.length; ijk++) {
            if (!pdmn.isValid(root[ijk].real())) {
                continue;
            }
            if (root[ijk].real() < 0.0)
                root[ijk] = new Complex(0.0);
            if (root[ijk].real() > 1.0)
                root[ijk] = new Complex(1.0);

            Point2D point;
            Vector2D tangent;

            try {
                point = coordinates(root[ijk].real());
                tangent = tangentVector(root[ijk].real());
            } catch (ParameterOutOfRange e) {
                throw new FatalException();
            }

            double dTol2 = getToleranceForDistance2();
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
        }

        PointOnCurve2D singularPoint[] =
                new PointOnCurve2D[paramVec.size()];
        for (ijk = 0; ijk < paramVec.size(); ijk++) {
            singularPoint[ijk] = new PointOnCurve2D
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
    public PointOnCurve2D[] inflexion() throws IndefiniteSolutionException {
        int uicp = nControlPoints();
        int uicp_1 = uicp - 1;
        Point2D[] cp = controlPoints();
        int ijk, klm, mno;

        if (uicp_1 < 2) {
            throw new IndefiniteSolutionException(this);
        }

        Vector paramVec = new Vector();
        Vector crvVec = new Vector();
        Vector2D collinearDir;
        if ((collinearDir = Point2D.collinear(cp, 0, uicp_1)) != null) {
            throw new IndefiniteSolutionException(this);
        }

        CurveCurvature2D crv;
        if ((collinearDir = Point2D.collinear(cp, 0, 2)) != null) {
            crv = curvature(0.0);
            paramVec.addElement(new Double(0.0));
            crvVec.addElement(crv);
        }

        if ((collinearDir = Point2D.collinear(cp, uicp_1 - 2, uicp_1))
                != null) {
            crv = curvature(1.0);
            paramVec.addElement(new Double(1.0));
            crvVec.addElement(crv);
        }

        PolynomialCurve2D polyCurve = polynomialCurve(isPolynomial());
        DoublePolynomial crossPoly = polyCurve.crossProductD1D2();

        //DoublePolynomial normalizedPoly = crossPoly.normalize();
        //ComplexPolynomial complexPoly = normalizedPoly.toComplexPolynomial();
        ComplexPolynomial complexPoly = crossPoly.toComplexPolynomial();

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
                                (((Double) paramVec.elementAt(klm)).
                                        doubleValue() - stake)) < 0.0)
                            break;
                    }
                    if (mno == uicp_1) {
                        if (Math.abs(crv.curvature())
                                < Math.abs(((CurveCurvature2D) crvVec.
                                elementAt(klm)).curvature())) {
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

        PointOnCurve2D[] inflexion =
                new PointOnCurve2D[paramVec.size()];
        for (ijk = 0; ijk < paramVec.size(); ijk++)
            inflexion[ijk] = new PointOnCurve2D
                    (this, ((Double) paramVec.elementAt(ijk)).doubleValue(), doCheckDebug);

        return inflexion;
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
    public PointOnCurve2D[] projectFrom(Point2D mate) {
        DoublePolynomial pointPoly[] = polynomial(isPolynomial());
        DoublePolynomial offsPoly[] = new DoublePolynomial[2];
        int coef_size = pointPoly.length;

        if (isRational()) {
            offsPoly[0] = pointPoly[2].scalarMultiply(mate.x());
            offsPoly[1] = pointPoly[2].scalarMultiply(mate.y());
        } else {
            double coef[][] = {{mate.x()}, {mate.y()}};
            offsPoly[0] = new DoublePolynomial(coef[0]);
            offsPoly[1] = new DoublePolynomial(coef[1]);
        }

        for (int i = 0; i < 2; i++)
            pointPoly[i] = (DoublePolynomial) pointPoly[i].subtract(offsPoly[i]);

        DoublePolynomial tangPoly[] = new DoublePolynomial[coef_size];
        DoublePolynomial dotePoly[] = new DoublePolynomial[2];

        // polynomial of tangent vector
        for (int klm = 0; klm < coef_size; klm++)
            tangPoly[klm] = (DoublePolynomial) pointPoly[klm].differentiate();

        if (!isRational()) {
            for (int klm = 0; klm < 2; klm++)
                dotePoly[klm] = (DoublePolynomial) pointPoly[klm].multiply(tangPoly[klm]);
        } else {
            DoublePolynomial work0, work1, work2, sub;
            double[] work3;

            for (int klm = 0; klm < 2; klm++) {
                work0 = (DoublePolynomial) pointPoly[2].multiply(tangPoly[klm]);
                work1 = (DoublePolynomial) tangPoly[2].multiply(pointPoly[klm]);
                // ((a * t^n) * (nb * t^(n-1))) - ((na * t^(n-1)) * (b * t^n)) == 0
                work2 = (DoublePolynomial) work0.subtract(work1);
                work3 = GeometryPrivateUtils.coefficientsBetween(work2, 0, (work2.degree() - 1));
                sub = new DoublePolynomial(work3);

                dotePoly[klm] = (DoublePolynomial) pointPoly[klm].multiply(sub);
            }
        }

        ComplexPolynomial dtPoly;

        try {
            dtPoly = ((DoublePolynomial) dotePoly[0].add(dotePoly[1])).toComplexPolynomial();
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
            PointOnCurve2D proj;

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
        return projList.toPointOnCurve2DArray();
    }

    /**
     * ���̗L��?�S�̂쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @return ���̋�?�S�̂�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve2D toBsplineCurve() {
        double[] www =
                (this.isRational()) ? this.weights : this.makeUniformWeights();

        return new BsplineCurve2D(BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
                this.controlPoints, www);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
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
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate) {
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
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        Axis2Placement2D placement =
                new Axis2Placement2D(mate.pnt(), mate.dir());
        CartesianTransformationOperator2D transform =
                new CartesianTransformationOperator2D(placement, 1.0);
        int uicp = nControlPoints();
        Point2D[] newCp = new Point2D[uicp];

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
        PureBezierCurve2D bzc = new PureBezierCurve2D(newCp, weights, doCheckDebug);

        DoublePolynomial[] realPoly = bzc.polynomial(isPolynomial());
        ComplexPolynomial compPoly = realPoly[1].toComplexPolynomial();

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

            Point2D workPoint = bzc.coordinates(realRoot);
            double dTol = bzc.getToleranceForDistance();

            int paramNum = bzcParam.size();
            if (Math.abs(workPoint.y()) < dTol) {
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
        IntersectionPoint2D[] intersectPoint = new
                IntersectionPoint2D[num];
        double mateLength = mate.dir().length();
        for (int i = 0; i < num; i++) {
            double work = ((Double) lineParam.elementAt(i)).doubleValue() / mateLength;
            ;
            PointOnCurve2D pointOnLine = new PointOnCurve2D(mate, work, doCheckDebug);

            work = ((Double) bzcParam.elementAt(i)).doubleValue();
            PointOnCurve2D pointOnBzc = new PointOnCurve2D(this, work, doCheckDebug);

            Point2D coordinates = (Point2D) bzcPoints.elementAt(i);

            if (!doExchange)
                intersectPoint[i] = new IntersectionPoint2D
                        (coordinates, pointOnBzc, pointOnLine, doCheckDebug);
            else
                intersectPoint[i] = new IntersectionPoint2D
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
     * {@link Circle2D#intersect(PureBezierCurve2D,boolean)
     * Circle2D.intersect(PureBezierCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
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
     * {@link Ellipse2D#intersect(PureBezierCurve2D,boolean)
     * Ellipse2D.intersect(PureBezierCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
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
     * {@link Parabola2D#intersect(PureBezierCurve2D,boolean)
     * Parabola2D.intersect(PureBezierCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
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
     * {@link Hyperbola2D#intersect(PureBezierCurve2D,boolean)
     * Hyperbola2D.intersect(PureBezierCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
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
     * {@link Polyline2D#intersect(PureBezierCurve2D,boolean)
     * Polyline2D.intersect(PureBezierCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
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
     * {@link IntsBzcBzc2D#intersection(PureBezierCurve2D,PureBezierCurve2D,boolean)
     * IntsBzcBzc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return IntsBzcBzc2D.intersection(this, mate, doExchange);
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
     * {@link IntsBzcBsc2D#intersection(PureBezierCurve2D,BsplineCurve2D,boolean)
     * IntsBzcBsc2D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        return IntsBzcBsc2D.intersection(this, mate, doExchange);
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
     * {@link TrimmedCurve2D#intersect(PureBezierCurve2D,boolean)
     * TrimmedCurve2D.intersect(PureBezierCurve2D, boolean)}
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
     * ���̃x�W�G��?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment2D#intersect(PureBezierCurve2D,boolean)
     * CompositeCurveSegment2D.intersect(PureBezierCurve2D, boolean)}
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
     * ���̃x�W�G��?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�N���X��?u��?���?� vs. �x�W�G��?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve2D#intersect(PureBezierCurve2D,boolean)
     * CompositeCurve2D.intersect(PureBezierCurve2D, boolean)}
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
     * ?�̃N���X��?u?� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link BoundedLine2D#interfere(PureBezierCurve2D,boolean)
     * BoundedLine2D.interfere(PureBezierCurve2D, boolean)}
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
     * �|�����C���̃N���X��?u�|�����C�� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link Polyline2D#interfere(PureBezierCurve2D,boolean)
     * Polyline2D.interfere(PureBezierCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�|�����C��)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(Polyline2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�x�W�G��?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBzcBzc2D#interference(PureBezierCurve2D,PureBezierCurve2D,boolean)
     * IntsBzcBzc2D.interference}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�x�W�G��?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(PureBezierCurve2D mate,
                                         boolean doExchange) {
        return IntsBzcBzc2D.interference(this, mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�a�X�v���C����?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBzcBsc2D#interference(PureBezierCurve2D,BsplineCurve2D,boolean)
     * IntsBzcBsc2D.interference}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�a�X�v���C����?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(BsplineCurve2D mate,
                                         boolean doExchange) {
        return IntsBzcBsc2D.interference(this, mate, doExchange);
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
     * �g������?�̃N���X��?u�g������?� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link TrimmedCurve2D#interfere(PureBezierCurve2D,boolean)
     * TrimmedCurve2D.interfere(PureBezierCurve2D, boolean)}
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
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurveSegment2D#interfere(PureBezierCurve2D,boolean)
     * CompositeCurveSegment2D.interfere(PureBezierCurve2D, boolean)}
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
     * ��?���?�N���X��?u��?���?� vs. �x�W�G��?�?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurve2D#interfere(PureBezierCurve2D,boolean)
     * CompositeCurve2D.interfere(PureBezierCurve2D, boolean)}
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
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ����� Bspline ��?��?�߂�?B
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
        Ofst2D doObj = new Ofst2D(this, pint, magni, side, tol);
        return doObj.offset();
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
    public PureBezierCurve2D[] divide(double param) {
        double[][] cntlPnts;
        double[][][] bzcs_array;
        PureBezierCurve2D[] bzcs;
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        try {
            bzcs_array = PureBezierCurveEvaluation.divide(cntlPnts, param);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
        bzcs = new PureBezierCurve2D[2];
        for (int i = 0; i < 2; i++) {
            try {
                bzcs[i] = new PureBezierCurve2D(bzcs_array[i]);
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
    public PureBezierCurve2D truncate(ParameterSection section) {
        double start_par, end_par;
        PureBezierCurve2D t_bzc;

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
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve2D ��
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
    public Polyline2D toPolyline(ParameterSection section,
                                 ToleranceForDistance tolerance) {
        PureBezierCurve2D root_bzc;
        double sp, ep;
        IntervalInfo root_info;
        BinaryTree pnt_tree;
        int no_pnts;
        Point2D[] pnts;
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

        pnts = new Point2D[no_pnts];
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
                        pnts[i++] = new PointOnCurve2D
                                (bi.bzc().controlPointAt(0), this, bi.sp());
                        if (i == (no_pnts - 1)) {
                            pnts[i] = new PointOnCurve2D
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
            return new Polyline2D(pnts);
        } else {
            return new Polyline2D(pnts).reverse();
        }
    }

    /**
     * �x�W�G��?�̂����Ԃ��Ք�N���X?B
     */
    private class IntervalInfo {
        /**
         * ���̋�ԂɑΉ����� (?ו������ꂽ) �x�W�G��?�?B
         */
        private PureBezierCurve2D bzc;

        /**
         * ���̋�Ԃ̊J�n�_�ɑΉ����錳��?�̃p���??[�^�l?B
         */
        private double sp;

        /**
         * ���̋�Ԃ�?I���_�ɑΉ����錳��?�̃p���??[�^�l?B
         */
        private double ep;

        /**
         * �e�t�B?[���h�̒l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param bzc ��ԂɑΉ����� (?ו������ꂽ) �x�W�G��?�
         * @param sp  ��Ԃ̊J�n�_�ɑΉ����錳��?�̃p���??[�^�l
         * @param ep  ��Ԃ�?I���_�ɑΉ����錳��?�̃p���??[�^�l
         */
        private IntervalInfo(PureBezierCurve2D bzc, double sp, double ep) {
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
        private PureBezierCurve2D bzc() {
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
        private ParametricCurve2D basisCurve;

        /**
         * ���̋�?�̂����Ԃ�?�ߎ�����_��?B
         */
        private Point2D[] pnts;

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
        private FillInfo(ParametricCurve2D basisCurve,
                         Point2D[] pnts,
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
     * @see #checkInterval(PureBezierCurve2D.IntervalInfo,double)
     */
    private int divideInterval(int no_pnts,
                               BinaryTree.Node crnt_node,
                               double tol_2) {
        IntervalInfo crnt_info;
        double mid_param;

        BinaryTree.Node left_node;
        IntervalInfo left_info;
        PureBezierCurve2D left_bzc;

        BinaryTree.Node right_node;
        IntervalInfo right_info;
        PureBezierCurve2D right_bzc;

        final double half_point = 0.5;
        PureBezierCurve2D[] div_bzcs;

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
        PureBezierCurve2D bzc = bi.bzc();
        int uicp_1 = bzc.nControlPoints() - 1;

        Vector2D edirs;
        Point2D ppnt;
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
    private Point2D projectPointLine(Point2D dApnt,
                                     Point2D dB_pnt,
                                     Vector2D dB_dir) {
        double magni_dir;    /* magnitude of dB_dir */
        Vector2D euvec;    /* unitized vector of line */
        Vector2D evpp;    /* vector from dB_pnt to dApnt */
        double edot;        /* dot product */
        double m_eps = MachineEpsilon.DOUBLE;

        if ((magni_dir = dB_dir.magnitude()) < m_eps)
            euvec = Vector2D.zeroVector;
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
         * pdata �� {@link PureBezierCurve2D.FillInfo PureBezierCurve2D.FillInfo} �N���X��
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

                    fill_info.pnts[idx++] = new PointOnCurve2D
                            (bi.bzc().controlPointAt(0), fill_info.basisCurve, bi.sp());

                    if (idx == (fill_info.pnts.length - 1))
                        fill_info.pnts[idx++] = new PointOnCurve2D
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
    public Point2D startPoint() {
        return controlPointAt(0);
    }

    /**
     * ���̗L��?��?I���_��Ԃ�?B
     *
     * @return ?I���_
     */
    public Point2D endPoint() {
        int index = nControlPoints() - 1;
        return controlPointAt(index);
    }

    /**
     * ���̃x�W�G��?�𔽓]�����x�W�G��?��Ԃ�?B
     *
     * @return ���]�����x�W�G��?�
     */
    PureBezierCurve2D reverse() {
        boolean isRat = isRational();
        int uicp = nControlPoints();
        Point2D[] rCp = new Point2D[uicp];
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
                return new PureBezierCurve2D(rCp, rWt);
            else
                return new PureBezierCurve2D(rCp);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
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
    public PureBezierCurve2D elevateOneDegree() {
        boolean isPoly = this.isPolynomial();
        int nCP = this.nControlPoints();

        double[][] newCP =
                FreeformCurveWithControlPoints2D.allocateDoubleArray(isPoly,
                        (nCP + 1));
        this.setCoordinatesToDoubleArray(isPoly, nCP, newCP);
        PureBezierCurveEvaluation.elevateOneDegree(nCP, newCP);

        return new PureBezierCurve2D(newCP);
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#PURE_BEZIER_CURVE_2D ParametricCurve2D.PURE_BEZIER_CURVE_2D}
     */
    int type() {
        return PURE_BEZIER_CURVE_2D;
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
        Point2D[] tControlPoints =
                Point2D.transform(this.controlPoints,
                        reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        if (this.isPolynomial() == true)
            return new PureBezierCurve2D(tControlPoints);
        else
            return new PureBezierCurve2D(tControlPoints, this.weights);

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
