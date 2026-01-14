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

/**
 * 3D ���ʂƉ~??�ʂ̌�?��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:42 $
 */
class IntsPlnCon3D {
    /** DOCUMENT ME! */
    private Plane3D dA;

    /** DOCUMENT ME! */
    private ConicalSurface3D dB;

    /** DOCUMENT ME! */
    private Point3D Bv; /* the vertex of B */

    /** DOCUMENT ME! */
    private Point3D pBv2A; /* projected Bv to A */

    /** DOCUMENT ME! */
    private Vector3D eB2A; /* vector from Bv to pBv2A */

    /** DOCUMENT ME! */
    private Vector3D pBz2A; /* projected B's z to A */

    /** DOCUMENT ME! */
    private double edot; /* dot_product(A's z, B's z) */

    /** DOCUMENT ME! */
    private double eBdot; /* dot_product(B's z, pBz2A) */

    /** DOCUMENT ME! */
    private Vector3D ecrs; /* cross_product(A's z, B's z) */

    /** DOCUMENT ME! */
    private double sinBsa; /* sin(B's semi angle) */

    /** DOCUMENT ME! */
    private double tanBsa; /* tan(B's semi angle) */

    /** DOCUMENT ME! */
    private double a_tol;

/**
     * Creates a new IntsPlnCon3D object.
     *
     * @param dA DOCUMENT ME!
     * @param dB DOCUMENT ME!
     */
    IntsPlnCon3D(Plane3D dA, ConicalSurface3D dB) {
        super();
        setupParams(dA, dB);
    }

    /**
     * DOCUMENT ME!
     *
     * @param dA DOCUMENT ME!
     * @param dB DOCUMENT ME!
     */
    private void setupParams(Plane3D dA, ConicalSurface3D dB) {
        this.dA = dA;
        this.dB = dB;

        sinBsa = Math.sin(dB.semiAngle());
        tanBsa = Math.tan(dB.semiAngle());
        a_tol = dA.getToleranceForAngle();

        /*
         * vertex of B
         */
        double ework = dB.radius() / tanBsa;
        Bv = dB.position().location().subtract(dB.position().z().multiply(ework));

        /*
         * project vertex of B to A
         */
        Vector3D evec = dA.position().location().subtract(Bv);
        ework = dA.position().z().dotProduct(evec);
        eB2A = dA.position().z().multiply(ework);
        pBv2A = Bv.add(eB2A);

        /*
         * project Z-axis of B to A
         */
        edot = dA.position().z().dotProduct(dB.position().z());

        if (Math.abs(edot) < Math.cos(a_tol)) {
            pBz2A = dB.position().z().subtract(dA.position().z().multiply(edot))
                      .unitized();
            eBdot = dB.position().z().dotProduct(pBz2A); /* always greater than 0 */
        } else {
            pBz2A = Vector3D.zeroVector;
            eBdot = 0.0;
        }

        ecrs = dA.position().z().crossProduct(dB.position().z());
    }

    /**
     * DOCUMENT ME!
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SurfaceSurfaceInterference3D[] oneLine(boolean doExchange) {
        Line3D res = new Line3D(Bv, pBz2A);
        IntersectionCurve3D ints = dA.curveToIntersectionCurve(res, dB,
                doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints };

        return sol;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SurfaceSurfaceInterference3D[] twoLines(boolean doExchange) {
        double ework = Math.sqrt((1.0 + (tanBsa * tanBsa)) -
                (1.0 / (eBdot * eBdot)));
        Vector3D ecrs2 = ecrs.unitized().multiply(ework);

        Line3D res1 = new Line3D(Bv, pBz2A.divide(eBdot).add(ecrs2).unitized());
        Line3D res2 = new Line3D(Bv,
                pBz2A.divide(eBdot).subtract(ecrs2).unitized());
        IntersectionCurve3D ints1 = dA.curveToIntersectionCurve(res1, dB,
                doExchange);
        IntersectionCurve3D ints2 = dA.curveToIntersectionCurve(res2, dB,
                doExchange);

        SurfaceSurfaceInterference3D[] sol = { ints1, ints2 };

        return sol;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SurfaceSurfaceInterference3D[] onePoint(boolean doExchange) {
        Point3D res = Bv;
        IntersectionPoint3D intsPnt = dA.pointToIntersectionPoint(res, dB,
                doExchange);
        SurfaceSurfaceInterference3D[] intf = { intsPnt };

        return intf;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SurfaceSurfaceInterference3D[] oneCircle(boolean doExchange) {
        double ework = eB2A.dotProduct(dB.position().z());
        Axis2Placement3D axis = new Axis2Placement3D(Bv.add(dB.position().z()
                                                              .multiply(ework)),
                dA.position().z(), dA.position().x());
        Circle3D res = new Circle3D(axis, Math.abs(ework) * tanBsa);
        IntersectionCurve3D ints = dA.curveToIntersectionCurve(res, dB,
                doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints };

        return sol;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SurfaceSurfaceInterference3D[] oneParabola(boolean doExchange) {
        /*
         * loc = pBv2A - (X / tanBsa) * pBz2A, (X is a length of eB2A).
         */
        Point3D loc = pBv2A;
        Vector3D Bz = dB.position().z();
        Vector3D pBz2A2 = pBz2A;

        if (eB2A.dotProduct(Bz) < 0.0) {
            Bz = Bz.reverse();
            pBz2A2 = pBz2A2.reverse();
        }

        double ework = eB2A.length();

        if (Math.abs(dB.semiAngle() - (Math.PI / 4.0)) > a_tol) {
            double ework2 = ework / Math.tan(2.0 * dB.semiAngle());
            loc = loc.add(pBz2A2.multiply(ework2));
        }

        /*
         * axis is A's axis
         * ref_dir is B's axis
         */
        Axis2Placement3D axis = new Axis2Placement3D(loc, dA.position().z(), Bz);

        /*
         * focal dist = X * tanBsa / 2
         */
        Parabola3D res = new Parabola3D(axis, (ework * tanBsa) / 2.0);
        IntersectionCurve3D ints = dA.curveToIntersectionCurve(res, dB,
                doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints };

        return sol;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Hyperbola3D oneHyperbola() {
        double ecos;
        double esin;
        double ework;
        double ework2;
        double ework3;

        /*
         * loc
         */
        Point3D loc = pBv2A;

        ecos = edot;
        esin = Math.sqrt(1.0 - (ecos * ecos));

        ework = eB2A.length();

        ework2 = Math.abs((esin * esin * tanBsa * tanBsa) - (ecos * ecos));

        ework3 = Math.abs((ecos * esin * ework * (1.0 + (tanBsa * tanBsa))) / ework2);

        if (eB2A.dotProduct(dB.position().z()) > 0.0) {
            loc = loc.subtract(pBz2A.multiply(ework3));
        } else {
            loc = loc.add(pBz2A.multiply(ework3));
        }

        /*
         * axis is A's axis
         * ref_dir is B's axis
         */
        Axis2Placement3D axis = new Axis2Placement3D(loc, dA.position().z(),
                dB.position().z());

        return new Hyperbola3D(axis, (ework * tanBsa) / ework2,
            (ework * tanBsa) / Math.sqrt(ework2));
    }

    /**
     * DOCUMENT ME!
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SurfaceSurfaceInterference3D[] hyperbola(boolean doExchange) {
        Hyperbola3D res1 = oneHyperbola();

        Point3D loc = dB.position().location();
        Vector3D vec = Bv.subtract(loc).multiply(2.0);
        loc = loc.add(vec);

        Axis2Placement3D axis2 = new Axis2Placement3D(loc,
                dB.position().z().reverse(), dB.position().x().reverse());
        ConicalSurface3D saved_dB = dB;
        ConicalSurface3D dB2 = new ConicalSurface3D(axis2, dB.radius(),
                dB.semiAngle());
        setupParams(dA, dB2);

        Hyperbola3D res2 = oneHyperbola();

        setupParams(dA, saved_dB);

        IntersectionCurve3D ints1 = dA.curveToIntersectionCurve(res1, dB,
                doExchange);
        IntersectionCurve3D ints2 = dA.curveToIntersectionCurve(res2, dB,
                doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints1, ints2 };

        return sol;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SurfaceSurfaceInterference3D[] oneEllipse(boolean doExchange) {
        double ecos;
        double esin;
        double ework;
        double ework2;
        double ework3;

        ecos = edot;
        esin = Math.sqrt(1.0 - (ecos * ecos));

        ework = eB2A.length();

        ework2 = Math.abs((esin * esin * tanBsa * tanBsa) - (ecos * ecos));

        ework3 = Math.abs((ecos * esin * ework * (1.0 + (tanBsa * tanBsa))) / ework2);

        if (eB2A.dotProduct(dB.position().z()) < 0.0) {
            ework3 = (-ework3);
        }

        /*
         * axis is A's axis
         * ref_dir is B's axis
         */
        Axis2Placement3D axis = new Axis2Placement3D(pBv2A.add(pBz2A.multiply(
                        ework3)), dA.position().z(), dB.position().z());

        Ellipse3D res = new Ellipse3D(axis, (ework * tanBsa) / ework2,
                (ework * tanBsa) / Math.sqrt(ework2));
        IntersectionCurve3D ints = dA.curveToIntersectionCurve(res, dB,
                doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints };

        return sol;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    SurfaceSurfaceInterference3D[] getInterference(boolean doExchange) {
        SurfaceSurfaceInterference3D[] result;

        /*
         * LINE / 2 LINES / POINT / CIRCLE / PARABOLA / HYPERBOLA / ELLIPSE
         */
        if (eB2A.length() < dA.getToleranceForDistance()) {
            /*
             * vertex of B is on A.
             */
            if (Math.abs(Math.acos(eBdot) - dB.semiAngle()) < a_tol) { // line

                return oneLine(doExchange);
            }

            if (Math.acos(eBdot) < dB.semiAngle()) { // 2 lines

                return twoLines(doExchange);
            }

            return onePoint(doExchange);
        }

        /*
         * vertex of B is NOT on A
         */
        if (ecrs.length() < Math.sin(a_tol)) { // circle

            return oneCircle(doExchange);
        }

        if (Math.abs(Math.acos(eBdot) - dB.semiAngle()) < a_tol) { // parabola

            return oneParabola(doExchange); // one parbola
        }

        if (Math.acos(eBdot) < dB.semiAngle()) { // hyperbola

            return hyperbola(doExchange);
        }

        return oneEllipse(doExchange); // ellipse
    }
}
