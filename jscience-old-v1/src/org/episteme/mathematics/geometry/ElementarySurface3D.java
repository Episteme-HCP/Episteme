/*
 * �R���� : ?����Ȗʂ̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ElementarySurface3D.java,v 1.7 2006/05/20 23:25:41 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.algebraic.numbers.Complex;
import org.episteme.mathematics.analysis.polynomials.ComplexPolynomial;
import org.episteme.mathematics.analysis.polynomials.DoublePolynomial;
import org.episteme.util.FatalException;

import java.util.Vector;

/**
 * �R���� : ?����Ȗʂ̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 * <p/>
 * ?����ȖʂƂ�?A����?^����?^�~����?^�~??�ʂȂǂ̂��Ƃ⢂�?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �Ȗʂ̈ʒu�ƌX���숒肷���?�?W�n
 * (�z�u?��?A{@link Axis2Placement3D Axis2Placement3D})
 * position ��ێ?����?B
 * </p>
 * <p/>
 * position �� null �ł��BĂ͂Ȃ�Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.7 $, $Date: 2006/05/20 23:25:41 $
 */

public abstract class ElementarySurface3D extends ParametricSurface3D {
    /**
     * �Ȗʂ�?u��?S?v�Ƌ�?����̕��숒肷���?�?W�n?B
     *
     * @serial
     */
    private final Axis2Placement3D position;

    /**
     * ��?�?W�n��w�肵�Ȃ��I�u�W�F�N�g��?��Ȃ�?B
     */
    private ElementarySurface3D() {
        super();
        this.position = null;
    }

    /**
     * ��?�?W�n��w�肵�ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param position ��?S�Ǝ����
     * @see InvalidArgumentValueException
     */
    protected ElementarySurface3D(Axis2Placement3D position) {
        super();
        if (position == null)
            throw new InvalidArgumentValueException("position is null.");
        this.position = position;
    }

    /**
     * ����?����Ȗʂ�?u��?S?v�Ƌ�?����̕��숒肵�Ă����?�?W�n��Ԃ�?B
     *
     * @return ��?S�Ƌ�?����̕�����?�?W�n
     */
    public Axis2Placement3D position() {
        return position;
    }

    /**
     * ����?����Ȗ�?�̋�?��^����?A���̃p���??[�^��Ԃł̂Q�����\���𓾂�?B
     * <p/>
     * ��?ۂ̉��Z��
     * {@link ToParameterSpaceOfSurface3D#convertCurve(ParametricCurve3D,ElementarySurface3D)
     * ToParameterSpaceOfSurface3D.convertCurve}(curve, this)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param curve �Ȗ�?�̋�?� (�R�����\��)
     * @return �p���??[�^��Ԃł̋�?� (�Q�����\��)
     * @see #curveToIntersectionCurve(ParametricCurve3D,ElementarySurface3D,boolean)
     * @see ToParameterSpaceOfSurface3D#convertCurve(ParametricCurve3D,ElementarySurface3D)
     */
    ParametricCurve2D curveToParameterCurve(ParametricCurve3D curve) {
        return ToParameterSpaceOfSurface3D.convertCurve(curve, this);
    }

    /**
     * ����?����ȖʂƑ���?����Ȗʂ̌�?��\���R������?��^����?A
     * ��?�I�u�W�F�N�g�ɕϊ�����?B
     * <p/>
     * ���̃?�\�b�h�̓Ք�ł�?A
     * ��?�̂R������?��e�Ȗʂ̃p���??[�^��Ԃł̂Q�����\���𓾂邽�߂�
     * {@link #curveToParameterCurve(ParametricCurve3D) curveToParameterCurve(ParametricCurve3D)}
     * �𗘗p���Ă���?B
     * </p>
     * <p/>
     * ?�?������?�I�u�W�F�N�g�� masterRepresentation �̒l��
     * PreferredSurfaceCurveRepresentation.CURVE_3D �Ƃ��Ă���?B
     * </p>
     *
     * @param curve      ��?��\���R������?�
     * @param surface    ���̋Ȗ�
     * @param doExchange ��?�� this �� mate �̊i�[?���귂��邩�ǂ���
     * @return ��?�I�u�W�F�N�g
     * @see #curveToParameterCurve(ParametricCurve3D)
     */
    IntersectionCurve3D curveToIntersectionCurve(ParametricCurve3D curve,
                                                 ElementarySurface3D mate,
                                                 boolean doExchange) {
        ParametricCurve2D pcrv1 = this.curveToParameterCurve(curve);
        ParametricCurve2D pcrv2 = mate.curveToParameterCurve(curve);
        if (!doExchange)
            return new IntersectionCurve3D(curve, this, pcrv1, mate, pcrv2,
                    PreferredSurfaceCurveRepresentation.CURVE_3D);
        else
            return new IntersectionCurve3D(curve, mate, pcrv2, this, pcrv1,
                    PreferredSurfaceCurveRepresentation.CURVE_3D);
    }

    /**
     * �X�P?[�����O�l�� 1 �Ƃ���?A
     * ����?����Ȗʂ̋�?�?W�n������I��?W�n�ւ̕ϊ���?s�Ȃ����Z�q��Ԃ�?B
     *
     * @return ��?�?W�n������I��?W�n�ւ̕ϊ���?s�Ȃ����Z�q
     */
    protected CartesianTransformationOperator3D toGlobal() {
        try {
            return new CartesianTransformationOperator3D(position(), 1.0);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ����?����Ȗʂ� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?����钊?ۃ?�\�b�h?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ����?����Ȗʂ� poly �̌�_��\����?�����?���
     * @see #intersect(PureBezierCurve3D,boolean)
     * @see #intersect(BsplineCurve3D,boolean)
     */
    abstract DoublePolynomial makePoly(DoublePolynomial[] poly);

    /**
     * �^����ꂽ�_�����̋Ȗ�?�ɂ��邩�ۂ���`�F�b�N���钊?ۃ?�\�b�h?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �^����ꂽ�_�����̋Ȗ�?�ɂ���� true?A�����łȂ���� false
     * @see #intersect(PureBezierCurve3D,boolean)
     * @see #intersect(BsplineCurve3D,boolean)
     */
    abstract boolean checkSolution(Point3D point);

    /**
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�~??��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * {@link Plane3D ����} �̃N���X�ł�?A���̃?�\�b�h��I?[�o?[���C�h���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~??��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Conic3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersectQrd(this, !doExchange);
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
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate,
                                    boolean doExchange) {
        /*
        * Translate Bezier's control points into
        * ElementarySurface3D's local coordinates system
        */
        CartesianTransformationOperator3D transform = this.toGlobal();
        int uicp = mate.nControlPoints();
        Point3D[] newCp = new Point3D[uicp];

        // translate control points
        for (int i = 0; i < uicp; i++) {
            newCp[i] = transform.toLocal(mate.controlPointAt(i));
        }

        // translate weights
        double[] weights = mate.weights();
        if (mate.isRational()) {
            double max_weight = 0.0;
            for (int i = 0; i < uicp; i++) {
                if (Math.abs(weights[i]) > max_weight) {
                    max_weight = weights[i];
                }
            }
            if (max_weight > 0.0) {
                for (int i = 0; i < uicp; i++) {
                    weights[i] /= max_weight;
                }
            }
        }

        // make Bezier Curve from new control points
        PureBezierCurve3D bzc = new PureBezierCurve3D(newCp, weights, doCheckDebug);

        /*
        * make polynomial
        */
        DoublePolynomial[] poly = bzc.polynomial(mate.isPolynomial());
        DoublePolynomial realPoly = makePoly(poly);
        ComplexPolynomial compPoly = realPoly.toComplexPolynomial();

        /*
        * get roots
        */
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
        Vector bzcParams = new Vector();
        Vector bzcPoints = new Vector();

        for (int j = 0; j < nRoots; j++) {
            double realRoot = roots[j].real();
            if (bzc.parameterValidity(realRoot) == ParameterValidity.OUTSIDE)
                continue;

            Point3D workPoint = bzc.coordinates(realRoot);

            int paramNum = bzcParams.size();
            if (checkSolution(workPoint)) {
                int k;
                for (k = 0; k < paramNum; k++) {
                    double paramA =
                            ((Double) bzcParams.elementAt(k)).doubleValue();
                    Point3D kthPoint = (Point3D) bzcPoints.elementAt(k);
                    if ((workPoint.identical(kthPoint)) &&
                            (bzc.identicalParameter(realRoot, paramA)))
                        break;
                }
                if (k >= paramNum) {
                    bzcParams.addElement(new Double(realRoot));
                    bzcPoints.addElement(workPoint);
                }
            }
        }

        /*
        * solution
        */
        int num = bzcParams.size();
        IntersectionPoint3D[] intersectPoints = new
                IntersectionPoint3D[num];
        for (int i = 0; i < num; i++) {
            double bzcParam = ((Double) bzcParams.elementAt(i)).doubleValue();
            Point3D coordinates = mate.coordinates(bzcParam);
            double[] myParams = pointToParameter(coordinates);

            if (doExchange) {
                intersectPoints[i] = new
                        IntersectionPoint3D(mate, bzcParam,
                        this, myParams[0], myParams[1],
                        doCheckDebug);
            } else {
                intersectPoints[i] = new
                        IntersectionPoint3D(this, myParams[0], myParams[1],
                        mate, bzcParam,
                        doCheckDebug);
            }
        }

        return intersectPoints;
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
    IntersectionPoint3D[] intersect(BsplineCurve3D mate,
                                    boolean doExchange) {
        CartesianTransformationOperator3D transform = this.toGlobal();
        BsplineKnot.ValidSegmentInfo vsegInfo = mate.validSegments();
        int uicp = mate.nControlPoints();
        Point3D[] newCp = new Point3D[uicp];

        // Transform Bspline's control points into conic's local coordinates
        for (int i = 0; i < uicp; i++)
            newCp[i] = transform.toLocal(mate.controlPointAt(i));

        // make Bspline curve from new control points
        BsplineCurve3D bsc = new
                BsplineCurve3D(mate.knotData(), newCp, mate.weights());

        // For each segment
        Vector pointVec = new Vector();
        Vector paramVec = new Vector();
        int nSeg = vsegInfo.nSegments();

        for (int i = 0; i < nSeg; i++) {
            // make polynomial
            DoublePolynomial[] poly =
                    bsc.polynomial(vsegInfo.segmentNumber(i), bsc.isPolynomial());
            DoublePolynomial realPoly = makePoly(poly);
            ComplexPolynomial compPoly = realPoly.toComplexPolynomial();

            // solve polynomial
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

            int k = 0;
            int nRoots = roots.length;
            for (int j = 0; j < nRoots; j++) {
                double realRoot = roots[j].real();
                if (bsc.parameterValidity(realRoot) == ParameterValidity.OUTSIDE)
                    continue;

                double[] knotParams = vsegInfo.knotPoint(i);
                if (realRoot < knotParams[0]) realRoot = knotParams[0];
                if (realRoot > knotParams[1]) realRoot = knotParams[1];

                Point3D workPoint = bsc.coordinates(realRoot);
                // check solution
                if (!checkSolution(workPoint))
                    continue;

                // check duplicate solution
                int jj;
                for (jj = 0; jj < k; jj++) {
                    double dTol = bsc.getToleranceForDistance();
                    Point3D pnt = (Point3D) pointVec.elementAt(jj);
                    double param = ((Double) paramVec.elementAt(jj)).doubleValue();
                    if (pnt.identical(workPoint)
                            && bsc.identicalParameter(param, realRoot))
                        break;
                }
                if (jj >= k) {
                    pointVec.addElement(workPoint);
                    paramVec.addElement(new Double(realRoot));
                    k++;
                }
            }
        }

        // make intersection point
        int num = paramVec.size();
        IntersectionPoint3D[] intersectPoints = new
                IntersectionPoint3D[num];
        for (int i = 0; i < num; i++) {
            // get intersection point parameter on Bsc
            double bscParam = ((Double) paramVec.elementAt(i)).doubleValue();

            // get Parameter from solution point
            Point3D coordinates = mate.coordinates(bscParam);
            double myParams[] = pointToParameter(coordinates);

            // make an intersection point
            if (doExchange) {
                intersectPoints[i] = new
                        IntersectionPoint3D(mate, bscParam,
                        this, myParams[0], myParams[1],
                        doCheckDebug);
            } else {
                intersectPoints[i] = new
                        IntersectionPoint3D(this, myParams[0], myParams[1],
                        mate, bscParam,
                        doCheckDebug);
            }
        }

        return intersectPoints;
    }
}
