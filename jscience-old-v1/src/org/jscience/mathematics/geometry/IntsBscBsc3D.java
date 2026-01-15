/*
 * 3D B�X�v���C����?�m�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsBscBsc3D.java,v 1.3 2007-10-23 18:19:41 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import java.util.Enumeration;
import java.util.Vector;


/**
 * 3D
 * B�X�v���C����?�m�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:41 $
 */
final class IntsBscBsc3D {
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
        BsplineCurve3D bscA, BsplineCurve3D bscB) {
        // ��?� A �̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsA = bscA.validSegments();

        // ��?� A ��\���x�W�G��?��
        PureBezierCurve3D[] bzcsA = bscA.toPureBezierCurveArray();

        // ��?� B �̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsB = bscB.validSegments();

        // ��?� B ��\���x�W�G��?��
        PureBezierCurve3D[] bzcsB = bscB.toPureBezierCurveArray();

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
                CurveCurveInterference3D[] localInterferences = IntsBzcBzc3D.interference(bzcsA[iA],
                        bzcsB[iB], false);

                // ��_�㊃X�g�ɒǉB���
                Vector intsList = CurveCurveInterferenceList.extractIntersections(localInterferences);

                for (Enumeration e = intsList.elements(); e.hasMoreElements();) {
                    IntersectionPoint3D ints = (IntersectionPoint3D) e.nextElement();
                    interferenceList.addAsIntersection(ints.coordinates(),
                        vldsA.l2Gp(iA, ints.pointOnCurve1().parameter()),
                        vldsB.l2Gp(iB, ints.pointOnCurve2().parameter()));
                }

                // ?d���㊃X�g�ɒǉB���
                Vector ovlpList = CurveCurveInterferenceList.extractOverlaps(localInterferences);

                for (Enumeration e = ovlpList.elements(); e.hasMoreElements();) {
                    OverlapCurve3D ovlp = (OverlapCurve3D) e.nextElement();
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
     * @see CurveCurveInterference3D
     */
    static CurveCurveInterference3D[] interference(BsplineCurve3D bscA,
        BsplineCurve3D bscB, boolean doExchange) {
        return getInterference(bscA, bscB)
                   .toCurveCurveInterference3DArray(doExchange);
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
     * @see IntersectionPoint3D
     */
    static IntersectionPoint3D[] intersection(BsplineCurve3D bscA,
        BsplineCurve3D bscB, boolean doExchange) {
        return getInterference(bscA, bscB).toIntersectionPoint3DArray(doExchange);
    }
}
// end of file
