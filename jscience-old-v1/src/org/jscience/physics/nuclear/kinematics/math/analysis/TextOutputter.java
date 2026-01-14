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

/*
 * TextOutputter.java
 *
 * Created on June 18, 2001, 12:08 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;


/**
 * Gives a framework for subclasses to use System.out to creat their output
 *
 * @author org.jscience.physics.nuclear.kinematics
 */
public class TextOutputter extends Object {
    /**
     * DOCUMENT ME!
     */
    static public String DEFAULT = "default";

    /**
     * DOCUMENT ME!
     */
    PrintStream defaultOutput = System.out;

    /**
     * DOCUMENT ME!
     */
    FileOutputStream fos;

/**
     * Creates new TextOutputter
     */
    public TextOutputter(String output) throws FileNotFoundException {
        setOutput(output);
    }

    /**
     * DOCUMENT ME!
     *
     * @param output DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     */
    void setOutput(String output) throws FileNotFoundException {
        if (!output.equals(DEFAULT)) {
            fos = new FileOutputStream(output);
            System.setOut(new PrintStream(fos));
            defaultOutput.println("Output now going to '" + output + "'");
        } //else if DEFAULT, leave it
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void closeOutput() throws IOException {
        if (fos != null) {
            fos.flush();
            fos.close();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void revertToDefaultOutput() {
        System.setOut(defaultOutput);
    }
}
