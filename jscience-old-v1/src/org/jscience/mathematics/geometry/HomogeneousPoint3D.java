/*
 * �R����?F�e?W�l����?��Œ�`���ꂽ3�����̓���?W�_��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: HomogeneousPoint3D.java,v 1.2 2006/03/01 21:15:59 virtualcall Exp $
 *
 */

package org.jscience.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �R����?F�e?W�l����?��Œ�`���ꂽ3�����̓���?W�_��\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:59 $
 * @see CartesianPoint3D
 */

public class HomogeneousPoint3D extends Point3D {
    /**
     * WX ?W�l?B
     *
     * @serial
     */
    private double wx;

    /**
     * WY ?W�l?B
     *
     * @serial
     */
    private double wy;

    /**
     * WZ ?W�l?B
     *
     * @serial
     */
    private double wz;

    /**
     * W ?W�l?B
     *
     * @serial
     */
    private double w;

    /**
     * WX, WY, WZ, W ��?ݒ肷��?B
     *
     * @param wx WX ?W�l
     * @param wy WY ?W�l
     * @param wz WZ ?W�l
     * @param w  W ?W�l
     */
    private void setArgs(double wx, double wy, double wz, double w) {
        this.wx = wx;
        this.wy = wy;
        this.wz = wz;
        this.w = w;
    }

    /**
     * (WX/W, WY/W, WZ/W) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param wx WX ?W�l
     * @param wy WY ?W�l
     * @param wz WZ ?W�l
     * @param w  W ?W�l
     */
    public HomogeneousPoint3D(double wx, double wy, double wz, double w) {
        super();

        setArgs(wx, wy, wz, w);
    }

    /**
     * (c[0]/c[3], c[1]/c[3], c[2]/c[3]) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param c ?W�l�̔z��
     */
    public HomogeneousPoint3D(double[] c) {
        super();

        setArgs(c[0], c[1], c[2], c[3]);
    }

    /**
     * X ?W�l (WX/W) ��Ԃ�?B
     *
     * @return X ?W�l
     */
    public double x() {
        return wx / w;
    }

    /**
     * Y ?W�l (WY/W) ��Ԃ�?B
     *
     * @return Y ?W�l
     */
    public double y() {
        return wy / w;
    }

    /**
     * Z ?W�l (WZ/W) ��Ԃ�?B
     *
     * @return Z ?W�l
     */
    public double z() {
        return wz / w;
    }

    /**
     * WX ?W�l��Ԃ�?B
     *
     * @return WX ?W�l
     */
    public double wx() {
        return wx;
    }

    /**
     * WY ?W�l��Ԃ�?B
     *
     * @return WY ?W�l
     */
    public double wy() {
        return wy;
    }

    /**
     * WZ ?W�l��Ԃ�?B
     *
     * @return WZ ?W�l
     */
    public double wz() {
        return wz;
    }

    /**
     * W ?W�l��Ԃ�?B
     *
     * @return W ?W�l
     */
    public double w() {
        return w;
    }

    /**
     * ���̓_��?A�^����ꂽ�􉽓I�ϊ����Z�q�ŕϊ�����?B
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
    protected synchronized Point3D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator3D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        Point3D crd;
        if (reverseTransform == false)
            crd = transformationOperator.transform(this);
        else
            crd = transformationOperator.reverseTransform(this);
        return new HomogeneousPoint3D((this.w * crd.x()),
                (this.w * crd.y()),
                (this.w * crd.z()),
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

        writer.println(indent_tab
                + getClassName() + " "
                + wx() + " "
                + wy() + " "
                + wz() + " "
                + w() + " End");

    }
}
