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

package org.jscience.social.history.time;


import org.jscience.core.util.EnumRegistry;
import org.jscience.core.util.ExtensibleEnum;

/**
 * An extensible enumeration for chronological eras.
 * Supports standard BC/AD (BCE/CE) but allows for cultural extensions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Era extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EnumRegistry<Era> REGISTRY = new EnumRegistry<>();

    /** Before Common Era (equivalent to BC). */
    public static final Era BCE = new Era("BCE", "Before Common Era", true);
    
    /** Common Era (equivalent to AD). */
    public static final Era CE = new Era("CE", "Common Era", true);

    /** Islamic (Hijri) Era. */
    public static final Era AH = new Era("AH", "Anno Hegirae", true);

    /** Hebrew (Anno Mundi) Era. */
    public static final Era AM = new Era("AM", "Anno Mundi", true);

    private final String description;

    private Era(String name, String description, boolean builtIn) {
        super(name);
        this.description = description;
        /* builtIn ignored as it is handled by ExtensibleEnum or ignored */
        REGISTRY.register(this);
    }

    public static Era valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static Era valueOf(int ordinal) {
        return REGISTRY.valueOf(ordinal);
    }

    @Override
    public String description() {
        return description != null ? description : name();
    }

    /* isBuiltIn removed as it is final in ExtensibleEnum */

    public String getAbbreviation() {
        return name();
    }
}

