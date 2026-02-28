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

package org.episteme.social.psychology;

/**
 * Constants and enumerations for psychology and behavioral sciences.
 * <p>
 * Provides classifications for emotions, Maslow's hierarchy of needs, 
 * personality models (Big Five, Myers-Briggs/Keirsey), and sleep stages.
 */
public final class PsychologyConstants {

    private PsychologyConstants() {}

    /**
     * Primary and secondary emotions.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Emotion {
        ANGER, RAGE, SORROW, SADNESS, JOY, HAPPINESS, DISGUST, 
        ACCEPTANCE, ANTICIPATION, SURPRISE, FEAR, TERROR, 
        BOREDOM, ENVY, GUILT, HATE, HOPE, JEALOUSY, LOVE, 
        REGRET, REMORSE, SHAME, NONE, UNKNOWN
    }

    /**
     * Maslow's Hierarchy of Needs.
     */
    public enum NeedLevel {
        PHYSIOLOGICAL, // Warmth, shelter, food
        SECURITY,      // Protection from danger
        SOCIAL,        // Love, friendship
        ESTEEM,        // Self-respect, personal worth (Ego)
        SELF_ACTUALIZATION // Full potential
    }

    /**
     * The Big Five personality traits (Five Factor Model).
     */
    public enum BigFiveTrait {
        OPENNESS, CONSCIENTIOUSNESS, EXTRAVERSION, AGREEABLENESS, NEUROTICISM
    }

    /**
     * Myers-Briggs/Keirsey personality dimensions.
     */
    public enum PersonalityDimension {
        EXTRAVERSION_VS_INTROVERSION,
        SENSING_VS_INTUITION,
        THINKING_VS_FEELING,
        JUDGING_VS_PERCEIVING
    }

    /**
     * Stages of sleep.
     */
    public enum SleepStage {
        AWAKE, 
        NREM1, // Light sleep
        NREM2, // Sleep spindles
        NREM3, // Deep sleep (Slow wave)
        REM    // Rapid Eye Movement
    }
}

