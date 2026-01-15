/*
 * �����̋��e�덷��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ToleranceForDistance.java,v 1.3 2007-10-21 21:08:20 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �����̋��e�덷��\���N���X�B
 * <p/>
 * JGCL �ł́A
 * �􉽉��Z��i�߂�ۂ̋��e�덷�����ɂ����ĎQ�Ƃ��ׂ��e��̋��e�덷�l��
 * ���Z�� {@link ConditionOfOperation ConditionOfOperation} �Ƃ��āA
 * �܂Ƃ߂ĊǗ�����B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:20 $
 * @see ConditionOfOperation
 * @see ToleranceForAngle
 * @see ToleranceForParameter
 * @see Tolerance
 */

public class ToleranceForDistance extends Tolerance {
    /**
     * �����̋��e�덷�l�̎���B
     * <p/>
     * �����̎���l�͕p�ɂɎQ�Ƃ����̂ŁA�\�ߌv�Z���Ă����B
     * </p>
     */
    private double value2;

    /**
     * �^����ꂽ�l�떗e�덷�l�Ƃ���I�u�W�F�N�g��\�z����B
     * <p/>
     * value �̒l�̎�舵���Ɋւ��ẮA
     * {@link Tolerance#Tolerance(double) �X�[�p�[�N���X�̃R���X�g���N�^}
     * �ɏ�����B
     * </p>
     *
     * @param value �����̋��e�덷�l
     */
    public ToleranceForDistance(double value) {
        super(value);
        this.value2 = value * value;
    }

    /**
     * ���̋����̋��e�덷�̒l�̎����Ԃ��B
     *
     * @return ���e�덷�l�̎���
     */
    public double value2() {
        return this.value2;
    }

    /**
     * ���̋����̋��e�덷�̒l�̎����Ԃ��B
     *
     * @return ���e�덷�l�̎���
     */
    public double squared() {
        return this.value2;
    }

    /**
     * ���̋����̋��e�덷��A
     * �^����ꂽ�Q�����̋Ȑ�̎w��̃p�����[�^�l�ł�
     * �u�p�����[�^�l�̍��v�ɕϊ�����B
     * <p/>
     * ���̋����̋��e�덷�̒l��A
     * curve �� t �ɂ�����ڃx�N�g���̑傫���Ŋ���A
     * �p�����[�^�l�̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param curve �Ȑ�
     * @param t     �p�����[�^�l
     * @return ���̋����̋��e�덷�ɑ�������p�����[�^�l�̋��e�덷
     */
    public ToleranceForParameter
    toToleranceForParameter(ParametricCurve2D curve, double t) {
        return new ToleranceForParameter(this.value() / curve.tangentVector(t).length());
    }

    /**
     * ���̋����̋��e�덷��A
     * �^����ꂽ�R�����̋Ȑ�̎w��̃p�����[�^�l�ł�
     * �u�p�����[�^�l�̍��v�ɕϊ�����B
     * <p/>
     * ���̋����̋��e�덷�̒l��A
     * curve �� t �ɂ�����ڃx�N�g���̑傫���Ŋ���A
     * �p�����[�^�l�̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param curve �Ȑ�
     * @param t     �p�����[�^�l
     * @return ���̋����̋��e�덷�ɑ�������p�����[�^�l�̋��e�덷
     */
    public ToleranceForParameter
    toToleranceForParameter(ParametricCurve3D curve, double t) {
        return new
                ToleranceForParameter(this.value() / curve.tangentVector(t).length());
    }

    /**
     * ���̋����̋��e�덷��A
     * �^����ꂽ�R�����̋Ȗʂ̎w��̃p�����[�^�l (u, v) �ł�
     * �uU ���̃p�����[�^�l�̍��v�ɕϊ�����B
     * <p/>
     * ���̋����̋��e�덷�̒l��A
     * surface �� (u, v) �ɂ����� U ���̈ꎟ�Γ��֐��̑傫���Ŋ���A
     * �p�����[�^�l�̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param surface �Ȗ�
     * @param u       U ���̃p�����[�^�l
     * @param v       V ���̃p�����[�^�l
     * @return ���̋����̋��e�덷�ɑ�������p�����[�^�l�̋��e�덷
     */
    public ToleranceForParameter
    toToleranceForParameterU(ParametricSurface3D surface,
                             double u, double v) {
        return new
                ToleranceForParameter(this.value() / surface.tangentVector(u, v)[0].length());
    }

    /**
     * ���̋����̋��e�덷��A
     * �^����ꂽ�R�����̋Ȗʂ̎w��̃p�����[�^�l (u, v) �ł�
     * �uV ���̃p�����[�^�l�̍��v�ɕϊ�����B
     * <p/>
     * ���̋����̋��e�덷�̒l��A
     * surface �� (u, v) �ɂ����� V ���̈ꎟ�Γ��֐��̑傫���Ŋ���A
     * �p�����[�^�l�̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param surface �Ȗ�
     * @param u       U ���̃p�����[�^�l
     * @param v       V ���̃p�����[�^�l
     * @return ���̋����̋��e�덷�ɑ�������p�����[�^�l�̋��e�덷
     */
    public ToleranceForParameter
    toToleranceForParameterV(ParametricSurface3D surface,
                             double u, double v) {
        return new
                ToleranceForParameter(this.value() / surface.tangentVector(u, v)[1].length());
    }

    /**
     * ���̋����̋��e�덷��A
     * �^����ꂽ���a�̉~�ł�
     * �u�p�x�̍��v�ɕϊ�����B
     * <p/>
     * ���̋����̋��e�덷�̒l��A
     * ���a radius �̒l�Ŋ���A
     * �p�x�̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param radius ���a
     * @return ���̋����̋��e�덷�ɑ�������p�x�̋��e�덷
     */
    public ToleranceForAngle toToleranceForAngle(double radius) {
        return new ToleranceForAngle(this.value() / radius);
    }
}

