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

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Writer;


/**
 * A class that implmements XMLSerializable can be written to an external
 * target and later reinitialized using that target as a source. As the data
 * is written in XML format, the exported data can be embedded in XML files
 * that also contain other data.
 *
 * @author Holger Antelmann
 */
public interface XMLSerializable {
    /**
     * exports the enire object as XML and writes it to the given
     * Writer, so that the object can be reconstructed with
     * <code>importXML(InputSource)</code>. The given Writer is neither
     * flushed nor closed, so that other data may be effectively written to
     * the InputSource after the method returned.
     *
     * @see #importXML(InputSource)
     */
    void exportXML(Writer out) throws IOException;

    /**
     * The object is fully reinitialized with the XML data contained in
     * the given ImputSource, so that the object has the same state as it had
     * during export.
     *
     * @see #exportXML(Writer)
     */
    void importXML(InputSource source) throws SAXException, IOException;
}
