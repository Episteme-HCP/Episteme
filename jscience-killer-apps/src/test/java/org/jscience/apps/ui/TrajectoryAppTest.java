package org.jscience.apps.ui;

import javafx.stage.Stage;
import org.jscience.apps.physics.trajectory.InterplanetaryTrajectoryApp;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class TrajectoryAppTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // Ensure Locale is English for consistent assertions
        Locale.setDefault(Locale.ENGLISH);
        new InterplanetaryTrajectoryApp().start(stage);
    }

    @Test
    public void testUIComponentsVisible() {
        // Verify Title Label via text lookup (from i18n)
        verifyThat("🚀 Mission Configuration", isVisible());

        // Verify Buttons
        verifyThat("Calculate Trajectory", isVisible());

        // Verify Inputs
        verifyThat("Origin:", isVisible());
        verifyThat("Target:", isVisible());
    }
}
