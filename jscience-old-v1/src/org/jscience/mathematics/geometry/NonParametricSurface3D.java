/*
 * �R�����̔�p���?�g���b�N�ȋȖʂ̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: NonParametricSurface3D.java,v 1.3 2007-10-21 21:08:15 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �R�����̔�p���?�g���b�N�ȋȖʂ̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:15 $
 */

public abstract class NonParametricSurface3D extends AbstractNonParametricSurface {
    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    public NonParametricSurface3D() {
        super();
    }

    /**
     * ���̋Ȗʂ̎�����Ԃ�?B
     * <p/>
     * ?�� 3 ��Ԃ�?B
     * </p>
     *
     * @return �R�����Ȃ̂�?A?�� 3
     */
    public int dimension() {
        return 3;
    }

    /**
     * ���̋Ȗʂ��R�������ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �R�����Ȃ̂�?A?�� true
     */
    public boolean is3D() {
        return true;
    }
}

/* end of file */
