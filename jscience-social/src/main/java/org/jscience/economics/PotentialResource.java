package org.jscience.economics;

import org.jscience.measure.Amount;

import org.jscience.util.Named;

import java.io.Serializable;


/**
 * A class representing a would be resource, or virtual resource, for
 * example oil in the soil that you expect to find or ingredients in a recipie
 * or task.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//They can be people, equipment, facilities, funding, or anything else capable of definition
//(usually other than labour) required for the completion of a project activity. The lack of
//a resource will therefore be a constraint on the completion of the project activity.
//Resources may be storable or non storable. Storable resources remain available unless
//depeleted by usage, and may be replenished by project tasks which produce them. Non-storable
//resources must be renewed for each time period, even if not utilised in previous time periods.
public class PotentialResource extends Object implements Named, Serializable {
    /** DOCUMENT ME! */
    private String name; //the name

    /** DOCUMENT ME! */
    private String description;

    /** DOCUMENT ME! */
    private Amount amount;

    /** DOCUMENT ME! */
    private double decayTime;

    /** DOCUMENT ME! */
    private int kind;

/**
     * Creates a new PotentialResource object.
     *
     * @param name        DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param amount      DOCUMENT ME!
     */
    public PotentialResource(String name, String description, Amount amount) {
        if ((name != null) && (name.length() > 0) && (description != null) &&
                (description.length() > 0) && (amount != null)) {
            if (amount.isGreaterThan(Amount.ZERO)) {
                this.name = name;
                this.description = description;
                this.amount = amount;
                this.decayTime = -1;
                this.kind = EconomicsConstants.UNKNOWN;
            } else {
                throw new IllegalArgumentException(
                    "The quantity should always be greater or equal to 0.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Resource constructor can't have null arguments and name and description can't be empty.");
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
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //you should normally avoid doing so after construction time
    public void setQuantity(Amount amount) {
        if (amount != null) {
            if (amount.isGreaterThan(Amount.ZERO)) {
                this.amount = amount;
            } else {
                throw new IllegalArgumentException(
                    "The quantity should always be greater or equal to 0.");
            }
        } else {
            throw new IllegalArgumentException("You can't set a null quantity.");
        }
    }

    //the number of seconds after production date after which the resource should not be used anymore
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDecayTime() {
        return decayTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param decayTime DOCUMENT ME!
     */
    public void setDecayTime(double decayTime) {
        this.decayTime = decayTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     */

    //see EconomicsConstants industries
    public void setKind(int kind) {
        this.kind = kind;
    }

    //equality on all but identification (which should never be the same anyway)
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        PotentialResource resource;

        if ((o != null) && (o instanceof PotentialResource)) {
            resource = (PotentialResource) o;

            return this.getName().equals(resource.getName()) &&
            this.getDescription().equals(resource.getDescription()) &&
            this.getAmount().equals(resource.getAmount()) &&
            (this.getDecayTime() == resource.getDecayTime()) &&
            (this.getKind() == resource.getKind());
        } else {
            return false;
        }
    }
}
