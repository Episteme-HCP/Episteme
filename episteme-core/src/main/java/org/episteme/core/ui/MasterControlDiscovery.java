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

package org.episteme.core.ui;

import java.util.*;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service to dynamically discover Episteme components (Apps, Demos, Viewers)
 * using the modern ServiceLoader mechanism (SPI).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MasterControlDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(MasterControlDiscovery.class);
    private static final MasterControlDiscovery INSTANCE = new MasterControlDiscovery();
    private final Map<String, List<ClassInfo>> classCache = new HashMap<>();
    private List<ClassInfo> allDiscoveredClasses = null;

    public static MasterControlDiscovery getInstance() {
        return INSTANCE;
    }

    /**
     * Discovers all available Viewer implementations using ServiceLoader.
     * Use {@link #getProvidersByType()} for categorized results.
     */
    public List<Viewer> getProviders() {
        List<Viewer> results = new ArrayList<>();
        try {
            ServiceLoader<Viewer> loader = ServiceLoader.load(Viewer.class);
            Iterator<Viewer> iterator = loader.iterator();
            while (iterator.hasNext()) {
                try {
                    results.add(iterator.next());
                } catch (ServiceConfigurationError e) {
                    logger.warn("Could not load Viewer provider: {}", e.getMessage());
                }
            }
        } catch (Throwable e) {
            logger.error("Error loading providers via SPI", e);
        }
        
        // Diagnostic output to root
        try {
            java.nio.file.Files.writeString(java.nio.file.Path.of("discovery_diag.txt"), 
                "Discovered Viewers: " + results.size() + "\n" +
                results.stream().map(v -> v.getClass().getName()).collect(java.util.stream.Collectors.joining("\n")),
                java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception ex) {}
        
        return results;
    }

    public enum ProviderType {
        APP, DEMO, VIEWER
    }

    /**
     * Discovers providers and groups them by type (APP, DEMO, VIEWER) and then by
     * Category.
     */
    public Map<ProviderType, Map<String, List<Viewer>>> getProvidersByType() {
        Map<ProviderType, Map<String, List<Viewer>>> groupedProviders = new EnumMap<>(ProviderType.class);

        for (Viewer provider : getProviders()) {
            ProviderType type = ProviderType.VIEWER;
            if (provider instanceof App) {
                type = ((App) provider).isDemo() ? ProviderType.DEMO : ProviderType.APP;
            }

            groupedProviders
                    .computeIfAbsent(type, k -> new TreeMap<>(java.text.Collator.getInstance()))
                    .computeIfAbsent(provider.getCategory() == null ? "General" : provider.getCategory(), c -> new ArrayList<>())
                    .add(provider);
        }
        return groupedProviders;
    }

    // --- Legacy Scanning Methods (Required for Loaders, Devices, and I18N) ---

    public List<ClassInfo> findClasses(String suffix) {
        if (classCache.containsKey(suffix)) {
            return new ArrayList<>(classCache.get(suffix));
        }

        if (allDiscoveredClasses == null) {
            allDiscoveredClasses = scanAllEpistemeClasses();
        }

        List<ClassInfo> results = new ArrayList<>();
        for (ClassInfo info : allDiscoveredClasses) {
            boolean matchesSuffix = info.simpleName.endsWith(suffix) && !info.simpleName.equals(suffix);
            
            // Flexible matching for loaders and readers
            if ("Loader".equals(suffix) && info.simpleName.endsWith("Reader")) {
                matchesSuffix = true;
            }
            
            // Special case: if suffix is "Demo", also include "App" (for Killer Apps)
            if ("Demo".equals(suffix) && info.simpleName.endsWith("App")) {
                matchesSuffix = true;
            }
            
            boolean isDeviceRequested = "Device".equals(suffix);

            if (matchesSuffix || isDeviceRequested) {
                try {
                    Class<?> cls = Class.forName(info.fullName, false, MasterControlDiscovery.class.getClassLoader());
                    if (!Modifier.isAbstract(cls.getModifiers()) && !Modifier.isInterface(cls.getModifiers())
                            && Modifier.isPublic(cls.getModifiers())) {

                        if (isDeviceRequested) {
                            if (!org.episteme.core.device.Device.class.isAssignableFrom(cls)) {
                                if (!matchesSuffix)
                                    continue;
                            }
                        }

                        if ("Loader".equals(suffix)) {
                            if (!org.episteme.core.io.ResourceIO.class.isAssignableFrom(cls)) {
                                if (!info.simpleName.endsWith("Loader") && !info.simpleName.endsWith("Reader")) {
                                    continue;
                                }
                            }
                        }

                        results.add(info);
                    }
                } catch (Throwable t) {
                    // Ignore
                }
            }
        }

        results.sort(Comparator.comparing((ClassInfo c) -> c.simpleName)
                .thenComparing(c -> c.fullName));
        
        classCache.put(suffix, results);
        return results;
    }

    private List<ClassInfo> scanAllEpistemeClasses() {
        logger.info("Performing one-time full Episteme class discovery...");
        long start = System.currentTimeMillis();
        
        Set<String> processed = new HashSet<>();
        List<ClassInfo> results = new ArrayList<>();
        
        Set<String> allPaths = new LinkedHashSet<>();
        String classpath = System.getProperty("java.class.path");
        if (classpath != null) {
            allPaths.addAll(Arrays.asList(classpath.split(java.io.File.pathSeparator)));
        }

        addClassLoaderPaths(allPaths, Thread.currentThread().getContextClassLoader());
        addClassLoaderPaths(allPaths, ClassLoader.getSystemClassLoader());
        addClassLoaderPaths(allPaths, MasterControlDiscovery.class.getClassLoader());

        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        String[] paths = allPaths.toArray(new String[0]);

        for (String path : paths) {
            java.io.File file = new java.io.File(path);
            if (!file.exists()) continue;

            if (file.isDirectory()) {
                scanDirectoryRecursive(file, "", results, processed, tccl);
            } else if (file.getName().endsWith(".jar")) {
                if (!isSystemJar(file.getName())) {
                    scanJarRecursive(file, results, processed, tccl);
                }
            }
        }

        // Development heuristic: find target/classes in peer modules
        try {
            java.io.File current = new java.io.File(System.getProperty("user.dir")).getAbsoluteFile();
            java.io.File root = current;
            
            // Traverse up until we find a directory that looks like the project root (contains episteme-core)
            while (root != null && !new java.io.File(root, "episteme-core").exists()) {
                root = root.getParentFile();
            }
            
            if (root != null) {
                logger.info("Dev Mode: Scanning Episteme project root at {}", root.getAbsolutePath());
                java.io.File[] modules = root.listFiles(java.io.File::isDirectory);
                if (modules != null) {
                    for (java.io.File module : modules) {
                        java.io.File targetClasses = new java.io.File(module, "target/classes");
                        if (targetClasses.exists() && targetClasses.isDirectory()) {
                            logger.info("Dev Mode: Scanning module classes at {}", targetClasses.getAbsolutePath());
                            scanDirectoryRecursive(targetClasses, "", results, processed, tccl);
                        }
                        // Also check for JARs in target if classes directory is missing
                        java.io.File targetDir = new java.io.File(module, "target");
                        if (targetDir.exists() && targetDir.isDirectory()) {
                             java.io.File[] jars = targetDir.listFiles(f -> f.getName().endsWith(".jar") && f.getName().startsWith("episteme-") && !f.getName().contains("sources"));
                             if (jars != null) {
                                 for (java.io.File jar : jars) {
                                     logger.info("Dev Mode: Scanning module JAR at {}", jar.getAbsolutePath());
                                     scanJarRecursive(jar, results, processed, tccl);
                                 }
                             }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Dev discovery failed: {}", e.getMessage());
        }

        long end = System.currentTimeMillis();
        logger.info("Discovered {} Episteme classes in {} ms", results.size(), (end - start));
        return results;
    }

    private void scanDirectoryRecursive(java.io.File directory, String packageName, List<ClassInfo> results,
            Set<String> processed, ClassLoader classLoader) {
        java.io.File[] files = directory.listFiles();
        if (files == null) return;

        for (java.io.File file : files) {
            if (file.isDirectory()) {
                String newPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                scanDirectoryRecursive(file, newPackage, results, processed, classLoader);
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                String fullClassName = packageName.isEmpty() ? className : packageName + "." + className;

                if (!fullClassName.startsWith("org.episteme.")) continue;
                if (processed.add(fullClassName)) {
                    results.add(new ClassInfo(className, fullClassName, "Discovered class"));
                }
            }
        }
    }

    private void scanJarRecursive(java.io.File jarFile, List<ClassInfo> results, Set<String> processed, ClassLoader classLoader) {
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
            Enumeration<java.util.jar.JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                java.util.jar.JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String entryName = entry.getName();
                    String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                    if (!className.startsWith("org.episteme.")) continue;
                    if (processed.add(className)) {
                        String simpleName = className.substring(className.lastIndexOf('.') + 1);
                        results.add(new ClassInfo(simpleName, className, "From " + jarFile.getName()));
                    }
                }
            }
        } catch (java.io.IOException e) {}
    }

    private boolean isSystemJar(String name) {
        return name.startsWith("java") || name.startsWith("jdk") || name.startsWith("jre")
                || name.startsWith("javafx") || name.startsWith("sun") || name.startsWith("oracle")
                || name.startsWith("maven") || name.startsWith("plexus");
    }

    private void addClassLoaderPaths(Set<String> paths, ClassLoader cl) {
        if (cl == null) return;
        logger.debug("Scanning ClassLoader: {}", cl.getClass().getName());
        
        if (cl instanceof java.net.URLClassLoader) {
            for (java.net.URL url : ((java.net.URLClassLoader) cl).getURLs()) {
                if ("file".equals(url.getProtocol())) {
                    try {
                        String p = new java.io.File(url.toURI()).getAbsolutePath();
                        paths.add(p);
                        logger.debug("Found URL path: {}", p);
                    } catch (Exception e) { /* ignore */ }
                }
            }
        }
        // Heuristic to find classpath entries in modern Java / Maven
        try {
            // Search for various Episteme package markers to find module roots
            String[] markers = {"org/episteme", "org/episteme/core", "org/episteme/natural", "org/episteme/native", "org/episteme/benchmarks"};
            for (String marker : markers) {
                java.util.Enumeration<java.net.URL> resources = cl.getResources(marker);
                while (resources.hasMoreElements()) {
                    java.net.URL url = resources.nextElement();
                    String p = url.toString();
                    logger.trace("Marker '{}' found at: {}", marker, p);
                    
                    String cleanPath = null;
                    if (p.startsWith("jar:file:")) {
                        cleanPath = p.substring(9);
                        if (cleanPath.contains("!")) cleanPath = cleanPath.substring(0, cleanPath.indexOf("!"));
                    } else if (p.startsWith("file:")) {
                        cleanPath = p.substring(5);
                        // Extract root path (before the package structure)
                        if (cleanPath.contains("/" + marker)) {
                            cleanPath = cleanPath.substring(0, cleanPath.indexOf("/" + marker));
                        }
                    }
                    
                    if (cleanPath != null) {
                        try {
                            cleanPath = java.net.URLDecoder.decode(cleanPath, "UTF-8");
                            // Fix Windows path issues (leading slash)
                            if (cleanPath.startsWith("/") && cleanPath.contains(":") && cleanPath.substring(1, 3).matches("[A-Z]:")) {
                                cleanPath = cleanPath.substring(1);
                            }
                            
                            java.io.File f = new java.io.File(cleanPath);
                            if (f.exists()) {
                                String absolute = f.getAbsolutePath();
                                if (paths.add(absolute)) {
                                    logger.debug("Added discovered Episteme path: {}", absolute);
                                }
                            }
                        } catch (Exception e) { /* ignore */ }
                    }
                }
            }
        } catch (java.io.IOException e) {
            logger.error("Error scanning resources", e);
        }
    }

    private void scanDirectory(java.io.File directory, String packageName, String suffix, List<ClassInfo> results,
            Set<String> processed, ClassLoader classLoader) {
        java.io.File[] files = directory.listFiles();
        if (files == null)
            return;

        for (java.io.File file : files) {
            if (file.isDirectory()) {
                String newPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                scanDirectory(file, newPackage, suffix, results, processed, classLoader);
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                String fullClassName = packageName.isEmpty() ? className : packageName + "." + className;

                if (!fullClassName.startsWith("org.episteme."))
                    continue;

                boolean matchesSuffix = className.endsWith(suffix) && !className.equals(suffix);
                // Flexible matching for loaders and readers
                if ("Loader".equals(suffix) && className.endsWith("Reader")) {
                    matchesSuffix = true;
                }
                
                // Special case: if suffix is "Demo", also include "App" (for Killer Apps)
                if ("Demo".equals(suffix) && className.endsWith("App")) {
                    matchesSuffix = true;
                }
                boolean isDeviceRequested = "Device".equals(suffix);

                if (matchesSuffix || isDeviceRequested) {
                    if (processed.contains(fullClassName))
                        continue;

                    try {
                        Class<?> cls = Class.forName(fullClassName, false, classLoader);
                        if (!Modifier.isAbstract(cls.getModifiers()) && !Modifier.isInterface(cls.getModifiers())
                                && Modifier.isPublic(cls.getModifiers())) {

                            if (isDeviceRequested) {
                                if (!org.episteme.core.device.Device.class.isAssignableFrom(cls)) {
                                    if (!matchesSuffix)
                                        continue;
                                }
                            }

                            if ("Loader".equals(suffix)) {
                                if (!org.episteme.core.io.ResourceIO.class.isAssignableFrom(cls)) {
                                    // Relax check for classes ending in Loader or Reader even if not ResourceIO
                                    if (!className.endsWith("Loader") && !className.endsWith("Reader")) {
                                        continue;
                                    }
                                }
                            }

                            // Note: We don't check for DemoProvider here anymore as we rely on the caller
                            // or just class presence
                            // for legacy reasons. Real Viewers should use getProviders().

                            processed.add(fullClassName);
                            String desc = "Episteme " + (isDeviceRequested ? "Device" : suffix);
                            results.add(new ClassInfo(className, fullClassName, desc));
                        }
                    } catch (Throwable t) {
                        // Ignore
                    }
                }
            }
        }
    }

    private void scanJar(java.io.File jarFile, String suffix, List<ClassInfo> results, Set<String> processed, ClassLoader classLoader) {
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
            Enumeration<java.util.jar.JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                java.util.jar.JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String entryName = entry.getName();
                    String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                    if (!className.startsWith("org.episteme."))
                        continue;

                    boolean matchesSuffix = className.endsWith(suffix);
                    if ("Loader".equals(suffix) && className.endsWith("Reader")) {
                        matchesSuffix = true;
                    }
                    if ("Demo".equals(suffix) && className.endsWith("App")) {
                        matchesSuffix = true;
                    }
                    boolean isDeviceRequested = "Device".equals(suffix);

                    if (matchesSuffix || isDeviceRequested) {
                        if (processed.contains(className))
                            continue;
                        try {
                            Class<?> cls = Class.forName(className, false, classLoader);
                            if (!Modifier.isAbstract(cls.getModifiers()) && !Modifier.isInterface(cls.getModifiers())
                                    && Modifier.isPublic(cls.getModifiers())) {

                                if (isDeviceRequested) {
                                    if (!org.episteme.core.device.Device.class.isAssignableFrom(cls)) {
                                        if (!matchesSuffix)
                                            continue;
                                    }
                                }

                                if ("Loader".equals(suffix)) {
                                    if (!org.episteme.core.io.ResourceIO.class.isAssignableFrom(cls)) {
                                        if (!className.endsWith("Loader") && !className.endsWith("Reader")) {
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
        } catch (java.io.IOException e) {
            // Ignore
        }
    }

    public List<String> findResources(String pattern) {
        List<String> results = new ArrayList<>();
        String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(java.io.File.pathSeparator);

        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        if (tccl instanceof java.net.URLClassLoader) {
            java.net.URL[] urls = ((java.net.URLClassLoader) tccl).getURLs();
            List<String> combinedPaths = new ArrayList<>(Arrays.asList(paths));
            for (java.net.URL url : urls) {
                if ("file".equals(url.getProtocol())) {
                    try {
                        combinedPaths.add(new java.io.File(url.toURI()).getAbsolutePath());
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
            paths = combinedPaths.toArray(new String[0]);
        }

        for (String path : paths) {
            java.io.File file = new java.io.File(path);
            if (!file.exists()) continue;
            
            if (file.isDirectory()) {
                scanDirectoryForResources(file, "", pattern, results);
            } else if (file.getName().endsWith(".jar")) {
                if (!isSystemJar(file.getName())) {
                    scanJarForResources(file, pattern, results);
                }
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
    private void scanJarForResources(java.io.File jarFile, String pattern, List<String> results) {
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
            Enumeration<java.util.jar.JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                java.util.jar.JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().contains(pattern)) {
                    results.add(entry.getName());
                }
            }
        } catch (java.io.IOException e) {
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

