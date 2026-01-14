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
 * Interpolation �̂��߂̃N���X(3D)
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:41 $
 */
class Interpolation3D {
    /**
     * ��ԂɕK�v��?��
     *
     * @see Interpolation
     */
    private Interpolation info;

    /**
     * ��Ԃ���_��
     *
     * @see Point3D
     */
    private Point3D[] points;

    /**
     * ���[�̌X��
     *
     * @see Vector3D
     */
    private Vector3D[] endvecs;

/**
     * �_��ƃp�����[�^��^���ăI�u�W�F�N�g��\�z����
     *
     * @param points �_��
     * @param params �p�����[�^
     * @see Point3D
     */
    Interpolation3D(Point3D[] points, double[] params) {
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
     * @see Point3D
     * @see Vector3D
     */
    Interpolation3D(Point3D[] points, double[] params, Vector3D[] endvecs) {
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
     * @see Point3D
     * @see Vector3D
     */
    Interpolation3D(Point3D[] points, double[] params, Vector3D[] endvecs,
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
     * ��ԂɕK�v��?���^���ăI�u�W�F�N�g��\�z����
     *
     * @param info    ��ԂɕK�v��?��
     * @param points  �_��
     * @param endvecs DOCUMENT ME!
     * @see Interpolation
     * @see Point3D
     * @see Vector3D
     */
    private Interpolation3D(Interpolation info, Point3D[] points,
        Vector3D[] endvecs) {
        this.info = info;
        this.points = points;

        if (!info.isClosed) {
            if (endvecs != null) {
                this.endvecs = endvecs;
            } else {
                this.endvecs = besselPoints(points, info.params);
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
     * @see Point3D
     * @see Vector3D
     */
    static Vector3D[] besselPoints(Point3D[] points, double[] params) {
        int uip = points.length;
        Vector3D[] endvecs = new Vector3D[2];
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

        Point3D mPoint = points[1].subtract(points[0].multiply(b0)).toPoint3D()
                                  .subtract(points[2].multiply(b2)).toPoint3D()
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
                                  .toPoint3D()
                                  .subtract(points[index + 2].multiply(b2))
                                  .toPoint3D().divide(b1);
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
     * @see Point3D
     */
    private Point3D[] solveLS(Point3D[] cp) {
        Point3D[] newCp = new Point3D[info.uip];

        for (int i = 0; i < info.uip; i++)
            newCp[i] = cp[i];

        for (int i = 1; i < info.uip; i++) {
            newCp[i] = newCp[i].subtract(newCp[i - 1].multiply(
                        info.matrix.getElementAt(i, 0))).toPoint3D();
        }

        for (int i = info.uip - 2; i >= 0; i--) {
            newCp[i] = newCp[i].subtract(newCp[i + 1].multiply(
                        info.matrix.getElementAt(i, 2))).toPoint3D();
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
     * @see Point3D
     */
    private Point3D[] solveLSClosed(Point3D[] cp) {
        double[] wX = new double[info.uip];
        double[] wY = new double[info.uip];
        double[] wZ = new double[info.uip];
        Point3D[] newCp = new Point3D[info.uip];

        for (int i = 0; i < info.uip; i++) {
            wX[i] = cp[i].x();
        }

        wX = info.matrix.solveSimultaneousLinearEquations(wX);

        for (int i = 0; i < info.uip; i++) {
            wY[i] = cp[i].y();
        }

        wY = info.matrix.solveSimultaneousLinearEquations(wY);

        for (int i = 0; i < info.uip; i++) {
            wZ[i] = cp[i].z();
        }

        wZ = info.matrix.solveSimultaneousLinearEquations(wZ);

        int i;

        for (i = 1; i < info.uip; i++) {
            newCp[i] = new CartesianPoint3D(wX[i - 1], wY[i - 1], wZ[i - 1]);
        }

        newCp[0] = new CartesianPoint3D(wX[i - 1], wY[i - 1], wZ[i - 1]);

        return newCp;
    }

    /**
     * ����_��?�߂�(�J������?��?�?�)
     *
     * @return ����_��
     *
     * @see Point3D
     */
    private Point3D[] solveLinearSystem() {
        Point3D[] cp = new Point3D[info.uip];

        cp[0] = points[0].add(endvecs[0].multiply(info.pInt(0) / 3));

        int i;

        for (i = 1; i < (info.uip - 1); i++) {
            double ework = info.pInt(i - 1) + info.pInt(i);
            cp[i] = points[i].multiply(ework);
        }

        cp[i] = points[i].subtract(endvecs[1].multiply(
                    info.pInt(info.uip - 2) / 3));
        cp = solveLS(cp);

        Point3D[] newCp = new Point3D[info.uip + 2];

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
     * @see Point3D
     */
    private Point3D[] solveLinearSystemClosed() {
        Point3D[] cp = new Point3D[info.uip];

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
     * @see Point3D
     */
    Point3D[] controlPoints() {
        if (!info.isClosed) {
            return solveLinearSystem();
        } else {
            return solveLinearSystemClosed();
        }
    }

    /**
     * ��Ԃ�����?��?���_��?�߂�
     *
     * @param info DOCUMENT ME!
     * @param points DOCUMENT ME!
     * @param endvecs DOCUMENT ME!
     *
     * @return ����_��
     *
     * @see Point3D
     */
    static Point3D[] controlPoints(Interpolation info, Point3D[] points,
        Vector3D[] endvecs) {
        Interpolation3D doObj = new Interpolation3D(info, points, endvecs);

        return doObj.controlPoints();
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
        CartesianPoint3D p0 = new CartesianPoint3D(0.0, 0.0, 0.0);
        CartesianPoint3D p1 = new CartesianPoint3D(1.0, 1.0, 0.0);
        CartesianPoint3D p2 = new CartesianPoint3D(2.0, 0.0, 0.0);
        CartesianPoint3D p3 = new CartesianPoint3D(1.0, -1.0, 0.0);
        LiteralVector3D v = new LiteralVector3D(0.0, -1.0, 0.0);

        CartesianPoint3D[] points = { p0, p1, p2 };
        double[] params = { 0.0, 0.5, 1.0 };
        LiteralVector3D[] vectors = { v, v };

        CartesianPoint3D[] pointsClosed = { p0, p1, p2, p3 };
        double[] paramsClosed = { 0.0, 0.25, 0.5, 0.75, 1.0 };

        Interpolation3D open;
        Interpolation3D closed;

        System.out.println("\n\n<for open case.>\n");
        open = new Interpolation3D(points, params, vectors);

        Point3D[] cp = open.controlPoints();
        System.out.println("\n\n[interpolated points]\n");

        for (int i = 0; i < cp.length; i++) {
            System.out.println("cp[" + i + "] = (" + cp[i].x() + ", " +
                cp[i].y() + ", " + cp[i].z() + ")");
        }

        System.out.println("\n\n<for closed case.>\n");
        closed = new Interpolation3D(pointsClosed, paramsClosed, null, true);

        Point3D[] cpClosed = closed.controlPoints();
        System.out.println("\n[interpolated points]\n");

        for (int i = 0; i < cpClosed.length; i++) {
            System.out.println("cp[" + i + "] = (" + cpClosed[i].x() + ", " +
                cpClosed[i].y() + ", " + cpClosed[i].z() + ")");
        }
    }
}
// end of file
