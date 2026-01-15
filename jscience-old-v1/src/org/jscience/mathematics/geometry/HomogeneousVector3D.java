/*
 * �R����?F�e?�������?��Œ�`���ꂽ�����x�N�g����\��
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: HomogeneousVector3D.java,v 1.3 2007-10-21 21:08:12 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �R����?F�e?�������?��Œ�`���ꂽ�����x�N�g����\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:12 $
 * @see LiteralVector3D
 */
public class HomogeneousVector3D extends Vector3D {

    /**
     * WX ?���?B
     *
     * @serial
     */
    private double wx;

    /**
     * WY ?���?B
     *
     * @serial
     */
    private double wy;

    /**
     * WZ ?���?B
     *
     * @serial
     */
    private double wz;

    /**
     * W ?���?B
     *
     * @serial
     */
    private double w;

    /**
     * wx, wy, wz, w ��?ݒ肷��?B
     *
     * @param wx WX ?���
     * @param wy WY ?���
     * @param wz WZ ?���
     * @param w  W ?���
     */
    private void setArgs(double wx, double wy, double wz, double w) {
        this.wx = wx;
        this.wy = wy;
        this.wz = wz;
        this.w = w;
    }

    /**
     * (wx/w, wy/w, wz/w) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param wx WX ?���
     * @param wy WY ?���
     * @param wz WZ ?���
     * @param w  W ?���
     */
    public HomogeneousVector3D(double wx, double wy, double wz, double w) {
        super();

        setArgs(wx, wy, wz, w);
    }

    /**
     * (c[0]/c[3], c[1]/c[3], c[2]/c[3]) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param c ?����̔z��
     */
    public HomogeneousVector3D(double[] c) {
        super();

        setArgs(c[0], c[1], c[2], c[3]);
    }

    /**
     * (wx/w, wy/w, wz/w) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param wx                  WX ?���
     * @param wy                  WY ?���
     * @param wz                  WZ ?���
     * @param w                   W ?���
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł��邱�Ƃ������BĂ���Ȃ�� <code>true</code>?A
     *                            ����Ȃ��� <code>false</code>
     */
    HomogeneousVector3D(double wx, double wy, double wz, double w,
                        boolean confirmedAsUnitized) {
        super(confirmedAsUnitized);

        setArgs(wx, wy, wz, w);
    }

    /**
     * (c[0]/c[3], c[1]/c[3], c[2]/c[3]) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param c                   ?����̔z��
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł��邱�Ƃ������BĂ���Ȃ�� <code>true</code>?A
     *                            ����Ȃ��� <code>false</code>
     */
    HomogeneousVector3D(double[] c, boolean confirmedAsUnitized) {
        super(confirmedAsUnitized);

        setArgs(c[0], c[1], c[2], c[3]);
    }

    /**
     * X ?�����Ԃ�?B
     *
     * @return X ?���
     */
    public double x() {
        return wx / w;
    }

    /**
     * Y ?�����Ԃ�?B
     *
     * @return Y ?���
     */
    public double y() {
        return wy / w;
    }

    /**
     * Z ?�����Ԃ�?B
     *
     * @return Z ?���
     */
    public double z() {
        return wz / w;
    }

    /**
     * WX ?�����Ԃ�?B
     *
     * @return WX ?���
     */
    public double wx() {
        return wx;
    }

    /**
     * WY ?�����Ԃ�?B
     *
     * @return WY ?���
     */
    public double wy() {
        return wy;
    }

    /**
     * WZ ?�����Ԃ�?B
     *
     * @return WZ ?���
     */
    public double wz() {
        return wz;
    }

    /**
     * W ?�����Ԃ�?B
     *
     * @return W ?���
     */
    public double w() {
        return w;
    }

    /**
     * ���̃x�N�g����?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
     * <p/>
     * transformedGeometries ��?A
     * �ϊ��O�̊􉽗v�f��L?[�Ƃ�?A
     * �ϊ���̊􉽗v�f��l�Ƃ���n�b�V���e?[�u���ł���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��ɃL?[�Ƃ��đ�?݂��Ȃ�?�?��ɂ�?A
     * this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * ����?ۂɃ?�\�b�h�Ք�ł� this ��L?[?A
     * �ϊ����ʂ�l�Ƃ��� transformedGeometries �ɒǉB���?B
     * </p>
     * <p/>
     * this �� transformedGeometries ��Ɋ�ɃL?[�Ƃ��đ�?݂���?�?��ɂ�?A
     * ��?ۂ̕ϊ���?s�Ȃ킸?A���̃L?[�ɑΉ�����l��Ԃ�?B
     * ����?��?��?ċA�I��?s�Ȃ���?B
     * </p>
     * <p/>
     * transformedGeometries �� null �ł�?\��Ȃ�?B
     * transformedGeometries �� null ��?�?��ɂ�?A
     * ?�� this �� transformationOperator �ŕϊ�������̂�Ԃ�?B
     * </p>
     *
     * @param reverseTransform       �t�ϊ�����̂ł���� true?A�����łȂ���� false
     * @param transformationOperator �􉽓I�ϊ����Z�q
     * @param transformedGeometries  ��ɓ��l�̕ϊ���{�����􉽗v�f��܂ރn�b�V���e?[�u��
     * @return �ϊ���̊􉽗v�f
     */
    protected synchronized Vector3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Vector3D vec;
        if (reverseTransform == false)
            vec = transformationOperator.transform(this);
        else
            vec = transformationOperator.reverseTransform(this);
        return new HomogeneousVector3D((this.w * vec.x()),
                (this.w * vec.y()),
                (this.w * vec.z()),
                this.w);
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     * @see GeometryElement
     */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);

        writer.println(indent_tab + getClassName() + " "
                + wx() + " "
                + wy() + " "
                + wz() + " "
                + w() + " End");
    }
}

