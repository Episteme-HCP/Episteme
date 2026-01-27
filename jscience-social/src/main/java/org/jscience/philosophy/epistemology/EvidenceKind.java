/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.philosophy.epistemology;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;

/**
 * An extensible enumeration for types of evidence.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class EvidenceKind extends ExtensibleEnum {

    public static final EvidenceKind EMPIRICAL = new EvidenceKind("EMPIRICAL");
    public static final EvidenceKind RATIONAL = new EvidenceKind("RATIONAL");
    public static final EvidenceKind TESTIMONIAL = new EvidenceKind("TESTIMONIAL");
    public static final EvidenceKind AXIOMATIC = new EvidenceKind("AXIOMATIC");
    public static final EvidenceKind DEONTIC = new EvidenceKind("DEONTIC");
    public static final EvidenceKind OTHER = new EvidenceKind("OTHER");

    public EvidenceKind(String name) {
        super(name);
        EnumRegistry.register(EvidenceKind.class, this);
    }
}
