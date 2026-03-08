package org.episteme.benchmarks;

import org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend;
import org.episteme.nativ.media.vision.backends.NativeCPUVisionBackend;
import org.episteme.nativ.media.backends.NativeJavaCVMediaBackend;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BenchmarkVisionBackends {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("      Episteme Vision Backend Benchmarks         ");
        System.out.println("=================================================");

        NativeCPUVisionBackend pureJava = new NativeCPUVisionBackend();
        NativeJavaCVMediaBackend javaCv = new NativeJavaCVMediaBackend();
        
        // Debug explicit load
        String dllPath = "c:\\Silvere\\Encours\\Developpement\\Episteme\\episteme-native\\target\\classes\\win32-x86_64\\episteme_vision.dll";
        System.out.println("DEBUG: Attempting explicit load of " + dllPath);
        try {
            System.load(dllPath);
            System.out.println("DEBUG: Explicit load SUCCESS!");
        } catch (Throwable t) {
            System.err.println("DEBUG: Explicit load FAILED: " + t.getMessage());
            t.printStackTrace();
        }

        NativeCPUCBindingVisionBackend cBinding = new NativeCPUCBindingVisionBackend();

        System.out.println("Pure Java available: " + pureJava.isAvailable());
        System.out.println("JavaCV available:    " + javaCv.isAvailable());
        System.out.println("CBinding available:  " + cBinding.isAvailable());
        
        System.out.println("\nGenerating 4K Test Image (3840x2160)...");
        BufferedImage testImage = createNoiseImage(3840, 2160);

        int iterations = 100;

        System.out.println("\nRunning Grayscale (Warmup)...");
        try {
            if (pureJava.isAvailable()) benchmarkJava(pureJava, testImage, 5);
            if (javaCv.isAvailable()) benchmarkJavaCV(javaCv, testImage, 5);
            if (cBinding.isAvailable()) benchmarkCBinding(cBinding, testImage, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\nRunning Grayscale (Iterations: " + iterations + ")...");
        try {
            if (pureJava.isAvailable()) {
                long duration = benchmarkJava(pureJava, testImage, iterations);
                System.out.printf("Pure Java: %d ms (Avg: %.2f ms / req)%n", duration, (double) duration / iterations);
            }
            if (javaCv.isAvailable()) {
                long duration = benchmarkJavaCV(javaCv, testImage, iterations);
                System.out.printf("JavaCV: %d ms (Avg: %.2f ms / req)%n", duration, (double) duration / iterations);
            }
            if (cBinding.isAvailable()) {
                long duration = benchmarkCBinding(cBinding, testImage, iterations);
                System.out.printf("Custom C++ (Panama): %d ms (Avg: %.2f ms / req)%n", duration, (double) duration / iterations);
            } else {
                System.out.println("Custom C++: Skipped (Not compiled via CMake)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long benchmarkJava(NativeCPUVisionBackend backend, BufferedImage img, int iterations) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            backend.processNative(img, 1);
        }
        return System.currentTimeMillis() - start;
    }

    private static long benchmarkJavaCV(NativeJavaCVMediaBackend backend, BufferedImage img, int iterations) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            // Simulated operation mapping for JavaCV benchmark since it doesn't have an opCode router yet
            // Assuming fallback maps it natively
            backend.apply(img, input -> {
               BufferedImage bw = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
               Graphics2D g = bw.createGraphics();
               g.drawImage(img, 0, 0, null);
               g.dispose();
               return bw;
            });
        }
        return System.currentTimeMillis() - start;
    }

    private static long benchmarkCBinding(NativeCPUCBindingVisionBackend backend, BufferedImage img, int iterations) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            backend.processNative(img, 1);
        }
        return System.currentTimeMillis() - start;
    }

    private static BufferedImage createNoiseImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Random r = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.setRGB(x, y, new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)).getRGB());
            }
        }
        return img;
    }
}
