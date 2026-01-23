/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.computing.loaders.pmml;

import java.util.*;

/** Data field in PMML data dictionary. */
public class DataField {
    private String name;
    private String optype;
    private String dataType;
    private final List<String> values = new ArrayList<>();

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    
    public String getOptype() { return optype; }
    public void setOptype(String o) { this.optype = o; }
    
    public String getDataType() { return dataType; }
    public void setDataType(String t) { this.dataType = t; }
    
    public void addValue(String v) { if (v != null) values.add(v); }
    public List<String> getValues() { return Collections.unmodifiableList(values); }
    
    public boolean isCategorical() { return "categorical".equals(optype); }
    public boolean isContinuous() { return "continuous".equals(optype); }
    
    @Override
    public String toString() { return name + " (" + dataType + ", " + optype + ")"; }
}
