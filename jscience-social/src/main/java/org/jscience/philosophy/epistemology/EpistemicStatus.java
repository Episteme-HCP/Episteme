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

package org.jscience.philosophy.epistemology;


import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * Enumeration of possible epistemic statuses of a proposition for a given subject.
 * Extensible to allow for nuanced philosophical states.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EpistemicStatus extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EnumRegistry<EpistemicStatus> REGISTRY = new EnumRegistry<>();

    /** The subject is unaware of the proposition. */
    public static final EpistemicStatus UNKNOWN = new EpistemicStatus("UNKNOWN", true);

    /** The subject knows the proposition for a fact (justified true belief). */
    public static final EpistemicStatus KNOWN = new EpistemicStatus("KNOWN", true);

    /** The subject believes the proposition but lacks sufficient justification. */
    public static final EpistemicStatus BELIEVED = new EpistemicStatus("BELIEVED", true);

    /** The subject is uncertain or suspended in judgment (Epoché). */
    public static final EpistemicStatus DOUBTED = new EpistemicStatus("DOUBTED", true);

    /** The subject actively rejects the proposition as false. */
    public static final EpistemicStatus REJECTED = new EpistemicStatus("REJECTED", true);

    /** The subject considers the proposition possible but not necessarily true. */
    public static final EpistemicStatus HYPOTHETICAL = new EpistemicStatus("HYPOTHETICAL", true);

    public EpistemicStatus(String name) {
        this(name, false);
    }

    private EpistemicStatus(String name, boolean builtIn) {
        super(name);
        /* builtIn ignored */
        REGISTRY.register(this);
    }

    public static EpistemicStatus valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}
