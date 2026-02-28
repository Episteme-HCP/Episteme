/*
 * 3D�V�����_���m�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsCylCyl3D.java,v 1.2 2007-10-21 17:38:24 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.analysis.polynomials.DoublePolynomial;
import org.episteme.util.FatalException;

/**
 * �R����?F�V�����_���m�̌�_��?�߂�N���X
 * <p/>
 * �~�����m�̌����?A�Q�̈ʒu�֌W�ɂ�B�?�?������ⵂ�?A���ꂼ��?�߂�?B
 * �~�����m�̈ʒu�֌W�ɂ��?A�P�O�ʂ��?�?���������?݂���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:24 $
 */

class IntsCylCyl3D {
    /**
     * 2�V�����_�̊֌W��\�����邽�߂̒�?�
     */
    /**
     * �V�����_����v����?��
     */
    private static final int COINCIDENT = 0;

    /**
     * �V�����_�̎�����?s��?��
     */
    private static final int PARALLEL = 1;

    /**
     * �V�����_�̎�����?s���V�����_���m��?�?G���Ȃ�?��
     */
    private static final int PARALLEL_NO_TOUCH = 2;

    /**
     * �V�����_�̎�����?s���V�����_���m��?�?G����?��
     */
    private static final int PARALLEL_TOUCH = 3;

    /**
     * �V�����_�̎�����?s���V�����_���m������?��
     */
    private static final int PARALLEL_INTERSECT = 4;

    /**
     * �V�����_�̎�����?����邩���a������?��
     */
    private static final int INTERSECT_SAME_RADIUS = 5;

    /**
     * �V�����_�̎�����?����Ȃ�����
     * ���̃V�����_������̃V�����_�ƌ���Ȃ�?��
     */
    private static final int NO_INTERSECT_OUT_OF_LINE = 6;

    /**
     * �V�����_�̎�����?����Ȃ�����
     * ���̃V�����_������̂̃V�����_�Ɗ��S�ɓՔ�Ō���?��
     */
    private static final int NO_INTERSECT_BETWEEN_LINE = 7;

    /**
     * �V�����_�̎�����?����Ȃ�����
     * ���̃V�����_������̃V�����_�ƊO����?�?G����?��
     */
    private static final int NO_INTERSECT_TOUCH_OUTSIDE = 8;

    /**
     * �V�����_�̎�����?����Ȃ�����
     * ���̃V�����_������̃V�����_�ƓՔ��?�?G����?��
     */
    private static final int NO_INTERSECT_TOUCH_INSIDE = 9;

    /**
     * �V�����_�̎�����?����Ȃ�����
     * ���̃V�����_������̃V�����_�ƕ����I�Ɍ���?��
     */
    private static final int NO_INTERSECT_LINE_INTERSECT = 10;

    /**
     * �V�����_��
     */
    private CylindricalSurface3D bCyl;

    /**
     * �V�����_?�
     */
    private CylindricalSurface3D sCyl;

    /**
     * �V�����_��̌��_
     */
    private Point3D bOrg;

    /**
     * �V�����_?��̌��_
     */
    private Point3D sOrg;

    /**
     * �V�����_��̎�
     */
    private Line3D bAxis;

    /**
     * �V�����_?��̎�
     */
    private Line3D sAxis;

    /**
     * �P�ʃx�N�g��
     */
    private Vector3D bCylU;
    private Vector3D bCylV;
    private Vector3D bCylW;

    /**
     * �V�����_���m�̎��̓�?�
     */
    private double w1w2;

    /**
     * ��?��\������|�����C���̓_��?�
     */
    private static final int nst = 41;

    /**
     * �V�����_��귂��邩�ǂ����̃t���O
     */
    private boolean doExchange;

    /**
     * ���̌�_(�ȉ~�̒�?S�ɗ��p)
     */
    private IntersectionPoint3D axisIntsP;

    /**
     * ��V�����_?��?��?��V�����_�̌�_
     */
    private IntersectionPoint3D[] bLineSCylIntsP;

    /**
     * 2�̉~���ƌ��ʂ�귂��邩�ǂ����̃t���O��^����
     * �����?�߂邽�߂̃I�u�W�F�N�g��?\�z����?B
     *
     * @param cyl1       �V�����_
     * @param cyl2       �V�����_
     * @param doExchange �V�����_��귂��邩�ǂ����̃t���O
     */
    private IntsCylCyl3D(CylindricalSurface3D cyl1,
                         CylindricalSurface3D cyl2,
                         boolean doExchange) {
        //
        // ���a�̑傫���Ȃǂ𒲂�?A�e?���l��?ݒ肷��
        //
        if (cyl1.radius() < cyl2.radius()) {
            sCyl = cyl1;
            bCyl = cyl2;
            this.doExchange = !doExchange;
        } else {
            sCyl = cyl2;
            bCyl = cyl1;
            this.doExchange = doExchange;
        }

        Axis2Placement3D bPosition = bCyl.position();
        Axis2Placement3D sPosition = sCyl.position();

        bOrg = bPosition.location();
        sOrg = sPosition.location();

        bAxis = bCyl.getAxis();
        sAxis = sCyl.getAxis();

        bCylW = bPosition.z();
        bCylV = bCylW.crossProduct(sOrg.subtract(bOrg)).unitized();
        bCylU = bCylV.crossProduct(bCylW).unitized();
        w1w2 = bAxis.dir().dotProduct(sAxis.dir());

    }

    /**
     * �V�����_�̎���ʂ镽�ʂƂ��̃V�����_�����BĂł����?��?��
     *
     * @return 2�{�̌�?�
     */
    private Line3D[] makeLinesOnCylinder() {
        Vector3D zOfSCyl = sAxis.dir();
        Vector3D zOfBCyl = bAxis.dir();
        Vector3D evec = zOfSCyl.crossProduct(zOfBCyl);

        Vector3D axis = evec.crossProduct(zOfBCyl);
        Vector3D refDirection = zOfBCyl;
        Point3D location = bCyl.position().location();

        Plane3D bPlane = new Plane3D
                (new Axis2Placement3D(location, axis, refDirection));

        try {
            SurfaceSurfaceInterference3D[] intf = bCyl.intersect(bPlane);
            int nSol = intf.length;
            Line3D[] lines = new Line3D[nSol];
            for (int i = 0; i < nSol; i++) {
                ParametricCurve3D curve = intf[i].toIntersectionCurve().curve3d();
                if (curve instanceof Line3D) {
                    lines[i] = (Line3D) curve;
                }
            }
            return lines;
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }
    }

    /**
     * �V�����_���m�̎�����?s��?�Ԃɂ�����?A����Ȃ�?�Ԃ𒲂ׂĕԂ�
     * <p/>
     * �V�����_���m�̎�������?s�ȂƂ�?A�����?ׂ���3��?�?��ɕ�������?B
     * ����𔻕ʂ��Ă�?o�����ɒ�?��l�Ƃ��ĕԋp����?B
     * </p>
     *
     * @return �V�����_���m��?�Ԃ�\��?�?�
     */
    private int getRelationInParallel() {
        Point3D source = sOrg;
        PointOnCurve3D[] foot = bAxis.projectFrom(source);
        if (foot.length != 1) throw new FatalException();
        double dist = source.distance(foot[0]);
        double dTol = bCyl.getToleranceForDistance();
        double bRadius = bCyl.radius();
        double sRadius = sCyl.radius();

        if (Math.abs(dist - (bRadius + sRadius)) < dTol
                || Math.abs(dist - (bRadius - sRadius)) < dTol)
            return PARALLEL_TOUCH;
        else if (dist > (bRadius + sRadius)
                || dist < (bRadius - sRadius))
            return PARALLEL_NO_TOUCH;
        else
            return PARALLEL_INTERSECT;
    }

    /**
     * �V�����_���m�̎�����?s�łȂ�?�Ԃɂ�����?A����Ȃ�?�Ԃ𒲂ׂĕԂ�
     * <p/>
     * �V�����_���m�̎�������?s�łȂ��Ƃ�?A�����?ׂ���?�?��ɕ�������?B
     * ����𔻕ʂ��Ă�?o�����ɒ�?��l�Ƃ��ĕԋp����?B
     * </p>
     *
     * @return �V�����_���m��?�Ԃ�\��?�?�
     */
    private int getRelationInNotParallel() {
        double dTol = bCyl.getToleranceForDistance();
        IntersectionPoint3D[] intsP;
        try {
            intsP = bAxis.intersect(sAxis);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        if ((intsP.length == 1) && (Math.abs(bCyl.radius() - sCyl.radius()) < dTol)) {
            axisIntsP = intsP[0];
            return INTERSECT_SAME_RADIUS;
        } else
            return getRelationInNotParallelNotIntersect();
    }

    /**
     * �V�����_���m�̎�����?s�łȂ�����������Ȃ�?�Ԃɂ�����?A
     * ����Ȃ�?�Ԃ𒲂ׂĕԂ�
     * <p/>
     * �V�����_���m�̎�������?s�łȂ�����?A��������Ȃ�?�Ԃɂ�����?A
     * �����?ׂ���?�?��ɕ�������?B
     * ����𔻕ʂ��Ă�?o�����ɒ�?��l�Ƃ��ĕԋp����?B
     * </p>
     *
     * @return �V�����_���m��?�Ԃ�\��?�?�
     */
    private int getRelationInNotParallelNotIntersect() {
        Line3D[] bLines = makeLinesOnCylinder();
        IntersectionPoint3D[] intsPB1;
        IntersectionPoint3D[] intsPB2;
        try {
            intsPB1 = bLines[0].intersect(sCyl);
            intsPB2 = bLines[1].intersect(sCyl);
        } catch (IndefiniteSolutionException e) {
            // �V�����_���m�̎�����?s�łȂ��̂�?A���̃P?[�X�͋N���蓾�Ȃ��͂�
            throw new FatalException();
        }
        int n1 = intsPB1.length;
        int n2 = intsPB2.length;

        if (n1 != 0)
            bLineSCylIntsP = intsPB1;
        else if (n2 != 0)
            bLineSCylIntsP = intsPB2;
        else
            bLineSCylIntsP = null;

        if ((n1 == 0) && (n2 == 0))
            return noIntersectionPoint();
        else if (((n1 == 0) && (n2 == 1)) || ((n1 == 1) && (n2 == 0))) {
            return oneIntersectionPoint();
        } else if (((n1 == 0) && (n2 == 2)) || ((n1 == 2) && (n2 == 0)))
            // 1 curve
            return NO_INTERSECT_LINE_INTERSECT;
        else
            throw new FatalException();
    }

    /**
     * �V�����_���m�̓Ք�Ŋ��S�Ɍ��邩?A�܂B�������Ȃ����𒲂ׂĕԂ�
     * <p/>
     * �V�����_���m�̓Ք�Ŋ��S�Ɍ��邩?A�܂B�������Ȃ����𒲂ׂ�?A
     * ���̒�?��l��Ă�?o�����ɕԋp����?B
     * </p>
     *
     * @return �V�����_���m��?�Ԃ�\��?�?�
     */
    private int noIntersectionPoint() {
        IntersectionPoint3D[] point;
        try {
            point = bCyl.intersect(sAxis);
        } catch (IndefiniteSolutionException e) {
            // �V�����_���m�̎�����?s�łȂ��̂�?A���̃P?[�X�͋N���蓾�Ȃ��͂�
            throw new FatalException();
        }
        if (point.length == 0)
            // nothing
            return NO_INTERSECT_OUT_OF_LINE;
        else
            // 2 freeform curve
            return NO_INTERSECT_BETWEEN_LINE;
    }

    /**
     * �V�����_���m���Ք��?�?G���邩?A�O����?�?G���邩�𒲂ׂĕԂ�
     * <p/>
     * �V�����_���m��?�?G�𒲂�?A����?�Ԃ�\����?��l��Ă�?o�����ɕԋp����?B
     * </p>
     *
     * @return �V�����_���m��?�Ԃ�\��?�?�
     */
    private int oneIntersectionPoint() {
        IntersectionPoint3D[] point;
        try {
            point = bCyl.intersect(sAxis);
        } catch (IndefiniteSolutionException e) {
            // �V�����_���m�̎�����?s�łȂ��̂�?A���̃P?[�X�͋N���蓾�Ȃ��͂�
            throw new FatalException();
        }
        if (point.length == 0)
            // 1 point
            return NO_INTERSECT_TOUCH_OUTSIDE;
        else
            // 8-figure curve(freeform curve)
            return NO_INTERSECT_TOUCH_INSIDE;
    }

    private int twoIntersectionPoint() {
        // 1 freeform curve
        return NO_INTERSECT_LINE_INTERSECT;
    }

    /**
     * �V�����_���m�̊֌W�𒲂ׂĕԂ�
     * <p/>
     * �V�����_���m����v���邩?A
     * �V�����_���m�̎�����?s��?A�����łȂ����𒲂�?A�����?ׂ���?�Ԃ𒲂ׂ�?B
     * </p>
     *
     * @return �V�����_���m��?�Ԃ�\��?�?�
     */
    private int getRelation() {
        // Cylinders are coincident
        if (bCyl.equals(sCyl)) return COINCIDENT;

        // Is Cylinders Parallel or not?
        if (bCyl.isParallel(sCyl)) {
            return getRelationInParallel();
        } else {
            return getRelationInNotParallel();
        }
    }

    /**
     * �V�����_���m�̌�?�(1 ��?�)��Ԃ�
     * <p/>
     * ���V�����_?�ɂ��B�?A���V�����_�̎��ƕ�?s�Ȓ�?��?�߂�?B
     * </p>
     *
     * @return ��?�̔z��
     */
    private SurfaceSurfaceInterference3D[] makeOneLine() {
        Vector3D bTos = bCylU.multiply(bCyl.radius());
        Point3D point = bOrg.add(bTos);
        Vector3D direction = bCylW;
        Line3D res = new Line3D(point, direction);
        IntersectionCurve3D ints = bCyl.curveToIntersectionCurve(res, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = {ints};
        return sol;
    }

    /**
     * �V�����_���m�̌�?�(2 ��?�)��Ԃ�
     * <p/>
     * �o��̃V�����_�̉~��?l��?A���̌�_��?�߂�?B
     * ���̌�_���痼�V�����_�̎�����?L�т���?�?�߂��?�ƂȂ�?B
     * </p>
     *
     * @return ��?�̔z��
     */
    private SurfaceSurfaceInterference3D[] makeTwoLine() {
        Circle3D bCylCircle = new Circle3D(bCyl.position(), bCyl.radius());
        IntersectionPoint3D[] intsP;
        try {
            intsP = bCylCircle.intersect(sCyl);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        if (intsP.length != 2) throw new FatalException();

        Line3D res = new Line3D(intsP[0], bCylW);
        IntersectionCurve3D ints1 = bCyl.curveToIntersectionCurve(res, sCyl, doExchange);
        res = new Line3D(intsP[1], bCylW);
        IntersectionCurve3D ints2 = bCyl.curveToIntersectionCurve(res, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = {ints1, ints2};
        return sol;
    }

    /**
     * �V�����_���m�̌�?�(2 �ȉ~)��Ԃ�
     * <p/>
     * �V�����_�̎����m�̌�_��2�ȉ~�̒�?S�ɂȂ�?B
     * �V�����_�̒Z�����ȉ~�̔��a�ɂȂ�?B
     * �ȉ~�̒����͎����m�����BĂ���p�x�Ȃǂ���?�߂���?B
     * </p>
     *
     * @return ��?�̔z��
     */
    private SurfaceSurfaceInterference3D[] makeTwoEllipse() {
        double theta = bAxis.angleWith(sAxis);
        Vector3D zOfBCyl = bAxis.dir();
        Vector3D zOfSCyl = sAxis.dir();
        Vector3D w1 = zOfSCyl.add(zOfBCyl).unitized();
        Vector3D w2 = zOfSCyl.subtract(zOfBCyl).unitized();

        // axisIntsP should be assign when intersection bAxis and sAxis
        if (axisIntsP == null) throw new FatalException();

        // use intersection point of two cylinder's z-axis as center of ellipse
        Axis2Placement3D position;
        double xRadius, yRadius;
        double bRadius = bCyl.radius();

        // make ellipse1
        position = new Axis2Placement3D(axisIntsP, w1, w2);
        xRadius = bRadius / Math.cos(theta / 2.0);
        yRadius = bRadius;
        Ellipse3D ellipse = new Ellipse3D(position, xRadius, yRadius);
        IntersectionCurve3D ints1 = bCyl.curveToIntersectionCurve(ellipse, sCyl, doExchange);

        // make ellipse2
        position = new Axis2Placement3D(axisIntsP, w2, w1);
        xRadius = bRadius / Math.sin(theta / 2.0);
        yRadius = bRadius;
        ellipse = new Ellipse3D(position, xRadius, yRadius);
        IntersectionCurve3D ints2 = bCyl.curveToIntersectionCurve(ellipse, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = {ints1, ints2};
        return sol;
    }

    private Point3D uvPointFromT(Point3D point, Vector3D uVec, Vector3D vVec, double r, double t) {
        double cost = Math.cos(t);
        double sint = Math.sin(t);

        return new CartesianPoint3D
                (point.x() + r * ((cost * uVec.x()) + (sint * vVec.x())),
                        point.y() + r * ((cost * uVec.y()) + (sint * vVec.y())),
                        point.z() + r * ((cost * uVec.z()) + (sint * vVec.z())));
    }

    private Vector3D[] getUVVector() {
        Vector3D cSu, cSv, cSw;
        PointOnCurve3D[] cNorm;
        try {
            cNorm = sAxis.commonNormal(bAxis);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        cSw = sAxis.dir();
        cSu = cNorm[1].subtract(cNorm[0]).unitized();
        if (cSu.norm() == 0) {
            cSu = sCyl.position().effectiveRefDirection();
        }
        cSv = cSw.crossProduct(cSu).unitized();
        Vector3D[] unitVec = {cSu, cSv, cSw};

        return unitVec;
    }

    /**
     * �V�����_���m�̌�?�(2 ���R��?�, 8�^��?�)��Ԃ�
     * <p/>
     * 2���R��?��?�߂�?B
     * �V�����_��?�񂩂���𗧂�?A���̉��p����?A
     * ���R��?�Ƃ��Ẵ|�����C���\���𓾂�?B
     * </p>
     *
     * @return ��?�̔z��
     */
    private SurfaceSurfaceInterference3D[] makeTwoCurve() {
        double step = 2 * Math.PI / (nst - 1);
        double[] dA = new double[3];
        Vector3D[] uvVec = getUVVector();
        Point3D ePnt;
        Vector3D evec;
        Vector3D evec2;
        double ework;
        double t;
        Point3D[] pArray1 = new Point3D[nst];
        Point3D[] pArray2 = new Point3D[nst];
        double dX0, dX1;
        double[] dX;

        dA[2] = 1.0 - w1w2 * w1w2;
        for (int i = 0; i < nst; i++) {
            t = -Math.PI + i * step;
            ePnt = uvPointFromT(sOrg, uvVec[0], uvVec[1], sCyl.radius(), t);
            evec = sAxis.dir().subtract(bAxis.dir().multiply(w1w2));
            evec2 = ePnt.subtract(bOrg);

            dA[1] = 2.0 * evec.dotProduct(evec2);
            ework = evec2.dotProduct(bAxis.dir());
            dA[0] = evec2.dotProduct(evec2)
                    - (ework * ework) - (bCyl.radius() * bCyl.radius());
            DoublePolynomial poly = new DoublePolynomial(dA);
            if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(poly)) == null) {
                throw new FatalException();
            }
            if (dX.length == 1)
                dX0 = dX1 = dX[0];
            else if (dX[0] < dX[1]) {
                dX0 = dX[1];
                dX1 = dX[0];
            } else {
                dX0 = dX[0];
                dX1 = dX[1];
            }
            pArray1[i] = ePnt.add(sAxis.dir().multiply(dX0));
            pArray2[i] = ePnt.add(sAxis.dir().multiply(dX1));
        }
        Polyline3D pol = new Polyline3D(pArray1);
        IntersectionCurve3D ints1 = bCyl.curveToIntersectionCurve(pol, sCyl, doExchange);
        pol = new Polyline3D(pArray2);
        IntersectionCurve3D ints2 = bCyl.curveToIntersectionCurve(pol, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = {ints1, ints2};
        return sol;
    }

    /**
     * �V�����_���m�̌�?�(1 �_)��Ԃ�
     *
     * @return ��_
     */
    private SurfaceSurfaceInterference3D[] makeOnePoint() {
        if (bLineSCylIntsP.length != 1) throw new FatalException();

        SurfaceSurfaceInterferenceList intf = new
                SurfaceSurfaceInterferenceList(bCyl, sCyl);

        Point3D point = bLineSCylIntsP[0].coordinates();
        double[] paramsA = bCyl.pointToParameter(point);
        double[] paramsB = sCyl.pointToParameter(point);
        intf.addAsIntersectionPoint(point, paramsA[0], paramsA[1], paramsB[0], paramsB[1]);
        return intf.toSurfaceSurfaceInterference3DArray(doExchange);
    }

    /**
     * �V�����_���m�̌�?�(8�^��?�)��Ԃ�
     * <p/>
     * makeTwoCurve()�?�\�b�h�Ɠ���
     * </p>
     *
     * @return ��?�̔z��
     */
    private SurfaceSurfaceInterference3D[] makeEightFigureCurve() {
        return makeTwoCurve();
    }

    /**
     * �V�����_���m�̌�?�(1 ���R��?�)��Ԃ�
     * <p/>
     * 1���R��?��?�߂�?B
     * �V�����_��?�񂩂���𗧂�?A���̉��p����?A
     * ���R��?�Ƃ��Ẵ|�����C���\���𓾂�?B
     * </p>
     *
     * @return ��?�̔z��
     */
    private SurfaceSurfaceInterference3D[] makeOneCurve() {
        int myNst = 2 * nst - 1;
        double[] dA = new double[3];
        Vector3D[] uvVec = getUVVector();
        Point3D ePnt;
        Vector3D evec;
        Vector3D evec2;
        double ework;
        double t;
        DoublePolynomial poly;
        double dX0, dX1;
        double dX[];
        Point3D[] pArray = new Point3D[myNst];

        evec = bLineSCylIntsP[0].subtract(sOrg);
        evec = evec.project(uvVec[2]).unitized();
        double t0 = Math.acos(evec.dotProduct(uvVec[0]));

        double step = (2.0 * t0) / (nst - 1);
        dA[2] = 1.0 - w1w2 * w1w2;

        for (int i = 0; i < (nst - 1); i++) {
            t = (-t0) + i * step;
            ePnt = uvPointFromT(sOrg, uvVec[0], uvVec[1], sCyl.radius(), t);
            evec = sAxis.dir().subtract(bAxis.dir().multiply(w1w2));
            evec2 = ePnt.subtract(bOrg);

            dA[1] = 2.0 * evec.dotProduct(evec2);
            ework = evec2.dotProduct(bAxis.dir());
            dA[0] = evec2.dotProduct(evec2)
                    - (ework * ework) - (bCyl.radius() * bCyl.radius());
            poly = new DoublePolynomial(dA);
            if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(poly)) == null) {
                throw new FatalException();
            }
            if (dX.length == 1)
                dX0 = dX1 = dX[0];
            else if (dX[0] < dX[1]) {
                dX0 = dX[1];
                dX1 = dX[0];
            } else {
                dX0 = dX[0];
                dX1 = dX[1];
            }
            pArray[i] = ePnt.add(sAxis.dir().multiply(dX0));
            pArray[myNst - 1 - i] = ePnt.add(sAxis.dir().multiply(dX1));
        }

        t = t0;
        ePnt = uvPointFromT(sOrg, uvVec[0], uvVec[1], sCyl.radius(), t);
        evec = sAxis.dir().subtract(bAxis.dir().multiply(w1w2));
        evec2 = ePnt.subtract(bOrg);

        dA[1] = 2.0 * evec.dotProduct(evec2);
        ework = evec2.dotProduct(bAxis.dir());
        dA[0] = evec2.dotProduct(evec2) - (ework * ework) - (bCyl.radius() * bCyl.radius());
        poly = new DoublePolynomial(dA);
        if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(poly)) == null) {
            throw new FatalException();
        }
        if (dX.length == 1)
            dX0 = dX1 = dX[0];
        else if (dX[0] < dX[1]) {
            dX0 = dX[1];
            dX1 = dX[0];
        } else {
            dX0 = dX[0];
            dX1 = dX[1];
        }
        pArray[nst - 1] = ePnt.add(sAxis.dir().multiply(dX0));
        Polyline3D pol = new Polyline3D(pArray);
        IntersectionCurve3D ints = bCyl.curveToIntersectionCurve(pol, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = {ints};
        return sol;
    }

    private SurfaceSurfaceInterference3D[] noIntersectionCurve() {
        return new SurfaceSurfaceInterference3D[0];
    }

    /**
     * �V�����_���m�̌�?��?�߂�
     * <p/>
     * 2�̃V�����_�̈ʒu�֌W����?�?�������?s��?A
     * ����?�?��ɂ������B���@��g�B�?A�����?�߂�?B
     * </p>
     *
     * @param cyl1       �V�����_1
     * @param cyl2       �V�����_2
     * @param doExchange �V�����_��귂��邩�ǂ����̃t���O
     * @return ��?�̔z��
     */
    static SurfaceSurfaceInterference3D[] intersection(CylindricalSurface3D cyl1, CylindricalSurface3D cyl2, boolean doExchange) throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] result;

        IntsCylCyl3D doObj = new IntsCylCyl3D(cyl1, cyl2, doExchange);
        switch (doObj.getRelation()) {
            case COINCIDENT:
                // throw IndefiniteSolutionException
                throw new IndefiniteSolutionException(cyl1);
            case PARALLEL_NO_TOUCH:
                // no intersection
                return doObj.noIntersectionCurve();
            case PARALLEL_TOUCH:
                // 1 line
                return doObj.makeOneLine();
            case PARALLEL_INTERSECT:
                // 2 line
                return doObj.makeTwoLine();
            case INTERSECT_SAME_RADIUS:
                // 2 ellipse
                return doObj.makeTwoEllipse();
            case NO_INTERSECT_OUT_OF_LINE:
                // no intersection
                return doObj.noIntersectionCurve();
            case NO_INTERSECT_BETWEEN_LINE:
                // 2 freeform curve
                return doObj.makeTwoCurve();
            case NO_INTERSECT_TOUCH_OUTSIDE:
                // 1 point
                return doObj.makeOnePoint();
            case NO_INTERSECT_TOUCH_INSIDE:
                // 8-figure curve
                return doObj.makeEightFigureCurve();
            case NO_INTERSECT_LINE_INTERSECT:
                // 1 freefrom curve
                return doObj.makeOneCurve();
        }
        throw new FatalException();
    }
}
