/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology;

import java.time.LocalDate;
import org.jscience.biology.taxonomy.Species;

/**
 * Represents Homo sapiens - the human species.
 * <p>
 * Provides a predefined Species instance for humans with taxonomic information.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
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

    /** Blood types */
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
