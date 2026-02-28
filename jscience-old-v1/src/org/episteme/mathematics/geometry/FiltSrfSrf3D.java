/*
 * 3D 2�ȖʊԂ̃t�B���b�g��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FiltSrfSrf3D.java,v 1.3 2007-10-21 21:08:11 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.ArrayMathUtils;
import org.episteme.mathematics.analysis.PrimitiveMappingND;

/**
 * 3D 2�ȖʊԂ̃t�B���b�g��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:11 $
 */
final class FiltSrfSrf3D {
    static boolean debug = false;

    /**
     * �?�߂�ꂽ�t�B���b�g�̃��X�g
     *
     * @see FilletObjectList
     */
    private FilletObjectList fillets;

    /**
     * ���z�I�t�Z�b�g�Ȗ� A ��?��
     * <p/>
     * sideA��WhichSide.BOTH�ł���Ε\�����ꂼ��̕���2��?A
     * ����ȊO��?�?��͎w�肳�ꂽ����1��?�?������?B
     * </p>
     *
     * @see SurfaceInfo
     * @see WhichSide
     */
    private SurfaceInfo[] infoA;

    /**
     * ���z�I�t�Z�b�g�Ȗ� B ��?��
     * <p/>
     * sideA��WhichSide.BOTH�ł���Ε\�����ꂼ��̕���2��?A
     * ����ȊO��?�?��͎w�肳�ꂽ����1��?�?������?B
     * </p>
     *
     * @see SurfaceInfo
     * @see WhichSide
     */
    private SurfaceInfo[] infoB;

    /**
     * �t�B���b�g���a
     */
    private double radius;

    static final int A_U_FIX = 0;
    static final int A_V_FIX = 1;
    static final int B_U_FIX = 2;
    static final int B_V_FIX = 3;

    /**
     * �I�u�W�F�N�g��?\�z����
     *
     * @param surfaceA �Ȗ� A
     * @param uSectA   �Ȗ� A �̑�?ۂƂȂ�U���̃p���??[�^���
     * @param vSectA   �Ȗ� A �̑�?ۂƂȂ�V���̃p���??[�^���
     * @param sideA    �Ȗ� A �̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
     * @param surfaceB �Ȗ� B
     * @param uSectB   �Ȗ� B �̑�?ۂƂȂ�U���̃p���??[�^���
     * @param vSectB   �Ȗ� B �̑�?ۂƂȂ�V���̃p���??[�^���
     * @param sideB    �Ȗ� B �̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
     * @param raidus   �t�B���b�g�̔��a
     * @see ParametricSurface3D
     * @see ParameterSection
     * @see WhichSide
     */
    private FiltSrfSrf3D(ParametricSurface3D surfaceA,
                         ParameterSection uSectA,
                         ParameterSection vSectA,
                         int sideA,
                         ParametricSurface3D surfaceB,
                         ParameterSection uSectB,
                         ParameterSection vSectB,
                         int sideB,
                         double radius) {
        super();

        surfaceA.checkUValidity(uSectA);
        surfaceA.checkVValidity(vSectA);
        surfaceB.checkUValidity(uSectB);
        surfaceB.checkVValidity(vSectB);

        double tol = surfaceA.getToleranceForDistance();
        if (radius < tol)
            throw new InvalidArgumentValueException();

        fillets = new FilletObjectList();
        this.radius = radius;
        infoA = getInfo(surfaceA, uSectA, vSectA, sideA);
        infoB = getInfo(surfaceB, uSectB, vSectB, sideB);
    }

    /**
     * �I�t�Z�b�g�Ȗ�(�ߎ���)��?�߂�
     * (�{����ParametricSurface3D���?�ׂ�)
     *
     * @param surface  �I�t�Z�b�g����Ȗ�
     * @param uSection �I�t�Z�b�g����U���̋��
     * @param vSection �I�t�Z�b�g����V���̋��
     * @param side     �I�t�Z�b�g������
     * @param radius   �I�t�Z�b�g���鋗��
     */
    private ParametricSurface3D offsetSurface(ParametricSurface3D surface,
                                              ParameterSection uSection,
                                              ParameterSection vSection,
                                              int side,
                                              double radius) {
        Axis2Placement3D a2p;
        switch (surface.type()) {
            case ParametricSurface3D.PLANE_3D:
                /*
                * ���ʂ�?�?��͕�?s�ړ���������(��g���~���O�������)�ƂȂ�
                */
            {
                Plane3D pln = (Plane3D) surface;
                Vector3D enrm = pln.normalVector(0.0, 0.0);
                if (side == WhichSide.BACK)
                    enrm = enrm.reverse();
                Point3D pnt = pln.position().location().add(enrm.multiply(radius));
                a2p = new Axis2Placement3D(pnt, pln.position().z(), pln.position().x());
                pln = new Plane3D(a2p);

                return new RectangularTrimmedSurface3D(pln, uSection, vSection);
            }
            case ParametricSurface3D.SPHERICAL_SURFACE_3D:
                /*
                * ����?�?��͔��a��t�B���b�g���a������?X�������(��g���~���O�������)�ƂȂ�
                */
            {
                SphericalSurface3D sph = (SphericalSurface3D) surface;
                double sRadius;
                if (side == WhichSide.FRONT)
                    sRadius = sph.radius() + radius;
                else {
                    sRadius = sph.radius() - radius;
                    if (sRadius < 0.0) {
                        break;        // do as Bspline
                    }
                }
                if (sRadius < surface.getToleranceForDistance())    // reduced into a point
                    break;        // ???
                a2p = new Axis2Placement3D(sph.position().location(),
                        sph.position().z(), sph.position().x());
                sph = new SphericalSurface3D(a2p, sRadius);

                return new RectangularTrimmedSurface3D(sph, uSection, vSection);
            }
            case ParametricSurface3D.CYLINDRICAL_SURFACE_3D:
                /*
                * �~����?�?��͔��a��t�B���b�g���a������?X�������(��g���~���O�������)�ƂȂ�
                */
            {
                CylindricalSurface3D cyl = (CylindricalSurface3D) surface;
                Vector3D x = cyl.position().x();
                double sRadius;
                if (side == WhichSide.FRONT)
                    sRadius = cyl.radius() + radius;
                else {
                    sRadius = cyl.radius() - radius;
                    if (sRadius < 0.0) {
                        x = x.reverse();
                        sRadius = -sRadius;
                    }
                }
                if (sRadius < surface.getToleranceForDistance())    // reduced into a line
                    break;        // ???
                a2p = new Axis2Placement3D(cyl.position().location(), cyl.position().z(), x);
                cyl = new CylindricalSurface3D(a2p, sRadius);

                return new RectangularTrimmedSurface3D(cyl, uSection, vSection);
            }
            case ParametricSurface3D.CONICAL_SURFACE_3D:
                /*
                * �~??��?�?��͎����Ɉړ������~??(��g���~���O�������)�ƂȂ�
                */
            {
                ConicalSurface3D con = (ConicalSurface3D) surface;
                double sinCon = Math.sin(con.semiAngle());
                double cosCon = Math.cos(con.semiAngle());
                double moveZ = radius * sinCon;
                if (side == WhichSide.FRONT)
                    moveZ = -moveZ;
                Point3D loc = con.position().location().add(con.position().z().multiply(moveZ));
                a2p = new Axis2Placement3D(loc, con.position().z(), con.position().x());
                double oftRadius = con.radius() + radius * cosCon;
                con = new ConicalSurface3D(a2p, oftRadius, con.semiAngle());

                return new RectangularTrimmedSurface3D(con, uSection, vSection);
            }
            case ParametricSurface3D.RECTANGULAR_TRIMMED_SURFACE_3D:
                /*
                * ��`�L�Ȗʂ�?�?��̓x?[�X�Ȗʂ�I�t�Z�b�g�������(��g���~���O�������)�ƂȂ�
                */
            {
                RectangularTrimmedSurface3D rts = (RectangularTrimmedSurface3D) surface;
                uSection = rts.toBasisUParameter(uSection);
                vSection = rts.toBasisVParameter(vSection);
                if (rts.uSense() != rts.vSense())
                    side = WhichSide.reverse(side);

                return offsetSurface(rts.basisSurface(), uSection, vSection, side, radius);
            }
        }
        /*
        * ����ȊO�̋Ȗʂ�Bspline�Ȗʂŋߎ�����
        */
        ToleranceForDistance ofst_tol = new ToleranceForDistance(radius / 100.0);
        return surface.offsetByBsplineSurface(uSection, vSection, radius, side, ofst_tol);
    }

    /**
     * �t�B���b�g��?�?�����͈͂�\�����E�֓_�𓊉e����?B
     * �^����ꂽ�I�t�Z�b�g���a�Ɉ�ԋ߂������̓��e�_��?�߂�?B
     *
     * @param surface �Ȗ�
     * @param uPInfo  �t�B���b�g��?�?�����U���͈̔�
     * @param vPInfo  �t�B���b�g��?�?�����V���͈̔�
     * @param point   ���e����_
     * @param radius  �I�t�Z�b�g���a
     * @return �^����ꂽ�I�t�Z�b�g���a�Ɉ�ԋ߂������̓��e�_
     */
    private PointOnSurface3D nearestPointOnBoundaryWithDistance(ParametricSurface3D surface,
                                                                ParameterDomain uPInfo,
                                                                ParameterDomain vPInfo,
                                                                Point3D point,
                                                                double radius) {
        Point3D prj = null;
        double uParam = 0.0;
        double vParam = 0.0;
        double minDist = -1.0;
        double dist;
        TrimmedCurve3D trc;
        Point3D prj0;
        PointOnCurve3D poc;
        double uParam0, vParam0;
        for (int i = 0; i < 8; i++) {
            switch (i) {
                /*
                * project on 4 boundaries
                * 4�{�̉��z���E��?�ł��铙�p���??[�^��?��?��?A
                * ���ꂼ��֓��e����?B
                */
                case 0:
                case 1:
                    if (uPInfo.isPeriodic())
                        continue;
                    if (i == 0)
                        uParam0 = uPInfo.section().start();
                    else
                        uParam0 = uPInfo.section().end();
                    try {
                        if (surface.type() == ParametricSurface3D.SPHERICAL_SURFACE_3D) {
                            /*
                            * ����V���̓��p���??[�^��?�͉~�ʂł��邪?A
                            * �g������?�ŕ\������邽��?A����V���̃p���??[�^��`��ƈقȂ�
                            * [0,��]�̒�`���?��?B���̂���vPInfo�Ƃ��܂�?���Ȃ��̂�?A
                            * ��U�x?[�X��?�ł���~����?o��?A�g������?���`�������Ă���?B
                            */
                            trc = (TrimmedCurve3D) surface.uIsoParametricCurve(uParam0);
                            trc = new TrimmedCurve3D((Circle3D) trc.basisCurve(),
                                    vPInfo.section());
                        } else {
                            trc = new TrimmedCurve3D(surface.uIsoParametricCurve(uParam0),
                                    vPInfo.section());
                        }
                        poc = trc.nearestProjectWithDistanceFrom(point, radius);
                        if (poc == null) continue;
                        vParam0 = trc.toBasisParameter(poc.parameter());
                        if (surface.type() == ParametricSurface3D.SPHERICAL_SURFACE_3D) {
                            /*
                            * ����V���͗L�ȋ�Ԃł��邪?A���p���??[�^��?�͉~�ł��邽��?A
                            * ���I�Ȓl�ƂȂBĂ���\?�������?B
                            * ����녂�V���̒�`��[-��/2,��/2](��[-��,��])�֕ϊ�����?B
                            */
                            ParameterDomain dmn = new ParameterDomain(true, -Math.PI,
                                    GeometryUtils.PI2);
                            vParam0 = dmn.wrap(vParam0);
                        }
                        prj0 = poc;
                    } catch (ReducedToPointException e) {
                        vParam0 = (vPInfo.section().start() + vPInfo.section().end()) / 2.0;
                        prj0 = (Point3D) e.point();
                    }
                    break;
                case 2:
                case 3:
                    if (vPInfo.isPeriodic())
                        continue;
                    if (i == 2)
                        vParam0 = vPInfo.section().start();
                    else
                        vParam0 = vPInfo.section().end();
                    try {
                        trc = new TrimmedCurve3D(surface.vIsoParametricCurve(vParam0), uPInfo.section());
                        poc = trc.nearestProjectWithDistanceFrom(point, radius);
                        if (poc == null) continue;
                        uParam0 = trc.toBasisParameter(poc.parameter());
                        prj0 = poc;
                    } catch (ReducedToPointException e) {
                        uParam0 = (uPInfo.section().start() + uPInfo.section().end()) / 2.0;
                        prj0 = (Point3D) e.point();
                    }
                    break;
                    /*
                    * project on 4 corner points
                    * ���E��4��̓_�ӊ�e�_�Ƃ��Ĉ���
                    */
                case 4:
                    if (uPInfo.isPeriodic() || vPInfo.isPeriodic()) continue;
                    uParam0 = uPInfo.section().start();
                    vParam0 = vPInfo.section().start();
                    prj0 = surface.coordinates(uParam0, vParam0);
                    break;
                case 5:
                    if (uPInfo.isPeriodic() || vPInfo.isPeriodic()) continue;
                    uParam0 = uPInfo.section().end();
                    vParam0 = vPInfo.section().start();
                    prj0 = surface.coordinates(uParam0, vParam0);
                    break;
                case 6:
                    if (uPInfo.isPeriodic() || vPInfo.isPeriodic()) continue;
                    uParam0 = uPInfo.section().start();
                    vParam0 = vPInfo.section().end();
                    prj0 = surface.coordinates(uParam0, vParam0);
                    break;
                default:    // case 7
                    if (uPInfo.isPeriodic() || vPInfo.isPeriodic()) continue;
                    uParam0 = uPInfo.section().end();
                    vParam0 = vPInfo.section().end();
                    prj0 = surface.coordinates(uParam0, vParam0);
                    break;
            }
            /*
            * ��BƂ�t�B���b�g���a�ɋ߂������̓��e�_��I��
            */
            dist = Math.abs(radius - point.distance(prj0));
            if (prj == null || dist < minDist) {
                prj = prj0;
                minDist = dist;
                uParam = uParam0;
                vParam = vParam0;
            }
        }
        if (prj == null)
            return null;

        return new PointOnSurface3D(prj, surface, uParam, vParam, GeometryElement.doCheckDebug);
    }

    /**
     * �_��Ȗʂɓ��e����?B
     * �^����ꂽ�I�t�Z�b�g���a�Ɉ�ԋ߂������̓��e�_��?�߂�?B
     *
     * @param surface �Ȗ�
     * @param uPInfo  ���e����U���͈̔�
     * @param vPInfo  ���e����V���͈̔�
     * @param point   ���e����_
     * @param radius  �I�t�Z�b�g���a
     */
    private PointOnSurface3D project(ParametricSurface3D surface,
                                     ParameterDomain uPInfo,
                                     ParameterDomain vPInfo,
                                     Point3D point,
                                     double radius) {
        RectangularTrimmedSurface3D rts
                = new RectangularTrimmedSurface3D(surface, uPInfo.section(), vPInfo.section());
        PointOnSurface3D pos = rts.nearestProjectWithDistanceFrom(point, radius);
        PointOnSurface3D pos2 = nearestPointOnBoundaryWithDistance(surface, uPInfo, vPInfo,
                point, radius);
        if (pos == null) {
            if (pos2 != null)
                pos = pos2;
        } else {
            pos = new PointOnSurface3D(pos, surface,
                    rts.toBasisUParameter(pos.uParameter()),
                    rts.toBasisVParameter(pos.vParameter()),
                    GeometryElement.doCheckDebug);
            if (pos2 != null &&
                    Math.abs(radius - point.distance(pos2)) < Math.abs(radius - point.distance(pos)))
                pos = pos2;
        }
        if (debug) {
            System.out.println("// project defference = " +
                    Math.abs(radius - point.distance(pos)));
        }
        return pos;
    }

    /**
     * ���z�I�t�Z�b�g�Ȗʂ̕]���l�⠂�킷�N���X
     */
    private class SurfaceDeriv {
        /**
         * ?W�l
         *
         * @see Point3D
         */
        Point3D pnt;

        /**
         * ���̋Ȗʂ̕]���l
         *
         * @see SurfaceDerivative3D
         */
        SurfaceDerivative3D deriv;

        /**
         * �@?�x�N�g��
         *
         * @see Vector3D
         */
        Vector3D nrm;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param pnt   ?W�l
         * @param deriv ���̋Ȗʂ̕]���l
         * @param nrm   �@?�x�N�g��
         * @see Point3D
         * @see SurfaceDerivative3D
         * @see Vector3D
         */
        private SurfaceDeriv(Point3D pnt, SurfaceDerivative3D deriv, Vector3D nrm) {
            this.pnt = pnt;
            this.deriv = deriv;
            this.nrm = nrm;
        }
    }

    /**
     * ���z�I�t�Z�b�g�Ȗʂ⠂�킷�N���X
     */
    private class SurfaceInfo {
        /*
        * ��?��ƂȂ�Ȗ�
        * @see	ParametricSurface3D
        */
        ParametricSurface3D surface;

        /*
        * U���̃t�B���b�g��?ۋ�Ԃ̎��?��
        * @see	ParameterDomain
        */
        ParameterDomain uPInfo;

        /*
        * V���̃t�B���b�g��?ۋ�Ԃ̎��?��
        * @see	ParameterDomain
        */
        ParameterDomain vPInfo;

        /**
         * �I�t�Z�b�g��(����܂�)
         *
         * @see WhichSide
         */
        double magni;

        /**
         * ��?ۂɃI�t�Z�b�g���ꂽ�Ȗ�(�ߎ���)
         *
         * @see ParametricSurface3D
         */
        ParametricSurface3D ofstSrf;

        /**
         * �t�B���b�g��?ۋ�Ԃ̎��?��𓾂�
         *
         * @param section �t�B���b�g��?ۋ��
         * @param domain  �Ȗʂ̃p���??[�^��`��?��
         * @param ptol    �p���??[�^�̋��e��?�
         * @return �t�B���b�g��?ۋ�Ԃ̎��?��
         */
        private ParameterDomain cyclicPInfo(ParameterSection section,
                                            ParameterDomain domain,
                                            double ptol) {
            double start = section.start();
            double increase = section.increase();
            boolean periodic = false;
            if (domain.isPeriodic()) {    // closed
                double dInc = domain.section().increase();
                if (Math.abs(increase) > dInc - ptol) {
                    /*
                    * the range of the target section is greater than the base section
                    */
                    periodic = true;
                    increase = (increase > 0.0) ? dInc : -dInc;
                }
            }
            return new ParameterDomain(periodic, start, increase);
        }

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param surface  �Ȗ�
         * @param uSection �Ȗʂ̑�?ۂƂȂ�U���̃p���??[�^���
         * @param vSection �Ȗʂ̑�?ۂƂȂ�V���̃p���??[�^���
         * @param side     �Ȗʂ̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
         * @see ParametricSurface3D
         * @see ParameterSection
         * @see WhichSide
         */
        private SurfaceInfo(ParametricSurface3D surface,
                            ParameterSection uSection,
                            ParameterSection vSection,
                            int side) {
            super();

            this.surface = surface;
            double ptol = surface.getToleranceForParameter();
            this.uPInfo = cyclicPInfo(uSection, surface.uParameterDomain(), ptol);
            this.vPInfo = cyclicPInfo(vSection, surface.vParameterDomain(), ptol);
            if (side == WhichSide.FRONT)
                this.magni = radius;
            else
                this.magni = -radius;
            ofstSrf = offsetSurface(surface, uPInfo.section(), vPInfo.section(), side, radius);
        }

        /**
         * ���z�I�t�Z�b�g�Ȗʂ̗^����ꂽ�p���??[�^�ł̕]���l��?�߂�
         *
         * @param parameter �p���??[�^
         * @return �]���l
         */
        private SurfaceDeriv evaluate(double[] parameter) {
            SurfaceDerivative3D deriv = surface.evaluation(parameter[0], parameter[1]);
            Vector3D nrm = deriv.du().crossProduct(deriv.dv()).unitized();
            Point3D pnt = deriv.d0().add(nrm.multiply(magni));
            return new SurfaceDeriv(pnt, deriv, nrm);
        }
    }

    /**
     * ���z�I�t�Z�b�g�Ȗʂ�?���?�߂�
     *
     * @param surface  ��?��ƂȂ�Ȗ�
     * @param uSection ��?��ƂȂ�Ȗʂ�U���̃p���??[�^���
     * @param vSection ��?��ƂȂ�Ȗʂ�V���̃p���??[�^���
     * @param side     ��?��ƂȂ�Ȗʂ̂ǂ��瑤�ɃI�t�Z�b�g���邩
     * @see SurfaceInfo
     * @see ParametricSurface3D
     * @see ParameterSection
     * @see WhichSide
     */
    private SurfaceInfo[] getInfo(ParametricSurface3D surface,
                                  ParameterSection uSection,
                                  ParameterSection vSection,
                                  int side) {
        SurfaceInfo[] infoArray;
        int nInfo;
        int[] sides;

        switch (side) {
            case WhichSide.BOTH:
                /*
                * ����ɋ?�߂�?�?��͕\�����ꂼ��̕��̉��z�I�t�Z�b�g�Ȗʂ�?���?�߂�
                */
                nInfo = 2;
                sides = new int[2];
                sides[0] = WhichSide.FRONT;
                sides[1] = WhichSide.BACK;
                break;
            case WhichSide.FRONT:
            case WhichSide.BACK:
                /*
                * �^����ꂽ���̉��z�I�t�Z�b�g�Ȗʂ�?���?�߂�
                */
                nInfo = 1;
                sides = new int[1];
                sides[0] = side;
                break;
            default:
                throw new InvalidArgumentValueException();
        }

        infoArray = new SurfaceInfo[nInfo];
        for (int i = 0; i < nInfo; i++) {
            infoArray[i] = new SurfaceInfo(surface, uSection, vSection, sides[i]);
        }
        return infoArray;
    }

    /**
     * �t�B���b�g��?���\���N���X
     */
    private class FilletInfo {
        /**
         * ���z�I�t�Z�b�g�Ȗ� A ��?��
         */
        SurfaceInfo sInfoA;

        /**
         * ���z�I�t�Z�b�g�Ȗ� B ��?��
         */
        SurfaceInfo sInfoB;

        /*
        * �ȉ��͎��Z�ɂ����Ĉꎞ�g�p
        */
        private nlFunc nl_func;
        private PrimitiveMappingND[] dnl_func;
        private cnvFunc cnv_func;
        private dltFunc dlt_func;

        private SurfaceDeriv derivA;
        private SurfaceDeriv derivB;

        private int fixedParamType;
        private double fxParam;

        private double[][] sMatrix = new double[3][3];

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param cInfoA ���z�I�t�Z�b�g�Ȗ� A ��?��
         * @param cInfoB ���z�I�t�Z�b�g�Ȗ� B ��?��
         */
        private FilletInfo(SurfaceInfo sInfoA, SurfaceInfo sInfoB) {
            super();

            this.sInfoA = sInfoA;
            this.sInfoB = sInfoB;

            nl_func = new nlFunc();
            dnl_func = new PrimitiveMappingND[3];
            for (int i = 0; i < 3; i++)
                dnl_func[i] = new dnlFunc(i);
            cnv_func = new cnvFunc();
            dlt_func = new dltFunc();
        }

        /**
         * �p���??[�^�̒l���?�����
         *
         * @param domain �Ȗʂ̃p���??[�^��`��?��
         * @param pinfo  �t�B���b�g��?ۋ�Ԃ̎��?��
         * @param param  �p���??[�^
         * @return ��?����ꂽ�p���??[�^
         */
        private double refineParam(ParameterDomain domain, ParameterDomain pInfo, double param) {
            if (domain.isNonPeriodic())
                return param;

            /*
            * make sure that param is inside of pInfo
            */
            double lo = pInfo.section().lower();
            double up = pInfo.section().upper();
            double il = domain.section().increase();
            if (pInfo.section().increase() > 0.0) {
                while (param < lo) param += il;
                while (param > up) param -= il;
            } else {
                while (param > lo) param -= il;
                while (param < up) param += il;
            }
            return param;
        }

        private double[] setupParams(int i,
                                     double uParamA,
                                     double vParamA,
                                     double uParamB,
                                     double vParamB) {
            double[] param = new double[3];

            switch (i) {
                case 0:
                    fixedParamType = A_U_FIX;
                    break;
                case 1:
                    fixedParamType = A_V_FIX;
                    break;
                case 2:
                    fixedParamType = B_U_FIX;
                    break;
                case 3:
                    fixedParamType = B_V_FIX;
                    break;
            }

            /*
            * set up parameters
            */
            switch (fixedParamType) {
                case A_U_FIX:
                    fxParam = uParamA;
                    param[0] = vParamA;
                    param[1] = uParamB;
                    param[2] = vParamB;
                    break;
                case A_V_FIX:
                    param[0] = uParamA;
                    fxParam = vParamA;
                    param[1] = uParamB;
                    param[2] = vParamB;
                    break;
                case B_U_FIX:
                    param[0] = uParamA;
                    param[1] = vParamA;
                    fxParam = uParamB;
                    param[2] = vParamB;
                    break;
                case B_V_FIX:
                    param[0] = uParamA;
                    param[1] = vParamA;
                    param[2] = uParamB;
                    fxParam = vParamB;
                    break;
            }

            return param;
        }

        private void reformParam(double[][] paramS) {
            ParameterDomain[] domainA = {sInfoA.surface.uParameterDomain(),
                    sInfoA.surface.vParameterDomain()};
            ParameterDomain[] domainB = {sInfoB.surface.uParameterDomain(),
                    sInfoB.surface.vParameterDomain()};
            ParameterDomain[] pInfoA = {sInfoA.uPInfo, sInfoA.vPInfo};
            ParameterDomain[] pInfoB = {sInfoB.uPInfo, sInfoB.vPInfo};
            ParameterDomain[] pInfo = pInfoA;
            ParameterDomain[] domain = domainA;

            double param_lo_p = 0.0;
            double param_up_s = 0.0;
            boolean s_exist;

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    double pup = pInfo[j].section().upper();
                    double plo = pInfo[j].section().lower();
                    s_exist = false;

                    if (domain[j].isPeriodic()) {    /* base surface is closed */
                        double lo = domain[j].section().lower();
                        double up = domain[j].section().upper();
                        double il = domain[j].section().increase();

                        while (paramS[i][j] < lo) paramS[i][j] += il;
                        while (paramS[i][j] > up) paramS[i][j] -= il;

                        if (pup > up) {
                            param_lo_p = plo;
                            param_up_s = lo + (pup - up);
                            s_exist = true;

                        } else if (plo < lo) {
                            param_lo_p = up - (plo - lo);
                            param_up_s = pup;
                            s_exist = true;
                        }
                    }

                    if (pInfo[j].isNonPeriodic()) {    /* target section is open */
                        if (!s_exist) {
                            if (paramS[i][j] < plo) paramS[i][j] = plo;
                            if (paramS[i][j] > pup) paramS[i][j] = pup;
                        } else {
                            if ((param_up_s < paramS[i][j]) && (paramS[i][j] < param_lo_p)) {
                                if ((paramS[i][j] - param_up_s) < (param_lo_p - paramS[i][j]))
                                    paramS[i][j] = param_up_s;
                                else
                                    paramS[i][j] = param_lo_p;
                            }
                        }
                    }
                }

                domain = domainB;
                pInfo = pInfoB;
            }
        }

        private double[][] fillParam(double[] param) {
            double[][] paramS = new double[2][2];

            switch (fixedParamType) {
                case A_U_FIX:
                    paramS[0][0] = fxParam;
                    paramS[0][1] = param[0];
                    paramS[1][0] = param[1];
                    paramS[1][1] = param[2];
                    break;
                case A_V_FIX:
                    paramS[0][0] = param[0];
                    paramS[0][1] = fxParam;
                    paramS[1][0] = param[1];
                    paramS[1][1] = param[2];
                    break;
                case B_U_FIX:
                    paramS[0][0] = param[0];
                    paramS[0][1] = param[1];
                    paramS[1][0] = fxParam;
                    paramS[1][1] = param[2];
                    break;
                case B_V_FIX:
                    paramS[0][0] = param[0];
                    paramS[0][1] = param[1];
                    paramS[1][0] = param[2];
                    paramS[1][1] = fxParam;
                    break;
            }

            reformParam(paramS);

            return paramS;
        }

        /**
         * �t�B���b�g�f�ʂ�refinement
         * <p/>
         * �A�����̊e���̒l��?�߂�
         * F of F(x) = 0
         * </p>
         *
         * @see Math#solveSimultaneousEquationsWithCorrection(PrimitiveMappingND,PrimitiveMappingND[],
         *PrimitiveBooleanMappingNDTo1D,
         *PrimitiveMappingND,double[])
         */
        private class nlFunc implements PrimitiveMappingND {
            private nlFunc() {
                super();
            }

            public double[] map(int x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            public double[] map(long x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            public double[] map(float x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            /**
             * The dimension of variable parameter. Should be a strictly positive integer.
             */
            public int numInputDimensions() {
                return 3;
            }

            /**
             * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
             */
            public int numOutputDimensions() {
                return 3;
            }

            public double[] map(double[] parameter) {
                /*
                * derivA & derivB are already computed by previous cnvFunc.map()
                */
                Vector3D evec = derivA.pnt.subtract(derivB.pnt);

                return evec.toDoubleArray();
            }
        }

        /**
         * �t�B���b�g�f�ʂ�refinement
         * <p/>
         * �A�����̊e���̕Δ�̒l��?�߂�
         * partial derivatives of F
         * </p>
         *
         * @see Math#solveSimultaneousEquationsWithCorrection(PrimitiveMappingND,PrimitiveMappingND[],
         *PrimitiveBooleanMappingNDTo1D,
         *PrimitiveMappingND,double[])
         */
        private class dnlFunc implements PrimitiveMappingND {
            int idx;

            private dnlFunc(int idx) {
                super();
                this.idx = idx;
            }

            public double[] map(int x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            public double[] map(long x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            public double[] map(float x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            /**
             * The dimension of variable parameter. Should be a strictly positive integer.
             */
            public int numInputDimensions() {
                return 2;
            }

            /**
             * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
             */
            public int numOutputDimensions() {
                return 2;
            }

            private void fillMatrix(int m_idx, Vector3D vec, double magni, double[] dX,
                                    boolean reverse) {
                sMatrix[0][m_idx] = vec.x() + magni * dX[0];
                sMatrix[1][m_idx] = vec.y() + magni * dX[1];
                sMatrix[2][m_idx] = vec.z() + magni * dX[2];
                if (reverse)
                    for (int i = 0; i < 3; i++)
                        sMatrix[i][m_idx] *= -1.0;
            }

            public double[] map(double[] parameter) {
                if (idx == 0) {        // this must be called first
                    /*
                    * computation of derivatives is already done by previous cnvFunc.evaluate()
                    */
                    Matrix dDm = new Matrix(3, 3);
                    double[] dB = new double[3];
                    double[] dX;

                    int m_idx = 0;

                    if (fixedParamType != A_U_FIX) {
                        dDm.setElementsAt(0, derivA.deriv.du().toDoubleArray());
                        dDm.setElementsAt(1, derivA.deriv.dv().toDoubleArray());
                        dDm.setElementsAt(2, derivA.nrm.toDoubleArray());
                        dB[0] = -derivA.deriv.duu().dotProduct(derivA.nrm);
                        dB[1] = -derivA.deriv.duv().dotProduct(derivA.nrm);
                        dB[2] = 0.0;
                        if ((dX = dDm.solveSimultaneousLinearEquations(dB)) == null)
                            return null;
                        fillMatrix(m_idx, derivA.deriv.du(), sInfoA.magni, dX, false);
                        m_idx++;
                    }

                    if (fixedParamType != A_V_FIX) {
                        dDm.setElementsAt(0, derivA.deriv.dv().toDoubleArray());
                        dDm.setElementsAt(1, derivA.deriv.du().toDoubleArray());
                        dDm.setElementsAt(2, derivA.nrm.toDoubleArray());
                        dB[0] = -derivA.deriv.dvv().dotProduct(derivA.nrm);
                        dB[1] = -derivA.deriv.duv().dotProduct(derivA.nrm);
                        dB[2] = 0.0;
                        if ((dX = dDm.solveSimultaneousLinearEquations(dB)) == null)
                            return null;
                        fillMatrix(m_idx, derivA.deriv.dv(), sInfoA.magni, dX, false);
                        m_idx++;
                    }

                    if (fixedParamType != B_U_FIX) {
                        dDm.setElementsAt(0, derivB.deriv.du().toDoubleArray());
                        dDm.setElementsAt(1, derivB.deriv.dv().toDoubleArray());
                        dDm.setElementsAt(2, derivB.nrm.toDoubleArray());
                        dB[0] = -derivB.deriv.duu().dotProduct(derivB.nrm);
                        dB[1] = -derivB.deriv.duv().dotProduct(derivB.nrm);
                        dB[2] = 0.0;
                        if ((dX = dDm.solveSimultaneousLinearEquations(dB)) == null)
                            return null;
                        fillMatrix(m_idx, derivB.deriv.du(), sInfoB.magni, dX, true);
                        m_idx++;
                    }

                    if (fixedParamType != B_V_FIX) {
                        dDm.setElementsAt(0, derivB.deriv.dv().toDoubleArray());
                        dDm.setElementsAt(1, derivB.deriv.du().toDoubleArray());
                        dDm.setElementsAt(2, derivB.nrm.toDoubleArray());
                        dB[0] = -derivB.deriv.dvv().dotProduct(derivB.nrm);
                        dB[1] = -derivB.deriv.duv().dotProduct(derivB.nrm);
                        dB[2] = 0.0;
                        if ((dX = dDm.solveSimultaneousLinearEquations(dB)) == null)
                            return null;
                        fillMatrix(m_idx, derivB.deriv.dv(), sInfoB.magni, dX, true);
                        m_idx++;
                    }

                    /*
                    * check whether the matrix is regular or not
                    */
                    int i, j;
                    double tol = sInfoA.surface.getToleranceForDistance();
                    for (i = 0; i < 3; i++) {
                        for (j = 0; j < 3; j++) {
                            if (Math.abs(sMatrix[i][j]) > tol)
                                break;
                        }
                        if (j == 3)
                            return null;
                    }
                }
                return sMatrix[idx];
            }
        }

        /**
         * �t�B���b�g�f�ʂ�refinement
         * <p/>
         * �A�����̉⪎������ǂ����𔻒肷��
         * convergence test
         * </p>
         *
         * @see Math#solveSimultaneousEquationsWithCorrection(PrimitiveMappingND,PrimitiveMappingND[],
         *PrimitiveBooleanMappingNDTo1D,
         *PrimitiveMappingND,double[])
         */
        private class cnvFunc implements PrimitiveBooleanMappingNDTo1D {
            private cnvFunc() {
                super();
            }

            public boolean map(int x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            public boolean map(long x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            public boolean map(float x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            /**
             * The dimension of variable parameter. Should be a strictly positive integer.
             */
            public int numInputDimensions() {
                return 2;
            }

            public boolean map(double[] parameter) {
                double[][] paramS = fillParam(parameter);

                derivA = sInfoA.evaluate(paramS[0]);
                derivB = sInfoB.evaluate(paramS[1]);

                if (debug) {
                    System.out.println("// refine dist = " +
                            derivA.pnt.distance(derivB.pnt));
                }

                return derivA.pnt.identical(derivB.pnt);
            }
        }

        /**
         * �t�B���b�g�f�ʂ�refinement
         * <p/>
         * ���Z�r���ł̃p���??[�^�̕�?���?s��
         * </p>
         *
         * @see Math#solveSimultaneousEquationsWithCorrection(PrimitiveMappingND,PrimitiveMappingND[],
         *PrimitiveBooleanMappingNDTo1D,
         *PrimitiveMappingND,double[])
         */
        private class dltFunc implements PrimitiveMappingND {
            private dltFunc() {
                super();
            }

            public double[] map(int x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            public double[] map(long x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            public double[] map(float x[]) {
                return map(ArrayMathUtils.toDouble(x));
            }

            /**
             * The dimension of variable parameter. Should be a strictly positive integer.
             */
            public int numInputDimensions() {
                return 3;
            }

            /**
             * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
             */
            public int numOutputDimensions() {
                return 3;
            }

            public double[] map(double[] parameter) {
                double[][] paramS = fillParam(parameter);

                switch (fixedParamType) {
                    case A_U_FIX:
                        parameter[0] = paramS[0][1];
                        parameter[1] = paramS[1][0];
                        parameter[2] = paramS[1][1];
                        break;
                    case A_V_FIX:
                        parameter[0] = paramS[0][0];
                        parameter[1] = paramS[1][0];
                        parameter[2] = paramS[1][1];
                        break;
                    case B_U_FIX:
                        parameter[0] = paramS[0][0];
                        parameter[1] = paramS[0][1];
                        parameter[2] = paramS[1][1];
                        break;
                    case B_V_FIX:
                        parameter[0] = paramS[0][0];
                        parameter[1] = paramS[0][1];
                        parameter[2] = paramS[1][0];
                        break;
                }
                return parameter;
            }
        }

        private FilletSection3D setbackParams(double[] parameter) {
            double[][] paramS = fillParam(parameter);

            /*
            * take the middle position of A and B
            */
            Point3D cntr = derivA.pnt.midPoint(derivB.pnt);
            PointOnSurface3D posA
                    = new PointOnSurface3D(sInfoA.surface, paramS[0][0], paramS[0][1],
                    GeometryElement.doCheckDebug);
            PointOnSurface3D posB
                    = new PointOnSurface3D(sInfoB.surface, paramS[1][0], paramS[1][1],
                    GeometryElement.doCheckDebug);
            return new FilletSection3D(radius, cntr, posA, posB);
        }

        /**
         * �t�B���b�g�f�ʂ�refinement��?s��?B
         * <p/>
         * ���z�I�t�Z�b�g�Ȗʓ��m�̌�_(��?�?��1�_)��t�B���b�g�f�ʂ̒�?S��?���l�Ƃ���?A
         * �t�B���b�g�f�ʂ�?�������?S�ʒu����Z�ŋ?�߂�
         * </p>
         *
         * @param intp ���z�I�t�Z�b�g�Ȗʓ��m�̌�_(�t�B���b�g�f�ʂ̒�?S��?���l)
         * @param pocA �Ȗ� A ?�̃t�B���b�g�f�ʂ�?ړ_��?���l
         * @param pocB �Ȗ� B ?�̃t�B���b�g�f�ʂ�?ړ_��?���l
         * @return �t�B���b�g�f��
         * @see Math#solveSimultaneousEquationsWithCorrection(PrimitiveMappingND,PrimitiveMappingND[],
         *PrimitiveBooleanMappingNDTo1D,
         *PrimitiveMappingND,double[])
         */
        private FilletSection3D refineFillet(Point3D intp,
                                             double uParamA, double vParamA,
                                             double uParamB, double vParamB,
                                             boolean doProjection) {
            /*
            * resolve (F = 0)
            *
            *	F = P + a * N - (Q + b * M)
            *
            * where
            *	P : point on A
            *	a : magnitude of offset of A (negative if offset_side is specified as BACK)
            *	N : unit normal at P
            *	Q : point on B
            *	b : magnitude of offset of B (negative if offset_side is specified as BACK)
            *	M : unit normal at Q
            */
            FilletSection3D sec = null;
            FilletSection3D secI;
            double diff = -1.0;
            double diffI;
            uParamA = refineParam(sInfoA.surface.uParameterDomain(), sInfoA.uPInfo, uParamA);
            vParamA = refineParam(sInfoA.surface.vParameterDomain(), sInfoA.vPInfo, vParamA);
            uParamB = refineParam(sInfoB.surface.uParameterDomain(), sInfoB.uPInfo, uParamB);
            vParamB = refineParam(sInfoB.surface.vParameterDomain(), sInfoB.vPInfo, vParamB);
            for (int i = 0; i < 4; i++) {
                double[] param = setupParams(i, uParamA, vParamA, uParamB, vParamB);
                double[] refined
                        = GeometryUtils.solveSimultaneousEquationsWithCorrection(nl_func, dnl_func, cnv_func,
                        dlt_func, param);
                if (refined == null)
                    continue;
                secI = setbackParams(refined);
                diffI = secI.center().distance(intp);
                if (sec == null || diffI < diff) {
                    sec = secI;
                    diff = diffI;
                }
            }
            return sec;
        }

        /**
         * ���z�I�t�Z�b�g�Ȗʓ��m�̌�?��t�B���b�g��?�߂�?B
         *
         * @param intp ���z�I�t�Z�b�g�Ȗʓ��m�̌�?�
         * @return �t�B���b�g
         */
        private FilletObject3D toFillet(SurfaceSurfaceInterference3D intf) {
            IntersectionCurve3D ints;
            if ((ints = intf.toIntersectionCurve()) == null)    // not a curve
                return null;

            ParametricCurve3D curve3d = ints.curve3d();
            Polyline3D pol;
            if (curve3d.type() == ParametricCurve3D.POLYLINE_3D)
                pol = (Polyline3D) curve3d;
            else
                pol = curve3d.toPolyline(curve3d.parameterDomain().section(), // must be bounded curve
                        curve3d.getToleranceForDistanceAsObject());

            int nPoints = pol.nPoints();
            FilletObjectList secList = new FilletObjectList();
            FilletSection3D oneSec;
            PointOnSurface3D posA;
            PointOnSurface3D posB;
            double uParamA, vParamA, uParamB, vParamB;
            for (int i = 0; i < nPoints; i++) {
                /*
                 * ��?ݖ����?�̓��e�ⷂ�悤�ɂȂBĂ��邪?A
                 * �{���͗��[��?����O��̃p���??[�^��?���l�Ƃ������e��?s���ׂ�?B
                if (i == 0 || i == nPoints - 1) {
                */
                posA = project(sInfoA.surface, sInfoA.uPInfo, sInfoA.vPInfo, pol.pointAt(i), radius);
                posB = project(sInfoB.surface, sInfoB.uPInfo, sInfoB.vPInfo, pol.pointAt(i), radius);
                uParamA = posA.uParameter();
                vParamA = posA.vParameter();
                uParamB = posB.uParameter();
                vParamB = posB.vParameter();
                if ((oneSec = refineFillet(pol.pointAt(i), uParamA, vParamA, uParamB, vParamB,
                        false)) != null)
                    secList.addSection(oneSec);
                /*
            } else {
                if ((sec[i] = refineFillet(pol.pointAt(i), uParamA, vParamA, uParamB, vParamB, true)) == null) {
                posA = project(sInfoA.surface, sInfoA.uPInfo,
                           sInfoA.vPInfo, pol.pointAt(i), radius);
                posB = project(sInfoB.surface, sInfoB.uPInfo,
                           sInfoB.vPInfo, pol.pointAt(i), radius);
                uParamA = posA.uParameter();
                vParamA = posA.vParameter();
                uParamB = posB.uParameter();
                vParamB = posB.vParameter();
                if ((oneSec = refineFillet(pol.pointAt(i), uParamA, vParamA, uParamB, vParamB,
                               false)) != null)
                    secList.addSection(oneSec);
                }
            }
                */
            }
            return secList.toFilletObject3D(false);
        }

        /**
         * ��?ۂɃt�B���b�g��?�߂�?��?
         */
        private void getFillets() {
            /*
            * �܂�?��߂Ɏ�?ۂɃI�t�Z�b�g�����Ȗ�(�ߎ���)���m�̌�?�𓾂�?B
            * ����ꂽ��?�t�B���b�g�̒�?S�O?�(��?���l)�ƂȂ�?B
            */
            SurfaceSurfaceInterference3D[] ints;
            try {
                if (debug) {
                    System.out.println("intersection start");
                    sInfoA.ofstSrf.output(System.out);
                    sInfoB.ofstSrf.output(System.out);
                }
                ints = sInfoA.ofstSrf.intersect(sInfoB.ofstSrf);
                if (debug) {
                    System.out.println("intersection OK");
                    if (ints.length < 1)
                        System.out.println("no intersection");
                }
            } catch (IndefiniteSolutionException e) {
                /*
                * ������IndefiniteSolution��?����ׂ����ǂ����Y�ނƂ���ł͂���?B
                * �R���ɂ̂݃t�B���b�g��?�������Ȃ�ǂ����µ��Ȃ���?A
                * �����ɔ�?������邱�Ƃ �邽��?B
                */
                SurfaceSurfaceInterference3D intf = (SurfaceSurfaceInterference3D) e.suitable();
                ints = new SurfaceSurfaceInterference3D[1];
                ints[0] = intf;
            }
            /*
            * ����ꂽ��?���ƂɃt�B���b�g��?��֕ϊ�����?B
            */
            FilletObject3D oneSol;
            for (int i = 0; i < ints.length; i++)
                if ((oneSol = toFillet(ints[i])) != null)
                    fillets.addFillet(oneSol);
        }
    }

    /**
     * 2�ȖʊԂ̃t�B���b�g�𓾂�
     *
     * @return 2 �Ȗʂ̃t�B���b�g�̔z��
     */
    private FilletObject3D[] getFillets() {
        /*
        * ���ꂼ��̋Ȗʂ̉��z�I�t�Z�b�g�Ȗʂ�?����ƂɃt�B���b�g�𓾂�
        */
        FilletInfo doObj;
        for (int i = 0; i < infoA.length; i++)
            for (int j = 0; j < infoB.length; j++) {
                doObj = new FilletInfo(infoA[i], infoB[j]);
                if (debug)
                    System.out.println("fillet doing at each FilletInfo");
                doObj.getFillets();
            }

        return fillets.toFilletObject3DArray(false);
    }

    /**
     * 2�ȖʊԂ̃t�B���b�g�𓾂�
     *
     * @param surfaceA �Ȗ� A
     * @param uSectA   �Ȗ� A �̑�?ۂƂȂ�U���̃p���??[�^���
     * @param vSectA   �Ȗ� A �̑�?ۂƂȂ�V���̃p���??[�^���
     * @param sideA    �Ȗ� A �̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
     * @param surfaceB �Ȗ� B
     * @param uSectB   �Ȗ� B �̑�?ۂƂȂ�U���̃p���??[�^���
     * @param vSectB   �Ȗ� B �̑�?ۂƂȂ�V���̃p���??[�^���
     * @param sideB    �Ȗ� B �̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
     * @param raidus   �t�B���b�g�̔��a
     * @return 2 �Ȗʂ̃t�B���b�g�̔z��
     * @see ParametricSurface3D
     * @see ParameterSection
     * @see WhichSide
     * @see FilletObject2D
     */
    static FilletObject3D[] fillet(ParametricSurface3D surfaceA,
                                   ParameterSection uSectA,
                                   ParameterSection vSectA,
                                   int sideA,
                                   ParametricSurface3D surfaceB,
                                   ParameterSection uSectB,
                                   ParameterSection vSectB,
                                   int sideB,
                                   double radius)
            throws IndefiniteSolutionException {
        /*
        * �����ł͋�?�E�ȖʈȊO�̋Ȗʂ赂�?B
        * ?�L�Ȗʂɂ��Ă�?A���ꂼ��̃t�B���b�g?��?�ɔC����?B
        */
        if (debug)
            System.out.println("fillet start");

        int typeA = surfaceA.type();
        int typeB = surfaceB.type();

        switch (typeA) {
            case ParametricSurface3D.PLANE_3D:
            case ParametricSurface3D.SPHERICAL_SURFACE_3D:
            case ParametricSurface3D.CYLINDRICAL_SURFACE_3D:
            case ParametricSurface3D.CONICAL_SURFACE_3D:
            case ParametricSurface3D.PURE_BEZIER_SURFACE_3D:
            case ParametricSurface3D.BSPLINE_SURFACE_3D:
            case ParametricSurface3D.SURFACE_OF_LINEAR_EXTRUSION_3D:
            case ParametricSurface3D.SURFACE_OF_REVOLUTION_3D:
            case ParametricSurface3D.RECTANGULAR_TRIMMED_SURFACE_3D:
                switch (typeB) {
                    case ParametricSurface3D.PLANE_3D:
                    case ParametricSurface3D.SPHERICAL_SURFACE_3D:
                    case ParametricSurface3D.CYLINDRICAL_SURFACE_3D:
                    case ParametricSurface3D.CONICAL_SURFACE_3D:
                    case ParametricSurface3D.PURE_BEZIER_SURFACE_3D:
                    case ParametricSurface3D.BSPLINE_SURFACE_3D:
                    case ParametricSurface3D.SURFACE_OF_LINEAR_EXTRUSION_3D:
                    case ParametricSurface3D.SURFACE_OF_REVOLUTION_3D:
                    case ParametricSurface3D.RECTANGULAR_TRIMMED_SURFACE_3D:
                        /*
                        * �{�N���X�������t�B���b�g?��?
                        */
                        FiltSrfSrf3D doObj = new FiltSrfSrf3D(surfaceA, uSectA, vSectA, sideA,
                                surfaceB, uSectB, vSectB, sideB,
                                radius);
                        if (debug)
                            System.out.println("fillet doing");

                        return doObj.getFillets();
                }
                throw new UnsupportedOperationException();
        }
        throw new UnsupportedOperationException();
    }
}

// end of file
