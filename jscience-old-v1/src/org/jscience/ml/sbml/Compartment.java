package org.jscience.ml.sbml;

/**
 * A bounded container in which species are located.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */

public class Compartment extends SBaseId {
    private boolean constant;
    private double size;
    private int spatialDimensions;
    private String outside;
    private String units;

    public Compartment(String id, String name) {
        super(id, name);
        constant = true;
        size = Double.NaN;
        spatialDimensions = 3;
    }

    /**
     * Creates a new instance of Compartment
     */

    public Compartment() {
        this(null, null);
    }

    /**
     * Getter for property outside.
     *
     * @return Value of property outside.
     */

    public String getOutside() {
        return outside;
    }

    /**
     * Setter for property volume.
     */

    public String getSize() {
        return spatialDimensions == 0 || Double.isNaN(size) ? null : String.valueOf(size);
    }

    /**
     * Setter for property volume.
     */

    public String getSpatialDimensions() {
        return String.valueOf(spatialDimensions);
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
     * Getter for property constant.
     *
     * @return Value of property constant.
     */

    public boolean isConstant() {
        return spatialDimensions == 0 || constant;
    }

    /**
     * Sets whether the compartment's volume may vary during simulation.
     *
     * @param constant New value of property constant.
     */

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    /**
     * Setter for property outside.
     *
     * @param outside New value of property outside.
     */

    public void setOutside(String outside) {
        this.outside = outside;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setSize(String size) {
        if (size == null) {
            setSize(Double.NaN);
            return;
        }
        try {
            setSize(Double.parseDouble(size));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Setter for property volume.
     *
     * @param volume New value of property volume.
     */

    public void setSpatialDimensions(String value) {
        if (value == null) {
            spatialDimensions = 3;
            return;
        }
        int dimensions;
        try {
            dimensions = Integer.parseInt(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Dimension must be 0, 1, 2, or 3");
        }
        if (spatialDimensions < 0 || spatialDimensions > 3)
            throw new IllegalArgumentException("Dimension must be 0, 1, 2, or 3");
        spatialDimensions = dimensions;
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
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */

    public String toString() {
        StringBuffer s = new StringBuffer("<compartment id=\"" + id + "\"");
        if (spatialDimensions != 3)
            s.append(" spatialDimensions=\"" + spatialDimensions + "\"");
        if (getSize() != null)
            s.append(" size=\"" + getSize() + "\"");
        if (name != null)
            s.append(" name=\"" + name + "\"");
        if (units != null)
            s.append(" units=\"" + units + "\"");
        if (outside != null)
            s.append(" outside=\"" + outside + "\"");
        if (!isConstant())
            s.append(" constant=\"false\"");
        printShortForm(s, "</compartment>");
        return s.toString();
    }
}
