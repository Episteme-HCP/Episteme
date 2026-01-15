package org.jscience.ml.sbml;

/**
 * This class represents an entity that takes part in {@link Reaction}s.
 * This code is licensed under the DARPA BioCOMP Open Source License.  See
 * LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class Species extends SBaseId {
    /** DOCUMENT ME! */
    private boolean boundaryCondition;

    /** DOCUMENT ME! */
    private boolean constant;

    /** DOCUMENT ME! */
    private boolean hasOnlySubstanceUnits;

    /** DOCUMENT ME! */
    private double initialAmount = Double.NaN;

    /** DOCUMENT ME! */
    private double initialConcentration = Double.NaN;

    /** DOCUMENT ME! */
    private String charge;

    /** DOCUMENT ME! */
    private String compartment;

    /** DOCUMENT ME! */
    private String spatialSizeUnits;

    /** DOCUMENT ME! */
    private String substanceUnits;

/**
     * Creates a new Species object.
     *
     * @param id   DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public Species(String id, String name) {
        super(id, name);
    }

/**
     * Creates a new instance of Species
     */
    public Species() {
        super();
    }

    /**
     * Getter for property charge.
     *
     * @return Value of property charge.
     */
    public String getCharge() {
        return charge;
    }

    /**
     * Getter for property compartment.
     *
     * @return Value of property compartment.
     */
    public String getCompartment() {
        return compartment;
    }

    /**
     * Getter for property hasOnlySubstanceUnits.
     *
     * @return Value of property hasOnlySubstanceUnits.
     */
    public boolean getHasOnlySubstanceUnits() {
        return hasOnlySubstanceUnits;
    }

    /**
     * Getter for property initialAmount.
     *
     * @return Value of property initialAmount.
     */
    public double getInitialAmount() {
        return initialAmount;
    }

    /**
     * Getter for property initialConcentration.
     *
     * @return Value of property initialConcentration.
     */
    public double getInitialConcentration() {
        return initialConcentration;
    }

    /**
     * Getter for property spatialSizeUnits.
     *
     * @return Value of property spatialSizeUnits.
     */
    public String getSpatialSizeUnits() {
        return spatialSizeUnits;
    }

    /**
     * Getter for property substanceUnits.
     *
     * @return Value of property substanceUnits.
     */
    public String getSubstanceUnits() {
        return substanceUnits;
    }

    /**
     * Getter for property boundaryCondition.
     *
     * @return Value of property boundaryCondition.
     */
    public boolean isBoundaryCondition() {
        return boundaryCondition;
    }

    /**
     * Getter for property constant.
     *
     * @return Value of property constant.
     */
    public boolean isConstant() {
        return constant;
    }

    /**
     * Sets whether the species is on the boundary of the reaction
     * system.
     *
     * @param boundaryCondition New value of property boundaryCondition.
     */
    public void setBoundaryCondition(boolean boundaryCondition) {
        this.boundaryCondition = boundaryCondition;
    }

    /**
     * Setter for property charge.
     *
     * @param charge New value of property charge.
     */
    public void setCharge(String charge) {
        this.charge = charge;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ref DOCUMENT ME!
     */
    public void setCompartment(Compartment ref) {
        compartment = ref.getId();
    }

    /**
     * Setter for property compartment.
     *
     * @param compartment New value of property compartment.
     */
    public void setCompartment(String compartment) {
        this.compartment = compartment;
    }

    /**
     * Setter for property constant.
     *
     * @param constant New value of property constant.
     */
    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    /**
     * Setter for property hasOnlySubstanceUnits.
     *
     * @param hasOnlySubstanceUnits New value of property
     *        hasOnlySubstanceUnits.
     */
    public void setHasOnlySubstanceUnits(boolean hasOnlySubstanceUnits) {
        this.hasOnlySubstanceUnits = hasOnlySubstanceUnits;
    }

    /**
     * Setter for property initialAmount.
     *
     * @param initialAmount New value of property initialAmount.
     */
    public void setInitialAmount(double initialAmount) {
        this.initialAmount = initialAmount;
    }

    /**
     * Setter for property initialConcentration.
     *
     * @param initialConcentration New value of property initialConcentration.
     */
    public void setInitialConcentration(double initialConcentration) {
        this.initialConcentration = initialConcentration;
    }

    /**
     * Setter for property spatialSizeUnits.
     *
     * @param spatialSizeUnits New value of property spatialSizeUnits.
     */
    public void setSpatialSizeUnits(String spatialSizeUnits) {
        this.spatialSizeUnits = spatialSizeUnits;
    }

    /**
     * Setter for property substanceUnits.
     *
     * @param substanceUnits New value of property substanceUnits.
     */
    public void setSubstanceUnits(String substanceUnits) {
        this.substanceUnits = substanceUnits;
    }

    /**
     * Getter for property units.
     *
     * @return Value of property units.
     */
    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */
    public String toString() {
        StringBuffer s = new StringBuffer("<species id=\"" + id +
                "\" compartment=\"" + compartment + "\"");

        if (Double.isNaN(initialAmount) && Double.isNaN(initialConcentration)) {
            ;
        } else if (!Double.isNaN(initialAmount)) {
            s.append(" initialAmount=\"" + initialAmount + "\"");
        } else if (!Double.isNaN(initialConcentration)) {
            s.append(" initialConcentration=\"" + initialConcentration + "\"");
        }

        if (boundaryCondition) {
            s.append(" boundaryCondition=\"" + boundaryCondition + "\"");
        }

        if (constant) {
            s.append(" constant=\"" + constant + "\"");
        }

        if (name != null) {
            s.append(" name=\"" + name + "\"");
        }

        if (hasOnlySubstanceUnits) {
            s.append(" hasOnlySubstanceUnits=\"" + hasOnlySubstanceUnits +
                "\"");
        }

        if ((spatialSizeUnits != null) && (spatialSizeUnits.length() != 0)) {
            s.append(" spatialSizeUnits=\"" + spatialSizeUnits + "\"");
        }

        if ((substanceUnits != null) && (substanceUnits.length() != 0)) {
            s.append(" substanceUnits=\"" + substanceUnits + "\"");
        }

        if (charge != null) {
            s.append(" charge=\"" + charge + "\"");
        }

        printShortForm(s, "</species>");

        return s.toString();
    }
}
