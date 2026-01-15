/*
 * �a�X�v���C���̃m�b�g���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: BsplineKnot.java,v 1.3 2007-10-21 21:08:07 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import org.jscience.util.FatalException;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * �a�X�v���C���̃m�b�g���\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A�ȉ��̑�?��l�ɂ�BĂa�X�v���C���̃m�b�g���\��?B
 * <ul>
 * <li>	degree :		�a�X�v���C���̎�?���?�?�
 * <li> nControlPoints :	���̃m�b�g��ɑΉ�����?���_��?���?�?�
 * <li> knotSpec :		�m�b�g��̎�ʂ�?�?� ({@link KnotType KnotType})
 * <li> periodic :		�����`�����ۂ����_�?�l
 * <li>	knotMultiplicities :	�e�m�b�g�̑�?d�x��?�?��̔z��
 * <li> knots :			�e�m�b�g�̒l����?��̔z��
 * </ul>
 * </p>
 * <p/>
 * ��?ۂ̃m�b�g��̒�����?A
 * periodic �� false �ł���� (degree + nControlPoints + 1)?A
 * periodic �� true �ł���� (2 * degree + nControlPoints + 1)
 * �ɂȂ�?B
 * </p>
 * <p/>
 * <a name="CONSTRAINTS">[��?��Ԃ�?S��?�?]</a>
 * </p>
 * <p/>
 * degree �� 1 ��?�łȂ���΂Ȃ�Ȃ�?B
 * </p>
 * <p/>
 * knotSpec �� KnotType.UNIFORM_KNOTS ��?�?��ɂ�
 * <ul>
 * <li>	knotMultiplicities ���邢�� knots �� null �łȂ���΂Ȃ�Ȃ�?B
 * </ul>
 * </p>
 * <p/>
 * knotSpec �� KnotType.UNSPECIFIED ��?�?��ɂ�
 * <ul>
 * <li>	knotMultiplicities ���邢�� knots �� null �ł��BĂ͂Ȃ�Ȃ�?B
 * <li>	knotMultiplicities �̒����� 1 ��?�łȂ���΂Ȃ�Ȃ�?B
 * <li>	knotMultiplicities �̒����� knots �̒�������v���Ȃ���΂Ȃ�Ȃ�?B
 * <li>	(knots[i-1] &gt; knots[i]) �ł��BĂ͂Ȃ�Ȃ�?B
 * <li>	knotMultiplicities[i] �� (degree + 1) �ȉ��łȂ���΂Ȃ�Ȃ�?B
 * <li>	periodic �� false �ł����
 * knotMultiplicities[i] �̑?�a�� (degree + nControlPoints + 1) �ƈ�v���Ȃ���΂Ȃ�Ȃ�?B
 * <li>	periodic �� true �ł����
 * <ul>
 * <li>	knotMultiplicities[i] �̑?�a�� (2 * degree + nControlPoints + 1) �ƈ�v���Ȃ���΂Ȃ�Ȃ�?B
 * <li>	?�?���?Ō�� (2 * degree) �̃m�b�g�Ԋu���݂��Ɉ�v���Ȃ���΂Ȃ�Ȃ�?B
 * </ul>
 * </ul>
 * </p>
 * <p/>
 * knotSpec ��
 * KnotType.QUASI_UNIFORM_KNOTS ���邢��
 * KnotType.PICEWISE_BEZIER_KNOTS �ł��BĂ͂Ȃ�Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:07 $
 */

public class BsplineKnot extends java.lang.Object implements Serializable {

    /**
     * ��?�?B
     *
     * @serial
     */
    private int degree;

    /**
     * ?���_��?�?B
     *
     * @serial
     */
    private int nControlPoints;

    /**
     * �m�b�g�̎�� ({@link KnotType KnotType}) ?B
     * <p/>
     * ���܂̂Ƃ���
     * {@link KnotType#QUASI_UNIFORM_KNOTS KnotType.QUASI_UNIFORM_KNOTS},
     * {@link KnotType#PIECEWISE_BEZIER_KNOTS KnotType.PIECEWISE_BEZIER_KNOTS}
     * �ɂ͑Ή����Ă��Ȃ�?B
     * </p>
     *
     * @serial
     */
    private int knotSpec;

    /**
     * �����`�����ۂ���\���t���O?B
     *
     * @serial
     */
    private boolean periodic;

    /**
     * �m�b�g��?d�x�̔z��?B
     *
     * @serial
     */
    private int[] knotMultiplicities;

    /**
     * �m�b�g�l�̔z��?B
     *
     * @serial
     */
    private double[] knots;

    /**
     * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param degree             ��?�
     * @param knotSpec           �m�b�g�̎�� (KnotType)
     * @param periodic           �����`�����ۂ���\���t���O
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param nControlPoints     ?���_��?�
     * @see KnotType
     * @see InvalidArgumentValueException
     */
    public BsplineKnot(int degree, int knotSpec, boolean periodic,
                       int[] knotMultiplicities, double[] knots,
                       int nControlPoints) {
        this(degree, knotSpec, periodic, knotMultiplicities, knots, nControlPoints, true);
    }

    /**
     * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * doCheck �� true �ł���Ƃ���?A
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param degree             ��?�
     * @param knotSpec           �m�b�g�̎�� (KnotType)
     * @param periodic           �����`�����ۂ���\���t���O
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param nControlPoints     ?���_��?�
     * @param doCheck            ��?��̃`�F�b�N�ⷂ邩�ǂ���
     * @see KnotType
     * @see InvalidArgumentValueException
     */
    BsplineKnot(int degree, int knotSpec, boolean periodic,
                int[] knotMultiplicities, double[] knots,
                int nControlPoints, boolean doCheck) {
        super();
        setData(degree, knotSpec, periodic, knotMultiplicities, knots, nControlPoints, doCheck);
    }

    /**
     * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^�ł͑��قȂ�m�b�g��?��𖾎�����?B
     * ��?��ɗ^����m�b�g�l����уm�b�g��?d�x�̔z��?A
     * ?\�z����I�u�W�F�N�g�ŕK�v�ƂȂ��̂��ҷ��?�?��ɗ��p����邱�Ƃ�z�肵�Ă���?B
     * </p>
     * <p/>
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param degree             ��?�
     * @param knotSpec           �m�b�g�̎�� (KnotType)
     * @param periodic           �����`�����ۂ���\���t���O
     * @param nKnots             �m�b�g��/��?d�x�̔z��̒���
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param nControlPoints     ?���_��?�
     * @see KnotType
     * @see InvalidArgumentValueException
     */
    BsplineKnot(int degree, int knotSpec, boolean periodic,
                int nKnots, int[] knotMultiplicities, double[] knots,
                int nControlPoints) {
        this(degree, knotSpec, periodic, nKnots, knotMultiplicities, knots, nControlPoints, true);
    }

    /**
     * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^�ł͑��قȂ�m�b�g��?��𖾎�����?B
     * ��?��ɗ^����m�b�g�l����уm�b�g��?d�x�̔z��?A
     * ?\�z����I�u�W�F�N�g�ŕK�v�ƂȂ��̂��ҷ��?�?��ɗ��p����邱�Ƃ�z�肵�Ă���?B
     * </p>
     * <p/>
     * doCheck �� true �ł���Ƃ���?A
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param degree             ��?�
     * @param knotSpec           �m�b�g�̎�� (KnotType)
     * @param periodic           �����`�����ۂ���\���t���O
     * @param nKnots             �m�b�g��/��?d�x�̔z��̒���
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param nControlPoints     ?���_��?�
     * @param doCheck            ��?��̃`�F�b�N�ⷂ邩�ǂ���
     * @see KnotType
     * @see InvalidArgumentValueException
     */
    BsplineKnot(int degree, int knotSpec, boolean periodic,
                int nKnots, int[] knotMultiplicities, double[] knots,
                int nControlPoints, boolean doCheck) {
        super();
        int[] new_multi = new int[nKnots];
        double[] new_knots = new double[nKnots];

        if (knots.length < nKnots || knotMultiplicities.length < nKnots)
            throw new InvalidArgumentValueException();
        for (int i = 0; i < nKnots; i++) {
            new_multi[i] = knotMultiplicities[i];
            new_knots[i] = knots[i];
        }
        setData(degree, knotSpec, periodic, new_multi, new_knots, nControlPoints, doCheck);
    }

    /**
     * ���̃C���X�^���X�̃t�B?[���h�ɒl��?ݒ肷��?B
     * <p/>
     * doCheck �� true �ł���Ƃ���?A
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param degree             ��?�
     * @param knotSpec           �m�b�g�̎�� (KnotType)
     * @param periodic           �����`�����ۂ���\���t���O
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param nControlPoints     ?���_��?�
     * @param doCheck            ��?��̃`�F�b�N�ⷂ邩�ǂ���
     * @see KnotType
     * @see InvalidArgumentValueException
     */
    private void setData(int degree, int knotSpec, boolean periodic,
                         int[] knotMultiplicities, double[] knots,
                         int nControlPoints, boolean doCheck) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double p_tol = condition.getToleranceForParameter();
        this.degree = degree;
        this.nControlPoints = nControlPoints;
        this.knotSpec = knotSpec;
        this.periodic = periodic;

        if (!doCheck) {
            if (knotSpec != KnotType.UNSPECIFIED) {
                this.knotMultiplicities = null;
                this.knots = null;
            } else {
                int uik = knotMultiplicities.length;
                this.knotMultiplicities = new int[uik];
                this.knots = new double[uik];
                for (int i = 0; i < uik; i++) {
                    this.knotMultiplicities[i] = knotMultiplicities[i];
                    this.knots[i] = knots[i];
                }
            }
            return;
        }

        if (degree < 1) {
            throw new InvalidArgumentValueException();
        }
        switch (knotSpec) {
            case KnotType.UNIFORM_KNOTS:
                if (knotMultiplicities != null) {
                    throw new InvalidArgumentValueException();
                }
                if (knots != null) {
                    throw new InvalidArgumentValueException();
                }
                this.knotMultiplicities = null;
                this.knots = null;
                break;
            case KnotType.UNSPECIFIED: {
                int uik;
                int sum;

                if (knotMultiplicities == null) {
                    throw new InvalidArgumentValueException();
                }
                if ((uik = knotMultiplicities.length) < 1) {
                    throw new InvalidArgumentValueException();
                }

                if (knots == null) {
                    throw new InvalidArgumentValueException();
                }
                if (knots.length != uik) {
                    throw new InvalidArgumentValueException();
                }

                this.knotMultiplicities = new int[uik];
                this.knots = new double[uik];
                sum = 0;
                for (int i = 0; i < uik; i++) {
                    this.knotMultiplicities[i] = knotMultiplicities[i];
                    this.knots[i] = knots[i];
                    sum += knotMultiplicities[i];
                    if (i > 0 && knots[i - 1] > knots[i]) {
                        throw new InvalidArgumentValueException();
                    }
                    if (knotMultiplicities[i] > degree + 1) {
                        throw new InvalidArgumentValueException();
                    }
                }
                if ((periodic && 2 * degree + nControlPoints + 1 != sum) ||
                        (!periodic && degree + nControlPoints + 1 != sum)) {
                    /*
                    System.out.println("degree:" + degree + " nControlPoints:" + nControlPoints
                               + " sum:" + sum);
                    */
                    throw new InvalidArgumentValueException();
                }
                if (periodic) {
                    /*
                    * In closed case, first (2 * n) intervals and last (2 * n) intervals
                    * should be coincide.
                    */
                    double h_intvl, h_start, h_end; /* head (interval, start, end) */
                    double t_intvl, t_start, t_end; /* tail (interval, start, end) */
                    int n2 = 2 * degree;
                    int i, j;

                    h_end = knotValueAt(i = 0);
                    t_end = knotValueAt(j = nControlPoints); /* In closed case,
                      the number of segments is equal to nControlPoints */
                    while (i < n2) {
                        h_start = h_end;
                        t_start = t_end;
                        h_intvl = (h_end = knotValueAt(i++)) - h_start;
                        t_intvl = (t_end = knotValueAt(j++)) - t_start;
                        if (Math.abs(h_intvl - t_intvl) > p_tol)
                            throw new InvalidArgumentValueException();
                    }
                }
            }
            break;
            default:    /* includes QUASI_UNIFORM_KNOTS and PICEWISE_BEZIER_KNOTS */
                throw new InvalidArgumentValueException();
        }
    }

    /**
     * ���̃m�b�g��̎�?���Ԃ�?B
     *
     * @return ��?�
     */
    public int degree() {
        return degree;
    }

    /**
     * ���̃m�b�g���?���_��?���Ԃ�?B
     *
     * @return ?���_��?�
     */
    public int nControlPoints() {
        return nControlPoints;
    }

    /**
     * ���̃m�b�g��̃m�b�g�̎�ʂ�Ԃ�?B
     *
     * @return �m�b�g�̎��
     * @see KnotType
     */
    public int knotSpec() {
        return knotSpec;
    }

    /**
     * ���̃m�b�g��̃m�b�g�l�̔z��̒�����Ԃ�?B
     * <p/>
     * �m�b�g�̎�ʂ� KnotType.UNSPECIFIED �łȂ�?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return �m�b�g�l�̔z��̒���
     * @see FatalException
     */
    public int nKnots() {
        if (knotSpec() != KnotType.UNSPECIFIED)
            throw new FatalException();

        return knots.length;
    }

    /**
     * ���̃m�b�g��̃m�b�g��?d�x�̔z���Ԃ�?B
     * <p/>
     * �m�b�g�̎�ʂ� KnotType.UNSPECIFIED �łȂ�?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return �m�b�g��?d�x�̔z��
     * @see FatalException
     */
    public int[] knotMultiplicities() {
        if (knotSpec() != KnotType.UNSPECIFIED)
            throw new FatalException();

        return (int[]) knotMultiplicities.clone();
    }

    /**
     * ���̃m�b�g��̃m�b�g�l�̔z���Ԃ�?B
     * <p/>
     * �m�b�g�̎�ʂ� KnotType.UNSPECIFIED �łȂ�?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return �m�b�g�l�̔z��
     * @see FatalException
     */
    public double[] knots() {
        if (knotSpec() != KnotType.UNSPECIFIED)
            throw new FatalException();

        return (double[]) knots.clone();
    }

    /**
     * ���̃m�b�g�񂪎?�m�b�g��?d�x�̔z��� i �Ԗڂ̒l��Ԃ�?B
     * <p/>
     * �m�b�g�̎�ʂ� KnotType.UNSPECIFIED �łȂ�?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return i �Ԗڂ̃m�b�g��?d�x
     * @see FatalException
     */
    public int knotMultiplicityAt(int i) {
        if (knotSpec() != KnotType.UNSPECIFIED)
            throw new FatalException();

        return knotMultiplicities[i];
    }

    /**
     * ���̃m�b�g���?A�w��̃m�b�g�l�ɑΉ�����m�b�g��?d�x��Ԃ�?B
     * <p/>
     * �^����ꂽ�l���m�b�g�l�łȂ�?�?��ɂ� 0 ��Ԃ�?B
     * </p>
     * <p/>
     * �Ȃ�?A���̃?�\�b�h��?������l��Ԃ�����ɂ�
     * �m�b�g��?��`����Ă���K�v������?B
     * </p>
     *
     * @param knot ��?d�x�𒲂ׂ�m�b�g�l
     * @return �w��̃m�b�g�l�̃m�b�g��?d�x
     * @see #beautify()
     */
    public int knotMultiplicityAt(double knot) {
        double pTol = ConditionOfOperation.getCondition().getToleranceForParameter();

        if (this.knotSpec() != KnotType.UNSPECIFIED) {
            // ���j�t�H?[��
            double lower = Math.floor(knot);
            double upper = Math.ceil(knot);

            double lowerLimit = 0 - this.degree();
            double upperLimit = (this.nKnotValues() - 1) - this.degree();

            if ((knot - lower) < pTol) {
                if ((lowerLimit <= lower) && (lower <= upperLimit))
                    return 1;
            } else if ((upper - knot) < pTol) {
                if ((lowerLimit <= upper) && (upper <= upperLimit))
                    return 1;
            }
        } else {
            // �񃆃j�t�H?[��
            for (int i = 0; i < this.nKnots(); i++) {
                double ithKnot = this.knotAt(i);
                if (Math.abs(knot - ithKnot) < pTol)
                    return this.knotMultiplicityAt(i);
                if (knot < ithKnot)
                    return 0;
            }
        }

        return 0;
    }

    /**
     * ���̃m�b�g�񂪎?�m�b�g�l�̔z��� i �Ԗڂ̒l��Ԃ�?B
     * <p/>
     * �m�b�g�̎�ʂ� KnotType.UNSPECIFIED �łȂ�?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @return i �Ԗڂ̃m�b�g�l
     * @see FatalException
     */
    public double knotAt(int i) {
        if (knotSpec() != KnotType.UNSPECIFIED)
            throw new FatalException();

        return knots[i];
    }

    /**
     * ���̃m�b�g�񂪕����`���ł��邩�ۂ���Ԃ�?B
     *
     * @return �����`���ł���� true?A�����łȂ���� false
     */
    public boolean isPeriodic() {
        return periodic;
    }

    /**
     * ���̃m�b�g�񂪊J�����`���ł��邩�ۂ���Ԃ�?B
     *
     * @return �J�����`���ł���� true?A�����łȂ���� false
     */
    public boolean isNonPeriodic() {
        return !periodic;
    }

    /**
     * ���̃m�b�g�񂪕\���p���??[�^��`���Ԃ�?B
     *
     * @return �p���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        double start, increase;

        start = knotValueAt(degree);
        increase = knotValueAt(degree + nSegments()) - start;

        try {
            return new ParameterDomain(periodic, start, increase);
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ���̃m�b�g�񂪎����a�X�v���C���̃Z�O�?���g��?���Ԃ�?B
     *
     * @return �Z�O�?���g��?�
     */
    public int nSegments() {
        if (periodic) {
            return nControlPoints;
        } else {
            return nControlPoints - degree;
        }
    }

    /**
     * ���̃m�b�g��̃m�b�g��?���Ԃ�?B
     * <p/>
     * �����Ō���?u�m�b�g��?�?v�Ƃ�
     * knots �t�B?[���h��?ݒ肳�ꂽ�m�b�g�l�̔z��̒����ł͂Ȃ�?A
     * �a�X�v���C���̃m�b�g��{���̃m�b�g��?��ł���?B
     * </p>
     *
     * @return �m�b�g��?�
     */
    public int nKnotValues() {
        return 2 * degree + nSegments() + 1;
    }

    /**
     * �^����ꂽ�m�b�g��� n �Ԗڂ̃m�b�g�l��Ԃ�?B
     * <p/>
     * �����Ō���?un �Ԗ�?v�Ƃ�
     * �^����ꂽ�m�b�g�l�̔z���̃C���f�b�N�X�ł͂Ȃ�?A
     * �a�X�v���C���̃m�b�g��{���̈Ӗ��ł̃C���f�b�N�X�ł���?B
     * </p>
     *
     * @param knotMultiplicities �m�b�g��?d�x�̔z��
     * @param knots              �m�b�g�l�̔z��
     * @param n                  �C���f�b�N�X
     * @return n �Ԗڂ̃m�b�g�l
     */
    static double knotValueAt(int[] knotMultiplicities,
                              double[] knots,
                              int n) {
        int sum = 0;
        int i = 0;

        while (n >= sum)
            sum += knotMultiplicities[i++];

        return (knots[i - 1]);
    }

    /**
     * ���̃m�b�g��� n �Ԗڂ̃m�b�g�l��Ԃ�?B
     * <p/>
     * �����Ō���?un �Ԗ�?v�Ƃ�
     * knots �t�B?[���h��?ݒ肳�ꂽ�m�b�g�l�̔z���̃C���f�b�N�X�ł͂Ȃ�?A
     * �a�X�v���C���̃m�b�g��{���̈Ӗ��ł̃C���f�b�N�X�ł���?B
     * </p>
     *
     * @param n �C���f�b�N�X
     * @return n �Ԗڂ̃m�b�g�l
     */
    public double knotValueAt(int n) {
        if (knotSpec == KnotType.UNIFORM_KNOTS)
            return (double) (n - degree);
        else
            return BsplineKnot.knotValueAt(knotMultiplicities, knots, n);
    }

    /**
     * ���̃m�b�g���?A�^����ꂽ�p���??[�^�l�ɑΉ�����Z�O�?���g�̃C���f�b�N�X��Ԃ�?B
     * <p/>
     * param ��
     * {@link ParameterDomain#wrap(double) ParameterDomain.wrap(double)}
     * ���邢��
     * {@link ParameterDomain#force(double) ParameterDomain.force(double)}
     * ��p����?��K������Ă���K�v������?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �Ή�����Z�O�?���g�̃C���f�b�N�X
     */
    public int segmentIndex(double param) {
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double p_tol = condition.getToleranceForParameter();
        int r;
        int sum;
        int i;

        r = nSegments();
        if (Math.abs(knotValueAt(degree + r) - param) < p_tol) {    /* upper limit */
            while ((--r >= 0) && (Math.abs(knotValueAt(degree + r) - param) < p_tol))
                ;
            return r;
        }

        if (knotSpec == KnotType.UNIFORM_KNOTS)
            return ((int) param);

        sum = i = 0;
        while (!(param < knots[i])) {
            /*
            * while knots[i] is less-equal than param,
            * sum is accumulated by knotMultiplicities[i], and
            * i   is incremented by 1.
            */
            sum += knotMultiplicities[i++];

            if (i >= knots.length) {
                /*
                * i is equal with the size of 'knots' array
                */
                if (param > (knots[--i] + p_tol)) {
                    /*
                    * param is greater than the max valid value, out of range
                    */
                    return (-1);
                }

                param = knots[i];
                while (!(param > knots[i]))
                    sum -= knotMultiplicities[i--];
                break;
            }
        }

        r = sum - degree - 1;
        if (r >= 0)
            return (r);
        else
            return (-1);
    }

    /**
     * ���̃m�b�g��� (?k�ނ��Ă��Ȃ�) �L��ȃZ�O�?���g��?���Ԃ�?B
     *
     * @return �L��ȃZ�O�?���g��?��
     */
    ValidSegmentInfo validSegments() {
        int nseg_p1;    /* # of segments + 1 */
        int[] sn;    /* pointer to 'segment number' array */
        double[] kp;    /* pointer to 'knot point' array */

        int nvseg;        /* # of valid segments */
        double kval, pval;    /* value of knot */
        int i, k;        /* loop counter */

        double tol_p = ConditionOfOperation.getCondition().getToleranceForParameter();

        nseg_p1 = nSegments() + 1;

        sn = new int[nseg_p1];
        kp = new double[nseg_p1];

        nvseg = (-1);
        i = degree;
        pval = (knotValueAt(i) - 1.0);    /* Initial value of 'pval' should be smaller
                     than lower limit of first segment.
                     So (- 1.0) is applied. */

        for (k = 0; k < nseg_p1; k++) {    /* from lower limit to upper limit */

            if (((kval = knotValueAt(i)) - pval) > tol_p) {
                /* segment has a width. first pass of this 'for' block shall reach here. */
                sn[++nvseg] = i - degree;
                kp[nvseg] = kval;
            } else {
                /* reduced segment */
                sn[nvseg] = i - degree;
            }

            i++;
            pval = kval;
        }

        return new ValidSegmentInfo(nvseg, sn, kp);
    }

    /**
     * ����m�b�g��� (?k�ނ��Ă��Ȃ�) �L��ȃZ�O�?���g��?���\���Ք�N���X?B
     */
    class ValidSegmentInfo {
        /**
         * �L��ȃZ�O�?���g�̃C���f�b�N�X�̔z��?B
         */
        private int[] segmentNumber;

        /**
         * �L��ȃZ�O�?���g�̃m�b�g�_�̔z��?B
         */
        private double[] knotPoint;

        /**
         * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param nSegments     �L��ȃZ�O�?���g��?�?B
         * @param segmentNumber �L��ȃZ�O�?���g�̃C���f�b�N�X�̔z��
         * @param knotPoint     �L��ȃZ�O�?���g�̃m�b�g�_�̔z��
         */
        ValidSegmentInfo(int nSegments, int[] segmentNumber, double[] knotPoint) {
            this.segmentNumber = new int[nSegments];
            this.knotPoint = new double[nSegments + 1];
            for (int i = 0; i < nSegments; i++) {
                this.segmentNumber[i] = segmentNumber[i];
                this.knotPoint[i] = knotPoint[i];
            }
            this.knotPoint[nSegments] = knotPoint[nSegments];
        }

        /**
         * �L��ȃZ�O�?���g��?���Ԃ�?B
         *
         * @return �L��ȃZ�O�?���g��?�
         */
        int nSegments() {
            return segmentNumber.length;
        }

        /**
         * n �Ԗڂ̗L��ȃZ�O�?���g�̃C���f�b�N�X��Ԃ�?B
         *
         * @return n �Ԗڂ̗L��ȃZ�O�?���g�̃C���f�b�N�X
         */
        int segmentNumber(int n) {
            return segmentNumber[n];
        }

        /**
         * n �Ԗڂ̗L��ȃZ�O�?���g�̃m�b�g�_��Ԃ�?B
         * <p/>
         * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
         * ?�?��̗v�f�ɂ̓Z�O�?���g�̎n�_�̃p���??[�^�l?A
         * ��Ԗڂ̗v�f�ɂ̓Z�O�?���g��?I�_�̃p���??[�^�l
         * �����?B
         * </p>
         *
         * @return n �Ԗڂ̗L��ȃZ�O�?���g�̃m�b�g�_�̔z��
         */
        double[] knotPoint(int n) {
            double[] prms = new double[2];

            prms[0] = knotPoint[n];
            prms[1] = knotPoint[n + 1];
            return prms;
        }

        /**
         * n �Ԗڂ̗L��ȃZ�O�?���g�̎n�_�̃p���??[�^�l��Ԃ�?B
         *
         * @return n �Ԗڂ̗L��ȃZ�O�?���g�̎n�_�̃p���??[�^�l
         */
        double headKnotPoint(int n) {
            return knotPoint[n];
        }

        /**
         * n �Ԗڂ̗L��ȃZ�O�?���g��?I�_�̃p���??[�^�l��Ԃ�?B
         *
         * @return n �Ԗڂ̗L��ȃZ�O�?���g��?I�_�̃p���??[�^�l
         */
        double tailKnotPoint(int n) {
            return knotPoint[n + 1];
        }

        /**
         * �^����ꂽ�p���??[�^�l��?g�̓Ք�Ɋ܂ޗL��ȃZ�O�?���g��
         * segmentNumber[] ��ł̃C���f�b�N�X��Ԃ�?B
         *
         * @param �p���??[�^�l
         * @return �p���??[�^�l��?g�̓Ք�Ɋ܂ޗL��ȃZ�O�?���g�� segmentNumber[] ��ł̃C���f�b�N�X
         */
        int segmentIndex(double param) {
            double tol_p = ConditionOfOperation.getCondition().getToleranceForParameter();
            int nseg;
            int i;

            if (param < knotPoint[0])
                return -1;

            nseg = nSegments();
            for (i = 0; i < nseg; i++)
                if (param < knotPoint[i + 1])
                    return i;

            if (Math.abs(param - knotPoint[nseg]) < tol_p)
                return (nseg - 1);

            return -1;
        }

        /**
         * �^����ꂽ�Z�O�?���g���L��ۂ��𒲂ׂ�?B
         * <p/>
         * �^����ꂽ�Z�O�?���g���L��łȂ����?A-1 ��Ԃ�?B
         * </p>
         *
         * @param �Z�O�?���g�̃C���f�b�N�X
         * @return �^����ꂽ�Z�O�?���g�� segmentNumber[] ��ł̃C���f�b�N�X
         */
        int isValidSegment(int seg) {
            int klm;
            int nvseg = nSegments();
            for (klm = 0; klm < nvseg; klm++) {
                if (segmentNumber(klm) == seg)
                    return klm;
            }
            return -1;
        }

        /**
         * ��?��p���??[�^�?����?�֕ϊ�����?B
         *
         * @param index ��?ۂƂ���L��ȃZ�O�?���g�̓Y��
         * @param local ��?��p���??[�^�?
         * @return ���?
         */
        double l2Gw(int index, double local) {
            double[] knots = knotPoint(index);
            return (knots[1] - knots[0]) * local;
        }

        /**
         * ��?��p���??[�^�l����l�֕ϊ�����?B
         *
         * @param index ��?ۂƂ���L��ȃZ�O�?���g�̓Y��
         * @param local ��?��p���??[�^�l
         * @return ���l
         */
        double l2Gp(int index, double local) {
            double[] knots = knotPoint(index);
            return knots[0] + (knots[1] - knots[0]) * local;
        }

        /**
         * �^����ꂽ�p���??[�^�l�ɓ������m�b�g�_��n�_�Ƃ���Z�O�?���g�̔�?���Ԃ�?B
         * <p/>
         * ?�?I�Z�O�?���g��?I�_�ɓ�����?�?��ɂ�?A(?�?I�Z�O�?���g�̔�?� + 1) ��Ԃ�?B
         * </p>
         * <p/>
         * �^����ꂽ�p���??[�^�l���m�b�g�_�łȂ�?�?��ɂ� -1 ��Ԃ�?B
         * </p>
         *
         * @param param �p���??[�^�l
         * @return �p���??[�^�l�ɓ������m�b�g�_��n�_�Ƃ���Z�O�?���g�̔�?�
         */
        int getSegmentNumberThatStartIsEqualTo(double param) {
            double pTol = ConditionOfOperation.getCondition().getToleranceForParameter();
            for (int i = 0; i <= this.segmentNumber.length; i++)
                if (Math.abs(param - this.knotPoint[i]) < pTol)
                    return segmentNumber[i];
            return (-1);
        }
    }

    /**
     * ���̃m�b�g���?A
     * �^����ꂽ�p���??[�^�l�ɓ������m�b�g�_��n�_�Ƃ��� (?Ō��) �Z�O�?���g�̔�?���Ԃ�?B
     * <p/>
     * ?�?I�Z�O�?���g��?I�_�ɓ�����?�?��ɂ�?A(?�?I�Z�O�?���g�̔�?� + 1) ��Ԃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l���m�b�g�_�łȂ�?�?��ɂ� -1 ��Ԃ�?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �p���??[�^�l�ɓ������m�b�g�_��n�_�Ƃ��� (?Ō��) �Z�O�?���g�̔�?�
     */
    int getSegmentNumberThatStartIsEqualTo(double param) {
        double pTol = ConditionOfOperation.getCondition().getToleranceForParameter();

        for (int i = (this.degree + this.nSegments()); i >= this.degree; i--)
            if (Math.abs(param - this.knotValueAt(i)) < pTol)
                return (i - this.degree);
        return (-1);
    }

    /**
     * ���̃m�b�g��̗��[�̒l������`���Ƃ���?�������̂ɂ����m�b�g���Ԃ�?B
     *
     * @return ���[�̒l�������`���Ƃ���?������m�b�g��
     */
    BsplineKnot makeKnotsClosed() {
        int n_kel = this.nKnotValues();
        int lower_idx = this.degree();
        int upper_idx = this.degree() + this.nSegments();
        double intvl;
        int i, j, k;

        double[] simple_knots = new double[n_kel];
        int[] simple_knot_multi = new int[n_kel];

        for (i = 0; i < n_kel; i++) {
            simple_knots[i] = this.knotValueAt(i);
            simple_knot_multi[i] = 1;
        }

        j = lower_idx;
        k = upper_idx;
        for (i = 0; i < this.degree(); i++) {
            intvl = simple_knots[j + 1] - simple_knots[j];
            simple_knots[k + 1] = simple_knots[k] + intvl;
            j++;
            k++;
        }

        j = lower_idx;
        k = upper_idx;
        for (i = 0; i < this.degree(); i++) {
            intvl = simple_knots[k] - simple_knots[k - 1];
            simple_knots[j - 1] = simple_knots[j] - intvl;
            j--;
            k--;
        }

        int uik =
                BsplineKnot.beautify(n_kel, simple_knots, simple_knot_multi);

        return new BsplineKnot(this.degree(), KnotType.UNSPECIFIED,
                this.isPeriodic(),
                uik, simple_knot_multi, simple_knots,
                this.nControlPoints());
    }

    /**
     * �m�b�g?���?��`����?B
     * <p/>
     * �^����ꂽ�m�b�g��ɂ��đ�?d�x��
     * knotMultiplicities �Ŗ�������Ă��Ȃ���?���?C?�����?B
     * </p>
     *
     * @param uik                �e�z���̗L��ȗv�f?�
     * @param knots              �m�b�g�l�̔z�� (�v�f?�?Fuik ��?�)
     * @param knotMultiplicities �m�b�g��?d�x�̔z�� (�v�f?�?Fuik ��?�)
     * @return ?C?����ꂽ�m�b�g��̗L��ȗv�f?�
     */
    static int beautify(int uik,
                        double[] knots,
                        int[] knotMultiplicities) {
        double kval = knots[0] - 1.0;
        double p_tol = ConditionOfOperation.getCondition().getToleranceForParameter();

        int j = 0;
        for (int i = 0; i < uik; i++) {
            if (Math.abs(kval - knots[i]) > p_tol) {
                kval = knots[j] = knots[i];
                knotMultiplicities[j] = knotMultiplicities[i];
                j++;
            } else {
                knotMultiplicities[j - 1] += knotMultiplicities[i];
            }
        }

        return j;
    }

    /**
     * �m�b�g?���?��`����?B
     * <p/>
     * �^����ꂽ�m�b�g��ɂ��đ�?d�x��
     * knotMultiplicities �Ŗ�������Ă��Ȃ���?���?C?�����?B
     * </p>
     *
     * @return ?C?����ꂽ�m�b�g��
     */
    public BsplineKnot beautify() {
        if (knotSpec != KnotType.UNSPECIFIED)
            return this;

        int uik = nKnots();
        double[] new_knots = knots();
        int[] new_knot_multi = knotMultiplicities();
        int n_new_knots = BsplineKnot.beautify(uik, new_knots, new_knot_multi);

        try {
            return new BsplineKnot(degree, knotSpec, periodic,
                    n_new_knots, new_knot_multi, new_knots,
                    nControlPoints);
        } catch (InvalidArgumentValueException e) {
            throw new FatalException();
        }
    }

    /**
     * ���̃m�b�g��𔽓]�����m�b�g���Ԃ�?B
     * <p/>
     * ���̃?�\�b�h���Ԃ��m�b�g��̃p���??[�^��`��� 0 �Ŏn�܂�?B
     * </p>
     *
     * @return ���]�����m�b�g��
     */
    public BsplineKnot reverse() {
        if (knotSpec != KnotType.UNSPECIFIED) {
            return this;    // same as this
        }

        int n_kel = nKnotValues();
        int[] new_multi = new int[n_kel];
        double[] new_knots = new double[n_kel];
        int i, j;

        new_multi[degree] = 1;
        new_knots[degree] = 0.0;

        int lk_idx = degree + nSegments();    // index of last internal knot

        for (i = degree - 1, j = lk_idx; i >= 0; i--, j++) {
            new_knots[i] = new_knots[i + 1] - (knotValueAt(j + 1) - knotValueAt(j));
            new_multi[i] = 1;
        }

        for (i = degree + 1, j = lk_idx; i < n_kel; i++, j--) {
            new_knots[i] = new_knots[i - 1] + (knotValueAt(j) - knotValueAt(j - 1));
            new_multi[i] = 1;
        }
        return new BsplineKnot(degree, knotSpec, periodic, new_multi, new_knots,
                nControlPoints).beautify();
    }

    /**
     * ����?u����?v�m�b�g���?A�^����ꂽ�m�b�g�_���J�n�_�ɂȂ�悤�ɃV�t�g����?B
     *
     * @param firstSegment �J�n�_�ƂȂ�m�b�g�_��n�_�Ƃ���Z�O�?���g�̔�?�
     * @return �^����ꂽ��?��̃Z�O�?���g�̎n�_��J�n�_�Ƃ���m�b�g��
     */
    BsplineKnot shift(int firstSegment) {
        if (this.periodic != true)
            throw new FatalException("knots should be closed form.");

        if ((firstSegment < 0) || (this.nSegments() < firstSegment))
            throw new FatalException("given index is wrong.");

        if (this.knotSpec == KnotType.UNIFORM_KNOTS)
            return this;

        int nKnots = this.nKnotValues();
        double[] newKnots = new double[nKnots];
        int[] newKnotMultiplicities = new int[nKnots];

        double upperParam = this.knotValueAt(this.degree + this.nSegments());
        double plusFactor = 0.0;
        double minusFactor = this.knotValueAt(this.degree + firstSegment) -
                this.knotValueAt(this.degree);

        for (int i = 0; i < nKnots; i++) {
            int j = i + firstSegment;
            if (j >= nKnots) {
                j += (2 * this.degree) + 1;
                plusFactor = upperParam;
            }
            newKnots[i] = this.knotValueAt(j % nKnots) + plusFactor - minusFactor;
            newKnotMultiplicities[i] = 1;
        }

        BsplineKnot knotData =
                new BsplineKnot(this.degree, this.knotSpec, true,
                        newKnotMultiplicities, newKnots, this.nControlPoints);
        return knotData.beautify();
    }

    /**
     * ���̃m�b�g��� knotSpec �� KnotType.UNSPECIFIED �ɕς����m�b�g���Ԃ�?B
     * <p/>
     * ���̃m�b�g��� knotSpec �� KnotType.UNSPECIFIED �ł���?�?��ɂ�?A��?g��Ԃ�?B
     * </p>
     *
     * @return knotSpec �� KnotType.UNSPECIFIED �ɕς����m�b�g��
     */
    BsplineKnot makeExplicit() {
        switch (knotSpec()) {
            case KnotType.UNSPECIFIED:
                return this;
            case KnotType.UNIFORM_KNOTS:
                int uik = nKnotValues();
                double[] new_knots = new double[uik];
                int[] new_knot_multi = new int[uik];
                for (int uj = 0; uj < uik; uj++) {
                    new_knots[uj] = uj - degree();
                    new_knot_multi[uj] = 1;
                }
                return new BsplineKnot(degree(), KnotType.UNSPECIFIED, periodic,
                        new_knot_multi, new_knots, nControlPoints());
        }
        throw new FatalException();
    }

    /*
    *                       n
    * �a�X�v���C������?� N (t) �̕]��
    *                       i
    */

    /**
     * ����?u���j�t�H?[����?v�m�b�g�񂪕\���a�X�v���C������?���?A
     * �^����ꂽ�p���??[�^�l�ŕ]������?B
     * <p/>
     * �]��������?��̒l��i�[���邽�߂̔z�� r �̗v�f?���?A
     * ?��Ȃ��Ƃ� (�a�X�v���C���̎�?� + 1) �łȂ���΂Ȃ�Ȃ�?B
     * </p>
     *
     * @param k0 �m�b�g���?擪�̃m�b�g�̒l
     * @param n  ��?��̎�?�
     * @param i  ���Z�̑�?ۂƂ���m�b�g�͈̔͂�?擪�̃m�b�g�̃C���f�b�N�X
     * @param t  �p���??[�^�l
     * @param r  ��?��l�̔z��
     * @see #evalBsplineU(double,double[])    ��?����?�\�b�h
     */
    private void evalUniform(int k0, int n, int i,
                             double t, double[] r) {
        int j, k;

        if (n == 0) {
            r[0] = 1.0;
        } else {
            double[] rTmp = new double[r.length - 1];

            for (j = 0; j < r.length - 1; j++) rTmp[j] = r[j + 1]; // �K�v��?
            evalUniform(k0, (n - 1), (i + 1), t, rTmp);
            for (j = 0; j < r.length - 1; j++) r[j + 1] = rTmp[j];

            for (j = 0, k = i; j <= n; j++, k++) {
                if (j == 0)
                    r[j] = (k0 + (k + n + 1) - t) * r[j + 1] / n;
                else if (j == n)
                    r[j] = (t - k0 + k) * r[j] / n;
                else
                    r[j] = ((t - k0 + k) * r[j] + (k0 + (k + n + 1) - t) * r[j + 1]) / n;
            }
        }
    }

    /**
     * ����?u���j�t�H?[����?v�m�b�g�񂪕\���a�X�v���C������?���?A
     * �^����ꂽ�p���??[�^�l�ŕ]������?B
     * <p/>
     * �]��������?��̒l��i�[���邽�߂̔z�� r �̗v�f?���?A
     * ?��Ȃ��Ƃ� (�a�X�v���C���̎�?� + 1) �łȂ���΂Ȃ�Ȃ�?B
     * </p>
     *
     * @param t �p���??[�^�l
     * @param r ��?��l�̔z��
     * @return t �ɑ΂��ėL��ƂȂ�Z�O�?���g�̔�?�
     * @see #evaluateBsplineFunction(double,double[])    ��?����?�\�b�h
     * @see #evalUniform(int,int,int,double,double[])    ��?����?�\�b�h
     */
    private int evalBsplineU(double t, double[] r) {
        int n_seg = nSegments();
        ParameterDomain dmn = getParameterDomain();
        if (!dmn.isValid(t))
            throw new FatalException();
        t = dmn.wrap(dmn.force(t));

        int isckt;
        int i;

        for (isckt = 1; !(t < isckt); isckt++)
            ;
        isckt--;
        if (isckt >= n_seg)
            isckt = n_seg - 1; // special case

        for (i = 0; i <= degree(); i++) r[i] = 0.0;
        evalUniform((-degree()), degree(), isckt, t, r);

        return isckt;
    }

    /**
     * ���̃m�b�g�񂪕\���a�X�v���C������?���?A�^����ꂽ�p���??[�^�l�ŕ]������?B
     * <p/>
     * �]��������?��̒l��i�[���邽�߂̔z�� r �̗v�f?���?A
     * ?��Ȃ��Ƃ� (�a�X�v���C���̎�?� + 1) �łȂ���΂Ȃ�Ȃ�?B
     * </p>
     *
     * @param n    ��?��̎�?�
     * @param i    ���Z�̑�?ۂƂ���m�b�g�͈̔͂�?擪�̃m�b�g�̃C���f�b�N�X
     * @param t    �p���??[�^�l
     * @param r    ��?��l�̔z��
     * @param pTol �p���??[�^�l�̋��e��?�
     * @see #evalBspline(double,double[])    ��?����?�\�b�h
     */
    private void evalNonUniform(int n, int i, double t, double[] r, double pTol) {
        double tk;
        double w1 = 0.0;
        double w2 = 0.0;
        int j, k;

        if (n == 0) {
            r[0] = 1.0;
        } else {
            double[] rTmp = new double[r.length - 1];

            for (j = 0; j < r.length - 1; j++) rTmp[j] = r[j + 1]; // �K�v��?
            evalNonUniform((n - 1), (i + 1), t, rTmp, pTol);
            for (j = 0; j < r.length - 1; j++) r[j + 1] = rTmp[j];

            for (j = 0, k = i; j <= n; j++, k++) {
                if (j != 0) {
                    if ((w1 = knotValueAt(k + n) - (tk = knotValueAt(k))) < pTol)
                        w1 = 0.0;
                    else
                        w1 = (t - tk) / w1;
                }

                if (j != n) {
                    if ((w2 = (tk = knotValueAt(k + n + 1)) - knotValueAt(k + 1)) < pTol)
                        w2 = 0.0;
                    else
                        w2 = (tk - t) / w2;
                }

                if (j == 0)
                    r[j] = w2 * r[j + 1];
                else if (j == n)
                    r[j] = w1 * r[j];
                else
                    r[j] = w1 * r[j] + w2 * r[j + 1];
            }
        }
    }

    /**
     * ���̃m�b�g�񂪕\���a�X�v���C������?���?A�^����ꂽ�p���??[�^�l�ŕ]������?B
     * <p/>
     * �]��������?��̒l��i�[���邽�߂̔z�� r �̗v�f?���?A
     * ?��Ȃ��Ƃ� (�a�X�v���C���̎�?� + 1) �łȂ���΂Ȃ�Ȃ�?B
     * </p>
     *
     * @param t �p���??[�^�l
     * @param r ��?��l�̔z��
     * @return t �ɑ΂��ėL��ƂȂ�Z�O�?���g�̔�?�
     * @see #evaluateBsplineFunction(double,double[])    ��?����?�\�b�h
     * @see #evalNonUniform(int,int,double,double[],double)    ��?����?�\�b�h
     */
    private int evalBspline(double t, double[] r) {
        int isckt;
        int i;

        int n_seg = nSegments();        // number of segments
        int n_seg_pd = n_seg + degree();    // (number of segments) + degree
        ParameterDomain dmn = getParameterDomain();
        if (!dmn.isValid(t))
            throw new FatalException();
        t = dmn.wrap(dmn.force(t));

        for (isckt = degree() + 1; isckt <= n_seg_pd; isckt++) {
            // #ifdef PAR_IS_LESS_THAN_UPPER_LIMIT
            //     if (t < knotValueAt(isckt))
            // 	break;
            // #else
            if (isckt < n_seg_pd) {
                if (t < knotValueAt(isckt))
                    break;
            } else {
                if (!(t > knotValueAt(isckt)))
                    break;
            }
            // #endif
        }
        if (isckt > n_seg_pd) {
            throw new FatalException();
        }

        isckt -= degree() + 1;

        double pTol = ConditionOfOperation.getCondition().getToleranceForParameter();
        for (i = 0; i <= degree(); i++) r[i] = 0.0;
        evalNonUniform(degree(), isckt, t, r, pTol);

        return isckt;
    }

    /**
     * ���̃m�b�g�񂪕\���a�X�v���C������?���?A�^����ꂽ�p���??[�^�l�ŕ]������?B
     * <p/>
     * �]��������?��̒l��i�[���邽�߂̔z�� r �̗v�f?���?A
     * ?��Ȃ��Ƃ� (�a�X�v���C���̎�?� + 1) �łȂ���΂Ȃ�Ȃ�?B
     * </p>
     *
     * @param t �p���??[�^�l
     * @param r ��?��l�̔z��
     * @return t �ɑ΂��ėL��ƂȂ�Z�O�?���g�̔�?�
     * @see #evalBsplineU(double,double[])    ���j�t�H?[����?�?��̉�?����?�\�b�h
     * @see #evalBspline(double,double[])    �񃆃j�t�H?[����?�?��̉�?����?�\�b�h
     */
    int evaluateBsplineFunction(double t, double[] r) {
        if (knotSpec() == KnotType.UNIFORM_KNOTS)
            return evalBsplineU(t, r);
        else
            return evalBspline(t, r);
    }

    /**
     * {@link #output(PrintWriter,int,int) output(PrintWriter, int, int)}
     * �ŎQ?Ƃ��� String �̓񎟌��z��?B
     * <p/>
     * 3 x 6 �̓񎟌��z��?B
     * <pre>
     * 		keyWords[0] : ��?�p�̕������܂ޔz��
     * 		keyWords[1] : �Ȗʂ� U ���p�̕������܂ޔz��
     * 		keyWords[2] : �Ȗʂ� V ���p�̕������܂ޔz��
     * </pre>
     * </p>
     */
    static final String[][] keyWords
            = {{"\tdegree\t", "\tnControlPoints\t", "\tknotSpec\t", "\tknotMultiplicities\t",
            "\tknots\t", "\tperiodic\t"},
            {"\tuDegree\t", "\tuNControlPoints\t", "\tuKnotSpec\t", "\tuKnotMultiplicities\t",
                    "\tuKnots\t", "\tuPeriodic\t"},
            {"\tvDegree\t", "\tvNControlPoints\t", "\tvKnotSpec\t", "\tvKnotMultiplicities\t",
                    "\tvKnots\t", "\tvPeriodic\t"}};

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     * <p/>
     * index �̒l�͈ȉ��̈Ӗ���?��?B
     * <pre>
     * 		0 : ��?�p�̕������?o�͂���
     * 		1 : �Ȗʂ� U ���p�̕������?o�͂���
     * 		2 : �Ȗʂ� V ���p�̕������?o�͂���
     * </pre>
     * </p>
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     * @param index  �L?[�??[�h��I�ⷂ�l
     */
    protected void output(PrintWriter writer, int indent, int index) {
        // make string of indent tabs
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < indent; i++) {
            buf.append("\t");
        }
        String indent_tab = new String(buf);

        // output formatted infomation of parameters
        //writer.println(indent_tab + "BsplineKnot");
        writer.println(indent_tab + keyWords[index][0] + degree);
        writer.println(indent_tab + keyWords[index][1] + nControlPoints);
        writer.println(indent_tab + keyWords[index][2] + KnotType.toString(knotSpec));
        if (knotSpec == KnotType.UNSPECIFIED) {
            // formatting knotMultiplicities
            writer.print(indent_tab + keyWords[index][3]);
            int i = 0;
            while (true) {
                writer.print(knotMultiplicities[i++]);
                for (int j = 0; j < 20 && i < knotMultiplicities.length; j++, i++) {
                    writer.print(" " + knotMultiplicities[i]);
                }
                writer.println();
                if (i < knotMultiplicities.length) {
                    writer.print(indent_tab + "\t\t");    // two tabs?
                } else {
                    break;
                }
            }
            // formatting knots
            writer.print(indent_tab + keyWords[index][4]);
            i = 0;
            while (true) {
                writer.print(knots[i++]);
                for (int j = 0; j < 3 && i < knots.length; j++, i++) {
                    writer.print(" " + knots[i]);
                }
                writer.println();
                if (i < knots.length) {
                    writer.print(indent_tab + "\t\t");    // two tabs?
                } else {
                    break;
                }
            }
        }
        writer.println(indent_tab + keyWords[index][5] + periodic);
        //writer.println(indent_tab + "End");
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param out ?o�̓X�g��?[��
     */
    public void output(OutputStream out) {
        PrintWriter writer = new PrintWriter(out, true);
        output(writer, 0, 0);
    }

    /**
     * 1 ����?���_?��� 2?A���[��?d�̃��j�t�H?[���ȃm�b�g��?B
     */
    static BsplineKnot quasiUniformKnotsOfLinearOneSegment;

    /**
     * static �ȃt�B?[���h�ɒl��?ݒ肷��?B
     */
    static {
        int[] knotMultiplicities = {2, 2};
        double[] knots = {0.0, 1.0};

        quasiUniformKnotsOfLinearOneSegment =
                new BsplineKnot(1, KnotType.UNSPECIFIED, false,
                        knotMultiplicities, knots, 2);
    }
}
