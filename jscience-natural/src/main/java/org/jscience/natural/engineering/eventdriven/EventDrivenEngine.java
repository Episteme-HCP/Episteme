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
    private final Map<String, SimulationEntity> entities;
    private volatile boolean running;
    private Thread engineThread;
    
    public EventDrivenEngine(String id) {
        this.id = id;
        this.eventQueue = new EventQueue();
        this.entities = new ConcurrentHashMap<>();
        this.running = false;
    }
    
    public void registerEntity(SimulationEntity entity) {
        entities.put(entity.getId(), entity);
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
    
    public void sendEvent(String targetId, EventSpec spec, Object... args) {
        SimulationEntity target = entities.get(targetId);
        if (target != null) {
            Event event = new Event(target, spec, args);
            eventQueue.queueNormal(event);
        } else {
            System.err.println("Engine Warning: Target entity not found: " + targetId);
        }
    }
    
    public void sendInternalEvent(SimulationEntity target, EventSpec spec, Object... args) {
        Event event = new Event(target, spec, args);
        eventQueue.queueInternal(event);
    }

    @Override
    public void run() {
        System.out.println("Engine " + id + " started.");
        while (running) {
            try {
                Event event = eventQueue.dequeue();
                processEvent(event);
            } catch (InterruptedException e) {
                if (running) {
                    System.out.println("Engine interrupted, stopping.");
                }
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Error processing event: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Engine " + id + " stopped.");
    }
    
    private void processEvent(Event event) {
        SimulationEntity target = event.getTarget();
        if (target != null) {
            try {
                target.processEvent(event);
            } catch (Exception e) {
                System.err.println("Error in entity " + target.getId() + ": " + e.getMessage());
            }
        }
    }
}
