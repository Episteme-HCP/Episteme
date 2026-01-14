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

import org.jscience.mathematics.MathUtils;


/**
 * 3D
 * �Ȗ�?�̗v�f��^����?A���̃p���??[�^��Ԃł�2�����\���𓾂�?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:45 $
 */
class ToParameterSpaceOfSurface3D {
    /** DOCUMENT ME! */
    private static final int MYDEF_UIP = 21;

    /** DOCUMENT ME! */
    private static final double MYDEF_PAR_BIAS = 0.1;

    // 0.03 (GHL �������ȂBĂ�) �͂ǂ���?����������
    // private static final double MYDEF_HYP_BIAS = 0.03;
    /** DOCUMENT ME! */
    private static final double MYDEF_HYP_BIAS = 0.1;

    /** DOCUMENT ME! */
    private ElementarySurface3D surface;

    /** DOCUMENT ME! */
    private CartesianTransformationOperator3D trns;

/**
     * Creates a new ToParameterSpaceOfSurface3D object.
     *
     * @param surface DOCUMENT ME!
     */
    private ToParameterSpaceOfSurface3D(ElementarySurface3D surface) {
        this.surface = surface;
        trns = new CartesianTransformationOperator3D(surface.position(), 1.0);
    }

    /**
     * ?���_�𕽖ʂ̃p���??[�^��Ԃł�2�����\���ɕϊ�����
     *
     * @param ffc ?���_��?�B���?�
     *
     * @return ���ʂ̃p���??[�^��Ԃł�2�����\��(?���_)
     */
    private Point2D[] to2DControlPointsOfPlane(
        FreeformCurveWithControlPoints3D ffc) {
        int npnts = ffc.nControlPoints();
        Point2D[] pnts2D = new Point2D[npnts];

        for (int i = 0; i < npnts; i++) {
            pnts2D[i] = ffc.controlPointAt(i).to2D(trns);
        }

        return pnts2D;
    }

    /**
     * ��?��?�ɂ���Ɖ��肵��?A���ʂ̃p���??[�^��Ԃł�2�����\���𓾂�
     *
     * @param curve ��?�
     *
     * @return ���ʂ̃p���??[�^��Ԃł�2�����\��(��?�)
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    private ParametricCurve2D convertCurvePlane(ParametricCurve3D curve) {
        switch (curve.type()) {
        case ParametricCurve3D.LINE_3D: {
            Line3D lin = (Line3D) curve;

            return lin.toLocal2D(trns);
        }

        case ParametricCurve3D.CIRCLE_3D:
        case ParametricCurve3D.ELLIPSE_3D:
        case ParametricCurve3D.HYPERBOLA_3D:
        case ParametricCurve3D.PARABOLA_3D: {
            Conic3D cnc = (Conic3D) curve;

            return cnc.toLocal2D(trns);
        }

        case ParametricCurve3D.POLYLINE_3D: {
            Polyline3D pol = (Polyline3D) curve;
            int npnts = pol.nPoints();
            Point2D[] pnts2D = new Point2D[npnts];

            for (int i = 0; i < npnts; i++) {
                pnts2D[i] = pol.pointAt(i).to2D(trns);
            }

            return new Polyline2D(pnts2D, pol.closed());
        }

        case ParametricCurve3D.PURE_BEZIER_CURVE_3D: {
            PureBezierCurve3D bzc = (PureBezierCurve3D) curve;

            return new PureBezierCurve2D(to2DControlPointsOfPlane(bzc),
                bzc.weights());
        }

        case ParametricCurve3D.BSPLINE_CURVE_3D: {
            BsplineCurve3D bsc = (BsplineCurve3D) curve;

            return new BsplineCurve2D(bsc.knotData(),
                to2DControlPointsOfPlane(bsc), bsc.weights());
        }
        }

        throw new UnsupportedOperationException();
    }

    /*
     * for Circle/Ellipse
     */
    private Point3D[] transformCirEllPol(Conic3D cirell) {
        Point3D[] pnts = new Point3D[MYDEF_UIP];
        double step;
        double t;

        step = GeometryUtils.PI2 / (MYDEF_UIP - 1);

        for (int i = 1; i < MYDEF_UIP; i++) {
            t = i * step;
            pnts[i] = cirell.coordinates(t);
        }

        pnts[0] = pnts[MYDEF_UIP - 1];

        return pnts;
    }

    /*
     * for Parabola
     */
    private Point3D[] transformParPol(Parabola3D par) {
        Point3D[] pnts = new Point3D[MYDEF_UIP];
        double t;
        int half_uip;

        half_uip = (MYDEF_UIP - 1) / 2;

        for (int i = 1; i <= half_uip; i++) {
            t = MYDEF_PAR_BIAS * i * i;
            pnts[half_uip + i] = par.coordinates(t);
            pnts[half_uip - i] = par.coordinates(-t);
        }

        pnts[half_uip] = par.position().location();

        return pnts;
    }

    /*
     * for Hyperbola
     */
    private Point3D[] transformHypPol(Hyperbola3D hyp) {
        Point3D[] pnts = new Point3D[MYDEF_UIP];
        double t;
        int half_uip;

        half_uip = (MYDEF_UIP - 1) / 2;

        for (int i = 1; i <= half_uip; i++) {
            t = MYDEF_HYP_BIAS * i * i;
            pnts[half_uip + i] = hyp.coordinates(t);
            pnts[half_uip - i] = hyp.coordinates(-t);
        }

        pnts[half_uip] = hyp.position().location()
                            .add(hyp.position().x().multiply(hyp.xRadius()));

        return pnts;
    }

    /**
     * DOCUMENT ME!
     *
     * @param is_pole DOCUMENT ME!
     * @param ith DOCUMENT ME!
     * @param inc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getNeighborNonPoleIdx(boolean[] is_pole, int ith, boolean inc) {
        int uip = is_pole.length;
        int src = ith;

        if (inc) {
            while (is_pole[ith]) {
                if (++ith >= uip) {
                    ith = 0;
                }

                if (ith == src) {
                    return (-1);
                }
            }
        } else {
            while (is_pole[ith]) {
                if (--ith <= 0) {
                    ith = uip - 1;
                }

                if (ith == src) {
                    return (-1);
                }
            }
        }

        return ith;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pnts DOCUMENT ME!
     * @param is_pole DOCUMENT ME!
     */
    private void adjustPole(Point2D[] pnts, boolean[] is_pole) {
        int ip; // prev. non pole
        int in; // next  non pole
        double x_ip; // prev. X
        double x_in; // next  X

        int uip = pnts.length;

        for (int i = 0; i < uip; i++) {
            if (!is_pole[i]) {
                continue;
            }

            if (((ip = getNeighborNonPoleIdx(is_pole, i, false)) < 0) ||
                    ((in = getNeighborNonPoleIdx(is_pole, i, true)) < 0)) {
                return;
            }

            x_ip = pnts[ip].x();
            x_in = pnts[in].x();

            if (x_ip > x_in) {
                x_in += GeometryUtils.PI2;
            }

            pnts[i] = new CartesianPoint2D((x_ip + x_in) / 2.0, pnts[i].y());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pnt3d DOCUMENT ME!
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private PointOnPolarSurface pntOnSph(Point3D pnt3d, double radius) {
        double x;
        double y;
        boolean isPole;
        Point3D epnt;
        Point2D epnt2d;

        epnt = trns.toLocal(pnt3d);
        epnt = epnt.divide(radius);

        double ework = MathUtils.midOf3(-1.0, epnt.z(), 1.0);
        y = Math.asin(ework);

        ework = Math.sqrt(1.0 - (ework * ework));
        epnt2d = epnt.to2D().divide(ework);

        ework = MathUtils.midOf3(-1.0, epnt2d.x(), 1.0);
        x = Math.acos(ework);

        if (epnt2d.y() < 0.0) {
            x = GeometryUtils.PI2 - x;
        }

        if (epnt2d.toVector2D().length() < pnt3d.getToleranceForDistance()) {
            isPole = true; // pole
        } else {
            isPole = false; // normal
        }

        return new PointOnPolarSurface(x, y, isPole);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pnt3d DOCUMENT ME!
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Point2D pntOnCyl(Point3D pnt3d, double radius) {
        Point3D epnt;
        double x;
        double y;

        epnt = trns.toLocal(pnt3d);
        epnt = epnt.divide(radius);

        double ework = MathUtils.midOf3(-1.0, epnt.x(), 1.0);
        x = Math.acos(ework);

        if (epnt.y() < 0.0) {
            x = GeometryUtils.PI2 - x;
        }

        y = epnt.z();

        return new CartesianPoint2D(x, y);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pnt3d DOCUMENT ME!
     * @param radius DOCUMENT ME!
     * @param semiAngle DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private PointOnPolarSurface pntOnCon(Point3D pnt3d, double radius,
        double semiAngle) {
        double x;
        double y;
        Point3D epnt;
        Point2D epnt2d;

        epnt = trns.toLocal(pnt3d);

        y = epnt.z();

        double ework = radius + (Math.tan(semiAngle) * epnt.z());

        if (Math.abs(ework) < pnt3d.getToleranceForDistance()) { // apex of cone

            return new PointOnPolarSurface(0.0, y, true);
        }

        epnt = epnt.divide(ework);

        ework = MathUtils.midOf3(-1.0, epnt.x(), 1.0);
        x = Math.acos(ework);

        if (epnt.y() < 0.0) {
            x = GeometryUtils.PI2 - x;
        }

        return new PointOnPolarSurface(x, y, false);
    }

    /**
     * ��?�?�ɂ���Ɖ��肵��?A���̃p���??[�^��Ԃł�2�����\���𓾂�
     *
     * @param curve ��?�
     *
     * @return ���̃p���??[�^��Ԃł�2�����\��(��?�)
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    private ParametricCurve2D convertCurveSphere(ParametricCurve3D curve) {
        SphericalSurface3D sph = (SphericalSurface3D) surface;
        Point2D[] pnts2d;
        boolean[] isPole;
        PointOnPolarSurface pos;

        switch (curve.type()) {
        case ParametricCurve3D.POLYLINE_3D: {
            Polyline3D pol = (Polyline3D) curve;
            int uip = pol.nPoints();
            pnts2d = new Point2D[uip];
            isPole = new boolean[uip];

            for (int i = 0; i < uip; i++) {
                pos = pntOnSph(pol.pointAt(i), sph.radius());
                pnts2d[i] = pos.pnt;
                isPole[i] = pos.isPole;
            }

            adjustPole(pnts2d, isPole);
            sph.confirmConnectionOfPointsOnSurface(pnts2d);

            return new Polyline2D(pnts2d, pol.closed());
        }

        case ParametricCurve3D.CIRCLE_3D: {
            Circle3D cir = (Circle3D) curve;
            Point3D[] pnts = transformCirEllPol(cir);
            int uip = pnts.length;
            pnts2d = new Point2D[uip];
            isPole = new boolean[uip];

            for (int i = 0; i < uip; i++) {
                pos = pntOnSph(pnts[i], sph.radius());
                pnts2d[i] = pos.pnt;
                isPole[i] = pos.isPole;
            }

            adjustPole(pnts2d, isPole);
            sph.confirmConnectionOfPointsOnSurface(pnts2d);

            return new Polyline2D(pnts2d, false);
        }
        }

        throw new UnsupportedOperationException();
    }

    /**
     * ��?�~��?�ɂ���Ɖ��肵��?A���̃p���??[�^��Ԃł�2�����\���𓾂�
     *
     * @param curve ��?�
     *
     * @return �~���̃p���??[�^��Ԃł�2�����\��(��?�)
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    private ParametricCurve2D convertCurveCylinder(ParametricCurve3D curve) {
        CylindricalSurface3D cyl = (CylindricalSurface3D) surface;
        Point2D[] pnts2d;

        switch (curve.type()) {
        case ParametricCurve3D.LINE_3D: {
            Line3D lin = (Line3D) curve;
            Point2D spnt = pntOnCyl(lin.pnt(), cyl.radius());
            Point2D epnt = pntOnCyl(lin.pnt().add(lin.dir()), cyl.radius());

            return new Line2D(spnt, epnt);
        }

        case ParametricCurve3D.POLYLINE_3D: {
            Polyline3D pol = (Polyline3D) curve;
            int uip = pol.nPoints();
            pnts2d = new Point2D[uip];

            for (int i = 0; i < uip; i++) {
                pnts2d[i] = pntOnCyl(pol.pointAt(i), cyl.radius());
            }

            cyl.confirmConnectionOfPointsOnSurface(pnts2d);

            return new Polyline2D(pnts2d, pol.closed());
        }

        case ParametricCurve3D.CIRCLE_3D:
        case ParametricCurve3D.ELLIPSE_3D: {
            Conic3D cnc = (Conic3D) curve;
            Point3D[] pnts = transformCirEllPol(cnc);
            int uip = pnts.length;
            pnts2d = new Point2D[uip];

            for (int i = 0; i < uip; i++) {
                pnts2d[i] = pntOnCyl(pnts[i], cyl.radius());
            }

            cyl.confirmConnectionOfPointsOnSurface(pnts2d);

            return new Polyline2D(pnts2d, false);
        }
        }

        throw new UnsupportedOperationException();
    }

    /**
     * ��?�~???�ɂ���Ɖ��肵��?A���̃p���??[�^��Ԃł�2�����\���𓾂�
     *
     * @param curve ��?�
     *
     * @return �~??�̃p���??[�^��Ԃł�2�����\��(��?�)
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    private ParametricCurve2D convertCurveCone(ParametricCurve3D curve) {
        ConicalSurface3D con = (ConicalSurface3D) surface;
        Point2D[] pnts2d;
        boolean[] isPole;
        PointOnPolarSurface pos;

        switch (curve.type()) {
        case ParametricCurve3D.LINE_3D: {
            Line3D lin = (Line3D) curve;
            isPole = new boolean[2];
            pos = pntOnCon(lin.pnt(), con.radius(), con.semiAngle());

            Point2D spnt = pos.pnt;
            isPole[0] = pos.isPole;
            pos = pntOnCon(lin.pnt().add(lin.dir()), con.radius(),
                    con.semiAngle());

            Point2D epnt = pos.pnt;
            isPole[1] = pos.isPole;

            if (isPole[0] && !isPole[1]) {
                spnt = new CartesianPoint2D(epnt.x(), spnt.y());
            } else if (!isPole[0] && isPole[1]) {
                epnt = new CartesianPoint2D(spnt.x(), epnt.y());
            }

            return new Line2D(spnt, epnt);
        }

        case ParametricCurve3D.POLYLINE_3D: {
            Polyline3D pol = (Polyline3D) curve;
            int uip = pol.nPoints();
            pnts2d = new Point2D[uip];
            isPole = new boolean[uip];

            for (int i = 0; i < uip; i++) {
                pos = pntOnCon(pol.pointAt(i), con.radius(), con.semiAngle());
                pnts2d[i] = pos.pnt;
                isPole[i] = pos.isPole;
            }

            adjustPole(pnts2d, isPole);
            con.confirmConnectionOfPointsOnSurface(pnts2d);

            return new Polyline2D(pnts2d, pol.closed());
        }

        case ParametricCurve3D.CIRCLE_3D:
        case ParametricCurve3D.ELLIPSE_3D: {
            Conic3D cnc = (Conic3D) curve;
            Point3D[] pnts = transformCirEllPol(cnc);
            int uip = pnts.length;
            pnts2d = new Point2D[uip];
            isPole = new boolean[uip];

            for (int i = 0; i < uip; i++) {
                pos = pntOnCon(pnts[i], con.radius(), con.semiAngle());
                pnts2d[i] = pos.pnt;
                isPole[i] = pos.isPole;
            }

            adjustPole(pnts2d, isPole);
            con.confirmConnectionOfPointsOnSurface(pnts2d);

            return new Polyline2D(pnts2d, false);
        }

        case ParametricCurve3D.PARABOLA_3D: {
            Parabola3D par = (Parabola3D) curve;
            Point3D[] pnts = transformParPol(par);
            int uip = pnts.length;
            pnts2d = new Point2D[uip];
            isPole = new boolean[uip];

            for (int i = 0; i < uip; i++) {
                pos = pntOnCon(pnts[i], con.radius(), con.semiAngle());
                pnts2d[i] = pos.pnt;
                isPole[i] = pos.isPole;
            }

            adjustPole(pnts2d, isPole);
            con.confirmConnectionOfPointsOnSurface(pnts2d);

            return new Polyline2D(pnts2d, false);
        }

        case ParametricCurve3D.HYPERBOLA_3D: {
            Hyperbola3D hyp = (Hyperbola3D) curve;
            Point3D[] pnts = transformHypPol(hyp);
            int uip = pnts.length;
            pnts2d = new Point2D[uip];
            isPole = new boolean[uip];

            for (int i = 0; i < uip; i++) {
                pos = pntOnCon(pnts[i], con.radius(), con.semiAngle());
                pnts2d[i] = pos.pnt;
                isPole[i] = pos.isPole;
            }

            adjustPole(pnts2d, isPole);
            con.confirmConnectionOfPointsOnSurface(pnts2d);

            return new Polyline2D(pnts2d, false);
        }
        }

        throw new UnsupportedOperationException();
    }

    /**
     * DOCUMENT ME!
     *
     * @param curve DOCUMENT ME!
     * @param surface DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    static ParametricCurve2D convertCurve(ParametricCurve3D curve,
        ElementarySurface3D surface) {
        ToParameterSpaceOfSurface3D doObj = new ToParameterSpaceOfSurface3D(surface);

        switch (surface.type()) {
        case ParametricSurface3D.PLANE_3D:
            return doObj.convertCurvePlane(curve);

        case ParametricSurface3D.SPHERICAL_SURFACE_3D:
            return doObj.convertCurveSphere(curve);

        case ParametricSurface3D.CYLINDRICAL_SURFACE_3D:
            return doObj.convertCurveCylinder(curve);

        case ParametricSurface3D.CONICAL_SURFACE_3D:
            return doObj.convertCurveCone(curve);
        }

        throw new UnsupportedOperationException();
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class PointOnPolarSurface {
        /** DOCUMENT ME! */
        Point2D pnt;

        /** DOCUMENT ME! */
        boolean isPole;

/**
         * Creates a new PointOnPolarSurface object.
         *
         * @param pnt    DOCUMENT ME!
         * @param isPole DOCUMENT ME!
         */
        PointOnPolarSurface(Point2D pnt, boolean isPole) {
            super();
            this.pnt = pnt;
            this.isPole = isPole;
        }

/**
         * Creates a new PointOnPolarSurface object.
         *
         * @param x      DOCUMENT ME!
         * @param y      DOCUMENT ME!
         * @param isPole DOCUMENT ME!
         */
        PointOnPolarSurface(double x, double y, boolean isPole) {
            super();
            this.pnt = new CartesianPoint2D(x, y);
            this.isPole = isPole;
        }
    }
}
