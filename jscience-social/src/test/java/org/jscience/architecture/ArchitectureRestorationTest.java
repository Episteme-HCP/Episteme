package org.jscience.architecture;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.architecture.lift.Building;
import org.jscience.architecture.lift.Elevator;
import org.jscience.architecture.traffic.Road;
import org.jscience.architecture.traffic.Vehicle;
import org.jscience.geography.Coordinate;

import java.util.Arrays;

public class ArchitectureRestorationTest {

    @Test
    public void testLiftSimulation() {
        Building empireState = new Building("Empire State", 0, 102);
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

        taxi.setSpeed(10.0);
        assertEquals(10.0, taxi.getSpeed());
    }
}
