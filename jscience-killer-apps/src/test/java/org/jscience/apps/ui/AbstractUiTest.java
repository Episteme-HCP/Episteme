/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.ui;

import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.TimeoutException;

/**
 * Base class for UI Tests using TestFX.
 */
public abstract class AbstractUiTest extends ApplicationTest {

    @BeforeEach
    public void setUpClass() throws Exception {
        // ApplicationTest.launch(getAppClass());
    }

    @Override
    public void start(Stage stage) throws Exception {
        // app.start(stage);
    }

    @AfterEach
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new javafx.scene.input.KeyCode[] {});
        release(new javafx.scene.input.MouseButton[] {});
    }

}
