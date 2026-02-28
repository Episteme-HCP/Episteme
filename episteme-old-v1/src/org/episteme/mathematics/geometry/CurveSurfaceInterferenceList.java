/*
 * ��?�ƋȖʂ̊�?̃��X�g�赂��N���X
 * ��������?�ł̓I?[�o?[���b�v�̓T�|?[�g����Ă��Ȃ�
 * �܂�2�����̗v�f�̓T�|?[�g����Ă��Ȃ�
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CurveSurfaceInterferenceList.java,v 1.3 2007-10-21 21:08:10 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.IllegalDimensionException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * ��?�ƋȖʂ̊�?̃��X�g�赂��N���X
 * ��������?�ł̓I?[�o?[���b�v�̓T�|?[�g����Ă��Ȃ�
 * �܂�2�����̗v�f�̓T�|?[�g����Ă��Ȃ�
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:10 $
 */
class CurveSurfaceInterferenceList {
    /**
     * ��?� A
     */
    AbstractParametricCurve curveA;

    /**
     * ��?� A �̒�`��?��
     */
    ParameterDomain parameterDomainA;

    /**
     * �Ȗ� B
     */
    AbstractParametricSurface surfaceB;

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
    Vector listOfIntersections;

    /*
    * �I�u�W�F�N�g��?\�z����
    *
    * @param	curveA	 ��?� A
    * @param	surfaceB �Ȗ� B
    */
    CurveSurfaceInterferenceList(AbstractParametricCurve curveA,
                                 AbstractParametricSurface surfaceB) {
        if ((curveA == null) || (surfaceB == null))
            throw new NullArgumentException();

        if (curveA.dimension() != surfaceB.dimension())
            throw new IllegalDimensionException();

        this.curveA = curveA;
        this.parameterDomainA = curveA.parameterDomain();

        this.surfaceB = surfaceB;
        // gimmick!!
        if (((ParametricSurface3D) surfaceB).type() != ParametricSurface3D.CURVE_BOUNDED_SURFACE_3D) {
            this.uParameterDomainB = surfaceB.uParameterDomain();
            this.vParameterDomainB = surfaceB.vParameterDomain();
        } else {
            ParametricSurface3D basisSurface = ((CurveBoundedSurface3D) surfaceB).basisSurface();
            this.uParameterDomainB = basisSurface.uParameterDomain();
            this.vParameterDomainB = basisSurface.vParameterDomain();
        }

        ConditionOfOperation cond = ConditionOfOperation.getCondition();
        this.dTol = cond.getToleranceForDistanceAsObject();

        this.listOfIntersections = new Vector();
    }

    /**
     * ��?�̂���p���??[�^�l�t�߂ł̃p���??[�^�̋��e��?���?�߂�?B
     * <p/>
     * ToleranceForDistance ����Z?o
     *
     * @param curve ��?�
     * @param param �p���??[�^�l
     */
    private double getToleranceForParameter(AbstractParametricCurve curve, double param) {
        return dTol.toToleranceForParameter((ParametricCurve3D) curve, param).value();
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
    private static final int PARAMETERS_CROSSBOUNDARY_A = 0x2;
    private static final int PARAMETERS_CROSSBOUNDARY_U_B = 0x4;
    private static final int PARAMETERS_CROSSBOUNDARY_V_B = 0x8;

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
         * ���_�̃p���??[�^�l����?� A �̋��E��ׂ���̂Ƃ���
         */
        private void setCrossBoundaryOfA() {
            value |= PARAMETERS_CROSSBOUNDARY_A;
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
         * ���_�̃p���??[�^�l����?� A �̋��E��ׂ����ۂ�
         */
        private boolean isCrossBoundaryOfA() {
            return ((value & PARAMETERS_CROSSBOUNDARY_A) != 0);
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
    class IntersectionInfo {
        /**
         * ��_��?W�l (null ���µ��Ȃ�)
         */
        AbstractPoint coord;

        /**
         * ��_�̋�?� A �ł̃p���??[�^�l
         */
        double paramA;

        /**
         * ��_�̋Ȗ� B �ł�U���p���??[�^�l
         */
        double uParamB;

        /**
         * ��_�̋Ȗ� B �ł�V���p���??[�^�l
         */
        double vParamB;

        /**
         * ��?� A �� paramA �t�߂ł̃p���??[�^�̋��e��?�
         */
        double pTolA;

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
         * @param paramA  ��_�̋�?� A �ł̃p���??[�^�l
         * @param uParamB ��_�̋Ȗ� B �ł�U���p���??[�^�l
         * @param vParamB ��_�̋Ȗ� B �ł�V���p���??[�^�l
         */
        IntersectionInfo(AbstractPoint coord,
                         double paramA,
                         double uParamB,
                         double vParamB) {
            this.coord = coord;    // null ���µ��Ȃ�

            this.paramA = paramA;
            this.uParamB = uParamB;
            this.vParamB = vParamB;

            this.pTolA = getToleranceForParameter(curveA, paramA);
            this.pTolBu = getToleranceForParameterU(surfaceB, uParamB, vParamB);
            this.pTolBv = getToleranceForParameterV(surfaceB, uParamB, vParamB);
        }

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param coord   ��_��?W�l (null ���µ��Ȃ�)
         * @param paramA  ��_�̋�?� A �ł̃p���??[�^�l
         * @param uParamB ��_�̋Ȗ� B �ł�U���p���??[�^�l
         * @param vParamB ��_�̋Ȗ� B �ł�V���p���??[�^�l
         * @param pTolA   ��?� A �� paramA �t�߂ł̃p���??[�^�̋��e��?�
         * @param pTolBu  �Ȗ� B �� [uv]ParamB �t�߂ł�U���p���??[�^�̋��e��?�
         * @param pTolBv  �Ȗ� B �� [uv]ParamB �t�߂ł�V���p���??[�^�̋��e��?�
         */
        IntersectionInfo(AbstractPoint coord,
                         double paramA,
                         double uParamB,
                         double vParamB,
                         double pTolA,
                         double pTolBu,
                         double pTolBv) {
            this.coord = coord;    // null ���µ��Ȃ�

            this.paramA = paramA;
            this.uParamB = uParamB;
            this.vParamB = vParamB;

            this.pTolA = pTolA;
            this.pTolBu = pTolBu;
            this.pTolBv = pTolBv;
        }

        /**
         * ���_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A�ɂ��Ă�?��𓾂�
         *
         * @param mate ��_��?��
         */
        private ParametricalIdentityOfTwoIntersections getParametricalIdentityWith(IntersectionInfo mate) {
            ParametricalIdentityOfTwoIntersections result =
                    new ParametricalIdentityOfTwoIntersections();

            if (this == mate) {
                result.setIdentical();
                return result;
            }

            double diffA = Math.abs(this.paramA - mate.paramA);
            double diffBu = Math.abs(this.uParamB - mate.uParamB);
            double diffBv = Math.abs(this.vParamB - mate.vParamB);

            double pTolA = Math.max(this.pTolA, mate.pTolA);
            double pTolBu = Math.max(this.pTolBu, mate.pTolBu);
            double pTolBv = Math.max(this.pTolBv, mate.pTolBv);

            if ((parameterDomainA.isPeriodic() == true) &&
                    (Math.abs(diffA - parameterDomainA.section().absIncrease()) < pTolA))
                result.setCrossBoundaryOfA();

            if ((uParameterDomainB.isPeriodic() == true) &&
                    (Math.abs(diffBu - uParameterDomainB.section().absIncrease()) < pTolBu))
                result.setCrossBoundaryOfBu();

            if ((vParameterDomainB.isPeriodic() == true) &&
                    (Math.abs(diffBv - vParameterDomainB.section().absIncrease()) < pTolBv))
                result.setCrossBoundaryOfBv();

            if (((result.isCrossBoundaryOfA() == true) || (diffA < pTolA)) &&
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
        private boolean isIdenticalWith(IntersectionInfo mate) {
            if ((this.coord != null) && (mate.coord != null)) {
                if (((Point3D) this.coord).identical((Point3D) mate.coord) != true)
                    return false;
            }

            return this.getParametricalIdentityWith(mate).isIdentical();
        }
    }

    /**
     * ��_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��_�Ɠ���Ƃ݂Ȃ����_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ��_�͒ǉB��Ȃ�
     *
     * @param theIntersection ��_
     */
    void addIntersection(IntersectionInfo theIntersection) {
        for (Enumeration e = listOfIntersections.elements(); e.hasMoreElements();)
            if (theIntersection.isIdenticalWith((IntersectionInfo) e.nextElement()) == true)
                return;

        listOfIntersections.addElement(theIntersection);
    }

    /**
     * ��_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��_�Ɠ���Ƃ݂Ȃ����_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ��_�͒ǉB��Ȃ�
     *
     * @param coord   ��_��?W�l (null ���µ��Ȃ�)
     * @param paramA  ��_�̋�?� A �ł̃p���??[�^�l
     * @param uParamB ��_�̋Ȗ� B �ł�U���̃p���??[�^�l
     * @param vParamB ��_�̋Ȗ� B �ł�V���̃p���??[�^�l
     */
    void addAsIntersection(AbstractPoint coord,
                           double paramA,
                           double uParamB,
                           double vParamB) {
        /*** Debug
         coord.output(System.out);

         if (dimension == 2)
         {
         ((ParametricCurve2D)curveA).coordinates(paramA).output(System.out);
         //((ParametricSurface2D)surfaceB).coordinates(uParamB, vParamB).output(System.out);
         }
         else
         {
         ((ParametricCurve3D)curveA).coordinates(paramA).output(System.out);
         ((ParametricSurface3D)surfaceB).coordinates(uParamB, vParamB).output(System.out);
         }
         ***/

        addIntersection(new IntersectionInfo(coord, paramA, uParamB, vParamB));
    }

    /**
     * ��_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��_�Ɠ���Ƃ݂Ȃ����_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ��_�͒ǉB��Ȃ�
     *
     * @param coord   ��_��?W�l (null ���µ��Ȃ�)
     * @param paramA  ��_�̋�?� A �ł̃p���??[�^�l
     * @param uParamB ��_�̋Ȗ� B �ł�U���̃p���??[�^�l
     * @param vParamB ��_�̋Ȗ� B �ł�V���̃p���??[�^�l
     * @param pTolA   ��?� A �� paramA �t�߂ł̃p���??[�^�̋��e��?�
     * @param pTolBu  �Ȗ� B �� [uv]ParamB �t�߂ł�U���̃p���??[�^�̋��e��?�
     * @param pTolBv  �Ȗ� B �� [uv]ParamB �t�߂ł�V���̃p���??[�^�̋��e��?�
     */
    void addAsIntersection(AbstractPoint coord,
                           double paramA,
                           double uParamB,
                           double vParamB,
                           double pTolA,
                           double pTolBu,
                           double pTolBv) {
        addIntersection(new IntersectionInfo(coord, paramA, uParamB, vParamB,
                pTolA, pTolBu, pTolBv));
    }

    /**
     * ��_��?d���̃��X�g�ⷂׂČ�_�Ƃ��� IntersectionPoint3D �̔z��Ƃ��ĕԂ�
     */
    IntersectionPoint3D[] toIntersectionPoint3DArray(boolean doExchange) {
        int totalSize = listOfIntersections.size();
        int i;

        IntersectionPoint3D[] result = new IntersectionPoint3D[totalSize];
        i = 0;

        for (Enumeration e = listOfIntersections.elements(); e.hasMoreElements();) {
            IntersectionInfo ints = (IntersectionInfo) e.nextElement();
            if (!doExchange)
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint3D((ParametricCurve3D) curveA, ints.paramA,
                        (ParametricSurface3D) surfaceB,
                        ints.uParamB, ints.vParamB,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint3D((Point3D) ints.coord,
                        (ParametricCurve3D) curveA, ints.paramA,
                        (ParametricSurface3D) surfaceB,
                        ints.uParamB, ints.vParamB,
                        GeometryElement.doCheckDebug);
            else
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint3D((ParametricSurface3D) surfaceB,
                        ints.uParamB, ints.vParamB,
                        (ParametricCurve3D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint3D((Point3D) ints.coord,
                        (ParametricSurface3D) surfaceB,
                        ints.uParamB, ints.vParamB,
                        (ParametricCurve3D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug);
        }

        return result;
    }
}

// end of file
