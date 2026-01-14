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

import java.util.Enumeration;
import java.util.Vector;


/**
 * �`?�v�f?�̓_�̃��X�g�赂����߂̃N���X
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:19:44 $
 */
class PointOnGeometryList {
    /**
     * �`?�v�f?�̓_�̃��X�g
     *
     * @see Vector
     */
    Vector list;

    /*
     * �R���X�g���N�^
     */
    PointOnGeometryList() {
        super();
        list = new Vector();
    }

    /*
     * 2D��?�?�̓_��ǉB���?B
     * ��ɓ���������?݂���Ƃ��͒ǉB��Ȃ�?B
     * @param thePointOnCurve        �ǉB���2D��?�?�̓_
     * @see        PointOnCurve2D
     */
    void addPoint(PointOnCurve2D thePointOnCurve) {
        PointOnCurve2D mate;

        for (Enumeration en = list.elements(); en.hasMoreElements();) {
            if (thePointOnCurve.parametricallyIdentical(
                        (PointOnCurve2D) en.nextElement())) {
                return;
            }
        }

        list.addElement(thePointOnCurve);
    }

    /*
     * 2D��?�?�̓_��ǉB���?B
     * ��ɓ���������?݂���Ƃ��͒ǉB��Ȃ�?B
     * @param thePointOnCurve        �ǉB���2D��?�?�̓_
     * @see        PointOnCurve2D
     */
    void addPoint(ParametricCurve2D curve, double parameter) {
        addPoint(new PointOnCurve2D(curve, parameter,
                GeometryElement.doCheckDebug));
    }

    /*
     * 3D��?�?�̓_��ǉB���?B
     * ��ɓ���������?݂���Ƃ��͒ǉB��Ȃ�?B
     * @param thePointOnCurve        �ǉB���3D��?�?�̓_
     * @see        PointOnCurve3D
     */
    void addPoint(PointOnCurve3D thePointOnCurve) {
        PointOnCurve3D mate;

        for (Enumeration en = list.elements(); en.hasMoreElements();) {
            if (thePointOnCurve.parametricallyIdentical(
                        (PointOnCurve3D) en.nextElement())) {
                return;
            }
        }

        list.addElement(thePointOnCurve);
    }

    /*
     * 3D��?�?�̓_��ǉB���?B
     * ��ɓ���������?݂���Ƃ��͒ǉB��Ȃ�?B
     * @param thePointOnCurve        �ǉB���3D��?�?�̓_
     * @see        PointOnCurve3D
     */
    void addPoint(ParametricCurve3D curve, double parameter) {
        addPoint(new PointOnCurve3D(curve, parameter,
                GeometryElement.doCheckDebug));
    }

    /**
     * 2D��?�?�̓_��z��Ƃ��Ď��?o��?B
     *
     * @return 2D��?�?�̓_�̔z��
     *
     * @see PointOnCurve2D
     */
    PointOnCurve2D[] toPointOnCurve2DArray() {
        PointOnCurve2D[] pocs = new PointOnCurve2D[list.size()];
        list.copyInto(pocs);

        return pocs;
    }

    /**
     * 3D��?�?�̓_��z��Ƃ��Ď��?o��?B
     *
     * @return 3D��?�?�̓_�̔z��
     *
     * @see PointOnCurve3D
     */
    PointOnCurve3D[] toPointOnCurve3DArray() {
        PointOnCurve3D[] pocs = new PointOnCurve3D[list.size()];
        list.copyInto(pocs);

        return pocs;
    }
}
// end of file
