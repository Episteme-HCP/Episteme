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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Portable launcher for JScience.
 * Usage: java Launcher.java [CPU|GPU|AUTO]
 */
public class Launcher {
    public static void main(String[] args) throws Exception {
        String mode = "AUTO";
        if (args.length > 0) {
            mode = args[0].toUpperCase();
        }

        String classpath = "target/classes" + File.pathSeparator + "target/test-classes";
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add("-Dorg.jscience.compute.mode=" + mode);
        command.add("org.jscience.JScience");

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        Process process = pb.start();
        process.waitFor();
    }
}
