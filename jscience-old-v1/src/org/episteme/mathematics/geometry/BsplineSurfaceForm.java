/*
 * �a�X�v���C���Ȗʂ̌`?�̓R���\����?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: BsplineSurfaceForm.java,v 1.3 2007-10-21 21:08:07 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �a�X�v���C���Ȗʂ̌`?�̓R���\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:07 $
 */

public class BsplineSurfaceForm extends Types {
    /**
     * ���ʂ̈ꕔ��\���o�ꎟ�̂a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int PLANE_SURF = 0;

    /**
     * �~���̈ꕔ��\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int CYLINDRICAL_SURF = 1;

    /**
     * ���~??�̈ꕔ��\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int CONICAL_SURF = 2;

    /**
     * ���̈ꕔ/�S�̂�\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int SPHERICAL_SURF = 3;

    /**
     * �g?[���X�̈ꕔ/�S�̂�\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int TOROIDAL_SURF = 4;

    /**
     * ��]�ʂ̈ꕔ��\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int SURF_OF_REVOLUTION = 5;

    /**
     * ��̃p���?�g���b�N�ȋ�?�̓����p���??[�^�ʒu��
     * ��?�Ō���łł���Ȗʂ�\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int RULED_SURF = 6;

    /**
     * RULED_SURF �̓Uʂ�?�?���?A��Ԗڂ̋�?�_��?k�ނ������?A
     * ���Ȃ킿��ӂ�?���_����_��?k�ނ����a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int GENERALISED_CONE = 7;

    /**
     * �Q���Ȗʂ̈ꕔ��\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int QUADRIC_SURF = 8;

    /**
     * ���ʂ�\�� (���ꎟ��) �a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int SURF_OF_LINEAR_EXTRUSION = 9;

    /**
     * �`?�̓R���BɎw�肳��Ȃ��a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int UNSPECIFIED = 10;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private BsplineSurfaceForm() {
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param surfaceForm ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int surfaceForm) {
        switch (surfaceForm) {
            case PLANE_SURF:
                return "PLANE_SURF";
            case CYLINDRICAL_SURF:
                return "CYLINDRICAL_SURF";
            case CONICAL_SURF:
                return "CONICAL_SURF";
            case SPHERICAL_SURF:
                return "SPHERICAL_SURF";
            case TOROIDAL_SURF:
                return "TOROIDAL_SURF";
            case SURF_OF_REVOLUTION:
                return "SURF_OF_REVOLUTION";
            case RULED_SURF:
                return "RULED_SURF";
            case GENERALISED_CONE:
                return "GENERALISED_CONE";
            case QUADRIC_SURF:
                return "QUADRIC_SURF";
            case SURF_OF_LINEAR_EXTRUSION:
                return "SURF_OF_LINEAR_EXTRUSION";
            case UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

