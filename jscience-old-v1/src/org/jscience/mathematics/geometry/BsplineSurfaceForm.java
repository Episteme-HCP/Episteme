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
 * �a�X�v���C���Ȗʂ̌`?�̓R���\����?���ێ?����N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:07 $
 */

public class BsplineSurfaceForm extends Types {
    /**
     * ���ʂ̈ꕔ��\���o�ꎟ�̂a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int PLANE_SURF = 0;

    /**
     * �~���̈ꕔ��\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int CYLINDRICAL_SURF = 1;

    /**
     * ���~??�̈ꕔ��\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int CONICAL_SURF = 2;

    /**
     * ���̈ꕔ/�S�̂�\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int SPHERICAL_SURF = 3;

    /**
     * �g?[���X�̈ꕔ/�S�̂�\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int TOROIDAL_SURF = 4;

    /**
     * ��]�ʂ̈ꕔ��\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int SURF_OF_REVOLUTION = 5;

    /**
     * ��̃p���?�g���b�N�ȋ�?�̓����p���??[�^�ʒu��
     * ��?�Ō���łł���Ȗʂ�\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int RULED_SURF = 6;

    /**
     * RULED_SURF �̓Uʂ�?�?���?A��Ԗڂ̋�?�_��?k�ނ������?A
     * ���Ȃ킿��ӂ�?���_����_��?k�ނ����a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int GENERALISED_CONE = 7;

    /**
     * �Q���Ȗʂ̈ꕔ��\���a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int QUADRIC_SURF = 8;

    /**
     * ���ʂ�\�� (���ꎟ��) �a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int SURF_OF_LINEAR_EXTRUSION = 9;

    /**
     * �`?�̓R���BɎw�肳��Ȃ��a�X�v���C���Ȗʂł��邱�Ƃ���?�
     */
    public static final int UNSPECIFIED = 10;

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private BsplineSurfaceForm() {
    }

    /**
     * ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l��t�B?[���h���ɕϊ�����?B
     * <p/>
     * �^����ꂽ�l�ɑΉ�����t�B?[���h����?݂��Ȃ�?�?��ɂ�
     * InvalidArgumentValueException �̗�O�𓊂���?B
     * </p>
     *
     * @param surfaceForm ���̃N���X�� static �t�B?[���h���ێ?�����?��̒l
     * @return �Ή�����t�B?[���h��
     * @see InvalidArgumentValueException
     */
    public static String toString(int surfaceForm) {
        switch (surfaceForm) {
            case PLANE_SURF:
                return "PLANE_SURF";
            case CYLINDRICAL_SURF:
                return "CYLINDRICAL_SURF";
            case CONICAL_SURF:
                return "CONICAL_SURF";
            case SPHERICAL_SURF:
                return "SPHERICAL_SURF";
            case TOROIDAL_SURF:
                return "TOROIDAL_SURF";
            case SURF_OF_REVOLUTION:
                return "SURF_OF_REVOLUTION";
            case RULED_SURF:
                return "RULED_SURF";
            case GENERALISED_CONE:
                return "GENERALISED_CONE";
            case QUADRIC_SURF:
                return "QUADRIC_SURF";
            case SURF_OF_LINEAR_EXTRUSION:
                return "SURF_OF_LINEAR_EXTRUSION";
            case UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                throw new InvalidArgumentValueException();
        }
    }
}

