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

package org.jscience.psychology;

import java.io.Serializable;

/**
 * Common constants used across psychological and behavioral models.
 * Includes definitions for emotions, needs, personality traits, and sleep states.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class PsychologyConstants implements Serializable {

    private static final long serialVersionUID = 1L;

    private PsychologyConstants() {
        // Prevent instantiation
    }

    /** Unspecified or unknown state. */
    public final static int UNKNOWN = 0;

    // Primary Emotions
    /** State of emotional neutralness. */
    public final static int NONE = 1;
    /** Strong feeling of annoyance, displeasure, or hostility. */
    public final static int ANGER = 2;
    /** Feeling of deep distress caused by loss or disappointment. */
    public final static int SORROW = 3;
    /** Feeling of great pleasure and happiness. */
    public final static int JOY = 4;
    /** Feeling of revulsion or strong disapproval. */
    public final static int DISGUST = 5;
    /** Consent to receive or undertake something. */
    public final static int ACCEPTANCE = 6;
    /** Expectation or prediction of future events. */
    public final static int ANTICIPATION = 7;
    /** Reaction to an unexpected event. */
    public final static int SURPRISE = 8;
    /** Unpleasant emotion caused by a threat of danger. */
    public final static int FEAR = 9;

    // Maslow's Hierarchy of Needs
    /** Basic survival needs (food, water, shelter). */
    public final static int PHYSIOLOGICAL_NEEDS = 1;
    /** Stability and protection from physical or emotional harm. */
    public final static int SECURITY_NEEDS = 2;
    /** Belonging, love, and social connection. */
    public final static int SOCIAL_NEEDS = 4;
    /** Achievement, recognition, and self-respect. */
    public final static int EGO_NEEDS = 8;
    /** Personal growth and reaching full potential. */
    public final static int SELF_NEEDS = 16;

    // BIG FIVE Personality Traits (Five Factor Model)
    /** Tendency to be outgoing, energetic, and talkative. */
    public final static int PERSONALITY_EXTROVERSION = 1;
    /** Tendency to experience negative emotions (emotional stability). */
    public final static int PERSONALITY_NEUROTICISM = 2;
    /** Tendency to be compassionate and cooperative. */
    public final static int PERSONALITY_AGREEABLENESS = 4;
    /** Tendency to be organized, dependable, and disciplined. */
    public final static int PERSONALITY_CONSCIENTIOUSNESS = 8;
    /** Tendency to be curious and open to new experiences. */
    public final static int PERSONALITY_OPENNESS = 16;

    // Jungian/MBTI Personality Dimensions
    /** Extraversion vs. Introversion. */
    public final static int PERSONALITY_EXTRAVERSION_INTROVERSION = 1;
    /** Intuition vs. Sensing. */
    public final static int PERSONALITY_INTUITION_SENSING = 2;
    /** Thinking vs. Feeling. */
    public final static int PERSONALITY_THINKING_FEELING = 4;
    /** Perceiving vs. Judging. */
    public final static int PERSONALITY_PERCEIVEING_JUDGING = 8;

    // Sleep Stages
    /** Fully conscious and alert state. */
    public final static int AWAKE = 1;
    /** Non-Rapid Eye Movement Stage 1 (Light sleep). */
    public final static int NREM1 = 2;
    /** Non-Rapid Eye Movement Stage 2 (Onset of sleep). */
    public final static int NREM2 = 3;
    /** Non-Rapid Eye Movement Stage 3 (Deep sleep). */
    public final static int NREM3 = 4;
    /** Non-Rapid Eye Movement Stage 4 (Very deep sleep). */
    public final static int NREM4 = 5;
    /** Rapid Eye Movement (Dreaming sleep). */
    public final static int REM = 6;
}
