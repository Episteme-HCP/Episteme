/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.ui;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.jscience.apps.biology.CrisprDesignApp;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class UiSmokeTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new CrisprDesignApp().start(stage);
    }

    @Test
    public void testAppLaunch() {
        // Verify title or main components
        // Note: Title verification via stage is harder in TestFX without direct Stage
        // access in @Test
        // But we can check buttons or labels visible
        verifyThat(".button", (Node n) -> n.isVisible());
    }
}
