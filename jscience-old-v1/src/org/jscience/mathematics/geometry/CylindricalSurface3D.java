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

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : �~���ʂ�\���N���X?B
 * <p/>
 * �~���ʂ�?A���̒�?S�̈ʒu�Ƌ�?� X/Y/Z ���̕�����?�?W�n
 * (�z�u?��?A{@link Axis2Placement3D Axis2Placement3D}) position ��
 * ���a radius �Œ�`�����?B
 * </p>
 * <p/>
 * �~���ʂ�
 * U ���̃p���??[�^��`��͗L�Ŏ��I�ł���?A
 * ���̃v���C�}���ȗL���Ԃ� [0, (2 * ��)] �ł���?B
 * V ���̃p���??[�^��`��͖��Ŕ���I�ł���?B
 * </p>
 * <p/>
 * (u, v) ��p���??[�^�Ƃ���~���� P(u, v) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	P(u, v) = c + radius * (cos(u) * x + sin(u) * y) + v * z
 * </pre>
 * ������?Ac, x, y, z �͂��ꂼ��
 * <pre>
 * 	c : position.location()
 * 	x : position.x()
 * 	y : position.y()
 * 	z : position.z()
 * </pre>
 * ��\��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.5 $, $Date: 2006/05/20 23:25:41 $
 */

public class CylindricalSurface3D extends ElementarySurface3D {
    /**
     * ���a?B
     *
     * @serial
     */
    private double radius;

    /**
     * ���a��?ݒ肷��?B
     * <p/>
     * radius �̒l��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param radius ���a
     * @see InvalidArgumentValueException
     */
    private void setRadius(double radius) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();

        if (radius < dTol) {
            throw new InvalidArgumentValueException();
        }
        this.radius = radius;
    }

    /**
     * ��?�?W�n�Ɣ��a��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * position �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * radius �̒l��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param position ��?����_�Ƌ�?� X/Y/Z ���̕�����?�?W�n
     * @param radius   ���a
     * @see InvalidArgumentValueException
     */
    public CylindricalSurface3D(Axis2Placement3D position,
                                double radius) {
        super(position);
        setRadius(radius);
    }

    /**
     * ��?S���Ɣ��a��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * cntr �µ���� axis �� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * radius �̒l��?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����?�����?�?��ɂ�
     * InvalidArgumentValueException	�̗�O��?�����?B
     * </p>
     *
     * @param cntr   ��?����_
     * @param axis   ��?� Z �����
     * @param radius ���a
     * @see InvalidArgumentValueException
     */
    public CylindricalSurface3D(Point3D cntr, Vector3D axis,
                                double radius) {
        super(new Axis2Placement3D(cntr, axis,
                axis.verticalVector().unitized()));
        setRadius(radius);
    }

    /**
     * ���̉~���ʂ̔��a��Ԃ�?B
     *
     * @return ���a
     */
    public double radius() {
        return this.radius;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     *
     * @param uParam U ���p���??[�^�l
     * @param vParam V ���p���??[�^�l
     * @return ?W�l
     */
    public Point3D coordinates(double uParam, double vParam) {
        CartesianTransformationOperator3D gtrans = toGlobal();
        double x = radius * Math.cos(uParam);
        double y = radius * Math.sin(uParam);
        Point3D pnt = new CartesianPoint3D(x, y, vParam);

        return gtrans.transform(pnt);
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
        CartesianTransformationOperator3D gtrans = toGlobal();
        double dux = -radius * Math.sin(uParam);
        double duy = radius * Math.cos(uParam);
        Vector3D dup = new LiteralVector3D(dux, duy, 0.0);
        Vector3D dvp = Vector3D.zUnitVector;

        Vector3D[] tang = {gtrans.transform(dup), gtrans.transform(dvp)};
        return tang;
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
        CartesianTransformationOperator3D gtrans = toGlobal();
        double x = radius * Math.cos(uParam);
        double y = radius * Math.sin(uParam);
        Vector3D nrm = new LiteralVector3D(x, y, 0.0);

        return gtrans.transform(nrm).unitized();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̎�ȗ�?���Ԃ�?B
     * <p/>
     * ��ȗ�1 (principalCurvature1) �ɂ� (- 1 / radius)?A
     * ��ȗ�2 (principalCurvature2) �ɂ� 0?A
     * ����x�N�g��1 (principalDirection1) �ɂ� U �p���??[�^�ɂ��Ă�?ڃx�N�g���̒P�ʃx�N�g��?A
     * ����x�N�g��2 (principalDirection2) �ɂ� V �p���??[�^�ɂ��Ă�?ڃx�N�g���̒P�ʃx�N�g��
     * ��Ԃ�?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ��ȗ�?��
     */
    public SurfaceCurvature3D curvature(double uParam, double vParam) {
        Vector3D[] tangent = tangentVector(uParam, vParam);

        return new SurfaceCurvature3D(-1.0 / radius, tangent[0].unitized(),
                0, tangent[1].unitized());
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ�?B
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     */
    public SurfaceDerivative3D evaluation(double uParam, double vParam) {
        CartesianTransformationOperator3D gtrans = toGlobal();
        double x = radius * Math.cos(uParam);
        double y = radius * Math.sin(uParam);
        // p    = (x, y, vParam)
        // dup  = (-y, x, 0)
        // dvp  = (0, 0, 1)
        // duup = (-x, -y, 0);
        // duvp = (0, 0, 0)
        // dvvp = (0, 0, 0)

        Point3D pnt = new CartesianPoint3D(x, y, vParam);
        Vector3D dup = new LiteralVector3D(-y, x, 0.0);
        Vector3D dvp = Vector3D.zUnitVector;
        Vector3D duup = new LiteralVector3D(-x, -y, 0.0);
        Vector3D zerov = Vector3D.zeroVector;

        return new SurfaceDerivative3D(gtrans.transform(pnt),
                gtrans.transform(dup),
                gtrans.transform(dvp),
                gtrans.transform(duup),
                gtrans.transform(zerov),
                gtrans.transform(zerov));
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * �^����ꂽ�_�����̉~���ʂ̒�?S��?�ɂȂ���?A
     * ?�ɓ�̓��e�_��Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�_�Ƃ��̉~���ʂ̒�?S���Ƃ̋�����?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?�����
     * ?�����?�?��ɂ�?A
     * IndefiniteSolutionException �̗�O�𓊂���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s�� (���e���̓_���~���ʂ̒�?S��?�ɂ���)
     */
    public PointOnSurface3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        CartesianTransformationOperator3D gtrans = toGlobal();
        Point3D lpoint = gtrans.reverseTransform(point);
        double z = lpoint.z();
        Point3D apoint = new CartesianPoint3D(0.0, 0.0, z);
        Vector3D eduvec = lpoint.subtract(apoint);

        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double dTol = Math.abs(condition.getToleranceForDistance());

        if (eduvec.length() < dTol) {
            PointOnSurface3D p;

            try {
                p = new PointOnSurface3D(this, 0.0, z, doCheckDebug);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

            throw new IndefiniteSolutionException(p);
        }
        eduvec = eduvec.unitized();

        // get vector angle
        double u = Math.atan2(eduvec.y(), eduvec.x());

        if (u < 0)
            u += 2 * Math.PI;
        PointOnSurface3D foot1 = new PointOnSurface3D(this, u, z, doCheckDebug);
        u += Math.PI;
        if (u >= 2 * Math.PI)
            u -= 2 * Math.PI;
        PointOnSurface3D foot2 = new PointOnSurface3D(this, u, z, doCheckDebug);

        PointOnSurface3D[] proj = {foot1, foot2};

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
    toMesh(ParameterSection uPint,
           ParameterSection vPint,
           ToleranceForDistance tol) {
        return this.makeMesh(1, uPint, vPint, tol);
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
     * @param meshType toMesh ����Ȃ� 1 ?AtoNonStructuredPoints ����Ȃ� 2
     * @param uPint    U ���̃p���??[�^���
     * @param vPint    V ���̃p���??[�^���
     * @param tol      �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see #toMesh(ParameterSection,ParameterSection,ToleranceForDistance)
     * @see #toNonStructuredPoints(ParameterSection,ParameterSection,double,double[])
     */
    private Mesh3D
    makeMesh(int meshType,
             ParameterSection uPint,
             ParameterSection vPint,
             ToleranceForDistance tol) {
        PointOnSurface3D[][] mesh;
        double uParam;
        double vStart = vPint.start();
        double vEnd = vPint.end();
        double vMiddle = (vStart + vEnd) / 2.0;
        Polyline3D pol;
        int u_npnts;
        PointOnCurve3D poc;
        Point3D pnt;
        Point3D pnt2;
        Vector3D vec;
        Vector3D vec2;
        int i;

        /*
        * V���̊J�n�ʒu�ł̒f�ʂƂȂ�~��?��?A
        * ����ɉ~��|�����C���ߎ�����?B
        * ����?ߓ_��V���J�n�ʒu�ł̊i�q�_�ƂȂ�?B
        */
        try {
            CartesianTransformationOperator3D gtrans = toGlobal();
            Point3D cntr;
            Axis2Placement3D pos;
            Circle3D cir;

            pnt = new CartesianPoint3D(0.0, 0.0, vStart);    // ��?S��?�߂�?B
            cntr = gtrans.transform(pnt);

            pos = new Axis2Placement3D(cntr, position().z(), position().x());
            cir = new Circle3D(pos, radius());        // �~��?�߂�?B

            pol = cir.toPolyline(uPint, tol);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }

        /*
        * V���̊J�n�_�̊i�q�_��V����?I���_�փR�s?[��?A
        * �i�q�_��?\?�����?B
        */
        u_npnts = pol.nPoints();
        vec = position().z().multiply(vPint.increase());

        if (meshType == 1) {
            vec2 = null;
            mesh = new PointOnSurface3D[u_npnts][2];
        } else {
            vec2 = position().z().multiply(vPint.increase() / 2.0);
            mesh = new PointOnSurface3D[u_npnts][3];
        }

        for (i = 0; i < u_npnts; i++) {
            try {
                poc = (PointOnCurve3D) pol.pointAt(i);
                pnt = new CartesianPoint3D(poc.x(), poc.y(), poc.z());
                uParam = poc.parameter();
                mesh[i][0] = new PointOnSurface3D(pnt, this, uParam, vStart, doCheckDebug);
                if (meshType == 1) {
                    pnt = pnt.add(vec);
                    mesh[i][1] = new PointOnSurface3D(pnt, this, uParam, vEnd, doCheckDebug);
                } else {
                    pnt2 = pnt.add(vec2);
                    mesh[i][1] = new PointOnSurface3D(pnt, this, uParam, vMiddle, doCheckDebug);
                    pnt = pnt.add(vec);
                    mesh[i][2] = new PointOnSurface3D(pnt, this, uParam, vEnd, doCheckDebug);
                }
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
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
        Circle2D circle2D =
                new Circle2D(Axis2Placement2D.origin, this.radius());

        BsplineCurve2D uBsplineCurve2D = circle2D.toBsplineCurve(uPint);

        int uNPoints = uBsplineCurve2D.nControlPoints();
        int vNPoints = 2;

        Point3D[][] controlPoints = new Point3D[uNPoints][vNPoints];
        double[][] weights = new double[uNPoints][vNPoints];

        CartesianTransformationOperator3D localTransformationOperator =
                this.position().toCartesianTransformationOperator(1.0);

        double vLowerCoord = vPint.start();
        double vUpperCoord = vPint.end();

        for (int ui = 0; ui < uNPoints; ui++) {
            Point2D uPoint = uBsplineCurve2D.controlPointAt(ui);
            controlPoints[ui][0] =
                    localTransformationOperator.toEnclosed(uPoint.to3D(vLowerCoord));
            controlPoints[ui][1] =
                    localTransformationOperator.toEnclosed(uPoint.to3D(vUpperCoord));
            weights[ui][0] = weights[ui][1] = uBsplineCurve2D.weightAt(ui);
        }

        return new BsplineSurface3D(uBsplineCurve2D.knotData(),
                BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
                controlPoints, weights);
    }

    /**
     * �^����ꂽ�_�����̋Ȗ�?�ɂ��邩�ۂ���`�F�b�N����?B
     *
     * @param point ?��?��?ۂƂȂ�_
     * @return �^����ꂽ�_�����̋Ȗ�?�ɂ���� true?A�����łȂ���� false
     */
    boolean checkSolution(Point3D point) {
        double dTol = getToleranceForDistance();
        double dist = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        return Math.abs(dist - radius()) < dTol;
    }

    /**
     * ����?����Ȗʂ� (��?����\�����ꂽ) ���R��?�̌�_��\����?�����?�?�����?B
     *
     * @param poly �x�W�G��?�邢�͂a�X�v���C����?�̂���Z�O�?���g�̑�?����\���̔z��
     * @return ����?����Ȗʂ� poly �̌�_��\����?�����?���
     */
    DoublePolynomial makePoly(DoublePolynomial[] poly) {
        DoublePolynomial xPoly = (DoublePolynomial) poly[0].multiply(poly[0]);
        DoublePolynomial yPoly = (DoublePolynomial) poly[1].multiply(poly[1]);
        double radius2 = this.radius() * this.radius();
        boolean isPoly = poly.length < 4;
        int degree = xPoly.degree();
        double[] coef = new double[degree + 1];
        if (isPoly) {
            for (int j = 0; j <= degree; j++) {
                coef[j] = xPoly.getCoefficientAsDouble(j) +
                        yPoly.getCoefficientAsDouble(j);
            }
            coef[0] -= radius2;
        } else {
            DoublePolynomial wPoly = (DoublePolynomial) poly[3].multiply(poly[3]);
            for (int j = 0; j <= degree; j++) {
                coef[j] = xPoly.getCoefficientAsDouble(j) +
                        yPoly.getCoefficientAsDouble(j) -
                        (radius2 * wPoly.getCoefficientAsDouble(j));
            }
        }
        return new DoublePolynomial(coef);
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?� (��?�)
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        // tolerance
        double aTol = getToleranceForAngle();
        double dTol = getToleranceForDistance();

        Vector3D unit_dir = mate.dir().unitized(); // line's unit dir
        Vector3D cyl_axis = position().z();        // cylinder's axis
        Point3D cyl_org = position().location();   // cylinder's origin

        if (Math.abs(unit_dir.dotProduct(cyl_axis)) > Math.cos(aTol)) {
            // Line & Cylinder are parallel
            Vector3D pnt_to_org = cyl_org.subtract(mate.pnt());
            double innerProduct = pnt_to_org.dotProduct(unit_dir);
            double[] pnt = new double[3];
            pnt[0] = mate.pnt().x() + innerProduct * unit_dir.x();
            pnt[1] = mate.pnt().y() + innerProduct * unit_dir.y();
            pnt[2] = mate.pnt().z() + innerProduct * unit_dir.z();
            CartesianPoint3D projected_org = new CartesianPoint3D(pnt[0], pnt[1], pnt[2]);
            Vector3D org_to_line = projected_org.subtract(cyl_org);
            if (Math.abs(org_to_line.length() - radius()) < dTol) {
                // overlap
                throw new IndefiniteSolutionException(mate.pnt());
            }
            return new IntersectionPoint3D[0];
        }

        // transform line to cylinder's local coordinates
        CartesianTransformationOperator3D trans = new
                CartesianTransformationOperator3D(position(), 1.0);

        Point3D ppnt = trans.reverseTransform(mate.pnt());
        Vector3D pvector = trans.reverseTransform(mate.dir());
        Line3D translated_line = new Line3D(ppnt, pvector);

        // resolve in 2D
        CartesianPoint2D cir_org = new CartesianPoint2D(0.0, 0.0);
        Circle2D circle = new Circle2D(cir_org, radius());
        Line2D line2 = mate.toLocal2D(trans);

        IntersectionPoint2D[] int_pnt2d = new IntersectionPoint2D[0];

        try {
            int_pnt2d = circle.intersect(line2);
        } catch (IndefiniteSolutionException e) {
            // IndefiniteSolutionException doesnt happen !
        }

        if (int_pnt2d.length == 0)
            return new IntersectionPoint3D[0];

        IntersectionPoint3D[] int_pnt = new IntersectionPoint3D[int_pnt2d.length];
        double cyl_param;
        double line_param;

        for (int i = 0; i < int_pnt2d.length; i++) {
            line_param = int_pnt2d[i].pointOnCurve2().parameter();
            cyl_param = int_pnt2d[i].pointOnCurve1().parameter();
            Point3D pnt = mate.coordinates(line_param);

            double height_param =
                    translated_line.pnt().z() + line_param * translated_line.dir().z();

            // point on line
            PointOnCurve3D PonC = new PointOnCurve3D(pnt, mate, line_param, doCheckDebug);

            // point on surface
            PointOnSurface3D PonS =
                    new PointOnSurface3D(pnt, this, cyl_param, height_param, doCheckDebug);

            // intersection point
            if (doExchange)
                int_pnt[i] = new IntersectionPoint3D(pnt, PonC, PonS, doCheckDebug);
            else
                int_pnt[i] = new IntersectionPoint3D(pnt, PonS, PonC, doCheckDebug);
        }
        return int_pnt;
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
     * @throws IndefiniteSolutionException mate ��~���ʂ�?A���҂��I?[�o?[���b�v���Ă���?A�⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    public SurfaceSurfaceInterference3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        return mate.intersect(this, true);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���ʂ̃N���X��?u���� vs. �~����?v�̌�?�?�\�b�h
     * {@link Plane3D#intersect(CylindricalSurface3D,boolean)
     * Plane3D.intersect(CylindricalSurface3D, boolean)}
     * ��Ă�?o���Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsSphCyl3D#intersection(SphericalSurface3D,CylindricalSurface3D,boolean)
     * IntsSphCyl3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate,
                                             boolean doExchange) {
        return IntsSphCyl3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsCylCyl3D#intersection(CylindricalSurface3D,CylindricalSurface3D,boolean)
     * IntsCylCyl3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException ���҂��I?[�o?[���b�v���Ă���?A�⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        return IntsCylCyl3D.intersection(this, mate, doExchange);
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
     * {@link IntsCylCon3D#intersection(CylindricalSurface3D,ConicalSurface3D,boolean)
     * IntsCylCon3D.intersection}(this, mate, doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate,
                                             boolean doExchange) {
        return IntsCylCon3D.intersection(this, mate, doExchange);
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
        Ofst3D doObj = new Ofst3D(this, uPint, vPint, magni, side, tol);
        return doObj.offset();
    }

    /**
     * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
     *
     * @param uParam U ���̃p���??[�^�l
     * @return �w��� U �p���??[�^�l�ł̓��p���??[�^��?�
     */
    public ParametricCurve3D uIsoParametricCurve(double uParam) {
        Point3D pnt = coordinates(uParam, 0.0);
        Vector3D dir = position().z();
        return new Line3D(pnt, dir);
    }

    /**
     * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
     *
     * @param vParam V ���̃p���??[�^�l
     * @return �w��� V �p���??[�^�l�ł̓��p���??[�^��?�
     */
    public ParametricCurve3D vIsoParametricCurve(double vParam) {
        Point3D pnt = position().location().add(position().z().multiply(vParam));
        Axis2Placement3D a2p = new Axis2Placement3D(pnt, position().z(), position().x());
        return new Circle3D(a2p, radius());
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * �L�Ŏ��I�Ȓ�`���Ԃ�?B
     * </p>
     *
     * @return ���̋Ȗʂ� U ���̃p���??[�^��`��
     */
    ParameterDomain getUParameterDomain() {
        try {
            return new ParameterDomain(true, 0, 2 * Math.PI);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * ���Ŕ���I�Ȓ�`���Ԃ�?B
     * </p>
     *
     * @return ���̋Ȗʂ� V ���̃p���??[�^��`��
     */
    ParameterDomain getVParameterDomain() {
        return new ParameterDomain();
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#CYLINDRICAL_SURFACE_3D ParametricSurface3D.CYLINDRICAL_SURFACE_3D}
     */
    int type() {
        return CYLINDRICAL_SURFACE_3D;
    }

    /**
     * ���̉~���� (�̒�?S��) ��?A�^����ꂽ�~���� (�̒�?S��) ����?s���ǂ����𔻒肷��?B
     * <p/>
     * ��̉~���ʂ̋�?� Z ���̂Ȃ��p�x�� ���邢�� (�� - ��) ��
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̊p�x�̋��e��?�����?��������
     * ��?s�ł����̂Ƃ���?B
     * </p>
     *
     * @param mate �����?ۂ̉~����
     * @return ��?s�ł���� true?A�����łȂ���� false
     */
    boolean isParallel(CylindricalSurface3D mate) {
        Vector3D thisAxis = this.position().z();
        Vector3D mateAxis = mate.position().z();
        double dot = thisAxis.dotProduct(mateAxis);

        return Math.abs(dot) > Math.cos(getToleranceForAngle());
    }

    /**
     * ���̉~���ʂ�?A�^����ꂽ�~���ʂ�����̉~���ʂƂ݂Ȃ��邩�ǂ����𔻒肷��?B
     *
     * @param mate �����?ۂ̉~����
     * @return ����̉~���ʂƂ݂Ȃ���� true?A�����łȂ���� false
     */
    boolean equals(CylindricalSurface3D mate) {
        if (isParallel(mate)) {
            Point3D source = mate.position().location();
            PointOnCurve3D[] foot = getAxis().projectFrom(source);

            if (foot.length != 1) throw new FatalException();

            double dist = source.distance(foot[0]);
            double dTol = getToleranceForDistance();

            if (dist < dTol)
                if (Math.abs(radius() - mate.radius()) < dTol)
                    return true;
        }
        return false;
    }

    /**
     * ���̉~���ʂ̒�?S���ƂȂ钼?��Ԃ�?B
     *
     * @return ��?S������?�
     */
    Line3D getAxis() {
        return new Line3D(position().location(), position().z());
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

        Mesh3D mesh = this.makeMesh(2, uParameterSection, vParameterSection,
                new ToleranceForDistance(tolerance));

        for (int u = 0; u < mesh.uNPoints(); u++)
            for (int v = 0; v < mesh.vNPoints(); v++)
                result.addElement(mesh.pointAt(u, v));

        scalingFactor[0] = this.radius();
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
        double tRadius;
        if (reverseTransform != true)
            tRadius = transformationOperator.transform(this.radius());
        else
            tRadius = transformationOperator.reverseTransform(this.radius());
        return new CylindricalSurface3D(tPosition, tRadius);
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
        writer.println(indent_tab + "\tradius\t" + radius);
        writer.println(indent_tab + "End");
    }
}
