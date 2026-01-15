/*
 * �R���� : �Ȗʂ̋ȗ���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SurfaceCurvature3D.java,v 1.3 2007-10-21 21:08:19 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �R���� : �Ȗʂ̋ȗ���\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��
 * ��̎�ȗ��ɑΉ�����
 * �P�ʉ����ꂽ����x�N�g�� principalDirection1, principalDirection2
 * ��?��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:19 $
 */

public class SurfaceCurvature3D extends SurfaceCurvature {
    /**
     * �P�ʉ����ꂽ����x�N�g��1 (�Ή������ȗ���?�Βl���傫����) ?B
     */
    private Vector3D principalDirection1;

    /**
     * �P�ʉ����ꂽ����x�N�g��2 (�Ή������ȗ���?�Βl��?�������) ?B
     */
    private Vector3D principalDirection2;

    /**
     * �t�B?|���h�ɒl��?ݒ肷��?B
     * <p/>
     * principalDirection1 ����� principalDirection2 ��
     * ���̃?�\�b�h�̓Ք�ŒP�ʉ������?ݒ肳���?B
     * </p>
     * <p/>
     * �Ȃ�?AprincipalDirection1 ����� principalDirection2 �̂����ꂩ�� null �ł���?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * <p/>
     *
     * @param principalDirection1 ����x�N�g��1 (�Ή������ȗ���?�Βl���傫����)
     * @param principalDirection2 ����x�N�g��2 (�Ή������ȗ���?�Βl��?�������)
     * @see NullArgumentException
     */
    private void setData(Vector3D principalDirection1,
                         Vector3D principalDirection2) {
        if (principalDirection1 == null || principalDirection2 == null)
            throw new NullArgumentException();

        this.principalDirection1 = principalDirection1.unitized();
        this.principalDirection2 = principalDirection2.unitized();
    }

    /**
     * ��̎�ȗ��Ɠ�̎���x�N�g����^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * |principalCurvature1| &lt; |principalCurvature2| �ł���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * principalDirection1 ����� principalDirection2 ��
     * ���̃R���X�g���N�^�̓Ք�ŒP�ʉ������?ݒ肳���?B
     * </p>
     * <p/>
     * �Ȃ�?AprincipalDirection1 ����� principalDirection2 �̂����ꂩ�� null �ł���?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * <p/>
     *
     * @param principalCurvature1 ��ȗ�1 (?�Βl�̑傫����)
     * @param principalDirection1 ����x�N�g��1 (�Ή������ȗ���?�Βl���傫����)
     * @param principalCurvature2 ��ȗ�2 (?�Βl��?�������)
     * @param principalDirection2 ����x�N�g��2 (�Ή������ȗ���?�Βl��?�������)
     * @see InvalidArgumentValueException
     * @see NullArgumentException
     */
    SurfaceCurvature3D(double principalCurvature1,
                       Vector3D principalDirection1,
                       double principalCurvature2,
                       Vector3D principalDirection2) {
        super(principalCurvature1, principalCurvature2);
        setData(principalDirection1, principalDirection2);
    }

    /**
     * ��̎�ȗ��Ɠ�̎���x�N�g����^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * principalCurvature �͓�̗v�f��?�z��łȂ���΂Ȃ�Ȃ�?B
     * ���̔z��̗v�f��?��� 2 �ȊO�ł���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * �܂�?AprincipalCurvature �� null ��?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * �����?A?�?��̗v�f��?�Βl����Ԗڂ̗v�f��?�Βl����?�����?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     * <p/>
     * principalDirection ���̗v�f��?�z��łȂ���΂Ȃ�Ȃ�?B
     * ���̔z��̗v�f��?��� 2 �ȊO�ł���?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * �܂�?AprincipalDirection�� null �ł���?�?��ɂ�
     * NullArgumentException �̗�O��?�����?B
     * �z���̃x�N�g���͂��ꂼ�ꂪ
     * ���̃R���X�g���N�^�̓Ք�ŒP�ʉ������?ݒ肳���?B
     * </p>
     * <p/>
     * �Ȃ�?AprincipalDirection[i] �� principalCurvature[i] �ɑΉ�����?B
     * </p>
     *
     * @param principalCurvature ��ȗ��̔z��
     * @param principalDirection ����x�N�g���̔z��
     * @see InvalidArgumentValueException
     * @see NullArgumentException
     */
    SurfaceCurvature3D(double[] principalCurvature,
                       Vector3D[] principalDirection) {
        super(principalCurvature);

        if (principalDirection == null)
            throw new NullArgumentException();

        if (principalDirection.length != 2)
            throw new InvalidArgumentValueException();

        setData(principalDirection[0], principalDirection[1]);
    }

    /**
     * ����x�N�g��1 (�Ή������ȗ���?�Βl���傫����) ��Ԃ�?B
     *
     * @return �P�ʉ����ꂽ����x�N�g��1 (�Ή������ȗ���?�Βl���傫����)
     */
    public Vector3D principalDirection1() {
        return principalDirection1;
    }

    /**
     * ����x�N�g��2 (�Ή������ȗ���?�Βl��?�������) ��Ԃ�?B
     *
     * @return �P�ʉ����ꂽ����x�N�g��2 (�Ή������ȗ���?�Βl��?�������)
     */
    public Vector3D principalDirection2() {
        return principalDirection2;
    }
}

