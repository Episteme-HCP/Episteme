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


/**
 * 3D
 * B�X�v���C����?�ƃx�W�G�Ȗʂ̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:41 $
 */
final class IntsBscBzs3D {
    /** DOCUMENT ME! */
    private static final boolean DEBUG = false;

    /**
     * B�X�v���C����?�ƃx�W�G�Ȗʂ̊�?𓾂�
     *
     * @param bscA B�X�v���C����?� A
     * @param bzsB �x�W�G�Ȗ� B
     *
     * @return ��?�ƋȖʂ̊�?̃��X�g
     *
     * @throws FatalException DOCUMENT ME!
     *
     * @see CurveCurveInterferenceList
     */
    private static CurveSurfaceInterferenceList getInterference(
        BsplineCurve3D bscA, PureBezierSurface3D bzsB) {
        // ��?� A �̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsA = bscA.validSegments();

        // ��?� A ��\���x�W�G��?��
        PureBezierCurve3D[] bzcsA = bscA.toPureBezierCurveArray();

        // ��?̃��X�g
        CurveSurfaceInterferenceList interferenceList = new CurveSurfaceInterferenceList(bscA,
                bzsB);

        // ��?� A �̊e�Z�O�?���g�ɑ΂���
        for (int iA = 0; iA < bzcsA.length; iA++) {
            if (DEBUG) {
                bzcsA[iA].output(System.out);
                bzsB.output(System.out);
            }

            // �x�W�G��?�x���ł̊�?𓾂�
            // **�µ��Ք�ő�?ݗ̈�̃��t�`�F�b�N�ⷂ�Ȃ�
            //   ���炩���ߋ?�߂���̂�n������ǂ�**
            // **���t�`�F�b�N���Ȃ��Ȃ�
            //   �����Ń��t�`�F�b�N��ǉ�
            IntersectionPoint3D[] ints;

            try {
                ints = bzcsA[iA].intersect(bzsB);
            } catch (IndefiniteSolutionException e) {
                throw new FatalException(); // ���̗�O�͔�?����Ȃ��͂�
            }

            // ��_�㊃X�g�ɒǉB���
            for (int i = 0; i < ints.length; i++) {
                interferenceList.addAsIntersection(ints[i].coordinates(),
                    vldsA.l2Gp(iA, ints[i].pointOnCurve1().parameter()),
                    ints[i].pointOnSurface2().uParameter(),
                    ints[i].pointOnSurface2().vParameter());
            }
        }

        return interferenceList;
    }

    /**
     * B�X�v���C����?�ƃx�W�G�Ȗʂ̌�_�𓾂�
     *
     * @param bscA B�X�v���C����?� A
     * @param bzsB �x�W�G�Ȗ� B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��?�ƋȖʂ̌�_�̔z��
     *
     * @see IntersectionPoint3D
     */
    static IntersectionPoint3D[] intersection(BsplineCurve3D bscA,
        PureBezierSurface3D bzsB, boolean doExchange) {
        return getInterference(bscA, bzsB).toIntersectionPoint3DArray(doExchange);
    }
}
// end of file
