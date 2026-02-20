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

package org.jscience.social.integration;

/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.physics.classical.mechanics.Kinematics;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Mass;
import org.jscience.core.measure.quantity.Energy;

import java.util.ArrayList;
import java.util.List;

/**
 * Integration tests verifying the interaction between different JScience
 * modules.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SystemIntegrationTest {

    /**
     * Verifies that Physics (Kinematics) correctly utilizes Mathematics
     * (Vectors/Real)
     * and Measure (Quantities) modules to compute kinetic energy.
     */
    @Test
    public void testPhysicsMathIntegration() {
        // 1. Setup Math Components (Vector)
        List<Real> vData = new ArrayList<>();
        vData.add(Real.of(3.0));
        vData.add(Real.of(4.0));
        vData.add(Real.of(0.0));
        Vector<Real> velocity = Vector.of(vData, org.jscience.core.mathematics.sets.Reals.getInstance());

        // 2. Setup Measure Components (Mass)
        Quantity<Mass> mass = Quantities.create(10.0, Units.KILOGRAM);

        // 3. Invoke Physics Logic
        Quantity<Energy> ke = Kinematics.kineticEnergy(mass, velocity);

        // 4. Verify Result
        // KE = 0.5 * m * v^2 = 0.5 * 10 * (3^2 + 4^2) = 5 * 25 = 125
        assertEquals(125.0, ke.to(Units.JOULE).getValue().doubleValue(), 0.001, "Kinetic Energy calculation failed");
    }

    /**
     * Verifies that ComputeContext configuration works correctly.
     */
    @Test
    public void testComputeContextConfiguration() {
        // Access the current compute context
        org.jscience.core.ComputeContext ctx = org.jscience.core.ComputeContext.current();
        
        assertNotNull(ctx, "ComputeContext should not be null");
        
        // Verify distributed context is available
        assertNotNull(ctx.getDistributedContext(), "DistributedContext should be initialized");
        
        // Verify parallelism is available
        assertTrue(ctx.getDistributedContext().getParallelism() > 0, 
                "Should have at least one parallel execution unit");
    }
}



