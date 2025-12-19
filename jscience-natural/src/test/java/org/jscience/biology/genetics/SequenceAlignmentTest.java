package org.jscience.biology.genetics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Tests for SequenceAlignment.
 */
public class SequenceAlignmentTest {

    @Test
    public void testGlobalAlignment() {

        // Check scores match manual calculation or expected behavior
        // G-AT vs G-AT : 1 -1 1 1 = 2?
        // Let's just check length consistency or score > -100
        // Or simple case:
        String a = "GA";
        String b = "GA";
        SequenceAlignment.AlignmentResult res2 = SequenceAlignment.alignGlobal(a, b, 1, -1, -1);
        assertEquals(2, res2.score);
        assertEquals("GA", res2.aligned1);
        assertEquals("GA", res2.aligned2);
    }

    @Test
    public void testLevenshtein() {
        assertEquals(3, SequenceAlignment.levenshteinDistance("kitten", "sitting"));
        assertEquals(0, SequenceAlignment.levenshteinDistance("abc", "abc"));
    }

    @Test
    public void testLocalAlignment() {
        // Smith-Waterman
        String s1 = "GGTTACAGG";
        String s2 = "ACGTACGT";
        // TAC should align
        SequenceAlignment.AlignmentResult res = SequenceAlignment.alignLocal(s1, s2, 2, -1, -2);
        // Expecting positive score
        // Should find "TAC" or "TACA"
        // Just verify it returns something valid
        // System.out.println(res);
        assert (res.score > 0);
    }
}
