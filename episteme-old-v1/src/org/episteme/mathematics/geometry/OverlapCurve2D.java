/*
 * �Q���� : ���?� (��?�����) �I?[�o?[���b�v���Ă����Ԃ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: OverlapCurve2D.java,v 1.3 2007-10-21 21:08:15 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �Q���� : ���?� (��?�����) �I?[�o?[���b�v���Ă����Ԃ�\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ���?�̃I?[�o?[���b�v��
 * ���̋�?�ɂ�����p���??[�^�͈͂�\���g������?� trc1
 * ��
 * ����̋�?�ɂ�����p���??[�^�͈͂�\���g������?� trc2
 * ��ێ?����?B
 * </p>
 * <p/>
 * �Ȃ�?A
 * trc1 �� trc2 ����?ۂɓ����O?Ղ�?���ǂ�����?A
 * ���̃N���X�̓Ք�ł͊֒m���Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:15 $
 */

public class OverlapCurve2D extends NonParametricCurve2D implements CurveCurveInterference2D {
    /**
     * ���̋�?� (��?�1) ��I?[�o?[���b�v��ԂŃg���~���O������?�
     *
     * @serial
     */
    private TrimmedCurve2D trc1;

    /**
     * ����̋�?� (��?�2) ��I?[�o?[���b�v��ԂŃg���~���O������?�
     *
     * @serial
     */
    private TrimmedCurve2D trc2;

    /**
     * ��?��I?[�o?[���b�v��Ԃ̎w�肹���ɃI�u�W�F�N�g��?\�z����?B
     */
    private OverlapCurve2D() {
        trc1 = null;
        trc2 = null;
    }

    /**
     * ��̃g������?��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
     *
     * @param trc1    ���̋�?� (��?�1) ��I?[�o?[���b�v��ԂŃg���~���O������?�
     * @param trc2    ����̋�?� (��?�2) ��I?[�o?[���b�v��ԂŃg���~���O������?�
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    OverlapCurve2D(TrimmedCurve2D trc1,
                   TrimmedCurve2D trc2,
                   boolean doCheck) {
        super();
        this.trc1 = trc1;
        this.trc2 = trc2;
    }

    /**
     * ��̋�?��?A�I?[�o?[���b�v��Ԃ̂��ꂼ��̋�?�ł̃p���??[�^�͈͂�^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
     *
     * @param curve1   ���̋�?� (��?�1)
     * @param section1 �I?[�o?[���b�v��Ԃ̋�?�1 �ł̃p���??[�^���
     * @param curve2   ���̋�?� (��?�2)
     * @param section2 �I?[�o?[���b�v��Ԃ̋�?�2 �ł̃p���??[�^���
     * @param doCheck  ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    OverlapCurve2D(ParametricCurve2D curve1,
                   ParameterSection section1,
                   ParametricCurve2D curve2,
                   ParameterSection section2,
                   boolean doCheck) {
        super();
        this.trc1 = new TrimmedCurve2D(curve1, section1);
        this.trc2 = new TrimmedCurve2D(curve2, section2);
    }

    /**
     * ��̋�?��?A�I?[�o?[���b�v��Ԃ̂��ꂼ��̋�?�ł̃p���??[�^�͈͂�^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
     *
     * @param curve1  ���̋�?� (��?�1)
     * @param start1  �I?[�o?[���b�v��Ԃ̋�?�1 �ł̃p���??[�^��Ԃ̊J�n�l
     * @param inc1    �I?[�o?[���b�v��Ԃ̋�?�1 �ł̃p���??[�^��Ԃ̑?���l
     * @param curve2  ���̋�?� (��?�2)
     * @param start2  �I?[�o?[���b�v��Ԃ̋�?�2 �ł̃p���??[�^��Ԃ̊J�n�l
     * @param inc2    �I?[�o?[���b�v��Ԃ̋�?�2 �ł̃p���??[�^��Ԃ̑?���l
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ����̃t���O
     */
    public OverlapCurve2D(ParametricCurve2D curve1,
                          double start1, double inc1,
                          ParametricCurve2D curve2,
                          double start2, double inc2,
                          boolean doCheck) {
        super();
        this.trc1 = new TrimmedCurve2D(curve1, new ParameterSection(start1, inc1));
        this.trc2 = new TrimmedCurve2D(curve2, new ParameterSection(start2, inc2));
    }

    /**
     * ���̃I?[�o?[���b�v�̈��̋�?� (��?�1) ��Ԃ�?B
     *
     * @return ��?�1
     */
    public ParametricCurve2D curve1() {
        return trc1.basisCurve();
    }

    /**
     * ���̃I?[�o?[���b�v�̈��̋�?� (��?�1) �ł̃p���??[�^��Ԃ̊J�n�l��Ԃ�?B
     *
     * @return ��?�1 �ł̃p���??[�^��Ԃ̊J�n�l
     */
    public double start1() {
        return trc1.tParam1();
    }

    /**
     * ���̃I?[�o?[���b�v�̈��̋�?� (��?�1) �ł̃p���??[�^��Ԃ�?I���l��Ԃ�?B
     *
     * @return ��?�1 �ł̃p���??[�^��Ԃ�?I���l
     */
    public double end1() {
        return trc1.tParam2();
    }

    /**
     * ���̃I?[�o?[���b�v�̈��̋�?� (��?�1) �ł̃p���??[�^��Ԃ̑?���l��Ԃ�?B
     *
     * @return ��?�1 �ł̃p���??[�^��Ԃ̑?���l
     */
    public double increase1() {
        return end1() - start1();
    }

    /**
     * ���̃I?[�o?[���b�v�̑���̋�?� (��?�2) ��Ԃ�?B
     *
     * @return ��?�2
     */
    public ParametricCurve2D curve2() {
        return trc2.basisCurve();
    }

    /**
     * ���̃I?[�o?[���b�v�̑���̋�?� (��?�2) �ł̃p���??[�^��Ԃ̊J�n�l��Ԃ�?B
     *
     * @return ��?�2 �ł̃p���??[�^��Ԃ̊J�n�l
     */
    public double start2() {
        return trc2.tParam1();
    }

    /**
     * ���̃I?[�o?[���b�v�̑���̋�?� (��?�2) �ł̃p���??[�^��Ԃ�?I���l��Ԃ�?B
     *
     * @return ��?�2 �ł̃p���??[�^��Ԃ�?I���l
     */
    public double end2() {
        return trc2.tParam2();
    }

    /**
     * ���̃I?[�o?[���b�v�̑���̋�?� (��?�2) �ł̃p���??[�^��Ԃ̑?���l��Ԃ�?B
     *
     * @return ��?�2 �ł̃p���??[�^��Ԃ̑?���l
     */
    public double increase2() {
        return end2() - start2();
    }

    /**
     * ���̊�?���_�ł��邩�ۂ���Ԃ�?B
     *
     * @return ��_�ł͂Ȃ��I?[�o?[���b�v�Ȃ̂�?A?�� false
     */
    public boolean isIntersectionPoint() {
        return false;
    }

    /**
     * ���̊�?��I?[�o?[���b�v�ł��邩�ۂ���Ԃ�?B
     *
     * @return �I?[�o?[���b�v�Ȃ̂�?A?�� true
     */
    public boolean isOverlapCurve() {
        return true;
    }

    /**
     * ���̊�?��_�ɕϊ�����?B
     * <p/>
     * �I?[�o?[���b�v���_�ɕϊ����邱�Ƃ͂ł��Ȃ��̂� null ��Ԃ�?B
     * </p>
     *
     * @return ?�� null
     */
    public IntersectionPoint2D toIntersectionPoint() {
        return null;
    }

    /**
     * ���̊�?�I?[�o?[���b�v�ɕϊ�����?B
     * <p/>
     * ������?g��Ԃ�?B
     * </p>
     *
     * @return ������?g
     */
    public OverlapCurve2D toOverlapCurve() {
        return this;
    }

    /**
     * ���̃I?[�o?[���b�v�� trc1 �� trc2 ��귂����I?[�o?[���b�v��Ԃ�?B
     *
     * @return trc1 �� trc2 ��귂����I?[�o?[���b�v
     */
    public OverlapCurve2D exchange() {
        OverlapCurve2D ex = new OverlapCurve2D();
        ex.trc1 = this.trc2;
        ex.trc2 = this.trc1;
        return ex;
    }

    /**
     * ���̊�?̈��̋�?� (��?�1) ?�ł̈ʒu��?A
     * �^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�Ԃ�?B
     *
     * @param sec  ��?�1 �̃p���??[�^���
     * @param conv ��?�1 �̃p���??[�^�l��ϊ�����I�u�W�F�N�g
     * @return ��?�1 ?�̈ʒu��^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�
     */
    public CurveCurveInterference2D trim1(ParameterSection sec,
                                          ParameterConversion2D conv) {
        // sec �� trc1 �̕��?�̃p���??[�^�ł���?B
        // (IntersectionPoint2D �Ɠ����Ӗ��ɂ��邽��)
        // senseAgreement == (tParam1 < tParam2)

        int lowerValidity, upperValidity;
        double lower1, upper1;
        double lower, upper;
        if (trc1.senseAgreement()) {
            lower1 = sec.lower();
            upper1 = sec.upper();
        } else {
            lower1 = sec.upper();
            upper1 = sec.lower();
        }

        lower = trc1.toOwnParameter(lower1);
        upper = trc1.toOwnParameter(upper1);
        lowerValidity = trc1.parameterValidity(lower);
        upperValidity = trc1.parameterValidity(upper);

        // �g������?�� parameterDomain �� [0..v]
        // ������ v = abs(tParam2 - tParam1)
        if (lowerValidity == ParameterValidity.OUTSIDE &&
                lower > 0)
            return null;    // sec is above upper

        if (upperValidity == ParameterValidity.OUTSIDE &&
                upper <= 0)
            return null;    // sec is below lower

        if (lowerValidity == ParameterValidity.TOLERATED_UPPER_LIMIT) {
            // touch at upper, so make IntersectionPoint
            // �g������?�?�� parameter ���?ő�̓_
            // == ���?�?� tParam2 �̓_
            PointOnCurve2D poc2 =
                    new PointOnCurve2D(trc2.basisCurve(), trc2.tParam2(), doCheckDebug);
            return new IntersectionPoint2D(conv.convToPoint(lower1),
                    poc2,
                    GeometryElement.doCheckDebug);
        }

        if (upperValidity == ParameterValidity.TOLERATED_LOWER_LIMIT) {
            // touch at lower, so make IntersectionPoint
            PointOnCurve2D poc1 =
                    new PointOnCurve2D(trc2.basisCurve(), trc2.tParam1(), doCheckDebug);
            return new IntersectionPoint2D(conv.convToPoint(upper1),
                    poc1,
                    GeometryElement.doCheckDebug);
        }

        double lparam1 = 0;
        double uparam1 = Math.abs(trc1.tParam2() - trc1.tParam1());
        double lparam2 = 0;
        double uparam2 = Math.abs(trc2.tParam2() - trc2.tParam1());

        if (upperValidity == ParameterValidity.PROPERLY_INSIDE) {
            Point2D upoint1 =
                    new PointOnCurve2D(trc1.basisCurve(), upper1, doCheckDebug);
            uparam2 = trc2.pointToParameter(upoint1);
            uparam1 = upper;
        }
        if (lowerValidity == ParameterValidity.PROPERLY_INSIDE) {
            Point2D lpoint1 =
                    new PointOnCurve2D(trc1.basisCurve(), lower1, doCheckDebug);
            lparam2 = trc2.pointToParameter(lpoint1);
            lparam1 = lower;
        }

        ParameterSection pint1 =
                trc1.toBasisParameter(new ParameterSection(lparam1,
                        uparam1 - lparam1));
        ParameterSection pint2 =
                trc2.toBasisParameter(new ParameterSection(lparam2,
                        uparam2 - lparam2));

        if (upperValidity != ParameterValidity.PROPERLY_INSIDE &&
                lowerValidity != ParameterValidity.PROPERLY_INSIDE) {
            // both lower and upper are outside, so no trim required
            return new OverlapCurve2D(conv.convToTrimmedCurve(pint1),
                    trc2,
                    false);
        } else {
            TrimmedCurve2D trc =
                    new TrimmedCurve2D(trc2.basisCurve(), pint2);
            return new OverlapCurve2D(conv.convToTrimmedCurve(pint1),
                    trc,
                    false);
        }
    }

    /**
     * ���̊�?̑���̋�?� (��?�2) ?�ł̈ʒu��?A
     * �^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�Ԃ�?B
     *
     * @param sec  ��?�2 �̃p���??[�^���
     * @param conv ��?�2 �̃p���??[�^�l��ϊ�����I�u�W�F�N�g
     * @return ��?�2 ?�̈ʒu��^����ꂽ�ϊ�?��?�ɂ�Bĕϊ�������̂ɒu����������?�
     */
    public CurveCurveInterference2D trim2(ParameterSection sec,
                                          ParameterConversion2D conv) {
        // sec �� trc2 �̕��?�̃p���??[�^�ł���?B
        // (IntersectionPoint2D �Ɠ����Ӗ��ɂ��邽��)
        // senseAgreement == (tParam1 < tParam2)

        int lowerValidity, upperValidity;
        double lower2, upper2;
        double lower, upper;
        if (trc2.senseAgreement()) {
            lower2 = sec.lower();
            upper2 = sec.upper();
        } else {
            lower2 = sec.upper();
            upper2 = sec.lower();
        }

        lower = trc2.toOwnParameter(lower2);
        upper = trc2.toOwnParameter(upper2);
        lowerValidity = trc2.parameterValidity(lower);
        upperValidity = trc2.parameterValidity(upper);

        // �g������?�� parameterDomain �� [0..v]
        // ������ v = abs(tParam2 - tParam1)
        if (lowerValidity == ParameterValidity.OUTSIDE &&
                lower > 0)
            return null;    // sec is above upper

        if (upperValidity == ParameterValidity.OUTSIDE &&
                upper <= 0)
            return null;    // sec is below lower

        if (lowerValidity == ParameterValidity.TOLERATED_UPPER_LIMIT) {
            // touch at upper, so make IntersectionPoint
            // �g������?�?�� parameter ���?ő�̓_
            // == ���?�?� tParam2 �̓_
            PointOnCurve2D poc2 =
                    new PointOnCurve2D(trc1.basisCurve(), trc1.tParam2(), doCheckDebug);
            return new IntersectionPoint2D(poc2,
                    conv.convToPoint(lower2),
                    GeometryElement.doCheckDebug);
        }

        if (upperValidity == ParameterValidity.TOLERATED_LOWER_LIMIT) {
            // touch at lower, so make IntersectionPoint
            PointOnCurve2D poc1 =
                    new PointOnCurve2D(trc1.basisCurve(), trc1.tParam1(), doCheckDebug);
            return new IntersectionPoint2D(poc1,
                    conv.convToPoint(upper2),
                    GeometryElement.doCheckDebug);
        }

        double lparam1 = 0;
        double uparam1 = Math.abs(trc1.tParam2() - trc1.tParam1());
        double lparam2 = 0;
        double uparam2 = Math.abs(trc2.tParam2() - trc2.tParam1());

        if (upperValidity == ParameterValidity.PROPERLY_INSIDE) {
            Point2D upoint2 =
                    new PointOnCurve2D(trc2.basisCurve(), upper2, doCheckDebug);
            uparam2 = upper;
            uparam1 = trc1.pointToParameter(upoint2);
        }
        if (lowerValidity == ParameterValidity.PROPERLY_INSIDE) {
            Point2D lpoint2 =
                    new PointOnCurve2D(trc2.basisCurve(), lower2, doCheckDebug);
            lparam2 = lower;
            lparam1 = trc1.pointToParameter(lpoint2);
        }

        ParameterSection pint1 =
                trc1.toBasisParameter(new ParameterSection(lparam1,
                        uparam1 - lparam1));
        ParameterSection pint2 =
                trc2.toBasisParameter(new ParameterSection(lparam2,
                        uparam2 - lparam2));

        if (upperValidity != ParameterValidity.PROPERLY_INSIDE &&
                lowerValidity != ParameterValidity.PROPERLY_INSIDE) {
            // both lower and upper are outside, so no trim required
            return new OverlapCurve2D(trc1,
                    conv.convToTrimmedCurve(pint2),
                    false);
        } else {
            TrimmedCurve2D trc =
                    new TrimmedCurve2D(trc1.basisCurve(), pint1);
            return new OverlapCurve2D(trc,
                    conv.convToTrimmedCurve(pint2),
                    false);
        }
    }

    /**
     * ���̊�?̈��̋�?� (��?�1) ��^����ꂽ��?�ɒu����������?�Ԃ�?B
     * <p/>
     * �p���??[�^�l�Ȃǂ͂��̂܂�?B
     * </p>
     *
     * @param newCurve ��?�1 ��?ݒ肷���?�
     * @return ��?�1��u����������?�
     */
    public CurveCurveInterference2D changeCurve1(ParametricCurve2D newCurve) {
        TrimmedCurve2D newTrc1 =
                new TrimmedCurve2D(newCurve,
                        this.trc1.tPnt1(), this.trc1.tPnt2(),
                        this.trc1.tParam1(), this.trc1.tParam2(),
                        this.trc1.masterRepresentation1(),
                        this.trc1.masterRepresentation2(),
                        this.trc1.senseAgreement());
        return new OverlapCurve2D(newTrc1, this.trc2, false);
    }

    /**
     * ���̊�?̑���̋�?� (��?�2) ��^����ꂽ��?�ɒu����������?�Ԃ�?B
     * <p/>
     * �p���??[�^�l�Ȃǂ͂��̂܂�?B
     * </p>
     *
     * @param newCurve ��?�2 ��?ݒ肷���?�
     * @return ��?�2 ��u����������?�
     */
    public CurveCurveInterference2D changeCurve2(ParametricCurve2D newCurve) {
        TrimmedCurve2D newTrc2 =
                new TrimmedCurve2D(newCurve,
                        this.trc2.tPnt1(), this.trc2.tPnt2(),
                        this.trc2.tParam1(), this.trc2.tParam2(),
                        this.trc2.masterRepresentation1(),
                        this.trc2.masterRepresentation2(),
                        this.trc2.senseAgreement());
        return new OverlapCurve2D(this.trc1, newTrc2, false);
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return trc1, trc2 ���Ƃ�Ɏ��R�`?�ł���Ȃ�� true?A����Ȃ��� false
     */
    public boolean isFreeform() {
        return (this.trc1.isFreeform() && this.trc2.isFreeform());
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
        writer.println(indent_tab + "\ttrc1");
        trc1.output(writer, indent + 2);
        writer.println(indent_tab + "\ttrc2");
        trc2.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
