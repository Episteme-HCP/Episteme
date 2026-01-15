/*
 * �R���� : ����`?�v�f��?�ɂ���_��\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PointOnGeometry3D.java,v 1.2 2006/03/01 21:16:07 virtualcall Exp $
 *
 */

package org.jscience.mathematics.geometry;

/**
 * �R���� : ����`?�v�f��?�ɂ���_��\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �`?�v�f��?�ɂ���_��?W�l ({@link Point3D Point3D}) point
 * ��ێ?����?B
 * </p>
 * <p/>
 * point �� null �ł�?\��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:07 $
 */

public abstract class PointOnGeometry3D extends Point3D {
    /**
     * �`?�v�f��?�ɂ���_��?W�l?B
     * <p/>
     * null �ł�?\��Ȃ�?B
     * </p>
     *
     * @serial
     */
    private Point3D point;

    /**
     * �`?�v�f��?�ɂ���_��?W�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * point �� null �ł�?\��Ȃ�?B
     * </p>
     *
     * @param point �`?�v�f?�̓_��?W�l
     */
    protected PointOnGeometry3D(Point3D point) {
        super();
        this.point = point;
    }

    /**
     * �`?�v�f��?�ɂ���_��?W�l��Ԃ�?B
     * <p/>
     * null ���Ԃ邱�Ƃ �蓾��?B
     * </p>
     *
     * @return �`?�v�f��?�ɂ���_��?W�l
     */
    public Point3D point() {
        return point;
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
     * ���̓_�� Y ?W�l��Ԃ�?B
     *
     * @return �_�� Y ?W�l
     */
    public double y() {
        if (point == null) {
            point = coordinates();
        }
        return point.y();
    }

    /**
     * ���̓_�� Z ?W�l��Ԃ�?B
     *
     * @return �_�� Z ?W�l
     */
    public double z() {
        if (point == null) {
            point = coordinates();
        }
        return point.z();
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
    abstract Point3D coordinates();
}
