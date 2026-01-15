/*
 * �z��⃊�X�g�ւ� cursor ��\�킷�C���^?[�t�F?[�X?B
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: Cursor.java,v 1.3 2007-10-21 21:08:09 virtualcall Exp $
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
