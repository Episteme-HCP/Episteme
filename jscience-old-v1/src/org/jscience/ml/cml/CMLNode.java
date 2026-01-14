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

package org.jscience.ml.cml;

import java.io.IOException;
import java.io.Writer;

/**
 * Adds i/o control
 *
 * @author P.Murray-Rust, 2003
 */
public interface CMLNode {

    /**
     * output CML variants
     */
    public final static String ARRAY = "ARRAY";
    public final static String CML1 = "CML1";
    public final static String CML2 = "CML2";

    /**
     * set CML version
     * <p/>
     * 1 or 2 at present (default 2)
     *
     * @param v version
     * @throws unsupported version
     */
    void setVersion(String v) throws CMLException;

    /**
     * set array syntax
     * <p/>
     * set CML array syntax (default false)
     *
     * @param syntax
     */
    void setArraySyntax(boolean syntax);

    /**
     * write XML (allows for syntactic variants)
     *
     * @param control applications-specific, for example "CML1+array" or "CML2"
     * @param w       output
     * @throws IOException
     * @throws org.jscience.ml.cml.CMLException
     *
     */

    void writeXML(Writer w, String control) throws org.jscience.ml.cml.CMLException, IOException;

    /**
     * write XML (uses default control)
     *
     * @param w output
     */
    void writeXML(Writer w) throws org.jscience.ml.cml.CMLException, IOException;

}
