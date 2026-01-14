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
 * CopiaRespaldo.java
 *
 * Created on 30 of noviembre of 2001, 19:57
 */
package org.jscience.physics.fluids.dynamics.navierstokes;

import org.jscience.physics.fluids.dynamics.KernelADFC;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


/**
 * DOCUMENT ME!
 *
 * @author balrog
 */
public class SaveBackupCopy {
    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private ObjectOutputStream os;

    /** DOCUMENT ME! */
    private String name;

/**
     * Creates new SaveBackupCopy
     *
     * @param kadfc DOCUMENT ME!
     */
    public SaveBackupCopy(KernelADFC kadfc) {
        kernel = kadfc;

        name = kernel.getConfiguration().meshName + ".backup";
    }

    /**
     * DOCUMENT ME!
     *
     * @param u0 DOCUMENT ME!
     * @param u00 DOCUMENT ME!
     * @param u000 DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param ink DOCUMENT ME!
     */
    public void salvaBackup(double[][] u0, double[][] u00, double[][] u000,
        double[] p, double[] ink) {
        try {
            os = new ObjectOutputStream(new FileOutputStream(name));
        } catch (Exception ex) {
            kernel.out(
                "</FONT COLOR=#FF0000><B>SaveBackupCopy:</B> exception open " +
                name + "</FONT>");
        }

        try {
            os.writeObject(u0);
            os.writeObject(u00);
            os.writeObject(u000);
            os.writeObject(p);
            os.writeObject(ink);
        } catch (Exception ex) {
            kernel.out(
                "</FONT COLOR=#FF0000><B>SaveBackupCopy:</B> exception serialize data</FONT>");
        }

        try {
            os.close();
        } catch (Exception ex) {
            kernel.out(
                "</FONT COLOR=#FF0000><B>SaveBackupCopy:</B> exception closing " +
                name + "</FONT>");
        }

        // kernel.out("<FONT COLOR=#008000><B>BackUp done in "+name+"<B></FONT>");
    }
}
