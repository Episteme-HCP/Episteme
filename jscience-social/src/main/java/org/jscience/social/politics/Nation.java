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

package org.jscience.social.politics;


import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.social.sociology.Human;
import org.jscience.social.economics.Organization;
import org.jscience.natural.earth.Place;
import org.jscience.social.law.Code;
import org.jscience.social.law.Constitution;
import org.jscience.social.law.Treaty;
import org.jscience.social.psychology.social.Tribe;
import org.jscience.social.sociology.Culture;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a sovereign nation or organized human group (modern tribe).
 * A nation is defined by its territory, identity (Culture), and supreme law (Constitution).
 * * @version 1.2
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Nation extends Tribe {

    private static final long serialVersionUID = 1L;

    @Attribute
    private int kind;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Administration government; 

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Human> parliment; 

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Constitution constitution;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Code> codes; 

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Treaty> treaties; 

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Organization> organizations;

    /**
     * Initializes a new Nation.
     *
     * @param name            the name of the nation
     * @param formalTerritory the physical territory
     * @param culture         the dominant cultural identity
     * @throws NullPointerException if any argument is null
     */
    public Nation(String name, Place formalTerritory, Culture culture) {
        this(name, formalTerritory, culture, null);
    }

    public Nation(String name, Place formalTerritory, Culture culture, org.jscience.natural.engineering.eventdriven.EventDrivenEngine engine) {
        super(Objects.requireNonNull(name, "Name cannot be null"), 
              Objects.requireNonNull(formalTerritory, "Territory cannot be null"), 
              Objects.requireNonNull(culture, "Culture cannot be null"),
              engine);

        this.kind = PoliticsConstants.UNKNOWN;
        this.parliment = new HashSet<>();
        this.codes = new HashSet<>();
        this.treaties = new HashSet<>();
        this.organizations = new HashSet<>();
    }

    /**
     * Returns the kind or classification of this nation.
     * @return the kind constant from {@link PoliticsConstants}
     */
    public int getKind() {
        return kind;
    }

    /**
     * Sets the classification of the nation.
     * @param kind the kind constant
     */
    public void setKind(int kind) {
        this.kind = kind;
    }

    /**
     * Returns the primary government administration.
     * @return government instance
     */
    public Administration getGovernment() {
        return government;
    }

    /**
     * Sets the government administration.
     * @param government the government
     */
    public void setGovernment(Administration government) {
        this.government = government;
    }

    /**
     * Returns an unmodifiable set of parliament members.
     * @return set of human representatives
     */
    public Set<Human> getParliment() {
        return Collections.unmodifiableSet(parliment);
    }

    /**
     * Overwrites the parliament membership.
     * @param parliment current set of members
     */
    public void setParliment(Set<Human> parliment) {
        this.parliment = new HashSet<>(Objects.requireNonNull(parliment, "Parliament cannot be null"));
    }

    /**
     * Adds an individual to the parliament.
     * @param human the representative to add
     */
    public void addParlimentMember(Human human) {
        parliment.add(Objects.requireNonNull(human, "Member cannot be null"));
    }

    /**
     * Removes an individual from the parliament membership.
     * @param human the representative to remove
     */
    public void removeParlimentMember(Human human) {
        parliment.remove(human);
    }

    /**
     * Returns the nation's constitution.
     * @return constitution document
     */
    public Constitution getConstitution() {
        return constitution;
    }

    /**
     * Sets the nation's constitution.
     * @param constitution the constitution
     */
    public void setConstitution(Constitution constitution) {
        this.constitution = constitution;
    }

    /**
     * Returns an unmodifiable set of legislative codes.
     * @return legal codes
     */
    public Set<Code> getCodes() {
        return Collections.unmodifiableSet(codes);
    }

    /**
     * Overwrites the nation's legal codes.
     * @param codes the set of codes
     */
    public void setCodes(Set<Code> codes) {
        this.codes = new HashSet<>(Objects.requireNonNull(codes, "Codes set cannot be null"));
    }

    /**
     * Adds a specific legal code to the nation's registry.
     * @param code the code to add
     */
    public void addCode(Code code) {
        codes.add(Objects.requireNonNull(code, "Code cannot be null"));
    }

    /**
     * Removes a legal code from the registry.
     * @param code the code to remove
     */
    public void removeCode(Code code) {
        codes.remove(code);
    }

    /**
     * Returns an unmodifiable set of registered international treaties.
     * @return treaties
     */
    public Set<Treaty> getTreaties() {
        return Collections.unmodifiableSet(treaties);
    }

    /**
     * Overwrites the list of international treaties.
     * @param treaties the set of treaties
     */
    public void setTreaties(Set<Treaty> treaties) {
        this.treaties = new HashSet<>(Objects.requireNonNull(treaties, "Treaties set cannot be null"));
    }

    /**
     * Adds a new treaty to the nation's records.
     * @param treaty the treaty instance
     */
    public void addTreaty(Treaty treaty) {
        treaties.add(Objects.requireNonNull(treaty, "Treaty cannot be null"));
    }

    /**
     * Removes an international treaty from registration.
     * @param treaty the treaty to remove
     */
    public void removeTreaty(Treaty treaty) {
        treaties.remove(treaty);
    }

    /**
     * Returns an unmodifiable set of affiliated organizations.
     * @return organizations
     */
    public Set<Organization> getOrganizations() {
        return Collections.unmodifiableSet(organizations);
    }

    /**
     * Overwrites the set of nation-affiliated organizations.
     * @param organizations the organization set
     */
    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = new HashSet<>(Objects.requireNonNull(organizations, "Organizations set cannot be null"));
    }

    /**
     * Registers a new organization under the nation's jurisdiction.
     * @param organization the organization to add
     */
    public void addOrganization(Organization organization) {
        organizations.add(Objects.requireNonNull(organization, "Organization cannot be null"));
    }

    /**
     * Unregisters an organization from the nation's jurisdiction.
     * @param organization the organization to remove
     */
    public void removeOrganization(Organization organization) {
        organizations.remove(organization);
    }
}

