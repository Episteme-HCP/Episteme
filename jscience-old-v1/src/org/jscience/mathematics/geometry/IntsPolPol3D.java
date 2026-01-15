/*
 * 3D�|�����C�����m�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsPolPol3D.java,v 1.3 2007-10-23 18:19:42 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

/**
 * 3D�|�����C�����m�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:42 $
 */
class IntsPolPol3D {
    /** DOCUMENT ME! */
    private Polyline3D dA;

    /** DOCUMENT ME! */
    private Vector3D[] dA_uvecs;

    /** DOCUMENT ME! */
    private double[] dA_lengs;

    /** DOCUMENT ME! */
    private EnclosingBox3D dA_enrc;

    /** DOCUMENT ME! */
    private Polyline3D dB;

    /** DOCUMENT ME! */
    private Vector3D[] dB_uvecs;

    /** DOCUMENT ME! */
    private double[] dB_lengs;

    /** DOCUMENT ME! */
    private EnclosingBox3D dB_enrc;

/**
     * Creates a new IntsPolPol3D object.
     *
     * @param dA DOCUMENT ME!
     * @param dB DOCUMENT ME!
     */
    private IntsPolPol3D(Polyline3D dA, Polyline3D dB) {
        this(dA, null, null, null, dB, null, null, null);
    }

/**
     * Creates a new IntsPolPol3D object.
     *
     * @param dA       DOCUMENT ME!
     * @param dA_uvecs DOCUMENT ME!
     * @param dA_lengs DOCUMENT ME!
     * @param dA_enrc  DOCUMENT ME!
     * @param dB       DOCUMENT ME!
     * @param dB_uvecs DOCUMENT ME!
     * @param dB_lengs DOCUMENT ME!
     * @param dB_enrc  DOCUMENT ME!
     */
    private IntsPolPol3D(Polyline3D dA, Vector3D[] dA_uvecs, double[] dA_lengs,
        EnclosingBox3D dA_enrc, Polyline3D dB, Vector3D[] dB_uvecs,
        double[] dB_lengs, EnclosingBox3D dB_enrc) {
        super();

        this.dA = dA;
        this.dA_uvecs = dA_uvecs;
        this.dA_lengs = dA_lengs;
        this.dA_enrc = dA_enrc;

        this.dB = dB;
        this.dB_uvecs = dB_uvecs;
        this.dB_lengs = dB_lengs;
        this.dB_enrc = dB_enrc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doRoughCheck DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private CurveCurveInterferenceList getInterference(boolean doRoughCheck) {
        CurveCurveInterferenceList interferenceList = new CurveCurveInterferenceList(dA,
                dB);

        if (doRoughCheck) {
            if (dA_enrc == null) {
                dA_enrc = dA.enclosingBox();
            }

            if (dB_enrc == null) {
                dB_enrc = dB.enclosingBox();
            }

            if (!dA_enrc.hasIntersection(dB_enrc)) {
                return interferenceList;
            }
        }

        int Anseg = dA.nSegments(); /* # of segments of A */
        int Bnseg = dB.nSegments(); /* # of segments of B */

        SegmentInfo Asi; /* a segment of A */
        SegmentInfo[] Bsi; /* array of segments of B */

        CurveCurveInterference3D intf;

        int i; /* loop counter */
        int j; /* loop counter */

        Bsi = new SegmentInfo[Bnseg];

        for (i = 0; i < Bnseg; i++) {
            if (dB_uvecs == null) {
                Bsi[i] = new SegmentInfo(dB.pointAt(i), dB.pointAt(i + 1));
            } else {
                Bsi[i] = new SegmentInfo(dB.pointAt(i), dB.pointAt(i + 1),
                        dB_uvecs[i], dB_lengs[i]);
            }
        }

        /*
         * for each segment of dA
         */
        for (i = 0; i < Anseg; i++) {
            if (dA_uvecs == null) {
                Asi = new SegmentInfo(dA.pointAt(i), dA.pointAt(i + 1));
            } else {
                Asi = new SegmentInfo(dA.pointAt(i), dA.pointAt(i + 1),
                        dA_uvecs[i], dA_lengs[i]);
            }

            if (Asi.tol < 0.0) {
                continue;
            }

            if ((dB_enrc != null) || doRoughCheck) {
                if (!Asi.box.hasIntersection(dB_enrc)) {
                    continue;
                }
            }

            /*
             * for each segment of dB
             */
            for (j = 0; j < Bnseg; j++) {
                if (Bsi[j].tol < 0.0) {
                    continue;
                }

                if (!Asi.box.hasIntersection(Bsi[j].box)) {
                    continue;
                }

                intf = Asi.bln.interfere1(Bsi[j].bln, Asi.udir, Bsi[j].udir,
                        Asi.leng, Bsi[j].leng);

                if (intf == null) {
                    continue;
                }

                if (intf.isIntersectionPoint()) {
                    /*
                     * intersect
                     */
                    IntersectionPoint3D ints = intf.toIntersectionPoint();
                    interferenceList.addAsIntersection(ints.coordinates(),
                        ints.pointOnCurve1().parameter() + i,
                        ints.pointOnCurve2().parameter() + j, Asi.tol,
                        Bsi[j].tol);
                } else {
                    /*
                     * overlap
                     */
                    OverlapCurve3D ovlp = intf.toOverlapCurve();
                    interferenceList.addAsOverlap(ovlp.start1() + i,
                        ovlp.start2() + j, ovlp.increase1(), ovlp.increase2(),
                        Asi.tol, Bsi[j].tol, Asi.tol, Bsi[j].tol);
                }
            }
        }

        interferenceList.removeOverlapsContainedInOtherOverlap();
        interferenceList.removeIntersectionsContainedInOverlap();

        return interferenceList;
    }

    /**
     * 2�|�����C���̌�_�𓾂�
     *
     * @param poly1 �|�����C��1
     * @param poly2 �|�����C��2
     * @param doExchange DOCUMENT ME!
     *
     * @return ��_�̔z��
     *
     * @see IntersectionPoint3D
     */
    static IntersectionPoint3D[] intersection(Polyline3D poly1,
        Polyline3D poly2, boolean doExchange) {
        IntsPolPol3D doObj = new IntsPolPol3D(poly1, poly2);

        return doObj.getInterference(true).toIntersectionPoint3DArray(doExchange);
    }

    /**
     * 2�|�����C���̊�?𓾂�
     *
     * @param poly1 �|�����C��1
     * @param poly2 �|�����C��2
     *
     * @return 2��?�̊�?̔z��
     *
     * @see CurveCurveInterference3D
     */
    static CurveCurveInterference3D[] interference(Polyline3D poly1,
        Polyline3D poly2) {
        IntsPolPol3D doObj = new IntsPolPol3D(poly1, poly2);

        return doObj.getInterference(true).toCurveCurveInterference3DArray(false);
    }

    /**
     * 2�|�����C���̊�?𓾂�(?�����)
     *
     * @param dA �|�����C��1
     * @param dA_uvecs
     *        �e�Z�O�?���g��?��K�����x�N�g��
     * @param dA_lengs �e�Z�O�?���g�̒���
     * @param dA_enrc �|�����C���̑�?ݔ͈͂���`
     * @param dB �|�����C��2
     * @param dB_uvecs
     *        �e�Z�O�?���g��?��K�����x�N�g��
     * @param dB_lengs �e�Z�O�?���g�̒���
     * @param dB_enrc �|�����C���̑�?ݔ͈͂���`
     *
     * @return 2��?�̊�?̔z��
     *
     * @see CurveCurveInterference3D
     * @see Polyline3D
     * @see Vector3D
     * @see EnclosingBox3D
     */
    static CurveCurveInterference3D[] interference(Polyline3D dA,
        Vector3D[] dA_uvecs, double[] dA_lengs, EnclosingBox3D dA_enrc,
        Polyline3D dB, Vector3D[] dB_uvecs, double[] dB_lengs,
        EnclosingBox3D dB_enrc) {
        IntsPolPol3D doObj = new IntsPolPol3D(dA, dA_uvecs, dA_lengs, dA_enrc,
                dB, dB_uvecs, dB_lengs, dB_enrc);

        return doObj.getInterference(false)
                    .toCurveCurveInterference3DArray(false);
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

            if (udir == null) {
                this.udir = epnt.subtract(spnt);
                this.leng = this.udir.length();

                if (this.leng > d_tol) {
                    this.udir = this.udir.divide(this.leng);
                } else {
                    this.udir = Vector3D.zeroVector;
                }
            } else {
                this.udir = udir;
                this.leng = leng;
            }

            if (this.leng <= d_tol) {
                this.tol = -1.0;
                bln = null;
                box = null;
            } else {
                this.tol = d_tol / this.leng;
                bln = new BoundedLine3D(spnt, epnt, GeometryElement.doCheckDebug);
                box = bln.enclosingBox();
            }
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
