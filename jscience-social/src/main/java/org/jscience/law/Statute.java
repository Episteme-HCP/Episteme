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

package org.jscience.law;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a legal statute, law, or regulation.
 * <p>
 * A statute is a formal written enactment of a legislative authority.
 * This class provides a structured representation including hierarchical elements like articles.
 * </p>
 * Modernized to implement ComprehensiveIdentification and use extensible StatuteType and StatuteStatus.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Statute implements ComprehensiveIdentification {

    private static final long serialVersionUID = 3L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    /**
     * @deprecated Use {@link StatuteType} instead.
     */
    @Deprecated
    public enum Type {
        @Deprecated CONSTITUTION, @Deprecated FEDERAL_LAW, @Deprecated STATE_LAW, @Deprecated REGULATION, @Deprecated ORDINANCE,
        @Deprecated TREATY, @Deprecated DIRECTIVE, @Deprecated DECREE, @Deprecated ACT;
        
        @Deprecated
        public StatuteType toStatuteType() {
             try {
                return StatuteType.valueOf(this.name());
            } catch (IllegalArgumentException e) {
                return StatuteType.OTHER;
            }
        }
    }

    /**
     * @deprecated Use {@link StatuteStatus} instead.
     */
    @Deprecated
    public enum Status {
        @Deprecated PROPOSED, @Deprecated ENACTED, @Deprecated AMENDED, @Deprecated REPEALED;
        
        @Deprecated
        public StatuteStatus toStatuteStatus() {
             try {
                return StatuteStatus.valueOf(this.name());
            } catch (IllegalArgumentException e) {
                return StatuteStatus.PROPOSED;
            }
        }
    }

    @Attribute
    private final String code;
    
    @Attribute
    private StatuteType type;
    
    @Attribute
    private String jurisdiction;
    
    @Attribute
    private int yearEnacted;
    
    @Attribute
    private StatuteStatus status;
    
    /** The hierarchical components (articles/clauses) of the statute. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Article> articles = new ArrayList<>();

    public Statute(String code, String title, StatuteType type, String jurisdiction,
            int yearEnacted, StatuteStatus status) {
        this.id = new SimpleIdentification("Statute:" + UUID.randomUUID());
        this.code = Objects.requireNonNull(code);
        setName(Objects.requireNonNull(title));
        this.type = Objects.requireNonNull(type);
        this.jurisdiction = jurisdiction;
        this.yearEnacted = yearEnacted;
        this.status = Objects.requireNonNull(status);
    }

    @Deprecated
    public Statute(String code, String title, Type type, String jurisdiction,
            int yearEnacted, Status status) {
        this(code, title, type != null ? type.toStatuteType() : StatuteType.OTHER, 
             jurisdiction, yearEnacted, status != null ? status.toStatuteStatus() : StatuteStatus.PROPOSED);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getCode() { return code; }
    
    public String getTitle() { return getName(); }
    
    public StatuteType getType() { return type; }
    
    public void setType(StatuteType type) { this.type = Objects.requireNonNull(type); }

    public String getJurisdiction() { return jurisdiction; }
    
    public void setJurisdiction(String jurisdiction) { this.jurisdiction = jurisdiction; }

    public int getYearEnacted() { return yearEnacted; }
    
    public void setYearEnacted(int yearEnacted) { this.yearEnacted = yearEnacted; }

    public StatuteStatus getStatus() { return status; }
    
    public void setStatus(StatuteStatus status) { this.status = Objects.requireNonNull(status); }

    public List<Article> getArticles() {
        return Collections.unmodifiableList(articles);
    }

    public void addArticle(Article article) {
        this.articles.add(Objects.requireNonNull(article));
    }

    public boolean isActive() {
        return status == StatuteStatus.ENACTED || status == StatuteStatus.AMENDED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Statute that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s, %d) - %s", code, getName(), type, yearEnacted, status);
    }
}
