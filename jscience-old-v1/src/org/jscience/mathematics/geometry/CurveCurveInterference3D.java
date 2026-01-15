/*
 * �R���� : ��Ȑ�Ԃ̊���\���C���^�[�t�F�C�X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CurveCurveInterference3D.java,v 1.3 2007-10-21 21:08:09 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �R���� : ��Ȑ�Ԃ̊���\���C���^�[�t�F�C�X�B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public interface CurveCurveInterference3D extends Interference3D {
    /**
     * ���̊����I�[�o�[���b�v�ł��邩�ۂ���Ԃ��B
     *
     * @return �I�[�o�[���b�v�ł���� true�A�����łȂ���� false
     */
    public boolean isOverlapCurve();

    /**
     * ���̊���I�[�o�[���b�v�ɕϊ�����B
     * <p/>
     * �I�[�o�[���b�v�ɕϊ��ł��Ȃ��ꍇ�� null ��Ԃ��B
     * </p>
     *
     * @return �I�[�o�[���b�v
     */
    public OverlapCurve3D toOverlapCurve();

    /**
     * ���̊��̈��̋Ȑ� (�Ȑ�1) ��ł̈ʒu��A
     * �^����ꂽ�ϊ������ɂ�Bĕϊ�������̂ɒu������������Ԃ��B
     *
     * @param sec  �Ȑ�1 �̃p�����[�^���
     * @param conv �Ȑ�1 �̃p�����[�^�l��ϊ�����I�u�W�F�N�g
     * @return �Ȑ�1 ��̈ʒu��^����ꂽ�ϊ������ɂ�Bĕϊ�������̂ɒu������������
     */
    public CurveCurveInterference3D trim1(ParameterSection sec,
                                          ParameterConversion3D conv);

    /**
     * ���̊��̑���̋Ȑ� (�Ȑ�2) ��ł̈ʒu��A
     * �^����ꂽ�ϊ������ɂ�Bĕϊ�������̂ɒu������������Ԃ��B
     *
     * @param sec  �Ȑ�2 �̃p�����[�^���
     * @param conv �Ȑ�2 �̃p�����[�^�l��ϊ�����I�u�W�F�N�g
     * @return �Ȑ�2 ��̈ʒu��^����ꂽ�ϊ������ɂ�Bĕϊ�������̂ɒu������������
     */
    public CurveCurveInterference3D trim2(ParameterSection sec,
                                          ParameterConversion3D conv);

    /**
     * ���̊��̈��̋Ȑ� (�Ȑ�1) ��^����ꂽ�Ȑ�ɒu������������Ԃ��B
     * <p/>
     * �p�����[�^�l�Ȃǂ͂��̂܂܁B
     * </p>
     *
     * @param newCurve �Ȑ�1 �ɐݒ肷��Ȑ�
     * @return �Ȑ�1��u������������
     */
    public CurveCurveInterference3D changeCurve1(ParametricCurve3D newCurve);

    /**
     * ���̊��̑���̋Ȑ� (�Ȑ�2) ��^����ꂽ�Ȑ�ɒu������������Ԃ��B
     * <p/>
     * �p�����[�^�l�Ȃǂ͂��̂܂܁B
     * </p>
     *
     * @param newCurve �Ȑ�2 �ɐݒ肷��Ȑ�
     * @return �Ȑ�2 ��u������������
     */
    public CurveCurveInterference3D changeCurve2(ParametricCurve3D newCurve);
}
