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

package org.jscience.politics;

/**
 * Constants and enumerations for political science and international relations.
 * <p>
 * Provides classifications for governance systems, political ideologies, 
 * development status, and ISO country codes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class PoliticsConstants {

    private PoliticsConstants() {}

    /** Unknown or unclassified value. */
    public static final int UNKNOWN = 0;

    /**
     * Development status of a nation.
     */
    public enum DevelopmentStatus {
        UNDERDEVELOPED, DEVELOPING, EMERGING, DEVELOPED
    }

    /**
     * Forms of government.
     */
    public enum GovernmentForm {
        ANARCHISM, AUTOCRACY, DEMOCRACY, OLIGARCHY, REPUBLIC, 
        THEOCRACY, MONARCHY, DICTATORSHIP, TOTALITARIANISM, 
        PARLIAMENTARY_SYSTEM, PRESIDENTIAL_SYSTEM, SEMI_PRESIDENTIAL
    }

    /**
     * Political spectrum orientation.
     */
    public enum PoliticalSpectrum {
        FAR_LEFT, LEFT_WING, CENTER_LEFT, CENTER, CENTER_RIGHT, RIGHT_WING, FAR_RIGHT
    }

    /**
     * Political ideologies.
     */
    public enum Ideology {
        LIBERALISM, CONSERVATISM, SOCIALISM, COMMUNISM, FASCISM, 
        LIBERTARIANISM, ANARCHISM, NATIONALISM, GREEN_POLITICS
    }

    /**
     * Common ISO 3166-1 alpha-2 country codes.
     * (Partial list of major countries, see complete ISO standards for full implementation).
     */
    public enum CountryCode {
        AF("Afghanistan"), AL("Albania"), DZ("Algeria"), AS("American Samoa"), 
        AD("Andorra"), AO("Angola"), AR("Argentina"), AM("Armenia"), 
        AU("Australia"), AT("Austria"), AZ("Azerbaijan"), BE("Belgium"), 
        BR("Brazil"), CA("Canada"), CH("Switzerland"), CN("China"), 
        DE("Germany"), ES("Spain"), FR("France"), GB("United Kingdom"), 
        IN("India"), IT("Italy"), JP("Japan"), RU("Russian Federation"), 
        US("United States of America");

        private final String englishName;

        CountryCode(String name) {
            this.englishName = name;
        }

        public String getEnglishName() {
            return englishName;
        }
    }
}
