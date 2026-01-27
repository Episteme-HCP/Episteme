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

/** Atomic coordinate record.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AtomSite {
    private int id;
    private String atomSymbol;
    private String atomName;
    private String residueName;
    private String chainId;
    private int residueSeq;
    private double x, y, z;
    private double occupancy;
    private double tempFactor;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getAtomSymbol() { return atomSymbol; }
    public void setAtomSymbol(String s) { this.atomSymbol = s; }
    
    public String getAtomName() { return atomName; }
    public void setAtomName(String n) { this.atomName = n; }
    
    public String getResidueName() { return residueName; }
    public void setResidueName(String n) { this.residueName = n; }
    
    public String getChainId() { return chainId; }
    public void setChainId(String id) { this.chainId = id; }
    
    public int getResidueSeq() { return residueSeq; }
    public void setResidueSeq(int seq) { this.residueSeq = seq; }
    
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    
    public double getZ() { return z; }
    public void setZ(double z) { this.z = z; }
    
    public double getOccupancy() { return occupancy; }
    public void setOccupancy(double o) { this.occupancy = o; }
    
    public double getTempFactor() { return tempFactor; }
    public void setTempFactor(double t) { this.tempFactor = t; }
    
    /** Calculate distance to another atom in Angstroms. */
    public double distanceTo(AtomSite other) {
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    
    /** Check if this is a backbone atom. */
    public boolean isBackbone() {
        return "CA".equals(atomName) || "C".equals(atomName) || 
               "N".equals(atomName) || "O".equals(atomName);
    }
    
    @Override
    public String toString() {
        return String.format("%s %s %s %d (%.2f, %.2f, %.2f)", 
            atomName, residueName, chainId, residueSeq, x, y, z);
    }
}
