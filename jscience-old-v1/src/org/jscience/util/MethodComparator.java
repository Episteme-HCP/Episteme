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

package org.jscience.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;


/**
 * compares objects by the return value of a specified method name. This
 * Comparator uses reflection to access the return values of the methods to be
 * compared.
 *
 * @author Holger Antelmann
 * @param <T> DOCUMENT ME!
 */
public class MethodComparator<T> implements Comparator<T> {
    /**
     * DOCUMENT ME!
     */
    String methodName;

    /**
     * DOCUMENT ME!
     */
    boolean ascending;

    /**
     * calls <code>MethodComparator(methodName, true)</code>
     */
    public MethodComparator(String methodName) {
        this(methodName, true);
    }

    /**
     * the methodName (usually a 'getter'-method) must have no arguments
     * for the objects to be compared. If ascending is false, the return
     * value of <code>compareTo(Object)</code> will be reversed.
     */
    public MethodComparator(String methodName, boolean ascending) {
        this.methodName = methodName;
        this.ascending = ascending;
    }

    /**
     * lists all method names of the given class that take no
     * parameters and return something but void - and are therefore usable for
     * a MethodComparator. If comparableReturnValuesOnly is true, the return
     * values of the methods must also implement Comparable.
     *
     * @param c                          DOCUMENT ME!
     * @param comparableReturnValuesOnly DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String[] getUsableMethodNames(Class c,
                                                boolean comparableReturnValuesOnly) {
        ArrayList<String> list = new ArrayList<String>();
        Method[] mlist = c.getMethods();

        for (Method m : mlist) {
            if (m.getReturnType().toString().equals("void")) {
                continue;
            }

            if (comparableReturnValuesOnly) {
                if (!classImplements(m.getReturnType(), Comparable.class)) {
                    continue;
                }
            }

            if (m.getParameterTypes().length == 0) {
                list.add(m.getName());
            }
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param i DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    static boolean classImplements(Class c, Class i) {
        for (Class x : c.getInterfaces()) {
            if (x.equals(i)) {
                return true;
            }

            if (classImplements(x, i)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * if the object returned by the specified method of the first
     * given object doesn't implement <code>Comparable</code>, the
     * <code>toString()</code> value of that object is used instead. If a null
     * value is encountered, it is considered to come before any other value.
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ClassCastException if any of the following Exceptions occur:
     *                            InvocationTargetException, NoSuchMethodException,
     *                            NoSuchMethodException
     */
    public int compare(T o1, T o2) throws ClassCastException {
        try {
            Method m1 = o1.getClass().getMethod(methodName);
            Method m2 = o2.getClass().getMethod(methodName);
            Object c1 = m1.invoke(o1);
            Object c2 = m2.invoke(o2);
            int result = internalCompare(c1, c2);

            if (!ascending) {
                result = 0 - result;
            }

            return result;
        } catch (InvocationTargetException ex) {
            ClassCastException cce = new ClassCastException(
                    "method call throws exception: " + ex.getMessage());
            cce.initCause(ex);
            throw cce;
        } catch (IllegalAccessException ex) {
            ClassCastException cce = new ClassCastException(
                    "method call not accessible: " + ex.getMessage());
            cce.initCause(ex);
            throw cce;
        } catch (NoSuchMethodException ex) {
            ClassCastException cce = new ClassCastException(
                    "given class doesn't contain method: " + methodName);
            cce.initCause(ex);
            throw cce;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param <U> DOCUMENT ME!
     * @param o1  DOCUMENT ME!
     * @param o2  DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private static <U> int internalCompare(Comparable<U> o1, U o2) {
        return o1.compareTo(o2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private static int internalCompare(Object o1, Object o2) {
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            }

            return -1;
        }

        if (o2 == null) {
            if (o1 == null) {
                return 0;
            }

            return 1;
        }

        return o1.toString().compareTo(o2.toString());
    }
}
