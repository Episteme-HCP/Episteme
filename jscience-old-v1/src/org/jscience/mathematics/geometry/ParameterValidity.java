/*
 * ����p�����[�^�l�́A���e�덷��l��������ł́A
 * �􉽗v�f�̒�`��ɑ΂���ʒu (������) ��\���l (�萔) ��ێ�����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ParameterValidity.java,v 1.2 2006/03/01 21:16:06 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * ����p�����[�^�l�́A���e�덷��l��������ł́A
 * �􉽗v�f�̒�`��ɑ΂���ʒu (������) ��\���l (�萔) ��ێ�����N���X�B
 * <p/>
 * ���̃N���X�̃C���X�^���X�͍��Ȃ��B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:06 $
 */

public class ParameterValidity extends Types {
    /**
     * ��`���ɂ��邱�Ƃ��萔
     */
    public static final int PROPERLY_INSIDE = 0;

    /**
     * ���e�덷��l�����āA��`�扺�ɂ��邱�Ƃ��萔
     */
    public static final int TOLERATED_LOWER_LIMIT = 1;

    /**
     * ���e�덷��l�����āA��`���ɂ��邱�Ƃ��萔
     */
    public static final int TOLERATED_UPPER_LIMIT = 2;

    /**
     * ��`���ɂȂ����Ƃ��萔
     */
    public static final int OUTSIDE = 3;

    /**
     * ���̃N���X�̃C���X�^���X�͍��Ȃ��B
     */
    private ParameterValidity() {
    }
}

