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
 * Analytical Information Markup Language (AnIML) loader for JScience.
 * <p>
 * This package provides classes for parsing AnIML files containing spectroscopic,
 * chromatographic, and other analytical chemistry data. AnIML is an ASTM standard
 * (E2077) for representing analytical instrument data.
 * </p>
 * <p>
 * <b>Key Classes:</b>
 * <ul>
 *   <li>{@link org.jscience.natural.chemistry.loaders.animl.AnIMLReader} - Main parser class</li>
 *   <li>{@link org.jscience.natural.chemistry.loaders.animl.AnIMLDocument} - Parsed document</li>
 *   <li>{@link org.jscience.natural.chemistry.loaders.animl.AnIMLExperimentStep} - Experiment data</li>
 *   <li>{@link org.jscience.natural.chemistry.loaders.animl.AnIMLSeriesData} - Numerical series</li>
 * </ul>
 * </p>
 * <p>
 * <b>Supported Analytical Techniques:</b>
 * <ul>
 *   <li>Spectroscopy: UV-Vis, IR, NMR, MS</li>
 *   <li>Chromatography: GC, HPLC, IC</li>
 *   <li>Electrochemistry</li>
 *   <li>Thermal analysis: DSC, TGA</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://www.astm.org/e2077-22.html">ASTM E2077 AnIML Standard</a>
 */
package org.jscience.natural.chemistry.loaders.animl;

