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

package org.jscience.architecture.lift;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a logical building structure containing a vertical transportation system.
 * It manages the group control logic (dispatching) and maintains passenger queues 
 * for each floor.
 *
 * <p>Dispatching logic utilizes an Estimated Time of Arrival (ETA) calculation 
 * to minimize average waiting time.</p>
 *
 * <p>Reference: Barney, G. C. (2003). Elevator Traffic Handbook: Theory and Practice.</p>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LiftBuilding implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String name;
    private final int minFloor;
    private final int maxFloor;
    private final List<Elevator> elevators;
    private final List<List<Passenger>> floorQueues;

    /**
     * Initializes a new building with a specific floor range.
     * 
     * @param name common name for the building
     * @param minFloor the lowest floor index (e.g., -1 for basement)
     * @param maxFloor the highest floor index
     */
    public LiftBuilding(String name, int minFloor, int maxFloor) {
        this.name = name;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.elevators = new ArrayList<>();
        
        int floorCount = Math.max(0, maxFloor - minFloor + 1);
        this.floorQueues = new ArrayList<>(floorCount);
        for (int i = 0; i < floorCount; i++) {
            floorQueues.add(new ArrayList<>());
        }
    }

    /**
     * Integrates an elevator cabin into the building's control system.
     * @param elevator the cabin unit to add
     */
    public void addElevator(Elevator elevator) {
        if (elevator != null) {
            elevators.add(elevator);
        }
    }

    /**
     * Processes a floor call from outside the elevator cabs.
     * Selects the optimal cab based on distance and current workload (ETA heuristic).
     * 
     * @param floor the floor index of the call
     * @param direction intended direction (UP or DOWN)
     */
    public void hallCall(int floor, Elevator.Direction direction) {
        if (floor < minFloor || floor > maxFloor) return;
        
        Elevator best = null;
        int minEta = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            int eta = calculateETA(e, floor, direction);
            if (eta < minEta) {
                minEta = eta;
                best = e;
            }
        }

        if (best != null) {
            best.hallCall(floor, direction);
        }
    }

    private int calculateETA(Elevator e, int targetFloor, Elevator.Direction callDir) {
        int dist = Math.abs(e.getCurrentFloor() - targetFloor);
        
        // Logical penalties for distance metrics
        if (e.getDirection() == Elevator.Direction.UP && targetFloor < e.getCurrentFloor()) dist += 10;
        if (e.getDirection() == Elevator.Direction.DOWN && targetFloor > e.getCurrentFloor()) dist += 10;
        if (e.getDirection() != Elevator.Direction.NONE && e.getDirection() != callDir) dist += 5;
        
        return dist;
    }

    /**
     * Introduces a passenger into the building's waiting area.
     * 
     * @param p the passenger to add
     */
    public void addPassenger(Passenger p) {
        if (p == null) return;
        int floorIdx = p.getSourceFloor() - minFloor;
        if (floorIdx >= 0 && floorIdx < floorQueues.size()) {
            floorQueues.get(floorIdx).add(p);
            hallCall(p.getSourceFloor(), p.getDestinationFloor() > p.getSourceFloor() ? 
                    Elevator.Direction.UP : Elevator.Direction.DOWN);
        }
    }

    /**
     * Progresses the entire building simulation by one time step.
     */
    public void tick() {
        for (Elevator elevator : elevators) {
            elevator.tick();
            
            // Transfer waiting passengers into cab if it is open and on the correct floor
            if (elevator.getState() == Elevator.State.OPEN) {
                int floorIdx = elevator.getCurrentFloor() - minFloor;
                if (floorIdx >= 0 && floorIdx < floorQueues.size()) {
                    List<Passenger> queue = floorQueues.get(floorIdx);
                    queue.removeIf(p -> {
                        if (elevator.getPassengerCount() < elevator.getCapacity()) {
                            elevator.addPassenger(p);
                            return true;
                        }
                        return false;
                    });
                }
            }
        }
    }

    public String getName() { return name; }
    
    public int getMinFloor() { return minFloor; }
    
    public int getMaxFloor() { return maxFloor; }
    
    public List<Elevator> getElevators() { 
        return Collections.unmodifiableList(elevators); 
    }
}
