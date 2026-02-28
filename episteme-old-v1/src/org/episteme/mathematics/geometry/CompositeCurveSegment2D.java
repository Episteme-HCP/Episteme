/*
 * �Q���� : ��?���?��?\?�����Z�O�?���g��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CompositeCurveSegment2D.java,v 1.3 2006/03/01 21:15:55 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �Q���� : ��?���?��?\?�����Z�O�?���g��\���N���X?B
 * <p/>
 * ��?���?�Ƃ�?A (�[�_�ŘA������) ����̗L��?��܂Ƃ߂�
 * ��{�̋�?�Ɍ����Ă���̂ł���?B
 * ���̃N���X��?A
 * ���̕�?���?��?\?������?X�̗L��?� (�����?���?�̃Z�O�?���g�Ƃ���)
 * ��\��?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * <ul>
 * <li> �Z�O�?���g�̎�?ۂ̋O?Ղ�\���L��?� parentCurve (�������?�Ƃ���)
 * <li>	�Z�O�?���g�����?�Ɠ�����ۂ����t���O sameSense
 * <li> ���̃Z�O�?���g�Ƃ̘A��?���?�?� transition
 * </ul>
 * ��ێ?����?B
 * </p>
 * <p/>
 * ��?���?�Z�O�?���g�̒�`���?A���?�̒�`��Ɉ�v����?B
 * </p>
 * <p/>
 * <a name="CONSTRAINTS">[��?��Ԃ�?S��?�?]</a>
 * </p>
 * <p/>
 * parentCurve �͊J�����`���łȂ���΂Ȃ�Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:55 $
 * @see CompositeCurve2D
 */

public class CompositeCurveSegment2D extends BoundedCurve2D {

    /**
     * ���̃Z�O�?���g�Ƃ̘A��?���?�?�?B
     *
     * @serial
     * @see TransitionCode
     */
    private int transition;

    /**
     * �Z�O�?���g�����?�Ɠ�����ۂ����t���O?B
     *
     * @serial
     */
    private boolean sameSense;

    /**
     * �Z�O�?���g�̎�?ۂ̋O?Ղ�\�����?�?B
     *
     * @serial
     */
    private BoundedCurve2D parentCurve;

    /**
     * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param transition  ���̃Z�O�?���g�Ƃ̘A��?���?�?�
     * @param sameSense   ���?�Ɠ�����ۂ����t���O
     * @param parentCurve ���?�
     * @see TransitionCode
     * @see InvalidArgumentValueException
     */
    public CompositeCurveSegment2D(int transition, boolean sameSense,
                                   BoundedCurve2D parentCurve) {
        super();

        if (parentCurve.isPeriodic() || parentCurve.isInfinite())
            throw new InvalidArgumentValueException();

        this.transition = transition;
        this.sameSense = sameSense;
        this.parentCurve = parentCurve;
    }

    /**
     * ���̃Z�O�?���g�̎��̃Z�O�?���g�Ƃ̘A��?���?�?���Ԃ�?B
     *
     * @return ���̃Z�O�?���g�Ƃ̘A��?���?�?�
     * @see TransitionCode
     */
    public int transition() {
        return this.transition;
    }

    /**
     * ���̃Z�O�?���g�����?�Ɠ�����ۂ����t���O��Ԃ�?B
     *
     * @return ��?�Ɠ�����ۂ����t���O
     */
    public boolean sameSense() {
        return this.sameSense;
    }

    /**
     * ���̃Z�O�?���g�̕��?��Ԃ�?B
     *
     * @return ���?�
     */
    public BoundedCurve2D parentCurve() {
        return this.parentCurve;
    }

    /**
     * ���̗L��?�̊J�n�_��Ԃ�?B
     *
     * @return �J�n�_
     */
    public Point2D startPoint() {
        if (sameSense)
            return parentCurve.startPoint();
        else
            return parentCurve.endPoint();
    }

    /**
     * ���̗L��?��?I���_��Ԃ�?B
     *
     * @return ?I���_
     */
    public Point2D endPoint() {
        if (sameSense)
            return parentCurve.endPoint();
        else
            return parentCurve.startPoint();
    }

    /**
     * ���̗L��?�̊J�n�_�̃p���??[�^�l��Ԃ�?B
     *
     * @return �J�n�_�̃p���??[�^�l
     */
    double sParameter() {
        return parameterDomain().section().start();
    }

    /**
     * ���̗L��?��?I���_�̃p���??[�^�l��Ԃ�?B
     *
     * @return ?I���_�̃p���??[�^�l
     */
    double eParameter() {
        return parameterDomain().section().end();
    }

    /**
     * ���̃Z�O�?���g�ɑ΂��ė^����ꂽ�p���??[�^�l��?A
     * ���?�ɑ΂���p���??[�^�l�ɕϊ�����?B
     * <p/>
     * ���̃Z�O�?���g�̒�`��͕��?�̒�`��ƈ�v���邪?A
     * sameSense �� false ��?�?��ɂ�?A�p���??[�^�l�̕ϊ����K�v�ɂȂ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l�����̋�?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �Z�O�?���g�ɑ΂���p���??[�^�l
     * @return ���?�ɑ΂���p���??[�^�l
     */
    private double toBasisParameter(double param) {
        checkValidity(param);

        if (sameSense)
            return param;
        else {
            ParameterSection sec = parameterDomain().section();
            return sec.end() - (param - sec.start());
        }
    }

    /**
     * ���̃Z�O�?���g�ɑ΂��ė^����ꂽ�p���??[�^��Ԃ�?A
     * ���?�ɑ΂���p���??[�^��Ԃɕϊ�����?B
     * <p/>
     * ���̃Z�O�?���g�̒�`��͕��?�̒�`��ƈ�v���邪?A
     * sameSense �� false ��?�?��ɂ�?A�p���??[�^�l�̕ϊ����K�v�ɂȂ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ����̋�?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param pint �Z�O�?���g�ɑ΂���p���??[�^���
     * @return ���?�ɑ΂���p���??[�^���
     */
    public ParameterSection toBasisParameter(ParameterSection pint) {
        double start = toBasisParameter(pint.start());
        double end = toBasisParameter(pint.end());

        return new ParameterSection(start, end - start);
    }

    /**
     * ���̃Z�O�?���g�̕��?�ɑ΂��ė^����ꂽ�p���??[�^�l��?A
     * ���̃Z�O�?���g�ɑ΂���p���??[�^�l�ɕϊ�����?B
     * <p/>
     * ���̃Z�O�?���g�̒�`��͕��?�̒�`��ƈ�v���邪?A
     * sameSense �� false ��?�?��ɂ�?A�p���??[�^�l�̕ϊ����K�v�ɂȂ�?B
     * </p>
     *
     * @param param ���?�ɑ΂���p���??[�^�l
     * @return �Z�O�?���g�ɑ΂���p���??[�^�l
     */
    private double toOwnParameter(double param) {
        double result;

        if (sameSense)
            result = param;
        else {
            ParameterSection sec = parameterDomain().section();
            result = sec.start() - (param - sec.end());
        }

        return result;
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
        return parentCurve.length(toBasisParameter(pint));
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
        return parentCurve.coordinates(toBasisParameter(param));
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
        Vector2D tang = parentCurve.tangentVector(toBasisParameter(param));

        if (sameSense)
            return tang;
        else
            return tang.multiply(-1);
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
        return parentCurve.curvature(toBasisParameter(param));
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
        CurveDerivative2D curv =
                parentCurve.evaluation(toBasisParameter(param));
        if (sameSense)
            return curv;
        else {
            CurveDerivative2D rcurv =
                    new CurveDerivative2D(curv.d0D(), curv.d1D().multiply(-1),
                            curv.d2D());
            return rcurv;
        }
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * �Hٓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnCurve2D[] singular() throws IndefiniteSolutionException {
        PointOnCurve2D[] singular = parentCurve.singular();
        PointOnCurve2D[] thisSingular =
                new PointOnCurve2D[singular.length];

        for (int i = 0; i < singular.length; i++) {
            try {
                thisSingular[i] = new PointOnCurve2D
                        (this, toOwnParameter(singular[i].parameter()));
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

        }
        return thisSingular;
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * �ϋȓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnCurve2D[] inflexion() throws IndefiniteSolutionException {
        PointOnCurve2D[] inflexion = parentCurve.inflexion();
        PointOnCurve2D[] thisInflexion =
                new PointOnCurve2D[inflexion.length];

        for (int i = 0; i < inflexion.length; i++) {
            try {
                thisInflexion[i] = new PointOnCurve2D
                        (this, toOwnParameter(inflexion[i].parameter()));
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }
        return thisInflexion;
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnCurve2D[] projectFrom(Point2D point)
            throws IndefiniteSolutionException {
        PointOnCurve2D[] proj = parentCurve.projectFrom(point);
        PointOnCurve2D[] thisProj =
                new PointOnCurve2D[proj.length];
        for (int i = 0; i < proj.length; i++) {
            try {
                thisProj[i] = new PointOnCurve2D
                        (this, toOwnParameter(proj[i].parameter()));
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }
        return thisProj;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X��?�Ƃ��� PointOnCurve2D ��
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
    public Polyline2D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        Polyline2D pl = parentCurve.toPolyline(toBasisParameter(pint), tol);
        Point2D[] pnts = new Point2D[pl.nPoints()];

        for (int i = 0; i < pnts.length; i++) {
            PointOnCurve2D p = (PointOnCurve2D) pl.pointAt(i);
            pnts[i] = new PointOnCurve2D(this, toOwnParameter(p.parameter()), doCheckDebug);
        }
        return new Polyline2D(pnts);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     * <p/>
     * pint �̒l��?A���̂a�X�v���C����?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     * @see ParameterOutOfRange
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        return parentCurve.toBsplineCurve(toBasisParameter(pint));
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * ���̋�?�̕��?�Ƒ��̋�?�̌�_��?�߂����?A
     * �����̕��?�ɑ΂���p���??[�^�l��
     * ���̃Z�O�?���g�ɑ΂���p���??[�^�l�ɕϊ��������
     * ��?u��_?v�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?�
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    private IntersectionPoint2D[] doIntersect(ParametricCurve2D mate,
                                              boolean doExchange) {
        IntersectionPoint2D[] ints;
        try {
            ints = parentCurve.intersect(mate);
        } catch (IndefiniteSolutionException e) {
            return new IntersectionPoint2D[0];    // ??
        }
        IntersectionPoint2D[] thisInts =
                new IntersectionPoint2D[ints.length];

        for (int i = 0; i < ints.length; i++) {
            double param = toOwnParameter(ints[i].pointOnCurve1().parameter());
            PointOnCurve2D thisPnts =
                    new PointOnCurve2D(this, param, doCheckDebug);

            if (!doExchange)
                thisInts[i] =
                        new IntersectionPoint2D(thisPnts, ints[i].pointOnCurve2(), doCheckDebug);
            else
                thisInts[i] =
                        new IntersectionPoint2D(ints[i].pointOnCurve2(), thisPnts, doCheckDebug);
        }
        return thisInts;
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
        return doIntersect(mate, false);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
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
        return this.interfere(mate, false);
    }

    /**
     * ���?�ɑ΂���p���??[�^�l����
     * �Z�O�?���g�ɑ΂���p���??[�^�l�ւ̕ϊ�?��?��\���Ք�N���X?B
     *
     * @see #interfere(BoundedCurve2D,boolean)
     */
    class ToSegmentConversion extends ParameterConversion2D {
        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         */
        ToSegmentConversion() {
        }

        /**
         * ���?�ɑ΂���p���??[�^�l��Z�O�?���g�ɑ΂���p���??[�^�l�֕ϊ�����?B
         *
         * @param param ���?�ɑ΂���p���??[�^�l
         * @return �Z�O�?���g�ɑ΂���p���??[�^�l
         */
        double convParameter(double param) {
            return CompositeCurveSegment2D.this.toOwnParameter(param);
        }

        /**
         * ���?�ɑ΂���p���??[�^�l��ϊ������?ۂł���Z�O�?���g��Ԃ�?B
         *
         * @param param ���?�ɑ΂���p���??[�^�l
         * @return �Z�O�?���g
         */
        ParametricCurve2D convCurve(double param) {
            return CompositeCurveSegment2D.this;
        }
    }

    /**
     * ���̗L��?�Ƒ��̗L��?�̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋�?�̕��?�Ƒ��̗L��?�̊�?�?�߂����?A
     * �����̕��?�ɑ΂���p���??[�^�l��
     * ���̃Z�O�?���g�ɑ΂���p���??[�^�l�ɕϊ��������
     * ��?u��?�?v�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?�
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     * @see CompositeCurveSegment2D.ToSegmentConversion
     */
    CurveCurveInterference2D[] interfere(BoundedCurve2D mate,
                                         boolean doExchange) {
        ToSegmentConversion conv = new ToSegmentConversion();
        ParameterSection sec = this.parameterDomain().section();

        CurveCurveInterference2D[] intf;
        if (!doExchange) {
            intf = this.parentCurve.interfere(mate);
        } else {
            intf = mate.interfere(this.parentCurve);
        }

        Vector vec = new Vector();

        for (int i = 0; i < intf.length; i++) {
            CurveCurveInterference2D trimintf;
            if (!doExchange) {
                trimintf = intf[i].trim1(sec, conv);
            } else {
                trimintf = intf[i].trim2(sec, conv);
            }
            if (trimintf != null)
                vec.addElement(trimintf);
        }
        CurveCurveInterference2D[] interf =
                new CurveCurveInterference2D[vec.size()];
        vec.copyInto(interf);
        return interf;
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(BoundedLine2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�|�����C��) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�|�����C��)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(Polyline2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�x�W�G��?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�x�W�G��?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(PureBezierCurve2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�a�X�v���C����?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�a�X�v���C����?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(BsplineCurve2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�g������?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�g������?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(TrimmedCurve2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�Z�O�?���g) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�Z�O�?���g)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(CompositeCurveSegment2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
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
    public BsplineCurve2D offsetByBsplineCurve(ParameterSection pint,
                                               double magni,
                                               int side,
                                               ToleranceForDistance tol) {
        Ofst2D doObj = new Ofst2D(this, pint, magni, side, tol);
        return doObj.offset();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ�����L��?��?�߂�?B
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
        ParameterSection basisPint = this.toBasisParameter(pint);
        int basisSide = (this.sameSense() == true)
                ? side : WhichSide.reverse(side);

        return this.parentCurve().offsetByBoundedCurve(basisPint, magni, basisSide, tol);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A���̋�?�̎w��̋�Ԃɂ�����t�B���b�g��?�߂�?B
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
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     */
    FilletObject2D[] doFillet(ParameterSection pint1,
                              int side1,
                              ParametricCurve2D mate,
                              ParameterSection pint2,
                              int side2,
                              double radius,
                              boolean doExchange)
            throws IndefiniteSolutionException {
        FilletObject2D[] flts;
        try {
            flts = parentCurve.fillet(toBasisParameter(pint1), side1, mate, pint2, side2, radius);
        } catch (IndefiniteSolutionException e) {
            flts = new FilletObject2D[1];
            flts[0] = (FilletObject2D) e.suitable();
        }

        for (int i = 0; i < flts.length; i++) {
            double param = toOwnParameter(flts[i].pointOnCurve1().parameter());
            PointOnCurve2D thisPnt = new PointOnCurve2D(this, param, doCheckDebug);

            if (!doExchange)
                flts[i] = new FilletObject2D(radius, flts[i].center(), thisPnt, flts[i].pointOnCurve2());
            else
                flts[i] = new FilletObject2D(radius, flts[i].center(), flts[i].pointOnCurve2(), thisPnt);
        }
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
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     * <p/>
     * ���?�̒�`���Ԃ�?B
     * </p>
     *
     * @return �L�Ŕ���I�ȃp���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        return parentCurve.parameterDomain();
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#COMPOSITE_CURVE_SEGMENT_2D ParametricCurve2D.COMPOSITE_CURVE_SEGMENT_2D}
     */
    int type() {
        return COMPOSITE_CURVE_SEGMENT_2D;
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ���?�R�`?�ł���� true?A�����łȂ���� false
     */
    public boolean isFreeform() {
        return this.parentCurve.isFreeform();
    }

    /**
     * ���̃Z�O�?���g��?u���̃Z�O�?���g�Ƃ̘A��?���?�?�?v��
     * �w��̒l�ɕ�?X�����I�u�W�F�N�g��Ԃ�?B
     *
     * @param transition ���̃Z�O�?���g�Ƃ̘A��?���?�?�
     * @return �w��̕�?X��B���?�?������Z�O�?���g
     */
    CompositeCurveSegment2D makeCopyWithTransition(int transition) {
        return new CompositeCurveSegment2D(transition,
                this.sameSense,
                this.parentCurve);
    }

    /**
     * ���̃Z�O�?���g��?u���̃Z�O�?���g�Ƃ̘A��?���?�?�?v��
     * �w��̒l�ɕ�?X��?A�Z�O�?���g�̕��𔽓]�������I�u�W�F�N�g��Ԃ�?B
     *
     * @param transition ���̃Z�O�?���g�Ƃ̘A��?���?�?�
     * @return �w��̕�?X��B���?�?������Z�O�?���g
     */
    CompositeCurveSegment2D makeReverseWithTransition(int transition) {
        return new CompositeCurveSegment2D(transition,
                !this.sameSense,
                this.parentCurve);
    }

    /**
     * ���̃Z�O�?���g��?A�^����ꂽ�p���??[�^��Ԃ�?ؒf�����I�u�W�F�N�g��Ԃ�?B
     *
     * @param section ?ؒf���Ďc��������\���p���??[�^���
     * @return ?ؒf���Ďc����������\���Z�O�?���g
     */
    CompositeCurveSegment2D truncate(ParameterSection section,
                                     int transition) {
        ParameterSection parentSection = this.toBasisParameter(section);
        TrimmedCurve2D newParent;

        if (this.parentCurve.type() == TRIMMED_CURVE_2D) {
            TrimmedCurve2D trc = (TrimmedCurve2D) this.parentCurve;
            newParent = new TrimmedCurve2D(trc.basisCurve(), parentSection);
        } else {
            newParent = new TrimmedCurve2D(this.parentCurve, parentSection);
        }

        return new CompositeCurveSegment2D(transition, true, newParent);
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
        BoundedCurve2D tParentCurve =
                (BoundedCurve2D) this.parentCurve().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new CompositeCurveSegment2D(this.transition(),
                this.sameSense(),
                tParentCurve);
    }

    /**
     * ���̋�?�|�����C���̕�����܂ނ��ۂ���Ԃ�?B
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C����܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean hasPolyline() {
        return parentCurve.hasPolyline();
    }

    /**
     * ���̋�?�|�����C���̕��������łł��Ă��邩�ۂ���Ԃ�?B
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C��������܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean isComposedOfOnlyPolylines() {
        return parentCurve.isComposedOfOnlyPolylines();
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
        writer.println(indent_tab + "\ttransition\t"
                + TransitionCode.toString(transition));
        writer.println(indent_tab + "\tsameSense\t" + sameSense);
        writer.println(indent_tab + "\tparentCurve");
        parentCurve.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
