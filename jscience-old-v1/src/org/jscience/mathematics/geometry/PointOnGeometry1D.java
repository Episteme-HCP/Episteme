/*
 * �P���� : ����`?�v�f��?�ɂ���_��\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PointOnGeometry1D.java,v 1.2 2006/03/01 21:16:07 virtualcall Exp $
 *
 */

package org.jscience.mathematics.geometry;

/**
 * �P���� : ����`?�v�f��?�ɂ���_��\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �`?�v�f��?�ɂ���_��?W�l ({@link Point1D Point1D}) point
 * ��ێ?����?B
 * </p>
 * <p/>
 * point �� null �ł�?\��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:07 $
 */

public abstract class PointOnGeometry1D extends Point1D {
    /**
     * �`?�v�f��?�ɂ���_��?W�l?B
     * <p/>
     * null �ł�?\��Ȃ�?B
     * </p>
     *
     * @serial
     */
    private Point1D point;

    /**
     * �`?�v�f��?�ɂ���_��?W�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     *
     * @param point �`?�v�f?�̓_��?W�l
     */
    protected PointOnGeometry1D(Point1D point) {
        super();
        this.point = point;
    }

    /**
     * ���̓_�� X ?W�l��Ԃ�?B
     *
     * @return �_�� X ?W�l
     */
    public double x() {
        if (point == null) {
            point = coordinates();
        }
        return point.x();
    }

    /**
     * �x?[�X�ƂȂ�`?�v�f��Ԃ���?ۃ?�\�b�h?B
     *
     * @return �x?[�X�ƂȂ�`?�v�f
     */
    public abstract GeometryElement geometry();

    /**
     * �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�_��?W�l��?�߂钊?ۃ?�\�b�h?B
     *
     * @return �x?[�X�ƂȂ�`?�v�f�ɑ΂���?�񂩂�?�߂��_��?W�l
     */
    abstract Point1D coordinates();
}
