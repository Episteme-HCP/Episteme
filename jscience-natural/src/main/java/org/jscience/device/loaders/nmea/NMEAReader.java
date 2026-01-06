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

package org.jscience.device.loaders.nmea;

import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * A basic NMEA parser that acts as a Sensor for NMEA messages.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NMEAReader extends AbstractResourceReader<List<NMEAMessage>> {

    private Scanner scanner;

    public NMEAReader() {
    }

    public NMEAReader(InputStream input, String name) {
        this.scanner = new Scanner(input);
    }

    @Override
    public String getResourcePath() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<NMEAMessage>> getResourceType() {
        return (Class<List<NMEAMessage>>) (Class<?>) List.class;
    }

    @Override
    protected List<NMEAMessage> loadFromSource(String id) throws Exception {
        List<NMEAMessage> messages = new ArrayList<>();
        try (InputStream is = new java.io.FileInputStream(id); Scanner sc = new Scanner(is)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.startsWith("$") && NMEAMessage.validateChecksum(line)) {
                    messages.add(new NMEAMessage(line));
                }
            }
        }
        return messages;
    }

    @Override
    protected MiniCatalog<List<NMEAMessage>> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<List<NMEAMessage>> getAll() {
                return List.of(List.of());
            }

            @Override
            public Optional<List<NMEAMessage>> findByName(String name) {
                return Optional.of(List.of());
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    /**
     * Parses next NMEA message from the stream (for streaming mode).
     * 
     * @return the message or null if end of stream
     */
    public NMEAMessage parse() {
        if (scanner == null)
            return null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.startsWith("$")) {
                if (NMEAMessage.validateChecksum(line)) {
                    return new NMEAMessage(line);
                }
            }
        }
        return null;
    }

    public void close() {
        if (scanner != null)
            scanner.close();
    }
}

