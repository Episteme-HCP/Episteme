/*
 * 2D �x�W�G��?��B�X�v���C����?�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsBzcBsc2D.java,v 1.3 2007-10-23 18:19:41 virtualcall Exp $
 */
package org.episteme.mathematics.geometry;

import java.util.Enumeration;
import java.util.Vector;


/**
 * 2D
 * �x�W�G��?��B�X�v���C����?�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:41 $
 */
final class IntsBzcBsc2D {
    /**
     * �x�W�G��?��B�X�v���C����?�̊�?𓾂�
     *
     * @param bzcA �x�W�G��?� A
     * @param bscB B�X�v���C����?� B
     *
     * @return 2 ��?�̊�?̃��X�g
     *
     * @see CurveCurveInterferenceList
     */
    private static CurveCurveInterferenceList getInterference(
        PureBezierCurve2D bzcA, BsplineCurve2D bscB) {
        // ��?� B �̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsB = bscB.validSegments();

        // ��?� B ��\���x�W�G��?��
        PureBezierCurve2D[] bzcsB = bscB.toPureBezierCurveArray();

        // ��?̃��X�g
        CurveCurveInterferenceList interferenceList = new CurveCurveInterferenceList(bzcA,
                bscB);

        // ��?� B �̊e�Z�O�?���g�ɑ΂���
        for (int iB = 0; iB < bzcsB.length; iB++) {
            /** Debug bzcA.output(System.out); bzcsB[iB].output(System.out); */

            // �x�W�G��?�x���ł̊�?𓾂�
            CurveCurveInterference2D[] localInterferences = IntsBzcBzc2D.interference(bzcA,
                    bzcsB[iB], false);

            // ��_�㊃X�g�ɒǉB���
            Vector intsList = CurveCurveInterferenceList.extractIntersections(localInterferences);

            for (Enumeration e = intsList.elements(); e.hasMoreElements();) {
                IntersectionPoint2D ints = (IntersectionPoint2D) e.nextElement();
                interferenceList.addAsIntersection(ints.coordinates(),
                    ints.pointOnCurve1().parameter(),
                    vldsB.l2Gp(iB, ints.pointOnCurve2().parameter()));
            }

            // ?d���㊃X�g�ɒǉB���
            Vector ovlpList = CurveCurveInterferenceList.extractOverlaps(localInterferences);

            for (Enumeration e = ovlpList.elements(); e.hasMoreElements();) {
                OverlapCurve2D ovlp = (OverlapCurve2D) e.nextElement();
                interferenceList.addAsOverlap(ovlp.start1(),
                    vldsB.l2Gp(iB, ovlp.start2()), ovlp.increase1(),
                    vldsB.l2Gw(iB, ovlp.increase2()));
            }
        }

        interferenceList.removeOverlapsContainedInOtherOverlap();
        interferenceList.removeIntersectionsContainedInOverlap();

        return interferenceList;
    }

    /**
     * �x�W�G��?��B�X�v���C����?�̊�?𓾂�
     *
     * @param bzcA �x�W�G��?� A
     * @param bscB B�X�v���C����?� B
     * @param doExchange DOCUMENT ME!
     *
     * @return 2 ��?�̊�?̔z��
     *
     * @see CurveCurveInterference2D
     */
    static CurveCurveInterference2D[] interference(PureBezierCurve2D bzcA,
        BsplineCurve2D bscB, boolean doExchange) {
        return getInterference(bzcA, bscB)
                   .toCurveCurveInterference2DArray(doExchange);
    }

    /**
     * �x�W�G��?��B�X�v���C����?�̌�_�𓾂�
     *
     * @param bzcA �x�W�G��?� A
     * @param bscB B�X�v���C����?� B
     * @param doExchange DOCUMENT ME!
     *
     * @return 2 ��?�̌�_�̔z��
     *
     * @see IntersectionPoint2D
     */
    static IntersectionPoint2D[] intersection(PureBezierCurve2D bzcA,
        BsplineCurve2D bscB, boolean doExchange) {
        return getInterference(bzcA, bscB).toIntersectionPoint2DArray(doExchange);
    }
}
// end of file
