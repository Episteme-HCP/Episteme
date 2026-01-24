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

package org.jscience.economics;

/**
 * Constants and enumerations for economics and management.
 * <p>
 * Provides classifications for industry sectors, economic processes,
 * organizational departments, and management levels.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class EconomicsConstants {

    private EconomicsConstants() {}

    /**
     * Economic industry sectors (Three-sector model + extensions).
     */
    public enum IndustrySector {
        PRIMARY,   // Raw material extraction (farming, mining)
        SECONDARY, // Manufacturing and processing
        TERTIARY,  // Services
        QUATERNARY,// Information, IT, research
        QUINARY    // High-level decision making, non-profits
    }

    /**
     * Stages in the economic value chain.
     */
    public enum EconomicProcess {
        PRODUCTION, TRANSFORMATION, DISTRIBUTION, CONSUMPTION, RECYCLING
    }

    /**
     * Broad industry categories.
     */
    public enum IndustryCategory {
        TRANSPORT, HYGIENE, ALIMENTATION, HABITAT, TECHNOLOGY, 
        ENERGY, SOCIETY, FINANCE, EDUCATION, HEALTHCARE, OTHER
    }

    /**
     * Standard organizational departments.
     */
    public enum Department {
        HUMAN_RESOURCES, SALES, MARKETING, ACCOUNTING, FINANCE, 
        RESEARCH_AND_DEVELOPMENT, PRODUCTION, LOGISTICS, DISTRIBUTION, 
        PROCUREMENT, LEGAL, INFRASTRUCTURE, INFORMATION_TECHNOLOGY, 
        CUSTOMER_SERVICE, QUALITY_ASSURANCE
    }

    /**
     * Management levels or roles.
     */
    public enum ManagementLevel {
        OPERATIONAL, SUPERVISORY, MIDDLE_MANAGEMENT, STRATEGIC, EXECUTIVE, BOARD
    }

    /**
     * Market structures.
     */
    public enum MarketStructure {
        PERFECT_COMPETITION, MONOPOLISTIC_COMPETITION, OLIGOPOLY, MONOPOLY, MONOPSONY
    }
}
