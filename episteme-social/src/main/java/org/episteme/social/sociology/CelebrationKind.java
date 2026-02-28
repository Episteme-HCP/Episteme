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

package org.episteme.social.sociology;

import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;


/**
 * Categories of cultural or religious celebrations.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class CelebrationKind extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;
    
    public static final EnumRegistry<CelebrationKind> REGISTRY = EnumRegistry.getRegistry(CelebrationKind.class);

    public static final CelebrationKind BIRTH = new CelebrationKind("BIRTH", true);
    public static final CelebrationKind INITIATION = new CelebrationKind("INITIATION", true);
    public static final CelebrationKind PUBERTY = new CelebrationKind("PUBERTY", true);
    public static final CelebrationKind SOCIAL_ADULTHOOD = new CelebrationKind("SOCIAL_ADULTHOOD", true);
    public static final CelebrationKind GRADUATION = new CelebrationKind("GRADUATION", true);
    public static final CelebrationKind BAPTISM = new CelebrationKind("BAPTISM", true);
    public static final CelebrationKind MARRIAGE = new CelebrationKind("MARRIAGE", true);
    public static final CelebrationKind DEATH = new CelebrationKind("DEATH", true);
    public static final CelebrationKind BURIAL = new CelebrationKind("BURIAL", true);
    public static final CelebrationKind PEACE = new CelebrationKind("PEACE", true);
    public static final CelebrationKind VICTORY = new CelebrationKind("VICTORY", true);
    public static final CelebrationKind WAR = new CelebrationKind("WAR", true);
    public static final CelebrationKind CORONATION = new CelebrationKind("CORONATION", true);
    public static final CelebrationKind SUN = new CelebrationKind("SUN", true);
    public static final CelebrationKind MOON = new CelebrationKind("MOON", true);
    public static final CelebrationKind EARTH = new CelebrationKind("EARTH", true);
    public static final CelebrationKind SEASON = new CelebrationKind("SEASON", true);
    public static final CelebrationKind SUCCESS = new CelebrationKind("SUCCESS", true);
    public static final CelebrationKind PLEASURE = new CelebrationKind("PLEASURE", true);
    public static final CelebrationKind BUSINESS = new CelebrationKind("BUSINESS", true);
    public static final CelebrationKind CALENDAR_SPECIFIC = new CelebrationKind("CALENDAR_SPECIFIC", true);
    public static final CelebrationKind EVENT_SPECIFIC = new CelebrationKind("EVENT_SPECIFIC", true);
    
    public static final CelebrationKind OTHER = new CelebrationKind("OTHER", true);
    public static final CelebrationKind UNKNOWN = new CelebrationKind("UNKNOWN", true);

    private final boolean builtIn;

    public CelebrationKind(String name) {
        this(name, false);
    }

    private CelebrationKind(String name, boolean builtIn) {
        super(name.toUpperCase());
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static CelebrationKind valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}

