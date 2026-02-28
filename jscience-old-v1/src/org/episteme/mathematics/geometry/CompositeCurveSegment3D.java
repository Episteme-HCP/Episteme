/*
 * �R���� : ��?���?��?\?�����Z�O�?���g��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CompositeCurveSegment3D.java,v 1.3 2006/03/01 21:15:55 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : ��?���?��?\?�����Z�O�?���g��\���N���X?B
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
 */

public class CompositeCurveSegment3D extends BoundedCurve3D {

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
    private BoundedCurve3D parentCurve;

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
    public CompositeCurveSegment3D(int transition, boolean sameSense,
                                   BoundedCurve3D parentCurve) {
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
    public BoundedCurve3D parentCurve() {
        return this.parentCurve;
    }

    /**
     * ���̗L��?�̊J�n�_��Ԃ�?B
     *
     * @return �J�n�_
     */
    public Point3D startPoint() {
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
    public Point3D endPoint() {
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
    public Point3D coordinates(double param) {
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
    public Vector3D tangentVector(double param) {
        Vector3D tang = parentCurve.tangentVector(toBasisParameter(param));

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
    public CurveCurvature3D curvature(double param) {
        return parentCurve.curvature(toBasisParameter(param));
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
        return parentCurve.torsion(toBasisParameter(param));
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
        CurveDerivative3D curv =
                parentCurve.evaluation(toBasisParameter(param));
        if (sameSense)
            return curv;
        else {
            CurveDerivative3D rcurv =
                    new CurveDerivative3D(curv.d0D(), curv.d1D().multiply(-1),
                            curv.d2D(), curv.d3D().multiply(-1));
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
    public PointOnCurve3D[] singular() throws IndefiniteSolutionException {
        PointOnCurve3D[] singular = parentCurve.singular();
        PointOnCurve3D[] thisSingular =
                new PointOnCurve3D[singular.length];

        for (int i = 0; i < singular.length; i++) {

            try {
                thisSingular[i] = new PointOnCurve3D
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
    public PointOnCurve3D[] inflexion() throws IndefiniteSolutionException {
        PointOnCurve3D[] inflexion = parentCurve.inflexion();
        PointOnCurve3D[] thisInflexion =
                new PointOnCurve3D[inflexion.length];

        for (int i = 0; i < inflexion.length; i++) {

            try {
                thisInflexion[i] = new PointOnCurve3D
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
    public PointOnCurve3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        PointOnCurve3D[] proj = parentCurve.projectFrom(point);
        PointOnCurve3D[] proj2 = new PointOnCurve3D[proj.length];

        for (int i = 0; i < proj.length; i++) {
            try {
                proj2[i] = new PointOnCurve3D
                        (this, toOwnParameter(proj[i].parameter()));
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }
        return proj2;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X��?�Ƃ��� PointOnCurve3D ��
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
    public Polyline3D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        Polyline3D pl = parentCurve.toPolyline(toBasisParameter(pint), tol);
        Point3D[] pnts = new Point3D[pl.nPoints()];

        for (int i = 0; i < pnts.length; i++) {
            PointOnCurve3D p = (PointOnCurve3D) pl.pointAt(i);
            pnts[i] = new PointOnCurve3D(this, toOwnParameter(p.parameter()), doCheckDebug);
        }
        return new Polyline3D(pnts);
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
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
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
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    private IntersectionPoint3D[] doIntersect(ParametricCurve3D mate,
                                              boolean doExchange) {
        IntersectionPoint3D[] ints;
        try {
            ints = parentCurve.intersect(mate);
        } catch (IndefiniteSolutionException e) {
            return new IntersectionPoint3D[0];    // ??
        }
        IntersectionPoint3D[] thisInts =
                new IntersectionPoint3D[ints.length];

        for (int i = 0; i < ints.length; i++) {
            double param = toOwnParameter(ints[i].pointOnCurve1().parameter());
            PointOnCurve3D thisPnts = new PointOnCurve3D(this, param, doCheckDebug);

            if (!doExchange)
                thisInts[i] =
                        new IntersectionPoint3D(thisPnts, ints[i].pointOnGeometry2(), doCheckDebug);
            else
                thisInts[i] =
                        new IntersectionPoint3D(ints[i].pointOnGeometry2(), thisPnts, doCheckDebug);
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
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate) {
        return doIntersect(mate, false);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(Circle3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(Hyperbola3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(Polyline3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(TrimmedCurve3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(CompositeCurveSegment3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve3D,boolean)
     */
    IntersectionPoint3D[] intersect(CompositeCurve3D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
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
    IntersectionPoint3D[] doIntersect(ParametricSurface3D mate,
                                      boolean doExchange) {
        IntersectionPoint3D[] intp;
        try {
            intp = parentCurve.intersect(mate);
        } catch (IndefiniteSolutionException e) {
            return new IntersectionPoint3D[0];    // ??
        }

        CurveSurfaceInterferenceList intfList =
                new CurveSurfaceInterferenceList(this, mate);

        for (int i = 0; i < intp.length; i++) {
            PointOnCurve3D crvPnt =
                    (PointOnCurve3D) intp[i].pointOnGeometry1();
            double crvParam = toOwnParameter(crvPnt.parameter());
            PointOnSurface3D srfPnt =
                    (PointOnSurface3D) intp[i].pointOnGeometry2();
            intfList.addAsIntersection(intp[i].coordinates(), crvParam,
                    srfPnt.uParameter(), srfPnt.vParameter());
        }
        return intfList.toIntersectionPoint3DArray(doExchange);
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
     * ���̗L��?�Ƒ��̗L��?�̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ���?�̊�?̔z��
     */
    public CurveCurveInterference3D[] interfere(BoundedCurve3D mate) {
        return this.interfere(mate, false);
    }

    /**
     * ���?�ɑ΂���p���??[�^�l����
     * �Z�O�?���g�ɑ΂���p���??[�^�l�ւ̕ϊ�?��?��\���Ք�N���X?B
     *
     * @see #interfere(BoundedCurve3D,boolean)
     */
    class ToSegmentConversion extends ParameterConversion3D {
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
            return CompositeCurveSegment3D.this.toOwnParameter(param);
        }

        /**
         * ���?�ɑ΂���p���??[�^�l��ϊ������?ۂł���Z�O�?���g��Ԃ�?B
         *
         * @param param ���?�ɑ΂���p���??[�^�l
         * @return �Z�O�?���g
         */
        ParametricCurve3D convCurve(double param) {
            return CompositeCurveSegment3D.this;
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
     * @see CompositeCurveSegment3D.ToSegmentConversion
     */
    CurveCurveInterference3D[] interfere(BoundedCurve3D mate,
                                         boolean doExchange) {
        ToSegmentConversion conv = new ToSegmentConversion();
        ParameterSection sec = this.parameterDomain().section();

        CurveCurveInterference3D[] intf;
        if (!doExchange) {
            intf = this.parentCurve.interfere(mate);
        } else {
            intf = mate.interfere(this.parentCurve);
        }

        Vector vec = new Vector();

        for (int i = 0; i < intf.length; i++) {
            CurveCurveInterference3D trimintf;
            if (!doExchange) {
                trimintf = intf[i].trim1(sec, conv);
            } else {
                trimintf = intf[i].trim2(sec, conv);
            }
            if (trimintf != null)
                vec.addElement(trimintf);
        }
        CurveCurveInterference3D[] interf =
                new CurveCurveInterference3D[vec.size()];
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
     * @see #interfere(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(BoundedLine3D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve3D) mate, doExchange);
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
     * @see #interfere(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(Polyline3D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve3D) mate, doExchange);
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
     * @see #interfere(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(PureBezierCurve3D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve3D) mate, doExchange);
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
     * @see #interfere(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(BsplineCurve3D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve3D) mate, doExchange);
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
     * @see #interfere(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(TrimmedCurve3D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve3D) mate, doExchange);
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
     * @see #interfere(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(CompositeCurveSegment3D mate,
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
     * @see #interfere(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(CompositeCurve3D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ�?B
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     */
    public ParametricCurve3D parallelTranslate(Vector3D moveVec) {
        BoundedCurve3D newParent = (BoundedCurve3D) parentCurve.parallelTranslate(moveVec);
        return new CompositeCurveSegment3D(transition, sameSense, newParent);
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
     * @return {@link ParametricCurve3D#COMPOSITE_CURVE_SEGMENT_3D ParametricCurve3D.COMPOSITE_CURVE_SEGMENT_3D}
     */
    int type() {
        return COMPOSITE_CURVE_SEGMENT_3D;
    }

    /**
     * a     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ���?�R�`?�ł���� true?A�����łȂ���� false
     */
    public boolean isFreeform() {
        return this.parentCurve.isFreeform();
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
        BoundedCurve3D r_bnd = (BoundedCurve3D) parentCurve().rotateZ(trns, rCos, rSin);
        return new CompositeCurveSegment3D(transition(), sameSense(), r_bnd);
    }

    /**
     * ���̋�?�?�̓_��?A�^����ꂽ��?�?�ɂȂ��_���Ԃ�?B
     *
     * @param line ��?�
     * @return �^����ꂽ��?�?�ɂȂ��_
     */
    Point3D getPointNotOnLine(Line3D line) {
        ParameterSection pint = parameterDomain().section();
        BsplineCurve3D b_spline = toBsplineCurve(pint);
        return b_spline.getPointNotOnLine(line);
    }

    /**
     * ���̃Z�O�?���g��?u���̃Z�O�?���g�Ƃ̘A��?���?�?�?v��
     * �w��̒l�ɕ�?X�����I�u�W�F�N�g��Ԃ�?B
     *
     * @param transition ���̃Z�O�?���g�Ƃ̘A��?���?�?�
     * @return �w��̕�?X��B���?�?������Z�O�?���g
     */
    CompositeCurveSegment3D makeCopyWithTransition(int transition) {
        return new CompositeCurveSegment3D(transition,
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
    CompositeCurveSegment3D makeReverseWithTransition(int transition) {
        return new CompositeCurveSegment3D(transition,
                !this.sameSense,
                this.parentCurve);
    }

    /**
     * ���̃Z�O�?���g��?A�^����ꂽ�p���??[�^��Ԃ�?ؒf�����I�u�W�F�N�g��Ԃ�?B
     *
     * @param section ?ؒf���Ďc��������\���p���??[�^���
     * @return ?ؒf���Ďc����������\���Z�O�?���g
     */
    CompositeCurveSegment3D truncate(ParameterSection section,
                                     int transition) {
        ParameterSection parentSection = this.toBasisParameter(section);
        TrimmedCurve3D newParent;

        if (this.parentCurve.type() == TRIMMED_CURVE_3D) {
            TrimmedCurve3D trc = (TrimmedCurve3D) this.parentCurve;
            newParent = new TrimmedCurve3D(trc.basisCurve(), parentSection);
        } else {
            newParent = new TrimmedCurve3D(this.parentCurve, parentSection);
        }

        return new CompositeCurveSegment3D(transition, true, newParent);
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
        BoundedCurve3D tParentCurve =
                (BoundedCurve3D) this.parentCurve().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new CompositeCurveSegment3D(this.transition(),
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
