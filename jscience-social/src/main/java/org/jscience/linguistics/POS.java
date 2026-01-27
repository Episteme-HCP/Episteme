/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.linguistics;


import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * Standard Part Of Speech (POS) tags for natural and formal languages.
 * Includes both universal markers and specific markers for complex grammars.
 * Now extensible to support custom linguistic models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class POS extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EnumRegistry<POS> REGISTRY = EnumRegistry.getRegistry(POS.class);

    // Universal Natural Language Tags
    public static final POS NOUN = new POS("NOUN", true);
    public static final POS VERB = new POS("VERB", true);
    public static final POS ADJECTIVE = new POS("ADJECTIVE", true);
    public static final POS ADVERB = new POS("ADVERB", true);
    public static final POS PRONOUN = new POS("PRONOUN", true);
    public static final POS PREPOSITION = new POS("PREPOSITION", true);
    public static final POS CONJUNCTION = new POS("CONJUNCTION", true);
    public static final POS DETERMINER = new POS("DETERMINER", true);
    public static final POS INTERJECTION = new POS("INTERJECTION", true);
    
    // Lojban specific broad categories
    public static final POS BRIVLA = new POS("BRIVLA", true);
    public static final POS CMAVO = new POS("CMAVO", true);
    public static final POS CMENE = new POS("CMENE", true);
    
    // Formal markers
    public static final POS TERMINAL = new POS("TERMINAL", true);
    public static final POS NON_TERMINAL = new POS("NON_TERMINAL", true);
    public static final POS RULE = new POS("RULE", true);
    
    public static final POS UNKNOWN = new POS("UNKNOWN", true);

    private final boolean builtIn;

    private POS(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    public static POS valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static POS valueOf(int ordinal) {
        return REGISTRY.valueOf(ordinal);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    /**
     * Helper to create custom POS tags.
     */
    public static POS custom(String name) {
        return new POS(name, false);
    }
}
