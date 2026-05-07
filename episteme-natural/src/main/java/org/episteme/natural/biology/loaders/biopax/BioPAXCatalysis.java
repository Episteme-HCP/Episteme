/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.loaders.biopax;

/** 
 * BioPAX Catalysis DTO.
 */
public class BioPAXCatalysis extends BioPAXInteraction {
    private BioPAXPhysicalEntity controller;

    public BioPAXPhysicalEntity getController() { return controller; }
    public void setController(BioPAXPhysicalEntity c) { this.controller = c; }
}

