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

package org.jscience.biology.loaders.pdbml;

/** Polymer entity (protein, nucleic acid).
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PolymerEntity {
    private String entityId;
    private String type;
    private String sequence;
    private String sequenceCanonical;

    public String getEntityId() { return entityId; }
    public void setEntityId(String id) { this.entityId = id; }
    
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    
    public String getSequence() { return sequence; }
    public void setSequence(String s) { this.sequence = s; }
    
    public String getSequenceCanonical() { return sequenceCanonical; }
    public void setSequenceCanonical(String s) { this.sequenceCanonical = s; }
    
    public int getSequenceLength() {
        return sequenceCanonical != null ? sequenceCanonical.replaceAll("\\s", "").length() : 0;
    }
    
    public boolean isProtein() {
        return type != null && type.toLowerCase().contains("polypeptide");
    }
    
    public boolean isNucleicAcid() {
        return type != null && (type.toLowerCase().contains("dna") || 
                                type.toLowerCase().contains("rna"));
    }
}
