/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.biology.loaders.biopax;

/** 
 * BioPAX Xref DTO.
 */
public class BioPAXXref {
    private String db;
    private String id;

    public BioPAXXref() {}
    public BioPAXXref(String db, String id) {
        this.db = db;
        this.id = id;
    }

    public String getDb() { return db; }
    public void setDb(String db) { this.db = db; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}

