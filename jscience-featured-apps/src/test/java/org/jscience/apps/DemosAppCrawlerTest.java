/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.apps;

import javafx.scene.Node;
import javafx.scene.Parent;

import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jscience.ui.JScienceDemosApp;
import org.jscience.ui.i18n.I18n;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * UI Crawler for JScienceDemosApp (run-demos.bat).
 * Iterates through all discovered demos and checks for missing translations.
 */
@ExtendWith(ApplicationExtension.class)
public class DemosAppCrawlerTest {

    private Stage mainStage;
    private final List<String> suspiciousTexts = new ArrayList<>();
    private static final String SCREENSHOT_DIR = "target/screenshots/demos_app/";

    @BeforeAll
    static void setupHeadless() {
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("testfx.headless", "true");

        new File(SCREENSHOT_DIR).mkdirs();
        // Force English for stable lookups
        Locale.setDefault(Locale.ENGLISH);
        I18n.getInstance().setLocale(Locale.ENGLISH);
    }

    @Start
    private void start(Stage stage) {
        this.mainStage = stage;
        I18n.setTestMode(true);
        new JScienceDemosApp().start(stage);
    }

    @AfterEach
    void tearDown() {
        // Log suspicious texts
        try (FileWriter writer = new FileWriter("target/demos_app_translation_report.txt")) {
            writer.write("Suspicious Translations (Wrapped in !!!):\n");
            for (String text : suspiciousTexts) {
                writer.write(text + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void crawlDemosApp(FxRobot robot) throws Exception {
        assertNotNull(mainStage);
        WaitForAsyncUtils.waitForFxEvents();

        // 1. Capture Main App
        captureScreenshot(robot, "main_app_listing");
        crawlNode(mainStage.getScene().getRoot());

        // 2. Iterate through all launch buttons
        Set<Button> launchButtons = robot.lookup(".launch-button").queryAllAs(Button.class);
        System.out.println("Found " + launchButtons.size() + " launch buttons.");

        // Instead of clicking directly (which might launch blocking stages),
        // we'll find the text of the demos and click them by name to be sure.
        // Set<String> demoNames = new HashSet<>();
        // Find labels near the buttons or just get the buttons themselves
        // In JScienceDemosApp, the buttons are "Launch" and the demo name is in a label
        // next to it.

        // Let's just find all buttons and click them one by one.
        // We need to re-lookup because clicking might change the scene
        // (though in JScienceDemosApp it opens new windows)

        for (int i = 0; i < launchButtons.size(); i++) {
            // Re-find buttons to avoid StaleElementReference equivalent
            List<Button> currentButtons = robot.lookup(".launch-button").queryAllAs(Button.class)
                    .stream().collect(Collectors.toList());

            if (i >= currentButtons.size())
                break;

            Button btn = currentButtons.get(i);
            // Get demo name for logging/filename
            String demoName = "demo_" + i;
            Parent parent = btn.getParent();
            if (parent instanceof javafx.scene.layout.HBox) {
                for (Node child : ((javafx.scene.layout.HBox) parent).getChildren()) {
                    if (child instanceof javafx.scene.layout.VBox) {
                        for (Node vchild : ((javafx.scene.layout.VBox) child).getChildren()) {
                            if (vchild instanceof Label) {
                                demoName = ((Label) vchild).getText().replaceAll("[^a-zA-Z0-9]", "_");
                                break;
                            }
                        }
                    }
                }
            }

            System.out.println("Testing Demo: " + demoName);
            robot.interact(() -> btn.fire());
            WaitForAsyncUtils.waitForFxEvents();

            // Look for new windows
            Optional<Window> newWindow = robot.listWindows().stream()
                    .filter(w -> w != mainStage && w.isShowing())
                    .findFirst();

            if (newWindow.isPresent()) {
                Stage demoStage = (Stage) newWindow.get();
                crawlNode(demoStage.getScene().getRoot());
                captureScreenshot(robot, "demo_" + demoName);

                // Close the demo stage
                robot.interact(() -> demoStage.close());
                WaitForAsyncUtils.waitForFxEvents();
            } else {
                System.out.println("No new window detected for " + demoName);
            }
        }
    }

    private void crawlNode(Node node) {
        if (node instanceof Labeled) {
            checkText(((Labeled) node).getText());
        } else if (node instanceof TextInputControl) {
            checkText(((TextInputControl) node).getPromptText());
        } else if (node instanceof TabPane) {
            for (Tab tab : ((TabPane) node).getTabs()) {
                checkText(tab.getText());
                if (tab.getContent() != null)
                    crawlNode(tab.getContent());
            }
        } else if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                crawlNode(child);
            }
        }
    }

    private void checkText(String text) {
        if (text != null && text.contains("!!!")) {
            suspiciousTexts.add(text);
        }
    }

    private void captureScreenshot(FxRobot robot, String name) {
        try {
            // Use AWT Robot for full screen headless capture which is more reliable in
            // Monocle
            Robot awtRobot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = awtRobot.createScreenCapture(screenRect);
            ImageIO.write(screenFullImage, "png", new File(SCREENSHOT_DIR + name + ".png"));
        } catch (Exception ex) {
            System.err.println("Failed to capture screenshot: " + name);
        }
    }
}


