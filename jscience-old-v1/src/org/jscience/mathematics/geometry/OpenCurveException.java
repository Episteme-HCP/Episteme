/*
 * ������?�^�����邱�Ƃ��҂��Ă����?���?A
 * �J������?�^����ꂽ���Ƃ���O�̃N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: OpenCurveException.java,v 1.2 2007-10-21 17:38:25 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

/**
 * ������?�^�����邱�Ƃ��҂��Ă����?���?A
 * <p/>
 * �J������?�^����ꂽ���Ƃ���O�̃N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:25 $
 */
public class OpenCurveException extends Exception {
    /**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     */
    public OpenCurveException() {
        super();
    }

    /**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s ?־
     */
    public OpenCurveException(String s) {
        super(s);
    }
}

// end of file
