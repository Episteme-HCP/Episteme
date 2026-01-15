/*
 * SCFMethod.java
 *
 * Created on August 5, 2004, 10:55 PM
 */
package org.jscience.chemistry.quantum;

import org.jscience.chemistry.quantum.event.SCFEvent;
import org.jscience.chemistry.quantum.event.SCFEventListener;
import org.jscience.chemistry.quantum.math.matrix.Matrix;


/**
 * An abstract class representing the Self Consistant Field (SCF) method
 * like Hartree-Fock, MP2 etc.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public abstract class SCFMethod {
    // default constants
    /** DOCUMENT ME! */
    private static final int MAX_ITERATION = 20;

    /** DOCUMENT ME! */
    private static final double ENERGY_TOLERANCE = 1.0e-4;

    /** DOCUMENT ME! */
    private static final double DENSITY_TOLERANCE = 1.0e-4;

    /** Holds value of property energyTolerance. */
    protected double energyTolerance;

    /** Holds value of property densityTolerance. */
    protected double densityTolerance;

    /** Holds value of property maxIteration. */
    protected int maxIteration;

    /** The molecule under consideration */
    protected Molecule molecule;

    /** The one electron integrals of the system */
    protected OneElectronIntegrals oneEI;

    /** The two electron integrals of the system */
    protected TwoElectronIntegrals twoEI;

    /** Holds value of property density - the Desity Matrix. */
    protected Matrix density;

    /** Holds value of property mos - Molecular Orbitals. */
    protected Matrix mos;

    /** Holds value of property orbE - orbital eigen values. */
    protected double[] orbE;

    /** Holds value of property scfIteration. */
    protected int scfIteration;

    /** Holds value of property densityGuesser. */
    protected DensityGuesser densityGuesser;

    /** Holds value of property guessInitialDM. */
    protected boolean guessInitialDM;

    /** Holds value of property fock - the Fock matrix. */
    protected Matrix fock;

    /** Utility field used by event firing mechanism. */
    private javax.swing.event.EventListenerList listenerList = null;

/**
     * Creates a new instance of SCFMethod
     *
     * @param molecule DOCUMENT ME!
     * @param oneEI    DOCUMENT ME!
     * @param twoEI    DOCUMENT ME!
     */
    public SCFMethod(Molecule molecule, OneElectronIntegrals oneEI,
        TwoElectronIntegrals twoEI) {
        maxIteration = MAX_ITERATION;

        energyTolerance = ENERGY_TOLERANCE;
        densityTolerance = DENSITY_TOLERANCE;

        this.molecule = molecule;
        this.oneEI = oneEI;
        this.twoEI = twoEI;

        guessInitialDM = false;
    }

    /**
     * Perform the SCF
     */
    public abstract void scf();

    /**
     * compute nuclear repulsion energy
     *
     * @return the nuclear repulsion energy
     */
    public double nuclearEnergy() {
        double eNuke = 0.0;
        int i;
        int j;
        int noOfAtoms = molecule.getNumberOfAtoms();

        Atom atomI;
        Atom atomJ;

        // read in the atomic numbers
        int[] atomicNumbers = new int[noOfAtoms];
        AtomInfo ai = AtomInfo.getInstance();

        for (i = 0; i < noOfAtoms; i++) {
            atomicNumbers[i] = ai.getAtomicNumber(molecule.getAtom(i).getSymbol());
        } // end for

        // and compute nuclear energy
        for (i = 0; i < noOfAtoms; i++) {
            atomI = (Atom) molecule.getAtom(i);

            for (j = 0; j < i; j++) {
                atomJ = (Atom) molecule.getAtom(j);

                eNuke += ((atomicNumbers[i] * atomicNumbers[j]) / atomI.distanceFrom(atomJ));
            } // end for
        } // end for

        return eNuke;
    }

    /**
     * Getter for property energyTolerance.
     *
     * @return Value of property energyTolerance.
     */
    public double getEnergyTolerance() {
        return this.energyTolerance;
    }

    /**
     * Setter for property energyTolerance.
     *
     * @param energyTolerance New value of property energyTolerance.
     */
    public void setEnergyTolerance(double energyTolerance) {
        this.energyTolerance = energyTolerance;
    }

    /**
     * Getter for property densityTolerance.
     *
     * @return Value of property densityTolerance.
     */
    public double getDensityTolerance() {
        return this.densityTolerance;
    }

    /**
     * Setter for property densityTolerance.
     *
     * @param densityTolerance New value of property densityTolerance.
     */
    public void setDensityTolerance(double densityTolerance) {
        this.densityTolerance = densityTolerance;
    }

    /**
     * Getter for property maxIteration.
     *
     * @return Value of property maxIteration.
     */
    public int getMaxIteration() {
        return this.maxIteration;
    }

    /**
     * Setter for property maxIteration.
     *
     * @param maxIteration New value of property maxIteration.
     */
    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    /**
     * Getter for property density.
     *
     * @return Value of property density.
     */
    public Matrix getDensity() {
        return this.density;
    }

    /**
     * Getter for property mos.
     *
     * @return Value of property mos.
     */
    public Matrix getMos() {
        return this.mos;
    }

    /**
     * Getter for property orbE.
     *
     * @return Value of property orbE.
     */
    public double[] getOrbE() {
        return this.orbE;
    }

    /**
     * Getter for property scfIteration.
     *
     * @return Value of property scfIteration.
     */
    public int getScfIteration() {
        return this.scfIteration;
    }

    /**
     * Getter for property molecule.
     *
     * @return Value of property molecule.
     */
    public Molecule getMolecule() {
        return this.molecule;
    }

    /**
     * Getter for property oneEI.
     *
     * @return Value of property oneEI.
     */
    public OneElectronIntegrals getOneEI() {
        return this.oneEI;
    }

    /**
     * Getter for property twoEI.
     *
     * @return Value of property twoEI.
     */
    public TwoElectronIntegrals getTwoEI() {
        return this.twoEI;
    }

    /**
     * Getter for property densityGuesser.
     *
     * @return Value of property densityGuesser.
     */
    public DensityGuesser getDensityGuesser() {
        return this.densityGuesser;
    }

    /**
     * Setter for property densityGuesser.
     *
     * @param densityGuesser New value of property densityGuesser.
     */
    public void setDensityGuesser(DensityGuesser densityGuesser) {
        this.densityGuesser = densityGuesser;
    }

    /**
     * Getter for property guessInitialDM.
     *
     * @return Value of property guessInitialDM.
     */
    public boolean isGuessInitialDM() {
        return this.guessInitialDM;
    }

    /**
     * Setter for property guessInitialDM.
     *
     * @param guessInitialDM New value of property guessInitialDM.
     */
    public void setGuessInitialDM(boolean guessInitialDM) {
        this.guessInitialDM = guessInitialDM;
    }

    /**
     * Getter for property fock.
     *
     * @return Value of property fock.
     */
    public Matrix getFock() {
        return this.fock;
    }

    /**
     * Registers SCFEventListener to receive events.
     *
     * @param listener The listener to register.
     */
    public synchronized void addSCFEventListener(SCFEventListener listener) {
        if (listenerList == null) {
            listenerList = new javax.swing.event.EventListenerList();
        }

        listenerList.add(SCFEventListener.class, listener);
    }

    /**
     * Removes SCFEventListener from the list of listeners.
     *
     * @param listener The listener to remove.
     */
    public synchronized void removeSCFEventListener(SCFEventListener listener) {
        listenerList.remove(SCFEventListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    protected void fireSCFEventListenerScfEventOccured(SCFEvent event) {
        if (listenerList == null) {
            return;
        }

        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SCFEventListener.class) {
                ((SCFEventListener) listeners[i + 1]).scfEventOccured(event);
            }
        }
    }
} // end of class SCFMethod
