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

package org.jscience.natural.computing.loaders.pmml;

import java.util.*;

/** Decision tree node.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TreeNode {
    private String id;
    private String score;
    private double recordCount;
    private String predicateField;
    private String predicateOperator;
    private String predicateValue;
    private final List<TreeNode> children = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getScore() { return score; }
    public void setScore(String s) { this.score = s; }
    
    public double getRecordCount() { return recordCount; }
    public void setRecordCount(double c) { this.recordCount = c; }
    
    public String getPredicateField() { return predicateField; }
    public void setPredicateField(String f) { this.predicateField = f; }
    
    public String getPredicateOperator() { return predicateOperator; }
    public void setPredicateOperator(String o) { this.predicateOperator = o; }
    
    public String getPredicateValue() { return predicateValue; }
    public void setPredicateValue(String v) { this.predicateValue = v; }
    
    public void addChild(TreeNode n) { if (n != null) children.add(n); }
    public List<TreeNode> getChildren() { return Collections.unmodifiableList(children); }
    
    public boolean isLeaf() { return children.isEmpty(); }
    
    /** Evaluate this node's predicate. */
    public boolean evaluatePredicate(Map<String, Object> inputs) {
        if (predicateField == null) return true; // True predicate
        
        Object input = inputs.get(predicateField);
        if (input == null) return false;
        
        switch (predicateOperator) {
            case "equal": return input.toString().equals(predicateValue);
            case "notEqual": return !input.toString().equals(predicateValue);
            case "lessThan": return toDouble(input) < toDouble(predicateValue);
            case "lessOrEqual": return toDouble(input) <= toDouble(predicateValue);
            case "greaterThan": return toDouble(input) > toDouble(predicateValue);
            case "greaterOrEqual": return toDouble(input) >= toDouble(predicateValue);
            default: return true;
        }
    }
    
    private double toDouble(Object val) {
        if (val instanceof Number) return ((Number) val).doubleValue();
        try { return Double.parseDouble(val.toString()); }
        catch (NumberFormatException e) { return 0; }
    }
    
    @Override
    public String toString() { return "TreeNode{id=" + id + ", score=" + score + ", children=" + children.size() + "}"; }
}

