/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.loaders.pmml;

import java.util.ArrayList;
import java.util.List;

public class PMMLTreeModel extends PMMLModel {
    private String splitCharacteristic;
    private String missingValueStrategy;
    private PMMLNode node;

    public String getSplitCharacteristic() { return splitCharacteristic; }
    public void setSplitCharacteristic(String s) { this.splitCharacteristic = s; }
    public String getMissingValueStrategy() { return missingValueStrategy; }
    public void setMissingValueStrategy(String s) { this.missingValueStrategy = s; }
    public PMMLNode getNode() { return node; }
    public void setRootNode(PMMLNode node) { this.node = node; }
    public void setNode(PMMLNode node) { this.node = node; }
}

class PMMLNode {
    private String id;
    private String score;
    private double recordCount;
    private PMMLPredicate predicate;
    private final List<PMMLNode> nodes = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getScore() { return score; }
    public void setScore(String s) { this.score = s; }
    public double getRecordCount() { return recordCount; }
    public void setRecordCount(double c) { this.recordCount = c; }
    public PMMLPredicate getPredicate() { return predicate; }
    public void setPredicate(PMMLPredicate p) { this.predicate = p; }
    public List<PMMLNode> getNodes() { return nodes; }
    public void addChild(PMMLNode n) { nodes.add(n); }
    public void addNode(PMMLNode n) { nodes.add(n); }
}

class PMMLPredicate {
    private String type;
    private String field;
    private String operator;
    private String value;

    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public String getField() { return field; }
    public void setField(String f) { this.field = f; }
    public String getOperator() { return operator; }
    public void setOperator(String o) { this.operator = o; }
    public String getValue() { return value; }
    public void setValue(String v) { this.value = v; }
}

