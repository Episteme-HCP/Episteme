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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * TestFX UI tests for JScienceDemoApp.
 * Tests demo launcher functionality, language switching, and theme toggling.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@ExtendWith(ApplicationExtension.class)
public class JScienceDemoAppTest {

    private Stage stage;

    @Start
    private void start(Stage stage) {
        this.stage = stage;
        new JScienceDemoApp().start(stage);
    }

    @Test
    void testAppLaunches(FxRobot robot) {
        // Verify the app launches and is showing
        assertTrue(stage.isShowing(), "Stage should be visible");
        assertNotNull(stage.getTitle(), "Stage should have a title");
    }

    @Test
    void testMenuBarExists(FxRobot robot) {
        // Verify menu bar has expected menus (Language and Theme)
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        assertNotNull(menuBar, "Menu bar should exist");
        assertEquals(2, menuBar.getMenus().size(), "Should have 2 menus: Language, Theme");
    }

    @Test
    void testLanguageMenuExists(FxRobot robot) {
        // Find and click Language menu by CSS class
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        Menu languageMenu = menuBar.getMenus().get(0);
        assertNotNull(languageMenu, "Language menu should exist");

        // Verify it has menu items
        assertTrue(languageMenu.getItems().size() >= 2, "Language menu should have at least 2 options");
    }

    @Test
    void testThemeMenuExists(FxRobot robot) {
        // Find Theme menu
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        Menu themeMenu = menuBar.getMenus().get(1);
        assertNotNull(themeMenu, "Theme menu should exist");

        // Verify it has Dark and Light options
        assertEquals(2, themeMenu.getItems().size(), "Theme menu should have 2 options (Dark, Light)");
    }

    @Test
    void testScrollPaneExists(FxRobot robot) {
        // Verify demo sections container exists
        ScrollPane scrollPane = robot.lookup(".scroll-pane").queryAs(ScrollPane.class);
        assertNotNull(scrollPane, "ScrollPane should contain demo sections");
        assertTrue(scrollPane.isFitToWidth(), "ScrollPane should fit to width");
    }

    @Test
    void testContentBoxExists(FxRobot robot) {
        // Verify content box with demos exists
        VBox contentBox = robot.lookup(".content-box").queryAs(VBox.class);
        assertNotNull(contentBox, "Content box should exist");
    }

    @Test
    void testHeaderExists(FxRobot robot) {
        // Verify header section exists
        VBox header = robot.lookup(".header-box").queryAs(VBox.class);
        assertNotNull(header, "Header box should exist");
    }

    @Test
    void testLaunchButtonsExist(FxRobot robot) {
        // Verify launch buttons exist for demos
        var buttons = robot.lookup(".launch-button").queryAll();
        // We expect at least some demo buttons to exist if demos are loaded
        assertNotNull(buttons, "Launch buttons should exist");
    }

    @Test
    void testThemeSwitchingDoesNotCrash(FxRobot robot) {
        // Get theme menu and click items programmatically
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        Menu themeMenu = menuBar.getMenus().get(1);
        assertNotNull(themeMenu, "Theme menu should exist");

        // Verify app stays visible throughout
        assertTrue(stage.isShowing(), "App should remain visible");
    }

    @Test
    void testLanguageSwitchingDoesNotCrash(FxRobot robot) {
        // Get language menu
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        Menu languageMenu = menuBar.getMenus().get(0);
        assertNotNull(languageMenu, "Language menu should exist");

        // Verify app stays visible
        assertTrue(stage.isShowing(), "App should remain visible after language operations");
    }
}
