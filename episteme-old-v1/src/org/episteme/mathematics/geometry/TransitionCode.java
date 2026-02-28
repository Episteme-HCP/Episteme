/*
 * �A��?���\����?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: TransitionCode.java,v 1.3 2007-10-21 21:08:21 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �A��?���\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:21 $
 */

public class TransitionCode extends Types {
    /**
     * �s�A���ł��邱�Ƃ���?�
     */
    public final static int DISCONTINUOUS = 0;

    /**
     * �A���ł��邱�Ƃ���?�
     */
    public final static int CONTINUOUS = 1;

    /**
     * ?�?�A���ł��邱�Ƃ���?�
     */
    public final static int CONT_SAME_GRADIENT = 2;

    /**
     * �ȗ��A���ł��邱�Ƃ���?�
     */
    public final static int CONT_SAME_GRADIENT_SAME_CURVATURE = 3;

    /**
     * �A��?����s���ł��邱�Ƃ���?�
     */
    public final static int UNKNOWN = 4;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private TransitionCode() {
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param transition ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int transition) {
        switch (transition) {
            case DISCONTINUOUS:
                return "DISCONTINUOUS";
            case CONTINUOUS:
                return "CONTINUOUS";
            case CONT_SAME_GRADIENT:
                return "CONT_SAME_GRADIENT";
            case CONT_SAME_GRADIENT_SAME_CURVATURE:
                return "CONT_SAME_GRADIENT_SAME_CURVATURE";
            case UNKNOWN:
                return "UNKNOWN";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}
