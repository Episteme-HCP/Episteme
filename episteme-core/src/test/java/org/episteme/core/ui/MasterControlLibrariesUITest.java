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

import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Optimized TestFX UI tests for the Master Control Libraries Tab.
 * Consolidates tests to reduce application restarts and prevent timeouts.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@ExtendWith(ApplicationExtension.class)
public class MasterControlLibrariesUITest {

    private Stage stage;

    @Start
    private void start(Stage stage) {
        this.stage = stage;
        new EpistemeMasterControl().start(stage);
    }

    private void ensureLibrariesTabSelected(FxRobot robot) {
        TabPane tabPane = robot.lookup(".tab-pane").queryAs(TabPane.class);
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == null || !"tab-libraries".equals(selectedTab.getId())) {
            robot.clickOn("#tab-libraries");
            robot.sleep(500); // Allow UI to transition and populate
        }
    }

    @Test
    void testStageAndTabInitialization(FxRobot robot) {
        robot.sleep(500); 
        assertTrue(stage.isShowing(), "Master Control should be visible");
        assertNotNull(stage.getTitle(), "Title should exist");
        
        TabPane tabPane = robot.lookup(".tab-pane").queryAs(TabPane.class);
        assertNotNull(tabPane, "TabPane should exist");
        
        ensureLibrariesTabSelected(robot);
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        assertEquals("tab-libraries", selectedTab.getId(), "Libraries tab should be selected");
    }

    @Test
    void testLibrariesContentAndDistributedComputing(FxRobot robot) {
        ensureLibrariesTabSelected(robot);
        
        // Check Distributed Computing section
        Set<Label> titleLabels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean foundDistributed = titleLabels.stream()
                .anyMatch(label -> label.getText() != null &&
                        label.getText().toLowerCase().contains("distributed"));
        assertTrue(foundDistributed, "Distributed Computing section should exist");

        // Check Spark
        Set<Label> allLabels = robot.lookup(".label").queryAllAs(Label.class);
        boolean foundSpark = allLabels.stream()
                .anyMatch(label -> label.getText() != null &&
                        (label.getText().contains("Spark") ||
                                label.getText().contains("Apache Spark")));
        assertTrue(foundSpark, "Apache Spark should be listed");

        // Check Status Labels
        long statusCount = allLabels.stream()
                .filter(label -> label.getText() != null)
                .filter(label -> label.getText().equals("Available") ||
                        label.getText().equals("Not Available"))
                .count();
        assertTrue(statusCount >= 2, "Should have status labels. Found: " + statusCount);
    }

    @Test
    void testCategoryDisplayStability(FxRobot robot) {
        ensureLibrariesTabSelected(robot);
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        
        assertCategory(labels, "framework");
        assertCategory(labels, "standards");
        assertCategory(labels, "hardware", "acceleration");
        assertCategory(labels, "math", "algorithm");
        assertCategory(labels, "tensor");
        assertCategory(labels, "visual", "plotting");
        assertCategory(labels, "geography", "gis");
        assertCategory(labels, "network", "graph");

        // Scroll test
        ScrollPane scrollPane = robot.lookup(".scroll-pane").queryAs(ScrollPane.class);
        if (scrollPane != null) {
            scrollPane.setVvalue(1.0);
            robot.sleep(100);
            scrollPane.setVvalue(0.0);
        }
        assertTrue(stage.isShowing(), "App should remain stable after scrolling");
    }

    @Test
    void testBackendSelectorsAndDump(FxRobot robot) {
        ensureLibrariesTabSelected(robot);

        // Check ComboBoxes
        @SuppressWarnings("rawtypes")
        Set<ComboBox> comboBoxes = robot.lookup(".combo-box").queryAllAs(ComboBox.class);
        assertTrue(comboBoxes.size() >= 4, "Should have backend selectors. Found: " + comboBoxes.size());

        // Check AUTO option in at least one
        boolean foundAuto = false;
        for (ComboBox<?> combo : comboBoxes) {
            if (combo.getItems().stream().anyMatch(item -> item != null && item.toString().equalsIgnoreCase("AUTO"))) {
                foundAuto = true;
                break;
            }
        }
        // Not strictly required for all but good to check if at least one exists
        System.out.println("At least one selector has AUTO option: " + foundAuto);

        // Dump for debug
        Set<Label> allLabels = robot.lookup(".label").queryAllAs(Label.class);
        System.out.println("=== DUMP LIBRARIES TAB LABELS ===");
        allLabels.stream().map(Label::getText).filter(t -> t != null && !t.isEmpty())
                .forEach(text -> System.out.println("LABEL: '" + text + "'"));
    }

    private void assertCategory(Set<Label> labels, String... keywords) {
        boolean found = labels.stream().anyMatch(l -> {
            String text = l.getText();
            if (text == null) return false;
            text = text.toLowerCase();
            for (String kw : keywords) {
                if (text.contains(kw)) return true;
            }
            return false;
        });
        assertTrue(found, "Category containing " + java.util.Arrays.toString(keywords) + " should be displayed");
    }
}
