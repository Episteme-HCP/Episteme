/*
 * 3D ��?��B�X�v���C���Ȗʂ̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsCrvBss3D.java,v 1.3 2007-10-23 18:19:41 virtualcall Exp $
 */
package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;


/**
 * 3D
 * ��?��B�X�v���C���Ȗʂ̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:41 $
 */
final class IntsCrvBss3D {
    /** DOCUMENT ME! */
    static final boolean debug = false;

    /**
     * ��?��B�X�v���C���Ȗʂ̊�?𓾂�
     *
     * @param crvA ��?� A
     * @param bssB B�X�v���C���Ȗ� B
     *
     * @return ��?�ƋȖʂ̊�?̃��X�g
     *
     * @throws FatalException DOCUMENT ME!
     *
     * @see CurveCurveInterferenceList
     */
    private static CurveSurfaceInterferenceList getInterference(
        ParametricCurve3D crvA, BsplineSurface3D bssB) {
        // �Ȗ� B ��U/V���̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsBu = bssB.uValidSegments();
        BsplineKnot.ValidSegmentInfo vldsBv = bssB.vValidSegments();

        // ��?� B ��\���x�W�G��?��
        PureBezierSurface3D[][] bzssB = bssB.toPureBezierSurfaceArray();

        // ��?̃��X�g
        CurveSurfaceInterferenceList interferenceList = new CurveSurfaceInterferenceList(crvA,
                bssB);

        IntersectionPoint3D[] ints;

        // �Ȗ� B ��U���̊e�Z�O�?���g�ɑ΂���
        for (int iBu = 0; iBu < bzssB.length; iBu++) {
            // �Ȗ� B ��V���̊e�Z�O�?���g�ɑ΂���
            for (int iBv = 0; iBv < bzssB[iBu].length; iBv++) {
                if (debug) {
                    crvA.output(System.out);
                    bzssB[iBu][iBv].output(System.out);
                }

                // �x�W�G�Ȗʃ��x���ł̊�?𓾂�
                try {
                    ints = crvA.intersect(bzssB[iBu][iBv]);
                } catch (IndefiniteSolutionException e) {
                    throw new FatalException();
                }

                // ��_�㊃X�g�ɒǉB���
                for (int i = 0; i < ints.length; i++) {
                    interferenceList.addAsIntersection(ints[i].coordinates(),
                        ints[i].pointOnCurve1().parameter(),
                        vldsBu.l2Gp(iBu, ints[i].pointOnSurface2().uParameter()),
                        vldsBv.l2Gp(iBv, ints[i].pointOnSurface2().vParameter()));
                }
            }
        }

        return interferenceList;
    }

    /**
     * ��?��B�X�v���C���Ȗʂ̌�_�𓾂�
     *
     * @param crvA ��?� A
     * @param bssB B�X�v���C���Ȗ� B
     * @param doExchange DOCUMENT ME!
     *
     * @return ��?�ƋȖʂ̌�_�̔z��
     *
     * @see IntersectionPoint3D
     */
    static IntersectionPoint3D[] intersection(ParametricCurve3D crvA,
        BsplineSurface3D bssB, boolean doExchange) {
        return getInterference(crvA, bssB).toIntersectionPoint3DArray(doExchange);
    }
}
// end of file
