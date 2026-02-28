/*
 * ��?���v�f�Ƃ���?s���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Matrix.java,v 1.3 2007-10-21 21:08:14 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.MachineEpsilon;

/**
 * ��?���v�f�Ƃ���?s���\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ?s��̊e�v�f�̒l��܂ގ�?��̓񎟌��z�� elm[?s][��]
 * ��?��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:14 $
 */
//This class is not part of the public API, please don't inlcude it as part of the javadoc
class Matrix extends java.lang.Object implements java.lang.Cloneable {

    /**
     * LU �����?s�Ȃ�?ۂ̑Ίp�v�f�ׂ̈̃C�v�V�?�� (��?�) ?B
     * <p/>
     * ��?݂� 1.0e-8 ��?ݒ肳��Ă���?B
     * </p>
     *
     * @see #doLUDecompose()
     */
    static private final double epsilon4DiagonalElements = 1.0e-8;

    /**
     * ?s?� (number of rows) ?B
     */
    private int nRows;

    /**
     * ��?� (number of columns) ?B
     */
    private int nCols;

    /**
     * ?s���?\?�����v�f�̓񎟌��z�� ([?s][��]) ?B
     */
    private double elm[][];

    /**
     * ?s�̃s�{�b�g��?�Ԃ��z��?B
     * <p/>
     * ����?��͖{�N���X�̓Ք�ł̂ݗ��p��?A�O������͌����Ȃ�?B
     * </p>
     */
    private int pvt[];

    /**
     * LU ���Ⳃ�Ă��邩�ۂ����t���O?B
     * <p/>
     * ����?��͖{�N���X�̓Ք�ł̂ݗ��p��?A�O������͌����Ȃ�?B
     * </p>
     */
    private boolean LUDecomposed;

    /**
     * ����?s���R�s?[���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^�� private �ł���?A
     * ���̃N���X�̗��p�҂�?s���R�s?[������?�?��ɂ� copy �𗘗p����?B
     * </p>
     *
     * @param src �R�s?[����?s��
     * @see #copy()
     */
    private Matrix(Matrix src) {
        this.nRows = src.getRowSize();
        this.nCols = src.getColumnSize();
        this.elm = new double[this.nRows][this.nCols];
        this.pvt = new int[this.nRows];
        for (int i = 0; i < this.nRows; i++) {
            this.pvt[i] = src.pvt[i];
            for (int j = 0; j < this.nCols; j++)
                this.elm[this.pvt[i]][j] = src.elm[src.pvt[i]][j];
        }
        this.LUDecomposed = src.LUDecomposed;
    }

    /**
     * �w�肳�ꂽ?s?�/��?���?�I�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���ׂĂ̗v�f�̒l�� 0.0 ��?������?B
     * </p>
     *
     * @param r ?s?�
     * @param c ��?�
     */
    public Matrix(int r, int c) {
        this.nRows = r;
        this.nCols = c;
        this.elm = new double[this.nRows][this.nCols];
        this.pvt = new int[this.nRows];
        for (int i = 0; i < this.nRows; i++) {
            this.pvt[i] = i;
            for (int j = 0; j < this.nCols; j++)
                this.elm[this.pvt[i]][j] = 0.0;
        }
        this.LUDecomposed = false;
    }

    /**
     * ��?��̓񎟌��z���^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param values ?s��̊e�v�f�̒l��܂ޓ񎟌��z��
     */
    public Matrix(double[][] values) {
        this.nRows = values.length;
        this.nCols = values[0].length;
        this.elm = new double[this.nRows][this.nCols];
        this.pvt = new int[this.nRows];
        for (int i = 0; i < this.nRows; i++) {
            this.pvt[i] = i;
            for (int j = 0; j < this.nCols; j++)
                this.elm[this.pvt[i]][j] = values[i][j];
        }
        this.LUDecomposed = false;
    }

    /**
     * ����?s���?s?���Ԃ�?B
     *
     * @return ?s?�
     */
    public int getRowSize() {
        return this.nRows;
    }

    /**
     * ����?s��̗�?���Ԃ�?B
     *
     * @return ��?�
     */
    public int getColumnSize() {
        return this.nCols;
    }

    /**
     * ?s�񂪊�� LU ���Ⳃ�Ă���?A�v�f�̒l���?X���邱�Ƃ��ł��Ȃ����Ƃ���O�̓Ք�N���X?B
     */
    public class MatrixIsLUDecomposedException extends RuntimeException {
        /**
         * ?־��^�����ɃI�u�W�F�N�g��?\�z����?B
         */
        public MatrixIsLUDecomposedException() {
            super();
        }

        /**
         * ?־��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param s ?־
         */
        public MatrixIsLUDecomposedException(String s) {
            super(s);
        }
    }

    /**
     * �^����ꂽ�ꎟ���z��̊e�v�f�̒l��?A����?s��̎w���?s�̊e�v�f��?ݒ肷��?B
     * <p/>
     * elm[i][j] �� value[j] �����?B
     * </p>
     *
     * @param i     ?s�̔�?� (0 �x?[�X)
     * @param value �v�f�̒l��܂ވꎟ���z��
     * @throws MatrixIsLUDecomposedException ?s��� LU ���Ⳃ�Ă���̂ŕ�?X�ł��Ȃ�
     */
    public void setElementsAt(int i, double[] value) {
        if (LUDecomposed)
            throw new MatrixIsLUDecomposedException();

        for (int j = 0; j < nCols; j++)
            this.elm[this.pvt[i]][j] = value[j];
    }

    /**
     * �^����ꂽ�l��?A����?s��̎w���?s/��̗v�f��?ݒ肷��?B
     * <p/>
     * elm[i][j] �� value �����?B
     * </p>
     *
     * @param i     ?s�̔�?� (0 �x?[�X)
     * @param j     ��̔�?� (0 �x?[�X)
     * @param value �v�f�̒l
     * @throws MatrixIsLUDecomposedException ?s��� LU ���Ⳃ�Ă���̂ŕ�?X�ł��Ȃ�
     */
    public void setElementAt(int i, int j, double value) {
        if (LUDecomposed)
            throw new MatrixIsLUDecomposedException();

        this.elm[this.pvt[i]][j] = value;
    }

    /**
     * ����?s��̎w���?s/��̗v�f�̒l��Ԃ�?B
     * <p/>
     * elm[i][j] �̒l��Ԃ�?B
     * </p>
     *
     * @param i ?s�̔�?� (0 �x?[�X)
     * @param j ��̔�?� (0 �x?[�X)
     * @return �v�f�̒l
     */
    public double getElementAt(int i, int j) {
        return this.elm[this.pvt[i]][j];
    }

    /**
     * ����?s��̎w���?s/��̗v�f�̒l��Ԃ�?B
     * <p/>
     * elm[i][j] �̒l��Ԃ�?B
     * </p>
     *
     * @param i ?s�̔�?� (0 �x?[�X)
     * @param j ��̔�?� (0 �x?[�X)
     * @return �v�f�̒l
     */
    private double elm(int i, int j) {
        return this.elm[this.pvt[i]][j];
    }

    /**
     * ����?s��̕�?���Ԃ�?B
     *
     * @return ��?����ꂽ?s��?B
     */
    public Matrix copy() {
        return new Matrix(this);
    }

    /**
     * ����?s��̕�?���Ԃ�?B
     *
     * @return ��?����ꂽ?s��
     */
    public java.lang.Object clone() {
        return this.copy();
    }

    /**
     * ����?s��Ƒ���?s���?u�a?v��\��?s���Ԃ�?B
     * <p/>
     * this �� mate ��?s?��Ɨ�?��͂��ꂼ�ꓙ�����Ȃ���΂Ȃ�Ȃ�?B
     * this �� mate ��?s?����邢�͗�?����قȂ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param mate �a���鑊���?s��
     * @return ��?s��̘a (this + mate)
     * @see InvalidArgumentValueException
     */
    public Matrix add(Matrix mate) {
        if (!(this.nRows == mate.nRows) || !(this.nCols == mate.nCols)) {
            throw new InvalidArgumentValueException();
        }

        Matrix add = new Matrix(this.nRows, this.nCols);
        for (int i = 0; i < this.nRows; i++) {
            for (int j = 0; j < this.nCols; j++) {
                add.setElementAt(i, j,
                        this.getElementAt(i, j) +
                                mate.getElementAt(i, j));
            }
        }

        return add;
    }

    /**
     * ����?s��Ƒ���?s���?u?�?v��\��?s���Ԃ�?B
     * <p/>
     * this �� mate ��?s?��Ɨ�?��͂��ꂼ�ꓙ�����Ȃ���΂Ȃ�Ȃ�?B
     * this �� mate ��?s?����邢�͗�?����قȂ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param mate ?����鑊���?s��
     * @return ��?s���?� (this - mate)
     * @see InvalidArgumentValueException
     */
    public Matrix subtract(Matrix mate) {
        if (!(this.nRows == mate.nRows) || !(this.nCols == mate.nCols)) {
            throw new InvalidArgumentValueException();
        }

        Matrix sub = new Matrix(this.nRows, this.nCols);
        for (int i = 0; i < this.nRows; i++) {
            for (int j = 0; j < this.nCols; j++) {
                sub.setElementAt(i, j,
                        this.getElementAt(i, j) -
                                mate.getElementAt(i, j));
            }
        }

        return sub;
    }

    /**
     * ����?s��Ƒ���?s���?u?�?v��\��?s���Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����?s���
     * ?s?��� this ��?s?��ɓ�����?A
     * ��?��� mate �̗�?��ɓ�����?B
     * </p>
     * <p/>
     * this �̗�?��� mate ��?s?��͓������Ȃ���΂Ȃ�Ȃ�?B
     * this �Ɨ�?��� mate ��?s?����قȂ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param mate ?ς��鑊���?s��
     * @return ��?s���?� (this * mate)
     * @see InvalidArgumentValueException
     */
    public Matrix multiply(Matrix mate) {
        if (!(this.nCols == mate.nRows)) {
            throw new InvalidArgumentValueException();
        }

        Matrix multi = new Matrix(this.nRows, mate.nCols);
        for (int i = 0; i < this.nRows; i++) {
            double[] iRow = this.elm[i];
            for (int j = 0; j < mate.nCols; j++) {
                double value = 0.0;
                for (int k = 0; k < this.nCols; k++) {
                    value =
                            value + (iRow[k] * mate.getElementAt(k, j));
                }
                multi.setElementAt(i, j, value);
            }
        }

        return multi;
    }

    /**
     * ?s��?���łȂ����Ƃ���O�̓Ք�N���X?B
     */
    public class MatrixIsNotSquare extends RuntimeException {
        /**
         * ?־��^�����ɃI�u�W�F�N�g��?\�z����?B
         */
        public MatrixIsNotSquare() {
            super();
        }

        /**
         * ?־��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param s ?־
         */
        public MatrixIsNotSquare(String s) {
            super(s);
        }
    }

    /**
     * ����?s���?s�񎮂̒l��Ԃ�?B
     * <p/>
     * ����?s��?���?s��łȂ�?�?��ɂ�?A
     * MatrixIsNotSquare �̗�O��?�����?B
     * </p>
     *
     * @return ?s�񎮂̒l
     */
    public double determinant() {
        if (this.nRows != this.nCols)
            throw new MatrixIsNotSquare();

        int theSize = this.nRows; // = this.nCols;

        if (theSize == 2)
            return ((elm(0, 0) * elm(1, 1)) - (elm(0, 1) * elm(1, 0)));

        double result = 0.0;

        for (int i = 0; i < theSize; i++) {
            double valueP = 1.0;
            double valueM = 1.0;
            for (int j = 0; j < theSize; j++) {
                int k = (i + j) % theSize;
                valueP *= elm(k, j);
                valueM *= elm(((theSize - 1) - k), j);
            }
            result += valueP;
            result -= valueM;
        }

        return result;
    }

    /**
     * ����?s�� (N x N ?���?s��) �� LU ���ⷂ�?B
     * <p/>
     * this �� (�s�{�b�g?��?���) �Ίp�v�f��
     * {@link #epsilon4DiagonalElements epsilon4DiagonalElements}
     * ����?�������̂������B�?�?��ɂ�?A?��?��r���Œ��f��?Afalse ��Ԃ�?B
     * </p>
     * <p/>
     * this ��?���?s��łȂ�?�?��ɂ�?A
     * MatrixIsNotSquare �̗�O�𓊂���?B
     * </p>
     *
     * @return ?��?��?�?��?I��� true?A�����łȂ���� false
     * @see #makeLUDecomposition()
     */
    private boolean doLUDecompose() {
        if (this.nRows != this.nCols)
            throw new MatrixIsNotSquare();

        int theSize = this.nRows; // = this.nCols;

        for (int i = 0; i < theSize; i++) {
            /*
            * i ��ڂ�?ő�l��?��?s (maxIdx ?s) ��T��
            */
            int maxIdx = i;
            double maxVal = Math.abs(this.elm(maxIdx, i));
            for (int j = (i + 1); j < theSize; j++) {
                double jValue = Math.abs(this.elm(j, i));
                if (jValue > maxVal) {
                    maxIdx = j;
                    maxVal = jValue;
                }
            }

            /*
            * i ?s�ڂ� maxIdx ?s�ڂ��ꊷ����
            */
            if (maxIdx != i) {
                int pvtVal = this.pvt[i];
                this.pvt[i] = this.pvt[maxIdx];
                this.pvt[maxIdx] = pvtVal;
            }

            /*
            * ?������ۂ��𔻒f����
     E�l�Ƃ��� 1.0e-8 ��g��
            * ���̔��f�ɂ� (�o���I��) machineEpsilon �̒l��?���������
            */
            if (Math.abs(this.elm(i, i)) < Matrix.epsilon4DiagonalElements)
                return false;

            /*
            * Gauss ?K��@
            */
            this.setElementAt(i, i, 1.0 / this.elm(i, i));

            for (int j = (i + 1); j < theSize; j++) {
                this.setElementAt(j, i, (this.elm(j, i) * this.elm(i, i)));
                for (int k = (i + 1); k < theSize; k++)
                    this.setElementAt(j, k, (this.elm(j, k) - (this.elm(j, i) * this.elm(i, k))));
            }
        }

        return true;
    }

    /**
     * ����?s�� (N x N ?���?s��) �� LU ���ⵂ����ʂ�Ԃ�?B
     * <p/>
     * ?��?���� this �̑Ίp�v�f��
     * 1.0e-8 ����?������l��?��̂������B�?�?��ɂ�?Anull ��Ԃ�?B
     * ���� 1.0e-8 �Ƃ����l��?A
     * ��?݂͂��̃N���X�̓Ք�Œ�?��Ƃ���?ݒ肳��Ă���?A
     * ���̃N���X�̃\?[�X�v�?�O�������?W���Ȃ���?A��?X���邱�Ƃ͂ł��Ȃ�?B
     * </p>
     * <p/>
     * this ����� LU ���Ⳃꂽ��̂ł���?�?���?Athis ��Ԃ�?B
     * </p>
     * <p/>
     * this ��?���?s��łȂ�?�?��ɂ�?A
     * MatrixIsNotSquare �̗�O�𓊂���?B
     * </p>
     *
     * @return LU ����̌���
     */
    public Matrix makeLUDecomposition() {
        if (this.nRows != this.nCols)
            throw new MatrixIsNotSquare();

        if (this.LUDecomposed == true)
            return this;

        Matrix dst = this.copy();
        if (dst.doLUDecompose() != true) {
            return null;
        }
        dst.LUDecomposed = true;

        return dst;
    }

    /**
     * ����?s���?��ӂ̈ꎟ���̌W?� (A) �Ƃ���A���ꎟ��� AX = B ���?B
     * <p/>
     * ���̉E�� (B) �͈�?��Ƃ��ė^�����?A�� X ��Ԃ�?B
     * </p>
     * <p/>
     * this (A) ��?���?s��ł���?A�\�� LU ���Ⳃ�Ă��Ȃ���΂Ȃ�Ȃ�?B
     * </p>
     * <p/>
     * this ��?���?s��łȂ��B���?A�\�� LU ���Ⳃ�Ă��Ȃ�?�?��ɂ� null ��Ԃ�?B
     * </p>
     *
     * @param rightHandValues �A�����̉E�� (B)
     * @return �A�����̉� (X)
     * @see #makeLUDecomposition()
     * @see #solveSimultaneousLinearEquations(double[])
     */
    private double[] doSolveSimultaneousLinearEquations(double[] rightHandValues) {
        /*
        * the matrix should be LUDecomposed in advance
        */
        if (this.LUDecomposed != true) {
            return null;
        }

        int n = this.nRows;    // = this.nCols

        /*
        * size of the array of right hand values should be same as this
        */
        if (n != rightHandValues.length)
            throw new InvalidArgumentValueException();

        double[] result = new double[n];
        double theValue;    // work area

        /*
        * forward substitution
        */
        for (int i = 0; i < n; i++) {
            theValue = rightHandValues[this.pvt[i]];
            for (int j = 0; j < i; j++)
                theValue -= this.elm(i, j) * result[j];
            result[i] = theValue;
        }

        /*
        * backward substitution
        */
        for (int i = (n - 1); i >= 0; i--) {
            theValue = result[i];
            for (int j = (i + 1); j < n; j++)
                theValue -= this.elm(i, j) * result[j];
            result[i] = theValue * elm(i, i);
        }

        return result;
    }

    /**
     * ����?s���?��ӂ̈ꎟ���̌W?� (A) �Ƃ���A���ꎟ��� AX = B ���?B
     * <p/>
     * ���̉E�� (B) �͈�?��Ƃ��ė^�����?A�� X ��Ԃ�?B
     * </p>
     * <p/>
     * this (A) ��?���?s��łȂ���΂Ȃ�Ȃ�?B
     * </p>
     * <p/>
     * this ��?���?s��łȂ��B���?A�t?s���?���Ȃ�?�?��ɂ� null ��Ԃ�
     * </p>
     *
     * @param rightHandValues �A�����̉E�� (B)
     * @return �A�����̉� (X)
     */
    public double[] solveSimultaneousLinearEquations(double[] rightHandValues) {
        Matrix LUDecomp = this.makeLUDecomposition();

        if (LUDecomp == null) {
            return null;
        }

        return LUDecomp.doSolveSimultaneousLinearEquations(rightHandValues);
    }

    /**
     * �v��?s�� A �� QR ����̌��ʂ�\���Ք�N���X?B
     */
    private class QRDecomposition {
        private double[] rd;
        private double[] coef;
        private int rank;
        private int[] ip;    // null �̉\?�����
        private double cond;

        QRDecomposition(double[] rd, double[] coef, int[] ip, int rank, double cond) {
            this.rd = rd;
            this.coef = coef;
            this.ip = ip;
            this.rank = rank;
            this.cond = cond;
        }

        private double[] getDiagonalElementOfR() {
            return rd;
        }

        private double[] getCoefficent() {
            return coef;
        }

        private int[] getIndexVector() {
            return ip;
        }

        private int getApproximatedRankOfA() {
            return rank;
        }

        private double getCondition() {
            return cond;
        }
    }

    /**
     * ����?s�� (�v��?s�� A) �� Householder �@�ɂ�B� QR ���ⵂ����ʂ�Ԃ�?B
     * <p/>
     * �v��?s�� A �� A = Q * R �� Q, R �ɕ��ⷂ�?B
     * </p>
     *
     * @return QR ���� �̌���
     * @see #solveLinearLeastSquare(double[])
     * @see #solveQREquations(Matrix.QRDecomposition,double[])
     */
    private QRDecomposition doHouseHolderQRDecomposition() {
        /*
        * Note:
        *
        * the orthogonal matrix Q can be written as
        *	Q = Q(0) * Q(1) * ... * Q(m-1)
        * where
        *	Q(k) = I - c(k) * w(k) * w(transpose)(k).
        *
        * vector w(k) is stored in ip(k)-th column of A
        *
        *	A[i][ip(k)] = Q[i][ip(k)],	i = k, n - 1
        *	A[i][ip(k)] = R[i][ip(k)],	i = 0, k - 1
        *
        * the diagonal of R is stored in rd
        */
        int n = getRowSize();        // number of rows of A
        int m = getColumnSize();    // number of columns of A

        int[] ip = new int[m];
        double[] rd = new double[m];
        double[] g2 = new double[m];
        double[] coef = new double[m];
        int rank;
        double cond;
        double gmax = 0.0;
        double tmax = 0.0;
        int i, j, k, l;

        // MachineEpsilon.DOUBLE == 2.220446049250313E-16
        // �Ȃ̂ɂ���͕K�v��?H (Solaris7 for Intel, jdk1.1.7 ��?�?�)
        double my_minute = 1.0e-75;

        for (i = 0; i < m; i++) {
            ip[i] = i;
        }

        /*
        * Householder transformation with pivoting
        */
        for (k = 0; k < m; k++) {

            /*
            * find the pivot column
            */
            int kp = 0;
            gmax = 0.0;
            tmax = 0.0;
            for (j = k; j < m; j++) {
                double t = 0.0;
                for (i = k; i < n; i++) {
                    t += elm(i, ip[j]) * elm(i, ip[j]);
                }

                if (k == 0) {
                    g2[ip[j]] = t;
                    if (t > tmax) {
                        tmax = t;
                        kp = j;
                    }
                } else {
                    double gv = 0.0;
                    if (g2[ip[j]] != 0.0)
                        gv = t / g2[ip[j]];
                    if (gv > gmax) {
                        gmax = gv;
                        tmax = t;
                        kp = j;
                    }
                }
            }

            if (((k == 0) && (tmax < my_minute)) ||
                    ((k > 0) && (gmax < MachineEpsilon.DOUBLE))) {
                /*
                * rank deficiency
                */
                rank = k;
                cond = 1 / Math.sqrt(MachineEpsilon.DOUBLE);
                return new QRDecomposition(rd, coef, ip, rank, cond);
            }

            /*
            * column exchange
            */
            if (kp != k) {
                int kv = ip[k];
                ip[k] = ip[kp];
                ip[kp] = kv;
            }

            /*
            * Householder transformation
            */
            double s = Math.sqrt(tmax);
            if (elm(k, ip[k]) < 0.0) s = (-s);

            setElementAt(k, ip[k], elm(k, ip[k]) + s);
            coef[ip[k]] = 1.0 / (elm(k, ip[k]) * s);
            rd[ip[k]] = (-1.0 / s);

            for (j = k + 1; j < m; j++) {
                double t = 0.0;
                for (l = k; l < n; l++) {
                    t += elm(l, ip[k]) * elm(l, ip[j]);
                }
                t *= coef[ip[k]];

                setElementAt(k, ip[j], elm(k, ip[j]) - t * elm(k, ip[k]));
                for (i = k + 1; i < n; i++) {
                    setElementAt(i, ip[j], elm(i, ip[j]) - t * elm(i, ip[k]));
                }
            }
        }

        /*
        * full rank
        */
        rank = m;
        cond = 1 / Math.sqrt(gmax);
        return new QRDecomposition(rd, coef, ip, rank, cond);
    }

    /**
     * ?�`�̘A����� R * X = Q * B �� X ��?�߂�?B
     *
     * @param decomposition   QR ����̌���
     * @param rightHandValues �A�����̉E�� (B)
     * @return �A�����̉� (X)
     * @see #solveLinearLeastSquare(double[])
     * @see #doHouseHolderQRDecomposition()
     */
    private double[] solveQREquations(QRDecomposition decomposition,
                                      double[] rightHandValues) {
        double[] rd = decomposition.getDiagonalElementOfR();
        double[] coef = decomposition.getCoefficent();
        int[] ip = decomposition.getIndexVector();
        int rank = decomposition.getApproximatedRankOfA();

        int n = getRowSize();        // number of rows of A
        int m = getColumnSize();    // number of columns of A
        double[] solution = new double[m];

        /*
        * compute Q(transpose) * B
        */
        for (int k = 0; k < rank; k++) {
            double t = 0.0;
            for (int l = k; l < n; l++)
                t += elm(l, ip[k]) * rightHandValues[l];
            t *= coef[ip[k]];

            for (int i = k; i < n; i++)
                rightHandValues[i] -= t * elm(i, ip[k]);
        }

        /*
        * solve R * X = Q(transpose) * B
        */
        for (int k = (rank - 1); k >= 0; k--) {
            double t = rightHandValues[k];
            for (int j = (k + 1); j < rank; j++) {
                t -= elm(k, ip[j]) * solution[ip[j]];
            }
            solution[ip[k]] = t * rd[ip[k]];
        }

        if (rank != m) {
            /*
            * rank deficiency
            */
            for (int k = rank; k < m; k++)
                solution[ip[k]] = 0.0;
        }

        return solution;
    }

    /**
     * ?�?��?� (���m?���?��������?��̕���) �̘A���ꎟ��� AX = B
     * �ɑ΂���?�?���?�� X' ��\���Ք�N���X
     *
     * @see #solveLinearLeastSquare(double[])
     */
    public class LinearLeastSquareSolution {
        /**
         * ?s�� A �̊K?�
         */
        private int rank;

        /**
         * ?s�� A ��?�??�
         */
        private double condition;

        /**
         * ?�?���?��̔z�� (X')
         */
        private double[] solutions;

        /**
         * �e�t�B?[���h�̒l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param rank      ?s�� A �̊K?�
         * @param condition ?s�� A ��?�??�
         * @param solution  ?�?���?��̔z�� (X')
         */
        private LinearLeastSquareSolution(int rank, double condition, double[] solutions) {
            this.rank = rank;
            this.condition = condition;
            this.solutions = solutions;
        }

        /**
         * ?s�� A �̊K?���Ԃ�?B
         *
         * @return ?s�� A �̊K?�
         */
        public int rank() {
            return rank;
        }

        /**
         * ?s�� A ��?�??���Ԃ�?B
         *
         * @return ?s�� A ��?�??�
         */
        public double condition() {
            return condition;
        }

        /**
         * �w��̔�?���?�?���?���Ԃ�?B
         * <p/>
         * X'[i] ��Ԃ�?B
         * </p>
         *
         * @param i X' ��̃C���f�b�N�X
         * @return i �Ԗڂ�?�?���?��
         */
        public double solutionAt(int i) {
            return solutions[i];
        }

        /**
         * �w��̔�?���?�?���?�� X' ��Ԃ�?B
         *
         * @return ?�?���?��̔z�� (X')
         */
        public double[] solutions() {
            return (double[]) solutions.clone();
        }
    }

    /**
     * ����?s���?��ӂ̈ꎟ���̌W?� (A) �Ƃ���
     * ?�?��?� (���m?���?��������?��̕���) �̘A���ꎟ��� AX = B
     * �ɑ΂���?�?���?�� X' ��?�߂�?B
     * <p/>
     * ����?s��̗�?��� rightHandValues �̗v�f?�����v���Ȃ����?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param rightHandValues �A�����̉E�� (B)
     * @return �A������?�?���?�� (X')
     * @see InvalidArgumentValueException
     * @see #solveLinearLeastSquare2(double[])
     */
    public LinearLeastSquareSolution solveLinearLeastSquare(double[] rightHandValues) {
        /*
        * ���̃R�?���g��?A��Ƃ�Ƃ�?�� javadoc �p�̃R�?���g�Ȃ񂾂���?A
        * private �Ȃ�̂ւ̎Q?ƂȂ̂�?A�O���Ă���
        *
        * @see	QRDecomposition
        * @see	#doHouseHolderQRDecomposition()
        * @see	#solveQREquations(Matrix.QRDecomposition, double[])
        */

        if (this.nRows != rightHandValues.length) {
            throw new InvalidArgumentValueException();
        }

        Matrix me = this.copy();    // �v�f�̒l��?���������̂ŕK���R�s?[��?��

        QRDecomposition decomposition = me.doHouseHolderQRDecomposition();
        double[] solutions = me.solveQREquations(decomposition, rightHandValues);

        return new LinearLeastSquareSolution(decomposition.getApproximatedRankOfA(),
                decomposition.getCondition(), solutions);
    }

    /**
     * ����?s�� (�v��?s�� A) �� Householder �@�ɂ�B� QR ���ⵂ����ʂ�Ԃ� (�^�C�v 2) ?B
     * <p/>
     * �v��?s�� A �� A = Q * R �� Q, R �ɕ��ⷂ�?B
     * </p>
     *
     * @return QR ���� �̌���
     * @see #solveLinearLeastSquare2(double[])
     * @see #solveQREquations2(Matrix.QRDecomposition,double[])
     */
    private QRDecomposition doHouseHolderQRDecomposition2() {
        /*
        * Note:
        *
        * the orthogonal matrix Q can be written as
        *	Q = Q(0) * Q(1) * ... * Q(m-1)
        * where
        *	Q(k) = I - c(k) * w(k) * w(transpose)(k).
        *
        * vector w(k) is stored in ip(k)-th column of A
        *
        *	A[i][ip(k)] = Q[i][ip(k)],	i = k, n - 1
        *	A[i][ip(k)] = R[i][ip(k)],	i = 0, k - 1
        *
        * the diagonal of R is stored in rd
        */
        int n = getRowSize();        // number of rows of A
        int m = getColumnSize();    // number of columns of A

        double[] rd = new double[m];
        double[] coef = new double[m];
        double[] g2 = new double[m];
        int rank;
        double cond;
        double gmax = 0.0;
        double tmax = 0.0;
        int i, j, k, l;

        // MachineEpsilon.DOUBLE == 2.220446049250313E-16
        // �Ȃ̂ɂ���͕K�v��?H(Solaris7 for Intel, jdk1.1.7 ��?�?�)
        double my_minute = 1.0e-75;

        /*
        * Householder transformation WITHOUT pivoting
        */
        for (k = 0; k < m; k++) {
            /*
            * get (tmax)
            */
            gmax = 0.0;
            tmax = 0.0;
            if (k == 0) {
                for (j = k; j < m; j++) {
                    double t = 0.0;
                    for (i = k; i < n; i++) {
                        if (elm(i, j) != 0.0)
                            t += elm(i, j) * elm(i, j);
                    }
                    g2[j] = t;
                }
                tmax = g2[0];

            } else {
                double t = 0.0;
                for (i = k; i < n; i++)
                    if (elm(i, k) != 0.0)
                        t += elm(i, k) * elm(i, k);

                gmax = (g2[k] != 0.0) ? (t / g2[k]) : 0.0;
                tmax = t;
            }

            if ((k == 0) && (tmax < my_minute) ||
                    (k > 0) && (gmax < MachineEpsilon.DOUBLE)) {
                /***
                 * rank deficiency
                 */
                rank = k;
                cond = 1 / Math.sqrt(MachineEpsilon.DOUBLE);
                return new QRDecomposition(rd, coef, null, rank, cond);
            }

            /*
            * Householder transformation
            */
            double s = Math.sqrt(tmax);
            if (elm(k, k) < 0.0) s = (-s);

            setElementAt(k, k, elm(k, k) + s);
            coef[k] = 1.0 / (elm(k, k) * s);
            rd[k] = (-1.0 / s);

            for (j = k + 1; j < m; j++) {
                double t = 0.0;
                for (l = k; l < n; l++) {
                    if (elm(l, k) == 0.0)
                        break;
                    if (elm(l, j) != 0.0)
                        t += elm(l, k) * elm(l, j);
                }
                t *= coef[k];

                setElementAt(k, j, elm(k, j) - t * elm(k, k));
                for (i = k + 1; i < n; i++) {
                    if (elm(i, k) == 0.0)
                        break;
                    setElementAt(i, j, elm(i, j) - t * elm(i, k));
                }
            }
        }

        /*
        * full rank
        *
        */
        rank = m;
        cond = 1 / Math.sqrt(gmax);
        return new QRDecomposition(rd, coef, null, rank, cond);
    }

    /**
     * ?�`�̘A����� R * X = Q * B �� X ��?�߂� (�^�C�v 2) ?B
     *
     * @param decomposition   QR ����̌���
     * @param rightHandValues �A�����̉E�� (B)
     * @return �A�����̉� (X)
     * @see #solveLinearLeastSquare2(double[])
     * @see #doHouseHolderQRDecomposition2()
     */
    private double[] solveQREquations2(QRDecomposition decomposition,
                                       double[] rightHandValues) {
        double[] rd = decomposition.getDiagonalElementOfR();
        double[] coef = decomposition.getCoefficent();
        int rank = decomposition.getApproximatedRankOfA();

        int n = getRowSize();        // number of rows of A
        int m = getColumnSize();    // number of columns of A
        double[] solution = new double[m];

        /*
        * compute Q(transpose) * B
        */
        for (int k = 0; k < rank; k++) {
            double t = 0.0;
            for (int l = k; l < n; l++) {
                if (elm(l, k) != 0.0) {
                    t += elm(l, k) * rightHandValues[l];
                }
            }
            t *= coef[k];

            for (int i = k; i < n; i++) {
                if (elm(i, k) != 0.0) {
                    rightHandValues[i] -= t * elm(i, k);
                }
            }
        }

        /*
        * solve R * X = Q(transpose) * B
        */
        for (int k = (rank - 1); k >= 0; k--) {
            double t = rightHandValues[k];
            for (int j = (k + 1); j < rank; j++) {
                if (elm(k, j) != 0.0) {
                    t -= elm(k, j) * solution[j];
                }
            }
            solution[k] = t * rd[k];
        }

        if (rank != m) {
            /*
            * rank deficiency
            */
            for (int k = rank; k < m; k++) {
                solution[k] = 0.0;
            }
        }

        return solution;
    }

    /**
     * ����?s���?��ӂ̈ꎟ���̌W?� (A) �Ƃ���
     * ?�?��?� (���m?���?��������?��̕���) �̘A���ꎟ��� AX = B
     * �ɑ΂���?�?���?�� X' ��?�߂� (�^�C�v 2) ?B
     * <p/>
     * ����?s��̗�?��� rightHandValues �̗v�f?�����v���Ȃ����?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * ���̃?�\�b�h��?A��{�I�ɂ�
     * {@link #solveLinearLeastSquare(double[]) solveLinearLeastSquare(double[])}
     * �Ɠ��l��?��?��?s�Ȃ���?A?s�� A �̓�e�Ɉȉ���?���t���邱�Ƃ�?A
     * ?��?��?����ɂ����o?[�W�����ł���?B
     * <ul>
     * <li>�Ίp�ʒu�̗v�f�͔��ł��邱��?B
     * <li>�Ίp�ʒu���?����̗v�f elm[i][j] (i ��?s?Aj �͗�?Ai >= j) �Ɋւ���?A
     * <ul>
     * <li>e[i][j] �����ł���� -> e[i+1][j] �͉��ł�ǂ�
     * <li>e[i][j] ����ł����   -> e[i+1][j] �͗�łȂ���΂Ȃ�Ȃ�
     * </ul>
     * </ul>
     * ���̃?�\�b�h�̓Ք�ł�?A
     * this ������?�?�𖞂����Ă����̂Ƃ���?��?��?i�߂Ă���?A
     * this ������?�?�𖞂����Ă��Ȃ�?�?��ɂ͗\��ł��Ȃ����ʂ�?���?B
     * </p>
     *
     * @param rightHandValues �A�����̉E�� (B)
     * @return �A�����̉� (X)
     * @see InvalidArgumentValueException
     * @see #solveLinearLeastSquare(double[])
     */
    public LinearLeastSquareSolution solveLinearLeastSquare2(double[] rightHandValues) {
        /*
        * ���̃R�?���g��?A��Ƃ�Ƃ�?�� javadoc �p�̃R�?���g�Ȃ񂾂���?A
        * private �Ȃ�̂ւ̎Q?ƂȂ̂�?A�O���Ă���
        *
        * @see	QRDecomposition
        * @see	#doHouseHolderQRDecomposition2()
        * @see	#solveQREquations2(Matrix.QRDecomposition, double[])
        */
        int n = getRowSize();
        int m = getColumnSize();

        if (n != rightHandValues.length) {
            throw new InvalidArgumentValueException();
        }

        int i, j, k;
        for (i = 0; i < n; i++)
            if (elm(i, 0) == 0.0)
                break;

        for (; i < n; i++)
            if (elm(i, 0) != 0.0)
                break;

        Matrix me;
        if (i < n) {
            Matrix copy = new Matrix(n, m);
            double[] rhsv2 = new double[n];
            for (j = 0; j < n; j++, i++) {
                if (i >= n)
                    i = 0;
                for (k = 0; k < m; k++)
                    copy.setElementAt(j, k, elm(i, k));
                rhsv2[j] = rightHandValues[i];
            }
            me = copy;
            rightHandValues = rhsv2;
        } else {
            me = this.copy();    // �v�f�̒l��?���������̂ŕK���R�s?[��?��
        }

        QRDecomposition decomposition =
                me.doHouseHolderQRDecomposition2();
        double[] solutions =
                me.solveQREquations2(decomposition, rightHandValues);

        return new LinearLeastSquareSolution(decomposition.getApproximatedRankOfA(),
                decomposition.getCondition(), solutions);
    }

    // for Debug
    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     */
    public static void main(String argv[]) {
        /*
        Matrix matrix = new Matrix(3, 3);
        double[] m0  = { 3,   4,  7};
        double[] m1  = {-2,   3, 19};
        double[] m2  = { 5, -10,  6};

        matrix.setElementsAt(0, m0);
        matrix.setElementsAt(1, m1);
        matrix.setElementsAt(2, m2);

        */
        double[] m0 = {3, 4, 7};
        double[] m1 = {-2, 3, 19};
        double[] m2 = {5, -10, 6};
        double[][] mmm = {m0, m1, m2};
        Matrix matrix = new Matrix(mmm);

        // ?s�񓯎m�̉NZ,���Z,?�Z�̃e�X�g
        Matrix add = matrix.add(matrix);
        Matrix sub = matrix.subtract(matrix);
        Matrix multi = matrix.multiply(matrix);

        System.out.println("\n[matrix + matrix]");
        for (int i = 0; i < 3; i++) {
            System.out.println(i + "th row : (" +
                    add.getElementAt(i, 0) + ", " +
                    add.getElementAt(i, 1) + ", " +
                    add.getElementAt(i, 2) + ")");
        }
        System.out.println("\n[matrix - matrix]");
        for (int i = 0; i < 3; i++) {
            System.out.println(i + "th row : (" +
                    sub.getElementAt(i, 0) + ", " +
                    sub.getElementAt(i, 1) + ", " +
                    sub.getElementAt(i, 2) + ")");
        }
        System.out.println("\n[matrix * matrix]");
        for (int i = 0; i < 3; i++) {
            System.out.println(i + "th row : (" +
                    multi.getElementAt(i, 0) + ", " +
                    multi.getElementAt(i, 1) + ", " +
                    multi.getElementAt(i, 2) + ")");
        }
        System.out.println();

        // �A���ꎟ���̃e�X�g
        double[] rrr = {3, -4, 8};
        double[] result = matrix.solveSimultaneousLinearEquations(rrr);
        for (int i = 0; i < 3; i++) {
            System.out.println("Equations solving result : " +
                    result[i] + ", value : " +
                    ((matrix.getElementAt(i, 0) * result[0]) +
                            (matrix.getElementAt(i, 1) * result[1]) +
                            (matrix.getElementAt(i, 2) * result[2]) -
                            rrr[i]));
        }

        // ?�?���?�@�̃e�X�g
        LinearLeastSquareSolution result1 = matrix.solveLinearLeastSquare(rrr);
        System.out.println("LinearLeastSquare result : {" +
                result1.solutionAt(0) + ", " +
                result1.solutionAt(1) + ", " +
                result1.solutionAt(2) + "}");

        LinearLeastSquareSolution result2 = matrix.solveLinearLeastSquare2(rrr);
        System.out.println("LinearLeastSquare2 result : {" +
                result2.solutionAt(0) + ", " +
                result2.solutionAt(1) + ", " +
                result2.solutionAt(2) + "}");
    }
}

/* end of file */
