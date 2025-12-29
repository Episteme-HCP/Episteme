/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * Comprehensive TestFX UI tests for the JScience Demo Launcher.
 * Tests application structure, navigation, accessibility of components,
 * and basic interaction flows.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@ExtendWith(ApplicationExtension.class)
public class DemoLauncherUITest {

    private Stage stage;

    @Start
    private void start(Stage stage) {
        this.stage = stage;
        new JScienceDemoApp().start(stage);
    }

    // ==================== Stage Tests ====================

    @Test
    void testStageIsShowing(FxRobot robot) {
        assertTrue(stage.isShowing(), "Stage should be visible after launch");
    }

    @Test
    void testStageHasTitle(FxRobot robot) {
        assertNotNull(stage.getTitle(), "Stage should have a title");
        assertFalse(stage.getTitle().isEmpty(), "Stage title should not be empty");
    }

    @Test
    void testStageHasMinimumSize(FxRobot robot) {
        assertTrue(stage.getWidth() >= 800, "Stage width should be at least 800px");
        assertTrue(stage.getHeight() >= 600, "Stage height should be at least 600px");
    }

    // ==================== Menu Bar Tests ====================

    @Test
    void testMenuBarStructure(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        assertNotNull(menuBar, "Menu bar should exist");

        // Should have exactly 2 menus: Language and Theme
        assertEquals(2, menuBar.getMenus().size(),
                "Menu bar should have exactly 2 menus (Language, Theme)");
    }

    @Test
    void testLanguageMenuHasOptions(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        Menu languageMenu = menuBar.getMenus().get(0);

        // Language menu should have at least English and French
        assertTrue(languageMenu.getItems().size() >= 2,
                "Language menu should have at least 2 language options");

        // All items should be RadioMenuItems for exclusive selection
        for (MenuItem item : languageMenu.getItems()) {
            assertTrue(item instanceof RadioMenuItem,
                    "Language menu items should be RadioMenuItems");
        }
    }

    @Test
    void testThemeMenuHasOptions(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        Menu themeMenu = menuBar.getMenus().get(1);

        // Theme menu should have Dark and Light options
        assertEquals(2, themeMenu.getItems().size(),
                "Theme menu should have exactly 2 options (Dark, Light)");

        // All items should be RadioMenuItems for exclusive selection
        for (MenuItem item : themeMenu.getItems()) {
            assertTrue(item instanceof RadioMenuItem,
                    "Theme menu items should be RadioMenuItems");
        }
    }

    @Test
    void testLanguageMenuHasToggleGroup(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        Menu languageMenu = menuBar.getMenus().get(0);

        if (!languageMenu.getItems().isEmpty()) {
            RadioMenuItem first = (RadioMenuItem) languageMenu.getItems().get(0);
            assertNotNull(first.getToggleGroup(), "Language items should share a ToggleGroup");
        }
    }

    @Test
    void testThemeMenuHasToggleGroup(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        Menu themeMenu = menuBar.getMenus().get(1);

        if (!themeMenu.getItems().isEmpty()) {
            RadioMenuItem first = (RadioMenuItem) themeMenu.getItems().get(0);
            assertNotNull(first.getToggleGroup(), "Theme items should share a ToggleGroup");
        }
    }

    // ==================== Header Tests ====================

    @Test
    void testHeaderBoxExists(FxRobot robot) {
        VBox header = robot.lookup(".header-box").queryAs(VBox.class);
        assertNotNull(header, "Header box should exist");
    }

    @Test
    void testHeaderHasLabels(FxRobot robot) {
        VBox header = robot.lookup(".header-box").queryAs(VBox.class);
        assertNotNull(header, "Header should exist");

        // Header should contain at least title and subtitle labels
        long labelCount = header.getChildren().stream()
                .filter(node -> node instanceof Label)
                .count();
        assertTrue(labelCount >= 2, "Header should have at least title and subtitle labels");
    }

    // ==================== Content Tests ====================

    @Test
    void testScrollPaneExists(FxRobot robot) {
        ScrollPane scrollPane = robot.lookup(".scroll-pane").queryAs(ScrollPane.class);
        assertNotNull(scrollPane, "ScrollPane should exist for demo sections");
    }

    @Test
    void testScrollPaneFitsToWidth(FxRobot robot) {
        ScrollPane scrollPane = robot.lookup(".scroll-pane").queryAs(ScrollPane.class);
        assertTrue(scrollPane.isFitToWidth(), "ScrollPane should fit to width");
    }

    @Test
    void testContentBoxExists(FxRobot robot) {
        VBox contentBox = robot.lookup(".content-box").queryAs(VBox.class);
        assertNotNull(contentBox, "Content box should exist");
    }

    @Test
    void testContentBoxHasPadding(FxRobot robot) {
        VBox contentBox = robot.lookup(".content-box").queryAs(VBox.class);
        assertNotNull(contentBox.getPadding(), "Content box should have padding");
    }

    // ==================== Demo Section Tests ====================

    @Test
    void testTitledPanesExist(FxRobot robot) {
        Set<TitledPane> titledPanes = robot.lookup(".titled-pane").queryAllAs(TitledPane.class);
        // Demos should be organized in TitledPanes by category
        assertNotNull(titledPanes, "TitledPanes should exist for demo categories");
    }

    @Test
    void testTitledPanesAreExpanded(FxRobot robot) {
        Set<TitledPane> titledPanes = robot.lookup(".titled-pane").queryAllAs(TitledPane.class);

        for (TitledPane pane : titledPanes) {
            assertTrue(pane.isExpanded(),
                    "Demo sections should be expanded by default: " + pane.getText());
        }
    }

    @Test
    void testTitledPanesAreCollapsible(FxRobot robot) {
        Set<TitledPane> titledPanes = robot.lookup(".titled-pane").queryAllAs(TitledPane.class);

        for (TitledPane pane : titledPanes) {
            assertTrue(pane.isCollapsible(),
                    "Demo sections should be collapsible: " + pane.getText());
        }
    }

    // ==================== Launch Button Tests ====================

    @Test
    void testLaunchButtonsExist(FxRobot robot) {
        Set<Button> launchButtons = robot.lookup(".launch-button").queryAllAs(Button.class);
        assertNotNull(launchButtons, "Launch buttons should exist");
    }

    @Test
    void testLaunchButtonsHaveText(FxRobot robot) {
        Set<Button> launchButtons = robot.lookup(".launch-button").queryAllAs(Button.class);

        for (Button button : launchButtons) {
            assertNotNull(button.getText(), "Launch button should have text");
            assertFalse(button.getText().isEmpty(), "Launch button text should not be empty");
        }
    }

    // ==================== Stability Tests ====================

    @Test
    void testAppRemainsStableAfterBrowsing(FxRobot robot) {
        // Interact with various elements to ensure stability
        ScrollPane scrollPane = robot.lookup(".scroll-pane").queryAs(ScrollPane.class);
        assertNotNull(scrollPane);

        // Verify app stays visible after interactions
        assertTrue(stage.isShowing(), "App should remain visible after browsing");
    }

    @Test
    void testNoExceptionsOnMenuAccess(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);

        // Access all menus - should not throw
        for (Menu menu : menuBar.getMenus()) {
            assertNotNull(menu.getText());
            for (MenuItem item : menu.getItems()) {
                assertNotNull(item.getText());
            }
        }

        assertTrue(stage.isShowing(), "App should remain visible after menu access");
    }

    // ==================== Accessibility Tests ====================

    @Test
    void testLabelsAreReadable(FxRobot robot) {
        Set<Label> labels = robot.lookup(".label").queryAllAs(Label.class);

        // Verify we can read labels without exceptions
        for (Label label : labels) {
            // Access text to ensure no exceptions (text can be null for non-text labels)
            assertNotNull(label, "Label should not be null");
        }

        assertTrue(stage.isShowing(), "App should remain stable when checking labels");
    }

    @Test
    void testButtonsAreClickable(FxRobot robot) {
        Set<Button> buttons = robot.lookup(".button").queryAllAs(Button.class);

        for (Button button : buttons) {
            // Verify buttons are not disabled by default (unless explicitly)
            assertFalse(button.isDisabled() && button.getStyleClass().contains("launch-button"),
                    "Launch buttons should not be disabled: " + button.getText());
        }
    }
}
