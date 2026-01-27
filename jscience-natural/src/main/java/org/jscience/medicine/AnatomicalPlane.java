/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.medicine;

import org.jscience.util.EnumRegistry;
import org.jscience.util.ExtensibleEnum;
import org.jscience.util.persistence.Persistent;

/**
 * Extensible enumeration for anatomical planes.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@Persistent
public final class AnatomicalPlane extends ExtensibleEnum {

    private static final long serialVersionUID = 1L;

    public static final AnatomicalPlane TRANSVERSE = new AnatomicalPlane("TRANSVERSE", "Axial (X-Y)", true);
    public static final AnatomicalPlane CORONAL = new AnatomicalPlane("CORONAL", "Frontal (X-Z)", true);
    public static final AnatomicalPlane SAGITTAL = new AnatomicalPlane("SAGITTAL", "(Y-Z)", true);
    
    private final String description;
    private final boolean builtIn;

    static {
        EnumRegistry.register(AnatomicalPlane.class, TRANSVERSE);
        EnumRegistry.register(AnatomicalPlane.class, CORONAL);
        EnumRegistry.register(AnatomicalPlane.class, SAGITTAL);
    }

    public AnatomicalPlane(String name, String description) {
        super(name);
        this.description = description;
        this.builtIn = false;
        EnumRegistry.register(AnatomicalPlane.class, this);
    }

    private AnatomicalPlane(String name, String description, boolean builtIn) {
        super(name);
        this.description = description;
        this.builtIn = builtIn;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean isBuiltIn() {
        return builtIn;
    }
    
    public static AnatomicalPlane valueOf(String name) {
        return EnumRegistry.getRegistry(AnatomicalPlane.class).valueOfRequired(name);
    }
    
    public static AnatomicalPlane[] values() {
        return EnumRegistry.getRegistry(AnatomicalPlane.class).values().toArray(new AnatomicalPlane[0]);
    }
}
