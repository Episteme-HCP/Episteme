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
 * Test.java
 *
 * Created on August 30, 2004, 12:30 PM
 */
package org.jscience.chemistry.quantum;

import org.jscience.chemistry.quantum.event.SCFEvent;
import org.jscience.chemistry.quantum.event.SCFEventListener;


/**
 * Test suite
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class Test {
    /** DOCUMENT ME! */
    private static long t1;

    /** DOCUMENT ME! */
    private static long t2;

    /** DOCUMENT ME! */
    private static java.text.DecimalFormat df;

/**
     * Creates a new instance of Test
     */
    public Test() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        t1 = System.currentTimeMillis();

        Molecule mol = new Molecule();
        mol.addAtom("H", 0.752510, -0.454585, 0.000000);
        mol.addAtom("O", 0.000000, 0.113671, 0.000000);
        mol.addAtom("H", -0.752510, -0.454585, 0.000000);

        BasisFunctions bfs = new BasisFunctions(mol, "sto3g");
        OneElectronIntegrals onee = new OneElectronIntegrals(bfs, mol);
        TwoElectronIntegrals twoe = new TwoElectronIntegrals(bfs);

        HartreeFockSCFMethod scfm = new HartreeFockSCFMethod(mol, onee, twoe);
        df = new java.text.DecimalFormat("#.########");
        df.setMinimumFractionDigits(9);

        scfm.addSCFEventListener(new SCFEventListener() {
                public void scfEventOccured(SCFEvent scfEvent) {
                    if (scfEvent.getType() == SCFEvent.CONVERGED_EVENT) {
                        System.out.println("Energy Converged");
                        t2 = System.currentTimeMillis();
                        System.out.println("Final Energy = " +
                            df.format(scfEvent.getCurrentEnergy()) + " a.u.");
                        System.out.println("Total Time : " + (t2 - t1) + " ms");

                        return;
                    } else if (scfEvent.getType() == SCFEvent.FAILED_CONVERGENCE_EVENT) {
                        System.out.println("SCF failed to converge");
                        t2 = System.currentTimeMillis();
                        System.out.println("Final Energy = " +
                            df.format(scfEvent.getCurrentEnergy()) + " a.u.");
                        System.out.println("Total Time : " + (t2 - t1) + " ms");

                        return;
                    } // end if

                    System.out.println(scfEvent.getCurrentIteration() + " " +
                        df.format(scfEvent.getCurrentEnergy()));
                }
            });

        scfm.scf();
    }
}
