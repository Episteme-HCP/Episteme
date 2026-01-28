/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

import java.util.*;

/** 
 * BioPAX Biochemical Reaction DTO.
 */
public class BioPAXBiochemicalReaction extends BioPAXInteraction {
    private String conversionDirection;
    private String ecNumber;
    private final List<BioPAXPhysicalEntity> left = new ArrayList<>();
    private final List<BioPAXPhysicalEntity> right = new ArrayList<>();
    private final List<BioPAXCatalysis> catalysis = new ArrayList<>();

    public String getConversionDirection() { return conversionDirection; }
    public void setConversionDirection(String d) { this.conversionDirection = d; }

    public String getEcNumber() { return ecNumber; }
    public void setEcNumber(String ec) { this.ecNumber = ec; }

    public void addLeft(BioPAXPhysicalEntity e) { left.add(e); }
    public List<BioPAXPhysicalEntity> getLeft() { return left; }

    public void addRight(BioPAXPhysicalEntity e) { right.add(e); }
    public List<BioPAXPhysicalEntity> getRight() { return right; }

    public void addCatalysis(BioPAXCatalysis c) { catalysis.add(c); }
    public List<BioPAXCatalysis> getCatalysis() { return catalysis; }
}
