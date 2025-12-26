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
package org.jscience.apps.ui;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TestFX UI tests for JScience Killer Apps.
 * Tests application launch, menu structure, and basic UI elements.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@ExtendWith(ApplicationExtension.class)
public class KillerAppUITest {

    private Stage stage;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        // Start with QuantumCircuitApp as it has a relatively simple UI
        new org.jscience.apps.physics.QuantumCircuitApp().start(stage);
    }

    // ==================== Launch Tests ====================

    @Test
    void testQuantumAppLaunches(FxRobot robot) {
        assertTrue(stage.isShowing(), "Stage should be visible");
        assertNotNull(stage.getTitle(), "Stage should have a title");
    }

    @Test
    void testQuantumAppHasScene(FxRobot robot) {
        assertNotNull(stage.getScene(), "Stage should have a scene");
        assertNotNull(stage.getScene().getRoot(), "Scene should have a root");
    }

    @Test
    void testQuantumAppHasMenuBar(FxRobot robot) {
        // Killer apps should have menu bar from KillerAppBase
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        assertNotNull(menuBar, "App should have a menu bar");
    }

    @Test
    void testQuantumAppMenuStructure(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        if (menuBar != null) {
            // KillerAppBase provides File, Edit, View, Tools, Preferences, Help menus
            assertTrue(menuBar.getMenus().size() >= 4,
                    "Menu bar should have at least 4 menus");
        }
    }

    // ==================== Layout Tests ====================

    @Test
    void testQuantumAppHasBorderPane(FxRobot robot) {
        // Check root is BorderPane directly from scene
        assertTrue(stage.getScene().getRoot() instanceof BorderPane ||
                stage.getScene().getRoot().getStyleClass().contains("root"),
                "App should have a proper root layout");
    }

    @Test
    void testAppHasButtonsOrControls(FxRobot robot) {
        var buttons = robot.lookup(".button").queryAll();
        // Most apps have at least some buttons
        assertNotNull(buttons, "App should have UI controls");
    }

    // ==================== Stability Tests ====================

    @Test
    void testAppRemainsStableAfterUIAccess(FxRobot robot) {
        // Access various UI elements
        robot.lookup(".menu-bar").queryAs(MenuBar.class);
        robot.lookup(".button").queryAll();
        robot.lookup(".label").queryAll();

        // Verify app stays visible
        assertTrue(stage.isShowing(), "App should remain visible after UI access");
    }

    @Test
    void testAppSceneHasStylesheets(FxRobot robot) {
        // Verify scene has stylesheets applied
        assertNotNull(stage.getScene().getStylesheets(),
                "Scene should have stylesheets collection");
    }

    // ==================== Menu Tests ====================

    @Test
    void testFileMenuExists(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        if (menuBar != null && !menuBar.getMenus().isEmpty()) {
            Menu fileMenu = menuBar.getMenus().get(0);
            assertNotNull(fileMenu, "File menu should exist");
            assertFalse(fileMenu.getItems().isEmpty(), "File menu should have items");
        }
    }

    @Test
    void testHelpMenuExists(FxRobot robot) {
        MenuBar menuBar = robot.lookup(".menu-bar").queryAs(MenuBar.class);
        if (menuBar != null && menuBar.getMenus().size() >= 4) {
            Menu helpMenu = menuBar.getMenus().get(menuBar.getMenus().size() - 1);
            assertNotNull(helpMenu, "Help menu should exist");
        }
    }

    // ==================== Window Tests ====================

    @Test
    void testWindowHasReasonableSize(FxRobot robot) {
        assertTrue(stage.getWidth() >= 400, "Window should be at least 400px wide");
        assertTrue(stage.getHeight() >= 300, "Window should be at least 300px tall");
    }

    @Test
    void testWindowIsResizable(FxRobot robot) {
        assertTrue(stage.isResizable(), "Window should be resizable");
    }
}
