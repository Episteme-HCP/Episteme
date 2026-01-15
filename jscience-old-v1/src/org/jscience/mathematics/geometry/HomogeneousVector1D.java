/*
 * �P����?F�e?�������?��Œ�`���ꂽ�����x�N�g����\��
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: HomogeneousVector1D.java,v 1.3 2007-10-23 18:19:40 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import java.io.PrintWriter;


/**
 * �P����?F�e?�������?��Œ�`���ꂽ�����x�N�g����\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:40 $
 *
 * @see LiteralVector1D
 */
public class HomogeneousVector1D extends Vector1D {
    /**
     * WX ?���?B
     *
     * @serial
     */
    private final double wx;

    /**
     * W ?���?B
     *
     * @serial
     */
    private final double w;

/**
     * wx/w
     * �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param wx WX ?���
     * @param w  W ?���
     */
    public HomogeneousVector1D(double wx, double w) {
        super();

        this.wx = wx;
        this.w = w;
    }

/**
     * wx/w
     * �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param wx                  WX ?���
     * @param w                   W ?���
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł��邱�Ƃ������BĂ���Ȃ��
     *                            <code>true</code>?A ����Ȃ��� <code>false</code>
     */
    HomogeneousVector1D(double wx, double w, boolean confirmedAsUnitized) {
        super(confirmedAsUnitized);

        this.wx = wx;
        this.w = w;
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
     * WX ?�����Ԃ�?B
     *
     * @return WX ?���
     */
    public double wx() {
        return wx;
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
     * ?o�̓X�g��?[���Ɍ`?�?���?o�͂���?B
     *
     * @param writer PrintWriter
     * @param indent �C���f���g��?[��
     *
     * @see GeometryElement
     */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);

        writer.println(indent_tab + getClassName() + " " + wx() + " " + w() +
            " End");
    }
}
