package org.jscience.ml.sbml;

/**
 * This class declares a variable for use in MathML structures. This code
 * is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for
 * more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class Parameter extends SBaseId {
    /** DOCUMENT ME! */
    private boolean constant = true;

    /** DOCUMENT ME! */
    private double value;

    /** DOCUMENT ME! */
    private String units;

/**
     * Creates a new Parameter object.
     *
     * @param id    DOCUMENT ME!
     * @param name  DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public Parameter(String id, String name, double value) {
        super(id, name);
        setValue(value);
    }

/**
     * Creates a new Parameter object.
     *
     * @param id   DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public Parameter(String id, String name) {
        this(id, name, Double.NaN);
    }

/**
     * Creates a new instance of Parameter
     */
    public Parameter() {
        this(null, null, Double.NaN);
    }

    /**
     * Getter for property units.
     *
     * @return Value of property units.
     */
    public String getUnits() {
        return units;
    }

    /**
     * Getter for property value.
     *
     * @return Value of property value.
     */
    public double getValue() {
        return value;
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
     * Sets whether the parameter's value is constant throughout a
     * simulation.
     *
     * @param constant New value of property constant.
     */
    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    /**
     * Setter for property units.
     *
     * @param units New value of property units.
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * Setter for property value.
     *
     * @param value New value of property value.
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */
    public String toString() {
        StringBuffer s = new StringBuffer("<parameter id=\"" + id + "\"");

        if (!Double.isNaN(value)) {
            s.append(" value=\"" + value + "\"");
        }

        if (constant != true) {
            s.append(" constant=\"" + constant + "\"");
        }

        if (name != null) {
            s.append(" name=\"" + name + "\"");
        }

        if ((units != null) && (units.length() != 0)) {
            s.append(" units=\"" + units + "\"");
        }

        printShortForm(s, "</parameter>");

        return s.toString();
    }
}
