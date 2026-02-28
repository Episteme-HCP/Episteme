/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.social.sports;

import java.util.List;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Dimensionless;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated baseline test for TrainingPeriodization.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TrainingPeriodizationTest {

    @Test
    public void testCalculateCTL_SteadyLoad() {
        List<Quantity<Dimensionless>> tss = new java.util.ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tss.add(Quantities.create(100.0, Units.ONE));
        }

        Quantity<Dimensionless> ctl = TrainingPeriodization.calculateCTL(tss, 42);
        // CTL should converge to the steady load value
        assertEquals(100.0, ctl.getValue().doubleValue(), 0.001);
    }

    @Test
    public void testCalculateATL_ShortWindow() {
        List<Quantity<Dimensionless>> tss = new java.util.ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tss.add(Quantities.create(100.0, Units.ONE));
        }

        Quantity<Dimensionless> atl = TrainingPeriodization.calculateATL(tss);
        assertEquals(100.0, atl.getValue().doubleValue(), 0.001);
    }

    @Test
    public void testCalculateTSB_FitnessVsFatigue() {
        Quantity<Dimensionless> ctl = Quantities.create(80.0, Units.ONE);
        Quantity<Dimensionless> atl = Quantities.create(100.0, Units.ONE);
        
        Quantity<Dimensionless> tsb = TrainingPeriodization.calculateTSB(ctl, atl);
        assertEquals(-20.0, tsb.getValue().doubleValue(), 0.001);
    }
}

