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

package org.jscience.apps.physics.spintronics;

import java.util.*;

/**
 * SPICE-like Netlist support for spintronic circuit simulation.
 * <p>
 * Enables textual description of spintronic circuits using a syntax
 * inspired by SPICE (Simulation Program with Integrated Circuit Emphasis).
 * </p>
 * 
 * <h3>Supported Components</h3>
 * <ul>
 *   <li>MTJ - Magnetic Tunnel Junction</li>
 *   <li>SOT - Spin-Orbit Torque device</li>
 *   <li>STT - Spin Transfer Torque device</li>
 *   <li>R - Resistor</li>
 *   <li>C - Capacitor</li>
 *   <li>L - Inductor</li>
 *   <li>V - Voltage source</li>
 *   <li>I - Current source</li>
 * </ul>
 * 
 * <h3>Example Netlist</h3>
 * <pre>
 * * STT-MRAM Cell
 * MTJ1 bit wl gnd CoFeB/MgO/CoFeB RA=10 TMR=150
 * R1 bit vdd 1k
 * V1 vdd gnd 1.0
 * .TRAN 0 10n 1p
 * .END
 * </pre>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpintronicNetlist {

    private final List<NetlistComponent> components = new ArrayList<>();
    private final Map<String, Integer> nodeMap = new HashMap<>();
    private final List<String> nodeNames = new ArrayList<>();
    private final List<AnalysisCommand> analyses = new ArrayList<>();
    private String title = "Untitled";
    private int nextNodeId = 0;

    /**
     * Parses a netlist from a string.
     * 
     * @param netlistText The netlist text
     * @return Parsed Netlist object
     */
    public static SpintronicNetlist parse(String netlistText) {
        SpintronicNetlist netlist = new SpintronicNetlist();
        String[] lines = netlistText.split("\n");
        
        boolean firstLine = true;
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            // First non-comment line is title
            if (firstLine && !line.startsWith("*") && !line.startsWith(".")) {
                netlist.title = line;
                firstLine = false;
                continue;
            }
            firstLine = false;
            
            // Comment
            if (line.startsWith("*")) continue;
            
            // Control command
            if (line.startsWith(".")) {
                netlist.parseCommand(line);
                continue;
            }
            
            // Component
            netlist.parseComponent(line);
        }
        
        return netlist;
    }

    private void parseCommand(String line) {
        String[] parts = line.split("\\s+");
        String cmd = parts[0].toUpperCase();
        
        switch (cmd) {
            case ".TRAN":
                if (parts.length >= 4) {
                    analyses.add(new TransientAnalysis(
                        parseTime(parts[1]), parseTime(parts[2]), parseTime(parts[3])));
                }
                break;
            case ".DC":
                if (parts.length >= 5) {
                    analyses.add(new DCAnalysis(parts[1], 
                        parseValue(parts[2]), parseValue(parts[3]), parseValue(parts[4])));
                }
                break;
            case ".AC":
                if (parts.length >= 5) {
                    analyses.add(new ACAnalysis(parts[1],
                        parseFreq(parts[2]), parseFreq(parts[3]), Integer.parseInt(parts[4])));
                }
                break;
            case ".TEMP":
                if (parts.length >= 2) {
                    analyses.add(new TempSweep(parseValue(parts[1])));
                }
                break;
            case ".END":
                break;
        }
    }

    private void parseComponent(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length < 3) return;
        
        String name = parts[0];
        char type = Character.toUpperCase(name.charAt(0));
        
        switch (type) {
            case 'M': // MTJ
                if (name.toUpperCase().startsWith("MTJ")) {
                    parseMTJ(parts);
                }
                break;
            case 'S': // SOT
                if (name.toUpperCase().startsWith("SOT")) {
                    parseSOT(parts);
                }
                break;
            case 'R': // Resistor
                parseResistor(parts);
                break;
            case 'C': // Capacitor
                parseCapacitor(parts);
                break;
            case 'L': // Inductor
                parseInductor(parts);
                break;
            case 'V': // Voltage source
                parseVoltageSource(parts);
                break;
            case 'I': // Current source
                parseCurrentSource(parts);
                break;
        }
    }

    private void parseMTJ(String[] parts) {
        // MTJ1 top bottom gnd MATERIAL RA=value TMR=value
        if (parts.length < 4) return;
        
        String name = parts[0];
        int nodeTop = getOrCreateNode(parts[1]);
        int nodeBot = getOrCreateNode(parts[2]);
        int nodeRef = getOrCreateNode(parts[3]);
        
        double ra = 10.0;  // Default RA in Ω·μm²
        double tmr = 100.0; // Default TMR in %
        String material = "CoFeB/MgO/CoFeB";
        
        for (int i = 4; i < parts.length; i++) {
            if (parts[i].contains("=")) {
                String[] kv = parts[i].split("=");
                switch (kv[0].toUpperCase()) {
                    case "RA": ra = parseValue(kv[1]); break;
                    case "TMR": tmr = parseValue(kv[1]); break;
                    default:
                        if (!kv[0].contains("/")) material = kv[0];
                }
            } else {
                material = parts[i];
            }
        }
        
        components.add(new MTJComponent(name, nodeTop, nodeBot, nodeRef, material, ra, tmr));
    }

    private void parseSOT(String[] parts) {
        // SOT1 write hm_in hm_out fm MATERIAL theta_sh=value
        if (parts.length < 5) return;
        
        String name = parts[0];
        int nodeWrite = getOrCreateNode(parts[1]);
        int nodeHMIn = getOrCreateNode(parts[2]);
        int nodeHMOut = getOrCreateNode(parts[3]);
        int nodeFM = getOrCreateNode(parts[4]);
        
        double thetaSH = 0.3; // Default spin Hall angle
        String material = "Pt";
        
        for (int i = 5; i < parts.length; i++) {
            if (parts[i].contains("=")) {
                String[] kv = parts[i].split("=");
                if (kv[0].toUpperCase().equals("THETA_SH")) {
                    thetaSH = parseValue(kv[1]);
                }
            } else {
                material = parts[i];
            }
        }
        
        components.add(new SOTComponent(name, nodeWrite, nodeHMIn, nodeHMOut, nodeFM, material, thetaSH));
    }

    private void parseResistor(String[] parts) {
        if (parts.length < 4) return;
        int n1 = getOrCreateNode(parts[1]);
        int n2 = getOrCreateNode(parts[2]);
        double value = parseValue(parts[3]);
        components.add(new ResistorComponent(parts[0], n1, n2, value));
    }

    private void parseCapacitor(String[] parts) {
        if (parts.length < 4) return;
        int n1 = getOrCreateNode(parts[1]);
        int n2 = getOrCreateNode(parts[2]);
        double value = parseValue(parts[3]);
        components.add(new CapacitorComponent(parts[0], n1, n2, value));
    }

    private void parseInductor(String[] parts) {
        if (parts.length < 4) return;
        int n1 = getOrCreateNode(parts[1]);
        int n2 = getOrCreateNode(parts[2]);
        double value = parseValue(parts[3]);
        components.add(new InductorComponent(parts[0], n1, n2, value));
    }

    private void parseVoltageSource(String[] parts) {
        if (parts.length < 4) return;
        int n1 = getOrCreateNode(parts[1]);
        int n2 = getOrCreateNode(parts[2]);
        double value = parseValue(parts[3]);
        String function = parts.length > 4 ? parts[4] : "DC";
        components.add(new VoltageSourceComponent(parts[0], n1, n2, value, function));
    }

    private void parseCurrentSource(String[] parts) {
        if (parts.length < 4) return;
        int n1 = getOrCreateNode(parts[1]);
        int n2 = getOrCreateNode(parts[2]);
        double value = parseValue(parts[3]);
        components.add(new CurrentSourceComponent(parts[0], n1, n2, value));
    }

    private int getOrCreateNode(String name) {
        name = name.toLowerCase();
        if (name.equals("gnd") || name.equals("0")) {
            return 0; // Ground node
        }
        return nodeMap.computeIfAbsent(name, n -> {
            nodeNames.add(n);
            return ++nextNodeId;
        });
    }

    private double parseValue(String s) {
        s = s.trim().toUpperCase();
        double multiplier = 1.0;
        
        if (s.endsWith("F")) { multiplier = 1e-15; s = s.substring(0, s.length()-1); }
        else if (s.endsWith("P")) { multiplier = 1e-12; s = s.substring(0, s.length()-1); }
        else if (s.endsWith("N")) { multiplier = 1e-9; s = s.substring(0, s.length()-1); }
        else if (s.endsWith("U")) { multiplier = 1e-6; s = s.substring(0, s.length()-1); }
        else if (s.endsWith("M")) { multiplier = 1e-3; s = s.substring(0, s.length()-1); }
        else if (s.endsWith("K")) { multiplier = 1e3; s = s.substring(0, s.length()-1); }
        else if (s.endsWith("MEG")) { multiplier = 1e6; s = s.substring(0, s.length()-3); }
        else if (s.endsWith("G")) { multiplier = 1e9; s = s.substring(0, s.length()-1); }
        
        try {
            return Double.parseDouble(s) * multiplier;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseTime(String s) { return parseValue(s); }
    private double parseFreq(String s) { return parseValue(s); }

    /**
     * Generates SPICE-compatible netlist output.
     */
    public String toSpice() {
        StringBuilder sb = new StringBuilder();
        sb.append("* ").append(title).append("\n");
        sb.append("* Generated by JScience Spintronics Simulator\n\n");
        
        for (NetlistComponent comp : components) {
            sb.append(comp.toSpice()).append("\n");
        }
        
        sb.append("\n");
        for (AnalysisCommand cmd : analyses) {
            sb.append(cmd.toSpice()).append("\n");
        }
        
        sb.append(".END\n");
        return sb.toString();
    }

    // Getters
    public List<NetlistComponent> getComponents() { return components; }
    public List<AnalysisCommand> getAnalyses() { return analyses; }
    public int getNodeCount() { return nextNodeId + 1; }
    public String getTitle() { return title; }

    // Component classes
    public abstract static class NetlistComponent {
        protected final String name;
        protected final int[] nodes;
        
        public NetlistComponent(String name, int... nodes) {
            this.name = name;
            this.nodes = nodes;
        }
        
        public abstract String toSpice();
        public String getName() { return name; }
        public int[] getNodes() { return nodes; }
    }

    public static class MTJComponent extends NetlistComponent {
        private final String material;
        private final double ra;
        private final double tmr;
        
        public MTJComponent(String name, int top, int bot, int ref, String mat, double ra, double tmr) {
            super(name, top, bot, ref);
            this.material = mat;
            this.ra = ra;
            this.tmr = tmr;
        }
        
        @Override
        public String toSpice() {
            // Model as variable resistor for SPICE compatibility
            double rP = ra * 1e-12; // Convert to Ω (assuming 1μm² area)
            // double rAP = rP * (1 + tmr / 100);
            return String.format("* MTJ %s: %s, RA=%.1f Ω·μm², TMR=%.0f%%\nR%s %d %d %.3e", 
                    name, material, ra, tmr, name, nodes[0], nodes[1], rP);
        }
        
        public double getRA() { return ra; }
        public double getTMR() { return tmr; }
        public String getMaterial() { return material; }
    }

    public static class SOTComponent extends NetlistComponent {
        private final String material;
        private final double thetaSH;
        
        public SOTComponent(String name, int write, int hmIn, int hmOut, int fm, String mat, double theta) {
            super(name, write, hmIn, hmOut, fm);
            this.material = mat;
            this.thetaSH = theta;
        }
        
        @Override
        public String toSpice() {
            return String.format("* SOT %s: %s, θ_SH=%.2f\nR%s_HM %d %d 50", 
                    name, material, thetaSH, name, nodes[1], nodes[2]);
        }
        
        public double getSpinHallAngle() { return thetaSH; }
    }

    public static class ResistorComponent extends NetlistComponent {
        private final double value;
        public ResistorComponent(String name, int n1, int n2, double val) {
            super(name, n1, n2);
            this.value = val;
        }
        @Override public String toSpice() { return String.format("%s %d %d %.3e", name, nodes[0], nodes[1], value); }
        public double getValue() { return value; }
    }

    public static class CapacitorComponent extends NetlistComponent {
        private final double value;
        public CapacitorComponent(String name, int n1, int n2, double val) {
            super(name, n1, n2);
            this.value = val;
        }
        @Override public String toSpice() { return String.format("%s %d %d %.3e", name, nodes[0], nodes[1], value); }
        public double getValue() { return value; }
    }

    public static class InductorComponent extends NetlistComponent {
        private final double value;
        public InductorComponent(String name, int n1, int n2, double val) {
            super(name, n1, n2);
            this.value = val;
        }
        @Override public String toSpice() { return String.format("%s %d %d %.3e", name, nodes[0], nodes[1], value); }
        public double getValue() { return value; }
    }

    public static class VoltageSourceComponent extends NetlistComponent {
        private final double value;
        private final String function;
        public VoltageSourceComponent(String name, int n1, int n2, double val, String func) {
            super(name, n1, n2);
            this.value = val;
            this.function = func;
        }
        @Override public String toSpice() { 
            return String.format("%s %d %d %s %.3e", name, nodes[0], nodes[1], function, value); 
        }
        public double getValue() { return value; }
    }

    public static class CurrentSourceComponent extends NetlistComponent {
        private final double value;
        public CurrentSourceComponent(String name, int n1, int n2, double val) {
            super(name, n1, n2);
            this.value = val;
        }
        @Override public String toSpice() { return String.format("%s %d %d %.3e", name, nodes[0], nodes[1], value); }
        public double getValue() { return value; }
    }

    // Analysis commands
    public abstract static class AnalysisCommand {
        public abstract String toSpice();
    }

    public static class TransientAnalysis extends AnalysisCommand {
        private final double tStart, tStop, tStep;
        public TransientAnalysis(double start, double stop, double step) {
            this.tStart = start; this.tStop = stop; this.tStep = step;
        }
        @Override public String toSpice() { 
            return String.format(".TRAN %.3e %.3e %.3e", tStart, tStop, tStep); 
        }
    }

    public static class DCAnalysis extends AnalysisCommand {
        private final String source;
        private final double vStart, vStop, vStep;
        public DCAnalysis(String src, double start, double stop, double step) {
            this.source = src; this.vStart = start; this.vStop = stop; this.vStep = step;
        }
        @Override public String toSpice() { 
            return String.format(".DC %s %.3e %.3e %.3e", source, vStart, vStop, vStep); 
        }
    }

    public static class ACAnalysis extends AnalysisCommand {
        private final String type;
        private final double fStart, fStop;
        private final int points;
        public ACAnalysis(String type, double start, double stop, int pts) {
            this.type = type; this.fStart = start; this.fStop = stop; this.points = pts;
        }
        @Override public String toSpice() { 
            return String.format(".AC %s %.3e %.3e %d", type, fStart, fStop, points); 
        }
    }

    public static class TempSweep extends AnalysisCommand {
        private final double temp;
        public TempSweep(double t) { this.temp = t; }
        @Override public String toSpice() { return String.format(".TEMP %.1f", temp); }
    }
}
