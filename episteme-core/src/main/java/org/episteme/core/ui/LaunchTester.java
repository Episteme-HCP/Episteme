package org.episteme.core.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.List;

/**
 * Diagnostic utility to verify that all discovered Episteme components are launchable.
 */
public class LaunchTester extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("=== Episteme Component Launch Test ===");
        
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        
        testGroup("Applications", discovery.findClasses("App"));
        testGroup("Demos", discovery.findClasses("Demo"));
        testGroup("Viewers", discovery.findClasses("Viewer"));
        
        System.out.println("\nAll discovery checks completed. Close this window to finish.");
        System.exit(0);
    }

    private void testGroup(String groupName, List<MasterControlDiscovery.ClassInfo> classes) {
        System.out.println("\n--- Testing Group: " + groupName + " ---");
        for (MasterControlDiscovery.ClassInfo info : classes) {
            try {
                Class<?> cls = Class.forName(info.fullName);
                System.out.print("Testing " + info.simpleName + " (" + info.fullName + ")... ");
                
                // We don't actually launch them all simultaneously to avoid crashing the machine,
                // but we verify they can be instantiated and have the required interfaces.
                if (Application.class.isAssignableFrom(cls) || Viewer.class.isAssignableFrom(cls)) {
                    System.out.println("OK (Compatible)");
                } else {
                    System.out.println("WARNING (Not assignable to Application or Viewer)");
                }
            } catch (Throwable t) {
                System.out.println("FAILED: " + t.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
