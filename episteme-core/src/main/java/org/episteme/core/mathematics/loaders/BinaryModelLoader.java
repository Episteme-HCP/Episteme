/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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
package org.episteme.core.mathematics.loaders;

import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.mathematics.ml.neural.Layer;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Loader/Serializer for Neural Network models using Java Binary Serialization.
 * <p>
 * Implements {@link AbstractResourceReader} for standardized access.
 * Formerly ModelSerializer.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class BinaryModelLoader extends AbstractResourceReader<Layer<?>> {

    private static final BinaryModelLoader INSTANCE = new BinaryModelLoader();

    public static BinaryModelLoader getInstance() {
        return INSTANCE;
    }

    @Override
    protected Layer<?> loadFromSource(String id) throws Exception {
        Path path = Paths.get(id);
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (Layer<?>) ois.readObject();
        }
    }

    /**
     * Saves a layer (or model) to a binary file.
     * @param model the model/layer to save.
     * @param path the destination path.
     * @throws IOException if an I/O error occurs.
     */
    public void save(Layer<?> model, Path path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(model);
        }
    }

    @Override
    public String getName() {
        return "Binary Model Loader";
    }

    @Override
    public String getDescription() {
        return "Loads/Saves usage Java Serialization";
    }

    @Override
    public String getCategory() {
        return "AI/ML";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Layer<?>> getResourceType() {
        return (Class<Layer<?>>) (Class<?>) Layer.class;
    }
    @Override
    public String getResourcePath() {
        return "ml/models";
    }

    @Override
    public String getLongDescription() {
        return "Loads and saves Neural Network models using Java standard serialization.";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] { "1.0" };
    }
}
