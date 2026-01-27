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

package org.jscience.biology.loaders.biopax;

import java.util.*;

/** Molecular interaction (reaction, catalysis).
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Interaction {
    private String rdfId;
    private String type;
    private String displayName;
    private final List<String> leftRefs = new ArrayList<>();
    private final List<String> rightRefs = new ArrayList<>();
    private String controllerRef;
    private String controlledRef;

    public String getRdfId() { return rdfId; }
    public void setRdfId(String id) { this.rdfId = id; }
    
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String n) { this.displayName = n; }
    
    public void addLeftRef(String ref) { leftRefs.add(ref); }
    public List<String> getLeftRefs() { return Collections.unmodifiableList(leftRefs); }
    
    public void addRightRef(String ref) { rightRefs.add(ref); }
    public List<String> getRightRefs() { return Collections.unmodifiableList(rightRefs); }
    
    public String getControllerRef() { return controllerRef; }
    public void setControllerRef(String ref) { this.controllerRef = ref; }
    
    public String getControlledRef() { return controlledRef; }
    public void setControlledRef(String ref) { this.controlledRef = ref; }
    
    @Override
    public String toString() { return type + "{" + rdfId + "}"; }
}
