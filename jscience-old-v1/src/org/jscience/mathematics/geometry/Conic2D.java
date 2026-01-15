/*
 * �Q���� : �~??��?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Conic2D.java,v 1.8 2006/05/20 23:25:39 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.util.Vector;

/**
 * �Q���� : �~??��?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �~??��?�̈ʒu�ƌX���숒肷���?�?W�n
 * (�z�u?��?A{@link Axis2Placement2D Axis2Placement2D})
 * position ��ێ?����?B
 * </p>
 * <p/>
 * position �� null �ł��BĂ͂Ȃ�Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.8 $, $Date: 2006/05/20 23:25:39 $
 */

public abstract class Conic2D extends ParametricCurve2D {
    /**
     * �~??��?��?u��?S?v�Ƌ�?����̕��숒肷���?�?W�n?B
     *
     * @serial
     */
    private Axis2Placement2D position;

    /**
     * ��?�?W�n��w�肵�Ȃ��I�u�W�F�N�g��?��Ȃ�?B
     */
    private Conic2D() {
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
     * @param position ��?S�Ƌ�?����̕����߂��?�?W�n
     * @see InvalidArgumentValueException
     */
    protected Conic2D(Axis2Placement2D position) {
        super();
        if (position == null)
            throw new InvalidArgumentValueException("position is null.");
        this.position = position;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_�� PointOnCurve2D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * �Ȃ�?A���ʂƂ��ē�����|�����C�����_��?k�ނ���悤��?�?��ɂ�
     * ZeroLengthException �̗�O��?�����?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     * @see PointOnCurve2D
     * @see ZeroLengthException
     */
    public Polyline2D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        if (pint.increase() < 0.0) {
            return toPolyline(pint.reverse(), tol).reverse();
        }

        double sp;
        double ep;
        ParameterDomain domain = parameterDomain();

        if (domain.isPeriodic()) {
            // Ellipse
            sp = domain.wrap(pint.start());
            ep = sp + pint.increase();
        } else {
            sp = pint.lower();
            checkValidity(sp);

            ep = pint.upper();
            checkValidity(ep);
        }

        double tolerance = Math.abs(tol.value());

        IntervalInfo root_info = new IntervalInfo();
        root_info.left = new PointOnCurve2D(this, sp, doCheckDebug);
        root_info.right = new PointOnCurve2D(this, ep, doCheckDebug);

        BinaryTree pnt_tree = new BinaryTree(root_info);

        int no_pnts = 2;    // left & right
        no_pnts += divideInterval(pnt_tree.rootNode(), tolerance);

        Point2D[] pnts = new Point2D[no_pnts];
        FillInfo fill_info = new FillInfo(pnts, 0);

        pnt_tree.rootNode().preOrderTraverse(new FillArray(), fill_info);

        if (no_pnts == 2 && pnts[0].identical(pnts[1]))
            throw new ZeroLengthException();

        return new Polyline2D(pnts);
    }

    /**
     * �~??��?�̂����Ԃ��Ք�N���X?B
     * <p/>
     * ���̓Ք�N���X��
     * {@link #toPolyline(ParameterSection,ToleranceForDistance)
     * toPolyline(ParameterSection, ToleranceForDistance)}
     * �̓Ք�ł̂ݗ��p�����?B
     * </p>
     */
    private class IntervalInfo {
        /**
         * ��Ԃ�?��[ (����)?B
         */
        PointOnCurve2D left;

        /**
         * ��Ԃ̉E�[ (?��)?B
         */
        PointOnCurve2D right;
    }

    /**
     * ���̉~??��?��?A�^����ꂽ��Ԃ�?�ߎ�����_���?�������?B
     * <p/>
     * ��?��������_���?��� crnt_node �ȉ��̓񕪖ؓ�Ɏ�߂���?B
     * </p>
     * <p/>
     * ���̃?�\�b�h��
     * {@link #toPolyline(ParameterSection,ToleranceForDistance)
     * toPolyline(ParameterSection, ToleranceForDistance)}
     * �̓Ք�ł̂ݗ��p�����?B
     * </p>
     *
     * @param crnt_node ���������Ԃ�ێ?����񕪖؂̃m?[�h
     * @param tol       ��?�ߎ���?��x�Ƃ��ė^����ꂽ?u�����̋��e��?�?v
     * @return ��?��?�ߎ�����_���̓_��?�
     * @see IntervalInfo
     * @see #checkInterval(Conic2D.IntervalInfo,double)
     */
    private int divideInterval(BinaryTree.Node crnt_node,
                               double tol) {
        int no_pnts = 1;    // mid

        IntervalInfo crnt_info = (IntervalInfo) crnt_node.data();
        double mid_param = (crnt_info.left.parameter() +
                crnt_info.right.parameter()) / 2.0;

        // divide current interval into two

        IntervalInfo left_info = new IntervalInfo();
        left_info.left = crnt_info.left;
        try {
            left_info.right = new PointOnCurve2D(this, mid_param, doCheckDebug);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        BinaryTree.Node left_node = crnt_node.makeLeft(left_info);

        IntervalInfo right_info = new IntervalInfo();
        right_info.left = left_info.right;
        right_info.right = crnt_info.right;

        BinaryTree.Node right_node = crnt_node.makeRight(right_info);

        // check

        if (!checkInterval(left_info, tol))
            no_pnts += divideInterval(left_node, tol);

        if (!checkInterval(right_info, tol))
            no_pnts += divideInterval(right_node, tol);

        return no_pnts;
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ�����?A
     * ��Ԃ̗��[�싂Ԍ�����?łף�ꂽ�_�̃p���??[�^�l��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ���̃?�\�b�h��
     * {@link #checkInterval(Conic2D.IntervalInfo,double)
     * checkInterval(Conic2D.IntervalInfo, double)}
     * ��ŌĂ�?o�����?B
     * </p>
     *
     * @param left  ?��[ (��ԉ���) �̃p���??[�^�l
     * @param right �E�[ (���?��) �̃p���??[�^�l
     * @return ?łף�ꂽ�_�̃p���??[�^�l
     * @see #checkInterval(Conic2D.IntervalInfo,double)
     */
    abstract double getPeak(double left, double right);

    /**
     * ���̋�?��?A�^����ꂽ��Ԃ�?u?���?v���w���?��x���?��������ۂ���Ԃ�?B
     * <p/>
     * ��Ԃ�?u?���?v��
     * {@link #getPeak(double,double) getPeak(double, double)}
     * �œ���?B
     * </p>
     * <p/>
     * ���̃?�\�b�h��
     * {@link #toPolyline(ParameterSection,ToleranceForDistance)
     * toPolyline(ParameterSection, ToleranceForDistance)}
     * �̓Ք�ł̂ݗ��p�����?B
     * ?��m�ɂ�?A
     * toPolyline(ParameterSection, ToleranceForDistance) ��
     * ���ŌĂ�?o�����
     * {@link #divideInterval(BinaryTree.Node,double)
     * divideInterval(BinaryTree.Node, double)}
     * �̒��ł̂݌Ă�?o�����?B
     * </p>
     *
     * @param info ��?�?�̋��
     * @param tol  ��?�ߎ���?��x�Ƃ��ė^����ꂽ?u�����̋��e��?�?v
     * @return ��Ԃ�?u?���?v���w���?��x���?�������� true?A�����łȂ���� false
     * @see #getPeak(double,double)
     */
    private boolean checkInterval(IntervalInfo info, double tol) {
        double peak_param = getPeak(info.left.parameter(),
                info.right.parameter());

        // ���̃R?[�h�Œu��������?����� : hideit 2000/03/17
        /***
         PointOnCurve2D peak = null;
         try {
         peak = new PointOnCurve2D(this, peak_param, doCheckDebug);
         }
         catch (InvalidArgumentValueException e) {
         throw new FatalException();
         }

         Line2D line = new Line2D(info.left, info.right);
         Point2D proj;
         try {
         proj = line.project1From(peak);
         }
         catch (InvalidArgumentValueException e) {
         throw new FatalException();
         }

         return (peak.subtract(proj).norm() < (tol * tol));
         ***/

        // ?�̃R?[�h��?�����
        Point2D peak = this.coordinates(peak_param);
        Vector2D unitChord = info.right.subtract(info.left).unitized();
        Vector2D left2peak = peak.subtract(info.left);
        double height = Math.abs(left2peak.zOfCrossProduct(unitChord));

        return (height < tol);
    }

    /**
     * �q�m?[�h��?���Ȃ��m?[�h��?u�f?[�^?v��
     * �^����ꂽ�z��ɑ��� BinaryTree.TraverseProc?B
     */
    private class FillArray implements BinaryTree.TraverseProc {
        /**
         * �q�m?[�h��?���Ȃ��m?[�h��?u�f?[�^?v��^����ꂽ�z��ɑ���?B
         * <p/>
         * pdata �� {@link Conic2D.FillInfo Conic2D.FillInfo} �N���X��
         * �C���X�^���X�łȂ���΂Ȃ�Ȃ�?B
         * </p>
         * <p/>
         * ���̓Ք�N���X��
         * {@link #toPolyline(ParameterSection,ToleranceForDistance)
         * toPolyline(ParameterSection, ToleranceForDistance)}
         * �̓Ք�ł̂ݗ��p�����?B
         * </p>
         *
         * @param node  ?��?��?ۂƂȂ�m?[�h
         * @param ctl   ?��?��J�n�����m?[�h���� node �܂ł�?[�� (�Q?Ƃ��Ȃ�)
         * @param pdata �m?[�h��?u�f?[�^?v�����z��
         * @see #toPolyline(ParameterSection,ToleranceForDistance)
         */
        public boolean doit(BinaryTree.Node node, int ctl, Object pdata) {
            if ((node.left() == null) && (node.right() == null)) {

                FillInfo fill_info = (FillInfo) pdata;
                int idx = fill_info.index;
                IntervalInfo info = (IntervalInfo) node.data();

                if (idx == 0)
                    fill_info.pnts[idx++] = info.left;

                fill_info.pnts[idx++] = info.right;
                fill_info.index = idx;
            }
            return false;
        }
    }

    /**
     * ���̋�?�̂����Ԃ�?�ߎ�����_����߂邽�߂̔z���?�N���X?B
     * <p/>
     * ���̓Ք�N���X��
     * {@link #toPolyline(ParameterSection,ToleranceForDistance)
     * toPolyline(ParameterSection, ToleranceForDistance)}
     * �̓Ք�ł̂ݗ��p�����?B
     * </p>
     */
    private class FillInfo {
        /**
         * ���̋�?�̂����Ԃ�?�ߎ�����_��?B
         */
        private Point2D[] pnts;

        /**
         * ���̑��� pnts[index] �ɑ΂���?s�Ȃ���ׂ��ł��邱�Ƃ��l?B
         */
        private int index;

        /**
         * �_���ێ?���ׂ��z���?A��̗v�f��?擪����?���^����
         * �I�u�W�F�N�g��?\�z����?B
         */
        private FillInfo(Point2D[] pnts, int index) {
            super();
            this.pnts = pnts;
            this.index = index;
        }
    }

    /**
     * ���̉~??��?��?u��?S?v�Ƌ�?����̕��숒肵�Ă����?�?W�n��Ԃ�?B
     *
     * @return ��?S�Ƌ�?����̕�����?�?W�n
     */
    public Axis2Placement2D position() {
        return position;
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * �~??��?�ɂ͓Hٓ_�͑�?݂��Ȃ��̂�?A?�ɒ��� 0 �̔z���Ԃ�?B
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
     * �~??��?�ɂ͕ϋȓ_�͑�?݂��Ȃ��̂�?A?�ɒ��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     */
    public PointOnCurve2D[] inflexion() {
        return new PointOnCurve2D[0];
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂� (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li>	�x�W�G��?��?A���̉~??��?�̋�?�?W�n��ł̕\���ɕϊ�����?B
     * <li>	���̋�?�ƕϊ������x�W�G��?�̌�_��\����?�����?�?�����?B
     * <li>	��?�����?���?�߂�?B
     * <li>	?��̊e?X�ɂ���?A��Ԃł̉�Ƃ��đÓ��ł��邩�ۂ���?؂���?B
     * <li>	��Ԃł̉�Ƃ��đÓ���?�����?u��_?v��?�?�����?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        Axis2Placement2D placement = position();
        CartesianTransformationOperator2D transform =
                new CartesianTransformationOperator2D(placement, 1.0);
        int uicp = mate.nControlPoints();
        Point2D[] newCp = new Point2D[uicp];

        // Transform Bezier's control points into conic's local coordinates
        for (int i = 0; i < uicp; i++)
            newCp[i] = transform.toLocal(mate.controlPointAt(i));

        // make Bezier curve from new control points
        PureBezierCurve2D bzc = new
                PureBezierCurve2D(newCp, mate.weights(), false);

        // For each segment
        Vector pointVec = new Vector();
        Vector paramVec = new Vector();
        boolean isPoly = bzc.isPolynomial();
        // make polynomial
        DoublePolynomial[] poly = bzc.polynomial(isPoly);
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

        int nRoots = roots.length;
        for (int j = 0; j < nRoots; j++) {
            double realRoot = roots[j].real();
            if (bzc.parameterValidity(realRoot) == ParameterValidity.OUTSIDE)
                continue;

            if (realRoot < 0.0) realRoot = 0.0;
            if (realRoot > 1.0) realRoot = 1.0;

            Point2D workPoint = bzc.coordinates(realRoot);
            // check solution
            if (!checkSolution(workPoint))
                continue;
            for (int jj = 0; jj < pointVec.size(); jj++) {
                Point2D pt = (Point2D) pointVec.elementAt(jj);
                double param = ((Double) paramVec.elementAt(jj)).doubleValue();
                if (pt.identical(workPoint)
                        && bzc.identicalParameter(param, realRoot))
                    break;
            }
            // add solution
            pointVec.addElement(workPoint);
            paramVec.addElement(new Double(realRoot));
        }

        // make intersection point
        int num = paramVec.size();
        IntersectionPoint2D[] intersectPoint = new IntersectionPoint2D[num];
        for (int i = 0; i < num; i++) {
            // get Parameter from solution point
            Point2D point = (Point2D) pointVec.elementAt(i);
            double param = getParameter(point);

            // make intersection point on Conic
            PointOnCurve2D pointOnConic = new
                    PointOnCurve2D(this, param, doCheckDebug);

            // make intersection point on Bzc
            double work = ((Double) paramVec.elementAt(i)).doubleValue();
            PointOnCurve2D pointOnBzc = new
                    PointOnCurve2D(mate, work, doCheckDebug);

            if (!doExchange)
                intersectPoint[i] = new
                        IntersectionPoint2D(pointOnConic, pointOnBzc, doCheckDebug);
            else
                intersectPoint[i] = new
                        IntersectionPoint2D(pointOnBzc, pointOnConic, doCheckDebug);
        }

        return intersectPoint;
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂� (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li>	�a�X�v���C����?��?A���̉~??��?�̋�?�?W�n��ł̕\���ɕϊ�����?B
     * <li>	�ϊ������a�X�v���C����?�̊e�Z�O�?���g�ɂ���
     * <ul>
     * <li>	���̋�?�ƕϊ������a�X�v���C����?�̃Z�O�?���g�̌�_��\����?�����?�?�����?B
     * <li>	��?�����?���?�߂�?B
     * <li>	?��̊e?X�ɂ���?A��Ԃł̉�Ƃ��đÓ��ł��邩�ۂ���?؂���?B
     * </ul>
     * <li>	��Ԃł̉�Ƃ��đÓ���?�����?u��_?v��?�?�����?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        Axis2Placement2D placement = position();
        CartesianTransformationOperator2D transform =
                new CartesianTransformationOperator2D(placement, 1.0);
        BsplineKnot.ValidSegmentInfo vsegInfo = mate.validSegments();
        Point2D[] cp = mate.controlPoints();
        int uicp = mate.nControlPoints();
        Point2D[] newCp = new Point2D[uicp];

        // Transform Bspline's control points into conic's local coordinates
        for (int i = 0; i < uicp; i++)
            newCp[i] = transform.toLocal(cp[i]);

        // make Bspline curve from new control points
        BsplineCurve2D bsc = new
                BsplineCurve2D(mate.knotData(), newCp, mate.weights());

        // For each segment
        Vector pointVec = new Vector();
        Vector paramVec = new Vector();
        int nSeg = vsegInfo.nSegments();
        int k = 0;
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

            int nRoots = roots.length;
            for (int j = 0; j < nRoots; j++) {
                double realRoot = roots[j].real();
                if (bsc.parameterValidity(realRoot) == ParameterValidity.OUTSIDE)
                    continue;

                double[] knotParams = vsegInfo.knotPoint(i);
                if (realRoot < knotParams[0]) realRoot = knotParams[0];
                if (realRoot > knotParams[1]) realRoot = knotParams[1];

                Point2D workPoint = bsc.coordinates(realRoot);
                // check solution
                if (!checkSolution(workPoint))
                    continue;
                int jj;
                for (jj = 0; jj < k; jj++) {
                    double dTol = bsc.getToleranceForDistance();
                    Point2D pt = (Point2D) pointVec.elementAt(jj);
                    double param = ((Double) paramVec.elementAt(jj)).doubleValue();
                    if (pt.identical(workPoint)
                            && bsc.identicalParameter(param, realRoot))
                        break;
                }
                // add solution
                if (jj >= k) {
                    pointVec.addElement(workPoint);
                    paramVec.addElement(new Double(realRoot));
                    k++;
                }
            }
        }

        // make intersection point
        int num = paramVec.size();
        IntersectionPoint2D[] intersectPoint = new IntersectionPoint2D[num];
        for (int i = 0; i < k; i++) {
            // get Parameter from solution point
            Point2D point = (Point2D) pointVec.elementAt(i);
            double param = getParameter(point);

            // make intersection point on Conic
            PointOnCurve2D pointOnConic = new
                    PointOnCurve2D(this, param, doCheckDebug);

            // make intersection point on Bsc
            double work = ((Double) paramVec.elementAt(i)).doubleValue();
            PointOnCurve2D pointOnBsc = new
                    PointOnCurve2D(mate, work, doCheckDebug);

            if (!doExchange)
                intersectPoint[i] = new IntersectionPoint2D
                        (pointOnConic, pointOnBsc, doCheckDebug);
            else
                intersectPoint[i] = new IntersectionPoint2D
                        (pointOnBsc, pointOnConic, doCheckDebug);
        }

        return intersectPoint;
    }

    /**
     * ���̉~??��?�� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?����钊?ۃ?�\�b�h?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ���̉~??��?�� poly �̌�_��\����?�����?���
     * @see #intersect(PureBezierCurve2D,boolean)
     * @see #intersect(BsplineCurve2D,boolean)
     */
    abstract DoublePolynomial makePoly(DoublePolynomial[] poly);

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ��邩�ۂ���`�F�b�N���钊?ۃ?�\�b�h?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �^����ꂽ�_�����̋�?�?�ɂ���� true?A�����łȂ���� false
     * @see #intersect(PureBezierCurve2D,boolean)
     * @see #intersect(BsplineCurve2D,boolean)
     */
    abstract boolean checkSolution(Point2D point);

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ����̂Ƃ���?A
     * ���̓_�̋�?�?�ł̃p���??[�^�l��?�߂钊?ۃ?�\�b�h?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �p���??[�^�l
     * @see #intersect(PureBezierCurve2D,boolean)
     * @see #intersect(BsplineCurve2D,boolean)
     */
    abstract double getParameter(Point2D point);

    /**
     * ���̋�?�̎w��̋�Ԃ�?Č����� (�L�?) �x�W�G��?��?���_���Ԃ�?B
     * <p/>
     * ?d�݂ɂ��Ă�?A���̃?�\�b�h�ł͊֒m���Ȃ�?B
     * </p>
     * <p/>
     * ������?���_��̗v�f?���?�� 3 �ł���?B
     * </p>
     * <p/>
     * pint �̑?���l�͕��ł�?\��Ȃ�?B
     * pint �̑?���l������?�?��ɂ�?A
     * ������?���_�񂪕\���x�W�G��?��?i?s����?A���̋�?��?i?s���̋t�ɂȂ�?B
     * </p>
     * <p/>
     * �Ȃ�?A�^����ꂽ��Ԃ��{�̃x�W�G��?��?Č��ł��Ȃ�?�?�?A
     * �����錋�ʂ�?������Ȃ����Ƃɒ?��?B
     * </p>
     *
     * @param pint �x�W�G��?��?Č�����p���??[�^���
     * @return ��Ԃ�?Č�����x�W�G��?��?���_��
     */
    protected Point2D[] getControlPointsOfBezierCurve(ParameterSection pint) {
        CurveDerivative2D derivative;

        derivative = this.evaluation(pint.lower());
        Line2D lowerTangentLine = new Line2D(derivative.d0D(), derivative.d1D());

        derivative = this.evaluation(pint.upper());
        Line2D upperTangentLine = new Line2D(derivative.d0D(), derivative.d1D());

        Point2D[] controlPoints = new Point2D[3];

        if (pint.increase() > 0.0) {
            controlPoints[0] = lowerTangentLine.pnt();
            controlPoints[2] = upperTangentLine.pnt();
        } else {
            controlPoints[2] = lowerTangentLine.pnt();
            controlPoints[0] = upperTangentLine.pnt();
        }

        double atol = this.getToleranceForAngle();
        boolean push = false;
        double angle = lowerTangentLine.dir().angleWith(upperTangentLine.dir());
        double angleFromParallel;

        if ((angleFromParallel = angle) < atol)
            push = true;
        else if ((angleFromParallel = Math.abs(Math.PI - angle)) < atol)
            push = true;
        else if ((angleFromParallel = Math.abs(GeometryUtils.PI2 - angle)) < atol)
            push = true;

        if (push == true)
            ConditionOfOperation.getCondition().
                    makeCopyWithToleranceForAngle(angleFromParallel / 2.0).push();

        try {
            controlPoints[1] = lowerTangentLine.intersect1Line(upperTangentLine).literal();
        } catch (IndefiniteSolutionException e) {
            throw new FatalException("Two tangent lines does not intersect");
        } finally {
            if (push == true)
                ConditionOfOperation.pop();
        }

        return controlPoints;
    }

    /**
     * ����~??��?�̈ꕔ��?Č�����L�?�x�W�G��?����{�̗L�?�a�X�v���C����?�ɕϊ�����?B
     * <p/>
     * �^������L�?�x�W�G��?���?A����~??��?�̈ꕔ��?Č�������̂�?A
     * ��?��̗v�f?��� 1 �Ȃ��� 3 �ł���?A
     * �e�v�f�͂��ׂĂQ����?�ł����̂Ƒz�肵�Ă���?B
     * </p>
     * <p/>
     * ����?��̂���?A���̃?�\�b�h��?A
     * �x�W�G��?�邢�͂a�X�v���C����?�̃N���X�ɒu���ׂ��ł͂Ȃ���?l����?B
     * </p>
     *
     * @param bezierCurves (����~??��?�̈ꕔ��?Č�����) �L�?�x�W�G��?��
     * @param closed       �L�?�x�W�G��?�񂪕��Ă���� true?A�����łȂ���� false
     * @return �L�?�a�X�v���C����?�
     */
    protected static BsplineCurve2D
    convertPolyBezierCurvesToOneBsplineCurve(PureBezierCurve2D[] bezierCurves,
                                             boolean closed) {
        int nBeziers = bezierCurves.length;
        int uicp;
        int uik;

        if (closed != true) {
            // open
            uicp = (nBeziers != 3) ? (nBeziers + 2) : (nBeziers + 3);
            uik = nBeziers + 1;
        } else {
            // closed : nBeziers should be always 3
            uicp = 5;   // nBeziers + 2
            uik = 6;    // nBeziers + 3
        }

        int degree = 2;
        boolean periodic = closed;
        int[] knotMultiplicities = new int[uik];
        double[] knots = new double[uik];
        Point2D[] controlPoints = new Point2D[uicp];
        double[] weights = new double[uicp];

        switch (nBeziers) {
            case 1:
                for (int i = 0; i < 3; i++) {
                    controlPoints[i] = bezierCurves[0].controlPointAt(i);
                    weights[i] = bezierCurves[0].weightAt(i);
                }
                knots[0] = 0.0;
                knotMultiplicities[0] = 3;
                knots[1] = 1.0;
                knotMultiplicities[1] = 3;
                break;

            case 2:
                /*
                * weights:
                *              inverse of standard form              bspline
                *	1 a 1 1 a 1 ========================> 1 b b b b 1 =======> 1 b b 1
                *
                *	b = a * a
                */
                controlPoints[0] = bezierCurves[0].controlPointAt(0);
                controlPoints[1] = bezierCurves[0].controlPointAt(1);
                controlPoints[2] = bezierCurves[1].controlPointAt(1);
                controlPoints[3] = bezierCurves[1].controlPointAt(2);
                weights[0] = 1.0;
                weights[1] = weights[2] =
                        bezierCurves[0].weightAt(1) * bezierCurves[0].weightAt(1);
                weights[3] = 1.0;

                knots[0] = 0.0;
                knotMultiplicities[0] = 3;
                knots[1] = 1.0;
                knotMultiplicities[1] = 1;
                knots[2] = 2.0;
                knotMultiplicities[2] = 3;
                break;

            case 3:
                if (closed != true) {
                    // open
                    controlPoints[0] = bezierCurves[0].controlPointAt(0);
                    controlPoints[1] = bezierCurves[0].controlPointAt(1);
                    controlPoints[2] = bezierCurves[1].controlPointAt(1);
                    controlPoints[3] = bezierCurves[1].controlPointAt(2);
                    controlPoints[4] = bezierCurves[2].controlPointAt(1);
                    controlPoints[5] = bezierCurves[2].controlPointAt(2);
                    weights[0] = 1.0;
                    weights[1] = weights[2] =
                            bezierCurves[0].weightAt(1) * bezierCurves[0].weightAt(1);
                    weights[3] = 1.0;
                    weights[4] = bezierCurves[2].weightAt(1);
                    weights[5] = 1.0;

                    knots[0] = 0.0;
                    knotMultiplicities[0] = 3;
                    knots[1] = 1.0;
                    knotMultiplicities[1] = 1;
                    knots[2] = 2.0;
                    knotMultiplicities[2] = 2;
                    knots[3] = 4.0;
                    knotMultiplicities[3] = 3; // knots[3] != 3.0
                } else {
                    // closed
                    controlPoints[0] = bezierCurves[2].controlPointAt(1);
                    controlPoints[1] = bezierCurves[0].controlPointAt(0);
                    controlPoints[2] = bezierCurves[0].controlPointAt(1);
                    controlPoints[3] = bezierCurves[1].controlPointAt(1);
                    controlPoints[4] = bezierCurves[1].controlPointAt(2);
                    weights[0] = bezierCurves[2].weightAt(1);
                    weights[1] = 1.0;
                    weights[2] = weights[3] =
                            bezierCurves[0].weightAt(1) * bezierCurves[0].weightAt(1);
                    weights[4] = 1.0;

                    knots[0] = (-2.0);
                    knotMultiplicities[0] = 2;
                    knots[1] = 0.0;
                    knotMultiplicities[1] = 2;
                    knots[2] = 1.0;
                    knotMultiplicities[2] = 1;
                    knots[3] = 2.0;
                    knotMultiplicities[3] = 2;
                    knots[4] = 4.0;
                    knotMultiplicities[4] = 2; // knots[4] != 3.0
                    knots[5] = 5.0;
                    knotMultiplicities[5] = 1;
                }
                break;
        }

        return new BsplineCurve2D(degree, periodic,
                knotMultiplicities, knots,
                controlPoints, weights);
    }
}

