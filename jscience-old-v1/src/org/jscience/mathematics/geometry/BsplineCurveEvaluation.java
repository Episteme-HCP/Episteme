/*
 * Bspline��?�̕]����\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: BsplineCurveEvaluation.java,v 1.3 2006/03/28 21:47:43 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;


/**
 * Bspline��?�̕]����\���N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/28 21:47:43 $
 */
class BsplineCurveEvaluation {
    /** DOCUMENT ME! */
    static boolean debug = false;

/**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�
     */
    private BsplineCurveEvaluation() {
    }

    /**
     * Bspline��?�̎�����?�߂�
     * ?���_�̂��ׂĂ̔z�񂪃Z�b�g����Ȃ�?�?������邽��
     *
     * @param controlPoints ?���_
     *
     * @return Bspline��?�̎���
     *
     * @throws FatalException DOCUMENT ME!
     */
    static int getDimension(double[][] controlPoints) {
        for (int i = 0; i < controlPoints.length; i++)
            if (controlPoints[i] != null) {
                return controlPoints[i].length;
            }

        throw new FatalException();
    }

    /**
     * ?W�l��?�߂�
     *
     * @param knotData �m�b�g?��
     * @param controlPoints ?���_
     * @param param
     *        �p���??[�^(ParameterDomain.{wrap,force}()��p����?��K������Ă��邱��)
     *
     * @return ?W�l
     */
    static double[] coordinates(BsplineKnot knotData, double[][] controlPoints,
        double param) {
        double[] d0D = new double[getDimension(controlPoints)];

        evaluation(knotData, controlPoints, param, d0D, null, null, null);

        return d0D;
    }

    /**
     * ����?���?�߂�
     *
     * @param knotData �m�b�g?��
     * @param controlPoints ?���_
     * @param param
     *        �p���??[�^(ParameterDomain.{wrap,force}()��p����?��K������Ă��邱��)
     * @param d0D Out: ��?�?�̓_
     * @param d1D Out: �ꎟ����?�
     * @param d2D Out: �񎟓���?�
     * @param d3D Out: �O������?�
     *
     * @throws FatalException DOCUMENT ME!
     */
    static void evaluation(BsplineKnot knotData, double[][] controlPoints,
        double param, double[] d0D, double[] d1D, double[] d2D, double[] d3D) {
        int dimension = getDimension(controlPoints);
        int degree = knotData.degree();
        int isckt; /* position of the given parameter in the knot vector */
        double[][] auxpnts; /* auxiliary points */
        double[][] auxv1s = null; /* auxiliary vectors */
        double[][] auxv2s = null; /* auxiliary vectors */
        double[][] auxv3s = null; /* auxiliary vectors */
        double denom;
        double scale;
        int i;
        int j;

        if ((isckt = knotData.segmentIndex(param)) < 0) {
            throw new FatalException(); // param is not 'wrap'ped nor 'force'ed.
        }

        auxpnts = new double[degree + 1][dimension];

        if ((d1D != null) || (d2D != null)) {
            auxv1s = new double[degree][dimension];
        }

        if (d2D != null) {
            if (degree > 1) {
                auxv2s = new double[degree - 1][dimension];
            }
        }

        if (d3D != null) {
            if (degree > 2) {
                auxv3s = new double[degree - 2][dimension];
            }
        }

        if (knotData.isNonPeriodic()) {
            for (j = degree; j >= 0; j--)
                for (i = 0; i < dimension; i++)
                    auxpnts[j][i] = controlPoints[isckt + j][i];
        } else {
            int k;

            for (j = degree; j >= 0; j--) {
                k = (isckt + j) % controlPoints.length;

                for (i = 0; i < dimension; i++)
                    auxpnts[j][i] = controlPoints[k][i];
            }
        }

        for (j = degree - 1; j >= 0; j--) {
            if ((d1D != null) || (d2D != null)) {
                for (i = 0; i < dimension; i++)
                    auxv1s[j][i] = auxpnts[j + 1][i] - auxpnts[j][i];

                denom = knotData.knotValueAt(degree + isckt + j + 1) -
                    knotData.knotValueAt(isckt + j + 1);

                for (i = 0; i < dimension; i++)
                    auxv1s[j][i] /= denom;

                if ((d2D != null) && (degree > 1) && (j < (degree - 1))) {
                    for (i = 0; i < dimension; i++)
                        auxv2s[j][i] = auxv1s[j + 1][i] - auxv1s[j][i];

                    denom = knotData.knotValueAt(degree + isckt + j + 1) -
                        knotData.knotValueAt(isckt + j + 2);

                    for (i = 0; i < dimension; i++)
                        auxv2s[j][i] /= denom;

                    if ((d3D != null) && (degree > 2) && (j < (degree - 2))) {
                        for (i = 0; i < dimension; i++)
                            auxv3s[j][i] = auxv2s[j + 1][i] - auxv2s[j][i];

                        denom = knotData.knotValueAt(degree + isckt + j + 1) -
                            knotData.knotValueAt(isckt + j + 3);

                        for (i = 0; i < dimension; i++)
                            auxv3s[j][i] /= denom;
                    }
                }
            }
        }

        if (d0D != null) {
            deboorProc(knotData, dimension, degree, (isckt + degree), param,
                auxpnts);

            for (i = 0; i < dimension; i++)
                d0D[i] = auxpnts[degree][i];
        }

        if (d1D != null) {
            deboorProc(knotData, dimension, (degree - 1), (isckt + degree),
                param, auxv1s);

            for (i = 0; i < dimension; i++)
                d1D[i] = auxv1s[degree - 1][i] * degree;
        }

        if (d2D != null) {
            if (degree > 1) {
                deboorProc(knotData, dimension, (degree - 2), (isckt + degree),
                    param, auxv2s);
                scale = degree * (degree - 1);

                for (i = 0; i < dimension; i++)
                    d2D[i] = auxv2s[degree - 2][i] * scale;
            } else {
                for (i = 0; i < dimension; i++)
                    d2D[i] = 0.0;
            }
        }

        if (d3D != null) {
            if (degree > 2) {
                deboorProc(knotData, dimension, (degree - 3), (isckt + degree),
                    param, auxv3s);
                scale = degree * (degree - 1) * (degree - 2);

                for (i = 0; i < dimension; i++)
                    d3D[i] = auxv3s[degree - 3][i] * scale;
            } else {
                for (i = 0; i < dimension; i++)
                    d3D[i] = 0.0;
            }
        }
    }

    /**
     * deboor
     *
     * @param knotData DOCUMENT ME!
     * @param dimension DOCUMENT ME!
     * @param degree DOCUMENT ME!
     * @param sct DOCUMENT ME!
     * @param param DOCUMENT ME!
     * @param avs DOCUMENT ME!
     */
    private static void deboorProc(BsplineKnot knotData, int dimension,
        int degree, int sct, double param, double[][] avs) {
        int i;
        int j;
        int k;
        int l;
        double kf;
        double kb;
        double t1;
        double t2;

        for (j = 1; j <= degree; j++)
            for (k = degree, i = sct; k >= j; k--, i--) {
                kf = knotData.knotValueAt((i + degree) - j + 1);
                kb = knotData.knotValueAt(i);

                t1 = (kf - param) / (kf - kb);
                t2 = 1.0 - t1;

                for (l = 0; l < dimension; l++)
                    avs[k][l] = (t1 * avs[k - 1][l]) + (t2 * avs[k][l]);
            }
    }

    /**
     * deboor of blossoming
     *
     * @param knotData DOCUMENT ME!
     * @param dimension DOCUMENT ME!
     * @param degree DOCUMENT ME!
     * @param sct DOCUMENT ME!
     * @param param DOCUMENT ME!
     * @param avs DOCUMENT ME!
     */
    private static void blossomDeboorProc(BsplineKnot knotData, int dimension,
        int degree, int sct, double[] param, double[][] avs) {
        int i;
        int j;
        int k;
        int l;
        double kf;
        double kb;
        double t1;
        double t2;

        for (j = 1; j <= degree; j++)
            for (k = degree, i = sct; k >= j; k--, i--) {
                kf = knotData.knotValueAt((i + degree) - j + 1);
                kb = knotData.knotValueAt(i);

                t1 = (kf - param[j - 1]) / (kf - kb);
                t2 = 1.0 - t1;

                for (l = 0; l < dimension; l++)
                    avs[k][l] = (t1 * avs[k - 1][l]) + (t2 * avs[k][l]);
            }
    }

    /**
     * �u�?�b�T�~���O
     *
     * @param knotData �m�b�g?��
     * @param controlPoints ?���_�̔z��
     * @param segNumber
     *        ���Z��?ۂƂȂ�Z�O�?���g�̔�?�
     *        (?擪�� 0)
     * @param param �p���??[�^�̔z��
     *        (�v�f?�?F��?�̎�?�)
     *
     * @return �u�?�b�T�~���O�̌���
     */
    static double[] blossoming(BsplineKnot knotData, double[][] controlPoints,
        int segNumber, double[] param) {
        int dimension = getDimension(controlPoints);
        int degree = knotData.degree();
        double[][] auxpnts = new double[degree + 1][dimension];

        if (knotData.isNonPeriodic()) {
            for (int j = degree; j >= 0; j--) {
                int k = segNumber + j;

                for (int i = 0; i < dimension; i++)
                    auxpnts[j][i] = controlPoints[k][i];
            }
        } else {
            for (int j = degree; j >= 0; j--) {
                int k = (segNumber + j) % controlPoints.length;

                for (int i = 0; i < dimension; i++)
                    auxpnts[j][i] = controlPoints[k][i];
            }
        }

        blossomDeboorProc(knotData, dimension, degree, (segNumber + degree),
            param, auxpnts);

        double[] result = new double[dimension];

        for (int i = 0; i < dimension; i++)
            result[i] = auxpnts[degree][i];

        return result;
    }

    /**
     * ��?�?グ��?��?��?A?V���ȃm�b�g���?�߂�
     *
     * @param knotData DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static BsplineKnot getNewKnotDataAtDegreeElevation(BsplineKnot knotData) {
        int oldNKnots = knotData.nKnotValues();
        BsplineKnot.ValidSegmentInfo oldValidSegments = knotData.validSegments();
        int oldNValidSegments = oldValidSegments.nSegments();

        int newNControlPoints = knotData.nControlPoints() + oldNValidSegments;
        int newNUniqueKnots = (knotData.knotSpec() == KnotType.UNIFORM_KNOTS)
            ? ((2 * oldNKnots) - (2 * knotData.degree())) : knotData.nKnots();

        /*
         * �^����ꂽ�m�b�g��̊e�Ք�m�b�g��?d���x���?�₷
         */
        double[] newKnots;
        int[] newKnotMultiplicities;
        int i;
        int j;

        if (knotData.knotSpec() == KnotType.UNIFORM_KNOTS) {
            // ���̃m�b�g�񂪃��j�t�H?[����?�?�
            newKnots = new double[newNUniqueKnots];
            newKnotMultiplicities = new int[newNUniqueKnots];

            int lowerOfInternal = knotData.degree();
            int upperOfInternal = knotData.degree() + knotData.nSegments();

            for (i = j = 0; i < oldNKnots; i++) {
                newKnots[j] = knotData.knotValueAt(i);
                newKnotMultiplicities[j] = 1;
                j++;

                if ((lowerOfInternal <= i) && (i <= upperOfInternal)) {
                    newKnots[j] = newKnots[j - 1];
                    newKnotMultiplicities[j] = 1;
                    j++;
                }
            }
        } else {
            // ���̃m�b�g�񂪃��j�t�H?[���łȂ�?�?�
            newKnots = knotData.knots();
            newKnotMultiplicities = knotData.knotMultiplicities();
            newNUniqueKnots = BsplineKnot.beautify(newNUniqueKnots, newKnots,
                    newKnotMultiplicities);

            double lowerKnotOfOldValidSegments = oldValidSegments.headKnotPoint(0);
            double upperKnotOfOldValidSegments = oldValidSegments.tailKnotPoint(oldNValidSegments -
                    1);

            for (i = 0; i < newNUniqueKnots; i++) {
                if ((!(lowerKnotOfOldValidSegments > newKnots[i])) &&
                        (!(newKnots[i] > upperKnotOfOldValidSegments))) {
                    newKnotMultiplicities[i] += 1;
                }
            }
        }

        BsplineKnot newKnotData;

        if (knotData.isNonPeriodic() == true) {
            // �J�����`��
            newNUniqueKnots = BsplineKnot.beautify(newNUniqueKnots, newKnots,
                    newKnotMultiplicities);
            newKnotData = new BsplineKnot(knotData.degree() + 1,
                    KnotType.UNSPECIFIED, knotData.isPeriodic(),
                    newNUniqueKnots, newKnotMultiplicities, newKnots,
                    newNControlPoints);
        } else {
            // �����`��
            newKnotData = new BsplineKnot(knotData.degree() + 1,
                    KnotType.UNSPECIFIED, knotData.isPeriodic(),
                    newNUniqueKnots, newKnotMultiplicities, newKnots,
                    newNControlPoints, false);
            newKnotData = newKnotData.makeKnotsClosed();
        }

        return newKnotData;
    }

    /**
     * ��?�?グ��?��?��?A?V����?���_��?�߂�?ۂ̃u�?�b�T�~���O�̑�?ۂƂȂ�
     * �Z�O�?���g�̔�?��𓾂�
     *
     * @param targetParams DOCUMENT ME!
     * @param oldDegree DOCUMENT ME!
     * @param oldValidSegments DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int getTargetSegmentOfBlossomingAtDegreeElevation(
        double[] targetParams, int oldDegree,
        BsplineKnot.ValidSegmentInfo oldValidSegments) {
        int oldNValidSegments = oldValidSegments.nSegments();

        int lowerSegmentIndex = -1;

        for (int k = 0; k < oldNValidSegments; k++) {
            if (!(targetParams[0] > oldValidSegments.tailKnotPoint(k))) {
                lowerSegmentIndex = k;

                break;
            }
        }

        int upperSegmentIndex = oldNValidSegments;

        for (int k = (oldNValidSegments - 1); k >= 0; k--) {
            if (!(oldValidSegments.headKnotPoint(k) > targetParams[oldDegree])) {
                upperSegmentIndex = k;

                break;
            }
        }

        int targetSegment;

        if (lowerSegmentIndex == (-1)) {
            targetSegment = oldValidSegments.segmentNumber(upperSegmentIndex);
        } else if (upperSegmentIndex == oldNValidSegments) {
            targetSegment = oldValidSegments.segmentNumber(lowerSegmentIndex);
        } else {
            if (targetParams[0] < oldValidSegments.headKnotPoint(
                        lowerSegmentIndex)) {
                targetSegment = oldValidSegments.segmentNumber(lowerSegmentIndex);
            } else if (targetParams[oldDegree] > oldValidSegments.tailKnotPoint(
                        upperSegmentIndex)) {
                targetSegment = oldValidSegments.segmentNumber(upperSegmentIndex);
            } else {
                int targetSegmentIndex = (lowerSegmentIndex +
                    upperSegmentIndex) / 2;
                targetSegment = oldValidSegments.segmentNumber(targetSegmentIndex);

                if (targetSegmentIndex != (oldNValidSegments - 1)) {
                    targetSegmentIndex++;

                    double diff1 = Math.abs(targetParams[0] -
                            oldValidSegments.headKnotPoint(targetSegmentIndex));
                    double diff2 = Math.abs(targetParams[oldDegree] -
                            oldValidSegments.tailKnotPoint(targetSegmentIndex));
                    double pTol = ConditionOfOperation.getCondition()
                                                      .getToleranceForParameter();

                    if ((diff1 < pTol) && (diff2 < pTol)) {
                        targetSegment = oldValidSegments.segmentNumber(targetSegmentIndex);
                    }
                }
            }
        }

        return targetSegment;
    }

    /**
     * ��?�?グ��?��?��?A?V����?���_���?�߂�
     *
     * @param oldKnotData DOCUMENT ME!
     * @param newKnotData DOCUMENT ME!
     * @param oldControlPoints DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static double[][] getNewControlPointsAtDegreeElevation(
        BsplineKnot oldKnotData, BsplineKnot newKnotData,
        double[][] oldControlPoints) {
        int i;
        int j;
        int k;
        int l;

        BsplineKnot.ValidSegmentInfo oldValidSegments = oldKnotData.validSegments();
        int oldNValidSegments = oldValidSegments.nSegments();
        int newNControlPoints = oldKnotData.nControlPoints() +
            oldNValidSegments;

        /*
         * ?V����?���_���?�߂�
         */
        double[] targetParams = new double[oldKnotData.degree() + 1];
        double[] blossomParams = new double[oldKnotData.degree()];

        int dimension = oldControlPoints[0].length;
        double[][] newControlPoints = new double[newNControlPoints][dimension];

        // ?V����?���_�̊e?X�ɂ���
        for (i = 0; i < newNControlPoints; i++) {
            // �u�?�b�T�~���O�̑�?ۂƂȂ�Z�O�?���g�𓾂�
            for (k = 0, l = (i + 1); k < newKnotData.degree(); k++, l++)
                targetParams[k] = newKnotData.knotValueAt(l);

            int targetSegment = BsplineCurveEvaluation.getTargetSegmentOfBlossomingAtDegreeElevation(targetParams,
                    oldKnotData.degree(), oldValidSegments);

            // ��Ƃ̋�?�Ƀu�?�b�T�~���O��{����?A?V����?���_��?W�l�𓾂�
            for (k = 0; k < dimension; k++)
                newControlPoints[i][k] = 0.0;

            for (j = 0;
                    j < newKnotData.degree() /* j <= (oldKnotData.degree() + 1) */;
                    j++) {
                for (k = 0, l = 0; k < oldKnotData.degree(); k++, l++) {
                    if (k == j) {
                        l++;
                    }

                    blossomParams[k] = targetParams[l];
                }

                double[] blossomPoint = BsplineCurveEvaluation.blossoming(oldKnotData,
                        oldControlPoints, targetSegment, blossomParams);

                for (k = 0; k < dimension; k++)
                    newControlPoints[i][k] += (blossomPoint[k] / newKnotData.degree());
            }
        }

        return newControlPoints;
    }

    /**
     * Bezier��?�Q�ɕ��ⷂ�
     *
     * @param knotData �m�b�g?��
     * @param controlPoints ?���_
     *
     * @return Bezier��?�Q
     */
    static double[][][] toBezierCurve(BsplineKnot knotData,
        double[][] controlPoints) {
        /*
         * At first, increase knot multiplicity of given Bspline curve
         * until each of knot points has C0 (dis)continuity.
         * Then each of regular segments has Bezier control points.
         */
        BsplineKnot knotData0;
        int uik;
        double[] knots;
        int[] knot_multi;
        int degree;
        int dimension = getDimension(controlPoints);
        int uicp;
        double[][] cntrl_pnts;
        double[][][] bzc;
        int s;
        int num;
        int d_num;
        int n_seg;
        double skn;
        double ekn;
        double p_tol = ConditionOfOperation.getCondition()
                                           .getToleranceForParameter();
        int i;
        int j;
        int k;
        int l;

        /*
         * prepare working knot vector
         */
        knotData0 = knotData.beautify().makeExplicit();
        degree = knotData.degree();
        knots = knotData0.knots();
        knot_multi = knotData0.knotMultiplicities();
        uicp = knotData.nControlPoints();

        if (knotData.isPeriodic()) {
            uicp += degree;
        }

        knotData0 = new BsplineKnot(degree, KnotType.UNSPECIFIED, false, // always open
                knot_multi, knots, uicp);

        /*
         * allocate working area for Bspline control points,
         * and copy original points to there.
         */
        uik = knotData0.nKnots();
        cntrl_pnts = new double[uik * (degree + 1)][dimension]; // big enough

        uicp = knotData.nControlPoints();

        for (i = 0; i < uicp; i++)
            for (j = 0; j < dimension; j++)
                cntrl_pnts[i][j] = controlPoints[i][j];

        if (knotData.isPeriodic()) {
            uicp += degree;

            for (j = 0; j < degree; j++, i++)
                for (k = 0; k < dimension; k++)
                    cntrl_pnts[i][k] = controlPoints[j][k];
        }

        /*
         * increase multiplicity at each knot points
         */
        d_num = sum_internal_multi(knotData0);
        n_seg = knotData0.nSegments();
        skn = knotData0.knotValueAt(degree);
        ekn = knotData0.knotValueAt(degree + n_seg);

        for (i = 0; i < uik; i++) {
            if ((knots[i] < skn) || (ekn < knots[i])) {
                continue;
            }

            while (knot_multi[i] < degree) {
                uicp = exch_bsc(knotData0, cntrl_pnts, dimension, knots[i]);
                knot_multi[i]++;
                knotData0 = new BsplineKnot(degree, KnotType.UNSPECIFIED,
                        false, knot_multi, knots, uicp);
            }
        }

        s = 0;

        while (knotData0.knotValueAt(s + degree + 1) < (skn + p_tol))
            s++;

        num = n_seg - d_num;

        bzc = new double[num][degree + 1][dimension];

        for (i = 0; i < num; i++) {
            for (j = 0; j <= degree; j++) {
                k = s + (i * degree) + j;

                for (l = 0; l < dimension; l++)
                    bzc[i][j][l] = cntrl_pnts[k][l];
            }
        }

        return bzc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param knotData DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int sum_internal_multi(BsplineKnot knotData) {
        int d_num;
        int s_knot_no;
        int e_knot_no;
        int i;
        int j;
        int k;

        d_num = 0;
        j = k = 0;
        s_knot_no = knotData.degree() + 1;
        e_knot_no = knotData.degree() + knotData.nSegments() + 1;

        for (i = 0; i < knotData.nKnots(); i++) {
            j += knotData.knotMultiplicityAt(i);

            if ((j < s_knot_no) || (e_knot_no <= k)) {
                ;
            } else if ((k < s_knot_no) && (s_knot_no <= j)) {
                d_num += ((knotData.knotMultiplicityAt(i) -
                (s_knot_no - 1 - k)) - 1);
            } else if ((k < e_knot_no) && (e_knot_no <= j)) {
                d_num += ((knotData.knotMultiplicityAt(i) - (j - e_knot_no)) -
                1);
            } else {
                d_num += (knotData.knotMultiplicityAt(i) - 1);
            }

            k = j;
        }

        return d_num;
    }

    /**
     * DOCUMENT ME!
     *
     * @param knotData DOCUMENT ME!
     * @param cntrl_pnts DOCUMENT ME!
     * @param dimension DOCUMENT ME!
     * @param para DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int exch_bsc(BsplineKnot knotData, double[][] cntrl_pnts,
        int dimension, double para) {
        int uicp;
        int degree;
        double alp;
        double[] p0;
        double[] p1;
        int i;
        int j;
        int k;

        uicp = knotData.nControlPoints();
        degree = knotData.degree();

        i = knotData.segmentIndex(para);
        p0 = new double[dimension];
        p1 = new double[dimension];

        for (j = (i + 1); j <= (i + degree); j++) {
            alp = (para - knotData.knotValueAt(j)) / (knotData.knotValueAt(j +
                    degree) - knotData.knotValueAt(j));

            for (k = 0; k < dimension; k++)
                p1[k] = ((1.0 - alp) * cntrl_pnts[j - 1][k]) +
                    (alp * cntrl_pnts[j][k]);

            if (j > (i + 1)) {
                for (k = 0; k < dimension; k++)
                    cntrl_pnts[j - 1][k] = p0[k];
            }

            for (k = 0; k < dimension; k++)
                p0[k] = p1[k];
        }

        for (j = (i + degree); j < uicp; j++) {
            for (k = 0; k < dimension; k++) {
                p1[k] = cntrl_pnts[j][k];
                cntrl_pnts[j][k] = p0[k];
                p0[k] = p1[k];
            }
        }

        for (k = 0; k < dimension; k++)
            cntrl_pnts[uicp][k] = p0[k];

        return uicp + 1;
    }

    /**
     * Bspline�Ȗʂ�Bezier�ȖʖԂɕ��ⷂ�
     *
     * @param uKnotData U���̃m�b�g?��
     * @param vKnotData DOCUMENT ME!
     * @param controlPoints ?���_
     *
     * @return Bezier�Ȗʖ�
     */
    static double[][][][][] toBezierSurface(BsplineKnot uKnotData,
        BsplineKnot vKnotData, double[][][] controlPoints) {
        int dimension = controlPoints[0][0].length;
        double p_tol = ConditionOfOperation.getCondition()
                                           .getToleranceForParameter();

        /*
         * prepare working knot vector
         */
        BsplineKnot uKnotData0 = uKnotData.beautify().makeExplicit();
        int uDegree = uKnotData.degree();
        double[] uKnots = uKnotData0.knots();
        int[] uKnotMulti = uKnotData0.knotMultiplicities();
        int uUicp = uKnotData.nControlPoints();

        if (uKnotData.isPeriodic()) {
            uUicp += uDegree;
        }

        uKnotData0 = new BsplineKnot(uDegree, KnotType.UNSPECIFIED, false, // always open
                uKnotMulti, uKnots, uUicp);

        BsplineKnot vKnotData0 = vKnotData.beautify().makeExplicit();
        int vDegree = vKnotData.degree();
        double[] vKnots = vKnotData0.knots();
        int[] vKnotMulti = vKnotData0.knotMultiplicities();
        int vUicp = vKnotData.nControlPoints();

        if (vKnotData.isPeriodic()) {
            vUicp += vDegree;
        }

        vKnotData0 = new BsplineKnot(vDegree, KnotType.UNSPECIFIED, false, // always open
                vKnotMulti, vKnots, vUicp);

        /*
         * allocate working area (big enough) for Bspline control points,
         * and copy original points to there.
         */
        int uicp0 = (uKnotData.nControlPoints() + uKnotData.degree()) * (uKnotData.degree() +
            1);
        int uicp1 = (vKnotData.nControlPoints() + vKnotData.degree()) * (vKnotData.degree() +
            1);
        double[][][] cntrl_pnts = new double[uicp0][uicp1][dimension];

        int uj;
        int vj;
        int uk;
        int vk;
        int j;

        for (uj = 0; uj < uKnotData.nControlPoints(); uj++) {
            for (vj = 0; vj < vKnotData.nControlPoints(); vj++) {
                for (j = 0; j < dimension; j++)
                    cntrl_pnts[uj][vj][j] = controlPoints[uj][vj][j];
            }

            if (vKnotData.isPeriodic()) {
                for (vk = 0; vk < vKnotData.degree(); vk++, vj++)
                    for (j = 0; j < dimension; j++)
                        cntrl_pnts[uj][vj][j] = controlPoints[uj][vk][j];
            }
        }

        if (uKnotData.isPeriodic()) {
            for (uk = 0; uk < uKnotData.degree(); uk++, uj++) {
                for (vj = 0; vj < vKnotData.nControlPoints(); vj++)
                    for (j = 0; j < dimension; j++)
                        cntrl_pnts[uj][vj][j] = controlPoints[uk][vj][j];

                if (vKnotData.isPeriodic()) {
                    for (vk = 0; vk < vKnotData.degree(); vk++, vj++)
                        for (j = 0; j < dimension; j++)
                            cntrl_pnts[uj][vj][j] = controlPoints[uk][vk][j];
                }
            }
        }

        /*
         * increase multiplicity at each knot points
         */
        int u_dno = sum_internal_multi(uKnotData0);
        int un_seg = uKnotData0.nSegments();
        double u_skn = uKnotData0.knotValueAt(uDegree);
        double u_ekn = uKnotData0.knotValueAt(uDegree + un_seg);
        int u_uik = uKnotData0.nKnots();

        for (int ui = 0; ui < u_uik; ui++) {
            if ((uKnots[ui] < u_skn) || (u_ekn < uKnots[ui])) {
                continue;
            }

            while (uKnotMulti[ui] < uDegree) {
                uUicp = exch_bss_u(uKnotData0, vKnotData0, cntrl_pnts,
                        dimension, uKnots[ui]);
                uKnotMulti[ui]++;
                uKnotData0 = new BsplineKnot(uDegree, KnotType.UNSPECIFIED,
                        false, uKnotMulti, uKnots, uUicp);
            }
        }

        int v_dno = sum_internal_multi(vKnotData0);
        int vn_seg = vKnotData0.nSegments();
        double v_skn = vKnotData0.knotValueAt(vDegree);
        double v_ekn = vKnotData0.knotValueAt(vDegree + vn_seg);
        int v_uik = vKnotData0.nKnots();

        for (int vi = 0; vi < v_uik; vi++) {
            if ((vKnots[vi] < v_skn) || (v_ekn < vKnots[vi])) {
                continue;
            }

            while (vKnotMulti[vi] < vDegree) {
                vUicp = exch_bss_v(uKnotData0, vKnotData0, cntrl_pnts,
                        dimension, vKnots[vi]);
                vKnotMulti[vi]++;
                vKnotData0 = new BsplineKnot(vDegree, KnotType.UNSPECIFIED,
                        false, vKnotMulti, vKnots, vUicp);
            }
        }

        int us = 0;

        while (uKnotData0.knotValueAt(us + uDegree + 1) < (u_skn + p_tol))
            us++;

        int u_no = un_seg - u_dno;

        int vs = 0;

        while (vKnotData0.knotValueAt(vs + vDegree + 1) < (v_skn + p_tol))
            vs++;

        int v_no = vn_seg - v_dno;

        double[][][][][] bzs = new double[u_no][v_no][uDegree + 1][vDegree + 1][dimension];

        for (int ui = 0; ui < u_no; ui++)
            for (int vi = 0; vi < v_no; vi++)
                for (uj = 0; uj <= uDegree; uj++)
                    for (vj = 0; vj <= vDegree; vj++)
                        bzs[ui][vi][uj][vj] = cntrl_pnts[us + (ui * uDegree) +
                            uj][vs + (vi * vDegree) + vj];

        return bzs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param uKnotData DOCUMENT ME!
     * @param vKnotData DOCUMENT ME!
     * @param cntrl_pnts DOCUMENT ME!
     * @param dimension DOCUMENT ME!
     * @param para DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int exch_bss_u(BsplineKnot uKnotData, BsplineKnot vKnotData,
        double[][][] cntrl_pnts, int dimension, double para) {
        double alp;
        int uj;
        int vj;
        int i;
        int k;

        int uUicp = uKnotData.nControlPoints();
        int vUicp = vKnotData.nControlPoints();
        int uDegree = uKnotData.degree();

        i = uKnotData.segmentIndex(para);

        double[] p0 = new double[dimension];
        double[] p1 = new double[dimension];

        for (vj = 0; vj < vUicp; vj++) {
            for (uj = (i + 1); uj <= (i + uDegree); uj++) {
                alp = (para - uKnotData.knotValueAt(uj)) / (uKnotData.knotValueAt(uj +
                        uDegree) - uKnotData.knotValueAt(uj));

                for (k = 0; k < dimension; k++)
                    p1[k] = ((1.0 - alp) * cntrl_pnts[uj - 1][vj][k]) +
                        (alp * cntrl_pnts[uj][vj][k]);

                if (uj > (i + 1)) {
                    for (k = 0; k < dimension; k++)
                        cntrl_pnts[uj - 1][vj][k] = p0[k];
                }

                for (k = 0; k < dimension; k++)
                    p0[k] = p1[k];
            }

            for (uj = (i + uDegree); uj < uUicp; uj++) {
                for (k = 0; k < dimension; k++) {
                    p1[k] = cntrl_pnts[uj][vj][k];
                    cntrl_pnts[uj][vj][k] = p0[k];
                    p0[k] = p1[k];
                }
            }

            for (k = 0; k < dimension; k++)
                cntrl_pnts[uUicp][vj][k] = p0[k];
        }

        return uUicp + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param uKnotData DOCUMENT ME!
     * @param vKnotData DOCUMENT ME!
     * @param cntrl_pnts DOCUMENT ME!
     * @param dimension DOCUMENT ME!
     * @param para DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int exch_bss_v(BsplineKnot uKnotData, BsplineKnot vKnotData,
        double[][][] cntrl_pnts, int dimension, double para) {
        double alp;
        int uj;
        int vj;
        int i;
        int k;

        int uUicp = uKnotData.nControlPoints();
        int vUicp = vKnotData.nControlPoints();
        int vDegree = vKnotData.degree();

        i = vKnotData.segmentIndex(para);

        double[] p0 = new double[dimension];
        double[] p1 = new double[dimension];

        for (uj = 0; uj < uUicp; uj++) {
            for (vj = (i + 1); vj <= (i + vDegree); vj++) {
                alp = (para - vKnotData.knotValueAt(vj)) / (vKnotData.knotValueAt(vj +
                        vDegree) - vKnotData.knotValueAt(vj));

                for (k = 0; k < dimension; k++)
                    p1[k] = ((1.0 - alp) * cntrl_pnts[uj][vj - 1][k]) +
                        (alp * cntrl_pnts[uj][vj][k]);

                if (vj > (i + 1)) {
                    for (k = 0; k < dimension; k++)
                        cntrl_pnts[uj][vj - 1][k] = p0[k];
                }

                for (k = 0; k < dimension; k++)
                    p0[k] = p1[k];
            }

            for (vj = (i + vDegree); vj < vUicp; vj++) {
                for (k = 0; k < dimension; k++) {
                    p1[k] = cntrl_pnts[uj][vj][k];
                    cntrl_pnts[uj][vj][k] = p0[k];
                    p0[k] = p1[k];
                }
            }

            for (k = 0; k < dimension; k++)
                cntrl_pnts[uj][vUicp][k] = p0[k];
        }

        return vUicp + 1;
    }

    /**
     * �^����ꂽ�p���??[�^�ŕ�������
     *
     * @param knotData �m�b�g?��
     * @param controlPoints ?���_
     * @param para ��������p���??[�^
     * @param newKnotData �������ꂽBspline��?�̃m�b�g
     * @param newControlPoints
     *        �������ꂽBspline��?��?���_
     */
    static void divide(BsplineKnot knotData, double[][] controlPoints,
        double para, BsplineKnot[] newKnotData, double[][][] newControlPoints) {
        int dimension = controlPoints[0].length;
        int degree = knotData.degree();
        int ins_seg = knotData.segmentIndex(para);
        int[] new_n;
        int new_uicp;
        int new_uik;
        double[] new_knots;
        double[] knots;
        int[] new_knot_multi;
        int[] knot_multi;
        double[][] new_cntrl_pnts;
        int[] nk_info = new int[2];
        int n_ins;
        double[] t;
        BsplineKnot kdn;
        double p_tol = ConditionOfOperation.getCondition()
                                           .getToleranceForParameter();
        int j;
        int k;
        int l;

        /*
         * revise the knot vector
         */
        new_n = get_new_ui_c(knotData, degree);
        new_uicp = new_n[0];
        new_uik = new_n[1];

        if (knotData.isPeriodic()) {
            new_uicp += degree;
        }

        new_knots = new double[new_uik];
        new_knot_multi = new int[new_uik];
        new_cntrl_pnts = new double[new_uicp][dimension];
        new_uik--;

        new_uik = fill_new_knot(knotData, para, new_uik, new_knots,
                new_knot_multi, nk_info, p_tol);

        if (debug) {
            System.out.println("new_uik : " + new_uik);
            System.out.println("new_uicp : " + new_uicp);
            System.out.println("nk_info : [" + nk_info[0] + "],[" + nk_info[1] +
                "]");

            for (int i = 0; i < new_uik; i++)
                System.out.println("new_knots[" + i + "] : " + new_knots[i]);

            for (int i = 0; i < new_uik; i++)
                System.out.println("new_knot_multi[" + i + "] : " +
                    new_knot_multi[i]);
        }

        if (knotData.isNonPeriodic()) {
            /*
             * open curve
             */
            int front_uicp;

            /*
             * open curve
             */
            int rear_uicp;
            int front_uik;
            int rear_uik;

            /*
             * revise the control points
             */
            if ((n_ins = degree - nk_info[1]) < 0) {
                n_ins = 0;
            } else if (nk_info[1] != 0) {
                new_uicp -= nk_info[1];
            }

            j = 0;
            k = ins_seg + degree + 1;

            for (; j < k; j++)
                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[j][l] = controlPoints[j][l];

            j = ins_seg + degree + 1;
            k = ins_seg + degree + n_ins;

            for (; j < k; j++)
                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[j][l] = controlPoints[ins_seg + degree][l];

            j = ins_seg + degree + n_ins;
            k = new_uicp;

            for (; j < k; j++)
                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[j][l] = controlPoints[j - n_ins][l];

            kdn = knotData; /* In the beginning, original knot vector is necessary. */

            for (; n_ins > 0; ins_seg++, n_ins--) {
                for (j = (ins_seg + degree); j >= (ins_seg + 1); j--) {
                    t = comp_iratio(kdn, j, para, p_tol);
                    comp_r2p(dimension, new_cntrl_pnts, j, t[0],
                        new_cntrl_pnts, (j - 1), t[1], new_cntrl_pnts, j);
                }

                new_knot_multi[nk_info[0]]++;

                kdn = make_open_knot(degree, new_knot_multi, new_knots,
                        new_uik, -1);
            }

            /*
             * divide (bsc0) into two
             */
            front_uik = nk_info[0] + 1;
            front_uicp = get_front_uicp(nk_info, new_knot_multi);

            newControlPoints[0] = new double[front_uicp][dimension];
            knots = new double[front_uik];
            knot_multi = new int[front_uik];

            copy_front_kinfo(knots, knot_multi, new_knots, new_knot_multi,
                front_uik, degree);
            newKnotData[0] = make_open_knot(degree, knot_multi, knots,
                    front_uik, front_uicp);

            for (j = 0; j < front_uicp; j++)
                for (l = 0; l < dimension; l++)
                    newControlPoints[0][j][l] = new_cntrl_pnts[j][l];

            rear_uik = new_uik - nk_info[0];
            rear_uicp = get_rear_uicp(nk_info, new_knot_multi, new_uik);

            newControlPoints[1] = new double[rear_uicp][dimension];
            knots = new double[rear_uik];
            knot_multi = new int[rear_uik];

            copy_rear_kinfo(knots, knot_multi, new_knots, new_knot_multi,
                front_uik, rear_uik, degree);
            newKnotData[1] = make_open_knot(degree, knot_multi, knots,
                    rear_uik, rear_uicp);

            for (j = 0, k = (front_uicp - 1); j < rear_uicp; j++)
                for (l = 0; l < dimension; l++)
                    newControlPoints[1][j][l] = new_cntrl_pnts[k + j][l];
        } else {
            /*
             * closed curve
             */
            int uicp = knotData.nControlPoints();
            int k_0;
            int n_kel;
            int iksct;
            int n_seg;
            double lower_limit;
            double upper_limit;
            int lower_idx;
            int upper_idx;
            int bsc1_uicp;
            int bsc1_uik;

            /*
             * revise the control points
             */
            if ((n_ins = degree - nk_info[1]) < 0) {
                n_ins = 0;
            } else if (nk_info[1] != 0) {
                new_uicp -= nk_info[1];
            }

            j = 0;
            k = ins_seg + degree + 1;

            for (; j < k; j++) {
                k_0 = j % uicp;

                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[j][l] = controlPoints[k_0][l];
            }

            j = ins_seg + degree + 1;
            k = ins_seg + degree + n_ins;

            for (; j < k; j++) {
                k_0 = (ins_seg + degree) % uicp;

                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[j][l] = controlPoints[k_0][l];
            }

            j = ins_seg + degree + n_ins;
            k = new_uicp;

            for (; j < k; j++) {
                k_0 = (j - n_ins) % uicp;

                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[j][l] = controlPoints[k_0][l];
            }

            /* uicp, uik and knot_multi[] are inconsistent here */
            kdn = knotData; /* In the beginning, original knot vector is necessary. */

            for (; n_ins > 0; ins_seg++, n_ins--) {
                for (j = (ins_seg + degree); j >= (ins_seg + 1); j--) {
                    t = comp_iratio(kdn, j, para, p_tol);
                    comp_r2p(dimension, new_cntrl_pnts, j, t[0],
                        new_cntrl_pnts, (j - 1), t[1], new_cntrl_pnts, j);

                    if (j < degree) {
                        for (l = 0; l < dimension; l++)
                            new_cntrl_pnts[new_uicp - degree + j][l] = new_cntrl_pnts[j][l];
                    } else if (j > (new_uicp - degree - 1)) {
                        for (l = 0; l < dimension; l++)
                            new_cntrl_pnts[j - (new_uicp - degree)][l] = new_cntrl_pnts[j][l];
                    }
                }

                /* nk_info[0] should be revised,
                   because following treat_closed_bsp()
                   maybe change knot vector. */
                nk_info[0] = get_knot_idx(new_uik, new_knots, para, p_tol);
                new_knot_multi[nk_info[0]]++;

                kdn = make_open_knot(degree, new_knot_multi, new_knots,
                        new_uik, -1);
                n_kel = kdn.nKnotValues();

                //n_kel = kdn.nKnotValues() - (n_ins - 1);
                kdn = treat_closed_bsp(kdn, n_kel, para, p_tol);

                new_knot_multi = kdn.knotMultiplicities();
                new_knots = kdn.knots();
                new_uik = kdn.nKnots();
            }

            kdn = make_open_knot(degree, new_knot_multi, new_knots, new_uik, -1);

            n_seg = kdn.nSegments();
            lower_limit = kdn.knotValueAt(degree);
            upper_limit = kdn.knotValueAt(degree + n_seg);

            lower_idx = get_knot_idx(new_uik, new_knots, lower_limit, p_tol);
            upper_idx = get_knot_idx(new_uik, new_knots, upper_limit, p_tol);
            bsc1_uik = upper_idx - lower_idx + 1;

            n_kel = 0;

            for (j = lower_idx; j < upper_idx; j++)
                n_kel += new_knot_multi[j];

            bsc1_uicp = (n_kel += (degree + 2)) - degree - 1;

            if (Math.abs(upper_limit - para) < p_tol) {
                para = lower_limit;
            }

            nk_info[0] = get_knot_idx(new_uik, new_knots, para, p_tol);
            iksct = kdn.segmentIndex(para);

            newControlPoints[0] = new double[bsc1_uicp][dimension];
            knots = new double[bsc1_uik];
            knot_multi = new int[bsc1_uik];

            knots[0] = 0.0;
            knot_multi[0] = new_knot_multi[nk_info[0]] + 1;

            for (j = 1, k = nk_info[0] + 1; j < (bsc1_uik - 1); j++, k++) {
                knots[j] = knots[j - 1] + (new_knots[k] - new_knots[k - 1]);
                knot_multi[j] = new_knot_multi[k];

                if (k == upper_idx) {
                    k = lower_idx;
                }
            }

            knots[j] = knots[j - 1] +
                (new_knots[nk_info[0]] - new_knots[nk_info[0] - 1]);
            knot_multi[j] = new_knot_multi[nk_info[0]] + 1;
            newKnotData[0] = make_open_knot(degree, knot_multi, knots,
                    bsc1_uik, bsc1_uicp);

            for (j = 0, k = iksct; j < bsc1_uicp; j++, k++) {
                for (l = 0; l < dimension; l++)
                    newControlPoints[0][j][l] = new_cntrl_pnts[k][l];

                if (k == (new_uicp - 1)) {
                    k = degree - 1;
                }
            }

            newControlPoints[1] = null;
            newKnotData[1] = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param knotData DOCUMENT ME!
     * @param controlPoints DOCUMENT ME!
     * @param para DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static Object[] insertKnot(BsplineKnot knotData, double[][] controlPoints,
        double para) {
        int dimension = controlPoints[0].length;
        int degree = knotData.degree();
        int i_seg = knotData.segmentIndex(para);
        int[] new_n;
        int new_uicp;
        int new_uik;
        double[] new_knots;
        double[] knots;
        int[] new_knot_multi;
        int[] knot_multi;
        double[][] new_cntrl_pnts;
        int[] nk_info = new int[2]; /* [0]: index of target knot,
        [1]: current multiplicity of that */

        double[] t;
        double p_tol = ConditionOfOperation.getCondition()
                                           .getToleranceForParameter();
        int j;
        int k;
        int l;

        Object[] result = new Object[2];
        BsplineKnot newKnotData;
        double[][] newControlPoints;

        /*
         * revise the knot vector
         */
        new_n = get_new_ui_c(knotData, 1);
        new_uicp = new_n[0];
        new_uik = new_n[1];

        new_knots = new double[new_uik];
        new_knot_multi = new int[new_uik];
        new_cntrl_pnts = new double[new_uicp][dimension];
        new_uik--;

        new_uik = fill_new_knot(knotData, para, new_uik, new_knots,
                new_knot_multi, nk_info, p_tol);
        new_knot_multi[nk_info[0]]++;

        /*
         * revise the control points
         */
        if (knotData.isNonPeriodic()) {
            /*
             * open curve
             */
            for (j = 0; j <= i_seg; j++)
                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[j][l] = controlPoints[j][l];

            for (j = (i_seg + 1); j <= (i_seg + degree); j++) {
                t = comp_iratio(knotData, j, para, p_tol);
                comp_r2p(dimension, new_cntrl_pnts, j, t[0], controlPoints,
                    (j - 1), t[1], controlPoints, j);
            }

            for (j = i_seg + degree + 1; j < new_uicp; j++)
                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[j][l] = controlPoints[j - 1][l];

            newKnotData = make_open_knot(degree, new_knot_multi, new_knots,
                    new_uik, new_uicp);
        } else {
            /*
             * closed curve
             */
            int k_0;

            /*
             * closed curve
             */
            int k_1;
            int n_kel;

            BsplineKnot kdn = new BsplineKnot(degree, KnotType.UNSPECIFIED,
                    true, new_uik, new_knot_multi, new_knots, new_uicp, false); // �ꎞ�I�ɕs���ȃm�b�g��?�?�����̂�doCheck��false��
            n_kel = kdn.nKnotValues();

            kdn = treat_closed_bsp(kdn, n_kel, para, p_tol);

            for (j = 0; j <= i_seg; j++) {
                k = j % knotData.nControlPoints(); /* j < knotData.nControlPoints() < kdn.nControlPoints() */

                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[k][l] = controlPoints[k][l];
            }

            for (j = (i_seg + 1); j <= (i_seg + degree); j++) {
                k_0 = j % kdn.nControlPoints();
                k = j % knotData.nControlPoints();
                k_1 = (j - 1) % knotData.nControlPoints();
                t = comp_iratio(knotData, j, para, p_tol);
                comp_r2p(dimension, new_cntrl_pnts, k_0, t[0], controlPoints,
                    k_1, t[1], controlPoints, k);
            }

            for (j = i_seg + degree + 1; j < (kdn.nControlPoints() + degree);
                    j++) {
                if (j >= kdn.nControlPoints()) {
                    continue;
                }

                k_0 = j % kdn.nControlPoints();
                k_1 = (j - 1) % knotData.nControlPoints();

                for (l = 0; l < dimension; l++)
                    new_cntrl_pnts[k_0][l] = controlPoints[k_1][l];
            }

            newKnotData = kdn;
        }

        result[0] = newKnotData;
        result[1] = new_cntrl_pnts;

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param knotData DOCUMENT ME!
     * @param n_ins DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int[] get_new_ui_c(BsplineKnot knotData, int n_ins) {
        int[] new_n = new int[2];

        new_n[0] = knotData.nControlPoints() + n_ins;
        new_n[1] = ((knotData.knotSpec() == KnotType.UNSPECIFIED)
            ? knotData.nKnots() : knotData.nKnotValues()) + 1;

        return new_n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param knotData DOCUMENT ME!
     * @param para DOCUMENT ME!
     * @param new_uik DOCUMENT ME!
     * @param new_knots DOCUMENT ME!
     * @param new_knot_multi DOCUMENT ME!
     * @param nk_info DOCUMENT ME!
     * @param p_tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws FatalException DOCUMENT ME!
     */
    private static int fill_new_knot(BsplineKnot knotData, double para,
        int new_uik, double[] new_knots, int[] new_knot_multi, int[] nk_info,
        double p_tol) {
        double[] ok;
        int[] om;
        int i;
        int f;

        if (knotData.knotSpec() == KnotType.UNSPECIFIED) {
            ok = knotData.knots();
            om = knotData.knotMultiplicities();
        } else {
            ok = new double[new_uik];
            om = new int[new_uik];

            for (i = 0; i < new_uik; i++) {
                ok[i] = i - knotData.degree();
                om[i] = 1;
            }
        }

        f = 0;
        nk_info[0] = -1;

        for (i = 0; i < new_uik; i++) {
            if (Math.abs(ok[i] - para) < p_tol) {
                /*
                 * the new knot is coincide with knots[i].
                 */
                if (nk_info[0] < 0) {
                    if (i == (new_uik - 1)) {
                        new_knots[f] = para;
                        new_knot_multi[f] = 0;
                        nk_info[0] = f;
                        nk_info[1] = new_knot_multi[f];
                        f++;
                    }

                    new_knots[f] = ok[i];
                    new_knot_multi[f] = om[i];

                    if (f == 0) {
                        f++;
                        new_knots[f] = para;
                        new_knot_multi[f] = 0;
                    }

                    if (i != (new_uik - 1)) {
                        nk_info[0] = f;
                        nk_info[1] = new_knot_multi[f];
                    }

                    f++;
                } else {
                    nk_info[1] = (new_knot_multi[nk_info[0]] += om[i]);
                }
            } else if ((i > 0) && ((ok[i - 1] + p_tol) < para) &&
                    (para < (ok[i] - p_tol))) {
                /*
                 * the new knot is in (knots[i-1], knot[i]).
                 */
                new_knots[f] = para;
                new_knot_multi[f] = 0;
                nk_info[0] = f;
                nk_info[1] = new_knot_multi[f];
                f++;

                new_knots[f] = ok[i];
                new_knot_multi[f] = om[i];
                f++;
            } else {
                /*
                 * the new knot is out of (knots[i-1], knot[i]).
                 */
                new_knots[f] = ok[i];
                new_knot_multi[f] = om[i];
                f++;
            }
        }

        if (nk_info[0] <= 0) {
            throw new FatalException();
        }

        return f;
    }

    /**
     * DOCUMENT ME!
     *
     * @param knotData DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param param DOCUMENT ME!
     * @param p_tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[] comp_iratio(BsplineKnot knotData, int j,
        double param, double p_tol) {
        double[] t = new double[2];
        double kf;
        double kb;

        kf = knotData.knotValueAt(j + knotData.degree());
        kb = knotData.knotValueAt(j);

        double kdiff = kf - kb;

        if (kdiff < p_tol) {
            t[0] = 0.5;
            t[1] = 0.5;

            return t;
        }

        t[0] = (kf - param) / kdiff;
        t[1] = 1.0 - t[0];

        return t;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dimension DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param di DOCUMENT ME!
     * @param t1 DOCUMENT ME!
     * @param s1 DOCUMENT ME!
     * @param s1i DOCUMENT ME!
     * @param t2 DOCUMENT ME!
     * @param s2 DOCUMENT ME!
     * @param s2i DOCUMENT ME!
     */
    private static void comp_r2p(int dimension, double[][] d, int di,
        double t1, double[][] s1, int s1i, double t2, double[][] s2, int s2i) {
        for (int i = 0; i < dimension; i++)
            d[di][i] = (t1 * s1[s1i][i]) + (t2 * s2[s2i][i]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d_knots DOCUMENT ME!
     * @param d_knot_multi DOCUMENT ME!
     * @param s_knots DOCUMENT ME!
     * @param s_knot_multi DOCUMENT ME!
     * @param n DOCUMENT ME!
     */
    private static void copy_kinfo(double[] d_knots, int[] d_knot_multi,
        double[] s_knots, int[] s_knot_multi, int n) {
        for (int i = 0; i < n; i++) {
            d_knots[i] = s_knots[i];
            d_knot_multi[i] = s_knot_multi[i];
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param nk_info DOCUMENT ME!
     * @param knot_multi DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int get_front_uicp(int[] nk_info, int[] knot_multi) {
        int uicp;

        uicp = 0;

        for (int j = (nk_info[0] - 1); j >= 0; j--)
            uicp += knot_multi[j];

        return uicp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nk_info DOCUMENT ME!
     * @param knot_multi DOCUMENT ME!
     * @param uik DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int get_rear_uicp(int[] nk_info, int[] knot_multi, int uik) {
        int uicp;

        uicp = 0;

        for (int j = (nk_info[0] + 1); j < uik; j++)
            uicp += knot_multi[j];

        return uicp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dk DOCUMENT ME!
     * @param dkm DOCUMENT ME!
     * @param sk DOCUMENT ME!
     * @param skm DOCUMENT ME!
     * @param uik DOCUMENT ME!
     * @param deg DOCUMENT ME!
     */
    private static void copy_front_kinfo(double[] dk, int[] dkm, double[] sk,
        int[] skm, int uik, int deg) {
        int j = uik - 1;

        copy_kinfo(dk, dkm, sk, skm, j);
        dk[j] = sk[j];
        dkm[j] = (deg + 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param dk DOCUMENT ME!
     * @param dkm DOCUMENT ME!
     * @param sk DOCUMENT ME!
     * @param skm DOCUMENT ME!
     * @param fuik DOCUMENT ME!
     * @param uik DOCUMENT ME!
     * @param deg DOCUMENT ME!
     */
    private static void copy_rear_kinfo(double[] dk, int[] dkm, double[] sk,
        int[] skm, int fuik, int uik, int deg) {
        int k = fuik - 1;
        double init_knot = sk[k];

        dk[0] = 0.0;
        dkm[0] = (deg + 1);

        for (int j = 1; j < uik; j++) {
            dk[j] = sk[k + j] - init_knot;
            dkm[j] = skm[k + j];
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param uik DOCUMENT ME!
     * @param knots DOCUMENT ME!
     * @param para DOCUMENT ME!
     * @param p_tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int get_knot_idx(int uik, double[] knots, double para,
        double p_tol) {
        int i;

        for (i = 0; i < uik; i++)
            if (Math.abs(knots[i] - para) < p_tol) {
                break;
            }

        return i;
    }

    /**
     * DOCUMENT ME!
     *
     * @param degree DOCUMENT ME!
     * @param knot_multi DOCUMENT ME!
     * @param knots DOCUMENT ME!
     * @param uik DOCUMENT ME!
     * @param uicp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static BsplineKnot make_open_knot(int degree, int[] knot_multi,
        double[] knots, int uik, int uicp) {
        if (uicp < 0) {
            uicp = 0;

            for (int i = 0; i < uik; i++)
                uicp += knot_multi[i];

            uicp -= (degree + 1);
        }

        return new BsplineKnot(degree, KnotType.UNSPECIFIED, false, uik,
            knot_multi, knots, uicp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param knotData DOCUMENT ME!
     * @param n_kel DOCUMENT ME!
     * @param para DOCUMENT ME!
     * @param p_tol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws FatalException DOCUMENT ME!
     */
    private static BsplineKnot treat_closed_bsp(BsplineKnot knotData,
        int n_kel, double para, double p_tol) {
        int degree = knotData.degree();
        double[] simple_knots;
        int[] simple_knot_multi;
        int ick; /* index of changed knot */
        int chck_intvl;
        int chck_head_s;
        int chck_head_e;
        int chck_tail_s;
        int chck_tail_e;
        int refer;
        int i;
        int j;
        int k;

        /*
         * make knot vector simple
         */
        simple_knots = new double[n_kel];
        simple_knot_multi = new int[n_kel];

        ick = (-1);

        for (i = 0; i < n_kel; i++) {
            simple_knots[i] = knotData.knotValueAt(i);
            simple_knot_multi[i] = 1;

            if ((ick < 0) && (Math.abs(simple_knots[i] - para) < p_tol)) {
                ick = i;
            }
        }

        /*
         * adjust both ends
         */
        chck_intvl = 2 * degree;
        chck_head_s = 1;
        chck_head_e = 2 * degree;
        chck_tail_s = n_kel - (2 * degree);
        chck_tail_e = n_kel - 1;

        refer = 0;

        if ((chck_head_s <= ick) && (ick <= chck_head_e)) {
            refer |= 1;
        }

        if ((chck_tail_s <= ick) && (ick <= chck_tail_e)) {
            refer |= 2;
        }

        if (refer == 0) {
            return knotData;
        }

        switch (refer) {
        case 1:
            j = chck_head_s + 1;
            k = chck_tail_s + 1;

            for (i = 1; i < chck_intvl; i++) {
                simple_knots[k] = simple_knots[k - 1] +
                    (simple_knots[j] - simple_knots[j - 1]);
                j++;
                k++;
            }

            /* first knot */
            j -= (chck_intvl + 1);
            k -= (chck_intvl + 1);
            simple_knots[j] = simple_knots[j + 1] -
                (simple_knots[k + 1] - simple_knots[k]);

            break;

        case 2:
            j = chck_head_e - 1;
            k = chck_tail_e - 1;

            for (i = 1; i < chck_intvl; i++) {
                simple_knots[j] = simple_knots[j + 1] -
                    (simple_knots[k + 1] - simple_knots[k]);
                j--;
                k--;
            }

            /* first knot */
            simple_knots[j] = simple_knots[j + 1] -
                (simple_knots[k + 1] - simple_knots[k]);

            break;

        case 3:
            j = chck_head_e - 1;
            k = chck_tail_e - 1;
            simple_knots[k + 1] = simple_knots[k] +
                (simple_knots[j + 1] - simple_knots[j]);
            j = chck_head_s + 1;
            k = chck_tail_s + 1;
            simple_knots[j - 1] = simple_knots[j] -
                (simple_knots[k] - simple_knots[k - 1]);

            /* first knot */
            simple_knots[j - 2] = simple_knots[j - 1] -
                (simple_knots[k - 1] - simple_knots[k - 2]);

            break;
        }

        /*
         * replace & beatify
         */
        try {
            int uicp = 0;

            for (i = 0; i < n_kel; i++)
                uicp += simple_knot_multi[i];

            if (knotData.isPeriodic()) {
                uicp -= ((2 * degree) + 1);
            } else {
                uicp -= (degree + 1);
            }

            return new BsplineKnot(degree, knotData.knotSpec(),
                knotData.isPeriodic(), n_kel, simple_knot_multi, simple_knots,
                uicp).beautify();
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
    }
}
