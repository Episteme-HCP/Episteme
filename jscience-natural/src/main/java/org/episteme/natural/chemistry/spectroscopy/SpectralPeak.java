/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.chemistry.spectroscopy;

import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a peak in a spectrum.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class SpectralPeak implements ComprehensiveIdentification {
    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private final double position;

    @Attribute
    private final double intensity;

    public SpectralPeak(double position, double intensity) {
        this.id = new SimpleIdentification("PEAK:" + UUID.randomUUID());
        this.position = position;
        this.intensity = intensity;
        setName(String.format("%.2f", position));
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public double getPosition() { return position; }
    public double getIntensity() { return intensity; }

    @Override
    public String toString() {
        return String.format("Peak[pos=%.2f, int=%.2f]", position, intensity);
    }
}

