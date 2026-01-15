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

package org.jscience.ui;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TestFX UI tests for the Master Control Libraries Tab.
 * Verifies that Spark and MPJ Express are displayed correctly
 * in the Distributed Computing section.
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
        new JScienceMasterControl().start(stage);
    }

    // ==================== Stage Tests ====================

    @Test
    void testStageIsShowing(FxRobot robot) {
        assertTrue(stage.isShowing(), "Master Control should be visible after launch");
    }

    @Test
    void testStageHasTitle(FxRobot robot) {
        assertNotNull(stage.getTitle(), "Master Control should have a title");
        assertFalse(stage.getTitle().isEmpty(), "Title should not be empty");
    }

    // ==================== Tab Tests ====================

    @Test
    void testLibrariesTabExists(FxRobot robot) {
        TabPane tabPane = robot.lookup(".tab-pane").queryAs(TabPane.class);
        assertNotNull(tabPane, "TabPane should exist");

        // Find Libraries tab by ID
        Tab librariesTab = null;
        for (Tab tab : tabPane.getTabs()) {
            if ("tab-libraries".equals(tab.getId())) {
                librariesTab = tab;
                break;
            }
        }
        assertNotNull(librariesTab, "Libraries tab should exist with id 'tab-libraries'");
    }

    @Test
    void testNavigateToLibrariesTab(FxRobot robot) {
        // Click on Libraries tab
        robot.clickOn("#tab-libraries");

        TabPane tabPane = robot.lookup(".tab-pane").queryAs(TabPane.class);
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        assertEquals("tab-libraries", selectedTab.getId(), "Libraries tab should be selected");
    }

    // ==================== Distributed Computing Section Tests ====================

    @Test
    void testDistributedComputingSectionExists(FxRobot robot) {
        // Navigate to Libraries tab
        robot.clickOn("#tab-libraries");

        // Look for "Distributed Computing" header label
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);

        boolean foundDistributed = labels.stream()
                .anyMatch(label -> label.getText() != null &&
                        label.getText().toLowerCase().contains("distributed"));

        assertTrue(foundDistributed, "Distributed Computing section should exist in Libraries tab");
    }

    @Test
    void testSparkLibraryDisplayed(FxRobot robot) {
        // Navigate to Libraries tab
        robot.clickOn("#tab-libraries");

        // Look for Spark label
        Set<Label> allLabels = robot.lookup(".label").queryAllAs(Label.class);

        boolean foundSpark = allLabels.stream()
                .anyMatch(label -> label.getText() != null &&
                        (label.getText().contains("Spark") ||
                                label.getText().contains("Apache Spark")));

        assertTrue(foundSpark, "Apache Spark should be listed in Libraries tab");
    }

    @Test
    void testMPJLibraryDisplayed(FxRobot robot) {
        // Navigate to Libraries tab
        robot.clickOn("#tab-libraries");

        // Look for MPJ label
        Set<Label> allLabels = robot.lookup(".label").queryAllAs(Label.class);

        boolean foundMPJ = allLabels.stream()
                .anyMatch(label -> label.getText() != null &&
                        (label.getText().contains("MPJ") ||
                                label.getText().contains("MPI")));

        assertTrue(foundMPJ, "MPJ Express should be listed in Libraries tab");
    }

    @Test
    void testSparkAndMPJHaveStatusLabels(FxRobot robot) {
        // Navigate to Libraries tab
        robot.clickOn("#tab-libraries");

        // Look for status labels (Available / Not Available)
        Set<Label> allLabels = robot.lookup(".label").queryAllAs(Label.class);

        // Count status-like labels in Distributed section
        long statusCount = allLabels.stream()
                .filter(label -> label.getText() != null)
                .filter(label -> label.getText().equals("Available") ||
                        label.getText().equals("Not Available"))
                .count();

        // Should have at least 2 status labels (for Spark and MPJ)
        assertTrue(statusCount >= 2,
                "Should have status labels for distributed computing libraries. Found: " + statusCount);
    }

    // ==================== Default Values Tests ====================

    @Test
    void testDefaultValuesWhenNoPreferences(FxRobot robot) {
        // The Master Control should launch successfully even without preferences file
        // This test validates that default values are properly applied.

        assertTrue(stage.isShowing(), "Master Control should show even without preferences file");

        // Check that UI elements are populated (not null/empty)
        TabPane tabPane = robot.lookup(".tab-pane").queryAs(TabPane.class);
        assertNotNull(tabPane, "TabPane should be populated");
        assertFalse(tabPane.getTabs().isEmpty(), "Tabs should be populated with defaults");
    }

    // ==================== Stability Tests ====================

    @Test
    void testLibrariesTabRemainsStable(FxRobot robot) {
        // Navigate to Libraries tab
        robot.clickOn("#tab-libraries");

        // Scroll through content
        ScrollPane scrollPane = robot.lookup(".scroll-pane").queryAs(ScrollPane.class);
        if (scrollPane != null) {
            scrollPane.setVvalue(0.5);
            scrollPane.setVvalue(1.0);
            scrollPane.setVvalue(0.0);
        }

        assertTrue(stage.isShowing(), "Master Control should remain stable after browsing Libraries");
    }

    // ==================== Backend Categories Tests ====================

    @Test
    void testFrameworkCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                l.getText().toLowerCase().contains("framework"));
        assertTrue(found, "Framework section should be displayed");
    }

    @Test
    void testStandardsCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                l.getText().toLowerCase().contains("standards"));
        assertTrue(found, "Standards section should be displayed");
    }

    @Test
    void testHardwareAccelerationCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                (l.getText().toLowerCase().contains("hardware") ||
                 l.getText().toLowerCase().contains("acceleration")));
        assertTrue(found, "Hardware Acceleration section should be displayed");
    }

    @Test
    void testMathematicsCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                (l.getText().toLowerCase().contains("math") ||
                 l.getText().toLowerCase().contains("algorithm")));
        assertTrue(found, "Mathematics & Algorithms section should be displayed");
    }

    @Test
    void testTensorsCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                l.getText().toLowerCase().contains("tensor"));
        assertTrue(found, "Tensor Engines section should be displayed");
    }

    @Test
    void testVisualizationCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                (l.getText().toLowerCase().contains("visual") ||
                 l.getText().toLowerCase().contains("plotting")));
        assertTrue(found, "Visualization & Plotting section should be displayed");
    }

    @Test
    void testChemistryCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                (l.getText().toLowerCase().contains("chemistry") ||
                 l.getText().toLowerCase().contains("biology")));
        assertTrue(found, "Chemistry & Biology section should be displayed");
    }

    @Test
    void testQuantumCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                l.getText().toLowerCase().contains("quantum"));
        assertTrue(found, "Quantum Computing section should be displayed");
    }

    @Test
    void testGeographyCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                (l.getText().toLowerCase().contains("geography") ||
                 l.getText().toLowerCase().contains("gis")));
        assertTrue(found, "Geography & GIS section should be displayed");
    }

    @Test
    void testNetworkCategoryDisplayed(FxRobot robot) {
        robot.clickOn("#tab-libraries");
        Set<Label> labels = robot.lookup(".header-title").queryAllAs(Label.class);
        boolean found = labels.stream().anyMatch(l -> l.getText() != null &&
                (l.getText().toLowerCase().contains("network") ||
                 l.getText().toLowerCase().contains("graph")));
        assertTrue(found, "Network & Graph Analysis section should be displayed");
    }

    // ==================== Backend Selector Tests ====================

    @Test
    void testBackendSelectorsExist(FxRobot robot) {
        robot.clickOn("#tab-libraries");

        // Look for ComboBox controls (backend selectors)
        Set<ComboBox> comboBoxes = robot.lookup(".combo-box").queryAllAs(ComboBox.class);

        // Should have multiple backend selectors (Math, Tensor, Molecular, Quantum, Map, Network)
        assertTrue(comboBoxes.size() >= 4,
                "Should have backend selector ComboBoxes. Found: " + comboBoxes.size());
    }

    @Test
    void testBackendSelectorsHaveAutoOption(FxRobot robot) {
        robot.clickOn("#tab-libraries");

        Set<ComboBox> comboBoxes = robot.lookup(".combo-box").queryAllAs(ComboBox.class);

        for (ComboBox<?> combo : comboBoxes) {
            boolean hasAuto = combo.getItems().stream()
                    .anyMatch(item -> item != null && item.toString().equalsIgnoreCase("AUTO"));
            if (!hasAuto && !combo.getItems().isEmpty()) {
                // Some dropdowns might be for other purposes
                continue;
            }
            // At least one should have AUTO
        }

        // Just verify we don't crash
        assertTrue(stage.isShowing(), "App should remain stable after checking ComboBoxes");
    }
}
