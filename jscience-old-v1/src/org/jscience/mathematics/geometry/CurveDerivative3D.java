/*
 * �R���� : �Ȑ�̓��֐��l��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CurveDerivative3D.java,v 1.3 2007-10-21 21:08:09 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �R���� : �Ȑ�̓��֐��l��\���N���X�B
 * <p/>
 * ���̃N���X�̃C���X�^���X�́A
 * ����Ȑ� P �̂���p�����[�^�l t �ɂ�����
 * �뎟/�ꎟ/��/�O���̓��֐��̒l d0D/d1D/d2D/d3D ��ێ�����B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public class CurveDerivative3D extends CurveDerivative {
    /**
     * �Ȑ��̓_ (�뎟���֐��l) �B
     */
    private final Point3D d0D;

    /**
     * �ꎟ���֐��l�B
     */
    private final Vector3D d1D;

    /**
     * �񎟓��֐��l�B
     */
    private final Vector3D d2D;

    /**
     * �O�����֐��l�B
     */
    private final Vector3D d3D;

    /**
     * �뎟/�ꎟ/��/�O���̓��֐��̒l��^���ăI�u�W�F�N�g��\�z����B
     *
     * @param d0D �Ȑ��̓_ (�뎟���֐��l)
     * @param d1D �ꎟ���֐��l
     * @param d2D �񎟓��֐��l
     * @param d3D �O�����֐��l
     */
    CurveDerivative3D(Point3D d0D,
                      Vector3D d1D,
                      Vector3D d2D,
                      Vector3D d3D) {
        super();
        this.d0D = d0D;
        this.d1D = d1D;
        this.d2D = d2D;
        this.d3D = d3D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̋Ȑ��̓_ (�뎟���֐��l) ��Ԃ��B
     *
     * @return �Ȑ��̓_ (�뎟���֐��l)
     */
    public Point3D d0D() {
        return d0D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̈ꎟ���֐���Ԃ��B
     *
     * @return �ꎟ���֐��l
     */
    public Vector3D d1D() {
        return d1D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̓񎟓��֐���Ԃ��B
     *
     * @return �񎟓��֐��l
     */
    public Vector3D d2D() {
        return d2D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̎O�����֐���Ԃ��B
     *
     * @return �O�����֐��l
     */
    public Vector3D d3D() {
        return d3D;
    }
}

