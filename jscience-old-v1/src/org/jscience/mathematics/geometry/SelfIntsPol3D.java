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

/**
 * 3D�|�����C���̎��Ȋ�?�?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:45 $
 */
class SelfIntsPol3D {
    /** DOCUMENT ME! */
    private Polyline3D polyline;

    /** DOCUMENT ME! */
    private Vector3D[] polylineUvecs;

    /** DOCUMENT ME! */
    private double[] polylineLengs;

    /** DOCUMENT ME! */
    private EnclosingBox3D polylineEncls;

/**
     * Creates a new SelfIntsPol3D object.
     *
     * @param polyline DOCUMENT ME!
     */
    private SelfIntsPol3D(Polyline3D polyline) {
        this(polyline, null, null, null);
    }

/**
     * Creates a new SelfIntsPol3D object.
     *
     * @param polyline      DOCUMENT ME!
     * @param polylineUvecs DOCUMENT ME!
     * @param polylineLengs DOCUMENT ME!
     * @param polylineEncls DOCUMENT ME!
     */
    private SelfIntsPol3D(Polyline3D polyline, Vector3D[] polylineUvecs,
        double[] polylineLengs, EnclosingBox3D polylineEncls) {
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

        CurveCurveInterference3D intf;

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

                    IntersectionPoint3D ints = intf.toIntersectionPoint();
                    interferenceList.addAsIntersection(ints.coordinates(),
                        ints.pointOnCurve1().parameter() + i,
                        ints.pointOnCurve2().parameter() + j,
                        segmentInfo[i].tol, segmentInfo[j].tol);
                } else {
                    /*
                     * overlap
                     */
                    OverlapCurve3D ovlp = intf.toOverlapCurve();
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
     * @see IntersectionPoint3D
     */
    static IntersectionPoint3D[] intersection(Polyline3D polyline) {
        SelfIntsPol3D doObj = new SelfIntsPol3D(polyline);

        return doObj.getInterference().toIntersectionPoint3DArray(false);
    }

    /**
     * �|�����C���̎��Ȋ�?𓾂�
     *
     * @param polyline �|�����C��
     *
     * @return ��?̔z��
     *
     * @see CurveCurveInterference3D
     */
    static CurveCurveInterference3D[] interference(Polyline3D polyline) {
        SelfIntsPol3D doObj = new SelfIntsPol3D(polyline);

        return doObj.getInterference().toCurveCurveInterference3DArray(false);
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
     * @see CurveCurveInterference3D
     * @see Polyline3D
     * @see Vector3D
     * @see EnclosingBox3D
     */
    static CurveCurveInterference3D[] interference(Polyline3D polyline,
        Vector3D[] polylineUvecs, double[] polylineLengs,
        EnclosingBox3D polylineEncls) {
        SelfIntsPol3D doObj = new SelfIntsPol3D(polyline, polylineUvecs,
                polylineLengs, polylineEncls);

        return doObj.getInterference().toCurveCurveInterference3DArray(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class SegmentInfo {
        /** DOCUMENT ME! */
        private BoundedLine3D bln; /* a segment */

        /** DOCUMENT ME! */
        private Vector3D udir; /* unitized vector */

        /** DOCUMENT ME! */
        private double leng; /* length of segment */

        /** DOCUMENT ME! */
        private double tol; /* tolerance */

        /** DOCUMENT ME! */
        private EnclosingBox3D box; /* enclosing box */

/**
         * Creates a new SegmentInfo object.
         *
         * @param spnt DOCUMENT ME!
         * @param epnt DOCUMENT ME!
         * @param udir DOCUMENT ME!
         * @param leng DOCUMENT ME!
         */
        private SegmentInfo(Point3D spnt, Point3D epnt, Vector3D udir,
            double leng) {
            super();

            double d_tol = spnt.getToleranceForDistance();

            bln = new BoundedLine3D(spnt, epnt);

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
        private SegmentInfo(Point3D spnt, Point3D epnt) {
            this(spnt, epnt, null, 0.0);
        }
    }
}
