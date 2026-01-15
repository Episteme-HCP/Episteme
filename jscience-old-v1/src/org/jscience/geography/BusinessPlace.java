package org.jscience.geography;

import org.jscience.economics.Organization;

import java.util.Set;


/**
 * A class representing a business spot where humans work but don't live.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class BusinessPlace extends OwnedPlace {
    /** DOCUMENT ME! */
    private Address address;

    /** DOCUMENT ME! */
    private Organization organization;

/**
     * public BusinessPlace(Boundary boundary, Address address, Organization
     * organization) { super(organization.getName(), boundary,
     * organization.getOwners()); if ((organization != null) && (address !=
     * null)) { this.address = address; address.setPosition(this); } else {
     * throw new IllegalArgumentException( "The BusinessPlace constructor
     * can't have null address or Organization."); }}
     *
     * @param name     DOCUMENT ME!
     * @param boundary DOCUMENT ME!
     * @param address  DOCUMENT ME!
     * @param owners   DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //it is not said if you own the place or not
    public BusinessPlace(String name, Boundary boundary, Address address,
        Set owners) {
        super(name, boundary, owners);

        if (address != null) {
            this.address = address;
            address.setPosition(this);
        } else {
            throw new IllegalArgumentException(
                "The BusinessPlace constructor can't have null address.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Address getAddress() {
        return address;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Organization getOrganization() {
        return organization;
    }

    //a callback to the Organization using this Address
    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     */
    protected void setOrganization(Organization organization) {
        if ((organization != null) &&
                (organization.getPosition().equals(this))) {
            this.organization = organization;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null Organization or an Organization whose place is not this.");
        }
    }
}
