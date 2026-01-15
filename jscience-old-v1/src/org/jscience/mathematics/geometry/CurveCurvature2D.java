/*
 * �Q���� : ��?�̋ȗ���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CurveCurvature2D.java,v 1.3 2007-10-21 21:08:09 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �Q���� : ��?�̋ȗ���\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��
 * ��@?�x�N�g�� normal
 * ��?��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public class CurveCurvature2D extends CurveCurvature {
    /**
     * ��@?�x�N�g��?B
     */
    private final Vector2D normal;

    /**
     * �ȗ��Ǝ�@?�x�N�g����^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param curvature �ȗ�
     * @param normal    ��@?�x�N�g��
     */
    CurveCurvature2D(double curvature,
                     Vector2D normal) {
        super(curvature);

        if (normal == null)
            throw new NullArgumentException();
        this.normal = normal.unitized();
    }

    /**
     * ���̋ȗ��I�u�W�F�N�g�̎�@?�x�N�g����Ԃ�?B
     *
     * @return ��@?�x�N�g��
     * @see Vector2D
     */
    public Vector2D normal() {
        return normal;
    }

    /**
     * ���̋ȗ��I�u�W�F�N�g��?A�^����ꂽ�ȗ��I�u�W�F�N�g������Ƃ݂Ȃ��邩�ۂ���Ԃ�?B
     * <p/>
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̉���?A
     * ��̋ȗ��I�u�W�F�N�g�̎�@?�x�N�g���̂Ȃ��p�x���p�x�̋��e��?����?�����?A
     * ��̋ȗ��I�u�W�F�N�g�̋ȗ��̒l�̋t?���?��������̋��e��?��ȓ�ł����?A
     * ��̋ȗ��I�u�W�F�N�g�͓���ł����̂Ƃ���?B
     * </p>
     *
     * @param mate �����?ۂ̋ȗ��I�u�W�F�N�g
     * @return this �� mate ������̋ȗ��Ƃ݂Ȃ���� true?A�����łȂ���� false
     */
    public boolean identical(CurveCurvature2D mate) {
        if (!this.normal().identicalDirection(mate.normal()))
            return false;

        return CurveCurvature.identical(this.curvature(), mate.curvature());
    }
}

