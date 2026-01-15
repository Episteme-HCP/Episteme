/*
 * �􉽗v�f�̒u�����ʒu����\���z�u?��̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: AbstractPlacement.java,v 1.2 2006/03/01 21:15:51 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �􉽗v�f�̒u�����ʒu����\���z�u?��̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * �z�u?���?A��?��I��?W�n���߂�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:51 $
 */

public abstract class AbstractPlacement extends GeometryElement {
    /**
     * �I�u�W�F�N�g��?\�z����?B
     */
    protected AbstractPlacement() {
        super();
    }

    /**
     * �z�u?�񂩔ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �z�u?��Ȃ̂�?A?�� true
     */
    public boolean isPlacement() {
        return true;
    }
}

