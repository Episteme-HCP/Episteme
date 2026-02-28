/*
 * 3D�x�W�G��?�ƃx�W�G�Ȗʂ̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsBzcBzs3D.java,v 1.2 2007-10-21 17:38:24 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.ArrayMathUtils;
import org.episteme.mathematics.analysis.PrimitiveMappingND;
import org.episteme.util.FatalException;

/**
 * �R����?F�x�W�G��?�ƃx�W�G�Ȗʂ̌�_��?�߂�N���X
 * <p/>
 * �x�W�G��?�͒�?�Ƃ݂Ȃ���܂ŕ�����?A
 * �x�W�G�Ȗʂ͕��ʂƂ݂Ȃ���܂ŕ�����?A��?�ƕ��ʂ̌�?���ɋA��������?B
 * ��?� x ���ʂŋ?�߂���_��?A���Z��?���l�Ƃ���?A
 * ?�?I�I�ɂ͎��Z�ɂ�B�?A?��������?�߂�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:24 $
 */

final class IntsBzcBzs3D {
    /**
     * �x�W�G��?�
     */
    PureBezierCurve3D dA;

    /**
     * �x�W�G�Ȗ�
     */
    PureBezierSurface3D dB;

    /**
     * �񕪖�
     */
    BinaryTree aTree;

    /**
     * �l����
     */
    QuadTree bTree;

    /**
     * ��_��ێ?���邽�߂̃��X�g?\��
     */
    CurveSurfaceInterferenceList solutions;

    /**
     * �����̌�?�
     */
    double dTol;

    /**
     * �����̌�?���2?�
     */
    double dTol2;

    /**
     * �p���??[�^�̌�?�
     */
    double pTol;

    /**
     * ��_�̋�?�?�̓_
     */
    Point3D sApnt;

    /**
     * ��_�̋Ȗ�?�̓_
     */
    Point3D sBpnt;

    /**
     * ��_�̋�?�?�̌X��
     */
    Vector3D aTang;

    /**
     * ��_�̋Ȗ�?�̌X��
     */
    Vector3D[] bTang;

    /**
     * �x�W�G�Ȗʂ�?�Ԃ�\�����߂̒�?�
     */
    private static final int UNKNOWN = 0;
    private static final int BEZIER = 1;
    private static final int LINE = 2;
    private static final int POINT = 3;
    private static final int PLANER = 4;

    /**
     * �x�W�G��?�ƃx�W�G�Ȗʂ�^���鎖�ɂ�BČ����?�߂邽�߂�
     * �I�u�W�F�N�g��?\�z����?B
     *
     * @param bzc �x�W�G��?�
     * @param bzs �x�W�G�Ȗ�
     */
    IntsBzcBzs3D(PureBezierCurve3D bzc,
                 PureBezierSurface3D bzs) {
        //
        // �t�B?[���h�Ȃǂ�?���?ݒ�
        //
        super();

        this.dA = bzc;
        this.dB = bzs;
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        dTol2 = cond.getToleranceForDistance2();
        dTol = cond.getToleranceForDistance();
        pTol = cond.getToleranceForParameter();

        //
        // �x�W�G��?��񕪖�?A�x�W�G�Ȗʂ�l���؂�?ݒ肷��?B
        //
        aTree = new BinaryTree(new BezierCurveInfo(dA, 0.0, 1.0, false));
        BezierSurfaceInfo dBRoot = new BezierSurfaceInfo(dB, dB, 0.0, 1.0, 0.0, 1.0, true);
        dBRoot.rivals.addElement(aTree.rootNode());
        bTree = new QuadTree(dBRoot);

        //
        // ���ێ?���Ă������߂�?�?���?�����?B
        //
        solutions = new CurveSurfaceInterferenceList(bzc, bzs);
    }

    /**
     * �x�W�G��?��?�Ƃ݂Ȃ��Ă����Ƃ���?��赂����߂̓Ք�N���X
     */
    class BezierCurveInfo {
        private PureBezierCurve3D bzc;
        private double sp;
        private double ep;
        private EnclosingBox3D box;
        private ObjectVector rivals;
        private int currentType;
        private GeometryElement geom;

        /**
         * �x�W�G��?�?A�J�n�p���??[�^?A?I���p���??[�^�Ȃǂ�?���p����?A
         * �I�u�W�F�N�g��?\�z����?B
         */
        BezierCurveInfo(PureBezierCurve3D bzc, double sp, double ep, boolean hasRival) {
            this.bzc = bzc;
            this.sp = sp;
            this.ep = ep;
            this.box = bzc.approximateEnclosingBox();

            if (hasRival)
                this.rivals = new ObjectVector();
            else
                this.rivals = null;

            this.currentType = UNKNOWN;
            this.geom = null;
        }

        private Point3D pnt() {
            return (Point3D) geom;
        }

        /**
         * �x�W�G��?�ǂ̂悤�Ȍ`?�ɂ݂Ȃ��邩�𒲂ׂ�?B
         *
         * @return �x�W�G��?�ǂ̂悤�Ȍ`?󂩂�\�����߂�?�?�
         */
        private int whatTypeIsBezierCurve() {
            if (currentType != UNKNOWN)
                return currentType;

            int uicp = bzc.nControlPoints();

            Vector3D s2e;
            double leng_s2e;
            Vector3D unit_s2e;
            Vector3D s2c;
            Vector3D crsv;
            double leng;
            int i;

            s2e = bzc.controlPointAt(uicp - 1).subtract(bzc.controlPointAt(0));
            leng_s2e = s2e.length();

            if (leng_s2e < dTol) {
                for (i = 1; i < (uicp - 1); i++) {
                    s2c = bzc.controlPointAt(i).subtract(bzc.controlPointAt(0));
                    if (!(s2c.length() < dTol))
                        break;
                }

                if (i == (uicp - 1)) {
                    /*
                    * AbstractPoint
                    */
                    Point3D pnt_geom
                            = bzc.controlPointAt(uicp - 1).linearInterpolate(bzc.controlPointAt(0), 0.5);

                    geom = pnt_geom;
                    return currentType = POINT;
                } else {
                    /*
                    * Bezier
                    */
                    geom = null;
                    return currentType = BEZIER;
                }
            }

            unit_s2e = s2e.divide(leng_s2e);

            for (i = 1; i < (uicp - 1); i++) {
                s2c = bzc.controlPointAt(i).subtract(bzc.controlPointAt(0));
                crsv = unit_s2e.crossProduct(s2c);
                if (crsv.length() > dTol) {
                    /*
                    * Bezier
                    */
                    geom = null;
                    return currentType = BEZIER;
                }

                leng = unit_s2e.dotProduct(s2c);
                if ((leng < (0.0 - dTol)) || (leng > (leng_s2e + dTol))) {
                    /*
                    * Bezier
                    */
                    geom = null;
                    return currentType = BEZIER;
                }
            }

            /*
            * Line
            */
            Line3D lin_geom = new Line3D(bzc.controlPointAt(0), s2e);
            geom = lin_geom;
            return currentType = LINE;
        }
    }

    /**
     * �x�W�G�Ȗʂ𕽖ʂƂ݂Ȃ��Ă����Ƃ���?��赂����߂̓Ք�N���X
     */
    class BezierSurfaceInfo {
        private PureBezierSurface3D bzs;
        private PureBezierSurface3D root;
        private double usp;
        private double uep;
        private double vsp;
        private double vep;
        private EnclosingBox3D box;
        private ObjectVector rivals;
        private PlaneBezier pb;
        private int currentType;

        /**
         * ?e�̃x�W�G�Ȗ�?A�x�W�G�Ȗ�?Au,v �J�n�p���??[�^?A
         * u,v ?I���p���??[�^�Ȃǂ�?���p����?A�I�u�W�F�N�g��?\�z����?B
         */
        BezierSurfaceInfo(PureBezierSurface3D root,
                          PureBezierSurface3D bzs,
                          double usp, double uep,
                          double vsp, double vep, boolean hasRivals) {
            this.root = root;
            this.bzs = bzs;
            this.usp = usp;
            this.uep = uep;
            this.vsp = vsp;
            this.vep = vep;
            this.box = bzs.approximateEnclosingBox();

            if (hasRivals)
                this.rivals = new ObjectVector();
            else
                this.rivals = null;
            this.currentType = UNKNOWN;
            this.pb = new PlaneBezier(bzs);
        }

        /**
         * �x�W�G�Ȗʂ��ǂ̂悤�Ȍ`?�ɂ݂Ȃ��邩�𒲂ׂ�?B
         *
         * @return �x�W�G�Ȗʂ��ǂ̂悤�Ȍ`?󂩂�\�����߂�?�?�
         */
        int whatTypeIsBezierSurface() {
            if (currentType != UNKNOWN)
                return currentType;

            int u_uicp = bzs.uNControlPoints();
            int v_uicp = bzs.vNControlPoints();
            PlaneBezier pb;
            Vector3D evec;

            currentType = BEZIER;

            /*
            * make_refplane can change the way of making a plane with
            * bi's parameter rectangle, but it is a little dangerous
            * with freeform VS. freeform.
            */
            this.pb = pb = new PlaneBezier(bzs);
            Point3D org = pb.origin();
            Vector3D zaxis = pb.zaxis();
            Vector3D[] xyz = pb.axis.axes();

            /*
            * just return if Bezier is not planar
            */
            for (int j = 0; j < v_uicp; j++)
                for (int i = 0; i < u_uicp; i++) {
                    evec = bzs.controlPointAt(i, j).subtract(org);
                    if (Math.abs(evec.dotProduct(zaxis)) > dTol)
                        return currentType;
                }

            /*
            * Bezier is planar, so make the PureBezierCurve3D
            */
            currentType = POINT;

            /*
            * boundary curves
            */
            for (int i = 0; i < 4; i++) {
                int uicp = ((i % 2) == 0) ? u_uicp : v_uicp;
                if (pb.shape_info[i] == 0) {
                    pb.bcrv[i] = null;
                    continue;
                }

                Point3D[] pnts = new Point3D[uicp];
                Point2D[] pnt2d = new Point2D[uicp];
                double[] ws = null;
                if (bzs.isRational())
                    ws = new double[uicp];
                for (int j = 0; j < uicp; j++) {
                    switch (i) {
                        case 0:
                            pnts[j] = bzs.controlPointAt(j, 0);
                            if (bzs.isRational())
                                ws[j] = bzs.weightAt(j, 0);
                            break;
                        case 1:
                            pnts[j] = bzs.controlPointAt(u_uicp - 1, j);
                            if (bzs.isRational())
                                ws[j] = bzs.weightAt(u_uicp - 1, j);
                            break;
                        case 2:
                            pnts[j] = bzs.controlPointAt(u_uicp - 1 - j, v_uicp - 1);
                            if (bzs.isRational())
                                ws[j] = bzs.weightAt(u_uicp - 1 - j, v_uicp - 1);
                            break;
                        case 3:
                            pnts[j] = bzs.controlPointAt(0, v_uicp - 1 - j);
                            if (bzs.isRational())
                                ws[j] = bzs.weightAt(0, v_uicp - 1 - j);
                            break;
                    }
                    evec = pnts[j].subtract(org);
                    pnt2d[j] = new CartesianPoint2D
                            (evec.dotProduct(xyz[0]), evec.dotProduct(xyz[1]));
                }

                pb.bcrv[i] = new PureBezierCurve2D(pnt2d, ws, GeometryElement.doCheckDebug);
                Vector2D s2e = pnt2d[uicp - 1].subtract(pnt2d[0]).unitized();
                int j;
                for (j = 1; j < (uicp - 1); j++) {
                    Vector2D evec2 = pnt2d[j].subtract(pnt2d[0]);
                    double edot = evec2.dotProduct(s2e);
                    if (Math.abs(evec2.norm() - (edot * edot)) > dTol2)
                        break;
                }
                pb.bcrv_is_line[i] = (j == (uicp - 1)) ? true : false;

                if (pb.bcrv_is_line[i]) {
                    currentType = LINE;
                } else {
                    currentType = PLANER;
                }
            }
            return currentType;
        }

        /**
         * �?�߂�ꂽ�_������?��?�BĂ��邩�ǂ����𒲂ׂ�?B
         *
         * @param ����?�ɂ��邩�ǂ����𒲂ׂ�_
         * @return �_������?�ɂ��邩�ǂ�����?^�U�l
         */
        private boolean isPointInPlane(Point3D point) {
            Vector3D evec = point.subtract(pb.origin());
            Point2D point2d = new CartesianPoint2D(evec.dotProduct(pb.axis.x()),
                    evec.dotProduct(pb.axis.y()));
            Vector2D dir = new LiteralVector2D(0.70710678, 0.70710678);
            Line2D line2d = new Line2D(point2d, dir);

            /*
            * saved_ipl_list check is needed
            */
            ObjectVector saved_ipl_list = new ObjectVector();
            double saved_ipl = 0;

            int icnt = 0;
            for (int i = 0; i < 4; i++) {
                if (pb.bcrv[i] == null)
                    continue;
                IntersectionPoint2D[] intp;
                try {
                    intp = line2d.intersect(pb.bcrv[i]);
                } catch (IndefiniteSolutionException e) {
                    throw new FatalException();
                } catch (FatalException e) { // add temporaly
                    continue;
                }
                if (intp.length <= 0)
                    continue;

                for (int j = 0; j < intp.length; j++) {
                    double ipl = intp[j].pointOnCurve1().parameter();
                    if (ipl > -dTol) {
                        if (ipl < dTol) {
                            /*
                            * line's parameter = 0.0 : is_in = TRUE
                            */
                            return true;
                        }

                        /*
                        * �ۑ������p���??[�^��?V�����p���??[�^���r����?A�������B���
                        * ���̃p���??[�^�͒ǉB��Ȃ�
                        */
                        int k;
                        for (k = 0; k < saved_ipl_list.size(); k++) {
                            saved_ipl = ((Double) saved_ipl_list.elementAt(k)).doubleValue();
                            if (Math.abs(ipl - saved_ipl) < dTol)
                                break;
                        }
                        if (k == saved_ipl_list.size()) {
                            saved_ipl_list.addElement(new Double(ipl));
                            icnt++;
                        }
                    }
                }
            }
            if (!((icnt % 2) != 0))
                return false;
            return true;
        }
    }

    /**
     * �ߎ��Ȗʂ�\���Ք�N���X
     */
    final class PlaneBezier {
        /**
         * �ߎ����ʂ�\����?�?W�n
         */
        Axis2Placement3D axis;

        /*
        * boundary curves
        * (0 : u = 0), (1 : v = 1), (2 : u = 1), (3 : v = 0)
        */
        PureBezierCurve2D[] bcrv;

        /*
        * flags for whether each of boundaries is linear or not
        */
        boolean[] bcrv_is_line;

        /**
         * ?k�ނ��Ă��Ȃ��ӂ�?�
         */
        int edge_cnt;

        /**
         * shape_info[i] = 1 if i-th edge is not reduced
         */
        int[] shape_info;

        /**
         * �x�W�G�Ȗʂ�^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param �x�W�G�Ȗ�
         */
        PlaneBezier(PureBezierSurface3D bzs) {
            int u_uicp = bzs.uNControlPoints();
            int v_uicp = bzs.vNControlPoints();

            Point3D c00, c10, c01, c11;
            double u0norm, v0norm, u1norm, v1norm;
            Vector3D u0dir, v0dir, u1dir, v1dir;    /* vectors which connect corners */
            int iu0dir, iv0dir, iu1dir, iv1dir;
            int retrying = 0;
            Vector3D udir = null;
            Vector3D vdir = null;

            shape_info = new int[4];

            bcrv_is_line = new boolean[4];
            bcrv = new PureBezierCurve2D[4];

            /*
            * make 4 vectors which connect 4 corners
            *
            *	         u1dir
            *	    +<--------+
            *	    |         ^
            *	    |         |
            *	    |v0dir    |v1dir
            *	  ^ |         |
            *	  | v         |
            *	 v| +-------->+
            *	    -->  u0dir
            *	    u
            */
            c00 = bzs.controlPointAt(0, 0);
            c10 = bzs.controlPointAt(u_uicp - 1, 0);
            c01 = bzs.controlPointAt(0, v_uicp - 1);
            c11 = bzs.controlPointAt(u_uicp - 1, v_uicp - 1);

            u0dir = c10.subtract(c00);
            v1dir = c11.subtract(c10);
            u1dir = c01.subtract(c11);
            v0dir = c00.subtract(c01);

            /*
            * select 2 vectors which are not reduced
            */
            RETRY_IF_BALLOON:
            do {
                u0norm = u0dir.norm();
                iu0dir = (u0norm > dTol2) ? 1 : 0;
                v0norm = v0dir.norm();
                iv0dir = (v0norm > dTol2) ? 1 : 0;
                u1norm = u1dir.norm();
                iu1dir = (u1norm > dTol2) ? 1 : 0;
                v1norm = v1dir.norm();
                iv1dir = (v1norm > dTol2) ? 1 : 0;
                edge_cnt = iu0dir + iv0dir + iu1dir + iv1dir;
                shape_info[0] = iu0dir;
                shape_info[3] = iv0dir;
                shape_info[2] = iu1dir;
                shape_info[1] = iv1dir;

                switch (edge_cnt) {
                    case 4:        /* rectangular (has 4 edges) */
                        udir = u0dir;
                        vdir = v0dir;
                        break;

                    case 3:        /* triangular (has 3 edges) */
                        if (iu0dir == 0) {
                            udir = v1dir.multiply(-1);
                            vdir = v0dir;
                        } else if (iv0dir == 0) {
                            udir = u0dir;
                            vdir = u1dir.multiply(-1);
                        } else {
                            udir = u0dir;
                            vdir = v0dir;
                        }
                        break;

                    case 2:        /* football shape (has 2 edges) */
                        udir = vdir = null;
                        if (iu0dir == 1) {
                            udir = bzs.controlPointAt(1, 0)
                                    .subtract(bzs.controlPointAt(0, 0));
                        }
                        if (iv0dir == 1) {
                            if (udir == null) {
                                udir = bzs.controlPointAt(0, 1)
                                        .subtract(bzs.controlPointAt(0, 0));
                            } else {
                                vdir = bzs.controlPointAt(0, 1)
                                        .subtract(bzs.controlPointAt(0, 0));
                                break;
                            }
                        }
                        if (iu1dir == 1) {
                            if (udir == null) {
                                udir = bzs.controlPointAt(u_uicp - 2, v_uicp - 1)
                                        .subtract(bzs.controlPointAt(u_uicp - 1, v_uicp - 1));

                            } else {
                                if (iu0dir == 1)
                                    vdir = bzs.controlPointAt(1, v_uicp - 1)
                                            .subtract(bzs.controlPointAt(0, v_uicp - 1));
                                else
                                    vdir = bzs.controlPointAt(u_uicp - 2, v_uicp - 1)
                                            .subtract(bzs.controlPointAt(u_uicp - 1, v_uicp - 1));
                                break;
                            }
                        } else {    /* if (iv1dir == 1) */
                            if (iv0dir == 1)
                                vdir = bzs.controlPointAt(u_uicp - 1, 1)
                                        .subtract(bzs.controlPointAt(u_uicp - 1, 0));
                            else
                                vdir = bzs.controlPointAt(u_uicp - 1, v_uicp - 2)
                                        .subtract(bzs.controlPointAt(u_uicp - 1, v_uicp - 1));
                        }
                        break;

                    default:        /* has 1 edge */
                    case 0:        /* balloon shape (has no edge) */
                        if (retrying == 1)
                            return;
                        retrying = 1;

                        /*
                        * make 4 vectors from neighbour points at 4 corners
                        */
                        u0dir = bzs.controlPointAt(1, 0)
                                .subtract(bzs.controlPointAt(0, 0));
                        v0dir = bzs.controlPointAt(0, 1)
                                .subtract(bzs.controlPointAt(0, 0));
                        u1dir = bzs.controlPointAt(u_uicp - 2, v_uicp - 1)
                                .subtract(bzs.controlPointAt(u_uicp - 1, v_uicp - 1));
                        v1dir = bzs.controlPointAt(u_uicp - 1, v_uicp - 2)
                                .subtract(bzs.controlPointAt(u_uicp - 1, v_uicp - 1));
                        continue RETRY_IF_BALLOON;
                }
                break RETRY_IF_BALLOON;
            } while (true);

            udir = udir.unitized();
            vdir = vdir.unitized();
            axis = new Axis2Placement3D(c00, udir.crossProduct(vdir), udir);
        }

        /**
         * ��?�?W�n�̌��_��Ԃ�
         *
         * @return ��?�?W�n�̌��_
         */
        Point3D origin() {
            return axis.location();
        }

        /**
         * ��?�?W�n��z����Ԃ�
         *
         * @return ��?�?W�n��z��
         */
        Vector3D zaxis() {
            return axis.z();
        }
    }

    /**
     * ��_��?���\���Ք�N���X
     */
    final class PointInfo {
        /**
         * 3D coordinates
         */
        Point3D pnt;

        /**
         * parameter of curve A
         */
        double aParam;

        /**
         * U parameter of surface B
         */
        double bUParam;

        /**
         * V parameter of surface B
         */
        double bVParam;

        /**
         * �_�Ƌ�?�?�̃p���??[�^?A�Ȗ�?��u, v�p���??[�^��^����
         * �I�u�W�F�N�g��?\�z����?B
         */
        PointInfo(Point3D pnt, double aParam, double bUParam, double bVParam) {
            this.pnt = pnt;
            this.aParam = aParam;
            this.bUParam = bUParam;
            this.bVParam = bVParam;
        }
    }

    /**********************************************************************
     *
     * Definitions of private function
     *
     **********************************************************************/

    /**
     * ��?�ɑ΂���?����ȕ��ʂ�?�� (make_norm_plane)
     *
     * @param line ��?�
     * @return �^����ꂽ��?�ɑ΂���?����ȕ���
     */
    private Plane3D makeNormPlane(Line3D line) {
        Point3D org = line.pnt();
        Vector3D z = line.dir();
        Vector3D x = z.verticalVector();

        return new Plane3D(new Axis2Placement3D(org, z, x));
    }

    /**
     * ����̂̊e���_��ϊ����� (translate_box)
     *
     * @param boxMin ?�?��_
     * @param boxMax ?ő�_
     * @param plane  ����
     * @return ���_��?W?���\�� EnclosingBox
     */
    private EnclosingBox3D translateBox(Point3D boxMin, Point3D boxMax, Plane3D plane) {
        Point3D[] boxPoints = new Point3D[8];

        boxPoints[0] = new CartesianPoint3D(boxMin.x(), boxMin.y(), boxMin.z());
        boxPoints[1] = new CartesianPoint3D(boxMax.x(), boxMin.y(), boxMin.z());
        boxPoints[2] = new CartesianPoint3D(boxMax.x(), boxMax.y(), boxMin.z());
        boxPoints[3] = new CartesianPoint3D(boxMin.x(), boxMax.y(), boxMin.z());
        boxPoints[4] = new CartesianPoint3D(boxMin.x(), boxMin.y(), boxMax.z());
        boxPoints[5] = new CartesianPoint3D(boxMax.x(), boxMin.y(), boxMax.z());
        boxPoints[6] = new CartesianPoint3D(boxMax.x(), boxMax.y(), boxMax.z());
        boxPoints[7] = new CartesianPoint3D(boxMin.x(), boxMax.y(), boxMax.z());

        Axis2Placement3D position = plane.position();
        CartesianTransformationOperator3D transformer =
                new CartesianTransformationOperator3D(position, 1.0);

        boxPoints[0] = transformer.toLocal(boxPoints[0]);
        Point3D tBoxMin = boxPoints[0];
        Point3D tBoxMax = boxPoints[0];

        for (int i = 0; i < 8; i++) {
            boxPoints[i] = transformer.toLocal(boxPoints[i]);
            tBoxMin = new CartesianPoint3D(Math.min(boxPoints[i].x(), tBoxMin.x()),
                    Math.min(boxPoints[i].y(), tBoxMin.y()),
                    Math.min(boxPoints[i].z(), tBoxMin.z()));
            tBoxMax = new CartesianPoint3D(Math.max(boxPoints[i].x(), tBoxMin.x()),
                    Math.max(boxPoints[i].y(), tBoxMin.y()),
                    Math.max(boxPoints[i].z(), tBoxMin.z()));
        }
        return new EnclosingBox3D(tBoxMin, tBoxMax);
    }

    /**
     * ��?����邩�ǂ����̌�?��ⷂ�(check_interfer)
     *
     * @param bci �x�W�G��?�?��
     * @param bsi �x�W�G�Ȗ�?��
     */
    private boolean checkInterfere(BezierCurveInfo bci, BezierSurfaceInfo bsi) {
        /*
        * if curve is already linear and surface is still Bezier
        * special consideration will be applied
        */
        if ((bci.currentType == LINE) && (bsi.pb != null)) {
            Plane3D plane = makeNormPlane((Line3D) bci.geom);
            EnclosingBox3D box = translateBox(bsi.box.min(), bsi.box.max(), plane);
            return ((box.min().x() < dTol) &&
                    (box.min().y() < dTol) &&
                    (box.max().x() > (-dTol)) &&
                    (box.max().y() > (-dTol)))
                    ? false  // interfere
                    : true;  // no interfere
        }

        /*
        * if surface is already planer and curve is still Bezier,
        * special consideration will be applied.
        */
        int pside = 0;
        int cside = 0;
        int j;

        if ((bsi.pb == null) && ((bci.currentType == UNKNOWN) || bci.currentType == BEZIER)) {
            for (j = 0; j < bci.bzc.nControlPoints(); j++) {
                Plane3D pl = new Plane3D(bsi.pb.origin(), bsi.pb.zaxis());
                cside = pl.pointIsWhichSide(bci.bzc.controlPointAt(j));
                if (j == 0) {
                    pside = cside;
                } else {
                    if (pside != cside) {
                        return false;
                    }
                }
            }
            return true;
        }

        return bci.box.min().x() > (bsi.box.max().x() + dTol) ||
                (bci.box.min().y() > (bsi.box.max().y() + dTol)) ||
                (bci.box.min().z() > (bsi.box.max().z() + dTol)) ||
                (bsi.box.min().x() > (bci.box.max().x() + dTol)) ||
                (bsi.box.min().y() > (bci.box.max().y() + dTol)) ||
                (bsi.box.min().z() > (bci.box.max().z() + dTol))
                ? false // interfere
                : true;  // no interfere
    }

    /**
     * ���C�o���𕪊�����(divide_rivals)
     *
     * @param dANode     �񕪖؂̃m?[�h
     * @param new_rivals �������ꂽ���C�o��?��
     * @return ��?�Ƃ݂Ȃ��邩�ǂ����̃t���O
     */
    private boolean divideRivals(BinaryTree.Node dANode, ObjectVector new_rivals) {

        BinaryTree.Node binL, binR;

        if (dANode.left() == null && dANode.right() == null) {
            BezierCurveInfo bi = (BezierCurveInfo) dANode.data();

            if (bi.whatTypeIsBezierCurve() != BEZIER) {
                new_rivals.addElement(dANode);
                return false;
            }

            //
            //subdivide rival
            //
            double harf_point = 0.5;
            PureBezierCurve3D[] bzcs = bi.bzc.divide(harf_point);
            double g_harf_point = (bi.sp + bi.ep) / 2.0;

            BezierCurveInfo biL = new BezierCurveInfo(bzcs[0], bi.sp, g_harf_point, false);
            binL = dANode.makeLeft(biL);

            BezierCurveInfo biR = new BezierCurveInfo(bzcs[1], g_harf_point, bi.ep, false);
            binR = dANode.makeRight(biR);
        } else {
            binL = dANode.left();
            binR = dANode.right();
        }
        new_rivals.addElement(binL);
        new_rivals.addElement(binR);

        return true;
    }

    /**
     * �������ꂽ��?�ƋȖʂ̌�_��?�߂�(intersect_linpln)
     *
     * @param bci �x�W�G��?�?��
     * @param bsi �x�W�G�Ȗ�?��
     */
    private void intersectLinePlane(BezierCurveInfo bci, BezierSurfaceInfo bsi) {
        Line3D line = (Line3D) bci.geom;
        Plane3D plane = new Plane3D(bsi.pb.axis);
        CartesianTransformationOperator3D transform = new
                CartesianTransformationOperator3D(plane.position(), 1.0);

        // Transform
        Point3D eApnt = transform.toLocal(line.pnt());
        Vector3D eAdir = transform.toLocal(line.dir());
        double et = (-eApnt.z() / eAdir.z());

        // solution
        Point3D dCpnt = line.pnt().add(line.dir().multiply(et));

        // point is out of line
        if ((et < -pTol) || ((1.0 + pTol) < et)) {
//	    throw new FatalException(); // ???
            return;
        }

        // point is out of plane
        if (bsi.isPointInPlane(dCpnt) != true) {
//	    throw new FatalException(); // ???
            return;
        }

        double cParam = ((1.0 - et) * bci.sp) + (et * bci.ep);
        double suParam = (bsi.usp + bsi.uep) / 2.0;
        double svParam = (bsi.vsp + bsi.vep) / 2.0;

        PointInfo pi = new PointInfo(dCpnt, cParam, suParam, svParam);
        if (refinePointInfo(pi))
            solutions.addAsIntersection(pi.pnt, pi.aParam, pi.bUParam, pi.bVParam);
        return;
    }

    /**
     * nl_func
     */
    private class nlFunc implements PrimitiveMappingND {
        private nlFunc() {
            super();
        }

        public double[] map(int x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public double[] map(long x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public double[] map(float x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        /**
         * The dimension of variable parameter. Should be a strictly positive integer.
         */
        public int numInputDimensions() {
            return 3;
        }

        /**
         * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
         */
        public int numOutputDimensions() {
            return 3;
        }

        public double[] map(double[] parameter) {
            Vector3D evec = sApnt.subtract(sBpnt);
            double[] vec = new double[3];
            vec[0] = evec.x();
            vec[1] = evec.y();
            vec[2] = evec.z();

            return vec;
        }
    }

    /**
     * dnl_func
     */
    private class dnlFunc1 implements PrimitiveMappingND {
        private dnlFunc1() {
            super();
        }

        public double[] map(int x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public double[] map(long x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public double[] map(float x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        /**
         * The dimension of variable parameter. Should be a strictly positive integer.
         */
        public int numInputDimensions() {
            return 3;
        }

        /**
         * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
         */
        public int numOutputDimensions() {
            return 3;
        }

        public double[] map(double[] parameter) {
            aTang = dA.tangentVector(dA.parameterDomain().force(parameter[0]));
            bTang = dB.tangentVector(dB.uParameterDomain().force(parameter[1]),
                    dB.vParameterDomain().force(parameter[2]));
            double mtrx[] = new double[3];
            mtrx[0] = aTang.x();
            mtrx[1] = -bTang[0].x();
            mtrx[2] = -bTang[1].x();
            return mtrx;
        }
    }

    /**
     * dnl_func
     */
    private class dnlFunc2 implements PrimitiveMappingND {
        private dnlFunc2() {
            super();
        }

        public double[] map(int x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public double[] map(long x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public double[] map(float x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        /**
         * The dimension of variable parameter. Should be a strictly positive integer.
         */
        public int numInputDimensions() {
            return 3;
        }

        /**
         * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
         */
        public int numOutputDimensions() {
            return 3;
        }

        public double[] map(double[] parameter) {
            double mtrx[] = new double[3];
            mtrx[0] = aTang.y();
            mtrx[1] = -bTang[0].y();
            mtrx[2] = -bTang[1].y();
            return mtrx;
        }
    }

    /**
     * dnl_func
     */
    private class dnlFunc3 implements PrimitiveMappingND {
        private dnlFunc3() {
            super();
        }

        public double[] map(int x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public double[] map(long x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public double[] map(float x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        /**
         * The dimension of variable parameter. Should be a strictly positive integer.
         */
        public int numInputDimensions() {
            return 3;
        }

        /**
         * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
         */
        public int numOutputDimensions() {
            return 3;
        }

        public double[] map(double[] parameter) {
            double mtrx[] = new double[3];
            mtrx[0] = aTang.z();
            mtrx[1] = -bTang[0].z();
            mtrx[2] = -bTang[1].z();
            return mtrx;
        }
    }

    /**
     * cnv_func
     */
    private class cnvFunc implements PrimitiveBooleanMappingNDTo1D {
        private cnvFunc() {
            super();
        }

        public boolean map(int x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public boolean map(long x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        public boolean map(float x[]) {
            return map(ArrayMathUtils.toDouble(x));
        }

        /**
         * The dimension of variable parameter. Should be a strictly positive integer.
         */
        public int numInputDimensions() {
            return 3;
        }

        public boolean map(double[] parameter) {
            sApnt = dA.coordinates(dA.parameterDomain().force(parameter[0]));
            sBpnt = dB.coordinates(dB.uParameterDomain().force(parameter[1]),
                    dB.vParameterDomain().force(parameter[2]));

            return sApnt.identical(sBpnt);
        }
    }

    /**
     * setback_params
     */
    private void setbackParams(PointInfo pi, double[] param) {
        Point3D aPnt, bPnt;

        pi.aParam = dA.parameterDomain().force(param[0]);
        pi.bUParam = dB.uParameterDomain().force(param[1]);
        pi.bVParam = dB.vParameterDomain().force(param[2]);

        aPnt = dA.coordinates(pi.aParam);
        bPnt = dB.coordinates(pi.bUParam, pi.bVParam);

        pi.pnt = aPnt.linearInterpolate(bPnt, 0.5);
    }

    /**
     * ���?����?s��(refine_pointinfo)
     *
     * @param pi �_?��
     */
    boolean refinePointInfo(PointInfo pi) {
        double[] param = new double[3];
        param[0] = pi.aParam;
        param[1] = pi.bUParam;
        param[2] = pi.bVParam;
        nlFunc nl_func = new nlFunc();
        PrimitiveMappingND[] dnl_func = new PrimitiveMappingND[3];
        dnl_func[0] = new dnlFunc1();
        dnl_func[1] = new dnlFunc2();
        dnl_func[2] = new dnlFunc3();
        cnvFunc cnv_func = new cnvFunc();

        param = GeometryUtils.solveSimultaneousEquations(nl_func, dnl_func,
                cnv_func, param);
        if (param == null)
            return false;

        setbackParams(pi, param);
        return true;
    }

    /**
     * ��_�𓾂�(get_intersection)
     *
     * @param crnt_bi �x�W�G�Ȗ�?��
     * @param level   �K�w?��
     */
    void getIntersections(QuadTree.Node crnt_node) {
        PureBezierSurface3D bzs00, bzs01, bzs10, bzs11;
        BezierSurfaceInfo bi00, bi01, bi10, bi11;
        QuadTree.Node bin00, bin01, bin10, bin11;
        int i;
        BinaryTree.Node dANode = null;
        QuadTree.Node dBNode = null;
        BezierSurfaceInfo crnt_bi = (BezierSurfaceInfo) crnt_node.data();
        /*
        * is there some interferes ?
        */
        int n_rivals = crnt_bi.rivals.size();
        Cursor cursor;

        if (crnt_bi.bzs == dB) {
            for (cursor = crnt_bi.rivals.cursor(); cursor.hasMoreElements();) {
                dANode = (BinaryTree.Node) cursor.nextElement();
                if (!checkInterfere((BezierCurveInfo) dANode.data(), crnt_bi))
                    cursor.removePrevElement();
            }
            if (crnt_bi.rivals.size() == 0)
                return;
        }

        /*
        * if current bezier is regarded as rectangular, get intersection.
        */
        if (crnt_bi.whatTypeIsBezierSurface() != BEZIER) {
            ObjectVector new_rivals = new ObjectVector();
            boolean all_rivals_are_line = true;

            n_rivals = crnt_bi.rivals.size();
            for (i = 0; i < n_rivals; i++)
                if (divideRivals((BinaryTree.Node) crnt_bi.rivals.elementAt(i),
                        new_rivals))
                    all_rivals_are_line = false;

            crnt_bi.rivals = new_rivals;

            if (!all_rivals_are_line) {
                /*
                * try again
                */
                getIntersections(crnt_node);
            } else {
                /*
                * get intersections
                */
                n_rivals = crnt_bi.rivals.size();
                for (i = 0; i < n_rivals; i++) {
                    dANode = (BinaryTree.Node) crnt_bi.rivals.elementAt(i);
                    intersectLinePlane((BezierCurveInfo) dANode.data(), crnt_bi);
                }
            }
            return;
        }

        /*
        * if current bezier is NOT regarded as rectangular,
        * generate children (divide current bezier).
        */
        double ug_half = (crnt_bi.usp + crnt_bi.uep) / 2.0;
        double vg_half = (crnt_bi.vsp + crnt_bi.vep) / 2.0;
        double half_point = 0.5;

        PureBezierSurface3D[] bzsx = crnt_bi.bzs.vDivide(half_point);
        PureBezierSurface3D[] bzs0 = bzsx[0].uDivide(half_point);
        PureBezierSurface3D[] bzs1 = bzsx[1].uDivide(half_point);

        if (((crnt_bi.uep - crnt_bi.usp) < 0.75) || ((crnt_bi.vep - crnt_bi.vsp) < 0.75)) {
            crnt_bi.bzs = null;
        }

        bi00 = new BezierSurfaceInfo(crnt_bi.root, bzs0[0], crnt_bi.usp, ug_half,
                crnt_bi.vsp, vg_half, true);
        bi10 = new BezierSurfaceInfo(crnt_bi.root, bzs1[0], crnt_bi.uep, ug_half,
                vg_half, crnt_bi.vep, true);
        bi11 = new BezierSurfaceInfo(crnt_bi.root, bzs1[1], ug_half, crnt_bi.uep,
                vg_half, crnt_bi.vep, true);
        bi01 = new BezierSurfaceInfo(crnt_bi.root, bzs0[1], ug_half, crnt_bi.uep,
                crnt_bi.vsp, vg_half, true);

        /*
        * create children's rival list
        */
        n_rivals = crnt_bi.rivals.size();
        for (i = 0; i < n_rivals; i++)
            divideRivals((BinaryTree.Node) crnt_bi.rivals.elementAt(i), bi00.rivals);

        n_rivals = bi00.rivals.size();
        for (i = 0; i < n_rivals; i++) {
            bi01.rivals.addElement(bi00.rivals.elementAt(i));
            bi11.rivals.addElement(bi00.rivals.elementAt(i));
            bi10.rivals.addElement(bi00.rivals.elementAt(i));
        }

        /*
        * recursive call
        */
        // bi00
        for (cursor = bi00.rivals.cursor(); cursor.hasMoreElements();) {
            dANode = (BinaryTree.Node) cursor.nextElement();
            if (!checkInterfere((BezierCurveInfo) dANode.data(), crnt_bi))
                cursor.removePrevElement();
        }
        if (bi00.rivals.size() != 0) {
            bin00 = crnt_node.makeChild(0, bi00);
            getIntersections(bin00);
        }

        // bi01
        for (cursor = bi01.rivals.cursor(); cursor.hasMoreElements();) {
            dANode = (BinaryTree.Node) cursor.nextElement();
            if (!checkInterfere((BezierCurveInfo) dANode.data(), crnt_bi))
                cursor.removePrevElement();
        }
        if (bi01.rivals.size() != 0) {
            bin01 = crnt_node.makeChild(1, bi01);
            getIntersections(bin01);
        }

        // bi10
        for (cursor = bi10.rivals.cursor(); cursor.hasMoreElements();) {
            dANode = (BinaryTree.Node) cursor.nextElement();
            if (!checkInterfere((BezierCurveInfo) dANode.data(), crnt_bi))
                cursor.removePrevElement();
        }
        if (bi10.rivals.size() != 0) {
            bin10 = crnt_node.makeChild(2, bi10);
            getIntersections(bin10);
        }

        // bi11
        for (cursor = bi11.rivals.cursor(); cursor.hasMoreElements();) {
            dANode = (BinaryTree.Node) cursor.nextElement();
            if (!checkInterfere((BezierCurveInfo) dANode.data(), crnt_bi))
                cursor.removePrevElement();
        }
        if (bi11.rivals.size() != 0) {
            bin11 = crnt_node.makeChild(3, bi11);
            getIntersections(bin11);
        }
    }

    /*********************************************************************
     *
     * Body (defined as external, since this is called from gh3intsBssBss.c)
     *
     **********************************************************************/

    /**
     * �x�W�G��?�ƃx�W�G�Ȗʂ̌�_�𓾂�
     *
     * @param dA       Bezier Curve   A
     * @param dB       Bezier Surface B
     * @param ��_�̔z��
     */
    CurveSurfaceInterferenceList intsBzcBzs() {
        BezierCurveInfo dARoot = (BezierCurveInfo) aTree.rootNode().data();
        QuadTree.Node dBRootNode = bTree.rootNode();
        BezierSurfaceInfo dBRoot = (BezierSurfaceInfo) dBRootNode.data();

        if ((dARoot.box.min().x() > (dBRoot.box.max().x() + dTol)) ||
                (dARoot.box.min().y() > (dBRoot.box.max().y() + dTol)) ||
                (dARoot.box.min().z() > (dBRoot.box.max().z() + dTol)) ||
                (dBRoot.box.min().x() > (dARoot.box.max().x() + dTol)) ||
                (dBRoot.box.min().y() > (dARoot.box.max().y() + dTol)) ||
                (dBRoot.box.min().z() > (dARoot.box.max().z() + dTol)))
            return solutions; /* no interfere */

        getIntersections(dBRootNode);

        // Make Results
        return solutions;

    }

    /**
     * �x�W�G��?�ƃx�W�G�Ȗʂ̌�_�𓾂�
     *
     * @param bzc      �x�W�G��?�
     * @param bzs      �x�W�G�Ȗ�
     * @param ��_�̔z��
     */
    static IntersectionPoint3D[] intersection(PureBezierCurve3D bzc,
                                              PureBezierSurface3D bzs,
                                              boolean doExchange) {
        IntsBzcBzs3D doObj = new IntsBzcBzs3D(bzc, bzs);
        return doObj.intsBzcBzs().toIntersectionPoint3DArray(doExchange);
    }
}

/* end of file */
