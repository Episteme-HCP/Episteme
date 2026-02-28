/*
 * �Q���� : ��?���?��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CompositeCurve2D.java,v 1.4 2006/03/01 21:15:54 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
 * �Q���� : ��?���?��\���N���X?B
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
 * @version $Revision: 1.4 $, $Date: 2006/03/01 21:15:54 $
 * @see CompositeCurveSegment2D
 */

public class CompositeCurve2D extends BoundedCurve2D {

    /**
     * ��?���?��?\?�����Z�O�?���g�̔z��?B
     *
     * @serial
     */
    private CompositeCurveSegment2D[] segments;

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
    public CompositeCurve2D(CompositeCurveSegment2D[] segments,
                            boolean periodic) {
        super();

        this.segments = new CompositeCurveSegment2D[segments.length];
        this.periodic = periodic;

        this.localStartParams = new double[segments.length];
        this.globalStartParams = new double[segments.length + 1];

        this.globalStartParams[0] = 0;

        for (int i = 0; i < segments.length; i++) {
            CompositeCurveSegment2D seg = segments[i];
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
     * ��?���?�Z�O�?���g ({@link CompositeCurveSegment2D CompositeCurveSegment2D})
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
     * @see CompositeCurveSegment2D
     * @see InvalidArgumentValueException
     */
    public CompositeCurve2D(BoundedCurve2D[] segments,
                            boolean[] sense) {
        super();

        if (segments.length != sense.length)
            throw new InvalidArgumentValueException();

        double dTol = ConditionOfOperation.getCondition().getToleranceForDistance();

        this.segments = new CompositeCurveSegment2D[segments.length];

        this.localStartParams = new double[segments.length];
        this.globalStartParams = new double[segments.length + 1];

        this.globalStartParams[0] = 0;

        int transition = TransitionCode.UNKNOWN;

        for (int i = 0; i < segments.length; i++) {
            int j = (i + 1 == segments.length) ? 0 : i + 1;
            BoundedCurve2D pseg = segments[i];
            BoundedCurve2D nseg = segments[j];
            ParameterSection psec = pseg.parameterDomain().section();
            ParameterSection nsec = nseg.parameterDomain().section();
            double pp = sense[i] ? psec.end() : psec.start();
            double np = sense[j] ? nsec.start() : nsec.end();
            CurveDerivative2D pder = pseg.evaluation(pp);
            CurveDerivative2D nder = nseg.evaluation(np);
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
                    new CompositeCurveSegment2D(transition, sense[i], pseg);

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
    CompositeCurveSegment2D[] segments() {
        return (CompositeCurveSegment2D[]) this.segments.clone();
    }

    /**
     * ���̕�?���?�� (?ċA�I��) �Z�O�?���g��ɕ��ⷂ�?B
     *
     * @return �Z�O�?���g�̔z��
     */
    CompositeCurveSegment2D[] decomposeAsSegmentsRecursively() {
        Vector resultList = new Vector();

        for (int i = 0; i < nSegments(); i++) {
            CompositeCurveSegment2D segment = this.segmentAt(i);
            BoundedCurve2D parent = segment.parentCurve();
            if (parent.type() == COMPOSITE_CURVE_2D) {
                CompositeCurve2D parentCmc = (CompositeCurve2D) parent;
                CompositeCurveSegment2D[] parentSegments =
                        parentCmc.decomposeAsSegmentsRecursively();
                CompositeCurveSegment2D revised;
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

        CompositeCurveSegment2D[] result =
                new CompositeCurveSegment2D[resultList.size()];
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
    public CompositeCurveSegment2D segmentAt(int ith) {
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
    public Point2D startPoint() {
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
    public Point2D endPoint() {
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
        void doit(CompositeCurveSegment2D seg,
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
    public Point2D coordinates(double param) {
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
    public Vector2D tangentVector(double param) {
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
    public CurveCurvature2D curvature(double param) {
        CompositeIndexParam ip = getIndexParam(param);
        return segments[ip.index].curvature(ip.param);
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
    public CurveDerivative2D evaluation(double param) {
        CompositeIndexParam ip = getIndexParam(param);
        return segments[ip.index].evaluation(ip.param);
    }

    /**
     * {@link #singular() singular()}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class SingularAccumulator extends SegmentAccumulator {
        CompositeCurve2D curve;
        Vector singularVec;
        IndefiniteSolutionException inf;

        SingularAccumulator(CompositeCurve2D curve) {
            this.curve = curve;
            inf = null;
        }

        void allocate(int nsegs) {
            singularVec = new Vector();
        }

        void doit(CompositeCurveSegment2D seg,
                  ParameterSection sec,
                  int compIndex) {
            PointOnCurve2D[] singular;

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
                        (new PointOnCurve2D(curve, param, doCheckDebug));
            }

            if (seg.transition() == TransitionCode.CONTINUOUS) {
                param = getCompositeParam(compIndex, sec.end());
                singularVec.addElement
                        (new PointOnCurve2D(curve, param, doCheckDebug));
            }
        }

        PointOnCurve2D[] extract() throws IndefiniteSolutionException {
            if (inf != null)
                throw inf;

            PointOnCurve2D[] thisSingular =
                    new PointOnCurve2D[singularVec.size()];
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
    public PointOnCurve2D[] singular() throws IndefiniteSolutionException {
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
        CompositeCurve2D curve;
        Vector inflexionVec;
        IndefiniteSolutionException inf;

        InflexionAccumulator(CompositeCurve2D curve) {
            this.curve = curve;
            inf = null;
        }

        void allocate(int nsegs) {
            inflexionVec = new Vector();
        }

        void doit(CompositeCurveSegment2D seg,
                  ParameterSection sec,
                  int compIndex) {
            PointOnCurve2D[] inflexion;

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
                        (new PointOnCurve2D(curve, param, doCheckDebug));
            }
            if (seg.transition() == TransitionCode.CONT_SAME_GRADIENT) {
                param = getCompositeParam(compIndex, sec.end());
                inflexionVec.addElement
                        (new PointOnCurve2D(curve, param, doCheckDebug));
            }
        }

        PointOnCurve2D[] extract() throws IndefiniteSolutionException {
            if (inf != null)
                throw inf;

            PointOnCurve2D[] thisInflexion =
                    new PointOnCurve2D[inflexionVec.size()];
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
    public PointOnCurve2D[] inflexion() throws IndefiniteSolutionException {
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
        abstract void doit(CompositeCurveSegment2D seg,
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
        void doit(CompositeCurveSegment2D seg, double sp, double ep,
                  int compIndex) {
            doit(seg, new ParameterSection(sp, ep), compIndex);
        }

        /**
         * ���炩��?��?��n�߂邽�߂�?����?s�Ȃ���?ۃ?�\�b�h?B
         * <p/>
         * ���̃?�\�b�h��
         * {@link #accumulate(ParameterSection) accumulate(ParameterSection)}
         * �̒���
         * {@link #doit(CompositeCurveSegment2D,double,double,int)
         * doit(CompositeCurveSegment2D, double, double, int)}
         * ��Ă�?o���O�ɌĂ�?o�����?B
         * </p>
         */
        abstract void allocate(int nsegs);

        /**
         * �^����ꂽ�p���??[�^��ԂɊ܂܂��Z�O�?���g����
         * {@link #doit(CompositeCurveSegment2D,double,double,int)
         * doit(CompositeCurveSegment2D, double, double, int)}
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

            CompositeCurveSegment2D seg;

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
     * {@link #projectFrom(Point2D) projectFrom(Point2D)}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class ProjectionAccumulator extends SegmentAccumulator {
        Point2D point;
        CompositeCurve2D curve;
        PointOnGeometryList projList;
        IndefiniteSolutionException inf;

        ProjectionAccumulator(Point2D point,
                              CompositeCurve2D curve) {
            this.curve = curve;
            this.point = point;
            inf = null;
        }

        void allocate(int nsegs) {
            projList = new PointOnGeometryList();
        }

        void doit(CompositeCurveSegment2D seg,
                  ParameterSection sec,
                  int compIndex) {
            PointOnCurve2D[] proj;
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

        PointOnCurve2D[] extract()
                throws IndefiniteSolutionException {
            if (inf != null)
                throw inf;

            return projList.toPointOnCurve2DArray();
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
    public PointOnCurve2D[] projectFrom(Point2D point)
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
        Polyline2D[] pls;
        CompositeCurveSegment2D[] segs;
        int[] compIndex;
        CompositeCurve2D curve;
        int segIndex;

        ToPolylineAccumulator(ToleranceForDistance tol,
                              CompositeCurve2D curve) {
            this.tol = tol;
            this.curve = curve;
            segIndex = 0;
        }

        void allocate(int nsegs) {
            pls = new Polyline2D[nsegs];
            segs = new CompositeCurveSegment2D[nsegs];
            compIndex = new int[nsegs];
        }

        void doit(CompositeCurveSegment2D seg,
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

        Polyline2D extract() {
            int npnts = 1;
            int i, j, k;

            for (i = 0; i < pls.length; i++) {
                if (pls[i] == null)
                    continue;
                npnts += (pls[i].nPoints() - 1);
            }

            if (npnts < 2)
                throw new ZeroLengthException();

            PointOnCurve2D[] points = new PointOnCurve2D[npnts];

            k = 0;

            double param;
            for (i = 0; i < pls.length; i++) {
                if (pls[i] == null)
                    continue;
                for (j = 0; j < pls[i].nPoints(); j++) {
                    PointOnCurve2D pnts =
                            (PointOnCurve2D) pls[i].pointAt(j);
                    param = getCompositeParam(compIndex[i], pnts.parameter());
                    if (i == 0 || j != 0) {
                        try {
                            points[k++] = new PointOnCurve2D(curve, param, doCheckDebug);
                        } catch (InvalidArgumentValueException e) {
                            throw new FatalException();
                        }
                    }
                }
            }

            return new Polyline2D(points);
        }
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��?A
     * ���̋�?��x?[�X�Ƃ��� PointOnCurve2D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param pint ��?�ߎ�����p���??[�^���
     * @param tol  �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     */
    public Polyline2D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        if (pint.increase() < 0.0) {
            return toPolyline(pint.reverse(), tol).reverse();
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
        BsplineCurve2D result;

        ToBsplineCurveAccumulator() {
        }

        void allocate(int nsegs) {
            result = null;
        }

        void doit(CompositeCurveSegment2D seg,
                  ParameterSection sec,
                  int compIndex) {
            try {
                BsplineCurve2D bsc = seg.toBsplineCurve(sec);
                result = (result == null) ? bsc : result.mergeIfContinuous(bsc);
            } catch (TwoGeomertiesAreNotContinuousException e) {
                // ����ł����̂�?H
                throw new FatalException();
            }
        }

        BsplineCurve2D extract() {
            return result;
        }
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        ToBsplineCurveAccumulator accm = new ToBsplineCurveAccumulator();
        accm.accumulate(pint.positiveIncrease());
        BsplineCurve2D result = accm.extract();

        // KOKO : ���Ă���?�?���?��?

        if (pint.increase() < 0.0)
            result = result.reverse();

        return result;
    }

    /**
     * {@link #doIntersect(ParametricCurve2D,boolean)
     * doIntersect(ParametricCurve2D, boolean)}
     * ��?��?���邽�߂� SegmentAccumulator ��
     */
    private class IntersectionAccumulator extends SegmentAccumulator {
        ParametricCurve2D mate;
        CompositeCurve2D curve;
        Vector intsvec;

        IntersectionAccumulator(ParametricCurve2D mate,
                                CompositeCurve2D curve) {
            this.curve = curve;
            this.mate = mate;
        }

        void allocate(int nsegs) {
            intsvec = new Vector();
        }

        void doit(CompositeCurveSegment2D seg,
                  ParameterSection sec,
                  int compIndex) {

            IntersectionPoint2D[] ints = seg.intersect(mate);

            for (int i = 0; i < ints.length; i++) {
                double cparam = ints[i].pointOnCurve1().parameter();
                double sparam = getCompositeParam(compIndex, cparam);

                PointOnCurve2D thisPnts =
                        new PointOnCurve2D(curve, sparam, doCheckDebug);
                IntersectionPoint2D thisInts =
                        new IntersectionPoint2D(thisPnts,
                                ints[i].pointOnCurve2(), doCheckDebug);
                intsvec.addElement(thisInts);
            }
        }

        IntersectionPoint2D[] extract(boolean doExchange) {
            IntersectionPoint2D[] ints =
                    new IntersectionPoint2D[intsvec.size()];
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
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    private IntersectionPoint2D[] doIntersect(ParametricCurve2D mate,
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
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate) {
        return doIntersect(mate, false);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
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
    public CurveCurveInterference2D[] interfere(BoundedCurve2D mate) {
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
    private CurveCurveInterference2D[] getInterference(BoundedCurve2D mate,
                                                       boolean doExchange) {
        CurveCurveInterferenceList interferenceList
                = new CurveCurveInterferenceList(this, mate);

        // this �̊e�Z�O�?���g�ɑ΂���
        for (int i = 0; i < nSegments(); i++) {
            // �Z�O�?���g���x���ł̊�?𓾂�
            CurveCurveInterference2D[] localInterferences
                    = this.segmentAt(i).interfere(mate, false);

            // ��_�㊃X�g�ɒǉB���
            Vector intsList
                    = CurveCurveInterferenceList.extractIntersections
                    (localInterferences);
            for (Enumeration e = intsList.elements(); e.hasMoreElements();) {
                IntersectionPoint2D ints
                        = (IntersectionPoint2D) e.nextElement();
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
                OverlapCurve2D ovlp
                        = (OverlapCurve2D) e.nextElement();
                interferenceList.addAsOverlap
                        (ovlp.start1(),
                                this.getCompositeParam(i, ovlp.start2()),
                                ovlp.increase1(),
                                ovlp.increase2());
            }
        }

        interferenceList.removeOverlapsContainedInOtherOverlap();
        interferenceList.removeIntersectionsContainedInOverlap();

        return interferenceList.toCurveCurveInterference2DArray(doExchange);
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
     * @see #getInterference(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(BoundedLine2D mate,
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
     * @see #getInterference(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(Polyline2D mate,
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
     * @see #getInterference(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(PureBezierCurve2D mate,
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
     * @see #getInterference(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(BsplineCurve2D mate,
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
     * @see #getInterference(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(TrimmedCurve2D mate,
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
     * @see #getInterference(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(CompositeCurveSegment2D mate,
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
     * @see #getInterference(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(CompositeCurve2D mate,
                                         boolean doExchange) {
        return this.getInterference(mate, doExchange);
    }

    /**
     * {@link #offsetByCompositeCurve(ParameterSection,double,int,ToleranceForDistance)
     * offsetByCompositeCurve(ParameterSection, double, int, ToleranceForDistance)}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class OffsetByCompositeCurveAccumulator extends SegmentAccumulator {
        double magni;
        int side;
        ToleranceForDistance tol;
        int offsettedCurveIndex;
        BoundedCurve2D[] offsettedCurves;
        Point2D[] vertices;

        OffsetByCompositeCurveAccumulator(double magni,
                                          int side,
                                          ToleranceForDistance tol) {
            this.magni = magni;
            this.side = side;
            this.tol = tol;
        }

        void allocate(int nsegs) {
            offsettedCurveIndex = 0;
            offsettedCurves = new BoundedCurve2D[nsegs];
            vertices = new Point2D[nsegs];
        }

        void doit(CompositeCurveSegment2D seg,
                  ParameterSection sec,
                  int compIndex) {
            offsettedCurves[offsettedCurveIndex] =
                    seg.offsetByBoundedCurve(sec, this.magni, this.side, this.tol);
            vertices[offsettedCurveIndex] = seg.coordinates(sec.start());
            offsettedCurveIndex++;
        }

        BoundedCurve2D[] extract() {
            return offsettedCurves;
        }

        Point2D[] extract2() {
            return vertices;
        }
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ����镡?���?��?�߂�?B
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ����镡?���?�
     * @see WhichSide
     */
    public CompositeCurve2D
    offsetByCompositeCurve(ParameterSection pint,
                           double magni,
                           int side,
                           ToleranceForDistance tol) {
        boolean offsettedIsPeriodic = false;
        if (this.isPeriodic() == true) {
            if ((this.parameterDomain().section().absIncrease() - pint.absIncrease()) <
                    this.getToleranceForParameter())
                offsettedIsPeriodic = true;
        }

        OffsetByCompositeCurveAccumulator accm
                = new OffsetByCompositeCurveAccumulator(magni, side, tol);
        accm.accumulate(pint);
        BoundedCurve2D[] offsettedCurves = accm.extract();
        int nOffsettedCurves = offsettedCurves.length;
        Point2D[] vertices = accm.extract2();

        CompositeCurveSegment2D[] offsetted =
                new CompositeCurveSegment2D[2 * nOffsettedCurves];

        BoundedCurve2D prevOffsettedCurve = null;
        BoundedCurve2D crntOffsettedCurve = null;
        BoundedCurve2D firstOffsettedCurve = offsettedCurves[0];

        int transition;

        for (int i = 0; i <= nOffsettedCurves; i++) {
            /*
            * offset the curve
            */
            if (i < nOffsettedCurves) {
                crntOffsettedCurve = offsettedCurves[i];

                transition = TransitionCode.CONTINUOUS;
                if ((offsettedIsPeriodic == false) && (i == (nOffsettedCurves - 1)))
                    transition = TransitionCode.DISCONTINUOUS;

                offsetted[2 * i] =
                        new CompositeCurveSegment2D(transition, true,
                                crntOffsettedCurve);
            } else {
                crntOffsettedCurve = offsettedCurves[0];
            }

            /*
            * offset the corner
            */
            if (i == 0) {
                // start point at first offsetted segment
                ; // do nothing
            } else if ((i == nOffsettedCurves) && (offsettedIsPeriodic == false)) {
                // start point at first offsetted segment, but offsetted is open
                offsetted[2 * i - 1] = null;
            } else if (prevOffsettedCurve.endPoint().identical(crntOffsettedCurve.startPoint()) == true) {
                // end point at prev. and start point at crnt. are identical
                offsetted[2 * i - 1] = null;
            } else {
                Point2D center = (i < nOffsettedCurves) ? vertices[i] : vertices[0];
                TrimmedCurve2D offsettedCorner =
                        Circle2D.makeTrimmedCurve(center,
                                prevOffsettedCurve.endPoint(),
                                crntOffsettedCurve.startPoint());
                transition = TransitionCode.CONTINUOUS;

                offsetted[2 * i - 1] =
                        new CompositeCurveSegment2D(transition, true,
                                offsettedCorner);
            }

            prevOffsettedCurve = crntOffsettedCurve;
        }

        Vector listOfOffsetted = new Vector();
        for (int i = 0; i < (2 * nOffsettedCurves); i++)
            if (offsetted[i] != null)
                listOfOffsetted.addElement(offsetted[i]);

        offsetted = new CompositeCurveSegment2D[listOfOffsetted.size()];
        listOfOffsetted.copyInto(offsetted);

        return new CompositeCurve2D(offsetted, offsettedIsPeriodic);
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
        CompositeCurve2D cmc =
                this.offsetByCompositeCurve(pint, magni, side, tol);

        return cmc.toBsplineCurve();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ�����L��?��?�߂�?B
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ�����L��?�
     * @see WhichSide
     */
    public BoundedCurve2D
    offsetByBoundedCurve(ParameterSection pint,
                         double magni,
                         int side,
                         ToleranceForDistance tol) {
        return this.offsetByCompositeCurve(pint, magni, side, tol);
    }

    /**
     * ������?�Ȍ�?������?��ŕ�������?A���Ȍ�?����Ȃ��P?��ȕ�?��?W?��ɂ���?B
     * <p/>
     * ���̃?�\�b�h��Ă΂��?ۂ̕�����?��?A�����?��
     * {@link #offsetByCompositeCurve(ParameterSection,double,int,ToleranceForDistance)
     * offsetByCompositeCurve(ParameterSection, double, int, ToleranceForDistance)}
     * �ŃI�t�Z�b�g�������ʂł����̂Ƒz�肵�Ă���?B
     * </p>
     * <p/>
     * cmcWise �͕�����?���?�̉�����l��?A
     * ���v��� (�E���) �ɉ�BĂ��� (�ƌ��Ȃ�����) �̂ł���� LoopWise.CW?A
     * �����v��� (?����) �ɉ�BĂ��� (�ƌ��Ȃ�����) �̂ł���� LoopWise.CCW
     * ��w�肷��?B
     * </p>
     * <p/>
     * valid_side �� this �����̋�?��ǂ��瑤�ɃI�t�Z�b�g������̂ł��邩���l��?A
     * this �����̋�?��Ѥ�ɃI�t�Z�b�g������̂ł���� WhichSide.IN?A
     * �����łȂ� this �����̋�?��O���ɃI�t�Z�b�g������̂ł���� WhichSide.OUT
     * ��w�肷��
     * </p>
     *
     * @param cmcWise   ������?���?�̉�����l
     * @param validSide ���̋�?��ǂ��瑤�ɃI�t�Z�b�g������̂ł��邩���l
     * @return ���Ȍ�?����Ȃ��P?��ȕ�?��?W?�
     * @throws OpenCurveException �J������?�ł���
     * @see LoopWise
     * @see WhichSide
     */
    public CompositeCurve2D[] divideIntoSimpleLoopsIfClosed(int cmcWise,
                                                            int validSide)
            throws OpenCurveException {
        DivideCmcIntoSimpleLoops2D proc =
                new DivideCmcIntoSimpleLoops2D(this, cmcWise, validSide);
        return proc.doIt();
    }

    /**
     * {@link #doFillet(ParameterSection,int,ParametricCurve2D,ParameterSection,int,double,boolean)
     * doFillet(ParameterSection, int, ParametricCurve2D, ParameterSection, int, double, boolean)}
     * ��?��?���邽�߂� SegmentAccumulator �̎�?B
     */
    private class FilletAccumulator extends SegmentAccumulator {
        ParametricCurve2D mate;
        CompositeCurve2D curve;
        ParameterSection mateSec;
        int mateSide;
        int mySide;
        double radius;
        boolean doExchange;
        FilletObjectList fltList;

        FilletAccumulator(ParametricCurve2D mate,
                          ParameterSection mateSec,
                          int mateSide,
                          CompositeCurve2D curve,
                          int mySide,
                          double radius,
                          boolean doExchange) {
            this.curve = curve;
            this.mate = mate;
            this.mateSec = mateSec;
            this.mateSide = mateSide;
            this.mySide = mySide;
            this.radius = radius;
            this.doExchange = doExchange;
        }

        void allocate(int nsegs) {
            fltList = new FilletObjectList();
        }

        void doit(CompositeCurveSegment2D seg,
                  ParameterSection sec,
                  int compIndex) {

            FilletObject2D[] flts;
            try {
                flts = seg.fillet(sec, mySide, mate, mateSec, mateSide, radius);
            } catch (IndefiniteSolutionException e) {
                flts = new FilletObject2D[1];
                flts[0] = (FilletObject2D) e.suitable();
            }

            FilletObject2D thisFlt;

            for (int i = 0; i < flts.length; i++) {
                double cparam = flts[i].pointOnCurve1().parameter();
                double sparam = getCompositeParam(compIndex, cparam);

                PointOnCurve2D thisPnt = new PointOnCurve2D(curve, sparam, doCheckDebug);
                if (!doExchange)
                    thisFlt = new FilletObject2D(radius, flts[i].center(), thisPnt, flts[i].pointOnCurve2());
                else
                    thisFlt = new FilletObject2D(radius, flts[i].center(), flts[i].pointOnCurve2(), thisPnt);
                fltList.addFillet(thisFlt);
            }
        }

        FilletObject2D[] extract() {
            return fltList.toFilletObject2DArray(false);
        }
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A���̋�?�̎w��̋�Ԃɂ�����t�B���b�g��?�߂�?B
     * <p/>
     * �t�B���b�g����?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param pint1      ���̋�?�̃p���??[�^���
     * @param side1      ���̋�?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *                   (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param mate       ���̋�?�
     * @param pint2      ���̋�?�̃p���??[�^���
     * @param side2      ���̋�?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *                   (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param radius     �t�B���b�g���a
     * @param doExchange �t�B���b�g�� point1/2 ��귂��邩�ǂ���
     * @return �t�B���b�g�̔z��
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     */
    FilletObject2D[]
    doFillet(ParameterSection pint1, int side1, ParametricCurve2D mate,
             ParameterSection pint2, int side2, double radius,
             boolean doExchange)
            throws IndefiniteSolutionException {
        FilletAccumulator acc = new FilletAccumulator(mate, pint2, side2, this, side1, radius, doExchange);
        acc.accumulate(pint1);
        return acc.extract();
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
     * @return {@link ParametricCurve2D#COMPOSITE_CURVE_2D ParametricCurve2D.COMPOSITE_CURVE_2D}
     */
    int type() {
        return COMPOSITE_CURVE_2D;
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
        CompositeCurveSegment2D[] tSegments =
                new CompositeCurveSegment2D[this.nSegments()];
        for (int i = 0; i < this.nSegments(); i++)
            tSegments[i] = (CompositeCurveSegment2D)
                    this.segmentAt(i).transformBy(reverseTransform,
                            transformationOperator,
                            transformedGeometries);
        return new CompositeCurve2D(tSegments, this.periodic());
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
