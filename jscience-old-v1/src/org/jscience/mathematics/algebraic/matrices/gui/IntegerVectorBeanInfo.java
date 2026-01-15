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

import org.jscience.mathematics.algebraic.matrices.IntegerVector;

import java.beans.*;


/**
 * BeanInfo for IntegerVector.
 */
public class IntegerVectorBeanInfo extends SimpleBeanInfo {
    /** DOCUMENT ME! */
    private static final Class beanClass = IntegerVector.class;

    /** DOCUMENT ME! */
    private final PropertyDescriptor[] pd = new PropertyDescriptor[2];

    /** DOCUMENT ME! */
    private final BeanDescriptor bd = new BeanDescriptor(beanClass,
            IntegerVectorCustomizer.class);

/**
     * Creates a new IntegerVectorBeanInfo object.
     *
     * @throws IntrospectionException DOCUMENT ME!
     */
    public IntegerVectorBeanInfo() throws IntrospectionException {
        System.out.println("IntegerVectorBeanInfo.<init>");

        PropertyDescriptor p = new PropertyDescriptor("size", beanClass,
                "size", null);
        pd[0] = p;

        try {
            java.lang.reflect.Method set = beanClass.getMethod("set",
                    new Class[] { int.class, int.class });
            java.lang.reflect.Method get = beanClass.getMethod("get",
                    new Class[] { int.class });
            java.lang.reflect.Method getA = beanClass.getMethod("toArray",
                    new Class[0]);
            p = new IndexedPropertyDescriptor("element", getA, null, get, set);
            System.out.println(
                "IntegerVectorBeanInfo.<init>: all methods found");
        } catch (NoSuchMethodException ex) {
            System.out.println(
                "IntegerVectorBeanInfo.<init>: not all methods found");
            throw new IntrospectionException(
                "missing method defined in this beaninfo");
        }

        p.setDisplayName("vector elements");
        pd[1] = p;
        bd.setDisplayName("Int AbstractVector");
        bd.setShortDescription("A AbstractVector of Int numbers.");
        System.out.println("IntegerVectorBeanInfo.<init>: ok");
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
