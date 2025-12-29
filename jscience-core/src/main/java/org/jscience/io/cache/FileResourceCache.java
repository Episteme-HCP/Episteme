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

package org.jscience.io.cache;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.Optional;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FileResourceCache implements ResourceCache {

    private static final FileResourceCache INSTANCE = new FileResourceCache();
    private final Path cacheDir;

    /** Default TTL in milliseconds (24 hours). */
    private static final long DEFAULT_TTL_MS = 24 * 60 * 60 * 1000;
    private long ttlMs = DEFAULT_TTL_MS;

    private FileResourceCache() {
        String userHome = System.getProperty("user.home");
        this.cacheDir = Paths.get(userHome, ".jscience", "cache");
        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            System.err.println("Warning: Could not create cache directory: " + e.getMessage());
        }
    }

    public static FileResourceCache getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the time-to-live for cached entries.
     * 
     * @param ttlMillis TTL in milliseconds
     */
    public void setTtl(long ttlMillis) {
        this.ttlMs = ttlMillis;
    }

    @Override
    public Optional<String> get(String key) {
        try {
            Path file = getFileForKey(key);
            if (Files.exists(file)) {
                // Check TTL
                long fileAge = System.currentTimeMillis() - Files.getLastModifiedTime(file).toMillis();
                if (fileAge > ttlMs) {
                    Files.delete(file); // Expired
                    return Optional.empty();
                }
                return Optional.of(new String(Files.readAllBytes(file), "UTF-8"));
            }
        } catch (Exception e) {
            // Ignore cache read errors
        }
        return Optional.empty();
    }

    @Override
    public void put(String key, String data) {
        try {
            Path file = getFileForKey(key);
            Files.write(file, data.getBytes("UTF-8"));
        } catch (Exception e) {
            System.err.println("Warning: Failed to write to cache: " + e.getMessage());
        }
    }

    @Override
    public void clear() {
        try {
            Files.walk(cacheDir)
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getFileForKey(String key) {
        String filename = hash(key) + ".cache";
        return cacheDir.resolve(filename);
    }

    private String hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return String.valueOf(key.hashCode());
        }
    }
}