package org.jscience.psychology;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PsychologyDomainTest {

    @Test
    public void testTraitValidation() {
        Trait openness = new Trait("Openness", 0.8);
        assertEquals(0.8, openness.getValue(), 0.001);

        // Test bounds
        assertThrows(IllegalArgumentException.class, () -> new Trait("Invalid", 1.5));
        assertThrows(IllegalArgumentException.class, () -> new Trait("Invalid", -0.1));

        openness.setValue(0.5);
        assertEquals(0.5, openness.getValue(), 0.001);

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
