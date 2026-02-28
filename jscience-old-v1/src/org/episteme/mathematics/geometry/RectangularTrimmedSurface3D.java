/*
 * �R���� : ��`�̃g�����Ȗʂ�\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: RectangularTrimmedSurface3D.java,v 1.3 2006/03/01 21:16:09 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
 * �R���� : ��`�̃g�����Ȗʂ�\���N���X?B
 * <p/>
 * ��`�̃g�����Ȗʂ�?A����Ȗʂ� (u, v) �p���??[�^�I�ɋ�`�Ȉꕔ��������L��Ƃ����L�Ȗʂł���?B
 * ����ꕔ��������L��Ƃ��邱�Ƃ�g���~���O?A
 * �L��Ƃ����Ԃ̂��Ƃ�g���~���O��ԂƂ���?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * <ul>
 * <li> �g���~���O�̑�?ۂƂȂ��Ȗ� basisSurface
 * <li> �g���~���O��Ԃ� U ���̎n�_��\����Ȗ�?�̃p���??[�^�l uParam1
 * <li> �g���~���O��Ԃ� U ����?I�_��\����Ȗ�?�̃p���??[�^�l uParam2
 * <li>	�g�����Ȗʂ� U ���?i?s����ȖʂƓ�����ۂ����t���O uSense
 * <li> �g���~���O��Ԃ� V ���̎n�_��\����Ȗ�?�̃p���??[�^�l vParam1
 * <li> �g���~���O��Ԃ� V ����?I�_��\����Ȗ�?�̃p���??[�^�l vParam2
 * <li>	�g�����Ȗʂ� V ���?i?s����ȖʂƓ�����ۂ����t���O vSense
 * </ul>
 * ��ێ?����?B
 * </p>
 * <p/>
 * �g�����Ȗʂ��̂�̂� U ���̒�`��͗L�Ŕ���I�Ȃ�̂ł���?A
 * �p���??[�^��Ԃ� [0, |uParam2 - uParam1|] �Ƃ���?B
 * ���l��?A
 * V ���̒�`���L�Ŕ���I�Ȃ�̂ł���?A
 * �p���??[�^��Ԃ� [0, |vParam2 - vParam1|] �Ƃ���?B
 * </p>
 * <p/>
 * <a name="CONSTRAINTS">[��?��Ԃ�?S��?�?]</a>
 * </p>
 * <p/>
 * basisSurface �͊J�����`���ł��邩?A�µ����
 * �g���~���O��Ԃ������`���̕��̎��̋��E��ׂ��ł��Ȃ���̂Ƃ���?B
 * </p>
 * <p/>
 * uParam1, uParam2 ����� vParam1, vParam2 ��
 * basisSurface �̃p���??[�^��`��Ɏ�܂BĂ��Ȃ���΂Ȃ�Ȃ�?B
 * </p>
 * <p/>
 * ��Ȗʂ� U ������I�ł���?�?�?A
 * uSense �� true  �ł���� (uParam1 &lt; uParam2)
 * uSense �� false �ł���� (uParam1 &gt; uParam2)
 * �łȂ���΂Ȃ�Ȃ�?B
 * <br>
 * V ���ɂ��Ă̒l�ɂӯ�l��?�?����?݂���?B
 * </p>
 * <p/>
 * ��Ȗʂ� U �����I�ł���?�?�?A
 * uSense �� true  �ł����
 * uParam2 �̒l�� (uParam1 &lt; uParam2) �𖞂����悤��
 * ���̃C���X�^���X�̓Ք�Ŏ����I��?C?������?B
 * ���l��
 * uSense �� false �ł����
 * uParam1 �̒l�� (uParam1 &gt; uParam2) �𖞂����悤��
 * ���̃C���X�^���X�̓Ք�Ŏ����I��?C?������?B
 * <br>
 * V ���ɂ��Ă̒l�ɂӯ�l��?�?����?݂���?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/01 21:16:09 $
 */
public class RectangularTrimmedSurface3D extends BoundedSurface3D {
    /**
     * ��Ȗ�?B
     *
     * @serial
     */
    ParametricSurface3D basisSurface;

    /**
     * �g���~���O��Ԃ� U ���̎n�_��\����Ȗ�?�̃p���??[�^�l?B
     *
     * @serial
     */
    private double uParam1;

    /**
     * �g���~���O��Ԃ� U ����?I�_��\����Ȗ�?�̃p���??[�^�l?B
     *
     * @serial
     */
    private double uParam2;

    /**
     * �g�����Ȗʂ� U ����?i?s����ȖʂƓ�����ۂ����t���O?B
     * <p/>
     * true �Ȃ�Γ�����?Afalse �Ȃ�΋t��?B
     * </p>
     *
     * @serial
     */
    boolean uSense;

    /**
     * �g���~���O��Ԃ� V ���̎n�_��\����Ȗ�?�̃p���??[�^�l?B
     *
     * @serial
     */
    private double vParam1;

    /**
     * �g���~���O��Ԃ� V ����?I�_��\����Ȗ�?�̃p���??[�^�l?B
     *
     * @serial
     */
    private double vParam2;

    /**
     * �g�����Ȗʂ� V ����?i?s����ȖʂƓ�����ۂ����t���O?B
     * <p/>
     * true �Ȃ�Γ�����?Afalse �Ȃ�΋t��?B
     * </p>
     *
     * @serial
     */
    boolean vSense;

    /**
     * �g���~���O��� �� U ���̕�Ȗ�?�ł̃p���??[�^���?B
     * <p/>
     * ���̃t�B?[���h��?A���̃N���X�̓Ք�ł̂ݗ��p�����?B
     * </p>
     *
     * @serial
     */
    private ParameterSection uTrimmingSectionOnBasis;

    /**
     * �g���~���O��� �� V ���̕�Ȗ�?�ł̃p���??[�^���?B
     * <p/>
     * ���̃t�B?[���h��?A���̃N���X�̓Ք�ł̂ݗ��p�����?B
     * </p>
     *
     * @serial
     */
    private ParameterSection vTrimmingSectionOnBasis;

    /**
     * �g���~���O��Ԃ̕�Ȗ�?�ł̋�`�ȃp���??[�^��Ԃ�\��
     * �Q�����̃|�����C��?B
     * <p/>
     * ���̃t�B?[���h��?A���̃N���X�̓Ք�ł̂ݗ��p�����?B
     * </p>
     *
     * @serial
     */
    private Polyline2D boundaryCurve;

    /**
     * ��Ȗʂɑ΂���p���??[�^�l��g�����Ȗʂɑ΂���p���??[�^�l�ɕϊ����邽�߂̕ϊ����Z�q?B
     *
     * @serial
     */
    private CartesianTransformationOperator2D paramTransformer;

    /**
     * �^����ꂽ�p���??[�^�l��?A���̃g�����Ȗʂ̕�Ȗʂ� U ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l�����̋Ȗʂ� U ���̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     * <p/>
     * ��Ȗʂ� U �������`���ł���?�?��ɂ�?A
     * ���ʂƂ��ē�����p���??[�^�l��?A�K�v�ɉ�����?A��Ȗʂ� U ���̃p���??[�^����?܂�?�܂��?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @return �K�v�ɉ�����?A��Ȗʂ� U ���̃p���??[�^����?܂�?�܂ꂽ�l
     * @see ParameterDomain#wrap(double)
     * @see ParameterOutOfRange
     */
    private double checkUParamValidity(double uParam) {
        if (basisSurface.isUPeriodic()) {
            ParameterDomain domain = basisSurface.uParameterDomain();
            return domain.wrap(uParam);
        }
        basisSurface.checkUValidity(uParam);

        return uParam;
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̃g�����Ȗʂ̕�Ȗʂ� V ���̒�`��ɑ΂��ėL��ۂ��𒲂ׂ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l�����̋Ȗʂ� V ���̒�`���O��Ă���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     * <p/>
     * ��Ȗʂ� V �������`���ł���?�?��ɂ�?A
     * ���ʂƂ��ē�����p���??[�^�l��?A�K�v�ɉ�����?A��Ȗʂ� V ���̃p���??[�^����?܂�?�܂��?B
     * </p>
     *
     * @param vParam V ���̃p���??[�^�l
     * @return �K�v�ɉ�����?A��Ȗʂ� V ���̃p���??[�^����?܂�?�܂ꂽ�l
     * @see ParameterDomain#wrap(double)
     * @see ParameterOutOfRange
     */
    private double checkVParamValidity(double vParam) {
        if (basisSurface.isVPeriodic()) {
            ParameterDomain domain = basisSurface.vParameterDomain();
            return domain.wrap(vParam);
        }
        basisSurface.checkVValidity(vParam);

        return vParam;
    }

    /**
     * �e�t�B?[���h�ɒl��?ݒ肷��?B
     * <p/>
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * ParameterOutOfRange �µ���� InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param basisSurface ��Ȗ�
     * @param uParam1      U ���̎n�_��\���p���??[�^�l
     * @param uParam2      U ����?I�_��\���p���??[�^�l
     * @param vParam1      V ���̎n�_��\���p���??[�^�l
     * @param vParam2      V ����?I�_��\���p���??[�^�l
     * @param uSense       U ����ȖʂƓ�����ۂ����t���O
     * @param vSense       V ����ȖʂƓ�����ۂ����t���O
     * @see ParameterOutOfRange
     * @see InvalidArgumentValueException
     */
    private void setFields(ParametricSurface3D basisSurface,
                           double uParam1,
                           double uParam2,
                           double vParam1,
                           double vParam2,
                           boolean uSense,
                           boolean vSense) {
        this.basisSurface = basisSurface;

        uParam1 = checkUParamValidity(uParam1);
        this.uParam1 = uParam1;
        uParam2 = checkUParamValidity(uParam2);
        this.uParam2 = uParam2;
        vParam1 = checkVParamValidity(vParam1);
        this.vParam1 = vParam1;
        vParam2 = checkVParamValidity(vParam2);
        this.vParam2 = vParam2;
        double pTol = this.getToleranceForParameter();

        if ((Math.abs(uParam2 - uParam1) < pTol) ||
                (Math.abs(vParam2 - vParam1) < pTol))
            throw new InvalidArgumentValueException();

        if (basisSurface.isUPeriodic()) {
            ParameterSection sec = basisSurface.uParameterDomain().section();
            if (uSense == true) {
                if (uParam1 > uParam2)
                    this.uParam2 = uParam2 += sec.increase();
            } else {
                if (uParam1 < uParam2)
                    this.uParam1 = uParam1 += sec.increase();
            }
        } else {
            if (uSense == true) {
                if (uParam1 > uParam2)
                    throw new InvalidArgumentValueException();
            } else {
                if (uParam1 < uParam2)
                    throw new InvalidArgumentValueException();
            }
        }

        if (basisSurface.isVPeriodic()) {
            ParameterSection sec = basisSurface.vParameterDomain().section();
            if (vSense == true) {
                if (vParam1 > vParam2)
                    this.vParam2 = vParam2 += sec.increase();
            } else {
                if (vParam1 < vParam2)
                    this.uParam1 = vParam1 += sec.increase();
            }
        } else {
            if (vSense == true) {
                if (vParam1 > vParam2)
                    throw new InvalidArgumentValueException();
            } else {
                if (vParam1 < vParam2)
                    throw new InvalidArgumentValueException();
            }
        }

        this.uSense = uSense;
        this.vSense = vSense;

        this.uTrimmingSectionOnBasis =
                new ParameterSection(this.uParam1, (this.uParam2 - this.uParam1));
        this.vTrimmingSectionOnBasis =
                new ParameterSection(this.vParam1, (this.vParam2 - this.vParam1));

        Point2D[] boudnaryPoints = new Point2D[4];
        boudnaryPoints[0] = Point2D.of(this.uParam1, this.vParam1);
        boudnaryPoints[1] = Point2D.of(this.uParam2, this.vParam1);
        boudnaryPoints[2] = Point2D.of(this.uParam2, this.vParam2);
        boudnaryPoints[3] = Point2D.of(this.uParam1, this.vParam2);
        this.boundaryCurve = new Polyline2D(boudnaryPoints, true);

        double diffU = -this.uParam1;
        Vector2D axisU = Vector2D.xUnitVector;
        if (this.uSense != true) {
            diffU = -diffU;
            axisU = axisU.reverse();
        }

        double diffV = -this.vParam1;
        Vector2D axisV = Vector2D.yUnitVector;
        if (this.vSense != true) {
            diffV = -diffV;
            axisV = axisV.reverse();
        }

        paramTransformer =
                new CartesianTransformationOperator2D(axisU, axisV,
                        Point2D.of(diffU, diffV),
                        1.0);
    }

    /**
     * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * ParameterOutOfRange �µ���� InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param basisSurface ��Ȗ�
     * @param uParam1      U ���̎n�_��\���p���??[�^
     * @param uParam2      U ����?I�_��\���p���??[�^
     * @param vParam1      V ���̎n�_��\���p���??[�^
     * @param vParam2      V ����?I�_��\���p���??[�^
     * @param uSense       U ����ȖʂƓ�����ۂ����t���O
     * @param vSense       V ����ȖʂƓ�����ۂ����t���O
     * @see ParameterOutOfRange
     * @see InvalidArgumentValueException
     */
    public RectangularTrimmedSurface3D(ParametricSurface3D basisSurface,
                                       double uParam1,
                                       double uParam2,
                                       double vParam1,
                                       double vParam2,
                                       boolean uSense,
                                       boolean vSense) {
        super();
        setFields(basisSurface,
                uParam1, uParam2,
                vParam1, vParam2,
                uSense, vSense);
    }

    /**
     * ��Ȗʂ� U/V �o���̃p���??[�^��Ԃ�^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * ��?��̒l�� <a href="#CONSTRAINTS">[��?��Ԃ�?S��?�?]</a> �𖞂����Ȃ�?�?��ɂ�?A
     * ParameterOutOfRange �µ���� InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param basicSurface ��Ȗ�
     * @param uPint        U ���̃g���~���O��Ԃ�\���p���??[�^���
     * @param vPint        V ���̃g���~���O��Ԃ�\���p���??[�^���
     * @see ParameterOutOfRange
     * @see InvalidArgumentValueException
     */
    public RectangularTrimmedSurface3D(ParametricSurface3D basisSurface,
                                       ParameterSection uPint,
                                       ParameterSection vPint) {
        super();
        setFields(basisSurface,
                uPint.start(), uPint.end(),
                vPint.start(), vPint.end(),
                (uPint.increase() > 0.0),
                (vPint.increase() > 0.0));
    }

    /**
     * ���̃g�����Ȗʂ̕�Ȗʂ�Ԃ�?B
     *
     * @return ��Ȗ�
     */
    public ParametricSurface3D basisSurface() {
        return this.basisSurface;
    }

    /**
     * ���̃g�����Ȗʂ̃g���~���O��Ԃ� U ���̎n�_��\����Ȗ�?�̃p���??[�^�l��Ԃ�?B
     *
     * @return �g���~���O��Ԃ� U ���̎n�_��\����Ȗ�?�̃p���??[�^�l
     */
    public double uParam1() {
        return this.uParam1;
    }

    /**
     * ���̃g�����Ȗʂ̃g���~���O��Ԃ� U ����?I�_��\����Ȗ�?�̃p���??[�^�l��Ԃ�?B
     *
     * @return �g���~���O��Ԃ� U ����?I�_��\����Ȗ�?�̃p���??[�^�l
     */
    public double uParam2() {
        return this.uParam2;
    }

    /**
     * ���̃g�����Ȗʂ̃g���~���O��Ԃ� V ���̎n�_��\����Ȗ�?�̃p���??[�^�l��Ԃ�?B
     *
     * @return �g���~���O��Ԃ� V ���̎n�_��\����Ȗ�?�̃p���??[�^�l
     */
    public double vParam1() {
        return this.vParam1;
    }

    /**
     * ���̃g�����Ȗʂ̃g���~���O��Ԃ� V ����?I�_��\����Ȗ�?�̃p���??[�^�l��Ԃ�?B
     *
     * @return �g���~���O��Ԃ� V ����?I�_��\����Ȗ�?�̃p���??[�^�l
     */
    public double vParam2() {
        return this.vParam2;
    }

    /**
     * ���̃g�����Ȗʂ� U ���?i?s����ȖʂƓ�����ۂ���Ԃ�?B
     *
     * @return U ����?i?s����ȖʂƓ�����Ȃ�� true?A����Ȃ��� false
     */
    public boolean uSense() {
        return this.uSense;
    }

    /**
     * ���̃g�����Ȗʂ� V ���?i?s����ȖʂƓ�����ۂ���Ԃ�?B
     *
     * @return V ����?i?s����ȖʂƓ�����Ȃ�� true?A����Ȃ��� false
     */
    public boolean vSense() {
        return this.vSense;
    }

    /**
     * ���̋Ȗʂ� U ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * [0, |uParam2 - uParam1|] ��Ԃ�?B
     * </p>
     *
     * @return �L�Ŕ���I�ȃp���??[�^��`��
     */
    ParameterDomain getUParameterDomain() {
        try {
            return new ParameterDomain(false, 0,
                    Math.abs(uParam2 - uParam1));
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * ���̋Ȗʂ� V ���̃p���??[�^��`���Ԃ�?B
     * <p/>
     * [0, |vParam2 - vParam1|] ��Ԃ�?B
     * </p>
     *
     * @return �L�Ŕ���I�ȃp���??[�^��`��
     */
    ParameterDomain getVParameterDomain() {
        try {
            return new ParameterDomain(false, 0,
                    Math.abs(vParam2 - vParam1));
        } catch (InvalidArgumentValueException e) {
            // should never be occurred
            throw new FatalException();
        }
    }

    /**
     * �g�����Ȗʂ� U ���̃p���??[�^�l���Ȗʂ̂���ɕϊ�����?B
     * <p/>
     * uParam ��?A���̃g�����Ȗʂ� U ���̃p���??[�^��`���O���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam �g�����Ȗʂ� U ���̃p���??[�^�l
     * @return ��Ȗʂ� U ���̃p���??[�^�l
     * @see ParameterOutOfRange
     */
    public double toBasisUParameter(double uParam) {
        checkUValidity(uParam);

        return (uSense == true) ? (uParam1 + uParam) : (uParam1 - uParam);
    }

    /**
     * �g�����Ȗʂ� V ���̃p���??[�^�l���Ȗʂ̂���ɕϊ�����?B
     * <p/>
     * vParam ��?A���̃g�����Ȗʂ� V ���̃p���??[�^��`���O���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam �g�����Ȗʂ� V ���̃p���??[�^�l
     * @return ��Ȗʂ� V ���̃p���??[�^�l
     * @see ParameterOutOfRange
     */
    public double toBasisVParameter(double vParam) {
        checkVValidity(vParam);

        return (vSense == true) ? (vParam1 + vParam) : (vParam1 - vParam);
    }

    /**
     * �g�����Ȗʂ� U ���̃p���??[�^��Ԃ��Ȗʂ̂���ɕϊ�����?B
     * <p/>
     * uPint ��?A���̃g�����Ȗʂ� U ���̃p���??[�^��`���O���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uPint �g�����Ȗʂ� U ���̃p���??[�^���
     * @return ��Ȗʂ� U ���̃p���??[�^���
     * @see ParameterOutOfRange
     */
    public ParameterSection toBasisUParameter(ParameterSection uPint) {
        double sp = toBasisUParameter(uPint.start());
        double ep = toBasisUParameter(uPint.end());

        return new ParameterSection(sp, ep - sp);
    }

    /**
     * �g�����Ȗʂ� V ���̃p���??[�^��Ԃ��Ȗʂ̂���ɕϊ�����?B
     * <p/>
     * vPint ��?A���̃g�����Ȗʂ� V ���̃p���??[�^��`���O���?�?��ɂ�
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param vPint �g�����Ȗʂ� V ���̃p���??[�^���
     * @return ��Ȗʂ� V ���̃p���??[�^���
     * @see ParameterOutOfRange
     */
    public ParameterSection toBasisVParameter(ParameterSection vPint) {
        double sp = toBasisVParameter(vPint.start());
        double ep = toBasisVParameter(vPint.end());

        return new ParameterSection(sp, ep - sp);
    }

    /**
     * ��Ȗʂ� U ���̃p���??[�^�l��g�����Ȗʂ̂���ɕϊ�����?B
     *
     * @param uParam ��Ȗʂ� U ���̃p���??[�^�l
     * @return �g�����Ȗʂ� U ���̃p���??[�^�l
     */
    public double toOwnUParameter(double uParam) {
        return (uSense == true) ? (uParam - uParam1) : (uParam1 - uParam);
    }

    /**
     * ��Ȗʂ� V ���̃p���??[�^�l��g�����Ȗʂ̂���ɕϊ�����?B
     *
     * @param vParam ��Ȗʂ� V ���̃p���??[�^�l
     * @return �g�����Ȗʂ� V ���̃p���??[�^�l
     */
    public double toOwnVParameter(double vParam) {
        return (vSense == true) ? (vParam - vParam1) : (vParam1 - vParam);
    }

    /**
     * �v�f��ʂ�Ԃ�?B
     *
     * @return {@link ParametricSurface3D#RECTANGULAR_TRIMMED_SURFACE_3D ParametricSurface3D.RECTANGULAR_TRIMMED_SURFACE_3D}
     */
    int type() {
        return ParametricSurface3D.RECTANGULAR_TRIMMED_SURFACE_3D;
    }

    /**
     * ���̊􉽗v�f�����R�`?󂩔ۂ���Ԃ�?B
     *
     * @return ��Ȗʂ����R�`?�ł���� true?A�����łȂ���� false
     */
    public boolean isFreeform() {
        return this.basisSurface.isFreeform();
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?W�l��Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?W�l
     * @see ParameterOutOfRange
     */
    public Point3D coordinates(double uParam,
                               double vParam) {
        return basisSurface.coordinates(toBasisUParameter(uParam),
                toBasisVParameter(vParam));
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł�?ڃx�N�g����Ԃ�?B
     * <p/>
     * �����ł�?ڃx�N�g���Ƃ�?A�p���??[�^ U/V �̊e?X�ɂ��Ă̈ꎟ�Γ���?��ł���?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ�z��̗v�f?��� 2 �ł���?B
     * �z���?�?��̗v�f�ɂ� U �p���??[�^�ɂ��Ă�?ڃx�N�g��?A
     * ��Ԗڂ̗v�f�ɂ� V �p���??[�^�ɂ��Ă�?ڃx�N�g����܂�?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return ?ڃx�N�g��
     * @see ParameterOutOfRange
     */
    public Vector3D[] tangentVector(double uParam,
                                    double vParam) {
        return basisSurface.tangentVector(toBasisUParameter(uParam),
                toBasisVParameter(vParam));
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�p���??[�^�l�ł̕Γ���?���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l����`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �Γ���?�
     * @see ParameterOutOfRange
     */
    public SurfaceDerivative3D evaluation(double uParam,
                                          double vParam) {
        return basisSurface.evaluation(toBasisUParameter(uParam),
                toBasisVParameter(vParam));
    }

    /**
     * �^����ꂽ�_���炱�̋Ȗʂւ̓��e�_��?�߂�?B
     * <p/>
     * ���e�_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * �^����ꂽ�_���炱�̋Ȗʂ̕�Ȗʂւ̓��e�_��?��?A
     * ���̓�ł��̋Ȗʂ̃g���~���O��Ԃ�O��Ă��Ȃ���̂ⱂ̋Ȗʂւ̓��e�_�Ƃ��Ă���?B
     * </p>
     *
     * @param point ���e���̓_
     * @return ���e�_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public PointOnSurface3D[] projectFrom(Point3D point)
            throws IndefiniteSolutionException {
        PointOnSurface3D[] basisResults = basisSurface.projectFrom(point);

        Vector thisResults = new Vector();

        for (int i = 0; i < basisResults.length; i++) {
            double uParam = toOwnUParameter(basisResults[i].uParameter());
            double vParam = toOwnVParameter(basisResults[i].vParameter());

            if (this.contains(uParam, vParam) == true)
                thisResults.addElement(new PointOnSurface3D(this, uParam, vParam, doCheckDebug));
        }

        PointOnSurface3D[] results = new PointOnSurface3D[thisResults.size()];
        thisResults.copyInto(results);

        return results;
    }

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param tol �����̋��e��?�
     * @return ���̗L�ȖʑS�̂𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     */
    public Mesh3D toMesh(ToleranceForDistance tol) {
        return this.toMesh(true,
                this.uTrimmingSectionOnBasis,
                this.vTrimmingSectionOnBasis,
                tol);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see ParameterOutOfRange
     */
    public Mesh3D toMesh(ParameterSection uPint,
                         ParameterSection vPint,
                         ToleranceForDistance tol) {
        return this.toMesh(false, uPint, vPint, tol);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ�?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param parametersAreOnBasis �p���??[�^��Ԃ���Ȗʂɑ΂��Ă��ۂ�
     * @param uPint                U ���̃p���??[�^���
     * @param vPint                V ���̃p���??[�^���
     * @param tol                  �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�Ԃ𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     * @see ParameterOutOfRange
     * @see #toMesh(ToleranceForDistance)
     * @see #toMesh(ParameterSection,ParameterSection,ToleranceForDistance)
     */
    private Mesh3D toMesh(boolean parametersAreOnBasis,
                          ParameterSection uPint,
                          ParameterSection vPint,
                          ToleranceForDistance tol) {
        if (parametersAreOnBasis != true) {
            uPint = toBasisUParameter(uPint);
            vPint = toBasisUParameter(vPint);
        }

        Mesh3D basisMesh = basisSurface.toMesh(uPint, vPint, tol);

        Point3D[][] thisPoints = new Point3D[basisMesh.uNPoints()][basisMesh.vNPoints()];

        for (int u = 0; u < basisMesh.uNPoints(); u++) {
            for (int v = 0; v < basisMesh.vNPoints(); v++) {
                PointOnSurface3D basisPoint =
                        (PointOnSurface3D) basisMesh.pointAt(u, v);
                thisPoints[u][v] =
                        new PointOnSurface3D(this,
                                toOwnUParameter(basisPoint.uParameter()),
                                toOwnVParameter(basisPoint.vParameter()),
                                doCheckDebug);
            }
        }

        return new Mesh3D(thisPoints, basisMesh.uClosed(), basisMesh.vClosed());
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^��Ԃ���`���O��Ă���?�?��ɂ�?A
     * ParameterOutOfRange �̗�O��?�����?B
     * </p>
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @return ���̋Ȗʂ̎w��̋�Ԃ�?Č�����L�? Bspline �Ȗ�
     * @see ParameterOutOfRange
     */
    public BsplineSurface3D toBsplineSurface(ParameterSection uPint,
                                             ParameterSection vPint) {
        return basisSurface.toBsplineSurface(toBasisUParameter(uPint),
                toBasisVParameter(vPint));
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̃g���~���O��Ԃ�O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate ���̋�?�
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate);

        return this.selectInternalIntersections(results, false);
    }

    /**
     * ���̋ȖʂƑ��̋�?�̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̃g���~���O��Ԃ�O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?�
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(ParametricCurve3D mate, boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate);

        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̃g���~���O��Ԃ�O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Line3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�~??��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̃g���~���O��Ԃ�O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�~??��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(Conic3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�x�W�G��?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̃g���~���O��Ԃ�O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�x�W�G��?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(PureBezierCurve3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋�?� (�a�X�v���C����?�) �̌�_��?�߂�?B
     * <p/>
     * ��_����?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋�?�Ƃ̌�_��?��?A
     * ���̓�ł��̋Ȗʂ̃g���~���O��Ԃ�O��Ă��Ȃ���̂ⱂ̋ȖʂƑ��̋�?�̌�_�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋�?� (�a�X�v���C����?�)
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩�ǂ���
     * @return ��_�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    IntersectionPoint3D[] intersect(BsplineCurve3D mate,
                                    boolean doExchange)
            throws IndefiniteSolutionException {
        IntersectionPoint3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.selectInternalIntersections(results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗʂ̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ���?������?��ɂ��Ă�?A��?� (IntersectionCurve3D) ���Ԃ�?B
     * </p>
     * <p/>
     * ��Ȗʂ�?ڂ����?��ɂ��Ă�?A��_ (IntersectionPoint3D) ���Ԃ邱�Ƃ�����?B
     * </p>
     *
     * @param mate ���̋Ȗ�
     * @return ��?� (�܂��͌�_) �̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     * @see IntersectionCurve3D
     * @see IntersectionPoint3D
     */
    public SurfaceSurfaceInterference3D[] intersect(ParametricSurface3D mate)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results;
        int basisType = this.basisSurface.type();
        if ((basisType == ParametricSurface3D.SURFACE_OF_LINEAR_EXTRUSION_3D) ||
                (basisType == ParametricSurface3D.SURFACE_OF_REVOLUTION_3D)) {
            results = ((SweptSurface3D) this.basisSurface).
                    intersect(this.uTrimmingSectionOnBasis, this.vTrimmingSectionOnBasis, mate);
        } else {
            results = this.basisSurface.intersect(mate);
        }

        return this.trimIntersectionsWithBoundaries(mate, results, false);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�I�t�Z�b�g�����Ȗʂ�
     * �^����ꂽ��?��ŋߎ����� Bspline �Ȗʂ�?�߂�?B
     *
     * @param uPint U ���̃p���??[�^���
     * @param vPint V ���̃p���??[�^���
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.FRONT/BACK)
     * @param tol   �����̋��e��?�
     * @return ���̋Ȗʂ̎w��̋�`��Ԃ̃I�t�Z�b�g�Ȗʂ�ߎ����� Bspline �Ȗ�
     * @see WhichSide
     */
    public BsplineSurface3D offsetByBsplineSurface(ParameterSection uPint,
                                                   ParameterSection vPint,
                                                   double magni,
                                                   int side,
                                                   ToleranceForDistance tol) {
        BsplineSurface3D convertedBspline =
                this.toBsplineSurface(uPint, vPint);

        return convertedBspline.offsetByBsplineSurface(convertedBspline.uParameterDomain().section(),
                convertedBspline.vParameterDomain().section(),
                magni, side, tol);
    }

    /*
    * ���̋Ȗʂ� U �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param uParam	U ���̃p���??[�^�l
    * @return	�w��� U �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D uIsoParametricCurve(double uParam)
            throws ReducedToPointException {
        ParametricCurve3D basisCurve = basisSurface.uIsoParametricCurve(toBasisUParameter(uParam));
        return new TrimmedCurve3D(basisCurve, vTrimmingSectionOnBasis);
    }

    /*
    * ���̋Ȗʂ� V �p���??[�^���̈ʒu�ɂ��铙�p���??[�^��?��Ԃ�?B
    *
    * @param vParam	V ���̃p���??[�^�l
    * @return	�w��� V �p���??[�^�l�ł̓��p���??[�^��?�
    */
    public ParametricCurve3D vIsoParametricCurve(double vParam)
            throws ReducedToPointException {
        ParametricCurve3D basisCurve = basisSurface.vIsoParametricCurve(toBasisVParameter(vParam));
        return new TrimmedCurve3D(basisCurve, uTrimmingSectionOnBasis);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̃g���~���O��ԂŃg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(Plane3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̃g���~���O��ԂŃg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(SphericalSurface3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~����) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̃g���~���O��ԂŃg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~����)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(CylindricalSurface3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�~??��) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̃g���~���O��ԂŃg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�~??��)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     * @throws IndefiniteSolutionException �⪕s��ł���
     */
    SurfaceSurfaceInterference3D[] intersect(ConicalSurface3D mate,
                                             boolean doExchange)
            throws IndefiniteSolutionException {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�x�W�G�Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̃g���~���O��ԂŃg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�x�W�G�Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(PureBezierSurface3D mate,
                                             boolean doExchange) {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋ȖʂƑ��̋Ȗ� (�a�X�v���C���Ȗ�) �̌�?��?�߂�?B
     * <p/>
     * ��?�?݂��Ȃ��Ƃ��͒��� 0 �̔z���Ԃ�?B
     * </p>
     * <p/>
     * [�Ք?��?]
     * <br>
     * ���̋Ȗʂ̕�ȖʂƑ��̋ȖʂƂ̊Ԃŋ?�߂���?��
     * ���̋Ȗʂ̃g���~���O��ԂŃg���~���O������̂ⱂ̋ȖʂƑ��̋Ȗʂ̌�?�Ƃ��Ă���?B
     * </p>
     *
     * @param mate       ���̋Ȗ� (�a�X�v���C���Ȗ�)
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @return ��?�̔z��
     */
    SurfaceSurfaceInterference3D[] intersect(BsplineSurface3D mate,
                                             boolean doExchange) {
        SurfaceSurfaceInterference3D[] results = this.basisSurface.intersect(mate, doExchange);
        return this.trimIntersectionsWithBoundaries(mate, results, doExchange);
    }

    /**
     * ���̋Ȗʂ̎w��� (�p���??[�^�I��) ��`��Ԃ�?A
     * �^����ꂽ��?��ŕ��ʋߎ�����_�Q��Ԃ�?B
     * <p/>
     * ?��?���ʂƂ��ē�����_�Q�͈�ʂ�?A�ʑ��I�ɂ�􉽓I�ɂ�?A�i�q?�ł͂Ȃ�?B
     * </p>
     * <p/>
     * scalingFactor ��?A��͗p�ł͂Ȃ�?A?o�͗p�̈�?��ł���?B
     * scalingFactor �ɂ�?A�v�f?� 2 �̔z���^����?B
     * scalingFactor[0] �ɂ� U ����?k�ڔ{��?A
     * scalingFactor[1] �ɂ� V ����?k�ڔ{�����Ԃ�?B
     * �����̒l�͉��炩��?�Βl�ł͂Ȃ�?A
     * �p���??[�^��?i�ޑ��x T �ɑ΂���?A
     * U/V �����ɂ��Ď��?�ŋȖ�?�̓_��?i�ޑ��x Pu/Pv ��\�����Βl�ł���?B
     * �܂�?A�p���??[�^�� T ����?i�ނ�?A
     * ���?�ł̋Ȗ�?�̓_�� U ���ł� Pu (scalingFactor[0])?A
     * V ���ł� Pv (scalingFactor[1]) ����?i�ނ��Ƃ�\���Ă���?B
     * T �̑傫���͖�������Ȃ��̂�?A���̒l��Q?Ƃ���?ۂɂ�?A
     * scalingFactor[0] �� scalingFactor[1] �̔䂾����p����ׂ��ł���?B
     * �Ȃ�?A�����̒l�͂����܂ł�ڈł���?A�����ȑ��x����̂ł͂Ȃ�?B
     * </p>
     * <p/>
     * ���ʂƂ��ĕԂ� Vector �Ɋ܂܂��e�v�f��
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D
     * �ł��邱�Ƃ���҂ł���?B
     * </p>
     *
     * @param uParameterSection U ���̃p���??[�^���
     * @param vParameterSection V ���̃p���??[�^���
     * @param tolerance         �����̋��e��?�
     * @param scalingFactor     �_�Q��O�p�`��������?ۂɗL�p�Ǝv���� U/V ��?k�ڔ{��
     * @return �_�Q��܂� Vector
     * @see PointOnSurface3D
     */
    public Vector toNonStructuredPoints(ParameterSection uParameterSection,
                                        ParameterSection vParameterSection,
                                        double tolerance,
                                        double[] scalingFactor) {
        Vector basisResults =
                basisSurface.toNonStructuredPoints(toBasisUParameter(uParameterSection),
                        toBasisVParameter(vParameterSection),
                        tolerance,
                        scalingFactor);
        Vector results = new Vector();
        for (Enumeration e = basisResults.elements(); e.hasMoreElements();) {
            PointOnSurface3D pos = (PointOnSurface3D) e.nextElement();
            results.addElement(new PointOnSurface3D(this,
                    toOwnUParameter(pos.uParameter()),
                    toOwnVParameter(pos.vParameter()),
                    doCheckDebug));
        }

        return results;
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l�����E?�ɂ���?�?��ɂ�?u�Ѥ?v�Ɣ��f����?B
     * </p>
     *
     * @param uParam U ���̃p���??[�^�l
     * @param vParam V ���̃p���??[�^�l
     * @return �p���??[�^�l�����E�̓Ѥ�ł���� true?A�����łȂ���� false
     */
    public boolean contains(double uParam,
                            double vParam) {
        if (this.isValidUParameter(uParam) != true) {
            return false;
        }

        if (this.isValidVParameter(vParam) != true) {
            return false;
        }

        return true;
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * point2D �����E?�ɂ���?�?��ɂ�?u�Ѥ?v�Ɣ��f����?B
     * </p>
     *
     * @param point2D (u, v) �p���??[�^�l
     * @return point2D �����E�̓Ѥ�ł���� true?A�����łȂ���� false
     */
    public boolean contains(Point2D point2D) {
        return this.contains(point2D.x(), point2D.y());
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v�p���??[�^�l��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l�����E?�ɂ���?�?��ɂ�?u�Ѥ?v�Ɣ��f����?B
     * </p>
     *
     * @param uParam ��Ȗʂɑ΂��� U ���̃p���??[�^�l
     * @param vParam ��Ȗʂɑ΂��� V ���̃p���??[�^�l
     * @return �p���??[�^�l�����E�̓Ѥ�ł���� true?A�����łȂ���� false
     */
    public boolean containsBasis(double uParam,
                                 double vParam) {
        if (uTrimmingSectionOnBasis.isValid(uParam) != true)
            return false;

        if (vTrimmingSectionOnBasis.isValid(vParam) != true)
            return false;

        return true;
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v�p���??[�^�l��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * point2D �����E?�ɂ���?�?��ɂ�?u�Ѥ?v�Ɣ��f����?B
     * </p>
     *
     * @param point2D ��Ȗʂɑ΂��� (u, v) �p���??[�^�l
     * @return point2D �����E�̓Ѥ�ł���� true?A�����łȂ���� false
     */
    public boolean containsBasis(Point2D point2D) {
        return this.containsBasis(point2D.x(), point2D.y());
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v�p���??[�^�l�����E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l���Ȗʂ̃p���??[�^��`��� wrap �������?A
     * {@link #containsBasis(double,double) containsBasis(double, double)}
     * ��Ă�?o��?B
     * </p>
     *
     * @param uParam ��Ȗʂɑ΂��� U ���̃p���??[�^�l
     * @param vParam ��Ȗʂɑ΂��� V ���̃p���??[�^�l
     * @return �p���??[�^�l�����E�̓Ѥ�ł���� true?A�����łȂ���� false
     * @see #containsBasis(double,double)
     * @see ParameterDomain#wrap(double)
     */
    private boolean containsBasisWithWrapping(double uParam,
                                              double vParam) {
        return this.containsBasis(basisSurface.uParameterDomain().wrap(uParam),
                basisSurface.vParameterDomain().wrap(vParam));
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v�p���??[�^�l�����E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     * <p/>
     * �^����ꂽ�p���??[�^�l���Ȗʂ̃p���??[�^��`��� wrap �������?A
     * {@link #containsBasis(double,double) containsBasis(double, double)}
     * ��Ă�?o��?B
     * </p>
     *
     * @param point2D ��Ȗʂɑ΂��� (u, v) �p���??[�^�l
     * @return point2D �����E�̓Ѥ�ł���� true?A�����łȂ���� false
     * @see #containsBasis(double,double)
     * @see ParameterDomain#wrap(double)
     */
    private boolean containsBasisWithWrapping(Point2D point2D) {
        return this.containsBasis(basisSurface.uParameterDomain().wrap(point2D.x()),
                basisSurface.vParameterDomain().wrap(point2D.y()));
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v��_��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��邩�ۂ���Ԃ�?B
     *
     * @param ints       ��Ȗʂɑ΂����_
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩
     * @return ��_�����E�ɓѤ�ɂ���� true?A�����łȂ���� false
     * @see #containsBasisWithWrapping(double,double)
     * @see #selectInternalIntersections(IntersectionPoint3D[],boolean)
     */
    private boolean
    intersectionIsInternal(IntersectionPoint3D ints,
                           boolean doExchange) {
        PointOnSurface3D pointOnSurface;
        if (doExchange == false) {
            pointOnSurface = (PointOnSurface3D) ints.pointOnGeometry1();
        } else {
            pointOnSurface = (PointOnSurface3D) ints.pointOnGeometry2();
        }
        return this.containsBasisWithWrapping(pointOnSurface.uParameter(),
                pointOnSurface.vParameter());
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v��_��?A���̋Ȗʂɑ΂����_�ɕϊ�����?B
     * <p/>
     * ���̃?�\�b�h���Ԃ���_��?A���̑�?ۂ����łȂ�
     * �p���??[�^�l�±�̋Ȗʂɑ΂����̂ɕϊ�����Ă���?B
     * </p>
     *
     * @param ints       ��Ȗʂɑ΂����_
     * @param doExchange ��_�� pointOnGeometry1/2 ��귂��邩
     * @return ��?ۂⱂ̋Ȗʂɕ�?X������_
     * @see #intersectionIsInternal(IntersectionPoint3D,boolean)
     * @see #changeTargetOfIntersection(IntersectionCurve3D,boolean)
     * @see #selectInternalIntersections(IntersectionPoint3D[],boolean)
     */
    private IntersectionPoint3D
    changeTargetOfIntersection(IntersectionPoint3D ints,
                               boolean doExchange) {
        PointOnGeometry3D pog1 = ints.pointOnGeometry1();
        PointOnGeometry3D pog2 = ints.pointOnGeometry2();
        PointOnSurface3D pos;
        double uParam;
        double vParam;
        if (doExchange == false) {
            pos = (PointOnSurface3D) pog1;
            uParam = basisSurface.uParameterDomain().wrap(pos.uParameter());
            vParam = basisSurface.vParameterDomain().wrap(pos.vParameter());
            pog1 = new PointOnSurface3D(this,
                    toOwnUParameter(uParam),
                    toOwnVParameter(vParam),
                    doCheckDebug);
        } else {
            pos = (PointOnSurface3D) pog2;
            uParam = basisSurface.uParameterDomain().wrap(pos.uParameter());
            vParam = basisSurface.vParameterDomain().wrap(pos.vParameter());
            pog2 = new PointOnSurface3D(this,
                    toOwnUParameter(uParam),
                    toOwnVParameter(vParam),
                    doCheckDebug);
        }
        return new IntersectionPoint3D(ints.coordinates(),
                pog1, pog2, doCheckDebug);
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v��?��?A���̋Ȗʂɑ΂����?�ɕϊ�����?B
     * <p/>
     * ���̃?�\�b�h���Ԃ���?��?A���̑�?ۂ���?X���ꂽ������?A
     * �p���??[�^�l�͕�Ȗʂɑ΂����̂̂܂܂ł��邱�Ƃɒ?��?B
     * </p>
     *
     * @param ints       ��Ȗʂɑ΂����?�
     * @param doExchange ��?�� basisSurface1/2 ��귂��邩
     * @return ��?ۂⱂ̋Ȗʂɕ�?X������?�
     * @see #changeTargetOfIntersection(IntersectionPoint3D,boolean)
     */
    private IntersectionCurve3D
    changeTargetOfIntersection(IntersectionCurve3D ints,
                               boolean doExchange) {
        ParametricSurface3D basisSurface1;
        ParametricSurface3D basisSurface2;
        if (doExchange == false) {
            basisSurface1 = this;
            basisSurface2 = ints.basisSurface2();
        } else {
            basisSurface1 = ints.basisSurface1();
            basisSurface2 = this;
        }
        return new IntersectionCurve3D(ints.curve3d(),
                basisSurface1, ints.curve2d1(),
                basisSurface2, ints.curve2d2(),
                ints.masterRepresentation());
    }

    /**
     * �^����ꂽ?u��Ȗʂɑ΂���?v��_�̓��?A���̋Ȗʂ̋��E�̓Ѥ�ɂ����_��Ԃ�?B
     * <p/>
     * ���ʂƂ��ē������_��?A
     * ���̑�?ۂƃp���??[�^�l�����̋Ȗʂɑ΂����̂ɕϊ�����Ă���?B
     * </p>
     *
     * @param intersections ��ȖʂƂ̌�_���Z�œ���ꂽ��_�̔z��
     * @param doExchange    ��_�� pointOnGeometry1/2 ��귂��邩
     * @return ���̋Ȗʂ̋��E�̓Ѥ�ɂ����_�̔z��
     * @see #intersectionIsInternal(IntersectionPoint3D,boolean)
     * @see #changeTargetOfIntersection(IntersectionPoint3D,boolean)
     */
    IntersectionPoint3D[]
    selectInternalIntersections(IntersectionPoint3D[] intersections,
                                boolean doExchange) {
        Vector innerPoints = new Vector();
        IntersectionPoint3D ints;

        for (int i = 0; i < intersections.length; i++) {
            if (this.intersectionIsInternal(intersections[i], doExchange) == true) {
                ints = this.changeTargetOfIntersection(intersections[i], doExchange);
                innerPoints.addElement(ints);
            }
        }

        IntersectionPoint3D[] results =
                new IntersectionPoint3D[innerPoints.size()];
        innerPoints.copyInto(results);
        return results;
    }

    /**
     * ��Ȗ�?�̋�?�Ƃ��̋Ȗʂ̋��E�Ƃ̌�_��\���Ք�N���X?B
     */
    private class IntersectionWithBoundaryInfo {
        /**
         * ���E?ォ�ۂ�?B
         */
        boolean onBoundary;

        /**
         * ��Ȗ�?�̋�?�?�̃p���??[�^�l?B
         */
        double curveParameter;

        /**
         * ����^�����ɃI�u�W�F�N�g��?\�z����?B
         */
        IntersectionWithBoundaryInfo() {
        }

        /**
         * �e�t�B?[���h��?ݒ肷��l��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param onBoundary     ���E?ォ�ۂ�
         * @param curveParameter ��Ȗ�?�̋�?�?�̃p���??[�^�l
         */
        IntersectionWithBoundaryInfo(boolean onBoundary,
                                     double curveParameter) {
            this.onBoundary = onBoundary;
            this.curveParameter = curveParameter;
        }
    }

    /**
     * �^����ꂽ��?�􉽓I�ɕ��Ă����?A�ʑ��I�ɂ�����`���ɂ���?B
     *
     * @param theIntersection ��?�
     * @return �ʑ��I�ȊJ��?�Ԃ�􉽓I�ȊJ��?�Ԃ�?��킹����?�
     */
    private IntersectionCurve3D
    makeIntersectionClose(IntersectionCurve3D theIntersection) {
        boolean changed = false;

        /*
        * curve3d
        */
        ParametricCurve3D curve3d = theIntersection.curve3d();
        if ((curve3d.isClosed() == true) &&
                (curve3d.isPeriodic() != true) &&
                (curve3d.type() == ParametricCurve3D.POLYLINE_3D)) {
            Polyline3D polyline3d = (Polyline3D) curve3d;
            int nPoints = polyline3d.nPoints() - 1;
            Point3D[] points = new Point3D[nPoints];
            for (int i = 0; i < nPoints; i++)
                points[i] = polyline3d.pointAt(i);
            curve3d = new Polyline3D(points, true);
            changed = true;
        }

        /*
        * curve2d1
        */
        ParametricCurve2D curve2d1 = theIntersection.curve2d1();
        if ((curve2d1.isClosed() == true) &&
                (curve2d1.isPeriodic() != true) &&
                ((curve2d1 instanceof Polyline2D) == true)) {
            Polyline2D polyline2d = (Polyline2D) curve2d1;
            int nPoints = polyline2d.nPoints() - 1;
            Point2D[] points = new Point2D[nPoints];
            for (int i = 0; i < nPoints; i++)
                points[i] = polyline2d.pointAt(i);
            curve2d1 = new Polyline2D(points, true);
            changed = true;
        }

        /*
        * curve2d2
        */
        ParametricCurve2D curve2d2 = theIntersection.curve2d2();
        if ((curve2d2.isClosed() == true) &&
                (curve2d2.isPeriodic() != true) &&
                ((curve2d2 instanceof Polyline2D) == true)) {
            Polyline2D polyline2d = (Polyline2D) curve2d2;
            int nPoints = polyline2d.nPoints() - 1;
            Point2D[] points = new Point2D[nPoints];
            for (int i = 0; i < nPoints; i++)
                points[i] = polyline2d.pointAt(i);
            curve2d2 = new Polyline2D(points, true);
            changed = true;
        }

        if (changed != true)
            return theIntersection;

        return new IntersectionCurve3D(curve3d,
                theIntersection.basisSurface1(), curve2d1,
                theIntersection.basisSurface2(), curve2d2,
                theIntersection.masterRepresentation());
    }

    /**
     * ��Ȗ�?�̋�?�뫊E�Ƃ̌�_�Ńg���~���O�����Ԃ�\���Ք�N���X?B
     */
    private class TrimmingInterval {
        /**
         * �J�n�_�̃��X�g��ł̈ʒu?B
         */
        int sIdx;

        /**
         * ?I���_�̃��X�g��ł̈ʒu?B
         */
        int eIdx;

        /**
         * �J�n�_?^?I���_�̃��X�g��ł̈ʒu��^���ăI�u�W�F�N�g��?\�z����?B
         *
         * @param sIdx �J�n�_�̃��X�g��ł̈ʒu
         * @param eIdx ?I���_�̃��X�g��ł̈ʒu
         */
        TrimmingInterval(int sIdx,
                         int eIdx) {
            this.sIdx = sIdx;
            this.eIdx = eIdx;
        }
    }

    /**
     * ����_�̂����?�?�ł̃p���??[�^�l��?�߂�?B
     * <p/>
     * point3d �� curve3d ��?�BĂ����̂Ƃ���?A
     * point3d �� curve3d �ł̃p���??[�^�l��Ԃ�?B
     * </p>
     * <p/>
     * �p���??[�^�l�����܂��?�߂��Ȃ�?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @param curve3d ��?�
     * @param point3d �_
     * @return �p���??[�^�l
     * @see FatalException
     * @see ParametricCurve3D#hasPolyline()
     * @see BoundedCurve3D#toPolyline(ToleranceForDistance)
     * @see ParametricCurve3D#nearestProjectFrom(Point3D)
     */
    private double getParameterWithCurve3D(ParametricCurve3D curve3d,
                                           Point3D point3d) {
        PointOnCurve3D nearProj3d;
        double nearDist;
        PointOnCurve3D nearestProj3d = null;
        double nearestDist = Double.NaN;

        PointOnCurve3D lowerPoint = null;
        PointOnCurve3D upperPoint = null;
        if (curve3d.isFinite() && curve3d.isOpen()) {
            lowerPoint = new PointOnCurve3D(curve3d, curve3d.parameterDomain().section().lower());
            upperPoint = new PointOnCurve3D(curve3d, curve3d.parameterDomain().section().upper());
        }

        Polyline3D polyline3d = null;
        if (curve3d.hasPolyline() == true) {
            // curve3d �͗L�ł���Ɍ��܂BĂ���
            BoundedCurve3D bounded3d = (BoundedCurve3D) curve3d;
            polyline3d = bounded3d.toPolyline(getToleranceForDistanceAsObject());
        }

        nearProj3d = curve3d.nearestProjectFrom(point3d);
        if (nearProj3d != null) {
            nearDist = nearProj3d.distance(point3d);
            if ((nearestProj3d == null) || (nearDist < nearestDist)) {
                nearestProj3d = nearProj3d;
                nearestDist = nearDist;
            }
        }

        if (lowerPoint != null) {
            nearDist = lowerPoint.distance(point3d);
            if ((nearestProj3d == null) || (nearDist < nearestDist)) {
                nearestProj3d = lowerPoint;
                nearestDist = nearDist;
            }
        }
        if (upperPoint != null) {
            nearDist = upperPoint.distance(point3d);
            if ((nearestProj3d == null) || (nearDist < nearestDist)) {
                nearestProj3d = upperPoint;
                nearestDist = nearDist;
            }
        }
        if (polyline3d != null) {
            for (int i = 0; i < polyline3d.nPoints(); i++) {
                nearDist = polyline3d.pointAt(i).distance(point3d);
                if ((nearestProj3d == null) || (nearDist < nearestDist)) {
                    nearestProj3d = (PointOnCurve3D) polyline3d.pointAt(i);
                    nearestDist = nearDist;
                }
            }
        }

        if (nearestProj3d == null)    // �ǂ�����Ⴂ���̂�
            throw new FatalException("No projection.");

        return nearestProj3d.parameter();
    }

    /**
     * ����_��?u����Ȗ�?�̂����?�?v?�ł̃p���??[�^�l��?�߂�?B
     * <p/>
     * curve2d �� surface3d ?�̋�?� C �̃p���??[�^��Ԃł̂Q�����\���ł����̂Ƃ���?B
     * </p>
     * <p/>
     * point3d �� C ��?�BĂ����̂Ƃ���?A
     * point3d �� C �ł̃p���??[�^�l��Ԃ�?B
     * </p>
     * <p/>
     * �p���??[�^�l�����܂��?�߂��Ȃ�?�?��ɂ�
     * FatalException �̗�O��?�����?B
     * </p>
     *
     * @param surface3d �Ȗ�
     * @param curve2d   ��?�
     * @param point3d   �_
     * @return �p���??[�^�l
     * @see FatalException
     * @see ParametricSurface3D#nearestProjectFrom(Point3D)
     * @see ParametricCurve2D#hasPolyline()
     * @see BoundedCurve2D#toPolyline(ToleranceForDistance)
     * @see ParametricCurve2D#nearestProjectFrom(Point2D)
     */
    private double getParameterWithCurveOnSurface3D(ParametricSurface3D surface3d,
                                                    ParametricCurve2D curve2d,
                                                    Point3D point3d) {
        PointOnSurface3D nearestProj3d = surface3d.nearestProjectFrom(point3d);
        if (nearestProj3d == null)    // �ǂ�����Ⴂ���̂�
            throw new FatalException("No projection in 3d.");
        double[] param3d = nearestProj3d.parameters();

        PointOnCurve2D lowerPoint = null;
        PointOnCurve2D upperPoint = null;
        if (curve2d.isFinite() && curve2d.isOpen()) {
            lowerPoint = new PointOnCurve2D(curve2d, curve2d.parameterDomain().section().lower());
            upperPoint = new PointOnCurve2D(curve2d, curve2d.parameterDomain().section().upper());
        }

        Polyline2D polyline2d = null;
        if (curve2d.hasPolyline() == true) {
            // curve2d �͗L�ł���Ɍ��܂BĂ���
            BoundedCurve2D bounded2d = (BoundedCurve2D) curve2d;
            polyline2d = bounded2d.toPolyline(getToleranceForDistanceAsObject());
        }

        int nU = 1;
        double dU = Double.NaN;

        int nV = 1;
        double dV = Double.NaN;

        if (surface3d.type() != ParametricSurface3D.CURVE_BOUNDED_SURFACE_3D) {
            if (surface3d.isUPeriodic() == true) {
                nU = 3;
                dU = surface3d.uParameterDomain().section().increase();
            }

            if (surface3d.isVPeriodic() == true) {
                nV = 3;
                dV = surface3d.vParameterDomain().section().increase();
            }
        }

        double pU;
        double pV;
        Point2D point2d;
        PointOnCurve2D nearProj2d;
        double nearDist;
        PointOnCurve2D nearestProj2d = null;
        double nearestDist = Double.NaN;

        for (int iU = 0; iU < nU; iU++) {
            switch (iU) {
                case 1:
                    pU = param3d[0] - dU;
                    break;
                case 2:
                    pU = param3d[0] + dU;
                    break;
                default:
                    pU = param3d[0];
                    break;
            }

            for (int iV = 0; iV < nV; iV++) {
                switch (iV) {
                    case 1:
                        pV = param3d[1] - dV;
                        break;
                    case 2:
                        pV = param3d[1] + dV;
                        break;
                    default:
                        pV = param3d[1];
                        break;
                }

                point2d = Point2D.of(pU, pV);
                nearProj2d = curve2d.nearestProjectFrom(point2d);
                if (nearProj2d != null) {
                    nearDist = nearProj2d.distance(point2d);
                    if ((nearestProj2d == null) || (nearDist < nearestDist)) {
                        nearestProj2d = nearProj2d;
                        nearestDist = nearDist;
                    }
                }

                if (lowerPoint != null) {
                    nearDist = lowerPoint.distance(point2d);
                    if ((nearestProj2d == null) || (nearDist < nearestDist)) {
                        nearestProj2d = lowerPoint;
                        nearestDist = nearDist;
                    }
                }

                if (upperPoint != null) {
                    nearDist = upperPoint.distance(point2d);
                    if ((nearestProj2d == null) || (nearDist < nearestDist)) {
                        nearestProj2d = upperPoint;
                        nearestDist = nearDist;
                    }
                }

                if (polyline2d != null) {
                    for (int i = 0; i < polyline2d.nPoints(); i++) {
                        nearDist = polyline2d.pointAt(i).distance(point2d);
                        if ((nearestProj2d == null) || (nearDist < nearestDist)) {
                            nearestProj2d = (PointOnCurve2D) polyline2d.pointAt(i);
                            nearestDist = nearDist;
                        }
                    }
                }
            }
        }

        if (nearestProj2d == null)    // �ǂ�����Ⴂ���̂�
            throw new FatalException("No projection in 2d.");

        return nearestProj2d.parameter();
    }

    /**
     * ���̋Ȗʂ̋��E��?u��Ȗʂɑ΂���?v��?�̌�_��?�߂�?B
     *
     * @param boundaryCurve ���̋Ȗʂ̋��E
     * @param intsT         ��Ȗʂɑ΂����?�̂Q�����\��
     * @return boundaryCurve �� intsT �̌�_�̔z��
     * @see #trimIntersectionsWithBoundaries(ParametricSurface3D,SurfaceSurfaceInterference3D[],boolean)
     */
    private IntersectionPoint2D[]
    getIntersectionsWithBoundary(Polyline2D boundaryCurve,
                                 ParametricCurve2D intsT) {
        int nU = 1;
        double dU = Double.NaN;

        int nV = 1;
        double dV = Double.NaN;

        if (basisSurface.type() != ParametricSurface3D.CURVE_BOUNDED_SURFACE_3D) {
            if (basisSurface.isUPeriodic() == true) {
                nU = 3;
                dU = basisSurface.uParameterDomain().section().increase();
            }

            if (basisSurface.isVPeriodic() == true) {
                nV = 3;
                dV = basisSurface.vParameterDomain().section().increase();
            }
        }

        double pU;
        double pV;
        Polyline2D tBoundaryCurve;
        IntersectionPoint2D[] intsWithBoundary;
        Vector intsWithBoundaryList = new Vector();
        int nInts = 0;

        for (int iU = 0; iU < nU; iU++) {
            switch (iU) {
                case 1:
                    pU = -dU;
                    break;
                case 2:
                    pU = dU;
                    break;
                default:
                    pU = 0.0;
                    break;
            }

            for (int iV = 0; iV < nV; iV++) {
                switch (iV) {
                    case 1:
                        pV = -dV;
                        break;
                    case 2:
                        pV = dV;
                        break;
                    default:
                        pV = 0.0;
                        break;
                }

                if ((iU == 0) && (iV == 0)) {
                    tBoundaryCurve = boundaryCurve;
                } else {
                    CartesianTransformationOperator2D transformer =
                            new CartesianTransformationOperator2D(null, null,
                                    Point2D.of(pU, pV),
                                    1.0);
                    tBoundaryCurve = (Polyline2D) boundaryCurve.transformBy(transformer, null);
                }
                intsWithBoundary = tBoundaryCurve.intersect(intsT);

                if (intsWithBoundary.length > 0) {
                    intsWithBoundaryList.addElement(intsWithBoundary);
                    nInts += intsWithBoundary.length;
                }
            }
        }

        IntersectionPoint2D[] result = new IntersectionPoint2D[nInts];
        int iResult = 0;
        for (Enumeration e = intsWithBoundaryList.elements(); e.hasMoreElements();) {
            intsWithBoundary = (IntersectionPoint2D[]) e.nextElement();
            for (int i = 0; i < intsWithBoundary.length; i++)
                result[iResult++] = intsWithBoundary[i];
        }

        return result;
    }

    /**
     * �^����ꂽ�p���??[�^�l��?A����J������`��̒��ɓ��悤�Ɋۂ߂�?B
     *
     * @param param   �p���??[�^�l
     * @param section (�J������`���) �p���??[�^���
     */
    private double wrapParameterIntoOpenSection(double param,
                                                ParameterSection section) {
        while (param < section.lower())
            param += section.absIncrease();
        while (param > section.upper())
            param -= section.absIncrease();

        return param;
    }

    /**
     * �^����ꂽ��?��g���~���O����̂�?A?K��ɓ���q����悤�ɂ��ăg���~���O����?B
     * <p/>
     * curve �͊J������?��?A(sp &gt; ep) �ł��邱�Ƃ�z�肵�Ă���?B
     * </p>
     *
     * @param curve (�J����) ��?�
     * @param sp    �g���~���O���Ďc����Ԃ̊J�n�p���??[�^�l
     * @param ep    �g���~���O���Ďc����Ԃ�?I���p���??[�^�l
     * @see #wrapParameterIntoOpenSection(double,ParameterSection)
     */
    private ParametricCurve2D connectHeadToTail(ParametricCurve2D curve,
                                                double sp,
                                                double ep) {
        ParameterSection section = curve.parameterDomain().section();

        double sp1 = wrapParameterIntoOpenSection(sp, section);
        double ep1 = section.upper();
        double sp2 = section.lower();
        double ep2 = wrapParameterIntoOpenSection(ep, section);

        Point2D lowerCoord = curve.coordinates(section.lower());
        Point2D upperCoord = curve.coordinates(section.upper());
        Vector2D period = upperCoord.subtract(lowerCoord);
        CartesianTransformationOperator2D transformer =
                new CartesianTransformationOperator2D(null, null,
                        period.toPoint2D(),
                        1.0);

        ParametricCurve2D curve1 = curve;
        ParametricCurve2D curve2 = curve.transformBy(transformer, null);

        TrimmedCurve2D tCurve1 = new TrimmedCurve2D(curve1, sp1, ep1, true);
        TrimmedCurve2D tCurve2 = new TrimmedCurve2D(curve2, sp2, ep2, true);
        CompositeCurveSegment2D[] segments =
                new CompositeCurveSegment2D[2];
        segments[0] = new CompositeCurveSegment2D(TransitionCode.CONTINUOUS,
                true, tCurve1);
        segments[1] = new CompositeCurveSegment2D(TransitionCode.DISCONTINUOUS,
                true, tCurve2);
        return new CompositeCurve2D(segments, false);
    }

    /**
     * �|�����C��������?\?����ꂽ��?��w��̃p���??[�^��ԂŃg���~���O����?B
     *
     * @param doExchange      ��?�� basisSurface1/2 ��귂��邩�ǂ���
     * @param theIntersection �g���~���O�����?�
     * @param isOpenT         �g���~���O�����?�� this ?�̕\�����J���Ă��邩�ۂ�
     * @param isOpenM         �g���~���O�����?�� mate ?�̕\�����J���Ă��邩�ۂ�
     * @param crossBoundary   �g���~���O��Ԃ� (�R�����I�ɕ���) ��?�̎���ׂ����ۂ�
     * @param spT             �g���~���O�̊J�n�p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @param ipT             �g���~���O�̑?���p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @return �g���~���O��̌�?�
     * @see #trimIntersection(boolean,IntersectionCurve3D,ParametricCurve2D,ParameterSection,boolean,boolean,boolean,double,double)
     * @see #wrapParameterIntoOpenSection(double,ParameterSection)
     * @see #connectHeadToTail(ParametricCurve2D,double,double)
     */
    private IntersectionCurve3D
    trimIntersection2(boolean doExchange,
                      IntersectionCurve3D theIntersection,
                      boolean isOpenT,
                      boolean isOpenM,
                      boolean crossBoundary,
                      double spT,
                      double ipT) {
        double epT = spT + ipT;

        ParameterSection section;
        double sp;
        double ep;
        boolean isOpen;

        /*
        * curve3d
        */
        ParametricCurve3D curve3d = theIntersection.curve3d();
        sp = spT;
        ep = epT;
        curve3d = new TrimmedCurve3D(curve3d, sp, ep, true);

        /*
        * curve2d1
        */
        ParametricCurve2D curve2d1 = theIntersection.curve2d1();
        sp = spT;
        ep = epT;
        isOpen = (doExchange == false) ? isOpenT : isOpenM;
        if ((crossBoundary == true) && (isOpen == true)) {
            section = curve2d1.parameterDomain().section();
            sp = wrapParameterIntoOpenSection(sp, section);
            ep = wrapParameterIntoOpenSection(ep, section);
        }
        if ((sp < ep) || (isOpen == false)) {
            curve2d1 = new TrimmedCurve2D(curve2d1, sp, ep, true);
        } else {
            curve2d1 = connectHeadToTail(curve2d1, sp, ep);
        }

        /*
        * curve2d2
        */
        ParametricCurve2D curve2d2 = theIntersection.curve2d2();
        sp = spT;
        ep = epT;
        isOpen = (doExchange == false) ? isOpenM : isOpenT;
        if ((crossBoundary == true) && (isOpen == true)) {
            section = curve2d2.parameterDomain().section();
            sp = wrapParameterIntoOpenSection(sp, section);
            ep = wrapParameterIntoOpenSection(ep, section);
        }
        if ((sp < ep) || (isOpen == false)) {
            curve2d2 = new TrimmedCurve2D(curve2d2, sp, ep, true);
        } else {
            curve2d2 = connectHeadToTail(curve2d2, sp, ep);
        }

        return new IntersectionCurve3D(curve3d,
                theIntersection.basisSurface1(), curve2d1,
                theIntersection.basisSurface2(), curve2d2,
                theIntersection.masterRepresentation());
    }

    /**
     * ��?��w��̃p���??[�^��ԂŃg���~���O����?B
     *
     * @param doExchange      ��?��basisSurface1,2��귂��邩�ǂ���
     * @param theIntersection �g���~���O�����?�
     * @param intsT           �g���~���O�����?�� this ?�̕\��
     * @param sectionOfIntsT  intsT �̃p���??[�^��`��̋��
     * @param isOpenT         �g���~���O�����?�� this ?�̕\�����J���Ă��邩�ۂ�
     * @param isOpenM         �g���~���O�����?�� mate ?�̕\�����J���Ă��邩�ۂ�
     * @param crossBoundary   �g���~���O��Ԃ� (�R�����I�ɕ���) ��?�̎���ׂ����ۂ�
     * @param spT             �g���~���O�̊J�n�p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @param ipT             �g���~���O�̑?���p���??[�^�l (this ?�̕\���̃p���??[�^�l)
     * @return �g���~���O��̌�?�
     * @see ParametricCurve3D#isComposedOfOnlyPolylines()
     * @see ParametricCurve2D#isComposedOfOnlyPolylines()
     * @see #trimIntersection2(boolean,IntersectionCurve3D,boolean,boolean,boolean,double,double)
     * @see #wrapParameterIntoOpenSection(double,ParameterSection)
     * @see #getParameterWithCurve3D(ParametricCurve3D,Point3D)
     * @see #getParameterWithCurveOnSurface3D(ParametricSurface3D,ParametricCurve2D,Point3D)
     * @see #connectHeadToTail(ParametricCurve2D,double,double)
     */
    private IntersectionCurve3D
    trimIntersection(boolean doExchange,
                     IntersectionCurve3D theIntersection,
                     ParametricCurve2D intsT,
                     ParameterSection sectionOfIntsT,
                     boolean isOpenT,
                     boolean isOpenM,
                     boolean crossBoundary,
                     double spT,
                     double ipT) {
        if ((theIntersection.curve3d().isComposedOfOnlyPolylines() == true) &&
                (theIntersection.curve2d1().isComposedOfOnlyPolylines() == true) &&
                (theIntersection.curve2d2().isComposedOfOnlyPolylines() == true)) {
            ParameterSection section3d =
                    theIntersection.curve3d().parameterDomain().section();
            ParameterSection section2d1 =
                    theIntersection.curve2d1().parameterDomain().section();
            ParameterSection section2d2 =
                    theIntersection.curve2d2().parameterDomain().section();

            if ((section3d.identical(section2d1) == true) &&
                    (section3d.identical(section2d2) == true))
                return trimIntersection2(doExchange,
                        theIntersection,
                        isOpenT, isOpenM,
                        crossBoundary, spT, ipT);
        }

        double epT = spT + ipT;

        if ((crossBoundary == true) && (isOpenT == true)) {
            spT = wrapParameterIntoOpenSection(spT, sectionOfIntsT);
            epT = wrapParameterIntoOpenSection(epT, sectionOfIntsT);
        }

        Point2D spnt2d = intsT.coordinates(spT);
        Point2D epnt2d = intsT.coordinates(epT);
        Point3D spnt3d = this.basisSurface.coordinates(spnt2d.x(), spnt2d.y());
        Point3D epnt3d = this.basisSurface.coordinates(epnt2d.x(), epnt2d.y());

        double sp;
        double ep;
        boolean isOpen;

        /*
        * curve3d
        */
        ParametricCurve3D curve3d = theIntersection.curve3d();
        sp = getParameterWithCurve3D(curve3d, spnt3d);
        ep = getParameterWithCurve3D(curve3d, epnt3d);
        curve3d = new TrimmedCurve3D(curve3d, sp, ep, true);

        /*
        * curve2d1
        */
        ParametricCurve2D curve2d1 = theIntersection.curve2d1();
        if (doExchange == false) {
            sp = spT;
            ep = epT;
            isOpen = isOpenT;
        } else {
            sp = getParameterWithCurveOnSurface3D(theIntersection.basisSurface1(),
                    curve2d1, spnt3d);
            ep = getParameterWithCurveOnSurface3D(theIntersection.basisSurface1(),
                    curve2d1, epnt3d);
            isOpen = isOpenM;
        }
        if ((sp < ep) || (isOpen == false)) {
            curve2d1 = new TrimmedCurve2D(curve2d1, sp, ep, true);
        } else {
            curve2d1 = connectHeadToTail(curve2d1, sp, ep);
        }

        /*
        * curve2d2
        */
        ParametricCurve2D curve2d2 = theIntersection.curve2d2();
        if (doExchange == false) {
            sp = getParameterWithCurveOnSurface3D(theIntersection.basisSurface2(),
                    curve2d2, spnt3d);
            ep = getParameterWithCurveOnSurface3D(theIntersection.basisSurface2(),
                    curve2d2, epnt3d);
            isOpen = isOpenM;
        } else {
            sp = spT;
            ep = epT;
            isOpen = isOpenT;
        }
        if ((sp < ep) || (isOpen == false)) {
            curve2d2 = new TrimmedCurve2D(curve2d2, sp, ep, true);
        } else {
            curve2d2 = connectHeadToTail(curve2d2, sp, ep);
        }

        return new IntersectionCurve3D(curve3d,
                theIntersection.basisSurface1(), curve2d1,
                theIntersection.basisSurface2(), curve2d2,
                theIntersection.masterRepresentation());
    }

    /**
     * ���̋Ȗʂ̕�Ȗʂ̃p���??[�^���?�̋�?��
     * ��Ȗʂ̃v���C�}���ȗL���ԓ�Ɉړ�����?B
     *
     * @param curve ��Ȗʂ̃p���??[�^���?�̋�?�
     * @return ��Ȗʂ̃v���C�}���ȗL���ԓ�ɂ����?�
     */
    private ParametricCurve2D
    moveIntoPrimarySections(ParametricCurve2D curve) {
        double lower = curve.parameterDomain().section().lower();
        double upper = curve.parameterDomain().section().upper();
        double middle = (lower + upper) / 2.0;
        Point2D lowerPoint = curve.coordinates(lower);
        Point2D upperPoint = curve.coordinates(upper);
        Point2D middlePoint = curve.coordinates(middle);

        int nU = 1;
        double dU = Double.NaN;

        int nV = 1;
        double dV = Double.NaN;

        if (basisSurface.type() != ParametricSurface3D.CURVE_BOUNDED_SURFACE_3D) {
            if (basisSurface.isUPeriodic() == true) {
                nU = 3;
                dU = basisSurface.uParameterDomain().section().increase();
            }

            if (basisSurface.isVPeriodic() == true) {
                nV = 3;
                dV = basisSurface.vParameterDomain().section().increase();
            }
        }

        double pU;
        double pV;
        CartesianTransformationOperator2D transformer;
        Point2D tLowerPoint;
        Point2D tUpperPoint;
        Point2D tMiddlePoint;

        for (int iU = 0; iU < nU; iU++) {
            switch (iU) {
                case 1:
                    pU = -dU;
                    break;
                case 2:
                    pU = dU;
                    break;
                default:
                    pU = 0.0;
                    break;
            }

            for (int iV = 0; iV < nV; iV++) {
                switch (iV) {
                    case 1:
                        pV = -dV;
                        break;
                    case 2:
                        pV = dV;
                        break;
                    default:
                        pV = 0.0;
                        break;
                }

                if ((iU == 0) && (iV == 0)) {
                    transformer = null;
                    tLowerPoint = lowerPoint;
                    tUpperPoint = upperPoint;
                    tMiddlePoint = middlePoint;
                } else {
                    transformer =
                            new CartesianTransformationOperator2D(null, null,
                                    Point2D.of(pU, pV),
                                    1.0);
                    tLowerPoint = lowerPoint.transformBy(transformer, null);
                    tUpperPoint = upperPoint.transformBy(transformer, null);
                    tMiddlePoint = middlePoint.transformBy(transformer, null);
                }

                if ((containsBasis(tLowerPoint) == true) &&
                        (containsBasis(tUpperPoint) == true) &&
                        (containsBasis(tMiddlePoint) == true)) {
                    if (transformer == null)
                        return curve;
                    else
                        return curve.transformBy(transformer, null);
                }
            }
        }

        return null;
    }

    /**
     * ���̋Ȗʂ̕�Ȗʂɑ΂���p���??[�^�l��?��?��
     * ���̋Ȗʂɑ΂���p���??[�^�l��?��?�ɕϊ�����?B
     * <p/>
     * �����ł͕ϊ����~��?[�����O��܂�?�?��ɖ�肪�N���蓾��?B
     * </p>
     *
     * @param ints       ���̋Ȗʂ̕�Ȗʂɑ΂���p���??[�^�l��?��?�
     * @param doExchange ��?�� pointOnSurface1/2 ��귂��邩
     * @return ���̋Ȗʂɑ΂���p���??[�^�l��?��?�
     * @see #moveIntoPrimarySections(ParametricCurve2D)
     */
    private IntersectionCurve3D
    changeParameterSpaceOfIntersection(IntersectionCurve3D ints,
                                       boolean doExchange) {
        ParametricCurve2D curve2d1 = ints.curve2d1();
        ParametricCurve2D curve2d2 = ints.curve2d2();
        if (doExchange == false) {
            curve2d1 = moveIntoPrimarySections(curve2d1).transformBy(paramTransformer, null);
        } else {
            curve2d2 = moveIntoPrimarySections(curve2d2).transformBy(paramTransformer, null);
        }
        return new IntersectionCurve3D(ints.curve3d(),
                ints.basisSurface1(), curve2d1,
                ints.basisSurface2(), curve2d2,
                ints.masterRepresentation());
    }

    /**
     * ���̋Ȗʂ̕�ȖʂƑ��̋Ȗʂ̌�?��?A
     * ���̋Ȗʂ̋��E�Ńg���~���O����?B
     * <p/>
     * �^����ꂽ��?�ɂ���?A���̋Ȗʂ̋��E�̓Ѥ�ɂ��镔���������?�Ƃ��ĕԂ�?B
     * </p>
     *
     * @param mate          ���̋Ȗ�
     * @param intersections ��ȖʂƂ̌�?�Z�œ���ꂽ��?�̔z��
     * @param doExchange    ��?�� pointOnSurface1/2 ��귂��邩
     * @return ���̋Ȗʂ̋��E�Ńg���~���O������?�̔z��
     * @see #changeTargetOfIntersection(IntersectionPoint3D,boolean)
     * @see #changeTargetOfIntersection(IntersectionCurve3D,boolean)
     * @see #intersectionIsInternal(IntersectionPoint3D,boolean)
     * @see #makeIntersectionClose(IntersectionCurve3D)
     * @see #getIntersectionsWithBoundary(Polyline2D,ParametricCurve2D)
     * @see RectangularTrimmedSurface3D.IntersectionWithBoundaryInfo
     * @see #containsBasisWithWrapping(double,double)
     * @see #containsBasisWithWrapping(Point2D)
     * @see #trimIntersection(boolean,IntersectionCurve3D,ParametricCurve2D,ParameterSection,boolean,boolean,boolean,double,double)
     * @see #changeParameterSpaceOfIntersection(IntersectionCurve3D,boolean)
     */
    SurfaceSurfaceInterference3D[]
    trimIntersectionsWithBoundaries(ParametricSurface3D mate,
                                    SurfaceSurfaceInterference3D[] intersections,
                                    boolean doExchange) {
        // ��?̑�?ۂ��Ȗʂ��玩?g�ɕ�?X����
        for (int i = 0; i < intersections.length; i++) {
            if (intersections[i].isIntersectionPoint() == true) {
                // ��_
                IntersectionPoint3D ints =
                        intersections[i].toIntersectionPoint();
                intersections[i] = changeTargetOfIntersection(ints, doExchange);
            } else {
                // ��?�
                IntersectionCurve3D ints =
                        intersections[i].toIntersectionCurve();
                intersections[i] = changeTargetOfIntersection(ints, doExchange);
            }
        }

        Vector results = new Vector();

        // final �ł���̂�?AiwbiBothEnds ������ comparator ����Q?Ƃ���邽��
        final IntersectionWithBoundaryInfo[] iwbiBothEnds =
                new IntersectionWithBoundaryInfo[2];

        // ���E�Ƃ̌�_��?u�傫��?v���?�?�̃p���??[�^�l�Ŕ��f����I�u�W�F�N�g
        ListSorter.ObjectComparator comparator =
                new ListSorter.ObjectComparator() {
                    public boolean latterIsGreaterThanFormer(java.lang.Object former,
                                                             java.lang.Object latter) {
                        IntersectionWithBoundaryInfo f = (IntersectionWithBoundaryInfo) former;
                        IntersectionWithBoundaryInfo l = (IntersectionWithBoundaryInfo) latter;
                        if (f == l)
                            return false;

                        if ((f == iwbiBothEnds[0]) || (l == iwbiBothEnds[1]))
                            return true;

                        if ((l == iwbiBothEnds[0]) || (f == iwbiBothEnds[1]))
                            return false;

                        return (f.curveParameter < l.curveParameter) ? true : false;
                    }
                };

        /*
        * ��?̂��ꂼ��ɂ���
        */
        for (int i = 0; i < intersections.length; i++) {
            /*
            * ��_��?�?�?A���ꂪ���E�̓Ѥ�ɂ���Ȃ�� results �ɉB���
            */
            if (intersections[i].isIntersectionPoint() == true) {
                IntersectionPoint3D ints =
                        intersections[i].toIntersectionPoint();
                if (this.intersectionIsInternal(ints, doExchange) == true)
                    results.addElement(ints);
                continue;
            }

            /*
            * �ȉ�?A��?��?�?�
            */
            IntersectionCurve3D theIntersection =
                    intersections[i].toIntersectionCurve();
            theIntersection = makeIntersectionClose(theIntersection);

            /*
            * �Ȗ�?�̃p���??[�^��?�𓾂�
            */
            ParametricCurve2D intsT;
            ParametricCurve2D intsM;

            if (doExchange == false) {
                intsT = theIntersection.curve2d1();
                intsM = theIntersection.curve2d2();
            } else {
                intsT = theIntersection.curve2d2();
                intsM = theIntersection.curve2d1();
            }

            boolean isOpen3 = theIntersection.curve3d().isOpen();
            boolean isOpenT = intsT.isOpen();
            boolean isOpenM = intsM.isOpen();

            ParameterDomain domainOfIntsT = intsT.parameterDomain();
            ParameterSection sectionOfIntsT = domainOfIntsT.section();

            /*
            * ���E�Ƃ̌�_�𓾂�
            */
            Vector listOfIntersectionsWithBoundaries = new Vector();
            IntersectionPoint2D[] intsWithBoundary =
                    getIntersectionsWithBoundary(boundaryCurve, intsT);

            for (int k = 0; k < intsWithBoundary.length; k++) {
                IntersectionWithBoundaryInfo iwbi =
                        new IntersectionWithBoundaryInfo(true,
                                intsWithBoundary[k].pointOnCurve2().parameter());
                listOfIntersectionsWithBoundaries.addElement(iwbi);
            }

            /*
            * ��_��?���?�����
            */
            iwbiBothEnds[0] = null;
            iwbiBothEnds[1] = null;
            boolean addEndPoints = false;

            if (isOpen3 == true) {
                /*
                * ��?�J���Ă���Ȃ��?A���̗��[�뫊E�Ƃ̌�_�ɉB���
                */
                if (intsT.isFinite() == true) {
                    iwbiBothEnds[0] =
                            new IntersectionWithBoundaryInfo(false,
                                    sectionOfIntsT.start());
                    iwbiBothEnds[1] =
                            new IntersectionWithBoundaryInfo(false,
                                    sectionOfIntsT.end());

                    listOfIntersectionsWithBoundaries.addElement(iwbiBothEnds[0]);
                    listOfIntersectionsWithBoundaries.addElement(iwbiBothEnds[1]);
                    addEndPoints = true;
                }
            } else if (listOfIntersectionsWithBoundaries.size() == 1) {
                /*
                * ��?���Ă���?A���E�Ƃ̌�_����Ȃ��?A
                * ���̌�_��Ⴄ��_�Ƃ��ĉB���
                */
                IntersectionWithBoundaryInfo iwbi =
                        (IntersectionWithBoundaryInfo) listOfIntersectionsWithBoundaries.elementAt(0);
                IntersectionWithBoundaryInfo iwbi2 =
                        new IntersectionWithBoundaryInfo(iwbi.onBoundary,
                                iwbi.curveParameter);
                listOfIntersectionsWithBoundaries.addElement(iwbi2);
            }

            /*
            * ���E�Ƃ̌�_���Ȃ�?�?�?A
            * ��?�?�̂����_ (�J�n�_) �����E�̓Ք�ɂ����?A
            * ���̌�?�⻂̂܂�?u?o�͂����?�̃��X�g?v�ɉB���
            */
            if (listOfIntersectionsWithBoundaries.size() == 0) {
                double aParameter = (intsT.isFinite() == true) ?
                        sectionOfIntsT.start() : 0.0;
                if (this.containsBasisWithWrapping(intsT.coordinates(aParameter)) == true) {
                    results.addElement(changeParameterSpaceOfIntersection(theIntersection, doExchange));
                }
                continue;
            }

            /*
            * �ȉ�?A���E�Ƃ̌�_������?�?�
            */

            /*
            * ���E�Ƃ̌�_���?�?�̃p���??[�^�l�Ń\?[�g����
            */
            ListSorter.doSorting(listOfIntersectionsWithBoundaries, comparator);

            int nIntervals;
            if (isOpen3 == true) {
                nIntervals = listOfIntersectionsWithBoundaries.size() - 1;
            } else {
                nIntervals = listOfIntersectionsWithBoundaries.size();
            }

            /*
            * �ׂ�?���?u���E�Ƃ̌�_?v�̒��_�����E�̓Ք�ɂ����?A
            * ���̋�Ԃ�c����̂Ƃ���
            */
            Vector listOfTrimmingIntervals = new Vector();
            TrimmingInterval trimmingInterval = null;

            int sIdx;
            int eIdx;
            IntersectionWithBoundaryInfo sIwb;
            IntersectionWithBoundaryInfo eIwb;

            double sp;
            double ip;
            double mp;

            boolean crossBoundary;

            sIdx = 0;
            sIwb = (IntersectionWithBoundaryInfo)
                    listOfIntersectionsWithBoundaries.elementAt(0);

            for (int j = 1; j <= nIntervals; j++) {
                sp = sIwb.curveParameter;
                if ((isOpen3 == true) || (j < nIntervals)) {
                    eIdx = j;
                    eIwb = (IntersectionWithBoundaryInfo)
                            listOfIntersectionsWithBoundaries.elementAt(j);
                    crossBoundary = false;
                    ip = eIwb.curveParameter - sIwb.curveParameter;

                    if (((sIwb == iwbiBothEnds[0]) ||
                            (eIwb == iwbiBothEnds[1])) &&
                            (Math.abs(ip) < getToleranceForParameter())) {
                        sIdx = eIdx;
                        sIwb = eIwb;
                        continue;
                    }
                } else {
                    eIdx = 0;
                    eIwb = (IntersectionWithBoundaryInfo)
                            listOfIntersectionsWithBoundaries.elementAt(0);
                    crossBoundary = true;
                    ip = eIwb.curveParameter - sIwb.curveParameter +
                            sectionOfIntsT.increase();

                    if (Math.abs(ip) < getToleranceForParameter()) {
                        // no cross boundary
                        sIdx = eIdx;
                        sIwb = eIwb;
                        continue;
                    }
                }

                mp = sp + (ip / 2.0);

                if (addEndPoints == true) {
                    if (j == 1) {
                        mp = sp;
                    } else if (j == nIntervals) {
                        mp = sp + ip;
                    }
                }

                if ((crossBoundary == true) && (isOpenT == true)) {
                    if (mp < sectionOfIntsT.lower())
                        mp += sectionOfIntsT.absIncrease();
                    if (mp > sectionOfIntsT.upper())
                        mp -= sectionOfIntsT.absIncrease();
                }

                if (this.containsBasisWithWrapping(intsT.coordinates(mp)) == true) {
                    if ((trimmingInterval != null) &&
                            (trimmingInterval.eIdx == sIdx)) {
                        trimmingInterval.eIdx = eIdx;
                    } else {
                        trimmingInterval = new TrimmingInterval(sIdx, eIdx);
                        listOfTrimmingIntervals.addElement(trimmingInterval);
                    }
                }

                sIdx = eIdx;
                sIwb = eIwb;
            }

            if ((nIntervals = listOfTrimmingIntervals.size()) > 1) {
                TrimmingInterval head =
                        (TrimmingInterval) listOfTrimmingIntervals.firstElement();
                TrimmingInterval tail =
                        (TrimmingInterval) listOfTrimmingIntervals.lastElement();
                if (head.sIdx == tail.eIdx) {
                    head.sIdx = tail.sIdx;
                    nIntervals--;
                }
            }

            /*
            * ��?��?u�c���Ɣ��f�������?v�Ńg���~���O����
            */
            for (int j = 0; j < nIntervals; j++) {
                trimmingInterval = (TrimmingInterval) listOfTrimmingIntervals.elementAt(j);
                sIwb = (IntersectionWithBoundaryInfo)
                        listOfIntersectionsWithBoundaries.elementAt(trimmingInterval.sIdx);
                eIwb = (IntersectionWithBoundaryInfo)
                        listOfIntersectionsWithBoundaries.elementAt(trimmingInterval.eIdx);

                sp = sIwb.curveParameter;
                if (trimmingInterval.sIdx < trimmingInterval.eIdx) {
                    crossBoundary = false;
                    ip = eIwb.curveParameter - sIwb.curveParameter;
                } else {
                    crossBoundary = true;
                    ip = eIwb.curveParameter - sIwb.curveParameter +
                            sectionOfIntsT.increase();
                }

                if (ip < getToleranceForParameter())
                    continue;

                IntersectionCurve3D theTrimmedIntersection =
                        trimIntersection(doExchange,
                                theIntersection, intsT, sectionOfIntsT,
                                isOpenT, isOpenM,
                                crossBoundary, sp, ip);

                BoundedCurve3D theTrimmedIntersection3 =
                        (BoundedCurve3D) theTrimmedIntersection.curve3d();
                if (theTrimmedIntersection3.length() < getToleranceForDistance())
                    continue;

                /*
                * �g���~���O�������ʂ̒[�_��?A���̋Ȗʂ̋��E?�ɂ���Ȃ��?A
                * ���̒[�_����Z�Ń��t�@�C������
                */
                // KOKO
                ;

                /*
                * �g���~���O������Ԃ�?u?o�͂����?�̃��X�g?v�ɉB���
                */
                results.addElement(changeParameterSpaceOfIntersection(theTrimmedIntersection,
                        doExchange));
            }
        }

        /*
        * ?u?o�͂����?�̃��X�g?v��Ԃ�
        */
        intersections = new SurfaceSurfaceInterference3D[results.size()];
        results.copyInto(intersections);
        return intersections;
    }

    /**
     * ���̋Ȗʂ�?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    protected synchronized ParametricSurface3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        ParametricSurface3D tBasisSurface =
                this.basisSurface.transformBy(reverseTransform,
                        transformationOperator,
                        transformedGeometries);
        return new RectangularTrimmedSurface3D(tBasisSurface,
                this.uParam1, this.uParam2,
                this.vParam1, this.vParam2,
                this.uSense,
                this.vSense);
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     */
    protected void output(PrintWriter writer,
                          int indent) {
        String indent_tab = makeIndent(indent);

        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tbasisSurface");
        basisSurface.output(writer, indent + 2);
        writer.println(indent_tab + "\tuParam1\t" + uParam1);
        writer.println(indent_tab + "\tuParam2\t" + uParam2);
        writer.println(indent_tab + "\tvParam1\t" + vParam1);
        writer.println(indent_tab + "\tvParam2\t" + vParam2);
        writer.println(indent_tab + "\tuSense\t" + uSense);
        writer.println(indent_tab + "\tvSense\t" + vSense);
        writer.println(indent_tab + "End");
    }
}

// end of file
