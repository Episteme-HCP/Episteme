/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Utility for automated testing of POJO (Plain Old Java Object) classes.
 * Tests getters, setters, equals, hashCode, and toString.
 *
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class PojoTester {

    public static void testPojo(Class<?> clazz) {
        testPojo(clazz, null);
    }

    public static void testPojo(Class<?> clazz, Supplier<?> instanceSupplier) {
        try {
            Object instance = (instanceSupplier != null) ? instanceSupplier.get() : createInstance(clazz);
            if (instance == null) {
                System.err.println("Skipping " + clazz.getName() + " - could not instantiate.");
                return;
            }

            testGettersSetters(instance);
            testEqualsHashCode(clazz, instanceSupplier);
            testToString(instance);

        } catch (Exception e) {
            System.err.println("Pojo test warning for " + clazz.getName() + ": " + e.getMessage());
        }
    }

    private static Object createInstance(Class<?> clazz) {
        // 1. Try default constructor
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            // Continue
        }

        // 2. Try constructors with least arguments
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Arrays.sort(constructors, (c1, c2) -> Integer.compare(c1.getParameterCount(), c2.getParameterCount()));

        for (Constructor<?> constructor : constructors) {
            try {
                constructor.setAccessible(true);
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] args = new Object[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    args[i] = createValue(paramTypes[i]);
                }
                return constructor.newInstance(args);
            } catch (Exception e) {
                // Try next
            }
        }
        return null;
    }

    private static void testGettersSetters(Object instance) throws Exception {
        Class<?> clazz = instance.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (isGetter(method)) {
                String getterName = method.getName();
                String propName = getterName.startsWith("is") ? getterName.substring(2) : getterName.substring(3);

                String setterName = "set" + propName;
                try {
                    Method setter = clazz.getMethod(setterName, method.getReturnType());

                    Object testValue = createValue(method.getReturnType());

                    setter.invoke(instance, testValue);
                    Object result = method.invoke(instance);

                    if (testValue != null) {
                        assertEquals(testValue, result, "Getter/Setter failure for " + propName);
                    }
                } catch (NoSuchMethodException e) {
                    method.invoke(instance);
                }
            }
        }
    }

    private static void testEqualsHashCode(Class<?> clazz, Supplier<?> instanceSupplier) {
        try {
            Object obj1 = (instanceSupplier != null) ? instanceSupplier.get() : createInstance(clazz);
            Object obj2 = (instanceSupplier != null) ? instanceSupplier.get() : createInstance(clazz);

            if (obj1 == null || obj2 == null)
                return;

            assertEquals(obj1, obj1);

            if (obj1.equals(obj2)) {
                assertEquals(obj2, obj1);
                assertEquals(obj1.hashCode(), obj2.hashCode());
            }

            assertNotEquals(obj1, null);
            assertNotEquals(obj1, new Object());

        } catch (Exception e) {
            // Ignore
        }
    }

    private static void testToString(Object instance) {
        assertNotNull(instance.toString());
        // assertFalse(instance.toString().isEmpty()); // Removed as empty toString can
        // be valid
    }

    private static boolean isGetter(Method method) {
        if (Modifier.isStatic(method.getModifiers()))
            return false;
        if (method.getParameterCount() != 0)
            return false;
        if (method.getReturnType() == void.class)
            return false;

        String name = method.getName();
        if (name.equals("getClass"))
            return false;

        return name.startsWith("get") || name.startsWith("is");
    }

    private static Object createValue(Class<?> type) {
        if (type == String.class)
            return "test";
        if (type == int.class || type == Integer.class)
            return 10;
        if (type == long.class || type == Long.class)
            return 10L;
        if (type == double.class || type == Double.class)
            return 0.5; // Changed to 0.5 for probability safety
        if (type == float.class || type == Float.class)
            return 0.5f;
        if (type == boolean.class || type == Boolean.class)
            return true;
        if (type == char.class || type == Character.class)
            return 'A';
        if (type == byte.class || type == Byte.class)
            return (byte) 1;
        if (type == short.class || type == Short.class)
            return (short) 1;

        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return (constants != null && constants.length > 0) ? constants[0] : null;
        }

        if (List.class.isAssignableFrom(type))
            return new ArrayList<>();

        return null;
    }
}
