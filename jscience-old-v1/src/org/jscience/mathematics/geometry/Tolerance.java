/*
 * ��?��l�̋��e��?���\���N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Tolerance.java,v 1.3 2007-10-21 21:08:20 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * ��?��l�̋��e��?���\���N���X?B
 * <p/>
 * JGCL �ł�?A
 * �􉽉��Z��?i�߂�?ۂ̋��e��?�?��?�ɂ����ĎQ?Ƃ��ׂ��e��̋��e��?��l��
 * ���Z?�? {@link ConditionOfOperation ConditionOfOperation} �Ƃ���?A
 * �܂Ƃ߂ĊǗ?����?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:20 $
 * @see ConditionOfOperation
 */

public class Tolerance extends java.lang.Object {

    /**
     * ���e��?��l?B
     */
    private final double value;

    /**
     * �^����ꂽ�l�떗e��?��l�Ƃ���I�u�W�F�N�g��?\�z����?B
     * <p/>
     * value �̒l������?�?��ɂ�?A����?�Βl�떗e��?��l�Ƃ���?B
     * </p>
     * <p/>
     * value �̒l�� 0 ��?�?��ɂ�?AInvalidArgumentValueException �̗�O��?�����?B
     * </p>
     *
     * @param value ?ݒ肷�鋖�e��?��l
     * @see InvalidArgumentValueException
     */
    public Tolerance(double value) {
        if (value == 0) {
            throw new InvalidArgumentValueException();
        }
        if (value < 0) {
            value = Math.abs(value);
        }

        this.value = value;
    }

    /**
     * ���̋��e��?��̒l��Ԃ�?B
     *
     * @return ���e��?��l
     */
    public double value() {
        return value;
    }
}

