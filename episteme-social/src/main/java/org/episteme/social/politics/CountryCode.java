/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.politics;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.social.politics.loaders.CountryCodesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(CountryCode.class);

    // Static initializer to load values from JSON
    static {
        try {
            CountryCodesReader.getInstance().loadAll();
        } catch (Exception e) {
            logger.error("Failed to load Country Codes from reader: {}", e.getMessage(), e);
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

