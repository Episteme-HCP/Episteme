package org.jscience.arts.loaders;

import org.jscience.arts.music.Composition;
import org.jscience.io.ResourceReader;

/**
 * Base loader for musical compositions using the ResourceReader interface.
 */
public abstract class CompositionLoader implements ResourceReader<Composition> {

    @Override
    public Class<Composition> getResourceType() {
        return Composition.class;
    }

    @Override
    public String getCategory() {
        return "Arts / Music";
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}
