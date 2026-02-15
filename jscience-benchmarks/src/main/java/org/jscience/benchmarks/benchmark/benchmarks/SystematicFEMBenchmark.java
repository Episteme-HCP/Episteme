package org.jscience.benchmarks.benchmark.benchmarks;

import com.google.auto.service.AutoService;
import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import org.jscience.core.technical.algorithm.FEMProvider;
import org.jscience.core.mathematics.analysis.fem.Mesh;
import org.jscience.core.mathematics.analysis.Function;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;

import java.util.ServiceLoader;
import java.util.ArrayList;
import java.util.List;
import org.jscience.core.mathematics.analysis.fem.Node;
import org.jscience.core.mathematics.analysis.fem.TriangularElement2D;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.sets.Reals;

/**
 * Systematic benchmark for Finite Element Method (FEM) providers.
 * Tests Poisson equation solving on a simple mesh.
 */
@AutoService(RunnableBenchmark.class)
public class SystematicFEMBenchmark implements SystematicBenchmark<FEMProvider> {

    private FEMProvider provider;
    private Mesh testMesh;
    private Function<Vector<Real>, Real> sourceTerm;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }

    @Override
    public Class<FEMProvider> getProviderClass() {
        return FEMProvider.class;
    }

    @Override
    public String getIdPrefix() {
        return "fem";
    }

    @Override
    public String getNameBase() {
        return "FEM Poisson Solver";
    }

    @Override
    public void setProvider(FEMProvider provider) {
        this.provider = provider;
    }

    @Override
    public void setup() {
        if (provider == null) {
            // Try to find any available FEM provider
            ServiceLoader<FEMProvider> loader = ServiceLoader.load(FEMProvider.class);
            provider = loader.findFirst().orElse(null);
        }
        
        if (provider == null || !provider.isAvailable()) {
            throw new UnsupportedOperationException("No FEM provider available");
        }

        // Create a simple 2D mesh (placeholder - would need actual mesh implementation)
        // For now, we'll skip actual mesh creation and just test provider availability
        // Create a simple 2D mesh (2 triangles forming a square)
        testMesh = new Mesh();
        
        // Nodes
        List<Real> c1 = new ArrayList<>(); c1.add(Real.ZERO); c1.add(Real.ZERO);
        Node n1 = new Node(1, new DenseVector<>(c1, Reals.getInstance()));
        
        List<Real> c2 = new ArrayList<>(); c2.add(Real.ONE); c2.add(Real.ZERO);
        Node n2 = new Node(2, new DenseVector<>(c2, Reals.getInstance()));
        
        List<Real> c3 = new ArrayList<>(); c3.add(Real.ZERO); c3.add(Real.ONE);
        Node n3 = new Node(3, new DenseVector<>(c3, Reals.getInstance()));
        
        List<Real> c4 = new ArrayList<>(); c4.add(Real.ONE); c4.add(Real.ONE);
        Node n4 = new Node(4, new DenseVector<>(c4, Reals.getInstance()));

        testMesh.addNode(n1);
        testMesh.addNode(n2);
        testMesh.addNode(n3);
        testMesh.addNode(n4);

        // Elements
        testMesh.addElement(new TriangularElement2D(n1, n2, n3));
        testMesh.addElement(new TriangularElement2D(n2, n4, n3));
        
        testMesh.indexNodes();
        
        // Simple source term: f(x) = 1.0
        sourceTerm = (vec) -> Real.valueOf(1.0);
    }

    @Override
    public void run() {
        if (testMesh != null) {
            provider.solvePoisson(testMesh, sourceTerm);
        }
        // If mesh is null, just verify provider is callable
    }

    @Override
    public void teardown() {
        testMesh = null;
        sourceTerm = null;
    }

    @Override
    public String getDomain() {
        return "Finite Element Method";
    }

    @Override
    public String getDescription() {
        return "Solves Poisson equation using FEM on a 2D mesh";
    }

    @Override
    public String getAlgorithmProvider() {
        return provider != null ? provider.getName() : "Unknown";
    }

    @Override
    public int getSuggestedIterations() {
        return 10; // FEM is computationally expensive
    }
}
