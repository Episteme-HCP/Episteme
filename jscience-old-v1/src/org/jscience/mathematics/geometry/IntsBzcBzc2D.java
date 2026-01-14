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

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.analysis.PrimitiveMappingND;

import java.util.Vector;

/**
 * 2D�x�W�G��?�m�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.7 $, $Date: 2006/03/01 21:16:00 $
 */

class IntsBzcBzc2D {
    private static final double pTol = 0.001;

    /**
     * �^����ꂽ�x�W�G��?� A
     *
     * @see PureBezierCurve2D
     */
    private PureBezierCurve2D dA;

    /**
     * �^����ꂽ�x�W�G��?� B
     *
     * @see PureBezierCurve2D
     */
    private PureBezierCurve2D dB;

    /**
     * ��?�A�̃p���??[�^���e��?��̋ߎ��l
     */
    private double aprx_ptolA;

    /**
     * ��?�A�̃p���??[�^���e��?��̋ߎ��l
     */
    private double aprx_ptolB;

    /**
     * ��?�A�̕�����\���񕪖�
     *
     * @see BinaryTree
     */
    private BinaryTree Atree;

    /*
    * ���ۑ����Ă������X�g
    * @see CurveCurveInterferenceList
    */
    private CurveCurveInterferenceList sol_list;

    private Point2D sApnt;        // temporally used in refinePointInfo
    private Point2D sBpnt;        // temporally used in refinePointInfo
    private Vector2D sAtang;    // temporally used in refinePointInfo
    private Vector2D sBtang;    // temporally used in refinePointInfo

    private static double getPtol(double d_tol, double length) {
        double tol = d_tol / length * 10.0;
        if (tol < pTol)
            tol = pTol;
        return tol;
    }

    /**
     * �I�u�W�F�N�g��?\�z����
     *
     * @param bzc1 �x�W�G��?� A
     * @param bzc2 �x�W�G��?� B
     * @see PureBezierCurve2D
     */
    private IntsBzcBzc2D(PureBezierCurve2D bzc1, PureBezierCurve2D bzc2) {
        super();

        dA = bzc1;
        dB = bzc2;

        double d_tol = dA.getToleranceForDistance();

        aprx_ptolA = getPtol(d_tol, dA.approximateLength());
        aprx_ptolB = getPtol(d_tol, dB.approximateLength());

        /*
        * Initialize Binary Trees
        */
        Atree = new BinaryTree(new BezierInfo(dA, 0.0, 1.0, false));

        sol_list = new CurveCurveInterferenceList(bzc1, bzc2);
    }

    private static final int UNKNOWN = 0;
    private static final int BEZIER = 1;
    private static final int LINE = 2;
    private static final int POINT = 3;

    /**
     * �������ꂽ�x�W�G��?�Z�O�?���g��\���N���X
     */
    private class BezierInfo {
        /**
         * �������ꂽ�x�W�G��?�Z�O�?���g�̎�
         *
         * @see PureBezierCurve2D
         */
        private PureBezierCurve2D bzc;

        /**
         * ���̃x�W�G��?�ł̎n�_�̃p���??[�^
         */
        private double sp;

        /**
         * ���̃x�W�G��?�ł�?I�_�̃p���??[�^
         */
        private double ep;

        /**
         * ��?ݔ͈͂���`
         *
         * @see EnclosingBox2D
         */
        private EnclosingBox2D box;

        /**
         * ��?���(�������ꂽ)����Z�O�?���g�̃��X�g
         */
        private Vector rivals;

        /**
         * �`?�̓R���\�����
         * UNKNOWN:	����?�
         * POINT:		�_�ł���
         * LINE:		?�ł���
         * BEZIER:		�_�ł�?�ł�Ȃ��x�W�G��?�
         */
        private int crnt_type;

        /*
        * �`?�̓R���\���v�f
        * crnt_type��POINT:	Point2D
        * crnt_type��LINE:	Line2D
        * ���̑�:		null
        */
        private GeometryElement geom;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param bzc       �������ꂽ�x�W�G��?�Z�O�?���g�̎�
         * @param sp        ���̃x�W�G��?�ł̎n�_�̃p���??[�^
         * @param ep        ���̃x�W�G��?�ł�?I�_�̃p���??[�^
         * @param hasRivals ���肪���邩?
         * @see PureBezierCurve2D
         */
        private BezierInfo(PureBezierCurve2D bzc, double sp, double ep, boolean hasRivals) {
            super();
            this.bzc = bzc;
            this.sp = sp;
            this.ep = ep;
            this.box = bzc.approximateEnclosingBox();

            if (hasRivals)
                this.rivals = new Vector();
            else
                this.rivals = null;
            this.crnt_type = UNKNOWN;
            this.geom = null;
        }

        /**
         * geom��Point2D�Ƃ݂Ȃ���?A���̒l��Ԃ�
         *
         * @return geom��Point2D�ɕϊ������l
         */
        private Point2D pnt() {
            return (Point2D) geom;
        }

        /**
         * geom��Line2D�Ƃ݂Ȃ���?A���̒l��Ԃ�
         *
         * @return geom��Line2D�ɕϊ������l
         */
        private Line2D line() {
            return (Line2D) geom;
        }

        /**
         * �`?�̓R��𒲂ׂ�?B
         * �܂����ׂ����ʂ�POINT�µ����LINE��?�?���?A
         * geom�ɂ��̎̂�Z�b�g����?B
         *
         * @return �`?�̓R�(POINT/LINE/BEZIER�̂����ꂩ)
         */
        private int whatTypeIsBezier() {
            if (crnt_type != UNKNOWN)
                return crnt_type;

            int uicp = bzc.nControlPoints();

            Vector2D s2e;
            double leng_s2e;
            Vector2D unit_s2e;
            Vector2D s2c;
            double dist;
            double leng;
            int i;
            double d_tol = bzc.getToleranceForDistance();

            s2e = bzc.controlPointAt(uicp - 1).subtract(bzc.controlPointAt(0));
            leng_s2e = s2e.length();

            if (leng_s2e < d_tol) {
                for (i = 1; i < (uicp - 1); i++) {
                    s2c = bzc.controlPointAt(i).subtract(bzc.controlPointAt(0));
                    if (!(s2c.length() < d_tol))
                        break;
                }

                if (i == (uicp - 1)) {
                    /*
                    * AbstractPoint
                    */
                    Point2D pnt_geom
                            = bzc.controlPointAt(uicp - 1).linearInterpolate(bzc.controlPointAt(0), 0.5);

                    geom = pnt_geom;
                    return crnt_type = POINT;
                } else {
                    /*
                    * Bezier
                    */
                    geom = null;
                    return crnt_type = BEZIER;
                }
            }

            unit_s2e = s2e.divide(leng_s2e);

            for (i = 1; i < (uicp - 1); i++) {
                s2c = bzc.controlPointAt(i).subtract(bzc.controlPointAt(0));
                dist = unit_s2e.zOfCrossProduct(s2c);
                if (Math.abs(dist) > d_tol) {
                    /*
                    * Bezier
                    */
                    geom = null;
                    return crnt_type = BEZIER;
                }

                leng = unit_s2e.dotProduct(s2c);
                if ((leng < (0.0 - d_tol)) || (leng > (leng_s2e + d_tol))) {
                    /*
                    * Bezier
                    */
                    geom = null;
                    return crnt_type = BEZIER;
                }
            }

            /*
            * Line
            */
            Line2D lin_geom = new Line2D(bzc.controlPointAt(0), s2e);

            geom = lin_geom;
            return crnt_type = LINE;
        }

        /**
         * ��?g�̃p���??[�^�쳂̃x�W�G��?�̃p���??[�^�ɕϊ�����?B
         *
         * @param param ��?g�̃p���??[�^
         * @return ���̋�?�Ƃ��Ẵp���??[�^
         */
        private double toBezierParam(double param) {
            return (1.0 - param) * sp + param * ep;
        }
    }

    /**
     * ���LINE?A����BEZIER�̂Ƃ���?A
     * 2�Z�O�?���g������\?������邩�ǂ������ׂ�
     *
     * @param dA LINE���µ��Ȃ��Z�O�?���g
     * @param dB ����̃Z�O�?���g
     * @return ����\?������邩�ǂ���
     * @see BezierInfo
     */
    private boolean checkLineBezier(BezierInfo dA, BezierInfo dB) {
        if (dA.crnt_type == LINE) {
            if ((dB.crnt_type == UNKNOWN) || (dB.crnt_type == BEZIER)) {
                Line2D line;
                Line2D unit_line;
                int i;
                int pside, cside;

                line = dA.line();
                unit_line = new Line2D(line.pnt(), line.dir().unitized());
                int uicp = dB.bzc.nControlPoints();

                pside = unit_line.pointIsWhichSide(dB.bzc.controlPointAt(0));
                if (pside == WhichSide.ON)
                    return true;
                for (i = 1; i < uicp; i++) {
                    cside = unit_line.pointIsWhichSide(dB.bzc.controlPointAt(i));
                    if (pside != cside)
                        return true;
                }
                return false;    /* all control points of dB lie in the same side
                 for line (dA->geom), no interfere */
            }
        }
        return true;
    }

    /**
     * 2�Z�O�?���g������\?������邩�ǂ������ׂ�
     *
     * @param dA �Z�O�?���gA
     * @param dB �Z�O�?���gB
     * @return ����\?������邩�ǂ���
     * @see BezierInfo
     */
    private boolean checkInterfere(BezierInfo dA, BezierInfo dB) {
        /*
        * if one curve is already linear and another is still Bezier,
        * special consideration will be applied.
        */
        if (!checkLineBezier(dA, dB))
            return false;
        if (!checkLineBezier(dB, dA))
            return false;

        /*
        * check Box vs. Box interfere
        */
        return dA.box.hasIntersection(dB.box);
    }

    /**
     * ����𕪊�����?B
     * �������ꂽ�Z�O�?���g��<code>new_rivals</code>�Ɋi�[�����?B
     * ���肪��ɕ�������Ă���Ƃ�(�q�m?[�h��?�Ƃ�)�͎q�m?[�h��i�[����?B
     * ���肪POINT/LINE�ł���?�?���?A���������ɂ��̂܂܊i�[����?B
     *
     * @param dANode     ��?ۂƂȂ鑊���\���񕪖؂̃m?[�h
     * @param new_rivals �������������i�[���郊�X�g
     * @return �������ꂽ��(�m?[�h��Bezier��ʂ�BEZIER�Ȃ�)<code>true</code>?A ��������Ȃ��Ȃ�(BEZIER�ȊO�Ȃ��)<code>false</code>
     * @see BinaryTree.Node
     */
    private boolean divideRivals(BinaryTree.Node dANode, Vector new_rivals) {
        BinaryTree.Node binL, binR;

        if (dANode.left() == null && dANode.right() == null) {
            BezierInfo bi = (BezierInfo) dANode.data();

            if (bi.whatTypeIsBezier() != BEZIER) {
                new_rivals.addElement(dANode);
                return false;
            }

            /*
            * subdivide rival
            */
            double half_point = 0.5;
            PureBezierCurve2D[] bzcs = bi.bzc.divide(half_point);

            double g_half_point = (bi.sp + bi.ep) / 2.0;

            BezierInfo biL = new BezierInfo(bzcs[0], bi.sp, g_half_point, false);
            binL = dANode.makeLeft(biL);

            BezierInfo biR = new BezierInfo(bzcs[1], g_half_point, bi.ep, false);
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
     * ��_(��?���l)��\���N���X
     */
    private class PointInfo {
        /**
         * ?W�l
         *
         * @see Point2D
         */
        private Point2D pnt;

        /**
         * ��?�A�̃p���??[�^
         */
        private double Apara;

        /**
         * ��?�B�̃p���??[�^
         */
        private double Bpara;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param pnt   ?W�l
         * @param Apara ��?�A�̃p���??[�^
         * @param Bpara ��?�B�̃p���??[�^
         * @see Point2D
         */
        private PointInfo(Point2D pnt, double Apara, double Bpara) {
            this.pnt = pnt;
            this.Apara = Apara;
            this.Bpara = Bpara;
        }

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param x     x?W�l
         * @param y     y?W�l
         * @param z     z?W�l
         * @param Apara ��?�A�̃p���??[�^
         * @param Bpara ��?�B�̃p���??[�^
         */
        private PointInfo(double x, double y, double Apara, double Bpara) {
            this.pnt = new CartesianPoint2D(x, y);
            this.Apara = Apara;
            this.Bpara = Bpara;
        }
    }

    /**
     * ���POINT?A����LINE�̂Ƃ��Ɍ�_��?�߂�
     * �_��?�ɓ��e����?A���e�_������΂����Ԃ�?B
     *
     * @param pbi POINT�ł��镪���Z�O�?���g
     * @param lbi LINE�ł��镪���Z�O�?���g
     * @return ��_(��?݂��Ȃ��Ƃ���null)
     */
    private PointInfo
    intersectPntLine(BezierInfo pbi, BezierInfo lbi) {
        Point2D pnt = pbi.pnt();
        Line2D line = lbi.line();
        BoundedLine2D bln = new BoundedLine2D(line.pnt(), line.dir());
        PointOnCurve2D poc;

        if ((poc = bln.project1From(pnt)) == null)
            return null;        // no solution

        double Apara = (pbi.sp + pbi.ep) / 2.0;
        double Bpara = lbi.toBezierParam(poc.parameter());
        return new PointInfo(poc.x(), poc.y(), Apara, Bpara);
    }

    /**
     * ��_��refinement: �A�����̊e���̒l��?�߂�
     *
     * @see Math#solveSimultaneousEquations(PrimitiveMappingND,PrimitiveMappingND[],
     *PrimitiveBooleanMappingNDTo1D,double[])
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
            return 2;
        }

        /**
         * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
         */
        public int numOutputDimensions() {
            return 2;
        }

        public double[] map(double[] parameter) {
            double[] vctr = new double[2];
            Vector2D evec;

            /*
            * sApnt & sBpnt are already computed by previous cnvFunc.map()
            */
            evec = sApnt.subtract(sBpnt);

            vctr[0] = evec.x();
            vctr[1] = evec.y();

            return vctr;
        }
    }

    /**
     * ��_��refinement: �A�����̊e���̕Δ�̒l��?�߂�
     *
     * @see Math#solveSimultaneousEquations(PrimitiveMappingND,PrimitiveMappingND[],
     *PrimitiveBooleanMappingNDTo1D,double[])
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
            return 2;
        }

        /**
         * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
         */
        public int numOutputDimensions() {
            return 2;
        }

        public double[] map(double[] parameter) {
            double[] mtrx = new double[2];
            if (idx == 0) {    /* this must be called first */
                sAtang = dA.tangentVector(dA.parameterDomain().force(parameter[0]));
                sBtang = dB.tangentVector(dB.parameterDomain().force(parameter[1]));
                mtrx[0] = sAtang.x();
                mtrx[1] = (-sBtang.x());
            } else {
                mtrx[0] = sAtang.y();
                mtrx[1] = (-sBtang.y());
            }
            return mtrx;
        }
    }

    /**
     * ��_��refinement: �A�����̉⪎������ǂ����𔻒肷��
     *
     * @see Math#solveSimultaneousEquations(PrimitiveMappingND,PrimitiveMappingND[],
     *PrimitiveBooleanMappingNDTo1D,double[])
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
            return 2;
        }

        public boolean map(double[] parameter) {
            sApnt = dA.coordinates(dA.parameterDomain().force(parameter[0]));
            sBpnt = dB.coordinates(dB.parameterDomain().force(parameter[1]));

            return sApnt.identical(sBpnt);
        }
    }

    private void setbackParams(PointInfo pi, double param[]) {
        Point2D Apnt, Bpnt;

        pi.Apara = dA.parameterDomain().force(param[0]);
        pi.Bpara = dB.parameterDomain().force(param[1]);

        Apnt = dA.coordinates(pi.Apara);
        Bpnt = dB.coordinates(pi.Bpara);

        pi.pnt = Apnt.linearInterpolate(Bpnt, 0.5);
    }

    /**
     * ��_��refinement��?s��
     *
     * @param refine�����_
     * @return �⪎������ǂ���
     */
    private boolean refinePointInfo(PointInfo pinfo) {
        nlFunc nl_func = new nlFunc();
        PrimitiveMappingND[] dnl_func = new PrimitiveMappingND[2];
        dnl_func[0] = new dnlFunc(0);
        dnl_func[1] = new dnlFunc(1);
        cnvFunc cnv_func = new cnvFunc();
        double[] param = new double[2];

        param[0] = pinfo.Apara;
        param[1] = pinfo.Bpara;

        param = GeometryUtils.solveSimultaneousEquations(nl_func, dnl_func, cnv_func, param);
        if (param == null)
            return false;

        setbackParams(pinfo, param);
        return true;
    }

    /**
     * ���POINT�µ����LINE�Ƃ݂Ȃ��ꂽ2�̃Z�O�?���g���m�̌�_�𓾂�?B
     *
     * @param dA POINT�µ����LINE�Ƃ݂Ȃ��ꂽ�Z�O�?���gA
     * @param dB POINT�µ����LINE�Ƃ݂Ȃ��ꂽ�Z�O�?���gB
     */
    private void intersectLines(BezierInfo dA, BezierInfo dB) {
        if (false) {    // for debug
            Point2D pnt1 = dA.bzc.controlPointAt(0);
            Point2D pnt2 = dA.bzc.controlPointAt(dA.bzc.nControlPoints() - 1);
            Point2D pntA = pnt1.linearInterpolate(pnt2, 0.5);
            pnt1 = dB.bzc.controlPointAt(0);
            pnt2 = dB.bzc.controlPointAt(dB.bzc.nControlPoints() - 1);
            Point2D pntB = pnt1.linearInterpolate(pnt2, 0.5);
            Point2D pnt = pntA.linearInterpolate(pntB, 0.5);
            double paramA = (dA.sp + dA.ep) * 0.5;
            double paramB = (dB.sp + dB.ep) * 0.5;

            sol_list.addAsIntersection(pnt, paramA, paramB, aprx_ptolA, aprx_ptolB);
            return;
        }

        PointInfo ints_pnt;

        double x, y, z;
        double Apara, Bpara;
        int i;

        if (dA.crnt_type == POINT) {
            if (dB.crnt_type == POINT) {
                x = (dA.pnt().x() + dB.pnt().x()) / 2.0;
                y = (dA.pnt().y() + dB.pnt().y()) / 2.0;
                Apara = (dA.sp + dA.ep) / 2.0;
                Bpara = (dB.sp + dB.ep) / 2.0;

                ints_pnt = new PointInfo(x, y, Apara, Bpara);

            } else {
                if ((ints_pnt = intersectPntLine(dA, dB)) == null)
                    return;
            }

        } else if (dA.crnt_type == LINE) {
            if (dB.crnt_type == LINE) {
                BoundedLine2D Abln;
                BoundedLine2D Bbln;
                CurveCurveInterference2D intf;

                Abln = new BoundedLine2D(dA.bzc.controlPointAt(0),
                        dA.bzc.controlPointAt(dA.bzc.nControlPoints() - 1));
                Bbln = new BoundedLine2D(dB.bzc.controlPointAt(0),
                        dB.bzc.controlPointAt(dB.bzc.nControlPoints() - 1));

                if ((intf = Abln.interfere1(Bbln)) == null)
                    return;

                if (intf.isIntersectionPoint()) {
                    /*
                    * intersect
                    */
                    IntersectionPoint2D ints = intf.toIntersectionPoint();

                    Apara = dA.toBezierParam(ints.pointOnCurve1().parameter());
                    Bpara = dB.toBezierParam(ints.pointOnCurve2().parameter());

                    ints_pnt = new PointInfo(ints.x(), ints.y(), Apara, Bpara);

                } else {
                    /*
                    * overlap
                    */
                    OverlapCurve2D ovlp = intf.toOverlapCurve();
                    double x1, y1, x2, y2;

                    x1 = dA.toBezierParam(ovlp.start1());
                    y1 = dB.toBezierParam(ovlp.start2());
                    x2 = dA.toBezierParam(ovlp.end1());
                    y2 = dB.toBezierParam(ovlp.end2());

                    sol_list.addAsOverlap(x1, y1, x2 - x1, y2 - y1,
                            aprx_ptolA, aprx_ptolB, aprx_ptolA, aprx_ptolB);

                    return;
                }

            } else {
                if ((ints_pnt = intersectPntLine(dB, dA)) == null) {
                    return;
                }
            }
        } else {    // not reached
            return;
        }

        if (refinePointInfo(ints_pnt))
            sol_list.addAsIntersection(ints_pnt.pnt, ints_pnt.Apara, ints_pnt.Bpara,
                    aprx_ptolA, aprx_ptolB);

        return;
    }

    /**
     * Main Process
     * <p/>
     * ��?ۃZ�O�?���g�Ƃ���rivals�Ƃ̌���𓾂�?B
     * </p><p>
     * ��?ۃZ�O�?���g���_�µ����?�Ƃ݂Ȃ��ꂽ��?A
     * rivals���_�µ����?�Ƃ݂Ȃ����܂ŕ�����?A
     * ��_�𓾂�?B
     * </p><p>
     * ��?ۃZ�O�?���g���܂��_��?�Ƃ݂Ȃ���Ȃ�?�?���?A
     * ��?ۃZ�O�?���g����т���rivals�𕪊���?A
     * ���������O��2�Z�O�?���g�ɑ΂���?ċA�I�Ɏ�?g��Ă�?o��?B
     * </p>
     *
     * @param crnt_bi ��?ۂƂȂ镪���Z�O�?���g
     * @see BezierInfo
     */
    private void getIntersections(BezierInfo crnt_bi) {
        BinaryTree.Node dANode;
        int i;

        /*
        * remove rivals which have no possibility of interference
        */
        int n_rivals = crnt_bi.rivals.size();
        for (i = n_rivals - 1; i >= 0; i--) {
            dANode = (BinaryTree.Node) crnt_bi.rivals.elementAt(i);
            if (!checkInterfere((BezierInfo) dANode.data(), crnt_bi))
                crnt_bi.rivals.removeElementAt(i);
        }
        if (crnt_bi.rivals.size() == 0)
            return;

        /*
        * if current bezier is regarded as line, get intersections
        */
        if (crnt_bi.whatTypeIsBezier() != BEZIER) {
            Vector new_rivals = new Vector();

            boolean all_rivals_are_line = true;

            n_rivals = crnt_bi.rivals.size();
            for (i = 0; i < n_rivals; i++)
                if (divideRivals((BinaryTree.Node) crnt_bi.rivals.elementAt(i), new_rivals))
                    all_rivals_are_line = false;

            crnt_bi.rivals = new_rivals;

            if (!all_rivals_are_line) {
                /*
                * try again
                */
                getIntersections(crnt_bi);
            } else {
                /*
                * get intersections
                */
                n_rivals = crnt_bi.rivals.size();
                for (i = 0; i < n_rivals; i++) {
                    dANode = (BinaryTree.Node) crnt_bi.rivals.elementAt(i);
                    intersectLines((BezierInfo) dANode.data(), crnt_bi);
                }
            }
            return;
        }

        /*
        * generate children (divide current bezier)
        */
        double half_point = 0.5;
        PureBezierCurve2D[] bzcs = crnt_bi.bzc.divide(half_point);

        double g_half_point = (crnt_bi.sp + crnt_bi.ep) / 2.0;

        BezierInfo biL = new BezierInfo(bzcs[0], crnt_bi.sp, g_half_point, true);
        BezierInfo biR = new BezierInfo(bzcs[1], g_half_point, crnt_bi.ep, true);

        /*
        * create children's rival list
        */
        n_rivals = crnt_bi.rivals.size();
        for (i = 0; i < n_rivals; i++)
            divideRivals((BinaryTree.Node) crnt_bi.rivals.elementAt(i), biL.rivals);

        n_rivals = biL.rivals.size();
        for (i = 0; i < n_rivals; i++)
            biR.rivals.addElement(biL.rivals.elementAt(i));

        /*
        * recursive call
        */
        getIntersections(biL);
        getIntersections(biR);
    }

    /**
     * 2�x�W�G��?�̊�?�?�߂�?�?�ʃ?�\�b�h
     */
    private CurveCurveInterferenceList getInterference() {
        BezierInfo dBRoot;

        dBRoot = new BezierInfo(dB, 0.0, 1.0, true);
        dBRoot.rivals.addElement(Atree.rootNode());

        /*
        * Get Intersections
        */
        getIntersections(dBRoot);

        sol_list.removeOverlapsContainedInOtherOverlap();
        sol_list.removeIntersectionsContainedInOverlap();

        return sol_list;
    }

    /**
     * 2�x�W�G��?�̌�_�𓾂�
     *
     * @param poly1 �x�W�G��?�1
     * @param poly2 �x�W�G��?�2
     * @return ��_�̔z��
     * @see IntersectionPoint2D
     */
    static IntersectionPoint2D[] intersection(PureBezierCurve2D bzc1,
                                              PureBezierCurve2D bzc2,
                                              boolean doExchange) {
        IntsBzcBzc2D doObj = new IntsBzcBzc2D(bzc1, bzc2);
        return doObj.getInterference().toIntersectionPoint2DArray(doExchange);
    }

    /**
     * 2�x�W�G��?�̊�?𓾂�
     *
     * @param poly1 �x�W�G��?�1
     * @param poly2 �x�W�G��?�2
     * @return 2��?�̊�?̔z��
     * @see CurveCurveInterference2D
     */
    static CurveCurveInterference2D[] interference(PureBezierCurve2D bzc1,
                                                   PureBezierCurve2D bzc2,
                                                   boolean doExchange) {
        IntsBzcBzc2D doObj = new IntsBzcBzc2D(bzc1, bzc2);
        return doObj.getInterference().toCurveCurveInterference2DArray(doExchange);
    }
}
