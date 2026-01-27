/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.test;

import org.jscience.biology.BiologicalSex;

import org.jscience.biology.VirusGenomeType;
import org.jscience.biology.VirusMorphology;
import org.jscience.earth.PlaceType;

/**
 * Test class to verify ExtensibleEnum functionality.
 */
public class ExtensibleEnumTest {

    public static void main(String[] args) {
        testBiologicalSex();
        testVirusEnums();
        testPlaceType();
        System.out.println("ExtensibleEnum Tests Passed.");
    }

    private static void testBiologicalSex() {
        if (BiologicalSex.values().length < 6) throw new AssertionError("BiologicalSex missing built-ins");
        if (BiologicalSex.MALE != BiologicalSex.valueOf("MALE")) throw new AssertionError("BiologicalSex lookup failed");
        
        BiologicalSex custom = new BiologicalSex("ALIEN");
        if (BiologicalSex.valueOf("ALIEN") != custom) throw new AssertionError("BiologicalSex custom registration failed");
        if (custom.isBuiltIn()) throw new AssertionError("Custom BiologicalSex should not be built-in");
        if (!BiologicalSex.MALE.isBuiltIn()) throw new AssertionError("MALE should be built-in");
    }

    private static void testVirusEnums() {
        if (VirusGenomeType.values().length < 7) throw new AssertionError("VirusGenomeType missing built-ins");
        if (VirusMorphology.values().length < 4) throw new AssertionError("VirusMorphology missing built-ins");
    }
    
    private static void testPlaceType() {
        if (PlaceType.values().length < 20) throw new AssertionError("PlaceType missing built-ins");
    }
}
