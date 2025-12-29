/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.psychology;

import org.jscience.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PsychologyDomainTest {

    @Test
    public void testTraitValidation() {
        Trait openness = new Trait("Openness", Real.of(0.8));
        assertEquals(0.8, openness.getValue().doubleValue(), 0.001);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> new Trait("Invalid", Real.of(1.5)));
        assertThrows(IllegalArgumentException.class, () -> new Trait("Invalid", Real.of(-0.1)));

        openness.setValue(Real.of(0.5));
        assertEquals(0.5, openness.getValue().doubleValue(), 0.001);

        assertEquals("Openness: 0.50", openness.toString());
    }

    @Test
    public void testDecisionRecording() {
        Decision d = new Decision("Agent 007", "Attack", "Threat detected");

        assertEquals("Agent 007", d.getSubject());
        assertEquals("Attack", d.getChoice());
        assertEquals("Threat detected", d.getRationale());
        assertNotNull(d.getTimestamp());

        assertTrue(d.toString().contains("decided to Attack"));
    }
}
