/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.architecture;

/**
 * Standard definitions and classification constants used throughout the 
 * architecture package. Includes architectural historical styles and 
 * structural occupancy categories.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class ArchitectureConstants {

    /** Classical Greek and Roman architectural style. */
    public static final String STYLE_CLASSICAL = "Classical";
    
    /** Medieval European style characterized by pointed arches and ribbed vaults. */
    public static final String STYLE_GOTHIC = "Gothic";
    
    /** Early modern style reviving classical elements (14th-16th century). */
    public static final String STYLE_RENAISSANCE = "Renaissance";
    
    /** Ornate and theatrical style of the 17th and 18th centuries. */
    public static final String STYLE_BAROQUE = "Baroque";
    
    /** Mid-18th century revival of classical Greek and Roman architecture. */
    public static final String STYLE_NEOCLASSICAL = "Neoclassical";
    
    /** Ornamental style characterized by fluid, organic lines (1890-1910). */
    public static final String STYLE_ART_NOUVEAU = "Art Nouveau";
    
    /** Sleek, geometric decorative style of the 1920s and 30s. */
    public static final String STYLE_ART_DECO = "Art Deco";
    
    /** Style prioritizing function and minimalism over ornament (20th century). */
    public static final String STYLE_MODERNIST = "Modernist";
    
    /** Response to modernism, reintroducing historical reference and playfulness. */
    public static final String STYLE_POSTMODERN = "Postmodern";
    
    /** Style characterized by monolithic, concrete, and blocky appearances. */
    public static final String STYLE_BRUTALIST = "Brutalist";
    
    // Structural Occupancy Categories
    
    /** Houses, apartments, and living spaces. */
    public static final int RESIDENTIAL = 1;
    
    /** Offices, retail, and business spaces. */
    public static final int COMMERCIAL = 2;
    
    /** Factories, warehouses, and production facilities. */
    public static final int INDUSTRIAL = 3;
    
    /** Churches, temples, and places of worship. */
    public static final int RELIGIOUS = 4;
    
    /** Museums, theaters, and exhibition spaces. */
    public static final int CULTURAL = 5;
    
    /** Civic buildings and administrative centers. */
    public static final int GOVERNMENT = 6;
    
    /** Transportation and utility systems. */
    public static final int INFRASTRUCTURE = 7;
    
    private ArchitectureConstants() {}
}

