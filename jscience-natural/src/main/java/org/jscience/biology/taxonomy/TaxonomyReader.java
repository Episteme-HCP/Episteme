package org.jscience.biology.taxonomy;

/**
 * Loader for taxonomy data.
 * Placeholder for missing class.
 */
public class TaxonomyReader {

    public static String getCategory() {
        return "Biology";
    }

    public static String getDescription() {
        return "Taxonomy Data Loader.";
    }

    public static Species getSpecies(String name) {
        return new Species(name, name);
    }
}

