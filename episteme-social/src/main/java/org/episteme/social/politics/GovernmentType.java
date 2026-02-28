/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.social.politics;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;

/**
 * Represents a type of government system (e.g. Republic, Monarchy).
 * Uses EnumRegistry pattern to allow dynamic extension.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class GovernmentType extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    // Standard Types
    public static final GovernmentType REPUBLIC = register("REPUBLIC", "Republic");
    public static final GovernmentType MONARCHY = register("MONARCHY", "Monarchy");
    public static final GovernmentType DEMOCRACY = register("DEMOCRACY", "Democracy");
    public static final GovernmentType DICTATORSHIP = register("DICTATORSHIP", "Dictatorship");
    public static final GovernmentType FEDERATION = register("FEDERATION", "Federation");
    public static final GovernmentType COMMUNIST_STATE = register("COMMUNIST_STATE", "Communist State");
    public static final GovernmentType THEOCRACY = register("THEOCRACY", "Theocracy");

    private final String displayName;

    /**
     * Creates a new GovernmentType.
     * Use {@link #of(String)} or {@link #register(String, String)} mainly.
     * 
     * @param name Unique internal name (enum name)
     * @param displayName Human readable description
     */
    public GovernmentType(String name, String displayName) {
        super(name);
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public boolean isBuiltIn() {
        // Simple logic: if in static constants list, it's built-in.
        // But here we rely on standard set.
        return true; 
    }

    /**
     * Helper to register constant.
     */
    private static GovernmentType register(String name, String displayName) {
        GovernmentType type = new GovernmentType(name, displayName);
        EnumRegistry.register(GovernmentType.class, type);
        return type;
    }

    /**
     * Helper to get or create a type.
     * Transforms "Semi-presidential republic" -> "SEMI_PRESIDENTIAL_REPUBLIC" (normalized) 
     * or purely dynamic name.
     */
    public static GovernmentType of(String displayName) {
        if (displayName == null || displayName.isEmpty()) return null;
        
        // Normalize name: UPPER_CASE_UNDERSCORE
        String name = displayName.trim().toUpperCase().replaceAll("[^A-Z0-9]+", "_");
        
        EnumRegistry<GovernmentType> registry = EnumRegistry.getRegistry(GovernmentType.class);
        GovernmentType type = registry.valueOf(name);
        
        if (type == null) {
            // Check if exact match exists in existing specific display names?
            // (Skipped for performance, rely on normalized name)
            
            // Register new
            type = new GovernmentType(name, displayName) {
                @Override
                public boolean isBuiltIn() { return false; }
            };
            registry.register(type);
        }
        return type;
    }
}
