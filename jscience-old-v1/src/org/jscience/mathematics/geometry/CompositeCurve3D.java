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
 * �R���� : ��?���?��\���N���X?B
 * <p/>
 * ��?���?�Ƃ�?A (�[�_�ŘA������) ����̗L��?��܂Ƃ߂�
 * ��{�̋�?�Ɍ����Ă���̂ł���?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * <ul>
 * <li> ��?���?��?\?�����Z�O�?���g�̔z�� segments
 * <li> ��?���?�����`�����ۂ���\���t���O periodic
 * </ul>
 * ��ێ?����?B
 * </p>
 * <p/>
 * ��?���?�̒�`��͗L��?A
 * ��?���?�����`���ł���Ύ��I?A
 * �����łȂ���Δ���I�Ȃ�̂ɂȂ�?B
 * �p���??[�^��`��� [0, (�Z�O�?���g�̃p���??[�^��Ԃ̑?���l�̑?�a)] �ɂȂ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.4 $, $Date: 2006/03/01 21:15:55 $
 * @see CompositeCurveSegment3D
 */

public class CompositeCurve3D extends BoundedCurve3D {

    /**
     * ��?���?��?\?�����Z�O�?���g�̔z��?B
     *
     * @serial
     */
    private CompositeCurveSegment3D[] segments;

    /**
     * �����`�����ۂ���\���t���O?B
     *
     * @serial
     */
    private boolean periodic;

    /**
     * �e�Z�O�?���g�� (���̃Z�O�?���g�̒�`��?�ł�) �J�n�p���??[�^�l�̔z��?B
     * <p/>
     * ���̔z��̗v�f?��̓Z�O�?���g?��ɓ�������̂Ƃ���?B
     * </p>
     * <p/>
     * ���̃t�B?[���h��?A���̃N���X�̓Ք�ł̂ݗ��p����?B
     * </p>
     *
     * @serial
     */
    private double[] localStartParams;

    /**
     * �e�Z�O�?���g�� (��?���?�̒�`��?�ł�) �J�n�p���??[�^�l�̔z��?B
     * <p/>
     * ���̔z��̗v�f?��� (�Z�O�?���g?� + 1) �Ƃ�?A
     * ?Ō�̗v�f�ɂ͕�?���?�̒�`���?I���p���??[�^�l��܂߂��̂Ƃ���?B
     * </p>
     * <p/>
     * ���̃t�B?[���h��?A���̃N���X�̓Ք�ł̂ݗ��p����?B
     * </p>
     *
     * @serial
     */
    private double[] globalStartParams;

    /**
     * �Z�O�?���g�̔z��ƊJ�t���O��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param segments ��?���?��?\?�����Z�O�?���g�̔z��
     * @param periodic �����`�����ۂ���\���t���O
     */
    public CompositeCurve3D(CompositeCurveSegment3D[] segments,
                            boolean periodic) {
        super();

        this.segments = new CompositeCurveSegment3D[segments.length];
        this.periodic = periodic;

        this.localStartParams = new double[segments.length];
        this.globalStartParams = new double[segments.length + 1];

        this.globalStartParams[0] = 0;

        for (int i = 0; i < segments.length; i++) {
            CompositeCurveSegment3D seg = segments[i];
            ParameterSection sec = seg.parameterDomain().section();
            this.segments[i] = seg;
            this.localStartParams[i] = sec.start();
            this.globalStartParams[i + 1] = this.globalStartParams[i] + sec.increase();
        }
    }

    /**
     * �Z�O�?���g�̕��?�̔z���^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * segments �̗v�f?��� sense �̗v�f?�����v���Ă��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * ���̃R���X�g���N�^�ł� segments[i] �� sense[i] ����
     * ��?���?�Z�O�?���g ({@link CompositeCurveSegment3D CompositeCurveSegment3D})
     * ��?\�z���邪?A
     * ����?ۂ̊e�Z�O�?���g��?u���̃Z�O�?���g�Ƃ̘A��?�?v�ɂ��Ă�?A
     * ���ꂼ��̋�?�̊􉽓I�ȓ�?�����?A�����I�ɔ��f����?B
     * </p>
     * <p/>
     * ?Ō�̃Z�O�?���g��?I�_��?�?��̃Z�O�?���g�̎n�_��
     * ��v���Ă��Ȃ���ΊJ�����`��?A
     * ��v���Ă���Ε����`��
     * �̕�?���?��?\�z����?B
     * </p>
     * <p/>
     * �ׂ�?����Z�O�?���g���􉽓I�ɘA���łȂ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param segments ��?���?��?\?�����Z�O�?���g�̕��?�̔z��
     * @param sense    ��?���?��?\?�����Z�O�?���g�̌���t���O�̔z��
     * @see CompositeCurveSegment3D
     * @see InvalidArgumentValueException
     */
    public CompositeCurve3D(BoundedCurve3D[] segments,
                            boolean[] sense) {
        super();

        if (segments.length != sense.length)
            throw new InvalidArgumentValueException();

        double dTol = ConditionOfOperation.getCondition().getToleranceForDistance();

        this.segments = new CompositeCurveSegment3D[segments.length];

        this.localStartParams = new double[segments.length];
        this.globalStartParams = new double[segments.length + 1];

        this.globalStartParams[0] = 0;

        int transition = TransitionCode.UNKNOWN;

        for (int i = 0; i < segments.length; i++) {
            int j = (i + 1 == segments.length) ? 0 : i + 1;
            BoundedCurve3D pseg = segments[i];
            BoundedCurve3D nseg = segments[j];
            ParameterSection psec = pseg.parameterDomain().section();
            ParameterSection nsec = nseg.parameterDomain().section();
            double pp = sense[i] ? psec.end() : psec.start();
            double np = sense[j] ? nsec.start() : nsec.end();
            CurveDerivative3D pder = pseg.evaluation(pp);
            CurveDerivative3D nder = nseg.evaluation(np);
            double pcur = pseg.curvature(pp).curvature();
            double ncur = nseg.curvature(np).curvature();

            if (!pder.d0D().identical(nder.d0D())) {
                // �s�A��
                transition = TransitionCode.DISCONTINUOUS;
            } else if (!pder.d1D().identicalDirection(nder.d1D())) {
                // ?�?�s�A��
                transition = TransitionCode.CONTINUOUS;
            } else if (Math.abs(pcur - ncur) >= dTol) {
                // ?�?�A�� & �ȗ��s�A��
                // CONT_SAME_GRADIENT
                transition = TransitionCode.CONTINUOUS;
            } else {
                // ?�?�A�� & �ȗ��A��
                // CONT_SAME_GRADIENT_SAME_CURVATURE
                transition = TransitionCode.CONTINUOUS;
            }

            if ((j != 0) && (transition == TransitionCode.DISCONTINUOUS))
                throw new InvalidArgumentValueException();

            this.segments[i] =
                    new CompositeCurveSegment3D(transition, sense[i], pseg);

            this.localStartParams[i] = psec.start();
            this.globalStartParams[i + 1] = this.globalStartParams[i] + psec.increase();
        }

        this.periodic = (transition == TransitionCode.DISCONTINUOUS) ? false : true;
    }

    /**
     * ���̕�?���?��?\?�����Z�O�?���g���Ԃ�?B
     *
     * @return �Z�O�?���g�̔z��
     */
    CompositeCurveSegment3D[] segments() {
        return (CompositeCurveSegment3D[]) this.segments.clone();
    }

    /**
     * ���̕�?���?�� (?ċA�I��) �Z�O�?���g��ɕ��ⷂ�?B
     *
     * @return �Z�O�?���g�̔z��
     */
    CompositeCurveSegment3D[] decomposeAsSegmentsRecursively() {
        Vector resultList = new Vector();

        for (int i = 0; i < nSegments(); i++) {
            CompositeCurveSegment3D segment = this.segmentAt(i);
            BoundedCurve3D parent = segment.parentCurve();
            if (parent.type() == COMPOSITE_CURVE_3D) {
                CompositeCurve3D parentCmc = (CompositeCurve3D) parent;
                CompositeCurveSegment3D[] parentSegments =
                        parentCmc.decomposeAsSegmentsRecursively();
                CompositeCurveSegment3D revised;
                if (segment.sameSense() == true) {
                    int j;
                    for (j = 0; j < (parentSegments.length - 1); j++)
                        resultList.addElement(parentSegments[j]);
                    revised = parentSegments[j].
                            makeCopyWithTransition(segment.transition());
                    resultList.addElement(revised);
                } else {
                    int j;
                    for (j = (parentSegments.length - 1); j > 0; j--) {
                        revised = parentSegments[j].
                                makeReverseWithTransition(parentSegments[j - 1].transition());
                        resultList.addElement(revised);
                    }
                    revised = parentSegments[j].
                            makeReverseWithTransition(segment.transition());
                    resultList.addElement(revised);
                }
            } else {
                resultList.addElement(segment);
            }
        }

        CompositeCurveSegment3D[] result =
                new CompositeCurveSegment3D[resultList.size()];
        resultList.copyInto(result);
        return result;
    }

    /**
     * ���̕�?���?�����`�����ۂ���Ԃ�?B
     *
     * @return �����`���ł���� true?A����Ȃ��� false
     */
    public boolean periodic() {
        return this.periodic;
    }

    /**
     * ���̕�?���?��?\?�����Z�O�?���g��?���Ԃ�?B
     *
     * @return �Z�O�?���g��?�
     */
    public int nSegments() {
        return this.segments.length;
    }

    /**
     * ���̕�?���?�� ith �Ԗڂ̃Z�O�?���g��Ԃ�?B
     *
     * @param ith �Z�O�?���g�̃C���f�b�N�X
     * @return ith �Ԗڂ̃Z�O�?���g
     */
    public CompositeCurveSegment3D segmentAt(int ith) {
        return this.segments[ith];
    }

    /**
     * �Z�O�?���g�̃C���f�b�N�X�Ƃ��̃Z�O�?���g�ł̋�?��p���??[�^�l��\���Ք�N���X?B
     */
    class CompositeIndexParam {
        /**
         * �Z�O�?���g�̃C���f�b�N�X?B
         */
        int index;

        /**
         * ��?��p���??[�^�l?B
         * <p/>
         * ���̒l�� [0, �Z�O�?���g�̃p���??[�^��Ԃ̑?���l] �Ɏ�܂�?B
         * </p>
         */
        double param;
    }

    /**
     * ���̕�?���?�ɑ΂��ė^����ꂽ�p���??[�^�l��?A
     * ����ɑΉ�����Z�O�?���g�̃C���f�b�N�X��
     * ���̃Z�O�?���g�ł̋�?��I�ȃp���??[�^�l�ɕϊ�����?B
     * <p/>
     * �^����ꂽ�p���??[�^�l�����̋�?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �Z�O�?���g�̃C���f�b�N�X�Ƌ�?��p���??[�^�l
     */
    private CompositeIndexParam getIndexParam(double param) {
        ParameterDomain domain = parameterDomain();
        CompositeIndexParam ip = new CompositeIndexParam();

        checkValidity(param);
        param = domain.wrap(param);
        ip.index = GeometryUtils.bsearchDoubleArray(globalStartParams, 1, (nSegments() - 1), param);
        ip.param = localStartParams[ip.index] + (param - globalStartParams[ip.index]);
        return ip;
    }

    /**
     * ���̕�?���?�̂���Z�O�?���g�̃C���f�b�N�X��
     * ���̃Z�O�?���g�ł̋�?��I�ȃp���??[�^�l��
     * ���̕�?���?�ɑ΂���p���??[�^�l�ɕϊ�����?B
     *
     * @param index �Z�O�?���g�̃C���f�b�N�X
     * @param param �Z�O�?���g�ł̋�?��p���??[�^�l
     * @return ���̕�?���?�ɑ΂���p���??[�^�l
     */
    private double getCompositeParam(int index, double param) {
        return globalStartParams[index] + (param - localStartParams[index]);
    }

    /**
     * ���̕�?���?�̊J�n�_��Ԃ�?B
     * <p/>
     * ���̕�?���?�����`����?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @return �J�n�_
     */
    public Point3D startPoint() {
        if (isPeriodic())
            return null;

        return segments[0].startPoint();
    }

    /**
     * ���̕�?���?��?I���_��Ԃ�?B
     * <p/>
     * ���̕�?���?�����`����?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @return ?I���_
     */
    public Point3D endPoint() {
        if (isPeriodic())
            return null;

        int n = nSegments();
        return segments[n - 1].endPoint();
    }

    /**
     * {@link #length(ParameterSection) length(ParameterSection)}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class LengthAccumulator extends SegmentAccumulator {
        /**
         * ��?�̎w��̋�Ԃ̒���?B
         */
        double length;

        /**
         * leng �� 0 ��?�����?B
         *
         * @param nsegs ��?ۂƂȂ�Z�O�?���g��?�
         */
        void allocate(int nsegs) {
            length = 0.0;
        }

        /**
         * �^����ꂽ�Z�O�?���g�̎w��̋�Ԃ̓��̂�� leng �ɑ���?B
         *
         * @param seg       �Z�O�?���g
         * @param sec       �p���??[�^���
         * @param compIndex �Z�O�?���g�̔�?�
         */
        void doit(CompositeCurveSegment3D seg,
                  ParameterSection sec,
                  int compIndex) {
            length += seg.length(sec);
        }

        /**
         * ��?�̎w��̋�Ԃ̒�����Ԃ�?B
         *
         * @param leng �̒l
         */
        double extract() {
            return length;
        }
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ�?B
     * <p/>
     * pint �̑?���l�͕��ł©�܂�Ȃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     * @see ParameterOutOfRange
     */
    public double length(ParameterSection pint) {
        LengthAccumulator acc = new LengthAccumulator();
        acc.accumulate(pint);

        return acc.extract();
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public Point3D coordinates(double param) {
        CompositeIndexParam ip = getIndexParam(param);
        return segments[ip.index].coordinates(ip.param);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public Vector3D tangentVector(double param) {
        CompositeIndexParam ip = getIndexParam(param);
        return segments[ip.index].tangentVector(ip.param);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public CurveCurvature3D curvature(double param) {
        CompositeIndexParam ip = getIndexParam(param);
        return segments[ip.index].curvature(ip.param);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̃��C����Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ���C��
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public double torsion(double param) {
        CompositeIndexParam ip = getIndexParam(param);
        return segments[ip.index].torsion(ip.param);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ����?�
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterOutOfRange
     */
    public CurveDerivative3D evaluation(double param) {
        CompositeIndexParam ip = getIndexParam(param);
        return segments[ip.index].evaluation(ip.param);
    }

    /**
     * {@link #singular() singular()}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class SingularAccumulator extends SegmentAccumulator {
        CompositeCurve3D curve;
        Vector singularVec;
        IndefiniteSolutionException inf;

        SingularAccumulator(CompositeCurve3D curve) {
            this.curve = curve;
            inf = null;
        }

        void allocate(int nsegs) {
            singularVec = new Vector();
        }

        void doit(CompositeCurveSegment3D seg,
                  ParameterSection sec,
                  int compIndex) {
            PointOnCurve3D[] singular;

            try {
                singular = seg.singular();
            } catch (IndefiniteSolutionException e) {
                inf = e;
                return;
            }

            double param;
            for (int i = 0; i < singular.length; i++) {
                param = getCompositeParam(compIndex, singular[i].parameter());
                singularVec.addElement
                        (new PointOnCurve3D(curve, param, doCheckDebug));
            }

            if (seg.transition() == TransitionCode.CONTINUOUS) {
                param = getCompositeParam(compIndex, sec.end());
                singularVec.addElement
                        (new PointOnCurve3D(curve, param, doCheckDebug));
            }
        }

        PointOnCurve3D[] extract() throws IndefiniteSolutionException {
            if (inf != null)
                throw inf;

            PointOnCurve3D[] thisSingular =
                    new PointOnCurve3D[singularVec.size()];
            singularVec.copyInto(thisSingular);

            return thisSingular;
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
        SingularAccumulator acc = new SingularAccumulator(this);

        try {
            acc.accumulate(parameterDomain().section());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
        return acc.extract();
    }

    /**
     * {@link #inflexion() inflexion()}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class InflexionAccumulator extends SegmentAccumulator {
        CompositeCurve3D curve;
        Vector inflexionVec;
        IndefiniteSolutionException inf;

        InflexionAccumulator(CompositeCurve3D curve) {
            this.curve = curve;
            inf = null;
        }

        void allocate(int nsegs) {
            inflexionVec = new Vector();
        }

        void doit(CompositeCurveSegment3D seg,
                  ParameterSection sec,
                  int compIndex) {
            PointOnCurve3D[] inflexion;

            try {
                inflexion = seg.inflexion();
            } catch (IndefiniteSolutionException e) {
                inf = e;
                return;
            }

            double param;
            for (int i = 0; i < inflexion.length; i++) {
                param = getCompositeParam
                        (compIndex, inflexion[i].parameter());
                inflexionVec.addElement
                        (new PointOnCurve3D(curve, param, doCheckDebug));
            }
            if (seg.transition() == TransitionCode.CONT_SAME_GRADIENT) {
                param = getCompositeParam(compIndex, sec.end());
                inflexionVec.addElement
                        (new PointOnCurve3D(curve, param, doCheckDebug));
            }
        }

        PointOnCurve3D[] extract() throws IndefiniteSolutionException {
            if (inf != null)
                throw inf;

            PointOnCurve3D[] thisInflexion =
                    new PointOnCurve3D[inflexionVec.size()];
            inflexionVec.copyInto(thisInflexion);

            return thisInflexion;
        }
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
        InflexionAccumulator acc = new InflexionAccumulator(this);

        try {
            acc.accumulate(parameterDomain().section());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
        return acc.extract();
    }

    /**
     * ?��?��?ۂƂȂ�p���??[�^��ԂɊ܂܂��Z�O�?���g���ɔC�ӂ�?��?��{�����߂̒�?ۃN���X?B
     */
    private abstract class SegmentAccumulator {
        /**
         * �^����ꂽ�Z�O�?���g�̎w��͈̔͂ɂ���?A���炩��?��?��?s�Ȃ���?ۃ?�\�b�h?B
         * <p/>
         * ���̃?�\�b�h��
         * {@link #accumulate(ParameterSection) accumulate(ParameterSection)}
         * �̒��ŌĂ�?o�����?B
         * </p>
         *
         * @param seg       �Z�O�?���g
         * @param sec       �p���??[�^���
         * @param compIndex �Z�O�?���g�̔�?�
         */
        abstract void doit(CompositeCurveSegment3D seg,
                           ParameterSection sec,
                           int compIndex);

        /**
         * �^����ꂽ�Z�O�?���g�̎w��͈̔͂ɂ���?A���炩��?��?��?s�Ȃ���?ۃ?�\�b�h?B
         * <p/>
         * ���̃?�\�b�h��
         * {@link #accumulate(ParameterSection) accumulate(ParameterSection)}
         * �̒��ŌĂ�?o�����?B
         * </p>
         *
         * @param seg       �Z�O�?���g
         * @param sp        �p���??[�^��Ԃ̊J�n�l
         * @param ep        �p���??[�^��Ԃ�?I���l
         * @param compIndex �Z�O�?���g�̔�?�
         */
        void doit(CompositeCurveSegment3D seg, double sp, double ep,
                  int compIndex) {
            doit(seg, new ParameterSection(sp, ep), compIndex);
        }

        /**
         * ���炩��?��?��n�߂邽�߂�?����?s�Ȃ���?ۃ?�\�b�h?B
         * <p/>
         * ���̃?�\�b�h��
         * {@link #accumulate(ParameterSection) accumulate(ParameterSection)}
         * �̒���
         * {@link #doit(CompositeCurveSegment3D,double,double,int)
         * doit(CompositeCurveSegment3D, double, double, int)}
         * ��Ă�?o���O�ɌĂ�?o�����?B
         * </p>
         */
        abstract void allocate(int nsegs);

        /**
         * �^����ꂽ�p���??[�^��ԂɊ܂܂��Z�O�?���g����
         * {@link #doit(CompositeCurveSegment3D,double,double,int)
         * doit(CompositeCurveSegment3D, double, double, int)}
         * ��Ă�?o��?B
         */
        void accumulate(ParameterSection pint) {
            CompositeIndexParam sx;
            CompositeIndexParam ex;
            ParameterDomain domain = parameterDomain();
            boolean wraparound;

            if (domain.isPeriodic()) {
                double sp = domain.wrap(pint.start());
                double ep = sp + pint.increase();
                sx = getIndexParam(sp);
                ex = getIndexParam(ep);
                wraparound = (domain.section().increase() < ep);
            } else {
                sx = getIndexParam(pint.lower());
                ex = getIndexParam(pint.upper());
                wraparound = false;
            }

            int nsegs;
            if (wraparound) {
                // tail: segments.length - sx.index
                // head: ex.index + 1
                nsegs = segments.length + ex.index - sx.index + 1;
            } else {
                nsegs = ex.index - sx.index + 1;
            }
            allocate(nsegs);

            CompositeCurveSegment3D seg;

            if (nsegs == 1)
                doit(segments[sx.index], sx.param, ex.param, sx.index);
            else if (wraparound) {
                seg = segments[sx.index];
                doit(seg, sx.param, seg.eParameter(), sx.index);

                int i;
                for (i = sx.index + 1; i < segments.length; i++) {
                    seg = segments[i];
                    doit(seg, seg.sParameter(), seg.eParameter(), i);
                }

                for (i = 0; i < ex.index; i++) {
                    seg = segments[i];
                    doit(seg, seg.sParameter(), seg.eParameter(), i);
                }
                seg = segments[ex.index];
                doit(seg, seg.sParameter(), ex.param, ex.index);
            } else {
                seg = segments[sx.index];
                doit(seg, sx.param, seg.eParameter(), sx.index);

                int i;
                for (i = sx.index + 1; i < ex.index; i++) {
                    seg = segments[i];
                    doit(seg, seg.sParameter(), seg.eParameter(), i);
                }
                seg = segments[ex.index];
                doit(seg, seg.sParameter(), ex.param, ex.index);
            }
        }
    }

    /**
     * {@link #projectFrom(Point3D) projectFrom(Point3D)}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class ProjectionAccumulator extends SegmentAccumulator {
        Point3D point;
        CompositeCurve3D curve;
        PointOnGeometryList projList;
        IndefiniteSolutionException inf;

        ProjectionAccumulator(Point3D point,
                              CompositeCurve3D curve) {
            this.curve = curve;
            this.point = point;
            inf = null;
        }

        void allocate(int nsegs) {
            projList = new PointOnGeometryList();
        }

        void doit(CompositeCurveSegment3D seg,
                  ParameterSection sec,
                  int compIndex) {
            PointOnCurve3D[] proj;
            try {
                proj = seg.projectFrom(point);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            } catch (IndefiniteSolutionException e) {
                inf = e;
                return;
            }

            for (int i = 0; i < proj.length; i++) {
                double param = getCompositeParam(compIndex, proj[i].parameter());
                projList.addPoint(curve, param);
            }
        }

        PointOnCurve3D[] extract()
                throws IndefiniteSolutionException {
            if (inf != null)
                throw inf;

            return projList.toPointOnCurve3DArray();
        }
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     */
    public PointOnCurve3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        ProjectionAccumulator acc = new ProjectionAccumulator(point, this);

        try {
            acc.accumulate(parameterDomain().section());
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
        return acc.extract();
    }

    /**
     * {@link #toPolyline(ParameterSection,ToleranceForDistance)
     * toPolyline(ParameterSection, ToleranceForDistance)}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class ToPolylineAccumulator extends SegmentAccumulator {
        ToleranceForDistance tol;
        Polyline3D[] pls;
        CompositeCurveSegment3D[] segs;
        int[] compIndex;
        CompositeCurve3D curve;
        int segIndex;

        ToPolylineAccumulator(ToleranceForDistance tol,
                              CompositeCurve3D curve) {
            this.tol = tol;
            this.curve = curve;
            segIndex = 0;
        }

        void allocate(int nsegs) {
            pls = new Polyline3D[nsegs];
            segs = new CompositeCurveSegment3D[nsegs];
            compIndex = new int[nsegs];
        }

        void doit(CompositeCurveSegment3D seg,
                  ParameterSection sec,
                  int compIndex) {
            segs[segIndex] = seg;
            this.compIndex[segIndex] = compIndex;
            try {
                pls[segIndex] = seg.toPolyline(sec, tol);
            } catch (ZeroLengthException e) {
                pls[segIndex] = null;
            }
            segIndex++;
        }

        Polyline3D extract() {
            int npnts = 1;
            int i, j, k;

            for (i = 0; i < pls.length; i++) {
                if (pls[i] == null)
                    continue;
                npnts += (pls[i].nPoints() - 1);
            }

            if (npnts < 2)
                throw new ZeroLengthException();

            PointOnCurve3D[] points = new PointOnCurve3D[npnts];

            k = 0;

            double param;
            for (i = 0; i < pls.length; i++) {
                if (pls[i] == null)
                    continue;
                for (j = 0; j < pls[i].nPoints(); j++) {
                    PointOnCurve3D pnts =
                            (PointOnCurve3D) pls[i].pointAt(j);
                    param = getCompositeParam(compIndex[i], pnts.parameter());
                    if (i == 0 || j != 0) {
                        try {
                            points[k++] = new PointOnCurve3D(curve, param, doCheckDebug);
                        } catch (InvalidArgumentValueException e) {
                            throw new FatalException();
                        }
                    }
                }
            }

            return new Polyline3D(points);
        }
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��?A
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     */
    public Polyline3D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        if (pint.increase() < 0.0) {
            return toPolyline(new ParameterSection(pint.end(),
                    -pint.increase()),
                    tol).reverse();
        }

        ToPolylineAccumulator accm = new ToPolylineAccumulator(tol, this);
        accm.accumulate(pint);

        return accm.extract();
    }

    /**
     * {@link #toBsplineCurve(ParameterSection)
     * toBsplineCurve(ParameterSection)}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class ToBsplineCurveAccumulator extends SegmentAccumulator {
        BsplineCurve3D result;

        ToBsplineCurveAccumulator() {
        }

        void allocate(int nsegs) {
            result = null;
        }

        void doit(CompositeCurveSegment3D seg,
                  ParameterSection sec,
                  int compIndex) {
            try {
                BsplineCurve3D bsc = seg.toBsplineCurve(sec);
                result = (result == null) ? bsc : result.mergeIfContinuous(bsc);
            } catch (TwoGeomertiesAreNotContinuousException e) {
                // ����ł����̂�?H
                throw new FatalException();
            }
        }

        BsplineCurve3D extract() {
            return result;
        }
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
        ToBsplineCurveAccumulator accm = new ToBsplineCurveAccumulator();
        accm.accumulate(pint.positiveIncrease());
        BsplineCurve3D result = accm.extract();

        // KOKO : ���Ă���?�?���?��?

        if (pint.increase() < 0.0)
            result = result.reverse();

        return result;
    }

    /**
     * {@link #doIntersect(ParametricCurve3D,boolean)
     * doIntersect(ParametricCurve3D, boolean)}
     * ��?��?���邽�߂� SegmentAccumulator ��
     */
    private class IntersectionAccumulator extends SegmentAccumulator {
        ParametricCurve3D mate;
        CompositeCurve3D curve;
        Vector intsvec;

        IntersectionAccumulator(ParametricCurve3D mate,
                                CompositeCurve3D curve) {
            this.curve = curve;
            this.mate = mate;
        }

        void allocate(int nsegs) {
            intsvec = new Vector();
        }

        void doit(CompositeCurveSegment3D seg,
                  ParameterSection sec,
                  int compIndex) {

            IntersectionPoint3D[] ints = seg.intersect(mate);

            for (int i = 0; i < ints.length; i++) {
                PointOnCurve3D pnt1 =
                        (PointOnCurve3D) ints[i].pointOnGeometry1();
                double cparam = pnt1.parameter();
                double sparam = getCompositeParam(compIndex, cparam);

                PointOnCurve3D thisPnts =
                        new PointOnCurve3D(curve, sparam, doCheckDebug);
                IntersectionPoint3D thisInts =
                        new IntersectionPoint3D(thisPnts,
                                ints[i].pointOnGeometry2(), doCheckDebug);
                intsvec.addElement(thisInts);
            }
        }

        IntersectionPoint3D[] extract(boolean doExchange) {
            IntersectionPoint3D[] ints =
                    new IntersectionPoint3D[intsvec.size()];
            intsvec.copyInto(ints);
            if (doExchange)
                for (int i = 0; i < ints.length; i++)
                    ints[i] = ints[i].exchange();
            return ints;
        }
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * ���̋�?�̊e�Z�O�?���g�Ƒ��̋�?�̌�_��?�߂����?A
     * �����̃Z�O�?���g�ɑ΂���p���??[�^�l��
     * ���̋�?�ɑ΂���p���??[�^�l�ɕϊ��������
     * ��?u��_?v�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?�
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    private IntersectionPoint3D[] doIntersect(ParametricCurve3D mate,
                                              boolean doExchange) {
        IntersectionAccumulator acc = new IntersectionAccumulator(mate, this);
        acc.accumulate(parameterDomain().section());
        return acc.extract(doExchange);
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
     * intersect(�Ȗ�) �̂��߂� SegmentAccumulator ��
     */
    private class IntersectionWithSurfaceAccumulator extends SegmentAccumulator {
        ParametricSurface3D mate;
        CompositeCurve3D curve;
        CurveSurfaceInterferenceList intfList;

        IntersectionWithSurfaceAccumulator(ParametricSurface3D mate,
                                           CompositeCurve3D curve) {
            this.curve = curve;
            this.mate = mate;
        }

        void allocate(int nsegs) {
            intfList = new CurveSurfaceInterferenceList(curve, mate);
        }

        void doit(CompositeCurveSegment3D seg,
                  ParameterSection sec,
                  int compIndex) {
            IntersectionPoint3D[] intp = seg.intersect(mate);

            for (int i = 0; i < intp.length; i++) {
                PointOnCurve3D crvPnt =
                        (PointOnCurve3D) intp[i].pointOnGeometry1();
                double param = crvPnt.parameter();
                double compositeParam = getCompositeParam(compIndex, param);
                PointOnSurface3D srfPnt =
                        (PointOnSurface3D) intp[i].pointOnGeometry2();
                intfList.addAsIntersection(intp[i].coordinates(), compositeParam,
                        srfPnt.uParameter(), srfPnt.vParameter());
            }
        }

        IntersectionPoint3D[] extract(boolean doExchange) {
            return intfList.toIntersectionPoint3DArray(doExchange);
        }
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
        IntersectionWithSurfaceAccumulator acc =
                new IntersectionWithSurfaceAccumulator(mate, this);
        acc.accumulate(parameterDomain().section());
        return acc.extract(doExchange);
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
        return doIntersect(mate, doExchange);
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
        return this.getInterference(mate, false);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?�̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * ���̋�?�̊e�Z�O�?���g�Ƒ��̋�?�̊�?�?�߂����?A
     * �����̃Z�O�?���g�ɑ΂���p���??[�^�l��
     * ���̋�?�ɑ΂���p���??[�^�l�ɕϊ��������
     * ��?u��?�?v�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?�
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     */
    private CurveCurveInterference3D[] getInterference(BoundedCurve3D mate,
                                                       boolean doExchange) {
        CurveCurveInterferenceList interferenceList
                = new CurveCurveInterferenceList(this, mate);

        // this �̊e�Z�O�?���g�ɑ΂���
        for (int i = 0; i < nSegments(); i++) {
            // �Z�O�?���g���x���ł̊�?𓾂�
            CurveCurveInterference3D[] localInterferences
                    = this.segmentAt(i).interfere(mate, false);

            // ��_�㊃X�g�ɒǉB���
            Vector intsList
                    = CurveCurveInterferenceList.extractIntersections
                    (localInterferences);
            for (Enumeration e = intsList.elements(); e.hasMoreElements();) {
                IntersectionPoint3D ints
                        = (IntersectionPoint3D) e.nextElement();
                interferenceList.addAsIntersection
                        (ints.coordinates(),
                                ints.pointOnCurve1().parameter(),
                                this.getCompositeParam(i, ints.pointOnCurve2().parameter()));
            }

            // ?d���㊃X�g�ɒǉB���
            Vector ovlpList
                    = CurveCurveInterferenceList.extractOverlaps
                    (localInterferences);
            for (Enumeration e = ovlpList.elements(); e.hasMoreElements();) {
                OverlapCurve3D ovlp
                        = (OverlapCurve3D) e.nextElement();
                interferenceList.addAsOverlap
                        (ovlp.start1(),
                                this.getCompositeParam(i, ovlp.start2()),
                                ovlp.increase1(),
                                ovlp.increase2());
            }
        }

        interferenceList.removeOverlapsContainedInOtherOverlap();
        interferenceList.removeIntersectionsContainedInOverlap();

        return interferenceList.toCurveCurveInterference3DArray(doExchange);
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
     * @see #getInterference(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(BoundedLine3D mate,
                                         boolean doExchange) {
        return this.getInterference(mate, doExchange);
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
     * @see #getInterference(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(Polyline3D mate,
                                         boolean doExchange) {
        return this.getInterference(mate, doExchange);
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
     * @see #getInterference(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(PureBezierCurve3D mate,
                                         boolean doExchange) {
        return this.getInterference(mate, doExchange);
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
     * @see #getInterference(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(BsplineCurve3D mate,
                                         boolean doExchange) {
        return this.getInterference(mate, doExchange);
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
     * @see #getInterference(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(TrimmedCurve3D mate,
                                         boolean doExchange) {
        return this.getInterference(mate, doExchange);
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
     * @see #getInterference(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(CompositeCurveSegment3D mate,
                                         boolean doExchange) {
        return this.getInterference(mate, doExchange);
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
     * @see #getInterference(BoundedCurve3D,boolean)
     */
    CurveCurveInterference3D[] interfere(CompositeCurve3D mate,
                                         boolean doExchange) {
        return this.getInterference(mate, doExchange);
    }

    /**
     * ���̋�?��?A�^����ꂽ�x�N�g����?]�Bĕ�?s�ړ�������?��Ԃ�?B
     *
     * @param moveVec ��?s�ړ��̕��Ɨʂ�\���x�N�g��
     * @return ��?s�ړ���̋�?�
     */
    public ParametricCurve3D parallelTranslate(Vector3D moveVec) {
        int nSeg = nSegments();
        CompositeCurveSegment3D[] newSegs = new CompositeCurveSegment3D[nSeg];
        for (int i = 0; i < nSeg; i++)
            newSegs[i] = (CompositeCurveSegment3D) segmentAt(i).parallelTranslate(moveVec);
        return new CompositeCurve3D(newSegs, periodic);
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     *
     * @return �p���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        try {
            return new ParameterDomain(periodic, 0,
                    globalStartParams[nSegments()]);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve3D#COMPOSITE_CURVE_3D ParametricCurve3D.COMPOSITE_CURVE_3D}
     */
    int type() {
        return COMPOSITE_CURVE_3D;
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ?�� true
     */
    public boolean isFreeform() {
        return true;
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
        int n_segs = nSegments();
        CompositeCurveSegment3D[] segs = new CompositeCurveSegment3D[n_segs];

        for (int i = 0; i < n_segs; i++)
            segs[i] = (CompositeCurveSegment3D) segmentAt(i).rotateZ(trns, rCos, rSin);

        return new CompositeCurve3D(segs, periodic());
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
        CompositeCurveSegment3D[] tSegments =
                new CompositeCurveSegment3D[this.nSegments()];
        for (int i = 0; i < this.nSegments(); i++)
            tSegments[i] = (CompositeCurveSegment3D)
                    this.segmentAt(i).transformBy(reverseTransform,
                            transformationOperator,
                            transformedGeometries);
        return new CompositeCurve3D(tSegments, this.periodic());
    }

    /**
     * ���̋�?�|�����C���̕�����܂ނ��ۂ���Ԃ�?B
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C����܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean hasPolyline() {
        for (int i = 0; i < this.nSegments(); i++) {
            if (this.segmentAt(i).hasPolyline() == true)
                return true;
        }
        return false;
    }

    /**
     * ���̋�?�|�����C���̕��������łł��Ă��邩�ۂ���Ԃ�?B
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C��������܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean isComposedOfOnlyPolylines() {
        for (int i = 0; i < this.nSegments(); i++) {
            if (this.segmentAt(i).isComposedOfOnlyPolylines() == false)
                return false;
        }
        return true;
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
        writer.println(indent_tab + "\tsegments");
        for (int i = 0; i < nSegments(); i++) {
            segments[i].output(writer, indent + 2);
        }
        writer.println(indent_tab + "\tperiodic\t" + periodic);
        writer.println(indent_tab + "End");
    }
}
