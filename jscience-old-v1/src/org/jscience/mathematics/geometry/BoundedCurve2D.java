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
 * �Q���� : �L�ȋ�?��\����?ۃN���X?B
 * <p/>
 * ���̃N���X�ɂ̓C���X�^���X���?�ׂ���?��͂Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:52 $
 */

public abstract class BoundedCurve2D extends ParametricCurve2D {
    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    protected BoundedCurve2D() {
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
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_�� PointOnCurve2D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?g�̃p���??[�^��`�� section ��^����
     * this.{@link ParametricCurve2D#toPolyline(ParameterSection,ToleranceForDistance)
     * toPolyline}(section, tolerance)
     * ��Ă�?o��?B
     * </p>
     *
     * @param tolerance �����̋��e��?�
     * @return ���̋�?�S�̂�?�ߎ�����|�����C��
     * @see PointOnCurve2D
     */
    public Polyline2D toPolyline(ToleranceForDistance tolerance) {
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
     * this.{@link ParametricCurve2D#toBsplineCurve(ParameterSection)
     * toBsplineCurve}(section)
     * ��Ă�?o��?B
     * </p>
     *
     * @return ���̋�?�S�̂�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve2D toBsplineCurve() {
        try {
            return toBsplineCurve(parameterDomain().section());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
    }

    /**
     * ���̗L��?�S�̂�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ����� Bspline ��?��?�߂�?B
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?g�̃p���??[�^��`�� section ��^����
     * this.{@link ParametricCurve2D#offsetByBsplineCurve(ParameterSection,double,int,ToleranceForDistance)
     * offsetByBsplineCurve}(section, magni, side, tol)
     * ��Ă�?o��?B
     * </p>
     *
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�S�̂̃I�t�Z�b�g��?��ߎ����� Bspline ��?�
     * @see WhichSide
     */
    public BsplineCurve2D
    offsetByBsplineCurve(double magni,
                         int side,
                         ToleranceForDistance tol) {
        try {
            return offsetByBsplineCurve(parameterDomain().section(),
                    magni, side, tol);
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
    }

    /**
     * ���̗L��?�S�̂�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ�����L��?��?�߂�?B
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?g�̃p���??[�^��`�� section ��^����
     * this.{@link ParametricCurve2D#offsetByBoundedCurve(ParameterSection,double,int,ToleranceForDistance)
     * offsetByBoundedCurve}(section, magni, side, tol)
     * ��Ă�?o��?B
     * </p>
     *
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�S�̂̃I�t�Z�b�g��?��ߎ�����L��?�
     * @see WhichSide
     */
    public BoundedCurve2D
    offsetByBoundedCurve(double magni,
                         int side,
                         ToleranceForDistance tol) {
        try {
            return offsetByBoundedCurve(parameterDomain().section(),
                    magni, side, tol);
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
    }

    /**
     * ���̗L��?�S�̂Ƒ��̗L��?�S�̂ɂ�����t�B���b�g��?�߂�?B
     * <p/>
     * �t�B���b�g����?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?g�̃p���??[�^��`�� thisSection
     * ��
     * ���̗L��?�̃p���??[�^��`�� mateSection
     * ��^����
     * this.{@link ParametricCurve2D#fillet(ParameterSection,int,ParametricCurve2D,ParameterSection,int,double)
     * fillet}(thisSection, side1, mate, mateSection, side2, radius)
     * ��Ă�?o��?B
     * </p>
     *
     * @param side1  ���̋�?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param mate   ���̗L��?�
     * @param side2  ���̗L��?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param radius �t�B���b�g���a
     * @return �t�B���b�g�̔z��
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     * @see WhichSide
     */
    public FilletObject2D[]
    fillet(int side1, BoundedCurve2D mate, int side2, double radius)
            throws IndefiniteSolutionException {
        return fillet(parameterDomain().section(), side1, mate,
                mate.parameterDomain().section(), side2, radius);
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
    abstract public CurveCurveInterference2D[] interfere(BoundedCurve2D mate);

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
    abstract CurveCurveInterference2D[] interfere(BoundedLine2D mate,
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
    abstract CurveCurveInterference2D[] interfere(Polyline2D mate,
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
    abstract CurveCurveInterference2D[] interfere(PureBezierCurve2D mate,
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
    abstract CurveCurveInterference2D[] interfere(BsplineCurve2D mate,
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
    abstract CurveCurveInterference2D[] interfere(TrimmedCurve2D mate,
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
    abstract CurveCurveInterference2D[] interfere(CompositeCurveSegment2D mate,
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
    abstract CurveCurveInterference2D[] interfere(CompositeCurve2D mate,
                                                  boolean doExchange);

    /**
     * ���̗L��?�̊J�n�_��Ԃ�?B
     * <p/>
     * ��?�����`����?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return �J�n�_
     */
    public Point2D startPoint() {
        if (isPeriodic())
            return null;

        try {
            return coordinates(parameterDomain().section().start());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();    // should not be occured
        }
    }

    /**
     * ���̗L��?��?I���_��Ԃ�
     * <p/>
     * ��?�����`����?�?��� null ��Ԃ�?B
     * </p>
     *
     * @return ?I���_
     */
    public Point2D endPoint() {
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
     * @see Point2D#identical(Point2D)
     */
    boolean getClosedFlag() {
        if (isPeriodic()) {
            return true;
        } else {
            return startPoint().identical(endPoint());
        }
    }
}

