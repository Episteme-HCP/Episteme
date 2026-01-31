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

package org.jscience.social.law;

import java.util.HashMap;
import java.util.Map;

/**
 * Models international treaties and their ratification status across different countries.
 * Provides metrics for global adoption and binding status.
 * */
public final class InternationalTreatyModel {

    private InternationalTreatyModel() {
        // Utility class
    }

    /**
     * Represents the formal status of a country regarding an international treaty.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum RatificationStatus {
        SIGNED, RATIFIED, ACCEDED, NOT_A_PARTY
    }

    /**
     * Represents an international treaty and the status of its member parties.
     */
    public record Treaty(
        String name,
        String abbreviation,
        int yearAdopted,
        Map<String, RatificationStatus> partyStatus
    ) {}

    /**
     * Checks if a country (by code) is legally bound by the specified treaty.
     * Binding status is associated with RATIFIED or ACCEDED states.
     *
     * @param treaty the treaty to check
     * @param countryCode the ISO or internal country code
     * @return true if the country is bound
     */
    public static boolean isBound(Treaty treaty, String countryCode) {
        if (treaty == null || countryCode == null) {
            return false;
        }
        RatificationStatus status = treaty.partyStatus().getOrDefault(countryCode, RatificationStatus.NOT_A_PARTY);
        return status == RatificationStatus.RATIFIED || status == RatificationStatus.ACCEDED;
    }

    /**
     * Calculates the "Global Adoption Rate" of a treaty.
     *
     * @param treaty the treaty to analyze
     * @param totalCountries the total number of sovereign states considered
     * @return the ratio of binding countries to the total number of countries
     */
    public static double adoptionRate(Treaty treaty, int totalCountries) {
        if (treaty == null || totalCountries <= 0) {
            return 0.0;
        }
        long count = treaty.partyStatus().values().stream()
            .filter(s -> s == RatificationStatus.RATIFIED || s == RatificationStatus.ACCEDED)
            .count();
        return (double) count / totalCountries;
    }

    /**
     * Returns a model representing the Universal Declaration of Human Rights.
     * @return the UDHR treaty model
     */
    public static Treaty udhr() {
        return new Treaty("Universal Declaration of Human Rights", "UDHR", 1948, new HashMap<>());
    }
}

