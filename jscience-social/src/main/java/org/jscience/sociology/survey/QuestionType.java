/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.sociology.survey;

import java.util.List;
import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

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
