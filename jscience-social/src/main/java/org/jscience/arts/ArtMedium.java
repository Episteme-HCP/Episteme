/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.arts;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;


/**
 * Extensible categories of artistic media used by artists.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class ArtMedium extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;
    
    public static final EnumRegistry<ArtMedium> REGISTRY = EnumRegistry.getRegistry(ArtMedium.class);

    public static final ArtMedium PAINTING = new ArtMedium("PAINTING", true);
    public static final ArtMedium SCULPTURE = new ArtMedium("SCULPTURE", true);
    public static final ArtMedium MUSIC = new ArtMedium("MUSIC", true);
    public static final ArtMedium DANCE = new ArtMedium("DANCE", true);
    public static final ArtMedium THEATER = new ArtMedium("THEATER", true);
    public static final ArtMedium FILM = new ArtMedium("FILM", true);
    public static final ArtMedium PHOTOGRAPHY = new ArtMedium("PHOTOGRAPHY", true);
    public static final ArtMedium ARCHITECTURE = new ArtMedium("ARCHITECTURE", true);
    public static final ArtMedium LITERATURE = new ArtMedium("LITERATURE", true);
    public static final ArtMedium DIGITAL = new ArtMedium("DIGITAL", true);
    
    public static final ArtMedium OTHER = new ArtMedium("OTHER", true);
    public static final ArtMedium UNKNOWN = new ArtMedium("UNKNOWN", true);

    private final boolean builtIn;

    public ArtMedium(String name) {
        this(name, false);
    }

    private ArtMedium(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static ArtMedium valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}
