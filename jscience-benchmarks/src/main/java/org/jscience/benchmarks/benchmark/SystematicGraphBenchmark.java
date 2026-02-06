package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.mathematics.discrete.generators.ScaleFreeGenerator;
import org.jscience.core.mathematics.numbers.integers.Natural;
import org.jscience.core.technical.algorithm.GraphAlgorithmProvider;

/**
 * A benchmark that systematically tests all available GraphAlgorithmProviders.
 */
public class SystematicGraphBenchmark implements SystematicBenchmark<GraphAlgorithmProvider> {

    private static final int NUM_VERTICES = 500;
    private Graph<String> graph;
    private GraphAlgorithmProvider currentProvider;

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "graph-systematic"; }
    @Override public String getNameBase() { return "Systematic Graph"; }
    @Override public Class<GraphAlgorithmProvider> getProviderClass() { return GraphAlgorithmProvider.class; }

    @Override
    public String getDescription() {
        return "Systematically benchmarks all discovered Graph algorithm providers (Multicore, Native, etc.) for community detection.";
    }

    @Override
    public String getDomain() {
        return "Graph Algorithms";
    }

    @Override
    public void setup() {
        // Generate scale-free graph
        Graph<Natural> naturalGraph = ScaleFreeGenerator.generate(NUM_VERTICES, 3, 2);
        
        // Convert to Graph<String>
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

    @Override
    public void setProvider(GraphAlgorithmProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.detectCommunities(graph, 10);
        }
    }

    @Override
    public void teardown() {
        graph = null;
    }
}
