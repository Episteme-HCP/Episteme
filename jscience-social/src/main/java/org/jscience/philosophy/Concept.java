package org.jscience.philosophy;

import org.jscience.util.Named;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a key element of a philosophy.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also look at org.jscience.linguistics.SemanticNetwork

//perhaps we should also have a Property class that would describe several possible values for a concept:
//for example the concept of color would have some possible red, blue, green properties
//(yet, one could claim all these are themselves Concepts)

//we could also call this a meme (although we would expect some extra properties for memes)
//http://en.wikipedia.org/wiki/Meme
public class Concept extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Set relatedConcepts;

/**
     * Creates a new Concept object.
     *
     * @param name DOCUMENT ME!
     */
    public Concept(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            relatedConcepts = new HashSet();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getRelatedConcepts() {
        return relatedConcepts;
    }

    //all members of the Set should be concepts
    /**
     * DOCUMENT ME!
     *
     * @param concepts DOCUMENT ME!
     */
    public void setRelatedConcepts(Set concepts) {
        Iterator iterator;
        boolean valid;

        if (concepts != null) {
            valid = true;
            iterator = concepts.iterator();

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Concept;
            }

            if (valid) {
                this.relatedConcepts = concepts;
            } else {
                throw new IllegalArgumentException(
                    "The Set of concepts should contain only Concepts.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of concepts shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param concept DOCUMENT ME!
     */
    public void addRelatedConcept(Concept concept) {
        relatedConcepts.add(concept);
    }

    /**
     * DOCUMENT ME!
     *
     * @param concept DOCUMENT ME!
     */
    public void removeRelatedConcept(Concept concept) {
        relatedConcepts.remove(concept);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllConcepts() {
        Set nextResult;
        Set result;
        Set previousResult;
        Iterator iterator;
        boolean valid;

        result = new HashSet();
        previousResult = new HashSet();
        result.add(this);

        while (!previousResult.containsAll(result)) {
            iterator = result.iterator();
            nextResult = new HashSet();
            nextResult.addAll(result);
            valid = true;

            while (iterator.hasNext() && valid) {
                nextResult.addAll(((Concept) iterator.next()).getRelatedConcepts());
            }

            previousResult = result;
            result = nextResult;
        }

        return result;
    }
}
