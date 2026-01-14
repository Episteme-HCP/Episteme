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
import java.util.Enumeration;
import java.util.Vector;

/**
 * �R���� : ��L�? (��?���) �a�X�v���C���Ȗʂ���їL�?�a�X�v���C���Ȗʂ�\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �a�X�v���C���� U/V ���̃m�b�g��Ɋւ���?�� uKnotData/vKnotData
 * ({@link BsplineKnot BsplineKnot})
 * ��?��?B
 * ?���_��Ȃǂ�ێ?����t�B?[���h�ɂ��Ă�?A
 * {@link FreeformSurfaceWithControlPoints3D �X?[�p?[�N���X�̉�?�} ��Q?�?B
 * </p>
 * <p/>
 * �a�X�v���C���Ȗʂ� U/V ���ꂼ��̕��̃p���??[�^��`���?A
 * �Ή�����m�b�g��ɂ�BČ��܂�?B
 * </p>
 * <p/>
 * (u, v) ��p���??[�^�Ƃ���a�X�v���C���Ȗ� P(u, v) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	m = U ���̎�?�
 * 	n = V ���̎�?�
 * 	p = U ����?���_��?�
 * 	q = V ����?���_��?�
 * 	K = U ���̃Z�O�?���g��?� (U ���J�����`�� : (p - m), U �������`�� : p)
 * 	L = V ���̃Z�O�?���g��?� (V ���J�����`�� : (q - n), V �������`�� : q)
 * 	di,j = controlPoints[i][j]
 * 	wi,j = weights[i][j]
 * </pre>
 * �Ƃ���?A��L�?�a�X�v���C���Ȗʂ�
 * <pre>
 * 	P(u, v) = ((di,j * Nm,i(u)) �̑?�a) * Nn,j(v) �̑?�a	(i = 0, ..., K+m-1, j = 0, ..., L+n-1)
 * </pre>
 * �L�?�a�X�v���C���Ȗʂ�
 * <pre>
 * 		  ((wi,j * di,j * Nm,i(u)) �̑?�a) * Nn,j(v) �̑?�a
 * 	P(u, v) = ------------------------------------------------- 	(i = 0, ..., K+m-1, j = 0, ..., L+n-1)
 * 		  ((wi,j * Nm,i(u)) �̑?�a) * Nn,j(v) �̑?�a
 * </pre>
 * ������ Nm,i(u), Nn,j(v) �͂a�X�v���C������?�?B
 * �Ȃ�?A
 * U ���ɕ����`����?�?��� i &gt; (p - 1) �ƂȂ� i �ɂ��Ă�?A
 * �Ή�����?���_��?d�݂����ꂼ�� dk, wk (k = i - p) �ƂȂ�?B
 * ���l��?A
 * V ���ɕ����`����?�?��� j &gt; (q - 1) �ƂȂ� j �ɂ��Ă�?A
 * �Ή�����?���_��?d�݂����ꂼ�� dl, wl (l = j - q) �ƂȂ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:07 $
 */

public class BsplineSurface3D extends FreeformSurfaceWithControlPoints3D {
    /**
     * U ���̃m�b�g��?B
     *
     * @serial
     */
    private final BsplineKnot uKnotData;

    /**
     * V ���̃m�b�g��?B
     *
     * @serial
     */
    private final BsplineKnot vKnotData;

    /**
     * �Ȗʂ̌`?�R����t���O?B
     * <p/>
     * ���̃t�B?[���h��?A���܂̂Ƃ��늈�p����Ă��炸?A
     * ?�� BsplineSurfaceForm.UNSPECIFIED �ɂȂBĂ���?B
     * </p>
     *
     * @serial
     * @see BsplineSurfaceForm
     */
    private int surfaceForm = BsplineSurfaceForm.UNSPECIFIED;

    /**
     * �m�b�g��𖾎���?A
     * ?���_���^���đ�?����ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(Point3D[][])
     * super}(controlPoints)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * uKnotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(uDegree, KnotType.UNSPECIFIED, uPeriodic, uKnotMultiplicities, uKnots, controlPoints.length)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * vKnotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(vDegree, KnotType.UNSPECIFIED, vPeriodic, vKnotMultiplicities, vKnots, controlPoints[0].length)
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param uDegree             U ���̎�?�
     * @param uPeriodic           U �������`�����ۂ���\���t���O
     * @param uKnotMultiplicities U ���̃m�b�g��?d�x�̔z��
     * @param uKnots              U ���̃m�b�g�l�̔z��
     * @param vDegree             V ���̎�?�
     * @param vPeriodic           V �������`�����ۂ���\���t���O
     * @param vKnotMultiplicities V ���̃m�b�g��?d�x�̔z��
     * @param vKnots              V ���̃m�b�g�l�̔z��
     * @param controlPoints       ?���_�̔z��
     */
    public BsplineSurface3D(int uDegree, boolean uPeriodic,
                            int[] uKnotMultiplicities, double[] uKnots,
                            int vDegree, boolean vPeriodic,
                            int[] vKnotMultiplicities, double[] vKnots,
                            Point3D[][] controlPoints) {
        super(controlPoints);
        uKnotData = new BsplineKnot(uDegree, KnotType.UNSPECIFIED, uPeriodic,
                uKnotMultiplicities, uKnots,
                controlPoints.length);
        vKnotData = new BsplineKnot(vDegree, KnotType.UNSPECIFIED, vPeriodic,
                vKnotMultiplicities, vKnots,
                controlPoints[0].length);
    }

    /**
     * �m�b�g��𖾎�������
     * �m�b�g��̎�ʂ�?���_���^���đ�?����ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���܂̂Ƃ���?AuKnotSpec/vKnotSpec ���Ƃ蓾��l�� KnotType.UNIFORM_KNOTS �����ł���
     * (KnotType.{QUASI_UNIFORM_KNOTS, PIECEWISE_BEZIER_KNOTS} �ɂ͖��Ή�) ?B
     * </p>
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(Point3D[][])
     * super}(controlPoints)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * uKnotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(uDegree, uKnotSpec, uPeriodic, null, null, controlPoints.length)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * vKnotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(vDegree, vKnotSpec, vPeriodic, null, null, controlPoints[0].length)
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param uDegree       U ���̎�?�
     * @param uPeriodic     U �������`�����ۂ���\���t���O
     * @param uKnotSpec     U ���̃m�b�g��̎��
     * @param vDegree       V ���̎�?�
     * @param vPeriodic     V �������`�����ۂ���\���t���O
     * @param vKnotSpec     V ���̃m�b�g��̎��
     * @param controlPoints ?���_�̔z��
     */
    public BsplineSurface3D(int uDegree, boolean uPeriodic, int uKnotSpec,
                            int vDegree, boolean vPeriodic, int vKnotSpec,
                            Point3D[][] controlPoints) {
        super(controlPoints);
        uKnotData = new BsplineKnot(uDegree, uKnotSpec, uPeriodic, null, null,
                controlPoints.length);
        vKnotData = new BsplineKnot(vDegree, vKnotSpec, vPeriodic, null, null,
                controlPoints[0].length);
    }

    /**
     * �m�b�g��𖾎���?A
     * ?���_���?d�ݗ��^���ėL�?�ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(Point3D[][],double[][])
     * super}(controlPoints, weights)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * uKnotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(uDegree, KnotType.UNSPECIFIED, uPeriodic, uKnotMultiplicities, uKnots, controlPoints.length)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * vKnotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(vDegree, KnotType.UNSPECIFIED, vPeriodic, vKnotMultiplicities, vKnots, controlPoints[0].length)
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param uDegree             U ���̎�?�
     * @param uPeriodic           U �������`�����ۂ���\���t���O
     * @param uKnotMultiplicities U ���̃m�b�g��?d�x�̔z��
     * @param uKnots              U ���̃m�b�g�l�̔z��
     * @param vDegree             V ���̎�?�
     * @param vPeriodic           V �������`�����ۂ���\���t���O
     * @param vKnotMultiplicities V ���̃m�b�g��?d�x�̔z��
     * @param vKnots              V ���̃m�b�g�l�̔z��
     * @param controlPoints       ?���_�̔z��
     * @param weights             ?d�݂̔z��
     */
    public BsplineSurface3D(int uDegree, boolean uPeriodic,
                            int[] uKnotMultiplicities, double[] uKnots,
                            int vDegree, boolean vPeriodic,
                            int[] vKnotMultiplicities, double[] vKnots,
                            Point3D[][] controlPoints, double[][] weights) {
        super(controlPoints, weights);
        uKnotData = new BsplineKnot(uDegree, KnotType.UNSPECIFIED, uPeriodic,
                uKnotMultiplicities, uKnots,
                controlPoints.length);
        vKnotData = new BsplineKnot(vDegree, KnotType.UNSPECIFIED, vPeriodic,
                vKnotMultiplicities, vKnots,
                controlPoints[0].length);
    }

    /**
     * �m�b�g��𖾎�������
     * �m�b�g��̎��?A?���_���?d�ݗ��^���ėL�?�ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���܂̂Ƃ���?AuKnotSpec/vKnotSpec ���Ƃ蓾��l�� KnotType.UNIFORM_KNOTS �����ł���
     * (KnotType.{QUASI_UNIFORM_KNOTS, PIECEWISE_BEZIER_KNOTS} �ɂ͖��Ή�) ?B
     * </p>
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(Point3D[][],double[][])
     * super}(controlPoints, weights)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * uKnotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(uDegree, uKnotSpec, uPeriodic, null, null, controlPoints.length)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * vKnotData ��?\�z�ɂ�?A
     * {@link BsplineKnot#BsplineKnot(int,int,boolean,int[],double[],int)
     * new BsplineKnot}(vDegree, vKnotSpec, vPeriodic, null, null, controlPoints[0].length)
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param uDegree       U ���̎�?�
     * @param uPeriodic     U �������`�����ۂ���\���t���O
     * @param uKnotSpec     U ���̃m�b�g��̎��
     * @param vDegree       V ���̎�?�
     * @param vPeriodic     V �������`�����ۂ���\���t���O
     * @param vKnotSpec     V ���̃m�b�g��̎��
     * @param controlPoints ?���_�̔z��
     * @param weights       ?d�݂̔z��
     */
    public BsplineSurface3D(int uDegree, boolean uPeriodic, int uKnotSpec,
                            int vDegree, boolean vPeriodic, int vKnotSpec,
                            Point3D[][] controlPoints, double[][] weights) {
        super(controlPoints, weights);
        uKnotData = new BsplineKnot(uDegree, uKnotSpec, uPeriodic, null, null,
                controlPoints.length);
        vKnotData = new BsplineKnot(vDegree, vKnotSpec, vPeriodic, null, null,
                controlPoints[0].length);
    }

    /**
     * �m�b�g��� BsplineKnot �̃I�u�W�F�N�g�Ƃ��ēn��?A
     * ?���_ (��?d��) ��O�����z��ŗ^����
     * ��?����Ȗ� (���邢�͗L�?�Ȗ�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(double[][][])
     * super}(cpArray)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * uKnotData/vKnotData �̎���?���_��?��� cpArray �̎���?���_��?�����v���Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param uKnotData U ���̃m�b�g��
     * @param vKnotData V ���̃m�b�g��
     * @param cpArray   ?���_ (�����?d��) �z��
     * @see InvalidArgumentValueException
     */
    BsplineSurface3D(BsplineKnot uKnotData, BsplineKnot vKnotData,
                     double[][][] cpArray) {
        super(cpArray);

        if ((uKnotData.nControlPoints() != uNControlPoints()) ||
                (vKnotData.nControlPoints() != vNControlPoints()))
            throw new InvalidArgumentValueException();

        /*
        * ��?�ň����implicit?A����explicit�ȃm�b�g��?�Ȗʂ�
        * �R���X�g���N�^?[���Ȃ�����?A���̂悤�ȋȖʂ�?��Ȃ��悤�ɂ��Ă���?B
        */
        if (uKnotData.knotSpec() == KnotType.UNSPECIFIED ||
                vKnotData.knotSpec() == KnotType.UNSPECIFIED) {
            uKnotData = uKnotData.makeExplicit();
            vKnotData = vKnotData.makeExplicit();
        }

        this.uKnotData = uKnotData;
        this.vKnotData = vKnotData;
    }

    /**
     * �m�b�g��� BsplineKnot �̃I�u�W�F�N�g�Ƃ��ēn��?A
     * ?���_���?d�ݗ��^����
     * ��?����Ȗ� (���邢�͗L�?�Ȗ�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(Point3D[][],double[][],boolean)
     * super}(controlPoints, weights, false)
     * ��Ă�?o���Ă���?B
     * </p>
     * <p/>
     * ���̃R���X�g���N�^�ł͈�?��̃`�F�b�N��?s�Ȃ�Ȃ��̂�?A���p�ɂ͒?�ӂ��K�v�ł���?B
     * </p>
     *
     * @param uKnotData     U ���̃m�b�g��
     * @param vKnotData     V ���̃m�b�g��
     * @param controlPoitns ?���_�̔z��
     * @param weights       ?d�݂̔z��
     */
    BsplineSurface3D(BsplineKnot uKnotData, BsplineKnot vKnotData,
                     Point3D[][] controlPoints, double[][] weights) {
        super(controlPoints, weights, false);

        /*
        * ��?�ň����implicit?A����explicit�ȃm�b�g��?�Ȗʂ�
        * �R���X�g���N�^?[���Ȃ�����?A���̂悤�ȋȖʂ�?��Ȃ��悤�ɂ��Ă���?B
        */
        if (uKnotData.knotSpec() == KnotType.UNSPECIFIED ||
                vKnotData.knotSpec() == KnotType.UNSPECIFIED) {
            uKnotData = uKnotData.makeExplicit();
            vKnotData = vKnotData.makeExplicit();
        }

        this.uKnotData = uKnotData;
        this.vKnotData = vKnotData;
    }

    /**
     * �ʑ��I�Ɋi�q?�̓_�Ԃ�?AU/V ���ꂼ��̕��̓_��ɑΉ�����p���??[�^�l�̗��^����?A
     * ���̓_�Ԃ��Ԃ��鑽?����ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * uIsClosed �� true ��?�?�?A
     * (uParams �̗v�f?�) = (points �� U ���̗v�f?� + 1) �ƂȂBĂ���K�v������?B
     * ���l��?A
     * vIsClosed �� true ��?�?�?A
     * (vParams �̗v�f?�) = (points �� V ���̗v�f?� + 1) �ƂȂBĂ���K�v������?B
     * </p>
     *
     * @param points    ��Ԃ���_��
     * @param uParams   �_�ԓ�� U ���̓_��̊e�_�ɂ�����p���??[�^�l�̗�
     * @param vParams   �_�ԓ�� V ���̓_��̊e�_�ɂ�����p���??[�^�l�̗�
     * @param uIsClosed U �������`�����ۂ�
     * @param vIsClosed V �������`�����ۂ�
     */
    public BsplineSurface3D(Point3D[][] points,
                            double[] uParams, double[] vParams,
                            boolean uIsClosed, boolean vIsClosed) {
        super();
        InterpolationSurface3D doObj = new InterpolationSurface3D(points, uParams, vParams,
                uIsClosed, vIsClosed);
        this.controlPoints = doObj.controlPoints();
        this.uKnotData = doObj.uKnotData();
        this.vKnotData = doObj.vKnotData();
        this.weights = doObj.weights();    // may be null
    }

    /**
     * �ʑ��I�Ɋi�q?�̓_�Ԃ�?AU/V ���ꂼ��̕��̓_��ɑΉ�����p���??[�^�l�̗��^����?A
     * ���̓_�Ԃ�ߎ����鑽?����ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * uIsClosed �� true ��?�?�?A
     * (uParams �̗v�f?�) = (points �� U ���̗v�f?� + 1) �ƂȂBĂ���K�v������?B
     * ���l��?A
     * vIsClosed �� true ��?�?�?A
     * (vParams �̗v�f?�) = (points �� V ���̗v�f?� + 1) �ƂȂBĂ���K�v������?B
     * </p>
     * <p/>
     * �w�肳�ꂽ?��x�ŋߎ��ł��Ȃ��B�?�?��ɂ�?A
     * �^����ꂽ�_�Ԃ��Ԃ���Ȗʂ�?�?�����?B
     * </p>
     *
     * @param points    �ߎ�����_��
     * @param uParams   �_�ԓ�� U ���̓_��̊e�_�ɂ�����p���??[�^�l�̗�
     * @param vParams   �_�ԓ�� V ���̓_��̊e�_�ɂ�����p���??[�^�l�̗�
     * @param uIsClosed U �������`�����ۂ�
     * @param vIsClosed V �������`�����ۂ�
     * @param tol       �ߎ���?��x
     */
    public BsplineSurface3D(Point3D[][] points,
                            double[] uParams, double[] vParams,
                            boolean uIsClosed, boolean vIsClosed,
                            ToleranceForDistance tol) {
        super();
        ApproximationSurface3D doObj = new ApproximationSurface3D(points, uParams, vParams,
                uIsClosed, vIsClosed);
        BsplineSurface3D bss = doObj.getApproximationWithTolerance(tol);
        this.controlPoints = bss.controlPoints;
        this.uKnotData = bss.uKnotData;
        this.vKnotData = bss.vKnotData;
        this.weights = bss.weights;    // may be null
    }

    /**
     * ���̋Ȗʂ� U ���̎�?���Ԃ�?B
     *
     * @return U ���̎�?�
     */
    public int uDegree() {
        return uKnotData.degree();
    }

    /**
     * ���̋Ȗʂ� U ���̃m�b�g�̎�ʂ�Ԃ�?B
     *
     * @return U ���̃m�b�g�̎��
     * @see KnotType
     */
    public int uKnotSpec() {
        return uKnotData.knotSpec();
    }

    /**
     * ���̋Ȗʂ� U ���̃m�b�g��?���Ԃ�?B
     * <p/>
     * �����Ō���?u�m�b�g��?�?v�Ƃ�
     * uKnotData �� knots �t�B?[���h��?ݒ肳�ꂽ�m�b�g�l�̔z��̒����ł͂Ȃ�?A
     * �a�X�v���C���̃m�b�g��{���̃m�b�g��?��ł���?B
     * </p>
     *
     * @return U ���̃m�b�g��?�
     */
    public int uNKnotValues() {
        return uKnotData.nKnotValues();
    }

    /**
     * ���̋Ȗʂ� U ���� n �Ԃ߂̃m�b�g�l��Ԃ�?B
     * <p/>
     * �����Ō���?un �Ԗ�?v�Ƃ�
     * uKnotData �� knots �t�B?[���h��?ݒ肳�ꂽ�m�b�g�l�̔z���̃C���f�b�N�X�ł͂Ȃ�?A
     * �a�X�v���C���̃m�b�g��{���̈Ӗ��ł̃C���f�b�N�X�ł���?B
     * </p>
     *
     * @param n �C���f�b�N�X
     * @return U ���� n �Ԃ߂̃m�b�g�l
     */
    public double uKnotValueAt(int n) {
        return uKnotData.knotValueAt(n);
    }

    /**
     * ���̋Ȗʂ� U ���̃Z�O�?���g��?���Ԃ�?B
     *
     * @return U ���̃Z�O�?���g��?�
     */
    int uNSegments() {
        return uKnotData.nSegments();
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^�I��?k�ނ��Ă��Ȃ��L��Z�O�?���g��?���Ԃ�?B
     *
     * @return U ���̃p���??[�^�I��?k�ނ��Ă��Ȃ��L��Z�O�?���g��?��
     */
    BsplineKnot.ValidSegmentInfo uValidSegments() {
        return uKnotData.validSegments();
    }

    /**
     * ���̋Ȗʂ� (i, j) �Ԗڂ�?���_��Ԃ�?B
     * <p/>
     * �����`����?�?��̓C���f�b�N�X����I�Ɉ���?B
     * </p>
     *
     * @param i U ���̃C���f�b�N�X (i �Ԃ�)
     * @param j V ���̃C���f�b�N�X (j �Ԃ�)
     * @return ?���_
     */
    public Point3D controlPointAt(int i, int j) {
        if (isUPeriodic()) {
            int ncp = uNControlPoints();
            while (i < 0) i += ncp;
            while (i >= ncp) i -= ncp;
        }
        if (isVPeriodic()) {
            int ncp = vNControlPoints();
            while (j < 0) j += ncp;
            while (j >= ncp) j -= ncp;
        }
        return controlPoints[i][j];
    }

    /**
     * ���̋Ȗʂ� V ���̎�?���Ԃ�?B
     *
     * @return V ���̎�?�
     */
    public int vDegree() {
        return vKnotData.degree();
    }

    /**
     * ���̋Ȗʂ� V ���̃m�b�g�̎�ʂ�Ԃ�?B
     *
     * @return V ���̃m�b�g�̎��
     * @see KnotType
     */
    public int vKnotSpec() {
        return vKnotData.knotSpec();
    }

    /**
     * ���̋Ȗʂ� V ���̃m�b�g��?���Ԃ�?B
     * <p/>
     * �����Ō���?u�m�b�g��?�?v�Ƃ�
     * vKnotData �� knots �t�B?[���h��?ݒ肳�ꂽ�m�b�g�l�̔z��̒����ł͂Ȃ�?A
     * �a�X�v���C���̃m�b�g��{���̃m�b�g��?��ł���?B
     * </p>
     *
     * @return V ���̃m�b�g��?�
     */
    public int vNKnotValues() {
        return vKnotData.nKnotValues();
    }

    /**
     * ���̋Ȗʂ� V ���� n �Ԃ߂̃m�b�g�l��Ԃ�?B
     * <p/>
     * �����Ō���?un �Ԗ�?v�Ƃ�
     * vKnotData �� knots �t�B?[���h��?ݒ肳�ꂽ�m�b�g�l�̔z���̃C���f�b�N�X�ł͂Ȃ�?A
     * �a�X�v���C���̃m�b�g��{���̈Ӗ��ł̃C���f�b�N�X�ł���?B
     * </p>
     *
     * @param n �C���f�b�N�X
     * @return V ���� n �Ԃ߂̃m�b�g�l
     */
    public double vKnotValueAt(int n) {
        return vKnotData.knotValueAt(n);
    }

    /**
     * ���̋Ȗʂ� V ���̃Z�O�?���g��?���Ԃ�?B
     *
     * @return V ���̃Z�O�?���g��?�
     */
    int vNSegments() {
        return vKnotData.nSegments();
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^�I��?k�ނ��Ă��Ȃ��L��Z�O�?���g��?���Ԃ�?B
     *
     * @return V ���̃p���??[�^�I��?k�ނ��Ă��Ȃ��L��Z�O�?���g��?��
     */
    BsplineKnot.ValidSegmentInfo vValidSegments() {
        return vKnotData.validSegments();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?W�l
     * @see ParameterOutOfRange
     */
    public Point3D coordinates(double uParam, double vParam) {
        double[][][] cntlPnts;
        int uUicp;
        int u_section;
        double[][] bsc;
        double[] d0D;
        boolean isPoly = isPolynomial();
        int i, j;

        uParam = checkUParameter(uParam);
        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray(isPoly);
        uUicp = cntlPnts.length;
        bsc = new double[uUicp][];

        /*
        * map for V-direction
        */
        if ((u_section = uKnotData.segmentIndex(uParam)) < 0)
            throw new FatalException();
        for (i = j = u_section; i <= (u_section + uDegree()); i++, j++) {
            if (!isPoly && j == uNControlPoints())
                j = 0;
            bsc[j] = BsplineCurveEvaluation.coordinates(vKnotData,
                    cntlPnts[j], vParam);
        }

        /*
        * map for U-direction
        */
        d0D = BsplineCurveEvaluation.coordinates(uKnotData, bsc, uParam);
        if (!isPoly) {
            convRational0Deriv(d0D);
        }
        return new CartesianPoint3D(d0D);
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * �����ł�?ڃx�N�g���Ƃ�?A�p���??[�^ U/V �̊e?X�ɂ��Ă̈ꎟ�Γ���?��ł���?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ�z��̗v�f?��� 2 �ł���?B
     * �z���?�?��̗v�f�ɂ� U �p���??[�^�ɂ��Ă�?ڃx�N�g��?A
     * ��Ԗڂ̗v�f�ɂ� V �p���??[�^�ɂ��Ă�?ڃx�N�g����܂�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?ڃx�N�g��
     * @see ParameterOutOfRange
     */
    public Vector3D[] tangentVector(double uParam, double vParam) {
        double[][][] cntlPnts;
        int uUicp;
        int u_section;
        double[][] pp, dd, tt;
        double[][] ld1D = new double[2][];
        Vector3D[] d1D = new Vector3D[2];
        boolean isPoly = isPolynomial();
        int i, j;

        uParam = checkUParameter(uParam);
        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray(isPoly);
        uUicp = cntlPnts.length;
        pp = new double[uUicp][4];
        tt = new double[uUicp][4];

        /*
        * map for V-direction
        */
        if ((u_section = uKnotData.segmentIndex(uParam)) < 0)
            throw new FatalException();
        for (i = j = u_section; i <= (u_section + uDegree()); i++, j++) {
            if (!isPoly && j == uNControlPoints())
                j = 0;
            BsplineCurveEvaluation.evaluation(vKnotData, cntlPnts[j],
                    vParam, pp[j], tt[j],
                    null, null);
        }

        /*
        * map for U-direction
        */
        ld1D[0] = new double[4];
        if (isPoly) {
            BsplineCurveEvaluation.evaluation(uKnotData, pp, uParam,
                    null, ld1D[0], null, null);
            ld1D[1] = BsplineCurveEvaluation.coordinates(uKnotData, tt, uParam);
        } else {
            double[] ld0D = new double[4];
            BsplineCurveEvaluation.evaluation(uKnotData, pp, uParam,
                    ld0D, ld1D[0], null, null);
            ld1D[1] = BsplineCurveEvaluation.coordinates(uKnotData, tt, uParam);
            convRational1Deriv(ld0D, ld1D[0], ld1D[1]);
        }
        for (i = 0; i < 2; i++) {
            d1D[i] = new LiteralVector3D(ld1D[i]);
        }
        return d1D;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     * @see ParameterOutOfRange
     */
    public SurfaceDerivative3D evaluation(double uParam, double vParam) {
        double[][][] cntlPnts;
        int uUicp;
        int u_section;
        double[][] pp, tt, dd;
        double[] ld0, ldu, ldv, lduu, lduv, ldvv;
        Point3D d0;
        Vector3D du, dv, duu, duv, dvv;
        boolean isPoly = isPolynomial();
        int i, j;

        uParam = checkUParameter(uParam);
        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray(isPoly);
        uUicp = cntlPnts.length;
        pp = new double[uUicp][4];
        tt = new double[uUicp][4];
        dd = new double[uUicp][4];

        /*
        * map for V-direction
        */
        if ((u_section = uKnotData.segmentIndex(uParam)) < 0)
            throw new FatalException();
        for (i = j = u_section; i <= (u_section + uDegree()); i++, j++) {
            if (!isPoly && j == uNControlPoints())
                j = 0;
            BsplineCurveEvaluation.evaluation(vKnotData, cntlPnts[j], vParam,
                    pp[j], tt[j], dd[j], null);
        }

        /*
        * map for U-direction
        */
        ldv = new double[4];
        lduv = new double[4];
        BsplineCurveEvaluation.evaluation(uKnotData, tt, uParam, ldv, lduv, null, null);
        ldvv = BsplineCurveEvaluation.coordinates(uKnotData, dd, uParam);
        ld0 = new double[4];
        ldu = new double[4];
        lduu = new double[4];
        BsplineCurveEvaluation.evaluation(uKnotData, pp, uParam, ld0, ldu, lduu, null);

        if (!isPoly) {
            convRational2Deriv(ld0, ldu, ldv, lduu, lduv, ldvv);
        }

        d0 = new CartesianPoint3D(ld0);
        du = new LiteralVector3D(ldu);
        dv = new LiteralVector3D(ldv);
        duu = new LiteralVector3D(lduu);
        duv = new LiteralVector3D(lduv);
        dvv = new LiteralVector3D(ldvv);
        return new SurfaceDerivative3D(d0, du, dv, duu, duv, dvv);
    }

    /**
     * ���̋Ȗ�?�̓�g�� (u, v) �p���??[�^�l���������Ƃ݂Ȃ��邩�ۂ�?A��Ԃ�?B
     *
     * @param pA   ���� (u, v) �p���??[�^�l
     * @param pB   ����� (u, v) �p���??[�^�l
     * @param dTol �����̋��e��?�
     * @return ��g�� (u, v) �p���??[�^�l����������� true?A�����łȂ���� false
     * @see #projectFrom(Point3D)
     */
    private boolean twoPointsCoincide(PointOnSurface3D pA,
                                      PointOnSurface3D pB,
                                      ToleranceForDistance dTol) {
        if (pA.coordinates().distance2(pB.coordinates()) > dTol.squared()) {
            return false;
        }

        double uA = pA.uParameter();
        double vA = pA.vParameter();
        double uB = pB.uParameter();
        double vB = pB.vParameter();

        double pDiff;
        double pTol;
        boolean coincide;

        pDiff = Math.abs(uA - uB);
        pTol = dTol.toToleranceForParameterU(this, uA, vA).value();
        coincide = false;
        if (pDiff < pTol) {
            coincide = true;
        } else if (this.isUPeriodic() == true) {
            if (Math.abs(pDiff - this.uParameterDomain().section().absIncrease()) < pTol)
                coincide = true;
        }
        if (coincide == false) {
            return false;
        }

        pDiff = Math.abs(vA - vB);
        pTol = dTol.toToleranceForParameterV(this, uA, vA).value();
        coincide = false;
        if (pDiff < pTol) {
            coincide = true;
        } else if (this.isVPeriodic() == true) {
            if (Math.abs(pDiff - this.vParameterDomain().section().absIncrease()) < pTol)
                coincide = true;
        }
        if (coincide == false) {
            return false;
        }

        return true;
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * �a�X�v���C���Ȗʂ�x�W�G�Ȗʂ�?W?��ɕϊ���?A
     * �_���炻�ꂼ��̃x�W�G�Ȗʂւ̓��e�_��?�߂Ă���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @see #toPureBezierSurfaceArray()
     */
    public PointOnSurface3D[] projectFrom(Point3D point) {
        Vector projectedPointList = new Vector();

        BsplineKnot.ValidSegmentInfo uValidSegmentsInfo = this.uValidSegments();
        BsplineKnot.ValidSegmentInfo vValidSegmentsInfo = this.vValidSegments();

        PureBezierSurface3D[][] bzss = this.toPureBezierSurfaceArray();
        ToleranceForDistance dTol = this.getToleranceForDistanceAsObject();

        for (int ui = 0; ui < uValidSegmentsInfo.nSegments(); ui++) {
            for (int vi = 0; vi < vValidSegmentsInfo.nSegments(); vi++) {
                PointOnSurface3D[] localProj = bzss[ui][vi].projectFrom(point);
                for (int i = 0; i < localProj.length; i++) {
                    PointOnSurface3D proj =
                            new PointOnSurface3D
                                    (this,
                                            uValidSegmentsInfo.l2Gp(ui, localProj[i].uParameter()),
                                            vValidSegmentsInfo.l2Gp(vi, localProj[i].vParameter()));

                    boolean isUnique = true;
                    for (Enumeration e = projectedPointList.elements();
                         e.hasMoreElements();) {
                        if (this.twoPointsCoincide
                                (proj, (PointOnSurface3D) e.nextElement(), dTol) == true) {
                            isUnique = false;
                            break;
                        }
                    }
                    if (isUnique == true)
                        projectedPointList.addElement(proj);
                }
            }
        }

        PointOnSurface3D[] results = new PointOnSurface3D[projectedPointList.size()];
        projectedPointList.copyInto(results);
        return results;
    }

    /**
     * ���� U ����?u�����`��?v�̂a�X�v���C���Ȗʂ̌`?��ς�����?A
     * �^����ꂽ U �p���??[�^�l�ɑΉ�����_��J�n�_�Ƃ���悤��
     * �ϊ�������̂�Ԃ�?B
     * <p/>
     * ?�?������a�X�v���C���Ȗʂ� U ���̊J�n�_�̃p���??[�^�l��?�� 0 �ɂȂ�?B
     * </p>
     *
     * @param newStartParam �J�n�_�ƂȂ�p���??[�^�l
     * @return �^����ꂽ�p���??[�^�l�ɑΉ�����_��J�n�_�Ƃ���a�X�v���C���Ȗ�
     * @throws OpenSurfaceForUDirectionException
     *          U ���ɊJ�����`���̋Ȗʂł���
     */
    public BsplineSurface3D uShiftIfPeriodic(double newStartParam)
            throws OpenSurfaceForUDirectionException {
        if (this.isUPeriodic() != true)
            throw new OpenSurfaceForUDirectionException();

        newStartParam = this.uParameterDomain().wrap(newStartParam);

        // ?擪�ɂȂ�Z�O�?���g�̔�?��𓾂�
        int newFirstSegment =
                this.uKnotData.getSegmentNumberThatStartIsEqualTo(newStartParam);

        if (newFirstSegment == (-1))
            return this.uInsertKnot(newStartParam).uShiftIfPeriodic(newStartParam);

        // �m�b�g�f?[�^��?�?�
        BsplineKnot newKnotData = this.uKnotData.shift(newFirstSegment);

        // ?���_���?�?�
        int uNNewControlPoints = newKnotData.nControlPoints();
        int vNNewControlPoints = this.vNControlPoints();

        Point3D[][] newControlPoints =
                new Point3D[uNNewControlPoints][vNNewControlPoints];

        for (int ui = 0; ui < uNNewControlPoints; ui++)
            for (int vi = 0; vi < vNNewControlPoints; vi++)
                newControlPoints[ui][vi] =
                        this.controlPointAt((ui + newFirstSegment) % uNNewControlPoints, vi);

        // ?d�ݗ��?�?�
        double[][] newWeights = null;
        if (this.isRational() == true) {
            newWeights =
                    new double[uNNewControlPoints][vNNewControlPoints];

            for (int ui = 0; ui < uNNewControlPoints; ui++)
                for (int vi = 0; vi < vNNewControlPoints; vi++)
                    newWeights[ui][vi] =
                            this.weightAt((ui + newFirstSegment) % uNNewControlPoints, vi);
        }

        return new BsplineSurface3D(newKnotData, this.vKnotData, newControlPoints, newWeights);
    }

    /**
     * ���� V ����?u�����`��?v�̂a�X�v���C���Ȗʂ̌`?��ς�����?A
     * �^����ꂽ V �p���??[�^�l�ɑΉ�����_��J�n�_�Ƃ���悤��
     * �ϊ�������̂�Ԃ�?B
     * <p/>
     * ?�?������a�X�v���C���Ȗʂ� V ���̊J�n�_�̃p���??[�^�l��?�� 0 �ɂȂ�?B
     * </p>
     *
     * @param newStartParam �J�n�_�ƂȂ�p���??[�^�l
     * @return �^����ꂽ�p���??[�^�l�ɑΉ�����_��J�n�_�Ƃ���a�X�v���C���Ȗ�
     * @throws OpenSurfaceForVDirectionException
     *          V ���ɊJ�����`���̋Ȗʂł���
     */
    public BsplineSurface3D vShiftIfPeriodic(double newStartParam)
            throws OpenSurfaceForVDirectionException {
        if (this.isVPeriodic() != true)
            throw new OpenSurfaceForVDirectionException();

        newStartParam = this.vParameterDomain().wrap(newStartParam);

        // ?擪�ɂȂ�Z�O�?���g�̔�?��𓾂�
        int newFirstSegment =
                this.vKnotData.getSegmentNumberThatStartIsEqualTo(newStartParam);

        if (newFirstSegment == (-1))
            return this.vInsertKnot(newStartParam).vShiftIfPeriodic(newStartParam);

        // �m�b�g�f?[�^��?�?�
        BsplineKnot newKnotData = this.vKnotData.shift(newFirstSegment);

        // ?���_���?�?�
        int uNNewControlPoints = this.uNControlPoints();
        int vNNewControlPoints = newKnotData.nControlPoints();

        Point3D[][] newControlPoints =
                new Point3D[uNNewControlPoints][vNNewControlPoints];

        for (int ui = 0; ui < uNNewControlPoints; ui++)
            for (int vi = 0; vi < vNNewControlPoints; vi++)
                newControlPoints[ui][vi] =
                        this.controlPointAt(ui, (vi + newFirstSegment) % vNNewControlPoints);

        // ?d�ݗ��?�?�
        double[][] newWeights = null;
        if (this.isRational() == true) {
            newWeights =
                    new double[uNNewControlPoints][vNNewControlPoints];

            for (int ui = 0; ui < uNNewControlPoints; ui++)
                for (int vi = 0; vi < vNNewControlPoints; vi++)
                    newWeights[ui][vi] =
                            this.weightAt(ui, (vi + newFirstSegment) % vNNewControlPoints);
        }

        return new BsplineSurface3D(this.uKnotData, newKnotData, newControlPoints, newWeights);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see ParameterOutOfRange
     */
    public Mesh3D
    toMesh(ParameterSection uPint, ParameterSection vPint,
           ToleranceForDistance tol) {
        BsplineSurface3D t_bss;
        Mesh3D Mesh;
        Point3D[][] mesh;
        int u_npnts, v_npnts;
        double uSp, uIp, vSp, vIp;
        double uParam, vParam;
        boolean isUPeriodic = isUPeriodic();
        boolean isVPeriodic = isVPeriodic();
        int i, j;

        t_bss = truncate(uPint, vPint);
        Mesh = t_bss.toMesh(tol);

        uSp = uPint.start();
        uIp = uPint.increase();
        vSp = vPint.start();
        vIp = vPint.increase();

        u_npnts = Mesh.uNPoints();
        v_npnts = Mesh.vNPoints();
        mesh = Mesh.points();

        for (i = 0; i < u_npnts; i++) {
            for (j = 0; j < v_npnts; j++) {
                if (isUPeriodic)
                    uParam = uSp + ((PointOnSurface3D) mesh[i][j]).uParameter();
                else
                    uParam = ((PointOnSurface3D) mesh[i][j]).uParameter();

                if (isVPeriodic)
                    vParam = vSp + ((PointOnSurface3D) mesh[i][j]).vParameter();
                else
                    vParam = ((PointOnSurface3D) mesh[i][j]).vParameter();

                try {
                    mesh[i][j] = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
                } catch (InvalidArgumentValueException e) {
                    throw new FatalException();
                }
            }
        }

        return new Mesh3D(mesh, false);
    }

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param tol �����̋��e��?�
     * @return ���̗L�ȖʑS�̂𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     */
    public Mesh3D toMesh(ToleranceForDistance tol) {
        BsplineSurface3D obss;        /* open bss */
        ParameterDomain uDmn, vDmn;        /* parameter domain */

        FreeformSurfaceWithControlPoints3D.SegInfo seg_info; /* a SegInfo */

        FreeformSurfaceWithControlPoints3D.GpList u_gp_list; /* list of MeshParam for U dir. */
        FreeformSurfaceWithControlPoints3D.GpList v_gp_list; /* list of MeshParam for V dir. */

        double[] u_kp = new double[2];    /* array of non-reduced knot points in [UV] dir. */
        double[] v_kp = new double[2];

        uDmn = uParameterDomain();
        vDmn = vParameterDomain();
        obss = openBssIfClosed().truncate(uDmn.section(), vDmn.section());
        // ���[�̃m�b�g��?d�ɂ��邽��?Atruncate()��Ă�

        /*
        * divide Bspline into planes and determine mesh.
        */
        u_gp_list = new FreeformSurfaceWithControlPoints3D.GpList();
        v_gp_list = new FreeformSurfaceWithControlPoints3D.GpList();

        seg_info = new FreeformSurfaceWithControlPoints3D.SegInfo
                (new MeshParam(0, 0, 1), new MeshParam(0, 1, 1),
                        new MeshParam(0, 0, 1), new MeshParam(0, 1, 1));

        obss.getSrfMesh(seg_info, tol, u_gp_list, v_gp_list);

        /*
        * make parameters and mesh points
        */
        u_kp[0] = uDmn.section().start();
        u_kp[1] = uDmn.section().end();

        v_kp[0] = vDmn.section().start();
        v_kp[1] = vDmn.section().end();

        return obss.makeParamAndMesh(u_gp_list, v_gp_list, u_kp, v_kp);
    }

    /**
     * ���̋Ȗʂ� U/V �o���ɊJ�����`���ɂ�����̂�Ԃ�?B
     * <p/>
     * this �� U/V �o���ɊJ�����`���ł����?Athis ��Ԃ�?B
     * </p>
     *
     * @return U/V �o���ɊJ�����`���̋Ȗ�
     * @see #toMesh(ToleranceForDistance)
     */
    private BsplineSurface3D openBssIfClosed() {
        BsplineSurface3D[] bsss;
        ParameterDomain uDmn, vDmn;

        uDmn = uParameterDomain();
        if (uDmn.isPeriodic()) {
            try {
                bsss = uDivide(uDmn.section().end());
            } catch (ParameterOutOfRange e) {
                throw new FatalException();
            }
            return bsss[0].openBssIfClosed();
        }

        vDmn = vParameterDomain();
        if (vDmn.isPeriodic()) {
            try {
                bsss = vDivide(vDmn.section().end());
            } catch (ParameterOutOfRange e) {
                throw new FatalException();
            }
            return bsss[0];
        }

        return this;
    }

    /**
     * ���̋Ȗʂ��^����ꂽ?��x�ɂ����ĕ��ʂƌ��Ȃ��Ȃ�?�?���?A
     * U/V ���Ƀp���??[�^���_�œ񕪊�����?B
     * <p/>
     * ���̋Ȗʂ� U/V �o���ɊJ�����`���ł���K�v������?B
     * </p>
     * <p/>
     * ���ʂƂ��ē�����z�� S �̗v�f��?��� 4 �ł���?B
     * �e�v�f��?A���̋Ȗʂ𕪊������Ȗʂ̂��ꂼ���\��?B
     * <p/>
     * �^����ꂽ tol �ɂ�����?A�Ȗʂ𕪊�����K�v���Ȃ�?�?��ɂ�
     * S[i] (i = 0, ..., 3) �ɂ͂��ׂ� null �����?B
     * </p>
     * <p/>
     * �Ȗʂ� U/V ���Ƃ�ɓ񕪊�����?�?��ɂ�?A
     * S �̊e�v�f�͈ȉ��̋Ȗʂ�\��?B
     * <pre>
     * 		S[0] : U ���?AV ���ɂ�����Ȗ�
     * 		S[1] : U ���?㑤?AV ���ɂ�����Ȗ�
     * 		S[2] : U ���?AV ���?㑤�ɂ�����Ȗ�
     * 		S[3] : U ���?㑤?AV ���?㑤�ɂ�����Ȗ�
     * </pre>
     * </p>
     * <p/>
     * �Ȗʂ� U ���ɂ̂ݓ񕪊����� (V ���ɂ͕�������K�v���Ȃ�) ?�?��ɂ�?A
     * S �̊e�v�f�͈ȉ��̋Ȗʂ�\��?B
     * <pre>
     * 		S[0] : U ���ɂ�����Ȗ�
     * 		S[1] : U ���?㑤�ɂ�����Ȗ�
     * 		S[2] : null
     * 		S[3] : null
     * </pre>
     * </p>
     * <p/>
     * �Ȗʂ� V ���ɂ̂ݓ񕪊����� (U ���ɂ͕�������K�v���Ȃ�) ?�?��ɂ�?A
     * S �̊e�v�f�͈ȉ��̋Ȗʂ�\��?B
     * <pre>
     * 		S[0] : V ���ɂ�����Ȗ�
     * 		S[1] : null
     * 		S[2] : V ���?㑤�ɂ�����Ȗ�
     * 		S[3] : null
     * </pre>
     * </p>
     *
     * @param tol ���ʂƂ݂Ȃ������̋��e��?�
     * @return �������ꂽ�Ȗʂ̔z��
     */
    FreeformSurfaceWithControlPoints3D[] divideForMesh(ToleranceForDistance tol) {
        boolean u_coln;
        boolean v_coln;

        ParameterDomain uDmn, vDmn;
        double u_mid_param;
        double v_mid_param;

        if (isUPeriodic() || isVPeriodic())
            throw new FatalException();

        BsplineSurface3D[] bsss;
        BsplineSurface3D vb_bss;
        BsplineSurface3D vu_bss;
        BsplineSurface3D lb_bss;
        BsplineSurface3D rb_bss;
        BsplineSurface3D lu_bss;
        BsplineSurface3D ru_bss;

        double told = tol.value();

        u_coln = uIsColinear(controlPoints, told);
        v_coln = vIsColinear(controlPoints, told);

        uDmn = uParameterDomain();
        vDmn = vParameterDomain();

        u_mid_param = (uDmn.section().start() + uDmn.section().end()) / 2.0;
        v_mid_param = (vDmn.section().start() + vDmn.section().end()) / 2.0;

        try {
            if (u_coln && v_coln) {
                lb_bss = null;
                rb_bss = null;
                lu_bss = null;
                ru_bss = null;

            } else if ((!u_coln) && (!v_coln)) {
                bsss = vDivide(v_mid_param);
                vb_bss = bsss[0];
                vu_bss = bsss[1];

                bsss = vb_bss.uDivide(u_mid_param);
                lb_bss = bsss[0];
                rb_bss = bsss[1];

                bsss = vu_bss.uDivide(u_mid_param);
                lu_bss = bsss[0];
                ru_bss = bsss[1];

            } else if (u_coln) {
                bsss = vDivide(v_mid_param);
                lb_bss = bsss[0];
                lu_bss = bsss[1];

                rb_bss = null;
                ru_bss = null;

            } else {    // if (v_coln)
                bsss = uDivide(u_mid_param);
                lb_bss = bsss[0];
                rb_bss = bsss[1];

                lu_bss = null;
                ru_bss = null;

            }
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }

        bsss = new BsplineSurface3D[4];

        bsss[0] = lb_bss;
        bsss[1] = rb_bss;
        bsss[2] = lu_bss;
        bsss[3] = ru_bss;

        return bsss;
    }

    /**
     * ���̋Ȗʂ����ʌ`?�Ƃ݂Ȃ��邩�ǂ�����Ԃ�?B
     *
     * @param tol ���ʂƂ݂Ȃ������̋��e��?�
     * @return ���ʂƂ݂Ȃ���Ȃ�� true?A�����łȂ���� false
     * @see PureBezierSurface3D#isPlaner(ToleranceForDistance)
     */
    boolean isPlaner(ToleranceForDistance tol) {
        PureBezierSurface3D bzs;

        try {
            bzs = new PureBezierSurface3D(controlPoints);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        return bzs.isPlaner(tol);
    }

    /**
     * ���̂a�X�v���C���Ȗʂ�?Č�����x�W�G�Ȗʂ̓񎟌��z���Ԃ�?B
     * <p/>
     * ���̋Ȗʂ� U/V �����Ƃ�Ƀp���??[�^�I��?k�ނ��Ă��Ȃ��L��Z�O�?���g�ɑΉ�����
     * �x�W�G�Ȗʂ̓񎟌��z���Ԃ�?B
     * </p>
     *
     * @return �x�W�G�Ȗʂ̓񎟌��z��
     */
    public PureBezierSurface3D[][] toPureBezierSurfaceArray() {
        double[][][] cntlPnts;
        boolean isPoly = isPolynomial();
        double[][][][][] bzs_array;
        PureBezierSurface3D[][] bzss;

        cntlPnts = toDoubleArray(isPoly);
        bzs_array = BsplineCurveEvaluation.toBezierSurface(uKnotData, vKnotData, cntlPnts);
        bzss = new PureBezierSurface3D[bzs_array.length][bzs_array[0].length];
        for (int i = 0; i < bzs_array.length; i++) {
            for (int j = 0; j < bzs_array[0].length; j++) {
                bzss[i][j] = new PureBezierSurface3D(bzs_array[i][j]);
            }
        }

        return bzss;
    }

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ�?B
     * <p/>
     * this ���L�?�Ȗʂł����?Athis ��Ԃ�?B
     * </p>
     *
     * @return ���̗L�ȖʑS�̂�?Č�����L�? Bspline �Ȗ�
     */
    public BsplineSurface3D toBsplineSurface() {
        if (this.isRational() == true)
            return this;

        return new BsplineSurface3D(this.uKnotData,
                this.vKnotData,
                this.controlPoints,
                this.makeUniformWeights());
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @return ���̋Ȗʂ̎w��̋�Ԃ�?Č�����L�? Bspline �Ȗ�
     * @see ParameterOutOfRange
     * @see #toBsplineSurface()
     */
    public BsplineSurface3D
    toBsplineSurface(ParameterSection uPint,
                     ParameterSection vPint) {
        BsplineSurface3D target = this;

        // U direction
        if (target.isUPeriodic() == true) {
            if (uPint.absIncrease() >= target.uParameterDomain().section().absIncrease()) {
                try {
                    target = target.uShiftIfPeriodic(uPint.start());
                } catch (OpenSurfaceForUDirectionException e) {
                    ;    // �N���蓾�Ȃ��͂�
                }
                if (uPint.increase() < 0.0)
                    target = target.reverse(true, false);
            } else {
                target = target.uTruncate(uPint);
            }
        } else {
            target = target.uTruncate(uPint);
        }

        // V direction
        if (target.isVPeriodic() == true) {
            if (vPint.absIncrease() >= target.vParameterDomain().section().absIncrease()) {
                try {
                    target = target.vShiftIfPeriodic(vPint.start());
                } catch (OpenSurfaceForVDirectionException e) {
                    ;    // �N���蓾�Ȃ��͂�
                }
                if (vPint.increase() < 0.0)
                    target = target.reverse(false, true);
            } else {
                target = target.vTruncate(vPint);
            }
        } else {
            target = target.vTruncate(vPint);
        }

        return target.toBsplineSurface();
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsCrvBss3D#intersection(ParametricCurve3D,BsplineSurface3D,boolean)
     * IntsCrvBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
        return IntsCrvBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�~??��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsCrvBss3D#intersection(ParametricCurve3D,BsplineSurface3D,boolean)
     * IntsCrvBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~??��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Conic3D mate, boolean doExchange) {
        return IntsCrvBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsCrvBss3D#intersection(ParametricCurve3D,BsplineSurface3D,boolean)
     * IntsCrvBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
        return IntsCrvBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsCrvBss3D#intersection(ParametricCurve3D,BsplineSurface3D,boolean)
     * IntsCrvBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
        return IntsCrvBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗʂ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ���?������?��ɂ��Ă�?A��?� (IntersectionCurve3D) ���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ�?ڂ����?��ɂ��Ă�?A��_ (IntersectionPoint3D) ���Ԃ邱�Ƃ�����?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��?� (�܂��͌�_) �̔z��
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    public SurfaceSurfaceInterference3D[] intersect(ParametricSurface3D mate) {
        return mate.intersect(this, true);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSrfBss3D#intersection(ElementarySurface3D,BsplineSurface3D,boolean)
     * IntsSrfBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate, boolean doExchange) {
        return IntsSrfBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSrfBss3D#intersection(ElementarySurface3D,BsplineSurface3D,boolean)
     * IntsSrfBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate,
                                             boolean doExchange) {
        return IntsSrfBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSrfBss3D#intersection(ElementarySurface3D,BsplineSurface3D,boolean)
     * IntsSrfBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange) {
        return IntsSrfBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~??��) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSrfBss3D#intersection(ElementarySurface3D,BsplineSurface3D,boolean)
     * IntsSrfBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate,
                                             boolean doExchange) {
        return IntsSrfBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�x�W�G�Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSrfBss3D#intersection(ElementarySurface3D,BsplineSurface3D,boolean)
     * IntsSrfBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(PureBezierSurface3D mate,
                                             boolean doExchange) {
        return IntsSrfBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSrfBss3D#intersection(ElementarySurface3D,BsplineSurface3D,boolean)
     * IntsSrfBss3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(BsplineSurface3D mate,
                                             boolean doExchange) {
        return IntsSrfBss3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�I�t�Z�b�g�����Ȗʂ�
     * �^����ꂽ��?��ŋߎ����� Bspline �Ȗʂ�?�߂�?B
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.FRONT/BACK)
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�`��Ԃ̃I�t�Z�b�g�Ȗʂ�ߎ����� Bspline �Ȗ�
     * @see WhichSide
     */
    public BsplineSurface3D
    offsetByBsplineSurface(ParameterSection uPint,
                           ParameterSection vPint,
                           double magni,
                           int side,
                           ToleranceForDistance tol) {
        Ofst3D doObj = new Ofst3D(this, uPint, vPint, magni, side, tol);
        return doObj.offset();
    }

    /*
    * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param uParam	U ���̃p���??[�^�l
    * @return	�w��� U �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D uIsoParametricCurve(double uParam) {
        uParam = checkUParameter(uParam);
        boolean isPoly = isPolynomial();
        double[][][] cntlPnts = toDoubleArray(isPoly);
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        double[][] tBsc = new double[uUicp][];
        double[][] bsc = new double[vUicp][];

        for (int i = 0; i < vUicp; i++) {
            for (int j = 0; j < uUicp; j++)
                tBsc[j] = cntlPnts[j][i];
            bsc[i] = BsplineCurveEvaluation.coordinates(uKnotData, tBsc, uParam);
        }
        return new BsplineCurve3D(vKnotData, bsc);
    }

    /*
    * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param vParam	V ���̃p���??[�^�l
    * @return	�w��� V �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D vIsoParametricCurve(double vParam) {
        vParam = checkVParameter(vParam);
        boolean isPoly = isPolynomial();
        double[][][] cntlPnts = toDoubleArray(isPoly);
        int uUicp = uNControlPoints();
        double[][] bsc = new double[uUicp][];

        for (int i = 0; i < uUicp; i++) {
            bsc[i] = BsplineCurveEvaluation.coordinates(vKnotData, cntlPnts[i], vParam);
        }
        return new BsplineCurve3D(uKnotData, bsc);
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ U ���̃p���??[�^�ʒu��?V���ȃm�b�g��}���?B
     * <p/>
     * �`?�͕ς�炸��?AU ���̃Z�O�?���g��������a�X�v���C���Ȗʂ�Ԃ�?B
     * </p>
     *
     * @param uParam �m�b�g��}��� U ���̃p���??[�^�ʒu
     * @return �m�b�g�}���̂a�X�v���C���Ȗ�
     */
    public BsplineSurface3D uInsertKnot(double uParam) {
        double[][][] cntlPnts;
        double[][] bsc;
        double[][][] bss_array = null;
        Object[] objs;
        BsplineKnot newUKd = null;
        double[][] newUCp;
        int newUUicp = 0;
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        boolean isPoly = isPolynomial();
        int i, j;

        uParam = checkUParameter(uParam);
        cntlPnts = toDoubleArray(isPoly);
        bsc = new double[uUicp][];

        for (i = 0; i < vUicp; i++) {
            for (j = 0; j < uUicp; j++)
                bsc[j] = cntlPnts[j][i];
            objs = BsplineCurveEvaluation.insertKnot(uKnotData, bsc, uParam);

            if (i == 0) {
                newUKd = (BsplineKnot) objs[0];
                newUUicp = newUKd.nControlPoints();
                bss_array = new double[newUUicp][vUicp][];
            }

            newUCp = (double[][]) objs[1];

            for (j = 0; j < newUUicp; j++)
                bss_array[j][i] = newUCp[j];
        }

        return new BsplineSurface3D(newUKd, vKnotData, bss_array);
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ V ���̃p���??[�^�ʒu��?V���ȃm�b�g��}���?B
     * <p/>
     * �`?�͕ς�炸��?AV ���̃Z�O�?���g��������a�X�v���C���Ȗʂ�Ԃ�?B
     * </p>
     *
     * @param vParam �m�b�g��}��� V ���̃p���??[�^�ʒu
     * @return �m�b�g�}���̂a�X�v���C���Ȗ�
     */
    public BsplineSurface3D vInsertKnot(double vParam) {
        double[][][] cntlPnts;
        double[][][] bss_array;
        Object[] objs;
        BsplineKnot newVKd = null;
        int uUicp = uNControlPoints();
        boolean isPoly = isPolynomial();
        int i;

        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray(isPoly);
        bss_array = new double[uUicp][][];

        for (i = 0; i < uUicp; i++) {
            objs = BsplineCurveEvaluation.insertKnot(vKnotData, cntlPnts[i], vParam);
            if (i == 0)
                newVKd = (BsplineKnot) objs[0];
            bss_array[i] = (double[][]) objs[1];
        }

        return new BsplineSurface3D(uKnotData, newVKd, bss_array);
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ U ���̃p���??[�^�l�ŕ�������?B
     * <p/>
     * ���̋Ȗʂ� U ���ɊJ�����`����?�?�?A
     * uParam �ɑΉ����铙�p���??[�^��?�őO���ʂɕ�������?B
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f�ɂ� U ���O���̋Ȗ�?A
     * ��Ԗڂ̗v�f�ɂ� U ���㑤�̋Ȗ�
     * �����?B
     * </p>
     * <p/>
     * ���̋Ȗʂ� U ���ɕ����`����?�?���?A
     * uParam �ɑΉ����铙�p���??[�^��?�뫊E�Ƃ���
     * U ���ɊJ�����`���̈ꖇ�̋Ȗʂɕϊ�����?B
     * ���ʂƂ��ē�����z��̗v�f?��� 1 �ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @return ������̂a�X�v���C���Ȗʂ̔z��
     * @see ParameterOutOfRange
     * @see #vDivide(double)
     */
    public BsplineSurface3D[] uDivide(double uParam) {
        double[][][] cntlPnts;
        double[][] bsc;
        double[][][][] bsss_array = new double[2][][][];
        BsplineKnot[] newUKd = new BsplineKnot[2];
        double[][][] newUCp = new double[2][][];
        int[] newUUicp = new int[2];
        BsplineSurface3D[] bsss;
        int n_bsss = 0;
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        boolean isPoly = isPolynomial();
        int i, j, k;

        uParam = checkUParameter(uParam);
        cntlPnts = toDoubleArray(isPoly);
        bsc = new double[uUicp][];

        for (i = 0; i < vUicp; i++) {
            for (j = 0; j < uUicp; j++)
                bsc[j] = cntlPnts[j][i];
            BsplineCurveEvaluation.divide(uKnotData, bsc, uParam, newUKd, newUCp);
            if (newUKd[0] == null)
                throw new FatalException();
            else if (newUKd[1] == null)
                n_bsss = 1;
            else
                n_bsss = 2;

            if (i == 0) {
                for (k = 0; k < n_bsss; k++) {
                    newUUicp[k] = newUKd[k].nControlPoints();
                    bsss_array[k] = new double[newUUicp[k]][vUicp][];
                }
            }

            for (k = 0; k < n_bsss; k++)
                for (j = 0; j < newUUicp[k]; j++)
                    bsss_array[k][j][i] = newUCp[k][j];
        }

        bsss = new BsplineSurface3D[n_bsss];
        for (i = 0; i < n_bsss; i++) {
            try {
                bsss[i] = new BsplineSurface3D(newUKd[i], vKnotData, bsss_array[i]);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }
        return bsss;
    }

    /*
    * ���̋Ȗʂ�?A�^����ꂽ V ���̃p���??[�^�l�ŕ�������?B
    * <p>
    * ���̋Ȗʂ� V ���ɊJ�����`����?�?�?A
    * vParam �ɑΉ����铙�p���??[�^��?�őO���ʂɕ�������?B
    * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
    * ?�?��̗v�f�ɂ� V ���O���̋Ȗ�?A
    * ��Ԗڂ̗v�f�ɂ� V ���㑤�̋Ȗ�
    * �����?B
    * </p>
    * <p>
    * ���̋Ȗʂ� V ���ɕ����`����?�?���?A
    * vParam �ɑΉ����铙�p���??[�^��?�뫊E�Ƃ���
    * V ���ɊJ�����`���̈ꖇ�̋Ȗʂɕϊ�����?B
    * ���ʂƂ��ē�����z��̗v�f?��� 1 �ł���?B
    * </p>
    * <p>
    * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
    * ParameterOutOfRange �̗�O��?�����?B
    * </p>
    *
    * @param vParam	V ���̃p���??[�^�l
    * @return	������̂a�X�v���C���Ȗʂ̔z��
    * @see	ParameterOutOfRange
    * @see	#uDivide(double)
    */
    public BsplineSurface3D[] vDivide(double vParam) {
        double[][][] cntlPnts;
        double[][][][] bsss_array;
        BsplineKnot[] newVKd = new BsplineKnot[2];
        double[][][] newVCp = new double[2][][];
        BsplineSurface3D[] bsss;
        int n_bsss;
        int uUicp = uNControlPoints();
        boolean isPoly = isPolynomial();
        int i;

        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray(isPoly);
        bsss_array = new double[2][uUicp][][];

        for (i = 0; i < uUicp; i++) {
            BsplineCurveEvaluation.divide(vKnotData, cntlPnts[i], vParam, newVKd, newVCp);
            bsss_array[0][i] = newVCp[0];
            if (newVKd[0] == null)
                throw new FatalException();
            else if (newVKd[1] != null)
                bsss_array[1][i] = newVCp[1];
        }

        if (newVKd[1] == null)
            n_bsss = 1;
        else
            n_bsss = 2;

        bsss = new BsplineSurface3D[n_bsss];
        for (i = 0; i < n_bsss; i++) {
            try {
                bsss[i] = new BsplineSurface3D(uKnotData, newVKd[i], bsss_array[i]);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }
        return bsss;
    }

    /**
     * ���̂a�X�v���C���Ȗʂ�?A�^����ꂽ��`��Ԃ�?ؒf����?B
     * <p/>
     * uSection �̑?���l������?�?��ɂ�?AU ����?i?s���]�����a�X�v���C���Ȗʂ�Ԃ�?B
     * ���l��?A
     * vSection �̑?���l������?�?��ɂ�?AV ����?i?s���]�����a�X�v���C���Ȗʂ�Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uSection ?ؒf���Ďc��������\�� U ���̃p���??[�^���
     * @param vSection ?ؒf���Ďc��������\�� V ���̃p���??[�^���
     * @return ?ؒf���Ďc����������\���a�X�v���C���Ȗ�
     * @see ParameterOutOfRange
     */
    public BsplineSurface3D truncate(ParameterSection uSection,
                                     ParameterSection vSection) {
        BsplineSurface3D t_bss;

        t_bss = uTruncate(uSection);
        t_bss = t_bss.vTruncate(vSection);
        return t_bss;
    }

    /**
     * ���̂a�X�v���C���Ȗʂ�?A�^����ꂽ U ���̋�Ԃ�?ؒf����?B
     * <p/>
     * section �̑?���l������?�?��ɂ�?AU ����?i?s���]�����a�X�v���C���Ȗʂ�Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param section ?ؒf���Ďc��������\�� U ���̃p���??[�^���
     * @return ?ؒf���Ďc����������\���a�X�v���C���Ȗ�
     * @see ParameterOutOfRange
     * @see #truncate(ParameterSection,ParameterSection)
     * @see #vTruncate(ParameterSection)
     * @see #reverse(boolean,boolean)
     */
    private BsplineSurface3D uTruncate(ParameterSection section) {
        double start_par, end_par;
        BsplineSurface3D t_bss;

        if (isUNonPeriodic()) {
            start_par = checkUParameter(section.lower());
            end_par = checkUParameter(section.upper());
            t_bss = uDivide(start_par)[1];
            t_bss = t_bss.uDivide(end_par)[0];
        } else {
            double srf_intvl = uParameterDomain().section().increase();
            double tol_p = ConditionOfOperation.getCondition().getToleranceForParameter();

            start_par = checkUParameter(section.start());
            t_bss = uDivide(start_par)[0];
            if (Math.abs(section.increase()) < srf_intvl - tol_p) {
                if (section.increase() > 0.0) {
                    end_par = section.increase();
                    t_bss = t_bss.uDivide(end_par)[0];
                } else {
                    end_par = srf_intvl + section.increase();
                    t_bss = t_bss.uDivide(end_par)[1];
                }
            }
        }

        if (section.increase() < 0.0)
            t_bss = t_bss.reverse(true, false);

        return t_bss;
    }

    /**
     * ���̂a�X�v���C���Ȗʂ�?A�^����ꂽ V ���̋�Ԃ�?ؒf����?B
     * <p/>
     * section �̑?���l������?�?��ɂ�?AV ����?i?s���]�����a�X�v���C���Ȗʂ�Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param section ?ؒf���Ďc��������\�� V ���̃p���??[�^���
     * @return ?ؒf���Ďc����������\���a�X�v���C���Ȗ�
     * @see ParameterOutOfRange
     * @see #truncate(ParameterSection,ParameterSection)
     * @see #uTruncate(ParameterSection)
     * @see #reverse(boolean,boolean)
     */
    private BsplineSurface3D vTruncate(ParameterSection section) {
        double start_par, end_par;
        BsplineSurface3D t_bss;

        if (isVNonPeriodic()) {
            start_par = checkVParameter(section.lower());
            end_par = checkVParameter(section.upper());
            t_bss = vDivide(start_par)[1];
            t_bss = t_bss.vDivide(end_par)[0];
        } else {
            double srf_intvl = vParameterDomain().section().increase();
            double tol_p = ConditionOfOperation.getCondition().getToleranceForParameter();

            start_par = checkVParameter(section.start());
            t_bss = vDivide(start_par)[0];
            if (Math.abs(section.increase()) < srf_intvl - tol_p) {
                if (section.increase() > 0.0) {
                    end_par = section.increase();
                    t_bss = t_bss.vDivide(end_par)[0];
                } else {
                    end_par = srf_intvl + section.increase();
                    t_bss = t_bss.vDivide(end_par)[1];
                }
            }
        }

        if (section.increase() < 0.0)
            t_bss = t_bss.reverse(false, true);

        return t_bss;
    }

    /**
     * ���̂a�X�v���C���Ȗʂ�?A�w��̕��ɔ��]�������a�X�v���C���Ȗʂ�Ԃ�?B
     *
     * @param isU U ���ɔ��]������ǂ���
     * @param isV V ���ɔ��]������ǂ���
     * @return ���]�����a�X�v���C���Ȗ�
     */
    private BsplineSurface3D reverse(boolean isU, boolean isV) {
        BsplineKnot rUKd, rVKd;
        boolean isRat = isRational();
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        Point3D[][] rCp = new Point3D[uUicp][vUicp];
        double[][] rWt = null;
        int i, j, k, l;

        if ((!isU) && (!isV)) {
            return this;
        }

        if (isRat)
            rWt = new double[uUicp][vUicp];

        if (isU) {
            j = uUicp - 1;
            rUKd = uKnotData.reverse();
        } else {
            j = 0;
            rUKd = uKnotData;
        }
        if (isV)
            rVKd = vKnotData.reverse();
        else
            rVKd = vKnotData;

        for (i = 0; i < uUicp; i++) {
            if (isV)
                l = vUicp - 1;
            else
                l = 0;
            for (k = 0; k < vUicp; k++) {
                rCp[i][k] = controlPointAt(j, l);
                if (isRat)
                    rWt[i][k] = weightAt(j, l);
                if (isV)
                    l--;
                else
                    l++;
            }
            if (isU)
                j--;
            else
                j++;
        }

        return new BsplineSurface3D(rUKd, rVKd, rCp, rWt);
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     *
     * @return U ���̃p���??[�^��`��
     * @see BsplineKnot#getParameterDomain()
     */
    ParameterDomain getUParameterDomain() {
        return uKnotData.getParameterDomain();
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
     *
     * @return V ���̃p���??[�^��`��
     * @see BsplineKnot#getParameterDomain()
     */
    ParameterDomain getVParameterDomain() {
        return vKnotData.getParameterDomain();
    }

    /*
    * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ� U ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
    * <p>
    * �^����ꂽ�p���??[�^�l�����̋Ȗʂ� U ���̒�`���O��Ă���?�?��ɂ�
    * ParameterOutOfRange �̗�O��?�����?B
    * </p>
    *
    * @param param	U ���̃p���??[�^�l
    * @return	�K�v�ɉ����Ă��̋Ȗʂ� U ���̒�`���Ɋۂ߂�ꂽ�p���??[�^�l
    * @see	AbstractParametricSurface#checkUValidity(double)
    * @see	ParameterDomain#force(double)
    * @see	ParameterDomain#wrap(double)
    * @see	ParameterOutOfRange
    */
    private double checkUParameter(double param) {
        checkUValidity(param);
        return uParameterDomain().force(uParameterDomain().wrap(param));
    }

    /*
    * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ� V ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
    * <p>
    * �^����ꂽ�p���??[�^�l�����̋Ȗʂ� V ���̒�`���O��Ă���?�?��ɂ�
    * ParameterOutOfRange �̗�O��?�����?B
    * </p>
    *
    * @param param	V ���̃p���??[�^�l
    * @return	�K�v�ɉ����Ă��̋Ȗʂ� V ���̒�`���Ɋۂ߂�ꂽ�p���??[�^�l
    * @see	AbstractParametricSurface#checkVValidity(double)
    * @see	ParameterDomain#force(double)
    * @see	ParameterDomain#wrap(double)
    * @see	ParameterOutOfRange
    */
    private double checkVParameter(double param) {
        checkVValidity(param);
        return vParameterDomain().force(vParameterDomain().wrap(param));
    }

    /**
     * ���̋Ȗʂ�?A�`?�⻂̂܂܂ɂ���?AU ���̎�?�����?グ���Ȗʂ�Ԃ�?B
     *
     * @return ����`?��?AU ���̎�?������?オ�B��Ȗ�
     */
    public BsplineSurface3D uElevateOneDegree() {
        BsplineKnot oldUKnotData = this.uKnotData;
        BsplineKnot oldVKnotData = this.vKnotData;
        int oldUNCP = oldUKnotData.nControlPoints();
        int oldVNCP = oldVKnotData.nControlPoints();
        double[][][] oldControlPoints = this.toDoubleArray(this.isPolynomial());

        BsplineKnot newUKnotData =
                BsplineCurveEvaluation.getNewKnotDataAtDegreeElevation(oldUKnotData);
        BsplineKnot newVKnotData = oldVKnotData;
        int newUNCP = newUKnotData.nControlPoints();
        int newVNCP = newVKnotData.nControlPoints();
        double[][][] newControlPoints = new double[newUNCP][newVNCP][];

        double[][] oldCurve = new double[oldUNCP][];
        double[][] newCurve;

        for (int vi = 0; vi < oldVNCP; vi++) {
            for (int ui = 0; ui < oldUNCP; ui++)
                oldCurve[ui] = oldControlPoints[ui][vi];
            newCurve = BsplineCurveEvaluation.
                    getNewControlPointsAtDegreeElevation(oldUKnotData,
                            newUKnotData,
                            oldCurve);
            for (int ui = 0; ui < newUNCP; ui++)
                newControlPoints[ui][vi] = newCurve[ui];
        }

        return new BsplineSurface3D(newUKnotData, newVKnotData, newControlPoints);
    }

    /**
     * ���̋Ȗʂ�?A�`?�⻂̂܂܂ɂ���?AV ���̎�?�����?グ���Ȗʂ�Ԃ�?B
     *
     * @return ����`?��?AV ���̎�?������?オ�B��Ȗ�
     */
    public BsplineSurface3D vElevateOneDegree() {
        BsplineKnot oldUKnotData = this.uKnotData;
        BsplineKnot oldVKnotData = this.vKnotData;
        int oldUNCP = oldUKnotData.nControlPoints();
        int oldVNCP = oldVKnotData.nControlPoints();
        double[][][] oldControlPoints = this.toDoubleArray(this.isPolynomial());

        BsplineKnot newUKnotData = oldUKnotData;
        BsplineKnot newVKnotData =
                BsplineCurveEvaluation.getNewKnotDataAtDegreeElevation(oldVKnotData);
        int newUNCP = newUKnotData.nControlPoints();
        int newVNCP = newVKnotData.nControlPoints();
        double[][][] newControlPoints = new double[newUNCP][][];

        for (int ui = 0; ui < oldUNCP; ui++) {
            newControlPoints[ui] = BsplineCurveEvaluation.
                    getNewControlPointsAtDegreeElevation(oldVKnotData,
                            newVKnotData,
                            oldControlPoints[ui]);
        }

        return new BsplineSurface3D(newUKnotData, newVKnotData, newControlPoints);
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#BSPLINE_SURFACE_3D ParametricSurface3D.BSPLINE_SURFACE_3D}
     */
    int type() {
        return BSPLINE_SURFACE_3D;
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * �^����ꂽ��?��ŕ��ʋߎ�����_�Q��Ԃ�?B
     * <p/>
     * ?��?���ʂƂ��ē�����_�Q�͈�ʂ�?A�ʑ��I�ɂ�􉽓I�ɂ�?A�i�q?�ł͂Ȃ�?B
     * </p>
     * <p/>
     * scalingFactor ��?A��͗p�ł͂Ȃ�?A?o�͗p�̈�?��ł���?B
     * scalingFactor �ɂ�?A�v�f?� 2 �̔z���^����?B
     * scalingFactor[0] �ɂ� U ����?k�ڔ{��?A
     * scalingFactor[1] �ɂ� V ����?k�ڔ{�����Ԃ�?B
     * �����̒l�͉��炩��?�Βl�ł͂Ȃ�?A
     * �p���??[�^��?i�ޑ��x T �ɑ΂���?A
     * U/V �����ɂ��Ď��?�ŋȖ�?�̓_��?i�ޑ��x Pu/Pv ��\�����Βl�ł���?B
     * �܂�?A�p���??[�^�� T ����?i�ނ�?A
     * ���?�ł̋Ȗ�?�̓_�� U ���ł� Pu (scalingFactor[0])?A
     * V ���ł� Pv (scalingFactor[1]) ����?i�ނ��Ƃ�\���Ă���?B
     * T �̑傫���͖�������Ȃ��̂�?A���̒l��Q?Ƃ���?ۂɂ�?A
     * scalingFactor[0] �� scalingFactor[1] �̔䂾����p����ׂ��ł���?B
     * �Ȃ�?A�����̒l�͂����܂ł�ڈł���?A�����ȑ��x����̂ł͂Ȃ�?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ� Vector �Ɋ܂܂��e�v�f��
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D
     * �ł��邱�Ƃ���҂ł���?B
     * </p>
     *
     * @param uParameterSection U ���̃p���??[�^���
     * @param vParameterSection V ���̃p���??[�^���
     * @param tolerance         �����̋��e��?�
     * @param scalingFactor     �_�Q��O�p�`��������?ۂɗL�p�Ǝv���� U/V ��?k�ڔ{��
     * @return �_�Q��܂� Vector
     * @see PointOnSurface3D
     */
    public Vector toNonStructuredPoints(ParameterSection uParameterSection,
                                        ParameterSection vParameterSection,
                                        double tolerance,
                                        double[] scalingFactor) {
        Vector result = new Vector();

        // ��芸����?A���̎�
        Mesh3D mesh = this.toMesh(uParameterSection,
                vParameterSection,
                new ToleranceForDistance(tolerance));

        for (int u = 0; u < mesh.uNPoints(); u++)
            for (int v = 0; v < mesh.vNPoints(); v++)
                result.addElement(mesh.pointAt(u, v));

        scalingFactor[0] = getMaxLengthOfUControlPolygons(uKnotData.isPeriodic());
        scalingFactor[1] = getMaxLengthOfVControlPolygons(vKnotData.isPeriodic());

        return result;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    protected synchronized ParametricSurface3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Point3D[][] tControlPoints = new Point3D[this.uNControlPoints()][];
        for (int i = 0; i < this.uNControlPoints(); i++)
            tControlPoints[i] = Point3D.transform(this.controlPoints[i],
                    reverseTransform,
                    transformationOperator,
                    transformedGeometries);
        return new BsplineSurface3D(this.uKnotData, this.vKnotData,
                tControlPoints, this.weights);
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
        // output knotData(BsplineKnot)
        //writer.println(indent_tab + "\tuKnotData");
        //uKnotData.output(writer, indent + 2);
        uKnotData.output(writer, indent, 1);
        //writer.println(indent_tab + "\tvKnotData");
        //vKnotData.output(writer, indent + 2);
        vKnotData.output(writer, indent, 2);

        // output controlPoints
        writer.println(indent_tab + "\tcontrolPoints");
        for (int i = 0; i < controlPoints.length; i++) {
            for (int j = 0; j < controlPoints[i].length; j++) {
                controlPoints[i][j].output(writer, indent + 2);
            }
        }

        // output weights
        if (weights() != null) {
            writer.println(indent_tab + "\tweights ");
            for (int j = 0; j < weights().length; j++) {
                writer.print(indent_tab + "\t\t");
                for (int k = 0; k < weights()[j].length; k++) {
                    writer.print(" " + weightAt(j, k));
                }
                writer.println();
            }
        }
        writer.println(indent_tab + "\tsurfaceForm\t" + BsplineSurfaceForm.toString(surfaceForm));

        writer.println(indent_tab + "End");
    }
}
