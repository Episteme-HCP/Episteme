package org.jscience.benchmarks.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.mathematics.discrete.generators.ScaleFreeGenerator;
import org.jscience.core.mathematics.numbers.integers.Natural;
import org.jscience.core.technical.backend.algorithms.GraphAlgorithmProvider;
import org.jscience.core.technical.backend.algorithms.MulticoreGraphAlgorithmProvider;
import org.jscience.nativ.mathematics.discrete.backends.NativeGraphAlgorithmProvider;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class GraphAlgorithmBenchmark {

    @Param({"100", "500"})
    public int numVertices;

    private GraphAlgorithmProvider multicoreProvider;
    private GraphAlgorithmProvider nativeProvider;
    private Graph<String> graph;

    @Setup(Level.Trial)
    public void doSetup() {
        multicoreProvider = new MulticoreGraphAlgorithmProvider();
        nativeProvider = new NativeGraphAlgorithmProvider();
        
        // Generate scale-free graph: n=numVertices, m0=3 (initial), m=2 (edges per new node)
        Graph<Natural> naturalGraph = ScaleFreeGenerator.generate(numVertices, 3, 2);
        
        // Convert to Graph<String> for provider compatibility
        graph = new org.jscience.core.mathematics.discrete.UndirectedGraph<>();
        for (Natural vertex : naturalGraph.vertices()) {
            graph.addVertex(vertex.toString());
        }
        for (Natural v1 : naturalGraph.vertices()) {
            for (Natural v2 : naturalGraph.neighbors(v1)) {
                graph.addEdge(v1.toString(), v2.toString());
            }
        }
    }

    @Benchmark
    public Map<String, Integer> benchmarkMulticoreCommunityDetection() {
        return multicoreProvider.detectCommunities(graph, 10);
    }

    @Benchmark
    public Map<String, Integer> benchmarkNativeCommunityDetection() {
        return nativeProvider.detectCommunities(graph, 10);
    }
}
