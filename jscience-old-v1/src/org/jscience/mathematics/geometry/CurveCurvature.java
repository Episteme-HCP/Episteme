/*
 * ��?�̋ȗ���\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CurveCurvature.java,v 1.3 2007-10-21 21:08:09 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * ��?�̋ȗ���\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��
 * �ȗ��̒l (��?��l) curvature
 * ��?��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

public abstract class CurveCurvature {
    /**
     * �ȗ��̒l?B
     */
    private final double curvature;

    /**
     * �ȗ��̒l��w�肵�ăI�u�W�F�N�g��?\�z����?B
     *
     * @param curvature �ȗ��̒l
     */
    protected CurveCurvature(double curvature) {
        super();
        this.curvature = curvature;
    }

    /**
     * ���̋ȗ��I�u�W�F�N�g�̋ȗ��̒l��Ԃ�?B
     *
     * @return �ȗ��̒l
     */
    public double curvature() {
        return curvature;
    }

    /**
     * �^����ꂽ��̋ȗ��̒l������Ƃ݂Ȃ��邩�ۂ���Ԃ�?B
     * <p/>
     * a, b ���ꂼ��̋t?���?���?A
     * ��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?��ȓ�ł����?A
     * a �� b �͓���̋ȗ��ł����̂Ƃ���?B
     * </p>
     *
     * @param a �ȗ��̒l1
     * @param b �ȗ��̒l2
     * @return a �� b �� ����̋ȗ��Ƃ݂Ȃ���� true?A�����łȂ���� false
     */
    static boolean identical(double a, double b) {
        if (GeometryUtils.isReciprocatable(a)) {
            if (!GeometryUtils.isReciprocatable(b))
                return false;

            double a_rad = 1.0 / a;
            double b_rad = 1.0 / b;
            double tol_d = ConditionOfOperation.getCondition().getToleranceForDistance();
            if (Math.abs(a_rad - b_rad) > tol_d)
                return false;
        } else {
            if (GeometryUtils.isReciprocatable(b))
                return false;
        }

        return true;
    }
}

