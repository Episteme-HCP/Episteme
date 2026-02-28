/*
 * 3D�x�W�G��?�m�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsBzcBzc3D.java,v 1.8 2006/03/01 21:16:01 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.ArrayMathUtils;
import org.episteme.mathematics.analysis.PrimitiveMappingND;

import java.util.Vector;

/**
 * 3D�x�W�G��?�m�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.8 $, $Date: 2006/03/01 21:16:01 $
 */

class IntsBzcBzc3D {
    /**
     * �^����ꂽ�x�W�G��?� A
     *
     * @see PureBezierCurve3D
     */
    private PureBezierCurve3D dA;

    /**
     * �^����ꂽ�x�W�G��?� B
     *
     * @see PureBezierCurve3D
     */
    private PureBezierCurve3D dB;

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

    static final int IGNORE_X = 0;
    static final int IGNORE_Y = 1;
    static final int IGNORE_Z = 2;
    private int ignoredDimension;    // temporally used in refinePointInfo
    private Point3D sApnt;        // temporally used in refinePointInfo
    private Point3D sBpnt;        // temporally used in refinePointInfo
    private Vector3D sAtang;    // temporally used in refinePointInfo
    private Vector3D sBtang;    // temporally used in refinePointInfo

    /**
     * �I�u�W�F�N�g��?\�z����
     *
     * @param bzc1 �x�W�G��?� A
     * @param bzc2 �x�W�G��?� B
     * @see PureBezierCurve3D
     */
    private IntsBzcBzc3D(PureBezierCurve3D bzc1, PureBezierCurve3D bzc2) {
        super();

        dA = bzc1;
        dB = bzc2;

        double d_tol = dA.getToleranceForDistance();

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
        private PureBezierCurve3D bzc;

        /**
         * ���̃x�W�G��?�ł̎n�_�̃p���??[�^
         */
        private double sp;

        /**
         * ���̃x�W�G��?�ł�?I�_�̃p���??[�^
         */
        private double ep;

        /**
         * ��?ݔ͈͂������
         *
         * @see EnclosingBox3D
         */
        private EnclosingBox3D box;

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
        * crnt_type��POINT:	Point3D
        * crnt_type��LINE:	Line3D
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
         * @see PureBezierCurve3D
         */
        private BezierInfo(PureBezierCurve3D bzc, double sp, double ep, boolean hasRivals) {
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
         * geom��Point3D�Ƃ݂Ȃ���?A���̒l��Ԃ�
         *
         * @return geom��Point3D�ɕϊ������l
         */
        private Point3D pnt() {
            return (Point3D) geom;
        }

        /**
         * geom��Line3D�Ƃ݂Ȃ���?A���̒l��Ԃ�
         *
         * @return geom��Line3D�ɕϊ������l
         */
        private Line3D line() {
            return (Line3D) geom;
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

            Vector3D s2e;
            double leng_s2e;
            Vector3D unit_s2e;
            Vector3D s2c;
            Vector3D crsv;
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
                    Point3D pnt_geom
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
                crsv = unit_s2e.crossProduct(s2c);
                if (crsv.length() > d_tol) {
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
            Line3D lin_geom = new Line3D(bzc.controlPointAt(0), s2e);

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
                Line3D line;
                EnclosingBox3D bx = dB.box;
                CartesianTransformationOperator3D trns;
                Vector3D[] xyz = new Vector3D[3];
                Point3D[] box_pnts = new Point3D[8];
                double min_x, min_y, max_x, max_y;
                double d_tol = dA.bzc.getToleranceForDistance();
                int i;

                line = dA.line();
                xyz[2] = line.dir();
                xyz[0] = xyz[2].verticalVector();
                xyz[1] = xyz[2].crossProduct(xyz[0]);
                trns = new CartesianTransformationOperator3D(xyz[0], xyz[1], xyz[2],
                        line.pnt(), 1.0);
                box_pnts[0] = new CartesianPoint3D(bx.min().x(), bx.min().y(), bx.min().z());
                box_pnts[1] = new CartesianPoint3D(bx.max().x(), bx.min().y(), bx.min().z());
                box_pnts[2] = new CartesianPoint3D(bx.max().x(), bx.max().y(), bx.min().z());
                box_pnts[3] = new CartesianPoint3D(bx.min().x(), bx.max().y(), bx.min().z());
                box_pnts[4] = new CartesianPoint3D(bx.min().x(), bx.min().y(), bx.max().z());
                box_pnts[5] = new CartesianPoint3D(bx.max().x(), bx.min().y(), bx.max().z());
                box_pnts[6] = new CartesianPoint3D(bx.max().x(), bx.max().y(), bx.max().z());
                box_pnts[7] = new CartesianPoint3D(bx.min().x(), bx.max().y(), bx.max().z());

                box_pnts[0] = trns.toLocal(box_pnts[0]);
                min_x = max_x = box_pnts[0].x();
                min_y = max_y = box_pnts[0].y();

                for (i = 1; i < 8; i++) {
                    box_pnts[i] = trns.toLocal(box_pnts[i]);
                    min_x = Math.min(box_pnts[i].x(), min_x);
                    min_y = Math.min(box_pnts[i].y(), min_y);
                    max_x = Math.max(box_pnts[i].x(), max_x);
                    max_y = Math.max(box_pnts[i].y(), max_y);
                }
                if (!((min_x < d_tol) &&
                        (min_y < d_tol) &&
                        (max_x > (-d_tol)) &&
                        (max_y > (-d_tol))))
                    return false;    /* no interfere */
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
            PureBezierCurve3D[] bzcs = bi.bzc.divide(half_point);

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
         * @see Point3D
         */
        private Point3D pnt;

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
         * @see Point3D
         */
        private PointInfo(Point3D pnt, double Apara, double Bpara) {
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
        private PointInfo(double x, double y, double z, double Apara, double Bpara) {
            this.pnt = new CartesianPoint3D(x, y, z);
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
        Point3D pnt = pbi.pnt();
        Line3D line = lbi.line();
        BoundedLine3D bln = new BoundedLine3D(line.pnt(), line.dir());
        PointOnCurve3D poc;

        if ((poc = bln.project1From(pnt)) == null)
            return null;        // no solution

        double Apara = (pbi.sp + pbi.ep) / 2.0;
        double Bpara = lbi.toBezierParam(poc.parameter());
        return new PointInfo(poc.x(), poc.y(), poc.z(), Apara, Bpara);
    }

    private double[] setupParams(PointInfo pinfo) {
        double[] param = new double[2];
        param[0] = pinfo.Apara;
        param[1] = pinfo.Bpara;
        double Apara = dA.parameterDomain().force(param[0]);
        double Bpara = dB.parameterDomain().force(param[1]);

        Vector3D Atang = dA.tangentVector(Apara);
        if (Atang.identical(Vector3D.zeroVector)) {
            if (Apara < 0.5)
                Apara += 0.01;
            else
                Apara -= 0.01;
            Atang = dA.tangentVector(Apara);
        }
        Vector3D Btang = dB.tangentVector(Bpara);
        if (Btang.identical(Vector3D.zeroVector)) {
            if (Bpara < 0.5)
                Bpara += 0.01;
            else
                Bpara -= 0.01;
            Btang = dB.tangentVector(Bpara);
        }

        Vector3D ecrs = Atang.crossProduct(Btang);

        ignoredDimension = (Math.abs(ecrs.x()) > Math.abs(ecrs.y())) ? IGNORE_X : IGNORE_Y;
        if (ignoredDimension == IGNORE_X) {
            if (Math.abs(ecrs.x()) < Math.abs(ecrs.z()))
                ignoredDimension = IGNORE_Z;
        } else {
            if (Math.abs(ecrs.y()) < Math.abs(ecrs.z()))
                ignoredDimension = IGNORE_Z;
        }

        return param;
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
            Vector3D evec;

            /*
            * sApnt & sBpnt are already computed by previous cnvFunc.map()
            */
            evec = sApnt.subtract(sBpnt);

            switch (ignoredDimension) {
                case IGNORE_X:
                    vctr[0] = evec.y();
                    vctr[1] = evec.z();
                    break;
                case IGNORE_Y:
                    vctr[0] = evec.z();
                    vctr[1] = evec.x();
                    break;
                case IGNORE_Z:
                    vctr[0] = evec.x();
                    vctr[1] = evec.y();
                    break;
            }

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
                switch (ignoredDimension) {
                    case IGNORE_X:
                        mtrx[0] = sAtang.y();
                        mtrx[1] = (-sBtang.y());
                        break;
                    case IGNORE_Y:
                        mtrx[0] = sAtang.z();
                        mtrx[1] = (-sBtang.z());
                        break;
                    case IGNORE_Z:
                        mtrx[0] = sAtang.x();
                        mtrx[1] = (-sBtang.x());
                        break;
                }
            } else {
                switch (ignoredDimension) {
                    case IGNORE_X:
                        mtrx[0] = sAtang.z();
                        mtrx[1] = (-sBtang.z());
                        break;
                    case IGNORE_Y:
                        mtrx[0] = sAtang.x();
                        mtrx[1] = (-sBtang.x());
                        break;
                    case IGNORE_Z:
                        mtrx[0] = sAtang.y();
                        mtrx[1] = (-sBtang.y());
                        break;
                }
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
        Point3D Apnt, Bpnt;

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
        double[] param = setupParams(pinfo);

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
            Point3D pnt1 = dA.bzc.controlPointAt(0);
            Point3D pnt2 = dA.bzc.controlPointAt(dA.bzc.nControlPoints() - 1);
            Point3D pntA = pnt1.linearInterpolate(pnt2, 0.5);
            pnt1 = dB.bzc.controlPointAt(0);
            pnt2 = dB.bzc.controlPointAt(dB.bzc.nControlPoints() - 1);
            Point3D pntB = pnt1.linearInterpolate(pnt2, 0.5);
            Point3D pnt = pntA.linearInterpolate(pntB, 0.5);
            double paramA = (dA.sp + dA.ep) * 0.5;
            double paramB = (dB.sp + dB.ep) * 0.5;

            sol_list.addAsIntersection(pnt, paramA, paramB);
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
                z = (dA.pnt().z() + dB.pnt().z()) / 2.0;
                Apara = (dA.sp + dA.ep) / 2.0;
                Bpara = (dB.sp + dB.ep) / 2.0;

                ints_pnt = new PointInfo(x, y, z, Apara, Bpara);

            } else {
                if ((ints_pnt = intersectPntLine(dA, dB)) == null)
                    return;
            }

        } else if (dA.crnt_type == LINE) {
            if (dB.crnt_type == LINE) {
                BoundedLine3D Abln;
                BoundedLine3D Bbln;
                CurveCurveInterference3D intf;

                Abln = new BoundedLine3D(dA.bzc.controlPointAt(0),
                        dA.bzc.controlPointAt(dA.bzc.nControlPoints() - 1));
                Bbln = new BoundedLine3D(dB.bzc.controlPointAt(0),
                        dB.bzc.controlPointAt(dB.bzc.nControlPoints() - 1));

                if ((intf = Abln.interfere1(Bbln)) == null)
                    return;

                if (intf.isIntersectionPoint()) {
                    /*
                    * intersect
                    */
                    IntersectionPoint3D ints = intf.toIntersectionPoint();

                    Apara = dA.toBezierParam(ints.pointOnCurve1().parameter());
                    Bpara = dB.toBezierParam(ints.pointOnCurve2().parameter());

                    ints_pnt = new PointInfo(ints.x(), ints.y(), ints.z(), Apara, Bpara);

                } else {
                    /*
                    * overlap
                    */
                    OverlapCurve3D ovlp = intf.toOverlapCurve();
                    double x1, y1, x2, y2;

                    x1 = dA.toBezierParam(ovlp.start1());
                    y1 = dB.toBezierParam(ovlp.start2());
                    x2 = dA.toBezierParam(ovlp.end1());
                    y2 = dB.toBezierParam(ovlp.end2());

                    sol_list.addAsOverlap(x1, y1, x2 - x1, y2 - y1);

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
            sol_list.addAsIntersection(ints_pnt.pnt, ints_pnt.Apara, ints_pnt.Bpara);

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
        PureBezierCurve3D[] bzcs = crnt_bi.bzc.divide(half_point);

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
     * @see IntersectionPoint3D
     */
    static IntersectionPoint3D[] intersection(PureBezierCurve3D bzc1,
                                              PureBezierCurve3D bzc2,
                                              boolean doExchange) {
        IntsBzcBzc3D doObj = new IntsBzcBzc3D(bzc1, bzc2);
        return doObj.getInterference().toIntersectionPoint3DArray(doExchange);
    }

    /**
     * 2�x�W�G��?�̊�?𓾂�
     *
     * @param poly1 �x�W�G��?�1
     * @param poly2 �x�W�G��?�2
     * @return 2��?�̊�?̔z��
     * @see CurveCurveInterference3D
     */
    static CurveCurveInterference3D[] interference(PureBezierCurve3D bzc1,
                                                   PureBezierCurve3D bzc2,
                                                   boolean doExchange) {
        IntsBzcBzc3D doObj = new IntsBzcBzc3D(bzc1, bzc2);
        return doObj.getInterference().toCurveCurveInterference3DArray(doExchange);
    }
}
