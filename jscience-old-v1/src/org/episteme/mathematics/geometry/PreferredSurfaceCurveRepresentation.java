/*
 * �����̖�?�?�?^��?��\����?��̋�?�\���̓�̂������D?悵�ĎQ?Ƃ��ׂ�������?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PreferredSurfaceCurveRepresentation.java,v 1.3 2007-10-21 21:08:18 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �����̖�?�?�?^��?��\����?��̋�?�\���̓�̂������D?悵�ĎQ?Ƃ��ׂ�������?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:18 $
 * @see SurfaceCurve3D
 * @see IntersectionCurve3D
 */

public class PreferredSurfaceCurveRepresentation extends Types {
    /**
     * �R�����\�� (curve3d) ��D?悳���邱�Ƃ���?�?B
     */
    public final static int CURVE_3D = 0;

    /**
     * ����Ȗʂ̃p���??[�^��Ԃł̂Q�����\�� (curve2d1) ��D?悳���邱�Ƃ���?�?B
     */
    public final static int CURVE_2D_1 = 1;

    /**
     * �¤���̋Ȗʂ̃p���??[�^��Ԃł̂Q�����\�� (curve2d2) ��D?悳���邱�Ƃ���?�?B
     */
    public final static int CURVE_2D_2 = 2;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private PreferredSurfaceCurveRepresentation() {
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
            case CURVE_3D:
                return "CURVE_3D";
            case CURVE_2D_1:
                return "CURVE_2D_1";
            case CURVE_2D_2:
                return "CURVE_2D_2";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

