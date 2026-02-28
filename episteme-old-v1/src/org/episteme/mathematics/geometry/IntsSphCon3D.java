/*
 * 3D ���Ɖ~??�̌�?��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsSphCon3D.java,v 1.6 2006/05/20 23:25:49 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.analysis.polynomials.DoublePolynomial;
import org.episteme.util.FatalException;

/**
 * 3D ���Ɖ~??�̌�?��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.6 $, $Date: 2006/05/20 23:25:49 $
 */
class IntsSphCon3D {
    /**
     * �f�o�O�t���O(��?��false)
     */
    private static final boolean debug = false;

    /**
     * ��?��\������|�����C���̓_��?�
     */
    private static final int nst = 41;

    /**
     * ��
     *
     * @see SphericalSurface3D
     */
    private final SphericalSurface3D Sph;

    /**
     * �~??
     *
     * @see ConicalSurface3D
     */
    private final ConicalSurface3D Con;

    private double d_tol;
    private double a_tol;

    private double C_cos, C_sin, C_tan;    // cos, sin & tan(Con.semiAngle)

    private Point3D C_org;        // Cone's local coordinates system (Corg is vertex)
    private Vector3D[] C_xyz;

    private Vector3D C2S;        // vector from vertex of Cone to center of Sphere
    private Vector3D S2C;        // opposite C2S
    private double distC2S;        // distance from center of Sphere to vetex of Cone

    /**
     * �R���X�g���N�^
     *
     * @param Sph        ��
     * @param Con        �~??
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see SphericalSurface3D
     * @see ConicalSurface3D
     */
    IntsSphCon3D(SphericalSurface3D Sph, ConicalSurface3D Con) {
        super();
        this.Sph = Sph;
        this.Con = Con;

        d_tol = Sph.getToleranceForDistance();
        a_tol = Sph.getToleranceForAngle();

        C_cos = Math.cos(Con.semiAngle());
        C_sin = Math.sin(Con.semiAngle());
        C_tan = Math.tan(Con.semiAngle());
        C_org = Con.position().location();
        C_xyz = Con.position().axes();
        C_org = C_org.subtract(C_xyz[2].multiply(Con.radius() / C_tan));

        C2S = Sph.position().location().subtract(C_org);
        S2C = C2S.reverse();
        distC2S = C2S.length();
    }

    private static Vector3D UVWpntfromT(Vector3D u, Vector3D v, Vector3D w,
                                        double Ctan, double t) {
        double cost, sint;

        cost = Math.cos(t);
        sint = Math.sin(t);

        return u.multiply(cost).add(v.multiply(sint)).multiply(Ctan).add(w);
    }

    /**
     * �~�ƂȂ��?��?�?�����
     *
     * @param loc        �~�̒�?S
     * @param axis       �~��?�镽�ʂ̖@?�
     * @param refDir     �~�̋�?�?W�n��X�����
     * @param radius     �~�̔��a
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see Point3D
     * @see Vector3D
     * @see IntersectionCurve3D
     */
    private IntersectionCurve3D makeCircle(Point3D loc, Vector3D axis,
                                           Vector3D refDir, double radius,
                                           boolean doExchange) {
        Axis2Placement3D a2p = new Axis2Placement3D(loc, axis, refDir);
        Circle3D resC = new Circle3D(a2p, radius);
        return Sph.curveToIntersectionCurve(resC, Con, doExchange);
    }

    /**
     * �|�����C���ƂȂ��?��?�?�����
     *
     * @param t0         �|�����C���̊J�n�_��\���p���??[�^
     * @param nst        �|�����C����?ړ_��?�(����?�)
     * @param Cu         �~���̋�?�U��(�����狅�̒�?S�Ɍ���)
     * @param Cv         �~���̋�?�V��(CW��Cu�̊O?ϕ��)
     * @param Cw         �~���̋�?�W��(�����)
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see Vector3D
     * @see IntersectionCurve3D
     */
    private IntersectionCurve3D makePolyline(double t0, int nst,
                                             Vector3D Cu, Vector3D Cv, Vector3D Cw,
                                             boolean doExchange) {
        Point3D[] pnts = new Point3D[nst];
        double step;
        double a, b;    // coefficients
        double t, s;
        Vector3D evec;

        step = (2.0 * t0) / (nst - 1);
        a = C_tan * C_tan + 1.0;
        for (int i = 0; i < nst; i++) {
            t = (-t0) + i * step;
            evec = UVWpntfromT(Cu, Cv, Cw, C_tan, t);
            b = 2.0 * evec.dotProduct(S2C);
            s = (-b / a);
            pnts[i] = C_org.add(evec.multiply(s));
        }

        Polyline3D res = new Polyline3D(pnts);
        return Sph.curveToIntersectionCurve(res, Con, doExchange);
    }

    /**
     * 2�|�����C���ƂȂ��?��?�?�����
     *
     * @param nst        �|�����C����?ړ_��?�(����?�)
     * @param Cu         �~���̋�?�U��(�����狅�̒�?S�Ɍ���)
     * @param Cv         �~���̋�?�V��(CW��Cu�̊O?ϕ��)
     * @param Cw         �~���̋�?�W��(�����)
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see Vector3D
     * @see SurfaceSurfaceInterference3D
     */
    private SurfaceSurfaceInterference3D[]
    makeTwoPolyline(int nst,
                    Vector3D Cu, Vector3D Cv, Vector3D Cw,
                    boolean doExchange) {
        double step;
        DoublePolynomial pol;
        double[] dA = new double[3];    // coefficients
        double[] dX;            // roots
        double dX0, dX1;
        int icnt;
        double t;
        Vector3D evec;

        Point3D[] pnts1 = new Point3D[nst];
        Point3D[] pnts2 = new Point3D[nst];

        step = GeometryUtils.PI2 / (nst - 1);
        dA[2] = C_tan * C_tan + 1.0;
        dA[0] = distC2S * distC2S - (Sph.radius() * Sph.radius());
        for (int i = 0; i < nst; i++) {
            t = (-Math.PI) + i * step;
            evec = UVWpntfromT(Cu, Cv, Cw, C_tan, t);
            dA[1] = 2.0 * evec.dotProduct(S2C);
            pol = new DoublePolynomial(dA);
            if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(pol)) == null)
                throw new FatalException();
            if (dX.length == 1) {
                dX0 = dX1 = dX[0];
            } else if (dX[0] > dX[1]) {
                dX0 = dX[0];
                dX1 = dX[1];
            } else {
                dX0 = dX[1];
                dX1 = dX[0];
            }
            pnts1[i] = C_org.add(evec.multiply(dX0));
            pnts2[i] = C_org.add(evec.multiply(dX1));
        }

        Polyline3D res = new Polyline3D(pnts1);
        IntersectionCurve3D ints1 = Sph.curveToIntersectionCurve(res, Con, doExchange);
        res = new Polyline3D(pnts2);
        IntersectionCurve3D ints2 = Sph.curveToIntersectionCurve(res, Con, doExchange);
        SurfaceSurfaceInterference3D[] sol = {ints1, ints2};
        return sol;
    }

    /**
     * �|�����C���ƂȂ��?��~??�̈��̑���?�?�����
     *
     * @param nst        �|�����C����?ړ_��?�(����?�)
     * @param Cu         �~���̋�?�U��(�����狅�̒�?S�Ɍ���)
     * @param Cv         �~���̋�?�V��(CW��Cu�̊O?ϕ��)
     * @param Cw         �~���̋�?�W��(�����)
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see Vector3D
     * @see IntersectionCurve3D
     */
    private IntersectionCurve3D
    makePolylineAtOneSide(int nst,
                          Vector3D Cu, Vector3D Cv, Vector3D Cw,
                          boolean doExchange) {
        IntersectionPoint3D[] CSint;
        Vector3D evec;
        double ework;
        double t0;
        double step;
        DoublePolynomial pol;
        double[] dA = new double[3];    // coefficients
        double[] dX;            // roots
        double dX0, dX1;
        double t;

        int my_nst = 2 * nst - 1;
        Point3D[] pnts = new Point3D[my_nst];

        /*
        * parametric range [-t0, t0]
        */
        ework = Math.sqrt((distC2S * distC2S) - (Sph.radius() * Sph.radius()));
        Point3D loc = C_org.add(Cw.multiply(ework * C_cos));
        Axis2Placement3D a2p
                = new Axis2Placement3D(loc, Con.position().z(), Con.position().x());
        Circle3D Cir = new Circle3D(a2p, ework * C_sin);
        try {
            CSint = Cir.intersect(Sph);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }
        if (CSint.length < 1) {
            if (debug) {
                Cir.output(System.out);
                Sph.output(System.out);
            }
            throw new FatalException();
        }
        evec = CSint[0].subtract(Cir.position().location()).unitized();
        t0 = Math.acos(Cu.dotProduct(evec));

        step = (2.0 * t0) / (nst - 1);
        dA[2] = C_tan * C_tan + 1.0;
        dA[0] = distC2S * distC2S - (Sph.radius() * Sph.radius());
        for (int i = 0; i < (nst - 1); i++) {
            t = (-t0) + i * step;
            evec = UVWpntfromT(Cu, Cv, Cw, C_tan, t);
            dA[1] = 2.0 * evec.dotProduct(S2C);
            pol = new DoublePolynomial(dA);
            if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(pol)) == null)
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
            pnts[i] = C_org.add(evec.multiply(dX0));
            pnts[my_nst - 1 - i] = C_org.add(evec.multiply(dX1));
        }

        evec = UVWpntfromT(Cu, Cv, Cw, C_tan, t0);
        dA[1] = 2.0 * evec.dotProduct(S2C);
        pol = new DoublePolynomial(dA);
        if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(pol)) == null)
            throw new FatalException();
        if ((dX.length == 2) && (dX[0] < dX[1]))
            dX[0] = dX[1];

        pnts[nst - 1] = C_org.add(evec.multiply(dX[0]));

        Polyline3D res = new Polyline3D(pnts);
        return Sph.curveToIntersectionCurve(res, Con, doExchange);
    }

    /**
     * ���̒�?S���~??�̎�?�ɂ���?A�~??�̒��_������?�BĂ��� :
     * �_�Ɖ~�ɂȂ�
     *
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see SurfaceSurfaceInterference3D
     */
    private SurfaceSurfaceInterference3D[] oneCircleOnePoint(boolean doExchange) {
        IntersectionPoint3D intsP
                = Sph.pointToIntersectionPoint(C_org, Con, doExchange);

        double ework = 2.0 * Sph.radius() * C_cos * C_cos;
        if (C2S.dotProduct(C_xyz[2]) < 0.0) ework *= -1.0;
        IntersectionCurve3D intsC
                = makeCircle(C_org.add(C_xyz[2].multiply(ework)), C_xyz[2], C_xyz[0],
                2.0 * Sph.radius() * C_cos * C_sin, doExchange);

        SurfaceSurfaceInterference3D[] sol = {intsP, intsC};
        return sol;
    }

    /**
     * ���̒�?S���~??�̎�?�ɂ���?A�~??�̒��_�����̓Ք�ɂ��� :
     * �~??�̂��ꂼ��̑��ɉ~���ł���
     *
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see SurfaceSurfaceInterference3D
     */
    private SurfaceSurfaceInterference3D[]
    twoCircleAtEachSide(boolean doExchange) {
        SurfaceSurfaceInterference3D[] sol = new SurfaceSurfaceInterference3D[2];
        double ework;
        double side = C2S.dotProduct(C_xyz[2]);

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                ework = (-distC2S * C_cos) +
                        Math.sqrt((Sph.radius() * Sph.radius()) - (distC2S * distC2S) * (C_sin * C_sin));
                if (side > 0.0)
                    ework *= -1.0;
            } else {
                ework = (distC2S * C_cos) +
                        Math.sqrt((Sph.radius() * Sph.radius()) - (distC2S * distC2S) * (C_sin * C_sin));
                if (side < 0.0)
                    ework *= -1.0;
            }

            sol[i] = makeCircle(C_org.add(C_xyz[2].multiply(ework * C_cos)),
                    C_xyz[2], C_xyz[0], Math.abs(ework * C_sin), doExchange);
        }
        return sol;
    }

    /**
     * ���̒�?S���~??�̎�?�ɂ���?A���Ɖ~??��?ڂ��� :
     * �~�����?�?������
     *
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see SurfaceSurfaceInterference3D
     */
    private SurfaceSurfaceInterference3D[] oneTangentCircle(boolean doExchange) {
        double ework = Sph.radius() * C_sin;
        if (C2S.dotProduct(C_xyz[2]) < 0.0)
            ework *= -1.0;
        IntersectionCurve3D intsC
                = makeCircle(Sph.position().location().subtract(C_xyz[2].multiply(ework)),
                C_xyz[2], C_xyz[0], Sph.radius() * C_cos, doExchange);
        SurfaceSurfaceInterference3D[] sol = {intsC};
        return sol;
    }

    /**
     * ���̒�?S���~??�̎�?�ɂ���?A�~??�̒��_�����̊O���ɂ��� :
     * �~??�̈��̑��ɉ~����ł���
     *
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see SurfaceSurfaceInterference3D
     */
    private SurfaceSurfaceInterference3D[]
    twoCircleAtOneSide(boolean doExchange) {
        double ework = Math.sqrt((Sph.radius() * Sph.radius()) - (distC2S * distC2S) * (C_sin * C_sin));
        double x1 = distC2S * C_cos + ework;
        double x2 = distC2S * C_cos - ework;
        if (C2S.dotProduct(C_xyz[2]) < 0.0) {
            x1 *= -1.0;
            x2 *= -1.0;
        }

        IntersectionCurve3D intsC1
                = makeCircle(C_org.add(C_xyz[2].multiply(x1 * C_cos)),
                C_xyz[2], C_xyz[0], Math.abs(x1 * C_sin), doExchange);

        IntersectionCurve3D intsC2
                = makeCircle(C_org.add(C_xyz[2].multiply(x2 * C_cos)),
                C_xyz[2], C_xyz[0], Math.abs(x2 * C_sin), doExchange);

        SurfaceSurfaceInterference3D[] sol = {intsC1, intsC2};
        return sol;
    }

    /**
     * �~??�̒��_������?�BĂ���?�?�
     *
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see SurfaceSurfaceInterference3D
     */
    private SurfaceSurfaceInterference3D[]
    vertexOfConeIsOnSphere(boolean doExchange) {
        Vector3D Cu, Cv, Cw;    // new local axis
        Vector3D CuR, CvR, CwR;    // reversed;
        double cosd;            // dot-product(C2S, Cw) / distC2S
        double acosd;
        boolean revSide;

        Cw = C_xyz[2];
        Cv = Cw.crossProduct(C2S);
        Cu = Cv.crossProduct(Cw);
        Cu = Cu.unitized();
        Cv = Cv.unitized();

        CuR = Cu;
        CvR = Cv.reverse();
        CwR = Cw.reverse();

        cosd = C2S.dotProduct(Cw) / distC2S;
        if (cosd < 0.0) {
            revSide = true;
            cosd *= -1.0;
        } else
            revSide = false;
        if (cosd > 1.0) cosd = 1.0;
        acosd = Math.acos(cosd);

        if (Math.abs(acosd - ((Math.PI / 2) - Con.semiAngle())) < a_tol) {
            /*
            * �~??�̒��_��܂ދ�?���
            */
            IntersectionCurve3D ints;
            if (!revSide)
                ints = makePolyline(Math.PI, nst, Cu, Cv, Cw, doExchange);
            else
                ints = makePolyline(Math.PI, nst, CuR, CvR, CwR, doExchange);
            SurfaceSurfaceInterference3D[] sol = {ints};
            return sol;
        }

        if (acosd < ((Math.PI / 2) - Con.semiAngle())) {
            /*
            * �~??�̒��_��?A��?���
            */
            IntersectionCurve3D intsC;
            if (!revSide)
                intsC = makePolyline(Math.PI, nst, Cu, Cv, Cw, doExchange);
            else
                intsC = makePolyline(Math.PI, nst, CuR, CvR, CwR, doExchange);

            IntersectionPoint3D intsP
                    = Sph.pointToIntersectionPoint(C_org, Con, doExchange);

            SurfaceSurfaceInterference3D[] sol = {intsC, intsP};
            return sol;
        }

        /*
        * 8�̎���?��`��
        */
        double t0;    // the starting point

        if (Math.abs(acosd - (Math.PI / 2.0)) < a_tol)
            t0 = Math.PI / 2.0;
        else
            t0 = Math.acos(-(1.0 / Math.tan(acosd)) / C_tan);

        IntersectionCurve3D ints1
                = makePolyline(t0, nst, Cu, Cv, Cw, doExchange);

        IntersectionCurve3D ints2
                = makePolyline(t0, nst, CuR, CvR, CwR, doExchange);

        SurfaceSurfaceInterference3D[] sol = {ints1, ints2};
        return sol;
    }

    /**
     * �~??�̒��_�����̓Ք�ɑ�?݂���?�?� :
     * �|�����C����2��
     *
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see SurfaceSurfaceInterference3D
     */
    private SurfaceSurfaceInterference3D[]
    vertexOfConeIsInSphere(boolean doExchange) {
        Vector3D Cu, Cv, Cw;        // new local axis

        Cw = C_xyz[2];
        Cv = Cw.crossProduct(C2S);
        Cu = Cv.crossProduct(Cw);
        Cu = Cu.unitized();
        Cv = Cv.unitized();

        if (C2S.dotProduct(Cw) < 0.0) {
            Cu = Cu;
            Cv = Cv.reverse();
            Cw = Cw.reverse();
        }

        return makeTwoPolyline(nst, Cu, Cv, Cw, doExchange);
    }

    /**
     * �~??�̒��_�����̊O���ɑ�?݂���?�?�
     *
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see SurfaceSurfaceInterference3D
     */
    private SurfaceSurfaceInterference3D[]
    vertexOfConeIsNotInSphere(boolean doExchange) {
        Vector3D Cu, Cv, Cw;    // new local axis
        Vector3D CuR, CvR, CwR;    // reversed;
        double cosd;            // dot-product(C2S, Cw) / distC2S
        double alpha, theta, phi;
        boolean revSide;

        Cw = C_xyz[2];
        Cv = Cw.crossProduct(C2S);
        Cu = Cv.crossProduct(Cw);
        Cu = Cu.unitized();
        Cv = Cv.unitized();

        CuR = Cu;
        CvR = Cv.reverse();
        CwR = Cw.reverse();

        cosd = C2S.dotProduct(Cw) / distC2S;
        if (cosd < 0.0) {
            revSide = true;
            cosd *= -1.0;
        } else
            revSide = false;
        if (cosd > 1.0) cosd = 1.0;

        alpha = Con.semiAngle();
        theta = Math.asin(Sph.radius() / distC2S);
        phi = Math.acos(cosd);

        if ((Math.abs(phi - (alpha + theta)) < a_tol) ||
                (Math.abs(phi - (alpha - theta)) < a_tol)) {
            /*
            * 1�_�µ����2�_�ŊO?ڂ���
            */
            if (revSide) {
                Cu = CuR;
                Cv = CvR;
                Cw = CwR;
            }

            double ework = Math.sqrt(distC2S * distC2S - Sph.radius() * Sph.radius());

            Point3D resP = C_org.add(Cw.multiply(ework * C_cos));
            resP = resP.add(Cu.multiply(ework * C_sin));
            IntersectionPoint3D intsP = Sph.pointToIntersectionPoint(resP, Con, doExchange);

            if ((Math.abs(phi - (alpha + theta)) < a_tol) &&
                    (Math.abs(phi - Math.PI / 2.0) < a_tol)) {
                /*
                * 2 points
                */
                resP = C_org.subtract(Cw.multiply(ework * C_cos));
                resP = resP.add(Cu.multiply(ework * C_sin));
                IntersectionPoint3D intsP2
                        = Sph.pointToIntersectionPoint(resP, Con, doExchange);
                SurfaceSurfaceInterference3D[] intf = {intsP, intsP2};
                return intf;
            }
            SurfaceSurfaceInterference3D[] intf = {intsP};
            return intf;
        }

        if ((phi > (alpha + theta)) || (phi < (alpha - theta))) {
            /*
            * ��?�͑�?݂��Ȃ�
            */
            return new SurfaceSurfaceInterference3D[0];
        }

        if ((Math.abs(theta - (alpha + phi)) < a_tol) ||
                (theta > (alpha + phi))) {
            /*
            * an 8 figure curve
            *   or
            * 2 curves
            */
            if (revSide) {
                Cu = CuR;
                Cv = CvR;
                Cw = CwR;
            }
            return makeTwoPolyline(nst, Cu, Cv, Cw, doExchange);
        }

        /*
        * 1 curve
        *   or
        * 1 curve + 1 more point at another side
        *   or
        * 1 curve + 1 more curve at another side
        */

        /*
        * �܂��~??�̈��̑��ɋ�?���
        */
        IntersectionCurve3D ints;
        if (!revSide)
            ints = makePolylineAtOneSide(nst, Cu, Cv, Cw, doExchange);
        else
            ints = makePolylineAtOneSide(nst, CuR, CvR, CwR, doExchange);

        if (Math.abs(Math.PI - (theta + phi + alpha)) < a_tol) {
            /*
            * ����ɉ~??�̑���̑���1�_�ŊO?�
            */
            if (!revSide) {
                Cu = CuR;
                Cv = CvR;
                Cw = CwR;
            }

            double ework = Math.sqrt(distC2S * distC2S - Sph.radius() * Sph.radius());

            Point3D resP = C_org.add(Cw.multiply(ework * C_cos));
            resP = resP.add(Cu.multiply(ework * C_sin));
            IntersectionPoint3D intsP = Sph.pointToIntersectionPoint(resP, Con, doExchange);

            SurfaceSurfaceInterference3D[] sol = {ints, intsP};
            return sol;
        }
        if (theta + phi + alpha > Math.PI) {
            /*
            * ����ɉ~??�̑���̑��ɂ��?�
            */
            if (!revSide) {
                Cu = CuR;
                Cv = CvR;
                Cw = CwR;
            }
            IntersectionCurve3D ints2 = makePolylineAtOneSide(nst, Cu, Cv, Cw, doExchange);

            SurfaceSurfaceInterference3D[] sol = {ints, ints2};
            return sol;
        }

        SurfaceSurfaceInterference3D[] sol = {ints};
        return sol;
    }

    /**
     * ��?��?�߂�
     *
     * @param doExchange ���ʂ̋��Ɖ~??��귂��邩�ǂ����̃t���O
     * @see SurfaceSurfaceInterference3D
     */
    SurfaceSurfaceInterference3D[]
    getInterference(boolean doExchange) {
        SurfaceSurfaceInterference3D[] intf;
        Line3D C_axis = new Line3D(C_org, C_xyz[2]);    // axis of Cone
        Point3D projp = C_axis.project1From(Sph.position().location());

        if (projp.distance(Sph.position().location()) < d_tol) {
            /*
            * ���̒�?S���~??�̎�?�ɂ��� :
            * ��͓_���~�ŕ\�������
            */

            /*
            * �~??�̒��_������?�BĂ��� :
            * �_�Ɖ~�ɂȂ�
            */
            if (Math.abs(distC2S - Sph.radius()) < d_tol) {
                return oneCircleOnePoint(doExchange);
            }

            /*
            * �~??�̒��_�����̓Ք�ɂ��� :
            * �~??�̂��ꂼ��̑��ɉ~���ł���
            */
            if (distC2S < Sph.radius()) {
                return twoCircleAtEachSide(doExchange);
            }

            /*
            * ���Ɖ~??��?ڂ��� :
            * �~�����?�?������
            */
            if (Math.abs(distC2S - (Sph.radius() / C_sin)) < d_tol) {
                return oneTangentCircle(doExchange);
            }

            /*
            * �~??�̒��_�����̊O���ɂ��� :
            * �~??�̈��̑��ɉ~����ł���
            */
            if (distC2S < (Sph.radius() / C_sin)) {
                return twoCircleAtOneSide(doExchange);
            }

            /*
            * �~??�ɓՔ�ɋ�����?݂���
            * ��?�͑�?݂��Ȃ�
            */
            return new SurfaceSurfaceInterference3D[0];
        }

        /*
        * �~??�̒��_������?�BĂ���?�?�
        */
        if (Math.abs(distC2S - Sph.radius()) < d_tol) {
            return vertexOfConeIsOnSphere(doExchange);
        }

        /*
        * �~??�̒��_�����̓Ք�ɑ�?݂���?�?�
        */
        if (distC2S < Sph.radius()) {
            return vertexOfConeIsInSphere(doExchange);
        }

        /*
        * �~??�̒��_�����̊O���ɑ�?݂���?�?�
        */
        return vertexOfConeIsNotInSphere(doExchange);
    }
}
