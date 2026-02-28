/*
 * �Ȗ�(��?͓I/�x�W�G/B�X�v���C��)��B�X�v���C���Ȗʂ̌�?��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsSrfBss3D.java,v 1.3 2007-10-23 18:19:42 virtualcall Exp $
 */
package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;


/**
 * (��?͓I/�x�W�G/B�X�v���C��)�Ȗʂ�B�X�v���C���Ȗʂ̌�?��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:42 $
 */
final class IntsSrfBss3D {
    /** DOCUMENT ME! */
    private static final boolean DEBUG = false;

    /**
     * ��?��p���??[�^�l�ŕ\�����2D�|�����C������l�̂�̂֕ϊ�
     *
     * @param uValidSegments
     *        �Ȗʂ̂�����̗L��ȃZ�O�?���g��?��
     * @param uIndex
     *        ��?ۂƂ���L��ȃZ�O�?���g�̓Y��
     * @param vValidSegments ��?��p���??[�^�l
     * @param vIndex DOCUMENT ME!
     * @param pol2d DOCUMENT ME!
     *
     * @return ���l�ŕ\�����2D�|�����C��
     */
    private static Polyline2D l2Gc(
        BsplineKnot.ValidSegmentInfo uValidSegments, int uIndex,
        BsplineKnot.ValidSegmentInfo vValidSegments, int vIndex,
        Polyline2D pol2d) {
        int npnts = pol2d.nPoints();
        Point2D[] pnts = new Point2D[npnts];

        for (int i = 0; i < npnts; i++)
            pnts[i] = new CartesianPoint2D(uValidSegments.l2Gp(uIndex,
                        pol2d.pointAt(i).x()),
                    vValidSegments.l2Gp(vIndex, pol2d.pointAt(i).y()));

        return new Polyline2D(pnts, pol2d.closed());
    }

    /**
     * ��?͓I�Ȗʂ�B�X�v���C���Ȗʂ̌�?��?�߂�
     *
     * @param elmA 2���Ȗ� A
     * @param bssB B�X�v���C���Ȗ� B
     *
     * @return ��?�ƋȖʂ̌�_�̔z��
     *
     * @throws FatalException DOCUMENT ME!
     *
     * @see SurfaceSurfaceInterferenceList
     */
    private static SurfaceSurfaceInterferenceList getInterference(
        ElementarySurface3D elmA, BsplineSurface3D bssB) {
        int i;
        int j;

        // ��?̃��X�g
        SurfaceSurfaceInterferenceList interferenceList = new SurfaceSurfaceInterferenceList(elmA,
                bssB);

        if (elmA.type() == ParametricSurface3D.PLANE_3D) {
            Plane3D plnA = (Plane3D) elmA;
            int uUicp = bssB.uNControlPoints();
            int vUicp = bssB.vNControlPoints();
            int pside;

            if ((pside = plnA.pointIsWhichSide(bssB.controlPointAt(0, 0))) != WhichSide.ON) {
                boolean sameSide = true;
sideCheck: 
                for (i = 0; i < uUicp; i++)
                    for (j = 0; j < vUicp; j++) {
                        if ((i == 0) && (j == 0)) {
                            continue;
                        }

                        if (plnA.pointIsWhichSide(bssB.controlPointAt(i, j)) != pside) {
                            sameSide = false;

                            break sideCheck;
                        }
                    }

                if (sameSide) {
                    return interferenceList;
                }
            }
        }

        // �Ȗ� B ��U/V���̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsBu = bssB.uValidSegments();
        BsplineKnot.ValidSegmentInfo vldsBv = bssB.vValidSegments();

        // ��?� B ��\���x�W�G��?��
        PureBezierSurface3D[][] bzssB = bssB.toPureBezierSurfaceArray();

        SurfaceSurfaceInterference3D[] ints;
        IntersectionPoint3D intp;
        IntersectionCurve3D intc;

        // �Ȗ� B ��U���̊e�Z�O�?���g�ɑ΂���
        for (int iBu = 0; iBu < bzssB.length; iBu++) {
            // �Ȗ� B ��V���̊e�Z�O�?���g�ɑ΂���
            for (int iBv = 0; iBv < bzssB[iBu].length; iBv++) {
                if (DEBUG) {
                    bzssB[iBu][iBv].output(System.out);
                }

                // �x�W�G�Ȗʃ��x���ł̊�?𓾂�
                try {
                    ints = elmA.intersect(bzssB[iBu][iBv]);
                } catch (IndefiniteSolutionException e) {
                    throw new FatalException();
                }

                for (i = 0; i < ints.length; i++) {
                    // ��_�㊃X�g�ɒǉB���
                    if (ints[i].isIntersectionPoint()) {
                        intp = ints[i].toIntersectionPoint();
                        interferenceList.addAsIntersectionPoint(intp.coordinates(),
                            intp.pointOnSurface1().uParameter(),
                            intp.pointOnSurface1().vParameter(),
                            vldsBu.l2Gp(iBu, intp.pointOnSurface2().uParameter()),
                            vldsBv.l2Gp(iBv, intp.pointOnSurface2().vParameter()));
                    }
                    // ��?�㊃X�g�ɒǉB���
                    else { // if (ints[i].isIntersectionCurve())
                        intc = ints[i].toIntersectionCurve();
                        interferenceList.addAsIntersectionCurve((Polyline3D) intc.curve3d(),
                            (Polyline2D) intc.curve2d(),
                            l2Gc(vldsBu, iBu, vldsBv, iBv,
                                (Polyline2D) intc.curve2d2()));
                    }
                }
            }
        }

        // ���f����Ă����?��Ȃ�
        interferenceList.connectIntersectionCurves();

        return interferenceList;
    }

    /**
     * �x�W�G�Ȗʂ�B�X�v���C���Ȗʂ̌�?��?�߂�
     *
     * @param bzsA �x�W�G�Ȗ� A
     * @param bssB B�X�v���C���Ȗ� B
     *
     * @return ��?�ƋȖʂ̌�_�̔z��
     *
     * @see SurfaceSurfaceInterferenceList
     */
    private static SurfaceSurfaceInterferenceList getInterference(
        PureBezierSurface3D bzsA, BsplineSurface3D bssB) {
        int i;
        int j;

        // ��?̃��X�g
        SurfaceSurfaceInterferenceList interferenceList = new SurfaceSurfaceInterferenceList(bzsA,
                bssB);

        // �Ȗ� B ��U/V���̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsBu = bssB.uValidSegments();
        BsplineKnot.ValidSegmentInfo vldsBv = bssB.vValidSegments();

        // ��?� B ��\���x�W�G��?��
        PureBezierSurface3D[][] bzssB = bssB.toPureBezierSurfaceArray();

        SurfaceSurfaceInterference3D[] ints;
        IntersectionPoint3D intp;
        IntersectionCurve3D intc;

        // �Ȗ� B ��U���̊e�Z�O�?���g�ɑ΂���
        for (int iBu = 0; iBu < bzssB.length; iBu++) {
            // �Ȗ� B ��V���̊e�Z�O�?���g�ɑ΂���
            for (int iBv = 0; iBv < bzssB[iBu].length; iBv++) {
                if (DEBUG) {
                    bzssB[iBu][iBv].output(System.out);
                }

                // �x�W�G�Ȗʃ��x���ł̊�?𓾂�
                ints = bzsA.intersect(bzssB[iBu][iBv]);

                for (i = 0; i < ints.length; i++) {
                    // ��_�㊃X�g�ɒǉB���
                    if (ints[i].isIntersectionPoint()) {
                        intp = ints[i].toIntersectionPoint();
                        interferenceList.addAsIntersectionPoint(intp.coordinates(),
                            intp.pointOnSurface1().uParameter(),
                            intp.pointOnSurface1().vParameter(),
                            vldsBu.l2Gp(iBu, intp.pointOnSurface2().uParameter()),
                            vldsBv.l2Gp(iBv, intp.pointOnSurface2().vParameter()));
                    }
                    // ��?�㊃X�g�ɒǉB���
                    else { // if (ints[i].isIntersectionCurve())
                        intc = ints[i].toIntersectionCurve();
                        interferenceList.addAsIntersectionCurve((Polyline3D) intc.curve3d(),
                            (Polyline2D) intc.curve2d(),
                            l2Gc(vldsBu, iBu, vldsBv, iBv,
                                (Polyline2D) intc.curve2d2()));
                    }
                }
            }
        }

        // ���f����Ă����?��Ȃ�
        interferenceList.connectIntersectionCurves();

        return interferenceList;
    }

    /**
     * B�X�v���C���Ȗʓ��m�̌�?��?�߂�
     *
     * @param bssA B�X�v���C���Ȗ� A
     * @param bssB B�X�v���C���Ȗ� B
     *
     * @return ��?�ƋȖʂ̌�_�̔z��
     *
     * @see SurfaceSurfaceInterferenceList
     */
    private static SurfaceSurfaceInterferenceList getInterference(
        BsplineSurface3D bssA, BsplineSurface3D bssB) {
        int i;
        int j;

        // ��?̃��X�g
        SurfaceSurfaceInterferenceList interferenceList = new SurfaceSurfaceInterferenceList(bssA,
                bssB);

        // �Ȗ� A ��U/V���̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsAu = bssA.uValidSegments();
        BsplineKnot.ValidSegmentInfo vldsAv = bssA.vValidSegments();

        // ��?� A ��\���x�W�G��?��
        PureBezierSurface3D[][] bzssA = bssA.toPureBezierSurfaceArray();

        // �Ȗ� B ��U/V���̗L��ȃZ�O�?���g��?��
        BsplineKnot.ValidSegmentInfo vldsBu = bssB.uValidSegments();
        BsplineKnot.ValidSegmentInfo vldsBv = bssB.vValidSegments();

        // ��?� B ��\���x�W�G��?��
        PureBezierSurface3D[][] bzssB = bssB.toPureBezierSurfaceArray();

        SurfaceSurfaceInterference3D[] ints;
        IntersectionPoint3D intp;
        IntersectionCurve3D intc;

        // �Ȗ� A ��U���̊e�Z�O�?���g�ɑ΂���
        for (int iAu = 0; iAu < bzssA.length; iAu++) {
            // �Ȗ� A ��V���̊e�Z�O�?���g�ɑ΂���
            for (int iAv = 0; iAv < bzssA[iAu].length; iAv++) {
                // �Ȗ� B ��U���̊e�Z�O�?���g�ɑ΂���
                for (int iBu = 0; iBu < bzssB.length; iBu++) {
                    // �Ȗ� B ��V���̊e�Z�O�?���g�ɑ΂���
                    for (int iBv = 0; iBv < bzssB[iBu].length; iBv++) {
                        if (DEBUG) {
                            bzssA[iAu][iAv].output(System.out);
                            bzssB[iBu][iBv].output(System.out);
                        }

                        // �x�W�G�Ȗʃ��x���ł̊�?𓾂�
                        ints = bzssA[iAu][iAv].intersect(bzssB[iBu][iBv]);

                        for (i = 0; i < ints.length; i++) {
                            // ��_�㊃X�g�ɒǉB���
                            if (ints[i].isIntersectionPoint()) {
                                intp = ints[i].toIntersectionPoint();
                                interferenceList.addAsIntersectionPoint(intp.coordinates(),
                                    vldsAu.l2Gp(iAu,
                                        intp.pointOnSurface1().uParameter()),
                                    vldsAv.l2Gp(iAv,
                                        intp.pointOnSurface1().vParameter()),
                                    vldsBu.l2Gp(iBu,
                                        intp.pointOnSurface2().uParameter()),
                                    vldsBv.l2Gp(iBv,
                                        intp.pointOnSurface2().vParameter()));
                            }
                            // ��?�㊃X�g�ɒǉB���
                            else { // if (ints[i].isIntersectionCurve())
                                intc = ints[i].toIntersectionCurve();
                                interferenceList.addAsIntersectionCurve((Polyline3D) intc.curve3d(),
                                    l2Gc(vldsAu, iAu, vldsAv, iAv,
                                        (Polyline2D) intc.curve2d()),
                                    l2Gc(vldsBu, iBu, vldsBv, iBv,
                                        (Polyline2D) intc.curve2d2()));
                            }
                        }
                    }
                }
            }
        }

        // ���f����Ă����?��Ȃ�
        interferenceList.connectIntersectionCurves();

        return interferenceList;
    }

    /**
     * ��?͓I�Ȗʂ�B�X�v���C���Ȗʂ̌�?��?�߂�
     *
     * @param elmA 2���Ȗ� A
     * @param bssB B�X�v���C���Ȗ� B
     * @param doExchange DOCUMENT ME!
     *
     * @return 2�Ȗʂ̊�?�(��_�ƌ�?�)�̔z��
     *
     * @see SurfaceSurfaceInterference3D
     */
    static SurfaceSurfaceInterference3D[] intersection(
        ElementarySurface3D elmA, BsplineSurface3D bssB, boolean doExchange) {
        return getInterference(elmA, bssB)
                   .toSurfaceSurfaceInterference3DArray(doExchange);
    }

    /**
     * �x�W�G�Ȗʂ�B�X�v���C���Ȗʂ̌�?��?�߂�
     *
     * @param bzsA �x�W�G�Ȗ� A
     * @param bssB B�X�v���C���Ȗ� B
     * @param doExchange DOCUMENT ME!
     *
     * @return 2�Ȗʂ̊�?�(��_�ƌ�?�)�̔z��
     *
     * @see SurfaceSurfaceInterference3D
     */
    static SurfaceSurfaceInterference3D[] intersection(
        PureBezierSurface3D bzsA, BsplineSurface3D bssB, boolean doExchange) {
        return getInterference(bzsA, bssB)
                   .toSurfaceSurfaceInterference3DArray(doExchange);
    }

    /**
     * B�X�v���C���Ȗʓ��m�̌�?��?�߂�
     *
     * @param bssA B�X�v���C���Ȗ� A
     * @param bssB B�X�v���C���Ȗ� B
     * @param doExchange DOCUMENT ME!
     *
     * @return 2�Ȗʂ̊�?�(��_�ƌ�?�)�̔z��
     *
     * @see SurfaceSurfaceInterference3D
     */
    static SurfaceSurfaceInterference3D[] intersection(BsplineSurface3D bssA,
        BsplineSurface3D bssB, boolean doExchange) {
        return getInterference(bssA, bssB)
                   .toSurfaceSurfaceInterference3DArray(doExchange);
    }
}
// end of file
