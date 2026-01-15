/*
 * �R���� : ���ʂ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Plane3D.java,v 1.5 2006/05/20 23:25:52 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.analysis.polynomials.DoublePolynomial;
import org.jscience.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : ���ʂ�\���N���X?B
 * <p/>
 * ���̃N���X�ɓWL�ȑ�?���\���t�B?[���h�͓BɂȂ�?B
 * ���ʂ̋�?��I�Ȍ��_�Ɗe���̕����z�u?�� (��?�?W�n) ��?A
 * {@link ElementarySurface3D �X?[�p?[�N���X} �� position �ŕێ?�����?B
 * </p>
 * <p/>
 * ���ʂ̃p���??[�^��`��� U/V �����Ƃ��?A���Ŕ���I�ł���?B
 * </p>
 * <p/>
 * (u, v) ��p���??[�^�Ƃ��镽�� P(u, v) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(u, v) = c + u * x + v * y
 * </pre>
 * ������?Ac, x, y �͂��ꂼ��
 * <pre>
 * 	c : position.location()
 * 	x : position.x()
 * 	y : position.y()
 * </pre>
 * ��\��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.5 $, $Date: 2006/05/20 23:25:52 $
 */

public class Plane3D extends ElementarySurface3D {
    /**
     * ��?�?W�n��w�肵�ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param position ��?����_�Ɗe���̕�����?�?W�n
     * @see InvalidArgumentValueException
     */
    public Plane3D(Axis2Placement3D position) {
        super(position);
    }

    /**
     * ��?����_�Ɩ@?�x�N�g����^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?\�z����镽�ʂ̋�?� X/Y ���̕���?A
     * ���̃R���X�g���N�^�̓Ք�Ō��肷��?B
     * </p>
     *
     * @param pnt    ��?����_��?W�l
     * @param normal �@?�x�N�g��
     */
    public Plane3D(Point3D pnt, Vector3D normal) {
        super(new Axis2Placement3D(pnt, normal,
                normal.verticalVector().unitized()));
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param uParam U ���p���??[�^�l
     * @param vParam V ���p���??[�^�l
     * @return ?W�l
     */
    public Point3D coordinates(double uParam, double vParam) {
        Point3D center = position().location();
        Vector3D x = position().x();
        Vector3D y = position().y();

        // c + u*x + v*y
        return center.add(x.multiply(uParam)).add(y.multiply(vParam));
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * �����ł�?ڃx�N�g���Ƃ�?A�p���??[�^ U/V �̊e?X�ɂ��Ă̈ꎟ�Γ���?��ł���?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ�z��̗v�f?��� 2 �ł���?B
     * �z���?�?��̗v�f�ɂ� U �p���??[�^�ɂ��Ă�?ڃx�N�g��?A
     * ��Ԗڂ̗v�f�ɂ� V �p���??[�^�ɂ��Ă�?ڃx�N�g����܂�?B
     * </p>
     *
     * @param uParam U ���p���??[�^�l
     * @param vParam V ���p���??[�^�l
     * @return ?ڃx�N�g���̔z��
     */
    public Vector3D[] tangentVector(double uParam, double vParam) {
        Vector3D[] tangent = new Vector3D[2];
        tangent[0] = position().x();
        tangent[1] = position().y();
        return tangent;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̖@?�x�N�g����Ԃ�?B
     * <p/>
     * ���̃?�\�b�h���Ԃ��@?�x�N�g����?A?��K�����ꂽ�P�ʃx�N�g���ł���?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?��K�����ꂽ�@?�x�N�g��
     */
    public Vector3D normalVector(double uParam, double vParam) {
        return position().z();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̎�ȗ�?���Ԃ�?B
     * <p/>
     * ��̎�ȗ� (principalCurvature1, principalCurvature2) �̒l�͂ǂ���� 0 �ł���?B
     * ����x�N�g��1 (principalDirection1) �ɂ͋�?� X �����̒P�ʃx�N�g��?A
     * ����x�N�g��2 (principalDirection2) �ɂ͋�?� Y �����̒P�ʃx�N�g��
     * ��Ԃ�?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ��ȗ�?��
     */
    public SurfaceCurvature3D curvature(double uParam, double vParam) {
        return new SurfaceCurvature3D(0.0, position().x(),
                0.0, position().y());
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ�?B
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     */
    public SurfaceDerivative3D evaluation(double uParam, double vParam) {
        Point3D center = position().location();
        Vector3D x = position().x();
        Vector3D y = position().y();
        Vector3D zerov = Vector3D.zeroVector;
        // c + u*x + v*y
        Point3D p = center.add(x.multiply(uParam)).add(y.multiply(vParam));

        return new SurfaceDerivative3D(p, x, y, zerov, zerov, zerov);
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ� (�������?݂���) ���e�_��?�߂�?B
     * <p/>
     * [�Ք?��?]
     * <br>
     * �^����ꂽ�_�ɂ���?A���̕��ʂ̋�?�?W�n�ɂ�����?W�l��?��?A
     * ���� x, y ?�����p���??[�^�l (uParameter, vParameter)
     * �Ƃ����?�_��?u���e�_?v�Ƃ��Ă���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     * @see #projectFrom(Point3D)
     */
    public PointOnSurface3D project1From(Point3D point) {
        CartesianTransformationOperator3D gtrans = toGlobal();
        Point3D lpoint = gtrans.reverseTransform(point);
        PointOnSurface3D foot =
                new PointOnSurface3D(this, lpoint.x(), lpoint.y(), doCheckDebug);
        return foot;
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * ���镽�ʂւ̔C�ӂ̓_����̓��e�_��?��͕K�� 1 �ɂȂ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @see #project1From(Point3D)
     */
    public PointOnSurface3D[] projectFrom(Point3D point) {
        PointOnSurface3D[] proj = {project1From(point)};
        return proj;
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * �^����ꂽ��?��ŕ��ʋߎ�����i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     */
    public Mesh3D
    toMesh(ParameterSection uPint, ParameterSection vPint,
           ToleranceForDistance tol) {
        PointOnSurface3D[][] mesh = new PointOnSurface3D[2][2];
        double uParam, vParam;
        int i, j;

        uParam = uPint.lower();
        for (i = 0; i < 2; i++) {
            vParam = vPint.lower();
            for (j = 0; j < 2; j++) {
                try {
                    mesh[i][j] = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
                } catch (InvalidArgumentValueException e) {
                    throw new FatalException();
                }
                vParam = vPint.upper();
            }
            uParam = uPint.upper();
        }

        return new Mesh3D(mesh, false);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ쵖���?Č�����L�? Bspline �Ȗʂ�Ԃ�?B
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @return ���̋Ȗʂ̎w��̋�Ԃ�?Č�����L�? Bspline �Ȗ�
     */
    public BsplineSurface3D
    toBsplineSurface(ParameterSection uPint,
                     ParameterSection vPint) {
        Point3D[][] controlPoints = new Point3D[2][2];
        double[][] weights = new double[2][2];

        double[] uParams = {uPint.start(), uPint.end()};
        double[] vParams = {vPint.start(), vPint.end()};

        for (int vi = 0; vi < 2; vi++) {
            for (int ui = 0; ui < 2; ui++) {
                controlPoints[ui][vi] = this.coordinates(uParams[ui], vParams[vi]);
                weights[ui][vi] = 1.0;
            }
        }

        return new BsplineSurface3D(BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
                BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
                controlPoints, weights);
    }

    /**
     * ����?����Ȗʂ� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?�����?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ����?����Ȗʂ� poly �̌�_��\����?�����?���
     */
    DoublePolynomial makePoly(DoublePolynomial[] poly) {
        return poly[2];
    }

    /**
     * �^����ꂽ�_�����̋Ȗ�?�ɂ��邩�ۂ���`�F�b�N����?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �^����ꂽ�_�����̋Ȗ�?�ɂ���� true?A�����łȂ���� false
     */
    boolean checkSolution(Point3D point) {
        double dTol = getToleranceForDistance();
        return Math.abs(point.z()) < dTol;
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̕��ʂƗ^����ꂽ��?�� (�����������) ��_��?�߂�
     * <p/>
     * mate �� this �̋�?�?W�n�ł̕\���ɕϊ�������̂� M �Ƃ���?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * M �̕��x�N�g����P�ʉ������x�N�g���� Z ?�����?�Βl��
     * �����̋��e��?�����?��������?A
     * ���҂͌���Ȃ���̂Ƃ��� null ��Ԃ�?B
     * <br>
     * ������?A����?�?��ɂ����
     * M �̃p���??[�^�l 0 �̓_�� Z ?�����?�Βl��
     * �����̋��e��?�����?��������?A
     * mate �� this ��?�BĂ����̂Ƃ���
     * IndefiniteSolutionException �̗�O��?�����?B
     * </p>
     *
     * @param mate ��?�
     * @return ��_
     * @throws IndefiniteSolutionException mate �� this ��?�BĂ���?A�⪕s��ł���
     */
    public IntersectionPoint3D intersect1(Line3D mate)
            throws IndefiniteSolutionException {
        CartesianTransformationOperator3D gtrans = toGlobal();
        Point3D localPnt = gtrans.reverseTransform(mate.pnt());
        Vector3D localDir = gtrans.reverseTransform(mate.dir());
        double dTol = getToleranceForDistance();

        // parallel ?
        Vector3D unitDir = localDir.unitized();
        if (Math.abs(unitDir.z()) < dTol) {
            // overlap ?
            if (Math.abs(localPnt.z()) < dTol) {
                throw new IndefiniteSolutionException(mate.pnt());
            }

            return null;
        }

        // Line3D's parameter at intersection
        double eT = (-localPnt.z() / localDir.z());

        // Gh3Plane's parameter at intersection
        double eU = localPnt.x() + eT * localDir.x();
        double eV = localPnt.y() + eT * localDir.y();

        PointOnSurface3D Apnt = new PointOnSurface3D(this, eU, eV, doCheckDebug);
        PointOnCurve3D Bpnt = new PointOnCurve3D(mate, eT, doCheckDebug);
        IntersectionPoint3D intersectionPoint = new
                IntersectionPoint3D(Apnt, Apnt, Bpnt, doCheckDebug);

        return intersectionPoint;
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * {@link #intersect1(Line3D) intersect1(Line3D)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException mate �� this ��?�BĂ���?A�⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D intp;
        try {
            intp = intersect1(mate);
        } catch (IndefiniteSolutionException e) {
            intp = (IntersectionPoint3D) e.suitable();
            if (doExchange)
                intp = intp.exchange();
            throw new IndefiniteSolutionException(intp);
        }
        if (intp == null)
            return new IntersectionPoint3D[0];

        if (doExchange) {
            intp = intp.exchange();
        }
        IntersectionPoint3D[] ints = {intp};
        return ints;
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�~??��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li> ���̕��� (this) A �Ɨ^����ꂽ�~??��?� (mate) ��?�镽�� B �Ƃ̌�?� I ��?�߂�?B
     * {@link #intersect1Plane(Plane3D) intersect1Plane(Plane3D)} �𗘗p?B
     * <li> A �� B �Ɍ�?�Ȃ����?Athis �� mate �̌�_��Ȃ�?B
     * <li> A �� B ���I?[�o?[���b�v���Ă����?AIndefiniteSolution �̗�O��?�����?B
     * <li> mate �� I �̌�_��?��?A����� this �� mate �̌�_�Ƃ���?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋�?� (�~??��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException mate �� this ��?�BĂ���?A�⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Conic3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        // get osculating plane
        Plane3D osculating_plane = new Plane3D(mate.position());

        // does planes intersect?
        Line3D intAB;
        try {
            intAB = intersect1Plane(osculating_plane);
        } catch (IndefiniteSolutionException ip) {
            // overlap
            Point3D pnt = coordinates(0.0, 0.0);
            throw new IndefiniteSolutionException(pnt);
        }

        if (intAB == null)
            return new IntersectionPoint3D[0];

        // intersection between line & conic
        IntersectionPoint3D[] int_pnt = mate.intersect(intAB, false);

        // make a intersection point plane & conic
        IntersectionPoint3D[] return_pnt = new IntersectionPoint3D[int_pnt.length];
        for (int i = 0; i < int_pnt.length; i++) {

            // intersection AbstractPoint
            Point3D pnt = int_pnt[i].coordinates();

            // plane's parameter
            Axis2Placement3D plane_axis = position();
            Point3D plane_org = plane_axis.location();
            Vector3D dir_vec = pnt.subtract(plane_org);

            // calculate parameter
            double dU = dir_vec.dotProduct(plane_axis.x());
            double dV = dir_vec.dotProduct(plane_axis.y());

            // point on plane(surface)
            PointOnSurface3D PonS =
                    new PointOnSurface3D(this, dU, dV, doCheckDebug);
            // point on conic(curve)
            PointOnGeometry3D PonC = int_pnt[i].pointOnGeometry1();

            // make a intersectionPoint
            if (!doExchange)
                return_pnt[i] = new IntersectionPoint3D(pnt, PonS, PonC, doCheckDebug);
            else
                return_pnt[i] = new IntersectionPoint3D(pnt, PonC, PonS, doCheckDebug);
        }
        return return_pnt;
    }

    /**
     * ���̋ȖʂƑ��̋Ȗʂ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ���?������?��ɂ��Ă�?A��?� (IntersectionCurve3D) ���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ�?ڂ����?��ɂ��Ă�?A��_ (IntersectionPoint3D) ���Ԃ邱�Ƃ�����?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��?� (�܂��͌�_) �̔z��
     * @throws IndefiniteSolutionException mate �ս�ʂ�?A���҂��I?[�o?[���b�v���Ă���?A�⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    public SurfaceSurfaceInterference3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̕��ʂƑ��̕��ʂ̂���������̌�?��?�Ƃ��ċ?�߂�?B
     * <p/>
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * this �� mate �̖@?�x�N�g���̂Ȃ��p�x��
     * �p�x�̋��e��?�����?�����?�?��ɂ�
     * ���҂͌���Ȃ���̂Ƃ��� null ��Ԃ�?B
     * <br>
     * ������?A����?�?��ɂ����
     * ���̕��ʂ̋�?����_���瑼��̕��ʂւ̋�����
     * �����̋��e��?�����?�����?�?��ɂ�
     * ���҂̓I?[�o?[���b�v���Ă����̂Ƃ���
     * IndefiniteSolutionException �̗�O��?�����?B
     * </p>
     *
     * @param mate ���̕���
     * @return ��?��\����?�
     * @throws IndefiniteSolutionException this �� mate ���I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    public Line3D intersect1Plane(Plane3D mate)
            throws IndefiniteSolutionException {
        Point3D pnt;
        Vector3D dir;
        Line3D intersectLine;
        double aTol = getToleranceForAngle();

        // get unitized normal vectors
        Vector3D eAnorm = this.position().z();
        Vector3D eBnorm = mate.position().z();
        Point3D eAloc = this.position().location();
        Point3D eBloc = mate.position().location();
        if (Math.abs(eAnorm.dotProduct(eBnorm)) > Math.cos(aTol)) {
            // 2 planes are parallel
            Vector3D evec = eBloc.subtract(eAloc);
            double dTol = getToleranceForDistance();
            if ((Math.abs(evec.dotProduct(eAnorm)) < dTol) &&
                    (Math.abs(evec.dotProduct(eBnorm)) < dTol)) {
                // 2 planes are overlap
                double dTol2 = getToleranceForDistance2();

                pnt = this.position().location();
                dir = mate.position().x();
                intersectLine = new Line3D(pnt, dir);

                throw new IndefiniteSolutionException(intersectLine);
            }
            return null;
        }

        // direction vector of intersection
        dir = eAnorm.crossProduct(eBnorm).unitized();

        // point of intersection
        Vector3D pdir = dir.crossProduct(eAnorm);
        Line3D perpendicularOfIntersection = new Line3D(eAloc, pdir);
        try {
            pnt = mate.intersect1(perpendicularOfIntersection);
        } catch (IndefiniteSolutionException e) {
            pnt = (Point3D) e.suitable();
        }
        Line3D line = new Line3D(pnt, dir);
        return line;
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * {@link #intersect1Plane(Plane3D) intersect1Plane(Plane3D)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException this �� mate ���I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        Line3D line;
        boolean indefinite = false;
        try {
            line = intersect1Plane(mate);
        } catch (IndefiniteSolutionException e) {
            line = (Line3D) e.suitable();
            indefinite = true;
        }
        if (line == null) {
            return new SurfaceSurfaceInterference3D[0];
        }
        IntersectionCurve3D ints = curveToIntersectionCurve(line, mate, doExchange);
        if (indefinite)
            throw new IndefiniteSolutionException(ints);
        SurfaceSurfaceInterference3D[] sol = {ints};

        return sol;
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * mate �̒�?S���� this �ւ̋����� D?Amate �̔��a�� R �Ƃ���
     * <ul>
     * <li> |D - R| �������̋��e��?�����?��������?Athis �� mate �͈�_�Ō����̂Ƃ�?A��_���Ԃ�?B
     * <li> D �� R ����?��������?A��?���Ԃ�?B���̌�?�̂R�����\���͉~�ł���?B
     * <li> D �� R ����傫�����?A��?�͂Ȃ�?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @see ElementarySurface3D#curveToIntersectionCurve(ParametricCurve3D,ElementarySurface3D,boolean)
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate, boolean doExchange) {
        PointOnSurface3D pcenter = project1From(mate.position().location());
        double dist = pcenter.distance(mate.position().location());
        double d_tol = getToleranceForDistance();

        if (Math.abs(dist - mate.radius()) <= d_tol) {
            /*
            * intersection is a point
            */
            double[] mateParams = mate.pointToParameter(pcenter);
            IntersectionPoint3D intsPnt;
            if (!doExchange)
                intsPnt
                        = new IntersectionPoint3D(this, pcenter.uParameter(), pcenter.vParameter(),
                        mate, mateParams[0], mateParams[1], doCheckDebug);
            else
                intsPnt
                        = new IntersectionPoint3D(mate, mateParams[0], mateParams[1],
                        this, pcenter.uParameter(), pcenter.vParameter(),
                        doCheckDebug);
            SurfaceSurfaceInterference3D[] intf = {intsPnt};
            return intf;
        }

        if (dist > mate.radius()) {
            /*
            * no intersection
            */
            return new SurfaceSurfaceInterference3D[0];
        }

        /*
        * intersection is a circle
        */
        Axis2Placement3D axis
                = new Axis2Placement3D(pcenter.coordinates(), position().z(), position().x());
        double radius = Math.sqrt(mate.radius() * mate.radius() - dist * dist);
        Circle3D res = new Circle3D(axis, radius);
        IntersectionCurve3D ints = curveToIntersectionCurve(res, mate, doExchange);
        SurfaceSurfaceInterference3D[] sol = {ints};
        return sol;
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * <ul>
     * <li> this �̖@?�x�N�g���� mate �̋�?� Z ���̂Ȃ��p�x�Ƃ�
     * �p�x�̋��e��?�����?��������?A��?� (�R�����\���͉~) ���Ԃ�?B
     * <li> |�� - ��| ���p�x�̋��e��?�����?�����?�?�?A
     * mate �̋�?����_���� this �ւ̋����� D?Amate �̔��a�� R �Ƃ���
     * <ul>
     * <li>	|D - R| �������̋��e��?�����?��������?A��?� (�R�����\���͒�?�) ���Ԃ�?B
     * <li>	D �� R ����?��������?A��?� (�R�����\���͒�?�) ���Ԃ�?B
     * <li>	D �� R ����傫�����?Athis �� mate �Ɍ�?�͂Ȃ�?B
     * </ul>
     * <li> ?�L�̂�����ł�Ȃ����?A��?� (�R�����\���͑ȉ~) ���Ԃ�?B
     * </ul>
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @see ElementarySurface3D#curveToIntersectionCurve(ParametricCurve3D,ElementarySurface3D,boolean)
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange) {
        Point3D Borg = mate.position().location();
        Vector3D Bz = mate.position().z();
        PointOnSurface3D pcenter;
        Vector3D ecrs = position().z().crossProduct(Bz);
        double dist;
        double a_tol = getToleranceForAngle();

        if (ecrs.length() < Math.sin(a_tol)) {
            /*
            * intersection is a circle
            */
            pcenter = project1From(Borg);
            Axis2Placement3D axis
                    = new Axis2Placement3D(pcenter.coordinates(), position().z(), position().x());
            double radius = mate.radius();
            Circle3D res = new Circle3D(axis, radius);
            IntersectionCurve3D ints = curveToIntersectionCurve(res, mate, doExchange);
            SurfaceSurfaceInterference3D[] sol = {ints};
            return sol;
        }
        double d_tol = getToleranceForDistance();
        double edot = Math.abs(position().z().dotProduct(Bz));
        if (edot < Math.sin(a_tol)) {
            /*
            * sin(DATL) = cos(PI / 2 - DATL)
            *
            * intersections are lines
            */
            pcenter = project1From(Borg);
            dist = pcenter.distance(Borg);
            if (Math.abs(dist - mate.radius()) < d_tol) {
                /*
                * intersection is a line
                */
                Line3D res = new Line3D(pcenter.coordinates(), Bz);
                IntersectionCurve3D ints = curveToIntersectionCurve(res, mate, doExchange);
                SurfaceSurfaceInterference3D[] sol = {ints};
                return sol;
            }
            if (dist < mate.radius()) {
                /*
                * intersections are 2 lines
                */
                dist = Math.sqrt(mate.radius() * mate.radius() - dist * dist);
                ecrs = ecrs.unitized().multiply(dist);
                SurfaceSurfaceInterference3D[] sol = new SurfaceSurfaceInterference3D[2];
                Line3D res1 = new Line3D(pcenter.add(ecrs), Bz);
                Line3D res2 = new Line3D(pcenter.subtract(ecrs), Bz);
                sol[0] = curveToIntersectionCurve(res1, mate, doExchange);
                sol[1] = curveToIntersectionCurve(res2, mate, doExchange);
                return sol;
            }
            return new SurfaceSurfaceInterference3D[0];
        }

        /*
        * intersection is an ellipse
        */
        CartesianTransformationOperator3D trns = toGlobal();
        Point3D BorginA = trns.toLocal(Borg);
        Vector3D BzinA = trns.toLocal(Bz);
        dist = (-BorginA.z() / BzinA.z());

        Axis2Placement3D axis
                = new Axis2Placement3D(Borg.add(Bz.multiply(dist)),
                position().z(), position().z().crossProduct(ecrs));
        Ellipse3D res = new Ellipse3D(axis, mate.radius() / edot, mate.radius());
        IntersectionCurve3D ints = curveToIntersectionCurve(res, mate, doExchange);
        SurfaceSurfaceInterference3D[] sol = {ints};
        return sol;
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~??��) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsPlnCon3D IntsPlnCon3D}
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate,
                                             boolean doExchange) {
        IntsPlnCon3D doObj = new IntsPlnCon3D(this, mate);
        return doObj.getInterference(doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�x�W�G�Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsQrdBzs3D#intersection(ElementarySurface3D,PureBezierSurface3D,boolean)
     * IntsQrdBzs3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(PureBezierSurface3D mate,
                                             boolean doExchange) {
        return IntsQrdBzs3D.intersection(this, mate, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSrfBss3D#intersection(ElementarySurface3D,BsplineSurface3D,boolean)
     * IntsSrfBss3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(BsplineSurface3D mate,
                                             boolean doExchange) {
        return IntsSrfBss3D.intersection(this, mate, doExchange);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�I�t�Z�b�g�����Ȗʂ�
     * �^����ꂽ��?��ŋߎ����� Bspline �Ȗʂ�?�߂�?B
     * <p/>
     * ���̃?�\�b�h���Ԃ��a�X�v���C���Ȗʂ�?A
     * ���ʂ�I�t�Z�b�g�����Ȗʂ�?�Ɍ�����?Č�����?B
     * ��B�?A���̃?�\�b�h�ł� tol �̒l�͎Q?Ƃ��Ȃ�?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.FRONT/BACK)
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�`��Ԃ̃I�t�Z�b�g�Ȗʂ�ߎ����� Bspline �Ȗ�
     * @see WhichSide
     */
    public BsplineSurface3D
    offsetByBsplineSurface(ParameterSection uPint,
                           ParameterSection vPint,
                           double magni,
                           int side,
                           ToleranceForDistance tol) {
        // ?���_
        int uicp = 4;

        // �m�b�g�z��̗v�f?�
        int uik = 2;

        // �m�b�g�z��
        double[] uknots = new double[2];
        double[] vknots = new double[2];
        int[] uknot_multi = new int[2];
        int[] vknot_multi = new int[2];

        uknots[0] = 0.0;
        uknot_multi[0] = 4;
        uknots[1] = Math.abs(uPint.increase());
        uknot_multi[1] = 4;

        vknots[0] = 0.0;
        vknot_multi[0] = 4;
        vknots[1] = Math.abs(vPint.increase());
        vknot_multi[1] = 4;

        // �p���??[�^�Ԋu
        double uparam_interval = uPint.increase() / (uicp - 1);
        double vparam_interval = vPint.increase() / (uicp - 1);

        // �I�t�Z�b�g�x�N�g��
        Vector3D offset_vector;
        if (side == WhichSide.FRONT)
            offset_vector = normalVector(uPint.start(), vPint.start());
        else if (side == WhichSide.BACK) {
            offset_vector = normalVector(uPint.start(), vPint.start());
            offset_vector = offset_vector.reverse();
        } else
            throw new InvalidArgumentValueException();

        offset_vector = offset_vector.multiply(magni);

        // �I�t�Z�b�g����
        int i, j;
        double crnt_param;
        Point3D[][] crnt_pnt = new Point3D[uicp][uicp];

        for (i = 0; i < uicp; i++) {
            for (j = 0; j < uicp; j++) {
                crnt_pnt[i][j] =
                        coordinates(uPint.start() + i * uparam_interval,
                                vPint.start() + j * vparam_interval);
                crnt_pnt[i][j] = crnt_pnt[i][j].add(offset_vector);
            }
        }

        // B�X�v���C���ǖʃI�t�W�F�N�g��?�?�
        BsplineSurface3D bss =
                new BsplineSurface3D(3, false, uknot_multi, uknots,
                        3, false, vknot_multi, vknots, crnt_pnt);
        return bss;
    }

    /*
    * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param uParam	U ���̃p���??[�^�l
    * @return	�w��� U �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D uIsoParametricCurve(double uParam) {
        Point3D pnt = coordinates(uParam, 0.0);
        Vector3D dir = position().y();
        return new Line3D(pnt, dir);
    }

    /*
    * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param vParam	V ���̃p���??[�^�l
    * @return	�w��� V �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D vIsoParametricCurve(double vParam) {
        Point3D pnt = coordinates(0.0, vParam);
        Vector3D dir = position().x();
        return new Line3D(pnt, dir);
    }

    /*
    * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
    * <p>
    * ���Ŕ���I�Ȓ�`���Ԃ�?B
    * </p>
    *
    * @return	���̋Ȗʂ� U ���̃p���??[�^��`��
    */
    ParameterDomain getUParameterDomain() {
        return new ParameterDomain();
    }

    /*
    * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
    * <p>
    * ���Ŕ���I�Ȓ�`���Ԃ�?B
    * </p>
    *
    * @return	���̋Ȗʂ� V ���̃p���??[�^��`��
    */
    ParameterDomain getVParameterDomain() {
        return new ParameterDomain();
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#PLANE_3D ParametricSurface3D.PLANE_3D}
     */
    int type() {
        return PLANE_3D;
    }

    /**
     * �^����ꂽ�_��?A���̕��ʂ̂ǂ��瑤�ɂ��邩�𒲂ׂ�?B
     * <p/>
     * point �� this �̋�?�?W�n�ł̕\���ɕϊ�������̂� P �Ƃ���?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * P �� Z ?����̒l�������̋��e��?�����傫����� WhichSide.FRONT?A
     * (- �����̋��e��?�) ����?�������� WhichSide.BACK?A
     * �ǂ���ł�Ȃ���� WhichSide.ON
     * ��Ԃ�?B
     * </p>
     *
     * @return WhichSide.{ON, FRONT, BACK} �̂����ꂩ
     * @see WhichSide
     */
    int pointIsWhichSide(Point3D point) {
        double d_tol = getToleranceForDistance();

        /*
        * transform dApnt into plane's coordinates system
        */
        CartesianTransformationOperator3D trns
                = new CartesianTransformationOperator3D(position(), 1.0);
        Point3D eApnt = trns.toLocal(point);

        /*
        * judgement
        */
        int result;

        if (eApnt.z() > d_tol)
            result = WhichSide.FRONT;
        else if (eApnt.z() < (-d_tol))
            result = WhichSide.BACK;
        else
            result = WhichSide.ON;

        return result;
    }

    /**
     * �^����ꂽ�_�Q�����ꕽ��?�ɂ��邩 (���ʂ�Ȃ���) �ǂ����𒲂ׂ�?B
     * <p/>
     * �^����ꂽ�_�̔z��̗v�f?��� 2 �ȉ���?�?��ɂ̓[�?�x�N�g����Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�_�Q����_��?k�ނ��Ă��邩?A���邢�͋�?�?�Ԃɂ���?�?��ɂ�
     * �[�?�x�N�g����Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�_�Q������?�ԂɂȂ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param points �_�Q
     * @return �_�Q��?�BĂ��镽�ʂ̒P�ʖ@?�x�N�g��
     */
    public static Vector3D coplaner(Point3D[] points) {
        int npnts = points.length;
        if (npnts <= 2)
            return Vector3D.zeroVector();

        Vector3D[] spanPlane = new Vector3D[2];
        ConditionOfOperation condition = ConditionOfOperation.getCondition();
        double dTol2 = condition.getToleranceForDistance2();
        double aTol = condition.getToleranceForAngle();
        int k = 0;
        Vector3D evec;

        // make the reference plane
        for (int j = 1; j < npnts; j++) {
            evec = points[j].subtract(points[0]);
            if (evec.norm() > dTol2) {
                evec = evec.unitized();
                if ((k != 0) && (Math.abs(evec.dotProduct(spanPlane[0])) > Math.cos(aTol)))
                    continue;
                spanPlane[k] = evec;
                if ((++k) == 2) break;
            }
        }
        if (k != 2) {
            // points are concurrent or collinear
            return Vector3D.zeroVector();
        }

        Vector3D uax = spanPlane[0].crossProduct(spanPlane[1]).unitized();
        for (int j = 1; j < npnts; j++) {
            evec = points[j].subtract(points[0]);
            if (Math.abs(evec.dotProduct(uax)) > condition.getToleranceForDistance())
                return null;
        }
        return uax;
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * �^����ꂽ��?��ŕ��ʋߎ�����_�Q��Ԃ�?B
     * <p/>
     * ?��?���ʂƂ��ē�����_�Q�͈�ʂ�?A�ʑ��I�ɂ�􉽓I�ɂ�?A�i�q?�ł͂Ȃ�?B
     * </p>
     * <p/>
     * scalingFactor ��?A��͗p�ł͂Ȃ�?A?o�͗p�̈�?��ł���?B
     * scalingFactor �ɂ�?A�v�f?� 2 �̔z���^����?B
     * scalingFactor[0] �ɂ� U ����?k�ڔ{��?A
     * scalingFactor[1] �ɂ� V ����?k�ڔ{�����Ԃ�?B
     * �����̒l�͉��炩��?�Βl�ł͂Ȃ�?A
     * �p���??[�^��?i�ޑ��x T �ɑ΂���?A
     * U/V �����ɂ��Ď��?�ŋȖ�?�̓_��?i�ޑ��x Pu/Pv ��\�����Βl�ł���?B
     * �܂�?A�p���??[�^�� T ����?i�ނ�?A
     * ���?�ł̋Ȗ�?�̓_�� U ���ł� Pu (scalingFactor[0])?A
     * V ���ł� Pv (scalingFactor[1]) ����?i�ނ��Ƃ�\���Ă���?B
     * T �̑傫���͖�������Ȃ��̂�?A���̒l��Q?Ƃ���?ۂɂ�?A
     * scalingFactor[0] �� scalingFactor[1] �̔䂾����p����ׂ��ł���?B
     * �Ȃ�?A�����̒l�͂����܂ł�ڈł���?A�����ȑ��x����̂ł͂Ȃ�?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ� Vector �Ɋ܂܂��e�v�f��
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D
     * �ł��邱�Ƃ���҂ł���?B
     * </p>
     *
     * @param uParameterSection U ���̃p���??[�^���
     * @param vParameterSection V ���̃p���??[�^���
     * @param tolerance         �����̋��e��?�
     * @param scalingFactor     �_�Q��O�p�`��������?ۂɗL�p�Ǝv���� U/V ��?k�ڔ{��
     * @return �_�Q��܂� Vector
     * @see PointOnSurface3D
     */
    public Vector toNonStructuredPoints(ParameterSection uParameterSection,
                                        ParameterSection vParameterSection,
                                        double tolerance,
                                        double[] scalingFactor) {
        Vector result = new Vector();
        double uParameter;
        double vParameter;
        PointOnSurface3D point;

        for (int u = 0; u < 2; u++) {
            uParameter = (u == 0)
                    ? uParameterSection.start() : uParameterSection.end();
            for (int v = 0; v < 2; v++) {
                vParameter = (v == 0)
                        ? vParameterSection.start() : vParameterSection.end();
                point = new PointOnSurface3D(this, uParameter, vParameter, doCheckDebug);
                result.addElement(point);
            }
        }

        scalingFactor[0] = 1.0;
        scalingFactor[1] = 1.0;

        return result;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� this ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param reverseTransform       �t�ϊ�����̂ł���� true?A�����łȂ���� false
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̊􉽗v�f
     */
    protected synchronized ParametricSurface3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Axis2Placement3D tPosition =
                this.position().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new Plane3D(tPosition);
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     * @see GeometryElement
     */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);

        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tposition");
        position().output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
