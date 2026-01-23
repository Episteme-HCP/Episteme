package org.jscience.linguistics;

import org.jscience.util.Named;

import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.Clip;


/**
 * The Language class provides a placeholder for a language.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://en.wikipedia.org/wiki/Animal_language
//These are the properties of human language that are argued to separate it from animal communication:
//'Arbitrariness:' There is no relationship between a sound or sign and its meaning. 
//'Cultural transmission:' Language is passed from one language user to the next, consciously or unconsciously. 
//'Discreteness:' Language is composed of discrete units that are used in combination to create meaning. 
//'Displacement:' Languages can be used to communicate ideas about things that are not in the immediate vicinity either spatially or temporally. 
//'Duality:' Language works on two levels at once, a surface level and a semantic (meaningful) level. 
//'Metalinguistics:' Ability to discuss language itself. 
//'Productivity:' A finite number of units can be used to create an infinite number of utterances. 
public class Language extends Object implements Named {
    private final String isoCode;
    private final String name;
    private final Set<Grapheme> graphemes = new HashSet<>();
    private final Set<Phoneme> phonemes = new HashSet<>();

    public Language(String isoCode, String name) {
        if (isoCode == null || name == null) {
            throw new IllegalArgumentException("Language code and name cannot be null");
        }
        this.isoCode = isoCode.toLowerCase();
        this.name = name;
    }

    @Deprecated
    public Language(String name) {
        this("und", name); // "und" for undetermined
    }

    public Language(String isoCode, String name, String alphabet) {
        this(isoCode, name);
        addGraphemes(alphabet);
    }

    public String getName() {
        return name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param grapheme DOCUMENT ME!
     */
    protected void addGrapheme(Grapheme grapheme) {
        graphemes.add(grapheme);
    }

    /**
     * DOCUMENT ME!
     *
     * @param character DOCUMENT ME!
     */
    public void addGrapheme(char character) {
        graphemes.add(new Grapheme(this, character));
    }

    /**
     * DOCUMENT ME!
     *
     * @param characters DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addGraphemes(String characters) {
        if ((characters != null) && (characters.length() > 0)) {
            for (int i = 0; i < characters.length(); i++) {
                graphemes.add(new Grapheme(this, characters.charAt(i)));
            }
        } else {
            throw new IllegalArgumentException(
                "characters can't be null or empty.");
        }
    }

    public Set<Grapheme> getGraphemes() {
        return Collections.unmodifiableSet(graphemes);
    }

    /**
     * DOCUMENT ME!
     *
     * @param phoneme DOCUMENT ME!
     */
    protected void addPhoneme(Phoneme phoneme) {
        phonemes.add(phoneme);
    }

    /**
     * DOCUMENT ME!
     *
     * @param clip DOCUMENT ME!
     */
    public void addPhoneme(Clip clip) {
        phonemes.add(new Phoneme(this, clip));
    }

    /**
     * DOCUMENT ME!
     *
     * @param clips DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addPhonemes(Clip[] clips) {
        if ((clips != null) && (clips.length > 0)) {
            for (int i = 0; i < clips.length; i++) {
                phonemes.add(new Phoneme(this, clips[i]));
            }
        } else {
            throw new IllegalArgumentException("clips can't be null or empty.");
        }
    }

    public Set<Phoneme> getPhonemes() {
        return Collections.unmodifiableSet(phonemes);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, isoCode);
    }
}
