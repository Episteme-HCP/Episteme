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

package org.episteme.social.history;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Time;


import org.episteme.social.history.time.FuzzyTimePoint;
import org.episteme.social.history.time.FuzzyTimeInterval;

/**
 * Constants and reference points for historical and cosmological timelines.
 * <p>
 * Provides estimated ages and durations for major events in universal and 
 * planetary history, using high-precision {@link Real} and {@link Quantity}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class HistoryConstants {

    private HistoryConstants() {}

    /** Unix Epoch (January 1, 1970). Reference time point 0. */
    public static final Real UNIX_EPOCH = Real.ZERO;

    /** Number of seconds in a standard Julian year. */
    public static final Quantity<Time> JULIAN_YEAR = Quantities.create(365.25, Units.DAY);

    /** Estimated age of the Universe (v2024 estimate: ~13.787 billion years). */

    public static final Quantity<Time> UNIVERSE_AGE = Quantities.create(Real.of("13.787e9"), Units.YEAR);

    /** Estimated age of the Solar System (~4.571 billion years). */
    public static final Quantity<Time> SOLAR_SYSTEM_AGE = Quantities.create(Real.of("4.571e9"), Units.YEAR);

    /** Estimated age of the Earth (~4.54 billion years). */
    public static final Quantity<Time> EARTH_AGE = Quantities.create(Real.of("4.54e9"), Units.YEAR);

    /** Estimated time of the Big Bang relative to Unix Epoch. */
    public static final Real BIG_BANG_TIMESTAMP = UNIX_EPOCH.subtract(
            (Real) UNIVERSE_AGE.to(Units.SECOND).getValue());

    /** Time since the formation of Earth relative to Unix Epoch. */
    public static final Real EARTH_FORMATION_TIMESTAMP = UNIX_EPOCH.subtract(
            (Real) EARTH_AGE.to(Units.SECOND).getValue());

    // ========== Geological Eras ==========

    public static final GeologicalEra HADEAN = new GeologicalEra("Hadean", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(4600_000_000L), FuzzyTimePoint.bce(4000_000_000L)));
    
    public static final GeologicalEra ARCHEAN = new GeologicalEra("Archean", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(4000_000_000L), FuzzyTimePoint.bce(2500_000_000L)));
    
    public static final GeologicalEra PROTEROZOIC = new GeologicalEra("Proterozoic", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(2500_000_000L), FuzzyTimePoint.bce(541_000_000L)));
    
    public static final GeologicalEra PHANEROZOIC = new GeologicalEra("Phanerozoic", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(541_000_000L), FuzzyTimePoint.of(1970))); // Up to present-ish

    public static final GeologicalEra PALEOZOIC = new GeologicalEra("Paleozoic", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(541_000_000L), FuzzyTimePoint.bce(252_000_000L)));
    
    public static final GeologicalEra MESOZOIC = new GeologicalEra("Mesozoic", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(252_000_000L), FuzzyTimePoint.bce(66_000_000L)));
    
    public static final GeologicalEra CENOZOIC = new GeologicalEra("Cenozoic", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(66_000_000L), FuzzyTimePoint.of(1970)));

    public static final GeologicalEra TRIASSIC = new GeologicalEra("Triassic", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(252_000_000L), FuzzyTimePoint.bce(201_000_000L)));
    
    public static final GeologicalEra JURASSIC = new GeologicalEra("Jurassic", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(201_000_000L), FuzzyTimePoint.bce(145_000_000L)));
    
    public static final GeologicalEra CRETACEOUS = new GeologicalEra("Cretaceous", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(145_000_000L), FuzzyTimePoint.bce(66_000_000L)));

    public static final GeologicalEra PALEOGENE = new GeologicalEra("Paleogene", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(66_000_000L), FuzzyTimePoint.bce(23_000_000L)));

    public static final GeologicalEra NEOGENE = new GeologicalEra("Neogene", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(23_000_000L), FuzzyTimePoint.bce(2_580_000L)));

    public static final GeologicalEra QUATERNARY = new GeologicalEra("Quaternary", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(2_580_000L), FuzzyTimePoint.of(1970)));

    public static final GeologicalEra PLEISTOCENE = new GeologicalEra("Pleistocene", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(2_580_000L), FuzzyTimePoint.bce(11_700L)));

    public static final GeologicalEra HOLOCENE = new GeologicalEra("Holocene", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(11_700L), FuzzyTimePoint.of(1970)));

    // ========== Common Historical Eras ==========

    /** The Renaissance (c. 1400 - c. 1600). */
    public static final HistoricalEra RENAISSANCE = new HistoricalEra("Renaissance", 
            new FuzzyTimeInterval(FuzzyTimePoint.circa(1400), FuzzyTimePoint.circa(1600)));

    /** The Industrial Revolution (c. 1760 - c. 1840). */
    public static final HistoricalEra INDUSTRIAL_REVOLUTION = new HistoricalEra("Industrial Revolution", 
            new FuzzyTimeInterval(FuzzyTimePoint.circa(1760), FuzzyTimePoint.circa(1840)));

    /** Ancient Greece (c. 800 BCE - 31 BCE). */
    public static final HistoricalEra ANCIENT_GREECE = new HistoricalEra("Ancient Greece", 
            new FuzzyTimeInterval(FuzzyTimePoint.circaBce(800), FuzzyTimePoint.bce(31)));

    /** Roman Empire (27 BCE - 476 CE). */
    public static final HistoricalEra ROMAN_EMPIRE = new HistoricalEra("Roman Empire", 
            new FuzzyTimeInterval(FuzzyTimePoint.bce(27), FuzzyTimePoint.of(476)));

    /** Middle Ages (c. 500 - c. 1500). */
    public static final HistoricalEra MIDDLE_AGES = new HistoricalEra("Middle Ages", 
            new FuzzyTimeInterval(FuzzyTimePoint.circa(500), FuzzyTimePoint.circa(1500)));

    // ========== Major Historical Events ==========

    public static final HistoricalEvent GREAT_PYRAMID = new HistoricalEvent("Great Pyramid of Giza", 
            FuzzyTimePoint.circaBce(2560), EventCategory.CULTURAL);

    public static final HistoricalEvent FOUNDING_OF_ROME = new HistoricalEvent("Founding of Rome", 
            FuzzyTimePoint.bce(753), EventCategory.POLITICAL);

    public static final HistoricalEvent BATTLE_OF_MARATHON = new HistoricalEvent("Battle of Marathon", 
            FuzzyTimePoint.bce(490), EventCategory.MILITARY);

    public static final HistoricalEvent FALL_OF_ROME = new HistoricalEvent("Fall of Western Roman Empire", 
            FuzzyTimePoint.of(476), EventCategory.POLITICAL);

    public static final HistoricalEvent FRENCH_REVOLUTION = new HistoricalEvent("French Revolution", 
            FuzzyTimePoint.of(1789), EventCategory.POLITICAL);

    public static final HistoricalEvent WORLD_WAR_I = new HistoricalEvent("World War I", 
            FuzzyTimePoint.of(1914), EventCategory.MILITARY);

    public static final HistoricalEvent WORLD_WAR_II = new HistoricalEvent("World War II", 
            FuzzyTimePoint.of(1939), EventCategory.MILITARY);

    public static final HistoricalEvent MOON_LANDING = new HistoricalEvent("Apollo 11 Moon Landing", 
            FuzzyTimePoint.of(1969, 7, 20), EventCategory.SCIENTIFIC);

    // ========== Timelines ==========

    /** Major events in world history. */
    public static final HistoricalTimeline WORLD_HISTORY = new HistoricalTimeline("World History")
            .addEvent(GREAT_PYRAMID)
            .addEvent(FOUNDING_OF_ROME)
            .addEvent(BATTLE_OF_MARATHON)
            .addEvent(FALL_OF_ROME)
            .addEvent(FRENCH_REVOLUTION)
            .addEvent(WORLD_WAR_I)
            .addEvent(WORLD_WAR_II)
            .addEvent(MOON_LANDING);

    /** Major events in ancient history. */
    public static final HistoricalTimeline ANCIENT_HISTORY = new HistoricalTimeline("Ancient History")
            .addEvent(GREAT_PYRAMID)
            .addEvent(FOUNDING_OF_ROME)
            .addEvent(BATTLE_OF_MARATHON)
            .addEvent(FALL_OF_ROME);

}

