/*
 * �g���~���O�ʒu�̊�?���\����?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: TrimmingPreference.java,v 1.2 2006/03/01 21:16:12 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �g���~���O�ʒu�̊�?���\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:12 $
 */

public class TrimmingPreference extends Types {
    /**
     * ��Ԃł̓_���?��Ƃ��邱�Ƃ���?�?B
     */
    public final static int POINT = 0;

    /**
     * �p���??[�^�l���?��Ƃ��邱�Ƃ���?�?B
     */
    public final static int PARAMETER = 1;

    /**
     * �BɊ�?��̂Ȃ����Ƃ���?�?B
     */
    public final static int UNSPECIFIED = 2;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private TrimmingPreference() {
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param masterRepresentation ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int masterRepresentation) {
        switch (masterRepresentation) {
            case POINT:
                return "POINT";
            case PARAMETER:
                return "PARAMETER";
            case UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}


