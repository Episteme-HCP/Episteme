/*
 * V ���ɕ����Ȗʂ��^�����邱�Ƃ��҂��Ă����?���?A
 * V ���ɊJ�����Ȗʂ��^����ꂽ���Ƃ���O�̃N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: OpenSurfaceForVDirectionException.java,v 1.2 2007-10-23 18:19:43 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

/**
 * V
 * ���ɕ����Ȗʂ��^�����邱�Ƃ��҂��Ă����?���?A
 * V
 * ���ɊJ�����Ȗʂ��^����ꂽ���Ƃ���O�̃N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-23 18:19:43 $
 */
public class OpenSurfaceForVDirectionException extends Exception {
/**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     */
    public OpenSurfaceForVDirectionException() {
        super();
    }

/**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s ?־
     */
    public OpenSurfaceForVDirectionException(String s) {
        super(s);
    }
}
// end of file
