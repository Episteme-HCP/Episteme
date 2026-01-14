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

import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

/**
 * �R����?F�~���Ɖ~??�̌�_��?�߂�N���X
 * <p/>
 * �~���Ɖ~??�̌����?A�Q�̈ʒu�֌W�ɂ�BĈȉ���?�?������ⵂ�
 * ���ꂼ��?�߂�?B
 * 1. �Uʂ�?�?��ɂ��ĂR�ʂ�
 * 2. �~??�̒��_���~��?�ɂ���?�?��Ƃ��ĂT�ʂ�
 * 3. �~??�̒��_���~����ɂ���?�?��Ƃ��ĂQ�ʂ�
 * 4. �~??�̒��_���~���O�ɂ���?�?��Ƃ��ĂR�ʂ�
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:24 $
 */

class IntsCylCon3D {

    /**
     * �~���Ɖ~??�̊֌W��\�����邽�߂̒�?�
     */
    /**
     * �Uʂ�?��1
     */
    private static final int SPECIAL_CASE_1 = 0;

    /**
     * �Uʂ�?��2
     */
    private static final int SPECIAL_CASE_2 = 1;

    /**
     * �Uʂ�?��3
     */
    private static final int SPECIAL_CASE_3 = 2;

    /**
     * �~??�̒��_���~��?�ɂ���
     */
    private static final int CONE_VERTEX_ON_CYLINDER = 3;
    private static final int CONE_ENTIRE_OUTSIDE_CYLINDER = 30;
    private static final int CONE_RULE_ON_CYLINDER = 31;
    private static final int THETA_EQUAL_SEMI_ANGLE = 32;
    private static final int THETA_NOT_EQUAL_SEMI_ANGLE = 33;
    private static final int CONE_ANGLE_INSIDE_CYLINDER = 34;

    /**
     * �~??�̒��_���~����ɂ���
     */
    private static final int CONE_VERTEX_IN_CYLINDER = 4;
    private static final int BOTH_AXIS_IS_EQUAL_SEMI_ANGLE = 40;
    private static final int BOTH_AXIS_IS_NOT_EQUAL_SEMI_ANGLE = 41;

    /**
     * �~??�̒��_���~���O�ɂ���
     */
    private static final int CONE_VERTEX_OUT_CYLINDER = 5;
    private static final int RULING_IS_PARALLEL_TO_AXIS = 50;
    private static final int ANGLE_IS_SMALLER_CONS_SEMI_ANGLE = 51;
    private static final int ANGLE_IS_GREATER_CONS_SEMI_ANGLE = 52;

    /**
     * �~��
     */
    private CylindricalSurface3D cyl;

    /**
     * �~���̌��_
     */
    private Point3D cylOrg;

    /**
     * �~���̎�
     */
    private Line3D cylAxis;

    /**
     * �~??
     */
    private ConicalSurface3D con;

    /**
     * �~??�̌��_
     */
    private Point3D conOrg;

    /**
     * �~??�̎�
     */
    private Line3D conAxis;

    /**
     * ��?��\������|�����C���̓_��?�
     */
    private static final int nst = 41;

    /**
     * ���ʂ�귂��邩�ǂ����̃t���O
     */
    private boolean doExchange;

    /**
     * �����̋��e��?�
     */
    private double dTol;

    /**
     * �p�x�̋��e��?�
     */
    private double aTol;

    /**
     * �~??�̒��_�Ɖ~���̎��̋���
     */
    private double dist;

    /**
     * �~??�Ɖ~���̎���?����p�x
     */
    private double theta;

    /**
     * �~??�̎��Ɖ~??�̒��_����~���̎��ւ�?�?�?����p�x
     */
    private double beta;

    /**
     * �~??�̒��_����~���̎��ւ�?�?�̃x�N�g��
     */
    private Vector3D con2cyl;

    /**
     * �Uʂ�?�?�3�̂Ƃ��Ɏg���_
     */
    private Point3D specialCase3Point;

    private double conCos;
    private double conTan;

    /**
     * �~���̋�?�?W��
     */
    private Vector3D[] cylAxes;

    /**
     * �~??�̋�?�?W��
     */
    private Vector3D[] conAxes;

    /**
     * �~���Ɖ~??�ƌ��ʂ�귂��邩�ǂ����̃t���O��^����
     * �����?�߂邽�߂̃I�u�W�F�N�g��?\�z����?B
     *
     * @param cyl        �~��
     * @param con        �~??
     * @param doExchange ���ʂ�귂��邩�ǂ����̃t���O
     */
    private IntsCylCon3D(CylindricalSurface3D cyl,
                         ConicalSurface3D con,
                         boolean doExchange) {
        // �e?���l��?ݒ肷��
        this.cyl = cyl;
        this.con = con;
        this.doExchange = doExchange;

        // �~���Ɋւ���t�B?[���h�l?ݒ�
        cylOrg = cyl.position().location();
        cylAxis = cyl.getAxis();
        cylAxes = cyl.position().axes();

        // �~??�Ɋւ���t�B?[���h�l?ݒ�
        conCos = Math.cos(con.semiAngle());
        conTan = Math.tan(con.semiAngle());
        conOrg = con.position().location();
        conAxis = con.getAxis();
        conAxes = con.position().axes();
        conOrg = conOrg.subtract(conAxes[2].multiply(con.radius() / conTan));

        // ��?��ȂǂɊւ���t�B?[���h�l?ݒ�
        dTol = cyl.getToleranceForDistance();
        aTol = cyl.getToleranceForAngle();
        PointOnCurve3D[] projectedPoint = cylAxis.projectFrom(conOrg);
        dist = conOrg.distance(projectedPoint[0]);
        con2cyl = projectedPoint[0].subtract(conOrg);

        // �~��?A�~??�̎����m�̊֌W�Ȃǂ�?�
        double ework = cylAxes[2].dotProduct(conAxes[2]);
        if (ework > 1.0) ework = 1.0;
        if (ework < -1.0) ework = -1.0;
        theta = Math.acos(ework);

        if (dist > dTol) {
            ework = con2cyl.dotProduct(conAxes[2]) / dist;
            if (ework > 1.0) ework = 1.0;
            if (ework < -1.0) ework = -1.0;
            beta = Math.acos(ework);
        }
    }

    /**
     * �Uʂ�?�?�1 ���ǂ������ׂ�
     * <p/>
     * �����m����v����?A�~??�̒��_����?�ɂ���?B?� 1�~
     * </p>
     *
     * @return �Uʂ�?�?�1 ���ǂ�����?^�U�l
     */
    private boolean isSpecialCase1() {
        return (((theta < dTol) || (theta > (Math.PI - dTol))) && (dist < dTol));
    }

    /**
     * �Uʂ�?�?�2 ���ǂ������ׂ�
     * <p/>
     * �~??�̗�?�~���̕\�ʂɉ����悤�Ɉ�v����?B?� 1�ȉ~��1?� or 1?�
     * �Ѥ�ƊO����?ڂ���Ƃ���2�p�^?[��
     * </p>
     *
     * @return �Uʂ�?�?�2 ���ǂ���?^�U�l
     */
    private boolean isSpecialCase2() {
        double semiAngle = con.semiAngle();
        return (((Math.abs(theta - semiAngle) < aTol)
                || (Math.abs(theta - (Math.PI - semiAngle)) < aTol))
                && ((Math.abs(beta - (Math.PI / 2.0 - semiAngle)) < aTol)
                || (Math.abs(beta - (Math.PI / 2.0 + semiAngle)) < aTol))
                && (Math.abs(dist - cyl.radius()) < dTol));
    }

    /**
     * �Uʂ�?�?�3 ���ǂ������ׂ�
     * <p/>
     * �~�����~??�̓Ք�ł��̗�?��?ڂ���?B?� 2�ȉ~
     * </p>
     *
     * @return �Uʂ�?�?�3 ���ǂ���
     */
    private boolean isSpecialCase3() {
        //
        // �~??�̌��_��ʂBĉ~���̋�?�?W����?���ʂ�?l��?A
        // ���̕��ʂƉ~??�̌�?��?�߂�?B
        //
        Axis2Placement3D position =
                new Axis2Placement3D(conOrg, cylAxes[2], cylAxes[0]);
        Plane3D plane = new Plane3D(position);
        SurfaceSurfaceInterference3D[] intf;
        try {
            intf = plane.intersect(con);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        //
        // ��?�(�ł���~??�̗�?�ł �钼?�)��2�{��?݂���Ȃ�?A
        // ���ꂼ���?�Ɖ~���̒�?S���̋�����
        // �~���̔��a�ƌ�?��ȓ�ň�v���邩�ǂ�����?�����?B
        //
        if (intf.length != 2)
            return false;

        for (int i = 0; i < 2; i++) {
            try {
                double ework = ((Line3D) intf[i].toIntersectionCurve().
                        curve3d()).distanceFrom(cylAxis);
                if (Math.abs(ework - cyl.radius()) > dTol)
                    return false;
            } catch (IndefiniteSolutionException e) {
                return false;
            }
        }

        //
        // �~??�̗�?�Ɖ~���̌����?l��?A���肪1�̓_�ɂȂ�̂ł����?A
        // �~���Ɖ~??�̊֌W�͓Uʂ�?�?�1�Ƃ݂Ȃ�?B
        //
        IntersectionPoint3D[] intp;
        try {
            intp = ((Line3D) intf[0]).intersect(cyl);
        } catch (IndefiniteSolutionException e) {
            // ������?����~??��2�̗�?��?A
            // �~���̎��ƕ�?s�ɂȂ�Ȃ��̂�?A���̃P?[�X�͗L�蓾�Ȃ�
            throw new FatalException();
        }
        if (intp.length != 1)
            throw new FatalException();

        specialCase3Point = intp[0];
        return true;
    }

    /**
     * �~??�̒��_���~��?�ɂ��邩�ǂ������ׂ�
     *
     * @return �~??�̒��_���~��?�ɂ��邩�ǂ�����?^�U�l?B
     */
    private boolean isConesVertexOnCylinder() {
        return Math.abs(dist - cyl.radius()) < dTol;
    }

    /**
     * �~??�̒��_���~����ɂ��邩�ǂ������ׂ�
     *
     * @return �~??�̒��_���~����ɂ��邩�ǂ�����?^�U�l?B
     */
    private boolean isConesVertexInCylinder() {
        return dist < cyl.radius();
    }

    /**
     * ��?�Ƃ��Ẳ~��?��?B(special case 1 �̂Ƃ�)
     * <p/>
     * �~���̔��a?A�~??�̔��a?A�~??�̊J����?�����
     * ��?�Ƃ��Ăǂ̈ʒu�ɉ~���ł��邩��v�Z����?B
     * </p>
     *
     * @return ��?�(1�~)
     */
    private GeometryElement[] oneCircle() {
        double radius = cyl.radius();
        double d = radius / conTan;
        Vector3D axis = conAxes[2];
        Point3D org = conOrg.add(axis.multiply(d));
        Axis2Placement3D position = new
                Axis2Placement3D(org, axis, cylAxes[0]);
        Circle3D circle = new Circle3D(position, radius);
        GeometryElement[] sol = {circle};
        return sol;
    }

    /**
     * ��?�Ƃ��Ă�1�ȉ~��1?��?��?B(special case 2 �̂Ƃ�)
     * <p/>
     * �~�����~??��ɂ���?�?���?A�܂���?�ł���ȉ~��?��?A
     * ���̌�?A�~��?A�~???�̒�?��?�߂�?B
     * </p>
     *
     * @return ��?�(1�ȉ~��1?�)
     */
    private GeometryElement[] oneEllipseOneLine() {
        //
        // �~���Ɖ~??�̓�O�����?A�ȉ~��?�߂�?B
        //
        GeometryElement[] ellipse = null;
        if (Math.abs(beta - (Math.PI / 2.0 - con.semiAngle())) < aTol) {
            ellipse = oneEllipse();
            if (ellipse.length != 1)
                throw new FatalException();
        }
        //
        // ��?�͉~??�̌��_��ʂ�~���̎��̌��?��̂�?�߂�΂悢?B
        //
        Line3D line = new Line3D(conOrg, cyl.position().z());

        //
        // ����ꂽ���܂Ƃ߂�
        //
        GeometryElement[] sol;
        if (ellipse != null) {
            sol = new GeometryElement[2];
            sol[0] = line;
            sol[1] = ellipse[0];
        } else {
            sol = new GeometryElement[1];
            sol[0] = line;
        }
        return sol;
    }

    /**
     * ��?�Ƃ��Ă�1�ȉ~��?��?B(special case 2 �̂Ƃ�)
     * <p/>
     * �~??�̗�?��4���?o��?A���̒�?�Ɖ~���̌�_��?�߂�
     * 4�̂���?��Ȃ��Ƃ�3�͌�_��������͂��Ȃ̂�?A����3�̌�_�ŕ��ʂ�?��?B
     * ���ʂƉ~���̌�?��Ƃ߂�ȉ~�ł���?B
     * </p>
     *
     * @return ��?�(1�ȉ~)
     */
    private GeometryElement[] oneEllipse() {
        Vector3D conU, conV, conW;

        conW = conAxes[2];
        conV = cylAxes[2].crossProduct(conW);
        conU = conV.crossProduct(conW);
        conU = conU.unitized();
        conV = conV.unitized();

        Point3D[] planePoint = new Point3D[3];
        //
        // 4�̗�?��?��?A�~���Ƃ̌�_��?Œ�3�͋?�߂�
        //
        for (int i = 0, j = 0; i < 4; i++, j++) {
            double t = (Math.PI / 2) * i;
            Vector3D dir = cnPointFromT(conU, conV, conW, t);
            Line3D conRuling = new Line3D(conOrg, dir);
            Vector3D evec = dir.unitized();
            // �~���̎��Ɖ~??�̎���?����p�x��0�x��?�?��̓X�L�b�v����
            if (Math.abs(evec.dotProduct(cylAxes[2])) > Math.cos(aTol))
                continue;
            IntersectionPoint3D[] intp;
            try {
                intp = conRuling.intersect(cyl);
            } catch (IndefiniteSolutionException e) {
                // �~���̎��Ɖ~??�̎���?����p�x��0�x��?�?���?A
                // ?��?Ȃ���Ă�̂�?A���̃P?[�X�͋N���蓾�Ȃ�
                throw new FatalException();
            }
            double ework = conOrg.distance(intp[0]);
            if (ework > dTol)
                planePoint[j] = intp[0];
            else
                planePoint[j] = intp[1];
        }

        //
        // ?�ŋ?�߂�ꂽ�_��g�Bĕ��ʂ�?��?A
        // ���ʂƉ~���̌����?�߂�?B
        //
        Point3D planeOrg = planePoint[1];
        Vector3D planeRef1 = planePoint[0].subtract(planePoint[1]);
        Vector3D planeRef2 = planePoint[1].subtract(planePoint[1]);
        Vector3D planeAxis = planeRef1.crossProduct(planeRef2);
        ;

        Axis2Placement3D position =
                new Axis2Placement3D(planeOrg, planeAxis, planeRef1);
        Plane3D plane = new Plane3D(position);
        SurfaceSurfaceInterference3D[] intf;
        try {
            intf = plane.intersect(cyl);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        if ((intf.length != 0) || (!intf[0].isIntersectionCurve()))
            throw new FatalException();
        ParametricCurve3D curve = intf[0].toIntersectionCurve().curve3d();
        GeometryElement[] sol = {curve};
        return sol;
    }

    /**
     * ��?�Ƃ��Ă�2�ȉ~��?��?B(special case 3 �̂Ƃ�)
     * <p/>
     * �~??�̗�?��2���?o��?A���̒�?�Ɖ~���̌�_��?�߂�
     * ��?�Ɖ~���ƌ�_��?A���炩���ߋ?�߂Ă���_��3�̌�_�ŕ��ʂ�?��?B
     * ���ʂƉ~���̌�?��Ƃ߂�ȉ~�ł���?B
     * </p>
     *
     * @return ��?�(2�ȉ~)
     */
    private GeometryElement[] twoEllipse() {
        Vector3D conU, conV, conW;

        conW = conAxes[2];
        conV = cylAxes[2].crossProduct(conW);
        conU = conV.crossProduct(conW);
        conU = conU.unitized();
        conV = conV.unitized();

        //
        // �~??�̗�?��?��?A����Ɖ~���Ƃ̌�_��?�߂�?B
        // 180�x���Α��ӯ�l�ɂ��ċ?�߂�?B
        //
        Point3D xRadiusPoint[][] = new Point3D[2][2];
        for (int i = 0; i < 2; i++) {
            double t = Math.PI * i;
            Vector3D dir = cnPointFromT(conU, conV, conW, t);
            Line3D conRuling = new Line3D(conOrg, dir);
            IntersectionPoint3D[] intp;
            try {
                intp = conRuling.intersect(cyl);
            } catch (IndefiniteSolutionException e) {
                // �Uʂ�?�?�3�̎���?A�~??�̗�?�Ɖ~����?ڂ��邱�Ƃ�?A
                // �N���蓾�Ȃ�
                throw new FatalException();
            }
            double dist0 = conOrg.distance(intp[0]);
            double dist1 = conOrg.distance(intp[1]);

            if (dist0 < dist1) {
                if (i == 0) {
                    xRadiusPoint[0][0] = intp[0];
                    xRadiusPoint[1][0] = intp[1];
                } else {
                    xRadiusPoint[0][1] = intp[1];
                    xRadiusPoint[1][1] = intp[0];
                }
            } else {
                if (i == 0) {
                    xRadiusPoint[0][0] = intp[1];
                    xRadiusPoint[1][0] = intp[0];
                } else {
                    xRadiusPoint[0][1] = intp[0];
                    xRadiusPoint[1][1] = intp[1];
                }
            }
        }
        //
        // ���ꂼ��2�̓_�̑g�Ƃ��炩���ߋ?�߂Ă������_��3��g�B�
        // ���ʂ�?��?A����Ɖ~���̌����?�߂�
        //
        SurfaceSurfaceInterference3D[] intf;
        GeometryElement[] sol = new GeometryElement[2];
        for (int i = 0; i < 2; i++) {
            Point3D planeOrg = specialCase3Point;
            Vector3D planeRef1 = xRadiusPoint[i][0].subtract(specialCase3Point);
            Vector3D planeRef2 = xRadiusPoint[i][1].subtract(specialCase3Point);
            Vector3D planeAxis = planeRef1.crossProduct(planeRef2);
            Axis2Placement3D position =
                    new Axis2Placement3D(planeOrg, planeAxis, planeRef1);
            Plane3D plane = new Plane3D(position);
            try {
                intf = plane.intersect(cyl);
            } catch (IndefiniteSolutionException e) {
                throw new FatalException();
            }
            if ((intf.length != 1) || (intf[0].isIntersectionCurve()))
                throw new FatalException();
            ParametricCurve3D curve = intf[0].toIntersectionCurve().curve3d();
            sol[i] = curve;
        }
        return sol;
    }

    /**
     * �~??�̒��_���~��?�ɂ���Ƃ�?A���̊֌W��?�?�?󋵂𒲂ׂ�
     * <p/>
     * �~??�̒��_���~��?�ɂ���Ƃ�?A�����?ׂ���5��?�?��ɕ�������?B
     * ����𔻕ʂ��Ă�?o�����ɒ�?��l�Ƃ��ĕԋp����?B
     * </p>
     *
     * @return �~??�̒��_���~��?�ɂ���Ƃ�?A���̊֌W��?�?�?�
     */
    private int getRelationInConeVertexOnCylinder() {
        double semiAngle = con.semiAngle();
        double HARF_PI = Math.PI / 2.0;
        if ((Math.abs(beta - (HARF_PI + semiAngle)) < aTol)
                || (beta > (HARF_PI + semiAngle)))
            // 1 point
            return CONE_ENTIRE_OUTSIDE_CYLINDER;
        else if (Math.abs(beta - (HARF_PI - semiAngle)) < aTol)
            // 1 curve
            return CONE_RULE_ON_CYLINDER;
        else if ((beta > (HARF_PI - semiAngle))
                && (beta < (HARF_PI + semiAngle)))
            if ((Math.abs(theta - semiAngle) < aTol)
                    || (Math.abs(theta - (Math.PI - semiAngle)) < aTol))
                // 1 line & 1 curve
                return THETA_EQUAL_SEMI_ANGLE;
            else
                // 1 leaf of an 8 figure curve
                return THETA_NOT_EQUAL_SEMI_ANGLE;
        else
            // 1 curve & 1 point
            return CONE_ANGLE_INSIDE_CYLINDER;
    }

    /**
     * �~??�̒��_���~��?�ɂ���Ƃ��̌�?��Ԃ�
     * <p/>
     * ����ꂽ��?��l����Y������?�\�b�h��Ă�?o��?A�����?�߂�?B
     * </p>
     *
     * @return ?󋵂ɉ�������?�
     */
    private GeometryElement[] coneVertexOnCylinder() {
        switch (getRelationInConeVertexOnCylinder()) {
            case CONE_ENTIRE_OUTSIDE_CYLINDER:
                return onePoint();
            case CONE_RULE_ON_CYLINDER:
                return oneCurve();
            case THETA_EQUAL_SEMI_ANGLE:
                return oneLineOneCurve();
            case THETA_NOT_EQUAL_SEMI_ANGLE:
                return oneLeafOfEightFigureCurve();
            case CONE_ANGLE_INSIDE_CYLINDER:
                return oneCurveOnePoint();
        }
        throw new FatalException();
    }

    /**
     * ��?�Ƃ��Ă�1�_��?��?B
     * <p/>
     * �~??�̒��_��Ԃ�?B
     * </p>
     *
     * @return ��?�(1�_)
     */
    private GeometryElement[] onePoint() {
        GeometryElement[] sol = {conOrg};
        return sol;
    }

    /**
     * ��?�Ƃ��Ă�1��?��?��?B
     * <p/>
     * 1���R��?�(�|�����C���\��)��Ԃ�
     * </p>
     *
     * @return ��?�(1��?�)
     */
    private GeometryElement[] oneCurve() {
        Vector3D conU, conV, conW;

        conW = conAxes[2];
        conV = con2cyl.crossProduct(conW);
        conU = conV.crossProduct(conW);
        conU = conU.unitized();
        conV = conV.unitized();

        Point3D[] point = new Point3D[nst];
        double step = Math.PI * 2 / (nst - 1);
        for (int i = 0; i < nst; i++) {
            double t = -Math.PI + i * step;
            Vector3D evec = cnPointFromT(conU, conV, conW, t);
            double s = -coefBCon(evec, cylAxes[2]) / coefACon(evec, cylAxes[2]);
            point[i] = conOrg.add(evec.multiply(s));
        }
        GeometryElement[] sol = {new Polyline3D(point)};
        return sol;
    }

    /**
     * ��?�Ƃ��Ă�1?��1��?��?��?B
     * <p/>
     * 1��?��1���R��?�(�|�����C���\��)��Ԃ�
     * </p>
     *
     * @return ��?�(1?��1��?�)
     */
    private GeometryElement[] oneLineOneCurve() {
        Vector3D conU, conV, conW;

        conW = conAxes[2];
        conV = con2cyl.crossProduct(conW);
        conU = conV.crossProduct(conW);
        conU = conU.unitized();
        conV = conV.unitized();

        double t, t0, t1, t2, t3;
        double[] bzero = bZero(cylAxes[2], conU, conW);
        Vector3D evec = cnPointFromT(conU, conV, conW, bzero[0]);
        double step, step1, step2;
        if (Math.abs(coefACon(evec, cylAxes[2])) < dTol) {
            t0 = bzero[0];
            if (bzero[0] < bzero[1]) {
                step1 = (bzero[1] - bzero[0]) / (nst - 1);
                step2 = (Math.PI * 2 + bzero[0] - bzero[1]) / (nst - 1);
                t1 = t0 + (step1 / 2.0);
                t2 = bzero[1];
                t3 = t0 + Math.PI * 2 - (step2 / 2.0);
            } else {
                step1 = (bzero[0] - bzero[1]) / (nst - 1);
                step2 = (Math.PI * 2 + bzero[1] - bzero[0]) / (nst - 1);
                t1 = t0 + (step1 / 2.0);
                t2 = bzero[1] + Math.PI * 2;
                t3 = t0 + Math.PI * 2 - (step2 / 2.0);
            }
        } else {
            t0 = bzero[1];
            if (bzero[1] < bzero[0]) {
                step1 = (bzero[0] - bzero[1]) / (nst - 1);
                step2 = (Math.PI * 2 + bzero[1] - bzero[0]) / (nst - 1);
                t1 = t0 + (step1 / 2.0);
                t2 = bzero[0];
                t3 = t0 + Math.PI * 2 - (step2 / 2.0);
            } else {
                step1 = (bzero[1] - bzero[0]) / (nst - 1);
                step2 = (Math.PI * 2 + bzero[0] - bzero[1]) / (nst - 1);
                t1 = t0 + (step1 / 2.0);
                t2 = bzero[0] + Math.PI * 2;
                t3 = t0 + Math.PI * 2 - (step2 / 2.0);
            }
        }

        t = (t1 + t2) / 2.0;
        evec = cnPointFromT(conU, conV, conW, t);
        double a = coefACon(evec, cylAxes[2]);
        double b = coefBCon(evec, cylAxes[2]);
        if ((b / a) > 0.0) {
            t1 = t2;
            t2 = t3;
        }

        step = (t2 - t1) / (nst - 1);
        Point3D[] points = new Point3D[nst];

        // for Polyline
        for (int i = 0; i < nst; i++) {
            t = t1 + i * step;
            evec = cnPointFromT(conU, conV, conW, t);
            a = coefACon(evec, cylAxes[2]);
            b = coefBCon(evec, cylAxes[2]);
            points[i] = conOrg.add(evec.multiply(-b / a));
        }
        Polyline3D polyline = new Polyline3D(points);

        // for Line
        evec = cnPointFromT(conU, conV, conW, t0);
        Line3D line = new Line3D(conOrg, evec);

        GeometryElement[] sol = {polyline, line};
        return sol;
    }

    /**
     * ��?�Ƃ��Ă�1��?�(8�̎���?��1�t)��?��?B
     * <p/>
     * 1���R��?�(�|�����C���\��)��Ԃ�
     * </p>
     *
     * @return ��?�(1��?�:8�̎���?��1�t)
     */
    private GeometryElement[] oneLeafOfEightFigureCurve() {
        Vector3D conU, conV, conW;

        conW = conAxes[2];
        conV = con2cyl.crossProduct(conW);
        conU = conV.crossProduct(conW);
        conU = conU.unitized();
        conV = conV.unitized();

        double[] bzero = bZero(cylAxes[2], conU, conW);
        double t1, t2;
        double step;
        if (bzero[0] < bzero[1]) {
            t1 = bzero[0];
            t2 = bzero[1];
        } else {
            t2 = bzero[0];
            t1 = bzero[1];
        }

        double t = (t1 + t2) / 2.0;
        Vector3D evec = cnPointFromT(conU, conV, conW, t);
        double a = coefACon(evec, cylAxes[2]);
        double b = coefBCon(evec, cylAxes[2]);
        if ((b / a) > 0.0) {
            t1 = t2;
            t2 = t1 + Math.PI * 2;
        }
        step = (t2 - t1) / (nst - 1);
        Point3D[] point = new Point3D[nst];
        for (int i = 0; i < nst; i++) {
            t = t1 + i * step;
            evec = cnPointFromT(conU, conV, conW, t);
            a = coefACon(evec, cylAxes[2]);
            b = coefBCon(evec, cylAxes[2]);
            point[i] = conOrg.add(evec.multiply(-b / a));
        }

        GeometryElement[] sol = {new Polyline3D(point)};
        return sol;
    }

    /**
     * ��?�Ƃ��Ă�1��?��1�_��?��?B
     * <p/>
     * 1�_��1���R��?�(�|�����C���\��)��Ԃ�
     * </p>
     *
     * @return ��?�(1��?��1�_)
     */
    private GeometryElement[] oneCurveOnePoint() {
        Vector3D conU, conV, conW;

        conW = conAxes[2];
        if (beta < aTol)
            conV = conW.crossProduct(cylAxes[2]);
        else
            conV = con2cyl.crossProduct(conW);
        conU = conV.crossProduct(conW);
        conU = conU.unitized();
        conV = conV.unitized();

        double step = Math.PI * 2 / (nst - 1);
        Point3D[] points = new Point3D[nst];
        for (int i = 0; i < nst; i++) {
            double t = -Math.PI + i * step;
            Vector3D evec = cnPointFromT(conU, conV, conW, t);
            double s = -coefBCon(evec, cylAxes[2]) / coefACon(evec, cylAxes[2]);
            points[i] = conOrg.add(evec.multiply(s));
        }

        GeometryElement[] sol = {new Polyline3D(points), conOrg};
        return sol;
    }

    /**
     * �~??�̒��_���~����ɂ���Ƃ�?A���̊֌W��?�?�?󋵂𒲂ׂ�
     * <p/>
     * �~??�̒��_���~����ɂ���Ƃ�?A�����?ׂ���2��?�?��ɕ�������?B
     * ����𔻕ʂ��Ă�?o�����ɒ�?��l�Ƃ��ĕԋp����?B
     * </p>
     *
     * @return �~??�̒��_���~����ɂ���Ƃ�?A���̊֌W��?�?�?�
     */
    private int getRelationInConeVertexInCylinder() {
        double semiAngle = con.semiAngle();
        if ((Math.abs(theta - semiAngle) < aTol)
                || (Math.abs(theta - (Math.PI - semiAngle)) < aTol)) {
            return BOTH_AXIS_IS_EQUAL_SEMI_ANGLE;
        } else {
            return BOTH_AXIS_IS_NOT_EQUAL_SEMI_ANGLE;
        }
    }

    /**
     * �~??�̒��_���~����ɂ���Ƃ��̌�?��Ԃ�
     * <p/>
     * ����ꂽ��?��l����Y������?�\�b�h��Ă�?o��?A�����?�߂�?B
     * </p>
     *
     * @return ?󋵂ɉ�������?�
     */
    private GeometryElement[] coneVertexInCylinder() {
        switch (getRelationInConeVertexInCylinder()) {
            case BOTH_AXIS_IS_EQUAL_SEMI_ANGLE:
                return thetaAndSemiAngleIsSame();
            case BOTH_AXIS_IS_NOT_EQUAL_SEMI_ANGLE:
                return thetaAndSemiAngleIsNotSame();
        }
        throw new FatalException();
    }

    /**
     * ��?�Ƃ��Ă�1��?��?��?B
     * <p/>
     * 1���R��?�(�|�����C���\��)��Ԃ�
     * </p>
     *
     * @return ��?�(1��?�)
     */
    private GeometryElement[] thetaAndSemiAngleIsSame() {
        Vector3D cylU, cylV, cylW;
        Vector3D conU, conV, conW;

        cylW = cylAxes[2];
        cylV = cylW.crossProduct(con2cyl);
        cylU = cylV.crossProduct(cylW);
        cylU = cylU.unitized();
        cylV = cylV.unitized();

        conW = conAxes[2];
        conV = conW.crossProduct(con2cyl).reverse();
        conU = conV.crossProduct(conW);
        conU = conU.unitized();
        conV = conV.unitized();

        Vector3D norm = conW.project(cylW);

        Axis2Placement3D position =
                new Axis2Placement3D(conOrg, norm, cylW);
        Plane3D plane = new Plane3D(position);

        SurfaceSurfaceInterference3D[] intf;
        try {
            intf = plane.intersect(cyl);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        double[] t = new double[2];
        Vector3D evec;
        for (int i = 0; i < intf.length; i++) {
            if (!intf[i].isIntersectionCurve())
                throw new FatalException();

            ParametricCurve3D curve = intf[i].toIntersectionCurve().curve3d();
            Line3D line = (Line3D) curve;
            evec = line.pnt().subtract(cylOrg);
            evec = evec.project(cylW);
            t[i] = cylU.angleWith(evec, cylW);
        }

        double ework;
        if (t[0] > t[1]) {
            ework = t[0];
            t[0] = t[1];
            t[1] = ework;
        }

        double t0 = (t[0] + t[1]) / 2.0;
        Point3D epnt = clPointFromT(cylOrg, cylU, cylV, cyl.radius(), t0);
        double b = coefBCyl(epnt, conW, cylW);
        double c = coefCCyl(epnt, conW);
        double s = -c / b;
        Point3D epnt2 = epnt.add(cylW.multiply(s));
        evec = epnt2.subtract(conOrg);

        if (evec.dotProduct(conW) < 0.0) {
            ework = t[0];
            t[0] = t[1];
            t[1] = ework + Math.PI * 2;
        }

        double step = (t[1] - t[0]) / (nst - 1);
        t[0] += step / 2.0;
        t[1] -= step / 2.0;
        step = (t[1] - t[0]) / (nst - 1);

        Point3D[] points = new Point3D[nst];
        for (int i = 0; i < nst; i++) {
            t0 = t[0] + i * step;
            epnt = clPointFromT(cylOrg, cylU, cylV, cyl.radius(), t0);
            s = -coefCCyl(epnt, conW) / coefBCyl(epnt, conW, cylW);
            points[i] = epnt.add(cylW.multiply(s));
        }

        Polyline3D polyline = new Polyline3D(points);
        GeometryElement[] sol = {polyline};
        return sol;
    }

    /**
     * ��?�Ƃ��Ă�1��?��?��?B
     * <p/>
     * 1���R��?�(�|�����C���\��)��Ԃ�
     * </p>
     *
     * @return ��?�(1��?�)
     */
    private GeometryElement[] thetaAndSemiAngleIsNotSame() {
        Vector3D conU, conV, conW;
        double dX0, dX1;
        double[] dX;

        conW = conAxes[2];
        if (dist < dTol)
            conV = cylAxes[2].crossProduct(conW);
        else
            conV = con2cyl.crossProduct(conW);
        conU = conV.crossProduct(conW);
        conU = conU.unitized();
        conV = conV.unitized();

        double step = Math.PI * 2 / (nst - 1);
        double[] dA = new double[3];
        dA[0] = coefCCon(cylAxes[2], cyl.radius());
        Point3D[] points = new Point3D[nst];
        for (int i = 0; i < nst; i++) {
            double t = -Math.PI + i * step;
            Vector3D evec = cnPointFromT(conU, conV, conW, t);
            dA[2] = coefACon(evec, cylAxes[2]);
            dA[1] = coefBCon(evec, cylAxes[2]);
            if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric((new DoublePolynomial(dA)))) == null)
                throw new FatalException();
            if (dX.length == 1)
                dX0 = dX1 = dX[0];
            else if (dX[0] < dX[1]) {
                dX0 = dX[1];
                dX1 = dX[0];
            } else {
                dX0 = dX[0];
                dX1 = dX[1];
            }
            points[i] = conOrg.add(evec.multiply(dX0));
        }

        Polyline3D polyline = new Polyline3D(points);
        GeometryElement[] sol = {polyline};
        return sol;
    }

    /**
     * �~??�̒��_���~���O�ɂ���Ƃ�?A���̊֌W��?�?�?󋵂𒲂ׂ�
     * <p/>
     * �~??�̒��_���~���O�ɂ���Ƃ�?A�����?ׂ���3��?�?��ɕ�������?B
     * ����𔻕ʂ��Ă�?o�����ɒ�?��l�Ƃ��ĕԋp����?B
     * </p>
     *
     * @return �~??�̒��_���~���O�ɂ���Ƃ�?A���̊֌W��?�?�?�
     */
    private int getRelationInConeVertexOutCylinder() {
        double semiAngle = con.semiAngle();
        if ((Math.abs(theta - semiAngle) < aTol)
                || (Math.abs(theta - (Math.PI - semiAngle)) < aTol))
            return RULING_IS_PARALLEL_TO_AXIS;
        else if (theta < semiAngle || theta > (Math.PI - semiAngle))
            return ANGLE_IS_SMALLER_CONS_SEMI_ANGLE;
        else
            return ANGLE_IS_GREATER_CONS_SEMI_ANGLE;

    }

    /**
     * �~??�̒��_���~���O�ɂ���Ƃ��̌�?��Ԃ�
     * <p/>
     * ����ꂽ��?��l����Y������?�\�b�h��Ă�?o��?A�����?�߂�?B
     * </p>
     *
     * @return ?󋵂ɉ�������?�
     */
    private GeometryElement[] coneVertexOutCylinder() {
        switch (getRelationInConeVertexOutCylinder()) {
            case RULING_IS_PARALLEL_TO_AXIS:
                return rulingIsParallelToAxis();
            case ANGLE_IS_SMALLER_CONS_SEMI_ANGLE:
                return angleIsSmallerConsSemiAngle();
            case ANGLE_IS_GREATER_CONS_SEMI_ANGLE:
                return angleIsGreaterConsSemiAngle();
        }
        throw new FatalException();
    }

    /**
     * ��?�Ƃ��Ă�1��?��?��?B
     * <p/>
     * 1���R��?�(�|�����C���\��)��Ԃ�
     * </p>
     *
     * @return ��?�(1��?�)
     */
    private GeometryElement[] rulingIsParallelToAxis() {
        if (con2cyl.dotProduct(conAxes[2]) < 0.0)
            return new GeometryElement[0];

        Vector3D cylU, cylV, cylW, conW;

        cylW = cylAxes[2];
        cylV = cylW.crossProduct(con2cyl);
        cylU = cylV.crossProduct(cylW);
        cylU = cylU.unitized();
        cylV = cylV.unitized();
        conW = conAxes[2];

        double step = Math.PI * 2 / (nst - 1);
        Point3D[] points = new Point3D[nst];
        for (int i = 0; i < nst; i++) {
            double t = -Math.PI + i * step;
            Point3D epnt = clPointFromT(cylOrg, cylU, cylV, cyl.radius(), t);
            double s = -coefCCyl(epnt, conW) / coefBCyl(epnt, conW, cylW);
            points[i] = epnt.add(cylW.multiply(s));
        }
        Polyline3D polyline = new Polyline3D(points);
        GeometryElement[] sol = {polyline};
        return sol;
    }

    /**
     * ��?�Ƃ��Ă�1��?��?��?B
     * <p/>
     * 1���R��?�(�|�����C���\��)��Ԃ�
     * </p>
     *
     * @return ��?�(1��?�)
     */
    private GeometryElement[] angleIsSmallerConsSemiAngle() {
        Vector3D cylU, cylV, cylW, conW;
        double dX0, dX1;
        double[] dX;

        cylW = cylAxes[2];
        cylV = cylW.crossProduct(con2cyl);
        cylU = cylV.crossProduct(cylW);
        cylU = cylU.unitized();
        cylV = cylV.unitized();
        conW = conAxes[2];

        double[] dA = new double[3];
        dA[2] = coefACyl(conW, cylW);
        Point3D epnt = clPointFromT(cylOrg, cylU, cylV, cyl.radius(), 0.0);
        dA[1] = coefBCyl(epnt, conW, cylW);
        dA[0] = coefCCyl(epnt, conW);

        if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric((new DoublePolynomial(dA)))) == null)
            throw new FatalException();
        if (dX.length == 1)
            dX0 = dX1 = dX[0];
        else if ((dX.length == 2) && (dX[0] < dX[1])) {
            dX0 = dX[1];
            dX1 = dX[0];
        } else {
            dX0 = dX[0];
            dX1 = dX[1];
        }
        Point3D epnt2 = epnt.add(cylW.multiply(dX0));
        Vector3D evec = epnt.subtract(conOrg);

        double step = Math.PI * 2 / (nst - 1);
        Point3D[] points = new Point3D[nst];
        for (int i = 0; i < nst; i++) {
            double t = Math.PI + i * step;
            epnt = clPointFromT(cylOrg, cylU, cylV, cyl.radius(), t);
            dA[1] = coefBCyl(epnt, conW, cylW);
            dA[0] = coefCCyl(epnt, conW);
            if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric((new DoublePolynomial(dA)))) == null)
                throw new FatalException();
            if (dX.length == 1)
                dX0 = dX1 = dX[0];
            else if ((dX.length == 2) && (dX[0] < dX[1])) {
                dX0 = dX[1];
                dX1 = dX[0];
            } else {
                dX0 = dX[0];
                dX1 = dX[1];
            }
            double s = (evec.dotProduct(conW) > 0.0) ? dX0 : dX1;
            points[i] = epnt.add(cylW.multiply(s));
        }
        Polyline3D polyline = new Polyline3D(points);
        GeometryElement[] sol = {polyline};
        return sol;
    }

    /**
     * ��?�Ƃ��Ă̑���?��?��?B
     * <p/>
     * �����R��?�(�|�����C���\��)��Ԃ�
     * </p>
     *
     * @return ��?�(����?�)
     */
    private GeometryElement[] angleIsGreaterConsSemiAngle() {
        int myNst = nst / 2;
        double dX0, dX1;
        double dX[];
        Point3D[][] points = new Point3D[10][];

        if (con2cyl.dotProduct(conAxes[2]) < 0.0)
            return new GeometryElement[0];

        Vector3D evec = con2cyl.unitized();
        Vector3D cylU, cylV, cylW;
        Vector3D conU, conV, conW;

        cylW = cylAxes[2];
        conW = conAxes[2];

        if (Math.abs(cylW.dotProduct(evec)) > Math.cos(aTol))
            cylV = cylW.crossProduct(conW);
        else
            cylV = cylW.crossProduct(con2cyl);
        cylU = cylV.crossProduct(cylW);
        cylU = cylU.unitized();
        cylV = cylV.unitized();

        if (Math.abs(conW.dotProduct(evec)) > Math.cos(aTol))
            conV = conW.crossProduct(cylW);
        else
            conV = conW.crossProduct(con2cyl);
        conV = conV.reverse();
        conU = conV.crossProduct(conW);
        conU = conU.unitized();
        conV = conV.unitized();

        double euw, evw, eww;
        euw = conU.dotProduct(cylW);
        evw = conV.dotProduct(cylW);
        eww = conW.dotProduct(cylW);

        double[] dA = new double[3];
        dA[2] = (euw * euw) + (evw * evw);
        dA[1] = 2.0 * conTan * eww * euw;
        dA[0] = (conTan * conTan * eww * eww) - (evw * evw);

        if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric((new DoublePolynomial(dA)))) == null)
            throw new FatalException();
        int nnn = dX.length;
        if (nnn == 1)
            dX0 = dX1 = dX[0];
        else if ((nnn == 2) && (dX[0] < dX[1])) {
            dX0 = dX[1];
            dX1 = dX[0];
        } else {
            dX0 = dX[0];
            dX1 = dX[1];
        }

        double ecos = dX0;
        double esin;
        int nnnn = 0;
        Vector3D[] norm = new Vector3D[2];
        double reps = 1.0e-8;
        if (((-1.0 - reps) < ecos) && (ecos < (1.0 + reps))) {
            if (ecos > 1.0) ecos = 1.0;
            if (ecos < -1.0) ecos = -1.0;
            esin = Math.sqrt(1.0 - (ecos * ecos));
            if (Math.abs((conTan * eww) + (ecos * euw) + (esin * evw)) > dTol)
                esin = -esin;
            norm[0] = conW.multiply(conTan);
            norm[0] = norm[0].add(conU.multiply(ecos));
            norm[0] = norm[0].add(conV.multiply(esin));
            nnnn++;
        }

        if (nnn == 2) {
            ecos = dX1;
            if (((-1.0 - reps) < ecos) && (ecos < (1.0 + reps))) {
                if (ecos > 1.0) ecos = 1.0;
                if (ecos < -1.0) ecos = -1.0;
                esin = Math.sqrt(1.0 - (ecos * ecos));
                if (Math.abs((conTan * eww) + (ecos * euw) + (esin * evw)) > dTol)
                    esin = -esin;
                norm[nnnn] = conW.multiply(conTan);
                norm[nnnn] = norm[nnnn].add(conU.multiply(ecos));
                norm[nnnn] = norm[nnnn].add(conV.multiply(esin));
                nnnn++;
            }
        }
        nnn = nnnn;

        int n = 0;
        Line3D savedLine = null;
        double[] t = new double[5];
        int num = 0;
        for (int i = 0; i < nnn; i++) {
            Axis2Placement3D position = new Axis2Placement3D(conOrg, norm[i], cylW);
            Plane3D plane = new Plane3D(position);
            SurfaceSurfaceInterference3D[] intf;
            try {
                intf = plane.intersect(cyl);
            } catch (IndefiniteSolutionException e) {
                throw new FatalException();
            }

            if (intf.length <= 0) continue;

            Line3D line = null;
            for (int j = 0; j < intf.length; j++) {
                line = (Line3D) intf[j].toIntersectionCurve().curve3d();
                if (num == 0) {
                    savedLine = new Line3D(line.pnt(), line.dir());
                }
                evec = line.pnt().subtract(cylOrg);
                evec = evec.project(cylW);
                t[num++] = cylU.angleWith(evec, cylW);
            }
        }

        if (num != 0) {
            int j0 = 0;
            int jn = num - 1;
            GeometryUtils.sortDoubleArray(t, j0, jn);
            t[num] = t[0] + Math.PI * 2;
        } else {
            num = 1;
            t[0] = -Math.PI;
            t[num] = Math.PI;
        }
        dA[2] = coefACyl(conW, cylW);

        double step;
        Polyline3D[] polyline = new Polyline3D[10];
        for (int i = 0; i < num; i++) {
            double t0 = (t[i] + t[i + 1]) / 2.0;
            Point3D epnt = clPointFromT(cylOrg, cylU, cylV, cyl.radius(), t0);
            dA[1] = coefBCyl(epnt, conW, cylW);
            dA[0] = coefCCyl(epnt, conW);
            double d = dA[1] * dA[1] - (4.0 * dA[2] * dA[0]);
            if (d < 0.0) {
                if ((num == 1) && (savedLine != null)) {
                    PointOnCurve3D[] pt;
                    try {
                        pt = conAxis.commonNormal(savedLine);
                    } catch (IndefiniteSolutionException e) {
                        throw new FatalException();
                    }
                    Point3D point = pt[1];
                    GeometryElement[] sol = {point};
                    return sol;
                }
                continue;
            }

            if (num == 1) {
                points[n] = new Point3D[nst];
                points[n + 1] = new Point3D[nst];
                step = (t[i + 1] - t[i]) / (nst - 1);
                for (int j = 0; j < nst; j++) {
                    t0 = t[i] + (j * step);
                    epnt = clPointFromT(cylOrg, cylU, cylV, cyl.radius(), t0);
                    dA[1] = coefBCyl(epnt, conW, cylW);
                    dA[0] = coefCCyl(epnt, conW);
                    if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric((new DoublePolynomial(dA)))) == null)
                        throw new FatalException();
                    int nn = dX.length;
                    if (nn == 1)
                        dX0 = dX1 = dX[0];
                    else if ((nn == 2) && (dX[0] < dX[1])) {
                        dX0 = dX[1];
                        dX1 = dX[0];
                    } else {
                        dX0 = dX[0];
                        dX1 = dX[1];
                    }
                    points[n][j] = epnt.add(cylW.multiply(dX0));
                    points[n + 1][j] = epnt.add(cylW.multiply(dX1));
                }
                n += 2;
            } else {
                points[n] = new Point3D[nst];
                step = (t[i + 1] - t[i]) / myNst;
                int j;
                for (j = 0; j < myNst; j++) {
                    t0 = t[i] + (j * step);
                    epnt = clPointFromT(cylOrg, cylU, cylV, cyl.radius(), t0);
                    dA[1] = coefBCyl(epnt, conW, cylW);
                    dA[0] = coefCCyl(epnt, conW);
                    if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric((new DoublePolynomial(dA)))) == null)
                        throw new FatalException();
                    int nn = dX.length;
                    if (nn == 1)
                        dX0 = dX1 = dX[0];
                    else if ((nn == 2) && (dX[0] < dX[1])) {
                        dX0 = dX[1];
                        dX1 = dX[0];
                    } else {
                        dX0 = dX[0];
                        dX1 = dX[1];
                    }
                    points[n][j] = epnt.add(cylW.multiply(dX0));
                    points[n][nst - 1 - j] = epnt.add(cylW.multiply(dX1));
                }
                t0 = t[i] + (j * step);
                epnt = clPointFromT(cylOrg, cylU, cylV, cyl.radius(), t0);
                dA[1] = coefBCyl(epnt, conW, cylW);
                dA[0] = coefCCyl(epnt, conW);
                if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric((new DoublePolynomial(dA)))) == null)
                    throw new FatalException();
                int nn = dX.length;
                if (nn == 1)
                    dX0 = dX1 = dX[0];
                else if ((nn == 2) && (dX[0] < dX[1])) {
                    dX0 = dX[1];
                    dX1 = dX[0];
                } else {
                    dX0 = dX[0];
                    dX1 = dX[1];
                }
                points[n][j] = epnt.add(cylW.multiply(dX0));
                n += 1;
            }
        }

        GeometryElement[] sol = new GeometryElement[n];
        for (int i = 0; i < n; i++) {
            sol[i] = new Polyline3D(points[i], false);
        }
        return sol;
    }

    /**
     * private function (cnPointFromT)
     */
    private Vector3D cnPointFromT(Vector3D conU,
                                  Vector3D conV,
                                  Vector3D conW,
                                  double t) {
        double cost, sint;

        cost = Math.cos(t);
        sint = Math.sin(t);

        return new LiteralVector3D
                (conTan * ((cost * conU.x()) + (sint * conV.x())) + conW.x(),
                        conTan * ((cost * conU.y()) + (sint * conV.y())) + conW.y(),
                        conTan * ((cost * conU.z()) + (sint * conV.z())) + conW.z());
    }

    /**
     * private function (coefACon)
     */
    private double coefACon(Vector3D conVec, Vector3D cylW) {
        double edot;

        edot = conVec.dotProduct(cylW);
        return ((conTan * conTan) + 1.0 - (edot * edot));
    }

    /**
     * private function (coefBCon)
     */
    private double coefBCon(Vector3D conVec, Vector3D cylW) {
        double edot;
        Vector3D evec;
        Vector3D cyl2con = con2cyl.reverse();

        edot = cyl2con.dotProduct(cylW);
        evec = cyl2con.subtract(cylW.multiply(edot));

        return (2.0 * evec.dotProduct(conVec));
    }

    /**
     * private function (coefCCon)
     */
    private double coefCCon(Vector3D cylW, double clr) {
        double edot, edot2;

        edot = con2cyl.dotProduct(con2cyl);
        edot2 = con2cyl.dotProduct(cylW);

        return (edot - (edot2 * edot2) - (clr * clr));
    }

    /**
     * private function (bZero)
     */
    private double[] bZero(Vector3D cylW, Vector3D conU, Vector3D conW) {
        Vector3D axis = conU.project(cylW);
        Axis2Placement3D position =
                new Axis2Placement3D(conOrg, axis, cylW);
        Plane3D plane = new Plane3D(position);
        Circle3D circle = new Circle3D(con.position(), con.radius());
        IntersectionPoint3D[] intp;
        try {
            intp = circle.intersect(plane);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }
        Vector3D evec;

        double[] bzero = new double[intp.length];
        for (int i = 0; i < intp.length; i++) {
            evec = intp[i].subtract(conOrg);
            evec = evec.project(conW);
            bzero[i] = conU.angleWith(evec, conW);
        }
        return bzero;
    }

    /**
     * private function (clPointFromT)
     */
    private Point3D clPointFromT(Point3D c,
                                 Vector3D u,
                                 Vector3D v,
                                 double r,
                                 double t) {
        double cost, sint;

        cost = Math.cos(t);
        sint = Math.sin(t);

        return new CartesianPoint3D
                (c.x() + r * ((cost * u.x()) + (sint * v.x())),
                        c.y() + r * ((cost * u.y()) + (sint * v.y())),
                        c.z() + r * ((cost * u.z()) + (sint * v.z())));
    }

    /**
     * private function (coefACyl)
     */
    private double coefACyl(Vector3D conW, Vector3D cylW) {
        double edot;

        edot = conW.dotProduct(cylW);
        return ((edot * edot) - (conCos * conCos));
    }

    /**
     * private function (coefBCyl)
     */
    private double coefBCyl(Point3D pepnt, Vector3D conW, Vector3D cylW) {
        double edot, conCos2;
        Vector3D evec2, evec3;

        edot = conW.dotProduct(cylW);
        conCos2 = conCos * conCos;

        evec2 = new LiteralVector3D((edot * conW.x()) - (conCos2 * cylW.x()),
                (edot * conW.y()) - (conCos2 * cylW.y()),
                (edot * conW.z()) - (conCos2 * cylW.z()));
        evec3 = pepnt.subtract(conOrg);

        return (2.0 * evec3.dotProduct(evec2));
    }

    /**
     * private function (coefCCyl)
     */
    private double coefCCyl(Point3D pepnt, Vector3D conW) {
        Vector3D evec;
        double edot, edot2;

        evec = pepnt.subtract(conOrg);
        edot = evec.dotProduct(conW);
        edot2 = evec.dotProduct(evec);

        return ((edot * edot) - (conCos * conCos * edot2));
    }

    /**
     * ��Ȃ���Ԃ�
     *
     * @return ��Ȃ���\������傫��0�̔z��
     */
    private GeometryElement[] noIntersectionCurve() {
        return new GeometryElement[0];
    }

    /**
     * �~���Ɖ~??�̊֌W�𒲂ׂĕԂ�
     * <p/>
     * �~���Ɖ~??�̈ʒu�֌W�𒲂�?A�傫��6��?�?��ɕ�����?B
     * �Ă�?o�����ɒ�?��l�Ƃ��ĕԋp����?B
     * </p>
     *
     * @return �~���Ɖ~??��?�Ԃ�\��?�?�
     */
    private int getRelation() {
        if (isSpecialCase1())
            return SPECIAL_CASE_1;
        else if (isSpecialCase2())
            return SPECIAL_CASE_2;
        else if (isSpecialCase3())
            return SPECIAL_CASE_3;
        else if (isConesVertexOnCylinder())
            return CONE_VERTEX_ON_CYLINDER;
        else if (isConesVertexInCylinder())
            return CONE_VERTEX_IN_CYLINDER;
        else
            return CONE_VERTEX_OUT_CYLINDER;
    }

    /**
     * �~���Ɖ~??�̊֌W�ɂ������BČ�?��?�߂�(�Б�)
     * <p/>
     * ����ꂽ��?��l����Y������?�\�b�h��Ă�?o��?A
     * ���҂̂����?ׂ����֌W�𒲂�?A�����?�߂�?B
     * �~??�̕Б��݂̂Ƃ̌����?�߂�?B
     * </p>
     *
     * @param relation �~���Ɖ~??��?�Ԃ�\��?�?�
     * @return ��?�̔z��
     */
    GeometryElement[] doIntersect(int relation) {
        switch (relation) {
            case SPECIAL_CASE_1:
                // Cone's and Cylinder's axis is coincident and
                // Cone's Origin is on Cylinder's axis
                // make one circle
                return oneCircle();
            case SPECIAL_CASE_2:
                // Cone's ridge line is touch Cylinder's surface at Cylinder's inside or outside
                // make one line & one ellipse or one line
                return oneEllipseOneLine();
            case SPECIAL_CASE_3:
                // Cone's both ridge line is touch Cylinder's surface at Cone's inside
                // make two ellipse
                return twoEllipse();
            case CONE_VERTEX_ON_CYLINDER:
                // Cone's vertex is on Cylinder
                // more 5 patterns
                return coneVertexOnCylinder();
            case CONE_VERTEX_IN_CYLINDER:
                // Cone's vertex is is Cylinder
                // more 2 patterns
                return coneVertexInCylinder();
            case CONE_VERTEX_OUT_CYLINDER:
                // Cone's vertex is out of Cylinder
                // more 3 patterns
                return coneVertexOutCylinder();
        }
        throw new FatalException();
    }

    /**
     * �t���̉~??�œ���ꂽ��?�?���?�����
     * <p/>
     * �~??�̋t���Ƃ̌����?�߂�����?A?����Ɠ���������?o���ꂽ��?A
     * ��ς�BĂ����肷��?}�`������̂�?A������?�����?��ɒ���
     * </p>
     *
     * @param geoms1   ?����̉~??�œ���ꂽ��?�
     * @param geoms2   �t���̉~??�œ���ꂽ��?�
     * @param relation �~���Ɖ~??��?�Ԃ�\��?�?�
     * @return ��?�̔z��
     */
    static GeometryElement[] adjustSolution(GeometryElement[] geoms1,
                                            GeometryElement[] geoms2,
                                            int relation) {
        // ����̊�?}�`��Ԉ�?��?��
        // �t���̉~??�Ƃ̊Ԃɋ?�߂�ꂽ��?�?���?�����
        switch (relation) {
            case SPECIAL_CASE_1:
                // �Ԉ�?}�`�͂Ȃ�(�t���͕ʂ̎��R��?���͂�)
                // �t���̉~???�Ō�?�?�߂��Ă���̂�?A
                // ��?�(�~)��?���?�����
                Circle3D circle1 = (Circle3D) geoms1[0];
                Circle3D circle2 = (Circle3D) geoms2[0];
                Axis2Placement3D circlePos =
                        new Axis2Placement3D(circle2.position().location(),
                                circle1.position().axis(),
                                circle1.position().refDirection());
                geoms2[0] = new Circle3D(circlePos, circle2.radius());
                break;
            case SPECIAL_CASE_2:
                // ����̒�?�ʂƂ��Ă����Ă���?�?�?A
                // ���̒�?�싉ʂ���Ԉ�
                // geom2�ɒ�?��Ă���?�?���?A�����Ԉ�
                // �ȉ~������Ă���?�?���?A�ȉ~?���?�����
                if (geoms2.length == 1) {
                    if (geoms2[0] instanceof Line3D) {
                        geoms2 = new GeometryElement[0];
                    }
                } else if (geoms2.length == 2) {
                    Ellipse3D ellipse;
                    if ((geoms2[0] instanceof Line3D)
                            && (geoms2[1] instanceof Ellipse3D)) {
                        ellipse = (Ellipse3D) geoms2[1];
                    } else if ((geoms2[1] instanceof Line3D)
                            && (geoms2[0] instanceof Ellipse3D)) {
                        ellipse = (Ellipse3D) geoms2[0];
                    } else {
                        throw new FatalException();
                    }
                    Axis2Placement3D ellipsePos =
                            new Axis2Placement3D(ellipse.position().location(),
                                    ellipse.position().axis().reverse(),
                                    ellipse.position().refDirection().reverse());
                    geoms2 = new GeometryElement[1];
                    geoms2[0] = new Ellipse3D(ellipsePos,
                            ellipse.semiAxis1(),
                            ellipse.semiAxis2());
                } else {
                    throw new FatalException();
                }
                break;
            case SPECIAL_CASE_3:
                // �Ԉ�?}�`�͂Ȃ�(�t���� null �̂͂�)
                break;
            case CONE_VERTEX_ON_CYLINDER:
                // ����̓_�����ʂƂ��Č���Ă�?�?�?A�Ȃ�����?A
                // ����̒�?�ʂƂ��Č���Ă���?�?�?A
                // ���싉ʂ���Ԉ�
                if (geoms2.length == 1) {
                    if (geoms2[0] instanceof Point3D) {
                        geoms2 = new GeometryElement[0];
                    }
                } else if (geoms2.length == 2) {
                    if ((geoms2[0] instanceof Line3D)
                            || (geoms2[1] instanceof Line3D)) {
                        ParametricCurve3D curve;
                        if ((geoms2[0] instanceof Line3D) && (geoms2[1] instanceof ParametricCurve3D)) {
                            curve = (ParametricCurve3D) geoms2[1];
                        } else if ((geoms2[1] instanceof Line3D) && (geoms2[0] instanceof ParametricCurve3D)) {
                            curve = (ParametricCurve3D) geoms2[0];
                        } else {
                            throw new FatalException();
                        }
                        geoms2 = new GeometryElement[1];
                        geoms2[0] = curve;
                    } else if ((geoms2[0] instanceof Point3D)
                            || (geoms2[1] instanceof Point3D)) {
                        ParametricCurve3D curve;
                        if ((geoms2[0] instanceof Point3D) && (geoms2[1] instanceof ParametricCurve3D)) {
                            curve = (ParametricCurve3D) geoms2[1];
                        } else if ((geoms2[1] instanceof Point3D) && (geoms2[0] instanceof ParametricCurve3D)) {
                            curve = (ParametricCurve3D) geoms2[0];
                        } else {
                            throw new FatalException();
                        }
                        geoms2 = new GeometryElement[1];
                        geoms2[0] = curve;
                    }
                } else {
                    throw new FatalException();
                }
                break;
            case CONE_VERTEX_IN_CYLINDER:
                // �Ԉ�?}�`�͂Ȃ�(�t���͕ʂ̎��R��?���͂�)
                break;
            case CONE_VERTEX_OUT_CYLINDER:
                // �Ԉ�?}�`�͂Ȃ�(�t���� null �̂͂�)
                break;
            default:
                throw new FatalException();
        }
        return geoms2;
    }

    /**
     * �~���Ɖ~??�̌�?��?�߂�(����)
     * <p/>
     * �~���Ɖ~??�̌����?�߂�?B�Б��̉~??��?�߂���?A
     * �~??��ЂB���Ԃ���?A������Ƃ�����?�߂�?B
     * ��?d�ȂB���?A�`?�̌�t�ɂȂ�?�?�������̂�?A
     * ���?C?�����?A?��K�̉��?�߂�?B
     * </p>
     *
     * @param cyl        �~��
     * @param con        �~??
     * @param doExchange ���ʂ�귂��邩�ǂ����̃t���O
     * @return ��?�̔z��
     */
    static SurfaceSurfaceInterference3D[] intersection(CylindricalSurface3D cyl, ConicalSurface3D con, boolean doExchange) {
        // ���̂܂܂̉~??�Ō��ʂ�?�߂�
        IntsCylCon3D doObj =
                new IntsCylCon3D(cyl, con, doExchange);
        int relation1 = doObj.getRelation();
        GeometryElement[] geoms1 = doObj.doIntersect(relation1);

        // �~??��ЂB���Ԃ���?A���ʂ�?�߂�
        ConicalSurface3D reversedCone = con.getReverse();
        IntsCylCon3D reversedDoObj =
                new IntsCylCon3D(cyl, reversedCone, doExchange);
        int relation2 = reversedDoObj.getRelation();
        GeometryElement[] geoms2 = reversedDoObj.doIntersect(relation2);

        // �ЂB���Ԃ��Ă�?A�~���Ɖ~??�̊֌W�͓����͂�������
        if (relation1 != relation2) throw new FatalException();

        // ����ꂽ���?�����
        geoms2 = adjustSolution(geoms1, geoms2, relation2);

        // 2�̉~??�ȖʂƂ̉��}?[�W����
        // ����?�?A�x?[�X�̉~??�Ȗʂɑ΂��� IntersectionCurve ��?��悤�ɂ���
        SurfaceSurfaceInterference3D[] intf =
                new SurfaceSurfaceInterference3D[geoms1.length + geoms2.length];
        int i = 0;

        // ?����̉~??�Ƃ̌�?�
        for (int j = 0; j < geoms1.length; j++, i++) {
            if (geoms1[j].isCurve()) {
                intf[i] = cyl.curveToIntersectionCurve((ParametricCurve3D) geoms1[j], con, doExchange);

            } else { // geoms1[j].isPoint()
                intf[i] = cyl.pointToIntersectionPoint((Point3D) geoms1[j], con, doExchange);
            }
        }

        // �t���̉~??�Ƃ̌�?�
        for (int j = 0; j < geoms2.length; j++, i++) {
            if (geoms2[j].isCurve()) {
                intf[i] = cyl.curveToIntersectionCurve((ParametricCurve3D) geoms2[j], con, doExchange);

            } else { // geoms1[j].isPoint()
                intf[i] = cyl.pointToIntersectionPoint((Point3D) geoms2[j], con, doExchange);
            }
        }
        return intf;
    }
}
