/*
 * ��p���?�g���b�N�ȋ�?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: AbstractNonParametricCurve.java,v 1.3 2007-10-21 21:08:05 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * ��p���?�g���b�N�ȋ�?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:05 $
 */

public abstract class AbstractNonParametricCurve extends GeometryElement {
    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    protected AbstractNonParametricCurve() {
        super();
    }

    /**
     * ��?�ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return ��?�Ȃ̂�?A?�� <code>true</code>
     */
    public boolean isCurve() {
        return true;
    }
}

/* end of file */
