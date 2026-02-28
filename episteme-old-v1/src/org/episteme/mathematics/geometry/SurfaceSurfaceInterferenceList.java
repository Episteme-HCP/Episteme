/*
 * �Ȗʓ��m�̊�?̃��X�g�赂��N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SurfaceSurfaceInterferenceList.java,v 1.3 2007-10-21 21:08:20 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.ArrayMathUtils;
import org.episteme.mathematics.analysis.PrimitiveMappingND;
import org.episteme.util.FatalException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * �Ȗʓ��m�̊�?̃��X�g�赂��N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:20 $
 */
class SurfaceSurfaceInterferenceList {
    static int debugFlag = 0;
    static final int DEBUG_TRACE = 1;
    static final int OMIT_BOUNDARY_CHECK = 2;

    static final boolean REMOVE_NO_LENGTH_SEGMENT = false;

    /**
     * �Ȗ� A
     */
    ParametricSurface3D surfaceA;

    /**
     * �Ȗ� A �̒�`��?��
     */
    ParameterDomain uParameterDomainA;
    ParameterDomain vParameterDomainA;

    /**
     * �Ȗ� B
     */
    ParametricSurface3D surfaceB;

    /**
     * �Ȗ� B �̒�`��?��
     */
    ParameterDomain uParameterDomainB;
    ParameterDomain vParameterDomainB;

    /**
     * �����̋��e��?� (�I�u�W�F�N�g��?\�z���ꂽ���_�ł�)
     */
    ToleranceForDistance dTol;

    /**
     * ��_�̃��X�g
     */
    Vector listOfPoints;

    /**
     * ��?�̃��X�g
     */
    Vector listOfCurves;

    /**
     * ���f����Ă����?�Z�O�?���g�̃��X�g
     */
    Vector listOfCurveSegments;

    /*
    * �I�u�W�F�N�g��?\�z����
    *
    * @param	surfaceA	�Ȗ� A
    * @param	surfaceB	�Ȗ� B
    */
    SurfaceSurfaceInterferenceList(ParametricSurface3D surfaceA,
                                   ParametricSurface3D surfaceB) {
        if ((surfaceA == null) || (surfaceB == null))
            throw new NullArgumentException();

        this.surfaceA = surfaceA;
        this.uParameterDomainA = surfaceA.uParameterDomain();
        this.vParameterDomainA = surfaceA.vParameterDomain();

        this.surfaceB = surfaceB;
        this.uParameterDomainB = surfaceB.uParameterDomain();
        this.vParameterDomainB = surfaceB.vParameterDomain();

        ConditionOfOperation cond = ConditionOfOperation.getCondition();
        this.dTol = cond.getToleranceForDistanceAsObject();

        this.listOfPoints = new Vector();
        this.listOfCurves = new Vector();
        this.listOfCurveSegments = new Vector();
    }

    /**
     * �Ȗʂ̂���p���??[�^�l�t�߂ł�U���̃p���??[�^�̋��e��?���?�߂�?B
     * <p/>
     * ToleranceForDistance ����Z?o
     *
     * @param surface �Ȗ�
     * @param uParam  U���p���??[�^�l
     * @param vParam  V���p���??[�^�l
     */
    private double getToleranceForParameterU(AbstractParametricSurface surface,
                                             double uParam, double vParam) {
        return dTol.toToleranceForParameterU((ParametricSurface3D) surface,
                uParam, vParam).value();
    }

    /**
     * ?ڑ��_���Ȗʂ̂Ȃ��ڂ�z����?�?��Ƀp���??[�^�l���?�����
     *
     * @param domain    �p���??[�^�h�?�C��
     * @param param     ��?ۃp���??[�^
     * @param lastParam ���O�̃p���??[�^
     * @return ��?����ꂽ�p���??[�^
     */
    private static double adjustParam(ParameterDomain domain,
                                      double param,
                                      double lastParam) {
        if (domain.isNonPeriodic())
            return param;

        double absInc = domain.section().absIncrease();
        double threshold = lastParam - absInc / 2.0;
        while (param < threshold)
            param += absInc;
        threshold = lastParam + absInc / 2.0;
        while (param > threshold)
            param -= absInc;
        return param;
    }

    /**
     * ��?�2D�p���??[�^��Ԃŕ��Ă��邩�ǂ����𒲂ׂ�
     *
     * @param uDomain  U���̃p���??[�^�h�?�C��
     * @param vDomain  V���̃p���??[�^�h�?�C��
     * @param uSParam  �n�_��U�p���??[�^
     * @param vSParam  �n�_��V�p���??[�^
     * @param uEParam  ?I�_��U�p���??[�^
     * @param vEParam  ?I�_��V�p���??[�^
     * @param closed3D 3D���ŕ��Ă��邩?
     * @return ���Ă��邩�ǂ���
     */
    private static boolean checkClosed2D(ParameterDomain uDomain,
                                         ParameterDomain vDomain,
                                         double uSParam, double vSParam,
                                         double uEParam, double vEParam,
                                         boolean closed3D) {
        if (!closed3D)
            return false;    // 3D���ŊJ���Ă���Ȃ�K���J���Ă���

        if (uDomain.isPeriodic()) {
            if (Math.abs(uEParam - uSParam) > uDomain.section().absIncrease() / 2.0)
                return false;
        }
        if (vDomain.isPeriodic()) {
            if (Math.abs(vEParam - vSParam) > vDomain.section().absIncrease() / 2.0)
                return false;
        }
        return true;
    }

    /**
     * �Ȗʂ̂���p���??[�^�l�t�߂ł�V���̃p���??[�^�̋��e��?���?�߂�?B
     * <p/>
     * ToleranceForDistance ����Z?o
     *
     * @param surface �Ȗ�
     * @param uParam  U���p���??[�^�l
     * @param vParam  V���p���??[�^�l
     */
    private double getToleranceForParameterV(AbstractParametricSurface surface,
                                             double uParam, double vParam) {
        return dTol.toToleranceForParameterV((ParametricSurface3D) surface,
                uParam, vParam).value();
    }

    /*
    * ���_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A�Ɋւ����?�
    */
    private static final int PARAMETERS_NOT_IDENTICAL = 0x0;
    private static final int PARAMETERS_IDENTICAL = 0x1;
    private static final int PARAMETERS_CROSSBOUNDARY_U_A = 0x2;
    private static final int PARAMETERS_CROSSBOUNDARY_V_A = 0x4;
    private static final int PARAMETERS_CROSSBOUNDARY_U_B = 0x8;
    private static final int PARAMETERS_CROSSBOUNDARY_V_B = 0x10;

    /**
     * ���_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A��\��
     */
    class ParametricalIdentityOfTwoIntersections {
        /**
         * ���_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A��\��?�?�
         */
        private int value;

        /**
         * �I�u�W�F�N�g��?\�z����
         */
        ParametricalIdentityOfTwoIntersections() {
            setNonIdentical();
        }

        /**
         * ���_�̃p���??[�^�l������Ƃ݂Ȃ��Ȃ���̂Ƃ���
         */
        private void setNonIdentical() {
            value = PARAMETERS_NOT_IDENTICAL;
        }

        /**
         * ���_�̃p���??[�^�l������Ƃ݂Ȃ����̂Ƃ���
         */
        private void setIdentical() {
            value |= PARAMETERS_IDENTICAL;
        }

        /**
         * ���_�̃p���??[�^�l���Ȗ� A ��U���E��ׂ���̂Ƃ���
         */
        private void setCrossBoundaryOfAu() {
            value |= PARAMETERS_CROSSBOUNDARY_U_A;
        }

        /**
         * ���_�̃p���??[�^�l���Ȗ� A ��V���E��ׂ���̂Ƃ���
         */
        private void setCrossBoundaryOfAv() {
            value |= PARAMETERS_CROSSBOUNDARY_V_A;
        }

        /**
         * ���_�̃p���??[�^�l���Ȗ� B ��U���E��ׂ���̂Ƃ���
         */
        private void setCrossBoundaryOfBu() {
            value |= PARAMETERS_CROSSBOUNDARY_U_B;
        }

        /**
         * ���_�̃p���??[�^�l���Ȗ� B ��V���E��ׂ���̂Ƃ���
         */
        private void setCrossBoundaryOfBv() {
            value |= PARAMETERS_CROSSBOUNDARY_V_B;
        }

        /**
         * ���_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�
         */
        private boolean isIdentical() {
            return ((value & PARAMETERS_IDENTICAL) != 0);
        }

        /**
         * ���_�̃p���??[�^�l���Ȗ� A ��U���̋��E��ׂ����ۂ�
         */
        private boolean isCrossBoundaryOfAu() {
            return ((value & PARAMETERS_CROSSBOUNDARY_U_A) != 0);
        }

        /**
         * ���_�̃p���??[�^�l���Ȗ� A ��V���̋��E��ׂ����ۂ�
         */
        private boolean isCrossBoundaryOfAv() {
            return ((value & PARAMETERS_CROSSBOUNDARY_V_A) != 0);
        }

        /**
         * ���_�̃p���??[�^�l���Ȗ� B ��U���̋��E��ׂ����ۂ�
         */
        private boolean isCrossBoundaryOfBu() {
            return ((value & PARAMETERS_CROSSBOUNDARY_U_B) != 0);
        }

        /**
         * ���_�̃p���??[�^�l���Ȗ� B ��V���̋��E��ׂ����ۂ�
         */
        private boolean isCrossBoundaryOfBv() {
            return ((value & PARAMETERS_CROSSBOUNDARY_V_B) != 0);
        }
    }

    /**
     * ��_��?��
     */
    class PointInfo {
        /**
         * ��_��?W�l (null ���µ��Ȃ�)
         */
        Point3D coord;

        /**
         * ��_�̋Ȗ� A �ł�U���̃p���??[�^�l
         */
        double uParamA;

        /**
         * ��_�̋Ȗ� A �ł�V���̃p���??[�^�l
         */
        double vParamA;

        /**
         * ��_�̋Ȗ� B �ł�U���̃p���??[�^�l
         */
        double uParamB;

        /**
         * ��_�̋Ȗ� B �ł�V���̃p���??[�^�l
         */
        double vParamB;

        /**
         * �Ȗ� A �� [uv]ParamA �t�߂ł̃p���??[�^��U���̋��e��?�
         */
        double pTolAu;

        /**
         * �Ȗ� A �� [uv]ParamA �t�߂ł̃p���??[�^��V���̋��e��?�
         */
        double pTolAv;

        /**
         * �Ȗ� B �� [uv]ParamB �t�߂ł̃p���??[�^��U���̋��e��?�
         */
        double pTolBu;

        /**
         * �Ȗ� B �� [uv]ParamB �t�߂ł̃p���??[�^��V���̋��e��?�
         */
        double pTolBv;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param coord   ��_��?W�l (null ���µ��Ȃ�)
         * @param uParamA ��_�̋Ȗ� A �ł�U���p���??[�^�l
         * @param vParamA ��_�̋Ȗ� A �ł�V���p���??[�^�l
         * @param uParamB ��_�̋Ȗ� B �ł�U���p���??[�^�l
         * @param vParamB ��_�̋Ȗ� B �ł�V���p���??[�^�l
         */
        PointInfo(Point3D coord,
                  double uParamA, double vParamA,
                  double uParamB, double vParamB) {
            this.coord = coord;    // null ���µ��Ȃ�

            this.uParamA = uParamA;
            this.vParamA = vParamA;
            this.uParamB = uParamB;
            this.vParamB = vParamB;

            this.pTolAu = getToleranceForParameterU(surfaceA, uParamA, vParamA);
            this.pTolAv = getToleranceForParameterV(surfaceA, uParamA, vParamA);
            this.pTolBu = getToleranceForParameterU(surfaceB, uParamB, vParamB);
            this.pTolBv = getToleranceForParameterV(surfaceB, uParamB, vParamB);
        }

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param coord   ��_��?W�l (null ���µ��Ȃ�)
         * @param uParamA ��_�̋Ȗ� A �ł�U���p���??[�^�l
         * @param vParamA ��_�̋Ȗ� A �ł�V���p���??[�^�l
         * @param uParamB ��_�̋Ȗ� B �ł�U���p���??[�^�l
         * @param vParamB ��_�̋Ȗ� B �ł�V���p���??[�^�l
         * @param pTolAu  �Ȗ� A �� [uv]ParamA �t�߂ł�U���p���??[�^�̋��e��?�
         * @param pTolAv  �Ȗ� A �� [uv]ParamA �t�߂ł�V���p���??[�^�̋��e��?�
         * @param pTolBu  �Ȗ� B �� [uv]ParamB �t�߂ł�U���p���??[�^�̋��e��?�
         * @param pTolBv  �Ȗ� B �� [uv]ParamB �t�߂ł�V���p���??[�^�̋��e��?�
         */
        PointInfo(Point3D coord,
                  double uParamA, double vParamA,
                  double uParamB, double vParamB,
                  double pTolAu, double pTolAv,
                  double pTolBu, double pTolBv) {
            this.coord = coord;    // null ���µ��Ȃ�

            this.uParamA = uParamA;
            this.vParamA = vParamA;
            this.uParamB = uParamB;
            this.vParamB = vParamB;

            this.pTolAu = pTolAu;
            this.pTolAv = pTolAv;
            this.pTolBu = pTolBu;
            this.pTolBv = pTolBv;
        }

        private Point3D coordinates() {
            if (coord != null)
                return coord;

            Point3D pntA = surfaceA.coordinates(uParamA, vParamA);
            Point3D pntB = surfaceB.coordinates(uParamB, vParamB);
            return pntA.linearInterpolate(pntB, 0.5);
        }

        /**
         * ���_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A�ɂ��Ă�?��𓾂�
         *
         * @param mate ��_��?��
         */
        private ParametricalIdentityOfTwoIntersections getParametricalIdentityWith(PointInfo mate) {
            ParametricalIdentityOfTwoIntersections result =
                    new ParametricalIdentityOfTwoIntersections();

            if (this == mate) {
                result.setIdentical();
                return result;
            }

            double diffAu = Math.abs(this.uParamA - mate.uParamA);
            double diffAv = Math.abs(this.vParamA - mate.vParamA);
            double diffBu = Math.abs(this.uParamB - mate.uParamB);
            double diffBv = Math.abs(this.vParamB - mate.vParamB);

            double pTolAu = Math.max(this.pTolAu, mate.pTolAu);
            double pTolAv = Math.max(this.pTolAv, mate.pTolAv);
            double pTolBu = Math.max(this.pTolBu, mate.pTolBu);
            double pTolBv = Math.max(this.pTolBv, mate.pTolBv);

            if ((uParameterDomainA.isPeriodic() == true) &&
                    (Math.abs(diffAu - uParameterDomainA.section().absIncrease()) < pTolAu))
                result.setCrossBoundaryOfAu();

            if ((vParameterDomainA.isPeriodic() == true) &&
                    (Math.abs(diffAv - vParameterDomainA.section().absIncrease()) < pTolAv))
                result.setCrossBoundaryOfAv();

            if ((uParameterDomainB.isPeriodic() == true) &&
                    (Math.abs(diffBu - uParameterDomainB.section().absIncrease()) < pTolBu))
                result.setCrossBoundaryOfBu();

            if ((vParameterDomainB.isPeriodic() == true) &&
                    (Math.abs(diffBv - vParameterDomainB.section().absIncrease()) < pTolBv))
                result.setCrossBoundaryOfBv();

            if (((result.isCrossBoundaryOfAu() == true) || (diffAu < pTolAu)) &&
                    ((result.isCrossBoundaryOfAv() == true) || (diffAv < pTolAv)) &&
                    ((result.isCrossBoundaryOfBu() == true) || (diffBu < pTolBu)) &&
                    ((result.isCrossBoundaryOfBv() == true) || (diffBv < pTolBv)))
                result.setIdentical();

            return result;
        }

        /**
         * ���_������Ƃ݂Ȃ��邩�ۂ�
         *
         * @param mate ��_��?��
         */
        private boolean isIdenticalWith(PointInfo mate) {
            if ((this.coord != null) && (mate.coord != null)) {
                if (this.coord.identical(mate.coord) != true)
                    return false;
            }

            return this.getParametricalIdentityWith(mate).isIdentical();
        }

        /**
         * ��_�����E?�ɂ��邩�ǂ���
         *
         * @retrun ���E?�ɂ����true
         */
        private boolean isBoundary() {
            double u_param_a, v_param_a, u_param_b, v_param_b;
            PointInfo pinfo;
            for (int i = 0; i < 8; i++) {
                u_param_a = uParamA;
                v_param_a = vParamA;
                u_param_b = uParamB;
                v_param_b = vParamB;
                if (i < 2) {
                    if (uParameterDomainA.isPeriodic() || uParameterDomainA.isInfinite())
                        continue;
                    if (i == 0)
                        u_param_a = uParameterDomainA.section().start();
                    else
                        u_param_a = uParameterDomainA.section().end();
                } else if (i < 4) {
                    if (vParameterDomainA.isPeriodic() || vParameterDomainA.isInfinite())
                        continue;
                    if (i == 2)
                        v_param_a = vParameterDomainA.section().start();
                    else
                        v_param_a = vParameterDomainA.section().end();
                } else if (i < 6) {
                    if (uParameterDomainB.isPeriodic() || uParameterDomainB.isInfinite())
                        continue;
                    if (i == 4)
                        u_param_b = uParameterDomainB.section().start();
                    else
                        u_param_b = uParameterDomainB.section().end();
                } else {
                    if (vParameterDomainB.isPeriodic() || vParameterDomainB.isInfinite())
                        continue;
                    if (i == 6)
                        v_param_b = vParameterDomainB.section().start();
                    else
                        v_param_b = vParameterDomainB.section().end();
                }
                pinfo = new PointInfo(null, u_param_a, v_param_a, u_param_b, v_param_b);
                if (isIdenticalWith(pinfo))
                    return true;
            }

            return false;
        }

        /**
         * �^����ꂽ�p���??[�^�̋��E�����?���Ԃ�
         */
        private double getBoundaryGap(int which, int side) {
            double para;

            switch (which) {
                default:
                    throw new FatalException();
                case Wp_Au:
                    para = uParamA;
                    break;
                case Wp_Av:
                    para = vParamA;
                    break;
                case Wp_Bu:
                    para = uParamB;
                    break;
                case Wp_Bv:
                    para = vParamB;
                    break;
            }

            if (side == UPPER)
                return Math.abs(1.0 - para);
            else
                return Math.abs(para);
        }
    }

    // BoundaryInfo indicator
    static final int Wp_No = 0;
    static final int Wp_Au = 1;
    static final int Wp_Av = 2;
    static final int Wp_Bu = 3;
    static final int Wp_Bv = 4;

    static final int Wp_START = 1;
    static final int Wp_END = 5;

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
            double pTol = ConditionOfOperation.getCondition()
                    .getToleranceForParameter();
            param = ref_param;
            is_boundary = true;
            this.pi = pi;

            setBoundaryInfo(pTol);
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

        RefineInfo() {
        }

        double[] setupParams(PointInfo pi) {
            double[] param = new double[3];

            switch (bi.param) {
                case Wp_Au:
                    fx_param = pi.uParamA;
                    param[0] = pi.vParamA;
                    param[1] = pi.uParamB;
                    param[2] = pi.vParamB;
                    break;
                case Wp_Av:
                    param[0] = pi.uParamA;
                    fx_param = pi.vParamA;
                    param[1] = pi.uParamB;
                    param[2] = pi.vParamB;
                    break;
                case Wp_Bu:
                    param[0] = pi.uParamA;
                    param[1] = pi.vParamA;
                    fx_param = pi.uParamB;
                    param[2] = pi.vParamB;
                    break;
                case Wp_Bv:
                    param[0] = pi.uParamA;
                    param[1] = pi.vParamA;
                    param[2] = pi.uParamB;
                    fx_param = pi.vParamB;
                    break;
            }

            return param;
        }

        void fillParam(double[] param) {
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

            A[0] = surfaceA.uParameterDomain().force(A[0]);
            A[1] = surfaceA.vParameterDomain().force(A[1]);
            B[0] = surfaceB.uParameterDomain().force(B[0]);
            B[1] = surfaceB.vParameterDomain().force(B[1]);

            sA_pnt = surfaceA.coordinates(A[0], A[1]);
            sB_pnt = surfaceB.coordinates(B[0], B[1]);

            Atang = surfaceA.tangentVector(A[0], A[1]);
            Btang = surfaceB.tangentVector(B[0], B[1]);
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

    private RefineInfo ri;

    private double[] setupParams(PointInfo pi, PointInfo b_pi) {
        BoundaryInfo bi;
        bi = ri.bi = new BoundaryInfo(Wp_No, pi);

        if (bi.param == Wp_No) {
            /*
            * point is in the internal area of surfaces
            *
            * select a parameter which gap with previous points is maximum
            * as fixed one
            */
            double Au_gap = Math.abs(pi.uParamA - b_pi.uParamA);
            double Av_gap = Math.abs(pi.vParamA - b_pi.vParamA);
            double Bu_gap = Math.abs(pi.uParamB - b_pi.uParamB);
            double Bv_gap = Math.abs(pi.vParamB - b_pi.vParamB);
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
            double pTol = ConditionOfOperation.getCondition()
                    .getToleranceForParameter();

            if ((b_bi.param == bi.param && b_bi.wend == bi.wend) ||
                    b_bi.param == Wp_No) {
                switch (bi.param) {
                    case Wp_Au:
                        if ((Math.abs(pi.uParamA - b_pi.uParamA) < pTol) &&
                                (Math.abs(pi.vParamA - b_pi.vParamA) > pTol))
                            bi.param = Wp_Av;
                        break;

                    case Wp_Av:
                        if ((Math.abs(pi.vParamA - b_pi.vParamA) < pTol) &&
                                (Math.abs(pi.uParamA - b_pi.uParamA) > pTol))
                            bi.param = Wp_Au;
                        break;

                    case Wp_Bu:
                        if ((Math.abs(pi.uParamB - b_pi.uParamB) < pTol) &&
                                (Math.abs(pi.vParamB - b_pi.vParamB) > pTol))
                            bi.param = Wp_Bv;
                        break;

                    case Wp_Bv:
                        if ((Math.abs(pi.vParamB - b_pi.vParamB) < pTol) &&
                                (Math.abs(pi.uParamB - b_pi.uParamB) > pTol))
                            bi.param = Wp_Bu;
                        break;
                }
            }
        }
        return ri.setupParams(pi);
    }

    private double[] reSetupParams(PointInfo pi) {
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

    // XXX: clip should use ?ParameterDomain?

    private void clipParam(int n, double param[]) {
        if (param[n] < 0.0) param[n] = 0.0;
        if (param[n] > 1.0) param[n] = 1.0;
    }

    private boolean setbackParams(PointInfo pi, double[] param,
                                  PointInfo b_pi, PointInfo a_pi) {
        double[] A_param = new double[2];
        double[] B_param = new double[2];
        Vector2D evec1, evec2;

        // XXX: clip
        switch (ri.bi.param) {
            case Wp_Au:
                clipParam(1, param);
                clipParam(2, param);
                A_param[0] = pi.uParamA;
                A_param[1] = param[0];
                B_param[0] = param[1];
                B_param[1] = param[2];
                break;
            case Wp_Av:
                clipParam(1, param);
                clipParam(2, param);
                A_param[0] = param[0];
                A_param[1] = pi.vParamA;
                B_param[0] = param[1];
                B_param[1] = param[2];
                break;
            case Wp_Bu:
                clipParam(2, param);
                A_param[0] = param[0];
                A_param[1] = param[1];
                B_param[0] = pi.uParamB;
                B_param[1] = param[2];
                break;
            case Wp_Bv:
                clipParam(2, param);
                A_param[0] = param[0];
                A_param[1] = param[1];
                B_param[0] = param[2];
                B_param[1] = pi.vParamB;
                break;
        }

        evec1 = new LiteralVector2D(A_param[0] - b_pi.uParamA,
                A_param[1] - b_pi.vParamA);
        evec2 = new LiteralVector2D(pi.uParamA - b_pi.uParamA,
                pi.vParamA - b_pi.vParamA);
        if (evec1.dotProduct(evec2) < 0.0)
            return false;

        evec1 = new LiteralVector2D(A_param[0] - a_pi.uParamA,
                A_param[1] - a_pi.vParamA);
        evec2 = new LiteralVector2D(pi.uParamA - a_pi.uParamA,
                pi.vParamA - a_pi.vParamA);
        if (evec1.dotProduct(evec2) < 0.0)
            return false;

        evec1 = new LiteralVector2D(B_param[0] - b_pi.uParamB,
                B_param[1] - b_pi.vParamB);
        evec2 = new LiteralVector2D(pi.uParamB - b_pi.uParamB,
                pi.vParamB - b_pi.vParamB);
        if (evec1.dotProduct(evec2) < 0.0)
            return false;

        evec1 = new LiteralVector2D(B_param[0] - a_pi.uParamB,
                B_param[1] - a_pi.vParamB);
        evec2 = new LiteralVector2D(pi.uParamB - a_pi.uParamB,
                pi.vParamB - a_pi.vParamB);
        if (evec1.dotProduct(evec2) < 0.0)
            return false;

        pi.uParamA = A_param[0];
        pi.vParamA = A_param[1];
        pi.uParamB = B_param[0];
        pi.vParamB = B_param[1];

        return true;
    }

    private boolean refinePointInfo(PointInfo pinfo,
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

        param = setupParams(pinfo, b_pinfo);

        param = GeometryUtils.solveSimultaneousEquations(nl_func, dnl_func,
                cnv_func, param);
        if (param == null && do_retry) {
            // make another parameter fixed, then retry
            param = reSetupParams(pinfo);
            param = GeometryUtils.solveSimultaneousEquations(nl_func, dnl_func,
                    cnv_func, param);
        }

        if (param == null) {
            return false;
        }

        if (!setbackParams(pinfo, param, b_pinfo, a_pinfo)) {
            return false;
        }
        return true;
    }

    /**
     * ��_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��_�Ɠ���Ƃ݂Ȃ����_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ��_�͒ǉB��Ȃ�
     *
     * @param theIntersection ��_
     */
    void addIntersectionPoint(PointInfo theIntersection) {
        for (Enumeration e = listOfPoints.elements(); e.hasMoreElements();)
            if (theIntersection.isIdenticalWith((PointInfo) e.nextElement()) == true)
                return;

        listOfPoints.addElement(theIntersection);
    }

    /**
     * ��_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��_�Ɠ���Ƃ݂Ȃ����_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ��_�͒ǉB��Ȃ�
     *
     * @param coord   ��_��?W�l (null ���µ��Ȃ�)
     * @param uParamA ��_�̋Ȗ� A �ł�U���̃p���??[�^�l
     * @param vParamA ��_�̋Ȗ� A �ł�V���̃p���??[�^�l
     * @param uParamB ��_�̋Ȗ� B �ł�U���̃p���??[�^�l
     * @param vParamB ��_�̋Ȗ� B �ł�V���̃p���??[�^�l
     */
    void addAsIntersectionPoint(Point3D coord,
                                double uParamA,
                                double vParamA,
                                double uParamB,
                                double vParamB) {
        if ((debugFlag & DEBUG_TRACE) != 0) {
            coord.output(System.out);

            surfaceA.coordinates(uParamA, vParamA).output(System.out);
            surfaceB.coordinates(uParamB, vParamB).output(System.out);
        }

        addIntersectionPoint(new PointInfo(coord, uParamA, vParamA, uParamB, vParamB));
    }

    /**
     * ��_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��_�Ɠ���Ƃ݂Ȃ����_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ��_�͒ǉB��Ȃ�
     *
     * @param coord   ��_��?W�l (null ���µ��Ȃ�)
     * @param uParamA ��_�̋Ȗ� A �ł�U���̃p���??[�^�l
     * @param vParamA ��_�̋Ȗ� A �ł�V���̃p���??[�^�l
     * @param uParamB ��_�̋Ȗ� B �ł�U���̃p���??[�^�l
     * @param vParamB ��_�̋Ȗ� B �ł�V���̃p���??[�^�l
     * @param pTolAu  �Ȗ� A �� [uv]ParamA �t�߂ł�U���̃p���??[�^�̋��e��?�
     * @param pTolAv  �Ȗ� A �� [uv]ParamA �t�߂ł�V���̃p���??[�^�̋��e��?�
     * @param pTolBu  �Ȗ� B �� [uv]ParamB �t�߂ł�U���̃p���??[�^�̋��e��?�
     * @param pTolBv  �Ȗ� B �� [uv]ParamB �t�߂ł�V���̃p���??[�^�̋��e��?�
     */
    void addAsIntersectionPoint(Point3D coord,
                                double uParamA,
                                double vParamA,
                                double uParamB,
                                double vParamB,
                                double pTolAu,
                                double pTolAv,
                                double pTolBu,
                                double pTolBv) {
        addIntersectionPoint(new PointInfo(coord, uParamA, vParamA, uParamB, vParamB,
                pTolAu, pTolAv, pTolBu, pTolBv));
    }

    /**
     * ��?��?��
     * (�|�����C���Ɍ���)
     */
    class CurveInfo {
        Vector segments;

        private CurveInfo(CurveSegmentInfo firstSegment) {
            segments = new Vector();
            segments.addElement(firstSegment);
        }

        private int nPoints() {
            int nPoints = segmentAt(0).nPoints();
            for (int i = 1; i < nSegments(); i++)
                nPoints += segmentAt(i).nPoints() - 1;
            return nPoints;
        }

        private int nSegments() {
            return segments.size();
        }

        private CurveSegmentInfo segmentAt(int index) {
            return (CurveSegmentInfo) segments.elementAt(index);
        }

        /**
         * ��?�̊J�n�_
         */
        private PointInfo headPoint() {
            CurveSegmentInfo headSeg = (CurveSegmentInfo) segments.elementAt(0);
            return headSeg.headPoint();
        }

        /**
         * ��?��?I���_
         */
        private PointInfo tailPoint() {
            CurveSegmentInfo tailSeg = (CurveSegmentInfo) segments.elementAt(nSegments() - 1);
            return tailSeg.tailPoint();
        }

        /**
         * ��?���Ă��邩�ǂ�����Ԃ�
         *
         * @return ���Ă����true
         */
        private boolean isClosed() {
            if (nPoints() <= 2)
                return false;

            return headPoint().isIdenticalWith(tailPoint());
        }

        /**
         * ��?�S�ł���(���[�����E?�ɂ���)���ǂ�����Ԃ�
         *
         * @return ��?�S�ł����true
         */
        private boolean isComplete() {
            if ((debugFlag & OMIT_BOUNDARY_CHECK) != 0)
                return true;

            /*
            * if both of start & end points are on boundary,
            * return as a complete solution immediately
            */
            if (headPoint().isBoundary() && tailPoint().isBoundary())
                return true;

            return false;
        }

        /**
         * �^����ꂽ��?�Z�O�?���g���n�_���Ɍq���BĂ����?Athis �Ƀ}?[�W����
         *
         * @param mate ����̌�?�Z�O�?���g
         * @return �q���BĂ���� true?A�����łȂ���� false
         */
        private boolean addAtHead(CurveSegmentInfo mate) {
            ParametricalIdentityOfTwoIntersections identity;

            // this   mate
            // -----------
            // Head - Head
            identity = this.headPoint().getParametricalIdentityWith(mate.headPoint());
            if (identity.isIdentical()) {
                mate.reverse();
                segments.insertElementAt(mate, 0);
                mate.setKilled();
                return true;
            }

            // Head - Tail
            identity = this.headPoint().getParametricalIdentityWith(mate.tailPoint());
            if (identity.isIdentical() == true) {
                segments.insertElementAt(mate, 0);
                mate.setKilled();
                return true;
            }

            return false;
        }

        /**
         * �^����ꂽ��?�Z�O�?���g��?I�_���Ɍq���BĂ����?Athis �Ƀ}?[�W����
         *
         * @param mate ����̌�?�Z�O�?���g
         * @return �q���BĂ���� true?A�����łȂ���� false
         */
        private boolean addAtTail(CurveSegmentInfo mate) {
            ParametricalIdentityOfTwoIntersections identity;

            // this   mate
            // -----------
            // Tail - Head
            identity = this.tailPoint().getParametricalIdentityWith(mate.headPoint());
            if (identity.isIdentical() == true) {
                segments.addElement(mate);
                mate.setKilled();
                return true;
            }

            // Tail - Tail
            identity = this.tailPoint().getParametricalIdentityWith(mate.tailPoint());
            if (identity.isIdentical() == true) {
                mate.reverse();
                segments.addElement(mate);
                mate.setKilled();
                return true;
            }

            return false;
        }

        /**
         * �^����ꂽ��?�Z�O�?���g���q���BĂ����?Athis �Ƀ}?[�W����
         *
         * @param mate ����̌�?�Z�O�?���g
         * @return �q���BĂ���� true?A�����łȂ���� false
         */
        private boolean mergeIfConnectWith(CurveSegmentInfo mate) {
            if (addAtHead(mate))
                return true;

            return addAtTail(mate);
        }

        /**
         * IntersectionCurve3D�ɂ��ĕԂ�
         *
         * @return ��?�
         */
        private IntersectionCurve3D getIntersection(boolean doExchange) {
            CurveSegmentInfo segInfo;
            PointInfo pInfo;
            double firstUParamA, firstVParamA, firstUParamB, firstVParamB;
            double lastUParamA, lastVParamA, lastUParamB, lastVParamB;
            lastUParamA = lastVParamA = lastUParamB = lastVParamB = 0.0;
            firstUParamA = firstVParamA = firstUParamB = firstVParamB = 0.0;
            int i, j, k;
            for (i = 0; i < nSegments(); i++) {
                segInfo = segmentAt(i);
                pInfo = segInfo.pointAt(0);
                if (i == 0) {
                    firstUParamA = lastUParamA = pInfo.uParamA;
                    firstVParamA = lastVParamA = pInfo.vParamA;
                    firstUParamB = lastUParamB = pInfo.uParamB;
                    firstVParamB = lastVParamB = pInfo.vParamB;
                }
                for (j = 1; j < segInfo.nPoints(); j++) {
                    pInfo = segInfo.pointAt(j);
                    lastUParamA = adjustParam(uParameterDomainA, pInfo.uParamA, lastUParamA);
                    lastVParamA = adjustParam(vParameterDomainA, pInfo.vParamA, lastVParamA);
                    lastUParamB = adjustParam(uParameterDomainB, pInfo.uParamB, lastUParamB);
                    lastVParamB = adjustParam(vParameterDomainB, pInfo.vParamB, lastVParamB);
                    pInfo.uParamA = lastUParamA;
                    pInfo.vParamA = lastVParamA;
                    pInfo.uParamB = lastUParamB;
                    pInfo.vParamB = lastVParamB;
                }
            }

            int nPnts, nPntsA, nPntsB;
            boolean closed;
            if (headPoint().isIdenticalWith(tailPoint())) {
                nPnts = nPoints() - 1;
                closed = true;
            } else {
                nPnts = nPoints();
                closed = false;
            }

            boolean closedA = checkClosed2D(uParameterDomainA, vParameterDomainA,
                    firstUParamA, firstVParamA,
                    lastUParamA, lastVParamA, closed);
            if (closedA)
                nPntsA = nPoints() - 1;
            else
                nPntsA = nPoints();
            boolean closedB = checkClosed2D(uParameterDomainB, vParameterDomainB,
                    firstUParamB, firstVParamB,
                    lastUParamB, lastVParamB, closed);
            if (closedB)
                nPntsB = nPoints() - 1;
            else
                nPntsB = nPoints();

            Vector v3d = new Vector();
            Vector v2dA = new Vector();
            Vector v2dB = new Vector();
            int n = 0;

            for (i = k = 0; i < nSegments(); i++) {
                segInfo = segmentAt(i);
                pInfo = segInfo.pointAt(0);
                if (i == 0) {
                    v3d.addElement(pInfo.coordinates());
                    v2dA.addElement(new CartesianPoint2D(pInfo.uParamA, pInfo.vParamA));
                    v2dB.addElement(new CartesianPoint2D(pInfo.uParamB, pInfo.vParamB));
                    k++;
                }
                for (j = 1; j < segInfo.nPoints(); j++, k++) {
                    pInfo = segInfo.pointAt(j);
                    Point3D prevP = (Point3D) v3d.elementAt(k - 1 - n);
                    if (!REMOVE_NO_LENGTH_SEGMENT &&
                            prevP.identical(pInfo.coordinates())) {
                        n++;
                        if (k == nPoints() - 1) {
                            /*
                            * case 1: open
                            *  .----------.----------.
                            *  nPoints-3  nPoints-2  nPoints-1
                            *  nPnts-3    nPnts-2    nPnts-1
                            *
                            * case 2: closed
                            *  .----------.----------.(== 0)
                            *  nPoints-3  nPoints-2  nPoints-1
                            *  nPnts-2    nPnts-1    nPnts
                            *
                            * In both case, the last point (== nPoints()-1)
                            * should not be removed.
                            * So remove a point just before the last point.
                            */
                            v3d.removeElementAt(k - n);
                            v2dA.removeElementAt(k - n);
                            v2dB.removeElementAt(k - n);
                        } else {
                            continue;
                        }
                    }

                    if (k != nPnts)
                        v3d.addElement(pInfo.coordinates());
                    if (k != nPntsA)
                        v2dA.addElement(new CartesianPoint2D(pInfo.uParamA, pInfo.vParamA));
                    if (k != nPntsB)
                        v2dB.addElement(new CartesianPoint2D(pInfo.uParamB, pInfo.vParamB));
                }
            }

            Point3D[] pnts3d = new Point3D[nPnts - n];
            v3d.copyInto(pnts3d);
            Point2D[] pnts2dA = new Point2D[nPntsA - n];
            v2dA.copyInto(pnts2dA);
            Point2D[] pnts2dB = new Point2D[nPntsB - n];
            v2dB.copyInto(pnts2dB);

            Polyline3D pol3d = new Polyline3D(pnts3d, closed);
            Polyline2D pol2dA = new Polyline2D(pnts2dA, closedA);
            Polyline2D pol2dB = new Polyline2D(pnts2dB, closedB);
            if (!doExchange)
                return new IntersectionCurve3D(pol3d,
                        surfaceA, pol2dA,
                        surfaceB, pol2dB,
                        PreferredSurfaceCurveRepresentation.CURVE_3D);
            else
                return new IntersectionCurve3D(pol3d,
                        surfaceB, pol2dB,
                        surfaceA, pol2dA,
                        PreferredSurfaceCurveRepresentation.CURVE_3D);
        }

        private void refine() {
            for (Enumeration e = segments.elements(); e.hasMoreElements();) {
                CurveSegmentInfo ci = (CurveSegmentInfo) e.nextElement();
                ci.refine();
            }
        }
    }

    static final int SAMEP_MAX_RETRY = 4;

    /**
     * ���f����Ă����?�Z�O�?���g��?��
     * (�|�����C���Ɍ���)
     */
    class CurveSegmentInfo {
        /**
         * �_��?�(!= �z���?�)
         */
        int nPoints;

        /**
         * ��?�Z�O�?���g(PointInfo�̔z��)
         */
        PointInfo[] points;

        /**
         * �L�򩖳��
         */
        boolean killed = false;

        /**
         * ���]���Ă��邩�ǂ���
         */
        boolean reversed = false;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param pol3  3�����̌�?�
         * @param pol2A �Ȗ� A �̃p���??[�^���?��2�����̌�?�
         * @param pol2B �Ȗ� B �̃p���??[�^���?��2�����̌�?�
         */
        CurveSegmentInfo(Polyline3D pol3,
                         Polyline2D pol2A,
                         Polyline2D pol2B) {
            nPoints = pol3.nSegments() + 1;
            points = new PointInfo[nPoints];
            for (int i = 0; i < nPoints; i++) {
                Point2D pnt = pol2A.pointAt(i);
                double paramAu = pnt.x();
                double paramAv = pnt.y();
                pnt = pol2B.pointAt(i);
                double paramBu = pnt.x();
                double paramBv = pnt.y();
                paramAu = uParameterDomainA.wrap(paramAu);
                paramAv = vParameterDomainA.wrap(paramAv);
                paramBu = uParameterDomainB.wrap(paramBu);
                paramBv = vParameterDomainB.wrap(paramBv);

                points[i] = new PointInfo(pol3.pointAt(i),
                        paramAu, paramAv,
                        paramBu, paramBv);
            }
        }

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param pol3       3�����̌�?�
         * @param pol2A      �Ȗ� A �̃p���??[�^���?��2�����̌�?�
         * @param pol2B      �Ȗ� B �̃p���??[�^���?��2�����̌�?�
         * @param headPTolAu ��?�̊J�n�_�t�߂ł̋Ȗ� A �� U ���̃p���??[�^�̋��e��?�
         * @param headPTolAv ��?�̊J�n�_�t�߂ł̋Ȗ� A �� V ���̃p���??[�^�̋��e��?�
         * @param headPTolBu ��?�̊J�n�_�t�߂ł̋Ȗ� B �� U ���̃p���??[�^�̋��e��?�
         * @param headPTolBv ��?�̊J�n�_�t�߂ł̋Ȗ� B �� V ���̃p���??[�^�̋��e��?�
         * @param tailPTolAu ��?��?I���_�t�߂ł̋Ȗ� A �� U ���̃p���??[�^�̋��e��?�
         * @param tailPTolAv ��?��?I���_�t�߂ł̋Ȗ� A �� V ���̃p���??[�^�̋��e��?�
         * @param tailPTolBu ��?��?I���_�t�߂ł̋Ȗ� B �� U ���̃p���??[�^�̋��e��?�
         * @param tailPTolBv ��?��?I���_�t�߂ł̋Ȗ� B �� V ���̃p���??[�^�̋��e��?�
         */
        CurveSegmentInfo(Polyline3D pol3,
                         Polyline2D pol2A,
                         Polyline2D pol2B,
                         double headPTolAu,
                         double headPTolAv,
                         double headPTolBu,
                         double headPTolBv,
                         double tailPTolAu,
                         double tailPTolAv,
                         double tailPTolBu,
                         double tailPTolBv) {
            nPoints = pol3.nSegments() + 1;
            points = new PointInfo[nPoints];
            for (int i = 0; i < nPoints; i++) {
                Point2D pnt = pol2A.pointAt(i);
                double paramAu = pnt.x();
                double paramAv = pnt.y();
                pnt = pol2B.pointAt(i);
                double paramBu = pnt.x();
                double paramBv = pnt.y();
                paramAu = uParameterDomainA.wrap(paramAu);
                paramAv = vParameterDomainA.wrap(paramAv);
                paramBu = uParameterDomainB.wrap(paramBu);
                paramBv = vParameterDomainB.wrap(paramBv);

                if (i == 0)
                    points[i] = new PointInfo(pol3.pointAt(i),
                            paramAu, paramAv,
                            paramBu, paramBv,
                            headPTolAu, headPTolAv,
                            headPTolBu, headPTolBv);
                else if (i == nPoints - 1)
                    points[i] = new PointInfo(pol3.pointAt(i),
                            paramAu, paramAv,
                            paramBu, paramBv,
                            tailPTolAu, tailPTolAv,
                            tailPTolBu, tailPTolBv);
                else
                    points[i] = new PointInfo(pol3.pointAt(i),
                            paramAu, paramAv,
                            paramBu, paramBv);
            }
        }

        private int nPoints() {
            return nPoints;
        }

        private void nPoints(int newN) {
            nPoints = newN;
        }

        private PointInfo pointAt(int index) {
            if (reversed)
                index = nPoints - index - 1;

            return points[index];
        }

        private void pointAt(int index, PointInfo newPi) {
            points[index] = newPi;
        }

        private boolean isValidIndex(int index) {
            if (index < 0 || index >= nPoints())
                return false;

            return true;
        }

        private boolean isKilled() {
            return killed;
        }

        private void setKilled() {
            killed = true;
        }

        private void reverse() {
            if (reversed)
                reversed = false;
            else
                reversed = true;
        }

        /**
         * �J�n�_
         */
        private PointInfo headPoint() {
            return pointAt(0);
        }

        /**
         * ?I���_
         */
        private PointInfo tailPoint() {
            return pointAt(nPoints() - 1);
        }

        /**
         * �L��Ȓ�����?�BĂ��邩�ǂ������ׂ�
         *
         * @retrun �L��Ȓ�����?�BĂ���Ȃ�true
         */
        private void isValidLengthPolyline() {
            if (isKilled())
                return;

            if (nPoints() > 2)
                return;

            if (!headPoint().isIdenticalWith(tailPoint()))
                return;

            setKilled();
        }

        private boolean isIdenticalWith(int this_i, CurveSegmentInfo mate, int mate_i) {
            if (!isValidIndex(this_i) || !mate.isValidIndex(mate_i))
                return false;

            return pointAt(this_i).isIdenticalWith(mate.pointAt(mate_i));
        }

        /**
         * ��?�Z�O�?���g���m��?d�����Ă��邩�ǂ������ׂ�
         * ?d�����Ă��镔���ɂ���?A����?d���������?�?�����
         * ���S�Ɉ�v���Ă���?�?���?A����?�?�����
         *
         * @param mate ����̌�?�Z�O�?���g
         */
        private void isSamePolyline(CurveSegmentInfo mate) {
            if (this.isKilled() || mate.isKilled())
                return;

            /*
            * deside that which segment is longer
            */
            int longer;
            CurveSegmentInfo l_crv, s_crv;
            if (this.nPoints() >= mate.nPoints()) {
                l_crv = this;
                s_crv = mate;
            } else {
                l_crv = mate;
                s_crv = this;
            }

            /*
            * find first coincidence
            */
            int so_s;
            int l_i;
            int s_i = -1;
            for (l_i = 0; l_i < l_crv.nPoints(); l_i++) {
                if (l_crv.pointAt(l_i).isIdenticalWith(s_crv.headPoint())) {
                    /* start point of short segment is coincide with long segment */
                    s_i = 0;
                    break;
                } else if (l_crv.pointAt(l_i).isIdenticalWith(s_crv.tailPoint())) {
                    /* last point of short segment is coincide with long segment */
                    s_i = s_crv.nPoints() - 1;
                    break;
                }
            }

            if ((so_s = s_i) == -1)
                return;

            /*
            * find next coincidence
            */
            int l_inc, s_inc, rty;

            l_inc = s_inc = 0;
            for (rty = 1; rty < SAMEP_MAX_RETRY; rty++) {
                if (l_crv.isIdenticalWith(l_i + 1, s_crv, s_i + rty) ||
                        l_crv.isIdenticalWith(l_i + rty, s_crv, s_i + 1)) {        // FF
                    l_inc = 1;
                    s_inc = 1;
                    break;
                } else if (l_crv.isIdenticalWith(l_i + 1, s_crv, s_i - rty) ||
                        l_crv.isIdenticalWith(l_i + rty, s_crv, s_i - 1)) {    // FB
                    l_inc = 1;
                    s_inc = (-1);
                    break;
                } else if (l_crv.isIdenticalWith(l_i - 1, s_crv, s_i + rty) ||
                        l_crv.isIdenticalWith(l_i - rty, s_crv, s_i + 1)) {    // BF
                    l_inc = (-1);
                    s_inc = 1;
                    break;
                } else if (l_crv.isIdenticalWith(l_i - 1, s_crv, s_i - rty) ||
                        l_crv.isIdenticalWith(l_i - rty, s_crv, s_i - 1)) {     // BB
                    l_inc = (-1);
                    s_inc = (-1);
                    break;
                }
            }

            if (rty == SAMEP_MAX_RETRY)        // just one coincidence
                return;

            /*
            * find last coincidence
            */
            int so_e = -1;
            while (true) {
                l_i += l_inc;
                s_i += s_inc;

                if (!l_crv.isValidIndex(l_i) || !s_crv.isValidIndex(s_i))
                    break;

                if (!l_crv.isIdenticalWith(l_i, s_crv, s_i)) {
                    for (rty = 1; rty < SAMEP_MAX_RETRY; rty++)
                        if (l_crv.isIdenticalWith(l_i + rty * l_inc, s_crv, s_i)) {
                            l_i += rty * l_inc;
                            break;
                        }
                    if (rty == SAMEP_MAX_RETRY) {
                        for (rty = 1; rty < SAMEP_MAX_RETRY; rty++)
                            if (l_crv.isIdenticalWith(l_i, s_crv, s_i + rty * s_inc)) {
                                s_i += rty * s_inc;
                                break;
                            }
                        if (rty == SAMEP_MAX_RETRY)
                            break;
                    }
                }

                so_e = s_i;
            }
            if (so_e == -1)
                throw new FatalException();

            /*
            * remove overlapped section from shorter segment
            *
            * !!! IMPORTANT !!!
            * currently we believe and pray that obtained overlapped section
            * contains an end point of short segment.
            */
            if (so_s > so_e) {
                int tmp = so_s;
                so_s = so_e;
                so_e = tmp;
            }

            if ((so_e - so_s + 1) == s_crv.nPoints()) {
                /*
                * whole of short segment is in long segment
                */
                s_crv.setKilled();
                return;
            }

            if (so_s == 0) {
                /*
                * front half of short segment is in long segment
                */
                int s_uip = s_crv.nPoints();
                int s_j;
                for (s_i = 0, s_j = so_e; s_j < s_uip; s_i++, s_j++) {
                    s_crv.pointAt(s_i, s_crv.pointAt(s_j));
                }
                s_crv.nPoints(s_i);

            } else if (so_e == (s_crv.nPoints() - 1)) {
                /*
                * rear half of short segment is in long segment
                */
                s_crv.nPoints(so_s + 1);
            }

            if (s_crv.nPoints() <= 1) {
                /*
                * if the remainder is just one point, that segment is useless.
                * so return as (whole of short segment is in longer segment).
                */
                s_crv.setKilled();
                return;
            }

            return;
        }

        private void refine() {
            for (int i = 0; i < nPoints; i++) {
                PointInfo a_point, point, b_point;

                point = points[i];

                if (i == 0)
                    a_point = points[0];
                else
                    a_point = points[i - 1];

                if (i == nPoints - 1)
                    b_point = points[nPoints - 1];
                else
                    b_point = points[i + 1];

                refinePointInfo(point, b_point, a_point, true);
            }
        }
    }

    /*
    * ����Ȍ�?�Z�O�?���g��?�?�����
   private void removeKilledCurves() {
   Vector newList = new Vector();
   for (Enumeration el = listOfCurveSegments.elements(); el.hasMoreElements();) {
       CurveSegmentInfo ci = (CurveSegmentInfo)el.nextElement();
       if (!ci.isKilled())
       newList.addElement(ci);
   }
   listOfCurveSegments = newList;
   }
    */

    /**
     * ��?�Z�O�?���g��ǉB���
     *
     * @param theCurve ��?�
     */
    void addCurve(CurveSegmentInfo theCurve) {
        listOfCurveSegments.addElement(theCurve);
    }

    /**
     * ��?���?�Z�O�?���g��ǉB���
     *
     * @param pol3  3�����̌�?�
     * @param pol2A �Ȗ� A �̃p���??[�^���?��2�����̌�?�
     * @param pol2B �Ȗ� B �̃p���??[�^���?��2�����̌�?�
     */
    void addAsIntersectionCurve(Polyline3D pol3,
                                Polyline2D pol2A,
                                Polyline2D pol2B) {
        addCurve(new CurveSegmentInfo(pol3, pol2A, pol2B));
    }

    /**
     * ��?���?�Z�O�?���g�Ƃ��ĒǉB���
     *
     * @param pol3       3�����̌�?�
     * @param pol2A      �Ȗ� A �̃p���??[�^���?��2�����̌�?�
     * @param pol2B      �Ȗ� B �̃p���??[�^���?��2�����̌�?�
     * @param headPTolAu ��?�̊J�n�_�t�߂ł̋Ȗ� A �� U ���̃p���??[�^�̋��e��?�
     * @param headPTolAv ��?�̊J�n�_�t�߂ł̋Ȗ� A �� V ���̃p���??[�^�̋��e��?�
     * @param headPTolBu ��?�̊J�n�_�t�߂ł̋Ȗ� B �� U ���̃p���??[�^�̋��e��?�
     * @param headPTolBv ��?�̊J�n�_�t�߂ł̋Ȗ� B �� V ���̃p���??[�^�̋��e��?�
     * @param tailPTolAu ��?��?I���_�t�߂ł̋Ȗ� A �� U ���̃p���??[�^�̋��e��?�
     * @param tailPTolAv ��?��?I���_�t�߂ł̋Ȗ� A �� V ���̃p���??[�^�̋��e��?�
     * @param tailPTolBu ��?��?I���_�t�߂ł̋Ȗ� B �� U ���̃p���??[�^�̋��e��?�
     * @param tailPTolBv ��?��?I���_�t�߂ł̋Ȗ� B �� V ���̃p���??[�^�̋��e��?�
     */
    void addAsIntersectionCurve(Polyline3D pol3,
                                Polyline2D pol2A,
                                Polyline2D pol2B,
                                double headPTolAu,
                                double headPTolAv,
                                double headPTolBu,
                                double headPTolBv,
                                double tailPTolAu,
                                double tailPTolAv,
                                double tailPTolBu,
                                double tailPTolBv) {
        addCurve(new CurveSegmentInfo(pol3, pol2A, pol2B,
                headPTolAu, headPTolAv, headPTolBu, headPTolBv,
                tailPTolAu, tailPTolAv, tailPTolBu, tailPTolBv));
    }

    /**
     * ���f����Ă����?�Z�O�?���g��Ȃ�
     */
    void connectIntersectionCurves() {
        if (REMOVE_NO_LENGTH_SEGMENT) {
            /*
            * remove no-length solutions
            */
            for (Enumeration el = listOfCurveSegments.elements(); el.hasMoreElements();) {
                CurveSegmentInfo ci = (CurveSegmentInfo) el.nextElement();
                ci.isValidLengthPolyline();
            }
        }

        /*
        * remove overlapped solutions
        */
        //removeKilledCurves();
        int nCurves = listOfCurveSegments.size();
        CurveSegmentInfo ci1, ci2;
        int i, j;
        for (i = 0; i < nCurves; i++) {
            ci1 = (CurveSegmentInfo) listOfCurveSegments.elementAt(i);

            for (j = i + 1; j < nCurves; j++) {
                ci2 = (CurveSegmentInfo) listOfCurveSegments.elementAt(j);
                ci1.isSamePolyline(ci2);
            }
        }

        /*
        * sort by the number of points
        */
        //removeKilledCurves();
        nCurves = listOfCurveSegments.size();
        for (i = 0; i < nCurves; i++) {
            ci1 = (CurveSegmentInfo) listOfCurveSegments.elementAt(i);
            if (ci1.isKilled())
                continue;

            for (j = i + 1; j < nCurves; j++) {
                ci2 = (CurveSegmentInfo) listOfCurveSegments.elementAt(j);
                if (ci2.isKilled())
                    continue;

                if (ci2.nPoints() < ci1.nPoints()) {
                    listOfCurveSegments.setElementAt(ci2, i);
                    listOfCurveSegments.setElementAt(ci1, j);
                    ci1 = ci2;
                }
            }
        }

        /*
        * connect
        */
        //int nSolution = 0;
        CurveInfo oneCurve;
        //Vector listOfIncompleteCurves = new Vector();
        boolean is_closed;
        boolean found;
        for (i = 0; i < nCurves; i++) {
            ci1 = (CurveSegmentInfo) listOfCurveSegments.elementAt(i);
            if (ci1.isKilled())
                continue;

            oneCurve = new CurveInfo(ci1);

            do {
                found = false;
                for (j = i + 1; !(is_closed = oneCurve.isClosed()) && j < nCurves; j++) {
                    ci2 = (CurveSegmentInfo) listOfCurveSegments.elementAt(j);
                    if (ci2.isKilled())
                        continue;

                    if (oneCurve.mergeIfConnectWith(ci2)) {
                        found = true;
                        break;
                    }
                }
            } while (found);

            /*
            * is oneCurve complete ?
            */
            if (is_closed || oneCurve.isComplete()) {
                listOfCurves.addElement(oneCurve);
                //nSolution++;
            } else {
                //listOfIncompleteCurves.addElement(ci1);
            }
        }
    }

    void refine() {
        for (Enumeration e = listOfCurves.elements(); e.hasMoreElements();) {
            CurveInfo ints = (CurveInfo) e.nextElement();
            ints.refine();
        }
    }

    /**
     * ��_�ƌ�?�̃��X�g�� SurfaceSurfaceInterference3D �̔z��Ƃ��ĕԂ�
     */
    SurfaceSurfaceInterference3D[] toSurfaceSurfaceInterference3DArray(boolean doExchange) {
        int totalSize = listOfPoints.size() + listOfCurves.size();
        int i;

        SurfaceSurfaceInterference3D[] result = new SurfaceSurfaceInterference3D[totalSize];
        i = 0;

        for (Enumeration e = listOfPoints.elements(); e.hasMoreElements();) {
            PointInfo ints = (PointInfo) e.nextElement();
            if (!doExchange)
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint3D(surfaceA, ints.uParamA, ints.vParamA,
                        surfaceB, ints.uParamB, ints.vParamB,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint3D(ints.coord,
                        surfaceA, ints.uParamA, ints.vParamA,
                        surfaceB, ints.uParamB, ints.vParamB,
                        GeometryElement.doCheckDebug);
            else
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint3D(surfaceB, ints.uParamB, ints.vParamB,
                        surfaceA, ints.uParamA, ints.vParamA,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint3D(ints.coord,
                        surfaceB, ints.uParamB, ints.vParamB,
                        surfaceA, ints.uParamA, ints.vParamA,
                        GeometryElement.doCheckDebug);
        }

        for (Enumeration e = listOfCurves.elements(); e.hasMoreElements();) {
            CurveInfo ints = (CurveInfo) e.nextElement();
            result[i++] = ints.getIntersection(doExchange);
        }

        return result;
    }
}

// end of file
