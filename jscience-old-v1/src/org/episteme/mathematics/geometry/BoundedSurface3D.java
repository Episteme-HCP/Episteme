/*
 * �R���� : �L�ȋȖʂ�\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: BoundedSurface3D.java,v 1.2 2006/03/01 21:15:53 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import org.episteme.util.FatalException;

/**
 * �R���� : �L�ȋȖʂ�\����?ۃN���X?B
 * <p/>
 * ���̃N���X�ɂ̓C���X�^���X���?�ׂ���?��͂Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:53 $
 */

public abstract class BoundedSurface3D extends ParametricSurface3D {
    /**
     * ����^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    protected BoundedSurface3D() {
        super();
    }

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂�?A�^����ꂽ��?��ŕ��ʋߎ�����
     * �i�q�_�Q��Ԃ���?ۃ?�\�b�h?B
     * <p/>
     * ���ʂƂ��ĕԂ����i�q�_�Q��?\?�����_��?A
     * ���̋Ȗʂ�x?[�X�Ƃ��� PointOnSurface3D ��
     * ���邱�Ƃ��҂ł���?B
     * </p>
     *
     * @param tol �����̋��e��?�
     * @return ���̗L�ȖʑS�̂𕽖ʋߎ�����i�q�_�Q
     * @see PointOnSurface3D
     */
    public abstract Mesh3D toMesh(ToleranceForDistance tol);

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂쵖���?Č�����
     * �L�? Bspline �Ȗʂ�Ԃ�?B
     *
     * @return ���̗L�ȖʑS�̂�?Č�����L�? Bspline �Ȗ�
     */
    public BsplineSurface3D toBsplineSurface() {
        try {
            return this.toBsplineSurface(this.uParameterDomain().section(),
                    this.vParameterDomain().section());
        } catch (ParameterOutOfRange e) {
            throw new FatalException("Something wrong. Maybe JGCL's bug");
        }
    }

    /**
     * ���� (��`�̃p���??[�^��`���?��) �L�ȖʑS�̂�I�t�Z�b�g�����Ȗʂ�
     * �^����ꂽ��?��ŋߎ����� Bspline �Ȗʂ�?�߂�?B
     *
     * @param magni �I�t�Z�b�g��
     * @param side  �I�t�Z�b�g�̌� (WhichSide.FRONT/BACK)
     * @param tol   �����̋��e��?�
     * @return ���̗L�ȖʑS�̂̃I�t�Z�b�g�Ȗʂ�ߎ����� Bspline �Ȗ�
     * @see WhichSide
     */
    public BsplineSurface3D offsetByBsplineSurface(double magni,
                                                   int side,
                                                   ToleranceForDistance tol) {
        try {
            return offsetByBsplineSurface(uParameterDomain().section(),
                    vParameterDomain().section(),
                    magni, side, tol);
        } catch (ParameterOutOfRange e) {
            throw new FatalException();
        }
    }

    /**
     * ���̗L�ȖʂƑ��̗L�Ȗʂ̃t�B���b�g��?�߂�?B
     * <p/>
     * �t�B���b�g����?݂��Ȃ�?�?��ɂ͒��� 0 �̔z���Ԃ�?B
     * </p>
     *
     * @param side1  ���̋Ȗʂ̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.FRONT�Ȃ�Ε\��?ARIGHT�Ȃ�Η���?ABOTH�Ȃ�Η���)
     * @param mate   ���̋Ȗ�
     * @param side2  ���̋Ȗʂ̂ǂ��瑤�Ƀt�B���b�g��?�߂邩���t���O
     *               (WhichSide.FRONT�Ȃ�Ε\��?ARIGHT�Ȃ�Η���?ABOTH�Ȃ�Η���)
     * @param radius �t�B���b�g���a
     * @return �t�B���b�g�̔z��
     * @throws IndefiniteSolutionException ��s�� (��������?�ł͔�?����Ȃ�)
     * @see WhichSide
     */
    public FilletObject3D[] fillet(int side1, BoundedSurface3D mate, int side2, double radius)
            throws IndefiniteSolutionException {
        if (mate.type() == CURVE_BOUNDED_SURFACE_3D) {
            FilletObject3D[] results = mate.fillet(side2, this, side1, radius);
            for (int i = 0; i < results.length; i++)
                results[i] = results[i].exchange();
            return results;
        }

        return fillet(uParameterDomain().section(), vParameterDomain().section(), side1,
                mate,
                mate.uParameterDomain().section(), mate.vParameterDomain().section(), side2,
                radius);
    }
}

