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

import java.util.*;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service to dynamically discover JScience components (Apps, Demos, Viewers)
 * using the modern ServiceLoader mechanism (SPI).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class MasterControlDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(MasterControlDiscovery.class);
    private static final MasterControlDiscovery INSTANCE = new MasterControlDiscovery();

    public static MasterControlDiscovery getInstance() {
        return INSTANCE;
    }

    /**
     * Discovers all available ViewerProvider implementations using ServiceLoader.
     * Use {@link #getProvidersByType()} for categorized results.
     */
    public List<ViewerProvider> getProviders() {
        List<ViewerProvider> results = new ArrayList<>();
        try {
            ServiceLoader<ViewerProvider> loader = ServiceLoader.load(ViewerProvider.class);
            for (ViewerProvider provider : loader) {
                results.add(provider);
            }
        } catch (Throwable e) {
            logger.error("Error loading providers via SPI", e);
        }
        return results;
    }

    public enum ProviderType {
        APP, DEMO, VIEWER
    }

    /**
     * Discovers providers and groups them by type (APP, DEMO, VIEWER) and then by
     * Category.
     */
    public Map<ProviderType, Map<String, List<ViewerProvider>>> getProvidersByType() {
        Map<ProviderType, Map<String, List<ViewerProvider>>> groupedProviders = new EnumMap<>(ProviderType.class);

        for (ViewerProvider provider : getProviders()) {
            ProviderType type = ProviderType.VIEWER;
            if (provider instanceof AppProvider) {
                type = ((AppProvider) provider).isDemo() ? ProviderType.DEMO : ProviderType.APP;
            }

            groupedProviders
                    .computeIfAbsent(type, k -> new TreeMap<>(java.text.Collator.getInstance()))
                    .computeIfAbsent(provider.getCategory(), c -> new ArrayList<>())
                    .add(provider);
        }
        return groupedProviders;
    }

    // --- Legacy Scanning Methods (Required for Loaders, Devices, and I18n) ---

    public List<ClassInfo> findClasses(String suffix) {
        Set<String> processed = new HashSet<>();
        List<ClassInfo> results = new ArrayList<>();
        String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(java.io.File.pathSeparator);

        for (String path : paths) {
            java.io.File file = new java.io.File(path);
            if (file.isDirectory()) {
                scanDirectory(file, "", suffix, results, processed);
            } else if (file.getName().endsWith(".jar")) {
                if (!isSystemJar(file.getName())) {
                    scanJar(file, suffix, results, processed);
                }
            }
        }

        results.sort(Comparator.comparing((ClassInfo c) -> c.simpleName)
                .thenComparing(c -> c.fullName));
        return results;
    }

    private boolean isSystemJar(String name) {
        return name.startsWith("java") || name.startsWith("jdk") || name.startsWith("jre")
                || name.startsWith("javafx") || name.startsWith("sun") || name.startsWith("oracle");
    }

    private void scanDirectory(java.io.File directory, String packageName, String suffix, List<ClassInfo> results,
            Set<String> processed) {
        java.io.File[] files = directory.listFiles();
        if (files == null)
            return;

        for (java.io.File file : files) {
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

                            // Note: We don't check for DemoProvider here anymore as we rely on the caller
                            // or just class presence
                            // for legacy reasons. Real ViewerProviders should use getProviders().

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

    private void scanJar(java.io.File jarFile, String suffix, List<ClassInfo> results, Set<String> processed) {
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
            Enumeration<java.util.jar.JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                java.util.jar.JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String entryName = entry.getName();
                    String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                    if (!className.startsWith("org.jscience."))
                        continue;

                    boolean matchesSuffix = className.endsWith(suffix);
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
        } catch (java.io.IOException e) {
            // Ignore
        }
    }

    public List<String> findResources(String pattern) {
        List<String> results = new ArrayList<>();
        String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(java.io.File.pathSeparator);

        for (String path : paths) {
            java.io.File file = new java.io.File(path);
            if (file.isDirectory()) {
                scanDirectoryForResources(file, "", pattern, results);
            }
        }
        return results;
    }

    private void scanDirectoryForResources(java.io.File directory, String packageName, String pattern,
            List<String> results) {
        java.io.File[] files = directory.listFiles();
        if (files == null)
            return;

        for (java.io.File file : files) {
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
