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

package org.jscience.engineering.eventdriven;

import java.util.HashMap;
import java.util.Map;


/**
 * <p/>
 * Contains a set of static utility methods used by other classes in the
 * package.
 * </p>
 *
 * @author Pete Ford, May 30, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
 */

//**********************************************
//
//This package is rebundled after the code from JSpasm
//
// Project Homepage : http://jspasm.sourceforge.net/
// Original Developer : Pete Ford
// Official Domain : CodeXombie.com
//
//**********************************************

class MiscellaneousUtils {
    /**
     * <p/>
     * Maps primitive type strings to the matching class objects.
     * </p>
     */
    private final static Map p2c;

    static {
        p2c = new HashMap();
        p2c.put("byte", byte.class);
        p2c.put("short", short.class);
        p2c.put("int", int.class);
        p2c.put("long", long.class);
        p2c.put("char", char.class);
        p2c.put("float", float.class);
        p2c.put("double", double.class);
        p2c.put("boolean", boolean.class);
    }

    /**
     * <p/>
     * Private constructor prevents the class being instantiated.
     * </p>
     */
    private MiscellaneousUtils() {
    }

    /**
     * <p/>
     * An array extension of the <code>Class.isAssignableFrom()</code> method.
     * </p>
     *
     * @param classArray1 Class Array 1.
     * @param classArray2 Class Array 2.
     * @return <code>true</code> if every class in Array 1 is assignable from
     *         its counterpart class in Array 2;<code>false</code> otherwise.
     */
    static boolean arrayIsAssignable(Class[] classArray1, Class[] classArray2) {
        boolean result = true;

        if (classArray1.length != classArray2.length) {
            result = false;
        } else {
            for (int index = 0; index < classArray1.length; index++) {
                if (!classArray1[index].isAssignableFrom(classArray2[index])) {
                    result = false;

                    break;
                }
            }
        }

        return result;
    }

    /**
     * <p/>
     * For a given array of Objects, creates and returns an array containing
     * the matching Class objects.
     * </p>
     *
     * @param objectArray The array to generate the class array from.
     * @return the class array.
     */
    static Class[] classesFromObjects(Object[] objectArray) {
        Class[] classArray = new Class[objectArray.length];

        for (int index = 0; index < objectArray.length; index++) {
            classArray[index] = objectArray[index].getClass();
        }

        return classArray;
    }

    /**
     * <p/>
     * Creates an array of classes from a comma/space-separated list of
     * fully-qualified class names.
     * </p>
     *
     * @param classString Comma/space-separated list if class names.
     * @return a class array.
     * @throws ClassNotFoundException if any of the named classes cannot be
     *                                loaded.
     */
    static Class[] classesFromString(String classString)
            throws ClassNotFoundException {
        if (classString.trim().equals("")) {
            return new Class[0];
        }

        String[] classNames = classString.split("[, ]+");
        Class[] classArray = new Class[classNames.length];

        for (int index = 0; index < classNames.length; index++) {
            if (p2c.containsKey(classNames[index])) {
                classArray[index] = (Class) p2c.get(classNames[index]);
            } else {
                classArray[index] = Class.forName(classNames[index]);
            }
        }

        return classArray;
    }

    /**
     * <p/>
     * Given an array of class objects, creates a String of class names (the
     * inverse of the <code>classesFromString()</code> method).
     * </p>
     *
     * @param argClasses The class array.
     * @return the string representation of the list.
     */
    static String stringFromClasses(Class[] argClasses) {
        StringBuffer sb = new StringBuffer();

        for (int index = 0; index < argClasses.length; index++) {
            if (index > 0) {
                sb.append(", ");
            }

            sb.append(argClasses[index].getName());
        }

        return sb.toString();
    }
}
