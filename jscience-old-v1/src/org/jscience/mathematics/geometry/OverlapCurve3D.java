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

import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �R���� : ��􉽗v�f�� (��?�����) �I?[�o?[���b�v���Ă����Ԃ�\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��􉽗v�f�̃I?[�o?[���b�v��
 * ���̊􉽗v�f�ɂ�����p���??[�^�͈͂�\���`?�v�f geom1
 * ��
 * ����̊􉽗v�f�ɂ�����p���??[�^�͈͂�\���`?�v�f geom2
 * ��ێ?����?B
 * </p>
 * <p/>
 * �Ȃ�?A
 * geom1 �� geom2 ����?ۂɃI?[�o?[���b�v���邩�ǂ�����?A
 * ���̃N���X�̓Ք�ł͊֒m���Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:15 $
 */

public class OverlapCurve3D extends NonParametricCurve3D implements CurveCurveInterference3D {
    /**
     * �I?[�o?[���b�v��Ԃ̌`?�v�f1 ?�ł̈ʒu��\���`?�v�f
     * <p/>
     * �`?�v�f1 ����?�Ȃ�΃g������?�?A�ȖʂȂ�Ζ�?�?�
     * </p>
     *
     * @serial
     */
    private GeometryElement geom1;

    /**
     * �I?[�o?[���b�v��Ԃ̌`?�v�f2 ?�ł̈ʒu��\���`?�v�f
     * <p/>
     * �`?�v�f2 ����?�Ȃ�΃g������?�?A�ȖʂȂ�Ζ�?�?�
     * </p>
     *
     * @serial
     */
    private GeometryElement geom2;

    /**
     * �􉽗v�f��I?[�o?[���b�v��Ԃ̎w�肹���ɃI�u�W�F�N�g��?\�z����?B
     */
    private OverlapCurve3D() {
        geom1 = null;
        geom2 = null;
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
    OverlapCurve3D(TrimmedCurve3D trc1,
                   TrimmedCurve3D trc2,
                   boolean doCheck) {
        super();
        this.geom1 = trc1;
        this.geom2 = trc2;
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
    OverlapCurve3D(ParametricCurve3D curve1,
                   ParameterSection section1,
                   ParametricCurve3D curve2,
                   ParameterSection section2,
                   boolean doCheck) {
        super();
        this.geom1 = new TrimmedCurve3D(curve1, section1);
        this.geom2 = new TrimmedCurve3D(curve2, section2);
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
    OverlapCurve3D(ParametricCurve3D curve1,
                   double start1, double inc1,
                   ParametricCurve3D curve2,
                   double start2, double inc2,
                   boolean doCheck) {
        super();
        this.geom1 = new TrimmedCurve3D(curve1, new ParameterSection(start1, inc1));
        this.geom2 = new TrimmedCurve3D(curve2, new ParameterSection(start2, inc2));
    }

    /**
     * ���̃I?[�o?[���b�v�̈��̊􉽗v�f (�􉽗v�f1) ����?� (��?�1) �ł���Ƃ���?A
     * ���̋�?��Ԃ�?B
     * <p/>
     * �`?�v�f1 ����?�łȂ�?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return �`?�v�f1 (��?�1)
     */
    public ParametricCurve3D curve1() {
        if (!geom1.isCurve())
            return null;
        TrimmedCurve3D trc1 = (TrimmedCurve3D) geom1;
        return trc1.basisCurve();
    }

    /**
     * ���̃I?[�o?[���b�v�̈��̊􉽗v�f (�􉽗v�f1) ����?� (��?�1) �ł���Ƃ���?A
     * ���̋�?�1 �ł̃p���??[�^��Ԃ̊J�n�l��Ԃ�?B
     * <p/>
     * �`?�v�f1 ����?�łȂ�?�?���
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return ��?�1 �ł̃p���??[�^��Ԃ̊J�n�l
     * @see FatalException
     */
    public double start1() {
        if (!geom1.isCurve())
            throw new FatalException();
        TrimmedCurve3D trc1 = (TrimmedCurve3D) geom1;
        return trc1.tParam1();
    }

    /**
     * ���̃I?[�o?[���b�v�̈��̊􉽗v�f (�􉽗v�f1) ����?� (��?�1) �ł���Ƃ���?A
     * ���̋�?�1 �ł̃p���??[�^��Ԃ�?I���l��Ԃ�?B
     * <p/>
     * �`?�v�f1 ����?�łȂ�?�?���
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return ��?�1 �ł̃p���??[�^��Ԃ�?I���l
     * @see FatalException
     */
    public double end1() {
        if (!geom1.isCurve())
            throw new FatalException();
        TrimmedCurve3D trc1 = (TrimmedCurve3D) geom1;
        return trc1.tParam2();
    }

    /**
     * ���̃I?[�o?[���b�v�̈��̊􉽗v�f (�􉽗v�f1) ����?� (��?�1) �ł���Ƃ���?A
     * ���̋�?�1 �ł̃p���??[�^��Ԃ̑?���l��Ԃ�?B
     * <p/>
     * �`?�v�f1 ����?�łȂ�?�?���
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return ��?�1 �ł̃p���??[�^��Ԃ̑?���l
     * @see FatalException
     */
    public double increase1() {
        if (!geom1.isCurve())
            throw new FatalException();
        TrimmedCurve3D trc1 = (TrimmedCurve3D) geom1;
        return end1() - start1();
    }

    /**
     * ���̃I?[�o?[���b�v�̑���̊􉽗v�f (�􉽗v�f2) ����?� (��?�2) �ł���Ƃ���?A
     * ���̋�?��Ԃ�?B
     * <p/>
     * �`?�v�f2 ����?�łȂ�?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return �`?�v�f2 (��?�2)
     */
    public ParametricCurve3D curve2() {
        if (!geom2.isCurve())
            return null;
        TrimmedCurve3D trc2 = (TrimmedCurve3D) geom2;
        return trc2.basisCurve();
    }

    /**
     * ���̃I?[�o?[���b�v�̑���̊􉽗v�f (�􉽗v�f2) ����?� (��?�2) �ł���Ƃ���?A
     * ���̋�?�2 �ł̃p���??[�^��Ԃ̊J�n�l��Ԃ�?B
     * <p/>
     * �`?�v�f2 ����?�łȂ�?�?���
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return ��?�2 �ł̃p���??[�^��Ԃ̊J�n�l
     * @see FatalException
     */
    public double start2() {
        if (!geom2.isCurve())
            throw new FatalException();
        TrimmedCurve3D trc2 = (TrimmedCurve3D) geom2;
        return trc2.tParam1();
    }

    /**
     * ���̃I?[�o?[���b�v�̑���̊􉽗v�f (�􉽗v�f2) ����?� (��?�2) �ł���Ƃ���?A
     * ���̋�?�2 �ł̃p���??[�^��Ԃ�?I���l��Ԃ�?B
     * <p/>
     * �`?�v�f2 ����?�łȂ�?�?���
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return ��?�2 �ł̃p���??[�^��Ԃ�?I���l
     * @see FatalException
     */
    public double end2() {
        if (!geom2.isCurve())
            throw new FatalException();
        TrimmedCurve3D trc2 = (TrimmedCurve3D) geom2;
        return trc2.tParam2();
    }

    /**
     * ���̃I?[�o?[���b�v�̑���̊􉽗v�f (�􉽗v�f2) ����?� (��?�2) �ł���Ƃ���?A
     * ���̋�?�2 �ł̃p���??[�^��Ԃ̑?���l��Ԃ�?B
     * <p/>
     * �`?�v�f2 ����?�łȂ�?�?���
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return ��?�2 �ł̃p���??[�^��Ԃ̑?���l
     * @see FatalException
     */
    public double increase2() {
        if (!geom2.isCurve())
            throw new FatalException();
        TrimmedCurve3D trc2 = (TrimmedCurve3D) geom2;
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
    public IntersectionPoint3D toIntersectionPoint() {
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
    public OverlapCurve3D toOverlapCurve() {
        return this;
    }

    /**
     * ���̃I?[�o?[���b�v�� geom1 �� geom2 ��귂����I?[�o?[���b�v��Ԃ�?B
     *
     * @return geom1 �� geom2 ��귂����I?[�o?[���b�v
     */
    public OverlapCurve3D exchange() {
        OverlapCurve3D ex = new OverlapCurve3D();
        ex.geom1 = this.geom2;
        ex.geom2 = this.geom1;
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
    public CurveCurveInterference3D trim1(ParameterSection sec,
                                          ParameterConversion3D conv) {
        // sec �� trc1 �̕��?�̃p���??[�^�ł���?B
        // (IntersectionPoint3D �Ɠ����Ӗ��ɂ��邽��)
        // senseAgreement == (tParam1 < tParam2)

        TrimmedCurve3D trc1 = (TrimmedCurve3D) geom1;
        TrimmedCurve3D trc2 = (TrimmedCurve3D) geom2;
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
            PointOnCurve3D poc2 =
                    new PointOnCurve3D(trc2.basisCurve(), trc2.tParam2(), doCheckDebug);
            return new IntersectionPoint3D(conv.convToPoint(lower1),
                    poc2,
                    GeometryElement.doCheckDebug);
        }

        if (upperValidity == ParameterValidity.TOLERATED_LOWER_LIMIT) {
            // touch at lower, so make IntersectionPoint
            PointOnCurve3D poc1 =
                    new PointOnCurve3D(trc2.basisCurve(), trc2.tParam1(), doCheckDebug);
            return new IntersectionPoint3D(conv.convToPoint(upper1),
                    poc1,
                    GeometryElement.doCheckDebug);
        }

        double lparam1 = 0;
        double uparam1 = Math.abs(trc1.tParam2() - trc1.tParam1());
        double lparam2 = 0;
        double uparam2 = Math.abs(trc2.tParam2() - trc2.tParam1());

        if (upperValidity == ParameterValidity.PROPERLY_INSIDE) {
            Point3D upoint1 =
                    new PointOnCurve3D(trc1.basisCurve(), upper1, doCheckDebug);
            uparam2 = trc2.pointToParameter(upoint1);
            uparam1 = upper;
        }
        if (lowerValidity == ParameterValidity.PROPERLY_INSIDE) {
            Point3D lpoint1 =
                    new PointOnCurve3D(trc1.basisCurve(), lower1, doCheckDebug);
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
            return new OverlapCurve3D(conv.convToTrimmedCurve(pint1),
                    trc2,
                    false);
        } else {
            TrimmedCurve3D trc =
                    new TrimmedCurve3D(trc2.basisCurve(), pint2);
            return new OverlapCurve3D(conv.convToTrimmedCurve(pint1),
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
    public CurveCurveInterference3D trim2(ParameterSection sec,
                                          ParameterConversion3D conv) {
        // sec �� trc2 �̕��?�̃p���??[�^�ł���?B
        // (IntersectionPoint3D �Ɠ����Ӗ��ɂ��邽��)
        // senseAgreement == (tParam1 < tParam2)

        TrimmedCurve3D trc1 = (TrimmedCurve3D) geom1;
        TrimmedCurve3D trc2 = (TrimmedCurve3D) geom2;
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
            PointOnCurve3D poc2 =
                    new PointOnCurve3D(trc1.basisCurve(), trc1.tParam2(), doCheckDebug);
            return new IntersectionPoint3D(poc2,
                    conv.convToPoint(lower2),
                    GeometryElement.doCheckDebug);
        }

        if (upperValidity == ParameterValidity.TOLERATED_LOWER_LIMIT) {
            // touch at lower, so make IntersectionPoint
            PointOnCurve3D poc1 =
                    new PointOnCurve3D(trc1.basisCurve(), trc1.tParam1(), doCheckDebug);
            return new IntersectionPoint3D(poc1,
                    conv.convToPoint(upper2),
                    GeometryElement.doCheckDebug);
        }

        double lparam1 = 0;
        double uparam1 = Math.abs(trc1.tParam2() - trc1.tParam1());
        double lparam2 = 0;
        double uparam2 = Math.abs(trc2.tParam2() - trc2.tParam1());

        if (upperValidity == ParameterValidity.PROPERLY_INSIDE) {
            Point3D upoint2 =
                    new PointOnCurve3D(trc2.basisCurve(), upper2, doCheckDebug);
            uparam2 = upper;
            uparam1 = trc1.pointToParameter(upoint2);
        }
        if (lowerValidity == ParameterValidity.PROPERLY_INSIDE) {
            Point3D lpoint2 =
                    new PointOnCurve3D(trc2.basisCurve(), lower2, doCheckDebug);
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
            return new OverlapCurve3D(trc1,
                    conv.convToTrimmedCurve(pint2),
                    false);
        } else {
            TrimmedCurve3D trc =
                    new TrimmedCurve3D(trc1.basisCurve(), pint1);
            return new OverlapCurve3D(trc,
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
    public CurveCurveInterference3D changeCurve1(ParametricCurve3D newCurve) {
        if (!this.geom1.isCurve())
            throw new FatalException();
        TrimmedCurve3D trc1 = (TrimmedCurve3D) this.geom1;
        TrimmedCurve3D newTrc1 =
                new TrimmedCurve3D(newCurve,
                        trc1.tPnt1(), trc1.tPnt2(),
                        trc1.tParam1(), trc1.tParam2(),
                        trc1.masterRepresentation1(),
                        trc1.masterRepresentation2(),
                        trc1.senseAgreement());
        return new OverlapCurve3D(newTrc1, (TrimmedCurve3D) this.geom2, false);
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
    public CurveCurveInterference3D changeCurve2(ParametricCurve3D newCurve) {
        if (!this.geom2.isCurve())
            throw new FatalException();
        TrimmedCurve3D trc2 = (TrimmedCurve3D) this.geom2;
        TrimmedCurve3D newTrc2 =
                new TrimmedCurve3D(newCurve,
                        trc2.tPnt1(), trc2.tPnt2(),
                        trc2.tParam1(), trc2.tParam2(),
                        trc2.masterRepresentation1(),
                        trc2.masterRepresentation2(),
                        trc2.senseAgreement());
        return new OverlapCurve3D((TrimmedCurve3D) this.geom1, newTrc2, false);
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return geom1, geom2 ���Ƃ�Ɏ��R�`?�ł���Ȃ�� true?A����Ȃ��� false
     */
    public boolean isFreeform() {
        return (this.geom1.isFreeform() && this.geom2.isFreeform());
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
        geom1.output(writer, indent + 2);
        writer.println(indent_tab + "\ttrc2");
        geom2.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
