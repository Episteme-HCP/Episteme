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
 * ��?�邢�͋Ȗʂł��邱�Ƃ���҂�����?���?A
 * ���ꂪ�_��?k�ނ��Ă��邱�Ƃ���O�̃N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��҂�����?�邢�͋Ȗʂ�?k�ނ������ʂł���_ (point) ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:19 $
 */

public class ReducedToPointException extends Exception {
    /**
     * ��?�邢�͋Ȗʂ�?k�ނ����_
     *
     * @serial
     */
    private final AbstractPoint point;

    /**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     *
     * @param point ��?�邢�͋Ȗʂ�?k�ނ����_
     */
    public ReducedToPointException(AbstractPoint point) {
        super();
        this.point = point;
    }

    /**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s     ?־
     * @param point ��?�邢�͋Ȗʂ�?k�ނ����_
     */
    public ReducedToPointException(String s, AbstractPoint point) {
        super(s);
        this.point = point;
    }

    /**
     * ��?�邢�͋Ȗʂ�?k�ނ����_��Ԃ�?B
     *
     * @return ��?�邢�͋Ȗʂ�?k�ނ����_
     */
    public AbstractPoint point() {
        return this.point;
    }
}

