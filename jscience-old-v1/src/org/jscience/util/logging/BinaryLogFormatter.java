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

package org.jscience.util.logging;

import org.jscience.io.IOUtils;

import java.io.IOException;
import java.io.Serializable;


/**
 * formats the LogEntry as a serialized byte array.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.io.IOUtils#deserialize(byte[])
 */
public class BinaryLogFormatter implements LogEntryFormatter {
    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws LogException DOCUMENT ME!
     */
    public Object format(LogEntry entry) {
        try {
            return IOUtils.serialize(entry);
        } catch (IOException ex) {
            Object[] param = entry.getParameters();

            for (int i = 0; i < param.length; i++) {
                if (!(param[i] instanceof Serializable)) {
                    param[i] = param[i].toString();
                }
            }

            try {
                return IOUtils.serialize(entry);
            } catch (IOException ioex) {
                throw new LogException("cannot serialize LogEntry", ioex);
            }
        }
    }
}
