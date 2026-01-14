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
