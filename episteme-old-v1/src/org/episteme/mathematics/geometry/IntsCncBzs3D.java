/*
 * 3D�~??��?�ƃx�W�G�Ȗʂ̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsCncBzs3D.java,v 1.2 2007-10-21 17:38:24 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.ArrayMathUtils;
import org.episteme.mathematics.analysis.PrimitiveMappingND;
import org.episteme.util.FatalException;

import java.util.Vector;

/**
 * �R����?F�~??��?�ƃx�W�G�Ȗʂ̌�_��?�߂�N���X
 * <p/>
 * �x�W�G�Ȗʂ͕��ʂƂ݂Ȃ���܂ŕ�����?A
 * ��?�܂��͉~??��?�ƕ��ʂ̌�?���ɋA��������?B
 * ��?�?A�~??��?� x ���ʂŋ?�߂���_��?A���Z��?���l�Ƃ���?A
 * ?�?I�I�ɂ͎��Z�ɂ�B�?A?��������?�߂�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:24 $
 */
final class IntsCncBzs3D {
    /**
     * ��?�(��?�~??��?�̂ǂ��炩)
     */
    ParametricCurve3D dA;

    /**
     * �x�W�G�Ȗ�
     */
    PureBezierSurface3D dB;

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
     * �ϊ����ꂽ�x�W�G�Ȗ�
     */
    PureBezierSurface3D bzsL;

    /**
     * �ϊ���
     */
    CartesianTransformationOperator3D transform;

    private static final int UNKNOWN = 0;
    private static final int BEZIER = 1;
    private static final int LINE = 2;
    private static final int POINT = 3;
    private static final int PLANER = 4;

    /**
     * �~??��?�ƃx�W�G�Ȗʂ�^���鎖�ɂ�BČ����?�߂邽�߂�
     * �I�u�W�F�N�g��?\�z����?B
     *
     * @param curve �~??��?�
     * @param bzs   �x�W�G�Ȗ�
     */
    IntsCncBzs3D(ParametricCurve3D curve,
                 PureBezierSurface3D bzs) {
        //
        // �t�B?[���h�Ȃǂ�?���?ݒ�
        //
        super();

        this.dA = curve;
        this.dB = bzs;
        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        dTol2 = cond.getToleranceForDistance2();
        dTol = cond.getToleranceForDistance();
        pTol = cond.getToleranceForParameter();

        Axis2Placement3D position;
        if (curve instanceof Line3D) {
            Line3D line = (Line3D) curve;
            position = new Axis2Placement3D(line.pnt(),
                    line.dir().verticalVector().unitized(),
                    line.dir().unitized());
        } else if (curve instanceof Conic3D) {
            position = ((Conic3D) curve).position();
        } else
            throw new FatalException();

        transform = new CartesianTransformationOperator3D(position);
        solutions = new CurveSurfaceInterferenceList(curve, bzs);
    }

    /**
     * �x�W�G�Ȗʂ𕽖ʂƂ݂Ȃ��Ă����Ƃ���?��赂����߂̓Ք�N���X
     */
    class BezierSurfaceInfo {
        private PureBezierSurface3D bzs;
        private double usp;
        private double uep;
        private double vsp;
        private double vep;
        EnclosingBox3D box;
        private PlaneBezier pb;
        private int currentType;

        /**
         * �x�W�G�Ȗ�?Au,v �J�n�p���??[�^?A
         * u,v ?I���p���??[�^�Ȃǂ�?���p����?A�I�u�W�F�N�g��?\�z����?B
         */
        BezierSurfaceInfo(PureBezierSurface3D bzs,
                          double usp, double uep,
                          double vsp, double vep) {
            this.bzs = bzs;
            this.usp = usp;
            this.uep = uep;
            this.vsp = vsp;
            this.vep = vep;
            this.box = bzs.approximateEnclosingBox();
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
                Point3D pnt = null;
                if (pb.shape_info[i] == 0) {
                    pb.bcrv[i] = null;
                    continue;
                }

                Point2D[] pnt2d = new Point2D[uicp];
                double[] ws = new double[uicp];
                for (int j = 0; j < uicp; j++) {
                    switch (i) {
                        case 0:
                            pnt = bzs.controlPointAt(j, 0);
                            if (bzs.isRational())
                                ws[j] = bzs.weightAt(j, 0);
                            break;
                        case 1:
                            pnt = bzs.controlPointAt(u_uicp - 1, j);
                            if (bzs.isRational())
                                ws[j] = bzs.weightAt(u_uicp - 1, j);
                            break;
                        case 2:
                            pnt = bzs.controlPointAt(u_uicp - 1 - j, v_uicp - 1);
                            if (bzs.isRational())
                                ws[j] = bzs.weightAt(u_uicp - 1 - j, v_uicp - 1);
                            break;
                        case 3:
                            pnt = bzs.controlPointAt(0, v_uicp - 1 - j);
                            if (bzs.isRational())
                                ws[j] = bzs.weightAt(0, v_uicp - 1 - j);
                            break;
                    }
                    evec = pnt.subtract(org);
                    pnt2d[j] = new CartesianPoint2D
                            (evec.dotProduct(xyz[0]), evec.dotProduct(xyz[1]));
                }

                if (bzs.isRational()) {
                    pb.bcrv[i] = new PureBezierCurve2D(pnt2d, ws);
                } else {
                    pb.bcrv[i] = new PureBezierCurve2D(pnt2d);
                }

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
            if ((point.x() > (box.max().x() + dTol)) ||
                    (point.y() > (box.max().y() + dTol)) ||
                    (point.z() > (box.max().z() + dTol)) ||
                    (point.x() < (box.min().x() - dTol)) ||
                    (point.y() < (box.min().y() - dTol)) ||
                    (point.z() < (box.min().z() - dTol))) {
                return false;
            }

            Vector3D evec = point.subtract(pb.origin());
            Point2D point2d = new CartesianPoint2D(evec.dotProduct(pb.axis.x()),
                    evec.dotProduct(pb.axis.y()));
            Vector2D dir = new LiteralVector2D(0.70710678, 0.70710678);
            Line2D line2d = new Line2D(point2d, dir);

            /*
            * saved_ipl_list check is needed
            */
            Vector saved_ipl_list = new Vector();
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

        /**
         * ��?����邩�ǂ�����?�����
         *
         * @param curve ��?�(��?ۂ͒�?�~??��?�̂ǂ��炩)
         * @return ��?����邩�ǂ���
         */
        private boolean checkInterfere(ParametricCurve3D curve) {
            //
            // ���̎��?��?��
            // curve.checkInterfere(this);
            // �Ƃ������Ƃ��낾��?AParametricCurve3D �ȉ��̃N���X���ׂĂ�
            // checkInterfere������Ȃ���΂����Ȃ��̂�?A�������Ă���?B
            //
            if (curve instanceof Line3D)
                return ((Line3D) curve).checkInterfere(this);
            else if (curve instanceof Conic3D)
                return ((Conic3D) curve).checkInterfere(this);
            else
                throw new FatalException();
        }

        /**
         * ��_��?�߂�
         *
         * @param line ��?�
         */
        private void intersectLinePlane(Line3D line) {
            // gh3ints_linpln
            Point3D origin = pb.origin();
            Vector3D[] xyz = pb.axis.axes();
            double eS, eT;
            eT = ((origin.y() * xyz[0].z())
                    - (origin.z() * xyz[0].y()))
                    / ((xyz[1].z() * xyz[0].y())
                    - (xyz[1].y() * xyz[0].z()));

            if (Math.abs(xyz[0].y()) > dTol)
                eS = -(origin.y() + (eT * xyz[1].y())) / xyz[0].y();
            else
                eS = -(origin.z() + (eT * xyz[1].z())) / xyz[0].z();

            // intersect_lin_pln
            double x = origin.x() + (eS * xyz[0].x()) + (eT * xyz[1].x());
            Point3D point = new CartesianPoint3D(x, 0, 0);

            if (!this.isPointInPlane(point))
                return;

            double ework = Math.sqrt(line.dir().norm());
            Line3D aLineL =
                    new Line3D(Point3D.origin,
                            new LiteralVector3D(ework, 0, 0));

            double aParam = x / aLineL.dir().x();
            double bUParam = (usp + uep) / 2.0;
            double bVParam = (vsp + vep) / 2.0;
            PointInfo pi = new PointInfo(point, aParam, bUParam, bVParam);
            if (refinePointInfo(pi)) {
                pi.pnt = transform.toEnclosed(pi.pnt);
                solutions.addAsIntersection(pi.pnt, pi.aParam,
                        pi.bUParam, pi.bVParam);
            }
            return;
        }

        /**
         * ��_��?�߂�
         *
         * @param cnc �~??��?�
         */
        private void intersectConicPlane(Conic3D cnc) {
            IntersectionPoint3D[] intp =
                    cnc.intersectConicPlane(new Plane3D(pb.axis));
            for (int i = 0; i < intp.length; i++) {
                if (!this.isPointInPlane(intp[i]))
                    continue;
                Point3D point = intp[i].coordinates();
                double aParam = intp[i].pointOnCurve1().parameter();
                double bUParam = (usp + uep) / 2.0;
                double bVParam = (vsp + vep) / 2.0;
                PointInfo pi = new PointInfo(point, aParam, bUParam, bVParam);
                if (refinePointInfo(pi)) {
                    pi.pnt = transform.toEnclosed(pi.pnt);
                    solutions.addAsIntersection(pi.pnt, pi.aParam,
                            pi.bUParam, pi.bVParam);
                }
            }
            return;
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
     * �������ꂽ�ȖʂƋ�?�̌�_��?�߂�(intersect_linpln)
     *
     * @param bsi �x�W�G�Ȗ�?��
     */
    private void intersect_dA_plane(BezierSurfaceInfo bsi) {
        // Line or Conic �𔻒f���� Dispatch ����
        if (dA instanceof Line3D)
            bsi.intersectLinePlane((Line3D) dA);
        else if (dA instanceof Conic3D)
            bsi.intersectConicPlane((Conic3D) dA);
        else
            throw new FatalException();

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
    private class dnlFunc implements PrimitiveMappingND {
        int idx;

        private dnlFunc(int idx) {
            super();
            this.idx = idx;
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
            Vector3D aTang;
            Vector3D[] bTang = new Vector3D[3];

            if (dA instanceof Line3D) {
                aTang = ((Line3D) dA).dnlFunc
                        (dA.parameterDomain().force(parameter[0]));
            } else if (dA instanceof Conic3D) {
                aTang = ((Conic3D) dA).dnlFunc
                        (dA.parameterDomain().force(parameter[0]));
            } else
                throw new FatalException();

            bTang = bzsL.tangentVector(dB.uParameterDomain().force(parameter[1]),
                    dB.vParameterDomain().force(parameter[2]));
            switch (idx) {
                case 0:
                    return new double[]{aTang.x(), -bTang[0].x(), -bTang[1].x()};
                case 1:
                    return new double[]{aTang.y(), -bTang[0].y(), -bTang[1].y()};
                case 2:
                    return new double[]{aTang.z(), -bTang[0].z(), -bTang[1].z()};
                default:
                    throw new FatalException();

            }
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
            if (dA instanceof Line3D)
                sApnt = ((Line3D) dA).nlFunc
                        (dA.parameterDomain().force(parameter[0]));
            else if (dA instanceof Conic3D)
                sApnt = ((Conic3D) dA).nlFunc
                        (dA.parameterDomain().force(parameter[0]));
            else
                throw new FatalException();

            sBpnt = bzsL.coordinates(dB.uParameterDomain().force(parameter[1]),
                    dB.vParameterDomain().force(parameter[2]));

            return sApnt.identical(sBpnt);
        }
    }

    /**
     * setback_params
     */
    private void setbackParams(PointInfo pi, double[] param) {
        Point3D aPnt = null;
        Point3D bPnt = null;

        pi.aParam = dA.parameterDomain().force(param[0]);
        pi.bUParam = dB.uParameterDomain().force(param[1]);
        pi.bVParam = dB.vParameterDomain().force(param[2]);

        if (dA instanceof Line3D) {
            aPnt = ((Line3D) dA).nlFunc(pi.aParam);
        } else if (dA instanceof Conic3D) {
            aPnt = ((Conic3D) dA).nlFunc(pi.aParam);
        }
        bPnt = bzsL.coordinates(pi.bUParam, pi.bVParam);

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
        dnl_func[0] = new dnlFunc(0);
        dnl_func[1] = new dnlFunc(1);
        dnl_func[2] = new dnlFunc(2);
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
    void getIntersections(BezierSurfaceInfo crnt_bi) {
        BezierSurfaceInfo bi00, bi01, bi10, bi11;
        boolean ci00, ci01, ci10, ci11;
        /*
        * is current bezier regarded as plane ?
        */
        if (crnt_bi.whatTypeIsBezierSurface() != BEZIER) {
            intersect_dA_plane(crnt_bi);
            return;
        }

        /*
        * generate children
        */
        double half_point = 0.5;
        double ug_half = (crnt_bi.usp + crnt_bi.uep) / 2.0;
        double vg_half = (crnt_bi.vsp + crnt_bi.vep) / 2.0;

        RETRY:
        do {
            PureBezierSurface3D[] geomB = crnt_bi.bzs.vDivide(half_point);
            PureBezierSurface3D[] bzs0 = geomB[0].uDivide(half_point);
            PureBezierSurface3D[] bzs1 = geomB[1].uDivide(half_point);

            bi00 = new BezierSurfaceInfo(bzs0[0],
                    crnt_bi.usp, ug_half,
                    crnt_bi.vsp, vg_half);
            bi01 = new BezierSurfaceInfo(bzs0[1],
                    ug_half, crnt_bi.uep,
                    crnt_bi.vsp, vg_half);
            bi10 = new BezierSurfaceInfo(bzs1[0],
                    crnt_bi.usp, ug_half,
                    vg_half, crnt_bi.vep);
            bi11 = new BezierSurfaceInfo(bzs1[1],
                    ug_half, crnt_bi.uep,
                    vg_half, crnt_bi.vep);

            ci00 = bi00.checkInterfere(dA);
            ci01 = bi01.checkInterfere(dA);
            ci10 = bi10.checkInterfere(dA);
            ci11 = bi11.checkInterfere(dA);

            if (half_point > 0.45) {
                if ((ci00 != true) && (ci01 != true)
                        && (ci10 != true) && (ci11 != true)) {
                    half_point = 0.4;
                    ug_half = (0.6 * crnt_bi.usp) + (0.4 * crnt_bi.uep);
                    vg_half = (0.6 * crnt_bi.vsp) + (0.4 * crnt_bi.vep);
                    continue RETRY;
                }
            }
            break RETRY;
        } while (true);

        /*
        * recursive call
        */
        if (ci00 == true) getIntersections(bi00);
        if (ci01 == true) getIntersections(bi01);
        if (ci10 == true) getIntersections(bi10);
        if (ci11 == true) getIntersections(bi11);
        return;
    }

    /**
     * �x�W�G�Ȗʂɕϊ�����(transform_bezier)
     *
     * @param curve ��?�(��?�~??��?�)
     * @param bzs   �x�W�G�Ȗ�
     * @return �ϊ����ꂽ�x�W�G�Ȗ�
     */
    PureBezierSurface3D transformBezier(ParametricCurve3D curve,
                                        PureBezierSurface3D bzs) {
        int uncp = bzs.uNControlPoints();
        int vncp = bzs.vNControlPoints();

        Point3D[][] cp = new Point3D[uncp][vncp];
        for (int i = 0; i < uncp; i++)
            for (int j = 0; j < vncp; j++)
                cp[i][j] = transform.toLocal(bzs.controlPointAt(i, j));

        if (!bzs.isRational()) {
            return new PureBezierSurface3D(cp);
        } else {
            return new PureBezierSurface3D(cp, bzs.weights());
        }
    }

    /*********************************************************************
     *
     * Body (defined as external, since this is called from gh3intsBssBss.c)
     *
     **********************************************************************/

    /**
     * �~??��?�ƃx�W�G�Ȗʂ̌�_�𓾂�
     *
     * @param dA Conic          A
     * @param dB Bezier Surface B
     * @return ��_�̔z��
     */
    CurveSurfaceInterferenceList intsCncBzs() {
        // Preparation
        // Initialize Tree & Make Roots
        bzsL = transformBezier(dA, dB);
        BezierSurfaceInfo dBRoot = new BezierSurfaceInfo(bzsL, 0.0, 1.0, 0.0, 1.0);

        // check interfere
        boolean hasInterfere;
        if (dA instanceof Line3D)
            hasInterfere = ((Line3D) dA).checkInterfere(dBRoot);
        else if (dA instanceof Conic3D)
            hasInterfere = ((Conic3D) dA).checkInterfere(dBRoot);
        else
            throw new FatalException();

        //get intersection
        if (hasInterfere)
            getIntersections(dBRoot);

        // Make Results
        return solutions;
    }

    /**
     * �~??��?�ƃx�W�G�Ȗʂ̌�_�𓾂�
     *
     * @param cnc �~??��?�
     * @param bzs �x�W�G�Ȗ�
     * @return ��_�̔z��
     */
    static IntersectionPoint3D[] intersection(Conic3D cnc,
                                              PureBezierSurface3D bzs,
                                              boolean doExchange) {
        IntsCncBzs3D doObj = new IntsCncBzs3D(cnc, bzs);
        return doObj.intsCncBzs().toIntersectionPoint3DArray(doExchange);
    }

    /**
     * ��?�ƃx�W�G�Ȗʂ̌�_�𓾂�
     *
     * @param line ��?�
     * @param bzs  �x�W�G�Ȗ�
     * @return ��_�̔z��
     */
    static IntersectionPoint3D[] intersection(Line3D line,
                                              PureBezierSurface3D bzs,
                                              boolean doExchange) {
        IntsCncBzs3D doObj = new IntsCncBzs3D(line, bzs);
        return doObj.intsCncBzs().toIntersectionPoint3DArray(doExchange);
    }
}

/* end of file */
