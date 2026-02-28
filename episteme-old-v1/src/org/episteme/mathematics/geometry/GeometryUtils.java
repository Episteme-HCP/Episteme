/*
 * ?�?��w�I�ȉ��Z��?s���e��� static �?�\�b�h��?�N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: GeometryUtils.java,v 1.3 2007-10-21 21:08:12 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.mathematics.MachineEpsilon;
import org.episteme.mathematics.analysis.PrimitiveMapping;
import org.episteme.mathematics.analysis.PrimitiveMappingND;
import org.episteme.mathematics.analysis.polynomials.DoublePolynomial;

/**
 * ?�?��w�I�ȉ��Z��?s���e��� static �?�\�b�h��?�N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:12 $
 */

public class GeometryUtils extends java.lang.Object {
    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�
     */
    private GeometryUtils() {
    }

    /**
     * ��?� (2 * ��) ?B
     */
    static final double PI2 = Math.PI * 2.0;

    /**
     * ���?��̎�?���?��̒�?ϕ��p�̒�?�?B
     */
    private static final int maxNumberOfDividing = 608;

    /**
     * ���?��̎�?���?��̒�?ϕ��?�\�b�h
     * {@link #getDefiniteIntegral(PrimitiveMapping,ParameterSection,double)
     * getDefiniteIntegral(PrimitiveMapping, ParameterSection, double)}
     * �Ք�ŗ��p�����?�?B
     */
    private static final int powerNumber = 6;

    /**
     * ���?��̎�?���?��̒�?ϕ��?�\�b�h
     * {@link #getDefiniteIntegral(PrimitiveMapping,ParameterSection,double)
     * getDefiniteIntegral(PrimitiveMapping, ParameterSection, double)}
     * �Ք�ŗ��p�����?�?B
     */
    private static final double minimumTolerance = 1.0e-32;

    /**
     * ���?��̎�?���?��̒�?ϕ��?�\�b�h
     * {@link #getDefiniteIntegral(PrimitiveMapping,ParameterSection,double)
     * getDefiniteIntegral(PrimitiveMapping, ParameterSection, double)}
     * �Ք�ŗ��p�����?�?B
     */
    private static final double zeroDividePoint = 0.0;

    /**
     * ���?��̎�?���?��̒�?ϕ��?�\�b�h
     * {@link #getDefiniteIntegral(PrimitiveMapping,ParameterSection,double)
     * getDefiniteIntegral(PrimitiveMapping, ParameterSection, double)}
     * �Ք�ŗ��p�����?�?B
     */
    private static final double zeroWeight = Math.PI / 2.0;

    /**
     * ���?��̎�?���?��̒�?ϕ��?�\�b�h
     * {@link #getDefiniteIntegral(PrimitiveMapping,ParameterSection,double)
     * getDefiniteIntegral(PrimitiveMapping, ParameterSection, double)}
     * �Ք�ŗ��p�����?�?B
     */
    private static final double[] minusDividePoints = new double[maxNumberOfDividing];

    /**
     * ���?��̎�?���?��̒�?ϕ��?�\�b�h
     * {@link #getDefiniteIntegral(PrimitiveMapping,ParameterSection,double)
     * getDefiniteIntegral(PrimitiveMapping, ParameterSection, double)}
     * �Ք�ŗ��p�����?�?B
     */
    private static final double[] plusDividePoints = new double[maxNumberOfDividing];

    /**
     * ���?��̎�?���?��̒�?ϕ��?�\�b�h
     * {@link #getDefiniteIntegral(PrimitiveMapping,ParameterSection,double)
     * getDefiniteIntegral(PrimitiveMapping, ParameterSection, double)}
     * �Ք�ŗ��p�����?�?B
     */
    private static final double[] weights = new double[maxNumberOfDividing];

    /**
     * ���?��̎�?���?��̒�?ϕ��?�\�b�h
     * {@link #getDefiniteIntegral(PrimitiveMapping,ParameterSection,double)
     * getDefiniteIntegral(PrimitiveMapping, ParameterSection, double)}
     * �Ք�ŗ��p�����?�?B
     */
    private static boolean alreadyPrepared = false;

    /**
     * ���?��̎�?���?��̒�?ϕ��?�\�b�h
     * {@link #getDefiniteIntegral(PrimitiveMapping,ParameterSection,double)
     * getDefiniteIntegral(PrimitiveMapping, ParameterSection, double)}
     * �Ք�ŗ��p�����?���ێ?���� static �t�B?[���h�ɒl��?ݒ肷��?B
     */
    private static void prepareDefiniteIntegralConstants() {
        if (alreadyPrepared == true)
            return;

        alreadyPrepared = true;

        double almostOne = 0.9999999999999999;
        double halfPi = Math.PI / 2.0;
        double eeh = Math.exp(1.0 / Math.pow(2.0, (double) (powerNumber + 1)));

        double een = 1.0;
        double eenI;
        double esh;
        double ech;
        double exs;
        double exsI;
        double echsi;

        for (int i = 0; i < maxNumberOfDividing; i++) {
            een *= eeh;
            eenI = 1.0 / een;
            esh = (een - eenI) / 2.0;
            ech = (een + eenI) / 2.0;
            exs = Math.exp(halfPi * esh);
            exsI = 1.0 / exs;
            echsi = 2.0 / (exs + exsI);

            plusDividePoints[i] = ((exs - exsI) / 2.0) * echsi;
            if (plusDividePoints[i] >= almostOne)
                plusDividePoints[i] = almostOne;
            minusDividePoints[i] = -plusDividePoints[i];
            weights[i] = halfPi * ech * Math.pow(echsi, 2.0);
        }
    }

    /**
     * ���?��̎�?���?��̒�?ϕ���?�߂�?B
     * <p/>
     * �^����ꂽ��?ϕ���?� func ��?ϕ���� parameterSection �ɑ΂����?ϕ��l��Ԃ�?B
     * </p>
     * <p/>
     * ���e��?��l tolerance �ɑ΂�?A
     * ��?d�w?���?��^?��l?ϕ���ɂ��?A
     * ���̋��e��?���̌�?�����?ϕ��̋ߎ��l��Ԃ�?B
     * </p>
     * <p/>
     * parameterSection �̑?���l�͕��ł�?\��Ȃ���?A
     * parameterSection �̕\����Ԃ� func �̒�`���Ɏ�܂BĂ���K�v������?B
     * </p>
     * <p/>
     * tolerance ��?A����?�Βl�𗘗p����?B
     * </p>
     *
     * @param func             ���?��̎�?���?�
     * @param parameterSection ��?ϕ��͈̔�
     * @param tolerance        ?ϕ����ʂɑ΂���?�Ό�?��̋��e�l
     * @return ��?ϕ��l
     */
    public static double getDefiniteIntegral(PrimitiveMapping func,
                                             ParameterSection parameterSection,
                                             double tolerance) {
        prepareDefiniteIntegralConstants();
        tolerance = (Math.abs(tolerance) > minimumTolerance) ? Math.abs(tolerance) : minimumTolerance;

        /*
        * local variables
        */
        double iterationTol = 0.2 * Math.sqrt(tolerance);    /* tolerance for iteration */
        double halfDiff = (parameterSection.upper() - parameterSection.lower()) / 2.0;
        double midParam = (parameterSection.upper() + parameterSection.lower()) / 2.0;
        double meshSize = 0.5;
        int startIndex = (int) Math.pow((double) 2, (double) powerNumber);
        int indexInterval = startIndex;
        double minusCutoff;        /* cut off value of minus side */
        double plusCutoff;        /* cut off value of plus side */
        int minusCutoffNumber = 0;    /* number for cut off of minus side */
        int plusCutoffNumber = 0;    /* number for cut off of plus side */
        int commonCutoffNumber;        /* common number for cut off */
        int minusCutoffFlag = 0;    /* flag for cut off of minus side */
        int plusCutoffFlag = 0;        /* flag for cut off of plus side */
        double lastIntegral;        /* last integral */
        double currentIntegral;        /* current integral */
        int i, j;            /* loop counter */

        /*
        * initial step : integrate with mesh size 0.5 and check decay of integrand
        */
        currentIntegral = func.map(zeroDividePoint * halfDiff + midParam) * zeroWeight;

        for (i = startIndex - 1; i < maxNumberOfDividing; i += indexInterval) {
            if (minusCutoffFlag < 2) {
                minusCutoff = func.map(minusDividePoints[i] * halfDiff + midParam) * weights[i];
                currentIntegral += minusCutoff;
                if (Math.abs(minusCutoff) <= tolerance) {
                    if (++minusCutoffFlag >= 2)
                        minusCutoffNumber = (i + 1) - indexInterval;
                } else {
                    minusCutoffFlag = 0;
                }
            }

            if (plusCutoffFlag < 2) {
                plusCutoff = func.map(plusDividePoints[i] * halfDiff + midParam) * weights[i];
                currentIntegral += plusCutoff;
                if (Math.abs(plusCutoff) <= tolerance) {
                    if (++plusCutoffFlag >= 2)
                        plusCutoffNumber = (i + 1) - indexInterval;
                } else {
                    plusCutoffFlag = 0;
                }
            }

            if ((minusCutoffFlag == 2) &&
                    (plusCutoffFlag == 2))
                break;
        }

        if (minusCutoffNumber == 0)
            minusCutoffNumber = maxNumberOfDividing;

        if (plusCutoffNumber == 0)
            plusCutoffNumber = maxNumberOfDividing;

        /*
        * general step
        */
        lastIntegral = meshSize * halfDiff * currentIntegral;
        commonCutoffNumber
                = (minusCutoffNumber < plusCutoffNumber) ? minusCutoffNumber : plusCutoffNumber;

        for (i = 0; i < powerNumber; i++) {
            currentIntegral = 0.0;
            indexInterval = startIndex;
            startIndex /= 2;

            for (j = startIndex - 1;
                 j < commonCutoffNumber;
                 j += indexInterval) {
                currentIntegral
                        += (func.map(minusDividePoints[j] * halfDiff + midParam) +
                        func.map(plusDividePoints[j] * halfDiff + midParam)) * weights[j];
            }

            if (minusCutoffNumber > commonCutoffNumber) {
                for (j = commonCutoffNumber + indexInterval - 1;
                     j < minusCutoffNumber;
                     j += indexInterval) {
                    currentIntegral
                            += func.map(minusDividePoints[j] * halfDiff + midParam) * weights[j];
                }
            }

            if (plusCutoffNumber > commonCutoffNumber) {
                for (j = commonCutoffNumber + indexInterval - 1;
                     j < plusCutoffNumber;
                     j += indexInterval) {
                    currentIntegral
                            += func.map(plusDividePoints[j] * halfDiff + midParam) * weights[j];
                }
            }

            currentIntegral = (lastIntegral + meshSize * halfDiff * currentIntegral) / 2.0;

            /*
            * converged!
            */
            if (Math.abs(currentIntegral - lastIntegral) < iterationTol) {
                return currentIntegral;
            }

            meshSize /= 2.0;
            lastIntegral = currentIntegral;
        }

        /*
        * not converged
        */
        return currentIntegral;
    }

    /**
     * �����?��l��?����̕�?��𑼂̎�?��l�̂����?��킹��?B
     * <p/>
     * a ��?����̕�?��� b �̂����?��킹���l��Ԃ�?B
     * </p>
     *
     * @param a ��?�
     * @param b ��?�
     * @return b �̕�?���?��킹�� a
     */
    public static double copySign(double a, double b) {
        a = Math.abs(a);
        return (b < 0.0) ? (-a) : a;
    }

    /**
     * ��?�`�̘A������j��?[�g���@�ɂ����Z�ŉ�?B
     * <p/>
     * n �̖��m?� (x0, ..., xm), (m = n - 1) �ɑ΂���
     * ��?�`�̘A����� Fi(x0, ..., xm) = 0, (i = 0, ..., m) ���?B
     * </p>
     * <p/>
     * func �� Fi(x0, ..., xm), (i = 0, ..., m) �̒l��Ԃ�
     * n ��?� (x0, ..., xm) �̊�?���?A
     * n �̒l (F0, ..., Fm) ��Ԃ�?B
     * </p>
     * <p/>
     * derivatives[i] �� Fi(x0, ..., xm) �̕Δ� dFi/dxj, (j = 0, ..., m) ��Ԃ�
     * n ��?� (x0, ..., xm) �̊�?���?A
     * n �̒l (dFi/dx0, ..., dFi/dxm) ��Ԃ�?B
     * </p>
     * <p/>
     * convergence ��?An �̉⪎��ł��邩�ۂ��𔻒f����
     * n ��?� (x0, ..., xm) �̊�?���?A
     * n �̉� (x0, ..., xm) �ŘA���������Ă���� true?A
     * �����łȂ���� false ��Ԃ�?B
     * </p>
     *
     * @param func           n �̖��m?� x ��܂ޘA����� Fi(x) = 0 ��?��Ӓl (F0, ..., Fm) ��Ԃ���?�
     * @param derivatives    Fi �̕Δ�l (dFi/dx0, ..., dFi/dxm) ��Ԃ���?��̔z��
     * @param convergence    n �̉� (x0, ..., xm) �����ł��邩�ۂ��𔻒f�����?�
     * @param initialGuesses n �̉� (x0, ..., xm) ��?���l�̔z��
     * @return �A�����̎�� (x0, ..., xm) �̔z��
     * @see #solveSimultaneousEquationsWithCorrection(PrimitiveMappingND,PrimitiveMappingND[],PrimitiveBooleanMappingNDTo1D,PrimitiveMappingND,double[])
     */
    public static double[] solveSimultaneousEquations(PrimitiveMappingND func,
                                                      PrimitiveMappingND[] derivatives,
                                                      PrimitiveBooleanMappingNDTo1D convergence,
                                                      double[] initialGuesses) {
        return solveSimultaneousEquationsWithCorrection(func, derivatives, convergence, null,
                initialGuesses);
    }

    /**
     * ��?�`�̘A������j��?[�g���@�ɂ����Z�ŉ� (���Z�r���ł̉�̕�?��@�\�t��) ?B
     * <p/>
     * n �̖��m?� (x0, ..., xm), (m = n - 1) �ɑ΂���
     * ��?�`�̘A����� Fi(x0, ..., xm) = 0, (i = 0, ..., m) ���?B
     * </p>
     * <p/>
     * func �� Fi(x0, ..., xm), (i = 0, ..., m) �̒l��Ԃ�
     * n ��?� (x0, ..., xm) �̊�?���?A
     * n �̒l (F0, ..., Fm) ��Ԃ�?B
     * </p>
     * <p/>
     * derivatives[i] �� Fi(x0, ..., xm) �̕Δ� dFi/dxj, (j = 0, ..., m) ��Ԃ�
     * n ��?� (x0, ..., xm) �̊�?���?A
     * n �̒l (dFi/dx0, ..., dFi/dxm) ��Ԃ�?B
     * </p>
     * <p/>
     * convergence ��?An �̉⪎��ł��邩�ۂ��𔻒f����
     * n ��?� (x0, ..., xm) �̊�?���?A
     * n �̉� (x0, ..., xm) �ŘA���������Ă���� true?A
     * �����łȂ���� false ��Ԃ�?B
     * </p>
     * <p/>
     * correct ��?A���Z�̓r���� n �̉� (x0, ..., xm) �̒l��?��I��?C?������?���?A
     * ?C?���� n �̉� (x0, ..., xm) ��Ԃ�?B
     * correct ��?A���Z�̃�?[�v�ɂ����� convergence �̌Ă�?o���̑O�ɌĂ�?o�����?B
     * </p>
     *
     * @param func           n �̖��m?� x ��܂ޘA����� Fi(x) = 0 ��?��Ӓl (F0, ..., Fm) ��Ԃ���?�
     * @param derivatives    Fi �̕Δ�l (dFi/dx0, ..., dFi/dxm) ��Ԃ���?��̔z��
     * @param convergence    n �̉� (x0, ..., xm) �����ł��邩�ۂ��𔻒f�����?�
     * @param correct        ���Z�̓r���� n �̉� (x0, ..., xm) �̒l��?��I��?C?������?�
     * @param initialGuesses n �̉� (x0, ..., xm) ��?���l�̔z��
     * @return �A�����̎�� (x0, ..., xm) �̔z��
     * @see #solveSimultaneousEquations(PrimitiveMappingND,PrimitiveMappingND[],PrimitiveBooleanMappingNDTo1D,double[])
     */
    public static double[]
    solveSimultaneousEquationsWithCorrection(PrimitiveMappingND func,
                                             PrimitiveMappingND[] derivatives,
                                             PrimitiveBooleanMappingNDTo1D convergence,
                                             PrimitiveMappingND correct,
                                             double[] initialGuesses) {
        int nX = initialGuesses.length;
        double[] X = (double[]) (initialGuesses.clone());
        double[] F;
        double[][] dF = new double[nX][];
        double[] delta;

        int maxIteration = 50;

        if (convergence.map(X) == true)
            return X;

        for (int i = 0; i < maxIteration; i++) {
            if ((F = func.map(X)) == null)
                return null;
            for (int j = 0; j < nX; j++)
                if ((dF[j] = derivatives[j].map(X)) == null)
                    return null;

            delta = (new Matrix(dF)).solveSimultaneousLinearEquations(F);

            if (delta == null)
                return null;

            for (int j = 0; j < nX; j++)
                X[j] -= delta[j];

            if (correct != null)
                X = correct.map(X);

            if (convergence.map(X) == true)
                return X;
        }

        return null;
    }

    /**
     * �^����ꂽ�p�x�� [0, 2 * PI] �̊Ԃ̒l��?��K������?B
     *
     * @param angle �p�x (���W�A��)
     * @return ?��K�����ꂽ�p�x (���W�A��)
     */
    public static double normalizeAngle(double angle) {
        double eangle = angle;

        while (eangle < 0.0)
            eangle += 2.0 * Math.PI;

        while (eangle > 2.0 * Math.PI)
            eangle -= 2.0 * Math.PI;

        return (eangle);
    }

    /**
     * ***********************************************************************
     * <p/>
     * Debug
     * <p/>
     * ************************************************************************
     */
    /* Debug : getDefiniteIntegral */
    private static void debugGetDefiniteIntegral(String argv[]) {
        try {
            double[] coef = new double[argv.length - 1];
            for (int i = 0; i < (argv.length - 1); i++)
                coef[i] = Double.valueOf(argv[i]).doubleValue();
            DoublePolynomial poly = new DoublePolynomial(coef);
            ParameterSection param = new ParameterSection(0.0, 1.0);
            double result
                    = getDefiniteIntegral(poly, param,
                    Double.valueOf(argv[argv.length - 1]).doubleValue());
            System.out.println("result : " + result);
        } catch (InvalidArgumentValueException e) {
        }
    }

    /**
     * �f�o�b�O�p�?�C���v�?�O����?B
     */
    public static void main(String argv[]) {
        debugGetDefiniteIntegral(argv);
    }

    /*
    * double �̂P�����z���̗v�f��?A���̒l�̑傫����?�?��Ƀ\?[�g����?B
    *
    * @param array	double �̂P�����z��
    * @see	#sortDoubleArray(double[], int, int)
    */
    protected static void sortDoubleArray(double[] array) {
        sortDoubleArray(array, 0, array.length);
    }

    /*
    * double �̂P�����z��̎w��͈͓̔�̗v�f��?A���̒l�̑傫����?�?��Ƀ\?[�g����?B
    * <p>
    * array[low] ���� array[up] �܂ł�?A���̒l�̑傫����?�?��Ƀ\?[�g����?B
    * </p>
    *
    * @param array	double �̂P�����z��
    * @param low	�\?[�g�̑�?۔͈͂̊J�n�C���f�b�N�X
    * @param up	�\?[�g�̑�?۔͈͂�?I���C���f�b�N�X
    * @see	#sortDoubleArray(double[])
    */
    protected static void sortDoubleArray(double[] array, int low, int up) {
        int lidx = low;
        int uidx = up;
        double key = array[(low + up) / 2];
        double swap;

        for (; lidx < uidx;) {
            for (; array[lidx] < key; lidx++)
                /* nop */
                ;
            for (; key < array[uidx]; uidx--)
                /* nop */
                ;

            if (lidx <= uidx) {
                swap = array[uidx];
                array[uidx] = array[lidx];
                array[lidx] = swap;
                lidx++;
                uidx--;
            }
        }

        if (low < uidx) sortDoubleArray(array, low, uidx);
        if (lidx < up) sortDoubleArray(array, lidx, up);
    }

    /**
     * ?�?��Ƀ\?[�g���ꂽ double �̂P�����z��ɂ�����?A
     * �^����ꂽ�l��z���Ȃ����?ő�l��?�v�f�̃C���f�b�N�X
     * <p/>
     * (array[v] &lt;= value && value &lt; array[v + 1]) �𖞂��l v ��Ԃ�?B
     * </p>
     * <p/>
     * value &lt; array[min] �Ȃ�� (min - 1) ��Ԃ�?B
     * </p>
     * <p/>
     * array[max] &lt;= value �Ȃ�� max ��Ԃ�?B
     * </p>
     *
     * @param array double �̂P�����z��
     * @param min   �\?[�g�̑�?۔͈͂̊J�n�C���f�b�N�X
     * @param max   �\?[�g�̑�?۔͈͂�?I���C���f�b�N�X
     * @param value ?��?��?ۂƂ���l
     * @return value ��z���Ȃ����?ő�l��?�v�f�̃C���f�b�N�X
     */
    protected static int bsearchDoubleArray(double[] array, int min,
                                            int max, double value) {
        if (value < array[min])
            return min - 1;
        else if (array[max] <= value)
            return max;
        else {
            int mid;

            while (min + 1 < max) {
                mid = (min + max) / 2;
                if (value < array[mid])
                    max = mid;
                else
                    min = mid;
            }
            return min;
        }
    }

    /**
     * Calculate the arcminute and arcsecond to a given angle.
     *
     * @param dd the angle in degree in decimal notation.
     */
    public double[] toDegreesMinutesSeconds(double dd) {

        double[] result = new double[3];

        double x = StrictMath.abs(dd);
        result[0] = (int) x;
        x = (x - result[0]) * 60.0;
        result[1] = (int) x;
        result[2] = (x - result[1]) * 60.0;

        if (dd < 0.0) {
            if (result[0] != 0) {
                result[0] *= -1;
            } else {
                if (result[1] != 0) {
                    result[1] *= -1;
                } else {
                    result[2] *= -1.0;
                }
            }
        }

        return result;

    }

    /**
     * Calculate the angle in decimal notation with the three values.
     *
     * @param d the angle in degree [�].
     * @param m the arcminute ['].
     * @param s the arcsecond [''].
     */
    public double toDecimalAngle(int d, int m, double s) {

        double sign;

        if ((d < 0) || (m < 0) || (s < 0)) {
            sign = -1.0;
        } else {
            sign = 1.0;
        }

        return sign * (StrictMath.abs(d) + (StrictMath.abs(m) / 60.0) +
                (StrictMath.abs(s) / 3600.0));
    }

    /**
     * Method to convert radians to degrees
     *
     * @param radians - the value
     * @return the equivalent in degrees
     */
    public static double toDegrees(double radians) {
        return (radians * 180.0 / Math.PI);
    }

    /**
     * Method to convert degrees to radians
     *
     * @param degrees - the value
     * @return the equivalent in radians
     */
    public static double toRadians(double degrees) {
        return (degrees * Math.PI / 180.0);
    }

    /**
     * �^����ꂽ��?��̋t?����Ƃ�邩�ǂ�����?�����?B
     *
     * @param value �t?����Ƃ�邩�ǂ�����?�����l
     * @return �t?����Ƃ��̂ł���� true?A�����łȂ���� false
     * @see MachineEpsilon#DOUBLE
     */
    public static boolean isReciprocatable(double value) {
        if (Math.abs(value) < MachineEpsilon.DOUBLE)
            return false;
        return true;
    }

    /**
     * �^����ꂽ��̎�?���?��Z���S���ǂ�����?�����?B
     * <p/>
     * (a / b) ���S���ǂ����𒲂ׂ�?B
     * </p>
     *
     * @param a ��?�?�
     * @param b ?�?�
     * @return �S�Ɋ����̂ł���� true?A�����łȂ���� false
     * @see #isReciprocatable(double)
     */
    public static boolean isDividable(double a, double b) {
        double c;

        // Division by floating point does never throw any exception.
        c = b / a; // reverse

        if (Double.isNaN(c) ||
                !isReciprocatable(c))
            //Math.abs(c) < MachineEpsilon.DOUBLE)
            return false;
        return true;
    }
}

/* end of file */
