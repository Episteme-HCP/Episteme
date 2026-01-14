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

package org.jscience.ml.sbml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores the annotation metadata for an SBML node.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public final class Annotations {
    private List annotations;

    private static String wrapItem(String item) {
        return SBase.isItemOfType("annotation", item) ? item : "<annotation>" + item + "</annotation>";
    }

    Annotations() {
    }

    public void add(String annotation) {
        if (annotations == null)
            annotations = new ArrayList();
        annotations.add(wrapItem(annotation));
    }

    public void clear() {
        annotations = null;
    }

    public String get(int index) {
        if (annotations == null)
            throw new ArrayIndexOutOfBoundsException(index);
        return (String) annotations.get(index);
    }

    public int size() {
        return annotations == null ? 0 : annotations.size();
    }

    public Iterator iterator() {
        if (annotations == null)
            annotations = new ArrayList();
        return annotations.iterator();
    }

    public void remove(int index) {
        if (annotations == null)
            throw new ArrayIndexOutOfBoundsException(index);
        annotations.remove(index);
    }

    public void set(int index, String annotation) {
        if (annotations == null)
            annotations = new ArrayList();
        annotations.set(index, wrapItem(annotation));
    }

    /**
     * The SBML for this element.
     */

    public String toString() {
        if (annotations == null)
            return "";
        StringBuffer buffer = new StringBuffer();
        for (Iterator iterator = annotations.iterator(); iterator.hasNext();)
            buffer.append((String) iterator.next() + "\n");
        return buffer.toString();
    }
}
