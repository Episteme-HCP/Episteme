/*
 * �p���?�g���b�N��2D�Ȗʂ�\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ParametricSurface2D.java,v 1.3 2006/03/28 21:47:45 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

/**
 * �p���?�g���b�N��2D�Ȗʂ�\����?ۃN���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/28 21:47:45 $
 */
abstract class ParametricSurface2D extends AbstractParametricSurface {
/**
     * uDomain, vDomain �� null
     * �Ƃ��ăI�u�W�F�N�g��?\�z����
     */
    protected ParametricSurface2D() {
        super();
    }

    /**
     * �􉽗v�f�̎�����Ԃ�
     *
     * @return �􉽗v�f�̎���?�
     */
    public int dimension() {
        return 2;
    }

    /**
     * �Q�������ۂ���Ԃ�
     *
     * @return �Q�������ۂ���Ԃ� (true ��Ԃ�)
     */
    public boolean is2D() {
        return true;
    }

    /**
     * �^����ꂽ�p���??[�^�ł�?W�l��?�߂�
     *
     * @param uParam U���p���??[�^
     * @param vParam V���p���??[�^
     *
     * @return ?W�l
     *
     * @see Point2D
     */
    public abstract Point2D coordinates(double uParam, double vParam);

    /**
     * �^����ꂽ�p���??[�^�ł�?ڃx�N�g����?�߂�
     *
     * @param uParam U���p���??[�^
     * @param vParam V���p���??[�^
     *
     * @return ?ڃx�N�g��[0]:U����?ڃx�N�g��
     *         [1]:V����?ڃx�N�g��
     *
     * @see Vector2D
     */
    public abstract Vector2D[] tangentVector(double uParam, double vParam);
}
// end of file
