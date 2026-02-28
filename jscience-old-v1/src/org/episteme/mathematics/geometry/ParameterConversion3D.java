/*
 * �R���� : ��?�Ԃ̃p���??[�^�ϊ�?��?��\����?ۃN���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ParameterConversion3D.java,v 1.3 2007-10-21 21:08:16 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * �R���� : ��?�Ԃ̃p���??[�^�ϊ�?��?��\����?ۃN���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * �����?� P �ɑ΂���p���??[�^�l s ��?A
 * P(s) = Q(t) �𖞂����悤��
 * �ʂ̋�?� Q �ɑ΂���p���??[�^�l t �ɕϊ�����?��?��?s�Ȃ��?�\�b�h����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:16 $
 */
abstract class ParameterConversion3D {
    /**
     * P �ɑ΂���p���??[�^�l�� Q �ɑ΂���p���??[�^�l�ɕϊ����钊?ۃ?�\�b�h?B
     *
     * @param param P �ɑ΂���p���??[�^�l
     * @return Q �ɑ΂���p���??[�^�l
     */
    abstract double convParameter(double param);

    /**
     * P �ɑ΂���p���??[�^�l��ϊ������?ۂł��� Q ��Ԃ���?ۃ?�\�b�h?B
     *
     * @param param P �ɑ΂���p���??[�^�l
     * @return ��?� Q
     */
    abstract ParametricCurve3D convCurve(double param);

    /**
     * P �ɑ΂���p���??[�^��Ԃ� Q �ɑ΂���p���??[�^��Ԃɕϊ�����?B
     *
     * @param sec P �ɑ΂���p���??[�^���
     * @return Q �ɑ΂���p���??[�^���
     * @see #convParameter(double)
     * @see #convCurve(double)
     */
    ParameterSection convParameter(ParameterSection sec) {
        ParametricCurve3D scurve = convCurve(sec.start());
        ParametricCurve3D ecurve = convCurve(sec.end());

        if (scurve != ecurve)
            return null;

        double sparam = convParameter(sec.start());
        double eparam = convParameter(sec.end());
        return new ParameterSection(sparam, eparam - sparam);
    }

    /**
     * P �ɑ΂���p���??[�^�l�ɑΉ����� Q ��?�?�_ Q(t) ��Ԃ�?B
     *
     * @param param P �ɑ΂���p���??[�^�l
     * @return �Ή����� Q ��?�?�_ Q(t)
     * @see #convParameter(double)
     * @see #convCurve(double)
     */
    PointOnCurve3D convToPoint(double param) {
        double tparam = convParameter(param);
        ParametricCurve3D curve = convCurve(param);

        return new PointOnCurve3D(curve, tparam, GeometryElement.doCheckDebug);
    }

    /**
     * P �ɑ΂���p���??[�^��ԂɑΉ����� Q �̃g������?��Ԃ�?B
     *
     * @param sec P �ɑ΂���p���??[�^���
     * @return �Ή����� Q �̃g������?�
     * @see #convParameter(double)
     * @see #convCurve(double)
     */
    TrimmedCurve3D convToTrimmedCurve(ParameterSection sec) {
        ParametricCurve3D scurve = convCurve(sec.start());
        ParametricCurve3D ecurve = convCurve(sec.end());

        if (scurve != ecurve)
            return null;

        double sparam = convParameter(sec.start());
        double eparam = convParameter(sec.end());
        return new TrimmedCurve3D(ecurve,
                new ParameterSection(sparam, eparam - sparam));
    }
}
