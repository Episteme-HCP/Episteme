/*
 * �Q����?F�e?�������?��Œ�`���ꂽ�x�N�g����\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: LiteralVector2D.java,v 1.3 2007-10-21 21:08:14 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

import java.io.PrintWriter;

/**
 * �Q����?F�e?�������?��Œ�`���ꂽ�x�N�g����\���N���X?B
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:14 $
 * @see HomogeneousVector2D
 */

public class LiteralVector2D extends Vector2D {

    /**
     * X ?���?B
     *
     * @serial
     */
    private final double x;

    /**
     * Y ?���?B
     *
     * @serial
     */
    private final double y;

    /**
     * (x, y) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param x X ?���
     * @param y Y ?���
     */
    public LiteralVector2D(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * (c[0], c[1]) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param c ?����̔z��
     */
    public LiteralVector2D(double[] c) {
        super();
        this.x = c[0];
        this.y = c[1];
    }

    /**
     * (x, y) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param x                   X ?���
     * @param y                   Y ?���
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł��邱�Ƃ������BĂ���Ȃ�� <code>true</code>?A
     *                            ����Ȃ��� <code>false</code>
     */
    LiteralVector2D(double x, double y, boolean confirmedAsUnitized) {
        super(confirmedAsUnitized);
        this.x = x;
        this.y = y;
    }

    /**
     * (c[0], c[1]) �Œ�`�����I�u�W�F�N�g��?\�z����?B
     *
     * @param c                   ?����̔z��
     * @param confirmedAsUnitized ?�?����悤�Ƃ���x�N�g����
     *                            �P�ʃx�N�g���ł��邱�Ƃ������BĂ���Ȃ�� <code>true</code>?A
     *                            ����Ȃ��� <code>false</code>
     */
    LiteralVector2D(double[] c, boolean confirmedAsUnitized) {
        super(confirmedAsUnitized);
        this.x = c[0];
        this.y = c[1];
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
     * Y ?�����Ԃ�?B
     *
     * @return �x�N�g���� Y ?���
     */
    public double y() {
        return this.y;
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
    protected synchronized Vector2D
    doTransformBy(boolean reverseTransform,
                  CartesianTransformationOperator2D transformationOperator,
                  java.util.Hashtable transformedGeometries) {
        if (reverseTransform == false)
            return transformationOperator.transform(this);
        else
            return transformationOperator.reverseTransform(this);
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
                + x() + " "
                + y() + " End");
    }
}
