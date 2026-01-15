/*
 * ?u����?v��܂ޘ_�?�l��\����?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: TrueFalseUndefined.java,v 1.3 2007-10-21 21:08:21 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * ?u����?v��܂ޘ_�?�l��\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:21 $
 */
public class TrueFalseUndefined extends Types {
    /**
     * ����ł��邱�Ƃ���?�
     */
    public static final int UNDEFINED = -1;

    /**
     * �U�ł��邱�Ƃ���?�
     */
    public static final int FALSE = 0;

    /**
     * ?^�ł��邱�Ƃ���?�
     */
    public static final int TRUE = 1;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private TrueFalseUndefined() {
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
            case UNDEFINED:
                return "UNDEFINED";
            case FALSE:
                return "FALSE";
            case TRUE:
                return "TRUE";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

