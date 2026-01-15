/*
 * ��?[�v���ǂ�����ł��邩��\����?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: LoopWise.java,v 1.3 2007-10-21 21:08:14 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * ��?[�v���ǂ�����ł��邩��\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:14 $
 */

public class LoopWise extends Types {
    /**
     * CCW:	�����v��� (?����) �ł��邱�Ƃ���?�
     */
    public static final int CCW = 0;

    /**
     * CW:	���v��� (�E���) �ł��邱�Ƃ���?�
     */
    public static final int CW = 1;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private LoopWise() {
    }

    /**
     * ��?��l�ŗ^����ꂽ����Ƃ͋t�̉������?���Ԃ�?B
     * <p/>
     * �^����ꂽ�l�ɑΉ��������?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param value ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �t�̉������?�
     * @see InvalidArgumentValueException
     */
    public static int reverse(int value) {
        switch (value) {
            case CCW:
                return CW;
            case CW:
                return CCW;
            default:
                throw new InvalidArgumentValueException();
        }
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param value ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int value) {
        switch (value) {
            case CCW:
                return "CCW";
            case CW:
                return "CW";
            default:
                throw new InvalidArgumentValueException();
        }
    }

}

