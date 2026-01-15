/*
 * ��?�邢�͋Ȗʂł��邱�Ƃ���҂�����?���?A
 * ���ꂪ�_��?k�ނ��Ă��邱�Ƃ���O�̃N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ReducedToPointException.java,v 1.3 2007-10-21 21:08:19 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * ��?�邢�͋Ȗʂł��邱�Ƃ���҂�����?���?A
 * ���ꂪ�_��?k�ނ��Ă��邱�Ƃ���O�̃N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��҂�����?�邢�͋Ȗʂ�?k�ނ������ʂł���_ (point) ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:19 $
 */

public class ReducedToPointException extends Exception {
    /**
     * ��?�邢�͋Ȗʂ�?k�ނ����_
     *
     * @serial
     */
    private final AbstractPoint point;

    /**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     *
     * @param point ��?�邢�͋Ȗʂ�?k�ނ����_
     */
    public ReducedToPointException(AbstractPoint point) {
        super();
        this.point = point;
    }

    /**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s     ?־
     * @param point ��?�邢�͋Ȗʂ�?k�ނ����_
     */
    public ReducedToPointException(String s, AbstractPoint point) {
        super(s);
        this.point = point;
    }

    /**
     * ��?�邢�͋Ȗʂ�?k�ނ����_��Ԃ�?B
     *
     * @return ��?�邢�͋Ȗʂ�?k�ނ����_
     */
    public AbstractPoint point() {
        return this.point;
    }
}

