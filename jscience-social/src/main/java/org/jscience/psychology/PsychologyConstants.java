package org.jscience.psychology;

/**
 * A class representing useful constants in psychology.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class PsychologyConstants extends Object {
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    //human and animal emotions, note that only human cries and smile is usually considered to be a human only feature
    //see http://en.wikipedia.org/wiki/List_of_emotions
    /** DOCUMENT ME! */
    public final static int NONE = 1;

    /** DOCUMENT ME! */
    public final static int ANGER = 2; //anger (or rage, which can be directed to the self or others)

    /** DOCUMENT ME! */
    public final static int SORROW = 3; //sorrow (or sadness, or grief or depression [which some people think of a separate emotion-see depression)

    /** DOCUMENT ME! */
    public final static int JOY = 4; //joy (happiness, glee, gladness)

    /** DOCUMENT ME! */
    public final static int DISGUST = 5; //disgust

    /** DOCUMENT ME! */
    public final static int ACCEPTANCE = 6; //acceptance

    /** DOCUMENT ME! */
    public final static int ANTICIPATION = 7; //anticipation

    /** DOCUMENT ME! */
    public final static int SURPRISE = 8; //surprise

    /** DOCUMENT ME! */
    public final static int FEAR = 9; //fear (or terror, shock, phobia)

    //one may also consider Boredom, Envy, Guilt, Hate, Hope, Jealousy, Love, Regret, Remorse, Sadness, Shame
// may be we should have a class for emotions

    //may be we should include Maslow's Hierarchy of Needs like seen at http://www.deepermind.com/20maslow.htm
    //here or in org.jscience.biology.BiologyConstants
    /** DOCUMENT ME! */
    public final static int PHYSIOLOGICAL_NEEDS = 1; //Physiological Needs (warmth, shelter, food)

    /** DOCUMENT ME! */
    public final static int SECURITY_NEEDS = 2; //Security Needs (protection from danger)

    /** DOCUMENT ME! */
    public final static int SOCIAL_NEEDS = 4; // Social Needs (love, friendship, comradeship)

    /** DOCUMENT ME! */
    public final static int EGO_NEEDS = 8; // Ego Needs (self respect, personal worth, autonomy)

    /** DOCUMENT ME! */
    public final static int SELF_NEEDS = 16; //Self Actualization Needs (full potential)

    //http://en.wikipedia.org/wiki/Personality
    //Cattell
    /** DOCUMENT ME! */
    public final static int PERSONALITY_EXTROVERSION = 1; //Extroversion (i.e., "extroversion vs. introversion" above; outgoing and physical-stimulation-oriented vs. quiet and physical-stimulation-averse)

    /** DOCUMENT ME! */
    public final static int PERSONALITY_NEUROTICISM = 2; //Neuroticism (i.e., emotional stability; calm, unperturbable, optimistic vs. emotionally reactive, prone to negative emotions)

    /** DOCUMENT ME! */
    public final static int PERSONALITY_AGREEABLENESS = 4; //Agreeableness (i.e., affable, friendly, conciliatory vs. aggressive, dominant, disagreeable)

    /** DOCUMENT ME! */
    public final static int PERSONALITY_CONSCIENTIOUSNESS = 8; //Conscientiousness (i.e., dutiful, planful, and orderly vs. spontaneous, flexible, and unreliable)

    /** DOCUMENT ME! */
    public final static int PERSONALITY_OPENNESS = 16; //Openness to experience (i.e., open to new ideas and change vs. traditional and staid)

    //Keirsey
    /** DOCUMENT ME! */
    public final static int PERSONALITY_EXTRAVERSION_INTROVERSION = 1; //Extroversion vs. Introversion (see above)

    /** DOCUMENT ME! */
    public final static int PERSONALITY_INTUITION_SENSING = 2; //Intuition vs. Sensing (trust in conceptual/abstract models of reality versus concrete sensory-oriented facts)

    /** DOCUMENT ME! */
    public final static int PERSONALITY_THINKING_FEELING = 4; //Thinking vs. Feeling (thinking as the prime-mover in decision-making vs. feelings as the prime-mover in decision-making)

    /** DOCUMENT ME! */
    public final static int PERSONALITY_PERCEIVEING_JUDGING = 8; //Perceiving vs. Judging (desire to perceive events vs. desire to have things done so judgements can be made)

    //Stages of sleep
    /** DOCUMENT ME! */
    public final static int AWAKE = 1;

    /** DOCUMENT ME! */
    public final static int NREM1 = 2;

    /** DOCUMENT ME! */
    public final static int NREM2 = 3;

    /** DOCUMENT ME! */
    public final static int NREM3 = 4;

    /** DOCUMENT ME! */
    public final static int NREM4 = 5;

    /** DOCUMENT ME! */
    public final static int REM = 6;
}
