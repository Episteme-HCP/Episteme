/*
 * �R���� : ��􉽗v�f�Ԃ̊���\���C���^�[�t�F�C�X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Interference3D.java,v 1.3 2007-10-21 21:08:13 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �R���� : ��􉽗v�f�Ԃ̊���\���C���^�[�t�F�C�X�B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:13 $
 */

public interface Interference3D {
    /**
     * ���̊�����_�ł��邩�ۂ���Ԃ��B
     *
     * @return ��_�ł���� true�A�����łȂ���� false
     */
    public boolean isIntersectionPoint();

    /**
     * ���̊����_�ɕϊ�����B
     * <p/>
     * ��_�ɕϊ��ł��Ȃ��ꍇ�� null ��Ԃ��B
     * </p>
     *
     * @return ��_
     */
    public IntersectionPoint3D toIntersectionPoint();
}
