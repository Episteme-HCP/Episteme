/*
 * �Q���� : (�`?�v�f�̑�?ݔ͈͂�) �ӂ������ɕ�?s�ȋ�`��\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: EnclosingBox2D.java,v 1.2 2006/03/01 21:15:57 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * �Q���� : (�`?�v�f�̑�?ݔ͈͂�) �ӂ������ɕ�?s�ȋ�`��\���N���X?B
 * <p/>
 * ���̃N���X�̃C���X�^���X��?A
 * ��`��
 * ?�����̓_ (?�?��l) ��\���_ min
 * ��
 * �E?��̓_ (?ő�l) ��\���_ max
 * ��ێ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2006/03/01 21:15:57 $
 */

public class EnclosingBox2D {
    /**
     * ?�����̓_ (?�?��l) ��\���_
     */
    private final Point2D min;

    /**
     * �E?��̓_ (?ő�l) ��\���_ max
     */
    private final Point2D max;

    /**
     * ��`��?�?�/?ő�l��\���_��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * min �̂���?����� max �̑Ή�����?�������傫��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param min ��`��?�?��l��\���_
     * @param max ��`��?ő�l��\���_
     * @see InvalidArgumentValueException
     */
    public EnclosingBox2D(Point2D min, Point2D max) {
        super();
        if (min.x() > max.x() || min.y() > max.y())
            throw new InvalidArgumentValueException();
        this.min = min;
        this.max = max;
    }

    /**
     * ��`��?�?�/?ő�l��\���_��?W�l��^���ăI�u�W�F�N�g��?\�z����?B
     * <p/>
     * minX �� maxX ����傫��?�?�
     * ���邢��
     * minY �� maxY ����傫��?�?��ɂ�
     * InvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param minX ��`��?�?��l��\���_�� X ?W�l
     * @param minY ��`��?�?��l��\���_�� Y ?W�l
     * @param maxX ��`��?ő�l��\���_�� X ?W�l
     * @param maxY ��`��?ő�l��\���_�� Y ?W�l
     * @see InvalidArgumentValueException
     */
    public EnclosingBox2D(double minX, double minY, double maxX, double maxY) {
        super();
        if (minX > maxX || minY > maxY)
            throw new InvalidArgumentValueException();
        this.min = new CartesianPoint2D(minX, minY);
        this.max = new CartesianPoint2D(maxX, maxY);
    }

    /**
     * �^����ꂽ�_�Q���߂�?�?��̋�`��\���I�u�W�F�N�g��?\�z����?B
     * <p/>
     * ?�?�������`��?A�^����ꂽ�_�Q�ɑ΂��ă}?[�W����܂܂Ȃ�?B
     * </p>
     *
     * @param points ��`��Ɏ�܂�ׂ��_�Q
     */
    public EnclosingBox2D(Point2D[] points) {
        super();

        double max_x;
        double max_y;
        double min_x;
        double min_y;

        min_x = max_x = points[0].x();
        min_y = max_y = points[0].y();

        for (int i = 1; i < points.length; i++) {
            if (max_x < points[i].x()) max_x = points[i].x();
            if (max_y < points[i].y()) max_y = points[i].y();
            if (min_x > points[i].x()) min_x = points[i].x();
            if (min_y > points[i].y()) min_y = points[i].y();
        }

        this.min = new CartesianPoint2D(min_x, min_y);
        this.max = new CartesianPoint2D(max_x, max_y);
    }

    /**
     * ���̋�`��?�����̓_ (?�?��l) ��\���_��Ԃ�?B
     *
     * @return ?�?��l��\���_
     */
    public Point2D min() {
        return this.min;
    }

    /**
     * ���̋�`�̉E?��̓_ (?ő�l) ��\���_��Ԃ�?B
     *
     * @return ?ő�l��\���_
     */
    public Point2D max() {
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
    public Point2D[] toArray() {
        Point2D[] array = new Point2D[2];
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
    public boolean hasIntersection(EnclosingBox2D mate) {
        double d_tol = ConditionOfOperation.getCondition().getToleranceForDistance();

        if ((min().x() > mate.max().x() + d_tol) ||
                (min().y() > mate.max().y() + d_tol) ||
                (mate.min().x() > max().x() + d_tol) ||
                (mate.min().y() > max().y() + d_tol))
            return false;
        return true;
    }
}


