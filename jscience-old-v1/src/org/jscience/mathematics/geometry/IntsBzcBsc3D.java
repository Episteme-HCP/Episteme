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

import java.util.Enumeration;
import java.util.Vector;


/**
 * 3D
 * �x�W�G��?��B�X�v���C����?�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:41 $
 */
final class IntsBzcBsc3D {
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
        PureBezierCurve3D bzcA, BsplineCurve3D bscB) {
        // ��?� B �̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsB = bscB.validSegments();

        // ��?� B ��\���x�W�G��?��
        PureBezierCurve3D[] bzcsB = bscB.toPureBezierCurveArray();

        // ��?̃��X�g
        CurveCurveInterferenceList interferenceList = new CurveCurveInterferenceList(bzcA,
                bscB);

        // ��?� B �̊e�Z�O�?���g�ɑ΂���
        for (int iB = 0; iB < bzcsB.length; iB++) {
            /** Debug bzcA.output(System.out); bzcsB[iB].output(System.out); */

            // �x�W�G��?�x���ł̊�?𓾂�
            CurveCurveInterference3D[] localInterferences = IntsBzcBzc3D.interference(bzcA,
                    bzcsB[iB], false);

            // ��_�㊃X�g�ɒǉB���
            Vector intsList = CurveCurveInterferenceList.extractIntersections(localInterferences);

            for (Enumeration e = intsList.elements(); e.hasMoreElements();) {
                IntersectionPoint3D ints = (IntersectionPoint3D) e.nextElement();
                interferenceList.addAsIntersection(ints.coordinates(),
                    ints.pointOnCurve1().parameter(),
                    vldsB.l2Gp(iB, ints.pointOnCurve2().parameter()));
            }

            // ?d���㊃X�g�ɒǉB���
            Vector ovlpList = CurveCurveInterferenceList.extractOverlaps(localInterferences);

            for (Enumeration e = ovlpList.elements(); e.hasMoreElements();) {
                OverlapCurve3D ovlp = (OverlapCurve3D) e.nextElement();
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
     * @see CurveCurveInterference3D
     */
    static CurveCurveInterference3D[] interference(PureBezierCurve3D bzcA,
        BsplineCurve3D bscB, boolean doExchange) {
        return getInterference(bzcA, bscB)
                   .toCurveCurveInterference3DArray(doExchange);
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
     * @see IntersectionPoint3D
     */
    static IntersectionPoint3D[] intersection(PureBezierCurve3D bzcA,
        BsplineCurve3D bscB, boolean doExchange) {
        return getInterference(bzcA, bscB).toIntersectionPoint3DArray(doExchange);
    }
}
// end of file
