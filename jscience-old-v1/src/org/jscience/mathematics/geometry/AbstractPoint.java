/*
 * �_�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: AbstractPoint.java,v 1.2 2007-10-21 17:38:21 virtualcall Exp $
 *
 */

package org.jscience.mathematics.geometry;

/**
 * �_�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:21 $
 */

public abstract class AbstractPoint extends GeometryElement {
    /**
     * �I�u�W�F�N�g��?\�z����?B
     */
    protected AbstractPoint() {
        super();
    }

    /**
     * �_���ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �_�Ȃ̂�?A?�� <code>true</code>
     */
    public boolean isPoint() {
        return true;
    }
}
