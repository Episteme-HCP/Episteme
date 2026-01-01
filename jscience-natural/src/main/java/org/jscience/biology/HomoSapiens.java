/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.biology;

import org.jscience.biology.taxonomy.Species;

/**
 * Represents Homo sapiens - the human species.
 * <p>
 * Provides a predefined Species instance for humans with taxonomic information.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class HomoSapiens {

    private HomoSapiens() {
    } // Utility class

    /** The human species */
    public static final Species SPECIES = createSpecies();

    private static Species createSpecies() {
        Species homo = new Species("Human", "Homo sapiens");
        homo.addAttribute("kingdom", "Animalia");
        homo.addAttribute("phylum", "Chordata");
        homo.addAttribute("class", "Mammalia");
        homo.addAttribute("order", "Primates");
        homo.addAttribute("family", "Hominidae");
        homo.addAttribute("genus", "Homo");
        homo.addAttribute("species", "sapiens");
        homo.addAttribute("chromosomes", "46");
        homo.addAttribute("genome_size_bp", "3200000000");
        homo.addAttribute("average_lifespan_years", "79");
        homo.addAttribute("gestation_days", "280");
        return homo;
    }

    /** Average human body temperature in Celsius */
    public static final double BODY_TEMPERATURE_CELSIUS = 37.0;

    /** Average human heart rate (beats per minute) */
    public static final int RESTING_HEART_RATE_BPM = 72;

    /** Average adult height in meters */
    public static final double AVERAGE_HEIGHT_MALE_M = 1.75;
    public static final double AVERAGE_HEIGHT_FEMALE_M = 1.62;

    /** Average adult weight in kg */
    public static final double AVERAGE_WEIGHT_MALE_KG = 70.0;
    public static final double AVERAGE_WEIGHT_FEMALE_KG = 58.0;

    /** Number of chromosomes */
    public static final int CHROMOSOME_COUNT = 46;

    /**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum BloodType {
        A_POSITIVE("A+"), A_NEGATIVE("A-"),
        B_POSITIVE("B+"), B_NEGATIVE("B-"),
        AB_POSITIVE("AB+"), AB_NEGATIVE("AB-"),
        O_POSITIVE("O+"), O_NEGATIVE("O-");

        private final String symbol;

        BloodType(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}


