/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.loaders.pmml;

import java.util.ArrayList;
import java.util.List;

public class PMMLMiningSchema {
    private final List<PMMLMiningField> miningFields = new ArrayList<>();
    public List<PMMLMiningField> getMiningFields() { return miningFields; }
    public void addMiningField(PMMLMiningField f) { miningFields.add(f); }
}

class PMMLMiningField {
    private String name;
    private String usageType;

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getUsageType() { return usageType; }
    public void setUsageType(String t) { this.usageType = t; }
}

