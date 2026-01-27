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

package org.jscience.test;

import org.jscience.biology.BiologicalSex;

import org.jscience.biology.VirusGenomeType;
import org.jscience.biology.VirusMorphology;
import org.jscience.earth.PlaceType;

/**
 * Test class to verify ExtensibleEnum functionality.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
