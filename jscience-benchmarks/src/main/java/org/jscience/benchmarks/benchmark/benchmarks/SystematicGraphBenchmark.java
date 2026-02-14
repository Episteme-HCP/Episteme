/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;

import com.google.auto.service.AutoService;

import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.mathematics.discrete.generators.ScaleFreeGenerator;
import org.jscience.core.mathematics.numbers.integers.Natural;
import org.jscience.core.technical.algorithm.GraphAlgorithmProvider;

/**
 * A benchmark that systematically tests all available GraphAlgorithmProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
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
