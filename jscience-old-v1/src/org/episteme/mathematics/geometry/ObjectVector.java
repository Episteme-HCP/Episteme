/*
 * Object �̉ϒ��ꎟ���z���\���N���X (java.util.Vector �̑��)
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: ObjectVector.java,v 1.3 2007-10-21 21:08:15 virtualcall Exp $
 */

package org.episteme.mathematics.geometry;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Object �̉ϒ��ꎟ���z���\���N���X?B
 * <p/>
 * java.util.Vector �̑��?B
 * </p>
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-21 21:08:15 $
 */

class ObjectVector {
    /**
     * ��?ۂ� Vector ?B
     *
     * @see Vector
     */
    private Vector vec;

    /**
     * �Ȃɂ�^�����ɃI�u�W�F�N�g��?\�z����?B
     */
    public ObjectVector() {
        vec = new Vector();
    }

    /**
     * ���̉ϒ��z��̓�e��?A�^����ꂽ�z��ɃR�s?[����?B
     *
     * @param anArray �R�s?[����?�̔z��
     */
    public final void copyInto(Object anArray[]) {
        vec.copyInto(anArray);
    }

    /**
     * ���̉ϒ��z��̒��� (�v�f��?�) ��Ԃ�?B
     *
     * @return ���� (�v�f��?�)
     */
    public final int size() {
        return vec.size();
    }

    /**
     * ���̉ϒ��z��ɗv�f�����邩�ǂ�����Ԃ�?B
     *
     * @return �v�f������� true?A����Ȃ��� false
     */
    public final boolean isEmpty() {
        return vec.size() == 0;
    }

    /**
     * ���̉ϒ��z��̗v�f�� Enumeration ��Ԃ�?B
     *
     * @return ���̉ϒ��z��̗v�f�� Enumeration
     */
    public final Enumeration elements() {
        return new SimpleCursor();
    }

    /**
     * ���̉ϒ��z��̗v�f�� Cursor ��Ԃ�?B
     *
     * @return ���̉ϒ��z��̗v�f�� Cursor
     */
    public final Cursor cursor() {
        return new SimpleCursor();
    }

    /**
     * ���̉ϒ��z�񂪗^����ꂽ�I�u�W�F�N�g��܂ނ��ۂ��𒲂ׂ�?B
     * <p/>
     * ���̉ϒ��z��� index ��?~�̗v�f�ɂ�����?A?�?��Ɍ���� elem �̃C���f�b�N�X��Ԃ�?B
     * elem ����?݂��Ȃ���� (- 1) ��Ԃ�?B
     * </p>
     *
     * @param elem  ��?�ׂ��I�u�W�F�N�g
     * @param index ��?��J�n����C���f�b�N�X
     * @return ?�?��ɂ݂��B� elem �̃C���f�b�N�X
     */
    public final int indexOf(Object elem, int index) {
        return vec.indexOf(elem, index);
    }

    /**
     * ���̉ϒ��z�񂪗^����ꂽ�I�u�W�F�N�g��܂ނ��ۂ��𒲂ׂ�?B
     * <p/>
     * ���̉ϒ��z��� index �ȑO�̗v�f�ɂ�����?A?Ō�Ɍ���� elem �̃C���f�b�N�X��Ԃ�?B
     * elem ����?݂��Ȃ���� (- 1) ��Ԃ�?B
     * </p>
     *
     * @param elem  ��?�ׂ��I�u�W�F�N�g
     * @param index ��?��J�n����C���f�b�N�X
     * @return ?Ō�ɂ݂��B� elem �̃C���f�b�N�X
     */
    public final int lastIndexOf(Object elem, int index) {
        return vec.lastIndexOf(elem, index);
    }

    /**
     * ���̉ϒ��z��� index �Ԗڂ̗v�f��Ԃ�?B
     *
     * @param index �Ԃ��v�f�̃C���f�b�N�X
     * @return index �Ԗڂ̗v�f
     */
    public final Object elementAt(int index) {
        return vec.elementAt(index);
    }

    /**
     * �^����ꂽ�I�u�W�F�N�g��?A���̉ϒ��z��� index �Ԗڂ̗v�f�Ƃ���?ݒ肷��?B
     *
     * @param index ?ݒ肷��v�f�̃C���f�b�N�X
     * @param obj   �v�f��?ݒ肷��I�u�W�F�N�g
     */
    public final void setElementAt(Object obj, int index) {
        vec.setElementAt(obj, index);
    }

    /**
     * ���̉ϒ��z��� index �Ԗڂ̗v�f��?�?�����?B
     *
     * @param index ?�?�����v�f�̃C���f�b�N�X
     */
    public final void removeElementAt(int index) {
        vec.removeElementAt(index);
    }

    /**
     * ���̉ϒ��z��� index �Ԗڂ̗v�f�̑O��?V�����v�f��}���?B
     *
     * @param index ���̑O�ɗv�f��}���v�f�̃C���f�b�N�X
     * @param obj   ?V���ȗv�f��?ݒ肷��I�u�W�F�N�g
     */
    public final void insertElementAt(Object obj, int index) {
        vec.insertElementAt(obj, index);
    }

    /**
     * ���̉ϒ��z���?Ō��?V�����v�f��ǉB���?B
     *
     * @param obj ?V���ȗv�f��?ݒ肷��I�u�W�F�N�g
     */
    public final void addElement(Object obj) {
        vec.addElement(obj);
    }

    /**
     * ���̉ϒ��z��̑S�Ă̗v�f��?�?�����?B
     */
    public final void removeAllElements() {
        vec.removeAllElements();
    }

    /**
     * ���̉ϒ��z���?�����?B
     */
    public Object clone() {
        try {
            ObjectVector newvec = (ObjectVector) super.clone();
            newvec.vec = (Vector) vec.clone();
            return newvec;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * ���̉ϒ��z��𕶎���ɕϊ�����?B
     *
     * @return ���̉ϒ��z���\��������
     */
    public final String toString() {
        return vec.toString();
    }

    /**
     * Cursor �������Ք�N���X?B
     */
    class SimpleCursor implements Cursor {
        /**
         * cursor �̌�?݂̈ʒu?B
         */
        int index;

        /**
         * cursor ��?擪�ɂ��ăI�u�W�F�N�g��?\�z����?B
         */
        SimpleCursor() {
            index = 0;
        }

        /**
         * cursor �̎��̗v�f��Ԃ�?Aindex ��?i�߂�?B
         * <p/>
         * peekNextElement() + gotoNext() �Ɠ��l��?��??B
         * </p>
         *
         * @return ���̗v�f
         */
        public Object nextElement() {
            synchronized (vec) {
                return vec.elementAt(index++);
            }
        }

        /**
         * cursor �̎��̗v�f�����邩�ǂ�����Ԃ�?B
         *
         * @return �v�f������� true?A����Ȃ��� false
         */
        public boolean hasMoreElements() {
            return index < vec.size();
        }

        /**
         * cursor �̎��̗v�f�����邩�ǂ�����Ԃ�?B
         * <p/>
         * {@link #hasMoreElements() hasMoreElements()} �̕ʖ�?B
         * </p>
         *
         * @return �v�f������� true?A����Ȃ��� false
         */
        public boolean hasNextElements() {
            return index < vec.size();
        }

        /**
         * cursor �̑O�ɗv�f�����邩�ǂ�����Ԃ�?B
         *
         * @return �v�f������� true?A����Ȃ��� false
         */
        public boolean hasPrevElements() {
            return 0 < index;
        }

        /**
         * cursor �̎��̗v�f��Ԃ�?B
         *
         * @return ���̗v�f
         */
        public Object peekNextElement() {
            return vec.elementAt(index);
        }

        /**
         * cursor ��?擪�Ɉړ�����?B
         */
        public void gotoHead() {
            index = 0;
        }

        /**
         * cursor ��?I�[�Ɉړ�����?B
         */
        public void gotoTail() {
            index = vec.size();
        }

        /**
         * cursor �� n �������Ɉړ�����?B
         *
         * @param n �ړ�����?�
         */
        public void gotoNext(int n) {
            synchronized (this) {
                if (index + n > vec.size()) {
                    throw new NoSuchElementException();
                }
                index += n;
            }
        }

        /**
         * cursor �����Ɉړ�����?B
         */
        public void gotoNext() {
            gotoNext(1);
        }

        /**
         * cursor �� n �����O�Ɉړ�����?B
         *
         * @param n �ړ�����?�
         */
        public void gotoPrev(int n) {
            synchronized (this) {
                if (index - n < 0) {
                    throw new NoSuchElementException();
                }
                index -= n;
            }
        }

        /**
         * cursor ���O�Ɉړ�����?B
         */
        public void gotoPrev() {
            gotoPrev(1);
        }

        /**
         * cursor �̎��̗v�f��?�?�����?B
         */
        public void removeNextElement() {
            synchronized (this) {
                if (index >= vec.size() || index < 0) {
                    throw new NoSuchElementException();
                }
                vec.removeElementAt(index);
            }
        }

        /**
         * cursor �̑O�̗v�f��?�?�����?B
         */
        public void removePrevElement() {
            synchronized (this) {
                if (index > vec.size() || index <= 0) {
                    throw new NoSuchElementException();
                }
                vec.removeElementAt(--index);
            }
        }

        /**
         * cursor �̎��̗v�f�� obj ��?ݒ肷��?B
         *
         * @param obj ?ݒ肷��v�f
         */
        public void setNextElement(Object obj) {
            synchronized (this) {
                if (index >= vec.size() || index < 0) {
                    throw new NoSuchElementException();
                }
                vec.setElementAt(obj, index);
            }
        }

        /**
         * cursor �̎��ɗv�f obj ��}���?B
         *
         * @param obj �}���v�f
         */
        public void insertAfter(Object obj) {
            vec.insertElementAt(obj, index);
        }

        /**
         * cursor �̑O�ɗv�f obj ��}���?B
         *
         * @param obj �}���v�f
         */
        public void insertBefore(Object obj) {
            vec.insertElementAt(obj, index++);
        }
    }
}

/* end of file */
