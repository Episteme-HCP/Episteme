/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.loaders.pmml;



/** Base PMML model representation. */
public class PMMLModel {
    private String modelName;
    private String functionName;
    private String algorithmName;
    private PMMLMiningSchema miningSchema = new PMMLMiningSchema();

    public String getModelName() { return modelName; }
    public void setModelName(String n) { this.modelName = n; }
    
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String f) { this.functionName = f; }

    public String getAlgorithmName() { return algorithmName; }
    public void setAlgorithmName(String a) { this.algorithmName = a; }

    public PMMLMiningSchema getMiningSchema() { return miningSchema; }
    public void setMiningSchema(PMMLMiningSchema schema) { this.miningSchema = schema; }

    public String getModelType() {
        if (this instanceof PMMLRegressionModel) return "RegressionModel";
        if (this instanceof PMMLTreeModel) return "TreeModel";
        if (this instanceof PMMLNeuralNetwork) return "NeuralNetwork";
        if (this instanceof PMMLClusteringModel) return "ClusteringModel";
        return "Unknown";
    }

    @Override
    public String toString() { return getModelType() + "{" + modelName + "}"; }
}

