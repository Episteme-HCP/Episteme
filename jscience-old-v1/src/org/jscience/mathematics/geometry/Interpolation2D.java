/*
 * Interpolation �̂��߂̃N���X(2D)
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Interpolation2D.java,v 1.3 2007-10-23 18:19:40 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

/**
 * Interpolation �̂��߂̃N���X(2D)
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:40 $
 */
class Interpolation2D {
    /**
     * ��ԂɕK�v��?��
     *
     * @see Interpolation
     */
    private Interpolation info;

    /**
     * ��Ԃ���_��
     *
     * @see Point2D
     */
    private Point2D[] points;

    /**
     * ���[�̌X��
     *
     * @see Vector2D
     */
    private Vector2D[] endvecs;

/**
     * �_��ƃp�����[�^��^���ăI�u�W�F�N�g��\�z����
     *
     * @param points �_��
     * @param params �p�����[�^
     * @see Point2D
     */
    Interpolation2D(Point2D[] points, double[] params) {
        this.info = new Interpolation(params, false);
        this.points = points;
        this.endvecs = besselPoints(points, params);
    }

/**
     * �_��A�p�����[�^�A���[�̌X����^���ăI�u�W�F�N�g��\�z����
     *
     * @param points  �_��
     * @param params  �p�����[�^
     * @param endvecs ���[�̌X��
     * @see Point2D
     * @see Vector2D
     */
    Interpolation2D(Point2D[] points, double[] params, Vector2D[] endvecs) {
        this.info = new Interpolation(params, false);
        this.points = points;
        this.endvecs = endvecs;
    }

/**
     * �_��A�p�����[�^�A���[�̌X���A���Ă��邩�ǂ����̃t���O��^���ăI�u�W�F�N�g��\�z����
     *
     * @param points   �_��
     * @param params   �p�����[�^
     * @param endvecs  ���[�̌X��(���Ă���͖��������)
     * @param isClosed ���Ă��邩�ǂ����̃t���O
     * @see Point2D
     * @see Vector2D
     */
    Interpolation2D(Point2D[] points, double[] params, Vector2D[] endvecs,
        boolean isClosed) {
        this.info = new Interpolation(params, isClosed);
        this.points = points;

        if (!info.isClosed) {
            if (endvecs != null) {
                this.endvecs = endvecs;
            } else {
                this.endvecs = besselPoints(points, params);
            }
        }
    }

    /**
     * �x�b�Z���_(���[�̌X��)��?�߂�
     *
     * @param points �_��
     * @param params �p�����[�^
     *
     * @return ���[�̌X��
     *
     * @see Point2D
     * @see Vector2D
     */
    static Vector2D[] besselPoints(Point2D[] points, double[] params) {
        int uip = points.length;
        Vector2D[] endvecs = new Vector2D[2];
        double delta = params[1] - params[0];

        if (uip == 2) {
            endvecs[0] = points[1].subtract(points[0]);
            endvecs[0] = endvecs[0].divide(delta);
            endvecs[1] = endvecs[0];

            return endvecs;
        }

        // start
        double t = delta / (params[2] - params[0]);
        double t1 = 1.0 - t;
        double b0 = t1 * t1;
        double b1 = 2.0 * t * t1;
        double b2 = t * t;

        Point2D mPoint = points[1].subtract(points[0].multiply(b0)).toPoint2D()
                                  .subtract(points[2].multiply(b2)).toPoint2D()
                                  .divide(b1);
        endvecs[0] = mPoint.subtract(points[0]).multiply((t * 2.0) / delta);

        // end
        int index = uip - 3;
        t = (params[index + 1] - params[index]) / (params[index + 2] -
            params[index]);
        t1 = 1.0 - t;
        b0 = t1 * t1;
        b1 = 2.0 * t * t1;
        b2 = t * t;

        mPoint = points[index + 1].subtract(points[index].multiply(b0))
                                  .toPoint2D()
                                  .subtract(points[index + 2].multiply(b2))
                                  .toPoint2D().divide(b1);
        delta = params[index + 2] - params[index + 1];
        endvecs[1] = points[index + 2].subtract(mPoint)
                                      .multiply((t1 * 2.0) / delta);

        return endvecs;
    }

    /**
     * ����_��?�߂�
     *
     * @param cp ����_��
     *
     * @return ����_��
     *
     * @see Point2D
     */
    private Point2D[] solveLS(Point2D[] cp) {
        Point2D[] newCp = new Point2D[info.uip];

        for (int i = 0; i < info.uip; i++)
            newCp[i] = cp[i];

        for (int i = 1; i < info.uip; i++) {
            newCp[i] = newCp[i].subtract(newCp[i - 1].multiply(
                        info.matrix.getElementAt(i, 0))).toPoint2D();
        }

        for (int i = info.uip - 2; i >= 0; i--) {
            newCp[i] = newCp[i].subtract(newCp[i + 1].multiply(
                        info.matrix.getElementAt(i, 2))).toPoint2D();
        }

        for (int i = 0; i < info.uip; i++)
            newCp[i] = newCp[i].divide(info.matrix.getElementAt(i, 1));

        return newCp;
    }

    /**
     * ����_��?�߂�(������?��?�?�)
     *
     * @param cp ����_��
     *
     * @return ����_��
     *
     * @see Point2D
     */
    private Point2D[] solveLSClosed(Point2D[] cp) {
        double[] wX = new double[info.uip];
        double[] wY = new double[info.uip];
        Point2D[] newCp = new Point2D[info.uip];

        for (int i = 0; i < info.uip; i++) {
            wX[i] = cp[i].x();
        }

        wX = info.matrix.solveSimultaneousLinearEquations(wX);

        for (int i = 0; i < info.uip; i++) {
            wY[i] = cp[i].y();
        }

        wY = info.matrix.solveSimultaneousLinearEquations(wY);

        int i;

        for (i = 1; i < info.uip; i++) {
            newCp[i] = new CartesianPoint2D(wX[i - 1], wY[i - 1]);
        }

        newCp[0] = new CartesianPoint2D(wX[i - 1], wY[i - 1]);

        return newCp;
    }

    /**
     * ����_��?�߂�(�J������?��?�?�)
     *
     * @return ����_��
     *
     * @see Point2D
     */
    private Point2D[] solveLinearSystem() {
        Point2D[] cp = new Point2D[info.uip];

        cp[0] = points[0].add(endvecs[0].multiply(info.pInt(0) / 3));

        int i;

        for (i = 1; i < (info.uip - 1); i++) {
            double ework = info.pInt(i - 1) + info.pInt(i);
            cp[i] = points[i].multiply(ework);
        }

        cp[i] = points[i].subtract(endvecs[1].multiply(
                    info.pInt(info.uip - 2) / 3));
        cp = solveLS(cp);

        Point2D[] newCp = new Point2D[info.uip + 2];

        newCp[0] = points[0];

        for (i = 0; i < info.uip; i++)
            newCp[i + 1] = cp[i];

        newCp[info.uip + 1] = points[info.uip - 1];

        return newCp;
    }

    /**
     * ����_��?�߂�(������?��?�?�)
     *
     * @return ����_��
     *
     * @see Point2D
     */
    private Point2D[] solveLinearSystemClosed() {
        Point2D[] cp = new Point2D[info.uip];

        for (int i = 0; i < info.uip; i++) {
            double ework = info.pInt(i - 1) + info.pInt(i);
            cp[i] = points[i].multiply(ework);
        }

        cp = solveLSClosed(cp);

        return cp;
    }

    /**
     * ��Ԃ�����?�̃m�b�g����?�߂�
     *
     * @return �m�b�g���
     *
     * @see BsplineKnot
     */
    BsplineKnot knotData() {
        return info.knotData();
    }

    /**
     * ��Ԃ�����?��?���_��?�߂�
     *
     * @return ����_��
     *
     * @see Point2D
     */
    Point2D[] controlPoints() {
        if (!info.isClosed) {
            return solveLinearSystem();
        } else {
            return solveLinearSystemClosed();
        }
    }

    /**
     * ��Ԃ�����?��?d�݂�?�߂�
     *
     * @return �d��(�������`���Ȃ̂�null)
     */
    double[] weights() {
        return null;
    }

    /**
     * �f�o�b�O�p���C���v���O�����B
     *
     * @param argv DOCUMENT ME!
     */
    public static void main(String[] argv) {
        CartesianPoint2D p0 = new CartesianPoint2D(0.0, 0.0);
        CartesianPoint2D p1 = new CartesianPoint2D(1.0, 1.0);
        CartesianPoint2D p2 = new CartesianPoint2D(2.0, 0.0);
        CartesianPoint2D p3 = new CartesianPoint2D(1.0, -1.0);
        LiteralVector2D v = new LiteralVector2D(0.0, -1.0);

        Interpolation2D open;
        Interpolation2D closed;

        // open case
        CartesianPoint2D[] points = { p0, p1, p2 };
        double[] params = { 0.0, 0.5, 1.0 };
        LiteralVector2D[] vectors = { v, v };

        System.out.println("\n\n<for open case.>\n");
        open = new Interpolation2D(points, params, vectors);

        Point2D[] cp = open.controlPoints();
        System.out.println("\n\n[interpolated points]\n");

        for (int i = 0; i < cp.length; i++) {
            System.out.println("cp[" + i + "] = (" + cp[i].x() + ", " +
                cp[i].y() + ")");
        }

        // closed case
        CartesianPoint2D[] pointsClosed = { p0, p1, p2, p3 };
        double[] paramsClosed = { 0.0, 0.25, 0.5, 0.75, 1.0 };

        System.out.println("\n\n<for closed case.>\n");

        closed = new Interpolation2D(pointsClosed, paramsClosed, null, true);

        Point2D[] cpClosed = closed.controlPoints();
        System.out.println("\n[interpolated points]\n");

        for (int i = 0; i < cpClosed.length; i++) {
            System.out.println("cp[" + i + "] = (" + cpClosed[i].x() + ", " +
                cpClosed[i].y() + ")");
        }
    }
}
// end of file
