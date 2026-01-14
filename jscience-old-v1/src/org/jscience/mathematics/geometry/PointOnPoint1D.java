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

import java.io.PrintWriter;

/**
 * �P���� : ����_��?�ɂ���_��\���N���X
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �_��?�BĂ���_ ({@link Point1D Point1D})
 * basisPoint ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:07 $
 */

public class PointOnPoint1D extends PointOnGeometry1D {
    /**
     * �_��?�BĂ���_?B
     *
     * @serial
     */
    private Point1D basisPoint;

    /**
     * �_��?�BĂ���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry1D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisPoint �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisPoint ����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param basisPoint �_��?�BĂ���_
     * @param doCheck    ��?��̒l�̑Ó�?���`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     * @see Point1D#identical(Point1D)
     */
    PointOnPoint1D(Point1D basisPoint, boolean doCheck) {
        this(null, basisPoint, doCheck);
    }

    /**
     * �_��?W�l�����
     * �_��?�BĂ���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * doCheck �� true ��?�?��ɂ�?A��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisPoint �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisPoint ����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param point      �_?�_��?W�l
     * @param basisPoint �_��?�BĂ���_
     * @param doCheck    ��?��̒l�̑Ó�?���`�F�b�N�ⷂ邩�ǂ����̃t���O
     * @see InvalidArgumentValueException
     * @see Point1D#identical(Point1D)
     */
    PointOnPoint1D(Point1D point,
                   Point1D basisPoint,
                   boolean doCheck) {
        super(point);

        if (doCheck == true) {
            if (basisPoint == null) {
                throw new InvalidArgumentValueException();
            }
            if (point != null) {
                if (!point.identical(basisPoint)) {
                    throw new InvalidArgumentValueException();
                }
            }
        }
        this.basisPoint = basisPoint;
    }

    /**
     * �_��?�BĂ���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * PointOnGeometry1D �ɂ����� point �� null ��?ݒ肳���?B
     * </p>
     * <p/>
     * ��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisPoint �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisPoint ����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param basisPoint �_��?�BĂ���_
     * @see InvalidArgumentValueException
     * @see Point1D#identical(Point1D)
     */
    public PointOnPoint1D(Point1D basisPoint) {
        this(null, basisPoint);
    }

    /**
     * �_��?W�l�����
     * �_��?�BĂ���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     * <p/>
     * ��?��̒l�ɑ΂��Ĉȉ��̌�?���?s�Ȃ�?B
     * <ul>
     * <li>	basisPoint �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * <li>	point �� null �łȂ�?A
     * point �� basisPoint ����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </ul>
     * </p>
     *
     * @param point      �_?�_��?W�l
     * @param basisPoint �_��?�BĂ���_
     * @see InvalidArgumentValueException
     * @see Point1D#identical(Point1D)
     */
    public PointOnPoint1D(Point1D point, Point1D basisPoint) {
        this(point, basisPoint, true);
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����`?�v�f��
     * Point1D �̃C���X�^���X�ł���?B
     * </p>
     *
     * @return �x?[�X�ƂȂ�`?�v�f
     * @see #basisPoint()
     */
    public GeometryElement geometry() {
        return basisPoint();
    }

    /**
     * �x?[�X�ƂȂ�_��Ԃ�?B
     *
     * @return �x?[�X�ƂȂ�_
     * @see #geometry()
     */
    public Point1D basisPoint() {
        return basisPoint;
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�_��?W�l��?�߂�?B
     *
     * @return �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�?�߂��_��?W�l
     * @see #basisPoint()
     */
    Point1D coordinates() {
        return basisPoint();
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
        basisPoint.output(writer, indent + 2);
        writer.println("End");
    }
}
