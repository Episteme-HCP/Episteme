/*
 * �a�X�v���C���̃m�b�g��̎�ʂ�\����?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: KnotType.java,v 1.3 2007-10-21 21:08:14 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �a�X�v���C���̃m�b�g��̎�ʂ�\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:14 $
 */

public class KnotType extends Types {
    /**
     * �S��ɓn�Bă��j�t�H?[���ȃm�b�g��ł��邱�Ƃ���?�?B
     */
    public static final int UNIFORM_KNOTS = 0;

    /**
     * �BɓR���w�肳��Ȃ���ʂ̃m�b�g��ł��邱�Ƃ���?�?B
     */
    public static final int UNSPECIFIED = 1;

    /**
     * ���[�� (��?�+1) �̑�?d�x��?�B����j�t�H?[���ȃm�b�g��ł��邱�Ƃ���?�?B
     * <p/>
     * �����_�� JGCL �ł�?A���̎�̃m�b�g���?�a�X�v���C���ɂ͖��Ή�
     * </p>
     */
    public static final int QUASI_UNIFORM_KNOTS = 2;

    /**
     * �敪�x�W�G��?�ɑΉ������m�b�g��ł��邱�Ƃ���?�
     * <p/>
     * �����_�� JGCL �ł�?A���̎�̃m�b�g���?�a�X�v���C���ɂ͖��Ή�
     * </p>
     */
    public static final int PIECEWISE_BEZIER_KNOTS = 3;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private KnotType() {
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param knotSpec ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int knotSpec) {
        switch (knotSpec) {
            case UNIFORM_KNOTS:
                return "UNIFORM_KNOTS";
            case UNSPECIFIED:
                return "UNSPECIFIED";
            case QUASI_UNIFORM_KNOTS:
                return "QUASI_UNIFORM_KNOTS";
            case PIECEWISE_BEZIER_KNOTS:
                return "PIECEWISE_BEZIER_KNOTS";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

