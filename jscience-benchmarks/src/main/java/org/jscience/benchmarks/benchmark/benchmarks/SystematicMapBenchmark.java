package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.social.ui.viewers.geography.MapBackend;

import java.util.ServiceLoader;
import java.util.List;
import java.util.ArrayList;

/**
 * Benchmark for Social/Geography Map Backends.
 * Validates discovery and metadata access for Map implementations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicMapBenchmark implements RunnableBenchmark {

    private List<MapBackend> backends;

    @Override public String getId() { return "social-map-discovery"; }
    @Override public String getName() { return "Map Backend Discovery"; }
    @Override public String getDescription() { return "Discovers and validates metadata of available Map Rendering Backends (GeoTools, Unfolding, etc.)."; }
    @Override public String getDomain() { return "Social / Geography"; }
    @Override public String getAlgorithmType() { return "Map Rendering"; }
    @Override public String getAlgorithmProvider() { return "ServiceLoader (MapBackend)"; }

    @Override
    public boolean isAvailable() {
        // Always available as a check
        return true;
    }

    @Override
    public void setup() {
        backends = new ArrayList<>();
        ServiceLoader<MapBackend> loader = ServiceLoader.load(MapBackend.class);
        for (MapBackend backend : loader) {
            backends.add(backend);
        }
    }

    @Override
    public void run() {
        // Iterate and validate metadata access speed
        for (MapBackend backend : backends) {
            String name = backend.getName();
            String id = backend.getId();
            boolean avail = backend.isAvailable();
            String desc = backend.getDescription();
            
            if (name == null || id == null) {
                throw new RuntimeException("Invalid backend metadata for: " + backend.getClass().getName());
            }
        }
    }

    @Override
    public void teardown() {
        backends = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 20;
    }
}
