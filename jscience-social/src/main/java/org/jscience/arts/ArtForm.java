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

package org.jscience.arts;

/**
 * Enumeration of the different forms of art, categorized by traditional 
 * and modern classifications. Includes the "Seven Arts" as well as modern 
 * digital and interactive media.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public enum ArtForm {

    /** Architecture (1st Art). */
    ARCHITECTURE(1),
    
    /** Sculpture (2nd Art). */
    SCULPTURE(2),
    
    /** Painting (3rd Art). */
    PAINTING(3),
    
    /** Music (4th Art). */
    MUSIC(4),
    
    /** Poetry and Literature (5th Art). */
    POETRY(5),
    
    /** Performing Arts and Dance (6th Art). */
    DANCE(6),
    
    /** Cinema (7th Art). */
    CINEMA(7),

    /** Photography (8th Art). */
    PHOTOGRAPHY(8),
    
    /** Comics and Sequential Art (9th Art). */
    COMICS(9),
    
    /** Television and Broadcast Media. */
    TELEVISION(10),
    
    /** Radio and Acoustic Arts. */
    RADIO(11),
    
    /** Interactive Media and Video games. */
    GAMING(12),
    
    /** Handicrafts and Applied Arts. */
    CRAFTING(13),
    
    /** Culinary Arts and Gastronomy. */
    GASTRONOMY(14),
    
    /** Theater and Dramatic Arts. */
    THEATER(15),
    
    /** Digital and Generative Art. */
    DIGITAL_ART(16),
    
    /** Performance and Conceptual Art. */
    PERFORMANCE_ART(17);

    private final int value;

    ArtForm(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value associated with this art form.
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the ArtForm corresponding to the given integer value.
     * 
     * @param value the numeric value to find
     * @return the corresponding ArtForm, or null if not found
     */
    public static ArtForm fromValue(int value) {
        for (ArtForm form : ArtForm.values()) {
            if (form.value == value) {
                return form;
            }
        }
        return null;
    }
}
