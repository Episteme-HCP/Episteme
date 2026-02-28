/*
 * �_�Ԃ�B�X�v���C���Ȗʂ���ߎ�(Approximation)���邽�߂̃N���X(3D)
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ApproximationSurface3D.java,v 1.3 2007-10-21 21:08:05 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �_�Ԃ�B�X�v���C���Ȗʂ���ߎ�(Approximation)���邽�߂̃N���X(3D)
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:05 $
 */

class ApproximationSurface3D {
    protected static boolean debug = false;

    /*
    * U���̊J�t���O
    */
    private boolean uIsClosed;

    /*
    * V���̊J�t���O
    */
    private boolean vIsClosed;

    /**
     * �ߎ�����_�Ԃ� U ���̓_�̐�
     */
    private int uNPoints;

    /**
     * �ߎ�����_�Ԃ� V ���̓_�̐�
     */
    private int vNPoints;

    /**
     * �ߎ�����_�� (�v�f�� [uNPoints][vNPoints])
     *
     * @see Point3D
     */
    private Point3D[][] points;

    /**
     * U ���̊e�_�ɑΉ�����p�����[�^�l�̔z��
     */
    private double[] uParams;

    /**
     * V ���̊e�_�ɑΉ�����p�����[�^�l�̔z��
     */
    private double[] vParams;

    /**
     * ����
     */
    private static final int degree = 3;

    private static final int MARGIN = 4;

    /**
     * �_�ԁA�p�����[�^��^���ăI�u�W�F�N�g��\�z����
     *
     * @param points    �_��
     * @param uParams   U���̃p�����[�^
     * @param vParams   V���̃p�����[�^
     * @param uIsClosed U�������`�����ǂ���
     * @param vIsClosed V�������`�����ǂ���
     */
    ApproximationSurface3D(Point3D[][] points,
                           double[] uParams, double[] vParams,
                           boolean uIsClosed, boolean vIsClosed) {
        this.uIsClosed = uIsClosed;
        this.vIsClosed = vIsClosed;
        this.uNPoints = points.length;
        this.vNPoints = points[0].length;
        this.points = points;
        this.uParams = uParams;
        this.vParams = vParams;
    }

    // �ȉ��̓Z�O�����g(�m�b�g��)��^�����ċߎ�����ꍇ�ɕK�v�ȏ���
    // gh3aprxBss.c ����̈ڐA����

    /**
     * �e�_ points[i][j] �� ���̃p�����[�^�l (uParams[i], vParams[j])
     * �ɑΉ�����Ȗʏ�̓_�̋���(�c��)��Ԃ�
     *
     * @param bss �ߎ����ꂽ�Ȗ�
     * @param res �c���̔z��
     * @see BsplineSurface3D
     */
    private void compRes(BsplineSurface3D bss, double[][] res) {
        for (int i = 0; i < uNPoints; i++) {
            for (int j = 0; j < vNPoints; j++) {
                Point3D bpnt = bss.coordinates(uParams[i], vParams[j]);
                res[i][j] = points[i][j].distance(bpnt);
            }
        }
    }

    /**
     * �_�Ԃ�ߎ�����Bspline�Ȗʂ끂߂�B
     * gh3aprxCBss2 (in gh3aprxBss.c)
     * <p/>
     * �_�Ԃ�ߎ�����Bspline�Ȗʂ�ŏ�����@��p���ċ��߂�B
     * ������Ȗʂ�(u_nseg, v_nseg)�̃Z�O�����g���A
     * (u_knots, v_knots)�Ŏw�肳�ꂽ�m�b�g��B
     * </p>
     *
     * @param u_nseg  U���̃Z�O�����g�̐�
     * @param v_nseg  V���̃Z�O�����g�̐�
     * @param u_knots U���̃m�b�g��(u_nsegs+1���L��ł���A����ȍ~�͖�������)
     * @param v_knots V���̃m�b�g��(v_nsegs+1���L��ł���A����ȍ~�͖�������)
     * @return �ߎ����ꂽ Bspline�Ȗ�
     * @see BsplineSurface3D
     */
    BsplineSurface3D getApproximationWithKnots(int u_nseg, int v_nseg,
                                               double[] u_knots, double[] v_knots) {
        Approximation3D aprx;
        int i, j;

        /*
        * �܂�V���̊e���Ȑ�ŋߎ�����B
        */
        BsplineCurve3D[] v_bscs = new BsplineCurve3D[uNPoints];

        for (i = 0; i < uNPoints; i++) {
            if (v_nseg > 0) {
                aprx = new Approximation3D(points[i], vParams, null, vIsClosed);
                v_bscs[i] = aprx.getApproximationWithKnots(v_nseg, v_knots);
            } else {
                v_bscs[i] = new BsplineCurve3D(points[i], vParams, null, vIsClosed);
            }
        }

        /*
        * ���ɓ���ꂽ�Ȑ��̐���_���Ƃ�
        * U���̊e���Ȑ�ŋߎ�����B
        */
        int v_uicp = v_bscs[0].nControlPoints();
        BsplineCurve3D[] u_bscs = new BsplineCurve3D[v_uicp];
        Point3D[] aux_pnts = new Point3D[uNPoints];

        for (j = 0; j < v_uicp; j++) {
            for (i = 0; i < uNPoints; i++) {
                aux_pnts[i] = v_bscs[i].controlPointAt(j);
            }
            if (u_nseg > 0) {
                aprx = new Approximation3D(aux_pnts, uParams, null, uIsClosed);
                u_bscs[j] = aprx.getApproximationWithKnots(u_nseg, u_knots);
            } else {
                u_bscs[j] = new BsplineCurve3D(aux_pnts, uParams, null, uIsClosed);
            }
        }

        /*
        * ����ꂽ�Ȑ��̐���_���Ȗʂ̐���_�ƂȂ�B
        */
        int u_uicp = u_bscs[0].nControlPoints();

        Point3D[][] controlPoints = new Point3D[u_uicp][v_uicp];
        for (i = 0; i < u_uicp; i++) {
            for (j = 0; j < v_uicp; j++) {
                controlPoints[i][j] = u_bscs[j].controlPointAt(i);
            }
        }
        return new BsplineSurface3D(u_bscs[0].knotData(),
                v_bscs[0].knotData(),
                controlPoints, null);
    }

    // �ȉ��͋��e�덷��^�����ċߎ�����ꍇ�ɕK�v�ȏ���
    // gh3aprcBss.c �̈ڐA����

    /**
     * check zigzag conditions
     */
    private class SumOfZigZag {
        double abs_val;
        int chg_sgn;

        private SumOfZigZag(Point3D[] pnts) {
            double sum_zz = 0.0;
            int n_chng = 0;

            Vector3D bvec = pnts[1].subtract(pnts[0]);
            Vector3D tvec;
            Vector3D pcrs = null;
            Vector3D ccrs = null;
            Vector3D svec;

            int n_interval = pnts.length - 1;

            for (int i = 1; i < n_interval; i++) {
                tvec = pnts[i + 1].subtract(pnts[i]);

                if ((bvec = bvec.unitized()) != Vector3D.zeroVector) {
                    ccrs = bvec.crossProduct(tvec);
                    sum_zz += ccrs.length();
                    if (i > 1 && pcrs.dotProduct(ccrs) < 0.0)
                        n_chng++;
                }
                svec = bvec;
                bvec = tvec;
                tvec = svec;

                svec = pcrs;
                pcrs = ccrs;
                ccrs = svec;
            }

            this.abs_val = sum_zz;
            this.chg_sgn = n_chng;
        }
    }

    private class SumOfZigZagInfo {
        double val;    /* sum of zigzag */
        int idx;    /* index of polygon */

        private SumOfZigZagInfo(double val, int idx) {
            this.val = val;
            this.idx = idx;
        }

        private void sort(SumOfZigZagInfo[] data) {
            int nData = data.length;
            for (int j = 0; j < nData; j++) {
                for (int i = nData - 1; i > j; i--) {
                    if (data[i - 1].val > data[i].val) {
                        SumOfZigZagInfo tmp = data[i - 1];
                        data[i - 1] = data[i];
                        data[i] = tmp;
                    }
                }
            }
        }
    }

    private class ChngSignInfo {
        int val;    /* count of changing sign */
        int idx;    /* index of polygon */

        private ChngSignInfo(int val, int idx) {
            this.val = val;
            this.idx = idx;
        }

        private void sort(ChngSignInfo[] data) {
            int nData = data.length;
            for (int j = 0; j < nData; j++) {
                for (int i = nData - 1; i > j; i--) {
                    if (data[i - 1].val > data[i].val) {
                        ChngSignInfo tmp = data[i - 1];
                        data[i - 1] = data[i];
                        data[i] = tmp;
                    }
                }
            }
        }
    }

    private static int checkSegmentNumberRange(int nseg, int nPoints,
                                               boolean isClosed, int degree) {
        int min_nseg = Approximation.minSegmentNumber(isClosed, degree);
        if (nseg < min_nseg) nseg = min_nseg;
        if (nseg > Approximation.maxSegmentNumber(nPoints, isClosed, degree))
            nseg = -1;        // nPoints is too few
        return nseg;
    }

    private static final boolean CHECK_EVERY_POLYGONS_STEADILY = false;
    private static final int MYDEF_NMAX = 5;

    /**
     * �Z�O�����g���̏���l��Z�o����
     *
     * @param tol �ߎ��̋��e�덷
     * @return �Z�O�����g���̏���l([0]:u [1]:v)
     */
    private int[] initSegmentNumber(ToleranceForDistance tol) {
        int u_nseg;
        int v_nseg;
        int nseg;        // number of segments
        int min_nseg;
        int max_nseg;
        BsplineCurve3D bsc;    // approximated bspline curve
        ToleranceForDistance mid_tol = new ToleranceForDistance(tol.value() * 100.0);
        Point3D[] cpoints = new Point3D[uNPoints];
        int i, j;

        if (!CHECK_EVERY_POLYGONS_STEADILY) {
            double sum_zzz;            // sum of zigzag
            int chg_sgn;            // count of changing sign

            SumOfZigZagInfo[] max_sz = new SumOfZigZagInfo[MYDEF_NMAX];    // max of sum of zigzag
            ChngSignInfo[] max_cs = new ChngSignInfo[MYDEF_NMAX];    // max of changing sign
            SumOfZigZag sum;
            int idx;

            /*
            * determine the initial guess in U-dir
            */
            for (j = 0; j < MYDEF_NMAX; j++) {
                max_sz[j] = new SumOfZigZagInfo(-1.0, -1);
                max_cs[j] = new ChngSignInfo(-1, -1);
            }
            for (i = 0; i < vNPoints; i++) {
                for (j = 0; j < uNPoints; j++)
                    cpoints[j] = points[j][i];
                sum = new SumOfZigZag(cpoints);
                if (max_sz[0].val < sum.abs_val) {
                    max_sz[0].val = sum.abs_val;
                    max_sz[0].idx = i;
                    max_sz[0].sort(max_sz);
                }
                if (max_cs[0].val < sum.chg_sgn) {
                    max_cs[0].val = sum.chg_sgn;
                    max_cs[0].idx = i;
                    max_cs[0].sort(max_cs);
                }
            }

            u_nseg = 0;
            for (i = 0; i < (2 * MYDEF_NMAX); i++) {
                if (i < MYDEF_NMAX) {
                    if ((idx = max_sz[i].idx) < 0)
                        continue;
                } else {
                    if ((idx = max_cs[i - MYDEF_NMAX].idx) < 0)
                        continue;
                    for (j = 0; j < MYDEF_NMAX; j++) {
                        if (idx == max_sz[j].idx)
                            break;
                    }
                    if (j < MYDEF_NMAX)
                        continue;
                }

                for (j = 0; j < uNPoints; j++)
                    cpoints[j] = points[j][idx];

                bsc = new BsplineCurve3D(cpoints, uParams, null, uIsClosed, tol, mid_tol);
                nseg = bsc.nSegments();
                if (u_nseg < nseg)
                    u_nseg = nseg;
            }
            u_nseg = checkSegmentNumberRange(u_nseg, uNPoints, uIsClosed, degree);

            /*
            * determine the initial guess in V-dir
            */
            for (j = 0; j < MYDEF_NMAX; j++) {
                max_sz[j].val = -1.0;
                max_sz[j].idx = -1;
                max_cs[j].val = -1;
                max_cs[j].idx = -1;
            }
            for (i = 0; i < uNPoints; i++) {
                sum = new SumOfZigZag(points[i]);
                if (max_sz[0].val < sum.abs_val) {
                    max_sz[0].val = sum.abs_val;
                    max_sz[0].idx = i;
                    max_sz[0].sort(max_sz);
                }
                if (max_cs[0].val < sum.chg_sgn) {
                    max_cs[0].val = sum.chg_sgn;
                    max_cs[0].idx = i;
                    max_cs[0].sort(max_cs);
                }
            }

            v_nseg = 0;
            for (i = 0; i < (2 * MYDEF_NMAX); i++) {
                if (i < MYDEF_NMAX) {
                    if ((idx = max_sz[i].idx) < 0)
                        continue;
                } else {
                    if ((idx = max_cs[i - MYDEF_NMAX].idx) < 0)
                        continue;
                    for (j = 0; j < MYDEF_NMAX; j++) {
                        if (idx == max_sz[j].idx)
                            break;
                    }
                    if (j < MYDEF_NMAX)
                        continue;
                }

                bsc = new BsplineCurve3D(points[idx], vParams, null, vIsClosed, tol, mid_tol);
                nseg = bsc.nSegments();
                if (v_nseg < nseg)
                    v_nseg = nseg;
            }
            v_nseg = checkSegmentNumberRange(v_nseg, vNPoints, vIsClosed, degree);

            return new int[]{u_nseg, v_nseg};
        }

        /*
        * check every polygons steadily
        * (currently not used)
        */

        u_nseg = 0;
        for (i = 0; i < vNPoints; i++) {
            for (j = 0; j < uNPoints; j++)
                cpoints[j] = points[j][i];

            bsc = new BsplineCurve3D(cpoints, uParams, null, uIsClosed, tol, mid_tol);

            nseg = bsc.nSegments();
            if (u_nseg < nseg)
                u_nseg = nseg;
        }
        u_nseg = checkSegmentNumberRange(u_nseg, uNPoints, uIsClosed, degree);

        v_nseg = 0;
        for (i = 0; i < uNPoints; i++) {
            bsc = new BsplineCurve3D(points[i], vParams, null, vIsClosed, tol, mid_tol);

            nseg = bsc.nSegments();
            if (v_nseg < nseg)
                v_nseg = nseg;
        }
        v_nseg = checkSegmentNumberRange(v_nseg, vNPoints, vIsClosed, degree);

        return new int[]{u_nseg, v_nseg};
    }

    /**
     * �Z�O�����g���𑝂₷
     *
     * @param nseg   ���݂̃Z�O�����g��
     * @param n_ng   ���e�덷�𖞂����Ȃ��B��Z�O�����g�̐�
     * @param closed ���Ă��邩�ǂ���
     * @param npnt   �_�̐�
     * @return ���₵���Z�O�����g��(����ȏ㑝�₹�Ȃ��Ƃ���-1)�B
     */
    private static int incr_nseg(int nseg, int n_ng, boolean closed, int npnt) {
        if (nseg < 0)
            return -1;

        int new_nseg = nseg + n_ng;
        int max_nseg = Approximation.maxSegmentNumber(npnt, closed, degree);

        if (new_nseg >= max_nseg)
            return -1;

        return new_nseg;
    }

    /**
     * �Z�O�����g������m�b�g�����
     */
    private static void compKnots(double sp,
                                  double ep,
                                  int nseg,
                                  int nPoints,
                                  double[] k) {
        if (nseg < 0)        // ���̕��ɂ͕�Ԃ�p����̂Ńm�b�g��͕K�v�Ȃ�
            return;

        double intvl = (ep - sp) / nseg;
        int i;

        k[0] = sp;
        for (i = 1; i < nseg; i++)
            k[i] = k[i - 1] + intvl;
        k[nseg] = ep;
    }

    /**
     * �m�b�g���Čv�Z����
     */
    private static void comp_new_knots(double[] knots,
                                       int nseg,
                                       boolean[] ng_segs,
                                       int nPoints,
                                       double[] new_knots) {
        int i, j;

        new_knots[0] = knots[0];
        for (i = 0, j = 1; i < nseg; i++) {
            if (ng_segs[i]) {
                new_knots[j++] = knots[i + 1];
            } else {
                new_knots[j++] = (knots[i] + knots[i + 1]) / 2;
                new_knots[j++] = knots[i + 1];
            }
        }
    }

    /**
     * ���e�덷�𖞂��������ǂ������ׂ�
     */
    private boolean converged(int u_nseg, int v_nseg, double[] u_knots, double[] v_knots,
                              double[][] res, double tol,
                              int[] n_ng, boolean[] u_ng_segs, boolean[] v_ng_segs) {
        int i, j, k, m;

        /*
        * check u direction
        */
        n_ng[0] = 0;
        if (u_nseg > 0) {
            for (k = 0; k < u_nseg; k++)
                u_ng_segs[k] = true;

            k = 0;
            for (i = 0; i < uNPoints; i++) {
                while ((!(uParams[i] < u_knots[k + 1])) && (k < u_nseg - 1))
                    k++;
                for (j = 0; j < vNPoints; j++) {
                    if ((res[i][j] > tol) && u_ng_segs[k]) {
                        u_ng_segs[k] = false;
                        n_ng[0]++;
                    }
                }
            }
        }

        /*
        * check v direction
        */
        n_ng[1] = 0;
        if (v_nseg > 0) {
            for (k = 0; k < v_nseg; k++)
                v_ng_segs[k] = true;

            k = 0;
            for (j = 0; j < vNPoints; j++) {
                while ((!(vParams[j] < v_knots[k + 1])) && (k < v_nseg - 1))
                    k++;
                for (i = 0; i < uNPoints; i++) {
                    if ((res[i][j] > tol) && v_ng_segs[k]) {
                        v_ng_segs[k] = false;
                        n_ng[1]++;
                    }
                }
            }
        }

        return ((n_ng[0] == 0) && (n_ng[1] == 0)) ? true : false;
    }

    /**
     * �_�Ԃ��Ԃ����Ȗʂ�Ԃ�
     *
     * @return �_�Ԃ��Ԃ����Ȗ�
     */
    private BsplineSurface3D getInterpolated() {
        return new BsplineSurface3D(points, uParams, vParams, uIsClosed, vIsClosed);
    }

    /**
     * �_�Ԃ�ߎ�����o3��Bspline�Ȗʂ끂߂�B
     * gh3aprcCBss (in gh3aprcBss.c)
     * <p/>
     * �_�Ԃ�^����ꂽ���x�ŋߎ�����o3��Bspline�Ȗʂ끂߂�B
     * </p>
     *
     * @param tol �ߎ�����o3���a�X�v���C���Ȗʂ̐��x�B
     *            ���������Ȗʂ͗^����ꂽ�_�Ԃɑ΂��Ă��̋��e�덷�ȓ�Ő��������B
     * @return �ߎ����ꂽ Bspline�Ȗ�
     * @see BsplineSurface3D
     * @see ToleranceForDistance
     */
    BsplineSurface3D getApproximationWithTolerance(ToleranceForDistance tol) {
        int[] nsegs;
        double[] u_cnt_knots = new double[uNPoints + MARGIN];
        double[] u_new_knots = new double[uNPoints + MARGIN];
        double[] v_cnt_knots = new double[vNPoints + MARGIN];
        double[] v_new_knots = new double[vNPoints + MARGIN];
        double[][] res = new double[uNPoints][vNPoints];
        int[] n_ng = new int[2];
        boolean[] u_ng_segs = new boolean[uNPoints + MARGIN];
        boolean[] v_ng_segs = new boolean[vNPoints + MARGIN];

        double[] tmp_p;
        int u_new_nseg, v_new_nseg;
        BsplineSurface3D bss;

        /*
        * �Z�O�����g���̏���l�숂߂�
        */
        nsegs = initSegmentNumber(tol);

        if (nsegs[0] < 0 && nsegs[1] < 0)    // �����ɓ_��������Ȃ�
            return getInterpolated();        // ��Ԃ����Ȗʂ�Ԃ�

        /*
        * ���߂��Z�O�����g�����珉��m�b�g��끂߂�
        */
        double ep;
        if (!uIsClosed)
            ep = uParams[uNPoints - 1];
        else
            ep = uParams[uNPoints];
        compKnots(uParams[0], ep, nsegs[0], uNPoints, u_cnt_knots);

        if (!vIsClosed)
            ep = vParams[vNPoints - 1];
        else
            ep = vParams[vNPoints];
        compKnots(vParams[0], ep, nsegs[1], vNPoints, v_cnt_knots);

        /*
        * �ȍ~�A�ߎ������Ȗʂ����x�𖞂����܂�
        * �Z�O�����g���𑝂₵�Ȃ���ċߎ��݂�B
        */

        while (true) {
            // ���݂̃Z�O�����g���ŋߎ��Ȗʂ끂߂�
            bss = getApproximationWithKnots(nsegs[0], nsegs[1], u_cnt_knots, v_cnt_knots);
            if (debug) {
                bss.output(System.out);
            }

            // �c����v�Z����
            compRes(bss, res);

            // ���e�덷�𖞂�������?
            if (converged(nsegs[0], nsegs[1], u_cnt_knots, v_cnt_knots, res, tol.value(),
                    n_ng, u_ng_segs, v_ng_segs)) {
                return bss;    // �ߎ��Ȗʂ�ԋp����
            }

            // U���̃Z�O�����g���𑝂₵�Ă݂�
            u_new_nseg = incr_nseg(nsegs[0], n_ng[0], uIsClosed, uNPoints);
            if (u_new_nseg > 0) {
                comp_new_knots(u_cnt_knots, nsegs[0],
                        u_ng_segs, uNPoints, u_new_knots);
                nsegs[0] = u_new_nseg;
                tmp_p = u_cnt_knots;
                u_cnt_knots = u_new_knots;
                u_new_knots = tmp_p;
            }

            // V���̃Z�O�����g���𑝂₵�Ă݂�
            v_new_nseg = incr_nseg(nsegs[1], n_ng[1], vIsClosed, vNPoints);
            if (v_new_nseg > 0) {
                comp_new_knots(v_cnt_knots, nsegs[1],
                        v_ng_segs, vNPoints, v_new_knots);
                nsegs[1] = v_new_nseg;
                tmp_p = v_cnt_knots;
                v_cnt_knots = v_new_knots;
                v_new_knots = tmp_p;
            }

            // ����ȏ�Z�O�����g���𑝂₹�Ȃ�
            if (u_new_nseg < 0 && v_new_nseg < 0)
                break;
        }

        // �����ɗ����Ƃ������Ƃ͋ߎ��Ȗʂ����x�𖞂����Ȃ��B��Ƃ������ƂȂ̂�
        // �_�Ԃ��Ԃ����Ȗʂ�ԋp����B
        return getInterpolated();
    }

    /**
     * �f�o�b�O�p���C���v���O�����B
     */
    public static void main(String argv[]) {
        ToleranceForDistance tol = new ToleranceForDistance(0.1);

        System.out.println("Main: [creating ApproximationSurface3D.]");

        // for closed case
        if (true) {
            CartesianPoint3D p00 = new CartesianPoint3D(0.0, 0.0, 0.0);
            CartesianPoint3D p01 = new CartesianPoint3D(0.4, 0.6, 0.0);
            CartesianPoint3D p02 = new CartesianPoint3D(1.0, 1.0, 0.0);
            CartesianPoint3D p03 = new CartesianPoint3D(1.6, 0.6, 0.0);
            CartesianPoint3D p04 = new CartesianPoint3D(2.0, 0.0, 0.0);
            CartesianPoint3D p05 = new CartesianPoint3D(1.6, -0.6, 0.0);
            CartesianPoint3D p06 = new CartesianPoint3D(1.0, -1.0, 0.0);
            CartesianPoint3D p07 = new CartesianPoint3D(0.4, -0.6, 0.0);

            CartesianPoint3D p10 = new CartesianPoint3D(0.0, 0.0, 1.0);
            CartesianPoint3D p11 = new CartesianPoint3D(0.4, 0.6, 1.0);
            CartesianPoint3D p12 = new CartesianPoint3D(1.0, 1.0, 1.0);
            CartesianPoint3D p13 = new CartesianPoint3D(1.6, 0.6, 1.0);
            CartesianPoint3D p14 = new CartesianPoint3D(2.0, 0.0, 1.0);
            CartesianPoint3D p15 = new CartesianPoint3D(1.6, -0.6, 1.0);
            CartesianPoint3D p16 = new CartesianPoint3D(1.0, -1.0, 1.0);
            CartesianPoint3D p17 = new CartesianPoint3D(0.4, -0.6, 1.0);

            CartesianPoint3D p20 = new CartesianPoint3D(0.0, 0.0, 2.0);
            CartesianPoint3D p21 = new CartesianPoint3D(0.4, 0.6, 2.0);
            CartesianPoint3D p22 = new CartesianPoint3D(1.0, 1.0, 2.0);
            CartesianPoint3D p23 = new CartesianPoint3D(1.6, 0.6, 2.0);
            CartesianPoint3D p24 = new CartesianPoint3D(2.0, 0.0, 2.0);
            CartesianPoint3D p25 = new CartesianPoint3D(1.6, -0.6, 2.0);
            CartesianPoint3D p26 = new CartesianPoint3D(1.0, -1.0, 2.0);
            CartesianPoint3D p27 = new CartesianPoint3D(0.4, -0.6, 2.0);

            CartesianPoint3D p30 = new CartesianPoint3D(0.0, 0.0, 3.0);
            CartesianPoint3D p31 = new CartesianPoint3D(0.4, 0.6, 3.0);
            CartesianPoint3D p32 = new CartesianPoint3D(1.0, 1.0, 3.0);
            CartesianPoint3D p33 = new CartesianPoint3D(1.6, 0.6, 3.0);
            CartesianPoint3D p34 = new CartesianPoint3D(2.0, 0.0, 3.0);
            CartesianPoint3D p35 = new CartesianPoint3D(1.6, -0.6, 3.0);
            CartesianPoint3D p36 = new CartesianPoint3D(1.0, -1.0, 3.0);
            CartesianPoint3D p37 = new CartesianPoint3D(0.4, -0.6, 3.0);

            CartesianPoint3D[][] pntsClosed = {{p00, p01, p02, p03, p04, p05, p06, p07},
                    {p10, p11, p12, p13, p14, p15, p16, p17},
                    {p20, p21, p22, p23, p24, p25, p26, p27},
                    {p30, p31, p32, p33, p34, p35, p36, p37}};
            double[] uPrmsClosed = {0.0, 1.0, 2.0, 3.0};
            double[] vPrmsClosed = {0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0};
            ApproximationSurface3D aprxClosed =
                    new ApproximationSurface3D(pntsClosed, uPrmsClosed, vPrmsClosed, false, true);
            System.out.println("Main: [creating BsplineSurface3D.]");
            BsplineSurface3D bsplineClosed = aprxClosed.getApproximationWithTolerance(tol);
            System.out.println("\nMain: [ApproximationSurface3D Closed Test]");
            bsplineClosed.output(System.out);
        }

        // for open case
        if (true) {
            CartesianPoint3D p00 = new CartesianPoint3D(0.0, 0.0, 0.0);
            CartesianPoint3D p01 = new CartesianPoint3D(0.4, 0.2, 0.0);
            CartesianPoint3D p02 = new CartesianPoint3D(1.0, 0.3, 0.0);
            CartesianPoint3D p03 = new CartesianPoint3D(1.6, 0.25, 0.0);
            CartesianPoint3D p04 = new CartesianPoint3D(2.0, 0.2, 0.0);
            CartesianPoint3D p05 = new CartesianPoint3D(2.4, 0.25, 0.0);
            CartesianPoint3D p06 = new CartesianPoint3D(3.0, 0.3, 0.0);
            CartesianPoint3D p07 = new CartesianPoint3D(3.6, 0.25, 0.0);
            CartesianPoint3D p08 = new CartesianPoint3D(4.0, 0.2, 0.0);

            CartesianPoint3D p10 = new CartesianPoint3D(0.0, 0.0, 1.0);
            CartesianPoint3D p11 = new CartesianPoint3D(0.4, 0.2, 1.0);
            CartesianPoint3D p12 = new CartesianPoint3D(1.0, 0.3, 1.0);
            CartesianPoint3D p13 = new CartesianPoint3D(1.6, 0.25, 1.0);
            CartesianPoint3D p14 = new CartesianPoint3D(2.0, 0.2, 1.0);
            CartesianPoint3D p15 = new CartesianPoint3D(2.4, 0.25, 1.0);
            CartesianPoint3D p16 = new CartesianPoint3D(3.0, 0.3, 1.0);
            CartesianPoint3D p17 = new CartesianPoint3D(3.6, 0.25, 1.0);
            CartesianPoint3D p18 = new CartesianPoint3D(4.0, 0.2, 1.0);

            CartesianPoint3D p20 = new CartesianPoint3D(0.0, 0.0, 2.0);
            CartesianPoint3D p21 = new CartesianPoint3D(0.4, 0.2, 2.0);
            CartesianPoint3D p22 = new CartesianPoint3D(1.0, 0.3, 2.0);
            CartesianPoint3D p23 = new CartesianPoint3D(1.6, 0.25, 2.0);
            CartesianPoint3D p24 = new CartesianPoint3D(2.0, 0.2, 2.0);
            CartesianPoint3D p25 = new CartesianPoint3D(2.4, 0.25, 2.0);
            CartesianPoint3D p26 = new CartesianPoint3D(3.0, 0.3, 2.0);
            CartesianPoint3D p27 = new CartesianPoint3D(3.6, 0.25, 2.0);
            CartesianPoint3D p28 = new CartesianPoint3D(4.0, 0.2, 2.0);

            CartesianPoint3D p30 = new CartesianPoint3D(0.0, 0.0, 3.0);
            CartesianPoint3D p31 = new CartesianPoint3D(0.4, 0.2, 3.0);
            CartesianPoint3D p32 = new CartesianPoint3D(1.0, 0.3, 3.0);
            CartesianPoint3D p33 = new CartesianPoint3D(1.6, 0.25, 3.0);
            CartesianPoint3D p34 = new CartesianPoint3D(2.0, 0.2, 3.0);
            CartesianPoint3D p35 = new CartesianPoint3D(2.4, 0.25, 3.0);
            CartesianPoint3D p36 = new CartesianPoint3D(3.0, 0.3, 3.0);
            CartesianPoint3D p37 = new CartesianPoint3D(3.6, 0.25, 3.0);
            CartesianPoint3D p38 = new CartesianPoint3D(4.0, 0.2, 3.0);

            CartesianPoint3D p40 = new CartesianPoint3D(0.0, 0.0, 4.0);
            CartesianPoint3D p41 = new CartesianPoint3D(0.4, 0.2, 4.0);
            CartesianPoint3D p42 = new CartesianPoint3D(1.0, 0.3, 4.0);
            CartesianPoint3D p43 = new CartesianPoint3D(1.6, 0.25, 4.0);
            CartesianPoint3D p44 = new CartesianPoint3D(2.0, 0.2, 4.0);
            CartesianPoint3D p45 = new CartesianPoint3D(2.4, 0.25, 4.0);
            CartesianPoint3D p46 = new CartesianPoint3D(3.0, 0.3, 4.0);
            CartesianPoint3D p47 = new CartesianPoint3D(3.6, 0.25, 4.0);
            CartesianPoint3D p48 = new CartesianPoint3D(4.0, 0.2, 4.0);

            CartesianPoint3D[][] pntsOpen = {{p00, p01, p02, p03, p04, p05, p06, p07, p08},
                    {p10, p11, p12, p13, p14, p15, p16, p17, p18},
                    {p20, p21, p22, p23, p24, p25, p26, p27, p28},
                    {p30, p31, p32, p33, p34, p35, p36, p37, p38},
                    {p40, p41, p42, p43, p44, p45, p46, p47, p48}};
            double[] uPrmsOpen = {0.0, 1.0, 2.0, 3.0, 4.0};
            double[] vPrmsOpen = {0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0};
            ApproximationSurface3D aprxOpen =
                    new ApproximationSurface3D(pntsOpen, uPrmsOpen, vPrmsOpen, false, false);
            System.out.println("\n\nMain: [creating Open BsplineSurface3D.]");
            BsplineSurface3D bsplineOpen = aprxOpen.getApproximationWithTolerance(tol);
            System.out.println("\nMain: [ApproximationSurface3D Open Test]");
            bsplineOpen.output(System.out);
        }
    }
}

// end of file
