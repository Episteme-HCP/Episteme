package org.jscience.core.technical.algorithm;

import org.jscience.core.technical.algorithm.mocks.TestService;
import org.jscience.core.technical.algorithm.OperationContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProviderSelectorTest {

    @Test
    void testSelectionBasedOnScore() {
        // Without GPU hint: B(50) > A(10) > C(0). Winner: B
        OperationContext ctx = new OperationContext.Builder()
                .dataSize(1000)
                .build();
        TestService best = ProviderSelector.select(TestService.class, ctx);
        assertEquals("ProviderB", best.getName());
    }

    @Test
    void testSelectionWithHints() {
        // With GPU hint: C(100) > B(50) > A(10). Winner: C
        OperationContext ctx = new OperationContext.Builder()
                .addHint(OperationContext.Hint.GPU_RESIDENT)
                .build();
        TestService best = ProviderSelector.select(TestService.class, ctx);
        assertEquals("ProviderC", best.getName());
    }
}
