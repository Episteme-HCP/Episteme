package org.jscience.arts.theatrical;

import java.util.Iterator;
import java.util.Vector;


/**
 * A class representing an act (a serie of scenes) in a show (opera,
 * play...).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Act extends Object {
    /** DOCUMENT ME! */
    private Vector scenes;

    //all the elements of the Vector should be scenes.
    /**
     * Creates a new Act object.
     *
     * @param scenes DOCUMENT ME!
     */
    public Act(Vector scenes) {
        Iterator iterator;
        boolean valid;

        if (scenes != null) {
            iterator = scenes.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Scene;
            }

            if (valid) {
                this.scenes = scenes;
            } else {
                throw new IllegalArgumentException(
                    "An Act must consist only of Scenes.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Act constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getScenes() {
        return scenes;
    }
}
