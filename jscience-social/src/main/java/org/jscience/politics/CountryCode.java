/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.politics;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;
import org.jscience.politics.loaders.CountryCodesReader;

import java.util.logging.Logger;

/**
 * Extensible listing of Country Codes (ISO 3166-1 alpha-2).
 * <p>
 * Values are loaded dynamically from a JSON resource via {@link CountryCodesReader}.
 * To access a country code, use {@link #valueOf(String)}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class CountryCode extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CountryCode.class.getName());

    // Static initializer to load values from JSON
    static {
        try {
            CountryCodesReader.getInstance().loadAll();
        } catch (Exception e) {
            LOGGER.severe("Failed to load Country Codes from reader: " + e.getMessage());
        }
    }

    private final String englishName;
    private final boolean builtIn;

    /**
     * Constructor used by the Reader.
     * 
     * @param code The ISO 2-letter code (used as name).
     * @param englishName The English name.
     * @param builtIn Whether it is a built-in code.
     */
    public CountryCode(String code, String englishName, boolean builtIn) {
        super(code);
        this.englishName = englishName;
        this.builtIn = builtIn;
        EnumRegistry.register(CountryCode.class, this);
    }
    
    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public String getEnglishName() {
        return englishName;
    }

    /**
     * Returns the {@link CountryCode} corresponding to the specified name (code).
     *
     * @param name The code name (e.g. "US").
     * @return The CountryCode instance.
     * @throws IllegalArgumentException if not found.
     */
    public static CountryCode valueOf(String name) {
        return EnumRegistry.getRegistry(CountryCode.class).valueOfRequired(name);
    }

    public static CountryCode[] values() {
        return EnumRegistry.getRegistry(CountryCode.class).values().toArray(new CountryCode[0]);
    }
}
