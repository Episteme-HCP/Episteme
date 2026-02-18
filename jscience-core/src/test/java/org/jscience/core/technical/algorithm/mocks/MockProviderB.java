package org.jscience.core.technical.algorithm.mocks;
import org.jscience.core.technical.algorithm.OperationContext;

public class MockProviderB implements TestService {
    @Override public boolean isAvailable() { return true; }
    @Override public String getName() { return "ProviderB"; }
    @Override public int getPriority() { return 10; }
    @Override public double score(OperationContext ctx) { return 50.0; }
}
