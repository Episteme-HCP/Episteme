package org.jscience.arts.theatrical;

import org.jscience.arts.musical.Composition;


/**
 * A class representing a scene that takes place in a show, with a text, a
 * music and body movements. In movies, "scenes" are further described in
 * terms of shots (a view from a camera). Shots are not provided here. Treat
 * them as scenes and scenes as acts (it is hard to tell when watching a movie
 * or even a play where an act really begins unless you are told).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Scene extends Object {
    /** DOCUMENT ME! */
    private String text;

    /** DOCUMENT ME! */
    private Composition composition;

    /** DOCUMENT ME! */
    private Choregraphy choregraphy; //or actors movements

/**
     * Creates a new Scene object.
     *
     * @param text DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Scene(String text) {
        if (text != null) {
            this.text = text;
            this.composition = null;
            this.choregraphy = null;
        } else {
            throw new IllegalArgumentException(
                "The Scene constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setText(String text) {
        if (text != null) {
            this.text = text;
        } else {
            throw new IllegalArgumentException("The text can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Composition getComposition() {
        return composition;
    }

    //null allowed
    /**
     * DOCUMENT ME!
     *
     * @param composition DOCUMENT ME!
     */
    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Choregraphy getChoregraphy() {
        return choregraphy;
    }

    //it is a good idea to match the number of poses with the corresponding note events
    /**
     * DOCUMENT ME!
     *
     * @param choregraphy DOCUMENT ME!
     */
    public void setChoregraphy(Choregraphy choregraphy) {
        this.choregraphy = choregraphy;
    }
}
