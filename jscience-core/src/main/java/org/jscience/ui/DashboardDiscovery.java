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

package org.jscience.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Service to dynamically discover JScience components (Apps, Loaders, Themes)
 * from the classpath.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DashboardDiscovery {

    private static final DashboardDiscovery INSTANCE = new DashboardDiscovery();

    // Cache
    private final Map<String, List<Class<?>>> cache = new HashMap<>();

    public static DashboardDiscovery getInstance() {
        return INSTANCE;
    }

    /**
     * Finds all accessible classes that match the given name suffix (e.g.,
     * "Loader", "Viewer").
     */
    public List<ClassInfo> findClasses(String suffix) {
        List<ClassInfo> results = new ArrayList<>();
        String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(File.pathSeparator);

        for (String path : paths) {
            File file = new File(path);
            if (file.isDirectory()) {
                scanDirectory(file, "", suffix, results);
            } else if (file.getName().endsWith(".jar")) {
                // Skip standard JRE jars to speed up and avoid permission issues
                if (!isSystemJar(file.getName())) {
                    scanJar(file, suffix, results);
                }
            }
        }

        // Sort by name
        results.sort(Comparator.comparing(c -> c.simpleName));
        return results;
    }

    private boolean isSystemJar(String name) {
        return name.startsWith("java") || name.startsWith("jdk") || name.startsWith("jre")
                || name.startsWith("javafx") || name.startsWith("sun") || name.startsWith("oracle");
    }

    private void scanDirectory(File directory, String packageName, String suffix, List<ClassInfo> results) {
        File[] files = directory.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                String newPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                scanDirectory(file, newPackage, suffix, results);
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                if (className.endsWith(suffix) && !className.equals(suffix)) { // Avoid interface itself if named same
                    String fullClassName = packageName.isEmpty() ? className : packageName + "." + className;
                    try {
                        // We only want concrete public classes
                        Class<?> cls = Class.forName(fullClassName, false, this.getClass().getClassLoader());
                        if (!Modifier.isAbstract(cls.getModifiers()) && !Modifier.isInterface(cls.getModifiers())
                                && Modifier.isPublic(cls.getModifiers())) {
                            // Basic description heuristic
                            String desc = "JScience " + suffix;
                            results.add(new ClassInfo(className, fullClassName, desc));
                        }
                    } catch (Throwable t) {
                        // Ignore load errors
                    }
                }
            }
        }
    }

    private void scanJar(File jarFile, String suffix, List<ClassInfo> results) {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                    if (className.endsWith(suffix)) {
                        try {
                            Class<?> cls = Class.forName(className, false, this.getClass().getClassLoader());
                            if (!Modifier.isAbstract(cls.getModifiers()) && !Modifier.isInterface(cls.getModifiers())
                                    && Modifier.isPublic(cls.getModifiers())) {
                                String simpleName = className.substring(className.lastIndexOf('.') + 1);
                                results.add(new ClassInfo(simpleName, className, "From " + jarFile.getName()));
                            }
                        } catch (Throwable t) {
                            // Ignore
                        }
                    }
                }
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    public static class ClassInfo {
        public final String simpleName;
        public final String fullName;
        public final String description;

        public ClassInfo(String simpleName, String fullName, String description) {
            this.simpleName = simpleName;
            this.fullName = fullName;
            this.description = description;
        }

        @Override
        public String toString() {
            return simpleName;
        }
    }
}
