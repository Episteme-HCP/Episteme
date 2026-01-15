/*
 * �v�Z�@ (Java Virtual Machine) �̕���?�?��_���Z�̊ۂߌ�?���ێ?����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: MachineEpsilon.java,v 1.2 2007-10-21 17:38:57 virtualcall Exp $
 */

package org.jscience.mathematics;

/**
 * �v�Z�@ (Java Virtual Machine) �̕���?�?��_���Z�̊ۂߌ�?���ێ?����N���X?B
 * <p/>
 * ((1 + a) &gt; 1) ���U�ƂȂ�?�?���?��l a ��\��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 17:38:57 $
 */
public class MachineEpsilon extends java.lang.Object {
    /**
     * �P?��x�̊ۂߌ�?�?B
     */
    public static final float SINGLE = determineFloatValue();

    /**
     * �{?��x�̊ۂߌ�?�?B
     */
    public static final double DOUBLE = determineDoubleValue();

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private MachineEpsilon() {
    }

    /**
     * �P?��x�̊ۂߌ�?���v�Z����?B
     */
    private static float determineFloatValue() {
        float half = (float) 0.5;
        float one = (float) 1.0;
        float two = (float) 2.0;
        float meps = one;

        for (; ; meps *= half) {
            float work = one + meps;
            if (work <= one)
                break;
        }

        return (meps * two);
    }

    /**
     * �{?��x�̊ۂߌ�?���v�Z����?B
     */
    private static double determineDoubleValue() {
        double half = 0.5;
        double one = 1.0;
        double two = 2.0;
        double meps = one;

        for (; ; meps *= half) {
            double work = one + meps;
            if (work <= one)
                break;
        }

        return (meps * two);
    }

    /* Debug
    public static void main(String argv[]) {
        System.out.println(SINGLE);
    System.out.println(DOUBLE);
    }
    */
}

/* end of file */
