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

package org.episteme.social.sociology.survey;

import java.util.List;
import org.episteme.core.util.EnumRegistry;
import org.episteme.core.util.ExtensibleEnum;
import org.episteme.core.util.persistence.Persistent;

/**
 * Extensible enumeration for question types in surveys.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@Persistent
public final class QuestionType extends ExtensibleEnum {



    public static final EnumRegistry<QuestionType> REGISTRY = EnumRegistry.getRegistry(QuestionType.class);

    public static final QuestionType TEXT = new QuestionType("TEXT", true);
    public static final QuestionType PARAGRAPH = new QuestionType("PARAGRAPH", true);
    public static final QuestionType MULTIPLE_CHOICE = new QuestionType("MULTIPLE_CHOICE", true);
    public static final QuestionType CHECKBOXES = new QuestionType("CHECKBOXES", true);
    public static final QuestionType DROPDOWN = new QuestionType("DROPDOWN", true);
    public static final QuestionType LINEAR_SCALE = new QuestionType("LINEAR_SCALE", true);
    public static final QuestionType DATE = new QuestionType("DATE", true);
    public static final QuestionType TIME = new QuestionType("TIME", true);

    private final boolean builtIn;

    public QuestionType(String name) {
        this(name, false);
    }
    
    private QuestionType(String name, boolean builtIn) {
        super(name);
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static QuestionType valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
    
    public static List<QuestionType> values() {
        return REGISTRY.values();
    }
}

