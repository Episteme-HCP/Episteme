/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.loaders.pmml;

import java.util.ArrayList;
import java.util.List;

public class PMMLDataDictionary {
    private final List<PMMLDataField> dataFields = new ArrayList<>();
    public List<PMMLDataField> getDataFields() { return dataFields; }
    public void addDataField(PMMLDataField f) { dataFields.add(f); }
}

class PMMLDataField {
    private String name;
    private String dataType;
    private String opType;

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getDataType() { return dataType; }
    public void setDataType(String t) { this.dataType = t; }
    public String getOpType() { return opType; }
    public void setOpType(String t) { this.opType = t; }
}

