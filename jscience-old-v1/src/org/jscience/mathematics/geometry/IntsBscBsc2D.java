/*
 * 2D B�X�v���C����?�m�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsBscBsc2D.java,v 1.3 2007-10-23 18:19:41 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import java.util.Enumeration;
import java.util.Vector;


/**
 * 2D
 * B�X�v���C����?�m�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:41 $
 */
final class IntsBscBsc2D {
    /**
     * 2 B�X�v���C����?�̊�?𓾂�
     *
     * @param bscA B�X�v���C����?� A
     * @param bscB B�X�v���C����?� B
     *
     * @return 2 ��?�̊�?̃��X�g
     *
     * @see CurveCurveInterferenceList
     */
    private static CurveCurveInterferenceList getInterference(
        BsplineCurve2D bscA, BsplineCurve2D bscB) {
        // ��?� A �̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsA = bscA.validSegments();

        // ��?� A ��\���x�W�G��?��
        PureBezierCurve2D[] bzcsA = bscA.toPureBezierCurveArray();

        // ��?� B �̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsB = bscB.validSegments();

        // ��?� B ��\���x�W�G��?��
        PureBezierCurve2D[] bzcsB = bscB.toPureBezierCurveArray();

        // ��?̃��X�g
        CurveCurveInterferenceList interferenceList = new CurveCurveInterferenceList(bscA,
                bscB);

        // ��?� A �̊e�Z�O�?���g�ɑ΂���
        for (int iA = 0; iA < bzcsA.length; iA++) {
            // ��?� B �̊e�Z�O�?���g�ɑ΂���
            for (int iB = 0; iB < bzcsB.length; iB++) {
                /**
                 * Debug bzcsA[iA].output(System.out);
                 * bzcsB[iB].output(System.out);
                 */

                // �x�W�G��?�x���ł̊�?𓾂�
                CurveCurveInterference2D[] localInterferences = IntsBzcBzc2D.interference(bzcsA[iA],
                        bzcsB[iB], false);

                // ��_�㊃X�g�ɒǉB���
                Vector intsList = CurveCurveInterferenceList.extractIntersections(localInterferences);

                for (Enumeration e = intsList.elements(); e.hasMoreElements();) {
                    IntersectionPoint2D ints = (IntersectionPoint2D) e.nextElement();
                    interferenceList.addAsIntersection(ints.coordinates(),
                        vldsA.l2Gp(iA, ints.pointOnCurve1().parameter()),
                        vldsB.l2Gp(iB, ints.pointOnCurve2().parameter()));
                }

                // ?d���㊃X�g�ɒǉB���
                Vector ovlpList = CurveCurveInterferenceList.extractOverlaps(localInterferences);

                for (Enumeration e = ovlpList.elements(); e.hasMoreElements();) {
                    OverlapCurve2D ovlp = (OverlapCurve2D) e.nextElement();
                    interferenceList.addAsOverlap(vldsA.l2Gp(iA, ovlp.start1()),
                        vldsB.l2Gp(iB, ovlp.start2()),
                        vldsA.l2Gw(iA, ovlp.increase1()),
                        vldsB.l2Gw(iB, ovlp.increase2()));
                }
            }
        }

        interferenceList.removeOverlapsContainedInOtherOverlap();
        interferenceList.removeIntersectionsContainedInOverlap();

        return interferenceList;
    }

    /**
     * 2 B�X�v���C����?�̊�?𓾂�
     *
     * @param bscA B�X�v���C����?� A
     * @param bscB B�X�v���C����?� B
     * @param doExchange DOCUMENT ME!
     *
     * @return 2 ��?�̊�?̔z��
     *
     * @see CurveCurveInterference2D
     */
    static CurveCurveInterference2D[] interference(BsplineCurve2D bscA,
        BsplineCurve2D bscB, boolean doExchange) {
        return getInterference(bscA, bscB)
                   .toCurveCurveInterference2DArray(doExchange);
    }

    /**
     * 2 B�X�v���C����?�̌�_�𓾂�
     *
     * @param bscA B�X�v���C����?� A
     * @param bscB B�X�v���C����?� B
     * @param doExchange DOCUMENT ME!
     *
     * @return 2 ��?�̌�_�̔z��
     *
     * @see IntersectionPoint2D
     */
    static IntersectionPoint2D[] intersection(BsplineCurve2D bscA,
        BsplineCurve2D bscB, boolean doExchange) {
        return getInterference(bscA, bscB).toIntersectionPoint2DArray(doExchange);
    }

    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     *
     * @param argv DOCUMENT ME!
     */
    public static void main(String[] argv) {
        int[] knotMulti1 = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
        double[] knots1 = { 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 2.0, 2.0, 2.0 };
        Point2D[] controlPoints1 = new Point2D[5];
        controlPoints1[0] = new CartesianPoint2D(0.0, 0.0);
        controlPoints1[1] = new CartesianPoint2D(10.0, 10.0);
        controlPoints1[2] = new CartesianPoint2D(20.0, -10.0);
        controlPoints1[3] = new CartesianPoint2D(30.0, 0.0);
        controlPoints1[4] = new CartesianPoint2D(40.0, 0.0);

        BsplineCurve2D bsc1 = new BsplineCurve2D(3, knotMulti1, knots1,
                controlPoints1);

        int[] knotMulti2 = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
        double[] knots2 = { 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 2.0, 2.0, 2.0 };
        Point2D[] controlPoints2 = new Point2D[5];
        controlPoints2[0] = new CartesianPoint2D(50.0, 0.0);
        controlPoints2[1] = new CartesianPoint2D(30.0, 1.0);
        controlPoints2[2] = new CartesianPoint2D(10.0, 0.0);
        controlPoints2[3] = new CartesianPoint2D(-10.0, 5.0);
        controlPoints2[4] = new CartesianPoint2D(-30.0, 10.0);

        BsplineCurve2D bsc2 = new BsplineCurve2D(3, knotMulti2, knots2,
                controlPoints2);

        IntersectionPoint2D[] result = intersection(bsc1, bsc2, false);

        for (int i = 0; i < result.length; i++)
            result[i].output(System.out);
    }
}
// end of file
