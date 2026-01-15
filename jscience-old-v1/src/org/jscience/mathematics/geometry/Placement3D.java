/*
 * �􉽗v�f�̒u�����ʒu����\���R�����̔z�u?��̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Placement3D.java,v 1.2 2006/03/01 21:16:06 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �􉽗v�f�̒u�����ʒu����\���R�����̔z�u?��̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * �R�����̔z�u?���?A
 * �R�����̋�?��I��?W�n (���_?A�w��?A�x��?A�y��) ���߂�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:06 $
 */

public abstract class Placement3D extends AbstractPlacement {
    /**
     * �􉽗v�f�̒u�����ʒu���_?B
     * <p/>
     * ���̓_��?A���̔z�u?�񂪒�߂��?�?W�n�̌��_�ƂȂ�?B
     * </p>
     *
     * @serial
     */
    private final Point3D location;

    /**
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * location �̒l�� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param location �􉽗v�f�̒u�����ʒu���_
     */
    protected Placement3D(Point3D location) {
        super();
        if (location == null)
            throw new InvalidArgumentValueException();

        this.location = location;
    }

    /**
     * ���̔z�u?��̎�����Ԃ�?B
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
     * ���̔z�u?�񂪂R�������ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �R�����Ȃ̂�?A?�� true
     */
    public boolean is3D() {
        return true;
    }

    /**
     * �􉽗v�f�̒u�����ʒu���_��Ԃ�?B
     * <p/>
     * ���̓_��?A���̔z�u?�񂪒�߂��?�?W�n�̌��_�ł���?B
     * </p>
     *
     * @return �􉽗v�f�̒u�����ʒu���_
     */
    public Point3D location() {
        return location;
    }
}

