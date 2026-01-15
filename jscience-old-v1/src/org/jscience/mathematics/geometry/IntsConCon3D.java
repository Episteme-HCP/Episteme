/*
 * 3D �~??���m�̌�_��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IntsConCon3D.java,v 1.2 2007-10-21 17:38:24 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import org.jscience.mathematics.MachineEpsilon;
import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.util.NoSuchElementException;
import java.util.Vector;


/**
 * 3D �~??���m�̌�_��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:24 $
 */
class IntsConCon3D {
    /*
     * ���R��?�(polyline)��?��x
     */

    /**
     * DOCUMENT ME!
     */
    private static int nst = 20;

    /*
     * ���̒l���傫�����邩�ǂ����̊�?��l
     */

    /**
     * DOCUMENT ME!
     */
    final static double MY_HUGE = 1.0e7;

    /*
     * �����Ƃ�~?? (this ��)
     */

    /**
     * DOCUMENT ME!
     */
    private ConicalSurface3D geomA;

    /*
     * �¤���̉~?? (mate ��)
     */

    /**
     * DOCUMENT ME!
     */
    private ConicalSurface3D geomB;

    /*
     * geomA �� geomB �Ŋp�x���傫����̉~??
     */

    /**
     * DOCUMENT ME!
     */
    private ConicalSurface3D coneB;

    /*
     * geomA �� geomB �Ŋp�x��?�������̉~??
     */

    /**
     * DOCUMENT ME!
     */
    private ConicalSurface3D coneS;

    /*
     * �傫���~??�̒��_����?������~??�̒��_�ւ̃x�N�g��
     */

    /**
     * DOCUMENT ME!
     */
    private Vector3D b2s;

    /*
     * ?������~??�̒��_����傫���~??�̒��_�ւ̃x�N�g��
     */

    /**
     * DOCUMENT ME!
     */
    private Vector3D s2b;

    /*
     * ?������~??�̒��_�Ƒ傫���~??�̒��_�̋���
     *
     */

    /**
     * DOCUMENT ME!
     */
    private double dist;

    /*
     * ����̉~??�̒�?S���̂Ȃ��p�x
     */

    /**
     * DOCUMENT ME!
     */
    private double theta;

    /*
     * ?������~??�̒�?S���̃x�N�g���� s2b �̂Ȃ��p�x
     */

    /**
     * DOCUMENT ME!
     */
    private double phiS;

    /*
     * �傫���~??�̒�?S���̃x�N�g���� b2s �̂Ȃ��p�x
     */

    /**
     * DOCUMENT ME!
     */
    private double phiB;

    /*
     * �����̋��e��?�
     */

    /**
     * DOCUMENT ME!
     */
    private double aTol;

    /*
     * �p�x�̋��e��?�
     */

    /**
     * DOCUMENT ME!
     */
    private double dTol;

    /*
     * �p���??[�^�������~??
     */

    /**
     * DOCUMENT ME!
     */
    private ConicalSurface3D cp;

    /*
     * �p���??[�^������Ȃ���̉~??
     */

    /**
     * DOCUMENT ME!
     */
    private ConicalSurface3D co;

    /*
     * �p���??[�^�������~??�̒�?S���̃x�N�g��
     */

    /**
     * DOCUMENT ME!
     */
    private Vector3D wp;

    /*
     * �p���??[�^������Ȃ���̉~??�̒�?S���̃x�N�g��
     */

    /**
     * DOCUMENT ME!
     */
    private Vector3D wo;

    /*
     * �p���??[�^�������~??�̒��_
     */

    /**
     * DOCUMENT ME!
     */
    private Point3D vep;

    /*
     * �p���??[�^������Ȃ���̉~??�̒��_
     */

    /**
     * DOCUMENT ME!
     */
    private Point3D veo;

    /*
     * ?u�p���??[�^�������~??�̒�?S���̃x�N�g��?v��?u�p���??[�^�������
     * �~??�̒��_����p���??[�^������Ȃ���̉~??�̒��_�ւ̃x�N�g��?v��
     * �Ȃ��p�x
     */

    /**
     * DOCUMENT ME!
     */
    private double phip;

    /*
     * �p���??[�^�������~??��(���p / 2)
     */

    /**
     * DOCUMENT ME!
     */
    private double alpp;

    /*
     * �p���??[�^�������~??��(���p / 2) �� cosine
     */

    /**
     * DOCUMENT ME!
     */
    private double cosp;

    /*
     * �p���??[�^������Ȃ���̉~??��(���p / 2) �� cosine
     */

    /**
     * DOCUMENT ME!
     */
    private double coso;

    /*
     * �p���??[�^�������~??��(���p / 2) �� tangent
     */

    /**
     * DOCUMENT ME!
     */
    private double tanp;

    /*
     * �p���??[�^������Ȃ���̉~??��(���p / 2) �� tangent
     */

    /**
     * DOCUMENT ME!
     */
    private double tano;

    /*
     * �p���??[�^�������~??�̒��_����p���??[�^������Ȃ���̉~??��
     * ���_�ւ̃x�N�g��
     */

    /**
     * DOCUMENT ME!
     */
    private Vector3D p2o;

    /*
     * �p���??[�^������Ȃ���̉~??�̒��_����p���??[�^�������~??��
     * ���_�ւ̃x�N�g��
     */

    /**
     * DOCUMENT ME!
     */
    private Vector3D o2p;

    /*
     * �I�u�W�F�N�g��?\�z����?B
     *
     * @param geomA        �~??A
     * @param geomB        �~??B
     */
    IntsConCon3D(ConicalSurface3D geomA, ConicalSurface3D geomB) {
        super();
        setupParams(geomA, geomB);
    }

    /*
     * �K�v�ȃp���??[�^��?�����?B
     *
     * @param geomA �����Ƃ�~??
     * @param geomB �¤���̉~??
     */
    private void setupParams(ConicalSurface3D geomA, ConicalSurface3D geomB) {
        this.aTol = geomA.getToleranceForAngle();
        this.dTol = geomA.getToleranceForDistance();
        this.geomA = geomA;
        this.geomB = geomB;

        // ���p���傫����� geomA ��?�������� geomB �ɃZ�b�g
        if (geomA.semiAngle() > geomB.semiAngle()) {
            this.coneB = geomA;
            this.coneS = geomB;
        } else {
            this.coneB = geomB;
            this.coneS = geomA;
        }

        this.b2s = coneS.apex().subtract(coneB.apex());
        this.s2b = coneB.apex().subtract(coneS.apex());

        this.dist = b2s.length();

        this.phiS = Math.acos(s2b.dotProduct(coneS.position().z()) / dist);
        this.phiB = Math.acos(b2s.dotProduct(coneB.position().z()) / dist);

        double ework = (coneB.position().z()).dotProduct(coneS.position().z());

        if (ework > 1.0) {
            ework = 1.0;
        }

        if (ework < (-1.0)) {
            ework = (-1.0);
        }

        this.theta = Math.acos(ework);
    }

    /**
     * �v���C�x?[�g�?�\�b�h (uvwPointFromT)
     *
     * @param u    ��?�?W�n��x���̃x�N�g��
     * @param v    ��?�?W�n��y���̃x�N�g��
     * @param w    ��?�?W�n��z���̃x�N�g��
     * @param ctan �~??��(���p / 2) �� tangent
     * @param t    �p�x
     * @return �x�N�g��
     */
    private LiteralVector3D uvwPointFromT(Vector3D u, Vector3D v, Vector3D w,
                                          double ctan, double t) {
        double cosT = Math.cos(t);
        double sinT = Math.sin(t);
        double x = (ctan * ((cosT * u.x()) + (sinT * v.x()))) + w.x();
        double y = (ctan * ((cosT * u.y()) + (sinT * v.y()))) + w.y();
        double z = (ctan * ((cosT * u.z()) + (sinT * v.z()))) + w.z();

        return new LiteralVector3D(x, y, z);
    }

    /**
     * �����?�߂邽�߂�2������2���̌W?���Ԃ�?B
     *
     * @param evec  uvwPointFromT() ���Ԃ��x�N�g��
     * @param w2    �p���??[�^������Ȃ���̉~??�̒�?S���̃x�N�g��
     * @param cosa1 �p���??[�^�������~??��(���p /
     *              2) �� cosine
     * @param cosa2 �p���??[�^������Ȃ���̉~??��(���p
     *              / 2) �� cosine
     * @return �����?�߂邽�߂�2������2���̌W?�
     */
    private double coefA(Vector3D evec, Vector3D w2, double cosa1, double cosa2) {
        double edot = evec.dotProduct(w2);

        return ((edot * edot) - ((cosa2 * cosa2) / (cosa1 * cosa1)));
    }

    /**
     * �����?�߂邽�߂�2������1���̌W?���Ԃ�?B
     *
     * @param evec  uvwPointFromT() ���Ԃ��x�N�g��
     * @param v12v2 �p���??[�^������Ȃ���̉~??�̒��_����p���??[�^�������~??�̒��_�ւ̃x�N�g��
     * @param w2    �p���??[�^������Ȃ���̉~??�̒�?S���̃x�N�g��
     * @param cosa2 �p���??[�^������Ȃ���̉~??��(���p
     *              / 2) �� cosine
     * @return �����?�߂邽�߂�2������1���̌W?�
     */
    private double coefB(Vector3D evec, Vector3D v12v2, Vector3D w2,
                         double cosa2) {
        double edot = v12v2.dotProduct(w2);
        double cosa22 = cosa2 * cosa2;
        Vector3D sw2 = w2.multiply(edot);
        sw2 = sw2.subtract(v12v2.multiply(cosa22));

        return 2.0 * sw2.dotProduct(evec);
    }

    /**
     * �����?�߂邽�߂�2������0���̌W?���Ԃ�?B
     *
     * @param v12v2 �p���??[�^������Ȃ���̉~??�̒��_����p���??[�^�������~??�̒��_�ւ̃x�N�g��
     * @param w2    �p���??[�^������Ȃ���̉~??�̒�?S���̃x�N�g��
     * @param cosa2 �p���??[�^������Ȃ���̉~??��(���p
     *              / 2) �� cosine
     * @return �����?�߂邽�߂�2������0���̌W?�
     */
    private double coefC(Vector3D v12v2, Vector3D w2, double cosa2) {
        double edot = v12v2.dotProduct(w2);

        return ((edot * edot) - (cosa2 * cosa2 * v12v2.dotProduct(v12v2)));
    }

    /**
     * �v���C�x?[�g�?�\�b�h (aZero)
     *
     * @param cp    �p���??[�^�������~??
     * @param co    �p���??[�^������Ȃ���̉~??
     * @param cpOrg �p���??[�^�������~??�̒��_
     * @param up    �p���??[�^�������~??�̋�?�?W�n��x���̃x�N�g��
     * @param wp    �p���??[�^�������~??�̋�?�?W�n��z���̃x�N�g��
     * @param wo    �p���??[�^������Ȃ���̉~??�̋�?�?W�n��z���̃x�N�g��
     * @return ��?��̔z��
     * @throws FatalException DOCUMENT ME!
     * @see IndefiniteSolutionException
     */
    private double[] aZero(ConicalSurface3D cp, ConicalSurface3D co,
                           Point3D cpOrg, Vector3D up, Vector3D wp, Vector3D wo) {
        Point3D loc = cpOrg.add(wo);
        Vector3D axis = co.position().z();
        Vector3D refDir = co.position().x();
        Axis2Placement3D position = new Axis2Placement3D(loc, axis, refDir);
        double radius = Math.tan(co.semiAngle());
        Circle3D circle = new Circle3D(position, radius);

        IntersectionPoint3D[] ints;

        try {
            ints = circle.intersect(cp);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        double[] azero = new double[ints.length];

        for (int i = 0; i < ints.length; i++) {
            Vector3D evec;
            evec = ints[i].subtract(cpOrg);
            evec = (evec.project(wp)).unitized();
            azero[i] = up.angleWith(evec, wp);
        }

        return azero;
    }

    /**
     * �v���C�x?[�g�?�\�b�h (bZero)
     *
     * @param cp    �p���??[�^�������~??
     * @param co    �p���??[�^������Ȃ���̉~??
     * @param cpOrg �p���??[�^�������~??�̒��_
     * @param up    �p���??[�^�������~??�̋�?�?W�n��x���̃x�N�g��
     * @param wp    �p���??[�^�������~??�̋�?�?W�n��z���̃x�N�g��
     * @param coOrg �p���??[�^������Ȃ���̉~??�̒��_
     * @param wo    �p���??[�^������Ȃ���̉~??�̋�?�?W�n��z���̃x�N�g��
     * @return ��?��̔z��
     * @throws FatalException DOCUMENT ME!
     * @see IndefiniteSolutionException
     */
    private double[] bZero(ConicalSurface3D cp, ConicalSurface3D co,
                           Point3D cpOrg, Vector3D up, Vector3D wp, Point3D coOrg, Vector3D wo) {
        Vector3D eO2P = cpOrg.subtract(coOrg);
        double edot = eO2P.dotProduct(wo);
        Vector3D evec = wo.multiply(edot);
        double ecos = Math.cos(co.semiAngle());
        double ecos2 = ecos * ecos;

        Vector3D axis = evec.subtract(eO2P.multiply(ecos2));
        Axis2Placement3D position = new Axis2Placement3D(cpOrg, axis, wo);

        Plane3D plane = new Plane3D(position);
        Circle3D circle = new Circle3D(cp.position(), cp.radius());

        IntersectionPoint3D[] ints;

        try {
            ints = circle.intersect(plane);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        double[] bzero = new double[ints.length];

        for (int i = 0; i < ints.length; i++) {
            evec = ints[i].subtract(cpOrg);
            evec = (evec.project(wp)).unitized();
            bzero[i] = up.angleWith(evec, wp);
        }

        return bzero;
    }

    /**
     * ��͂��ꂽ3�x�N�g�������ʂ�Ȃ������ׂ�?B
     *
     * @param w1   �P�ʃx�N�g��
     * @param w2   �P�ʃx�N�g��
     * @param diff �C�ӂ̒����̃x�N�g��
     * @return ���ʂ�Ȃ�?�?���?^,�Ȃ��Ȃ�?�?��͋U��Ԃ�???
     */
    private boolean areCoplanar(Vector3D w1, Vector3D w2, Vector3D diff) {
        Vector3D udiff = diff.unitized();
        Vector3D a = w1.crossProduct(udiff).unitized();
        Vector3D b = w2.crossProduct(udiff).unitized();

        return Math.abs(a.dotProduct(b)) > Math.cos(aTol);
    }

    /**
     * are Real Tangent?
     *
     * @param sz  �x�N�g��(coneS.axis.z())
     * @param bz  �x�N�g��(coneB.axis.z())
     * @param s2b �x�N�g��(coneS.apex() -> coneB.apex())
     * @param b2s �x�N�g��(coneB.apex() -> coneS.apex())
     * @return are Real Tangent?
     */
    private boolean areRealTangent(Vector3D sz, Vector3D bz, Vector3D s2b,
                                   Vector3D b2s) {
        Vector3D a;
        Vector3D b;

        if (sz.dotProduct(s2b) > 0.0) {
            a = s2b.crossProduct(sz);
            b = s2b.crossProduct(bz);
        } else {
            a = b2s.crossProduct(sz);
            b = b2s.crossProduct(bz);
        }

        return a.dotProduct(b) <= 0.0;
    }

    /*
     * ��?S��������?�?��̌����?�߂�?B
     *
     * @param doExchange �~??��귂��邩�ǂ����̃t���O
     * @return                 ��?�̔z��
     * @see IndefiniteSolutionException
     */
    private SurfaceSurfaceInterference3D[] axesAreOverlap(boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] intf;

        if (dist < dTol) {
            Point3D mid = coneB.apex().midPoint(coneS.apex());
            IntersectionPoint3D intersectPoint = geomA.pointToIntersectionPoint(mid,
                    geomB, doExchange);

            if (Math.abs(coneB.semiAngle() - coneS.semiAngle()) < aTol) {
                /*
                 * coincident
                 */
                throw new IndefiniteSolutionException(intersectPoint);
            } else {
                /*
                 * return 1 point
                 */
                SurfaceSurfaceInterference3D[] work = {intersectPoint};
                intf = work;
            }
        } else {
            // one side
            double tanB = Math.tan(coneB.semiAngle());
            double tanS = Math.tan(coneS.semiAngle());
            double x1 = (dist * tanB) / (tanB + tanS);
            Vector3D axis = coneS.position().z();
            Vector3D refDir = coneS.position().x();
            Point3D location1 = coneS.apex().add(axis.multiply(x1));
            Axis2Placement3D position1 = new Axis2Placement3D(location1, axis,
                    refDir);
            Circle3D circle1 = new Circle3D(position1, x1 * tanS);

            // transrate one intersection curves
            IntersectionCurve3D ints1 = geomA.curveToIntersectionCurve(circle1,
                    geomB, doExchange);

            if (Math.abs(coneB.semiAngle() - coneS.semiAngle()) < aTol) {
                /*
                 * return 1 circle
                 */
                SurfaceSurfaceInterference3D[] work = {ints1};
                intf = work;
            } else {
                /*
                 * return 2 circle
                 */

                // another side
                double x2 = dist + ((dist * tanS) / (tanB - tanS));
                Point3D location2 = coneS.apex().add(axis.multiply(x2));
                Axis2Placement3D position2 = new Axis2Placement3D(location2,
                        axis, refDir);
                Circle3D circle2 = new Circle3D(position2, x2 * tanS);

                // transrate another intersection curves
                IntersectionCurve3D ints2 = geomA.curveToIntersectionCurve(circle2,
                        geomB, doExchange);

                SurfaceSurfaceInterference3D[] work = {ints1, ints2};
                intf = work;
            }
        }

        return intf;
    }

    /**
     * ����ƂȂ�~??��?�𓾂�?B
     *
     * @return ����ƂȂ�~??��?�
     * @throws FatalException DOCUMENT ME!
     * @see IndefiniteSolutionException
     */
    private SurfaceSurfaceInterference3D[] getConic() {
        Point3D[] res = new Point3D[3];
        Vector3D cu;
        Vector3D cv;
        Vector3D cw;
        cw = coneS.position().z();
        cv = (cw.crossProduct(s2b));
        cu = (cv.crossProduct(cw)).unitized();
        cv = cv.unitized();

        double[] azero = aZero(coneS, coneB, coneS.apex(), cu, cw,
                coneB.position().z());
        int naz = azero.length;

        if (naz < 0) {
            return new SurfaceSurfaceInterference3D[0];
        }

        if (naz > 1) {
            GeometryUtils.sortDoubleArray(azero);
        }

        for (int i = 0; i < 3; i++) {
            double t = 0.0;

            switch (naz) {
                case 0:

                    // ellipse
                    t = ((i + 1.0) * Math.PI) / 2.0;

                    break;

                case 1:

                    // parabola
                    switch (i) {
                        case 0:
                            t = GeometryUtils.PI2 / 3.0;

                            break;

                        case 1:
                            t = Math.PI / 36.0;

                            break;

                        case 2:
                            t = (4.0 * Math.PI) / 3.0;

                            break;
                    }

                    break;

                case 2:

                    // hyperbola
                    switch (i) {
                        case 0:
                            t = azero[0] - aTol;

                            break;

                        case 1:
                            t = azero[0] / 10.0;

                            break;

                        case 2:
                            t = azero[1] + aTol;

                            break;
                    }

                    break;

                default:

                    // never be occured
                    throw new FatalException();
            }

            Vector3D dir = uvwPointFromT(cu, cv, cw,
                    Math.tan(coneS.semiAngle()), t);
            Line3D line = new Line3D(coneS.apex(), dir);
            IntersectionPoint3D[] ints;

            try {
                ints = line.intersect(coneB);
                res[i] = ints[0];

                if (ints.length == 2) {
                    double dist0 = coneS.apex().distance2(ints[0]);
                    double dist1 = coneS.apex().distance2(ints[1]);

                    if (dist0 < dist1) {
                        res[i] = ints[1];
                    }
                }
            } catch (IndefiniteSolutionException e) {
                Point3D pnt = (Point3D) e.suitable();
                PointOnCurve3D pocL = new PointOnCurve3D(pnt, line,
                        line.pointToParameter(pnt), GeometryElement.doCheckDebug);
                double[] params = coneB.pointToParameter(pnt);
                PointOnSurface3D posConeB = new PointOnSurface3D(pnt, coneB,
                        params[0], params[1], GeometryElement.doCheckDebug);
                res[i] = new IntersectionPoint3D(coneS.apex(), pocL, posConeB,
                        GeometryElement.doCheckDebug);
            }
        }

        Point3D loc = res[1];
        Vector3D rf1 = res[0].subtract(res[1]);
        Vector3D rf2 = res[2].subtract(res[1]);
        Vector3D ax = rf1.crossProduct(rf2);
        Axis2Placement3D position = new Axis2Placement3D(loc, rf2, ax);
        Plane3D plane = new Plane3D(position);
        SurfaceSurfaceInterference3D[] conics;

        try {
            conics = plane.intersect(coneB);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException(); // shoud never be ocurred
        }

        return conics;
    }

    /*
     * �~??���m������Ƃ��ċȖ�?�̒�?��?��?�?��Ɍ����?�߂�?B
     *
     * @param doExchange �~??��귂��邩�ǂ����̃t���O
     * @return                 ��?�̔z��
     */
    private SurfaceSurfaceInterference3D[] rulingTangent(boolean doExchange) {
        IntersectionCurve3D ints = null;
        Line3D resL;
        SurfaceSurfaceInterference3D[] resG = null;

        if ((Math.abs(theta - (coneS.semiAngle() + coneB.semiAngle())) < aTol) &&
                areRealTangent(coneS.position().z(), coneB.position().z(), s2b,
                        b2s)) {
            /*
             * (coneB.semiAngle + coneS.semiAngle) == angle between both axes
             *
             * 1 line
             */
            if (coneS.position().z().dotProduct(s2b) > 0.0) {
                resL = new Line3D(coneB.apex(), s2b);
            } else {
                resL = new Line3D(coneS.apex(), b2s);
            }

            ints = geomA.curveToIntersectionCurve(resL, geomB, doExchange);
        } else if (Math.abs(theta - (coneB.semiAngle() - coneS.semiAngle())) < aTol) {
            /*
             * (coneB.semiAngle - coneS.semiAngle) == angle between both axes
             */
            if (coneB.position().z().dotProduct(b2s) > 0.0) {
                /*
                 * 1 line
                 */
                resL = new Line3D(coneS.apex(), b2s);
                ints = geomA.curveToIntersectionCurve(resL, geomB, doExchange);
            } else {
                /*
                 * 1 line and 1 ellipse
                 */
                resL = new Line3D(coneB.apex(), s2b);
                ints = geomA.curveToIntersectionCurve(resL, geomB, doExchange);
                resG = getConic();
            }
        } else if (Math.abs((coneS.semiAngle() + coneB.semiAngle() + theta) -
                Math.PI) < aTol) {
            /*
             * (coneB.semiAngle + coneS.semiAngle + (angle between both axes) == PI
             */
            if (coneB.position().z().dotProduct(b2s) > 0.0) {
                /*
                 * 1 line and 1 conic
                 */
                resL = new Line3D(coneB.apex(), coneS.apex());
                ints = geomA.curveToIntersectionCurve(resL, geomB, doExchange);
                resG = getConic();
            } else {
                if ((coneS.semiAngle() + coneB.semiAngle()) > ((Math.PI / 2.0) +
                        aTol)) {
                    /*
                     * 1 hyperbola
                     */
                    resG = getConic();
                } else {
                    /*
                     * no intersection
                     */
                }
            }
        } else if (Math.abs(((coneB.semiAngle() + theta) - coneS.semiAngle()) -
                Math.PI) < aTol) {
            /*
             * (coneB.semiAngle + (angle between both axes) - coneS.semiAngle) == PI
             */
            if (coneB.position().z().dotProduct(b2s) > 0.0) {
                /*
                 * 1 line
                 */
                resL = new Line3D(coneB.apex(), coneS.apex());
                ints = geomA.curveToIntersectionCurve(resL, geomB, doExchange);
            } else {
                /*
                 * no intersection
                 */
            }
        }

        Vector vec = new Vector();

        if (ints != null) {
            vec.addElement(ints);
        }

        if (resG != null) {
            for (int i = 0; i < resG.length; i++) {
                vec.addElement(resG[i]);
            }
        }

        SurfaceSurfaceInterference3D[] intf = new SurfaceSurfaceInterference3D[vec.size()];
        vec.copyInto(intf);

        return intf;
    }

    /*
     * at most 4 lines
     *
     * @param doExchange �~??��귂��邩�ǂ����̃t���O
     * @return                 ��?�̔z��
     * @see IndefiniteSolutionException
     */
    private SurfaceSurfaceInterference3D[] fourLines(boolean doExchange) {
        Circle3D circle = new Circle3D(coneB.position(), coneB.radius());
        IntersectionPoint3D[] rcv;

        try {
            rcv = circle.intersect(coneS);
        } catch (IndefiniteSolutionException e) {
            throw new FatalException();
        }

        int numberOfSolutions = rcv.length;

        Point3D pnt = coneB.apex().midPoint(coneS.apex());

        SurfaceSurfaceInterference3D[] intf;

        if (numberOfSolutions == 0) {
            IntersectionPoint3D intersectPoint = geomA.pointToIntersectionPoint(pnt,
                    geomB, doExchange);
            SurfaceSurfaceInterference3D[] work = {intersectPoint};
            intf = work;
        } else {
            SurfaceSurfaceInterference3D[] work = new SurfaceSurfaceInterference3D[numberOfSolutions];

            for (int i = 0; i < numberOfSolutions; i++) {
                Line3D line = new Line3D(pnt, rcv[i]);
                work[i] = geomA.curveToIntersectionCurve(line, geomB, doExchange);
            }

            intf = work;
        }

        return intf;
    }

    /*
     * set parameterized cone
     *
     * @param condition        �ǂ���̉~??��p���??[�^������ׂ����̃t���O
     */
    private void setParametarizedCone(boolean condition) {
        if (condition) {
            this.cp = coneB;
            this.co = coneS;
            this.vep = coneB.apex();
            this.veo = coneS.apex();
            this.wp = coneB.position().z();
            this.wo = coneS.position().z();
            this.p2o = b2s;
            this.o2p = s2b;
            this.phip = phiB;
            this.alpp = coneB.semiAngle();
            this.cosp = Math.cos(coneB.semiAngle());
            this.coso = Math.cos(coneS.semiAngle());
            this.tanp = Math.tan(coneB.semiAngle());
            this.tano = Math.tan(coneS.semiAngle());
        } else {
            this.cp = coneS;
            this.co = coneB;
            this.vep = coneS.apex();
            this.veo = coneB.apex();
            this.wp = coneS.position().z();
            this.wo = coneB.position().z();
            this.p2o = s2b;
            this.o2p = b2s;
            this.phip = phiS;
            this.alpp = coneS.semiAngle();
            this.cosp = Math.cos(coneS.semiAngle());
            this.coso = Math.cos(coneB.semiAngle());
            this.tanp = Math.tan(coneS.semiAngle());
            this.tano = Math.tan(coneB.semiAngle());
        }
    }

    /*
     * �Е�̉~??�̒��_���¤���̉~??�̋Ȗ�?�ɂ���?�?��̌����?�߂�?B
     *
     * @param doExchange �~??��귂��邩�ǂ����̃t���O
     * @return                 ����⠂�킷�N���X
     * @see #InterferenceOfVertexIsOnTheOther
     */
    private InterferenceOfVertexIsOnTheOther vertexIsOnTheOther(
            boolean doExchange) {
        /*
         * parameterize one cone
         */
        setParametarizedCone((Math.abs(phiS - coneS.semiAngle()) < aTol) ||
                (Math.abs((Math.PI - phiS) - coneS.semiAngle()) < aTol));

        /*
         * set temporaly local axes
         */
        Vector3D cu;

        /*
         * set temporaly local axes
         */
        Vector3D cv;

        /*
         * set temporaly local axes
         */
        Vector3D cw;
        cw = wp;
        cv = cw.crossProduct(p2o);

        if ((cv.length() / dist) < Math.sin(aTol)) {
            /*
             * cw & p2o are parallel
             */
            cv = cw.crossProduct(wo);
        }

        cu = (cv.crossProduct(cw)).unitized();
        cv = cv.unitized();

        /*
         * the value of t
         */
        double[] azero = aZero(cp, co, vep, cu, cw, wo);
        int naz = azero.length;
        double[] bzero = bZero(cp, co, vep, cu, cw, veo, wo);
        int nbz = bzero.length;

        Polyline3D[] res = null;
        Point3D res2 = null;

        if ((Math.abs(phip - alpp) < aTol) ||
                (Math.abs((Math.PI - phip) - alpp) < aTol)) {
            /*
             * the other vertex is also on the cone --  some polyline
             */
            double[] t = new double[6];
            double t1 = 0.0;
            int count1;
            int count2;

            for (count1 = 0, count2 = 2; count1 < naz; count1++) {
                if (Math.abs(bzero[0] - azero[count1]) < aTol) {
                    t1 = bzero[0];
                    t[0] = bzero[1];
                } else if (Math.abs(bzero[1] - azero[count1]) < aTol) {
                    t1 = bzero[1];
                    t[0] = bzero[0];
                } else {
                    t[count2++] = azero[count1];
                }
            }

            Vector3D evec = (wp.project(p2o)).unitized();
            t[1] = Math.acos(evec.dotProduct(cu));

            int j0 = 0;
            int jn = count2 - 1;
            GeometryUtils.sortDoubleArray(t, j0, jn);

            if (t1 < t[0]) {
                t1 += GeometryUtils.PI2;
            }

            t[count2] = t[0] + GeometryUtils.PI2;

            Vector polvec = new Vector();

            for (int i = 0; i < count2; i++) {
                /*
                 * check the center point of t[i] & t[i+1]
                 */
                double t2 = (t[i] + t[i + 1]) / 2.0;

                if (Math.abs(t2 - t1) < aTol) {
                    t2 = (t[i] + t2) / 2.0;
                }

                evec = uvwPointFromT(cu, cv, cw, tanp, t2);

                double s = -coefB(evec, o2p, wo, coso) / coefA(evec, wo, cosp,
                        coso);

                if (s < 0.0) {
                    continue;
                }

                Point3D epnt = vep.add(evec.multiply(s));
                evec = epnt.subtract(veo);

                double ework = evec.dotProduct(wo);

                if (ework < 0.0) {
                    continue;
                }

                double step = (t[i + 1] - t[i]) / (nst - 1);
                Vector pntvec = new Vector();

                for (int k = 0; k < nst; k++) {
                    t2 = t[i] + (k * step);

                    if (Math.abs(t2 - t1) < aTol) {
                        t2 += aTol;
                    }

                    evec = uvwPointFromT(cu, cv, cw, tanp, t2);

                    double a = coefA(evec, wo, cosp, coso);

                    if (Math.abs(a) < MachineEpsilon.DOUBLE) {
                        if (k == 0) {
                            t2 = t[i] + ((k * step) / 2.0);
                        } else {
                            t2 = t2 - ((k * step) / 2.0);
                        }

                        if (Math.abs(t2 - t1) < aTol) {
                            t2 += aTol;
                        }

                        evec = uvwPointFromT(cu, cv, cw, tanp, t2);
                        a = coefA(evec, wo, cosp, coso);
                    }

                    s = -coefB(evec, o2p, wo, coso) / a;

                    Point3D pnt = vep.add(evec.multiply(s));

                    if (pnt != null) {
                        pntvec.addElement(pnt);
                    }
                }

                Point3D[] points = new Point3D[pntvec.size()];
                pntvec.copyInto(points);

                Polyline3D pol = new Polyline3D(points);

                if (pol != null) {
                    polvec.addElement(pol);
                }
            }

            res = new Polyline3D[polvec.size()];
            polvec.copyInto(res);
        } else {
            /*
             * the other vertex is not on the cone
             */
            if (naz == 0) {
                if (nbz == 0) {
                    Vector3D evec = uvwPointFromT(cu, cv, cw, tanp, 0.0);
                    double s = -coefB(evec, o2p, wo, coso) / coefA(evec, wo,
                            cosp, coso);
                    Point3D epnt = vep.add(evec.multiply(s));
                    evec = epnt.subtract(veo);

                    if ((s < 0.0) || (evec.dotProduct(wo) < 0.0)) {
                        /*
                         * 1 point
                         */
                        res2 = vep;
                    } else {
                        /*
                         * 1 polyline & 1 point
                         */
                        double step = GeometryUtils.PI2 / (nst - 1);
                        Vector pntvec = new Vector();

                        for (int i = 0; i < nst; i++) {
                            double t = (-Math.PI) + (i * step);
                            evec = uvwPointFromT(cu, cv, cw, tanp, t);
                            s = -coefB(evec, o2p, wo, coso) / coefA(evec, wo,
                                    cosp, coso);

                            Point3D pnt = vep.add(evec.multiply(s));
                            pntvec.addElement(pnt);
                        }

                        Point3D[] points = new Point3D[pntvec.size()];
                        pntvec.copyInto(points);
                        res = new Polyline3D[1];
                        res[0] = new Polyline3D(points);

                        res2 = vep;
                    }
                } else if (nbz == 1) {
                    /*
                     * 1 polyline
                     */
                    Vector3D evec = uvwPointFromT(cu, cv, cw, tanp, 0.0);
                    double s = -coefB(evec, o2p, wo, coso) / coefA(evec, wo,
                            cosp, coso);

                    if (s < 0.0) {
                        res2 = vep;
                    } else {
                        double step = GeometryUtils.PI2 / (nst - 1);
                        Vector pntvec = new Vector();

                        for (int i = 0; i < nst; i++) {
                            double t = (-Math.PI) + (i * step);
                            evec = uvwPointFromT(cu, cv, cw, tanp, t);
                            s = -coefB(evec, o2p, wo, coso) / coefA(evec, wo,
                                    cosp, coso);
                            pntvec.addElement(vep.add(evec.multiply(s)));
                        }

                        Point3D[] points = new Point3D[pntvec.size()];
                        pntvec.copyInto(points);
                        res = new Polyline3D[1];
                        res[0] = new Polyline3D(points);
                    }
                } else {
                    /*
                     * 8 figure curve : 1 polyline
                     */
                    double[] t0 = new double[2];
                    double t1;

                    if (bzero[0] < bzero[1]) {
                        t0[0] = bzero[0];
                        t0[1] = bzero[1];
                    } else {
                        t0[0] = bzero[1];
                        t0[1] = bzero[0];
                    }

                    double t = (t0[0] + t0[1]) / 2.0;

                    Vector3D evec = uvwPointFromT(cu, cv, cw, tanp, t);
                    double s = -coefB(evec, o2p, wo, coso) / coefA(evec, wo,
                            cosp, coso);
                    double step;

                    if (s > 0.0) {
                        t1 = t0[0];
                        step = (t0[1] - t0[0]) / (nst - 1);
                    } else {
                        t1 = t0[1];
                        step = ((GeometryUtils.PI2 + t0[0]) - t0[1]) / (nst -
                                1);
                    }

                    Vector pntvec = new Vector();

                    for (int i = 0; i < nst; i++) {
                        t = t1 + (i * step);
                        evec = uvwPointFromT(cu, cv, cw, tanp, t);
                        s = -coefB(evec, o2p, wo, coso) / coefA(evec, wo, cosp,
                                coso);
                        pntvec.addElement(vep.add(evec.multiply(s)));
                    }

                    Point3D[] points = new Point3D[pntvec.size()];
                    pntvec.copyInto(points);
                    res = new Polyline3D[1];
                    res[0] = new Polyline3D(points);
                }
            } else {
                /*
                 * naz != 0 :  some polyline
                 */
                double[] t1 = new double[7];
                int count1;
                int count2;

                for (count1 = 0; count1 < naz; count1++) {
                    t1[count1] = azero[count1];
                }

                for (count2 = 0; count2 < nbz; count2++) {
                    t1[count1 + count2] = bzero[count2];
                }

                int j0 = 0;
                int jn = (naz + nbz) - 1;
                GeometryUtils.sortDoubleArray(t1, j0, jn);
                t1[naz + nbz] = t1[0] + GeometryUtils.PI2;

                Vector polyvec = new Vector();

                for (int i = 0; i < (naz + nbz); i++) {
                    /*
                     * check the center point of t1[i] & t1[i+1]
                     */
                    double t2 = (t1[i] + t1[i + 1]) / 2.0;
                    Vector3D evec = uvwPointFromT(cu, cv, cw, tanp, t2);
                    double s1 = -coefB(evec, o2p, wo, coso) / coefA(evec, wo,
                            cosp, coso);

                    if (s1 < 0.0) {
                        continue;
                    }

                    Point3D epnt = vep.add(evec.multiply(s1));
                    evec = epnt.subtract(veo);

                    double ework = evec.dotProduct(wo);

                    if (ework < 0.0) {
                        continue;
                    }

                    double step1 = (t1[i + 1] - t1[i]) / (nst - 1);
                    Vector pntvec = new Vector();

                    for (int k = 0; k < nst; k++) {
                        t2 = t1[i] + (k * step1);
                        evec = uvwPointFromT(cu, cv, cw, tanp, t2);

                        double a = coefA(evec, wo, cosp, coso);

                        if (Math.abs(a) < MachineEpsilon.DOUBLE) {
                            if (k == 0) {
                                t2 = t1[i] + (step1 / 2.0);
                            } else {
                                t2 = t2 - (step1 / 2.0);
                            }

                            evec = uvwPointFromT(cu, cv, cw, tanp, t2);
                            a = coefA(evec, wo, cosp, coso);
                        }

                        s1 = -coefB(evec, o2p, wo, coso) / a;
                        pntvec.addElement(vep.add(evec.multiply(s1)));
                    }

                    Point3D[] points = new Point3D[pntvec.size()];
                    pntvec.copyInto(points);

                    Polyline3D poly = new Polyline3D(points);

                    polyvec.addElement(poly);
                }

                res = new Polyline3D[polyvec.size()];
                polyvec.copyInto(res);

                /*
                 * if (nbz == 0) then some polyline & 1 point
                 */
                if (nbz == 0) {
                    res2 = vep;
                }
            }
        }

        return new InterferenceOfVertexIsOnTheOther(res, res2);
    }

    /*
     * �^����ꂽ�W?�����ł�����̉��Ԃ�?B
     * ���������?�?���2��?������⩂��?��ɕёւ���?B
     *
     * @param dA        ���̌W?�
     * @return                ���̉�
     */
    double[] getRootsByCoefficents(double[] dA) {
        DoublePolynomial poly = new DoublePolynomial(dA);
        double[] roots;
        double dX0;
        double dX1;

        if ((roots = GeometryPrivateUtils.getAlwaysRootsIfQuadric(poly)) == null) {
            throw new FatalException();
        }

        if (roots.length == 1) {
            dX0 = dX1 = roots[0];
        } else if (roots[0] < roots[1]) {
            dX0 = roots[1];
            dX1 = roots[0];
        } else {
            dX0 = roots[0];
            dX1 = roots[1];
        }

        if (dX0 > MY_HUGE) { // ���܂�傫�������͖���Ƃ���?B
            dX0 = dX1;
        } else if (dX1 < -MY_HUGE) {
            dX1 = dX0;
        }

        double[] dX = {dX0, dX1};

        return dX;
    }

    /*
     * �p���??[�^�������~??�̒��_���¤���̉~??�̓Ѥ�ɂ���?�?���
     * �����|�����C���Ƃ��ċ?�߂�
     *
     * @return                 ��?�ƂȂ�|�����C���̔z��
     */
    private Polyline3D[] vertexOfParametarizedConeIsInTheOther() {
        /*
         * set Parametarized Cone
         */
        setParametarizedCone((phiS < coneS.semiAngle()) ||
                ((Math.PI - phiS) < coneS.semiAngle()));

        // initialize local variables
        Vector3D cu;

        // initialize local variables
        Vector3D cv;

        // initialize local variables
        Vector3D cw;
        cw = wp;
        cv = cw.crossProduct(p2o);
        cu = (cv.crossProduct(cw)).unitized();
        cv = cv.unitized();

        // the value of t
        double[] azero = aZero(cp, co, vep, cu, cw, wo);
        int naz = azero.length;
        double[] bzero = bZero(cp, co, vep, cu, cw, veo, wo);
        int nbz = bzero.length;

        Polyline3D[] pols; // return value

        if (naz == 0) {
            /*
             * naz == 0
             */
            double[] dA = new double[3]; // coefficients

            dA[0] = coefC(o2p, wo, coso);

            double step = GeometryUtils.PI2 / (nst - 1);

            Vector pntvec = new Vector();

            for (int k = 0; k < nst; k++) {
                double t = (-Math.PI) + (k * step);
                Vector3D evec = uvwPointFromT(cu, cv, cw, tanp, t);
                dA[2] = coefA(evec, wo, cosp, coso);
                dA[1] = coefB(evec, o2p, wo, coso);

                double[] dX = getRootsByCoefficents(dA);

                Point3D pnt = vep.add(evec.multiply(dX[0]));

                if (dX[1] > (-MachineEpsilon.DOUBLE)) {
                    Vector3D evec2 = pnt.subtract(veo);

                    if (evec2.dotProduct(wo) < 0.0) {
                        pnt = vep.add(evec.multiply(dX[1]));
                    }
                }

                pntvec.addElement(pnt);
            }

            Point3D[] points = new Point3D[pntvec.size()];
            pntvec.copyInto(points);

            Polyline3D[] work = {new Polyline3D(points)};
            pols = work;
        } else {
            /*
             * naz != 0
             */
            double[] dA = new double[3];
            double[] t = new double[5];

            for (int i = 0; (i < naz) && (i < t.length); i++) {
                t[i] = azero[i];
            }

            int j0 = 0;
            int jn = naz - 1;
            GeometryUtils.sortDoubleArray(t, j0, jn);
            t[naz] = t[0] + GeometryUtils.PI2;

            dA[0] = coefC(o2p, wo, coso);

            Vector polvec = new Vector();

            for (int i = 0; i < naz; i++) {
                double t1 = (t[i] + t[i + 1]) / 2.0;
                Vector3D evec = uvwPointFromT(cu, cv, cw, tanp, t1);
                dA[2] = coefA(evec, wo, cosp, coso);
                dA[1] = coefB(evec, o2p, wo, coso);

                double[] dX = getRootsByCoefficents(dA);

                if (dX[0] > 0.0) {
                    Point3D epnt = vep.add(evec.multiply(dX[0]));
                    Vector3D evec2 = epnt.subtract(veo);
                    double ework = evec2.dotProduct(wo);

                    Vector pntvec1 = new Vector();

                    if (ework > 0.0) {
                        double step = (t[i + 1] - t[i]) / (nst - 1);

                        for (int k = 0; k < nst; k++) {
                            if (k == 0) {
                                t1 = t[i] + (0.5 * step);
                            } else if (k == (nst - 1)) {
                                t1 = t[i + 1] - (0.5 * step);
                            } else {
                                t1 = t[i] + (k * step);
                            }

                            evec = uvwPointFromT(cu, cv, cw, tanp, t1);
                            dA[2] = coefA(evec, wo, cosp, coso);
                            dA[1] = coefB(evec, o2p, wo, coso);

                            double[] dX2 = getRootsByCoefficents(dA);

                            Point3D pnt = vep.add(evec.multiply(dX2[0]));

                            if (dX2[1] > (-MachineEpsilon.DOUBLE)) {
                                evec2 = pnt.subtract(veo);

                                if (evec2.dotProduct(wo) < 0.0) {
                                    pnt = vep.add(evec.multiply(dX2[1]));
                                }
                            }

                            if (pnt != null) {
                                pntvec1.addElement(pnt);
                            }
                        }
                    }

                    Point3D[] points = new Point3D[pntvec1.size()];

                    if (points.length > 0) {
                        pntvec1.copyInto(points);

                        Polyline3D pol = new Polyline3D(points);
                        polvec.addElement(pol);
                    }
                }

                if (dX[1] > 0.0) {
                    Point3D epnt = vep.add(evec.multiply(dX[1]));
                    Vector3D evec2 = epnt.subtract(veo);
                    double ework = evec2.dotProduct(wo);

                    Vector pntvec2 = new Vector();

                    if (ework > 0.0) {
                        double step = (t[i + 1] - t[i]) / (nst - 1);

                        for (int k = 0; k < nst; k++) {
                            if (k == 0) {
                                t1 = t[i] + (0.5 * step);
                            } else if (k == (nst - 1)) {
                                t1 = t[i + 1] - (0.5 * step);
                            } else {
                                t1 = t[i] + (k * step);
                            }

                            evec = uvwPointFromT(cu, cv, cw, tanp, t1);
                            dA[2] = coefA(evec, wo, cosp, coso);
                            dA[1] = coefB(evec, o2p, wo, coso);

                            double[] dX2 = getRootsByCoefficents(dA);

                            Point3D pnt = vep.add(evec.multiply(dX2[1]));

                            if (dX2[0] > (-MachineEpsilon.DOUBLE)) {
                                // Maybe this 'if' statement is unnecessary.
                                evec2 = pnt.subtract(veo);

                                if (evec2.dotProduct(wo) < 0.0) {
                                    pnt = vep.add(evec.multiply(dX2[0]));
                                }
                            }

                            pntvec2.addElement(pnt);
                        }

                        Point3D[] points = new Point3D[pntvec2.size()];
                        pntvec2.copyInto(points);

                        Polyline3D pol = new Polyline3D(points);
                        polvec.addElement(pol);
                    }
                }
            }

            Polyline3D[] work = new Polyline3D[polvec.size()];
            polvec.copyInto(work);
            pols = work;
        }

        return pols;
    }

    /*
     * 2�̃|�����C����Ȃ��_���?ڑ�����
     *
     * @param resp        �|�����C����Ȃ��_��
     * @param resc        �|�����C����Ȃ��_��
     * @return                2�̃|�����C����?ڑ������|�����C����Ȃ��_��
     */
    private Point3D[] connect2Polyline(Point3D[] resp, Point3D[] resc) {
        // if (true) return null;
        int i = 0;

        Point3D[] resn = new Point3D[(resp.length + resc.length) - 1];

        /*
         *  case 1 : <---- (resp) + ----> (resc)
         */
        if (resp[0].identical(resc[0])) {
            // copy resp to resn
            for (i = resp.length - 1; i > 0; i--) {
                resn[resp.length - (i + 1)] = resp[i];
            }

            // make middle point
            resn[resp.length - 1] = resp[0].midPoint(resc[0]);

            // copy resc to resn
            for (i = 1; i < resc.length; i++) {
                resn[(resp.length - 1) + i] = resc[i];
            }

            return resn;
        }

        /*
         *  case 2 : <---- (resp) + <---- (resc)
         */
        if (resp[0].identical(resc[resc.length - 1])) {
            // copy resp to resn
            for (i = resp.length - 1; i > 0; i--) {
                resn[resp.length - (i + 1)] = resp[i];
            }

            // make middle point
            resn[resp.length - 1] = resp[0].midPoint(resc[resc.length - 1]);

            // copy resc to resn
            for (i = resc.length - 2; i >= 0; i--) {
                resn[((resp.length + resc.length) - 2) - i] = resc[i];
            }

            return resn;
        }

        /*
         *  case 3 : ----> (resp) + ----> (resc)
         */
        if (resp[resp.length - 1].identical(resc[0])) {
            for (i = 0; i < (resp.length - 1); i++) {
                resn[i] = resp[i];
            }

            // make middle point
            resn[resp.length - 1] = resp[resp.length - 1].midPoint(resc[0]);

            // copy resc to resn
            for (i = 1; i < resc.length; i++) {
                resn[(resp.length - 1) + i] = resc[i];
            }

            return resn;
        }

        /*
         * case 4 : ----> (resp) + <---- (resc)
         */
        if (resp[resp.length - 1].identical(resc[resc.length - 1])) {
            for (i = 0; i < (resp.length - 1); i++) {
                resn[resp.length - (i + 2)] = resp[i];
            }

            // make middle point
            resn[resp.length - 1] = resp[resp.length - 1].midPoint(resc[resc.length -
                    1]);

            // copy resc to resn
            for (i = resc.length - 2; i >= 0; i--) {
                resn[((resp.length + resc.length) - 2) - i] = resc[i];
            }

            return resn;
        }

        return null; // must not be here
    }

    /*
     * �C���^?[�o���̃^�C�v�`�F�b�N
     *
     * @param        t1        �p���??[�^
     * @param        t2        �p���??[�^
     * @param        azero        ��?��̔z��
     * @return                ��?�̔z��
     */
    private int checkIntervalType(double t1, double t2, double[] azero) {
        int naz = azero.length;
        int type = 0;

        for (int i = 0; i < naz; i++)
            if (Math.abs(t1 - azero[i]) < MachineEpsilon.DOUBLE) {
                type++;

                break;
            }

        for (int i = 0; i < naz; i++) {
            if (Math.abs(t2 - azero[i]) < MachineEpsilon.DOUBLE) {
                type++;

                break;
            }
        }

        return type;
    }

    /*
     * �ǂ���̉~??�̒��_��¤���̉~??�̊O���ɂ���?�?��̌����
     * �|�����C���Ƃ��ċ?�߂�
     *
     * @param doExchange �~??��귂��邩�ǂ����̃t���O
     * @return                 ��?�̃|�����C���̔z��
     * @see IndefiniteSolutionException
     */
    private Polyline3D[] bothVerticesAreOutOfTheOther() {
        /*
         * Parametarized Bigger Cone
         */
        setParametarizedCone(true);

        /*
         * initialaize local variables
         */
        double[] t = new double[10];
        double[] dA = new double[3]; /* coefficients of polinomial */
        Point3D[][] res = new Point3D[10][];
        int myNst = (2 * nst) - 1;
        int intvlType = 0;
        int prevIntvlType = 0;

        /*
         * set temporaly local axes
         */
        Vector3D cu;

        /*
         * set temporaly local axes
         */
        Vector3D cv;

        /*
         * set temporaly local axes
         */
        Vector3D cw;
        cw = wp;
        cv = cw.crossProduct(p2o);
        cu = (cv.crossProduct(cw)).unitized();
        cv = cv.unitized();

        Vector3D uo;
        Vector3D vo;
        vo = wo.crossProduct(p2o).reverse();
        uo = vo.crossProduct(wo).unitized();
        uo = uo.unitized();
        vo = vo.unitized();

        int num = 0;
        Vector3D tmp1 = wo.multiply(tano);
        double r = tmp1.dotProduct(o2p) / uo.dotProduct(o2p);
        Vector3D tmp2 = tmp1.subtract(uo.multiply(r));

        if (Math.abs(r) < (1.0 + MachineEpsilon.DOUBLE)) {
            if (r > 1.0) {
                r = 1.0;
            }

            if (r < (-1.0)) {
                r = (-1.0);
            }

            double r2 = Math.sqrt(1.0 - (r * r));

            Vector3D[] nor = new Vector3D[2];
            nor[0] = tmp2.add(vo.multiply(r2));
            nor[1] = tmp2.subtract(vo.multiply(r2));

            for (int i = 0; i < 2; i++) {
                // �~?? cp �̒��_ vep �촓_�Ƃ��镽�ʂ�?�?�
                Plane3D plane = new Plane3D(new Axis2Placement3D(vep, nor[i],
                        o2p));
                SurfaceSurfaceInterference3D[] ints;

                // �~?? cp ��?��?�?��������ʂ̌����?�߂�?B
                // �?�܂����͈ȉ���2�_��K��������?B
                //   (1) ?��Ȃ��ƂҸ�_ vep �Ō���?B
                //   (2) ����͒��_ vep ��ʂ钼?�ɂȂ�?B
                try {
                    ints = plane.intersect(cp);
                } catch (IndefiniteSolutionException e) {
                    // should never be occured by (1)
                    throw new FatalException();
                }

                // ���肪�_�ɂȂ�?�?��͖���
                if (ints[0] instanceof IntersectionPoint3D) {
                    continue;
                }

                for (int j = 0; j < ints.length; j++) {
                    Line3D line = (Line3D) ((IntersectionCurve3D) ints[j]).curve3d();
                    Vector3D evec = line.dir().project(cw);
                    evec = evec.unitized();
                    t[num] = cu.angleWith(evec, cw);
                    num++;
                }
            }
        }

        /*
         * exec private method aZero()
         */
        double[] azero = aZero(cp, co, vep, cu, cw, wo);
        int naz = azero.length;

        for (int i = 0; i < naz; i++) {
            t[num++] = azero[i];
        }

        /*
         * make coefC
         */
        if (num > 0) {
            int j0 = 0;
            int jn = num - 1;
            GeometryUtils.sortDoubleArray(t, j0, jn);
            t[num] = t[0] + GeometryUtils.PI2;

            for (int j = 0; j < naz; j++) {
                if (Math.abs(t[0] - azero[j]) < MachineEpsilon.DOUBLE) {
                    double[] temp = new double[naz + 1];

                    for (int jj = 0; jj < naz; jj++) {
                        temp[jj] = azero[jj];
                    }

                    temp[naz] = azero[j] + GeometryUtils.PI2;
                    azero = temp;
                    naz = azero.length;

                    break;
                }
            }

            // exec private method checkIntervalType()
            prevIntvlType = checkIntervalType(t[num - 1], t[num], azero);

            dA[0] = coefC(o2p, wo, coso);
        }

        /*
         * loop by "num"
         */
        int n = 0;

        for (int i = 0; i < num; i++) {
            // make coefA, coefB
            double t0 = (t[i] + t[i + 1]) / 2.0;
            Vector3D evec = uvwPointFromT(cu, cv, cw, tanp, t0);
            dA[2] = coefA(evec, wo, cosp, coso);
            dA[1] = coefB(evec, o2p, wo, coso);

            // solve polynomial
            DoublePolynomial poly = new DoublePolynomial(dA);
            double dX0;
            double dX1;
            double[] dX = GeometryPrivateUtils.getRootsIfQuadric(poly); // roots

            if (dX == null) {
                throw new FatalException();
            }

            if (dX.length == 0) {
                prevIntvlType = 0;

                continue;
            }

            if (dX.length == 1) {
                dX0 = dX1 = dX[0];
            } else if (dX[0] > dX[1]) {
                dX0 = dX[0];
                dX1 = dX[1];
            } else {
                dX0 = dX[1];
                dX1 = dX[0];
            }

            if (dX0 < 0.0) {
                prevIntvlType = 0;

                continue;
            }

            Point3D epnt = vep.add(evec.multiply(dX0));
            Vector3D evec2 = epnt.subtract(veo);

            if (evec2.dotProduct(wo) < 0.0) {
                prevIntvlType = 0;

                continue;
            }

            // check interval type
            intvlType = checkIntervalType(t[i], t[i + 1], azero);

            // make array of points
            Point3D[] points;

            if (intvlType == 0) {
                points = new Point3D[myNst];
            } else if (intvlType == 1) {
                points = new Point3D[myNst - 1];
            } else { // intvlType == 2
                points = new Point3D[nst];
            }

            double step = (t[i + 1] - t[i]) / (nst - 1);
            int nsetted = 0;

            for (int j = 0; j < (nst - 1); j++) {
                // make coefA, coefB
                t0 = t[i] + (j * step);
                evec = uvwPointFromT(cu, cv, cw, tanp, t0);
                dA[2] = coefA(evec, wo, cosp, coso);
                dA[1] = coefB(evec, o2p, wo, coso);

                // solve polynomial
                dX = getRootsByCoefficents(dA);

                switch (intvlType) {
                    case 0:
                        points[j] = vep.add(evec.multiply(dX[0]));
                        points[myNst - 1 - j] = vep.add(evec.multiply(dX[1]));

                        break;

                    case 1:

                        if (prevIntvlType == 0) {
                            points[(nst - 1) - j - 1] = vep.add(evec.multiply(dX[0]));
                            points[((nst - 1) + j) - 1] = vep.add(evec.multiply(
                                    dX[1]));
                        } else {
                            points[j] = vep.add(evec.multiply(dX[1]));

                            if (j > 0) {
                                points[myNst - 1 - j] = vep.add(evec.multiply(dX[0]));
                            }
                        }

                        break;

                    case 2:

                        if (dX[0] > 0.0) {
                            points[nsetted] = vep.add(evec.multiply(dX[0]));
                            nsetted++;
                        }

                        break;
                }
            }

            /*
             * make coefA, coefB
             */
            t0 = t[i + 1];
            evec = uvwPointFromT(cu, cv, cw, tanp, t0);
            dA[2] = coefA(evec, wo, cosp, coso);
            dA[1] = coefB(evec, o2p, wo, coso);

            dX = getRootsByCoefficents(dA);

            switch (intvlType) {
                case 0:
                    points[nst - 1] = vep.add(evec.multiply(dX[0]));

                    break;

                case 1:

                    if (prevIntvlType == 0) {
                        points[myNst - 2] = vep.add(evec.multiply(dX[0]));
                    } else {
                        points[nst - 1] = vep.add(evec.multiply(dX[0]));
                    }

                    break;

                case 2:

                    if (dX[0] > 0.0) {
                        points[nsetted] = vep.add(evec.multiply(dX[0]));
                    }

                    if (nsetted != (nst - 1)) {
                        Point3D[] points2 = new Point3D[nsetted];

                        for (int jj = 0; jj < nsetted; jj++)
                            points2[jj] = points[jj];

                        points = points2;
                    }

                    break;
            }

            res[n] = points;

            /*
             * connect2Polyline
             */
            Point3D[] newRes;

            if ((i > 0) && (intvlType != 0) && (prevIntvlType != 0)) {
                if ((newRes = connect2Polyline(res[n - 1], res[n])) != null) {
                    res[n - 1] = newRes;
                } else {
                    n++;
                }
            } else if ((i == (num - 1)) && (intvlType != 0) && (n > 0)) {
                if ((newRes = connect2Polyline(res[0], res[n])) != null) {
                    res[0] = newRes;
                } else {
                    n++;
                }
            } else {
                n++;
            }

            prevIntvlType = intvlType;
        }

        Polyline3D[] pols = new Polyline3D[n];

        for (int i = 0; i < n; i++) {
            pols[i] = new Polyline3D(res[i]);
        }

        return pols;
    }

    /*
     * �ǂ��炩�̒��_���¤�Е�̋Ȗ�?�ɂ���?�?��̉~??���m�̌�?��?�߂�
     *
     * @return                 ��?�̔z��
     * @see IndefiniteSolutionException
     */
    SurfaceSurfaceInterference3D[] getInterferenceVertexIsOnTheOther(
            boolean doExchange) throws IndefiniteSolutionException {
        /*
         * �t���̉~??�𓾂�
         *     (geomA: ?���, geomB: ?���)
         *     (geomA: ?���, geomB: �t��)
         *     (geomA: �t��, geomB: ?���)
         *     (geomA: �t��, geomB: �t��)
         * �̑g?��킹�̌�?��v�Z����?B����������ȊO�̓N�P?[�X�ł�
         * �K�v�Ȃ�?B
         *
         * (GHL �ł͉~??�͒��_����݂ĕБ������Ȃ���,STEP �̒�`�ł�
         *  ���_�̗����ɉ~??������悤�ɂȂBĂ���?B���̃N���X�̌�?�
         *  �̌v�Z�� GHL �̎�@��g�BĂ���̂�(?���,?���)�̌v�Z
         *  �����ł��Ȃ�?B���̂��߉~??���̂�t��ɂ��Čv�Z���Ă���)
         */
        IntsConCon3D doObj;
        InterferenceOfVertexIsOnTheOther[] res = new InterferenceOfVertexIsOnTheOther[4];

        // geomA: ?���, geomB: ?���
        res[0] = vertexIsOnTheOther(doExchange);

        // geomA: ?���, geomB: �t��
        doObj = new IntsConCon3D(geomA, geomB.getReverse());
        res[1] = doObj.vertexIsOnTheOther(doExchange);

        // geomA: �t��, geomB: ?���
        doObj = new IntsConCon3D(geomA.getReverse(), geomB);
        res[2] = doObj.vertexIsOnTheOther(doExchange);

        // geomA: �t��, geomB: �t��
        doObj = new IntsConCon3D(geomA.getReverse(), geomB.getReverse());
        res[3] = doObj.vertexIsOnTheOther(doExchange);

        Vector intfvec = new Vector();
        Vector intspnt = new Vector();

        for (int i = 0; i < res.length; i++) {
            if (res[i] != null) {
                if (res[i].ints() != null) {
                    double[] geomAParams = geomA.pointToParameter(res[i].ints());
                    double[] geomBParams = geomB.pointToParameter(res[i].ints());
                    IntersectionPoint3D intsPoint = new IntersectionPoint3D(geomA,
                            geomAParams[0], geomAParams[1], geomB,
                            geomBParams[0], geomBParams[1],
                            GeometryElement.doCheckDebug);
                    intspnt.addElement(intsPoint);
                }

                if (res[i].pols() != null) {
                    Polyline3D[] pols = res[i].pols();

                    for (int j = 0; j < pols.length; j++) {
                        if (pols[j] != null) {
                            SurfaceSurfaceInterference3D intsPoly = geomA.curveToIntersectionCurve(pols[j],
                                    geomB, doExchange);
                            intfvec.addElement(intsPoly);
                        }
                    }
                }
            }
        }

        // ��_������?�?�������_��1�����B���?B
        // ��_����?�����?�?���?d�����Ă���͂��Ȃ̂�
        // ?�?��̌�_�ȊO�͖�������?B
        try {
            IntersectionPoint3D pnt = (IntersectionPoint3D) intspnt.firstElement();
            intfvec.addElement(pnt);
        } catch (NoSuchElementException e) {
            // ��_���Ȃ�?�?��͉��µ�Ȃ�
        }

        SurfaceSurfaceInterference3D[] intf = new SurfaceSurfaceInterference3D[intfvec.size()];
        intfvec.copyInto(intf);

        return intf;
    }

    /*
     * �Б��̉~??���m�̌�?��|�����C���Ƃ��ċ?�߂�
     *
     * @return                 ��?�ƂȂ�|�����C���̔z��
     * @see IndefiniteSolutionException
     */
    Polyline3D[] getInterferenceVertexIsNotOnTheOtherHalfSide(
            boolean doExchange) throws IndefiniteSolutionException {
        /*
         * the vertex of one is not on each other
         */
        Polyline3D[] pols;

        if ((phiS < coneS.semiAngle()) ||
                ((Math.PI - phiS) < coneS.semiAngle()) ||
                (phiB < coneB.semiAngle()) ||
                ((Math.PI - phiB) < coneB.semiAngle())) {
            if ((phiS > coneS.semiAngle()) && (phiB > coneB.semiAngle()) &&
                    (theta > (coneS.semiAngle() + coneB.semiAngle()))) {
                /*
                 * no intersection
                 */
                pols = new Polyline3D[0];
            } else {
                /*
                 * the vertex of the parameterized cone is in the other cone
                 */
                pols = vertexOfParametarizedConeIsInTheOther();
            }
        } else {
            /*
             * both vertices are out of the other cone
             */
            pols = bothVerticesAreOutOfTheOther();
        }

        return pols;
    }

    /*
     * ���_������̋Ȗ�?�ɖ���?�?��̉~??���m�̌�?��?�߂�
     *
     * @return                 ��?�̔z��
     * @see IndefiniteSolutionException
     */
    SurfaceSurfaceInterference3D[] getInterferenceVertexIsNotOnTheOther(
            boolean doExchange) throws IndefiniteSolutionException {
        /*
         * �t���̉~??�𓾂�
         *     (geomA: ?���, geomB: ?���)
         *     (geomA: ?���, geomB: �t��)
         *     (geomA: �t��, geomB: ?���)
         *     (geomA: �t��, geomB: �t��)
         * �̑g?��킹�̌�?��v�Z����?B����������ȊO�̓N�P?[�X�ł�
         * �K�v�Ȃ�?B
         *
         * (GHL �ł͉~??�͒��_����݂ĕБ������Ȃ���,STEP �̒�`�ł�
         *  ���_�̗����ɉ~??������悤�ɂȂBĂ���?B���̃N���X�̌�?�
         *  �̌v�Z�� GHL �̎�@��g�BĂ���̂�(?���,?���)�̌v�Z
         *  �����ł��Ȃ�?B���̂��߉~??���̂�t��ɂ��Čv�Z���Ă���)
         */
        IntsConCon3D doObj;
        Polyline3D[][] pols = new Polyline3D[4][];

        // geomA: ?���, geomB: ?���
        pols[0] = getInterferenceVertexIsNotOnTheOtherHalfSide(doExchange);

        // geomA: ?���, geomB: �t��
        doObj = new IntsConCon3D(geomA, geomB.getReverse());
        pols[1] = doObj.getInterferenceVertexIsNotOnTheOtherHalfSide(doExchange);

        // geomA: �t��, geomB: ?���
        doObj = new IntsConCon3D(geomA.getReverse(), geomB);
        pols[2] = doObj.getInterferenceVertexIsNotOnTheOtherHalfSide(doExchange);

        // geomA: �t��, geomB: �t��
        doObj = new IntsConCon3D(geomA.getReverse(), geomB.getReverse());
        pols[3] = doObj.getInterferenceVertexIsNotOnTheOtherHalfSide(doExchange);

        Vector intfvec = new Vector();

        for (int i = 0; i < pols.length; i++) {
            for (int j = 0; j < pols[i].length; j++) {
                if (pols[i][j] != null) {
                    SurfaceSurfaceInterference3D intsPoly = geomA.curveToIntersectionCurve(pols[i][j],
                            geomB, doExchange);
                    intfvec.addElement(intsPoly);
                }
            }
        }

        SurfaceSurfaceInterference3D[] intf = new SurfaceSurfaceInterference3D[intfvec.size()];
        intfvec.copyInto(intf);

        return intf;
    }

    /*
     * �~??���m�̌�?��?�߂�
     *
     * @param doExchange �~??��귂��邩�ǂ����̃t���O
     * @return                 ��?�̔z��
     * @see IndefiniteSolutionException
     */
    SurfaceSurfaceInterference3D[] getInterference(boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] intf;

        /*
         * get intersections
         */
        Line3D axisBZ = new Line3D(coneB.position().location(),
                coneB.position().z());
        Line3D axisSZ = new Line3D(coneS.position().location(),
                coneS.position().z());

        if (coneB.position().z().parallelDirection(coneS.position().z()) &&
                coneS.position().location().isOn(axisBZ) &&
                coneB.position().location().isOn(axisSZ)) {
            /*
             * [special case 1]
             * axes are overlap
             */
            intf = axesAreOverlap(doExchange);

            return intf;
        }

        if ((dist > dTol) &&
                ((Math.abs(phiS - coneS.semiAngle()) < aTol) ||
                        (Math.abs((Math.PI - phiS) - coneS.semiAngle()) < aTol)) &&
                ((Math.abs(phiB - coneB.semiAngle()) < aTol) ||
                        (Math.abs((Math.PI - phiB) - coneB.semiAngle()) < aTol)) &&
                areCoplanar(coneS.position().z(), coneB.position().z(), s2b)) {
            /*
             * [special case 2]
             * the cones share a ruling tangentially
             */
            intf = rulingTangent(doExchange);
        } else if (dist < dTol) {
            /*
             * [special case 3]
             * at most 4 lines
             */
            intf = fourLines(doExchange);
        } else {
            /*
             * [general cases]
             *
             * the vertex of one is on the other or not
             */
            if (((Math.abs(phiS - coneS.semiAngle()) < aTol) ||
                    (Math.abs((Math.PI - phiS) - coneS.semiAngle()) < aTol)) ||
                    ((Math.abs(phiB - coneB.semiAngle()) < aTol) ||
                            (Math.abs((Math.PI - phiB) - coneB.semiAngle()) < aTol))) {
                intf = getInterferenceVertexIsOnTheOther(doExchange);
            } else {
                intf = getInterferenceVertexIsNotOnTheOther(doExchange);
            }
        }

        return intf;
    }

    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     *
     * @param argv DOCUMENT ME!
     * @throws IndefiniteSolutionException DOCUMENT ME!
     */
    public static void main(String[] argv) throws IndefiniteSolutionException {
        // both vertices are out of the other case
        System.out.println("\n>>> [both vertices are out of the other case]\n");

        ConicalSurface3D coneA;
        ConicalSurface3D coneB;
        coneA = new ConicalSurface3D(Point3D.origin, Vector3D.xUnitVector, 0.8,
                0.4);
        coneB = new ConicalSurface3D(Point3D.origin,
                Vector3D.of(1.0, 0.8, 0.0), 1.0, 0.5);

        SurfaceSurfaceInterference3D[] intf = coneA.intersect(coneB);
        System.out.print("both vertices are out of the other case: ");
        System.out.print("interference number is " + intf.length + "\n");

        // verteces is in the other case
        System.out.println("\n>>> [verteces is in the other case]\n");
        coneA = new ConicalSurface3D(Point3D.of(0.0, -0.8, 0.0),
                Vector3D.xUnitVector, 1.0, 0.5);

        coneB = new ConicalSurface3D(Point3D.of(-1.8, 0.0, 0.0),
                Vector3D.yUnitVector, 1.0, 0.5);
        intf = coneA.intersect(coneB);
        System.out.print("verteces is in the other case: ");
        System.out.print("interference number is " + intf.length + "\n");
    }

    /*
     *
     */
    private class InterferenceOfVertexIsOnTheOther {
        /**
         * DOCUMENT ME!
         */
        private Polyline3D[] pols;

        /**
         * DOCUMENT ME!
         */
        private Point3D ints;

        /**
         * Creates a new InterferenceOfVertexIsOnTheOther object.
         *
         * @param pols DOCUMENT ME!
         * @param ints DOCUMENT ME!
         */
        InterferenceOfVertexIsOnTheOther(Polyline3D[] pols, Point3D ints) {
            this.pols = pols;
            this.ints = ints;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        Polyline3D[] pols() {
            return this.pols;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        Point3D ints() {
            return this.ints;
        }
    }
}
