/*
 * �P����?F�e?W�l����?��Œ�`���ꂽ�_��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: CartesianPoint1D.java,v 1.3 2006/03/28 21:47:43 virtualcall Exp $
 *
 */
package org.jscience.mathematics.geometry;

import java.io.PrintWriter;


/**
 * �P����?F�e?W�l����?��Œ�`���ꂽ�_��\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/28 21:47:43 $
 *
 * @see HomogeneousPoint1D
 */
public class CartesianPoint1D extends Point1D {
    /**
     * X ?W�l?B
     *
     * @serial
     */
    private final double x;

/**
     * X
     * �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param x X ?W�l
     */
    public CartesianPoint1D(double x) {
        super();
        this.x = x;
    }

    /**
     * X ?W�l��Ԃ�?B
     *
     * @return X ?W�l
     */
    public double x() {
        return x;
    }

    /**
     * ?o�̓X�g��?[���Ɍ`?�?���?o��?B
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
