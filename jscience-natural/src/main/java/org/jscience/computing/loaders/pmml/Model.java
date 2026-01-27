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

package org.jscience.computing.loaders.pmml;

import java.util.*;

/** Predictive model representation.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Model {
    private String type;
    private String modelName;
    private String functionName;
    private String targetField;
    private String activationFunction;
    private String splitCharacteristic;
    private double intercept;
    private int layerCount;
    private int clusterCount;
    private TreeNode rootNode;
    private final Map<String, Double> coefficients = new LinkedHashMap<>();
    private final Map<String, String> miningFields = new LinkedHashMap<>();

    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    
    public String getModelName() { return modelName; }
    public void setModelName(String n) { this.modelName = n; }
    
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String f) { this.functionName = f; }
    
    public String getTargetField() { return targetField; }
    public void setTargetField(String t) { this.targetField = t; }
    
    public String getActivationFunction() { return activationFunction; }
    public void setActivationFunction(String a) { this.activationFunction = a; }
    
    public String getSplitCharacteristic() { return splitCharacteristic; }
    public void setSplitCharacteristic(String s) { this.splitCharacteristic = s; }
    
    public double getIntercept() { return intercept; }
    public void setIntercept(double i) { this.intercept = i; }
    
    public int getLayerCount() { return layerCount; }
    public void setLayerCount(int c) { this.layerCount = c; }
    
    public int getClusterCount() { return clusterCount; }
    public void setClusterCount(int c) { this.clusterCount = c; }
    
    public TreeNode getRootNode() { return rootNode; }
    public void setRootNode(TreeNode n) { this.rootNode = n; }
    
    public void addCoefficient(String name, double value) { coefficients.put(name, value); }
    public Map<String, Double> getCoefficients() { return Collections.unmodifiableMap(coefficients); }
    
    public void addMiningField(String name, String usage) { miningFields.put(name, usage); }
    public Map<String, String> getMiningFields() { return Collections.unmodifiableMap(miningFields); }
    
    /** Predict for regression model. */
    public double predictRegression(Map<String, Double> inputs) {
        double result = intercept;
        for (Map.Entry<String, Double> coef : coefficients.entrySet()) {
            Double inputVal = inputs.get(coef.getKey());
            if (inputVal != null) {
                result += coef.getValue() * inputVal;
            }
        }
        return result;
    }
    
    @Override
    public String toString() { return type + "{" + modelName + ", function=" + functionName + "}"; }
}
