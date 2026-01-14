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

package org.jscience.ml.openmath.phrasebook;

import java.util.Vector;

/**
 * Defines what a minimal phrasebook should implement. <p>
 * <p/>
 * Note that this class does NOT assume anything about the protocol
 * used between client and server. This is the responsibility of the
 * implementing subclass. Because of this, problems with the communication
 * should be thrown as a subclass of PhrasebookException. <p>
 *
 * @author Manfred Riem (mriem@win.tue.nl)
 * @version $Version$
 */
public abstract class Phrasebook {
    /**
     * Constructor. <p>
     */
    public Phrasebook() {
        super();
    }

    /**
     * A perform method. <p>
     *
     * @param fMethod    the type of perform.
     * @param fArguments the arguments to the perform.
     * @return the result.
     * @throws PhrasebookException thrown when a major error occurs.
     */
    public String perform(String fMethod, Vector fArguments)
            throws PhrasebookException {
        throw new PhrasebookException("Subclasses should implement this");
    }

    /**
     * An execute method. <p>
     * <p/>
     * <p/>
     * <i>Note:</i> the previous version of the library (1.2) didn't allow
     * you to return any object as a result of a perform. To make it possible
     * this method is the right one to implement.
     * </p>
     *
     * @param fMethod    the type of execution.
     * @param fArguments the arguments to the execute.
     * @return the result.
     * @throws PhrasebookException thrown when a major error occurs.
     */
    public Object execute(String fMethod, Vector fArguments)
            throws PhrasebookException {
        throw new PhrasebookException("Subclasses should implement this");
    }

    /**
     * Adds a CD to the phrasebook. <p>
     *
     * @param fName      the name of the CD.
     * @param fLocation  the URL of the CD, if none is given it is
     *                   assumed to be on the classpath
     * @param fClassName the Codec of the CD.
     * @throws PhrasebookException thrown when a major error occurs.
     */
    public void addCD(String fName, String fLocation, String fClassName)
            throws PhrasebookException {
        throw new PhrasebookException("Subclasses should implement this");
    }

    /**
     * Removes a CD from the phrasebook. <p>
     *
     * @param fName the name of the CD.
     * @throws PhrasebookException thrown when a major error occurs.
     */
    public void removeCD(String fName) throws PhrasebookException {
        throw new PhrasebookException("Subclasses should implement this");
    }
}
