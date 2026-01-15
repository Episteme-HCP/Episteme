/*
 * �R���� : ��?��̈ʒu��\���C���^�[�t�F�[�X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ParameterRangeOnCurve3D.java,v 1.2 2007-10-23 18:19:44 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

/**
 * �R���� :
 * ��?��̈ʒu��\���C���^�[�t�F�[�X�B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-23 18:19:44 $
 */
public interface ParameterRangeOnCurve3D {
    /**
     * �_���ۂ���Ԃ��B
     *
     * @return �_�Ȃ�� true�A�����łȂ���� false
     */
    public boolean isPoint();

    /**
     * ��Ԃ��ۂ���Ԃ�
     *
     * @return ��ԂȂ�� true�A�����łȂ���� false
     */
    public boolean isSection();
}
// end of file
