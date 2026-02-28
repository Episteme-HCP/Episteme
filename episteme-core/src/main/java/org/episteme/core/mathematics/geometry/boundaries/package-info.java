/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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
 * Comprehensive boundary and region support for geometric modeling.
 * <p>
 * This package provides a complete framework for representing spatial boundaries
 * in 2D and 3D, including support for complex territorial shapes with enclaves
 * and exclaves.
 *
 * <h2>Core Interfaces</h2>
 * <ul>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.Boundary} - Base interface for all boundaries</li>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.Boundary2D} - 2D boundary interface (polygons)</li>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.Boundary3D} - 3D boundary interface (polyhedra)</li>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.BoundingBox} - Axis-aligned bounding box interface</li>
 * </ul>
 *
 * <h2>2D Boundaries</h2>
 * <ul>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.ConvexPolygon2D} - Convex polygon</li>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.CompositeBoundary2D} - Multiple regions with enclaves/exclaves</li>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.FuzzyBoundary2D} - Fuzzy membership boundaries</li>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.BoundingBox2D} - 2D axis-aligned box</li>
 * </ul>
 *
 * <h2>3D Boundaries</h2>
 * <ul>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.ConvexPolyhedron3D} - Convex polyhedron</li>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.CompositeBoundary3D} - Multiple volumes with cavities</li>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.BoundingBox3D} - 3D axis-aligned box</li>
 * </ul>
 *
 * <h2>Special Boundaries</h2>
 * <ul>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.PointBoundary} - Zero-dimensional point</li>
 * </ul>
 *
 * <h2>Utilities</h2>
 * <ul>
 *   <li>{@link org.episteme.core.mathematics.geometry.boundaries.BoundaryConverter} - 2D↔3D conversion</li>
 * </ul>
 *
 * <h2>Enclave/Exclave Support</h2>
 * <p>
 * The composite boundary classes support complex territorial configurations:
 * <ul>
 *   <li><b>Exclaves</b> - Multiple disjoint regions belonging to the same entity</li>
 *   <li><b>Enclaves</b> - Holes/exclusions within a region</li>
 *   <li><b>Third-degree cases</b> - Enclaves within enclaves (counter-enclaves)</li>
 * </ul>
 * <p>
 * Example: Modeling Baarle-Hertog/Baarle-Nassau (Belgium/Netherlands border):
 * <pre>{@code
 * CompositeBoundary2D belgium = new CompositeBoundary2D();
 * belgium.addInclusion(mainBelgiumTerritory);
 * belgium.addInclusion(baarleHertogExclave1);
 * 
 * // Dutch enclave within Belgian exclave
 * CompositeBoundary2D baarleWithEnclave = new CompositeBoundary2D(baarleHertogExclave2);
 * baarleWithEnclave.addExclusion(dutchCounterEnclave);
 * belgium.addInclusion(baarleWithEnclave);
 * }</pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
package org.episteme.core.mathematics.geometry.boundaries;

