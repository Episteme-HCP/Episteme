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

package org.jscience.io;

import java.io.File;


/**
 * filters by matching the file name to a regular expression.
 *
 * @author Holger Antelmann
 */
public class FileNamePatternFilter {
    /** DOCUMENT ME! */
    String regex;

    /** DOCUMENT ME! */
    boolean fullPath;

/**
     * Creates a new FileNamePatternFilter object.
     *
     * @param regexFileName DOCUMENT ME!
     */
    public FileNamePatternFilter(String regexFileName) {
        this(regexFileName, false);
    }

/**
     * Creates a new FileNamePatternFilter object.
     *
     * @param regexFileName   DOCUMENT ME!
     * @param includeFullPath DOCUMENT ME!
     */
    public FileNamePatternFilter(String regexFileName, boolean includeFullPath) {
        regex = regexFileName;
        fullPath = includeFullPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean accept(File file) {
        return fullPath ? file.getAbsolutePath().matches(regex)
                        : file.getName().matches(regex);
    }
}
