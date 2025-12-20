/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.sociology;

import java.util.*;

/**
 * Represents a social role.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Role {

    public enum Type {
        FAMILY, OCCUPATIONAL, POLITICAL, SOCIAL, RELIGIOUS, EDUCATIONAL
    }

    private final String name;
    private Type type;
    private String description;
    private final List<String> responsibilities = new ArrayList<>();
    private final List<String> privileges = new ArrayList<>();
    private boolean formal;

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, Type type) {
        this(name);
        this.type = type;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFormal() {
        return formal;
    }

    public List<String> getResponsibilities() {
        return Collections.unmodifiableList(responsibilities);
    }

    public List<String> getPrivileges() {
        return Collections.unmodifiableList(privileges);
    }

    // Setters
    public void setType(Type type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFormal(boolean formal) {
        this.formal = formal;
    }

    public void addResponsibility(String responsibility) {
        responsibilities.add(responsibility);
    }

    public void addPrivilege(String privilege) {
        privileges.add(privilege);
    }

    @Override
    public String toString() {
        return String.format("Role '%s' (%s)", name, type);
    }

    // Common roles
    public static Role parent() {
        Role r = new Role("Parent", Type.FAMILY);
        r.addResponsibility("Care for children");
        r.addResponsibility("Provide shelter");
        r.addResponsibility("Education");
        return r;
    }

    public static Role teacher() {
        Role r = new Role("Teacher", Type.EDUCATIONAL);
        r.setFormal(true);
        r.addResponsibility("Instruction");
        r.addResponsibility("Assessment");
        return r;
    }
}
