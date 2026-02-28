/*
 * ��?�m�̊�?̃��X�g�赂��N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CurveCurveInterferenceList.java,v 1.3 2007-10-21 21:08:09 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.IllegalDimensionException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * ��?�m�̊�?̃��X�g�赂��N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */
class CurveCurveInterferenceList {
    /**
     * ��?�̑�?݂����Ԃ̎���
     */
    int dimension;

    /**
     * ��?� A
     */
    AbstractParametricCurve curveA;

    /**
     * ��?� A �̒�`��?��
     */
    ParameterDomain parameterDomainA;

    /**
     * ��?� B
     */
    AbstractParametricCurve curveB;

    /**
     * ��?� B �̒�`��?��
     */
    ParameterDomain parameterDomainB;

    /**
     * �����̋��e��?� (�I�u�W�F�N�g��?\�z���ꂽ���_�ł�)
     */
    ToleranceForDistance dTol;

    /**
     * ��_�̃��X�g
     */
    Vector listOfIntersections;

    /**
     * ?d���̃��X�g
     */
    Vector listOfOverlaps;

    /*
    * �I�u�W�F�N�g��?\�z����
    *
    * @param	curveA	��?� A
    * @param	curveB	��?� B
    */
    CurveCurveInterferenceList(AbstractParametricCurve curveA,
                               AbstractParametricCurve curveB) {
        if ((curveA == null) || (curveB == null))
            throw new NullArgumentException();

        if ((this.dimension = curveA.dimension()) != curveB.dimension())
            throw new IllegalDimensionException();

        this.curveA = curveA;
        this.parameterDomainA = curveA.parameterDomain();

        this.curveB = curveB;
        this.parameterDomainB = curveB.parameterDomain();

        ConditionOfOperation cond = ConditionOfOperation.getCondition();
        this.dTol = cond.getToleranceForDistanceAsObject();

        this.listOfIntersections = new Vector();
        this.listOfOverlaps = new Vector();
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
        if (dimension == 2) {
            return dTol.toToleranceForParameter((ParametricCurve2D) curve, param).value();
        } else {
            return dTol.toToleranceForParameter((ParametricCurve3D) curve, param).value();
        }
    }

    /*
    * ���_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A�Ɋւ����?�
    */
    private static final int PARAMETERS_NOT_IDENTICAL = 0x0;
    private static final int PARAMETERS_IDENTICAL = 0x1;
    private static final int PARAMETERS_CROSSBOUNDARY_A = 0x2;
    private static final int PARAMETERS_CROSSBOUNDARY_B = 0x4;

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
         * ���_�̃p���??[�^�l����?� B �̋��E��ׂ���̂Ƃ���
         */
        private void setCrossBoundaryOfB() {
            value |= PARAMETERS_CROSSBOUNDARY_B;
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
         * ���_�̃p���??[�^�l����?� B �̋��E��ׂ����ۂ�
         */
        private boolean isCrossBoundaryOfB() {
            return ((value & PARAMETERS_CROSSBOUNDARY_B) != 0);
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
         * ��_�̋�?� B �ł̃p���??[�^�l
         */
        double paramB;

        /**
         * ��?� A �� paramA �t�߂ł̃p���??[�^�̋��e��?�
         */
        double pTolA;

        /**
         * ��?� B �� paramB �t�߂ł̃p���??[�^�̋��e��?�
         */
        double pTolB;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param coord  ��_��?W�l (null ���µ��Ȃ�)
         * @param paramA ��_�̋�?� A �ł̃p���??[�^�l
         * @param paramB ��_�̋�?� B �ł̃p���??[�^�l
         */
        IntersectionInfo(AbstractPoint coord,
                         double paramA,
                         double paramB) {
            this.coord = coord;    // null ���µ��Ȃ�

            this.paramA = paramA;
            this.paramB = paramB;

            this.pTolA = getToleranceForParameter(curveA, paramA);
            this.pTolB = getToleranceForParameter(curveB, paramB);
        }

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param coord  ��_��?W�l (null ���µ��Ȃ�)
         * @param paramA ��_�̋�?� A �ł̃p���??[�^�l
         * @param paramB ��_�̋�?� B �ł̃p���??[�^�l
         * @param pTolA  ��?� A �� paramA �t�߂ł̃p���??[�^�̋��e��?�
         * @param pTolB  ��?� B �� paramB �t�߂ł̃p���??[�^�̋��e��?�
         */
        IntersectionInfo(AbstractPoint coord,
                         double paramA,
                         double paramB,
                         double pTolA,
                         double pTolB) {
            this.coord = coord;    // null ���µ��Ȃ�

            this.paramA = paramA;
            this.paramB = paramB;

            this.pTolA = pTolA;
            this.pTolB = pTolB;
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
            double diffB = Math.abs(this.paramB - mate.paramB);

            double pTolA = Math.max(this.pTolA, mate.pTolA);
            double pTolB = Math.max(this.pTolB, mate.pTolB);

            if ((parameterDomainA.isPeriodic() == true) &&
                    (Math.abs(diffA - parameterDomainA.section().absIncrease()) < pTolA))
                result.setCrossBoundaryOfA();

            if ((parameterDomainB.isPeriodic() == true) &&
                    (Math.abs(diffB - parameterDomainB.section().absIncrease()) < pTolB))
                result.setCrossBoundaryOfB();

            if (((result.isCrossBoundaryOfA() == true) || (diffA < pTolA)) &&
                    ((result.isCrossBoundaryOfB() == true) || (diffB < pTolB)))
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
                if (dimension == 2) {
                    if (((Point2D) this.coord).identical((Point2D) mate.coord) != true)
                        return false;
                } else {
                    if (((Point3D) this.coord).identical((Point3D) mate.coord) != true)
                        return false;
                }
            }

            return this.getParametricalIdentityWith(mate).isIdentical();
        }

        /**
         * ?d���Ɋ܂܂�Ă��邩�ۂ�
         *
         * @param mate ?d��
         * @return ?d���Ɋ܂܂�� true?A�����łȂ���� false
         */
        boolean isContainedIn(OverlapInfo ovlp) {
            if (this.isIdenticalWith(ovlp.headPoint) == true)
                return true;

            if (this.isIdenticalWith(ovlp.tailPoint) == true)
                return true;

            double thisParam;

            boolean ovlpCrossBoundary;
            double ovlpHead;
            double ovlpTail;

            double ovlpHeadPTol;
            double ovlpTailPTol;

            double ovlpLower;
            double ovlpUpper;

            for (int i = 0; i < 2; i++) {
                if (i == 0) {   // for curve A
                    thisParam = this.paramA;

                    ovlpCrossBoundary = ovlp.crossBoundaryA;
                    ovlpHead = ovlp.headPoint.paramA;
                    ovlpTail = ovlp.tailPoint.paramA;

                    ovlpHeadPTol = ovlp.headPoint.pTolA;
                    ovlpTailPTol = ovlp.tailPoint.pTolA;
                } else {   // for curve B
                    thisParam = this.paramB;

                    ovlpCrossBoundary = ovlp.crossBoundaryB;
                    ovlpHead = ovlp.headPoint.paramB;
                    ovlpTail = ovlp.tailPoint.paramB;

                    ovlpHeadPTol = ovlp.headPoint.pTolB;
                    ovlpTailPTol = ovlp.tailPoint.pTolB;
                }

                if (ovlpHead < ovlpTail) {
                    ovlpLower = ovlpHead - ovlpHeadPTol;
                    ovlpUpper = ovlpTail + ovlpTailPTol;
                } else {
                    ovlpLower = ovlpTail - ovlpTailPTol;
                    ovlpUpper = ovlpHead + ovlpHeadPTol;
                }

                if (ovlpCrossBoundary == true) {
                    double swap = ovlpLower;
                    ovlpLower = ovlpUpper;
                    ovlpUpper = swap;
                }

                if ((thisParam < ovlpLower) && (ovlpUpper < thisParam))
                    return false;
            }

            return true;
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
     * @param coord  ��_��?W�l (null ���µ��Ȃ�)
     * @param paramA ��_�̋�?� A �ł̃p���??[�^�l
     * @param paramB ��_�̋�?� B �ł̃p���??[�^�l
     */
    void addAsIntersection(AbstractPoint coord,
                           double paramA,
                           double paramB) {
        /*** Debug
         coord.output(System.out);

         if (dimension == 2)
         {
         ((ParametricCurve2D)curveA).coordinates(paramA).output(System.out);
         ((ParametricCurve2D)curveB).coordinates(paramB).output(System.out);
         }
         else
         {
         ((ParametricCurve3D)curveA).coordinates(paramA).output(System.out);
         ((ParametricCurve3D)curveB).coordinates(paramB).output(System.out);
         }
         ***/

        addIntersection(new IntersectionInfo(coord, paramA, paramB));
    }

    /**
     * ��_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��_�Ɠ���Ƃ݂Ȃ����_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ��_�͒ǉB��Ȃ�
     *
     * @param coord  ��_��?W�l (null ���µ��Ȃ�)
     * @param paramA ��_�̋�?� A �ł̃p���??[�^�l
     * @param paramB ��_�̋�?� B �ł̃p���??[�^�l
     * @param pTolA  ��?� A �� paramA �t�߂ł̃p���??[�^�̋��e��?�
     * @param pTolB  ��?� B �� paramB �t�߂ł̃p���??[�^�̋��e��?�
     */
    void addAsIntersection(AbstractPoint coord,
                           double paramA,
                           double paramB,
                           double pTolA,
                           double pTolB) {
        addIntersection(new IntersectionInfo(coord, paramA, paramB, pTolA, pTolB));
    }

    /**
     * ?d���Ɋ܂܂�Ă����_��?�?�����
     */
    void removeIntersectionsContainedInOverlap() {
        Vector clonedList = (Vector) (listOfIntersections.clone());
        listOfIntersections.removeAllElements();

        for (Enumeration e1 = clonedList.elements(); e1.hasMoreElements();) {
            IntersectionInfo intersection = (IntersectionInfo) e1.nextElement();
            boolean contained = false;

            for (Enumeration e2 = listOfOverlaps.elements(); e2.hasMoreElements();) {
                if (intersection.isContainedIn((OverlapInfo) e2.nextElement()) == true) {
                    contained = true;
                    break;
                }
            }

            if (contained != true)
                listOfIntersections.addElement(intersection);
        }
    }

    /**
     * ?d����?��
     */
    class OverlapInfo {
        /**
         * ?d���̊J�n�_
         */
        IntersectionInfo headPoint;

        /**
         * ?d����?I���_
         */
        IntersectionInfo tailPoint;

        /**
         * ?d������?� A �̎��̋��E��ׂ��ł��邩�ۂ�?B�ׂ��ł����?^
         */
        private boolean crossBoundaryA;

        /**
         * ?d������?� B �̎��̋��E��ׂ��ł��邩�ۂ�?B�ׂ��ł����?^
         */
        private boolean crossBoundaryB;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param headParamA     ?d���̊J�n�_�̋�?� A �ł̃p���??[�^�l
         * @param headParamB     ?d���̊J�n�_�̋�?� B �ł̃p���??[�^�l
         * @param increaseParamA ?d���̋�?� A �ł̃p���??[�^�?���l
         * @param increaseParamB ?d���̋�?� B �ł̃p���??[�^�?���l
         */
        OverlapInfo(double headParamA,
                    double headParamB,
                    double increaseParamA,
                    double increaseParamB) {
            headParamA = parameterDomainA.wrap(headParamA);
            headParamB = parameterDomainB.wrap(headParamB);
            double tailParamA = parameterDomainA.wrap(headParamA + increaseParamA);
            double tailParamB = parameterDomainB.wrap(headParamB + increaseParamB);

            this.headPoint = new IntersectionInfo(null, headParamA, headParamB);
            this.tailPoint = new IntersectionInfo(null, tailParamA, tailParamB);

            if (((headParamA > tailParamA) && (increaseParamA > 0)) ||
                    ((headParamA < tailParamA) && (increaseParamA < 0)))
                this.crossBoundaryA = true;
            else
                this.crossBoundaryA = false;

            if (((headParamB > tailParamB) && (increaseParamB > 0)) ||
                    ((headParamB < tailParamB) && (increaseParamB < 0)))
                this.crossBoundaryB = true;
            else
                this.crossBoundaryB = false;
        }

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param headParamA     ?d���̊J�n�_�̋�?� A �ł̃p���??[�^�l
         * @param headParamB     ?d���̊J�n�_�̋�?� B �ł̃p���??[�^�l
         * @param increaseParamA ?d���̋�?� A �ł̃p���??[�^�?���l
         * @param increaseParamB ?d���̋�?� B �ł̃p���??[�^�?���l
         * @param headPTolA      ��?� A �̊J�n�_�t�߂ł̃p���??[�^�̋��e��?�
         * @param headPTolB      ��?� B �̊J�n�_�t�߂ł̃p���??[�^�̋��e��?�
         * @param tailPTolA      ��?� A ��?I���_�t�߂ł̃p���??[�^�̋��e��?�
         * @param tailPTolB      ��?� B ��?I���_�t�߂ł̃p���??[�^�̋��e��?�
         */
        OverlapInfo(double headParamA,
                    double headParamB,
                    double increaseParamA,
                    double increaseParamB,
                    double headPTolA,
                    double headPTolB,
                    double tailPTolA,
                    double tailPTolB) {
            headParamA = parameterDomainA.wrap(headParamA);
            headParamB = parameterDomainB.wrap(headParamB);
            double tailParamA = parameterDomainA.wrap(headParamA + increaseParamA);
            double tailParamB = parameterDomainB.wrap(headParamB + increaseParamB);

            this.headPoint = new IntersectionInfo(null, headParamA, headParamB, headPTolA, headPTolB);
            this.tailPoint = new IntersectionInfo(null, tailParamA, tailParamB, tailPTolA, tailPTolB);

            if (((headParamA > tailParamA) && (increaseParamA > 0)) ||
                    ((headParamA < tailParamA) && (increaseParamA < 0)))
                this.crossBoundaryA = true;
            else
                this.crossBoundaryA = false;

            if (((headParamB > tailParamB) && (increaseParamB > 0)) ||
                    ((headParamB < tailParamB) && (increaseParamB < 0)))
                this.crossBoundaryB = true;
            else
                this.crossBoundaryB = false;
        }

        /**
         * crossBoundary[AB] �̒l��?X?V����
         *
         * @param mate     ?d��
         * @param identity �p���??[�^�̓���?�
         */
        private void setCrossBoundaryFlags(OverlapInfo mate,
                                           ParametricalIdentityOfTwoIntersections identity) {
            if ((mate.crossBoundaryA == true) || (identity.isCrossBoundaryOfA() == true))
                this.crossBoundaryA = true;

            if ((mate.crossBoundaryB == true) || (identity.isCrossBoundaryOfB() == true))
                this.crossBoundaryB = true;
        }

        /**
         * ��?� A �ł̃p���??[�^�̑?���l��v�Z���ĕԂ�
         *
         * @return ��?� A �ł̃p���??[�^�̑?���l
         */
        private double computeIncreaseA() {
            double increase = this.tailPoint.paramA - this.headPoint.paramA;

            if ((parameterDomainA.isPeriodic() == true) && (this.crossBoundaryA == true)) {
                if (increase > 0.0)
                    increase -= parameterDomainA.section().absIncrease();
                else
                    increase += parameterDomainA.section().absIncrease();
            }

            return increase;
        }

        /**
         * ��?� B �ł̃p���??[�^�̑?���l��v�Z���ĕԂ�
         *
         * @return ��?� B �ł̃p���??[�^�̑?���l
         */
        private double computeIncreaseB() {
            double increase = this.tailPoint.paramB - this.headPoint.paramB;

            if ((parameterDomainB.isPeriodic() == true) && (this.crossBoundaryB == true)) {
                if (increase > 0.0)
                    increase -= parameterDomainB.section().absIncrease();
                else
                    increase += parameterDomainB.section().absIncrease();
            }

            return increase;
        }

        /**
         * �^����ꂽ?d�����q���BĂ����?Athis �Ƀ}?[�W����
         *
         * @param mate ?d��
         * @return �q���BĂ���� true?A�����łȂ���� false
         */
        boolean mergeIfConnectWith(OverlapInfo mate) {
            if (this == mate)
                return false;

            ParametricalIdentityOfTwoIntersections identity;

            // this   mate
            // -----------
            // Head - Head	: this.increase = this.increase - mate.increase;
            identity = this.headPoint.getParametricalIdentityWith(mate.headPoint);
            if (identity.isIdentical() == true) {
                this.headPoint = mate.tailPoint;
                this.setCrossBoundaryFlags(mate, identity);
                return true;
            }

            // Head - Tail	: this.increase = this.increase + mate.increase;
            identity = this.headPoint.getParametricalIdentityWith(mate.tailPoint);
            if (identity.isIdentical() == true) {
                this.headPoint = mate.headPoint;
                this.setCrossBoundaryFlags(mate, identity);
                return true;
            }

            // Tail - Head	: this.increase = this.increase + mate.increase;
            identity = this.tailPoint.getParametricalIdentityWith(mate.headPoint);
            if (identity.isIdentical() == true) {
                this.tailPoint = mate.tailPoint;
                this.setCrossBoundaryFlags(mate, identity);
                return true;
            }

            // Tail - Tail	: this.increase = this.increase - mate.increase;
            identity = this.tailPoint.getParametricalIdentityWith(mate.tailPoint);
            if (identity.isIdentical() == true) {
                this.tailPoint = mate.headPoint;
                this.setCrossBoundaryFlags(mate, identity);
                return true;
            }

            return false;
        }

        /**
         * ����?d���Ɋ܂܂�Ă��邩�ۂ�
         *
         * @param mate ?d��
         * @return ����?d���Ɋ܂܂�� true?A�����łȂ���� false
         */
        boolean isContainedIn(OverlapInfo mate) {
            if (mate == this)
                return false;

            boolean thisCrossBoundary;
            double thisHead;
            double thisTail;

            boolean mateCrossBoundary;
            double mateHead;
            double mateTail;

            double mateHeadPTol;
            double mateTailPTol;

            double mateLower;
            double mateUpper;

            for (int i = 0; i < 2; i++) {
                if (i == 0) {   // for curve A
                    thisCrossBoundary = this.crossBoundaryA;
                    thisHead = this.headPoint.paramA;
                    thisTail = this.tailPoint.paramA;

                    mateCrossBoundary = mate.crossBoundaryA;
                    mateHead = mate.headPoint.paramA;
                    mateTail = mate.tailPoint.paramA;

                    mateHeadPTol = mate.headPoint.pTolA;
                    mateTailPTol = mate.tailPoint.pTolA;
                } else {   // for curve B
                    thisCrossBoundary = this.crossBoundaryB;
                    thisHead = this.headPoint.paramB;
                    thisTail = this.tailPoint.paramB;

                    mateCrossBoundary = mate.crossBoundaryB;
                    mateHead = mate.headPoint.paramB;
                    mateTail = mate.tailPoint.paramB;

                    mateHeadPTol = mate.headPoint.pTolB;
                    mateTailPTol = mate.tailPoint.pTolB;
                }

                if (mateCrossBoundary == false) {
                    if (thisCrossBoundary == true) {
                        /*
                        * mate:  |---------------|
                        * this: - -|          |- - -
                        */
                        return false;
                    }

                    if (mateHead < mateTail) {
                        mateLower = mateHead - mateHeadPTol;
                        mateUpper = mateTail + mateTailPTol;
                    } else {
                        mateLower = mateTail - mateTailPTol;
                        mateUpper = mateHead + mateHeadPTol;
                    }
                    if ((thisHead < mateLower) || (mateUpper < thisHead) ||
                            (thisTail < mateLower) || (mateUpper < thisTail)) {
                        /*
                        * mate:  |---------------|
                        * this:       |-------------|
                        */
                        return false;
                    }

                } else {
                    if (mateHead < mateTail) {
                        mateLower = mateTail - mateTailPTol;
                        mateUpper = mateHead + mateHeadPTol;
                    } else {
                        mateLower = mateHead - mateHeadPTol;
                        mateUpper = mateTail + mateTailPTol;
                    }
                    if (((mateUpper < thisHead) && (thisHead < mateLower)) ||
                            ((mateUpper < thisTail) && (thisTail < mateLower))) {
                        /*
                        * mate: ----|    |----------
                        * this:       |--------|
                        */
                        return false;
                    }

                    if (thisCrossBoundary == false) {
                        if (((mateUpper < thisHead) || (mateUpper < thisTail)) &&
                                ((thisHead < mateLower) || (thisTail < mateLower))) {
                            /*
                            * mate: ----|    |----------
                            * this:  |----------|
                            */
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    /**
     * ?d����ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ?d�� P ��?ڑ����Ă���Ƃ݂Ȃ���?d�� Q ����ɑ�?݂���Ƃ��ɂ�?A
     * P �� Q ��}?[�W����
     *
     * @param theOverlap ?d��
     */
    void addOverlap(OverlapInfo theOverlap) {
        while (true) {
            OverlapInfo mergedMate = null;

            for (Enumeration e = listOfOverlaps.elements(); e.hasMoreElements();) {
                OverlapInfo mate = (OverlapInfo) e.nextElement();
                if (theOverlap.mergeIfConnectWith(mate) == true) {
                    mergedMate = mate;
                    break;
                }
            }

            if (mergedMate == null)
                break;

            listOfOverlaps.removeElement(mergedMate);
        }

        listOfOverlaps.addElement(theOverlap);
    }

    /**
     * ?d����ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ?d�� P ��?ڑ����Ă���Ƃ݂Ȃ���?d�� Q ����ɑ�?݂���Ƃ��ɂ�?A
     * P �� Q ��}?[�W����
     *
     * @param headParamA     ?d���̊J�n�_�̋�?� A �ł̃p���??[�^�l
     * @param headParamB     ?d���̊J�n�_�̋�?� B �ł̃p���??[�^�l
     * @param increaseParamA ?d���̋�?� A �ł̃p���??[�^�?���l
     * @param increaseParamB ?d���̋�?� B �ł̃p���??[�^�?���l
     */
    void addAsOverlap(double headParamA,
                      double headParamB,
                      double increaseParamA,
                      double increaseParamB) {
        addOverlap(new OverlapInfo(headParamA, headParamB, increaseParamA, increaseParamB));
    }

    /**
     * ?d����ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ?d�� P ��?ڑ����Ă���Ƃ݂Ȃ���?d�� Q ����ɑ�?݂���Ƃ��ɂ�?A
     * P �� Q ��}?[�W����
     *
     * @param headParamA     ?d���̊J�n�_�̋�?� A �ł̃p���??[�^�l
     * @param headParamB     ?d���̊J�n�_�̋�?� B �ł̃p���??[�^�l
     * @param increaseParamA ?d���̋�?� A �ł̃p���??[�^�?���l
     * @param increaseParamB ?d���̋�?� B �ł̃p���??[�^�?���l
     * @param headPTolA      ��?� A �̊J�n�_�t�߂ł̃p���??[�^�̋��e��?�
     * @param headPTolB      ��?� B �̊J�n�_�t�߂ł̃p���??[�^�̋��e��?�
     * @param tailPTolA      ��?� A ��?I���_�t�߂ł̃p���??[�^�̋��e��?�
     * @param tailPTolB      ��?� B ��?I���_�t�߂ł̃p���??[�^�̋��e��?�
     */
    void addAsOverlap(double headParamA,
                      double headParamB,
                      double increaseParamA,
                      double increaseParamB,
                      double headPTolA,
                      double headPTolB,
                      double tailPTolA,
                      double tailPTolB) {
        addOverlap(new OverlapInfo(headParamA, headParamB, increaseParamA, increaseParamB,
                headPTolA, headPTolB, tailPTolA, tailPTolB));
    }

    /**
     * ����?d���Ɋ܂܂�Ă���?d����?�?�����
     */
    void removeOverlapsContainedInOtherOverlap() {
        Vector clonedList = (Vector) (listOfOverlaps.clone());
        listOfOverlaps.removeAllElements();

        for (Enumeration e1 = clonedList.elements(); e1.hasMoreElements();) {
            OverlapInfo overlap = (OverlapInfo) e1.nextElement();
            boolean contained = false;

            for (Enumeration e2 = clonedList.elements(); e2.hasMoreElements();) {
                if (overlap.isContainedIn((OverlapInfo) e2.nextElement()) == true) {
                    contained = true;
                    break;
                }
            }

            if (contained != true)
                listOfOverlaps.addElement(overlap);
        }
    }

    /**
     * ��_��?d���̃��X�g�� CurveCurveInterference2D �̔z��Ƃ��ĕԂ�
     */
    CurveCurveInterference2D[] toCurveCurveInterference2DArray(boolean doExchange) {
        if (dimension != 2)
            throw new IllegalDimensionException();

        int totalSize = listOfIntersections.size() + listOfOverlaps.size();
        int i;

        CurveCurveInterference2D[] result = new CurveCurveInterference2D[totalSize];
        i = 0;

        for (Enumeration e = listOfIntersections.elements(); e.hasMoreElements();) {
            IntersectionInfo ints = (IntersectionInfo) e.nextElement();
            if (!doExchange) {
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint2D((ParametricCurve2D) curveA, ints.paramA,
                        (ParametricCurve2D) curveB, ints.paramB,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint2D((Point2D) ints.coord,
                        (ParametricCurve2D) curveA, ints.paramA,
                        (ParametricCurve2D) curveB, ints.paramB,
                        GeometryElement.doCheckDebug);
            } else {
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint2D((ParametricCurve2D) curveB, ints.paramB,
                        (ParametricCurve2D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint2D((Point2D) ints.coord,
                        (ParametricCurve2D) curveB, ints.paramB,
                        (ParametricCurve2D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug);
            }
        }

        for (Enumeration e = listOfOverlaps.elements(); e.hasMoreElements();) {
            OverlapInfo ovlp = (OverlapInfo) e.nextElement();
            if (!doExchange) {
                result[i++] = new OverlapCurve2D((ParametricCurve2D) curveA,
                        ovlp.headPoint.paramA, ovlp.computeIncreaseA(),
                        (ParametricCurve2D) curveB,
                        ovlp.headPoint.paramB, ovlp.computeIncreaseB(),
                        false);
            } else {
                result[i++] = new OverlapCurve2D((ParametricCurve2D) curveB,
                        ovlp.headPoint.paramB, ovlp.computeIncreaseB(),
                        (ParametricCurve2D) curveA,
                        ovlp.headPoint.paramA, ovlp.computeIncreaseA(),
                        false);
            }
        }

        return result;
    }

    /**
     * ��_��?d���̃��X�g�ⷂׂČ�_�Ƃ��� IntersectionPoint2D �̔z��Ƃ��ĕԂ�
     */
    IntersectionPoint2D[] toIntersectionPoint2DArray(boolean doExchange) {
        if (dimension != 2)
            throw new IllegalDimensionException();

        int totalSize = listOfIntersections.size() + (listOfOverlaps.size() * 2);
        int i;

        IntersectionPoint2D[] result = new IntersectionPoint2D[totalSize];
        i = 0;

        for (Enumeration e = listOfIntersections.elements(); e.hasMoreElements();) {
            IntersectionInfo ints = (IntersectionInfo) e.nextElement();
            if (!doExchange)
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint2D((ParametricCurve2D) curveA, ints.paramA,
                        (ParametricCurve2D) curveB, ints.paramB,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint2D((Point2D) ints.coord,
                        (ParametricCurve2D) curveA, ints.paramA,
                        (ParametricCurve2D) curveB, ints.paramB,
                        GeometryElement.doCheckDebug);
            else
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint2D((ParametricCurve2D) curveB, ints.paramB,
                        (ParametricCurve2D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint2D((Point2D) ints.coord,
                        (ParametricCurve2D) curveB, ints.paramB,
                        (ParametricCurve2D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug);
        }

        for (Enumeration e = listOfOverlaps.elements(); e.hasMoreElements();) {
            OverlapInfo ovlp = (OverlapInfo) e.nextElement();
            if (!doExchange) {
                result[i++] = new IntersectionPoint2D((ParametricCurve2D) curveA,
                        ovlp.headPoint.paramA,
                        (ParametricCurve2D) curveB,
                        ovlp.headPoint.paramB,
                        GeometryElement.doCheckDebug);
                result[i++] = new IntersectionPoint2D((ParametricCurve2D) curveA,
                        ovlp.tailPoint.paramA,
                        (ParametricCurve2D) curveB,
                        ovlp.tailPoint.paramB,
                        GeometryElement.doCheckDebug);
            } else {
                result[i++] = new IntersectionPoint2D((ParametricCurve2D) curveB,
                        ovlp.headPoint.paramB,
                        (ParametricCurve2D) curveA,
                        ovlp.headPoint.paramA,
                        GeometryElement.doCheckDebug);
                result[i++] = new IntersectionPoint2D((ParametricCurve2D) curveB,
                        ovlp.tailPoint.paramB,
                        (ParametricCurve2D) curveA,
                        ovlp.tailPoint.paramA,
                        GeometryElement.doCheckDebug);
            }
        }

        return result;
    }

    /**
     * ��_��?d���̃��X�g�� CurveCurveInterference3D �̔z��Ƃ��ĕԂ�
     */
    CurveCurveInterference3D[] toCurveCurveInterference3DArray(boolean doExchange) {
        if (dimension != 3)
            throw new IllegalDimensionException();

        int totalSize = listOfIntersections.size() + listOfOverlaps.size();
        int i;

        CurveCurveInterference3D[] result = new CurveCurveInterference3D[totalSize];
        i = 0;

        for (Enumeration e = listOfIntersections.elements(); e.hasMoreElements();) {
            IntersectionInfo ints = (IntersectionInfo) e.nextElement();
            if (!doExchange) {
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint3D((ParametricCurve3D) curveA, ints.paramA,
                        (ParametricCurve3D) curveB, ints.paramB,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint3D((Point3D) ints.coord,
                        (ParametricCurve3D) curveA, ints.paramA,
                        (ParametricCurve3D) curveB, ints.paramB,
                        GeometryElement.doCheckDebug);
            } else {
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint3D((ParametricCurve3D) curveB, ints.paramB,
                        (ParametricCurve3D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint3D((Point3D) ints.coord,
                        (ParametricCurve3D) curveB, ints.paramB,
                        (ParametricCurve3D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug);
            }
        }

        for (Enumeration e = listOfOverlaps.elements(); e.hasMoreElements();) {
            OverlapInfo ovlp = (OverlapInfo) e.nextElement();
            if (!doExchange) {
                result[i++] = new OverlapCurve3D((ParametricCurve3D) curveA,
                        ovlp.headPoint.paramA, ovlp.computeIncreaseA(),
                        (ParametricCurve3D) curveB,
                        ovlp.headPoint.paramB, ovlp.computeIncreaseB(),
                        false);
            } else {
                result[i++] = new OverlapCurve3D((ParametricCurve3D) curveB,
                        ovlp.headPoint.paramB, ovlp.computeIncreaseB(),
                        (ParametricCurve3D) curveA,
                        ovlp.headPoint.paramA, ovlp.computeIncreaseA(),
                        false);
            }
        }

        return result;
    }

    /**
     * ��_��?d���̃��X�g�ⷂׂČ�_�Ƃ��� IntersectionPoint3D �̔z��Ƃ��ĕԂ�
     */
    IntersectionPoint3D[] toIntersectionPoint3DArray(boolean doExchange) {
        if (dimension != 3)
            throw new IllegalDimensionException();

        int totalSize = listOfIntersections.size() + (listOfOverlaps.size() * 2);
        int i;

        IntersectionPoint3D[] result = new IntersectionPoint3D[totalSize];
        i = 0;

        for (Enumeration e = listOfIntersections.elements(); e.hasMoreElements();) {
            IntersectionInfo ints = (IntersectionInfo) e.nextElement();
            if (!doExchange)
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint3D((ParametricCurve3D) curveA, ints.paramA,
                        (ParametricCurve3D) curveB, ints.paramB,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint3D((Point3D) ints.coord,
                        (ParametricCurve3D) curveA, ints.paramA,
                        (ParametricCurve3D) curveB, ints.paramB,
                        GeometryElement.doCheckDebug);
            else
                result[i++] = (ints.coord == null)
                        ? new IntersectionPoint3D((ParametricCurve3D) curveB, ints.paramB,
                        (ParametricCurve3D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug)
                        : new IntersectionPoint3D((Point3D) ints.coord,
                        (ParametricCurve3D) curveB, ints.paramB,
                        (ParametricCurve3D) curveA, ints.paramA,
                        GeometryElement.doCheckDebug);
        }

        for (Enumeration e = listOfOverlaps.elements(); e.hasMoreElements();) {
            OverlapInfo ovlp = (OverlapInfo) e.nextElement();
            if (!doExchange) {
                result[i++] = new IntersectionPoint3D((ParametricCurve3D) curveA,
                        ovlp.headPoint.paramA,
                        (ParametricCurve3D) curveB,
                        ovlp.headPoint.paramB,
                        GeometryElement.doCheckDebug);
                result[i++] = new IntersectionPoint3D((ParametricCurve3D) curveA,
                        ovlp.tailPoint.paramA,
                        (ParametricCurve3D) curveB,
                        ovlp.tailPoint.paramB,
                        GeometryElement.doCheckDebug);
            } else {
                result[i++] = new IntersectionPoint3D((ParametricCurve3D) curveB,
                        ovlp.headPoint.paramB,
                        (ParametricCurve3D) curveA,
                        ovlp.headPoint.paramA,
                        GeometryElement.doCheckDebug);
                result[i++] = new IntersectionPoint3D((ParametricCurve3D) curveB,
                        ovlp.tailPoint.paramB,
                        (ParametricCurve3D) curveA,
                        ovlp.tailPoint.paramA,
                        GeometryElement.doCheckDebug);
            }
        }

        return result;
    }

    /**
     * CurveCurveInterference2D �̔z�񂩂��_����?o��
     *
     * @param array CurveCurveInterference2D �̔z��
     */
    static Vector extractIntersections(CurveCurveInterference2D[] array) {
        Vector result = new Vector();

        for (int i = 0; i < array.length; i++)
            if (array[i].isIntersectionPoint() == true)
                result.addElement(array[i]);

        return result;
    }

    /**
     * CurveCurveInterference2D �̔z�񂩂�?d������?o��
     *
     * @param array CurveCurveInterference2D �̔z��
     */
    static Vector extractOverlaps(CurveCurveInterference2D[] array) {
        Vector result = new Vector();

        for (int i = 0; i < array.length; i++)
            if (array[i].isOverlapCurve() == true)
                result.addElement(array[i]);

        return result;
    }

    /**
     * CurveCurveInterference3D �̔z�񂩂��_����?o��
     *
     * @param array CurveCurveInterference3D �̔z��
     */
    static Vector extractIntersections(CurveCurveInterference3D[] array) {
        Vector result = new Vector();

        for (int i = 0; i < array.length; i++)
            if (array[i].isIntersectionPoint() == true)
                result.addElement(array[i]);

        return result;
    }

    /**
     * CurveCurveInterference3D �̔z�񂩂�?d������?o��
     *
     * @param array CurveCurveInterference3D �̔z��
     */
    static Vector extractOverlaps(CurveCurveInterference3D[] array) {
        Vector result = new Vector();

        for (int i = 0; i < array.length; i++)
            if (array[i].isOverlapCurve() == true)
                result.addElement(array[i]);

        return result;
    }
}

// end of file
