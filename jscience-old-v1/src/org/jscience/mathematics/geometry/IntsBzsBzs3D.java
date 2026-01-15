/*
 * 3D�x�W�G�Ȗʓ��m�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsBzsBzs3D.java,v 1.7 2006/03/01 21:16:01 virtualcall Exp $
 */

/*
 * intersection of 2 bezier surfaces
 *
 *	1. if 2 surfaces seem to intersect, divide them respectively until
 *	   they are considered as rectangular.
 *	2. find the bounded intersection between 2 bounded planes.
 *	3. connect the bounded intersections.
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.MachineEpsilon;
import org.jscience.mathematics.analysis.PrimitiveMappingND;
import org.jscience.util.FatalException;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 3D�x�W�G�Ȗʓ��m�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.7 $, $Date: 2006/03/01 21:16:01 $
 */

final class IntsBzsBzs3D {
    /**
     * �f�o�b�O�t���O
     */
    int debugFlag;

    /**
     * �f�o�b�O�t���O��?�: �����A���S���Y���̃f�o�b�O
     */
    static final int DEBUG_DIVIDE = 1;

    /**
     * �f�o�b�O�t���O��?�: ��?�v�Z���̃f�o�b�O
     */
    static final int DEBUG_INTERSEC = 2;

    /**
     * �f�o�b�O�t���O��?�: ���C���Z�O�?���g?�?����̃f�o�b�O
     */
    static final int DEBUG_LINESEG = 4;

    /**
     * �f�o�b�O�t���O��?�: ���C���Z�O�?���g��?d����?��̃f�o�b�O
     */
    static final int DEBUG_SAMESEG = 8;

    /**
     * �f�o�b�O�t���O��?�: ���C���Z�O�?���g�̌�?������̃f�o�b�O
     */
    static final int DEBUG_CONNECT = 16;

    /**
     * �f�o�b�O�t���O��?�: �_�� refine �����̃f�o�b�O
     */
    static final int DEBUG_REFINE = 32;

    /**
     * �f�o�b�O�t���O��?�: �S��
     */
    static final int DEBUG_ALL = 1 | 2 | 4 | 8 | 16 | 32;

    /**
     * �p���??[�^��?���?ő�l
     */
    static final double pTolMax = 0.1;

    /**
     * �����A���S���Y�����̋�����?�
     */
    static final double TORELANCE_OVERRIDE_IN_INTERSECTION = 1e-2;

    /**
     * bezier �Ȗ� A
     */
    PureBezierSurface3D dA;

    /**
     * bezier �Ȗ� B
     */
    PureBezierSurface3D dB;

    /**
     * �Ȗ� A �� 4 ����
     */
    QuadTree Atree;

    /**
     * ��� PointList �� Vector
     */
    Vector solutionCurves;

    /**
     * ��?ڑ��̉�
     */
    ObjectVector solutionSegs;

    /**
     * �����̋��e��?�(�L���b�V��)
     */
    double dTol;

    /**
     * �����̋��e��?��̎�?�(�L���b�V��)
     */
    double dTol2;

    /**
     * �p���??[�^��(���I)���e��?�(�L���b�V��)
     */
    double pTol;

    // BoundaryInfo indicator
    static final int Wp_No = 0;
    static final int Wp_Au = 1;
    static final int Wp_Av = 2;
    static final int Wp_Bu = 3;
    static final int Wp_Bv = 4;

    static final int Wp_START = 1;
    static final int Wp_END = 5;

    /**
     * ��?�?�̓_��\������Ք�N���X
     */
    final class PointInfo {
        /**
         * �_�̂R����?W
         */
        Point3D pnt;

        /**
         * �� A ?�� U �p���??[�^
         */
        double Aupara;

        /**
         * �� A ?�� V �p���??[�^
         */
        double Avpara;

        /**
         * �� B ?�� U �p���??[�^
         */
        double Bupara;

        /**
         * �� B ?�� V �p���??[�^
         */
        double Bvpara;

        /**
         * �� A ?�� U �p���??[�^�̋��e��?�
         */
        double AuPTol;

        /**
         * �� A ?�� V �p���??[�^�̋��e��?�
         */
        double AvPTol;

        /**
         * �� B ?�� U �p���??[�^�̋��e��?�
         */
        double BuPTol;

        /**
         * �� B ?�� V �p���??[�^�̋��e��?�
         */
        double BvPTol;

        /**
         * �_�� tangentVector �Ƌ����̋��e��?�����
         * �p���??[�^�̋��e��?���v�Z����
         */
        private double tangentToPTol(Vector3D tangent) {
            double pt = dTol / tangent.length();
            return Math.min(pt, pTolMax);
        }

        /**
         * ���̓_�ɂ�����p���??[�^�̋��e��?��̒l��v�Z����
         */
        private void setupPTol() {
            Vector3D[] tang;

            tang = dA.tangentVector(Aupara, Avpara);
            AuPTol = tangentToPTol(tang[0]);
            AvPTol = tangentToPTol(tang[1]);

            tang = dB.tangentVector(Bupara, Bvpara);
            BuPTol = tangentToPTol(tang[0]);
            BvPTol = tangentToPTol(tang[1]);
        }

        /**
         * �p���??[�^��l�^���ăI�u�W�F�N�g��?�?�����
         */
        PointInfo(Point3D pnt, double Au, double Av, double Bu, double Bv) {
            this.pnt = pnt;
            Aupara = Au;
            Avpara = Av;
            Bupara = Bu;
            Bvpara = Bv;
            setupPTol();
        }

        /**
         * PointOnSurface ��^���ăI�u�W�F�N�g��?�?�����
         */
        PointInfo(Point3D pnt,
                  PointOnSurface3D iA, PointOnSurface3D iB) {
            this(pnt,
                    iA.uParameter(), iA.vParameter(),
                    iB.uParameter(), iB.vParameter());
        }

        /**
         * 2�_�̒��ԓ_��v�Z��?Athis �̒l���?X����
         */
        void interpolate(PointInfo pb) {
            this.pnt = pnt.linearInterpolate(pb.pnt, 0.5);

            Aupara = (Aupara + pb.Aupara) / 2.0;
            Avpara = (Avpara + pb.Avpara) / 2.0;
            Bupara = (Bupara + pb.Bupara) / 2.0;
            Bvpara = (Bvpara + pb.Bvpara) / 2.0;
            setupPTol();
        }

        /**
         * �^����ꂽ�p���??[�^�̋��E�����?���Ԃ�
         */
        double getBoundaryGap(int which, int side) {
            double para;

            switch (which) {
                default:
                    throw new FatalException();
                case Wp_Au:
                    para = Aupara;
                    break;
                case Wp_Av:
                    para = Avpara;
                    break;
                case Wp_Bu:
                    para = Bupara;
                    break;
                case Wp_Bv:
                    para = Bvpara;
                    break;
            }

            if (side == UPPER)
                return Math.abs(1.0 - para);
            else
                return Math.abs(para);
        }
    }

    /**
     * ��?�̃Z�O�?���g��\������Ք�N���X
     */
    final class LineSegmentInfo {
        /**
         * �_1
         */
        PointInfo p1;

        /**
         * �_2
         */
        PointInfo p2;

        /**
         * �Z�O�?���g��͂ޔ�
         */
        EnclosingBox3D box;

        /**
         * �Z�O�?���g�̎�?撷
         */
        double leng2;

        /**
         * �Z�O�?���g�̒���
         */
        double leng;

        boolean is_main_line;

        /**
         * ��_��w�肵�ăZ�O�?���g��?�?�����
         */
        LineSegmentInfo(PointInfo p1, PointInfo p2) {
            this.p1 = p1;
            this.p2 = p2;

            leng2 = p1.pnt.distance2(p2.pnt);
            leng = Math.sqrt(leng2);

            Point3D pnt1 = p1.pnt;
            Point3D pnt2 = p2.pnt;

            box = new EnclosingBox3D(Math.min(pnt1.x(), pnt2.x()),
                    Math.min(pnt1.y(), pnt2.y()),
                    Math.min(pnt1.z(), pnt2.z()),
                    Math.max(pnt1.x(), pnt2.x()),
                    Math.max(pnt1.y(), pnt2.y()),
                    Math.max(pnt1.z(), pnt2.z()));
            is_main_line = true;
            if ((debugFlag & DEBUG_LINESEG) != 0) {
                System.out.println("LineSegmentInfo:");
                pnt1.literal().output(System.out);
                pnt2.literal().output(System.out);
            }
        }

        /**
         * ����_���Z�O�?���g?゠�邩�ǂ�����?�����
         */
        boolean isPointOnLineseg(PointInfo pi) {
            Point3D tgt = pi.pnt;
            Point3D bsp = p1.pnt;
            Point3D bep = p2.pnt;
            Vector3D bln = bep.subtract(bsp);
            Vector3D dir = tgt.subtract(bsp);
            double bleng = bln.length();
            Vector2D bln2;
            Vector2D dir2;
            double edot;
            double esin2;

            if ((debugFlag & DEBUG_LINESEG) != 0) {
                System.out.println("isPointOnLineseg:");
            }

            if (bleng < MachineEpsilon.SINGLE) {
                // base vector is reduced
                edot = 0.0;
            } else {
                // make an unit base vector & take a dot product
                edot = bln.divide(bleng).dotProduct(dir);
                if ((edot < 0.0) || (bleng < edot))
                    return false;
            }

            esin2 = dir.norm() - (edot * edot);
            if (esin2 > dTol2)
                return false;

            bln2 = new LiteralVector2D(p2.Aupara - p1.Aupara,
                    p2.Avpara - p1.Avpara);
            dir2 = new LiteralVector2D(pi.Aupara - p1.Aupara,
                    pi.Avpara - p1.Avpara);

            bleng = bln2.length();

            if (bleng < MachineEpsilon.SINGLE) {
                edot = 0.0;
            } else {
                edot = bln2.divide(bleng).dotProduct(dir2);
                if ((edot < 0.0) || (bleng < edot))
                    return false;
            }

            esin2 = dir2.norm() - (edot * edot);
            if (esin2 > dTol2)
                return false;

            bln2 = new LiteralVector2D(p2.Bupara - p1.Bupara,
                    p2.Bvpara - p1.Bvpara);
            dir2 = new LiteralVector2D(pi.Bupara - p1.Bupara,
                    pi.Bvpara - p1.Bvpara);

            bleng = bln2.length();

            if (bleng < MachineEpsilon.SINGLE) {
                edot = 0.0;
            } else {
                edot = bln2.divide(bleng).dotProduct(dir2);
                if ((edot < 0.0) || (bleng < edot))
                    return false;
            }

            esin2 = dir2.norm() - (edot * edot);
            if (esin2 > dTol2)
                return false;

            // Range in A's 2D
            if (Math.abs(p1.Aupara - p2.Aupara) >
                    Math.abs(p1.Avpara - p2.Avpara)) {
                if (p1.Aupara > p2.Aupara) {
                    if ((pi.Aupara > (p1.Aupara + p1.AuPTol)) ||
                            (pi.Aupara < (p2.Aupara - p2.AuPTol)))
                        return false;
                } else {
                    if ((pi.Aupara < (p1.Aupara - p1.AuPTol)) ||
                            (pi.Aupara > (p2.Aupara + p2.AuPTol)))
                        return false;
                }

            } else {
                if (p1.Avpara > p2.Avpara) {
                    if ((pi.Avpara > (p1.Avpara + p1.AvPTol)) ||
                            (pi.Avpara < (p2.Avpara - p2.AvPTol)))
                        return false;
                } else {
                    if ((pi.Avpara < (p1.Avpara - p1.AvPTol)) ||
                            (pi.Avpara > (p2.Avpara + p2.AvPTol)))
                        return false;
                }
            }

            // Range in B's 2D
            if (Math.abs(p1.Bupara - p2.Bupara) >
                    Math.abs(p1.Bvpara - p2.Bvpara)) {
                if (p1.Bupara > p2.Bupara) {
                    if ((pi.Bupara > (p1.Bupara + p1.BuPTol)) ||
                            (pi.Bupara < (p2.Bupara - p2.BuPTol)))
                        return false;
                } else {
                    if ((pi.Bupara < (p1.Bupara - p1.BuPTol)) ||
                            (pi.Bupara > (p2.Bupara + p2.BuPTol)))
                        return false;
                }

            } else {
                if (p1.Bvpara > p2.Bvpara) {
                    if ((pi.Bvpara > (p1.Bvpara + p1.BvPTol)) ||
                            (pi.Bvpara < (p2.Bvpara - p2.BvPTol)))
                        return false;
                } else {
                    if ((pi.Bvpara < (p1.Bvpara - p1.BvPTol)) ||
                            (pi.Bvpara > (p2.Bvpara + p2.BvPTol)))
                        return false;
                }
            }

            if ((debugFlag & DEBUG_LINESEG) != 0) {
                System.out.println("isPointOnLineseg: true");
            }
            return true;
        }

        /**
         * ?d������Z�O�?���g����?���
         */
        boolean assignSameLineseg(LineSegmentInfo l2,
                                  boolean two_cc) {
            if ((debugFlag & DEBUG_SAMESEG) != 0) {
                System.out.println("assignSameLineseg:");
            }

            if (two_cc) {
                // 2 coincidences : former segment is preferred
                if ((leng + dTol) * (leng + dTol) < l2.leng2) {
                    is_main_line = false;
                    return false;    /* continue searching */
                } else {
                    l2.is_main_line = false;
                    return true;    /* stop searching */
                }

            } else {
                // there are some gaps
                if (leng2 < l2.leng2) {
                    is_main_line = false;
                    return false;    /* continue searching */
                } else {
                    l2.is_main_line = false;
                    return true;    /* stop searching */
                }
            }
        }

        /**
         * pair �Ŏw�肵���[�_���m����v���邩�ǂ�����?�����
         */
        boolean isSamePoint(LineSegmentInfo l2, double dist2[], int pair) {
            PointInfo l1p = null;
            PointInfo l2p = null;

            if ((debugFlag & DEBUG_LINESEG) != 0) {
                System.out.println("isSamePoint:");
            }

            if (dTol2 < dist2[pair])
                return false;

            switch (pair) {
                case SS:
                    if ((dist2[SS] > dist2[ES]) || (dist2[SS] > dist2[SE]))
                        return false;
                    l1p = p1;
                    l2p = l2.p1;
                    break;

                case EE:
                    if ((dist2[EE] > dist2[ES]) || (dist2[EE] > dist2[SE]))
                        return false;
                    l1p = p2;
                    l2p = l2.p2;
                    break;

                case ES:
                    if ((dist2[ES] > dist2[SS]) || (dist2[ES] > dist2[EE]))
                        return false;
                    l1p = p2;
                    l2p = l2.p1;
                    break;

                case SE:
                    if ((dist2[SE] > dist2[SS]) || (dist2[SE] > dist2[EE]))
                        return false;
                    l1p = p1;
                    l2p = l2.p2;
                    break;
            }

            if (Math.abs(l1p.Aupara - l2p.Aupara) > (l1p.AuPTol + l2p.AuPTol))
                return false;
            if (Math.abs(l1p.Avpara - l2p.Avpara) > (l1p.AvPTol + l2p.AvPTol))
                return false;
            if (Math.abs(l1p.Bupara - l2p.Bupara) > (l1p.BuPTol + l2p.BuPTol))
                return false;
            if (Math.abs(l1p.Bvpara - l2p.Bvpara) > (l1p.BvPTol + l2p.BvPTol))
                return false;

            if ((debugFlag & DEBUG_LINESEG) != 0) {
                System.out.println("isSamePoint: true");
            }
            return true;
        }

        /**
         * ?d������Z�O�?���g��?o����
         */
        boolean isSameLineseg(LineSegmentInfo l2) {
            double[] dist2 = new double[4]; // squared distances of 2 points
            boolean[] is_same = new boolean[4];
            int is_on_cnt;
            boolean check_is_on;

            if (!is_main_line)
                return false;

            // rough check with min-max box
            if ((box.max().x() + dTol < l2.box.min().x()) ||
                    (box.max().y() + dTol < l2.box.min().y()) ||
                    (box.max().z() + dTol < l2.box.min().z()) ||
                    (l2.box.max().x() + dTol < box.min().x()) ||
                    (l2.box.max().y() + dTol < box.min().y()) ||
                    (l2.box.max().z() + dTol < box.min().z()))
                return false;

            if ((debugFlag & DEBUG_SAMESEG) != 0) {
                System.out.println("isSameLineseg1:");
                p1.pnt.literal().output(System.out);
                p2.pnt.literal().output(System.out);
                l2.p1.pnt.literal().output(System.out);
                l2.p2.pnt.literal().output(System.out);
            }

            /*
            * assign this as same if there are 2 coincidences
            *
            *	+-----------+
            *	+-----------+
            */
            dist2[SS] = p1.pnt.distance2(l2.p1.pnt);
            dist2[EE] = p2.pnt.distance2(l2.p2.pnt);
            dist2[ES] = p2.pnt.distance2(l2.p1.pnt);
            dist2[SE] = p1.pnt.distance2(l2.p2.pnt);

            if ((debugFlag & DEBUG_SAMESEG) != 0) {
                System.out.println(dist2[SS] + " " +
                        dist2[EE] + " " +
                        dist2[ES] + " " +
                        dist2[SE]);
            }

            is_on_cnt = 0;
            is_same[SS] = isSamePoint(l2, dist2, SS);
            if (is_same[SS])
                ++is_on_cnt;

            is_same[EE] = isSamePoint(l2, dist2, EE);
            if (is_same[EE]) {
                if (++is_on_cnt == 2)
                    return assignSameLineseg(l2, true);
            }

            is_same[ES] = isSamePoint(l2, dist2, ES);
            if (is_same[ES]) {
                if (++is_on_cnt == 2)
                    return assignSameLineseg(l2, true);
            }

            is_same[SE] = isSamePoint(l2, dist2, SE);
            if (is_same[SE]) {
                if (++is_on_cnt == 2)
                    return assignSameLineseg(l2, true);
            }

            if ((debugFlag & DEBUG_SAMESEG) != 0) {
                System.out.println("isSameLineseg2:");
            }

            /*
            * assign this as same OK if (there is 1 coincidence) and
            *		  (either of other ends is in the opponent)
            *
            *	+-----------+
            *	+-----+
            */
            check_is_on = false;
            if (is_same[SS]) {        /* p1 == l2.p1 */
                if (l2.isPointOnLineseg(p2) ||
                        isPointOnLineseg(l2.p2))
                    return assignSameLineseg(l2, false);

                check_is_on = true;
            } else if (is_same[EE]) {    /* p2 == l2.p2 */
                if (l2.isPointOnLineseg(p1) ||
                        isPointOnLineseg(l2.p1))
                    return assignSameLineseg(l2, false);

                check_is_on = true;
            } else if (is_same[ES]) {    /* p2 == l2.p1 */
                if (l2.isPointOnLineseg(p1) ||
                        isPointOnLineseg(l2.p2))
                    return assignSameLineseg(l2, false);

                check_is_on = true;
            } else if (is_same[SE]) {    /* p1 == l2.p2 */
                if (l2.isPointOnLineseg(p2) ||
                        isPointOnLineseg(l2.p1))
                    return assignSameLineseg(l2, false);

                check_is_on = true;
            }

            if ((debugFlag & DEBUG_SAMESEG) != 0) {
                System.out.println("isSameLineseg3:");
            }

            /*
            * assign this as same OK if (there is no coincidence) and
            *		  (2 or more points are in oppenent)
            *
            *	+-----------+	or	+-----------+
            *	   +-----+		      +-----------+
            */
            if (!check_is_on) {
                is_on_cnt = 0;
                is_on_cnt += l2.isPointOnLineseg(p1) ? 1 : 0;
                is_on_cnt += l2.isPointOnLineseg(p2) ? 1 : 0;
                is_on_cnt += isPointOnLineseg(l2.p1) ? 1 : 0;
                is_on_cnt += isPointOnLineseg(l2.p2) ? 1 : 0;

                if (is_on_cnt > 1)
                    return assignSameLineseg(l2, false);
            }

            if ((debugFlag & DEBUG_SAMESEG) != 0) {
                System.out.println("isSameLineseg4:");
            }
            return false;
        }
    }

    /**
     * ��_�Ƃ��̕t??�f?[�^��\�����邽�߂̓Ք�N���X
     */
    final class IntersectPoint {
        /**
         * �O�p�`�̕���?�ɂ�����?A�O�p�`�̕ӂƒ�?�̌�_
         */
        IntersectionPoint2D ip;

        /**
         * ��?�̌�_�p���??[�^
         */
        double lineParam;

        /**
         * �O�p�`�̕ӂ̌�_�p���??[�^?B
         * [0.0 ... 1.0] �łȂ����?A�ӂƒ�?�͌�?�Ȃ�?B
         */
        double segParam;

        /**
         * �O�p�`�̕ӂ̎n�_(���_�̈��)�� PointOnSurface �Ƃ��ĕ\���������
         */
        PointOnSurface3D startP;

        /**
         * �O�p�`�̕ӂ�?I�_(���_�̈��)�� PointOnSurface �Ƃ��ĕ\���������
         */
        PointOnSurface3D endP;

        /**
         * ��_�� PointOnSurface �Ƃ��ĕ\�������ߎ��l
         */
        PointOnSurface3D segPoint;

        /**
         * ��������Ă��Ȃ� BezierCurve
         */
        PureBezierSurface3D bzs;

        /**
         * �p���??[�^��?ݒ肷��
         */
        void setup(PureBezierSurface3D root,
                   IntersectionPoint2D ip,
                   PointOnSurface3D sp,
                   PointOnSurface3D ep) {
            this.ip = ip;
            segParam = ip.pointOnCurve1().parameter();
            lineParam = ip.pointOnCurve2().parameter();
            bzs = root;
            startP = sp;
            endP = ep;

            double uparam = (1.0 - segParam) * sp.uParameter() +
                    segParam * ep.uParameter();
            double vparam = (1.0 - segParam) * sp.vParameter() +
                    segParam * ep.vParameter();
            segPoint = new PointOnSurface3D(root, uparam, vparam, GeometryElement.doCheckDebug);
            // segPoint = refine(segPoint);
        }

        /**
         * �O�p�`�̌�_�f?[�^��^���ăI�u�W�F�N�g��?�?�����
         */
        IntersectPoint(PureBezierSurface3D root,
                       IntersectionPoint2D ip,
                       PointOnSurface3D sp,
                       PointOnSurface3D ep) {
            setup(root, ip, sp, ep);
        }

        /**
         * ��_�p���??[�^�Ɠ�?��^���ăI�u�W�F�N�g��?�?�����
         */
        IntersectPoint(PureBezierSurface3D root,
                       Line2D seg, double param, Line2D line,
                       PointOnSurface3D sp,
                       PointOnSurface3D ep) {
            PointOnCurve2D poc1 = new PointOnCurve2D(seg, param, GeometryElement.doCheckDebug);
            PointOnCurve2D poc2 = line.project1From(poc1);
            setup(root, new IntersectionPoint2D(poc1, poc2, GeometryElement.doCheckDebug),
                    sp, ep);
        }

        double lineParam() {
            return lineParam;
        }

        double segParam() {
            return segParam;
        }

        PointOnSurface3D segPoint() {
            return segPoint;
        }

        /**
         * ������?���(����)�ł͂Ȃ���̖�(����)�ɂ���?A
         * PointOnCurve �̋ߎ��l��v�Z����?B
         */
        PointOnSurface3D interpPoint(IntersectPoint sp, IntersectPoint ep) {
            double uparam;
            double vparam;
            PointOnSurface3D ssp = sp.segPoint();
            PointOnSurface3D esp = ep.segPoint();

            // assertion
            if (sp.bzs != ep.bzs)
                throw new FatalException();

            if (sp.ip.identical(ep.ip)) {
                uparam = (ssp.uParameter() + esp.uParameter()) / 2.0;
                vparam = (ssp.vParameter() + esp.vParameter()) / 2.0;
            } else {
                // param == 0.0 if this == sp
                // param == 1.0 if this == ep
                double param = (lineParam - sp.lineParam) /
                        (ep.lineParam - sp.lineParam);
                uparam = (1.0 - param) * ssp.uParameter() +
                        param * esp.uParameter();
                vparam = (1.0 - param) * ssp.vParameter() +
                        param * esp.vParameter();
            }
            PointOnSurface3D pos =
                    new PointOnSurface3D(sp.bzs, uparam, vparam, GeometryElement.doCheckDebug);
            return pos;
            // return refine(pos);
        }
    }

    /*
    * �l�p�`�̊e���_?A�y�ъe�ӂ�?A���̂悤�ɓY���t���邱�Ƃɂ��܂�:
    *
    *		           (1,1)
    *	       [3]<---<2>---[2]
    *		|            ^
    *	    V	|            |
    *	    ^	|            |
    *	    |  <3>          <1>
    *	    |	|            |
    *	    |	|            |
    *		v            |
    *	       [0]---<0>--->[1]
    *	      (0,0)
    *		     --->U
    *
    * �܂�?A�֋X?㒸�_ [4] �� ���_ [0] �Ɠ��ꎋ���܂�?B
    */

    /**
     * �O�p�`��\������Ք�N���X
     */
    final class TriangleInfo {
        /**
         * 3�p�`�̒��_
         */
        PointOnSurface3D[] bp;

        /**
         * 3�p�`��3��(��?�?W)
         */
        Line2D[] seg2;

        /**
         * �ߎ����ʂ�\����?�?W�n
         */
        Axis2Placement3D axis;

        /**
         * �ߎ�����
         */
        Plane3D plane;

        /**
         * ��?�?W�n�ւ� Cartesian �ϊ�
         */
        CartesianTransformationOperator3D trans;

        /**
         * BezierInfo
         */
        BezierInfo bi;

        void output(OutputStream out) {
            PrintWriter writer = new PrintWriter(out, true);
            writer.println("[[");
            for (int i = 0; i < 3; i++)
                bp[i].literal().output(out);
            writer.println("xaxis = ");
            axis.x().output(out);
            writer.println("zaxis = ");
            axis.z().output(out);
            writer.println("]]");
        }

        /**
         * PointOnSurface ��?�?�
         */
        PointOnSurface3D makePointOnSurface(BezierInfo bi,
                                            Point3D pnt,
                                            double lUpara,
                                            double lVpara) {
            this.bi = bi;
            double upar = (1.0 - lUpara) * (bi.u_sp) + lUpara * (bi.u_ep);
            double vpar = (1.0 - lVpara) * (bi.v_sp) + lVpara * (bi.v_ep);

            PointOnSurface3D pos =
                    new PointOnSurface3D(pnt, bi.root, upar, vpar, GeometryElement.doCheckDebug);
            return pos;
        }

        void output_line(Line2D line) {
            System.out.println("line");
            line.pnt().literal().output(System.out);
            line.dir().output(System.out);
        }

        /**
         * ���_�̓Y������?A���_�� U �p���??[�^��v�Z����
         */
        private final double uparam(int idx) {
            switch (idx) {
                case 0:
                case 3:
                case 4:
                default:
                    return 0.0;
                case 1:
                case 2:
                    return 1.0;
            }
        }

        /**
         * ���_�̓Y������?A���_�� V �p���??[�^��v�Z����
         */
        private final double vparam(int idx) {
            switch (idx) {
                case 0:
                case 1:
                case 4:
                default:
                    return 0.0;
                case 2:
                case 3:
                    return 1.0;
            }
        }

        /**
         * ���_�� PointOnCurface �Ƃ��ė^����?�����
         */
        private void setupTriangle(PointOnSurface3D[] tmpbp) {
            Vector3D xaxis = tmpbp[1].subtract(tmpbp[0]).unitized();
            Vector3D zaxis = tmpbp[2].subtract(tmpbp[0])
                    .crossProduct(xaxis).unitized();
            axis = new Axis2Placement3D(tmpbp[0], zaxis, xaxis);
            trans = new CartesianTransformationOperator3D(axis, 1.0);
            plane = new Plane3D(axis);

            int maxdist = -1;
            double maxnorm = 0.0;
            int i;
            for (i = 0; i < 3; i++) {
                double norm = tmpbp[(i + 1) % 3].distance2(tmpbp[i]);
                if (maxnorm <= norm) {
                    maxdist = i;
                    maxnorm = norm;
                }
            }

            bp = new PointOnSurface3D[4];
            seg2 = new Line2D[3];

            for (i = maxdist; i < maxdist + 3; i++) {
                bp[i - maxdist] = tmpbp[i % 3];
                if (tmpbp[i % 3].identical(tmpbp[(i + 1) % 3])) {
                    seg2[i - maxdist] = null;
                } else {
                    seg2[i - maxdist] = new Line2D(tmpbp[i % 3].to2D(trans),
                            tmpbp[(i + 1) % 3].to2D(trans));
                }
            }
            bp[3] = bp[0];
        }

        /**
         * �l�p�`�ƒ��_�� index ��w�肵�� TriangleInfo ��?�?�����
         */
        TriangleInfo(BezierInfo bi, Point3D[] pnts,
                     int idx1, int idx2, int idx3) {
            PointOnSurface3D[] tmpbp = new PointOnSurface3D[3];
            int[] jx = {idx1, idx2, idx3};

            for (int i = 0; i < 3; i++) {
                int j = jx[i];
                tmpbp[i] = makePointOnSurface(bi, pnts[j],
                        uparam(j), vparam(j));
            }
            setupTriangle(tmpbp);
        }

        /**
         * �l�p�`��?k�ނ����ӂ� index ��w�肵�� TriangleInfo ��?�?�����
         */
        TriangleInfo(BezierInfo bi, Point3D[] pnts, int idx) {
            PointOnSurface3D[] tmpbp = new PointOnSurface3D[3];
            int j = 0;

            for (int i = 0; i < 4; i++) {
                if (i < idx) {
                    tmpbp[i] = makePointOnSurface(bi, pnts[i],
                            uparam(i), vparam(i));
                } else if (idx == i) {
                    Point3D p = pnts[i].linearInterpolate(pnts[i + 1], 0.5);
                    tmpbp[i] = makePointOnSurface(bi, p,
                            (uparam(i) + uparam(i + 1)) / 2.0,
                            (vparam(i) + vparam(i + 1)) / 2.0);
                    i++;
                } else {
                    tmpbp[i - 1] = makePointOnSurface(bi, pnts[i],
                            uparam(i), vparam(i));
                }
            }

            setupTriangle(tmpbp);
        }

        /**
         * �ӂƎO�p�`���ʓ��m�̌�?��?A��_��?�߂�?B
         * �ӂ����傤�ǌ�?�?�ɂ���Η��[�_��Ԃ�?A
         * ��_���Ȃ���� null ��Ԃ�
         */
        IntersectionPoint2D[] intersectSeg(Line2D seg, Line2D line) {
            if ((debugFlag & DEBUG_INTERSEC) != 0) {
                System.out.println("intersectSeg:enter");
                output_line(seg);
                output_line(line);
            }

            // IntersectionPoint2D ip = seg.intersect1Line(line);

            Point2D s1 = seg.coordinates(0.0);
            Point2D s2 = seg.coordinates(1.0);
            PointOnCurve2D p1 = line.project1From(s1);
            PointOnCurve2D p2 = line.project1From(s2);

            if (s1.distance2(p1) < dTol2 && s2.distance2(p2) < dTol2) {
                if ((debugFlag & DEBUG_INTERSEC) != 0) {
                    System.out.println("intersectSeg: dual");
                }
                IntersectionPoint2D[] ip = new IntersectionPoint2D[2];
                ip[0] = new IntersectionPoint2D(seg, 0.0,
                        line, p1.parameter(), GeometryElement.doCheckDebug);
                ip[1] = new IntersectionPoint2D(seg, 1.0,
                        line, p2.parameter(), GeometryElement.doCheckDebug);
                return ip;
            }

            Vector2D p = line.pnt().subtract(seg.pnt());
            double pa = p.zOfCrossProduct(seg.dir());
            double pb = p.zOfCrossProduct(line.dir());
            double o = seg.dir().zOfCrossProduct(line.dir());
            double param = pb / o;

            if (param < 0.0 || 1.0 < param) {
                if ((debugFlag & DEBUG_INTERSEC) != 0) {
                    System.out.println("intersectSeg: null");
                }
                return null;
            }

            IntersectionPoint2D[] ip = new IntersectionPoint2D[1];
            ip[0] = new IntersectionPoint2D(seg, param, line, pa / o, GeometryElement.doCheckDebug);

            if ((debugFlag & DEBUG_INTERSEC) != 0) {
                System.out.println("intersectSeg: param = " + param);
            }

            return ip;
        }

        /**
         * �O�p�`���ʓ��m�̌�?�ƎO�p�`�̌�_��?�߂�
         */
        IntersectPoint[] intersect(Line3D line3d) {
            int i;
            IntersectPoint[] rval = new IntersectPoint[2];
            Line2D line = new Line2D(line3d.pnt().to2D(trans),
                    line3d.dir().to2D(trans));
            IntersectionPoint2D[] ip = null;

            int ints_cnt = 0;
            for (i = 0; i < 3; i++) {
                if (seg2[i] == null)
                    continue;

                ip = intersectSeg(seg2[i], line);
                if (ip == null)
                    continue;
                else if (ip.length == 2) {
                    rval[0] = new IntersectPoint(bi.root, seg2[i], 0.0, line,
                            bp[i], bp[i + 1]);
                    rval[1] = new IntersectPoint(bi.root, seg2[i], 1.0, line,
                            bp[i], bp[i + 1]);
                    if ((debugFlag & DEBUG_INTERSEC) != 0) {
                        System.out.println("Infinite");
                    }
                    return rval;
                } else {
                    if (ints_cnt < 2)
                        rval[ints_cnt] = new IntersectPoint(bi.root, ip[0],
                                bp[i], bp[i + 1]);
                    ints_cnt++;
                }
            }
            if ((debugFlag & DEBUG_INTERSEC) != 0) {
                System.out.println("ints_cnt = " + ints_cnt);
            }

            switch (ints_cnt) {
                case 3:
                    if (rval[0].ip.identical(rval[1].ip))
                        rval[1] = new IntersectPoint(bi.root, ip[0], bp[2], bp[3]);
                    // thru down
                case 2:
                    return rval;
            }
            return null;
        }

        void sortIntersect2(IntersectPoint[] p) {
            if (p[0].lineParam() > p[1].lineParam()) {
                IntersectPoint tmp = p[0];
                p[0] = p[1];
                p[1] = tmp;
            }
        }

        /**
         * �O�p�`���ʓ��m�̌�?�Z�O�?���g��v�Z����
         */
        LineSegmentInfo intersectTriangles(TriangleInfo tB) {
            Line3D line;

            if ((debugFlag & DEBUG_INTERSEC) != 0) {
                System.out.println("intersectTriangles:");
                output(System.out);
                tB.output(System.out);
            }

            try {
                line = plane.intersect1Plane(tB.plane);
            } catch (IndefiniteSolutionException e) {
                return null;
            }

            IntersectPoint[] lA = this.intersect(line);
            if (lA == null)
                return null;

            IntersectPoint[] lB = tB.intersect(line);
            if (lB == null)
                return null;

            if ((debugFlag & DEBUG_INTERSEC) != 0) {
                System.out.println("found");
            }

            sortIntersect2(lA);
            sortIntersect2(lB);
            // now lA[0].lineParam() <= lA[1].lineParam()
            //  && lB[0].lineParam() <= lB[1].lineParam()

            if ((debugFlag & DEBUG_INTERSEC) != 0) {
                System.out.println("found");
                System.out.println("line A[" + lA[0].lineParam()
                        + " ... " + lA[1].lineParam() + "]");
                System.out.println("line B[" + lB[0].lineParam()
                        + " ... " + lB[1].lineParam() + "]");
            }

            if ((lA[1].lineParam() <= lB[0].lineParam()) ||
                    (lB[1].lineParam() <= lA[0].lineParam())) {
                // no duplicate
                return null;
            }

            PointOnSurface3D sA, sB, eA, eB;
            PointInfo sP, eP;

            if (lA[0].lineParam() < lB[0].lineParam()) {
                sB = lB[0].segPoint();
                sA = lB[0].interpPoint(lA[0], lA[1]);
                sP = new PointInfo(lB[0].segPoint(), sA, sB);
                if ((debugFlag & DEBUG_INTERSEC) != 0) {
                    System.out.println("start edge(B):");
                    lB[0].startP.literal().output(System.out);
                    lB[0].endP.literal().output(System.out);
                }
            } else {
                sA = lA[0].segPoint();
                sB = lA[0].interpPoint(lB[0], lB[1]);
                sP = new PointInfo(lA[0].segPoint(), sA, sB);
                if ((debugFlag & DEBUG_INTERSEC) != 0) {
                    System.out.println("start edge(A):");
                    lA[0].startP.literal().output(System.out);
                    lA[0].endP.literal().output(System.out);
                }
            }

            if (lA[1].lineParam() < lB[1].lineParam()) {
                eA = lA[1].segPoint();
                eB = lA[1].interpPoint(lB[0], lB[1]);
                eP = new PointInfo(lA[1].segPoint(), eA, eB);
                if ((debugFlag & DEBUG_INTERSEC) != 0) {
                    System.out.println("end edge(A):");
                    lA[1].startP.literal().output(System.out);
                    lA[1].endP.literal().output(System.out);
                }
            } else {
                eB = lB[1].segPoint();
                eA = lB[1].interpPoint(lA[0], lA[1]);
                eP = new PointInfo(lB[1].segPoint, eA, eB);
                if ((debugFlag & DEBUG_INTERSEC) != 0) {
                    System.out.println("end edge(B):");
                    lB[1].startP.literal().output(System.out);
                    lB[1].endP.literal().output(System.out);
                }
            }
            return new LineSegmentInfo(sP, eP);
        }
    }

    /**
     * Bezier�Ȗʂ̋ߎ����ʂ�\������Ք�N���X
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
        PureBezierCurve3D[] boundaryCurves;

        /*
        * flags for whether each of boundaries is linear or not
        */
        boolean[] boundaryCurveIsLine;

        /**
         * ?k�ނ��Ă��Ȃ��ӂ�?�
         */
        int edgeCount;

        /**
         * shapeInfo[i] = 1 if i-th edge is not reduced
         */
        int[] shapeInfo;

        /**
         * @param bzs Bezier �Ȗ�
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

            shapeInfo = new int[4];

            boundaryCurveIsLine = new boolean[4];
            boundaryCurves = new PureBezierCurve3D[4];

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
                edgeCount = iu0dir + iv0dir + iu1dir + iv1dir;
                shapeInfo[0] = iu0dir;
                shapeInfo[3] = iv0dir;
                shapeInfo[2] = iu1dir;
                shapeInfo[1] = iv1dir;

                switch (edgeCount) {
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
            } while (false);

            udir = udir.unitized();
            vdir = vdir.unitized();
            axis = new Axis2Placement3D(c00, udir.crossProduct(vdir), udir);
        }

        Point3D origin() {
            return axis.location();
        }

        Vector3D zaxis() {
            return axis.z();
        }
    }

    private static final int UNKNOWN = 0;
    private static final int BEZIER = 1;
    private static final int PLANER = 2;
    private static final int RECTANGULAR = 3;
    private static final int LINE = 4;
    private static final int POINT = 5;

    /**
     * Bezier �Ȗʂ�\������Ք�N���X
     */
    final private class BezierInfo {
        /**
         * �������ꂽ Bezier �Ȗ�
         */
        private PureBezierSurface3D bzs;

        /**
         * root �� Bezier �Ȗ�
         */
        private PureBezierSurface3D root;

        /**
         * ��� U �p���??[�^�̊J�n�l
         */
        private double u_sp;

        /**
         * ��� U �p���??[�^��?I���l
         */
        private double u_ep;

        /**
         * ��� V �p���??[�^�̊J�n�l
         */
        private double v_sp;

        /**
         * ��� V �p���??[�^��?I���l
         */
        private double v_ep;

        /**
         * �Ȗʂ�͂ޔ�
         */
        private EnclosingBox3D box;

        /**
         * ���C�o���̃��X�g
         */
        Vector rivals;

        /**
         * �ߎ�����?��
         */
        PlaneBezier pb;

        /**
         * �Ȗʂ̋ߎ�?��
         */
        int crnt_type;

        /**
         * �Ȗʂ̋ߎ��O�p�`
         */
        TriangleInfo[] tri;

        void output() {
            System.out.println("BezierInfo: min = ");
            box.min().literal().output(System.out);
            System.out.println("BezierInfo: max = ");
            box.max().literal().output(System.out);
            System.out.println("BezierInfo: ("
                    + u_sp + ", " + u_ep + "), (" +
                    +v_sp + ", " + v_ep + ")");
        }

        /**
         * �I�u�W�F�N�g��?�?�����
         */
        private BezierInfo(PureBezierSurface3D root,
                           PureBezierSurface3D bzs,
                           double u_sp, double u_ep,
                           double v_sp, double v_ep,
                           boolean hasRivals) {
            super();

            this.root = root;
            this.bzs = bzs;
            this.u_sp = u_sp;
            this.u_ep = u_ep;
            this.v_sp = v_sp;
            this.v_ep = v_ep;
            this.box = bzs.approximateEnclosingBox();

            if (hasRivals)
                this.rivals = new Vector();
            else
                this.rivals = null;
            this.crnt_type = UNKNOWN;
            this.pb = null;
            this.tri = null;
        }

        // Just like a IntsBzcBzc3D.BezierInfo.whatTypeIsBezier()
        /**
         * �Ȗʂ̋��E?�̋ߎ�?�Ԃ𒲂ׂ�
         */
        int whatTypeIsBezierCurve(PureBezierCurve3D bzc) {
            int uicp = bzc.nControlPoints();
            Vector3D s2c;
            Vector3D crsv;
            double leng;
            int i;

            Vector3D s2e =
                    bzc.controlPointAt(uicp - 1).subtract(bzc.controlPointAt(0));
            double norm_s2e = s2e.norm();
            double leng_s2e = Math.sqrt(norm_s2e);

            if (norm_s2e < dTol2) {
                for (i = 1; i < (uicp - 1); i++) {
                    s2c = bzc.controlPointAt(i)
                            .subtract(bzc.controlPointAt(0));
                    if (!(s2c.norm() < dTol2))
                        break;
                }

                if (i == (uicp - 1))
                    return POINT;
                else
                    return BEZIER;
            }

            Vector3D unit_s2e = s2e.divide(Math.sqrt(norm_s2e));

            for (i = 1; i < (uicp - 1); i++) {
                s2c = bzc.controlPointAt(i).subtract(bzc.controlPointAt(0));
                crsv = unit_s2e.crossProduct(s2c);
                if (crsv.norm() > dTol2)
                    return BEZIER;

                leng = unit_s2e.dotProduct(s2c);
                if ((leng < (0.0 - dTol)) || (leng > (leng_s2e + dTol)))
                    return BEZIER;
            }
            return LINE;
        }

        /**
         * �Ȗʂ̋ߎ�?�Ԃ𒲂ׂ�
         */
        int whatTypeIsBezier() {
            if (crnt_type != UNKNOWN)
                return crnt_type;

            int u_uicp = bzs.uNControlPoints();
            int v_uicp = bzs.vNControlPoints();
            PlaneBezier pb;
            int uicp;
            Vector3D evec;

            crnt_type = BEZIER;

            if (((u_ep - u_sp) > 0.5) || ((v_ep - v_sp) > 0.5)) {
                return crnt_type;
            }

            /*
            * make_refplane can change the way of making a plane with
            * bi's parameter rectangle, but it is a little dangerous
            * with freeform VS. freeform.
            */
            this.pb = pb = new PlaneBezier(bzs);
            Point3D org = pb.origin();
            Vector3D zaxis = pb.zaxis();

            /*
            * just return if Bezier is not planar
            */
            for (int j = 0; j < v_uicp; j++)
                for (int i = 0; i < u_uicp; i++) {
                    evec = bzs.controlPointAt(i, j).subtract(org);
                    if (Math.abs(evec.dotProduct(zaxis)) > dTol)
                        return crnt_type;
                }

            /*
            * Bezier is planar, so make the PureBezierCurve3D
            */
            crnt_type = POINT;

            /*
            * boundary curves
            */
            for (int i = 0; i < 4; i++) {
                if (pb.shapeInfo[i] == 0) {
                    pb.boundaryCurves[i] = null;
                    continue;
                }
                if (crnt_type == POINT)
                    crnt_type = RECTANGULAR;

                pb.boundaryCurves[i] = bzs.getBoundaryCurve(i);
                int type = whatTypeIsBezierCurve(pb.boundaryCurves[i]);

                if (type == LINE)
                    pb.boundaryCurveIsLine[i] = true;
                else {
                    pb.boundaryCurveIsLine[i] = false;
                    crnt_type = PLANER;
                }
            }
            return crnt_type;
        }
    }

    /**
     * �e�Z�O�?���g����?\?����ꂽ�_���\������N���X
     */
    final class PointList {
        /**
         * the number of points in it
         */
        int no;

        /**
         * Vector of points
         */
        Vector list;

        /**
         * sum of squared lengths
         * (NOT squared sum of lengths)
         */
        double leng2;

        PointList(CLInfo cl) {
            no = 2;
            list = new Vector();
            list.addElement(cl.ps);
            list.addElement(cl.pe);
            leng2 = cl.leng2;
        }

        PointInfo first() {
            return (PointInfo) list.elementAt(0);
        }

        PointInfo last() {
            return (PointInfo) list.lastElement();
        }

        PointInfo first(int n) {
            return (PointInfo) list.elementAt(n);
        }

        PointInfo last(int n) {
            int size = list.size();
            return (PointInfo) list.elementAt(size - 1 - n);
        }

        void prepend(PointInfo p, double leng2) {
            list.insertElementAt(p, 0);
            this.leng2 += leng2;
            no++;
        }

        void append(PointInfo p, double leng2) {
            list.addElement(p);
            this.leng2 += leng2;
            no++;
        }

        void removeFirst() {
            list.removeElementAt(0);
            no--;
        }

        void removeLast() {
            int size = list.size();
            list.removeElementAt(size - 1);
            no--;
        }

        void connect(PointList pl, boolean front, boolean rev) {
            PointInfo p1;
            PointInfo p2;

            no += (pl.no - 1);
            this.leng2 += pl.leng2;
            Vector olist = list;

            if (front)
                list = new Vector();

            if (rev) {
                for (int i = pl.list.size() - 1; 0 <= i; i--)
                    list.addElement(pl.list.elementAt(i));
            } else {
                for (int i = 0; i < pl.list.size() - 1; i++)
                    list.addElement(pl.list.elementAt(i));
            }

            if (front) {
                for (int i = 0; i < olist.size(); i++)
                    list.addElement(olist.elementAt(i));

            }
        }

        boolean is_closed() {
            double upara_gap;
            double vpara_gap;
            double Apara_gap;
            double Bpara_gap;
            double AuPTol;
            double AvPTol;
            double BuPTol;
            double BvPTol;
            double pTol2;

            if (no <= 3)
                return false;
            if (leng2 < dTol2)
                return false;

            PointInfo pspnt = (PointInfo) list.firstElement();
            PointInfo pepnt = (PointInfo) list.lastElement();

            upara_gap = pspnt.Aupara - pepnt.Aupara;
            vpara_gap = pspnt.Avpara - pepnt.Avpara;
            Apara_gap = (upara_gap * upara_gap) + (vpara_gap * vpara_gap);
            AuPTol = (pspnt.AuPTol + pepnt.AuPTol) / 2.0;
            AvPTol = (pspnt.AvPTol + pepnt.AvPTol) / 2.0;
            pTol2 = (AuPTol * AuPTol) + (AvPTol * AvPTol);

            if (Apara_gap > pTol2)
                return false;

            upara_gap = pspnt.Bupara - pepnt.Bupara;
            vpara_gap = pspnt.Bvpara - pepnt.Bvpara;
            Bpara_gap = (upara_gap * upara_gap) + (vpara_gap * vpara_gap);
            BuPTol = (pspnt.BuPTol + pepnt.BuPTol) / 2.0;
            BvPTol = (pspnt.BvPTol + pepnt.BvPTol) / 2.0;
            pTol2 = (BuPTol * BuPTol) + (BvPTol * BvPTol);

            if (Bpara_gap > pTol2)
                return false;

            if (pspnt.pnt.distance2(pepnt.pnt) > dTol2)
                return false;

            if ((debugFlag & DEBUG_CONNECT) != 0) {
                System.out.println("closed:");
            }

            return true;
        }
    }

    // various information for connecting intersection segments
    // (this is used as temporary area)
    final class CLInfo {
        PointInfo p1;        /* front point of primary segment */
        PointInfo p2;        /* rear  point of primary segment */
        PointInfo ps;        /* first point of current intersection */
        PointInfo pe;        /* last  point of current intersection */
        int dir;        /* which point is connected (START if ps, END  if pe) */
        int cpnt;        /* which point is connected (PNT1  if p1, PNT2 if p2) */
        boolean found;        /* whether the connection is found or not */
        boolean is_consistent;    /* whether the primary segment is consistent or not */
        double leng2;        /* squared length of the primary segment */

        GapInfo gi;
        boolean severe_retry_criterion;

        void output() {
            System.out.println("CLInfo: primary");
            p1.pnt.literal().output(System.out);
            p2.pnt.literal().output(System.out);
            System.out.println("CLInfo: total");
            ps.pnt.literal().output(System.out);
            pe.pnt.literal().output(System.out);
        }
    }

    /* enumeration for judgement of which side of plane a point lies on */
    static final int ONPLANE = 0;
    static final int FORESIDE = 1;
    static final int BACKSIDE = 2;

    /* which end point of linesegment */
    static final int START = 0;
    static final int END = 1;
    static final int PNT1 = 1;
    static final int PNT2 = 2;

    static final int SS = 0;
    static final int EE = 1;
    static final int ES = 2;
    static final int SE = 3;

    /**
     * determine which side of plane a point is in
     *
     * @param pnt AbstractPoint
     * @param org PointOnPlane
     * @param zax Axis of Plane
     * @return 0 : pnt is on plane
     *         1 : pnt is foreside of plane
     *         2 : pnt is backside of plane
     */
    int sidePointPlane(Point3D pnt, Point3D org, Vector3D zax) {
        Vector3D vec = pnt.subtract(org);
        double zval = vec.dotProduct(zax);

        if (zval > dTol)
            return FORESIDE;
        else if (zval < -dTol)
            return BACKSIDE;
        else
            return ONPLANE;
    }

    /**
     * ��� Bezier �Ȗʂ���?�����\?������邩�ǂ�����?�����
     */
    boolean checkInterfere(BezierInfo dA, BezierInfo dB) {
        Point3D org;
        Vector3D zax;
        int pside, cside;
        int i, j;
        PlaneBezier pb;
        PureBezierSurface3D bzs;

        if ((dA.box.min().x() > (dB.box.max().x() + dTol)) ||
                (dA.box.min().y() > (dB.box.max().y() + dTol)) ||
                (dA.box.min().z() > (dB.box.max().z() + dTol)) ||
                (dB.box.min().x() > (dA.box.max().x() + dTol)) ||
                (dB.box.min().y() > (dA.box.max().y() + dTol)) ||
                (dB.box.min().z() > (dA.box.max().z() + dTol)))
            return false;    /* no interfere */

        if (dB.pb == null && dA.pb != null) {
            // dA is planar & dB is not planar
            pb = dA.pb;
            bzs = dB.bzs;
        } else if (dA.pb == null && dB.pb != null) {
            //  dB is planar & dA is not planar
            pb = dB.pb;
            bzs = dA.bzs;
        } else {
            return true;
        }

        org = pb.origin();
        zax = pb.zaxis();
        int k = 0;
        pside = ONPLANE;
        for (j = 0; j < bzs.vNControlPoints(); j++) {
            for (i = 0; i < bzs.uNControlPoints(); i++) {
                cside = sidePointPlane(bzs.controlPointAt(i, j), org, zax);
                if (k++ == 0) {
                    pside = cside;
                    if (pside == ONPLANE)
                        return true;    /* interfere */
                } else {
                    if (pside != cside)
                        return true;    /* interfere */
                }
            }
        }
        return false;        /* no interfere */
    }

    /**
     * �m?[�h�ɑΉ�����Ȗʂ̕����� new_rivals �ɉB���?B
     * �������K�v�Ȃ番����?s��?A�q�m?[�h��?�?�����?B
     * �Ȗʂ��l�p�`�ߎ��ł���̂ł����?A���������� new_rivals �ɉB���?B
     */
    private boolean divideRivals(QuadTree.Node dANode, Vector new_rivals) {
        BezierInfo bi00, bi01, bi10, bi11;
        QuadTree.Node bin00, bin01, bin10, bin11;

        double half_point = 0.5;
        double ug_half, vg_half;

        if ((dANode.child(0) == null) &&
                (dANode.child(1) == null) &&
                (dANode.child(2) == null) &&
                (dANode.child(3) == null)) {

            //  rival is leaf
            BezierInfo bi = (BezierInfo) dANode.data();
            bi.whatTypeIsBezier();

            if (bi.crnt_type == RECTANGULAR) {
                new_rivals.addElement(dANode);
                return false;
            }

            //  subdivide rival
            PureBezierSurface3D[] bzsx = bi.bzs.vDivide(half_point);
            PureBezierSurface3D[] bzs0 = bzsx[0].uDivide(half_point);
            PureBezierSurface3D[] bzs1 = bzsx[1].uDivide(half_point);

            ug_half = (bi.u_sp + bi.u_ep) / 2.0;
            vg_half = (bi.v_sp + bi.v_ep) / 2.0;

            bi00 = new BezierInfo(bi.root, bzs0[0], bi.u_sp, ug_half,
                    bi.v_sp, vg_half, false);
            bi10 = new BezierInfo(bi.root, bzs1[0], bi.u_sp, ug_half,
                    vg_half, bi.v_ep, false);
            bi11 = new BezierInfo(bi.root, bzs1[1], ug_half, bi.u_ep,
                    vg_half, bi.v_ep, false);
            bi01 = new BezierInfo(bi.root, bzs0[1], ug_half, bi.u_ep,
                    bi.v_sp, vg_half, false);

            bin00 = dANode.makeChild(0, bi00);
            bin01 = dANode.makeChild(1, bi01);
            bin11 = dANode.makeChild(2, bi11);
            bin10 = dANode.makeChild(3, bi10);
        } else {
            bin00 = dANode.child(0);
            bin01 = dANode.child(1);
            bin11 = dANode.child(2);
            bin10 = dANode.child(3);
        }

        new_rivals.addElement(bin00);
        new_rivals.addElement(bin01);
        new_rivals.addElement(bin11);
        new_rivals.addElement(bin10);

        return true;
    }

    // assume pb.edgeCount == 4
    /**
     * �l�p�`��O�p�`�ߎ�����̂ɓK?؂ȑΊp?��v�Z����
     */
    /*
    int getDiag(BezierInfo bi, Point3D[] pnts) {
    Vector3D[] edges = new Vector3D[4];
    Vector3D crvec;
    double[] crv = new double[4];
    double crvalue;
    PlaneBezier pb = bi.pb;
    Vector3D zaxis = pb.axis.z();
    int i, plus_cnt;

    for (i = 0; i < 4; i++)
        edges[i] = pnts[(i+1)%4].subtract(pnts[i]).unitized();

    plus_cnt = 0;
    for (i = 0; i < 4; i++) {
        crvec = edges[i].crossProduct(edges[(i+1)%4]);
        crv[(i+1)%4] = crvalue = crvec.dotProduct(zaxis);
        if (crvalue >= 0.0)
        plus_cnt++;
    }
    switch(plus_cnt) {
    case 4:
    case 0:
        // �����̒Z���Ίp?�
        break;
    case 1:
        // �p�x 180?���?�̒��_ (crv[i] > 0.0)
        break;
    case 2:
        // ?u8?v�̎��^... XXX
        break;
    case 3:
        // �p�x 180?���?�̒��_ (crv[i] < 0.0)
        break;
    }
    return 0;		// XXX stub
    }
    */
    int getDiag(BezierInfo bi, Point3D[] pnts) {
        return 0;        // XXX stub
    }

    /**
     * �ߎ��O�p�`��Ԃ�
     */
    TriangleInfo[] makeTriangles(BezierInfo bi) {
        int edgeCount = bi.pb.edgeCount;

        if (edgeCount == 4) {
            Point3D[] pnts = new Point3D[4];

            for (int i = 0; i < 4; i++)
                pnts[i] = bi.pb.boundaryCurves[i].controlPointAt(0);

            int n = getDiag(bi, pnts);

            TriangleInfo[] tri = new TriangleInfo[2];
            if (n == 0) {
                tri[0] = new TriangleInfo(bi, pnts, 0, 1, 2);
                tri[1] = new TriangleInfo(bi, pnts, 2, 3, 0);
            } else {
                tri[0] = new TriangleInfo(bi, pnts, 0, 1, 3);
                tri[1] = new TriangleInfo(bi, pnts, 1, 2, 3);
            }
            return tri;
        } else if (edgeCount == 3) {
            Point3D[] pnts = new Point3D[4];
            int i;

            for (i = 0; i < 4; i++)
                pnts[i] = bi.pb.boundaryCurves[i].controlPointAt(0);

            TriangleInfo[] tri = new TriangleInfo[1];

            for (i = 0; i < 4; i++) {
                if (bi.pb.shapeInfo[i] == 0)
                    break;
            }
            tri[0] = new TriangleInfo(bi, pnts, i);
            return tri;
        } else
            return null;
    }

    void addLineseg(LineSegmentInfo li) {
        for (Cursor e = solutionSegs.cursor(); e.hasMoreElements();) {
            LineSegmentInfo li2 = (LineSegmentInfo) e.nextElement();
            if (li2.isSameLineseg(li))
                break;
        }
        solutionSegs.addElement(li);
    }

    /**
     * ��̋ߎ��l�p�`�̌�?�Z�O�?���g��v�Z����
     */
    void intersectRectangle(BezierInfo dA, BezierInfo dB) {
        if (dA.tri == null)
            dA.tri = makeTriangles(dA);
        if (dB.tri == null)
            dB.tri = makeTriangles(dB);

        for (int i = 0; i < dA.tri.length; i++)
            for (int j = 0; j < dB.tri.length; j++) {
                LineSegmentInfo li = dA.tri[i].intersectTriangles(dB.tri[j]);
                if (li != null)
                    addLineseg(li);
            }
    }

    /**
     * Bezier ��?�m�̌�?�Z�O�?���g��v�Z����
     */
    void getIntersections(BezierInfo currentBI, int level) {
        PureBezierSurface3D bzs00, bzs01, bzs10, bzs11;
        BezierInfo bi00, bi01, bi10, bi11;
        double half_point = 0.5;
        double ug_half, vg_half;
        int ret_val;
        int i;
        QuadTree.Node dANode;

        /*
        * is there some interferes ?
        */
        int n_rivals = currentBI.rivals.size();
        for (i = n_rivals - 1; i >= 0; i--) {
            dANode = (QuadTree.Node) currentBI.rivals.elementAt(i);
            if (!checkInterfere((BezierInfo) dANode.data(), currentBI))
                currentBI.rivals.removeElementAt(i);
        }
        if (currentBI.rivals.size() == 0)
            return;

        if ((debugFlag & DEBUG_DIVIDE) != 0) {
            System.out.println("getIntersections: level = " + level);
            currentBI.output();
            n_rivals = currentBI.rivals.size();
            for (i = n_rivals - 1; i >= 0; i--) {
                dANode = (QuadTree.Node) currentBI.rivals.elementAt(i);
                BezierInfo bi = (BezierInfo) dANode.data();
                System.out.println("getIntersections: rival");
                bi.output();
            }
        }

        // is current bezier regarded as plane ?
        currentBI.whatTypeIsBezier();

        /*
        * if current bezier is regarded as rectangular, get intersection.
        */
        if (currentBI.crnt_type == RECTANGULAR) {
            Vector new_rivals = new Vector();
            boolean all_rivals_are_plane = true;

            n_rivals = currentBI.rivals.size();
            for (i = 0; i < n_rivals; i++)
                if (divideRivals((QuadTree.Node) currentBI.rivals.elementAt(i),
                        new_rivals))
                    all_rivals_are_plane = false;

            currentBI.rivals = new_rivals;

            if (!all_rivals_are_plane) {
                /*
                * try again
                */
                getIntersections(currentBI, level + 1);
            } else {
                /*
                * get intersections
                */
                n_rivals = currentBI.rivals.size();
                for (i = 0; i < n_rivals; i++) {
                    dANode = (QuadTree.Node) currentBI.rivals.elementAt(i);
                    intersectRectangle((BezierInfo) dANode.data(), currentBI);
                }
            }
            return;
        }

        /*
        * if current bezier is NOT regarded as rectangular,
        * generate children (divide current bezier).
        */
        ug_half = (currentBI.u_sp + currentBI.u_ep) / 2.0;
        vg_half = (currentBI.v_sp + currentBI.v_ep) / 2.0;

        PureBezierSurface3D[] bzsx = currentBI.bzs.vDivide(half_point);
        PureBezierSurface3D[] bzs0 = bzsx[0].uDivide(half_point);
        PureBezierSurface3D[] bzs1 = bzsx[1].uDivide(half_point);

        bi00 = new BezierInfo(currentBI.root, bzs0[0], currentBI.u_sp, ug_half,
                currentBI.v_sp, vg_half, true);
        bi10 = new BezierInfo(currentBI.root, bzs1[0], currentBI.u_sp, ug_half,
                vg_half, currentBI.v_ep, true);
        bi11 = new BezierInfo(currentBI.root, bzs1[1], ug_half, currentBI.u_ep,
                vg_half, currentBI.v_ep, true);
        bi01 = new BezierInfo(currentBI.root, bzs0[1], ug_half, currentBI.u_ep,
                currentBI.v_sp, vg_half, true);

        /*
        * create children's rival list
        */
        n_rivals = currentBI.rivals.size();
        for (i = 0; i < n_rivals; i++)
            divideRivals((QuadTree.Node) currentBI.rivals.elementAt(i), bi00.rivals);

        n_rivals = bi00.rivals.size();
        for (i = 0; i < n_rivals; i++) {
            bi01.rivals.addElement(bi00.rivals.elementAt(i));
            bi11.rivals.addElement(bi00.rivals.elementAt(i));
            bi10.rivals.addElement(bi00.rivals.elementAt(i));
        }

        /*
        * recursive call
        */
        getIntersections(bi00, level + 1);
        getIntersections(bi01, level + 1);
        getIntersections(bi11, level + 1);
        getIntersections(bi10, level + 1);
    }

    // which boundary of surface
    static final int LOWER = 0;
    static final int UPPER = 1;

    static final int Wside_START = 0;
    static final int Wside_END = 2;

    /**
     * �Ȗʂ̋��E��\������Ք�N���X
     */
    final class BoundaryInfo {
        int wend;
        int param;
        boolean is_boundary;
        PointInfo pi;

        void setBoundaryInfo(double gap) {
            double g;

            for (int i = Wp_START; i < Wp_END; i++) {
                for (int j = Wside_START; j < Wside_END; j++) {
                    g = pi.getBoundaryGap(i, j);
                    if (g < gap && param != i) {
                        gap = g;
                        param = i;
                        wend = j;
                    }
                }
            }
        }

        BoundaryInfo(int ref_param, PointInfo pi) {
            param = ref_param;
            is_boundary = true;
            this.pi = pi;

            setBoundaryInfo(pTol);
        }

        void adjust() {
            switch (param) {
                case Wp_Au:
                    pi.Aupara = (wend == LOWER) ? 0.0 : 1.0;
                    break;
                case Wp_Av:
                    pi.Avpara = (wend == LOWER) ? 0.0 : 1.0;
                    break;
                case Wp_Bu:
                    pi.Bupara = (wend == LOWER) ? 0.0 : 1.0;
                    break;
                case Wp_Bv:
                    pi.Bvpara = (wend == LOWER) ? 0.0 : 1.0;
                    break;
                default:
                    is_boundary = false;
                    break;
            }
        }
    }

    /**
     * �_�̃��X�g�����S�ȋ�?�𔻒肷��?B
     */
    boolean isCompleteCurve(PointList plist, boolean addToSolutionCurves) {
        BoundaryInfo bis, bie, bis2, bie2;

        if (plist.is_closed()) {
            if (addToSolutionCurves)
                solutionCurves.addElement(plist);
            return true;
        }

        bis = new BoundaryInfo(Wp_No, plist.first());
        bis.adjust();
        bie = new BoundaryInfo(Wp_No, plist.last());
        bie.adjust();

        if (bis.is_boundary && bie.is_boundary) {
            if (addToSolutionCurves)
                solutionCurves.addElement(plist);
            return true;
        }

        // neighbours are on boundary?
        if (plist.no > 3 && addToSolutionCurves) {
            if (!bis.is_boundary) {
                bis2 = new BoundaryInfo(Wp_No, plist.first(1));
                bis2.adjust();
                if (bis2.is_boundary) {
                    plist.removeFirst();
                    bis = bis2;
                }
            }

            if (!bie.is_boundary) {
                bie2 = new BoundaryInfo(Wp_No, plist.last(1));
                bie2.adjust();
                if (bie2.is_boundary) {
                    plist.removeLast();
                    bie = bie2;
                }
            }

            if (bis.is_boundary && bie.is_boundary) {
                if (addToSolutionCurves)
                    solutionCurves.addElement(plist);
                return true;
            }
        }
        return false;
    }

    final class NListInfo {
        int plist_open_side;
        double nlist_dist2;
        PointList nlist;
        int nlist_connect_side;
    }

    boolean isSemiCompleteCurve(PointList plist, NListInfo nli) {
        BoundaryInfo bi;

        bi = new BoundaryInfo(Wp_No, plist.first());
        if (bi.param != Wp_No) {
            nli.plist_open_side = END;
            return true;
        }

        bi = new BoundaryInfo(Wp_No, plist.last());
        if (bi.param != Wp_No) {
            nli.plist_open_side = START;
            return true;
        }

        return false;
    }

    void compareNListDist(PointList clist, PointList plist, NListInfo nli) {
        PointInfo cspnt = clist.first();
        PointInfo cepnt = clist.last();
        double csdist2, cedist2;
        BoundaryInfo bi;

        if (clist == plist)
            return;

        PointInfo popnt =
                (nli.plist_open_side == START) ? plist.first() : plist.last();

        bi = new BoundaryInfo(Wp_No, cspnt);
        if (bi.param == Wp_No) {
            csdist2 = popnt.pnt.distance2(cspnt.pnt);
            if (csdist2 < nli.nlist_dist2) {
                nli.nlist_dist2 = csdist2;
                nli.nlist = clist;
                nli.nlist_connect_side = START;
            }
        }

        bi = new BoundaryInfo(Wp_No, cepnt);
        if (bi.param == Wp_No) {
            cedist2 = popnt.pnt.distance2(cepnt.pnt);
            if (cedist2 < nli.nlist_dist2) {
                nli.nlist_dist2 = cedist2;
                nli.nlist = clist;
                nli.nlist_connect_side = END;
            }
        }
        return;
    }

    boolean moveToSolutionCurves(PointList plist) {
        PointInfo pspnt = plist.first();
        PointInfo pepnt = plist.last();

        if (plist.is_closed()) {
            solutionCurves.addElement(plist);
            return true;
        }

        BoundaryInfo bi;

        bi = new BoundaryInfo(Wp_No, pspnt);
        bi.adjust();
        if (!bi.is_boundary)
            return false;

        bi = new BoundaryInfo(Wp_No, pepnt);
        bi.adjust();
        if (!bi.is_boundary)
            return false;

        solutionCurves.addElement(plist);
        return true;
    }

    final class MListInfo {
        int mlist_no;
        PointList list;

        MListInfo() {
            mlist_no = 0;
        }
    }

    void compareMListLength(PointList plist, MListInfo mi) {
        if (plist.no > mi.mlist_no) {
            mi.mlist_no = plist.no;
            mi.list = plist;
        }
    }

    void moveMListToSolutionCurves(PointList plist, MListInfo mi) {
        PointInfo pspnt = plist.first();
        PointInfo pepnt = plist.last();
        BoundaryInfo bi;

        if (plist.is_closed()) {
            solutionCurves.addElement(plist);
            return;
        }

        bi = new BoundaryInfo(Wp_No, pspnt);
        bi.adjust();

        bi = new BoundaryInfo(Wp_No, pepnt);
        bi.adjust();

        solutionCurves.addElement(plist);
    }

    void getFirstNode(LineSegmentInfo ls_info, CLInfo c_info) {
        c_info.p1 = ls_info.p1;
        c_info.p2 = ls_info.p2;
        c_info.found = true;
    }

    final class GapInfo {
        boolean gap_rev;
        double gap;
        double gap_Au;
        double gap_Av;
        double gap_Bu;
        double gap_Bv;
        PointInfo gap_pi1;
        PointInfo gap_pi2;
        double dist2;

        GapInfo() {
        }
    }

    boolean areConnected(PointInfo pi1, PointInfo pi2, GapInfo gi) {
        double Aupara_gap = pi1.Aupara - pi2.Aupara;
        double Avpara_gap = pi1.Avpara - pi2.Avpara;
        double Bupara_gap = pi1.Bupara - pi2.Bupara;
        double Bvpara_gap = pi1.Bvpara - pi2.Bvpara;
        double Apara_gap = (Aupara_gap * Aupara_gap)
                + (Avpara_gap * Avpara_gap);
        double Bpara_gap = (Bupara_gap * Bupara_gap)
                + (Bvpara_gap * Bvpara_gap);
        double para_gap = Apara_gap + Bpara_gap;
        double AuPTol = (pi1.AuPTol + pi2.AuPTol) / 2.0;
        double AvPTol = (pi1.AvPTol + pi2.AvPTol) / 2.0;
        double BuPTol = (pi1.BuPTol + pi2.BuPTol) / 2.0;
        double BvPTol = (pi1.BvPTol + pi2.BvPTol) / 2.0;
        double APTol = (AuPTol * AuPTol) + (AvPTol * AvPTol);
        double BPTol = (BuPTol * BuPTol) + (BvPTol * BvPTol);

        if (!gi.gap_rev || (gi.gap > para_gap)) {
            gi.gap_rev = true;
            gi.gap = para_gap;
            gi.gap_Au = Aupara_gap;
            gi.gap_Av = Avpara_gap;
            gi.gap_Bu = Bupara_gap;
            gi.gap_Bv = Bvpara_gap;
            gi.gap_pi1 = pi1;
            gi.gap_pi2 = pi2;
        }

        gi.dist2 = pi1.pnt.distance2(pi2.pnt);
        if ((Apara_gap > APTol) || (Bpara_gap > BPTol))
            return false;

        if (gi.dist2 > dTol2)
            return false;
        return true;
    }

    boolean are_min_para_gap_points(PointInfo pi1, PointInfo pi2, GapInfo gi) {
        if ((pi1 == gi.gap_pi1) && (pi2 == gi.gap_pi2)) {
            gi.dist2 = pi1.pnt.distance2(pi2.pnt);
            return true;
        }
        return false;
    }

    boolean getNextNode1(LineSegmentInfo ls_info, CLInfo c_info) {
        double dist2;
        GapInfo gi = c_info.gi;

        // judge with distance & parameter

        // start vs p1
        if (areConnected(c_info.ps, ls_info.p1, c_info.gi)) {
            c_info.dir = START;
            c_info.cpnt = PNT1;
            dist2 = gi.dist2;
            if (areConnected(c_info.ps, ls_info.p2, c_info.gi)) {
                if (gi.dist2 < dist2)
                    c_info.cpnt = PNT2;
            }
            c_info.found = true;
            return true;
        }

        // start vs p2
        if (areConnected(c_info.ps, ls_info.p2, c_info.gi)) {
            c_info.dir = START;
            c_info.cpnt = PNT2;
            c_info.found = true;
            return true;
        }

        // end vs p1
        if (areConnected(c_info.pe, ls_info.p1, c_info.gi)) {
            c_info.dir = END;
            c_info.cpnt = PNT1;
            dist2 = gi.dist2;
            if (areConnected(c_info.pe, ls_info.p2, c_info.gi)) {
                if (gi.dist2 < dist2)
                    c_info.cpnt = PNT2;
            }
            c_info.found = true;
            return true;
        }

        // end vs p2
        if (areConnected(c_info.pe, ls_info.p2, c_info.gi)) {
            c_info.dir = END;
            c_info.cpnt = PNT2;
            c_info.found = true;
            return true;
        }

        return false;
    }

    boolean getNextNode(LineSegmentInfo ls_info, CLInfo c_info) {
        if (!getNextNode1(ls_info, c_info) && !c_info.found)
            return false;

        c_info.p1 = ls_info.p1;
        c_info.p2 = ls_info.p2;
        c_info.leng2 = ls_info.leng2;
        return true;
    }

    boolean retryGetNextNode1(LineSegmentInfo ls_info, CLInfo c_info) {
        if (are_min_para_gap_points(c_info.ps, ls_info.p1, c_info.gi)) {
            c_info.dir = START;
            c_info.cpnt = PNT1;
            c_info.found = true;
            return true;
        }

        if (are_min_para_gap_points(c_info.ps, ls_info.p2, c_info.gi)) {
            c_info.dir = START;
            c_info.cpnt = PNT2;
            c_info.found = true;
            return true;
        }

        if (are_min_para_gap_points(c_info.pe, ls_info.p1, c_info.gi)) {
            c_info.dir = END;
            c_info.cpnt = PNT1;
            c_info.found = true;
            return true;
        }

        if (are_min_para_gap_points(c_info.pe, ls_info.p2, c_info.gi)) {
            c_info.dir = END;
            c_info.cpnt = PNT2;
            c_info.found = true;
            return true;
        }

        return false;
    }

    boolean retryGetNextNode(LineSegmentInfo ls_info, CLInfo c_info) {
        double[] p_gap = new double[4];

        if (!retryGetNextNode1(ls_info, c_info) && !c_info.found)
            return false;

        p_gap[0] = Math.abs(c_info.gi.gap_Au);
        p_gap[1] = Math.abs(c_info.gi.gap_Av);
        p_gap[2] = Math.abs(c_info.gi.gap_Bu);
        p_gap[3] = Math.abs(c_info.gi.gap_Bv);

        if (c_info.severe_retry_criterion) {
            /*
            * regular jedgement
            */
            /* XXX
            if (!param_gaps_are_tolerable(p_gap)) {
            c_info.found = false;
            return false;
            }
            */

            /*
            * we do not want to check with 3D distance. but if we do not it,
            * there is a fear that 2 distinct, but (parametrically) close,
            * intersections are considered as 1 intersection.
            */
            /*
            if (dist > dTol) {
            c_info.found = false;
            return false;
            }
            */
        } else {
            /*
            * relaxed jedgement
            */
            if (((p_gap[0] > pTol)) && ((p_gap[1] > pTol)) &&
                    ((p_gap[2] > pTol)) && ((p_gap[3] > pTol))) {
                c_info.found = false;
                return false;
            }
        }

        c_info.p1 = ls_info.p1;
        c_info.p2 = ls_info.p2;
        c_info.leng2 = ls_info.leng2;

        return true;
    }

    void searchNext(CLInfo c_info, int level) {
        boolean v;
        LineSegmentInfo ls_info;
        boolean found = false;

        for (Cursor e = solutionSegs.cursor(); e.hasMoreElements();) {
            ls_info = (LineSegmentInfo) e.nextElement();

            switch (level) {
                case 0:
                    found = getNextNode(ls_info, c_info);
                    break;
                case 1:
                    c_info.severe_retry_criterion = true;
                    found = retryGetNextNode(ls_info, c_info);
                    break;
                case 2:
                    c_info.severe_retry_criterion = false;
                    found = retryGetNextNode(ls_info, c_info);
                    break;
            }

            if (found) {
                e.removePrevElement();
                return;
            }
        }
        return;
    }

    final PointInfo furtherPoint(CLInfo c) {
        return (c.cpnt == PNT1) ? c.p2 : c.p1;
    }

    boolean shouldRetry(PointList plist, GapInfo gi) {
        if (gi.gap_rev) {
            /*
            * we should retry if
            *	+ gi.gap is small and plist is open (even if plist is complete,
            *	  it may go along the boundary of surface), or
            *	+ plist is still NOT complete
            */
            if ((gi.gap < pTol) && (plist.is_closed() != true))
                return true;

            if (!isCompleteCurve(plist, false))
                return true;
        }
        return false;
    }

    boolean is_min_para_gap_pi1_bound(GapInfo gi) {
        BoundaryInfo bi = new BoundaryInfo(Wp_No, gi.gap_pi1);
        if (bi.param == Wp_No)
            return false;
        else
            return true;
    }

    void connectIntersections() {
        PointInfo pspnt, pepnt;
        Cursor se;
        Enumeration e;
        PointList plist;
        LineSegmentInfo ls_info;

        // find connections
        ObjectVector pre_solutionCurves = new ObjectVector();
        CLInfo c_info = new CLInfo();
        c_info.gi = new GapInfo();
        GapInfo gi = c_info.gi;

        if ((debugFlag & DEBUG_CONNECT) != 0) {
            System.out.println("solutionSegs");
            int i = 0;
            for (se = solutionSegs.cursor(); se.hasMoreElements();) {
                LineSegmentInfo li = (LineSegmentInfo) se.nextElement();
                System.out.println("LineSegmentInfo[" + i + "]");
                li.p1.pnt.literal().output(System.out);
                li.p2.pnt.literal().output(System.out);
                if (li.is_main_line)
                    System.out.println("main_line");
                i++;
            }
        }

        while (true) {
            // get 1st node
            c_info.found = false;
            c_info.leng2 = 0.0;
            if (solutionSegs.size() > 1) {
                for (se = solutionSegs.cursor(); se.hasMoreElements();) {
                    ls_info = (LineSegmentInfo) se.nextElement();
                    if (ls_info.leng2 >= dTol2) {
                        c_info.leng2 = ls_info.leng2;
                        getFirstNode(ls_info, c_info);
                        se.removePrevElement();
                        break;
                    }
                }
            }
            if (!c_info.found) {
                if (solutionSegs.size() == 0)
                    break;
                else {
                    ls_info = (LineSegmentInfo) solutionSegs.elementAt(0);
                    solutionSegs.removeElementAt(0);
                    getFirstNode(ls_info, c_info);
                }
            }

            c_info.ps = c_info.p1;
            c_info.pe = c_info.p2;

            if ((debugFlag & DEBUG_CONNECT) != 0) {
                System.out.println("getFirstNode:");
                c_info.output();
            }

            // make a new list
            plist = new PointList(c_info);

            while (true) {
                // get next node
                c_info.found = false;
                gi.gap_rev = false; // XXX GapInfo
                searchNext(c_info, 0);

                if (!c_info.found) {
                    if ((debugFlag & DEBUG_CONNECT) != 0) {
                        System.out.println("not found:");
                        c_info.output();
                        System.out.println("gap = " + c_info.gi.gap);
                        System.out.println("dist = " + Math.sqrt(c_info.gi.dist2));
                    }

                    pspnt = c_info.ps;
                    pepnt = c_info.pe;

                    // get tolerable node as next

                    if (shouldRetry(plist, gi)) {
                        if ((debugFlag & DEBUG_CONNECT) != 0) {
                            System.out.println("retry:");
                        }
                        searchNext(c_info, 1);
                        if (!c_info.found && !is_min_para_gap_pi1_bound(gi))
                            searchNext(c_info, 2);
                    }

                    if (!c_info.found && !isCompleteCurve(plist, false)) {
                        searchNext(c_info, 0);
                        if (!c_info.found && shouldRetry(plist, gi)) {
                            searchNext(c_info, 1);
                            if (!c_info.found && !is_min_para_gap_pi1_bound(gi))
                                searchNext(c_info, 2);
                        }
                    }

                    if (!c_info.found) {
                        if ((debugFlag & DEBUG_CONNECT) != 0) {
                            System.out.println("no more:");
                            c_info.output();
                        }
                        // no more connected node
                        pre_solutionCurves.addElement(plist);
                        break;
                    }
                }

                if ((debugFlag & DEBUG_CONNECT) != 0) {
                    System.out.println("searchNext:");
                    c_info.output();
                }

                // extend the list
                if (c_info.dir == START) {
                    pspnt = c_info.ps = furtherPoint(c_info);
                    plist.prepend(pspnt, c_info.leng2);
                } else {
                    pepnt = c_info.pe = furtherPoint(c_info);
                    plist.append(pepnt, c_info.leng2);
                }
            }
        }

        // pre_solutionCurves -> solutionCurves
        while (pre_solutionCurves.size() > 1) {
            // move complete curves into 'solutionCurves'
            for (se = pre_solutionCurves.cursor(); se.hasMoreElements();) {
                plist = (PointList) se.nextElement();
                if (isCompleteCurve(plist, true))
                    se.removePrevElement();
            }

            // get a semi complete curve ==> 'plist'
            if (pre_solutionCurves.size() <= 1)
                break;

            NListInfo nli = new NListInfo();
            plist = null;
            for (se = pre_solutionCurves.cursor(); se.hasMoreElements();) {
                plist = (PointList) se.nextElement();
                if (isSemiCompleteCurve(plist, nli))
                    break;
            }
            if (plist == null)
                break;

            // find a curve which is closest to 'plist' ==> 'nlist'
            nli.nlist_dist2 = 1 / MachineEpsilon.SINGLE;
            for (se = pre_solutionCurves.cursor(); se.hasMoreElements();) {
                PointList clist = (PointList) se.nextElement();
                compareNListDist(clist, plist, nli);
            }

            // add 'nlist' into 'plist'
            if (nli.plist_open_side == END) {
                pspnt = plist.last();
                plist.removeLast();
                if (nli.nlist_connect_side == START) {
                    pepnt = nli.nlist.first();
                    plist.connect(nli.nlist, false, false);
                } else { /* nlist_connect_side == END */
                    pepnt = nli.nlist.last();
                    plist.connect(nli.nlist, false, true);
                }
            } else { /* plist_open_side == START */
                pspnt = plist.first();
                plist.removeFirst();

                if (nli.nlist_connect_side == START) {
                    pepnt = nli.nlist.first();
                    plist.connect(nli.nlist, true, true);
                } else { /* nlist_connect_side == END */
                    pepnt = nli.nlist.last();
                    plist.connect(nli.nlist, true, false);
                }
            }
            pepnt.interpolate(pspnt);

            // delete 'nlist'
            for (se = pre_solutionCurves.cursor(); se.hasMoreElements();) {
                plist = (PointList) se.nextElement();
                if (plist == nli.nlist)
                    se.removePrevElement();
            }
        }

        if (pre_solutionCurves.size() > 0) {
            for (se = pre_solutionCurves.cursor(); se.hasMoreElements();) {
                plist = (PointList) se.nextElement();
                if (isCompleteCurve(plist, true))
                    se.removePrevElement();
            }
        }

        if (pre_solutionCurves.size() > 0) {
            for (se = pre_solutionCurves.cursor(); se.hasMoreElements();) {
                plist = (PointList) se.nextElement();
                if (moveToSolutionCurves(plist))
                    se.removePrevElement();
            }
        }

        if (pre_solutionCurves.size() > 0) {
            if (solutionCurves.size() == 0) {
                MListInfo mi = new MListInfo();
                for (se = pre_solutionCurves.cursor(); se.hasMoreElements();) {
                    plist = (PointList) se.nextElement();
                    compareMListLength(plist, mi);
                }
                // In GHL, moveMListToSolutionCurves() returns GH__ERR
                // only if plist != mlist;
                moveMListToSolutionCurves(mi.list, mi);
                for (se = pre_solutionCurves.cursor(); se.hasMoreElements();) {
                    plist = (PointList) se.nextElement();
                    if (plist == mi.list)
                        se.removePrevElement();
                }
            }
        }
    }

    /**
     * *******************************************************************
     * <p/>
     * Refinement
     * <p/>
     * ********************************************************************
     */

    final class RefineInfo {
        Point3D sA_pnt, sB_pnt;
        Vector3D[] Atang;
        Vector3D[] Btang;
        double fx_param;
        BoundaryInfo bi;
        double[] pcache;

        RefineInfo() {
            pcache = null;
        }

        double[] setupParams(PointInfo pi) {
            double[] param = new double[3];

            if ((debugFlag & DEBUG_REFINE) != 0) {
                System.out.println("setupParams: " + bi.param);
            }

            switch (bi.param) {
                case Wp_Au:
                    fx_param = pi.Aupara;
                    param[0] = pi.Avpara;
                    param[1] = pi.Bupara;
                    param[2] = pi.Bvpara;
                    break;
                case Wp_Av:
                    param[0] = pi.Aupara;
                    fx_param = pi.Avpara;
                    param[1] = pi.Bupara;
                    param[2] = pi.Bvpara;
                    break;
                case Wp_Bu:
                    param[0] = pi.Aupara;
                    param[1] = pi.Avpara;
                    fx_param = pi.Bupara;
                    param[2] = pi.Bvpara;
                    break;
                case Wp_Bv:
                    param[0] = pi.Aupara;
                    param[1] = pi.Avpara;
                    param[2] = pi.Bupara;
                    fx_param = pi.Bvpara;
                    break;
            }

            return param;
        }

        /*

        boolean cacheAvailable(double[] param) {
            if (pcache == null)
            return false;

            for (int i = 0; i < param.length; i++) {
            if(pcache[i] != param[i])
                return false;
            }
            return true;
        }
        */

        void fillParam(double[] param) {
            /*
            if (cacheAvailable(param))
            return;
            */

            double[] A = new double[2];
            double[] B = new double[2];

            switch (bi.param) {
                case Wp_Au:
                    A[0] = fx_param;
                    A[1] = param[0];
                    B[0] = param[1];
                    B[1] = param[2];
                    break;
                case Wp_Av:
                    A[0] = param[0];
                    A[1] = fx_param;
                    B[0] = param[1];
                    B[1] = param[2];
                    break;
                case Wp_Bu:
                    A[0] = param[0];
                    A[1] = param[1];
                    B[0] = fx_param;
                    B[1] = param[2];
                    break;
                case Wp_Bv:
                    A[0] = param[0];
                    A[1] = param[1];
                    B[0] = param[2];
                    B[1] = fx_param;
                    break;
            }

            A[0] = dA.uParameterDomain().force(A[0]);
            A[1] = dA.vParameterDomain().force(A[1]);
            B[0] = dB.uParameterDomain().force(B[0]);
            B[1] = dB.vParameterDomain().force(B[1]);

            sA_pnt = dA.coordinates(A[0], A[1]);
            sB_pnt = dB.coordinates(B[0], B[1]);

            Atang = dA.tangentVector(A[0], A[1]);
            Btang = dB.tangentVector(B[0], B[1]);
            Btang[0] = Btang[0].multiply(-1);
            Btang[1] = Btang[1].multiply(-1);
        }

        Vector3D[] getVectors() {
            Vector3D[] vecs = null;
            switch (bi.param) {
                case Wp_Au: {
                    Vector3D[] v = {Atang[1], Btang[0], Btang[1]};
                    vecs = v;
                }
                break;

                case Wp_Av: {
                    Vector3D[] v = {Atang[0], Btang[0], Btang[1]};
                    vecs = v;
                }
                break;

                case Wp_Bu: {
                    Vector3D[] v = {Atang[0], Atang[1], Btang[1]};
                    vecs = v;
                }
                break;

                case Wp_Bv: {
                    Vector3D[] v = {Atang[0], Atang[1], Btang[0]};
                    vecs = v;
                }
                break;
            }
            return vecs;
        }
    }

    RefineInfo ri;

    double[] setupParams(PointInfo pi, PointInfo b_pi) {
        BoundaryInfo bi;
        bi = ri.bi = new BoundaryInfo(Wp_No, pi);

        if (bi.param == Wp_No) {
            /*
            * point is in the internal area of surfaces
            *
            * select a parameter which gap with previous points is maximum
            * as fixed one
            */
            double Au_gap = Math.abs(pi.Aupara - b_pi.Aupara);
            double Av_gap = Math.abs(pi.Avpara - b_pi.Avpara);
            double Bu_gap = Math.abs(pi.Bupara - b_pi.Bupara);
            double Bv_gap = Math.abs(pi.Bvpara - b_pi.Bvpara);
            double c_gap;

            /* at first */
            {
                c_gap = Au_gap;
                bi.param = Wp_Au;
            }
            if (c_gap < Av_gap) {
                c_gap = Av_gap;
                bi.param = Wp_Av;
            }
            if (c_gap < Bu_gap) {
                c_gap = Bu_gap;
                bi.param = Wp_Bu;
            }
            if (c_gap < Bv_gap) {
                c_gap = Bv_gap;
                bi.param = Wp_Bv;
            }
        } else {
            /*
            * point is on boundary
            *
            * select a parameter which is at boundary as fixed one
            * BUT if previous point is on same boundary,
            * select another parameter of same surface as fixed
            * in order to avoid to converge same coordinates with
            * previous point
            *
            */
            BoundaryInfo b_bi = new BoundaryInfo(Wp_No, b_pi);

            if ((b_bi.param == bi.param && b_bi.wend == bi.wend) ||
                    b_bi.param == Wp_No) {
                switch (bi.param) {
                    case Wp_Au:
                        if ((Math.abs(pi.Aupara - b_pi.Aupara) < pTol) &&
                                (Math.abs(pi.Avpara - b_pi.Avpara) > pTol))
                            bi.param = Wp_Av;
                        break;

                    case Wp_Av:
                        if ((Math.abs(pi.Avpara - b_pi.Avpara) < pTol) &&
                                (Math.abs(pi.Aupara - b_pi.Aupara) > pTol))
                            bi.param = Wp_Au;
                        break;

                    case Wp_Bu:
                        if ((Math.abs(pi.Bupara - b_pi.Bupara) < pTol) &&
                                (Math.abs(pi.Bvpara - b_pi.Bvpara) > pTol))
                            bi.param = Wp_Bv;
                        break;

                    case Wp_Bv:
                        if ((Math.abs(pi.Bvpara - b_pi.Bvpara) < pTol) &&
                                (Math.abs(pi.Bupara - b_pi.Bupara) > pTol))
                            bi.param = Wp_Bu;
                        break;
                }
            }
        }
        return ri.setupParams(pi);
    }

    double[] reSetupParams(PointInfo pi) {
        BoundaryInfo bi = ri.bi;
        BoundaryInfo nbi = new BoundaryInfo(bi.param, pi);

        if (nbi.param == bi.param) {
            switch (nbi.param) {
                case Wp_Au:
                    nbi.param = Wp_Av;
                    break;
                case Wp_Av:
                    nbi.param = Wp_Au;
                    break;
                case Wp_Bu:
                    nbi.param = Wp_Bv;
                    break;
                case Wp_Bv:
                    nbi.param = Wp_Bu;
                    break;
            }
        }
        ri.bi = nbi;

        return ri.setupParams(pi);
    }

    void reformParam(int n, double param[]) {
        int i;

        for (i = 0; i < n; i++) {
            if (param[i] < 0.0) param[i] = 0.0;
            if (param[i] > 1.0) param[i] = 1.0;
        }
    }

    /*
    * F of F(x) = 0
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
            double[] vctr = new double[3];
            Vector3D evec;

            /*
            * sA_pnt & sB_pnt are already computed by previous cnv_func()
            */
            evec = ri.sA_pnt.subtract(ri.sB_pnt);
            vctr[0] = evec.x();
            vctr[1] = evec.y();
            vctr[2] = evec.z();

            return vctr;
        }
    }

    /*
    * partial derivatives of F
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
            // ri.fillParam(parameter);
            /*
            Vector3D vec = ri.getVectors()[idx];

            double[] mtrx = new double[3];
            mtrx[0] = vec.x();
            mtrx[1] = vec.y();
            mtrx[2] = vec.z();
            return mtrx;
            */

            Vector3D[] vecs = ri.getVectors();

            double[] mtrx = new double[3];
            for (int i = 0; i < 3; i++) {
                switch (idx) {
                    default:
                    case 0:
                        mtrx[i] = vecs[i].x();
                        break;
                    case 1:
                        mtrx[i] = vecs[i].y();
                        break;
                    case 2:
                        mtrx[i] = vecs[i].z();
                        break;
                }
            }
            return mtrx;
        }
    }

    /*
    * convergence test
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
            ri.fillParam(parameter);
            return ri.sA_pnt.identical(ri.sB_pnt);
        }
    }

    boolean setbackParams(PointInfo pi, double[] param,
                          PointInfo b_pi, PointInfo a_pi) {
        double[] A_param = new double[2];
        double[] B_param = new double[2];
        Vector2D evec1, evec2;

        reformParam(3, param);

        switch (ri.bi.param) {
            case Wp_Au:
                A_param[0] = pi.Aupara;
                A_param[1] = param[0];
                B_param[0] = param[1];
                B_param[1] = param[2];
                break;
            case Wp_Av:
                A_param[0] = param[0];
                A_param[1] = pi.Avpara;
                B_param[0] = param[1];
                B_param[1] = param[2];
                break;
            case Wp_Bu:
                A_param[0] = param[0];
                A_param[1] = param[1];
                B_param[0] = pi.Bupara;
                B_param[1] = param[2];
                break;
            case Wp_Bv:
                A_param[0] = param[0];
                A_param[1] = param[1];
                B_param[0] = param[2];
                B_param[1] = pi.Bvpara;
                break;
        }

        evec1 = new LiteralVector2D(A_param[0] - b_pi.Aupara,
                A_param[1] - b_pi.Avpara);
        evec2 = new LiteralVector2D(pi.Aupara - b_pi.Aupara,
                pi.Avpara - b_pi.Avpara);
        if (evec1.dotProduct(evec2) < 0.0)
            return false;

        evec1 = new LiteralVector2D(A_param[0] - a_pi.Aupara,
                A_param[1] - a_pi.Avpara);
        evec2 = new LiteralVector2D(pi.Aupara - a_pi.Aupara,
                pi.Avpara - a_pi.Avpara);
        if (evec1.dotProduct(evec2) < 0.0)
            return false;

        evec1 = new LiteralVector2D(B_param[0] - b_pi.Bupara,
                B_param[1] - b_pi.Bvpara);
        evec2 = new LiteralVector2D(pi.Bupara - b_pi.Bupara,
                pi.Bvpara - b_pi.Bvpara);
        if (evec1.dotProduct(evec2) < 0.0)
            return false;

        evec1 = new LiteralVector2D(B_param[0] - a_pi.Bupara,
                B_param[1] - a_pi.Bvpara);
        evec2 = new LiteralVector2D(pi.Bupara - a_pi.Bupara,
                pi.Bvpara - a_pi.Bvpara);
        if (evec1.dotProduct(evec2) < 0.0)
            return false;

        pi.Aupara = A_param[0];
        pi.Avpara = A_param[1];
        pi.Bupara = B_param[0];
        pi.Bvpara = B_param[1];

        return true;
    }

    boolean refinePointInfo(PointInfo pinfo,
                            PointInfo b_pinfo,
                            PointInfo a_pinfo,
                            boolean do_retry) {
        double[] param;
        ri = new RefineInfo();

        nlFunc nl_func = new nlFunc();
        PrimitiveMappingND[] dnl_func = new PrimitiveMappingND[3];
        dnl_func[0] = new dnlFunc(0);
        dnl_func[1] = new dnlFunc(1);
        dnl_func[2] = new dnlFunc(2);
        cnvFunc cnv_func = new cnvFunc();

        if ((debugFlag & DEBUG_REFINE) != 0) {
            System.out.println("refine:");
            pinfo.pnt.literal().output(System.out);
            System.out.println("(" + pinfo.Aupara + "," + pinfo.Avpara +
                    "), (" + pinfo.Bupara + "," + pinfo.Bvpara +
                    ")");
        }
        param = setupParams(pinfo, b_pinfo);

        param = GeometryUtils.solveSimultaneousEquations(nl_func, dnl_func,
                cnv_func, param);
        if (param == null && do_retry) {
            if ((debugFlag & DEBUG_REFINE) != 0) {
                System.out.println("refine retry:");
            }
            // make another parameter fixed, then retry
            param = reSetupParams(pinfo);
            param = GeometryUtils.solveSimultaneousEquations(nl_func, dnl_func,
                    cnv_func, param);
        }

        if (param == null) {
            if ((debugFlag & DEBUG_REFINE) != 0) {
                System.out.println("refine fail:");
            }
            return false;
        }

        if (!setbackParams(pinfo, param, b_pinfo, a_pinfo)) {
            if ((debugFlag & DEBUG_REFINE) != 0) {
                System.out.println("setback fail:");
            }
            return false;
        }
        return true;
    }

    IntersectionPoint3D makeIntersectionPoint(ParametricSurface3D s1,
                                              Point2D p1,
                                              ParametricSurface3D s2,
                                              Point2D p2) {
        return new IntersectionPoint3D(s1, p1.x(), p1.y(),
                s2, p2.x(), p2.y(), false);
    }

    /*
    * fill array
    */
    SurfaceSurfaceInterference3D fillArray(PointList plist,
                                           PureBezierSurface3D dA,
                                           PureBezierSurface3D dB) {
        PointInfo pinfo;
        PointInfo b_pinfo;    // for setupParams() in refinePointInfo()
        PointInfo a_pinfo;    // for setbackParams() in refinePointInfo()
        Vector vecA = new Vector();
        Vector vecB = new Vector();
        Vector vecP = new Vector();
        CartesianPoint2D Apara, Bpara;
        int i;
        boolean closed = plist.is_closed();

        for (i = closed ? 1 : 0; i < plist.no; i++) {
            pinfo = plist.first(i);
            if (i == 0)
                b_pinfo = plist.first();
            else
                b_pinfo = plist.first(i - 1);

            if (i < (plist.no - 1))
                a_pinfo = plist.first(i + 1);
            else
                a_pinfo = plist.last();

            if (!refinePointInfo(pinfo, b_pinfo, a_pinfo, true))
                continue;

            Point3D p1 =
                    new PointOnSurface3D(dA, pinfo.Aupara, pinfo.Avpara, GeometryElement.doCheckDebug);
            Point3D p2 =
                    new PointOnSurface3D(dB, pinfo.Bupara, pinfo.Bvpara, GeometryElement.doCheckDebug);
            pinfo.pnt = p1.linearInterpolate(p2, 0.5);

            Apara = new CartesianPoint2D(pinfo.Aupara, pinfo.Avpara);
            Bpara = new CartesianPoint2D(pinfo.Bupara, pinfo.Bvpara);
            if (vecP.size() > 0) {
                Point3D pnt = (Point3D) vecP.lastElement();
                if (pnt.distance2(pinfo.pnt) < dTol2) {
                    if (i == plist.no - 1) {
                        int last = vecP.size() - 1;

                        if (last == 0) {
                            /* ���[�_�̒��ԓ_�� IntersectionPoint �ɂ��� */
                            return makeIntersectionPoint(dA,
                                    Apara.linearInterpolate((Point2D) vecA.firstElement(), 0.5),
                                    dB,
                                    Bpara.linearInterpolate((Point2D) vecB.firstElement(), 0.5));

                        }
                        vecA.setElementAt(Apara, last);
                        vecB.setElementAt(Bpara, last);
                        vecP.setElementAt(pinfo.pnt, last);
                    }
                    continue;
                }
            }

            vecA.addElement(Apara);
            vecB.addElement(Bpara);
            vecP.addElement(pinfo.pnt);
        }

        if (vecP.size() < 1) {
            return null;
        } else if (vecP.size() == 1) {
            /* �B��̓_�� IntersectionPoint �ɂ��� */
            return makeIntersectionPoint(dA, (Point2D) vecA.firstElement(),
                    dB, (Point2D) vecB.firstElement());
        }

        CartesianPoint2D[] Apnts = new CartesianPoint2D[vecP.size()];
        vecA.copyInto(Apnts);
        Polyline2D curve2d1 = new Polyline2D(Apnts, closed);

        CartesianPoint2D[] Bpnts = new CartesianPoint2D[vecP.size()];
        vecB.copyInto(Bpnts);
        Polyline2D curve2d2 = new Polyline2D(Bpnts, closed);

        Point3D[] pnts = new Point3D[vecP.size()];
        vecP.copyInto(pnts);
        Polyline3D curve3d = new Polyline3D(pnts, closed);

        return new IntersectionCurve3D(curve3d, dA, curve2d1, dB, curve2d2,
                PreferredSurfaceCurveRepresentation.CURVE_3D);
    }

    /*********************************************************************
     *
     * Body (defined as external, since this is called from gh3intsBssBss.c)
     *
     **********************************************************************/

    /**
     * @param dA Bezier Surface A
     * @param dB Bezier Surface B
     */

    SurfaceSurfaceInterference3D[] intsBzsBzs() {
        BezierInfo dARoot;
        QuadTree.Node dARootNode;
        BezierInfo dBRoot;

        ConditionOfOperation cond =
                ConditionOfOperation.getCondition();
        cond = cond.makeCopyWithToleranceForDistance(TORELANCE_OVERRIDE_IN_INTERSECTION);
        cond.push();
        try {
            dTol2 = cond.getToleranceForDistance2();
            dTol = cond.getToleranceForDistance();
            pTol = cond.getToleranceForParameter();

            // Preparation

            /*
              first_rough_check(dA, dB);
            */

            // Initialize Binary Trees & Make Roots
            dARoot = new BezierInfo(dA, dA, 0.0, 1.0, 0.0, 1.0, false);
            Atree = new QuadTree(dARoot);
            dARootNode = Atree.rootNode();

            dBRoot = new BezierInfo(dB, dB, 0.0, 1.0, 0.0, 1.0, true);
            dBRoot.rivals.addElement(dARootNode);

            // Get Unconnected Segments of Intersections
            solutionSegs = new ObjectVector();

            getIntersections(dBRoot, 0);

            Cursor e;

            // remove ambiguous segments
            for (e = solutionSegs.cursor(); e.hasMoreElements();) {
                LineSegmentInfo ls_info = (LineSegmentInfo) e.nextElement();
                if (!ls_info.is_main_line)
                    e.removePrevElement();
            }
            if (solutionSegs.size() == 0) {
                return new SurfaceSurfaceInterference3D[0];
            }

            // Connect Segments of Intersections
            solutionCurves = new Vector();
            connectIntersections();
            if (solutionCurves.size() == 0) {
                return new SurfaceSurfaceInterference3D[0];
            }
        } finally {
            ConditionOfOperation.pop();
        }

        // Make Results
        Vector ints_crvs = new Vector();
        Enumeration ee;
        SurfaceSurfaceInterference3D sol;
        for (ee = solutionCurves.elements(); ee.hasMoreElements();) {
            PointList plist = (PointList) ee.nextElement();
            if ((sol = fillArray(plist, dA, dB)) != null)
                ints_crvs.addElement(sol);
        }

        SurfaceSurfaceInterference3D[] crvs =
                new SurfaceSurfaceInterference3D[ints_crvs.size()];
        ints_crvs.copyInto(crvs);

        return crvs;
    }

    IntsBzsBzs3D(PureBezierSurface3D dA,
                 PureBezierSurface3D dB) {

        this.dA = dA;
        this.dB = dB;
        debugFlag = 0;
    }

    IntsBzsBzs3D(PureBezierSurface3D dA,
                 PureBezierSurface3D dB,
                 boolean debug) {
        this(dA, dB);

        if (debug) {
            // debugFlag = 0;
            debugFlag = DEBUG_REFINE;
            // debugFlag = DEBUG_ALL & ~DEBUG_SAMESEG;
        }
    }

    static SurfaceSurfaceInterference3D[]
    intersection(PureBezierSurface3D bzs1,
                 PureBezierSurface3D bzs2) {
        IntsBzsBzs3D doObj = new IntsBzsBzs3D(bzs1, bzs2, false);
        return doObj.intsBzsBzs();
    }

    static SurfaceSurfaceInterference3D[]
    intersection(PureBezierSurface3D bzs1,
                 PureBezierSurface3D bzs2,
                 boolean debug) {
        IntsBzsBzs3D doObj = new IntsBzsBzs3D(bzs1, bzs2, debug);
        return doObj.intsBzsBzs();
    }

    static Point3D[][] toPnt(double[][][] array) {
        Point3D[][] pnts = new Point3D[4][4];

        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 4; i++)
                pnts[i][j] = new CartesianPoint3D(array[i][j]);
        return pnts;
    }

    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     */
    public static void main(String[] args) {
        double[][][] surf1 = {
                {{0.0, 0.0, 1.5}, {1.0, 0.0, 1.5}, {2.0, 0.0, 1.5}, {3.0, 0.0, 1.5}},
                {{0.0, 1.0, 1.5}, {1.0, 1.0, 1.5}, {2.0, 1.0, 1.5}, {3.0, 1.0, 1.5}},
                {{0.0, 2.0, 1.5}, {1.0, 2.0, 1.5}, {2.0, 2.0, 1.5}, {3.0, 2.0, 1.5}},
                {{0.0, 3.0, 1.5}, {1.0, 3.0, 1.5}, {2.0, 3.0, 1.5}, {3.0, 3.0, 1.5}}
        };

        double[][][] surf2 = {
                {{1.5, 0.0, 0.0}, {1.5, 1.0, 0.0}, {1.5, 2.0, 0.0}, {1.5, 3.0, 0.0}},
                {{1.5, 0.0, 1.0}, {1.5, 1.0, 1.0}, {1.5, 2.0, 1.0}, {1.5, 3.0, 1.0}},
                {{1.5, 0.0, 2.0}, {1.5, 1.0, 2.0}, {1.5, 2.0, 2.0}, {1.5, 3.0, 2.0}},
                {{1.5, 0.0, 3.0}, {1.5, 1.0, 3.0}, {1.5, 2.0, 3.0}, {1.5, 3.0, 3.0}}
        };

        double[][][] surf3 = {
                {{0.0, 0.0, 0.0}, {1.0, 0.0, 0.0}, {2.0, 0.0, 0.0}, {3.0, 0.0, 0.0}},
                {{0.0, 1.0, 0.0}, {1.0, 1.0, 4.0}, {2.0, 1.0, 4.0}, {3.0, 1.0, 0.0}},
                {{0.0, 2.0, 0.0}, {1.0, 2.0, 4.0}, {2.0, 2.0, 4.0}, {3.0, 2.0, 0.0}},
                {{0.0, 3.0, 0.0}, {1.0, 3.0, 0.0}, {2.0, 3.0, 0.0}, {3.0, 3.0, 0.0}}
        };

        double[][][] bezierSurface5 = {
                {{3.00, 5.00, 8.00},
                        {7.82963, 6.2941, 13.00},
                        {12.6593, 7.58819, 3.00},
                        {17.4889, 8.88229, 8.00}},

                {{1.7059, 9.82963, 18.00},
                        {6.53553, 11.1237, 23.00},
                        {11.3652, 12.4178, 13.00},
                        {16.1948, 13.7119, 18.00}},

                {{0.41181, 14.6593, 18.00},
                        {5.24144, 15.9534, 23.00},
                        {10.0711, 17.2474, 13.00},
                        {14.9007, 18.5415, 18.00}},

                {{-0.882286, 19.4889, 8.00},
                        {3.94734, 20.783, 13.00},
                        {8.77697, 22.0771, 3.00},
                        {13.6066, 23.3712, 8.00}}
        };

        double[][][] bezierSurface6 = {
                {{0, 0, 0},
                        {10.00, 0, 0},
                        {20.00, 0, 0},
                        {30.00, 0, 0}},

                {{0, 6.83013, 11.8301},
                        {10.00, 6.83013, 10.8301},
                        {20.00, 6.83013, 11.8301},
                        {30.00, 6.83013, 10.8301}},

                {{0, 16.1603, 14.3301},
                        {10.00, 16.1603, 13.3301},
                        {20.00, 16.1603, 14.3301},
                        {30.00, 16.1603, 13.3301}},

                {{0, 27.9904, 7.50},
                        {10.00, 27.9904, 6.50},
                        {20.00, 27.9904, 7.50},
                        {30.00, 27.9904, 6.50}}
        };

        double[][][] bezierSurface7 = {
                {{0, 0, 0},
                        {9.96195, -0.871557, 0},
                        {19.9239, -1.74311, 0},
                        {29.8858, -2.61467, 0}},

                {{0.59528, 6.80414, 11.8301},
                        {10.5572, 5.93258, 11.8301},
                        {20.5192, 5.06102, 11.8301},
                        {30.4811, 4.18947, 11.8301}},

                {{1.40846, 16.0988, 14.3301},
                        {11.3704, 15.2272, 14.3301},
                        {21.3324, 14.3557, 14.3301},
                        {31.2943, 13.4841, 14.3301}},

                {{2.43952, 27.8839, 7.50},
                        {12.4015, 27.0123, 7.50},
                        {22.3634, 26.1408, 7.50},
                        {32.3254, 25.2692, 7.50}}
        };

        double[][][] bezierSurface8 = {
                {{3.00, 5.00, 4.00},
                        {7.33013, 4.66506, 9.58015},
                        {11.6603, 11.8301, 2.16987},
                        {15.9904, 11.4951, 7.74999},
                        {20.3205, 13.6603, 9.00002}},

                {{0.50, 3.74999, 14.8253},
                        {4.83013, 3.41505, 20.4054},
                        {9.16025, 10.5801, 12.9951},
                        {13.4904, 10.2452, 18.5753},
                        {17.8205, 12.4102, 19.8253}},

                {{-2.00, 7.50003, 16.9904},
                        {2.33013, 7.16506, 22.5705},
                        {6.66025, 14.3301, 15.1603},
                        {10.9904, 13.9953, 20.7405},
                        {15.3205, 16.1603, 21.9904}},

                {{-4.50, 16.25, 10.4952},
                        {-0.169873, 15.9151, 16.0753},
                        {4.16025, 23.0802, 8.66508},
                        {8.49038, 22.7452, 14.2452},
                        {12.8205, 24.9102, 15.4952}}
        };

        double[][][] bezierSurfaceA = {
                {{0.0, 0.0, 0.0},
                        {50.0, 0.0, 0.0},
                        {66.66666666666666, 0.0, 0.0},
                        {83.33333333333333, 0.0, 0.0}},
                {{0.0, 20.0, 0.0},
                        {50.0, 20.0, 0.0},
                        {66.66666666666666, 20.0, 0.0},
                        {83.33333333333333, 20.0, 0.0}},
                {{0.0, 50.0, 0.0},
                        {50.0, 50.0, 0.0},
                        {66.66666666666666, 50.0, 0.0},
                        {83.33333333333333, 50.0, 0.0}},
                {{0.0, 100.0, 0.0},
                        {50.0, 100.0, 0.0},
                        {66.66666666666666, 100.0, 0.0},
                        {83.33333333333333, 100.0, 0.0}}
        };

        double[][][] bezierSurfaceB = {
                {{0.0, 50.0, -40.0},
                        {50.0, 50.0, -40.0},
                        {66.66666666666666, 50.0, -40.0},
                        {83.33333333333333, 50.0, -40.0}},
                {{0.0, 50.0, -20.0},
                        {50.0, 50.0, -20.0},
                        {66.66666666666666, 50.0, -20.0},
                        {83.33333333333333, 50.0, -20.0}},
                {{0.0, 50.0, 35.0},
                        {50.0, 50.0, 35.0},
                        {66.66666666666666, 50.0, 35.0},
                        {83.33333333333333, 50.0, 35.0}},
                {{0.0, 50.0, 60.0},
                        {50.0, 50.0, 60.0},
                        {66.66666666666666, 50.0, 60.00000000000001},
                        {83.33333333333333, 50.0, 60.00000000000001}}
        };

        PureBezierSurface3D dA =
                new PureBezierSurface3D(toPnt(bezierSurfaceA));
        PureBezierSurface3D dB =
                new PureBezierSurface3D(toPnt(bezierSurfaceB));

        dA.output(System.out);
        dB.output(System.out);

        SurfaceSurfaceInterference3D[] ints
                = intersection(dA, dB, true);
        for (int i = 0; i < ints.length; i++) {
            if (ints[i].isIntersectionCurve()) {
                ints[i].toIntersectionCurve().output(System.out);
            } else if (ints[i].isIntersectionPoint()) {
                ints[i].toIntersectionPoint().output(System.out);
            }
        }
    }
}

/* end of file */
