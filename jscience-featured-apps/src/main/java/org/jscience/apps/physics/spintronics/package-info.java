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

/**
 * Advanced Spintronics Simulation Module.
 * 
 * <p>
 * This package provides a comprehensive framework for simulating spintronic devices
 * and phenomena, from basic magnetoresistance to cutting-edge neuromorphic computing.
 * </p>
 * 
 * <h2>Core Physics Models</h2>
 * <ul>
 *   <li><b>Magnetoresistance</b>: GMR (Valet-Fert), TMR (Jullière)</li>
 *   <li><b>Magnetization Dynamics</b>: LLG equation with Euler/Heun integrators, stochastic thermal noise</li>
 *   <li><b>Spin Torques</b>: STT (Slonczewski), SOT (SHE, Rashba, OHE)</li>
 *   <li><b>Thermal Effects</b>: Spin Seebeck, Spin Pumping</li>
 * </ul>
 * 
 * <h2>Device Structures</h2>
 * <ul>
 *   <li>{@link org.jscience.apps.physics.spintronics.SpinValve} - GMR/TMR stacks with SAF support</li>
 *   <li>{@link org.jscience.apps.physics.spintronics.FerrimagneticLayer} - Two-sublattice dynamics (GdFeCo)</li>
 *   <li>{@link org.jscience.apps.physics.spintronics.StochasticMTJ} - p-bits for probabilistic computing</li>
 * </ul>
 * 
 * <h2>Micromagnetics</h2>
 * <ul>
 *   <li>{@link org.jscience.apps.physics.spintronics.Micromagnetics1D} - Domain wall dynamics</li>
 *   <li>{@link org.jscience.apps.physics.spintronics.Micromagnetics2D} - Skyrmions with DMI</li>
 * </ul>
 * 
 * <h2>Neuromorphic Computing</h2>
 * <ul>
 *   <li>{@link org.jscience.apps.physics.spintronics.PBitNetwork} - Boltzmann machines, simulated annealing</li>
 * </ul>
 * 
 * <h2>Example Usage</h2>
 * <pre>{@code
 * // Create a spin valve
 * FerromagneticLayer pinned = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(5e-9), true);
 * FerromagneticLayer free = new FerromagneticLayer(SpintronicMaterial.PERMALLOY, Real.of(5e-9), false);
 * SpinValve sv = new SpinValve(pinned, SpintronicMaterial.COPPER, Real.of(3e-9), free);
 * 
 * // Calculate GMR
 * Real resistance = GMREffect.valetFertResistance(sv);
 * 
 * // Run LLG dynamics
 * Real[] hEff = {Real.of(5000), Real.ZERO, Real.ZERO};
 * Real[] newM = SpinTransport.stepLLGHeun(free, hEff, Real.of(1e-12), Real.of(0.01), Real.of(1.76e11));
 * 
 * // Simulate SOT switching
 * SpinOrbitTorque sot = new SpinOrbitTorque(SpinOrbitTorque.HeavyMetal.PLATINUM, Real.of(5e-9));
 * Real[] torque = sot.calculateDampingLikeTorque(Real.of(1e12), free, new Real[]{Real.ONE, Real.ZERO, Real.ZERO});
 * }</pre>
 * 
 * <h2>References</h2>
 * <p>Key publications implemented in this module:</p>
 * <ul>
 *   <li>Valet, T. & Fert, A. (1993). PRB 48, 7099 - CPP-GMR theory</li>
 *   <li>Slonczewski, J.C. (1996). JMMM 159, L1 - STT theory</li>
 *   <li>Liu, L. et al. (2012). Science 336, 555 - SOT switching</li>
 *   <li>Fert, A. et al. (2017). Nat. Rev. Mat. 2, 17031 - Skyrmions</li>
 *   <li>Camsari, K.Y. et al. (2017). PRX 7, 031014 - p-bits</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
package org.jscience.apps.physics.spintronics;
