/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.chemistry.loaders.cml;

/**
 * DTO for CML Atom.
 */
public class CMLAtom {
    private String id;
    private String elementType;
    private Double x3;
    private Double y3;
    private Double z3;
    private Double formalCharge;
    private String isotope;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getElementType() { return elementType; }
    public void setElementType(String elementType) { this.elementType = elementType; }
    public Double getX3() { return x3; }
    public void setX3(Double x3) { this.x3 = x3; }
    public Double getY3() { return y3; }
    public void setY3(Double y3) { this.y3 = y3; }
    public Double getZ3() { return z3; }
    public void setZ3(Double z3) { this.z3 = z3; }
    public Double getFormalCharge() { return formalCharge; }
    public void setFormalCharge(Double formalCharge) { this.formalCharge = formalCharge; }
    public String getIsotope() { return isotope; }
    public void setIsotope(String isotope) { this.isotope = isotope; }
}

