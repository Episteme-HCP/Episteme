package org.episteme.benchmarks.test.audit;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.ProviderSelector;

public class ReproduceCrash {
    public static void main(String[] args) {
        System.out.println("Starting reproduction...");
        try {
            OperationContext ctx = OperationContext.DEFAULT;
            System.out.println("Selecting provider...");
            LinearAlgebraProvider<?> p = ProviderSelector.select(LinearAlgebraProvider.class, ctx);
            System.out.println("Selected: " + p.getName());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
