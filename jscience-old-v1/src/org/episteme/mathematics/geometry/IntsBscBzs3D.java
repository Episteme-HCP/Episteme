/*
 * 3D B�X�v���C����?�ƃx�W�G�Ȗʂ̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsBscBzs3D.java,v 1.3 2007-10-23 18:19:41 virtualcall Exp $
 */
package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;


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
