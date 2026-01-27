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

package org.jscience.law;


import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * An extensible enumeration for legal act classifications.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ActKind extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final EnumRegistry<ActKind> REGISTRY = new EnumRegistry<>();

    public static final ActKind BIRTH = new ActKind("BIRTH", true);
    public static final ActKind RESIDENCE = new ActKind("RESIDENCE", true);
    public static final ActKind NATIONALITY = new ActKind("NATIONALITY", true);
    public static final ActKind MARRIAGE = new ActKind("MARRIAGE", true);
    public static final ActKind DIVORCE = new ActKind("DIVORCE", true);
    public static final ActKind PROPERTY = new ActKind("PROPERTY", true);
    public static final ActKind APTITUDE = new ActKind("APTITUDE", true);
    public static final ActKind DEATH = new ActKind("DEATH", true);
    public static final ActKind OTHER = new ActKind("OTHER", true);

    private ActKind(String name, boolean builtIn) {
        super(name);
        REGISTRY.register(this);
    }

    public static ActKind valueOf(String name) {
        return REGISTRY.valueOf(name);
    }

    public static ActKind valueOf(int ordinal) {
        return REGISTRY.valueOf(ordinal);
    }


}
