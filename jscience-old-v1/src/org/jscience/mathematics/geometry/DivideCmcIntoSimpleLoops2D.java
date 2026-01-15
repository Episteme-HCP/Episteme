/*
 * 2D ������?���?�Ȍ�?������?��ŕ�������?A
 * ���Ȍ�?����Ȃ��P?��ȕ�?��?W?��ɂ���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: DivideCmcIntoSimpleLoops2D.java,v 1.4 2007-10-23 18:19:39 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import java.util.Vector;


/**
 * 2D
 * ������?���?�Ȍ�?������?��ŕ�������?A
 * 
 * ���Ȍ�?����Ȃ��P?��ȕ�?��?W?��ɂ���N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.4 $, $Date: 2007-10-23 18:19:39 $
 */
class DivideCmcIntoSimpleLoops2D {
    /**
     * DOCUMENT ME!
     */
    private static final int VI_A = 0;

    /**
     * DOCUMENT ME!
     */
    private static final int VI_B = 0;

    /** ������?���?� */
    private CompositeCurve2D closedCmc;

    /**
     * ������?���?�̉�����l��?A
     * ���v��� (�E���) �ɉ�BĂ���
     * (�ƌ��Ȃ�����) �̂ł���� LoopWise.CW?A
     * �����v��� (?����) �ɉ�BĂ���
     * (�ƌ��Ȃ�����) �̂ł���� LoopWise.CCW
     * ��w�肷��
     */
    private int cmcWise;

    /**
     * ������?���?�̋�?��ǂ��瑤�ɃI�t�Z�b�g������̂ł��邩���l��?A
     * 
     * ���̋�?��Ѥ�ɃI�t�Z�b�g������̂ł����
     * WhichSide.IN?A
     * ���̋�?��O���ɃI�t�Z�b�g������̂ł����
     * WhichSide.OUT ��w�肷��
     */
    private int validSide;

    /** ?�?����� ParameterOnSegment �̌�?� */
    private int parameterOnSegmentCounter;

    /** �p�x�̋��e��?� */
    private double aTol;

/**
     * ���Z�I�u�W�F�N�g��?\�z����
     *
     * @param closedCmc ������?���?�
     * @param cmcWise   ������?���?�̉�����l
     * @param validSide ������?���?�̋�?��ǂ��瑤�ɃI�t�Z�b�g������̂ł��邩���l
     * @throws OpenCurveException �J������?�ł���
     */
    DivideCmcIntoSimpleLoops2D(CompositeCurve2D closedCmc, int cmcWise,
        int validSide) throws OpenCurveException {
        // ���Ă���K�v������
        if (closedCmc.isClosed() != true) {
            throw new OpenCurveException();
        }

        if (closedCmc.isPeriodic() != true) {
            closedCmc = new CompositeCurve2D(closedCmc.segments(), true);
        }

        this.closedCmc = closedCmc;
        this.cmcWise = cmcWise;
        this.validSide = validSide;

        this.parameterOnSegmentCounter = 0;
        this.aTol = ConditionOfOperation.getCondition().getToleranceForAngle();
    }

    /**
     * ������?���?�Ȍ�?������?��ŕ�������?A���Ȍ�?����Ȃ��P?��ȕ�?��?W?��ɂ���
     *
     * @return ���Ȍ�?����Ȃ��P?��ȕ�?��?W?�
     */
    CompositeCurve2D[] doIt() {
        // �Z�O�?���g�Ԃ̎��Ȍ�?��_��?�߂�
        CompositeCurveSegment2D[] segments = closedCmc.decomposeAsSegmentsRecursively();
        int nSegments = segments.length;
        ToleranceForDistance dTol = closedCmc.getToleranceForDistanceAsObject();

        Vector selfIntsList = new Vector();

        for (int i = 0; i < nSegments; i++) {
            IntersectionPoint2D[] ints;

            if (segments[i].isFreeform() == true) {
                Polyline2D polyline = segments[i].toPolyline(dTol);
                Point1D[] parameters = new Point1D[polyline.nPoints()];

                for (int j = 0; j < polyline.nPoints(); j++) {
                    PointOnCurve2D poc = (PointOnCurve2D) polyline.pointAt(j);
                    parameters[j] = Point1D.of(poc.parameter());
                }

                Polyline1D polyline1D = new Polyline1D(parameters,
                        polyline.isPeriodic());

                ints = polyline.selfIntersect();

                Point1D pnt1D;

                for (int j = 0; j < ints.length; j++) {
                    pnt1D = polyline1D.coordinates(ints[j].pointOnCurve1()
                                                          .parameter());

                    ParameterOnSegment posA = new ParameterOnSegment(segments[i],
                            i, pnt1D.x());
                    pnt1D = polyline1D.coordinates(ints[j].pointOnCurve2()
                                                          .parameter());

                    ParameterOnSegment posB = new ParameterOnSegment(segments[i],
                            i, pnt1D.x());
                    IntersectionInfo intsInfo = new IntersectionInfo(ints[j],
                            posA, posB);
                    selfIntsList.addElement(intsInfo);
                }
            }

            for (int k = (i + 1); k < nSegments; k++) {
                ints = segments[i].intersect(segments[k]);

                for (int j = 0; j < ints.length; j++) {
                    ParameterOnSegment posA = new ParameterOnSegment(segments[i],
                            i, ints[j].pointOnCurve1().parameter());
                    ParameterOnSegment posB = new ParameterOnSegment(segments[k],
                            k, ints[j].pointOnCurve2().parameter());
                    IntersectionInfo intsInfo = new IntersectionInfo(ints[j],
                            posA, posB);
                    selfIntsList.addElement(intsInfo);
                }
            }
        }

        // ���Ȍ�?��_���Ȃ����?A��?g��Ԃ�
        if (selfIntsList.size() == 0) {
            CompositeCurve2D[] result = new CompositeCurve2D[1];
            result[0] = closedCmc;

            return result;
        }

        // ���Ȍ�?��_��\?[�g
        ListSorter.ObjectComparator comparator = new ListSorter.ObjectComparator() {
                public boolean latterIsGreaterThanFormer(
                    java.lang.Object former, java.lang.Object latter) {
                    IntersectionInfo f = (IntersectionInfo) former;
                    IntersectionInfo l = (IntersectionInfo) latter;

                    if (f == l) {
                        return false;
                    }

                    if (f.posA.segmentIndex < l.posA.segmentIndex) {
                        return true;
                    }

                    if (f.posA.segmentIndex > l.posA.segmentIndex) {
                        return false;
                    }

                    return (f.posA.parameter < l.posA.parameter) ? true : false;
                }
            };

        ListSorter.doSorting(selfIntsList, comparator);

        // ���Ȍ�?��_��?A�P?���?[�v�ɕ�������
        return divideIntoSimpleLoops(segments, selfIntsList);
    }

    /**
     * DOCUMENT ME!
     *
     * @param intersectionList DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private VertexInfo[] makeInOutInfo(Vector intersectionList) {
        int nVInfo = intersectionList.size();
        VertexInfo[] vInfo = new VertexInfo[nVInfo];

        /*
        * fill 'me'
        */
        for (int i = 0; i < nVInfo; i++) {
            IntersectionInfo ints = (IntersectionInfo) intersectionList.elementAt(i);
            vInfo[i] = new VertexInfo(ints.posA, ints.posB);
        }

        /*
        * fill 'pair'
        */
        for (int i = 0; i < nVInfo; i++) {
            for (int k = 0; k < 2; k++) {
                for (int j = 0; j < nVInfo; j++) {
                    for (int l = 0; l < 2; l++) {
                        if (vInfo[i].pair[k] == null) {
                            if (vInfo[i].me[k].isMateBig(vInfo[j].me[l]) == true) { /* < */
                                vInfo[i].pair[k] = vInfo[j].me[l];
                            }
                        } else {
                            if ((vInfo[i].me[k].isMateBig(vInfo[j].me[l]) == true) /* < */ &&
                                    (vInfo[i].pair[k].isMateBig(vInfo[j].me[l]) != true)) { /* > */
                                vInfo[i].pair[k] = vInfo[j].me[l];
                            }
                        }
                    }
                }

                if (vInfo[i].pair[k] == null) { /* vInfo[i].me[k] is greatest */
                    vInfo[i].pair[k] = vInfo[i].me[k];

                    for (int j = 0; j < nVInfo; j++) {
                        for (int l = 0; l < 2; l++) {
                            if (vInfo[i].pair[k].isMateBig(vInfo[j].me[l]) != true) { /* > */
                                vInfo[i].pair[k] = vInfo[j].me[l];
                            }
                        }
                    }
                }
            }
        }

        return vInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vInfo DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ParameterOnSegment getUnusedOut(VertexInfo[] vInfo) {
        for (int i = 0; i < vInfo.length; i++) {
            if (vInfo[i].used[VI_A] == false) {
                return vInfo[i].me[VI_A];
            }

            if (vInfo[i].used[VI_B] == false) {
                return vInfo[i].me[VI_B];
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vInfo DOCUMENT ME!
     * @param popc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ParameterOnSegment getMate(VertexInfo[] vInfo,
        ParameterOnSegment popc) {
        for (int i = 0; i < vInfo.length; i++) {
            if (vInfo[i].me[VI_A] == popc) {
                return vInfo[i].me[VI_B];
            }

            if (vInfo[i].me[VI_B] == popc) {
                return vInfo[i].me[VI_A];
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vInfo DOCUMENT ME!
     * @param out DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ParameterOnSegment getNextIn(VertexInfo[] vInfo,
        ParameterOnSegment out) {
        for (int i = 0; i < vInfo.length; i++) {
            if (vInfo[i].me[VI_A] == out) {
                vInfo[i].used[VI_A] = true;

                return vInfo[i].pair[VI_A];
            }

            if (vInfo[i].me[VI_B] == out) {
                vInfo[i].used[VI_B] = true;

                return vInfo[i].pair[VI_B];
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean forward2Right(double cp) {
        return (cp < (-this.aTol));
    }

    /**
     * DOCUMENT ME!
     *
     * @param cp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean forward2Left(double cp) {
        return (cp > this.aTol);
    }

    /**
     * DOCUMENT ME!
     *
     * @param in_tang DOCUMENT ME!
     * @param out_tang DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int isValidCmc(Vector2D in_tang, Vector2D out_tang) {
        Vector2D iu_tang = in_tang.unitized();
        Vector2D ou_tang = out_tang.unitized();
        double crs_prod = in_tang.zOfCrossProduct(ou_tang);

        int is_valid;

        if (this.cmcWise == LoopWise.CW) {
/**/
            if (forward2Right(crs_prod)) {
                is_valid = TrueFalseUndefined.TRUE;
            } else if (forward2Left(crs_prod)) {
                is_valid = TrueFalseUndefined.FALSE;
            } else {
                is_valid = TrueFalseUndefined.UNDEFINED;
            }
        } else { /* CCW */

/**/
            if (forward2Right(crs_prod)) {
                is_valid = TrueFalseUndefined.FALSE;
            } else if (forward2Left(crs_prod)) {
                is_valid = TrueFalseUndefined.TRUE;
            } else {
                is_valid = TrueFalseUndefined.UNDEFINED;
            }
        }

        if (this.validSide == WhichSide.OUT) {
/**/
            if (is_valid == TrueFalseUndefined.TRUE) {
                is_valid = TrueFalseUndefined.FALSE;
            } else if (is_valid == TrueFalseUndefined.FALSE) {
                is_valid = TrueFalseUndefined.TRUE;
            }
        }

        return is_valid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param segmentList DOCUMENT ME!
     * @param segment DOCUMENT ME!
     * @param sP DOCUMENT ME!
     * @param eP DOCUMENT ME!
     * @param transition DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private CompositeCurveSegment2D addTruncatedSegment(Vector segmentList,
        CompositeCurveSegment2D segment, double sP, double eP, int transition) {
        ParameterSection trimmingSection = new ParameterSection(sP, (eP - sP));
        CompositeCurveSegment2D trimmedSegment = segment.truncate(trimmingSection,
                transition);
        segmentList.addElement(trimmedSegment);

        return trimmedSegment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param segments DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getNextSegmentIndex(CompositeCurveSegment2D[] segments, int i) {
        return (++i < segments.length) ? i : 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param segments DOCUMENT ME!
     * @param intersectionList DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private CompositeCurve2D[] divideIntoSimpleLoops(
        CompositeCurveSegment2D[] segments, Vector intersectionList) {
        Vector resultVector = new Vector();

        VertexInfo[] vInfo = makeInOutInfo(intersectionList);

        Vector segmentList;
        CompositeCurveSegment2D lastSegment;

        ParameterOnSegment first_out;
        ParameterOnSegment c_out;
        ParameterOnSegment c_in;
        int loop_is_valid;
        int is_v;

        Vector2D in_tang = null;
        Vector2D out_tang;
        Vector2D first_out_tang = null;

        int segmentIndex;

        while ((first_out = getUnusedOut(vInfo)) != null) {
            segmentList = new Vector();

            c_out = first_out;
            lastSegment = null;
            loop_is_valid = TrueFalseUndefined.UNDEFINED;

            do {
                c_in = getNextIn(vInfo, c_out);

                out_tang = c_out.segment.tangentVector(c_out.parameter);

                if (lastSegment == null) {
                    first_out_tang = out_tang;
                } else {
                    is_v = isValidCmc(in_tang, out_tang);

                    if (is_v != TrueFalseUndefined.UNDEFINED) {
                        if (loop_is_valid != TrueFalseUndefined.FALSE) {
                            loop_is_valid = is_v;
                        }
                    }
                }

                if ((c_out.segmentIndex == c_in.segmentIndex) &&
                        (c_out.isMateBig(c_in) == true)) {
                    /*
                    * one curve
                    */
                    lastSegment = addTruncatedSegment(segmentList,
                            c_out.segment, c_out.parameter, c_in.parameter,
                            TransitionCode.CONTINUOUS);
                } else {
                    /*
                    * two or more curves
                    */
                    lastSegment = addTruncatedSegment(segmentList,
                            c_out.segment, c_out.parameter,
                            c_out.segment.eParameter(),
                            c_out.segment.transition());

                    segmentIndex = c_out.segmentIndex;

                    while ((segmentIndex = getNextSegmentIndex(segments,
                                    segmentIndex)) != c_in.segmentIndex) {
                        segmentList.addElement(segments[segmentIndex]);
                        lastSegment = segments[segmentIndex];
                    }

                    lastSegment = addTruncatedSegment(segmentList,
                            c_in.segment, c_in.segment.sParameter(),
                            c_in.parameter, TransitionCode.CONTINUOUS);
                }

                if (lastSegment != null) {
                    in_tang = lastSegment.tangentVector(lastSegment.eParameter());
                }
            } while ((c_out = getMate(vInfo, c_in)) != first_out);

            is_v = isValidCmc(in_tang, first_out_tang);

            if (is_v != TrueFalseUndefined.UNDEFINED) {
                if (loop_is_valid != TrueFalseUndefined.FALSE) {
                    loop_is_valid = is_v;
                }
            }

            CompositeCurveSegment2D[] newSegments = new CompositeCurveSegment2D[segmentList.size()];
            segmentList.copyInto(newSegments);

            CompositeCurve2D newCurve = new CompositeCurve2D(newSegments, true);
            resultVector.addElement(newCurve);
        }

        CompositeCurve2D[] result = new CompositeCurve2D[resultVector.size()];
        resultVector.copyInto(result);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
      */
    private class ParameterOnSegment {
        /**
         * DOCUMENT ME!
         */
        CompositeCurveSegment2D segment;

        /**
         * DOCUMENT ME!
         */
        int segmentIndex;

        /**
         * DOCUMENT ME!
         */
        double parameter;

        /**
         * DOCUMENT ME!
         */
        int id;

        /**
         * Creates a new ParameterOnSegment object.
         *
         * @param s DOCUMENT ME!
         * @param i DOCUMENT ME!
         * @param p DOCUMENT ME!
         */
        ParameterOnSegment(CompositeCurveSegment2D s, int i, double p) {
            segment = s;
            segmentIndex = i;
            parameter = p;
            id = DivideCmcIntoSimpleLoops2D.this.parameterOnSegmentCounter++;
        }

        /**
         * DOCUMENT ME!
         *
         * @param mate DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        boolean isMateBig(ParameterOnSegment mate) {
            boolean result;

            if (this.segmentIndex < mate.segmentIndex) {
                result = true;
            } else if (this.segmentIndex > mate.segmentIndex) {
                result = false;
            } else {
                result = false;

                if (this.parameter < mate.parameter) {
                    result = true;
                }

                if (result == false) {
                    if ((this.id != mate.id) &&
                            (!(this.parameter < mate.parameter)) &&
                            (!(this.parameter > mate.parameter))) {
                        /* 2 parameters are exactly same */
                        if (this.id < mate.id) {
                            result = true;
                        }
                    }
                }
            }

            return result;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
      */
    private class IntersectionInfo {
        /**
         * DOCUMENT ME!
         */
        Point2D coordinates;

        /**
         * DOCUMENT ME!
         */
        ParameterOnSegment posA;

        /**
         * DOCUMENT ME!
         */
        ParameterOnSegment posB;

        /**
         * Creates a new IntersectionInfo object.
         *
         * @param c DOCUMENT ME!
         * @param pA DOCUMENT ME!
         * @param pB DOCUMENT ME!
         */
        IntersectionInfo(Point2D c, ParameterOnSegment pA, ParameterOnSegment pB) {
            coordinates = c;
            posA = pA;
            posB = pB;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
      */
    private class VertexInfo {
        /**
         * DOCUMENT ME!
         */
        boolean[] used; // TRUE if outward path was already used

        /**
         * DOCUMENT ME!
         */
        ParameterOnSegment[] me; // pointer to my PntOnCmc

        /**
         * DOCUMENT ME!
         */
        ParameterOnSegment[] pair; // pointer to the next-in of outward path

        /**
         * Creates a new VertexInfo object.
         *
         * @param posA DOCUMENT ME!
         * @param posB DOCUMENT ME!
         */
        VertexInfo(ParameterOnSegment posA, ParameterOnSegment posB) {
            used = new boolean[2];
            me = new ParameterOnSegment[2];
            pair = new ParameterOnSegment[2];

            used[VI_A] = false;
            used[VI_B] = false;

            me[VI_A] = posA;
            me[VI_B] = posB;

            pair[VI_A] = null;
            pair[VI_B] = null;
        }
    }
}
