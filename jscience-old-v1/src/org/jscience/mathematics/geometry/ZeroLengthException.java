/*
 * ��?��x�N�g�����̒������[�?�ɓ��������߂�
 * ?��?����?s�ł��Ȃ����Ƃ������^�C���ȗ�O�̃N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ZeroLengthException.java,v 1.2 2007-10-21 17:38:28 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

/**
 * ��?��x�N�g�����̒������[�?�ɓ��������߂�
 * <p/>
 * ?��?����?s�ł��Ȃ����Ƃ������^�C���ȗ�O�̃N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:28 $
 */
public class ZeroLengthException extends RuntimeException {
    /**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     */
    public ZeroLengthException() {
        super();
    }

    /**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s ?־
     */
    public ZeroLengthException(String s) {
        super(s);
    }
}
