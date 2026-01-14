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

// JTEM - Java Tools for Experimental Mathematics
// Copyright (C) 2001 JEM-Group
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
package org.jscience.mathematics.algebraic.matrices.gui;

import org.jscience.mathematics.algebraic.matrices.ComplexVector;
import org.jscience.mathematics.algebraic.numbers.Complex;

import java.beans.*;


/**
 * BeanInfo for ComplexVector.
 */
public class ComplexVectorBeanInfo extends SimpleBeanInfo {
    /** DOCUMENT ME! */
    private static final Class beanClass = ComplexVector.class;

    /** DOCUMENT ME! */
    private final PropertyDescriptor[] pd = new PropertyDescriptor[4];

    /** DOCUMENT ME! */
    private final BeanDescriptor bd = new BeanDescriptor(beanClass,
            ComplexVectorCustomizer.class);

/**
     * Creates a new ComplexVectorBeanInfo object.
     *
     * @throws IntrospectionException DOCUMENT ME!
     */
    public ComplexVectorBeanInfo() throws IntrospectionException {
        System.out.println("ComplexVectorBeanInfo.<init>");

        PropertyDescriptor p = new PropertyDescriptor("size", beanClass,
                "size", null);
        pd[0] = p;

        try {
            java.lang.reflect.Method set = beanClass.getMethod("set",
                    new Class[] { int.class, Complex.class });
            java.lang.reflect.Method get = beanClass.getMethod("get",
                    new Class[] { int.class });
            java.lang.reflect.Method getA = beanClass.getMethod("toArray",
                    new Class[0]);
            p = new IndexedPropertyDescriptor("element", getA, null, get, set);
            System.out.println(
                "ComplexVectorBeanInfo.<init>: all methods found");
        } catch (NoSuchMethodException ex) {
            System.out.println(
                "ComplexVectorBeanInfo.<init>: not all methods found");
            throw new IntrospectionException(
                "missing method defined in this beaninfo");
        }

        p.setDisplayName("vector elements");
        pd[1] = p;
        p = new PropertyDescriptor("re", beanClass);
        p.setDisplayName("re part vector");
        pd[2] = p;
        p = new PropertyDescriptor("im", beanClass);
        p.setDisplayName("im part vector");
        pd[3] = p;
        bd.setDisplayName("Field.Complex AbstractVector");
        bd.setShortDescription("A AbstractVector of complex numbers.");
        System.out.println("ComplexVectorBeanInfo.<init>: ok");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        return pd;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDefaultPropertyIndex() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BeanDescriptor getBeanDescriptor() {
        return bd;
    }
}
