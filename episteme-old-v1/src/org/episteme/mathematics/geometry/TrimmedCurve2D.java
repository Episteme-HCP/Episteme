/*
 * �Q���� : �g������?��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: TrimmedCurve2D.java,v 1.3 2006/03/01 21:16:12 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * �Q���� : �g������?��\���N���X?B
 * <p/>
 * �g������?��?A�����?�̈ꕔ��������L��Ƃ����L��?�ł���?B
 * ����ꕔ��������L��Ƃ��邱�Ƃ�g���~���O?A
 * �L��Ƃ����Ԃ̂��Ƃ�g���~���O��ԂƂ���?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * <ul>
 * <li> �g���~���O�̑�?ۂƂȂ���?� basisCurve
 * <li> �g���~���O��Ԃ̎n�_�����?�?�̃p���??[�^�l tParam1 ���邢��?W�l tPnt1
 * <li> �g���~���O��Ԃ�?I�_�����?�?�̃p���??[�^�l tParam2 ���邢��?W�l tPnt2
 * <li> �g���~���O��Ԃ̎n�_�Ƃ��� tParam1 �� tPnt1 �̂ǂ����D?悷�邩��?�?� masterRepresentation1
 * <li> �g���~���O��Ԃ�?I�_�Ƃ��� tParam2 �� tPnt2 �̂ǂ����D?悷�邩��?�?� masterRepresentation2
 * <li>	�g������?���?�Ɠ�����ۂ����t���O senseAgreement
 * </ul>
 * ��ێ?����?B
 * </p>
 * <p/>
 * �g������?�̂�̂̒�`��͗L�Ŕ���I�Ȃ�̂ł���?A
 * �p���??[�^��Ԃ� [0, |tParam2 - tParam1|] �Ƃ���?B
 * </p>
 * <p/>
 * <a name="CONSTRAINTS">[��?��Ԃ�?S��?�?]</a>
 * </p>
 * <p/>
 * tPnt1 �� null �ł�?\��Ȃ���?A����?�?��ɂ�
 * masterRepresentation1 �̒l��
 * TrimmingPreference.PARAMETER �łȂ���΂Ȃ�Ȃ�?B
 * </p>
 * <p/>
 * ���l��
 * tPnt2 �� null �ł�?\��Ȃ���?A����?�?��ɂ�
 * masterRepresentation2 �̒l��
 * TrimmingPreference.PARAMETER �łȂ���΂Ȃ�Ȃ�?B
 * </p>
 * <p/>
 * tParam1, tParam2 ��
 * basisCurve �̃p���??[�^��`��Ɏ�܂BĂ��Ȃ���΂Ȃ�Ȃ�?B
 * </p>
 * <p/>
 * ���?����I�ł���?�?�?A
 * senseAgreement �� true  �ł���� (tParam1 &lt; tParam2)
 * senseAgreement �� false �ł���� (tParam1 &gt; tParam2)
 * �łȂ���΂Ȃ�Ȃ�?B
 * </p>
 * <p/>
 * ���?���I�ł���?�?�?A
 * senseAgreement �� true  �ł����
 * tParam2 �̒l�� (tParam1 &lt; tParam2) �𖞂����悤��
 * ���̃C���X�^���X�̓Ք�Ŏ����I��?C?������?B
 * ���l��
 * senseAgreement �� false �ł����
 * tParam1 �̒l�� (tParam1 &gt; tParam2) �𖞂����悤��
 * ���̃C���X�^���X�̓Ք�Ŏ����I��?C?������?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:12 $
 */

public class TrimmedCurve2D extends BoundedCurve2D {

    /**
     * ���?�?B
     *
     * @serial
     */
    private ParametricCurve2D basisCurve;

    /**
     * �g���~���O��Ԃ̎n�_��?W�l?B
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * </p>
     *
     * @serial
     */
    private Point2D tPnt1;

    /**
     * �g���~���O��Ԃ�?I�_��?W�l?B
     * <p/>
     * �K�v�ɉ����ăL���b�V�������?B
     * </p>
     *
     * @serial
     */
    private Point2D tPnt2;

    /**
     * �g���~���O��Ԃ̎n�_�����?�?�̃p���??[�^�l?B
     *
     * @serial
     */
    private double tParam1;

    /**
     * �g���~���O��Ԃ�?I�_�����?�?�̃p���??[�^�l?B
     *
     * @serial
     */
    private double tParam2;

    /**
     * �g���~���O��Ԃ̎n�_�Ƃ��� tParam1 �� tPnt1 �̂ǂ����D?悷�邩��?�?�?B
     *
     * @serial
     * @see TrimmingPreference
     */
    private int masterRepresentation1;

    /**
     * �g���~���O��Ԃ�?I�_�Ƃ��� tParam2 �� tPnt2 �̂ǂ����D?悷�邩��?�?�?B
     *
     * @serial
     * @see TrimmingPreference
     */
    private int masterRepresentation2;

    /**
     * �g������?���?�Ɠ�����ۂ����t���O?B
     *
     * @serial
     */
    private boolean senseAgreement;

    /**
     * �^����ꂽ�p���??[�^�l��?A
     * ���̃g������?�̕��?�̃p���??[�^��`��͈͓̔�ɂ��邩�ۂ��𒲂ׂ�?B
     * <p/>
     * param ��?A���?�̃p���??[�^��`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param ���?�ɑ΂���p���??[�^�l
     * @return �K�v�ɉ�����?A?܂�?�񂾃p���??[�^�l
     * @see AbstractParametricCurve#checkValidity(double)
     * @see ParameterDomain#wrap(double)
     * @see ParameterOutOfRange
     */
    private double checkParamValidity(double param) {
        if (basisCurve.isPeriodic()) {
            ParameterDomain domain = basisCurve.parameterDomain();
            return domain.wrap(param);
        }
        basisCurve.checkValidity(param);
        return param;
    }

    /**
     * �^����ꂽ?W�l�� (���̃g������?�̕��?�ɑ΂���) �p���??[�^�l�̊Ԃ�
     * ?�?������邩�ۂ��𒲂ׂ�?B
     * <p/>
     * pnt �� param �̊Ԃ�?�?����Ȃ�?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * pnt �� null �ł����?A���̃?�\�b�h�͉��µ�Ȃ�?B
     * </p>
     *
     * @param pnt   ?W�l
     * @param param ���?�ɑ΂���p���??[�^�l
     * @see InvalidArgumentValueException
     */
    private void checkPointValidity(Point2D pnt, double param) {
        if (pnt != null) {
            if (!basisCurve.coordinates(param).identical(pnt))
                throw new InvalidArgumentValueException();
        }
    }

    /**
     * �e�t�B?[���h�ɒl��?ݒ肷��?B
     * <p/>
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * ParameterOutOfRange �µ���� InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param basicCurve            ���?�
     * @param tPnt1                 �g���~���O��Ԃ̎n�_��?W�l
     * @param tPnt2                 �g���~���O��Ԃ�?I�_��?W�l
     * @param tParam1               �g���~���O��Ԃ̎n�_���p���??[�^�l
     * @param tParam2               �g���~���O��Ԃ�?I�_���p���??[�^�l
     * @param masterRepresentation1 tPnt1 �� tParam1 �̂ǂ����D?悷�邩���l
     * @param masterRepresentation2 tPnt2 �� tParam2 �̂ǂ����D?悷�邩���l
     * @param senseAgreement        �g������?���?�Ɠ�����ۂ����t���O
     * @see TrimmingPreference
     * @see ParameterOutOfRange
     * @see InvalidArgumentValueException
     */
    private void setParams(ParametricCurve2D basisCurve,
                           Point2D tPnt1, Point2D tPnt2,
                           double tParam1, double tParam2,
                           int masterRepresentation1,
                           int masterRepresentation2,
                           boolean senseAgreement) {
        this.basisCurve = basisCurve;
        tParam1 = checkParamValidity(tParam1);
        this.tParam1 = tParam1;
        tParam2 = checkParamValidity(tParam2);
        this.tParam2 = tParam2;
        ConditionOfOperation condition =
                ConditionOfOperation.getCondition();
        double pTol = condition.getToleranceForParameter();

        if (Math.abs(tParam2 - tParam1) < pTol)
            throw new InvalidArgumentValueException();

        checkPointValidity(tPnt1, tParam1);
        this.tPnt1 = tPnt1;
        checkPointValidity(tPnt2, tParam2);
        this.tPnt2 = tPnt2;
        this.masterRepresentation1 = masterRepresentation1;
        this.masterRepresentation2 = masterRepresentation2;

        if (basisCurve.isPeriodic()) {
            ParameterSection sec = basisCurve.parameterDomain().section();
            if (senseAgreement) {
                if (tParam1 > tParam2)
                    this.tParam2 = tParam2 += sec.increase();
            } else {
                if (tParam1 < tParam2)
                    this.tParam1 = tParam1 += sec.increase();
            }
        } else {
            if (senseAgreement) {
                if (tParam1 > tParam2)
                    throw new InvalidArgumentValueException();
            } else {
                if (tParam1 < tParam2)
                    throw new InvalidArgumentValueException();
            }
        }
        // now always senseAgreement == (tParam1 < tParam2)

        this.senseAgreement = senseAgreement;
    }

    /**
     * �e�t�B?[���h��?ݒ肷��l���̂�̂�^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * ParameterOutOfRange �µ���� InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param basicCurve            ���?�
     * @param tPnt1                 �g���~���O��Ԃ̎n�_��?W�l
     * @param tPnt2                 �g���~���O��Ԃ�?I�_��?W�l
     * @param tParam1               �g���~���O��Ԃ̎n�_���p���??[�^�l
     * @param tParam2               �g���~���O��Ԃ�?I�_���p���??[�^�l
     * @param masterRepresentation1 tPnt1 �� tParam1 �̂ǂ����D?悷�邩���l
     * @param masterRepresentation2 tPnt2 �� tParam2 �̂ǂ����D?悷�邩���l
     * @param senseAgreement        �g������?���?�Ɠ�����ۂ����t���O
     * @see TrimmingPreference
     * @see ParameterOutOfRange
     * @see InvalidArgumentValueException
     */
    TrimmedCurve2D(ParametricCurve2D basisCurve,
                   Point2D tPnt1, Point2D tPnt2,
                   double tParam1, double tParam2,
                   int masterRepresentation1,
                   int masterRepresentation2,
                   boolean senseAgreement) {
        super();
        setParams(basisCurve, tPnt1, tPnt2, tParam1, tParam2,
                masterRepresentation1, masterRepresentation2,
                senseAgreement);
    }

    /**
     * ���?�ƃg���~���O��Ԃ̗��[�_��?W�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^�̓Ք�ł�?A
     * tPnt1, tPnt2 �ɑΉ�������?�?�̃p���??[�^�l tParam1, tParam2 ��
     * �?�߂�?B����?��?�Ɏ��s���� (�܂�?A�^����ꂽ?W�l�����?�?�ɂȂ�) ?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * masterRepresentation1, masterRepresentation2 �̒l�͂Ƃ��
     * TrimmingPreference.POINT �Ƃ���?B
     * </p>
     * <p/>
     * ���̂悤�ɗp�ӂ����l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * ParameterOutOfRange �µ���� InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param basicCurve     ���?�
     * @param tPnt1          �g���~���O��Ԃ̎n�_��?W�l
     * @param tPnt2          �g���~���O��Ԃ�?I�_��?W�l
     * @param senseAgreement �g������?���?�Ɠ�����ۂ����t���O
     * @see TrimmingPreference
     * @see ParameterOutOfRange
     * @see InvalidArgumentValueException
     * @see ParametricCurve2D#pointToParameter(Point2D)
     */
    public TrimmedCurve2D(ParametricCurve2D basisCurve,
                          Point2D tPnt1, Point2D tPnt2,
                          boolean senseAgreement) {
        super();
        setParams(basisCurve, tPnt1, tPnt2,
                basisCurve.pointToParameter(tPnt1),
                basisCurve.pointToParameter(tPnt2),
                TrimmingPreference.POINT,
                TrimmingPreference.POINT,
                senseAgreement);
    }

    /**
     * ���?�ƃg���~���O��Ԃ̗��[�_�̕��?�?�ł̃p���??[�^�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ���̃R���X�g���N�^�̓Ք�ł�?A
     * tParam1, tParam �ɑΉ�������?�?��?W�l tPnt1, tPnt2 ��
     * �?�߂�?B����?��?�Ɏ��s���� (�܂�?A�^����ꂽ�p���??[�^�l��
     * ���?�̃p���??[�^��`���O��Ă���) ?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     * <p/>
     * masterRepresentation1, masterRepresentation2 �̒l�͂Ƃ��
     * TrimmingPreference.PARAMETER �Ƃ���?B
     * </p>
     * <p/>
     * ���̂悤�ɗp�ӂ����l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * ParameterOutOfRange �µ���� InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param basicCurve     ���?�
     * @param tParam1        �g���~���O��Ԃ̎n�_��\���p���??[�^�l
     * @param tParam2        �g���~���O��Ԃ�?I�_��\���p���??[�^�l
     * @param senseAgreement �g������?���?�Ɠ�����ۂ����t���O
     * @see ParametricCurve2D#coordinates(double)
     * @see TrimmingPreference
     * @see ParameterOutOfRange
     * @see InvalidArgumentValueException
     */
    public TrimmedCurve2D(ParametricCurve2D basisCurve,
                          double tParam1, double tParam2,
                          boolean senseAgreement) {
        super();
        setParams(basisCurve,
                basisCurve.coordinates(tParam1),
                basisCurve.coordinates(tParam2),
                tParam1, tParam2,
                TrimmingPreference.PARAMETER,
                TrimmingPreference.PARAMETER,
                senseAgreement);
    }

    /**
     * ���?�ƃg���~���O��Ԃ�\���p���??[�^��ԗ^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * pint �̊J�n�l�� tParam1?A?I���l�� tParam2 �Ƃ���?B
     * �܂� pint �̑?���l��?��ł���� senseAgreement �� true?A
     * �����łȂ���� senseAgreement �� false �Ƃ���?B
     * </p>
     * <p/>
     * tPnt1, tPnt2 �̒l�͂Ƃ�� null �Ƃ���?B
     * </p>
     * <p/>
     * masterRepresentation1, masterRepresentation2 �̒l�͂Ƃ��
     * TrimmingPreference.PARAMETER �Ƃ���?B
     * </p>
     * <p/>
     * ���̂悤�ɗp�ӂ����l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * ParameterOutOfRange �µ���� InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param basicCurve ���?�
     * @param pint       �g���~���O��Ԃ�\���p���??[�^���
     * @see TrimmingPreference
     * @see ParameterOutOfRange
     * @see InvalidArgumentValueException
     */
    public TrimmedCurve2D(ParametricCurve2D basisCurve,
                          ParameterSection pint) {
        super();
        setParams(basisCurve, null, null, pint.start(), pint.end(),
                TrimmingPreference.PARAMETER,
                TrimmingPreference.PARAMETER,
                pint.increase() > 0);
    }

    /**
     * ���̃g������?�̕��?��Ԃ�?B
     *
     * @return ���?�
     */
    public ParametricCurve2D basisCurve() {
        return this.basisCurve;
    }

    /**
     * ���̃g������?�̃g���~���O��Ԃ̎n�_��?W�l��Ԃ�?B
     * <p/>
     * �L���b�V����?s�킸?A��?ݒl�⻂̂܂ܕԂ�?B
     * </p>
     *
     * @return �g���~���O��Ԃ̎n�_��?W�l
     */
    public Point2D tPnt1() {
        return this.tPnt1;
    }

    /**
     * ���̃g������?�̃g���~���O��Ԃ�?I�_��?W�l��Ԃ�?B
     * <p/>
     * �L���b�V����?s�킸?A��?ݒl�⻂̂܂ܕԂ�?B
     * </p>
     *
     * @return �g���~���O��Ԃ�?I�_��?W�l
     */
    public Point2D tPnt2() {
        return this.tPnt2;
    }

    /**
     * ���̃g������?�̃g���~���O��Ԃ̎n�_�����?�?�̃p���??[�^�l��Ԃ�?B
     *
     * @return �g���~���O��Ԃ̎n�_�����?�?�̃p���??[�^�l
     */
    public double tParam1() {
        return this.tParam1;
    }

    /**
     * ���̃g������?�̃g���~���O��Ԃ�?I�_�����?�?�̃p���??[�^�l��Ԃ�?B
     *
     * @return �g���~���O��Ԃ�?I�_�����?�?�̃p���??[�^�l
     */
    public double tParam2() {
        return this.tParam2;
    }

    /**
     * ���̃g������?�� masterRepresentation1 ��Ԃ�?B
     *
     * @return masterRepresentation1 �̒l
     * @see TrimmingPreference
     */
    public int masterRepresentation1() {
        return this.masterRepresentation1;
    }

    /**
     * ���̃g������?�� masterRepresentation2 ��Ԃ�?B
     *
     * @return masterRepresentation2 �̒l
     * @see TrimmingPreference
     */
    public int masterRepresentation2() {
        return this.masterRepresentation2;
    }

    /**
     * ���̃g������?�� senseAgreement ��Ԃ�?B
     *
     * @return �g������?���?�Ɠ�����Ȃ�� true?A����Ȃ��� false
     */
    public boolean senseAgreement() {
        return this.senseAgreement;
    }

    /**
     * ���̃g������?�ɑ΂��ė^����ꂽ�p���??[�^�l��?A
     * ���?�ɑ΂���p���??[�^�l�ɕϊ�����?B
     * <p/>
     * �^����ꂽ�p���??[�^�l�����̋�?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �g������?�ɑ΂���p���??[�^�l
     * @return ���?�ɑ΂���p���??[�^�l
     */
    public double toBasisParameter(double param) {
        checkValidity(param);
        if (senseAgreement) {
            // s = t - t1, i.e. t = s + t1
            return param + tParam1;
        } else {
            // s = t1 - t, i.e. t = t1 - s
            return tParam1 - param;
        }
    }

    /**
     * ���̃g������?�ɑ΂��ė^����ꂽ�p���??[�^��Ԃ�?A
     * ���?�ɑ΂���p���??[�^��Ԃɕϊ�����?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ����̋�?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �g������?�ɑ΂���p���??[�^���
     * @return ���?�ɑ΂���p���??[�^���
     */
    public ParameterSection toBasisParameter(ParameterSection pint) {
        double start = toBasisParameter(pint.start());
        double end = toBasisParameter(pint.end());

        return new ParameterSection(start, end - start);
    }

    /**
     * ���̃g������?�̕��?�ɑ΂��ė^����ꂽ�p���??[�^�l��?A
     * ���̃g������?�ɑ΂���p���??[�^�l�ɕϊ�����?B
     *
     * @param param ���?�ɑ΂���p���??[�^�l
     * @return �g������?�ɑ΂���p���??[�^�l
     */
    public double toOwnParameter(double param) {
        if (basisCurve.isPeriodic()) {
            double absInc = basisCurve.parameterDomain().section().absIncrease();
            if (senseAgreement == true) {
                while (param < tParam1)
                    param += absInc;
                while (tParam2 < param)
                    param -= absInc;
                if (param < tParam1) {
                    if ((tParam1 - param) > ((param + absInc) - tParam2))
                        param += absInc;
                }
            } else {
                while (param < tParam2)
                    param += absInc;
                while (tParam1 < param)
                    param -= absInc;
                if (param < tParam2) {
                    if ((tParam2 - param) > ((param + absInc) - tParam1))
                        param += absInc;
                }
            }
        }

        return (senseAgreement) ? (param - tParam1) : (tParam1 - param);
    }

    /**
     * �^����ꂽ�p���??[�^��Ԃɂ����邱�̋�?�̎��?�ł̒��� (���̂�) ��Ԃ�?B
     * <p/>
     * pint �̑?���l�͕��ł©�܂�Ȃ�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ����̋�?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param pint ��?�̒�����?�߂�p���??[�^���
     * @return �w�肳�ꂽ�p���??[�^��Ԃɂ������?�̒���
     * @see ParameterOutOfRange
     */
    public double length(ParameterSection pint) {
        return basisCurve.length(toBasisParameter(pint));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ?W�l
     * @see ParameterOutOfRange
     */
    public Point2D coordinates(double param) {
        return basisCurve.coordinates(toBasisParameter(param));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ?ڃx�N�g��
     * @see ParameterOutOfRange
     */
    public Vector2D tangentVector(double param) {
        Vector2D tang = basisCurve.tangentVector(toBasisParameter(param));

        if (senseAgreement)
            return tang;
        else
            return tang.multiply(-1);
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̋ȗ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return �ȗ�
     * @see ParameterOutOfRange
     */
    public CurveCurvature2D curvature(double param) {
        return basisCurve.curvature(toBasisParameter(param));
    }

    /**
     * ���̋�?��?A�^����ꂽ�p���??[�^�l�ł̓���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param param �p���??[�^�l
     * @return ����?�
     * @see ParameterOutOfRange
     */
    public CurveDerivative2D evaluation(double param) {
        CurveDerivative2D curv =
                basisCurve.evaluation(toBasisParameter(param));
        if (senseAgreement)
            return curv;
        else {
            CurveDerivative2D rcurv =
                    new CurveDerivative2D(curv.d0D(), curv.d1D().multiply(-1),
                            curv.d2D());
            return rcurv;
        }
    }

    /**
     * ���炩�̉��Z�ɂ�Bē���ꂽ�p���??[�^�l��?A
     * ���̋�?�̒�`���ɋ�?��I�Ɏ�߂�?B
     * <p/>
     * �l����`�悩��O�ꂷ���Ă���?A
     * ��?��I�Ɏ�߂邱�Ƃ��K���łȂ�?�?��ɂ�?ADouble.NaN ��Ԃ�?B
     * </p>
     *
     * @param param ���炩�̉��Z�ɂ�Bē���ꂽ�p���??[�^�l
     * @return ��`���Ɏ�߂��p���??[�^�l
     * @see ParametricCurve2D#parameterValidity(double)
     */
    private double forceComputedParameter(double param) {
        switch (parameterValidity(param)) {
            case ParameterValidity.OUTSIDE:
                param = Double.NaN;
                break;

            case ParameterValidity.TOLERATED_LOWER_LIMIT:
                param = parameterDomain().section().lower();
                break;

            case ParameterValidity.TOLERATED_UPPER_LIMIT:
                param = parameterDomain().section().upper();
                break;

            default:
                break;
        }

        return param;
    }

    /**
     * ���̋�?�̓Hٓ_��Ԃ�?B
     * <p/>
     * �Hٓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �Hٓ_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnCurve2D[] singular() throws IndefiniteSolutionException {
        Vector singularVec = new Vector();
        PointOnCurve2D[] singular = basisCurve.singular();
        ParameterDomain domain = parameterDomain();

        for (int i = 0; i < singular.length; i++) {
            double param = toOwnParameter(singular[i].parameter());
            param = forceComputedParameter(param);

            if (Double.isNaN(param) == true)
                continue;

            try {
                singularVec.addElement(new PointOnCurve2D(this, param, doCheckDebug));
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

        }
        PointOnCurve2D[] thisSingular =
                new PointOnCurve2D[singularVec.size()];
        singularVec.copyInto(thisSingular);
        return thisSingular;
    }

    /**
     * ���̋�?�̕ϋȓ_��Ԃ�?B
     * <p/>
     * �ϋȓ_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @return �ϋȓ_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnCurve2D[] inflexion() throws IndefiniteSolutionException {
        Vector inflexionVec = new Vector();
        PointOnCurve2D[] inflexion = basisCurve.inflexion();
        ParameterDomain domain = parameterDomain();

        for (int i = 0; i < inflexion.length; i++) {
            double param = toOwnParameter(inflexion[i].parameter());
            param = forceComputedParameter(param);

            if (Double.isNaN(param) == true)
                continue;

            try {
                inflexionVec.addElement
                        (new PointOnCurve2D(this, param, doCheckDebug));
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

        }
        PointOnCurve2D[] thisInflexion =
                new PointOnCurve2D[inflexionVec.size()];
        inflexionVec.copyInto(thisInflexion);
        return thisInflexion;
    }

    /**
     * �^����ꂽ�_���炱�̋�?�ւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnCurve2D[] projectFrom(Point2D point)
            throws IndefiniteSolutionException {
        Vector projvec = new Vector();
        PointOnCurve2D[] proj = basisCurve.projectFrom(point);
        ParameterDomain domain = parameterDomain();

        for (int i = 0; i < proj.length; i++) {
            PointOnCurve2D proj2;
            double param = toOwnParameter(proj[i].parameter());
            param = forceComputedParameter(param);

            if (Double.isNaN(param) == true)
                continue;

            try {
                proj2 = new PointOnCurve2D(this, param, doCheckDebug);
            } catch (InvalidArgumentValueException e) {
                throw new FatalException();
            }

            projvec.addElement(proj2);
        }
        PointOnCurve2D[] prj = new PointOnCurve2D[projvec.size()];
        projvec.copyInto(prj);
        return prj;
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A�^����ꂽ��?��Œ�?�ߎ�����|�����C����Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����|�����C����?\?�����_��
     * ���̋�?��x?[�X��?�Ƃ��� PointOnCurve2D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * section �̒l��?A���̃x�W�G��?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param section   ��?�ߎ�����p���??[�^���
     * @param tolerance �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ�?�ߎ�����|�����C��
     * @see ParameterOutOfRange
     */
    public Polyline2D toPolyline(ParameterSection pint,
                                 ToleranceForDistance tol) {
        Polyline2D pl = basisCurve.toPolyline(toBasisParameter(pint), tol);
        Point2D[] pnts = new Point2D[pl.nPoints()];

        for (int i = 0; i < pnts.length; i++) {
            PointOnCurve2D p = (PointOnCurve2D) pl.pointAt(i);
            pnts[i] = new PointOnCurve2D(this, toOwnParameter(p.parameter()), doCheckDebug);
        }

        return new Polyline2D(pnts);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ쵖���?Č�����L�? Bspline ��?��Ԃ�?B
     * <p/>
     * pint �̒l��?A���̂a�X�v���C����?�̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param pint �L�? Bspline ��?��?Č�����p���??[�^���
     * @return ���̋�?�̎w��̋�Ԃ�?Č�����L�? Bspline ��?�
     * @see ParameterOutOfRange
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        return basisCurve.toBsplineCurve(toBasisParameter(pint));
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * ���̋�?�̕��?�Ƒ��̋�?�̌�_��?�߂����?A
     * �����̓�ł��̋�?�̃g���~���O��Ԃ̓Ѥ�Ɏ�܂BĂ�����
     * ��?u��_?v�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?�
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     */
    private IntersectionPoint2D[] doIntersect(ParametricCurve2D mate,
                                              boolean doExchange) {
        Vector intsvec = new Vector();

        IntersectionPoint2D[] ints;
        try {
            ints = basisCurve.intersect(mate);
        } catch (IndefiniteSolutionException e) {
            ints = new IntersectionPoint2D[0];    // ??
        }
        ParameterDomain domain = parameterDomain();

        for (int i = 0; i < ints.length; i++) {
            double param = toOwnParameter(ints[i].pointOnCurve1().parameter());

            param = forceComputedParameter(param);

            if (Double.isNaN(param) == true)
                continue;

            PointOnCurve2D pnts = new PointOnCurve2D(this, param, doCheckDebug);

            IntersectionPoint2D ints2 =
                    new IntersectionPoint2D(pnts, ints[i].pointOnCurve2(), doCheckDebug);

            intsvec.addElement(ints2);
        }

        IntersectionPoint2D[] xints =
                new IntersectionPoint2D[intsvec.size()];
        intsvec.copyInto(xints);
        if (doExchange)
            for (int i = 0; i < xints.length; i++)
                xints[i] = xints[i].exchange();

        return xints;
    }

    /**
     * ���̋�?�Ƒ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate) {
        return doIntersect(mate, false);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�ȉ~) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�ȉ~)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�o��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�o��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�|�����C��) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�|�����C��)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (�g������?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (�g������?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�Z�O�?���g) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�Z�O�?���g)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���̋�?�Ƒ��̋�?� (��?���?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̋�?� (��?���?�)
     * @param doExchange ��_�� pointOnCurve1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @see #doIntersect(ParametricCurve2D,boolean)
     */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     * ���?�ɑ΂���p���??[�^�l����
     * �g������?�ɑ΂���p���??[�^�l�ւ̕ϊ�?��?��\���Ք�N���X?B
     *
     * @see #interfere(BoundedCurve2D,boolean)
     */
    class ToTrimConversion extends ParameterConversion2D {
        /**
         * �g������?�?B
         */
        TrimmedCurve2D curve;

        /**
         * �g������?��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param curve �g������?�
         */
        ToTrimConversion(TrimmedCurve2D curve) {
            this.curve = curve;
        }

        /**
         * ���?�ɑ΂���p���??[�^�l��g������?�ɑ΂���p���??[�^�l�֕ϊ�����?B
         *
         * @param param ���?�ɑ΂���p���??[�^�l
         * @return �g������?�ɑ΂���p���??[�^�l
         */
        double convParameter(double param) {
            return curve.toOwnParameter(param);
        }

        /**
         * ���?�ɑ΂���p���??[�^�l��ϊ������?ۂł���g������?��Ԃ�?B
         *
         * @param param ���?�ɑ΂���p���??[�^�l
         * @return �g������?�
         */
        ParametricCurve2D convCurve(double param) {
            return curve;
        }
    }

    /**
     * �a�X�v���C����?�ɑ΂���p���??[�^�l����
     * �g������?�ɑ΂���p���??[�^�l�ւ̕ϊ�?��?��\���Ք�N���X?B
     *
     * @see #interfere(BoundedCurve2D,boolean)
     */
    class BsplineConversion extends ParameterConversion2D {
        /**
         * �g������?�?B
         */
        TrimmedCurve2D curve;

        /**
         * �a�X�v���C����?�?B
         */
        BoundedCurve2D bcurve;

        /**
         * �g������?�Ƃa�X�v���C����?��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param curve  �g������?�
         * @param bcurve �a�X�v���C����?�
         */
        BsplineConversion(TrimmedCurve2D curve,
                          BoundedCurve2D bcurve) {
            this.curve = curve;
            this.bcurve = bcurve;
        }

        /**
         * �a�X�v���C����?�ɑ΂���p���??[�^�l��g������?�ɑ΂���p���??[�^�l�֕ϊ�����?B
         * <p/>
         * �^����ꂽ�p���??[�^�l�ɑΉ�����a�X�v���C����?�?�̓_��
         * �g������?�̕��?��?�BĂ��Ȃ�?�?��ɂ�
         * InvalidArgumentValueException �̗�O��?�����?B
         * </p>
         *
         * @param param �a�X�v���C����?�ɑ΂���p���??[�^�l
         * @return �g������?�ɑ΂���p���??[�^�l
         * @see ParametricCurve2D#pointToParameter(Point2D)
         * @see InvalidArgumentValueException
         */
        double convParameter(double param) {
            PointOnCurve2D pnt = new PointOnCurve2D(bcurve, param, doCheckDebug);
            double bparam = curve.basisCurve().pointToParameter(pnt);
            return curve.toOwnParameter(bparam);
        }

        /**
         * �a�X�v���C����?�ɑ΂���p���??[�^�l��ϊ������?ۂł���g������?��Ԃ�?B
         *
         * @param param �a�X�v���C����?�ɑ΂���p���??[�^�l
         * @return �g������?�
         */
        ParametricCurve2D convCurve(double param) {
            return curve;
        }
    }

    /**
     * ���̗L��?�Ƒ��̗L��?�̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ���?�̊�?̔z��
     */
    public CurveCurveInterference2D[] interfere(BoundedCurve2D mate) {
        return this.interfere(mate, false);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?�̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋�?�̕��?�Ƒ��̗L��?�̊�?�?�߂����?A
     * �����̓�̂��̋�?�̃g���~���O��Ԃ̓Ѥ�Ɏ�܂BĂ��镔��
     * ��?u��?�?v�Ƃ��Ă���?B
     * �Ȃ�?A���̋�?�̕��?�L�łȂ�?�?��ɂ�?A
     * ���?�̃g���~���O��Ԃ�L�?�a�X�v���C����?�ɕϊ���?A
     * ���̗L�?�a�X�v���C����?�Ƒ��̗L��?�̊�?�x?[�X�̊�?Ƃ���?B
     * </p>
     *
     * @param mate       ���̋�?�
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ���?�̊�?̔z��
     * @see TrimmedCurve2D.ToTrimConversion
     * @see TrimmedCurve2D.BsplineConversion
     */
    CurveCurveInterference2D[] interfere(BoundedCurve2D mate,
                                         boolean doExchange) {
        CurveCurveInterferenceList intfList
                = new CurveCurveInterferenceList(this, mate);
        ParameterDomain domain = parameterDomain();
        BoundedCurve2D bcurve;
        ParameterConversion2D conv;
        ParameterSection sec =
                new ParameterSection(tParam1, tParam2 - tParam1);

        if (basisCurve instanceof BoundedCurve2D) {
            bcurve = (BoundedCurve2D) basisCurve;
            conv = new ToTrimConversion(this);
        } else {
            bcurve = basisCurve.toBsplineCurve(sec);
            conv = new BsplineConversion(this, bcurve);
        }

        CurveCurveInterference2D[] intf;
        if (!doExchange) {
            intf = bcurve.interfere(mate);
        } else {
            intf = mate.interfere(bcurve);
        }

        Vector vec = new Vector();

        for (int i = 0; i < intf.length; i++) {
            CurveCurveInterference2D trimintf;
            if (!doExchange) {
                trimintf = intf[i].trim1(sec, conv);
            } else {
                trimintf = intf[i].trim2(sec, conv);
            }
            if (trimintf != null)
                vec.addElement(trimintf);
        }
        CurveCurveInterference2D[] interf =
                new CurveCurveInterference2D[vec.size()];
        vec.copyInto(interf);
        return interf;
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(BoundedLine2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�|�����C��) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�|�����C��)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(Polyline2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�x�W�G��?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�x�W�G��?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(PureBezierCurve2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�a�X�v���C����?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�a�X�v���C����?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(BsplineCurve2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (�g������?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (�g������?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(TrimmedCurve2D mate,
                                         boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�Z�O�?���g) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�Z�O�?���g)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(CompositeCurveSegment2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̗L��?�Ƒ��̗L��?� (��?���?�) �̊�?�?�߂�?B
     * <p/>
     * ��?���?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param mate       ���̗L��?� (��?���?�)
     * @param doExchange ��?�?���� this �� mate �̈ʒu��귂��邩�ǂ���
     * @return ��?�?��
     * @see #interfere(BoundedCurve2D,boolean)
     */
    CurveCurveInterference2D[] interfere(CompositeCurve2D mate,
                                         boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ����� Bspline ��?��?�߂�?B
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ����� Bspline ��?�
     * @see WhichSide
     */
    public BsplineCurve2D
    offsetByBsplineCurve(ParameterSection pint,
                         double magni,
                         int side,
                         ToleranceForDistance tol) {
        Ofst2D doObj = new Ofst2D(this, pint, magni, side, tol);
        return doObj.offset();
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�I�t�Z�b�g������?��?A
     * �^����ꂽ��?��ŋߎ�����L��?��?�߂�?B
     *
     * @param pint  �I�t�Z�b�g����p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.LEFT/RIGHT)
     * @param tol   �����̋��e��?�
     * @return ���̋�?�̎w��̋�Ԃ̃I�t�Z�b�g��?��ߎ�����L��?�
     * @see WhichSide
     */
    public BoundedCurve2D
    offsetByBoundedCurve(ParameterSection pint,
                         double magni,
                         int side,
                         ToleranceForDistance tol) {
        ParameterSection basisPint = this.toBasisParameter(pint);
        int basisSide = (this.senseAgreement() == true)
                ? side : WhichSide.reverse(side);

        return this.basisCurve().offsetByBoundedCurve(basisPint, magni, basisSide, tol);
    }

    /**
     * ���̋�?�̎w��̋�Ԃ�?A���̋�?�̎w��̋�Ԃɂ�����t�B���b�g��?�߂�?B
     * <p/>
     * �t�B���b�g����?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param pint1      ���̋�?�̃p���??[�^���
     * @param side1      ���̋�?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *                   (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param mate       ���̋�?�
     * @param pint2      ���̋�?�̃p���??[�^���
     * @param side2      ���̋�?�̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *                   (WhichSide.LEFT�Ȃ��?���?ARIGHT�Ȃ�ΉE��?ABOTH�Ȃ�Η���)
     * @param radius     �t�B���b�g���a
     * @param doExchange �t�B���b�g�� point1/2 ��귂��邩�ǂ���
     * @return �t�B���b�g�̔z��
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     */
    FilletObject2D[]
    doFillet(ParameterSection pint1, int side1, ParametricCurve2D mate,
             ParameterSection pint2, int side2, double radius,
             boolean doExchange)
            throws IndefiniteSolutionException {
        Vector intsvec = new Vector();

        FilletObject2D[] flts;
        try {
            flts = basisCurve.fillet(toBasisParameter(pint1), side1, mate, pint2, side2, radius);
        } catch (IndefiniteSolutionException e) {
            flts = new FilletObject2D[1];
            flts[0] = (FilletObject2D) e.suitable();
        }
        ParameterDomain domain = parameterDomain();

        for (int i = 0; i < flts.length; i++) {
            double param = toOwnParameter(flts[i].pointOnCurve1().parameter());

            PointOnCurve2D pnt = new PointOnCurve2D(this, param, doCheckDebug);

            if (!doExchange)
                flts[i] = new FilletObject2D(radius, flts[i].center(), pnt, flts[i].pointOnCurve2());
            else
                flts[i] = new FilletObject2D(radius, flts[i].center(), flts[i].pointOnCurve2(), pnt);
        }
        return flts;
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̋���?�?��?�߂�?B
     * <p/>
     * ����?�?�?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����_�ł͎�����Ă��Ȃ�����?A
     * UnsupportedOperationException	�̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ����?�?�̔z��
     * @throws UnsupportedOperationException ���܂̂Ƃ���?A������Ȃ��@�\�ł���
     */
    public CommonTangent2D[] commonTangent(ParametricCurve2D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�Ƒ��̋�?�Ƃ̋��ʖ@?��?�߂�?B
     * <p/>
     * ���ʖ@?�?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * �����_�ł͎�����Ă��Ȃ�����?A
     * UnsupportedOperationException	�̗�O��?�����?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ���ʖ@?�̔z��
     * @throws UnsupportedOperationException ���܂̂Ƃ���?A������Ȃ��@�\�ł���
     */
    public CommonNormal2D[] commonNormal(ParametricCurve2D mate) {
        throw new UnsupportedOperationException();
    }

    /**
     * ���̋�?�̃p���??[�^��`���Ԃ�?B
     * <p/>
     * [0, |tParam2 - tParam1|] ��Ԃ�?B
     * </p>
     *
     * @return �L�Ŕ���I�ȃp���??[�^��`��
     */
    ParameterDomain getParameterDomain() {
        try {
            return new ParameterDomain(false, 0,
                    Math.abs(tParam2 - tParam1));
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ���?�R�`?�ł���� true?A�����łȂ���� false
     */
    public boolean isFreeform() {
        return this.basisCurve.isFreeform();
    }

    /**
     * ���̗L��?�̊J�n�_��Ԃ�?B
     * <p/>
     * masterRepresentation1 �� PARAMETER �Ȃ��?A���?�� tParam1 �ŕ]�������l��Ԃ�?B
     * masterRepresentation1 �� POINT �Ȃ��?AtPnt1 ��Ԃ�?B
     * </p>
     *
     * @return �J�n�_
     */
    public Point2D startPoint() {
        Point2D pnt1;

        try {
            if (this.tPnt1 == null ||
                    masterRepresentation1 == TrimmingPreference.PARAMETER)
                pnt1 = basisCurve.coordinates(tParam1);
            else
                pnt1 = this.tPnt1;
        } catch (ParameterOutOfRange e) {
            // should never be occurred
            throw new FatalException();
        }
        if (this.tPnt1 == null)
            this.tPnt1 = pnt1;
        return pnt1;
    }

    /**
     * ���̗L��?��?I���_��Ԃ�?B
     * <p/>
     * masterRepresentation2 �� PARAMETER �Ȃ��?A���?�� tParam2 �ŕ]�������l��Ԃ�?B
     * masterRepresentation2 �� POINT �Ȃ��?AtPnt2 ��Ԃ�?B
     * </p>
     *
     * @return ?I���_
     */
    public Point2D endPoint() {
        Point2D pnt2;

        try {
            if (this.tPnt2 == null ||
                    masterRepresentation2 == TrimmingPreference.PARAMETER)
                pnt2 = basisCurve.coordinates(tParam2);
            else
                pnt2 = this.tPnt2;
        } catch (ParameterOutOfRange e) {
            // should never be occurred
            throw new FatalException();
        }
        if (this.tPnt2 == null)
            this.tPnt2 = pnt2;
        return pnt2;
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricCurve2D#TRIMMED_CURVE_2D ParametricCurve2D.TRIMMED_CURVE_2D}
     */
    int type() {
        return TRIMMED_CURVE_2D;
    }

    /**
     * ���̋�?��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    protected synchronized ParametricCurve2D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator2D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        ParametricCurve2D tBasisCurve =
                this.basisCurve().transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        Point2D tTPnt1 = null;
        Point2D tTPnt2 = null;
        if (this.masterRepresentation1() == TrimmingPreference.POINT)
            tTPnt1 = this.tPnt1().transformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);
        if (this.masterRepresentation2() == TrimmingPreference.POINT)
            tTPnt2 = this.tPnt2().transformBy(reverseTransform,
                    transformationOperator,
                    transformedGeometries);

        return new TrimmedCurve2D(tBasisCurve,
                tTPnt1, tTPnt2,
                this.tParam1(), this.tParam2(),
                this.masterRepresentation1(),
                this.masterRepresentation2(),
                this.senseAgreement());
    }

    /**
     * ���̋�?�|�����C���̕�����܂ނ��ۂ���Ԃ�?B
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C����܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean hasPolyline() {
        return basisCurve.hasPolyline();
    }

    /**
     * ���̋�?�|�����C���̕��������łł��Ă��邩�ۂ���Ԃ�?B
     *
     * @return ���̋�?�|�����C���ł��邩?A �܂��͎�?g��?\?����镔�i�Ƃ��ă|�����C��������܂ނȂ�� true?A
     *         �����łȂ���� false
     */
    protected boolean isComposedOfOnlyPolylines() {
        return basisCurve.isComposedOfOnlyPolylines();
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
        writer.println(indent_tab + "\tbasisCurve");
        basisCurve.output(writer, indent + 2);
        if (tPnt1 != null) {
            writer.println(indent_tab + "\ttPnt1");
            tPnt1.output(writer, indent + 2);
        }
        if (tPnt2 != null) {
            writer.println(indent_tab + "\ttPnt2");
            tPnt2.output(writer, indent + 2);
        }
        writer.println(indent_tab + "\ttParam1\t" + tParam1);
        writer.println(indent_tab + "\ttParam2\t" + tParam2);
        writer.println(indent_tab + "\tmasterRepresentation1\t"
                + TrimmingPreference.toString(masterRepresentation1));
        writer.println(indent_tab + "\tmasterRepresentation2\t"
                + TrimmingPreference.toString(masterRepresentation2));
        writer.println(indent_tab + "\tsenseAgreement\t" + senseAgreement);
        writer.println(indent_tab + "End");
    }
}
