/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.loaders.pmml;

import java.util.ArrayList;
import java.util.List;

public class PMMLClusteringModel extends PMMLModel {
    private String modelClass;
    private int numberOfClusters;
    private final List<PMMLCluster> clusters = new ArrayList<>();

    public String getModelClass() { return modelClass; }
    public void setModelClass(String c) { this.modelClass = c; }
    public int getNumberOfClusters() { return numberOfClusters; }
    public void setNumberOfClusters(int n) { this.numberOfClusters = n; }
    public List<PMMLCluster> getClusters() { return clusters; }
    public void addCluster(PMMLCluster c) { clusters.add(c); }
}

class PMMLCluster {
    private String id;
    private String name;
    private double[] array;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public double[] getArray() { return array; }
    public void setArray(double[] a) { this.array = a; }
}

