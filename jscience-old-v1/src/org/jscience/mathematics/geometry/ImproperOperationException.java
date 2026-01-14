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
 * �L��ȈӖ���?���Ȃ�?A���邢�͗L��Ȍ��ʂ��Ȃ�
 * �@�\�ł��邱�Ƃ������^�C���ȗ�O�̃N���X
 * <ul>
 * <li>��?�E�Ȗʂɑ΂���?AtoBsplineSurface()��toMesh()���?s���悤�Ƃ���?B
 * <li>�X�C?[�v�ʂƂ̌�?��?�߂悤�Ƃ���?B
 * </ul>
 * �Ȃ�
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:23 $
 */

public class ImproperOperationException extends RuntimeException {
    /**
     * ?־��^���Ȃ��ŃI�u�W�F�N�g��?\�z����?B
     */
    public ImproperOperationException() {
        super();
    }

    /**
     * ?־��^���ăI�u�W�F�N�g��?\�z����?B
     *
     * @param s ?־
     */
    public ImproperOperationException(String s) {
        super(s);
    }
}

// end of file
