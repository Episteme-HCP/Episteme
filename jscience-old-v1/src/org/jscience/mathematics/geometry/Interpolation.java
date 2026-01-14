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
 * ��ԑS�ʂɋ��ʂ�?���ێ?����N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:13 $
 */

final class Interpolation {
    /**
     * ��Ԃ���_��ɑΉ�����p���??[�^
     */
    final double[] params;

    /**
     * ��Ԃ���_���?�
     */
    final int uip;

    /**
     * �p���??[�^�Ԋu
     * <p/>
     * �J����?�?�-1�I���W��?A����?�?�-2�I���W���ƂȂ邽��?A
     * ��?ڃA�N�Z�X���Ȃ�����(pInt()�?�\�b�h��g��)
     */
    private double[] pInt = null;

    /**
     * ?s��
     *
     * @see Matrix
     */
    final Matrix matrix;

    /**
     * �_�񂪕��Ă��邩�ǂ����̃t���O
     */
    final boolean isClosed;

    /**
     * �p���??[�^��^���ăI�u�W�F�N�g��?\�z����
     * (�_��͊J���Ă����̂Ƃ���)
     *
     * @param params �p���??[�^
     */
    Interpolation(double[] params) {
        if (params.length < 2)
            throw new InvalidArgumentValueException();

        this.params = params;
        this.isClosed = false;
        this.uip = params.length;
        this.pInt = getInterval();
        this.matrix = setupLinearSystem();
        computeLeftSideLinearSystem();
    }

    /**
     * �p���??[�^�Ɠ_�񂪕��Ă��邩�ǂ�����?���^���ăI�u�W�F�N�g��?\�z����
     *
     * @param params   �p���??[�^
     * @param isClosed �_�񂪕��Ă��邩�ǂ����̃t���O
     */
    Interpolation(double[] params, boolean isClosed) {
        if (params.length < 2)
            throw new InvalidArgumentValueException();

        this.params = params;
        this.isClosed = isClosed;
        if (this.isClosed) {
            this.uip = params.length - 1;
        } else {
            this.uip = params.length;
        }
        this.pInt = getInterval();
        if (!isClosed) {
            this.matrix = setupLinearSystem();
            computeLeftSideLinearSystem();
        } else {
            this.matrix = setupLinearSystemClosed();
        }
    }

    /**
     * �p���??[�^�Ԋu��?�߂�
     *
     * @param i �C���f�b�N�X
     * @return �p���??[�^�Ԋu�̒l
     */
    double pInt(int i) {
        if (!isClosed)
            return pInt[i + 1];
        else
            return pInt[i + 2];
    }

    /**
     * �p���??[�^�Ԋu�̒l��?ݒ肷��
     *
     * @param i     �C���f�b�N�X
     * @param value ?ݒ肷��l
     */
    private void pInt(int i, double value) {
        if (!isClosed)
            pInt[i + 1] = value;
        else
            pInt[i + 2] = value;
    }

    /**
     * ��Ԃ���_��̃p���??[�^�Ԋu��?�߂�
     *
     * @return �p���??[�^�̊Ԋu
     */
    private double[] getInterval() {
        if (!isClosed) {
            pInt = new double[uip + 1];
            pInt(-1, 0.0);
            for (int i = 0; i < uip - 1; i++) {
                pInt(i, params[i + 1] - params[i]);
            }
            pInt(uip - 1, 0.0);
        } else {
            pInt = new double[uip + 3];
            int i;
            for (i = 0; i < uip; i++) {
                pInt(i, params[i + 1] - params[i]);
            }
            pInt(i, pInt(0));
            pInt(-2, pInt(uip - 2));
            pInt(-1, pInt(uip - 1));
        }
        return pInt;
    }

    /**
     * ?s���?��(�J�����_���?�?�)
     *
     * @return ?s��
     * @see Matrix
     */
    private Matrix setupLinearSystem() {
        Matrix matrix = new Matrix(uip, 3);

        // 0th row
        double[] firstRow = {0.0, 1.0, 0.0};
        matrix.setElementsAt(0, firstRow);

        // 1st, (uip - 2)th row
        double denomJ = pInt(-1) + pInt(0) + pInt(1);
        for (int j = 1; j < uip - 1; j++) {
            double denomJ1 = pInt(j - 1) + pInt(j) + pInt(j + 1);
            double[] value = {
                    (pInt(j) * pInt(j)) / denomJ, // alpha
                    ((pInt(j) * (pInt(j - 2) + pInt(j - 1))) / denomJ) + // beta
                            ((pInt(j - 1) * (pInt(j) + pInt(j + 1))) / denomJ1),
                    (pInt(j - 1) * pInt(j - 1)) / denomJ1                // gamma
            };
            matrix.setElementsAt(j, value);
            denomJ = denomJ1;
        }

        // (uip - 1)th row
        double[] lastRow = {0.0, 1.0, 0.0};
        matrix.setElementsAt(uip - 1, lastRow);

        return matrix;
    }

    /**
     * ?s���?��(�����_���?�?�)
     *
     * @return ?s��
     * @see Matrix
     */
    private Matrix setupLinearSystemClosed() {
        Matrix matrix = new Matrix(uip, uip);

        for (int j = 0; j < uip; j++) {
            for (int k = 0; k < uip; k++) {
                matrix.setElementAt(j, k, 0.0);
            }
        }

        double denomJ = pInt(-2) + pInt(-1) + pInt(0);
        for (int j = 0; j < uip; j++) {
            double denomJ1 = pInt(j - 1) + pInt(j) + pInt(j + 1);
            int alpha = (j == 0) ? (uip - 1) : j - 1;
            int beta = j;
            int gamma = (j == (uip - 1)) ? 0 : j + 1;
            matrix.setElementAt(j, alpha,
                    (pInt(j) * pInt(j)) / denomJ);
            matrix.setElementAt(j, beta,
                    ((pInt(j) * (pInt(j - 2) + pInt(j - 1))) / denomJ) +
                            ((pInt(j - 1) * (pInt(j) + pInt(j + 1))) / denomJ1));
            matrix.setElementAt(j, gamma, (pInt(j - 1) * pInt(j - 1)) / denomJ1);
            denomJ = denomJ1;
        }

        matrix.makeLUDecomposition();
        return matrix;
    }

    /**
     * ?s���v�Z����
     */
    private void computeLeftSideLinearSystem() {
        for (int i = 1; i < uip; i++) {
            double val0 = matrix.getElementAt(i, 0);
            double val1 = matrix.getElementAt(i - 1, 1);
            double val2 = matrix.getElementAt(i - 1, 2);
            double value = matrix.getElementAt(i, 1);
            //double value0 = val0 / val1 * val2;
            //value -= value0;
            double value0 = val0 / val1;
            value -= value0 * val2;
            matrix.setElementAt(i, 0, value0);
            matrix.setElementAt(i, 1, value);
        }
        for (int i = uip - 2; i >= 0; i--) {
            double value0 = matrix.getElementAt(i, 2);
            double value1 = matrix.getElementAt(i + 1, 1);
            double value = value0 / value1;
            matrix.setElementAt(i, 2, value);
        }
    }

    /**
     * ��Ԃ�����?�̃m�b�g?���?�߂�(�J�����_��)
     *
     * @return �m�b�g?��
     * @see BsplineKnot
     */
    private BsplineKnot knotDataOpened() {
        int uik = uip;
        double[] knots = new double[uik];
        int[] knotMultiplicities = new int[uik];

        knots[0] = params[0];
        knotMultiplicities[0] = 4;
        int i;
        for (i = 1; i < uik - 1; i++) {
            knots[i] = params[i];
            knotMultiplicities[i] = 1;
        }
        knots[i] = params[i];
        knotMultiplicities[i] = 4;

        return new BsplineKnot(3, KnotType.UNSPECIFIED, isClosed,
                uik, knotMultiplicities, knots, uip + 2);
    }

    /**
     * ��Ԃ�����?�̃m�b�g?���?�߂�(�����_��)
     *
     * @return �m�b�g?��
     * @see BsplineKnot
     */
    private BsplineKnot knotDataClosed() {
        int degree = 3;
        int uik = (2 * degree) + uip + 1;
        double[] knots = new double[uik];
        int[] knotMultiplicities = new int[uik];

        knots[degree] = params[0];
        knotMultiplicities[degree] = 1;

        int i, j;
        for (i = degree - 1, j = uip - 1; i >= 0; i--, j--) {
            knots[i] = knots[i + 1] - pInt(j);
            knotMultiplicities[i] = 1;
        }

        for (i = degree + 1, j = 1; j < uip + 1; i++, j++) {
            knots[i] = params[j];
            knotMultiplicities[i] = 1;
        }

        for (j = 0; j < degree; i++, j++) {
            knots[i] = knots[i - 1] + pInt(j);
            knotMultiplicities[i] = 1;
        }

        return new BsplineKnot(3, KnotType.UNSPECIFIED, isClosed, uik,
                knotMultiplicities, knots, uip);
    }

    /**
     * ��Ԃ�����?�̃m�b�g?���?�߂�
     *
     * @return �m�b�g?��
     * @see BsplineKnot
     */
    BsplineKnot knotData() {
        if (!isClosed) {
            return knotDataOpened();
        } else {
            return knotDataClosed();
        }
    }

    /**
     * ��Ԃ�����?��?���_��?���Ԃ�
     *
     * @return ?���_��?�
     */
    int nControlPoints() {
        if (isClosed)
            return uip;
        else
            return uip + 2;
    }
}

// end of file
