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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.devices.signals.nmea;

import java.io.InputStream;
import java.util.Scanner;
import org.jscience.devices.Sensor;

/**
 * A basic NMEA parser that acts as a Sensor for NMEA messages.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 5.0
 */
public class NMEAParser implements Sensor<NMEAMessage> {

    private final InputStream input;
    private final Scanner scanner;
    private final String name;

    public NMEAParser(InputStream input, String name) {
        this.input = input;
        this.scanner = new Scanner(input);
        this.name = name;
    }

    @Override
    public NMEAMessage readValue() {
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

    @Override
    public void connect() {
        // Assume stream is already open
    }

    @Override
    public void disconnect() {
        scanner.close();
    }

    @Override
    public boolean isConnected() {
        return input != null; // Simplified logic
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
