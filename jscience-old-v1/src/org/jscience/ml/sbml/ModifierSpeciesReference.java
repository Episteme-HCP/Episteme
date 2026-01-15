package org.jscience.ml.sbml;

/**
 * This class defines a modifier that takes part in a {@link Reaction}.
 * This code is licensed under the DARPA BioCOMP Open Source License.  See
 * LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class ModifierSpeciesReference extends SBase {
    /** DOCUMENT ME! */
    private String species;

/**
     * Creates a new instance of ModifierSpeciesReference
     */
    public ModifierSpeciesReference() {
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
     * Sets the name of the species to reference.
     *
     * @param species New value of property species.
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */
    public String toString() {
        StringBuffer s = new StringBuffer(
                "<modifierSpeciesReference species=\"" + species + "\"");
        printShortForm(s, "</modifierSpeciesReference>");

        return s.toString();
    }
}
