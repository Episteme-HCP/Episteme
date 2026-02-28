/*
 * �p�x�̋��e�덷��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ToleranceForAngle.java,v 1.3 2007-10-21 21:08:20 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �p�x�̋��e�덷��\���N���X�B
 * <p/>
 * JGCL �ł́A
 * �􉽉��Z��i�߂�ۂ̋��e�덷�����ɂ����ĎQ�Ƃ��ׂ��e��̋��e�덷�l��
 * ���Z�� {@link ConditionOfOperation ConditionOfOperation} �Ƃ��āA
 * �܂Ƃ߂ĊǗ�����B
 * </p>
 * <p/>
 * �����ł̊p�x�̒P�ʂ́u�ʓx (���W�A��) �v�ł����̂Ƃ���B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:20 $
 * @see ConditionOfOperation
 * @see ToleranceForDistance
 * @see ToleranceForParameter
 * @see Tolerance
 */

public class ToleranceForAngle extends Tolerance {

    /**
     * �^����ꂽ�l�떗e�덷�l�Ƃ���I�u�W�F�N�g��\�z����B
     * <p/>
     * value �̒l�̎�舵���Ɋւ��ẮA
     * {@link Tolerance#Tolerance(double) �X�[�p�[�N���X�̃R���X�g���N�^}
     * �ɏ�����B
     * </p>
     *
     * @param value �p�x�̋��e�덷�l
     */
    public ToleranceForAngle(double value) {
        super(value);
    }

    /**
     * ���̊p�x�̋��e�덷��A
     * �^����ꂽ���a�̉~���ł�
     * �u�����̍��v�ɕϊ�����B
     * <p/>
     * ���̊p�x�̋��e�덷�̒l�ɁA
     * ���a radius �̒l��|���āA
     * �����̋��e�덷�ɕϊ������l��Ԃ��B
     * </p>
     *
     * @param radius ���a
     * @return ���̊p�x�̋��e�덷�ɑ������鋗���̋��e�덷
     */
    public ToleranceForDistance toToleranceForDistance(double radius) {
        return new ToleranceForDistance(this.value() * radius);
    }
}

