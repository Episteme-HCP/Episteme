/*
 * ���X�g (Vector) �̊e�v�f��?A�v�f�̒l��?]�B�?�?��Ƀ\?[�g����N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ListSorter.java,v 1.3 2007-10-21 21:08:14 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

/**
 * ���X�g (Vector) �̊e�v�f�̈ʒu��?A�v�f�̒l��?]�B�?�?��Ƀ\?[�g����N���X?B
 * <p/>
 * ���̃N���X�� static �ȃ?�\�b�h�݂̂�?��?B
 * </p>
 * <p/>
 * ���̃N���X�̃C���X�^���X��?�邱�Ƃ͂ł��Ȃ�?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:14 $
 */

public class ListSorter {

    /**
     * ��̃I�u�W�F�N�g��?u�傫��?v���r����I�u�W�F�N�g�̃C���^?[�t�F�C�X?B
     */
    public interface ObjectComparator {
        /**
         * ��҂��O�҂��傫�����ۂ���Ԃ�?B
         *
         * @param former �O��
         * @param latter ���
         * @return ��҂��O�҂��傫����� true?A�����łȂ���� false
         */
        boolean latterIsGreaterThanFormer(java.lang.Object former,
                                          java.lang.Object latter);
    }

    /**
     * ���̃N���X�̃C���X�^���X��?��Ȃ�?B
     */
    private ListSorter() {
    }

    /**
     * �^����ꂽ���X�g�̊e�v�f�̈ʒu��?A�v�f�̒l��?]�B�?�?��Ƀ\?[�g����?B
     *
     * @param list       �\?[�g���郊�X�g
     * @param comparator ��̃I�u�W�F�N�g��?u�傫��?v�𔻒肷��I�u�W�F�N�g
     */
    public static void doSorting(java.util.Vector list,
                                 ListSorter.ObjectComparator comparator) {
        int low = 0;
        int up = list.size() - 1;

        if (low < up)
            doIt(list, low, up, comparator);
    }

    /**
     * �^����ꂽ���X�g�̊e�v�f�̈ʒu��?A�v�f�̒l��?]�B�?�?��Ƀ\?[�g����?B
     *
     * @param list       �\?[�g���郊�X�g
     * @param low        �\?[�g����͈͂̉��� (0 �x?[�X)
     * @param up         �\?[�g����͈͂�?�� (0 �x?[�X)
     * @param comparator ��̃I�u�W�F�N�g��?u�傫��?v�𔻒肷��I�u�W�F�N�g
     */
    private static void doIt(java.util.Vector list,
                             int low,
                             int up,
                             ListSorter.ObjectComparator comparator) {
        int lidx = low;
        int uidx = up;
        java.lang.Object lnode = list.elementAt(lidx);
        java.lang.Object unode = list.elementAt(uidx);
        java.lang.Object key = list.elementAt((lidx + uidx) / 2);

        while (lidx < uidx) {
            for (;
                 (lnode != key) && comparator.latterIsGreaterThanFormer(lnode, key);
                 lnode = list.elementAt(++lidx))
                ; // nop

            for (;
                 (key != unode) && comparator.latterIsGreaterThanFormer(key, unode);
                 unode = list.elementAt(--uidx))
                ; // nop

            if (lidx <= uidx) {
                list.insertElementAt(unode, lidx);
                list.removeElementAt(lidx + 1);
                list.insertElementAt(lnode, uidx);
                list.removeElementAt(uidx + 1);

                lidx++;
                uidx--;
                if (lidx <= (list.size() - 1))
                    lnode = list.elementAt(lidx);
                if (uidx >= 0)
                    unode = list.elementAt(uidx);
            }
        }

        if (low < uidx) doIt(list, low, uidx, comparator);
        if (lidx < up) doIt(list, lidx, up, comparator);
    }
}

// end of file
