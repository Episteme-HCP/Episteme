/*
 * 3D ���Ɖ~���̌�?��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsSphCyl3D.java,v 1.3 2007-10-23 18:19:42 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;

import org.jscience.util.FatalException;


/**
 * 3D ���Ɖ~���̌�?��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:42 $
 */
class IntsSphCyl3D {
    /**
     * ��?��\������|�����C���̓_��?�
     */
    static final int nst = 41;

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param u DOCUMENT ME!
     * @param v DOCUMENT ME!
     * @param r DOCUMENT ME!
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static Point3D UVpntfromT(Point3D c, Vector3D u, Vector3D v, double r,
        double t) {
        double cost;
        double sint;

        cost = Math.cos(t);
        sint = Math.sin(t);

        return c.add(u.multiply(cost).add(v.multiply(sint)).multiply(r));
    }

    /**
     * ���Ɖ~���̌�?��?�߂�
     *
     * @param Sph ��
     * @param Cyl �~��
     * @param doExchange
     *        ���ʂ̋��Ɖ~����귂��邩�ǂ����̃t���O
     *
     * @return DOCUMENT ME!
     *
     * @throws FatalException DOCUMENT ME!
     *
     * @see SphericalSurface3D
     * @see CylindricalSurface3D
     * @see SurfaceSurfaceInterference3D
     */
    static SurfaceSurfaceInterference3D[] intersection(SphericalSurface3D Sph,
        CylindricalSurface3D Cyl, boolean doExchange) {
        Line3D C_axis = new Line3D(Cyl.position().location(), Cyl.position().z());
        Point3D projp = C_axis.project1From(Sph.position().location());
        double dist = projp.distance(Sph.position().location());
        double d_tol = Sph.getToleranceForDistance();

        if (dist < d_tol) {
            /*
             * ���̒�?S���~���̎�?�ɂ���
             */
            if (Math.abs(Sph.radius() - Cyl.radius()) < d_tol) {
                /*
                 * ���a�������� :
                 * ?ڂ���~�����
                 */
                Axis2Placement3D a2p = new Axis2Placement3D(Sph.position()
                                                               .location(),
                        Cyl.position().z(), Cyl.position().x());
                Circle3D res = new Circle3D(a2p,
                        (Cyl.radius() + Sph.radius()) / 2.0);
                IntersectionCurve3D ints = Sph.curveToIntersectionCurve(res,
                        Cyl, doExchange);
                SurfaceSurfaceInterference3D[] sol = { ints };

                return sol;
            }

            if (Sph.radius() > Cyl.radius()) {
                /*
                 * ���̔��a���~���̔��a���傫�� :
                 * �~�����
                 */
                double r = Math.sqrt((Sph.radius() * Sph.radius()) -
                        (Cyl.radius() * Cyl.radius()));
                Vector3D offset = Cyl.position().z().multiply(r);

                Axis2Placement3D a2p = new Axis2Placement3D(Sph.position()
                                                               .location()
                                                               .add(offset),
                        Cyl.position().z(), Cyl.position().x());
                Circle3D res = new Circle3D(a2p, Cyl.radius());
                IntersectionCurve3D ints1 = Sph.curveToIntersectionCurve(res,
                        Cyl, doExchange);

                a2p = new Axis2Placement3D(Sph.position().location()
                                              .subtract(offset),
                        Cyl.position().z(), Cyl.position().x());
                res = new Circle3D(a2p, Cyl.radius());

                IntersectionCurve3D ints2 = Sph.curveToIntersectionCurve(res,
                        Cyl, doExchange);

                SurfaceSurfaceInterference3D[] sol = { ints1, ints2 };

                return sol;
            }

            /*
             * ���̔��a���~���̔��a���?����� :
             * ����Ȃ�
             */
            return new SurfaceSurfaceInterference3D[0];
        }

        /*
         * ���̒�?S�͉~���̎�?�ɂ͂Ȃ�
         */
        Vector3D Cu; // new local axis

        /*
         * ���̒�?S�͉~���̎�?�ɂ͂Ȃ�
         */
        Vector3D Cv; // new local axis

        /*
         * ���̒�?S�͉~���̎�?�ɂ͂Ȃ�
         */
        Vector3D Cw; // new local axis

        Cw = Cyl.position().z();
        Cu = Sph.position().location().subtract(Cyl.position().location());
        Cv = Cw.crossProduct(Cu);
        Cu = Cv.crossProduct(Cw).unitized();
        Cv = Cv.unitized();

        if ((Math.abs(dist - (Cyl.radius() + Sph.radius())) < d_tol) ||
                (Math.abs(dist - (Cyl.radius() - Sph.radius())) < d_tol)) {
            /*
             * �~���ɊO?�/��?ڂ��鋅 :
             * 1�_��?ڂ���
             */
            Point3D res;

            if (Math.abs(dist - (Cyl.radius() + Sph.radius())) < d_tol) {
                res = Sph.position().location()
                         .subtract(Cu.multiply(Sph.radius()));
            } else {
                res = Sph.position().location().add(Cu.multiply(Sph.radius()));
            }

            IntersectionPoint3D intsPnt = Sph.pointToIntersectionPoint(res,
                    Cyl, doExchange);
            SurfaceSurfaceInterference3D[] intf = { intsPnt };

            return intf;
        }

        if ((Math.abs(dist - (Sph.radius() - Cyl.radius())) < d_tol) ||
                (dist < (Sph.radius() - Cyl.radius()))) {
            /*
             * 2��?�Ō��� :
             * 8�̎���?�
             *   �µ����
             * ��?X��2��?�
             */
            Point3D[] pnts1;

            /*
             * 2��?�Ō��� :
             * 8�̎���?�
             *   �µ����
             * ��?X��2��?�
             */
            Point3D[] pnts2;
            double[] dA = new double[3]; // coefficients of polynomial
            DoublePolynomial pol;
            double[] dX; // roots of polynomial
            double dX0;
            double dX1;
            double t; // first parameter of Cylinder
            double step; // delta t
            Point3D UVp; // UV point of t
            Vector3D evec; // work area

            pnts1 = new Point3D[nst];
            pnts2 = new Point3D[nst];

            step = GeometryUtils.PI2 / (nst - 1);
            evec = Cyl.position().location().subtract(Sph.position().location());
            dA[2] = 1.0;
            dA[1] = 2.0 * evec.dotProduct(Cw);

            for (int i = 0; i < nst; i++) {
                t = (-Math.PI) + (step * i);
                UVp = UVpntfromT(Cyl.position().location(), Cu, Cv,
                        Cyl.radius(), t);
                evec = UVp.subtract(Sph.position().location());
                dA[0] = evec.dotProduct(evec) - (Sph.radius() * Sph.radius());
                pol = new DoublePolynomial(dA);

                if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(pol)) == null) {
                    throw new FatalException();
                }

                if (dX.length == 1) {
                    dX0 = dX1 = dX[0];
                } else if (dX[0] < dX[1]) {
                    dX0 = dX[1];
                    dX1 = dX[0];
                } else {
                    dX0 = dX[0];
                    dX1 = dX[1];
                }

                pnts1[i] = UVp.add(Cw.multiply(dX0));
                pnts2[i] = UVp.add(Cw.multiply(dX1));
            }

            Polyline3D res = new Polyline3D(pnts1);
            IntersectionCurve3D ints1 = Sph.curveToIntersectionCurve(res, Cyl,
                    doExchange);
            res = new Polyline3D(pnts2);

            IntersectionCurve3D ints2 = Sph.curveToIntersectionCurve(res, Cyl,
                    doExchange);
            SurfaceSurfaceInterference3D[] sol = { ints1, ints2 };

            return sol;
        }

        if ((dist < (Cyl.radius() - Sph.radius())) ||
                (dist > (Cyl.radius() + Sph.radius()))) {
            /*
             * ���͉~���̊O���ɂ��邩?A�Ք�ɂ��� :
             * ����Ȃ�
             */
            return new SurfaceSurfaceInterference3D[0];
        }

        /*
         * �~���Ƌ���1��?�Ō���
         */
        Point3D[] pnts;
        int my_nst;
        IntersectionPoint3D[] CCint;
        Vector3D evec;
        double t0;

        double[] dA = new double[3]; // coefficients of polynomial
        DoublePolynomial pol;
        double[] dX; // roots of polynomial
        double dX0;
        double dX1;
        double t; // first parameter of Cylinder
        double step; // delta t
        Point3D UVp; // UV point of t

        my_nst = (2 * nst) - 1;
        pnts = new Point3D[my_nst];

        /*
         * parametric range [-t0, t0]
         */
        Axis2Placement3D a2p = new Axis2Placement3D(Sph.position().location(),
                Cyl.position().z(), Cyl.position().x());
        Circle3D Cir = new Circle3D(a2p, Sph.radius());

        try {
            CCint = Cir.intersect(Cyl);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        evec = CCint[0].subtract(Cyl.position().location());
        evec = evec.project(Cw).unitized();
        t0 = Math.acos(evec.dotProduct(Cu));

        /*
         * solution
         */
        step = (2.0 * t0) / (nst - 1);
        evec = Cyl.position().location().subtract(Sph.position().location());
        dA[2] = 1.0;
        dA[1] = 2.0 * evec.dotProduct(Cw);

        t = (-t0);
        UVp = UVpntfromT(Cyl.position().location(), Cu, Cv, Cyl.radius(), t);
        evec = UVp.subtract(Sph.position().location());
        dA[0] = evec.dotProduct(evec) - (Sph.radius() * Sph.radius());
        pol = new DoublePolynomial(dA);

        if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(pol)) == null) {
            throw new FatalException();
        }

        pnts[0] = pnts[my_nst - 1] = UVp.add(Cw.multiply(dX[0]));

        for (int i = 1; i < (nst - 1); i++) {
            t = (-t0) + (i * step);
            UVp = UVpntfromT(Cyl.position().location(), Cu, Cv, Cyl.radius(), t);
            evec = UVp.subtract(Sph.position().location());
            dA[0] = evec.dotProduct(evec) - (Sph.radius() * Sph.radius());
            pol = new DoublePolynomial(dA);

            if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(pol)) == null) {
                throw new FatalException();
            }

            if (dX[0] < dX[1]) {
                dX0 = dX[1];
                dX1 = dX[0];
            } else {
                dX0 = dX[0];
                dX1 = dX[1];
            }

            pnts[i] = UVp.add(Cw.multiply(dX0));
            pnts[my_nst - 1 - i] = UVp.add(Cw.multiply(dX1));
        }

        t = t0;
        UVp = UVpntfromT(Cyl.position().location(), Cu, Cv, Cyl.radius(), t);
        evec = UVp.subtract(Sph.position().location());
        dA[0] = evec.dotProduct(evec) - (Sph.radius() * Sph.radius());
        pol = new DoublePolynomial(dA);

        if ((dX = GeometryPrivateUtils.getAlwaysRootsIfQuadric(pol)) == null) {
            throw new FatalException();
        }

        pnts[nst - 1] = UVp.add(Cw.multiply(dX[0]));

        Polyline3D res = new Polyline3D(pnts);
        IntersectionCurve3D ints = Sph.curveToIntersectionCurve(res, Cyl,
                doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints };

        return sol;
    }
}
