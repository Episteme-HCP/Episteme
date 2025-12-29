/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import java.util.*;

/**
 * Type inference for property values.
 * <p>
 * Automatically infers types from string values in property files:
 * - int, long, double, float
 * - boolean
 * - String
 * - List (comma-separated)
 * - Map (key=value pairs)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TypeInference {

    /**
     * Infers the type and converts the value.
     * 
     * @param value the string value
     * @return the converted object
     */
    public static Object infer(String value) {
        if (value == null || value.trim().isEmpty()) {
            return value;
        }

        String trimmed = value.trim();

        // Boolean
        if (trimmed.equalsIgnoreCase("true") || trimmed.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(trimmed);
        }

        // List (comma-separated)
        if (trimmed.contains(",") && !trimmed.contains("=")) {
            return parseList(trimmed);
        }

        // Map (key=value pairs)
        if (trimmed.contains("=")) {
            return parseMap(trimmed);
        }

        // Number
        Object number = tryParseNumber(trimmed);
        if (number != null) {
            return number;
        }

        // Default: String
        return value;
    }

    /**
     * Tries to parse as a number.
     */
    private static Object tryParseNumber(String value) {
        try {
            // Try int first
            if (!value.contains(".") && !value.toLowerCase().contains("e")) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    // Try long
                    return Long.parseLong(value);
                }
            }

            // Try double
            if (value.toLowerCase().endsWith("d")) {
                return Double.parseDouble(value.substring(0, value.length() - 1));
            }

            // Try float
            if (value.toLowerCase().endsWith("f")) {
                return Float.parseFloat(value.substring(0, value.length() - 1));
            }

            // Default to double for decimal numbers
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses a comma-separated list.
     */
    private static List<Object> parseList(String value) {
        List<Object> list = new ArrayList<>();
        String[] parts = value.split(",");
        for (String part : parts) {
            list.add(infer(part.trim()));
        }
        return list;
    }

    /**
     * Parses key=value pairs into a map.
     */
    private static Map<String, Object> parseMap(String value) {
        Map<String, Object> map = new LinkedHashMap<>();
        String[] pairs = value.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(kv[0].trim(), infer(kv[1].trim()));
            }
        }
        return map;
    }

    /**
     * Loads properties with type inference.
     * 
     * @param properties the properties
     * @return map with inferred types
     */
    public static Map<String, Object> loadWithInference(Properties properties) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : properties.stringPropertyNames()) {
            result.put(key, infer(properties.getProperty(key)));
        }
        return result;
    }

    /**
     * Gets a typed value from a map.
     * 
     * @param <T>   the type
     * @param map   the map
     * @param key   the key
     * @param clazz the class
     * @return the typed value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Map<String, Object> map, String key, Class<T> clazz) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        throw new ClassCastException("Cannot cast " + value.getClass() + " to " + clazz);
    }
}