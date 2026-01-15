/*
 * �Q�����̔�p���?�g���b�N�ȋ�?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: NonParametricCurve2D.java,v 1.3 2007-10-21 21:08:15 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �Q�����̔�p���?�g���b�N�ȋ�?�̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:15 $
 */

public abstract class NonParametricCurve2D extends AbstractNonParametricCurve {
    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    protected NonParametricCurve2D() {
        super();
    }

    /**
     * ���̋�?�̎�����Ԃ�?B
     * <p/>
     * ?�� 2 ��Ԃ�?B
     * </p>
     *
     * @return �Q�����Ȃ̂�?A?�� 2
     */
    public int dimension() {
        return 2;
    }

    /**
     * ���̋�?�Q�������ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �Q�����Ȃ̂�?A?�� true
     */
    public boolean is2D() {
        return true;
    }
}

/* end of file */
