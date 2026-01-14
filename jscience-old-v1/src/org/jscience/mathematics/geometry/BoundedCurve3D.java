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

/**
 * �R���� : �L�ȋ�?��\����?ۃN���X?B
 * <p/>
 * ���̃N���X�ɂ̓C���X�^���X���?�ׂ���?��͂Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:52 $
 */

public abstract class BoundedCurve3D extends ParametricCurve3D {
    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    protected BoundedCurve3D() {
        super();
    }

    /**
     * ���̗L��?�S�̂̎��?�ł̒��� (���̂�) ��Ԃ�?B
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?g�̃p���??[�^��`�� section ��^����
     * this.{@link AbstractParametricCurve#length(ParameterSection)
     * length}(section)
     * ��Ă�?o��?B
     * </p>
     *
     * @return ��?�S�̂̒���
     */
    public double length() {
        try {
            return length(parameterDomain().section());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
    }

    /**
     * ���̗L��?�S�̂�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_�� PointOnCurve3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?g�̃p���??[�^��`�� section ��^����
     * this.{@link ParametricCurve3D#toPolyline(ParameterSection,ToleranceForDistance)
     * toPolyline}(section, tolerance)
     * ��Ă�?o��?B
     * </p>
     *
     * @param tolerance �����̋��e��?�
     * @return ���̋�?�S�̂�?�ߎ�����|�����C��
     * @see PointOnCurve3D
     */
    public Polyline3D toPolyline(ToleranceForDistance tolerance) {
        try {
            return toPolyline(parameterDomain().section(), tolerance);
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
    }

    /**
     * ���̗L��?�S�̂쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?g�̃p���??[�^��`�� section ��^����
     * this.{@link ParametricCurve3D#toBsplineCurve(ParameterSection)
     * toBsplineCurve}(section)
     * ��Ă�?o��?B
     * </p>
     *
     * @return ���̋�?�S�̂�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve3D toBsplineCurve() {
        try {
            return toBsplineCurve(parameterDomain().section());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
    }

    /**
     * ���̗L��?�Ƒ��̗L��?�̊�?�?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̗L��?�
     * @return ���?�̊�?̔z��
     */
    abstract public CurveCurveInterference3D[] interfere(BoundedCurve3D mate);

    /**
     * ���̗L��?�Ƒ��̗L��?� (?�) �̊�?�?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    abstract CurveCurveInterference3D[] interfere(BoundedLine3D mate,
                                                  boolean doExchange);

    /**
     * ���̗L��?�Ƒ��̗L��?� (�|�����C��) �̊�?�?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�|�����C��)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    abstract CurveCurveInterference3D[] interfere(Polyline3D mate,
                                                  boolean doExchange);

    /**
     * ���̗L��?�Ƒ��̗L��?� (�x�W�G��?�) �̊�?�?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�x�W�G��?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    abstract CurveCurveInterference3D[] interfere(PureBezierCurve3D mate,
                                                  boolean doExchange);

    /**
     * ���̗L��?�Ƒ��̗L��?� (�a�X�v���C����?�) �̊�?�?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�a�X�v���C����?�)
     * @param doExchange ��?�� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    abstract CurveCurveInterference3D[] interfere(BsplineCurve3D mate,
                                                  boolean doExchange);

    /**
     * ���̗L��?�Ƒ��̗L��?� (�g������?�) �̊�?�?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�g������?�)
     * @param doExchange ��?�� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    abstract CurveCurveInterference3D[] interfere(TrimmedCurve3D mate,
                                                  boolean doExchange);

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�Z�O�?���g) �̊�?�?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�Z�O�?���g)
     * @param doExchange ��?�� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    abstract CurveCurveInterference3D[] interfere(CompositeCurveSegment3D mate,
                                                  boolean doExchange);

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�) �̊�?�?�߂钊?ۃ?�\�b�h?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�)
     * @param doExchange ��?�� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    abstract CurveCurveInterference3D[] interfere(CompositeCurve3D mate,
                                                  boolean doExchange);

    /**
     * ���̗L��?�̊J�n�_��Ԃ�?B
     * <p/>
     * ��?�����`����?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return �J�n�_
     */
    public Point3D startPoint() {
        if (isPeriodic())
            return null;

        try {
            return coordinates(parameterDomain().section().start());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();    // should not be occured
        }
    }

    /**
     * ���̗L��?��?I���_��Ԃ�?B
     * <p/>
     * ��?�����`����?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return ?I���_
     */
    public Point3D endPoint() {
        if (isPeriodic())
            return null;

        try {
            return coordinates(parameterDomain().section().end());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();    // should not be occured
        }
    }

    /**
     * ���̗L��?�􉽓I�ɕ��Ă��邩�ۂ���Ԃ�?B
     * <p/>
     * ��?�����`���ł���� true ��Ԃ�?B
     * </p>
     * <p/>
     * ��?�J�����`���ł����?A�J�n�_��?I���_�̓���?��𒲂ׂ�?B
     * </p>
     *
     * @return �􉽓I�ɕ��Ă���� true?A�����łȂ���� false
     * @see Point3D#identical(Point3D)
     */
    boolean getClosedFlag() {
        if (isPeriodic()) {
            return true;
        } else {
            return startPoint().identical(endPoint());
        }
    }
}

