/*
 * ��?�?�̓_�Ƌ�Ԃ̃��X�g�赂��N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ParameterRangeOnCurveList.java,v 1.3 2007-10-21 21:08:16 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.IllegalDimensionException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * ��?�?�̓_�Ƌ�Ԃ̃��X�g�赂��N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:16 $
 */
class ParameterRangeOnCurveList {
    /**
     * ��?�̑�?݂����Ԃ̎���
     */
    int dimension;

    /**
     * ��?�
     */
    AbstractParametricCurve curve;

    /**
     * ��?�̒�`��?��
     */
    ParameterDomain parameterDomain;

    /**
     * �����̋��e��?� (�I�u�W�F�N�g��?\�z���ꂽ���_�ł�)
     */
    ToleranceForDistance dTol;

    /**
     * �_�̃��X�g
     */
    Vector listOfPoints;

    /**
     * ��Ԃ̃��X�g
     */
    Vector listOfSections;

    /*
    * �I�u�W�F�N�g��?\�z����
    *
    * @param	curve	��?�
    */
    ParameterRangeOnCurveList(AbstractParametricCurve curve) {
        if (curve == null)
            throw new NullArgumentException();

        this.dimension = curve.dimension();
        this.curve = curve;
        this.parameterDomain = curve.parameterDomain();

        ConditionOfOperation cond = ConditionOfOperation.getCondition();
        this.dTol = cond.getToleranceForDistanceAsObject();

        this.listOfPoints = new Vector();
        this.listOfSections = new Vector();
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
    * ��_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A�Ɋւ����?�
    */
    private static final int PARAMETERS_NOT_IDENTICAL = 0x0;
    private static final int PARAMETERS_IDENTICAL = 0x1;
    private static final int PARAMETERS_CROSSBOUNDARY = 0x2;

    /**
     * ��_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A��\��
     */
    class ParametricalIdentityOfTwoPoints {
        /**
         * ��_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A��\��?�?�
         */
        private int value;

        /**
         * �I�u�W�F�N�g��?\�z����
         */
        ParametricalIdentityOfTwoPoints() {
            setNonIdentical();
        }

        /**
         * ��_�̃p���??[�^�l������Ƃ݂Ȃ��Ȃ���̂Ƃ���
         */
        private void setNonIdentical() {
            value = PARAMETERS_NOT_IDENTICAL;
        }

        /**
         * ��_�̃p���??[�^�l������Ƃ݂Ȃ����̂Ƃ���
         */
        private void setIdentical() {
            value |= PARAMETERS_IDENTICAL;
        }

        /**
         * ��_�̃p���??[�^�l����?�̋��E��ׂ���̂Ƃ���
         */
        private void setCrossBoundary() {
            value |= PARAMETERS_CROSSBOUNDARY;
        }

        /**
         * ��_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�
         */
        private boolean isIdentical() {
            return ((value & PARAMETERS_IDENTICAL) != 0);
        }

        /**
         * ��_�̃p���??[�^�l����?�̋��E��ׂ����ۂ�
         */
        private boolean isCrossBoundary() {
            return ((value & PARAMETERS_CROSSBOUNDARY) != 0);
        }
    }

    /**
     * �_��?��
     */
    class PointInfo {
        /**
         * �_��?W�l (null ���µ��Ȃ�)
         */
        AbstractPoint coord;

        /**
         * �_�̋�?�ł̃p���??[�^�l
         */
        double param;

        /**
         * ��?�� param �t�߂ł̃p���??[�^�̋��e��?�
         */
        double pTol;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param coord �_��?W�l (null ���µ��Ȃ�)
         * @param param �_�̋�?�ł̃p���??[�^�l
         */
        PointInfo(AbstractPoint coord,
                  double param) {
            this.coord = coord;    // null ���µ��Ȃ�
            this.param = param;

            this.pTol = getToleranceForParameter(curve, param);
        }

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param coord �_��?W�l (null ���µ��Ȃ�)
         * @param param �_�̋�?�ł̃p���??[�^�l
         * @param pTol  ��?�� param �t�߂ł̃p���??[�^�̋��e��?�
         */
        PointInfo(AbstractPoint coord,
                  double param,
                  double pTol) {
            this.coord = coord;    // null ���µ��Ȃ�
            this.param = param;
            this.pTol = pTol;
        }

        /**
         * ��_�̃p���??[�^�l������Ƃ݂Ȃ��邩�ۂ�?A�ɂ��Ă�?��𓾂�
         *
         * @param mate �_��?��
         */
        private ParametricalIdentityOfTwoPoints getParametricalIdentityWith(PointInfo mate) {
            ParametricalIdentityOfTwoPoints result =
                    new ParametricalIdentityOfTwoPoints();

            if (this == mate) {
                result.setIdentical();
                return result;
            }

            double diff = Math.abs(this.param - mate.param);
            double pTol = Math.max(this.pTol, mate.pTol);

            if ((parameterDomain.isPeriodic() == true) &&
                    (Math.abs(diff - parameterDomain.section().absIncrease()) < pTol))
                result.setCrossBoundary();

            if ((result.isCrossBoundary() == true) || (diff < pTol))
                result.setIdentical();

            return result;
        }

        /**
         * ��_������Ƃ݂Ȃ��邩�ۂ�
         *
         * @param mate �_��?��
         */
        private boolean isIdenticalWith(PointInfo mate) {
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
         * ��ԂɊ܂܂�Ă��邩�ۂ�
         *
         * @param mate ���
         * @return ��ԂɊ܂܂�� true?A�����łȂ���� false
         */
        boolean isContainedIn(SectionInfo ovlp) {
            if (this.isIdenticalWith(ovlp.headPoint) == true)
                return true;

            if (this.isIdenticalWith(ovlp.tailPoint) == true)
                return true;

            double ovlpLower;
            double ovlpUpper;

            if (ovlp.headPoint.param < ovlp.tailPoint.param) {
                ovlpLower = ovlp.headPoint.param - ovlp.headPoint.pTol;
                ovlpUpper = ovlp.tailPoint.param + ovlp.tailPoint.pTol;
            } else {
                ovlpLower = ovlp.tailPoint.param - ovlp.tailPoint.pTol;
                ovlpUpper = ovlp.headPoint.param + ovlp.headPoint.pTol;
            }

            if (ovlp.crossBoundary == true) {
                double swap = ovlpLower;
                ovlpLower = ovlpUpper;
                ovlpUpper = swap;
            }

            if ((this.param < ovlpLower) && (ovlpUpper < this.param))
                return false;

            return true;
        }
    }

    /**
     * �_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ�_�Ɠ���Ƃ݂Ȃ���_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ�_�͒ǉB��Ȃ�
     *
     * @param thePoint �_
     */
    void addPoint(PointInfo thePoint) {
        for (Enumeration e = listOfPoints.elements(); e.hasMoreElements();)
            if (thePoint.isIdenticalWith((PointInfo) e.nextElement()) == true)
                return;

        listOfPoints.addElement(thePoint);
    }

    /**
     * �_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ�_�Ɠ���Ƃ݂Ȃ���_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ�_�͒ǉB��Ȃ�
     *
     * @param coord �_��?W�l (null ���µ��Ȃ�)
     * @param param �_�̋�?�ł̃p���??[�^�l
     */
    void addAsPoint(AbstractPoint coord,
                    double param) {
        /*** Debug
         coord.output(System.out);

         if (dimension == 2)
         {
         ((ParametricCurve2D)curve).coordinates(param).output(System.out);
         }
         else
         {
         ((ParametricCurve3D)curve).coordinates(param).output(System.out);
         }
         ***/

        addPoint(new PointInfo(coord, param));
    }

    /**
     * �_��ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ�_�Ɠ���Ƃ݂Ȃ���_����ɑ�?݂���Ƃ��ɂ�?A
     * ��?��ɗ^����ꂽ�_�͒ǉB��Ȃ�
     *
     * @param coord �_��?W�l (null ���µ��Ȃ�)
     * @param param �_�̋�?�ł̃p���??[�^�l
     * @param pTol  ��?�� param �t�߂ł̃p���??[�^�̋��e��?�
     */
    void addAsPoint(AbstractPoint coord,
                    double param,
                    double pTol) {
        addPoint(new PointInfo(coord, param, pTol));
    }

    /**
     * ��ԂɊ܂܂�Ă���_��?�?�����
     */
    void removePointsContainedInSection() {
        Vector clonedList = (Vector) (listOfPoints.clone());
        listOfPoints.removeAllElements();

        for (Enumeration e1 = clonedList.elements(); e1.hasMoreElements();) {
            PointInfo point = (PointInfo) e1.nextElement();
            boolean contained = false;

            for (Enumeration e2 = listOfSections.elements(); e2.hasMoreElements();) {
                if (point.isContainedIn((SectionInfo) e2.nextElement()) == true) {
                    contained = true;
                    break;
                }
            }

            if (contained != true)
                listOfPoints.addElement(point);
        }
    }

    /**
     * ��Ԃ�?��
     */
    class SectionInfo {
        /**
         * ��Ԃ̊J�n�_
         */
        PointInfo headPoint;

        /**
         * ��Ԃ�?I���_
         */
        PointInfo tailPoint;

        /**
         * ��Ԃ���?�̎��̋��E��ׂ��ł��邩�ۂ�?B�ׂ��ł����?^
         */
        private boolean crossBoundary;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param headParam     ��Ԃ̊J�n�_�̋�?�ł̃p���??[�^�l
         * @param increaseParam ��Ԃ̋�?�ł̃p���??[�^�?���l
         */
        SectionInfo(double headParam,
                    double increaseParam) {
            headParam = parameterDomain.wrap(headParam);
            double tailParam = parameterDomain.wrap(headParam + increaseParam);

            this.headPoint = new PointInfo(null, headParam);
            this.tailPoint = new PointInfo(null, tailParam);

            if (((headParam > tailParam) && (increaseParam > 0)) ||
                    ((headParam < tailParam) && (increaseParam < 0)))
                this.crossBoundary = true;
            else
                this.crossBoundary = false;
        }

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param headParam     ��Ԃ̊J�n�_�̋�?�ł̃p���??[�^�l
         * @param increaseParam ��Ԃ̋�?�ł̃p���??[�^�?���l
         * @param headPTol      ��?�̊J�n�_�t�߂ł̃p���??[�^�̋��e��?�
         * @param tailPTol      ��?��?I���_�t�߂ł̃p���??[�^�̋��e��?�
         */
        SectionInfo(double headParam,
                    double increaseParam,
                    double headPTol,
                    double tailPTol) {
            headParam = parameterDomain.wrap(headParam);
            double tailParam = parameterDomain.wrap(headParam + increaseParam);

            this.headPoint = new PointInfo(null, headParam, headPTol);
            this.tailPoint = new PointInfo(null, tailParam, tailPTol);

            if (((headParam > tailParam) && (increaseParam > 0)) ||
                    ((headParam < tailParam) && (increaseParam < 0)))
                this.crossBoundary = true;
            else
                this.crossBoundary = false;
        }

        /**
         * crossBoundary �̒l��?X?V����
         *
         * @param mate     ���
         * @param identity �p���??[�^�̓���?�
         */
        private void setCrossBoundaryFlags(SectionInfo mate,
                                           ParametricalIdentityOfTwoPoints identity) {
            if ((mate.crossBoundary == true) || (identity.isCrossBoundary() == true))
                this.crossBoundary = true;
        }

        /*
         * �p���??[�^�̑?���l��v�Z���ĕԂ�
        *
        * @return	�p���??[�^�̑?���l
        */
        private double computeIncrease() {
            double increase = this.tailPoint.param - this.headPoint.param;

            if ((parameterDomain.isPeriodic() == true) && (this.crossBoundary == true)) {
                if (increase > 0.0)
                    increase -= parameterDomain.section().absIncrease();
                else
                    increase += parameterDomain.section().absIncrease();
            }

            return increase;
        }

        /**
         * �^����ꂽ��Ԃ��q���BĂ����?Athis �Ƀ}?[�W����
         *
         * @param mate ���
         * @return �q���BĂ���� true?A�����łȂ���� false
         */
        boolean mergeIfConnectWith(SectionInfo mate) {
            if (this == mate)
                return false;

            ParametricalIdentityOfTwoPoints identity;

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
         * ���̋�ԂɊ܂܂�Ă��邩�ۂ�
         *
         * @param mate ���
         * @return ���̋�ԂɊ܂܂�� true?A�����łȂ���� false
         */
        boolean isContainedIn(SectionInfo mate) {
            if (mate == this)
                return false;

            double mateLower;
            double mateUpper;

            if (mate.crossBoundary == false) {
                if (this.crossBoundary == true) {
                    /*
                    * mate:  |---------------|
                    * this: - -|          |- - -
                    */
                    return false;
                }

                if (mate.headPoint.param < mate.tailPoint.param) {
                    mateLower = mate.headPoint.param - mate.headPoint.pTol;
                    mateUpper = mate.tailPoint.param + mate.tailPoint.pTol;
                } else {
                    mateLower = mate.tailPoint.param - mate.tailPoint.pTol;
                    mateUpper = mate.headPoint.param + mate.headPoint.pTol;
                }
                if ((this.headPoint.param < mateLower) || (mateUpper < this.headPoint.param) ||
                        (this.tailPoint.param < mateLower) || (mateUpper < this.tailPoint.param)) {
                    /*
                    * mate:  |---------------|
                    * this:       |-------------|
                    */
                    return false;
                }
            } else {
                if (mate.headPoint.param < mate.tailPoint.param) {
                    mateLower = mate.tailPoint.param - mate.tailPoint.pTol;
                    mateUpper = mate.headPoint.param + mate.headPoint.pTol;
                } else {
                    mateLower = mate.headPoint.param - mate.headPoint.pTol;
                    mateUpper = mate.tailPoint.param + mate.tailPoint.pTol;
                }
                if (((mateUpper < this.headPoint.param) && (this.headPoint.param < mateLower)) ||
                        ((mateUpper < this.tailPoint.param) && (this.tailPoint.param < mateLower))) {
                    /*
                    * mate: ----|    |----------
                    * this:       |--------|
                    */
                    return false;
                }

                if (this.crossBoundary == false) {
                    if (((mateUpper < this.headPoint.param) || (mateUpper < this.tailPoint.param)) &&
                            ((this.headPoint.param < mateLower) || (this.tailPoint.param < mateLower))) {
                        /*
                        * mate: ----|    |----------
                        * this:  |----------|
                        */
                        return false;
                    }
                }
            }

            return true;
        }
    }

    /**
     * ��Ԃ�ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��� P ��?ڑ����Ă���Ƃ݂Ȃ����� Q ����ɑ�?݂���Ƃ��ɂ�?A
     * P �� Q ��}?[�W����
     *
     * @param theSection ���
     */
    void addSection(SectionInfo theSection) {
        while (true) {
            SectionInfo mergedMate = null;

            for (Enumeration e = listOfSections.elements(); e.hasMoreElements();) {
                SectionInfo mate = (SectionInfo) e.nextElement();
                if (theSection.mergeIfConnectWith(mate) == true) {
                    mergedMate = mate;
                    break;
                }
            }

            if (mergedMate == null)
                break;

            listOfSections.removeElement(mergedMate);
        }

        listOfSections.addElement(theSection);
    }

    /**
     * ��Ԃ�ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��� P ��?ڑ����Ă���Ƃ݂Ȃ����� Q ����ɑ�?݂���Ƃ��ɂ�?A
     * P �� Q ��}?[�W����
     *
     * @param headParam     ��Ԃ̊J�n�_�̋�?�ł̃p���??[�^�l
     * @param increaseParam ��Ԃ̋�?�ł̃p���??[�^�?���l
     */
    void addAsSection(double headParam,
                      double increaseParam) {
        addSection(new SectionInfo(headParam, increaseParam));
    }

    /**
     * ��Ԃ�ǉB���
     * <p/>
     * ��?��ɗ^����ꂽ��� P ��?ڑ����Ă���Ƃ݂Ȃ����� Q ����ɑ�?݂���Ƃ��ɂ�?A
     * P �� Q ��}?[�W����
     *
     * @param headParam     ��Ԃ̊J�n�_�̋�?�ł̃p���??[�^�l
     * @param increaseParam ��Ԃ̋�?�ł̃p���??[�^�?���l
     * @param headPTol      ��?�̊J�n�_�t�߂ł̃p���??[�^�̋��e��?�
     * @param tailPTol      ��?��?I���_�t�߂ł̃p���??[�^�̋��e��?�
     */
    void addAsSection(double headParam,
                      double increaseParam,
                      double headPTol,
                      double tailPTol) {
        addSection(new SectionInfo(headParam, increaseParam, headPTol, tailPTol));
    }

    /**
     * ���̋�ԂɊ܂܂�Ă����Ԃ�?�?�����
     */
    void removeSectionsContainedInOtherSection() {
        Vector clonedList = (Vector) (listOfSections.clone());
        listOfSections.removeAllElements();

        for (Enumeration e1 = clonedList.elements(); e1.hasMoreElements();) {
            SectionInfo section = (SectionInfo) e1.nextElement();
            boolean contained = false;

            for (Enumeration e2 = clonedList.elements(); e2.hasMoreElements();) {
                if (section.isContainedIn((SectionInfo) e2.nextElement()) == true) {
                    contained = true;
                    break;
                }
            }

            if (contained != true)
                listOfSections.addElement(section);
        }
    }

    /**
     * �_�Ƌ�Ԃ̃��X�g�� ParameterRangeOnCurve2D �̔z��Ƃ��ĕԂ�
     */
    ParameterRangeOnCurve2D[] toParameterRangeOnCurve2DArray() {
        if (dimension != 2)
            throw new IllegalDimensionException();

        int totalSize = listOfPoints.size() + listOfSections.size();
        int i;

        ParameterRangeOnCurve2D[] result = new ParameterRangeOnCurve2D[totalSize];
        i = 0;

        for (Enumeration e = listOfPoints.elements(); e.hasMoreElements();) {
            PointInfo point = (PointInfo) e.nextElement();
            result[i++] = (point.coord == null)
                    ? new PointOnCurve2D((ParametricCurve2D) curve, point.param,
                    GeometryElement.doCheckDebug)
                    : new PointOnCurve2D((Point2D) point.coord,
                    (ParametricCurve2D) curve, point.param,
                    GeometryElement.doCheckDebug);
        }

        for (Enumeration e = listOfSections.elements(); e.hasMoreElements();) {
            SectionInfo ovlp = (SectionInfo) e.nextElement();
            result[i++] = new ParameterSectionOnCurve2D((ParametricCurve2D) curve,
                    ovlp.headPoint.param, ovlp.computeIncrease(),
                    false);
        }

        return result;
    }

    /**
     * �_�Ƌ�Ԃ̃��X�g�ⷂׂē_�Ƃ��� PointOnCurve2D �̔z��Ƃ��ĕԂ�
     */
    PointOnCurve2D[] toPointOnCurve2DArray() {
        if (dimension != 2)
            throw new IllegalDimensionException();

        int totalSize = listOfPoints.size() + (listOfSections.size() * 2);
        int i;

        PointOnCurve2D[] result = new PointOnCurve2D[totalSize];
        i = 0;

        for (Enumeration e = listOfPoints.elements(); e.hasMoreElements();) {
            PointInfo point = (PointInfo) e.nextElement();
            result[i++] = (point.coord == null)
                    ? new PointOnCurve2D((ParametricCurve2D) curve, point.param,
                    GeometryElement.doCheckDebug)
                    : new PointOnCurve2D((Point2D) point.coord,
                    (ParametricCurve2D) curve, point.param,
                    GeometryElement.doCheckDebug);
        }

        for (Enumeration e = listOfSections.elements(); e.hasMoreElements();) {
            SectionInfo ovlp = (SectionInfo) e.nextElement();
            result[i++] = new PointOnCurve2D((ParametricCurve2D) curve,
                    ovlp.headPoint.param,
                    GeometryElement.doCheckDebug);
            result[i++] = new PointOnCurve2D((ParametricCurve2D) curve,
                    ovlp.tailPoint.param,
                    GeometryElement.doCheckDebug);
        }

        return result;
    }

    /**
     * �_�Ƌ�Ԃ̃��X�g�� ParameterRangeOnCurve3D �̔z��Ƃ��ĕԂ�
     */
    ParameterRangeOnCurve3D[] toParameterRangeOnCurve3DArray() {
        if (dimension != 3)
            throw new IllegalDimensionException();

        int totalSize = listOfPoints.size() + listOfSections.size();
        int i;

        ParameterRangeOnCurve3D[] result = new ParameterRangeOnCurve3D[totalSize];
        i = 0;

        for (Enumeration e = listOfPoints.elements(); e.hasMoreElements();) {
            PointInfo point = (PointInfo) e.nextElement();
            result[i++] = (point.coord == null)
                    ? new PointOnCurve3D((ParametricCurve3D) curve, point.param,
                    GeometryElement.doCheckDebug)
                    : new PointOnCurve3D((Point3D) point.coord,
                    (ParametricCurve3D) curve, point.param,
                    GeometryElement.doCheckDebug);
        }

        for (Enumeration e = listOfSections.elements(); e.hasMoreElements();) {
            SectionInfo ovlp = (SectionInfo) e.nextElement();
            result[i++] = new ParameterSectionOnCurve3D((ParametricCurve3D) curve,
                    ovlp.headPoint.param, ovlp.computeIncrease(),
                    false);
        }

        return result;
    }

    /**
     * �_�Ƌ�Ԃ̃��X�g�ⷂׂē_�Ƃ��� PointOnCurve3D �̔z��Ƃ��ĕԂ�
     */
    PointOnCurve3D[] toPointOnCurve3DArray() {
        if (dimension != 3)
            throw new IllegalDimensionException();

        int totalSize = listOfPoints.size() + (listOfSections.size() * 2);
        int i;

        PointOnCurve3D[] result = new PointOnCurve3D[totalSize];
        i = 0;

        for (Enumeration e = listOfPoints.elements(); e.hasMoreElements();) {
            PointInfo point = (PointInfo) e.nextElement();
            result[i++] = (point.coord == null)
                    ? new PointOnCurve3D((ParametricCurve3D) curve, point.param,
                    GeometryElement.doCheckDebug)
                    : new PointOnCurve3D((Point3D) point.coord,
                    (ParametricCurve3D) curve, point.param,
                    GeometryElement.doCheckDebug);
        }

        for (Enumeration e = listOfSections.elements(); e.hasMoreElements();) {
            SectionInfo ovlp = (SectionInfo) e.nextElement();
            result[i++] = new PointOnCurve3D((ParametricCurve3D) curve,
                    ovlp.headPoint.param,
                    GeometryElement.doCheckDebug);
            result[i++] = new PointOnCurve3D((ParametricCurve3D) curve,
                    ovlp.tailPoint.param,
                    GeometryElement.doCheckDebug);
        }

        return result;
    }

    /**
     * ParameterRangeOnCurve2D �̔z�񂩂�_����?o��
     *
     * @param array ParameterRangeOnCurve2D �̔z��
     */
    static Vector extractPoints(ParameterRangeOnCurve2D[] array) {
        Vector result = new Vector();

        for (int i = 0; i < array.length; i++)
            if (array[i].isPoint() == true)
                result.addElement(array[i]);

        return result;
    }

    /**
     * ParameterRangeOnCurve2D �̔z�񂩂��Ԃ���?o��
     *
     * @param array ParameterRangeOnCurve2D �̔z��
     */
    static Vector extractSections(ParameterRangeOnCurve2D[] array) {
        Vector result = new Vector();

        for (int i = 0; i < array.length; i++)
            if (array[i].isSection() == true)
                result.addElement(array[i]);

        return result;
    }

    /**
     * ParameterRangeOnCurve3D �̔z�񂩂�_����?o��
     *
     * @param array ParameterRangeOnCurve3D �̔z��
     */
    static Vector extractPoints(ParameterRangeOnCurve3D[] array) {
        Vector result = new Vector();

        for (int i = 0; i < array.length; i++)
            if (array[i].isPoint() == true)
                result.addElement(array[i]);

        return result;
    }

    /**
     * ParameterRangeOnCurve3D �̔z�񂩂��Ԃ���?o��
     *
     * @param array ParameterRangeOnCurve3D �̔z��
     */
    static Vector extractSections(ParameterRangeOnCurve3D[] array) {
        Vector result = new Vector();

        for (int i = 0; i < array.length; i++)
            if (array[i].isSection() == true)
                result.addElement(array[i]);

        return result;
    }
}

// end of file
