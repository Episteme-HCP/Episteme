package org.jscience.core.technical.algorithm.mocks;
import org.jscience.core.technical.algorithm.OperationContext;

public class MockProviderC implements TestService {
    @Override public boolean isAvailable() { return true; }
    @Override public String getName() { return "ProviderC"; }
    @Override public int getPriority() { return 5; }
    @Override public double score(OperationContext ctx) {
        if (ctx.hasHint(OperationContext.Hint.GPU_RESIDENT)) return 100.0;
        return 0.0;
    }
}
