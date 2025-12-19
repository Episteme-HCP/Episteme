package org.jscience.chemistry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReactionBalancerTest {

    @Test
    public void testComplexBalancing() {
        // Calcium hydroxide + Phosphoric acid -> Calcium phosphate + Water
        // 3Ca(OH)2 + 2H3PO4 -> Ca3(PO4)2 + 6H2O
        String eq = "Ca(OH)2 + H3PO4 -> Ca3(PO4)2 + H2O";
        ChemicalReaction balanced = ReactionBalancer.balance(eq);

        System.out.println("Balanced: " + balanced);

        assertEquals(3, balanced.getReactants().get("Ca(OH)2"));
        assertEquals(2, balanced.getReactants().get("H3PO4"));
        assertEquals(1, balanced.getProducts().get("Ca3(PO4)2"));
        assertEquals(6, balanced.getProducts().get("H2O"));
    }
}
