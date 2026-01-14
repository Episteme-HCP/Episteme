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

package org.jscience.biology.services;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for GBIF data.
 * Stub implementation.
 */
public class GbifService {
    private static final GbifService INSTANCE = new GbifService();

    public static GbifService getInstance() {
        return INSTANCE;
    }

    public CompletableFuture<List<GbifSpecies>> searchSpecies(String query) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    public CompletableFuture<String> getSpeciesMedia(long key) {
        return CompletableFuture.completedFuture(null);
    }

    public record GbifSpecies(long key, String scientificName, String rank, String kingdom, String phylum, String clazz,
            String order, String family, String genus) {
    }
}
