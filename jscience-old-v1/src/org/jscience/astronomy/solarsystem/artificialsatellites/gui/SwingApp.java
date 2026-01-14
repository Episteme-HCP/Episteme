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

package org.jscience.astronomy.solarsystem.artificialsatellites.gui;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * 
 */
public class SwingApp {
/**
     * Deny the ability to construct an instance of this class.
     */
    private SwingApp() {
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param args Array of command line arguments
     */
    public static void main(String[] args) {
        try {
            AppFrame appFrame = new AppFrame();
            appFrame.setTitle(
                "org.jscience.astronomy.solarsystem.artificialsatellites");
            appFrame.setSize(1050, 700);

            appFrame.setVisible(true);
        } catch (Throwable t) {
            StringWriter stackTrace = new StringWriter();
            t.printStackTrace(new PrintWriter(stackTrace));
            System.out.println(stackTrace.toString());
        }
    }
}
