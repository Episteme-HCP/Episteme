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

/**
 * �⪕s��ł��� (��?��̉⪑�?݂���) ���Ƃ���O�̃N���X?B
 * <p/>
 * ���̃N���X��?A���鉉�Z�̉⪕s��ł��� (��?��̉⪑�?݂���) ���Ƃ�
 * �����̂ɗ��p�����?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ���鉉�Z�ɑ΂��閳?��̉�̓�̓K���Ȉ�̉� (suitable) ��
 * GeometryElement �Ƃ��ĕێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:23 $
 */

public class IndefiniteSolutionException extends Exception {
    /**
     * ��?��̉�̓�̓K���Ȉ��?B
     *
     * @serial
     */
    private final GeometryElement suitable;

    /**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     *
     * @param suitable ��?��̉�̓�̓K���Ȉ�̉�
     */
    public IndefiniteSolutionException(GeometryElement suitable) {
        super();
        this.suitable = suitable;
    }

    /**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s        ?־
     * @param suitable ��?��̉�̓�̓K���Ȉ�̉�
     */
    public IndefiniteSolutionException(String s, GeometryElement suitable) {
        super(s);
        this.suitable = suitable;
    }

    /**
     * ��?��̉�̓�̓K���Ȉ�̉��Ԃ�?B
     *
     * @return ��?��̉�̓�̓K���Ȉ�̉�
     */
    public GeometryElement suitable() {
        return this.suitable;
    }
}

