package org.episteme.core.technical.algorithm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AutoTuningManagerTest {

    @Test
    public void testDynamicScoring() {
        // Higher priority should yield higher score initially
        double scoreLow = AutoTuningManager.getDynamicScore("LowPri", 100, 10);
        double scoreHigh = AutoTuningManager.getDynamicScore("HighPri", 100, 100);
        
        assertTrue(scoreHigh > scoreLow, "High priority should have better score");
        
        // Test penalty for poor performance
        AutoTuningManager.recordSample("HighPri", 100, 500.0, 1000); // Too slow, low GFLOPS
        AutoTuningManager.recordSample("LowPri", 100, 1.0, 1_000_000_000L);  // Very fast, high GFLOPS

        
        double scoreLowAfter = AutoTuningManager.getDynamicScore("LowPri", 100, 10);
        double scoreHighAfter = AutoTuningManager.getDynamicScore("HighPri", 100, 100);
        
        assertTrue(scoreLowAfter > scoreHighAfter, "Faster provider should eventually have better score regardless of priority");
    }
}
