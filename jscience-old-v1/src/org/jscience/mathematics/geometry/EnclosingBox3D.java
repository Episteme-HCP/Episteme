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
 * �R���� : (�`?�v�f�̑�?ݔ͈͂�) �ӂ������ɕ�?s�Ȓ���̂�\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ����̂�
 * XYZ ?������ꂼ��̒l��?�?��ł���_ (?�?��l) min
 * ��
 * XYZ ?������ꂼ��̒l��?ő�ł���_ (?ő�l) max
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:57 $
 */

public class EnclosingBox3D {
    /**
     * XYZ ?������ꂼ��̒l��?�?��ł���_ (?�?��l)
     */
    private final Point3D min;

    /**
     * XYZ ?������ꂼ��̒l��?ő�ł���_ (?ő�l)
     */
    private final Point3D max;

    /**
     * ����̂�?�?�/?ő�l��\���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * min �̂���?����� max �̑Ή�����?�������傫��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param min ����̂�?�?��l��\���_
     * @param max ����̂�?ő�l��\���_
     * @see InvalidArgumentValueException
     */
    public EnclosingBox3D(Point3D min, Point3D max) {
        super();
        if (min.x() > max.x() || min.y() > max.y() || min.z() > max.z())
            throw new InvalidArgumentValueException();
        this.min = min;
        this.max = max;
    }

    /**
     * ����̂�?�?�/?ő�l��\���_��?W�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * minX �� maxX ����傫��?�?�?A
     * minY �� maxY ����傫��?�?�
     * ���邢��
     * minZ �� maxZ ����傫��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param minX ����̂�?�?��l��\���_�� X ?W�l
     * @param minY ����̂�?�?��l��\���_�� Y ?W�l
     * @param minZ ����̂�?�?��l��\���_�� Z ?W�l
     * @param maxX ����̂�?ő�l��\���_�� X ?W�l
     * @param maxY ����̂�?ő�l��\���_�� Y ?W�l
     * @param maxZ ����̂�?ő�l��\���_�� Z ?W�l
     * @see InvalidArgumentValueException
     */
    public EnclosingBox3D(double minX, double minY, double minZ,
                          double maxX, double maxY, double maxZ) {
        super();
        if (minX > maxX || minY > maxY || minZ > maxZ)
            throw new InvalidArgumentValueException();
        this.min = new CartesianPoint3D(minX, minY, minZ);
        this.max = new CartesianPoint3D(maxX, maxY, maxZ);
    }

    /**
     * �^����ꂽ�_�Q���߂�?�?��̒���̂�\���I�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?�?�����钼��̂�?A�^����ꂽ�_�Q�ɑ΂��ă}?[�W����܂܂Ȃ�?B
     * </p>
     *
     * @param points ����̂Ɏ�܂�ׂ��_�Q
     */
    public EnclosingBox3D(Point3D[] points) {
        super();

        double max_x;
        double max_y;
        double max_z;
        double min_x;
        double min_y;
        double min_z;

        min_x = max_x = points[0].x();
        min_y = max_y = points[0].y();
        min_z = max_z = points[0].z();

        for (int i = 1; i < points.length; i++) {
            if (max_x < points[i].x()) max_x = points[i].x();
            if (max_y < points[i].y()) max_y = points[i].y();
            if (max_z < points[i].z()) max_z = points[i].z();
            if (min_x > points[i].x()) min_x = points[i].x();
            if (min_y > points[i].y()) min_y = points[i].y();
            if (min_z > points[i].z()) min_z = points[i].z();
        }

        this.min = new CartesianPoint3D(min_x, min_y, min_z);
        this.max = new CartesianPoint3D(max_x, max_y, max_z);
    }

    /**
     * ���̒���̂�?�?��l��\���_��Ԃ�?B
     *
     * @return ?�?��l��\���_
     */
    public Point3D min() {
        return this.min;
    }

    /**
     * ���̒���̂�?ő�l��\���_��Ԃ�?B
     *
     * @return ?ő�l��\���_
     */
    public Point3D max() {
        return this.max;
    }

    /**
     * ���̋�`��?�?��l/?ő�l��ӂ��ޔz���Ԃ�?B
     * <p/>
     * ���ʂƂ��ē�����z��̗v�f?��� 2 ��?A
     * ?�?��̗v�f��?�?��l?A��Ԗڂ̗v�f��?ő�l��܂�?B
     * </p>
     *
     * @return ?�?��l/?ő�l��ӂ��ޔz��
     */
    public Point3D[] toArray() {
        Point3D[] array = new Point3D[2];
        array[0] = this.min;
        array[1] = this.max;
        return array;
    }

    /**
     * ���̋�`���^����ꂽ���̋�`�ƌ��邩�ۂ���Ԃ�?B
     * <p/>
     * ������܂�?�?���?u����?v�Ƃ݂Ȃ�?B
     * </p>
     * <p/>
     * ���邩�ۂ��̔��f�ɂ�?A��?�?ݒ肳��Ă��鉉�Z?�?�̋����̋��e��?���?l������?B
     * </p>
     *
     * @param mate ���̋�`
     * @return ����Ȃ�� true?A�����łȂ���� false
     * @see ConditionOfOperation
     */
    public boolean hasIntersection(EnclosingBox3D mate) {
        double d_tol = ConditionOfOperation.getCondition().getToleranceForDistance();

        if ((min().x() > mate.max().x() + d_tol) ||
                (min().y() > mate.max().y() + d_tol) ||
                (min().z() > mate.max().z() + d_tol) ||
                (mate.min().x() > max().x() + d_tol) ||
                (mate.min().y() > max().y() + d_tol) ||
                (mate.min().z() > max().z() + d_tol))
            return false;
        return true;
    }
}


