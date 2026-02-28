/*
 * 2D�|�����C���̎��Ȋ�?�?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SelfIntsPol2D.java,v 1.3 2007-10-23 18:19:45 virtualcall Exp $
 */
package org.episteme.mathematics.geometry;

/**
 * 2D�|�����C���̎��Ȋ�?�?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:45 $
 */
class SelfIntsPol2D {
    /** DOCUMENT ME! */
    private Polyline2D polyline;

    /** DOCUMENT ME! */
    private Vector2D[] polylineUvecs;

    /** DOCUMENT ME! */
    private double[] polylineLengs;

    /** DOCUMENT ME! */
    private EnclosingBox2D polylineEncls;

/**
     * Creates a new SelfIntsPol2D object.
     *
     * @param polyline DOCUMENT ME!
     */
    private SelfIntsPol2D(Polyline2D polyline) {
        this(polyline, null, null, null);
    }

/**
     * Creates a new SelfIntsPol2D object.
     *
     * @param polyline      DOCUMENT ME!
     * @param polylineUvecs DOCUMENT ME!
     * @param polylineLengs DOCUMENT ME!
     * @param polylineEncls DOCUMENT ME!
     */
    private SelfIntsPol2D(Polyline2D polyline, Vector2D[] polylineUvecs,
        double[] polylineLengs, EnclosingBox2D polylineEncls) {
        super();

        this.polyline = polyline;
        this.polylineUvecs = polylineUvecs;
        this.polylineLengs = polylineLengs;
        this.polylineEncls = polylineEncls;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private CurveCurveInterferenceList getInterference() {
        CurveCurveInterferenceList interferenceList = new CurveCurveInterferenceList(polyline,
                polyline);

        int nSegments = polyline.nSegments(); /* # of segments */

        if (nSegments < 3) {
            return interferenceList;
        }

        SegmentInfo[] segmentInfo; /* array of segments */

        CurveCurveInterference2D intf;

        int i; /* loop counter */
        int j; /* loop counter */

        segmentInfo = new SegmentInfo[nSegments];

        for (i = 0; i < nSegments; i++) {
            if (polylineUvecs == null) {
                segmentInfo[i] = new SegmentInfo(polyline.pointAt(i),
                        polyline.pointAt(i + 1));
            } else {
                segmentInfo[i] = new SegmentInfo(polyline.pointAt(i),
                        polyline.pointAt(i + 1), polylineUvecs[i],
                        polylineLengs[i]);
            }
        }

        for (i = 0; i < nSegments; i++) {
            if (segmentInfo[i].tol < 0.0) {
                continue;
            }

            for (j = i + 1; j < nSegments; j++) {
                if (segmentInfo[j].tol < 0.0) {
                    continue;
                }

                if (!segmentInfo[i].box.hasIntersection(segmentInfo[j].box)) {
                    continue;
                }

                intf = segmentInfo[i].bln.interfere1(segmentInfo[j].bln,
                        segmentInfo[i].udir, segmentInfo[j].udir,
                        segmentInfo[i].leng, segmentInfo[j].leng);

                if (intf == null) {
                    continue;
                }

                if (intf.isIntersectionPoint()) {
                    /*
                     * intersect
                     */
                    if (((i + 1) == j) ||
                            ((polyline.isPeriodic() == true) && (i == 0) &&
                            (j == (nSegments - 1)))) {
                        continue;
                    }

                    IntersectionPoint2D ints = intf.toIntersectionPoint();
                    interferenceList.addAsIntersection(ints.coordinates(),
                        ints.pointOnCurve1().parameter() + i,
                        ints.pointOnCurve2().parameter() + j,
                        segmentInfo[i].tol, segmentInfo[j].tol);
                } else {
                    /*
                     * overlap
                     */
                    OverlapCurve2D ovlp = intf.toOverlapCurve();
                    interferenceList.addAsOverlap(ovlp.start1() + i,
                        ovlp.start2() + j, ovlp.increase1(), ovlp.increase2(),
                        segmentInfo[i].tol, segmentInfo[j].tol,
                        segmentInfo[i].tol, segmentInfo[j].tol);
                }
            }
        }

        interferenceList.removeOverlapsContainedInOtherOverlap();
        interferenceList.removeIntersectionsContainedInOverlap();

        return interferenceList;
    }

    /**
     * �|�����C���̎��Ȍ�?��𓾂�
     *
     * @param polyline �|�����C��
     *
     * @return ��_�̔z��
     *
     * @see IntersectionPoint2D
     */
    static IntersectionPoint2D[] intersection(Polyline2D polyline) {
        SelfIntsPol2D doObj = new SelfIntsPol2D(polyline);

        return doObj.getInterference().toIntersectionPoint2DArray(false);
    }

    /**
     * �|�����C���̎��Ȋ�?𓾂�
     *
     * @param polyline �|�����C��
     *
     * @return ��?̔z��
     *
     * @see CurveCurveInterference2D
     */
    static CurveCurveInterference2D[] interference(Polyline2D polyline) {
        SelfIntsPol2D doObj = new SelfIntsPol2D(polyline);

        return doObj.getInterference().toCurveCurveInterference2DArray(false);
    }

    /**
     * �|�����C���̎��Ȋ�?𓾂�(?�����)
     *
     * @param polyline �|�����C��
     * @param polylineUvecs
     *        �e�Z�O�?���g��?��K�����x�N�g��
     * @param polylineLengs �e�Z�O�?���g�̒���
     * @param polylineEncls
     *        �|�����C���̑�?ݔ͈͂���`
     *
     * @return ��?̔z��
     *
     * @see CurveCurveInterference2D
     * @see Polyline2D
     * @see Vector2D
     * @see EnclosingBox2D
     */
    static CurveCurveInterference2D[] interference(Polyline2D polyline,
        Vector2D[] polylineUvecs, double[] polylineLengs,
        EnclosingBox2D polylineEncls) {
        SelfIntsPol2D doObj = new SelfIntsPol2D(polyline, polylineUvecs,
                polylineLengs, polylineEncls);

        return doObj.getInterference().toCurveCurveInterference2DArray(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class SegmentInfo {
        /** DOCUMENT ME! */
        private BoundedLine2D bln; /* a segment */

        /** DOCUMENT ME! */
        private Vector2D udir; /* unitized vector */

        /** DOCUMENT ME! */
        private double leng; /* length of segment */

        /** DOCUMENT ME! */
        private double tol; /* tolerance */

        /** DOCUMENT ME! */
        private EnclosingBox2D box; /* enclosing box */

/**
         * Creates a new SegmentInfo object.
         *
         * @param spnt DOCUMENT ME!
         * @param epnt DOCUMENT ME!
         * @param udir DOCUMENT ME!
         * @param leng DOCUMENT ME!
         */
        private SegmentInfo(Point2D spnt, Point2D epnt, Vector2D udir,
            double leng) {
            super();

            double d_tol = spnt.getToleranceForDistance();

            bln = new BoundedLine2D(spnt, epnt);

            if (udir == null) {
                this.udir = bln.unitizedDirection();
                this.leng = bln.length();
            } else {
                this.udir = udir;
                this.leng = leng;
            }

            if (this.leng < d_tol) {
                this.tol = -1.0;
            } else {
                this.tol = d_tol / this.leng;
            }

            box = bln.enclosingBox();
        }

/**
         * Creates a new SegmentInfo object.
         *
         * @param spnt DOCUMENT ME!
         * @param epnt DOCUMENT ME!
         */
        private SegmentInfo(Point2D spnt, Point2D epnt) {
            this(spnt, epnt, null, 0.0);
        }
    }
}
