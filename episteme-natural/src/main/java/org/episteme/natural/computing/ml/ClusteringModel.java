/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.computing.ml;

import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a clustering model.
 */
@Persistent
public class ClusteringModel extends Model {
    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Cluster> clusters = new ArrayList<>();

    public ClusteringModel() {
    }

    public ClusteringModel(String name) {
        super(name);
    }

    public void addCluster(Cluster cluster) {
        clusters.add(cluster);
    }

    @Persistent
    public static class Cluster implements Serializable {
        @Attribute
        private String id;
        @Attribute
        private String name;
        @Attribute
        private double[] centroid;

        public Cluster() {}
        public Cluster(String id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setCentroid(double[] centroid) { this.centroid = centroid; }
    }
}

