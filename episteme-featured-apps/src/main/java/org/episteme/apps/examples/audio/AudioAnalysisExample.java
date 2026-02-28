/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.apps.examples.audio;

import org.episteme.core.media.AudioBackend;

import java.util.Scanner;

/**
 * Demonstrates audio analysis using the scientific TarsosDSP backend.
 */
public class AudioAnalysisExample {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   Episteme Audio Analysis Example");
        System.out.println("==========================================");

        String filePath;
        if (args.length > 0) {
            filePath = args[0];
        } else {
            System.out.println("Please enter the path to an audio file (wav/mp3):");
            try (Scanner scanner = new Scanner(System.in)) {
                filePath = scanner.nextLine().trim();
            }
        }
        
        if (filePath.isEmpty()) {
            System.out.println("No file provided. Exiting.");
            return;
        }

        try {
            // 1. Manually instantiate TarsosBackend (or look it up via ServiceLoader if implemented)
            // Ideally: AudioBackend backend = BackendDiscovery.getAudioBackend("tarsos");
            // For this example, we use the class directly if available.
            Class<?> clazz = Class.forName("org.episteme.core.media.backends.TarsosBackend");
            AudioBackend backend = (AudioBackend) clazz.getDeclaredConstructor().newInstance();
            
            if (!backend.isAvailable()) {
                System.err.println("TarsosDSP libraries not found. Please ensure dependencies are loaded.");
                return;
            }

            System.out.println("Backend: " + backend.getName());
            
            // 2. Load Audio
            System.out.println("Loading: " + filePath);
            backend.load(filePath);
            
            // 3. Play and Analyze
            System.out.println("Playing...");
            backend.play();
            
            // 4. Analysis Loop
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < 10000) { // Run for 10 seconds
                float[] spectrum = backend.getSpectrum();
                double time = backend.getTime();
                
                // Visualizer (ASCII)
                System.out.printf("\rTime: %.2fs | Spectrum Peak: %.4f | ", time, max(spectrum));
                printBar(max(spectrum));
                
                Thread.sleep(100);
            }
            
            System.out.println("\nStopping...");
            backend.stop();
            System.exit(0); // Force exit as Tarsos thread might keep JVM alive

        } catch (ClassNotFoundException e) {
            System.err.println("TarsosBackend class not found. Check episteme-core dependencies.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static float max(float[] arr) {
        float m = 0;
        for(float v : arr) if(v>m) m=v;
        return m;
    }
    
    private static void printBar(float val) {
        int len = (int) (val * 50); // Scale
        if (len > 50) len = 50;
        for(int i=0; i<len; i++) System.out.print("#");
    }
}
