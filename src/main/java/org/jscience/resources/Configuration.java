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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Global configuration loader for JScience.
 * <p>
 * Loads properties from 'jscience.properties' at the root of the classpath.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class Configuration {

    private static final Properties properties = new Properties();

    static {
        try (InputStream is = Configuration.class.getResourceAsStream("/jscience.properties")) {
            if (is != null) {
                properties.load(is);
            } else {
                System.err.println("Warning: jscience.properties not found on classpath.");
            }
        } catch (IOException e) {
            System.err.println("Error loading jscience.properties: " + e.getMessage());
        }
    }

    /**
     * Gets a property value.
     * 
     * @param key the property key
     * @return the property value, or null if not found
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a property value with a default.
     * 
     * @param key          the property key
     * @param defaultValue the default value
     * @return the property value, or defaultValue if not found
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}

