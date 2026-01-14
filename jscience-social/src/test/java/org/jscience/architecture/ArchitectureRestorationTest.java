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

package org.jscience.architecture;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.architecture.lift.LiftBuilding;
import org.jscience.architecture.lift.Elevator;
import org.jscience.architecture.traffic.Road;
import org.jscience.architecture.traffic.Vehicle;
import org.jscience.geography.Coordinate;
import org.jscience.mathematics.numbers.real.Real;

import java.util.Arrays;

public class ArchitectureRestorationTest {

    @Test
    public void testLiftSimulation() {
        LiftBuilding empireState = new LiftBuilding("Empire State", 0, 102);
        Elevator expressLift = new Elevator("Express 1", 20);

        empireState.addElevator(expressLift);
        assertEquals(0, expressLift.getCurrentFloor());
        assertEquals(Elevator.State.WAITING, expressLift.getState());

        // Call to 50th floor
        expressLift.call(50);
        assertEquals(Elevator.State.MOVING_UP, expressLift.getState());

        // Simulate ticks (would normally loop, here manual step check)
        empireState.tick();
        assertEquals(1, expressLift.getCurrentFloor());
    }

    @Test
    public void testTrafficSimulation() {
        Coordinate start = new Coordinate(40.7128, -74.0060); // NYC
        Coordinate end = new Coordinate(40.7306, -73.9352);

        Road broadway = new Road("Broadway", Arrays.asList(start, end), 13.8, 4); // 50km/h ~= 13.8m/s

        Vehicle taxi = new Vehicle("Taxi 123");
        taxi.enterRoad(broadway);

        assertEquals(broadway, taxi.getCurrentRoad());
        assertEquals(start, taxi.getPosition());

        taxi.setSpeed(Real.of(10.0));
        assertEquals(10.0, taxi.getSpeed().doubleValue(), 0.001);
    }
}


