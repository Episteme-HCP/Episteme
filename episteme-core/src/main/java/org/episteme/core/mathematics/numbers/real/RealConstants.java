/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.numbers.real;

/**
 * Internal constants for the Real number system.
 * This class is used to break the cyclic dependency between Real and its subclasses.
 */
final class RealConstants {

    static final Real DOUBLE_ZERO = RealDouble.create(0.0);
    static final Real DOUBLE_ONE = RealDouble.create(1.0);
    
    static final Real BIG_ZERO = RealBig.create(java.math.BigDecimal.ZERO);
    static final Real BIG_ONE = RealBig.create(java.math.BigDecimal.ONE);

    static final Real ZERO = DOUBLE_ZERO; // Legacy/Default
    static final Real ONE = DOUBLE_ONE;   // Legacy/Default
    
    static final Real NaN = RealDouble.create(Double.NaN);
    static final Real PI = RealDouble.create(Math.PI);
    static final Real E = RealDouble.create(Math.E);
    
    // High Precision constants
    static final Real BIG_PI = RealBig.create(new java.math.BigDecimal("3.1415926535897932384626433832795028841971693993751"));
    static final Real BIG_E = RealBig.create(new java.math.BigDecimal("2.7182818284590452353602874713526624977572470936999"));

    static final Real TWO = RealDouble.create(2.0);
    static final Real TWO_PI = RealDouble.create(2.0 * Math.PI);
    static final Real HALF_PI = RealDouble.create(Math.PI / 2.0);
    static final Real POSITIVE_INFINITY = RealDouble.create(Double.POSITIVE_INFINITY);
    static final Real NEGATIVE_INFINITY = RealDouble.create(Double.NEGATIVE_INFINITY);
    
    static final Real LN2 = RealDouble.create(Math.log(2.0));
    static final Real LN10 = RealDouble.create(Math.log(10.0));


    private RealConstants() {}
}
