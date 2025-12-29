package org.jscience.ui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Generic Launcher for JScience Applications and Demos.
 * Handles:
 * 1. JavaFX Applications (extends Application)
 * 2. DemoProviders (implements DemoProvider)
 * 3. Standard Main classes (public static void main)
 */
public class AppLauncher extends Application {

    private static DemoProvider demoProvider;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java org.jscience.ui.AppLauncher <ClassName> [Args...]");
            System.exit(1);
        }

        String targetClassName = args[0];
        String[] appArgs = Arrays.copyOfRange(args, 1, args.length);

        try {
            Class<?> clazz = Class.forName(targetClassName);

            // 1. If it's a JavaFX Application
            if (Application.class.isAssignableFrom(clazz)) {
                // Launch it directly
                Application.launch(clazz.asSubclass(Application.class), appArgs);
                return;
            }

            // 2. If it's a DemoProvider
            if (DemoProvider.class.isAssignableFrom(clazz)) {
                demoProvider = (DemoProvider) clazz.getDeclaredConstructor().newInstance();
                // Launch this wrapper app which will show the demo
                launch(appArgs);
                return;
            }

            // 3. Try to find a main method
            try {
                Method mainMethod = clazz.getMethod("main", String[].class);
                mainMethod.invoke(null, (Object) appArgs);
            } catch (NoSuchMethodException e) {
                System.err.println("Error: Class " + targetClassName
                        + " is not an Application, DemoProvider, and has no main method.");
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (demoProvider != null) {
            demoProvider.show(primaryStage);
        } else {
            // Should not happen if logic is correct
            System.err.println("No DemoProvider loaded.");
            System.exit(0);
        }
    }
}
