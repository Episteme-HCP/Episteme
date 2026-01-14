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
 * �􉽗v�f�̒u�����ʒu����\���Q�����̔z�u?��̃N���X�K�w�̃�?[�g�ƂȂ钊?ۃN���X?B
 * <p/>
 * �Q�����̔z�u?���?A�Q�����̋�?��I��?W�n (���_?A�w��?A�x��) ���߂�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:16:06 $
 */

public abstract class Placement2D extends AbstractPlacement {
    /**
     * �􉽗v�f�̒u�����ʒu���_?B
     * <p/>
     * ���̓_��?A���̔z�u?�񂪒�߂��?�?W�n�̌��_�ƂȂ�?B
     * </p>
     *
     * @serial
     */
    private final Point2D location;

    /**
     * �I�u�W�F�N�g��?\�z����?B
     * <p/>
     * location �̒l�� null ��?�?��ɂ�?A
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param location �􉽗v�f�̒u�����ʒu���_
     */
    protected Placement2D(Point2D location) {
        super();

        if (location == null)
            throw new InvalidArgumentValueException();

        this.location = location;
    }

    /**
     * ���̔z�u?��̎�����Ԃ�?B
     * <p/>
     * ?�� 2 ��Ԃ�?B
     * </p>
     *
     * @return �Q�����Ȃ̂�?A?�� 2
     */
    public int dimension() {
        return 2;
    }

    /**
     * ���̔z�u?�񂪂Q�������ۂ���Ԃ�?B
     * <p/>
     * ?�� true ��Ԃ�?B
     * </p>
     *
     * @return �Q�����Ȃ̂�?A?�� true
     */
    public boolean is2D() {
        return true;
    }

    /**
     * �􉽗v�f�̒u�����ʒu���_��Ԃ�?B
     * <p/>
     * ���̓_��?A���̔z�u?�񂪒�߂��?�?W�n�̌��_�ł���?B
     * </p>
     *
     * @return �􉽗v�f�̒u�����ʒu���_
     */
    public Point2D location() {
        return location;
    }
}

