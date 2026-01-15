package org.jscience.ml.sbml;

/**
 * A class defining a reference to a {@link Species} for use in a {@link
 * Reaction}. This code is licensed under the DARPA BioCOMP Open Source
 * License.  See LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class SpeciesReference extends SBase {
    /** DOCUMENT ME! */
    private double stoichiometry;

    /** DOCUMENT ME! */
    private StoichiometryMath stoichiometryMath;

    /** DOCUMENT ME! */
    private String species;

/**
     * Creates a new instance of SpeciesReference
     */
    public SpeciesReference() {
        this(null);
    }

/**
     * Creates a new SpeciesReference object.
     *
     * @param species DOCUMENT ME!
     */
    public SpeciesReference(Species species) {
        setSpecies(species);
        setStoichiometry(1.0);
    }

    /**
     * Getter for property species.
     *
     * @return Value of property species.
     */
    public String getSpecies() {
        return species;
    }

    /**
     * Getter for property stoichiometry.
     *
     * @return Value of property stoichiometry.
     */
    public String getStoichiometry() {
        return Double.isNaN(stoichiometry) ? null : String.valueOf(stoichiometry);
    }

    /**
     * Getter for property stoichiometryMath.
     *
     * @return Value of property stoichiometryMath.
     */
    public StoichiometryMath getStoichiometryMath() {
        return stoichiometryMath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ref DOCUMENT ME!
     */
    public void setSpecies(Species ref) {
        setSpecies((ref == null) ? null : ref.getId());
    }

    /**
     * Setter for property species.
     *
     * @param species Setter for property species.
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * Sets the rational stoichiometry of a reaction.  NaN indicates
     * that the stoichiometry is not set.  If the stoichiometry is not NaN,
     * the stoichiometry math will be cleared.  Stoichiometries must be
     * non-negative.  For maximum compatibility with other software, the
     * stoichiometry should be an integer.
     *
     * @param stoichiometry Stoichiometry
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setStoichiometry(double stoichiometry) {
        if (stoichiometry < 0.0) {
            throw new IllegalArgumentException(
                "Stoichiometry must be non-negative.");
        }

        this.stoichiometry = stoichiometry;

        if (!Double.isNaN(stoichiometry)) {
            setStoichiometryMath(null);
        }
    }

    /**
     * Sets the stoichiometry of a reaction.  If the stoichiometry math
     * is not null, the rational stoichiometry will be cleared.  For maximum
     * compatibility with other software, use a rational stoichiometry instead
     * of stoichiometry math.
     *
     * @param stoichiometryMath Stoichiometry math
     */
    public void setStoichiometryMath(StoichiometryMath stoichiometryMath) {
        this.stoichiometryMath = stoichiometryMath;

        if (stoichiometryMath != null) {
            stoichiometry = Double.NaN;
        }
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */
    public String toString() {
        StringBuffer s = new StringBuffer("<speciesReference species=\"" +
                species + "\"");

        if (stoichiometryMath != null) {
            s.append(">\n");
            s.append(stoichiometryMath.toString());
            s.append(super.toString());
            s.append("</speciesReference>\n");

            return s.toString();
        }

        if (!Double.isNaN(stoichiometry) && (stoichiometry != 1.0)) {
            s.append(" stoichiometry=\"" + stoichiometry + "\"");
        }

        printShortForm(s, "</speciesReference>");

        return s.toString();
    }
}
