/*
 * �a�X�v���C����?�̌`?�̓R���\����?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: BsplineCurveForm.java,v 1.3 2007-10-21 21:08:07 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �a�X�v���C����?�̌`?�̓R���\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:07 $
 */

public class BsplineCurveForm extends Types {
    /**
     * �|�����C����\���P���̂a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int POLYLINE_FORM = 0;

    /**
     * �~�̈ꕔ/�S�̂�\���a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int CIRCULAR_ARC = 1;

    /**
     * �ȉ~�̈ꕔ/�S�̂�\���a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int ELLIPTIC_ARC = 2;

    /**
     * ��?�̈ꕔ��\���a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int PARABOLIC_ARC = 3;

    /**
     * �o��?�̈ꕔ��\���a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int HYPERBOLIC_ARC = 4;

    /**
     * �`?�̓R���BɎw�肳��Ȃ��a�X�v���C����?�ł��邱�Ƃ���?�
     */
    public static final int UNSPECIFIED = 5;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private BsplineCurveForm() {
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param curveForm ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int curveForm) {
        switch (curveForm) {
            case POLYLINE_FORM:
                return "POLYLINE_FORM";
            case CIRCULAR_ARC:
                return "CIRCULAR_ARC";
            case ELLIPTIC_ARC:
                return "ELLIPTIC_ARC";
            case PARABOLIC_ARC:
                return "PARABOLIC_ARC";
            case HYPERBOLIC_ARC:
                return "HYPERBOLIC_ARC";
            case UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

