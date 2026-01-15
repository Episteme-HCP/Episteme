/*
 * �Q���� : �Ȑ�̓��֐���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CurveDerivative2D.java,v 1.3 2007-10-21 21:08:09 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �Q���� : �Ȑ�̓��֐���\���N���X�B
 * <p/>
 * ���̃N���X�̃C���X�^���X�́A
 * ����Ȑ� P �̂���p�����[�^�l t �ɂ�����
 * �뎟/�ꎟ/�񎟂̓��֐��̒l d0D/d1D/d2D ��ێ�����B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public class CurveDerivative2D extends CurveDerivative {
    /**
     * �Ȑ��̓_ (�뎟���֐��l) �B
     */
    private final Point2D d0D;

    /**
     * �ꎟ���֐��l�B
     */
    private final Vector2D d1D;

    /**
     * �񎟓��֐��l�B
     */
    private final Vector2D d2D;

    /**
     * �뎟/�ꎟ/�񎟂̓��֐��̒l��^���ăI�u�W�F�N�g��\�z����B
     *
     * @param d0D �Ȑ��̓_ (�뎟���֐��l)
     * @param d1D �ꎟ���֐��l
     * @param d2D �񎟓��֐��l
     */
    CurveDerivative2D(Point2D d0D,
                      Vector2D d1D,
                      Vector2D d2D) {
        super();
        this.d0D = d0D;
        this.d1D = d1D;
        this.d2D = d2D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̋Ȑ��̓_ (�뎟���֐��l) ��Ԃ��B
     *
     * @return �Ȑ��̓_ (�뎟���֐��l)
     */
    public Point2D d0D() {
        return d0D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̈ꎟ���֐���Ԃ��B
     *
     * @return �ꎟ���֐��l
     */
    public Vector2D d1D() {
        return d1D;
    }

    /**
     * ���̓��֐��l�I�u�W�F�N�g�̓񎟓��֐���Ԃ��B
     *
     * @return �񎟓��֐��l
     */
    public Vector2D d2D() {
        return d2D;
    }

    /**
     * �f�o�b�O�p���C���v���O�����B
     */
    public static void main(String[] args) {
        Point2D d0D = Point2D.origin;
        Vector2D d1D = Vector2D.xUnitVector;
        Vector2D d2D = Vector2D.yUnitVector;
        CurveDerivative2D deriv;

        deriv = new CurveDerivative2D(d0D, d1D, d2D);
        System.out.println(deriv);
    }
}

