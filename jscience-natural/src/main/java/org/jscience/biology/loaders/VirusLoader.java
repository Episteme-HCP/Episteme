/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.biology.loaders;

import org.jscience.biology.VirusSpecies;
import org.jscience.biology.Virus;
import org.jscience.io.AbstractLoader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loader for virus species from JSON resource.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VirusLoader extends AbstractLoader<VirusSpecies> {

    private static final String RESOURCE_PATH = "/org/jscience/biology/viruses.json";
    private static VirusLoader instance;
    private final Map<String, VirusSpecies> cache = new HashMap<>();
    private boolean loaded = false;

    public VirusLoader() {
        // AbstractLoader has default constructor
    }

    public static synchronized VirusLoader getInstance() {
        if (instance == null) {
            instance = new VirusLoader();
        }
        return instance;
    }

    @Override
    public String getResourcePath() {
        return RESOURCE_PATH;
    }

    @Override
    public Class<VirusSpecies> getResourceType() {
        return VirusSpecies.class;
    }

    @Override
    protected VirusSpecies loadFromSource(String identifier) throws Exception {
        ensureLoaded();
        return cache.get(identifier.toUpperCase());
    }

    @Override
    public List<VirusSpecies> loadAll() {
        try {
            ensureLoaded();
        } catch (IOException e) {
            return new ArrayList<>();
        }
        return new ArrayList<>(cache.values());
    }

    private synchronized void ensureLoaded() throws IOException {
        if (loaded)
            return;

        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getResourceAsStream(RESOURCE_PATH);
        if (is == null) {
            throw new IOException("Resource not found: " + RESOURCE_PATH);
        }

        List<VirusDTO> dtos = mapper.readValue(is, new TypeReference<List<VirusDTO>>() {
        });
        for (VirusDTO dto : dtos) {
            // Map genome type string to enum
            Virus.GenomeType genomeType = parseGenomeType(dto.genomeType);

            VirusSpecies species = new VirusSpecies(
                    dto.scientificName != null ? dto.scientificName : dto.name,
                    dto.name, // common name
                    dto.family,
                    null, // genus not in JSON
                    genomeType,
                    Virus.Morphology.COMPLEX);

            if (dto.hostRange != null) {
                for (String host : dto.hostRange) {
                    species.addHostSpecies(host);
                }
            }

            cache.put(dto.name.toUpperCase(), species);
            if (dto.scientificName != null) {
                cache.put(dto.scientificName.toUpperCase(), species);
            }
        }
        loaded = true;
    }

    private Virus.GenomeType parseGenomeType(String type) {
        if (type == null)
            return Virus.GenomeType.DNA_DOUBLE_STRANDED;
        if (type.equalsIgnoreCase("RNA"))
            return Virus.GenomeType.RNA_SINGLE_STRANDED_POSITIVE;
        if (type.equalsIgnoreCase("DNA"))
            return Virus.GenomeType.DNA_DOUBLE_STRANDED;
        return Virus.GenomeType.DNA_DOUBLE_STRANDED;
    }

    // DTO for JSON parsing
    private static class VirusDTO {
        public String name;
        public String scientificName;
        public String family;
        public String genomeType;
        public List<String> hostRange;
    }
}
