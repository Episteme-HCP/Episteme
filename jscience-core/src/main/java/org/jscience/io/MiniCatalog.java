package org.jscience.io;

/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * data.
 * 
 * @param <T> type of data entries
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
import java.util.List;
import java.util.Optional;

public interface MiniCatalog<T> {

    /**
     * Returns all entries in the catalog.
     */
    List<T> getAll();

    /**
     * Finds an entry by name.
     */
    Optional<T> findByName(String name);

    /**
     * Returns the size of the catalog.
     */
    int size();
}
