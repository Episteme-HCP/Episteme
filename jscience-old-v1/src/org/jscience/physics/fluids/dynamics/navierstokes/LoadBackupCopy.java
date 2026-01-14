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

import java.io.FileInputStream;
import java.io.ObjectInputStream;


/**
 * DOCUMENT ME!
 *
 * @author balrog
 */
public class LoadBackupCopy {
    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private ObjectInputStream is;

    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private double[][] u0;

    /** DOCUMENT ME! */
    private double[][] u00;

    /** DOCUMENT ME! */
    private double[][] u000;

    /** DOCUMENT ME! */
    private double[] p;

    /** DOCUMENT ME! */
    private double[] ink;

    /** DOCUMENT ME! */
    private boolean ok;

/**
     * Creates new CopiaRespaldo
     *
     * @param kadfc DOCUMENT ME!
     */
    public LoadBackupCopy(KernelADFC kadfc) {
        kernel = kadfc;

        name = kernel.getConfiguration().meshName + ".backup";
        ok = false;
    }

    /**
     * DOCUMENT ME!
     */
    public void loadBackup() {
        try {
            is = new ObjectInputStream(new FileInputStream(name));
        } catch (Exception ex) {
            kernel.out(
                "</FONT COLOR=#FF0000><B>LoadBackupCopy:</B> exception al open " +
                name + "</FONT>");

            return;
        }

        if (kernel.continueSerializedSimulation()) {
            try {
                u0 = (double[][]) is.readObject();
                u00 = (double[][]) is.readObject();
                u000 = (double[][]) is.readObject();
                p = (double[]) is.readObject();
                ink = (double[]) is.readObject();
            } catch (Exception ex) {
                kernel.out(
                    "</FONT COLOR=#FF0000><B>LoadBackupCopy:</B> exception al deserialize data</FONT>");

                return;
            }

            try {
                is.close();
            } catch (Exception ex) {
                kernel.out(
                    "</FONT COLOR=#FF0000><B>LoadBackupCopy:</B> exception al cerrar " +
                    name + "</FONT>");

                return;
            }

            kernel.out("<FONT COLOR=#008000><B>Continuing BackUp done in " +
                name + "<B></FONT>");
            ok = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[][] getU0() {
        return u0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[][] getU00() {
        return u00;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[][] getU000() {
        return u000;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getP() {
        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getInk() {
        return ink;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOk() {
        return ok;
    }
}
