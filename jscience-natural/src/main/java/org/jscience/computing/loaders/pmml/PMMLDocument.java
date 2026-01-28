/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.computing.loaders.pmml;

import java.util.*;

/** PMML document container. */
public class PMMLDocument {
    private String version;
    private String copyright;
    private String description;
    private String applicationName;
    private String applicationVersion;
    private PMMLDataDictionary dataDictionary = new PMMLDataDictionary();
    private final List<PMMLModel> models = new ArrayList<>();

    public String getVersion() { return version; }
    public void setVersion(String v) { this.version = v; }
    
    public String getCopyright() { return copyright; }
    public void setCopyright(String c) { this.copyright = c; }
    
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    
    public String getApplicationName() { return applicationName; }
    public void setApplicationName(String n) { this.applicationName = n; }
    
    public String getApplicationVersion() { return applicationVersion; }
    public void setApplicationVersion(String v) { this.applicationVersion = v; }
    
    public PMMLDataDictionary getDataDictionary() { return dataDictionary; }
    public void setDataDictionary(PMMLDataDictionary dict) { this.dataDictionary = dict; }
    
    public void addModel(PMMLModel m) { if (m != null) models.add(m); }
    public List<PMMLModel> getModels() { return Collections.unmodifiableList(models); }
    
    public PMMLModel getModel() {
        return models.isEmpty() ? null : models.get(0);
    }
    
    @Override
    public String toString() {
        return "PMMLDocument{version=" + version + ", models=" + models.size() + "}";
    }
}
