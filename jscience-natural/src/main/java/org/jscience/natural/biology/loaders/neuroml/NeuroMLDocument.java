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

package org.jscience.natural.biology.loaders.neuroml;

import java.util.*;

/** NeuroML document container.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NeuroMLDocument {
    private String id;
    private final List<Cell> cells = new ArrayList<>();
    private final List<IonChannel> ionChannels = new ArrayList<>();
    private final List<Network> networks = new ArrayList<>();
    private final List<Synapse> synapses = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public void addCell(Cell cell) { if (cell != null) cells.add(cell); }
    public List<Cell> getCells() { return Collections.unmodifiableList(cells); }
    
    public void addIonChannel(IonChannel ch) { if (ch != null) ionChannels.add(ch); }
    public List<IonChannel> getIonChannels() { return Collections.unmodifiableList(ionChannels); }
    
    public void addNetwork(Network n) { if (n != null) networks.add(n); }
    public List<Network> getNetworks() { return Collections.unmodifiableList(networks); }
    
    public void addSynapse(Synapse s) { if (s != null) synapses.add(s); }
    public List<Synapse> getSynapses() { return Collections.unmodifiableList(synapses); }
}

