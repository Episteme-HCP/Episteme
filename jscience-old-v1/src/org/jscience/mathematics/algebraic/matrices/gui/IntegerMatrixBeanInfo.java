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

import org.jscience.mathematics.algebraic.matrices.IntegerMatrix;
import org.jscience.mathematics.algebraic.matrices.IntegerVector;

import java.beans.*;


/**
 * BeanInfo for IntegerMatrix.
 */
public class IntegerMatrixBeanInfo extends SimpleBeanInfo {
    /** DOCUMENT ME! */
    private static final Class beanClass = IntegerMatrix.class;

    /** DOCUMENT ME! */
    private final PropertyDescriptor[] pd = new PropertyDescriptor[7];

    /** DOCUMENT ME! */
    private final BeanDescriptor bd = new BeanDescriptor(beanClass,
            IntegerMatrixCustomizer.class);

/**
     * Creates a new IntegerMatrixBeanInfo object.
     *
     * @throws IntrospectionException DOCUMENT ME!
     */
    public IntegerMatrixBeanInfo() throws IntrospectionException {
        try {
            System.out.println("IntegerMatrixBeanInfo.<init>");

            PropertyDescriptor p;
            java.lang.reflect.Method get = beanClass.getMethod("getNumCols",
                    new Class[0]);
            p = new PropertyDescriptor("numCols", get, null);
            p.setDisplayName("number of columns");
            pd[0] = p;
            get = beanClass.getMethod("getNumRows", new Class[0]);
            p = new PropertyDescriptor("numRows", get, null);
            p.setDisplayName("number of rows");
            pd[1] = p;
            get = beanClass.getMethod("getNumEntries", new Class[0]);
            p = new PropertyDescriptor("numEntries", get, null);
            p.setDisplayName("number of entries");
            pd[2] = p;
            System.out.println('2');
            p = new IndexedPropertyDescriptor("row", null, null,
                    beanClass.getMethod("getRow", new Class[] { int.class }),
                    beanClass.getMethod("setRow",
                        new Class[] { int.class, IntegerVector.class }));
            p.setDisplayName("row as vec");
            pd[3] = p;
            System.out.println('3');
            p = new IndexedPropertyDescriptor("col", null, null,
                    beanClass.getMethod("getCol", new Class[] { int.class }),
                    beanClass.getMethod("setCol",
                        new Class[] { int.class, IntegerVector.class }));
            p.setDisplayName("column as vec");
            pd[4] = p;
            System.out.println('4');
            p = new PropertyDescriptor("square", beanClass, "isSquared", null);
            p.setDisplayName("square matrix ?");
            pd[5] = p;
            p = new PropertyDescriptor("symmetric", beanClass, "isSymmetric",
                    null);
            p.setDisplayName("symmetric matrix ?");
            pd[6] = p;
            bd.setDisplayName("Int AbstractMatrix");
            bd.setShortDescription("A matrix of Int numbers.");
        } catch (NoSuchMethodException ex) {
            System.out.println(ex);
            throw new IntrospectionException("method missing");
        } catch (IntrospectionException ex) {
            System.out.println(ex);
            throw ex;
        }
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
        return -1;
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
