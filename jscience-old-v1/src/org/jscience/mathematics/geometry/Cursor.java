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
 * �z��⃊�X�g�ւ� cursor ��\�킷�C���^?[�t�F?[�X?B
 * <p/>
 * cursor ��?�ɗv�f�Ɨv�f�̊Ԃɑ�?݂����̂�?l����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:09 $
 */

//This class could perhaps be moved to org.jscience.util
public interface Cursor extends java.util.Enumeration {
    /**
     * cursor �̎��ɗv�f�����邩�ǂ�����Ԃ�?B
     * <p/>
     * hasMoreElements() �̕ʖ�?B
     * </p>
     *
     * @return �v�f������� true?A����Ȃ��� false
     */
    boolean hasNextElements();

    /**
     * cursor �̑O�ɗv�f�����邩�ǂ�����Ԃ�?B
     *
     * @return �v�f������� true?A����Ȃ��� false
     */
    boolean hasPrevElements();

    /**
     * cursor �̎��̗v�f��Ԃ�?B
     *
     * @return ���̗v�f
     */
    Object peekNextElement();

    /**
     * cursor ��?擪�Ɉړ�����?B
     */
    void gotoHead();

    /**
     * cursor ��?I�[�Ɉړ�����?B
     */
    void gotoTail();

    /**
     * cursor �����Ɉړ�����?B
     */
    void gotoNext();

    /**
     * cursor �� n �������Ɉړ�����?B
     *
     * @param n �ړ�����?�
     */
    void gotoNext(int n);

    /**
     * cursor ���O�Ɉړ�����?B
     */
    void gotoPrev();

    /**
     * cursor �� n �����O�Ɉړ�����?B
     *
     * @param n �ړ�����?�
     */
    void gotoPrev(int n);

    /**
     * cursor �̎��̗v�f��?�?�����?B
     */
    void removeNextElement();

    /**
     * cursor �̑O�̗v�f��?�?�����?B
     */
    void removePrevElement();

    /**
     * cursor �̎��̗v�f�� obj ��?ݒ肷��?B
     *
     * @param obj ?ݒ肷��v�f
     */
    void setNextElement(Object obj);

    /**
     * cursor �̎��ɗv�f obj ��}���?B
     *
     * @param obj �}���v�f
     */
    void insertAfter(Object obj);

    /**
     * cursor �̑O�ɗv�f obj ��}���?B
     *
     * @param obj �}���v�f
     */
    void insertBefore(Object obj);
}
