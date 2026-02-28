/*
 * 2D 2��?�Ԃ̃t�B���b�g��?�߂�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FiltCrvCrv2D.java,v 1.3 2007-10-21 21:08:11 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.ArrayMathUtils;
import org.episteme.mathematics.analysis.PrimitiveMappingND;

/**
 * 2D 2��?�Ԃ̃t�B���b�g��?�߂�N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:11 $
 */
final class FiltCrvCrv2D {
    static boolean debug = false;

    /**
     * �?�߂�ꂽ�t�B���b�g�̃��X�g
     *
     * @see FilletObjectList
     */
    private FilletObjectList fillets;

    /**
     * ���z�I�t�Z�b�g��?� A ��?��
     * <p/>
     * sideA��WhichSide.BOTH�ł����?��E���ꂼ��̕���2��?A
     * ����ȊO��?�?��͎w�肳�ꂽ����1��?�?������?B
     * </p>
     *
     * @see CurveInfo
     * @see WhichSide
     */
    private CurveInfo[] infoA;

    /**
     * ���z�I�t�Z�b�g��?� B ��?��
     * <p/>
     * sideA��WhichSide.BOTH�ł����?��E���ꂼ��̕���2��?A
     * ����ȊO��?�?��͎w�肳�ꂽ����1��?�?������?B
     * </p>
     *
     * @see CurveInfo
     * @see WhichSide
     */
    private CurveInfo[] infoB;

    /**
     * �t�B���b�g���a
     */
    private double radius;

    /**
     * �I�u�W�F�N�g��?\�z����
     *
     * @param curveA ��?� A
     * @param sectA  ��?� A �̑�?ۂƂȂ�p���??[�^���
     * @param sideA  ��?� A �̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
     * @param curveB ��?� B
     * @param sectB  ��?� B �̑�?ۂƂȂ�p���??[�^���
     * @param sideB  ��?� B �̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
     * @param raidus �t�B���b�g�̔��a
     * @see ParametricCurve2D
     * @see ParameterSection
     * @see WhichSide
     */
    private FiltCrvCrv2D(ParametricCurve2D curveA,
                         ParameterSection sectA,
                         int sideA,
                         ParametricCurve2D curveB,
                         ParameterSection sectB,
                         int sideB,
                         double radius) {
        super();

        curveA.checkValidity(sectA);
        curveB.checkValidity(sectB);

        double tol = curveA.getToleranceForDistance();
        if (radius < tol)
            throw new InvalidArgumentValueException();

        fillets = new FilletObjectList();
        this.radius = radius;
        infoA = getInfo(curveA, sectA, sideA);
        infoB = getInfo(curveB, sectB, sideB);
    }

    /**
     * �I�t�Z�b�g��?�(�ߎ���)��?�߂�
     * (�{����ParametricCurve2D���?�ׂ�)
     *
     * @param curve   �I�t�Z�b�g�����?�
     * @param section �I�t�Z�b�g������
     * @param side    �I�t�Z�b�g������
     * @param radius  �I�t�Z�b�g���鋗��
     */
    private ParametricCurve2D offsetCurve(ParametricCurve2D curve,
                                          ParameterSection section,
                                          int side,
                                          double radius) {
        switch (curve.type()) {
            case ParametricCurve2D.LINE_2D:
                /*
                * ��?��?�?��͕�?s�ړ�������?�(��g���~���O�������)�ƂȂ�
                */
                Line2D lin = (Line2D) curve;
                Vector2D enrm;
                if (side == WhichSide.RIGHT)
                    enrm = new LiteralVector2D(lin.dir().y(), -lin.dir().x());
                else
                    enrm = new LiteralVector2D(-lin.dir().y(), lin.dir().x());
                enrm = enrm.unitized();
                Point2D pnt = lin.pnt().add(enrm.multiply(radius));
                lin = new Line2D(pnt, lin.dir());

                return new TrimmedCurve2D(lin, section);
            case ParametricCurve2D.CIRCLE_2D:
                /*
                * �~��?�?��͔��a��t�B���b�g���a������?X�������(��g���~���O�������)�ƂȂ�
                */
                Circle2D cir = (Circle2D) curve;
                double cRadius;
                boolean rev = false;
                if (side == WhichSide.RIGHT)
                    cRadius = cir.radius() + radius;
                else {
                    cRadius = cir.radius() - radius;
                    if (cRadius < 0.0) {
                        cRadius = -cRadius;
                        rev = true;
                    }
                }
                if (cRadius < curve.getToleranceForDistance())    // reduced into a point
                    break;        // ???
                cir = new Circle2D(cir.position(), cRadius);
                if (rev) {
                    double newStart = section.start() + Math.PI;
                    if (newStart > GeometryUtils.PI2)
                        newStart -= GeometryUtils.PI2;
                    section = new ParameterSection(newStart, section.increase());
                }

                return new TrimmedCurve2D(cir, section);
        }
        /*
        * ����ȊO�̋�?��Bspline��?�ŋߎ�����
        */
        ToleranceForDistance ofst_tol = new ToleranceForDistance(radius / 100.0);
        return curve.offsetByBsplineCurve(section, radius, side, ofst_tol);
    }

    /**
     * ���z�I�t�Z�b�g��?�⠂�킷�N���X
     */
    private class CurveInfo {
        /**
         * ��?��ƂȂ��?�
         *
         * @see ParametricCurve2D
         */
        ParametricCurve2D curve;

        /**
         * ��?��ƂȂ��?�̃p���??[�^���
         *
         * @see ParameterSection
         */
        ParameterSection section;

        /**
         * ��?��ƂȂ��?�̂ǂ��瑤�ɃI�t�Z�b�g���邩
         *
         * @see WhichSide
         */
        int side;

        /**
         * ��?ۂɃI�t�Z�b�g���ꂽ��?�(�ߎ���)
         *
         * @see BsplineCurve2D
         */
        ParametricCurve2D ofstCrv;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param curve   ��?�
         * @param section ��?�̑�?ۂƂȂ�p���??[�^���
         * @param side    ��?�̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
         * @param raidus  �t�B���b�g�̔��a
         * @see ParametricCurve2D
         * @see ParameterSection
         * @see WhichSide
         */
        private CurveInfo(ParametricCurve2D curve,
                          ParameterSection section,
                          int side,
                          double radius) {
            super();

            this.curve = curve;
            this.section = section;
            this.side = side;
            ofstCrv = offsetCurve(curve, section, side, radius);
            if (debug) {
                ofstCrv.output(System.out);
            }
        }

        /**
         * ���z�I�t�Z�b�g��?�̗^����ꂽ�p���??[�^�ł�?W�l��?�߂�
         *
         * @param parameter �p���??[�^
         * @return ?W�l
         * @see Point2D
         */
        private Point2D evaluate(double parameter) {
            CurveDerivative2D deriv;
            Vector2D enrm;
            deriv = curve.evaluation(curve.parameterDomain().force(parameter));
            if (side == WhichSide.RIGHT)
                enrm = new LiteralVector2D(deriv.d1D().y(), -deriv.d1D().x());
            else
                enrm = new LiteralVector2D(-deriv.d1D().y(), deriv.d1D().x());
            enrm = enrm.unitized();

            return deriv.d0D().add(enrm.multiply(radius));
        }
    }

    /**
     * ���z�I�t�Z�b�g��?��?���?�߂�
     *
     * @param curve   ��?��ƂȂ��?�
     * @param section ��?��ƂȂ��?�̃p���??[�^���
     * @param side    ��?��ƂȂ��?�̂ǂ��瑤�ɃI�t�Z�b�g���邩
     * @see CurveInfo
     * @see ParametricCurve2D
     * @see ParameterSection
     * @see WhichSide
     */
    private CurveInfo[] getInfo(ParametricCurve2D curve,
                                ParameterSection section,
                                int side) {
        CurveInfo[] infoArray;
        int nInfo;
        int[] sides;

        switch (side) {
            case WhichSide.BOTH:
                /*
                * ����ɋ?�߂�?�?���?��E���ꂼ��̕��̉��z�I�t�Z�b�g��?��?���?�߂�
                */
                nInfo = 2;
                sides = new int[2];
                sides[0] = WhichSide.LEFT;
                sides[1] = WhichSide.RIGHT;
                break;
            case WhichSide.RIGHT:
            case WhichSide.LEFT:
                /*
                * �^����ꂽ���̉��z�I�t�Z�b�g��?��?���?�߂�
                */
                nInfo = 1;
                sides = new int[1];
                sides[0] = side;
                break;
            default:
                throw new InvalidArgumentValueException();
        }

        infoArray = new CurveInfo[nInfo];
        for (int i = 0; i < nInfo; i++) {
            infoArray[i] = new CurveInfo(curve, section, sides[i], radius);
        }
        return infoArray;
    }

    /**
     * �t�B���b�g��?���\���N���X
     */
    private class FilletInfo {
        /**
         * ���z�I�t�Z�b�g��?� A ��?��
         */
        CurveInfo cInfoA;

        /**
         * ���z�I�t�Z�b�g��?� B ��?��
         */
        CurveInfo cInfoB;

        /*
        * �ȉ��͎��Z�ɂ����Ĉꎞ�g�p
        */
        private nlFunc nl_func;
        private PrimitiveMappingND[] dnl_func;
        private cnvFunc cnv_func;

        private Point2D sPntA;
        private Point2D sPntB;
        private Vector2D sTngA;
        private Vector2D sTngB;
        private Vector2D sNrmA;
        private Vector2D sNrmB;

        /**
         * �I�u�W�F�N�g��?\�z����
         *
         * @param cInfoA ���z�I�t�Z�b�g��?� A ��?��
         * @param cInfoB ���z�I�t�Z�b�g��?� B ��?��
         */
        private FilletInfo(CurveInfo cInfoA, CurveInfo cInfoB) {
            super();

            this.cInfoA = cInfoA;
            this.cInfoB = cInfoB;

            nl_func = new nlFunc();
            dnl_func = new PrimitiveMappingND[2];
            dnl_func[0] = new dnlFunc(0);
            dnl_func[1] = new dnlFunc(1);
            cnv_func = new cnvFunc();
        }

        /**
         * �t�B���b�g��refinement
         * <p/>
         * �A�����̊e���̒l��?�߂�
         * </p>
         *
         * @see Math#solveSimultaneousEquations(PrimitiveMappingND,PrimitiveMappingND[],
         *PrimitiveBooleanMappingNDTo1D,double[])
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
                return 2;
            }

            /**
             * The dimension of the result values. Should be inferior or equal to numInputDimensions(). Should be a strictly positive integer.
             */
            public int numOutputDimensions() {
                return 2;
            }

            public double[] map(double[] parameter) {
                double[] vctr = new double[2];
                Vector2D evec;

                /*
                * sPntA & sPntB are already computed by previous cnvFunc.map()
                */
                evec = sPntA.subtract(sPntB);

                vctr[0] = evec.x();
                vctr[1] = evec.y();

                return vctr;
            }
        }

        /**
         * �t�B���b�g��refinement
         * <p/>
         * �A�����̊e���̕Δ�̒l��?�߂�
         * </p>
         *
         * @see Math#solveSimultaneousEquations(PrimitiveMappingND,PrimitiveMappingND[],
         *PrimitiveBooleanMappingNDTo1D,double[])
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

            public double[] map(double[] parameter) {
                double[] mtrx = new double[2];
                if (idx == 0) {    /* this must be called first */
                    CurveDerivative2D deriv;
                    Vector2D enrm;

                    deriv = cInfoA.curve.evaluation(cInfoA.curve.parameterDomain().force(parameter[0]));
                    sTngA = deriv.d1D();
                    if (cInfoA.side == WhichSide.RIGHT)
                        enrm = new LiteralVector2D(sTngA.y(), -sTngA.x());
                    else
                        enrm = new LiteralVector2D(-sTngA.y(), sTngA.x());
                    enrm = enrm.unitized();

                    /*
                    * solve the following simultaneous equations for N' (enrm: N)
                    *
                    *	(N, N') = 0		->	Nx * N'x + Ny * N'y = 0
                    *
                    *	(P'', N) + (P', N') = 0	->	P''x * Nx + P''y * Ny + P'x * N'x + P'y * N'y = 0
                    */
                    double nrmX, nrmY;
                    if (Math.abs(enrm.x()) > Math.abs(enrm.y())) {
                        nrmY = (-(deriv.d2D().x() * enrm.x() + deriv.d2D().y() * enrm.y()))
                                / (sTngA.y() - ((sTngA.x() * enrm.y()) / enrm.x()));
                        nrmX = (-(enrm.y() * nrmY)) / enrm.x();
                    } else {
                        nrmX = (-(deriv.d2D().x() * enrm.x() + deriv.d2D().y() * enrm.y()))
                                / (sTngA.x() - ((sTngA.y() * enrm.x()) / enrm.y()));
                        nrmY = (-(enrm.x() * nrmX)) / enrm.y();
                    }
                    sNrmA = new LiteralVector2D(nrmX, nrmY);

                    deriv = cInfoB.curve.evaluation(cInfoB.curve.parameterDomain().force(parameter[1]));
                    sTngB = deriv.d1D();
                    if (cInfoB.side == WhichSide.RIGHT)
                        enrm = new LiteralVector2D(sTngB.y(), -sTngB.x());
                    else
                        enrm = new LiteralVector2D(-sTngB.y(), sTngB.x());

                    /*
                    * solve the following simultaneous equations for M' (enrm: M)
                    *
                    *	(M, M') = 0		->	Mx * M'x + My * M'y = 0
                    *
                    *	(Q'', M) + (Q', M') = 0	->	Q''x * Mx + Q''y * My + Q'x * M'x + Q'y * M'y = 0
                    */
                    nrmY = (-(deriv.d2D().x() * enrm.x() + deriv.d2D().y() * enrm.y()))
                            / (sTngB.y() - ((sTngB.x() * enrm.y()) / enrm.x()));
                    nrmX = (-(enrm.y() * nrmY)) / enrm.x();
                    sNrmB = new LiteralVector2D(nrmX, nrmY);

                    mtrx[0] = sTngA.x() + radius * sNrmA.x();
                    mtrx[1] = -sTngB.x() - radius * sNrmB.x();
                } else {
                    mtrx[0] = sTngA.y() + radius * sNrmA.y();
                    mtrx[1] = -sTngB.y() - radius * sNrmB.y();
                }
                return mtrx;
            }
        }

        /**
         * �t�B���b�g��refinement
         * <p/>
         * �A�����̉⪎������ǂ����𔻒肷��
         * </p>
         *
         * @see Math#solveSimultaneousEquations(PrimitiveMappingND,PrimitiveMappingND[],
         *PrimitiveBooleanMappingNDTo1D,double[])
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
                sPntA = cInfoA.evaluate(parameter[0]);
                sPntB = cInfoB.evaluate(parameter[1]);

                return sPntA.identical(sPntB);
            }
        }

        /**
         * �t�B���b�g��refinement��?s��?B
         * <p/>
         * ���z�I�t�Z�b�g��?�m�̌�_��t�B���b�g�̒�?S��?���l�Ƃ���?A
         * �t�B���b�g��?�������?S�ʒu����Z�ŋ?�߂�
         * </p>
         *
         * @param intp ���z�I�t�Z�b�g��?�m�̌�_(�t�B���b�g�̒�?S��?���l)
         * @param pocA ��?� A ?�̃t�B���b�g��?ړ_��?���l
         * @param pocB ��?� B ?�̃t�B���b�g��?ړ_��?���l
         * @return �t�B���b�g
         * @see Math#solveSimultaneousEquations(PrimitiveMappingND,PrimitiveMappingND[],
         *PrimitiveBooleanMappingNDTo1D,double[])
         */
        private FilletObject2D refineFillet(IntersectionPoint2D intp,
                                            PointOnCurve2D pocA,
                                            PointOnCurve2D pocB) {
            double[] param = new double[2];

            param[0] = pocA.parameter();
            param[1] = pocB.parameter();

            double[] refined = GeometryUtils.solveSimultaneousEquations(nl_func, dnl_func, cnv_func, param);
            if (refined == null)
                return null;

            Point2D cntr = sPntA.midPoint(sPntB);
            pocA = new PointOnCurve2D(cInfoA.curve, refined[0], GeometryElement.doCheckDebug);
            pocB = new PointOnCurve2D(cInfoB.curve, refined[1], GeometryElement.doCheckDebug);
            return new FilletObject2D(radius, cntr, pocA, pocB);
        }

        /**
         * ���z�I�t�Z�b�g��?�m�̌�_����t�B���b�g��?�߂�?B
         *
         * @param intp ���z�I�t�Z�b�g��?�m�̌�_
         * @return �t�B���b�g
         */
        private FilletObject2D toFillet(IntersectionPoint2D intp) {
            PointOnCurve2D pocA = cInfoA.curve.nearestProjectWithDistanceFrom(intp, radius);
            PointOnCurve2D pocB = cInfoB.curve.nearestProjectWithDistanceFrom(intp, radius);
            return refineFillet(intp, pocA, pocB);
        }

        /**
         * ��?ۂɃt�B���b�g��?�߂�?��?
         */
        private void getFillets() {
            /*
            * �܂�?��߂Ɏ�?ۂɃI�t�Z�b�g������?�(�ߎ���)���m�̌�_�𓾂�?B
            * ����ꂽ��_���t�B���b�g�̒�?S(��?���l)�ƂȂ�?B
            */
            IntersectionPoint2D[] ints;
            try {
                ints = cInfoA.ofstCrv.intersect(cInfoB.ofstCrv);
            } catch (IndefiniteSolutionException e) {
                /*
                * ������IndefiniteSolution��?����ׂ����ǂ����Y�ނƂ���ł͂���?B
                * �R���ɂ̂݃t�B���b�g��?�������Ȃ�ǂ����µ��Ȃ���?A
                * �����ɔ�?������邱�Ƃ �邽��?B
                */
                IntersectionPoint2D intp = (IntersectionPoint2D) e.suitable();
                ints = new IntersectionPoint2D[1];
                ints[0] = intp;
            }
            /*
            * ����ꂽ��_���ƂɃt�B���b�g��?��֕ϊ�����?B
            */
            FilletObject2D oneSol;
            for (int i = 0; i < ints.length; i++)
                if ((oneSol = toFillet(ints[i])) != null)
                    fillets.addFillet(oneSol);
        }
    }

    /**
     * 2��?�Ԃ̃t�B���b�g�𓾂�
     *
     * @return 2 ��?�̃t�B���b�g�̔z��
     */
    private FilletObject2D[] getFillets() {
        /*
        * ���ꂼ��̋�?�̉��z�I�t�Z�b�g��?��?����ƂɃt�B���b�g�𓾂�
        */
        FilletInfo doObj;
        for (int i = 0; i < infoA.length; i++)
            for (int j = 0; j < infoB.length; j++) {
                doObj = new FilletInfo(infoA[i], infoB[j]);
                doObj.getFillets();
            }
        return fillets.toFilletObject2DArray(false);
    }

    /**
     * 2��?�Ԃ̃t�B���b�g�𓾂�
     *
     * @param curveA ��?� A
     * @param sectA  ��?� A �̑�?ۂƂȂ�p���??[�^���
     * @param sideA  ��?� A �̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
     * @param curveB ��?� B
     * @param sectB  ��?� B �̑�?ۂƂȂ�p���??[�^���
     * @param sideB  ��?� B �̂ǂ��瑤�Ƀt�B���b�g��?�?����邩
     * @param raidus �t�B���b�g�̔��a
     * @return 2 ��?�̃t�B���b�g�̔z��
     * @see ParametricCurve2D
     * @see ParameterSection
     * @see WhichSide
     * @see FilletObject2D
     */
    static FilletObject2D[] fillet(ParametricCurve2D curveA,
                                   ParameterSection sectA,
                                   int sideA,
                                   ParametricCurve2D curveB,
                                   ParameterSection sectB,
                                   int sideB,
                                   double radius)
            throws IndefiniteSolutionException {
        /*
        * �����ł̓|�����C��/�g������?�/��?���?�/��?���?�Z�O�?���g�ȊO�̋�?�赂�?B
        * ?�L��?�ɂ��Ă�?A���ꂼ��̃t�B���b�g?��?�ɔC����?B
        */
        int typeA = curveA.type();
        int typeB = curveB.type();

        switch (typeA) {
            case ParametricCurve2D.LINE_2D:
            case ParametricCurve2D.CIRCLE_2D:
            case ParametricCurve2D.ELLIPSE_2D:
            case ParametricCurve2D.PARABOLA_2D:
            case ParametricCurve2D.HYPERBOLA_2D:
            case ParametricCurve2D.PURE_BEZIER_CURVE_2D:
            case ParametricCurve2D.BSPLINE_CURVE_2D:
                switch (typeB) {
                    case ParametricCurve2D.LINE_2D:
                    case ParametricCurve2D.CIRCLE_2D:
                    case ParametricCurve2D.ELLIPSE_2D:
                    case ParametricCurve2D.PARABOLA_2D:
                    case ParametricCurve2D.HYPERBOLA_2D:
                    case ParametricCurve2D.PURE_BEZIER_CURVE_2D:
                    case ParametricCurve2D.BSPLINE_CURVE_2D:
                        /*
                        * �{�N���X�������t�B���b�g?��?
                        */
                        FiltCrvCrv2D doObj = new FiltCrvCrv2D(curveA, sectA, sideA,
                                curveB, sectB, sideB,
                                radius);    // ?���
                        return doObj.getFillets();                // �t�B���b�g��?�߂�
                    case ParametricCurve2D.TRIMMED_CURVE_2D:
                        return ((TrimmedCurve2D) curveB).doFillet(sectB, sideB, curveA, sectA, sideA,
                                radius, true);
                    case ParametricCurve2D.COMPOSITE_CURVE_2D:
                        return ((CompositeCurve2D) curveB).doFillet(sectB, sideB, curveA, sectA, sideA,
                                radius, true);
                    case ParametricCurve2D.COMPOSITE_CURVE_SEGMENT_2D:
                        return ((CompositeCurveSegment2D) curveB).doFillet(sectB, sideB, curveA, sectA, sideA,
                                radius, true);
                    case ParametricCurve2D.POLYLINE_2D:
                        return ((Polyline2D) curveB).doFillet(sectB, sideB, curveA, sectA, sideA,
                                radius, true);
                    case ParametricCurve2D.BOUNDED_LINE_2D:
                        return ((BoundedLine2D) curveB).doFillet(sectB, sideB, curveA, sectA, sideA,
                                radius, true);
                }
                throw new UnsupportedOperationException();
            case ParametricCurve2D.TRIMMED_CURVE_2D:
                return ((TrimmedCurve2D) curveA).doFillet(sectA, sideA, curveB, sectB, sideB, radius, false);
            case ParametricCurve2D.COMPOSITE_CURVE_2D:
                return ((CompositeCurve2D) curveA).doFillet(sectA, sideA, curveB, sectB, sideB, radius, false);
            case ParametricCurve2D.COMPOSITE_CURVE_SEGMENT_2D:
                return ((CompositeCurveSegment2D) curveA).doFillet(sectA, sideA, curveB, sectB, sideB,
                        radius, false);
            case ParametricCurve2D.POLYLINE_2D:
                return ((Polyline2D) curveA).doFillet(sectA, sideA, curveB, sectB, sideB, radius, false);
            case ParametricCurve2D.BOUNDED_LINE_2D:
                return ((BoundedLine2D) curveA).doFillet(sectA, sideA, curveB, sectB, sideB, radius, false);
        }
        throw new UnsupportedOperationException();
    }
}

// end of file
