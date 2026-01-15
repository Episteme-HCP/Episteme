/*
 * �R���� : ��ȖʊԂ̊���\���C���^�[�t�F�C�X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: SurfaceSurfaceInterference3D.java,v 1.3 2007-10-21 21:08:20 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �R���� : ��ȖʊԂ̊���\���C���^�[�t�F�C�X�B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:20 $
 */

public interface SurfaceSurfaceInterference3D extends Interference3D {
    /**
     * ���̊������ł��邩�ۂ���Ԃ��B
     *
     * @return ���ł���� true�A�����łȂ���� false
     */
    public boolean isIntersectionCurve();

    /**
     * ���̊�����ɕϊ�����B
     * <p/>
     * ���ɕϊ��ł��Ȃ��ꍇ�� null ��Ԃ��B
     * </p>
     *
     * @return ���
     */
    public IntersectionCurve3D toIntersectionCurve();
}
