package org.jscience.sociology;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a social context or interaction.
 */
public class Situation {

    private final String name;
    private final String description;
    private final Set<Role> roles;

    public Situation(String name, String description) {
        this.name = name;
        this.description = description;
        this.roles = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void addRole(Role role) {
        if (role != null) {
            role.setSituation(this);
            roles.add(role);
        }
    }

    public void removeRole(Role role) {
        if (roles.contains(role)) {
            roles.remove(role);
            role.setSituation(null); // Or keep it loose?
        }
    }
}
