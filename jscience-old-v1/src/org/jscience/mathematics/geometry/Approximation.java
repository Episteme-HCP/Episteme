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
 * �ߎ�(Approximation)���ꂽBspline��?��?�?����邽�߂̒�?ۃN���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:38 $
 */
class Approximation {
    /** DOCUMENT ME! */
    protected static boolean debug = false;

    /** ��?� */
    protected static final int degree = 3;

    /** DOCUMENT ME! */
    protected static final int MARGIN = 4;

    /*
     * �����`�����ۂ���\���t���O
     */
    /** DOCUMENT ME! */
    protected boolean isClosed;

    /** �ߎ�����_���?� */
    protected int nPoints;

    /**
     * �ߎ�����_��̃p���??[�^
     * �J�����`����?�?�?AnPoints�̔z��?B
     * �����`����?�?�?A(nPoints+1)�̔z��?B
     */
    protected double[] params;

    /** DOCUMENT ME! */
    private int nseg_numer = 1;

    /** DOCUMENT ME! */
    private int nseg_denom = 2;

/**
     * �I�u�W�F�N�g��?\�z����
     *
     * @param nPoints  �_�̌�?�
     * @param params   �p���??[�^��
     * @param isClosed �����`�����ǂ���
     * @throws InvalidArgumentValueException DOCUMENT ME!
     */
    protected Approximation(int nPoints, double[] params, boolean isClosed) {
        // �_��?�������Ȃ�
        if ((nPoints < 2) || (isClosed && (nPoints < 3))) {
            throw new InvalidArgumentValueException();
        }

        // �z���?���?��BĂ��Ȃ�
        if ((!isClosed && (nPoints != params.length)) ||
                (isClosed && ((nPoints + 1) != params.length))) {
            throw new InvalidArgumentValueException();
        }

        this.nPoints = nPoints;
        this.params = params;
        this.isClosed = isClosed;
    }

    //
    // �ȉ��͋��e��?���^�����ċߎ�����?�?��ɕK�v��?��?
    // gh2aprcBsc3_Rev2, gh2aprcCBsc3_Rev2 (in gh2aprcBscR2.c) �����?A
    //
    /**
     * �Z�O�?���g��?��̎�蓾��?�?��l��Ԃ�
     *
     * @param isClosed
     *        �����`�����ۂ���\���t���O
     * @param degree ��?�
     *
     * @return �Z�O�?���g��?��̎�蓾��?�?��l
     */
    static int minSegmentNumber(boolean isClosed, int degree) {
        if (isClosed) {
            /*
             * if closed curve is desired, nseg should be greater
             * than the degree of curve
             */
            return degree + 1;
        } else {
            return 1;
        }
    }

    /**
     * �Z�O�?���g��?��̎�蓾��?�?��l��Ԃ�
     *
     * @return �Z�O�?���g��?��̎�蓾��?�?��l
     */
    protected int minSegmentNumber() {
        return minSegmentNumber(isClosed, degree);
    }

    /**
     * �Z�O�?���g��?��̎�蓾��?ő�l��Ԃ�
     *
     * @param nPoints DOCUMENT ME!
     * @param isClosed
     *        �����`�����ۂ���\���t���O
     * @param degree ��?�
     *
     * @return �Z�O�?���g��?��̎�蓾��?ő�l
     */
    static int maxSegmentNumber(int nPoints, boolean isClosed, int degree) {
        if (isClosed) {
            return nPoints;
        } else {
            return nPoints - degree;
        }
    }

    /**
     * �Z�O�?���g��?��̎�蓾��?ő�l��Ԃ�
     *
     * @return �Z�O�?���g��?��̎�蓾��?ő�l
     */
    protected int maxSegmentNumber() {
        return maxSegmentNumber(nPoints, isClosed, degree);
    }

    /**
     * �Z�O�?���g��?���?���l��Z?o����
     *
     * @return �Z�O�?���g��?���?���l
     */
    protected int initSegmentNumber() {
        nseg_numer = 1;
        nseg_denom = 2;

        int nseg = (nPoints * nseg_numer) / nseg_denom;

        int min_nseg = minSegmentNumber();

        if (nseg < min_nseg) {
            nseg = min_nseg;
        }

        if (nseg > maxSegmentNumber()) {
            return -1; // nPoints is too few
        }

        return nseg;
    }

    /*
     * ?󋵂ɉ�����?A���Ɏ����Z�O�?���g?��숂߂�
     *
     * @param nsegs        ?��܂łɎ������Z�O�?���g?��̔z��
     *                        ����ю��Ɏ����Z�O�?���g?�(nsegI�ԖڂɃZ�b�g�����)
     * @param nsegI        ?��܂łɎ������Z�O�?���g?���?�
     * @param is_tolerated        �O��̋ߎ������e��?��𖞂��������ǂ���?
     *                                <code>true</code>�Ȃ�Ύ��̃Z�O�?���g?��츂炷?B
     *                                <code>false</code>�Ȃ�Α?�₷?B
     * @return                <code>true</code>�Ȃ�Ύ��̃Z�O�?���g?��͂܂�������Ă��Ȃ�?B
     *                        <code>false</code>�Ȃ�Ύ��̃Z�O�?���g?��͊�Ɏ�����Ă���?B
     */
    protected boolean reNewSegmentNumber(int[] nsegs, int nsegI,
        boolean is_tolerated) {
        if (debug) {
            System.err.println("// nseg = " + nsegs[nsegI] + ", tolerated = " +
                is_tolerated);
        }

        /*
         * if current curve is tolerated, decrease segments.
         * otherwise, increase segments.
         */
        nseg_denom *= 2;
        nseg_numer *= 2;

        if (is_tolerated) {
            nseg_numer--;
        } else {
            nseg_numer++;
        }

        nsegs[++nsegI] = (nPoints * nseg_numer) / nseg_denom;

        /*
         * compare nseg with the maximum value
         */
        int min_nseg = minSegmentNumber();
        int max_nseg = maxSegmentNumber();

        if (max_nseg < min_nseg) {
            max_nseg = min_nseg;
        }

        if (nsegs[nsegI] < min_nseg) {
            nsegs[nsegI] = min_nseg;
        }

        if (nsegs[nsegI] > max_nseg) {
            nsegs[nsegI] = max_nseg;
        }

        /*
         * if nseg is same as previous, stop the approaching
         */
        for (int i = 0; i < nsegI; i++) {
            if (nsegs[i] == nsegs[nsegI]) {
                return false;
            }
        }

        return true;
    }

    /**
     * �Z�O�?���g?�����m�b�g���?��
     *
     * @param sp DOCUMENT ME!
     * @param ep DOCUMENT ME!
     * @param nseg DOCUMENT ME!
     * @param lower DOCUMENT ME!
     * @param upper DOCUMENT ME!
     * @param curvatures DOCUMENT ME!
     * @param sorted_curvatures DOCUMENT ME!
     * @param knots DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean compKnots(double sp, double ep, int nseg, int lower,
        int upper, double[] curvatures, double[] sorted_curvatures,
        double[] knots) {
        double threshold;
        int i;
        int k;

        if (nseg == 1) {
            knots[0] = sp;
            knots[1] = ep;
        } else {
            if ((k = upper - (nseg - 2)) < lower) {
                if (debug) {
                    System.err.println("nseg is too large\n");
                }

                return false;
            }

            threshold = sorted_curvatures[k];

            if (debug) {
                System.err.println("threshold : " + threshold);
            }

            k = 0;
            knots[k++] = sp;

            for (i = lower; i <= upper; i++) {
                if (!(curvatures[i] < threshold) && (k < nseg)) {
                    knots[k++] = params[i];
                }
            }

            knots[k] = ep;

            if (nseg != k) {
                if (debug) {
                    System.err.println("something wrong\n");
                }

                return false;
            }
        }

        return true;
    }

    /**
     * ���e��?��𖞂��������ǂ����𔻒肷��
     *
     * @param tol ���e��?�
     * @param res �c?��̔z��
     *
     * @return ���e��?��𖞂��������ǂ���
     */
    protected boolean tolerated(double tol, double[] res) {
        int i;

        if (debug) {
            double max_r;
            int max_i;

            max_r = res[0];
            max_i = 0;

            for (i = 0; i < nPoints; i++) {
                if (res[i] > max_r) {
                    max_r = res[i];
                    max_i = i;
                }
            }

            System.err.println("max res : " + max_r + " [" + max_i + "]");
        }

        for (i = 0; i < nPoints; i++) {
            if (res[i] > tol) {
                return false;
            }
        }

        return true;
    }

    //
    // �ȉ��̓Z�O�?���g(�m�b�g��)��^�����ċߎ�����?�?��ɕK�v��?��?
    // gh2aprxBsc3, gh2aprxCBsc3 (in gh2aprxBsc.c) �����?A
    //

    /*
     * �m�b�g��𓾂�
     *
     * @return        �m�b�g��
     */
    private double[] getKnotArray(int uik, double[] orig, int nSegments) {
        double[] knots = new double[uik];

        if (isClosed) {
            int i;
            int j;

            for (i = (this.degree - 1), j = (nSegments - 1); i >= 0;
                    i--, j--) {
                knots[i] = knots[i + 1] - (orig[j + 1] - orig[j]);
            }

            for (i = (this.degree + 1), j = 1; j < (nSegments + 1); i++, j++) {
                knots[i] = orig[j];
            }

            for (j = 0; j < this.degree; i++, j++) {
                knots[i] = knots[i - 1] + (orig[j + 1] - orig[j]);
            }
        } else {
            for (int i = 0; i < knots.length; i++) {
                knots[i] = orig[i];
            }
        }

        return knots;
    }

    /*
     * �m�b�g�̑�?d�x�𓾂�
     *
     * @return        �m�b�g�̑�?d�x
     */
    private int[] getKnotMultiplicities(int length) {
        int[] knotMultiplicities = new int[length];

        for (int i = 0; i < knotMultiplicities.length; i++) {
            knotMultiplicities[i] = 1;
        }

        if (!this.isClosed) {
            knotMultiplicities[0] = knotMultiplicities[length - 1] = this.degree +
                    1;
        }

        return knotMultiplicities;
    }

    /**
     * Bspline�̃m�b�g��𓾂�
     *
     * @param nsegs �Z�O�?���g?�
     * @param knots �m�b�g�̒l
     *
     * @return Bspline�̃m�b�g��
     *
     * @see BsplineKnot
     */
    protected BsplineKnot getKnotData(int nsegs, double[] knots) {
        int uicp;
        int uik;

        if (isClosed) {
            uicp = nsegs;
            uik = (2 * degree) + nsegs + 1;
        } else {
            uicp = nsegs + degree;
            uik = nsegs + 1;
        }

        knots = getKnotArray(uik, knots, nsegs);

        if (debug) {
            for (int i = 0; i < knots.length; i++) {
                System.err.println("knots[" + i + "] = " + knots[i]);
            }
        }

        // �m�b�g�̑�?d�x
        int[] knotMultiplicities = getKnotMultiplicities(knots.length);

        if (debug) {
            for (int i = 0; i < knotMultiplicities.length; i++) {
                System.err.println("knotMultiplicities[" + i + "] = " +
                    knotMultiplicities[i]);
            }
        }

        // get BsplineKnot
        return new BsplineKnot(degree, KnotType.UNSPECIFIED, isClosed, uik,
            knotMultiplicities, knots, uicp, GeometryElement.doCheckDebug);
    }

    /**
     * �ߎ��v�Z�̂��߂̃��R�r�A��?s���?�߂�
     *
     * @param knotData Bspline�̃m�b�g��
     *
     * @return �ߎ��v�Z�̂��߂̃��R�r�A��?s��
     *
     * @see BsplineKnot
     * @see Matrix
     */
    protected Matrix getDesignMatrix(BsplineKnot knotData) {
        int uicp = knotData.nControlPoints();
        int nseg = knotData.nSegments();
        int npnts = nPoints;
        Matrix designMatrix = new Matrix(npnts, uicp);
        double[] bcoef = new double[degree + 1];

        if (debug) {
            System.err.println("<start getDesignMatrix()>");
        }

        for (int i = 0; i < npnts; i++) {
            int cseg = knotData.evaluateBsplineFunction(params[i], bcoef);

            if (this.isClosed) {
                // ���Ă���?�?�
                int j;

                // ���Ă���?�?�
                int m;

                for (j = 0; j < cseg; j++) {
                    designMatrix.setElementAt(i, j, 0.0);
                }

                //for (int l = 0; l < degree; l++, j++) {
                for (int l = 0; l <= degree; l++, j++) {
                    m = j % uicp;
                    designMatrix.setElementAt(i, m, bcoef[l]);
                }

                for (; j < uicp; j++) {
                    designMatrix.setElementAt(i, j, 0.0);
                }
            } else {
                // �J���Ă���?�?�
                int j;

                // �J���Ă���?�?�
                int k;

                for (j = 0, k = 0; j < cseg; j++, k++) {
                    designMatrix.setElementAt(i, k, 0.0);
                }

                for (int l = 0; l <= degree; l++, j++, k++) {
                    designMatrix.setElementAt(i, k, bcoef[l]);
                }

                for (; j < uicp; j++, k++) {
                    designMatrix.setElementAt(i, k, 0.0);
                }
            }
        }

        if (debug) {
            for (int i = 0; i < designMatrix.getRowSize(); i++) {
                System.err.print("<" + designMatrix.getElementAt(i, 0));

                for (int j = 1; j < designMatrix.getColumnSize(); j++) {
                    System.err.print(", " + designMatrix.getElementAt(i, j));
                }

                System.err.println(">");
            }
        }

        return designMatrix;
    }
}
// end of file
