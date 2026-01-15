/*
 * �p�����[�^�l�̋��e�덷��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ToleranceForParameter.java,v 1.3 2007-10-21 21:08:20 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �p�����[�^�l�̋��e�덷��\���N���X�B
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
 * @see ToleranceForDistance
 * @see ToleranceForAngle
 * @see Tolerance
 */

public class ToleranceForParameter extends Tolerance {

    /**
     * �^����ꂽ�l�떗e�덷�l�Ƃ���I�u�W�F�N�g��\�z����B
     * <p/>
     * value �̒l�̎�舵���Ɋւ��ẮA
     * {@link Tolerance#Tolerance(double) �X�[�p�[�N���X�̃R���X�g���N�^}
     * �ɏ�����B
     * </p>
     *
     * @param value �p�����[�^�l�̋��e�덷
     */
    public ToleranceForParameter(double value) {
        super(value);
    }

    /**
     * ���̃p�����[�^�l�̋��e�덷��A
     * �^����ꂽ�Q�����̋Ȑ�̎w��̃p�����[�^�l�ł�
     * �u�Ȑ�̓��̂�v�ɕϊ�����B
     * <p/>
     * ���̃p�����[�^�l�̋��e�덷�̒l�ɁA
     * curve �� t �ɂ�����ڃx�N�g���̑傫����|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param curve �Ȑ�
     * @param t     �p�����[�^�l
     * @return ���̃p�����[�^�l�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance
    toToleranceForDistance(ParametricCurve2D curve, double t) {
        return new
                ToleranceForDistance(this.value() * curve.tangentVector(t).length());
    }

    /**
     * ���̃p�����[�^�l�̋��e�덷��A
     * �^����ꂽ�R�����̋Ȑ�̎w��̃p�����[�^�l�ł�
     * �u�Ȑ�̓��̂�v�ɕϊ�����B
     * <p/>
     * ���̃p�����[�^�l�̋��e�덷�̒l�ɁA
     * curve �� t �ɂ�����ڃx�N�g���̑傫����|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param curve �Ȑ�
     * @param t     �p�����[�^�l
     * @return ���̃p�����[�^�l�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance
    toToleranceForDistance(ParametricCurve3D curve, double t) {
        return new
                ToleranceForDistance(this.value() * curve.tangentVector(t).length());
    }

    /**
     * ���̃p�����[�^�l�̋��e�덷��A
     * �^����ꂽ�R�����̋Ȗʂ̎w��̃p�����[�^�l (u, v) �ł�
     * �uU ���̓��p�����[�^�Ȑ�̓��̂�v�ɕϊ�����B
     * <p/>
     * ���̃p�����[�^�l�̋��e�덷�̒l�ɁA
     * surface �� (u, v) �ɂ����� U ���̈ꎟ�Γ��֐��̑傫����|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param surface �Ȗ�
     * @param u       U ���̃p�����[�^�l
     * @param v       V ���̃p�����[�^�l
     * @return ���̃p�����[�^�l�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance
    toToleranceForDistanceU(ParametricSurface3D surface,
                            double u, double v) {
        return new
                ToleranceForDistance(this.value() * surface.tangentVector(u, v)[0].length());
    }

    /**
     * ���̃p�����[�^�l�̋��e�덷��A
     * �^����ꂽ�R�����̋Ȗʂ̎w��̃p�����[�^�l (u, v) �ł�
     * �uV ���̓��p�����[�^�Ȑ�̓��̂�v�ɕϊ�����B
     * <p/>
     * ���̃p�����[�^�l�̋��e�덷�̒l�ɁA
     * surface �� (u, v) �ɂ����� V ���̈ꎟ�Γ��֐��̑傫����|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param surface �Ȗ�
     * @param u       U ���̃p�����[�^�l
     * @param v       V ���̃p�����[�^�l
     * @return ���̃p�����[�^�l�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance
    toToleranceForDistanceV(ParametricSurface3D surface,
                            double u, double v) {
        return new
                ToleranceForDistance(this.value() * surface.tangentVector(u, v)[0].length());
    }
}

