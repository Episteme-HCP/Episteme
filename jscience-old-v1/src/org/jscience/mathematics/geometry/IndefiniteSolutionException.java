/*
 * �⪕s��ł��� (��?��̉⪑�?݂���) ���Ƃ���O�̃N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: IndefiniteSolutionException.java,v 1.2 2007-10-21 17:38:23 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �⪕s��ł��� (��?��̉⪑�?݂���) ���Ƃ���O�̃N���X?B
 * <p/>
 * ���̃N���X��?A���鉉�Z�̉⪕s��ł��� (��?��̉⪑�?݂���) ���Ƃ�
 * �����̂ɗ��p�����?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ���鉉�Z�ɑ΂��閳?��̉�̓�̓K���Ȉ�̉� (suitable) ��
 * GeometryElement �Ƃ��ĕێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:23 $
 */

public class IndefiniteSolutionException extends Exception {
    /**
     * ��?��̉�̓�̓K���Ȉ��?B
     *
     * @serial
     */
    private final GeometryElement suitable;

    /**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     *
     * @param suitable ��?��̉�̓�̓K���Ȉ�̉�
     */
    public IndefiniteSolutionException(GeometryElement suitable) {
        super();
        this.suitable = suitable;
    }

    /**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s        ?־
     * @param suitable ��?��̉�̓�̓K���Ȉ�̉�
     */
    public IndefiniteSolutionException(String s, GeometryElement suitable) {
        super(s);
        this.suitable = suitable;
    }

    /**
     * ��?��̉�̓�̓K���Ȉ�̉��Ԃ�?B
     *
     * @return ��?��̉�̓�̓K���Ȉ�̉�
     */
    public GeometryElement suitable() {
        return this.suitable;
    }
}

