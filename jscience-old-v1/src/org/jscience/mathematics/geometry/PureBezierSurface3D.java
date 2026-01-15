/*
 * �R���� : ��L�? (��?���) �x�W�G�Ȗʂ���їL�?�x�W�G�Ȗʂ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PureBezierSurface3D.java,v 1.4 2006/03/01 21:16:09 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �R���� : ��L�? (��?���) �x�W�G�Ȗʂ���їL�?�x�W�G�Ȗʂ�\���N���X?B
 * <p/>
 * ���̃N���X�ɓWL�ȑ�?���\���t�B?[���h�͓BɂȂ�?B
 * ?���_��Ȃǂ�ێ?����t�B?[���h�ɂ��Ă�?A
 * {@link FreeformSurfaceWithControlPoints3D �X?[�p?[�N���X�̉�?�} ��Q?�?B
 * </p>
 * <p/>
 * �x�W�G�Ȗʂ̃p���??[�^��`��� U/V �����Ƃ�ɗL�Ŕ���I�ł���?A
 * ���̗L���Ԃ� [0, 1] �ł���?B
 * </p>
 * <p/>
 * (u, v) ��p���??[�^�Ƃ���x�W�G�Ȗ� P(u, v) �̃p���?�g���b�N�\����?A�ȉ��̒ʂ�?B
 * <pre>
 * 	m = U ����?���_��?� - 1
 * 	n = V ����?���_��?� - 1
 * 	bi,j = controlPoints[i][j]
 * 	wi,j = weights[i][j]
 * </pre>
 * �Ƃ���?A��L�?�x�W�G�Ȗʂ�
 * <pre>
 * 	P(u, v) = ((bi,j * Bm,i(u)) �̑?�a) * Bn,j(v) �̑?�a	(i = 0, ..., m, j = 0, ..., n)
 * </pre>
 * �L�?�x�W�G�Ȗʂ�
 * <pre>
 * 		  ((wi,j * bi,j * Bm,i(u)) �̑?�a) * Bn,j(v) �̑?�a
 * 	P(u, v) = ------------------------------------------------- 	(i = 0, ..., m, j = 0, ..., n)
 * 		  ((wi,j * Bm,i(u)) �̑?�a) * Bn,j(v) �̑?�a
 * </pre>
 * ������ Bm,i(u), Bn,j(v) �̓o?[���X�^�C������?��ł���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.4 $, $Date: 2006/03/01 21:16:09 $
 */

public class PureBezierSurface3D extends FreeformSurfaceWithControlPoints3D {
    /**
     * ?���_���^���đ�?����ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(Point3D[][])
     * super}(controlPoints)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param controlPoints ?���_�̔z��
     */
    public PureBezierSurface3D(Point3D[][] controlPoints) {
        super(controlPoints);
    }

    /**
     * ?���_���?d�ݗ��^���ėL�?�ȖʂƂ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(Point3D[][],double[][])
     * super}(controlPoints, weights)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param controlPoints ?���_�̔z��
     * @param weights       ?d�݂̔z��
     */
    public PureBezierSurface3D(Point3D[][] controlPoints, double[][] weights) {
        super(controlPoints, weights);
    }

    /**
     * ?���_���?d�ݗ��^����
     * ��?����Ȗ� (���邢�͗L�?�Ȗ�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(Point3D[][],double[][],boolean)
     * super}(controlPoints, weights, doCheck)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param controlPoints ?���_�̔z��?B
     * @param weights       ?d�݂̔z��
     * @param doCheck       ��?��̃`�F�b�N�ⷂ邩�ǂ���
     */
    PureBezierSurface3D(Point3D[][] controlPoints,
                        double[][] weights,
                        boolean doCheck) {
        super(controlPoints, weights, doCheck);
    }

    /**
     * ?���_ (��?d��) ��O�����z��ŗ^����
     * ��?����Ȗ� (���邢�͗L�?�Ȗ�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(double[][][])
     * super}(cpArray)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param cpArray ?���_ (�����?d��) �̔z��
     */
    PureBezierSurface3D(double[][][] cpArray) {
        super(cpArray);
    }

    /**
     * ?���_ (��?d��) ��O�����z��ŗ^����
     * ��?����Ȗ� (���邢�͗L�?�Ȗ�) �Ƃ��ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^��?A
     * {@link FreeformSurfaceWithControlPoints3D#FreeformSurfaceWithControlPoints3D(double[][][],boolean)
     * super}(cpArray, doCheck)
     * ��Ă�?o���Ă��邾���ł���?B
     * </p>
     *
     * @param cpArray ?���_ (�����?d��) �̔z��
     * @param doCheck ��?��̃`�F�b�N�ⷂ邩�ǂ���
     */
    PureBezierSurface3D(double[][][] cpArray,
                        boolean doCheck) {
        super(cpArray, doCheck);
    }

    /**
     * ���̋Ȗʂ� U ���̎�?���Ԃ�?B
     *
     * @return U ���̎�?�
     */
    public int uDegree() {
        return uNControlPoints() - 1;
    }

    /**
     * ���̋Ȗʂ� V ���̎�?���Ԃ�?B
     *
     * @return V ���̎�?�
     */
    public int vDegree() {
        return vNControlPoints() - 1;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?W�l
     * @see ParameterOutOfRange
     */
    public Point3D coordinates(double uParam, double vParam) {
        double[][][] cntlPnts;
        int uUicp, vUicp;
        double[][] bzc;
        double[] d0D;
        boolean isPoly = isPolynomial();

        uParam = checkUParameter(uParam);
        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray(isPoly);
        uUicp = cntlPnts.length;
        vUicp = cntlPnts[0].length;
        bzc = new double[uUicp][];

        /*
        * map for V-direction
        */
        for (int i = 0; i < uUicp; i++) {
            bzc[i] = PureBezierCurveEvaluation.coordinates(cntlPnts[i], vParam);
        }

        /*
        * map for U-direction
        */
        d0D = PureBezierCurveEvaluation.coordinates(bzc, uParam);
        if (!isPoly) {
            convRational0Deriv(d0D);
        }
        return new CartesianPoint3D(d0D);
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
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?ڃx�N�g��
     * @see ParameterOutOfRange
     */
    public Vector3D[] tangentVector(double uParam, double vParam) {
        double[][][] cntlPnts;
        int uUicp, vUicp;
        double[][] pp, dd, tt;
        double[][] ld1D = new double[2][];
        Vector3D[] d1D = new Vector3D[2];
        boolean isPoly = isPolynomial();

        uParam = checkUParameter(uParam);
        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray(isPoly);
        uUicp = cntlPnts.length;
        vUicp = cntlPnts[0].length;
        pp = new double[uUicp][4];
        tt = new double[uUicp][4];

        /*
        * map for V-direction
        */
        for (int i = 0; i < uUicp; i++) {
            PureBezierCurveEvaluation.evaluation(cntlPnts[i], vParam,
                    pp[i], tt[i], null, null);
        }

        /*
        * map for U-direction
        */
        ld1D[0] = new double[4];
        if (isPoly) {
            PureBezierCurveEvaluation.evaluation(pp, uParam, null, ld1D[0], null, null);
            ld1D[1] = PureBezierCurveEvaluation.coordinates(tt, uParam);
        } else {
            double[] ld0D = new double[4];
            PureBezierCurveEvaluation.evaluation(pp, uParam, ld0D, ld1D[0], null, null);
            ld1D[1] = PureBezierCurveEvaluation.coordinates(tt, uParam);
            convRational1Deriv(ld0D, ld1D[0], ld1D[1]);
        }
        for (int i = 0; i < 2; i++) {
            d1D[i] = new LiteralVector3D(ld1D[i]);
        }
        return d1D;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     * @see ParameterOutOfRange
     */
    public SurfaceDerivative3D evaluation(double uParam, double vParam) {
        double[][][] cntlPnts;
        int uUicp, vUicp;
        double[][] pp, tt, dd;
        double[] ld0, ldu, ldv, lduu, lduv, ldvv;
        Point3D d0;
        Vector3D du, dv, duu, duv, dvv;
        boolean isPoly = isPolynomial();

        uParam = checkUParameter(uParam);
        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray(isPoly);
        uUicp = cntlPnts.length;
        vUicp = cntlPnts[0].length;
        pp = new double[uUicp][4];
        tt = new double[uUicp][4];
        dd = new double[uUicp][4];

        /*
        * map for V-direction
        */
        for (int i = 0; i < uUicp; i++) {
            PureBezierCurveEvaluation.evaluation(cntlPnts[i], vParam,
                    pp[i], tt[i], dd[i], null);
        }

        /*
        * map for U-direction
        */
        ldv = new double[4];
        lduv = new double[4];
        PureBezierCurveEvaluation.evaluation(tt, uParam, ldv, lduv, null, null);
        ldvv = PureBezierCurveEvaluation.coordinates(dd, uParam);
        ld0 = new double[4];
        ldu = new double[4];
        lduu = new double[4];
        PureBezierCurveEvaluation.evaluation(pp, uParam, ld0, ldu, lduu, null);

        if (!isPoly) {
            convRational2Deriv(ld0, ldu, ldv, lduu, lduv, ldvv);
        }

        d0 = new CartesianPoint3D(ld0);
        du = new LiteralVector3D(ldu);
        dv = new LiteralVector3D(ldv);
        duu = new LiteralVector3D(lduu);
        duv = new LiteralVector3D(lduv);
        dvv = new LiteralVector3D(ldvv);
        return new SurfaceDerivative3D(d0, du, dv, duu, duv, dvv);
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     */
    public PointOnSurface3D[] projectFrom(Point3D point) {
        return ProjPntBzs3D.projection(point, this);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see ParameterOutOfRange
     * @see #truncate(ParameterSection,ParameterSection)
     * @see #toMesh(ToleranceForDistance)
     */
    public Mesh3D
    toMesh(ParameterSection uPint, ParameterSection vPint,
           ToleranceForDistance tol) {
        PureBezierSurface3D t_bzs;
        Mesh3D Mesh;
        Point3D[][] mesh;
        int u_npnts, v_npnts;
        double uSp, uIp, vSp, vIp;
        double uParam, vParam;
        int i, j;

        t_bzs = truncate(uPint, vPint);
        Mesh = t_bzs.toMesh(tol);

        uSp = uPint.start();
        uIp = uPint.increase();
        vSp = vPint.start();
        vIp = vPint.increase();

        u_npnts = Mesh.uNPoints();
        v_npnts = Mesh.vNPoints();
        mesh = Mesh.points();

        for (i = 0; i < u_npnts; i++) {
            for (j = 0; j < v_npnts; j++) {
                uParam = uSp + uIp * ((PointOnSurface3D) mesh[i][j]).uParameter();
                vParam = vSp + vIp * ((PointOnSurface3D) mesh[i][j]).vParameter();
                try {
                    mesh[i][j] = new PointOnSurface3D(this, uParam, vParam, doCheckDebug);
                } catch (InvalidArgumentValueException e) {
                    throw new FatalException();
                }
            }
        }

        return new Mesh3D(mesh, false);
    }

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param tol �����̋��e��?�
     * @return ���̗L�ȖʑS�̂𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     */
    public Mesh3D toMesh(ToleranceForDistance tol) {
        FreeformSurfaceWithControlPoints3D.SegInfo seg_info; /* a SegInfo */

        FreeformSurfaceWithControlPoints3D.GpList u_gp_list; /* list of MeshParam for U dir. */
        FreeformSurfaceWithControlPoints3D.GpList v_gp_list; /* list of MeshParam for V dir. */

        double[] kp = new double[2];        /* parameter interval */

        /*
        * divide Bezier into planes and determine mesh.
        */
        u_gp_list = new FreeformSurfaceWithControlPoints3D.GpList();
        v_gp_list = new FreeformSurfaceWithControlPoints3D.GpList();

        seg_info = new FreeformSurfaceWithControlPoints3D.SegInfo
                (new MeshParam(0, 0, 1), new MeshParam(0, 1, 1),
                        new MeshParam(0, 0, 1), new MeshParam(0, 1, 1));

        getSrfMesh(seg_info, tol, u_gp_list, v_gp_list);

        /*
        * make parameters and mesh points
        */
        kp[0] = 0.0;
        kp[1] = 1.0;
        return makeParamAndMesh(u_gp_list, v_gp_list, kp, kp);
    }

    /**
     * ���̋Ȗʂ��^����ꂽ?��x�ɂ����ĕ��ʂƌ��Ȃ��Ȃ�?�?���?A
     * U/V ���Ƀp���??[�^���_�œ񕪊�����?B
     * <p/>
     * ���ʂƂ��ē�����z�� S �̗v�f��?��� 4 �ł���?B
     * �e�v�f��?A���̋Ȗʂ𕪊������Ȗʂ̂��ꂼ���\��?B
     * <p/>
     * �^����ꂽ tol �ɂ�����?A�Ȗʂ𕪊�����K�v���Ȃ�?�?��ɂ�
     * S[i] (i = 0, ..., 3) �ɂ͂��ׂ� null �����?B
     * </p>
     * <p/>
     * �Ȗʂ� U/V ���Ƃ�ɓ񕪊�����?�?��ɂ�?A
     * S �̊e�v�f�͈ȉ��̋Ȗʂ�\��?B
     * <pre>
     * 		S[0] : U ���?AV ���ɂ�����Ȗ�
     * 		S[1] : U ���?㑤?AV ���ɂ�����Ȗ�
     * 		S[2] : U ���?AV ���?㑤�ɂ�����Ȗ�
     * 		S[3] : U ���?㑤?AV ���?㑤�ɂ�����Ȗ�
     * </pre>
     * </p>
     * <p/>
     * �Ȗʂ� U ���ɂ̂ݓ񕪊����� (V ���ɂ͕�������K�v���Ȃ�) ?�?��ɂ�?A
     * S �̊e�v�f�͈ȉ��̋Ȗʂ�\��?B
     * <pre>
     * 		S[0] : U ���ɂ�����Ȗ�
     * 		S[1] : U ���?㑤�ɂ�����Ȗ�
     * 		S[2] : null
     * 		S[3] : null
     * </pre>
     * </p>
     * <p/>
     * �Ȗʂ� V ���ɂ̂ݓ񕪊����� (U ���ɂ͕�������K�v���Ȃ�) ?�?��ɂ�?A
     * S �̊e�v�f�͈ȉ��̋Ȗʂ�\��?B
     * <pre>
     * 		S[0] : V ���ɂ�����Ȗ�
     * 		S[1] : null
     * 		S[2] : V ���?㑤�ɂ�����Ȗ�
     * 		S[3] : null
     * </pre>
     * </p>
     *
     * @param tol ���ʂƂ݂Ȃ������̋��e��?�
     * @return �������ꂽ�Ȗʂ̔z��
     */
    FreeformSurfaceWithControlPoints3D[] divideForMesh(ToleranceForDistance tol) {
        boolean u_coln;
        boolean v_coln;

        PureBezierSurface3D[] bzss;
        PureBezierSurface3D vb_bzs;
        PureBezierSurface3D vu_bzs;
        PureBezierSurface3D lb_bzs;
        PureBezierSurface3D rb_bzs;
        PureBezierSurface3D lu_bzs;
        PureBezierSurface3D ru_bzs;

        double told = tol.value();

        u_coln = uIsColinear(controlPoints, told);
        v_coln = vIsColinear(controlPoints, told);

        try {
            if (u_coln && v_coln) {
                lb_bzs = null;
                rb_bzs = null;
                lu_bzs = null;
                ru_bzs = null;

            } else if ((!u_coln) && (!v_coln)) {
                bzss = vDivide(0.5);
                vb_bzs = bzss[0];
                vu_bzs = bzss[1];

                bzss = vb_bzs.uDivide(0.5);
                lb_bzs = bzss[0];
                rb_bzs = bzss[1];

                bzss = vu_bzs.uDivide(0.5);
                lu_bzs = bzss[0];
                ru_bzs = bzss[1];

            } else if (u_coln) {
                bzss = vDivide(0.5);
                lb_bzs = bzss[0];
                lu_bzs = bzss[1];

                rb_bzs = null;
                ru_bzs = null;

            } else {    // if (v_coln)
                bzss = uDivide(0.5);
                lb_bzs = bzss[0];
                rb_bzs = bzss[1];

                lu_bzs = null;
                ru_bzs = null;
            }
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }

        bzss = new PureBezierSurface3D[4];

        bzss[0] = lb_bzs;
        bzss[1] = rb_bzs;
        bzss[2] = lu_bzs;
        bzss[3] = ru_bzs;

        return bzss;
    }

    /**
     * ���̋Ȗʂ����ʌ`?�Ƃ݂Ȃ��邩�ǂ�����Ԃ�?B
     *
     * @param tol ���ʂƂ݂Ȃ������̋��e��?�
     * @return ���ʂƂ݂Ȃ���Ȃ�� true?A�����łȂ���� false
     * @see #makeRefPln()
     */
    boolean isPlaner(ToleranceForDistance tol) {
        int u_uicp = uNControlPoints();
        int v_uicp = vNControlPoints();
        Plane3D pln;

        Vector3D evec;
        double tolerance = tol.value();
        int i, j;

        if ((pln = makeRefPln()) == null) {
            return true;
        } else {
            for (i = 0; i < u_uicp; i++) {
                for (j = 0; j < v_uicp; j++) {
                    evec = controlPointAt(i, j).subtract(pln.position().location());
                    if (Math.abs(evec.dotProduct(pln.position().z())) > tolerance)
                        return false;
                }
            }
        }

        return true;
    }

    /**
     * ���̋Ȗʂ����ʌ`?�Ƃ݂Ȃ��邩�ǂ����𔻒肷��?ۂ�
     * ��?��ƂȂ镽�ʂ�?�?�����?B
     *
     * @return ���̋Ȗʂ����ʌ`?�Ƃ݂Ȃ��邩�ǂ����𔻒肷��?ۂ̊�?��ƂȂ镽��
     * @see #isPlaner(ToleranceForDistance)
     */
    private Plane3D makeRefPln() {
        double tol = ConditionOfOperation.getCondition().getToleranceForDistance();
        double tol2 = tol * tol;
        double atol = ConditionOfOperation.getCondition().getToleranceForAngle();

        Point3D org;
        Vector3D normal;

        getDirInfo uInfo;
        getDirInfo vInfo;

        int u_uicp = uNControlPoints();
        int v_uicp = vNControlPoints();

        boolean found;
        double[] cos_val = new double[4];
        int i;

        cos_val[0] = Math.cos(Math.PI * (1.0 / 3.0));    /* 60 degree */
        cos_val[1] = Math.cos(Math.PI * (1.0 / 4.0));    /* 45 degree */
        cos_val[2] = Math.cos(Math.PI * (1.0 / 6.0));    /* 30 degree */
        cos_val[3] = Math.cos(atol);            /* tolerance */

        org = controlPointAt(0, 0);

        /*
        * first search
        */
        found = false;
        uInfo = new getDirInfo();
        vInfo = new getDirInfo();
        first_loop:
        for (i = 0; i < 3; i++) {
            uInfo.firstCall = true;
            while (getUDir(org, uInfo, u_uicp, v_uicp, tol2)) {
                vInfo.firstCall = true;
                while (getVDir(org, vInfo, u_uicp, v_uicp, tol2)) {
                    if (Math.abs(uInfo.dir.dotProduct(vInfo.dir)) < cos_val[i]) {
                        found = true;
                        break first_loop;
                    }
                }
            }
        }

        /*
        * second search
        */
        if (!found) {
            double leng;
            Vector3D evec;
            boolean isU;
            int j, k;

            second_loop:
            for (i = 0; i < 4; i++) {
                isU = true;
                for (j = 0; j < u_uicp; j++) {
                    for (k = 0; k < v_uicp; k++) {
                        if (j == 0 && k == 0) continue;

                        evec = controlPointAt(j, k).subtract(org);
                        if ((leng = evec.norm()) < tol2)
                            continue;

                        leng = Math.sqrt(leng);
                        evec = evec.divide(leng);

                        if (isU) {
                            isU = false;
                            uInfo.dir = evec;
                            continue;
                        }
                        vInfo.dir = evec;

                        if (Math.abs(uInfo.dir.dotProduct(vInfo.dir)) < cos_val[i]) {
                            found = true;
                            break second_loop;
                        }
                    }
                }
            }
        }

        if (!found) {
            return null;
        }

        normal = uInfo.dir.crossProduct(vInfo.dir);
        try {
            return new Plane3D(org, normal);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
    }

    /**
     * ���̋Ȗʂ� U ����?���_���?L�т���𓾂�?B
     *
     * @param org    ����?�߂�?ۂ̊�?��ƂȂ�_
     * @param info   �Ă�?o�����Ƃ��Ƃ肷��?��
     * @param u_uicp U ����?���_��?�
     * @param v_uicp V ����?���_��?�
     * @param tol2   �����̋��e��?��̎�?�l
     * @return ������� true?A�����łȂ���� false
     * @see #makeRefPln()
     * @see #getVDir(Point3D,PureBezierSurface3D.getDirInfo,int,int,double)
     */
    boolean getUDir(Point3D org, getDirInfo info, int u_uicp, int v_uicp, double tol2) {
        Point3D cpnt;
        double leng;

        if (info.firstCall) {
            info.firstCall = false;
            info.v = 0;
            info.u = u_uicp - 1;
        } else {
            info.u--;
        }

        for (; info.v < v_uicp; info.v++) {
            for (; info.u > info.v; info.u--) {
                cpnt = controlPointAt(info.u, info.v);
                info.dir = cpnt.subtract(org);
                if ((leng = info.dir.norm()) > tol2) {
                    leng = Math.sqrt(leng);
                    info.dir = info.dir.divide(leng);
                    return true;
                }
            }

            info.u = u_uicp - 1;
        }

        return false;
    }

    /**
     * ���̋Ȗʂ� V ����?���_���?L�т���𓾂�?B
     *
     * @param org    ����?�߂�?ۂ̊�?��ƂȂ�_
     * @param info   �Ă�?o�����Ƃ��Ƃ肷��?��
     * @param u_uicp U ����?���_��?�
     * @param v_uicp V ����?���_��?�
     * @param tol2   �����̋��e��?��̎�?�l
     * @return ������� true?A�����łȂ���� false
     * @see #makeRefPln()
     * @see #getUDir(Point3D,PureBezierSurface3D.getDirInfo,int,int,double)
     */
    boolean getVDir(Point3D org, getDirInfo info, int u_uicp, int v_uicp, double tol2) {
        Point3D cpnt;
        double leng;

        if (info.firstCall) {
            info.firstCall = false;
            info.u = 0;
            info.v = v_uicp - 1;
        } else {
            info.v--;
        }

        for (; info.u < u_uicp; info.u++) {
            for (; info.v > info.u; info.v--) {
                cpnt = controlPointAt(info.u, info.v);
                info.dir = cpnt.subtract(org);
                if ((leng = info.dir.norm()) > tol2) {
                    leng = Math.sqrt(leng);
                    info.dir = info.dir.divide(leng);
                    return true;
                }
            }

            info.v = v_uicp - 1;
        }

        return false;
    }

    /**
     * {@link #getUDir(Point3D,PureBezierSurface3D.getDirInfo,int,int,double)
     * getUDir(Point3D, PureBezierSurface3D.getDirInfo, int, int, double)}
     * �����
     * {@link #getVDir(Point3D,PureBezierSurface3D.getDirInfo,int,int,double)
     * getVDir(Point3D, PureBezierSurface3D.getDirInfo, int, int, double)}
     * �ɂ�����?A�Ă�?o�����Ƃ��Ƃ肷��?���i�[���邽�߂̓Ք�N���X?B
     *
     * @see #makeRefPln()
     */
    private class getDirInfo {
        /**
         * ?�?��̌Ă�?o���ł��邩�ۂ����t���O?B
         */
        private boolean firstCall;

        /**
         * ?��?���ׂ�?���_�� U ���̃C���f�b�N�X?B
         */
        private int u;

        /**
         * ?��?���ׂ�?���_�� V ���̃C���f�b�N�X?B
         */
        private int v;

        /**
         * ?���_���?L�т���?B
         */
        private Vector3D dir;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         */
        private getDirInfo() {
            super();
        }
    }

    /**
     * ���̋Ȗʂ̎w��̋��E��\���x�W�G��?��Ԃ�?B
     *
     * @param nth ���E��?��� (u,v):(0,0)-(1,0)-(1,1)-(0,1)-(0,0)
     * @return ���E��?�
     * @see IntsBzsBzs3D.BezierInfo
     * @see IntsQrdBzs3D.BezierInfo
     */
    PureBezierCurve3D getBoundaryCurve(int nth) {

        int u_uicp = uNControlPoints();
        int v_uicp = vNControlPoints();
        int uicp = ((nth % 2) == 0) ? u_uicp : v_uicp;
        Point3D[] pnts = new Point3D[uicp];
        double[] ws;
        int i;

        if (isRational())
            ws = new double[uicp];
        else
            ws = null;
        switch (nth) {
            case 0:
                for (i = 0; i < u_uicp; i++) {
                    pnts[i] = controlPointAt(i, 0);
                    if (isRational())
                        ws[i] = weightAt(i, 0);
                }
                break;
            case 1:
                for (i = 0; i < v_uicp; i++) {
                    pnts[i] = controlPointAt(u_uicp - 1, i);
                    if (isRational())
                        ws[i] = weightAt(u_uicp - 1, i);
                }
                break;
            case 2:
                for (i = 0; i < u_uicp; i++) {
                    pnts[i] = controlPointAt(u_uicp - 1 - i, v_uicp - 1);
                    if (isRational())
                        ws[i] = weightAt(u_uicp - 1 - i, v_uicp - 1);
                }
                break;
            case 3:
                for (i = 0; i < v_uicp; i++) {
                    pnts[i] = controlPointAt(0, v_uicp - 1 - i);
                    if (isRational())
                        ws[i] = weightAt(0, v_uicp - 1 - i);
                }
                break;
        }

        PureBezierCurve3D result;
        if (isRational()) {
            result = new PureBezierCurve3D(pnts, ws);
            //return new PureBezierCurve3D(pnts, ws);
        } else {
            result = new PureBezierCurve3D(pnts);
            //return new PureBezierCurve3D(pnts);
        }

        return result;
    }

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ�?B
     *
     * @return ���̗L�ȖʑS�̂�?Č�����L�? Bspline �Ȗ�
     */
    public BsplineSurface3D toBsplineSurface() {
        double[][] www =
                (this.isRational()) ? this.weights : this.makeUniformWeights();

        return new BsplineSurface3D(BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
                BsplineKnot.quasiUniformKnotsOfLinearOneSegment,
                this.controlPoints, www);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @return ���̋Ȗʂ̎w��̋�Ԃ�?Č�����L�? Bspline �Ȗ�
     * @see ParameterOutOfRange
     * @see #truncate(ParameterSection,ParameterSection)
     * @see #toBsplineSurface()
     */
    public BsplineSurface3D
    toBsplineSurface(ParameterSection uPint,
                     ParameterSection vPint) {
        return this.truncate(uPint, vPint).toBsplineSurface();
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
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsCncBzs3D#intersection(Line3D,PureBezierSurface3D,boolean)
     * IntsCncBzs3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Line3D mate, boolean doExchange) {
        return IntsCncBzs3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�~??��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsCncBzs3D#intersection(Conic3D,PureBezierSurface3D,boolean)
     * IntsCncBzs3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~??��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(Conic3D mate, boolean doExchange) {
        return IntsCncBzs3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsBzcBzs3D#intersection(PureBezierCurve3D,PureBezierSurface3D,boolean)
     * IntsBzcBzs3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate, boolean doExchange) {
        return IntsBzcBzs3D.intersection(mate, this, !doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ��?ۂ̉��Z��
     * {@link IntsBscBzs3D#intersection(BsplineCurve3D,PureBezierSurface3D,boolean)
     * IntsBscBzs3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate, boolean doExchange) {
        return IntsBscBzs3D.intersection(mate, this, !doExchange);
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
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    public SurfaceSurfaceInterference3D[] intersect(ParametricSurface3D mate) {
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
     * ��?ۂ̉��Z��
     * {@link IntsQrdBzs3D#intersection(ElementarySurface3D,PureBezierSurface3D,boolean)
     * IntsQrdBzs3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate, boolean doExchange) {
        return IntsQrdBzs3D.intersection(mate, this, !doExchange);
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
     * {@link IntsQrdBzs3D#intersection(ElementarySurface3D,PureBezierSurface3D,boolean)
     * IntsQrdBzs3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate,
                                             boolean doExchange) {
        return IntsQrdBzs3D.intersection(mate, this, !doExchange);
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
     * {@link IntsQrdBzs3D#intersection(ElementarySurface3D,PureBezierSurface3D,boolean)
     * IntsQrdBzs3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange) {
        return IntsQrdBzs3D.intersection(mate, this, !doExchange);
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
     * {@link IntsQrdBzs3D#intersection(ElementarySurface3D,PureBezierSurface3D,boolean)
     * IntsQrdBzs3D.intersection}(mate, this, !doExchange)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate,
                                             boolean doExchange) {
        return IntsQrdBzs3D.intersection(mate, this, !doExchange);
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
     * {@link IntsBzsBzs3D#intersection(PureBezierSurface3D,PureBezierSurface3D)
     * IntsBzsBzs3D.intersection}(this, mate)
     * ���邢��
     * IntsBzsBzs3D.intersection(mate, this)
     * ��?s�ȂBĂ���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(PureBezierSurface3D mate,
                                             boolean doExchange) {
        if (doExchange) {
            return IntsBzsBzs3D.intersection(mate, this);
        }
        return IntsBzsBzs3D.intersection(this, mate);
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
     * {@link IntsSrfBss3D#intersection(PureBezierSurface3D,BsplineSurface3D,boolean)
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

    /*
    * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param uParam	U ���̃p���??[�^�l
    * @return	�w��� U �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D uIsoParametricCurve(double uParam) {
        uParam = checkUParameter(uParam);
        boolean isPoly = isPolynomial();
        double[][][] cntlPnts = toDoubleArray(isPoly);
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        double[][] tBzc = new double[uUicp][];
        double[][] bzc = new double[vUicp][];

        for (int i = 0; i < vUicp; i++) {
            for (int j = 0; j < uUicp; j++)
                tBzc[j] = cntlPnts[j][i];
            bzc[i] = PureBezierCurveEvaluation.coordinates(tBzc, uParam);
        }
        return new PureBezierCurve3D(bzc);
    }

    /*
    * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param vParam	V ���̃p���??[�^�l
    * @return	�w��� V �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D vIsoParametricCurve(double vParam) {
        vParam = checkVParameter(vParam);
        boolean isPoly = isPolynomial();
        double[][][] cntlPnts = toDoubleArray(isPoly);
        int uUicp = uNControlPoints();
        double[][] bzc = new double[uUicp][];

        for (int i = 0; i < uUicp; i++) {
            bzc[i] = PureBezierCurveEvaluation.coordinates(cntlPnts[i], vParam);
        }
        return new PureBezierCurve3D(bzc);
    }

    /**
     * ����?��?�̒��ԃf?[�^�̗̈��l������?B
     *
     * @return ����?��?�̒��ԃf?[�^�̗̈�
     * @see #uDivide(double)
     * @see #vDivide(double)
     */
    double[][][][] allocateIntermediateDoubleArrayForDividing() {
        boolean isPoly = isPolynomial();
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        double[][][][] bzss_array = new double[2][][][];
        for (int i = 0; i < 2; i++)
            bzss_array[i] = allocateDoubleArray(isPoly, uUicp, vUicp);
        return bzss_array;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ U ���̃p���??[�^�l�œ񕪊�����?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f�ɂ� U ���̋Ȗ�?A
     * ��Ԗڂ̗v�f�ɂ� U ���?㑤�̋Ȗ�
     * �����?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @return �񕪊����ꂽ�x�W�G�Ȗʂ̔z��
     * @see ParameterOutOfRange
     * @see #vDivide(double)
     */
    public PureBezierSurface3D[] uDivide(double uParam) {
        return uDivide(uParam, allocateIntermediateDoubleArrayForDividing());
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ U ���̃p���??[�^�l�œ񕪊�����?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f�ɂ� U ���̋Ȗ�?A
     * ��Ԗڂ̗v�f�ɂ� U ���?㑤�̋Ȗ�
     * �����?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam    U ���̃p���??[�^�l
     * @param bzssArray ����?��?�̒��ԃf?[�^�̗̈�
     * @return �񕪊����ꂽ�x�W�G�Ȗʂ̔z��
     * @see ParameterOutOfRange
     * @see #uDivide(double)
     * @see #allocateIntermediateDoubleArrayForDividing()
     */
    PureBezierSurface3D[] uDivide(double uParam,
                                  double[][][][] bzssArray) {
        double[][][] cntlPnts;
        double[][] bzc;
        double[][][] bzcsArray;
        PureBezierSurface3D[] bzss;
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        boolean isPoly = isPolynomial();
        int i, j, k;

        uParam = checkUParameter(uParam);
        cntlPnts = toDoubleArray(isPoly);
        bzc = new double[uUicp][];
        bzcsArray = new double[2][uUicp][];

        for (i = 0; i < vUicp; i++) {
            for (j = 0; j < uUicp; j++) {
                bzc[j] = cntlPnts[j][i];
                bzcsArray[0][j] = bzssArray[0][j][i];
                bzcsArray[1][j] = bzssArray[1][j][i];
            }
            try {
                PureBezierCurveEvaluation.divide(bzc, uParam, bzcsArray);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        bzss = new PureBezierSurface3D[2];
        for (i = 0; i < 2; i++) {
            try {
                bzss[i] = new PureBezierSurface3D(bzssArray[i], false);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        return bzss;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ V ���̃p���??[�^�l�œ񕪊�����?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f�ɂ� V ���̋Ȗ�?A
     * ��Ԗڂ̗v�f�ɂ� V ���?㑤�̋Ȗ�
     * �����?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param vParam V ���̃p���??[�^�l
     * @return �񕪊����ꂽ�x�W�G�Ȗʂ̔z��
     * @see ParameterOutOfRange
     * @see #uDivide(double)
     */
    public PureBezierSurface3D[] vDivide(double vParam) {
        return vDivide(vParam, allocateIntermediateDoubleArrayForDividing());
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ V ���̃p���??[�^�l�œ񕪊�����?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f�ɂ� V ���̋Ȗ�?A
     * ��Ԗڂ̗v�f�ɂ� V ���?㑤�̋Ȗ�
     * �����?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param vParam    V ���̃p���??[�^�l
     * @param bzssArray ����?��?�̒��ԃf?[�^�̗̈�
     * @return �񕪊����ꂽ�x�W�G�Ȗʂ̔z��
     * @see ParameterOutOfRange
     * @see #vDivide(double)
     * @see #allocateIntermediateDoubleArrayForDividing()
     */
    PureBezierSurface3D[] vDivide(double vParam,
                                  double[][][][] bzssArray) {
        double[][][] cntlPnts;
        double[][][] bzcsArray;
        PureBezierSurface3D[] bzss;
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        boolean isPoly = isPolynomial();
        int i, j;

        vParam = checkVParameter(vParam);
        cntlPnts = toDoubleArray(isPoly);
        bzcsArray = new double[2][][];

        for (i = 0; i < uUicp; i++) {
            bzcsArray[0] = bzssArray[0][i];
            bzcsArray[1] = bzssArray[1][i];
            try {
                PureBezierCurveEvaluation.divide(cntlPnts[i], vParam, bzcsArray);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        bzss = new PureBezierSurface3D[2];
        for (i = 0; i < 2; i++) {
            try {
                bzss[i] = new PureBezierSurface3D(bzssArray[i], false);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }
        }

        return bzss;
    }

    /**
     * ���̃x�W�G�Ȗʂ�?A�^����ꂽ��`��Ԃ�?ؒf����?B
     * <p/>
     * uSection �̑?���l������?�?��ɂ�?AU ����?i?s���]�����x�W�G�Ȗʂ�Ԃ�?B
     * ���l��?A
     * vSection �̑?���l������?�?��ɂ�?AV ����?i?s���]�����x�W�G�Ȗʂ�Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uSection ?ؒf���Ďc��������\�� U ���̃p���??[�^���
     * @param vSection ?ؒf���Ďc��������\�� V ���̃p���??[�^���
     * @return ?ؒf���Ďc����������\���x�W�G�Ȗ�
     * @see ParameterOutOfRange
     */
    public PureBezierSurface3D truncate(ParameterSection uSection,
                                        ParameterSection vSection) {
        PureBezierSurface3D t_bzs;

        t_bzs = truncate(uSection, true);
        t_bzs = t_bzs.truncate(vSection, false);
        return t_bzs;
    }

    /**
     * ���̃x�W�G�Ȗʂ�?A�^����ꂽ��Ԃ�?ؒf����?B
     * <p/>
     * section �̑?���l������?�?��ɂ�?A?i?s���]�����x�W�G�Ȗʂ�Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param section ?ؒf���Ďc��������\���p���??[�^���
     * @param isU     U ���ۂ�
     * @return ?ؒf���Ďc����������\���x�W�G�Ȗ�
     * @see ParameterOutOfRange
     * @see #truncate(ParameterSection,ParameterSection)
     * @see #reverse(boolean,boolean)
     */
    private PureBezierSurface3D truncate(ParameterSection section,
                                         boolean isU) {
        double start_par, end_par;
        PureBezierSurface3D t_bzs;

        if (isU) {
            start_par = checkUParameter(section.lower());
            end_par = checkUParameter(section.upper());
            t_bzs = uDivide(start_par)[1];
        } else {
            start_par = checkVParameter(section.lower());
            end_par = checkVParameter(section.upper());
            t_bzs = vDivide(start_par)[1];
        }

        end_par = (end_par - start_par) / (1.0 - start_par);

        if (isU)
            t_bzs = t_bzs.uDivide(end_par)[0];
        else
            t_bzs = t_bzs.vDivide(end_par)[0];

        if (section.increase() < 0.0)
            if (isU)
                t_bzs = t_bzs.reverse(true, false);
            else
                t_bzs = t_bzs.reverse(false, true);

        return t_bzs;
    }

    /**
     * ���̃x�W�G�Ȗʂ�?A�w��̕��ɔ��]�������x�W�G�Ȗʂ�Ԃ�?B
     *
     * @param isU U ���ɔ��]������ǂ���
     * @param isV V ���ɔ��]������ǂ���
     * @return ���]�����x�W�G�Ȗ�
     */
    private PureBezierSurface3D reverse(boolean isU, boolean isV) {
        boolean isRat = isRational();
        int uUicp = uNControlPoints();
        int vUicp = vNControlPoints();
        Point3D[][] rCp = new Point3D[uUicp][vUicp];
        double[][] rWt = null;
        int i, j, k, l;

        if ((!isU) && (!isV)) {
            return this;
        }

        if (isRat)
            rWt = new double[uUicp][vUicp];

        if (isU)
            j = uUicp - 1;
        else
            j = 0;
        for (i = 0; i < uUicp; i++) {
            if (isV)
                l = vUicp - 1;
            else
                l = 0;
            for (k = 0; k < vUicp; k++) {
                rCp[i][k] = controlPointAt(j, l);
                if (isRat)
                    rWt[i][k] = weightAt(j, l);
                if (isV)
                    l--;
                else
                    l++;
            }
            if (isU)
                j--;
            else
                j++;
        }
        try {
            PureBezierSurface3D result;

            if (isRat) {
                result = new PureBezierSurface3D(rCp, rWt);
                //return new PureBezierSurface3D(rCp, rWt);
            } else {
                result = new PureBezierSurface3D(rCp);
                //return new PureBezierSurface3D(rCp);
            }

            return result;

        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     *
     * @return U ���̃p���??[�^��`��
     */
    ParameterDomain getUParameterDomain() {
        try {
            return new ParameterDomain(false, 0.0, 1.0);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
     *
     * @return V ���̃p���??[�^��`��
     */
    ParameterDomain getVParameterDomain() {
        try {
            return new ParameterDomain(false, 0.0, 1.0);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /*
    * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ� U ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
    * <p>
    * �^����ꂽ�p���??[�^�l�����̋Ȗʂ� U ���̒�`���O��Ă���?�?��ɂ�
    * ParameterOutOfRange �̗�O��?�����?B
    * </p>
    *
    * @param param	U ���̃p���??[�^�l
    * @return	�K�v�ɉ����Ă��̋Ȗʂ� U ���̒�`���Ɋۂ߂�ꂽ�p���??[�^�l
    * @see	AbstractParametricSurface#checkUValidity(double)
    * @see	ParameterDomain#force(double)
    * @see	ParameterOutOfRange
    */
    private double checkUParameter(double param) {
        checkUValidity(param);
        return uParameterDomain().force(param);
    }

    /*
    * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ� V ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
    * <p>
    * �^����ꂽ�p���??[�^�l�����̋Ȗʂ� V ���̒�`���O��Ă���?�?��ɂ�
    * ParameterOutOfRange �̗�O��?�����?B
    * </p>
    *
    * @param param	V ���̃p���??[�^�l
    * @return	�K�v�ɉ����Ă��̋Ȗʂ� V ���̒�`���Ɋۂ߂�ꂽ�p���??[�^�l
    * @see	AbstractParametricSurface#checkVValidity(double)
    * @see	ParameterDomain#force(double)
    * @see	ParameterOutOfRange
    */
    private double checkVParameter(double param) {
        checkVValidity(param);
        return vParameterDomain().force(param);
    }

    /**
     * ���̋Ȗʂ�?A�`?�⻂̂܂܂ɂ���?AU ���̎�?�����?グ���Ȗʂ�Ԃ�?B
     *
     * @return ����`?��?AU ���̎�?������?オ�B��Ȗ�
     */
    public PureBezierSurface3D uElevateOneDegree() {
        boolean isPoly = isPolynomial();
        int uNCP = this.uNControlPoints();
        int vNCP = this.vNControlPoints();

        double[][][] newCP =
                FreeformSurfaceWithControlPoints3D.
                        allocateDoubleArray(isPoly, (uNCP + 1), vNCP);

        this.setCoordinatesToDoubleArray(isPoly, uNCP, vNCP, newCP);

        double[][] curve = new double[uNCP + 1][];

        for (int vi = 0; vi < vNCP; vi++) {
            for (int ui = 0; ui < (uNCP + 1); ui++)
                curve[ui] = newCP[ui][vi];
            PureBezierCurveEvaluation.elevateOneDegree(uNCP, curve);
        }

        return new PureBezierSurface3D(newCP);
    }

    /**
     * ���̋Ȗʂ�?A�`?�⻂̂܂܂ɂ���?AV ���̎�?�����?グ���Ȗʂ�Ԃ�?B
     *
     * @return ����`?��?AV ���̎�?������?オ�B��Ȗ�
     */
    public PureBezierSurface3D vElevateOneDegree() {
        boolean isPoly = isPolynomial();
        int uNCP = this.uNControlPoints();
        int vNCP = this.vNControlPoints();

        double[][][] newCP =
                FreeformSurfaceWithControlPoints3D.
                        allocateDoubleArray(isPoly, uNCP, (vNCP + 1));

        this.setCoordinatesToDoubleArray(isPoly, uNCP, vNCP, newCP);

        for (int ui = 0; ui < uNCP; ui++)
            PureBezierCurveEvaluation.elevateOneDegree(vNCP, newCP[ui]);

        return new PureBezierSurface3D(newCP);
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#PURE_BEZIER_SURFACE_3D ParametricSurface3D.PURE_BEZIER_SURFACE_3D}
     */
    int type() {
        return PURE_BEZIER_SURFACE_3D;
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

        // ��芸����?A���̎�
        Mesh3D mesh = this.toMesh(uParameterSection,
                vParameterSection,
                new ToleranceForDistance(tolerance));

        for (int u = 0; u < mesh.uNPoints(); u++)
            for (int v = 0; v < mesh.vNPoints(); v++)
                result.addElement(mesh.pointAt(u, v));

        scalingFactor[0] = getMaxLengthOfUControlPolygons(false);
        scalingFactor[1] = getMaxLengthOfVControlPolygons(false);

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
        Point3D[][] tControlPoints = new Point3D[this.uNControlPoints()][];
        for (int i = 0; i < this.uNControlPoints(); i++)
            tControlPoints[i] = Point3D.transform(this.controlPoints[i],
                    reverseTransform,
                    transformationOperator,
                    transformedGeometries);
        if (this.isPolynomial() == true)
            return new PureBezierSurface3D(tControlPoints);
        else
            return new PureBezierSurface3D(tControlPoints, this.weights);
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
        writer.println(indent_tab + "\tuNControlPoints\t" + uNControlPoints());
        writer.println(indent_tab + "\tvNControlPoints\t" + vNControlPoints());
        writer.println(indent_tab + "\tcontrolPoints");
        for (int i = 0; i < controlPoints.length; i++) {
            for (int j = 0; j < controlPoints[i].length; j++) {
                controlPoints[i][j].output(writer, indent + 2);
            }
        }

        // output weights
        if (weights() != null) {
            writer.println(indent_tab + "\tweights ");
            for (int j = 0; j < weights().length; j++) {
                writer.print(indent_tab + "\t\t");
                for (int k = 0; k < weights()[j].length; k++) {
                    writer.print(" " + weightAt(j, k));
                }
                writer.println();
            }
        }

        writer.println(indent_tab + "End");
    }
}
