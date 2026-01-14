/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
