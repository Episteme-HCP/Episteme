package org.episteme.benchmarks.test.audit;

import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Audit test to verify that the "Zero-Fallback" policy is strictly enforced.
 * Ensures that when native backends are disabled via properties, they are NOT
 * registered or available in the system.
 */
public class ZeroFallbackNativeHardeningTest {

    private static final Logger logger = LoggerFactory.getLogger(ZeroFallbackNativeHardeningTest.class);

    @BeforeEach
    public void setup() {
        // Clear properties to be safe
        clearHardeningProperties();
        BackendDiscovery.getInstance().refresh();
    }

    @AfterEach
    public void tearDown() {
        clearHardeningProperties();
        BackendDiscovery.getInstance().refresh();
    }

    private void clearHardeningProperties() {
        System.clearProperty("episteme.backend.ffm-blas-real.disabled");
        System.clearProperty("episteme.backend.ffm-blas-complex.disabled");
        System.clearProperty("episteme.backend.cpu-real.disabled");
        System.clearProperty("episteme.backend.cpu-complex.disabled");
        System.clearProperty("episteme.backend.simd-real.disabled");
        System.clearProperty("episteme.backend.simd-complex.disabled");
        System.clearProperty("episteme.backend.mpfr-dense.disabled");
        System.clearProperty("episteme.backend.mpfr-sparse.disabled");
        System.clearProperty("episteme.backend.mpfr.disabled");
        System.clearProperty("episteme.backend.nd4j.disabled");
        System.clearProperty("episteme.backend.gpu.disabled");
        System.clearProperty("episteme.backend.opencl.disabled");
        System.clearProperty("episteme.backend.cuda.disabled");
        System.clearProperty("episteme.backend.native.disabled");
    }

    @Test
    public void testStrictDisabling() {
        logger.info("Verifying STRICT DISABLING of native backends...");

        // 1. Set disabling properties for all native backends
        System.setProperty("episteme.backend.ffm-blas-real.disabled", "true");
        System.setProperty("episteme.backend.ffm-blas-complex.disabled", "true");
        System.setProperty("episteme.backend.cpu-real.disabled", "true");
        System.setProperty("episteme.backend.cpu-complex.disabled", "true");
        System.setProperty("episteme.backend.simd-real.disabled", "true");
        System.setProperty("episteme.backend.simd-complex.disabled", "true");
        System.setProperty("episteme.backend.mpfr.disabled", "true");
        System.setProperty("episteme.backend.nd4j.disabled", "true");
        System.setProperty("episteme.backend.gpu.disabled", "true");
        System.setProperty("episteme.backend.native.disabled", "true");

        // Refresh discovery to apply properties
        BackendDiscovery.getInstance().refresh();

        List<Backend> nativeBackends = BackendDiscovery.getInstance().getProviders().stream()
                .filter(p -> p.getClass().getName().contains(".nativ."))
                .filter(Backend::isAvailable)
                .collect(Collectors.toList());

        // Log discovered native backends (should be empty)
        nativeBackends.forEach(b -> logger.error("Hardening Failure: Native backend '{}' is still available!", b.getName()));

        assertTrue(nativeBackends.isEmpty(), 
            "Zero-Fallback Policy Violation: Native backends found available even when disabled: " + 
            nativeBackends.stream().map(Backend::getName).collect(Collectors.joining(", ")));
        
        logger.info("Success: Zero native backends registered under strict disabling.");
    }

    @Test
    public void testPartialDisabling() {
        logger.info("Verifying PARTIAL DISABLING (ND4J only)...");

        System.setProperty("episteme.backend.nd4j.disabled", "true");
        BackendDiscovery.getInstance().refresh();

        List<Backend> nd4jBackends = BackendDiscovery.getInstance().getProviders().stream()
                .filter(p -> p.getId().equals("nd4j"))
                .collect(Collectors.toList());

        for (Backend b : nd4jBackends) {
            assertFalse(b.isAvailable(), "ND4J backend should be unavailable when disabled");
        }
        
        logger.info("Success: ND4J properly gated.");
    }
}
