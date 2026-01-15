package org.jscience.biology;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Molecule;

import org.jscience.util.Named;


/**
 * A class representing an Amino-Acid.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class AminoAcid extends Molecule implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String abbreviation;

    /** DOCUMENT ME! */
    private String symbol;

    /** DOCUMENT ME! */
    private double isoelectricPoint;

    /** DOCUMENT ME! */
    private double hydrophobicity;

/**
     * Creates a new AminoAcid object.
     *
     * @param atom DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public AminoAcid(Atom atom, String name) {
        super(atom);

        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException(
                "AminoAcid constructor doesn't accept null or empty arguments.");
        }
    }

/**
     * Creates a new AminoAcid object.
     *
     * @param atom         DOCUMENT ME!
     * @param name         DOCUMENT ME!
     * @param abbreviation DOCUMENT ME!
     * @param symbol       DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AminoAcid(Atom atom, String name, String abbreviation, String symbol) {
        super(atom);

        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.abbreviation = abbreviation;
            this.symbol = symbol;
        } else {
            throw new IllegalArgumentException(
                "AminoAcid constructor doesn't accept null or empty name.");
        }
    }

    /**
     * Returns the name.
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the abbreviation.
     *
     * @param name DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    protected void setName(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null or empty name.");
        }
    }

    /**
     * Returns the abbreviation.
     *
     * @return DOCUMENT ME!
     */

    //may retrun null
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Sets the abbreviation.
     *
     * @param abbreviation DOCUMENT ME!
     */
    protected void setAbbreviation(String abbreviation) {
        //if ((abbreviation != null) && (abbreviation.length() > 0)) {
        this.abbreviation = abbreviation;

        //} else {
        //  throw new IllegalArgumentException("You can't set a null or empty abbreviation.");
        //}
    }

    /**
     * Returns the symbol.
     *
     * @return DOCUMENT ME!
     */

    //may retrun null
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol.
     *
     * @param symbol DOCUMENT ME!
     */
    protected void setSymbol(String symbol) {
        //if ((symbol != null) && (symbol.length() > 0)) {
        this.symbol = symbol;

        //} else {
        //  throw new IllegalArgumentException("You can't set a null or empty symbol.");
        //}
    }

    /**
     * Returns the isoelectric point.
     *
     * @return DOCUMENT ME!
     */
    public double getIsoelectricPoint() {
        return isoelectricPoint;
    }

    /**
     * Sets the isoelectric point.
     *
     * @param isoelectricPoint DOCUMENT ME!
     */
    protected void setIsoelectricPoint(double isoelectricPoint) {
        this.isoelectricPoint = isoelectricPoint;
    }

    /**
     * Returns the hydrophobiticy.
     *
     * @return DOCUMENT ME!
     */
    public double getHydrophobicity() {
        return hydrophobicity;
    }

    /**
     * Sets the hydrophobiticy.
     *
     * @param hydrophobicity DOCUMENT ME!
     */
    protected void setHydrophobicity(double hydrophobicity) {
        this.hydrophobicity = hydrophobicity;
    }
}
