package org.jscience.linguistics.loaders;

import org.jscience.io.ResourceReader;

/**
 * Interface for linguistic resource loaders (Corpus, Dictionaries, etc.).
 */
public abstract class LinguisticResourceReader<T> implements ResourceReader<T> {

    @Override
    public String getCategory() {
        return "Linguistics";
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}
