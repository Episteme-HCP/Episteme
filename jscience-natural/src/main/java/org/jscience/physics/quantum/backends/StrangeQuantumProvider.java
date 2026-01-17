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

package org.jscience.physics.quantum.backends;

import java.util.HashMap;
import java.util.Map;

import org.jscience.physics.quantum.QuantumBackend;
import org.jscience.physics.quantum.QuantumContext;
import org.jscience.physics.quantum.QuantumGate;
import org.jscience.physics.quantum.QuantumGateType;
import org.redfx.strange.Program;

import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.Gate;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.X;
import org.redfx.strange.gate.Y;
import org.redfx.strange.gate.Z;
import org.redfx.strange.gate.Cnot;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

/**
 * Quantum Backend implementation using the Strange pure Java library.
 * 
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Dirac, P. A. M. (1930). <i>The Principles of Quantum Mechanics</i>. Oxford University Press.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StrangeQuantumProvider implements QuantumBackend {

    @Override
    public String getName() {
        return "Strange (Local)";
    }

    @Override
    public String getId() {
        return "strange";
    }

    @Override
    public String getDescription() {
        return "Pure Java quantum simulator relying on Strange.";
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.redfx.strange.Program");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Map<String, Integer> execute(QuantumContext context) {
        int numQubits = context.getRegisters().stream().mapToInt(r -> r.getSize()).sum();
        
        Program p = new Program(numQubits);
        
        for (QuantumGate gate : context.getGates()) {
            Step s = new Step();
            Gate strangeGate = convertGate(gate);
            if (strangeGate != null) {
                s.addGate(strangeGate);
                p.addStep(s);
            }
        }
        
        // Strange handles measurement automatically at the end for final state,
        // but if we have specific measurement gates, we might need to handle them.
        // For now, Strange's simple model executes the circuit.
        // JScience's QuantumGate has a type MEASURE.
        
        try {
            SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
            Result res = sqee.runProgram(p);
            
            // Result.getProbability() returns Complex[] representing the state vector.
            // We calculate |amplitude|^2 for each state to get the probability.
            org.redfx.strange.Complex[] probabilities = res.getProbability();
            
            Map<String, Integer> counts = new HashMap<>();
            int shots = 1024;
            java.util.Random rand = new java.util.Random();
            
            for (int i = 0; i < shots; i++) {
                double r = rand.nextDouble();
                double cumulativeProbability = 0.0;
                for (int j = 0; j < probabilities.length; j++) {
                    cumulativeProbability += probabilities[j].abssqr();
                    if (r <= cumulativeProbability) {
                        String binaryString = Integer.toBinaryString(j);
                        // Pad with leading zeros to match numQubits
                        int len = binaryString.length();
                        if (len < numQubits) {
                             StringBuilder sb = new StringBuilder();
                             for (int k = 0; k < numQubits - len; k++) sb.append('0');
                             sb.append(binaryString);
                             binaryString = sb.toString();
                        }
                        counts.put(binaryString, counts.getOrDefault(binaryString, 0) + 1);
                        break;
                    }
                }
            }
            
            return counts;
            
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    
    private Gate convertGate(QuantumGate gate) {
        QuantumGateType type = gate.getType();
        int[] targets = gate.getTargetQubits();
        
        switch (type) {
        case H:
            return new Hadamard(targets[0]);
        case X:
            return new X(targets[0]);
        case Y:
            return new Y(targets[0]);
        case Z:
            return new Z(targets[0]);
        case CX:
            // In Strange CNot(control, target)
            return new Cnot(targets[0], targets[1]);
        case MEASURE:
             return null; // Implicit in Strange for simple run? Or use Measurement gate if exists.
        default:
            return null;
        }
    }
}
