/*
 * �R���� : �~??��?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Conic3D.java,v 1.6 2006/05/20 23:25:40 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.util.Vector;

/**
 * �R���� : �~??��?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �~??��?�̈ʒu�ƌX���숒肷���?�?W�n
 * (�z�u?��?A{@link Axis2Placement3D Axis2Placement3D})
 * position ��ێ?����?B
 * </p>
 * <p/>
 * position �� null �ł��BĂ͂Ȃ�Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.6 $, $Date: 2006/05/20 23:25:40 $
 */

public abstract class Conic3D extends ParametricCurve3D {
    /**
     * �~??��?��?u��?S?v�Ƌ�?����̕��숒肷���?�?W�n?B
     *
     * @serial
     */
    private Axis2Placement3D position;

    /**
     * ��?�?W�n��w�肵�Ȃ��I�u�W�F�N�g��?��Ȃ�?B
     */
    private Conic3D() {
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
    protected Conic3D(Axis2Placement3D position) {
        super();
        if (position == null)
            throw new InvalidArgumentValueException("position is null.");
        this.position = position;
    }

    /**
     * �X�P?[�����O�l�� 1 �Ƃ���?A
     * ���̉~??��?�̋�?�?W�n������I��?W�n�ւ̕ϊ���?s�Ȃ����Z�q��Ԃ�?B
     *
     * @return ��?�?W�n������I��?W�n�ւ̕ϊ���?s�Ȃ����Z�q
     */
    protected CartesianTransformationOperator3D toGlobal() {
        return new CartesianTransformationOperator3D(position(), 1.0);
    }

    /**
     * ���̉~??��?��?A
     * �^����ꂽ�Q�����̋�?�?W�n (�z�u?��) ��?�Q�����`?�ɕϊ�������̂�Ԃ�?B
     *
     * @param position �Q�����̋�?�?W�n (�z�u?��)
     * @return �Q�����`?�
     */
    abstract Conic2D toLocal2D(Axis2Placement2D position);

    /**
     * ���̉~??��?��?A
     * �Q�����̑��I��?W�n��z�u?��Ƃ���Q�����`?�ɕϊ�������̂�Ԃ�?B
     *
     * @return �Q�����`?�
     * @see #toLocal2D(Axis2Placement2D)
     */
    Conic2D toLocal2D() {
        return toLocal2D(Axis2Placement2D.origin);
    }

    /**
     * ���̉~??��?��?A
     * �^����ꂽ��?�?W�n��ɋt�ϊ��������?A
     * ���̋�?�?W�n�� X/Y ����?�̂Q�����`?�ɕϊ�������̂�Ԃ�?B
     * <p/>
     * transform ��?A
     * ���̉~??��?�?�镽�ʂ̖@?�����?� Z ���Ƃ���
     * ��?�?W�n��\����̂łȂ���΂Ȃ�Ȃ�?B
     * </p>
     *
     * @param transform ��?�?W�n��\���􉽓I�ϊ����Z�q
     * @return �Q�����`?�
     * @see #toLocal2D(Axis2Placement2D)
     */
    Conic2D toLocal2D(CartesianTransformationOperator3D transform) {
        Point2D location = transform.toLocal(position().location()).to2D();
        Vector2D x_axis = transform.toLocal(position().x()).to2D();
        Axis2Placement2D position =
                new Axis2Placement2D(location, x_axis);
        return toLocal2D(position);
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ�?B
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     */
    public double length(ParameterSection pint) {
        Conic2D conic2D = toLocal2D();
        return conic2D.length(pint);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_�� PointOnCurve3D ��
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
     * @see PointOnCurve3D
     * @see ZeroLengthException
     */
    public Polyline3D toPolyline(ParameterSection pint,
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
            ep = domain.wrap(pint.end());

            if (sp > ep)
                ep += domain.section().increase();
        } else {
            sp = pint.lower();
            checkValidity(sp);

            ep = pint.upper();
            checkValidity(ep);
        }

        double tolerance = Math.abs(tol.value());

        IntervalInfo root_info = new IntervalInfo();
        try {
            root_info.left = new PointOnCurve3D(this, sp, doCheckDebug);
            root_info.right = new PointOnCurve3D(this, ep, doCheckDebug);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        BinaryTree pnt_tree = new BinaryTree(root_info);

        int no_pnts = 2;    // left & right
        no_pnts += divideInterval(pnt_tree.rootNode(), tolerance * tolerance);

        Point3D[] pnts = new Point3D[no_pnts];
        FillInfo fill_info = new FillInfo(pnts, 0);

        pnt_tree.rootNode().preOrderTraverse(new FillArray(), fill_info);

        if (no_pnts == 2 && pnts[0].identical(pnts[1]))
            throw new ZeroLengthException();

        return new Polyline3D(pnts);
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
        PointOnCurve3D left;

        /**
         * ��Ԃ̉E�[ (?��)?B
         */
        PointOnCurve3D right;
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
     * @see #checkInterval(Conic3D.IntervalInfo,double)
     */
    private int divideInterval(BinaryTree.Node crnt_node,
                               double tol_2) {
        int no_pnts = 1;    // mid

        IntervalInfo crnt_info = (IntervalInfo) crnt_node.data();
        double mid_param = (crnt_info.left.parameter() +
                crnt_info.right.parameter()) / 2.0;

        // divide current interval into two

        IntervalInfo left_info = new IntervalInfo();
        left_info.left = crnt_info.left;
        try {
            left_info.right = new PointOnCurve3D(this, mid_param, doCheckDebug);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        BinaryTree.Node left_node = crnt_node.makeLeft(left_info);

        IntervalInfo right_info = new IntervalInfo();
        right_info.left = left_info.right;
        right_info.right = crnt_info.right;

        BinaryTree.Node right_node = crnt_node.makeRight(right_info);

        // check

        if (!checkInterval(left_info, tol_2))
            no_pnts += divideInterval(left_node, tol_2);

        if (!checkInterval(right_info, tol_2))
            no_pnts += divideInterval(right_node, tol_2);

        return no_pnts;
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ�����?A
     * ��Ԃ̗��[�싂Ԍ�����?łף�ꂽ�_�̃p���??[�^�l��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ���̃?�\�b�h��
     * {@link #checkInterval(Conic3D.IntervalInfo,double)
     * checkInterval(Conic3D.IntervalInfo, double)}
     * ��ŌĂ�?o�����?B
     * </p>
     *
     * @param left  ?��[ (��ԉ���) �̃p���??[�^�l
     * @param right �E�[ (���?��) �̃p���??[�^�l
     * @return ?łף�ꂽ�_�̃p���??[�^�l
     * @see #checkInterval(Conic3D.IntervalInfo,double)
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
    private boolean checkInterval(IntervalInfo info, double tol_2) {
        double peak_param = getPeak(info.left.parameter(),
                info.right.parameter());

        // ���̃R?[�h�Œu��������?����� : hideit 2000/03/17
        /***
         PointOnCurve3D peak = null;
         try {
         peak = new PointOnCurve3D(this, peak_param, doCheckDebug);
         }
         catch (InvalidArgumentValueException e) {
         throw new FatalException();
         }

         Line3D line = new Line3D(info.left, info.right);
         Point3D proj;
         try {
         proj = line.project1From(peak);
         }
         catch (InvalidArgumentValueException e) {
         throw new FatalException();
         }

         return (peak.subtract(proj).norm() < tol_2);
         ***/

        // ?�̃R?[�h��?�����
        Point3D peak = this.coordinates(peak_param);
        Vector3D unitChord = info.right.subtract(info.left).unitized();
        Vector3D left2peak = peak.subtract(info.left);
        double norm = left2peak.crossProduct(unitChord).norm();

        return (norm < tol_2);
    }

    /**
     * �q�m?[�h��?���Ȃ��m?[�h��?u�f?[�^?v��
     * �^����ꂽ�z��ɑ��� BinaryTree.TraverseProc?B
     */
    private class FillArray implements BinaryTree.TraverseProc {
        /**
         * �q�m?[�h��?���Ȃ��m?[�h��?u�f?[�^?v��^����ꂽ�z��ɑ���?B
         * <p/>
         * pdata �� {@link Conic3D.FillInfo Conic3D.FillInfo} �N���X��
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
        private Point3D[] pnts;

        /**
         * ���̑��� pnts[index] �ɑ΂���?s�Ȃ���ׂ��ł��邱�Ƃ��l?B
         */
        private int index;

        /**
         * �_���ێ?���ׂ��z���?A��̗v�f��?擪����?���^����
         * �I�u�W�F�N�g��?\�z����?B
         */
        private FillInfo(Point3D[] pnts, int index) {
            super();
            this.pnts = pnts;
            this.index = index;
        }
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���̋�?�~�ł���Ƃ�?A
     * �^����ꂽ�_�Ƃ��̉~�̒�?S�Ƃ̋�����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����
     * ?�����?�?��ɂ�?A
     * �p���??[�^�l 0 �̓_�� suitable �Ƃ���
     * IndefiniteSolutionException �̗�O�𓊂���?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li>	�^����ꂽ�_���?��?�BĂ��镽�ʂɓ��e�����_ Q ��?�߂�
     * <li>	Q �����?�ւ̓��e�_ R ��Q��������?�ŋ?�߂�
     * <li>	���e�_ R �̃p���??[�^�l��p���� PointOnCurve3D ��?�?�����
     * </ul>
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s�� (��?�~�ł���?A���e���̓_���~�̒�?S�Ɉ�v����)
     */
    public PointOnCurve3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        CartesianTransformationOperator3D trans = toGlobal();

        Point3D localp = trans.reverseTransform(point);
        Point2D p2 = localp.to2D();
        Conic2D conic2d = toLocal2D();

        PointOnCurve2D[] proj2 = conic2d.projectFrom(p2);
        PointOnCurve3D[] proj3 = new PointOnCurve3D[proj2.length];
        for (int i = 0; i < proj2.length; i++)
            proj3[i] = new PointOnCurve3D(this, proj2[i].parameter(), doCheckDebug);
        return proj3;
    }

    /**
     * ���̉~??��?��?u��?S?v�Ƌ�?����̕��숒肵�Ă����?�?W�n��Ԃ�?B
     *
     * @return ��?S�Ƌ�?����̕�����?�?W�n
     */
    public Axis2Placement3D position() {
        return position;
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̃��C����Ԃ�?B
     * <p/>
     * �~??��?��?�����Ă��Ȃ��̂�?A?�� 0 ��Ԃ�?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ���C��
     */
    public double torsion(double param) {
        // �S�Ă� conic ��?�����Ă��Ȃ�?B
        return 0.0;
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * �~??��?�ɂ͓Hٓ_�͑�?݂��Ȃ��̂�?A?�ɒ��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     */
    public PointOnCurve3D[] singular() {
        return new PointOnCurve3D[0];
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * �~??��?�ɂ͕ϋȓ_�͑�?݂��Ȃ��̂�?A?�ɒ��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     */
    public PointOnCurve3D[] inflexion() {
        return new PointOnCurve3D[0];
    }

    /**
     * ���̉~??��?�� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?����钊?ۃ?�\�b�h?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ���̉~??��?�� poly �̌�_��\����?�����?���
     * @see #intersect(PureBezierCurve3D,boolean)
     * @see #intersect(BsplineCurve3D,boolean)
     */
    abstract DoublePolynomial makePoly(DoublePolynomial[] poly);

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ��邩�ۂ���`�F�b�N���钊?ۃ?�\�b�h?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �^����ꂽ�_�����̋�?�?�ɂ���� true?A�����łȂ���� false
     * @see #intersect(PureBezierCurve3D,boolean)
     * @see #intersect(BsplineCurve3D,boolean)
     */
    abstract boolean checkSolution(Point3D point);

    /**
     * �^����ꂽ�_�����̋�?�?�ɂ����̂Ƃ���?A
     * ���̓_�̋�?�?�ł̃p���??[�^�l��?�߂钊?ۃ?�\�b�h?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �p���??[�^�l
     * @see #intersect(PureBezierCurve3D,boolean)
     * @see #intersect(BsplineCurve3D,boolean)
     */
    abstract double getParameter(Point3D point);

    /**
     * �񎟌��̌�_ {@link IntersectionPoint2D IntersectionPoint2D} ��
     * �O�����̌�_�֕ϊ�����?B
     * <p/>
     * i �Ԗڂ̌�_ ints2[i] �ɂ���?A����
     * {@link IntersectionPoint2D#pointOnCurve1() pointOnCurve1()}.parameter(),
     * {@link IntersectionPoint2D#pointOnCurve2() pointOnCurve2()}.parameter()
     * �⻂̂܂܎g�B�?A
     * �O�����̋�?� curve1, curve2 �̌�_��?�?�����?B
     * </p>
     *
     * @param ints2      �񎟌��̌�_�̔z��
     * @param curve1     pointOnCurve1 �ɑΉ�����O������?�
     * @param curve2     pointOnCurve2 �ɑΉ�����O������?�
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return �O�����̌�_�̔z��
     */
    private IntersectionPoint3D[]
    toIntersectionPoint3D(IntersectionPoint2D[] ints2,
                          ParametricCurve3D curve1,
                          ParametricCurve3D curve2,
                          boolean doExchange) {

        IntersectionPoint3D[] ints3 =
                new IntersectionPoint3D[ints2.length];

        for (int i = 0; i < ints2.length; i++) {
            if (!doExchange)
                ints3[i] = new IntersectionPoint3D
                        (curve1, ints2[i].pointOnCurve1().parameter(),
                                curve2, ints2[i].pointOnCurve2().parameter(), false);
            else
                ints3[i] = new IntersectionPoint3D
                        (curve2, ints2[i].pointOnCurve2().parameter(),
                                curve1, ints2[i].pointOnCurve1().parameter(), false);
        }
        return ints3;
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li>	���̉~??��?� C ��?�BĂ��镽�� P �Ɨ^����ꂽ��?� L �Ƃ̌�_ A ��?�߂�?B
     * <li>	L �� P ��?��?�BĂ��� A ���R�ł��Ȃ�?�?��ɂ�
     * P ?�̓񎟌���Ԃ� C �� L �Ƃ̌�_��?�߂�?B
     * <li>	A ��������?�܂�?�?�?A
     * A �� C ��?��?�BĂ���� A ���_�Ƃ���?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
        IntersectionPoint3D ints;
        Plane3D plane = new Plane3D(position());

        try {
            ints = plane.intersect1(mate);
        } catch (IndefiniteSolutionException e) {
            // lines on the plane
            CartesianTransformationOperator3D trans =
                    new CartesianTransformationOperator3D(position(), 1.0);

            Line2D lline = mate.toLocal2D(trans);
            IntersectionPoint2D[] ints2d;
            try {
                ints2d = toLocal2D().intersect(lline);
            } catch (IndefiniteSolutionException e2) {
                throw new FatalException();    // Never be occured??
            }
            return toIntersectionPoint3D(ints2d, this, mate, doExchange);
        }

        if (ints != null) {
            double cparam;
            try {
                cparam = pointToParameter(ints);
            } catch (InvalidArgumentValueException e) {
                // XXX: assume "point not on the curve"
                return new IntersectionPoint3D[0];
            }

            // just 1 intersection
            PointOnCurve3D pnt2 =
                    (PointOnCurve3D) ints.pointOnGeometry2();

            IntersectionPoint3D[] intvec = {
                    new IntersectionPoint3D(this, cparam,
                            mate, pnt2.parameter(), doCheckDebug)
            };
            if (!doExchange)
                intvec[0] = intvec[0].exchange();

            return intvec;
        }
        return new IntersectionPoint3D[0];
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~??��?�) �Ƃ̌�_�𓾂� (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li>	���̉~??��?� C ��?�BĂ��镽�� P ��
     * �^����ꂽ�~??��?� D ��?�BĂ��镽�� Q �Ƃ̌�?� A ��?�߂�?B
     * <li>	P �� Q ���I?[�o?[���b�v���Ă��� A ��R�ł��Ȃ�?�?��ɂ�
     * ���̋��ʂ̕���?�̓񎟌���Ԃ� C �� D �Ƃ̌�_��?�߂�?B
     * <li>	A ��������?�܂�?�?�?A
     * C �� A �̌�_��?��?A�����̓�� D ��?��?�BĂ����̂��_�Ƃ���?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋�?�(�~??��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersectCnc(Conic3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        Plane3D plane1 = new Plane3D(position());
        Plane3D plane2 = new Plane3D(mate.position());
        Line3D line;
        IntersectionPoint2D[] lpnts;
        CartesianTransformationOperator3D trans1 =
                new CartesianTransformationOperator3D(position(), 1.0);
        Conic2D con1 = toLocal2D();
        Conic2D con2 = mate.toLocal2D(trans1);

        try {
            line = plane1.intersect1Plane(plane2);
        } catch (IndefiniteSolutionException e) {
            // overlap
            lpnts = con1.intersect(con2);
            return toIntersectionPoint3D(lpnts, this, mate, doExchange);
        }
        if (line == null)
            return new IntersectionPoint3D[0];

        Line2D lline = line.toLocal2D(trans1);
        lpnts = con1.intersect(lline);
        Vector intsvec = new Vector();

        for (int i = 0; i < lpnts.length; i++) {
            IntersectionPoint2D int2d = lpnts[i];
            double c2param;

            try {
                c2param = con2.pointToParameter(int2d);
            } catch (InvalidArgumentValueException e) {
                // XXX: assume "point not on the curve"
                continue;
            }
            double c1param = int2d.pointOnCurve1().parameter();
            IntersectionPoint3D int3d =
                    new IntersectionPoint3D(this, c1param,
                            mate, c2param, doCheckDebug);
            intsvec.addElement(int3d);
        }

        IntersectionPoint3D[] ints =
                new IntersectionPoint3D[intsvec.size()];
        intsvec.copyInto(ints);
        return ints;
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
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
        Axis2Placement3D placement = position();
        CartesianTransformationOperator3D transform =
                new CartesianTransformationOperator3D(placement, 1.0);
        int uicp = mate.nControlPoints();
        Point3D[] newCp = new Point3D[uicp];

        // Transform Bezier's control points into conic's local coordinates
        for (int i = 0; i < uicp; i++)
            newCp[i] = transform.toLocal(mate.controlPointAt(i));

        // make Bezier curve from new control points
        PureBezierCurve3D bzc = new
                PureBezierCurve3D(newCp, mate.weights(), false);

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

            Point3D workPoint = bzc.coordinates(realRoot);
            // check solution
            if (!checkSolution(workPoint))
                continue;
            for (int jj = 0; jj < pointVec.size(); jj++) {
                Point3D pt = (Point3D) pointVec.elementAt(jj);
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
        IntersectionPoint3D[] intersectPoint = new IntersectionPoint3D[num];
        for (int i = 0; i < num; i++) {
            // get Parameter from solution point
            Point3D point = (Point3D) pointVec.elementAt(i);
            double param = getParameter(point);

            // make intersection point on Conic
            PointOnCurve3D pointOnConic = new
                    PointOnCurve3D(this, param, doCheckDebug);

            // make intersection point on Bzc
            double work = ((Double) paramVec.elementAt(i)).doubleValue();
            PointOnCurve3D pointOnBzc = new
                    PointOnCurve3D(mate, work, doCheckDebug);

            if (!doExchange)
                intersectPoint[i] = new
                        IntersectionPoint3D(pointOnConic, pointOnBzc, doCheckDebug);
            else
                intersectPoint[i] = new
                        IntersectionPoint3D(pointOnBzc, pointOnConic, doCheckDebug);
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
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
        Axis2Placement3D placement = position();
        CartesianTransformationOperator3D transform =
                new CartesianTransformationOperator3D(placement, 1.0);
        BsplineKnot.ValidSegmentInfo vsegInfo = mate.validSegments();
        Point3D[] cp = mate.controlPoints();
        int uicp = mate.nControlPoints();
        Point3D[] newCp = new Point3D[uicp];

        // Transform Bspline's control points into conic's local coordinates
        for (int i = 0; i < uicp; i++)
            newCp[i] = transform.toLocal(cp[i]);

        // make Bspline curve from new control points
        BsplineCurve3D bsc = new
                BsplineCurve3D(mate.knotData(), newCp, mate.weights());

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

                Point3D workPoint = bsc.coordinates(realRoot);
                // check solution
                if (!checkSolution(workPoint))
                    continue;
                int jj;
                for (jj = 0; jj < k; jj++) {
                    double dTol = bsc.getToleranceForDistance();
                    Point3D pt = (Point3D) pointVec.elementAt(jj);
                    double param = ((Double) paramVec.elementAt(jj)).doubleValue();
                    if (pt.identical(workPoint)
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
        IntersectionPoint3D[] intersectPoint = new IntersectionPoint3D[num];
        for (int i = 0; i < k; i++) {
            // get Parameter from solution point
            Point3D point = (Point3D) pointVec.elementAt(i);
            double param = getParameter(point);

            // make intersection point on Conic
            PointOnCurve3D pointOnConic = new
                    PointOnCurve3D(this, param, doCheckDebug);

            // make intersection point on Bsc
            double work = ((Double) paramVec.elementAt(i)).doubleValue();
            PointOnCurve3D pointOnBsc = new
                    PointOnCurve3D(mate, work, doCheckDebug);

            if (!doExchange)
                intersectPoint[i] = new IntersectionPoint3D
                        (pointOnConic, pointOnBsc, doCheckDebug);
            else
                intersectPoint[i] = new IntersectionPoint3D
                        (pointOnBsc, pointOnConic, doCheckDebug);
        }

        return intersectPoint;
    }

    /**
     * ���̋�?�Ƒ��̋Ȗʂ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public IntersectionPoint3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (����) �ƌ�_��?�߂� (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * return mate.intersect(this, !doExchange)
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Plane3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�񎟋Ȗ�) �ƌ�_��?�߂� (internal use) ?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li>	���̉~??��?��?�BĂ��镽�ʂƗ^����ꂽ�񎟋ȖʂƂ̌�?� A ��?�߂�?B
     * <li>	A �Ƃ��̉~??��?�̌�_���_�Ƃ���?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋Ȗ� (�񎟋Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersectQrd(ElementarySurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] ints;
        SurfaceSurfaceInterference3D[] dCint;
        IntersectionPoint3D[] dEpnt;
        IntersectionPoint3D one_int;
        double[] params;
        double param;
        int i, j;

        /*
        * intersection between Conic's osculating plane & mate => dCint
        */
        Plane3D pln = new Plane3D(this.position());
        dCint = pln.intersect(mate);
        if (dCint.length < 1) {
            return new IntersectionPoint3D[0];
        }

        /*
        * intersection between Conic & dCint
        */
        Vector ints_list = new Vector();
        boolean indefinite = false;
        for (i = 0; i < dCint.length; i++) {
            if (dCint[i].isIntersectionCurve()) {    // ��?�
                IntersectionCurve3D dCintCurve = dCint[i].toIntersectionCurve();
                try {
                    dEpnt = this.intersect(dCintCurve.curve3d());
                } catch (IndefiniteSolutionException e) {        // ����~??��?�
                    dEpnt = new IntersectionPoint3D[1];
                    dEpnt[0] = (IntersectionPoint3D) e.suitable();
                    indefinite = true;
                }
                for (j = 0; j < dEpnt.length; j++) {
                    params = mate.pointToParameter(dEpnt[j]);
                    if (!doExchange)
                        one_int = new IntersectionPoint3D(dEpnt[j].coordinates(),
                                this,
                                dEpnt[j].pointOnCurve1().parameter(),
                                mate, params[0], params[1],
                                doCheckDebug);
                    else
                        one_int = new IntersectionPoint3D(dEpnt[j].coordinates(),
                                mate, params[0], params[1],
                                this,
                                dEpnt[j].pointOnCurve1().parameter(),
                                doCheckDebug);
                    ints_list.addElement(one_int);
                }
            } else {                    // ��_
                IntersectionPoint3D dCintPnt = dCint[i].toIntersectionPoint();
                try {
                    param = this.pointToParameter(dCintPnt);
                } catch (InvalidArgumentValueException e) {    // ?�BĂ��Ȃ�
                    continue;
                }
                if (!doExchange)
                    one_int = new IntersectionPoint3D(this, param,
                            mate,
                            dCintPnt.pointOnSurface2().uParameter(),
                            dCintPnt.pointOnSurface2().vParameter(),
                            doCheckDebug);
                else
                    one_int = new IntersectionPoint3D(mate,
                            dCintPnt.pointOnSurface2().uParameter(),
                            dCintPnt.pointOnSurface2().vParameter(),
                            this, param,
                            doCheckDebug);
                ints_list.addElement(one_int);
            }
        }

        if (indefinite && ints_list.size() == 1) {
            throw new IndefiniteSolutionException((IntersectionPoint3D) ints_list.elementAt(0));
        }
        ints = new IntersectionPoint3D[ints_list.size()];
        ints_list.copyInto(ints);
        return ints;
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (��?͋Ȗ�) �ƌ�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * {@link #intersectQrd(ElementarySurface3D,boolean)
     * intersectQrd(ElementarySurface3D, boolean)} ��Ă�?o��?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (��?͋Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(ElementarySurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return intersectQrd(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�x�W�G�Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * return mate.intersect(this, !doExchange)
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(PureBezierSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * return mate.intersect(this, !doExchange)
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(BsplineSurface3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̉~??��?��
     * ��?�?W�n��łQ�����ŕ\�����ꂽ (���̉~??��?�̈ꕔ��?Č�����) �L�?�x�W�G��?���
     * �R�����̑��?W�n�ł̕\���ɕϊ�����?B
     * <p/>
     * �^������L�?�x�W�G��?���?A
     * (���̉~??��?�̈ꕔ��?Č�������̂�) �e�v�f�͂��ׂĂQ����?�ł����̂Ƒz�肵�Ă���?B
     * </p>
     * <p/>
     * ����?��̂���?A���̃?�\�b�h��?A
     * �x�W�G��?�邢�͂a�X�v���C����?�̃N���X�ɒu���ׂ��ł͂Ȃ���?l����?B
     * </p>
     *
     * @param bezierCurves2D ��?�?W�n��łQ�����ŕ\�����ꂽ�L�?�x�W�G��?�̔z��
     * @return �R�����̑��?W�n�ŕ\�����ꂽ�L�?�x�W�G��?�̔z��
     */
    PureBezierCurve3D[]
    transformPolyBezierCurvesInLocal2DToGrobal3D(PureBezierCurve2D[] bezierCurves2D) {
        int nCurves = bezierCurves2D.length;

        CartesianTransformationOperator3D localTransformationOperator =
                this.position().toCartesianTransformationOperator(1.0);

        PureBezierCurve3D[] bzcs = new PureBezierCurve3D[nCurves];

        for (int i = 0; i < nCurves; i++) {
            Point3D[] controlPoints = new Point3D[3];
            double[] weights = new double[3];

            for (int j = 0; j < 3; j++) {
                Point3D pnt = bezierCurves2D[i].controlPointAt(j).to3D();
                controlPoints[j] = localTransformationOperator.toEnclosed(pnt);
                weights[j] = bezierCurves2D[i].weightAt(j);
            }

            bzcs[i] = new PureBezierCurve3D(controlPoints, weights);
        }

        return bzcs;
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
    protected static BsplineCurve3D
    convertPolyBezierCurvesToOneBsplineCurve(PureBezierCurve3D[] bezierCurves,
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
        Point3D[] controlPoints = new Point3D[uicp];
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

        return new BsplineCurve3D(degree, periodic,
                knotMultiplicities, knots,
                controlPoints, weights);
    }

    /**
     * ���̋�?��?A�������ꂽ�x�W�G�ȖʂƂ̊�?𒲂ׂ钊?ۃ?�\�b�h?B
     * <p/>
     * ���̃?�\�b�h�� {@link IntsCncBzs3D IntsCncBzs3D} �̓Ք�Ŏg����?B
     * </p>
     *
     * @param bi �������ꂽ�x�W�G�Ȗʂ�?��
     * @return ��?��Ă���� true?A�����łȂ���� false
     */
    abstract boolean checkInterfere(IntsCncBzs3D.BezierSurfaceInfo bi);

    /**
     * ���̉~??��?�Ɨ^����ꂽ���ʂ̌�_��?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ���̃?�\�b�h�� {@link IntsCncBzs3D IntsCncBzs3D} �̓Ք�Ŏg����?B
     * </p>
     *
     * @param plane ����
     * @return ��_�̔z��
     */
    abstract IntersectionPoint3D[] intersectConicPlane(Plane3D plane);

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł� (���̉~??��?�̋�?�?W�n��ł�) ?W�l
     * ��Ԃ���?ۃ?�\�b�h?B
     *
     * @param parameter �p���??[�^�l
     * @return ���̉~??��?�̋�?�?W�n��ł�?W�l
     */
    abstract Point3D nlFunc(double parameter);

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł� (���̉~??��?�̋�?�?W�n��ł�) ?ڃx�N�g��
     * ��Ԃ���?ۃ?�\�b�h?B
     *
     * @param parameter �p���??[�^�l
     * @return ���̉~??��?�̋�?�?W�n��ł�?ڃx�N�g��
     */
    abstract Vector3D dnlFunc(double parameter);
}
