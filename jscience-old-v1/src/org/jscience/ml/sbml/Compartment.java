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
