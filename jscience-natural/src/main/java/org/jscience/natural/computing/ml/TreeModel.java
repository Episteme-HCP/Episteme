/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.computing.ml;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a decision tree model.
 */
@Persistent
public class TreeModel extends Model {
    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Node rootNode;

    public TreeModel() {
    }

    public TreeModel(String name) {
        super(name);
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    @Persistent
    public static class Node implements Serializable {
        @Attribute
        private String id;
        @Attribute
        private String score;
        @Attribute
        private double recordCount;

        @Attribute
        protected final java.util.Map<String, Object> traits = new java.util.HashMap<>();

        @Relation(type = Relation.Type.ONE_TO_MANY)
        private final List<Node> children = new ArrayList<>();

        public Node() {}
        public Node(String id) { this.id = id; }

        public String getId() { return id; }
        public void setScore(String score) { this.score = score; }
        public void setRecordCount(double count) { this.recordCount = count; }
        public void setTrait(String key, Object value) { traits.put(key, value); }
        public void addChild(Node child) { children.add(child); }
    }
}

