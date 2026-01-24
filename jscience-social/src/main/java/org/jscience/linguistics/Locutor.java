package org.jscience.linguistics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.biology.Individual;
import org.jscience.sociology.Role;

/**
 * Represents an individual participant in a linguistic interaction.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Locutor extends Role {

    private final Set<Language> knownLanguages = new HashSet<>();

    public Locutor(Individual individual, ChatSituation situation) {
        super(individual, "Locutor", situation, Role.CLIENT);
    }

    public Set<Language> getKnownLanguages() {
        return Collections.unmodifiableSet(knownLanguages);
    }

    public void addLanguage(Language language) {
        if (language != null) {
            knownLanguages.add(language);
        }
    }

    public void removeLanguage(Language language) {
        knownLanguages.remove(language);
    }

    public void setLanguages(Set<Language> languages) {
        this.knownLanguages.clear();
        if (languages != null) {
            this.knownLanguages.addAll(languages);
        }
    }
}
