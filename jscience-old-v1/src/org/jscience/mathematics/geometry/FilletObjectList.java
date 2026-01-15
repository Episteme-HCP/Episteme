/*
 * �t�B���b�g?��̃��X�g�������߂̃N���X
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: FilletObjectList.java,v 1.3 2007-10-23 18:19:40 virtualcall Exp $
 */
package org.jscience.mathematics.geometry;

import java.util.Enumeration;
import java.util.Vector;


/**
 * �t�B���b�g?��̃��X�g�������߂̃N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:40 $
 */
class FilletObjectList {
    /**
     * �t�B���b�g?��̃��X�g
     *
     * @see Vector
     */
    Vector list;

    /*
     * �R���X�g���N�^
     */
    FilletObjectList() {
        super();
        list = new Vector();
    }

    /*
     * �t�B���b�g��?�?���ǉB���?B
     * ��ɓ���������?݂���Ƃ��͒ǉB��Ȃ�?B
     * @param theFilletObject        �ǉB���t�B���b�g��?�?��
     * @see        FilletObject2D
     */
    void addFillet(FilletObject2D theFilletObject) {
        FilletObject2D mate;

        for (Enumeration en = list.elements(); en.hasMoreElements();) {
            if (theFilletObject.parametricallyIdentical(
                        (FilletObject2D) en.nextElement())) {
                return;
            }
        }

        list.addElement(theFilletObject);
    }

    /**
     * �t�B���b�g��?�?���z��Ƃ��Ď��?o��?B
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return �t�B���b�g��?�?��̔z��
     *
     * @see FilletObject2D
     */
    FilletObject2D[] toFilletObject2DArray(boolean doExchange) {
        FilletObject2D[] flts = new FilletObject2D[list.size()];
        list.copyInto(flts);

        if (doExchange) {
            for (int i = 0; i < flts.length; i++)
                flts[i] = flts[i].exchange();
        }

        return flts;
    }

    /*
     * �t�B���b�g�f��?���ǉB���?B
     * ?Ō�ɒǉB��ꂽ�f�ʂƓ����?�?��͒ǉB��Ȃ�?B
     * @param theFilletObject        �ǉB���t�B���b�g�f��?��
     * @see        FilletSection3D
     */
    void addSection(FilletSection3D theFilletSection) {
        if (list.size() > 0) {
            if (theFilletSection.parametricallyIdentical(
                        (FilletSection3D) list.lastElement())) {
                return;
            }
        }

        list.addElement(theFilletSection);
    }

    /**
     * �t�B���b�g�f��?���t�B���b�g�Ȗ�?�񂵂Ď��?o��?B
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return �t�B���b�g�Ȗ�?��(�f�ʂ�?���2�ɖ����Ȃ�?�?���null��Ԃ�)
     *
     * @see FilletObject3D
     */
    FilletObject3D toFilletObject3D(boolean doExchange) {
        int nSecs;

        if ((nSecs = list.size()) < 2) {
            return null;
        }

        FilletSection3D[] secs = new FilletSection3D[nSecs];
        list.copyInto(secs);

        if (doExchange) {
            for (int i = 0; i < secs.length; i++)
                secs[i] = secs[i].exchange();
        }

        return new FilletObject3D(secs);
    }

    /*
     * �t�B���b�g�Ȗ�?���ǉB���?B
     * �����ł͒P?��ɒǉB���̂�?B
     * @param theFilletObject        �ǉB���t�B���b�g�Ȗ�?��
     * @see        FilletObject3D
     */
    void addFillet(FilletObject3D theFilletObject) {
        list.addElement(theFilletObject);
    }

    /**
     * �t�B���b�g�Ȗ�?���z��Ƃ��Ď��?o��?B
     *
     * @param doExchange DOCUMENT ME!
     *
     * @return �t�B���b�g��?�?��̔z��
     *
     * @see FilletObject3D
     */
    FilletObject3D[] toFilletObject3DArray(boolean doExchange) {
        FilletObject3D[] flts = new FilletObject3D[list.size()];
        list.copyInto(flts);

        if (doExchange) {
            for (int i = 0; i < flts.length; i++)
                flts[i] = flts[i].exchange();
        }

        return flts;
    }
}
// end of file
