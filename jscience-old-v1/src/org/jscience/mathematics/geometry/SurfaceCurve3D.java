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

import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.util.FatalException;

import java.io.PrintWriter;

/**
 * �R���� : �Ȗ�?�̋�?� (��?�?�) ��\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X�͈ȉ��̑�?���ێ?����?B
 * <ul>
 * <li> ��?�?�̂R�����\�� curve3d
 * <li> ��?�?�BĂ���Ȗ� basisSurface1
 * <li> ��?�?�� basisSurface1 �̃p���??[�^��Ԃł̂Q�����\�� curve2d1
 * <li> �I�v�V���i�� : ��?�?�BĂ���¤���̋Ȗ� basisSurface2
 * <li> �I�v�V���i�� : ��?�?�� basisSurface2 �̃p���??[�^��Ԃł̂Q�����\�� curve2d2
 * <li>	curve3d, curve2d1, curve2d2 �̂������D?悷�邩��?�?� masterRepresentation
 * ({@link PreferredSurfaceCurveRepresentation PreferredSurfaceCurveRepresentation})
 * </ul>
 * </p>
 * <p/>
 * masterRepresentation �̒l�͈ȉ��̂����ꂩ�łȂ���΂Ȃ�Ȃ�?B
 * <ul>
 * <li>	PreferredSurfaceCurveRepresentation.CURVE_3D
 * <li>	PreferredSurfaceCurveRepresentation.CURVE_2D_1
 * <li>	PreferredSurfaceCurveRepresentation.CURVE_2D_2
 * </ul>
 * </p>
 * <p/>
 * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ����
 * curve3d �� null �ł�?\��Ȃ�?B
 * </p>
 * <p/>
 * basisSurface1 �� null �ł��BĂ͂Ȃ�Ȃ�?B
 * </p>
 * <p/>
 * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_2D_1 �łȂ����
 * curve2d1 �� null �ł�?\��Ȃ�?B
 * </p>
 * <p/>
 * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_2D_2 �łȂ����
 * basisSurface2 �� null �ł�?\��Ȃ�?B
 * </p>
 * <p/>
 * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_2D_2 �łȂ����
 * curve2d2 �� null �ł�?\��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:19 $
 */

public class SurfaceCurve3D extends ParametricCurve3D {

    /**
     * ��?�?�̂R�����\��?B
     * <p/>
     * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ����
     * curve3d �� null �ł�?\��Ȃ�?B
     * </p>
     *
     * @serial
     */
    private ParametricCurve3D curve3d;

    /**
     * ��?�?�BĂ���Ȗ�?B
     * <p/>
     * basisSurface1 �� null �ł��BĂ͂Ȃ�Ȃ�?B
     * </p>
     *
     * @serial
     */
    private ParametricSurface3D basisSurface1;

    /**
     * ��?�?�� basisSurface1 �̃p���??[�^��Ԃł̂Q�����\��?B
     * <p/>
     * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_2D_1 �łȂ����
     * curve2d1 �� null �ł�?\��Ȃ�?B
     * </p>
     *
     * @serial
     */
    private ParametricCurve2D curve2d1;

    /**
     * ��?�?�BĂ���¤���̋Ȗ�?B
     * <p/>
     * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_2D_2 �łȂ����
     * basisSurface2 �� null �ł�?\��Ȃ�?B
     * </p>
     *
     * @serial
     */
    private ParametricSurface3D basisSurface2;

    /**
     * ��?�?�� basisSurface2 �̃p���??[�^��Ԃł̂Q�����\��?B
     * <p/>
     * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_2D_2 �łȂ����
     * curve2d2 �� null �ł�?\��Ȃ�?B
     * </p>
     *
     * @serial
     */
    private ParametricCurve2D curve2d2;

    /**
     * curve3d, curve2d1, curve2d2 �̂������D?悷�邩��?�?�?B
     * <p/>
     * masterRepresentation �̒l�͈ȉ��̂����ꂩ�łȂ���΂Ȃ�Ȃ�?B
     * <ul>
     * <li>	PreferredSurfaceCurveRepresentation.CURVE_3D
     * <li>	PreferredSurfaceCurveRepresentation.CURVE_2D_1
     * <li>	PreferredSurfaceCurveRepresentation.CURVE_2D_2
     * </ul>
     * </p>
     *
     * @serial
     * @see PreferredSurfaceCurveRepresentation
     */
    private int masterRepresentation;

    /**
     * ��?�?�̂R�����\���ƈ�̋Ȗʂɑ΂���Q�����\����^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?\�z����C���X�^���X�̊e�t�B?[���h�ɑ���l�͈ȉ��̒ʂ�?B
     * <pre>
     * 		this.curve3d = curve3d;
     * 		this.basisSurface1 = basisSurface;
     * 		this.curve2d1 = curve2d;
     * 		this.basisSurface2 = null;
     * 		this.curve2d2 = null;
     * 		this.masterRepresentation = masterRepresentation;
     * </pre>
     * </p>
     * <p/>
     * curve3d, curve2d �̗��҂̒l��������� null ��?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * </p>
     * <p/>
     * basisSurface �� null �ł��BĂ͂����Ȃ�?B
     * basisSurface �� null ��?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * </p>
     * <p/>
     * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_3D �ł���Ƃ��ɂ�
     * curve3d �� null �ł��BĂ͂����Ȃ�?B
     * ����?�?����������Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_2D_1 �ł���Ƃ��ɂ�
     * curve2d �� null �ł��BĂ͂����Ȃ�?B
     * ����?�?����������Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * masterRepresentation �̒l��?�L�̂�����ł�Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param curve3d              �R�����\��
     * @param basisSurface         ��?�?�BĂ���Ȗ�
     * @param curve2d              basisSurface �̃p���??[�^��Ԃł̂Q�����\��
     * @param masterRepresentation curve3d �� curve2d �̂ǂ����D?悷�邩����?�
     * @see PreferredSurfaceCurveRepresentation
     * @see NullArgumentException
     * @see InvalidArgumentValueException
     */
    public SurfaceCurve3D(ParametricCurve3D curve3d,
                          ParametricSurface3D basisSurface,
                          ParametricCurve2D curve2d,
                          int masterRepresentation) {
        super();
        if (curve3d == null && curve2d == null)
            throw new NullArgumentException();

        if (basisSurface == null)
            throw new NullArgumentException();

        switch (masterRepresentation) {
            case PreferredSurfaceCurveRepresentation.CURVE_3D:
                if (curve3d == null)
                    throw new InvalidArgumentValueException();
                break;
            case PreferredSurfaceCurveRepresentation.CURVE_2D_1:
                if (curve2d == null)
                    throw new InvalidArgumentValueException();
                break;
            default:
                throw new InvalidArgumentValueException();
        }
        this.curve3d = curve3d;
        this.basisSurface1 = basisSurface;
        this.curve2d1 = curve2d;
        this.basisSurface2 = null;
        this.curve2d2 = null;
        this.masterRepresentation = masterRepresentation;
    }

    /**
     * ��?�?�̂R�����\���Ɠ�̋Ȗʂɑ΂���Q�����\����^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?\�z����C���X�^���X�̊e�t�B?[���h�ɑ���l�͈ȉ��̒ʂ�?B
     * <pre>
     * 		this.curve3d = curve3d;
     * 		this.basisSurface1 = basisSurface1;
     * 		this.curve2d1 = curve2d1;
     * 		this.basisSurface2 = basisSurface2;
     * 		this.curve2d2 = curve2d2;
     * 		this.masterRepresentation = masterRepresentation;
     * </pre>
     * </p>
     * <p/>
     * curve3d, curve2d, curve3d �̎O�҂̒l��������� null ��?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * </p>
     * <p/>
     * basisSurface1, basisSurface2 �̂����ꂩ�� null ��?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * </p>
     * <p/>
     * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_3D �ł���Ƃ��ɂ�
     * curve3d �� null �ł��BĂ͂����Ȃ�?B
     * ����?�?����������Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_2D_1 �ł���Ƃ��ɂ�
     * curve2d1 �� null �ł��BĂ͂����Ȃ�?B
     * ����?�?����������Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * masterRepresentation �̒l�� PreferredSurfaceCurveRepresentation.CURVE_2D_2 �ł���Ƃ��ɂ�
     * curve2d2 �� null �ł��BĂ͂����Ȃ�?B
     * ����?�?����������Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * masterRepresentation �̒l��?�L�̂�����ł�Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param curve3d              �R�����\��
     * @param basisSurface1        ��?�?�BĂ���Ȗ�
     * @param curve2d1             basisSurface1 �̃p���??[�^��Ԃł̂Q�����\��
     * @param basisSurface2        ��?�?�BĂ���¤���̋Ȗ�
     * @param curve2d2             basisSurface2 �̃p���??[�^��Ԃł̂Q�����\��
     * @param masterRepresentation �ǂ̋�?�\����D?悷�邩����?�
     * @see PreferredSurfaceCurveRepresentation
     * @see NullArgumentException
     * @see InvalidArgumentValueException
     */
    protected SurfaceCurve3D(ParametricCurve3D curve3d,
                             ParametricSurface3D basisSurface1,
                             ParametricCurve2D curve2d1,
                             ParametricSurface3D basisSurface2,
                             ParametricCurve2D curve2d2,
                             int masterRepresentation) {
        super();
        if (curve3d == null && curve2d1 == null && curve2d2 == null)
            throw new NullArgumentException();

        if (basisSurface1 == null || basisSurface2 == null)
            throw new NullArgumentException();

        switch (masterRepresentation) {
            case PreferredSurfaceCurveRepresentation.CURVE_3D:
                if (curve3d == null)
                    throw new InvalidArgumentValueException();
                break;
            case PreferredSurfaceCurveRepresentation.CURVE_2D_1:
                if (curve2d1 == null)
                    throw new InvalidArgumentValueException();
                break;
            case PreferredSurfaceCurveRepresentation.CURVE_2D_2:
                if (curve2d2 == null)
                    throw new InvalidArgumentValueException();
                break;
            default:
                throw new InvalidArgumentValueException();
        }
        this.curve3d = curve3d;
        this.basisSurface1 = basisSurface1;
        this.curve2d1 = curve2d1;
        this.basisSurface2 = basisSurface2;
        this.curve2d2 = curve2d2;
        this.masterRepresentation = masterRepresentation;
    }

    /**
     * ��?�?�̂R�����\���ƈ�̋Ȗʂ�^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?\�z����C���X�^���X�̊e�t�B?[���h�ɑ���l�͈ȉ��̒ʂ�?B
     * <pre>
     * 		this.curve3d = curve3d;
     * 		this.basisSurface1 = basisSurface;
     * 		this.curve2d1 = null;
     * 		this.basisSurface2 = null;
     * 		this.curve2d2 = null;
     * 		this.masterRepresentation = PreferredSurfaceCurveRepresentation.CURVE_3D;
     * </pre>
     * </p>
     * <p/>
     * curve3d, basisSurface �̂����ꂩ�� null ��?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * </p>
     *
     * @param curve3d      �R�����\��
     * @param basisSurface ��?�?�BĂ���Ȗ�
     * @see PreferredSurfaceCurveRepresentation
     * @see NullArgumentException
     */
    public SurfaceCurve3D(ParametricCurve3D curve3d,
                          ParametricSurface3D basisSurface) {
        super();
        if (curve3d == null || basisSurface == null)
            throw new NullArgumentException();
        this.curve3d = curve3d;
        this.basisSurface1 = basisSurface;
        this.curve2d1 = null;
        this.basisSurface2 = null;
        this.curve2d2 = null;
        this.masterRepresentation = PreferredSurfaceCurveRepresentation.CURVE_3D;
    }

    /**
     * ��?�?�̈�̋Ȗʂɑ΂���Q�����\��������^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?\�z����C���X�^���X�̊e�t�B?[���h�ɑ���l�͈ȉ��̒ʂ�?B
     * <pre>
     * 		this.curve3d = null;
     * 		this.basisSurface1 = basisSurface;
     * 		this.curve2d1 = curve2d;
     * 		this.basisSurface2 = null;
     * 		this.curve2d2 = null;
     * 		this.masterRepresentation = PreferredSurfaceCurveRepresentation.CURVE_2D_1;
     * </pre>
     * </p>
     * <p/>
     * curve2d, basisSurface �̂����ꂩ�� null ��?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * </p>
     *
     * @param basisSurface ��?�?�BĂ���Ȗ�
     * @param curve2d      basisSurface �̃p���??[�^��Ԃł̂Q�����\��
     * @see PreferredSurfaceCurveRepresentation
     * @see NullArgumentException
     */
    public SurfaceCurve3D(ParametricSurface3D basisSurface,
                          ParametricCurve2D curve2d) {
        super();
        if (curve2d == null || basisSurface == null)
            throw new NullArgumentException();
        this.curve3d = null;
        this.basisSurface1 = basisSurface;
        this.curve2d1 = curve2d;
        this.basisSurface2 = null;
        this.curve2d2 = null;
        this.masterRepresentation = PreferredSurfaceCurveRepresentation.CURVE_2D_1;
    }

    /**
     * ���̖�?�?�̂R�����\�� (curve3d) ��Ԃ�?B
     * <p/>
     * null ���Ԃ邱�Ƃ ��?B
     * </p>
     *
     * @return �R�����\�� (curve3d)
     */
    public ParametricCurve3D curve3d() {
        return this.curve3d;
    }

    /**
     * ���̖�?�?�?�BĂ���Ȗ� (basisSurface1) ��Ԃ�?B
     *
     * @return ��?�?�BĂ���Ȗ� (basisSurface1)
     */
    public ParametricSurface3D basisSurface1() {
        return this.basisSurface1;
    }

    /**
     * ���̖�?�?�� basisSurface1 �̃p���??[�^��Ԃł̂Q�����\�� (curve2d1) ��Ԃ�?B
     * <p/>
     * null ���Ԃ邱�Ƃ ��?B
     * </p>
     *
     * @return basisSurface1 �̃p���??[�^��Ԃł̂Q�����\�� (curve2d1)
     */
    public ParametricCurve2D curve2d1() {
        return this.curve2d1;
    }

    /**
     * ���̖�?�?�?�BĂ���¤���̋Ȗ� (basisSurface2) ��Ԃ�?B
     * <p/>
     * null ���Ԃ邱�Ƃ ��?B
     * </p>
     *
     * @return ��?�?�BĂ���¤���̋Ȗ� (basisSurface2)
     */
    public ParametricSurface3D basisSurface2() {
        return this.basisSurface2;
    }

    /**
     * ���̖�?�?�� basisSurface2 �̃p���??[�^��Ԃł̂Q�����\�� (curve2d2) ��Ԃ�?B
     * <p/>
     * null ���Ԃ邱�Ƃ ��?B
     * </p>
     *
     * @return basisSurface2 �̃p���??[�^��Ԃł̂Q�����\�� (curve2d2)
     */
    public ParametricCurve2D curve2d2() {
        return this.curve2d2;
    }

    /**
     * ���̖�?�?�?�BĂ���Ȗ� (basisSurface1) ��Ԃ�?B
     *
     * @return ��?�?�BĂ���Ȗ� (basisSurface1)
     */
    public ParametricSurface3D basisSurface() {
        return basisSurface1();
    }

    /**
     * ���̖�?�?�� basisSurface1 �̃p���??[�^��Ԃł̂Q�����\�� (curve2d1) ��Ԃ�?B
     * <p/>
     * null ���Ԃ邱�Ƃ ��?B
     * </p>
     *
     * @return basisSurface1 �̃p���??[�^��Ԃł̂Q�����\�� (curve2d1)
     */
    public ParametricCurve2D curve2d() {
        return curve2d1();
    }

    /**
     * ���̖�?�?�ɂ�����?A������̋�?�\����D?悷�邩��?�?���Ԃ�?B
     *
     * @return ������̋�?�\����D?悷�邩��?�?�
     * @see PreferredSurfaceCurveRepresentation
     */
    public int masterRepresentation() {
        return this.masterRepresentation;
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^��Ԃɂ�������?�ł̒��� (���̂�) ��Ԃ�?B
     *
     * @param pint ������?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     */
    public double length(ParameterSection pint) {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D)
            return curve3d().length(pint);

        PrimitiveMapping realFunction
                = new PrimitiveMapping() {
            /**
             * Evaluates this function.
             */
            public double map(float x) {
                return map((double) x);
            }

            /**
             * Evaluates this function.
             */
            public double map(long x) {
                return map((double) x);
            }

            /**
             * Evaluates this function.
             */
            public double map(int x) {
                return map((double) x);
            }

            public double map(double parameter) {
                return tangentVector(parameter).length();
            }
        };
        double dTol = getToleranceForDistance() / 2.0;

        return GeometryUtils.getDefiniteIntegral(realFunction, pint, dTol);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     */
    public Point3D coordinates(double param) {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D)
            return curve3d().coordinates(param);

        ParametricCurve2D curve2d;
        ParametricSurface3D basisSurface;
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_2D_1) {
            basisSurface = basisSurface1();
            curve2d = curve2d1();
        } else {
            basisSurface = basisSurface2();
            curve2d = curve2d2();
        }

        Point2D paramS = curve2d.coordinates(param);
        return basisSurface.coordinates(paramS.x(), paramS.y());
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     */
    public Vector3D tangentVector(double param) {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D)
            return curve3d().tangentVector(param);

        ParametricCurve2D curve2d;
        ParametricSurface3D basisSurface;
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_2D_1) {
            basisSurface = basisSurface1();
            curve2d = curve2d1();
        } else {
            basisSurface = basisSurface2();
            curve2d = curve2d2();
        }
        Vector2D tngC = curve2d.tangentVector(param);
        Point2D paramS = curve2d.coordinates(param);
        Vector3D[] tngS = basisSurface.tangentVector(paramS.x(), paramS.y());
        return tngS[0].multiply(tngC.x()).add(tngS[1].multiply(tngC.y()));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     * @see UnsupportedOperationException
     */
    public CurveCurvature3D curvature(double param) {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D)
            return curve3d().curvature(param);

        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ����?�
     * @see UnsupportedOperationException
     */
    public CurveDerivative3D evaluation(double param) {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D)
            return curve3d().evaluation(param);

        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̃��C����Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ���C��
     * @see UnsupportedOperationException
     */
    public double torsion(double param) {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D)
            return curve3d().torsion(param);

        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * �Hٓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     * @throws IndefiniteSolutionException ��?�S�̂�?k�ނ��Ă���
     * @see UnsupportedOperationException
     */
    public PointOnCurve3D[] singular()
            throws IndefiniteSolutionException {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D) {
            PointOnCurve3D[] sol;
            try {
                sol = curve3d().singular();
            } catch (IndefiniteSolutionException e) {
                PointOnCurve3D sol1 = (PointOnCurve3D) e.suitable();
                sol1 = new PointOnCurve3D(this, sol1.parameter(), doCheckDebug);
                throw new IndefiniteSolutionException(sol1);
            }
            for (int i = 0; i < sol.length; i++) {
                sol[i] = new PointOnCurve3D(this, sol[i].parameter(), doCheckDebug);
            }
            return sol;
        }

        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * �ϋȓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł��� (���̋�?�͒�?�?�ł���)
     * @see UnsupportedOperationException
     */
    public PointOnCurve3D[] inflexion()
            throws IndefiniteSolutionException {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D) {
            PointOnCurve3D[] sol;
            try {
                sol = curve3d().inflexion();
            } catch (IndefiniteSolutionException e) {
                PointOnCurve3D sol1 = (PointOnCurve3D) e.suitable();
                sol1 = new PointOnCurve3D(this, sol1.parameter(), doCheckDebug);
                throw new IndefiniteSolutionException(sol1);
            }
            for (int i = 0; i < sol.length; i++) {
                sol[i] = new PointOnCurve3D(this, sol[i].parameter(), doCheckDebug);
            }
            return sol;
        }

        throw new UnsupportedOperationException();
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @see UnsupportedOperationException
     */
    public PointOnCurve3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D) {
            PointOnCurve3D[] sol;
            try {
                sol = curve3d().projectFrom(point);
            } catch (IndefiniteSolutionException e) {
                PointOnCurve3D sol1 = (PointOnCurve3D) e.suitable();
                sol1 = new PointOnCurve3D(this, sol1.parameter(), doCheckDebug);
                throw new IndefiniteSolutionException(sol1);
            }
            for (int i = 0; i < sol.length; i++) {
                sol[i] = new PointOnCurve3D(this, sol[i].parameter(), doCheckDebug);
            }
            return sol;
        }

        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param section   ��?�ߎ�����p���??[�^���
     * @param tolerance �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     * @see UnsupportedOperationException
     */
    public Polyline3D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D) {
            Polyline3D pol = curve3d().toPolyline(pint, tol);
            int nPnts = pol.nPoints();
            Point3D[] pnts = new Point3D[nPnts];
            PointOnCurve3D poc;
            for (int i = 0; i < nPnts; i++) {
                poc = (PointOnCurve3D) pol.pointAt(i);
                pnts[i] = new PointOnCurve3D(this, poc.parameter(), doCheckDebug);
            }
            return new Polyline3D(pnts, pol.closed());
        }

        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     * @see UnsupportedOperationException
     */
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D)
            return curve3d().toBsplineCurve(pint);

        throw new UnsupportedOperationException();
    }

    /**
     * ���̖�?�?�Ƃ��̋�?�?^�Ȗʂ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�܂��͋Ȗ�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    private IntersectionPoint3D[] doIntersection(GeometryElement mate,
                                                 boolean doExchange)
            throws IndefiniteSolutionException {
        if (!mate.is3D() || !mate.isParametric() || mate.isPoint())
            throw new FatalException();

        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D) {
            IntersectionPoint3D[] sol;
            try {
                if (mate.isCurve())
                    sol = curve3d().intersect((ParametricCurve3D) mate);
                else
                    sol = curve3d().intersect((ParametricSurface3D) mate);
            } catch (IndefiniteSolutionException e) {
                IntersectionPoint3D sol1 = (IntersectionPoint3D) e.suitable();
                sol1 = (IntersectionPoint3D) sol1.changeCurve1(this);
                if (doExchange)
                    sol1 = sol1.exchange();
                throw new IndefiniteSolutionException(sol1);
            }
            for (int i = 0; i < sol.length; i++) {
                sol[i] = (IntersectionPoint3D) sol[i].changeCurve1(this);
                if (doExchange)
                    sol[i] = sol[i].exchange();
            }

            return sol;
        }

        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
        return doIntersection(mate, false);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Circle3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Hyperbola3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(Polyline3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(TrimmedCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(CompositeCurveSegment3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_��pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    IntersectionPoint3D[] intersect(CompositeCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋Ȗʂ̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ���܂̂Ƃ���
     * masterRepresentation �� PreferredSurfaceCurveRepresentation.CURVE_3D �łȂ�?�?��ɂ�
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see UnsupportedOperationException
     */
    public IntersectionPoint3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        return doIntersection(mate, false);
    }

    /**
     * ���̋Ȗ�(��?͓I�Ȗ�)�Ƃ̌�_�𓾂�(internal use)
     *
     * @param mate       ���̋Ȗ�
     * @param doExchange ��_��pointOnCurve1,2��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see IntersectionPoint3D
     */
    IntersectionPoint3D[] intersect(ElementarySurface3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋Ȗ�(Bezier�Ȗ�)�Ƃ̌�_�𓾂�(internal use)
     *
     * @param mate       ���̋Ȗ�
     * @param doExchange ��_��pointOnCurve1,2��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see IntersectionPoint3D
     */
    IntersectionPoint3D[] intersect(PureBezierSurface3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋Ȗ�(Bspline�Ȗ�)�Ƃ̌�_�𓾂�(internal use)
     *
     * @param mate       ���̋Ȗ�
     * @param doExchange ��_��pointOnCurve1,2��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see IntersectionPoint3D
     */
    IntersectionPoint3D[] intersect(BsplineSurface3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        return doIntersection(mate, doExchange);
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̃?�\�b�h��?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     * @see UnsupportedOperationException
     */
    public ParametricCurve3D parallelTranslate(Vector3D moveVec) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     *
     * @return �p���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D)
            return curve3d().getParameterDomain();

        ParametricCurve2D curve2d;
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_2D_1)
            curve2d = curve2d1();
        else
            curve2d = curve2d2();

        return curve2d.getParameterDomain();
    }

    /**
     * ���̋�?�􉽓I�ɕ��Ă��邩�ۂ���Ԃ�?B
     *
     * @return �􉽓I�ɕ��Ă���� true?A�����łȂ���� false
     */
    boolean getClosedFlag() {
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_3D)
            return curve3d().getClosedFlag();

        ParametricCurve2D curve2d;
        ParametricSurface3D basisSurface;
        if (masterRepresentation() == PreferredSurfaceCurveRepresentation.CURVE_2D_1) {
            basisSurface = basisSurface1();
            curve2d = curve2d1();
        } else {
            basisSurface = basisSurface2();
            curve2d = curve2d2();
        }
        if (curve2d.isPeriodic())
            return true;
        if (curve2d.isInfinite())
            return false;

        ParameterSection section = curve2d.parameterDomain().section();
        if (coordinates(section.start()).identical(coordinates(section.end())))
            return true;

        return false;
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve3D#SURFACE_CURVE_3D ParametricCurve3D.SURFACE_CURVE_3D}
     */
    int type() {
        return SURFACE_CURVE_3D;
    }

    /**
     * ���̋�?��?A�^����ꂽ��?�?W�n�� Z ���̎���?A
     * �^����ꂽ�p�x������]��������?��Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̃?�\�b�h��?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param trns ��?�?W�n���瓾��ꂽ?W�ϊ����Z�q
     * @param rCos cos(��]�p�x)
     * @param rSin sin(��]�p�x)
     * @return ��]��̋�?�
     * @see UnsupportedOperationException
     */
    ParametricCurve3D rotateZ(CartesianTransformationOperator3D trns,
                              double rCos, double rSin) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�?�̓_��?A�^����ꂽ��?�?�ɂȂ��_���Ԃ�?B
     * <p/>
     * ���܂̂Ƃ���?A���̃?�\�b�h��?��
     * UnsupportedOperationException �̗�O��?�����?B
     * </p>
     *
     * @param line ��?�
     * @return �^����ꂽ��?�?�ɂȂ��_
     */
    Point3D getPointNotOnLine(Line3D line) {
        throw new UnsupportedOperationException();
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
        ParametricCurve3D tCurve3d = this.curve3d;
        ParametricSurface3D tBasisSurface1 = this.basisSurface1;
        ParametricSurface3D tBasisSurface2 = this.basisSurface2;

        if (tCurve3d != null)
            tCurve3d = tCurve3d.transformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);
        if (tBasisSurface1 != null)
            tBasisSurface1 = tBasisSurface1.transformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);
        if (tBasisSurface2 != null)
            tBasisSurface2 = tBasisSurface2.transformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        return new SurfaceCurve3D(tCurve3d,
                tBasisSurface1, this.curve2d1,
                tBasisSurface2, this.curve2d2,
                this.masterRepresentation);
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
        if (curve3d != null) {
            writer.println(indent_tab + "\tcurve3d");
            curve3d.output(writer, indent + 2);
        }
        writer.println(indent_tab + "\tbasisSurface1");
        basisSurface1.output(writer, indent + 2);
        if (curve2d1 != null) {
            writer.println(indent_tab + "\tcurve2d1");
            curve2d1.output(writer, indent + 2);
        }
        if (basisSurface2 != null) {
            writer.println(indent_tab + "\tbasisSurface2");
            basisSurface2.output(writer, indent + 2);
            if (curve2d2 != null) {
                writer.println(indent_tab + "\tcurve2d2");
                curve2d2.output(writer, indent + 2);
            }
        }
        writer.println(indent_tab + "\tmasterRepresentation\t"
                + PreferredSurfaceCurveRepresentation.toString(masterRepresentation));
        writer.println(indent_tab + "End");
    }
}

