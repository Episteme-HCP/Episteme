package org.jscience.psychology.social;

import org.jscience.biology.HomoSapiens;

import org.jscience.geography.Place;


import org.jscience.util.persistence.Persistent;

import java.util.Objects;

/**
 * Represents a group of human individuals (Homo Sapiens).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 */
@Persistent
public class HumanGroup extends Group {
/**
     * Creates a new HumanGroup object.
     */
    public HumanGroup() {
        super(HomoSapiens.SPECIES);
    }

    /**
     * Initializes a new HumanGroup instance with a physical location.
     *
     * @param formalTerritory the physical place or territory associated with this group
     * @throws NullPointerException if formalTerritory is null
     */
    public HumanGroup(Place formalTerritory) {
        super(HomoSapiens.SPECIES, Objects.requireNonNull(formalTerritory, "Territory cannot be null"));
    }
}
