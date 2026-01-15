/*
 * �x�N�g���̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: AbstractVector.java,v 1.3 2007-10-21 21:08:05 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �x�N�g���̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:05 $
 */

//there is a bridge between this class and org.jscience.mathematics.DoubleVector (and Double2Vector and Double3Vector for subclasses)
//yet the current solution seems unsatisfying from my point of view
public abstract class AbstractVector extends GeometryElement {
    /**
     * �I�u�W�F�N�g��?\�z����?B
     */
    protected AbstractVector() {
        super();
    }

    /**
     * �x�N�g�����ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �x�N�g���Ȃ̂�?A?�� <code>true</code>
     */
    public boolean isVector() {
        return true;
    }

    /**
     * �x�N�g���̑傫����Ԃ�?B
     *
     * @return �x�N�g���̑傫��
     */
    public double length() {
        return Math.sqrt(norm());
    }

    /**
     * �x�N�g���̑傫����Ԃ�?B
     *
     * @return �x�N�g���̑傫��
     */
    public double magnitude() {
        return Math.sqrt(norm());
    }

    /**
     * �x�N�g���̃m������Ԃ���?ۃ?�\�b�h?B
     *
     * @return �x�N�g���̃m����
     */
    public abstract double norm();
}
