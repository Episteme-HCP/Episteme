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
