package org.jscience.methodology;


import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A formal description of a scientific entity, observation, or theory.
 * <p>
 * This class provides a structured way to document scientific concepts and findings.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class ScientificDescription implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private String name;

    @Attribute
    private final Set<String> authors;

    @Attribute
    private Instant date;

    @Attribute
    private String contents;

    /**
     * Creates a new Scientific Description.
     * @param name The name of the description.
     */
    public ScientificDescription(String name) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        setName(Objects.requireNonNull(name));
        this.authors = new HashSet<>();
        this.date = Instant.now();
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public Identification getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void addAuthor(String author) {
        if (author != null) authors.add(author);
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return String.format("Description{name='%s', date=%s}", name, date);
    }
}
