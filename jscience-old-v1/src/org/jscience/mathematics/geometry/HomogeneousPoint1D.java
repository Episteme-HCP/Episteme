/*
 * �P����?F�e?W�l����?��Œ�`���ꂽ3�����̓���?W�_��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: HomogeneousPoint1D.java,v 1.3 2006/03/28 21:47:43 virtualcall Exp $
 *
 */
package org.jscience.mathematics.geometry;

import java.io.PrintWriter;


/**
 * �P����?F�e?W�l����?��Œ�`���ꂽ3�����̓���?W�_��\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2006/03/28 21:47:43 $
 *
 * @see CartesianPoint1D
 */
public class HomogeneousPoint1D extends Point1D {
    /**
     * WX ?W�l?B
     *
     * @serial
     */
    private final double wx;

    /**
     * W ?W�l?B
     *
     * @serial
     */
    private final double w;

/**
     * WX/W
     * �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param wx WX ?W�l
     * @param w  W ?W�l
     */
    public HomogeneousPoint1D(double wx, double w) {
        super();

        this.wx = wx;
        this.w = w;
    }

    /**
     * X ?W�l��Ԃ�?B
     *
     * @return X ?W�l
     */
    public double x() {
        return wx / w;
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
     * W ?W�l��Ԃ�?B
     *
     * @return W ?W�l
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
