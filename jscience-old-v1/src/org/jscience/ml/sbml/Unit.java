package org.jscience.ml.sbml;

/**
 * Units of measurement for quantities in an SBML model.  A unit is defined by unit = (multiplier * 10^scale * kind^exponent) + offset.
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 *
 * @author Nicholas Allen
 */

public final class Unit extends SBase {
    private double multiplier;
    private double offset;
    private int exponent;
    private int scale;
    private String name;
    private Unit kind;

    /**
     * Creates a new unit.  unit = kind.
     */

    public Unit(Unit kind) {
        this(kind, 1.0, 0, 1, 0.0);
    }

    /**
     * Creates a new unit.  unit = (multiplier * 10^scale * kind^exponent) + offset
     */

    public Unit(Unit kind, double multiplier, int scale, int exponent, double offset) {
        super();
        setKind(kind);
        setMultiplier(multiplier);
        setScale(scale);
        setExponent(exponent);
        setOffset(offset);
    }

    public int getExponent() {
        return exponent;
    }

    public Unit getKind() {
        return isBaseUnit() ? this : kind;
    }

    public String getKindName() {
        return isBaseUnit() ? name : kind.getKindName();
    }

    public double getMultiplier() {
        return multiplier;
    }

    public double getOffset() {
        return offset;
    }

    public int getScale() {
        return scale;
    }

    public boolean isBaseUnit() {
        return kind == null;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    public void setKind(Unit kind) {
        if (kind == null)
            throw new IllegalArgumentException("Must specify a unit kind");
        if (!kind.isBaseUnit())
            throw new IllegalArgumentException("Units cannot be derived from a user defined unit");
        this.kind = kind;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("<unit kind=\"" + getKindName() + "\"");
        if (exponent != 1)
            s.append(" exponent=\"" + exponent + "\"");
        if (scale != 0)
            s.append(" scale=\"" + scale + "\"");
        if (multiplier != 1.0)
            s.append(" multiplier=\"" + multiplier + "\"");
        if (offset != 0.0)
            s.append(" offset=\"" + offset + "\"");
        printShortForm(s, "</unit>");
        return s.toString();
    }

    /**
     * Creates a new base unit.
     *
     * @param name Unit name
     */

    Unit(String name) {
        super();
        this.name = name;
        setMultiplier(1.0);
        setScale(0);
        setExponent(1);
        setOffset(0.0);
    }
}
