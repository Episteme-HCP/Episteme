package org.jscience.economics.resources;

/**
 * A class representing something that can be eaten or drunk.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also account for drinks
//although most food is dead creatures, food can be made of Minerals (salt for example),
//but also of pure synthetics chemicals (artifacts)
//some people are also capable of eating almost everything (car, tv, etc...) (yes amazingly) although this is not really food since one can't sustain living eating this
public interface Food {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComposition();

    //we could also wonder for its calories, etc...
    //and the date until you can eat/drink it ("best before")
    //where and how it was made
    //the proper way to prepare it and to eat it
    //how to store it
    //what it is stored in
}
