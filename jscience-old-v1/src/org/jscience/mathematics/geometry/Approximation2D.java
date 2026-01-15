/*
 * �ߎ�(Approximation)���ꂽBspline��?��?�?����邽�߂̃N���X(2D)
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Approximation2D.java,v 1.3 2006/03/01 21:15:52 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �ߎ�(Approximation)���ꂽBspline��?��?�?����邽�߂̃N���X(2D)
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:15:52 $
 */

class Approximation2D extends Approximation {
    /**
     * �ߎ�����_��
     *
     * @see Point2D
     */
    private Point2D[] points;

    /**
     * �n�_��?I�_��?�?��� --- [0]?F�n�_?A[1]?F?I�_
     * (�����`����?�?��� null �̂܂�)
     *
     * @see Vector2D
     */
    private Vector2D[] endVectors = null;

    /**
     * �_��?A�p���??[�^��^���ăI�u�W�F�N�g��?\�z����
     *
     * @param points     �_��
     * @param params     �p���??[�^
     * @param endVectors �n�_,?I�_��?�?���
     * @param isClosed   �����`�����ǂ���
     * @see Point2D
     * @see Vector2D
     */
    Approximation2D(Point2D[] points, double[] params,
                    Vector2D[] endVectors,
                    boolean isClosed) {
        super(points.length, params, isClosed);
        this.points = points;

        if (!isClosed) {
            // �J�����`����?�?���?���_�̗��[��?���?ݒ�
            if (endVectors == null)
                this.endVectors = Interpolation2D.besselPoints(points, params);
            else
                this.endVectors = endVectors;
        }
    }

    //
    // �ȉ��͋��e��?���^�����ċߎ�����?�?��ɕK�v��?��?
    // gh2aprcBsc3_Rev2, gh2aprcCBsc3_Rev2 (in gh2aprcBscR2.c) �����?A
    //

    /**
     * �_�̒��Ԃ�?��x�𖞂����Ă��邩�ǂ����𒲂ׂ�
     *
     * @param bsc     �ߎ����ꂽ��?�
     * @param mid_tol �_�̒��Ԃ�?��x
     * @return ?��x�𖞂����Ă��邩�ǂ���
     * @see BsplineCurve2D
     */
    private boolean tolerated2(BsplineCurve2D bsc, double mid_tol) {
        Point2D mid_pnt;
        double mid_param;
        Point2D mid_param_crd;
        int i, j;

        if (!isClosed) {
            for (i = 0; i < (nPoints - 1); i++) {
                mid_pnt = points[i].midPoint(points[i + 1]);
                mid_param = 0.5 * params[i] + 0.5 * params[i + 1];
                mid_param_crd = bsc.coordinates(mid_param);
                if (mid_pnt.distance(mid_param_crd) > mid_tol)
                    return false;
            }
        } else {
            for (i = 0, j = 1; i < nPoints; i++, j++) {
                mid_pnt = points[i].midPoint(points[j % nPoints]);
                mid_param = 0.5 * params[i] + 0.5 * params[j];
                mid_param_crd = bsc.coordinates(mid_param);
                if (mid_pnt.distance(mid_param_crd) > mid_tol)
                    return false;
            }
        }

        return true;
    }

    /**
     * �ȗ���?�߂�?B
     *
     * @param lower    ���l
     * @param upper    ?�l
     * @param bsc_intp ��Ԃ��ꂽ Bspline��?�
     * @return �ȗ��̔z��
     * @see BsplineCurve2D
     */
    private double[] getCurvatures(int lower, int upper, BsplineCurve2D bsc_intp) {
        double[] curvatures = new double[nPoints];
        for (int i = lower; i <= upper; i++) {
            curvatures[i] = bsc_intp.curvature(params[i]).curvature();
        }

        return curvatures;
    }

    //
    // �ȉ��̓Z�O�?���g(�m�b�g��)��^�����ċߎ�����?�?��ɕK�v��?��?
    // gh2aprxBsc3, gh2aprxCBsc3 (in gh2aprxBsc.c) �����?A
    //

    /**
     * �_��?�ɓ��e����?B
     *
     * @param dApnt ���e�����_A
     * @param dBpnt ��?�?�̓_B
     * @param dBdir ��?�̃x�N�g��B
     * @return ��?�B�ւ̓_A��?�?�̑�
     */
    private Point2D projectPointLine(Point2D dApnt,
                                     Point2D dBpnt,
                                     Vector2D dBdir) {
        Vector2D euvec;    // unitized vector of line
        Vector2D evpp;    // vector from dBpnt to dApnt
        double edot;        // dot product

        // set unit vector of dBdir
        euvec = dBdir.unitized();

        evpp = dApnt.subtract(dBpnt);
        edot = euvec.dotProduct(evpp);
        return dBpnt.add(euvec.multiply(edot));
    }

    /**
     * �e�_ points[i] �� ���̃p���??[�^�l params[i] �ɑΉ������?�?��
     * �_�̋���(�c?�)��Ԃ�
     *
     * @param bsc �ߎ����ꂽ��?�
     * @return �c?��̔z��
     * @see BsplineCurve2D
     */
    private double[] compResiduals(BsplineCurve2D bsc) {
        Point2D bpnt;
        int npnts = points.length;
        double[] res = new double[npnts];

        if (debug)
            bsc.output(System.err);

        for (int i = 0; i < npnts; i++) {
            bpnt = bsc.coordinates(params[i]);
            res[i] = points[i].distance(bpnt);

            if (debug) {
                System.err.println("i = " + i);
                System.err.println("params[" + i + "] = " + params[i]);
                System.err.println("bpnt[" + i + "] = ("
                        + bpnt.x() + ", " + bpnt.y() + ")");
                System.err.println("res[" + i + "] = " + res[i]);
            }
        }

        return res;
    }

    /**
     * ?���_��� X ?�����ߎ�����
     *
     * @param �ߎ��v�Z�̂��߂̃��R�r�A��?s��
     * @return ?���_��� X ?����̔z��
     * @see Matrix
     */
    private Matrix.LinearLeastSquareSolution fitX(Matrix matrix) {
        if (debug)
            System.err.println("[getting fitX]");

        double[] rightHandSideVector = new double[matrix.getRowSize()];
        for (int i = 0; i < rightHandSideVector.length; i++) {
            rightHandSideVector[i] = points[i].x();
        }

        return matrix.solveLinearLeastSquare2(rightHandSideVector);
    }

    /**
     * ?���_��� Y ?�����ߎ�����
     *
     * @param �ߎ��v�Z�̂��߂̃��R�r�A��?s��
     * @return ?���_��� Y ?����̔z��
     * @see Matrix
     */
    private Matrix.LinearLeastSquareSolution fitY(Matrix matrix) {
        if (debug)
            System.err.println("[getting fitY]");

        double[] rightHandSideVector = new double[matrix.getRowSize()];
        for (int i = 0; i < rightHandSideVector.length; i++) {
            rightHandSideVector[i] = points[i].y();
        }

        return matrix.solveLinearLeastSquare2(rightHandSideVector);
    }

    /**
     * ?���_���?�߂�
     *
     * @param knotData Bspline�̃m�b�g��
     * @return ?���_��
     */
    private CartesianPoint2D[] getControlPoints(BsplineKnot knotData) {
        Matrix matrix = getDesignMatrix(knotData);

        Matrix.LinearLeastSquareSolution x = fitX(matrix);
        Matrix.LinearLeastSquareSolution y = fitY(matrix);

        CartesianPoint2D[] controlPoints =
                new CartesianPoint2D[knotData.nControlPoints()];
        for (int i = 0; i < knotData.nControlPoints(); i++) {
            controlPoints[i] = new CartesianPoint2D(x.solutionAt(i),
                    y.solutionAt(i));
        }
        return controlPoints;
    }

    /**
     * �_���ߎ�����Bspline��?��?�߂�?B
     * (���Ă���?�?��� gh2aprxBsc3, �J���Ă���?�?��� gh2aprxCBsc3)
     * <p/>
     * �_���ߎ�����Bspline��?��?�?���?�@��p���ċ?�߂�?B
     * �������?��nsegs�̃Z�O�?���g��?��?Aknots�Ŏw�肳�ꂽ�m�b�g���?��?B
     * </p>
     *
     * @param nsegs �Z�O�?���g��?�
     * @param knots �m�b�g��(nsegs+1���L��ł���?A�����?~�͖�������)
     * @return �ߎ����ꂽ Bspline��?�
     * @see BsplineCurve3D
     */
    BsplineCurve2D getApproximationWithKnots(int nsegs, double[] knots) {
        if (nsegs < minSegmentNumber() || nsegs > maxSegmentNumber())
            throw new InvalidArgumentValueException();

        // Knot Data
        BsplineKnot knotData = getKnotData(nsegs, knots);

        // ?���_
        Point2D[] control = getControlPoints(knotData);
        if (debug) {
            for (int i = 0; i < control.length; i++) {
                System.err.println("control[" + i + "] = ("
                        + control[i].x() + ", "
                        + control[i].y() + ")");
            }
        }

        // �J���Ă���?�?�
        if (!isClosed) {
            // adjust the neighbour of endpoints with a given tangential
            // direction
            control[1] = projectPointLine(control[1], control[0], endVectors[0]);
            control[control.length - 2]
                    = projectPointLine(control[control.length - 2], control[control.length - 1], endVectors[1]);
        }

        return new BsplineCurve2D(knotData, control, null);
    }

    /**
     * �_���ߎ�����3��Bspline��?��?�߂�?B
     * (���Ă���?�?��� gh2aprxBsc3, �J���Ă���?�?��� gh2aprxCBsc3)
     * <p/>
     * �_���^����ꂽ?��x�ŋߎ�����Bspline��?��?�߂�?B
     * </p>
     *
     * @param tol    �ߎ�����R���a�X�v���C����?��?��x?B
     *               ?�?�������?�͗^����ꂽ�_��ɑ΂��Ă��̋��e��?��ȓ��?�?������?B
     * @param midTol �ߎ�����R���a�X�v���C����?��?��x(�_�̒��Ԃ�?��x)?B
     *               ?�?�������?�͗^����ꂽ�_��̂��ꂼ��̒��ԓ_�ɑ΂���
     *               ���̋��e��?��ȓ��?�?������?B
     * @return �ߎ����ꂽ Bspline��?�
     * @see BsplineCurve2D
     * @see ToleranceForDistance
     */
    BsplineCurve2D getApproximationWithTolerance(ToleranceForDistance tol,
                                                 ToleranceForDistance midTol) {
        /*
        * ?��߂ɗ^����ꂽ�_����Ԃ����?��?�߂Ă���?B
        * ���̋�?�͋ߎ������?�̎w�W�ƂȂ�?A
        * �܂��^����ꂽ���e��?��𖞂����Ȃ�?�?��ɂ͂��̋�?��ԋp����?B
        */
        BsplineCurve2D intp_bsc = new BsplineCurve2D(points, params, endVectors, isClosed);

        if (debug)
            intp_bsc.output(System.err);

        /*
        * ��Ԃ�����?��p���^����ꂽ�e�_�ł̋ȗ���?�߂Ă���
        */
        int upper, lower;
        lower = 2;
        if (isClosed) {
            upper = nPoints - 2;
        } else {
            upper = nPoints - 3;
        }

        double[] curvatures = getCurvatures(lower, upper, intp_bsc);
        double[] sortedCurvatures = (double[]) curvatures.clone();
        if (lower < upper) {
            GeometryUtils.sortDoubleArray(sortedCurvatures, lower, upper);
        }

        /*
        * �ߎ������?�̃Z�O�?���g?���?���l�숂߂�
        */
        int[] nsegs = new int[nPoints + MARGIN];
        int nsegI = 0;
        if ((nsegs[nsegI] = initSegmentNumber()) < 0)
            return intp_bsc;        // nPoints is too few

        /*
        * ��?~?A�ߎ�������?�?��x�𖞂����Ă���?�?���?A
        * �Z�O�?���g?��츂炵?A�������Ȃ�?�?��͑?�₵��?ċߎ��݂�?B
        * �����J��Ԃ���?A?��x�𖞂�������BƂ�Z�O�?���g?���?��Ȃ�
        * ��?��?�߂�?B
        */
        double[] knots = new double[nPoints + MARGIN];
        boolean isTolerated;

        BsplineCurve2D bsc = null;
        BsplineCurve2D aprx_bsc = null;
        int bsc_nseg = nsegs[nsegI];

        while (true) {
            // �Z�O�?���g?�����m�b�g���?��
            double ep;
            if (isClosed)
                ep = params[nPoints];
            else
                ep = params[nPoints - 1];
            if (compKnots(params[0], ep, nsegs[nsegI], lower, upper,
                    curvatures, sortedCurvatures, knots)) {
                // �Z�O�?���g��^���ċߎ���?�𓾂�
                aprx_bsc = getApproximationWithKnots(nsegs[nsegI], knots);
                // �c?��𓾂�
                double[] res = compResiduals(aprx_bsc);

                // ���e��?��𖞂�������?
                if (tolerated(tol.value(), res) &&
                        tolerated2(aprx_bsc, midTol.value())) {
                    isTolerated = true;
                    bsc = aprx_bsc;
                    bsc_nseg = nsegs[nsegI];
                } else {
                    isTolerated = false;
                }
            } else {
                // ��?݂̃Z�O�?���g?��ł̓m�b�g��?��Ȃ�
                aprx_bsc = null;
                isTolerated = false;
            }

            // ���̃Z�O�?���g?��𓾂�
            if (!reNewSegmentNumber(nsegs, nsegI, isTolerated))
                break;    // �¤�����?㎎����Z�O�?���g?����Ȃ�

            nsegI++;
        }

        if (isClosed && bsc != null && bsc_nseg >= (nPoints - degree)) {
            /*
            * if closed curve is desired and the number of segments of obtained
            * curve is near as the number of given points, there is a fear of
            * zig-zag winding. therefore we discard that for safety.
            */
            if (debug)
                System.err.println("nseg = " + bsc_nseg + ", discarded");
            bsc = null;
        }

        if (bsc == null) {    // ?��x�𖞂����ߎ���?�͓����Ȃ��B�
            bsc = intp_bsc;    // ��ԋ�?��ԋp����
        }
        return bsc;
    }

    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     */
    public static void main(String argv[]) {
        ToleranceForDistance tol = new ToleranceForDistance(0.1);
        ToleranceForDistance mid_tol = new ToleranceForDistance(10.0);

        System.out.println("Main: [creating Approximation2D.]");

        // for closed case
        if (true) {
            CartesianPoint2D p0 = new CartesianPoint2D(0.0, 0.0);
            CartesianPoint2D p1 = new CartesianPoint2D(0.4, 0.6);
            CartesianPoint2D p2 = new CartesianPoint2D(1.0, 1.0);
            CartesianPoint2D p3 = new CartesianPoint2D(1.6, 0.6);
            CartesianPoint2D p4 = new CartesianPoint2D(2.0, 0.0);
            CartesianPoint2D p5 = new CartesianPoint2D(1.6, -0.6);
            CartesianPoint2D p6 = new CartesianPoint2D(1.0, -1.0);
            CartesianPoint2D p7 = new CartesianPoint2D(0.4, -0.6);

            CartesianPoint2D[] pntsClosed = {p0, p1, p2, p3, p4, p5, p6, p7};
            double[] prmsClosed = {0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0};
            Approximation2D aprxClosed =
                    new Approximation2D(pntsClosed, prmsClosed, null, true);
            System.out.println("Main: [creating BsplineCurve2D.]");
            BsplineCurve2D bsplineClosed = aprxClosed.getApproximationWithTolerance(tol, mid_tol);
            System.out.println("\nMain: [Approximation2D Closed Test]");
            bsplineClosed.output(System.out);
        }

        // for open case
        if (true) {
            CartesianPoint2D p0 = new CartesianPoint2D(0.0, 0.0);
            CartesianPoint2D p1 = new CartesianPoint2D(0.4, 0.2);
            CartesianPoint2D p2 = new CartesianPoint2D(1.0, 0.3);
            CartesianPoint2D p3 = new CartesianPoint2D(1.6, 0.25);
            CartesianPoint2D p4 = new CartesianPoint2D(2.0, 0.2);
            CartesianPoint2D p5 = new CartesianPoint2D(2.4, 0.25);
            CartesianPoint2D p6 = new CartesianPoint2D(3.0, 0.3);
            CartesianPoint2D p7 = new CartesianPoint2D(3.6, 0.25);
            CartesianPoint2D p8 = new CartesianPoint2D(4.0, 0.2);
            LiteralVector2D sv = new LiteralVector2D(0.4, 0.2);
            LiteralVector2D ev = new LiteralVector2D(0.4, -0.05);

            CartesianPoint2D[] pntsOpen = {p0, p1, p2, p3, p4, p5, p6, p7, p8};
            double[] prmsOpen = {0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0};
            LiteralVector2D[] endvecs = {sv, ev};
            Approximation2D aprxOpen =
                    new Approximation2D(pntsOpen, prmsOpen, endvecs, false);
            System.out.println("\n\nMain: [creating Open BsplineCurve2D.]");
            BsplineCurve2D bsplineOpen = aprxOpen.getApproximationWithTolerance(tol, mid_tol);
            System.out.println("\nMain: [Approximation2D Open Test]");
            bsplineOpen.output(System.out);
        }
    }
}

// end of file
