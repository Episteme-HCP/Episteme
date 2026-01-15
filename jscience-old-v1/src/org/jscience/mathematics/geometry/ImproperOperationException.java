/*
 * �L��ȈӖ���?���Ȃ�?A���邢�͗L��Ȍ��ʂ��Ȃ�
 * �@�\�ł��邱�Ƃ������^�C���ȗ�O�̃N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ImproperOperationException.java,v 1.2 2007-10-21 17:38:23 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �L��ȈӖ���?���Ȃ�?A���邢�͗L��Ȍ��ʂ��Ȃ�
 * �@�\�ł��邱�Ƃ������^�C���ȗ�O�̃N���X
 * <ul>
 * <li>��?�E�Ȗʂɑ΂���?AtoBsplineSurface()��toMesh()���?s���悤�Ƃ���?B
 * <li>�X�C?[�v�ʂƂ̌�?��?�߂悤�Ƃ���?B
 * </ul>
 * �Ȃ�
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:23 $
 */

public class ImproperOperationException extends RuntimeException {
    /**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     */
    public ImproperOperationException() {
        super();
    }

    /**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s ?־
     */
    public ImproperOperationException(String s) {
        super(s);
    }
}

// end of file
