/*
 * �Q���� : ��L�? (��?���) �a�X�v���C����?��їL�?�a�X�v���C����?��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: BsplineCurve2D.java,v 1.3 2007-10-21 21:08:07 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.PrimitiveMapping;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �Q���� : ��L�? (��?���) �a�X�v���C����?��їL�?�a�X�v���C����?��\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �a�X�v���C���̃m�b�g��Ɋւ���?�� knotData ({@link BsplineKnot BsplineKnot})
 * ��?��?B
 * ?���_��Ȃǂ�ێ?����t�B?[���h�ɂ��Ă�?A
 * {@link FreeformCurveWithControlPoints2D �X?[�p?[�N���X�̉�?�} ��Q?�?B
 * </p>
 * <p/>
 * �a�X�v���C����?�̃p���??[�^��`���?A�Ή�����m�b�g��ɂ�BČ��܂�?B
 * </p>
 * <p/>
 * t ��p���??[�^�Ƃ���a�X�v���C����?� P(t) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	n = ��?�̎�?�
 * 	m = �Z�O�?���g��?� (�J�����`�� : (?���_��?� - ��?�̎�?�), �����`�� : ?���_��?�)
 * 	di = controlPoints[i]
 * 	wi = weights[i]
 * </pre>
 * �Ƃ���?A��L�?�a�X�v���C����?��
 * <pre>
 * 	P(t) =	(di * Nn,i(t)) �̑?�a		(i = 0, ..., (m + n - 1))
 * </pre>
 * �L�?�a�X�v���C����?��
 * <pre>
 * 		(wi * di * Nn,i(t)) �̑?�a
 * 	P(t) =	-------------------------- 	(i = 0, ..., (m + n - 1))
 * 		(wi * Nn,i(t)) �̑?�a
 * </pre>
 * ������ Nn,i(t) �͂a�X�v���C������?�?B
 * �Ȃ�?A�����`����?�?��� i &gt; (?���_��?� - 1) �ƂȂ� i �ɂ��Ă�?A
 * �Ή�����?���_��?d�݂����ꂼ�� dj, wj (j = i - ?���_��?�) �ƂȂ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:07 $
 */

public class BsplineCurve2D extends FreeformCurveWithControlPoints2D {
    /**
     * �m�b�g��?B
     *
     * @serial
     */
    private BsplineKnot knotData;

    /**
     * ��?�̌`?�R����t���O?B
     * <p/>
     * ���̃t�B?[���h��?A���܂̂Ƃ��늈�p����Ă��炸?A
     * ?�� BsplineCurveForm.UNSPECIFIED �ɂȂBĂ���?B
     * </p>
     *
     * @serial
     * @see BsplineCurveForm
     */
    private int curveForm = BsplineCurveForm.UNSPECIFIED;

    /**
     * �m�b�g��𖾎���?A
     * ?���_���^���đ�?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[])
     * super}(controlPoints)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * knotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(degree, KnotType.UNSPECIFIED, periodic, knotMultiplicities, knots, nControlPoints())
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param degree             ��?�̎�?�
     * @param periodic           �����`�����ۂ���\���t���O
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param controlPoints      ?���_�̔z��
     */
    public BsplineCurve2D(int degree, boolean periodic,
                          int[] knotMultiplicities, double[] knots,
                          Point2D[] controlPoints) {
        super(controlPoints);
        knotData = new BsplineKnot(degree, KnotType.UNSPECIFIED, periodic,
                knotMultiplicities, knots,
                nControlPoints());
    }

    /**
     * �m�b�g��𖾎���?A?���_���^���ĊJ�����`���̑�?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[])
     * super}(controlPoints)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * knotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(degree, KnotType.UNSPECIFIED, false, knotMultiplicities, knots, nControlPoints())
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param degree             ��?�̎�?�
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param controlPoints      ?���_�̔z��
     */
    public BsplineCurve2D(int degree,
                          int[] knotMultiplicities, double[] knots,
                          Point2D[] controlPoints) {
        super(controlPoints);
        knotData = new BsplineKnot(degree, KnotType.UNSPECIFIED, false,
                knotMultiplicities, knots,
                nControlPoints());
    }

    /**
     * �m�b�g��𖾎���?A
     * ?���_���?d�ݗ��^���ėL�?��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[],double[])
     * super}(controlPoints, weights)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * knotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(degree, KnotType.UNSPECIFIED, periodic, knotMultiplicities, knots, nControlPoints())
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param degree             ��?�̎�?�
     * @param periodic           �����`�����ۂ���\���t���O
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param controlPoints      ?���_�̔z��
     * @param weights            ?d�݂̔z��
     */
    public BsplineCurve2D(int degree, boolean periodic,
                          int[] knotMultiplicities, double[] knots,
                          Point2D[] controlPoints, double[] weights) {
        super(controlPoints, weights);
        knotData = new BsplineKnot(degree, KnotType.UNSPECIFIED, periodic,
                knotMultiplicities, knots,
                nControlPoints());
    }

    /**
     * �m�b�g��𖾎���?A
     * ?���_���?d�ݗ��^���ĊJ�����`���̗L�?��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[],double[])
     * super}(controlPoints, weights)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * knotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(degree, KnotType.UNSPECIFIED, false, knotMultiplicities, knots, nControlPoints())
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param degree             ��?�̎�?�
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param controlPoints      ?���_�̔z��
     * @param weights            ?d�݂̔z��
     */
    public BsplineCurve2D(int degree,
                          int[] knotMultiplicities, double[] knots,
                          Point2D[] controlPoints, double[] weights) {
        super(controlPoints, weights);
        knotData = new BsplineKnot(degree, KnotType.UNSPECIFIED, false,
                knotMultiplicities, knots,
                nControlPoints());
    }

    /**
     * �m�b�g��𖾎�������
     * �m�b�g��̎�ʂ�?���_���^���đ�?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���܂̂Ƃ���?AknotSpec ���Ƃ蓾��l�� KnotType.UNIFORM_KNOTS �����ł���
     * (KnotType.{QUASI_UNIFORM_KNOTS, PIECEWISE_BEZIER_KNOTS} �ɂ͖��Ή�) ?B
     * </p>
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[])
     * super}(controlPoints)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * knotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(degree, knotSpec, periodic, null, null, nControlPoints())
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param degree        ��?�̎�?�
     * @param periodic      �����`�����ۂ���\���t���O
     * @param knotSpec      �m�b�g��̎��
     * @param controlPoints ?���_�̔z��
     */
    public BsplineCurve2D(int degree, boolean periodic,
                          int knotSpec,
                          Point2D[] controlPoints) {
        super(controlPoints);
        knotData = new BsplineKnot(degree, knotSpec, periodic, null, null,
                nControlPoints());
    }

    /**
     * �m�b�g��𖾎�������
     * �m�b�g��̎�ʂ�?���_���^���ĊJ������?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���܂̂Ƃ���?AknotSpec ���Ƃ蓾��l�� KnotType.UNIFORM_KNOTS �����ł���
     * (KnotType.{QUASI_UNIFORM_KNOTS, PIECEWISE_BEZIER_KNOTS} �ɂ͖��Ή�) ?B
     * </p>
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[])
     * super}(controlPoints)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * knotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(degree, knotSpec, false, null, null, nControlPoints())
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param degree        ��?�̎�?�
     * @param knotSpec      �m�b�g��̎��
     * @param controlPoints ?���_�̔z��
     */
    public BsplineCurve2D(int degree,
                          int knotSpec,
                          Point2D[] controlPoints) {
        super(controlPoints);
        knotData = new BsplineKnot(degree, knotSpec, false, null, null,
                nControlPoints());
    }

    /**
     * �m�b�g��𖾎�������
     * �m�b�g��̎�ʂ�?���_�񂨂��?d�ݗ��^���ėL�?��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���܂̂Ƃ���?AknotSpec ���Ƃ蓾��l�� KnotType.UNIFORM_KNOTS �����ł���
     * (KnotType.{QUASI_UNIFORM_KNOTS, PIECEWISE_BEZIER_KNOTS} �ɂ͖��Ή�) ?B
     * </p>
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[],double[])
     * super}(controlPoints, weights)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * knotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(degree, knotSpec, periodic, null, null, nControlPoints())
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param degree        ��?�̎�?�
     * @param periodic      �����`�����ۂ���\���t���O
     * @param knotSpec      �m�b�g��̎��
     * @param controlPoints ?���_�̔z��
     * @param weights       ?d�݂̔z��
     */
    public BsplineCurve2D(int degree, boolean periodic,
                          int knotSpec,
                          Point2D[] controlPoints, double[] weights) {
        super(controlPoints, weights);
        knotData = new BsplineKnot(degree, knotSpec, periodic, null, null,
                nControlPoints());
    }

    /**
     * �m�b�g��𖾎�������
     * �m�b�g��̎�ʂ�?���_�񂨂��?d�ݗ��^���ĊJ�����`���̗L�?��?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���܂̂Ƃ���?AknotSpec ���Ƃ蓾��l�� KnotType.UNIFORM_KNOTS �����ł���
     * (KnotType.{QUASI_UNIFORM_KNOTS, PIECEWISE_BEZIER_KNOTS} �ɂ͖��Ή�) ?B
     * </p>
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[],double[])
     * super}(controlPoints, weights)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * knotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(degree, knotSpec, false, null, null, nControlPoints())
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param degree        ��?�̎�?�
     * @param knotSpec      �m�b�g��̎��
     * @param controlPoints ?���_�̔z��
     * @param weights       ?d�݂̔z��
     */
    public BsplineCurve2D(int degree,
                          int knotSpec,
                          Point2D[] controlPoints, double[] weights) {
        super(controlPoints, weights);
        knotData = new BsplineKnot(degree, knotSpec, false, null, null,
                nControlPoints());
    }

    /**
     * �m�b�g��� BsplineKnot �̃I�u�W�F�N�g�Ƃ��ēn��?A
     * ?���_ (��?d��) ��񎟌��z��ŗ^����
     * ��?�����?� (���邢�͗L�?��?�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(double[][])
     * super}(cpArray)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * knotData �̎���?���_��?��� cpArray �̎���?���_��?�����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param knotData �m�b�g��
     * @param cpArray  ?���_ (�����?d��) �̔z��
     * @see InvalidArgumentValueException
     */
    BsplineCurve2D(BsplineKnot knotData, double[][] cpArray) {
        super(cpArray);

        if (knotData.nControlPoints() != nControlPoints())
            throw new InvalidArgumentValueException();

        this.knotData = knotData;
    }

    /**
     * �m�b�g��� BsplineKnot �̃I�u�W�F�N�g�Ƃ��ēn��?A
     * ?���_���?d�ݗ��^����
     * ��?�����?� (���邢�͗L�?��?�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformCurveWithControlPoints2D#FreeformCurveWithControlPoints2D(Point2D[],double[],boolean)
     * super}(controlPoints, weights, false)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * ���̃R���X�g���N�^�ł͈�?��̃`�F�b�N��?s�Ȃ�Ȃ��̂�?A���p�ɂ͒?�ӂ��K�v�ł���?B
     * </p>
     *
     * @param knotData      �m�b�g��
     * @param controlPoitns ?���_�̔z��
     * @param weights       ?d�݂̔z��
     */
    BsplineCurve2D(BsplineKnot knotData,
                   Point2D[] controlPoints,
                   double[] weights) {
        super(controlPoints, weights, false);
        this.knotData = knotData;
    }

    /**
     * �_���?A����ɑΉ�����p���??[�^�l�̗��^����?A
     * ���̓_����Ԃ���J�����`���̑�?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     *
     * @param points ��Ԃ���_��
     * @param params �_���̊e�_�ɂ�����p���??[�^�l�̗�
     */
    public BsplineCurve2D(Point2D[] points, double[] params) {
        super();
        Interpolation2D doObj = new Interpolation2D(points, params);
        this.controlPoints = doObj.controlPoints();
        this.knotData = doObj.knotData();
        this.weights = doObj.weights();    // may be null
    }

    /**
     * �_���?A����ɑΉ�����p���??[�^�l�̗񂨂�ї��[�_�ł�?ڃx�N�g����^����?A
     * ���̓_����Ԃ���J�����`���̑�?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * endvecs[0] �Ŏn�_�ł�?ڃx�N�g��?A
     * endvecs[1] ��?I�_�ł�?ڃx�N�g����?B
     * </p>
     *
     * @param points  ��Ԃ���_��
     * @param params  �_���̊e�_�ɂ�����p���??[�^�l�̗�
     * @param endvecs ���[�_�ł�?ڃx�N�g��
     */
    public BsplineCurve2D(Point2D[] points, double[] params, Vector2D[] endvecs) {
        super();
        Interpolation2D doObj = new Interpolation2D(points, params, endvecs);
        this.controlPoints = doObj.controlPoints();
        this.knotData = doObj.knotData();
        this.weights = doObj.weights();    // may be null
    }

    /**
     * �_���?A����ɑΉ�����p���??[�^�l�̗�?A���[�_�ł�?ڃx�N�g������ъJ��?���^����?A
     * ���̓_����Ԃ��鑽?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * isClosed �� true ��?�?�?A
     * (params �̗v�f?�) = (points �̗v�f?� + 1) �ƂȂBĂ���K�v������?B
     * </p>
     * <p/>
     * endvecs[0] �Ŏn�_�ł�?ڃx�N�g��?A
     * endvecs[1] ��?I�_�ł�?ڃx�N�g����?B
     * �Ȃ�?AisClosed �� true ��?�?��ɂ�?A���̔z��͎Q?Ƃ���Ȃ�?B
     * </p>
     *
     * @param points   ��Ԃ���_��
     * @param params   �_���̊e�_�ɂ�����p���??[�^�l�̗�
     * @param endvecs  ���[�_�ł�?ڃx�N�g��
     * @param isClosed �����`���̋�?��?�?����邩�ۂ��̃t���O
     */
    public BsplineCurve2D(Point2D[] points, double[] params, Vector2D[] endvecs, boolean isClosed) {
        super();
        Interpolation2D doObj = new Interpolation2D(points, params, endvecs, isClosed);
        this.controlPoints = doObj.controlPoints();
        this.knotData = doObj.knotData();
        this.weights = doObj.weights();    // may be null
    }

    /**
     * �_���?A����ɑΉ�����p���??[�^�l�̗�?A���[�_�ł�?�?�̕���ъJ��?���^����?A
     * ���̓_���ߎ����鑽?�����?�Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * isClosed �� true ��?�?�?A
     * (params �̗v�f?�) = (points �̗v�f?� + 1) �ƂȂBĂ���K�v������?B
     * </p>
     * <p/>
     * endDir[0] �Ŏn�_�ł�?�?���?A
     * endDir[1] ��?I�_�ł�?�?����?B
     * �Ȃ�?AisClosed �� true ��?�?��ɂ�?A���̔z��͎Q?Ƃ���Ȃ�?B
     * </p>
     * <p/>
     * �w�肳�ꂽ?��x�ŋߎ��ł��Ȃ��B�?�?��ɂ�?A
     * �^����ꂽ�_����Ԃ����?��?�?�����?B
     * </p>
     *
     * @param points   ��Ԃ���_��
     * @param params   �_���̊e�_�ɂ�����p���??[�^�l�̗�
     * @param endDir   ���[�_�ł�?�?�̕��
     * @param isClosed �����`���̋�?��?�?����邩�ۂ��̃t���O
     * @param tol      �e�_�ɂ�����ߎ���?��x
     * @param midTol   �_�̒��Ԃɂ�����ߎ���?��x
     */
    public BsplineCurve2D(Point2D[] points, double[] params, Vector2D[] endDir,
                          boolean isClosed, ToleranceForDistance tol,
                          ToleranceForDistance midTol) {
        super();
        Approximation2D doObj = new Approximation2D(points, params, endDir, isClosed);
        BsplineCurve2D bsc = doObj.getApproximationWithTolerance(tol, midTol);
        this.controlPoints = bsc.controlPoints;
        this.knotData = bsc.knotData;
        this.weights = bsc.weights;    // may be null
    }

    /**
     * ���̋�?�̎�?���Ԃ�?B
     *
     * @return ��?�
     */
    public int degree() {
        return knotData.degree();
    }

    /**
     * ���̋�?�̃m�b�g�̎�ʂ�Ԃ�?B
     *
     * @return �m�b�g�̎��
     * @see KnotType
     */
    public int knotSpec() {
        return knotData.knotSpec();
    }

    /**
     * ���̋�?�̃m�b�g��?���Ԃ�?B
     * <p/>
     * �����Ō���?u�m�b�g��?�?v�Ƃ�
     * knotData �� knots �t�B?[���h��?ݒ肳�ꂽ�m�b�g�l�̔z��̒����ł͂Ȃ�?A
     * �a�X�v���C���̃m�b�g��{���̃m�b�g��?��ł���?B
     * </p>
     *
     * @return �m�b�g��?�
     */
    public int nKnotValues() {
        return knotData.nKnotValues();
    }

    /**
     * ���̋�?�̃m�b�g��� n �Ԃ߂̃m�b�g�l��Ԃ�?B
     * <p/>
     * �����Ō���?un �Ԗ�?v�Ƃ�
     * knotData �� knots �t�B?[���h��?ݒ肳�ꂽ�m�b�g�l�̔z���̃C���f�b�N�X�ł͂Ȃ�?A
     * �a�X�v���C���̃m�b�g��{���̈Ӗ��ł̃C���f�b�N�X�ł���?B
     * </p>
     *
     * @param n �C���f�b�N�X
     * @return n �Ԃ߂̃m�b�g�l
     */
    public double knotValueAt(int n) {
        return knotData.knotValueAt(n);
    }

    /**
     * ���̋�?�̃Z�O�?���g��?���Ԃ�?B
     *
     * @return �Z�O�?���g��?�
     */
    int nSegments() {
        return knotData.nSegments();
    }

    /**
     * ���̋�?��?A�p���??[�^�I��?k�ނ��Ă��Ȃ��L��Z�O�?���g��?���Ԃ�?B
     *
     * @return �p���??[�^�I��?k�ނ��Ă��Ȃ��L��Z�O�?���g��?��
     */
    BsplineKnot.ValidSegmentInfo validSegments() {
        return knotData.validSegments();
    }

    /**
     * ���̋�?�̃m�b�g��Ԃ�?B
     *
     * @return �m�b�g��
     */
    BsplineKnot knotData() {
        return knotData;
    }

    /**
     * ���̋�?�̃p���??[�^��`�悪���I���ۂ���Ԃ�?B
     * <p/>
     * �p���??[�^�h�?�C���𒲂ׂ�K�v���Ȃ��̂ŃI?[�o?[���C�h����?B
     * </p>
     *
     * @return ���I�ł���� true?A�����łȂ���� false
     */
    public boolean isPeriodic() {
        return knotData.isPeriodic();
    }

    /**
     * ���̋�?�� n �Ԗڂ�?���_��Ԃ�?B
     * <p/>
     * �����`����?�?��̓C���f�b�N�X����I�Ɉ���?B
     * </p>
     *
     * @param n �C���f�b�N�X
     * @return ?���_
     */
    public Point2D controlPointAt(int n) {
        if (isPeriodic()) {
            int ncp = nControlPoints();
            while (n < 0) n += ncp;
            while (n >= ncp) n -= ncp;
        }
        return controlPoints[n];
    }

    /**
     * ���̋�?�̎w��̂a�X�v���C������?��̑�?����\���̌W?��𓾂�?B
     *
     * @param coef ���Z���ʂł��鑽?����̌W?���i�[����z��
     * @param jjj  ���Z�̑�?ۂƂ���m�b�g�͈̔͂�?擪�̃m�b�g�̃C���f�b�N�X
     * @param seg  ���Z�̑�?ۂƂ���Z�O�?���g�̊J�n�p���??[�^�ƂȂ�m�b�g�̃C���f�b�N�X
     * @param pTol �p���??[�^�l�̋��e��?�
     */
    private void make_coef(double[] coef, int jjj, int seg, double pTol) {
        int degree = coef.length - 3;

        if (degree == 0)
            coef[1] = (jjj == seg) ? 1.0 : 0.0;
        else {
            double coef_1[] = new double[degree + 2];
            double aaa;
            double kj;
            int ijk;

            for (ijk = 0; ijk <= degree; ijk++)
                coef[ijk + 1] = 0.0;

            coef_1[0] = coef_1[degree + 1] = 0.0;

            if (jjj != (seg - degree)) {
                kj = knotValueAt(jjj);
                aaa = knotValueAt(jjj + degree) - kj;
                if (aaa > pTol) {
                    make_coef(coef_1, jjj, seg, pTol);
                    for (ijk = 0; ijk <= degree; ijk++)
                        coef[ijk + 1] += (coef_1[ijk + 1] - coef_1[ijk] * kj) / aaa;
                }
            }

            if (jjj != seg) {
                kj = knotValueAt(jjj + degree + 1);
                aaa = kj - knotValueAt(jjj + 1);
                if (aaa > pTol) {
                    make_coef(coef_1, jjj + 1, seg, pTol);
                    for (ijk = 0; ijk <= degree; ijk++)
                        coef[ijk + 1] -= (coef_1[ijk + 1] - coef_1[ijk] * kj) / aaa;
                }
            }
        }
    }

    /**
     * ���̋�?�̎w��̃Z�O�?���g�̑�?����\����Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����z�� R �̗v�f?���?A
     * ���̋�?��L�?�ł���� 2?A
     * �L�?�ł���� 3 �ł���?B
     * </p>
     * <p/>
     * ��L�?��?��?�?�?A
     * R[0] �� X ?���?A
     * R[1] �� Y ?���
     * �̑�?����\����\��?B
     * </p>
     * <p/>
     * �L�?��?��?�?�?A
     * R[0] �� WX ?���?A
     * R[1] �� WY ?���
     * R[2] �� W ?���
     * �̑�?����\����\��?B
     * </p>
     *
     * @param iSseg  �Z�O�?���g�̔�?�
     * @param isPoly ��L�?�ł��邩�ǂ���
     * @return ��?����̔z��
     */
    public DoublePolynomial[] polynomial(int iSseg, boolean isPoly) {
        int degree = degree();
        int isckt = iSseg;
        int ijk, klm, mno, pklm, i;
        double[][] cntlPnts = toDoubleArray(isPoly);
        int uicp = nControlPoints();
        int dim = cntlPnts[0].length;
        double[][] coef = new double[dim][];
        for (i = 0; i < dim; i++)
            coef[i] = new double[degree + 1];
        double[] eA = new double[degree + 3];
        double pTol = getToleranceForParameter();
        DoublePolynomial[] polynomial = new DoublePolynomial[dim];

        for (i = 0; i < dim; i++)
            for (ijk = 0; ijk <= degree; ijk++)
                coef[i][ijk] = 0.0;

        for (ijk = 0, pklm = klm = isckt; ijk <= degree; ijk++, pklm++, klm++) {
            make_coef(eA, klm, (isckt + degree), pTol);

            if ((isPeriodic()) && (pklm == uicp))
                pklm = 0;
            for (i = 0; i < dim; i++) {
                for (mno = 0; mno <= degree; mno++)
                    coef[i][degree - mno] += eA[mno + 1] * cntlPnts[pklm][i];
            }
        }

        for (i = 0; i < dim; i++)
            polynomial[i] = new DoublePolynomial(coef[i]);

        return polynomial;
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
        double length = 0.0;
        PrimitiveMapping realFunction;
        double dTol = getToleranceForDistance() / 2.0;
        double pTol = getToleranceForParameter();

        if (!isPolynomial() || Math.abs(pint.increase()) <= pTol) {
            realFunction
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
            length = GeometryUtils.getDefiniteIntegral(realFunction, pint, dTol);
        } else {
            BsplineCurve2D tbsc = truncate(pint);
            BsplineKnot.ValidSegmentInfo vsegInfo
                    = tbsc.knotData.validSegments();
            int nvseg = vsegInfo.nSegments();

            for (int ijk = 0; ijk < nvseg; ijk++) {
                DoublePolynomial[] poly =
                        tbsc.polynomial(ijk, isPolynomial());
                final DoublePolynomial[] deriv =
                        new DoublePolynomial[poly.length];

                for (int klm = 0; klm < poly.length; klm++)
                    deriv[klm] = (DoublePolynomial) poly[klm].differentiate();

                realFunction
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
                        final double[] tang = new double[2];
                        for (int klm = 0; klm < 2; klm++)
                            tang[klm] = deriv[klm].map(parameter);

                        return Math.sqrt(tang[0] * tang[0] +
                                tang[1] * tang[1]);
                    }
                };
                ParameterSection psec =
                        new ParameterSection(vsegInfo.knotPoint(ijk)[0],
                                vsegInfo.knotPoint(ijk)[1]);
                length += GeometryUtils.getDefiniteIntegral(realFunction,
                        psec, dTol);
            }
        }
        return length;
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
        double[][] cntlPnts;
        double[] d0D;
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        d0D = BsplineCurveEvaluation.coordinates(knotData, cntlPnts, param);
        if (!isPoly) {
            convRational0Deriv(d0D);
        }
        return new CartesianPoint2D(d0D[0], d0D[1]);
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
        double[][] cntlPnts;
        double[] d1D = new double[3];
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        if (isPoly) {
            BsplineCurveEvaluation.evaluation(knotData, cntlPnts, param,
                    null, d1D, null, null);
        } else {
            double[] d0D = new double[3];

            BsplineCurveEvaluation.evaluation(knotData, cntlPnts, param,
                    d0D, d1D, null, null);
            convRational1Deriv(d0D, d1D);
        }
        return new LiteralVector2D(d1D[0], d1D[1]);
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
        int degree;
        CurveDerivative2D deriv;
        boolean tang0;
        double tang_leng;
        double dDcrv;
        Vector2D dDnrm;
        CurveCurvature2D crv;
        ConditionOfOperation condition = ConditionOfOperation.getCondition();
        double tol_d = condition.getToleranceForDistance();

        degree = degree();
        deriv = evaluation(param);

        tang_leng = deriv.d1D().norm();
        if (tang_leng < (tol_d * tol_d)) {
            tang0 = true;
        } else {
            tang0 = false;
        }

        if ((degree < 2) || (tang0 == true)) {
            dDcrv = 0.0;
            dDnrm = Vector2D.zeroVector;
        } else {
            double ewvec;

            tang_leng = Math.sqrt(tang_leng);
            dDcrv = tang_leng * tang_leng * tang_leng;

            ewvec = deriv.d1D().zOfCrossProduct(deriv.d2D());
            dDcrv = Math.abs(ewvec) / dDcrv;

            if (ewvec < 0.0) {
                dDnrm = new LiteralVector2D(deriv.d1D().y(), (-deriv.d1D().x()));
            } else {
                dDnrm = new LiteralVector2D((-deriv.d1D().y()), deriv.d1D().x());
            }

            dDnrm = dDnrm.unitized();
        }

        return new CurveCurvature2D(dDcrv, dDnrm);
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
        double[][] cntlPnts;
        double[] ld0D = new double[3];
        double[] ld1D = new double[3];
        double[] ld2D = new double[3];
        Point2D d0D;
        Vector2D d1D;
        Vector2D d2D;
        boolean isPoly = isPolynomial();

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        BsplineCurveEvaluation.evaluation(knotData, cntlPnts, param,
                ld0D, ld1D, ld2D, null);
        if (!isPoly) {
            convRational2Deriv(ld0D, ld1D, ld2D);
        }
        d0D = new CartesianPoint2D(ld0D[0], ld0D[1]);
        d1D = new LiteralVector2D(ld1D[0], ld1D[1]);
        d2D = new LiteralVector2D(ld2D[0], ld2D[1]);
        return new CurveDerivative2D(d0D, d1D, d2D);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^��ɑ΂���u�?�b�T�~���O�̌��ʂ�Ԃ�?B
     * <p/>
     * parameters �̗v�f?���?A���̋�?�̎�?��Ɉ�v���Ă���K�v������?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param segNumber  ���Z��?ۂƂȂ�Z�O�?���g�̔�?� (?擪�� 0)
     * @param parameters �p���??[�^�l�̔z��
     * @return �u�?�b�T�~���O�̌��ʂł���?W�l
     * @see ParameterOutOfRange
     */
    public Point2D blossoming(int segNumber,
                              double[] parameters) {
        double[] adjustedParameters = new double[this.degree()];
        for (int i = 0; i < this.degree(); i++)
            adjustedParameters[i] = this.checkParameter(parameters[i]);
        boolean isPoly = this.isPolynomial();

        double[] d0D =
                BsplineCurveEvaluation.blossoming(knotData,
                        toDoubleArray(isPoly),
                        segNumber,
                        adjustedParameters);

        if (isPoly != true)
            this.convRational0Deriv(d0D);

        if (isPoly == true)
            return new CartesianPoint2D(d0D[0], d0D[1]);
        else
            return new HomogeneousPoint2D(d0D[0], d0D[1], d0D[2]);
    }

    /**
     * ���̋�?�̒[�_���Hٓ_?A�ϋȓ_�̉�ƂȂ邩�ǂ����𒲂ׂ�?B
     *
     * @param minParam  �Z�O�?���g�̎n�_�̃p���??[�^�l
     * @param maxParam  �Z�O�?���g��?I�_�̃p���??[�^�l
     * @param minDegree ��?�
     * @param seg       �Z�O�?���g�̃C���f�b�N�X
     * @param nvseg     �L��ȃZ�O�?���g��?�
     * @param paramVec  ���ێ?���郊�X�g
     */
    private void checkEndPoint(double minParam,
                               double maxParam,
                               int minDegree,
                               int seg,
                               int nvseg,
                               Vector paramVec) {
        int mno;
        if (degree() < minDegree) {
            if (isClosed() && (paramVec.size() == 0)) {
                paramVec.addElement(new Double(minParam));
            }
            if (seg != (nvseg - 1)) {
                for (mno = 0; mno < paramVec.size(); mno++)
                    if (identicalParameter(((Double) paramVec.elementAt(mno)).doubleValue(),
                            maxParam))
                        break;
                if (mno == paramVec.size())
                    paramVec.addElement(new Double(maxParam));
            }
        }
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * �Hٓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     */
    public PointOnCurve2D[] singular() {
        BsplineKnot.ValidSegmentInfo vsegInfo = knotData.validSegments();
        int nvseg = vsegInfo.nSegments();
        int numseg = nSegments();
        PureBezierCurve2D[] bzcs = toPureBezierCurveArray();
        int minDegree = 2;
        int ijk, klm, mno;
        Vector snglrParam = new Vector();

        for (ijk = 0; ijk < numseg; ijk++) {
            double minParam;
            double maxParam;
            if ((klm = vsegInfo.isValidSegment(ijk)) < 0) {
                // ?k��?���i�[����?B?k�ދ�Ԃ̃p���??[�^�𓾂�?B
                minParam = knotValueAt(degree() + ijk);
                maxParam = knotValueAt(degree() + ijk + 1);
                checkEndPoint(minParam, maxParam, minDegree, ijk, nvseg, snglrParam);
                continue;
            }

            // �Z�O�?���g�̗��[�_���ɉB��邩�ǂ����̌�?�
            minParam = vsegInfo.knotPoint(klm)[0];
            maxParam = vsegInfo.knotPoint(klm)[1];
            checkEndPoint(minParam, maxParam, minDegree, klm, nvseg, snglrParam);

            // �Z�O�?���g���ɉ⪂��邩���ׂ�
            PointOnCurve2D[] singularOnBzc;
            try {
                singularOnBzc = bzcs[klm].singular();
            } catch (IndefiniteSolutionException e) {
                // BezierCurve �S�̂�?k�ނ��Ă���?�?�?B
                // BezierCurve �̗��[�_�̃p���??[�^��?�߉�ɉB���?B(���łɉ�ɉB�BĂ���?H)
                continue;
            }

            // ��̈��?��𒲂ׂ�?B
            for (klm = 0; klm < singularOnBzc.length; klm++) {
                double candidateParam = (maxParam - minParam) *
                        singularOnBzc[klm].parameter() + minParam;
                for (mno = 0; mno < snglrParam.size(); mno++) {
                    if (identicalParameter(((Double) snglrParam.elementAt(mno)).
                            doubleValue(), candidateParam))
                        break;
                }
                if (mno == snglrParam.size())
                    snglrParam.addElement(new Double(candidateParam));
            }
        }

        int numberOfSolution = snglrParam.size();
        PointOnCurve2D[] singular =
                new PointOnCurve2D[numberOfSolution];
        for (ijk = 0; ijk < numberOfSolution; ijk++) {
            singular[ijk] =
                    new PointOnCurve2D(this,
                            ((Double) snglrParam.elementAt(ijk)).doubleValue(),
                            false);
        }
        return singular;
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * �ϋȓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     */
    public PointOnCurve2D[] inflexion() {
        BsplineKnot.ValidSegmentInfo vsegInfo = knotData.validSegments();
        int nvseg = vsegInfo.nSegments();
        int numseg = nSegments();
        PureBezierCurve2D[] bzcs = toPureBezierCurveArray();
        int minDegree = 3;
        int ijk, klm, mno;
        Vector inflxParam = new Vector();

        for (ijk = 0; ijk < numseg; ijk++) {
            double minParam;
            double maxParam;
            if ((klm = vsegInfo.isValidSegment(ijk)) < 0) {
                // ?k�� or �ȗ� 0 ��Ԃ̃p���??[�^�𓾂�?B
                minParam = knotValueAt(degree() + ijk);
                maxParam = knotValueAt(degree() + ijk + 1);
                checkEndPoint(minParam, maxParam, minDegree, ijk, nvseg, inflxParam);
                continue;
            }

            // �Z�O�?���g�̗��[�_���ɉB��邩�ǂ����̌�?�
            minParam = vsegInfo.knotPoint(klm)[0];
            maxParam = vsegInfo.knotPoint(klm)[1];
            checkEndPoint(minParam, maxParam, minDegree, klm, nvseg, inflxParam);

            // �Z�O�?���g���ɉ⪂��邩���ׂ�
            PointOnCurve2D[] inflexionOnBzc;
            try {
                inflexionOnBzc = bzcs[klm].inflexion();
            } catch (IndefiniteSolutionException e) {
                // BezierCurve �S�̂� �ȗ� 0 ��Ԃ�?�?�
                // BezierCurve �̗��[�_�̃p���??[�^��?�߉�ɉB���?B(���łɉB�BĂ���?)
                continue;
            }

            // ��̈��?��𒲂ׂ�?B
            for (klm = 0; klm < inflexionOnBzc.length; klm++) {
                double candidateParam = (maxParam - minParam) *
                        inflexionOnBzc[klm].parameter() + minParam;
                for (mno = 0; mno < inflxParam.size(); mno++) {
                    if (identicalParameter(((Double) inflxParam.elementAt(mno)).
                            doubleValue(), candidateParam))
                        break;
                }
                if (mno == inflxParam.size())
                    inflxParam.addElement(new Double(candidateParam));
            }
        }

        int numberOfSolution = inflxParam.size();
        PointOnCurve2D[] inflexion =
                new PointOnCurve2D[numberOfSolution];
        for (ijk = 0; ijk < numberOfSolution; ijk++) {
            inflexion[ijk] = new PointOnCurve2D
                    (this, ((Double) inflxParam.elementAt(ijk)).
                            doubleValue(), false);
        }
        return inflexion;
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?�?�̂���_ P(t) ����^����ꂽ�_�֌�x�N�g����
     * P(t) �ɂ�����?ڃx�N�g�� P'(t) �̓�?ς�\����?��� D(t) ��?�?���?A
     * �����?��ӂƂ����?���� D(t) = 0 ��⢂Ă���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     */
    public PointOnCurve2D[] projectFrom(Point2D mate) {
        int dimension = 2;
        int ijk, klm, mno, i;    // loop counter
        int coef_size = isPolynomial() ? dimension : dimension + 1;
        DoublePolynomial work0, work1, work2, sub;
        double[] work3;
        DoublePolynomial[] pointPoly;
        DoublePolynomial[] offsPoly = new DoublePolynomial[dimension];
        DoublePolynomial[] tangPoly = new DoublePolynomial[coef_size];
        DoublePolynomial[] dotePoly = new DoublePolynomial[dimension];

        BsplineKnot.ValidSegmentInfo vsegInfo = knotData.validSegments();
        PointOnCurve2D proj;
        PointOnGeometryList projList = new PointOnGeometryList();
        double dTol = getToleranceForDistance();
        ComplexPolynomial dtPoly;
        Complex[] root;
        double[] intv;
        ParameterDomain domain;
        double par;

        for (ijk = 0; ijk < vsegInfo.nSegments(); ijk++) {
            pointPoly = polynomial(vsegInfo.segmentNumber(ijk), isPolynomial());
            if (isRational()) {
                offsPoly[0] = pointPoly[dimension].scalarMultiply(mate.x());
                offsPoly[1] = pointPoly[dimension].scalarMultiply(mate.y());
            } else {
                double coef[][] = {{mate.x()}, {mate.y()}};
                offsPoly[0] = new DoublePolynomial(coef[0]);
                offsPoly[1] = new DoublePolynomial(coef[1]);
            }

            for (i = 0; i < dimension; i++)
                pointPoly[i] = (DoublePolynomial) pointPoly[i].subtract(offsPoly[i]);

            // polynomial of tangent vector
            for (klm = 0; klm < coef_size; klm++)
                tangPoly[klm] = (DoublePolynomial) pointPoly[klm].differentiate();

            /*
            * polynomial for position --> for difference with point A
            * polynomial for dot product
            */
            if (!isRational()) {
                for (klm = 0; klm < dimension; klm++)
                    dotePoly[klm] = (DoublePolynomial) pointPoly[klm].multiply(tangPoly[klm]);
            } else {
                for (klm = 0; klm < 2; klm++) {
                    work0 = (DoublePolynomial) pointPoly[2].multiply(tangPoly[klm]);
                    work1 = (DoublePolynomial) tangPoly[2].multiply(pointPoly[klm]);
                    // ((a * t^n) * (nb * t^(n-1))) - ((na * t^(n-1)) * (b * t^n)) == 0
                    work2 = (DoublePolynomial) work0.subtract(work1);
                    work3 = GeometryPrivateUtils.coefficientsBetween(work2, 0, (work2.degree() - 1));
                    sub = new DoublePolynomial(work3);
                    dotePoly[klm] = (DoublePolynomial) pointPoly[klm].multiply(sub);
                }
            }

            try {
                dtPoly = ((DoublePolynomial) dotePoly[0].add(dotePoly[1])).toComplexPolynomial();
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

            try {
                root = GeometryPrivateUtils.getRootsByDKA(dtPoly);
            } catch (GeometryPrivateUtils.DKANotConvergeException e) {
                root = e.getValues();
            } catch (GeometryPrivateUtils.ImpossibleEquationException e) {
                throw new FatalException();
            } catch (GeometryPrivateUtils.IndefiniteEquationException e) {
                throw new FatalException();
            }

            // extract proper roots
            intv = vsegInfo.knotPoint(ijk);
            domain = new ParameterDomain(false, intv[0], intv[1] - intv[0]);

            for (mno = 0; mno < root.length; mno++) {
                par = root[mno].real();
                if (!domain.isValid(par))
                    continue;
                par = domain.force(par);
                proj = checkProjection(par, mate, dTol * dTol);
                if (proj != null)
                    projList.addPoint(proj);
            }
        }

        return projList.toPointOnCurve2DArray();
    }

    /**
     * ���̗L��?�S�̂쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @return ���̋�?�S�̂�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve2D toBsplineCurve() {
        if (this.isRational() == true)
            return this;

        return new BsplineCurve2D(this.knotData,
                this.controlPoints,
                this.makeUniformWeights());
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
        BsplineCurve2D target;
        if (this.isPeriodic() == true) {
            if (pint.absIncrease() >= this.parameterDomain().section().absIncrease()) {
                target = this;
                try {
                    target = target.shiftIfPeriodic(pint.start());
                } catch (OpenCurveException e) {
                    ;    // �N���蓾�Ȃ��͂�
                }
                if (pint.increase() < 0.0)
                    target = target.reverse();
            } else {
                target = this.truncate(pint);
            }
        } else {
            target = this.truncate(pint);
        }
        return target.toBsplineCurve();
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
        return mate.intersect(this, true);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ����a�X�v���C���ƒ�?�̌�_��\����?����ɋA�������ĉ⢂Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        BsplineKnot.ValidSegmentInfo vsegInfo = knotData.validSegments();
        Axis2Placement2D placement =
                new Axis2Placement2D(mate.pnt(), mate.dir());
        CartesianTransformationOperator2D transform =
                new CartesianTransformationOperator2D(placement, 1.0);
        int uicp = nControlPoints();
        Point2D[] newCp = new Point2D[uicp];

        for (int i = 0; i < uicp; i++)
            newCp[i] = transform.toLocal(controlPointAt(i));

        double[] weights = weights();
        if (isRational()) {
            double max_weight = 0.0;
            for (int i = 0; i < uicp; i++)
                if (Math.abs(weights[i]) > max_weight)
                    max_weight = weights[i];

            if (max_weight > 0.0)
                for (int i = 0; i < uicp; i++) {
                    weights[i] /= max_weight;
                }
        }

        // ����?���_(newCp)��Bspline��?��
        BsplineCurve2D bsc = new
                BsplineCurve2D(knotData, newCp, weights);

        Vector lineParam = new Vector();
        Vector bscParam = new Vector();
        int nSeg = vsegInfo.nSegments();
        int k = 0;
        for (int i = 0; i < nSeg; i++) {
            DoublePolynomial[] realPoly =
                    bsc.polynomial(vsegInfo.segmentNumber(i), isPolynomial());
            ComplexPolynomial compPoly =
                    realPoly[1].toComplexPolynomial();

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

            // set new ParameterDomain

            // ��̃`�F�b�N
            for (int j = 0; j < nRoots; j++) {
                double realRoot = roots[j].real();
                if (bsc.parameterValidity(realRoot) ==
                        ParameterValidity.OUTSIDE)
                    continue;

                double[] knotParams = vsegInfo.knotPoint(i);
                if (realRoot < knotParams[0]) realRoot = knotParams[0];
                if (realRoot > knotParams[1]) realRoot = knotParams[1];

                Point2D workPoint = bsc.coordinates(realRoot);
                double dTol = bsc.getToleranceForDistance();
                int jj;
                if (Math.abs(workPoint.y()) < dTol) {
                    for (jj = 0; jj < k; jj++) {
                        double paramA = ((Double) lineParam.elementAt(jj)).doubleValue();
                        double paramB = ((Double) bscParam.elementAt(jj)).doubleValue();
                        if ((Math.abs(paramA - workPoint.x()) < dTol)
                                && (bsc.identicalParameter(paramA, paramB)))
                            break;
                    }
                    if (jj >= k) {
                        lineParam.addElement(new Double(workPoint.x()));
                        bscParam.addElement(new Double(realRoot));
                        k++;
                    }
                }
            }
        }

        int num = bscParam.size();
        IntersectionPoint2D[] intersectPoint = new
                IntersectionPoint2D[num];
        double mateLength = mate.dir().length();
        for (int i = 0; i < k; i++) {
            double work = ((Double) lineParam.elementAt(i)).doubleValue() / mateLength;
            PointOnCurve2D pointOnLine = new
                    PointOnCurve2D(mate, work, doCheckDebug);

            work = ((Double) bscParam.elementAt(i)).doubleValue();
            PointOnCurve2D pointOnBsc = new
                    PointOnCurve2D(this, work, doCheckDebug);

            Point2D coordinates = coordinates(work);

            if (!doExchange)
                intersectPoint[i] = new IntersectionPoint2D
                        (coordinates, pointOnBsc, pointOnLine, doCheckDebug);
            else
                intersectPoint[i] = new IntersectionPoint2D
                        (coordinates, pointOnLine, pointOnBsc, doCheckDebug);
        }

        return intersectPoint;
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �~�̃N���X��?u�~ vs. �a�X�v���C����?�?v�̌�_���Z�?�\�b�h
     * {@link Circle2D#intersect(BsplineCurve2D,boolean)
     * Circle2D.intersect(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �ȉ~�̃N���X��?u�ȉ~ vs. �a�X�v���C����?�?v�̌�_���Z�?�\�b�h
     * {@link Ellipse2D#intersect(BsplineCurve2D,boolean)
     * Ellipse2D.intersect(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?�̃N���X��?u��?� vs. �a�X�v���C����?�?v�̌�_���Z�?�\�b�h
     * {@link Parabola2D#intersect(BsplineCurve2D,boolean)
     * Parabola2D.intersect(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �o��?�̃N���X��?u�o��?� vs. �a�X�v���C����?�?v�̌�_���Z�?�\�b�h
     * {@link Hyperbola2D#intersect(BsplineCurve2D,boolean)
     * Hyperbola2D.intersect(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �|�����C���̃N���X��?u�|�����C�� vs. �a�X�v���C����?�?v�̌�_���Z�?�\�b�h
     * {@link Polyline2D#intersect(BsplineCurve2D,boolean)
     * Polyline2D.intersect(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBzcBsc2D#intersection(PureBezierCurve2D,BsplineCurve2D,boolean)
     * IntsBzcBsc2D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return IntsBzcBsc2D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBscBsc2D#intersection(BsplineCurve2D,BsplineCurve2D,boolean)
     * IntsBscBsc2D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        return IntsBscBsc2D.intersection(this, mate, doExchange);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �g������?�̃N���X��?u�g������?� vs. �a�X�v���C����?�?v�̌�_���Z�?�\�b�h
     * {@link TrimmedCurve2D#intersect(BsplineCurve2D,boolean)
     * TrimmedCurve2D.intersect(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �a�X�v���C����?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurveSegment2D#intersect(BsplineCurve2D,boolean)
     * CompositeCurveSegment2D.intersect(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̂a�X�v���C����?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�̃N���X��?u��?���?� vs. �a�X�v���C����?�?v�̌�_���Z�?�\�b�h
     * {@link CompositeCurve2D#intersect(BsplineCurve2D,boolean)
     * CompositeCurve2D.intersect(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L�E��?�̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ���?�̊�?̔z��
     */
    public CurveCurveInterference2D[] interfere(BoundedCurve2D mate) {
        return mate.interfere(this, true);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ?�̃N���X��?u?� vs. �a�X�v���C����?�?v�̊�?��Z�?�\�b�h
     * {@link BoundedLine2D#interfere(BsplineCurve2D,boolean)
     * BoundedLine2D.interfere(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(BoundedLine2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�|�����C��) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �|�����C���̃N���X��?u�|�����C�� vs. �a�X�v���C����?�?v�̊�?��Z�?�\�b�h
     * {@link Polyline2D#interfere(BsplineCurve2D,boolean)
     * Polyline2D.interfere(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�|�����C��)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(Polyline2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�x�W�G��?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBzcBsc2D#interference(PureBezierCurve2D,BsplineCurve2D,boolean)
     * IntsBzcBsc2D.interference}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�x�W�G��?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(PureBezierCurve2D mate,
                                         boolean doExchange) {
        return IntsBzcBsc2D.interference(mate, this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�a�X�v���C����?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * {@link IntsBscBsc2D#interference(BsplineCurve2D,BsplineCurve2D,boolean)
     * IntsBscBsc2D.interference}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�a�X�v���C����?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(BsplineCurve2D mate,
                                         boolean doExchange) {
        return IntsBscBsc2D.interference(this, mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�g������?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * �g������?�̃N���X��?u�g������?� vs. �a�X�v���C����?�?v�̊�?��Z�?�\�b�h
     * {@link TrimmedCurve2D#interfere(BsplineCurve2D,boolean)
     * TrimmedCurve2D.interfere(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (�g������?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(TrimmedCurve2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�Z�O�?���g) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�Z�O�?���g�̃N���X��?u��?���?�Z�O�?���g vs. �a�X�v���C����?�?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurveSegment2D#interfere(BsplineCurve2D,boolean)
     * CompositeCurveSegment2D.interfere(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�Z�O�?���g)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    CurveCurveInterference2D[] interfere(CompositeCurveSegment2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��?A
     * ��?���?�̃N���X��?u��?���?� vs. �a�X�v���C����?�?v�̊�?��Z�?�\�b�h
     * {@link CompositeCurve2D#interfere(BsplineCurve2D,boolean)
     * CompositeCurve2D.interfere(BsplineCurve2D, boolean)}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
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
    public BsplineCurve2D
    offsetByBsplineCurve(ParameterSection pint,
                         double magni,
                         int side,
                         ToleranceForDistance tol) {
        Ofst2D doObj = new Ofst2D(this, pint, magni, side, tol);
        return doObj.offset();
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
     * ���̋�?��?A�w��̃p���??[�^�l�̈ʒu��?V���ȃm�b�g��}���?��Ԃ�?B
     * <p/>
     * �`?�͂��̋�?�̂܂܂�?A�Z�O�?���g����?�����a�X�v���C����?��Ԃ�?B
     * </p>
     *
     * @param param �m�b�g��}���p���??[�^�l
     * @return �m�b�g�}���̂a�X�v���C����?�
     */
    public BsplineCurve2D insertKnot(double param) {
        double[][] cntlPnts;
        boolean isPoly = isPolynomial();
        Object[] result;
        BsplineKnot newKnotData;
        double[][] newControlPoints;
        BsplineCurve2D bsc;

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        result = BsplineCurveEvaluation.insertKnot(knotData, cntlPnts, param);
        newKnotData = (BsplineKnot) result[0];
        newControlPoints = (double[][]) result[1];
        return new BsplineCurve2D(newKnotData, newControlPoints);
    }

    /**
     * ���̂a�X�v���C����?��?A�^����ꂽ�p���??[�^�l�ŕ�������?B
     * <p/>
     * ���̋�?�J�����`����?�?��ɂ�?A
     * param �ɑΉ�����_�őO���{�ɕ�������?B
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f��?u�n�_���番���_�܂ł�\���a�X�v���C����?� A?v?A
     * ��Ԗڂ̗v�f��?u�����_����?I�_�܂ł�\���a�X�v���C����?� B?v
     * �����?B
     * ���̋�?�̃p���??[�^��`��� [start, end] �Ƃ����?A
     * A �̃p���??[�^��`��� [start, param]?A
     * B �̃p���??[�^��`��� [0, (end - param)] �ɂȂ�?B
     * </p>
     * <p/>
     * ���̋�?�����`����?�?��ɂ�?A
     * param �ɑΉ�����_�𗼒[�Ƃ���J�����`����
     * ��{�̂a�X�v���C����?� C �ɕϊ�����?B
     * ���ʂƂ��ē�����z��̗v�f?��� 1 �ł���?B
     * ���̋�?�̃p���??[�^��`��� [start, end] �Ƃ����?A
     * C �̃p���??[�^��`��� [0, (end - start)] �ɂȂ�?B
     * </p>
     * <p/>
     * param �̒l��?A���̂a�X�v���C����?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ������̂a�X�v���C����?��܂ޔz��
     * @see ParameterOutOfRange
     */
    public BsplineCurve2D[] divide(double param) {
        double[][] cntlPnts;
        boolean isPoly = isPolynomial();
        BsplineKnot[] newKnotData = new BsplineKnot[2];
        double[][][] newControlPoints = new double[2][][];
        int n_bsc;
        BsplineCurve2D[] bsc;

        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        BsplineCurveEvaluation.divide(knotData, cntlPnts, param,
                newKnotData, newControlPoints);
        if (newKnotData[0] == null)
            throw new FatalException();
        else if (newKnotData[1] == null)
            n_bsc = 1;
        else
            n_bsc = 2;

        bsc = new BsplineCurve2D[n_bsc];

        for (int i = 0; i < n_bsc; i++) {
            try {
                bsc[i] = new BsplineCurve2D(newKnotData[i], newControlPoints[i]);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        return bsc;
    }

    /**
     * ���̂a�X�v���C����?��?A�^����ꂽ�p���??[�^��Ԃ�?ؒf����?B
     * <p/>
     * section �̑?���l������?�?��ɂ�?A?i?s���]�����a�X�v���C����?��Ԃ�?B
     * </p>
     * <p/>
     * section �̒l��?A���̂a�X�v���C����?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     * <p/>
     * ?ؒf��̋�?�̃p���??[�^��`��� [0, section.absIncrease()] �ɂȂ�?B
     * </p>
     *
     * @param section ?ؒf���Ďc��������\���p���??[�^���
     * @return ?ؒf���Ďc����������\���a�X�v���C����?�
     * @see ParameterOutOfRange
     */
    public BsplineCurve2D truncate(ParameterSection section) {
        // �s?���Knot��?�?����Ă��܂����߃G��?[�Ƃ���?B
        if (Math.abs(section.increase()) <= getToleranceForParameter())
            throw new InvalidArgumentValueException();

        double start_par, end_par;
        BsplineCurve2D t_bsc;

        if (isNonPeriodic()) {    // open curve
            start_par = checkParameter(section.lower());
            end_par = checkParameter(section.upper());

            t_bsc = divide(start_par)[1];
            t_bsc = t_bsc.divide(end_par - start_par)[0];
        } else {        // closed curve
            double crv_intvl = parameterDomain().section().increase();
            double tol_p = ConditionOfOperation.getCondition().getToleranceForParameter();

            start_par = checkParameter(section.start());
            t_bsc = divide(start_par)[0];
            if (Math.abs(section.increase()) < crv_intvl - tol_p) {
                if (section.increase() > 0.0) {
                    end_par = section.increase();
                    t_bsc = t_bsc.divide(end_par)[0];
                } else {
                    end_par = crv_intvl + section.increase();
                    t_bsc = t_bsc.divide(end_par)[1];
                }
            }
        }

        if (section.increase() < 0.0)
            t_bsc = t_bsc.reverse();

        return t_bsc;
    }

    /**
     * ����?u�����`��?v�̂a�X�v���C����?�̌`?��ς�����?A
     * �^����ꂽ�p���??[�^�l�ɑΉ�����_��J�n�_�Ƃ���悤��
     * �ϊ�������̂�Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����a�X�v���C����?�̊J�n�_�̃p���??[�^�l��?�� 0 �ɂȂ�?B
     * </p>
     *
     * @param newStartParam �J�n�_�ƂȂ�p���??[�^�l
     * @return �^����ꂽ�p���??[�^�l�ɑΉ�����_��J�n�_�Ƃ���a�X�v���C����?�
     * @throws OpenCurveException ���̋�?�͊J�����`���ł���
     */
    public BsplineCurve2D shiftIfPeriodic(double newStartParam)
            throws OpenCurveException {
        if (this.isPeriodic() != true)
            throw new OpenCurveException();

        newStartParam = this.parameterDomain().wrap(newStartParam);

        // ?擪�ɂȂ�Z�O�?���g�̔�?��𓾂�
        int newFirstSegment =
                this.knotData().getSegmentNumberThatStartIsEqualTo(newStartParam);

        if (newFirstSegment == (-1))
            return this.insertKnot(newStartParam).shiftIfPeriodic(newStartParam);

        // �m�b�g�f?[�^��?�?�
        BsplineKnot newKnotData = this.knotData().shift(newFirstSegment);

        // ?���_���?�?�
        int nNewControlPoints = newKnotData.nControlPoints();
        Point2D[] newControlPoints = new Point2D[nNewControlPoints];

        for (int i = 0; i < nNewControlPoints; i++)
            newControlPoints[i] =
                    this.controlPointAt((i + newFirstSegment) % nNewControlPoints);

        // ?d�ݗ��?�?�
        double[] newWeights = null;
        if (this.isRational() == true) {
            newWeights = new double[nNewControlPoints];

            for (int i = 0; i < nNewControlPoints; i++)
                newWeights[i] =
                        this.weightAt((i + newFirstSegment) % nNewControlPoints);
        }

        return new BsplineCurve2D(newKnotData, newControlPoints, newWeights);
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
    public Polyline2D toPolyline(ParameterSection section,
                                 ToleranceForDistance tolerance) {
        BsplineKnot.ValidSegmentInfo vseg_info;
        int nseg;
        double lower_limit, upper_limit;
        double my_sp, my_ep;
        int my_sseg, my_eseg;
        PureBezierCurve2D[] bzcs;
        int no_total_pnts;
        SegmentInfo[] seg_infos;
        double[] kp;
        double bzc_sp, bzc_ep, bzc_ip;
        ParameterSection lpint;
        Polyline2D lpol;
        Point2D[] pnts;
        PointOnCurve2D lpnt;
        Point2D pnt;
        double param;
        int npnts;
        double tol_p = ConditionOfOperation.getCondition().getToleranceForParameter();
        int i, j, m;

        /*
        * check the parametric consistency
        */
        vseg_info = knotData.validSegments();
        nseg = vseg_info.nSegments();
        lower_limit = vseg_info.knotPoint(0)[0];
        upper_limit = vseg_info.knotPoint(nseg - 1)[1];

        if (isPeriodic()) {
            /*
            * closed curve
            */
            BsplineCurve2D o_bsc;    /* open curve */
            double par_intvl = upper_limit - lower_limit;

            my_sp = checkParameter(section.start());

            o_bsc = divide(my_sp)[0];
            if (section.increase() > 0.0)
                my_sp = lower_limit;
            else
                my_sp = upper_limit;

            lpint = new ParameterSection(my_sp, section.increase());
            lpol = o_bsc.toPolyline(lpint, tolerance);

            pnts = new Point2D[npnts = lpol.nPoints()];
            for (i = 0; i < npnts; i++) {
                lpnt = (PointOnCurve2D) lpol.pointAt(i);
                pnt = new CartesianPoint2D(lpnt.x(), lpnt.y());
                param = lpnt.parameter() + section.start();
                if (param > upper_limit) param -= upper_limit;
                pnts[i] = new PointOnCurve2D(pnt, this, param, doCheckDebug);
            }

            return new Polyline2D(pnts);
        }

        my_sp = checkParameter(section.lower());
        my_ep = checkParameter(section.upper());

        my_sseg = vseg_info.segmentIndex(my_sp);
        my_eseg = vseg_info.segmentIndex(my_ep);

        /*
        * exchange Bspline Curve to Bezier Curves
        */
        bzcs = toPureBezierCurveArray();

        /*
        * solution in bezier form
        */
        no_total_pnts = 0;
        seg_infos = new SegmentInfo[nseg];

        for (i = my_sseg; i <= my_eseg; i++) {
            kp = vseg_info.knotPoint(i);

            seg_infos[i] = new SegmentInfo(kp[0], kp[1]);

            if (i == my_sseg)
                bzc_sp = (my_sp - seg_infos[i].lp()) / seg_infos[i].dp();
            else
                bzc_sp = 0.0;

            if (i == my_eseg)
                bzc_ep = (my_ep - seg_infos[i].lp()) / seg_infos[i].dp();
            else
                bzc_ep = 1.0;

            if ((bzc_ip = bzc_ep - bzc_sp) < tol_p) {
                my_eseg = i - 1;
                break;
            }

            lpint = new ParameterSection(bzc_sp, bzc_ip);
            try {
                lpol = bzcs[i].toPolyline(lpint, tolerance);
            } catch (ZeroLengthException e) {
                continue;
            }
            seg_infos[i].pnts(lpol.points());

            no_total_pnts += seg_infos[i].nPnts();
            if (i > my_sseg)
                no_total_pnts--;
        }

        /*
        * solution in bspline form
        */
        if (no_total_pnts < 2)
            throw new ZeroLengthException();

        pnts = new Point2D[no_total_pnts];

        boolean first = true;
        for (i = my_sseg, m = 0; i <= my_eseg; i++) {
            if (first)
                j = 0;
            else
                j = 1;
            for (; j < seg_infos[i].nPnts(); j++, m++) {
                lpnt = (PointOnCurve2D) seg_infos[i].pnts(j);
                pnt = new CartesianPoint2D(lpnt.x(), lpnt.y());
                param = seg_infos[i].lp() + (seg_infos[i].dp() * lpnt.parameter());
                pnts[m] = new PointOnCurve2D(pnt, this, param, doCheckDebug);
                first = false;
            }
        }
        if (section.increase() > 0.0) {
            return new Polyline2D(pnts);
        } else {
            return new Polyline2D(pnts).reverse();
        }
    }

    /**
     * ����Z�O�?���g��?�ߎ�����_���?���\���Ք�N���X?B
     */
    private class SegmentInfo {
        /**
         * �Z�O�?���g�̊J�n�p���??[�^�l?B
         */
        private double lp;

        /**
         * �Z�O�?���g��?I���p���??[�^�l?B
         */
        private double up;

        /**
         * �Z�O�?���g�̑?���p���??[�^�l?B
         */
        private double dp;

        /**
         * �Z�O�?���g��?�ߎ�����_��?B
         */
        private Point2D[] pnts;

        /**
         * �J�n�p���??[�^�l��?I���p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param lp �J�n�p���??[�^�l
         * @param up ?I���p���??[�^�l
         */
        private SegmentInfo(double lp, double up) {
            this.lp = lp;
            this.up = up;
            this.dp = up - lp;
        }

        /**
         * �Z�O�?���g��?�ߎ�����_���?ݒ肷��?B
         *
         * @param pnts �Z�O�?���g��?�ߎ�����_��
         */
        private void pnts(Point2D[] pnts) {
            this.pnts = pnts;
        }

        /**
         * �Z�O�?���g�̊J�n�p���??[�^�l��Ԃ�?B
         *
         * @return �Z�O�?���g�̊J�n�p���??[�^�l
         */
        private double lp() {
            return lp;
        }

        /**
         * �Z�O�?���g��?I���p���??[�^�l��Ԃ�?B
         *
         * @return �Z�O�?���g��?I���p���??[�^�l
         */
        private double up() {
            return up;
        }

        /**
         * �Z�O�?���g�̑?���p���??[�^�l��Ԃ�?B
         *
         * @return �Z�O�?���g�̑?���p���??[�^�l
         */
        private double dp() {
            return dp;
        }

        /**
         * �Z�O�?���g��?�ߎ�����_��̓_��?���Ԃ�?B
         * <p/>
         * �_��?ݒ肳��Ă��Ȃ�?�?��ɂ� 0 ��Ԃ�?B
         * </p>
         *
         * @return �Z�O�?���g��?�ߎ�����_��̓_��?�
         */
        private int nPnts() {
            if (pnts == null)
                return 0;
            return pnts.length;
        }

        /**
         * �Z�O�?���g��?�ߎ�����_��� n �Ԗڂ̓_��Ԃ�?B
         *
         * @return �Z�O�?���g��?�ߎ�����_��� n �Ԗڂ̓_
         */
        private Point2D pnts(int n) {
            return pnts[n];
        }
    }

    /**
     * ���̂a�X�v���C����?��?Č�����x�W�G��?�̗��Ԃ�?B
     * <p/>
     * ���̋�?��?A�p���??[�^�I��?k�ނ��Ă��Ȃ��L��Z�O�?���g�ɑΉ�����x�W�G��?�̗��Ԃ�?B
     * </p>
     *
     * @return �x�W�G��?�̔z��
     */
    public PureBezierCurve2D[] toPureBezierCurveArray() {
        double[][] cntlPnts;
        boolean isPoly = isPolynomial();
        double[][][] bzc_array;
        PureBezierCurve2D[] bzcs;

        cntlPnts = toDoubleArray(isPoly);
        bzc_array = BsplineCurveEvaluation.toBezierCurve(knotData, cntlPnts);
        bzcs = new PureBezierCurve2D[bzc_array.length];
        for (int i = 0; i < bzc_array.length; i++) {
            try {
                bzcs[i] = new PureBezierCurve2D(bzc_array[i]);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        return bzcs;
    }

    /**
     * ���̂a�X�v���C����?��?i?s���𔽓]�������a�X�v���C����?��Ԃ�?B
     *
     * @return ���]��̂a�X�v���C����?�
     */
    BsplineCurve2D reverse() {
        BsplineKnot rKd;
        boolean isRat = isRational();
        int uicp = nControlPoints();
        Point2D[] rCp = new Point2D[uicp];
        double[] rWt = null;
        int i, j;

        rKd = knotData.reverse();
        if (isRat)
            rWt = new double[uicp];
        for (i = 0, j = uicp - 1; i < uicp; i++, j--) {
            rCp[i] = controlPointAt(j);
            if (isRat)
                rWt[i] = weightAt(j);
        }
        return new BsplineCurve2D(rKd, rCp, rWt);
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     *
     * @return �p���??[�^��`��
     * @see BsplineKnot#getParameterDomain()
     */
    ParameterDomain getParameterDomain() {
        return knotData.getParameterDomain();
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋�?�̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l�����̋�?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @return �K�v�ɉ����Ă��̋�?�̒�`���Ɋۂ߂�ꂽ�p���??[�^�l
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterDomain#force(double)
     * @see ParameterDomain#wrap(double)
     * @see ParameterOutOfRange
     */
    private double checkParameter(double param) {
        checkValidity(param);
        return parameterDomain().force(parameterDomain().wrap(param));
    }

    /**
     * ���̋�?��?A�`?�⻂̂܂܂ɂ���?A��?�����?グ����?��Ԃ�?B
     *
     * @return ����`?��?A��?������?オ�B���?�
     */
    public BsplineCurve2D elevateOneDegree() {
        BsplineKnot oldKnotData = this.knotData();
        double[][] oldControlPoints = this.toDoubleArray(this.isPolynomial());

        BsplineKnot newKnotData =
                BsplineCurveEvaluation.
                        getNewKnotDataAtDegreeElevation(oldKnotData);

        double[][] newControlPoints =
                BsplineCurveEvaluation.
                        getNewControlPointsAtDegreeElevation(oldKnotData,
                                newKnotData,
                                oldControlPoints);

        return new BsplineCurve2D(newKnotData, newControlPoints);
    }

    /**
     * ���̂a�X�v���C����?��?A
     * ���̂a�X�v���C����?��?I���_�Ɋ􉽓I�Ɍq���BĂ���a�X�v���C����?��
     * ��{�̂a�X�v���C����?�ɂ���?B
     *
     * @param mate ���̋�?��?I���_�Ɍq�����?�
     * @return this ��?I���_�� mate �̊J�n�_��q���ň�{�ɂ�����?�
     * @throws TwoGeomertiesAreNotContinuousException
     *          this ��?I���_�� mate �̊J�n�_����v���Ȃ�
     */
    public BsplineCurve2D mergeIfContinuous(BsplineCurve2D mate)
            throws TwoGeomertiesAreNotContinuousException {
        BsplineCurve2D headCurve = this;
        BsplineCurve2D tailCurve = mate;

        ParameterSection headSection = headCurve.parameterDomain().section();
        ParameterSection tailSection = tailCurve.parameterDomain().section();
        double headEndParameter = headSection.end();
        double tailStartParameter = tailSection.start();

        // �q���BĂ��邩?H
        Point2D headEnd = headCurve.coordinates(headEndParameter);
        Point2D tailStart = tailCurve.coordinates(tailStartParameter);
        if (headEnd.identical(tailStart) != true) {
            // debug
            // headEnd.output(System.err);
            // tailStart.output(System.err);
            throw new TwoGeomertiesAreNotContinuousException();
        }

        // �L�?���ǂ�����?��킹��
        boolean headPoly = headCurve.isPolynomial();
        boolean tailPoly = tailCurve.isPolynomial();
        boolean isPoly;

        if ((headPoly == true) && (tailPoly == true)) {
            isPoly = true;
        } else if (headPoly == true) {
            isPoly = false;
            headCurve = headCurve.toBsplineCurve();
        } else if (tailPoly == true) {
            isPoly = false;
            tailCurve = tailCurve.toBsplineCurve();
        } else {
            isPoly = false;
        }

        // ��?���?��킹��
        int headDegree = headCurve.degree();
        int tailDegree = tailCurve.degree();

        while (headDegree < tailDegree) {
            headCurve = headCurve.elevateOneDegree();
            headDegree++;
        }

        while (headDegree > tailDegree) {
            tailCurve = tailCurve.elevateOneDegree();
            tailDegree++;
        }

        // ���ꂼ���?�?��ʒu�ŕ�������?A�m�b�g�̑�?d�x��?グ��
        BsplineCurve2D[] dividedCurves;
        dividedCurves = headCurve.divide(headEndParameter);
        headCurve = dividedCurves[0];
        dividedCurves = tailCurve.divide(tailStartParameter);
        tailCurve = dividedCurves[1];

        // debug
        // headCurve.output(System.err);
        // tailCurve.output(System.err);

        // ?V������?�̃m�b�g���p�ӂ���
        BsplineKnot headKnotData = headCurve.knotData();
        BsplineKnot tailKnotData = tailCurve.knotData();

        int arrayLength;

        arrayLength = headKnotData.nKnots() + tailKnotData.nKnots() - 1;
        double[] newKnots = new double[arrayLength];
        int[] newKnotMultiplicities = new int[arrayLength];

        int nNewKnots = 0;
        for (int j = 0; j < headKnotData.nKnots(); j++) {
            newKnots[nNewKnots] = headKnotData.knotAt(j);
            newKnotMultiplicities[nNewKnots] = headKnotData.knotMultiplicityAt(j);
            nNewKnots++;
        }
        newKnotMultiplicities[nNewKnots - 1] = headDegree;

        double offset = headEndParameter - tailStartParameter;
        for (int j = 1; j < tailKnotData.nKnots(); j++) {
            newKnots[nNewKnots] = tailKnotData.knotAt(j) + offset;
            newKnotMultiplicities[nNewKnots] = tailKnotData.knotMultiplicityAt(j);
            nNewKnots++;
        }

        // ?V������?��?���_���p�ӂ���
        arrayLength = headKnotData.nControlPoints() + tailKnotData.nControlPoints() - 1;
        Point2D[] newControlPoints = new Point2D[arrayLength];
        double[] newWeights = null;
        if (isPoly != true)
            newWeights = new double[arrayLength];

        int nNewControlPoints = 0;
        for (int j = 0; j < headKnotData.nControlPoints(); j++) {
            newControlPoints[nNewControlPoints] = headCurve.controlPointAt(j);
            if (isPoly != true)
                newWeights[nNewControlPoints] = headCurve.weightAt(j);
            nNewControlPoints++;
        }

        double weightRatio = 0;
        if (isPoly != true)
            weightRatio = newWeights[nNewControlPoints - 1] / tailCurve.weightAt(0);

        for (int j = 1; j < tailKnotData.nControlPoints(); j++) {
            newControlPoints[nNewControlPoints] = tailCurve.controlPointAt(j);
            if (isPoly != true)
                newWeights[nNewControlPoints] = tailCurve.weightAt(j) * weightRatio;
            nNewControlPoints++;
        }

        // ?V������?��?�?�����
        BsplineCurve2D result;

        if (isPoly == true)
            result = new BsplineCurve2D(headDegree, false,
                    newKnotMultiplicities, newKnots,
                    newControlPoints);
        else
            result = new BsplineCurve2D(headDegree, false,
                    newKnotMultiplicities, newKnots,
                    newControlPoints, newWeights);

        return result;
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#BSPLINE_CURVE_2D ParametricCurve2D.BSPLINE_CURVE_2D}
     */
    int type() {
        return BSPLINE_CURVE_2D;
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
        Point2D[] tControlPoints =
                Point2D.transform(this.controlPoints,
                        reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new BsplineCurve2D(this.knotData, tControlPoints, this.weights);
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
        StringBuffer buf = new StringBuffer();

        writer.println(indent_tab + getClassName());
        // output knotData(BsplineKnot)
        //knotData.output(writer, indent + 2);
        knotData.output(writer, indent, 0);

        // output others
        writer.println(indent_tab + "\tcontrolPoints");
        for (int i = 0; i < nControlPoints(); i++) {
            controlPointAt(i).output(writer, indent + 2);
        }
        if (weights() != null) {
            writer.println(indent_tab + "\tweights ");
            int i = 0;
            while (true) {
                for (int j = 0; j < 10 && i < weights().length; j++, i++) {
                    writer.print(" " + weightAt(i));
                }
                writer.println();
                if (i < weights().length) {
                    writer.print(indent_tab + "\t");
                } else {
                    break;
                }
            }
        }
        writer.println(indent_tab + "\tcurveForm\t" + BsplineCurveForm.toString(curveForm));
        writer.println(indent_tab + "End");
    }
}
