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
package org.jscience.mathematics.logic.connector;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * Interface for importing formal systems from external verification tools.
 * <p>
 * Importers parse files from formal verification systems (Coq, Metamath, Qedeq)
 * and convert them into JScience logic structures.
 * </p>
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface FormalSystemImporter {

    /**
     * Imports a formal system from the specified reader.
     * 
     * @param reader the reader to import from
     * @return a map of module names to their axioms/theorems
     * @throws IOException    if an I/O error occurs
     * @throws ParseException if the input format is invalid
     */
    Map<String, Object> importSystem(Reader reader) throws IOException, ParseException;

    /**
     * Returns the file extensions this importer can handle.
     * 
     * @return array of file extensions (e.g., [".v", ".vo"])
     */
    String[] getSupportedExtensions();

    /**
     * Exception thrown when parsing fails.
     */
    class ParseException extends Exception {
        private static final long serialVersionUID = 1L;

        public ParseException(String message) {
            super(message);
        }

        public ParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
