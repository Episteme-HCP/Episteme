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

package org.jscience.architecture.traffic.xml;

import org.jscience.architecture.traffic.util.StringUtils;

import java.io.OutputStream;
import java.io.PrintWriter;


/**
 * A utility class to write XML to an OutputStream
 */
class XMLWriter extends PrintWriter {
    /** DOCUMENT ME! */
    OutputStream stream;

/**
     * Make a new XMLWriter
     *
     * @param stream    The OutputStream on which this XMLWriter is based
     * @param autoFlush Indicates if the internal PrintWriter needs autoflush
     */
    public XMLWriter(OutputStream stream, boolean autoFlush) {
        super(stream, autoFlush);
        this.stream = stream;
    }

/**
     * Make a new XMLWriter
     *
     * @param stream The OutputStream on which this XMLWriter is based
     */
    public XMLWriter(OutputStream stream) {
        super(stream);
    }

    /**
     * Write the open tag of a XMLElement
     *
     * @param element The XMLElement
     * @param indent The number of spaces to indent the tag
     */
    public void writeOpenTag(XMLElement element, int indent) {
        println(StringUtils.repeat(' ', indent) + element.getOpenTag());
    }

    /**
     * Write the open tag of a XMLElement
     *
     * @param element The XMLElement
     */
    public void writeOpenTag(XMLElement element) {
        writeOpenTag(element, 0);
    }

    /**
     * Write the close tag of a XMLElement
     *
     * @param element The XMLElement
     * @param indent The number of spaces to indent the tag
     */
    public void writeCloseTag(XMLElement element, int indent) {
        println(StringUtils.repeat(' ', indent) + element.getCloseTag());
    }

    /**
     * Write the close tag of a XMLElement
     *
     * @param element The XMLElement
     */
    public void writeCloseTag(XMLElement element) {
        writeCloseTag(element, 0);
    }

    /**
     * Write both open and close tags of a XMLElement
     *
     * @param element The XMLElement
     * @param indent The number of spaces to indent the tags
     */
    public void writeAtomaryElement(XMLElement element, int indent) {
        println(StringUtils.repeat(' ', indent) + element.getOpenTag() + " " +
            element.getCloseTag());
    }

    /**
     * Write both open and close tags of a XMLElement
     *
     * @param element The XMLElement
     */
    public void writeAtomaryElement(XMLElement element) {
        writeAtomaryElement(element, 0);
    }
}
