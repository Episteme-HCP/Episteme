/*
 * �􉽗v�f�̂ǂ��瑤����\����?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: WhichSide.java,v 1.3 2007-10-21 21:08:21 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �􉽗v�f�̂ǂ��瑤����\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:21 $
 */

public class WhichSide extends Types {
    /**
     * ��������?�
     */
    public static final int BOTH = -1;

    /**
     * �v�f?����?�
     */
    public static final int ON = 0;

    /**
     * �E������?�
     */
    public static final int RIGHT = 1;

    /**
     * ?�������?�
     */
    public static final int LEFT = 2;

    /**
     * �O������?�
     */
    public static final int FRONT = 3;

    /**
     * ��둤����?�
     */
    public static final int BACK = 4;

    /**
     * �Ѥ����?�
     */
    public static final int IN = 5;

    /**
     * �O������?�
     */
    public static final int OUT = 6;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private WhichSide() {
    }

    /**
     * ��?��l�ŗ^����ꂽ?u��?v�Ƃ͋t�̑�����?���Ԃ�?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����?u��?v?A�µ���͂��̋t�̑�����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     * <p/>
     * BOTH ����� ON �ɂ͋t�̑�����?݂��Ȃ�?B
     * </p>
     *
     * @param value ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �t�̑�����?�
     * @see InvalidArgumentValueException
     */
    public static int reverse(int value) {
        switch (value) {
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            case FRONT:
                return BACK;
            case BACK:
                return FRONT;
            case IN:
                return OUT;
            case OUT:
                return IN;
        }
        throw new InvalidArgumentValueException();
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
            case BOTH:
                return "BOTH";
            case ON:
                return "ON";
            case RIGHT:
                return "RIGHT";
            case LEFT:
                return "LEFT";
            case FRONT:
                return "FRONT";
            case BACK:
                return "BACK";
            case IN:
                return "IN";
            case OUT:
                return "OUT";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

