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
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    public static DashboardDiscovery getInstance() {
        return INSTANCE;
    }

    /**
     * Finds all accessible classes that match the given name suffix (e.g.,
     * "Loader", "Viewer").
     */
    public List<ClassInfo> findClasses(String suffix) {
        Set<String> processed = new HashSet<>();
        List<ClassInfo> results = new ArrayList<>();
        String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(File.pathSeparator);

        for (String path : paths) {
            File file = new File(path);
            if (file.isDirectory()) {
                scanDirectory(file, "", suffix, results, processed);
            } else if (file.getName().endsWith(".jar")) {
                if (!isSystemJar(file.getName())) {
                    scanJar(file, suffix, results, processed);
                }
            }
        }

        // Sort by simple name, then full name
        results.sort(Comparator.comparing((ClassInfo c) -> c.simpleName)
                .thenComparing(c -> c.fullName));
        return results;
    }

    private boolean isSystemJar(String name) {
        return name.startsWith("java") || name.startsWith("jdk") || name.startsWith("jre")
                || name.startsWith("javafx") || name.startsWith("sun") || name.startsWith("oracle");
    }

    private void scanDirectory(File directory, String packageName, String suffix, List<ClassInfo> results,
            Set<String> processed) {
        File[] files = directory.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                String newPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                scanDirectory(file, newPackage, suffix, results, processed);
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                String fullClassName = packageName.isEmpty() ? className : packageName + "." + className;

                if (!fullClassName.startsWith("org.jscience."))
                    continue;

                boolean matchesSuffix = className.endsWith(suffix) && !className.equals(suffix);
                // Special case: if suffix is "Demo", also include "App" (for Killer Apps)
                if ("Demo".equals(suffix) && className.endsWith("App")) {
                    matchesSuffix = true;
                }
                boolean isDeviceRequested = "Device".equals(suffix);

                if (matchesSuffix || isDeviceRequested) {
                    if (processed.contains(fullClassName))
                        continue;

                    try {
                        Class<?> cls = Class.forName(fullClassName, false, this.getClass().getClassLoader());
                        if (!Modifier.isAbstract(cls.getModifiers()) && !Modifier.isInterface(cls.getModifiers())
                                && Modifier.isPublic(cls.getModifiers())) {

                            if (isDeviceRequested) {
                                if (!org.jscience.device.Device.class.isAssignableFrom(cls)) {
                                    if (!matchesSuffix)
                                        continue;
                                }
                            }

                            if ("Loader".equals(suffix)) {
                                if (!org.jscience.io.ResourceLoader.class.isAssignableFrom(cls)) {
                                    continue;
                                }
                            }

                            if ("Demo".equals(suffix) || className.endsWith("App")) {
                                try {
                                    Class<?> demoProvider = Class.forName("org.jscience.ui.DemoProvider", false,
                                            this.getClass().getClassLoader());
                                    if (!demoProvider.isAssignableFrom(cls)) {
                                        continue;
                                    }
                                } catch (ClassNotFoundException e) {
                                    // Fallback to AbstractDemo if DemoProvider not found
                                    try {
                                        Class<?> abstractDemo = Class.forName("org.jscience.ui.AbstractDemo", false,
                                                this.getClass().getClassLoader());
                                        if (!abstractDemo.isAssignableFrom(cls)) {
                                            continue;
                                        }
                                    } catch (ClassNotFoundException e2) {
                                        continue;
                                    }
                                }
                            }

                            processed.add(fullClassName);
                            String desc = "JScience " + (isDeviceRequested ? "Device" : suffix);
                            results.add(new ClassInfo(className, fullClassName, desc));
                        }
                    } catch (Throwable t) {
                        // Ignore
                    }
                }
            }
        }
    }

    private void scanJar(File jarFile, String suffix, List<ClassInfo> results, Set<String> processed) {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String entryName = entry.getName();
                    String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                    if (!className.startsWith("org.jscience."))
                        continue;

                    boolean matchesSuffix = className.endsWith(suffix);
                    // Special case: if suffix is "Demo", also include "App" (for Killer Apps)
                    if ("Demo".equals(suffix) && className.endsWith("App")) {
                        matchesSuffix = true;
                    }
                    boolean isDeviceRequested = "Device".equals(suffix);

                    if (matchesSuffix || isDeviceRequested) {
                        if (processed.contains(className))
                            continue;
                        try {
                            Class<?> cls = Class.forName(className, false, this.getClass().getClassLoader());
                            if (!Modifier.isAbstract(cls.getModifiers()) && !Modifier.isInterface(cls.getModifiers())
                                    && Modifier.isPublic(cls.getModifiers())) {

                                if (isDeviceRequested) {
                                    if (!org.jscience.device.Device.class.isAssignableFrom(cls)) {
                                        if (!matchesSuffix)
                                            continue;
                                    }
                                }

                                if ("Loader".equals(suffix)) {
                                    if (!org.jscience.io.ResourceLoader.class.isAssignableFrom(cls)) {
                                        continue;
                                    }
                                }

                                if ("Demo".equals(suffix) || className.endsWith("App")) {
                                    try {
                                        Class<?> demoProvider = Class.forName("org.jscience.ui.DemoProvider", false,
                                                this.getClass().getClassLoader());
                                        if (!demoProvider.isAssignableFrom(cls)) {
                                            continue;
                                        }
                                    } catch (ClassNotFoundException e) {
                                        try {
                                            Class<?> abstractDemo = Class.forName("org.jscience.ui.AbstractDemo", false,
                                                    this.getClass().getClassLoader());
                                            if (!abstractDemo.isAssignableFrom(cls)) {
                                                continue;
                                            }
                                        } catch (ClassNotFoundException e2) {
                                            continue;
                                        }
                                    }
                                }

                                processed.add(className);
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

    /**
     * Finds resources on the classpath matching the given pattern.
     * 
     * @param pattern substring to match (e.g., "messages_core")
     * @return list of matching resource paths
     */
    public List<String> findResources(String pattern) {
        List<String> results = new ArrayList<>();
        String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(File.pathSeparator);

        for (String path : paths) {
            File file = new File(path);
            if (file.isDirectory()) {
                scanDirectoryForResources(file, "", pattern, results);
            }
            // Jar scanning for resources omitted for brevity/speed in this context
            // as we mostly care about project files for now.
            // If needed, can be added similar to scanJar.
        }
        return results;
    }

    private void scanDirectoryForResources(File directory, String packageName, String pattern, List<String> results) {
        File[] files = directory.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                String newPackage = packageName.isEmpty() ? file.getName() : packageName + "/" + file.getName();
                scanDirectoryForResources(file, newPackage, pattern, results);
            } else {
                if (file.getName().contains(pattern)) {
                    String resPath = packageName.isEmpty() ? file.getName() : packageName + "/" + file.getName();
                    results.add(resPath);
                }
            }
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
