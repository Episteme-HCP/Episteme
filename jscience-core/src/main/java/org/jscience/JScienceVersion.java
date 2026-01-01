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

package org.jscience;

/**
 * Provides version, author, and build information for the JScience library.
 * <p>
 * Version information is loaded from {@code version.properties} if available,
 * otherwise defaults are used.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class JScienceVersion {

    /** The version string (e.g., "5.0.0-SNAPSHOT") */
    public static final String VERSION;

    /** The build date (e.g., "2025-12-29") */
    public static final String BUILD_DATE;

    /** The authors of JScience */
    public static final String[] AUTHORS = {
            "Silvere Martin-Michiellot",
            "Gemini AI (Google DeepMind)"
    };

    static {
        String v = "5.0.0-SNAPSHOT";
        String d = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        try (java.io.InputStream is = JScienceVersion.class.getResourceAsStream("version.properties")) {
            if (is != null) {
                java.util.Properties p = new java.util.Properties();
                p.load(is);
                v = p.getProperty("version", v);
                d = p.getProperty("build.date", d);
            }
        } catch (Exception e) {
            // Ignore - use defaults
        }
        VERSION = v;
        BUILD_DATE = d;
    }

    private JScienceVersion() {
        // Prevent instantiation
    }
}


