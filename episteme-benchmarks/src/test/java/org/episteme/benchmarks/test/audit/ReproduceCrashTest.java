package org.episteme.benchmarks.test.audit;

import org.junit.jupiter.api.Test;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.ProviderSelector;

public class ReproduceCrashTest {
    @Test
    public void testSelect() {
        System.out.println("Starting reproduction test...");
        OperationContext ctx = OperationContext.DEFAULT;
        System.out.println("Selecting provider...");
        LinearAlgebraProvider<?> p = ProviderSelector.select(LinearAlgebraProvider.class, ctx);
        System.out.println("Selected: " + p.getName());
    }
}
