/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.taxonomy;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;


/**
 * IUCN Red List conservation status.
 * Extensible for local or alternative conservation rankings.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class ConservationStatus extends ExtensibleEnum {

    private static final long serialVersionUID = 2L;

    public static final EnumRegistry<ConservationStatus> REGISTRY = EnumRegistry.getRegistry(ConservationStatus.class);

    public static final ConservationStatus NOT_EVALUATED = new ConservationStatus("NOT_EVALUATED", "NE", true);
    public static final ConservationStatus DATA_DEFICIENT = new ConservationStatus("DATA_DEFICIENT", "DD", true);
    public static final ConservationStatus LEAST_CONCERN = new ConservationStatus("LEAST_CONCERN", "LC", true);
    public static final ConservationStatus NEAR_THREATENED = new ConservationStatus("NEAR_THREATENED", "NT", true);
    public static final ConservationStatus VULNERABLE = new ConservationStatus("VULNERABLE", "VU", true);
    public static final ConservationStatus ENDANGERED = new ConservationStatus("ENDANGERED", "EN", true);
    public static final ConservationStatus CRITICALLY_ENDANGERED = new ConservationStatus("CRITICALLY_ENDANGERED", "CR", true);
    public static final ConservationStatus EXTINCT_IN_WILD = new ConservationStatus("EXTINCT_IN_WILD", "EW", true);
    public static final ConservationStatus EXTINCT = new ConservationStatus("EXTINCT", "EX", true);

    private final String code;
    private final boolean builtIn;

    public ConservationStatus(String name, String code) {
        this(name, code, false);
    }

    private ConservationStatus(String name, String code, boolean builtIn) {
        super(name.toUpperCase());
        this.code = code;
        this.builtIn = builtIn;
        REGISTRY.register(this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }

    public static ConservationStatus valueOf(String name) {
        return REGISTRY.valueOf(name);
    }
}
