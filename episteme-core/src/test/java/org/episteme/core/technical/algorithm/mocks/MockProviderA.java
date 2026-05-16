package org.episteme.core.technical.algorithm.mocks;
import org.episteme.core.technical.algorithm.OperationContext;

public class MockProviderA implements TestService {
    @Override public boolean isAvailable() { return true; }
    @Override public String getName() { return "ProviderA"; }
    @Override public int getPriority() { return 1; }
    @Override public double score(OperationContext ctx) { return 10.0; }
}
