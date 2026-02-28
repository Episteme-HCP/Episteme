/*
 * �\��Ȃ��G��?[����?��������Ƃ������^�C���ȗ�O�̃N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FatalException.java,v 1.2 2007-10-23 18:24:16 virtualcall Exp $
 */
package org.episteme.util;

/**
 * �\��Ȃ��G��?[����?��������Ƃ������^�C���ȗ�O�̃N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-23 18:24:16 $
 */
public class FatalException extends RuntimeException {
/**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     */
    public FatalException() {
        super();
    }

/**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s ?־
     */
    public FatalException(String s) {
        super(s);
    }
}
