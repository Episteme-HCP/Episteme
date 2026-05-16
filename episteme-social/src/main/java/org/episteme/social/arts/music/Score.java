/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.social.arts.music;

import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a musical score (notation).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Score implements ComprehensiveIdentification {
    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private String title;

    @Attribute
    private String composer;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    private final java.util.List<Part> parts = new java.util.ArrayList<>();

    public Score(String title) {
        this.id = new SimpleIdentification("SCORE:" + UUID.randomUUID());
        this.title = title;
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public void addPart(Part part) {
        parts.add(part);
    }

    public java.util.List<Part> getParts() {
        return parts;
    }
}

