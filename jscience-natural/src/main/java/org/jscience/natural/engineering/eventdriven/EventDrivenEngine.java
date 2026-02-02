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

package org.jscience.natural.engineering.eventdriven;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Run-time core of the event driven simulation engine.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EventDrivenEngine implements Runnable {
    
    private final String id;
    private final EventQueue eventQueue;
    private final Map<String, SimulationAgent> entities;
    private volatile double currentTime = 0.0;
    
    private volatile boolean running = false;
    private Thread engineThread;

    public EventDrivenEngine(String id) {
        this.id = id;
        this.eventQueue = new EventQueue();
        this.entities = new ConcurrentHashMap<>();
        this.running = false;
    }

    public SimulationAgent getEntity(String id) {
        return entities.get(id);
    }
    
    public double getCurrentTime() {
        return currentTime;
    }

    public void registerEntity(SimulationAgent entity) {
        entities.put(entity.getSimId(), entity);
    }
    
    public void unregisterEntity(String entityId) {
        entities.remove(entityId);
    }
    
    public void start() {
        if (running) return;
        running = true;
        engineThread = new Thread(this, "JScience-Engine-" + id);
        engineThread.start();
    }
    
    public void stop() {
        running = false;
        if (engineThread != null) {
            engineThread.interrupt();
        }
    }
    
    public void join() throws InterruptedException {
        if (engineThread != null) {
            engineThread.join();
        }
    }
    
    /**
     * Schedules an event to occur after a delay.
     * @param delay time from now to schedule
     */
    public void scheduleEvent(String targetId, EventSpec spec, double delay, Object... args) {
        SimulationAgent target = entities.get(targetId);
        if (target != null) {
            double scheduledTime = currentTime + delay;
            Event event = new Event(target, spec, scheduledTime, args);
            eventQueue.queueNormal(event);
        } else {
            System.err.println("Engine Warning: Target not found: " + targetId);
        }
    }
    
    // Legacy support (immediate/next step)
    public void sendEvent(String targetId, EventSpec spec, Object... args) {
        scheduleEvent(targetId, spec, 0.0, args);
    }
    
    public void sendInternalEvent(SimulationAgent target, EventSpec spec, Object... args) {
        // Internal events are immediate/priority, assume same time or ASAP
        Event event = new Event(target, spec, currentTime, args);
        eventQueue.queueInternal(event);
    }

    @Override
    public void run() {
        System.out.println("Engine " + id + " started at t=" + currentTime);
        
        // Cache integrators for continuum entities
        // Simple strategy: Create RK4 on demand or cache?
        // Reuse RK4 instance per entity if possible.
        
        while (running) {
            try {
                // Hybrid Loop:
                // 1. Determine next event time
                double nextEventTime = eventQueue.peekNextTime();
                
                // If internal queue has events, nextEventTime is -Infinity (immediate)
                // If normal queue has events, nextEventTime is T
                // If both empty, NaN
                
                if (Double.isNaN(nextEventTime)) {
                    // No events. Wait or step forward continuously?
                    // Strategy: Step forward by dt_max? Or wait for input?
                    // For now, simple wait (via dequeue blocking)
                    // But if we have continuous entities, they should evolve!
                    // Assuming simulation ends if no events? Or evolves until maxTime?
                    // Let's assume we wait for events.
                    Event event = eventQueue.dequeue(); // Block
                    processEvent(event);
                    continue; // Re-evaluate loop
                }
                
                if (nextEventTime == Double.NEGATIVE_INFINITY) {
                    // Immediate processing
                    Event event = eventQueue.dequeue();
                    processEvent(event);
                    continue;
                }
                
                // Normal event at T > currentTime?
                if (nextEventTime > currentTime) {
                    // Integrate from currentTime to nextEventTime
                    double dt = nextEventTime - currentTime;
                    integrateContinuum(dt);
                    currentTime = nextEventTime;
                }
                
                // Process the event
                Event event = eventQueue.dequeue();
                // Ensure we update time if logic differs
                currentTime = Math.max(currentTime, event.getTime());
                processEvent(event);

            } catch (InterruptedException e) {
                if (running) System.out.println("Engine interrupted.");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Engine Loop Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Engine " + id + " stopped.");
    }
    
    private void integrateContinuum(double duration) {
        if (duration <= 1e-9) return;
        
        org.jscience.core.mathematics.numbers.real.Real h = org.jscience.core.mathematics.numbers.real.Real.of(0.01);
        org.jscience.core.mathematics.numbers.real.Real t = org.jscience.core.mathematics.numbers.real.Real.of(currentTime);
        org.jscience.core.mathematics.numbers.real.Real tEnd = t.add(org.jscience.core.mathematics.numbers.real.Real.of(duration));
        
        // Integrator acquisition from registry/context
        org.jscience.core.technical.backend.algorithms.ODEProvider integrator = new org.jscience.core.technical.backend.algorithms.RungeKuttaODEProvider();
        
        for (SimulationAgent entity : entities.values()) {
            if (entity instanceof org.jscience.natural.engineering.continuum.Continuum continuum) {
                // Create adapter function
                org.jscience.core.mathematics.analysis.Function<org.jscience.core.mathematics.numbers.real.Real[], org.jscience.core.mathematics.numbers.real.Real[]> f = 
                    new org.jscience.core.mathematics.analysis.Function<org.jscience.core.mathematics.numbers.real.Real[], org.jscience.core.mathematics.numbers.real.Real[]>() {
                        @Override
                        public org.jscience.core.mathematics.numbers.real.Real[] evaluate(org.jscience.core.mathematics.numbers.real.Real[] input) {
                            org.jscience.core.mathematics.numbers.real.Real time = input[0];
                            org.jscience.core.mathematics.numbers.real.Real[] state = new org.jscience.core.mathematics.numbers.real.Real[input.length - 1];
                            System.arraycopy(input, 1, state, 0, state.length);
                            return continuum.computeDerivatives(time, state);
                        }
                        
                        @Override
                        public String getDomain() { return "Real^" + (continuum.getDimension() + 1); }
                        
                        @Override
                        public String getCodomain() { return "Real^" + continuum.getDimension(); }
                    };

                org.jscience.core.mathematics.numbers.real.Real[] y0 = continuum.getState();
                org.jscience.core.mathematics.numbers.real.Real[] yNew = integrator.solve(f, t, y0, tEnd, h);
                continuum.setState(yNew);
            }
        }
    }
    
    private void processEvent(Event event) {
        SimulationAgent target = event.getTarget();
        if (target != null) {
            try {
                target.processEvent(event);
            } catch (Exception e) {
                System.err.println("Error in agent " + target.getSimId() + ": " + e.getMessage());
            }
        }
    }
}
