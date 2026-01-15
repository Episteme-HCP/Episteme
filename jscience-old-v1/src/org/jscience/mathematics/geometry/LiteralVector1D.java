/*
 * �P����?F�e?�������?��Œ�`���ꂽ�x�N�g����\��
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: LiteralVector1D.java,v 1.3 2007-10-23 18:19:42 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import java.io.PrintWriter;


/**
 * �P����?F�e?�������?��Œ�`���ꂽ�x�N�g����\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:42 $
 *
 * @see HomogeneousVector1D
 */
public class LiteralVector1D extends Vector1D {
    /**
     * X ?���?B
     *
     * @serial
     */
    private final double x;

/**
     * x
     * �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param x X ?���
     */
    public LiteralVector1D(double x) {
        super();
        this.x = x;
    }

/**
     * x
     * �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param x                   X ?���
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł��邱�Ƃ������BĂ���Ȃ��
     *                            <code>true</code>?A ����Ȃ��� <code>false</code>
     */
    LiteralVector1D(double x, boolean confirmedAsUnitized) {
        super(confirmedAsUnitized);
        this.x = x;
    }

    /**
     * X ?�����Ԃ�?B
     *
     * @return �x�N�g���� X ?���
     */
    public double x() {
        return this.x;
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

        writer.println(indent_tab + getClassName() + " " + x() + " End");
    }
}
