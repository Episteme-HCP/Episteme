package org.episteme.benchmarks.ui;

public class Launcher {
    public static void main(String[] args) {
        for (String arg : args) {
            if ("--cli".equals(arg)) {
                org.episteme.benchmarks.cli.BenchmarkCLI.main(args);
                return;
            }
        }
        EpistemeBenchmarkingApp.main(args);
    }
}
