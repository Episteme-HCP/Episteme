/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.measure;

import java.io.Serializable;
import java.util.Date;
import org.jscience.economics.Organization;
import org.jscience.earth.Place;
import org.jscience.sociology.Human;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.Positioned;

/**
 * A class representing an information concerning someone or something.
 * Simplified version for jscience-social.
 */
public class Report implements Serializable, Identified<Identification>, Positioned<Place> {
    
    private Organization authority;
    private Identification identification;
    private Human author;
    private Place place;
    private Date date;
    private String content;

    public Report(Organization authority, Identification identification,
        Human author, Place place, Date date, String content) {
        this.authority = authority;
        this.identification = identification;
        this.author = author;
        this.place = place;
        this.date = date;
        this.content = content;
    }

    public Organization getAuthority() { return authority; }
    public Identification getId() { return identification; }
    public Human getAuthor() { return author; }
    public Place getPosition() { return place; }
    public Date getDate() { return date; }
    public String getContent() { return content; }
}
