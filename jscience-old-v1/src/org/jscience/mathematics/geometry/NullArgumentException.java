/*
 * �R���X�g���N�^��?�\�b�h�ɗ^����ꂽ��?��̒l�� null �ł��邱�Ƃ�
 * �����^�C���ȗ�O�̃N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: NullArgumentException.java,v 1.2 2007-10-23 18:19:43 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

/**
 * �R���X�g���N�^��?�\�b�h�ɗ^����ꂽ��?��̒l��
 * null �ł��邱�Ƃ�
 * �����^�C���ȗ�O�̃N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-23 18:19:43 $
 */
public class NullArgumentException extends InvalidArgumentValueException {
/**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     */
    public NullArgumentException() {
        super();
    }

/**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s ?־
     */
    public NullArgumentException(String s) {
        super(s);
    }
}
