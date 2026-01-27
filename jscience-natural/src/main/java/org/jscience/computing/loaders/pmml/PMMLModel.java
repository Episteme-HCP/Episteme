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

package org.jscience.computing.loaders.pmml;

import java.util.*;

/** PMML model container.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PMMLModel {
    private String version;
    private String copyright;
    private String description;
    private String applicationName;
    private String applicationVersion;
    private final List<DataField> dataFields = new ArrayList<>();
    private final List<Model> models = new ArrayList<>();

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
    
    public void addDataField(DataField f) { if (f != null) dataFields.add(f); }
    public List<DataField> getDataFields() { return Collections.unmodifiableList(dataFields); }
    
    public void addModel(Model m) { if (m != null) models.add(m); }
    public List<Model> getModels() { return Collections.unmodifiableList(models); }
    
    public Model getFirstModel() {
        return models.isEmpty() ? null : models.get(0);
    }
    
    @Override
    public String toString() {
        return "PMMLModel{version=" + version + ", models=" + models.size() + "}";
    }
}
