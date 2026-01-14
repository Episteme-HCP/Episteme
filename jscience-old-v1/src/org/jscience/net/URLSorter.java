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

package org.jscience.net;

import java.net.URL;

import java.util.Arrays;
import java.util.Comparator;


/**
 * A simple convenience class that sorts URLs by their external form
 * lexicographically.<p>Note that this implementation is inconsistent to
 * the equals() method.</p>
 *
 * @author Holger Antelmann
 */
public class URLSorter implements Comparator<URL> {
    /**
     * DOCUMENT ME!
     */
    static URLSorter sorter = new URLSorter();

    /**
     * DOCUMENT ME!
     *
     * @param urlList DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static URL[] sort(URL[] urlList) {
        Arrays.sort(urlList, sorter);

        return urlList;
    }

    /**
     * DOCUMENT ME!
     *
     * @param url1 DOCUMENT ME!
     * @param url2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(URL url1, URL url2) {
        return url1.toExternalForm().compareTo(url2.toExternalForm());
    }
}
